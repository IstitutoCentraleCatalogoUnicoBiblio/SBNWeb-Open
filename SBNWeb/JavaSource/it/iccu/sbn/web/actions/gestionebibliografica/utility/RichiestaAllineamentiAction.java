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
//	SBNWeb - Rifacimento ClientServer
//		ACTION
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actions.gestionebibliografica.utility;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO.FileXMLTipoOperazione;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioElenchiListeConfrontoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.FusioneMassivaBatchVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionebibliografica.RichiestaAllineamentiForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;

public class RichiestaAllineamentiAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(RichiestaAllineamentiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.confermaAllineamenti", "confermaAllineamenti");
		map.put("button.richiestaBatchAllineamenti", "richiestaBatchAllineamenti");
		map.put("button.allineam.idLista.Doc", "allineaDoc");
		map.put("button.allineam.idLista.Au", "allineaAu");
		map.put("button.allineam.idLista.Ma", "allineaMa");
		map.put("button.allineam.idLista.Tu", "allineaTu");
		map.put("button.allineam.idLista.Um", "allineaUm");
		map.put("button.richiestaAllineaRepertori", "richiestaAllineaRepertori");

		map.put("button.caricaFileIdCatturaMassiva","caricaFileIdCatturaMassiva");
		map.put("button.richiestaCatturaMassiva", "richiestaCatturaMassiva");

		map.put("button.caricaFileIdFusioneMassiva","caricaFileIdFusioneMassiva");
		map.put("button.richiestaFusioneMassiva", "richiestaFusioneMassiva");

		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;
		currentForm.setStatoAvanzamento("0");

		if (currentForm.getTipoMatSelez() == null) {
			currentForm.setTipoMatSelez("*");
		}
		if (currentForm.getIdFileAllineamenti() == null) {
			currentForm.setIdFileAllineamenti("");
		}
		if (currentForm.getDataAllineaDa() == null) {
			currentForm.setDataAllineaDa("");
		}
		if (currentForm.getDataAllineaA() == null) {
			currentForm.setDataAllineaA("");
		}


		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		// Modifica almaviva2 15.01.2010 - gestione nuova della richiesta allineamenti
		if (request.getParameter(Constants.CODICE_ATTIVITA) != null) {
			currentForm.setTipoProspettazione(request.getParameter(Constants.CODICE_ATTIVITA));

			if (currentForm.getTipoProspettazione().equals("IA000")){
				Navigation.getInstance(request).setTesto("Allineamento Base Dati");
			} else if (currentForm.getTipoProspettazione().equals("IA004")){
				Navigation.getInstance(request).setTesto("Cattura Massiva");
			} else if (currentForm.getTipoProspettazione().equals("IA005")){
				Navigation.getInstance(request).setTesto("Allineamento Repertori");
			} else if (currentForm.getTipoProspettazione().equals("IF010")){
				Navigation.getInstance(request).setTesto("Fusione Massiva");
				// Impostazione di inizializzazione jsp
				caricaComboGeneriche(request, currentForm);





			}
		}

		return mapping.getInputForward();
	}

	public ActionForward richiestaBatchAllineamenti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;


		// Inizio verifica dell'Impostazione sul range di date e se presente sulla loro correttezza

		if (!currentForm.getDataAllineaDa().equals("")) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			  long longData1 = 0;
			  long longData2 = 0;

			if (currentForm.getDataAllineaA().equals("")) {
				currentForm.setDataAllineaA(currentForm.getDataAllineaDa());
			}
			try {
				  Date data1 = simpleDateFormat.parse(currentForm.getDataAllineaDa());
				  Date data2 = simpleDateFormat.parse(currentForm.getDataAllineaA());
				  longData1 = data1.getTime();
				  longData2 = data2.getTime();
			  } catch (ParseException e) {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.ins024"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
			  }

			  if ((longData2 - longData1) < 0) {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.ric002"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
			  }
		}
		// Fine verifica dell'Impostazione sul range di date e se presente sulla loro correttezza


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utente = Navigation.getInstance(request).getUtente();

		// INIZIO PRENOTAZIONE ALLINEAMENTO
		AllineaVO allineaVo = new AllineaVO();
		allineaVo.setCodPolo(utente.getCodPolo());
		allineaVo.setCodBib(utente.getCodBib());
		allineaVo.setUser(utente.getUserId());
		allineaVo.setCodAttivita(CodiciAttivita.getIstance().ALLINEAMENTI_1032);

		String basePath=this.servlet.getServletContext().getRealPath(File.separator);
		allineaVo.setBasePath(basePath);
		String downloadPath = StampeUtil.getBatchFilesPath();
		log.info("download path: " + downloadPath);
		String downloadLinkPath = "/";
		allineaVo.setDownloadPath(downloadPath);
		allineaVo.setDownloadLinkPath(downloadLinkPath);
		allineaVo.setTicket(Navigation.getInstance(request).getUserTicket());
		allineaVo.setTipoMaterialeDaAllineare(currentForm.getTipoMatSelez());
		allineaVo.setDataAllineaDa(currentForm.getDataAllineaDa());
		allineaVo.setDataAllineaA(currentForm.getDataAllineaA());
		allineaVo.setIdFileAllineamenti(currentForm.getIdFileAllineamenti());

		// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
		// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
		// del flag di avvenuto allineamento.
		if (currentForm.getIdFileLocaleAllineamenti() != null && !currentForm.getIdFileLocaleAllineamenti().equals("")) {
			allineaVo.setAllineamentoDaFileLocale(true);
			allineaVo.setIdFileLocaleAllineamenti(currentForm.getIdFileLocaleAllineamenti());
			//se il tipo operazione sui file xml provenienti dall'indice è SALVA si cambia il tipo di elaborazione differita
			if (currentForm.getFileXmlTipoOperazione() == FileXMLTipoOperazione.SALVA) {
				allineaVo.setCodAttivita(CodiciAttivita.getIstance().ALLINEAMENTI_SALVA_XML_INDICE);
			}
		} else {
			allineaVo.setAllineamentoDaFileLocale(false);
		}

		//String s = factory.getElaborazioniDifferite().allinea(allineaVo);
		String idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), allineaVo, null);

		if (idMessaggio == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.prenotAllineaFallita"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.prenotAllineaOk", idMessaggio.toString()));
		this.saveErrors(request, errors);
		resetToken(request);
		return mapping.getInputForward();


	}



	public ActionForward confermaAllineamenti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AllineaVO allineamentoPoloIndiceVOReturn = factory.getGestioneBibliografica().richiediListaAllineamenti(
				currentForm.getAreaDatiAllineamentoPoloIndiceVO(), Navigation.getInstance(request).getUserTicket());

		if (allineamentoPoloIndiceVOReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (allineamentoPoloIndiceVOReturn.getCodErr().equals("9999") || allineamentoPoloIndiceVOReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo" ,allineamentoPoloIndiceVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!allineamentoPoloIndiceVOReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica."
							+ allineamentoPoloIndiceVOReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		currentForm.setAreaDatiAllineamentoPoloIndiceVO(allineamentoPoloIndiceVOReturn);
		currentForm.setStatoAvanzamento("1");

		String diagnDoc = "";
		String diagnAu = "";
		String diagnMa = "";
		String diagnTu = "";
		String diagnUm = "";
		if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareDocumento() == 999999999) {
			currentForm.getAreaDatiAllineamentoPoloIndiceVO().setNumListaDaAllineareDocumento(0);
			diagnDoc = "Allineamenti Documenti inferiori a 50 - Operazione Correttamente eseguita";
		} else if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareDocumento() == 0) {
			diagnDoc = "Allineamenti Documenti assenti";
		} else {
			diagnDoc = "Allineamenti Documenti superiori a 50 - Effettuare la richiesta di allineamento";
		}

		if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareAutore() == 999999999) {
			currentForm.getAreaDatiAllineamentoPoloIndiceVO().setNumListaDaAllineareAutore(0);
			diagnAu = "Allineamenti Autori inferiori a 50 - Operazione Correttamente eseguita";
		} else if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareAutore() == 0) {
			diagnAu = "Allineamenti Autori assenti";
		} else {
			diagnAu = "Allineamenti Autori superiori a 50 - Effettuare la richiesta di allineamento";
		}

		if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareMarca() == 999999999) {
			currentForm.getAreaDatiAllineamentoPoloIndiceVO().setNumListaDaAllineareMarca(0);
			diagnMa = "Allineamenti Marche inferiori a 50 - Operazione Correttamente eseguita";
		} else if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareMarca() == 0) {
			diagnMa = "Allineamenti Marche assenti";
		} else {
			diagnMa = "Allineamenti Marche superiori a 50 - Effettuare la richiesta di allineamento";
		}

		if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareTitUniforme() == 999999999) {
			currentForm.getAreaDatiAllineamentoPoloIndiceVO().setNumListaDaAllineareTitUniforme(0);
			diagnTu = "Allineamenti Titoli Uniformi inferiori a 50 - Operazione Correttamente eseguita";
		} else if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareTitUniforme() == 0) {
			diagnTu = "Allineamenti Titoli Uniformi assenti";
		} else {
			diagnTu = "Allineamenti Titoli Uniformi superiori a 50 - Effettuare la richiesta di allineamento";
		}

		if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareTitUniformeMus() == 999999999) {
			currentForm.getAreaDatiAllineamentoPoloIndiceVO().setNumListaDaAllineareTitUniformeMus(0);
			diagnUm = "Allineamenti Titoli Uniformi Musicali inferiori a 50 - Operazione Correttamente eseguita";
		} else if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareTitUniformeMus() == 0) {
			diagnUm = "Allineamenti Titoli Uniformi Musicali assenti";
		} else {
			diagnUm = "Allineamenti Titoli Uniformi Musicali superiori a 50 - Effettuare la richiesta di allineamento";
		}
		diagnTu = diagnTu + "<BR>" + diagnUm;
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.statoAllineamenti" ,diagnDoc, diagnAu, diagnMa, diagnTu));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}

	public ActionForward allineaDoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareDocumento() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.nessunAllineamento"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		currentForm.getAreaDatiAllineamentoPoloIndiceVO().setTipoAuthorityDaAllineare("");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AllineaVO allineamentoPoloIndiceVOReturn = factory.getGestioneBibliografica().allineaBaseLocale(
				currentForm.getAreaDatiAllineamentoPoloIndiceVO(), Navigation.getInstance(request).getUserTicket());

		if (allineamentoPoloIndiceVOReturn.getCodErr().equals("9999")
				|| allineamentoPoloIndiceVOReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					allineamentoPoloIndiceVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}



	public ActionForward allineaAu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		if (currentForm.getAreaDatiAllineamentoPoloIndiceVO().getNumListaDaAllineareAutore() == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.nessunAllineamento"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		currentForm.getAreaDatiAllineamentoPoloIndiceVO().setTipoAuthorityDaAllineare("AU");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AllineaVO allineamentoPoloIndiceVOReturn = factory.getGestioneBibliografica().allineaBaseLocale(
				currentForm.getAreaDatiAllineamentoPoloIndiceVO(), Navigation.getInstance(request).getUserTicket());

		if (allineamentoPoloIndiceVOReturn.getCodErr().equals("9999")
				|| allineamentoPoloIndiceVOReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					allineamentoPoloIndiceVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}

	public ActionForward allineaMa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		currentForm.getAreaDatiAllineamentoPoloIndiceVO().setTipoAuthorityDaAllineare("MA");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AllineaVO allineamentoPoloIndiceVOReturn = factory.getGestioneBibliografica().allineaBaseLocale(
				currentForm.getAreaDatiAllineamentoPoloIndiceVO(), Navigation.getInstance(request).getUserTicket());

		if (allineamentoPoloIndiceVOReturn.getCodErr().equals("9999")
				|| allineamentoPoloIndiceVOReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					allineamentoPoloIndiceVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}

	public ActionForward allineaTu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		currentForm.getAreaDatiAllineamentoPoloIndiceVO().setTipoAuthorityDaAllineare("TU");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AllineaVO allineamentoPoloIndiceVOReturn = factory.getGestioneBibliografica().allineaBaseLocale(
				currentForm.getAreaDatiAllineamentoPoloIndiceVO(), Navigation.getInstance(request).getUserTicket());

		if (allineamentoPoloIndiceVOReturn.getCodErr().equals("9999")
				|| allineamentoPoloIndiceVOReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					allineamentoPoloIndiceVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}

	public ActionForward Um(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		currentForm.getAreaDatiAllineamentoPoloIndiceVO().setTipoAuthorityDaAllineare("UM");

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AllineaVO allineamentoPoloIndiceVOReturn = factory.getGestioneBibliografica().allineaBaseLocale(
				currentForm.getAreaDatiAllineamentoPoloIndiceVO(), Navigation.getInstance(request).getUserTicket());

		if (allineamentoPoloIndiceVOReturn.getCodErr().equals("9999")
				|| allineamentoPoloIndiceVOReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					allineamentoPoloIndiceVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);
		return mapping.getInputForward();

	}

	public ActionForward richiestaAllineaRepertori(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AllineaVO allineamentoPoloIndiceVOReturn = factory.getGestioneBibliografica().allineamentoRepertoriDaIndice(Navigation.getInstance(request).getUserTicket());

		if (allineamentoPoloIndiceVOReturn == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		if (allineamentoPoloIndiceVOReturn.getCodErr().equals("9999") || allineamentoPoloIndiceVOReturn.getTestoProtocollo() != null ) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo" ,allineamentoPoloIndiceVOReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else if (!allineamentoPoloIndiceVOReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica."
							+ allineamentoPoloIndiceVOReturn.getCodErr()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.operOk"));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}


	public ActionForward caricaFileIdCatturaMassiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		byte[] buf;
		try {
			buf = currentForm.getUploadImmagine().getFileData();

			if (buf == null || buf.length == 0) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
				return mapping.getInputForward();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
			List<String> listaBidDaFile = new ArrayList<String>();

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!ValidazioneDati.leggiXID(line)) {
					log.warn("caricaFileIdCatturaMassiva() - Riga file input non valida o già presente: '" + line + "'");
					continue;
				}
				listaBidDaFile.add(line);
			}

			if (!ValidazioneDati.isFilled(listaBidDaFile)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
				return mapping.getInputForward();
			}

			currentForm.setListaBidDaFile(listaBidDaFile);

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile", currentForm.getUploadImmagine().getFileName()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.marImgNotValid"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward richiestaCatturaMassiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;
		if (!ValidazioneDati.isFilled(currentForm.getListaBidDaFile()) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.fileIdentificativiMancante"));
			return mapping.getInputForward();
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		UserVO utente = Navigation.getInstance(request).getUtente();

		// INIZIO PRENOTAZIONE CATTURA MASSIVA

		// Nuova gestione batch
		CatturaMassivaBatchVO catturaMassivaBatchVO = new CatturaMassivaBatchVO();
		catturaMassivaBatchVO.setCodPolo(utente.getCodPolo());
		catturaMassivaBatchVO.setCodBib(utente.getCodBib());
		catturaMassivaBatchVO.setUser(utente.getUserId());
		catturaMassivaBatchVO.setCodAttivita(CodiciAttivita.getIstance().CATTURA_MASSIVA);

		String basePath=this.servlet.getServletContext().getRealPath(File.separator);
		catturaMassivaBatchVO.setBasePath(basePath);
		String downloadPath = StampeUtil.getBatchFilesPath();
		log.info("download path: " + downloadPath);
		String downloadLinkPath = "/"; //File.separator+"sbn"+File.separator+"download"+File.separator;
		catturaMassivaBatchVO.setDownloadPath(downloadPath);
		catturaMassivaBatchVO.setDownloadLinkPath(downloadLinkPath);
		catturaMassivaBatchVO.setTicket(Navigation.getInstance(request).getUserTicket());
		catturaMassivaBatchVO.setListaBidDaCatturare(currentForm.getListaBidDaFile());
//		String s = factory.getElaborazioniDifferite().catturaMassivaBatch(catturaMassivaBatchVO);

		String idMessaggio = "";
		try {
			idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), catturaMassivaBatchVO, null);
		} catch (ApplicationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getErrorCode().getErrorMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		if (idMessaggio == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.prenotCattMassivaFallita"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.prenotCattMassivaOk", idMessaggio.toString()));
		this.saveErrors(request, errors);
		resetToken(request);
		return mapping.getInputForward();

	}

	public ActionForward caricaFileIdFusioneMassiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;

		byte[] buf;
		try {
			FormFile file = currentForm.getUploadImmagine2();
			buf = file.getFileData();

			if (buf == null || buf.length == 0) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
				return mapping.getInputForward();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
			List<String> listaBidDaFile = new ArrayList<String>();

			String line = null;
			while ( (line = reader.readLine() ) != null ) {

				//check coppie bid
				line = line.trim();
				String[] bids = line.split("\\|");
				if (ValidazioneDati.size(bids) != 2
					|| !ValidazioneDati.leggiXID(bids[0])
                    || !ValidazioneDati.leggiXID(bids[1])
                    || ValidazioneDati.equals(bids[0], bids[1])
                    || listaBidDaFile.contains(line) ) {
					log.warn("caricaFileIdFusioneMassiva() - Riga file input non valida o già presente: '" + line + "'");
					continue;
				}

				listaBidDaFile.add(line);
			}

			if (!ValidazioneDati.isFilled(listaBidDaFile)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setListaBidDaFile(listaBidDaFile);

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile", file.getFileName()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.marImgNotValid"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward richiestaFusioneMassiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RichiestaAllineamentiForm currentForm = (RichiestaAllineamentiForm) form;
		if (!ValidazioneDati.isFilled(currentForm.getListaBidDaFile()) && !ValidazioneDati.isFilled(currentForm.getDataListaSelez())) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.fileIdentificativiMancante"));
			return mapping.getInputForward();
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		UserVO utente = Navigation.getInstance(request).getUtente();

		// INIZIO PRENOTAZIONE CATTURA MASSIVA

		// Nuova gestione batch
		FusioneMassivaBatchVO fusioneMassivaBatchVO = new FusioneMassivaBatchVO();
		fusioneMassivaBatchVO.setCodPolo(utente.getCodPolo());
		fusioneMassivaBatchVO.setCodBib(utente.getCodBib());
		fusioneMassivaBatchVO.setUser(utente.getUserId());

		fusioneMassivaBatchVO.setCodAttivita(CodiciAttivita.getIstance().FUSIONE_MASSIVA);

		String basePath=this.servlet.getServletContext().getRealPath(File.separator);
		fusioneMassivaBatchVO.setBasePath(basePath);
		String downloadPath = StampeUtil.getBatchFilesPath();
		log.info("download path: " + downloadPath);
		String downloadLinkPath = "/"; //File.separator+"sbn"+File.separator+"download"+File.separator;
		fusioneMassivaBatchVO.setDownloadPath(downloadPath);
		fusioneMassivaBatchVO.setDownloadLinkPath(downloadLinkPath);
		fusioneMassivaBatchVO.setTicket(Navigation.getInstance(request).getUserTicket());
		if (currentForm.getListaBidDaFile() != null) {
			fusioneMassivaBatchVO.setListaCoppieBidDaFondere(currentForm.getListaBidDaFile());
		}
		if (currentForm.getDataListaSelez() != null) {
			fusioneMassivaBatchVO.setListaDiConfrontoSelez(currentForm.getDataListaSelez());
		}
		String idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), fusioneMassivaBatchVO, null);

		if (idMessaggio == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.prenotFusiMassivaFallita"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneBibliografica.prenotFusiMassivaOk", idMessaggio.toString()));
		this.saveErrors(request, errors);
		resetToken(request);
		return mapping.getInputForward();

	}


	// Evolutiva per trattamento delle fusioni batch per liste di confronto 10.11.2011

	public void caricaComboGeneriche(HttpServletRequest request, RichiestaAllineamentiForm currentForm) throws Exception {

		AreaDatiPassaggioElenchiListeConfrontoVO elenchiListeConfrontoVO = new AreaDatiPassaggioElenchiListeConfrontoVO();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		AreaDatiPassaggioElenchiListeConfrontoVO elenchiListeConfrontoReturnVO = factory.getGestioneBibliografica().getElenchiListeConfronto(elenchiListeConfrontoVO, Navigation.getInstance(request).getUserTicket());


		// Impostazione di inizializzazione jsp area generalizzata
		ComboCodDescVO generico = new ComboCodDescVO();
		generico.setCodice("");
		generico.setDescrizione("");
		currentForm.addListaDataLista(generico);

		for (int i = 0; i < elenchiListeConfrontoReturnVO.getListaDataLista().size(); i++) {
			generico = new ComboCodDescVO();
			generico.setCodice(String.valueOf(elenchiListeConfrontoReturnVO.getListaIdLista().get(i)));
			// Modifica almaviva2 BUG 5041 (collaudo) - Nella maschera di Fusione massiva l'etichetta 'Data produzione lista'
			// andrebbe sostituita con 'Identificativo lista' in analogia con l'etichetta della maschera delle Liste di confronto.
//			generico.setDescrizione((String) elenchiListeConfrontoReturnVO.getListaDataLista().get(i));
			generico.setDescrizione(String.valueOf(elenchiListeConfrontoReturnVO.getListaIdLista().get(i))
					+ " - "
					+ (String) elenchiListeConfrontoReturnVO.getListaDataLista().get(i));

			currentForm.addListaDataLista(generico);
		}

	}

	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		if (idCheck.equals("allineamentoDaFileLocale")) {
			try {
				String path = CommonConfiguration.getProperty(Configuration.SBNWEB_BATCH_ALLINEAMENTO_DA_LOCALE);
				return ValidazioneDati.isFilled(path);
			} catch (Exception e) {	}
		}

		if (idCheck.equals("ALLINEAMENTI_SALVA_XML_INDICE")) {
			try {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().ALLINEAMENTI_SALVA_XML_INDICE);
				return true;

			} catch (Exception e) {
				return false;
			}
		}

		return false;
	}

}
