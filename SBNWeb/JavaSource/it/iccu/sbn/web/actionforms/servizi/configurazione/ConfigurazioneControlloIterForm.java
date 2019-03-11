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
package it.iccu.sbn.web.actionforms.servizi.configurazione;

import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ConfigurazioneControlloIterForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 1025905922154262369L;
	private boolean sessione = false;
	private boolean nuovo    = false;
	private boolean conferma = false;

	private String chiamante = "";
	private IterServizioVO iterServizio = new IterServizioVO();
	private TipoServizioVO tipoServizio = new TipoServizioVO();

	/**
	 * Mappa dei controlli associati all'attività
	 */
	private Map<String, FaseControlloIterVO> controlloIterMap;
	/**
	 * Codice del controllo selezionato per il dettaglio
	 * E' associato a un check box
	 */
	private String codControlloScelto;
	/**
	 * Lista dei codici selezionati per la cancellazione
	 * E' associato a un multi-box
	 */
	private String[] lstCodControlloScelti;
	/**
	 * Lista dei codici controllo già associati all'iter
	 */
	private List<String> lstCodControlloGiaAssociati;


	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		lstCodControlloScelti = new String[]{};
	}




	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getChiamante() {
		return chiamante;
	}

	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}

	public IterServizioVO getIterServizio() {
		return iterServizio;
	}

	public void setIterServizio(IterServizioVO iterServizio) {
		this.iterServizio = iterServizio;
	}

	public Map<String, FaseControlloIterVO> getControlloIterMap() {
		return controlloIterMap;
	}

	public void setControlloIterMap(
			Map<String, FaseControlloIterVO> controlloIterMap) {
		this.controlloIterMap = controlloIterMap;
	}

	public String getCodControlloScelto() {
		return codControlloScelto;
	}

	public void setCodControlloScelto(String codControlloScelto) {
		this.codControlloScelto = codControlloScelto;
	}


	public TipoServizioVO getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(TipoServizioVO tipiServizio) {
		this.tipoServizio = tipiServizio;
	}

	public String[] getLstCodControlloScelti() {
		return lstCodControlloScelti;
	}

	public void setLstCodControlloScelti(String[] lstCodControlloScelti) {
		this.lstCodControlloScelti = lstCodControlloScelti;
	}

	public List<String> getLstCodControlloGiaAssociati() {
		return lstCodControlloGiaAssociati;
	}

	public void setLstCodControlloGiaAssociati(
			List<String> lstCodControlloGiaAssociati) {
		this.lstCodControlloGiaAssociati = lstCodControlloGiaAssociati;
	}

}
