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
package it.iccu.sbn.web.integration.action;

import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.exception.RandomIdGenerator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class ServiziBaseAction extends SinteticaLookupDispatchAction {

	protected static Logger log = Logger.getLogger(ServiziBaseAction.class);

	public static final String COD_TIPO_SERVIZIO_ATTR         = "TIPO_SERVIZIO_DA_CONFIGURARE";
	public static final String COD_ATTIVITA_ATTR              = "CODICE_ATTIVITA";
	public static final String DESC_TIPO_SERVIZIO_ATTR        = "DESCRIZIONE_TIPO_SERVIZIO_DA_CONFIGURARE";
	public static final String DESC_ATTIVITA_ATTR             = "DESCRIZIONE_ATTIVITA";
	public static final String LISTA_SERVIZI_ATTR             = "LISTA_SERVIZI_ATTR";
	public static final String LISTA_CODICI_SERVIZI_ATTR      = "LISTA_CODICI_SERVIZI";
	public static final String VO_TIPO_SERVIZIO_ATTR          = "TIPO_SERVIZIO_VO";
	public static final String ID_TIPO_SERVIZIO_ATTR          = "ID_TIPO_SERVIZIO";
	public static final String VO_SERVIZIO_ATTR               = "SERVIZIO_VO";
	public static final String VO_PENALITA_ATTR               = "PENALITA_SERVIZIO_VO";
	public static final String BIBLIOTECA_ATTR                = "CODICE_BIBLIOTECA";
	public static final String PATH_CHIAMANTE_ATTR            = "PATH_CHIAMANTE";
	public static final String PENALITA_ATTR                  = "PENALITA";
	public static final String VO_TARIFFA_MODALITA_EROGAZIONE = "TARIFFA_MODALITA_EROGAZIONE";
	public static final String LISTA_CODICI_EROGAZIONE_ATTR   = "LISTA_CODICI_EROGAZIONE";
	public static final String VO_ITER_SERVIZIO               = "ITER_SERVIZIO";
	public static final String LISTA_CODICI_ATTIVITA          = "LISTA_CODICI_ATTIVITA";
	public static final String PROGRESSIVO_ITER_SCELTO        = "PROGRESSIVO_ITER_SCELTO";
	public static final String VO_DETTAGLIO_CONTROLLO_ATTR    = "DETTAGLIO_CONTROLLO_VO";
	public static final String LISTA_CODICI_GIA_ASSEGNATI     = "LISTA_CODICI_GIA_ASSEGNATI";
	public static final String PROGRESSIVO_CONTROLLO_SCELTO   = "PROGRESSIVO_CONTROLLO_SCELTO";
	public static final String PARAMETRI_ATTR                 = "PARAMETRI_BIBLIOTECA";
	public static final String VO_SUPPORTO_ATTR               = "SUPPORTO_VO";

	public static final String LISTA_ID_IGNORATI              = "LISTA_ID_IGNORATI";


	/**
	 * Carica una riga di TB_CODICI relativa al co0dice ricerca passato
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getCodice
	  * TB_CODICI
	  * @param codiceRicerca
	  * @param tipoCodice
	  * @param tipoRicerca
	 * @param request
	  * @return
	 * @throws CreateException
	 * @throws RemoteException
	  *
	  *
	 */
	public TB_CODICI getCodice(String codiceRicerca, CodiciType tipoCodice,	CodiciRicercaType tipoRicerca, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getCodice(codiceRicerca, tipoCodice, tipoRicerca);
	}


	/**
	 * Carica tutti i codici tipo servizio<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadTipiServizio
	  * List<ComboCodDescVO>
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadTipiServizio(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_TIPO_SERVIZIO);
	}


	/**
	 * Carica la combo di tipo servizio con le informazioni relative ai codici tipo servizio contenuti in codici<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadTipiServizio
	  * List<ComboCodDescVO>
	  * @param codici
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadTipiServizio(List codici, HttpServletRequest request) throws RemoteException, CreateException {
		List listaCodici = new ArrayList();
		listaCodici.add(new String(""));
		listaCodici.addAll(codici);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_TIPO_SERVIZIO, listaCodici);
	}

	protected List<ComboCodDescVO> loadModalitaErogazione(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_MODALITA_EROGAZIONE);
	}

	protected List<ComboCodDescVO> loadCategorieFruizione(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE);
	}

	protected List<ComboCodDescVO> loadCategorieRiproduzione(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM);
	}

	/**
	 * Carica la combo di tipo servizio con le informazioni
	  * relative ai codici tipo servizio non contenuti in codici<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadTipiServizioDiversiDa
	  * List<ComboCodDescVO>
	  * @param codici
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadTipiServizioDiversiDa(List codici, HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodiciDiversiDa(CodiciType.CODICE_TIPO_SERVIZIO, codici);
	}


	protected List<ComboCodDescVO> loadModalitaErogazioneDiverseDa(List codici, HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodiciDiversiDa(CodiciType.CODICE_MODALITA_EROGAZIONE, codici);
	}

	protected List<ComboCodDescVO> loadModalitaErogazioneDiverseDaQuelleDefinite(String codPolo, String codBib, List codici, HttpServletRequest request) throws RemoteException, CreateException {
		return this.loadModalitaErogazioneDiverseDaQuelleDefinite(codPolo, codBib, codici, request, null);
	}

	protected List<ComboCodDescVO> loadModalitaErogazioneDiverseDaQuelleDefinite(String codPolo, String codBib,
			List codici, HttpServletRequest request, String flSvolg) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodiciDiversiDaQuelliDefiniti(codPolo, codBib, codici, flSvolg);
	}

	/**
	 *  Carica la combo relativa alle attività escluse quelle i cui codici sono inclusi in <strong>codici</strong>
	 *  <br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadCodiciAttivitaDiversiDa
	  * List<ComboCodDescVO>
	  * @param codici  Lista dei codici attività da escludere dalla ricerca
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadCodiciAttivitaDiversiDa(List<String> codici, HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodiciDiversiDa(CodiciType.CODICE_ATTIVITA_ITER, codici);
	}

	protected List<ComboCodDescVO> loadCodiciAttivita(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_ATTIVITA_ITER);
	}

	/**
	 * Carica la combo relativa ai codici stato richiesta
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadCodiciStatoRichiesta
	  * List<ComboCodDescVO>
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadCodiciStatoRichiesta(HttpServletRequest request)  throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_STATO_RICHIESTA);
	}


	/**
	 * Carica la combo relativa ai codici stato movimento
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadCodiciStatoMovimento
	  * List<ComboCodDescVO>
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadCodiciStatoMovimento(HttpServletRequest request)  throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_STATO_MOVIMENTO);
	}


	/**
	 * Carica la combo relativa ai codici controllo iter
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadCodiciControlloIter
	  * List<ComboCodDescVO>
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadCodiciControlloIter(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_FUNZIONE_CONTROLLO_ITER);
	}


	protected List<ComboCodDescVO> loadCodiciControlloIterDiversiDa(List<String> listaCodici, HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodiciDiversiDa(CodiciType.CODICE_FUNZIONE_CONTROLLO_ITER, listaCodici);
	}


	/**
	 * Carica la combo relativa ai codici supporto<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadCodiciControlloIter
	  * List<ComboCodDescVO>
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadCodiciSupportiBiblioteca(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_SUPPORTO_COPIA);
	}
	/**
	 * Carica la combo relativa ai codici supporto che non sono inclusi nella lista passata in ingresso<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * loadCodiciControlloIter
	  * List<ComboCodDescVO>
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected List<ComboCodDescVO> loadCodiciSupportiBibliotecaDiversiDa(List<String> listaCodici, HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodiciDiversiDa(CodiciType.CODICE_SUPPORTO_COPIA, listaCodici);
	}

	protected List<ComboCodDescVO> loadProfessioni(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_PROFESSIONI);
	}

	protected List<ComboCodDescVO> loadTitoliStudio(HttpServletRequest request) throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.loadCodici(CodiciType.CODICE_TITOLO_STUDIO);
	}

	/**
	 * Torna la lista dei codici tipi servizio associati alla biblioteca<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getCodiciTipiServizio
	  * List<String>
	  * @param codPolo
	  * @param codBib
	 * @param request
	  * @return
	  * @throws RemoteException
	  *
	  *
	 */
	protected List<String> getCodiciTipiServizio(String codPolo, String codBib, HttpServletRequest request) throws RemoteException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List<String> lstTipiServizio = delegate.getCodiciTipiServizio(codPolo, codBib);
		return lstTipiServizio;

	}


	/**
	 * Torna una istanza di TipoServizioVO<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getTipoServizio
	  * TipoServizioVO
	  * @param codPolo
	  * @param codBib
	  * @param codTipoServizio
	 * @param request
	  * @return TipoServizioVO
	  * @throws RemoteException
	  *
	  *
	 */
	protected TipoServizioVO getTipoServizio(String codPolo, String codBib, String codTipoServizio, HttpServletRequest request) throws RemoteException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getTipoServizio(codPolo, codBib, codTipoServizio);
	}

	/**
	 * Torna una istanza di ServizioBibliotecaVO<br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getServizio
	  * ServizioBibliotecaVO
	  * @param codPolo
	  * @param codBib
	  * @param codTipoServizio
	  * @param codServ
	 * @param request
	  * @return ServizioBibliotecaVO
	  * @throws RemoteException
	  *
	  *
	 */
	protected ServizioBibliotecaVO getServizio(String codPolo, String codBib, String codTipoServizio, String codServ, HttpServletRequest request) throws RemoteException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getServizioBiblioteca(codPolo, codBib, codTipoServizio, codServ);
	}


	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getTariffeModalitaErogazione
	  * List<TariffeModalitaErogazioneVO>
	  * @param codPolo
	  * @param codBib
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  * <br/>Torna la lista di TariffeModalitaErogazione relative al codice tipo servizio passato
	 */
	protected List<ModalitaErogazioneVO> getTariffeModalitaErogaz(String codPolo, String codBib, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getTariffeModalitaErogazione(codPolo, codBib);
	}

	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getTariffeModalitaErogazione
	  * List<TariffeModalitaErogazioneVO>
	  * @param codPolo
	  * @param codBib
	  * @param codTipoServizio
	 * @param request
	 * @param natura
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  * <br/>Torna la lista di TariffeModalitaErogazione relative al codice tipo servizio passato
	 */
	protected List<ServizioWebDatiRichiestiVO> getModuloRichiesta(
			String codPolo, String codBib, String codTipoServizio,
			HttpServletRequest request, String natura)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getModuloRichiesta(codPolo, codBib, codTipoServizio, natura);
	}

	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getTariffeModalitaErogazione
	  * List<TariffeModalitaErogazioneVO>
	  * @param codPolo
	  * @param codBib
	  * @param codTipoServizio
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  * <br/>Torna la lista di TariffeModalitaErogazione relative al codice tipo servizio passato
	 */
	protected List<TariffeModalitaErogazioneVO> getTariffeModalitaErogazione(String codPolo, String codBib, String codTipoServizio, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getTariffeModalitaErogazioneServizio(codPolo, codBib, codTipoServizio, null);
	}

	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getSupportiModalitaErogazione
	  * List<SupportiModalitaErogazioneVO>
	  * @param codPolo
	  * @param codBib
	  * @param codSupporto
	 * @param request
	 * @param fl_svolg
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  * <br/>Torna la lista di SupportiModalitaErogazione relative al codice tipo servizio passato
	 */
	protected List<SupportiModalitaErogazioneVO> getSupportiModalitaErogazione(String codPolo, String codBib,
			String codSupporto, HttpServletRequest request) throws RemoteException, CreateException {
		return this.getSupportiModalitaErogazione(codPolo, codBib, codSupporto, request, null);
	}

	protected List<SupportiModalitaErogazioneVO> getSupportiModalitaErogazione(String codPolo, String codBib,
			String codSupporto, HttpServletRequest request, String fl_svolg)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		log.debug("getSupportiModalitaErogazione(): codPolo, " + codPolo + ", codBib: " + codBib + ", codSupp: " + codSupporto);
		return delegate.getSupportiModalitaErogazione(codPolo, codBib, codSupporto, fl_svolg);
	}

	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getIterServizio
	  * List<IterServizioVO>
	  * @param codPolo
	  * @param codBib
	  * @param codTipoServizio
	 * @param request
	  * @return
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *<br/> Torna la lista dioggetti IterServizioVO associati al codice tipo servizio passato
	 */
	protected List<IterServizioVO> getIterServizio(String codPolo, String codBib, String codTipoServizio, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getIterServizio(codPolo, codBib, codTipoServizio);
	}

	protected IterServizioVO getIterServizio(String codPolo, String codBib, String codTipoServizio, String codAttivita, HttpServletRequest request)
	throws RemoteException, CreateException {
		List<IterServizioVO> iterServizio = this.getIterServizio(codPolo, codBib, codTipoServizio, request);
		if (ValidazioneDati.isFilled(iterServizio))
			for (IterServizioVO iter : iterServizio)
				if (ValidazioneDati.equals(iter.getCodAttivita(), codAttivita))
					return iter;

		return null;
	}

	protected List<FaseControlloIterVO> getControlloIter(int idTipoServizio, String codAttivita, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getControlloIter(idTipoServizio, codAttivita);
	}


	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getListaServizi
	  * List<ServizioAllVO>
	  * @param codPolo
	  * @param codBib
	  * @param codTipoServizio
	 * @param request
	  * @return
	  * @throws RemoteException
	  *
	  * <br/>Restituisce la lista dei servizi associati a un tipo servizio
	 */
	protected List<ServizioBibliotecaVO> getListaServizi(String codPolo, String codBib, String codTipoServizio, HttpServletRequest request)
	throws RemoteException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getListaServizi(codPolo, codBib, codTipoServizio);
	}

	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getListaServizi
	  * List<ServizioAllVO>
	  * @param codPolo
	  * @param codBib
	  * @param codTipoServizio
	 * @param request
	  * @return
	  * @throws RemoteException
	  *
	  * <br/>Restituisce la lista dei servizi associati a un tipo servizio
	 */
	protected List<TipoServizioVO> getListaTipiServizio(String codPolo, String codBib, HttpServletRequest request)
	throws RemoteException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getListaTipiServizio(codPolo, codBib);
	}


	/**
	 * Torna un'istanza di ParametriBibliotecaVO
	 * <br/>
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getParametriBiblioteca
	  * ParametriBibliotecaVO
	  * @param codPolo
	  * @param codBib
	 * @param request
	  * @return
	  * @throws RemoteException
	  *
	  *
	 */
	protected ParametriBibliotecaVO getParametriBiblioteca(String codPolo, String codBib, HttpServletRequest request)
	throws RemoteException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getParametriBiblioteca(codPolo, codBib);
	}


	protected List<SupportoBibliotecaVO> getSupportiBiblioteca(String codPolo, String codBib, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getSupportiBiblioteca(codPolo, codBib, null);
	}

	protected SupportoBibliotecaVO getSupportoBiblioteca(String codPolo, String codBib, String codSupporto, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getSupportoBiblioteca(codPolo, codBib, codSupporto);
	}

	protected List<ModalitaPagamentoVO> getModalitaPagamento(String codPolo, String codBib, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getModalitaPagamento(codPolo, codBib);
	}


	protected UtenteBaseVO getUtente(String codPolo, String codBibUte, String codUtente, String codBib, HttpServletRequest request) throws RemoteException
	{
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getUtente(codPolo, codBibUte, codUtente, codBib);
	}

	protected UtenteBaseVO getUtente(String codUtente, HttpServletRequest request) throws RemoteException
	{
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getUtente(codUtente);
	}

	protected InfoDocumentoVO getInfoSegnatura(HttpServletRequest request,
			String codPolo, String codBibDocLett, String tipoDoc, int codDoc,
			int prgEsempl) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		CommandResultVO result = delegate.invoke(CommandType.DETTAGLIO_DOCUMENTO_NON_SBN_CON_ESEMPLARE,
				codPolo,
				codBibDocLett,
				tipoDoc,
				codDoc,
				prgEsempl);

		result.throwError();

		DocumentoNonSbnVO doc = (DocumentoNonSbnVO) result.getResult();
		InfoDocumentoVO info = new InfoDocumentoVO();
		info.setDocumentoNonSbnVO(doc);
		info.setTitolo(doc.getTitolo());
		return info;

	}

	protected InfoDocumentoVO getInfoInventario(String codPolo,
			String codBibInv, String codSerieInv, int codInvenInv,
			String ticket, Locale locale, HttpServletRequest request)
			throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		try {
			return delegate.getInfoInventario(codPolo, codBibInv, codSerieInv, codInvenInv, ticket, locale);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}


	protected String getDescrizioneCodiceSBN(CodiciType type, String code, HttpServletRequest request)
	throws RemoteException, CreateException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getDescrizioneCodiceSBN(type, code);
	}


	protected List<ControlloAttivitaServizio> getListaAttivitaSuccessive(String codPolo, String codBib, String codTipoServ, int progrIter,
			DatiRichiestaILLVO datiILL, HttpServletRequest request)
	throws RemoteException {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		return delegate.getListaAttivitaSuccessive(codPolo, codBib, codTipoServ, progrIter, datiILL);
	}




	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * getErroreGenerico
	  * ActionMessages
	  * @return ActionMessages
	  *
	  *<br/>Torna una istanza di ActionMessages con un messaggio generico di errore
	 */
	protected ActionMessages getErroreGenerico() {
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.generico"));
		return errors;
	}


	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * setErroreGenerico
	  * void
	  * @param request
	  *
	  *<br/>Imposta il messaggio di errore generico previsto
	 */
	protected void setErroreGenerico(HttpServletRequest request, Throwable t) {
		if (t instanceof SbnBaseException)
			LinkableTagUtils.addError(request, (SbnBaseException)t);
		else {
			//almaviva5_20111118
			String errorId = RandomIdGenerator.getId();
			log.error("servizi erroreGenerico [errorId: " + errorId + ']');
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.generico"));
			LinkableTagUtils.addError(request, new ActionMessage("ERROR_ID_TEMPLATE", errorId ));
		}
	}

	protected void saveErrors(HttpServletRequest request, String[] msg) {
		ActionMessages errors = LinkableTagUtils.getErrors(request);
		for (int i = 0; i < msg.length; i++)
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi", msg[i]));
	}

	protected void saveMessages(HttpServletRequest request, String[] msg) {
		ActionMessages messages = new ActionMessages();
		for (int i=0; i<msg.length; i++)
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messages.servizi", msg[i]));

		this.saveMessages(request, messages);
	}

	protected void saveMessages(HttpServletRequest request, List<String> codiciMsg) {
		ActionMessages messages = new ActionMessages();

		for (String codiceMsg : codiciMsg) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(codiceMsg));
		}

		this.saveMessages(request, messages);
	}

	protected void addErrors(HttpServletRequest request, List<String> codiciMsg) {
		for (String codiceMsg : codiciMsg)
			LinkableTagUtils.addError(request, new ActionMessage(codiceMsg));

	}

	protected void addErrors(HttpServletRequest request, String[] msg) {
		this.addErrors(request, Arrays.asList(msg));
	}


	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * backForward
	  * ActionForward
	  * @param mapping
	  * @param pathChiamante
	  * @return
	  *
	  * <br/>Costruisce una istanza di ActionForward impostando il path con
	  * pathChiamante se quest'ultimo non è null altrimenti ritorna
	  * mapping.findForward("indietro")
	 */
	protected ActionForward backForward(ActionMapping mapping, String pathChiamante) {
		if (pathChiamante!=null && !pathChiamante.equalsIgnoreCase("")) {
			ActionForward action = new ActionForward();
			action.setName("back");
			action.setPath(pathChiamante+".do");
			return action;
		} else {
			return mapping.findForward("indietro");
		}
	}

	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * backForward
	  * ActionForward
	  * @param request
	  * @return
	  *
	  * Torna alla action chiamante
	 */
	protected ActionForward backForward(HttpServletRequest request) {
		return Navigation.getInstance(request).goBack();
	}


	/**
	 *
	  * it.iccu.sbn.web.actions.servizi
	  * ServiziBaseAction.java
	  * backForward
	  * ActionForward
	  * @param request
	  * @param fromBar <b>true</b> se back da barra di navigazione, <b>false</b> altrimenti
	  * @return
	  *
	  * Torna alla action chiamante
	 */
	protected ActionForward backForward(HttpServletRequest request, boolean fromBar) {
		return Navigation.getInstance(request).goBack(fromBar);
	}


	protected String getFirmaUtente(HttpServletRequest request) {
		Navigation navi = Navigation.getInstance(request);
		return navi.getUtente().getFirmaUtente();
	}

	protected void saveErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	public String getCodBibOperante(HttpServletRequest request) {
		Navigation navi = Navigation.getInstance(request);
		BibliotecaVO bib = (BibliotecaVO) navi.getAttribute(NavigazioneServizi.BIBLIOTECA_OPERANTE);
		if (bib != null)
			return bib.getCod_bib();

		return navi.getUtente().getCodBib();
	}

	/**
	 * Recupera il tipo si operatore collegato al sistema (bibliotecario/utente)
	 */
	public static final OperatoreType getOperatore(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session.getAttribute(Constants.UTENTE_WEB_KEY) != null ?
			OperatoreType.UTENTE :
			OperatoreType.BIBLIOTECARIO;
	}

}
