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
package it.iccu.sbn.ejb.vo.amministrazionesistema.sif;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.constant.ConstantDefault;

public abstract class SIFListaBibliotecheBaseVO extends SerializableVO {

	private static final long serialVersionUID = 8037755790332412163L;
	protected String codPolo;
	protected String codBib;
	protected String requestAttribute;
	protected int elementiPerBlocco = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	protected boolean multiSelezione;
	protected String testoNavigazione = null;

	protected Integer[] selected;

	public Integer[] getSelected() {
		return selected;
	}

	public void setSelected(Integer[] selected) {
		this.selected = selected;
	}

	public SIFListaBibliotecheBaseVO() {
		super();
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public int getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public void setElementiPerBlocco(int elementiPerBlocco) {
		this.elementiPerBlocco = elementiPerBlocco;
	}

	public String getRequestAttribute() {
		return requestAttribute;
	}

	public void setRequestAttribute(String requestAttribute) {
		this.requestAttribute = requestAttribute;
	}

	public boolean isMultiSelezione() {
		return multiSelezione;
	}

	public void setMultiSelezione(boolean multiSelezione) {
		this.multiSelezione = multiSelezione;
	}

	public String getTestoNavigazione() {
		return testoNavigazione;
	}

	public void setTestoNavigazione(String testoNavigazione) {
		this.testoNavigazione = testoNavigazione;
	}

}
