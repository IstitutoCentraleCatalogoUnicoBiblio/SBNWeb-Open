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

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class DettaglioSupportoForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -4295594362265659841L;
	private boolean sessione = false;
	private boolean conferma = false;
	private boolean nuovo    = false;

	private String prov_dett_supp = null;

	public String getProv_dett_supp() {
		return prov_dett_supp;
	}

	public void setProv_dett_supp(String prov_dett_supp) {
		this.prov_dett_supp = prov_dett_supp;
	}

	private SupportiModalitaErogazioneVO scelRecModErog;

	public SupportiModalitaErogazioneVO getScelRecModErog() {
		return scelRecModErog;
	}

	public void setScelRecModErog(SupportiModalitaErogazioneVO scelRecModErog) {
		this.scelRecModErog = scelRecModErog;
	}

	private String stringaMessaggioSupportiModalita;

	public String getStringaMessaggioSupportiModalita() {
		return stringaMessaggioSupportiModalita;
	}
	public void setStringaMessaggioSupportiModalita(String stringaMessaggioSupportiModalita) {
		this.stringaMessaggioSupportiModalita = stringaMessaggioSupportiModalita;
	}


	/**
	 * Codice supporto selezionato per Nuovo
	 */
	private String codiceSupporto;
	/**
	 * List per la combo dei Codici supporto
	 */
	private List<TB_CODICI> lstSupporti;
	private List<TB_CODICI> lstSvolgimento;

	private String tipoSvolgimento;
	private String updateCombo;

	private List<String> lstSupportiGiaAssegnati = new ArrayList<String>();

	//Lista dei codici erogazione
	private List<String> lstCodiciErogazione;

	public List<String> getLstCodiciErogazione() {
		return lstCodiciErogazione;
	}
	public void setLstCodiciErogazione(List<String> lstCodiciErogazione) {
		this.lstCodiciErogazione = lstCodiciErogazione;
	}

	private String  selectedModalitaErogazione;

	public String getSelectedModalitaErogazione() {
		return selectedModalitaErogazione;
	}
	public void setSelectedModalitaErogazione(String selectedModalitaErogazione) {
		this.selectedModalitaErogazione = selectedModalitaErogazione;
	}

	private SupportoBibliotecaVO supporto = new SupportoBibliotecaVO();

	/**
	 * Istanza di SupportoBibliotecaVO contenente i dati presenti sul DB.
	 * Utilizzato nel caso di aggiornamento per controllare che effettivamente
	 * siano state apportate delle modifiche rispetto ai dati presenti.
	 */
	private SupportoBibliotecaVO ultimoSalvato;


	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	//Lista delle tariffe modalità erogazione associate
	private List<SupportiModalitaErogazioneVO> lstSupportiModalitaErogazione;

	public List<SupportiModalitaErogazioneVO> getLstSupportiModalitaErogazione() {
		return lstSupportiModalitaErogazione;
	}
	public void setLstSupportiModalitaErogazione(
			List<SupportiModalitaErogazioneVO> lstSupportiModalitaErogazione) {
		this.lstSupportiModalitaErogazione = lstSupportiModalitaErogazione;
	}

	//Mappa delle tariffe modalità erogazione associate
	private Map<String, SupportiModalitaErogazioneVO> supportiModalitaErogazioneMap;

	public Map<String, SupportiModalitaErogazioneVO> getSupportiModalitaErogazioneMap() {
		return supportiModalitaErogazioneMap;
	}
	public void setSupportiModalitaErogazioneMap(
			Map<String, SupportiModalitaErogazioneVO> supportiModalitaErogazioneMap) {
		this.supportiModalitaErogazioneMap = supportiModalitaErogazioneMap;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}



	public String getCodiceSupporto() {
		return codiceSupporto;
	}



	public void setCodiceSupporto(String codiceSupporto) {
		this.codiceSupporto = codiceSupporto;
	}



	public List<TB_CODICI> getLstSupporti() {
		return lstSupporti;
	}



	public void setLstSupporti(List<TB_CODICI> lstSupporti) {
		this.lstSupporti = lstSupporti;
	}

	public List<TB_CODICI> getLstSvolgimento() {
		return lstSvolgimento;
	}

	public void setLstSvolgimento(List<TB_CODICI> lstSvolgimento) {
		this.lstSvolgimento = lstSvolgimento;
	}

	public List<String> getLstSupportiGiaAssegnati() {
		return lstSupportiGiaAssegnati;
	}



	public void setLstSupportiGiaAssegnati(List<String> lstSupportiGiaAssegnati) {
		this.lstSupportiGiaAssegnati = lstSupportiGiaAssegnati;
	}



	public SupportoBibliotecaVO getSupporto() {
		return supporto;
	}



	public void setSupporto(SupportoBibliotecaVO supporto) {
		this.supporto = supporto;
	}



	public SupportoBibliotecaVO getUltimoSalvato() {
		return ultimoSalvato;
	}



	public void setUltimoSalvato(SupportoBibliotecaVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}

	public String getTipoSvolgimento() {
		return tipoSvolgimento;
	}

	public void setTipoSvolgimento(String tipoSvolgimento) {
		this.tipoSvolgimento = tipoSvolgimento;
	}

	public String getUpdateCombo() {
		return updateCombo;
	}

	public void setUpdateCombo(String updateCombo) {
		this.updateCombo = updateCombo;
	}

}
