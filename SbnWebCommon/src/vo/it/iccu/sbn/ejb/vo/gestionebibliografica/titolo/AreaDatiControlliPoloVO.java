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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiControlliPoloVO  extends SerializableVO {

	// = AreaDatiControlliPoloVO.class.hashCode();


	/**
	 *
	 */
	private static final long serialVersionUID = -4199017181098503314L;
	private String idRicerca;
	private boolean cancellareInferiori;
	private String tipoAut;
	private String formaNome;
	private String timeStampRinvio;
	private DatiDocType datiDocType;
	private DatiElementoType datiElementoType;
	private TitAccessoType titAccessoType;
	private ElementAutType elementAutType;


	private String codErr;
	private String testoProtocollo;

	public String getCodErr() {
		return codErr;
	}
	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}
	public DatiDocType getDatiDocType() {
		return datiDocType;
	}
	public void setDatiDocType(DatiDocType datiDocType) {
		this.datiDocType = datiDocType;
	}
	public String getIdRicerca() {
		return idRicerca;
	}
	public void setIdRicerca(String idRicerca) {
		this.idRicerca = idRicerca;
	}
	public String getTestoProtocollo() {
		return testoProtocollo;
	}
	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}
	public boolean isCancellareInferiori() {
		return cancellareInferiori;
	}
	public void setCancellareInferiori(boolean cancellareInferiori) {
		this.cancellareInferiori = cancellareInferiori;
	}
	public DatiElementoType getDatiElementoType() {
		return datiElementoType;
	}
	public void setDatiElementoType(DatiElementoType datiElementoType) {
		this.datiElementoType = datiElementoType;
	}
	public TitAccessoType getTitAccessoType() {
		return titAccessoType;
	}
	public void setTitAccessoType(TitAccessoType titAccessoType) {
		this.titAccessoType = titAccessoType;
	}
	public String getTipoAut() {
		return tipoAut;
	}
	public void setTipoAut(String tipoAut) {
		this.tipoAut = tipoAut;
	}
	public String getFormaNome() {
		return formaNome;
	}
	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}
	public String getTimeStampRinvio() {
		return timeStampRinvio;
	}
	public void setTimeStampRinvio(String timeStampRinvio) {
		this.timeStampRinvio = timeStampRinvio;
	}
	public ElementAutType getElementAutType() {
		return elementAutType;
	}
	public void setElementAutType(ElementAutType elementAutType) {
		this.elementAutType = elementAutType;
	}

}
