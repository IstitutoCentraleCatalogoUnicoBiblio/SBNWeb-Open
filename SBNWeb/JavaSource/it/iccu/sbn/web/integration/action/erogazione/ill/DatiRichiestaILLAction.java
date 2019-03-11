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
package it.iccu.sbn.web.integration.action.erogazione.ill;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.ill.DatiRichiestaILLDecorator;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.erogazione.ErogazioneAction;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ill.DatiRichiestaILLForm;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziILLDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

public class DatiRichiestaILLAction extends ErogazioneAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(DatiRichiestaILLAction.class);

	private static String[] BOTTONIERA = new String[] {
		"servizi.bottone.ok",
		"servizi.bottone.respingi",
		"servizi.bottone.ill.proponi.annullamento",
		"servizi.bottone.ill.allinea",
		"servizi.bottone.ill.arrivo.materiale",
		//"servizi.bottone.ill.invia.messaggio",
		"servizi.bottone.ill.condizione",
		"servizi.bottone.ill.crea.mov.locale",
		"servizi.bottone.esaminaLoc",
		"servizi.bottone.annulla"
	};

	private static final String[] BOTTONIERA_CONFERMA = new String[] {
		"servizi.bottone.si", "servizi.bottone.no",
		"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_INVENTARIO = new String[] {
		"servizi.bottone.ill.cambia.inventario",
		"servizi.bottone.ill.scollega.inventario",
		"servizi.bottone.ill.collega.inventario"
	};


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("servizi.bottone.annulla", "annulla");
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");

		map.put("servizi.bottone.esame.utente", "utente");
		map.put("servizi.bottone.ill.collega.inventario", "collegaInventario");
		map.put("servizi.bottone.ill.cambia.inventario", "collegaInventario");
		map.put("servizi.bottone.ill.scollega.inventario", "scollegaInventario");
		map.put("servizi.bottone.ill.crea.mov.locale", "creaRichiesta");

		map.put("servizi.bottone.respingi", "rifiuta");
		map.put("servizi.bottone.ill.proponi.annullamento", "rifiuta");
		map.put("servizi.bottone.esaminaLoc", "esaminaLoc");

		map.put("servizi.bottone.ill.allinea", "allinea");

		map.put("servizi.bottone.ill.invia.messaggio", "inviaMessaggio");
		map.put("servizi.bottone.ill.condizione", "condizione");

		map.put("servizi.bottone.ill.arrivo.materiale", "arrivoMateriale");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		if (currentForm.isInitialized())
			return;

		log.debug("DatiRichiestaILLAction::init()");
		currentForm.setModalita(ModalitaGestioneType.GESTIONE);
		currentForm.setPulsanti(BOTTONIERA);

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		currentForm.setListaTipoFonte(delegate
				.getComboCodici(CodiciType.CODICE_FONTE_DOCUMENTO_LETTORE));
		currentForm.setListaTipoDocumento(delegate
				.getComboCodici(CodiciType.CODICE_TIPO_DOCUMENTO_LETTORE));
		currentForm.setListaTipoCodFruizione(delegate
				.getComboCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE));
		currentForm.setListaTipoCodNoDisp(delegate
				.getComboCodici(CodiciType.CODICE_NON_DISPONIBILITA));
		currentForm.setListaPaesi(delegate
				.getComboCodici(CodiciType.CODICE_PAESE));
		currentForm.setListaLingue(delegate
				.getComboCodici(CodiciType.CODICE_LINGUA));
		currentForm.setListaNature(CodiciProvider.getCodici(CodiciType.CODICE_NATURA_ORDINE));

		//almaviva5_20141125 servizi ill
		currentForm.setListaTipoNumeroStd(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_NUMERO_STANDARD, true));
		currentForm.setListaProvincia(delegate.getComboCodici(CodiciType.CODICE_PROVINCE));

		Navigation.getInstance(request).addBookmark(Bookmark.Servizi.DATI_RICHIESTA_ILL);

		ParametriServizi parametri = ParametriServizi.retrieve(request);
		currentForm.setParametri(parametri);

		//bottoniera inventari
		currentForm.setPulsantiInventario(BOTTONIERA_INVENTARIO);

		List<TB_CODICI> listaTipoRecord = CodiciProvider.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC, true);
		currentForm.setListaTipoRecord(listaTipoRecord);

		currentForm.setInitialized(true);
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		ParametriServizi parametri = currentForm.getParametri();
		MovimentoVO mov = (MovimentoVO) parametri.get(ParamType.DETTAGLIO_MOVIMENTO);
		currentForm.setMovimento(mov);
		DatiRichiestaILLVO datiILL = mov.getDatiILL();

		//collega inventario
		InventarioTitoloVO inv = (InventarioTitoloVO)request.getAttribute("scelInv");
		if (inv != null) {
			datiILL.setInventario(inv);
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.ill.richiesta.inventario.collega.ok", inv.getChiaveInventario()));
		}

		//ritorno da mov. locale
		if (ValidazioneDati.equals(request.getAttribute(NavigazioneServizi.CHIUDI), NavigazioneServizi.CHIUDI_DETTAGLIO_MOVIMENTO))
			if (NavigazioneServizi.isOggettoModificato(request)) {
				ServiziILLDelegate delegate = ServiziILLDelegate.getInstance(request);
				datiILL = delegate.getDettaglioRichiestaILL(datiILL);
				mov.setDatiILL(datiILL);
			}

		DatiRichiestaILLDecorator decorator = new DatiRichiestaILLDecorator(datiILL);
		currentForm.setDati(decorator);

		ILLServiceType servizioILL = ILLServiceType.fromValue(datiILL.getServizio());

		List<TB_CODICI> tipiSupportoILL = ILLConfiguration2.getInstance().getListaSupportiILL(servizioILL);
		tipiSupportoILL = CaricamentoCombo.cutFirst(tipiSupportoILL);
		currentForm.setTipiSupportoILL(tipiSupportoILL);

		List<TB_CODICI> modoErogazioneILL = ILLConfiguration2.getInstance().getListaModErogazioneILL(servizioILL);
		modoErogazioneILL = CaricamentoCombo.cutFirst(modoErogazioneILL);
		currentForm.setModoErogazioneILL(modoErogazioneILL);
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;

		try {
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.SALVA);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		TipoOperazione op = currentForm.getOperazione();
		try {
			switch (op) {
			case SALVA:
				eseguiAggiornamento(mapping, form, request, response);
				no(mapping, currentForm, request, response);
				break;

			case RIFIUTA:
				return eseguiRifiuto(mapping, form, request, response);

			case ALLINEA_DATI_ILL:
				return eseguiAllinea(mapping, form, request, response);

			case CREA_PRENOTAZIONE:
				return eseguiCreaPrenotazione(mapping, currentForm, request, response);

			case CREA_MOVIMENTO_LOCALE: {
				if (eseguiAggiornamento(mapping, currentForm, request, response)) {
					LinkableTagUtils.resetErrors(request);
					return eseguiCreaMovimentoLocale(mapping, form, request, response);
				}
				break;
			}

			case SELEZIONE_SERVIZIO_LOCALE: {
				final ServizioBibliotecaVO servizioLoc = currentForm.getServizioLocale();
				currentForm.setServizioLocale(Stream.of(currentForm.getServiziLocali()).filter(new Predicate<ServizioBibliotecaVO>() {
					public boolean test(ServizioBibliotecaVO srv) {
						return srv.getCodTipoServ().equals(servizioLoc.getCodTipoServ());
					}
				}).single());
				return eseguiCreaMovimentoLocale(mapping, form, request, response);
			}

			case INVIA_MESSAGGIO:
				return eseguiInvioMessaggio(mapping, currentForm, request, response);

			case CONDIZIONE:
				return eseguiCondizione(mapping, currentForm, request, response);

			case ARRIVO_MATERIALE:
				return eseguiNotificaArrivoMateriale(mapping, currentForm, request, response);

			default:
				break;

			}
		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return no(mapping, currentForm, request, response);

		} finally {
			//no(mapping, currentForm, request, response);
		}

		return mapping.getInputForward();
	}

	private ActionForward eseguiAllinea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati();

		try {
			dati = ServiziILLDelegate.getInstance(request).allineaRichiestaILL(dati);
			currentForm.setDati(new DatiRichiestaILLDecorator(dati));

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ill.allinea.richiesta.ok"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return no(mapping, currentForm, request, response);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);
		}

		return no(mapping, currentForm, request, response);
	}

	private ActionForward eseguiRifiuto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati().copy();

		try {
			dati.addUltimoMessaggio(currentForm.getMessaggio());

			dati = ServiziILLDelegate.getInstance(request).rifiutaRichiestaILL(dati);
			currentForm.setDati(new DatiRichiestaILLDecorator(dati));

			if (StatoIterRichiesta.of(dati.getCurrentState()) == StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO)
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ill.propostaAnullamentoInviata"));
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceRifiutiEffettuati"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);

		}

		return no(mapping, currentForm, request, response);
	}

	private ActionForward eseguiInvioMessaggio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati().copy();

		try {
			dati.addUltimoMessaggio(currentForm.getMessaggio());

			dati = ServiziILLDelegate.getInstance(request).inviaMessaggioILL(dati);

			currentForm.setDati(new DatiRichiestaILLDecorator(dati));

			//LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceRifiutiEffettuati"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);

		}

		return no(mapping, currentForm, request, response);
	}

	private ActionForward eseguiCondizione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati().copy();

		try {
			dati.addUltimoMessaggio(currentForm.getMessaggio());

			dati = ServiziILLDelegate.getInstance(request).inviaCondizioneILL(dati);

			currentForm.setDati(new DatiRichiestaILLDecorator(dati));

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);

		}

		return no(mapping, currentForm, request, response);
	}

	private ActionForward eseguiNotificaArrivoMateriale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati().copy();

		try {
			dati = ServiziILLDelegate.getInstance(request).avanzaRichiestaILL(dati,
					StatoIterRichiesta.F114_ARRIVO_MATERIALE, currentForm.getMessaggio());

			currentForm.setDati(new DatiRichiestaILLDecorator(dati));

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);

		}

		return no(mapping, currentForm, request, response);
	}

	private boolean eseguiAggiornamento(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati();

		try {
			ServiziILLDelegate delegate = ServiziILLDelegate.getInstance(request);

			dati = delegate.aggiornaDatiRichiestaILL(dati);

			MovimentoVO mov = (MovimentoVO) currentForm.getParametri().get(ParamType.DETTAGLIO_MOVIMENTO);
			if (mov != null)
				mov.setDatiILL(dati);

			DatiRichiestaILLDecorator decorator = new DatiRichiestaILLDecorator(dati);
			currentForm.setDati(decorator);

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

			return true;

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);
		}

		return false;
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		currentForm.setConferma(false);
		currentForm.setMessaggio(null);
		currentForm.setOperazione(null);
		currentForm.setServizioLocale(new ServizioBibliotecaVO());

		ModalitaGestioneType modalita = currentForm.getModalita();
		switch (modalita) {
		case GESTIONE:
			currentForm.setPulsanti(BOTTONIERA);
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward collegaInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		ParametriServizi parametri = currentForm.getParametri();
		Navigation navi = Navigation.getInstance(request);
		BibliotecaVO bib = (BibliotecaVO) parametri.get(ParamType.BIBLIOTECA);

		request.setAttribute(BibliotecaDelegate.BIBLIOTECHE_FILTRO_SISTEMA_METRO, ValidazioneDati.asSingletonList(bib));
		request.setAttribute(NavigazioneServizi.DETTAGLIO_DOCUMENTO, currentForm.getDati().getDocumento().copy() );

		return navi.goForward(mapping.findForward("sif_titolo"));
	}

	public ActionForward scollegaInventario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati();
		dati.setInventario(null);

		return mapping.getInputForward();
	}

	public ActionForward creaRichiesta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;

		try {
			/*
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.CREA_MOVIMENTO_LOCALE);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
			*/
			//almaviva5_20170928 segnalazione ICCU: elimnata conferma salvataggio
			currentForm.setOperazione(TipoOperazione.CREA_MOVIMENTO_LOCALE);
			return si(mapping, currentForm, request, response);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();

	}

	private ActionForward eseguiCreaMovimentoLocale(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		ParametriServizi parametri = currentForm.getParametri();
		BibliotecaVO bib = (BibliotecaVO) parametri.get(ParamType.BIBLIOTECA);
		DatiRichiestaILLVO dati = currentForm.getDati().copy();

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		try {
			MovimentoVO mov = new MovimentoVO();
			mov.setDatiILL(dati);

			String codUtente = dati.isRichiedente() ? dati.getCodUtente() : dati.getCodUtenteBibRichiedente();
			UtenteBaseVO utenteVO = this.getUtente(codUtente, request);
			if (utenteVO == null)
				throw new ApplicationException(SbnErrorTypes.SRV_UTENTE_NON_TROVATO);

			InfoDocumentoVO infoVO = null;
			if (dati.isInventarioPresente() ) {
				// inventario
				InventarioTitoloVO inv = dati.getInventario();
				infoVO = this.getInfoInventario(inv.getCodPolo(), inv.getCodBib(),
						inv.getCodSerie(), inv.getCodInvent(), navi.getUserTicket(), Locale.getDefault(), request);
			} else {
				//doc non sbn
				DocumentoNonSbnVO doc = dati.getDocumento();
				infoVO = this.getInfoSegnatura(request, doc.getCodPolo(), doc.getCodBib(),
						String.valueOf(doc.getTipo_doc_lett()), (int) doc.getCod_doc_lett(), 1);
			}

			//diritti dell'utente in biblioteca
			List<ServizioBibliotecaVO> dirittiUtente = delegate.getServiziAttivi(bib.getCod_polo(), utenteVO.getCodBib(), utenteVO.getCodUtente(),
					bib.getCod_bib(), null);//DaoManager.now());

			if (currentForm.getServizioLocale().isNuovo()) {	//servizio locale non impostato

				//servizio locale legato al servizio ill richiesto
				List<ServizioBibliotecaVO> serviziLocali = ServiziUtil.getServiziLocaliLegatiTipoServizioILL(dirittiUtente, ILLServiceType.fromValue(dati.getServizio()));
				int serviziTrovati = ValidazioneDati.size(serviziLocali);
				if (serviziTrovati == 0)
					//nessun servizio trovato
					throw new ApplicationException(SbnErrorTypes.SRV_UTENTE_NON_AUTORIZZATO,
							utenteVO.getCodUtente(),
							CodiciProvider.cercaDescrizioneCodice(
									dati.getServizio(),
									CodiciType.CODICE_TIPO_SERVIZIO_ILL,
									CodiciRicercaType.RICERCA_CODICE_SBN));

				if (serviziTrovati > 1) {
					//Più servizi locali puntano allo stesso servizio iso-ill
					//TODO NON GESTITO
					//currentForm.setOperazione(TipoOperazione.SELEZIONE_SERVIZIO_LOCALE);
					//currentForm.setServiziLocali(serviziLocali);
					//return mapping.getInputForward();
					throw new ApplicationException(SbnErrorTypes.SRV_ILL_TROPPI_SERVIZI_LEGATI_A_SERVIZIO_ILL);
				}

				if (serviziTrovati == 1)
					//un solo servizio locale legato al servizio iso-ill
					currentForm.setServizioLocale(ValidazioneDati.first(serviziLocali));
			}

			ServizioBibliotecaVO servizioLoc = currentForm.getServizioLocale();

			//recupero primo passo iter per controlli
			List<ControlloAttivitaServizio> listaAttivita =
					super.getListaAttivitaSuccessive(bib.getCod_polo(), bib.getCod_bib(), servizioLoc.getCodTipoServ(), 0, null, request);

			if (!ValidazioneDati.isFilled(listaAttivita) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfigurazioneIter"));
				return mapping.getInputForward();
			}

			ControlloAttivitaServizio primoIter = this.primoPassoIter(listaAttivita);

			mov = preparaMovimento(request, mov, bib.getCod_polo(), bib.getCod_bib(), utenteVO, infoVO, servizioLoc, primoIter, OperatoreType.BIBLIOTECARIO);

			//impostazione costo max.
			Number importo = dati.getImporto();
			if (ValidazioneDati.isFilled(importo))
				mov.setCostoServizio(importo.doubleValue());

			Number costoMax = dati.getCostoMax();
			if (ValidazioneDati.isFilled(costoMax))
				mov.setPrezzoMax(costoMax.doubleValue());

			Date dataMassima = dati.getDataMassima();
			if (dataMassima != null)
				mov.setDataMax(new java.sql.Date(dataMassima.getTime()));

			//mov.setIntervalloCopia(dati.getIntervalloCopia());
			mov.setIntervalloCopia(dati.getDocumento().getPagine());

			//dati periodico
			if (dati.isInventarioPresente() ) {
				// inventario
				InventarioTitoloVO inv = dati.getInventario();
				mov.setAnnoPeriodico(inv.getAnnoAbb());
				mov.setNumVolume(inv.getNumVol());
			} else {
				//doc non sbn
				DocumentoNonSbnVO doc = dati.getDocumento();
				mov.setAnnoPeriodico(doc.getAnnata());
				mov.setNumVolume(doc.getNum_volume());
				mov.setNumFascicolo(doc.getFascicolo());
			}

			boolean ok = controllaNuovaRichiesta(mapping, currentForm, request, response, mov, primoIter);
			if (ok) {
				request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, creaDettaglioMovimento(request, utenteVO, infoVO, mov) );
				return navi.goForward(mapping.findForward("richiestaLocale") );
			}

			return mapping.getInputForward();

		} catch (SbnBaseException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);

			Navigation.getInstance(request).setExceptionLog(e);
			return mapping.getInputForward();

		} finally {
			//this.no(mapping, currentForm, request, response);
		}
	}

	private boolean controllaNuovaRichiesta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, MovimentoVO mov,
			ControlloAttivitaServizio primoIter)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;

		try {
			DatiControlloVO controlliBase = super.eseguiControlli(request,
					mov, ControlloAttivitaServizio.getControlliBase(),
					getOperatore(request), false, null);

			ControlloAttivitaServizioResult checkControlliBase = controlliBase.getResult();

			mov = controlliBase.getMovimento();

			// verifico i controlli base
			switch (checkControlliBase) {
			case OK:
			case OK_NON_ANCORA_DISPONIBILE:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
				// successivamente si differenzieranno tali casi
				break;

			case ERRORE_DOCUMENTO_NON_DISPONIBILE:
			case ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL:
				String codNoDisp = controlliBase.getCodNoDisp();
				String descr = CodiciProvider.cercaDescrizioneCodice(codNoDisp, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(), descr, controlliBase.getDataRedisp()));
				return false;

			case ERRORE_DOCUMENTO_NON_DISPONIBILE_NO_PRENOT:
			default:
				// in caso di errore sui controlli base
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
				throw new ValidationException("errore");
			}


			//controlli base superati, passo al controllo del primo passo iter configurato per il tipo servizio

			//almaviva5_20121017 #5146
			int numPezzi = ServiziUtil.getNumeroPaginePerRiproduzione(mov.getIntervalloCopia());
			if (numPezzi != ServiziConstant.NUM_PAGINE_ERROR)
				mov.setNumPezzi(String.valueOf(numPezzi));
			else
				LinkableTagUtils.addError(request, new ValidationException(SbnErrorTypes.SRV_ERRORE_INTERVALLO_COPIE) );

			DatiControlloVO controlliPrimoIter = super.eseguiControlli(request, mov, primoIter, getOperatore(request), false, checkControlliBase);
			if (controlliPrimoIter.getResult() != ControlloAttivitaServizioResult.OK)
				throw new ValidationException("Errore durante creazione nuova richiesta");

			Timestamp now = DaoManager.now();

			// dopo i controlli sul primo passo dell'iter
			// verifico il risultato dei precedenti controlli base
			// per efffettuare l'eventuale prenotazione
			switch (checkControlliBase) {
			case OK:
				break;

			case OK_NON_ANCORA_DISPONIBILE: {
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) controlliBase.getCheckData();
				Timestamp tsRitiro = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(), tsRitiro));
				Timestamp tsInizioPrev = mov.getDataInizioPrev();
				mov.setDataInizioPrev(tsRitiro.after(tsInizioPrev) ? tsRitiro : tsInizioPrev);
				break;
			}

			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:	{
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) controlliBase.getCheckData();
				Timestamp dataPrenotazione = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage(),
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO,
						disponibilitaVO.getMovimentoAttivo().getCodTipoServ()),
						dataPrenotazione));

				currentForm.setOperazione(TipoOperazione.CREA_PRENOTAZIONE);
				if (dataPrenotazione.before(now) )
					dataPrenotazione = now;

				mov.setDataInizioPrev(dataPrenotazione);
				currentForm.setMovimento(mov);

				currentForm.setConferma(true);
				currentForm.setPulsanti(BOTTONIERA_CONFERMA);
				saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

				//preparazione messaggio per prenotazione alla bib. richiedente
				mov.setFlTipoRec(RichiestaRecordType.FLAG_PRENOTAZIONE);
				MessaggioVO msg = new MessaggioVO(currentForm.getDati());
				msg.setDataMessaggio(DaoManager.now());
				msg.setTipoInvio(TipoInvio.INVIATO);
				msg.setStato(StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO.getISOCode());
				currentForm.setMessaggio(msg);

				return false;
			}

			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE: {
				ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) controlliBase.getCheckData();
				Timestamp dataPrenotazione = disponibilitaVO.getDataPrenotazione();
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
				currentForm.setOperazione(TipoOperazione.CREA_PRENOTAZIONE);
				if (dataPrenotazione.before(now) )
					dataPrenotazione = now;

				mov.setDataInizioPrev(dataPrenotazione);
				currentForm.setMovimento(mov);
				currentForm.setConferma(true);
				saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

				return false;
			}

			default:
				// se errore nei controlli base il successivo codice è stato già elaborato in precedenza
				LinkableTagUtils.addError(request, new ActionMessage(checkControlliBase.getMessage()) );
				throw new ValidationException("errore");
			}


		} catch (ValidationException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			throw e;
		}

		return true;

	}

	public ActionForward utente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO datiIll = currentForm.getMovimento().getDatiILL();

		UserVO utente = Navigation.getInstance(request).getUtente();
		String codUtente = datiIll.isRichiedente() ? datiIll.getCodUtente() : datiIll.getCodUtenteBibRichiedente();
		return ServiziDelegate.getInstance(request).sifDettaglioUtente(utente.getCodBib(), null, codUtente, 0);
	}

	public ActionForward rifiuta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		try {
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.RIFIUTA);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			DatiRichiestaILLVO dati = currentForm.getDati();
			//se la richiesta ha già generato un TransactionId va notificato il rifiuto all'altra biblioteca
			if (ValidazioneDati.isFilled(dati.getTransactionId())) {
				MessaggioVO msg = new MessaggioVO(dati);
				msg.setDataMessaggio(DaoManager.now());
				msg.setTipoInvio(TipoInvio.INVIATO);

				if (dati.isFornitrice()) {
					//se l'annullamento parte dalla richiedente il messaggio è diverso
					if (StatoIterRichiesta.of(dati.getCurrentState()) == StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO)
						msg.setStato(StatoIterRichiesta.F1221_CONFERMA_ANNULLAMENTO.getISOCode());
					else
						msg.setStato(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE.getISOCode() );
				} else
					msg.setStato(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO.getISOCode());

				currentForm.setMessaggio(msg);
			}

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();
	}

	public ActionForward inviaMessaggio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		try {
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.INVIA_MESSAGGIO);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			DatiRichiestaILLVO dati = currentForm.getDati();

			MessaggioVO msg = new MessaggioVO(dati);
			msg.setDataMessaggio(DaoManager.now());
			msg.setTipoInvio(TipoInvio.INVIATO);
			msg.setStato(dati.getCurrentState());

			currentForm.setMessaggio(msg);

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();
	}

	public ActionForward condizione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		try {
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.CONDIZIONE);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			MessaggioVO msg = new MessaggioVO(currentForm.getDati());
			msg.setDataMessaggio(DaoManager.now());
			msg.setTipoInvio(TipoInvio.INVIATO);
			msg.setStato(StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA.getISOCode());

			currentForm.setMessaggio(msg);

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();
	}

	public ActionForward arrivoMateriale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		try {
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.ARRIVO_MATERIALE);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			MessaggioVO msg = new MessaggioVO(currentForm.getDati());
			msg.setDataMessaggio(DaoManager.now());
			msg.setTipoInvio(TipoInvio.INVIATO);
			msg.setStato(StatoIterRichiesta.F114_ARRIVO_MATERIALE.getISOCode());

			currentForm.setMessaggio(msg);

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();
	}

	public ActionForward esaminaLoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		MovimentoVO mov = new MovimentoVO();
		mov.setIdRichiesta(currentForm.getDati().getCod_rich_serv());
		MovimentoListaVO movimento = delegate.getDettaglioMovimento(mov, Locale.getDefault());

		request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimento);

		return navi.goForward(mapping.findForward("richiestaLocale") );
	}

	private ActionForward eseguiCreaPrenotazione(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;

		try {
			MovimentoVO mov = currentForm.getMovimento().copy();
			DatiRichiestaILLVO dati = currentForm.getDati().copy();
			MessaggioVO msg = currentForm.getMessaggio().copy();
			dati.addUltimoMessaggio(msg);
			mov.setDatiILL(dati);

			mov = ServiziILLDelegate.getInstance(request).inserisciPrenotazioneILL(mov);

			currentForm.setDati(new DatiRichiestaILLDecorator(mov.getDatiILL()));

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.prenotazioneInserita"));

			//vai a dettaglio prenotazione
			//return esaminaLoc(mapping, currentForm, request, response);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error("", e);
		}

		return no(mapping, form, request, response);
	}

	public ActionForward allinea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		try {
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.ALLINEA_DATI_ILL);

			//LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			//return mapping.getInputForward();
			return eseguiAllinea(mapping, currentForm, request, response);

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		Navigation navi = Navigation.getInstance(request);
		DatiRichiestaILLForm currentForm = (DatiRichiestaILLForm) form;
		DatiRichiestaILLVO dati = currentForm.getDati();
		StatoIterRichiesta stato = StatoIterRichiesta.of(dati.getCurrentState());
		ILLConfiguration2 conf = ILLConfiguration2.getInstance();

		boolean last = navi.isLast();
		if (ValidazioneDati.equals(idCheck, "LAST_FORM") && !last)
			return false;

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.ill.crea.mov.locale"))
		try {
			if (!last || (dati.getCod_rich_serv() > 0) )	//mov. locale già presente
				return false;

			if (ValidazioneDati.in(stato,
					StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE,
					StatoIterRichiesta.F1218_TERMINE_SCADUTO,
					StatoIterRichiesta.F1221_CONFERMA_ANNULLAMENTO,
					StatoIterRichiesta.F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA))
				return false;

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			if (dati.isInventarioPresente()) {
				//se ho collegato un inventario e sono fornitrice posso creare un mov. locale
				//solo se disponibile o prenotabile
				if (dati.isRichiedente() )
					return true;

				if (!ILLConfiguration2.getInstance().isRichiestaPrenotabile(dati) ) {
					InventarioVO inv = dati.getInventario();
					MovimentoVO mov = new MovimentoVO();
					mov.setCodPolo(inv.getCodPolo());
					mov.setCodBibInv(inv.getCodBib());
					mov.setCodSerieInv(inv.getCodSerie());
					mov.setCodInvenInv("" + inv.getCodInvent());
					return !delegate.esisteMovimentoAttivo(mov);
				}

				return true;
			}

			//se non è presente l'inventario la segnatura deve avere una cat.frui valida
			String catFrui = delegate.getCategoriaFruizioneSegnatura(dati.getDocumento());
			return ValidazioneDati.isFilled(catFrui);

		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		if (ValidazioneDati.equals(idCheck, "SELEZIONE_SERVIZIO_LOCALE")) {
			return currentForm.getOperazione() == TipoOperazione.SELEZIONE_SERVIZIO_LOCALE;
		}

		if (ValidazioneDati.equals(idCheck, RuoloBiblioteca.FORNITRICE.name()) )
			return dati.isFornitrice();

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.respingi"))
			return last &&
				conf.isRichiestaAnnullabile(dati)
				&& (dati.isFornitrice() || dati.getTransactionId() == 0);

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.ill.proponi.annullamento"))
			return last &&
				dati.getTransactionId() > 0 &&
				dati.isRichiedente() &&
				conf.isRichiestaAnnullabile(dati) ;

		if (ValidazioneDati.equals(idCheck, "MESSAGGIO")) {
			TipoOperazione op = currentForm.getOperazione();
			return last
					&& dati.getTransactionId() > 0
					&& currentForm.isConferma()
					&& ValidazioneDati.in(op,
							TipoOperazione.RIFIUTA,
							TipoOperazione.CREA_PRENOTAZIONE,
							TipoOperazione.INVIA_MESSAGGIO,
							TipoOperazione.CONDIZIONE,
							TipoOperazione.ARRIVO_MATERIALE);
		}

		if (ValidazioneDati.equals(idCheck, "CONDIZIONE")) {
			return checkAttivita(request, currentForm, "servizi.bottone.ill.condizione")
					&& currentForm.getOperazione() == TipoOperazione.CONDIZIONE;
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.esaminaLoc"))
			return (dati.getCod_rich_serv() > 0) &&
					!navi.bookmarkExists(Bookmark.Servizi.DETTAGLIO_MOVIMENTO);

		if (ValidazioneDati.in(idCheck,
				"servizi.bottone.ill.cambia.inventario",
				"servizi.bottone.ill.scollega.inventario",
				"servizi.bottone.ill.collega.inventario")) {

			MovimentoVO mov = currentForm.getMovimento();
			boolean check = last && dati.isFornitrice() && mov.isNuovo() && conf.isRichiestaAnnullabile(dati);
			if (check && ValidazioneDati.equals(idCheck, "servizi.bottone.ill.collega.inventario") )
				return !dati.isInventarioPresente();

			//scollega/cambia
			return check && dati.isInventarioPresente();
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.ill.allinea"))
			return (dati.getTransactionId() > 0);

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.ill.invia.messaggio"))
			return (dati.getTransactionId() > 0) && (dati.getDataFine() == null)
					&& (StatoIterRichiesta.of(dati.getCurrentState()) != StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE);

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.ill.condizione")) {
			return ILLConfiguration2.getInstance().isRichiestaCondizionabile(dati);
		}

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.ill.arrivo.materiale")) {
			return dati.isRichiedente() && stato == StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE;
		}

		if (ValidazioneDati.in(idCheck, "POSSEDUTO")) {
			return false;
		}

		return true;
	}

}
