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
package it.iccu.sbn.web.actions.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO.LegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.TipoOperazione;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.ListaClassiTermineForm;
import it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder.RankDirection;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ListaClassiTermineAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("button.gestione", "esamina");
		map.put("button.conferma", "conferma");
		map.put("button.chiudi", "annulla");

		map.put("servizi.bottone.frecciaSu", "moveUp");
		map.put("servizi.bottone.frecciaGiu", "moveDown");

		map.put("button.blocco", "blocco");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);
		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		if (currentForm.isInitialized())
			return;

		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		currentForm.setParametri(parametri);
		OggettoRiferimentoVO rif = (OggettoRiferimentoVO) parametri.get(ParamType.OGGETTO_RIFERIMENTO);
		currentForm.setOggettoRiferimento(rif);

		ClassiCollegateTermineVO classi = (ClassiCollegateTermineVO) parametri.get(ParamType.CLASSI_COLLEGATE);
		currentForm.setClassi(classi);

		TreeElementViewSoggetti analitica = (TreeElementViewSoggetti) parametri.get(ParamType.ANALITICA);
		currentForm.setAnalitica(analitica);

		DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);
		currentForm.setDettaglio(dettaglio);

		resetBlocchi(request, currentForm, classi);

		currentForm.setInitialized(true);
	}

	private void resetBlocchi(HttpServletRequest request, ActionForm form,	ClassiCollegateTermineVO classi) {
		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		currentForm.setIdFunzioneLegame("");
		currentForm.setComboGestioneLegame(LabelGestioneSemantica
				.getComboGestioneSematicaPerLegame(servlet
						.getServletContext(), request, form,
						new String[] { "TH-CL" }, this));

		List<SinteticaClasseVO> risultati = classi.getRisultati();
		int size = ValidazioneDati.size(risultati);
		if (size > 0) {
			Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
			currentForm.setBloccoSelezionato(1);
			currentForm.getBlocchiCaricati().clear();
			currentForm.getBlocchiCaricati().add(1);
			if (size == 1)
				currentForm.setSelected(ValidazioneDati.first(risultati).getRepeatableId());
		}
	}

	@Override
	protected void loadDefault(HttpServletRequest request, ActionForm form)
			throws Exception {
		super.loadDefault(request, form);

	}

	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		super.loadForm(request, form);
		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		if (parametri != null) {
			currentForm.setParametri(parametri);
			ClassiCollegateTermineVO classi = (ClassiCollegateTermineVO) parametri.get(ParamType.CLASSI_COLLEGATE);
			ClassiCollegateTermineVO oldClassi = currentForm.getClassi();
			if (classi.newer(oldClassi) ) {
				currentForm.setClassi(classi);
				resetBlocchi(request, currentForm, classi);

				DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);
				currentForm.setDettaglio(dettaglio);

				//va aggiornato il T005 dell'analitica collegata al thesauro
				TreeElementViewSoggetti node = (TreeElementViewSoggetti) currentForm.getAnalitica().findElement(dettaglio.getDid());
				node.setT005(dettaglio.getT005());
				node.getAreaDatiDettaglioOggettiVO().setDettaglioTermineThesauroVO(dettaglio);
			}
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadDefault(request, form);
		loadForm(request, form);

		//ritorno da linea classi per creazione legame
		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		if (parametri != null) {
			TipoOperazione op = (TipoOperazione) parametri.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
			if (ValidazioneDati.equals(op, TipoOperazione.CREA_LEGAME_TERMINE_CLASSE) )	{
				ParametriThesauro.send(request, parametri);
				return Navigation.getInstance(request).goForward(mapping.findForward("gestioneLegame") );
			}
		}

		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		String idFunzione = currentForm.getIdFunzioneLegame();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		if (!checkAttivita(request, form, idFunzione)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
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

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE, TipoOperazione.CREA_LEGAME_TERMINE_CLASSE);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("ricercaClasse") );
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
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

		ParametriThesauro parametri = currentForm.getParametri();
		parametri.put(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE, TipoOperazione.MODIFICA_LEGAME_TERMINE_CLASSE);
		DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
		parametri.put(ParamType.DETTAGLIO_LEGAME_CLASSE, datiLegame.new LegameTermineClasseVO(dettaglio) );

		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneLegame") );
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
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

		ParametriThesauro parametri = currentForm.getParametri();
		parametri.put(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE, TipoOperazione.ELIMINA_LEGAME_TERMINE_CLASSE);
		DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
		parametri.put(ParamType.DETTAGLIO_LEGAME_CLASSE, datiLegame.new LegameTermineClasseVO(dettaglio) );

		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneLegame") );
	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return modificaRank(mapping, form, request, response, RankDirection.UP);
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return modificaRank(mapping, form, request, response, RankDirection.DOWN);
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		int numBlocco = currentForm.getBloccoSelezionato();
		if (currentForm.getBlocchiCaricati().contains(numBlocco))
			mapping.getInputForward();

		ClassiCollegateTermineVO classi = currentForm.getClassi();
		ParametriThesauro parametri = currentForm.getParametri();
		RicercaClassiTermineVO richiesta = ((RicercaClassiTermineVO) parametri.get(ParamType.PARAMETRI_RICERCA_CLASSI)).copy();
		richiesta.setIdLista(classi.getIdLista());
		richiesta.setNumBlocco(numBlocco);

		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		ClassiCollegateTermineVO cct = delegate.ricercaClassiPerTermineCollegato(richiesta);
		if (cct == null)
			return mapping.getInputForward();

		currentForm.getBlocchiCaricati().add(numBlocco);
		List<SinteticaClasseVO> risultati = classi.getRisultati();
		risultati.addAll(cct.getRisultati());
		Collections.sort(risultati, SinteticaClasseVO.ORDINA_PER_PROGRESSIVO);

		return mapping.getInputForward();
	}

	private ActionForward modificaRank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			RankDirection direction) throws Exception {

		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		ClassiCollegateTermineVO classi = currentForm.getClassi();
		List<SinteticaClasseVO> risultati = classi.getRisultati();
		SinteticaClasseVO classe = SinteticaClasseVO.searchRepeatableId(currentForm.getSelected(), risultati);
		if (classe == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		short newRank = classe.getRank();
		int progr = classe.getProgr();
		switch (direction) {
		case UP:
			if (progr == 1) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}
			newRank--;
			break;

		case DOWN:
			if (progr == classi.getNumNotizie() ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}
			newRank++;
			break;
		}

		CreaVariaClasseVO analitica = ClassiDelegate.getInstance(request).analiticaClasse(true, classe.getIdentificativoClasse());
		if (analitica == null)
			return mapping.getInputForward();

		DettaglioClasseVO dettaglio = new DettaglioClasseVO(analitica);
		dettaglio.setRank(newRank);

		DettaglioTermineThesauroVO thes = currentForm.getDettaglio();

		DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
		datiLegame.setOperazione(TipoOperazioneLegame.MODIFICA);
		datiLegame.setCodThesauro(thes.getCodThesauro());
		datiLegame.setDid(thes.getDid());
		datiLegame.setLivAut(thes.getLivAut());
		datiLegame.setT005(thes.getT005());

		LegameTermineClasseVO legame = datiLegame.new LegameTermineClasseVO(dettaglio);
		datiLegame.getLegami().add(legame);

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		SBNMarcCommonVO risposta = delegate.gestioneLegameTermineClasse(datiLegame);

		if (risposta == null)
			return mapping.getInputForward();

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));

		//reload
		ParametriThesauro parametri = currentForm.getParametri().copy();
		AnaliticaThesauroVO reticolo = ThesauroDelegate.getInstance(request).ricaricaReticolo(true, thes.getDid());
		if (reticolo == null)
			return mapping.getInputForward();

		parametri.put(ParamType.DETTAGLIO_OGGETTO, reticolo.getReticolo().getDettaglio());
		RicercaClassiTermineVO richiesta = (RicercaClassiTermineVO) parametri.get(ParamType.PARAMETRI_RICERCA_CLASSI);

		ClassiCollegateTermineVO cct = ClassiDelegate.getInstance(request).ricercaClassiPerTermineCollegato(richiesta);
		if (cct == null)
			return mapping.getInputForward();

		parametri.put(ParamType.CLASSI_COLLEGATE, cct);
		ParametriThesauro.send(request, parametri);

		return unspecified(mapping, currentForm, request, response);

	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		ListaClassiTermineForm currentForm = (ListaClassiTermineForm) form;
		try {
			TipoOperazione op = TipoOperazione.valueOf(idCheck);
			DettaglioTermineThesauroVO dettaglio = currentForm.getDettaglio();
			List<SinteticaClasseVO> risultati = currentForm.getClassi().getRisultati();
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

			switch (op) {
			case CREA_LEGAME_TERMINE_CLASSE:
				return delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301)
					&& delegate.isThesauroGestito(dettaglio.getCodThesauro())
					&& delegate.isLivAutOk(dettaglio.getLivAut(), false);

			case MODIFICA_LEGAME_TERMINE_CLASSE:
			case ELIMINA_LEGAME_TERMINE_CLASSE:
				return ValidazioneDati.isFilled(risultati)
						&& delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301)
						&& delegate.isThesauroGestito(dettaglio.getCodThesauro())
						&& delegate.isLivAutOk(dettaglio.getLivAut(), false);

			case RANKING:
				return ValidazioneDati.size(risultati) > 1
					&& delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301)
					&& delegate.isThesauroGestito(dettaglio.getCodThesauro())
					&& delegate.isLivAutOk(dettaglio.getLivAut(), false);
			}

			return false;

		} catch (Exception e) {
			log.error("", e);
			return false;
		}

	}

}
