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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiVariazionePropostaDiCorrezioneVO extends SerializableVO {

	// = AreaDatiVariazionePropostaDiCorrezioneVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 5403796480150335931L;
	private String codErr;
	private String testoProtocollo;

	SinteticaProposteDiCorrezioneView proposteDiCorrezioneView = new SinteticaProposteDiCorrezioneView();
	private boolean modifica;

	public boolean isModifica() {
		return modifica;
	}

	public void setModifica(boolean modifica) {
		this.modifica = modifica;
	}

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public SinteticaProposteDiCorrezioneView getProposteDiCorrezioneView() {
		return proposteDiCorrezioneView;
	}

	public void setProposteDiCorrezioneView(
			SinteticaProposteDiCorrezioneView proposteDiCorrezioneView) {
		this.proposteDiCorrezioneView = proposteDiCorrezioneView;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}


	}
