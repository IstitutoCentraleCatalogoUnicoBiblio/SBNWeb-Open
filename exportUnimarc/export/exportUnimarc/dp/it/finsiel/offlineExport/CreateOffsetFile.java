/***
11/12/2009 15.35 	Causa problemi record spezzati (newline in tbf_biblioteca) devo gestire un record su piu righe.
					Questo causa un aggravio nelle performance.

09/12/2010 Retrocompatibilita' con 1.4.2

*/
package it.finsiel.offlineExport;

import it.finsiel.misc.DateUtil;
import it.finsiel.misc.MiscString;
import it.finsiel.misc.MiscStringTokenizer;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

// test 

public class CreateOffsetFile {
	final public int DB_SBNWEB = 1;
	final public int DB_INDICE = 2;
	
	final public int BID_LENGTH = 10;
	final public int KEYLOC_LENGTH = 9;
	String dataFilenameIn;
	String offsetFilenameOut1;
	String offsetFilenameOut2;
	boolean keyLocDone = false;
	// RandomAccessFile fh1 = null;
	FileInputStream fis;
	int keyStart, keyEnd;
	int keyLocStart, keyLocEnd; // 
	int keyPos, recPos;
	long rowCtr = 0;
	StringBuffer sb;
	StringBuffer sbKeyLoc;
	BufferedWriter offsetBufferredWriter1, offsetBufferredWriter2;
	int offsetLength;
	long offset = 0;
	long lastOffset = 0;
	long lastKeylocOffset = 0;

	String lastBid = "";
	String lastMid = "";
	String lastIdPersonaggio = "";
	String lastIdLinkMultim = "";

	String lastWrittenBid = "";
	
	
	Pattern p = Pattern.compile("\\Q&$%\\E", Pattern.CASE_INSENSITIVE); // "&$%", \046\044\045 

	// String lastKeyloc = "";
	// int curBidCtr=0;

	byte buffer1[]; // the file cache

	int bytesRead;

	boolean presoRepertorio = false;

	int campiInTabella = 0;	
	boolean listaInvertita = false;
	int db = DB_SBNWEB;
	
	
	
	// Arge 17/06/09
	public static String truncateDoubleDecimals(double time, int decimals) {
		Double big = new Double(time);

		String stringaTime = big.toString();
		int len = stringaTime.length();
		int ix = stringaTime.indexOf(".");

		if (ix != -1 && (len - ix - 1) > decimals)
			stringaTime = stringaTime.substring(0, ix + 1 + decimals);
		return stringaTime;
	}

	// end Arge 17/06/09


	public CreateOffsetFile() {
		sb = new StringBuffer();
		sbKeyLoc = new StringBuffer();
		keyPos = 0;
		recPos = 0;
	}

	public void apriOffsetFiles() // String sequenceFilename, int
									// anOffsetLength
	{
		// this.offsetFilenameOut1 = sequenceFilename;
		this.offsetLength = 11; // 

		try {

			offsetBufferredWriter1 = new BufferedWriter(new FileWriter(this.offsetFilenameOut1));
			// if (offsetFilenameOut2 != null && !offsetFilenameOut2.isEmpty())
			// // jav 1.6
			if (offsetFilenameOut2 != null && offsetFilenameOut2.length() > 0)
				offsetBufferredWriter2 = new BufferedWriter(new FileWriter(
						this.offsetFilenameOut2));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void chiudiOffsetFile() {
		try {
			if (offsetBufferredWriter1 != null)
				offsetBufferredWriter1.close();
			if (offsetBufferredWriter2 != null)
				offsetBufferredWriter2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * * pad a string S with a size of N with char C * on the left (True) or on
	 * the right(flase)
	 */
	public static String padString(String s, int n, char c, boolean paddingLeft) {
		StringBuffer str = new StringBuffer(s);
		int strLength = str.length();
		if (n > 0 && n > strLength) {
			for (int i = 0; i <= n; i++) {
				if (paddingLeft) {
					if (i < n - strLength)
						str.insert(0, c);
				} else {
					if (i > strLength)
						str.append(c);
				}
			}
		}
		return str.toString();
	}

	void run() {
		boolean in950 = false;
		boolean inTbClasse = false;
		boolean inTbClasseIndice = false;
		boolean inTbcSezioneCollocazione = false;
		
		boolean inTrTitTit = false;
		boolean inTrTitMar = false;
		boolean inTrTitBib = false;
		boolean inTrTitBibIndice = false;
		// boolean inTbaOrdini = false;
		boolean inTbNumeroStd = false;
		boolean inTrTitAut = false;
		
		boolean inTrAutAut = false;

		
		boolean inTrAutAutInv = false;
		boolean inTrAutAutInvRel = false;

		boolean inTrXxxYyyIndice = false;
		
		boolean inTbRepertorio = false;
		
		boolean inTrSogDes = false;
		boolean inTrDesDes = false;
		boolean inTbfBiblioteca = false;
		boolean inTbBibliotecaIndice = false;
		boolean inTbcNotaInv = false;

		boolean inTrcPossProvInventari = false;
		boolean inTrRepMar = false;
		boolean inTrPerInt = false;
		boolean inTsLinkMultim = false;

		boolean inTbParola = false;

		boolean inTbSogVar = false;

		
		
//		boolean inTrBidAltroId = false;

		
//		if (dataFilenameIn.contains("950"))
		if (dataFilenameIn.indexOf("950") != -1)
			in950 = true;
//		else if (dataFilenameIn.contains("tbf_biblioteca"))
		else if (dataFilenameIn.indexOf("tbf_biblioteca") != -1)
		{
			inTbfBiblioteca = true;
		}
//		else if (dataFilenameIn.contains("tb_biblioteca"))
		else if (dataFilenameIn.indexOf("tb_biblioteca") != -1)
			inTbBibliotecaIndice = true;
		
//		else if (dataFilenameIn.contains("tb_parola"))
		else if (dataFilenameIn.indexOf("tb_parola") != -1)
			inTbParola = true;
		
//		else if (dataFilenameIn.contains("tb_classe"))
		else if (dataFilenameIn.indexOf("tb_classe") != -1)
		{
			if (db == DB_INDICE)
				inTbClasseIndice = true;
			else
				inTbClasse = true;
		}

//		else if (dataFilenameIn.indexOf("tr_bid_altroid") != -1)
//				inTrBidAltroId = true;
		
		
//		else if (dataFilenameIn.contains("tr_tit_bib.out") // .srt 
		else if (dataFilenameIn.indexOf("tr_tit_bib.out") != -1 // .srt 
			//|| dataFilenameIn.contains("tr_tit_bib.out.sepSingolo.srt")
			)
		inTrTitBib = true;
		
		
//		else if (dataFilenameIn.contains("tr_rep_mar.out"))
		else if (dataFilenameIn.indexOf("tr_rep_mar.out") != -1)
			inTrRepMar = true;

//		else if (dataFilenameIn.contains("tr_per_int.out"))
		else if (dataFilenameIn.indexOf("tr_per_int.out") != -1)
			inTrPerInt = true;

//		else if (dataFilenameIn.contains("ts_link_multim.out"))
		else if (dataFilenameIn.indexOf("ts_link_multim.out") != -1)
			inTsLinkMultim = true;
		
//		else if (dataFilenameIn.contains("tb_numero_std"))
		else if (dataFilenameIn.indexOf("tb_numero_std") != -1)
			inTbNumeroStd = true;

//		else if (dataFilenameIn.contains("tr_aut_aut.out.inv.srt.rel"))
		else if (dataFilenameIn.indexOf("tr_aut_aut.out.inv.srt.rel") != -1)
			inTrAutAutInvRel = true;
		

		else if (
//				dataFilenameIn.contains("tr_aut_aut.out.srt.rel") || 
//				dataFilenameIn.contains("input/tr_aut_aut.out.inv.srt.rel") ||
//				dataFilenameIn.contains("tr_tit_cla.out.srt.rel") || 
//				dataFilenameIn.contains("tr_tit_luo.out.srt.rel") || 
//				dataFilenameIn.contains("tr_tit_mar.out.srt.rel") || 
//				dataFilenameIn.contains("tr_tit_sog.out.srt.rel") || 
//				dataFilenameIn.contains("tr_tit_tit.out.srt.rel") || 
//				dataFilenameIn.contains("tr_tit_tit.out.inv.srt.rel") 
				(dataFilenameIn.indexOf("tr_aut_aut.out.srt.rel") != -1) || 
				(dataFilenameIn.indexOf("input/tr_aut_aut.out.inv.srt.rel") != -1) ||
				(dataFilenameIn.indexOf("tr_tit_cla.out.srt.rel") != -1) || 
				(dataFilenameIn.indexOf("tr_tit_luo.out.srt.rel") != -1) || 
				(dataFilenameIn.indexOf("tr_tit_mar.out.srt.rel") != -1) || 
				(dataFilenameIn.indexOf("tr_tit_sog.out.srt.rel") != -1) || 
				(dataFilenameIn.indexOf("tr_tit_tit.out.srt.rel") != -1) || 
				dataFilenameIn.indexOf("tr_tit_tit.out.inv.srt.rel") != -1 
				)
			inTrXxxYyyIndice = true;

//		else if (dataFilenameIn.contains("tr_tit_tit"))
		else if (dataFilenameIn.indexOf("tr_tit_tit") != -1)
			inTrTitTit = true;
		
//		else if (dataFilenameIn.contains("tr_tit_mar.out.srt"))
		else if (dataFilenameIn.indexOf("tr_tit_mar.out.srt") != -1)
			inTrTitMar = true;
		
//		else if (dataFilenameIn.contains("tr_aut_aut.out.inv.srt"))
		else if (dataFilenameIn.indexOf("tr_aut_aut.out.inv.srt") != -1)
			inTrAutAutInv = true;
		
//		else if (dataFilenameIn.contains("tr_aut_aut.out"))
		else if (dataFilenameIn.indexOf("tr_aut_aut.out") != -1)
			inTrAutAut = true;
		
		
//		else if (dataFilenameIn.contains("tr_tit_aut") && !dataFilenameIn.contains(".rel"))
		else if ((dataFilenameIn.indexOf("tr_tit_aut") != -1) && (dataFilenameIn.indexOf(".rel") == -1))
			inTrTitAut = true;
//		else if (dataFilenameIn.contains("tb_repertorio"))
		else if (dataFilenameIn.indexOf("tb_repertorio") != -1)
			inTbRepertorio = true;

//		else if (dataFilenameIn.contains("tr_sog_des"))
		else if (dataFilenameIn.indexOf("tr_sog_des") != -1)
			inTrSogDes = true;
//		else if (dataFilenameIn.contains("tr_des_des"))
		else if (dataFilenameIn.indexOf("tr_des_des") != -1)
			inTrDesDes = true;

//		else if (dataFilenameIn.contains("tbc_nota_inv"))
		else if (dataFilenameIn.indexOf("tbc_nota_inv") != -1)
			inTbcNotaInv = true;
				
//		else if (dataFilenameIn.contains("trc_poss_prov_inventari"))
		else if (dataFilenameIn.indexOf("trc_poss_prov_inventari") != -1)
			inTrcPossProvInventari = true;
		
//		else if (dataFilenameIn.contains("tbc_sezione_collocazione"))
		else if (dataFilenameIn.indexOf("tbc_sezione_collocazione") != -1)
			inTbcSezioneCollocazione = true;
		
		
		else if (dataFilenameIn.indexOf("VARIANTI") != -1 || dataFilenameIn.indexOf("SCOMPOSIZIONI") != -1)
			inTbSogVar = true;
		
		
		
		int bufsize; // size of smallest file
		try {
			// fh1 = new RandomAccessFile(dataFilenameIn, "r");

			fis = new FileInputStream(dataFilenameIn);
		} catch (IOException ioErr) {
			System.err.println("Could not find " + dataFilenameIn);
			System.err.println(ioErr);
			System.exit(100);
		}
		// apriOffsetFile(offsetFilenameOut1, 11);
		apriOffsetFiles();
		// if (dataFilenameIn.contains("950coll"))
		// apriOffsetFile(offsetFilenameOut2, 9);

		// allocate two buffers large enough to hold entire files

		bufsize = 1024*512; // 512k
		//bufsize = 1024*4; // 4k per block
		//bufsize = 512; // 4k per block
		
		buffer1 = new byte[bufsize];
		// scriviOffset(Long.toString(offset));

		lastOffset = 0;
		while (true) {
			try {
				bytesRead = fis.read(buffer1);
				if (in950 == true)
					findOffsets950();
				else if (inTbfBiblioteca == true)
					findOffsetsBiblioteca();

				else if (inTbParola == true)
					findOffsetsTbParola();

				else if (inTbBibliotecaIndice == true)
					findOffsetsBibliotecaIndice();
				
				
				else if (inTbClasse == true)
					findOffsetsClasse();

				else if (inTbClasseIndice == true)
					findOffsetsClasseIndice();

//				else if (inTrBidAltroId == true)
//					findOffsetsTrBidAltroid();
//				
				
				else if (inTrTitTit == true)
				{
					if (listaInvertita == false)
						findOffsetsTrTitTit();
					else
						findOffsetsTrTitTitInv();
				}

				else if (inTrTitMar == true)
				{
						findOffsetsTrTitMar();
				}
				
				
				else if (inTrAutAut == true)
				{
					if (listaInvertita == true)
					{
						findOffsetsTrAutAutInv();
					}
					else
					{
						findOffsetsTrAutAut();
					}
				}
				else if (inTrXxxYyyIndice == true)
					findOffsetsTrXxxYyyRel();
				
				else if (inTrAutAutInv == true)
					findOffsetsTrAutAutInv();
				
				else if (inTrAutAutInvRel == true)
					findOffsetsTrAutAutInvRel();

				else if (inTrRepMar == true)
					findOffsetsTrRepMar();

				else if (inTrPerInt == true)
					findOffsetsTrPerInt();
				
				else if (inTsLinkMultim == true)
					findOffsetsTsLinkMultim();
				
				
				else if (inTrTitBib == true)
					findOffsetsLocalizzazioni();
//				else if (inTrTitBibIndice == true)
//					findOffsetsLocalizzazioniIndice();
				
				// else if (inTbaOrdini == true)
				// findOffsetsOrdini();
				else if (inTbNumeroStd == true)
					findOffsetsTbNumeroStd();
				else if (inTrTitAut == true)
					findOffsetsTrTitAut();


				else if (inTbRepertorio == true)
					findOffsetsTbRepertorio();
				else if (inTrSogDes == true)
					findOffsetsTrSogDes();
				else if (inTrDesDes == true)
					findOffsetsTrDesDes();
				else if (inTbcNotaInv == true)
					findOffsetsTbcNotaInv();

				else if (inTrcPossProvInventari == true)
					findOffsetsTrcPossProvInventari();
				
				else if (inTbcSezioneCollocazione == true)
					findOffsetsTbcSezioneCollocazione();
				
				
				else if (inTbSogVar == true)
					findOffsetsTbSoggettoVariante();
				
				else
					findOffsets();
				if (bytesRead < buffer1.length)
				{
					if (sb.length() > 0) // 201/08/2012 Gestione ultimo record in assenza di LF
					{
						String bidColl = sb.substring(0, 10); // Relazione invertita
						String bidBase = sb.substring(11, 21);

						scriviOffset(offsetBufferredWriter1, bidColl, Long.toString(lastOffset));
						rowCtr++;
						
					}

					break;
				}
			} catch (IOException ioErr) {
				System.err.println("ERROR: An exception occurred while processing the files");
				System.err.println(ioErr);
			}
		} // End while

		try {
			// fh1.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chiudiOffsetFile();

	}// End run


	private void scriviOffset(BufferedWriter offsetBufferredWriter,
			String aKey, String anOffset) {
		String squenceOut = padString(anOffset, 11, '0', true) + '\n';
		try {
			offsetBufferredWriter.write(aKey + squenceOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	String stringSepArray[] = { "&$%" };
	/*
	 * Prendi l'offset del primo di una serie di bid uguali (multipli)
	 */

	/*
	 * void findOffsetsOrdini() {
	 * 
	 * for (int i = 0; i < bytesRead; i++) { // if ( recPos <= keyEnd && recPos >=
	 * keyStart ) // posizioni invertite per accelerare il test
	 * sb.append((char)buffer1[i]);
	 * 
	 * if (buffer1[i] == '\n') { // Prendiamo il 31mo campo String ar[]; ar =
	 * MiscString.estraiCampi(sb.toString(), stringSepArray,
	 * MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "
	 * 
	 * String bid = ar[30]; //sb.substring(0,BID_LENGTH);
	 *  // e' uguale al bid precedente? if (!bid.equals(lastBid)) { // Cambio
	 * bid, quindi salvo l'offset del primo bid della serie
	 * //System.out.println("File offset at " + offset); if ((rowCtr & 0x1FFF) ==
	 * 0) System.out.println("File offset for " + bid +" at " + lastOffset + " - " +
	 * Long.toHexString(lastOffset) + " Riga " + rowCtr);
	 * 
	 * scriviOffset(offsetBufferredWriter1, bid, Long.toString(lastOffset)); //
	 * sb.toString() lastBid = bid; // sb.substring(0,BID_LENGTH); rowCtr++; }
	 * lastOffset = offset+1; // -BID_LENGTH sb.setLength(0); // clear recPos =
	 * 0; } else recPos++;
	 * 
	 * offset++; } }
	 */

	/*
	 * Prendi l'offset del primo di una serie di bid uguali (multipli)
	 */
	void findOffsetsTbNumeroStd() {

		for (int i = 0; i < bytesRead; i++) {
			if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
														// per accelerare il
														// test
				sb.append((char) buffer1[i]);

			if (buffer1[i] == '\n') {

				String bid = sb.substring(0, BID_LENGTH);

				// e' uguale al bid precedente?
				if (!bid.equals(lastBid)) {
					// Cambio bid, quindi salvo l'offset del primo bid della
					// serie
					// System.out.println("File offset at " + offset);
					if ((rowCtr & 0x1FFF) == 0)
						System.out.println("File offset for " + bid + " at "
								+ lastOffset + " - "
								+ Long.toHexString(lastOffset) + " Riga "
								+ rowCtr);

					scriviOffset(offsetBufferredWriter1, bid, Long
							.toString(lastOffset)); // sb.toString()
					lastBid = bid; // sb.substring(0,BID_LENGTH);
					rowCtr++;
				}
				lastOffset = offset + 1; // -BID_LENGTH
				sb.setLength(0); // clear
				recPos = 0;
			} else
				recPos++;

			offset++;
		}
	}


	/*
	 * Prendi l'offset del primo di una serie di bid uguali (multipli)
	 */
	void findOffsets950() {
		for (int i = 0; i < bytesRead; i++) {
			/*
			 * if ( recPos <= keyEnd && recPos >= keyStart ) // posizioni
			 * invertite per accelerare il test sb.append((char)buffer1[i]);
			 * 
			 * else if (keyLocStart != -1 && recPos <= keyLocEnd && recPos >=
			 * keyLocStart) // posizioni invertite per accelerare il test { if
			 * (keyLocDone == false &&(char)buffer1[i] >= '0' &&
			 * (char)buffer1[i] <= '9') sbKeyLoc.append((char)buffer1[i]); else
			 * keyLocDone = true; }
			 */
			if (keyLocStart == -1) // no keyLoc
			{
				if (recPos <= keyEnd && recPos >= keyStart) // posizioni
															// invertite per
															// accelerare il
															// test
					sb.append((char) buffer1[i]);
			} else
				sb.append((char) buffer1[i]);

			if (buffer1[i] == '\n') {
				// keyLocDone = false;
				String bid = sb.substring(0, BID_LENGTH);
				// if (keyLocStart != -1 && sbKeyLoc.length()>0) //
				// offsetFilenameOut2 != null && !offsetFilenameOut2.isEmpty()
				// && offsetFilenameOut2.contains("950coll")
				if (keyLocStart != -1) // offsetFilenameOut2 != null &&
										// !offsetFilenameOut2.isEmpty() &&
										// offsetFilenameOut2.contains("950coll")
				{
					String ar[] = MiscString.estraiCampi(sb.toString(), stringSepArray, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "
					String keyLoc = padString(ar[5], KEYLOC_LENGTH, '0', true);
					scriviOffset(offsetBufferredWriter2, keyLoc, Long.toString(lastOffset));
				}

				// e' uguale al bid precedente?
				if (!bid.equals(lastBid)) {
					// Cambio bid, quindi salvo l'offset del primo bid della
					// serie
					// System.out.println("File offset at " + offset);
					if ((rowCtr & 0x1FFF) == 0)
						System.out.println("File offset for " + bid + " at "
								+ lastOffset + " - "
								+ Long.toHexString(lastOffset) + " Riga "
								+ rowCtr);

					scriviOffset(offsetBufferredWriter1, bid, Long
							.toString(lastOffset)); // sb.toString()
					lastBid = bid; // sb.substring(0,BID_LENGTH);
					rowCtr++;
				}
				sbKeyLoc.setLength(0); // clear
				lastOffset = offset + 1; // -BID_LENGTH
				sb.setLength(0); // clear
				recPos = 0;
			} else
				recPos++;

			offset++;
		}
	}


	void findOffsetsTbRepertorio() {

		for (int i = 0; i < bytesRead; i++) {
			if (presoRepertorio == false) {
				if (buffer1[i] == '&')
					presoRepertorio = true;
				else
					sb.append((char) buffer1[i]);
			}

			if (buffer1[i] == '\n') {
				String repertorio = padString(sb.toString(), 10, '0', true);

				scriviOffset(offsetBufferredWriter1, repertorio, Long
						.toString(lastOffset));
				rowCtr++;

				if ((rowCtr & 0x1FFF) == 0)
					System.out.println("Riga " + rowCtr + " File offset for "
							+ sb.toString() + " at " + lastOffset + " - "
							+ Long.toHexString(lastOffset));

				lastOffset = offset + 1;
				sb.setLength(0); // clear
				recPos = 0;
				presoRepertorio = false;
			} else
				recPos++;

			offset++;
		}
	} // end findOffsetsRepertorio

	

	void findOffsetsTbcNotaInv() {
		
		for (int i = 0; i < bytesRead; i++) {
			if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
														// per accelerare il
														// test
				sb.append((char) buffer1[i]);

			if (buffer1[i] == '\n') {
				
				// splittiamo i campi a &$%
				String ar[] = MiscString.estraiCampi(sb.toString(),	stringSepArray,	MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "
				
				sb.setLength(0);

				sb.append(ar[1]);
				sb.append(ar[2]);
				sb.append(ar[3]);
				
				scriviOffset(offsetBufferredWriter1, sb.toString(), Long.toString(lastOffset));
				rowCtr++;
				if ((rowCtr & 0x1FFF) == 0)
					System.out.println("Riga " + rowCtr + " File offset for " + sb.toString() + " at " + lastOffset + " - " + Long.toHexString(lastOffset));

				lastOffset = offset + 1;
				sb.setLength(0); // clear
				recPos = 0;
			} else
				recPos++;

			offset++;
		}
	} // end findOffsetsTbcNotaInv







void findOffsetsBibliotecaIndice() {
	
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite per accelerare il test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			
			// splittiamo i campi a &$%
			
			String ar[] = MiscString.estraiCampi(sb.toString(),	stringSepArray,	MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "
			
			sb.setLength(0);
			sb.append(ar[0]); // cd_polo
			sb.append(ar[1]); // cd_bib
			
			scriviOffset(offsetBufferredWriter1, sb.toString(), Long.toString(lastOffset));
			
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for " + sb.toString() + " at " + lastOffset + " - " + Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsBiblioteca





public static void main(String args[]) {

//	double d = 0.0010;
//	System.out.println("Double " + d);
//	String s = truncateDoubleDecimals(d, 3);
//	System.out.println("Double string" + s);

	CreateOffsetFile createOffsetFile = new CreateOffsetFile();

	if (args.length < 2) {
		System.out.println("Parametri mancanti. Uso: CreateOffsetFile dataFilenameIn, offsetFilenameOut1, offsetFilenameOutN, 'listaInvertita'");
		System.exit(1);
	}
	System.out.println( 
     "\nVersione 1.0.0" + 	// aggiunta gestione tr_tit_aut non ordinata per indice
     					 	// aggiunta tr_tit_bib no ordinata per indice 
	 "\n====================================="
	);
	createOffsetFile.dataFilenameIn = args[0];
	
	//createOffsetFile.campiInTabella = Integer.parseInt(args[1]);	
	createOffsetFile.offsetFilenameOut1 = args[1];
	
	
	
	createOffsetFile.keyLocStart = -1;
	createOffsetFile.keyLocEnd = -1;
	
//	if (args[0].contains("950coll")) {
	if (args[0].indexOf("950coll") != -1) {
		createOffsetFile.offsetFilenameOut2 = args[2];
		createOffsetFile.keyLocStart = 46 - 1;
		createOffsetFile.keyLocEnd = createOffsetFile.keyLocStart + createOffsetFile.KEYLOC_LENGTH;
	}
	
	// Testiamo l'ultimo argomento per vedere se dobbiamo fare un indice per la lista invertita 
//	if (args[args.length-1].contains("listaInvertita"))
	if (args[args.length-1].indexOf("listaInvertita") != -1)
		createOffsetFile.listaInvertita = true;
	
//	if (args.length > 2 && args[2].contains("campi="))
//			createOffsetFile.campiInTabella = Integer.parseInt(args[2].substring(6));
			
	if (args.length > 2)
	{
		// Prendiamo i parametri con =
		for (int i= 2; i < args.length; i++)
		{
//			if (args[i].contains("campi="))
			if (args[i].indexOf("campi=") != -1)
				createOffsetFile.campiInTabella = Integer.parseInt(args[i].substring(6));
//			else if (args[i].contains("db="))
			else if (args[i].indexOf("db=") != -1)
			{
//				if (args[i].contains("db=indice"))
				if (args[i].indexOf("db=indice") != -1)
					createOffsetFile.db = createOffsetFile.DB_INDICE;	
			}
		}
	}
	
	
	
	
	createOffsetFile.keyStart = 0;
//	if (args[0].contains("tb_classe") )
	if (args[0].indexOf("tb_classe") != -1 )
		{
		if (createOffsetFile.db == createOffsetFile.DB_INDICE)
//			createOffsetFile.keyEnd = 40;
			createOffsetFile.keyEnd = 41+1; // 02/02/2015
		else
			createOffsetFile.keyEnd = 43;
	}

	else if (args[0].indexOf("tr_bid_altroid") != -1 )
	{
		createOffsetFile.keyEnd = 23;
	}
	
	
//	else if (args[0].contains("tbf_biblioteca") )
	else if (args[0].indexOf("tbf_biblioteca") != -1 )
	{
		createOffsetFile.campiInTabella = 32;
		createOffsetFile.keyEnd = 40;
	}
//	else if (args[0].contains("tb_biblioteca") )
	else if (args[0].indexOf("tb_biblioteca") != -1 )
		createOffsetFile.keyEnd = 8;

//	else if (args[0].endsWith("tr_tit_aut.out.srt.rel"))
	else if (args[0].endsWith("tr_tit_aut.out.srt.rel"))
		createOffsetFile.keyEnd = 9;

//	else if (args[0].contains("tr_tit_tit")
	else if ((args[0].indexOf("tr_tit_tit") != -1)
			|| args[0].endsWith("tr_tit_aut.out.srt")
			|| args[0].endsWith("tr_tit_aut.out")
			|| args[0].endsWith("tr_tit_mar.out.srt")
			) // tr_tit_aut di indice non sortata
		createOffsetFile.keyEnd = 22;
	
	else if (args[0].endsWith("tr_tit_bib.out.srt")
//			|| args[0].contains("tr_aut_aut.out.inv.srt.rel"))
			|| args[0].indexOf("tr_aut_aut.out.inv.srt.rel") != -1)
		createOffsetFile.keyEnd = 21;

//	else if (args[0].endsWith("tr_tit_bib.out"))
//			createOffsetFile.keyEnd = 16;
	
//	else if (args[0].contains("tr_tit_bib.out"))
	else if (args[0].indexOf("tr_tit_bib.out") != -1)
		createOffsetFile.keyEnd = 16;
	
//	else if (args[0].contains("tr_aut_aut.out.inv.srt") 
//			|| args[0].contains("tr_aut_aut.out")
//			|| args[0].contains("tr_sog_des.out.srt") 
//			|| args[0].contains("tr_rep_mar.out") 
//			|| args[0].endsWith("ts_link_multim.out.srt")
//			)
	else if ((args[0].indexOf("tr_aut_aut.out.inv.srt") != -1) 
			|| (args[0].indexOf("tr_aut_aut.out") != -1)
			|| (args[0].indexOf("tr_sog_des") != -1) // .out.srt
//			|| (args[0].indexOf("tr_sog_des.out.inv.srt") != -1)
			|| (args[0].indexOf("tr_rep_mar.out") != -1)
			|| args[0].endsWith("ts_link_multim.out.srt")
			|| (args[0].indexOf("tr_des_des") != -1)
			)

		createOffsetFile.keyEnd = 23;
//	else if (args[0].contains("tr_sog_des.out.srt"))
//		createOffsetFile.keyEnd = 23;

//	else if (args[0].contains("tbc_nota_inv") )
	else if (args[0].indexOf("tbc_nota_inv")  != -1)
	{
		createOffsetFile.keyEnd = 27;
	}
//	else if (args[0].contains("trc_poss_prov_inventari") )
	else if (args[0].indexOf("trc_poss_prov_inventari")  != -1)
	{
		createOffsetFile.keyEnd = 34;
	}
//	else if (args[0].contains("tbc_sezione_collocazione"))
	else if (args[0].indexOf("tbc_sezione_collocazione") != -1)
		createOffsetFile.keyEnd = 22;
	else
		createOffsetFile.keyEnd = 9;

	System.out.println("Inizio elaborazione "
			+ createOffsetFile.dataFilenameIn + " " + DateUtil.getDate()
			+ " " + DateUtil.getTime());
	
	createOffsetFile.run();
	
	System.out.println("Fine elaborazione "
			+ createOffsetFile.dataFilenameIn + " " + DateUtil.getDate()
			+ " " + DateUtil.getTime());
} // End main
















void findOffsetsTrcPossProvInventari() {
	
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			
			// splittiamo i campi a &$%
			String ar[] = MiscString.estraiCampi(sb.toString(),	stringSepArray,	MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "
			
			sb.setLength(0);

			sb.append(ar[3]); // Biblioteca
			sb.append(ar[2]); // Serie
			sb.append(ar[1]); // Inventario
			
			scriviOffset(offsetBufferredWriter1, sb.toString(), Long.toString(lastOffset));
			rowCtr++;
			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for " + sb.toString() + " at " + lastOffset + " - " + Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTbcNotaInv



void findOffsetsBiblioteca() {
	
	for (int i = 0; i < bytesRead; i++) {
//		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite per accelerare il test
		sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			
			// splittiamo i campi a &$%
			
			String ar[] = MiscString.estraiCampi(sb.toString(),	stringSepArray,	MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "
			
			// Controlliamo se abbiamo dei record spezzati causa LF
			if (ar.length != campiInTabella)
			{
				System.out.println("Record " + rowCtr + " spezzato" + sb.toString());
				sb.setLength(sb.length()-1); // remove linefeed
			}
			else
			{
				sb.setLength(0);
				sb.append(ar[2]); // cd_polo
				sb.append(ar[3]); // cd_bib
				
				scriviOffset(offsetBufferredWriter1, sb.toString(), Long.toString(lastOffset));
				
				rowCtr++;

				if ((rowCtr & 0x1FFF) == 0)
					System.out.println("Riga " + rowCtr + " File offset for " + sb.toString() + " at " + lastOffset + " - " + Long.toHexString(lastOffset));

				lastOffset = offset + 1;
				sb.setLength(0); // clear
				recPos = 0;
			}
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsBiblioteca


void findOffsets() {
	// for (int i = 0; i < buffer1.length; i++) {
//	String ar[];
	int ctr;
	int campiInTabellaMenoUno = campiInTabella-1;
	
		for (int i = 0; i < bytesRead; i++) {
			

			if (campiInTabella == 0)
			{
				if (recPos <= keyEnd && recPos >= keyStart) 
				{
					sb.append((char) buffer1[i]);
					keyPos++;
				}
			}
			else
				sb.append((char) buffer1[i]);
				
			
		if (buffer1[i] == '\n') {
			if (campiInTabella > 0)
			{
				// Controlliamo se abbiamo dei record spezzati causa LF
				ctr = MiscString.countPattern(p, sb.toString());
				if (ctr < campiInTabellaMenoUno)
				
				{
					System.out.println("Record " + rowCtr + " spezzato" + sb.toString());
					sb.setLength(sb.length()-1); // remove linefeed
					offset++;
					continue;
				}
			}
		

			sb.setLength(10); // solo id
			String bid = sb.toString();
// 30/09/2012 Rimosso su richiesta Renato/Rossana
//			if (lastWrittenBid.equals(bid))
//			{
//				System.out.println("SEGNALAZIONE - Bid gia' scritto precedentemente: " + bid); // 27/08/2012 problema cont tb_impronta chiave 
//				System.out.println("Riga " + rowCtr + " File offset for " + bid + " at " + lastOffset + " - "	+ Long.toHexString(lastOffset));
//			}
			
			scriviOffset(offsetBufferredWriter1, bid, Long.toString(lastOffset));
			lastWrittenBid = bid;

			recPos = 0;
			lastOffset = offset + 1;
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for " + bid + " at " + lastOffset + " - "	+ Long.toHexString(lastOffset));

			sb.setLength(0); // clear
		} 
		else
			recPos++;

		offset++;
	} // End for
}





void findOffsetsClasse() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String cdSysClassEdizione = sb.substring(0, 3);
			String edizione = sb.substring(6, 8);
			String cdClasse = sb.substring(11, 42);

//			scriviOffset(offsetBufferredWriter1, cdSysClassEdizione	+ cdClasse, Long.toString(lastOffset));
			scriviOffset(offsetBufferredWriter1, cdSysClassEdizione + edizione	+ cdClasse, Long.toString(lastOffset)); // 02/02/2015
			
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsClasse


void findOffsetsClasseIndice() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {

			
			String key = sb.substring(0,1) + sb.substring(4,7) + " " + sb.substring(10, 41); // 02/02/2015 
			scriviOffset(offsetBufferredWriter1, key, Long.toString(lastOffset));
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsClasse






void findOffsetsTrAutAut() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String bidBase = sb.substring(0, 10);
//			String bidColl = sb.substring(13, 23);

			scriviOffset(offsetBufferredWriter1, bidBase, Long.toString(lastOffset));
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrAutAut



//void findOffsetsLocalizzazioniIndice() {
//	String bid;
//	String polo;
//	int ctr;
//	int campiInTabellaMenoUno = campiInTabella-1;
//	
////	for (int i = 0; i < bytesRead; i++) {
////		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite per accelerare il test
////			sb.append((char) buffer1[i]);
////		if (buffer1[i] == '\n') {
////		try{
////			bid = sb.substring(0, 10);
////			polo = sb.substring(13, 16); // 
////
////			scriviOffset(offsetBufferredWriter1, bid + polo, Long.toString(lastOffset));
////
////		} catch (Exception e) {
////			System.out.println("String: '" + sb.toString() + "'");
////			e.printStackTrace();
////		}
////			rowCtr++;
////
////			if ((rowCtr & 0x1FFF) == 0)
////				System.out.println("Riga " + rowCtr + " File offset for " + sb.toString() + " at " + lastOffset + " - " + Long.toHexString(lastOffset));
////
////			lastOffset = offset + 1;
////			sb.setLength(0); // clear
////			recPos = 0;
////		} else
////			recPos++;
////
////		offset++;
////	}
//
////	String ar[];
//	
////	Pattern p = Pattern.compile("\\Q&$%\\E", Pattern.CASE_INSENSITIVE); // "&$%", \046\044\045 
//	
//	for (int i = 0; i < bytesRead; i++) {
//		
//
//		if (campiInTabella == 0)
//		{
//			if (recPos <= keyEnd && recPos >= keyStart) 
//			{
//				sb.append((char) buffer1[i]);
//				keyPos++;
//			}
//		}
//		else
//			sb.append((char) buffer1[i]);
//			
//		
//	if (buffer1[i] == '\n') {
//
//
//		if (campiInTabella > 0)
//		{
//			// splittiamo i campi a &$%
//			//ar = MiscString.estraiCampi(sb.toString(),	stringSepArray,	MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "
////			if (ar.length != campiInTabella)
//			
//			
//			// Controlliamo se abbiamo dei record spezzati causa LF
//			ctr = MiscString.countPattern(p, sb.toString());
//			if (ctr < campiInTabellaMenoUno)
//			{
//				System.out.println("Record " + rowCtr + " spezzato" + sb.toString());
//				sb.setLength(sb.length()-1); // remove linefeed
//				offset++;
//				continue;
//			}
//
//		}
//	
//
//		bid = sb.substring(0, 10);
////		polo = sb.substring(13, 16); // 
//		polo = sb.substring(11, 14); // 
//
//		scriviOffset(offsetBufferredWriter1, bid + polo, Long.toString(lastOffset));
//		
//		
//		recPos = 0;
//		lastOffset = offset + 1;
//		rowCtr++;
//
//		if ((rowCtr & 0x1FFF) == 0)
//			System.out.println("Riga " + rowCtr + " File offset for " + sb.toString().substring(1,keyEnd) + " at " + lastOffset + " - "	+ Long.toHexString(lastOffset));
//
//		sb.setLength(0); // clear
//	} 
//	else
//		recPos++;
//	offset++;
//	}
//} // end findOffsetsLocalizzazioniIndice

/*
 * Prendi l'offset del primo di una serie di bid uguali (multipli)
 */
void findOffsetsLocalizzazioni() {

	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);
		if (buffer1[i] == '\n') {
			String bid = sb.substring(0, BID_LENGTH);

			// e' uguale al bid precedente?
			if (!bid.equals(lastBid)) {
				// Cambio bid, quindi salvo l'offset del primo bid della serie
				// System.out.println("File offset at " + offset);
				if ((rowCtr & 0x1FFF) == 0)
					System.out.println("File offset for " + bid + " at "
							+ lastOffset + " - "
							+ Long.toHexString(lastOffset) + " Riga "
							+ rowCtr);

				scriviOffset(offsetBufferredWriter1, bid, Long
						.toString(lastOffset)); // sb.toString()
				lastBid = bid; // sb.substring(0,BID_LENGTH);
				rowCtr++;
			}
			lastOffset = offset + 1; // -BID_LENGTH
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // End findOffsetsLocalizzazioni





void findOffsetsTrTitTitInv() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String bidBase = sb.substring(13, 23); // SOURCE and TARGET invertiti
			String bidColl = sb.substring(0, 10);

			// 01/04/2008 scriviOffset(offsetBufferredWriter1,
			// bidBase+bidColl, Long.toString(lastOffset));
			scriviOffset(offsetBufferredWriter1, bidBase, Long.toString(lastOffset));
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrTitTitInv








void findOffsetsTbcSezioneCollocazione() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String sezione = sb.substring(0, 10);
			String bib = sb.substring(13, 16);
			String polo = sb.substring(19, 22);

			// 01/04/2008 scriviOffset(offsetBufferredWriter1, bidBase+bidColl, Long.toString(lastOffset));
			// 13/05/2010 scriviOffset(offsetBufferredWriter1, bidBase, Long.toString(lastOffset));
			scriviOffset(offsetBufferredWriter1, sezione+bib+polo, Long.toString(lastOffset));
			
			
			
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrTitTit



void findOffsetsTrTitTit() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String bidBase = sb.substring(0, 10);
			String bidColl = sb.substring(13, 23);

			// 01/04/2008 scriviOffset(offsetBufferredWriter1, bidBase+bidColl, Long.toString(lastOffset));
			// 13/05/2010 scriviOffset(offsetBufferredWriter1, bidBase, Long.toString(lastOffset));
			scriviOffset(offsetBufferredWriter1, bidBase+bidColl, Long.toString(lastOffset));
			
			
			
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrTitTit


void findOffsetsTrXxxYyyRel() {
	int keyLen = keyEnd+1;
	
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			rowCtr++;

			if (sb.length() < keyLen)
			{
				System.out.println("Riga invalida" + rowCtr + "'"+ sb.toString()+"'");
			}
			else
			{
				String bidBase = sb.substring(0, 10); 
				scriviOffset(offsetBufferredWriter1, bidBase, Long.toString(lastOffset));

				if ((rowCtr & 0x1FFF) == 0)
					System.out.println("Riga " + rowCtr + " File offset for "
							+ sb.toString() + " at " + lastOffset + " - "
							+ Long.toHexString(lastOffset));
			}
			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrXxxYyyRel

void findOffsetsTrRepMar() {// Chiave univoca (prima della serie)
	
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String mid = sb.substring(13, 23); // 
			if (!mid.equals(lastMid))
			{
				scriviOffset(offsetBufferredWriter1, mid, Long.toString(lastOffset));
				lastMid = mid;
				rowCtr++;
			}
			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for " + sb.toString() + " at " + lastOffset + " - " + Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrRepMar



void findOffsetsTrPerInt() { // Chiave univoca (prima della serie)
    
    for (int i = 0; i < bytesRead; i++) {
        if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
                                                    // per accelerare il
                                                    // test
            sb.append((char) buffer1[i]);

        if (buffer1[i] == '\n') {
			rowCtr++;
            String idPersonaggio = sb.substring(0, 10);
//            String vid = sb.substring(13, 23);
//            scriviOffset(offsetBufferredWriter1, idPersonaggio+vid, Long.toString(lastOffset));

			if (!idPersonaggio.equals(lastIdPersonaggio))
			{
				scriviOffset(offsetBufferredWriter1, idPersonaggio, Long.toString(lastOffset));
				lastIdPersonaggio = idPersonaggio;
				rowCtr++;
			}
            
            if ((rowCtr & 0x1FFF) == 0)
                System.out.println("Riga " + rowCtr + " File offset for " + sb.toString() + " at " + lastOffset + " - " + Long.toHexString(lastOffset));

            lastOffset = offset + 1;
            sb.setLength(0); // clear
            recPos = 0;
        } else
            recPos++;

        offset++;
    }
} // end findOffsetsTrPerInt


void findOffsetsTrTitAut() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String bid = sb.substring(0, 10);
			String vid = sb.substring(13, 23);

			scriviOffset(offsetBufferredWriter1, bid + vid, Long
					.toString(lastOffset));
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrTitAut


/*
*	Indice con chiave univoca
**/
void findOffsetsTsLinkMultim() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		
		if (buffer1[i] == '\n') {
			//String seq = sb.substring(0, 10);
			String idLinkMultim = sb.substring(13, 23);
			if (!idLinkMultim.equals(lastIdLinkMultim))
			{
				scriviOffset(offsetBufferredWriter1, idLinkMultim, Long.toString(lastOffset));
				lastIdLinkMultim = idLinkMultim;
				rowCtr++;
			}
			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTsLinkMultim


void findOffsetsTbParola() {

	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
		sb.append((char) buffer1[i]);
		if (buffer1[i] == '\n') {
			String bid = sb.substring(0, BID_LENGTH);
			// e' uguale al bid precedente?
			if (!bid.equals(lastBid)) {
				if ((rowCtr & 0x1FFF) == 0)
					System.out.println("File offset for " + bid + " at "
							+ lastOffset + " - "
							+ Long.toHexString(lastOffset) + " Riga "
							+ rowCtr);

				scriviOffset(offsetBufferredWriter1, bid, Long
						.toString(lastOffset)); // sb.toString()
				lastBid = bid; // sb.substring(0,BID_LENGTH);
				rowCtr++;
			}
			lastOffset = offset + 1; // -BID_LENGTH
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
}



void findOffsetsTrTitMar() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String bid = sb.substring(0, 10);
			String marColl = sb.substring(13, 23);

			// 01/04/2008 scriviOffset(offsetBufferredWriter1, bidBase+bidColl, Long.toString(lastOffset));
			// 13/05/2010 scriviOffset(offsetBufferredWriter1, bidBase, Long.toString(lastOffset));
			scriviOffset(offsetBufferredWriter1, bid+marColl, Long.toString(lastOffset));
			
			
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrTitTit





String lastDidBase = "";




String lastCid = "";

void findOffsetsTrSogDes() // Ordine DID/CID dove noi crechiami il CID
{
	String did;
	String cid;

	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			
			if (db == DB_INDICE)
			{
				if (listaInvertita)
				{
					cid = sb.substring(13, 23);
					did = sb.substring(0, 10);
				}
				else
				{
					cid = sb.substring(0, 10);
					did = sb.substring(13, 23);
				}
			}
			else
			{
				if (listaInvertita)
				{
					did = sb.substring(13, 23);
					cid = sb.substring(0, 10);
				}
				else
				{
					did = sb.substring(0, 10);
					cid = sb.substring(13, 23);
				}
			}
			

			
			
			
			
			
			if (!lastCid.equals(cid)) {
				scriviOffset(offsetBufferredWriter1, cid, Long.toString(lastOffset));
				lastCid = cid;
			}

			rowCtr++;
			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));
			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrSogDes


void findOffsetsTrDesDes() // Ordine DID/CID dove noi crechiami il CID
{
	String didBase;
	
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {

			if (listaInvertita)
				didBase = sb.substring(13, 23);
			
			else
				didBase = sb.substring(0, 10);
			
			
			if (!lastDidBase.equals(didBase)) {
				scriviOffset(offsetBufferredWriter1, didBase, Long.toString(lastOffset));
				lastDidBase = didBase;
			}

			rowCtr++;
			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));
			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrDesDes




void findOffsetsTrAutAutInv() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String bidColl = sb.substring(0, 10); // Relazione invertica
			String bidBase = sb.substring(13, 23);

			scriviOffset(offsetBufferredWriter1, bidBase, Long
					.toString(lastOffset));
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
} // end findOffsetsTrAutAutInv





void findOffsetsTrAutAutInvRel() {
	for (int i = 0; i < bytesRead; i++) {
		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
													// per accelerare il
													// test
			sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			// String bidBase = sb.substring(0,10);
			// String bidColl = sb.substring(13,23);

			String bidColl = sb.substring(0, 10); // Relazione invertita
			String bidBase = sb.substring(11, 21);

			scriviOffset(offsetBufferredWriter1, bidColl, Long.toString(lastOffset));
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));

			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;

		offset++;
	}
	
	
} // end findOffsetsTrAutAutInvRel


// 12/10/2016 Gestione numero OCN
//void findOffsetsTrBidAltroid() {
//	for (int i = 0; i < bytesRead; i++) {
//		if (recPos <= keyEnd && recPos >= keyStart) // posizioni invertite
//													// per accelerare il
//													// test
//			sb.append((char) buffer1[i]);
//
//		if (buffer1[i] == '\n') {
//			String bid = sb.substring(0, 10);
//			String cd_istituzione = sb.substring(13, 23);
//
//			scriviOffset(offsetBufferredWriter1, bid + cd_istituzione, Long.toString(lastOffset));
//			rowCtr++;
//			if ((rowCtr & 0x1FFF) == 0)
//				System.out.println("Riga " + rowCtr + " File offset for "
//						+ sb.toString() + " at " + lastOffset + " - "
//						+ Long.toHexString(lastOffset));
//
//			lastOffset = offset + 1;
//			sb.setLength(0); // clear
//			recPos = 0;
//		} else
//			recPos++;
//
//		offset++;
//	}
//} // end findOffsetsTrBidAltroid


// 12/01/2018. 
// 15/01/2018 Non neccessario per ora 
void findOffsetsTbSoggettoVariante() {

	for (int i = 0; i < bytesRead; i++) {
		sb.append((char) buffer1[i]);

		if (buffer1[i] == '\n') {
			String sog_variante = sb.toString().substring(1,80);

			scriviOffset(offsetBufferredWriter1, sog_variante, Long.toString(lastOffset));
			rowCtr++;

			if ((rowCtr & 0x1FFF) == 0)
				System.out.println("Riga " + rowCtr + " File offset for "
						+ sb.toString() + " at " + lastOffset + " - "
						+ Long.toHexString(lastOffset));
			lastOffset = offset + 1;
			sb.setLength(0); // clear
			recPos = 0;
		} else
			recPos++;
		offset++;
	}
} // end findOffsetsTbSoggettoVariante




} // end CreateOffsetFile



