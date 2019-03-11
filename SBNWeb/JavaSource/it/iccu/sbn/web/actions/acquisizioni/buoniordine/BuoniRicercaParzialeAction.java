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
package it.iccu.sbn.web.actions.acquisizioni.buoniordine;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO.TipoOperazione;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;
import it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.BuoniRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.LookupDispatchAction;

public class BuoniRicercaParzialeAction extends LookupDispatchAction implements SbnAttivitaChecker{

	//private BuoniRicercaParzialeForm ricBuoni;


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.ordine","ordineCerca");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_BUONI_ORDINE, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 	{
		BuoniRicercaParzialeForm currentForm = (BuoniRicercaParzialeForm) form;
		try {

			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
	            return mapping.getInputForward();

			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.buoniordine") && (currentForm.getCodBibl()==null || (currentForm.getCodBibl()!=null && currentForm.getCodBibl().trim().length()==0)) && navi.getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
				request.getSession().removeAttribute("buoniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE);
*/			}

			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=navi.getUtente().getCodBib();
			if (biblio!=null && (currentForm.getCodBibl()==null  || (currentForm.getCodBibl()!=null && currentForm.getCodBibl().trim().length()==0)))
			{
				currentForm.setCodBibl(biblio);
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				currentForm.setCodBibl(bibScelta.getCod_bib());
			}
			String polo=navi.getUtente().getCodPolo();
			currentForm.setCodPolo(polo);

			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(currentForm.getCodBibl());
			configurazioneCriteri.setCodPolo(currentForm.getCodPolo());
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
			Boolean gestBil =true;
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneBilancio())
			{
				gestBil =configurazioneLetta.isGestioneBilancio();
			}

			if (gestBil!=null && !gestBil)
			{
				currentForm.setGestBil(false);
			}

			this.loadTipoOrdinamento( currentForm);
			this.loadTipoOrdine( currentForm);

			// condizioni di ricerca univoca
			if ( currentForm.getNumBuonoA()!=null && currentForm.getNumBuonoDa()!=null && currentForm.getNumBuonoA().trim().length()!=0 && currentForm.getNumBuonoDa().trim().length()!=0 && currentForm.getNumBuonoDa().trim().equals(currentForm.getNumBuonoA().trim()) )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				currentForm.setTipoOrdine("");
				currentForm.setAnno("");
				currentForm.setNumero("");
				currentForm.setEsercizio("");
				currentForm.setCapitolo("");
				currentForm.setFornitore ("");
				currentForm.setCodFornitore("");
				currentForm.setDataBuonoDa("");
				currentForm.setDataBuonoA("");
				currentForm.setDisabilitaTutto(true);

			}
			else
			{
				currentForm.setDisabilitaTutto(false);
			}



			/*
			// test inizio per simulare l'accesso a lista supporto cambi richiamato dalla pagina cambiricercaparziale
			if (request.getSession().getAttribute("attributeListaSuppBilancioVO")==null)
			{
				this.caricaParametroTest(request, mapping);
			}
			// test fine

			*/
			ListaSuppBuoniOrdineVO ricerca=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);

			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						currentForm.setCodFornitore(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						currentForm.setFornitore(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
						currentForm.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					currentForm.setCodFornitore("");
					currentForm.setFornitore("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");
 			}
			//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
			ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))
 			{
				if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 )
				{
					if (ricOrd.getSelezioniChiamato().get(0).getChiave()!=null && ricOrd.getSelezioniChiamato().get(0).getChiave().length()!=0 )
					{
						currentForm.setTipoOrdine(ricOrd.getSelezioniChiamato().get(0).getTipoOrdine());
						currentForm.setAnno(ricOrd.getSelezioniChiamato().get(0).getAnnoOrdine());
						currentForm.setNumero(ricOrd.getSelezioniChiamato().get(0).getCodOrdine());
						currentForm.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					currentForm.setTipoOrdine("");
					currentForm.setAnno("");
					currentForm.setNumero("");

				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

 			}




	/*	//controllo se ho un risultato di una lista di supporto richiamata da questa pagina (risultato della simulazione)
			if (ricArr!=null && ricArr.size()!=0 && ricArr.get(0).getSelezioniChiamato()!=null)
			{
				if (ricArr.size()>0)
				{
					this.ricCambi.setValuta(ricArr.get(0).getCodValuta());
				}

			}
			else
			{
				this.ricCambi.setValuta("");
			}
	*/
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricerca!=null &&  ricerca.getSelezioniChiamato()==null && ricerca.getChiamante()!=null)
			{
				// per il layout
				currentForm.setVisibilitaIndietroLS(true);
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
				if (ricerca.getChiamante().equals("/acquisizioni/buoniordine/esaminaOrdineMod"))
				{
					currentForm.setLSRicerca(true); // fai rox 2
				}

				//almaviva5_20140624 #4631
				currentForm.setParametri(ricerca);

					//}
				// per il layout fine
				if (ricerca.isModalitaSif())
				{
					List<BuoniOrdineVO> listaBuoniOrdine = this.getListaBuoniOrdineVO(ricerca); // va in errore se non ha risultati
					this.caricaAttributeListaSupp(currentForm,ricerca); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.findForward("cerca");
				}
				else
				{
					if (!currentForm.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
					{
						this.caricaAttributeListaSupp(currentForm,ricerca); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					}
					else
					{
						currentForm.setRientroDaSif(false);
					}
					return mapping.getInputForward();
				}



			}
			else
			{
				return mapping.getInputForward();
			}

		}	catch (ValidationException ve) {
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppBuoniOrdineVO ricArrRec=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
					this.caricaAttributeListaSupp(currentForm,ricArrRec);
					currentForm.setVisibilitaIndietroLS(true);
					//return mapping.getInputForward();
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && ricArrRec.getChiamante().endsWith("RicercaParziale"))
					if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null )
					{
		 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
						// in tal caso la ricerca senza esito
						// non deve automaticamente presentare la maschera di crea ma emettere il messaggio

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

						//return mapping.getInputForward();
					}
					return mapping.getInputForward();

/*					else
					{
						// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
						// in tal caso la ricerca senza esito
						// deve automaticamente presentare la maschera di crea
						this.passaCriteri( ricBuoni, request); // imposta il crea con i valori cercati
						return mapping.findForward("crea");
					}
*/
				}
				else
				{
					return mapping.getInputForward();
				}

		}
		// altri tipi di errore
		catch (Exception e) {

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}
	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		BuoniRicercaParzialeForm currentForm = (BuoniRicercaParzialeForm) form;
		try {
			String chiama=null;
			//if (ricBuoni.isLSRicerca())
			HttpSession session = request.getSession();
			if (currentForm.isVisibilitaIndietroLS())
			{
				ListaSuppBuoniOrdineVO ricArr=(ListaSuppBuoniOrdineVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
				if (ricArr != null)
					chiama=ricArr.getChiamante();
			}

			// condizioni di ricerca univoca
			if ( currentForm.getNumBuonoA()!=null && currentForm.getNumBuonoDa()!=null && currentForm.getNumBuonoA().trim().length()!=0 && currentForm.getNumBuonoDa().trim().length()!=0 && currentForm.getNumBuonoDa().trim().equals(currentForm.getNumBuonoA().trim()) )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				currentForm.setTipoOrdine("");
				currentForm.setAnno("");
				currentForm.setNumero("");
				currentForm.setEsercizio("");
				currentForm.setCapitolo("");
				currentForm.setFornitore ("");
				currentForm.setCodFornitore("");
				currentForm.setDataBuonoDa("");
				currentForm.setDataBuonoA("");
			}


			Navigation navi = Navigation.getInstance(request);
			String ticket = navi.getUserTicket();
			session.removeAttribute("ultimoBloccoBO");
			// carica i criteri di ricerca da passare alla esamina
			String codP = navi.getUtente().getCodPolo();
			String codB = currentForm.getCodBibl();
			String numDa = currentForm.getNumBuonoDa();
			String numA = currentForm.getNumBuonoA();
			String dataDa = currentForm.getDataBuonoDa();
			String dataA = currentForm.getDataBuonoA();
			String stato = "";
			StrutturaTerna ord=new StrutturaTerna(currentForm.getTipoOrdine(),currentForm.getAnno(),currentForm.getNumero());
			StrutturaCombo forn=new StrutturaCombo(currentForm.getCodFornitore(),currentForm.getFornitore());
			StrutturaTerna bil=new StrutturaTerna(currentForm.getEsercizio(),currentForm.getCapitolo(),"");
			//String chiama=null;
			String ordina=currentForm.getTipoOrdinamSelez();

			ListaSuppBuoniOrdineVO ricerca = new ListaSuppBuoniOrdineVO(codP,
					codB, numDa, numA, dataDa, dataA, stato, ord, forn, bil,
					chiama, ordina);
			ricerca.setTicket(ticket);
			session.setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE, ricerca);

			ricerca.setElemXBlocchi(currentForm.getElemXBlocchi());
			ricerca.setOrdinamento("");
			if (currentForm.getTipoOrdinamSelez()!=null && !currentForm.getTipoOrdinamSelez().equals(""))
			{
				ricerca.setOrdinamento(currentForm.getTipoOrdinamSelez());
			}
			// obbligo di almeno un criterio di ricerca impostato
			boolean criteriMinimiEsistenza=false;

			if (ricerca.getDataBuonoOrdineDa()!=null && ricerca.getDataBuonoOrdineDa().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}
			if (ricerca.getDataBuonoOrdineA()!=null && ricerca.getDataBuonoOrdineA().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}

			if (ricerca.getBilancio()!=null && ricerca.getBilancio().getCodice1()!=null && ricerca.getBilancio().getCodice1().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}
			if (ricerca.getBilancio()!=null && ricerca.getBilancio().getCodice2()!=null && ricerca.getBilancio().getCodice2().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}

			if (ricerca.getNumBuonoOrdineDa()!=null &&  ricerca.getNumBuonoOrdineDa().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}
			if (ricerca.getNumBuonoOrdineA()!=null &&  ricerca.getNumBuonoOrdineA().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}

			if (ricerca.getFornitore()!=null && ricerca.getFornitore().getCodice()!=null &&  ricerca.getFornitore().getCodice().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}
			//
			if (ricerca.getFornitore()!=null && ricerca.getFornitore().getDescrizione()!=null && ricerca.getFornitore().getDescrizione().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}

			// tipo ordine
			if (ricerca.getOrdine()!=null && ricerca.getOrdine().getCodice1()!=null && ricerca.getOrdine().getCodice1().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}
			// anno ord
			if (ricerca.getOrdine()!=null && String.valueOf(ricerca.getOrdine().getCodice2()).length()!= 0)
			{
				criteriMinimiEsistenza=true;
			}
			// codice ordine
			if (ricerca.getOrdine()!=null && ricerca.getOrdine().getCodice3()!=null &&  ricerca.getOrdine().getCodice3().length()!=0)
			{
				criteriMinimiEsistenza=true;
			}
			if (!criteriMinimiEsistenza)
			{

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ricercaDaRaffinare"));

				return mapping.getInputForward();
			}

			//almaviva5_20140624 #4631
			TipoOperazione op = currentForm.getParametri().getTipoOperazione();
			switch (op) {
			case CERCA_DA_ORDINE:
				ricerca.setTipoOperazione(TipoOperazione.SCEGLI_DA_ORDINE);
				break;
			default:
				ricerca.setTipoOperazione(op);
			}

			// fine obbligo
			List<BuoniOrdineVO> listaBuoniOrdine = this.getListaBuoniOrdineVO(ricerca);
			return mapping.findForward("cerca");

		}	catch (ValidationException ve) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		BuoniRicercaParzialeForm currentForm = (BuoniRicercaParzialeForm) form;
		try {
			this.passaCriteri(currentForm, request);
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void passaCriteri(BuoniRicercaParzialeForm currentForm, HttpServletRequest request) throws Exception {
		// caricamento dei criteri di ricerca per il crea

		String chiama=null;
		if (currentForm.isLSRicerca())
		{
			ListaSuppBuoniOrdineVO ricArr=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			chiama=ricArr.getChiamante();
		}

		ListaSuppBuoniOrdineVO ricerca = null;
		//almaviva5_20140624 #4631
		TipoOperazione op = currentForm.getParametri().getTipoOperazione();
		Navigation navi = Navigation.getInstance(request);
		switch (op) {
		case CERCA_DA_ORDINE:
			ricerca = currentForm.getParametri().copy();
			ricerca.setTipoOperazione(TipoOperazione.CREA_DA_ORDINE);
			//navi.purgeThis();
			break;

		default:
			// carica i criteri di ricerca da passare alla esamina
			String codP=navi.getUtente().getCodPolo();
			String codB=currentForm.getCodBibl();
			StrutturaTerna ord = new StrutturaTerna(currentForm.getTipoOrdine(), currentForm.getAnno(),	currentForm.getNumero());
			StrutturaCombo forn = new StrutturaCombo(currentForm.getCodFornitore(), currentForm.getFornitore());
			StrutturaTerna bil = new StrutturaTerna(currentForm.getEsercizio(),	currentForm.getCapitolo(), "");
			//String chiama=null;
			ricerca = new ListaSuppBuoniOrdineVO(codP, codB, "", "", "", "", "", ord, forn, bil, chiama, "" );
			//eleRicerca.setTicket(ticket);
			ricerca.setElemXBlocchi(currentForm.getElemXBlocchi());
			ricerca.setOrdinamento("");
		}

		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE, ricerca);
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBuoniOrdineVO ricerca = (ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			if (ricerca!=null )
			{
				// gestione del chiamante
				if (ricerca!=null && ricerca.getChiamante()!=null)
				{
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricerca.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.getInputForward();
				}
			}
			else
			{
				return Navigation.getInstance(request).goBack(true);
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private List<BuoniOrdineVO> getListaBuoniOrdineVO(ListaSuppBuoniOrdineVO criRicerca) throws Exception
		{
		List<BuoniOrdineVO> listaBuoniOrdine;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaBuoniOrdine = factory.getGestioneAcquisizioni().getRicercaListaBuoniOrd(criRicerca);
		return listaBuoniOrdine;
		}

		private void loadTipoOrdine(BuoniRicercaParzialeForm ricBuoni) throws Exception {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo carCombo = new CaricamentoCombo();
			ricBuoni.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));
		}

		private void caricaAttributeListaSupp(BuoniRicercaParzialeForm ricBuoni, ListaSuppBuoniOrdineVO  ricArr)
		{
		//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
		try {
			ricBuoni.setCodBibl(ricArr.getCodBibl());
			ricBuoni.setDataBuonoDa(ricArr.getDataBuonoOrdineDa());
			ricBuoni.setDataBuonoA(ricArr.getDataBuonoOrdineA());
			ricBuoni.setNumBuonoDa(ricArr.getNumBuonoOrdineDa());
			ricBuoni.setNumBuonoA(ricArr.getNumBuonoOrdineA());
			ricBuoni.setTipoOrdine(ricArr.getOrdine().getCodice1());
			ricBuoni.setAnno(ricArr.getOrdine().getCodice2());
			ricBuoni.setNumero(ricArr.getOrdine().getCodice3());
			ricBuoni.setEsercizio(ricArr.getBilancio().getCodice1());
			ricBuoni.setCapitolo(ricArr.getBilancio().getCodice2());
			ricBuoni.setCodFornitore(ricArr.getFornitore().getCodice());
			ricBuoni.setFornitore(ricArr.getFornitore().getDescrizione());

		}catch (Exception e) {	}
		}

		public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			BuoniRicercaParzialeForm ricBuoni = (BuoniRicercaParzialeForm) form;
			try {
				ActionForward forward = fornitoreCercaVeloce(mapping, request, ricBuoni);
				if (forward != null){
					return forward;
				}
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null

				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

/*				if (request.getSession().getAttribute("criteriRicercaFornitore")==null )
				{
					request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
					request.getSession().removeAttribute("fornitoriSelected");
					request.getSession().removeAttribute("criteriRicercaFornitore");

				}
				else
				{
					//throw new Exception("limite di ricorsione");

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));

					return mapping.getInputForward();
				}
*/

				this.impostaFornitoreCerca( ricBuoni,request,mapping);
				return mapping.findForward("fornitoreCerca");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		private void impostaFornitoreCerca(BuoniRicercaParzialeForm ricBuoni, HttpServletRequest request, ActionMapping mapping)
		{
		//impostazione di una lista di supporto
		try {
			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricBuoni.getCodBibl();
			String codForn=ricBuoni.getCodFornitore();
			String nomeForn=ricBuoni.getFornitore();
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
			BuoniRicercaParzialeForm ricBuoni = (BuoniRicercaParzialeForm) form;
			try {
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				//&& request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

/*				if (request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null )
				{
					request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
					request.getSession().removeAttribute("ordiniSelected");
					request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

				}
				else
				{
					//throw new Exception("limite di ricorsione");

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));

					return mapping.getInputForward();
				}		*/
				this.impostaOrdineCerca(ricBuoni,request,mapping);
				return mapping.findForward("ordineCerca");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		private void impostaOrdineCerca( BuoniRicercaParzialeForm ricBuoni, HttpServletRequest request, ActionMapping mapping)
		{
		//impostazione di una lista di supporto
		try {
			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricBuoni.getCodBibl();
			String codBAff = null;
			String codOrd=ricBuoni.getNumero();
			String annoOrd=ricBuoni.getAnno();
			String tipoOrd=ricBuoni.getTipoOrdine(); // A
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


	private void loadTipoOrdinamento(BuoniRicercaParzialeForm ricBuoni) throws Exception {
		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Num. buono-data");
		lista.add(elem);
		elem = new StrutturaCombo("2","Data (disc.)");
		lista.add(elem);
		elem = new StrutturaCombo("3","Fornitore");
		lista.add(elem);

		ricBuoni.setListaTipiOrdinam(lista);
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_BUONI_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
*/		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BUONI_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}


		return false;
}



/*	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession httpSession = request.getSession();
		//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());

			BuoniRicercaParzialeForm ricBuoni = (BuoniRicercaParzialeForm) form;

			if (request.getParameter("cerca0") != null) {
				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("cerca");
			}
			if (request.getParameter("crea0") != null) {
				NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
				return mapping.findForward("nuovo");
			}

			this.loadTipoOrdine();
			ricBuoni.setNumBuonoDa("");
			ricBuoni.setNumBuonoA("");
			ricBuoni.setTipoOrdine("");
			ricBuoni.setDataBuonoDa("");
			ricBuoni.setDataBuonoA("");
			ricBuoni.setAnno("");
			ricBuoni.setNumero("");
			ricBuoni.setEsercizio("2006");
			ricBuoni.setCapitolo("1");
			ricBuoni.setFornitore("");
			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
			return mapping.getInputForward();
	}
*/
	/**
	  * it.iccu.sbn.web.actions.acquisizioni.ordini
	  * OrdineRicercaParzialeAction.java
	  * fornitoreCercaVeloce
	  * ActionForward
	  * @param mapping
	  * @param request
	  * @param ricOrdini
	  * @return
	  *
	  *
	 */
	private ActionForward fornitoreCercaVeloce(ActionMapping mapping,
			HttpServletRequest request, AcquisizioniBaseFormIntf myForm) {
		try{
			FornitoreVO fornVO = null;
			if (myForm.getCodFornitore() != null &&
					myForm.getCodFornitore().equals("")){
				if (myForm.getFornitore() != null &&
						myForm.getFornitore().equals("")){
					//sif
				}else{
					//cerco il fornitore per nome
					fornVO = this.getFornitore(Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(),
							null, myForm.getFornitore(), Navigation.getInstance(request).getUserTicket());
					if (fornVO != null){
						myForm.setCodFornitore(fornVO.getCodFornitore().toString());
						myForm.setFornitore(fornVO.getNomeFornitore());
						return mapping.getInputForward();
					}else{
						//sif
					}
				}
			}else{
				//cerco il fornitore per cod
				fornVO = this.getFornitore(Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(),
						myForm.getCodFornitore(), null, Navigation.getInstance(request).getUserTicket());
				if (fornVO != null){
					myForm.setCodFornitore(fornVO.getCodFornitore().toString());
					myForm.setFornitore(fornVO.getNomeFornitore());
					return mapping.getInputForward();
				}else{
					//sif
				}
			}
		} catch (DataException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.trovatoPiùDiUnRecordConNomeFornitoreIndicato"));

		}catch (Exception e) { // altri tipi di errore
			return mapping.getInputForward();
		}
		return null;
	}

	private FornitoreVO getFornitore(String codPolo, String codBib, String codFornitore, String descr, String ticket) throws Exception {
		FornitoreVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneAcquisizioni().getFornitore(codPolo, codBib, codFornitore, descr, ticket);
		return rec;
	}


}
