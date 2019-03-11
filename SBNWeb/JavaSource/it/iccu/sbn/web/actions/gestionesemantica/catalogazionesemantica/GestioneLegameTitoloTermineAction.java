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
package it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO.LegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO.LegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTitoloTermineType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.GestioneLegameTitoloTermineForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
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

public class GestioneLegameTitoloTermineAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory.getLog(GestioneLegameTitoloTermineAction.class);

	private static String[] BOTTONIERA_CREA = new String[] { "button.lega",
			"button.annulla" };
	private static String[] BOTTONIERA_MODIFICA = new String[] { "button.ok",
			"button.annulla" };
	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
			"button.no" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.ok", "ok");
		map.put("button.annulla", "annulla");
		map.put("button.lega", "lega");
		map.put("button.stampa", "stampa");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.gestione", "esamina");
		return map;
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors,
			Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form) {

		try {
			GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;
			currentForm.setListaThesauri(CaricamentoComboSemantica
					.loadComboThesauro(Navigation.getInstance(request).getUserTicket(), true));

			currentForm.setListaLivelloAutorita(CaricamentoComboSemantica
					.loadComboStato(null));

			return true;

		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, ex);
			return false;
		}
	}

	private void init(ActionMapping mapping, HttpServletRequest request, ActionForm form) {

		GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;
		log.info("GestioneLegameTitoloTermineAction::init");
		initCombo(request, form);

		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		currentForm.setParametri(parametri);
		currentForm.setInitialized(true);

		DettaglioTermineThesauroVO thes = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);
		currentForm.setDettaglio(thes);

		AreaDatiPassBiblioSemanticaVO datiBibliografici = (AreaDatiPassBiblioSemanticaVO) parametri
				.get(ParamType.DATI_BIBLIOGRAFICI);
		currentForm.setDatiBibliografici(datiBibliografici);
		currentForm.getCatalogazioneSemanticaComune().setBid(datiBibliografici.getBidPartenza());
		currentForm.getCatalogazioneSemanticaComune().setTestoBid(datiBibliografici.getDescPartenza());

		ModalitaLegameTitoloTermineType modalita = (ModalitaLegameTitoloTermineType) parametri
				.get(ParamType.MODALITA_LEGAME_TITOLO_TERMINE);
		currentForm.setModalita(modalita);

		//nota al legame
		TreeElementViewTitoli didTree = (TreeElementViewTitoli) datiBibliografici.getTreeElement().findElement(thes.getDid());
		if (didTree != null)
			currentForm.setNotaLegame(didTree.getAreaDatiDettaglioOggettiVO().getDettaglioTermineThesauroGeneraleVO().getNotaLegame());

		//almaviva5_20111024 evolutive CFI
		if (modalita == ModalitaLegameTitoloTermineType.CREA)
			loadClassiCollegate(request, form);

	}

	private void loadClassiCollegate(HttpServletRequest request, ActionForm form) {

		GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;
		DettaglioTermineThesauroVO thes = currentForm.getDettaglio();
		AreaDatiPassBiblioSemanticaVO datiBibliografici = currentForm.getDatiBibliografici();

		try {
			RicercaClassiTermineVO richiesta = new RicercaClassiTermineVO();
			richiesta.setCodThesauro(thes.getCodThesauro());
			richiesta.setDid(thes.getDid());
			richiesta.setEscludiClassiLegateTitolo(true);
			richiesta.setBid(datiBibliografici.getBidPartenza());
			richiesta.setElemBlocco(CommonConfiguration.getProperty(Configuration.MAX_RESULT_ROWS));

			ClassiCollegateTermineVO cct = ClassiDelegate.getInstance(request).ricercaClassiPerTermineCollegato(richiesta);
			if (cct != null)
				currentForm.setClassi(cct);

			if (!cct.isEmpty())
				currentForm.setSelected(ValidazioneDati.first(cct.getRisultati()).getRepeatableId());

		} catch (Exception e) {
			log.error("", e);
		}
	}

	private ActionForward eseguiOperazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, TipoOperazioneLegame operazione,
			LegameTitoloClasseVO legameTitoloClasse) throws Exception {

		GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;
		ParametriThesauro parametri = currentForm.getParametri().copy();

		//imposto dati per nuovo legame titolo-termine
		AreaDatiPassBiblioSemanticaVO datiGB = currentForm.getDatiBibliografici();
		TreeElementViewTitoli titolo = currentForm.getDatiBibliografici().getTreeElement();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();
		DatiLegameTitoloTermineVO datiLegame = new DatiLegameTitoloTermineVO();
		datiLegame.setOperazione(operazione);
		datiLegame.setBid(titolo.getKey());
		datiLegame.setBidNatura(titolo.getNatura());
		datiLegame.setBidTipoMateriale(titolo.getTipoMateriale());
		datiLegame.setBidLivelloAut(titolo.getLivelloAutorita());
		datiLegame.setT005(detTitoloPFissaVO.getVersione() );
		datiLegame.setLivelloPolo(true);

		DettaglioTermineThesauroVO termine = currentForm.getDettaglio();
		LegameTitoloTermineVO legame = datiLegame.new LegameTitoloTermineVO();
		String did = termine.getDid();
		legame.setDid(did);
		legame.setNotaLegame(currentForm.getNotaLegame());
		datiLegame.getLegami().add(legame);

		//almaviva5_20111025 evolutive CFI
		if (legameTitoloClasse != null)
			datiLegame.getLegami().add(legameTitoloClasse);

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		SBNMarcCommonVO risposta = delegate.gestioneLegameTitoloTermini(datiLegame);
		if (risposta == null)
			return mapping.getInputForward();

		//l'oggetto titolo è stato modificato
		titolo.setT005(risposta.getT005());
		detTitoloPFissaVO.setVersione(risposta.getT005());
		//aggiorno nota
		TreeElementViewTitoli didTree = (TreeElementViewTitoli) datiGB.getTreeElement().findElement(did);
		if (didTree != null)
			didTree.getAreaDatiDettaglioOggettiVO().getDettaglioTermineThesauroGeneraleVO().setNotaLegame(currentForm.getNotaLegame());
		parametri.put(ParamType.CATALOGAZIONE_TITOLO_T005, risposta.getT005());
		ParametriThesauro.send(request, parametri);
		return Navigation.getInstance(request).goBack(mapping.findForward("catalogazione"));

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;

		if (!currentForm.isInitialized())
			init(mapping, request, form);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		switch (currentForm.getModalita()) {
		case CREA:
			navi.setTesto(".gestionesemantica.catalogazionesemantica.GestioneLegameTitoloTermine.CREA.testo");
			navi.setDescrizioneX(".gestionesemantica.catalogazionesemantica.GestioneLegameTitoloTermine.CREA.descrizione");
			currentForm.setListaPulsanti(BOTTONIERA_CREA);
			break;

		case MODIFICA:
			currentForm.setListaPulsanti(BOTTONIERA_MODIFICA);
			return mapping.getInputForward();

		case CANCELLA:
			navi.setTesto(".gestionesemantica.catalogazionesemantica.GestioneLegameTitoloTermine.CANCELLA.testo");
			navi.setDescrizioneX(".gestionesemantica.catalogazionesemantica.GestioneLegameTitoloTermine.CANCELLA.descrizione");
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.thesauro.cancLegameTitolo") );
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public ActionForward lega(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//almaviva5_20111024 evolutive CFI
		LegameTitoloClasseVO legameTitoloClasse = null;
		GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;
		Integer selected = currentForm.getSelected();
		if (selected != null && !selected.equals(currentForm.getClassi().getRepeatableId()))
			legameTitoloClasse = preparaLegameTitoloClasse(form);

		return eseguiOperazione(mapping, form, request, response, TipoOperazioneLegame.CREA, legameTitoloClasse);
	}

	private LegameTitoloClasseVO preparaLegameTitoloClasse(ActionForm form) {
		GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;
		ClassiCollegateTermineVO classi = currentForm.getClassi();
		List<SinteticaClasseVO> risultati = classi.getRisultati();
		Integer selected = currentForm.getSelected();

		SinteticaClasseVO classe = SinteticaClasseVO.searchRepeatableId(selected, risultati);
		log.debug("classe da collegare: " + classe.getSimboloDewey().toString());

		DatiLegameTitoloClasseVO legame = new DatiLegameTitoloClasseVO();
		LegameTitoloClasseVO legameTitCla = legame.new LegameTitoloClasseVO();
		legameTitCla.setCodSistemaClassificazione(classe.getSistema());
		legameTitCla.setIdentificativoClasse(classe.getIdentificativoClasse());

		return legameTitCla;
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return eseguiOperazione(mapping, form, request, response, TipoOperazioneLegame.MODIFICA, null);
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return eseguiOperazione(mapping, form, request, response, TipoOperazioneLegame.CANCELLA, null);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
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

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GestioneLegameTitoloTermineForm currentForm = (GestioneLegameTitoloTermineForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, true);

		List<SinteticaClasseVO> risultati = currentForm.getClassi().getRisultati();
		SinteticaClasseVO classe = SinteticaClasseVO.searchRepeatableId(currentForm.getSelected(), risultati);
		if (classe == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		CreaVariaClasseVO analitica = ClassiDelegate.getInstance(request).analiticaClasse(true, classe.getIdentificativoClasse());
		if (analitica == null)
			return mapping.getInputForward();

		analitica.setRank(classe.getRank()); //non disponibile in analitica classe
		DettaglioClasseVO dettaglio = new DettaglioClasseVO(analitica);

		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, dettaglio);
		return Navigation.getInstance(request).goForward(mapping.findForward("esaminaClasse") );
	}

}
