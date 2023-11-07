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
package it.iccu.sbn.web.actions.common;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriBatchSoggettoBaseVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriAllineamentoBibliotecheILLVO;
import it.iccu.sbn.extension.sms.SMSResult;
import it.iccu.sbn.servizi.pagination.QueryPage;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.sms.SMSUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.common.MigrazioneForm;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziILLDelegate;
import it.iccu.sbn.web.util.FileUtil;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;

public final class MigrazioneAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(MigrazioneAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.mig.rinnovi", "rinnovi");
		map.put("button.mig.soggetti", "soggetti");
		map.put("button.mig.pagination", "paginazione");
		map.put("button.mig.segnature", "segnature");
		//almaviva5_20120228
		map.put("button.mig.segnature.docnosbn", "segnatureDocNoSbn");
		map.put("button.caricafile", "caricaFile");
		// Intervento almaviva2 Novembre 2013 INTERROGAZIONE MASSIVA - per gestione lista bid locali da confrontare con oggetti di Indice
		map.put("button.mig.carFileInterrMassiva", "carFileInterrMassiva");
		map.put("button.mig.prenBatchInterrMassiva", "prenBatchInterrMassiva");

		map.put("button.pad.execute", "reload");

		map.put("button.mig.force.batch.start", "forceBatch");
		map.put("button.mig.force.batch.stop", "forceBatchStop");
		map.put("button.mig.test.mail", "testMail");
		map.put("button.mig.test.sms", "testSMS");

		map.put("button.mig.salva.file.allinea", "salvaFileAllinea");

		map.put("button.mig.allinea.ill", "allineaILL");

		map.put("button.mig.invia.ill.xml", "invioXmlILL");
		map.put("button.mig.chiavi.possessore", "chiaviPossessore");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward rinnovi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		try {
			DomainEJBFactory.getInstance().getAcquisizioni().migrazioneCStoSBNWEBcateneRinnoviBis();
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.rinnovicatene.connection"));
			return (mapping.getInputForward());
		}
		LinkableTagUtils.addError(request, new ActionMessage("error.rinnovicatene.ok"));

		return mapping.getInputForward();

	}

	public ActionForward soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MigrazioneForm currentForm = (MigrazioneForm) form;
		ParametriBatchSoggettoBaseVO parametri = currentForm.getParametri();
		UserVO utente = Navigation.getInstance(request).getUtente();
		parametri.setCodPolo(utente.getCodPolo());
		parametri.setCodBib(utente.getCodBib());
		parametri.setUser(utente.getUserId());
		parametri.setTicket(utente.getTicket());
		parametri.setCodAttivita(CodiciAttivita.getIstance().CANCELLAZIONE_SOGGETTI_INUTILIZZATI);
		parametri.setTipoOperazione(CommandType.SEM_AGGIORNAMENTO_MASSIVO_SOGGETTI);
		//parametri.setCodSoggettario("FIR");

		return prenota(mapping, form, request, response, parametri);
	}

	public ActionForward paginazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			QueryPage page = FactoryEJBDelegate.getInstance().getSistema().testPaginazione();
			log.debug(page);

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.rinnovicatene.connection"));
			return (mapping.getInputForward());
		}

		return mapping.getInputForward();
	}

	public ActionForward segnature(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			DomainEJBFactory.getInstance().getServizi().fixRangeSegnature(Navigation.getInstance(request).getUserTicket());

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.rinnovicatene.connection"));
			return (mapping.getInputForward());
		}

		return mapping.getInputForward();
	}

	public ActionForward segnatureDocNoSbn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			//almaviva5_20120228 migrazione TO0
			DomainEJBFactory.getInstance().getServiziBMT().fixSegnatureDocNoSbn(Navigation.getInstance(request).getUserTicket());
			LinkableTagUtils.addError(request, new ActionMessage("error.rinnovicatene.ok"));

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.rinnovicatene.connection"));
			return (mapping.getInputForward());
		}

		return mapping.getInputForward();
	}

	public ActionForward caricaFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		MigrazioneForm currentForm = (MigrazioneForm) form;

		// gestione upload immagine marca
		FormFile input = currentForm.getFileSoggetti();
		if (input == null)
			return mapping.getInputForward();

		try {
			String fileName = FileUtil.getTemporaryFileName();
			log.debug("Caricamento file temporaneo: " + fileName);
			FileUtil.uploadFile(input, fileName, null);
			LinkableTagUtils.addError(request, new ActionMessage("errors.importa.ricConfermaAcquisizioneFile", input.getFileName()));
			currentForm.getParametri().setInputFile(fileName);

		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("errors.importa.erroreAcquisizioneFile"));
		}

		return mapping.getInputForward();
	}

    protected ActionForward prenota(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response,
            ParametriRichiestaElaborazioneDifferitaVO parametri) throws Exception {

    	try {
			UserVO utente = Navigation.getInstance(request).getUtente();
			parametri.validate();

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), parametri, null);
			if (idBatch == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
				resetToken(request);
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return mapping.getInputForward();

    }


	// Intervento almaviva2 Novembre 2013 INTERROGAZIONE MASSIVA - per gestione lista bid locali da confrontare con oggetti di Indice
	public ActionForward carFileInterrMassiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MigrazioneForm currentForm = (MigrazioneForm) form;

		try {
			BufferedReader reader;
			try {
				InputStream in = currentForm.getUploadImmagine().getInputStream();
				reader = new BufferedReader(new InputStreamReader(in));
			} catch (Exception e) {
				log.error("", e);
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
				return mapping.getInputForward();
			}

			List<String> listaBidDaFile = new ArrayList<String>();

			String line = null;
			while ( (line = reader.readLine() ) != null ) {
				line = line.trim();
				if (!ValidazioneDati.leggiXID(line)) {
					log.warn("caricaFileIdCatalLocale() - Riga file input non valida o già presente: '" + line + "'");
					continue;
				}
				listaBidDaFile.add(line);
			}

			if (!ValidazioneDati.isFilled(listaBidDaFile))
				throw new Exception("nessun bid caricato");


			currentForm.setListaBidDaFile(listaBidDaFile);

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile", currentForm.getUploadImmagine().getFileName()));
			return mapping.getInputForward();

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
			return mapping.getInputForward();
		}
	}

	public ActionForward prenBatchInterrMassiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		MigrazioneForm currentForm = (MigrazioneForm) form;

		UserVO utente = Navigation.getInstance(request).getUtente();
		CatturaMassivaBatchVO catturaMassivaBatchVO = new CatturaMassivaBatchVO();
		catturaMassivaBatchVO.setCodPolo(utente.getCodPolo());
		catturaMassivaBatchVO.setCodBib(utente.getCodBib());
		catturaMassivaBatchVO.setUser(utente.getUserId());
		catturaMassivaBatchVO.setCodAttivita(CodiciAttivita.getIstance().CATTURA_MASSIVA);
		catturaMassivaBatchVO.setCallInterrogPerCreazListe(true);

		String basePath=this.servlet.getServletContext().getRealPath(File.separator);
		catturaMassivaBatchVO.setBasePath(basePath);
		String downloadPath = StampeUtil.getBatchFilesPath();
		log.info("download path: " + downloadPath);
		String downloadLinkPath = "/";
		catturaMassivaBatchVO.setDownloadPath(downloadPath);
		catturaMassivaBatchVO.setDownloadLinkPath(downloadLinkPath);
		catturaMassivaBatchVO.setTicket(Navigation.getInstance(request).getUserTicket());
		catturaMassivaBatchVO.setListaBidDaCatturare(currentForm.getListaBidDaFile());

		String idMessaggio = "";
		try {
			idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), catturaMassivaBatchVO, null);
		} catch (ApplicationException e) {

			LinkableTagUtils.addError(request, new ActionMessage(e.getErrorCode().getErrorMessage()));

			return mapping.getInputForward();
		}

		if (idMessaggio == null) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.prenotCattMassivaFallita"));

			resetToken(request);
			return mapping.getInputForward();
		}


		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.prenotCattMassivaOk", idMessaggio.toString()));

		resetToken(request);
		return mapping.getInputForward();

	}

	public ActionForward reload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		CommandInvokeVO cmd = CommandInvokeVO.build(navi.getUserTicket(), CommandType.AMM_RELOAD);

		FactoryEJBDelegate.getInstance().getGestioneServizi().invoke(cmd);

		LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
		return mapping.getInputForward();
	}

	public ActionForward forceBatch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MigrazioneForm currentForm = (MigrazioneForm) form;
		try {
			DomainEJBFactory.getInstance().getPolo().forceBatchStart(currentForm.getIdBatch());
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}

	public ActionForward forceBatchStop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MigrazioneForm currentForm = (MigrazioneForm) form;
		try {
			DomainEJBFactory.getInstance().getPolo().forceBatchStop(currentForm.getIdBatchStop());
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}	

	public ActionForward testMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MigrazioneForm currentForm = (MigrazioneForm) form;

		try {
			MailUtil.testMailServer(currentForm.getEmail());

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}

	public ActionForward testSMS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//MigrazioneForm currentForm = (MigrazioneForm) form;

		try {
			SMSResult result = SMSUtil.send("123456789", "987654321", "sms test", false);

			LinkableTagUtils.addError(request, new ActionMessage(result.toString()));
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}

	public ActionForward salvaFileAllinea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MigrazioneForm currentForm = (MigrazioneForm) form;

		try {
			String idBatchAllinea = currentForm.getIdBatchAllinea();
			if (!ValidazioneDati.isFilled(idBatchAllinea))
				return mapping.getInputForward();

			AllineaVO allinea = new AllineaVO();
			allinea.setIdFileAllineamenti(idBatchAllinea);
			DomainEJBFactory.getInstance().getSrvBibliografica()
					.scaricaFileAllineamentoBaseLocale(allinea,
							Navigation.getInstance(request).getUserTicket());

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}

	public ActionForward allineaILL(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			ParametriAllineamentoBibliotecheILLVO richiesta = new ParametriAllineamentoBibliotecheILLVO();
			richiesta.setTicket(Navigation.getInstance(request).getUserTicket());
			richiesta.setAllineaBiblioteche(true);
			richiesta.setAllineaRichieste(true);
			DomainEJBFactory.getInstance().getServiziBMT().allineaServerILL(richiesta, null);

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}

	public ActionForward invioXmlILL(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MigrazioneForm currentForm = (MigrazioneForm) form;
		try {
			String isil = currentForm.getIsil();
			String ill_xml = currentForm.getIll_xml();
			if (!ValidazioneDati.isFilled(isil) || !ValidazioneDati.isFilled(ill_xml)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.detail", "impostare isil e xml"));
				return mapping.getInputForward();
			}

			ServiziILLDelegate.getInstance(request).executeHandler(ill_xml, isil);

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));

		} catch (SbnBaseException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);

		} catch (JAXBException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getLinkedException().getMessage()));

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}

	public ActionForward chiaviPossessore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			DomainEJBFactory.getInstance().getPossessoriBMT().ricalcolaChiavi();
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.operazioneCorrettamenteEseguita"));
		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(e.getMessage()));
		}

		return mapping.getInputForward();
	}

}
