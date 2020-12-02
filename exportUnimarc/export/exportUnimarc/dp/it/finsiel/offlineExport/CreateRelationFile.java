package it.finsiel.offlineExport;

/*
Parametri per deub  
/media/export70/indice/dp/input/tr_aut_bib.out.bytes.srt /media/export70/indice/dp/input/tr_aut_bib.out.bytes.srt.rel 1 db=indice

/media/export/exportUnimarc/dp/input/trs_termini_titoli_biblioteche.out.srt /media/export/exportUnimarc/dp/input/trs_termini_titoli_biblioteche.out.srt.rel 1 db=sbnweb
 
*/

//import ConvertConstants;

import it.finsiel.misc.DateUtil;
import it.finsiel.misc.MiscString;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
//import MiscStringTokenizer;


public class CreateRelationFile {
	final public int DB_SBNWEB = 1;
	final public int DB_INDICE = 2;
	final public int DB_IMPORT_SOGGETTI_INDICE = 3;
	int db = DB_SBNWEB;
	
	BufferedReader in;
//	BufferedReader offsetsOrdinatoIn = null;
	BufferedWriter out;
//	String inputFile, outputFile;
	long rowCtr;
	long skipLines = 0; // 3907584; // 2670592; // 876544; // 3202540
	int offsetLength = 11;
	
	static String stringSepDiCampoInArray[] = { "&$%"};  
	String[] ar;
	static char escapeCharacter = '\\'; // il backslash di default
  	
	final static int RELAZIONE_TITOLO_TITOLO = 1;
	final static int RELAZIONE_TITOLO_TITOLO_INV = 2;

	final static int RELAZIONE_TITOLO_AUTORE = 3;
	final static int RELAZIONE_TITOLO_AUTORE_INV = 4;
	
	final static int RELAZIONE_TITOLO_MARCA = 5;
	final static int RELAZIONE_TITOLO_MARCA_INV = 6;
	
	final static int RELAZIONE_TITOLO_SOGGETTO_BIBLIOTECA = 7;
	final static int RELAZIONE_TITOLO_SOGGETTO_BIBLIOTECA_INV = 8;

	
	final static int RELAZIONE_TITOLO_CLASSE = 9;
	final static int RELAZIONE_TITOLO_CLASSE_INV = 10;
	
	final static int RELAZIONE_AUTORE_AUTORE = 11;
	final static int RELAZIONE_AUTORE_AUTORE_INV = 12;

	final static int RELAZIONE_TITOLO_LUOGO = 13;
	final static int RELAZIONE_TITOLO_LUOGO_INV = 14;
	
	
	
	final static int RELAZIONE_TITOLO_TITOLO_INDICE = 16;
	final static int RELAZIONE_TITOLO_TITOLO_INV_INDICE = 17;

	final static int RELAZIONE_AUTORE_AUTORE_INDICE = 18;
	final static int RELAZIONE_AUTORE_AUTORE_INV_INDICE = 19;

	final static int RELAZIONE_TITOLO_AUTORE_INDICE = 20;
	final static int RELAZIONE_TITOLO_AUTORE_INV_INDICE = 21;
	
	final static int RELAZIONE_TITOLO_CLASSE_INDICE = 22;
	final static int RELAZIONE_TITOLO_CLASSE_INV_INDICE = 23;

	final static int RELAZIONE_TITOLO_LUOGO_INDICE = 24;
	final static int RELAZIONE_TITOLO_LUOGO_INV_INDICE = 25;

	final static int RELAZIONE_TITOLO_MARCA_INDICE = 26;
	final static int RELAZIONE_TITOLO_MARCA_INV_INDICE = 27;

	final static int RELAZIONE_TITOLO_SOGGETTO_INDICE = 28;
	final static int RELAZIONE_TITOLO_SOGGETTO_INV_INDICE = 29;

	final static int RELAZIONE_TITOLO_SOGGETTO_INDICE_IMPORT = 32;
	final static int RELAZIONE_TITOLO_SOGGETTO_INV_INDICE_IMPORT = 33;

	
	final static int RELAZIONE_AUTORE_BIBLIOTECA = 30;
	
	final static int RELAZIONE_TITOLO_TESAURO = 31;	// 03/12/2015
	
	
	
	boolean usingOffsets = false;
	
	//	final static int RELAZIONE_TITOLO_LUOGO = ;

	//	final static int RELAZIONE_TITOLO_BIBLIOTECA = ;
//	final static int RELAZIONE_AUTORE_LUOGO = ;
//	final static int RELAZIONE_AUTORE_MARCA = ;
//	final static int RELAZIONE_LUOGO_LUOGO = ;

	int relazione;
	String savedSourceId="";
	String tipoLegame, tipoLegameMusica, cdNaturaBase, cdNaturaColl, sequenza, tpLuogo;
	String tpResponsabilita, cdRelazione, flIncerto, flSuperfluo,  cdStrumentoMusicale;
	String cdBiblioteca;
	String sourceId, targetId, targetPolo, targetBiblioteca;
	
	    

		
		










void run(String inputFile, String outputFile, int campiInTabella) 
{
		String s;
		
			try {
				in = new BufferedReader(new FileReader(inputFile));
				out = new BufferedWriter(new FileWriter(outputFile));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				

			// Faccio fuori la prima riga pr evitare un test nel loop
			try {
				s = in.readLine();
			if (s == null)
				return;
			
			if (campiInTabella == 1 && relazione == RELAZIONE_AUTORE_BIBLIOTECA) // 14/10/2015
			{
				ar = new String[3];
				ar[0] = s.substring(0,10);	// VID
				ar[1] = s.substring(10,13);	// Polo
				ar[2] = s.substring(13);	// Biblioteca
			}
			else	
				ar = MiscString.estraiCampiConEscapePerSeparatore(s, stringSepDiCampoInArray, escapeCharacter);

			//stampaArray(ar);
			if (relazione == RELAZIONE_AUTORE_BIBLIOTECA) // 14/10/2015
			{
				savedSourceId = sourceId = ar[0];	// VID
				out.write(sourceId );

				out.write("|" + ar[1] + ar[2]); // VID + Polo + biblioteca

			}
			else if (relazione == RELAZIONE_TITOLO_TITOLO || relazione == RELAZIONE_TITOLO_TITOLO_INV)
			{
				if (creaRelazioneTitTit(ar))
					out.write(sourceId + "|" + targetId +','+ tipoLegame +','+ tipoLegameMusica +','+ cdNaturaBase +','+ cdNaturaColl +','+ sequenza +','); // Chiudiamo con ',' per assicurarci i campi vuoti
				savedSourceId = sourceId;
			}
			else if (relazione == RELAZIONE_TITOLO_TITOLO_INDICE || relazione == RELAZIONE_TITOLO_TITOLO_INV_INDICE)
			{
				if (creaRelazioneTitTitInvIndice(ar))
				{
					out.write(sourceId + "|" + targetId +','+ tipoLegame +','+ tipoLegameMusica +','+ cdNaturaBase +','+ cdNaturaColl +','+ sequenza +',');
					savedSourceId=sourceId;
				}
			}

			
			else if (relazione == RELAZIONE_TITOLO_AUTORE || relazione == RELAZIONE_TITOLO_AUTORE_INV)
			{
				creaRelazioneTitAut(ar);
				// 1 titolo legato a + autori
				out.write(sourceId + "|" + targetId +','+ tpResponsabilita +','+ cdRelazione +','+ flIncerto +','+ flSuperfluo + ',' + cdStrumentoMusicale +','); //  
				savedSourceId = sourceId;
			}
			else if (relazione == RELAZIONE_TITOLO_AUTORE_INDICE || relazione == RELAZIONE_TITOLO_AUTORE_INV_INDICE)
			{
				creaRelazioneTitAutIndice(ar);
				out.write(sourceId + "|" + targetId +','+ tpResponsabilita +','+ cdRelazione +','+ flIncerto +','+ flSuperfluo + ',' + cdStrumentoMusicale +',' ); // 
				savedSourceId=sourceId;
			}
			
			else if (relazione == RELAZIONE_TITOLO_SOGGETTO_BIBLIOTECA )
			{
				creaRelazioneTitSogBib(ar);
				out.write(sourceId + "|" + targetId +','+ cdBiblioteca +','); 
				savedSourceId = sourceId;
			}
			
//			else if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE )
//			{
//				creaRelazioneTitSog(ar);
//				out.write(sourceId + "|" + targetId); 
//				savedSourceId = sourceId;
//			}
			
			else if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE || relazione == RELAZIONE_TITOLO_SOGGETTO_INV_INDICE)
			{
				creaRelazioneTitSogIndice(ar);
				out.write(sourceId + "|" + targetId); 
				savedSourceId=sourceId;
			}
			else if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE_IMPORT || relazione == RELAZIONE_TITOLO_SOGGETTO_INV_INDICE_IMPORT)
			{
				creaRelazioneTitSogIndiceImport(ar);
				out.write(sourceId + "|" + targetId + ","+targetPolo+","+targetBiblioteca); 
				savedSourceId=sourceId;
			}
			
			
			
			else if (relazione == RELAZIONE_TITOLO_CLASSE )
			{
				creaRelazioneTitCla(ar);
				// 1 titolo legato a + autori
				out.write(sourceId + "|" + targetId); 
				savedSourceId = sourceId;
			}
			else if (relazione == RELAZIONE_TITOLO_CLASSE_INDICE || relazione == RELAZIONE_TITOLO_CLASSE_INV_INDICE)
			{
				creaRelazioneTitClaIndice(ar);
				out.write(sourceId + "|" + targetId); 
				savedSourceId=sourceId;
			}
			else if (relazione == RELAZIONE_TITOLO_TESAURO)
			{
				creaRelazioneTitTesauro(ar);
				out.write(sourceId + "|" + targetId); 
				savedSourceId=sourceId;
			}

			else if (relazione == RELAZIONE_TITOLO_MARCA )
			{
				creaRelazioneTitMar(ar);
				// 1 titolo legato a + autori
				out.write(sourceId + "|" + targetId); 
				savedSourceId = sourceId;
			}
			else if (relazione == RELAZIONE_TITOLO_MARCA_INDICE)
			{
				creaRelazioneTitMarIndice(ar);
				out.write(sourceId + "|" + targetId); 
				savedSourceId=sourceId;
			}
			
			else if (relazione == RELAZIONE_AUTORE_AUTORE || relazione == RELAZIONE_AUTORE_AUTORE_INV)
			{
				creaRelazioneAutAut(ar);
				out.write(sourceId + "|" + targetId +',' + tipoLegame); 
				savedSourceId = sourceId;
			}
			else if (relazione == RELAZIONE_AUTORE_AUTORE_INDICE || relazione == RELAZIONE_AUTORE_AUTORE_INV_INDICE)
			{
				creaRelazioneAutAutIndice(ar);
				out.write(sourceId+ "|" + targetId +',' + tipoLegame); 
				savedSourceId=sourceId;
			}
			
			else if (relazione == RELAZIONE_TITOLO_LUOGO || relazione == RELAZIONE_TITOLO_LUOGO_INV)
			{
				creaRelazioneTitLuo(ar);
				out.write(sourceId + "|" + targetId +','+ tpLuogo);
				savedSourceId = sourceId;
			}
			else if (relazione == RELAZIONE_TITOLO_LUOGO_INDICE || relazione == RELAZIONE_TITOLO_LUOGO_INV_INDICE)
			{
				creaRelazioneTitLuoIndice(ar);
				out.write(sourceId + "|" + targetId +','+ tpLuogo);
				savedSourceId=sourceId;
			}
			
			rowCtr = 1;
			
			// Leggiamo i dati
			// ---------------
			while (true) {
//			while (rowCtr < 100) {
				try {
					s = in.readLine();
					
					rowCtr++;
										
					if (s == null)
						break;
					
//					if (s.isEmpty())
//						continue;

					if (campiInTabella == 1 && relazione == RELAZIONE_AUTORE_BIBLIOTECA) // 14/10/2015
					{
						ar = new String[3];
						ar[0] = s.substring(0,10);	// VID
						ar[1] = s.substring(10,13);	// Polo
						ar[2] = s.substring(13);	// Biblioteca
					}
					else	
						ar = MiscString.estraiCampi(s, stringSepDiCampoInArray, false); // 02/12/2009 15.38
					
//stampaArray(ar);

					// Controlliamo se abbiamo dei record spezzati causa LF
//					if (ar.length != campiInTabella)
					if (relazione != RELAZIONE_AUTORE_BIBLIOTECA && ar.length != campiInTabella)
					{
						// Bypassiamo bug in estrai campi 
						if (ar.length < (campiInTabella-1))
						{
						// Cerchiamo di recuperare la parte mancante!!
						System.out.println("Record " + rowCtr + " spezzato" + s);
						while (true)
						{
							String s2 = in.readLine();
							if (s2 == null)
							{
								System.out.println("Record " + rowCtr +" non terminato" );
								return;
							}
							// Concateniamo gli array
							s = s+s2;
							ar = MiscString.estraiCampiConEscapePerSeparatore(s, stringSepDiCampoInArray, escapeCharacter);
							if (ar.length == campiInTabella) // Completato i campi di una tabella
								break;

							// 12/12/20010
							if (ar.length > campiInTabella) // Problema 0 binario con sort aix
							{
								ar = MiscString.estraiCampi(s2, stringSepDiCampoInArray, false); 
								if (ar.length == campiInTabella)
								{
									System.out.println("Record " + " troncato: " + s);
									break;
								}
							}
						} // End while
						}
						
						}
					
					if (relazione == RELAZIONE_AUTORE_BIBLIOTECA) // 14/10/2015
					{
						sourceId = ar[0];
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId = sourceId;	// VID
						}

						out.write("|" + ar[1] + ar[2]); // Polo + biblioteca
						
					}
					else if (relazione == RELAZIONE_TITOLO_TITOLO || relazione == RELAZIONE_TITOLO_TITOLO_INV)
					{
						if (creaRelazioneTitTit(ar))
						{
							if (!sourceId.equals(savedSourceId))
							{
								out.write("\n" + sourceId );
								savedSourceId=sourceId;
							}
							out.write("|" + targetId +','+ tipoLegame +','+ tipoLegameMusica +','+ cdNaturaBase +','+ cdNaturaColl +','+ sequenza +',');
						}
					}
					else if (relazione == RELAZIONE_TITOLO_TITOLO_INDICE || relazione == RELAZIONE_TITOLO_TITOLO_INV_INDICE)
					{
						if (creaRelazioneTitTitInvIndice(ar))
						{
							if (!sourceId.equals(savedSourceId))
							{
								out.write("\n" + sourceId );
								savedSourceId=sourceId;
							}
							out.write( "|" + targetId +','+ tipoLegame +','+ tipoLegameMusica +','+ cdNaturaBase +','+ cdNaturaColl +','+ sequenza +',');
						}
					}
					
					else if (relazione == RELAZIONE_TITOLO_AUTORE || relazione == RELAZIONE_TITOLO_AUTORE_INV)
					{
						creaRelazioneTitAut(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId +','+ tpResponsabilita +','+ cdRelazione +','+ flIncerto +','+ flSuperfluo + ',' + cdStrumentoMusicale +','); //
					}
					else if (relazione == RELAZIONE_TITOLO_AUTORE_INDICE || relazione == RELAZIONE_TITOLO_AUTORE_INV_INDICE)
					{
						creaRelazioneTitAutIndice(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId +','+ tpResponsabilita +','+ cdRelazione +','+ flIncerto +','+ flSuperfluo + ',' + cdStrumentoMusicale +','); // 
					}

					else if (relazione == RELAZIONE_TITOLO_SOGGETTO_BIBLIOTECA )
					{
						creaRelazioneTitSogBib(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId +','+ cdBiblioteca); 
					}
					else if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE || relazione == RELAZIONE_TITOLO_SOGGETTO_INV_INDICE)
					{
						creaRelazioneTitSogIndice(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId); 
					}
					

					else if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE_IMPORT || relazione == RELAZIONE_TITOLO_SOGGETTO_INV_INDICE_IMPORT)
					{
						creaRelazioneTitSogIndiceImport(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId+","+targetPolo+","+targetBiblioteca); 
					}
					
					
					else if (relazione == RELAZIONE_TITOLO_CLASSE )
					{
						creaRelazioneTitCla(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId); 
					}
					else if (relazione == RELAZIONE_TITOLO_CLASSE_INDICE || relazione == RELAZIONE_TITOLO_CLASSE_INV_INDICE)
					{
						creaRelazioneTitClaIndice(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId); 
					}
					else if (relazione == RELAZIONE_TITOLO_TESAURO)
					{
						creaRelazioneTitTesauro(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId); 
						savedSourceId=sourceId;
					}
					
					else if (relazione == RELAZIONE_TITOLO_MARCA )
					{
						creaRelazioneTitMar(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId); 
					}
					else if (relazione == RELAZIONE_TITOLO_MARCA_INDICE)
					{
						creaRelazioneTitMarIndice(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId); 
					}
					
					else if (relazione == RELAZIONE_AUTORE_AUTORE || relazione == RELAZIONE_AUTORE_AUTORE_INV)
					{
						creaRelazioneAutAut(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId); //  +',' + tipoLegame
							savedSourceId=sourceId;
						}
						out.write("|" + targetId +',' + tipoLegame); 
					}
					else if (relazione == RELAZIONE_AUTORE_AUTORE_INDICE || relazione == RELAZIONE_AUTORE_AUTORE_INV_INDICE)
					{
						creaRelazioneAutAutIndice(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId); //  +',' + tipoLegame
							savedSourceId=sourceId;
						}
						out.write("|" + targetId +',' + tipoLegame); 
					}
					
					else if (relazione == RELAZIONE_TITOLO_LUOGO || relazione == RELAZIONE_TITOLO_LUOGO_INV)
					{
						creaRelazioneTitLuo(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId +','+ tpLuogo);
					}
					else if (relazione == RELAZIONE_TITOLO_LUOGO_INDICE || relazione == RELAZIONE_TITOLO_LUOGO_INV_INDICE)
					{
						creaRelazioneTitLuoIndice(ar);
						if (!sourceId.equals(savedSourceId))
						{
							out.write("\n" + sourceId );
							savedSourceId=sourceId;
						}
						out.write("|" + targetId +','+ tpLuogo);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ((rowCtr & 0x1FFF) == 0)
				{
					System.out.print("\nRiga " + rowCtr);
				}

			} // End while
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				in.close();
				
				out.write("\n"); // Per evitare problemi alla CreateOffsetFile. 27/08/2012
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("\nRighe fatte " + rowCtr);

}// End run		







boolean 	creaRelazioneTitTit(String ar[])
{
	if (relazione != RELAZIONE_TITOLO_TITOLO_INV)
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
	if (sourceId.equals(targetId))
	{
		System.out.print("\nERRORE: SOURCE ID " + sourceId + " uguale a TARGET ID " + targetId);
		return false;
	}
	
	tipoLegame = ar[2];
	tipoLegameMusica = ar[3];
	cdNaturaBase = ar[4];
	cdNaturaColl = ar[5];
	sequenza  = ar[6].trim();

//ATTENDO CONFERMA 14/10/2015	if(db == DB_INDICE)
//		sequenza  = ar[6]; // 12/10/2015 No trim (Mail Mataloni 05/10/2015)
//	else
//		sequenza  = ar[6].trim();
		
	
	// if (sequenza.isEmpty()) // 13/10/2009 11.44

// 02/02/2010 15.48
//	if (sequenza.length() == 0) // 13/10/2009 11.44
//		sequenza = "null"; 
	return true;
}

boolean 	creaRelazioneTitTitInvIndice(String ar[])
{
	if   (ar.length < 2)
	{
		System.out.print("\nERRORE: ARRAY < 2 ");
		if   (ar.length == 1)
			System.out.print( ar[0]);
		return false;
	}
		
	if (relazione == RELAZIONE_TITOLO_TITOLO_INDICE)
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
	
//		sourceId = ar[1];
//		targetId = ar[0];

	if (sourceId.equals(targetId))
	{
		System.out.print("\nERRORE: SOURCE ID " + sourceId + " uguale a TARGET ID " + targetId);
		return false;
	}
	
	tipoLegame = ar[2];
	tipoLegameMusica = ar[3];
	cdNaturaBase = ar[4];
	cdNaturaColl = ar[5];
	sequenza  = ar[6].trim();

	//ATTENDO CONFERMA 14/10/2015	if(db == DB_INDICE)
//	if(db == DB_INDICE)
//		sequenza  = ar[6]; // 12/10/2015 No trim (Mail Mataloni 05/10/2015)
//	else
//		sequenza  = ar[6].trim();
	
	
	// if (sequenza.isEmpty()) // 13/10/2009 11.44
//	 02/02/2010 15.48
//	if (sequenza.length() == 0) // 13/10/2009 11.44
//		sequenza = "null"; 
	return true;
}




void 	creaRelazioneAutAut(String ar[])
{
	if (relazione == RELAZIONE_AUTORE_AUTORE )
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
	tipoLegame = ar[2];
}

void 	creaRelazioneAutAutIndice(String ar[])
{
	if (relazione == RELAZIONE_AUTORE_AUTORE_INDICE )
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
	tipoLegame = ar[2];
}

void 	creaRelazioneTitAut(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_AUTORE)
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
	tpResponsabilita=ar[2]; 
	cdRelazione=ar[3]; 
	
	flIncerto=ar[5]; 
	flSuperfluo=ar[6]; 
	cdStrumentoMusicale=ar[7].trim(); // 23/012015 
	
} // End creaRelazioneTitAut


void 	creaRelazioneTitAutIndice(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_AUTORE_INDICE)
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
	tpResponsabilita=ar[2]; 
	cdRelazione=ar[3]; 
	
//	flIncerto=ar[6]; 
//	flSuperfluo=ar[7]; 
	flIncerto=ar[5]; // 23/012015
	flSuperfluo=ar[6]; 
	cdStrumentoMusicale=ar[7].trim(); // 23/012015 
} // End creaRelazioneTitAut




void 	creaRelazioneTitLuo(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_LUOGO)
	{
		sourceId = ar[0];
		targetId = ar[1].trim();
	}
	else // 
	{
		sourceId = ar[1].trim();
		targetId = ar[0];
	}
	tpLuogo = ar[2];
}

void 	creaRelazioneTitLuoIndice(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_LUOGO_INDICE)
	{
		sourceId = ar[0];
		targetId = ar[1].trim();
	}
	else // 
	{
		sourceId = ar[1].trim();
		targetId = ar[0];
	}
	tpLuogo = ar[2];
}



void 	creaRelazioneTitMar(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_MARCA)
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
}

void 	creaRelazioneTitMarIndice(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_MARCA_INDICE)
	{
		sourceId = ar[0];
		targetId = ar[1];
	}
	else // 
	{
		sourceId = ar[1];
		targetId = ar[0];
	}
}






void 	creaRelazioneTitSogIndice(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE)
	{
		sourceId = ar[0]; // Bid
		targetId = ar[1]; // Cid
	}
	else // 
	{
		sourceId = ar[1]; // Cid
		targetId = ar[0]; // Bid
	}
	cdBiblioteca = "???";
} // End creaRelazioneTitSogIndice

void 	creaRelazioneTitSogIndiceImport(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE_IMPORT)
	{
		sourceId = ar[1]; // Bid
		targetId = ar[0]; // Cid
		targetPolo = ar[2];
		targetBiblioteca = ar[3];
	}
	else // 
	{
		sourceId = ar[0]; // Cid
		targetId = ar[1]; // Bid
		targetPolo = ar[2];
		targetBiblioteca = ar[3];
	}
	cdBiblioteca = "???";
} // End creaRelazioneTitSogIndice




// 03/12/2015
void 	creaRelazioneTitTesauro(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_TESAURO)
	{
		sourceId = ar[0];
		targetId = ar[4].trim();
	}
	else // 
	{
		sourceId = "??"; // ar[1].trim();
		targetId = "??"; // ar[0];
	}
}




public static void main(String args[])
{
	if(args.length < 3)
    {
//        System.out.println("Uso: CreateRelationFile filenameIn filenameOut relationFromStart relationFromEnd relationToStart relationToEnd (posizioni iniziano da 1) []");
        System.out.println("Uso: CreateRelationFile filenameIn filenameOut numeroCampiInTabella [fileOffsetOrdinatoIn keyLength]");
        System.exit(1);
    }
	stampaVersione();
	String start=
	 "\n"+
	 "\nfilenameIn " + args[0] + 
	 "\nfilenameOut " + args[1] +
	 "\ncampi in tabella " + args[2] ;
    
    System.out.println(start);
    CreateRelationFile createRelationFile = new CreateRelationFile();
    String offsetFileOrdinatoIn = null;
    int keyLength = -1;
    
//    if (args.length > 4)
     if (args.length > 3)
    {
//        System.out.println("\nfileOffsetOrdinatoIn " + args[3]);
//        createRelationFile.usingOffsets = true;
//        offsetFileOrdinatoIn = args[3];
//        keyLength = Integer.parseInt(args[4], 10);
        
		// Prendiamo i parametri con =
		for (int i= 3; i < args.length; i++)
		{
			if (args[i].indexOf("usingOffsets=") != -1)
				createRelationFile.usingOffsets = true;
			else if (args[i].indexOf("db=") != -1)
			{
				if (args[i].indexOf("db=indice") != -1)
					createRelationFile.db = createRelationFile.DB_INDICE;	
				else if (args[i].indexOf("db=import_soggetti_indice") != -1)
					createRelationFile.db = createRelationFile.DB_IMPORT_SOGGETTI_INDICE;	
			}
		}
    }

//  ------    

    
    if (args[0].indexOf("tr_aut_aut.out.inv") != -1) // else 
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
        	createRelationFile.relazione = RELAZIONE_AUTORE_AUTORE_INV_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_AUTORE_AUTORE_INV;
    }
    else if (args[0].indexOf("tr_aut_aut.out") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
        	createRelationFile.relazione = RELAZIONE_AUTORE_AUTORE_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_AUTORE_AUTORE;
    }


    else if (args[0].indexOf("tr_tit_mar.out") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
        	createRelationFile.relazione = RELAZIONE_TITOLO_MARCA_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_MARCA;
    }
    
    else if (args[0].indexOf("tr_tit_luo.out") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
        	createRelationFile.relazione = RELAZIONE_TITOLO_LUOGO_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_LUOGO;
    }
    
    else if (args[0].indexOf("tr_tit_cla.out") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
        	createRelationFile.relazione = RELAZIONE_TITOLO_CLASSE_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_CLASSE;
    }

    else if (args[0].indexOf("tr_tit_aut.out.inv") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
        	createRelationFile.relazione = RELAZIONE_TITOLO_AUTORE_INV_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_AUTORE_INV;
    }
    else if (args[0].indexOf("tr_tit_aut.out") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
        	createRelationFile.relazione = RELAZIONE_TITOLO_AUTORE_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_AUTORE;
    }
    
    
    else if (args[0].indexOf("tr_tit_tit.out.inv") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
    		createRelationFile.relazione = RELAZIONE_TITOLO_TITOLO_INV_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_TITOLO_INV;
    }
    else if (args[0].indexOf("tr_tit_tit.out") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_INDICE)
    		createRelationFile.relazione = RELAZIONE_TITOLO_TITOLO_INDICE;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_TITOLO;
    	
    }


    
    
    else if ( args[1].indexOf("tr_tit_sog.out.inv") != -1) // .off.srt
    {
    	if (createRelationFile.db == createRelationFile.DB_IMPORT_SOGGETTI_INDICE)
    		createRelationFile.relazione = RELAZIONE_TITOLO_SOGGETTO_INV_INDICE_IMPORT;
    	else	
    		createRelationFile.relazione = RELAZIONE_TITOLO_SOGGETTO_INV_INDICE;
    }
    else if ( args[1].indexOf("tr_tit_sog.out") != -1)
    {
    	if (createRelationFile.db == createRelationFile.DB_IMPORT_SOGGETTI_INDICE)
    		createRelationFile.relazione = RELAZIONE_TITOLO_SOGGETTO_INDICE_IMPORT;
    	else
    		createRelationFile.relazione = RELAZIONE_TITOLO_SOGGETTO_INDICE;
    }
    
    
    
    
    
    else if (args[0].indexOf("tr_tit_sog_bib.out") != -1)
    	createRelationFile.relazione = RELAZIONE_TITOLO_SOGGETTO_BIBLIOTECA;

    else if (args[0].indexOf("tr_aut_bib.out") != -1)
    	createRelationFile.relazione = RELAZIONE_AUTORE_BIBLIOTECA;
    
    else if ( args[1].indexOf("trs_termini_titoli_biblioteche.out") != -1) 
    	createRelationFile.relazione = RELAZIONE_TITOLO_TESAURO;

    
    
    System.out.println("Inizio elaborazione " + DateUtil.getDate() + " " + DateUtil.getTime());
    if (offsetFileOrdinatoIn != null)
    	createRelationFile.run(args[0], args[1], Integer.parseInt(args[2]), offsetFileOrdinatoIn, keyLength);
    else
    	createRelationFile.run(args[0], args[1], Integer.parseInt(args[2]));

    System.out.println("\nFine elaborazione " + DateUtil.getDate() + " " + DateUtil.getTime());
} // End main


static void stampaVersione()
{
	String versione = 
		"CreateRelationFile tool - � Almaviva S.p.A (Argentino Trombin) 2008-2010"+
//	 "\nVersione 1.0.0" +    
//		"\nVersione 1.0.1 del 03/02/2010" + // Aggiunto ',' in coda per gestire l'ultimo campo a null     
		"\nVersione 1.0.2 del 14/10/2015" + // gestione no trim per sequenze indice, gestione legami autori biblioteche     
		"\n=====================================";
    System.out.println(versione);
	
}


void stampaArray(String []ar)
{
    System.out.println("Ar[] ");
	for (int i=0; i < ar.length; i++)
	    System.out.println("Ar["+i+"] '" + ar[i] + "'");

} // End CreateRelationFile









void 	creaRelazioneTitSogBib(String ar[])
{
	if (relazione == RELAZIONE_TITOLO_SOGGETTO_BIBLIOTECA)
	{
		sourceId = ar[4]; // Bid
		targetId = ar[0]; // Cid
	}
	else // 
	{
		sourceId = ar[0]; // Cid
		targetId = ar[4]; // Bid
	}

	cdBiblioteca = ar[1];
} // End creaRelazioneTitSogBib

void 	creaRelazioneTitSog(String ar[])
{
	sourceId = ar[0];
	targetId = ar[1];
} // End creaRelazioneTitSogBib


void 	creaRelazioneTitCla(String ar[])
{
//	if (relazione == RELAZIONE_TITOLO_CLASSE)
//	{
		sourceId = ar[0];		// Bid
//		targetId = ar[2]+ar[1]; // Codice sistema + Descrizione classe
		targetId = ar[2]+ar[3]+ar[1]; // 02/02/2015 Codice sistema + edizione + Descrizione classe

		
//	}
//	else // 
//	{
//		sourceId = ar[2]+ar[1];
//		targetId = ar[0];
//	}
}

void 	creaRelazioneTitClaIndice(String ar[])
{
//	if (relazione == RELAZIONE_TITOLO_CLASSE_INDICE)
//	{
	
		sourceId = ar[0];		// Bid
		targetId = ar[1] + ar[2] + " " + ar[3]; // Codice sistema + edizione + filler + Descrizione classe
//	}
//	else // 
//	{
//		sourceId = ar[1]+ar[3];
//		targetId = ar[0];
//	}
}


/*
 * Metodo per creare le relazioni dove la consecutivita' delle chiavi non e' nella tabella di base ma nel file degli offset sortato
*/
void run(String inputFile, String outputFile, int campiInTabella, String offsetFileOrdinatoIn, int keyLength) 
{
	System.out.println("ERRORE: Funzione obsoleta... 'void run(String inputFile, String outputFile, int campiInTabella, String offsetFileOrdinatoIn, int keyLength)'");
	return;
/*	
		String s, s2;
		 RandomAccessFile rafIn = null, rafOffsetIn = null; 

			try {
				//in = new BufferedReader(new FileReader(inputFile));
				rafIn = new RandomAccessFile(inputFile, "r");
				rafOffsetIn = new RandomAccessFile(offsetFileOrdinatoIn, "r");
				
//				offsetsOrdinatoIn = new BufferedReader(new FileReader(offsetFileOrdinatoIn));
				
				out = new BufferedWriter(new FileWriter(outputFile));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}				

			
			// Dobbiamo saltare delle righe per debuggure file enormi da gigabytes?
			if (skipLines != 0)
			{
				try {
					System.out.println("Skip " + skipLines + " lines"); 
					rafOffsetIn.seek(skipLines*(keyLength+offsetLength+1));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			// Facciao fuori la prima riga pr evitare un test nel loop
			try {
				// Prendiamo l'offset
//				s = offsetsOrdinatoIn.readLine();
				s = rafOffsetIn.readLine();
				
				long pos = Long.parseLong(s.substring(keyLength));
				// Posizioniamoci tramite seek
				rafIn.seek(pos);
				// Leggiamo una riga
				s = rafIn.readLine();
				if (s == null)
					return;
			
			ar = MiscString.estraiCampiConEscapePerSeparatore(s, stringSepDiCampoInArray, escapeCharacter);
			if (relazione == RELAZIONE_TITOLO_TITOLO_INDICE || relazione == RELAZIONE_TITOLO_TITOLO_INV_INDICE)
			{
				if (creaRelazioneTitTitInvIndice(ar))
					out.write(sourceId + "|" + targetId +','+ tipoLegame +','+ tipoLegameMusica +','+ cdNaturaBase +','+ cdNaturaColl +','+ sequenza);
				savedSourceId = sourceId;
			}
			else if (relazione == RELAZIONE_AUTORE_AUTORE_INDICE || relazione == RELAZIONE_AUTORE_AUTORE_INV_INDICE)
			{
				creaRelazioneAutAut(ar);
				// 1 titolo legato a + autori
				out.write(sourceId + "|" + targetId +',' + tipoLegame); 
				savedSourceId = sourceId;
			}
			
			rowCtr = 1;
			
			// Leggiamo i dati
			// ---------------
			while (true) {
//			while (rowCtr < 10) {
				try {
//					s = in.readLine();
					// Prendiamo l'offset
//					s = offsetsOrdinatoIn.readLine();
					s2 = rafOffsetIn.readLine();
					if (s2 == null)
						break;
					pos = Long.parseLong(s2.substring(keyLength));
					
					// Posizioniamoci tramite seek
					rafIn.seek(pos);
					// Leggiamo una riga
					s = rafIn.readLine();
					
					rowCtr++;
										
					if (s == null)
						break;
					
//					if (s.isEmpty())
//						continue;

//					ar = MiscString.estraiCampiConEscapePerSeparatore(s, stringSepDiCampoInArray, escapeCharacter);
					ar = MiscString.estraiCampi(s, stringSepDiCampoInArray, false); // 02/12/2009 15.38
					
					// Controlliamo se abbiamo dei record spezzati causa LF
					if (ar.length != campiInTabella)
					{
						// Bypassiamo bug in estrai campi 
						if (ar.length == (campiInTabella-1))
						{
							// se ultimo campo vuoto ok
							if (!ar[ar.length-2].endsWith(stringSepDiCampoInArray[0]))
								
							
						
						System.out.println("Record " + rowCtr + " spezzato: '" + s + "'\nKey+Offset: " + s2);

						while (true)
						{
							s2 = rafIn.readLine();
							if (s2 == null)
							{
								System.out.println("Record " + rowCtr +" non terminato" );
								return;
							}
							// Concateniamo gli array
							s = s+s2;
//System.out.println(s);							
							
							ar = MiscString.estraiCampiConEscapePerSeparatore(s, stringSepDiCampoInArray, escapeCharacter);
							if (ar.length == campiInTabella)
								break;
						}
						}	
					}

//					if (relazione == RELAZIONE_TITOLO_TITOLO_INDICE || relazione == RELAZIONE_TITOLO_TITOLO_INV_INDICE)
//					{
//						if (creaRelazioneTitTitInvIndice(ar))
//						{
//							if (!sourceId.equals(savedSourceId))
//							{
//								out.write("\n" + sourceId );
//								savedSourceId=sourceId;
//							}
//							out.write( "|" + targetId +','+ tipoLegame +','+ tipoLegameMusica +','+ cdNaturaBase +','+ cdNaturaColl +','+ sequenza);
//						}
//					}

//					else if (relazione == RELAZIONE_TITOLO_AUTORE_INDICE || relazione == RELAZIONE_TITOLO_AUTORE_INV_INDICE)
//					{
//						creaRelazioneTitAutIndice(ar);
//						if (!sourceId.equals(savedSourceId))
//						{
//							out.write("\n" + sourceId );
//							savedSourceId=sourceId;
//						}
//						out.write("|" + targetId +','+ tpResponsabilita +','+ cdRelazione +','+ flIncerto +','+ flSuperfluo ); // + ',' + cdStrumentoMusicale
//					}

//					else if (relazione == RELAZIONE_TITOLO_CLASSE_INDICE || relazione == RELAZIONE_TITOLO_CLASSE_INV_INDICE)
//					{
//						creaRelazioneTitClaIndice(ar);
//						if (!sourceId.equals(savedSourceId))
//						{
//							out.write("\n" + sourceId );
//							savedSourceId=sourceId;
//						}
//						out.write("|" + targetId); 
//					}
					
//					else if (relazione == RELAZIONE_TITOLO_MARCA_INDICE)
//					{
//						creaRelazioneTitMarIndice(ar);
//						if (!sourceId.equals(savedSourceId))
//						{
//							out.write("\n" + sourceId );
//							savedSourceId=sourceId;
//						}
//						out.write("|" + targetId); 
//					}
					
					

//					else if (relazione == RELAZIONE_TITOLO_SOGGETTO_INDICE || relazione == RELAZIONE_TITOLO_SOGGETTO_INV_INDICE)
//					{
//						creaRelazioneTitSogIndice(ar);
//						if (!sourceId.equals(savedSourceId))
//						{
//							out.write("\n" + sourceId );
//							savedSourceId=sourceId;
//						}
//						out.write("|" + targetId); 
//					}
					

//					else if (relazione == RELAZIONE_AUTORE_AUTORE_INDICE || relazione == RELAZIONE_AUTORE_AUTORE_INV_INDICE)
//					{
//						creaRelazioneAutAutIndice(ar);
//						if (!sourceId.equals(savedSourceId))
//						{
//							out.write("\n" + sourceId); //  +',' + tipoLegame
//							savedSourceId=sourceId;
//						}
//						out.write("|" + targetId +',' + tipoLegame); 
//					}

//					else if (relazione == RELAZIONE_TITOLO_LUOGO_INDICE || relazione == RELAZIONE_TITOLO_LUOGO_INV_INDICE)
//					{
//						creaRelazioneTitLuoIndice(ar);
//						if (!sourceId.equals(savedSourceId))
//						{
//							out.write("\n" + sourceId );
//							savedSourceId=sourceId;
//						}
//						out.write("|" + targetId +','+ tpLuogo);
//					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if ((rowCtr & 0x1FFF) == 0)
//				if ((rowCtr & 0xFF) == 0)
				{
					System.out.print("\nRiga " + rowCtr);
				}

// facciamone solo un po per prova 
//				if (rowCtr == 4096)
//					break;
				
				
			} // End while
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				rafIn.close();
				//offsetsOrdinatoIn.close();
				rafOffsetIn.close();
				
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("\nRighe fatte " + rowCtr);
*/
}// End run	file degli offset sortato	





} // End Create relation file



