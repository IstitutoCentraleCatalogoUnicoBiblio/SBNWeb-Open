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
package it.iccu.sbn.web.actions.servizi.elaborazioniDifferite;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ArchiviazioneMovLocVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.elaborazioniDifferite.ArchiviazioneMovLocForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ArchiviazioneMovLocAction extends ServiziBaseAction {
	private static Logger log = Logger.getLogger(ArchiviazioneMovLocAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.conferma","ok");
		map.put("button.indietro","indietro");
		map.put("ricerca.label.bibliolist", "biblioCerca");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ArchiviazioneMovLocForm currentForm = (ArchiviazioneMovLocForm) form;
		Navigation navi = Navigation.getInstance(request);

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = navi.getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().SERVIZI_ARCHIVIAZIONE_MOVLOC, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
		}


		if (navi.isFromBar())	return mapping.getInputForward();

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				//Settaggio biblioteche
				currentForm.setCodBib(utente.getCodBib());
				currentForm.setCodPolo(utente.getCodPolo());
				currentForm.setDescrBib(utente.getBiblioteca());
				BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null && currentForm.getCodBib()!=null )
				{
					currentForm.setCodBib(bibScelta.getCod_bib());
					currentForm.setDescrBib(bibScelta.getNom_biblioteca());
				}
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}
		return mapping.getInputForward();

	}


	public ActionForward ok(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ArchiviazioneMovLocForm currentForm = (ArchiviazioneMovLocForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();

		ActionMessages errors = new ActionMessages();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().SERVIZI_ARCHIVIAZIONE_MOVLOC, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();//return null;
		}

		try {

			if (!ValidazioneDati.strIsNull(currentForm.getDataSvecchiamento()))
			{
				String campo = currentForm.getDataSvecchiamento();
				int codRitorno = -1;
				try {
					codRitorno = ValidazioneDati.validaDataPassata(campo);
					if (codRitorno != ValidazioneDati.DATA_OK)
					{
						errors.add("generico", new ActionMessage("errors.stampaUtenti.dataFormatoErrore"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				} catch (Exception e) {
					switch (codRitorno) {
					case ValidazioneDati.DATA_ERRATA:
						errors.add("generico", new ActionMessage("errors.stampaUtenti.dataFormatoErrore"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					case ValidazioneDati.DATA_MAGGIORE:
						errors.add("generico", new ActionMessage("errors.stampaUtenti.dataMaggioreErrore"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					case ValidazioneDati.DATA_PASSATA_ERRATA:
						errors.add("generico", new ActionMessage("errors.stampaUtenti.dataVuotaErrore"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					default:
						errors.add("generico", new ActionMessage("errors.stampaUtenti.dataFormatoErrore"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
			}
			else
			{
				errors.add("generico", new ActionMessage("errors.stampaUtenti.indicaData"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			// validazione
			if (currentForm.getCodBib()!=null && currentForm.getCodBib().length()!=0)
			{
				if (currentForm.getCodBib().length()>3)
				{
					errors.add("generico", new ActionMessage("errors.stampaUtenti.erroreCodBiblEccedente"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

			ArchiviazioneMovLocVO richiesta = new ArchiviazioneMovLocVO();

			richiesta.setCodPolo(utente.getCodPolo());
			richiesta.setCodBib(utente.getCodBib());
			richiesta.setDataInizio(currentForm.getDataInizio());
			richiesta.setDataSvecchiamento(currentForm.getDataSvecchiamento());
			richiesta.setUser(utente.getUserId());
			richiesta.setCodAttivita(CodiciAttivita.getIstance().SERVIZI_ARCHIVIAZIONE_MOVLOC);
			richiesta.setTicket(utente.getTicket());

			//almaviva5_20130424 segnalazione ICCU: bib. affiliata
			richiesta.setCodBibArchiviazione(currentForm.getCodBib());

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			richiesta.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			log.info("download path: " + downloadPath);
			String downloadLinkPath = "/";
			richiesta.setDownloadPath(downloadPath);
			richiesta.setDownloadLinkPath(downloadLinkPath);


			String s =  null;
			try {
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				s = factory.getPolo().prenotaElaborazioneDifferita(richiesta, null);
			} catch (ApplicationException e) {
				if (e.getErrorCode().getErrorMessage().equals("USER_NOT_AUTHORIZED"))
				{
					errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			} catch (Exception e) {
				log.error("", e);
			}


			if (s == null) {
				errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotBatchKO"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

			errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();

		}catch (Exception e){
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),
	utente.getCodBib(), CodiciAttivita.getIstance().SERVIZI_ARCHIVIAZIONE_MOVLOC, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("STAMPA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().SERVIZI_ARCHIVIAZIONE_MOVLOC, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				// 04.12.08 log.error("", e);
				log.error(e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		return false;
	}




	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);

	}

}



