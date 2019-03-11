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
package it.iccu.sbn.web.actions.elaborazioniDifferite;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ActionPathVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.AreaBatchVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ElaborazioniDifferiteConfig;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.TipoAttivita;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchSollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.batch.RifiutaPrenotazioniScaduteVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.InvioElaborazioniDifferiteForm;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.InvioElaborazioniDifferiteForm.TipoVisualizzazione;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;


public class InvioElaborazioniDifferiteAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static final String ACTION_TYPE = "type";
	private static final String AREA = "area.batch";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.indietro", 	"indietro");
		return map;
	}

	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String parameter)
			throws Exception {

		InvioElaborazioniDifferiteForm currentForm = (InvioElaborazioniDifferiteForm) form;

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(InvioElaborazioniDifferiteForm.SUBMIT_TESTO_PULSANTE) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				currentForm.setSelez(tokens[2]);
				return "prenota";
			}

		}
		return super.getMethodName(mapping, form, request, response, parameter);

	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		InvioElaborazioniDifferiteForm currentForm = (InvioElaborazioniDifferiteForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar() )
		        return mapping.getInputForward();

			if (!currentForm.isSessione())
				init(request, form);

			if (!ValidazioneDati.isFilled(currentForm.getArea().getAttivita()) ) {
				switch (currentForm.getTipoAttivita()) {
				case STAMPA:
					navi.setDescrizioneX("Invio richieste di stampa");
					navi.setTesto("Invio richieste di stampa");
					break;
				case FUNZIONE:
					navi.setDescrizioneX("Invio richieste funzioni di servizio");
					navi.setTesto("Invio richieste funzioni di servizio");
					break;
				case STATISTICHE:
					navi.setDescrizioneX("Invio richieste Statistiche");
					navi.setTesto("Invio richieste Statistiche");
					break;
				}
			} else {
				String id = currentForm.getArea().getId();
				log.debug("invio batch: " + id);
				navi.setDescrizioneX(LinkableTagUtils.findMessage(request, getLocale(request), id));
				navi.setTesto(LinkableTagUtils.findMessage(request, getLocale(request), id));
			}

			return mapping.getInputForward();

		} catch (ValidationException ve) {
			return mapping.getInputForward();

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.elabdiff.erroreNonGestito"));
			return mapping.getInputForward();
		}

	}

	private ActionForward prenotaElaborazioneDifferita(ActionMapping mapping,
			HttpServletRequest request, String cdAttivita) throws Exception {

		if (ValidazioneDati.equals(cdAttivita, CodiciAttivita.getIstance().SERVIZI_SOLLECITI))
			return prenotaSolleciti(mapping, request);

		if (ValidazioneDati.equals(cdAttivita, CodiciAttivita.getIstance().SRV_RIFIUTO_PRENOTAZIONI_SCADUTE))
			return prenotaRifiutoPrenotazioniScadute(mapping, request);

		return mapping.getInputForward();

	}

	private ActionForward prenotaRifiutoPrenotazioniScadute(ActionMapping mapping, HttpServletRequest request) throws Exception {

		String idBatch = null;

		try {
			resetToken(request);

			// Prova export
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// codice polo + codice biblioteca
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			RifiutaPrenotazioniScaduteVO richiesta = ServiziUtil.buildRifiutaPrenotazioniScaduteVO(
					utenteCollegato.getCodPolo(), utenteCollegato.getCodBib(), utenteCollegato.getUserId());
			richiesta.validate();

			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), richiesta, null);

		} catch (ValidationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);

		}catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		}

		if (idBatch != null)
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));
		else
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));

		return Navigation.getInstance(request).goBack(true);
	}

	private ActionForward prenotaSolleciti(ActionMapping mapping, HttpServletRequest request) throws Exception {

		String idBatch = null;

		try {
			resetToken(request);

			// Prova export
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// Setting biblioteca corrente (6 caratteri)
			// codice polo + codice biblioteca
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			ParametriBatchSollecitiVO richiesta = new ParametriBatchSollecitiVO();
			richiesta.setCodPolo(utenteCollegato.getCodPolo());
			richiesta.setCodBib(utenteCollegato.getCodBib());
			richiesta.setUser(utenteCollegato.getUserId());
			richiesta.setDataScadenza(DaoManager.now());
			richiesta.setDescrizioneBiblioteca(utenteCollegato.getBiblioteca());
			richiesta.setCodAttivita(CodiciAttivita.getIstance().SERVIZI_SOLLECITI);

			richiesta.validate();

			List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().SERVIZI_SOLLECITI);
			if (!ValidazioneDati.isFilled(modelli)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.stampe.modelli.conf"));
				resetToken(request);
				return mapping.getInputForward();
			}

			String jrxml = modelli.get(0).getJrxml();

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml = basePath + File.separator + "jrxml" + File.separator + jrxml;
			richiesta.setTemplate(pathJrxml);
			richiesta.setTipoStampa(TipoStampa.PDF.name());

			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), richiesta, null);

		} catch (ValidationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);

		}catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		}

		if (idBatch != null)
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));
		else
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));

		return Navigation.getInstance(request).goBack(true);
	}

	private void init(HttpServletRequest request, ActionForm form) throws Exception {

		Navigation navi = Navigation.getInstance(request);
		InvioElaborazioniDifferiteForm currentForm = (InvioElaborazioniDifferiteForm) form;

		//almaviva5_20100408
		String type = request.getParameter(ACTION_TYPE);
		if (type != null)
			currentForm.setTipoAttivita(TipoAttivita.valueOf(type));

		AreaBatchVO area = (AreaBatchVO) request.getAttribute(AREA);

		if (area == null) {	// sono entrato per selezionare un'area
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ElaborazioniDifferiteConfig config =
				factory.getElaborazioniDifferite().getConfigurazioneElaborazioniDifferite(currentForm.getTipoAttivita());
			currentForm.setConfig(config);
			currentForm.setTipoVisualizzazione(TipoVisualizzazione.AREA);

		} else {	// selezione batch
			currentForm.setArea(area);
			currentForm.setTipoAttivita(area.getAttivita().get(0).getTipo());//rosa
			currentForm.setTipoVisualizzazione(TipoVisualizzazione.ATTIVITA);
			if (navi.isFirst()){
				navi.setSuffissoTesto(" " + LinkableTagUtils.findMessage(request, getLocale(request), area.getId()) );
			}
		}


		currentForm.setSessione(true);
	}

	public ActionForward prenota(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
	        return mapping.getInputForward();

		String cdAttivita = request.getParameter(Constants.CODICE_ATTIVITA);
		if (cdAttivita != null)
			return prenotaElaborazioneDifferita(mapping, request, cdAttivita);


		if (request.getParameter(AREA) != null)
			return unspecified(mapping, form, request, response);

		InvioElaborazioniDifferiteForm currentForm = (InvioElaborazioniDifferiteForm) form;

		if (ValidazioneDati.strIsNull(currentForm.getSelez()))
		  	return mapping.getInputForward();

		ElaborazioniDifferiteConfig config = currentForm.getConfig();
		Integer idx = Integer.valueOf(currentForm.getSelez());

		switch (currentForm.getTipoVisualizzazione()) {
		case AREA:	// cambio la visualizzazione per singola area
			request.setAttribute(AREA, config.getAree().get(idx) );
			currentForm.setSelez(null);
			NavigationForward forward = navi.goForward(mapping.findForward("self"));
			forward.addParameter(AREA, AREA);
			return forward;

		case ATTIVITA:
			//chiamo la pagina personalizzata per la prenotazione del batch
			ActionPathVO actionVO = currentForm.getArea().getAttivita().get(idx);
			NavigationForward forward2 = navi.goForward(new ActionForward(actionVO.getActionPath()) );
			forward2.addParameter(Constants.CODICE_ATTIVITA, actionVO.getCodAttivita());
			return forward2;
		}

		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBack() )
	        return mapping.getInputForward();

		return navi.goBack();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			//mostro sulla jsp solo le attivita abilitate
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			InvioElaborazioniDifferiteForm currentForm = (InvioElaborazioniDifferiteForm) form;
			ElaborazioniDifferiteConfig config = currentForm.getConfig();

			//check area funzionale
			//se almeno uno dei codici attivita contenuto nell'area risulta abilitato
			//devo mostrare l'area funzionale sulla pagina jsp
			if (idCheck.startsWith("AREA")) {
				int idx = Integer.valueOf(idCheck.split("-")[1]);
				AreaBatchVO area = config.getAree().get(idx);
				List<String> codiciAttivita = area.getCodiciAttivita();
				boolean show = false;
				for (String attivita : codiciAttivita) {
					try {
						utenteEjb.checkAttivita(attivita);
						show = true;
						break;
					} catch (UtenteNotAuthorizedException e) {
						show = false;
					}
				}

				return show;
			}

			//check singolo codice attivita
			utenteEjb.checkAttivita(idCheck);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

}

