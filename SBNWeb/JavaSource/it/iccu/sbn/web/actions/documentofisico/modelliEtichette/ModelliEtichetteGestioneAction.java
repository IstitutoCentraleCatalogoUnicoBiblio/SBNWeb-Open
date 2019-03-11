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
package it.iccu.sbn.web.actions.documentofisico.modelliEtichette;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.etichette.FormattazioneVOCampoEtichetta;
import it.iccu.sbn.web.actionforms.documentofisico.modelliEtichette.ModelliEtichetteGestioneForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

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
import org.apache.struts.actions.LookupDispatchAction;

/**
 * ModelliEtichetteGestioneAction.java
 * dic-2006
 *
 */
public class ModelliEtichetteGestioneAction extends LookupDispatchAction {

	private static Log log = LogFactory.getLog(ModelliEtichetteGestioneAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.aggiorna", "aggiorna");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try{
			ModelliEtichetteGestioneForm myForm = (ModelliEtichetteGestioneForm) form;
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();
			// controllo se ho già i dati in sessione;
			if (!myForm.isSessione()) {
				myForm.setTicket(Navigation.getInstance(request).getUserTicket());
				myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				myForm.setSessione(true);
			}
			if (request.getAttribute("codBib") != null
					&& request.getAttribute("descrBib") != null) {
				myForm.setCodBib((String)request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
				//quando è modifica o esamina
				if (request.getAttribute("codModello") != null){
					myForm.getRecModello().setCodModello((String)request.getAttribute("codModello"));
				}
			}
			myForm.setDisableBib(true);
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("nuova")) {

				myForm.setDisable(false);
				myForm.setProv("nuova");
				//da qui
				request.setAttribute("currentForm", form);
					String nomeSalva="cache_modello";
					request.getSession().setAttribute(nomeSalva,myForm.copiaValoriFormattazione());

					nomeSalva="cache_mod_caricato";
					if (request.getSession().getAttribute(nomeSalva)==null)
					{
						request.getSession().setAttribute(nomeSalva,myForm.copiaValoriFormattazione());
					}
//					myForm.setDescrBibEtichetta(Navigation.getInstance(request).getUtente().getBiblioteca());
					myForm.setDescrBibEtichetta(myForm.getCodBib());

					return mapping.getInputForward();
					//a qui
				} else {
				// carico i dati
				ModelloEtichetteVO modello = this.getModello(myForm.getCodPolo(), myForm.getCodBib(),
						myForm.getRecModello().getCodModello(), myForm.getTicket());
				if (modello != null){
					if (modello.getDescrBib() != null){
						myForm.setDescrBibEtichetta(modello.getDescrBib());
					}
					myForm.setCodModello(modello.getCodModello());
					myForm.setDescrModello(modello.getDescrModello());
					myForm.setRecModello(modello);
					myForm.getFormattazioneModello().importaModello(modello.getDatiForm());
					if (request.getAttribute("prov").equals("esamina")){
						myForm.setProv("esamina");
						myForm.setDisable(true);
						myForm.setDisableCodModello(true);
						myForm.setEsamina(true);
					}else if (request.getAttribute("prov").equals("modifica")) {
						myForm.setProv("modifica");
						myForm.setDisable(false);
						myForm.setDisableBib(true);
						myForm.setDisableCodModello(true);
					}
				}
			}
			return mapping.getInputForward();

		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteGestioneAction.java
	 * ok
	 * ActionForward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * scelta di creazione di una sezione di collocazione
	 */
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteGestioneForm myForm = (ModelliEtichetteGestioneForm) form;
		try {
			// insert sezione
			if (myForm.getProv().equals("nuova")) {

				ModelloEtichetteVO recModello = new ModelloEtichetteVO();
				recModello.setCodPolo(myForm.getCodPolo());
				recModello.setCodBib(myForm.getCodBib());
				recModello.setDescrBib(myForm.getDescrBibEtichetta());
				recModello.setCodModello(myForm.getCodModello());
				recModello.setDescrModello(myForm.getDescrModello());
				boolean errore = false;
				boolean visualizza = false;
				if (myForm.getFormattazioneModello().getCampiEtichetta().size() > 0){
					ActionForward forward = controllaCheck(request, myForm, visualizza, mapping);
					if (forward != null)
						return forward;
				}
				recModello.setDatiForm(myForm.getFormattazioneModello().esportaModello());
				recModello.setTipoModello("C");
				recModello.setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				myForm.setRecModello(recModello);
				if (this.insertModello(myForm.getRecModello(), myForm.getTicket())) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.insertOk"));
					this.saveErrors(request, errors);
					this.saveToken(request);
					return Navigation.getInstance(request).goBack();
				}
			} else if (myForm.getProv().equals("modifica")) {
				// richiesta conferma update sezione
				boolean errore = false;
				boolean visualizza = false;
				if (myForm.getFormattazioneModello().getCampiEtichetta().size() > 0){
					ActionForward forward = controllaCheck(request, myForm, visualizza, mapping);
					if (forward != null)
						return forward;

				}

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaModifica"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setConferma(true);
				myForm.setDisable(true);
				return mapping.getInputForward();
			} else {
				return mapping.findForward("ok");
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.modelliEtichette
	  * ModelliEtichetteGestioneAction.java
	  * controllaCheck
	  * void
	  * @param request
	  * @param myForm
	  * @param visualizza
	 * @param mapping
	  *
	  *
	 */
	private ActionForward controllaCheck(HttpServletRequest request, ModelliEtichetteGestioneForm myForm, boolean visualizza, ActionMapping mapping) {
		boolean errore=false;

		List<FormattazioneVOCampoEtichetta> listaEtichette = myForm.getFormattazioneModello().getCampiEtichetta();
		int size = (listaEtichette.size() - 1);
		for (int i = size; i >=0; i--){
			FormattazioneVOCampoEtichetta elementoEtich = listaEtichette.get(i);
			if (elementoEtich.isPresente()) visualizza = true;
			if (elementoEtich.isConcatena()) errore = true;
			if(elementoEtich.isPresente() && !elementoEtich.isConcatena()) errore = false;
		}
		if (!visualizza){
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico.visualizzareAlmenoUnElemento"));
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();

		} else if(errore){
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico.incongruenzaSuConcatena"));
			this.saveErrors(request, errors);
			this.saveToken(request);
			return mapping.getInputForward();
		}

		for (int i = 0; i <= size; i++ ) {
			FormattazioneVOCampoEtichetta elementoEtich = listaEtichette.get(i);
			if (elementoEtich.isPresente() && !elementoEtich.isConcatena() && i > 0) {
				for (int p = 0; p < i; p++) {
					FormattazioneVOCampoEtichetta prev = listaEtichette.get(p);
					if (prev.isPresente() && elementoEtich.getY() < prev.getY()) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.incongruenzaSuPosizioneY",
								prev.getNomeCampo(), elementoEtich.getNomeCampo()));
						this.saveErrors(request, errors);
						this.saveToken(request);
						return mapping.getInputForward();
					}
				}
			}
		}

		return null;
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteGestioneAction.java
	 * chiudi
	 * ActionForward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * scelta di abbandono della pagina di inserimento sezione di collocazione
	 */
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteGestioneForm myForm = (ModelliEtichetteGestioneForm) form;
		try {
			if (myForm.getProv() != null && (myForm.getProv().equals("nuova") || myForm.getProv().equals("modifica"))) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaModifica"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setConferma(true);
				return mapping.getInputForward();
			} else {
				return Navigation.getInstance(request).goBack(true);
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteGestioneAction.java
	 * formati
	 * ActionForward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * il metodo corrisponde al tasto 'formati' che viene abilitato a fronte
	 * di tipo collocazione 'a formato' o 'continuazione'
	 */
	public ActionForward formati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteGestioneForm myForm = (ModelliEtichetteGestioneForm) form;
		try {
			if (myForm.getProv().equals("nuova") ) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaSalva"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setConferma(true);
				return mapping.getInputForward();
			}else if (myForm.getProv().equals("esamina")){
				request.setAttribute("codPolo",myForm.getRecModello().getCodPolo());
				request.setAttribute("codBib",myForm.getRecModello().getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				request.setAttribute("codModello",myForm.getRecModello().getCodModello());
				request.setAttribute("prov", "sezGest");
				return mapping.findForward("esamina");
			}else {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaSalva"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setConferma(true);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return Navigation.getInstance(request).goBack(true);
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteGestioneAction.java
	 * aggiorna
	 * ActionForward
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * il metodo corrisponde al tasto 'aggiorna' che viene abilitato a fronte
	 * di scelta di tipo collocazione impostato a
	 * "magazzino non a formato", per l'abilitazione del campo ultPrgAss, o
	 * "sistema di classificazione", per l'abilitazione del campo sistClass o
	 * "magazzino a formato" o "continuazion" per l'abilitazione del tasto "formati"
	 */
	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteGestioneForm myForm = (ModelliEtichetteGestioneForm) form;
		try {
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModelliEtichetteGestioneForm myForm = (ModelliEtichetteGestioneForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (myForm.getProv().equals("modifica")) {
				myForm.getRecModello().setDescrBib(myForm.getDescrBibEtichetta());
				myForm.getRecModello().setDescrModello(myForm.getDescrModello().trim());
				myForm.getRecModello().setDatiForm(myForm.getFormattazioneModello().esportaModello());
				myForm.getRecModello().setTipoModello("C");
				myForm.getRecModello().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				if (!this.updateModello(myForm.getRecModello(), myForm.getTicket())) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.errataModifica"));
					this.saveErrors(request, errors);
					this.saveToken(request);
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.updateOk"));
					this.saveErrors(request, errors);
					this.saveToken(request);
				}
				return mapping.findForward("ok");
			} else if (myForm.isConferma()) {
			} else {
				return mapping.findForward("ok");
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModelliEtichetteGestioneForm myForm = (ModelliEtichetteGestioneForm) form;
		// Viene settato il token per le transazioni successive
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	private boolean insertModello(ModelloEtichetteVO modello, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().insertModello(modello, ticket);
		return ret;
	}

	private boolean updateModello(ModelloEtichetteVO modello, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().updateModello(modello,  ticket);
		return ret;
	}

	private ModelloEtichetteVO getModello(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		ModelloEtichetteVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getModello(codPolo, codBib, codSez, ticket);
		return rec;
	}
}
