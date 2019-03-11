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
package it.iccu.sbn.web.integration.bd.gestioneservizi;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.IntervalloSegnaturaNonValidoException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.blocchi.DescrittoreBlocchiUtil;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.MovimentoPerStampaServCorrVO;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.relazioni.RelazioniVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.vo.custom.servizi.DocumentoNonSbnDecorator;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

@SuppressWarnings("unchecked")
public class ServiziDelegate {

	private static Logger log = Logger.getLogger(ServiziDelegate.class);

	public static final String LISTA_UTENTI = "lista_utenti";
	public static final String LISTA_AUTORIZZAZIONI = "lista_autorizzazioni";
	public static final String LISTA_MATERIE_INTERESSE = "lista_materie_interesse";
	public static final String LISTA_OCCUPAZIONI = "lista_occupazioni";
	public static final String LISTA_SEGNATURE = "lista_segnature";
	public static final String LISTA_SPECIALITA_TDS = "lista_specialita_titoloStudio";
	public static final String LISTA_ANAGRAFICA_SERVIZI = "lista_anagrafica_servizi";
	public static final String LISTA_ANAGRAFICA_SERVIZI_UTENTE = "lista_anagrafica_servizi_utente";
	public static final String PARAMETRI_RICERCA_AUTORIZZAZIONI = "param_ricerca_autorizzazioni";
	public static final String LISTA_MOV_PREN_RICH_UTENTE = "lista_mov_pren_ric_ute";
	public static final String LISTA_TEMATICHE = "lista_tematiche_mov_pren_ric_ute";
	public static final String PARAMETRI_RICERCA_MOV_PREN_RICH_UTENTE = "param_ricerca_mov_pren_ric_ute";
	public static final String LISTA_SOLLECITI_MOVIMENTI_UTENTE = "lista_solleciti_dei_movimenti_utente";
	public static final String LISTA_ANAGRAFICA_SERVIZI_BIBLIOTECA = "lista_anagrafica_servizi_biblioteca";
	public static final String SIF_DOCUMENTI_NON_SBN = "srv.sif.lista.doc.non.sbn";
	public static final String SIF_DOCUMENTI_NON_SBN_ALTRA_BIB = "srv.sif.lista.doc.non.sbn.altra.bib";
	public static final String MOV_DA_INSERIRE = "mov_da_inserire";

	public static final String DETTAGLIO_OCCUPAZIONE = "occupazione.dettaglio";
	public static final String DETTAGLIO_SPEC_TITOLO_STUDIO = "dettaglio.titolo.studio";
	public static final String DETTAGLIO_MATERIA = "Materia.dettaglio";
	public static final String DETTAGLIO_AUTORIZZAZIONE = "AutAna.DETTAGLIO";
	public static final String DOCUMENTI_SELEZIONATI = "DocSel";

	public static final String LISTA_BIB_SISTEMI_METROPOLITANI = "ser.lista.bib.sist.metro";
	public static final String DETTAGLIO_SEGNATURA = "srv.dett.range.segn";
	public static final String PARAMETRI_RICERCA = "srv.params.ricerca";

	private static final String SERVIZI_DELEGATE = "req.servizi.delegate";

	private FactoryEJBDelegate factory;
	private Utente utenteEjb;
	private UserVO utente;

	private final HttpServletRequest request;



	protected void saveErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);

		if (errors == null || errors.isEmpty()) {
			request.removeAttribute("org.apache.struts.action.ERROR");
			return;
		} else {
			request.setAttribute("org.apache.struts.action.ERROR", errors);
			return;
		}
	}

	public static final ServiziDelegate getInstance(HttpServletRequest request) {
		ServiziDelegate delegate = (ServiziDelegate) request.getAttribute(SERVIZI_DELEGATE);
		if (delegate == null) {
			delegate = new ServiziDelegate(request);
			request.setAttribute(SERVIZI_DELEGATE, delegate);
		}
		return delegate;
	}

	protected ServiziDelegate(HttpServletRequest request) {
		this.request = request;
		try {
			this.factory = FactoryEJBDelegate.getInstance();
			this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		} catch (Exception e) {
			log.error("", e);
		}
		this.utente = Navigation.getInstance(request).getUtente();
	}

	public DescrittoreBloccoVO caricaBlocco(String ticket, String idLista,
			int numBlocco) throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().nextBlocco(ticket, idLista, numBlocco);
		return lista;
	}


	public MovimentoListaVO getDettaglioMovimento(MovimentoVO mov, Locale locale)
			throws Exception {
		return factory.getGestioneServizi().getMovimentoListaVO(utente.getTicket(), mov, locale);
	}

	// Utenti
	public DescrittoreBloccoVO caricaListaUtenti(HttpServletRequest request,
			String ticket, RicercaUtenteBibliotecaVO ricerca, int elemBlocco)
			throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().getListaUtenti(ticket, ricerca, elemBlocco);
		return lista;
	}

	// Anagrafica Servizi Utente
	public DescrittoreBloccoVO caricaListaAnagServiziUte(String ticket,
			UtenteBibliotecaVO utente, int elemBlocco) throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().getListaAnagServiziUte(ticket,
				utente, elemBlocco);
		return lista;
	}

	// Anagrafica Servizi
	// public DescrittoreBloccoVO caricaListaAnagServizi(HttpServletRequest
	// request, String ticket,
	// List serviziAssociati, String codiceBiblioteca, int elemBlocco)
	// throws Exception {
	// DescrittoreBloccoVO lista = null;
	// lista =
	//factory.getGestioneServizi().getListaAnagServizi(utenteCollegato.getTicket
	// (), ticket,
	// serviziAssociati, codiceBiblioteca, elemBlocco);
	// return lista;
	// }
	//
	public DescrittoreBloccoVO caricaListaServiziBiblioteca(
			List serviziAssociati, String codicePolo,
			String codiceBiblioteca, String ticket, int elemBlocco)
			throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().getListaServiziBiblioteca(ticket,
				serviziAssociati, codicePolo, codiceBiblioteca, elemBlocco);
		return lista;
	}

	// Autorizzazioni
	public DescrittoreBloccoVO caricaListaAutorizzazioni(
			RicercaAutorizzazioneVO ricercaAut) throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().getListaAutorizzazioni(
				utente.getTicket(), ricercaAut);

		return lista;
	}

	// Materie di Interesse
	public void cancellaMaterie(Integer[] idMaterie, String uteVar)
			throws Exception {
		factory.getGestioneServizi().cancellaMaterie(
				utente.getTicket(), idMaterie, uteVar);
	}

	public void aggiornaMateria(MateriaVO materiaVO, boolean nuovo)
			throws RemoteException, AlreadyExistsException {
		try {
			factory.getGestioneServizi().aggiornaMateria(
					utente.getTicket(), materiaVO, nuovo);
		} catch (AlreadyExistsException e) {
			throw e;
		}
	}

	public DescrittoreBloccoVO caricaListaMaterie(HttpServletRequest request,
			String ticket, RicercaMateriaVO materiaRicerca) throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().getListaMaterie(ticket,
				materiaRicerca);
		return lista;
	}

	public List<MateriaVO> getListaMaterie(RicercaMateriaVO materiaRicerca)
			throws Exception {
		return factory.getGestioneServizi().getListaMaterieCompleta(
				utente.getTicket(), materiaRicerca);
	}

	// Occupazioni
	public void cancellaOccupazioni(Integer[] id, String uteVar)
			throws Exception {
		factory.getGestioneServizi().cancellaOccupazioni(
				utente.getTicket(), id, uteVar);
	}

	public DescrittoreBloccoVO caricaListaOccupazioni(
			RicercaOccupazioneVO ricerca) throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().getListaOccupazioni(
				utente.getTicket(), ricerca);
		return lista;
	}

	public void aggiornaOccupazione(OccupazioneVO occupazioneVO,
			boolean nuovo) throws RemoteException, AlreadyExistsException {
		try {
			factory.getGestioneServizi().aggiornaOccupazione(
					utente.getTicket(), occupazioneVO, nuovo);
		} catch (AlreadyExistsException e) {
			throw e;
		}
	}

	// Segnature
	// public DescrittoreBloccoVO caricaListaSegnature(HttpServletRequest
	// request,
	// String codBiblioteca, String ticket, int elemBlocco)
	// throws Exception {
	// DescrittoreBloccoVO lista = null;
	// lista =
	// factory.getGestioneServizi().getListaSegnature(utenteCollegato.getTicket
	// (), codBiblioteca,
	// ticket, elemBlocco);
	// return lista;
	// }

	// SpecialitaTds
	public void aggiornaSpecTitoloStudio(SpecTitoloStudioVO specTitoloVO,
			boolean nuovo) throws Exception, AlreadyExistsException {
		try {
			factory.getGestioneServizi().aggiornaSpecTitoloStudio(
					utente.getTicket(), specTitoloVO, nuovo);
		} catch (AlreadyExistsException e) {
			throw e;
		}
	}

	public DescrittoreBloccoVO caricaListaSpecialita(
			RicercaTitoloStudioVO RicTDS) throws Exception {
		DescrittoreBloccoVO lista = null;
		lista = factory.getGestioneServizi().getListaTitoloStudio(
				utente.getTicket(), RicTDS);
		return lista;
	}

	public void cancellaSpecTitoloStudio(Integer[] id, String uteVar)
			throws Exception {
		factory.getGestioneServizi().cancellaSpecTitoloStudio(
				utente.getTicket(), id, uteVar);
	}

	// Movimenti
	public Map getListaMovimentiPerErogazione(HttpServletRequest request,
			MovimentoVO anaMov, RicercaRichiesteType tipoRicerca, Locale locale) throws Exception {
		return factory.getGestioneServizi().getListaMovimentiPerErogazione(utente.getTicket(),
				anaMov, tipoRicerca, locale);
	}

	public Map getListaMovimenti(MovimentoVO anaMov,
			RicercaRichiesteType ricercaRichiesteType, String ticket, Locale locale) throws Exception {
		return factory.getGestioneServizi().getListaMovimenti(ticket, anaMov,
				ricercaRichiesteType, locale);
	}

	// Solleciti
	public List<SollecitiVO> caricaListaSollecitiUte(MovimentoVO anaMov,
			RicercaRichiesteType tipoRicerca, String ticket) throws Exception {
		List lista = null;
		lista = factory.getGestioneServizi().getListaSollecitiUte(ticket,
				anaMov, tipoRicerca);
		return lista;
	}

	public DescrittoreBloccoVO verificaEsistenzaUtentePolo(
			UtenteBibliotecaVO uteAna, String ticket) throws Exception {
		return factory.getGestioneServizi().verificaEsistenzaUtentePolo(ticket,
				uteAna);
	}

	public DescrittoreBloccoVO verificaEsistenzaUtentePolo(BibliotecaVO bibVO,
			String codPolo, String codBib, BaseVO vo, String ticket)
			throws Exception {
		return factory.getGestioneServizi().verificaEsistenzaUtentePolo(ticket,
				bibVO, codPolo, codBib, vo);
	}

	// Metodi di utility
	public List<ComboCodDescVO> loadCodici(CodiciType codice)
			throws RemoteException, CreateException {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		List listaCodice = factory.getCodici().getListaCodice(codice);

		return carCombo.loadComboCodiciDesc(listaCodice);
	}

	public List<ComboCodDescVO> loadCodici(CodiciType codice,
			List listaCodici) throws RemoteException, CreateException {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		List listaCodice = factory.getCodici().getListaCodice(codice,
				listaCodici);

		return carCombo.loadComboCodiciDesc(filtraCodiciScaduti(listaCodice));
	}

	public List<ComboCodDescVO> loadCodiciDiversiDa(CodiciType codice,
			List listaCodici) throws RemoteException, CreateException {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		List listaCodice = factory.getCodici().getListaCodiceDiversiDa(codice,
				listaCodici);

		return carCombo.loadComboCodiciDesc(filtraCodiciScaduti(listaCodice));
	}

	public TB_CODICI getCodice(String codiceRicerca, CodiciType tipoCodice,
			CodiciRicercaType tipoRicerca) throws RemoteException,
			CreateException {
		return factory.getCodici().cercaCodice(codiceRicerca, tipoCodice,
				tipoRicerca);
	}

	public List<ComboCodDescVO> loadCodiciCategoriaDiFruizione()
			throws RemoteException, CreateException {
		List<TB_CODICI> listaCodice = factory.getCodici()
				.getCodiceCategoriaDiFruizione();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		return carCombo.loadComboCodiciDesc(filtraCodiciScaduti(listaCodice));
	}

	public List<ComboCodDescVO> loadCodiciNonDisponibilita()
			throws RemoteException, CreateException {
		List<TB_CODICI> listaCodice = factory.getCodici()
				.getCodiceNonDisponibilita();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		return carCombo.loadComboCodiciDesc(filtraCodiciScaduti(listaCodice));
	}

	private List<TB_CODICI> filtraCodiciScaduti(List<TB_CODICI> listaIn) {
		List<TB_CODICI> listaOut = new ArrayList<TB_CODICI>();

		Iterator<TB_CODICI> iterator = listaIn.iterator();
		TB_CODICI codice;
		Date oggi = Calendar.getInstance().getTime();

		while (iterator.hasNext()) {
			codice = iterator.next();
			if (codice.getDt_fine_validita() == null
					|| codice.getDt_fine_validita().after(oggi))
				listaOut.add(codice);
		}

		return listaOut;
	}

	// //////////////////////////////////////////////////////////

	/**
	 * Torna la lista dei codici tipi servizio associati alla biblioteca<br/>
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java
	 * getCodiciTipiServizio List<String>
	 *
	 * @param codPolo
	 * @param codBib
	 * @return
	 * @throws RemoteException
	 *
	 *
	 */
	public List<String> getCodiciTipiServizio(String codPolo, String codBib)
			throws RemoteException {
		List<String> lstTipiServizio = factory.getGestioneServizi()
				.getListaCodiciTipiServizio(utente.getTicket(),
						codPolo, codBib);
		return lstTipiServizio;

	}

	public java.util.List getListaTipiServizio(String codPolo, String codBib)
			throws RemoteException {
		return factory.getGestioneServizi().getListaTipiServizio(
				utente.getTicket(), codPolo, codBib);
	}

	/**
	 * Torna una istanza di TipoServizioVO<br/> it.iccu.sbn.web.actions.servizi
	 * ServiziBaseAction.java getTipoServizio TipoServizioVO
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServizio
	 * @return TipoServizioVO
	 * @throws RemoteException
	 *
	 *
	 */
	public TipoServizioVO getTipoServizio(String codPolo, String codBib,
			String codTipoServizio) throws RemoteException {
		return factory.getGestioneServizi().getTipoServizio(
				utente.getTicket(), codPolo, codBib, codTipoServizio);
	}

	/**
	 *
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java
	 * getTariffeModalitaErogazione List<TariffeModalitaErogazioneVO>
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServizio
	 * @param fl_svolg
	 * @return
	 * @throws RemoteException
	 * @throws CreateException
	 *
	 *             <br/>Torna la lista di TariffeModalitaErogazione relative al
	 *             codice tipo servizio passato
	 */
	public List<TariffeModalitaErogazioneVO> getTariffeModalitaErogazioneServizio(
			String codPolo, String codBib, String codTipoServizio, String fl_svolg)
			throws RemoteException, CreateException {
		// Caricamento tariffe ModalitÃ  Erogazione
		List<TariffeModalitaErogazioneVO> tariffeErogazioneVO = factory
				.getGestioneServizi().getTariffaModalitaErogazione(
						utente.getTicket(), codPolo, codBib,
						codTipoServizio, fl_svolg);

		// Caricamento descrizioni modalitÃ  di erogazione
		Iterator<TariffeModalitaErogazioneVO> i = tariffeErogazioneVO.iterator();
		while (i.hasNext()) {
			TariffeModalitaErogazioneVO tme = i.next();
			String descr = ValidazioneDati.coalesce(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE, tme.getCodErog()),
					factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE_ILL, tme.getCodErog()));
			tme.setDesModErog(descr);
		}

		return tariffeErogazioneVO;
	}

	/**
	 *
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java
	 * getTariffeModalitaErogazione List<TariffeModalitaErogazioneVO>
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServizio
	 * @param natura
	 * @return
	 * @throws RemoteException
	 * @throws CreateException
	 *
	 *             <br/>Torna la lista di TariffeModalitaErogazione relative al
	 *             codice tipo servizio passato
	 */
	public List<ServizioWebDatiRichiestiVO> getModuloRichiesta(
			String codPolo, String codBib, String codTipoServizio, String natura)
			throws RemoteException, CreateException {
		// Caricamento Modulo Richiesta
		List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO = factory
				.getGestioneServizi().getServizioWebDatiRichiesti(
						utente.getTicket(), codPolo, codBib,
						codTipoServizio, natura);

//		// Caricamento descrizioni Modulo Richiesta
//		Iterator<ServizioWebDatiRichiestiVO> iterator = lstServizioWebDatiRichiestiVO
//				.iterator();
//		ServizioWebDatiRichiestiVO servizioWebDatiRichiestiVO = null;
//		while (iterator.hasNext()) {
//			servizioWebDatiRichiestiVO = iterator.next();
//			servizioWebDatiRichiestiVO.setDescrizione(factory.getCodici()
//					.getDescrizioneCodiceSBN(
//							CodiciType.CODICE_CAMPO_RICHIESTA_WEB,
//							String.valueOf(servizioWebDatiRichiestiVO.getCampoRichiesta())));
//		}

		return lstServizioWebDatiRichiestiVO;
	}


	/**
	 *
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java
	 * getSupportiModalitaErogazione List<SupportiModalitaErogazioneVO>
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codSupporto
	 * @param object
	 * @return
	 * @throws RemoteException
	 * @throws CreateException
	 *
	 *             <br/>Torna la lista di SupportiModalitaErogazione relative al
	 *             codice tipo servizio passato
	 */
	public List<SupportiModalitaErogazioneVO> getSupportiModalitaErogazione(
			String codPolo, String codBib, String codSupporto, String fl_svolg)
			throws RemoteException, CreateException {
		// Caricamento tariffe ModalitÃ  Erogazione
		List<SupportiModalitaErogazioneVO> tariffeErogazioneVO = factory
				.getGestioneServizi().getSupportoModalitaErogazione(
						utente.getTicket(), codPolo, codBib,
						codSupporto, fl_svolg);

		// Caricamento descrizioni modalitÃ  di erogazione
		Iterator<SupportiModalitaErogazioneVO> i = tariffeErogazioneVO.iterator();

		while (i.hasNext()) {
			SupportiModalitaErogazioneVO tme = i.next();
			String descr = ValidazioneDati.coalesce(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE, tme.getCodErog()),
					factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE_ILL, tme.getCodErog()));
			tme.setDesModErog(descr);
		}

		return tariffeErogazioneVO;
	}


	/**
	 *
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java
	 * getTariffeModalitaErogazione List<TariffeModalitaErogazioneVO>
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServizio
	 * @return
	 * @throws RemoteException
	 * @throws CreateException
	 *
	 *             <br/>Torna la lista di TariffeModalitaErogazione relative al
	 *             codice tipo servizio passato
	 */
	public List<ModalitaErogazioneVO> getTariffeModalitaErogazione(
			String codPolo, String codBib)
			throws RemoteException, CreateException {
		// Caricamento tariffe ModalitÃ  Erogazione
		List<ModalitaErogazioneVO> tariffeErogazioneVO = factory
				.getGestioneServizi().getTariffaModalitaErogaz(
						utente.getTicket(), codPolo, codBib);

		// Caricamento descrizioni modalitÃ  di erogazione
		Iterator<ModalitaErogazioneVO> i = tariffeErogazioneVO
				.iterator();

		while (i.hasNext()) {
			ModalitaErogazioneVO me = i.next();
			String descr = ValidazioneDati.coalesce(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE, me.getCodErog()),
					factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE_ILL, me.getCodErog()));
			me.setDesModErog(descr);
		}

		return tariffeErogazioneVO;
	}


	/**
	 *
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java getIterServizio
	 * List<IterServizioVO>
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServizio
	 * @return
	 * @throws RemoteException
	 * @throws CreateException
	 *
	 *             <br/> Torna la lista dioggetti IterServizioVO associati al
	 *             codice tipo servizio passato
	 */
	public List<IterServizioVO> getIterServizio(String codPolo, String codBib,
			String codTipoServizio) throws RemoteException, CreateException {
		List<IterServizioVO> listaAttivita = factory.getGestioneServizi()
				.getIterServizio(utente.getTicket(), codPolo, codBib,
						codTipoServizio);
		// Caricamento descrizioni
		Iterator<IterServizioVO> iteratorServizioVO = listaAttivita.iterator();
		IterServizioVO iterServizioVO = null;
		while (iteratorServizioVO.hasNext()) {
			iterServizioVO = iteratorServizioVO.next();
			iterServizioVO.setDescrizione(factory.getCodici()
					.getDescrizioneCodiceSBN(CodiciType.CODICE_ATTIVITA_ITER,
							iterServizioVO.getCodAttivita()).trim());
		}

		return listaAttivita;
	}

	public List<FaseControlloIterVO> getControlloIter(int idTipoServizio,
			String codAttivita) throws RemoteException, CreateException {
		List<FaseControlloIterVO> listaControllo = factory.getGestioneServizi()
				.getControlloIter(utente.getTicket(), idTipoServizio,
						codAttivita);
		Iterator<FaseControlloIterVO> iterator = listaControllo.iterator();
		FaseControlloIterVO controlloIterVO = null;
		while (iterator.hasNext()) {
			controlloIterVO = iterator.next();
			controlloIterVO.setDescControllo(factory.getCodici()
					.getDescrizioneCodiceSBN(
							CodiciType.CODICE_FUNZIONE_CONTROLLO_ITER,
							controlloIterVO.getCodControllo().toString()));
		}

		return listaControllo;
	}

	/**
	 *
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java getListaServizi
	 * List<ServizioAllVO>
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServizio
	 * @return
	 * @throws RemoteException
	 *
	 *             <br/>Restituisce la lista dei servizi associati a un tipo
	 *             servizio
	 */
	public List<ServizioBibliotecaVO> getListaServizi(String codPolo,
			String codBib, String codTipoServizio) throws RemoteException {
		List<ServizioBibliotecaVO> listaServizi = factory.getGestioneServizi()
				.getListaServiziPerTipoServizio(utente.getTicket(),
						codPolo, codBib, codTipoServizio);

		return listaServizi;
	}

	/**
	 * Torna un'istanza di ParametriBibliotecaVO <br/>
	 * it.iccu.sbn.web.actions.servizi ServiziBaseAction.java
	 * getParametriBiblioteca ParametriBibliotecaVO
	 *
	 * @param codPolo
	 * @param codBib
	 * @return
	 * @throws RemoteException
	 *
	 *
	 */
	public ParametriBibliotecaVO getParametriBiblioteca(String codPolo,
			String codBib) throws RemoteException {
		return factory.getGestioneServizi().getParametriBiblioteca(
				utente.getTicket(), codPolo, codBib);
	}

	public List<SupportoBibliotecaVO> getSupportiBiblioteca(String codPolo,
			String codBib, String fl_svolg) throws RemoteException, CreateException {
		List<SupportoBibliotecaVO> listaSupporti = factory.getGestioneServizi()
				.getSupportiBiblioteca(utente.getTicket(), codPolo,	codBib, fl_svolg);
		SupportoBibliotecaVO supporto = null;
		Iterator<SupportoBibliotecaVO> i = listaSupporti.iterator();
		while (i.hasNext()) {
			supporto = i.next();
			if (supporto.isLocale() )
				supporto.setDescrizione(factory.getCodici()
						.getDescrizioneCodiceSBN(CodiciType.CODICE_SUPPORTO_COPIA,
								supporto.getCodSupporto().trim()));
			else
				supporto.setDescrizione(factory.getCodici()
						.getDescrizioneCodiceSBN(CodiciType.CODICE_SUPPORTO_COPIA_ILL,
								supporto.getCodSupporto().trim()));
		}
		return listaSupporti;
	}

	public SupportoBibliotecaVO getSupportoBiblioteca(String codPolo,
			String codBib, String codSupporto) throws RemoteException, CreateException {
		SupportoBibliotecaVO supporto = factory.getGestioneServizi()
				.getSupportoBiblioteca(utente.getTicket(), codPolo,
						codBib, codSupporto);
		if (supporto != null) {
			if (supporto.isLocale() )
				supporto.setDescrizione(factory.getCodici()
						.getDescrizioneCodiceSBN(CodiciType.CODICE_SUPPORTO_COPIA,
								supporto.getCodSupporto().trim()));
			else
				supporto.setDescrizione(factory.getCodici()
						.getDescrizioneCodiceSBN(CodiciType.CODICE_SUPPORTO_COPIA_ILL,
								supporto.getCodSupporto().trim()));
		}
		return supporto;
	}

	public List<ModalitaPagamentoVO> getModalitaPagamento(String codPolo,
			String codBib) throws RemoteException, CreateException {
		return factory.getGestioneServizi().getModalitaPagamento(
				utente.getTicket(), codPolo, codBib);

	}

	public UtenteBaseVO getUtente(String codPolo, String codBibUte,
			String codUtente, String codBib) throws RemoteException {
		return factory.getGestioneServizi().getUtente(
				utente.getTicket(), codPolo, codBibUte, codUtente,
				codBib);
	}

	public UtenteBaseVO getUtente(String codUtente) throws RemoteException {
		return factory.getGestioneServizi().getUtente(
				utente.getTicket(), codUtente);
	}

	public void cancellaRichieste(Long[] codSelMov, String uteVar)
			throws RemoteException {
		factory.getGestioneServizi().cancellaRichieste(
				utente.getTicket(), codSelMov, uteVar);
	}

	public void rifiutaRichieste(Long[] codSelMov, String uteVar, boolean inviaMailNotifica)
			throws RemoteException {
		factory.getGestioneServizi().rifiutaRichieste(
				utente.getTicket(), codSelMov, uteVar, inviaMailNotifica);
	}

	public List<ServizioBibliotecaVO> getServiziAttivi(String codPolo,
			String codBibUte, String codUte, String codBibOperante,
			Timestamp data, boolean remoto) throws RemoteException {
		return factory.getGestioneServizi().getServiziAttivi(
				utente.getTicket(), codPolo, codBibUte, codUte,
				codBibOperante, data, remoto);
	}

	public List<ServizioBibliotecaVO> getServiziAttivi(String codPolo,
			String codBibUte, String codUte, String codBibOperante,
			Timestamp data) throws RemoteException {
		return factory.getGestioneServizi().getServiziAttivi(
				utente.getTicket(), codPolo, codBibUte, codUte,
				codBibOperante, data, false);
	}

	public List<ServizioBibliotecaVO> getServiziAttiviPerCatFruizione(String codPolo,
			String codBib, String codFrui)
			throws RemoteException {
		return factory.getGestioneServizi().getServiziAttivi(
				utente.getTicket(), codPolo, codBib, codFrui);
	}

	public InfoDocumentoVO getInfoInventario(String codPolo, String codBib,
			String serie, int codInv, String ticket, Locale locale)
			throws Exception {
		InfoDocumentoVO infoDocumento = null;
		InventarioDettaglioVO inventario = null;
		try {
			inventario = DomainEJBFactory.getInstance().getInventario()
					.getInventarioDettaglio(codPolo, codBib, serie, codInv,
							locale, ticket, false);
		} catch (DataException e) {
			log.error("", e);
		}

		//almaviva5_20190111 #6872
		if (inventario != null && inventario.isCollocato()) {
			infoDocumento = new InfoDocumentoVO();
			InventarioTitoloVO inventarioTitolo = new InventarioTitoloVO(inventario);
			infoDocumento.setInventarioTitoloVO(inventarioTitolo);
			infoDocumento.setTitolo(inventario.getTitIsbd());

			inventarioTitolo.setCodLoc(inventario.getCollCodLoc());

			// segnatura = Sezione + Collocazione + Specificazione + Numero di sequenza
			StringBuilder buf = new StringBuilder();
			buf.append(ValidazioneDati.trimOrEmpty(inventario.getCollCodSez())).append(' ');
			buf.append(ValidazioneDati.trimOrEmpty(inventario.getCollCodLoc())).append(' ');
			buf.append(ValidazioneDati.trimOrEmpty(inventario.getCollSpecLoc())).append(' ');
			buf.append(ValidazioneDati.trimOrEmpty(inventario.getSeqColl()));

			infoDocumento.setSegnatura(buf.toString());
		}

		return infoDocumento;
	}

	public MovimentoVO aggiornaRichiesta(MovimentoVO movimento, int idServizio) throws Exception {
		return factory.getGestioneServizi().aggiornaRichiesta(
				utente.getTicket(), movimento, idServizio);
	}

	public MovimentoVO aggiornaRichiestaPerCambioServizio(MovimentoVO nuovaRichiesta,
			long codRichDaCancellare, int idServizio, String uteVar)
			throws RemoteException {
		return factory.getGestioneServizi().aggiornaRichiestaPerCambioServizio(
				utente.getTicket(), nuovaRichiesta,
				codRichDaCancellare, idServizio, uteVar);
	}

	public String getDescrizioneCodiceSBN(CodiciType type, String code)
			throws RemoteException, CreateException {
		return factory.getCodici().getDescrizioneCodiceSBN(type, code);
	}

	public List<ControlloAttivitaServizio> getListaAttivitaSuccessive(
			String codPolo, String codBib, String codTipoServ, int progrIter, DatiRichiestaILLVO datiILL)
			throws RemoteException {
		// La lista tornata Ã¨ di istanze della classe
		// it.iccu.sbn.ejb.vo.servizi.Attivita
		// mentre il metodo deve tornare una lista di istanze di
		// it.iccu.sbn.web.integration.servizi.erogazione.Attivita
		List<AttivitaServizioVO> listaAttivita = factory
				.getGestioneServizi().getListaAttivitaSuccessive(
						utente.getTicket(), codPolo, codBib,
						codTipoServ, progrIter, datiILL);
		Iterator<AttivitaServizioVO> i = listaAttivita.iterator();

		List<ControlloAttivitaServizio> listaAttivitaOutput = new ArrayList<ControlloAttivitaServizio>();
		while (i.hasNext()) {
			AttivitaServizioVO attivitaInput = i.next();
			listaAttivitaOutput.add(new ControlloAttivitaServizio(attivitaInput));
		}

		return listaAttivitaOutput;
	}

	public ControlloAttivitaServizio getAttivita(
			String codPolo, IterServizioVO iterServizioVO)
			throws RemoteException {
		AttivitaServizioVO attivitaInput = factory.getGestioneServizi()
				.getAttivita(utente.getTicket(), codPolo, iterServizioVO);
		ControlloAttivitaServizio attivitaOutput = new ControlloAttivitaServizio(attivitaInput);

		return attivitaOutput;
	}

	public void sospendiDirittoUtente(MovimentoVO mov, Date dataSospensione,
			BaseVO datiModifica) throws Exception {
		factory.getGestioneServizi()
				.sospendiDirittoUtente(utente.getTicket(), mov,
						dataSospensione, datiModifica);
	}

	public boolean esistonoPrenotazioni(UtenteBaseVO utenteBaseVO,
			MovimentoVO anaMov, Timestamp data) throws RemoteException {
		return factory.getGestioneServizi().esistonoPrenotazioni(
				utente.getTicket(), utenteBaseVO, anaMov, data);
	}

	public boolean esistonoPrenotazioni(MovimentoVO anaMov, Timestamp data)
			throws RemoteException {
		return factory.getGestioneServizi().esistonoPrenotazioni(
				utente.getTicket(), anaMov, data);
	}

	public int getNumeroPrenotazioni(MovimentoVO anaMov) throws RemoteException {
		return factory.getGestioneServizi().getNumeroPrenotazioni(
				utente.getTicket(), anaMov);
	}

	public int getNumeroPrenotazioni() throws RemoteException {
		return factory.getGestioneServizi().getNumeroPrenotazioni(
				utente.getTicket());
	}

	public DescrittoreBloccoVO getPrenotazioni(MovimentoVO anaMov,
			String codBibDest, Locale locale)
			throws RemoteException {
		return factory.getGestioneServizi().getPrenotazioni(
				utente.getTicket(), anaMov, locale);
	}

	public DescrittoreBloccoVO getPrenotazioni(String codBibDest, int maxRighe,
			String ticket, Locale locale, String tipoOrd) throws RemoteException {
		return factory.getGestioneServizi().getPrenotazioni(
				utente.getTicket(), codBibDest, maxRighe, locale, tipoOrd);
	}

	public DescrittoreBloccoVO getProroghe(String codBibDest, int maxRighe,
			String ticket, Locale locale, String tipoOrd) throws RemoteException {
		return factory.getGestioneServizi().getProroghe(
				utente.getTicket(), codBibDest, maxRighe, locale, tipoOrd);
	}

	public DescrittoreBloccoVO getGiacenze(String codBibDest, int maxRighe,
			String ticket, Locale locale, String tipoOrd) throws RemoteException {
		// almaviva5_20150701
		DescrittoreBloccoVO blocco1 = factory.getGestioneServizi()
				.getGiacenze(utente.getTicket(), codBibDest, 0, locale, tipoOrd);
		if (DescrittoreBloccoVO.isFilled(blocco1))
			blocco1 = DescrittoreBlocchiUtil.saveBlocchi(ticket, blocco1.getLista(),
				maxRighe, new GiacenzePrenotazioniCountInterceptor(this));

		return blocco1;
	}

	public UtenteBibliotecaVO importaBibliotecaComeUtente(String codPolo,
			String codBib, BibliotecaVO bibVO, BaseVO vo, Locale locale)
			throws RemoteException {
		return factory.getGestioneServizi()
				.importaBibliotecaComeUtente(utente.getTicket(),
						codPolo, codBib, bibVO, vo, locale);
	}

	public void aggiornaSegnatura(RangeSegnatureVO segnaturaVO, boolean isNew)
			throws RemoteException, IntervalloSegnaturaNonValidoException,
			AlreadyExistsException {
		factory.getGestioneServizi().aggiornaSegnatura(
				utente.getTicket(), segnaturaVO, isNew);
	}

	public void cancellaSegnature(Integer[] id, String uteVar)
			throws RemoteException {
		factory.getGestioneServizi().cancellaSegnature(
				utente.getTicket(), id, uteVar);
	}

	public DescrittoreBloccoVO caricaSegnature(String codPolo, String codBib,
			RangeSegnatureVO segnaturaVO, String ticket) throws RemoteException {
		return factory.getGestioneServizi().caricaSegnature(
				utente.getTicket(), codPolo, codBib, segnaturaVO);
	}

	public List<RelazioniVO> caricaRelazioni(String codPolo, String codBib,
			String codRelazione) throws RemoteException {
		return factory.getGestioneServizi().caricaRelazioni(
				utente.getTicket(), codPolo, codBib, codRelazione);
	}

	public void cancellaRelazioni(Integer[] id, String uteVar)
			throws RemoteException {
		factory.getGestioneServizi().cancellaRelazioni(
				utente.getTicket(), id, uteVar);
	}

	public void riattivaRelazioni(Integer[] id, String uteVar)
			throws RemoteException {
		factory.getGestioneServizi().riattivaRelazioni(
				utente.getTicket(), id, uteVar);
	}

	public void aggiornaRelazione(RelazioniVO relazioneVO)
			throws RemoteException, AlreadyExistsException {
		factory.getGestioneServizi().aggiornaRelazione(
				utente.getTicket(), relazioneVO);
	}

	public boolean isUtenteAutorizzato(String codPolo, String codBibUte,
			String codUtente, String codBib, String codTipoServ, String codServ)
			throws RemoteException {

		return factory.getGestioneServizi().isUtenteAutorizzato(
				utente.getTicket(), codPolo, codBibUte, codUtente,
				codBib, codTipoServ, codServ,
				DaoManager.now());
	}

	public DescrittoreBloccoVO caricaListeTematiche(MovimentoVO filtroMov,
			boolean attivitaAttuale, int elemBlocco) throws RemoteException {
		DescrittoreBloccoVO blocco1 = factory.getGestioneServizi()
				.getListeTematiche(utente.getTicket(), filtroMov, attivitaAttuale, elemBlocco);
		if (!DescrittoreBloccoVO.isFilled(blocco1) )
			throw new ValidationException(SbnErrorTypes.SRV_NUSSUN_ELEMENTO_TROVATO);

		return blocco1;
	}

	public ServizioBibliotecaVO getServizioBiblioteca(String codPolo,
			String codBib, String codTipoServizio, String codServ)
			throws RemoteException {

		return factory.getGestioneServizi().getServizioBiblioteca(
				utente.getTicket(), codPolo, codBib, codTipoServizio,
				codServ);
	}

	public boolean esisteMovimentoAttivo(MovimentoVO movimento) throws RemoteException {
		return factory.getGestioneServizi().esisteMovimentoAttivo(
				utente.getTicket(), movimento);
	}

	public boolean movimentoStatoPrecedeConsegnaDocLett(MovimentoListaVO movimento) throws RemoteException {
		return factory.getGestioneServizi().movimentoStatoPrecedeConsegnaDocLett(
				utente.getTicket(), movimento);
	}


	public DescrittoreBloccoVO getListaDocumentiNonSbn(DocumentoNonSbnRicercaVO filtro) throws Exception {
		DocumentoNonSbnRicercaVO filtro2 = filtro.copy();
		filtro2.setElementiPerBlocco(0); //disattivo blocchi
		DescrittoreBloccoVO blocco1 = factory.getGestioneServizi().getListaDocumentiNonSbn(utente.getTicket(), filtro2);
		if (DescrittoreBloccoVO.isFilled(blocco1)) {

			//almaviva5_20160427 servizi ILL
			DescrittoreBloccoInterceptor interceptor = null;
			if (filtro.isRicercaOpac()) {
				BibliotecaVO bib = factory.getBiblioteca().getBiblioteca(filtro.getCodPolo(), filtro.getCodBib());
				interceptor = new DocNonSbnBibliotecheOpacInterceptor(ServiziILLDelegate.getInstance(request), filtro.getTipoServizio(),
						bib.getCd_ana_biblioteca() );
			}
			else
				interceptor = new DocNonSbnEsemplareCountInterceptor(this);

			blocco1 = DescrittoreBlocchiUtil.saveBlocchi(utente.getTicket(),
					blocco1.getLista(),
					filtro.getElementiPerBlocco(),
					interceptor);
		}

		return blocco1;
	}

	public DescrittoreBloccoVO getListaEsemplariDocumentiNonSbn(DocumentoNonSbnRicercaVO filtro) throws RemoteException {
		return factory.getGestioneServizi().getListaEsemplariDocumentiNonSbn(utente.getTicket(), filtro);
	}

	public List<DocumentoNonSbnVO> aggiornaDocumentoNonSbn(List<DocumentoNonSbnVO> documenti) throws RemoteException, ApplicationException {
		return factory.getGestioneServizi().aggiornaDocumentoNonSbn(utente.getTicket(), documenti);
	}

	public void aggiornaEsemplariDocumentoNonSbn(List<EsemplareDocumentoNonSbnVO> esemplari) throws RemoteException, ApplicationException {
		factory.getGestioneServizi().aggiornaEsemplariDocumentoNonSbn(utente.getTicket(), esemplari);
	}

	public static final Utente getUtenteEJB(HttpServletRequest request) {
		return (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	}

	public List<ComboCodDescVO> getComboCodici(CodiciType type) {

		List<ComboCodDescVO> output = new ArrayList<ComboCodDescVO>();
		try {
			List codici = factory.getCodici().getCodici(type);
			CaricamentoCombo combo = new CaricamentoCombo();
			output = combo.loadComboCodiciDesc(codici);

		} catch (Exception e) {
			log.error(e);
		}

		return output;
	}

	public DocumentoNonSbnVO getDettaglioDocumentoNonSbn(
			DocumentoNonSbnVO documento) throws RemoteException, ApplicationException {

		ActionMessages errors = new ActionMessages();
		try {
			DocumentoNonSbnVO dettaglio = factory.getGestioneServizi().getDettaglioDocumentoNonSbn(utente.getTicket(), documento);
			if (dettaglio == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.ListaVuota"));
				saveErrors(request, errors, null);
			}

			return dettaglio;
		} catch (ApplicationException e) {
			log.error("", e);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage()));
			saveErrors(request, errors, null);
			return null;
		}
	}

	public DocumentoNonSbnVO getDettaglioDocumentoNonSbn(String codPolo, String codBib, String ordSegn) throws RemoteException, ApplicationException {

		ActionMessages errors = new ActionMessages();
		try {
			DocumentoNonSbnVO dettaglio = factory.getGestioneServizi()
					.getCategoriaFruizioneSegnatura(utente.getTicket(), codPolo, codBib, ordSegn);
			if (dettaglio == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.ListaVuota"));
				saveErrors(request, errors, null);
			}

			return dettaglio;
		} catch (ApplicationException e) {
			log.error("", e);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage()));
			saveErrors(request, errors, null);
			return null;
		}
	}

	public CommandResultVO invoke(CommandType command, Serializable... params) {

		ActionMessages errors = new ActionMessages();
		try {
			CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(), command, params);
			invoke.validate();

			return factory.getGestioneServizi().invoke(invoke);

		} catch (ApplicationException e) {
			log.error("", e);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage()));
			saveErrors(request, errors, e);
			return null;

		} catch (ValidationException e) {
			log.error("", e);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage()));
			saveErrors(request, errors, e);
			return null;

		} catch (RemoteException e) {
			log.error("", e);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.generico"));
			saveErrors(request, errors, e);
			return null;
		}
	}

	public ActionForward getSIFListaDocumentiNonSbn(
			SIFListaDocumentiNonSbnVO richiesta) {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		try {
			ParametriServizi parametri = new ParametriServizi();
			parametri.put(ParamType.MODALITA_CERCA_DOCUMENTO, ModalitaCercaType.CERCA_PER_EROGAZIONE);

			parametri.put(ParamType.PARAMETRI_SIF_DOCNONSBN, richiesta);

			ParametriServizi.send(request, parametri);

			// imposto il bookmark che servirÃ  per tornare direttamente alla mappa che ha chiamato il sif.
			navi.addBookmark(richiesta.getRequestAttribute() );
			return navi.goForward(mapping.findForward("sifListaDocumentiNonSbn"));

		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			return mapping.getInputForward();
		}

	}

	public List<ComboCodDescVO> loadCodiciDiversiDaQuelliDefiniti(
			String codPolo, String codBib, List codici, String flSvolg) {
		try {
			List<ComboCodDescVO> loadCodiciDiversiDaQuelliDefiniti = factory.getGestioneServizi().loadCodiciDiversiDaQuelliDefiniti(utente.getTicket(), codPolo, codBib, codici, flSvolg);
			Iterator iterator = loadCodiciDiversiDaQuelliDefiniti.iterator();
			while (iterator.hasNext()) {
				ComboCodDescVO me = (ComboCodDescVO) iterator.next();
				String descr = ValidazioneDati.coalesce(factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE, me.getCodice()),
						factory.getCodici().getDescrizioneCodiceSBN(CodiciType.CODICE_MODALITA_EROGAZIONE_ILL, me.getCodice()));
				me.setDescrizione(descr.trim());
			}
			loadCodiciDiversiDaQuelliDefiniti.add(0, new ComboCodDescVO());
			return loadCodiciDiversiDaQuelliDefiniti;
		}
		catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public List<TariffeModalitaErogazioneVO> getListaModErogLegateASupporti(MovimentoVO mov) throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(), CommandType.LISTA_MOD_EROGAZIONE_LEGATE_SUPPORTI, mov);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			List<TariffeModalitaErogazioneVO> tariffeErogazioneVO = (List<TariffeModalitaErogazioneVO>) result.getResult();
			// Caricamento descrizioni modalitÃ  di erogazione
			Iterator<TariffeModalitaErogazioneVO> iterator = tariffeErogazioneVO
					.iterator();
			TariffeModalitaErogazioneVO tariffaModalitaErog = null;
			while (iterator.hasNext()) {
				tariffaModalitaErog = iterator.next();
				tariffaModalitaErog.setDesModErog(factory.getCodici()
						.getDescrizioneCodiceSBN(
								CodiciType.CODICE_MODALITA_EROGAZIONE,
								tariffaModalitaErog.getCodErog()).trim());
			}

			return tariffeErogazioneVO;

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public MovimentoVO getDettaglioMovimentoDiPrenotazione(	MovimentoListaVO prenotazione, Locale locale) throws Exception {

		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.DETTAGLIO_MOVIMENTO_DI_PRENOTAZIONE, prenotazione, locale);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (MovimentoVO)result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public String getCategoriaFruizioneSegnatura(DocumentoNonSbnVO doc) throws Exception {

		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.CATEGORIA_FRUIZIONE_DOC_NON_SBN_PER_SALVA, doc);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			DocumentoNonSbnVO out = (DocumentoNonSbnVO) result.getResult();

			return out.getCodFruizione();

		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			log.error("", e.detail);
		}

		return null;
	}

	public boolean esisteMovimentoAttivoPerIter(IterServizioVO iter)  throws Exception {

		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.ESISTE_MOVIMENTO_ATTIVO_PER_ITER, iter);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			log.error("", e.detail);
		}

		return false;
	}

	public MovimentoVO inviaNotificaUtentePrenotazione(MovimentoVO mov) throws Exception {
		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_NOTIFICA_UTENTE_PRENOTAZIONE, mov.copy() );
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (MovimentoVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return mov;

	}

	public short cercaEsemplareLibero(String polo, String bib, DocumentoNonSbnVO datiDoc) throws Exception {
		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.CERCA_ESEMPLARE_DOCUMENTO_LIBERO, polo, bib, datiDoc);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (Short) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return 0;

	}

	public List<MovimentoPerStampaServCorrVO> getStampaListeTematicheStorico(MovimentoVO filtro) throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(),  CommandType.LISTA_RICHIESTE_STORICO, filtro);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (List<MovimentoPerStampaServCorrVO>) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;

	}

	public boolean esisteInventarioBibliotecaSistemaMetro(List<BibliotecaVO> filtroBib, String bid) throws Exception {

		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.ESISTE_INVENTARIO_BIB_SISTEMA_METRO, (Serializable)filtroBib, bid);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return false;

	}

	public boolean isAbilitatoErogazione() throws Exception {
		try {
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().SRV_EROGAZIONE);
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			return false;
		}
	}

	public ActionForward sifDettaglioUtente(String codBib, String codBibUte, String codUtente, int idUtenteBib) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		RicercaUtenteBibliotecaVO richiesta = new RicercaUtenteBibliotecaVO();
		richiesta.setCodPolo(navi.getUtente().getCodPolo());
		richiesta.setCodBib(codBib);
		richiesta.setCodBibUte(codBibUte);
		richiesta.setCodUte(codUtente);
		richiesta.setIdUte(idUtenteBib);

		UtenteBibliotecaVO uteBib = factory.getGestioneServizi().getDettaglioUtente(navi.getUserTicket(), richiesta,
				ServiziConstant.NUMBER_FORMAT_CONVERTER, Locale.getDefault() );

		if (uteBib == null || uteBib.getCodiceUtente() == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.UtenteNonPresente"));
			return mapping.getInputForward();
		}

		request.setAttribute("Nuovo", "O");
		request.setAttribute("PathForm", mapping.getPath());
		request.setAttribute("UtenteScelto", uteBib);
		request.setAttribute("RicercaUtenti", richiesta);

		return navi.goForward(mapping.findForward("sifDettaglioUtente"));

	}

	public ActionForward sifDettaglioDocumentoNonSBN(int idDocumento) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();
		DocumentoNonSbnVO richiesta = new DocumentoNonSbnVO();
		richiesta.setIdDocumento(idDocumento);
		DocumentoNonSbnVO doc = getDettaglioDocumentoNonSbn(richiesta);
		if (doc == null)
			return mapping.getInputForward();

		ParametriServizi parametri = new ParametriServizi();

		parametri.put(ParamType.DETTAGLIO_DOCUMENTO, new DocumentoNonSbnDecorator(doc) );
		parametri.put(ParamType.MODALITA_CERCA_DOCUMENTO, ModalitaCercaType.CERCA_PER_EROGAZIONE);
		parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE_SIF);
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("sifDettaglioDocumentoNonSbn"));
	}

	public ActionForward sifEsameInventario(MovimentoVO mov) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		request.setAttribute("codBib", mov.getCodBibInv());
		request.setAttribute("codSerie", mov.getCodSerieInv());
		request.setAttribute("codInvent", mov.getCodInvenInv());
		request.setAttribute("prov", "interrogazioneEsame");

		return Navigation.getInstance(request).goForward(mapping.findForward("sifEsameInventario"));
	}

	public int countEsemplariDocumento(int idDocumento) throws Exception {
		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_CONTA_ESEMPLARI_DOCUMENTO_LIBERO, new Integer(idDocumento));
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (Integer) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return 0;
	}

	public DescrittoreBloccoVO getListaRichiesteILL(DatiRichiestaILLRicercaVO ricerca) throws Exception {
		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
				CommandType.SRV_ILL_RICERCA_RICHIESTE, ricerca.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			List<DatiRichiestaILLVO> richieste = (List<DatiRichiestaILLVO>) result.getResult();
			DescrittoreBloccoVO blocco1 = DescrittoreBlocchiUtil.saveBlocchi(
					utente.getTicket(), richieste,
					ricerca.getNumeroElementiBlocco(), null);

			return blocco1;

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public List<UtenteBaseVO> checkMailUtenteBiblioteca(String idUtente, String mail1, String mail2) throws Exception {
		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_CONTROLLA_MAIL_UTENTE, idUtente, mail1, mail2);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (List<UtenteBaseVO>) result.getResult();

		} catch (SbnBaseException e) {
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;

	}

	public String validaModelloSollecito(ModelloSollecitoVO modello, TipoModello tipo)
			throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(),
				CommandType.SRV_VALIDA_MODELLO_SOLLECITO, modello, tipo);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (String) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}

	}


	public MovimentoVO aggiornaRichiestaPerProroga(MovimentoVO movimento, Date dataProroga)
			throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(
				utente.getTicket(),
				CommandType.SRV_AGGIORNA_RICHIESTA_PER_PROROGA,
				movimento.copy(), dataProroga);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (MovimentoVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public boolean cancellaTipoServizio(TipoServizioVO tipoServizio) throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(
				utente.getTicket(),
				CommandType.SRV_CANCELLA_TIPO_SERVIZIO,
				tipoServizio.copy() );
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return true;
	}

	public boolean documentoCancellabile(DocumentoNonSbnVO documento) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_DOCUMENTO_NON_SBN_CANCELLABILE, documento.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}
		return false;

	}

}
