package it.finsiel.offlineExport;

import it.finsiel.misc.DateUtil;
import it.finsiel.misc.MiscString;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RearrangeFields {
	    


	BufferedReader in;
	BufferedWriter out;
	int rowCtr;
	static String stringSepDiCampoInArray[] = { "&$%"};  
	String[] ar;
	static char escapeCharacter = '\\'; // il backslash di default
  	

//	int relazione;
//	String savedSourceId="";
//	String sourceId, targetId, tipoLegame, tipoLegameMusica, cdNaturaBase, cdNaturaColl, sequenza, tpLuogo;
//	String tpResponsabilita, cdRelazione, flIncerto, flSuperfluo,  cdStrumentoMusicale;
//	String cdBiblioteca;
	
	
	public static void main(String args[])
	{
		if(args.length < 2)
	    {
	        System.out.println("Uso: RearrangeFields filenameIn filenameOut )");
	        System.exit(1);
	    }
	    String start="RearrangeFields tool - © Almaviva S.p.A 2008"+
		 "\n====================================="+
		 "\nfilenameIn " + args[0] + 
		 "\nfilenameOut " + args[1] ;
	    System.out.println(start);
	    
	    RearrangeFields rearrangeFields = new RearrangeFields ();  
	    
        System.out.println("\nInizio elaborazione " + DateUtil.getDate() + " " + DateUtil.getTime());
        rearrangeFields.run(args[0], args[1]);  
        System.out.println("\nFine elaborazione " + DateUtil.getDate() + " " + DateUtil.getTime());
	}
	
	    
void run(String inputFile, String outputFile) // , int relationFromStart, int relationFromEnd, int relationToStart, int relationToEnd
{
		String s;
		StringBuffer sb = new StringBuffer();
		
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
			
			// Leggiamo i dati
			// ---------------
			rowCtr=0;
			s="";
			while (true) { // true
				try {
					s = in.readLine();
					rowCtr++;
					
					if (s == null)
						break;

//System.out.print("\nRiga " + rowCtr + " " + s);

					// Read line
					ar = MiscString.estraiCampiConEscapePerSeparatore(s, stringSepDiCampoInArray, escapeCharacter);
					// Swap fields
					s = ar[30];		// swap
					ar[30] = ar[0];
					ar[0] = s;
					
					// write swapped line
					sb.setLength(0); // clear
					sb.append(ar[0]);
					for (int i=1; i < ar.length; i++)
						sb.append("&$%" + ar[i]);
					sb.append('\n');
					out.write(sb.toString());
					
					if ((rowCtr & 0x1FFF) == 0)
					{
						System.out.print("\nRiga " + rowCtr);
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

			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("\nRighe fatte " + rowCtr);

	}// End run		


}	






