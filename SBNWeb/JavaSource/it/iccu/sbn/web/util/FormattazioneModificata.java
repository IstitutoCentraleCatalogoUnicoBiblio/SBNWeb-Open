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
package it.iccu.sbn.web.util;



import it.iccu.sbn.web.actions.gestionebibliografica.isbd.akros.EccezioneSbnDiagnostico;
import it.iccu.sbn.web.actions.gestionebibliografica.isbd.akros.ElencoArticoli;

    /**
     * Classe Formattazione
     * <p>
     * Converte le stringhe per ripulirle dalla punteggiatura e renderle maiuscole, in
     * modo da poter essere utilizzate per ricerche o inserimenti nel DB.
     * </p>
     *
     * @author
     * @author
     *
     * @version 30-ott-02
     */
    public class FormattazioneModificata {
            static char[] separatoriC = { '\u0020', //spazio '-"+/:<=>\&@ il punto e la virgola ?
                '\'', '-', '\"', '+', '/', ':', '<', '=', '>', '\\', '&', '@' };
            static String separatori = new String(separatoriC);
//          MANTIS 2256
    		static String[] separatoriAreaTitolo = { " / ",  " : ", " ; "};

            static char spazioC = '\u0020';
            static String spazio = "" + spazioC;

        /**
         * Formatta una stringa sostituendo ai separatori degli spazi, eliminando i caratteri
         * non alfanumerici e quindi eliminando i doppi spazi. Restituisce la stringa modificata e
         * resa maiuscola.
         */
        public static String formatta(String stringa) throws EccezioneSbnDiagnostico{
            if (stringa==null) return null;
            char c;
            stringa = stringa.trim();
            //togli punteggiatura
            //Separatori sostituiti dallo spazio
            for (int i = 0; i < separatoriC.length; i++) {
                c = separatoriC[i];
                stringa = stringa.replace(c, spazioC);
            }
            //Il resto deve essere semplicemente tolto
            for (int i = 0; i < stringa.length(); i++) {
                c = stringa.charAt(i);
                if ((c != spazioC) && !Character.isLetter(c) && !Character.isDigit(c)) {
                    //stringa=stringa.replace(c,spazioC);
                    stringa = stringa.substring(0, i) + stringa.substring(i + 1);
                }
            }
            int n;
            //elimina doppi spazi(dovuti alla punteggiatura)
            while ((n = stringa.indexOf(spazio + spazio)) > 0)
                stringa = stringa.substring(0, n) + stringa.substring(n + 1);



            // almaviva2 10.02.2010
            // ATTENZIONE CLASSE NON RICHIAMATA TEMPORANEAMENTE
            // SUL CLIENT USIAMO A FINI DI TEST L'UPPER
            //rendi tutto maiuscolo
            return stringa.toUpperCase();
//            return UnicodeForOrdinamento2.convert(stringa);
        }

        /**
         * Rimuove tutti i caratteri non alfanumerici da una stringa (esclusi gli spazi);
         * @param stringa
         * @return
         */
        public static String rimuoviPunteggiatura(String stringa) {
            if (stringa == null)
                return null;
            char c;
            for (int i = 0; i < stringa.length(); i++) {
                c = stringa.charAt(i);
                if ((c != ' ') && !Character.isLetterOrDigit(c)) {
                    stringa = stringa.replace(c, ' ');
                }
            }
            //Rimuovo i doppi spazi.
            int n;
            while ((n = stringa.indexOf("  ")) >= 0)
                stringa = stringa.substring(0, n) + stringa.substring(n + 1);
            //Alla fine eseguo la trim
            return stringa.trim();
        }

    	/**
    	 * MANTIS 2256
    	 * metodo responsabile della formattazione nel caso della ricerca di un titolo
    	 * "perstringalike": toglie dalla stringa di ricerca, sulla quale vengono calcolate le chiavi,
    	 * tutti quelli che sono separatori catalografici (" : ", " / ", " ; ").
    	 * La logica di formattazione è identica a quella del metodo "formatta", precedentemente utilizzato.
    	 * (sostituisce ai separatori degli spazi, elimina i caratteri non alfanumerici ed infine,
    	 * elimina i doppi spazi. Restituisce la stringa modificata e resa maiuscola.
    	 */
    	public static String formattaPerAreaTitolo(String stringa) throws EccezioneSbnDiagnostico{
    		if (stringa==null) return null;
    		char c;
    		stringa = stringa.trim();
    		// MANTIS 2256
    		// Se occorre un separatore convenzionale, dalla stringa di ricerca
    		// viene tolto il separatore stesso e tutto ciò che segue
    		for (int i = 0; i < separatoriAreaTitolo.length; i++) {
    			String k = separatoriAreaTitolo[i];
    			int j = stringa.indexOf(k);
    			if (j > -1)
    				stringa = stringa.substring(0,j);
    		}
    		//togli punteggiatura
    		//Separatori sostituiti dallo spazio
    		for (int i = 0; i < separatoriC.length; i++) {
    			c = separatoriC[i];
    			stringa = stringa.replace(c, spazioC);
    		}
    		//Il resto deve essere semplicemente tolto
    		for (int i = 0; i < stringa.length(); i++) {
    			c = stringa.charAt(i);
    			if ((c != spazioC) && !Character.isLetter(c) && !Character.isDigit(c)) {
    				stringa = stringa.substring(0, i) + stringa.substring(i + 1);
    			}
    		}
    		int n;
    		//elimina doppi spazi(dovuti alla punteggiatura)
    		while ((n = stringa.indexOf(spazio + spazio)) > 0)
    			stringa = stringa.substring(0, n) + stringa.substring(n + 1);



            // almaviva2 10.02.2010
            // ATTENZIONE CLASSE NON RICHIAMATA TEMPORANEAMENTE
            // SUL CLIENT USIAMO A FINI DI TEST L'UPPER
            //rendi tutto maiuscolo
    		return stringa.toUpperCase();
//    		return UnicodeForOrdinamento.convert(stringa);
    	}


    	/**
    	 * almaviva2 21.03.2013
    	 * METODO COPIATO DA CercaFactoring del Protocollo SbnMArc - Restituisce la
    	 * stringa che segue l'asterisco e poi eliminazione caratteri speciali
    	 */
    	public static String dopoAsterisco(String stringa) {
    		if (stringa == null)
    			return null;
    		int n = stringa.indexOf('*');
    		if (n >= 0)
    			return stringa.substring(n + 1);
    		else {
    			n = stringa.indexOf(' ');
    			int n2 = stringa.indexOf('\'');
    			if (n < 0 || (n2 >= 0 && n > n2))
    				n = n2;

    			if (n >= 0)
    				if (ElencoArticoli.contiene(stringa.substring(0, n)))
    					return stringa.substring(n + 1);
    		}
    		return stringa;
    	}


    	public static String rimuoviCaratteriSpec(String stringa) {
    		if (stringa == null)
    			return null;
            stringa = stringa.replace("à", "a");
            stringa = stringa.replace("è", "e");
            stringa = stringa.replace("é", "e");
            stringa = stringa.replace("ì", "i");
            stringa = stringa.replace("ò", "o");
            stringa = stringa.replace("ù", "u");
    		return stringa;
    	}

    	public static String rimuoviPuntegg(String stringa) {
    		if (stringa == null)
    			return null;
    		stringa = stringa.replace("'", "");
            stringa = stringa.replace(",", "");
            stringa = stringa.replace("*", "");
            stringa = stringa.replace("?", "");
            stringa = stringa.replace("<", "");
            stringa = stringa.replace(">", "");
    		return stringa;
    	}


    }
