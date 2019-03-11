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

import it.finsiel.sbn.polo.orm.Tb_marca;

/**
 * Classe AllineamentoMarca
 * <p>
 * Oggetto di utilità per gestire l'allineamento di una marca.
 * Contiene le informazioni di cosa è stato modificato relativamente alla marca.
 * </p>
 * @author
 * @author
 *
 * @version 29-lug-2003
 */
public class AllineamentoMarca {

    Tb_marca marca;

    boolean tb_marca = false;
    boolean legami = false;

    public AllineamentoMarca(Tb_marca marca) {
        this.marca = marca;
    }

    /**
     * Controlla se sono stati modificati i dati
     * @return true se sono avvenute modifiche dei dati
     */
    public boolean isModificatoDati() {
        return (tb_marca);
    }

    /**
     * Controlla se sono stati modificati i legami
     * @return true se sono avvenute modifiche dei legami
     */
    public boolean isModificatoLegami() {
        return (legami);
    }

    /**
     * Controlla se il titolo ha subito una cancellazione
     * @return
     */
    public boolean isCancellato() {
        if (marca.getFL_CANC().equals("S"))
            return isModificatoDati();
        return false;
    }

    public String getFlagAllinea () {
        if (tb_marca) return "S";
        return " ";
    }

    public String getFlagAllineaSbnmarc () {
        if (legami) return "S";
        return " ";
    }

    /**
     * @param b
     */
    public void setTb_marca(boolean b) {
        tb_marca = b;
    }


    /**
     * @param b
     */
    public void setLegami(boolean b) {
        legami = b;
    }

    /**
     * @return
     */
    public Tb_marca getMarca() {
        return marca;
    }

}
