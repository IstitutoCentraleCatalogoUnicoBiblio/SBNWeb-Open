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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;

/**
 * Classe ControlloImpronta.java
 * <p>
 * Verifica la correttezza formale dell'impronta legata
 * ad un documento antico o di tipo musica.
 * </p>
 *
 * @author
 * @author
 *
 * @version 28-feb-2003
 */

// 11.03.2015 almaviva2 - Adeguativa del controllo Impronta alla versione di Indice
// nella data presente nella impronta puo essere presente il carattere "." in quarta e terza/quarta posizione
// ammessa però sole se la cifra tra parentesi è uguale a valore Q (es. "r.us o-n- utro bide (3) 182. (Q)")
public class ImprontaValida extends Impronta {


    /**
	 * 
	 */
	private static final long serialVersionUID = 245224211939020721L;

	public ImprontaValida() {
        super();
    }

    static String validChars = ":-.,;\"()[]!?+'&*";

    protected EccezioneSbnDiagnostico preparaEccezione(int id,String nota,String messaggio) {
        EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3070, "Impronta formalmente errata");
        ecc.setMessaggio(messaggio);
        return ecc;
    }
    /**
     * Verifica la correttezza di un'impronta.
     * @throws EccezioneSbnDiagnostico se l'impronta è errata, con il relativo
     * codice di errore
     */
    public void verificaCorrettezza(C012 t012) throws EccezioneSbnDiagnostico {
        if (t012.getA_012_1() == null || t012.getA_012_2() == null || t012.getA_012_3() == null)
            throw preparaEccezione(3070, "Impronta formalmente errata", "L'impronta deve essere composta da tre parti");
        verificaPrimo(t012.getA_012_1());
        verificaSecondo(t012.getA_012_2());
        verificaTerzo(t012.getA_012_3());
    }

    protected boolean verificaPrimo(String primo) throws EccezioneSbnDiagnostico {
        //verificaLunghezza(primo,10,"La prima parte dell'impronta deve essere lunga 10 caratteri");
    	verificaLunghezza(primo,10,"La prima parte dell'impronta deve essere lunga 10 caratteri");
        verificaCaratteri(primo.substring(0, 4), "Caratteri non validi nel primo gruppo (4 caratteri)");
        assertEqual(primo.charAt(4), ' ', "Il primo e il secondo gruppo devono essere separati da spazio");
        verificaCaratteri(primo.substring(5, 9), "Caratteri non validi nel secondo gruppo");
        assertEqual(primo.charAt(9), ' ', "Il secondo e il terzo gruppo devono essere separati da spazio");
        return true;
    }

    protected boolean verificaSecondo(String secondo) throws EccezioneSbnDiagnostico {
        verificaLunghezza(secondo,14,"La seconda parte dell'impronta deve essere lunga 14 caratteri");
        verificaCaratteri(secondo.substring(0, 4), "Caratteri non validi nel terzo gruppo");
        assertEqual(secondo.charAt(4), ' ', "Il terzo e il quarto gruppo devono essere separati da spazio");
        verificaCaratteri(secondo.substring(5, 9), "Caratteri non validi nel quarto gruppo");
        assertEqual(secondo.charAt(9), ' ', "Dopo il quarto gruppo deve essere presente uno spazio");
        assertEqual(secondo.charAt(10), '(', "Si attende dopo il quarto gruppo un carattere (3, 7, C, S) racchiuso tra parentesi");
        assertIn(secondo.charAt(11), "37CS", "Si attende dopo il quarto gruppo un carattere (3, 7, C, S) racchiuso tra parentesi");
        assertEqual(secondo.charAt(12), ')', "Si attende dopo il quarto gruppo un carattere (3, 7, C, S) racchiuso tra parentesi");
        assertEqual(secondo.charAt(13), ' ', "Dopo l'indicazione di rilevazione deve essere presente uno spazio");

        return true;
    }

    // INIZIO EVOLUTIVA Gennaio 2016 - almaviva2 - Occorre adeguare SbnWeb al nuovo trattamento (già realizzato in Indice)
    // della data nell’impronta. In presenza di suffisso (Q) (= data incerta) dovrà essere possibile inserire uno o due punti
    // al posto dell’ultima o ultime due cifre della data. Non dovrebbe essere più possibile valorizzare la data con 0000
    // (e, se possibile, nemmeno con le prime due cifre iniziali diverse da 14, 15, 16, 17, 18).
    // si asterisca il vecchio trattamento e si sostituisce con quello di Indice

//    protected boolean verificaTerzo(String terzo) throws EccezioneSbnDiagnostico {
//        verificaLunghezza(terzo,8,"La terza parte dell'impronta deve essere lunga 8 caratteri");
//        //almaviva4 12/01/2015 la data deve accettare il '.' in terza e quarta posizione, richiesta Servello
////        verificaNumeri(terzo.substring(0, 4), "Riportare la data in cifre arabe");
////        verificaNumeriPunti(terzo.substring(0, 4));
//          assertEqual(terzo.charAt(4), ' ', "Manca uno spazio dopo la data");
//          assertEqual(terzo.charAt(5), '(', "Si attende un carattere (A,C,F,G,M,H,R,T,Y,X,Z,Q,E) racchiuso tra parentesi dopo la data");
//          assertIn(terzo.charAt(6), "ACFGMHRTYXZQE", "Si attende un carattere (A,C,F,G,M,H,R,T,Y,X,Z,Q,E) racchiuso tra parentesi dopo la data");
//          assertEqual(terzo.charAt(7), ')', "Si attende un carattere (A,C,F,G,M,H,R,T,Y,X,Z,Q,E) racchiuso tra parentesi dopo la data");
//
//          if (terzo.charAt(6) == 'Q')
//              verificaNumeriPunti(terzo.substring(0, 4));
//          else
//          	verificaNumeri(terzo.substring(0, 4), "Riportare la data in cifre arabe");
//
//          return true;
//      }

    protected boolean verificaTerzo(String terzo) throws EccezioneSbnDiagnostico {
        verificaLunghezza(terzo,8,"La terza parte dell'impronta deve essere lunga 8 caratteri");
        assertEqual(terzo.charAt(4), ' ', "Manca uno spazio dopo la data");
        assertEqual(terzo.charAt(5), '(', "Si attende un carattere (A,C,F,G,M,H,R,T,Y,X,Z,Q,E) racchiuso tra parentesi dopo la data");
        assertIn(terzo.charAt(6), "ACFGMHRTYXZQE", "Si attende un carattere (A,C,F,G,M,H,R,T,Y,X,Z,Q,E) racchiuso tra parentesi dopo la data");
        assertEqual(terzo.charAt(7), ')', "Si attende un carattere (A,C,F,G,M,H,R,T,Y,X,Z,Q,E) racchiuso tra parentesi dopo la data");

        if (terzo.charAt(6) == 'Q')
            verificaNumeriPunti(terzo.substring(0, 4));
        else
        	verificaNumeri(terzo.substring(0, 4), "Riportare la data in cifre arabe");

        return true;
    }
 // FINE EVOLUTIVA Gennaio 2016 - almaviva2

    protected void verificaLunghezza(String stringa ,int length, String messaggio_errore) throws EccezioneSbnDiagnostico {
    if (stringa.length()!= length)
        throw preparaEccezione(3070, "Impronta formalmente errata", messaggio_errore);
    }

    protected void assertEqual(char c1, char c2, String messaggio_errore) throws EccezioneSbnDiagnostico {
        if (c1 != c2)
        throw preparaEccezione(3070, "Impronta formalmente errata", messaggio_errore);
    }

    protected void assertIn(char c1, String inside, String messaggio_errore) throws EccezioneSbnDiagnostico {
        if (inside.indexOf(c1) < 0)
        throw preparaEccezione(3070, "Impronta formalmente errata", messaggio_errore);
    }

    /** Verifica che i caratteri di una stringa soddisfano i requisiti di correttezza
     * delle impronte*/
    protected void verificaCaratteri(String st, String messaggio_errore) throws EccezioneSbnDiagnostico {
        char c;
        for (int i = 0; i < st.length(); i++) {
            c = st.charAt(i);
            if (!((c >= '0' && c <= '9')
                || (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || (validChars.indexOf(c) >= 0)))
            throw preparaEccezione(3070, "Impronta formalmente errata", messaggio_errore);
        }
    }

    /** Verifica che i caratteri di una stringa compongano una data */
    protected void verificaNumeri(String st, String messaggio_errore) throws EccezioneSbnDiagnostico {
        char c;
        for (int i = 0; i < st.length(); i++) {
            c = st.charAt(i);
            if (! (c >= '0' && c <= '9'))
            throw preparaEccezione(3070, "Impronta formalmente errata", messaggio_errore);
        }
    }

    protected void verificaNumeriPunti(String st) throws EccezioneSbnDiagnostico {
        if (! (st.charAt(0) >= '0' && st.charAt(0) <= '9'))
        	throw new EccezioneSbnDiagnostico(3070, "Data impronta accetta solo numeri per i primi due caratteri");
        if (! (st.charAt(1) >= '0' && st.charAt(1) <= '9'))
        	throw new EccezioneSbnDiagnostico(3070, "Data impronta accetta solo numeri per i primi due caratteri");
        if (! (st.charAt(2) >= '0' && st.charAt(2) <= '9')) {
        	if (st.charAt(2) != '.')
        		throw new EccezioneSbnDiagnostico(3070, "Data impronta accetta solo numeri o punti per il terzo e quarto carattere");
        	if (st.charAt(3) != '.')
        		throw new EccezioneSbnDiagnostico(3070, "Data impronta accetta solo numeri o punti per il terzo e quarto carattere");
        }
        if (! (st.charAt(3) >= '0' && st.charAt(3) <= '9')) {
        	if (st.charAt(3) != '.')
        		throw new EccezioneSbnDiagnostico(3070, "Data impronta accetta solo numeri o punti per il terzo e quarto carattere");
        }
    }

    public static void main(String[] a) {
        C012 c012 = new C012();
        c012.setA_012_1("amos note ");
        c012.setA_012_2("s:ti diti (3) ");
        c012.setA_012_3("1712 (A)");
        ImprontaValida ci = new ImprontaValida();
        try {
            ci.verificaCorrettezza(c012);
            log.debug("verifica eseguita con successo ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
