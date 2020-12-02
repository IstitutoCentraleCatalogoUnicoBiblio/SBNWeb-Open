package it.finsiel.offlineExport;

//import ConfigTable;
//import DbUpload;
import it.finsiel.misc.DateUtil;
import it.finsiel.misc.Misc;
import it.finsiel.misc.MiscString;
import it.finsiel.misc.MiscStringTokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Select950 {
    FileOutputStream streamOut; // declare a file output object
    String step;
    String inputFilename;
    String outputFilename;

    //String inputFilenameInventario = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\tbc_inventario.out";
    //String outputFilenameInventario = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\950inventario.out";
    
//    String inputFilenameCollocazione = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\tbc_collocazione.out";
//    String outputFilenameCollocazione = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\950collocazione.out";
//
//    String inputFilenameEsemplare = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\tbc_esemplare_titolo.out";
//    String outputFilenameEsemplare = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\950esemplare.out";
//
//
//    String inputFilenameFillData = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\950ice.out.srt";
//    String outputFilenameFillData = "C:\\SbnWeb\\migrazione\\CFI\\BNCF.take2\\db_out_postgres\\950ice.out.srt.out";
    
    
    
    
    static String stringSepDiCampoInArray[] = { "&$%"}; // &$%
	static char escapeCharacter = '\\'; // il backslash di default
	
	String sepFiltri = "---";
//	int rowCtrSpan = 0x1FFF;
	int rowCtrSpan = 0x1FFF;
	long recordsCancellati = 0;

	/**
	 * @param args
	 */
	public static void main(String args[])
	{
		char charSepArrayEquals[] = { '='};
		char charSepArraySpace[] = { ' '};

 if(args.length < 3)
 {
	        System.out.println("Uso: Select950 stepName inputFile outputFile");
	        System.exit(1);
 }
	    
	    String start="Select950 tool - � Almaviva S.p.A 2008"+
		 "\n====================================="+
		 "\nInizio esecuzione " + DateUtil.getDate() + " " + DateUtil.getTime(); 

	    System.out.println(start);

		
// System.out.println("inp: " + inputFile);

		Select950 select950 = new Select950();

		select950.step = args[0];
		select950.inputFilename = args[1];
		select950.outputFilename = args[2];

		select950.run();
		
// System.out.println("Righe elaborate: " + Integer.toString(rowCounter));
	    System.exit(0);
	} // End main






public void doSelect950inventario() {
	
// String tableName;
// int pos;
// Statement stmt = null;
	String data = ""; 
	int rowCtr = 0;
	int recordScritti = 0;
	String arData[];
	StringBuffer sb = new StringBuffer();
	BufferedReader in;
	String s;
	String ar[];
	byte[] ba_Utef8;
	BufferedReader fileIn = null;
	try {
		fileIn = new BufferedReader(new FileReader(inputFilename));
		System.out.println("\nElaborazione di " + inputFilename + " " +DateUtil.getDate() + " " + DateUtil.getTime()); 
		// Leggiamo le configurazioni di base
		// ----------------------------------
		while (true) {
			try {
				data = fileIn.readLine();
				if (data == null)
					break;
				else if (Misc.emptyString(data))
					continue;
				rowCtr++;

				// Scomponiamo la riga dei dati in un array
				ar = MiscString.estraiCampi(data, stringSepDiCampoInArray, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
				if (ar[56-1].charAt(0) == 'S') // Cancellato
			    {
			    	recordsCancellati++;
			    }
				else
			    {
			    // Componiamo il record in uscita
				sb.setLength(0); // clear
			    sb.append(ar[7-1]); // bid di INVENTARIO
			    sb.append("&$%"+ar[6-1]); 		// tbinv_key_loc
			    sb.append("&$%TB_INV"); // Riga da tabella di inventario
			    sb.append("&$%"+ar[2-1]); // tbinv_cd_bib
			    sb.append("&$%"+ar[3-1]); // tbinv_cd_serie
			    sb.append("&$%"+ar[4-1]); // tbinv_cd_inven
			    sb.append("&$%"+ar[13-1]); // tbinv_cd_sit
			    sb.append("&$%"+ar[64-1]); // tbinv_data_ingresso
			    sb.append("&$%"+MiscString.trimRight(ar[11-1])); // tbinv_seq_coll
			    sb.append("&$%"+ar[12-1]); // tbinv_precis_inv
			    // Per le nuove etichette (960)	
			    sb.append("&$%"+ar[22-1]); // tbinv_stato_con
			    sb.append("&$%"+ar[35-1]); // tbinv_cd_frui
			    sb.append("&$%"+ar[46-1]); // tbinv_sez_old
			    sb.append("&$%"+ar[47-1]); // tbinv_loc_old
			    sb.append("&$%"+ar[48-1]); // tbinv_spec_old
			    
			    //sb.append("&$%"+ar[49-1]); // tbinv_cd_supporto
			    sb.append("&$%"+ar[24-1]); // tbinv_cd_mat_inv Mantis 5037 collaudo. 22/08/2012
			    
			    sb.append("&$%"+ar[63-1]); // tbinv_cd_riproducibilita
			    sb.append("&$%"+ar[34-1]); // cd_no_disp
			    sb.append("&$%"+ar[51-1]); // ts_ins_prima_coll
			    // 30/03/2010
			    sb.append("&$%"+ar[59-1]); // digitalizzazione
			    sb.append("&$%"+ar[60-1]); // disp_copia_digitale
			    sb.append("&$%"+ar[61-1]); // id_accesso_remoto
			    sb.append("&$%"+ar[62-1]); // rif_teca_digitale
			    
			    sb.append("\n"); // EOL
				
			    ba_Utef8 = sb.toString().getBytes("UTF-8");
			    
			    recordScritti++;
			    streamOut.write(ba_Utef8);
			    } // Se non cancellato
			    
			    if ((rowCtr & rowCtrSpan) == 0)
			    	System.out.println("Record letti " + rowCtr);
			    if ((recordScritti & rowCtrSpan) == 0)
			    	System.out.println("Record scritti " + recordScritti);
			}
			    catch (ArrayIndexOutOfBoundsException e) {
			    	System.out.println("Array index out of bounds.");
			    	System.out.println("Record letti " + rowCtr);
			    	System.out.println("Record scritti " + recordScritti);
			    	System.out.println("Record errato '" + data+"'");
			 			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // End while
	}
	 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// Close file
	 if (fileIn != null)
		try {
			fileIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Totale record letti " + rowCtr);
		System.out.println("Totale record scritti " + recordScritti);
    	System.out.println("Totale record con flagCanc a S " + recordsCancellati);

} // End doSelect950inventario


public void doSelect950collocazione() {
	String data = ""; 
	int rowCtr = 0;
	String arData[];
	StringBuffer sb = new StringBuffer();
	BufferedReader in;
	String s;
	String ar[];
	byte[] ba_Utef8;
	BufferedReader fileIn = null;
	try {
		fileIn = new BufferedReader(new FileReader(inputFilename));
		System.out.println("Elaborazione di " + inputFilename + " " +DateUtil.getDate() + " " + DateUtil.getTime()); 

		while (true) {
			try {
				data = fileIn.readLine();
				if (data == null)
					break;
				else if (Misc.emptyString(data))
					continue;
				
				rowCtr++;
//				data = data.replace("'", "''"); // gestione di eventuali singoli
												// apici
				// Scomponiamo la riga dei dati in un array
//				ar = MiscString.estraiCampiConEscapePerSeparatore(data, stringSepDiCampoInArray, escapeCharacter);
				ar = MiscString.estraiCampi(data, stringSepDiCampoInArray, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

				while (ar.length < 25)
				{
					System.out.println("\nRiga spezzata " + rowCtr + " " + data);
					data += fileIn.readLine();
					ar = MiscString.estraiCampi(data, stringSepDiCampoInArray, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
					rowCtr++;
				}
				
			    if (ar[25-1].charAt(0) == 'S') // Cancellato
			    {
			    	recordsCancellati++;
			    }
			    {
				sb.setLength(0); // clear
			    // Componiamo il record in uscita
			    sb.append(ar[12-1]); // bid di COLLOCAZIONE

			    sb.append("&$%"+ar[9-1]); 		// tbcol_bid_doc
			    sb.append("&$%"+ar[10-1]); 		// tbcol_cd_doc
			    sb.append("&$%"+ar[4-1]); 		// tbcol_cd_biblioteca_sezione
			    sb.append("&$%TB_COL"); // Riga da tabella di collocazione
			    sb.append("&$%"+ar[11-1]); // '0' as tbcol_key_loc
			    sb.append("&$%"+ar[5-1]); // ' ' as tbcol_cd_sez
			    sb.append("&$%"+ar[13-1]); // ' ' as tbcol_cd_loc
			    sb.append("&$%"+ar[14-1]); // ' ' as tbcol_spec_loc
			    sb.append("&$%"+ar[15-1]); // '$' as tbcol_consis

			    
			    // Per le nuove etichette (960)	
			    sb.append("&$%"+ar[1-1]); // 'tbcol_cd_sistema
			    sb.append("&$%"+ar[2-1]); // 'tbcol_cd_edizione
			    sb.append("&$%"+ar[3-1]); // 'tbcol_classe
			    
			    
			    sb.append("\n"); // EOL
				
			    ba_Utef8 = sb.toString().getBytes("UTF-8");
			    streamOut.write(ba_Utef8);
			    } // End se snon cancellato				
			    
			    if ((rowCtr & rowCtrSpan) == 0)
			    	System.out.println("Record letti " + rowCtr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // End while
	}
	 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// Close file
	 if (fileIn != null)
		try {
			fileIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	System.out.println("Totale record letti " + rowCtr);
    	System.out.println("Totale record con flagCanc a S " + recordsCancellati);
} // End doSelect950collocazione

public void doSelect950esemplare() {
	String data = ""; 
	int rowCtr = 0;
	String arData[];
	StringBuffer sb = new StringBuffer();
	BufferedReader in;
	String s;
	String ar[];
	byte[] ba_Utef8;
	BufferedReader fileIn = null;
	try {
		fileIn = new BufferedReader(new FileReader(inputFilename));
		System.out.println("\nElaborazione di " + inputFilename + " " +DateUtil.getDate() + " " + DateUtil.getTime()); 

		// Leggiamo le configurazioni di base
		// ----------------------------------
		while (true) {
			try {
				data = fileIn.readLine();
				if (data == null)
					break;
				else if (Misc.emptyString(data))
					continue;

				
				rowCtr++;
//				data = data.replace("'", "''"); // gestione di eventuali singoli
												// apici
				// Scomponiamo la riga dei dati in un array
//				ar = MiscString.estraiCampiConEscapePerSeparatore(data, stringSepDiCampoInArray, escapeCharacter);
				ar = MiscString.estraiCampi(data, stringSepDiCampoInArray, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);


				while (ar.length < 10)
				{
					System.out.println("\nRiga spezzata " + rowCtr + " " + data);
					data += fileIn.readLine();
					ar = MiscString.estraiCampi(data, stringSepDiCampoInArray, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
					rowCtr++;
				}
				
				
				
			    if (ar[10-1].charAt(0) == 'S') // Cancellato
			    {
			    	recordsCancellati++;
			    }
			    else
			    {
				sb.setLength(0); // clear
			    // Componiamo il record in uscita
			    sb.append(ar[2-1]); // bid ESEMPLARE

//			    sb.append("&$%"+"         "); 	// tbinv_key_loc
//			    sb.append("&$%"+"          ");	// tbcol_bid_doc
//			    sb.append("&$%"+"         ");	// tbcol_cd_doc
			    sb.append("&$%"+ar[1-1]); // '0' as tbese_cd_doc
			    sb.append("&$%"+ar[3-1]); 		// tbese_cd_biblioteca
			    sb.append("&$%TB_ESE"); // Riga da tabella di esemplare

//			    sb.append("&$%__"); // tbinv_cd_bib
//			    sb.append("&$%__"); // tbinv_cd_serie
//			    sb.append("&$%__"); // tbinv_cd_inven
//			    sb.append("&$%__"); // tbinv_flg_disp
//			    sb.append("&$%__"); // tbinv_data_scarico
//			    sb.append("&$%__"); // tbinv_seq_coll
//			    sb.append("&$%__"); // tbinv_precis_inv
//			    sb.append("&$%__"); // '0' as tbcol_key_loc
//			    sb.append("&$%__"); // ' ' as tbcol_cd_sez
//			    sb.append("&$%__"); // ' ' as tbcol_cd_loc
//			    sb.append("&$%__"); // ' ' as tbcol_spec_loc
//			    sb.append("&$%__"); // '$' as tbcol_consis

//			    sb.append("&$%"+ar[2-1]); // ' ' as tbese_bid
			    sb.append("&$%"+ar[5-1]); // '$' as tbese_cons_doc
			    sb.append("\n"); // EOL
				
			    ba_Utef8 = sb.toString().getBytes("UTF-8");
			    streamOut.write(ba_Utef8);
			    } // End se snon cancellato				
			    
			    if ((rowCtr & rowCtrSpan) == 0)
			    	System.out.println("Record letti " + rowCtr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // End while
	}
	 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// Close file
	 if (fileIn != null)
		try {
			fileIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	System.out.println("Totale record letti " + rowCtr);
    	System.out.println("Totale record con flagCanc a S " + recordsCancellati);
} // End doSelect950esemplare


void run()
{
	try
	{
	// Inventari
	if (step.equals("stepInventari"))
	{
		streamOut = new FileOutputStream(outputFilename);  
		doSelect950inventario(); 
		streamOut.close();
	}
	
	// Collocazioni
	else if (step.equals("stepCollocazioni"))
	{
		streamOut = new FileOutputStream(outputFilename);  
	 	doSelect950collocazione();
		streamOut.close();
	}
 	
	// Esemplari
	else if (step.equals("stepEsemplari"))
	{
	streamOut = new FileOutputStream(outputFilename);  
	doSelect950esemplare();
	streamOut.close();
	}
//
//	// Step 2		
//	// Concatenare e ordinare per bid
//	// Vedi concatena.bat
	
/*
 NON + necessatio
 
	// Step 3 Mettere i dati nei campi mancanti
	streamOut = new FileOutputStream(outputFilenameFillData);  
	doSelect950fillData();
	streamOut.close();
*/
	
	}
	catch(Exception fnfEx)
	{
	    fnfEx.printStackTrace();
	    return;
	}
		
	// Esegui trattamento delle singole tabelle
	// ----------------------------------------
	System.out.println("\nFine elaborazione "+ DateUtil.getDate() + " " + DateUtil.getTime()); 
}



/**
public void doSelect950fillData() {
	String data = ""; 
	int rowCtr = 0;
	StringBuffer sb = new StringBuffer();
	BufferedReader in;
	String s;
	String tbcol_cd_doc, tbese_cd_doc, tbcol_bid_doc;
	String tbinv_key_loc, tbcol_key_loc; 
	String ar[];
	byte[] ba_Utef8;
	BufferedReader fileIn = null;

	ArrayList alEse = new ArrayList();
	ArrayList alEseNew = new ArrayList();
	ArrayList alCol = new ArrayList();
	ArrayList alColNew = new ArrayList();
	ArrayList alInv = new ArrayList();
	ArrayList alInvNew = new ArrayList();
	ArrayList alICEout = new ArrayList();
	String arInv[], arCol[], arEse[];
	
	String lastBid = "";
	int ctr=0;
	int ctrInv=-1;
	
	try {
		fileIn = new BufferedReader(new FileReader(inputFilenameFillData));
		System.out.println("\nElaborazione di " + inputFilenameFillData + " " +DateUtil.getDate() + " " + DateUtil.getTime()); 

		// Leggiamo le configurazioni di base
		// ----------------------------------
		while (true) {
			try {
				data = fileIn.readLine();
				if (data == null)
					break;
				else if (Misc.emptyString(data))
					continue;
				rowCtr++;

				
				// Scomponiamo la riga dei dati in un array
				ar = MiscString.estraiCampiConEscapePerSeparatore(data, stringSepDiCampoInArray, escapeCharacter);
				if (ar[1-1].equals(lastBid) == false)
				{
					// Elabora blocco delle 950
					if (lastBid.length() != 0)
					{
						
						// Troviamo tutti gli inventari
						// Controlliamo che tbinv.key_loc is null
						// Troviamo tutte le collocazioni
						for (int i=0; i < alInv.size(); i++)
						{
							// Controlliamo  tbcol.bid_doc is null
							tbinv_key_loc = ((String [])(alInv.get(i)))[2-1];
							if (!tbinv_key_loc.equals("NULL"))
								continue;
							alInvNew.add(alInv.get(i)); 
						}						

						// Troviamo tutte le collocazioni
						for (int i=0; i < alCol.size(); i++)
						{
							// Controlliamo  tbcol.bid_doc is null
							tbcol_bid_doc = ((String [])(alCol.get(i)))[3-1];
							if (tbcol_bid_doc.equals("NULL"))
								continue;
							
							// Controlliamo  tbinv.key_loc = tbcol.key_loc
							tbcol_key_loc = ((String [])(alCol.get(i)))[14-1];
							for (int j=0; j < alInv.size(); j++)
							{
								tbinv_key_loc = ((String [])(alInv.get(j)))[2-1];
								if (!tbcol_key_loc.equals(tbinv_key_loc))
									continue;
								// Settiamo le informazioni mancanti dell'inventario
								arInv = (String [])alInv.get(j);
								arCol = (String [])alCol.get(i);
								
								arCol[7-1] = arInv[7-1]; // tbinv_cd_bib
								arCol[8-1] = arInv[8-1]; // tbinv_cd_serie
								arCol[9-1] = arInv[9-1]; // tbinv_cd_inven
								arCol[10-1] = arInv[10-1]; // tbinv_flg_disp
								arCol[11-1] = arInv[11-1]; // tbinv_data_scarico
								arCol[12-1] = arInv[12-1]; // tbinv_seq_coll
								arCol[13-1] = arInv[13-1]; // tbinv_precis_inv

								alColNew.add(arCol); 
							}
						}
						
						// Troviamo tutti gli esemplari
						for (int i=0; i < alEse.size(); i++)
						{
							// Controlliamo  tbcol.cd_doc = tbese.cd_doc
							tbese_cd_doc = ((String [])(alEse.get(i)))[20-1];
							for (int j=0; j < alColNew.size(); j++)
							{
								tbcol_cd_doc = ((String [])(alColNew.get(j)))[4-1];
								if (!tbese_cd_doc.equals(tbcol_cd_doc))
									continue;

								// Settiamo le informazioni mancanti dell'inventario
								arEse = (String [])alEse.get(i);
								arCol = (String [])alCol.get(j);

								tbcol_key_loc = (arCol[14-1]);
								int k=0;
								arInv=null;
								for (; k < alInv.size(); k++)
								{
									tbinv_key_loc = ((String [])(alInv.get(k)))[2-1];
									if (!tbcol_key_loc.equals(tbinv_key_loc))
										continue;
									// Settiamo le informazioni mancanti dell'inventario
									arInv = (String [])alInv.get(j);
									break;
								}
								if (k == alInv.size())
									continue; // non trovato

                                arEse[7-1] = arInv[7-1]; // tbinv_cd_bib
                                arEse[8-1] = arInv[8-1]; // tbinv_cd_serie
                                arEse[9-1] = arInv[9-1]; // tbinv_cd_inven
                                arEse[10-1] = arInv[10-1]; // tbinv_flg_disp
                                arEse[11-1] = arInv[11-1]; // tbinv_data_scarico
                                arEse[12-1] = arInv[12-1]; // tbinv_seq_coll
                                arEse[13-1] = arInv[13-1]; // tbinv_precis_inv

                                arEse[14-1] = arCol[14-1]; // tbcol_key_loc
                                arEse[15-1] = arCol[15-1]; // tbcol_cd_sez
                                arEse[16-1] = arCol[16-1]; // tbcol_cd_loc
                                arEse[17-1] = arCol[17-1]; // tbcol_spec_loc
                                arEse[18-1] = arCol[18-1]; // tbcol_consis

                                alEseNew.add(arEse); 
							}
						}
						

						
						// Concateniamo tutti gli elementi trovati
						alICEout.clear();
						for (int i=0; i<alInvNew.size(); i++)
							alICEout.add(alInvNew.get(i));
						for (int i=0; i<alColNew.size(); i++)
							alICEout.add(alColNew.get(i));
						for (int i=0; i<alEseNew.size(); i++)
							alICEout.add(alEseNew.get(i));
						
						
						
						// Scriviamo le 950 per il BID in questione
						for (int i=0; i<alICEout.size(); i++)
						{
							ar = (String [])alICEout.get(i);
							sb.setLength(0);
							
						    // Componiamo il record in uscita
						    sb.append(ar[1-1]); 		// tbinv_bid
						    sb.append("&$%"+ar[1-1]); 	// tbinv_key_loc
						    sb.append("&$%"+ar[2-1]);	// tbcol_bid_doc
						    sb.append("&$%"+ar[3-1]);	// tbcol_cd_doc
						    sb.append("&$%"+ar[4-1]); 	// tbese_cd_biblioteca
						    sb.append("&$%"+ar[5-1]); 	// ID Tabelal

						    sb.append("&$%"+ar[6-1]); // tbinv_cd_bib
						    sb.append("&$%"+ar[7-1]); // tbinv_cd_serie
						    sb.append("&$%"+ar[8-1]); // tbinv_cd_inven
						    sb.append("&$%"+ar[9-1]); // tbinv_flg_disp
						    sb.append("&$%"+ar[10-1]); // tbinv_data_scarico
						    sb.append("&$%"+ar[11-1]); // tbinv_seq_coll
						    sb.append("&$%"+ar[12-1]); // tbinv_precis_inv
						    sb.append("&$%"+ar[13-1]); // '0' as tbcol_key_loc
						    sb.append("&$%"+ar[14-1]); // ' ' as tbcol_cd_sez
						    sb.append("&$%"+ar[15-1]); // ' ' as tbcol_cd_loc
						    sb.append("&$%"+ar[16-1]); // ' ' as tbcol_spec_loc
						    sb.append("&$%"+ar[17-1]); // '$' as tbcol_consis
						    sb.append("&$%"+ar[18-1]); // ' ' as tbese_bid
						    sb.append("&$%"+ar[19-1]); // '0' as tbese_cd_doc
						    sb.append("&$%"+ar[20-1]); // '$' as tbese_cons_doc
						    sb.append("\n"); // EOL
							
						    ba_Utef8 = sb.toString().getBytes("UTF-8");
						    streamOut.write(ba_Utef8);
							
							
						}
					}
					ctr=0;
					alEse.clear();
					alCol.clear();
					alInv.clear();
					alEseNew.clear();
					alColNew.clear();
					alInvNew.clear();
					
					alICEout.clear();
					lastBid = ar[1-1];
				}

			if (ar[6-1].equals("TB_ESE") == true) // ctrInv == -1  &&  
				alEse.add(ar);
			else if (ar[6-1].equals("TB_COL") == true) // ctrInv == -1  &&  
				alCol.add(ar);
			else if (ar[6-1].equals("TB_INV") == true) // ctrInv == -1  &&  
				alInv.add(ar);
			ctr++;
				
				
				
//				
//			    } // End se snon cancellato				
			    
			    if ((rowCtr & rowCtrSpan) == 0)
			    	System.out.println("Record letti " + rowCtr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // End while
	}
	 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// Close file
	 if (fileIn != null)
		try {
			fileIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	System.out.println("Totale record letti " + rowCtr);

		// Ordiniamo gli elementi trovati con unix sort


} // End doSelect950fillData
*/






} // End Select950

