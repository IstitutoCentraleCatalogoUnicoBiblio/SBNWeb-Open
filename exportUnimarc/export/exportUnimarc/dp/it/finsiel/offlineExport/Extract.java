package it.finsiel.offlineExport;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

public class Extract {

	private void doAccess(String[] args) {
    	String s;
    	int bytesLength=0;
    	int lines=0;
    	char charSepArrayEquals[] = { '='};
    	char charSepArraySpace[] = { ' '};
    	String ar[];
    	byte[] bytes = null;
    	
        try {
        	if (args.length < 3)
        	{
        	System.out.println("ERROR, usage: Extract filename offset bytesLength=n | lines=n");
        	return;	
        	}

        	
        	ar = getPropertyKeyValue(args[2]);  
        	if (ar[0].equals("bytesLength"))
        	{
        		bytesLength = Integer.parseInt(ar[1]);
            	bytes = new byte[bytesLength]; 
        	}
        	else if (ar[0].equals("lines"))
        	{
        		lines =	Integer.parseInt(ar[1]);
        	}
        	else
        	{
          	  System.out.println(ar[0]+" Parametro non valido");
          	  return;
        	}
        		

        	
        	
            File file = new File(args[0]);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");

          raf.seek(Long.parseLong(args[1]));
//          for (int i=0; i < Integer.parseInt(args[2]); i++)
//        	  System.out.println("Read full line: " + raf.readLine());

          
          if (bytesLength > 0)
          {
        	  raf.read(bytes);
        	  System.out.write(bytes);
        	  
//	          String value = new String(bytes);
//	          System.out.println(value);
          }
          else
          {
            for (int i=0; i < lines; i++)
        	  System.out.println(raf.readLine());
          }
        	  
            
            raf.close();
          
        } catch (IOException e) {
            System.out.println("IOException:");
            e.printStackTrace();
        }
    }
    
private  String[] getPropertyKeyValue(String s)
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
 
public static void main(String[] args) {
	Extract extract = new Extract();
	extract.doAccess(args);
}

    
} // End Extract
