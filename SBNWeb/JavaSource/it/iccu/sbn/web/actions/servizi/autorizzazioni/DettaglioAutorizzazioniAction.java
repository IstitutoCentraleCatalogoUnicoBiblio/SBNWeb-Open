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
package it.iccu.sbn.web.actions.servizi.autorizzazioni;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AggiornaDirittiUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.autorizzazioni.DettaglioAutorizzazioniForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

public class DettaglioAutorizzazioniAction extends AutorizzazioniBaseAction {

	private static Logger log = Logger.getLogger(DettaglioAutorizzazioniAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		map.put("servizi.bottone.nuovo", "nuovo");
		map.put("servizi.bottone.cancella", "Cancella");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("servizi.bottone.inserisciServizio2", "insServizio");
		map.put("servizi.bottone.cancellaServizio2", "cancServizio");
		map.put("servizi.bottone.scorriAvanti","scorriAvanti");
		map.put("servizi.bottone.scorriIndietro","scorriIndietro");
		return map;
	}

	private void checkForm(HttpServletRequest request, ActionForm form) throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;

		ActionMessages errors = new ActionMessages();
		String data = "";
		data = currentForm.getAutAna().getCodAutorizzazione().trim().toUpperCase();
		currentForm.getAutAna().setCodAutorizzazione(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.erroreCodAutorizzazioneCampoObbligtorio"));
			this.saveErrors(request, errors);
			// error = true;
			throw new Exception();
		}
		//tck 2689
/*		data = currentForm.getAutAna().getAutomaticoPer().trim();
		currentForm.getAutAna().setAutomaticoPer(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.erroreAutomaticoXCampoObbligtorio"));
			this.saveErrors(request, errors);
			// error = true;
			throw new Exception();
		}
*/	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				log.debug("unspecified()");
				boolean parametriRicercaDisponibili=false;
				boolean anagrafeAutorizzazioneDisponibile=false;

				currentForm.setSessione(true);
				if (request.getAttribute("AutSel") != null) {
					currentForm.setSelectedAutorizzazioni((List) request.getAttribute("AutSel"));
					currentForm.setNumAutorizzazioni(currentForm.getSelectedAutorizzazioni().size());
					currentForm.setAutAna((AutorizzazioneVO) currentForm.getSelectedAutorizzazioni().get(0));
					if (currentForm.getPosizioneScorrimento()>0)
						currentForm.setAutAna((AutorizzazioneVO) currentForm.getSelectedAutorizzazioni().get(currentForm.getPosizioneScorrimento()));

					anagrafeAutorizzazioneDisponibile = true;
				}

				if (request.getAttribute(ServiziDelegate.DETTAGLIO_AUTORIZZAZIONE) != null) {
					currentForm.setAutAna((AutorizzazioneVO) request.getAttribute(ServiziDelegate.DETTAGLIO_AUTORIZZAZIONE));
					anagrafeAutorizzazioneDisponibile = true;
				}
				if (request.getAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI)!=null) {
					currentForm.setAutRicerca((RicercaAutorizzazioneVO) request
							.getAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI));
					parametriRicercaDisponibili = true;
				}
				else {
					if (request.getAttribute("RicercaAut")!=null) {
						currentForm.setAutRicerca((RicercaAutorizzazioneVO) request
								.getAttribute("RicercaAut"));
						parametriRicercaDisponibili = true;
					}
					//this.currentForm.setConferma(false);
				}

				if (parametriRicercaDisponibili) {
					currentForm.setBiblioteca(currentForm.getAutRicerca().getCodBib());
				}
				if (!anagrafeAutorizzazioneDisponibile && parametriRicercaDisponibili) {
					AutorizzazioneVO autAna = new AutorizzazioneVO();
					autAna.setNuovaAut(true);
					autAna.setCodPolo(currentForm.getAutRicerca().getCodPolo());
					autAna.setCodBiblioteca(currentForm.getAutRicerca().getCodBib());
					currentForm.setAutAna(autAna);
				}
				if (!anagrafeAutorizzazioneDisponibile) {
					currentForm.setRichiesta(DettaglioAutorizzazioniForm.NUOVA_AUT);
				}

				if (anagrafeAutorizzazioneDisponibile) {
					// clono l'old Autorizzazione appena caricato per constatare eventuali modifiche
					AutorizzazioneVO aut = new AutorizzazioneVO(currentForm.getAutAna().getCodBiblioteca(),
														currentForm.getAutAna().getCodAutorizzazione(),
														currentForm.getAutAna().getDesAutorizzazione(),
														currentForm.getAutAna().getAutomaticoPer());//this.currentForm.getAutAna();
					aut.setNuovaAut(currentForm.getAutAna().isNuovaAut());
					aut.setListaServizi(currentForm.getAutAna().getElencoServizi());
					currentForm.setAutAnaOLD((AutorizzazioneVO) aut.copy());
				}

				CaricamentoCombo combo = new CaricamentoCombo();
				currentForm.setElencoAutomaticoX(combo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROFESSIONI)));
			}

			//currentForm.setAutAnaOLD(currentForm.getAutAna());
			String listaServizi = (String) request.getAttribute("VengoDa");
			if (listaServizi !=null  && listaServizi.equals("ListaServizi")) {
				currentForm.getAutAna().setListaServizi(
						this.loadListaServizi((List) request.getAttribute("ServSelezionati")));
			}
			// si può omettere 22.02.10
			if (((listaServizi==null) || (listaServizi !=null  && !listaServizi.equals("ListaServizi"))) && currentForm.getAutAna()!=null && currentForm.getAutAna().getElencoServizi()!=null &&  currentForm.getAutAna().getElencoServizi().size() > 0)
			{
				for (int index = 0; index <currentForm.getAutAna().getElencoServizi().size(); index++) {
					ElementoSinteticaServizioVO ele=currentForm.getAutAna().getElencoServizi().get(index);
					ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getTipServizio()));
				}
				currentForm.getAutAnaOLD().setListaServizi(new ArrayList<ElementoSinteticaServizioVO>(currentForm.getAutAna().getElencoServizi()));
				//currentForm.setAutAnaOLD((AutorizzazioneVO)currentForm.getAutAna().clone());
			}
			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.findForward("cancella");//findForward("annulla");
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			this.checkForm(request, form);
			ActionMessages errors = new ActionMessages();

			if (!currentForm.getAutAna().isNuovaAut() && currentForm.getAutAnaOLD().equals(currentForm.getAutAna())) {
				//currentForm.getAutAna().clearAut();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
				//return mapping.findForward("ok");
			}
		if (currentForm.getAutAna()!=null &&  currentForm.getAutAna().getElencoServizi()!=null &&  currentForm.getAutAna().getElencoServizi().size()>0)
			{
				currentForm.setConferma(true);
				currentForm.setRichiesta(DettaglioAutorizzazioniForm.AGGIORNA_AUT);
				//ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this,
						mapping, request));
			}
			else
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.autorizzazioneSenzaDiritti"));
				this.saveErrors(request, errors);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		Navigation navi = Navigation.getInstance(request);
		UserVO utenteAbil = navi.getUtente();

		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().SERVIZI_AGGIORNA_DIRITTI_AUTORIZZAZIONE, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

		} catch (UtenteNotAuthorizedException e) {
			LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noaut"));
			resetToken(request);
			currentForm.setConferma(false);
			return mapping.getInputForward();//return null;
		}

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			currentForm.setConferma(false);
			boolean ret = true;
			AutorizzazioneVO aut = currentForm.getAutAna();
			if (currentForm.getRichiesta().equals(DettaglioAutorizzazioniForm.CANCELLA_AUT)) {
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				//Verifica esistenza utenti cui è assegnata l'autorizzazione che si vuole cancellare
				ret = factory.getGestioneServizi().esistonoUtentiCon(navi.getUserTicket(), aut.getCodPolo(),
														aut.getCodBiblioteca(), aut.getCodAutorizzazione());
				if (ret) {
					//Impossibile cancellare autorizzazione perchè assegnata almeno a un utente
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.autorizzazione.utilizzata"));
					this.resetToken(request);
					currentForm.setAutAna(null);
					return mapping.findForward("cancella");
				}

				ret = factory.getGestioneServizi().cancelAutorizzazione(navi.getUserTicket(), aut);
				if (ret) {
					// mando msg di operazione ok
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
					this.resetToken(request);
					currentForm.setAutAna(null);
					return mapping.findForward("cancella");
				}
			}
			if (currentForm.getRichiesta().equals(DettaglioAutorizzazioniForm.AGGIORNA_AUT)) {
				int numAtomaticoX = 0;
				//AnagAutorizzazioniVO autorizzazioneOLD=null;
				if (!ValidazioneDati.isFilled(aut.getAutomaticoPer()))
					aut.setAutomaticoPer(" ");
				else {
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

					numAtomaticoX = factory.getGestioneServizi().getAutomaticoX(navi.getUserTicket(),
							currentForm.getAutRicerca().getCodPolo(),
							currentForm.getAutRicerca().getCodBib(),
							aut.getCodAutorizzazione(),
							aut.getAutomaticoPer().charAt(0));
				}

				if (numAtomaticoX > 0 && !aut.getAutomaticoPer().equals(currentForm.getAutAnaOLD().getAutomaticoPer())) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codAutomaticoXAssegnato"));
					return mapping.getInputForward();
				}
				else
				{
					//if (!currentForm.getAutAna().isNuovaAut()) {
						// passo la lista servizi per aggiornare la base dati
						if (!this.aggiornaDb(navi.getUtente().getFirmaUtente(), form, request)) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
							return mapping.getInputForward();
						}
						// gestione batch
						if (!aut.isNuovaAut()&&  aut!=null && currentForm.getAutAnaOLD()!=null && aut.getListaServizi()!=null && currentForm.getAutAnaOLD().getListaServizi()!=null &&  !currentForm.getAutAnaOLD().getListaServizi().equals(aut.getListaServizi())) {
							//lancio del batch
//							 new gestione batch inizio
							AggiornaDirittiUtenteVO richiesta = new AggiornaDirittiUtenteVO();
							richiesta.setCodPolo(aut.getCodPolo());
							richiesta.setCodBib(aut.getCodBiblioteca());
							richiesta.setUser(navi.getUtente().getUserId());
							richiesta.setCodAttivita(CodiciAttivita.getIstance().SERVIZI_AGGIORNA_DIRITTI_AUTORIZZAZIONE);

//							richiesta.setParametri(inputForStampeService);
//							richiesta.setTipoOrdinamento(ordinamFile);
							String ticket=navi.getUserTicket();
							richiesta.setTicket(ticket);
							richiesta.setListaDiritti(aut.getListaServizi());
							richiesta.setAutorizzazione(aut);

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
								LinkableTagUtils.addError(request, e);
								return mapping.getInputForward();
							} catch (Exception e) {
								log.error("", e);
							}

							if (s == null) {
								LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotBatchKO"));
								resetToken(request);
								return mapping.getInputForward();
							}

							LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));
							resetToken(request);
							return mapping.getInputForward();
						}
						else
						{
							// mando msg di operazione ok
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

						    String chiamante = "";//navi.getActionCaller();
							if (chiamante.toUpperCase().indexOf("LISTA") > -1) {
								ServiziDelegate delegate = ServiziDelegate.getInstance(request);
								Navigation navbar = navi;

								currentForm.getAutRicerca().setCodBib(aut.getCodBiblioteca());
								currentForm.getAutRicerca().setCodPolo(aut.getCodPolo());
								currentForm.getAutRicerca().setTicket(navi.getUserTicket());
								//currentForm.getRicAut().setNumeroElementiBlocco(10);
								DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(
										currentForm.getAutRicerca());

								if (blocco1.getTotRighe() > 0) {
									request.setAttribute(ServiziDelegate.LISTA_AUTORIZZAZIONI, blocco1);
									request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI,
														currentForm.getAutRicerca());
									request.setAttribute("RicercaAut", currentForm.getAutRicerca());
									request.setAttribute("PathForm", mapping.getPath());

									// resetto campi
//									this.resetToken(request);
//									this.currentForm.getAutAna().getListaServizi().clear();
//									this.currentForm.getAutAna().getElencoServizi().clear();
//									this.currentForm.setAutAna(null);
									return mapping.findForward("okLista");
								} else {
									LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.ListaVuota"));
									resetToken(request);
									return mapping.getInputForward();
								}
							} else {
								// resetto campi
//								this.resetToken(request);
//								this.currentForm.getAutAna().getListaServizi().clear();
//								this.currentForm.getAutAna().getElencoServizi().clear();
//								this.currentForm.setAutAna(null);
								return mapping.findForward("okRicerca");
							}
							/**
							 * Navigation navi = Navigation.getInstance(request); String
							 * chiamante = navi.getActionCaller(); if
							 * (chiamante.toUpperCase().indexOf("LISTA") > -1) { return
							 * navi .goBack(mapping.findForward("okLista"), true); //
							 * return mapping.findForward("okLista"); } else { return
							 * mapping.findForward("okRicerca"); }
							 */
						//}

						}
				}
			}
			currentForm.setRichiesta(null);
		} catch (Exception e) {
			log.error("", e);
			if (e.getCause()!=null && e.getCause().getMessage()!=null &&  e.getCause().getMessage().indexOf("SRV_AUTORIZZAZIONE_ESISTENTE")>0)
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.documenti.autorizzazioneEsistente"));
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.generico"));

			this.resetToken(request);
			currentForm.setConferma(false);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public boolean aggiornaDb(String utente, ActionForm form, HttpServletRequest request) throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

//		List servAsso = (List) this.currentForm.getAutAna().getElencoServizi();
		// vedo : se è nuovo inserire autorizzazione
		// se è vecchio update di autorizzazione cambiare solo data_agge
		// descrizione
		boolean result = true;
		AutorizzazioneVO aut = currentForm.getAutAna();
		if (aut.isNuovaAut()) {
			aut.setFlCanc("N");
			aut.setUteIns(utente);
			aut.setUteVar(utente);
			aut.setTsIns(DaoManager.now());
			result = factory.getGestioneServizi().insertAutorizzazione(Navigation.getInstance(request).getUserTicket(), aut);
		} else {
			aut.setUteVar(utente);
			// verifica del cambio lista diritti associati per lancio del batch
			try {
				if (currentForm.getAutAnaOLD().getListaServizi().equals(aut.getListaServizi()))
					result = factory.getGestioneServizi().updateAutorizzazione(Navigation.getInstance(request).getUserTicket(), aut);
			} catch (ApplicationException e) {
				LinkableTagUtils.addError(request, e);
				result = false;
			}
		}

		return result;
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.setConferma(false);
		currentForm.setRichiesta(null);
		return mapping.getInputForward();
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.getAutAna().clearAut();
		currentForm.getAutAna().setNuovaAut(AutorizzazioneVO.NEW);
		currentForm.setRichiesta(DettaglioAutorizzazioniForm.NUOVA_AUT);
		return mapping.getInputForward();
	}

	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.setConferma(true);
		currentForm.setRichiesta(DettaglioAutorizzazioniForm.CANCELLA_AUT);
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.confermaOperazione"));
		this.saveErrors(request, errors);
		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping,
				request));
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		resetToken(request);
		//this.currentForm.getAutAna().clearAut();

		if (currentForm.getRichiesta()!=null &&
			currentForm.getRichiesta().equalsIgnoreCase(DettaglioAutorizzazioniForm.NUOVA_AUT))
		{
			return mapping.findForward("ricerca");
		}
		else {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			//Navigation navi = Navigation.getInstance(request);


			DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(
					currentForm.getAutRicerca());
			if (blocco1.getTotRighe() > 0) {
				request.setAttribute(ServiziDelegate.LISTA_AUTORIZZAZIONI, blocco1);
				request.setAttribute(
						ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI,
						currentForm.getAutRicerca());
				request.setAttribute("RicercaAut", currentForm.getAutRicerca());
				return mapping.findForward("annulla");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.ListaVuota"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		}
	}

	public ActionForward cancServizio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		// controllo se la lista c'è (!null) ed ha piu di 0 elementi
		List<ElementoSinteticaServizioVO> elencoServizi = currentForm.getAutAna().getElencoServizi();
		if (ValidazioneDati.isFilled(elencoServizi) ) {
			// controllo se i selezionati per la cancellazione sono almeno uno
			if (this.contaSelezionati(elencoServizi))
				// controllo che siano da cancellare in base dati
				this.cancellaServizio(elencoServizi);
			else {
				// messaggio di errore.
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.codiceNessunaSelezione"));
				this.saveErrors(request, errors);
			}

		} else {
				// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNoElemCancellabile"));
			this.saveErrors(request, errors);
		}
		this.resetCampoC(elencoServizi);
		return mapping.getInputForward();
	}


	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getSelectedAutorizzazioni().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setAutAna((AutorizzazioneVO) currentForm.getSelectedAutorizzazioni().get(attualePosizione));
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			// si può omettere 22.02.10
/*			if (currentForm.getAutAna()!=null && currentForm.getAutAna().getElencoServizi()!=null &&  currentForm.getAutAna().getElencoServizi().size() > 0)
			{
				for (int index = 0; index <currentForm.getAutAna().getElencoServizi().size(); index++) {
					ElementoSinteticaServizioVO ele=(ElementoSinteticaServizioVO) currentForm.getAutAna().getElencoServizi().get(index);
					ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getTipServizio()));
				}
				currentForm.setAutAnaOLD((AutorizzazioneVO)currentForm.getAutAna().clone());
			}
*/			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;

		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			int dimensione=currentForm.getSelectedAutorizzazioni().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setAutAna((AutorizzazioneVO) currentForm.getSelectedAutorizzazioni().get(attualePosizione));
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			// si può omettere 22.02.10
/*			if (currentForm.getAutAna()!=null && currentForm.getAutAna().getElencoServizi()!=null &&  currentForm.getAutAna().getElencoServizi().size() > 0)
			{
				for (int index = 0; index <currentForm.getAutAna().getElencoServizi().size(); index++) {
					ElementoSinteticaServizioVO ele=(ElementoSinteticaServizioVO) currentForm.getAutAna().getElencoServizi().get(index);
					ele.setDescrizioneTipoServizio(this.cercaDescrServizio(ele.getTipServizio()));
				}
			}
*/			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward insServizio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioAutorizzazioniForm currentForm = (DettaglioAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		// resetto lo stato eventualnmente impostato da check fatti erroneamente
		this.resetCancServizio(currentForm.getAutAna().getElencoServizi());
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		Navigation navi = Navigation.getInstance(request);
//		DescrittoreBloccoVO blocco1 = delegate.caricaListaAnagServizi(request,
//															navi.getUserTicket(), this.currentForm.getAutAna()
//															.getElencoServizi(), this.currentForm.getAutAna()
//															.getCodBiblioteca(), 10);

		DescrittoreBloccoVO blocco1 = delegate.caricaListaServiziBiblioteca(
						currentForm.getAutAna().getElencoServizi(),
						currentForm.getAutAna().getCodPolo(),
						currentForm.getAutAna().getCodBiblioteca(),
						navi.getUserTicket(), Integer.MAX_VALUE);

		if (blocco1.getTotRighe() > 0) {
			request.setAttribute("ServAssociati", currentForm.getAutAna()
					.getElencoServizi());
			request.setAttribute("CodiceBiblioteca", currentForm
					.getAutAna().getCodBiblioteca());
			request.setAttribute("BlocoAnagraficaServizi", blocco1);
			request.setAttribute(ServiziDelegate.DETTAGLIO_AUTORIZZAZIONE, currentForm.getAutAna());
			request.setAttribute("PathForm", mapping.getPath());
			return mapping.findForward("insServizio");
		} else {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.ListaVuota"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}
	}

	private boolean cancellaServizio(List elencoServizi) throws Exception {
		boolean del = false;
		Iterator<ElementoSinteticaServizioVO> i = elencoServizi.iterator();

		while (i.hasNext()) {
			ElementoSinteticaServizioVO serBib = i.next();
			if (serBib.getStato() == ElementoSinteticaServizioVO.ELI)
				i.remove();

			if (serBib.getStato() == ElementoSinteticaServizioVO.DEL)
				del = true;

		}
		return del;
	}

	private boolean contaSelezionati(List elencoServizi) throws Exception {
		for (Object o : elencoServizi) {
			ElementoSinteticaServizioVO serBib = (ElementoSinteticaServizioVO) o;
			if (ValidazioneDati.in(serBib.getStato(),
					ElementoSinteticaServizioVO.DEL,
					ElementoSinteticaServizioVO.ELI) )
				return true;

		}
		return false;
	}

	private void resetCampoC(List elencoServizi) throws Exception {
		for (Object o : elencoServizi) {
			ElementoSinteticaServizioVO serBib = (ElementoSinteticaServizioVO) o;
			if (ValidazioneDati.equals(serBib.getCancella(), "C"))
				serBib.setCancella("");

		}
	}

	private void resetCancServizio(List elencoServizi) throws Exception {
		for (Object o : elencoServizi) {
			ElementoSinteticaServizioVO serBib = (ElementoSinteticaServizioVO) o;
			if (ValidazioneDati.equals(serBib.getCancella(), "C")) {
				serBib.setCancella("");
				if (serBib.getStato() == ElementoSinteticaServizioVO.DEL) {
					serBib.setStato(ElementoSinteticaServizioVO.OLD);
				}
				if (serBib.getStato() == ElementoSinteticaServizioVO.ELI) {
					serBib.setStato(ElementoSinteticaServizioVO.NEW);
				}
			}
		}
	}

	private List loadListaServizi(List serviziSel) throws Exception {
		List listamat = new ArrayList();
		if (serviziSel != null && serviziSel.size() != 0) {
			ElementoSinteticaServizioVO serBib=null;
			for (int index = 0; index < serviziSel.size(); index++) {
				serBib = (ElementoSinteticaServizioVO) serviziSel.get(index);
				listamat.add(serBib);
			}
		}
		return listamat;
	}

	private String cercaDescrServizio(String cod) throws Exception {
		return CodiciProvider.cercaDescrizioneCodice(cod,
				CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN);
	}
}
