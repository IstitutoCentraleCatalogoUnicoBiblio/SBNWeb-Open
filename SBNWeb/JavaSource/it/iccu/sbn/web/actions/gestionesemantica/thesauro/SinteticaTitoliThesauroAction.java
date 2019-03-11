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
package it.iccu.sbn.web.actions.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.SinteticaTitoliThesauroForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaTitoliThesauroAction extends
		SinteticaLookupDispatchAction {

	private static Log log = LogFactory
			.getLog(SinteticaTitoliThesauroAction.class);

	private static String[] BOTTONIERA = new String[] { "button.selTutti",
			"button.deselTutti", "button.ok", "button.stampa", "button.annulla" };
	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
			"button.no", "button.annulla" };
	private static String[] BOTTONIERA_FUSIONE = new String[] { "button.fondi", "button.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.blocco", "blocco");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		map.put("button.ok", "ok");
		map.put("button.si", "si");
		map.put("button.no", "no");

		map.put("button.fondi", "si");
		return map;
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors,
			Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		SinteticaTitoliThesauroForm currentForm = (SinteticaTitoliThesauroForm) form;
		if (!currentForm.isInitialized()) {

			log.info("SinteticaTitoliThesauroAction::unspecified");
			ParametriThesauro parametri = ParametriThesauro.retrieve(request);
			currentForm.setParametri(parametri);
			DatiFusioneTerminiVO datiLegame = (DatiFusioneTerminiVO) parametri
					.get(ParamType.DATI_FUSIONE_TERMINI);
			currentForm.setDatiLegame(datiLegame);
			currentForm.setNumBlocco(1);
			currentForm.setInitialized(true);
			currentForm.setListaPulsanti(BOTTONIERA);

			String didTargetFusione = (String) parametri.get(ParamType.DID_TARGET_FUSIONE);
			if (didTargetFusione != null) {
				//non è un semplice trascina ma un fondi
				navi.setTesto(".gestionesemantica.thesauro.SinteticaTitoliThesauro.FONDI.testo");
				navi.setDescrizioneX(".gestionesemantica.thesauro.SinteticaTitoliThesauro.FONDI.descrizione");
				currentForm.setFusione(true);
				currentForm.setListaPulsanti(BOTTONIERA_FUSIONE);
				//seleziono tutto
				return tutti(mapping, form, request, response);
			}

		}

		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliThesauroForm currentForm = (SinteticaTitoliThesauroForm) form;
		ActionMessages errors = new ActionMessages();

		String[] titoliSelezionati = currentForm.getTitoliSelezionati();
		if (titoliSelezionati == null || titoliSelezionati.length == 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		currentForm.setEnableConferma(true);
		currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.thesauro.okTra"));
		this.setErrors(request, errors, null);
		return mapping.getInputForward();

	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		SinteticaTitoliThesauroForm currentForm = (SinteticaTitoliThesauroForm) form;
		AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliCollegati =
			currentForm.getDatiLegame().getTitoliCollegati();

		if (currentForm.getNumBlocco() > titoliCollegati.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.sogNotFound"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		HashSet<Integer> appoggio = currentForm.getAppoggio();
		int blocco = currentForm.getNumBlocco();
		if (appoggio != null)
			if (appoggio.contains(blocco))
				return mapping.getInputForward();

		AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(titoliCollegati.getMaxRighe());
		String idLista = titoliCollegati.getIdLista();
		areaDatiPass.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		areaDatiPass.setTipoOrdinam("1");
		areaDatiPass.setTipoOutput("001");
		areaDatiPass.setRicercaPolo(true);
		areaDatiPass.setRicercaIndice(false);
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().getNextBloccoTitoli(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		titoliCollegati.getListaSintetica().addAll(areaDatiPassReturn.getListaSintetica());

		Collections.sort(titoliCollegati.getListaSintetica(), SinteticaTitoliView.ORDINA_PER_PROGRESSIVO);
		if (currentForm.isFusione() )
			return tutti(mapping, form, request, response);

		return mapping.getInputForward();

	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliThesauroForm currentForm = (SinteticaTitoliThesauroForm) form;
		DatiFusioneTerminiVO datiFusione = (DatiFusioneTerminiVO) currentForm
				.getDatiLegame().customClone(false);
		if (currentForm.isFusione() )
			datiFusione.setSpostaID(null);
		else
			datiFusione.setSpostaID(currentForm.getTitoliSelezionati());

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		AreaDatiAccorpamentoReturnVO returnVO = delegate.fondiTerminiThesauro(
				true, datiFusione);
		if (returnVO == null)
			return mapping.getInputForward();

		String did = datiFusione.getDid2().getDid();
		AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true, did);
		if (analitica == null)
			return mapping.getInputForward();

		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.ANALITICA, analitica.getReticolo());
		ParametriThesauro.send(request, parametri);

		return mapping.findForward("analiticaThesauro");
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliThesauroForm currentForm = (SinteticaTitoliThesauroForm) form;
		currentForm.setEnableConferma(false);
		currentForm.setListaPulsanti(BOTTONIERA);
		return mapping.getInputForward();
	}

	public ActionForward deseleziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliThesauroForm currentForm = (SinteticaTitoliThesauroForm) form;
		currentForm.setTitoliSelezionati(null);
		return mapping.getInputForward();
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaTitoliThesauroForm currentForm = (SinteticaTitoliThesauroForm) form;
		List<SinteticaTitoliView> titoliCollegati = currentForm.getDatiLegame().getTitoliCollegati().getListaSintetica();
		List<String> tmp = new ArrayList<String>();
		for (SinteticaTitoliView titolo : titoliCollegati)
			tmp.add(titolo.getBid());

		currentForm.setTitoliSelezionati(tmp.toArray(new String[tmp.size()]));

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.setErrors(request, errors, null);
		// nessun codice selezionato
		return mapping.getInputForward();
	}

}
