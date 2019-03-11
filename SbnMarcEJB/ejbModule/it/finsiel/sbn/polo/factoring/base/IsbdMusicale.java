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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ElencoArticoli;
import it.finsiel.sbn.polo.factoring.util.ElencoForme;
import it.finsiel.sbn.polo.factoring.util.ElencoFormeU;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Classe IsbdMusicale
 * CREATA PER FINSIEL: da sistemare e inviare ogni volta che si apportano modifiche al
 * metodo definisciISBDtitUniMusicale
 * <p>
 * Questa classe contiene i metodi con cui si elabora l'isbd della composizione musicale.
 * Deve essere estesa per implementarne il metodo abstract getDescrizioneCodice che accede
 * a informazioni lette da DB.
 * </p>
 * @author Data Management s.p.a.
 * @author Ragazzini Taymer
 *
 * @version 30-mar-2007
 */
public abstract class IsbdMusicale {
    static char[] separatoriC = { '\u0020', //spazio '-"+/:<=>\&@ il punto e la virgola ?
        '\'', '-', '\"', '+', '/', ':', '<', '=', '>', '\\', '&', '@' };
    static String separatori = new String(separatoriC);
    static String asterisco = "\u002a";
    static char spazioC = '\u0020';
    static String spazio = "" + spazioC;
    /**Elenco degli articoli nelle varie lingue*/
    /**Caratteri di esclusione.
     * Per efficienza utilizzo una matrice. Si ha un accesso sequenziale.*/
    static String[][] caratteriEsclusione = { { "nsg", "nse" }, {
            "<<", ">>" }
    };

    String indice_isbd;
    String isbd;
    boolean str;
    String lingua;

    /**
     * Per leggere l' isbd al termine dell'elaborazione
     * @return la stringa isbd
     */
    public String getIsbd() {
        return isbd;
    }

    /**
     * Per leggere l'indice isbd al termine dell'elaborazione
     * @return la stringa indice dell'isbd
     */
    public String getIndice_isbd() {
        return indice_isbd;
    }

    public void definisciISBDtitUniMusicale(
        String a_929,
        String b_929,
        String c_929,
        String e_929,
        String f_929,
        String g_929,
        String h_929,
        String i_929,
        String a_928[],
        String b_928,
        String c_928)
        throws EccezioneSbnDiagnostico {
        str = false;
        indice_isbd = elementoIndiceIsbd("A", 1);
        isbd = g_929;
        if (isbd == null)
            isbd = "";
        if (h_929 != null) {
            h_929 = rimuoviAsterisco(h_929);
            isbd = concatena(isbd, ". ");
            indice_isbd += elementoIndiceIsbd("I", isbd.length());
            isbd = concatena(isbd, h_929);
        }
        if (b_928 != null) {
            String b928 = preparaB928(a_928, b_928, c_928, g_929);
            if (b928 != null) {
                indice_isbd += elementoIndiceIsbd("R", isbd.length());
                isbd = concatena(isbd, b928);
            }
        }
        boolean numero = false;
        if (a_929 != null) {
            a_929 = pulisciNumero(a_929);
            if (a_929.length() > 0) {
                isbd = concatena(isbd, ". N.");
                indice_isbd += elementoIndiceIsbd("S", isbd.length());
                isbd = concatena(isbd, a_929);
                numero = true;
            }
        }
        if (c_929 != null) {
            isbd = concatena(isbd, ". ");
            if (!numero)
                indice_isbd += elementoIndiceIsbd("S", isbd.length());
            isbd = concatena(isbd, c_929);
            numero = true;
        }
        if (b_929 != null && c_929 == null) {
            b_929 = pulisciNumero(b_929);
            if (b_929.length() > 0) {
                isbd = concatena(isbd, ". Op.");
                if (!numero)
                    indice_isbd += elementoIndiceIsbd("S", isbd.length());
                isbd = concatena(isbd, b_929);
            }
        }
        if (e_929 != null && str) {
            String ton;
            ton = getDescrizioneCodice("TONO", e_929);
            if (ton != null) {
                isbd = concatena(isbd, ". ");
                indice_isbd += elementoIndiceIsbd("U", isbd.length());
                isbd = concatena(isbd, ton);
            }
        }
        if (i_929 != null) {
            i_929 = rimuoviAsterisco(i_929);
            isbd = concatena(isbd, ". ");
            indice_isbd += elementoIndiceIsbd("I", isbd.length());
            isbd = concatena(isbd, i_929);
        }
        verificaDueAsterischi(isbd);
    }

    /**
     * Rimuove gli asterische da una stringa
     * @param stringa
     * @return la stringa senza più gli asterischi.
     */
    protected static String rimuoviAsterisco(String stringa) {
        if (stringa == null)
            return null;
        int n;
        while ((n=stringa.indexOf('*'))>=0) {
            stringa = stringa.substring(0,n)+stringa.substring(n+1);
        }
        return stringa;
    }

    /**
     * Costruisce un singolo elemento dell'indice
     */
    protected String elementoIndiceIsbd(String tipo, int posizione) {
        String tmp = "";
        if (posizione < 10) {
            tmp = "000";
            tmp += posizione;
        } else if (posizione < 100) {
            tmp = "00";
            tmp += posizione;
        } else if (posizione < 1000) {
            tmp = "0";
            tmp += posizione;
        }
        return tipo + "-" + tmp + ";";
    }
    protected void verificaDueAsterischi(String stringa) throws EccezioneSbnDiagnostico {
        int counter = 0;
        int tmp = -1;
        while ((tmp = stringa.indexOf("*", tmp + 1)) >= 0)
            counter++;
        if (counter > 2)
            throw new EccezioneSbnDiagnostico(3059, "Al massimo 2 asterischi");
        if (counter == 2) {
            if (stringa.indexOf(" : ") > 0)
                if (stringa.lastIndexOf('*') < stringa.indexOf(" : "))
                    throw new EccezioneSbnDiagnostico(
                        3078,
                        "Secondo asterisco deve essere nel complemento del titolo");
        }
    }
    /**
     * Verifica se è presente l'asterisco nel titolo,
     * se non esiste lo aggiunge.
     */
    public String aggiungiAsterisco(String titolo) {
        if (titolo.indexOf("*") >= 0)
            return titolo;
        String retString = sostituisciCaratteriEsclusione(titolo);
        if (retString != null)
            return retString;
        return inserisciAsteriscoDopoArticolo(titolo);
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
    private String pulisciNumero(String stringa) {
      int i = 0;
      for (; i < stringa.length(); i++) {
        if (Character.isDigit(stringa.charAt(i)))
          break;
      }
      return stringa.substring(i);
    }

    public String preparaB928(String a_928[], String b_928, String c_928, String g_929)
    throws EccezioneSbnDiagnostico {
    String token;
    boolean prosegui = false;
    for (int i = 0; i < a_928.length; i++) {
        String fomu;
            fomu = getDescrizioneCodice("FOMU", a_928[i]);
        if (ElencoFormeU.getInstance().contiene(fomu)) {
            prosegui = true;
            break;
        }
    }
    if (!prosegui) {
        if (ElencoForme.getInstance().contiene(NormalizzaNomi.normalizzazioneGenerica(g_929))) {
            prosegui = true;
        }
    }
    if (!prosegui)
        return null;
    b_928 = b_928.trim();
    if (b_928.indexOf(' ')>=0) {
        throw new EccezioneSbnDiagnostico(3238,"Spazi non ammessi");
    }
    if (c_928 != null) {
        c_928 = c_928.trim();
        if (c_928.indexOf(' ')>=0) {
            throw new EccezioneSbnDiagnostico(3238,"Spazi non ammessi");
        }
    }

    StringTokenizer st = new StringTokenizer(b_928, ",");
    str = true;
    while (st.hasMoreElements()) {
        token = st.nextToken();
        if (token.endsWith("str")) {
            // boolean digit = true;
            // for (int i = 0; i < token.length() - 3; i++)
            // if (!Character.isDigit(token.charAt(i)))
            // digit = false;
            // if (!digit)
            //   break;
            if (c_928 == null)
                return ". " + b_928;
            if (st.hasMoreTokens()) {
                token = st.nextToken();
                if (token.equals("orch")
                    || token.equals("banda")
                    || token.equals("orchfi")
                    || token.equals("orchar")) {
                    String ret = preparaC928(c_928, true);
                    if (!ret.equals(". "))
                        ret += " e ";
                    ret += getOrganico(token, false);
                    return ret;
                } else
                    return preparaC928(c_928, false);
            } else
                return preparaC928(c_928, false);
        }
    }
    //Elaboro la stringa b_928
    List tokens = separaToken928(b_928);
    String risultato = "";
    int counter = 0;
    String precToken = null;
    for (int i = 0; i < tokens.size(); i++) {
        token = (String) tokens.get(i);
        if (token.toUpperCase().indexOf("CORO")>=0) {
            int lung = token.indexOf("(") + 1;
            if (token.toUpperCase().endsWith("V)"))
                if (isNumber(token.substring(lung, token.length() - 2))) {
                  int init = token.toUpperCase().indexOf("CORO");
                  if (init > 0) {
                    token = token.substring(0,init) + "Cori a " + token.substring(lung, token.length() - 2) + " voci";
                  } else {
                    token = "Coro a " + token.substring(lung, token.length() - 2) + " voci";
                  }
                }
            risultato += "," + token;
            continue;
        } else if (token.endsWith("V")) {
            if (isNumber(token.substring(0, token.length() - 1))) {
                int n = Integer.parseInt(token.substring(0, token.length() - 1));
                if (c_928 == null)
                    //throw new EccezioneSbnDiagnostico(3216, "Manca l'organico analitico");
                    return ". " + b_928;
                risultato += "," + appendiVoci(c_928, n);
                continue;
            } else if (token.startsWith(">")){
              risultato += ". Voci" ;
              continue;
            }
        }
        if ((i + 1) < tokens.size()
            && togliNumeroFinale(token).equals(togliNumeroFinale((String) tokens.get(i + 1)))) {
            counter++;
        } else {
            if (Character.isDigit(token.charAt(0))) {
                String num ="";
                for (int n = 0; n< token.length() && Character.isDigit(token.charAt(n));n++)
                    num+= token.charAt(n) ;
                token = token.substring(num.length());
                if (token.toUpperCase().startsWith("CORO(") || token.toUpperCase().startsWith("CORO (")) {
                  int lung = 5;
                  if (token.toUpperCase().startsWith("CORO ("))
                      lung = 6;
                  if (token.toUpperCase().endsWith("V)"))
                      if (isNumber(token.substring(lung, token.length() - 2))) {
                          token = "Cori a " + token.substring(lung, token.length() - 2) + " voci";
                      }
                  risultato += "," + num+ " " + token;
                } else {
                  risultato += "," + num+ " " + getOrganico(token, true);
                }
            } else if (counter > 0) {
                risultato += "," + (counter + 1) + " " + getOrganico(togliNumeroFinale(token), true);
                counter = 0;
            } else {
                risultato += "," + getOrganico(togliNumeroFinale(token), false);
            }
        }

    }
    //Cavo la virgola iniziale.
    if (risultato.length() > 0)
        risultato = ". " + risultato.substring(1);
    return risultato;
}
    protected boolean isNumber(String token) {
        int i = token.length();
        if (i == 0)
            return false;
        for (; i > 0; i--)
            if (!Character.isDigit(token.charAt(i - 1)))
                return false;
        return true;

    }
    protected String togliNumeroFinale(String token) {
        int i = token.length();
        for (; i > 0; i--)
            if (!Character.isDigit(token.charAt(i - 1)))
                break;
        return token.substring(0, i);
    }
    protected List separaToken928(String b928) {
        List v = new ArrayList();
        String token = "";
        boolean inside = false;
        int n = 0;
        char c;
        while (n < b928.length()) {
            c = b928.charAt(n);
            if (c == ',' && !inside) {
                v.add(token);
                token = "";
            } else {
                if (c == '(')
                    inside = true;
                else if (c == ')')
                    inside = false;
                token += c;
            }
            n++;
        }
        v.add(token);
        return v;
    }
    /** Elabora l'organico analitico della composizione musicale */
    protected String preparaC928(String stringa, boolean solo)
    throws EccezioneSbnDiagnostico {
    if (stringa == null) {
        //throw new EccezioneSbnDiagnostico(3216, "Manca l'organico analitico");
        return null;
    }
    str = true;
    String token;
    String risultato = ". ";
    int counter = 0;
    boolean primo = true;
    List tokens = separaToken928(stringa);
    if (!solo) {
        for (int i = 0; i < tokens.size(); i++) {
            token = (String) tokens.get(i);
            String num ="";
            for (int n = 0; n< token.length() && Character.isDigit(token.charAt(n));n++)
                num+= token.charAt(n) ;
            token = token.substring(num.length());
            if (token.toUpperCase().startsWith("CORO")) {
                StringTokenizer st = new StringTokenizer(token, ",");
                int voci = 0;
                while (st.hasMoreTokens()) {
                    st.nextToken();
                    voci ++;
                }
                if (primo)
                    primo = false;
                else
                    risultato += ",";
                if (num.length() > 0)
                  risultato += num + " cori a " + voci + " voci";
                else
                risultato += "Coro a " + voci + " voci";
            } else if ((i + 1) < tokens.size()
                && togliNumeroFinale(token).equals(togliNumeroFinale((String) tokens.get(i + 1)))) {
                counter++;
            } else {
              if (counter > 0 || num.length() > 0) {
                if (num.length() > 0)
                  counter = Integer.parseInt(num) - 1;
                   if (primo)
                        primo = false;
                    else
                        risultato += ",";
                    //Si usa il plurale
                    risultato += (counter + 1)
                        + " "
                        + getOrganico(togliNumeroFinale(token), true);
                    counter = 0;
                } else {
                    if (primo)
                        primo = false;
                    else
                        risultato += ",";
                    risultato += getOrganico(togliNumeroFinale(token), false);
                }
            }
        }
    } else {
        StringTokenizer st = new StringTokenizer(stringa, ",");
        while (st.hasMoreElements()) {
            token = st.nextToken();
            if (token.endsWith("-solo")) {
                if (primo)
                    primo = false;
                else
                    risultato += ",";
                risultato += getOrganico(token.substring(0, token.length() - 5), false);
            }
        }
    }
    return risultato;
}

    protected String getOrganico(String token, boolean plurale) throws EccezioneSbnDiagnostico {
      String orga;
      int n;
      if (token.endsWith("-solo")) {
        token = token.substring(0, token.length() - 5);
      }
      while((n = token.indexOf("$")) >= 0)  {
              token = token.substring(0, n) + token.substring(n+1);
      }

      while((n = token.indexOf("%")) >= 0)  {
              token = token.substring(0, n) + token.substring(n+1);
      }
      token = togliNumeroFinale(token);
      String codice;
      if (plurale) codice = "ORGP";
      else codice = "ORGA";
        orga = getDescrizioneCodice(codice, token);
      if (orga == null) {
          int barra = token.indexOf("/");
          if (barra>=0) {
              orga = getOrganico(token.substring(0,barra), plurale)+" o "+getOrganico(token.substring(barra+1), plurale);
          } else {
              EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3124, "Organico errato");
              ecc.appendMessaggio(": \"" + token + "\"");
              throw ecc;
          }
      }
      return orga;
  }

    protected String appendiVoci(String stringa, int n) {
        StringTokenizer st = new StringTokenizer(stringa, ",");
        String token;
        String risultato = "";
        boolean first = true;
        while (st.hasMoreElements() && n > 0) {
            token = st.nextToken();
            if (first) {
                first = false;
            } else {
                risultato += ",";
            }
            if (Character.isDigit(token.charAt(0))) {
                String num ="";
                for (int i = 0; i< token.length() && Character.isDigit(token.charAt(i));i++)
                    num+= token.charAt(i) ;
                token = token.substring(num.length());
                risultato += num+ " " + getDescrizioneCodice("ORGP",token);
                n -= Integer.parseInt(num);
            } else {
                risultato += getDescrizioneCodice("ORGA",token);
                n--;
            }
        }
        return risultato;
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
     * FINSIEL - Akros
     *
     * Metodo abstract da implementare nelle classi CostuttoreIsbdFinsiel
     * e CostruttoreIsbdAkros.
     *
     */
    protected abstract String getDescrizioneCodice(String tipoCodice, String codice);

}
