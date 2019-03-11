/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.BaseDate;

import org.apache.log4j.Category;

/**
 * CLASSE PER LA GESTIONE DELLE DATE
 * <p/>
 * GESTISCE E VERIFICA UNA DATA
 * FA PARTE DEL PACKAGE MODELLI
 * (TIPI SPECIALI DI VARIABILI DEL PROGETTO)
 * GESTISCE DI BASE IL FORMATO yyyy
 * <p/>
 *
 * @author Akros Informatica s.rl.
 * @author Francesca Di Carlo   04/03/2002
 */
public class AnnoDateTitolo  //extends GregorianCalendar
{
	static  Category log = Category.getInstance("iccu.box.AnnoDateTitolo");

	private String anno;

    /**
	   * METODO DI CREATE INSTANCE
	   * <p>
	   * Crea un'istanza della data nel formato yyyy
	   * </p>
	   * @param   void
	   * @return  nuova istanza data
	   */
       public AnnoDateTitolo(){
	     //this.clear();
//    	   anno = "1000";
    	   anno = "0000"; // 29/09/14
	   }

	   /**
	   * METODO DI CREATE INSTANCE
	   * <p>
	   * SECONDA VERSIONE- CREA UNA ISTANZA DATA E SETTA GLI ATTRIBUTI
	   * </p>
	   * @param    input_date    data in input
	   * @return  nuova istanza per default
	   */
//	   public AnnoDate(int input_year)
//	   {
//	     setAnnoDate(input_year) ;
//	   }

	   /**
	   * METODO DI CREATE INSTANCE
	   * <p>
	   * SECONDA VERSIONE- CREA UNA ISTANZA DATA E SETTA GLI ATTRIBUTI
	   * </p>
	   * @param    input_date    data in input
	   * @return  nuova istanza per default
	   */
	   public AnnoDateTitolo(String input_year)
	   {
	     //setAnnoDate(input_year) ;
		   // TODO verificare validita' data in costruttore? // 28/04/2014
		   anno = input_year;
	   }

	   public boolean equals(AnnoDateTitolo input_year)
	   {
	 	   return anno.equals(input_year.getAnnoDate());
	   }


	   //METODO DI SET PER PROVA ANNO NULLO
//	   public void setAnnoDateNull(String input_year)
//       {
//		 this.clear();
//
//		 String appo_year = "0000";
//	     input_year = appo_year;
//		 int year = Integer.parseInt(appo_year);
//
//		 if (verifyYear(year) == true)
//		 {
//			 this.set(YEAR, year);
//		 }
//		 else
//		 {
//		 	// gestisci errore
//		 	log.info("Anno non corretto : "+input_year);
//		 }
//       }


	   /**
	   * METODO DI SETTING DATA:
	   * <p>
	   * Spacchetto la data nel formato yyyy
	   * </p>
	   * @param   input_date    data in input
	   * @return  void
	   */
//       public void setAnnoDate(int input_year)
//       {
//
//		 this.clear();
//
//		 if (verifyYear(input_year) == true)
//		 {
//		     this.set(YEAR, input_year);
//		 }
//		 else
//		 {
//		 	// gestisci errore
//		 	log.info("Anno non corretto : "+input_year);
//		 }
//       }

	   /**
	   * METODO DI SETTING DATA:
	   * <p>
	   * Spacchetto la data nel formato yyyy
	   * </p>
	   * @param   input_date    data in input
	   * @return  void
	   */
       public void setAnnoDate(String input_year)
       {
//		   if (input_year != null  && input_year.trim().length()>0)
//
//		   {
	    	   try {
				verifyYear(input_year, "campo?");	// 29/09/14
			   anno = input_year;
			} catch (EccezioneSbnDiagnostico e) {
				anno = "0000";
			}


//	   	       try
//			   {
//				   // converto in intero e passo al metodo idoneo
//		  		   int year = Integer.parseInt(input_year);
//		           setAnnoDate(year);
//			    }
//			    catch (NumberFormatException e)
//			    {
//				 	//log.error("Stringa non corrispondente ad un anno : "+input_year);
//	                // gestire errore
//                    setAnnoDate(0);
//		            //e.getMessage;
//			    }
//            }
//			else
//			{
//				anno = "0000";
//			}
       }


      /**
      * METODO DI GETTING DATA:
      * <p>
      * Torno la data da formato yyyy a FORMATO STRINGA
      * </p>
      * @param    NESSUNO
      * @return  input_date    data in input
      */
      public String getAnnoDate()
      {
//		String str_anno = new String("0000");
//
//		// converto l'anno in una stringa
//		String tmp_anno = String.valueOf(this.get(YEAR));
//
//    // controllo che la stringa sia composta da anno_length caratteri
//    // se è più corta aggiungo degli zeri in fondo
//    str_anno = tmp_anno + str_anno.substring(0, 4-tmp_anno.length());
//
//		// Tappo per evitare il ritorno del valore '0001' or '0000'
//		// Speriamo non determini strani errori
//		if ((str_anno.equals("1000")==true) || (str_anno.equals("0000")==true))
//    	  	str_anno = null;
//  		return str_anno;
  		if ((anno.equals("0000") == true)) // 29/09/14
			return null;

    	  return anno;
      }

//      public int getIntAnnoDate(){
//	  	return this.get(YEAR);
//	  }


      /**
      * METODO DI VERIFICA FORMATO DATA
      * <p>
      * Verifica la data in base al formato standard yyyy
      * </p>
      * @param  input_date    data in input
      * @return result        risultato (true = data ok /false = data ko)
      */
      public static void verifyDateFormat(String input_date, String fieldName) throws EccezioneSbnDiagnostico
      {
        if (input_date == null)
        	throw new EccezioneSbnDiagnostico(3354, fieldName + " non impostata");

        if (input_date.length() !=4)
        	throw new EccezioneSbnDiagnostico(3357, fieldName + " deve essere di 4 caratteri");

		for (int indice = 0; indice < input_date.length(); indice++)
        {
        	if (!Character.isDigit(input_date.charAt(indice)))
        	{
        		char chr = input_date.charAt(indice);
        		if (indice < 2)
        			throw new EccezioneSbnDiagnostico(3357, " I primi due caratteri di " + fieldName + " devono essere numerici");
//        		if (chr != '#')
           		if (chr != '.') // 28/04/2014
        			throw new EccezioneSbnDiagnostico(3357, " Gli ultimi due caratteri di " + fieldName + " devono essere numerici o '.'");
        	}
        	else if (indice == 0 && (input_date.charAt(indice) == '0')) // 28/04/2014 - almaviva7 anno >= 1000
        	{
    			throw new EccezioneSbnDiagnostico(3357, " Il primo carattere di " + fieldName + " non puo' essere minore di 1");
        	}
		}
      }

//	  public static boolean verifyYear(int dummy_anno)
//      {
//	  	boolean esito = true ;
//        // Verifico la data
//        //if (dummy_anno.intValue() < 1900 || dummy_anno.intValue() > 3000)
//
//       	if (dummy_anno < 0 || dummy_anno > 3000)
//        	esito = false ;
//        return esito;
//      }

/**
 * Controlla che anno sia un numero valido (inclusivo dei punti come wild character)
 * e che l'anno sia > di 1000 o <= all'anno corrente
 */
  public static void verifyYear(String anno, String fieldName) throws EccezioneSbnDiagnostico
      {
	  	verifyDateFormat(anno, fieldName);
//almaviva4 13/02/2014
	  	// Anno non puo' essere maggiore di anno corrente
        BaseDate today_date = new BaseDate("today");
        String anno_attuale = today_date.getAnno().toString();

        anno = 	anno.replace('.', '0'); // Replace . con 0

        if (anno.compareTo(anno_attuale) > 0)
			throw new EccezioneSbnDiagnostico(3357, fieldName + " non deve essere maggiore di anno corrente");

	  	// Anno non puo' essere minore di 1000 - almaviva7 28/04/2014 (richiesta almaviva)
//        if (anno.compareTo("1") < 0)
//			throw new EccezioneSbnDiagnostico(3357, fieldName + " non deve essere minore di 1000");

      }

  public static void verifyYearNumeric(String anno, String fieldName) throws EccezioneSbnDiagnostico
  {
      if (anno == null)
      	throw new EccezioneSbnDiagnostico(3354, fieldName + " non impostato");

      if (anno.length() !=4)
      	throw new EccezioneSbnDiagnostico(3357, fieldName + " deve essere di 4 caratteri");


	for (int indice = 0; indice < anno.length(); indice++)
    	if (!Character.isDigit(anno.charAt(indice)))
          	throw new EccezioneSbnDiagnostico(3357, fieldName + " deve essere numerico");

  }

  public static void verifyYearAntico(String anno, String fieldName) throws EccezioneSbnDiagnostico
  {
      if (anno == null)
      	throw new EccezioneSbnDiagnostico(3354, fieldName + " non impostato");

      if (anno.length() !=4)
      	throw new EccezioneSbnDiagnostico(3357, fieldName + " deve essere di 4 caratteri");

    anno = 	anno.replace('.', '0'); // Lower limit

	for (int indice = 0; indice < anno.length(); indice++)
    	if (!Character.isDigit(anno.charAt(indice)))
          	throw new EccezioneSbnDiagnostico(3357, fieldName + " deve essere numerico (ammessi solo 1 o due '.' finali)");

	if (anno.compareTo("1830") > 0)
		throw new EccezioneSbnDiagnostico(3357, fieldName + " > 1830");
  }

  public static boolean isYearAntico(String anno)
  {
      if (anno == null)
      	return false;

      if (anno.length() !=4)
      	return false;

    anno = 	anno.replace('.', '0'); // Lower limit

	for (int indice = 0; indice < anno.length(); indice++)
    	if (!Character.isDigit(anno.charAt(indice)))
          	return false;

	if (anno.compareTo("1830") > 0)
		return false;
	return true;
  }


 /**
  * Verifica che anno non sia null che sia di 4 caratteri (inclusi i punti (wild chars))
  * converte i punti in cifre, controlla che l'anno sia numerico e che l'anno sia > 1830
  *
  * @param anno
  * @param fieldName
  * @throws EccezioneSbnDiagnostico
  */
  public static void verifyYearModerno(String anno, String fieldName) throws EccezioneSbnDiagnostico
  {
      if (anno == null)
      	throw new EccezioneSbnDiagnostico(3354, fieldName + " non impostato");

      if (anno.length() !=4)
      	throw new EccezioneSbnDiagnostico(3357, fieldName + " deve essere di 4 caratteri");

      // Modifica 05/03/2015: riportata da software di Indice
  	if (anno.charAt(2) == '.' && anno.charAt(3) != '.')// Mail almaviva/Ferrari 10/12/14
		throw new EccezioneSbnDiagnostico(3357, " Il '.' in terza posizione puo essere' seguito solo da altro '.'");

    anno = 	anno.replace('.', '0'); // Lower limit

	for (int indice = 0; indice < anno.length(); indice++)
    	if (!Character.isDigit(anno.charAt(indice)))
          	throw new EccezioneSbnDiagnostico(3357, fieldName + " deve essere numerico (ammessi solo 1 o due '.' finali)");

	if (anno.compareTo("1831") < 0)
		throw new EccezioneSbnDiagnostico(3357, fieldName + " non puo' essere inferiore a 1831");

  } // End verifyYearModerno



 public static int compareToAnno1Anno2 (String anno1, String anno2) throws EccezioneSbnDiagnostico
  {
      if (anno1 == null || anno2 == null)
      	throw new EccezioneSbnDiagnostico(3354, " anno1 o anno2 non impostato");
    anno1 =	anno1.replace('.', '0'); // Lower limit
    anno2 =	anno2.replace('.', '0'); // Lower limit
	return (anno1.compareTo(anno2));
  } // End compareToAnno1Anno2

 public static int compareToAnni (String annoA, String annoB) throws EccezioneSbnDiagnostico
 {
     if (annoA == null || annoB == null)
     	throw new EccezioneSbnDiagnostico(3354, " anno1 o anno2 non impostato");
   annoA =	annoA.replace('.', '0'); // Lower limit
   annoB =	annoB.replace('.', '0'); // Lower limit
	return (annoA.compareTo(annoB));
 } // End compareToAnni

//almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015
 public static int compareToAnniUpperLimit (String annoA, String annoB) throws EccezioneSbnDiagnostico
 	{
	if (annoA == null || annoB == null)
	     	throw new EccezioneSbnDiagnostico(3354, " anno1 o anno2 non impostato");
	annoA =	annoA.replace('.', '9'); // Upper limit
	annoB =	annoB.replace('.', '9'); // Upper limit
	return (annoA.compareTo(annoB));
 	} // End compareToAnni



}

