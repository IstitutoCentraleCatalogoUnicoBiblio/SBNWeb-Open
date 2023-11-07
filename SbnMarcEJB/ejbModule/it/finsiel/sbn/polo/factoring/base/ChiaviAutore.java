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

import it.finsiel.gateway.intf.KeyAutore;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ElencoArticoli;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.log4j.Category;
/**
 * Classe ChiaviAutore.java
 * <p>
 * Genera le chiavi di un autore
 * </p>
 *
 * @author
 * @author
 *
 * @version 27-ago-2003
 */
public class ChiaviAutore implements KeyAutore {

	private static final long serialVersionUID = 9210700938218520655L;

	private static String asterisco = "\u002a";
    private static char[] separatoriC = { '\u0020', //spazio '-"+/:<=>\&@ il punto e la virgola ?
        '-', '\"', '+', '/', ':', '<', '=', '>', '\\', '&', '@', '\'' };
    //tolgo l'apostrofo, trattato a parte
    private static String separatori = new String(separatoriC);
    static Category log = Category.getInstance("iccu.box.ChiaviAutore");
    String ky_auteur = null;
    String ky_cautun = null;
    String ky_el1 = null;
    String ky_el2 = null;
    String ky_el1a = null;
    String ky_el2a = null;
    String ky_el1b = null;
    String ky_el2b = null;
    String ky_el3 = null;
    String ky_el4 = null;
    String ky_el5 = null;
    String ky_cles1_A = null;
    String ky_cles2_A = null;
    String nome;
    String testo;
    String tipoAut;
    int ml;
    int ast;

    /**
     * Costruttore della classes
     * @param tipoAut tipologia di autore
     * @param nome nome dell'autore
     */
    public ChiaviAutore(String tipoAut, String nome) {
        this.nome = ValidazioneDati.trimOrEmpty(nome);
        this.tipoAut = ValidazioneDati.trimOrEmpty(tipoAut);
    }

//	mantis2204 devo togliere il carattere +
    private String EliminaCarattere(String nome){

		 int i = 0;
		 String nomebis = "";
		 if (nome==null || "".equalsIgnoreCase(nome)){
		 	return nome;
		 }
		 nomebis = nome;
		 i = nome.indexOf("+");
		 if (i > -1){
		  nomebis = nome.substring(0,i) + nome.substring(i+1);
		 }
		 int n= 0;
		return nomebis;
    }


    /**
     * Costruttore vuoto
     */
    public ChiaviAutore() {
    	super();
    }

    /** Rimuove l'apostrofo se è nella prima parola, altrimenti lo sostituisce con lo spazio */
    public static String rimuoviApostrofi(String stringa) {
        int n;
        String temp;
        int init = stringa.indexOf(' ');
        if (init >= 0)
            temp = stringa.substring(0, init);
        else
            temp = stringa;
        while ((n = temp.indexOf('\'')) >= 0)
            temp = temp.substring(0, n) + temp.substring(n + 1);
        if (init >= 0)
            temp = temp + stringa.substring(init).replace('\'', ' ');
        return temp;
    }

    /**
     * il controllo sull'asterisco dei tipi G deve essere:
     *  almeno un * prima di ' : ' e almeno un asterisco dopo ' : '
     *  max 4 * prima di ' : ' e max 2 asterischi dopo ' : '
     * @param nome
     * @throws EccezioneSbnDiagnostico
     */
    private void controlloAsterischiTipoG(String nome) throws EccezioneSbnDiagnostico {
        String nome1;
        String nome2 = null;
        int duepunti = nome.indexOf(" : ");
        if (duepunti >= 0) {
            nome1 = nome.substring(0, duepunti);
            nome2 = nome.substring(duepunti + 3);
        } else
            nome1 = nome;
        int counter = numeroAsterischi(nome1);
        if (counter == 0)
            throw new EccezioneSbnDiagnostico(3054, "Almeno un asterisco è obbligatorio");
        if (counter > 4)
            throw new EccezioneSbnDiagnostico(3055, "Al massimo 4 asterischi");
        if (nome2 != null) {
            String primoSottoente;
            String secondoS = null;
            duepunti = nome2.indexOf(" : ");
            if (duepunti >= 0) {
                primoSottoente = nome2.substring(0, duepunti);
                secondoS = nome2.substring(duepunti);
            } else
                primoSottoente = nome2;

            counter = numeroAsterischi(primoSottoente);
            if (counter == 0)
                throw new EccezioneSbnDiagnostico(3259, "Almeno un asterisco è obbligatorio");
            if (counter > 2)
                throw new EccezioneSbnDiagnostico(3260, "Al massimo 2 asterischi");

            if (secondoS!=null) {
                counter = numeroAsterischi(secondoS);
                if (counter > 0) {
                    throw new EccezioneSbnDiagnostico(3265, "Asterischi non ammessi nel secondo sottoente");
                }
            }
        }

    }

    private int numeroAsterischi(String st) {
        int tmp = -1;
        int counter = 0;
        while ((tmp = st.indexOf("*", tmp + 1)) >= 0)
            counter++;
        return counter;
    }

    /**
     * Elabora le chiavi di un autore. Che possono poi essere lette con getXXXX().
     * @return true se non insorgono problemi.
     * @throws EccezioneSbnDiagnostico
     */
    public boolean calcolaChiavi() throws EccezioneSbnDiagnostico {
        //String tipoAut = autore.getTP_NOME_AUT();

        //Rimuovo letteralmente gli apostrofi
        nome = rimuoviApostrofi(nome);
        ast = nome.indexOf("*");

        if (tipoAut.equals("G"))
            controlloAsterischiTipoG(nome);
        else if (tipoAut.equals("E") || tipoAut.equals("R")) {
            if (ast < 0)
                throw new EccezioneSbnDiagnostico(3054, "Almeno un asterisco è obbligatorio");
            int counter = numeroAsterischi(nome);
            if (counter > 4)
                throw new EccezioneSbnDiagnostico(3055, "Al massimo 4 asterischi");
        } else {
            if (ast>=0) {
                if (numeroAsterischi(nome)>1)
                    throw new EccezioneSbnDiagnostico(3065, "Al massimo un asterisco");
                int fine1 = nome.indexOf(" : ");
                int fine2 = nome.indexOf("<");
                int fine3 = nome.indexOf(", ");
                if (fine1<0 || (fine2>=0 && fine2<fine1))
                    fine1 = fine2;
                if (fine1<0 || (fine3>=0 && fine3<fine1))
                    fine1 = fine3;
                if (fine1>=0) {
                    if (numeroAsterischi(nome.substring(fine1))>0)
                        throw new EccezioneSbnDiagnostico(3065, "Al massimo un asterisco, nella prima parte del nome");
                }
            }
        }
        int n;
        testo = nome.substring(ast + 1);
        testo = UnicodeForOrdinamento2.convertAutore(testo);
        //ml = testo.length();
        int virgola = testo.indexOf(", ");
        if (tipoAut.equals("C") || tipoAut.equals("D")) {
            if (virgola < 0) {
                virgola = testo.indexOf(",");
                //if (virgola != ml)
                //Aggiungo la virgola
                if (virgola < 0)
                    testo += ",";
            }
        } else if (tipoAut.equals("A") || tipoAut.equals("B")) {
            // Inizio modifica almaviva2 04.03.2010 - BUG MANTIS 3582 - il controllo va fatto nell'area del note
            // escludendo l'area delle qualificazione.
//          if (virgola > testo.indexOf(" ") && testo.indexOf(" ") > 0) {
//    	     	return false;
//          }
            // Correzione errore introdotto Mantis 3635 almaviva2 18.03.2010 - il nuovo calcolo posizionale della virgola
        	// deve essere fatto solo se ci sono le uncinate sennò vale il vecchiop calcolo

        	if (testo.indexOf(" <") < 0) {
              if (virgola > testo.indexOf(" ") && testo.indexOf(" ") > 0) {
            	return false;
              }
        	} else {
                int virgolaCorretta = testo.substring(0, testo.indexOf(" <")).indexOf(",");
                if (virgolaCorretta > testo.indexOf(" ") && testo.indexOf(" ") > 0) {
                	return false;
                }
        	}
        }
        // Fine modifica almaviva2 04.03.2010 - BUG MANTIS 3582

        virgola = 0;
        //elimina doppi spazi(dovuti alla punteggiatura)
        while ((n = testo.indexOf("  " /*spazio + spazio*/
            )) >= 0)
            testo = testo.substring(0, n) + testo.substring(n + 1);
        ml = nome.length();
        virgola = testo.length();
        int cont = 0;
        String esteso = "";
        //almaviva5_20120418 segnalazione ICCU: riportata correzione indice calcolo cautun tipo nome B/D
        int lunPrima;
        if (tipoAut.equals("B") || tipoAut.equals("D")) {
          lunPrima = testo.indexOf(' ');
          int j = testo.indexOf('-');
          if (lunPrima < 0 || j < 0) {
            lunPrima = Math.max(lunPrima, j);
          } else {
            lunPrima = Math.min(lunPrima, j);
          }
        } else {
          lunPrima = testo.indexOf(" ");
        }
        //
        if (lunPrima <= 0)
            lunPrima = virgola;
        //elimino dalla prima parola i caratteri _ e '
        //compattando l'intestazione autore
        char my;
        //int mp = 0;
        while (cont < lunPrima) {
            my = testo.charAt(cont);
            if (my != '_' && my != '\'') {
                if (tipoAut.equals("A") || tipoAut.equals("C")) {
                    if (my != '-') {
                        esteso = esteso + my;
                    } else {
                        //mp++;
                    }
                } else {
                    esteso = esteso + my;
                }
            } else {
                //mp++;
            }
            cont++;
        }
        ky_auteur = calcolaAuteur(esteso);

        // INIZIO Modifica settembre 2015 - GESTIONE CHIAVI CON CARATTERE "+" Si chiama solo il modulo UnicodeForOrdinamento2
        //mantis 2204
        ky_auteur = EliminaCarattere(ky_auteur);

        if (tipoAut.equals("A"))
            calcolaChiaviA();
        if (tipoAut.equals("B"))
            calcolaChiaviB();
        if (tipoAut.equals("C"))
            calcolaChiaviC();
        if (tipoAut.equals("D"))
            calcolaChiaviD();
        if (tipoAut.equals("E") || tipoAut.equals("R"))
            calcolaChiaviER();
        if (tipoAut.equals("G"))
            calcolaChiaviG();
        // INIZIO Modifica settembre 2015 - GESTIONE CHIAVI CON CARATTERE "+" Si chiama solo il modulo UnicodeForOrdinamento2
        ky_el1 = trasf_spazi(EliminaCarattere(ky_el1));
        ky_el2 = trasf_spazi(EliminaCarattere(ky_el2));
        trattaEl3();
        ky_el4 = trasf_spazi(EliminaCarattere(ky_el4));
        ky_el5 = trasf_spazi(EliminaCarattere(ky_el5));
        //ky_cautun = calcolaCautun(cont + mp, ky_auteur, tipoAut);
        // Bug Mantis 5398 (esercizio): il calcolo della ky_cautun deve essere fatto utilizzando il camnpo testo che corrisponde al
        // nome dopo essere passato dal trattamento UnicodeForOrdinamento2.convertAutore che converte i caratteri speciali
        // nei corrispondenti caratteri utilizzabili dal calcolo della chiave.
//        Prove di calcolo:
//        String old_ky_cautun = calcolaCautun(cont, ky_auteur, tipoAut);
//        String new_ky_cautun = calcolaCautun(cont, ky_auteur, tipoAut, testo);
        ky_cautun = calcolaCautun(cont, ky_auteur, tipoAut, testo);
        //Mantis 2204
		ky_cautun = EliminaCarattere(ky_cautun);

        //Divido ky_el1 nelle due sottoparti
        if (ky_el1 != null)
            if (ky_el1.length() > 3) {
                ky_el1a = ky_el1.substring(0, 3);
                if (ky_el1.length() > 6)
                    ky_el1b = ky_el1.substring(3, 6);
                else
                    ky_el1b = ky_el1.substring(3);
            } else
                ky_el1a = ky_el1;
        //Divido ky_el2 nelle due sottoparti
        if (ky_el2 != null)
            if (ky_el2.length() > 3) {
                ky_el2a = ky_el2.substring(0, 3);
                if (ky_el2.length() > 6)
                    ky_el2b = ky_el2.substring(3, 6);
                else
                    ky_el2b = ky_el2.substring(3);
            } else
                ky_el2a = ky_el2;
        if (ky_el4 != null)
            if (ky_el4.length() > 6)
                ky_el4 = ky_el4.substring(0, 6);
        if (ky_el5 != null)
            if (ky_el5.length() > 6)
                ky_el5 = ky_el5.substring(0, 6);
        //CLES: Cavo la roba in più e elimino i doppi spazi
        String temp = testo;
        char c;
        for (int i = 0; i < temp.length(); i++) {
            c = temp.charAt(i);
            if ((c != ' ') && !Character.isLetter(c) && !Character.isDigit(c)) {
                temp = temp.replace(c, ' ');
            }
        }

        //calcoloCles(nomePuro);
        //Se gli apostrofi vanno gestiti con la formula strana, bisogna passare a questo.
        calcoloCles(nome);
        return true;
    }

    public static String rimuoviCarattere(String stringa, String da_rimuovere) {
        int n;
        int l = da_rimuovere.length();
        //Elimino gli _
        while ((n = stringa.indexOf(da_rimuovere)) >= 0)
            stringa = stringa.substring(0, n) + stringa.substring(n + l);
        return stringa;
    }

    private void calcoloCles(String temp) throws EccezioneSbnDiagnostico {
        //CLES: Cavo la roba in più e elimino i doppi spazi
        char c;
        temp = temp.trim();
        //Elimino gli _
        temp = rimuoviCarattere(temp, "_");
        if (tipoAut.equals("A") || tipoAut.equals("C")) {
            int n = temp.indexOf(' ');
            if (n > 0)
                temp = rimuoviCarattere(temp.substring(0, n), "-") + temp.substring(n).replace('-', ' ');
            else
                temp = rimuoviCarattere(temp, "-");
        } else {
            temp = temp.replace('-', ' ');
        }
        //Rimuovo #
        if (tipoAut.equals("A") || tipoAut.equals("B")) {
            temp = temp.replace('#', ' ');
        } else {
            temp = rimuoviCarattere(temp, "#");
        }
        //Tolgo tutti i brutti caratteri residui
        temp = rimuoviCarattere(temp,".");
        for (int i = 0; i < temp.length(); i++) {
            c = temp.charAt(i);
            if ((c != ' ') && !Character.isLetter(c) && !Character.isDigit(c)) {
                temp = temp.replace(c, ' ');
            }
        }

     // INIZIO Modifica settembre 2015 - GESTIONE CHIAVI CON CARATTERE "+" Si chiama solo il modulo UnicodeForOrdinamento2
        //Formatto
        // temp = Formattazione.formatta(temp);
        temp = Formattazione.formattaAutore(temp);

        if (temp.length() <= 50)
            ky_cles1_A = temp;
        else {
            ky_cles1_A = temp.substring(0, 50);
            if (temp.length() > 80)
                ky_cles2_A = temp.substring(50, 80);
            else
                ky_cles2_A = temp.substring(50);
        }

        // Manutenzione Evolutiva 29.05.2018 almaviva2 - Eliminazione degli articoli nella scrittura della Cles
        // usando la stopList ElencoArticoli così da consentire interrogazione degli Enti con la presenza/assenza degli articoli
        // INIZIO
        String nomeSenzaArticolo ="";
        if (tipoAut.equals("E") || tipoAut.equals("R")) {
        	nomeSenzaArticolo = dopoAsterisco(ky_cles1_A);
        	if (nomeSenzaArticolo.length() <= 50)
        		ky_cles1_A = nomeSenzaArticolo;
        	else {
               	ky_cles1_A = nomeSenzaArticolo.substring(0, 50);
	            if (nomeSenzaArticolo.length() > 80)
	            	ky_cles2_A = nomeSenzaArticolo.substring(50, 80);
	            else
	                ky_cles2_A = nomeSenzaArticolo.substring(50);
	        }
        }
        // FINE
    }

    /** Restituisce la stringa che segue l'asterisco */
    protected String dopoAsterisco(String stringa) {
        if (stringa == null)
            return null;
        int n = stringa.indexOf('*');
        if (n >= 0)
            return stringa.substring(n + 1);
        else {
            n = stringa.indexOf(' ');
            int n2 = stringa.indexOf('\'');
            if (n < 0 || (n2 >= 0 && n > n2)) {
                n = n2;
            }
            if (n >= 0)
                if (ElencoArticoli.getInstance().contiene(stringa.substring(0, n)))
                    return stringa.substring(n + 1);
        }
        return stringa;
    }



    /** Calcolo la chiave autori di 10 caratteri sull'intestazione (fino
     * al primo spazio o al primo -
     * Per il tipo autore A la chiave coincide con la prima parte del nome.
     */
    public String calcolaAuteur(String stringa) {
        int cont = 0;
        int my = 'z';
        stringa += " ";
        //lo aggiungo io, altrimenti rischio l'errore se c'è una sola
        //parola e non ci sono spazi in fondo
        while (my != ' ' && my != '-') {
            my = stringa.charAt(cont);
            cont++;
        }
        String parola = stringa.substring(0, cont);
        //Elimino tutti i caratteri diversi da lettere e numeri
        for (int i = 0; i < parola.length(); i++) {
            my = parola.charAt(i);
            if ((my < '0' || my > '9') && (my < 'a' || my > 'z') && (my < 'A' || my > 'Z')) {
                parola = parola.substring(0, i) + parola.substring(i + 1);
                i--;
            }
        }
        if (parola.length() >= 10)
            return parola.substring(0, 10).toUpperCase();
        else
            return parola.toUpperCase();
    }

    /**
     * Method calcolaChiaviA.
     * @param nome
     */
    private void calcolaChiaviA() {
        //Calcolo el3
        int pos = nome.indexOf(" : ");
        int cont;
        char mx;
        if (pos >= 0) {
            pos += 3;
            cont = contaFinoA(" -", nome, pos, ml);
            ky_el3 = nome.substring(pos, cont);
        }
        //fine calcolo el3
        //inizio el4
        pos = nome.indexOf(" <");
        if (pos >= 0) {
            pos += 2;
            cont = contaFinoA(" ->", nome, pos, ml);
            ky_el4 = nome.substring(pos, cont);
            //Fine el4
            //inizio el5
            pos = nome.indexOf(" ; ");
            if (pos >= 0) {
                pos += 3;
                cont = pos;
                mx = 'z';
                while (mx != ' ' && mx != '-' && mx != '>' && cont < ml) {
                    mx = nome.charAt(cont);
                    cont++;
                }
                ky_el5 = nome.substring(pos, cont);
            }
            //fine el5
        }
    }
    /**
     * Method calcolaChiaviC.
     * @param nome
     */
    private void calcolaChiaviC() {
        char mx = 'z';
        int cont;
        //calcolo el3
        int l = nome.indexOf(", ");
        if (l >= 0) {
            cont = l + 2;
            if (cont + 2< nome.length() && nome.substring(cont, cont + 2).equals(": ")) {
                cont += 2;
                calcolaA_El5();
                return;
            }
            if (nome.charAt(cont) == '<') {
                cont++;
                calcolaA_El5();
                return;
            }
            int pos = cont;
            cont = contaFinoA(" -\'", nome, pos, ml);
            ky_el3 = nome.substring(pos, cont);
            if (mx != '\'' && mx != '-') {
                if ((cont + 2 < ml && nome.substring(cont, cont + 2).equals(": "))
                    || (cont < ml && nome.charAt(cont) == '<')) {
                    calcolaA_El5();
                    return;
                }
            }
            //calcolo El4
            pos = cont;
            cont = contaFinoA(" -\'", nome, pos, ml);
            if (pos < cont)
                ky_el4 = nome.substring(pos, cont);
            //fine el4
            calcolaA_El5();
        }
    }
    /**
     * Method calcolaA_El5.
     * @param cont
     * @param nome
     */
    private void calcolaA_El5() {
        int cont;
        if ((cont = nome.indexOf(" <")) < 0)
            return;
        cont += 2;
        int pos = cont;
        cont = contaFinoA(" ->", nome, pos, ml);
        //        if (pos < cont)
        ky_el5 = nome.substring(pos, cont);
    }
    /**
     * Method calcolaChiaviE.
     * @param nome
     */
    private void calcolaChiaviD() {
        //calcolo el1
        int cont = 0;
        cont = contaFinoA(" -", nome, cont, ml);
        char mx = nome.charAt(cont - 1);
        int pos = cont;
        cont = contaFinoA(" -,", nome, pos, ml);
        ky_el1 = nome.substring(pos, cont - 1);
        mx = nome.charAt(cont - 1);
        if (mx != ',') {
            pos = cont;
            cont = contaFinoA(" -,\'", nome, cont, ml);
            ky_el2 = nome.substring(pos, cont);
            if (cont == nome.length())
                return;
            if (nome.charAt(cont) == '<') {
                calcolaD_El5();
                return;
            }
            if (nome.substring(cont, cont + 2).equals(": ")) {
                calcolaD_El5();
                return;
            }
            cont = contaFinoA(",", nome, pos, ml);
            mx = nome.charAt(cont - 1);
        }
        if (mx == ',') {
            if (cont < nome.length() && nome.charAt(cont) == ' ') {
                calcolaD_El3();
                return;
            }
        }
    }
    private void calcolaD_El3() {
        int l = nome.indexOf(", ");
        int cont = l + 2;




        // Inizio Intervento almaviva2 27.06.2011 segnalazione LIGURIA via Mail di  almaviva
        // inserito controllo che il contatore, aumentato di due caratteri non sfori la luinghezza del nome
        // (Vedi intrevento colleghi di Protocollo Indice per mantis 2409)
//        if (nome.substring(cont, cont + 2).equals(": ") || nome.charAt(cont) == '<') {
//            calcolaD_El5();
//            return;
//        }
        if ((nome.length() > cont + 2 &&
           nome.substring(cont, cont + 2).equals(": ")) || nome.charAt(cont) == '<') {
            calcolaD_El5();
            return;
        }
      // Fine Intervento almaviva2 27.06.2011 segnalazione LIGURIA via Mail di  almaviva



        int pos = cont;
        cont = contaFinoA(" -\'", nome, cont, ml);
        //        if (pos < cont)
        ky_el3 = nome.substring(pos, cont);
        char mx = nome.charAt(cont - 1);
        if (mx != '\'' && mx != '-') {
            if (nome.substring(cont).startsWith(": ") || nome.substring(cont).startsWith("<")) {
                calcolaD_El5();
                return;
            }
        }
        //calcolo El4
        pos = cont;
        cont = contaFinoA(" -\'", nome, cont, ml);
        //        if (pos < cont)
        ky_el4 = nome.substring(pos, cont);
        //fine el4
        calcolaD_El5();
    }
    private void calcolaD_El5() {
        int cont;
        if ((cont = nome.indexOf(" <")) < 0)
            return;
        cont += 2;
        int pos = cont;
        cont = contaFinoA(" ->", nome, cont, ml);
        //        if (pos < cont)
        ky_el5 = nome.substring(pos, cont - 1);
    }
    /**
     * Method calcolaChiaviG.
     * el1 deve contenere i primi 6 caratteri della prima parola della
    qualificazione se c'è prima di ' : '
    el2 deve contenere 2+1+1+1+1 della parte compresa tra ' : ' e ' <' oppure '
    : '
    el3 deve contenere i primi sei caratteri della prima parola della
    qualificazione tra il primo ' : ' e un eventuale secondo ' : '
    el4 deve contenere 2+1+1+1+1 della parte compresa tra il secondo ' : ' e '
    <' oppure ' : '
    el5 deve contenere 2+1+1+1+1 della parte compresa tra il terzo ' : ' e ' <'
    oppure ' : '
     * @param nome
     */
    private void calcolaChiaviG() {
        //elimino gli asterischi
        String nomeG = eliminaDoppiSpazi(nome.replace('*', ' '));
        int fine = nomeG.indexOf(" : ");
        String primaParte;
        String secondaParte;
        if (fine < 0) {
            primaParte = nomeG;
            secondaParte = "";
        }
        else {
            primaParte = nomeG.substring(0, fine);
            secondaParte = nomeG.substring(fine+3);
        }
        int init = primaParte.indexOf(" <");
        //Preparo la ky_el1
        if (init >= 0) {
            init += 2;
            char c = 'z';
            int end = init;
            while (c != ' ' && c != '>') {
                end++;
                c = primaParte.charAt(end);
            }
            ky_el1 = primaParte.substring(init, end);
        }
        //Calcolo prima la ky_el3
        fine = secondaParte.indexOf(" : ", fine + 3);
        if (fine >= 0) {
            secondaParte = secondaParte.substring(0,fine);
        }
        init = secondaParte.indexOf(" <");
        if (init >= 0) {
            init += 2;
            char c = 'z';
            int end = init;
            while (c != ' ' && c != '>') {
                end++;
                c = secondaParte.charAt(end);
            }
            ky_el3 = secondaParte.substring(init, end);
        }
        List<String> ky = new ArrayList<String>();
        //Preparo le altre ky_el
        for (int i = 0; i < 3; i++) {
            int mp = nomeG.indexOf(" : ");
            if (mp < 0)
                break;
            nomeG = nomeG.substring(mp + 3);
            String esteso;
            mp = nomeG.indexOf(" : ");
            if (mp < 0)
                esteso = nomeG.substring(0);
            else
                esteso = nomeG.substring(0, mp);
            if (esteso.indexOf(" <") > 0)
                esteso = esteso.substring(0, esteso.indexOf(" <"));
            int ml = esteso.length();
            char c = 'z';
            int end = 0;
            while (c != ' ' && end < ml - 1) {
                end++;
                c = esteso.charAt(end);
            }
            String parola = esteso.substring(0, end);
            if (parola.length() > 2)
                parola = parola.substring(0, 2);
            //esteso = esteso.substring(end+1);
            for (int ic = 0; ic < 4; ic++) {
                int n = esteso.indexOf(' ');
                if (n >= 0) {
                    parola += esteso.charAt(n + 1);
                    esteso = esteso.substring(n + 1);
                } else
                    break;
            }
            ky.add(parola);
        }
        if (ky.size() > 0)
            ky_el2 = ky.get(0);
        if (ky.size() > 1)
            ky_el4 = ky.get(1);
        if (ky.size() > 2)
            ky_el5 = ky.get(2);
    }
    /**
     * Rimuove i doppi spazi e li sostituisce con uno spazio singolo
     */
    public static String eliminaDoppiSpazi(String stringa) {
        int n;
        while ((n = stringa.indexOf("  ")) >= 0)
            stringa = stringa.substring(0, n) + stringa.substring(n + 1);
        return stringa;
    }

    /**
     * Method calcolaCautun.
     * @param i
     * @param nome
     * @param auteur
     * @param tipoAut
     */
//    protected String calcolaCautun(int p, String auteur, String tipoAut) {
//        String ky_cautun = null;
//        if (tipoAut.equals("E") || tipoAut.equals("R") || tipoAut.equals("G")) {
//            ky_cautun = p21111(auteur, tipoAut, p);
//        } else {
//            if (auteur.length() >= 4)
//                ky_cautun = auteur.substring(0, 4);
//            else {
//                ky_cautun = auteur;
//                for (int i = auteur.length(); i < 4; i++)
//                    ky_cautun += " ";
//            }
//            //if (tipoAut.equals("C"))
//            //    p++;
//            int cont = p;
//            for (int l = 0; l < 2; l++) {
//                if (cont >= ml || nome.charAt(cont) == '<')
//                    break;
//                if (tipoAut.equals("A") || tipoAut.equals("B")) {
//                    if (cont < ml - 2 && nome.substring(cont, cont + 2).equals(": ")) {
//                        cont += 2;
//                        p = cont;
//                    }
//                }
//                if (tipoAut.equals("C") || tipoAut.equals("D")) {
//                    if (cont < ml - 2 && nome.substring(cont, cont + 2).equals(": ")) {
//                        break;
//                    }
//                }
//                /* Mi riposiziono sulla parola successiva(la separazione delle parole
//                 * è data anche dal diesis e dall'apostrofo.)*/
//                cont = contaFinoA(" -#\'", nome, cont, ml);
//                /*Controllo il carattere da mettere in chiave, può essere solamente un numero spazio o lettera*/
//                char my;
//                while (p < cont) {
//                    my = nome.charAt(p);
//                    if (my == ' '
//                        || (my != 32 && (my < 48 || my > 57) && (my < 65 || my > 90) && (my < 97 || my > 122)))
//                        p++;
//                    else {
//                        ky_cautun += my;
//                        break;
//                    }
//                }
//                if (p == cont)
//                    l--;
//                if (cont >= ml) {
//                    break;
//                }
//                p = cont;
//            }
//        }
//        return ky_cautun.toUpperCase();
//    }



    private String calcolaCautun(int p, String auteur, String tipoAut, String nomeConvertito) {
        String ky_cautun = null;


        // Intervento Settembre 2013: viene inserito il contatore del campo nomeConvertito
        // per effettuare correttaqmente il calcolo della chiave.
        int contNomeConv = nomeConvertito.length();

        if (tipoAut.equals("E") || tipoAut.equals("R") || tipoAut.equals("G")) {
            // Manutenzione almaviva2 Febbraio 2018 - il calcolo della cautun, per quanto rigarda la parte relativa ai caratteri successivi
            // ai primi due , deve essere fatta sul campo nomeConvertito e non sul campo nome (cioè sul campo contenete solo le parole
            // a partire dal primo asterisco es. "The *Abcdefghi *XYZ" è nome mentre "*Abcdefghi *XYZ" è nomeConvertito altrimenti il calcolo
            // risulta errato)
            // ky_cautun = p21111(auteur, tipoAut, p);
        	ky_cautun = p21111(auteur, tipoAut, p, nomeConvertito);
        } else {
            if (auteur.length() >= 4)
                ky_cautun = auteur.substring(0, 4);
            else {
                ky_cautun = auteur;
                for (int i = auteur.length(); i < 4; i++)
                    ky_cautun += " ";
            }
            int cont = p;

            for (int l = 0; l < 2; l++) {
                if (cont >= contNomeConv || nomeConvertito.charAt(cont) == '<')
                    break;
                if (tipoAut.equals("A") || tipoAut.equals("B")) {
                    if (cont < contNomeConv - 2 && nomeConvertito.substring(cont, cont + 2).equals(": ")) {
                        cont += 2;
                        p = cont;
                    }
                }
                if (tipoAut.equals("C") || tipoAut.equals("D")) {
                    if (cont < contNomeConv - 2 && nomeConvertito.substring(cont, cont + 2).equals(": ")) {
                        break;
                    }
                }
                /* Mi riposiziono sulla parola successiva(la separazione delle parole
                 * è data anche dal diesis e dall'apostrofo.)*/
                cont = contaFinoA(" -#\'", nomeConvertito, cont, contNomeConv);
                /*Controllo il carattere da mettere in chiave, può essere solamente un numero spazio o lettera*/
                char my;
                while (p < cont) {
                    my = nomeConvertito.charAt(p);
                    if (my == ' '
                        || (my != 32 && (my < 48 || my > 57) && (my < 65 || my > 90) && (my < 97 || my > 122)))
                        p++;
                    else {
                        ky_cautun += my;
                        break;
                    }
                }
                if (p == cont)
                    l--;
                if (cont >= contNomeConv) {
                    break;
                }
                p = cont;
            }
        }
        return ky_cautun.toUpperCase();
    }



    /**
     * Method p21111.
     * @param auteur
     * @param nome
     * @param tipoAut
     * @param p
     * @return String
     */
    private String p21111(String auteur, String tipoAut, int p, String nomeConvertito) {
        String cautun;
        if (auteur.length() >= 2)
            cautun = auteur.substring(0, 2);
        else {
            cautun = auteur;
            while (cautun.length() < 2)
                cautun += " ";
        }
        int cont = p + 1;
        String temp = "";
        int l = 0;


        // Manutenzione almaviva2 Febbraio 2018 - il calcolo della cautun, per quanto rigarda la parte relativa ai caratteri successivi
        // ai primi due , deve essere fatta sul campo nomeConvertito e non sul campo nome (cioè sul campo contenete solo le parole
        // a partire dal primo asterisco es. "The *Abcdefghi *XYZ" è nome mentre "*Abcdefghi *XYZ" è nomeConvertito altrimenti il calcolo
        // risulta errato)
        int mlNomeConvertito = nomeConvertito.length();
        while (cont < mlNomeConvertito && l < 4) {
            if (nomeConvertito.charAt(cont) == '*')
                cont++;
            if (nomeConvertito.charAt(cont) == '<')
                break;
            if (tipoAut.equals("G") && nomeConvertito.substring(cont, cont + 2).equals(": "))
                break;
            p = cont;
            /* Mi riposiziono sulla parola successiva*/
            cont = contaFinoA(" -\'", nomeConvertito, cont, mlNomeConvertito);
            /*controllo il carattere da mettere in chiave */
            char my;
            while (p < cont) {
                my = nomeConvertito.charAt(p);
                if (my == ' '
                    || (my != 32 && (my < 48 || my > 57) && (my < 65 || my > 90) && (my < 97 || my > 122))) {
                    p++;
                    continue;
                }
                temp += nomeConvertito.charAt(p);
                break;
            }
            if (p == cont)
                l--;
            if (cont > mlNomeConvertito || cont < 0)
                break;
            l++;
        }
        return cautun + temp;
    }
    /**
     * Estratta dalla routine sbeleaut.elb di progress
     */
    private void calcolaChiaviB() {
        //Calcolo EL1
        int cont = 0;
        cont = contaFinoA(" -", nome, cont, ml);
        int pos = cont;
        cont = contaFinoA(" -\'", nome, cont, ml);
        if (pos < cont)
            ky_el1 = nome.substring(pos, cont);
        char mx = nome.charAt(cont - 1);
        if (mx == '-' || mx == '\'') {
            return;
        }
//      INIZIO COPIATO DA PROTOCOLLO INDICE
//      Mantis 0002690 Metto in try e catch perche va in errore su noni con accento finale paroa1 parola2' COPIATO DA PROTOCOLLO INDICE
        try {
	        if (mx == ' ') {
	            if (nome.charAt(cont) == '<') {
	                cont++;
	                calcolaB_El4(cont);
	                return;
	            } else if (nome.charAt(cont) == '<') {
	                cont += 2;
	                calcolaB_El3();
	                return;
	            }
	        }
		} catch (IndexOutOfBoundsException e) { }
//      FINE COPIATO DA PROTOCOLLO INDICE
        //fine calcolo el1
        //inizio el2
        pos = cont;
        cont = contaFinoA(" -\'", nome, cont, ml);
        //Qualche controllo.
        ky_el2 = nome.substring(pos, cont);
        if (cont == ml)
            return;
        if (nome.charAt(cont) == '<') {
            cont++;
            calcolaB_El4(cont);
            return;
        }
        //fine elaborazione el2
        calcolaB_El3();
    }
    /**
     * Method calcolaB_El4.
     * @param cont
     */
    private void calcolaB_El4(int cont) {
        //CAlcolo el4
        int pos = cont;
        cont = contaFinoA(" ->", nome, cont, ml);
        //cont--; //Aggiunto
        //        if (pos < cont)
        ky_el4 = nome.substring(pos, cont);
        //fine calcolo el4
        //calcolo el5
        pos = nome.indexOf(" ; ");
        if (pos >= 0) {
            pos += 3;
            cont = pos;
            cont = contaFinoA(" ->", nome, cont, ml);
            //            if (pos < cont)
            ky_el5 = nome.substring(pos, cont);
        }
    }
    /**
     * Method calcolaB_El3.
     * @param cont
     */
    private void calcolaB_El3() {
        //calcolo el3
        int cont;
        int pos = nome.indexOf(" : ");
        if (pos >= 0) {
            pos += 3;
            cont = pos;
            cont = contaFinoA(" -", nome, cont, ml);
            //            if (pos < cont)
            ky_el3 = nome.substring(pos, cont);
        }
        //fine elaborazione el3
        //preparo per el4
        if (nome.indexOf(" <") >= 0) {
            cont = nome.indexOf(" <") + 2;
            //continuo
            calcolaB_El4(cont);
        }
    }
    /**
     * Estratta dalla routine sbeleaut.elr di progress
     */
    private void calcolaChiaviER() {
        //Calcolo EL1
        int cont = ast + 1;
        cont = contaFinoA("*", nome, cont, ml);
        if (cont == ml) {
            calcolaER_El4();
            return;
        }
        int pos = cont;
        cont = contaFinoA(" -\'", nome, cont, ml);
        if (pos < cont)
            ky_el1 = nome.substring(pos, cont);
        //Calcolo el2
        cont = contaFinoA("*", nome, cont, ml);
        if (cont == ml) {
            calcolaER_El4();
            return;
        }
        pos = cont;
        cont = contaFinoA(" -\'", nome, cont, ml);
        if (pos < cont)
            ky_el2 = nome.substring(pos, cont);
        //calcolo el3
        cont = contaFinoA("*", nome, cont, ml);
        if (cont == ml) {
            calcolaER_El4();
            return;
        }
        pos = cont;
        cont = contaFinoA(" -\'", nome, cont, ml);
        if (pos < cont)
            ky_el3 = nome.substring(pos, cont);
        calcolaER_El4();
    }
    /**
     * Method calcolaB_El4.
     * @param cont
     */
    private void calcolaER_El4() {
        int cont = nome.indexOf(" <");
        if (cont < 0)
            return;
        cont += 2;
        //CAlcolo el4
        int pos = cont;
        cont = contaFinoA(" ->", nome, cont, ml);
        //cont--; //Aggiunto
        //        if (pos < cont)
        ky_el4 = nome.substring(pos, cont);
        //fine calcolo el4
        //calcolo el5
        pos = nome.indexOf(" ; ");
        if (pos >= 0) {
            pos += 3;
            cont = pos;
            cont = contaFinoA(" ->", nome, cont, ml);
            //            if (pos < cont)
            ky_el5 = nome.substring(pos, cont);
        }
    }
    private void trattaEl3() throws EccezioneSbnDiagnostico {
        if (ky_el3 == null)
            return;
    	ky_el3 = EliminaCarattere(ky_el3) ;
        ky_el3 = UnicodeForOrdinamento2.convert(ky_el3);
        String temp = "";
        char my;
        for (int cont = 0; cont < ky_el3.length(); cont++) {
            my = ky_el3.charAt(cont);
            if ((my > 47 && my < 58) || (my > 64 && my < 91) || (my > 96 && my < 123))
                temp += my;
            else if ((tipoAut.equals("A") || tipoAut.equals("B")) && (my == 35 || my == 39))
                temp += ' ';
        }
        if (temp.length() > 6)
            ky_el3 = temp.substring(0, 6).toUpperCase();
        else
            ky_el3 = temp.toUpperCase();
    }
    private String trasf_spazi(String chiave) throws EccezioneSbnDiagnostico {
        if (chiave == null)
            return null;
        chiave = UnicodeForOrdinamento2.convert(chiave);
        if (chiave == null)
            return null;
        String temp = "";
        char my;
        for (int cont = 0; cont < chiave.length(); cont++) {
            my = chiave.charAt(cont);
            if ((my > 47 && my < 58) || (my > 64 && my < 91) || (my > 96 && my < 123))
                temp += my;
            if ((tipoAut.equals("A") || tipoAut.equals("B")) && (my == 35 || my == 39))
                temp += ' ';
        }
        if (temp.length() > 6)
            return temp.substring(0, 6).toUpperCase();
        else
            return temp.toUpperCase();
    }
    private int contaFinoA(String caratteri, String testo, int da, int a) {
        char car;
        //Aggiungo questo controllo
        if (da >= a)
            return a;
        do {
            car = testo.charAt(da);
            da++;
        } while (da < a && caratteri.indexOf(car) < 0);
        return da;
    }
    /**
     * Returns the ky_auteur.
     * @return String
     */
    public String getKy_auteur() {
        return ky_auteur;
    }
    /**
     * Returns the ky_cautun.
     * @return String
     */
    public String getKy_cautun() {
        return ky_cautun;
    }

    /**
     * Returns the ky_el1a.
     * @return String
     */
    public String getKy_el1a() {
        if (ky_el1a == null)
            return null;
        while (ky_el1a.length() < 3)
            ky_el1a += " ";
        return ky_el1a;
    }
    /**
     * Returns the ky_el1b.
     * @return String
     */
    public String getKy_el1b() {
        if (ky_el1b == null)
            return null;
        while (ky_el1b.length() < 3)
            ky_el1b += " ";
        return ky_el1b;
    }

    /**
     * Returns the ky_el2a.
     * @return String
     */
    public String getKy_el2a() {
        if (ky_el2a == null)
            return null;
        while (ky_el2a.length() < 3)
            ky_el2a += " ";
        return ky_el2a;
    }
    /**
     * Returns the ky_el2b.
     * @return String
     */
    public String getKy_el2b() {
        if (ky_el2b == null)
            return null;
        while (ky_el2b.length() < 3)
            ky_el2b += " ";
        return ky_el2b;
    }
    /**
     * Returns the ky_el3.
     * @return String
     */
    public String getKy_el3() {
        if (ky_el3 == null)
            return null;
        while (ky_el3.length() < 6)
            ky_el3 += " ";
        return ky_el3;
    }
    /**
     * Returns the ky_el4.
     * @return String
     */
    public String getKy_el4() {
        if (ky_el4 == null)
            return null;
        while (ky_el4.length() < 6)
            ky_el4 += " ";
        return ky_el4;
    }
    /**
     * Returns the ky_el5.
     * @return String
     */
    public String getKy_el5() {
        if (ky_el5 == null)
            return null;
        while (ky_el5.length() < 6)
            ky_el5 += " ";
        return ky_el5;
    }
    /**
     * Returns the ky_cles1_A.
     * @return String
     */
    public String getKy_cles1_A() {
        return ky_cles1_A;
    }
    /**
     * Returns the ky_cles2_A, null se ky_cles2_A è lunga zero
     * @return String
     */
    public String getKy_cles2_A() {
        if (ky_cles2_A==null)
            return null;
        while (ky_cles2_A.length() < 30)
            ky_cles2_A += " ";
        return ky_cles2_A;
    }

	@Override
	public String getKy_el1() {
		return this.ky_el1 != null ? ValidazioneDati.fillRight(this.ky_el1, ' ', 6) : null;
	}

	@Override
	public String getKy_el2() {
		return this.ky_el2 != null ? ValidazioneDati.fillRight(this.ky_el2, ' ', 6) : null;
	}

    /**
     * Il metodo si applica soo se non ci sono già asterischi nel nome.
     *
     * per tipo E R G: si inserisce un asterisco davanti alle prime 4 parole
     * significative del nome (a_210) ad esclusione di articoli e preposizioni
     * (usa la stop list di tipo 'D' : è sul db nella tabella ts_stop_list ma
     * dovrebbe essere caricata in LDAP)
     *
     * per tipo G si inserisce un asterisco davanti alle prime 2 parole
     * significative del primo c_210
     *
     * @return
     */
    protected String aggiungiAsterischi() {
        int ast = nome.indexOf("*");
        if (ast < 0) {
            if (tipoAut.equals("E") || tipoAut.equals("R")) {
                nome = aggiungiMaxAsterischi(nome, 4);
            } else if (tipoAut.equals("G")) {
                int duepunti = nome.indexOf(" : ");
                if (duepunti >= 0) {
                    String nome1 = nome.substring(0, duepunti);
                    String nome2 = nome.substring(duepunti + 3);
                    nome1 = aggiungiMaxAsterischi(nome1, 4);
                    nome2 = aggiungiMaxAsterischi(nome2, 2);
                    nome = nome1 + " : " + nome2;
                } else {
                    //Qui non credo ci dovrebbe passare
                    nome = aggiungiMaxAsterischi(nome, 4);
                }
            }
        } //Aggiungo questa parte qui sotto per la import dei tipi G.
        else if (tipoAut.equals("G")) {
            int duepunti = nome.indexOf(" : ");
            if (duepunti >= 0) {
                String nome1 = nome.substring(0, duepunti);
                String nome2 = nome.substring(duepunti + 3);
                if (nome2.indexOf("*") < 0) {
                    nome2 = aggiungiMaxAsterischi(nome2, 2);
                    nome = nome1 + " : " + nome2;
                }
            }
        }
        return nome;
    }

    private String aggiungiMaxAsterischi(String nome, int max) {
        int contatore = 0;
        int unc = nome.indexOf("<");
        String fine = "";
        if (unc >= 0) {
            nome = nome.substring(0,unc);
            fine = nome.substring(unc);
        }
        StringTokenizer tokenizer = new StringTokenizer(nome, separatori);
        if (tokenizer.hasMoreTokens()) {
            String parola = tokenizer.nextToken();


            int pos = 0;
            while (parola != null && contatore < max) {
                if (!ElencoArticoli.getInstance().contiene(parola.toUpperCase(), "ita")) {
                    int init = nome.indexOf(parola, pos);
                    String a = nome.substring(0, init);
                    String b = nome.substring(init + parola.length(), nome.length());
                    String c = a + asterisco + parola + b;
                    nome = c;
                    contatore++;
                }
                try {
                    parola = tokenizer.nextToken();
                } catch (NoSuchElementException e) {
                    break;
                }
            }
        }
        return nome+fine;
    }

    public static String idTitAut(String bid, String vid, String cd_rel, String tp_resp) {
    	StringBuilder id = new StringBuilder(32);
    	id.append(bid).append('-').append(vid).append('-');
    	id.append(ValidazioneDati.isFilled(cd_rel) ? cd_rel : "REL").append('-');
    	id.append(ValidazioneDati.isFilled(tp_resp) ? tp_resp : "RESP");

    	return id.toString();
    }

	public static void main(String[] args) {
		String nome = "il mio porblema la casa.";
		ChiaviAutore ca = new ChiaviAutore("E", nome);
		ca.aggiungiAsterischi();

		System.out.println(idTitAut("CSWE123456", "CSWV123456", null, null));
		return;
	}

}
