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
package it.iccu.sbn.web.actions.gestionestampe.semantica;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaSoggettarioVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.validators.semantica.StampaSoggettarioValidator;
import it.iccu.sbn.web.actionforms.gestionestampe.semantica.PrenotaStampaSemanticaForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.util.ArrayList;
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
import org.apache.struts.actions.LookupDispatchAction;

public class PrenotaStampaSemanticaAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(PrenotaStampaSemanticaAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma", "conferma");
		map.put("button.indietro", "indietro");
		return map;
	}


	private void initCombo(HttpServletRequest request, ActionForm form, ParametriStampaVO parametri) throws Exception {

		PrenotaStampaSemanticaForm currentForm = (PrenotaStampaSemanticaForm) form;

		//lista parametri si/no
		List<CodiceVO> listaSiNo = new ArrayList<CodiceVO>();
		listaSiNo.add(new CodiceVO("true", "Si"));
		listaSiNo.add(new CodiceVO("false", "No"));
		currentForm.setListaSiNo(listaSiNo);

		String ticket = Navigation.getInstance(request).getUserTicket();

		//soggettari
		List<ComboCodDescVO> listaSoggettari = CaricamentoComboSemantica.loadComboSoggettario(ticket, true);
		if (ValidazioneDati.isFilled(listaSoggettari))
			listaSoggettari = CaricamentoCombo.cutFirst(listaSoggettari);
		currentForm.setListaSoggettari(listaSoggettari);

		//thesauri
		List<ComboCodDescVO> listaThesauri = CaricamentoComboSemantica.loadComboThesauro(ticket, true);
		if (ValidazioneDati.isFilled(listaThesauri))	// solo un thesauro
			listaThesauri = CaricamentoCombo.cutFirst(listaThesauri);
		currentForm.setListaThesauri(listaThesauri);

		//classi
		List<ComboCodDescVO> listaClassi = CaricamentoComboSemantica.loadComboSistemaClassificazione(ticket, true);
		currentForm.setListaSistemiClassificazione(listaClassi);

		//edizione
		CodiciType type;
		if (ValidazioneDati.in(parametri.getCodAttivita(),
				CodiciAttivita.getIstance().STAMPA_SOGGETTARIO,
				CodiciAttivita.getIstance().STAMPA_DESCRITTORI) )
			 type = CodiciType.CODICE_EDIZIONE_SOGGETTARIO;
		else
			type = CodiciType.CODICE_EDIZIONE_CLASSE;
		currentForm.setListaEdizioni(CodiciProvider.getCodici(type));
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrenotaStampaSemanticaForm currentForm = (PrenotaStampaSemanticaForm) form;
		ParametriStampaVO parametri = null;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				String codAttivita = request.getParameter(Constants.CODICE_ATTIVITA);

				if (ValidazioneDati.equals(CodiciAttivita.getIstance().STAMPA_SOGGETTARIO, codAttivita) )
					parametri = new ParametriStampaSoggettarioVO();
				if (ValidazioneDati.equals(CodiciAttivita.getIstance().STAMPA_THESAURO_POLO, codAttivita) )
					parametri = new ParametriStampaTerminiThesauroVO();
				if (ValidazioneDati.equals(CodiciAttivita.getIstance().STAMPA_DESCRITTORI, codAttivita) ) {
					ParametriStampaDescrittoriVO psd = new ParametriStampaDescrittoriVO();
					psd.setStampaInsModBiblioteca(false);
					psd.setStampaFormeRinvio(true);
					psd.setStampaDescrittoriManuali(true);
					parametri = psd;
				}

				parametri.setCodAttivita(codAttivita);
				parametri.setTipoStampa(TipoStampa.PDF.name());
				currentForm.setParametri(parametri);
				currentForm.setTipoFormato(TipoStampa.PDF.name());

				initCombo(request, form, parametri);

				currentForm.setSessione(true);
			}

			return mapping.getInputForward();

		} catch (Exception e) {
			log.error("", e);
			return mapping.getInputForward();
		}

	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return Navigation.getInstance(request).goBack();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PrenotaStampaSemanticaForm currentForm = (PrenotaStampaSemanticaForm) form;

		ParametriStampaVO parametri = currentForm.getParametri();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		try {
			UserVO utente = Navigation.getInstance(request).getUtente();
			parametri.setTicket(utente.getTicket());
			parametri.setCodPolo(utente.getCodPolo());
			parametri.setCodBib(utente.getCodBib());
			parametri.setUser(utente.getUserId());
			parametri.setDescrizioneBiblioteca(utente.getBiblioteca());

			parametri.validate();

			List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(parametri.getCodAttivita());
			if (!ValidazioneDati.isFilled(modelli)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.stampe.modelli.conf"));
				resetToken(request);
				return mapping.getInputForward();
			}

			String jrxml = modelli.get(0).getJrxml();

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml = basePath + File.separator + "jrxml" + File.separator + jrxml;
			parametri.setTemplate(pathJrxml);
			parametri.setTipoStampa(currentForm.getTipoFormato());

			String idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(),
					parametri, null, new StampaSoggettarioValidator());
			if (idBatch == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
				resetToken(request);
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok" , idBatch));

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

}


