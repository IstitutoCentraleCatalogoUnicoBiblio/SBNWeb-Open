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
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.Ac_210Type;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C205;
import it.iccu.sbn.ejb.model.unimarcmodel.C206;
import it.iccu.sbn.ejb.model.unimarcmodel.C207;
import it.iccu.sbn.ejb.model.unimarcmodel.C208;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C215;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.Cf_200Type;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Classe ISBD
 * <p>
 * Gestisce il campo ISBD di un titolo.
 * Crea gli oggetti Castor da una stringa che contiene tutte le informazioni
 * concatenate con la sintassi SBN.
 * </p>
 *      1) dalla punteggiatura . (( è l'area delle note 300, se ci sono . - dopo .
 *         (( sono tutte aree 300
 *
 *      2) si scompatta la riga con la punteggiatura . -
 *      La prima area è l'unica obbligatoria ed è sempre la 200
 *
 *      se la natura è S
 *      se ci sono 5 aree sono in sequenza 200, 205, 207, 210, 215
 *      se ci sono 4 aree sono 200, 207, 210,215
 *      se ci sone 3 aree sono 200,210,215
 *      se ci sono 2 aree sono 200,210
 *
 *      se la natura è C ci posso essere al massimo due aree, la 200 e la 210
 *
 *      se la natura è M o W
 *      se ci sono 4 aree sono le 200,205,210,215
 *      se ci sono 3 aree sono le 200,210,215
 *      se ci sono 2 aree sono 200,210
 *
 *      Per le altre nature c'è solo una area 200.
 *
 * @author Akros Informatica s.r.l.
 * @author Ragazzini Taymer
 *
 * @version 16-ott-02
 */
public class Isbd extends IsbdAbstract {
    C200 c200;
    C205 c205;
    C206[] c206;
    C207 c207;
    C208 c208;
    C210[] c210;
    C215 c215;
    A230 c230;
    C3XX[] c3xx;
    boolean antico = false;
    Tb_titolo titolo = null;

    protected boolean str;
    /**
     * METODO PER LA RICOSTRUZIONE DEGLI ELEMENTI ISBD
     * Ricrea le stringhe dei vari elementi, e da queste ricava gli
       * elementi castor.
     */
    public void ricostruisciISBD(Tb_titolo titolo) {
        try {
            String indice = titolo.getINDICE_ISBD();
            if (indice == null || indice.trim().equals(""))
                indice = "200-0001;";
            String input_isbd = titolo.getISBD();
            this.titolo = titolo;
            antico = titolo.getTP_MATERIALE().equals("E");
            int init = posizione(indice, "200");
            int next;
            if (init >= 0) {
                next = successivo(indice, "200");
                String c200_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c200_string = input_isbd.substring(init, next - 4);
                } else
                    c200_string = input_isbd.substring(init);
                c200 = costruisciC200(c200_string);
                if (c200.getA_200Count()==0) {
                    c200.addA_200("");
                }
                if (titolo.getCD_NATURA().equals("W"))
                    c200.setId1(Indicatore.valueOf("0"));
                else //if (titolo.getCd_natura().equals("M"))
                    c200.setId1(Indicatore.valueOf("1"));
            }
            init = posizione(indice, "205");
            if (init >= 0) {
                next = successivo(indice, "205");
                String c205_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c205_string = input_isbd.substring(init, next - 4);
                } else
                    c205_string = input_isbd.substring(init);
                c205 = costruisciC205(c205_string);
            }
            int n = 0;
            List v = new ArrayList();
            while ((init = posizione(indice, "206", n)) > 0) {
                next = successivo(indice, "206", n);
                String c206_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c206_string = input_isbd.substring(init, next - 4);
                } else
                    c206_string = input_isbd.substring(init);
                v.add(costruisciC206(c206_string));
                n++;
            }
            c206 = new C206[v.size()];
            for (int i = 0; i < c206.length; i++)
                c206[i] = (C206) v.get(i);
            init = posizione(indice, "207");
            if (init >= 0) {
                next = successivo(indice, "207");
                String c207_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c207_string = input_isbd.substring(init, next - 4);
                } else
                    c207_string = input_isbd.substring(init);
                c207 = costruisciC207(c207_string);
            }
            init = posizione(indice, "208");
            if (init >= 0) {
                next = successivo(indice, "208");
                String c208_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c208_string = input_isbd.substring(init, next - 4);
                } else
                    c208_string = input_isbd.substring(init);
                c208 = costruisciC208(c208_string);
            }
            init = posizione(indice, "210");
            if (init >= 0) {
                next = successivo(indice, "210");
                String c210_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c210_string = input_isbd.substring(init, next - 4);
                } else
                    c210_string = input_isbd.substring(init);
                c210 = costruisciC210(c210_string);
            } else
                c210 = new C210[0];
            init = posizione(indice, "215");
            if (init >= 0) {
                next = successivo(indice, "215");
                String c215_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c215_string = input_isbd.substring(init, next - 4);
                } else
                    c215_string = input_isbd.substring(init);
                c215 = costruisciC215(c215_string);
            }
            init = posizione(indice, "300");
            if (init >= 0) {
                next = successivo(indice, "300"); //Non so
                String c300_string;
                if (next >= 0) {
                    if (next >= 5 && input_isbd.charAt(next - 5) == '.')
                        next++;
                    c300_string = input_isbd.substring(init, next - 4);
                } else
                    c300_string = input_isbd.substring(init);
                //if (titolo.gettipoNota().equals("300")) ??
                c3xx = costruisciC3XX(c300_string);
            }
        } catch (Exception e) {
            log.error(
                "Errore nel calcolo isbd del titolo: "
                    + titolo.getBID()
                    + ", isbd: <----"
                    + titolo.getISBD()
                    + "----> indice:"
                    + titolo.getINDICE_ISBD());
            c200 = new C200();
            if (titolo.getCD_NATURA().equals("W"))
                c200.setId1(Indicatore.valueOf("0"));
            else //if (titolo.getCd_natura().equals("M"))
                c200.setId1(Indicatore.valueOf("1"));
            c200.addA_200(titolo.getISBD());
        }
    }
    protected C200 costruisciC200(String stringa) {
        //I manoscritti musicali vanno trattati diversamente: tutto in A200
        if (titolo.getCD_NATURA().equals("D")
                || (titolo.getTP_MATERIALE().equals("U")
                        && titolo.getTP_RECORD_UNI() != null && titolo
                        .getTP_RECORD_UNI().equalsIgnoreCase("D"))) {
            C200 c200 = new C200();
            c200.addA_200(stringa);
            return c200;
        } else if (antico)
            return costruisciC200Antico(stringa);
        else
            return costruisciC200NonAntico(stringa);
    }

    protected C200 costruisciC200NonAntico(String stringa) {
        C200 c200 = new C200();
        List a200 = new ArrayList();
        if (stringa.length() == 0)
            return c200;
        //Tralascio il primo carattere. (appartiene a a200)
        int n = 1;
        int start;
        String coda = null;
        while (n < stringa.length()) {
            coda = stringa.substring(n);
            // 31.01.2013 almaviva2: intervento interno su segnalazione di Maria Paola Giliberto; Biblioteca nazionale centrale Firenze
            // adeguamento a software dell'Indice per la costruzione della seconda chiave in presenza di parentesi quadre nella
            // prima parte del titolo
//            if (coda.startsWith("[")
              if (coda.startsWith(" = ")
                || coda.startsWith(" . ")
                || coda.startsWith(" : ")
                || coda.startsWith(
                    " / ")) ////               Tolto per volere di ICCU
            //                || (coda.startsWith(". ")
            //                    && !(n > 1 && stringa.charAt(n - 2) == ' ')
            //                    && !(Character.isDigit(stringa.charAt(n - 1)))
            //                    && !(stringa.charAt(n - 1) == '.'))
//                || coda.startsWith("="))
//                  || coda.startsWith(" = "))
// Modifica almaviva2 11.11.2009 - Bug Mantis 3265 - ripresa dalla correzione del Prot. di indice
// L'IF deve essere per " = "  e non per "="

                break;
            n++;
        }
        String a200String = stringa.substring(0, n);
        int end;
        if (titolo.getCD_NATURA().equals("B") == false) {
          end = a200String.indexOf(" ; ");
          while (end > 0) {
            a200.add(a200String.substring(0, end));
            a200String = a200String.substring(end + 3);
            end = a200String.indexOf(" ; ");
          }
        }
        a200.add(a200String);
        for (int i = 0; i < a200.size(); i++)
            c200.addA_200((String) a200.get(i));
        if (coda == null)
            return c200;
        while (coda.startsWith("[")) {
            end = coda.indexOf("]");
            if (end > 0) {
              //Se dopo la parentesi quadra c'è qualcosa metto tutto in un a200
                if (end < coda.length() -1) {
                  String[] a_200Array = new String[1];
                  a_200Array[0] = stringa;
                  c200.setA_200(a_200Array);
                  return c200;
                }
                c200.addB_200(coda.substring(1, end));
                n += end + 1;
                coda = stringa.substring(n);
            } else
                break;
        }
//      Modifica almaviva2 11.11.2009 - Bug Mantis 3265 - ripresa dalla correzione del Prot. di indice
//        while (coda.startsWith("=")) {
// Modiofcato il valore di implementazione di n ( n += 1; diventa n += 3;) BUG 3332  MANTIS almaviva2 19.11.2009
        while (coda.startsWith(" = ")) {
//            n += 1;
        	n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" = ")
                    || coda.startsWith(" : ")
                    || coda.startsWith(" . ")
                    || coda.startsWith(" / "))
                    break;
                n++;
            }
            c200.addD_200(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        while (coda.startsWith(" : ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" : ") || coda.startsWith(" / ") || coda.startsWith(" . "))
                    break;
                n++;
            }
            c200.addE_200(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        boolean serial = false;
        if (titolo != null && titolo.getCD_NATURA() != null)
            if (titolo.getCD_NATURA().equals("C") || titolo.getCD_NATURA().equals("S"))
                serial = true;
        boolean resp = false;
        while (coda.startsWith(" / ")) { //stringa = coda.substring(3);
            resp = true;
            //n = 0;
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" / ") || coda.startsWith(" ; ") || coda.startsWith(" . "))
                    break;
                n++;
            }
            c200.addF_200(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        while (resp && coda.startsWith(" ; ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" ; ") || coda.startsWith(" . "))
                    break;
                n++;
            }
            c200.addG_200(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        while (coda.startsWith(" . ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" . ")
                    //|| coda.startsWith(" : ") sarebbe un E, ma non esiste nel xsd del cf
                    || coda.startsWith(" / ")
//      Modifica almaviva2 11.11.2009 - Bug Mantis 3265 - ripresa dalla correzione del Prot. di indice
//                    || coda.startsWith("="))
                    || coda.startsWith(" = "))
                    break;
                n++;
            }
            Cf_200Type cf = new Cf_200Type();
            cf.setC_200(stringa.substring(start, n));
            c200.addCf_200(cf);
            coda = stringa.substring(n);
            while (coda.startsWith(" / ")) { //stringa = coda.substring(3);
                resp = true;
                //n = 0;
                n += 3;
                start = n;
                while (n < stringa.length()) {
                    coda = stringa.substring(n);
                    if (coda.startsWith(" / ") || coda.startsWith(" ; ") || coda.startsWith(" . "))
                        break;
                    n++;
                }
                cf.addF_200(stringa.substring(start, n));
                coda = stringa.substring(n);
            }
            while (resp && coda.startsWith(" ; ")) {
                n += 3;
                start = n;
                while (n < stringa.length()) {
                    coda = stringa.substring(n);
                    if (coda.startsWith(" ; ") || coda.startsWith(" . "))
                        break;
                    n++;
                }
                cf.addG_200(stringa.substring(start, n));
                coda = stringa.substring(n);
            }
        }
        return c200;
    }
    protected C200 costruisciC200Antico(String stringa) {
        C200 c200 = new C200();
        int f = stringa.indexOf(" / ");
        if (f >= 0) {
            c200.addF_200(stringa.substring(f + 3));
            c200.addA_200(stringa.substring(0, f));
        } else
            c200.addA_200(stringa);
        return c200;
    }

    protected C205 costruisciC205(String stringa) {
        if (antico)
            return costruisciC205Antico(stringa);
        else
            return costruisciC205NonAntico(stringa);
    }
    protected C205 costruisciC205Antico(String stringa) {
        C205 c205 = new C205();
        int f = stringa.indexOf(" / ");
        if (f >= 0) {
            c205.addF_205(stringa.substring(f));
            c205.setA_205(stringa.substring(0, f));
        } else
            c205.setA_205(stringa);
        return c205;
    }
    protected C205 costruisciC205NonAntico(String stringa) {
        C205 c205 = new C205();
        String coda = stringa;
        int n = 0, start;
        start = n;
        while (n < stringa.length()) {
            coda = stringa.substring(n);
            if (coda.startsWith(", ")
// Modifica almaviva2 15.01.2010 - BUG 3485  per l'area dell'edizione la sequenza " = " non è delimitatore si area; viene asteriscata
//                || coda.startsWith(" = ")
                || coda.startsWith(" / ")
                || coda.startsWith(" ; "))
                break;
            n++;
        }
        c205.setA_205(stringa.substring(start, n));
        coda = stringa.substring(n);
        while (coda.startsWith(", ")) {
            n += 2;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(", ")
                    || coda.startsWith(" = ")
                    || coda.startsWith(" / ")
                    || coda.startsWith(" ; "))
                    break;
                n++;
            }
            c205.addB_205(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        //Questo non esiste più
        while (coda.startsWith(" = ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" = ") || coda.startsWith(" / ") || coda.startsWith(" ; "))
                    break;
                n++;
            }
            //c205.setD_205(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        while (coda.startsWith(" / ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" / ") || coda.startsWith(" ; "))
                    break;
                n++;
            }
            c205.addF_205(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        while (coda.startsWith(" ; ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" ; "))
                    break;
                n++;
            }
            c205.addG_205(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        return c205;
    }
    /** Dovrebbe essere solo per cartografico, immagino che sia automatico */
    protected C206 costruisciC206(String stringa) {
        C206 c206 = new C206();
        c206.setA_206(stringa);
        return c206;
    }
    protected C207 costruisciC207(String stringa) {
        C207 c207 = new C207();
        List a207 = new ArrayList();
        int end = stringa.indexOf(" ; ");
        while (end > 0) {
            a207.add(stringa.substring(0, end));
            stringa = stringa.substring(end + 3);
            end = stringa.indexOf(" ; ");
        }
        a207.add(stringa);
        for (int i = 0; i < a207.size(); i++)
            c207.addA_207((String) a207.get(i));
        return c207;
    }
    protected C208 costruisciC208(String stringa) {
        C208 c208 = new C208();
        int end = stringa.indexOf(" = ");
        if (end > 0) {
            c208.setA_208(stringa.substring(0, end));
            int start = end + 3;
            end = stringa.indexOf(" = ", start);
            while (end > 0) {
                c208.addD_208(stringa.substring(start, end));
                start = end + 3;
                end = stringa.indexOf(" = ", start);
            }
            c208.addD_208(stringa.substring(start));
        } else
            c208.setA_208(stringa);
        return c208;
    }
    protected C210[] costruisciC210(String stringa) {
        C210[] c210 = new C210[1];
        c210[0] = new C210();
        boolean tipografo = false;
        while (stringa.endsWith(" ")) {
          stringa = stringa.substring(0, stringa.length()-1);
        }
        if (stringa.startsWith(": ")) {
          stringa = " " + stringa;
        }
        int chiusura = stringa.indexOf(")");
        if (chiusura == stringa.length() - 1) {
          tipografo = true;
        }
        int n = 0;
        int start;
        String coda = stringa;
        start = n;
        if (stringa.indexOf(", ") < 0
            && stringa.indexOf(" : ") < 0
            && stringa.indexOf(" ; ") < 0
            && stringa.indexOf("(") < 0) {
            c210[0] = estraiUnicoElemento210(stringa);
            return c210;
        }
        List ac210 = new ArrayList();
        while (n < stringa.length()) {
            coda = stringa.substring(n);
            if (((coda.startsWith(" (")
                || coda.startsWith("(")) && tipografo)
                || (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)))
                break;
            n++;
        }
        String a200String = stringa.substring(start, n);
        //if (contieneAnno(a200String)) {
        //    c210[0].addD_210(a200String);
        //} else {

//        	Modifica riportata dalla soluzione di Indicealmaviva4 bug mantis 3108
//    	    if (contieneAnno(a200String) == false) {
   		if (((contieneAnno(a200String) == false)) || (contieneAnno(a200String) == true) && (stringa.indexOf(", ") >= 0)){
            int end = a200String.indexOf(" ; ");
            if (end < 0)
                end = a200String.length();
            while (end > 0) {
                String t = a200String.substring(0, end);
                Ac_210Type ac = new Ac_210Type();
                int end2 = t.indexOf(" : ");
                if (end2 >= 0) {
                    if (end2 > 0) {
                      ac.addA_210(t.substring(0, end2));
                    }
                    start = end2 + 3;
                    end2 = t.indexOf(" : ", start);
                    while (end2 > 0) {
                        ac.addC_210(t.substring(start, end2));
                        start = end2 + 3;
                        end2 = t.indexOf(" : ", start);
                    }
                    ac.addC_210(t.substring(start));
                } else
                    ac.addA_210(t);
                ac210.add(ac);
                if (end + 3 <= a200String.length()) {
                    a200String = a200String.substring(end + 3);
                    end = a200String.indexOf(" ; ");
                    if (end < 0)
                        end = a200String.length();
                } else
                    end = -1;
            }
            for (int i = 0; i < ac210.size(); i++)
                c210[0].addAc_210((Ac_210Type) ac210.get(i));
          while (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)) {
              n += 2;
              start = n;
              while (n < stringa.length()) {
                  coda = stringa.substring(n);
                  if (((coda.startsWith(" (") || coda.startsWith("(")) && tipografo) || (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)))
                      break;
                  n++;
              }
              c210[0].addD_210(stringa.substring(start, n));
              coda = stringa.substring(n);
          }
          // Inizio Manutenzione BUG MANTIS (esercizio) 5135 (ripreso da correttiva bug mantis 4452 di protocollo Indice)
          // correzione della prospettazione dell'area di pubblicazione (veniva inserita una virgola che precede il luogo)
		} else if ((contieneAnno(a200String) == true)) {
			int end = a200String.indexOf(" ; ");
			if (end < 0)
				end = a200String.length();
			while (end > 0) {
				String t = a200String.substring(0, end);
				Ac_210Type ac = new Ac_210Type();
				int end2 = t.indexOf(" : ");
				if (end2 >= 0) {
					if (end2 > 0) {
					  ac.addA_210(t.substring(0, end2));
					}
					start = end2 + 3;
					end2 = t.indexOf(" : ", start);
					while (end2 > 0) {
						ac.addC_210(t.substring(start, end2));
						start = end2 + 3;
						end2 = t.indexOf(" : ", start);
					}
					ac.addC_210(t.substring(start));
				} else
					ac.addA_210(t);
				ac210.add(ac);
				if (end + 3 <= a200String.length()) {
					a200String = a200String.substring(end + 3);
					end = a200String.indexOf(" ; ");
					if (end < 0)
						end = a200String.length();
				} else
					end = -1;
			}
			for (int i = 0; i < ac210.size(); i++)
				c210[0].addAc_210((Ac_210Type) ac210.get(i));
		  while (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)) {
			  n += 2;
			  start = n;
			  while (n < stringa.length()) {
				  coda = stringa.substring(n);
				  if (((coda.startsWith(" (") || coda.startsWith("(")) && tipografo) || (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)))
					  break;
				  n++;
			  }
			  c210[0].addD_210(stringa.substring(start, n));
			  coda = stringa.substring(n);
		  }
          // Fine Manutenzione BUG MANTIS (esercizio) 5135 (ripreso da correttiva bug mantis 4452 di protocollo Indice)


        } else {
          while (n < stringa.length()) {
              coda = stringa.substring(n);
              if (((coda.startsWith(" (") || coda.startsWith("(")) && tipografo) || (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)))
                  break;
              n++;
          }
          c210[0].addD_210(stringa.substring(start, n));
          coda = stringa.substring(n);
        }
        if ((coda.startsWith(" (") || coda.startsWith("(")) && tipografo) {
            if (coda.startsWith(" (")) {
                n += 2;
            } else {
                n += 1;
            }
            int temp = stringa.indexOf(')', n);
            if (temp >= 0)
                stringa = stringa.substring(0, temp);
            coda = stringa.substring(n);
            boolean primo = true;
            while (primo || coda.startsWith(" ; ")) {
                if (primo)
                    primo = false;
                else
                    n += 3;
                start = n;
                while (n < stringa.length()) {
                    coda = stringa.substring(n);
                    if (coda.startsWith(" ; ")
                        || coda.startsWith(" : ")
                        || (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)))
                        break;
                    n++;
                }
                if (contieneAnno(stringa.substring(start, n)) == false) {
                  c210[0].addE_210(stringa.substring(start, n));
                } else {
                  c210[0].addH_210(stringa.substring(start, n));
                }
                coda = stringa.substring(n);
            }
        }
        while (coda.startsWith(" : ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" : ") || (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)))
                    break;
                n++;
            }
            c210[0].addG_210(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        while (coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)) {
            n += 2;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if ((coda.startsWith(", ") && isAnnoBeforeSeparatore(coda)))
                    break;
                n++;
            }
            c210[0].addH_210(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        return c210;
    }
    private boolean isAnnoBeforeSeparatore(String stringa) {
        int n = 2;
        String coda = "";
        while (n < stringa.length()) {
            coda = stringa.substring(n);
            if (coda.startsWith(" ; ") || coda.startsWith(" : ") || coda.startsWith(", "))
                break;
            n++;
        }
        return contieneAnno(stringa.substring(0, n));
    }
    /**
     * Verifica se esiste una sequenza consecutiva di 4 caratteri numerici, il cui
     * valore sia > 1450.
     * Su richiesta di ICCU modificato: controlla che esista un numero di 2 cifre.
     * @return
     */
    private boolean contieneAnno(String stringa) {
        int numeri = 0;
    int x = 0;
        for (int i = 0; i < stringa.length(); i++) {
          //MANTIS 2206 commentato queste righe di codice perchè non hanno senso
          //soprattutto la frase: "Se ho una X lo considero un numero romano"
          //perchè se trova una parola tipo "Xunta" (BID PUV0572091) va in tilt!!!
//          if (stringa.charAt(i) == 'X') {
//              //Se ho una X lo considero un numero romano
//              return true;
//            }
            if (Character.isDigit(stringa.charAt(i))) {
                numeri++;
                if (numeri == 2) {
           //MANTIS 1750 Aggiunta if x > -1. se non c'è il trattino da errore
                  x = i;
                  x-=2;
          if(x > -1) {
          //Per evitare che i numeri civici vengano considerati anni scriviamo
                  //questa boiata.
            if (stringa.charAt(x) == '-') {
            //Solo per il caso 'Il sole -24 ore'
            return false;
            }
            while (x>=0) {
            char c = stringa.charAt(x);
            if (Character.isLetterOrDigit(c)) {
              break;
            } else {
              x--;
            }
            }
            while (x>=0) {
            char c = stringa.charAt(x);
            if (Character.isLetterOrDigit(c)) {
              x--;
            } else {
              break;
            }
            }
            x++;
            char c = stringa.charAt(x);
            if (Character.isLetterOrDigit(c)) {
            if (c == 'n' || c=='N') {
              return false;
            }
            }
            //fine boiata
            return true;
          }
                }
            } else
                numeri = 0;
        }
        //Mantis 1750 - 1832
        //Se ho una stringa vuota (length = 0) allora ritorno false cioè la
        //stringa non contiene alcun anno altrimenti faccio il controllo
        if (stringa.length() != 0) {
      if (stringa.length() == numeri) {
        return true;
      } else {
        return false;
      }
        } else
      return false;
    }
    /**
     * Se ci sono le parentesi è h (con numrei) o e (senza numeri)
     * senza parentesi è d (con numeri) o a (senza numeri)
     * @param stringa
     * @return
     */
    private C210 estraiUnicoElemento210(String stringa) {
        C210 c210 = new C210();
        boolean numero = contieneAnno(stringa);
        if (stringa.trim().startsWith("(") && stringa.endsWith(")")) {
            if (numero)
                c210.addH_210(stringa);
            else
                c210.addE_210(stringa);
        } else {
            if (numero)
                c210.addD_210(stringa);
            else {
                Ac_210Type ac = new Ac_210Type();
                ac.addA_210(stringa);
                c210.addAc_210(ac);
            }
        }
        return c210;
    }
    protected C215 costruisciC215(String stringa) {
        C215 c215 = new C215();
        int n = 0;
        int start;
        String coda = stringa;
        start = n;
        //List a215 = new ArrayList();
        while (n < stringa.length()) {
            coda = stringa.substring(n);
            if (coda.startsWith(" : ") || coda.startsWith(" + ") || coda.startsWith(" ; "))
                break;
            n++;
        }
        String a215String = stringa.substring(start, n);
        c215.addA_215(a215String);
        //int end = a215String.indexOf(" ; ");
        //while (end > 0) {
        //    a215.add(a215String.substring(0, end));
        //    a215String = a215String.substring(end + 3);
        //    end = a215String.indexOf(" ; ");
        //}
        //a215.add(a215String);
        //for (int i = 0; i < a215.size(); i++)
        //    c215.addA_215((String) a215.get(i));
        //}
        if (coda.startsWith(" : ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" ; ") || coda.startsWith(" + "))
                    break;
                n++;
            }
            c215.setC_215(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        if (coda.startsWith(" ; ")) {
            n += 3;
            start = n;
            while (n < stringa.length()) {
                coda = stringa.substring(n);
                if (coda.startsWith(" + "))
                    break;
                n++;
            }
            c215.addD_215(stringa.substring(start, n));
            coda = stringa.substring(n);
        }
        if (coda.startsWith(" + ")) {
            n += 3;
            c215.addE_215(stringa.substring(n));
        }
        return c215;
    }
    protected A230 costruisciC230(String stringa) {
        A230 c230 = new A230();
        c230.setA_230(stringa);
        return c230;
    }
    protected C3XX[] costruisciC3XX(String stringa) {
        List a300 = new ArrayList();
        int end = stringa.indexOf(". - ");
        while (end > 0) {
            a300.add(stringa.substring(0, end));
            stringa = stringa.substring(end + 4);
            end = stringa.indexOf(". - ");
        }
        a300.add(stringa);
        C3XX[] c300 = new C3XX[a300.size()];
        for (int i = 0; i < c300.length; i++) {
            c300[i] = new C3XX();
            c300[i].setA_3XX((String) a300.get(i));
            c300[i].setTipoNota(SbnTipoNota.valueOf("300"));
        }
        return c300;
    }
    /**
     * Returns the c200.
     * @return C200
     */
    public C200 getC200() {
        return c200;
    }
    /**
     * Returns the c200 limitato a num_car caratteri.
     * @return C200
     */
    public C200 getC200(int num_car) {
        int counter = 0;
        C200 nuovo = new C200();
        nuovo.setId1(c200.getId1());
        Enumeration en = c200.enumerateA_200();
        String temp;
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addA_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addA_200(temp);
        }
        en = c200.enumerateB_200();
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addB_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addB_200(temp);
        }
        en = c200.enumerateD_200();
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addD_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addD_200(temp);
        }
        en = c200.enumerateE_200();
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addE_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addE_200(temp);
        }
        en = c200.enumerateF_200();
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addF_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addF_200(temp);
        }
        en = c200.enumerateG_200();
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addG_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addG_200(temp);
        }
        en = c200.enumerateCf_200();
        while (en.hasMoreElements()) {
            Cf_200Type cf = (Cf_200Type) en.nextElement();
            Cf_200Type cfn = new Cf_200Type();
            nuovo.addCf_200(cfn);
            temp = cf.getC_200();
            if (counter + temp.length() > num_car) {
                cfn.setC_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            cfn.setC_200(temp);
            for (int i = 0; i < cf.getF_200Count(); i++) {
                temp = cf.getF_200(i);
                if (counter + temp.length() > num_car) {
                    cfn.addF_200(temp.substring(0, num_car - counter));
                    return nuovo;
                }
                counter += temp.length();
                cfn.addF_200(temp);
            }
            for (int i = 0; i < cf.getG_200Count(); i++) {
                temp = cf.getG_200(i);
                if (counter + temp.length() > num_car) {
                    cfn.addG_200(temp.substring(0, num_car - counter));
                    return nuovo;
                }
                counter += temp.length();
                cfn.addG_200(temp);
            }
        }
        en = c200.enumerateH_200();
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addH_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addH_200(temp);
        }
        en = c200.enumerateI_200();
        while (en.hasMoreElements()) {
            temp = (String) en.nextElement();
            if (counter + temp.length() > num_car) {
                nuovo.addI_200(temp.substring(0, num_car - counter));
                return nuovo;
            }
            counter += temp.length();
            nuovo.addI_200(temp);
        }
        return nuovo;
    }
    /**
     * Returns the c205.
     * @return C205
     */
    public C205 getC205() {
        return c205;
    }
    public C206[] getC206() {
      if (c206 == null)
        c206 = new C206[0];
        return c206;
    }
    /**
     * Returns the c207.
     * @return C207
     */
    public C207 getC207() {
        return c207;
    }
    public C208 getC208() {
        return c208;
    }
    /**
     * Returns the c210.
     * @return C210
     */
    public C210[] getC210() {
        if (c210 == null)
            c210 = new C210[0];
        return c210;
    }
    public C210[] getC210(int num_car) {
        List nuovo = estraiC210(num_car);
        C210[] ret = new C210[nuovo.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (C210) nuovo.get(i);
        }
        return ret;
    }
    protected List estraiC210(int num_car) {
        int counter = 0;
        int caratteri = 0;
        int l = 0;
        List v = new ArrayList();
        if (c210 != null) {
            for (; counter < c210.length; counter++) {
                C210 el = new C210();
                v.add(el);
                for (int c = 0; c < c210[counter].getAc_210Count(); c++) {
                    Ac_210Type ac = new Ac_210Type();
                    el.addAc_210(ac);
                    for ( int s = 0;c210[counter].getAc_210(c).getA_210Count() > s;s++) {
                      l = c210[counter].getAc_210(c).getA_210(s).length();
                      if (caratteri + l > num_car) {
                          ac.addA_210(
                              c210[counter].getAc_210(c).getA_210(s).substring(0, (num_car - caratteri)));
                          return v;
                      }
                      ac.addA_210(c210[counter].getAc_210(c).getA_210(s));
                      caratteri += l;
                    }
                    for (int s=0;c210[counter].getAc_210(c).getC_210Count() > s;s++) {
                        l = c210[counter].getAc_210(c).getC_210(s).length();
                        if (caratteri + l > num_car) {
                            ac.addC_210(
                                c210[counter].getAc_210(c).getC_210(s).substring(0, (num_car - caratteri)));
                            return v;
                        }
                        ac.addC_210(c210[counter].getAc_210(c).getC_210(s));
                        caratteri += l;
                    }
                }
                for (int c = 0; c < c210[counter].getD_210Count(); c++) {
                    l = c210[counter].getD_210(c).length();
                    if (caratteri + l > num_car) {
                        el.addD_210(c210[counter].getD_210(c).substring(0, (num_car - caratteri)));
                        return v;
                    }
                    caratteri += l;
                    el.addD_210(c210[counter].getD_210(c));
                }
                for (int c = 0; c < c210[counter].getE_210Count(); c++) {
                    l = c210[counter].getE_210(c).length();
                    if (caratteri + l > num_car) {
                        el.addE_210(c210[counter].getE_210(c).substring(0, (num_car - caratteri)));
                        return v;
                    }
                    caratteri += l;
                    el.addE_210(c210[counter].getE_210(c));
                }
                for (int c = 0; c < c210[counter].getG_210Count(); c++) {
                    l = c210[counter].getG_210(c).length();
                    if (caratteri + l > num_car) {
                        el.addG_210(c210[counter].getG_210(c).substring(0, (num_car - caratteri)));
                        return v;
                    }
                    caratteri += l;
                    el.addG_210(c210[counter].getG_210(c));
                }
                for (int c = 0; c < c210[counter].getH_210Count(); c++) {
                    l = c210[counter].getH_210(c).length();
                    if (caratteri + l > num_car) {
                        el.addH_210(c210[counter].getH_210(c).substring(0, (num_car - caratteri)));
                        return v;
                    }
                    caratteri += l;
                    el.addH_210(c210[counter].getH_210(c));
                }
            }
        }
        return v;
    }
    /**
     * Returns the c215.
     * @return C215
     */
    public C215 getC215() {
        return c215;
    }
    public A230 getA230() {
        return c230;
    }
    /**
     * Returns the c3xx.
     * @return C3XX
     */
    public C3XX[] getC3xx() {
        if (c3xx == null)
            return new C3XX[0];
        return c3xx;
    }
}
