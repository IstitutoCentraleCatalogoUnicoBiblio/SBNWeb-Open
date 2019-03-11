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
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.OperazioneSuOrdineForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.acquisizioni.AcquisizioniDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;



public class OperazioneSuOrdineAction extends  SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(OperazioneSuOrdineAction.class);

	private static String ANNULLA = "ordine.label.annullamento";
	private static String CHIUDI = "ordine.label.chiusura";
	private static String RINNOVA = "ordine.label.rinnovo";
	private static String DUPLICA = "ordine.label.duplicazione";
	private static String RIAPRI = "ordine.label.riapertura";



	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro", "indietro");
		map.put("button.ok", "salva");
		map.put("acquisizioni.bottone.si", "si");
		map.put("acquisizioni.bottone.no", "no");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {

			//request.getSession().setAttribute("provieneDaOperazioneOrdini",(String) "si");
			OperazioneSuOrdineForm currentForm = (OperazioneSuOrdineForm ) form;
			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			// caricamento radio
			this.loadTipiOperazione( currentForm, request);
			// per ora gestione della modifica dello stato dell'ordine solo da esamina

			// se è stata selezionata l'operazione differita
			List<Integer> listaIDOrdini= (List<Integer>) request.getAttribute("listaIDOrdini");
			if (listaIDOrdini!=null && listaIDOrdini.size()>0)
			{
				//request.getSession().removeAttribute("listaIDOrdini");
				currentForm.setListaIDOrdini(listaIDOrdini);
			}
			// se è stata selezionata l'operazione differita
			OperazOrdVO passaAOperazOrdini=(OperazOrdVO) request.getAttribute("passaAOperazOrdini");
			if (passaAOperazOrdini!=null )
			{
				if (passaAOperazOrdini.getListaIDOrdini()!=null && passaAOperazOrdini.getListaIDOrdini().size()>0)
				{
					currentForm.setListaIDOrdini(passaAOperazOrdini.getListaIDOrdini());
				}
				currentForm.setCodPolo(passaAOperazOrdini.getCodPolo());
				currentForm.setCodBibl(passaAOperazOrdini.getCodBibl());
			}



			if (request.getSession().getAttribute("chiamante").equals("/acquisizioni/ordini/esaminaOrdineMod"))
			{
				OrdiniVO ordine= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
				if (ordine!=null)
				{
					if (ordine.getStatoOrdine().equals("N"))
					{
						currentForm.setSelectedTipoOperazione(ANNULLA);
					}
					else if (ordine.getStatoOrdine().equals("C"))
					{
						currentForm.setSelectedTipoOperazione(CHIUDI);

					}
					else if (ordine.getStatoOrdine().equals("R"))
					{
						currentForm.setSelectedTipoOperazione(RINNOVA);

					}
					else
					{
						currentForm.setSelectedTipoOperazione("");
					}
				}
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			request.getSession().removeAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
			ActionForward action = new ActionForward();
			action.setName("RITORNA");
			action.setPath(request.getSession().getAttribute("chiamante")+".do");
			return action;
			//return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OperazioneSuOrdineForm currentForm = (OperazioneSuOrdineForm ) form;
		try {
			String selected = currentForm.getSelectedTipoOperazione();
			if (!ValidazioneDati.isFilled(selected) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricerca"));
				return mapping.getInputForward();
			}

				// tck 2115 su eliminazione conferma su operazione duplicazione
				OrdiniVO ord= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
				// dovrà essere gestita la funzione quando si proviene da sintetica (operaz. massiva)

				if (ord!=null && request.getSession().getAttribute("chiamante").equals("/acquisizioni/ordini/esaminaOrdineMod"))
				{
				if (selected.equals(DUPLICA) )	{
						this.passaCriteri( currentForm, request, mapping,  "D"); // imposta il crea con i valori cercati
					if (ord.getTipoOrdine()!=null)	{
							if (ord.getTipoOrdine().equals("A"))
							{
								return mapping.findForward("nuovoA");
							}
							else if (ord.getTipoOrdine().equals("L"))
							{
								return mapping.findForward("nuovoL");
							}
							else if (ord.getTipoOrdine().equals("D"))
							{
								return mapping.findForward("nuovoD");
							}
							else if (ord.getTipoOrdine().equals("C"))
							{
								return mapping.findForward("nuovoC");
							}
							else if (ord.getTipoOrdine().equals("V"))
							{
								return mapping.findForward("nuovoV");
							}
							else if (ord.getTipoOrdine().equals("R"))
							{
								return mapping.findForward("nuovoR");
							}
							else
							{
								return mapping.findForward("nuovoA");
							}
						}
						else
						{
							return mapping.findForward("nuovoA");
						}

					}
				if (selected.equals(RINNOVA))
					{
						// controllo solo per ordini continuativi
						//eleOrdineAttivo.setStatoOrdine("");
						if (ord.isContinuativo()) // && !eleOrdineAttivo.isRinnovato()
						{

							if (ord.getStatoOrdine().equals("C") && ord.isRinnovato() )
							{
								// come da specifiche
							//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.rinnovoOrdiniChiusi"));

								return mapping.getInputForward();
							}
							else
							{
								// aggiorna lo stato dell'ordine originario a rinnovato
								ord.setRinnovato(true);
								if (!this.modificaOrdine(ord)) {
									ord.setRinnovato(false);
								LinkableTagUtils.addError(request, new ActionMessage(
											"errors.acquisizioni.erroreModifica"));

									return mapping.getInputForward();
								}
								// prepara l'ordine da rinnovare
								this.passaCriteri( currentForm, request, mapping,  "R"); // imposta il crea con i valori cercati

								if (ord.getTipoOrdine()!=null)
								{
									if (ord.getTipoOrdine().equals("A"))
									{
										return mapping.findForward("nuovoA");
									}
									else if (ord.getTipoOrdine().equals("L"))
									{
										return mapping.findForward("nuovoL");
									}
									else if (ord.getTipoOrdine().equals("D"))
									{
										return mapping.findForward("nuovoD");
									}
									else if (ord.getTipoOrdine().equals("C"))
									{
										return mapping.findForward("nuovoC");
									}
									else if (ord.getTipoOrdine().equals("V"))
									{
										return mapping.findForward("nuovoV");
									}
									else if (ord.getTipoOrdine().equals("R"))
									{
										return mapping.findForward("nuovoR");
									}
									else
									{
										return mapping.findForward("nuovoA");
									}
								}

							}

						}
						else
						{
						//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.rinnovoOrdini"));

							return mapping.getInputForward();
						}

					}
				}
				currentForm.setConferma(true);
				if (ord!=null) // selezione singola
				{
				if (selected!=null && selected.trim().length()>0)
					{
					if (selected.equals(ANNULLA))
						{
			    		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazioneAnnullamento"));
						}
					else if (selected.equals(RIAPRI))
						{
			    		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazioneRiapri"));
						}
					else if (selected.equals(CHIUDI))
						{
						//bug #2412: CONTROLLO ESISTENZA INVENTARI ALL'ATTO DEL SALVA
						AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
						int cntInv = delegate.countInventariOrdine(ord);
							//CASO: ORDINE DA CHIUDERE SOLO SE ESISTONO INVENTARI
						if (!ord.getTipoOrdine().equals("R")  && cntInv == 0 ) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.noChiusuraInventari"));
							currentForm.setConferma(false); // si interrompe la richiesta di conferma operazione
				        } else
							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazioneChiusura"));

					          }
							else
							  {
			    		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));
							  }
						}
					else
					{
		    		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));
					}

				}
				else // selezione multipla
				{
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazioneMultipla"));

				}

	    		return mapping.getInputForward();

		}	catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordinierroreValidazioneDaOperazione"));
			return mapping.getInputForward();

		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		OperazioneSuOrdineForm currentForm = (OperazioneSuOrdineForm ) form;
		try {
			currentForm.setConferma(false);
			// per ora gestione della modifica dello stato dell'ordine solo da esamina
			String chiamante = (String) request.getSession().getAttribute("chiamante");
			String selected = ValidazioneDati.trimOrEmpty(currentForm.getSelectedTipoOperazione());
			if (chiamante.equals("/acquisizioni/ordini/esaminaOrdineMod"))
			{
				OrdiniVO ordine = (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
				if (ordine!=null)
				{
					String oldDataChiusura="";
					String oldStato = ordine.getStatoOrdine();

					String tipoOrdine = ordine.getTipoOrdine();
					if (selected.equals(ANNULLA))
					{
						ordine.setStatoOrdine("N");
					}
					else if (selected.equals(RIAPRI))
					{
						if (ordine.getStatoOrdine().equals("C"))
						{
							ordine.setStatoOrdine("A");
							ordine.setRiapertura(true);
							if (ordine.getDataChiusura()!=null)
							{
								oldDataChiusura=ordine.getDataChiusura();
							}
							ordine.setDataChiusura(null);

						}
						else
						{

							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordineNonChiuso"));

							return mapping.getInputForward();
						}


					}

					else if (selected.equals(CHIUDI))
					{
						ordine.setStatoOrdine("C");
					}

					else if (selected.equals(DUPLICA))
					{
						//eleOrdineAttivo.setStatoOrdine("");
						this.passaCriteri( currentForm, request, mapping, "D"); // imposta il crea con i valori cercati
						if (tipoOrdine!=null)
						{
							if (tipoOrdine.equals("A"))
							{
								return mapping.findForward("nuovoA");
							}
							else if (tipoOrdine.equals("L"))
							{
								return mapping.findForward("nuovoL");
							}
							else if (tipoOrdine.equals("D"))
							{
								return mapping.findForward("nuovoD");
							}
							else if (tipoOrdine.equals("C"))
							{
								return mapping.findForward("nuovoC");
							}
							else if (tipoOrdine.equals("V"))
							{
								return mapping.findForward("nuovoV");
							}
							else if (tipoOrdine.equals("R"))
							{
								return mapping.findForward("nuovoR");
							}
							else
							{
								return mapping.findForward("nuovoA");
							}
						}
						else
						{
							return mapping.findForward("nuovoA");
						}

					}
					else if (selected.equals(RINNOVA))
					{

						// controllo solo per ordini continuativi
						if (ordine.isContinuativo())
						{
							this.passaCriteri(currentForm, request, mapping,  "R"); // imposta il crea con i valori cercati
							//imposta i valori per il rinnovato
							if (tipoOrdine!=null)
							{
								if (tipoOrdine.equals("A"))
								{
									return mapping.findForward("nuovoA");
								}
								else if (tipoOrdine.equals("L"))
								{
									return mapping.findForward("nuovoL");
								}
								else if (tipoOrdine.equals("D"))
								{
									return mapping.findForward("nuovoD");
								}
								else if (tipoOrdine.equals("C"))
								{
									return mapping.findForward("nuovoC");
								}
								else if (tipoOrdine.equals("V"))
								{
									return mapping.findForward("nuovoV");
								}
								else if (tipoOrdine.equals("R"))
								{
									return mapping.findForward("nuovoR");
								}
								else
								{
									return mapping.findForward("nuovoA");
								}
							}
							else
							{
								return mapping.findForward("nuovoA");
							}

						}
					}
					else
					{
						ordine.setStatoOrdine("");
					}
					// test su chiusura ed esistenza inventari (ad esclusione della riapertura)
					if (!selected.equals(RIAPRI) && ValidazioneDati.in(ordine.getStatoOrdine(), "C", "N") )	{
						// caso di ordine che è possibile chiudere
						// immettere le condizioni di configurazioni della fase di chiusura
						ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
						configurazioneCriteri.setCodBibl(ordine.getCodBibl());
						configurazioneCriteri.setCodPolo(ordine.getCodPolo());
						ConfigurazioneORDVO conf = this.loadConfigurazioneORD(configurazioneCriteri);
						//almaviva5_20130513 #4762 adegua prezzo ordine, default a NO
						String adeguaPrezzo = (conf != null) ? ValidazioneDati.coalesce(conf.getAllineamento(), "N") : "N";

						if (!tipoOrdine.equals("R") && ordine.getStatoOrdine().equals("C") ) {
							if (!currentForm.isAdeguamentoPrezzo() && adeguaPrezzo.equals("R"))	{
								// deve essere emesso messaggio di richiesta con si, no sull'adeguamento e comunque chiuso
								currentForm.setAdeguamentoPrezzo(true);
								// emissione messaggio di conferma
								currentForm.setConferma(true);
								LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazioneAdeguamentoPrezzi"));
					    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					    		return mapping.getInputForward();
							}
							currentForm.setAdeguamentoPrezzo(false); // reset
				        }

						//AGGIUNGERE I CALCOLI DI AGGIORNAMENTO PREZZO CON I VALORI REALI DEGLI INVENTARI
						if (!tipoOrdine.equals("R") && ordine.getStatoOrdine().equals("C") ) {
							// dipendenza dalla configurazione il prezzo si adegua se configurazione A o R con conferma SI
							// se configurazione N non si adegua
							if (ValidazioneDati.in(adeguaPrezzo, "A", "R"))	{
								if (ValidazioneDati.in(tipoOrdine, "A", "V") ) {
									//calcolo prezzo
									AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
									double totImportInv = delegate.importoInventariOrdine(ordine).doubleValue();

									ordine.setPrezzoEuroOrdine(totImportInv);
									ordine.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( ordine.getPrezzoEuroOrdine()));
									}

								}
							}

						if (!tipoOrdine.equals("R") &&  ordine.getStatoOrdine().equals("N")) {
							AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
							int inventariOrdine = delegate.countInventariOrdine(ordine);
							if (inventariOrdine > 0 ) {
								LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.noAnnullaInventari"));
								return mapping.getInputForward();
							}

							//almaviva5_20171121 segnalazione BA1
							int righeFattura = delegate.countRigheFatturaOrdine(ordine);
							if (righeFattura > 0) {
								LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.noAnnullaRigheFattura"));
								return mapping.getInputForward();
							}

					}
					}

					if (ordine.getStatoOrdine()!=null &&  ordine.getStatoOrdine().length()>0)
					{
						try
						{
							this.modificaOrdine(ordine);
						}	catch (Exception e) {
							// reset
							if (ordine.isRiapertura())
							{
								ordine.setStatoOrdine("C");
								ordine.setRiapertura(false);
								ordine.setDataChiusura(oldDataChiusura);
							}
							// annullamento
							if (selected.equals(ANNULLA))
							{
								ordine.setStatoOrdine(oldStato);
							}
							// chiusura
							if (selected.equals(CHIUDI))
							{
								ordine.setStatoOrdine(oldStato);
							}
							request.getSession().setAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO,ordine.clone() );
							log.error("", e);
							throw e;
						}

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));
						// tck 3948
						if (selected.equals(RIAPRI))
						{
							currentForm.setDisabilitaConferma(true);
						}

					}

				}
			}
			// gestione della modifica dello stato dell'ordine differita
			if (chiamante.equals("/acquisizioni/ordini/sinteticaOrdine")) {

				// sarebbe meglio passare i codici bibl e polo del primo della lista di sintetica
				ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
				Navigation navi = Navigation.getInstance(request);
				UserVO utente = navi.getUtente();

				OperazioneSuOrdiniMassivaVO richiesta = new OperazioneSuOrdiniMassivaVO();
				ParametriRichiestaElaborazioneDifferitaVO.fill(richiesta, CodiciAttivita.getIstance().GA_OPERAZIONI_SU_ORDINE, utente);

				configurazioneCriteri.setCodBibl(utente.getCodBib());
				configurazioneCriteri.setCodPolo(utente.getCodPolo());

				ConfigurazioneORDVO conf = this.loadConfigurazioneORD(configurazioneCriteri);
				//almaviva5_20130513 #4762 adegua prezzo ordine, default a NO
				String adeguaPrezzo = (conf != null) ? ValidazioneDati.coalesce(conf.getAllineamento(), "N") : "N";

				if (selected.equals(ANNULLA))
					richiesta.setTipoOperazione("A");
				else
					if (selected.equals(CHIUDI)) {
						richiesta.setTipoOperazione("C");
					// immettere le condizioni di configurazioni della fase di chiusura
						if (!currentForm.isAdeguamentoPrezzo() && adeguaPrezzo.equals("R"))	{
						// deve essere emesso messaggio di richiesta con si, no sull'adeguamento e comunque chiuso
							currentForm.setAdeguamentoPrezzo(true); // da resettare
						// emissione messaggio di conferma
							currentForm.setConferma(true);
						//operazordine.setDisabilitaTutto(true);
							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazioneAdeguamentoPrezzi"));

			    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			    		return mapping.getInputForward();
					}

					currentForm.setAdeguamentoPrezzo(false); // reset

				}
					else
						if (selected.equals(RINNOVA))
							richiesta.setTipoOperazione("R");

				// impostare gli id degli ordini selezionati come array e passarlo
				richiesta.setDatiInput(currentForm.getListaIDOrdini());
				// sarebbe meglio passare i codici bibl e polo del primo della lista di sintetica
				richiesta.setCodPolo(utente.getCodPolo());
				richiesta.setCodBib(utente.getCodBib());
				richiesta.setAdeguamentoPrezzo(ValidazioneDati.in(adeguaPrezzo, "A", "R"));

				String basePath=this.servlet.getServletContext().getRealPath(File.separator);
				richiesta.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				log.info("download path: " + downloadPath);
				String downloadLinkPath = "/";
				richiesta.setDownloadPath(downloadPath);
				richiesta.setDownloadLinkPath(downloadLinkPath);

				//almaviva5_20110824 #4602
				richiesta.setTicket(navi.getUserTicket());

				//AmministrazionePolo  anministrazionePolo;

				String s =  null;
				try {
					s = FactoryEJBDelegate.getInstance().getPolo().prenotaElaborazioneDifferita(richiesta, null);
				} catch (ApplicationException e) {
					if (e.getErrorCode() == SbnErrorTypes.USER_NOT_AUTHORIZED)
					{

						LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

						return mapping.getInputForward();
					}
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

			return mapping.getInputForward();
		}	catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));


			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}


	private void passaCriteri(OperazioneSuOrdineForm currentForm, HttpServletRequest request, ActionMapping mapping, String operazione)
	{
		OrdiniVO ord= (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);

		// caricamento dei criteri di ricerca per il crea
		try {
			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			String chiama=ricArr.getChiamante();
/*			if (operazordine.isLSRicerca())
			{
				ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
				chiama=ricArr.getChiamante();
			}
*/
			// memorizzazione dei dati del precedente ordine rinnovato
			String codOrdPrec="";
			String annoOrdPrec="";
			if (operazione.equals("R"))
			{
				if (ord.isRinnovato() && ord.getCodPrimoOrdine()!=null  &&  ord.getCodPrimoOrdine().trim().length()>0 && !ord.getCodPrimoOrdine().trim().equals("0") && ord.getAnnoPrimoOrdine()!=null && ord.getAnnoPrimoOrdine().trim().length()>0 && !ord.getAnnoPrimoOrdine().trim().equals("0"))
				{
					// viene salvaguardato sempre l'ordine originario
					codOrdPrec=ord.getCodPrimoOrdine();
					annoOrdPrec=ord.getAnnoPrimoOrdine();
				}
				else
				{
					//
					codOrdPrec=ord.getCodOrdine();
					annoOrdPrec=ord.getAnnoOrdine();
				}

			}


			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ord.getCodBibl();
			//List<String> codBAff=ricOrdini.getBiblAffil();
			String codBAff = ""; //?
			String codOrd="";  // l'ordine duplicato dovrà avere un progr all'atto del salva
			//String annoOrd=ricOrdini.;
			String tipoOrd=ord.getTipoOrdine();
			String dataOrdDa=ord.getDataOrdine();
			String dataOrdA=ord.getDataOrdine();
			String cont="0" ;
			if (ord.isContinuativo())
			{
				cont="1" ;
			}
			//String statoOrd=ricOrdini.getStato();
			String statoOrd=null;
			StrutturaCombo forn=new StrutturaCombo (ord.getFornitore().getCodice(),ord.getFornitore().getDescrizione());
			String tipoInvioOrd=ord.getTipoInvioOrdine();
			StrutturaTerna bil=new StrutturaTerna(ord.getBilancio().getCodice1(),ord.getBilancio().getCodice2(),ord.getBilancio().getCodice3());
			String sezioneAcqOrd=ord.getSezioneAcqOrdine();
			StrutturaCombo tit=new StrutturaCombo (ord.getTitolo().getCodice(),ord.getTitolo().getDescrizione());
			String dataFineAbbOrdDa=ord.getDataFineAbbOrdine();
			String dataFineAbbOrdA=ord.getDataFineAbbOrdine();
			String naturaOrd=ord.getNaturaOrdine();
			//String chiama=null;
			String[] statoOrdArr=new String [1];
			statoOrdArr[0]="A"; // impostare lo stato dell'ordine duplicato ad APERTO
			Boolean stamp=ord.isStampato();
			Boolean rinn=ord.isRinnovato();
			if (operazione.equals("R"))
			{
				rinn=false;
				// incremento dell' anno della data abbonamento e data fine

			}
			// prova
			String annoOrd=ord.getAnnoOrdine();

			eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp );
			eleRicerca.setTicket(ticket);
			// IMPOSTAZIONE ORDINAMENTO
			eleRicerca.setOrdinamento("");

			Calendar c=new GregorianCalendar();
		 	int anno=c.get(Calendar.YEAR);
		 	String annoAttuale="";
		 	annoAttuale=Integer.valueOf(anno).toString();
			String annoOrd2=annoAttuale;

			// ASSEGNAZIONE DELLA data di sistema
			Date dataodierna=new Date();
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String dataOdiernaStr =formato.format(dataodierna);

			ord.setCodOrdine("");
			ord.setStatoOrdine("A");
			ord.setAnnoOrdine(annoOrd2);
			ord.setDataOrdine(dataOdiernaStr);
			ord.setStampato(false);
			ord.setRinnovato(false);
			// memorizzazione dei dati del precedente ordine rinnovato
			if (operazione.equals("R"))
			{
				if (codOrdPrec!=null && codOrdPrec.trim().length()>0)
				{
					ord.setCodPrimoOrdine(codOrdPrec);
				}
				if (annoOrdPrec!=null && annoOrdPrec.trim().length()>0)
				{
					ord.setAnnoPrimoOrdine(annoOrdPrec);
				}
			}
			if (operazione.equals("R") && ord.isContinuativo() &&  ord.isGestBil() && ord.getBilancio()!=null && ord.getBilancio().getCodice1()!=null &&  ord.getBilancio().getCodice1().trim().length()>0 && strIsNumeric(ord.getBilancio().getCodice1().trim()))
			{
				//vedere tck 2335: Il Rinnovo deve aggiornare a +1 l'Esercizio del bilancio per qualsiasi ordine continuativo (verificandone l'esistenza; se non esiste l'Esercizio e capitolo per l'anno successivo lo mette sullo stesso Esercizio e Capitolo dell'ordine che si rinnova)
				// controllo esistenza bilancio incrementato
	        	ListaSuppBilancioVO ricercaBilanci=new ListaSuppBilancioVO();
	        	ricercaBilanci.setCodPolo(ord.getCodPolo());
	        	ricercaBilanci.setCodBibl(ord.getCodBibl());
	        	ricercaBilanci.setEsercizio(String.valueOf(Integer.valueOf(ord.getBilancio().getCodice1().trim())+1));
	        	ricercaBilanci.setCapitolo(ord.getBilancio().getCodice2());
	        	ricercaBilanci.setImpegno(ord.getBilancio().getCodice3());

	        	List<BilancioVO> elenco=null;
				try {
					elenco= this.getListaBilanciVO(ricercaBilanci,request );

				} catch (Exception e) {
					log.error("", e);
				}

	        	if (elenco!=null && elenco.size()==1)
	        	{
					ord.getBilancio().setCodice1(String.valueOf(Integer.valueOf(ord.getBilancio().getCodice1().trim())+1));
	        	}

			}
			// vedere tck 2335: SOLO per i periodici in abbonamento (cioè nature S & ordini continuativi & impegno 2) il rinnovo prevede anche l'aggiornamento della data inizio e data fine abbonamento, del volume e dell'anno di abbonamento
			boolean periodicoInAbbonamento=false;
			if (ord.isContinuativo() &&  ord.getNaturaOrdine()!=null  && ord.getNaturaOrdine().equals("S") &&  ord.getStatoOrdine()!=null  && ord.getStatoOrdine().equals("A") )
			{
				if (ord.isGestBil() && ord.getBilancio()!=null && ord.getBilancio().getCodice3()!=null &&  ord.getBilancio().getCodice3().trim().length()>0 && ord.getBilancio().getCodice3().trim().equals("2"))
				{
					periodicoInAbbonamento=true;
				}
				else if ( ord.getTipoOrdine().equals("L") || ord.getTipoOrdine().equals("D") || ord.getTipoOrdine().equals("C"))
				{
					periodicoInAbbonamento=true;
				}


			}
			if (operazione.equals("R") && periodicoInAbbonamento) {
				//almaviva5_20110714 #4525
				ord.setAnnataAbbOrdine(null);
				ord.setNumVolAbbOrdine(0);
				ord.setNumFascicoloAbbOrdine(null);
				//

				int incremento = 1;
				if (ValidazioneDati.isFilled(ord.getPeriodoValAbbOrdine()) ) {
					if (ord.getPeriodoValAbbOrdine().equals("2")) incremento = 2;
					if (ord.getPeriodoValAbbOrdine().equals("3")) incremento = 3;
				}
				// 14.07.09 assoggettare anche annoabbordine all'incremento periodale (Rossana)

				String annoAbbOrdine = ord.getAnnoAbbOrdine();
				if (ValidazioneDati.isFilled(annoAbbOrdine) && strIsNumeric(annoAbbOrdine))
					ord.setAnnoAbbOrdine(String.valueOf(Integer.valueOf(annoAbbOrdine) + incremento));


				if (ValidazioneDati.isFilled(ord.getDataFineAbbOrdine())) {
					Date dtFine = DateUtil.toDate(ord.getDataFineAbbOrdine());
					if (dtFine != null)
						ord.setDataFineAbbOrdine(DateUtil.formattaData(DateUtil.addYear(dtFine, incremento)));
				}

				if (ValidazioneDati.isFilled(ord.getDataPubblFascicoloAbbOrdine()) ) {
					Date dtInizio = DateUtil.toDate(ord.getDataPubblFascicoloAbbOrdine());
					if (dtInizio != null)
						ord.setDataPubblFascicoloAbbOrdine(DateUtil.formattaData(DateUtil.addYear(dtInizio, incremento)));
				}

			}

			eleRicerca.setOrdineDuplicatoRinnovato(ord);

			/*			if (ricOrdini.getTipoOrdinamSelez()!=null && !ricOrdini.getTipoOrdinamSelez().equal(""))
			{
				eleRicerca.setOrdinamento(ricOrdini.getTipoOrdinamSelez());
			}
			eleRicerca.setElemXBlocchi(ricOrdini.getElemXBlocchi());
*/
			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("ATTRIBUTEListaSuppOrdiniVO", eleRicerca);

		}catch (Exception e)
		{
			log.error("", e);
		}
	}



	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OperazioneSuOrdineForm currentForm = (OperazioneSuOrdineForm ) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setConferma(false);
		// il prezzo non adegua se configurazione R senza conferma SI
		if (currentForm.isAdeguamentoPrezzo()) // in caso di no della richiesta di adeguamento prezzo, l'ordine deve essere solo chiuso senza adeguamento prezzo
		{
			currentForm.setAdeguamentoPrezzo(false); // reset
			Object chiamante = request.getSession().getAttribute("chiamante");
			if (chiamante.equals("/acquisizioni/ordini/sinteticaOrdine"))
			{
				this.adeguamentoPrezzoOrdineChiusoBatch(currentForm,  request, false, null);
			}
			if (chiamante.equals("/acquisizioni/ordini/esaminaOrdineMod"))
			{
				this.adeguamentoPrezzoOrdineChiuso(currentForm,  request, false, null );
			}

		}

		return mapping.getInputForward();
	}


	private boolean modificaOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaOrdine(ordine);
		return valRitorno;
	}

	private ConfigurazioneORDVO loadConfigurazioneORD(ConfigurazioneORDVO configurazioneORD) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneORDVO configurazioneTrovata = new ConfigurazioneORDVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(configurazioneORD);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		// impostazione delle variabili dinamiche

		return configurazioneTrovata;
	}


	private void loadTipiOperazione(OperazioneSuOrdineForm operazordine, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		OrdiniVO ordine= (OrdiniVO) session.getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);

		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();

		// gestione operazioni solo da esamina
		if (ordine!=null && session.getAttribute("chiamante").equals("/acquisizioni/ordini/esaminaOrdineMod"))
		{
			// tck 2411 - un Ordine annullato non può essere né rinnovato, né chiuso, né annullato, ma proprio perché è stato annullato può essere utile crearne uno nuovo,

		if (ordine!=null
				&& ordine.getStatoOrdine()!=null
				&& !ordine.getStatoOrdine().equals("N")
				&&  !ordine.getStatoOrdine().equals("C")) {
					lista.add(new StrutturaCombo(ANNULLA, "annullamento"));
			}

			if (ordine!=null
					&& ordine.getStatoOrdine()!=null
					&& !ordine.getStatoOrdine().equals("N")
					&& !ordine.getStatoOrdine().equals("C")) {
						lista.add(new StrutturaCombo(CHIUDI, "chiusura"));
			}

			if (ordine.getTipoOrdine()!=null  && !ordine.getTipoOrdine().equals("R") ) {
				if (ordine!=null
						&& ordine.getStatoOrdine()!=null
						&&  !ordine.getStatoOrdine().equals("C")) {
							lista.add(new StrutturaCombo(DUPLICA, "duplicazione"));
				}
				if (ordine.getStatoOrdine()!=null
						&& !ordine.getStatoOrdine().equals("N")
						&&  !ordine.getStatoOrdine().equals("C")) {

					// Bug Mantis collaudo 4309 - Maggio 2013 - Manutenzione correttiva perchè non dovrebbe essere
					// possibile rinnovare un ordine continuativo più di una volta, ovvero già rinnovato.
					// Per gli ordini con flag su 'rinnovato' viene rimossa l'opzione Rinnovo
					if (ordine.isContinuativo() && ordine.isRinnovato()) {
					// non si deve inserire la voce Rinnovato
					} else {
						lista.add(new StrutturaCombo(RINNOVA, "rinnovo"));
					}
				}
			}
			if (ordine!=null
					&& session.getAttribute("chiamante").equals("/acquisizioni/ordini/esaminaOrdineMod")
					&& ordine.getStatoOrdine()!=null
					&&  ordine.getStatoOrdine().equals("C")) {
				lista.add(new StrutturaCombo(RIAPRI, "riapertura"));
				}

		}
		// gestione operazione massiva da sintetica  (tck 2382)
		if (!session.getAttribute("chiamante").equals("/acquisizioni/ordini/esaminaOrdineMod")) {
			lista.add(new StrutturaCombo(ANNULLA, "annullamento"));
			lista.add(new StrutturaCombo(CHIUDI, "chiusura"));
			lista.add(new StrutturaCombo(RINNOVA, "rinnovo"));
		}

		operazordine.setTipoOperazionenuovo(lista);
		//operazordine.setSelectedTipoOperazione("rinnovo");
	}

	private void adeguamentoPrezzoOrdineChiuso(OperazioneSuOrdineForm operazordine, HttpServletRequest request, Boolean adegua, OrdiniVO ordine ) throws Exception {
		// riguarda i soli ordini che vanno in chiusura
		try {
			if (ordine == null)
				ordine = (OrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
			if (ordine != null)	{

				String oldStato=ordine.getStatoOrdine();
				ordine.setStatoOrdine("C");
				int cntInv = 0;
				double totImportInv = 0;
				try {
					AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
					cntInv = delegate.countInventariOrdine(ordine);
					totImportInv = delegate.importoInventariOrdine(ordine).doubleValue();

				} catch (Exception e) {
					// l'errore capita in questo punto
					log.error("", e);
				}
					//AGGIUNGERE I CALCOLI DI AGGIORNAMENTO PREZZO CON I VALORI REALI DEGLI INVENTARI
				if (!ordine.getTipoOrdine().equals("R") && ordine.getStatoOrdine().equals("C") && cntInv > 0)
		          {
					// dipendenza dalla configurazione
					if (adegua) {

						if (ValidazioneDati.in(ordine.getTipoOrdine(), "A", "V") ) {
							ordine.setPrezzoEuroOrdine(totImportInv);
							ordine.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( ordine.getPrezzoEuroOrdine()));
							}
						}
					}

				try
				{
					this.modificaOrdine(ordine);
				}	catch (Exception e) {
					// reset
					// chiusura
					ordine.setStatoOrdine(oldStato);
					request.getSession().setAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO,ordine.clone());
					throw e;
				}


				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));


			}
		}	catch (ValidationException ve) {
			throw ve;
		}	catch (Exception e) {
			throw e;
		}
	}


	private void adeguamentoPrezzoOrdineChiusoBatch(OperazioneSuOrdineForm currentForm, HttpServletRequest request, Boolean adegua, OrdiniVO ordine) throws Exception {
		// riguarda i soli ordini che vanno in chiusura
		try {
			OperazioneSuOrdiniMassivaVO richiesta = new OperazioneSuOrdiniMassivaVO();
			UserVO utente = Navigation.getInstance(request).getUtente();
			ParametriRichiestaElaborazioneDifferitaVO.fill(richiesta, CodiciAttivita.getIstance().GA_OPERAZIONI_SU_ORDINE, utente);

			richiesta.setTipoOperazione("C");
			richiesta.setAdeguamentoPrezzo(false);
			richiesta.setDatiInput(currentForm.getListaIDOrdini());

			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			richiesta.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			log.info("download path: " + downloadPath);
			String downloadLinkPath = "/";
			richiesta.setDownloadPath(downloadPath);
			richiesta.setDownloadLinkPath(downloadLinkPath);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			String idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), richiesta, null);
			if (idBatch == null) {
				LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotBatchKO"));
				resetToken(request);
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.prenotazioneBatchOK", idBatch));

			resetToken(request);

		}	catch (SbnBaseException e) {
			LinkableTagUtils.addError(request, e);

		}	catch (Exception e) {
			throw e;
		}
	}


	public  boolean strIsNumeric(String data) {
		return (Pattern.matches("[0-9&&[^a-z]]+", data));
	}

	private List<BilancioVO> getListaBilanciVO(ListaSuppBilancioVO criRicerca, HttpServletRequest request) throws Exception
	{
		List<BilancioVO> listaBilanci;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//criRicerca.setLoc(request.getLocale()); // aggiunto per Documento Fisico 09/05/08
		criRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunto per Documento Fisico 27/05/08 insieme al SinteticaLookupDispatchAction invece di LookupDispatchAction
		listaBilanci = factory.getGestioneAcquisizioni().getRicercaListaBilanci(criRicerca);
		//this.sinCambio.setListaCambi(listaBilanci);
		return listaBilanci;
	}

/*
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession httpSession = request.getSession();
		//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());

		OperazioneSuOrdineForm operazordine = (OperazioneSuOrdineForm ) form;
		// leggo l'attributo di sessione
		String chiamante = (String) request.getSession().getAttribute("chiamante");
		//String chiamante = (String) request.getAttribute("chiamante");

		operazordine.setVarIndietro("indietro9");

		if(chiamante.equalsIgnoreCase("sinteticaOrdineForm"))
		{
			operazordine.setVarIndietro("indietro4");
			//varIndietro="indietro4";
		}
		else
		{
			operazordine.setVarIndietro("indietro9");
			//varIndietro="indietro9";
		}

		if (request.getParameter("indietro4") != null) {
			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), true);

			return mapping.findForward(operazordine.getVarIndietro());
		}

		//if (request.getParameter("indietro4") != null) {
		//	return mapping.findForward("indietro");
		//}

		//if (request.getParameter("indietro9") != null) {
		//	return mapping.findForward("indietro9");
		//}


		if (request.getParameter("salva") != null) {

			return mapping.findForward("salva");
		}

		// caricamento radio
		this.loadTipiOperazione();
		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), true);

		return mapping.getInputForward();
	}
*/
}
