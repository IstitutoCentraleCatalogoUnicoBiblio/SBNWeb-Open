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
package it.iccu.sbn.web.actions.documentofisico.elaborazioniDifferite;

import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.validators.documentofisico.AcquisizioneUriCopiaDigitaleValidator;
import it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleForm;
import it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleForm.FolderType;
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

public class AcquisizioneUriCopiaDigitaleAction extends NavigationBaseAction {

	private static String[] BOTTONIERA = new String[] {
		"button.prenota" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.prenota", "prenota");
		map.put("button.caricafile", "caricaFile");
		map.put("button.test", "test");

		//folder
		map.put("folder.documentofisico.uri.generaUri", "genera");
		map.put("folder.documentofisico.uri.acquisisciUri", "acquisisci");
		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form)
			throws Exception {
		AcquisizioneUriCopiaDigitaleForm currentForm = (AcquisizioneUriCopiaDigitaleForm) form;
		if (currentForm.isInitialized())
			return;

		UserVO utente = Navigation.getInstance(request).getUtente();
		AcquisizioneUriCopiaDigitaleVO richiesta = currentForm.getRichiesta();
		AcquisizioneUriCopiaDigitaleVO.fill(richiesta, CodiciAttivita.getIstance().IMPORTA_URI_COPIA_DIGITALE, utente);
		currentForm.setPulsanti(BOTTONIERA);
		setFolder(form, FolderType.WITHOUT_URI);

		currentForm.setListaTipoDigit(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DIGITALIZZAZIONE));
		currentForm.setListaTeche(CodiciProvider.getCodici(CodiciType.CODICE_TECHE_DIGITALI));
		currentForm.setListaDispDaRemoto(CodiciProvider.getCodici(CodiciType.CODICE_DISP_ACCESSO_REMOTO));
		currentForm.setListaTipoFileInput(AcquisizioneUriCopiaDigitale.getListaTipoFileInput());
		currentForm.setListaTipoModelloURI(CodiciProvider.getCodici(CodiciType.CODICE_MODELLO_URI_COPIA_DIGITALE, true));

		richiesta.setTipoDigit("1");	//default a 'completo'
		//almaviva5_20131002 evolutive google2
		richiesta.setEliminaSpaziUri(true);

		super.init(request, form);

		currentForm.setInitialized(true);
	}

	private void setFolder(ActionForm form, FolderType folder) {
		AcquisizioneUriCopiaDigitaleForm currentForm = (AcquisizioneUriCopiaDigitaleForm) form;
		currentForm.setFolder(folder);
		AcquisizioneUriCopiaDigitaleVO richiesta = currentForm.getRichiesta();
		switch (folder) {
		case WITHOUT_URI:
			richiesta.setTipoInput("0");	//inventari
			//richiesta.setPrefisso("http://");
			break;
		case WITH_URI:
			richiesta.setTipoInput("2");	//inventari + uri
			richiesta.setPrefisso(null);
			richiesta.setModel(null);
			richiesta.setSuffisso(null);
			break;
		}
	}

	@Override
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

		AcquisizioneUriCopiaDigitaleForm currentForm = (AcquisizioneUriCopiaDigitaleForm) form;
		AcquisizioneUriCopiaDigitaleVO richiesta = currentForm.getRichiesta();
		String model = currentForm.getModel();
		TB_CODICI cod = CodiciProvider.cercaCodice(model,
				CodiciType.CODICE_MODELLO_URI_COPIA_DIGITALE,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		richiesta.setModel(cod != null ? cod.getCd_flg2() : null);

		try {
			richiesta.validate(new AcquisizioneUriCopiaDigitaleValidator());

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

		AcquisizioneUriCopiaDigitaleForm currentForm = (AcquisizioneUriCopiaDigitaleForm) form;
		AcquisizioneUriCopiaDigitaleVO richiesta = currentForm.getRichiesta();

		FormFile file = currentForm.getInput();
		if (file == null || file.getFileSize() < 1) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
			return mapping.getInputForward();
		}

		String output = FileUtil.getTemporaryFileName();
		FileUtil.uploadFile(file, output, null);
		richiesta.setInputFile(output);

		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile", file.getFileName()));

		return mapping.getInputForward();
	}

	public ActionForward test(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		String testUri = null;
		AcquisizioneUriCopiaDigitaleForm currentForm = (AcquisizioneUriCopiaDigitaleForm) form;
		AcquisizioneUriCopiaDigitaleVO richiesta = currentForm.getRichiesta();
		String model = currentForm.getModel();
		TB_CODICI cod = CodiciProvider.cercaCodice(model,
				CodiciType.CODICE_MODELLO_URI_COPIA_DIGITALE,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		richiesta.setModel(cod != null ? cod.getCd_flg2() : null);

		try {
			AcquisizioneUriCopiaDigitaleValidator validator = new AcquisizioneUriCopiaDigitaleValidator();
			testUri = validator.testUri(richiesta);
		} catch (ValidationException e) {
			testUri = LinkableTagUtils.findMessage(request, request.getLocale(), "error.documentofisico.campoNonValido");
		}

		currentForm.setTest(testUri);
		return mapping.getInputForward();
	}

	public ActionForward genera(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setFolder(form, FolderType.WITHOUT_URI);
		return mapping.getInputForward();
	}

	public ActionForward acquisisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setFolder(form, FolderType.WITH_URI);
		return mapping.getInputForward();
	}
}
