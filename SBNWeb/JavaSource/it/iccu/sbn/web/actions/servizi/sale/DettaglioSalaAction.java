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
package it.iccu.sbn.web.actions.servizi.sale;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.custom.servizi.calendario.ModelloCalendarioDecorator;
import it.iccu.sbn.vo.custom.servizi.sale.GruppoPostiDecorator;
import it.iccu.sbn.vo.custom.servizi.sale.SalaDecorator;
import it.iccu.sbn.vo.validators.servizi.SalaValidator;
import it.iccu.sbn.web.actionforms.servizi.sale.DettaglioSalaForm;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.Bookmark;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.CalendarioDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.SaleDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.navigation.Navigation;
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

public class DettaglioSalaAction extends ServiziBaseAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(DettaglioSalaAction.class);

	private static final String[] BOTTONIERA_CREA = new String[] {
			"servizi.bottone.ok",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_GESTIONE = new String[] {
			"servizi.bottone.ok",
			"servizi.bottone.cancella",
			//"servizi.bottone.supporti",
			"servizi.bottone.calendario",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_CONFERMA = new String[] {
			"servizi.bottone.si",
			"servizi.bottone.no",
			"servizi.bottone.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("servizi.bottone.cancella", "cancella");

		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");

		map.put("servizi.bottone.calendario", "calendario");

		map.put("servizi.bottone.aggiungi", "add");
		map.put("servizi.bottone.rimuovi", "delete");

		map.put("servizi.bottone.supporti", "supporti");

		map.put("servizi.bottone.cancella.calendario", "cancellaCalendario");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		if (currentForm.isInitialized())
			return;

		ParametriServizi params = ParametriServizi.retrieve(request);
		currentForm.setParametri(params);

		SalaVO sala = (SalaVO) params.get(ParamType.DETTAGLIO_SALA);
		currentForm.setSala(new SalaDecorator(sala));
		currentForm.setSalaOld((SalaVO) sala.copy());

		currentForm.setStrumentiMediazione(CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE));

		currentForm.setInitialized(true);
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		ParametriServizi params = ValidazioneDati.coalesce(ParametriServizi.retrieve(request), currentForm.getParametri());

		SalaVO sala = currentForm.getSala();
		ModelloCalendarioVO modello = (ModelloCalendarioVO) params.get(ParamType.MODELLO_CALENDARIO);
		if (modello != null) {
			sala.setCalendario(ModelloCalendarioDecorator.decorate(modello));
		}

		GruppoPostiVO gruppo = (GruppoPostiVO) params.get(ParamType.GRUPPO_POSTI_SALA);
		if (gruppo != null) {
			int idx = gruppo.getGruppo() - 1;
			sala.getGruppi().remove(idx);
			sala.getGruppi().add(idx, new GruppoPostiDecorator(gruppo));
		}

		impostaPulsanti(form);
	}

	private void impostaPulsanti(ActionForm form) {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		SalaVO sala = currentForm.getSala();
		if (sala.isNuovo())
			currentForm.setPulsanti(BOTTONIERA_CREA);
		else
			currentForm.setPulsanti(BOTTONIERA_GESTIONE);
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		return navi.goBack(true);
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;

		try {
			SalaVO sala = currentForm.getSala();

			sala.validate(new SalaValidator());

			if (currentForm.getSalaOld().equals(sala)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.noAggiornaNoVariazioni"));
				resetToken(request);
				return mapping.getInputForward();
			}

			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.SALVA);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));
			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
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

		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		TipoOperazione op = currentForm.getOperazione();
		switch (op) {
		case SALVA:
			return eseguiAggiornamento(mapping, form, request, response);
		case CANCELLA:
			return eseguiCancellazione(mapping, form, request, response);
		default:
			break;
		}

		return no(mapping, currentForm, request, response);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		currentForm.setConferma(false);
		impostaPulsanti(currentForm);

		return mapping.getInputForward();
	}

	private ActionForward eseguiAggiornamento(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		try {
			SalaVO sala = currentForm.getSala();
			sala = SaleDelegate.getInstance(request).aggiornaSala(sala);
			currentForm.setSala(new SalaDecorator(sala));
			currentForm.setSalaOld((SalaVO) sala.copy());
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();

		} finally {
			no(mapping, form, request, response);
		}

		return mapping.getInputForward();
	}

	private ActionForward eseguiCancellazione(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		try {
			SalaVO sala = currentForm.getSala().copy();

			sala = SaleDelegate.getInstance(request).cancellaSala(sala);
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

			return Navigation.getInstance(request).goToBookmark(Bookmark.Servizi.RICERCA_SALE, true);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();

		} finally {
			no(mapping, form, request, response);
		}

	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;

		try {
			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.CANCELLA);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneCan"));
			return mapping.getInputForward();

		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			setErroreGenerico(request, e);
			log.error(e);
		}

		return mapping.getInputForward();

	}

	public ActionForward calendario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;

		SalaVO sala = currentForm.getSala();
		CalendarioVO modello = sala.getCalendario();
		try {
			if (modello == null) {
				//nuovo calendario da modello biblioteca
				BibliotecaVO bib = (BibliotecaVO) currentForm.getParametri().get(ParamType.BIBLIOTECA);
				modello = CalendarioDelegate.getInstance(request).getCalendarioBiblioteca(bib.getCod_polo(), bib.getCod_bib());
				if (modello == null)
					throw new ApplicationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_BIB_NON_TROVATO);

				modello.setId(0); //nuovo calendario
				modello.setDescrizione(String.format("calendario sala '%s' (%s)", sala.getDescrizione(), sala.getCodSala() ));
			}

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		modello.setTipo(TipoCalendario.SALA);
		ParametriServizi params = currentForm.getParametri().copy();
		params.put(ParamType.MODELLO_CALENDARIO, modello.copy() );
		ParametriServizi.send(request, params);

		return Navigation.getInstance(request).goForward(mapping.findForward("calendario"));

	}

	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		String action_index = currentForm.getAction_index();
		log.debug("action_index: " + action_index);

		SalaVO sala = currentForm.getSala();
		List<GruppoPostiVO> gruppi = sala.getGruppi();
		GruppoPostiVO last = ValidazioneDati.last(gruppi);
		GruppoPostiVO gp = new GruppoPostiVO();
		if (last == null) {
			//primo gruppo
			gp.setGruppo(1);
			gp.setPosto_da(1);
			gp.setPosto_a(sala.getNumeroPosti());
		} else {
			gp.setGruppo(last.getGruppo() + 1);
			int posto = last.getPosto_a() + 1;
			gp.setPosto_da(posto);
			gp.setPosto_a(posto);
		}

		gruppi.add(new GruppoPostiDecorator(gp));

		//anchor
		request.setAttribute("anchor", LinkableTagUtils.ANCHOR_PREFIX + gp.getUniqueId() );

		return mapping.getInputForward();
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		String action_index = currentForm.getAction_index();
		log.debug("action_index: " + action_index);
		int idx = Integer.valueOf(action_index).intValue();
		List<GruppoPostiVO> gruppi = currentForm.getSala().getGruppi();

		if (ValidazioneDati.size(gruppi) == 1)
			return mapping.getInputForward();

		gruppi.remove(idx);

		for (int grp = 0; grp < gruppi.size(); grp++)
			gruppi.get(grp).setGruppo(grp + 1);

		//anchor
		//request.setAttribute("anchor", day);

		return mapping.getInputForward();
	}

	public ActionForward supporti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		String action_index = currentForm.getAction_index();
		log.debug("action_index: " + action_index);
		int idx = Integer.valueOf(action_index).intValue();

		SalaVO sala = currentForm.getSala();
		GruppoPostiVO gruppo = sala.getGruppi().get(idx).copy();

		ParametriServizi params = currentForm.getParametri().copy();
		params.put(ParamType.GRUPPO_POSTI_SALA, gruppo);
		ParametriServizi.send(request, params);

		return Navigation.getInstance(request).goForward(mapping.findForward("supporti"));

	}

	public ActionForward cancellaCalendario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		SalaVO sala = currentForm.getSala();
		ModelloCalendarioVO calendario = sala.getCalendario();
		if (calendario != null && !calendario.isNuovo())
			calendario.setFlCanc("S");
		else
			sala.setCalendario(null);

		return mapping.getInputForward();

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		DettaglioSalaForm currentForm = (DettaglioSalaForm) form;
		SalaVO sala = currentForm.getSala();

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.calendario"))
			return !sala.isNuovo();

		if (ValidazioneDati.equals(idCheck, "CANCELLA_GRUPPO_POSTI")) {
			return sala.getGruppi().size() > 1;
		}

		if (ValidazioneDati.equals(idCheck, "CALENDARIO_ATTIVO")) {
			CalendarioVO calendario = sala.getCalendario();
			return calendario != null && !calendario.isCancellato();
		}

		if (ValidazioneDati.equals(idCheck, "ALTRI_UTENTI")) {
			try {
				// richiesta DDS: l'utente che prenota un posto può essere accompagnato da uno o più utenti
				String altriUtenti = CommonConfiguration.getProperty(Configuration.SRV_PRENOTAZIONE_POSTO_UTENTI_MULTIPLI,
					Boolean.FALSE.toString());
				return Boolean.parseBoolean(altriUtenti);
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

}
