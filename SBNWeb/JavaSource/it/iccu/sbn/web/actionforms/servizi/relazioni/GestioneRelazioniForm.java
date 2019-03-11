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
package it.iccu.sbn.web.actionforms.servizi.relazioni;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.relazioni.RelazioniVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class GestioneRelazioniForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 5130727779612675169L;
	// costanti per la definizione dei possibili step nella gestione delle
	// relazioni
	public static final short SCELTA_RELAZIONE = 1;
	public static final short GESTIONE_RELAZIONI = 2;
	public static final short AGGIUNGI_MODIFICA_RELAZIONE = 3;

	// costanti indicanti i possibili chiamanti
	public static final short NESSUN_CHIAMANTE = 0;
	public static final short TIPO_SERVIZIO_CATEGORIE_FRUIZIONE = 1;
	public static final short TIPO_SERVIZIO_MODALITA_EROGAZIONE = 2;
	public static final short TIPO_SUPPORTO_MODALITA_EROGAZIONE = 3;

	// costanti indicanti il tipo di codiceRelazione scelto
	public static final String TSCF = CodiciType.CODICE_TIPO_SERVIZIO_CATEGORIA_FRUIZIONE.getTp_Tabella();
	public static final String TSME = CodiciType.CODICE_TIPO_SERVIZIO_MOD_EROGAZIONE.getTp_Tabella();
	public static final String SUME = CodiciType.CODICE_TIPO_SUPPORTO_MODALITA_EROGAZIONE.getTp_Tabella();

	// costanti relative al campo tabella_di_relazione della tabella
	// Trl_relazioni_servizi
	public static final String TIPO_SERVIZIO = "tbl_tipo_servizio";
	public static final String TIPO_SUPPORTO = "tbl_supporti_biblioteca";

	private boolean conferma = false;
	private boolean sessione = false;
	private short step = GestioneRelazioniForm.SCELTA_RELAZIONE;
	private String richiesta = "";

	/**
	 * Lista delle possibili relazioni da poter configurare Codice tabella in
	 * tb_codici = "LTRE"
	 */
	private List<ComboCodDescVO> lstRelazioni;
	/**
	 * codice della relazione selezionata
	 */
	private String codiceRelazione;

	private List<TipoServizioVO> lstTipiServizio;
	private int idTipoServizio;

	private List<SupportoBibliotecaVO> lstTipiSupporto;
	private int idTipoSupporto;

	private List<ComboCodDescVO> lstModalitaErogazione;
	private String codModalitaErogazione;

	private List<ComboCodDescVO> lstCategorieFruizione;
	private String codFruizione;

	private List<RelazioniVO> lstRelazioniCaricate;
	private short progrRelazioneScelta;
	private Integer[] idSelMultipla;
	private Integer[] idSelMultiplaConferma;

	private RelazioniVO relazione = new RelazioniVO();

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

	public List<ComboCodDescVO> getLstRelazioni() {
		return lstRelazioni;
	}

	public void setLstRelazioni(List<ComboCodDescVO> lstRelazioni) {
		this.lstRelazioni = lstRelazioni;
	}

	public String getCodiceRelazione() {
		return codiceRelazione;
	}

	public void setCodiceRelazione(String codiceRelazione) {
		this.codiceRelazione = codiceRelazione;
	}

	public final short getSceltaRelazione() {
		return GestioneRelazioniForm.SCELTA_RELAZIONE;
	}

	public final short getGestioneRelazioni() {
		return GestioneRelazioniForm.GESTIONE_RELAZIONI;
	}

	public final short getAggiungiModificaRelazione() {
		return GestioneRelazioniForm.AGGIUNGI_MODIFICA_RELAZIONE;
	}

	public short getStep() {
		return step;
	}

	public void setStep(short step) {
		this.step = step;
	}

	public List<TipoServizioVO> getLstTipiServizio() {
		return lstTipiServizio;
	}

	public void setLstTipiServizio(List<TipoServizioVO> lstTipiServizio) {
		this.lstTipiServizio = lstTipiServizio;
	}

	public int getIdTipoServizio() {
		return idTipoServizio;
	}

	public void setIdTipoServizio(int codTipoServizio) {
		this.idTipoServizio = codTipoServizio;
	}

	public List<SupportoBibliotecaVO> getLstTipiSupporto() {
		return lstTipiSupporto;
	}

	public void setLstTipiSupporto(List<SupportoBibliotecaVO> lstTipiSupporto) {
		this.lstTipiSupporto = lstTipiSupporto;
	}

	public int getIdTipoSupporto() {
		return idTipoSupporto;
	}

	public void setIdTipoSupporto(int codTipoSupporto) {
		this.idTipoSupporto = codTipoSupporto;
	}

	public List<ComboCodDescVO> getLstModalitaErogazione() {
		return lstModalitaErogazione;
	}

	public void setLstModalitaErogazione(
			List<ComboCodDescVO> lstModalitaErogazione) {
		this.lstModalitaErogazione = lstModalitaErogazione;
	}

	public String getCodModalitaErogazione() {
		return codModalitaErogazione;
	}

	public void setCodModalitaErogazione(String codModalitaErogazione) {
		this.codModalitaErogazione = codModalitaErogazione;
	}

	public List<ComboCodDescVO> getLstCategorieFruizione() {
		return lstCategorieFruizione;
	}

	public void setLstCategorieFruizione(
			List<ComboCodDescVO> lstCategorieFruizione) {
		this.lstCategorieFruizione = lstCategorieFruizione;
	}

	public String getCodFruizione() {
		return codFruizione;
	}

	public void setCodFruizione(String codFruizione) {
		this.codFruizione = codFruizione;
	}

	public List<RelazioniVO> getLstRelazioniCaricate() {
		return lstRelazioniCaricate;
	}

	public void setLstRelazioniCaricate(List<RelazioniVO> lstRelazioniCaricate) {
		this.lstRelazioniCaricate = lstRelazioniCaricate;
	}

	public String getTipoServizioCategoriaFruizione() {
		return GestioneRelazioniForm.TSCF;
	}

	public String getTipoServizioModalitaErogazione() {
		return GestioneRelazioniForm.TSME;
	}

	public String getTipoSupportoModalitaErogazione() {
		return GestioneRelazioniForm.SUME;
	}

	public short getProgrRelazioneScelta() {
		return progrRelazioneScelta;
	}

	public void setProgrRelazioneScelta(short progrRelazioneScelta) {
		this.progrRelazioneScelta = progrRelazioneScelta;
	}

	public Integer[] getIdSelMultipla() {
		return idSelMultipla;
	}

	public void setIdSelMultipla(Integer[] idSelMultipla) {
		this.idSelMultipla = idSelMultipla;
	}

	public RelazioniVO getRelazione() {
		return relazione;
	}

	public void setRelazione(RelazioniVO relazione) {
		this.relazione = relazione;
	}

	public Integer[] getIdSelMultiplaConferma() {
		return idSelMultiplaConferma;
	}

	public void setIdSelMultiplaConferma(Integer[] idSelMultiplaConferma) {
		this.idSelMultiplaConferma = idSelMultiplaConferma;
	}

	public String getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
}
