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
package it.finsiel.sbn.polo.factoring.util;

import java.util.GregorianCalendar;

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
 * @author
 * @author
 */
public class AnnoDate  extends GregorianCalendar
{

	private static final long serialVersionUID = 6554862012773136501L;

	static  Category log = Category.getInstance("iccu.box.AnnoDate");

	private int anno_length = 4;
    /**
	   * METODO DI CREATE INSTANCE
	   * <p>
	   * Crea un'istanza della data nel formato yyyy
	   * </p>
	   * @param   void
	   * @return  nuova istanza data
	   */
       public AnnoDate(){
	     this.clear();
	   }

	   /**
	   * METODO DI CREATE INSTANCE
	   * <p>
	   * SECONDA VERSIONE- CREA UNA ISTANZA DATA E SETTA GLI ATTRIBUTI
	   * </p>
	   * @param    input_date    data in input
	   * @return  nuova istanza per default
	   */
	   public AnnoDate(int input_year)
	   {
	     setAnnoDate(input_year) ;
	   }

	   /**
	   * METODO DI CREATE INSTANCE
	   * <p>
	   * SECONDA VERSIONE- CREA UNA ISTANZA DATA E SETTA GLI ATTRIBUTI
	   * </p>
	   * @param    input_date    data in input
	   * @return  nuova istanza per default
	   */
	   public AnnoDate(String input_year)
	   {
	     setAnnoDate(input_year) ;
	   }



	   //METODO DI SET PER PROVA ANNO NULLO
	   public void setAnnoDateNull(String input_year)
       {
		 this.clear();

		 String appo_year = "0000";
	     input_year = appo_year;
		 int year = Integer.parseInt(appo_year);

		 if (verifyYear(year) == true)
		 {
			 this.set(YEAR, year);
		 }
		 else
		 {
		 	// gestisci errore
		 	log.info("Anno non corretto : "+input_year);
		 }
       }


	   /**
	   * METODO DI SETTING DATA:
	   * <p>
	   * Spacchetto la data nel formato yyyy
	   * </p>
	   * @param   input_date    data in input
	   * @return  void
	   */
       public void setAnnoDate(int input_year)
       {

		 this.clear();

		 if (verifyYear(input_year) == true)
		 {
		     this.set(YEAR, input_year);
		 }
		 else
		 {
		 	// gestisci errore
		 	log.info("Anno non corretto : "+input_year);
		 }
       }

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
		   if (input_year != null  && input_year.trim().length()>0)
		   {
	   	       try
			   {
				   // converto in intero e passo al metodo idoneo
		  		   int year = Integer.parseInt(input_year);
		           setAnnoDate(year);
			    }
			    catch (NumberFormatException e)
			    {
				 	log.error("Stringa non corrispondente ad un anno : "+input_year);
	                // gestire errore
                    setAnnoDate(0);
		            //e.getMessage;
			    }
            }
			else
			{
				setAnnoDateNull(input_year);
			}


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
        String tmp_anno = new String();
		String str_anno = new String("0000");

		// converto l'anno in una stringa
		tmp_anno = String.valueOf(this.get(YEAR));

		if (tmp_anno == null)
		    str_anno = null ;
		else
		{
		    // controllo che la stringa sia composta da anno_length caratteri
		    // se è più corta antepongo degli zeri
		    str_anno = str_anno.substring(0, anno_length-tmp_anno.length())+tmp_anno;
		}

		// Tappo per evitare il ritorno del valore '0001' or '0000'
		// Speriamo non determini strani errori
		if ((str_anno.equals("0001")==true) || (str_anno.equals("0000")==true))
			str_anno = null ;

		return str_anno;

      }

      public int getIntAnnoDate(){
	  	return this.get(YEAR);
	  }


      /**
      * METODO DI VERIFICA FORMATO DATA
      * <p>
      * Verifica la data in base al formato standard yyyy
      * </p>
      * @param  input_date    data in input
      * @return result        risultato (true = data ok /false = data ko)
      */
      public boolean verifyDateFormat(String input_date)
      {
        if (input_date == null || input_date.length() !=4) return false;

		for (int indice = 0; indice < input_date.length(); indice++)
        {
        	if (!Character.isDigit(input_date.charAt(indice)))
              return false;
		}
		return true;
      }

	  public static boolean verifyYear(int dummy_anno)
      {
	  	boolean esito = true ;
        // Verifico la data
        //if (dummy_anno.intValue() < 1900 || dummy_anno.intValue() > 3000)

       	if (dummy_anno < 0 || dummy_anno > 3000)
        	esito = false ;
        return esito;
      }

}

