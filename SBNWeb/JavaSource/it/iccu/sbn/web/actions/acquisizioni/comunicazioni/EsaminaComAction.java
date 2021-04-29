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
package it.iccu.sbn.web.actions.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMail;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.mail.MailUtil.AddressPair;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni.EsaminaComForm;
import it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni.SinteticaComForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJBException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class EsaminaComAction extends ComunicazioneBaseAction implements SbnAttivitaChecker {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
		map.put("ricerca.button.invia","invia");
		map.put("ricerca.label.fattura","fatturaCerca");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.button.ordine","ordineCerca");
		map.put("ricerca.button.stampa","stampa");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			try {
				EsaminaComForm currentForm = (EsaminaComForm) form;
				if (Navigation.getInstance(request).isFromBar() )
		            return mapping.getInputForward();

				if(!currentForm.isSessione())
					currentForm.setSessione(true);

				// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
				// DISABILITAZIONE DELL'INPUT
				HttpSession session = request.getSession();
				ListaSuppComunicazioneVO ricArr=(ListaSuppComunicazioneVO) session.getAttribute("attributeListaSuppComunicazioneVO");
				// controllo che non riceva l'attributo di sessione di una lista supporto
				if (ricArr!=null  && ricArr.getChiamante()!=null) {
					currentForm.setEsaminaInibito(true);
					currentForm.setDisabilitaTutto(true);
				}

				currentForm.setListaDaScorrere((List<ListaSuppComunicazioneVO>) session.getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_COMUNICAZIONE));
				if(currentForm.getListaDaScorrere() != null && currentForm.getListaDaScorrere().size()!=0)
				{
					if(currentForm.getListaDaScorrere().size()>1 )
					{
						currentForm.setEnableScorrimento(true);
						//esaCambio.setPosizioneScorrimento(0);
					}
					else
					{
						currentForm.setEnableScorrimento(false);
					}
				}
				// || strIsAlfabetic(String.valueOf(this.esaSezione.getPosizioneScorrimento()))
				if (String.valueOf(currentForm.getPosizioneScorrimento()).length()==0 )
				{
					currentForm.setPosizioneScorrimento(0);
				}
				// richiamo ricerca su db con elemento 1 di ricerca
				if (!currentForm.isCaricamentoIniziale())
				{
					this.loadDatiComunicazionePassata(request,currentForm,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
					currentForm.setCaricamentoIniziale(true);
				}

				//this.loadDatiComunicazionePassata(currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
				this.loadStatoComunicazione( currentForm);
				this.loadDirezioneComunicazione( currentForm);
				this.loadTipoDocumento( currentForm);
				// elenchi differenziati per direzione e tipo doc/ogg
				this.loadTipoMessaggio( currentForm);
				if (currentForm.getDatiComunicazione().getDirezioneComunicazione()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione().equals(""))
				{
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getTipoDocumento().equals("F"))
					{
						this.loadTipoMessaggioFatt( currentForm);
					}
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("O"))
					{
						this.loadTipoMessaggioOrd( currentForm);
					}
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals(""))
					{
						this.loadTipoMessaggio( currentForm);
					}
				}
				if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione().equals("A"))
				{
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("F"))
					{
						this.loadTipoMessaggioPerFornFatt( currentForm);
					}
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("O"))
					{
						this.loadTipoMessaggioPerFornOrd( currentForm);
					}
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals(""))
					{
						this.loadTipoMessaggioPerForn( currentForm);
					}
				}
				if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione().equals("D"))
				{
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("F"))
					{
						this.loadTipoMessaggioDaFornFatt( currentForm);
					}
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getDatiComunicazione().getTipoDocumento().equals("O"))
					{
						this.loadTipoMessaggioDaFornOrd( currentForm);
					}
					if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals(""))
					{
						this.loadTipoMessaggioDaForn( currentForm);
					}
				}
				// fine elenchi differenziati
				this.loadTipoInvio( currentForm);
				this.loadTipoOrdine( currentForm);
				//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
				ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)session.getAttribute("attributeListaSuppFornitoreVO");
				if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
					{
						if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
						{
							currentForm.getDatiComunicazione().getFornitore().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
							currentForm.getDatiComunicazione().getFornitore().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						currentForm.getDatiComunicazione().getFornitore().setCodice("");
						currentForm.getDatiComunicazione().getFornitore().setDescrizione("");
					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					session.removeAttribute("attributeListaSuppFornitoreVO");
					session.removeAttribute("fornitoriSelected");
					session.removeAttribute("criteriRicercaFornitore");
	 			}
				//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
				ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 )
					{
						if (ricOrd.getSelezioniChiamato().get(0).getChiave()!=null && ricOrd.getSelezioniChiamato().get(0).getChiave().length()!=0 )
						{
							currentForm.getDatiComunicazione().getIdDocumento().setCodice1(ricOrd.getSelezioniChiamato().get(0).getTipoOrdine());
							currentForm.getDatiComunicazione().getIdDocumento().setCodice2(ricOrd.getSelezioniChiamato().get(0).getAnnoOrdine());
							currentForm.getDatiComunicazione().getIdDocumento().setCodice3(ricOrd.getSelezioniChiamato().get(0).getCodOrdine());
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						currentForm.getDatiComunicazione().getIdDocumento().setCodice1("");
						currentForm.getDatiComunicazione().getIdDocumento().setCodice2("");
						currentForm.getDatiComunicazione().getIdDocumento().setCodice3("");

					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
					session.removeAttribute("ordiniSelected");
					session.removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

	 			}
				//controllo se ho un risultato di una lista di supporto FATTURA richiamata da questa pagina (risultato della simulazione)
				ListaSuppFatturaVO ricFatt=(ListaSuppFatturaVO) session.getAttribute("attributeListaSuppFatturaVO");
				if (ricFatt!=null && ricFatt.getChiamante()!=null && ricFatt.getChiamante().equals(mapping.getPath()))
	 			{
					if (ricFatt!=null && ricFatt.getSelezioniChiamato()!=null && ricFatt.getSelezioniChiamato().size()!=0 )
					{
						if (ricFatt.getSelezioniChiamato().get(0).getNumFattura()!=null && ricFatt.getSelezioniChiamato().get(0).getNumFattura().length()!=0 )
						{
							currentForm.getDatiComunicazione().getIdDocumento().setCodice1("");
							currentForm.getDatiComunicazione().getIdDocumento().setCodice2(ricFatt.getSelezioniChiamato().get(0).getAnnoFattura());
							currentForm.getDatiComunicazione().getIdDocumento().setCodice3(ricFatt.getSelezioniChiamato().get(0).getProgrFattura());
						}
					}
					else
					{
						// pulizia della maschera di ricerca
						currentForm.getDatiComunicazione().getIdDocumento().setCodice1("");
						currentForm.getDatiComunicazione().getIdDocumento().setCodice2("");
						currentForm.getDatiComunicazione().getIdDocumento().setCodice3("");
					}

					// il reset dell'attributo di sessione deve avvenire solo dal chiamante
					session.removeAttribute("attributeListaSuppFatturaVO");
					session.removeAttribute("fattureSelected");
					session.removeAttribute("criteriRicercaFattura");

	 			}
				if (currentForm.getDatiComunicazione().getStatoComunicazione().equals("2")) {
					currentForm.setDisabilitaTutto(true);
				} else 	{
					currentForm.setDisabilitaTutto(false);
				}
				return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}


	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaComunicazioneVO(currentForm.getDatiComunicazione());
			// fine validazione

			ActionMessages errors = new ActionMessages();
			currentForm.setConferma(true);
			currentForm.setPressioneBottone("salva");
    		currentForm.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
    		currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();

		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
    		currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			this.loadDatiComunicazionePassata( request, currentForm,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			currentForm.setConferma(false);
    		currentForm.setDisabilitaTutto(false);

			if (currentForm.getPressioneBottone().equals("salva")) {
				currentForm.setPressioneBottone("");

				ComunicazioneVO eleComunicazione=currentForm.getDatiComunicazione();
				eleComunicazione.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				ListaSuppComunicazioneVO attrLS=(ListaSuppComunicazioneVO) request.getSession().getAttribute("attributeListaSuppComunicazioneVO");
				ListaSuppComunicazioneVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(eleComunicazione, attrLS);
				request.getSession().setAttribute("attributeListaSuppComunicazioneVO",attrLSagg );
				// imposta a ricevuto se la direzione è "da fornitore"
				if (currentForm.getDatiComunicazione().getDirezioneComunicazione().equals("D"))
				{
					currentForm.getDatiComunicazione().setStatoComunicazione("3");
				}

				if (!this.modificaComunicazione(eleComunicazione)) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreModifica"));
					this.saveErrors(request, errors);
					//return mapping.getInputForward();
				}
				else
				{
					// esame del tipo di  messaggio se notifica annullamento ordine (03) oppure  notifica chiusura ordine (04)
					//si deve procedere all'annullamento e/o chiusura ordine
					boolean buonfine=true;
					if (eleComunicazione.getTipoDocumento()!=null &&  eleComunicazione.getTipoDocumento().equals("O") && eleComunicazione.getTipoMessaggio()!=null &&  (eleComunicazione.getTipoMessaggio().equals("03") || eleComunicazione.getTipoMessaggio().equals("04"))) // annullamento o chiusura
					{
						// lettura ordine
						OrdiniVO eleOrd=null;
						eleOrd=this.loadDatiOrdinePassato( eleComunicazione, request);
						// controllo lo stato che deve essere aperto
						if (eleOrd!=null && eleOrd.getStatoOrdine()!=null &&  eleOrd.getStatoOrdine().equals("A"))
						{
							if (eleOrd!=null &&  eleComunicazione.getTipoMessaggio().equals("04") ) // chiusura
							{
								eleOrd.setStatoOrdine("C");
							}
							if (eleOrd!=null && eleComunicazione.getTipoMessaggio().equals("03")) // annullamento
							{
								eleOrd.setStatoOrdine("N");
							}
							// controllo esistenza inventari
							if (eleOrd!=null && eleOrd.getStatoOrdine().length()>0 && (eleOrd.getStatoOrdine().equals("C") || eleOrd.getStatoOrdine().equals("N") ) )
							{
								List listaInv=null;
								try
								{

									  String codPolo=eleOrd.getCodPolo();
							          String codBib=eleOrd.getCodBibl();
							          String codBibO=eleOrd.getCodBibl();
							          String codTipOrd=eleOrd.getTipoOrdine();
							          String codBibF="";
							          //String ticket=ordine.getTicket();
							          String ticket=eleOrd.getTicket();
							          int   annoOrd=Integer.valueOf(eleOrd.getAnnoOrdine().trim());
							          int   codOrd=Integer.valueOf(eleOrd.getCodOrdine());
							          int   nRec=999;

							          FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
							          listaInv = factory.getGestioneDocumentoFisico().getListaInventariOrdine( codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, this.getLocale(request, Constants.SBN_LOCALE), ticket,  nRec).getLista();

							          //listaInv=this.getInventariRox(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, ticket,  nRec);
							         // listaInv= inventario.getListaInventariOrdine(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, ticket,  nRec);
								} catch (Exception e) {
									// l'errore capita in questo punto
									log.error("", e);
								}
								//CASO: ORDINE DA CHIUDERE SOLO SE ESISTONO INVENTARI
								if (!eleOrd.getTipoOrdine().equals("R") && eleOrd.getStatoOrdine().equals("C") && (listaInv==null || (listaInv!=null && listaInv.size()==0)) )
						          {
									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage(
											"errors.acquisizioni.noChiusuraInventari"));
									this.saveErrors(request, errors);
									//return mapping.getInputForward();
									buonfine=false;
						          }

								if (!eleOrd.getTipoOrdine().equals("R") &&  eleOrd.getStatoOrdine().equals("N") && (listaInv!=null && listaInv.size()>0))
						          {
									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage(
											"errors.acquisizioni.noAnnullaInventari"));
									this.saveErrors(request, errors);
									//return mapping.getInputForward();
									buonfine=false;
						          }

								if (buonfine)
									{
										if (!modificaOrdine(eleOrd))
										{
											buonfine=false;
											eleOrd=null;
										}
									}
							}	// fine controllo esistenza inventari

						}
						else
						{
							eleOrd=null;
							buonfine=false;
						}

						if (eleOrd==null)
						{
							buonfine=false;
						}

					}
					if (buonfine==true)
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.modificaOK"));
						this.saveErrors(request, errors);
						return ripristina( mapping,  form,  request,  response);

					}
					else
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.modificaParzialeComOK"));
						this.saveErrors(request, errors);
					}


				}
			}
			if (currentForm.getPressioneBottone().equals("cancella")) {
				currentForm.setPressioneBottone("");
				ComunicazioneVO eleComunicazione=currentForm.getDatiComunicazione();
				if (!this.cancellaComunicazione(eleComunicazione)) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreCancella"));
					this.saveErrors(request, errors);
				}
				else
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.cancellaOK"));
					this.saveErrors(request, errors);
		    		currentForm.setDisabilitaTutto(true);
				}

			}


			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
    		currentForm.setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		// altri tipi di errore
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
		EsaminaComForm currentForm = (EsaminaComForm) form;
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

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					try
					{
						this.loadDatiComunicazionePassata( request, currentForm,currentForm.getListaDaScorrere().get(attualePosizione));
						// va ricaricata la combo dei tipi di messaggio
						cambiaComboTipoMsg(currentForm);
					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							currentForm.setPosizioneScorrimento(attualePosizione);
							return scorriAvanti( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						log.error("", e);
						throw e;
					}
					currentForm.setPosizioneScorrimento(attualePosizione);
					// aggiornamento del tab di visualizzazione dei dati per tipo ordine
					//	 disabilitazione campi di input se l'ordine non è di tipo aperto
					if (currentForm.getDatiComunicazione().getStatoComunicazione().equals("2")) {
						currentForm.setDisabilitaTutto(true);
					} else 	{
						currentForm.setDisabilitaTutto(false);
					}
					if (currentForm.isEsaminaInibito())
					{
						currentForm.setDisabilitaTutto(true);
					}

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			int dimensione=currentForm.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					try
					{
						this.loadDatiComunicazionePassata( request, currentForm,currentForm.getListaDaScorrere().get(attualePosizione));
						// va ricaricata la combo dei tipi di messaggio
						cambiaComboTipoMsg(currentForm);

					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							currentForm.setPosizioneScorrimento(attualePosizione);
							return scorriIndietro( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						log.error("", e);
						throw e;
					}

					currentForm.setPosizioneScorrimento(attualePosizione);
					if (currentForm.getDatiComunicazione().getStatoComunicazione().equals("2")) {
						currentForm.setDisabilitaTutto(true);
					} else 	{
						currentForm.setDisabilitaTutto(false);
					}
					if (currentForm.isEsaminaInibito())
					{
						currentForm.setDisabilitaTutto(true);
					}

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			ActionMessages errors = new ActionMessages();
			currentForm.setConferma(true);
			currentForm.setPressioneBottone("cancella");
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


	private void loadDatiComunicazionePassata(HttpServletRequest request, EsaminaComForm currentForm, ListaSuppComunicazioneVO criteriRicercaComunicazione) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<ComunicazioneVO> comunicazioneTrovata = new ArrayList();
		comunicazioneTrovata = factory.getGestioneAcquisizioni().getRicercaListaComunicazioni(criteriRicercaComunicazione);
		//gestire l'esistenza del risultato e che sia univoco
		currentForm.setDatiComunicazione(comunicazioneTrovata.get(0));
		currentForm.getDatiComunicazione().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
	}

	private boolean modificaComunicazione(ComunicazioneVO comunicazione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaComunicazione(comunicazione);
		return valRitorno;
	}

	private boolean cancellaComunicazione(ComunicazioneVO comunicazione) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaComunicazione(comunicazione);
		return valRitorno;
	}
	private ListaSuppComunicazioneVO AggiornaTipoVarRisultatiListaSupporto (ComunicazioneVO eleComunicazione, ListaSuppComunicazioneVO attributo)
	{
		try {
			if (eleComunicazione !=null)
			{
			List<ComunicazioneVO> risultati=new ArrayList();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getChiave().trim();
					if (eleRis.equals(eleComunicazione.getChiave().trim()))
					{
						//risultati.get(i).setTipoVariazione(eleCambio.getTipoVariazione());
						risultati.get(i).setTipoVariazione("M");

						break;
					}
			}
			attributo.setSelezioniChiamato(risultati);
			}
		} catch (Exception e) {

		}
		return attributo;
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
			EsaminaComForm currentForm = (EsaminaComForm) form;
			//almaviva5_201011223 periodici
			Navigation navi = Navigation.getInstance(request);
			if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_FASCICOLO)) {
				request.setAttribute(PeriodiciDelegate.SIF_COMUNICAZIONI, currentForm.getDatiComunicazione());
				return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_FASCICOLO, false);
			}

			if (navi.bookmarkExists(PeriodiciDelegate.BOOKMARK_KARDEX)) {
				request.setAttribute(PeriodiciDelegate.SIF_COMUNICAZIONI, currentForm.getDatiComunicazione());
				return navi.goToBookmark(PeriodiciDelegate.BOOKMARK_KARDEX, false);
			}

			//almaviva5_20110407 #4340, #4240.
			ActionForm prevForm = navi.getCache().getPreviousElement().getForm();
			if (prevForm instanceof SinteticaComForm)
				return mapping.findForward("indietro");

			return navi.goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward invia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;

		try {
			// cambia stato al messaggio e disabilita tutto
			ComunicazioneVO com = currentForm.getDatiComunicazione();

			String tipoInvio = com.getTipoInvioComunicazione();
			if (!isFilled(tipoInvio)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.comunicazioneerroreTipoInvioObblig"));
				return mapping.getInputForward();
			}

			com.setStatoComunicazione("2");
			com.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
			// imposta a ricevuto se la direzione è "da fornitore"
			if (com.getDirezioneComunicazione().equals("D"))
				com.setStatoComunicazione("1");

			if (!this.modificaComunicazione(com)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreModifica"));
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));
			currentForm.setDisabilitaTutto(true);
			if (tipoInvio.equals("M") )
				// posta elettronica
				try {
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					String indirizzoEMail = "dummy@mail.test";
					FornitoreVO fornitore = com.getAnagFornitore();
					if (fornitore != null)
						indirizzoEMail = fornitore.getEmail();

					String oggetto="";
					if (com.getDesMessaggio()!=null && com.getDesMessaggio().trim().length()>0)
						oggetto=com.getDesMessaggio();

					String testo="";
					if (com.getTipoDocumento()!=null && com.getTipoDocumento().equals("F"))
						testo="Documento: FATTURA " ;

					if (com.getTipoDocumento()!=null && com.getTipoDocumento().equals("O"))
						testo="Documento: ORDINE " ;

					if (com.getIdDocumento()!=null && com.getIdDocumento().getCodice1()!=null)
						testo=testo+com.getIdDocumento().getCodice1().trim();

					if (com.getIdDocumento()!=null && com.getIdDocumento().getCodice2()!=null)
					{
						if (com.getTipoDocumento()!=null && com.getTipoDocumento().equals("O"))
						{
							testo=testo+" ";
						}
						testo=testo+com.getIdDocumento().getCodice2().trim();
					}
					if (com.getIdDocumento()!=null && com.getIdDocumento().getCodice3()!=null)
					{
						testo=testo+" "+ com.getIdDocumento().getCodice3().trim()+"\n" ;
					}
					if (com.getNoteComunicazione()!=null && com.getNoteComunicazione().trim().length()>0)
					{
						testo=testo+"\n"+com.getNoteComunicazione();
					}
					if (com.getDenoBibl()!=null && com.getDenoBibl().trim().length()>0 )
					{
						testo=testo+"\n\n"+com.getDenoBibl();
					}

					// a capo=\n
					if (isFilled(indirizzoEMail) && isFilled(oggetto) ) { //&& testo!=null && testo.trim().length()>0)

						AmministrazioneMail ejb = factory.getGestioneAcquisizioni().getAmministrazioneMailBean();
						AddressPair pair = ejb.getMailBiblioteca(com.getCodPolo(), com.getCodBibl() );
						InternetAddress to = new InternetAddress(indirizzoEMail, fornitore.getNomeFornitore() );

						//almaviva5_20130702 #4765
						String html = preparaMailHTML(request, response, com);
						if (!isFilled(html)) {
							LinkableTagUtils.addError(request,  new ActionMessage("errors.acquisizioni.erroreServerPosta"));
							return mapping.getInputForward();
						}

						MailVO mail = new MailVO();
						mail.setBody(html);
						mail.setSubject(oggetto);
						mail.setFrom(pair.getFrom());
						mail.getReplyTo().add(pair.getReplyTo());
						mail.getTo().add(to);
						mail.setType(MailUtil.MIME_TYPE_HTML);
						MailUtil.sendMailAsync(mail);
					}
					else
						LinkableTagUtils.addError(request,  new ActionMessage("errors.acquisizioni.erroreServerPosta"));

				} catch (Exception e) {
					log.error("", e);
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreServerPosta"));
				}

    		return mapping.getInputForward();
		}	catch (ValidationException ve) {
				currentForm.setDisabilitaTutto(false);
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				return mapping.getInputForward();
		// altri tipi di errore
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private String preparaMailHTML(HttpServletRequest request, HttpServletResponse response, ComunicazioneVO com) throws Exception {
		//almaviva5_20130702 #4765
		List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_COMUNCAZIONE);
		ModelloStampaVO modello = ValidazioneDati.first(listaModelli);
		List listaDati = new ArrayList();
		listaDati.add(com);

		// base path per i percorsi fisici
		String basePath = this.servlet.getServletContext().getRealPath(File.separator);
		// file e path del template jrxml
		String fileJrxml = modello.getJrxml() + ".jrxml";
		String pathJrxml = basePath + File.separator + "jrxml" + File.separator + fileJrxml;
		// cartella da utilizzare come storage dei file generati con i
		// report
		String pathDownload = StampeUtil.getBatchFilesPath();
		String tipoFormato = TipoStampa.HTML.name();

		// preparo StampaOnLineVO e vi inserisco i dati comuni a tutte le stampe
		StampaOnLineVO stampaOnLineVO = new StampaOnLineVO();
		stampaOnLineVO.setDownload(pathDownload);
		stampaOnLineVO.setDownloadLinkPath("/");
		stampaOnLineVO.setTipoStampa(tipoFormato);
		stampaOnLineVO.setUser(Navigation.getInstance(request).getUtente().getUserId());
		stampaOnLineVO.setDatiStampa(listaDati);
		stampaOnLineVO.setCodPolo(com.getCodPolo());
		stampaOnLineVO.setCodBib(com.getCodBibl());
		stampaOnLineVO.setTemplate(pathJrxml);

		// vado a preparare l'ouput stampa per stampa
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		OutputStampaVo output = factory.getStampeOnline().stampaOnlineComunicazione(stampaOnLineVO);
		// ora ho finito di preparare la stampa, devo effettuare alcune
		// operazioni prima di uscire
		if (output.getStato().equals(ConstantsJMS.STATO_OK)) {
			InputStream in = output.getOutput();
			String html = IOUtils.toString(in);

			return html;
		}

		return null;
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			ComunicazioneVO eleComunicazione=currentForm.getDatiComunicazione();
			request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_COMUNICAZIONE);
			request.setAttribute("DATI_STAMPE_ON_LINE", eleComunicazione);
			return mapping.findForward("stampaOL");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, currentForm);
			if (forward != null){
				return forward;
			}
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

/*			if (request.getSession().getAttribute("criteriRicercaFornitore")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaFornitoreCerca( currentForm,request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( EsaminaComForm currentForm, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getDatiComunicazione().getCodBibl();
		String codForn=currentForm.getDatiComunicazione().getFornitore().getCodice();
		String nomeForn=currentForm.getDatiComunicazione().getFornitore().getDescrizione();
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="0"; // ricerca sempre locale
		String chiama=mapping.getPath();
		//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}

	public ActionForward ordineCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			request.getSession().removeAttribute("ordiniSelected");
			request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

/*			if (request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null )
			{
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaOrdineCerca( currentForm,request,mapping);
			return mapping.findForward("ordineCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaOrdineCerca( EsaminaComForm currentForm, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getDatiComunicazione().getCodBibl();
		String codBAff = null;
		String codOrd=currentForm.getDatiComunicazione().getIdDocumento().getCodice3();
		String annoOrd=currentForm.getDatiComunicazione().getIdDocumento().getCodice2();
		String tipoOrd=currentForm.getDatiComunicazione().getIdDocumento().getCodice1(); // A
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		if (currentForm.getDatiComunicazione()!=null && currentForm.getDatiComunicazione().getFornitore()!=null && currentForm.getDatiComunicazione().getFornitore().getCodice()!=null && currentForm.getDatiComunicazione().getFornitore().getCodice().trim().length()>0 )
		{
			forn.setCodice(currentForm.getDatiComunicazione().getFornitore().getCodice());
		}
		String tipoInvioOrd=null;
		StrutturaTerna bil=new StrutturaTerna("","","" );
		String sezioneAcqOrd=null;
		StrutturaCombo tit=new StrutturaCombo ("","");
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd=null;
		String chiama=mapping.getPath();
		String[] statoOrdArr=new String[0];
		Boolean stamp=false;
		Boolean rinn=false;

		eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp);
		String ticket=Navigation.getInstance(request).getUserTicket();
		eleRicerca.setTicket(ticket);
		eleRicerca.setModalitaSif(false);

		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);

	}catch (Exception e) {	}
	}

	public ActionForward fatturaCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaComForm currentForm = (EsaminaComForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFatturaVO")==null
			request.getSession().removeAttribute("attributeListaSuppFatturaVO");
			request.getSession().removeAttribute("fattureSelected");
			request.getSession().removeAttribute("criteriRicercaFattura");

/*			if (request.getSession().getAttribute("criteriRicercaFattura")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFatturaVO");
				request.getSession().removeAttribute("fattureSelected");
				request.getSession().removeAttribute("criteriRicercaFattura");
			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaFatturaCerca( currentForm,request,mapping);
			return mapping.findForward("fatturaCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFatturaCerca( EsaminaComForm currentForm, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getDatiComunicazione().getCodBibl();
		String annoF=currentForm.getDatiComunicazione().getIdDocumento().getCodice2();
		String numF="";
		String staF="";
		String dataDa="";
		String dataA="";
		String prgF=currentForm.getDatiComunicazione().getIdDocumento().getCodice3();
		String dataF="";
		String dataRegF="";
		String tipF="";
		StrutturaTerna ordFatt=new StrutturaTerna("","","");
		StrutturaCombo fornFatt=new StrutturaCombo("","");
		if (currentForm.getDatiComunicazione()!=null && currentForm.getDatiComunicazione().getFornitore()!=null && currentForm.getDatiComunicazione().getFornitore().getCodice()!=null && currentForm.getDatiComunicazione().getFornitore().getCodice().trim().length()>0 )
		{
			fornFatt.setCodice(currentForm.getDatiComunicazione().getFornitore().getCodice());
		}
		StrutturaTerna bilFatt=new StrutturaTerna("","","");
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppFatturaVO( codP, codB,  annoF, prgF , dataDa,  dataA ,   numF,  dataF, dataRegF,  staF, tipF ,  fornFatt, ordFatt,  bilFatt,  chiama,   ordina);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFatturaVO", eleRicerca);
		//String ticket=Navigation.getInstance(request).getUserTicket();
		//eleRicerca.setTicket(ticket);

	}catch (Exception e) {	}
	}

/*	private void loadDatiComunicazione() throws Exception {

		//	(String codP, String codB, String codMsg, String tipoDoc,String tipoMsg, StrutturaCombo forn, StrutturaTerna idDoc, String statoCom,String dataCom, String dirCom, String tipoInvioCom, String noteCom) throws Exception {
		ComunicazioneVO com=new ComunicazioneVO("X10", "01", "18", "O","25", new StrutturaCombo("90","Libreria Moderna"),new StrutturaTerna("A","2006","43"), "3", "27/03/2006", "A", "S" , "Fasc.12 - 25/11/2004 -  - Vol.0000 Fasc.2 - 01/02/2004 -  - Vol.0000 Fasc.1 - 01/01/2004 -  - Vol.0000");
		currentForm.setDatiComunicazione(com);
	}*/

	private void loadDirezioneComunicazione(EsaminaComForm currentForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("A","Per fornitore");
		lista.add(elem);
		elem = new StrutturaCombo("D","Da Fornitore");
		lista.add(elem);

		currentForm.setListaDirezioneComunicazione(lista);
	}

/*	private void loadTipoOrdine() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","Acquisto");
		lista.add(elem);
		elem = new StrutturaCombo("L","Deposito Legale");
		lista.add(elem);
		elem = new StrutturaCombo("D","Dono");
		lista.add(elem);
		elem = new StrutturaCombo("C","Scambio");
		lista.add(elem);
		elem = new StrutturaCombo("V","Visione Trattenuta");
		lista.add(elem);

		currentForm.setListaTipoOrdine(lista);
	}*/

	private void loadTipoOrdine(EsaminaComForm currentForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		currentForm.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));
	}

	private void loadTipoDocumento(EsaminaComForm currentForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","Tutti");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - Fattura");
		lista.add(elem);
		elem = new StrutturaCombo("O","O - Ordine");
		lista.add(elem);
		currentForm.setListaTipoDocumento(lista);
	}


	private void loadStatoComunicazione(EsaminaComForm currentForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1	- RICEVUTO");
		lista.add(elem);
		elem = new StrutturaCombo("2","2	- SPEDITO");
		lista.add(elem);
		elem = new StrutturaCombo("3","3	- NON SPEDITO");
		lista.add(elem);
		currentForm.setListaStatoComunicazione(lista);
	}

	private void loadTipoMessaggio(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	private void loadTipoMessaggioOrd(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	private void loadTipoMessaggioFatt(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	private void loadTipoMessaggioDaFornOrd(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	private void loadTipoMessaggioDaFornFatt(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	private void loadTipoMessaggioPerFornOrd(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	private void loadTipoMessaggioPerFornFatt(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);

		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	private void loadTipoMessaggioPerForn(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("02","reclamo ordine");
		lista.add(elem);
		elem = new StrutturaCombo("03","notifica annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("04","notifica chiusura ordine");
		lista.add(elem);
		elem = new StrutturaCombo("05","richiesta annullamento ordine");
		lista.add(elem);
		elem = new StrutturaCombo("06","note ordine");
		lista.add(elem);
		elem = new StrutturaCombo("07","note fattura");
		lista.add(elem);
		elem = new StrutturaCombo("08","reclamo fattura");
		lista.add(elem);
		elem = new StrutturaCombo("24","sollecito ordine");
		lista.add(elem);
		elem = new StrutturaCombo("25","reclamo e sollecito ordine");
		lista.add(elem);

		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}
	private void loadTipoMessaggioDaForn(EsaminaComForm currentForm) throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","note di precisazione");
		lista.add(elem);
		elem = new StrutturaCombo("10","già fornito");
		lista.add(elem);
		elem = new StrutturaCombo("11","non rintracciabile");
		lista.add(elem);
		elem = new StrutturaCombo("12","non trattato");
		lista.add(elem);
		elem = new StrutturaCombo("13","non venduto separatamente");
		lista.add(elem);
		elem = new StrutturaCombo("14","temporaneamente non in stock");
		lista.add(elem);
		elem = new StrutturaCombo("15","aumento considerevole del prezzo");
		lista.add(elem);
		elem = new StrutturaCombo("16","non ottenibile");
		lista.add(elem);
		elem = new StrutturaCombo("17","fuori commercio");
		lista.add(elem);
		elem = new StrutturaCombo("18","esaurito in brossura, disponibile rilegato");
		lista.add(elem);
		elem = new StrutturaCombo("19","non ancora pubblicato");
		lista.add(elem);
		elem = new StrutturaCombo("20","pubblicazione esaurita");
		lista.add(elem);
		elem = new StrutturaCombo("21","pubblicazione esaurita, sostituita da altra edizione");
		lista.add(elem);
		elem = new StrutturaCombo("22","esaurito in rilegatura, disponibile in brossura");
		lista.add(elem);
		elem = new StrutturaCombo("23","in attesa di ristampa");
		lista.add(elem);
		currentForm.setListaTipoMessaggio(lista);
		currentForm.setListaTipoMessaggio(this.ElencaPer(lista, currentForm,"tipo"));

	}

	public List<StrutturaCombo> ElencaPer(List<StrutturaCombo> lst,EsaminaComForm esaCom,String sortBy ) throws EJBException {
		//List<OrdiniVO> lst = sintordini.getListaOrdini();
		Comparator comp=null;
		if (sortBy==null)
		{
			comp =new TipoMsgAscending();
		}
		else if (sortBy.equals("tipo")) {
			comp =new TipoMsgAscending();
		}
		if (lst != null)
		{
			if (comp != null)
			{
				Collections.sort(lst, comp);
			}
		}
		return lst;
	}

	private static class TipoMsgAscending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((StrutturaCombo) o1).getDescrizione();
				String e2 = ((StrutturaCombo) o2).getDescrizione();
				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		}
	}

	private static class TipoMsgDescending implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((StrutturaCombo) o1).getDescrizione();
				String e2 = ((StrutturaCombo) o2).getDescrizione();
				return - e1.compareTo(e2);
			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		}
	}

	private boolean modificaOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaOrdine(ordine);
		return valRitorno;
	}

	private OrdiniVO loadDatiOrdinePassato(ComunicazioneVO eleComunicazione, HttpServletRequest request) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		// prepara lista supporto

		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		String ticket=Navigation.getInstance(request).getUserTicket();
		OrdiniVO eleElenco=new OrdiniVO();

		String codP=eleComunicazione.getCodPolo();
		String codB=eleComunicazione.getCodBibl();
		String codBAff=null;
		String codOrd=eleComunicazione.getIdDocumento().getCodice3();
		String annoOrd=eleComunicazione.getIdDocumento().getCodice2();
		String tipoOrd=eleComunicazione.getIdDocumento().getCodice1();
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=""; // ininfluente doppione
		StrutturaCombo forn=new StrutturaCombo("","") ;
		String tipoInvioOrd="";
		StrutturaTerna bil=new StrutturaTerna("","","");
		String sezioneAcqOrd="";
		StrutturaCombo tit=new StrutturaCombo("","");
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd="";
		String chiama=null;
		String[] statoOrdArr=null; // solo per scorrimenti di  dettaglio
		Boolean rinn=false;
		Boolean stamp=false;
		eleRicerca=new ListaSuppOrdiniVO( codP, codB,  codBAff,   codOrd,  annoOrd,  tipoOrd,  dataOrdDa, dataOrdA,   cont,  statoOrd,  forn,   tipoInvioOrd,  bil,    sezioneAcqOrd,  tit,   dataFineAbbOrdDa,  dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn, stamp);
		eleRicerca.setTicket(ticket);
		eleRicerca.setOrdinamento("");

		List<OrdiniVO> ordiniTrovato = factory.getGestioneAcquisizioni().getRicercaListaOrdini(eleRicerca);
		//gestire l'esistenza del risultato e che sia univoco
		if (ordiniTrovato!=null && ordiniTrovato.size()==1)
		{
			eleElenco=ordiniTrovato.get(0);
			eleElenco.setUtente(eleComunicazione.getUtente());
		}
		return eleElenco;
	}

	protected Locale getLocale(HttpServletRequest request, String locale) {
		return RequestUtils.getUserLocale(request, locale);
	}


	private void loadTipoInvio(EsaminaComForm currentForm) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	currentForm.setListaTipoInvio(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoInvio()));


/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("F","F - fax");
		lista.add(elem);
		elem = new StrutturaCombo("P","P - posta");
		lista.add(elem);
		elem = new StrutturaCombo("S","S - stampa");
		lista.add(elem);
		currentForm.setListaTipoInvio(lista);
*/	}
/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			EsaminaComForm currentForm = (EsaminaComForm) form;

			if (request.getParameter("salva0") != null) {
				//return mapping.findForward("salva");
			}
			if (request.getParameter("invia0") != null) {
				//return mapping.findForward("invia");
			}

			if (request.getParameter("ripristina0") != null) {
				//return mapping.findForward("ripristina");
			}

			if (request.getParameter("cancella0") != null) {
				//return mapping.findForward("cancella");
			}

			if (request.getParameter("indietro1") != null) {
				return mapping.findForward("indietro");
			}

			this.loadDatiComunicazione();
			this.loadStatoComunicazione();
			this.loadDirezioneComunicazione();
			this.loadTipoDocumento();
			this.loadTipoMessaggio();
			this.loadTipoInvio();
			this.loadTipoOrdine();
			currentForm.setTipoInvio(currentForm.getDatiComunicazione().getTipoInvioComunicazione());
			currentForm.setTipoDocumento(currentForm.getDatiComunicazione().getTipoDocumento());
			currentForm.setTipoMessaggio(currentForm.getDatiComunicazione().getTipoMessaggio());
			currentForm.setStatoComunicazione(currentForm.getDatiComunicazione().getStatoComunicazione());
			currentForm.setDirezioneComunicazione(currentForm.getDatiComunicazione().getDirezioneComunicazione());

			// mettere controllo su tipo doc se ORDINE
			if (currentForm.getDatiComunicazione().getTipoDocumento().equals("O")) {
				currentForm.setTipoOrdine(currentForm.getDatiComunicazione().getIdDocumento().getCodice1());
			}
			else
			{
				currentForm.setTipoOrdine("");
			}
			return mapping.getInputForward();
	}
*/

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		Navigation navi = Navigation.getInstance(request);
		UserVO utente = navi.getUtente();
		if (idCheck.equals("GESTIONE") ){
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_COMUNICAZIONI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				log.error("", e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		return super.checkAttivita(request, form, idCheck);
	}

	public void cambiaComboTipoMsg( EsaminaComForm currentForm) throws Exception
	{
		if (currentForm.getDatiComunicazione().getDirezioneComunicazione()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione().equals(""))
		{
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getTipoDocumento().equals("F"))
			{
				this.loadTipoMessaggioFatt( currentForm);
			}
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("O"))
			{
				this.loadTipoMessaggioOrd( currentForm);
			}
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals(""))
			{
				this.loadTipoMessaggio( currentForm);
			}
		}
		if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione().equals("A"))
		{
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("F"))
			{
				this.loadTipoMessaggioPerFornFatt( currentForm);
			}
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("O"))
			{
				this.loadTipoMessaggioPerFornOrd( currentForm);
			}
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals(""))
			{
				this.loadTipoMessaggioPerForn( currentForm);
			}
		}
		if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione()!=null && currentForm.getDatiComunicazione().getDirezioneComunicazione().equals("D"))
		{
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals("F"))
			{
				this.loadTipoMessaggioDaFornFatt( currentForm);
			}
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null && currentForm.getDatiComunicazione().getTipoDocumento().equals("O"))
			{
				this.loadTipoMessaggioDaFornOrd( currentForm);
			}
			if (currentForm.getDatiComunicazione().getTipoDocumento()!=null &&  currentForm.getDatiComunicazione().getTipoDocumento().equals(""))
			{
				this.loadTipoMessaggioDaForn( currentForm);
			}
		}
	}

}
