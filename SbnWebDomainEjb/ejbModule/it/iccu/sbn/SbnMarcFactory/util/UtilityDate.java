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
package it.iccu.sbn.SbnMarcFactory.util;


import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Title: SBN Web
 * </p>
 *
 * <p>
 * Description: Interfaccia web per il sitema bibliotecario nazionale
 * </p>
 *
 * <p>
 * Classe contenente funzioni di utilità richiamate dal resto dell'applicazione
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company: Finsiel
 * </p>
 *
 * @version 1.0
 * @author
 */
public class UtilityDate {

//////////////////////////////////////////////////////////////////////////////////
/////////////////			FUNZIONI SULLE DATE			   		 /////////////////
//////////////////////////////////////////////////////////////////////////////////
  /**
	   * Controlla la differenza fra due anni
	   * nel formato yyyy
	   *
	   * @param strAnno1 yyyy
	   * @param strAnno2 yyyy
	   * @return 0 se gli anni sono uguali, un
	   * long > 0 se l' anno2 è maggiore dell'
	   * anno1, un long < 0 se l' anno2 è minore
	   * della anno1.
	   */
  public static long comparaAnni(String strAnno1, String strAnno2) {
	  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");

	  String strData1 = "01/01/" + strAnno1;
	  String strData2 = "01/01/" + strAnno2;
	  long longData1 = 0;
	  long longData2 = 0;

	  try {
		  Date data1 = simpleDateFormat.parse(strData1);
		  Date data2 = simpleDateFormat.parse(strData2);
		  longData1 = data1.getTime();
		  longData2 = data2.getTime();
	  } catch (ParseException e) {
	  }
	  return longData2 - longData1;
  }

  /**
   * Converte la data variazione in formato SBN al formato dell'utente.
   *
   * @param dataformatoSBN data variazione in formato SBN (es.20010418115015.0)
   *
   * @return data variazione formato utente (es: 2001-04-18)
   */
  public String converteDataVariazione(String dataformatoSBN) {
	  String data = null;

	  if (dataformatoSBN != null) {
		// Controlla che la stringa sia formata almeno da
		// 8 caratteri in quando questa è la somma dei caratteri
		// da estrarre: anno(4) + mese(2) + giorno(2)
		if (dataformatoSBN.length() >= 8){
			String year = dataformatoSBN.substring(0, 4);
			String month = dataformatoSBN.substring(4, 6);
			String day  = dataformatoSBN.substring(6, 8);
			data = day + "-" + month + "-" + year;
		}
	  }

	  return data;
  }



  /**
   * Converte la data da formato SBN (yyyy-mm-dd) al
   * formato utente (dd-mm-yyyy).
   *
   * @param dataFormatoUtente
   * @return data in formato Utente
   */
  public String converteDataSBN (String dataFormatoSBN) {
	String data = null;
	if (dataFormatoSBN != null) {
		if (dataFormatoSBN.length() > 8) {
			String day  = dataFormatoSBN.substring(6,8);
			String month = dataFormatoSBN.substring(4,6);
			String year = dataFormatoSBN.substring(0,4);
			// data = day + "-" + month + "-" + year;
			data = year + "-" + month + "-" + day;
		}
	}
	return data;
  }

  /**
   * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
   * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
   * o uguale al carattere punto '.';
   * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
   */
  //Evolutiva: Spostato da almaviva2 Marzo 2017 nell'oggetto DateUtil in modo che tale metodo sia visibile a tutto l'applicativo

  public String verificaFormatoDataParziale(String data, String fieldName) {
	  return DateUtil.verificaFormatoDataParziale(data, fieldName);
  }


  /**
   * Controlla che l'intervallo date sia corretto
   *
   * @param dataDa yyyy-mm-dd
   * @param dataA yyyy-mm-dd
   *
   * @return boolean
   */
  public String isOkControlloDate(String dataDa, String dataA) {
	  /* CONTROLLO SULLA DATA ULTIMO AGGIORNAMENTO
	  1)- DA MINORE O UGUALE A
	  2)- SE NON ESISTE A SARA' CONSIDERATO UGUALE A DA
	  3)- SE ESISTE A E NON ESISTE DA INVIARE UN MESSAGIO DI ERRORE
	   "Indicare il valore di DA"
	  */

	  if ((isDataVuota(dataA)) && (isDataVuota(dataDa))) {
		  return "";
	  } else {
		  if ((!isDataVuota(dataA)) && (isDataVuota(dataDa))) {
			  return "ric001";
		  } else {
			  // Inizio Modifica Gennaio 2015- Bug 5708 esercizio: I controlli sul formalismo della date (cioè la presenza dei punti es: 197.)
			  // vengono rimandati al protocollo nella TitoloValida così da concentrarli in un unico posto
//			  if ((isDataVuota(dataA)) && (!isDataVuota(dataDa))) {
//				  dataA = dataDa;
//			  }

//			  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
//
//			  try {
//				  java.util.Date dateDa = simpleDateFormat.parse(dataDa);
//				  java.util.Date dateA  = simpleDateFormat.parse(dataA);
//
//				  if (dateDa.compareTo(dateA) > 0) {
//					  return "ric002";
//				  } else {
//					  return "";
//				  }
//			  } catch (ParseException e) {
//				  return ("ERROR >>" + e.getMessage() + e.toString());
//			  }

			  return "";

			  // Fine Modifica Gennaio 2015
		  }
	  }
  }


  /**
   * Indica se la data è vuota, vuol dire
   * che l'utente non ha inserito una data
   *
   * @param data dd/mm/yyyy
   *
   * @return true se l'utente non ha inserito una data
   */
  public boolean isDataVuota(String data) {
//	  if (data.equals("")) {
//		  return true;
//	  } else {
//		  return false;
//	  }
	  //almaviva5_20100324
	  return ValidazioneDati.strIsNull(data);
  }


  /**
   * Converte la data da formato utente (dd-mm-yyyy) al
   * formato SBN (yyyy-mm-dd).
   *
   * @param dataFormatoUtente
   * @return data in formato SBN
   */
  public static String converteDataUtente (String dataFormatoUtente) {
	String data = null;
	if (dataFormatoUtente != null) {
		if (dataFormatoUtente.length() > 6) {
			String day  = dataFormatoUtente.substring(0,2);
			String month = dataFormatoUtente.substring(3,5);
			String year = dataFormatoUtente.substring(6);
			data = year + "/" + month + "/" + day;
		}
	}
	return data;
  }

  //almaviva5_20150316
  public static boolean isAntico(String data) {
	  if (!ValidazioneDati.isFilled(data))
		  return false;

	  //data numerica
	  if (data.matches("\\d{4}"))
		  if (Integer.valueOf(data) < 1831)
			  return true;

	  //un punto finale
	  if (data.matches("\\d{3}\\."))
		  if (Integer.valueOf(data.substring(0, 3)) <= 183)
			  return true;

	  //due punti
	  if (data.matches("\\d{2}\\.{2}"))
		  if (Integer.valueOf(data.substring(0, 2)) <= 18)
			  return true;

	  return false;
  }

  public static void main(String[] args) {
	  String[] test = {"1899", "1830", "197.", "20..", "173.", "18.." };
	  for (String t : test)
		  System.out.println(t + ": " + isAntico(t));
  }


}
