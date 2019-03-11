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
package it.iccu.sbn.web.actions.acquisizioni.util;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Pulisci {

	public static final void PulisciVar(HttpServletRequest request) {

		HttpSession session = request.getSession();

		session.removeAttribute("attributeListaSuppBiblioVO");

		session.removeAttribute("bid");
		session.removeAttribute("desc");
		session.removeAttribute("natura");

		session.removeAttribute("attributeListaSuppOffertaFornitoreVO");
		session.removeAttribute("offerteSelected");
		session.removeAttribute("criteriRicercaOfferta");
		session.removeAttribute("ultimoBloccoOfferte");
		session.removeAttribute("listaOfferteEmessa");

		session.removeAttribute("attributeListaSuppSuggerimentoVO");
		session.removeAttribute("suggerimentiSelected");
		session.removeAttribute("criteriRicercaSuggerimento");
		session.removeAttribute("ultimoBloccoSugg");
		session.removeAttribute("listaSuggEmessa");

		session.removeAttribute("attributeListaSuppDocumentoVO");
		session.removeAttribute("documentiSelected");
		session.removeAttribute("criteriRicercaDocumento");
		session.removeAttribute("ultimoBloccoDoc");
		session.removeAttribute("listaDocEmessa");

		session.removeAttribute("attributeListaSuppGaraVO");
		session.removeAttribute("gareSelected");
		session.removeAttribute("criteriRicercaGara");
		session.removeAttribute("listaGareEmessa");
		session.removeAttribute("ultimoBloccoGare");

		session.removeAttribute("attributeListaSuppFornitoreVO");
		session.removeAttribute("fornitoriSelected");
		session.removeAttribute("criteriRicercaFornitore");
		session.removeAttribute("listaFornitoriEmessa");
		session.removeAttribute("ultimoBloccoFornitori");

		session.removeAttribute("attributeListaSuppBilancioVO");
		session.removeAttribute("bilanciSelected");
		session.removeAttribute("criteriRicercaBilancio");
		session.removeAttribute("listaBilanciEmessa");
		session.removeAttribute("ultimoBloccoBilanci");

		session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
		session.removeAttribute("sezioniSelected");
		session.removeAttribute("criteriRicercaSezione");
		session.removeAttribute("listaSezioniEmessa");
		session.removeAttribute("ultimoBloccoSezioni");

		session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
		session.removeAttribute("buoniSelected");
		session.removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE);
		session.removeAttribute("ultimoBloccoBO");
		session.removeAttribute("listaBOEmessa");

		session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
		session.removeAttribute("ordiniSelected");
		session.removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
		session.removeAttribute("listaOrdiniEmessa");
		session.removeAttribute("ultimoBloccoOrdini");
		session.removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);
		session.removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE_R);
		session.removeAttribute("elencoINVANDATA");

		session.removeAttribute("attributeListaSuppFatturaVO");
		session.removeAttribute("fattureSelected");
		session.removeAttribute("criteriRicercaFattura");
		session.removeAttribute("listaFattureEmessa");
		session.removeAttribute("ultimoBloccoFatture");
		session.removeAttribute("passaggioListaSuppFatturaVO");

		session.removeAttribute("attributeListaSuppProfiloVO");
		session.removeAttribute("profiliSelected");
		session.removeAttribute("criteriRicercaProfilo");
		session.removeAttribute("listaProfiliEmessa");
		session.removeAttribute("ultimoBloccoProfili");

		session.removeAttribute("attributeListaSuppComunicazioneVO");
		session.removeAttribute("comunicazioniSelected");
		session.removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_COMUNICAZIONE);
		session.removeAttribute("listaComEmessa");
		session.removeAttribute("ultimoBloccoCom");

		session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
		session.removeAttribute("cambiSelected");
		session.removeAttribute("criteriRicercaCambio");
		session.removeAttribute("listaCambiEmessa");
		session.removeAttribute("ultimoBloccoCambi");

		session.removeAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
	}

	public static Double ControllaImporto(String stringa) throws ParseException, ValidationException
	{

	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setGroupingSeparator('.');
	    dfs.setDecimalSeparator(',');
 	    dfs.setDigit('#');
	    dfs.setZeroDigit('0');

	    // controllo formattazione con virgola separatore dei decimali
	    try {
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 //DecimalFormat formato = new DecimalFormat();
	    	 //String pattern = "0.0#";
	    	 //formato.applyPattern(pattern);

	    	 df.setMaximumFractionDigits(2);
	    	 df.setMinimumFractionDigits(2);
   			 df.setGroupingSize(3);

		    Double importo = df.parse(stringa).doubleValue();
		    BigDecimal bd = new BigDecimal(importo);
   			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP) ;
   			Double importoNew=bd.doubleValue();

   			return importoNew;

		} catch (ParseException e) {
		    	e.printStackTrace();
		    	throw new ValidationException("importoErrato",
	        			ValidationExceptionCodici.importoErrato);
		}
	}



	public static String VisualizzaImporto(Double importo) throws Exception
	{

	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setGroupingSeparator('.');
	    dfs.setDecimalSeparator(',');

	    dfs.setDigit('#');
	    dfs.setZeroDigit('0');
	    //Locale locale=new Locale();
	   // controllo formattazione con virgola separatore dei decimali
	    try {
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(importo);

	    	return stringa;
		} catch (Exception e) {
		    	e.printStackTrace();
		    	throw new ValidationException("importoErrato",
	        			ValidationExceptionCodici.importoErrato);
		}
	}

	public static String VisualizzaImportoCambio(Double importo) throws Exception
	{

	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setGroupingSeparator('.');
	    dfs.setDecimalSeparator(',');
	    dfs.setDigit('#');
	    dfs.setZeroDigit('0');

	    // controllo formattazione con virgola separatore dei decimali
	    try {
	    	 //DecimalFormat df = new DecimalFormat("#,##0.00########", dfs);
	    	DecimalFormat df = new DecimalFormat("#,##0.00####", dfs);
	    	 // importo
	    	String stringa  = df.format(importo);

	    	return stringa;
		} catch (Exception e) {
		    	e.printStackTrace();
		    	throw new ValidationException("importoErrato",
	        			ValidationExceptionCodici.importoErrato);
		}
	}

	public static Double ControllaImportoCambio(String stringa) throws ParseException, ValidationException
	{

	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setGroupingSeparator('.');
	    dfs.setDecimalSeparator(',');
	    dfs.setDigit('#');
	    dfs.setZeroDigit('0');

	    // controllo formattazione con virgola separatore dei decimali
	    try {
	    	//DecimalFormat df = new DecimalFormat("#,##0.00########", dfs);
	    	DecimalFormat df = new DecimalFormat("#,##0.00#####", dfs);
	    	Double importo = df.parse(stringa).doubleValue();
		    BigDecimal bd = new BigDecimal(importo);
   			//bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP) ;
   			Double importoNew=bd.doubleValue();

		    // se è tutto a posto allora
		    // scambio

/*		    DecimalFormatSymbols dfs2 = new DecimalFormatSymbols();
		    dfs2.setDecimalSeparator('.');
	    	DecimalFormat df2 = new DecimalFormat("####.00", dfs);
		    String appo=df2.format(importStrIssimo);
		    Double appoissmo=df2.parse(appo).doubleValue();
*/		    return importoNew;
		} catch (ParseException e) {
		    	e.printStackTrace();
		    	throw new ValidationException("importoErrato",
	        			ValidationExceptionCodici.importoErrato);
		}
	}

}
