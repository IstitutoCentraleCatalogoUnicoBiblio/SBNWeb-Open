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
package it.iccu.sbn.web.actions.gestionesemantica.batch;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriCancellazioneSoggettiNonUtilizzatiVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.amministrazione.Attivita;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.validators.semantica.CancellaSoggettiNonUtilizzatiValidator;
import it.iccu.sbn.web.actionforms.gestionesemantica.batch.PrenotaBatchSemanticaForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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
import org.apache.struts.action.ActionMessages;

public class PrenotaBatchSemanticaAction extends NavigationBaseAction {

	private static Logger log = Logger.getLogger(PrenotaBatchSemanticaAction.class);

	private static String[] BOTTONIERA = new String[] {
		"button.conferma", "button.annulla" };
	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
		"button.no", "button.annulla" };

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma", "prenota");
		map.put("button.annulla", "indietro");
		map.put("button.si", "si");
		map.put("button.no", "no");
		return map;
	}

	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		PrenotaBatchSemanticaForm currentForm = (PrenotaBatchSemanticaForm) form;
		UserVO utente = Navigation.getInstance(request).getUtente();
		List<ComboCodDescVO> listaSogg = CaricamentoComboSemantica.loadComboSoggettario(utente.getTicket(), true);
		currentForm.setListaSoggettari(listaSogg);
		currentForm.setInitialized(true);
		currentForm.setListaPulsanti(BOTTONIERA);

		ParametriCancellazioneSoggettiNonUtilizzatiVO parametri = currentForm.getParametri();
		parametri.setCodPolo(utente.getCodPolo());
		parametri.setCodBib(utente.getCodBib());
		parametri.setUser(utente.getUserId());

		//almaviva5_20111125 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}

    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
    		return mapping.getInputForward();

    	PrenotaBatchSemanticaForm currentForm = (PrenotaBatchSemanticaForm) form;
		if (!currentForm.isInitialized()) {
			init(request, form);
			String cdAttivita = request.getParameter("CDATTIVITA");
			if (ValidazioneDati.strIsNull(cdAttivita)) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.batch.cdAttivitaMancante"));
				saveErrors(request, errors);
				return navi.goBack(true);
			}
			// tipo batch richiesto
			currentForm.getParametri().setCodAttivita(cdAttivita);
			log.debug("Codice Attivita richiesto: " + cdAttivita);

			//descrizione attivita su navigazione
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			Attivita Att = (Attivita) utenteEjb.getListaAttivita().get(cdAttivita);
			navi.setSuffissoTesto(" " + Att.getAttivita());
		}

    	return mapping.getInputForward();
    }

    public ActionForward prenota(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	ActionMessages errors = new ActionMessages();
    	PrenotaBatchSemanticaForm currentForm = (PrenotaBatchSemanticaForm) form;
    	try {
			currentForm.getParametri().validate();
			currentForm.setConferma(true);
			currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);

			String tipoOggetto;
			String codAttivita = currentForm.getParametri().getCodAttivita();

			if (CodiciAttivita.getIstance().CANCELLAZIONE_SOGGETTI_INUTILIZZATI.equals(codAttivita))
				tipoOggetto = "soggetti";
			else
				if (CodiciAttivita.getIstance().CANCELLAZIONE_DESCRITTORI_INUTILIZZATI.equals(codAttivita))
					tipoOggetto = "descrittori";
				else
					throw new ValidationException(SbnErrorTypes.BATCH_INVALID_PARAMS);

			//avvertimento
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.batch.confermaCancellaSogDes", tipoOggetto));
			saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (ValidationException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

    }

    public ActionForward indietro(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	return Navigation.getInstance(request).goBack(true);
    }

    public ActionForward si(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	PrenotaBatchSemanticaForm currentForm = (PrenotaBatchSemanticaForm) form;

		ParametriCancellazioneSoggettiNonUtilizzatiVO parametri = currentForm.getParametri();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		try {
			try {
				UserVO utente = Navigation.getInstance(request).getUtente();
				parametri.validate();

				String idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), parametri,
					null, new CancellaSoggettiNonUtilizzatiValidator() );
				if (idBatch == null) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
					resetToken(request);
					return mapping.getInputForward();
				}

				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));

			} catch (ApplicationException e) {
				LinkableTagUtils.addError(request, e);
				return mapping.getInputForward();

			} catch (ValidationException e) {
				LinkableTagUtils.addError(request, e);
				return mapping.getInputForward();
			}
		}	finally {
			//disattivo conferma
			this.no(mapping, currentForm, request, response);
		}

		return mapping.getInputForward();
	}

    public ActionForward no(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	PrenotaBatchSemanticaForm currentForm = (PrenotaBatchSemanticaForm) form;
		currentForm.setConferma(false);
		currentForm.setListaPulsanti(BOTTONIERA);
    	return mapping.getInputForward();
    }

}
