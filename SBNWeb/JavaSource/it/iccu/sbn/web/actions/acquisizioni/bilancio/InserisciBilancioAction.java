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
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.bilancio.InserisciBilancioForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

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
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public class InserisciBilancioAction extends LookupDispatchAction  {
	//private InserisciBilancioForm insBilanci;
	//private InserisciBilancioForm insBilanciNorm;


	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
		map.put("ricerca.button.insRiga","inserisciRiga");
		map.put("ricerca.button.cancRiga","cancellaRiga");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBilancioForm insBilanciNorm = (InserisciBilancioForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_BILANCIO, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		try {
			InserisciBilancioForm insBilanciNorm = (InserisciBilancioForm) form;
    		if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!insBilanciNorm.isSessione())
			{
				insBilanciNorm.setSessione(true);
			}
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			DynaValidatorForm insBilanci = (DynaValidatorForm) form;

			BilancioVO oggettoBil=loadBilancio();
			if (biblio!=null && oggettoBil!=null && (oggettoBil.getCodBibl()==null || (oggettoBil.getCodBibl()!=null && oggettoBil.getCodBibl().trim().length()==0)))
			{
				oggettoBil.setCodBibl(biblio);
			}

			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			oggettoBil.setCodPolo(polo);

			// preimpostazione della schermata di inserimento con i valori ricercati

			if (request.getSession().getAttribute("ATTRIBUTEListaSuppBilancioVO")!=null)
			{
				ListaSuppBilancioVO ele=(ListaSuppBilancioVO) request.getSession().getAttribute("ATTRIBUTEListaSuppBilancioVO");
				request.getSession().removeAttribute("ATTRIBUTEListaSuppBilancioVO");
				if (ele.getCapitolo()!=null && ele.getCapitolo().trim().length()>0 )
				{
					oggettoBil.setCapitolo(ele.getCapitolo());
				}
				if (ele.getEsercizio()!=null && ele.getEsercizio().trim().length()>0 )
				{
					oggettoBil.setEsercizio(ele.getEsercizio());;
				}
//				 MANCA GESTIONE DI RIGA ORDINE
			}


			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				oggettoBil.setCodBibl(bibScelta.getCod_bib());
			}

			insBilanci.set("bilancio",oggettoBil);

			int totImpegni=0;
			if (oggettoBil.getDettagliBilancio()!=null && oggettoBil.getDettagliBilancio().size()>0)
			{
				totImpegni=oggettoBil.getDettagliBilancio().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			insBilanci.set("numImpegni",totImpegni);

			BilancioDettVO[] oggettoDettBil=new BilancioDettVO[totImpegni];
			for (int i=0; i<totImpegni; i++)
			{
				oggettoDettBil[i]=oggettoBil.getDettagliBilancio().get(i);
			}
			insBilanci.set("elencaImpegni",oggettoDettBil);

			//insBilanciNorm.setNumImpegni(prova.size());
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				// perimpostazione di campi con elemento ricercato e non trovato
				BilancioVO oggBil= (BilancioVO) insBilanci.get("bilancio");
				oggBil.setEsercizio(ricArr.getEsercizio());
				oggBil.setCapitolo(ricArr.getCapitolo());
				insBilanci.set("bilancio", oggBil);
				insBilanci.set("visibilitaIndietroLS",true);

			}
			List arrListaTipoImpegno=this.loadTipoImpegno();
			insBilanci.set("listaTipoImpegno",arrListaTipoImpegno);

			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ve.printStackTrace();
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			e.printStackTrace();
			ActionMessages errors = new ActionMessages();
			//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBilancioForm insBilanci = (InserisciBilancioForm) form;
		try {
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				ActionForward action = new ActionForward();
				action.setName("RITORNA");
				action.setPath(ricArr.getChiamante()+".do");
				return action;
			}
			else
			{
				return mapping.findForward("indietro");
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
			//List<BilancioDettVO> lista_new= (List<BilancioDettVO>)insBilanci2.get("listaImpegni");
			//List lista_new = this.insBilanci.getListaImpegni();
			// acquisizione dati aggiornati
			DynaValidatorForm insBilanci = (DynaValidatorForm) form;
			BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) insBilanci.get("elencaImpegni");
			BilancioVO appobil=(BilancioVO)insBilanci.get("bilancio");
			BilancioDettVO dettBil = new BilancioDettVO("",0.00,0.00,0.00,0.00 );
			dettBil.setBudgetStr("0,00");

			if (elencaImpegni!=null &&  elencaImpegni.length!=0)
			{
				BilancioDettVO[] listaDettBil=new BilancioDettVO[elencaImpegni.length +1];
				for (int i=0; i<elencaImpegni.length; i++)
				{
					listaDettBil[i]=elencaImpegni[i];
				}
				listaDettBil[elencaImpegni.length]=dettBil;
				insBilanci.set("elencaImpegni",listaDettBil);

				// gestione selezione della riga in inserimento
				Integer[]  radioImpegno=new Integer[1];
				if (appobil!=null && elencaImpegni.length>0)
				{
					radioImpegno[0]=elencaImpegni.length;
					insBilanci.set("radioImpegno",radioImpegno);
				}
			}
			else
			{
				elencaImpegni=new BilancioDettVO[1];
				elencaImpegni[0]=dettBil;
				insBilanci.set("elencaImpegni",elencaImpegni);

				// gestione selezione della riga in inserimento
				Integer[]  radioImpegno=new Integer[1];
				if (elencaImpegni!=null && elencaImpegni.length>0)
				{
					radioImpegno[0]=elencaImpegni.length -1;
					insBilanci.set("radioImpegno",radioImpegno);
				}

			}
			insBilanci.set("numImpegni",elencaImpegni.length);


			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			DynaValidatorForm insBilanci = (DynaValidatorForm) form;
			BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) insBilanci.get("elencaImpegni");
			//BilancioVO appobil=(BilancioVO)insBilanci.get("bilancio");
			Integer[] radioImpegno=(Integer[])insBilanci.get("radioImpegno");
			if (radioImpegno.length!=0)
			{
				if (elencaImpegni.length!=0 )
				{
					List lista =new ArrayList();
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
						lista_fin [r] = (BilancioDettVO)lista.get(r);
					}

				insBilanci.set("elencaImpegni",lista_fin);
				insBilanci.set("radioImpegno", null) ;
				insBilanci.set("numImpegni",lista_fin.length);
				}


			//List lista_new = this.insBilanci.getListaImpegni();
			//lista_new.remove(this.insBilanci.getRadioImpegno());
			//insBilanci.setListaImpegni(lista_new);
			//this.insBilanci.setNumImpegni(insBilanci.getListaImpegni().size());


			}
			//this.insBilanci.setRadioImpegno(0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//InserisciBilancioForm insBilanci = (InserisciBilancioForm) form;
		DynaValidatorForm insBilanci = (DynaValidatorForm) form;

		try {
			// validazione
			BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) insBilanci.get("elencaImpegni");
			BilancioVO eleBilancio=(BilancioVO)insBilanci.get("bilancio");

			String amountBil=eleBilancio.getBudgetDiCapitoloStr().trim();
		    Double risultBil=Pulisci.ControllaImporto(amountBil);
		    eleBilancio.setBudgetDiCapitolo(risultBil);
		    // refresh del campo (da spostare nella form????? validate)
		    // serve per troncare eventuali cifre decimali immesse oltre la seconda
		    eleBilancio.setBudgetDiCapitoloStr(Pulisci.VisualizzaImporto( eleBilancio.getBudgetDiCapitolo()));

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
			insBilanci.set("bilancio",eleBilancio);

			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaBilancioVO(eleBilancio);
			// fine validazione
			ActionMessages errors = new ActionMessages();
			((InserisciBilancioForm) insBilanci).setConferma(true);
			((InserisciBilancioForm) insBilanci).setPressioneBottone("salva");
			((InserisciBilancioForm) insBilanci).setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			((InserisciBilancioForm) insBilanci).setConferma(false);
			((InserisciBilancioForm) insBilanci).setPressioneBottone("");
			((InserisciBilancioForm) insBilanci).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);

			return mapping.getInputForward();

		} catch (Exception e) {
			((InserisciBilancioForm) insBilanci).setConferma(false);
			((InserisciBilancioForm) insBilanci).setPressioneBottone("");
			((InserisciBilancioForm) insBilanci).setDisabilitaTutto(false);

			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		DynaValidatorForm insBilanci = (DynaValidatorForm) form;

		try {

			// n.b. il ripristina in inserimento consiste nell'azzeramento se il record è inesistente,
			// mentre se si vuole effettuare modifica su un record appena inserito deve leggere
			BilancioVO eleBilancio=(BilancioVO)insBilanci.get("bilancio");

			if ( eleBilancio!=null && eleBilancio.getIDBil()>0)
			{
				// il record è già esistente
				// lettura
				BilancioVO eleBilancioLetto=this.loadDatiINS(eleBilancio);

				insBilanci.set("bilancio",eleBilancioLetto);
				int totImpegni=0;
				if (eleBilancioLetto.getDettagliBilancio()!=null && eleBilancioLetto.getDettagliBilancio().size()>0)
				{
					totImpegni=eleBilancioLetto.getDettagliBilancio().size();

				}
				//this.insBilanci.setNumImpegni(totImpegni);
				insBilanci.set("numImpegni",totImpegni);

				BilancioDettVO[] oggettoDettBil=new BilancioDettVO[totImpegni];
				for (int i=0; i<totImpegni; i++)
				{
					oggettoDettBil[i]=eleBilancioLetto.getDettagliBilancio().get(i);

				}
				insBilanci.set("elencaImpegni",oggettoDettBil);



			}
			else
			{
				BilancioVO oggettoBil=loadBilancio();
				if (eleBilancio!=null )
				{
					if (eleBilancio.getCodBibl()!=null &&  eleBilancio.getCodBibl().trim().length()>0)
					{
						oggettoBil.setCodBibl(eleBilancio.getCodBibl());
					}
					if (eleBilancio.getCodPolo()!=null &&  eleBilancio.getCodPolo().trim().length()>0)
					{
						oggettoBil.setCodPolo(eleBilancio.getCodPolo());
					}
				}
				insBilanci.set("bilancio",oggettoBil);
				insBilanci.set("numImpegni",0);
				BilancioDettVO[] oggettoDettBil=new BilancioDettVO[0];
				insBilanci.set("elencaImpegni",oggettoDettBil);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm insBilanci = (DynaValidatorForm) form;

		try {
			BilancioDettVO[] elencaImpegni= (BilancioDettVO[]) insBilanci.get("elencaImpegni");
			BilancioVO eleBilancio=(BilancioVO)insBilanci.get("bilancio");
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

			insBilanci.set("bilancio",eleBilancio);

			//Integer[] radioImpegno=(Integer[])insBilanci.get("radioImpegno");
			((InserisciBilancioForm) insBilanci).setConferma(false);
    		((InserisciBilancioForm) insBilanci).setDisabilitaTutto(false);


			if (((InserisciBilancioForm) insBilanci).getPressioneBottone().equals("salva")) {
				((InserisciBilancioForm) insBilanci).setPressioneBottone("");

				// se il codice ordine è già valorzzato si deve procedere alla modifica

				if (eleBilancio.getIDBil()!=0)
				{
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
				else
				{
					if (!this.inserisciBilancio(eleBilancio)) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreinserimento"));
						this.saveErrors(request, errors);
						//return mapping.getInputForward();

					}
					else
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.inserimentoOK"));
						this.saveErrors(request, errors);
						return ripristina( mapping,  form,  request,  response);


/*						Timestamp prova=this.loadDatiBilancioINS();
						if (prova!=null)
						{
							// aggiorno i dati di bilancio riguardo la data
							eleBilancio.setDataUpd(prova);
							insBilanci.set("bilancio",eleBilancio);

						}
*/					}

				}

			}
			if (((InserisciBilancioForm) insBilanci).getPressioneBottone().equals("cancella")) {
				((InserisciBilancioForm) insBilanci).setPressioneBottone("");
				//BilancioVO eleBilancio=insBilanci.getBilancio();
/*				if (!this.cancellaFornitore(eleBilancio)) {
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
				}*/

			}
			return mapping.getInputForward();
		}	catch (ValidationException ve) {

			((InserisciBilancioForm) insBilanci).setConferma(false);
			((InserisciBilancioForm) insBilanci).setPressioneBottone("");
			((InserisciBilancioForm) insBilanci).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			((InserisciBilancioForm) insBilanci).setConferma(false);
			((InserisciBilancioForm) insBilanci).setPressioneBottone("");
			((InserisciBilancioForm) insBilanci).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBilancioForm insBilanci = (InserisciBilancioForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			insBilanci.setConferma(false);
			insBilanci.setPressioneBottone("");
    		insBilanci.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBilancioForm insBilanci = (InserisciBilancioForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{

						// carico i risultati della selezione nella variabile da restituire
						request.getSession().setAttribute("attributeListaSuppBilancioVO", this.AggiornaRisultatiListaSupportoDaIns(insBilanci, ricArr));
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
	/**
	 * InserisciBilancioAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppBilancioVO AggiornaRisultatiListaSupportoDaIns (InserisciBilancioForm insBilanci, ListaSuppBilancioVO eleRicArr)
	{
		try {
			List<BilancioVO> risultati=new ArrayList();
			BilancioVO eleBilancio=(BilancioVO)insBilanci.get("bilancio");
			//BilancioVO eleBilancio=(BilancioVO) insBilanci.getFornitore();
			risultati.add(eleBilancio);
			eleRicArr.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return eleRicArr;
	}


/*	private boolean sbilancio() throws Exception {

	//	insBilanci.getListaImpegni();
		int i;
		double singleBdg=0;
		double totBdg=0;
	//	for (i=0;  i < insBilanci.getListaImpegni().size(); i++)
				{
			BilancioVO bila= (BilancioVO) insBilanci.getListaImpegni().get(i);
			singleBdg=bila.getBudget();
			totBdg= totBdg+ singleBdg;

		}

		if ( totBdg >insBilanci.getTotBudget())
			{
				return true;
			}
		return false;
	}

	private boolean duplicato() throws Exception
	{
		//insBilanci.getListaImpegni();
		int i=0;
		int j=0;
		String tipImp="";
		String appoTipImp="";
		BilancioVO bilaappo;
		BilancioVO bila;

		while (j<insBilanci.getListaImpegni().size())
		{
			bilaappo= (BilancioVO) insBilanci.getListaImpegni().get(i);
			appoTipImp=bilaappo.getImpegno();

			for (i=j;  i < insBilanci.getListaImpegni().size(); i++)
			{
				bila= (BilancioVO) insBilanci.getListaImpegni().get(i);
				tipImp=bila.getImpegno();
				if (tipImp==appoTipImp && i!=j )
				{
					return true;
				}
			}
			j=j+1;
		}

		return false;
	}*/
/*
    private List loadTipoImpegno() throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//insBilanciNorm = (InserisciBilancioForm) form;
    	//insBilanciNorm.setListaTipoImpegno(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale()));
    	List prova=(List)carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale());
    	return prova;

    }
*/
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
		//insBilanciNorm.setListaTipoImpegno(lista);
		//insBilanci.set("listaTipoImpegno",lista);
		//insBilanciNorm.setListaTipoImpegno(lista);
		//insBilanci.set("listaTipoImpegno",lista);

	}




	private BilancioVO loadBilancio() throws Exception {
		List lista=new ArrayList();

		BilancioVO bil = new BilancioVO("","","", "",0.00, "" );
		bil.setDettagliBilancio(null);
		bil.setBudgetDiCapitoloStr("0,00");

		//insBilanci.setBilancio(bil);
		//insBilanci.setListaImpegni(lista);

		// impostazione di default di una riga di impegno vuota
		//List<BilancioDettVO> lista_new2 = new ArrayList();
		//BilancioDettVO dettBil2 = new BilancioDettVO("",0.00,0.00,0.00,0.00 );
		//lista_new2.add(dettBil2);
		//bil.setDettagliBilancio(lista_new2);

		return bil;
		/*		List lista_new2 = new ArrayList();
		 BilancioDettVO dettBil2 = new BilancioDettVO("1",100.00,20.00,10.00,0.00 );
		 lista_new2.add(dettBil2);
		 //insBilanci.set("dettagliBilancio",dettBil2);
		 //lista_new2.add( insBilanci.get("dettagliBilancio"));
		 BilancioVO prova= (BilancioVO)insBilanci.get("bilancio");
		 prova.setDettagliBilancio(lista_new2);
		insBilanci.set("bilancio",prova);
*/
		 //insBilanci.set("listaImpegni",lista_new2);

	}

	private boolean inserisciBilancio(BilancioVO bilancio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciBilancio(bilancio);
		return valRitorno;
	}
	private boolean modificaBilancio(BilancioVO bilancio) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaBilancio(bilancio);
		return valRitorno;
	}


	private BilancioVO loadDatiINS(BilancioVO ele) throws Exception
	{

		BilancioVO eleLetto =null;

		ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
		// condizioni di ricerca univoca


		String codP=ele.getCodPolo();
		String codB=ele.getCodBibl();
		String ese=ele.getEsercizio();
		String cap=ele.getCapitolo();
		String imp="";
		String ordina="";
		String chiama=null;
		eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<BilancioVO> recordTrovati = new ArrayList();
		try {
			recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaBilanci(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (recordTrovati.size()>0)
		{
			eleLetto=recordTrovati.get(0);

			try {
				eleLetto.setBudgetDiCapitoloStr(Pulisci.VisualizzaImporto( eleLetto.getBudgetDiCapitolo()));
				for (int w=0;  w < eleLetto.getDettagliBilancio().size(); w++)
				{
					//BilancioDettVO elem = bil.getDettagliBilancio().get(w);
					eleLetto.getDettagliBilancio().get(w).setBudgetStr(Pulisci.VisualizzaImporto( eleLetto.getDettagliBilancio().get(w).getBudget()));
					//lista.add(elem);
				}
			} catch (Exception e) {
			    	//e.printStackTrace();
			    	//throw new ValidationException("importoErrato",
			    	//		ValidationExceptionCodici.importoErrato);
				eleLetto.setBudgetDiCapitoloStr("0,00");
			}
		}

		return eleLetto;
	}


}
