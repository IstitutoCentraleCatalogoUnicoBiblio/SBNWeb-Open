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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Category;

/**
 * CLASSE PER LA GESTIONE DELLE DATE
 * <p/>
 * GESTISCE E VERIFICA UNA DATA GENERICA
 * FA PARTE DEL PACKAGE MODELLI
 * (TIPI SPECIALI DI VARIABILI DEL PROGETTO)
 * GESTISCE DI BASE IL FORMATO dd/MM/yyyy
 * CONVERTE IN TUTTI I VARI FORMATI (metodi specifici)
 * <p/>
 *
 * @author
 * @author
 */
public class BaseDate extends AnnoDate
{


	private static final long serialVersionUID = -1892079021485531792L;

	static Category log = Category.getInstance("iccu.box.BaseDate");

	protected String    original_date = new String("") ;
	protected boolean  ok_date   = false;
    protected Integer   anno      = null ;
    protected Integer   mese      = null ;
	protected Integer   giorno    = null ;
    protected Integer   ora       = null ;
	protected Integer   minuti    = null ;
    protected Integer   secondi   = null ;

    protected long     one_day_mills = 86400000 ;

    /**
	   * METODO DI CREATE INSTANCE
	   * <p>
	   * PRIMA VERSIONE - CREA (PER DEFAULT) LA SOLA ISTANZA
	   * </p>
	   * @param    NESSUNO
	   * @return  nuova istanza (per default)
	   */
    public BaseDate()
	  {
	  }

	/**
	 * METODO DI CREATE INSTANCE
	 * <p>
	 * SECONDA VERSIONE- CREA UNA ISTANZA DATA E SETTA GLI ATTRIBUTI
	 * </p>
	 * @param    input_date    data in input
	 * @return  nuova istanza per default
	 */
	public BaseDate(java.sql.Date input_date)
	{
	      setDate(input_date) ;
	}

    /**
	 * METODO DI CREATE INSTANCE
	 * <p>
	 * SECONDA VERSIONE- CREA UNA ISTANZA DATA E SETTA GLI ATTRIBUTI
	 * </p>
	 * @param    input_date    data in input
	 * @return  nuova istanza per default
	 */
	public BaseDate(String input_date)
	{
		if (input_date!=null)
			if (input_date.equals("today"))
				setTodayDate();
			else
				setDate(input_date);
	  }

    /**
     * METODO DI AGGIUNTA(SOTTRAZIONE) GIORNI DALLA DATA ODIERNA
     * PER ORA NON DISPONIBILE (TROPPO GENERICO)
     * <p>
     * se utilizzato con days = 0 equivale al metodo setTodayDate
     * sono stati tenuti entrambi per non creare ambiguità
     *</p>
     * @param  giorni da sommare o sottrarre alla data odierna
     * @return NESSUNO
     */
    private void addTodayDate(int days)
    {
        long days_mills = days * one_day_mills ;

        Date now = new Date(System.currentTimeMillis() + days_mills) ;
        SimpleDateFormat Format = new SimpleDateFormat("dd/MM/yyyy") ;
        original_date = Format.format(now) ;
        setDate(original_date) ;
    }


	/**
     * METODO DI AGGIUNTA(SOTTRAZIONE) GIORNI ALLA DATA CORRENTE
     * PER ORA NON DISPONIBILE (TROPPO GENERICO)
     * <p>
     * se utilizzato con days = 0 equivale a lasciare la data invariata
     * sono stati tenuti entrambi per non creare ambiguità
     *</p>
     * @param  giorni da sommare o sottrarre alla data ocorrente
     * @return NESSUNO
     */
    private void addDaysToDate(int days)
    {
        long days_mills = days * one_day_mills ;

  		set(getAnno().intValue() ,getMese().intValue()-1, getGiorno().intValue());
  		Date now = new Date(getTimeInMillis() + days_mills) ;

        SimpleDateFormat Format = new SimpleDateFormat("dd/MM/yyyy") ;

        original_date = Format.format(now) ;

        setDate(original_date) ;
    }

    /**
     * METODO DI CLEAR ATTRIBUTI DATA
     * Specializzato poi dalle sottoclassi
     * <p>
     * Effettua la pulizia campi dettaglio data
     * Utile ad esempio per un clear se data errata
     * </p>
     *
     * @param NESSUNO
     * @return  void    nessun ritorno
     */
    public void clearDate()
    {
	    original_date = new String("") ;
	    ok_date  = false;
	    anno    = null ;
	    mese    = null ;
        giorno  = null ;
        ora     = null ;
        minuti  = null ;
        secondi = null ;
    }

    /**
     * METODO DI COMPARE DI DUE DATE
     * <p>
     * Versione 1:Le due date sono di tipo BaseDate
     * Importante: non tratto la timezone e il dettaglio orario!
     * </p>
     * @param  second_date   data da confrontare con la corrente
     * @return result        risultato   (-1 or 0 or 1)
     *                                   -1 se istanza MINORE data input
     *                                    0 se istanze UGUALE data input
     *                                    1 se istanza MAGGIORE data input
     */
    public int compareTo(BaseDate input_date)
    {
        int result_difference = 0 ;

		if (original_date == null || original_date.compareTo("") == 0)
            return (-1) ;

        result_difference = anno.toString().compareTo(input_date.getAnno().toString()) ;

		    if (result_difference == 0)
			      result_difference = mese.toString().compareTo(input_date.getMese().toString()) ;

        if (result_difference == 0)
            result_difference = giorno.toString().compareTo(input_date.getGiorno().toString()) ;

        return (result_difference) ;
    }

	/**
	 * METODO DI COMPARE DI DUE DATE
	 * <p>
     * Versione 2:Le data in input è una stringa
     * (in formato standard MM/YYYY)
     *
     * Converto la stringa in data ed eseguo il metodo "gemello" di confronto
     * Importante: non tratto la timezone ed il dettaglio orario!
     * </p>
     * @param  second_date   stringa da confrontare con la corrente
     * @return result        risultato   (-1 or 0 or 1)
     *                                   -1 se istanza MINORE data input
     *                                    0 se istanze UGUALE data input
     *                                    1 se istanza MAGGIORE data input
     */
	protected int compareTo(String input_date)
	{
		BaseDate dummy_date = new BaseDate(input_date) ;

		return (compareTo(dummy_date)) ;
	}

    /**
     * METODO DI GETTING DATA:
     * <p>
     * TORNO LA DATA IN FORMATO STRINGA (UTILE PER COSTRUZIONE MESSAGGIO IN SEND)
     * </p>
     * @param    NESSUNO
     * @return  input_date    data in input
     */
    public String getDate()
    {
    	return(original_date) ;
	}

	// METODI DI GET ATTRIBUTI DI BASE
	public  Integer   getAnno()   	{ return (anno) ; }
    public  Integer   getMese()   	{ return (mese) ; }
    public  Integer   getGiorno() 	{ return (giorno) ; }
    public  Integer   getOra() 	{ return (ora) ; }
    public  Integer   getMinuti() 	{ return (minuti) ; }
	public  Integer   getSecondi() { return (secondi) ; }

    /**
     * METODO DI GET FLAG CONGRUENZA CONTENUTO DATA
     *
     */
    public  boolean   getOkDate()  { return (ok_date) ; }

	/**
	 * METODO DI SETTING DATA:
	 * <p>
	 * SPACCHETTO LA STRINGA E COPIO GLI ATTRIBUTI
	 * (formato data DD/MM/YYYY)
	 * </p>
	 * @param    input_date    data in input
	 * @return  NESSUNO
	 */
    public void setDate(String input_date)
    {
		Integer _giorno, _mese, _anno;

        if (verifyDateFormat(input_date));
		{
			_giorno  = new Integer(input_date.substring(0,2)) ;
        	_mese    = new Integer(input_date.substring(3,5)) ;
        	_anno    = new Integer(input_date.substring(6)) ;

			if (verifyDate(_giorno, _mese, _anno))
			{
                //Aggiunto da T
                giorno=_giorno;
                mese=_mese;
                anno = _anno;
				original_date = aggiungiZero(giorno) +
								aggiungiZero(mese) +
								anno.toString() ;
			}
		}
    }

   /**
     * "PICCOLO" METODO PER LA GESTIONE DEGLI ZERI
     * <p>
     * BANALE
     * </p>
     * @param  Integer di riferimento
     * @return Stringa di due caratteri
     */
    protected String aggiungiZero(Integer informazione)
    {
    	String risultato = informazione.toString();

    	if (informazione.intValue() < 10)
    		risultato = "0" + informazione.toString();

		return risultato ;
    }

    /**
     * METODO DI SETTING DATA:
     * <p>
     * SPACCHETTO LA STRINGA E COPIO GLI ATTRIBUTI
     * (formato data DD/MM/YYYY)
     * </p>
     * @param    input_date    data in input
     * @return  NESSUNO
     */
	public void setDate(java.sql.Date input_date)
 	{

		if (input_date==null) return ;

		setTime(input_date);

		//ok_date = verifyDate(input_date) ;

		//if (!ok_date) return ;

		giorno  = new Integer(get(DAY_OF_MONTH)) ;
		mese    = new Integer(get(MONTH) + 1 ) ;
 		anno    = new Integer(get(YEAR));

		original_date = aggiungiZero(giorno) +
						aggiungiZero(mese) +
						anno.toString() ;
	}

	/**
	 * get data pervenuta orginariamente
	 */
 	public String getOriginalDate()
 	{
 		return (original_date) ;
	}

    /**
     * METODO DI SETTINGS DATA ODIERNA ALL'INTERNO DELL'ISTANZA
     * PER ORA NON DISPONIBILE (TROPPO GENERICO)
     *
     * @param  input_date    data in input
     * @return NESSUNO
     */
    public void setTodayDate()
    {
        Date now = new Date(System.currentTimeMillis()) ;
        SimpleDateFormat Format = new SimpleDateFormat("dd/MM/yyyy") ;
		original_date = Format.format(now) ;
        setDate(original_date) ;
    }

    /**
     * METODO DI VERIFICA DATA
     * <p>
     * Verifica la data in base al formato standard dd/MM/YYYY
     * identico a quello ricevuto in input da campi data 3M (esclusa la timezone)
     * </p>
     * @param  input_date    data in input
     * @return result        risultato (true = data ok /false = data ko)
     */
    public boolean verifyDateFormat(String input_date)
    {
        if (input_date == null || input_date.length() != 10) return false;

		for (int indice = 0; indice < input_date.length(); indice++)
        {
                // Non controllo i separatori
                if (indice != 2 && indice != 5)
                	if (!Character.isDigit(input_date.charAt(indice)))
                		return false;
		}
		return true;
    }

  /**
     * METODO DI VERIFICA DATA
     * <p>
     * Verifica la data Lavorando solo sugli interi
     * </p>
     * @param  input_date    data in input
     * @return result        risultato (true = data ok /false = data ko)
     */
	public boolean verifyDate(Integer dummy_giorno, Integer dummy_mese, Integer dummy_anno)
    {
		Date dummy_date;
        int mesi_anno[] = new int[12];

        //Setto il calendario
        mesi_anno[0] = mesi_anno[2] = mesi_anno[4] = mesi_anno[6] = mesi_anno[7] = mesi_anno[9] = mesi_anno[11] = 31;
        mesi_anno[3] = mesi_anno[5] = mesi_anno[8] = mesi_anno[10] = 30;

        //Anche il bisestile
        if ( (dummy_anno.intValue()%4)!=0 || (!((dummy_anno.intValue()%4)!=0) &&
        	 (dummy_anno.intValue()%400)!=0 && !((dummy_anno.intValue()%100)!=0)))
        	mesi_anno[1]=28;
        else
        	mesi_anno[1]=29;

        // Verifico la data
        if ( ((dummy_anno.intValue() < 1900 || dummy_anno.intValue() > 3000) ||
             (dummy_mese.intValue() > 12 || dummy_mese.intValue() < 1) ||
             (dummy_giorno.intValue() > mesi_anno[dummy_mese.intValue()-1] ||
             dummy_giorno.intValue() < 1)) )
        	return false ;

        return true;
    }

    public String getXmlDate() {
        return ""+anno+"-"+(mese.intValue()>9?"":"0")+mese+"-"+(giorno.intValue()>9?"":"0")+giorno;
    }
}
