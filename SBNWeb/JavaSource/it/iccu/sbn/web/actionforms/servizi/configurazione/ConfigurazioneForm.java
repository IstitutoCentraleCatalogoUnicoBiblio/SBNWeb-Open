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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

/**
 * ConfigurazioneRicercaForm.java 13/nov/07
 *
 */
public class ConfigurazioneForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -8916997221413241261L;
	public static final String TIPI_SERVIZIO = "T";
	public static final String SUPPORTI = "S";
	public static final String PARAMETRI_BIBLIOTECA = "P";
	public static final String MODALITA_PAGAMENTO = "M";
	public static final String MODALITA_EROGAZIONE = "E";

	/**
	 * Determina il folder che verrà visualizzato nella mappa relativa al menu
	 * Configurazione.<br/>
	 * <br/>
	 * Valori previsti:
	 * <ul>
	 * <li>T: Tipi servizio</li>
	 * <li>M: Modalità erogazione</li> *
	 * <li>S: Supporti</li>
	 * <li>P: Parametri biblioteca</li>
	 * <li>M: Modalità pagamento</li>
	 * </ul>
	 */
	private String folder;
	private boolean conferma = false;
	private boolean sessione = false;
	private boolean nuovo = false;

	private String prov = null;

	private List<TB_CODICI> listaTipoRinnovo;

	private List<AutorizzazioneVO> listaTipoAutorizzazione;

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	private String cambiato = null;

	public String getCambiato() {
		return cambiato;
	}

	public void setCambiato(String cambiato) {
		this.cambiato = cambiato;
	}

	private String inserimentoUtente = null;

	public String getInserimentoUtente() {
		return inserimentoUtente;
	}

	public void setInserimentoUtente(String inserimentoUtente) {
		this.inserimentoUtente = inserimentoUtente;
	}

	private TipoServizioVO tipoServizio = new TipoServizioVO();

	// Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di
	// aggiornamento
	// per stabilire se ci sono delle modifiche da salvare o meno, per evitare
	// di effettuare
	// un aggiornamento inutile con valori uguali a quelli precedentemente
	// inseriti.
	private TipoServizioVO ultimoSalvato;

	public TipoServizioVO getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(TipoServizioVO tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	private String stringaMessaggioServizioModalitaUltMod = "";

	public String getStringaMessaggioServizioModalitaUltMod() {
		return stringaMessaggioServizioModalitaUltMod;
	}

	public void setStringaMessaggioServizioModalitaUltMod(
			String stringaMessaggioServizioModalitaUltMod) {
		this.stringaMessaggioServizioModalitaUltMod = stringaMessaggioServizioModalitaUltMod;
	}

	private String stringaMessaggioServizioSupportiUltSupp = "";

	public String getStringaMessaggioServizioSupportiUltSupp() {
		return stringaMessaggioServizioSupportiUltSupp;
	}

	public void setStringaMessaggioServizioSupportiUltSupp(
			String stringaMessaggioServizioSupportiUltSupp) {
		this.stringaMessaggioServizioSupportiUltSupp = stringaMessaggioServizioSupportiUltSupp;
	}

	private String selectedTipoServizio;

	public String getSelectedTipoServizio() {
		return selectedTipoServizio;
	}

	public void setSelectedTipoServizio(String selectedTipoServizio) {
		this.selectedTipoServizio = selectedTipoServizio;
	}

	/**
	 * codice tipo servizio selezionato
	 */
	private String codTipoServizio;

	// Codice erogazione scelto per la visualizzazione
	private String codErog;

	/**
	 * List per la combo dei tipi servizio
	 */
	private List<TipoServizioVO> lstTipiServizio;

	// Lista delle tariffe modalità erogazione associate
	private List<ModalitaErogazioneVO> lstTariffeModalitaErogazione;
	// Mappa delle tariffe modalità erogazione associate
	private Map<String, ModalitaErogazioneVO> tariffeModalitaErogazioneMap;

	// Lista dei codici erogazione
	private List<String> lstCodiciErogazione;

	private ModalitaErogazioneVO scelRecModErog;

	public ModalitaErogazioneVO getScelRecModErog() {
		return scelRecModErog;
	}

	public void setScelRecModErog(ModalitaErogazioneVO scelRecModErog) {
		this.scelRecModErog = scelRecModErog;
	}

	public List<ModalitaErogazioneVO> getLstTariffeModalitaErogazione() {
		return lstTariffeModalitaErogazione;
	}

	public void setLstTariffeModalitaErogazione(
			List<ModalitaErogazioneVO> lstTariffeModalitaErogazione) {
		this.lstTariffeModalitaErogazione = lstTariffeModalitaErogazione;
	}

	public Map<String, ModalitaErogazioneVO> getTariffeModalitaErogazioneMap() {
		return tariffeModalitaErogazioneMap;
	}

	public void setTariffeModalitaErogazioneMap(
			Map<String, ModalitaErogazioneVO> tariffeModalitaErogazioneMap) {
		this.tariffeModalitaErogazioneMap = tariffeModalitaErogazioneMap;
	}

	public String getCodErog() {
		return codErog;
	}

	public void setCodErog(String codErog) {
		this.codErog = codErog;
	}

	public List<String> getLstCodiciErogazione() {
		return lstCodiciErogazione;
	}

	public void setLstCodiciErogazione(List<String> lstCodiciErogazione) {
		this.lstCodiciErogazione = lstCodiciErogazione;
	}

	private String selectedModalitaErogazione;

	public String getSelectedModalitaErogazione() {
		return selectedModalitaErogazione;
	}

	public void setSelectedModalitaErogazione(String selectedModalitaErogazione) {
		this.selectedModalitaErogazione = selectedModalitaErogazione;
	}

	/**
	 * List per la combo delle Categorie di Fruizione
	 */
	private List<ComboCodDescVO> lstCatFruizione;

	/**
	 * List per la combo delle Categorie di Riproduzione
	 */
	private List<ComboCodDescVO> lstCatRiproduzione;

	/**
	 * List per la combo del umero massimo di solleciti
	 */
	private List<ComboCodDescVO> lstNumMaxSolleciti;

	public List<ComboCodDescVO> getLstNumMaxSolleciti() {
		return lstNumMaxSolleciti;
	}

	public void setLstNumMaxSolleciti(List<ComboCodDescVO> lstNumMaxSolleciti) {
		this.lstNumMaxSolleciti = lstNumMaxSolleciti;
	}

	/**
	 * List per la combo del umero massimo di solleciti
	 */
	private List<ComboCodDescVO> lstModalitaInvio;

	public List<ComboCodDescVO> getLstModalitaInvio() {
		return lstModalitaInvio;
	}

	public void setLstModalitaInvio(List<ComboCodDescVO> lstModalitaInvio) {
		this.lstModalitaInvio = lstModalitaInvio;
	}

	/**
	 * Codici suporto selezionati per la cancellazione. E' associato a un
	 * multibox
	 */
	private String[] supportiSelezionati;
	/**
	 * Codice supporto selezionato per Esamina
	 */
	private String codiceSupporto;

	/**
	 * Mappa supporti associati alla biblioteca
	 */
	private Map<String, SupportoBibliotecaVO> supportiBiblioteca = new HashMap<String, SupportoBibliotecaVO>();

	/**
	 * Lista dei codici supporti associati alla biblioteca
	 */
	private List<String> codiciSupportiGiaAssociati;

	/**
	 * Lista di istanze di ModalitaPagamentoVO relative alle modalita pagamento
	 * associate alla biblioteca
	 */
	private List<ModalitaPagamentoVO> modalitaPagamento = new ArrayList<ModalitaPagamentoVO>();
	/**
	 * Lista dei codici delle modalità pagamento associate alla biblioteca
	 */
	private List<Short> codiciModalitaPagamentoAssociati;
	/**
	 * Se true vuol dire che si è cliccato sul bottone NUOVO per inserire una
	 * nuova modalità di pagamento
	 */
	private boolean aggiungiModalita = false;

	private ModalitaPagamentoVO modalitaVO = new ModalitaPagamentoVO();

	/**
	 * Modalità selezionate per la cancellazione. E' associato a un multibox
	 */
	private String[] modalitaSelezionate;
	private List<Mediazione> listaCatMediazione;

	private String polo;

	private ParametriBibliotecaVO parametriBib = new ParametriBibliotecaVO();
	/**
	 * Istanza relativa agli ultimi dati inseriti sul server. Serve per esere
	 * confrontata in caso di aggiornamento con i dati del form (con l'attributo
	 * parametriBib) per decidere se sono state fatte delle modifihe o no.
	 */
	private ParametriBibliotecaVO ultimiParametriSalvati;

	private boolean verificaAutoregistrazione;

	public boolean isVerificaAutoregistrazione() {
		return verificaAutoregistrazione;
	}

	public void setVerificaAutoregistrazione(boolean verificaAutoregistrazione) {
		this.verificaAutoregistrazione = verificaAutoregistrazione;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		supportiSelezionati = new String[] {};
		// modalitaSelezionate = new String[] {};
		//if (request.getParameter("navigation") == null)
			//this.selectedTipoServizio=null;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
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

	public String getCodTipoServizio() {
		return codTipoServizio;
	}

	public void setCodTipoServizio(String codTipoServizio) {
		this.codTipoServizio = codTipoServizio;
	}

	public List<TipoServizioVO> getLstTipiServizio() {
		return lstTipiServizio;
	}

	public void setLstTipiServizio(List<TipoServizioVO> lstTipiServizio) {
		this.lstTipiServizio = lstTipiServizio;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}

	public boolean esistonoTipiServizio() {
		return (this.lstTipiServizio.size() > 0);
	}

	public ParametriBibliotecaVO getParametriBib() {
		return parametriBib;
	}

	public void setParametriBib(ParametriBibliotecaVO parametriBib) {
		this.parametriBib = parametriBib;
	}

	public ParametriBibliotecaVO getUltimiParametriSalvati() {
		return ultimiParametriSalvati;
	}

	public void setUltimiParametriSalvati(
			ParametriBibliotecaVO ultimiParametriSalvati) {
		this.ultimiParametriSalvati = ultimiParametriSalvati;
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

	public Map<String, SupportoBibliotecaVO> getSupportiBiblioteca() {
		return supportiBiblioteca;
	}

	public void setSupportiBiblioteca(
			Map<String, SupportoBibliotecaVO> supportiBiblioteca) {
		this.supportiBiblioteca = supportiBiblioteca;
	}

	public String[] getSupportiSelezionati() {
		return supportiSelezionati;
	}

	public void setSupportiSelezionati(String[] supportiSelezionati) {
		this.supportiSelezionati = supportiSelezionati;
	}

	public List<String> getCodiciSupportiGiaAssociati() {
		return codiciSupportiGiaAssociati;
	}

	public void setCodiciSupportiGiaAssociati(
			List<String> codiciSupportiGiaAssociati) {
		this.codiciSupportiGiaAssociati = codiciSupportiGiaAssociati;
	}

	public List<ModalitaPagamentoVO> getModalitaPagamento() {
		return modalitaPagamento;
	}

	public void setModalitaPagamento(List<ModalitaPagamentoVO> modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}

	public boolean isAggiungiModalita() {
		return aggiungiModalita;
	}

	public void setAggiungiModalita(boolean aggiungiModalita) {
		this.aggiungiModalita = aggiungiModalita;
	}

	public ModalitaPagamentoVO getModalitaVO() {
		return modalitaVO;
	}

	public void setModalitaVO(ModalitaPagamentoVO modalitaVO) {
		this.modalitaVO = modalitaVO;
	}

	public List<Short> getCodiciModalitaPagamentoAssociati() {
		return codiciModalitaPagamentoAssociati;
	}

	public void setCodiciModalitaPagamentoAssociati(
			List<Short> codiciModalitaPagamentoAssociati) {
		this.codiciModalitaPagamentoAssociati = codiciModalitaPagamentoAssociati;
	}

	public List<Mediazione> getListaCatMediazione() {
		return this.listaCatMediazione;
	}

	public void setListaCatMediazione(List<Mediazione> listaCatMediazione) {
		this.listaCatMediazione = listaCatMediazione;
	}

	public String[] getModalitaSelezionate() {
		return modalitaSelezionate;
	}

	public void setModalitaSelezionate(String[] modalitaSelezionate) {
		this.modalitaSelezionate = modalitaSelezionate;
	}

	public List<ComboCodDescVO> getLstCatFruizione() {
		return lstCatFruizione;
	}

	public void setLstCatFruizione(List<ComboCodDescVO> lstCatFruizione) {
		this.lstCatFruizione = lstCatFruizione;
	}

	public List<ComboCodDescVO> getLstCatRiproduzione() {
		return lstCatRiproduzione;
	}

	public void setLstCatRiproduzione(List<ComboCodDescVO> lstCatRiproduzione) {
		this.lstCatRiproduzione = lstCatRiproduzione;
	}

	public List<TB_CODICI> getListaTipoRinnovo() {
		return listaTipoRinnovo;
	}

	public void setListaTipoRinnovo(List<TB_CODICI> listaTipoRinnovo) {
		this.listaTipoRinnovo = listaTipoRinnovo;
	}

	public List<AutorizzazioneVO> getListaTipoAutorizzazione() {
		return listaTipoAutorizzazione;
	}

	public void setListaTipoAutorizzazione(List<AutorizzazioneVO> listaTipoAutorizzazione) {
		this.listaTipoAutorizzazione = listaTipoAutorizzazione;
	}

}
