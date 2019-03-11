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
package it.iccu.sbn.web.actions.gestionebibliografica.utility;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ImportaLegamiBidAltroIdVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ImportaLegamiBidAltroIdVO.Institution;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.validators.bibliografica.ImportaLegamiBidAltroIdValidator;
import it.iccu.sbn.web.actionforms.gestionebibliografica.ImportaLegamiBidAltroIdForm;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.FileUtil;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

public class ImportaLegamiBidAltroIdAction extends NavigationBaseAction {

	private static String[] BOTTONIERA = new String[] { "button.prenota" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.prenota", "prenota");
		map.put("button.caricafile", "caricaFile");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form)
			throws Exception {
		ImportaLegamiBidAltroIdForm currentForm = (ImportaLegamiBidAltroIdForm) form;
		if (currentForm.isInitialized())
			return;

		ImportaLegamiBidAltroIdVO richiesta = currentForm.getRichiesta();
		UserVO user = Navigation.getInstance(request).getUtente();
		ParametriRichiestaElaborazioneDifferitaVO.fill(richiesta, CodiciAttivita.getIstance().IMPORTA_RELAZIONI_BID_ALTRO_ID, user);
		richiesta.setCodIstituzione(Institution.OCLC);
		currentForm.setPulsanti(BOTTONIERA);

		currentForm.setInitialized(true);
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		init(request, form);

		return mapping.getInputForward();
	}

	public ActionForward prenota(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ImportaLegamiBidAltroIdForm currentForm = (ImportaLegamiBidAltroIdForm) form;
		ImportaLegamiBidAltroIdVO richiesta = currentForm.getRichiesta();

		try {
			richiesta.validate(new ImportaLegamiBidAltroIdValidator());

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			String idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(navi.getUserTicket(), richiesta, null);
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));

		} catch (SbnBaseException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward caricaFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaLegamiBidAltroIdForm currentForm = (ImportaLegamiBidAltroIdForm) form;
		ImportaLegamiBidAltroIdVO richiesta = currentForm.getRichiesta();

		FormFile file = currentForm.getInput();
		if (file == null || file.getFileSize() < 1) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
			return mapping.getInputForward();
		}

		String output = FileUtil.getTemporaryFileName();
		FileUtil.uploadFile(file, output, null);
		richiesta.setInputFile(output);
		richiesta.setNomeInputFile(file.getFileName());

		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile", file.getFileName()));

		return mapping.getInputForward();
	}

}
