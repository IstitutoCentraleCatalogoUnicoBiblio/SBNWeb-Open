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

import it.finsiel.sbn.polo.factoring.util.ElencoArticoli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Category;

/**
 * Classe IsbdAbstract
 * <p>
 * Metodi di utilità per il calcolo e l'estrazione dell'isbd dei titoli.
 * Contiene poi i metodi per gestire il titolo:
 * - gestione asterisco e caratteri di esclusione dell'articolo
 * - preparazione all'ordinamento
 * - creazione chiave oclc
 * </p>
 * @author
 * @author
 *
 * @version 28-mag-03
 */
public abstract class IsbdAbstract {

    static char[] separatoriC = { '\u0020', //spazio '-"+/:<=>\&@ il punto e la virgola ?
        '\'', '-', '\"', '+', '/', ':', '<', '=', '>', '\\', '&', '@' };
    static String separatori = new String(separatoriC);
    static String asterisco = "\u002a";
    static char spazioC = '\u0020';
    static String spazio = "" + spazioC;
    /**Elenco degli articoli nelle varie lingue*/
    /**Caratteri di esclusione.
     * Per efficienza utilizzo una matrice. Si ha un accesso sequenziale.*/
    static String[][] caratteriEsclusione = { { "nsb", "nse" }, {
            "<<", ">>" }
    };
    String lingua;

    static Category log = Category.getInstance("iccu.box.Isbd");

    //static {
    //    initCaratteriEsclusione();
    //    initStopList();
    //}

    /**
     * Crea una stringa concatenando tutti gli elementi dell'array di stringhe
     * contenuto.<br>
     * Segue la formula:<br>
     * 'intro'+'primoElemento' [+ 'prefisso'+'altriElementi'+'suffisso']
     */
    protected String concatena(
        String stringa,
        String contenuto[],
        String intro,
        String prefisso,
        String suffisso) {
        if (intro.startsWith("."))
            if (stringa.endsWith("."))
                intro = intro.substring(1);
        StringBuffer sb = new StringBuffer(stringa);
        sb.append(intro);
        for (int i = 0; i < contenuto.length; i++) {
            if (i != 0)
                sb.append(prefisso);
            sb.append(contenuto[i] + suffisso);
        }
        return sb.toString();
    }
    /**
     * Crea una stringa concatenando tutti gli elementi dell'array di stringhe
     * contenuto.<br>
     * Segue la formula:<br>
     * 'intro'+'primoElemento'+'terminale' [+ 'prefisso'+'altriElementi'+'suffisso']
     */
    protected String concatena(
        String stringa,
        String contenuto[],
        String intro,
        String terminale,
        String prefisso,
        String suffisso) {
        if (intro.startsWith("."))
            if (stringa.endsWith("."))
                intro = intro.substring(1);
        StringBuffer sb = new StringBuffer(stringa);
        sb.append(intro);
        for (int i = 0; i < contenuto.length; i++) {
            if (i == 0)
                sb.append(contenuto[i] + terminale);
            else
                sb.append(prefisso + contenuto[i] + suffisso);
        }
        return sb.toString();
    }
    /**
     * Crea una stringa concatenando contenuto al prefisso e suffisso.<br>
     * Segue la formula:<br>
     * 'prefisso'+'contenuto'+'suffisso'
     */
    protected String concatena(String stringa, String contenuto, String prefisso, String suffisso) {
        if (prefisso.startsWith("."))
            if (stringa.endsWith("."))
                prefisso = prefisso.substring(1);
        return stringa + prefisso + contenuto + suffisso;
    }
    /**
     * Crea una stringa concatenando i contenuti.<br>
     */
    protected String concatena(String stringa, String suffisso) {
        if (suffisso.startsWith("."))
            if (stringa.endsWith("."))
                suffisso = suffisso.substring(1);
        return stringa + suffisso;
    }
    /**
     * Crea una stringa concatenando i contenuti.<br>
     */
    protected boolean conflittopunti(String stringa, String suffisso) {
        if (suffisso.startsWith("."))
            if (stringa.endsWith("."))
                return true;
        return false;
    }
    /**
     * Verifica se esiste l'asterisco in un array di titoli,
     * se non esiste lo aggiunge.
     */
    protected String[] aggiungiAsterisco(String[] a_200) {
        String[] ritorno = new String[a_200.length];
        for (int i = 0; i < a_200.length; i++)
            ritorno[i] = aggiungiAsterisco(a_200[i]);
        return ritorno;
    }
    /**
     * Verifica se esiste l'asterisco in un array di titoli,
     * se non esiste lo aggiunge.
     */
    protected String[] aggiungiAsteriscoA200(String[] a_200) {
        a_200[0] = aggiungiAsterisco(a_200[0]);
        return a_200;
    }

    /**
     * Toglie l'asterisco sostituendolo con la relativa sequenza di
     * caratteri di esclusione.
     */
    public String sostituisciAsterisco(String titolo, String codifica) {
        String init, end;
        if (codifica.equalsIgnoreCase("UNIMARC")) {
            init = "<<";
            end = ">>";
        } else {
            init = "";
            end = "";
        }
        return init
            + titolo.substring(0, titolo.indexOf(asterisco)).trim()
            + end
            + spazio
            + titolo.substring(titolo.indexOf(asterisco) + 1);
    }

    /**
         * Inizializza la lista delle coppie (init,end) della stringa da escludere
     * @param nomeFile
     */
    public static void initCaratteriEsclusione(String nomeFile) throws IOException {
        Map elementi = new HashMap();
        BufferedReader filein = new BufferedReader(new FileReader(nomeFile));
        // Ciclo per la lettura completa del file riportato su di una stringa
        String linea;
        String init, end;
        while (filein.ready()) {
            linea = filein.readLine().trim();
            if (linea != null && !linea.equals("") && !linea.startsWith("#")) {
                linea = linea.substring(linea.indexOf(" ") + 1);
                //consumo il nome
                init = linea.substring(0, linea.indexOf(" "));
                linea = linea.substring(linea.indexOf(" ") + 1);
                end = linea.substring(linea.indexOf(" ") + 1);
                elementi.put(init, end);
            }
        }
        filein.close();
        caratteriEsclusione = new String[elementi.size()][2];
        Iterator en = elementi.keySet().iterator();
        for (int i = 0; en.hasNext(); i++) {
            caratteriEsclusione[i][0] = (String) en.next();
            caratteriEsclusione[i][1] = (String) elementi.get(caratteriEsclusione[i][0]);
        }
    }
    /**
     * Prepara un titolo per l'ordinamento:
     * rimuove la punteggiatura (ripulendo eventuali doppi spazi così creati)
     * rende tutto maiuscolo.
     * Prevede che ci sia già l'asterisco.
     */
    public String preparaPerOrdinamento(String titolo) {
        char c;
        //Parto dopo l'asterisco:

        titolo = titolo.substring(titolo.indexOf(asterisco));
        //togli punteggiatura
        //Separatori sostituiti dallo spazio
        for (int i = 0; i < separatoriC.length; i++) {
            c = separatoriC[i];
            titolo = titolo.replace(c, spazioC);
        } //Il resto deve essere semplicemente tolto
        for (int i = 0; i < titolo.length(); i++) {
            c = titolo.charAt(i);
            if ((c != spazioC)
                && !Character.isLetter(c)
                && !Character.isDigit(c)) { //titolo=titolo.replace(c,spazioC);
                titolo = titolo.substring(0, i) + titolo.substring(i + 1);
            }
        }
        int n;
        //elimina doppi spazi(dovuti alla punteggiatura)
        while ((n = titolo.indexOf(spazio + spazio)) > 0)
            titolo = titolo.substring(0, n) + titolo.substring(n + 1);
        //rendi tutto maiuscolo
        return titolo.toUpperCase();
    }

    /**
     * Inserisce l'asterisco nel punto necessario:
     * estra la prima parola della stringa, verifica se questa appartiene alla stop list
     * e quindi, se esiste, aggiunge l'asterisco prima della seconda parola.
     * Se la prima parola non è nella stop list l'asterisco viene inserito all'inizio
     * del titolo
     * Restituisce la stringa con l'asterisco
     */
    public String inserisciAsteriscoDopoArticolo(String titolo) {
        StringTokenizer tokenizer = new StringTokenizer(titolo, separatori);
        String primaParola = tokenizer.nextToken();
        if (!tokenizer.hasMoreTokens())
            return asterisco + titolo;
        String secondaParola = tokenizer.nextToken();
        int initResto = titolo.indexOf(secondaParola, primaParola.length());
        String separatore = titolo.substring(primaParola.length(), initResto);
        String resto = titolo.substring(initResto);
        if (ElencoArticoli.getInstance().contiene(primaParola.toUpperCase(), lingua))
            //for (int i = 0; i < stopList.length; i++)
            //if (stopList[i].equalsIgnoreCase(primaParola))
            return primaParola + separatore + asterisco + resto;
        return asterisco + titolo;
    }
    /**
     * Verifica se è presente l'asterisco nel titolo,
     * se non esiste lo aggiunge.
     */
    public String aggiungiAsterisco(String titolo) {
        if (titolo.indexOf(asterisco) >= 0)
            return titolo;
        String retString = sostituisciCaratteriEsclusione(titolo);
        if (retString != null)
            return retString.trim();
        return inserisciAsteriscoDopoArticolo(titolo);
    }
    /**
     * Sostituisce i caratteri di esclusione che sono contenuti in properties.
     * Verifica se esistono caratteri di esclusione della prima parte ( nsg,<< etc.)
     * se esistono sostituisce il carattere di fine stringa (nse, >> etc.) con
     * il carattere asterisco, ed elimina il carattere di inizio stringa.
     * @return la stringa contenente il testo modificato oppure null se non esistono i caratteri
     * di inizio e fine stringa
     */
    protected String sostituisciCaratteriEsclusione(String titolo) {
        int init;
        for (int i = 0; i < caratteriEsclusione.length; i++)
            if ((init = titolo.toUpperCase().indexOf(caratteriEsclusione[i][0])) >= 0) {
                //titolo.replaceFirst(properties[i][0],"");
                titolo =
                    titolo.substring(0, init) + titolo.substring(init + caratteriEsclusione[i][0].length());
                //titolo.replaceFirst(properties[i][1],asterisco);
                int lung = caratteriEsclusione[i][1].length();
                init = titolo.toUpperCase().indexOf(caratteriEsclusione[i][1]);
                //if (titolo.substring(init + lung, init + lung + 1).equals(spazio))
                if (titolo.charAt(init + lung) == spazioC)
                    return titolo.substring(0, init) + spazio + asterisco + titolo.substring(init + lung + 1);
                return titolo.substring(0, init) + asterisco + titolo.substring(init + lung);
            }
        return null;
    }

    /** Legge dall'indice la posizione relativa ad un campo, estraendo il tag numero index */
    public static int posizione(String indice, String nome) {
        return posizione(indice, nome, 0);
    }

    /** Legge dall'indice la posizione relativa ad un campo, estraendo il tag numero index */
    public static int posizione(String indice, String nome, int index) {
        String separatore_tag = "-";
        String separatore_area = ";";
        int start = 0;
        int tmp = 0;
        for (int i = 0; i <= index; i++) {
            tmp = indice.indexOf(nome + separatore_tag, start);
            if (tmp < 0)
                start = tmp;
            else
                start = tmp + 1;
        }
        if (start < 0)
            return -1;
        start ++;
        int end = indice.indexOf(separatore_area, start);
        if (end < 0)
            end = indice.length();
        return Integer.parseInt(
            indice.substring(
                indice.indexOf(separatore_tag, start) + 1,
                end))
            - 1;
        //ATTENZIONE AL -1
    }

    /** Legge dall'indice la posizione relativa ad un campo */
    public static int successivo(String indice, String nome) {
        return successivo(indice, nome, 0);
    }
    protected static int successivo(String indice, String nome, int index) {
        String separatore_tag = "-";
        String separatore_area = ";";
        int start = -1;
        for (int i = 0; i <= index; i++)
            start = indice.indexOf(nome + separatore_tag, start + 1);
        if (start < 0)
            return -1;
        start = indice.indexOf(separatore_tag, start + nome.length() + 2);
        if (start < 0)
            return -1;
        start++;
        int end = indice.indexOf(separatore_area, start);
        if (end < 0)
            end = indice.length();
        return Integer.parseInt(indice.substring(start, end)) - 1;
        //ATTENZIONE AL -1
    }

    /**
     * Calcola la chiave oclc da un titolo:
     * consiste nella estrazione delle prime 3 lettere della prima parola,
     * più la prima lettera delle tre successive parole.
     * Se la prima parola è più corta di tre caratteri gli appende degli spazi.
     *
     * Rende la chiave maiuscola.
     */
    public String calcolaOCLC(String titolo) {
        String parola, oclc = null;
        int n;
        for (int i = 0; i < 4; i++) {
            parola = "";
            if ((n = titolo.indexOf(spazio)) > 0) {
                parola = titolo.substring(0, n);
                titolo = titolo.substring(n + 1);
            } else {
                parola = titolo;
                titolo = "";
            }
            if (i == 0) {
                if (parola.length() >= 3)
                    oclc = parola.substring(0, 3);
                else {
                    oclc = parola;
                    while (oclc.length() < 3)
                        oclc += spazio;
                }
            } else
                oclc = oclc + (parola != null && parola.length() > 0 ? parola.substring(0, 1) : " ");
        }
        return oclc.toUpperCase();
    }


}
