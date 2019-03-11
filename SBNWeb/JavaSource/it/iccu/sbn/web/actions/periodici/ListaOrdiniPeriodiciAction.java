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
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.TipoOperazione;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ordini.RicercaOrdiniPeriodicoVO;
import it.iccu.sbn.web.actionforms.periodici.ListaOrdiniPeriodiciForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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

public class ListaOrdiniPeriodiciAction extends SinteticaLookupDispatchAction
		implements SbnAttivitaChecker {

	private static final String[] BOTTONIERA = new String[] {
		"button.periodici.scegli",
		"button.periodici.esamina.ordine",
		"button.periodici.annulla"
	};


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.periodici.annulla", "annulla");
		map.put("button.periodici.scegli", "scegli");
		map.put("button.periodici.esamina.ordine", "esaminaOrdine");
		map.put("button.blocco", "blocco");
		return map;
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, PeriodiciConstants.CAMBIO_BID))
			return false;

		return true;
	}

	private static Logger log = Logger.getLogger(PeriodiciAction.class);


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(form, request);

		return mapping.getInputForward();
	}


	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form)	throws Exception {
		ListaOrdiniPeriodiciForm currentForm = (ListaOrdiniPeriodiciForm) form;
		if (currentForm.isInitialized() )
			return;

		try {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			String def = (String) utenteEjb.getDefault(ConstantDefault.PER_KARDEX_ELEM_BLOCCHI);
			currentForm.setNumeroElementiBlocco(Integer.valueOf(def));
		} catch (Exception e) {
			log.error("Errore caricamento default: ", e);
		}
	}


	private void loadForm(ActionForm form, HttpServletRequest request) throws Exception {
		return;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {

		ListaOrdiniPeriodiciForm currentForm = (ListaOrdiniPeriodiciForm) form;
		if (currentForm.isInitialized())
			return;

		ParametriPeriodici parametri = ParametriPeriodici.retrieve(request);
		currentForm.setParametri(parametri);

		RicercaOrdiniPeriodicoVO ricerca = (RicercaOrdiniPeriodicoVO) parametri.get(ParamType.PARAMETRI_RICERCA_ORDINI);
		currentForm.setRicerca(ricerca);
		BibliotecaVO bib = ricerca.getBiblioteca();
		currentForm.setBiblioteca(bib);

		EsameSeriePeriodicoVO esame = (EsameSeriePeriodicoVO) parametri.get(ParamType.ESAME_SERIE_PERIODICO);
		currentForm.setEsame(esame);

		DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) parametri.get(ParamType.LISTA_ORDINI_PERIODICO);
		List<SerieOrdineVO> listaOrdini = currentForm.getListaOrdini();
		listaOrdini.addAll(blocco1.getLista());
		if (ValidazioneDati.size(listaOrdini) == 1)
			currentForm.setSelected(listaOrdini.get(0).getUniqueId());

		ConfigurazioneORDVO conf = PeriodiciDelegate.getInstance(request).loadConfigurazioneOrdini(bib.getCod_polo(), bib.getCod_bib());
		currentForm.setBilancioAttivo(conf != null && conf.isGestioneBilancio());

		currentForm.setBlocco(blocco1);
		currentForm.setBloccoSelezionato(1);
		currentForm.getBlocchiCaricati().add(1);

		currentForm.setPulsanti(BOTTONIERA);

		loadDefault(request, form);

		currentForm.setInitialized(true);

	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward esaminaOrdine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ListaOrdiniPeriodiciForm currentForm = (ListaOrdiniPeriodiciForm) form;
		SerieOrdineVO selected = UniqueIdentifiableVO.search(currentForm.getSelected(), currentForm.getListaOrdini() );
		if (selected == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));
			return mapping.getInputForward();
		}
		log.debug("codSelezionato: " + selected );

		return PeriodiciDelegate.getInstance(request).sifEsaminaOrdine(selected);
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ListaOrdiniPeriodiciForm currentForm = (ListaOrdiniPeriodiciForm) form;
		SerieOrdineVO selected = UniqueIdentifiableVO.search(currentForm.getSelected(), currentForm.getListaOrdini() );
		if (selected == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.listamovimenti.nessunaSelezionePerEsamina"));
			return mapping.getInputForward();
		}
		log.debug("codSelezionato: " + selected );

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);
		RicercaKardexPeriodicoVO<FascicoloVO> ricerca = new RicercaKardexPeriodicoVO<FascicoloVO>();
		ricerca.setOggettoRicerca(selected);
		ricerca.setNumeroElementiBlocco(currentForm.getNumeroElementiBlocco());
		KardexPeriodicoVO kardex = delegate.getKardexPeriodico(currentForm.getBiblioteca(), ricerca);
		if (kardex == null)
			return mapping.getInputForward();

		ParametriPeriodici parametri = currentForm.getParametri();
		parametri.put(ParamType.TIPO_OPERAZIONE, TipoOperazione.KARDEX);
		parametri.put(ParamType.PARAMETRI_RICERCA, ricerca);
		parametri.put(ParamType.KARDEX_PERIODICO, kardex);
		ParametriPeriodici.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("kardex"));

	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ListaOrdiniPeriodiciForm currentForm = (ListaOrdiniPeriodiciForm) form;
		DescrittoreBloccoVO blocco = currentForm.getBlocco();
		int bloccoSelezionato = currentForm.getBloccoSelezionato();
		if (bloccoSelezionato < 2
			|| blocco.getIdLista() == null
			|| currentForm.getBlocchiCaricati().contains(bloccoSelezionato))
			return mapping.getInputForward();

		PeriodiciDelegate delegate = PeriodiciDelegate.getInstance(request);

		DescrittoreBloccoVO nextBlocco = delegate.nextBlocco(blocco.getIdLista(), bloccoSelezionato);

		if (nextBlocco == null)
			return mapping.getInputForward();

		currentForm.getBlocchiCaricati().add(nextBlocco.getNumBlocco());
		List<SerieOrdineVO> ordini = currentForm.getListaOrdini();
		ordini.addAll(nextBlocco.getLista() );
		BaseVO.sortAndEnumerate(ordini, ValidazioneDati.invertiComparatore(SerieOrdineVO.NATURAL_ORDER));

		return mapping.getInputForward();
	}

}
