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

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO.LegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.TipoOperazione;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.GestioneLegameTermineClasseForm;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class GestioneLegameTermineClasseAction extends NavigationBaseAction {

	private static Logger log = Logger.getLogger(GestioneLegameTermineClasseAction.class);

	private static String[] BOTTONIERA_CREA = new String[] { "button.lega",
			"button.annulla" };
	private static String[] BOTTONIERA_MODIFICA = new String[] { "button.ok",
			"button.annulla" };
	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
			"button.no" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.lega", "lega");
		map.put("button.stampa", "stampa");
		map.put("button.ok", "ok");
		map.put("button.annulla", "chiudi");

		map.put("button.si", "si");
		map.put("button.no", "no");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		GestioneLegameTermineClasseForm currentForm = (GestioneLegameTermineClasseForm) form;
		if (currentForm.isInitialized())
			return;

		log.debug("GestioneLegameTermineClasseAction::init");
		super.init(request, form);
		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		currentForm.setParametri(parametri);
		OggettoRiferimentoVO rif = (OggettoRiferimentoVO) parametri.get(ParamType.OGGETTO_RIFERIMENTO);
		currentForm.setOggettoRiferimento(rif);
		LegameTermineClasseVO legame = (LegameTermineClasseVO) parametri.get(ParamType.DETTAGLIO_LEGAME_CLASSE);
		currentForm.setLegame(legame);
		Navigation navi = Navigation.getInstance(request);

		TipoOperazione op = (TipoOperazione) parametri.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
		switch (op) {
		case CREA_LEGAME_TERMINE_CLASSE:
			currentForm.setPulsanti(BOTTONIERA_CREA);
			navi.setTesto(".gestionesemantica.thesauro.NEW.gestioneLegameTermineClasse.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.NEW.gestioneLegameTermineClasse.descrizione");
			break;

		case MODIFICA_LEGAME_TERMINE_CLASSE:
			currentForm.setPulsanti(BOTTONIERA_MODIFICA);
			navi.setTesto(".gestionesemantica.thesauro.MOD.gestioneLegameTermineClasse.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.MOD.gestioneLegameTermineClasse.descrizione");
			break;

		case ELIMINA_LEGAME_TERMINE_CLASSE:
			navi.setTesto(".gestionesemantica.thesauro.DEL.gestioneLegameTermineClasse.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.DEL.gestioneLegameTermineClasse.descrizione");
			preparaConferma(request, navi.getCache().getCurrentElement().getMapping(), currentForm,
					new ActionMessage("errors.thesauro.cancLegameTermineClasse"), null);
			break;
		}

		//combo
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_CLASSE));
		currentForm.setListaSistemiClassificazione(CodiciProvider.getCodici(CodiciType.CODICE_SISTEMA_CLASSE));
		currentForm.setListaStatoControllo(CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA));

		currentForm.setInitialized(true);

	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTermineClasseForm currentForm = (GestioneLegameTermineClasseForm) form;
		// setto il token per le transazioni successive
		this.saveToken(request);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, currentForm);
		loadForm(request, currentForm);

		return mapping.getInputForward();
	}

	private SBNMarcCommonVO gestioneLegame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, TipoOperazioneLegame tipoOperazione) throws Exception {

		GestioneLegameTermineClasseForm currentForm = (GestioneLegameTermineClasseForm) form;
		ParametriThesauro parametri = currentForm.getParametri();
		DettaglioTermineThesauroVO thes = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);

		LegameTermineClasseVO legame = currentForm.getLegame();

		DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
		datiLegame.setOperazione(tipoOperazione);
		datiLegame.setCodThesauro(thes.getCodThesauro());
		datiLegame.setDid(thes.getDid());
		datiLegame.setLivAut(thes.getLivAut());
		datiLegame.setT005(thes.getT005());
		datiLegame.getLegami().add(legame);

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		SBNMarcCommonVO risposta = delegate.gestioneLegameTermineClasse(datiLegame);

		return risposta;

	}

	public ActionForward lega(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTermineClasseForm currentForm = (GestioneLegameTermineClasseForm) form;
		ParametriThesauro parametri = currentForm.getParametri();
		ClassiCollegateTermineVO classi = (ClassiCollegateTermineVO) parametri.get(ParamType.CLASSI_COLLEGATE);
		LegameTermineClasseVO legame = currentForm.getLegame();
		legame.setRank((short) (classi.getNumNotizie() + 1));
		SBNMarcCommonVO risposta = gestioneLegame(mapping, form, request, TipoOperazioneLegame.CREA);
		if (risposta == null)
			return mapping.getInputForward();

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));

		return reload(mapping, form, request, response);
	}

	protected ActionForward reload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTermineClasseForm currentForm = (GestioneLegameTermineClasseForm) form;
		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.remove(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
		DettaglioTermineThesauroVO thes = (DettaglioTermineThesauroVO) parametri.get(ParamType.DETTAGLIO_OGGETTO);

		AnaliticaThesauroVO reticolo = ThesauroDelegate.getInstance(request).ricaricaReticolo(true, thes.getDid());
		if (reticolo == null)
			return mapping.getInputForward();

		parametri.put(ParamType.DETTAGLIO_OGGETTO, reticolo.getReticolo().getDettaglio());

		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		RicercaClassiTermineVO richiesta = new RicercaClassiTermineVO();
		richiesta.setCodThesauro(thes.getCodThesauro());
		richiesta.setDid(thes.getDid());

		ClassiCollegateTermineVO cct = delegate.ricercaClassiPerTermineCollegato(richiesta);
		if (cct == null)
			return mapping.getInputForward();

		parametri.put(ParamType.CLASSI_COLLEGATE, cct);
		ParametriThesauro.send(request, parametri);

		return mapping.findForward("listaClassi");

	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SBNMarcCommonVO risposta = gestioneLegame(mapping, form, request, TipoOperazioneLegame.MODIFICA);
		if (risposta == null)
			return mapping.getInputForward();

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));

		return reload(mapping, form, request, response);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTermineClasseForm currentForm = (GestioneLegameTermineClasseForm) form;
		ParametriThesauro parametri = currentForm.getParametri();
		TipoOperazione op = (TipoOperazione) parametri.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
		if (op != TipoOperazione.ELIMINA_LEGAME_TERMINE_CLASSE)
			return mapping.getInputForward();

		SBNMarcCommonVO risposta = gestioneLegame(mapping, form, request, TipoOperazioneLegame.CANCELLA);
		if (risposta == null)
			return mapping.getInputForward();

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));

		return reload(mapping, form, request, response);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	protected ActionForward preparaConferma(HttpServletRequest request,
			ActionMapping mapping, ActionForm form, ActionMessage msg, String[] pulsanti)
			throws Exception {
		GestioneLegameTermineClasseForm currentForm = (GestioneLegameTermineClasseForm) form;
		currentForm.setConferma(true);
		currentForm.setPulsanti(pulsanti != null ? pulsanti : BOTTONIERA_CONFERMA);
		LinkableTagUtils.addError(request, msg != null ? msg : new ActionMessage("errors.servizi.confermaOperazioneAgg"));
		return mapping.getInputForward();
	}

}
