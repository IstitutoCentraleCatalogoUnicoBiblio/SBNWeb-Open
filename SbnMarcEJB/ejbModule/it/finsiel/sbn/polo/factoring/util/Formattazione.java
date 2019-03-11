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



import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.UnicodeForOrdinamento2;

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
    public class Formattazione {
    	// Mantis BUG 6633 esercizio- almaviva2 luglio 2018
    	// sia in fase di ricerca che di scrittura di titoli che prsentano il carattere "%" questo deve essere sostituito da " ";
    	// (commento originale // almaviva 04/07/2018 Aggiunto % nella conversione a spazio)
            static char[] separatoriC = { '\u0020', //spazio '-"+/:<=>\&@ il punto e la virgola ?
                '\'', '-', '\"', '+', '/', ':', '<', '=', '>', '\\', '&', '@', '%'};
            static String separatori = new String(separatoriC);
//          MANTIS 2256
    		static String[] separatoriAreaTitolo = { " / ",  " : ", " ; "};

            static char spazioC = '\u0020';
            static String spazio = "" + spazioC;

         // INIZIO Modifica settembre 2015 - GESTIONE CHIAVI CON CARATTERE "+" Si chiama solo il modulo UnicodeForOrdinamento2
        	/**Mantis 2204
        	 * Formatta la stringa per le chiavi ky_cles1_a, ky_cles2_a sostituendo ai separatori degli spazi, eliminando i caratteri
        	 * non alfanumerici e quindi eliminando i doppi spazi, mantenendo comunque il carattere +. Restituisce la stringa modificata e
        	 * resa maiuscola.
        	 */
        	public static String formattaAutore(String stringa) throws EccezioneSbnDiagnostico{
        		if (stringa==null) return null;
        		char c;
        		stringa = stringa.trim();
        		//togli punteggiatura
        		//Separatori sostituiti dallo spazio
        		for (int i = 0; i < separatoriC.length; i++) {
        			if (separatoriC[i]!= '+'  ){
        				c = separatoriC[i];
        				stringa = stringa.replace(c, spazioC);
        			}

        		}
        		//Il resto deve essere semplicemente tolto
        		for (int i = 0; i < stringa.length(); i++) {
        			c = stringa.charAt(i);
        			if ((c != spazioC) && (c != '+') && !Character.isLetter(c) && !Character.isDigit(c)) {
        				//stringa=stringa.replace(c,spazioC);
        				stringa = stringa.substring(0, i) + stringa.substring(i + 1);
        			}
        		}
        		int n;
        		//elimina doppi spazi(dovuti alla punteggiatura)
        		while ((n = stringa.indexOf(spazio + spazio)) > 0)
        			stringa = stringa.substring(0, n) + stringa.substring(n + 1);
        		//rendi tutto maiuscolo
        		//Mantis 2204 nel caso del calcolo delle chiavi lunghe per gli autori deve mantenere il +
        		return UnicodeForOrdinamento2.convertStringaAutore(stringa);
        	}




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
					stringa = stringa.substring(0, i) + stringa.substring(i + 1);
				}
            }

            int n;
            //elimina doppi spazi(dovuti alla punteggiatura)
            while ((n = stringa.indexOf(spazio + spazio)) > 0)
                stringa = stringa.substring(0, n) + stringa.substring(n + 1);
            //rendi tutto maiuscolo
            return UnicodeForOrdinamento2.convert(stringa);
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
		 * Verifica se la stringa è composta da soli caratteri ASCII base
		 * @param input
		 * @return
		 */
		public static boolean isASCII(String input) {
			boolean isASCII = true;
			int len = ValidazioneDati.length(input);
			for (int i = 0; i < len; i++) {
				int c = input.charAt(i);
				if (c > 0x7F) {
					isASCII = false;
					break;
				}
			}
			return isASCII;
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
    		//rendi tutto maiuscolo
    		// Modifica settembre 2015 - GESTIONE CHIAVI CON CARATTERE "+" Si chiama solo il modulo UnicodeForOrdinamento2
    		// return UnicodeForOrdinamento.convert(stringa);
    		return UnicodeForOrdinamento2.convert(stringa);
    	}

    }
