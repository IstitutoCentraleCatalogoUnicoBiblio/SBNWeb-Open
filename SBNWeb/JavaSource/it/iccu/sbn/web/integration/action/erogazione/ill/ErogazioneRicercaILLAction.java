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

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.blocchi.DescrittoreBlocchiUtil;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.ill.DatiRichiestaILLDecorator;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneProfilazione;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.erogazione.ErogazioneAction;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ill.ErogazioneRicercaILLForm;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziILLDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ErogazioneRicercaILLAction extends ErogazioneAction {

	private static Logger log = Logger.getLogger(ErogazioneRicercaILLAction.class);

	private static String[] BOTTONIERA = new String[] {
		"servizi.bottone.aggiorna",
		"servizi.bottone.esamina"//, "servizi.bottone.annulla"
	};

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("servizi.bottone.esamina", "esamina");
		map.put("servizi.bottone.esaminaLoc", "esaminaMovLocale");

		map.put("button.blocco", "blocco");

		map.put("servizi.bottone.cambioBiblioteca", "biblioteca");

		//folder
		map.put("servizi.erogazione.ill.ricerca.folder.requester", "requester");
		map.put("servizi.erogazione.ill.ricerca.folder.responder", "responder");

		map.put("servizi.bottone.aggiorna", "aggiorna");

		map.put("servizi.bottone.hlputente", "sif_utente");
		map.put("servizi.bottone.biblioteca", "sif_biblioteca");

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

		return aggiorna(mapping, form, request, response);
	}

	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		if (currentForm.isInitialized())
			return;

		log.debug("DatiRichiestaILLAction::init()");
		currentForm.setPulsanti(BOTTONIERA);
		Navigation navi = Navigation.getInstance(request);
		currentForm.setBiblioteca(navi.getUtente().getCodBib());
		currentForm.setFolder(RuoloBiblioteca.FORNITRICE);

		ParametriServizi parametri = ParametriServizi.retrieve(request);
		if (parametri != null)
			currentForm.setParametri(parametri);

		currentForm.setListaStatoRichiestaILL(CodiciProvider.getCodici(CodiciType.CODICE_STATO_RICHIESTA_ILL, true));
		currentForm.setListaStatoRichiestaLocale(CodiciProvider.getCodici(CodiciType.CODICE_ATTIVITA_ITER, true));

		currentForm.setListaTipoOrdinamento(Arrays.asList(new TB_CODICI[] {
				new TB_CODICI("01", "Utente asc"),
				new TB_CODICI("02", "Utente desc"),
				new TB_CODICI("03", "Bib. richiedente asc"),
				new TB_CODICI("04", "Bib. richiedente desc"),
				new TB_CODICI("05", "Bib. fornitrice asc"),
				new TB_CODICI("06", "Bib. fornitrice desc"),
				new TB_CODICI("07", "Servizio asc"),
				new TB_CODICI("08", "Servizio desc"),
				new TB_CODICI("09", "Data agg. asc"),
				new TB_CODICI("10", "Data agg. desc")
		}));

		DatiRichiestaILLRicercaVO richiesta = currentForm.getRicerca();
		richiesta.setNumeroElementiBlocco(currentForm.getMaxRighe());
		richiesta.setTipoOrdinamento("10");

		navi.addBookmark(Bookmark.Servizi.EROGAZIONE_RICERCA_ILL);

		loadDefault(request, currentForm);

		currentForm.setInitialized(true);
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		DatiRichiestaILLRicercaVO richiesta = currentForm.getRicerca();
		UserVO u = Navigation.getInstance(request).getUtente();

		BibliotecaVO bib = DomainEJBFactory.getInstance().getBiblioteca().getBiblioteca(u.getCodPolo(), currentForm.getBiblioteca() );
		currentForm.getParametri().put(ParamType.BIBLIOTECA, bib);

		String ute = request.getParameter("UTERICERCA");
		if ( ute != null) {
			richiesta.setCodUtente((String) request.getAttribute("CodUte"));
		}

		//almaviva5_20151216 servizi ILL
		bib = (BibliotecaVO) request.getAttribute(NavigazioneProfilazione.BIBLIOTECA);
		if (bib != null) {
			if (currentForm.getFolder() == RuoloBiblioteca.RICHIEDENTE)
				currentForm.setBibliotecaFornitrice(bib);
			else
				currentForm.setBibliotecaRichiedente(bib);
		}

	}

	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form) throws Exception {
		super.loadDefault(request, form);
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		//folder
		String folder = (String) utenteEjb.getDefault(ConstantDefault.SER_RIC_ILL_FOLDER);
		currentForm.setFolder(RuoloBiblioteca.fromString(folder));
	}

	private List<DatiRichiestaILLVO> decorate(List<DatiRichiestaILLVO> lista) throws Exception {
		List<DatiRichiestaILLVO> tmp = new ArrayList<DatiRichiestaILLVO>(lista.size());
		for (DatiRichiestaILLVO dri : lista)
			tmp.add(new DatiRichiestaILLDecorator(dri));

		return tmp;
	}

	public ActionForward biblioteca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward requester(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		currentForm.setFolder(RuoloBiblioteca.RICHIEDENTE);

		ParametriServizi parametri = currentForm.getParametri();
		BibliotecaVO bib = (BibliotecaVO) parametri.get(ParamType.BIBLIOTECA);

		DatiRichiestaILLRicercaVO richiesta = currentForm.getRicerca().copy();
		//ricerca le richieste per le quali la bib corrente è richiedente
		richiesta.setRequesterId(bib.getIsil());
		richiesta.setRuolo(RuoloBiblioteca.RICHIEDENTE);

		//filtro bib fornitrice
		String isil = currentForm.getBibliotecaFornitrice().getIsil();
		if (ValidazioneDati.isFilled(isil))
			richiesta.setResponderId(isil);

		setNavigationText(request, RuoloBiblioteca.RICHIEDENTE);

		return eseguiRicerca(mapping, currentForm, request, response, richiesta);
	}

	public ActionForward responder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		currentForm.setFolder(RuoloBiblioteca.FORNITRICE);

		ParametriServizi parametri = currentForm.getParametri();
		BibliotecaVO bib = (BibliotecaVO) parametri.get(ParamType.BIBLIOTECA);

		DatiRichiestaILLRicercaVO richiesta = currentForm.getRicerca().copy();
		//ricerca le richieste per le quali la bib corrente è richiedente
		richiesta.setResponderId(bib.getIsil());
		richiesta.setRuolo(RuoloBiblioteca.FORNITRICE);

		//filtro bib richiedente
		String isil = currentForm.getBibliotecaRichiedente().getIsil();
		if (ValidazioneDati.isFilled(isil))
			richiesta.setRequesterId(isil);

		setNavigationText(request, RuoloBiblioteca.FORNITRICE);

		return eseguiRicerca(mapping, currentForm, request, response, richiesta);
	}

	private void setNavigationText(HttpServletRequest request, RuoloBiblioteca ruolo) {
		Navigation navi = Navigation.getInstance(request);
		String defaultText = LinkableTagUtils.findMessage(request, Locale.getDefault(), navi.getCurrentElementTextKey());
		navi.setTesto(String.format("%s (%s)", defaultText, ruolo));
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		DescrittoreBloccoVO blocco = currentForm.getBlocco();
		int bloccoSelezionato = currentForm.getBloccoSelezionato();
		if (bloccoSelezionato < 2 || blocco.getIdLista() == null
				|| currentForm.getBlocchiCaricati().contains(bloccoSelezionato))
			return mapping.getInputForward();

		DescrittoreBloccoVO nextBlocco = DescrittoreBlocchiUtil.browseBlocco(
				Navigation.getInstance(request).getUserTicket(), blocco.getIdLista(),
				bloccoSelezionato);

		if (nextBlocco == null)
			return mapping.getInputForward();

		currentForm.getBlocchiCaricati().add(nextBlocco.getNumBlocco());
		List<DatiRichiestaILLVO> richieste = currentForm.getRichieste();
		richieste.addAll(decorate(nextBlocco.getLista()));
		Collections.sort(richieste, BaseVO.ORDINAMENTO_PER_PROGRESSIVO);

		return mapping.getInputForward();
	}

	public ActionForward esaminaMovLocale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;

		List<Integer> items = getMultiBoxSelectedItems(currentForm.getSelected());
		if (!ValidazioneDati.isFilled(items))
			return mapping.getInputForward();

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		MovimentoVO mov = new MovimentoVO();
		mov.setIdRichiesta(items.get(0));
		MovimentoListaVO movimento = delegate.getDettaglioMovimento(mov, Locale.getDefault());

		request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimento);

		return navi.goForward(mapping.findForward("esaminaLoc") );
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		List<DatiRichiestaILLVO> richieste = currentForm.getRichieste();
		List<Integer> items = getMultiBoxSelectedItems(currentForm.getSelected());
		if (!ValidazioneDati.isFilled(items))
			return mapping.getInputForward();

		DatiRichiestaILLVO selected = UniqueIdentifiableVO.searchRepeatableId(items.get(0), richieste);
		if (selected == null) {
			return mapping.getInputForward();
		}

		selected = ServiziILLDelegate.getInstance(request).getDettaglioRichiestaILL(selected);
		long cod_rich_serv = selected.getCod_rich_serv();
		MovimentoVO mov = new MovimentoVO();
		mov.setIdRichiesta(cod_rich_serv);
		mov.setDatiILL(selected);

		ParametriServizi parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.DETTAGLIO_MOVIMENTO, mov);

		ParametriServizi.send(request, parametri);
		return Navigation.getInstance(request).goForward(mapping.findForwardConfig("esaminaILL"));
	}

	private ActionForward eseguiRicerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, DatiRichiestaILLRicercaVO richiesta)
			throws Exception {

		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;

		List<DatiRichiestaILLVO> richieste = currentForm.getRichieste();
		richieste.clear();

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);

		DescrittoreBloccoVO blocco1 = delegate.getListaRichiesteILL(richiesta);
		currentForm.setBlocco(blocco1);
		if (!DescrittoreBloccoVO.isFilled(blocco1))
			return mapping.getInputForward();

		currentForm.setBloccoSelezionato(1);
		// almaviva5_20190702 #6997 fix caricamento blocchi
		currentForm.getBlocchiCaricati().clear();
		currentForm.getBlocchiCaricati().add(1);
		Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
		richieste.addAll(decorate(blocco1.getLista()));
		if (richieste.size() == 1)
			currentForm.setSelected(new Integer[] { richieste.get(0).getRepeatableId() });

		return mapping.getInputForward();


	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		switch(currentForm.getFolder()) {
		case RICHIEDENTE:
			return requester(mapping, currentForm, request, response);
		case FORNITRICE:
			return responder(mapping, currentForm, request, response);
		}

		return mapping.getInputForward();
	}

	public ActionForward sif_utente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		//currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_UTENTE);
		request.setAttribute(BIBLIOTECA_ATTR, currentForm.getBiblioteca());
		request.setAttribute(PATH_CHIAMANTE_ATTR, "ErogazioneRicerca");

		return mapping.findForward("utente");
	}

	public ActionForward sif_biblioteca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;

		BibliotecaRicercaVO richiesta = new BibliotecaRicercaVO();
		if (currentForm.getFolder() == RuoloBiblioteca.RICHIEDENTE)
			richiesta.setRuoloILL(RuoloBiblioteca.FORNITRICE);
		else
			richiesta.setRuoloILL(RuoloBiblioteca.RICHIEDENTE);

		return BibliotecaDelegate.getInstance(request).getSIFListaBibliotecheILLFornitrici(richiesta);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.esamina")) {
			ErogazioneRicercaILLForm currentForm = (ErogazioneRicercaILLForm) form;
			return ValidazioneDati.isFilled(currentForm.getRichieste());
		}

		return super.checkAttivita(request, form, idCheck);
	}

}
