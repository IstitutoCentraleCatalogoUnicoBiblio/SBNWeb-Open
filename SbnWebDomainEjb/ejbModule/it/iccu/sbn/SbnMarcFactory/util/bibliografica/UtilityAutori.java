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
package it.iccu.sbn.SbnMarcFactory.util.bibliografica;

import it.iccu.sbn.SbnMarcFactory.util.UtilityDate;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import javax.swing.JTable;

/**
 * <p>Title: Interfaccia in diretta</p>
 * <p>Description: Interfaccia web per il sistema bibliotecario nazionale</p>
 * <p>Pannello analitico dei simili degli autori</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Finsiel</p>
 * @author Finsiel
 * @version 1.0
 */
public class UtilityAutori extends UtilityDate {

	public static final String IID_STRINGAVUOTA = "";
	public static final String AUT_TIPO_NOME_E = "E";
	public static final String AUT_TIPO_NOME_R = "R";
	public static final String AUT_TIPO_NOME_G = "G";

    /**
     * Creates a new UtilityAutori object.
     */
    public UtilityAutori() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param sbnMarcType DOCUMENT ME!
     * @param frame DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /**
    * Controlla se il range delle date (anno di morte e nascita)
    * sono corretti.
    *
    * @param frame frame contenitore del campo livello di autorità
    * @param annoDa range da
    * @param annoA range a
    *
    * @return true se tutto ok
    */
    public String isOkControlloAnno(String annoDa, String annoA) {
        return isOkControlloRange(annoDa, annoA);
    }

    /**
     * Usato internamente. Controlla il range di un intervallo di campi.
     *
     * @param frame frame contenitori del campo
     * @param rangeDa campo da
     * @param rangeA campo A
     *
     * @return true se tutto ok.
     */
    private String isOkControlloRange(String rangeDa,  String rangeA) {

    	String esito="";
        /* CONTROLLO SULLA ANNO ULTIMO AGGIORNAMENTO
          1)- DA MINORE O UGUALE A
          2)- SE NON ESISTE A SARA' CONSIDERATO UGUALE A DA
          3)- SE ESISTE A E NON ESISTE DA INVIARE UN MESSAGIO DI ERRORE
           "Indicare il valore di DA"
          */

        // SE SONO TUTTE VUOTE NON  FACCIO NIENTE
        if ((rangeDa.equals(IID_STRINGAVUOTA)) &&
                (rangeA.equals(IID_STRINGAVUOTA))) {
            return esito;
        } else {
            if ((!rangeA.equals(IID_STRINGAVUOTA)) &&
                    (rangeDa.equals(IID_STRINGAVUOTA))) {
            	return "ric001";
            } else {
                // PUNTO 2
                if ((rangeA.equals(IID_STRINGAVUOTA)) &&
                        (!rangeDa.equals(IID_STRINGAVUOTA))) {
                    rangeA = rangeDa;
                }

                try {
                    long annoDaInt = new Integer(rangeDa).longValue();
                    long annoAInt = new Integer(rangeA).longValue();

                    if ((annoDaInt > 9999) || (annoAInt > 9999)) {
                        return "formDataInv";
                    }

                    if (annoDaInt > annoAInt) {
                    	return "ric002";
                    } else {
                        return esito; // tutto ok !
                    }
                } catch (NumberFormatException e) {
                	return "formDataInv";
                }
            }
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNomeParteQualificazioni(String nome, String tipoNome) {
        String qualificaNome = null;

        int    index2 = nome.indexOf(" <");
        int    index3 = nome.indexOf(">");

        // PUNTEGGIATURA UGUALE A " : " SPAZIO DUE PUNTI SPAZIO,
        // controllo se c'e' uno spazio, due punti, spazio
        if (!this.isEnte(tipoNome)) {
            if (index2 != -1) {
                String[] qualifica = nome.substring(index2 + 2, index3).split(" ; ");

                // CI SONO QUALIFICAZIONI O (SPECIFICAZIONE CRONOLOGICA) !!
                int i = 1;

                // metto il primo elemento
                if (!isDatazione(qualifica[0])) {
                    qualificaNome = qualifica[0];
                } else {
                    // se il primo elemento e' una data allora metto il secondo elemento
                    if (qualifica.length >= 2) {
                        qualificaNome = qualifica[1];

                        // incremento indice
                        i++;
                    }
                }

                // SPECIFICAZIONE CRONOLOGICA
                // mentre non trovo una data)
                while (i < qualifica.length) {
                    if (!isDatazione(qualifica[i])) {
                        qualificaNome = qualificaNome + " ; " + qualifica[i];
                    }

                    i++;
                }
            }
        } else {
            if (index2 != -1) {
                qualificaNome = nome.substring(index2 + 2, index3);
            }
        }

		// Gestione qualificazioni - 03/05/2004
		if (qualificaNome != null){
			qualificaNome = "<" + qualificaNome + ">";
		}

        return qualificaNome;
    }

	public boolean isEnte(String tipoNome) {
		if (tipoNome != null) {
			if ((tipoNome.equals(AUT_TIPO_NOME_E))
					|| (tipoNome.equals(AUT_TIPO_NOME_G))
					|| (tipoNome.equals(AUT_TIPO_NOME_R))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

    /**
     * DOCUMENT ME!
     *
     * @param nome DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getNomeTipoG_b_210(String nome) {
        String[] b_210 = new String[2];
        String   ente1 = null;
        String   ente2 = null;

        // nome ente 1 <qualifica> : nome ente 2 <qualifica>     : nome ente 3 <qualifica>
        //                       index1                        index2
        int index1 = nome.indexOf(" : ");
        nome = nome.substring(index1 + 2, nome.length());

        //        System.out.println("nome restante: "+nome);
        int index2 = nome.indexOf(" : ");

        if (index2 != -1) {
            ente1     = nome.substring(0, index2);
            ente2     = nome.substring(index2 + 2, nome.length());
        } else {
            ente1     = nome.substring(0, nome.length());
            ente2     = null;
        }

        System.out.println("nome ente1: " + ente1);
        System.out.println("nome ente2: " + ente2);
        b_210[0] = getNomeStringaEnte(ente1); // NON E' MAI NULL !!

        if (ente2 != null) {
            b_210[1] = getNomeStringaEnte(ente2);
        } else {
            b_210[1] = null; // POTREBBE ESSERE NULL
        }

        return b_210;
    }
    /**
     * DOCUMENT ME!
     *
     * @param ente DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getNomeStringaEnte(String ente) {
        String nome   = null;
        int    index1 = ente.indexOf(" <");

        if (index1 != -1) {
            nome = ente.substring(0, index1);
        } else {
            nome = ente.substring(0, ente.length());
        }

        return nome;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isDatazione(String data) {
        boolean esito       = false;
        String  dueChar     = "";
        String  treChar     = "";
        String  quattroChar = "";

        if (data.length() >= 2) {
            dueChar = data.substring(0, 2);
        }

        if (data.length() >= 3) {
            treChar = data.substring(0, 3);
        }

        if (data.length() >= 4) {
            quattroChar = data.substring(0, 4);
        }

        if ((dueChar.equals("n.")) || (dueChar.equals("m."))) {
            esito = true;
        } else if ((treChar != null) && (treChar.equals("fl."))) {
            esito = true;
        } else if ((quattroChar != null) && (quattroChar.equals("sec."))) {
            esito = true;
        }

        String  REGEX = "\\d\\d\\d\\d";

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            esito = true;
        }

        return esito;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nome DOCUMENT ME!
     * @param tipoNome DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNomeParteData(String nome, String tipoNome) {
        String dataNome = null;

        // PUNTEGGIATURA UGUALE A " : " SPAZIO DUE PUNTI SPAZIO,
        // controllo se c'e' uno spazio, due punti, spazio
        if (!tipoNome.equals(AUT_TIPO_NOME_G)) {
            int index2 = nome.indexOf(" <");
            int index3 = nome.indexOf(">");

            if (index2 != -1) {
                // CI SONO QUALIFICAZIONI O (SPECIFICAZIONE CRONOLOGICA) !!
                String[] data = nome.substring(index2 + 2, index3).split(" ; ");

                // mentre non trovo una data)
                int     i        = 0;
                boolean isOkData = false;

                while ((!isOkData) && (i < data.length)) {
                    if (isDatazione(data[i])) {
                        dataNome     = data[i];
                        isOkData     = true;
                    } else {
                        i++;
                    }
                }
            }
        }

        return dataNome;
    }


    /**
     * DOCUMENT ME!
     *
     * @param frame DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOkControlloRepertori(List listaRepertori) {
        int      count = listaRepertori.size();
        boolean  esito = true;
        TabellaNumSTDImpronteVO areaRepert;
        for (int i = 0; i < count; i++) {
        	areaRepert = (TabellaNumSTDImpronteVO) listaRepertori.get(i);

            if (areaRepert.getCampoUno().trim().equals(IID_STRINGAVUOTA)) {
                esito = false;
                break;
            }
        }

        return esito;
    }

    public boolean isOkControlloRepertoriAutore(List listaRepertori) {
        int      count = listaRepertori.size();
        boolean  esito = true;
        TabellaNumSTDImpronteAggiornataVO areaRepert;
        for (int i = 0; i < count; i++) {
        	areaRepert = (TabellaNumSTDImpronteAggiornataVO) listaRepertori.get(i);

            if (areaRepert.getCampoUno().trim().equals(IID_STRINGAVUOTA)) {
                esito = false;
                break;
            }
        }

        return esito;
    }


}// end class UtilityAutori
