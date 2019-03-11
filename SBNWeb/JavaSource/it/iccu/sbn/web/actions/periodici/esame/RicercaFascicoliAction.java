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
package it.iccu.sbn.web.actions.periodici.esame;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.RicercaPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieTitoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.util.periodici.PrevisionaleUtil;
import it.iccu.sbn.web.actionforms.periodici.esame.RicercaFascicoliForm;
import it.iccu.sbn.web.actions.periodici.PeriodiciListaAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class RicercaFascicoliAction extends PeriodiciListaAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(RicercaFascicoliAction.class);

	private static String[] BOTTONIERA = new String[] {
		"button.periodici.cerca",
		"button.periodici.previsionale",
		"button.periodici.chiudi" };


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("button.periodici.cerca", "cerca");
		map.put("button.periodici.previsionale", "previsionale");
		map.put("button.periodici.chiudi", "annulla");

		//almaviva5_20111007 #4652
		map.put("button.periodici.esamina.bid", "esaminaBid");
		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);
		log.debug("init()");
		RicercaFascicoliForm currentForm = (RicercaFascicoliForm) form;
		if (currentForm.isInitialized() )
			return;

		ParametriPeriodici params = ParametriPeriodici.retrieve(request);
		currentForm.setParametri(params);

		DatiBibliograficiPeriodicoVO dbp = (DatiBibliograficiPeriodicoVO) params.get(ParamType.DATI_BIBLIOGRAFICI_PER_PERIODICI);
		currentForm.setDatiBibliografici(dbp);

		//la biblioteca é impostata a quella di login
		UserVO utente = Navigation.getInstance(request).getUtente();
		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(utente.getCodPolo());
		bib.setCod_bib(utente.getCodBib());
		bib.setNom_biblioteca(utente.getBiblioteca());
		currentForm.setBiblioteca(bib);
		currentForm.setRicercaKardex(new RicercaKardexPrevisionaleVO());

		currentForm.setPulsanti(BOTTONIERA);

		loadDefault(request, currentForm);
		currentForm.setInitialized(true);
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadDefault(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}



	@Override
	public ActionForward posiziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	public ActionForward previsionale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		RicercaFascicoliForm currentForm = (RicercaFascicoliForm) form;
		ParametriPeriodici params = currentForm.getParametri().copy();
		ParametriPeriodici.send(request, params);

		return Navigation.getInstance(request).goForward(mapping.findForward("previsionale"));
	}


	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {

		super.loadForm(request, form);

		RicercaFascicoliForm currentForm = (RicercaFascicoliForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		DatiBibliograficiPeriodicoVO dbp = currentForm.getDatiBibliografici();

		BibliotecaVO bib = currentForm.getBiblioteca();
		EsameSeriePeriodicoVO esame =
			delegate.getEsameSeriePeriodico(new RicercaPeriodicoVO<FascicoloVO>(bib.getCod_polo(), bib.getCod_bib(), dbp.getBid()) );

		if (esame == null)
			return;

		currentForm.setEsame(esame);
		ParametriPeriodici parametri = currentForm.getParametri();
		parametri.put(ParamType.ESAME_SERIE_PERIODICO, esame);

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, PeriodiciConstants.CAMBIO_BID))
			return true; //almaviva5_20111007 #4652

		try {
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
			if (ValidazioneDati.equals(idCheck, "button.periodici.previsionale"))
				return delegate.isAbilitatoDescrizioneFascicoli();

			return true;

		} catch (Exception e) {
			log.error("", e);
			return false;
		}

	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Navigation navi = Navigation.getInstance(request);

		RicercaFascicoliForm currentForm = (RicercaFascicoliForm) form;
		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		RicercaKardexPrevisionaleVO ricerca = (RicercaKardexPrevisionaleVO) currentForm.getRicercaKardex();
		ricerca.setBiblioteca(currentForm.getBiblioteca());
		SerieTitoloVO tit = new SerieTitoloVO();
		tit.setBid(currentForm.getEsame().getBid());
		ricerca.setOggettoRicerca(tit);
		ricerca.setComparator(ValidazioneDati.invertiComparatore(PrevisionaleUtil.ORDINAMENTO_PREVISIONALE));

		KardexPeriodicoVO kardex = delegate.getKardexPeriodico(currentForm.getBiblioteca(), ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		ParametriPeriodici params = currentForm.getParametri();//.copy();
		params.put(ParamType.PARAMETRI_RICERCA, ricerca);
		params.put(ParamType.KARDEX_PERIODICO, kardex);
		params.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.DESCRIZIONE_FASCICOLI);
		ParametriPeriodici.send(request, params);

		return navi.goForward(mapping.findForward("esameKardex"));
	}

	@Override
	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form)	throws Exception {
		RicercaFascicoliForm currentForm = (RicercaFascicoliForm) form;
		try {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			String def = (String) utenteEjb.getDefault(ConstantDefault.PER_KARDEX_ELEM_BLOCCHI);
			currentForm.getRicercaKardex().setNumeroElementiBlocco(Integer.valueOf(def));
		} catch (Exception e) {
			log.error("Errore caricamento default: ", e);
		}
	}

	@Override
	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

	public ActionForward esaminaBid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		RicercaFascicoliForm currentForm = (RicercaFascicoliForm) form;

		String bidSel = currentForm.getBidSelezionato();
		if (ValidazioneDati.strIsNull(bidSel)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		//ricarico esame con nuovo bid
		UserVO utente = Navigation.getInstance(request).getUtente();
		String bib = utente.getCodPolo() + currentForm.getBiblioteca().getCod_bib();
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(bib);
		areaDatiPass.setBidRicerca(bidSel);
		areaDatiPass.setRicercaIndice(false);
		areaDatiPass.setRicercaPolo(true);

		//richiesta analitica
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO analitica =
			factory.getGestioneBibliografica().creaRichiestaAnaliticoTitoliPerBID(areaDatiPass, utente.getTicket());
		TreeElementViewTitoli reticolo = analitica.getTreeElementViewTitoli();
		if (ValidazioneDati.isFilled(analitica.getCodErr())) {
			LinkableTagUtils.addError(request, new ActionMessage(analitica.getTestoProtocollo()));
			return mapping.getInputForward();
		}

		DatiBibliograficiPeriodicoVO dbp = new DatiBibliograficiPeriodicoVO(
				reticolo.getKey(),
				reticolo.getDescription(),
				reticolo);
		currentForm.setDatiBibliografici(dbp);

		loadForm(request, currentForm);

		return mapping.getInputForward();
	}


}

