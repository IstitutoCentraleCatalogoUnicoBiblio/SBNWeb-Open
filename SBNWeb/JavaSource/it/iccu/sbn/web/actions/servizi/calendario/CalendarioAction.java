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
package it.iccu.sbn.web.actions.servizi.calendario;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.servizi.CalendarioUtil;
import it.iccu.sbn.vo.custom.servizi.calendario.ModelloCalendarioDecorator;
import it.iccu.sbn.vo.validators.calendario.ModelloCalendarioValidator;
import it.iccu.sbn.web.actionforms.servizi.calendario.CalendarioForm;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.gestioneservizi.CalendarioDelegate;
import it.iccu.sbn.web.vo.UserVO;
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
import org.joda.time.LocalDate;

public class CalendarioAction extends CalendarioBaseAction implements SbnAttivitaChecker {

	static Logger log = Logger.getLogger(CalendarioAction.class);

	private static final String[] BOTTONIERA = new String[] {
			"servizi.bottone.conferma",
			//"servizi.bottone.esamina",
			"servizi.bottone.nuovo.intervallo",
			//"servizi.bottone.elimina.intervallo",
			//"servizi.bottone.calendario",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_MEDIAZIONE = new String[] {
			"servizi.bottone.ok",
			"servizi.bottone.esamina",
			"servizi.bottone.nuovo.intervallo",
			"servizi.bottone.elimina.intervallo",
			//"servizi.bottone.calendario",
			"servizi.bottone.annulla" };

	private static final String[] BOTTONIERA_CONFERMA = new String[] {
			"servizi.bottone.si",
			"servizi.bottone.no",
			"servizi.bottone.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.conferma", "conferma");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("servizi.bottone.esamina", "esamina");
		map.put("servizi.bottone.nuovo.intervallo", "nuovo");
		map.put("servizi.bottone.elimina.intervallo", "cancella");
		map.put("servizi.bottone.calendario", "calendario");

		map.put("servizi.bottone.cambioBiblioteca", "biblio");

		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		CalendarioForm currentForm = (CalendarioForm) form;
		if (!currentForm.isInitialized() )
			init(request, form);

		loadForm(request, currentForm);

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		super.init(request, form);

		CalendarioForm currentForm = (CalendarioForm) form;
		UserVO u = Navigation.getInstance(request).getUtente();
		currentForm.setBiblioteca(u.getCodBib());

		BibliotecaVO bib = DomainEJBFactory.getInstance().getBiblioteca().getBiblioteca(u.getCodPolo(), currentForm.getBiblioteca() );
		ParametriServizi params = currentForm.getParametri();
		params.put(ParamType.BIBLIOTECA, bib);

		currentForm.setListaCategorieMediazione(CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE));

		currentForm.setInitialized(true);
	}

	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		super.loadForm(request, form);

		CalendarioForm currentForm = (CalendarioForm) form;
		ParametriServizi params = ValidazioneDati.coalesce(ParametriServizi.retrieve(request), currentForm.getParametri() );
		ModelloCalendarioVO modello = (ModelloCalendarioVO) params.get(ParamType.MODELLO_CALENDARIO);
		if (modello == null) {
			LocalDate inizio = LocalDate.now().withMonthOfYear(1).withDayOfMonth(1);
			LocalDate fine = inizio.plusYears(1).minusDays(1);
			modello = CalendarioUtil.getTemplate(inizio.toDate(), fine.toDate(), "nuovo calendario");
			BibliotecaVO bib = (BibliotecaVO) params.get(ParamType.BIBLIOTECA);
			modello.setCodPolo(bib.getCod_polo());
			modello.setCodBib(bib.getCod_bib());
			modello.setTipo(TipoCalendario.BIBLIOTECA);
		}

		modello.setFlCanc("N");
		ModelloCalendarioVO decorated = ModelloCalendarioDecorator.decorate(modello);
		currentForm.setModello(decorated);
		List<IntervalloVO> intervalli = decorated.getIntervalli();
		if (intervalli.size() == 1)
			currentForm.setSelected(ValidazioneDati.first(intervalli).getUniqueId());

		//personalizzazione testo navbar
		Navigation navi = Navigation.getInstance(request);
		String prefix = navi.getCache().getCurrentElement().getUri().replace('/', '.') + '.' + modello.getTipo();
		navi.setTesto(prefix  + ".testo");
		navi.setDescrizioneX(prefix + ".descrizione");

		impostaPulsanti(currentForm);

		currentForm.setParametri(params);
	}

	private void impostaPulsanti(ActionForm form) {
		CalendarioForm currentForm = (CalendarioForm) form;
		CalendarioVO modello = currentForm.getModello();
		if (modello.getTipo() == TipoCalendario.MEDIAZIONE)
			currentForm.setPulsanti(BOTTONIERA_MEDIAZIONE);
		else
			currentForm.setPulsanti(BOTTONIERA);
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CalendarioForm currentForm = (CalendarioForm) form;
		ModelloCalendarioVO modello = currentForm.getModello();

		IntervalloVO nuovoIntervallo = ValidazioneDati.first(modello.getIntervalli()).copy();
		nuovoIntervallo.setId(0);
		nuovoIntervallo.setBase(false);
		nuovoIntervallo.setDescrizione(null);
		ParametriServizi parametri = currentForm.getParametri().copy();

		//parametri.put(ParamType.PARAMETRI_RICERCA, ricerca.copy() );
		parametri.put(ParamType.MODELLO_CALENDARIO, modello);
		parametri.put(ParamType.INTERVALLO_CALENDARIO, nuovoIntervallo );
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("intervallo"));
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CalendarioForm currentForm = (CalendarioForm) form;
		ModelloCalendarioVO modello = currentForm.getModello();

		if (!isTokenValid(request))
			saveToken(request);

		Integer selected = currentForm.getSelected();
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		IntervalloVO intervallo = BaseVO.search(selected, modello.getIntervalli() );
		ParametriServizi parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.MODELLO_CALENDARIO, modello);
		parametri.put(ParamType.INTERVALLO_CALENDARIO, intervallo );
		ParametriServizi.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("intervallo"));
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CalendarioForm currentForm = (CalendarioForm) form;
		ModelloCalendarioVO modello = currentForm.getModello();

		if (!isTokenValid(request))
			saveToken(request);

		Integer selected = currentForm.getSelected();
		if (!ValidazioneDati.isFilled(selected)) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceNessunaSelezione"));
			saveToken(request);
			return mapping.getInputForward();
		}

		List<IntervalloVO> intervalli = modello.getIntervalli();
		int idx = BaseVO.indexOfUniqueId(selected, intervalli);
		IntervalloVO intervallo = intervalli.get(idx);
		if (intervallo.isBase()) {
			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.calendario.cancella.intervallo.base"));
			saveToken(request);
			return mapping.getInputForward();
		}

		intervalli.remove(idx);

		return mapping.getInputForward();
	}

	public ActionForward calendario(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CalendarioForm currentForm = (CalendarioForm) form;
		ModelloCalendarioVO modello = currentForm.getModello();

		try {
			modello.validate(new ModelloCalendarioValidator() );

			checkCongruenzaCalendarioBib(request, modello);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		ParametriServizi parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.MODELLO_CALENDARIO, modello);
		ParametriServizi.send(request, parametri);

		Navigation navi = Navigation.getInstance(request);
		return navi.goBack();
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

		CalendarioForm currentForm = (CalendarioForm) form;
		ModelloCalendarioVO modello = currentForm.getModello();

		try {
			modello.validate(new ModelloCalendarioValidator() );

			checkCongruenzaCalendarioBib(request, modello);

			currentForm.setConferma(true);
			currentForm.setPulsanti(BOTTONIERA_CONFERMA);
			currentForm.setOperazione(TipoOperazione.SALVA);

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazioneAgg"));
			return mapping.getInputForward();

		} catch (SbnBaseException e) {
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

		CalendarioForm currentForm = (CalendarioForm) form;
		TipoOperazione op = currentForm.getOperazione();
		switch (op) {
		case SALVA:
			return eseguiAggiornamento(mapping, form, request, response);
		default:
			break;
		}

		return no(mapping, currentForm, request, response);
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CalendarioForm currentForm = (CalendarioForm) form;
		currentForm.setConferma(false);
		impostaPulsanti(currentForm);

		return mapping.getInputForward();
	}

	private ActionForward eseguiAggiornamento(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CalendarioForm currentForm = (CalendarioForm) form;
		try {
			ModelloCalendarioVO modello = currentForm.getModello();
			modello = CalendarioDelegate.getInstance(request).aggiornaModelloCalendario(modello);
			currentForm.setModello(modello);

			LinkableTagUtils.addError(request, new ActionMessage("message.servizi.insOK"));

			Navigation navi = Navigation.getInstance(request);
			return navi.goBack();

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

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		CalendarioForm currentForm = (CalendarioForm) form;
		ModelloCalendarioVO modello = currentForm.getModello();

		if (ValidazioneDati.equals(idCheck, "servizi.bottone.elimina.intervallo")) {
			return ValidazioneDati.size(modello.getIntervalli()) > 1;
		}

		return true;
	}

}
