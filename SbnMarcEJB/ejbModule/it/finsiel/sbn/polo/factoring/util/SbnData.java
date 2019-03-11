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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.Category;

/**
 * CLASSE PER LA GESTIONE DELLE DATE
 * <p/>
 * Gestisce il tipo SbnData (omonimo)
 * Il tipo data ricevuto è particolarmente esteso:ccyyMMdd ovvero yyyyMMdd
 * Può ricevere l'input date nei seguenti formati
 * - String
 * - java.sql.Date
 * - long
 * L'informazioni viene spacchettata nei campi
 * anno,mese,giorno,
 * <p/>
 *
 * @author
 * @author
 */
public class SbnData extends BaseDate
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2404204768135525025L;
	static Category log = Category.getInstance("iccu.box.SnnData");

    /**
	 * METODO DI CREATE INSTANCE
     * <p>
	 * VUOTO - NON ABUSARNE -
	 * MEGLIO SETTARE L'ISTANZA A NULL CHE ISTANZIARE UNA DATA SENZA VALORI
     * </p>
     * @param    NESSUNO
     * @return  nuova istanza (per default)
     */
    public SbnData()
    {
    }

    /**
     * METODO DI CREATE INSTANCE
     * <p>
	 * SECONDA VERSIONE ACCETTA UN "supertipo" java.sql.Date
	 * Crea un'istanza della data nel formato yyyyMMdd
     * </p>
     * @param    input_date    data in input
     * @return  nuova istanza per default
     */
    public SbnData(java.util.Date input_date)
    {
		String data_formato_corretto = null ;

		// Converto nel formato opportuno
        SimpleDateFormat Format = new SimpleDateFormat("yyyyMMdd") ;
        data_formato_corretto = Format.format(input_date) ;

        // Spacchetta la data di tipo  yyyyMMdd
		setSbnData(data_formato_corretto) ;
    }

    /**
     * METODO DI CREATE INSTANCE
     * <p>
	 * TERZA VERSIONE ACCETTA UN tipo String (yyyyMMdd)
     * </p>
     * @param   void
     * @return  nuova istanza data
     */
    public SbnData(String input_date)
    {
        if (input_date != null)
			setSbnData(input_date);
    }

	/**
     * METODO DI CREATE INSTANCE
	 * <p>
	 * Crea un'istanza della data nel formato  yyyyMMdd
	 * </p>
	 * @param   void
	 * @return  nuova istanza data
	 */
	public SbnData(long input_date )
	{
		if (input_date !=0)
		{
   			// Mi appoggio ad un TimeStamp anche se non necessito un dettaglio
    		// così approfondito
			Timestamp appoggio_timestamp = new Timestamp(input_date);
			String data = appoggio_timestamp.toString();

			//Lancio la set sul tipo String
			setSbnData(data);
		}

	}

    /**
     * METODO DI SETTING DATA:
     * <p>
     * SPACCHETTO LA STRINGA E COPIO GLI ATTRIBUTI
     * (formato data yyyyMMdd)
     * </p>
     * @param    input_date    data in input
     * @return  NESSUNO
     */
    public void setSbnData(String input_date)
    {
		Integer  _giorno, _mese, _anno ;

        if (verifyDateFormat(input_date))
		{
			_anno       = new Integer(input_date.substring(0,4)) ;
        	_mese       = new Integer(input_date.substring(4,6)) ;
        	_giorno     = new Integer(input_date.substring(6,8)) ;

			if (verifyDate(	_giorno, _mese, _anno))
			{
				anno   	= _anno;
				mese   	= _mese;
				giorno 	= _giorno;

				// Ricostruisco la ORIGINAL DATE (compresi gli 0)
				// Solito Formato yyyyMMdd
				original_date = anno.toString() +
								aggiungiZero(mese) +
								aggiungiZero(giorno) ;
			}
			else
				ok_date = false ;

			ok_date = true ;
		}
		else
		{
			ok_date = false ;
		}
    }


    /**
     * METODO DI VERIFICA FORMATO DATA
     * <p>
     * Verifica la data in base al formato standard yyyyMMdd
     * Controlla SOLO che i caratteri  siano TUTTI NUMERI
     * </p>
     * @param  input_date    data in input
     * @return result        risultato (true = data ok /false = data ko)
     */
    public boolean verifyDateFormat(String input_date)
    {
        if (input_date == null) return false;

		// VERIFICA "FISSA" PER I SOLI PRIMI 15 CARATTERI
		// DOPO C'E' IL PUNTO!!
		for (int indice = 0; indice < input_date.length(); indice++)
        {
        	if (!Character.isDigit(input_date.charAt(indice)))
              return false;
		}
		return true;
    }

}
