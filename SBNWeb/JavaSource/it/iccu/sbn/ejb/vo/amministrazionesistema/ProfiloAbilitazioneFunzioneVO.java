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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.vo.BaseVO;

public class ProfiloAbilitazioneFunzioneVO extends BaseVO {
    /**
	 *
	 */
	private static final long serialVersionUID = 6257783990979367795L;

	private String codBib;

    private String codiceProfilo;

    private String codiceBibliotecaFunzione;

    private String codiceFunzione;

    private String flagCanc;

    public String getCodBib() {
        return codBib;
    }

    public void setCodBib(String codBib) {
        this.codBib = codBib;
    }

    public String getCodiceBibliotecaFunzione() {
        return codiceBibliotecaFunzione;
    }

    public void setCodiceBibliotecaFunzione(String codiceBibliotecaFunzione) {
        this.codiceBibliotecaFunzione = codiceBibliotecaFunzione;
    }

    public String getCodiceFunzione() {
        return codiceFunzione;
    }

    public void setCodiceFunzione(String codiceFunzione) {
        this.codiceFunzione = codiceFunzione;
    }

    public String getCodiceProfilo() {
        return codiceProfilo;
    }

    public void setCodiceProfilo(String codiceProfilo) {
        this.codiceProfilo = codiceProfilo;
    }

    public String getFlagCanc() {
        return flagCanc;
    }

    public void setFlagCanc(String flagCanc) {
        this.flagCanc = flagCanc;
    }



}
