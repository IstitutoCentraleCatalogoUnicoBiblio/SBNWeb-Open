/**
 * Argentino Trombin - Almaviva
 12/11/2009 11.53 Aggiunta gestione scarico file accessori 

03/04/2010 Aggiunta gestione binaria dei file per separatore singolo che va in conflitto con unicode utf8 in quanto carattere non usato

10/05/2011 Aggiunta gestione parametrica da riga comando (sostituzione parametri)

**/



package it.finsiel.offlineExport;

import it.finsiel.misc.DateUtil;
import it.finsiel.misc.Misc;
import it.finsiel.misc.MiscString;
import it.finsiel.misc.MiscStringTokenizer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class DbDownload {
	public static final int MAX_BYTES_PER_UTF8_CHARACTER = 4;

	Map parameters = new HashMap();    // hash table

	
	
	java.util.Vector vecInputFiles; // Buffer Reader Files
	String downloadDir = "";
	int fileCounter = 0;
	String logFileOut = "";
	String fileOut = "";
	boolean setCurCfg=false;
	//FileOutputStream streamOutLog;
	BufferedWriter OutLog;
	
	//BufferedWriter OutTable;
//	FileOutputStream streamOutTable;
//	FileOutputStream streamOutTableBytes;
	BufferedOutputStream bufferedStreamOutTable;	// 15/09/2010
	BufferedOutputStream bufferedStreamOutTableBytes;

	
	Connection con = null;
	int commitEveryNRows = 10;
	//String dbName = "";
	String jdbcDriver="";
	String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	String userName="";
	String userPassword="";
	String	fieldSeparator="|";
	byte fieldSeparatorByte = (byte)0x7C; // 0xC0
	boolean useSingleSeparator = false;
	boolean rimuoviNewline = false;
	
	
	String preprocess;
	
	
	
	char charSepArrayComma[] = { ','};
	char charSepArraySpace[] = { ' '};
	static char escapeCharacter = '\\'; // il backslash di default

	
	int progress=0x3ff; 
//	int downloadMaxRecords=0; // all
	String query;
	
	public DbDownload()
	    {
	    }

	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public boolean openConnection()
	{
		System.out.println("Conncecting to " + connectionUrl + " userName=" + userName);
		
		try {
			 Class.forName ( jdbcDriver);
		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {

				con = DriverManager.getConnection(connectionUrl,userName, userPassword);
				boolean autoCommit = con.getAutoCommit();
				con.setAutoCommit(false);
				
				Statement stmt = null;
				try {
					stmt = con.createStatement();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// if (jdbcDriver.contains("postgres"))
				int pos = jdbcDriver.indexOf("postgres"); // 09/12/2010
				if (pos  != -1)
				{
					stmt.execute("SET search_path = sbnweb, pg_catalog, public");
					if (setCurCfg == true)
						stmt.execute("select set_curcfg('default')"); // Esegui questa select per attivare gli indici testuali 
				}
				
				return true;
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		return false;
	} // End openConnection
	

	public void closeConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // End closeConnection
	
	
	
	
	
	
	
public static void main(String args[])
{
	char charSepArrayEquals[] = { '='};
	char charSepArraySpace[] = { ' '};
	String ar[];

	if(args.length < 1)
    {
        System.out.println("Uso: DbDownload  <List filename>"); 
        System.exit(1);
    }
//    String configFile = args[0];
    String inputFile = args[0];
//	logFileOut = args[2];

    
    
    
    String start=
       "DbDownload tool - � Almaviva S.p.A 2008-2014"+
	 "\n============================================"+
	 "\nTool di export della base data per migrazione da applicativo Client/Server"+
	 "\nInizio esecuzione " + DateUtil.getDate() + " " + DateUtil.getTime()+ 
	 "\nElenco tabelle da trattare: " + inputFile ;

    System.out.println(start);

//	System.out.println("inp: " + inputFile);

	DbDownload dbDownload = new DbDownload();

    // abbiamo parametri in ingresso?
System.out.println("args: " + args.length);
	
    for (int i=1; i < args.length; i++)
    {
    	ar = dbDownload.getPropertyKeyValue(args[i]);  

    	// Add key/value pairs to the map
    	dbDownload.parameters.put(ar[0], ar[1]);
    	
    }
	
	
	BufferedReader in;
	String s;
	
	try {
		in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"UTF8"));

		// Leggiamo le configurazioni di base
		// ----------------------------------
		while (true) {
			try {
				s = in.readLine();
				if (s == null)
					break;
				else if (Misc.emptyString(s))
					continue;
				else if ((	s.length() == 0) 
							||  (s.charAt(0) == '#') 
							|| (Misc.emptyString(s) == true))
						continue;
				else {
					ar = dbDownload.getPropertyKeyValue(s); //MiscString.estraiCampi(s,  charSepArrayEquals, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // "="
					if (ar[0].startsWith("downloadDir"))
						dbDownload.downloadDir = ar[1];
					else if (ar[0].startsWith("jdbcDriver"))
						dbDownload.jdbcDriver = ar[1];
					else if (ar[0].startsWith("connectionUrl"))
						dbDownload.connectionUrl = ar[1];
					else if (ar[0].startsWith("userName"))
						dbDownload.userName = ar[1];
					else if (ar[0].startsWith("userPassword"))
						dbDownload.userPassword = ar[1];
					
					else if (ar[0].startsWith("useSingleSeparator"))
					{
						if (ar[1].charAt(0) == '1')
							dbDownload.useSingleSeparator = true;
						else
							dbDownload.useSingleSeparator = false;
					}
					
					else if (ar[0].startsWith("fieldSeparator"))
					{
						dbDownload.fieldSeparator = ar[1];
						if (dbDownload.useSingleSeparator == true)
						{
							byte [] bSep = hexStringToByteArray(ar[1]);
							dbDownload.fieldSeparatorByte = bSep[0];
						}
					}
					
					else if (ar[0].startsWith("rimuoviNewline"))
					{
						if ( ar[1].equals("true") || ar[1].equals("TRUE"))
						dbDownload.rimuoviNewline = true;
					}
						
					
					else if (ar[0].startsWith("logFileOut"))
						dbDownload.logFileOut = ar[1];
					
					else if (ar[0].startsWith("query"))
						dbDownload.query = ar[1];
					
					else if (ar[0].startsWith("endConfig"))
						break;

					else if (ar[0].startsWith("progress"))
						dbDownload.progress = Integer.parseInt(ar[1], 16);


					else
						System.out.println("ERRORE: parametro sconosciuto"	+ ar[0]);
					
						
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Apriamo il file di log
		try {

			System.out.println("File di log: "	+ dbDownload.logFileOut);
			dbDownload.OutLog = new BufferedWriter(new FileWriter(dbDownload.logFileOut));				
			dbDownload.OutLog.write(start);
			
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}
		
		// Apriamo il DB
		if (!dbDownload.openConnection())
			{
			try {
				dbDownload.OutLog.write("Failed to open DB of URL: " + dbDownload.connectionUrl);
				dbDownload.OutLog.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
			}
		
		// Esegui trattamento delle singole tabelle
			// ----------------------------------------
			dbDownload.fileCounter = 0;
			long startTime = System.currentTimeMillis();

			while (true) {
				try {
					s = in.readLine();
					if (s == null)
						break;
					else {
						if ((	s.length() == 0) 
								||  (s.charAt(0) == '#') 
								|| (Misc.emptyString(s) == true))
							continue;

						
						//ar = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); //  " "
						ar = MiscString.estraiCampiDelimitatiENon(s, " ", '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_TRUE, MiscString.KEEP_ESCAPE_TRUE);
						
						dbDownload.fileCounter++;
//						dbDownload.download(ar[0]); // Esporta tabella
						dbDownload.download(s); // Esporta tabella
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
			    long elapsedTimeMillis = System.currentTimeMillis()-startTime;
			    s = "\n----------------------------------\nExport eseguito in " + elapsedTimeMillis/(60*1000F) + " minuti";
	
				
				System.out.println(s);
				dbDownload.OutLog.write(s);
				
				System.out.println("Vedi " + dbDownload.logFileOut + " per i dettagli dell'elaborazione");
				// Close log file 
				if (dbDownload.OutLog != null)
					dbDownload.OutLog.close();
				// Close filelist input file	
				in.close();

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Chiudiamo il DB
			dbDownload.closeConnection();
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//    System.out.println("Righe elaborate: " + Integer.toString(rowCounter));

	System.exit(0);
} // End main



String[] getPropertyKeyValue(String s)
{
	int pos = s.indexOf("=");
	if (pos == -1)
	{
		String[] arrCampi = new String[1];
		arrCampi[0] = s;
		return arrCampi;
	}
		
	
	String key = new String (s.substring(0,pos));
	String value = new String (s.substring(pos+1));
	String[] arrCampi = new String[2];

	arrCampi[0] = key;
	arrCampi[1] = value;
	return arrCampi;

} // End getPropertyKeyValue




private byte[] myGetBytesUtf8(String s) {
	int len = s.length();
	int en = MAX_BYTES_PER_UTF8_CHARACTER * len;
	byte[] ba = new byte[en];
	if (len == 0)
		return ba;

	int ctr = 0;

	for (int i = 0; i < len; i++) {
		char c = s.charAt(i);
		if (c < 0x80) {
			ba[ctr++] = (byte) c;
		} else if (c < 0x800) {
			ba[ctr++] = (byte) (0xC0 | c >> 6);
			ba[ctr++] = (byte) (0x80 | c & 0x3F);
		} else if (c < 0x10000) {
			ba[ctr++] = (byte) (0xE0 | c >> 12);
			ba[ctr++] = (byte) (0x80 | c >> 6 & 0x3F);
			ba[ctr++] = (byte) (0x80 | c & 0x3F);
		} else if (c < 0x200000) {
			ba[ctr++] = (byte) (0xE0 | c >> 18);
			ba[ctr++] = (byte) (0x80 | c >> 12 & 0x3F);
			ba[ctr++] = (byte) (0x80 | c >> 6 & 0x3F);
			ba[ctr++] = (byte) (0x80 | c & 0x3F);
		} else if (c < 0x800) {

		}
	} // end for

	return trim(ba, ctr);
} // End myGetBytesUtf8

private static byte[] trim(byte[] ba, int len) {
	if (len == ba.length)
		return ba;
	byte[] tba = new byte[len];
	System.arraycopy(ba, 0, tba, 0, len);
	return tba;
}


//public void download(String tableName) 
public void download(String tableAndMaybeQuery) 
{
	String arTable[];
	String arStringLunghezzeCampi[] = null;
	int arIntLunghezzeCampi[] = null;
	char arLeftPad[] = null;
	char arRightPad[] = null;
	int idxNL; 
	
	boolean arBoolTrimCampi[] = null;
	
	//fieldSeparator = ""+0xC0;
	//byte[] byteAr = hexStringToByteArray("C0");
//	fieldSeparator = ""+0x00+0xC0;
//	
	System.out.println("Field separator ='"+fieldSeparator);
	
	
	arTable = MiscString.estraiCampiDelimitatiENon(tableAndMaybeQuery, " ", '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_TRUE, MiscString.KEEP_ESCAPE_TRUE);
	
	String tableName = arTable[0];
	
	int pos;
	Statement stmt = null;
	StringBuffer sb = new StringBuffer();
	int rows=0;
	long start=0;
	long elapsedTimeMillis=0;
	long milliSeconds;
	String s;
	String filename="";
	String currentQuery = query;
	String lunghezzeCampi = "";
	char charSepArrayComma[] = { ','};
	boolean chiudiConSeparatore = false;
	
	
//	downloadMaxRecords = 0; // all
	// Leggiamo i parametri specifici della tabella
	for (int i=0; i < arTable.length; i++)
	{
	
		if (arTable[i].startsWith("fileOut="))
			filename = downloadDir+arTable[i].substring(8);
	
		else if (arTable[i].startsWith("query="))
		{
			currentQuery = arTable[i].substring(6);
			if (currentQuery.startsWith("\"@query_"))
			{
				currentQuery = loadQueryFromFile(currentQuery);
			}
			else
				currentQuery = substituteParameters(currentQuery);
		}
		else if (arTable[i].startsWith("lunghezze="))
		{
			lunghezzeCampi = arTable[i].substring(10);
			arStringLunghezzeCampi = MiscString.estraiCampi(lunghezzeCampi,  charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			
			arIntLunghezzeCampi = new int [arStringLunghezzeCampi.length];
			
			for (int j=0; j < arStringLunghezzeCampi.length; j++)
				arIntLunghezzeCampi[j] = Integer.parseInt(arStringLunghezzeCampi[j]);
		}
		else if (arTable[i].startsWith("chiudiConSeparatore="))
		{
			String tmp = arTable[i].substring(20);
			if(tmp.equals("true"))
				chiudiConSeparatore = true;
			else
				chiudiConSeparatore = false;
				
		}
//		else if (arTable[i].startsWith("downloadMaxRecords="))
//			downloadMaxRecords = Integer.parseInt(arTable[i].substring(19), 10);
		else if (arTable[i].startsWith("trim="))
		{
			String trimCampi = arTable[i].substring(5);
			String arStringTrimCampi[] = MiscString.estraiCampi(trimCampi,  charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			
			arBoolTrimCampi = new boolean [arStringTrimCampi.length];
			
			for (int j=0; j < arBoolTrimCampi.length; j++)
				if (arStringTrimCampi[j].charAt(0) == '1')
					arBoolTrimCampi[j] = true;
		}
		else if (arTable[i].startsWith("leftPad="))
		{
			String padCampi = arTable[i].substring(8);
			String arStringPadCampi[] = MiscString.estraiCampi(padCampi,  charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			
			arLeftPad = new char [arStringPadCampi.length];
			
			for (int j=0; j < arLeftPad.length; j++)
			{
				if (arStringPadCampi[j].charAt(0) == '\\')
				{
					int x= Integer.parseInt(arStringPadCampi[j].substring(1),16);
					arLeftPad[j] = (char)x;
					
//					arLeftPad[j] = arStringPadCampi[j].charAt(1);
				}
				else
					arLeftPad[j] = arStringPadCampi[j].charAt(0);
			}
		}
		else if (arTable[i].startsWith("rightPad="))
		{
			String padCampi = arTable[i].substring(9);
			String arStringPadCampi[] = MiscString.estraiCampi(padCampi,  charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			
			arRightPad = new char [arStringPadCampi.length];
			
			for (int j=0; j < arRightPad.length; j++)
			{
				if (arStringPadCampi[j].charAt(0) == '\\')
				{
					int x= Integer.parseInt(arStringPadCampi[j].substring(1),16);
					arRightPad[j] = (char)x;
				}
				else
					arRightPad[j] = arStringPadCampi[j].charAt(0);
				
			}
		}
		else if (arTable[i].startsWith("output="))
		{
			filename = 	downloadDir+arTable[i].substring(7);
		}
		
		
	} // end for
	
	// Se non valorizzati mettiamo i default
	if (filename.length() == 0)
		filename = downloadDir+tableName+".out";
	
	
	
	
 	System.out.println("\nInizio export " + tableName + " su " + filename); //  + " " + DateUtil.getDate() + " " + DateUtil.getTime() 
	try {
		OutLog.write("\n\n-------------------------");
		OutLog.write("\nInizio export " + filename); //  + " " + DateUtil.getDate() + " " + DateUtil.getTime()
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	} 
	
		
	// Apriamo il file di export
	try {

//		System.out.println("File di log: "	+ fileOut);
//		OutTable = new BufferedWriter(new FileWriter(filename));		
		
//		streamOutTable = new FileOutputStream(filename, true); // append
		if (useSingleSeparator == true)
			bufferedStreamOutTableBytes = new BufferedOutputStream(new FileOutputStream(filename+".bytes", false)); // dont append
		else
			bufferedStreamOutTable = new BufferedOutputStream(new FileOutputStream(filename, false)); // dont append
		
	} catch (Exception fnfEx) {
		fnfEx.printStackTrace();
		return;
	}
	
	// Elabora
	try {
		stmt = con.createStatement();

		stmt.setFetchSize(5000);
		
		//String sql = "select * from " + tableName + " where fl_canc != 'S'";
		String ar[];
		String delimiters = " ";
//		ar = MiscString.estraiCampiAncheDelimitati(queryUtente, ' ', '"', '"');
		int queryOverrideIndex = arTable.length-1;
		
		if (arTable.length == 1) // No query override
			ar = MiscString.estraiCampiDelimitatiENon(currentQuery, delimiters, '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_FALSE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_FALSE, MiscString.KEEP_ESCAPE_FALSE);
		else
			// Query override
			ar = MiscString.estraiCampiDelimitatiENon(currentQuery, delimiters, '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_FALSE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_TRUE, MiscString.KEEP_ESCAPE_FALSE);
		
		String sql;
		if (ar.length > 2)
			sql = ar[0] +  tableName + ar[2];
		else if (ar.length > 1)
			sql = ar[0] +  tableName;
		else
			sql = ar[0];
		
		s = "Start executing query [" + sql +  "]"; //  + DateUtil.getDate() + " " + DateUtil.getTime()
		System.out.println(s);
		OutLog.write("\n"+s);

		
		
		start = System.currentTimeMillis();
		ResultSet rs = stmt.executeQuery(sql);
		
		
		
		
		
//		System.out.println("End executing query " + sql  + DateUtil.getDate() + " " + DateUtil.getTime()); 
	    elapsedTimeMillis = System.currentTimeMillis()-start;

	    s = "Esecuzione query in secondi " + elapsedTimeMillis/1000F;
	    System.out.println(s); 
		OutLog.write("\n"+s);

		s = "Start on working set " + DateUtil.getDate() + " " + DateUtil.getTime();
		
		System.out.println(s); 
		OutLog.write("\n"+s);

		start = System.currentTimeMillis();
		int columns = rs.getMetaData().getColumnCount();
		rows=0;
		int recordsWritten = 0;
		
		
		
byte[] byteBuffer = new byte[4000];		
int indexByteBuffer = 0;		
byte[] fieldByteBuffer = new byte[1000];		
		
		while (rs.next()) {
			sb.setLength(0);
			indexByteBuffer = 0;
			
			rows++;
			for (int i=1; i <= columns; i++)
			{
				String field = rs.getString(i);
				if (field != null) // 12/01/2010 15.37
				{
					
				if (arIntLunghezzeCampi != null )
				{
					// Tronchiamo o expandiamo il campo alla lunghezza indicata
					if (arIntLunghezzeCampi[i-1] != 0)
					{
						if (arIntLunghezzeCampi[i-1] < field.length())
							field = field.substring(0, arIntLunghezzeCampi[i-1]);
						// Expandiamo con padding
						else if (arIntLunghezzeCampi[i-1] > field.length())
						{
							// expand with specified padding?
							if (arLeftPad != null && arLeftPad[i-1] != 0)
								field = MiscString.paddingString(field, arIntLunghezzeCampi[i-1], arLeftPad[i-1], MiscString.PADDING_LEFT); 
							else if (arRightPad != null && arRightPad[i-1] != 0)
								field = MiscString.paddingString(field, arIntLunghezzeCampi[i-1], arRightPad[i-1], MiscString.PADDING_RIGHT); 
							else	
								field = MiscString.paddingString(field, arIntLunghezzeCampi[i-1], ' ', MiscString.PADDING_RIGHT);
						}
					}
				}
				if (arBoolTrimCampi != null && arBoolTrimCampi[i-1] == true)
					field = field.trim();
				
				if (i>1)
				{
					if (useSingleSeparator == true)
						byteBuffer[indexByteBuffer++] = fieldSeparatorByte;
					else
						sb.append(fieldSeparator);
					
				}
				if (useSingleSeparator == true)
				{
					fieldByteBuffer = myGetBytesUtf8(field); //field.getBytes(); 
					System.arraycopy(fieldByteBuffer, 0, byteBuffer, indexByteBuffer, fieldByteBuffer.length);
					indexByteBuffer += fieldByteBuffer.length;				
				}
				else
					sb.append(field);
					
				
				
				} // End if field != null

				else
				{
					if (i>1) // 15/01/2010 16.51
					{
//						byteBuffer[indexByteBuffer++] = (byte)0x26; // &
//						byteBuffer[indexByteBuffer++] = (byte)0x24; // $
//						byteBuffer[indexByteBuffer++] = (byte)0x25; // %
						if (useSingleSeparator == true)
							byteBuffer[indexByteBuffer++] = fieldSeparatorByte;
						else
							sb.append(fieldSeparator);
					}
				}
			}
			// Srivi record
			try {
				if (chiudiConSeparatore == true)
				{
//					byteBuffer[indexByteBuffer++] = (byte)0x26; // &
//					byteBuffer[indexByteBuffer++] = (byte)0x24; // $
//					byteBuffer[indexByteBuffer++] = (byte)0x25; // %
					if (useSingleSeparator == true)
						byteBuffer[indexByteBuffer++] = fieldSeparatorByte;
					else
						sb.append(fieldSeparator);
				}

				// Rimuoviamo i newLine che tando danno fastidio all'opac
				if (rimuoviNewline == true)
				{
					if (useSingleSeparator == true)
					{
					// Eliminiamo dal byteBuffer
					for (int i=0; i < indexByteBuffer; i++)
						if (byteBuffer[i] == 0x0d || byteBuffer[i] == 0x0a) // \r o \n
							byteBuffer[i] = 0x20; // spazio
					}
					else
					{
						while (true)
						{
							idxNL = sb.indexOf("\n");
							if (idxNL == -1)
								break;
							sb.replace(idxNL, idxNL+1, " "); // sostituiamo il NewLine con Spazio
							idxNL = sb.indexOf("\r");
							if (idxNL != -1)
								sb.replace(idxNL, idxNL+1, " "); // sostituiamo il Carriage return con Spazio
						}
						
					}
				}
				
				if (useSingleSeparator == true)
				{
					byteBuffer[indexByteBuffer++] = (byte)0x0a; // \n
					// Scrittura BYTES
					byte [] outByteBuffer = new byte[indexByteBuffer];
					System.arraycopy(byteBuffer, 0, outByteBuffer, 0, indexByteBuffer);
					bufferedStreamOutTableBytes.write(outByteBuffer);
				}
				else
				{
					sb.append('\n');
					// Scrittura UTF
					bufferedStreamOutTable.write(myGetBytesUtf8(sb.toString())); // utf8 translation
				}
				

				recordsWritten++;
				
//				if (downloadMaxRecords > 0 && recordsWritten == downloadMaxRecords)
//					break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ((rows & progress) == progress)
				System.out.println("Fatti: " + rows + " records");
		}
		stmt.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e2) {
		e2.printStackTrace();
	}
	try {
		
		s = "Fatti: " + rows + " records";
		System.out.println(s);
		OutLog.write("\n"+s);

		milliSeconds = System.currentTimeMillis();
	    elapsedTimeMillis = milliSeconds-start;
	    float secondi = elapsedTimeMillis/1000F;
	    float minuti = elapsedTimeMillis/(60*1000F);
	    if (minuti < 1)
	    	s = "Scarico tabella in " + secondi + " secondi";
	    else
	    	s = "Scarico tabella in " + minuti + " minuti";
	    
	    System.out.println(s);
		OutLog.write("\n"+s);
	
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	// Close file
	 if (fileOut != null)
		try {
			//OutTable.close();
			if (useSingleSeparator == true)
				bufferedStreamOutTableBytes.close();
			else
				bufferedStreamOutTable.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fine export "+ filename ); //  + DateUtil.getDate() + " " + DateUtil.getTime() 
		try {
			OutLog.write("\nFine export " + filename ); //  + DateUtil.getDate() + " " + DateUtil.getTime()
			
			OutLog.close();	// Chiudi log dopo aver caricato un file
			OutLog = new BufferedWriter(new FileWriter(logFileOut, true)); // Riapri in APPEND				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
} // End download


String substituteParameters(String currentQuery)
{
	// Cerchiamo nella stringa qualcosa che cominci con $
	int startPos=0;
	int foundPos1=-1, foundPos2=0;
	
	while (true )
	{
		foundPos1 = currentQuery.indexOf("$", startPos);
		if (foundPos1 == -1)
			break;
			
		// Troviamo la fine del parametro
		foundPos2 = currentQuery.indexOf("$", foundPos1+1);
		
		String parameter = 	currentQuery.substring(foundPos1+1, foundPos2);
		System.out.println("Parameter="+parameter);
		
		String value = (String)parameters.get(parameter);
		if (value == null)
			System.out.println("Parametro '"+parameter +"' non dichiarato da riga comando");
		else
		{
			System.out.println("Parametrized query before="+currentQuery);
			System.out.println("Value="+value);
			currentQuery = MiscString.replace(currentQuery, "$"+parameter+"$", value);
			startPos = foundPos1+1;
			System.out.println("Parametrized query after="+currentQuery);
		}
		
	}
	
	return currentQuery;
}

String loadQueryFromFile(String currentQuery)
{
	// TO DO
	String filename = currentQuery.substring(8, currentQuery.length()-1);
	
	BufferedReader in;
	String s;
	StringBuffer sb = new StringBuffer("\"");
	
	try {
		in = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF8"));
		while (true) {
			try {
				s = in.readLine();
				if (s == null)
					break;
				sb.append(" " + s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // end while		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	sb.append("\"");
	
	return sb.toString();

} // End loadQueryFromFile

} // End DbDownload
