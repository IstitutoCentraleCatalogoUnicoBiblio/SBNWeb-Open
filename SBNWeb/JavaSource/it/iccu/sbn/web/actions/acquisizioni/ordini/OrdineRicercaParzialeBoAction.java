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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBiblioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.OrdineRicercaParzialeBoForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class OrdineRicercaParzialeBoAction extends AcquisizioniBaseAction implements SbnAttivitaChecker {
	//private OrdineRicercaParzialeForm ricOrdini;

	private static Log log = LogFactory.getLog(OrdineRicercaParzialeBoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.label.sezione","sezioneCerca");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.button.indietro","indietro");
		map.put("ordine.label.bilancio","bilancioCerca");
		map.put("ordine.bottone.searchTit", "sifbid");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			if(!ricOrdini.isSessione())
			{
				//loadDefault(request, mapping, form);
				ricOrdini.setSessione(true);
			}

			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.ordini") && (ricOrdini.getCodiceBibl()==null || (ricOrdini.getCodiceBibl()!=null && ricOrdini.getCodiceBibl().trim().length()==0)) && Navigation.getInstance(request).getActionCaller()==null )
			{
				// si proviene dal menu
				// pulizia integrale delle variabili di sessione
				Pulisci.PulisciVar(request);
/*				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);*/
			}
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();

			if (biblio!=null &&   (ricOrdini.getCodiceBibl()==null || (ricOrdini.getCodiceBibl()!=null && ricOrdini.getCodiceBibl().trim().length()==0)))
			{
				ricOrdini.setCodiceBibl(biblio);
				ricOrdini.setDenoBibl(Navigation.getInstance(request).getUtente().getBiblioteca());
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				ricOrdini.setCodiceBibl(bibScelta.getCod_bib());
				ricOrdini.setDenoBibl(bibScelta.getNom_biblioteca());
			}
			if (!ricOrdini.isCaricamentoIniziale())
			{
				//this.loadDatiSuggerimentoPassato(esaSugg.getListaDaScorrere().get(this.esaSugg.getPosizioneScorrimento()));
				ricOrdini.setTitolo(new StrutturaCombo("",""));
				ricOrdini.setCaricamentoIniziale(true);
			}

			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(ricOrdini.getCodiceBibl());
			configurazioneCriteri.setCodPolo(ricOrdini.getCodPolo());
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
			Boolean gestBil =true;
			Boolean gestSez =true;
			Boolean gestProf =true;
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneBilancio())
			{
				gestBil =configurazioneLetta.isGestioneBilancio();
			}
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneSezione())
			{
				gestSez =configurazioneLetta.isGestioneSezione();
			}
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneProfilo())
			{
				gestProf =configurazioneLetta.isGestioneProfilo();
			}

			if (gestBil!=null && !gestBil)
			{
				ricOrdini.setGestBil(false);
			}
			if (gestSez!=null && !gestSez)
			{
				ricOrdini.setGestSez(false);
			}
			if (gestProf!=null && !gestProf)
			{
				ricOrdini.setGestProf(false);
			}

			//ricOrdini.setTitolo(new StrutturaCombo("",""));
			this.loadContinuativo( ricOrdini);
			this.loadBiblAffil( ricOrdini);
			this.loadTipoOrdine( ricOrdini);
			this.loadNatura( ricOrdini);
			this.loadStato( ricOrdini);
			this.loadTipoInvio( ricOrdini);
			this.loadTipoImpegno( ricOrdini);
			this.loadTipoOrdinamento( ricOrdini);
			// condizioni di ricerca univoca
			if ( ricOrdini.getNumero()!=null && ricOrdini.getNumero().trim().length()!=0   && ricOrdini.getTipoOrdine()!=null && ricOrdini.getTipoOrdine().trim().length()!=0  &&  ricOrdini.getAnnoOrdine()!=null && ricOrdini.getAnnoOrdine().trim().length()!=0    )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				ricOrdini.setSezione("");
				ricOrdini.setContinuativo("");
				ricOrdini.setNatura("");
				ricOrdini.getTitolo().setCodice("");
				ricOrdini.getTitolo().setDescrizione("");
				ricOrdini.setFornitore("");
				ricOrdini.setCodFornitore("");
				ricOrdini.setDataOrdineDa("");
				ricOrdini.setDataOrdineA("");
				ricOrdini.setDataStampaOrdineA("");
				ricOrdini.setDataStampaOrdineDa("");
				ricOrdini.setDataOrdineAbbA("");
				ricOrdini.setDataOrdineAbbDa("");
				ricOrdini.setEsercizio("");
				ricOrdini.setCapitolo("");
				ricOrdini.setTipoImpegno("");
				ricOrdini.setStatoArr(new String[0]);
				ricOrdini.setTipoInvio("");
				ricOrdini.setStampato(false);
				ricOrdini.setRinnovato(false);

				//ricOrdini.setDisabilitaTutto(true); // è stato tolto l'onchange
			}
			else
			{
				//ricOrdini.setDisabilitaTutto(false); / è stato tolto l'onchange
			}

/*			if (ricOrdini.isSubmitDinamico())
			{
					//controllo terna completa per disabilitazione campi ricOrdini.getPuntuale()!=null &&
				if ( ricOrdini.getNumero()!=null && ricOrdini.getNumero().trim().length()!=0   && ricOrdini.getTipoOrdine()!=null && ricOrdini.getTipoOrdine().trim().length()!=0  &&  ricOrdini.getAnnoOrdine()!=null && ricOrdini.getAnnoOrdine().trim().length()!=0    )
				{
					ricOrdini.setDisabilitaTutto(true);
				}
				ricOrdini.setSubmitDinamico(false);
			}
*//*			else
			{
				ricOrdini.setPuntuale(new StrutturaTerna("","",""));
			}
*/

			// controllo se la ricerca è stata richiamata dal VAI A
			String bid=(String) request.getAttribute("bid");
			String desc=(String) request.getAttribute("desc");
			String natura=(String) request.getAttribute("natura");

			if (bid!=null && bid.length()!=0 && desc!=null && desc.length()!=0 && natura!=null && natura.length()!=0)
			{
				// controllo se richiamo da VAI A o da interrogazione titoli come lista supp
				if ( Navigation.getInstance(request)!=null  && Navigation.getInstance(request).getActionCaller()!=null && Navigation.getInstance(request).getActionCaller().equals("/gestionebibliografica/titolo/analiticaTitolo"))
//
				{
					ricOrdini.setProvenienzaVAIA(true);
					// imposta maschera di ricerca
					ricOrdini.getTitolo().setCodice(bid);
					ricOrdini.getTitolo().setDescrizione(desc);
					ricOrdini.setNatura(natura);
					// imposta parametri di ricerca
					ListaSuppOrdiniVO eleRicerca=this.caricaParametriVAIA ( ricOrdini, bid,desc,natura);
					eleRicerca.setCodBibl(biblio);
					request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);
				}
			}
			// controllo se ho un risultato da interrogazione ricerca

			if (bid!=null && bid.length()!=0 )
			{
				String titolo=(String) request.getAttribute("titolo");
				// controllo se ho un risultato da interrogazione ricerca
				//String acq = request.getParameter("ACQUISIZIONI");
				//if ( acq != null) {
				ricOrdini.getTitolo().setCodice(bid);
				if ( titolo != null) {
					ricOrdini.getTitolo().setDescrizione(titolo);
					ricOrdini.setNatura((String)request.getAttribute("natura"));
				}
			}
			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);

			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						ricOrdini.setCodFornitore(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						ricOrdini.setFornitore(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
						ricOrdini.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricOrdini.setCodFornitore("");
					ricOrdini.setFornitore("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

 			}
			//controllo se ho un risultato di una lista di supporto BILANCIO richiamata da questa pagina (risultato della simulazione)
			ListaSuppBilancioVO ricBil=(ListaSuppBilancioVO)request.getSession().getAttribute("attributeListaSuppBilancioVO");
			if (ricBil!=null && ricBil.getChiamante()!=null && ricBil.getChiamante().equals(mapping.getPath()))
 			{
				if (ricBil!=null && ricBil.getSelezioniChiamato()!=null && ricBil.getSelezioniChiamato().size()!=0 )
				{
					if (ricBil.getSelezioniChiamato().get(0).getChiave()!=null && ricBil.getSelezioniChiamato().get(0).getChiave().length()!=0 )
					{
						ricOrdini.setEsercizio(ricBil.getSelezioniChiamato().get(0).getEsercizio());
						ricOrdini.setCapitolo(ricBil.getSelezioniChiamato().get(0).getCapitolo());
						//ricOrdini.setTipoImpegno(ricBil.getSelezioniChiamato().get(0).getImpegno());
						ricOrdini.setTipoImpegno(ricBil.getSelezioniChiamato().get(0).getSelezioneImp());
						ricOrdini.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricOrdini.setEsercizio("");
					ricOrdini.setCapitolo("");
					ricOrdini.setTipoImpegno("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppBilancioVO");
				request.getSession().removeAttribute("bilanciSelected");
				request.getSession().removeAttribute("criteriRicercaBilancio");

 			}
			//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
			ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
 			{
				if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
				{
					if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
					{
						ricOrdini.setSezione(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
						ricOrdini.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricOrdini.setSezione("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");

 			}
			// controllo che non riceva l'attributo di sessione di una lista supporto
			// oppure provenga dal vai A || (ricArr.getChiamante()!=null && ricOrdini.isProvenienzaVAIA())

			if ((ricArr!=null &&  ricArr.getChiamante()!=null && ricArr.getSelezioniChiamato()==null) )
			{
				// per il layout
				if (!ricOrdini.isProvenienzaVAIA())
				{
					ricOrdini.setVisibilitaIndietroLS(true);
				}
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					ricOrdini.setLSRicerca(true); // fai rox 2
				//}
				// per il layout fine

				if (ricArr.getFornitore()!=null && ricArr.getFornitore().getCodice()!=null &&  ricArr.getFornitore().getCodice().trim().length()>0 && ricArr.getChiamante().equals("/acquisizioni/fatture/inserisciFattura") || ricArr.getChiamante().equals("/acquisizioni/fatture/esaminaFattura") )
				{
					ricOrdini.setDisabilitaFornitore(true); // solo da fattura
				}

				if (ricArr.isModalitaSif())
				{
					List<OrdiniVO> listaOrdini;
					//this.caricaAttributeListaSupp( ricOrdini, ricArr);
					listaOrdini=this.getListaOrdiniVO(ricArr); // va in errore se non ha risultati
					this.caricaAttributeListaSupp( ricOrdini,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.findForward("cerca");
				}
				else
				{
					if (!ricOrdini.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
					{
						this.caricaAttributeListaSupp( ricOrdini,ricArr);
					}
					else
					{
						ricOrdini.setRientroDaSif(false);
					}
					return mapping.getInputForward();
				}

			}
			else
			{

				return mapping.getInputForward();
			}
		}	catch (ValidationException ve) {

				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppOrdiniVO ricArrRec=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
					this.caricaAttributeListaSupp( ricOrdini,ricArrRec);
					//nov 2008 segnalazione di rossana e almaviva2 in caso di utente non autorizzato il bottone indietro non compare se ordini non trovati
					//if (!ricOrdini.isProvenienzaVAIA())
					//{
						ricOrdini.setVisibilitaIndietroLS(true);
					//}

/*					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();*/

					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && ricArrRec.getChiamante().endsWith("RicercaParziale"))
					if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && !ricArrRec.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo") )
					{
		 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
						// in tal caso la ricerca senza esito
						// non deve automaticamente presentare la maschera di crea ma emettere il messaggio
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					else
					{
						// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
						// in tal caso la ricerca senza esito
						// deve automaticamente presentare la maschera di crea (ANCHE PER IL VAI A)
						if (ricArrRec.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
						{
							this.passaCriteri( ricOrdini, request); // imposta il crea con i valori cercati
							// SE SI E' IMPOSTATO UN VALORE SPECIFICO DI TIPO ORDINE
							Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
							UserVO utente = Navigation.getInstance(request).getUtente();
							try {
								utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI, utente.getCodPolo(), utente.getCodBib(), null);
								if (ricArrRec.getTipoOrdine()!=null)
								{

									if (ricArrRec.getTipoOrdine().equals("A"))
									{
										utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA, utente.getCodPolo(), utente.getCodBib(), null);
										return mapping.findForward("nuovoA");
									}
									else if (ricArrRec.getTipoOrdine().equals("L"))
									{
										utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO, utente.getCodPolo(), utente.getCodBib(), null);
										return mapping.findForward("nuovoL");
									}
									else if (ricArrRec.getTipoOrdine().equals("D"))
									{
										utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO, utente.getCodPolo(), utente.getCodBib(), null);
										return mapping.findForward("nuovoD");
									}
									else if (ricArrRec.getTipoOrdine().equals("C"))
									{
										utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_SCAMBIO, utente.getCodPolo(), utente.getCodBib(), null);
										return mapping.findForward("nuovoC");
									}
									else if (ricArrRec.getTipoOrdine().equals("V"))
									{
										utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA, utente.getCodPolo(), utente.getCodBib(), null);
										return mapping.findForward("nuovoV");
									}
									else if (ricArrRec.getTipoOrdine().equals("R"))
									{
										utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_RILEGATURA, utente.getCodPolo(), utente.getCodBib(), null);
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

							}   catch (UtenteNotAuthorizedException e) {
								//ActionMessages errors = new ActionMessages();
								//errors.add("generico", new ActionMessage("error.authentication.non_abilitato"));
								//this.saveErrors(request, errors);
								ActionMessages errors = new ActionMessages();
								errors.add("generico", new ActionMessage("messaggio.info.noaut"));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
						} // fine gestione vai A
						else
						{
							return mapping.getInputForward();
						}
					}
				}
				else
				{
					//innesto per presentazione maschera di ricerca senza criteriminimi
/*					if (ve.getError()==4224)
					{

					}

*/
					return mapping.getInputForward();
				}

		}
		// altri tipi di errore
    	 catch (UtenteNotAuthorizedException e) {
				// 04.12.08 e.printStackTrace();
				log.error(e);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.authentication.non_abilitato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

    	} catch (Exception e) {

			ActionMessages errors = new ActionMessages();
			//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}

	private void passaCriteri(OrdineRicercaParzialeBoForm ricOrdini, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {
			String chiama=null;
			if (ricOrdini.isLSRicerca())
			{
				ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				chiama=ricArr.getChiamante();
			}

			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricOrdini.getCodiceBibl();
			//List<String> codBAff=ricOrdini.getBiblAffil();
			String codBAff = ricOrdini.getBiblAffil();
			String codOrd=ricOrdini.getNumero();
			//String annoOrd=ricOrdini.;
			String tipoOrd=ricOrdini.getTipoOrdine();
			String dataOrdDa=ricOrdini.getDataOrdineDa();
			String dataOrdA=ricOrdini.getDataOrdineA();
			String cont=ricOrdini.getContinuativo() ;
			//String statoOrd=ricOrdini.getStato();
			String statoOrd=null;
			StrutturaCombo forn=new StrutturaCombo ("","");
			if (ricOrdini.getCodFornitore()!=null && ricOrdini.getCodFornitore().length()>0)
			{
				forn.setCodice(ricOrdini.getCodFornitore());
			}
			if (ricOrdini.getFornitore()!=null && ricOrdini.getFornitore().length()>0)
			{
				forn.setDescrizione(ricOrdini.getFornitore());
			}
			String tipoInvioOrd=ricOrdini.getTipoInvio();
			StrutturaTerna bil=new StrutturaTerna(ricOrdini.getEsercizio(),ricOrdini.getCapitolo(),ricOrdini.getTipoImpegno() );
			String sezioneAcqOrd=ricOrdini.getSezione();
			StrutturaCombo tit=new StrutturaCombo (ricOrdini.getTitolo().getCodice(),ricOrdini.getTitolo().getDescrizione());
			String dataFineAbbOrdDa=ricOrdini.getDataOrdineAbbDa();
			String dataFineAbbOrdA=ricOrdini.getDataOrdineAbbA();
			String naturaOrd=ricOrdini.getNatura();
			//String chiama=null;
			String[] statoOrdArr=ricOrdini.getStatoArr();
			Boolean stamp=ricOrdini.isStampato();
			Boolean rinn=ricOrdini.isRinnovato();
			// prova
			String annoOrd="";

			eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp );
			eleRicerca.setTicket(ticket);
			// IMPOSTAZIONE ORDINAMENTO
			eleRicerca.setOrdinamento("");
			if (ricOrdini.getTipoOrdinamSelez()!=null && !ricOrdini.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricOrdini.getTipoOrdinamSelez());
			}
			eleRicerca.setElemXBlocchi(ricOrdini.getElemXBlocchi());

			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;
			request.getSession().setAttribute("ATTRIBUTEListaSuppOrdiniVO", eleRicerca);

		}catch (Exception e)
		{
			//04.12.08 e.printStackTrace();
			log.error(e);
		}
	}



	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {


			String chiama=null;
			//if (ricOrdini.isLSRicerca())
			if (ricOrdini.isVisibilitaIndietroLS())
			{
				ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				if (ricArr!=null && ricArr.getChiamante()!=null &&  ricArr.getChiamante().trim().length()>0)
				{
					chiama=ricArr.getChiamante();
				}
			}

			if ( ricOrdini.getNumero()!=null && ricOrdini.getNumero().trim().length()!=0   && ricOrdini.getTipoOrdine()!=null && ricOrdini.getTipoOrdine().trim().length()!=0  &&  ricOrdini.getAnnoOrdine()!=null && ricOrdini.getAnnoOrdine().trim().length()!=0    )
			{
				// ripulitura di tutti gli altri campi e disabilitazione (necessario se javascript disattivato)
				ricOrdini.setSezione("");
				ricOrdini.setContinuativo("");
				ricOrdini.setNatura("");
				ricOrdini.getTitolo().setCodice("");
				ricOrdini.getTitolo().setDescrizione("");
				ricOrdini.setFornitore("");
				ricOrdini.setCodFornitore("");
				ricOrdini.setDataOrdineDa("");
				ricOrdini.setDataOrdineA("");
				ricOrdini.setDataStampaOrdineA("");
				ricOrdini.setDataStampaOrdineDa("");
				ricOrdini.setDataOrdineAbbA("");
				ricOrdini.setDataOrdineAbbDa("");
				ricOrdini.setEsercizio("");
				ricOrdini.setCapitolo("");
				ricOrdini.setTipoImpegno("");
				ricOrdini.setStatoArr(new String[0]);
				ricOrdini.setTipoInvio("");
				ricOrdini.setStampato(false);
				ricOrdini.setRinnovato(false);
			}



			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
			String ticket=Navigation.getInstance(request).getUserTicket();
			request.getSession().removeAttribute("ultimoBloccoOrdini");

			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricOrdini.getCodiceBibl();
			//List<String> codBAff=ricOrdini.getBiblAffil();
			String codBAff = ricOrdini.getBiblAffil();
			String codOrd=ricOrdini.getNumero();
			//String annoOrd=ricOrdini.;
			String tipoOrd=ricOrdini.getTipoOrdine();
			String dataOrdDa=ricOrdini.getDataOrdineDa();
			String dataOrdA=ricOrdini.getDataOrdineA();
			String cont=ricOrdini.getContinuativo() ;
			//String statoOrd=ricOrdini.getStato();
			String statoOrd=null;
			StrutturaCombo forn=new StrutturaCombo (ricOrdini.getCodFornitore().trim(),ricOrdini.getFornitore().trim());
			String tipoInvioOrd=ricOrdini.getTipoInvio();
			StrutturaTerna bil=new StrutturaTerna("", "","");
			if (ricOrdini.getEsercizio()!=null && ricOrdini.getEsercizio().trim().length()>0)
			{
				 bil.setCodice1(ricOrdini.getEsercizio().trim());
			}
			if (ricOrdini.getCapitolo()!=null && ricOrdini.getCapitolo().trim().length()>0)
			{
				 bil.setCodice2(ricOrdini.getCapitolo().trim());
			}
			if (ricOrdini.getTipoImpegno()!=null && ricOrdini.getTipoImpegno().trim().length()>0)
			{
				 bil.setCodice3(ricOrdini.getTipoImpegno().trim());
			}

			String sezioneAcqOrd="";
			if (ricOrdini.getSezione()!=null && ricOrdini.getSezione().trim().length()>0)
			{
				 sezioneAcqOrd=ricOrdini.getSezione().trim();
			}

			StrutturaCombo tit=new StrutturaCombo ("","");
			if (ricOrdini.getTitolo()!=null && ricOrdini.getTitolo().getCodice()!=null && ricOrdini.getTitolo().getCodice().trim().length()>0)
			{
				tit.setCodice(ricOrdini.getTitolo().getCodice().trim());
			}
			if (ricOrdini.getTitolo()!=null && ricOrdini.getTitolo().getDescrizione()!=null && ricOrdini.getTitolo().getDescrizione().trim().length()>0)
			{
				tit.setDescrizione(ricOrdini.getTitolo().getDescrizione().trim());
			}

			String dataFineAbbOrdDa=ricOrdini.getDataOrdineAbbDa();
			String dataFineAbbOrdA=ricOrdini.getDataOrdineAbbA();
			String naturaOrd=ricOrdini.getNatura();
			//String chiama=null;
			String[] statoOrdArr=ricOrdini.getStatoArr();
			//Boolean stamp=ricOrdini.isStampato();
			//Boolean rinn=ricOrdini.isRinnovato();
			Boolean stamp=false;
			Boolean rinn=false;
			Boolean ricercaSensibile=false;
			if (ricOrdini.getStampatoStr()!=null && ricOrdini.getStampatoStr().equals("01"))
			{
				ricercaSensibile=true;
				stamp=true;
			}
			if (ricOrdini.getStampatoStr()!=null && ricOrdini.getStampatoStr().equals("00"))
			{
				ricercaSensibile=true;
				stamp=false;
			}
			if (ricOrdini.getRinnovatoStr()!=null && ricOrdini.getRinnovatoStr().equals("01"))
			{
				ricercaSensibile=true;
				rinn=true;
			}
			if (ricOrdini.getRinnovatoStr()!=null && ricOrdini.getRinnovatoStr().equals("00"))
			{
				ricercaSensibile=true;
				rinn=false;
			}


			// prova
			String annoOrd=ricOrdini.getAnnoOrdine();
			String dataStampaOrdDa=ricOrdini.getDataStampaOrdineDa();
			String dataStampaOrdA=ricOrdini.getDataStampaOrdineA();

			eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp );
			eleRicerca.setTicket(ticket);
			eleRicerca.setDataStampaOrdineDa(dataStampaOrdDa);
			eleRicerca.setDataStampaOrdineA(dataStampaOrdA);
			eleRicerca.setDenoBiblStampe(ricOrdini.getDenoBibl());

			if (ricercaSensibile)
			{
				// ATTIVATO DA RICERCA per la gestione dei boolean stampato e rinnovato
				eleRicerca.setAttivatoDaRicerca(true);
			}

			// ATTIVATO DA RICERCA per la gestione dei boolean stampato e rinnovato
			eleRicerca.setAttivatoDaRicerca(true);
			// filtro gestione stampato e rinnovato da escludere se ricerca puntuale
			if ( ricOrdini.getNumero()!=null && ricOrdini.getNumero().trim().length()!=0   && ricOrdini.getTipoOrdine()!=null && ricOrdini.getTipoOrdine().trim().length()!=0  &&  ricOrdini.getAnnoOrdine()!=null && ricOrdini.getAnnoOrdine().trim().length()!=0    )
			{
				eleRicerca.setAttivatoDaRicerca(false);

			}
/*			else if ((ricOrdini.getStampatoStr()!=null && ricOrdini.getStampatoStr().equals("")) || (ricOrdini.getRinnovatoStr()!=null && ricOrdini.getRinnovatoStr().equals("")))
			{
				eleRicerca.setAttivatoDaRicerca(false);
			}
*/
			if ((ricOrdini.getStampatoStr()!=null && !ricOrdini.getStampatoStr().equals("")) )
			{
				eleRicerca.setStampatoStr(ricOrdini.getStampatoStr());
			}
			if ((ricOrdini.getRinnovatoStr()!=null && !ricOrdini.getRinnovatoStr().equals("")))
			{
				eleRicerca.setRinnovatoStr(ricOrdini.getRinnovatoStr());
			}

			// IMPOSTAZIONE ORDINAMENTO
			eleRicerca.setOrdinamento("");
			if (ricOrdini.getTipoOrdinamSelez()!=null && !ricOrdini.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricOrdini.getTipoOrdinamSelez());
			}
			eleRicerca.setElemXBlocchi(ricOrdini.getElemXBlocchi());

			// IMPOSTAZIONE ATTRIBUTO DI SESSIONE ;

			request.setAttribute("passaggioListaSuppOrdiniVO", eleRicerca);
			// controllo di esistenza risultati su db se ci sono eccezioni
			List<OrdiniVO> listaOrdini;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	/*		factory.getGestioneAcquisizioni().ValidaPreRicercaOrdini(eleRicerca);
			listaOrdini=this.getListaOrdiniVO(eleRicerca);
			return mapping.findForward("cerca");
	*/

			int numRis=factory.getGestioneAcquisizioni().ValidaPreRicercaOrdini(eleRicerca);
			if (numRis>0) // solo se non è andato in eccezione per troppi risultati
			{
				return mapping.findForward("cerca");
			}
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.assenzaRisultati"));
			this.saveErrors(request, errors);
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
			errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}


	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {
			this.passaCriteri( ricOrdini, request); // imposta il crea con i valori cercati
			ListaSuppOrdiniVO passaACrea=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);

			if (ricOrdini.isProvenienzaVAIA() && passaACrea!=null)
			{
				if (passaACrea.getTitolo()!=null && passaACrea.getTitolo().getCodice()!=null && passaACrea.getTitolo().getCodice().length()!=0 && passaACrea.getTitolo().getDescrizione()!=null && passaACrea.getTitolo().getDescrizione().length()!=0 && passaACrea.getNaturaOrdine()!=null && passaACrea.getNaturaOrdine().length()!=0 )
					request.getSession().setAttribute("bid",passaACrea.getTitolo().getCodice());
					request.getSession().setAttribute("desc",passaACrea.getTitolo().getDescrizione());
					request.getSession().setAttribute("natura",passaACrea.getNaturaOrdine());
					// pulizia dei criteri da ricercare
					request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			}
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();

			if (ricOrdini.getTipoOrdine().equals("A"))
			{
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA, utente.getCodPolo(), utente.getCodBib(), null);
				//Acquisto
				return mapping.findForward("nuovoA");
			}
			else if (ricOrdini.getTipoOrdine().equals("L"))
			{
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO, utente.getCodPolo(), utente.getCodBib(), null);
				//Deposito Legale
				return mapping.findForward("nuovoL");
			}
			else if (ricOrdini.getTipoOrdine().equals("D"))
			{
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO, utente.getCodPolo(), utente.getCodBib(), null);
				//Dono
				return mapping.findForward("nuovoD");
			}
			else if (ricOrdini.getTipoOrdine().equals("C"))
			{
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_SCAMBIO, utente.getCodPolo(), utente.getCodBib(), null);
				//Scambio
				return mapping.findForward("nuovoC");
			}
			else if (ricOrdini.getTipoOrdine().equals("V"))
			{
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA, utente.getCodPolo(), utente.getCodBib(), null);
				//Visione Trattenuta
				return mapping.findForward("nuovoV");
			}
			else if (ricOrdini.getTipoOrdine().equals("R"))
			{
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_RILEGATURA, utente.getCodPolo(), utente.getCodBib(), null);
				//Visione Trattenuta
				return mapping.findForward("nuovoR");
			}

			else
			{
				//DEFAULT SU ACQUISTO
				return mapping.findForward("nuovoA");
			}
			//return mapping.findForward("crea");
    	} catch (UtenteNotAuthorizedException e) {
			// 04.12.08 e.printStackTrace();
			log.error(e);

			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.authentication.non_abilitato"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE)==null
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			request.getSession().removeAttribute("sezioniSelected");
			request.getSession().removeAttribute("criteriRicercaSezione");

/*			if (request.getSession().getAttribute("criteriRicercaSezione")==null )
			{
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");

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
			this.impostaSezioneCerca( ricOrdini, request,mapping);
			//return mapping.findForward("sezioneCerca");
			return mapping.findForward("sezioneLista");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaSezioneCerca(OrdineRicercaParzialeBoForm ricOrdini,  HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricOrdini.getCodiceBibl();
		String codSez=ricOrdini.getSezione();
		String desSez="";
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
	}catch (Exception e) {	}
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, ricOrdini);
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
			this.impostaFornitoreCerca( ricOrdini, request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( OrdineRicercaParzialeBoForm ricOrdini, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricOrdini.getCodiceBibl();
		String codForn=ricOrdini.getCodFornitore();
		String nomeForn=ricOrdini.getFornitore();
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


	public ActionForward bilancioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppBilancioVO")==null
			request.getSession().removeAttribute("attributeListaSuppBilancioVO");
			request.getSession().removeAttribute("bilanciSelected");
			request.getSession().removeAttribute("criteriRicercaBilancio");

/*			if (request.getSession().getAttribute("criteriRicercaBilancio")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppBilancioVO");
				request.getSession().removeAttribute("bilanciSelected");
				request.getSession().removeAttribute("criteriRicercaBilancio");

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
			this.impostaBilancioCerca( ricOrdini, request,mapping);
			return mapping.findForward("bilancioCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppBilancioVO")==null
/*			request.getSession().removeAttribute("attributeListaSuppBiblioVO");
			this.impostaBiblioCerca( ricOrdini, request,mapping);
			return mapping.findForward("biblioCerca");
*/

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),
	utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_ORDINI, 10, "codBibDalista");
	        //   GA_INTERROGAZIONE_ORDINI
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaBiblioCerca(OrdineRicercaParzialeBoForm ricOrdini,  HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppBiblioVO eleRicerca=new ListaSuppBiblioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricOrdini.getCodiceBibl();
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppBiblioVO(codP,  codB, chiama, ordina);
		request.getSession().setAttribute("attributeListaSuppBiblioVO", eleRicerca);

	}catch (Exception e) {	}
	}

	private void impostaBilancioCerca(OrdineRicercaParzialeBoForm ricOrdini,  HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricOrdini.getCodiceBibl();
		String ese=ricOrdini.getEsercizio();
		String cap=ricOrdini.getCapitolo();
		String imp=ricOrdini.getTipoImpegno();
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppBilancioVO", eleRicerca);

	}catch (Exception e) {	}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			if (ricArr!=null )
			{
				//ListaSuppCambioVO eleRicArr=ricArr.get(0);
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null && !ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))
				{
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.getInputForward();
				}
			}
			else
			{
				return mapping.getInputForward();
			}
			//return Navigation.getInstance(request)..goBack();
			//return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	private List<OrdiniVO> getListaOrdiniVO(ListaSuppOrdiniVO criRicerca) throws Exception
	{
	List<OrdiniVO> listaOrdini;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaOrdini = factory.getGestioneAcquisizioni().getRicercaListaOrdini(criRicerca);
	//this.sinCambio.setListaCambi(listaCambi);
	//DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) factory.getGestioneAcquisizioni().getRicercaListaOrdini(criRicerca);
	//listaOrdini =(List<OrdiniVO>)blocco1.getLista();
	return listaOrdini;
	}


	private void loadContinuativo(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo  elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("01","Si");
		lista.add(elem);
		elem = new StrutturaCombo("00","No");
		lista.add(elem);
		ricOrdini.setListaContinuativo(lista);
	}
    private void loadTipoInvio(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricOrdini.setListaTipoInvio(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoInvio()));
	}

    private void loadTipoImpegno(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	ricOrdini.setListaTipoImpegno(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale()));
	}

	private void loadStato(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricOrdini.setListaStato(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoOrdine()));

/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - Aperto");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - Chiuso");
		lista.add(elem);
		elem = new StrutturaCombo("I","I - Spedito");
		lista.add(elem);
		elem = new StrutturaCombo("N","N - Annullato");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - Rinnovato");
		lista.add(elem);

		ricOrdini.setListaStato(lista);
*/
	}

	private void loadTipoOrdine(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricOrdini.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));
/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("A","Acquisto");
		lista.add(elem);
		elem = new StrutturaCombo("L","Deposito Legale");
		lista.add(elem);
		elem = new StrutturaCombo("D","Dono");
		lista.add(elem);
		elem = new StrutturaCombo("C","Scambio");
		lista.add(elem);
		elem = new StrutturaCombo("V","Visione Trattenuta");
		lista.add(elem);

		ricOrdini.setListaTipoOrdine(lista);
*/
	}

    private void loadNatura(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricOrdini.setListaNatura(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceNaturaOrdine()));

/*		List arrListaNatura=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceNatura());

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);


		for (int j=0;  j < arrListaNatura.size(); j++)
		{
			ComboCodDescVO eleNat=(ComboCodDescVO)arrListaNatura.get(j);
			if (eleNat.getCodice().equals("C") || eleNat.getCodice().equals("M") || eleNat.getCodice().equals("S"))
			{
				elem = new StrutturaCombo(eleNat.getCodice(), eleNat.getCodice() +" - " + eleNat.getDescrizione());
				lista.add(elem);
			}
		}
		ricOrdini.setListaNatura(lista);
*/


/*    	List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("D","D - Altro Titolo");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - Collezione");
		lista.add(elem);
		elem = new StrutturaCombo("M","M - Monografia");
		lista.add(elem);
		elem = new StrutturaCombo("S","S - Pubblicazione in serie");
		lista.add(elem);
		elem = new StrutturaCombo("N","N - Titolo Analitico ");
		lista.add(elem);
		elem = new StrutturaCombo("A","A - Titolo di raggruppamento");
		lista.add(elem);
		elem = new StrutturaCombo("P","P - Titolo parallelo");
		lista.add(elem);
		elem = new StrutturaCombo("T","T - Titolo subordinato");
		lista.add(elem);
		elem = new StrutturaCombo("W","W - Volume senza titolo");
		lista.add(elem);
		ricOrdini.setListaNatura(lista);
*/	}

	private void loadBiblAffil(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","     ");
		lista.add(elem);
		elem = new StrutturaCombo("00","Tutte");
		lista.add(elem);
		List<StrutturaCombo> listaBiblAff = new ArrayList<StrutturaCombo>();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//listaBiblAff = (List<StrutturaCombo>) factory.getGestioneAcquisizioni().getRicercaBiblAffiliate(ricOrdini.getCodiceBibl(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_ORDINI);
		if (listaBiblAff!=null && listaBiblAff.size()!=0)
		{
			for (int j=0;  j < listaBiblAff.size(); j++)
			{
				elem = listaBiblAff.get(j);
				lista.add(elem);
			}
		}
		ricOrdini.setListaBiblAffil(lista);

		/*
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("00","Tutte");
		lista.add(elem);
		elem = new StrutturaCombo("01","Biblioteca di Prova");
		lista.add(elem);
		elem = new StrutturaCombo("02","Biblioteca Storia");
		lista.add(elem);

		ricOrdini.setListaBiblAffil(lista);
*/
	}

	private void caricaAttributeListaSupp(OrdineRicercaParzialeBoForm ricOrdini, ListaSuppOrdiniVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		ricOrdini.setCodiceBibl(ricArr.getCodBibl());
		//ricOrdini.setListaBiblAffil(ricArr.getCodBiblAffil());
		//List<String> codBAff=ricOrdini.getBiblAffil();
		//ricOrdini.setListaBiblAffil(null);
		if (ricArr.getCodBiblAffil()!=null &&  ricArr.getCodBiblAffil().trim().length()>0)
		{
			ricOrdini.setBiblAffil(ricArr.getCodBiblAffil());
		}
		if (ricArr.getFornitore()!=null &&  ricArr.getFornitore().getCodice()!=null &&  ricArr.getFornitore().getCodice().trim().length()>0)
		{
			ricOrdini.setCodFornitore(ricArr.getFornitore().getCodice());
		}
		if (ricArr.getTipoInvioOrdine()!=null &&  ricArr.getTipoInvioOrdine().trim().length()>0)
		{
			ricOrdini.setTipoInvio(ricArr.getTipoInvioOrdine());
		}
		if (ricArr.getBilancio()!=null && ricArr.getBilancio().getCodice1()!=null && ricArr.getBilancio().getCodice1().trim().length()>0)
		{
			ricOrdini.setEsercizio(ricArr.getBilancio().getCodice1());
		}
		if (ricArr.getBilancio()!=null && ricArr.getBilancio().getCodice2()!=null && ricArr.getBilancio().getCodice2().trim().length()>0)
		{
			ricOrdini.setCapitolo(ricArr.getBilancio().getCodice2());
		}
		if (ricArr.getBilancio()!=null && ricArr.getBilancio().getCodice3()!=null && ricArr.getBilancio().getCodice3().trim().length()>0)
		{
			ricOrdini.setTipoImpegno(ricArr.getBilancio().getCodice3());
		}
		if (ricArr.getContinuativo()!=null && ricArr.getContinuativo().trim().length()>0)
		{
			ricOrdini.setContinuativo(ricArr.getContinuativo());
		}
		if (ricArr.getDataOrdineA()!=null && ricArr.getDataOrdineA().trim().length()>0)
		{
			ricOrdini.setDataOrdineA(ricArr.getDataOrdineA());
		}
		if (ricArr.getDataFineAbbOrdineA()!=null && ricArr.getDataFineAbbOrdineA().trim().length()>0)
		{
			ricOrdini.setDataOrdineAbbA(ricArr.getDataFineAbbOrdineA());
		}
		if (ricArr.getDataFineAbbOrdineDa()!=null && ricArr.getDataFineAbbOrdineDa().trim().length()>0)
		{
			ricOrdini.setDataOrdineAbbDa(ricArr.getDataFineAbbOrdineDa());
		}
		if (ricArr.getDataOrdineDa()!=null && ricArr.getDataOrdineDa().trim().length()>0)
		{
			ricOrdini.setDataOrdineDa(ricArr.getDataOrdineDa());
		}
		if (ricArr.getNaturaOrdine()!=null && ricArr.getNaturaOrdine().trim().length()>0)
		{
			ricOrdini.setNatura(ricArr.getNaturaOrdine());
		}
		if (ricArr.getTipoOrdine()!=null && ricArr.getTipoOrdine().trim().length()>0)
		{
			ricOrdini.setTipoOrdine(ricArr.getTipoOrdine());
		}
		if (ricArr.getAnnoOrdine()!=null && ricArr.getAnnoOrdine().trim().length()>0)
		{
			ricOrdini.setAnnoOrdine(ricArr.getAnnoOrdine());
		}
		if (ricArr.getCodOrdine()!=null && ricArr.getCodOrdine().trim().length()>0)
		{
			ricOrdini.setNumero(ricArr.getCodOrdine());
		}
		if (ricArr.getStatoOrdine()!=null && ricArr.getStatoOrdine().trim().length()>0)
		{
			ricOrdini.setStato(ricArr.getStatoOrdine());
		}
		if (ricArr.getSezioneAcqOrdine()!=null && ricArr.getSezioneAcqOrdine().trim().length()>0)
		{
			ricOrdini.setSezione(ricArr.getSezioneAcqOrdine());
		}
		if (ricArr.getCodOrdine()!=null && ricArr.getCodOrdine().trim().length()>0)
		{
			ricOrdini.setNumero(ricArr.getCodOrdine());
		}
		if (ricArr.getFornitore()!=null &&  ricArr.getFornitore().getDescrizione()!=null &&  ricArr.getFornitore().getDescrizione().trim().length()>0)
		{
			ricOrdini.setFornitore(ricArr.getFornitore().getDescrizione());
		}
		if (ricArr.getStatoOrdineArr()!=null && ricArr.getStatoOrdineArr().length >0)
		{
			ricOrdini.setStatoArr(ricArr.getStatoOrdineArr());
		}
		//StrutturaCombo tit=new StrutturaCombo ("","");
		//String chiama="/acquisizioni/cambi/cambiRicercaParziale";;
		// prova
	}catch (Exception e) {
		// 04.12.08 e.printStackTrace();
		log.error(e);

		}

	}

	private void caricaParametroTest(OrdineRicercaParzialeBoForm ricOrdini, HttpServletRequest request, ActionMapping mapping)
	{
	//simulazione di una lista di supporto richiamata da cambiricercaparziale con codvaluta=eur
	try {
		//List<ListaSuppCambioVO> ricerca=new ArrayList();

		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=Navigation.getInstance(request).getUtente().getCodBib();
		//List<String> codBAff=ricOrdini.getBiblAffil();
		String codBAff = null;
		String codOrd="11";
		//String annoOrd=ricOrdini.;
		String tipoOrd="V"; // A
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		String tipoInvioOrd=null;
		StrutturaTerna bil=new StrutturaTerna("","","" );
		String sezioneAcqOrd=null;
		StrutturaCombo tit=new StrutturaCombo ("","");
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd=null;
		String chiama="/acquisizioni/cambi/cambiRicercaParziale";;
		// prova
		String annoOrd="";
		String[] statoOrdArr=ricOrdini.getStatoArr();
		Boolean stamp=false;
		Boolean rinn=false;
		eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn, stamp);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);

	}catch (Exception e) {	}
	}

	private ListaSuppOrdiniVO caricaParametriVAIA(OrdineRicercaParzialeBoForm ricOrdini, String bid, String desc, String natura)
	{
	try {

		// carica i criteri di ricerca da passare alla esamina
		String codP="";
		String codB="";
		//List<String> codBAff=ricOrdini.getBiblAffil();
		String codBAff=null;
		String codOrd=null;
		//String annoOrd=ricOrdini.;
		String tipoOrd=null; // A
		String dataOrdDa=null;
		String dataOrdA=null;
		String cont=null;
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		String tipoInvioOrd=null;
		StrutturaTerna bil=new StrutturaTerna("","","" );
		String sezioneAcqOrd=null;
		StrutturaCombo tit=new StrutturaCombo (bid,desc);
		String dataFineAbbOrdDa=null;
		String dataFineAbbOrdA=null;
		String naturaOrd=natura;
		//String chiama="";
		String chiama="/gestionebibliografica/titolo/analiticaTitolo";
		// prova
		String annoOrd="";
		String[] statoOrdArr=ricOrdini.getStatoArr();
		Boolean stamp=false;
		Boolean rinn=false;
		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr,rinn,stamp);
		return	eleRicerca;

	}catch (Exception e) {return null;	}
	}
	private void loadTipoOrdinamento(OrdineRicercaParzialeBoForm ricOrdini) throws Exception {
		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		//elem = new StrutturaCombo("1","TIPO - NUM ORDINE");
		//lista.add(elem);
		StrutturaCombo elem = new StrutturaCombo("2","Anno- Tipo - Num.ordine(disc)");
		lista.add(elem);
		elem = new StrutturaCombo("3","Nome fornitore");
		lista.add(elem);
		elem = new StrutturaCombo("4","Data (disc)");
		lista.add(elem);
		elem = new StrutturaCombo("5","Stato");
		lista.add(elem);
		if (ricOrdini.isGestBil())
		{
			elem = new StrutturaCombo("6","Bilancio");
			lista.add(elem);
		}


		ricOrdini.setListaTipiOrdinam(lista);
	}

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		try {
			request.setAttribute("bidFromRicOrd", ricOrdini.getTitolo().getCodice());
			return Navigation.getInstance(request).goForward(mapping.findForward("sifbid"));
			}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		OrdineRicercaParzialeBoForm ricOrdini = (OrdineRicercaParzialeBoForm) form;
		if (!ricOrdini.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
				ricOrdini.setTipoOrdine((String) utenteEjb.getDefault(ConstantDefault.ACQ_TIPO_ORDINE));

			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.acquisizioni.erroreDefault"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
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


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_ORDINI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				// 04.12.08 e.printStackTrace();
				log.error(e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
*/		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				//04.12.08 e.printStackTrace();
				log.error(e);

				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}


		return false;
	}

}


