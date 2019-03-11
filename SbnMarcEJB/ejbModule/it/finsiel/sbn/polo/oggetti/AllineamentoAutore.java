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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.polo.orm.Tb_autore;

/**
 * Classe AllineamentoAutore
 * <p>
 * Oggetto di utilità per gestire l'allineamento di un titolo.
 * Contiene le informazioni di cosa è stato modificato relativamente al titolo.
 * </p>
 * @author
 * @author
 *
 * @version 16-giu-03
 */
public class AllineamentoAutore {
    Tb_autore autore;

    boolean tb_autore = false;
    boolean tb_autore_scambio = false;
    boolean tr_aut_aut = false;
    boolean tr_aut_mar = false;
    boolean tr_rep_aut = false;

    public AllineamentoAutore(Tb_autore autore) {
        this.autore = autore;
    }

    /**
     * Controlla se sono stati modificati i dati
     * @return true se sono avvenute modifiche dei dati
     */
    public boolean isModificatoDati() {
        return (tb_autore);
    }

    /**
     * Controlla se sono stati modificati i legami
     * @return true se sono avvenute modifiche dei legami
     */
    public boolean isModificatoLegami() {
        return (tr_aut_aut || tr_aut_mar || tr_rep_aut);
    }

    /**
     * Controlla se il titolo ha subito una cancellazione
     * @return
     */
    public boolean isCancellato() {
        if (autore.getFL_CANC().equals("S"))
            return isModificatoDati();
        return false;
    }

    public String getFlagAllinea (String vecchio_flag) {
        if (tb_autore_scambio) return "X";
        if (isCancellato()) {
            return "S";
        }
        if (tb_autore) {
            if ("R".equals(autore.getTP_FORMA_AUT())) {
                return "S";
            } else {
                if (tr_aut_aut || "R".equals(vecchio_flag) || "Z".equals(vecchio_flag)) {
                    return "Z";
                } else {
                    return "S";
                }
            }
        }
        if (tr_aut_aut) {
            //Aggiunto da Taymer, senza conferma di Daniela.(Mantis 424
            if ("R".equals(autore.getTP_FORMA_AUT()))
                return vecchio_flag;

            if ("S".equals(vecchio_flag) || "Z".equals(vecchio_flag))
                return "Z";
            else
                return "R";
        }
        return " ";
    }

    public String getFlagAllineaSbnmarc () {
        if (tr_aut_mar || tr_rep_aut) return "S";
        return " ";
    }

    /**
     * @param b
     */
    public void setTb_autore(boolean b) {
        tb_autore = b;
    }

    /**
     * @param b
     */
    public void setTb_autore_scambio(boolean b) {
        tb_autore_scambio = b;
    }

    /**
     * @param b
     */
    public void setTr_aut_aut(boolean b) {
        tr_aut_aut = b;
    }

    /**
     * @param b
     */
    public void setTr_aut_mar(boolean b) {
        tr_aut_mar = b;
    }

    /**
     * @param b
     */
    public void setTr_rep_aut(boolean b) {
        tr_rep_aut = b;
    }

    /**
     * @return
     */
    public Tb_autore getAutore() {
        return autore;
    }

}
