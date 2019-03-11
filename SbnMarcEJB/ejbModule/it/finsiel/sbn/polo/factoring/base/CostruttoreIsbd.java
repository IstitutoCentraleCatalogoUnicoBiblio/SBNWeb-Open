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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ElencoForme;
import it.finsiel.sbn.polo.factoring.util.ElencoFormeU;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C205;
import it.iccu.sbn.ejb.model.unimarcmodel.C206;
import it.iccu.sbn.ejb.model.unimarcmodel.C207;
import it.iccu.sbn.ejb.model.unimarcmodel.C208;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C215;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Classe CostruttoreIsbd
 * <p>
 * Crea dagli oggetti Castor una stringa che contiene tutte le informazioni
 * concatenando gli elementi con la sintassi SBN.
 * </p>
 * @author Akros Informatica s.r.l.
 * @author Ragazzini Taymer
 *
 * @version 28-mag-03
 */
public class CostruttoreIsbd extends Isbd {
    String c200_string = null;
    String c205_string = null;
    String c206_string = null;
    String c207_string = null;
    String c208_string = null;
    String c210_string = null;
    String c215_string = null;
    String c230_string = null;
    String c300_string = null;

    String isbd = null;
    String indice_isbd = null;

	Map<String, String> hmVoci = new HashMap<String, String>();

	int weigths[] = new int [50];
	String description[] = new String[50];
	int elements = 0;


	final int RICHIESTA_STRUMENTI = 1;
	final int RICHIESTA_VOCI = 2;
	final int RICHIESTA_VOCI_STRUMENTI = 3;


    protected void verificaDueAsterischi(String stringa) throws EccezioneSbnDiagnostico {
        int counter = 0;
        int tmp = -1;
        while ((tmp = stringa.indexOf("*", tmp + 1)) >= 0)
            counter++;
        if (counter == 0)
            throw new EccezioneSbnDiagnostico(3059, "Asterisco obbligatorio");
        if (stringa.indexOf(" : ") > 0) {
            if (stringa.indexOf('*') > stringa.indexOf(" : "))
                throw new EccezioneSbnDiagnostico(
                    3078,
                    "Asterisco mancante nel titolo proprio");
        }
    }

    /**
     * METODO GENERALE PER LA DEFINIZIONE DEL CAMPO ISBD
     * Invoca i metodi necessari per la costruzione delle stringhe,
     * quindi setta i dati nella tavola.
     */
    public void definisciISBD(
        Tb_titolo tavola,
        C200 c200,
        C205 c205,
        C206 c206[],
        C207 c207,
        C208 c208,
        C210 c210[],
        C215 c215,
        A230 c230,
        C3XX[] c300,
        boolean antico,
        String lingua)
        throws EccezioneSbnDiagnostico { //la lingua è necessaria per la stop list degli articoli
        titolo = tavola;
        calcolaIsbd(
            tavola.getCD_NATURA(),
            c200,
            c205,
            c206,
            c207,
            c208,
            c210,
            c215,
            c230,
            c300,
            antico,
            lingua);
        if (!"W".equals(tavola.getCD_NATURA())) {
            verificaDueAsterischi(isbd);
            tavola.setISBD(aggiungiAsterisco(isbd));
        } else {
            tavola.setISBD(isbd);
        }
        tavola.setINDICE_ISBD(indice_isbd);
    }

    /**
    * METODO GENERALE PER LA DEFINIZIONE DEL CAMPO ISBD
    * Invoca i metodi necessari per la costruzione delle stringhe,
    * quindi setta i dati nella tavola.
    */
    public void definisciISBD(Tb_titolo titolo, DatiDocType doc) throws EccezioneSbnDiagnostico {
        C206[] new_t206 = null;
        C208 new_t208 = null;
        boolean isAntico = false;
        if (doc.getTipoMateriale() != null &&
        		doc.getTipoMateriale().getType() == SbnMateriale.valueOf("E").getType())
            isAntico = true;
        new_t208 = doc.getT208();
        new_t206 = doc.getT206();
        definisciISBD(
            titolo,
            doc.getT200(),
            doc.getT205(),
            new_t206,
            doc.getT207(),
            new_t208,
            doc.getT210(),
            doc.getT215(),
            null,
            doc.getT3XX(),
            isAntico,
            doc.getT101() == null ? null : doc.getT101().getA_101(0));
    }

    /** Solo per titolo uniforme musicale si compone un isbd diverso */
    public void definisciISBDtitUni(Tb_titolo titolo, TitoloUniformeType datiEl)
        throws EccezioneSbnDiagnostico {

    	 // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
        // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
			if (datiEl.getT231() != null) { //si gestisce opera 231
				verificaDueAsterischi(datiEl.getT231().getA_231());
				titolo.setISBD(aggiungiAsterisco(datiEl.getT231().getA_231()));
				titolo.setINDICE_ISBD("200-0001;");
		        titolo.setCD_NATURA("A");
			} else {
				if (datiEl.getNaturaTU() != null && datiEl.getNaturaTU().equals("V")
						&& datiEl.getT431() != null) { //si gestisce variante opera 431
			        titolo.setCD_NATURA("V");
					verificaDueAsterischi(datiEl.getT431().getA_431());
					titolo.setISBD(aggiungiAsterisco(datiEl.getT431().getA_431()));
					titolo.setINDICE_ISBD("200-0001;");
				}
			}
	}

    /** Solo per titolo uniforme musicale si compone un isbd diverso */
    public void definisciISBDtitUniMusicale(Tb_titolo titolo, TitoloUniformeMusicaType datiEl)
        throws EccezioneSbnDiagnostico {
        if (datiEl.getT929() != null) {
            A929 a929 = datiEl.getT929();
            A928 a928 = datiEl.getT928();
            definisciISBDtitUniMusicale(
                titolo,
                true,
                a929.getA_929(),
                a929.getB_929(),
                a929.getC_929(),
                a929.getE_929(),
                a929.getF_929(),
                a929.getG_929(),
                a929.getH_929(),
                a929.getI_929(),
                a928.getA_928(),
                a928.getB_928(),
                a928.getC_928());
            //Setto l'A230 con l'isbd; le chiavi verranno calcolate con questo.
            A230 a230 = new A230();
            a230.setA_230(isbd);
            datiEl.setT230(a230);
        }
    }

    public void definisciISBDtitUniMusicale(
        Tb_titolo titolo,
        boolean unimarc,
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


//almaviva4 evolutiva TUM almaviva2 REPLICATA il 14.05.2015
//        if (h_929 != null) {
//            h_929 = rimuoviAsterisco(h_929);
//            isbd = concatena(isbd, ". ");
//            indice_isbd += elementoIndiceIsbd("I", isbd.length());
//            isbd = concatena(isbd, h_929);
//        }
//almaviva4 evolutiva TUM almaviva2 REPLICATA il 14.05.2015

        if (b_928 != null) {
            String b928 = preparaB928(a_928, b_928, c_928, g_929, unimarc);
            if (b928 != null) {
                indice_isbd += elementoIndiceIsbd("R", isbd.length());
                isbd = concatena(isbd, b928);
            }
        }
        boolean numero = false;
        if (a_929 != null) {
            a_929 = pulisciNumero(a_929);
            if (a_929.length() > 0) {
            	//almaviva4 evolutiva TUM almaviva2 REPLICATA il 14.05.2015  isbd = concatena(isbd, ". N.");
            	//bug mantis 5219
				indice_isbd += elementoIndiceIsbd("S", isbd.length());
				numero = true;
				if (Character.isDigit(a_929.charAt(0))){
					isbd = concatena(isbd, ", n. ");
					isbd = concatena(isbd, a_929);
				} else {
					isbd = concatena(isbd, ", ");
					isbd = concatena(isbd, a_929);
				}
			}
		}
//bug mantis 5219 fine


        if (c_929 != null) {
//almaviva4 evolutiva TUM almaviva2 REPLICATA il 14.05.2015  isbd = concatena(isbd, ". ");
			isbd = concatena(isbd, ", ");
			if (!numero)
				indice_isbd += elementoIndiceIsbd("S", isbd.length());
			isbd = concatena(isbd, c_929);
			numero = true;
		}
        if (b_929 != null && c_929 == null) {
			b_929 = pulisciNumero(b_929);
			if (b_929.length() > 0) {
//almaviva4 evolutiva TUM almaviva2 REPLICATA il 14.05.2015  isbd = concatena(isbd, ". Op.");
				isbd = concatena(isbd, ", op. ");
				if (!numero)
					indice_isbd += elementoIndiceIsbd("S", isbd.length());
				isbd = concatena(isbd, b_929);
			}
		}
        if (e_929 != null && str) {
            String ton;
            if (unimarc)
                //ton = Decodificatore.getDs_tabellaFromUnimarc("Tb_composizione", "cd_tonalita", e_929);
            	ton = Decodificatore.getDs_tabella("Tb_composizione", "cd_tonalita", e_929);
            else
                ton = Decodificatore.getDs_tabella("Tb_composizione", "cd_tonalita", e_929);
            if (ton != null) {
//almaviva4 evolutiva TUM almaviva2 REPLICATA il 14.05.2015  isbd = concatena(isbd, ". ");
				isbd = concatena(isbd, ", ");
				indice_isbd += elementoIndiceIsbd("U", isbd.length());
				isbd = concatena(isbd, ton);
			}
        }
        if (i_929 != null) {
			i_929 = rimuoviAsterisco(i_929);
			isbd = concatena(isbd, ". ");
			indice_isbd += elementoIndiceIsbd("I", isbd.length());
//almaviva4 evolutiva TUM  almaviva2 REPLICATA il 14.05.2015
            i_929 = "<" + i_929 + ">";
//almaviva4 fine evolutiva TUM  almaviva2 REPLICATA il 14.05.2015
			isbd = concatena(isbd, i_929);
		}

//almaviva4 evolutiva TUM   almaviva2 REPLICATA il 14.05.2015
		  if (h_929 != null) {
			  h_929 = rimuoviAsterisco(h_929);
			  isbd = concatena(isbd, ". ");
			  indice_isbd += elementoIndiceIsbd("I", isbd.length());
			  isbd = concatena(isbd, h_929);
		  }
//almaviva4 fine evolutiva TUM  almaviva2 REPLICATA il 14.05.2015

        verificaDueAsterischi(isbd);
        titolo.setINDICE_ISBD(indice_isbd);
        titolo.setISBD(aggiungiAsterisco(isbd));
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

    private String pulisciNumero(String stringa) {
      int i = 0;
      for (; i < stringa.length(); i++) {
        if (Character.isDigit(stringa.charAt(i)))
          break;
      }
      return stringa.substring(i);
    }

    public void calcolaIsbd(
        C200 c200,
        C205 c205,
        C206 c206[],
        C207 c207,
        C208 c208,
        C210 c210[],
        C215 c215,
        A230 c230,
        C3XX[] c300,
        boolean antico,
        String lingua)
        throws EccezioneSbnDiagnostico {
        calcolaIsbd(null, c200, c205, c206, c207, c208, c210, c215, c230, c300, antico, lingua);
    }

    public void calcolaIsbd(
        String natura,
        C200 c200,
        C205 c205,
        C206 c206[],
        C207 c207,
        C208 c208,
        C210 c210[],
        C215 c215,
        A230 c230,
        C3XX[] c300,
        boolean antico,
        String lingua)
        throws EccezioneSbnDiagnostico { //la lingua è necessaria per la stop list degli articoli
        this.lingua = lingua;
        this.antico = antico;
        creaC200(c200, natura);
        creaC205(c205);
        creaC206(c206);
        creaC207(c207);
        creaC208(c208);
        creaC210(c210);
        creaC215(c215);
        creaC230(c230);
        creaC300(c300);
        indice_isbd = "200-0001;";
        if (!c205_string.equals("")) {
            if (conflittopunti(c200_string, c205_string))
                indice_isbd += elementoIndiceIsbd("205", c200_string.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("205", c200_string.length() + 5);
        }
        isbd = concatena(c200_string, c205_string);
        if (!c206_string.equals("")) {
            int n = 0;
            if (conflittopunti(isbd, c206_string))
                indice_isbd += elementoIndiceIsbd("206", isbd.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("206", isbd.length() + 5);
            while ((n = c206_string.indexOf(". - ", n + 1)) >= 0)
                indice_isbd += elementoIndiceIsbd("206", isbd.length() + n + 5);
        }
        isbd = concatena(isbd, c206_string);
        if (!c207_string.equals("")) {
            if (conflittopunti(isbd, c207_string))
                indice_isbd += elementoIndiceIsbd("207", isbd.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("207", isbd.length() + 5);
        }
        isbd = concatena(isbd, c207_string);
        if (!c208_string.equals("")) {
            if (conflittopunti(isbd, c208_string))
                indice_isbd += elementoIndiceIsbd("208", isbd.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("208", isbd.length() + 5);
        }
        isbd = concatena(isbd, c208_string);
        if (!c210_string.equals("")) {
            if (conflittopunti(isbd, c210_string))
                indice_isbd += elementoIndiceIsbd("210", isbd.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("210", isbd.length() + 5);
        }
        isbd = concatena(isbd, c210_string);
        if (!c215_string.equals("")) {
            if (conflittopunti(isbd, c215_string))
                indice_isbd += elementoIndiceIsbd("215", isbd.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("215", isbd.length() + 5);
        }
        isbd = concatena(isbd, c215_string);
        if (!c230_string.equals("")) {
            if (conflittopunti(isbd, c230_string))
                indice_isbd += elementoIndiceIsbd("230", isbd.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("230", isbd.length() + 5);
        }
        isbd = concatena(isbd, c230_string);
        if (!c300_string.equals("")) {
            if (conflittopunti(isbd, c300_string))
                indice_isbd += elementoIndiceIsbd("300", isbd.length() + 4);
            else
                indice_isbd += elementoIndiceIsbd("300", isbd.length() + 5);
        }
        isbd = concatena(isbd, c300_string);

        //almaviva5_20120614
        if (isbd.length() > ResourceLoader.getPropertyInteger("ISBD_MAX_LEN"))
        	throw new EccezioneSbnDiagnostico(3288); //len eccessiva
    }

    /**
     * Crea la stringa che rappresenta l'elemento di tipo C200
     */
    protected void creaC200(C200 c200, String cd_natura) {
        c200_string = "";
        if (c200 == null)
            return;
        // Maggio 2015: almaviva2 si riporta la manutenzione sul software del protocollo
        // di Indice //bug 5895
        // if (!antico) {
            if (c200.getA_200Count() > 0) {
                if (c200.getId1().getType() != Indicatore.valueOf("0").getType() && !"W".equals(cd_natura)) {

                	// Inizio intervento almaviva2 04.11.2011 BUG MANTIS 4702 (esercizio)
                	// La situazione trovata è quella sotto che è stata asteriscata che comporta l'inserimento di un astreisco su ogni elemento
                	// di titolo c200.getA_200(); il comportamento sembra invece essere quello che era effettuato prima della modifica del 2007
                	// e che tuttora è effettuato in Indice e che a questo punto viene ripristinato !!!!!!!!
                	// INIZIO PARTE VECCHIA CHE CORREGGEVA L'INDICE NEL 2007
//                    //c200_string = concatena(c200_string, aggiungiAsteriscoA200(c200.getA_200()), "", " ; ", "");
//                	c200_string = concatena(c200_string, aggiungiAsterisco(c200.getA_200()), "", " ; ", "");
            		//  FINE PARTE VECCHIA CHE CORREGGEVA L'INDICE NEL 2007



                	// MODIFICA CHE RIPRISTINA IL VECCHIO COMPORTAMENTO (UGUALE A QUELLO DELL'INDICE)
                	c200_string = concatena(c200_string, aggiungiAsteriscoA200(c200.getA_200()), "", " ; ", "");


                	// Fine intervento almaviva2 04.11.2011 BUG MANTIS 4702 (esercizio)

                } else {
                    c200_string = concatena(c200_string, c200.getA_200(), "", " ; ", "");
                }
            }
            if (c200.getB_200Count() > 0)
                c200_string = concatena(c200_string, c200.getB_200(), " [", "[", "]");
            if (c200.getD_200Count() > 0)
                c200_string = concatena(c200_string, c200.getD_200(), " = ", " = ", "");
            if (c200.getE_200Count() > 0)
                c200_string = concatena(c200_string, c200.getE_200(), " : ", " : ", "");
            if (c200.getF_200Count() > 0)
                c200_string = concatena(c200_string, c200.getF_200(), " / ", " / ", "");
            if (c200.getG_200Count() > 0)
                c200_string = concatena(c200_string, c200.getG_200(), " ; ", " ; ", "");
            for (int i = 0; i<c200.getCf_200Count(); i++) {
                c200_string = concatena(c200_string, c200.getCf_200(i).getC_200(), " . ", "");
                if (c200.getCf_200(i).getF_200Count()>0)
                    c200_string = concatena(c200_string, c200.getCf_200(i).getF_200(), " / ", " / ", "");
                if (c200.getCf_200(i).getG_200Count()>0)
                    c200_string = concatena(c200_string, c200.getCf_200(i).getG_200(), " ; ", " ; ", "");
            }
            if (c200.getH_200Count() > 0)
                c200_string = concatena(c200_string, c200.getH_200(), ". ", ", ", "");
            if (c200.getI_200Count() > 0)
                c200_string =
                    concatena(
                        c200_string,
                        c200.getI_200(),
                        c200.getH_200().length == 0 ? ". " : ", ",
                        ", ",
                        "");
//        } else {
//            if (c200.getA_200Count() > 0)
//                c200_string = concatena(c200_string, c200.getA_200(), "", "", "");
//            if (c200.getB_200Count() > 0)
//                c200_string = concatena(c200_string, c200.getB_200(), "", "", "");
//            if (c200.getD_200Count() > 0)
//                c200_string = concatena(c200_string, c200.getD_200(), "", "", "");
//            if (c200.getE_200Count() > 0)
//                c200_string = concatena(c200_string, c200.getE_200(), "", "", "");
//            if (c200.getF_200Count() > 0)
//                c200_string = concatena(c200_string, c200.getF_200(), "", "", "");
//            for (int i = 0; i<c200.getCf_200Count(); i++) {
//                c200_string = concatena(c200_string, c200.getCf_200(i).getC_200(), " . ", "");
//                if (c200.getCf_200(i).getF_200Count()>0)
//                    c200_string = concatena(c200_string, c200.getCf_200(i).getF_200(), " / ", " / ", "");
//                if (c200.getCf_200(i).getG_200Count()>0)
//                    c200_string = concatena(c200_string, c200.getCf_200(i).getG_200(), " ; ", " ; ", "");
//            }
//        }
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C205
     */
    protected void creaC205(C205 c205) {
        c205_string = "";
        if (c205 == null)
            return;
        if (!antico) {
            if (c205.getA_205() != null)
                c205_string = concatena(c205_string, c205.getA_205(), ". - ", "");
            if (c205.getB_205Count() > 0)
                c205_string = concatena(c205_string, c205.getB_205(), ", ", ", ", "");
            //if (c205.getD_205() != null)
            //    c205_string = concatena(c205_string,c205.getD_205(), " = ", "");
            if (c205.getF_205Count() > 0)
                c205_string = concatena(c205_string, c205.getF_205(), " / ", " / ", "");
            if (c205.getG_205Count() > 0)
                c205_string = concatena(c205_string, c205.getG_205(), " ; ", " ; ", "");
        } else {
            if (c205.getA_205() != null)
                c205_string = concatena(c205_string, c205.getA_205(), ". - ", "");
            if (c205.getB_205Count() > 0)
                c205_string = concatena(c205_string, c205.getB_205(), "", "", "");
            //if (c205.getD_205() != null)
            //    c205_string = concatena(c205.getD_205(), "", "");
            if (c205.getF_205Count() > 0)
                c205_string = concatena(c205_string, c205.getF_205(), " / ", " / ", "");
        }
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C206
     */
    protected void creaC206(C206[] c206) {
        c206_string = "";
        if (c206 == null)
            return;
        for (int i = 0; i < c206.length; i++)
            c206_string = concatena(c206_string, c206[i].getA_206(), ". - ", "");
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C207
     */
    protected void creaC207(C207 c207) {
        c207_string = "";
        if (c207 == null)
            return;
        c207_string = concatena(c207_string, c207.getA_207(), ". - ", " ; ", "");
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C208
     */
    protected void creaC208(C208 c208) {
        c208_string = "";
        if (c208 == null)
            return;
        c208_string = concatena(c208_string, c208.getA_208(), ". - ", "");
        if (c208.getD_208Count() > 0)
            c208_string = concatena(c208_string, c208.getD_208(), " = ", " = ", "");
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C210
     */
    protected void creaC210(C210[] c210) throws EccezioneSbnDiagnostico {
        c210_string = "";
        if (c210 == null)
            return;
        C210 c210t;
        boolean primo = true;
        for (int i = 0; i < c210.length; i++) {
            c210t = c210[i];
            for (int j = 0; j < c210t.getAc_210Count(); j++) {
                if (primo) {
                    c210_string = concatena(c210_string, c210t.getAc_210(j).getA_210(), ". - ", " ; ", "");
                    primo = false;
                } else {
                    c210_string = concatena(c210_string, c210t.getAc_210(j).getA_210(), " ; ", " ; ", "");
                }
                if (c210t.getAc_210(j).getC_210Count() > 0)
                    c210_string = concatena(c210_string, c210t.getAc_210(j).getC_210(), " : ", " : ", "");
            }
            if (c210[i].getD_210Count() > 0)
                if (primo) {
                    primo = false;
                    c210_string = concatena(c210_string, c210[i].getD_210(), ". - ", ", ", "");
                } else
                    c210_string = concatena(c210_string, c210[i].getD_210(), ", ", ", ", "");
            if (c210[i].getE_210Count() > 0)
                if (primo) {
                    primo = false;
                    c210_string = concatena(c210_string, c210[i].getE_210(), ". - (", "", " ; ", "");
                } else
                    c210_string = concatena(c210_string, c210[i].getE_210(), " (", "", " ; ", "");
            if (c210[i].getG_210Count() > 0)
                if (primo) {
                    primo = false;
                    c210_string = concatena(c210_string, c210[i].getG_210(), ". - (", " : ", "");
                } else
                    c210_string = concatena(c210_string, c210[i].getG_210(), " : ", " : ", "");
            if (c210[i].getH_210Count() > 0)
                if (primo) {
                    primo = false;
                    c210_string = concatena(c210_string, c210[i].getH_210(), ". - (", ", ", "");
                } else if (c210[i].getE_210Count() == 0)
                    c210_string = concatena(c210_string, c210[i].getH_210(), " (", ", ", "");
                else
                    c210_string = concatena(c210_string, c210[i].getH_210(), ", ", ", ", "");
            if (c210[i].getE_210Count() > 0 || c210[i].getH_210Count() > 0)
                c210_string += ")";
        }
        if (titolo != null && titolo.getCD_NATURA().equals("S"))
            if (c210_string.equals(""))
                throw new EccezioneSbnDiagnostico(3114, "Area della pubblicazione obbligatoria");
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C215
     */
    public String creaC215(C215 c215) {
        c215_string = "";
        if (c215 == null)
            return c215_string;
        if (c215.getA_215Count() > 0)
            c215_string = concatena(c215_string, c215.getA_215(), ". - ", " ; ", "");
        if (c215.getC_215() != null)
            c215_string = concatena(c215_string, c215.getC_215(), " : ", "");
        if (c215.getD_215Count() > 0)
            c215_string = concatena(c215_string, c215.getD_215(), " ; ", " ; ", "");
        if (c215.getE_215Count() > 0)
            c215_string = concatena(c215_string, c215.getE_215(), " + ", " + ", "");
        return c215_string;
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C300
     */
    protected void creaC230(A230 c230) {
        c230_string = "";
        if (c230 == null)
            return;
        c230_string = concatena(c230_string, c230.getA_230(), ". - ", "");
    }
    /**
     * Crea la stringa che rappresenta l'elemento di tipo C300
     */
    protected void creaC300(C3XX[] c300) {
        c300_string = "";
        if (c300 == null)
            return;
        boolean primo = true;
        for (int i = 0; i < c300.length; i++) {
            if (c300[i].getTipoNota() != null &&
            		c300[i].getTipoNota().getType() == SbnTipoNota.valueOf("300").getType())
                if (primo) {
                    primo = false;
                    c300_string = concatena(c300_string, c300[i].getA_3XX(), ". ((", "");
                } else
                    c300_string = concatena(c300_string, c300[i].getA_3XX(), ". - ", "");
        }
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

         // almaviva7 25/09/2009 "Bug in canna" - Ripreso da almaviva2 01.12.2009
		} else if (posizione < 10000) {
			tmp += posizione;
		} else {
			tmp += "????";
// End almaviva7 25/09/2009 - Ripreso da almaviva2 01.12.2009
        }
        return tipo + "-" + tmp + ";";
    }
    /**
      * Returns the indice_isbd.
      * @return String
      */
    public String getIndice_isbd() {
        return indice_isbd;
    }
    /**
     * Returns the isbd.
     * @return String
     */
    public String getIsbd() {
        return isbd;
    }

    /** Elabora l'organico della composizione musicale */
    public String preparaB928(String a_928[], String b_928, String c_928, String g_929, boolean unimarc)
        throws EccezioneSbnDiagnostico {

    	// Modifica Luglio 2014 BUG 5606 - errore organico inesistente su allineamenti
    	// sostituito tutto il metodo con il corrispettivo di Indice

//        String token;
//        boolean prosegui = false;
//        for (int i = 0; i < a_928.length; i++) {
//            String fomu;
//            if (unimarc)
//                //fomu = Decodificatore.getDs_tabellaFromUnimarc("FOMU", a_928[i]);
//            	fomu = Decodificatore.getDs_tabella("FOMU", a_928[i]);
//            else
//                fomu = Decodificatore.getDs_tabella("FOMU", a_928[i]);
//            if (ElencoFormeU.getInstance().contiene(fomu)) {
//                prosegui = true;
//                break;
//            }
//        }
//        if (!prosegui) {
//            if (ElencoForme.getInstance().contiene(NormalizzaNomi.normalizzazioneGenerica(g_929))) {
//                prosegui = true;
//            }
//        }
//        if (!prosegui)
//            return null;
//        b_928 = b_928.trim();
//        if (b_928.indexOf(' ')>=0) {
//            throw new EccezioneSbnDiagnostico(3238,"Spazi non ammessi");
//        }
//        if (c_928 != null) {
//            c_928 = c_928.trim();
//            if (c_928.indexOf(' ')>=0) {
//                throw new EccezioneSbnDiagnostico(3238,"Spazi non ammessi");
//            }
//        }
//
//        StringTokenizer st = new StringTokenizer(b_928, ",");
//        str = true;
//        while (st.hasMoreElements()) {
//            token = st.nextToken();
//            if (token.endsWith("str")) {
//                // boolean digit = true;
//                // for (int i = 0; i < token.length() - 3; i++)
//                // if (!Character.isDigit(token.charAt(i)))
//                // digit = false;
//                // if (!digit)
//                //   break;
//                if (c_928 == null)
//                    return ". " + b_928;
//                if (st.hasMoreTokens()) {
//                    token = st.nextToken();
//                    if (token.equals("orch")
//                        || token.equals("banda")
//                        || token.equals("orchfi")
//                        || token.equals("orchar")) {
//                        String ret = preparaC928(c_928, true, unimarc);
//                        if (!ret.equals(". "))
//                            ret += " e ";
//                        ret += getOrganico(token, unimarc, false);
//                        return ret;
//                    } else
//                        return preparaC928(c_928, false, unimarc);
//                } else
//                    return preparaC928(c_928, false, unimarc);
//            }
//        }
//        //Elaboro la stringa b_928
//        List tokens = separaToken928(b_928);
//        String risultato = "";
//        int counter = 0;
//        String precToken = null;
//        for (int i = 0; i < tokens.size(); i++) {
//            token = (String) tokens.get(i);
//            if (token.toUpperCase().indexOf("CORO")>=0) {
//                int lung = token.indexOf("(") + 1;
//                if (token.toUpperCase().endsWith("V)"))
//                    if (isNumber(token.substring(lung, token.length() - 2))) {
//                      int init = token.toUpperCase().indexOf("CORO");
//                      if (init > 0) {
//                        token = token.substring(0,init) + "Cori a " + token.substring(lung, token.length() - 2) + " voci";
//                      } else {
//                        token = "Coro a " + token.substring(lung, token.length() - 2) + " voci";
//                      }
//                    }
//                risultato += "," + token;
//                continue;
//            } else if (token.endsWith("V")) {
//                if (isNumber(token.substring(0, token.length() - 1))) {
//                    int n = Integer.parseInt(token.substring(0, token.length() - 1));
//                    if (c_928 == null)
//                        //throw new EccezioneSbnDiagnostico(3216, "Manca l'organico analitico");
//                        return ". " + b_928;
//                    risultato += "," + appendiVoci(c_928, n, unimarc);
//                    continue;
//                } else if (token.startsWith(">")){
//                    risultato += ". Voci" ;
//                    continue;
//                }
//            }
//            if ((i + 1) < tokens.size()
//                && togliNumeroFinale(token).equals(togliNumeroFinale((String) tokens.get(i + 1)))) {
//                counter++;
//            } else {
//                if (Character.isDigit(token.charAt(0))) {
//                    String num ="";
//                    for (int n = 0; n< token.length() && Character.isDigit(token.charAt(n));n++)
//                        num+= token.charAt(n) ;
//                    token = token.substring(num.length());
//                    if (token.toUpperCase().startsWith("CORO(") || token.toUpperCase().startsWith("CORO (")) {
//                      int lung = 5;
//                      if (token.toUpperCase().startsWith("CORO ("))
//                          lung = 6;
//                      if (token.toUpperCase().endsWith("V)"))
//                          if (isNumber(token.substring(lung, token.length() - 2))) {
//                              token = "Cori a " + token.substring(lung, token.length() - 2) + " voci";
//                          }
//                      risultato += "," + num+ " " + token;
//                    } else {
//                      risultato += "," + num+ " " + getOrganico(token, unimarc, true);
//                    }
//                } else if (counter > 0) {
//                    risultato += "," + (counter + 1) + " " + getOrganico(togliNumeroFinale(token), unimarc, true);
//                    counter = 0;
//                } else {
//                    risultato += "," + getOrganico(togliNumeroFinale(token), unimarc, false);
//                }
//            }
//
//        }
//        //Cavo la virgola iniziale.
//        if (risultato.length() > 0)
//            risultato = ". " + risultato.substring(1);
//        return risultato;



		String token;
		boolean prosegui = false;

		hmVoci.put("A", "V");
		hmVoci.put("A-bi", "V");
		hmVoci.put("B", "V");
		hmVoci.put("Br", "V");
		hmVoci.put("C", "V");
		hmVoci.put("Coro", "V");
		hmVoci.put("Mzs", "V");
		hmVoci.put("S", "V");
		hmVoci.put("S-bi", "V");
		hmVoci.put("T", "V");
		hmVoci.put("V", "V");
		hmVoci.put("V-bi", "V");
		hmVoci.put("V-d", "V");
		hmVoci.put("V-dd", "V");
		hmVoci.put("V-nn", "V");
		hmVoci.put("V-oc", "V");
		hmVoci.put("V-q", "V");
		hmVoci.put("V-recit", "V");
		hmVoci.put("V-sp", "V");
		hmVoci.put("V-sx", "V");
		hmVoci.put("V-ud", "V");

		for (int i = 0; i < a_928.length; i++) {
			String fomu;
			if (unimarc)
				//fomu = Decodificatore.getDs_tabellaFromUnimarc("FOMU", a_928[i]);
				fomu = Decodificatore.getDs_tabella("FOMU", a_928[i]);
			else
				fomu = Decodificatore.getDs_tabella("FOMU", a_928[i]);
//almaviva4 12/02/2013
			if (ElencoFormeU.getInstance().contiene(fomu)) {
//			if (fomu != null) {
//almaviva4 12/02/2013 fine
				prosegui = true;
				break;
			}
		}
		if (prosegui == false) {
			if (ElencoForme.getInstance()
				.contiene(NormalizzaNomi.normalizzazioneGenerica(g_929))) {
				prosegui = true;
			}
		}
//		if (!prosegui)
//		if (prosegui == false)
//			return null;
//bug mantis 4876
		if (prosegui == false) {
			b_928 = b_928.trim();
			if (b_928.indexOf(' ') >= 0) {
				throw new EccezioneSbnDiagnostico(3238, "Spazi non ammessi");
			}
			if (c_928 != null) {
				c_928 = c_928.trim();
				if (c_928.indexOf(' ') >= 0) {
					throw new EccezioneSbnDiagnostico(3238, "Spazi non ammessi");
				}
			}
			return null;
		}

		b_928 = b_928.trim();
		if (b_928.indexOf(' ') >= 0) {
			throw new EccezioneSbnDiagnostico(3238, "Spazi non ammessi");
		}
		if (c_928 != null) {
			c_928 = c_928.trim();
			if (c_928.indexOf(' ') >= 0) {
				throw new EccezioneSbnDiagnostico(3238, "Spazi non ammessi");
			}
		}

		StringTokenizer strin = new StringTokenizer(b_928, ",");
		str = true;

//Mette in ordine la stringa dell'organico sintetico:

		if (strin.countTokens() >= 2 ) {
			HashMap mapOrgSintetico = new HashMap();

			int cnt = MAX_WEIGHT;
			while (strin.hasMoreElements()) {
				token = strin.nextToken();
				int weight = getWeight(token);
				weigths[elements] = weight;
				description[elements] = token;
				elements++;
			}
			// Ordiniamo per peso
			int temp = 0;
			String tempDesc;
			for(int j=0;j<elements;j++)
				{
				for(int i=j;i<elements;i++)
						{
						if(weigths[j]>weigths[i])
								{
									temp=weigths[j];
									tempDesc = description[j];

									weigths[j]=weigths[i];
									description[j]=description[i];

									weigths[i]=temp;
									description[i]=tempDesc;

								}//fine if
						}//fine for
				}//fi

			StringBuffer buf = new StringBuffer();
			for(int i=0;i<elements;i++)
			{
				if (i!=0)
					buf.append(",");
				buf.append(description[i]);
			}

			b_928 =  buf.toString();
		}

		// Spacchetta l'organico sintetico
		StringTokenizer st = new StringTokenizer(b_928, ",");
		String ret=". ";
		boolean orchestra = false;
		boolean coro = false;
		while (st.hasMoreElements()) {
			token = st.nextToken();

			if (token.endsWith("str")) {	// Gestione strumenti
				if (c_928 == null)
				{
//almaviva4 12/02/2013 bug mantis 5249

					String numero = ""; // Rimuoviamo numeri iniziali
					for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n)); n++)
						numero += token.charAt(n);
					String s = token.substring(numero.length());

					if (isNumber(numero)) {
						String strumenti = " strumenti";
						if ("1".equals(numero))
							strumenti = " strumento";
							token = numero + strumenti;
					} else {
						token = " strumento";
					}

					if (!ret.equals(". "))
						ret += ", "; //" e ";
					ret += token;

//						ret += " ,strumento";
//					ret += b_928; // ". " +
//almaviva4 12/02/2013 bug mantis 5249 fine
				}
				else
				{
				if (st.hasMoreTokens()) {
					token = st.nextToken();
					if (token.equals("orch")
						|| token.equals("banda")
						|| token.equals("orchfi")
						|| token.equals("orchar")) {
						orchestra = true;
						ret += preparaC928(c_928, true, unimarc, orchestra, RICHIESTA_STRUMENTI);
						if (!ret.equals(". "))
//almaviva4 01/07/2013			ret += ","; //" e ";
							ret += ", "; //" e ";
						ret += getOrganico(token, unimarc, false);
					} //end if orch
					else if (token.toUpperCase().indexOf("COR") > -1) {

						String numCori = "";
						for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n));	n++)
							numCori += token.charAt(n);
						token = token.substring(numCori.length());


						ret += preparaC928(c_928, false, unimarc, orchestra, RICHIESTA_STRUMENTI);
						if (!ret.equals(". "))
//almaviva4 01/07/2013			ret += ","; //" e ";
							ret += ", "; //" e ";

						int idx = token.indexOf("(");
						if (idx > 0) {
							int lung = idx+1;
							if (token.toUpperCase().endsWith("V)")){

								String num = token.substring(lung, token.length() - 2);
								if (isNumber(num)) {
									String voci = " voci";
									if ("1".equals(num))
										voci = " voce";
									if (numCori.length() > 0)
										token = numCori + " Cori a " + num + voci;
									else
										token = token.substring(0,idx)+ " a " + num + voci; // "Coro a "
								}
							}
						}
						else
						{
							if (numCori.length() > 0)
								token = numCori + " Cori";
						}

						ret += token;
						//return ret;
					}
					 else
//					almaviva4 12/12
								  if (!ret.equals(". "))
//almaviva4 01/07/2013	ret += ","; //" e ";
									  ret += ", "; //" e ";
//					almaviva4 12/12
						ret +=  preparaC928(c_928, false, unimarc, orchestra, RICHIESTA_STRUMENTI);
				} //end if has more
				else
//					almaviva4 12/12
								  if (!ret.equals(". "))
//almaviva4 01/07/2013					  ret += ","; //" e ";
									  ret += ", "; //" e ";
//					almaviva4 12/12
					ret +=  preparaC928(c_928, false, unimarc, orchestra, RICHIESTA_STRUMENTI);
				}

			} //end if str

			else if (token.equals("orch")|| token.equals("banda")|| token.equals("orchfi")|| token.equals("orchar")) {
//almaviva4 12/12
				if (!ret.equals(". "))
//almaviva4 01/07/2013	ret += ","; //" e ";
					ret += ", "; //" e ";
//almaviva4 12/12
				ret += getOrganico(token, unimarc, false);
				orchestra = true;

			} //end if orch

			else if (token.toUpperCase().indexOf("COR") > -1) {
				coro = true;

				String numCori = "";
				for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n));	n++)
					numCori += token.charAt(n);
				token = token.substring(numCori.length());



				if (c_928 == null)
				{
					ret +=  b_928; // ". " +
				}
				else
				{
				if (!ret.equals(". "))
//almaviva4 01/07/2013	ret += ","; //" e ";
					ret += ", "; //" e ";
				int idx = token.indexOf("(");
				if (idx > 0)
				{
					int lung = idx+1;
					if (token.toUpperCase().endsWith("V)")){
						String num = token.substring(lung, token.length() - 2);
						if (isNumber(num)) {
							String voci = " voci";
							if ("1".equals(num))
								voci = " voce";
							if (numCori.length() > 0)
								token = numCori + " Cori a " + num + voci;
							else
								token = token.substring(0,idx) + " a " + num + voci;
						}

					}
				} //end startsWith
				else
				{
					if (numCori.length() > 0)
						token = numCori + " Cori";
				}
				ret += token;
				}
			} //end if coro

			else if (token.toUpperCase().endsWith("V")) {
//almaviva4 12/02/2013 bug mantis 5249
				if (c_928 == null)
				{
					String numero = ""; // Rimuoviamo numeri iniziali
					for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n)); n++)
						numero += token.charAt(n);
					String s = token.substring(numero.length());

					if (isNumber(numero)) {
						String voci = " voci";
						if ("1".equals(numero))
							voci = " voce";
							token = numero + voci;
					} else {
						token = " voce";
					}
					if (!ret.equals(". "))
						ret += ", ";
					ret += token;

//				ret += " ,voce";
				}
				else
//almaviva4 12/02/2013 bug mantis 5249 fine

			//if (coro == true)
					if (!ret.equals(". "))
//almaviva4 01/07/2013		ret += ","; //" e ";
						ret += ", "; //" e ";
				ret += preparaC928(c_928, false, unimarc, orchestra, RICHIESTA_VOCI);
			} //end if V

			else {
					if (!ret.equals(". "))
//almaviva4 01/07/2013		ret += ","; //" e ";
						ret += ", "; //" e ";
				ret += getOrganico(token, unimarc, false);
			} //end if 'resto'

		} //end while sull'organico sintetico

		return ret;



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



 // MODIFICA PRE-IBM-RAD
    protected boolean IfNumeroIniziale(String token) {
 	   //int i = token.length();
 	   int i;
 	   int start =0;
 	   for (i=0; i < token.length(); i++){
 		   if (Character.isDigit(token.charAt(i))){
 			   return true;
 		   }
 	   }
 	   return false;
    }
    protected String togliNumeroIniziale(String token) {
 	   //int i = token.length();
 	   int i;
 	   int start =0;
 	   for (i=0; i < token.length(); i++){
 		   if (Character.isDigit(token.charAt(i))){
 			   start++;
 			   //break;
 		   }
 	   }
 	   if(start >0)
 		   return token.substring(start, token.length());
 	   else
 		   return token;
    }
    protected String prendiNumeroIniziale(String token) {
 	   int i;
 	   int start =0;
 	   for (i=0; i < token.length(); i++){
 		   if (Character.isDigit(token.charAt(i))){
 			   start++;
 			   //break;
 		   }
 	   }
 	   if(start >0)
 		   return token.substring(0,start);
 	   else
 		   return token;
    }


 // FINE MODIFICA PRE-IBM-RAD





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
    protected List separaToken928c(String b928) {
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
              else if (!inside)
                token += c;
          }
          n++;
      }
      v.add(token);
      return v;
  }
    /** Elabora l'organico analitico della composizione musicale */

// Modifica Luglio 2014 BUG 5606 - errore organico inesistente su allineamenti
// sostituito tutto il metodo con il corrispettivo di Indice
//	protected String preparaC928(String stringa, boolean solo, boolean unimarc, boolean orchestra)
	protected String preparaC928(String stringa, boolean solo, boolean unimarc, boolean orchestra, int richiesta)
        throws EccezioneSbnDiagnostico {
//
//
//        if (stringa == null) {
//            //throw new EccezioneSbnDiagnostico(3216, "Manca l'organico analitico");
//            return null;
//        }
//        str = true;
//        String token;
//        String risultato = ". ";
//        int counter = 0;
//        boolean primo = true;
//        List tokens = separaToken928c(stringa);
//        if (!solo) {
//            for (int i = 0; i < tokens.size(); i++) {
//                token = (String) tokens.get(i);
//                String num ="";
//                for (int n = 0; n< token.length() && Character.isDigit(token.charAt(n));n++)
//                    num+= token.charAt(n) ;
//                token = token.substring(num.length());
//                if (token.toUpperCase().startsWith("CORO")) {
//                    StringTokenizer st = new StringTokenizer(token, ",");
//                    int voci = 0;
//                    while (st.hasMoreTokens()) {
//                        st.nextToken();
//                        voci ++;
//                    }
//                    if (primo)
//                        primo = false;
//                    else
//                        risultato += ",";
//                    if (num.length() > 0)
//                      risultato += num + " cori a " + voci + " voci";
//                    else
//                      risultato += "Coro a " + voci + " voci";
//                } else if ((i + 1) < tokens.size()
//                    && togliNumeroFinale(token).equals(togliNumeroFinale((String) tokens.get(i + 1)))) {
//                    counter++;
//                } else {
//                    if (counter > 0 || num.length() > 0) {
//                      if (num.length() > 0)
//                        counter = Integer.parseInt(num) - 1;
//                        if (primo)
//                            primo = false;
//                        else
//                            risultato += ",";
//                        //Si usa il plurale
//                        risultato += (counter + 1)
//                            + " "
//                            + getOrganico(togliNumeroFinale(token), unimarc, true);
//                        counter = 0;
//                    } else {
//                        if (primo)
//                            primo = false;
//                        else
//                            risultato += ",";
//                        risultato += getOrganico(togliNumeroFinale(token), unimarc, false);
//                    }
//                }
//            }
//        } else {
//            StringTokenizer st = new StringTokenizer(stringa, ",");
//            while (st.hasMoreElements()) {
//                token = st.nextToken();
//                if (token.endsWith("-solo")) {
//                    if (primo)
//                        primo = false;
//                    else
//                        risultato += ",";
//                    risultato += getOrganico(token.substring(0, token.length() - 5), unimarc, false);
//                }
//            }
//        }
//        return risultato;


		if (stringa == null) {
			//throw new EccezioneSbnDiagnostico(3216, "Manca l'organico analitico");
//bug mantis 5053
//			return null;
			return "";
		}
		str = true;
		String token;
		String risultato = ""; //". ";
		int counter = 0;
		boolean primo = true;
		List tokens = separaToken928c(stringa);

		if (!solo) {
			for (int i = 0; i < tokens.size(); i++) {
				token = (String) tokens.get(i);
				// Se orchestra a true non dobbiamo tirar fuori gli strumenti ma solo le voci
				if (orchestra == true || richiesta == RICHIESTA_VOCI)
				{
					String num = ""; // Rimuoviamo numeri iniziali
					for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n)); n++)
						num += token.charAt(n);
					String s = token.substring(num.length());

					if(!hmVoci.containsKey(togliNumeroFinale(s)))
						continue;
				}

//				if (richiesta == RICHIESTA_VOCI)
//					if(!hmVoci.containsKey(togliNumeroFinale(token)))
//						continue;

//bug mantis 5053
//if(hmVoci.containsKey(togliNumeroFinale(token)))
				if (richiesta == RICHIESTA_STRUMENTI)
				{
					String num = ""; // Rimuoviamo numeri iniziali
					for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n)); n++)
						num += token.charAt(n);
					String s = token.substring(num.length());

					if(hmVoci.containsKey(togliNumeroFinale(s)))
						continue;
				}


				String num = "";
				for (int n = 0;
					n < token.length() && Character.isDigit(token.charAt(n));
					n++)
					num += token.charAt(n);
				token = token.substring(num.length());

				// Ignoriamo il coro
				if (token.toUpperCase().startsWith("CORO"))
					continue;

				 if (
					(i + 1) < tokens.size()
						&& togliNumeroFinale(token).equals(
							togliNumeroFinale((String) tokens.get(i + 1)))) {
					counter++;
				} else {
					if (counter > 0 || num.length() > 0) {
						if (num.length() > 0)
							counter = Integer.parseInt(num) - 1;
						if (primo)
							primo = false;
						else
//almaviva4 01/07/2013	risultato += ",";
							risultato += ", ";
						//Si usa il plurale
						risultato += (counter + 1)
							+ " ";

					// Ignoriamo orchestra
					if (!token.equals("orch") && !token.equals("banda") && !token.equals("orchfi") && !token.equals("orchar")) // ARGE
						risultato += getOrganico(
								togliNumeroFinale(token),
								unimarc,
								true);
						counter = 0;
					} else {
						if (primo)
							primo = false;
						else
//almaviva4 01/07/2013	risultato += ",";
							risultato += ", ";


						// Ignoriamo orchestra
						if (!token.equals("orch") && !token.equals("banda") && !token.equals("orchfi") && !token.equals("orchar")) // ARGE

						risultato
							+= getOrganico(
								togliNumeroFinale(token),
								unimarc,
								false);
					}
				}
			}
		} else { // Strumenti con suffisso -solo
//			if (richiesta == RICHIESTA_STRUMENTI || richiesta == RICHIESTA_VOCI_STRUMENTI)
//			{
//				StringTokenizer st = new StringTokenizer(stringa, ",");
//				while (st.hasMoreElements()) {
//					token = st.nextToken();
//					if (token.endsWith("-solo")) {
//						if (primo)
//							primo = false;
//						else
////almaviva4 01/07/2013	risultato += ",";
//							risultato += ", ";
//						risultato
//							+= getOrganico(
//								token.substring(0, token.length() - 5),
//								unimarc,
//								false);
//					}
//				} // end while
//			}
//
			if (richiesta == RICHIESTA_STRUMENTI || richiesta == RICHIESTA_VOCI_STRUMENTI)
			{
			for (int i = 0; i < tokens.size(); i++) {
				token = (String) tokens.get(i);
				if (token.endsWith("-solo"))
				   token = token.substring(0, token.length() - 5);
//bug mantis 5471/5472 arge
				else
				{
					if (orchestra == true)
						continue;
				}
//bug mantis 5471/5472 fine
// arge
//				// Se orchestra a true non dobbiamo tirar fuori gli strumenti ma solo le voci
//				if (orchestra == true || richiesta == RICHIESTA_VOCI)
//				{
//					String num = ""; // Rimuoviamo numeri iniziali
//					for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n)); n++)
//						num += token.charAt(n);
//					String s = token.substring(num.length());
//
//					if(!hmVoci.containsKey(togliNumeroFinale(s)))
//						continue;
//				}


//				if (richiesta == RICHIESTA_STRUMENTI)
//				{
//					String num = ""; // Rimuoviamo numeri iniziali
//					for (int n = 0;	n < token.length() && Character.isDigit(token.charAt(n)); n++)
//						num += token.charAt(n);
//					String s = token.substring(num.length());
//
//					if(hmVoci.containsKey(togliNumeroFinale(s)))
//						continue;
//				}


				String num = "";
				for (int n = 0;
					n < token.length() && Character.isDigit(token.charAt(n));
					n++)
					num += token.charAt(n);
				token = token.substring(num.length());

//				// Ignoriamo il coro
//				if (token.toUpperCase().startsWith("CORO"))
//					continue;
				if (i < tokens.size()-1)
				{
					String tokenNext = 	(String )tokens.get(i + 1);
					if (tokenNext.endsWith("-solo"))
						   tokenNext = tokenNext.substring(0, tokenNext.length() - 5);
					 if (
						(i + 1) < tokens.size()
							&& togliNumeroFinale(token).equals(
									togliNumeroFinale(tokenNext))) {
						i++;
						counter++;
					}

				}
// else {
					if (counter > 0 || num.length() > 0) {
						if (num.length() > 0)
							counter = Integer.parseInt(num) - 1;
						if (primo)
							primo = false;
						else
//almaviva4 01/07/2013	risultato += ",";
							risultato += ", ";
						//Si usa il plurale
						risultato += (counter + 1)
							+ " ";

					// Ignoriamo orchestra
					if (!token.equals("orch") && !token.equals("banda") && !token.equals("orchfi") && !token.equals("orchar")) // ARGE
						risultato += getOrganico(
								togliNumeroFinale(token),
								unimarc,
								true);
						counter = 0;
					} else {
						if (primo)
							primo = false;
						else
//almaviva4 01/07/2013	risultato += ",";
							risultato += ", ";


						// Ignoriamo orchestra
						if (!token.equals("orch") && !token.equals("banda") && !token.equals("orchfi") && !token.equals("orchar")) // ARGE

						risultato
							+= getOrganico(
								togliNumeroFinale(token),
								unimarc,
								false);
					}
//				}
			}

			}
		}
		return risultato;



    }

    protected String getOrganico(String token, boolean unimarc, boolean plurale) throws EccezioneSbnDiagnostico {

//    	// Modifica Luglio 2014 BUG 5606 - errore organico inesistente su allineamenti
//    	// sostituito tutto il metodo con il corrispettivo di Indice
//
//        String orga;
//        int n;
//        if (token.endsWith("-solo")) {
//          token = token.substring(0, token.length() - 5);
//        }
//        while((n = token.indexOf("$")) >= 0)  {
//                token = token.substring(0, n) + token.substring(n+1);
//        }
//
//        while((n = token.indexOf("%")) >= 0)  {
//                token = token.substring(0, n) + token.substring(n+1);
//        }
//        token = togliNumeroFinale(token);
//        String codice;
//        if (plurale) codice = "ORGP";
//        else codice = "ORGA";
//        if (unimarc)
//            //orga = Decodificatore.getDs_tabellaFromUnimarc(codice, token);
//        	orga = Decodificatore.getDs_tabella(codice, token);
//        else
//            orga = Decodificatore.getDs_tabella(codice, token);
//        if (orga == null) {
//            int barra = token.indexOf("/");
//            if (barra>=0) {
//
//            	// Intervento interno - almaviva2 Modifica riportata da Intervento di Indice 11.04.2012
//            	// almaviva7 23/02/09 mantis 2640
//				//				orga = 	getOrganico(token.substring(0,barra),unimarc, plurale)+" o "+token.substring(barra+1);
//            	// orga = getOrganico(token.substring(0,barra),unimarc, plurale)+" o "+getOrganico(token.substring(barra+1),unimarc, plurale);
//				// almaviva7 new
//				String parte2 = token.substring(barra + 1);
//				String num = "";
//				for (int n1 = 0;
//					n1 < parte2.length()
//						&& Character.isDigit(parte2.charAt(n1));
//					n1++)
//					num += parte2.charAt(n1);
//
//				orga =
//					getOrganico(token.substring(0, barra), unimarc, plurale)
//						+ " o ";
//				if (num.length() > 0) {
//					orga += num
//						+ " "
//						+ getOrganico(
//							token.substring(barra + 1 + num.length()),
//							unimarc,
//							plurale);
//				} else
//					orga
//						+= getOrganico(
//							token.substring(barra + 1),
//							unimarc,
//							plurale);
//				// End Arge
//
//            } else {
//                EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3124, "Organico errato");
//                ecc.appendMessaggio(": \"" + token + "\"");
//                throw ecc;
//            }
//        }
//        return orga;
//

		String orga;
		int n;
		if (token.endsWith("-solo")) {
			token = token.substring(0, token.length() - 5);
		}
		while ((n = token.indexOf("$")) >= 0) {
			token = token.substring(0, n) + token.substring(n + 1);
		}

		while ((n = token.indexOf("%")) >= 0) {
			token = token.substring(0, n) + token.substring(n + 1);
		}
		String numero = ""; // Rimuoviamo numeri iniziali
		for (int nn = 0;	nn < token.length() && Character.isDigit(token.charAt(nn)); nn++)
			numero += token.charAt(nn);
		String s = token.substring(numero.length());

// MODIFICA PRE-IBM-RAD
		token = togliNumeroFinale(token);
		String tokenSenzaNumero="";
		String NumToken = "";
		boolean numIniziale=false;
// Mantis 5216

		int barra1 = token.indexOf("/");
		if ( (barra1 >= 0) && ((IfNumeroIniziale(token)) && (Character.isDigit(token.charAt(0))))) {
			numIniziale=true;
			NumToken = prendiNumeroIniziale(token);
			token = togliNumeroIniziale(token);
		}
		else{

//almaviva4 bug mantis 5296
//aggiunto && (barra1 < 0) all'if
//tolto commento alle 3 istruzioni successive (messo da Marco alcune versioni fa)
			if(IfNumeroIniziale(token) && (barra1 < 0)){
				numIniziale=true;
				NumToken = prendiNumeroIniziale(token);
				token = togliNumeroIniziale(token);
			}
		}
// END Mantis 5216
// FINE MODIFICA PRE-IBM-RAD
		String codice = null;
		if (plurale)
			codice = "ORGP";
		else
//bug mantis 5332
		if (isNumber(numero)) {
			if ("1".equals(numero))
				codice = "ORGA";
			else
				codice = "ORGP";
			}
		else
		codice = "ORGA";

//bug mantis 5332 fine
		if (unimarc)
			//orga = Decodificatore.getDs_tabellaFromUnimarc(codice, token);
			orga = Decodificatore.getDs_tabella(codice, token);
		else
			orga = Decodificatore.getDs_tabella(codice, token);
		if (orga == null) {
			int barra = token.indexOf("/");
			if (barra >= 0) {
				// almaviva7 23/02/09 mantis 2640
				//				orga = 	getOrganico(token.substring(0,barra),unimarc, plurale)+" o "+token.substring(barra+1);
				// almaviva7 new
				String parte2 = token.substring(barra + 1);
				String num = "";
				for (int n1 = 0;
					n1 < parte2.length()
						&& Character.isDigit(parte2.charAt(n1));
					n1++)
					num += parte2.charAt(n1);
				String parte1 = token.substring(0, barra);
				orga =
					getOrganico(token.substring(0, barra), unimarc, plurale)
						+ " o ";
				if (num.length() > 0) {
					orga += num
						+ " "
						+ getOrganico(
							token.substring(barra + 1 + num.length()),
							unimarc,
							plurale);
				} else
					orga
						+= getOrganico(
							token.substring(barra + 1),
							unimarc,
							plurale);
				// End Arge
			} else {
				EccezioneSbnDiagnostico ecc =
					new EccezioneSbnDiagnostico(3124, "Organico errato");
				ecc.appendMessaggio(": \"" + token + "\"");
				throw ecc;
			}
		}
//MODIFICA PRE-IBM-RAD
		if(numIniziale){
//bug mantis 5332			return NumToken + " " + token;
			return NumToken + " " + orga;
		}
		else{
			return orga;
		}
//	END	MODIFICA PRE-IBM-RAD

    }

    protected String appendiVoci(String stringa, int n, boolean unimarc) throws EccezioneSbnDiagnostico {
        String token;
        String risultato = "";
        StringTokenizer st = new StringTokenizer(stringa, ",");
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
                risultato += num+ " " + getOrganico(token, unimarc, true);
                n -= Integer.parseInt(num);
            } else {
              for (int i = 0; i< token.length();i++)
                if (Character.isDigit(token.charAt(i))) {
                  token = token.substring(0,i);
                  break;
                }
                risultato += getOrganico(token, unimarc, false);
                n--;
            }
        }
        return risultato;
    }

    public static String calcolaIsbd(
        C200 c200,
        C205 c205,
        C206 c206[],
        C207 c207,
        C208 c208,
        C210 c210[],
        C215 c215,
        A230 c230,
        C3XX[] c300,
        boolean antico,
        String lingua,
        String cd_natura)
        throws EccezioneSbnDiagnostico {
        //la lingua è necessaria per la stop list degli articoli
        CostruttoreIsbd costr_isbd = new CostruttoreIsbd();
        costr_isbd.lingua = lingua;
        costr_isbd.antico = antico;
        costr_isbd.creaC200(c200, cd_natura);
        costr_isbd.creaC205(c205);
        costr_isbd.creaC206(c206);
        costr_isbd.creaC207(c207);
        costr_isbd.creaC208(c208);
        costr_isbd.creaC210(c210);
        costr_isbd.creaC215(c215);
        costr_isbd.creaC230(c230);
        costr_isbd.creaC300(c300);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.c200_string, costr_isbd.c205_string);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.isbd, costr_isbd.c206_string);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.isbd, costr_isbd.c207_string);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.isbd, costr_isbd.c208_string);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.isbd, costr_isbd.c210_string);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.isbd, costr_isbd.c215_string);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.isbd, costr_isbd.c230_string);
        costr_isbd.isbd = costr_isbd.concatena(costr_isbd.isbd, costr_isbd.c300_string);
        if (!cd_natura.equals("W")) {
            costr_isbd.isbd = costr_isbd.aggiungiAsterisco(costr_isbd.isbd);
        }
        return costr_isbd.isbd;
    }

    /**
     *  Calcola l'indice isbd dall'isbd e dalla natura di un titolo
        1) dalla punteggiatura . (( è l'area delle note 300, se ci sono . - dopo .
        (( sono tutte aree 300


        2) si scompatta la riga con la punteggiatura . -
        La prima area è l'unica obbligatoria ed è sempre la 200

        se la natura è S
        se ci sono 5 aree sono in sequenza 200, 205, 207, 210, 215
        se ci sono 4 aree sono 200, 207, 210,215
        se ci sone 3 aree sono 200,210,215
        se ci sono 2 aree sono 200,210

        se la natura è C ci posso essere al massimo due aree, la 200 e la 210

        se la natura è M o W
        se ci sono 4 aree sono le 200,205,210,215
        se ci sono 3 aree sono le 200,210,215
        se ci sono 2 aree sono 200,210

        Per le altre nature c'è solo una area 200.
     * @param isbd
     * @param natura
     * @return
     */
    private String calcolaIndice(String isbd, String natura) {
        String isbd2 = isbd;
        int c300 = isbd.indexOf(". ((");
        if (c300 >= 0) {
            isbd2 = isbd2.substring(0, c300);
        }
        List v = new ArrayList();
        int n;
        while ((n = isbd2.indexOf(". - ")) >= 0) {
            v.add(isbd2.substring(0, n));
            n += 4;
            isbd2 = isbd2.substring(n);
        }
        v.add(isbd2);
        String indice = "200-0001;";
        int i = 0;
        int punt = ((String) v.get(i)).length() + 4 + 1; //carattere successivo
        i++;
        if (natura.equalsIgnoreCase("S")) {
            if (v.size() == 5) {
                indice += elementoIndiceIsbd("205", punt);
                punt += ((String) v.get(i)).length() + 4;
                i++;
            }
            if (v.size() >= 4) {
                indice += elementoIndiceIsbd("207", punt);
                punt += ((String) v.get(i)).length() + 4;
                i++;
            }
            if (v.size() >= 3) {
                indice += elementoIndiceIsbd("210", punt);
                punt += ((String) v.get(i)).length() + 4;
                i++;
                indice += elementoIndiceIsbd("215", punt);
            }
            if (v.size() == 2) {
                indice += elementoIndiceIsbd("210", punt);
            }
        }
        if (natura.equalsIgnoreCase("C")) {
            if (v.size() > 1) {
                indice += elementoIndiceIsbd("210", punt);
            }
        }
        if (natura.equalsIgnoreCase("M") || natura.equalsIgnoreCase("W")) {
            if (v.size() >= 4) {
                indice += elementoIndiceIsbd("205", punt);
                punt += ((String) v.get(i)).length() + 4;
                i++;
            }
            if (v.size() >= 3) {
                indice += elementoIndiceIsbd("210", punt);
                punt += ((String) v.get(i)).length() + 4;
                i++;
                indice += elementoIndiceIsbd("215", punt);
            }
            if (v.size() == 2) {
                indice += elementoIndiceIsbd("210", punt);
            }
        }
        if (c300 >= 0) {
            indice += elementoIndiceIsbd("300", c300 + 5);
        }
        return indice;
    }

    /**
     * Utilizzata una volta solo Modifica tutti gli indici isbd del db.
     * --- NON UTILIZZARLA --- la rendo private per essere sicuro
     * Creata per riempire i campi indice_isbd ove vuoti nel db,
     */
//    private static void routineAggiornamentoDbIsbd(Connection conn) {
//        try {
//            //Estrae e modifica gli attributi
//            int ELEMENTI_PER_VOLTA = 100;
//            Tb_titoloResult result = new Tb_titoloResult();
//            result.setConnessione(conn);
//            result.openStatement();
//            result.selectAll();
//            Tb_titoloResult t = new Tb_titoloResult();
//            t.setConnessione(conn);
//            t.openStatement();
//            List v;
//            CostruttoreIsbd ci = new CostruttoreIsbd();
//            do {
//                v = result.getElencoRisultati(ELEMENTI_PER_VOLTA);
//                for (int i = 0; i < v.size(); i++) {
//                    Tb_titolo tit = (Tb_titolo) v.get(i);
//                    tit.setIndice_isbd(ci.calcolaIndice(tit.getIsbd(), tit.getCd_natura()));
//                    t.setTb_titolo(tit);
//                    t.executeCustom("updateIndiceIsbd");
//                }
//            } while (v.size() == 0);
//        } catch (Exception e) {
//            log.debug("Errore " + e);
//            e.printStackTrace();
//        }
//    }

    /**Main di prova*/
    public static void main(String arg[]) {
        try {
/*            if (arg.length != 0 && arg.length != 1) {
                log.debug("NomeProgramma [jndi_url]");
                return;
            }
            if (arg.length == 1) {
                System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
                System.setProperty("java.naming.provider.url", arg[0]);
            }
            ConsoleAppender ca = new ConsoleAppender(new SimpleLayout(), "System.out");
            ca.setThreshold(Priority.ERROR);
            ca.setName("iccu");
            BasicConfigurator.resetConfiguration();
            BasicConfigurator.configure(ca);

            String driver = IndiceServiceLocator.getProperty("DB_DRIVER");
            Class.forName(driver);
            log.info("DB_Driver: " + driver);
            String url = IndiceServiceLocator.getProperty("DB_URL");
            log.info("DB_URL: " + url);
            Connection db_conn =
                DriverManager.getConnection(
                    url,
                    IndiceServiceLocator.getProperty("DB_USER"),
                    IndiceServiceLocator.getProperty("DB_PASSWORD"));
            db_conn.setAutoCommit(false);
            log.debug("Vado sul db: " + url);
            log.debug("INIZIO ESECUZIONE");
            routineAggiornamentoDbIsbd(db_conn);
            db_conn.rollback();
            log.debug("FINE ESECUZIONE");
*/
            log.debug(new CostruttoreIsbd().pulisciNumero("Op. 5 n. 12312"));
        } catch (Exception e) {
            log.error("Problemi: " + e);
            e.printStackTrace();
        }

    }

    private static final String[][] STR_WEIGHT = new String[][] {
//		{"STR",  "1"},
//		{"ORCH", "2"},
//		{"BANDA", "2"},
//		{"ORCHFI", "2"},
//		{"ORCHAR", "2"},
//		{"CORO", "3"},
//		{"CORI", "3"},
//		{"V", "4"}

		{"V",  "1"},
		{"CORO", "2"},
		{"CORI", "2"},
		{"STR", "3"},
		{"ORCH", "4"},
		{"ORCHAR", "4"},
		{"ORCHFI", "4"},
		{"BANDA", "4"}

	};

    private static final int MAX_WEIGHT = STR_WEIGHT.length;

	public int getWeight(String value) {
		for (int i = 0; i < MAX_WEIGHT; i++ ) {
			String[] w = STR_WEIGHT[i];

			if (value.toUpperCase().startsWith(w[0]))
				return Integer.parseInt(w[1]);

			if (value.toUpperCase().endsWith(w[0]))
				return Integer.parseInt(w[1]);

			if (value.toUpperCase().indexOf(w[0])> -1)
				return Integer.parseInt(w[1]);
		}

//almaviva4 12/12		return -1;
		return 5;
	}

}
