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
package it.iccu.sbn.web.integration.actionforms.servizi.erogazione;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.PenalitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.ill.DatiRichiestaILLDecorator;
import it.iccu.sbn.vo.custom.servizi.ill.MessaggioVODecorator;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DettaglioMovimentoForm extends AbstractBibliotecaForm {



	private static final long serialVersionUID = 710646354138655606L;

	public enum RichiestaDettaglioMovimentoType {
		OK,
		GESTIONE,
		AVANZA,
		CAMBIO_SERVIZIO,
		CANCELLA,
		RIFIUTA,
		RINNOVA,
		RIFIUTA_PROROGA,
		RINNOVO_CON_PRENOTAZIONE,
		SOSPENDI,
		PROROGA_SUCCESSIVA_DATA_MASSIMA,
		INOLTRO_PRENOTAZIONE,
		STAMPA_MODULO_RICHIESTA,
		RICHIESTA_ILL,
		MESSAGGIO,
		CONDIZIONE,
		PRENOTAZIONI,
		INOLTRO_ALTRA_BIB_FORNITRICE,
		LISTA_BIB_FORNITRICI,
		SUPPORTO_EROGAZIONE_ILL,
		DATA_MASSIMA_ILL,
		ILL_RICHIEDENTE,
		ILL_UPLOAD,
		ILL_DOWNLOAD,
		CHIEDI_ANNULLAMENTO;
	}

	private boolean sessione = false;
	// conferma pre operazioni
	private boolean conferma = false;

	private boolean erroreBloccante	= false;

	private boolean stampaModulo = false;

	private String vengoDa;
	private String inventario;
	private RichiestaDettaglioMovimentoType richiesta = null;

	// conferma pre dati nuova richiesta
	private boolean confermaNuovaRichiesta = false;

	private boolean esistonoPrenotazioni = false;

	/**
	 * Dati del movimento visualizzato nel dettaglio JSP
	 */
	private MovimentoListaVO detMov = new MovimentoListaVO();

	/**
	 * Istanza contenente i dati presenti sul DB. Utilizzato per confrontare i
	 * dati che l'utente vuole salvare, dando OK nella maschera per evitare di
	 * effettuare un aggiornamento non necessario
	 */
	private MovimentoListaVO movimentoSalvato = new MovimentoListaVO();

	/**
	 * Dati movimento aggiornati in seguito ad avanzamento non ancora committato
	 * sul DB Una volta che i nuovi dati sono aggiornati anche sul DB allora
	 * detMov sarà impostato uguale a detMovDaAvanzare
	 */
	//private MovimentoListaVO detMovDaAvanzare = new MovimentoListaVO();

	/**
	 * Dati Dati del movimento in attesa di conferma
	 */
	private MovimentoListaVO detMovConferma = new MovimentoListaVO();

	/**
	 * dati di ricerca
	 */
	private MovimentoVO movRicerca = new MovimentoRicercaVO();

	/**
	 * dati della prenotazione
	 */
	private MovimentoVO prenotazione = new MovimentoListaVO();

	/**
	 * dati del nuovo movimento dopo il cambio servizio
	 */
	private MovimentoVO movCambioServizio= new MovimentoListaVO();

	/**
	 * Dati utente
	 */
	private UtenteBaseVO utenteVO = new UtenteBaseVO();

	/**
	 * Dati documento
	 */
	private InfoDocumentoVO infoDocumentoVO = new InfoDocumentoVO();

	/**
	 * Salvo il polo
	 */
	private String salvaPolo;

	//posizione corrente
	private int posizioneCorrente;

	private String updateCombo;

	public String getSalvaPolo() {
		return salvaPolo;
	}

	public void setSalvaPolo(String salvaPolo) {
		this.salvaPolo = salvaPolo;
	}

	/**
	 * Salvo la biblioteca operante
	 */
	private String salvaBibOperante;


	public String getSalvaBibOperante() {
		return salvaBibOperante;
	}

	public void setSalvaBibOperante(String salvaBibOperante) {
		this.salvaBibOperante = salvaBibOperante;
	}

	/**
	 * Dettagli del servizio associato al movimento in esame
	 */
	private ServizioBibliotecaVO servizioMovimento;

	private List<SupportoBibliotecaVO> tipiSupporto;
	private List<?> bibliotechePolo;
	private List<TariffeModalitaErogazioneVO> modoErogazione;
	private List<ControlloAttivitaServizio> listaAttivitaSucc;
	private PenalitaServizioVO penalita;
	private Long gg_sosp;
	private Long gg_rit;
	private Date data_sosp = null;

	private List<ServizioBibliotecaVO> lstCodiciServizio;

	private String ult_supp;
	private String ult_mod;

	boolean flgNumPezzi;

	// errore nuova richiesta
	private boolean flgErrNuovaRichiesta;

	/**
	 * Nel caso di rinnovo, contiene la data massima per la quale si può
	 * chiedere il rinnovo. Nel caso la data proroga inserita dal bibliotecario
	 * superi questa data viene chiesto al bibliotecario se si deve accettare la
	 * data inserita o considerare il valore contenuto in questo campo.
	 */
	private Date dataProrogaMassima = null;

	/**
	 * Indica il numero di rinnovi relativi al movimento, incluso il rinnovo
	 * appena chiesto. Viene utilizzato per salvare il numero calcolato, nel
	 * caso si chieda conferma all'utente nella situazione in cui la data
	 * proroga inserita dal bibliotecario supera la data massima prevista Viene
	 * utilizzata in coppia con dataProrogaMassima
	 */
	private short numeroRinnovo = 0;

	// Lista servizi per la funzione di cambio servizio
	private List<ServizioBibliotecaVO> lstServizi;
	// vale true se si sceglie la funzione cambia servizio
	private boolean cambioServizio = false;
	// codice servizio scelto per funzione cambia servizio
	private String codServNuovaRich;
	// Istanza del serivzio selezionato per cambio servizio
	private ServizioBibliotecaVO servizioSelezionato = new ServizioBibliotecaVO();

	// Lista iter servizio per funzione cambio servizio
	private List<IterServizioVO> lstIter;
	// Progressivo iter scelto
	private String progrIter;
	private List<MovimentoListaVO> listaMovimenti = new ArrayList<MovimentoListaVO>();

	private ControlloAttivitaServizio attivitaPendente;
	private MessaggioVO messaggio;
	private int bibSelezionata;

	private List<TB_CODICI> tipiSupportoILL;
	private List<TB_CODICI> modoErogazioneILL;
	private List<TB_CODICI> listaTipoNumeroStd;
	private List<TB_CODICI> listaTipoRecord;
	private List<ComboCodDescVO> listaTipoCodFruizione;
	private List<ComboCodDescVO> listaTipoCodNoDisp;
	private List<ComboCodDescVO> listaPaesi;
	private List<ComboCodDescVO> listaLingue;
	private List<TB_CODICI> listaNature;

	public PenalitaServizioVO getPenalita() {
		return penalita;
	}

	public void setPenalita(PenalitaServizioVO penalita) {
		this.penalita = penalita;
	}

	public List<SupportoBibliotecaVO> getTipiSupporto() {
		return tipiSupporto;
	}

	public void setTipiSupporto(List<SupportoBibliotecaVO> tipiSupporto) {
		this.tipiSupporto = tipiSupporto;
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

	public RichiestaDettaglioMovimentoType getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(RichiestaDettaglioMovimentoType richiesta) {
		this.richiesta = richiesta;
	}

	public MovimentoVO getMovRicerca() {
		return movRicerca;
	}

	public void setMovRicerca(MovimentoVO movRicerca) {
		this.movRicerca = movRicerca;
	}

	public MovimentoListaVO getDetMov() {
		return detMov;
	}

	public void setDetMov(MovimentoListaVO mov) throws Exception {
		this.detMov = mov;
		if (mov != null) {
			if (mov.isWithPrenotazionePosto()) {
				PrenotazionePostoVO pp = mov.getPrenotazionePosto();
				mov.setPrenotazionePosto(new PrenotazionePostoDecorator(pp));
			}

			if (mov.isRichiestaILL()) {
				DatiRichiestaILLVO dati = mov.getDatiILL();
				mov.setDatiILL(new DatiRichiestaILLDecorator(dati));
			}
		}
	}

	public List<TariffeModalitaErogazioneVO> getModoErogazione() {
		return modoErogazione;
	}

	public void setModoErogazione(List<TariffeModalitaErogazioneVO> modoErogazione) {
		this.modoErogazione = modoErogazione;
	}

	public List<?> getBibliotechePolo() {
		return bibliotechePolo;
	}

	public void setBibliotechePolo(List<?> bibliotechePolo) {
		this.bibliotechePolo = bibliotechePolo;
	}

	public List<ControlloAttivitaServizio> getListaAttivitaSucc() {
		return listaAttivitaSucc;
	}

	public void setListaAttivitaSucc(
			List<ControlloAttivitaServizio> listaAttivita) {
		this.listaAttivitaSucc = listaAttivita;
	}

	public Long getGg_rit() {
		return gg_rit;
	}

	public void setGg_rit(Long gg_rit) {
		this.gg_rit = gg_rit;
	}

	public Long getGg_sosp() {
		return gg_sosp;
	}

	public void setGg_sosp(Long gg_sosp) {
		this.gg_sosp = gg_sosp;
	}

	public Date getData_sosp() {
		return data_sosp;
	}

	public void setData_sosp(Date data_sosp) {
		this.data_sosp = data_sosp;
	}

	public boolean isCambioServizio() {
		return cambioServizio;
	}

	public void setCambioServizio(boolean cambioServizio) {
		this.cambioServizio = cambioServizio;
	}

	public List<ServizioBibliotecaVO> getLstServizi() {
		return lstServizi;
	}

	public void setLstServizi(List<ServizioBibliotecaVO> lstServizi) {
		this.lstServizi = lstServizi;
	}

	public List<IterServizioVO> getLstIter() {
		return lstIter;
	}

	public void setLstIter(List<IterServizioVO> lstIter) {
		this.lstIter = lstIter;
	}

	public String getProgrIter() {
		return progrIter;
	}

	public void setProgrIter(String progrIter) {
		this.progrIter = progrIter;
	}

	public String getCodServNuovaRich() {
		return codServNuovaRich;
	}

	public void setCodServNuovaRich(String codServNuovaRich) {
		this.codServNuovaRich = codServNuovaRich;
	}

	public UtenteBaseVO getUtenteVO() {
		return utenteVO;
	}

	public void setUtenteVO(UtenteBaseVO utenteVO) {
		this.utenteVO = utenteVO;
	}

	public InfoDocumentoVO getInfoDocumentoVO() {
		return infoDocumentoVO;
	}

	public void setInfoDocumentoVO(InfoDocumentoVO infoDocumentoVO) {
		this.infoDocumentoVO = infoDocumentoVO;
	}

	public ServizioBibliotecaVO getServizioSelezionato() {
		return servizioSelezionato;
	}

	public void setServizioSelezionato(ServizioBibliotecaVO servizioSelezionato) {
		this.servizioSelezionato = servizioSelezionato;
	}

	public ServizioBibliotecaVO getServizioMovimento() {
		return servizioMovimento;
	}

	public void setServizioMovimento(ServizioBibliotecaVO servizioMovimento) {
		this.servizioMovimento = servizioMovimento;
	}

	public Date getDataProrogaMassima() {
		return dataProrogaMassima;
	}

	public void setDataProrogaMassima(Date dataProrogaMassima) {
		this.dataProrogaMassima = dataProrogaMassima;
	}

	public short getNumeroRinnovo() {
		return numeroRinnovo;
	}

	public void setNumeroRinnovo(short numeroRinnovo) {
		this.numeroRinnovo = numeroRinnovo;
	}

	public MovimentoListaVO getMovimentoSalvato() {
		return movimentoSalvato;
	}

	public void setMovimentoSalvato(MovimentoListaVO movimentoSalvato) {
		this.movimentoSalvato = movimentoSalvato;
	}

	public int getCostoServizioSize() {
		return (this.detMov.getCostoServizio() == null ? 0 : this.detMov
				.getCostoServizio().trim().length());
	}

	public String getVengoDa() {
		return vengoDa;
	}

	public void setVengoDa(String vengoDa) {
		this.vengoDa = vengoDa;
	}

	public MovimentoListaVO getDetMovConferma() {
		return detMovConferma;
	}

	public void setDetMovConferma(MovimentoListaVO detMovConferma) {
		this.detMovConferma = detMovConferma;
	}

	public void setPrenotazione(MovimentoVO prenotazione) {
		this.prenotazione = prenotazione;
	}

	public MovimentoVO getPrenotazione() {
		return prenotazione;
	}

	public String getUlt_mod() {
		return ult_mod;
	}

	public void setUlt_mod(String ultMod) {
		ult_mod = ultMod;
	}

	public String getUlt_supp() {
		return ult_supp;
	}

	public void setUlt_supp(String ultSupp) {
		ult_supp = ultSupp;
	}

	public boolean isFlgNumPezzi() {
		return flgNumPezzi;
	}

	public void setFlgNumPezzi(boolean flgNumPezzi) {
		this.flgNumPezzi = flgNumPezzi;
	}
	public boolean isFlgErrNuovaRichiesta() {
		return flgErrNuovaRichiesta;
	}
	public void setFlgErrNuovaRichiesta(boolean flgErrNuovaRichiesta) {
		this.flgErrNuovaRichiesta = flgErrNuovaRichiesta;
	}


	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public List<ServizioBibliotecaVO> getLstCodiciServizio() {
		return lstCodiciServizio;
	}

	public void setLstCodiciServizio(
			List<ServizioBibliotecaVO> lstCodiciServizio) {
		this.lstCodiciServizio = lstCodiciServizio;
	}

	public boolean isConfermaNuovaRichiesta() {
		return confermaNuovaRichiesta;
	}

	public void setConfermaNuovaRichiesta(boolean confermaNuovaRichiesta) {
		this.confermaNuovaRichiesta = confermaNuovaRichiesta;
	}

	public MovimentoVO getMovCambioServizio() {
		return movCambioServizio;
	}

	public void setMovCambioServizio(MovimentoVO movCambioServizio) {
		this.movCambioServizio = movCambioServizio;
	}

	public boolean isStampaModulo() {
		return stampaModulo;
	}

	public void setStampaModulo(boolean checkIter) {
		this.stampaModulo = checkIter;
	}

	public boolean isErroreBloccante() {
		return erroreBloccante;
	}

	public void setErroreBloccante(boolean erroreBloccante) {
		this.erroreBloccante = erroreBloccante;
	}

	public boolean isEsistonoPrenotazioni() {
		return esistonoPrenotazioni;
	}

	public void setEsistonoPrenotazioni(boolean esistonoPrenotazioni) {
		this.esistonoPrenotazioni = esistonoPrenotazioni;
	}

	public void setListaMovimenti(List<MovimentoListaVO> list) {
		this.listaMovimenti = list;
	}

	public List<MovimentoListaVO> getListaMovimenti() {
		return listaMovimenti;
	}

	public ControlloAttivitaServizio getAttivitaPendente() {
		return attivitaPendente;
	}

	public void setAttivitaPendente(ControlloAttivitaServizio attivitaPendente) {
		this.attivitaPendente = attivitaPendente;
	}

	public MessaggioVO getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(MessaggioVO msg) throws Exception {
		this.messaggio = (msg != null) ? new MessaggioVODecorator(msg) : null;
	}

	public int getBibSelezionata() {
		return bibSelezionata;
	}

	public void setBibSelezionata(int bibSelezionata) {
		this.bibSelezionata = bibSelezionata;
	}

	public int getPosizioneCorrente() {
		return posizioneCorrente;
	}

	public void setPosizioneCorrente(int posizioneCorrente) {
		this.posizioneCorrente = posizioneCorrente;
	}

	public String getUpdateCombo() {
		return updateCombo;
	}

	public void setUpdateCombo(String updateCombo) {
		this.updateCombo = updateCombo;
	}

	public List<TB_CODICI> getTipiSupportoILL() {
		return tipiSupportoILL;
	}

	public void setTipiSupportoILL(List<TB_CODICI> tipiSupportoILL) {
		this.tipiSupportoILL = tipiSupportoILL;
	}

	public List<TB_CODICI> getModoErogazioneILL() {
		return modoErogazioneILL;
	}

	public void setModoErogazioneILL(List<TB_CODICI> modoErogazioneILL) {
		this.modoErogazioneILL = modoErogazioneILL;
	}

	public DocumentoNonSbnVO getDocumento() {
		if (detMov.isRichiestaILL())
			return detMov.getDatiILL().getDocumento();

		if (detMov.isRichiestaSuSegnatura())
			return infoDocumentoVO.getDocumentoNonSbnVO();

		return new DocumentoNonSbnVO();
	}

	public List<TB_CODICI> getListaTipoNumeroStd() {
		return listaTipoNumeroStd;
	}

	public void setListaTipoNumeroStd(List<TB_CODICI> listaTipoNumeroStd) {
		this.listaTipoNumeroStd = listaTipoNumeroStd;
	}

	public List<TB_CODICI> getListaTipoRecord() {
		return listaTipoRecord;
	}

	public void setListaTipoRecord(List<TB_CODICI> listaTipoRecord) {
		this.listaTipoRecord = listaTipoRecord;
	}

	public List<ComboCodDescVO> getListaTipoCodFruizione() {
		return listaTipoCodFruizione;
	}

	public void setListaTipoCodFruizione(List<ComboCodDescVO> listaTipoCodFruizione) {
		this.listaTipoCodFruizione = listaTipoCodFruizione;
	}

	public List<ComboCodDescVO> getListaTipoCodNoDisp() {
		return listaTipoCodNoDisp;
	}

	public void setListaTipoCodNoDisp(List<ComboCodDescVO> listaTipoCodNoDisp) {
		this.listaTipoCodNoDisp = listaTipoCodNoDisp;
	}

	public List<ComboCodDescVO> getListaPaesi() {
		return listaPaesi;
	}

	public void setListaPaesi(List<ComboCodDescVO> listaPaesi) {
		this.listaPaesi = listaPaesi;
	}

	public List<ComboCodDescVO> getListaLingue() {
		return listaLingue;
	}

	public void setListaLingue(List<ComboCodDescVO> listaLingue) {
		this.listaLingue = listaLingue;
	}

	public List<TB_CODICI> getListaNature() {
		return listaNature;
	}

	public void setListaNature(List<TB_CODICI> listaNature) {
		this.listaNature = listaNature;
	}

}
