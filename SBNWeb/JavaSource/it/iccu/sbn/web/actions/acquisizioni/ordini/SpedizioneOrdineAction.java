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
package it.iccu.sbn.web.actions.acquisizioni.ordini;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuonoOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.custom.acquisizioni.ordini.OrdineCarrelloSpedizioneDecorator;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.OrdineBaseForm;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.SpedizioneOrdineForm;
import it.iccu.sbn.web.actions.acquisizioni.util.OrdiniWebUtil;
import it.iccu.sbn.web.actions.common.SbnDownloadAction;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.acquisizioni.AcquisizioniDelegate;
import it.iccu.sbn.web.util.FileUtil;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class SpedizioneOrdineAction extends NavigationBaseAction implements SbnAttivitaChecker {

	private static String[] BOTTONIERA = new String[] {
		"ricerca.button.conferma",
		"button.periodici.annulla" };

	private static String[] BOTTONIERA_CONFERMA = new String[] {
		"button.periodici.si",
		"button.periodici.no",
		"button.periodici.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		//almaviva5_20121120 evolutive google
		map.put("ricerca.button.conferma", "conferma");
		map.put("button.periodici.si", "si");
		map.put("button.periodici.no", "no");
		map.put("button.periodici.annulla", "annulla");

		return map;
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form)
			throws Exception {

		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;
		if (currentForm.isInitialized())
			return;

		currentForm.setListaStatoOrdine(CodiciProvider.getCodici(CodiciType.CODICE_STATO_ORDINE));

		OrdiniVO ordine = (OrdiniVO) request.getAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE);
		currentForm.setOrdine(ordine);
		currentForm.setPulsanti(BOTTONIERA);
		currentForm.setTipoFormato(TipoStampa.PDF.name());
		currentForm.setInitialized(true);
	}

	@Override
	protected void loadForm(HttpServletRequest request, ActionForm form)
			throws Exception {

		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;
		OrdiniVO ordine = currentForm.getOrdine();

		OrdineCarrelloSpedizioneVO ocs = ordine.getOrdineCarrelloSpedizione();
		if (ocs != null)
			return;

		String firmaUtente = Navigation.getInstance(request).getUtente().getFirmaUtente();
		ocs = OrdiniWebUtil.preparaOrdineCarrelloSpedizione(ordine, firmaUtente);

		AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);

		ConfigurazioneORDVO config = delegate.getConfigurazioneOrdini();
		String cd_bib_google = config.getCd_bib_google();
		if (ordine.isGoogle() && !ValidazioneDati.isFilled(cd_bib_google))
			throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);

		ocs.setCartName(OrdiniWebUtil.formattaCartName(cd_bib_google, null) );

		ordine.setOrdineCarrelloSpedizione(new OrdineCarrelloSpedizioneDecorator(ocs));
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		try {
			this.init(request, form);
			this.loadDefault(request, form);
			this.loadForm(request, form);

		} catch (SbnBaseException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
			NavigationForward back = Navigation.getInstance(request).goBack(true);
			back.setRedirect(true);
			return back;
		}

		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;

		try {
			OrdiniVO ordine = currentForm.getOrdine();
			if (ordine.getCreationTime().after(currentForm.getCreationTime())) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreModifica"));
				return mapping.getInputForward();
			}

			AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);

			OrdiniVO modified = delegate.spedisciOrdineRilegatura(ordine);

			if (modified == null)
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreModifica"));

			currentForm.setOrdine(modified);
			//aggiornamento dettagli ordine su mappa precedente
			NavigationElement prev = Navigation.getInstance(request).getCache().getPreviousElement();
			if (prev != null) {
				ActionForm fprev = prev.getForm();
				if (fprev != null && fprev instanceof OrdineBaseForm)
					((OrdineBaseForm) fprev).setDatiOrdine(modified);
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.spedizioneOK"));

			return effettuaStampa(mapping, currentForm, request, response);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;
		currentForm.setConferma(false);
		currentForm.setPulsanti(BOTTONIERA);

		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;

		try {
			OrdiniVO ordine = currentForm.getOrdine();
			ordine.setStampato(true);
			AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
			delegate.modificaOrdine(ordine);

		} catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		currentForm.setConferma(true);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;
		OrdiniVO ordine = currentForm.getOrdine();
		if (ordine.isSpedito()) {
			request.setAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE, ordine);
			request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE, ordine.getRigheInventariRilegatura());
			return Navigation.getInstance(request).goBack();
		}

		return Navigation.getInstance(request).goBack(true);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;
		OrdiniVO ordine = currentForm.getOrdine();
		if (ValidazioneDati.equals(idCheck, "ricerca.button.conferma") )
			return !ordine.isStampato() && !ordine.isSpedito();

		if (ValidazioneDati.equals(idCheck, NavigazioneAcquisizioni.STAMPA) ) {
			boolean check = ordine.isStampato() && ordine.isSpedito();
			return check;
		}

		if (ValidazioneDati.equals(idCheck, NavigazioneAcquisizioni.GOOGLE) )
			return ordine.isGoogle();


		return true;
	}

	@SuppressWarnings("rawtypes")
	private ActionForward effettuaStampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SpedizioneOrdineForm currentForm = (SpedizioneOrdineForm) form;

		//almaviva5_20121123 evolutive google
		StampaBuonoOrdineVO richiesta = new StampaBuonoOrdineVO();
		OrdiniVO ordine = currentForm.getOrdine();
		richiesta.setCodBibl(ordine.getCodBibl());
		richiesta.setCodPolo(ordine.getCodPolo());
		richiesta.setListaOrdiniDaStampare(ValidazioneDati.asSingletonList(ordine));
		richiesta.setListaBuoniOrdineDaStampare(null);
		richiesta.setTipoStampa(StampaType.STAMPA_ORDINE_RILEGATURA);

		// base path per i percorsi fisici
		String basePath = this.servlet.getServletContext().getRealPath(File.separator);
		// file e path del template jrxml
		String fileJrxml = "default_ordine_" + StampaType.STAMPA_ORDINE_RILEGATURA.name() + ".jrxml";
		String pathJrxml = basePath + File.separator + "jrxml" + File.separator + fileJrxml;
		// cartella da utilizzare come storage dei file generati con i report
		String pathDownload = StampeUtil.getBatchFilesPath();
		String tipoFormato = currentForm.getTipoFormato();
		// codici polo e biblioteca per dove servono
		UserVO utente = Navigation.getInstance(request).getUtente();

		// preparo StampaOnLineVO e vi inserisco i dati comuni a tutte le stampe
		StampaOnLineVO stampaOnLineVO = new StampaOnLineVO();
		stampaOnLineVO.setDownload(pathDownload);
		stampaOnLineVO.setDownloadLinkPath("/");
		stampaOnLineVO.setTipoStampa(tipoFormato);

		stampaOnLineVO.setTicket(utente.getTicket());
		stampaOnLineVO.setCodPolo(utente.getCodPolo());
		stampaOnLineVO.setCodBib(utente.getCodBib());
		stampaOnLineVO.setUser(utente.getFirmaUtente());

		OutputStampaVo output = null;
		try {
			stampaOnLineVO.setTemplate(pathJrxml);
			stampaOnLineVO.setDatiStampa(ValidazioneDati.asSingletonList(richiesta));
			stampaOnLineVO.setTipoOperazione(StampaType.STAMPA_ORDINE_RILEGATURA.name());

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			output = factory.getStampeOnline().stampaOnlineBuoniOrdine(stampaOnLineVO);
			if (!ValidazioneDati.equals(output.getStato(), ConstantsJMS.STATO_OK)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.finestampa"));
				return mapping.getInputForward();
			}

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("errors.finestampa"));
			return mapping.getInputForward();
		}

		StringBuilder filename = new StringBuilder();
		filename.append(ordine.getChiaveOrdine().replaceAll("\\s+", "_"));
		filename.append('.');
		filename.append(tipoFormato.toLowerCase());

		SbnDownloadAction.downloadFile(request, filename.toString(), FileUtil.getData(output.getOutput()) );

		return mapping.getInputForward();
	}

}
