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
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;



public class DettaglioModalitaErogazioneForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 8571855165648809287L;
	private String  chiamante;
	private boolean conferma = false;
	private boolean sessione = false;
	private boolean nuovo    = false;
	//Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di aggiornamento
	//per stabilire se ci sono delle modifiche da salvare o meno, per evitare di effettuare
	//un aggiornamento inutile con valori uguali a quelli precedentemente inseriti.
	private ModalitaErogazioneVO ultimoSalvato;

	private ModalitaErogazioneVO tariffaModalitaErogazione = new ModalitaErogazioneVO();

	//Deve essere sempre valorizzato, sia nel caso di Esamina che nel caso di Nuovo
	private String             codiceTipoServizio;
	//Deve essere sempre valorizzato, sia nel caso di Esamina che nel caso di Nuovo
	private String             desTipoServizio;
	//Deve essere sempre valorizzato
	private int                idTipoServizio;

	//Lista dei codici erogazione già associati al tipo servizio
	private List<String>         lstCodiciErogazioneAssociati;
	//Lista dei codici erogazione tra i quali scegliere nel caso di "Aggiungi nuova modalità"
	private List<TB_CODICI> lstModalitaErogazione;

	private List<TB_CODICI> lstSvolgimento;

	private String tipoSvolgimento;
	private String updateCombo;


	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
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
	public ModalitaErogazioneVO getUltimoSalvato() {
		return ultimoSalvato;
	}
	public void setUltimoSalvato(ModalitaErogazioneVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}
	public String getCodiceTipoServizio() {
		return codiceTipoServizio;
	}
	public void setCodiceTipoServizio(String codiceTipoServizio) {
		this.codiceTipoServizio = codiceTipoServizio;
	}
	public String getDesTipoServizio() {
		return desTipoServizio;
	}
	public void setDesTipoServizio(String desTipoServizio) {
		this.desTipoServizio = desTipoServizio;
	}
	public int getIdTipoServizio() {
		return idTipoServizio;
	}
	public void setIdTipoServizio(int idTipoServizio) {
		this.idTipoServizio = idTipoServizio;
	}
	public ModalitaErogazioneVO getTariffaModalitaErogazione() {
		return tariffaModalitaErogazione;
	}
	public void setTariffaModalitaErogazione(
			ModalitaErogazioneVO tariffaModalitaErogazione) {
		this.tariffaModalitaErogazione = tariffaModalitaErogazione;
	}
	public List<String> getLstCodiciErogazioneAssociati() {
		return lstCodiciErogazioneAssociati;
	}
	public void setLstCodiciErogazioneAssociati(
			List<String> lstCodiciErogazioneAssociati) {
		this.lstCodiciErogazioneAssociati = lstCodiciErogazioneAssociati;
	}
	public List<TB_CODICI> getLstModalitaErogazione() {
		return lstModalitaErogazione;
	}
	public void setLstModalitaErogazione(List<TB_CODICI> lstModalitaErogazione) {
		this.lstModalitaErogazione = lstModalitaErogazione;
	}
	public List<TB_CODICI> getLstSvolgimento() {
		return lstSvolgimento;
	}
	public void setLstSvolgimento(List<TB_CODICI> lstSvolgimento) {
		this.lstSvolgimento = lstSvolgimento;
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
