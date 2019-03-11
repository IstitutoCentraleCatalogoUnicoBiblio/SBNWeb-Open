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
package it.iccu.sbn.web.actions.periodici;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ElementoSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoDecorator;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.RicercaPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieEsemplareCollVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ordini.RicercaOrdiniPeriodicoVO;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.web.actionforms.periodici.PeriodiciForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class PeriodiciAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(PeriodiciAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.periodici.chiudi", "chiudi");
		map.put("button.periodici.esamina", "esamina");
		map.put("button.periodici.esegui", "esegui");
		map.put("button.periodici.esamina.bid", "esaminaBid");
		map.put("button.blocco", "blocco");
		map.put("button.periodici.biblioteca", "cambioBib");

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

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		PeriodiciForm currentForm = (PeriodiciForm) form;
		DatiBibliograficiPeriodicoVO dbp = currentForm.getDatiBibliografici();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		BibliotecaVO bib = currentForm.getBiblioteca();
		EsameSeriePeriodicoVO esame =
			delegate.getEsameSeriePeriodico(new RicercaPeriodicoVO<FascicoloVO>(bib.getCod_polo(), bib.getCod_bib(), dbp.getBid()) );
		if (esame == null)
			return;

		currentForm.setEsame(esame);
		ParametriPeriodici parametri = currentForm.getParametri();
		parametri.put(ParamType.ESAME_SERIE_PERIODICO, esame);
		resetBlocchi(request, currentForm);

		List<ElementoSeriePeriodicoVO> serie = esame.getSerie();
		if (ValidazioneDati.size(serie) == 1)
			currentForm.setSelectedItem(serie.get(0).getUniqueId());

	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward collocazioni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PeriodiciForm currentForm = (PeriodiciForm) form;

		SerieBaseVO selected = currentForm.getEsame().search(currentForm.getSelectedItem() );
		log.debug("codSelezionato: " + selected );

		if (selected == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ElementoSeriePeriodicoVO esp = (ElementoSeriePeriodicoVO) selected;
		switch (esp.getTipo()) {
		case ORDINE:
			LinkableTagUtils.addError(request, new ActionMessage("errors.periodici.associa.selezione.errata"));
			return mapping.getInputForward();

		case ESEMPLARE:
			SerieEsemplareCollVO e = esp.getEsemplare();
			EsameCollocRicercaVO paramRicerca = new EsameCollocRicercaVO();
			paramRicerca.setCodPolo(e.getCodPolo());
			paramRicerca.setCodBib(e.getCodBib());
			paramRicerca.setCodPoloDoc(e.getCodPolo());
			paramRicerca.setCodBibDoc(e.getCodBib());
			paramRicerca.setBidDoc(e.getBid());
			paramRicerca.setCodDoc(e.getCd_doc());
			paramRicerca.setCodBibSez(e.getCodBib());

			request.setAttribute("paramRicerca", paramRicerca);
			request.setAttribute("prov", "posseduto");
			request.setAttribute("listaCollEsemplare", PeriodiciDelegate.LISTA_COLL_ESEMPL_PERIODICI);
	        request.setAttribute("codBib", currentForm.getBiblioteca().getCod_bib());
	        request.setAttribute("descrBib", currentForm.getBiblioteca().getNom_biblioteca());

			return Navigation.getInstance(request).goForward(mapping.findForward("collocazioni"));

		case COLLOCAZIONE:
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
			return delegate.sifListaInventariDiCollocazione(currentForm.getBiblioteca(), esp.getCollocazione());
		}

		return mapping.getInputForward();

	}

	public ActionForward ordini(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		PeriodiciForm currentForm = (PeriodiciForm) form;

		SerieBaseVO selected = currentForm.getEsame().search(currentForm.getSelectedItem());
		log.debug("codSelezionato: " + selected );

		if (selected == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ElementoSeriePeriodicoVO esp = (ElementoSeriePeriodicoVO) selected;
		SerieBaseVO oggettoRicerca = esp.getActualType();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		RicercaOrdiniPeriodicoVO ricerca = new RicercaOrdiniPeriodicoVO(currentForm.getBiblioteca(), oggettoRicerca);
		ricerca.setComparator(ValidazioneDati.invertiComparatore(SerieOrdineVO.NATURAL_ORDER));
		DescrittoreBloccoVO blocco1 = delegate.getListaOrdiniPeriodico(ricerca);
		if (!DescrittoreBloccoVO.isFilled(blocco1))
			return mapping.getInputForward();

		ParametriPeriodici parametri = currentForm.getParametri();
		parametri.put(ParamType.PARAMETRI_RICERCA_ORDINI, ricerca);
		parametri.put(ParamType.LISTA_ORDINI_PERIODICO, blocco1);
		ParametriPeriodici.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("ordini"));
	}

	public ActionForward cambioBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

	public ActionForward esaminaBid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PeriodiciForm currentForm = (PeriodiciForm) form;

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

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PeriodiciForm currentForm = (PeriodiciForm) form;

		Navigation navi = Navigation.getInstance(request);
		SerieBaseVO selected = currentForm.getEsame().search(currentForm.getSelectedItem());
		log.debug("codSelezionato: " + selected );

		if (selected != null) {
			//cambio id selezionato se provengo da buttonlink tag
			if (selected.getParent() != null)
				currentForm.setSelectedItem(selected.getParent().getUniqueId() );

			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
			RicercaKardexPeriodicoVO<FascicoloVO> ricerca = currentForm.getParamRicercaKardex().copy();
			ricerca.setOggettoRicerca(selected);
			ricerca.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));
			KardexPeriodicoVO kardex = delegate.getKardexPeriodico(currentForm.getBiblioteca(), ricerca);
			if (kardex == null)
				return mapping.getInputForward();

			ParametriPeriodici parametri = currentForm.getParametri();
			parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.KARDEX);
			parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
			parametri.put(ParamType.KARDEX_PERIODICO, kardex);
			ParametriPeriodici.send(request, parametri);

			return navi.goForward(mapping.findForward("kardex"));
		}


		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
		// nessun codice selezionato
		return mapping.getInputForward();
	}

	public ActionForward esegui(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PeriodiciForm currentForm = (PeriodiciForm) form;

		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
			if (ValidazioneDati.equals(idCheck, PeriodiciConstants.DESCRIZIONE_FASCICOLO))
				return delegate.isAbilitatoDescrizioneFascicoli();

		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return true;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);
		PeriodiciForm currentForm = (PeriodiciForm) form;
		if (currentForm.isInitialized() )
			return;

		currentForm.setComboGestioneEsamina(LabelGestioneSemantica
				.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
						request, form, new String[]{"PR"}, this));
		currentForm.setIdFunzioneEsamina("");

		ParametriPeriodici parametri = ParametriPeriodici.retrieve(request);
		currentForm.setParametri(parametri);

		DatiBibliograficiPeriodicoVO dbp = (DatiBibliograficiPeriodicoVO) parametri.get(ParamType.DATI_BIBLIOGRAFICI_PER_PERIODICI);
		currentForm.setDatiBibliografici(dbp);

		//la biblioteca é impostata a quella di login
		UserVO utente = Navigation.getInstance(request).getUtente();
		BibliotecaVO bib = new BibliotecaVO();
		bib.setCod_polo(utente.getCodPolo());
		bib.setCod_bib(utente.getCodBib());
		bib.setNom_biblioteca(utente.getBiblioteca());
		currentForm.setBiblioteca(bib);

		loadDefault(request, form);

		currentForm.setInitialized(true);
	}

	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form)	throws Exception {
		PeriodiciForm currentForm = (PeriodiciForm) form;
		if (currentForm.isInitialized() )
			return;

		try {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			String def = (String) utenteEjb.getDefault(ConstantDefault.PER_KARDEX_ELEM_BLOCCHI);
			currentForm.getParamRicercaKardex().setNumeroElementiBlocco(Integer.valueOf(def));
		} catch (Exception e) {
			log.error("Errore caricamento default: ", e);
		}
	}

	protected void resetBlocchi(HttpServletRequest request, PeriodiciForm currentForm) {
		EsameSeriePeriodicoDecorator esame = (EsameSeriePeriodicoDecorator) currentForm.getEsame();
		List<ElementoSeriePeriodicoVO> serie = esame.getSerie();
		if (!esame.isEmpty()) {
			Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
			serie.clear();
			DescrittoreBloccoVO blocco1 = esame.getBlocco();
			serie.addAll(blocco1.getLista());
			currentForm.setBloccoSelezionato(1);
			currentForm.getBlocchiCaricati().clear();
			currentForm.getBlocchiCaricati().add(1);
		} else
			esame.setSerie(ValidazioneDati.emptyList(ElementoSeriePeriodicoVO.class));
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PeriodiciForm currentForm = (PeriodiciForm) form;
		EsameSeriePeriodicoDecorator esame = (EsameSeriePeriodicoDecorator) currentForm.getEsame();
		DescrittoreBloccoVO blocco = esame.getBlocco();
		int bloccoSelezionato = currentForm.getBloccoSelezionato();
		if (bloccoSelezionato < 2 || blocco.getIdLista() == null
				|| currentForm.getBlocchiCaricati().contains(bloccoSelezionato))
			return mapping.getInputForward();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		DescrittoreBloccoVO nextBlocco = delegate.nextBlocco(
				blocco.getIdLista(), bloccoSelezionato);

		if (nextBlocco == null)
			return mapping.getInputForward();

		currentForm.getBlocchiCaricati().add(nextBlocco.getNumBlocco());
		List<ElementoSeriePeriodicoVO> serie = esame.getSerie();
		serie.addAll(nextBlocco.getLista());
		Collections.sort(serie, ElementoSeriePeriodicoVO.ORDINAMENTO_SERIE_PERIODICO);

		return mapping.getInputForward();

	}

}
