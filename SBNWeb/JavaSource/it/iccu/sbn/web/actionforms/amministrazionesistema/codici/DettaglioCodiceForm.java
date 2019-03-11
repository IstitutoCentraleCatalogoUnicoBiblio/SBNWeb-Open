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
package it.iccu.sbn.web.actionforms.amministrazionesistema.codici;

import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class DettaglioCodiceForm extends ActionForm {


	private static final long serialVersionUID = -7526460320004401614L;
	private boolean initialized = false;
	private int dettaglioBloccoCorrente;
	private boolean dettaglioAbilitaBlocchi;
	private int dettaglioMaxRighe = 200000;
	private int dettaglioTotRighe;
	private String dettaglioIdLista = "";

	private int dettaglioTotBlocchi;
	private String dettaglioSelezRadio;

	private CodiceConfigVO configCodici;
	private List<CodiceVO> dettaglioElencoCodici = new ArrayList<CodiceVO>();

	private String dettaglioOrdinamento = "";

	private String codice = "";
	private String descrizione = "";
	private String dataAttivazione = "";
	private String abilitatoWrite = "FALSE";

	private String flagNuovo = "FALSE";
	private String flagModifica = "FALSE";

	private String[] pulsanti;
	private boolean root;
	private Boolean authorized = null;

	public String getFlagNuovo() {
		return flagNuovo;
	}

	public void setFlagNuovo(String flagNuovo) {
		this.flagNuovo = flagNuovo;
	}

	public String getFlagModifica() {
		return flagModifica;
	}

	public void setFlagModifica(String flagModifica) {
		this.flagModifica = flagModifica;
	}

	public String getAbilitatoWrite() {
		return abilitatoWrite;
	}

	public void setAbilitatoWrite(String abilitatoWrite) {
		this.abilitatoWrite = abilitatoWrite;
	}

	public int getDettaglioBloccoCorrente() {
		return dettaglioBloccoCorrente;
	}

	public void setDettaglioBloccoCorrente(int dettaglioBloccoCorrente) {
		this.dettaglioBloccoCorrente = dettaglioBloccoCorrente;
	}

	public boolean isDettaglioAbilitaBlocchi() {
		return dettaglioAbilitaBlocchi;
	}

	public void setDettaglioAbilitaBlocchi(boolean dettaglioAbilitaBlocchi) {
		this.dettaglioAbilitaBlocchi = dettaglioAbilitaBlocchi;
	}

	public int getDettaglioMaxRighe() {
		return dettaglioMaxRighe;
	}

	public void setDettaglioMaxRighe(int dettaglioMaxRighe) {
		this.dettaglioMaxRighe = dettaglioMaxRighe;
	}

	public int getDettaglioTotRighe() {
		return dettaglioTotRighe;
	}

	public void setDettaglioTotRighe(int dettaglioTotRighe) {
		this.dettaglioTotRighe = dettaglioTotRighe;
	}

	public String getDettaglioIdLista() {
		return dettaglioIdLista;
	}

	public void setDettaglioIdLista(String dettaglioIdLista) {
		this.dettaglioIdLista = dettaglioIdLista;
	}

	public int getDettaglioTotBlocchi() {
		return dettaglioTotBlocchi;
	}

	public void setDettaglioTotBlocchi(int dettaglioTotBlocchi) {
		this.dettaglioTotBlocchi = dettaglioTotBlocchi;
	}

	public String getDettaglioSelezRadio() {
		return dettaglioSelezRadio;
	}

	public void setDettaglioSelezRadio(String dettaglioSelezRadio) {
		this.dettaglioSelezRadio = dettaglioSelezRadio;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDataAttivazione() {
		return dataAttivazione;
	}

	public void setDataAttivazione(String dataAttivazione) {
		this.dataAttivazione = dataAttivazione;
	}

	public String getDettaglioOrdinamento() {
		return dettaglioOrdinamento;
	}

	public void setDettaglioOrdinamento(String dettaglioOrdinamento) {
		this.dettaglioOrdinamento = dettaglioOrdinamento;
	}

	public CodiceConfigVO getConfigCodici() {
		return configCodici;
	}

	public void setConfigCodici(CodiceConfigVO configCodici) {
		this.configCodici = configCodici;
	}

	public void setDettaglioElencoCodici(List<CodiceVO> dettaglioElencoCodici) {
		this.dettaglioElencoCodici = dettaglioElencoCodici;
	}

	public List<CodiceVO> getDettaglioElencoCodici() {
		return dettaglioElencoCodici;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public boolean isRoot() {
		return root;
	}

	public Boolean getAuthorized() {
		return authorized;
	}

	public void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}

}
