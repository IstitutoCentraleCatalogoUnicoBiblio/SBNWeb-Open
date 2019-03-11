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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.elaborazioniDifferite.RinnovoAutorizzazioniUtenteForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RinnovoAutorizzazioniUtenteAction extends ServiziBaseAction {
	private static Logger log = Logger.getLogger(ArchiviazioneMovLocAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		//map.put("servizi.bottone.conferma","ok");
		map.put("servizi.bottone.conferma","salva");
		map.put("button.indietro","indietro");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		map.put("servizi.bottone.deselTutti", "deselTutti");
		map.put("servizi.bottone.selTutti",   "selTutti");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");

		return map;
	}

	private void checkForm(HttpServletRequest request) throws Exception {
		ActionMessages errors = new ActionMessages();
		boolean error = false;
		if (error) {
			throw new Exception();
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		Navigation navi = Navigation.getInstance(request);

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().SRV_DIRITTI_UTENTE, utente.getCodPolo(), utente.getCodBib(), null);

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
				currentForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				currentForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				currentForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
				if (bibScelta!=null && bibScelta.getCod_bib()!=null && currentForm.getCodBib()!=null )
				{
					currentForm.setCodBib(bibScelta.getCod_bib());
					currentForm.setDescrBib(bibScelta.getNom_biblioteca());
				}
				this.loadAutorizzazioni(request, form);
				if(currentForm.getTipoRinnModalitaDiff().equals("D"))
				{
					currentForm.setSelectedAut(null);
					currentForm.setElencoAutSel(null);
					currentForm.setDataRinnovoOpz2("");
					currentForm.setDataRinnovoOpz3("");
				}
				else if (currentForm.getTipoRinnModalitaDiff().equals("T"))
				{
					currentForm.setElencoAutSel(null);
					currentForm.setSelectedAut(null);
					currentForm.setDataRinnovoOpz3("");
					if (currentForm.getListaAutorizzazioni()!=null && currentForm.getListaAutorizzazioni().size()> 0)
					{
						for (int index = 0; index <currentForm.getListaAutorizzazioni().size(); index++)
						{
							StrutturaTerna elem2=(StrutturaTerna) currentForm.getListaAutorizzazioni().get(index);
							elem2.setCodice3("");
						}
					}
				}
				else if (currentForm.getTipoRinnModalitaDiff().equals("U"))
				{
					currentForm.setElencoAutSel(null);
					currentForm.setSelectedAut(null);
					currentForm.setDataRinnovoOpz2("");
					if (currentForm.getListaAutorizzazioni()!=null && currentForm.getListaAutorizzazioni().size()> 0)
					{
						for (int index = 0; index <currentForm.getListaAutorizzazioni().size(); index++)
						{
							StrutturaTerna elem2=(StrutturaTerna) currentForm.getListaAutorizzazioni().get(index);
							elem2.setCodice3("");
						}
					}
				}
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}
		return mapping.getInputForward();

	}


	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		try {
			// validazioni
			HttpSession httpSession = request.getSession();
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utenteAbil = Navigation.getInstance(request).getUtente();
			ActionMessages errors = new ActionMessages();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().SERVIZI_ARCHIVIAZIONE_MOVLOC, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

			}   catch (UtenteNotAuthorizedException e) {
				errors.add("generico", new ActionMessage("messaggio.info.noaut"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();//return null;
			}

			try {

				if(currentForm.getTipoRinnModalitaDiff().equals("D"))
				{
					if (currentForm.getSelectedAut()==null || (currentForm.getSelectedAut()!=null && currentForm.getSelectedAut().length==0))
					{
						errors.add("generico", new ActionMessage("errors.servizi.codiceNessunaSelezione"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					// validare la corrispondenza fra selezione e data stessa riga
					if (currentForm.getSelectedAut()!=null && currentForm.getSelectedAut().length>0)
					{
						StrutturaTerna elem2;
						String elemSel;
						if (currentForm.getListaAutorizzazioni()!=null && currentForm.getListaAutorizzazioni().size()> 0)
						{
							List<StrutturaCombo> autSel= new ArrayList<StrutturaCombo>();
							StrutturaCombo autSelEle=null;
							for (int index = 0; index <currentForm.getSelectedAut().length; index++)
							{
								elemSel=currentForm.getSelectedAut()[index];
								for (int y = 0; y <currentForm.getListaAutorizzazioni().size(); y++)
								{
									elem2=(StrutturaTerna)currentForm.getListaAutorizzazioni().get(y);
									if(elem2.getCodice1().equals(elemSel))
									{
										if(elem2.getCodice3()!=null && elem2.getCodice3().trim().length()>0)
										{
											autSelEle=new StrutturaCombo(elem2.getCodice1(), elem2.getCodice3());
											//data valorizzata
//											// controllo in ciclo della validità della data
											if (!ValidazioneDati.strIsNull(elem2.getCodice3()))
											{
												String campo = elem2.getCodice3();
												int codRitorno = -1;
												try {
													codRitorno = ValidazioneDati.validaDataPassata(campo);
													if (codRitorno != ValidazioneDati.DATA_OK && codRitorno != ValidazioneDati.DATA_MAGGIORE)
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
											autSel.add(autSelEle);  // riempimento dell'array delle aut selez
											break;
										}
										else
										{
											errors.add("generico", new ActionMessage("errors.servizi.valorizzareDataSele"));
											this.saveErrors(request, errors);
											resetToken(request);
											return mapping.getInputForward();
										}
									}
								}
								currentForm.setElencoAutSel(autSel); // memorizzazione array delle aut selez nella form
							}
						}
					}
				}
				else if (currentForm.getTipoRinnModalitaDiff().equals("T"))
				{
					// controllo della validità della data 2

					if (currentForm.getSelectedAut()==null || (currentForm.getSelectedAut()!=null && currentForm.getSelectedAut().length==0))
					{
						errors.add("generico", new ActionMessage("errors.servizi.codiceNessunaSelezione"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}

					if (!ValidazioneDati.strIsNull(currentForm.getDataRinnovoOpz2()))
					{
						String campo = currentForm.getDataRinnovoOpz2();
						int codRitorno = -1;
						try {
							codRitorno = ValidazioneDati.validaDataPassata(campo);
							if (codRitorno != ValidazioneDati.DATA_OK && codRitorno != ValidazioneDati.DATA_MAGGIORE)
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

					// preparazione lista autorizzazioni da rinnovare
					if (currentForm.getSelectedAut()!=null && currentForm.getSelectedAut().length>0)
					{
						StrutturaTerna elem2;
						String elemSel;
						if (currentForm.getListaAutorizzazioni()!=null && currentForm.getListaAutorizzazioni().size()> 0)
						{
							List<StrutturaCombo> autSel= new ArrayList<StrutturaCombo>();
							StrutturaCombo autSelEle=null;
							for (int index = 0; index <currentForm.getSelectedAut().length; index++)
							{
								elemSel=currentForm.getSelectedAut()[index];
								for (int y = 0; y <currentForm.getListaAutorizzazioni().size(); y++)
								{
									elem2=(StrutturaTerna)currentForm.getListaAutorizzazioni().get(y);
									if(elem2.getCodice1().equals(elemSel))
									{
										autSelEle=new StrutturaCombo(elem2.getCodice1(), currentForm.getDataRinnovoOpz2()); // stessa data per tutte le aut
										autSel.add(autSelEle);  // riempimento dell'array delle aut selez
										break;
									}
								}
								currentForm.setElencoAutSel(autSel); // memorizzazione array delle aut selez nella form
							}
						}
					}



				}
				else if (currentForm.getTipoRinnModalitaDiff().equals("U"))
				{
					// controllo della validità della data 3
					if (!ValidazioneDati.strIsNull(currentForm.getDataRinnovoOpz3()))
					{
						String campo = currentForm.getDataRinnovoOpz3();
						int codRitorno = -1;
						try {
							codRitorno = ValidazioneDati.validaDataPassata(campo);
							if (codRitorno != ValidazioneDati.DATA_OK && codRitorno != ValidazioneDati.DATA_MAGGIORE)
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
			}catch (Exception e){
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


			boolean valRitorno = false;
			//ActionMessages errors = new ActionMessages();
			currentForm.setConferma(true);
			currentForm.setPressioneBottone("salva");
			currentForm.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


/*	public ActionForward ok(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession httpSession = request.getSession();
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utenteAbil = Navigation.getInstance(request).getUtente();
		ActionMessages errors = new ActionMessages();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().SERVIZI_ARCHIVIAZIONE_MOVLOC, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

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

			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
			String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
			ArchiviazioneMovLocVO  richiesta= new ArchiviazioneMovLocVO();

//				 new gestione batch inizio
			richiesta.setCodPolo(polo);
			richiesta.setCodBib(bibl);
			richiesta.setDataSvecchiamento(currentForm.getDataSvecchiamento());
			richiesta.setUser(Navigation.getInstance(request).getUtente().getUserId());
			richiesta.setCodAttivita(CodiciAttivita.getIstance().SERVIZI_RINNOVO_DIRITTI);

//				richiesta.setParametri(inputForStampeService);
//				richiesta.setTipoOrdinamento(ordinamFile);
			String ticket=Navigation.getInstance(request).getUserTicket();
			richiesta.setTicket(ticket);

			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			richiesta.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			log.info("download path: " + downloadPath);
			String downloadLinkPath = "/";
			richiesta.setDownloadPath(downloadPath);
			richiesta.setDownloadLinkPath(downloadLinkPath);


//				AmministrazionePolo  anministrazionePolo;

			String s = getEjb().prenotaElaborazioneDifferita((ParametriRichiestaElaborazioneDifferitaVO) richiesta, null);

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
	}*/

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),
	utente.getCodBib(), CodiciAttivita.getIstance().SRV_DIRITTI_UTENTE, 10, "codBibDalista");
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
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().SRV_DIRITTI_UTENTE, utente.getCodPolo(), utente.getCodBib(), null);
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
		HttpSession httpSession = request.getSession();
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		try {
			currentForm.setSelectedAut(null);
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;

		int numAut=0;
		try {
			//currentForm.setCodSelAutSing("");
			numAut = currentForm.getListaAutorizzazioni().size();
			if (numAut>0) {
				String[] codAut = new String[numAut];
				int i=0;
				java.util.Iterator iterator=currentForm.getListaAutorizzazioni().iterator();
				while (iterator.hasNext()) {
					codAut[i] = ((StrutturaTerna)iterator.next()).getCodice1();
					i++;
				}
				currentForm.setSelectedAut(codAut);
			}
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}

		return mapping.getInputForward();
	}

	private void loadAutorizzazioni(HttpServletRequest request, ActionForm form)
	throws Exception {
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		// autorizzazioni che vengono caricate tutte e 4000 x la lista combo
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		RicercaAutorizzazioneVO ricercaAut = new RicercaAutorizzazioneVO();
		ricercaAut.setCodPolo(currentForm.getCodPolo());
		ricercaAut.setCodBib(currentForm.getCodBib());
		ricercaAut.setNumeroElementiBlocco(4000);
		ricercaAut.setTicket(Navigation.getInstance(request).getUserTicket());
		DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(ricercaAut);

		List<AutorizzazioneVO> listaAut = new ArrayList<AutorizzazioneVO>();
		listaAut.add(new AutorizzazioneVO("", ""));
		if (blocco1.getTotRighe() > 0)
			listaAut.addAll(blocco1.getLista());

		//currentForm.setListaAutorizzazioni(listaAut);

		List lista = new ArrayList();
		StrutturaTerna elem;
		if (listaAut!=null && listaAut.size()> 0)
		{
			currentForm.setRisultatiPresenti(true);
			for (int index = 0; index <listaAut.size(); index++)
			{
				if (listaAut!=null &&  listaAut.get(index).getCodAutorizzazione()!=null  && listaAut.get(index).getCodAutorizzazione().trim().length()>0)
				{
					elem = new StrutturaTerna(listaAut.get(index).getCodAutorizzazione().trim().toUpperCase(),listaAut.get(index).getDesAutorizzazione().trim().toUpperCase(), "");
					lista.add(elem);
				}
			}
			elem = new StrutturaTerna("","Utenti senza autorizzazione specifica", "");
			lista.add(elem);
			currentForm.setListaAutorizzazioni(lista);
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		try {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();

			if (currentForm.getPressioneBottone().equals("salva")) {
				currentForm.setPressioneBottone("");
				String polo = Navigation.getInstance(request).getUtente().getCodPolo();
				String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
				RinnovoDirittiDiffVO  richiesta= new RinnovoDirittiDiffVO();

//			    new gestione batch inizio
				richiesta.setCodPolo(polo);
				richiesta.setCodBib(bibl);
				//richiesta.setDataSvecchiamento(currentForm.getDataSvecchiamento());
				richiesta.setUser(Navigation.getInstance(request).getUtente().getUserId());
				richiesta.setCodAttivita(CodiciAttivita.getIstance().SRV_DIRITTI_UTENTE);

//				richiesta.setParametri(inputForStampeService);
//				richiesta.setTipoOrdinamento(ordinamFile);
				String ticket=Navigation.getInstance(request).getUserTicket();
				richiesta.setTicket(ticket);

				String basePath=this.servlet.getServletContext().getRealPath(File.separator);
				richiesta.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				log.info("download path: " + downloadPath);
				String downloadLinkPath = "/";
				richiesta.setDownloadPath(downloadPath);
				richiesta.setDownloadLinkPath(downloadLinkPath);
				richiesta.setTipoRinnModalitaDiff(currentForm.getTipoRinnModalitaDiff());
				richiesta.setElencoAutSel(currentForm.getElencoAutSel());
				richiesta.setDataRinnovoOpz2(currentForm.getDataRinnovoOpz2());
				richiesta.setDataRinnovoOpz3(currentForm.getDataRinnovoOpz3());

//				AmministrazionePolo  anministrazionePolo;

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

			}
			return mapping.getInputForward();
		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			currentForm.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RinnovoAutorizzazioniUtenteForm currentForm = (RinnovoAutorizzazioniUtenteForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	private void loadTipiOrdinamento(HttpServletRequest request, ActionForm form) throws Exception {
		ArchiviazioneMovLocForm currentForm = (ArchiviazioneMovLocForm) form;

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List listaCodice = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_MATERIE);

		currentForm.setLstTipiOrdinamento(listaCodice);
	}*/
}



