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

public class ProfiloAbilitazioneVO extends BaseVO {
    /**
	 *
	 */
	private static final long serialVersionUID = 6588058101077996821L;

	private String codBib;

    private String codiceProfilo;

    private String descrizione;

    private String flagCanc;

    private String nota;

    public String getCodBib() {
        return codBib;
    }

    public void setCodBib(String codBib) {
        this.codBib = codBib;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

}
