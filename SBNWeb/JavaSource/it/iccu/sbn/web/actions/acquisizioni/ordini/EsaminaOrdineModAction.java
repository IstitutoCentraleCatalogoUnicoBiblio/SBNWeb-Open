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
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO.TipoOperazione;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuoniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuonoOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.EsaminaOrdineModForm;
import it.iccu.sbn.web.actionforms.acquisizioni.ordini.OrdineBaseForm;
import it.iccu.sbn.web.actions.acquisizioni.util.OrdiniWebUtil;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.acquisizioni.AcquisizioniDelegate;
import it.iccu.sbn.web.integration.bd.periodici.PeriodiciDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.NavigationListener;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EsaminaOrdineModAction extends OrdineBaseAction implements SbnAttivitaChecker, NavigationListener {


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("ricerca.button.indietro", "indietro");
		map.put("ricerca.button.bibloaffil", "bibloaffil");
		map.put("ricerca.button.operazionesuordine", "operazioneSuOrdine");
		map.put("ricerca.button.buonoOrdine", "buonoOrdine");
		map.put("ricerca.button.inventari", "inventari");
		map.put("ricerca.button.listaInventariOrdine", "listaInventariOrdine");
		map.put("ricerca.button.schedoneRinnovi", "schedaRinnovi");
		map.put("ricerca.button.periodici", "kardexPeriodici");
		map.put("ricerca.button.stampa", "stampa");
		map.put("ricerca.button.scorriAvanti", "scorriAvanti");
		map.put("ricerca.button.scorriIndietro", "scorriIndietro");
		map.put("ricerca.button.ripristina", "ripristina");
		map.put("ricerca.button.salva", "salva");
		map.put("ricerca.button.cancella", "cancella");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		map.put("button.ok", "noScript");
		map.put("ordine.label.fornitore", "fornitoreCerca");
		map.put("ordine.label.bilancio", "bilancioCerca");
		map.put("ricerca.label.sezione", "sezioneCerca");
		map.put("ordine.bottone.searchTit", "sifbid");
		map.put("crea.button.associaInventari", "associaInv");
		map.put("crea.button.visualizzInventari", "visualInv");
		map.put("ricerca.label.converti", "converti");
		map.put("ricerca.button.fornitoriProfili", "fornPreferiti");

		//almaviva5_20121120 evolutive google
		map.put("ricerca.button.spedisciOrdine", "spedisci");
		return map;
	}


	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm) form;
		Navigation navi = Navigation.getInstance(request);
		HttpSession session = request.getSession();
		if( navi.isFromBar()
				//almaviva5_20110923 #4605
				&& !ValidazioneDati.equals(request.getAttribute("ordineCompletato"), "ordineCompletato")) {
			return mapping.getInputForward();
		}
		if (request.getParameter("prov") != null && request.getParameter("prov").equals("SiDiEsaminaOrdine")){
			currentForm.setProv(request.getParameter("prov"));
		}
		if (navi.isFromBar() ) {
			// gestione del caso in cui rientro nell'esamina da menu bar da associa inventari per ordine di rilegatura
			if ( currentForm.getDatiOrdine()!=null
					&& currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R")){
				if (session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE)!=null)	{
					session.removeAttribute("chiamante");
					// carica
					List listaInv = (List) session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);

					currentForm.getDatiOrdine().getBilancio().setCodice3("4");
					currentForm.getDatiOrdine().setRigheInventariRilegatura(listaInv);

					currentForm.setElencaInventari(listaInv);
					currentForm.setNumRigheInv(listaInv.size());

					session.removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);

					// rilettura

					this.loadDatiOrdinePassato(currentForm,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
					ActionForward forward = super.controllaPrezzoSuOrdine(mapping, request, currentForm);
					if (forward != null)
						return forward;


				}else if (currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null
						&& currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  ){
					currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
					currentForm.setNumRigheInv(currentForm.getElencaInventari().size());
				}


			}

			if (ValidazioneDati.equals(navi.getBackAction(), "/documentofisico/datiInventari/modificaInvColl.do"))	{
				if (ValidazioneDati.equals(request.getAttribute("ordineCompletato"), "ordineCompletato")) {
					currentForm.setOrdineCompletato(true);

					// a seconda della configurazione adeguamento se automatico, su richiesta, mai
					ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
					configurazioneCriteri.setCodBibl(currentForm.getDatiOrdine().getCodBibl());
					configurazioneCriteri.setCodPolo(currentForm.getDatiOrdine().getCodPolo());
					ConfigurazioneORDVO conf = this.loadConfigurazioneORD(configurazioneCriteri);
					//almaviva5_20130513 #4762 adegua prezzo ordine, default a NO
					String adeguaPrezzo = (conf != null) ? ValidazioneDati.coalesce(conf.getAllineamento(), "N") : "N";
					// escludere gli ordini di tipo C, D, L dalla richiesta e dall'adeguemanto al prezzo reale
					if (currentForm.getDatiOrdine() != null && ValidazioneDati.in(currentForm.getDatiOrdine().getTipoOrdine(), "C", "D", "L"))
						adeguaPrezzo = "N";

					if (adeguaPrezzo.equals("A")) {
						// deve essere chiuso e adeguato
						// deve essere aggiornato l'ordine mettendo lo stato a chiuso solo se sono stati inseriti gli inventari
						// ed aggiornando il campo prezzo con il prezzo reale
						// introdotto con l'inventariazione
						// controllo inventariazione
						// controllo esistenza inventari

						this.ordineCompletamenteRicevuto(currentForm, request, true, true);
						return ripristina(mapping, form, request, response);
					}
					else if (adeguaPrezzo.equals("R")) {
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
						this.ordineCompletamenteRicevuto( currentForm,  request, false, true);
						return ripristina( mapping,  form,  request,  response);
					}

					}
				else
				{
					// proviene da inventari ma l'ordine NON E' STATO DICHIARATO COMPLETAMENTE CHIUSO
					// SE ORDINE CONTINUATIVO(M o C CON FLAG CONTINUATIVO)
					if (currentForm.getDatiOrdine()!=null  && currentForm.getDatiOrdine().getStatoOrdine()!=null
							&& currentForm.getDatiOrdine().getStatoOrdine().equals("A")
							&& currentForm.getDatiOrdine().isContinuativo()
							&& (currentForm.getDatiOrdine().getNaturaOrdine().equals("M")
									|| currentForm.getDatiOrdine().getNaturaOrdine().equals("C")) )
					{
						this.ordineCompletamenteRicevuto( currentForm,  request, true, false);
						return ripristina( mapping,  form,  request,  response);
					}

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));
				}

			}
			// da controllare perchè non disabilita i campi
			if (!currentForm.getDatiOrdine().getStatoOrdine().equals("A")) {
				currentForm.setOrdineApertoAbilitaInput(true);
				if (currentForm.getDatiOrdine().getStatoOrdine().equals("C"))
				{
					currentForm.setDisabilitazioneBottoneInventari(true);
				}
			}
			else
			{
				currentForm.setOrdineApertoAbilitaInput(false);
				currentForm.setDisabilitazioneBottoneInventari(false);
			}

			return mapping.getInputForward();
		}

		if(!currentForm.isSessione())
		{
			currentForm.setSessione(true);
		}

		try {
			OrdiniVO ordine = (OrdiniVO) request.getAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE);
			if (ordine != null) {
				currentForm.setDatiOrdine(ordine);
				currentForm.setElencaInventari(ordine.getRigheInventariRilegatura());
				return mapping.getInputForward();
			}
/*			if (request.getAttribute("provieneDaOperazioneOrdini")!=null &&  request.getAttribute("provieneDaOperazioneOrdini").equals("si"))
			{
				// rilettura nel rientro da operazione su ordini altrimenti crea problemi di concorrenza
				this.loadDatiOrdinePassato(esaordini,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
			}
*/
			ListaSuppOrdiniVO ricArr=(ListaSuppOrdiniVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
//			controllo che non riceva l'attributo di sessione di una lista supporto
// 			DISABILITAZIONE TOTALE DI ESAMINA SE LISTA SUPPORTO
//			escludendo il caso del VAI A
			if (ricArr!=null  && ricArr.getChiamante()!=null && !ricArr.getChiamante().equals("/gestionebibliografica/titolo/analiticaTitolo") )
			{
				currentForm.setEsaminaInibito(true);
				currentForm.setDisabilitaTutto(true);
			}


			currentForm.setListaDaScorrere((List<ListaSuppOrdiniVO>) session.getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE));
			if(ValidazioneDati.isFilled(currentForm.getListaDaScorrere()) )
			{
				if(currentForm.getListaDaScorrere().size()>1 )
				{
					currentForm.setEnableScorrimento(true);
					//esaCambio.setPosizioneScorrimento(0);
				}
				else
				{
					//almaviva5_20110208 sif da lista ordini periodico
					if (isSifEsameOrdinePeriodico(request, form)) {
						currentForm.setDisabilitaTutto(true);
						currentForm.setEsaminaOrdine(true);
						currentForm.setPulsanti(BOTTONIERA_ESAMINA);
					}
					currentForm.setEnableScorrimento(false);
				}


			}

			if (String.valueOf(currentForm.getPosizioneScorrimento()).length()==0 )
			{
				currentForm.setPosizioneScorrimento(0);
			}
			// richiamo ricerca su db con elemento 1 di ricerca
			if (!currentForm.isSubmitDinamico())
			{
				//tck 3950
				if (currentForm.getListaDaScorrere()!=null)
				{
					this.loadDatiOrdinePassato(currentForm,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
					ActionForward forward = super.controllaPrezzoSuOrdine(mapping, request, currentForm);
					if (forward != null){
						return forward;
					}
				}
			}else{
				if (currentForm.getDatiOrdine()!=null)		{
	    			OrdiniVO oggettoTemp=currentForm.getDatiOrdine();
					currentForm.setDatiOrdine(oggettoTemp);
					currentForm.setSubmitDinamico(false);
					// modifica del boolean del vo di ordini alla variazione del check del continuativo della pagina jsp (vedi anche loadDatiOrdinePassato che imposta il check del boolean della pagina jsp)
					if (request.getParameter("contin") != null) 	{
						currentForm.getDatiOrdine().setContinuativo(true);
						currentForm.setContin("on");
					}		else		{
						currentForm.getDatiOrdine().setContinuativo(false);
						currentForm.setContin(null);
					}

				}else{
	    			this.loadDatiOrdinePassato(currentForm,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
					ActionForward forward = super.controllaPrezzoSuOrdine(mapping, request, currentForm);
					if (forward != null){
						return forward;
					}
				}
			}
			//******** inizio gestione provenienza da inventari
			if (navi.getBackAction()!=null && navi.getBackAction().equals("/documentofisico/datiInventari/modificaInvColl.do"))
			{
				if (request.getAttribute("ordineCompletato")!=null &&  request.getAttribute("ordineCompletato").equals("ordineCompletato"))
				{
					currentForm.setOrdineCompletato(true);

					// a seconda della configurazione adeguamento se automatico, su richiesta, mai
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

						this.ordineCompletamenteRicevuto( currentForm,  request, true, true);
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
						this.ordineCompletamenteRicevuto( currentForm,  request, false, true);
						return ripristina( mapping,  form,  request,  response);

					}
		    		// tolto da qui
			    	// tolto a qui
				}
				else
				{
					// proviene da inventari ma l'ordine NON E' STATO DICHIARATO COMPLETAMENTE CHIUSO
					// SE ORDINE CONTINUATIVO(M o C CON FLAG CONTINUATIVO)
					if (currentForm.getDatiOrdine()!=null  && currentForm.getDatiOrdine().getStatoOrdine()!=null  && currentForm.getDatiOrdine().getStatoOrdine().equals("A") && currentForm.getDatiOrdine().isContinuativo() && (currentForm.getDatiOrdine().getNaturaOrdine().equals("M") || currentForm.getDatiOrdine().getNaturaOrdine().equals("C")) )
					{
						this.ordineCompletamenteRicevuto( currentForm,  request, true, false);
						return ripristina( mapping,  form,  request,  response);
					}

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.modificaOK"));


				}

			}

			//******** fine gestione provenienza da inventari



			//this.loadDatiOrdine();
			//caricameto delle combo
			this.loadBiblAffil( currentForm);
			try {
				this.loadValuta(currentForm, request );
			} catch (ValidationException e) {
				//almaviva5_20140613 #5078
				if (e.getError() == ValidationExceptionCodici.assenzaRisultati)
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.cambierroreAssenzaValutaRiferimento"));
				return mapping.getInputForward();
			}
			this.loadCombo( currentForm);
			this.loadPeriodo( currentForm);
			// impostazione della provenienza se esiste uno fra codRicOffertaOrdine, codDocOrdine, idOffertaFornOrdine, codSuggBiblOrdine
			currentForm.setProvenienza("");
			// impostazione delle combo ai valori dell'oggetto preso in esame
			currentForm.setNatura(currentForm.getDatiOrdine().getNaturaOrdine());
			currentForm.setStato(currentForm.getDatiOrdine().getStatoOrdine());
			//rosa
			if (currentForm.getValuta() != null && currentForm.getDatiOrdine().getValutaOrdine() != null){
				if (!currentForm.getValuta().equals(currentForm.getDatiOrdine().getValutaOrdine())){
//					currentForm.setPrezzoEurStr("");
//					currentForm.setPrezzoEuroOrdine(0);
					super.converti(mapping, currentForm, request, response);

					//currentForm.setPrezzoEurStr("0,00");

				}else{
					currentForm.setValuta(currentForm.getDatiOrdine().getValutaOrdine());
					currentForm.setPrezzoEurStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoEuroOrdine()));//rrrrr
				}
			}
			//
			if (currentForm.getDatiOrdine()!=null  && currentForm.getDatiOrdine().getBilancio()!=null  && currentForm.getDatiOrdine().getBilancio().getCodice3()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3().trim().length()>0)
			{
				currentForm.setTipoImpegno(currentForm.getDatiOrdine().getBilancio().getCodice3());
			}
			currentForm.setTipoInvio(currentForm.getDatiOrdine().getTipoInvioOrdine());
			currentForm.setUrg(currentForm.getDatiOrdine().getCodUrgenzaOrdine());
			currentForm.setPeriodo(currentForm.getDatiOrdine().getPeriodoValAbbOrdine());
			// selezione del tab di visualizzazione dei dati per tipo ordine
			if (currentForm.getDatiOrdine().getTipoOrdine() != null) {
				currentForm.setScegliTAB(currentForm.getDatiOrdine().getTipoOrdine() );
			}
			//	 disabilitazione campi di input se l'ordine non è di STATO aperto
			if (!currentForm.getDatiOrdine().getStatoOrdine().equals("A")) {
				currentForm.setOrdineApertoAbilitaInput(true);
			}
			else
			{
				currentForm.setOrdineApertoAbilitaInput(false);
			}
			// abilitazione/disabilitazione sezione abbonamenti
/*			if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A") && currentForm.getDatiOrdine().getBilancio().getCodice3().equals("2")) {
				currentForm.setDisabilitazioneSezioneAbbonamento(false);
			}*/
			if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S")  && currentForm.getDatiOrdine().getStatoOrdine().equals("A") )
			{
				//VEDI TCK 2557
				currentForm.setDisabilitazioneSezioneAbbonamento(false);

				if (!currentForm.getDatiOrdine().isGestBil()  && (currentForm.getDatiOrdine().getTipoOrdine().equals("A") || currentForm.getDatiOrdine().getTipoOrdine().equals("V")))
				{
					currentForm.setDisabilitazioneSezioneAbbonamento(false);
				}

				if (currentForm.getDatiOrdine().isGestBil() && currentForm.getDatiOrdine().getBilancio()!=null && currentForm.getDatiOrdine().getBilancio().getCodice3()!=null  &&  currentForm.getDatiOrdine().getBilancio().getCodice3().equals("2") && (currentForm.getDatiOrdine().getTipoOrdine().equals("A") || currentForm.getDatiOrdine().getTipoOrdine().equals("V")))
					{
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}
				if (currentForm.getDatiOrdine().getTipoOrdine().equals("L") || currentForm.getDatiOrdine().getTipoOrdine().equals("D")  || currentForm.getDatiOrdine().getTipoOrdine().equals("C"))
				{
					currentForm.setDisabilitazioneSezioneAbbonamento(false);
				}
			}



			// abilitazione/disabilitazione bottone periodici
			if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") ) {
				currentForm.setDisabilitazioneBottonePeriodici(false);
			}

			currentForm.setDisabilitazioneBottoneInventari(false);
			// abilitazione/disabilitazione bottone biblioaffi

			// è stato scelto un fornitore preferito dalla lista e si devono caricare tutti i dati
			String fornScelto= (String)request.getAttribute("fornScelto");
			if (fornScelto!=null &&  fornScelto.length()!=0)
			{
				currentForm.getDatiOrdine().getFornitore().setCodice(fornScelto);
				currentForm.getDatiOrdine().getFornitore().setDescrizione("");
				//request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				String fornSceltoDeno= (String)request.getAttribute("fornSceltoDeno");
				if (fornSceltoDeno!=null &&  fornSceltoDeno.length()!=0)
				{
					currentForm.getDatiOrdine().getFornitore().setDescrizione(fornSceltoDeno);
				}
				request.removeAttribute("fornScelto");
				request.removeAttribute("fornSceltoDeno");
			}

			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)session.getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						currentForm.getDatiOrdine().getFornitore().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						currentForm.getDatiOrdine().getFornitore().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
					}
				}
				else
				{
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
			if (ricBil!=null && ricBil.getChiamante()!=null && ricBil.getChiamante().equals(mapping.getPath()))
 			{
				if (ricBil!=null && ricBil.getSelezioniChiamato()!=null && ricBil.getSelezioniChiamato().size()!=0 )
				{
					if (ricBil.getSelezioniChiamato().get(0).getChiave()!=null && ricBil.getSelezioniChiamato().get(0).getChiave().length()!=0 )
					{
						currentForm.getDatiOrdine().getBilancio().setCodice1(ricBil.getSelezioniChiamato().get(0).getEsercizio());
						currentForm.getDatiOrdine().getBilancio().setCodice2(ricBil.getSelezioniChiamato().get(0).getCapitolo());
						currentForm.getDatiOrdine().getBilancio().setCodice3(ricBil.getSelezioniChiamato().get(0).getSelezioneImp());
					}
				}
				else
				{
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
			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
 			{
				if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
				{
					if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
					{
						currentForm.getDatiOrdine().setSezioneAcqOrdine(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					currentForm.getDatiOrdine().setSezioneAcqOrdine("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				session.removeAttribute("sezioniSelected");
				session.removeAttribute("criteriRicercaSezione");

 			}
			//controllo se ho un risultato di una lista di supporto BUONI ORDINE richiamata da questa pagina (risultato della simulazione)
			//ListaSuppBuoniOrdineVO lsbo = (ListaSuppBuoniOrdineVO)session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			BuoniOrdineVO buono = (BuoniOrdineVO) request.getAttribute(NavigazioneAcquisizioni.BUONO_ORDINE_SELEZIONATO);
			//if (lsbo != null && ValidazioneDati.equals(lsbo.getChiamante(), mapping.getPath())) {
			if (buono != null) {
				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
				session.removeAttribute("buoniSelected");
				session.removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE);

				//almaviva5_20140624 #4631
				List<OrdiniVO> listaOrdiniBuono = buono.getListaOrdiniBuono();
				if (listaOrdiniBuono == null)
					listaOrdiniBuono = new ArrayList<OrdiniVO>();
				OrdiniVO ord = currentForm.getDatiOrdine();
				listaOrdiniBuono.add(ord);

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordine.lega.buono.conferma",
						ord.getChiaveOrdine(), buono.getNumBuonoOrdine()));
				currentForm.setConferma(true);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				currentForm.setDisabilitaTutto(true);
				currentForm.setPressioneBottone("buono");
				session.setAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE, buono);
				return mapping.getInputForward();
			}

			// controllo se ho un risultato da interrogazione ricerca
			String bid=(String) request.getAttribute("bid");
			if (bid!=null && bid.length()!=0 )
			{
				String titolo=(String) request.getAttribute("titolo");
				// controllo se ho un risultato da interrogazione ricerca
				//String acq = request.getParameter("ACQUISIZIONI");
				//if ( acq != null) {
				currentForm.getDatiOrdine().getTitolo().setCodice(bid);
				if ( titolo != null) {
					currentForm.getDatiOrdine().getTitolo().setDescrizione(titolo);
				}
			}
/*			if (currentForm.getScegliTAB().equals("R") )
			{
				request.getSession().setAttribute("formDaPassareMod",(EsaminaOrdineModForm) esaordini );
				return mapping.findForward("pagRilegatura");
			}
			*/
			// ritorno da bottone specifico: da gestire il caricamento
			// currentForm.getScegliTAB().equals("R")
			if ( currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R"))
			{
				if (session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE)!=null)
				{
					session.removeAttribute("chiamante");
					// carica
					List listaInv = (List) session.getAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);

					currentForm.getDatiOrdine().getBilancio().setCodice3("4");
					currentForm.getDatiOrdine().setRigheInventariRilegatura(listaInv);

					currentForm.setElencaInventari(listaInv);
					currentForm.setNumRigheInv(listaInv.size());

					session.removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);

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



			if (currentForm.getScegliTAB().equals("R") ){
				currentForm.getDatiOrdine().getBilancio().setCodice3("4");
			}
			if (request.getParameter("VAIACREAINVENTARIO")!=null){
//				return mapping.findForward("sifInventario");
		           request.setAttribute("prov", "ordineIns");
				return this.inventari(mapping, currentForm, request, response);
			}else{
			//currentForm.setBiblioNONCentroSistema(false); //solo per test
			return mapping.getInputForward();
			}

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

	public ActionForward noScript(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward visualInv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try
		{
			if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R") &&  currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )
			{
				//currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
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
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try
		{
			if (currentForm.getDatiOrdine()!=null && (!currentForm.getDatiOrdine().getStatoOrdine().trim().equals("C") && !currentForm.getDatiOrdine().getStatoOrdine().trim().equals("N")))
				{
					currentForm.setPressioneBottone("salva");
					currentForm.setBottoneAssociaInvPremuto(true);
					// GESTIONE DEL BOTTONE ASSOCIAINV DEMANDATA AL BOTTONE SI
					//return Si( mapping,  form,  request,  response);
					return salva( mapping,  form,  request,  response);

			}
			else
			{
				return mapping.getInputForward();
			}

		}
		catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.erroreModifica"));

			return mapping.getInputForward();
		}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
			//almaviva5_20110208 sif da lista ordini periodico
			if (isSifEsameOrdinePeriodico(request, form))
				return Navigation.getInstance(request).goBack();
			if (currentForm.getProv()!= null ||
					Navigation.getInstance(request).bookmarkExists(TitoliCollegatiInvoke.ANALITICA_PROGRESS)){
				Navigation navi = Navigation.getInstance(request);
				navi.purgeThis();
				request.setAttribute("prov", "IndietroDiEsaminaOrdine");
//				return Navigation.getInstance(request).goBack(true);
				return Navigation.getInstance(request).goBack();
			}

			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward inventari(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	OrdineBaseForm currentForm = (OrdineBaseForm ) form;
	Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	UserVO utente = Navigation.getInstance(request).getUtente();
	try {
		utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_ACCESSIONAMENTO_ORDINI, utente.getCodPolo(), utente.getCodBib(), null);

	}   catch (UtenteNotAuthorizedException e) {

		LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

		return mapping.getInputForward();
	}

	try {

			if (Navigation.getInstance(request).isFromBar()){
                return mapping.getInputForward();

          }

		if ( !currentForm.getDatiOrdine().getStatoOrdine().equals("C"))
		{
			//	        request.setAttribute("codPoloO", currentForm.getDatiOrdine().getCodPolo());//potrebbe servire

			request.setAttribute("codBibF", "");

			request.setAttribute("codBibO", currentForm.getDatiOrdine().getCodBibl());

			request.setAttribute("bid", currentForm.getDatiOrdine().getTitolo().getCodice());

			request.setAttribute("codTipoOrd", currentForm.getDatiOrdine().getTipoOrdine());

			request.setAttribute("annoOrd", currentForm.getDatiOrdine().getAnnoOrdine());

			request.setAttribute("codOrd", currentForm.getDatiOrdine().getCodOrdine());

			request.setAttribute("codFornitore", currentForm.getDatiOrdine().getFornitore());

			//almaviva2 23/09/2011
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("ordineIns")){
				request.setAttribute("prov", "ordineIns");
			}else{
				request.setAttribute("prov", "ordine");
			}

			return mapping.findForward("sifInventario");

		}
		else
		{

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordineChiuso"));

			return mapping.getInputForward();
		}


		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
			// ripristino del valore iniziale
			this.loadDatiOrdinePassato(currentForm,currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
			ActionForward forward = super.controllaPrezzoSuOrdine(mapping, request, currentForm);
			if (forward != null){
				return forward;
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return eseguiSalva(mapping, form, request, response);
	}

	private ActionForward eseguiSalva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		Navigation navi = Navigation.getInstance(request);
		UserVO utente = navi.getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {

			LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

			return mapping.getInputForward();
		}

		try {
			request.setAttribute("salva", "salva");
			ActionForward forward = super.controllaPrezzi(mapping, form, request, response);
			if (forward != null) {
				//errore
				if (currentForm.isConferma()){
					currentForm.setConferma(true);
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					currentForm.setDisabilitaTutto(true);
					currentForm.setPressioneBottone("salva");
				}
				return mapping.getInputForward();
			}
			if (request.getAttribute("errore") != null){
				this.ripristina(mapping, form, request, response);
				return mapping.getInputForward();
			}


			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			OrdiniVO ordine = currentForm.getDatiOrdine();
			ordine.setUtente(navi.getUtente().getFirmaUtente());
			factory.getGestioneAcquisizioni().ValidaOrdiniVO(ordine);
			currentForm.setPrezzoEurStr(Pulisci.VisualizzaImporto( ordine.getPrezzoEuroOrdine()));
			currentForm.setPrezzoStr(Pulisci.VisualizzaImporto( ordine.getPrezzoOrdine()));
			ordine.setValutaOrdine(currentForm.getValuta());
			// fine validazione
			//almaviva2 - controllo che se la valuta è EUR prezzoOrdine e prezzoEuroOrdine devono essere uguali
			//			if (currentForm.getDatiOrdine().getValutaOrdine() != null && currentForm.getDatiOrdine().getValutaOrdine().trim().equals("EUR")){
			//				if (!currentForm.getDatiOrdine().getPrezzoEuroOrdineStr().equals(currentForm.getDatiOrdine().getPrezzoOrdineStr())){
			//					//diagnostico di errore
			//
			//					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.controllarePrezzoOrdineEPrezzoEuroOrdine"));
			//
			//					currentForm.setConferma(false);
			//					return mapping.getInputForward();
			//				}
			//			}


			// GESTIONE BOTTONE ASSOCIAINV degli ordini di rilegatura
			if (currentForm.isBottoneAssociaInvPremuto())
			{
				currentForm.setBottoneAssociaInvPremuto(false);
				if (ValidazioneDati.isFilled(ordine.getRigheInventariRilegatura()) )
				{
					List<StrutturaInventariOrdVO> inventari = new ArrayList<StrutturaInventariOrdVO>(ordine.getRigheInventariRilegatura());
					request.getSession().setAttribute("elencoINVANDATA", ClonePool.deepCopy(inventari) );
				}

				request.getSession().setAttribute("chiamante",  mapping.getPath());
				request.getSession().setAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE_R, ordine.clone());
				return mapping.findForward("assInv");
			}
			// FINE GESTIONE BOTTONE ASSCOCIA INV degli ordini di rilegatura



			// gestione del tipo rilegatura senza inventari associati tck 3026 collaudo
			if (ordine!=null && ordine.getTipoOrdine()!=null && ordine.getStatoOrdine()!=null && ordine.getTipoOrdine().equals("R") && ordine.getStatoOrdine().equals("A") ) {
				if ( ordine.getRigheInventariRilegatura()==null || (ordine.getRigheInventariRilegatura()!=null && ordine.getRigheInventariRilegatura().size()==0)){
					throw new ValidationException("rilegaturaInventariAssenti",
							ValidationExceptionCodici.rilegaturaInventariAssenti);
				}
			}


			currentForm.setConferma(true);
			currentForm.setDisabilitaTutto(true);
			currentForm.setPressioneBottone("salva");
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
			currentForm.setPressioneBottone("");
			return mapping.getInputForward();

		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			currentForm.setPressioneBottone("");

			return mapping.getInputForward();
		}
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
	Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	UserVO utente = Navigation.getInstance(request).getUtente();
	//26.07.10 cambio idControllo di cancella (GA_CANCELLAZIONE_ORDINE) con quello di salva (GA_GESTIONE_ORDINI)
	try {
		utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI, utente.getCodPolo(), utente.getCodBib(), null);

	}   catch (UtenteNotAuthorizedException e) {

		LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

		return mapping.getInputForward();

	}


	try {

    		currentForm.setConferma(true);
    		currentForm.setDisabilitaTutto(true);
			currentForm.setPressioneBottone("cancella");
    		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

    		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
    		return mapping.getInputForward();

		} catch (Exception e) {
    		currentForm.setConferma(false);
    		currentForm.setDisabilitaTutto(false);
			currentForm.setPressioneBottone("");

			return mapping.getInputForward();
		}
	}


	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
			//Questa funzione è disponibile solo a livello di Biblioteca e non di Polo
			//e può essere effettuata solo da biblioteca Centro Sistema (non affiliate).
			//	Permette di modificare un ordine in uno stato “aperto”, ma non ancora inviato al fornitore.
			//Non è consentito variare la tipologia e il cod_ordine.
			// In questa fase se è stato modificato il prezzo allora viene aggiornato nel bilancio relativo all’ordine
			// e nel budget della sezione di acquisizione, il valore ordinato.

			currentForm.setConferma(false);
    		currentForm.setDisabilitaTutto(false);

    		if (currentForm.isAdeguamentoPrezzo()) {
    			currentForm.setAdeguamentoPrezzo(false); // reset
    			// su conferma di adeguamento prezzo previsto al reale
				this.ordineCompletamenteRicevuto(currentForm, request, true, true);
				return ripristina(mapping, form, request, response);
    		}

    		// non siamo passati per ordine completamente ricevuto
    		Navigation navi = Navigation.getInstance(request);
			currentForm.getDatiOrdine().setUtente(navi.getUtente().getFirmaUtente());
			HttpSession session = request.getSession();

			String tipoOperazione = currentForm.getPressioneBottone();
			if (ValidazioneDati.in(tipoOperazione, "salva", "spedisci")) {
				currentForm.setPressioneBottone("");
				//String tipoVar="M";
				OrdiniVO ordine=currentForm.getDatiOrdine();
				String ticket=navi.getUserTicket();
				ordine.setTicket(ticket);
				ListaSuppOrdiniVO attrLS=(ListaSuppOrdiniVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				if (currentForm.getFornitoreVO() != null){
					ordine.setAnagFornitore(currentForm.getFornitoreVO());
				}
				ListaSuppOrdiniVO attrLSagg=this.AggiornaTipoVarRisultatiListaSupporto(ordine, attrLS);

				session.setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE,attrLSagg );

				if (!this.modificaOrdine(ordine)) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreModifica"));

				} else {
					if (ValidazioneDati.equals(ordine.getTipoOrdine(), "R")) {
						//almaviva5_20121116 evolutive google
						boolean risultato = true;
						List<StrutturaInventariOrdVO> inventari = currentForm.getElencaInventari();
						if (ValidazioneDati.isFilled(inventari))
							risultato = OrdiniWebUtil.aggiornaDisponibilitaInventari(request, ordine, inventari);
						if (!risultato)
							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.legameInventariAggDispKO"));
					}

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));

					if (ValidazioneDati.equals(tipoOperazione, "spedisci"))
						return continuaSpedizione(mapping, currentForm, request, response);

					return ripristina(mapping, form, request, response);
				}
			}
			if (tipoOperazione.equals("cancella")) {
				currentForm.setPressioneBottone("");
				OrdiniVO eleOrdine=currentForm.getDatiOrdine();

				if (!this.cancellaOrdine(eleOrdine)) {

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreCancella2"));

					//return mapping.getInputForward();
				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.cancellaOK"));

					//currentForm.setOrdineApertoAbilitaInput(true);
					currentForm.getDatiOrdine().setFlag_canc(true);
					currentForm.setDisabilitaTutto(true);
				}
			}

			//almaviva5_20140624 #4631
			if (ValidazioneDati.in(tipoOperazione, "buono")) {
				BuoniOrdineVO buono = (BuoniOrdineVO) session.getAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE);
				session.removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE);
				AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
				delegate.modificaBuonoOrdine(buono);
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.modificaOK"));
			}

			return mapping.getInputForward();

		}	catch (ValidationException ve) {

			SbnErrorTypes error = ve.getErrorCode();
    		if (error != SbnErrorTypes.ERROR_GENERIC)
    			LinkableTagUtils.addError(request, ve);
    		else
    			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage(), ve.getLabels()));

			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			currentForm.setPressioneBottone("");
			return mapping.getInputForward();

		}	catch (ApplicationException e) {

			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");


			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	private ActionForward continuaSpedizione(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm) form;
		try {
			// ripristino del valore iniziale
			this.loadDatiOrdinePassato(
					currentForm,
					currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()));
			ActionForward forward = super.controllaPrezzoSuOrdine(mapping, request, currentForm);
			if (forward != null)
				return forward;

			OrdiniVO ordine = currentForm.getDatiOrdine();
			request.setAttribute(NavigazioneAcquisizioni.DETTAGLIO_ORDINE, ordine.copy() );

			return Navigation.getInstance(request).goForward(mapping.findForwardConfig("spedizione"));

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm) form;
		this.saveToken(request);

		try {
			if (currentForm.isAdeguamentoPrezzo()) { // in caso di no deve essere	solo chiuso senza adeguamento prezzo.
				currentForm.setAdeguamentoPrezzo(false); // reset
				this.ordineCompletamenteRicevuto(currentForm, request, false, true);
				return ripristina(mapping, form, request, response);
			}

			HttpSession session = request.getSession();
			String tipoOperazione = currentForm.getPressioneBottone();
			if (ValidazioneDati.in(tipoOperazione, "buono")) {
				session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
				session.removeAttribute("buoniSelected");
				session.removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE);
			}

		} catch (ValidationException ve) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			return mapping.getInputForward();

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			return mapping.getInputForward();

		} finally {
			currentForm.setConferma(false);
			currentForm.setDisabilitaTutto(false);
			currentForm.setPressioneBottone("");
		}

		return mapping.getInputForward();
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));


				}else{
					// rimozione var di sessione per impedire la visualizzazione degli inventari ddell'ordine precedente o succ
					request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);
					//currentForm.setElencaInventari(listaInv);
					currentForm.setNumRigheInv(0);


					//this.loadDatiOrdinePassato(esaordini,currentForm.getListaDaScorrere().get(attualePosizione));

					try {
						this.loadDatiOrdinePassato(currentForm,currentForm.getListaDaScorrere().get(attualePosizione));
						ActionForward forward = super.controllaPrezzoSuOrdine(mapping, request, currentForm);
						if (forward != null){
							return forward;
						}
					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001){
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
					if (currentForm.getDatiOrdine().getTipoOrdine() != null) {
						currentForm.setScegliTAB(currentForm.getDatiOrdine().getTipoOrdine() );
					}
					//	 disabilitazione campi di input se l'ordine non è di tipo aperto
					currentForm.setDisabilitaTutto(false);
					if (!currentForm.getDatiOrdine().getStatoOrdine().equals("A")) {
						currentForm.setOrdineApertoAbilitaInput(true);
					} else 	{
						currentForm.setOrdineApertoAbilitaInput(false);
						//currentForm.setDisabilitaTutto(false);
					}

					// abilitazione/disabilitazione sezione abbonamenti
					//VEDI TCK 2557
					if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A"))
					{
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}

					if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A") && !currentForm.getDatiOrdine().isGestBil() )
					{
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}
					if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A") && (currentForm.getDatiOrdine().isGestBil() && currentForm.getDatiOrdine().getBilancio().getCodice3().equals("2")) )
					{
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}

						// rilegatura
/*					if (currentForm.getScegliTAB().equals("R"))
					{
						request.getSession().setAttribute("formDaPassareMod",(EsaminaOrdineModForm) esaordini );
						return mapping.findForward("pagRilegatura");
					}*/
					if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R") &&  currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )
					{
						currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
						currentForm.setNumRigheInv(currentForm.getElencaInventari().size());
					}

					//currentForm.setNumRigheInv(0); // per imporre il refresh della lista inventari

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
	EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			int dimensione=currentForm.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{


				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));


				}
			else
				{
					// rimozione var di sessione per impedire la visualizzazione degli inventari ddell'ordine precedente o succ
					request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);
					currentForm.setNumRigheInv(0);

					//this.loadDatiOrdinePassato(esaordini,currentForm.getListaDaScorrere().get(attualePosizione));

					try {
						this.loadDatiOrdinePassato(currentForm,currentForm.getListaDaScorrere().get(attualePosizione));
						ActionForward forward = super.controllaPrezzoSuOrdine(mapping, request, currentForm);
						if (forward != null){
							return forward;
						}
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

					// aggiornamento del tab di visualizzazione dei dati per tipo ordine
					if (currentForm.getDatiOrdine().getTipoOrdine() != null) {
						currentForm.setScegliTAB(currentForm.getDatiOrdine().getTipoOrdine() );
					}

					//	 disabilitazione campi di input se l'ordine non è di tipo aperto
					currentForm.setDisabilitaTutto(false);
					if (!currentForm.getDatiOrdine().getStatoOrdine().equals("A")) {
						currentForm.setOrdineApertoAbilitaInput(true);
					} else 	{
						//currentForm.setDisabilitaTutto(false);
						currentForm.setOrdineApertoAbilitaInput(false);
					}
					// abilitazione/disabilitazione sezione abbonamenti
					//VEDI TCK 2557
					if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A"))
					{
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}

					if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A") && !currentForm.getDatiOrdine().isGestBil() )
					{
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}
					if (currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && currentForm.getDatiOrdine().getStatoOrdine().equals("A") && (currentForm.getDatiOrdine().isGestBil() && currentForm.getDatiOrdine().getBilancio().getCodice3().equals("2")) )
					{
						currentForm.setDisabilitazioneSezioneAbbonamento(false);
					}


					// rilegatura (vedere problema )
/*					if (currentForm.getScegliTAB().equals("R"))
					{
						request.getSession().setAttribute("formDaPassareMod",(EsaminaOrdineModForm) esaordini );
						return mapping.findForward("pagRilegatura");
					}*/
					if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R") &&  currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )
					{
						currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
						currentForm.setNumRigheInv(currentForm.getElencaInventari().size());
					}

					//currentForm.setNumRigheInv(0); // per imporre il refresh della lista inventari
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

	public ActionForward listaInventariOrdine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	OrdineBaseForm currentForm = (OrdineBaseForm ) form;
		try {
            if (Navigation.getInstance(request).isFromBar()){

				//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.inventariAssenti"));


            	return mapping.getInputForward();
            }

          request.setAttribute("codBibF", "");

          request.setAttribute("codBibO", currentForm.getDatiOrdine().getCodBibl());

          request.setAttribute("codTipoOrd", currentForm.getDatiOrdine().getTipoOrdine());

          request.setAttribute("annoOrd", currentForm.getDatiOrdine().getAnnoOrdine());

          request.setAttribute("codOrd", currentForm.getDatiOrdine().getCodOrdine());

          request.setAttribute("prov", "ordine");

          request.setAttribute("titOrd", currentForm.getDatiOrdine().getTitolo());


        return mapping.findForward("sifListeInventari");


		}
		 catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward kardexPeriodici(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
			OrdiniVO datiOrdine = currentForm.getDatiOrdine();
			if (datiOrdine.isContinuativo() && datiOrdine.getNaturaOrdine().equals("S"))
				{
					Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
					UserVO utente = Navigation.getInstance(request).getUtente();
					try {
						utenteEJB.checkAttivita(CodiciAttivita.getIstance().PERIODICI, utente.getCodPolo(), utente.getCodBib(), null);

					}   catch (UtenteNotAuthorizedException e) {

						LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

						return mapping.getInputForward();
					}
					BibliotecaVO bib = new BibliotecaVO();
			        bib.setCod_polo(datiOrdine.getCodPolo());
			        bib.setCod_bib(datiOrdine.getCodBibl());
			        SerieOrdineVO ordine = new SerieOrdineVO();
			        ordine.setId_ordine(datiOrdine.getIDOrd());
			        ordine.setBid(datiOrdine.getTitolo().getCodice());
			        ordine.setCod_bib_ord(datiOrdine.getCodBibl());
			        ordine.setAnno_ord(Integer.parseInt(datiOrdine.getAnnoOrdine()));
			        ordine.setCod_tip_ord(datiOrdine.getTipoOrdine().charAt(0));
			        ordine.setCod_ord(Integer.parseInt(datiOrdine.getCodOrdine()));
			        ordine.setId_fornitore(Integer.parseInt(datiOrdine.getFornitore().getCodice()));
			        ordine.setFornitore(datiOrdine.getFornitore().getDescrizione());
				    return PeriodiciDelegate.getInstance(request).sifKardexPeriodico(bib, ordine);
					//return mapping.getInputForward();
				}
				else
				{
					currentForm.setDisabilitazioneBottonePeriodici(false);

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.ordinierroreBottonePeriodici"));

					return mapping.getInputForward();
				}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);

				}   catch (UtenteNotAuthorizedException e) {

					LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

					return mapping.getInputForward();

				}

				OrdiniVO ordine = currentForm.getDatiOrdine();
				if (ordine.getTipoOrdine().equals("R") ) {
					if (currentForm.getNumRigheInv() < 1 ) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.noStampaOrdiniRilNoInv"));
						return mapping.getInputForward();
					}

					//almaviva5_20121123 evolutive google
					StampaBuonoOrdineVO richiesta = new StampaBuonoOrdineVO();
					richiesta.setCodBibl(ordine.getCodBibl());
					richiesta.setCodPolo(ordine.getCodPolo());
					richiesta.setListaOrdiniDaStampare(ValidazioneDati.asSingletonList(ordine));
					richiesta.setListaBuoniOrdineDaStampare(null);
					richiesta.setTipoStampa(StampaType.STAMPA_ORDINE_RILEGATURA);
					request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_ORDINE_RILEGATURA); // aggiungere stampa_lista_ordini
					request.setAttribute("DATI_STAMPE_ON_LINE", richiesta);
					return mapping.findForward("stampaOL");
				}

				List<OrdiniVO> ordini = new ArrayList<OrdiniVO>();
				ordini.add(ordine);

				// aggiunta per la lettura dinamica integrale della configurazione senza impostazioni statiche
				ConfigurazioneBOVO conf = new ConfigurazioneBOVO(ordine);
				ConfigurazioneBOVO confLettonew = this.loadConfigurazione(conf);
				if (confLettonew != null) {
					conf = confLettonew;
				}
				conf.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
                // fine aggiunta

				// le due istruzioni seguenti servono al sw di stampa per non andare in errore per l'assenza di voci per tipo ordine/lingua
				//confBOnew.setTestoOggetto(strTestoOggetto);
                //confBOnew.setTestoIntroduttivo(strTestoIntroduttivo);


				StampaBuonoOrdineVO stampaOL = new StampaBuonoOrdineVO();
				stampaOL.setCodBibl(ordine.getCodBibl());
				stampaOL.setCodPolo(ordine.getCodPolo());
				stampaOL.setConfigurazione(conf);
				// stampaOL.setConfigurazione(confBO);
				// stampaOL.setListaOrdiniDaStampare(listaOrdiniSel);
				stampaOL.setListaOrdiniDaStampare(ordini);
				stampaOL.setListaBuoniOrdineDaStampare(null);


				//gestione del cambio di stato del flag stampato


					List listaFiltrata=this.Stampato(ordini,"ORD","");
 					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
 					List<StampaBuoniVO> risultatoPerStampa=factory.getGestioneAcquisizioni().impostaBuoniOrdineDaStampare( conf, listaFiltrata, "ORD", false,  Navigation.getInstance(request).getUserTicket(), Navigation.getInstance(request).getUtente().getFirmaUtente(),  conf.getDenoBibl());

 					listaFiltrata=risultatoPerStampa;

					if (listaFiltrata!=null && listaFiltrata.size()>0)
					{
						stampaOL.setListaOrdiniDaStampare(listaFiltrata);
	 					// test su memorizzazione flag stampato
						// test su memorizzazione flag stampato	SPOSTATO IN STAMPAONLINEACTION
						// boolean risultato=this.StampaOrdini(provaElencoOrdinato,null,"ORD",confBOnew.getUtente(),"");
						boolean risultato=true;
						if (!risultato)
						{

							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonStampati"));

							return mapping.getInputForward();
						}
						else
						{

							request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_BUONI_ORDINE); // aggiungere stampa_lista_ordini
							request.setAttribute("DATI_STAMPE_ON_LINE", listaFiltrata);
							return mapping.findForward("stampaOL");
						}
					}
					else
					{

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonStampati"));

						return mapping.getInputForward();
					}


	} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			return mapping.getInputForward();
		}

	}



	public ActionForward bibloaffil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
			try {
				if (!currentForm.isBiblioNONCentroSistema() )
				{
					request.getSession().setAttribute("chiamante", mapping.getPath());
					request.setAttribute("biblInOgg", currentForm.getDatiOrdine().getCodBibl());
					return mapping.findForward("bibloaffil");
				}
				else
				{
					currentForm.setBiblioNONCentroSistema(false);

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.ordinierroreBottoneBiblioAffil"));

					return mapping.getInputForward();
				}

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}



	public ActionForward operazioneSuOrdine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			Navigation navi = Navigation.getInstance(request);
			UserVO utente = navi.getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_OPERAZIONI_SU_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);

			}   catch (UtenteNotAuthorizedException e) {

				LinkableTagUtils.addError(request, new ActionMessage("messaggio.info.noautOP"));

				return mapping.getInputForward();
			}
			request.getSession().setAttribute("chiamante",  mapping.getPath());
			//request.setAttribute("chiamante", mapping.getName());
			String ticket = navi.getUserTicket();
			currentForm.getDatiOrdine().setTicket(ticket);
// aggiunti
			currentForm.setConferma(false);
    		currentForm.setDisabilitaTutto(false);
    		currentForm.getDatiOrdine().setUtente(navi.getUtente().getFirmaUtente());


// fine aggiunta

			// validazione
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaOrdiniVO(currentForm.getDatiOrdine());
			// fine validazione

		    OrdiniVO eleOrdineAttivo= ((OrdineBaseForm) currentForm).getDatiOrdine();
		    //request.getSession().setAttribute("ordineAttivoRox",(OrdiniVO) currentForm.getDatiOrdine().clone());
			request.getSession().setAttribute(NavigazioneAcquisizioni.ORDINE_ATTIVO,eleOrdineAttivo.clone() );
			return mapping.findForward("operazionesuordine");
		}	catch (ValidationException ve) {
				SbnErrorTypes error = ve.getErrorCode();
	    		if (error != SbnErrorTypes.ERROR_GENERIC)
	    			LinkableTagUtils.addError(request, ve);
	    		else
	    			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward buonoOrdine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineBaseForm currentForm = (OrdineBaseForm ) form;

		try {
			HttpSession session = request.getSession();
			session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			session.removeAttribute("buoniSelected");
			session.removeAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE);

			// stato buono da cambiare
			// impongo il fornitore e il bilancio prescelti
			OrdiniVO o = currentForm.getDatiOrdine();
			Navigation navi = Navigation.getInstance(request);
			String ticket = navi.getUserTicket();
			// carica i criteri di ricerca da passare alla esamina
			UserVO utente = navi.getUtente();
			StrutturaTerna ord = new StrutturaTerna(o.getTipoOrdine(), o.getAnnoOrdine(), o.getCodOrdine());
			StrutturaCombo forn = new StrutturaCombo(o.getFornitore().getCodice(), o.getFornitore().getDescrizione());
			StrutturaTerna bil = new StrutturaTerna(o.getBilancio().getCodice1(), o.getBilancio().getCodice2(), "");

			ListaSuppBuoniOrdineVO ricerca = new ListaSuppBuoniOrdineVO(
					utente.getCodPolo(), o.getCodBibl(), "", "", "", "", "",
					ord, forn, bil, mapping.getPath(), "");
			ricerca.setTicket(ticket);
			ricerca.setModalitaSif(false);
			ricerca.setTipoOperazione(TipoOperazione.CERCA_DA_ORDINE);
			ricerca.setDatiOrdine(o);

			//almaviva5_20140624 #4631
			navi.addBookmark(NavigazioneAcquisizioni.ORDINE_ATTIVO);
			AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
			List<BuoniOrdineVO> buoniOrdine = delegate.ricercaBuoniOrdine(ricerca);
			if (ValidazioneDati.isFilled(buoniOrdine)) {
				ricerca = preparaRicercaBuonoSingle(buoniOrdine, request, 0);
				ricerca.setTipoOperazione(TipoOperazione.ESAMINA_DA_ORDINE);
				return mapping.findForward("esaminaBuono");
			} else
				//eliminazione filtro puntuale su ordine
				ricerca.setOrdine(new StrutturaTerna());

			session.setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE, ricerca);
			return mapping.findForward("buoniord");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineBaseForm esaordini = (OrdineBaseForm ) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE)==null
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			request.getSession().removeAttribute("sezioniSelected");
			request.getSession().removeAttribute("criteriRicercaSezione");

			this.impostaSezioneCerca(esaordini,request,mapping);
			//return mapping.findForward("sezioneCerca");
			return mapping.findForward("sezioneLista");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaSezioneCerca( OrdineBaseForm currentForm, HttpServletRequest request, ActionMapping mapping)
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



	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineBaseForm currentForm = (OrdineBaseForm ) form;
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

			//tck 3952
			// fornitore
			if (currentForm.getDatiOrdine().getFornitore()!=null && currentForm.getDatiOrdine().getFornitore().getCodice()!=null &&  currentForm.getDatiOrdine().getFornitore().getCodice().trim().length()>0 && currentForm.getDatiOrdine().getIDOrd()>0)
			{
				ListaSuppFatturaVO eleRicerca=new ListaSuppFatturaVO();
				// carica i criteri di ricerca
				eleRicerca.setFornitore(new StrutturaCombo("",""));
				eleRicerca.getFornitore().setCodice(currentForm.getDatiOrdine().getFornitore().getCodice());
				eleRicerca.getFornitore().setDescrizione("");
				eleRicerca.setTipoFattura("F");
				eleRicerca.setOrdine(new StrutturaTerna("","",""));
				eleRicerca.getOrdine().setCodice1(currentForm.getDatiOrdine().getTipoOrdine());
				eleRicerca.getOrdine().setCodice2(currentForm.getDatiOrdine().getAnnoOrdine());
				eleRicerca.getOrdine().setCodice3(currentForm.getDatiOrdine().getCodOrdine());
				eleRicerca.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				eleRicerca.setCodBibl(currentForm.getDatiOrdine().getCodBibl());
				List<FatturaVO> listaFatture=null;
				try {
					listaFatture=this.getListaFatturaVO(eleRicerca);
				} catch (Exception e) {
				}
				if (listaFatture!=null && listaFatture.size()>0)
				{

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.fattureEmesse" ));

					return mapping.getInputForward();
				}
			}
			// fine


			this.impostaFornitoreCerca(currentForm,request,mapping, true);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private List<FatturaVO> getListaFatturaVO(ListaSuppFatturaVO criRicerca) throws Exception
	{
	List<FatturaVO> listaFatture;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaFatture = factory.getGestioneAcquisizioni().getRicercaListaFatture(criRicerca);
	return listaFatture;
	}


	public ActionForward fornPreferiti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineBaseForm currentForm = (OrdineBaseForm ) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar()) {
			return mapping.getInputForward();
		}
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			if (currentForm.getDatiOrdine().getTitolo().getCodice()!=null && currentForm.getDatiOrdine().getTitolo().getCodice().trim().length()>0
				&& 	currentForm.getDatiOrdine().getSezioneAcqOrdine()!=null && currentForm.getDatiOrdine().getSezioneAcqOrdine().trim().length()>0
				) 			{
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

				this.impostaFornitoreCerca( currentForm , request,mapping, false);

				ListaSuppFornitoreVO eleRicerca=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");

				if (currentForm.getDatiOrdine().getTitolo().getCodice()!=null && currentForm.getDatiOrdine().getTitolo().getCodice().trim().length()>0) // caso di fornitori di profilo
				{
					boolean valRitorno = false;
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					String ticket = navi.getUserTicket();
					List lista = null;
					//if (esaord.getDatiOrdine().getTitolo().getCodice()!=null && esaord.getDatiOrdine().getTitolo().getCodice().trim().length()>0)
//					 momentaneo
/*					lista=(List) factory.getGestioneAcquisizioni().getTitolo(currentForm.getDatiOrdine().getTitolo().getCodice(), ticket);
*/
					TitoloACQVO recTit=null;
					recTit=factory.getGestioneAcquisizioni().getTitoloRox(currentForm.getDatiOrdine().getTitolo().getCodice());
					if (recTit!=null)
					{
						//recTit = (TitoloACQVO) lista.get(0);
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
							request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
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
			}			else			{
				//throw new Exception("non esistono fornitori per profilo di acquisto in base ai dati presenti");

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.profAcqInesistenti" ));

				return mapping.getInputForward();
			}
			//return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			log.error("", e);
			return mapping.getInputForward();
		}
	}


	private void impostaFornitoreCerca( OrdineBaseForm currentForm, HttpServletRequest request, ActionMapping mapping, boolean cercaF)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=currentForm.getDatiOrdine().getCodBibl();
		String codForn="";
		String nomeForn="";
		// si esclude l'impostazione del fornitore per la ricerca di fornitori di profilo
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
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama,loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}

	public ActionForward bilancioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		OrdineBaseForm esaordini = (OrdineBaseForm ) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppBilancioVO")==null
			request.getSession().removeAttribute("attributeListaSuppBilancioVO");
			request.getSession().removeAttribute("bilanciSelected");
			request.getSession().removeAttribute("criteriRicercaBilancio");

			this.impostaBilancioCerca( esaordini, request,mapping);
			return mapping.findForward("bilancioCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaBilancioCerca( OrdineBaseForm currentForm, HttpServletRequest request, ActionMapping mapping)
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
		OrdineBaseForm currentForm = (OrdineBaseForm ) form;
		try {
			if (currentForm.getDatiOrdine().getTitolo()!=null && currentForm.getDatiOrdine().getTitolo().getCodice()!=null)
			{
				request.setAttribute("bidFromRicOrd", currentForm.getDatiOrdine().getTitolo().getCodice());
			}
			//return mapping.findForward("sifbid");
			return Navigation.getInstance(request).goForward(mapping.findForward("sifbid"));

		}
		catch (Exception e)
		{
			return mapping.getInputForward();
		}
	}


	private void loadAppo(EsaminaOrdineModForm currentForm, String[] prova)  {
		//currentForm.setCheckSelezionati(prova);
		currentForm.setAppo(prova[0]);
	}


	private void loadDatiOrdinePassato(EsaminaOrdineModForm currentForm, ListaSuppOrdiniVO criteriRicercaOrdine) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<OrdiniVO> ordiniTrovato = factory.getGestioneAcquisizioni().getRicercaListaOrdini(criteriRicercaOrdine);
		//gestire l'esistenza del risultato e che sia univoco
		currentForm.setDatiOrdine(ordiniTrovato.get(0));


		try {
			//rosa
			currentForm.setPrezzoEurStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoEuroOrdine()));
			currentForm.setPrezzoStr(Pulisci.VisualizzaImporto( currentForm.getDatiOrdine().getPrezzoOrdine()));
			currentForm.setValuta(currentForm.getDatiOrdine().getValutaOrdine());
		} catch (Exception e) {
			//rosa
			currentForm.setPrezzoStr("0,00");
			currentForm.setPrezzoEurStr("0,00");
		}
		if (currentForm.getDatiOrdine()!=null  && currentForm.getDatiOrdine().isContinuativo())	{
			currentForm.setContin("on");
		}
		else{
			currentForm.setContin(null);
		}
		// solo per ordini di rilegatura
		if (currentForm.getDatiOrdine()!=null && currentForm.getDatiOrdine().getTipoOrdine()!=null &&  currentForm.getDatiOrdine().getTipoOrdine().equals("R")  )
		{
			if (currentForm.getDatiOrdine().getRigheInventariRilegatura()!=null && currentForm.getDatiOrdine().getRigheInventariRilegatura().size()>0  )
			{
				for (int i=0;  i < currentForm.getDatiOrdine().getRigheInventariRilegatura().size(); i++)
				{
					StrutturaInventariOrdVO ele=currentForm.getDatiOrdine().getRigheInventariRilegatura().get(i);
					if (ele.getTitolo()==null)
					{
						ele.setTitolo("");
					}
					String	titoInv="";
					titoInv=this.controllaTitolo(ele.getBid());

					if (titoInv!=null)
					{
						ele.setTitolo(titoInv);
					}
					else
					{
						ele.setTitolo("");
					}

				}
				currentForm.setElencaInventari(currentForm.getDatiOrdine().getRigheInventariRilegatura());
				currentForm.setNumRigheInv(currentForm.getDatiOrdine().getRigheInventariRilegatura().size());
			}

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
				tito=tit.getIsbd();
			}
		} catch (Exception e) {
		    	log.error("", e);
		}
		return tito;
	}



	private boolean modificaOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaOrdine(ordine);
		return valRitorno;
	}

	private boolean cancellaOrdine(OrdiniVO ordine) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaOrdine(ordine);
		return valRitorno;
	}



	private ListaSuppOrdiniVO AggiornaTipoVarRisultatiListaSupporto (OrdiniVO eleOrdine, ListaSuppOrdiniVO attributo)
	{
		try {
			if (eleOrdine !=null)
			{
			List<OrdiniVO> risultati=new ArrayList();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getChiave();
					if (eleRis.equals(eleOrdine.getChiave()))
					{
						//risultati.get(i).setTipoVariazione(eleOrdine.getTipoVariazione());
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



	private void loadBiblAffil(EsaminaOrdineModForm currentForm) throws Exception {
		List<StrutturaCombo> listaBiblAff = new ArrayList<StrutturaCombo>();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		// decommentare se si vuole la gestione del bottone bilio affil.
		//listaBiblAff = (List<StrutturaCombo>) factory.getGestioneAcquisizioni().getRicercaBiblAffiliate(currentForm.getDatiOrdine().getCodBibl(), CodiciAttivita.getIstance().GA_GESTIONE_ORDINI);
		if (listaBiblAff!=null && listaBiblAff.size()!=0)
		{
			currentForm.setBiblioNONCentroSistema(false);
		}
	}


    private void loadPeriodo(EsaminaOrdineModForm currentForm) throws Exception {
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

    private void loadCombo(EsaminaOrdineModForm currentForm) throws Exception {
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	currentForm.setListaStato(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_STATO_ORDINE)));
	   	currentForm.setListaTipoImpegno(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_IMPEGNO_ACQUISTO_MATERIALE)));
	   	currentForm.setListaTipoInvio(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_INVIO)));
	   	currentForm.setListaUrg(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_URGENZA)));
	   	currentForm.setListaNatura(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_NATURA_ORDINE)));
	}


	private ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneBOVO configurazioneTrovata = new ConfigurazioneBOVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazione(configurazione);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		return configurazioneTrovata;
	}

	private List  Stampato(List listaOggetti, String tipoOggetti,  String bo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List ris = factory.getGestioneAcquisizioni().gestioneStampato( listaOggetti,  tipoOggetti, bo);
		return ris;
	}

	private Boolean  StampaOrdini(List listaOggetti, List idList, String tipoOggetti, String utente, String bo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		Boolean ris = factory.getGestioneAcquisizioni().gestioneStampaOrdini( listaOggetti,  idList,  tipoOggetti,  utente,  bo);
		return ris;
	}

	private List<StrutturaQuinquies>  costruisci(StrutturaTerna ordRinn, String codBibl) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<StrutturaQuinquies> ris = factory.getGestioneAcquisizioni().costruisciCatenaRinnovati(ordRinn, codBibl);
		return ris;
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

	public ActionForward schedaRinnovi(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm ) form;
		try {
            if (Navigation.getInstance(request).isFromBar()){

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.rinnoviAssenti"));


            	return mapping.getInputForward();
            }
			List<StrutturaQuinquies> catenaRinnovi=null; // tipo, anno, cod, anno_abb, id_ordine
			if (!currentForm.isDisabilitazioneSezioneAbbonamento() || (currentForm.isDisabilitazioneSezioneAbbonamento() && currentForm.getDatiOrdine().isContinuativo() && currentForm.getDatiOrdine().getNaturaOrdine().equals("S") && (currentForm.getDatiOrdine().getStatoOrdine().equals("C") || currentForm.getDatiOrdine().getStatoOrdine().equals("N"))))
			{
				// ricostruzione della catena dei rinnovi
				StrutturaTerna parCatena=new StrutturaTerna("","",""); // tipo, anno, cod
				parCatena.setCodice1(currentForm.getDatiOrdine().getTipoOrdine());
				parCatena.setCodice2(currentForm.getDatiOrdine().getAnnoOrdine());
				parCatena.setCodice3(currentForm.getDatiOrdine().getCodOrdine());
				//parCatena.setCodice4("");
				try {
					catenaRinnovi=this.costruisci(parCatena, currentForm.getDatiOrdine().getCodBibl());
				} catch (Exception e) {
			 		log.error("", e);
			 	}
				List<StrutturaQuinquies> listaOrdiniCatena=new ArrayList<StrutturaQuinquies>() ;

				if (ValidazioneDati.isFilled(catenaRinnovi) ) {
					for (StrutturaQuinquies rinnovo : catenaRinnovi)
					{
						StrutturaQuinquies eleLista=new StrutturaQuinquies("", "", "", "", "");
						eleLista.setCodice1(rinnovo.getCodice1());
						eleLista.setCodice2(rinnovo.getCodice2());
						eleLista.setCodice3(rinnovo.getCodice3());
						eleLista.setCodice4(currentForm.getDatiOrdine().getCodPolo());
						eleLista.setCodice5(currentForm.getDatiOrdine().getCodBibl());
						listaOrdiniCatena.add(eleLista);
					}

				}
				if (listaOrdiniCatena!=null && listaOrdiniCatena.size()>0)
				{
					// agganciare metodo di documento fisico per l'emissione della schemata di lista inventari
			         // request.setAttribute("codBibO", currentForm.getDatiOrdine().getCodBibl());
			          request.setAttribute("listaOrdini", listaOrdiniCatena);
			          request.setAttribute("prov", "listaOrdini");
			          request.setAttribute("locale", this.getLocale(request, Constants.SBN_LOCALE));

				}
    	        return mapping.findForward("sifListeInventari");

			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void ordineCompletamenteRicevuto(EsaminaOrdineModForm currentForm,
			HttpServletRequest request, boolean adegua, Boolean chiuso)
			throws Exception {

		try {
			OrdiniVO ordine = currentForm.getDatiOrdine();
			double totImportInv = 0.00;
			try {
				AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
				totImportInv = delegate.importoInventariOrdine(ordine).doubleValue();

			} catch (Exception e) {
				log.error("", e);
			}

			ordine.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
			String oldStatoOrdine = ordine.getStatoOrdine();
			if (chiuso)
				ordine.setStatoOrdine("C");
			else {
				if (ordine != null
						&& ordine.getStatoOrdine() != null
						&& ordine.getStatoOrdine().equals("A")
						&& ordine.isContinuativo()
						&& (ordine.getNaturaOrdine().equals("M") || ordine.getNaturaOrdine().equals("C"))) {
					// se il valore del prezzo reale supera il previsto deve
					// essere adeguato altrimenti NO
					if (totImportInv > 0
							&& totImportInv <= ordine.getPrezzoEuroOrdine()) {
						adegua = false;
					}
				}
			}

			if (adegua) {
				if (totImportInv > 0) {
					if (ordine.getTipoOrdine().equals("A")
							|| ordine.getTipoOrdine().equals("V")) {
						ordine.setPrezzoEuroOrdine(totImportInv);
						ordine.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto(ordine.getPrezzoEuroOrdine()));
					}
				}
			}

			currentForm.setDatiOrdine(ordine);
			if (chiuso)
				currentForm.setDisabilitaTutto(true);

			if (this.modificaOrdine(ordine)) {
				if (chiuso)
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordineChiusoCompletato"));
				 else
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.adeguamentoPrezzoRealeOrdiniContinuativi"));

			} else {
				ordine.setStatoOrdine(oldStatoOrdine);
				// messaggio di incompletezza sell'inserimento per l'assenza
				// inventari
			}

		}	catch (ValidationException ve) {
			throw ve;
		}	catch (Exception e) {
			throw e;
		}


	}



	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		OrdineBaseForm currentForm = (OrdineBaseForm) form;

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		OrdiniVO ordine = currentForm.getDatiOrdine();

		if (idCheck.equals(NavigazioneAcquisizioni.GESTIONE) )
			try {
				String codPolo = utente.getCodPolo();
				String codBib = utente.getCodBib();
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI, codPolo, codBib, null);
				if ( ordine!=null && ordine.getTipoOrdine()!=null &&  ordine.getTipoOrdine().equals("A"))
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA, codPolo, codBib, null);
				else if ( ordine!=null && ordine.getTipoOrdine()!=null &&  ordine.getTipoOrdine().equals("L"))
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO, codPolo, codBib, null);
				else if ( ordine!=null && ordine.getTipoOrdine()!=null &&  ordine.getTipoOrdine().equals("D"))
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO, codPolo, codBib, null);
				else if ( ordine!=null && ordine.getTipoOrdine()!=null &&  ordine.getTipoOrdine().equals("V"))
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA, codPolo, codBib, null);
				else if ( ordine!=null && ordine.getTipoOrdine()!=null &&  ordine.getTipoOrdine().equals("C"))
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_SCAMBIO, codPolo, codBib, null);
				else if ( ordine!=null && ordine.getTipoOrdine()!=null &&  ordine.getTipoOrdine().equals("R"))
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI_RILEGATURA, codPolo, codBib, null);

				if (ValidazioneDati.equals(idCheck, NavigazioneAcquisizioni.PERIODICI))
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().PERIODICI);

				return true;
			}
			catch (Exception e) {
				log.error("", e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}


		//almaviva5_20110214 #4213
		if (currentForm.isEsaminaOrdine() && ValidazioneDati.in(idCheck, BOTTONIERA_ESAMINA))
			return true;

		if (ValidazioneDati.equals(idCheck, NavigazioneAcquisizioni.SPEDISCI)) {
			return //ordine.isGoogle() &&
				!ordine.isSpedito() && !ordine.isStampato() &&
				ValidazioneDati.isFilled(ordine.getRigheInventariRilegatura());
		}

		if (ValidazioneDati.equals(idCheck, NavigazioneAcquisizioni.SPEDITO)) {
			if (!ValidazioneDati.equals(ordine.getTipoOrdine(), "R"))
				return false;

			return ordine.isSpedito() && ordine.isGoogle();
		}

		if (ValidazioneDati.equals(idCheck, NavigazioneAcquisizioni.STAMPATO))
			return ordine.isStampato();

		return super.checkAttivita(request, form, idCheck);
	}

	public void enter(HttpServletRequest request, HttpServletResponse response,
			ActionForm form) {
		return;

	}

	public void leave(HttpServletRequest request, HttpServletResponse response,
			ActionForm form) {
		return;

	}

	public void destroy(HttpServletRequest request,	HttpServletResponse response, ActionForm form) {

		if (isSifEsameOrdinePeriodico(request, form))
			Pulisci.PulisciVar(request);

		//almaviva5_20130617
		request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_INVENTARI_ORDINE);
	}

	public ActionForward spedisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaOrdineModForm currentForm = (EsaminaOrdineModForm) form;
		request.setAttribute("salva", "spedisci");
		ActionForward forward = super.controllaPrezzi(mapping, form, request, response);
		if (forward != null) {
			//errore
			if (currentForm.isConferma()){
				currentForm.setConferma(true);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				currentForm.setDisabilitaTutto(true);
				currentForm.setPressioneBottone("spedisci");
			}
			return mapping.getInputForward();
		}

		currentForm.setConferma(true);
		LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));
		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
		currentForm.setDisabilitaTutto(true);
		currentForm.setPressioneBottone("spedisci");
		return mapping.getInputForward();
	}
}
