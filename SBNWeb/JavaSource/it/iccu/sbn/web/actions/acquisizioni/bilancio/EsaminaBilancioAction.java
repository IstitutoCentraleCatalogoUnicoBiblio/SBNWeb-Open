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
package it.iccu.sbn.web.actions.acquisizioni.bilancio;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.bilancio.EsaminaBilancioForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.acquisizioni.AcquisizioniDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class EsaminaBilancioAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva", "salva");
		map.put("ricerca.button.ripristina", "ripristina");
		map.put("ricerca.button.cancella", "cancella");
		map.put("ricerca.button.indietro", "indietro");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		map.put("ricerca.button.scorriAvanti", "scorriAvanti");
		map.put("ricerca.button.scorriIndietro", "scorriIndietro");
		map.put("ricerca.button.insRiga", "inserisciRiga");
		map.put("ricerca.button.cancRiga", "cancellaRiga");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
/*			if(!esaBilanci.isSessione())
			{
				esaBilanci.setSessione(true);
			}
*/

			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				currentForm.setEsaminaInibito(true);
				currentForm.setDisabilitaTutto(true);
			}

			currentForm.setListaDaScorrere((List<ListaSuppBilancioVO>) request.getSession().getAttribute("criteriRicercaBilancio"));
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

				//this.loadAppo(resultckeck);
			}
/*			if (!this.esaBilanci.isSubmitDinamico())
			{
				this.esaBilanci.setPosizioneScorrimento(0);
				// richiamo ricerca su db con elemento 1 di ricerca
				this.loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(0));
			}
*/
			//this.loadDatiBilancioPassato(this.esaBilanci.getListaDaScorrere().get(0));
			if (String.valueOf(currentForm.getPosizioneScorrimento()).length()==0 )
			{
				currentForm.setPosizioneScorrimento(0);
			}
			// richiamo ricerca su db con elemento 1 di ricerca
			//this.loadDatiOrdinePassato(esaordini.getListaDaScorrere().get(this.esaordini.getPosizioneScorrimento()));

			BilancioVO oggettoBil=this.loadDatiBilancioPassato(currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()),request);
			currentForm.set("bilancio",oggettoBil);
			int totImpegni=0;
			if (oggettoBil.getDettagliBilancio()!=null && oggettoBil.getDettagliBilancio().size()>0)
			{
				totImpegni=oggettoBil.getDettagliBilancio().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			currentForm.set("numImpegni",totImpegni);

			BilancioDettVO[] oggettoDettBil=new BilancioDettVO[totImpegni];
			for (int i=0; i<totImpegni; i++)
			{
				oggettoDettBil[i]=oggettoBil.getDettagliBilancio().get(i);
			}
			currentForm.set("elencaImpegni",oggettoDettBil);
			//this.esaBilanci.setNumImpegni(this.esaBilanci.getListaImpegni().size());

			//esaBilanci.set("elencaImpegni",oggettoBil.getDettagliBilancio());
			//this.loadImpegni();


			//this.loadTipoImpegno();
			List arrListaTipoImpegno=this.loadTipoImpegno();
			currentForm.set("listaTipoImpegno",arrListaTipoImpegno);
			currentForm.set("enableScorrimento",currentForm.isEnableScorrimento());

			//r this.esaBilanci.setTipoImpegno(this.esaBilanci.getBilancio().getImpegno());

/*			if (this.sbilancio())
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.bilancio.incongruenza"));
				this.saveErrors(request, errors);
			}

			if (this.duplicato())
			{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.bilancio.duplicazioneImpegno"));
				this.saveErrors(request, errors);
			}*/


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

		public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			try {
			return mapping.findForward("indietro");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}


		public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
			try {
				BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) currentForm.get("elencaImpegni");
				BilancioVO appobil=(BilancioVO)currentForm.get("bilancio");
				BilancioDettVO dettBil = new BilancioDettVO("",0.00,0.00,0.00,0.00 );
				if (elencaImpegni!=null &&  elencaImpegni.length!=0)
				{
					BilancioDettVO[] listaDettBil=new BilancioDettVO[elencaImpegni.length +1];
					for (int i=0; i<elencaImpegni.length; i++)
					{
						listaDettBil[i]=elencaImpegni[i];
					}
					listaDettBil[elencaImpegni.length]=dettBil;
					currentForm.set("elencaImpegni",listaDettBil);
					// gestione selezione della riga in inserimento
					Integer[]  radioImpegno=new Integer[1];
					if (appobil!=null && elencaImpegni.length>0)
					{
						radioImpegno[0]=elencaImpegni.length;
						currentForm.set("radioImpegno",radioImpegno);
					}

				}
				else
				{
					elencaImpegni=new BilancioDettVO[1];
					elencaImpegni[0]=dettBil;
					currentForm.set("elencaImpegni",elencaImpegni);
					// gestione selezione della riga in inserimento
					Integer[]  radioImpegno=new Integer[1];
					if (elencaImpegni!=null && elencaImpegni.length>0)
					{
						radioImpegno[0]=elencaImpegni.length -1;
						currentForm.set("radioImpegno",radioImpegno);
					}

				}
				currentForm.set("numImpegni",elencaImpegni.length);
				//esaBilanci.set("radioImpegno",0);

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
			try {
				BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) currentForm.get("elencaImpegni");
				//BilancioVO appobil=(BilancioVO)insBilanci.get("bilancio");
				Integer[] radioImpegno=(Integer[])currentForm.get("radioImpegno");
				if (radioImpegno.length!=0)	{

					if (elencaImpegni.length!=0 ) {
						//almaviva5_20130604 #4757
						BilancioVO bilancio = (BilancioVO) currentForm.get("bilancio");
						if (bilancio.getIDBil() > 0) {
							AcquisizioniDelegate delegate = AcquisizioniDelegate.getInstance(request);
							boolean exists = delegate.esisteLegameRigaBilancio(bilancio.getIDBil(), elencaImpegni[radioImpegno[0]]);
							if (exists) {
								LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreCancellaBilancio"));
								return mapping.getInputForward();
							}
						}

						List<BilancioDettVO> lista =new ArrayList<BilancioDettVO>();
						for (int t=0;  t < elencaImpegni.length; t++)
						{
							BilancioDettVO elem = elencaImpegni[t];
							lista.add(elem);
						}

						//String[] appo=  selectedDatiIntest;
						int i= (radioImpegno.length) -1;
						// ciclo dall'ultimo codice selezionato
						while (i>=0)
						{
							int elem = radioImpegno[i];
							// il valore del num riga è superiore di una unità rispetto all'indice dell'array
							lista.remove(elem);
							i=i-1;
						}

						//this.renumera(lista);
					BilancioDettVO[] lista_fin =new BilancioDettVO [lista.size()];

						for (int r=0;  r < lista.size(); r++)
						{
							lista_fin [r] = lista.get(r);
						}

					currentForm.set("elencaImpegni",lista_fin);
					currentForm.set("radioImpegno", null) ;
					currentForm.set("numImpegni",lista_fin.length);
					}
				}
				currentForm.set("radioImpegno",0);
				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}





		public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			//EsaminaBilancioForm esaBilanci = (EsaminaBilancioForm) form;
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;

			try {
				// validazione
				BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) currentForm.get("elencaImpegni");
				BilancioVO eleBilancio=(BilancioVO)currentForm.get("bilancio");

				String amountBil=eleBilancio.getBudgetDiCapitoloStr().trim();
			    Double risultBil=Pulisci.ControllaImporto(amountBil);
			    eleBilancio.setBudgetDiCapitolo(risultBil);
			    // refresh del campo (da spostare nella form????? validate)
			    // serve per troncare eventuali cifre decimali immesse oltre la seconda
			    eleBilancio.setBudgetDiCapitoloStr(Pulisci.VisualizzaImporto( eleBilancio.getBudgetDiCapitolo()));

				List<BilancioDettVO> lista =new ArrayList<BilancioDettVO>();
				for (int w=0;  w < elencaImpegni.length; w++)
				{
					BilancioDettVO elem = elencaImpegni[w];
					String amountDettBil=elem.getBudgetStr().trim();
				    Double risultDettBil=Pulisci.ControllaImporto(amountDettBil);
				    elem.setBudget(risultDettBil);
				    elem.setBudgetStr(Pulisci.VisualizzaImporto( elem.getBudget()));
				    lista.add(elem);
				}
				eleBilancio.setDettagliBilancio(lista);
				currentForm.set("bilancio",eleBilancio);

				boolean valRitorno = false;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				factory.getGestioneAcquisizioni().ValidaBilancioVO(eleBilancio);
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
				currentForm.setConferma(false);
				currentForm.setPressioneBottone("");
				currentForm.setDisabilitaTutto(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);

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
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
			try {
				BilancioVO oggettoBil=loadDatiBilancioPassato(currentForm.getListaDaScorrere().get(currentForm.getPosizioneScorrimento()), request);
				currentForm.set("bilancio",oggettoBil);
				currentForm.set("numImpegni",0);
				int totImpegni=0;
				if (oggettoBil.getDettagliBilancio()!=null && oggettoBil.getDettagliBilancio().size()>0)
				{
					totImpegni=oggettoBil.getDettagliBilancio().size();
				}
				//this.insBilanci.setNumImpegni(totImpegni);
				currentForm.set("numImpegni",totImpegni);

				BilancioDettVO[] oggettoDettBil=new BilancioDettVO[totImpegni];
				for (int i=0; i<totImpegni; i++)
				{
					oggettoDettBil[i]=oggettoBil.getDettagliBilancio().get(i);
				}
				currentForm.set("elencaImpegni",oggettoDettBil);

				return mapping.getInputForward();
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
			try {
				BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) currentForm.get("elencaImpegni");
				BilancioVO eleBilancio=(BilancioVO)currentForm.get("bilancio");
				eleBilancio.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

				String amountBil=eleBilancio.getBudgetDiCapitoloStr().trim();
			    Double risultBil=Pulisci.ControllaImporto(amountBil);
			    eleBilancio.setBudgetDiCapitolo(risultBil);
			    // refresh del campo (da spostare nella form????? validate)
			    // serve per troncare eventuali cifre decimali immesse oltre la seconda
			    eleBilancio.setBudgetDiCapitoloStr(Pulisci.VisualizzaImporto( eleBilancio.getBudgetDiCapitolo()));

				// trasformo in arraylist
				List lista =new ArrayList();
				for (int w=0;  w < elencaImpegni.length; w++)
				{
					BilancioDettVO elem = elencaImpegni[w];

					String amountDettBil=elem.getBudgetStr().trim();
				    Double risultDettBil=Pulisci.ControllaImporto(amountDettBil);
				    elem.setBudget(risultDettBil);
				    elem.setBudgetStr(Pulisci.VisualizzaImporto( elem.getBudget()));

				    lista.add(elem);
				}
				eleBilancio.setDettagliBilancio(lista);
				currentForm.set("bilancio",eleBilancio);

				currentForm.setConferma(false);
	    		currentForm.setDisabilitaTutto(false);

				if (currentForm.getPressioneBottone().equals("salva")) {
					currentForm.setPressioneBottone("");

					if (!this.modificaBilancio(eleBilancio)) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.acquisizioni.erroreModifica"));
						this.saveErrors(request, errors);
						//return mapping.getInputForward();
					}
					else
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.modificaOK"));
						this.saveErrors(request, errors);
						return ripristina( mapping,  form,  request,  response);

					}
				}
				if (currentForm.getPressioneBottone().equals("cancella")) {
					currentForm.setPressioneBottone("");
					//BilancioVO eleBilancio=esaBilanci.getBilancio();
					if (!this.cancellaBilancio(eleBilancio)) {
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

		public ActionForward no(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
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
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
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
						//this.loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(attualePosizione));
					try
					{
						BilancioVO oggettoBil=loadDatiBilancioPassato(currentForm.getListaDaScorrere().get(attualePosizione),request);
						currentForm.set("bilancio",oggettoBil);
						currentForm.set("numImpegni",0);
						int totImpegni=0;
						if (oggettoBil.getDettagliBilancio()!=null && oggettoBil.getDettagliBilancio().size()>0)
						{
							totImpegni=oggettoBil.getDettagliBilancio().size();
						}
						//this.insBilanci.setNumImpegni(totImpegni);
						currentForm.set("numImpegni",totImpegni);

						BilancioDettVO[] oggettoDettBil=new BilancioDettVO[totImpegni];
						for (int i=0; i<totImpegni; i++)
						{
							oggettoDettBil[i]=oggettoBil.getDettagliBilancio().get(i);
						}
						currentForm.set("elencaImpegni",oggettoDettBil);

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
						e.printStackTrace();
						throw e;
					}


						//BilancioVO oggettoBil=loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(attualePosizione),request);

						currentForm.setPosizioneScorrimento(attualePosizione);
						// aggiornamento del tab di visualizzazione dei dati per tipo ordine
			    		currentForm.setDisabilitaTutto(false);
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
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
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
						//this.loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(attualePosizione));
						try
						{
							BilancioVO oggettoBil=loadDatiBilancioPassato(currentForm.getListaDaScorrere().get(attualePosizione),request);
							currentForm.set("bilancio",oggettoBil);
							currentForm.set("numImpegni",0);
							int totImpegni=0;
							if (oggettoBil.getDettagliBilancio()!=null && oggettoBil.getDettagliBilancio().size()>0)
							{
								totImpegni=oggettoBil.getDettagliBilancio().size();
							}
							//this.insBilanci.setNumImpegni(totImpegni);
							currentForm.set("numImpegni",totImpegni);

							BilancioDettVO[] oggettoDettBil=new BilancioDettVO[totImpegni];
							for (int i=0; i<totImpegni; i++)
							{
								oggettoDettBil[i]=oggettoBil.getDettagliBilancio().get(i);
							}
							currentForm.set("elencaImpegni",oggettoDettBil);

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
							e.printStackTrace();
							throw e;
						}
						currentForm.setPosizioneScorrimento(attualePosizione);
			    		currentForm.setDisabilitaTutto(false);
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
			EsaminaBilancioForm currentForm = (EsaminaBilancioForm) form;
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

	private List loadTipoImpegno() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("1","1  - Monografie e periodici non in abbonamento");
		lista.add(elem);
		elem = new StrutturaCombo("2","2  - Periodici in abbonamento");
		lista.add(elem);
		elem = new StrutturaCombo("3","3  - Collane");
		lista.add(elem);
		elem = new StrutturaCombo("4","4  - Altre spese");
		lista.add(elem);

		return lista;
		//this.insBilanciNorm.setListaTipoImpegno(lista);
		//this.insBilanci.set("listaTipoImpegno",lista);
		//this.insBilanciNorm.setListaTipoImpegno(lista);
		//insBilanci.set("listaTipoImpegno",lista);

	}


	private BilancioVO loadDatiBilancioPassato(ListaSuppBilancioVO criteriRicercaBilancio, HttpServletRequest request) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<BilancioVO> bilanciTrovato = new ArrayList<BilancioVO>();
		// criteriRicercaBilancio.setLoc(request.getLocale()); // aggiunto per Documento Fisico 09/05/08
		criteriRicercaBilancio.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunto per Documento Fisico 27/05/08 insieme al SinteticaLookupDispatchAction invece di LookupDispatchAction
		criteriRicercaBilancio.setRicercaEsamina(true);
		bilanciTrovato = factory.getGestioneAcquisizioni().getRicercaListaBilanci(criteriRicercaBilancio);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaBilanci.setBilancio(bilanciTrovato.get(0));
		//for (int i=0; i<bilanciTrovato.size(); i++)
		//this.esaBilanci.setListaImpegni(bilanciTrovato.get(0).getDettagliBilancio());
		BilancioVO bil=bilanciTrovato.get(0);
		try {
			bil.setBudgetDiCapitoloStr(Pulisci.VisualizzaImporto( bil.getBudgetDiCapitolo()));
			for (int w=0;  w < bil.getDettagliBilancio().size(); w++)
			{
				//BilancioDettVO elem = bil.getDettagliBilancio().get(w);
				bil.getDettagliBilancio().get(w).setBudgetStr(Pulisci.VisualizzaImporto( bil.getDettagliBilancio().get(w).getBudget()));
				//lista.add(elem);
			}
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			bil.setBudgetDiCapitoloStr("0,00");

		}

		return bil;
	}
	private boolean modificaBilancio(BilancioVO bilancio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaBilancio(bilancio);
		return valRitorno;
	}

	private boolean cancellaBilancio(BilancioVO bilancio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaBilancio(bilancio);
		return valRitorno;
	}
	private ListaSuppBilancioVO AggiornaTipoVarRisultatiListaSupporto (BilancioVO eleBilancio, ListaSuppBilancioVO attributo)
	{
		try {
			if (eleBilancio !=null)
			{
			List<BilancioVO> risultati=new ArrayList();
			// carica i criteri di ricerca da passare alla esamina
			risultati=attributo.getSelezioniChiamato();
			for (int i=0;  i < risultati.size(); i++)
			{
				String eleRis=risultati.get(i).getChiave();
					if (eleRis.equals(eleBilancio.getChiave()))
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

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("GESTIONE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BILANCIO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		return false;
	}

}
