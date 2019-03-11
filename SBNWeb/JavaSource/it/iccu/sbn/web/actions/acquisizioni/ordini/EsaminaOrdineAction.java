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
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.EsaminaOrdineForm;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.OrdineRicercaParzialeForm;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.SinteticaOrdineForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationCache;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class EsaminaOrdineAction extends OrdineBaseAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(EsaminaOrdineAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("ricerca.button.indietro","indietro");
		map.put("crea.button.importaDa","importada");
		map.put("ricerca.button.bibloaffil","bibloaffil");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.stampa","stampa");
        map.put("servizi.bottone.si", "si");
        map.put("servizi.bottone.no", "no");
        map.put("button.ok", "noScript");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ordine.label.bilancio","bilancioCerca");
		map.put("ricerca.label.sezione","sezioneCerca");
		map.put("ricerca.button.scegli","scegli");
		map.put("ordine.bottone.searchTit", "sifbid");
		map.put("crea.button.associaInventari","associaInv");
		map.put("ricerca.label.converti","converti");
		map.put("ricerca.button.fornitoriProfili","fornPreferiti");
		map.put("crea.button.visualizzInventari","visualInv");
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
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_ORDINI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;

		if (request.getAttribute("bidNotiziaCorrente") != null){
			currentForm.setBidNotiziaCorrente((String)request.getAttribute("bidNotiziaCorrente"));
		}
		Navigation navi = Navigation.getInstance(request);
		HttpSession session = request.getSession();
		if (navi.isFromBar()){


			// gestione del caso in cui rientro nell'esamina da menu bar da associa inventari per ordine di rilegatura
			if ( currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R"))
			{
				if (session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE)!=null)
				{
					session.removeAttribute("chiamante");
					// carica
					List listaInv =new ArrayList();
					listaInv=(List) session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);

	/*				for (int w=0;  w < 2; w++)
					{
						StrutturaInventariOrd elem = new StrutturaInventariOrd("17191", "Le radici storiche dell'etica analitica", "", "", "");
						listaInv.add(elem);
					}
	*/
					currentForm.getDatiOrdine().getBilancio().setCodice3("4");
					currentForm.getDatiOrdine().setRigheInventariRilegatura(listaInv);

					currentForm.setElencaInventari(listaInv);
					currentForm.setNumRigheInv(listaInv.size());

					session.removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);

					// rilettura
					if (session.getAttribute("nuovoOrdineRilegatura")!=null && session.getAttribute("nuovoOrdineRilegatura") instanceof OrdiniVO)
					{
						// in caso sia stato creato all'interno della gestione degli inventari va aggiornato il codice e l'id dell'ordine
						OrdiniVO ordNew=(OrdiniVO) session.getAttribute("nuovoOrdineRilegatura");
						if (currentForm.getDatiOrdine()!=null && (currentForm.getDatiOrdine().getCodOrdine()==null || (currentForm.getDatiOrdine().getCodOrdine()!=null &&  currentForm.getDatiOrdine().getCodOrdine().trim().length()==0)))
						{
							currentForm.getDatiOrdine().setCodOrdine(ordNew.getCodOrdine());
						}
						if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getIDOrd()==0)
						{
							currentForm.getDatiOrdine().setIDOrd(ordNew.getIDOrd());
						}
						session.removeAttribute("nuovoOrdineRilegatura");

					}
					OrdiniVO eleOrdineLetto=this.loadDatiINS(currentForm.getDatiOrdine());

					if (eleOrdineLetto!=null )
					{
						currentForm.setDatiOrdine(eleOrdineLetto);
					}

					if (eleOrdineLetto!=null  && eleOrdineLetto.isContinuativo())
					{
						currentForm.setContin("on");
					}
					else
					{
						currentForm.setContin(null);
					}



/*
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.inventariAssociatiSalvare"));

*/
				}
				else if (currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )
				{
					currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
					currentForm.setNumRigheInv(currentForm.getElencaInventari().size());
				}
				//currentForm.setNumRigheInv(0); // per imporre il refresh della lista inventari

			}


			// per gestire il rientro dalla attribuzione degli inventari che impedisce la visualizzazione del messaggio di
			// inserimento corretto nel bottone si
			// inserimento corretto nel bottone si
			if (navi.getBackAction()!=null && navi.getBackAction().equals("/documentofisico/datiInventari/modificaInvColl.do"))
			{
				if (request.getAttribute("ordineCompletato")!=null &&  request.getAttribute("ordineCompletato").equals("ordineCompletato"))
				{
						// deve essere aggiornato l'ordine mettendo lo stato a chiuso solo se sono stati inseriti gli inventari
						// ed aggiornando il campo prezzo con il prezzo reale
						// introdotto con l'inventariazione
						// controllo inventariazione
					// controllo esistenza inventari
					// a seconda della configurazione adeguamento se automatico, su richiesta, mai
					currentForm.setOrdineCompletato(true);
					ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
					configurazioneCriteri.setCodBibl(currentForm.getDatiOrdine().getCodBibl());
					configurazioneCriteri.setCodPolo(currentForm.getDatiOrdine().getCodPolo());
					ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
					String confAdeguamento="N";
					if (configurazioneLetta!=null && configurazioneLetta.getAllineamento()!=null)
					{
						confAdeguamento=configurazioneLetta.getAllineamento();
					}
					// escludere gli ordini di tipo C, D, L dalla richiesta e dall'adeguemanto al prezzo reale
					if (currentForm!=null && currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null && (currentForm.getDatiOrdine().getTipoOrdine().equals("C") || currentForm.getDatiOrdine().getTipoOrdine().equals("D") || currentForm.getDatiOrdine().getTipoOrdine().equals("L")))
					{
						confAdeguamento="N";
					}
					if (confAdeguamento!=null &&  confAdeguamento.equals("A"))
					{
						// deve essere chiuso e adeguato
						// deve essere aggiornato l'ordine mettendo lo stato a chiuso solo se sono stati inseriti gli inventari
						// ed aggiornando il campo prezzo con il prezzo reale
						// introdotto con l'inventariazione
						// controllo inventariazione
						// controllo esistenza inventari

						this.ordineCompletamenteRicevuto( currentForm,  request, true);
						return ripristina( mapping,  form,  request,  response);
					}
					else if (confAdeguamento!=null && confAdeguamento.equals("R"))
					{
						// deve essere emesso messaggio di richiesta con si, no sull'adeguamento e comunque chiuso
						currentForm.setAdeguamentoPrezzo(true); // da resettare
						// emissione messaggio di conferma

						currentForm.setConferma(true);
						currentForm.setDisabilitaTutto(true);
						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazioneAdeguamentoPrezzi"));

			    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			    		return mapping.getInputForward();
					}
					else  // in tutti gli altri casi
					{
						// solo chiuso senza adeguamento
						this.ordineCompletamenteRicevuto( currentForm,  request, false);
						return ripristina( mapping,  form,  request,  response);
					}
				}
				else
				{

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.inserimentoOK"));

				}
			}
			return mapping.getInputForward();
		}

    	if (navi.isFromBar() )
    	{

    		// gestione del caso in cui rientro nel crea da menu bar da associa inventari
    		if ( currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R"))
			{
				if ( session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE)!=null)
				{
					session.removeAttribute("chiamante");
					// carica da gestire
					List listaInv =new ArrayList();
					listaInv=(List) session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);
					//for (int w=0;  w < elencaRigheFatt0.length; w++)
	/*				for (int w=0;  w < 2; w++)
					{
						StrutturaCombo elem = new StrutturaCombo("17191", "Le radici storiche dell'etica analitica");
						listaInv.add(elem);
					}
	*/
					currentForm.getDatiOrdine().getBilancio().setCodice3("4");
					currentForm.getDatiOrdine().setRigheInventariRilegatura(listaInv);
					currentForm.setElencaInventari(listaInv);
					currentForm.setNumRigheInv(listaInv.size());
					session.removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);

/*
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.inventariAssociatiSalvare"));

*/
				}
				else if ( currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )
				{
					currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
					//esaordini.setNumRigheInv(esaordini.getElencaInventari().size());
				}
				currentForm.setNumRigheInv(0); // per imporre il refresh della lista inventari

			}

    		return mapping.getInputForward();
		}

    	if(!currentForm.isSessione())
		{
    		log.debug("EsaminaOrdineAction::unspecified()");
    		currentForm.setSessione(true);
		}

    	try {
    		String ticket=navi.getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=navi.getUtente().getCodBib();
			//currentForm.getDatiOrdine().setCodBibl(biblio);

			if (biblio!=null &&   currentForm.getDatiOrdine()!=null  &&  (currentForm.getDatiOrdine().getCodBibl()==null || (currentForm.getDatiOrdine().getCodBibl()!=null && currentForm.getDatiOrdine().getCodBibl().trim().length()==0)))
			{
				currentForm.getDatiOrdine().setCodBibl(biblio);
				currentForm.setDenoBibl(navi.getUtente().getBiblioteca());
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				currentForm.getDatiOrdine().setCodBibl(bibScelta.getCod_bib());
				currentForm.setDenoBibl(bibScelta.getNom_biblioteca());

			}

			String polo=navi.getUtente().getCodPolo();
			currentForm.getDatiOrdine().setCodPolo(polo);


			if (!currentForm.isCaricamentoIniziale() ) {
				this.loadDatiOrdine( currentForm ,request);
				//almaviva2 1/12/2011
				loadDefault(request, mapping, form);

				currentForm.setCaricamentoIniziale(true);
			}else{
				if (!currentForm.isSubmitDinamico())	{
					if ((request.getAttribute("ordineCompletato")!=null &&  request.getAttribute("ordineCompletato").equals("ordineCompletato")) || currentForm.isOrdineCompletato())	{
						// deve essere aggiornato l'ordine mettendo lo stato a chiuso solo se sono stati inseriti gli inventari
						// ed aggiornando il campo prezzo con il prezzo reale
						// introdotto con l'inventariazione
						// controllo inventariazione
						// controllo esistenza inventari

						List listaInv=null;
						try {
							  String codPolo=currentForm.getDatiOrdine().getCodPolo();
						      String codBib=currentForm.getDatiOrdine().getCodBibl();
						      String codBibO=currentForm.getDatiOrdine().getCodBibl();
						      String codTipOrd=currentForm.getDatiOrdine().getTipoOrdine();
						      String codBibF="";
						      //String ticket=ordine.getTicket();
						      String ticket2=currentForm.getDatiOrdine().getTicket();
						      int   annoOrd=Integer.valueOf(currentForm.getDatiOrdine().getAnnoOrdine().trim());
						      int   codOrd=Integer.valueOf(currentForm.getDatiOrdine().getCodOrdine());
						      int   nRec=999;

						      FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
						      listaInv = factory.getGestioneDocumentoFisico().getListaInventariOrdine( codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, this.getLocale(request, Constants.SBN_LOCALE),ticket2,  nRec).getLista();

						      //listaInv=this.getInventariRox(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, ticket,  nRec);
						     // listaInv= inventario.getListaInventariOrdine(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, ticket,  nRec);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							// l'errore capita in questo punto
							//e.printStackTrace();
						}
						if (listaInv!=null && listaInv.size()>0)	{
							// calcolo del prezzo reale
							double totImportInv=0.00;
							for (int t=0; t<listaInv.size(); t++)	{
								// listaInv.get(t)
								InventarioListeVO eleInve=(InventarioListeVO)listaInv.get(t);
								double importInv=0;
								if (eleInve!=null && eleInve.getImportoDouble()>0)	{
									importInv=eleInve.getImportoDouble();
								}
								totImportInv=totImportInv + importInv;
							}

							OrdiniVO eleOrdine=currentForm.getDatiOrdine();
							//if (currentForm.getDatiOrdine().getTipoOrdine().equals("L") || currentForm.getDatiOrdine().getTipoOrdine().equals("D") || currentForm.getDatiOrdine().getTipoOrdine().equals("C") )
							//{
								String oldStatoOrdine=eleOrdine.getStatoOrdine();
								eleOrdine.setStatoOrdine("C");
								if (totImportInv>0)		{
									if (eleOrdine.getTipoOrdine().equals("A") || eleOrdine.getTipoOrdine().equals("V") )	{
										eleOrdine.setPrezzoEuroOrdine(totImportInv);
										eleOrdine.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( eleOrdine.getPrezzoEuroOrdine()));
									}
								}

								if (this.modificaOrdine(eleOrdine))		{
									currentForm.setDatiOrdine(eleOrdine);
									currentForm.setDisabilitaTutto(true);
									currentForm.setStatiCDL(true);

									LinkableTagUtils.addError(request, new ActionMessage(
									"errors.acquisizioni.inserimentoOK"));


								}	else	{
									// messaggio di incompletezza sell'inserimento per l'assenza inventari
									eleOrdine.setStatoOrdine(oldStatoOrdine);
								}
							//}

						}
					}
/*					else // di if request
					{

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.inserimentoOK"));

					}
*/
				}	else	{
	    			currentForm.setSubmitDinamico(false);
					if (currentForm.getDatiOrdine()!=null)	{
		    			OrdiniVO oggettoTemp=currentForm.getDatiOrdine();
		    			currentForm.setDatiOrdine(oggettoTemp);
					}
					// modifica del boolean del vo di ordini alla variazione del check del continuativo della pagina jsp (vedi anche loadDatiOrdinePassato che imposta il check del boolean della pagina jsp)
					if (request.getParameter("contin") != null) {
						currentForm.getDatiOrdine().setContinuativo(true);
						currentForm.setContin("on");
					}	else	{
						currentForm.getDatiOrdine().setContinuativo(false);
						currentForm.setContin(null);
					}


				}

			}


			// impostazione della provenienza se esiste uno fra codRicOffertaOrdine, codDocOrdine, idOffertaFornOrdine, codSuggBiblOrdine
			currentForm.setProvenienza("");

    		this.initCombo( currentForm );
			try {
				this.loadValuta(currentForm, request );
			} catch (ValidationException e) {
				//almaviva5_20140613 #5078
				if (e.getError() == ValidationExceptionCodici.assenzaRisultati)
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.cambierroreAssenzaValutaRiferimento"));
				throw e;
			}

			this.loadPeriodo( currentForm );
			this.loadBiblAffil( currentForm );



			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);

			// controllo che non riceva l'attributo di sessione di una lista supporto
			// oppure provenga dal vai A || (ricArr.getChiamante()!=null && this.ricOrdini.isProvenienzaVAIA())
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				// preimpostazione di campi con elemento ricercato e non trovato
				currentForm.getDatiOrdine().setBilancio(ricArr.getBilancio());
				currentForm.getDatiOrdine().setFornitore(ricArr.getFornitore());
				currentForm.getDatiOrdine().setTitolo(ricArr.getTitolo());
				currentForm.getDatiOrdine().setSezioneAcqOrdine(ricArr.getSezioneAcqOrdine());
				currentForm.getDatiOrdine().setCodBibl(ricArr.getCodBibl());
				if (ricArr.getAnnoOrdine()!=null && ricArr.getAnnoOrdine().trim().length()>0)
				{
					currentForm.getDatiOrdine().setAnnoOrdine(ricArr.getAnnoOrdine());
				}
				if (ricArr.getTipoOrdine()!=null && ricArr.getTipoOrdine().trim().length()>0)
				{
					currentForm.getDatiOrdine().setTipoOrdine(ricArr.getTipoOrdine());
				}
				if (ricArr.getStatoOrdine()!=null && ricArr.getStatoOrdine().trim().length()>0)
				{
					currentForm.getDatiOrdine().setStatoOrdine(ricArr.getStatoOrdine());
				}
				if (ricArr.getNaturaOrdine()!=null && ricArr.getNaturaOrdine().trim().length()>0)
				{
					currentForm.getDatiOrdine().setNaturaOrdine(ricArr.getNaturaOrdine());
				}
				//currentForm.setScegliTAB(ricArr.getTipoOrdine());
				if (!ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo"))	{
					currentForm.setVisibilitaIndietroLS(true); // bottone scegli
				}else{
					session.setAttribute("bid",ricArr.getTitolo().getCodice());
					session.setAttribute("desc",ricArr.getTitolo().getDescrizione());
					if (!ricArr.getNaturaOrdine().equals("C") && !ricArr.getNaturaOrdine().equals("M") && !ricArr.getNaturaOrdine().equals("S"))	{

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.naturaNonAmmessa"));


					}	else	{
						session.setAttribute("natura",ricArr.getNaturaOrdine());
					}
					// pulizia dei criteri da ricercare solo per il VAI A
					session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				}
			}

			//VALUTAZIONE SCELTA DEL TAB
	   		if (!currentForm.isCaricamentoIniziale()){
				currentForm.setStatiCDL(false);
	   			currentForm.getDatiOrdine().setStatoOrdine("A"); // impostazione
			}
			if (request.getParameter("paramLink") != null) {
				currentForm.setScegliTAB(request.getParameter("paramLink"));
				if (currentForm.getScegliTAB()!=null && currentForm.getScegliTAB().trim().length()>0)	{
					currentForm.getDatiOrdine().setTipoOrdine(currentForm.getScegliTAB());
				}	else	{
					currentForm.getDatiOrdine().setTipoOrdine("A");
				}

				if (!currentForm.getDatiOrdine().getTipoOrdine().equals("R") && currentForm.getDatiOrdine().getBilancio()!=null &&  currentForm.getDatiOrdine().getBilancio().getCodice3()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3().equals("4"))		{
					currentForm.getDatiOrdine().getBilancio().setCodice3("");
				}

				if (currentForm.getDatiOrdine().getTipoOrdine().equals("R"))	{
					currentForm.setStatiCDL(false);
					// pulizia dei campi non in comune
		   			currentForm.getDatiOrdine().setStatoOrdine("A"); // impostazione
					currentForm.getDatiOrdine().setTitolo(new  StrutturaCombo ("", ""));
					currentForm.getDatiOrdine().setCodOrdine("");
		    		currentForm.setDisabilitaTutto(false);
					currentForm.getDatiOrdine().setNaturaOrdine("");
					currentForm.getDatiOrdine().setSezioneAcqOrdine("");
					currentForm.getDatiOrdine().setNumCopieOrdine(0);
					currentForm.getDatiOrdine().setRegTribOrdine("");
					currentForm.getDatiOrdine().setFornitore(new  StrutturaCombo ("", ""));
					currentForm.getDatiOrdine().setPrezzoEuroOrdine(0);
					currentForm.getDatiOrdine().setPrezzoOrdine(0);
					//this.esaordR.getEsaord().getDatiOrdine().setBilancio(new  StrutturaTerna ("", "", ""));
					//this.esaordR.getEsaord().getDatiOrdine().setNoteFornitore("");
					//this.esaordR.getEsaord().getDatiOrdine().setValutaOrdine("");
					//this.esaordR.getEsaord().getDatiOrdine().setPrezzoEuroOrdine("");
					//this.esaordR.getEsaord().getDatiOrdine().setPrezzoOrdine("");
					//currentForm.getDatiOrdine().setTipoInvioOrdine("");
					//currentForm.getDatiOrdine().setCodUrgenzaOrdine("");
					currentForm.getDatiOrdine().setIdOffertaFornOrdine("");
					currentForm.getDatiOrdine().setStatoAbbOrdine("");
					currentForm.getDatiOrdine().setPeriodoValAbbOrdine("");
					currentForm.getDatiOrdine().setAnnoAbbOrdine("");
					currentForm.getDatiOrdine().setNumFascicoloAbbOrdine("");
					currentForm.getDatiOrdine().setDataPubblFascicoloAbbOrdine("");
					currentForm.getDatiOrdine().setAnnataAbbOrdine("");
					currentForm.getDatiOrdine().setNumVolAbbOrdine(0);
					//(pluto.getDatiOrdine().isContinuativo() && pluto.getDatiOrdine().getNaturaOrdine().equals("S") && pluto.getDatiOrdine().getStatoOrdine().equals("A") )
				}
				if (currentForm.getDatiOrdine().getTipoOrdine().equals("L") || currentForm.getDatiOrdine().getTipoOrdine().equals("D") || currentForm.getDatiOrdine().getTipoOrdine().equals("C") )	{
					// pulizia dei campi non in comune
		   			currentForm.getDatiOrdine().setStatoOrdine("A"); // impostazione
					currentForm.setStatiCDL(true);
					currentForm.getDatiOrdine().setCodOrdine("");
		    		currentForm.setDisabilitaTutto(false);
					//currentForm.getDatiOrdine().setNaturaOrdine("");
		    		currentForm.getDatiOrdine().setSezioneAcqOrdine("");
					currentForm.getDatiOrdine().setNumCopieOrdine(0);
					currentForm.getDatiOrdine().setNumCopieOrdine(0);
					if ( currentForm.getDatiOrdine().getTipoOrdine().equals("D") || currentForm.getDatiOrdine().getTipoOrdine().equals("C") )	{
						currentForm.getDatiOrdine().setRegTribOrdine("");
					}
					currentForm.getDatiOrdine().setBilancio(new  StrutturaTerna ("", "", ""));
					currentForm.getDatiOrdine().setFornitore(new  StrutturaCombo ("", ""));
					currentForm.getDatiOrdine().setNoteFornitore("");
					currentForm.getDatiOrdine().setValutaOrdine("");
					currentForm.getDatiOrdine().setPrezzoEuroOrdine(0);
					currentForm.getDatiOrdine().setPrezzoOrdine(0);
					currentForm.getDatiOrdine().setTipoInvioOrdine("");
					currentForm.getDatiOrdine().setCodUrgenzaOrdine("");
					currentForm.getDatiOrdine().setIdOffertaFornOrdine("");
					currentForm.getDatiOrdine().setStatoAbbOrdine("");
					currentForm.getDatiOrdine().setPeriodoValAbbOrdine("");
					currentForm.getDatiOrdine().setAnnoAbbOrdine("");
					currentForm.getDatiOrdine().setNumFascicoloAbbOrdine("");
					currentForm.getDatiOrdine().setDataPubblFascicoloAbbOrdine("");
					currentForm.getDatiOrdine().setAnnataAbbOrdine("");
					currentForm.getDatiOrdine().setNumVolAbbOrdine(0);
					currentForm.getDatiOrdine().setStampato(false);

				}	else	{
					currentForm.setStatiCDL(false);
					currentForm.getDatiOrdine().setFornitore(new  StrutturaCombo ("", ""));
					currentForm.getDatiOrdine().setBilancio(new  StrutturaTerna ("", "", ""));
					currentForm.getDatiOrdine().setPrezzoEuroOrdine(0);
					currentForm.getDatiOrdine().setPrezzoOrdine(0);
					//currentForm.getDatiOrdine().setNaturaOrdine("");
					currentForm.getDatiOrdine().setContinuativo(false);
					currentForm.getDatiOrdine().setTipoInvioOrdine("");
					currentForm.getDatiOrdine().setCodUrgenzaOrdine("");
					currentForm.getDatiOrdine().setIdOffertaFornOrdine("");
					currentForm.getDatiOrdine().setStatoAbbOrdine("");
					currentForm.getDatiOrdine().setPeriodoValAbbOrdine("");
					currentForm.getDatiOrdine().setAnnoAbbOrdine("");
					currentForm.getDatiOrdine().setNumFascicoloAbbOrdine("");
					currentForm.getDatiOrdine().setDataPubblFascicoloAbbOrdine("");
					currentForm.getDatiOrdine().setAnnataAbbOrdine("");
					currentForm.getDatiOrdine().setNumVolAbbOrdine(0);
					currentForm.getDatiOrdine().setStampato(false);

				}

			} else {
				currentForm.getDatiOrdine().setTipoOrdine("A");
				if (currentForm.getScegliTAB() != null)	{
					currentForm.getDatiOrdine().setTipoOrdine(currentForm.getScegliTAB());
				}

				if (currentForm.isSubmitDinamico())	{
					currentForm.getDatiOrdine().setTipoOrdine(currentForm.getScegliTAB());
					if (request.getParameter("contin") != null) 	{
						currentForm.getDatiOrdine().setContinuativo(true);
						currentForm.setContin("on");
					}	else	{
						currentForm.getDatiOrdine().setContinuativo(false);
						currentForm.setContin(null);
					}

				}
			}

			// preimpostazione della schermata di inserimento con i valori ricercati
			if (session.getAttribute("ATTRIBUTEListaSuppOrdiniVO")!=null)
			{
				ListaSuppOrdiniVO ele= (ListaSuppOrdiniVO )session.getAttribute("ATTRIBUTEListaSuppOrdiniVO");
				session.removeAttribute("ATTRIBUTEListaSuppOrdiniVO");
				if (ele.getBilancio()!=null )	{
					currentForm.getDatiOrdine().setBilancio(ele.getBilancio());
				}
				if (ele.getFornitore()!=null )	{
					currentForm.getDatiOrdine().setFornitore(ele.getFornitore());
				}
				if (ele.getTitolo()!=null )		{
					currentForm.getDatiOrdine().setTitolo(ele.getTitolo());
				}
				if (ele.getSezioneAcqOrdine()!=null && ele.getSezioneAcqOrdine().trim().length()>0  )	{
					currentForm.getDatiOrdine().setSezioneAcqOrdine(ele.getSezioneAcqOrdine());
				}
				if (ele.getTipoOrdine()!=null && ele.getTipoOrdine().trim().length()>0 )	{
					currentForm.getDatiOrdine().setTipoOrdine(ele.getTipoOrdine());
				}
				if (ele.getNaturaOrdine()!=null && ele.getNaturaOrdine().trim().length()>0 ){
					currentForm.getDatiOrdine().setNaturaOrdine(ele.getNaturaOrdine());
				}
				// recupero ulteriori informazioni dell'ordine da duplicare
				if (ele.getOrdineDuplicatoRinnovato()!=null )	{

/*					Calendar c=new GregorianCalendar();
				 	int anno=c.get(Calendar.YEAR);
				 	String annoAttuale="";
				 	annoAttuale=Integer.valueOf(anno).toString();
					String annoOrd=annoAttuale;

					// ASSEGNAZIONE DELLA data di sistema
					Date dataodierna=new Date();
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					String dataOdiernaStr =(String)formato.format(dataodierna);
*/
					//((EsaminaOrdineForm) esaord).setDatiOrdine((OrdiniVO) ele.getOrdineDuplicatoRinnovato());

					currentForm.setDatiOrdine(ele.getOrdineDuplicatoRinnovato());
/*					((EsaminaOrdineForm) esaord).getDatiOrdine().setCodOrdine("");
					((EsaminaOrdineForm) esaord).getDatiOrdine().setStatoOrdine("A");
					((EsaminaOrdineForm) esaord).getDatiOrdine().setAnnoOrdine(annoOrd);
					((EsaminaOrdineForm) esaord).getDatiOrdine().setDataOrdine(dataOdiernaStr);
					((EsaminaOrdineForm) esaord).getDatiOrdine().setStampato(false);
*/
				}

			}


			// abilitazione/disabilitazione sezione abbonamenti
			currentForm.setDisabilitazioneSezioneAbbonamento(true);
			if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A") )  {
				//VEDI TCK 2557
				currentForm.setDisabilitazioneSezioneAbbonamento(false);

				if (!currentForm.getDatiOrdine().isGestBil() && (currentForm.getDatiOrdine().getTipoOrdine().equals("A") || currentForm.getDatiOrdine().getTipoOrdine().equals("V"))) 	{
					currentForm.setDisabilitazioneSezioneAbbonamento(false);
				}

				if (currentForm.getDatiOrdine().isGestBil() &&  currentForm.getDatiOrdine().getBilancio()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3().equals("2") && (currentForm.getDatiOrdine().getTipoOrdine().equals("A") || currentForm.getDatiOrdine().getTipoOrdine().equals("V"))) {
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}
				if (currentForm.getDatiOrdine().getTipoOrdine().equals("L") || currentForm.getDatiOrdine().getTipoOrdine().equals("D")  || currentForm.getDatiOrdine().getTipoOrdine().equals("C")) {
					currentForm.setDisabilitazioneSezioneAbbonamento(false);
				}
			}

			//controllo se ho un risultato di IMPORTA DA DOCUMENTI richiamata da questa pagina (risultato della simulazione)
			ListaSuppDocumentoVO ricDoc=(ListaSuppDocumentoVO)session.getAttribute("attributeListaSuppDocumentoVO");
			if (ricDoc!=null && ricDoc.getChiamante()!=null && ricDoc.getChiamante().equals(mapping.getPath())) 	{
				if (ricDoc!=null && ricDoc.getSelezioniChiamato()!=null && ricDoc.getSelezioniChiamato().size()!=0 )	{
					if (ricDoc.getSelezioniChiamato().get(0).getTitolo()!=null && ricDoc.getSelezioniChiamato().get(0).getTitolo().getCodice().length()!=0 ){
						if (ricDoc.getSelezioniChiamato().get(0).getStatoSuggerimentoDocumento()!=null && !ricDoc.getSelezioniChiamato().get(0).getStatoSuggerimentoDocumento().equals("A") )	{
							// il suggerimento che si vorrebbe importare non è accettato

							LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.importaDaSuggBibl"));

						}	else	{
							currentForm.getDatiOrdine().getTitolo().setCodice(ricDoc.getSelezioniChiamato().get(0).getTitolo().getCodice());
							currentForm.getDatiOrdine().getTitolo().setDescrizione(ricDoc.getSelezioniChiamato().get(0).getTitolo().getDescrizione());
							currentForm.getDatiOrdine().setCodDocOrdine(String.valueOf(ricDoc.getSelezioniChiamato().get(0).getIDDoc()));
							currentForm.getDatiOrdine().setNaturaOrdine(ricDoc.getSelezioniChiamato().get(0).getNaturaBid());
							//currentForm.setProvenienza("Sugg. lett.");
							currentForm.getDatiOrdine().setProvenienza(currentForm.getDatiOrdine().getCodDocOrdine() + "-Sugg. lett.");
						}
					}
				}else{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().getTitolo().setCodice("");
					currentForm.getDatiOrdine().getTitolo().setDescrizione("");
					currentForm.getDatiOrdine().setCodDocOrdine("");
					currentForm.getDatiOrdine().setNaturaOrdine("");

					//currentForm.setProvenienza("");
					currentForm.getDatiOrdine().setProvenienza("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute("attributeListaSuppDocumentoVO");
				session.removeAttribute("documentiSelected");
				session.removeAttribute("criteriRicercaDocumento");
				session.removeAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
 			}

			//controllo se ho un risultato di IMPORTA DA SUGGERIMENTI richiamata da questa pagina (risultato della simulazione)
			ListaSuppSuggerimentoVO ricSugg=(ListaSuppSuggerimentoVO)session.getAttribute("attributeListaSuppSuggerimentoVO");
			if (ricSugg!=null && ricSugg.getChiamante()!=null && ricSugg.getChiamante().equals(mapping.getPath()))	{
				if (ricSugg!=null && ricSugg.getSelezioniChiamato()!=null && ricSugg.getSelezioniChiamato().size()!=0 )	{
					if (ricSugg.getSelezioniChiamato().get(0).getTitolo()!=null && ricSugg.getSelezioniChiamato().get(0).getTitolo().getCodice().length()!=0 )	{
						if (ricSugg.getSelezioniChiamato().get(0).getStatoSuggerimento()!=null && !ricSugg.getSelezioniChiamato().get(0).getStatoSuggerimento().equals("A") ){
							// il suggerimento che si vorrebbe importare non è accettato

							LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.importaDaSuggBibl"));

						}else{
							currentForm.getDatiOrdine().getTitolo().setCodice(ricSugg.getSelezioniChiamato().get(0).getTitolo().getCodice());
							currentForm.getDatiOrdine().getTitolo().setDescrizione(ricSugg.getSelezioniChiamato().get(0).getTitolo().getDescrizione());
							currentForm.getDatiOrdine().setCodSuggBiblOrdine(ricSugg.getSelezioniChiamato().get(0).getChiave());
							currentForm.getDatiOrdine().setSezioneAcqOrdine(ricSugg.getSelezioniChiamato().get(0).getSezione().getCodice());
							currentForm.getDatiOrdine().setNoteFornitore(ricSugg.getSelezioniChiamato().get(0).getNoteFornitore());
							currentForm.getDatiOrdine().setNaturaOrdine(ricSugg.getSelezioniChiamato().get(0).getNaturaBid());

							//currentForm.setProvenienza("Sugg. bibliotec.");
							currentForm.getDatiOrdine().setProvenienza(currentForm.getDatiOrdine().getCodSuggBiblOrdine() + "-Sugg. bibliotec.");
						}
					}
				}	else	{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().getTitolo().setCodice("");
					currentForm.getDatiOrdine().getTitolo().setDescrizione("");
					currentForm.getDatiOrdine().setCodSuggBiblOrdine("");
					currentForm.getDatiOrdine().setSezioneAcqOrdine("");
					//currentForm.setProvenienza("");
					currentForm.getDatiOrdine().setProvenienza("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute("attributeListaSuppSuggerimentoVO");
				session.removeAttribute("suggerimentiSelected");
				session.removeAttribute("criteriRicercaSuggerimento");
				session.removeAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
 			}

			//controllo se ho un risultato di IMPORTA DA OFFERTE richiamata da questa pagina (risultato della simulazione)
			ListaSuppOffertaFornitoreVO ricOff=(ListaSuppOffertaFornitoreVO)session.getAttribute("attributeListaSuppOffertaFornitoreVO");
			if (ricOff!=null && ricOff.getChiamante()!=null && ricOff.getChiamante().equals(mapping.getPath())) 	{
				if (ricOff!=null && ricOff.getSelezioniChiamato()!=null && ricOff.getSelezioniChiamato().size()!=0 )	{
					if (ricOff.getSelezioniChiamato().get(0).getBid()!=null && ricOff.getSelezioniChiamato().get(0).getBid().getCodice().length()!=0 )	{
						if (ricOff.getSelezioniChiamato().get(0).getStatoOfferta()!=null && !ricOff.getSelezioniChiamato().get(0).getStatoOfferta().equals("A") ){
							// L'offerta che si vorrebbe importare non è accettata

							LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.importaDaOfferteForn"));

						}	else	{
							currentForm.getDatiOrdine().getTitolo().setCodice(ricOff.getSelezioniChiamato().get(0).getBid().getCodice());
							currentForm.getDatiOrdine().getTitolo().setDescrizione(ricOff.getSelezioniChiamato().get(0).getBid().getDescrizione());
							currentForm.getDatiOrdine().setIdOffertaFornOrdine(String.valueOf(ricOff.getSelezioniChiamato().get(0).getIDOff()));
							currentForm.getDatiOrdine().setPrezzoEuroOrdine(Double.valueOf(ricOff.getSelezioniChiamato().get(0).getPrezzo()));
							currentForm.getDatiOrdine().setFornitore(ricOff.getSelezioniChiamato().get(0).getFornitore());
							currentForm.getDatiOrdine().setNaturaOrdine(ricOff.getSelezioniChiamato().get(0).getNaturaBid());

							//currentForm.setProvenienza("Offerte fornitore");
							currentForm.getDatiOrdine().setProvenienza(currentForm.getDatiOrdine().getIdOffertaFornOrdine() + "-Offerte fornitore");
						}
					}
				}	else	{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().getTitolo().setCodice("");
					currentForm.getDatiOrdine().getTitolo().setDescrizione("");
					currentForm.getDatiOrdine().setIdOffertaFornOrdine("");
					currentForm.getDatiOrdine().setPrezzoEuroOrdine(0);
					currentForm.getDatiOrdine().setFornitore(new StrutturaCombo("",""));
					currentForm.getDatiOrdine().setNaturaOrdine("");
					//currentForm.setProvenienza("");
					currentForm.getDatiOrdine().setProvenienza("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute("attributeListaSuppOffertaFornitoreVO");
				session.removeAttribute("offerteSelected");
				session.removeAttribute("criteriRicercaOfferta");
				session.removeAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
 			}

			//controllo se ho un risultato di IMPORTA DA GARE richiamata da questa pagina (risultato della simulazione)
			ListaSuppGaraVO ricGara=(ListaSuppGaraVO)session.getAttribute("attributeListaSuppGaraVO");
			if (ricGara!=null && ricGara.getChiamante()!=null && ricGara.getChiamante().equals(mapping.getPath()))	{
				if (ricGara!=null && ricGara.getSelezioniChiamato()!=null && ricGara.getSelezioniChiamato().size()!=0 )		{
					if (ricGara.getSelezioniChiamato().get(0).getBid()!=null && ricGara.getSelezioniChiamato().get(0).getBid().getCodice().length()!=0 )	{
						if (ricGara.getSelezioniChiamato().get(0).getStatoRicOfferta()!=null && !ricGara.getSelezioniChiamato().get(0).getStatoRicOfferta().equals("2") )	{
							// la gara che si vorrebbe importare non è chiusa

							LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.importaDaGare"));

						}else{
							currentForm.getDatiOrdine().getTitolo().setCodice(ricGara.getSelezioniChiamato().get(0).getBid().getCodice());
							currentForm.getDatiOrdine().getTitolo().setDescrizione(ricGara.getSelezioniChiamato().get(0).getBid().getDescrizione());
							currentForm.getDatiOrdine().setCodRicOffertaOrdine(ricGara.getSelezioniChiamato().get(0).getChiave());
							currentForm.getDatiOrdine().setPrezzoEuroOrdine(ricGara.getSelezioniChiamato().get(0).getPrezzoIndGara());
			    		    currentForm.getDatiOrdine().setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoEuroOrdine()));
							currentForm.getDatiOrdine().setNumCopieOrdine(Integer.valueOf(ricGara.getSelezioniChiamato().get(0).getNumCopieRicAcq()));
							// selezione del fornitore vincente
							currentForm.getDatiOrdine().setNaturaOrdine(ricGara.getSelezioniChiamato().get(0).getNaturaBid());
							for (int i=0; i<ricGara.getSelezioniChiamato().get(0).getDettaglioPartecipantiGara().size(); i++)	{
								if (ricGara.getSelezioniChiamato().get(0).getDettaglioPartecipantiGara().get(i).getStatoPartecipante().equals("V"))		{
									currentForm.getDatiOrdine().setFornitore(ricGara.getSelezioniChiamato().get(0).getDettaglioPartecipantiGara().get(i).getFornitore());
								}
							}
							//currentForm.setProvenienza("Gare");
							currentForm.getDatiOrdine().setProvenienza(currentForm.getDatiOrdine().getCodRicOffertaOrdine() + "-Gare");
						}
					}
				}	else	{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().getTitolo().setCodice("");
					currentForm.getDatiOrdine().getTitolo().setDescrizione("");
					currentForm.getDatiOrdine().setCodRicOffertaOrdine("");
					currentForm.getDatiOrdine().setPrezzoEuroOrdine(0);
					currentForm.getDatiOrdine().setNumCopieOrdine(0);
					currentForm.getDatiOrdine().setFornitore(new StrutturaCombo("",""));
					currentForm.getDatiOrdine().setNaturaOrdine("");

					//currentForm.setProvenienza("");
					currentForm.getDatiOrdine().setProvenienza("");

				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute("attributeListaSuppGaraVO");
				session.removeAttribute("gareSelected");
				session.removeAttribute("criteriRicercaGara");
				session.removeAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO);
 			}

			// è stato scelto un fornitore preferito dalla lista e si devono caricare tutti i dati
			String fornScelto= (String)request.getAttribute("fornScelto");
			if (fornScelto!=null &&  fornScelto.length()!=0)	{
				String fornSceltoDeno= (String)request.getAttribute("fornSceltoDeno");
				currentForm.getDatiOrdine().getFornitore().setCodice(fornScelto);
				currentForm.getDatiOrdine().getFornitore().setDescrizione("");

				if (fornSceltoDeno!=null &&  fornSceltoDeno.length()!=0){
					currentForm.getDatiOrdine().getFornitore().setDescrizione(fornSceltoDeno);
				}
				request.removeAttribute("fornScelto");
				request.removeAttribute("fornSceltoDeno");

			}


			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)session.getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))		{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )	{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )	{
						currentForm.getDatiOrdine().getFornitore().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						currentForm.getDatiOrdine().getFornitore().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
					}
				}	else	{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().getFornitore().setCodice("");
					currentForm.getDatiOrdine().getFornitore().setDescrizione("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute("attributeListaSuppFornitoreVO");
				session.removeAttribute("fornitoriSelected");
				session.removeAttribute("criteriRicercaFornitore");

 			}
			//controllo se ho un risultato di una lista di supporto BILANCIO richiamata da questa pagina (risultato della simulazione)
			ListaSuppBilancioVO ricBil=(ListaSuppBilancioVO)session.getAttribute("attributeListaSuppBilancioVO");
			if (ricBil!=null && ricBil.getChiamante()!=null && ricBil.getChiamante().equals(mapping.getPath()))	{
				if (ricBil!=null && ricBil.getSelezioniChiamato()!=null && ricBil.getSelezioniChiamato().size()!=0 ){
					if (ricBil.getSelezioniChiamato().get(0).getChiave()!=null && ricBil.getSelezioniChiamato().get(0).getChiave().length()!=0 )	{
						currentForm.getDatiOrdine().getBilancio().setCodice1(ricBil.getSelezioniChiamato().get(0).getEsercizio());
						currentForm.getDatiOrdine().getBilancio().setCodice2(ricBil.getSelezioniChiamato().get(0).getCapitolo());
						currentForm.getDatiOrdine().getBilancio().setCodice3(ricBil.getSelezioniChiamato().get(0).getSelezioneImp());
					}
				}else	{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().getBilancio().setCodice1("");
					currentForm.getDatiOrdine().getBilancio().setCodice2("");
					currentForm.getDatiOrdine().getBilancio().setCodice3("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute("attributeListaSuppBilancioVO");
				session.removeAttribute("bilanciSelected");
				session.removeAttribute("criteriRicercaBilancio");

 			}
			//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
			ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))	{
				if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 ){
					if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )	{
						currentForm.getDatiOrdine().setSezioneAcqOrdine(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
					}
				}else{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().setSezioneAcqOrdine("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				session.removeAttribute("sezioniSelected");
				session.removeAttribute("criteriRicercaSezione");
 			}
			// controllo se ho un risultato da interrogazione ricerca
			String bid=(String) request.getAttribute("bid");
			if (bid!=null && bid.length()!=0 )	{
				String titolo=(String) request.getAttribute("titolo");
				// controllo se ho un risultato da interrogazione ricerca
				//String acq = request.getParameter("ACQUISIZIONI");
				//if ( acq != null) {
				currentForm.getDatiOrdine().getTitolo().setCodice(bid);
				if ( titolo != null) {
					currentForm.getDatiOrdine().getTitolo().setDescrizione(titolo);
				}
				// controllo titolo
				this.controllaTitolo(currentForm,bid);

			}
			// ritorno da bottone specifico: da gestire il caricamento
			if ( currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R"))	{
				// rilettura
				if (session.getAttribute("nuovoOrdineRilegatura")!=null && session.getAttribute("nuovoOrdineRilegatura") instanceof OrdiniVO)	{
					// in caso sia stato creato all'interno della gestione degli inventari va aggiornato il codice e l'id dell'ordine
					OrdiniVO ordNew=(OrdiniVO) session.getAttribute("nuovoOrdineRilegatura");
					if (currentForm.getDatiOrdine()!=null && (currentForm.getDatiOrdine().getCodOrdine()==null || (currentForm.getDatiOrdine().getCodOrdine()!=null &&  currentForm.getDatiOrdine().getCodOrdine().trim().length()==0))){
						currentForm.getDatiOrdine().setCodOrdine(ordNew.getCodOrdine());
					}
					if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getIDOrd()==0)	{
						currentForm.getDatiOrdine().setIDOrd(ordNew.getIDOrd());
					}
					session.removeAttribute("nuovoOrdineRilegatura");

				}
				if ( session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE)!=null){
					session.removeAttribute("chiamante");
					// carica da gestire
					List listaInv =new ArrayList();
					listaInv=(List) session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);
					//for (int w=0;  w < elencaRigheFatt0.length; w++)
					if (currentForm.getDatiOrdine().getBilancio()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3()!=null){
						currentForm.getDatiOrdine().getBilancio().setCodice3("4");
					}
					currentForm.getDatiOrdine().setRigheInventariRilegatura(listaInv);
					currentForm.setElencaInventari(listaInv);
					currentForm.setNumRigheInv(listaInv.size());
					session.removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);
				} else if ( currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )	{
					currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
					currentForm.setNumRigheInv(currentForm.getElencaInventari().size());
				}

				//almaviva5_20140320 redirect a mappa gestione
				if (ValidazioneDati.isFilled(currentForm.getDatiOrdine().getCodOrdine()))
					return forwardMappaGestione(mapping, request, form);
			}

			if (currentForm.getScegliTAB().equals("R") )	{
				// carica inventari
				if (currentForm.getDatiOrdine().getBilancio()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3()!=null)	{
					currentForm.getDatiOrdine().getBilancio().setCodice3("4");
				}


			}
			if (currentForm.getDatiOrdine()!=null  && currentForm.getDatiOrdine().isContinuativo())	{
				currentForm.setContin("on");
			}	else	{
				currentForm.setContin(null);
			}
			if (currentForm.getDatiOrdine()!=null  && currentForm.getDatiOrdine().getStatoOrdine()==null)	{
				currentForm.getDatiOrdine().setStatoOrdine("A");
			}

			loadDefault(request, mapping, form);


			return mapping.getInputForward();

    	} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward noScript(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("bid")!=null
					&& session.getAttribute("desc")!=null && session.getAttribute("natura")!=null){
				session.removeAttribute("bid");
				session.removeAttribute("desc");
				session.removeAttribute("natura");
				//return mapping.findForward("indietro");
			}
			// se VAI A , attributeListaSuppOrdiniVO è stato reso null
			// l'indietro torna al chiamante ma senza risultati
			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			Navigation navi = Navigation.getInstance(request);
			if (ricArr!=null  && ricArr.getChiamante()!=null){
				return navi.goBack(true);
//				ActionForward action = new ActionForward();
//				action.setName("RITORNA");
//				action.setPath(ricArr.getChiamante()+".do");
//				return action;
			}else{
				NavigationCache cache = navi.getCache();
				NavigationElement prev = cache.getElementAt(cache.getCurrentPosition() - 1);
				if (prev != null && prev.getForm() instanceof OrdineRicercaParzialeForm) {
					return navi.goBack(true);
				}else if (prev != null && prev.getForm() instanceof SinteticaOrdineForm) {
					return navi.goBack();
				}else{
					return mapping.findForward("indietro");
				}
			}
		}
		catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward visualInv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try
		{
			if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R") &&  currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )
			{
				//esaordini.setElencaInventari(esaordini.getDatiOrdine().getRigheInventariRilegatura());
				currentForm.setNumRigheInv(currentForm.getElencaInventari().size());
			}
			else
			{
				if (currentForm.getNumRigheInv()==0)
				{

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.NOInventariAssociati"));


				}
			}
			return mapping.getInputForward();
		}
		catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	public ActionForward associaInv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try
		{
/*			if (Navigation.getInstance(request).isFromBar()){
                return mapping.getInputForward();
			}*/

			OrdiniVO ordine = currentForm.getDatiOrdine();
			if (ordine!=null && (!ordine.getStatoOrdine().trim().equals("C") && !ordine.getStatoOrdine().trim().equals("N")))
			{
				// EFFETTUAZIONE DEL SALVA PREVENTIVO
				currentForm.setBottoneAssociaInvPremuto(true);
				// GESTIONE DEL BOTTONE ASSOCIAINV DEMANDATA AL BOTTONE SI

//				01.07.09 currentForm.setConferma(true);
//				01.07.09  return Si( mapping,  form,  request,  response);

				return salva( mapping,  form,  request,  response);
				// FINE EFFETTUAZIONE DEL SALVA PREVENTIVO

			}
			else
			{
				return mapping.getInputForward();
			}
		}
		catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			HttpSession session = request.getSession();
			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			if (session.getAttribute("bid")!=null && session.getAttribute("desc")!=null && session.getAttribute("natura")!=null)
			{
				session.removeAttribute("bid");
				session.removeAttribute("desc");
				session.removeAttribute("natura");
			}
			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{

						// carico i risultati della selezione nella variabile da restituire
						session.setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, this.aggiornaRisultatiListaSupportoDaIns(currentForm , ricArr));
					}

					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
				}
				else
				{
					return mapping.getInputForward();
				}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {

			OrdiniVO eleOrdine=currentForm.getDatiOrdine();
			if ( eleOrdine!=null && eleOrdine.getIDOrd()>0)	{
				// il record è già esistente
				// lettura
				OrdiniVO eleOrdineLetto=this.loadDatiINS(currentForm.getDatiOrdine());

				if (eleOrdineLetto!=null )	{
					currentForm.setDatiOrdine(eleOrdineLetto);
				}

				if (eleOrdineLetto!=null  && eleOrdineLetto.isContinuativo())	{
					currentForm.setContin("on");
				}	else	{
					currentForm.setContin(null);
				}
			}	else	{
				this.loadDatiOrdine( currentForm ,request);
				loadDefault(request, mapping, form);
				if (eleOrdine!=null )	{
					if (eleOrdine.getCodBibl()!=null &&  eleOrdine.getCodBibl().trim().length()>0)	{
						currentForm.getDatiOrdine().setCodBibl(eleOrdine.getCodBibl());
					}
					if (eleOrdine.getCodPolo()!=null &&  eleOrdine.getCodPolo().trim().length()>0)	{
						currentForm.getDatiOrdine().setCodPolo(eleOrdine.getCodPolo());
					}
				}
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;

    	try {
    		ActionForward forward = super.controllaPrezzi(mapping, form, request, response);
    		if (forward !=null){
     			//errore
    			if (currentForm.isConferma()){
    				currentForm.setConferma(true);
    				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    				currentForm.setDisabilitaTutto(true);
    			}
				return mapping.getInputForward();
    		}

			// validazione
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			OrdiniVO ordine = currentForm.getDatiOrdine();
			ordine.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
			factory.getGestioneAcquisizioni().ValidaOrdiniVO(ordine);

			// GESTIONE BOTTONE ASSOCIAINV degli ordini di rilegatura
			if (currentForm.isBottoneAssociaInvPremuto())
			{
				currentForm.setBottoneAssociaInvPremuto(false);
				HttpSession session = request.getSession();
				if (ValidazioneDati.isFilled(ordine.getRigheInventariRilegatura()) )
				{
					session.setAttribute("elencoINVANDATA", ordine.getRigheInventariRilegatura() );
				}
				session.setAttribute("chiamante",  mapping.getPath());
				session.setAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE_R, ordine.clone());
				return mapping.findForward("assInv");
			}
			// FINE GESTIONE BOTTONE ASSOCIAINV degli ordini di rilegatura

			// gestione del tipo rilegatura senza inventari associati tck 3026 collaudo
			if (ordine!=null && ordine.getTipoOrdine()!=null && ordine.getStatoOrdine()!=null && ordine.getTipoOrdine().equals("R") && ordine.getStatoOrdine().equals("A") )	{
				if ( ordine.getRigheInventariRilegatura()==null || (ordine.getRigheInventariRilegatura()!=null && ordine.getRigheInventariRilegatura().size()==0))	{
					throw new ValidationException("rilegaturaInventariAssenti",
							ValidationExceptionCodici.rilegaturaInventariAssenti);
				}

			}


    		currentForm.setConferma(true);
    		currentForm.setDisabilitaTutto(true);
    		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();

    	}	catch (ValidationException ve) {
    		SbnErrorTypes error = ve.getErrorCode();
    		if (error != SbnErrorTypes.ERROR_GENERIC)
    			LinkableTagUtils.addError(request, ve);
    		else
    			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			currentForm.setConferma(false);
    		currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();

    	} catch (Exception e) {
			currentForm.setConferma(false);
    		currentForm.setDisabilitaTutto(false);
    		return mapping.getInputForward();
		}
	}


	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);

			if (currentForm.isAdeguamentoPrezzo())		{
				currentForm.setAdeguamentoPrezzo(false); // reset
				// su conferma di adeguamento prezzo previsto al reale
				this.ordineCompletamenteRicevuto( currentForm,  request, true);
				return ripristina( mapping,  form,  request,  response);
			}		else /* non siamo passati per ordine completamente ricevuto*/ {
				// nel passaggio fra i tab, possono essere presenti informazioni di troppo per la tipologia in oggetto

				// sezione
				if (!currentForm.getDatiOrdine().getTipoOrdine().equals("A") && !currentForm.getDatiOrdine().getTipoOrdine().equals("V") )		{
					currentForm.getDatiOrdine().setSezioneAcqOrdine("");
					currentForm.getDatiOrdine().setNumCopieOrdine(0);
				}
				// fine sezione

				//reg trib
				if (!currentForm.getDatiOrdine().getTipoOrdine().equals("A") && !currentForm.getDatiOrdine().getTipoOrdine().equals("L") && !currentForm.getDatiOrdine().getTipoOrdine().equals("V") )		{
					currentForm.getDatiOrdine().setRegTribOrdine("");
				}
				//fine reg trib

				// valuta
				if (!currentForm.getDatiOrdine().getTipoOrdine().equals("A") && !currentForm.getDatiOrdine().getTipoOrdine().equals("V")  && !currentForm.getDatiOrdine().getTipoOrdine().equals("R") )	{
					currentForm.getDatiOrdine().setValutaOrdine("");
					currentForm.getDatiOrdine().setPrezzoOrdine(0);
					currentForm.getDatiOrdine().setPrezzoEuroOrdine(0);
				}
				// fine valuta

				// bilancio
				if (!currentForm.getDatiOrdine().getTipoOrdine().equals("A") && !currentForm.getDatiOrdine().getTipoOrdine().equals("V") && !currentForm.getDatiOrdine().getTipoOrdine().equals("R"))	{
					currentForm.getDatiOrdine().setBilancio(new StrutturaTerna("","",""));
				}
				if (!currentForm.getDatiOrdine().getTipoOrdine().equals("R") && currentForm.getDatiOrdine().getBilancio()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3().equals("4"))	{
					currentForm.getDatiOrdine().getBilancio().setCodice3("");
				}

				// fine bilancio


				// note fornitore e  tipo invio
				if (!currentForm.getDatiOrdine().getTipoOrdine().equals("A")  && !currentForm.getDatiOrdine().getTipoOrdine().equals("R")  )	{
					currentForm.getDatiOrdine().setNoteFornitore("");
					currentForm.getDatiOrdine().setTipoInvioOrdine("");
					currentForm.getDatiOrdine().setCodUrgenzaOrdine("");
				}
				// fine note fornitore e tipo invio



				HttpSession session = request.getSession();
				if (session.getAttribute("bid")!=null && session.getAttribute("desc")!=null && session.getAttribute("natura")!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null && !currentForm.getDatiOrdine().getTipoOrdine().equals("R"))	{
					// nel caso vai a il titolo non può essere modificato
					if (!currentForm.getDatiOrdine().getTitolo().getCodice().equals(session.getAttribute("bid")))	{

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreinserimentoVAIA"));

						currentForm.setConferma(false);
						currentForm.setDisabilitaTutto(false);

						return mapping.getInputForward();

					}
				}
				String ticket=Navigation.getInstance(request).getUserTicket();
				currentForm.getDatiOrdine().setTicket(ticket);
				currentForm.getDatiOrdine().setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				// se il codice ordine è già valorzzato si deve procedere alla modifica

				if (currentForm.getDatiOrdine().getCodOrdine()!=null && currentForm.getDatiOrdine().getCodOrdine().length()>0 )	{
					if (!this.modificaOrdine(currentForm.getDatiOrdine())) {

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreModifica"));

						//return mapping.getInputForward();
					}	else	{
						if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null && (currentForm.getDatiOrdine().getTipoOrdine().equals("D") || currentForm.getDatiOrdine().getTipoOrdine().equals("L") || currentForm.getDatiOrdine().getTipoOrdine().equals("C") ))		{
							currentForm.setStatiCDL(true);
							if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getStatoOrdine()!=null && currentForm.getDatiOrdine().getStatoOrdine().equals("C")  )		{
								currentForm.setDisabilitaTutto(true);
							}
						}


						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.modificaOK"));

						this.ripristina( mapping,  form,  request,  response);
						return forwardMappaGestione(mapping, request, currentForm);

					}

				}else{
					if (this.inserisciOrdine(currentForm.getDatiOrdine()))	{
						if (session.getAttribute("bid")!=null && session.getAttribute("desc")!=null && session.getAttribute("natura")!=null)		{
							session.removeAttribute("bid");
							session.removeAttribute("desc");
							session.removeAttribute("natura");
						}
						// obbligare all'inserimento degli inventari se non esistenti

						if (!currentForm.getDatiOrdine().getTipoOrdine().equals("R") && !currentForm.getDatiOrdine().getTipoOrdine().equals("A") )	{

							// controllo esistenza inventari
							List listaInv=null;
							try 	{
								String codPolo=currentForm.getDatiOrdine().getCodPolo();
								String codBib=currentForm.getDatiOrdine().getCodBibl();
								String codBibO=currentForm.getDatiOrdine().getCodBibl();
								String codTipOrd=currentForm.getDatiOrdine().getTipoOrdine();
								String codBibF="";
								//String ticket=ordine.getTicket();
								ticket=currentForm.getDatiOrdine().getTicket();
								int   annoOrd=Integer.valueOf(currentForm.getDatiOrdine().getAnnoOrdine().trim());
								int   codOrd=Integer.valueOf(currentForm.getDatiOrdine().getCodOrdine());
								int   nRec=999;

								FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
								listaInv = factory.getGestioneDocumentoFisico().getListaInventariOrdine( codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF,this.getLocale(request, Constants.SBN_LOCALE), ticket,  nRec).getLista();

							} catch (Exception e) {
								// TODO Auto-generated catch block
								// l'errore capita in questo punto
								e.printStackTrace();
							}

							// obbligo ad inserire inventari per l'inserimento e non per la modifica
							// tck 2861- Gli ordini per visione trattenuta devono avviare direttamente all'attribuzione dell'inventario, come i doni, i depositi legali e i cambi.
							if (currentForm.getDatiOrdine().getTipoOrdine().equals("L") || currentForm.getDatiOrdine().getTipoOrdine().equals("D") || currentForm.getDatiOrdine().getTipoOrdine().equals("C") || currentForm.getDatiOrdine().getTipoOrdine().equals("V") )	{
								currentForm.setStatiCDL(true);
								if (listaInv==null)	{
									try {

										if (Navigation.getInstance(request).isFromBar()){
											return mapping.getInputForward();
										}

										request.setAttribute("codBibF", "");

										request.setAttribute("codBibO", currentForm.getDatiOrdine().getCodBibl());

										request.setAttribute("bid", currentForm.getDatiOrdine().getTitolo().getCodice());

										request.setAttribute("codTipoOrd", currentForm.getDatiOrdine().getTipoOrdine());

										request.setAttribute("annoOrd", currentForm.getDatiOrdine().getAnnoOrdine());

										request.setAttribute("codOrd", currentForm.getDatiOrdine().getCodOrdine());

										request.setAttribute("codFornitore", currentForm.getDatiOrdine().getFornitore());


										request.setAttribute("prov", "ordineIns");
										//
									} catch (Exception e) {
										//return mapping.getInputForward();
										//e.printStackTrace();
									}
								}	else	{
									// inventari esistenti

									LinkableTagUtils.addError(request, new ActionMessage(
									"errors.acquisizioni.inserimentoOK"));


								}

							}

						}
						// fine obbligo inserimento inventari

						// GESTIONE BOTTONE ASSOCIAINV degli ordini di rilegatura
						if (currentForm.isBottoneAssociaInvPremuto()){
							currentForm.setBottoneAssociaInvPremuto(false);
							if (currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0)	{
								session.setAttribute("elencoINVANDATA", currentForm.getDatiOrdine().getRigheInventariRilegatura());
							}
							session.setAttribute("chiamante",  mapping.getPath());
							session.setAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE_R, currentForm.getDatiOrdine().clone());
							return mapping.findForward("assInv");
						}
						// FINE GESTIONE BOTTONE ASSOCIAINV degli ordini di rilegatura



						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.inserimentoOK"));

						//							return ripristina( mapping,  form,  request,  response);
						this.ripristina( mapping,  form,  request,  response);
						return forwardMappaGestione(mapping, request, currentForm);

					}	else	{

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreinserimento"));

					}
				}
			}
			//if (currentForm.getDatiOrdine().)
			// gestire se si vuole che nelle tipologie d, l,..ecce lo stato deve essere modificabile anche se viene impostato a chiuso
			//currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();

		}	catch (ValidationException ve) {

			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);


			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);


			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setConferma(false);
		currentForm.setDisabilitaTutto(false);
		try
		{
			if (currentForm.isAdeguamentoPrezzo()) // in caso di no deve essere solo chiuso senza adeguamento prezzo
			{
				currentForm.setAdeguamentoPrezzo(false); // reset
				this.ordineCompletamenteRicevuto( currentForm,  request, false);
				return ripristina( mapping,  form,  request,  response);
			}

		}	catch (ValidationException ve) {

				currentForm.setConferma(false);

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			currentForm.setConferma(false);

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}


		return mapping.getInputForward();
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward importada(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			HttpSession session = request.getSession();
			session.setAttribute("chiamante",  mapping.getPath());
			//request.setAttribute("chiamante", mapping.getName());
			String ticket=Navigation.getInstance(request).getUserTicket();
			currentForm.getDatiOrdine().setTicket(ticket);
			OrdiniVO eleOrdineAttivo=currentForm.getDatiOrdine();
			session.setAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO,eleOrdineAttivo.clone() );
			return mapping.findForward("importada");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward bibloaffil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			request.getSession().setAttribute("chiamante", mapping.getPath());
			request.setAttribute("biblInOgg", currentForm.getDatiOrdine().getCodBibl());
			return mapping.findForward("bibloaffil");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
    	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE)==null
			HttpSession session = request.getSession();
			session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			session.removeAttribute("sezioniSelected");
			session.removeAttribute("criteriRicercaSezione");

			this.impostaSezioneCerca( currentForm , request,mapping);
			//return mapping.findForward("sezioneCerca");
			return mapping.findForward("sezioneLista");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaSezioneCerca( EsaminaOrdineForm currentForm , HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getDatiOrdine().getCodBibl();
		String codSez=currentForm.getDatiOrdine().getSezioneAcqOrdine();
		String desSez="";
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
	}catch (Exception e) {	}
	}


	public ActionForward fornPreferiti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	   	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		if(Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		try {
			if (currentForm.getDatiOrdine().getTitolo().getCodice()!=null && currentForm.getDatiOrdine().getTitolo().getCodice().trim().length()>0
				&& 	currentForm.getDatiOrdine().getSezioneAcqOrdine()!=null && currentForm.getDatiOrdine().getSezioneAcqOrdine().trim().length()>0
				)
			{

				//this.controllaTitolo(esaord,currentForm.getDatiOrdine().getTitolo().getCodice());

				HttpSession session = request.getSession();
				session.removeAttribute("attributeListaSuppFornitoreVO");
				session.removeAttribute("fornitoriSelected");
				session.removeAttribute("criteriRicercaFornitore");

				this.impostaFornitoreCerca( currentForm , request,mapping, false);

				ListaSuppFornitoreVO eleRicerca=(ListaSuppFornitoreVO) session.getAttribute("attributeListaSuppFornitoreVO");

				if (currentForm.getDatiOrdine().getTitolo().getCodice()!=null && currentForm.getDatiOrdine().getTitolo().getCodice().trim().length()>0) // caso di fornitori di profilo
				{
					boolean valRitorno = false;
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					String ticket=Navigation.getInstance(request).getUserTicket();
					List lista = null;

					TitoloACQVO recTit=null;
					recTit=factory.getGestioneAcquisizioni().getTitoloRox(currentForm.getDatiOrdine().getTitolo().getCodice());
					if (recTit!=null)
					{

						// aggiungo condizioni
						if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTitolo().getDescrizione()!=null && currentForm.getDatiOrdine().getTitolo().getDescrizione().trim().length()==0)
						{
							currentForm.getDatiOrdine().getTitolo().setDescrizione(recTit.getIsbd());
						}
						if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getNaturaOrdine()!=null && currentForm.getDatiOrdine().getNaturaOrdine().trim().length()==0)
						{
							currentForm.getDatiOrdine().setNaturaOrdine(recTit.getNatura());
						}
						if (recTit.getCodPaese()!=null && recTit.getCodPaese().trim().length()>0
								&& recTit.getArrCodLingua()!=null && recTit.getArrCodLingua().length>0)
						{
							eleRicerca.setPaese(recTit.getCodPaese());
							eleRicerca.setCodLingua(recTit.getArrCodLingua()[0]);
							eleRicerca.setCodSezione(currentForm.getDatiOrdine().getSezioneAcqOrdine());
							session.setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
							return mapping.findForward("listaForn");
						}
						else // efettuare il test
						{
							//throw new Exception("non esistono fornitori per profilo di acquisto in base ai dati presenti");

							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.profAcqInesistenti" ));

							return mapping.getInputForward();
						}
					}
					else // efettuare il test
					{
						// reset dei dati del titolo non trovato
						currentForm.getDatiOrdine().getTitolo().setDescrizione("");
						currentForm.getDatiOrdine().setNaturaOrdine("");

						//throw new Exception("non esistono fornitori per profilo di acquisto in base ai dati presenti");

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.profAcqInesistenti" ));

						return mapping.getInputForward();
					}
				}
				else // efettuare il test
				{
					//throw new Exception("non esistono fornitori per profilo di acquisto in base ai dati presenti");

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.profAcqInesistenti" ));

					return mapping.getInputForward();
				}
			}
			else
			{
				//throw new Exception("non esistono fornitori per profilo di acquisto in base ai dati presenti");

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.profAcqInesistenti" ));

				return mapping.getInputForward();
			}
			//return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.getInputForward();
		}
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	   	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, currentForm);
			if (forward != null){
				return forward;
			}
			/*//rosa
			FornitoreVO fornVO = null;
			if (currentForm.getDatiOrdine().getAnagFornitore().getCodFornitore() != null &&
					currentForm.getDatiOrdine().getAnagFornitore().getCodFornitore().equals("")){
				if (currentForm.getDatiOrdine().getAnagFornitore().getNomeFornitore() != null &&
						currentForm.getDatiOrdine().getAnagFornitore().getNomeFornitore().equals("")){
					//sif
				}else{
					//cerco il fornitore per nome
					fornVO = this.getFornitore(Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(),
							null, currentForm.getDatiOrdine().getAnagFornitore().getNomeFornitore(), Navigation.getInstance(request).getUserTicket());
					if (fornVO != null){
						currentForm.getDatiOrdine().getAnagFornitore().setCodFornitore(fornVO.getCodFornitore().toString());
						currentForm.getDatiOrdine().getAnagFornitore().setNomeFornitore(fornVO.getNomeFornitore());
					}else{
						//sif
					}
				}
			}else{
				//cerco il fornitore per cod
				fornVO = this.getFornitore(Navigation.getInstance(request).getUtente().getCodPolo(), Navigation.getInstance(request).getUtente().getCodBib(),
						currentForm.getDatiOrdine().getAnagFornitore().getCodFornitore(), null, Navigation.getInstance(request).getUserTicket());
				if (fornVO != null){
					currentForm.getDatiOrdine().getAnagFornitore().setCodFornitore(fornVO.getCodFornitore().toString());
					currentForm.getDatiOrdine().getAnagFornitore().setNomeFornitore(fornVO.getNomeFornitore());
				}else{
					//sif
				}
			}
			//rosa*/

			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			HttpSession session = request.getSession();
			session.removeAttribute("attributeListaSuppFornitoreVO");
			session.removeAttribute("fornitoriSelected");
			session.removeAttribute("criteriRicercaFornitore");

			this.impostaFornitoreCerca( currentForm , request,mapping, true);
			return mapping.findForward("fornitoreCerca");
//		} catch (DataException e) {
//			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( EsaminaOrdineForm currentForm , HttpServletRequest request, ActionMapping mapping, boolean cercaF)
	{
		//impostazione di una lista di supporto
		try {
			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=currentForm.getDatiOrdine().getCodBibl();
			// si esclude l'impostazione del fornitore per la ricerca di fornitori di profilo
			String codForn="";
			String nomeForn="";
			if (cercaF)
			{
				codForn=currentForm.getDatiOrdine().getFornitore().getCodice();
				nomeForn=currentForm.getDatiOrdine().getFornitore().getDescrizione().trim();
			}
			String codProfAcq="";
			String paeseForn="";
			String tipoPForn="";
			if (currentForm.getDatiOrdine().getTipoOrdine().equals("R"))
			{
				tipoPForn="R";
			}
			if (currentForm.getDatiOrdine().getTipoOrdine().equals("D"))
			{
				tipoPForn="C";
			}
			String provForn="";
			String loc="0"; // ricerca sempre locale
			String chiama=mapping.getPath();
			//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
			eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
			//ricerca.add(eleRicerca);
			eleRicerca.setModalitaSif(false);
			request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
		}catch (Exception e) {
		}
	}


	public ActionForward bilancioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	   	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			HttpSession session = request.getSession();
			session.removeAttribute("attributeListaSuppBilancioVO");
			session.removeAttribute("bilanciSelected");
			session.removeAttribute("criteriRicercaBilancio");

			this.impostaBilancioCerca( currentForm ,request,mapping);
			return mapping.findForward("bilancioCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaBilancioCerca(EsaminaOrdineForm currentForm ,  HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getDatiOrdine().getCodBibl();
		String ese=currentForm.getDatiOrdine().getBilancio().getCodice1();
		String cap=currentForm.getDatiOrdine().getBilancio().getCodice2();
		String imp=currentForm.getDatiOrdine().getBilancio().getCodice3();
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppBilancioVO", eleRicerca);

	}catch (Exception e) {	}
	}

	public ActionForward sifbid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	   	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		try {
			if (currentForm.getDatiOrdine().getTitolo()!=null && currentForm.getDatiOrdine().getTitolo().getCodice()!=null)
			{
				request.setAttribute("bidFromRicOrd", currentForm.getDatiOrdine().getTitolo().getCodice());
			}
			return Navigation.getInstance(request).goForward(mapping.findForward("sifbid"));
			}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}

	private void loadDatiOrdine(EsaminaOrdineForm currentForm ,HttpServletRequest request) throws Exception {
		// caricamento offerta
		int anno = DateUtil.getYear(DaoManager.now());
		String annoOrd = Integer.valueOf(anno).toString();

		ConfigurazioneORDVO conf = new ConfigurazioneORDVO();
		conf.setCodBibl(currentForm.getDatiOrdine().getCodBibl());
		conf.setCodPolo(currentForm.getDatiOrdine().getCodPolo());
		ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(conf);

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


		// ASSEGNAZIONE DELLA data di sistema
		Date dataodierna=new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataOdiernaStr =formato.format(dataodierna);
		//String dataOdiernaStr=DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataodierna);
		//String codP, String codB, String codOrd, String annoOrd, String tipoOrd, String dataOrd, String noteOrd, int numCopieOrd, boolean cont, String statoOrd, String codDocOrd, String codTipoDocOrd,("1", "") String codUrgenzaOrd, String codRicOffertaOrd, String idOffertaFornOrd, StrutturaCombo forn, String noteForn, String tipoInvioOrd, StrutturaTerna bil, String codPrimoOrd, String annoPrimoOrd, String valutaOrd, String prezzoOrd, String prezzoEuroOrd, String paeseOrd, String sezioneAcqOrd, String codBibliotecaSuggOrd (codice del bibliotecario), String codSuggBiblOrd (arg, 1), StrutturaCombo tit, String statoAbbOrd, String periodoValAbbOrd, String annoAbbOrd, String numFascicoloAbbOrd, String dataPubblFascicoloAbbOrd, String annataAbbOrd, int  numVolAbbOrd, String dataFineAbbOrd, String regTribOrd, String naturaOrd, boolean stamp, String tipoVar)
		OrdiniVO ordi = new OrdiniVO("", "", "", annoOrd, "", dataOdiernaStr,
				"", 0, false, "A", "", "", "", "", "", new StrutturaCombo("",
						""), "", "", new StrutturaTerna("", "", ""), "", "",
				null, 0, 0, "", "", "0", "", new StrutturaCombo("", ""), "",
				"", "", "", "", "", 0, "", "", "", false, false, "");
		if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getCodBibl()!=null && currentForm.getDatiOrdine().getCodBibl().trim().length()>0)
		{
			ordi.setCodBibl(currentForm.getDatiOrdine().getCodBibl());
		}
		if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getCodPolo()!=null && currentForm.getDatiOrdine().getCodPolo().trim().length()>0)
		{
			ordi.setCodPolo(currentForm.getDatiOrdine().getCodPolo());
		}
		ordi.setPrezzoOrdineStr("0,00");
		ordi.setPrezzoEuroOrdineStr("0,00");
		//rosa
		currentForm.setPrezzoStr(ordi.getPrezzoOrdineStr());
		currentForm.setPrezzoEurStr(ordi.getPrezzoEuroOrdineStr());
		currentForm.setValuta(ordi.getValutaOrdine());
		//
		// controllo se la ricerca è stata richiamata dal VAI A con esito negativo
		HttpSession session = request.getSession();
		String bid = (String) session.getAttribute("bid");
		String desc = (String) session.getAttribute("desc");
		String natura = (String) session.getAttribute("natura");

		if (bid!=null && bid.length()!=0 && desc!=null && desc.length()!=0 && natura!=null && natura.length()!=0)
		{
			ordi.getTitolo().setCodice(bid);
			ordi.getTitolo().setDescrizione(desc);
			ordi.setNaturaOrdine(natura);
		}
		ordi.setStatoOrdine("A");
		if (currentForm.getBidNotiziaCorrente() != null){
			ordi.getTitolo().setCodice(currentForm.getBidNotiziaCorrente());
		}
		//gestire l'esistenza del risultato e che sia univoco

		// TODO lettura configurazione ordini (se non obbligatorio mettere a false)
		// TODO SE TRUE IMPOSTARE I VALORI DI DEFAULT SE SPECIFICATI
/*		ordi.setGestBil(false);
		ordi.setIDBil(0);
		ordi.setBilancio(new StrutturaTerna("", "", ""));

		ordi.setGestSez(false);
		ordi.setIDSez(0);
		ordi.setSezioneAcqOrdine("");


		if (!ordi.isGestSez())
		{
			ordi.setGestProf(false);
		}
			*/

		currentForm.setDatiOrdine(ordi);
		if (currentForm.getDatiOrdine()!=null  && currentForm.getDatiOrdine().isContinuativo())		{
			currentForm.setContin("on");
		}		else		{
			currentForm.setContin(null);
		}
		if (gestBil!=null && !gestBil)		{
			currentForm.getDatiOrdine().setGestBil(false);
		}
		if (gestSez!=null && !gestSez)		{
			currentForm.getDatiOrdine().setGestSez(false);
		}
		if (gestProf!=null && !gestProf)		{
			currentForm.getDatiOrdine().setGestProf(false);
		}

		// solo per ordini di rilegatura
		if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R")  )	{
			if (currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  ){
				for (int i=0;  i < currentForm.getDatiOrdine().getRigheInventariRilegatura().size(); i++) {
					StrutturaInventariOrdVO ele=currentForm.getDatiOrdine().getRigheInventariRilegatura().get(i);
					if (ele.getTitolo()==null) 	{
						ele.setTitolo("");
					}
					String	titoInv="";
					titoInv=this.controllaTitolo(ele.getBid());

					if (titoInv!=null) 	{
						ele.setTitolo(titoInv);
					}else{
						ele.setTitolo("");
					}
				}
			}
			//esaordini.getDatiOrdine().setRigheInventariRilegatura(listaInv);
			currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
			currentForm.setNumRigheInv(currentForm.getDatiOrdine().getRigheInventariRilegatura().size());
		}
	}
	private String controllaTitolo(String bidPassato) throws Exception {
		String tito=null;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//List lista = null;
			TitoloACQVO tit=null;
			tit = factory.getGestioneAcquisizioni().getTitoloRox(bidPassato);
			if (tit!=null)
			{
				//TitoloACQVO titoloTrovatoElemento = (TitoloACQVO) lista.get(0);
				tito=tit.getIsbd();
			}
		} catch (Exception e) {
		    	e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
		}
		return tito;
	}


	private void controllaTitolo(EsaminaOrdineForm currentForm, String bidPassato) throws Exception {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//List lista = null;
			TitoloACQVO tit=null;
			tit = factory.getGestioneAcquisizioni().getTitoloRox(bidPassato);
			if (tit!=null)
			{
				//TitoloACQVO titoloTrovatoElemento = (TitoloACQVO) lista.get(0);
				currentForm.getDatiOrdine().getTitolo().setCodice(bidPassato);
				currentForm.getDatiOrdine().getTitolo().setDescrizione(tit.getIsbd());
				currentForm.getDatiOrdine().setNaturaOrdine(tit.getNatura());
			}
		} catch (Exception e) {
		    	e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
		}

	}

    private void loadPeriodo(EsaminaOrdineForm currentForm ) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1 - annuale");
		lista.add(elem);
		elem = new StrutturaCombo("2","2 - biennale");
		lista.add(elem);
		elem = new StrutturaCombo("3","3 - triennale");
		lista.add(elem);
		currentForm.setListaPeriodo(lista);
	}

	private boolean inserisciOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciOrdine(ordine);

		// test di inserimento ordine su n bibl PER CENTRO SERVIZI CON BIBL AFFILIATE
		//		TODO  gestire le biblioteche affiliate
		//List<String> appo=new ArrayList<String>();
		//appo.add(" IC");
		//valRitorno = factory.getGestioneAcquisizioni().inserisciOrdineBiblHib(ordine, appo);

		return valRitorno;
	}

	private boolean modificaOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaOrdine(ordine);
		return valRitorno;
	}



	private void loadBiblAffil(EsaminaOrdineForm currentForm ) throws Exception {
		List<StrutturaCombo> listaBiblAff = new ArrayList<StrutturaCombo>();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		// decommentare se si vuole la gestione del bottone bilio affil.
		//ok listaBiblAff = (List<StrutturaCombo>) factory.getGestioneAcquisizioni().getRicercaBiblAffiliate(currentForm.getDatiOrdine().getCodBibl(),CodiciAttivita.getIstance().GA_GESTIONE_ORDINI);
		// oppure
		//		inizio ricerca centro sistema
/*        try {
			DescrittoreBloccoVO blocco1 = factory.getSistema().getListaBibliotecheAffiliatePerAttivita(
					utente.getTicket(), utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_ORDINI,
					1000);
			if (blocco1.getTotRighe() >1) {
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
*/		//fine ricerca centro sistema

		if (listaBiblAff!=null && listaBiblAff.size()!=0)
		{
			currentForm.setBiblioNONCentroSistema(false);
		}
	}


    private void initCombo(EsaminaOrdineForm form) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	    CaricamentoCombo carCombo = new CaricamentoCombo();

		form.setListaNatura(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceNaturaOrdine()));
		form.setListaStato(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceStatoOrdine()));
		form.setListaTipoImpegno(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale()));
		form.setListaTipoInvio(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoInvio()));
		form.setListaUrg(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoUrgenza()));
	}

	private ListaSuppOrdiniVO aggiornaRisultatiListaSupportoDaIns (EsaminaOrdineForm currentForm , ListaSuppOrdiniVO eleRicArr)
	{
		try {
			List<OrdiniVO> risultati=new ArrayList();
			OrdiniVO eleOrdine=currentForm.getDatiOrdine();
			risultati.add(eleOrdine);
			eleRicArr.setSelezioniChiamato(risultati);
		} catch (Exception e) {

		}
		return eleRicArr;
	}


	private OrdiniVO loadDatiINS(OrdiniVO ele ) throws Exception
	{

		OrdiniVO eleLetto =null;

		ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
		// condizioni di ricerca univoca

		// carica i criteri di ricerca da passare alla esamina
		String codP=ele.getCodPolo();
		String codB=ele.getCodBibl();
		String codBAff ="";
		String codOrd=ele.getCodOrdine();
		String tipoOrd=ele.getTipoOrdine();
		String dataOrdDa="";
		String dataOrdA="";
		String cont="" ;
		//String statoOrd=ricOrdini.getStato();
		String statoOrd=null;
		StrutturaCombo forn=new StrutturaCombo ("","");
		String tipoInvioOrd="";
		StrutturaTerna bil=new StrutturaTerna("","","");
		String sezioneAcqOrd="";
		StrutturaCombo tit=new StrutturaCombo ("","");
		String dataFineAbbOrdDa="";
		String dataFineAbbOrdA="";
		String naturaOrd="";
		//String chiama=null;
		String[] statoOrdArr=null;
		Boolean stamp=ele.isStampato();
		Boolean rinn=ele.isRinnovato();
		// prova
		String annoOrd=ele.getAnnoOrdine();
		String ordina="";
		String chiama=null;
		eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp );

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<OrdiniVO> recordTrovati = new ArrayList();
		try {
			recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaOrdini(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (recordTrovati.size()>0)
		{
			eleLetto=recordTrovati.get(0);
			try {
				eleLetto.setPrezzoOrdineStr(Pulisci.VisualizzaImporto( eleLetto.getPrezzoOrdine()));
				eleLetto.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto(eleLetto.getPrezzoEuroOrdine()));

			} catch (Exception e) {
			    	//e.printStackTrace();
			    	//throw new ValidationException("importoErrato",
			    	//		ValidationExceptionCodici.importoErrato);
				eleLetto.setPrezzoOrdineStr("0,00");
				eleLetto.setPrezzoEuroOrdineStr("0,00");

			}




		}

		return eleLetto;
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


	private void ordineCompletamenteRicevuto(EsaminaOrdineForm currentForm, HttpServletRequest request, Boolean adegua) throws Exception {

		try
		{
			List listaInv=null;
			try
			{
				  String codPolo=currentForm.getDatiOrdine().getCodPolo();
		          String codBib=currentForm.getDatiOrdine().getCodBibl();
		          String codBibO=currentForm.getDatiOrdine().getCodBibl();
		          String codTipOrd=currentForm.getDatiOrdine().getTipoOrdine();
		          String codBibF="";
		          //String ticket=ordine.getTicket();
		          String ticket2=currentForm.getDatiOrdine().getTicket();
		          int   annoOrd=Integer.valueOf(currentForm.getDatiOrdine().getAnnoOrdine().trim());
		          int   codOrd=Integer.valueOf(currentForm.getDatiOrdine().getCodOrdine());
		          int   nRec=999;

		          FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		          listaInv = factory.getGestioneDocumentoFisico().getListaInventariOrdine( codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF,this.getLocale(request, Constants.SBN_LOCALE), ticket2,  nRec).getLista();

		          //listaInv=this.getInventariRox(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, ticket,  nRec);
		         // listaInv= inventario.getListaInventariOrdine(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, ticket,  nRec);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// l'errore capita in questo punto
				//e.printStackTrace();
			}
			if (listaInv!=null && listaInv.size()>0)
			{
				// calcolo del prezzo reale
				double totImportInv=0.00;
				if (adegua)
				{
					for (int t=0; t<listaInv.size(); t++)
					{
						// listaInv.get(t)
						InventarioListeVO eleInve=(InventarioListeVO)listaInv.get(t);
						double importInv=0;
						if (eleInve!=null && eleInve.getImportoDouble()>0)
						{
							importInv=eleInve.getImportoDouble();
						}
						totImportInv=totImportInv + importInv;
					}
				}
				OrdiniVO eleOrdine=currentForm.getDatiOrdine();
				eleOrdine.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
				String oldStatoOrdine=eleOrdine.getStatoOrdine();
				eleOrdine.setStatoOrdine("C");
				if (adegua)
				{
					if (totImportInv>0)
					{
						if (eleOrdine.getTipoOrdine().equals("A") || eleOrdine.getTipoOrdine().equals("V") )
						{
							eleOrdine.setPrezzoEuroOrdine(totImportInv);
							eleOrdine.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( eleOrdine.getPrezzoEuroOrdine()));
						}
					}
				}

				currentForm.setDatiOrdine(eleOrdine);
				currentForm.setDisabilitaTutto(true);
				if (this.modificaOrdine(eleOrdine))
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.ordineChiusoCompletato"));

				}
				else
				{
					eleOrdine.setStatoOrdine(oldStatoOrdine);
					// messaggio di incompletezza sell'inserimento per l'assenza inventari
				}
			}

		}	catch (ValidationException ve) {
			throw ve;
		}	catch (Exception e) {
			throw e;
		}


	}
	private ActionForward loadDefault(HttpServletRequest request,ActionMapping mapping, ActionForm form) {

	   	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;
		//if (!currentForm.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

			try {
				if (currentForm.getDatiOrdine()!=null)
				{
					if (currentForm.getDatiOrdine().getTipoOrdine()==null || currentForm.getDatiOrdine().getTipoOrdine().trim().length()==0)
					{
						// si forza a tipo=a
						currentForm.getDatiOrdine().setTipoOrdine("A");
					}
					if(currentForm.getDatiOrdine().getFornitore()==null  )
					{
						currentForm.getDatiOrdine().setFornitore(new StrutturaCombo("", ""));
					}
					// solo se non è già stato impostato il fornitore
					if(currentForm.getDatiOrdine().getFornitore()!=null && currentForm.getDatiOrdine().getFornitore().getCodice()!=null && currentForm.getDatiOrdine().getFornitore().getCodice().trim().length()==0   )
					{
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("A"))
						{
							currentForm.getDatiOrdine().getFornitore().setCodice((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_FORN_TIPO_A));
						}
						// vedere gli altri casi
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("V"))
						{
							currentForm.getDatiOrdine().getFornitore().setCodice((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_FORN_TIPO_V));
						}
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("L"))
						{
							currentForm.getDatiOrdine().getFornitore().setCodice((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_FORN_TIPO_L));
						}
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("C"))
						{
							currentForm.getDatiOrdine().getFornitore().setCodice((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_FORN_TIPO_C));
						}
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("D"))
						{
							currentForm.getDatiOrdine().getFornitore().setCodice((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_FORN_TIPO_D));
						}
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("R"))
						{
							currentForm.getDatiOrdine().getFornitore().setCodice((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_FORN_TIPO_R));
						}
					}
					// solo se non è già stata impostata la sezione
					if(currentForm.getDatiOrdine().isGestSez() && currentForm.getDatiOrdine().getSezioneAcqOrdine()!=null && currentForm.getDatiOrdine().getSezioneAcqOrdine().trim().length()==0   )
					{
						// solo tipo ord=A, V
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("V") || currentForm.getDatiOrdine().getTipoOrdine().equals("A"))
						{
							currentForm.getDatiOrdine().setSezioneAcqOrdine((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_SEZIONE));
						}
					}
					// solo se non è già stato impostato l'esercizio
					if(currentForm.getDatiOrdine().isGestBil() && currentForm.getDatiOrdine().getBilancio()!=null && currentForm.getDatiOrdine().getBilancio().getCodice1()!=null && currentForm.getDatiOrdine().getBilancio().getCodice1().trim().length()==0)
					{
						// solo tipo ord=A, V
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("V") || currentForm.getDatiOrdine().getTipoOrdine().equals("A") || currentForm.getDatiOrdine().getTipoOrdine().equals("R"))
						{
							currentForm.getDatiOrdine().getBilancio().setCodice1((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_ESERCIZIO));
						}
					}
					// solo se non è già stato impostato il bilancio
					if(currentForm.getDatiOrdine().isGestBil() && currentForm.getDatiOrdine().getBilancio()!=null && currentForm.getDatiOrdine().getBilancio().getCodice2()!=null && currentForm.getDatiOrdine().getBilancio().getCodice2().trim().length()==0)
					{
						// solo tipo ord=A, V
						if (currentForm.getDatiOrdine().getTipoOrdine().equals("V") || currentForm.getDatiOrdine().getTipoOrdine().equals("A") || currentForm.getDatiOrdine().getTipoOrdine().equals("R"))
						{
							currentForm.getDatiOrdine().getBilancio().setCodice2((String) utenteEjb.getDefault(ConstantDefault.GA_CREA_ORD_CAPITOLO));
						}
					}

				}
			} catch (Exception e) {
				LinkableTagUtils.resetErrors(request);
				LinkableTagUtils.addError(request, new ActionMessage("error.acquisizioni.erroreDefault"));

				return mapping.getInputForward();
			}
		//}

		return mapping.getInputForward();
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		if (idCheck.equals("ORDINEDEPOSITOLEGALEDONO") ){

			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		if (idCheck.equals("ORDINEACQUISTOVISIONETRATTENUTA") ){

			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		if (idCheck.equals("ORDINECAMBIO") ){

			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_SCAMBIO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		if (idCheck.equals("ORDINERILEGATURA") ){

			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_RILEGATURA, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		return super.checkAttivita(request, form, idCheck);
	}

	private ActionForward forwardMappaGestione(ActionMapping mapping, HttpServletRequest request, ActionForm form) throws RemoteException {
	   	EsaminaOrdineForm currentForm = (EsaminaOrdineForm) form;

		String[] appoSelezione = new String[0];
		OrdiniVO datiOrdine = currentForm.getDatiOrdine();
		String ordine = datiOrdine.getCodBibl() + "|"
				+ datiOrdine.getTipoOrdine() + "|"
				+ datiOrdine.getAnnoOrdine() + "|"
				+ datiOrdine.getProgressivo();

		appoSelezione = new String[] { ordine };
		//this.AggiornaParametriSintetica(request);
		this.preparaRicercaOrdine(currentForm,request);
		// si aggiorna l'attributo con l'elenco dei cambi trovati
		HttpSession session = request.getSession();
		ListaSuppOrdiniVO ricArr = (ListaSuppOrdiniVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
		session.setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, this.aggiornaRisultatiListaSupportoDaIns( currentForm , ricArr));
		session.setAttribute("ordiniSelected", appoSelezione);

		Navigation navi = Navigation.getInstance(request);
		navi.purgeThis();

		NavigationForward forward = null;
		String prov = (String) request.getAttribute("prov");
		if (ValidazioneDati.equals(prov, "ordineIns")) {
			LinkableTagUtils.resetErrors(request);
			forward = navi.goForward(mapping.findForward("esaminaCDL"));
		} else
			forward = navi.goForward(mapping.findForward("esamina"));

		forward.setRedirect(true);	//pulisco request per evitare errore bean populate
		forward.addParameter("prov", "SiDiEsaminaOrdine");
		return forward;
	}

	private void  preparaRicercaOrdine(EsaminaOrdineForm currentForm,  HttpServletRequest request) {

		try {
			List<ListaSuppOrdiniVO> ricerca=new ArrayList();
			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
			String ticket=Navigation.getInstance(request).getUserTicket();

			// carica i criteri di ricerca da passare alla esamina
			OrdiniVO ordine = currentForm.getDatiOrdine();
			OrdiniVO eleElenco=ordine;
			String chiaveComposta=eleElenco.getChiave();
			String codP=ordine.getCodPolo();
			String codB=ordine.getCodBibl();
			String codBAff=null;
			String codOrd=ordine.getCodOrdine();
			String annoOrd=ordine.getAnnoOrdine();
			String tipoOrd=ordine.getTipoOrdine();
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
			Boolean rinn=ordine.isRinnovato();
			Boolean stamp=ordine.isStampato();
			eleRicerca=new ListaSuppOrdiniVO( codP, codB,  codBAff,   codOrd,  annoOrd,  tipoOrd,  dataOrdDa, dataOrdA,   cont,  statoOrd,  forn,   tipoInvioOrd,  bil,    sezioneAcqOrd,  tit,   dataFineAbbOrdDa,  dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn, stamp);
			eleRicerca.setTicket(ticket);
			eleRicerca.setOrdinamento("");

			ricerca.add(eleRicerca);
			request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE, ricerca);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



