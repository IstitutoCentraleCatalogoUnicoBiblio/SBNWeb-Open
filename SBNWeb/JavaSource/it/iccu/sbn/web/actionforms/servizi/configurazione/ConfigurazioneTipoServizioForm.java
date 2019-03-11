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
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ConfigurazioneTipoServizioForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -1653594197630969813L;
	public static final String SERVIZI = "S";
	public static final String ITER = "I";
	public static final String MODALITA = "M";
	public static final String MODULORICHIESTA = "R";

	/**
	 * Determina il folder che verrà visualizzato nella mappa relativa al menu
	 * Configurazione.<br/>
	 * <br/>
	 * Valori previsti:
	 * <ul>
	 * <li>S: Servizi</li>
	 * <li>I: Iter</li>
	 * <li>M: Modalità</li>
	 * </ul>
	 */
	private String folder = "S";
	private boolean conferma = false;
	private boolean sessione = false;
	private String prov = null;

	boolean diversoModRichiesta;

	private boolean aggiornaServizio = false;

	private List<TB_CODICI> tipiServizioILL;

	public boolean isAggiornaServizio() {
		return aggiornaServizio;
	}

	public void setAggiornaServizio(boolean aggiornaServizio) {
		this.aggiornaServizio = aggiornaServizio;
	}

	public List<TB_CODICI> getTipiServizioILL() {
		return tipiServizioILL;
	}

	public void setTipiServizioILL(List<TB_CODICI> tipiServizioILL) {
		this.tipiServizioILL = tipiServizioILL;
	}

	private boolean aggiornaModuloRichiesta = false;

	public boolean isAggiornaModuloRichiesta() {
		return aggiornaModuloRichiesta;
	}

	public void setAggiornaModuloRichiesta(boolean aggiornaModuloRichiesta) {
		this.aggiornaModuloRichiesta = aggiornaModuloRichiesta;
	}

	private String ult_supp;

	public String getUlt_supp() {
		return ult_supp;
	}

	public void setUlt_supp(String ult_supp) {
		this.ult_supp = ult_supp;
	}

	private String ult_mod;

	public String getUlt_mod() {
		return ult_mod;
	}

	public void setUlt_mod(String ult_mod) {
		this.ult_mod = ult_mod;
	}

	private String richiede_iter;

	public String getRichiede_iter() {
		return richiede_iter;
	}

	public void setRichiede_iter(String richiedeIter) {
		richiede_iter = richiedeIter;
	}

	private String visibileModuloRichiesta = null;

	public String getVisibileModuloRichiesta() {
		return visibileModuloRichiesta;
	}

	public void setVisibileModuloRichiesta(String visibileModuloRichiesta) {
		this.visibileModuloRichiesta = visibileModuloRichiesta;
	}

	private String selectedServizio;

	public String getSelectedServizio() {
		return selectedServizio;
	}

	public void setSelectedServizio(String selectedServizio) {
		this.selectedServizio = selectedServizio;
	}

	private String selectedModalitaErogazione;

	public String getSelectedModalitaErogazione() {
		return selectedModalitaErogazione;
	}

	public void setSelectedModalitaErogazione(String selectedModalitaErogazione) {
		this.selectedModalitaErogazione = selectedModalitaErogazione;
	}

	private ServizioBibliotecaVO scelRec;

	private TariffeModalitaErogazioneVO scelRecModErog;

	public ServizioBibliotecaVO getScelRec() {
		return scelRec;
	}

	public void setScelRec(ServizioBibliotecaVO scelRec) {
		this.scelRec = scelRec;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	private String polo;

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}

	private boolean ammInsUtenteParamBiblioteca;

	public boolean isAmmInsUtenteParamBiblioteca() {
		return ammInsUtenteParamBiblioteca;
	}

	public void setAmmInsUtenteParamBiblioteca(
			boolean ammInsUtenteParamBiblioteca) {
		this.ammInsUtenteParamBiblioteca = ammInsUtenteParamBiblioteca;
	}

	private String inserimentoUtente = null;

	public String getInserimentoUtente() {
		return inserimentoUtente;
	}

	public void setInserimentoUtente(String inserimentoUtente) {
		this.inserimentoUtente = inserimentoUtente;
	}

	private String stringaMessaggioIterOK = "";

	public String getStringaMessaggioIterOK() {
		return stringaMessaggioIterOK;
	}

	public void setStringaMessaggioIterOK(String stringaMessaggioIterOK) {
		this.stringaMessaggioIterOK = stringaMessaggioIterOK;
	}

	private String stringaMessaggioIterKO = "";

	public String getStringaMessaggioIterKO() {
		return stringaMessaggioIterKO;
	}

	public void setStringaMessaggioIterKO(String stringaMessaggioIterKO) {
		this.stringaMessaggioIterKO = stringaMessaggioIterKO;
	}

	private String stringaMessaggioModalitaUltSupp = "";

	public String getStringaMessaggioModalitaUltSupp() {
		return stringaMessaggioModalitaUltSupp;
	}

	public void setStringaMessaggioModalitaUltSupp(
			String stringaMessaggioModalitaUltSupp) {
		this.stringaMessaggioModalitaUltSupp = stringaMessaggioModalitaUltSupp;
	}

	private String stringaMessaggioNoIter = "";

	public String getStringaMessaggioNoIter() {
		return stringaMessaggioNoIter;
	}

	public void setStringaMessaggioNoIter(String stringaMessaggioNoIter) {
		this.stringaMessaggioNoIter = stringaMessaggioNoIter;
	}

	private String stringaMessaggioNoIterSeAncoraPresente = "";

	public String getStringaMessaggioNoIterSeAncoraPresente() {
		return stringaMessaggioNoIterSeAncoraPresente;
	}

	public void setStringaMessaggioNoIterSeAncoraPresente(
			String stringaMessaggioNoIterSeAncoraPresente) {
		this.stringaMessaggioNoIterSeAncoraPresente = stringaMessaggioNoIterSeAncoraPresente;
	}

	private String stringaMessaggioNoModalita = "";

	public String getStringaMessaggioNoModalita() {
		return stringaMessaggioNoModalita;
	}

	public void setStringaMessaggioNoModalita(String stringaMessaggioNoModalita) {
		this.stringaMessaggioNoModalita = stringaMessaggioNoModalita;
	}

	private String stringaMessaggioNoModErogSeAncoraPresente = "";

	public String getStringaMessaggioNoModErogSeAncoraPresente() {
		return stringaMessaggioNoModErogSeAncoraPresente;
	}

	public void setStringaMessaggioNoModErogSeAncoraPresente(
			String stringaMessaggioNoModErogSeAncoraPresente) {
		this.stringaMessaggioNoModErogSeAncoraPresente = stringaMessaggioNoModErogSeAncoraPresente;
	}

	private String stringaMessaggioModalitaUltMod = "";

	public String getStringaMessaggioModalitaUltMod() {
		return stringaMessaggioModalitaUltMod;
	}

	public void setStringaMessaggioModalitaUltMod(
			String stringaMessaggioModalitaUltMod) {
		this.stringaMessaggioModalitaUltMod = stringaMessaggioModalitaUltMod;
	}

	private TipoServizioVO tipoServizio = new TipoServizioVO();

	// Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di
	// aggiornamento
	// per stabilire se ci sono delle modifiche da salvare o meno, per evitare
	// di effettuare
	// un aggiornamento inutile con valori uguali a quelli precedentemente
	// inseriti.
	private TipoServizioVO ultimoSalvato;

	public TipoServizioVO getUltimoSalvato() {
		return ultimoSalvato;
	}

	public void setUltimoSalvato(TipoServizioVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}

	private List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiesti;

	public List<ServizioWebDatiRichiestiVO> getLstServizioWebDatiRichiesti() {
		return lstServizioWebDatiRichiesti;
	}

	public void setLstServizioWebDatiRichiesti(
			List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiesti) {
		this.lstServizioWebDatiRichiesti = lstServizioWebDatiRichiesti;
	}

	// Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di
	// aggiornamento
	// per stabilire se ci sono delle modifiche da salvare o meno, per evitare
	// di effettuare
	// un aggiornamento inutile con valori uguali a quelli precedentemente
	// inseriti.
	private List<ServizioWebDatiRichiestiVO> lstUltimoSalvatoModuloRichiesta;

	public List<ServizioWebDatiRichiestiVO> getLstUltimoSalvatoModuloRichiesta() {
		return lstUltimoSalvatoModuloRichiesta;
	}

	public void setLstUltimoSalvatoModuloRichiesta(
			List<ServizioWebDatiRichiestiVO> lstUltimoSalvatoModuloRichiesta) {
		this.lstUltimoSalvatoModuloRichiesta = lstUltimoSalvatoModuloRichiesta;
	}

	private boolean nuovo = false;

	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}

	public ServizioWebDatiRichiestiVO getItem(int index) {

		// automatically grow List size
		while (index >= this.getLstServizioWebDatiRichiesti().size()) {
			this.getLstServizioWebDatiRichiesti().add(
					new ServizioWebDatiRichiestiVO());
		}
		return this
				.getLstServizioWebDatiRichiesti().get(index);
	}

	// Lista de servizi associati a un Tipo Servizio
	private List<ServizioBibliotecaVO> lstServizi;
	// Mappa dei servizi associati a un Tipo Servizio
	private Map<String, ServizioBibliotecaVO> serviziMap;

	// Lista delle tariffe modalità erogazione associate
	private List<TariffeModalitaErogazioneVO> lstTariffeModalitaErogazione;
	// Mappa delle tariffe modalità erogazione associate
	private Map<String, TariffeModalitaErogazioneVO> tariffeModalitaErogazioneMap;

	// Lista degli iter servizio associati
	private List<IterServizioVO> lstIter;
	// Mappa degli iter servizio associati
	private Map<String, IterServizioVO> iterMap;

	// Lista dei codici servizio
	private List<String> lstCodiciServizio;
	// Lista dei codici erogazione
	private List<String> lstCodiciErogazione;
	// Lista dei codici attività associati
	private List<String> lstCodiciAttivita;

	// Contiene il codice del servizio scelto per la visualizzazione
	// nella mappa Configurazione Tipo Servizio
	private String codServizio;
	// Codice erogazione scelto per la visualizzazione
	private String codErog;
	// Progressivo iter scelto
	private String progrIter;

	private String[] progressiviIterSelezionati;
	private List<ServizioWebDatiRichiestiVO> listaModificati;

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		progressiviIterSelezionati = new String[] {};
		if (request.getParameter("navigation") == null)
			this.selectedServizio = null;
		//this.selectedModalitaErogazione = null;
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

	public String getCodServizio() {
		return codServizio;
	}

	public void setCodServizio(String codServizio) {
		this.codServizio = codServizio;
	}

	public List<ServizioBibliotecaVO> getLstServizi() {
		return lstServizi;
	}

	public void setLstServizi(List<ServizioBibliotecaVO> lstServizi) {
		this.lstServizi = lstServizi;
	}

	public Map<String, ServizioBibliotecaVO> getServiziMap() {
		return serviziMap;
	}

	public void setServiziMap(Map<String, ServizioBibliotecaVO> serviziMap) {
		this.serviziMap = serviziMap;
	}

	public TipoServizioVO getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(TipoServizioVO tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public List<String> getLstCodiciServizio() {
		return lstCodiciServizio;
	}

	public void setLstCodiciServizio(List<String> lstCodiciServizio) {
		this.lstCodiciServizio = lstCodiciServizio;
	}

	public List<TariffeModalitaErogazioneVO> getLstTariffeModalitaErogazione() {
		return lstTariffeModalitaErogazione;
	}

	public void setLstTariffeModalitaErogazione(
			List<TariffeModalitaErogazioneVO> lstTariffeModalitaErogazione) {
		this.lstTariffeModalitaErogazione = lstTariffeModalitaErogazione;
	}

	public Map<String, TariffeModalitaErogazioneVO> getTariffeModalitaErogazioneMap() {
		return tariffeModalitaErogazioneMap;
	}

	public void setTariffeModalitaErogazioneMap(
			Map<String, TariffeModalitaErogazioneVO> tariffeModalitaErogazioneMap) {
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

	public List<IterServizioVO> getLstIter() {
		return lstIter;
	}

	public void setLstIter(List<IterServizioVO> lstIter) {
		this.lstIter = lstIter;
	}

	public Map<String, IterServizioVO> getIterMap() {
		return iterMap;
	}

	public void setIterMap(Map<String, IterServizioVO> iterMap) {
		this.iterMap = iterMap;
	}

	public String getProgrIter() {
		return progrIter;
	}

	public void setProgrIter(String progrIter) {
		this.progrIter = progrIter;
	}

	public List<String> getLstCodiciAttivita() {
		return lstCodiciAttivita;
	}

	public void setLstCodiciAttivita(List<String> lstCodiciAttivita) {
		this.lstCodiciAttivita = lstCodiciAttivita;
	}

	public String[] getProgressiviIterSelezionati() {
		return progressiviIterSelezionati;
	}

	public void setProgressiviIterSelezionati(
			String[] progressiviIterSelezionati) {
		this.progressiviIterSelezionati = progressiviIterSelezionati;
	}

	public TariffeModalitaErogazioneVO getScelRecModErog() {
		return scelRecModErog;
	}

	public void setScelRecModErog(TariffeModalitaErogazioneVO scelRecModErog) {
		this.scelRecModErog = scelRecModErog;
	}

	public void setLstModificati(
			List<ServizioWebDatiRichiestiVO> listaModificati) {
		this.listaModificati = listaModificati;
		// TODO Auto-generated method stub

	}

	public List<ServizioWebDatiRichiestiVO> getLstModificati() {
		return listaModificati;
	}

	public boolean isDiversoModRichiesta() {
		return diversoModRichiesta;
	}

	public void setDiversoModRichiesta(boolean diversoModRichiesta) {
		this.diversoModRichiesta = diversoModRichiesta;
	}

}
