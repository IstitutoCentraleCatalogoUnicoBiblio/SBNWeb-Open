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
package it.iccu.sbn.web.actions.elaborazioniDifferite.esporta;

import it.iccu.sbn.batch.unimarc.ExportUnimarcBatch;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaAuthorityVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta.EstrattoreIdAuthorityForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.FileUtil;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;



public class EstrattoreIdAuthorityAction extends NavigationBaseAction {

	private static Logger log = Logger.getLogger(EstrattoreIdAuthorityAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.prenotaEstr", "prenota");// metodo per esporta
		map.put("button.caricafile", "caricaFile");
		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form)
			throws Exception {

		EstrattoreIdAuthorityForm currentForm = (EstrattoreIdAuthorityForm) form;
		if (currentForm.isInitialized())
			return;

		log.debug("init()");
		EsportaAuthorityVO esporta = currentForm.getEsporta();
		esporta.setAuthority(SbnAuthority.AU);	//default su autore

		UserVO utente = Navigation.getInstance(request).getUtente();
		esporta.setCodPolo(utente.getCodPolo());
		esporta.setCodBib(utente.getCodBib());
		esporta.setUser(utente.getUserId());
		esporta.setTicket(utente.getTicket());
		esporta.setCodAttivita(CodiciAttivita.getIstance().ESPORTA_ELEMENTI_DI_AUTHORITY_1041);

		long extractionTime = ExportUnimarcBatch.getDbLastExtractionTime();
		if (extractionTime > 0)
			currentForm.setExtractionTime((new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Timestamp(extractionTime))));
		else {
			esporta.setExportDB(true); // db mai esportato
			currentForm.setExtractionTime(null);
		}

		super.init(request, form);

		currentForm.setInitialized(true);
	}

	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form)
			throws Exception {

		super.loadForm(request, form);
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	public ActionForward caricaFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		EstrattoreIdAuthorityForm currentForm = (EstrattoreIdAuthorityForm) form;

		// gestione upload immagine marca
		FormFile input = currentForm.getInputFile();
		if (input == null)
			return mapping.getInputForward();

		try {
			String fileName = FileUtil.getTemporaryFileName();
			log.debug("Caricamento file temporaneo: " + fileName);
			FileUtil.uploadFile(input, fileName, null);
			LinkableTagUtils.addError(request, new ActionMessage("errors.importa.ricConfermaAcquisizioneFile", input.getFileName()));
			currentForm.getEsporta().setInputFile(fileName);

		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("errors.importa.erroreAcquisizioneFile"));
		}

		return mapping.getInputForward();
	}


	public ActionForward prenota(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EstrattoreIdAuthorityForm currentForm = (EstrattoreIdAuthorityForm) form;
		String idBatch = null;

		try {
			resetToken(request);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			UserVO utente = Navigation.getInstance(request).getUtente();
			EsportaAuthorityVO esporta = currentForm.getEsporta().copy();

			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), esporta, null);

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

		return mapping.getInputForward();
	}

}
