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

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ElencoStopList;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;

import java.util.StringTokenizer;

/**
 * Classe ChiaviTitolo.java
 * <p>
 * Estrae e gestisce le chiavi di ordinamento del titolo
 * cles1, cles2, clet1, clet2
 * </p>
 *
 * @author
 * @author
 *
 * @version 10-feb-2003
 */
public class ChiaviTitolo {
	// Mantis BUG 6633 esercizio- almaviva2 luglio 2018
	// sia in fase di ricerca che di scrittura di titoli che prsentano il carattere "%" questo deve essere sostituito da " ";
	// (commento originale // almaviva 04/07/2018 Aggiunto % nella conversione a spazio)

   // static final String separatori = "?-\"+/:<>=\\'!";
   // static final String separatori = "?-\"+/:<>=\\'!;#"; //18/03/2015 aggiunto '#'
	static final String separatori = "?-\"+/:<>=\\'!;#@%";

    static final int sepLength = separatori.length();
    String ky_cles1_t = " ";
    String ky_cles2_t = " ";
    String ky_clet1_t = " ";
    String ky_clet2_t = " ";
    String ky_cles1_ct = "      "; //6
    String ky_cles2_ct = "                                            "; //44
    String ky_clet1_ct = "   "; //3
    String ky_clet2_ct = "   "; //3

    /**
     * Da' avvio all'estrazione delle chiavi
     */
    public void estraiChiavi(C200 a200)  throws EccezioneSbnDiagnostico{
        if (a200 != null && a200.getA_200Count()>0)
            estraiChiavi(a200.getA_200(0));
    }

    /**
     * Legge le chiavi di un titolo
     */
    public void estraiChiavi(Tb_titolo titolo)  throws EccezioneSbnDiagnostico{
        ky_cles1_t = titolo.getKY_CLES1_T();
        ky_cles2_t = titolo.getKY_CLES2_T();
        ky_clet1_t = titolo.getKY_CLET1_T();
        ky_clet2_t = titolo.getKY_CLET2_T();
        ky_cles1_ct = titolo.getKY_CLES1_CT();
        ky_cles2_ct = titolo.getKY_CLES2_CT();
        ky_clet1_ct = titolo.getKY_CLET1_CT();
        ky_clet2_ct = titolo.getKY_CLET2_CT();
    }

    public void estraiChiavi(String a_200)  throws EccezioneSbnDiagnostico{
        estraiChiavi(a_200, false);
    }
    /**
     * Da' avvio all'estrazione delle chiavi
     */
    public void estraiChiavi(String a_200, boolean ignoraPuntaggiatura)  throws EccezioneSbnDiagnostico{
        Isbd isbd = new Isbd();
        String titolo = isbd.aggiungiAsterisco(a_200);
        titolo = titolo.substring(titolo.indexOf('*') + 1);
        // Inizio Modifica del 05.02.2013 su esempio del protocollo di Indice almaviva2
      //almaviva4 28/05/2010 aggiunta trim per eliminare spazi dopo * e prima del titolo
        titolo = titolo.trim();
        // Fine Modifica del 05.02.2013 su esempio del protocollo di Indice almaviva2
        String temp;
        int i = 0;
        if (ignoraPuntaggiatura) {
            i=titolo.length();
        } else {
            while (i < titolo.length()) {
                temp = titolo.substring(i);
                if (temp.startsWith(" / ")
                    || temp.startsWith(". -")
                    || temp.startsWith("! -")
                    || temp.startsWith("? -")
                    || temp.startsWith(" : ")
                    || temp.startsWith(" . ")
                    || temp.startsWith(".  ")
                    || temp.startsWith("?  ")
                    || temp.startsWith("!  ")
//      Modifica almaviva2 12.01.2012 - Intervento Interno Il calcolo delle chiavi viene adeguato al Prot. di indice
//                    || temp.startsWith(" ; ")
//      Modifica almaviva2 11.11.2009 - Bug Mantis 3265 - ripresa dalla correzione del Prot. di indice
//                    || temp.startsWith("=")
//      Modifica almaviva2 18.03.2010 - Bug Mantis 3265 - ripresa dalla correzione del Prot. di indice
//      Commento originale F.Ronchi - mantis 3278
//                    || temp.startsWith(" = ")
//      Modifica almaviva2 14.04.2010 - Bug Mantis 3679 - ripresa dalla correzione del Prot. di indice
//                    || temp.startsWith("  ")
                    ) {
                    break;
                }
                i++;
            }
        }
        // Inizio Modifica del 05.02.2013 su esempio del protocollo di Indice almaviva2
		//dario controllo tutti gli spazi in eccesso contenuti all'interno del titolo e li riduco a un solo spazio
		//IMPORTANTE aggiorno il valore della variabile con la nuova lunghezza del titolo (priva di spazi in eccesso)
        temp = titolo;
//        titolo = titolo.substring(0, i);
		titolo = replaceSpaces(titolo.substring(0,i));
        // Fine Modifica del 05.02.2013 su esempio del protocollo di Indice almaviva2
        titolo = trasformaCaratteri(titolo);
        //titolo = titolo.toUpperCase();
        titolo = UnicodeForOrdinamento2.convert(titolo);
        titolo = eliminaDoppiSpazi(titolo);
        if (titolo.length() > 6) {
            ky_cles1_t = titolo.substring(0, 6);
            if (titolo.length() > 50)
                ky_cles2_t = titolo.substring(6, 50);
            else
                ky_cles2_t = titolo.substring(6);

        } else
            ky_cles1_t = titolo;
        ky_clet1_t = estraiClet1(titolo);
        ky_clet2_t = estraiClet2(titolo);

        //Proseguo con le seconde
        if ((i = temp.indexOf("")) < 0)
            return;

        // Inizio modifica almaviva2 riportata dall'Indice in data 14.09.2012 a seguito di
        // mail  da giancarlo.salzano@regione.sicilia.it a Contardi in data 11 settembre 2012 10:07
        // riporto da mail:
        // rilevo che l'asterisco è stato messo sul terzo complemento del titolo.
        // La seconda chiave (calcolata in base al secondo asterisco) dovrebbe essere calcolata solo
        // sul primo complemento del titolo, non sui successivi.
        // Temo perciò che l'Indice sia nel giusto e che SbnWeb abbia un diverso modo di intercettare il secondo asterisco.

        int y = 0;
        y = temp.indexOf(" : ",i);
        if (y < 0)
			return;
		temp = temp.substring(y + 3);
		titolo = temp;
		int z =0;
		if (ignoraPuntaggiatura) {
			   z=titolo.length();
		} else {
			   while (z < titolo.length()) {
				   temp = titolo.substring(z);
				   if (temp.startsWith(" / ")
					   || temp.startsWith(". -")
					   || temp.startsWith("! -")
					   || temp.startsWith("? -")
					   || temp.startsWith(" : ")
					   || temp.startsWith(" . ")
					   || temp.startsWith(".  ")
					   || temp.startsWith("?  ")
					   || temp.startsWith("!  ")
					   || temp.startsWith(" ; ")){
					   break;
				   }
				   z++;
			   }
		}

		if (!ignoraPuntaggiatura) {


			temp = titolo;

			int x = 0;
			x = temp.indexOf("*", i);
	        if (x < 0)
	            return;
	        if (x > z)
	        	return;
	        titolo = temp.substring(x + 1);
	        i = 0;
			//MANTIS
	        if (ignoraPuntaggiatura) {
	            i=titolo.length();
	        } else {
	            while (i < titolo.length()) {
	                temp = titolo.substring(i);
	                if (temp.startsWith(" / ")
	                    || temp.startsWith(". -")
	                    || temp.startsWith("! -")
	                    || temp.startsWith("? -")
	                    || temp.startsWith(" : ")
	                    || temp.startsWith(" . ")
	                    || temp.startsWith(".  ")
	                    || temp.startsWith("?  ")
	                    || temp.startsWith("!  ")
	                    || temp.startsWith(" ; ")){
	                    break;
	                }
	                i++;
	            }
	        }
		}else{
			i = z;
		}

//
//        i = temp.indexOf("*", i);
//        if (i < 0)
//            return;
//        titolo = temp.substring(i + 1);
//        i = 0;
//        if (ignoraPuntaggiatura) {
//            i=titolo.length();
//        } else {
//            while (i < titolo.length()) {
//                temp = titolo.substring(i);
//                if (temp.startsWith(" / ")
//                    || temp.startsWith(". -")
//                    || temp.startsWith("! -")
//                    || temp.startsWith("? -")
//                    || temp.startsWith(" : ")
//                    || temp.startsWith(" . ")
//                    || temp.startsWith(".  ")
//                    || temp.startsWith("?  ")
//                    || temp.startsWith("!  ")
//                    || temp.startsWith(" ; ")
////      Modifica almaviva2 11.11.2009 - Bug Mantis 3265 - ripresa dalla correzione del Prot. di indice
////                    || temp.startsWith("=")
////      Modifica almaviva2 18.03.2010 - Bug Mantis 3265 - ripresa dalla correzione del Prot. di indice
////      Commento originale F.Ronchi - mantis 3278
////                    || temp.startsWith(" = ")
////      Modifica almaviva2 14.04.2010 - Bug Mantis 3679 - ripresa dalla correzione del Prot. di indice
////                    || temp.startsWith("  ")
//                    ) {
//                    break;
//                }
//                i++;
//            }
//        }


        // Fine modifica almaviva2 riportata dall'Indice in data 14.09.2012 a seguito di


        titolo = titolo.substring(0, i);
        titolo = trasformaCaratteri(titolo);
        titolo = eliminaDoppiSpazi(titolo);
        //		titolo = titolo.toUpperCase();
        titolo = UnicodeForOrdinamento2.convert(titolo);
        if (titolo.length() > 6) {
            ky_cles1_ct = titolo.substring(0, 6);
            if (titolo.length() > 50)
                ky_cles2_ct = titolo.substring(6, 50);
            else
                ky_cles2_ct = titolo.substring(6);

        } else
            ky_cles1_ct = titolo;
        ky_clet1_ct = estraiClet1(titolo);
        ky_clet2_ct = estraiClet2(titolo);

    }

    /**
     * Trasforma i caratteri di esclusione in spazi
     */
    protected String trasformaCaratteri(String stringa) {
        for (int i = 0; i < sepLength; i++)
            stringa = stringa.replace(separatori.charAt(i), ' ');
        return stringa;
    }

    /**
     * Rimuove i doppi spazi e li sostituisce con uno spazio singolo
     */
    public String eliminaDoppiSpazi(String stringa) {
        int n;
        while ((n = stringa.indexOf("  ")) >= 0)
            stringa = stringa.substring(0, n) + stringa.substring(n + 1);
        // Inizio Modifica del 05.02.2013 su esempio del protocollo di Indice almaviva2
        //almaviva4 09/03/2011 mantis 4296
        //      return stringa;
		return stringa.trim();
		//almaviva4 09/03/2011 fine
        // Fine Modifica del 05.02.2013 su esempio del protocollo di Indice almaviva2
    }

    /**
     * Estrae la chiave clet1
     */
    public String estraiClet1(String stringa) {
        int n = stringa.indexOf(" ");
        if (n < 0)
            n = stringa.length();
        if (n > 3)
            return stringa.substring(0, 3);
        stringa = stringa.substring(0, n);
        for (; n < 3; n++)
            stringa = stringa + " ";
        return stringa;
    }

    /**
     * Estrae la chiave clet2
     */
    public String estraiClet2(String stringa) {
        int n, c;
        String clet = "";
        for (c = 0; c < 3; c++) {
            n = stringa.indexOf(" ");
            if (n < 0)
                break;
            stringa = stringa.substring(n + 1);
            if (stringa.length() > 0)
                clet += stringa.charAt(0);
        }
        while (clet.length() < 3)
            clet = clet + " ";
        return clet;
    }

    /**
     * Returns the ky_cles1_ct.
     * @return String
     */
    public String getKy_cles1_ct() {
        return ky_cles1_ct;
    }

    /**
     * Returns the ky_cles1_t.
     * @return String
     */
    public String getKy_cles1_t() {
        return ky_cles1_t;
    }

    /**
     * Returns the ky_cles2_ct.
     * @return String
     */
    public String getKy_cles2_ct() {
        return ky_cles2_ct;
    }

    /**
     * Returns the ky_cles2_t.
     * @return String
     */
    public String getKy_cles2_t() {
        return ky_cles2_t;
    }

    /**
     * Returns the ky_clet1_ct.
     * @return String
     */
    public String getKy_clet1_ct() {
        return ky_clet1_ct;
    }

    /**
     * Returns the ky_clet1_t.
     * @return String
     */
    public String getKy_clet1_t() {
        return ky_clet1_t;
    }

    /**
     * Returns the ky_clet2_ct.
     * @return String
     */
    public String getKy_clet2_ct() {
        return ky_clet2_ct;
    }

    /**
     * Returns the ky_clet2_t.
     * @return String
     */
    public String getKy_clet2_t() {
        return ky_clet2_t;
    }

    /**
     * Estrazione chiave ky_editore
     * Si applica sull’xml in input dal client: DatiDocumento:T210
     * Obiettivo è estrarre dall’editore fino a 4 parole significative e registrarle in tb_titolo.ky_editore. Si applica ai documenti di natura M,W,C,S di materiale moderno.
     * Si costruisce una stringa accodando il contenuto di c_210 eliminando le eventuali stringhe ‘s.n.’ e le iniziali puntate (una lettera seguita da puntospazio)
     * Si maiuscolizza la stringa e si elimina la punteggiatura
     * Per ogni parola estratta: si cerca nella stop-list di tipo E  e di tipo D: se è presente la parola si scarta.
     * In creazione o modifica documento: si accodano le parole rimaste fino a un massimo di quattro separate da spazi e si registrano in ky_editore del titolo.
     * In ricerca o controllo dei simili si utilizza come filtro: se almeno una parola tra quelle estratte è contenuta in ky_editore del titolo letto nel db si considera positivo il filtro
     *
     * @param t210
     * @return
     */
    public String estraiChiaveEditore(C210 t210[],boolean togliS) throws EccezioneDB{
        String stringa = "";
        for (int i = 0; i < t210.length; i++)
            if (t210[i].getAc_210Count()>0) {
                for (int j = 0; j < t210[i].getAc_210Count(); j++) {
                        for (int n = 0; n < t210[i].getAc_210(j).getC_210Count(); n++)
                            stringa += " " + t210[i].getAc_210(j).getC_210(n);
                    }
            } else {
                for (int j = 0; j < t210[i].getG_210Count(); j++) {
                    stringa += " " + t210[i].getG_210(j);
                }
            }
        //a questo punto ho una stringa unica con tutti gli editori in sequenza
        String upper = stringa.toUpperCase();
        if (togliS) {
            if (upper.indexOf("S.N.") >= 0)
                stringa = "";
            if (upper.indexOf("S.E.") >= 0)
                stringa = "";
            if (upper.indexOf("S.T.") >= 0)
                stringa = "";
        }
        // MODIFICA MARCO RANIERI SOSTITUISCO VIRGOLE CON SPAZI.
        stringa = stringa.replace(',',' ');

        StringTokenizer st = new StringTokenizer(stringa, " ");
        String token;
        //Tolgo tutti i token di tipo x. (lettera punto spazio)
        int n;
        while (st.hasMoreTokens()) {
            token = st.nextToken();
            if (token.length() == 2 && token.charAt(1) == '.') {
                n = stringa.indexOf(token);
                stringa = stringa.substring(0, n) + stringa.substring(n + 2);
            }
        }
        stringa = stringa.toUpperCase();
        st = new StringTokenizer(stringa);
        stringa = null;
        n = 0;
        while (st.hasMoreTokens() && n < 4) {
            token = st.nextToken();
            if (!ElencoStopList.getInstance().contiene(token)) {
                if (n == 0)
                    stringa = token;
                else
                    stringa = stringa + " " + token;
                n++;
            }
        }
        return stringa;
    }

	public String replaceSpaces(String titolo)
	{
		int start=0;
		int end=0;
		for(int i=0; i<titolo.length(); i++){
			if(titolo.charAt(i) == ' ' && start == 0){
				start=i;
			}
			if(start != 0 && titolo.charAt(i) != ' ' && end == 0)
				 end=i;
		}
		if(start != 0 && end != 0){
			titolo = titolo.substring(0,start).concat(" "+titolo.substring(end,titolo.length()));
		}
		return titolo;
	}





    public static void main(String args[])  throws EccezioneSbnDiagnostico{
        String titolo = "Il *re principe : da *Roma con furore  ";
//        log.debug("Estrazione chiavi da titolo:" + titolo);
        ChiaviTitolo ct = new ChiaviTitolo();
        ct.estraiChiavi(titolo);
//        log.debug("ky_cles1_t:->" + ct.getKY_CLES1_T());
//        log.debug("ky_cles2_t:->" + ct.getKY_CLES2_T());
//        log.debug("ky_clet1_t:->" + ct.getKY_CLET1_T());
//        log.debug("ky_clet2_t:->" + ct.getKY_CLET2_T());
//        log.debug("ky_cles1_ct:->" + ct.getKy_cles1_ct());
//        log.debug("ky_cles2_ct:->" + ct.getKy_cles2_ct());
//        log.debug("ky_clet1_ct:->" + ct.getKy_clet1_ct());
//        log.debug("ky_clet2_ct:->" + ct.getKy_clet2_ct());
    }

 // Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
 // e opportunamente salvato sulla tb_titolo campo ky_editore
    public String estraiChiaveEditoreCollana(DatiDocType datiDoc, boolean togliS) throws EccezioneDB {
    	//public String estraiChiaveEditoreCollana(DatiDocType datiDoc,java.sql.Connection conn, boolean togliS) throws EccezioneDB {

        String stringa = "";
        if (datiDoc.getT210Count() > 0) {
        	for (int i = 0; i < datiDoc.getT210().length; i++)
            if (datiDoc.getT210(i).getAc_210Count()>0) {
            	for (int j = 0; j < datiDoc.getT210(i).getAc_210Count(); j++) {

            		if (datiDoc.getT210(i).getAc_210(j).getC_210Count() > 0) // Se abbiamo la $c (editore)
            		{
                		for (int n = 0; n < datiDoc.getT210(i).getAc_210(j).getC_210Count(); n++)
                            stringa += " " + datiDoc.getT210(i).getAc_210(j).getC_210(n);
            		}
            		else
            		{ // Non abbiamo l'editore in $c e lo cerchiamo in $a dopo i ':'
                		for (int n = 0; n < datiDoc.getT210(i).getAc_210(j).getA_210Count(); n++)
                		{
                			String s = datiDoc.getT210(i).getAc_210(j).getA_210(n);

                			int edStart = s.indexOf(":");
                			if (edStart > -1)
                			{
                				//int edEnd = s.indexOf("</");
                                stringa += " " + s.substring(edStart+1);
                			}
                		}
            		}
                    }


            } else {
                for (int j = 0; j < datiDoc.getT210(i).getG_210Count(); j++) {
                    stringa += " " + datiDoc.getT210(i).getG_210(j);
                }
            }
        } else if (datiDoc.getT200() != null) {
        //almaviva5_20160114 NON GESTITO DAL POLO IN QUESTA MODALITA'
        /*
            Isbd isbd = new Isbd();
            String campo = null;
            Integer start = datiDoc.getT200().toString().indexOf(". - ");
            Integer end = datiDoc.getT200().toString().indexOf("</a_200>");
            campo = datiDoc.getT200().toString();
            campo = campo.substring(start+4, end);
            C210[] c210 = isbd.costruisciC210(campo);
//            C210[] c210 = isbd.costruisciC210(datiDoc.getT200().toString());

            	for (int i = 0; i < c210.length; i++)
                if (c210[i].getAc_210Count()>0) {
                    for (int j = 0; j < c210[i].getAc_210Count(); j++) {
                            for (int n = 0; n < c210[i].getAc_210(j).getC_210Count(); n++)
                                stringa += " " + c210[i].getAc_210(j).getC_210(n);
                        }
                } else {
                    for (int j = 0; j < c210[i].getG_210Count(); j++) {
                        stringa += " " + c210[i].getG_210(j);
                    }
                }
         */
        }
        //a questo punto ho una stringa unica con tutti gli editori in sequenza
        String upper = stringa.toUpperCase();
        if (togliS) {
            if (upper.indexOf("S.N.") >= 0)
                stringa = "";
            if (upper.indexOf("S.E.") >= 0)
                stringa = "";
            if (upper.indexOf("S.T.") >= 0)
                stringa = "";
        }
        // MODIFICA MARCO RANIERI SOSTITUISCO VIRGOLE CON SPAZI.
        stringa = stringa.replace(',',' ');

        StringTokenizer st = new StringTokenizer(stringa, " ");
        String token;
        //Tolgo tutti i token di tipo x. (lettera punto spazio)
        int n;
        while (st.hasMoreTokens()) {
            token = st.nextToken();
            if (token.length() == 2 && token.charAt(1) == '.') {
                n = stringa.indexOf(token);
                stringa = stringa.substring(0, n) + stringa.substring(n + 2);
            }
        }

		stringa = stringa.replace('.',' ');

        stringa = stringa.toUpperCase();
        st = new StringTokenizer(stringa);
        stringa = null;
        n = 0;
        while (st.hasMoreTokens() && n < 4) {
            token = st.nextToken();
            if (! ElencoStopList.getInstance().contiene(token)) {
                if (n == 0)
                    stringa = token;
                else
                    stringa = stringa + " " + token;
                n++;
            }
        }
        return stringa;

    }
 // Fine Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore
}
