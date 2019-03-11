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

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTerminiType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.GestioneLegameTerminiForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class GestioneLegameTerminiAction extends LookupDispatchAction {

	private static String[] BOTTONIERA = new String[] { "button.ok",
			"button.annulla" };
	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
			"button.no" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.ok", "ok");
		map.put("button.annulla", "annulla");
		map.put("button.si", "si");
		map.put("button.no", "no");
		return map;
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors,
			Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			DatiLegameTerminiVO datiLegame) {

		try {
			GestioneLegameTerminiForm currentForm = (GestioneLegameTerminiForm) form;

			currentForm.setListaTipiLegame(CaricamentoComboSemantica
					.loadComboTipoLegameTerminiFiltrato(
							datiLegame.getDid1().getFormaNome(),
							datiLegame.getDid2().getFormaNome()));

			return true;

		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, ex);
			return false;
		}
	}

	private ActionForward init(ActionMapping mapping,
			HttpServletRequest request, ActionForm form) {

		GestioneLegameTerminiForm currentForm = (GestioneLegameTerminiForm) form;
		currentForm.setListaPulsanti(BOTTONIERA);
		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		currentForm.setParametri(parametri);
		DatiLegameTerminiVO datiLegame = (DatiLegameTerminiVO) parametri
				.get(ParamType.DATI_LEGAME_TERMINI);
		ModalitaLegameTerminiType modalita = (ModalitaLegameTerminiType) parametri
				.get(ParamType.MODALITA_LEGAME_TERMINI);
		currentForm.setDatiLegame(datiLegame);
		currentForm.setModalita(modalita);

		initCombo(request, form, datiLegame);
		loadDefault(request, form);

		Navigation navi = Navigation.getInstance(request);
		switch (modalita) {
		case CREA:
			navi.setTesto(".gestionesemantica.thesauro.GestioneLegameTermini.CREA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneLegameTermini.CREA.descrizione");
			break;

		case MODIFICA:
			currentForm.setTipoLegame(datiLegame.getTipoLegame());
			break;

		case CANCELLA:
			navi.setTesto(".gestionesemantica.thesauro.GestioneLegameTermini.CANCELLA.testo");
			navi.setDescrizioneX(".gestionesemantica.thesauro.GestioneLegameTermini.CANCELLA.descrizione");
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.thesauro.cancLegameTermini") );
			this.setErrors(request, errors, null);
			break;
		}

		currentForm.setInitialized(true);
		return mapping.getInputForward();
	}

	private void loadDefault(HttpServletRequest request, ActionForm form) {

		GestioneLegameTerminiForm currentForm = (GestioneLegameTerminiForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		ModalitaLegameTerminiType modalita = currentForm.getModalita();
		DatiLegameTerminiVO datiLegame = currentForm.getDatiLegame();

		try {
			switch (modalita) {
			case CREA:
				String tipoLegame = (String)utenteEjb.getDefault(ConstantDefault.CREA_THE_THE_TIPO_LEGAME);
				List<ComboCodDescVO> listaTipiLegame = currentForm.getListaTipiLegame();
				for (ComboCodDescVO tipo : listaTipiLegame)
					if (tipo.getCodice().equals(tipoLegame))
						datiLegame.setTipoLegame(tipoLegame);

			default:
				break;
			}

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.default"));
			this.saveErrors(request, errors);
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTerminiForm currentForm = (GestioneLegameTerminiForm) form;
		if (!currentForm.isInitialized())
			init(mapping, request, form);

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		return mapping.getInputForward();

	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		GestioneLegameTerminiForm currentForm = (GestioneLegameTerminiForm) form;
		ParametriThesauro parametri = currentForm.getParametri().copy();
		TreeElementViewSoggetti nodoPartenzaLegame =
			(TreeElementViewSoggetti) parametri.get(ParamType.NODO_PARTENZA_LEGAME);
		DatiLegameTerminiVO datiLegame = currentForm.getDatiLegame();
		datiLegame.setTipoLegame(currentForm.getTipoLegame());

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		CreaVariaTermineVO termine = null;

		switch (currentForm.getModalita()) {
		case CREA:
			datiLegame.setOperazione(TipoOperazioneLegame.CREA);
			break;

		case MODIFICA:
			datiLegame.setOperazione(TipoOperazioneLegame.MODIFICA);
			break;
		}

		termine = delegate.gestioneLegameTermini(datiLegame);
		if (termine == null)
			return mapping.getInputForward();

		// se l'analitica da cui provenivo aveva come root un did diverso da quello che ho
		// gestito, devo ricaricare il suo reticolo
		AnaliticaThesauroVO analitica = null;
		if (nodoPartenzaLegame == null)
			analitica = delegate.ricaricaReticolo(
					datiLegame.isLivelloPolo(), termine.getDid());
		else
			analitica = delegate.ricaricaReticolo(
					datiLegame.isLivelloPolo(), nodoPartenzaLegame.getKey() );

		if (analitica == null)
			return mapping.getInputForward();

		// sostituisco il sotto reticolo aggiornato al nodo partenza del legame in modo
		// da poter aggiornare l'analitica senza ricaricare tutti i sotto-reticoli
		nodoPartenzaLegame.adoptChildren(analitica.getReticolo() );
		nodoPartenzaLegame.open();
		parametri.put(ParamType.ID_NODO_SELEZIONATO, nodoPartenzaLegame.getRepeatableId() );
		// la radice dell'analitica è comunque quella originale
		parametri.put(ParamType.ANALITICA, nodoPartenzaLegame.getRoot() );

		ParametriThesauro.send(request, parametri);
		navi.purgeThis();
		return navi.goBack(mapping.findForwardConfig("analiticaThesauro"));

	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneLegameTerminiForm currentForm = (GestioneLegameTerminiForm) form;

		//imposto i dati del legame
		DatiLegameTerminiVO datiLegame = (DatiLegameTerminiVO) currentForm.getDatiLegame().clone();
		datiLegame.setOperazione(TipoOperazioneLegame.CANCELLA);

		Navigation navi = Navigation.getInstance(request);
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		CreaVariaTermineVO legameTermini = delegate.gestioneLegameTermini(datiLegame);
		if (legameTermini == null)
			return navi.goBack(true);

		ParametriThesauro parametri = currentForm.getParametri().copy();
		TreeElementViewSoggetti nodoPartenzaLegame =
			(TreeElementViewSoggetti) parametri.get(ParamType.NODO_PARTENZA_LEGAME);
		AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true, nodoPartenzaLegame.getKey() );
		if (analitica == null)
			return navi.goBack(true);

		// sostituisco il sotto reticolo aggiornato al nodo partenza del legame in modo
		// da poter aggiornare l'analitica senza ricaricare tutti i sotto-reticoli
		nodoPartenzaLegame.adoptChildren(analitica.getReticolo() );
		nodoPartenzaLegame.open();
		parametri.put(ParamType.ID_NODO_SELEZIONATO, nodoPartenzaLegame.getRepeatableId() );
		// la radice dell'analitica è comunque quella originale
		parametri.put(ParamType.ANALITICA, nodoPartenzaLegame.getRoot() );

		ParametriThesauro.send(request, parametri);
		return navi.goBack(mapping.findForwardConfig("analiticaThesauro"));
	}
}
