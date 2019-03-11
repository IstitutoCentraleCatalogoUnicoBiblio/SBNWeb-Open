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
package it.iccu.sbn.web.actions.acquisizioni.profiliacquisto;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.profiliacquisto.InserisciProfiloForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

public class InserisciProfiloAction extends LookupDispatchAction {
	//private InserisciProfiloForm insProfilo;
	//private InserisciProfiloForm insProfiloNorm;



	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
//		map.put("ricerca.button.insRiga","inserisciRiga");
//		map.put("ricerca.button.cancRiga","cancellaRiga");
//		map.put("ricerca.button.stampa","stampa");
		map.put("ricerca.label.fornitori","fornitore");
		map.put("ricerca.label.sezione","sezioneCerca");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.insForn","inserisciForn");
		map.put("ricerca.button.cancForn","cancellaForn");
		map.put("ricerca.label.bibliolist", "biblioCerca");

		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciProfiloForm insProfiloNorm = (InserisciProfiloForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_PROFILI_DI_ACQUISTO, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		InserisciProfiloForm insProfiloNorm = (InserisciProfiloForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!insProfiloNorm.isSessione())
			{
				insProfiloNorm.setSessione(true);
			}
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();


			DynaValidatorForm insProfilo = (DynaValidatorForm) form;
			StrutturaProfiloVO oggettoProf=this.loadDatiProfilo();

			// preimpostazione della schermata di inserimento con i valori ricercati

			if (request.getSession().getAttribute("ATTRIBUTEListaSuppProfiloVO")!=null)
			{
				ListaSuppProfiloVO ele=(ListaSuppProfiloVO) request.getSession().getAttribute("ATTRIBUTEListaSuppProfiloVO");
				request.getSession().removeAttribute("ATTRIBUTEListaSuppProfiloVO");
				if (ele.getSezione()!=null )
				{
					oggettoProf.setSezione(ele.getSezione());
				}
				if (ele.getProfilo()!=null )
				{
					oggettoProf.setProfilo(ele.getProfilo());;
				}
			}


			// reimposta la fattura con i valori immessi nella parte superiore
			if (insProfilo.get("datiProfilo")!=null )
			{
				StrutturaProfiloVO oggettoTemp=(StrutturaProfiloVO) insProfilo.get("datiProfilo");

				if (oggettoTemp.getCodBibl()!=null || (oggettoTemp.getProfilo()!=null && oggettoTemp.getProfilo().getCodice()!=null) || (oggettoTemp.getSezione()!=null && oggettoTemp.getSezione().getCodice()!=null) || (oggettoTemp.getPaese()!=null && oggettoTemp.getPaese().getCodice()!=null) || (oggettoTemp.getLingua()!=null && oggettoTemp.getLingua().getCodice()!=null)    )
				{
					oggettoProf=(StrutturaProfiloVO) insProfilo.get("datiProfilo");
				}
			}
			if (biblio!=null &&   (oggettoProf.getCodBibl()==null || (oggettoProf.getCodBibl()!=null && oggettoProf.getCodBibl().trim().length()==0)))
			{
				oggettoProf.setCodBibl(biblio);
			}


			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			oggettoProf.setCodPolo(polo);

			// carico eventuali righe inserite
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");
			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			oggettoProf.setListaFornitori(lista);
			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				oggettoProf.setCodBibl(bibScelta.getCod_bib());
			}

			insProfilo.set("datiProfilo",oggettoProf);


			//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
			ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
 			{
				if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
				{
					if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
					{
						oggettoProf.getSezione().setCodice(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
						oggettoProf.setIDSez(ricSez.getSelezioniChiamato().get(0).getIdSezione());
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					oggettoProf.getSezione().setCodice("");
				}
				// vedere se è necessaria l'impostazione
				insProfilo.set("datiProfilo",oggettoProf);
				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");

 			}


			//controllo se ho un risultato di una lista di supporto FORNITORI richiamata da questa pagina (risultato della simulazione)

			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						//dal size aggiungo le righe selezionate e importateù
						if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
						{
							int indiceStart=oggettoProf.getListaFornitori().size();
							for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
							{
								indiceStart=indiceStart+1;
								FornitoreVO eleRicForn = ricForn.getSelezioniChiamato().get(i);
								StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(indiceStart),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
								oggettoProf.getListaFornitori().add(eleListaForn);
							}

						}
						else
						{
							oggettoProf.setListaFornitori(new ArrayList());
							for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
							{
								FornitoreVO eleRicForn = ricForn.getSelezioniChiamato().get(i);
								StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(i),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
								oggettoProf.getListaFornitori().add(eleListaForn);

							}
						}
						insProfilo.set("datiProfilo",oggettoProf);
					}
				}
/*				else
				{
					// pulizia della maschera di ricerca
					this.ricBuoni.setTipoOrdine("");
					this.ricBuoni.setAnno("");
					this.ricBuoni.setNumero("");

				}
*/
				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

 			}


			// da qui !!!!!!


/*			// reimposta la fattura con i valori immessi
			if (insProfilo.get("datiProfilo")!=null )
			{
				StrutturaProfiloVO oggettoTemp=(StrutturaProfiloVO) insProfilo.get("datiProfilo");

				if (oggettoTemp.getProfilo()!=null && oggettoTemp.getCodBibl()!=null && oggettoTemp.getProfilo().getCodice()!=null   )
				{
					oggettoProf=(StrutturaProfiloVO) insProfilo.get("datiProfilo");
				}
			}*/

			insProfilo.set("datiProfilo",oggettoProf);
			int totProfili=0;
			if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
			{
				totProfili=oggettoProf.getListaFornitori().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			insProfilo.set("numForn",totProfili);

			StrutturaTerna[] oggettoDettProf=new StrutturaTerna[totProfili];
			for (int i=0; i<totProfili; i++)
			{
				//FornitoreVO eleRicForn = (FornitoreVO) oggettoProf.getListaFornitori().get(i);
				//StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(i+1),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
				oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);
			}
			insProfilo.set("elencaFornitori",oggettoDettProf);

			List arrListaPaesi=this.loadPaesi();
			insProfilo.set("listaPaesi",arrListaPaesi);

			List arrListaLingue=this.loadLingue();
			insProfilo.set("listaLingue",arrListaLingue);

			// gestione lista supporto richiamata per i profili
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				// preimpostazione di campi con elemento ricercato e non trovato
				StrutturaProfiloVO oggProf= (StrutturaProfiloVO) insProfilo.get("datiProfilo");
				oggProf.getSezione().setCodice(ricArr.getSezione().getCodice());
				oggProf.getProfilo().setDescrizione(ricArr.getProfilo().getDescrizione());
				insProfilo.set("datiProfilo", oggProf);
				insProfilo.set("visibilitaIndietroLS",true);
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

	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm insProfilo = (DynaValidatorForm) form;
		try {
			// aggiorno le righe eventuali
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) insProfilo.get("datiProfilo");

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			insProfilo.set("datiProfilo",eleProfilo);

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
			this.impostaSezioneCerca(request,mapping, eleProfilo);
			//return mapping.findForward("sezioneCerca");
			return mapping.findForward("sezioneLista");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaSezioneCerca( HttpServletRequest request, ActionMapping mapping, StrutturaProfiloVO eleProfilo)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=eleProfilo.getCodBibl();
		String codSez=eleProfilo.getSezione().getCodice();
		String desSez="";
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
	}catch (Exception e) {	}
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciProfiloForm insProfilo = (InserisciProfiloForm) form;

// 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

		try {
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
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

	public ActionForward fornitore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		DynaValidatorForm insProfilo = (DynaValidatorForm) form;

		try {

			// CONTROLLO CHECK DI RIGA
/*			Integer[] radioForn=(Integer[])insProfilo.get("radioForn");

			if (radioForn.length!=0)
			{
				int indiceRigaIns= radioForn[0]; // unico check
			}
			else
			{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.buoniOrdine.crea"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
			}*/


			// aggiorno le righe eventuali
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) insProfilo.get("datiProfilo");

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			insProfilo.set("datiProfilo",eleProfilo);

			// impongo il profilo,  il paese
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

			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleProfilo.getCodBibl();
			String codForn="";
			String nomeForn="";
			String codProfAcq="";
			String paeseForn="";
			String tipoPForn="";
			String provForn="";
			String loc="0"; // I FORNITORI DEVONO ESSERE DI BIBLIOTECA
			String chiama=mapping.getPath();
			//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
			eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
			//ricerca.add(eleRicerca);
			request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);

			return mapping.findForward("fornitori");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward inserisciForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm insProfilo = (DynaValidatorForm) form;

		try {

			// aggiorno le righe eventuali
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) insProfilo.get("datiProfilo");

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			insProfilo.set("datiProfilo",eleProfilo);

			// impongo il profilo,  il paese
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleProfilo.getCodBibl();
			String codForn="";
			String nomeForn="";
			String codProfAcq="";
			String paeseForn="";
			String tipoPForn="";
			String provForn="";
			String loc="0"; // I FORNITORI DEVONO ESSERE DI BIBLIOTECA
			String chiama=mapping.getPath();
			//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
			eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
			//ricerca.add(eleRicerca);
			eleRicerca.setModalitaSif(false);
			request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);

			return mapping.findForward("fornitori");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



/*	public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm insProfilo = (DynaValidatorForm) form;

		try {

			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");

			StrutturaTerna dettProf = new StrutturaTerna("1", "","");
			if (elencaRigheProf!=null &&  elencaRigheProf.length!=0)
			{
				StrutturaTerna[] listaDettProf=new StrutturaTerna[elencaRigheProf.length +1];

				for (int i=0; i<elencaRigheProf.length; i++)
				{
					listaDettProf[i]=elencaRigheProf[i];
				}
				// imposta numero di riga
				dettProf.setCodice1(String.valueOf(elencaRigheProf.length+1));
				listaDettProf[elencaRigheProf.length]=dettProf;
				insProfilo.set("elencaFornitori",listaDettProf);
				insProfilo.set("numForn",listaDettProf.length);
				// ricarica la fattura completamente
				List<StrutturaTerna> elencaRigheProfArr=new ArrayList();
				for (int i=0; i<listaDettProf.length; i++)
				{
					elencaRigheProfArr.add(listaDettProf[i]);
				}

			}
			else
			{
				elencaRigheProf=new StrutturaTerna[1];
				elencaRigheProf[0]=dettProf;
				insProfilo.set("elencaFornitori",elencaRigheProf);
			}
			insProfilo.set("numForn",elencaRigheProf.length);
			// trasformazione in arraylist



			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
*/
	public ActionForward cancellaForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm insProfilo = (DynaValidatorForm) form;
		try {
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");

			Integer[] radioForn=(Integer[])insProfilo.get("radioForn");
			if (radioForn.length!=0)
			{
				if (elencaRigheProf.length!=0 )
				{
					List lista =new ArrayList();
					for (int t=0;  t < elencaRigheProf.length; t++)
					{
						StrutturaTerna elem = elencaRigheProf[t];
						lista.add(elem);
					}

					//String[] appo=  selectedDatiIntest;
					int i= (radioForn.length) -1;
					// ciclo dall'ultimo codice selezionato
					while (i>=0)
					{
						int elem = radioForn[i];
						// il valore del num riga è superiore di una unità rispetto all'indice dell'array
						lista.remove(elem);
						i=i-1;
					}

					//this.renumera(lista);


					StrutturaTerna[] lista_fin =new StrutturaTerna [lista.size()];

					for (int r=0;  r < lista.size(); r++)
					{
						lista_fin [r] = (StrutturaTerna)lista.get(r);
						lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
					}

					insProfilo.set("elencaFornitori",lista_fin);
					insProfilo.set("radioForn", null) ;
					insProfilo.set("numForn",lista_fin.length);
					// ricarica la fattura completamente
					StrutturaProfiloVO appoProf=(StrutturaProfiloVO)insProfilo.get("datiProfilo");
					List<StrutturaTerna> elencaRigheProfArr=new ArrayList();
					for (int j=0; j<lista_fin.length; j++)
					{
						elencaRigheProfArr.add(lista_fin[j]);
					}
					appoProf.setListaFornitori(elencaRigheProfArr);
					insProfilo.set("datiProfilo",appoProf);

				}
			}
			insProfilo.set("radioForn",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm insProfilo = (DynaValidatorForm) form;
		try {
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");

			Integer[] radioForn=(Integer[])insProfilo.get("radioForn");
			if (radioForn.length!=0)
			{
				if (elencaRigheProf.length!=0 )
				{
					List lista =new ArrayList();
					for (int t=0;  t < elencaRigheProf.length; t++)
					{
						StrutturaTerna elem = elencaRigheProf[t];
						lista.add(elem);
					}

					//String[] appo=  selectedDatiIntest;
					int i= (radioForn.length) -1;
					// ciclo dall'ultimo codice selezionato
					while (i>=0)
					{
						int elem = radioForn[i];
						// il valore del num riga è superiore di una unità rispetto all'indice dell'array
						lista.remove(elem);
						i=i-1;
					}

					//this.renumera(lista);


					StrutturaTerna[] lista_fin =new StrutturaTerna [lista.size()];

					for (int r=0;  r < lista.size(); r++)
					{
						lista_fin [r] = (StrutturaTerna)lista.get(r);
						lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
					}

					insProfilo.set("elencaFornitori",lista_fin);
					insProfilo.set("radioForn", null) ;
					insProfilo.set("numForn",lista_fin.length);
					// ricarica la fattura completamente
					StrutturaProfiloVO appoProf=(StrutturaProfiloVO)insProfilo.get("datiProfilo");
					List<StrutturaTerna> elencaRigheProfArr=new ArrayList();
					for (int j=0; j<lista_fin.length; j++)
					{
						elencaRigheProfArr.add(lista_fin[j]);
					}
					appoProf.setListaFornitori(elencaRigheProfArr);
					insProfilo.set("datiProfilo",appoProf);

				}
			}
			insProfilo.set("radioForn",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	*/

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//InserisciProfiloForm insProfilo = (InserisciProfiloForm) form;

		DynaValidatorForm insProfilo = (DynaValidatorForm) form;

		try {
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) insProfilo.get("datiProfilo");
			eleProfilo.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			insProfilo.set("datiProfilo",eleProfilo);


			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaStrutturaProfiloVO(eleProfilo);
			// fine validazione

			ActionMessages errors = new ActionMessages();
			((InserisciProfiloForm) insProfilo).setConferma(true);
			((InserisciProfiloForm) insProfilo).setPressioneBottone("salva");
			((InserisciProfiloForm) insProfilo).setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
			}	catch (ValidationException ve) {
			((InserisciProfiloForm) insProfilo).setConferma(false);
			((InserisciProfiloForm) insProfilo).setPressioneBottone("");
			((InserisciProfiloForm) insProfilo).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		} catch (Exception e) {
			((InserisciProfiloForm) insProfilo).setConferma(false);
			((InserisciProfiloForm) insProfilo).setPressioneBottone("");
			((InserisciProfiloForm) insProfilo).setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciProfiloForm insProfilo = (InserisciProfiloForm) form;

		try {
			// n.b. il ripristina in inserimento consiste nell'azzeramento se il record è inesistente,
			// mentre se si vuole effettuare modifica su un record appena inserito deve leggere
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) insProfilo.get("datiProfilo");

			if ( eleProfilo!=null && eleProfilo.getProfilo()!=null && eleProfilo.getProfilo().getCodice()!=null  && eleProfilo.getProfilo().getCodice().trim().length()>0)
			{
				// il record è già esistente
				// lettura
				StrutturaProfiloVO eleProfiloLetto=this.loadDatiINS(eleProfilo);


				insProfilo.set("datiProfilo",eleProfiloLetto);
				int totProfili=0;
				if (eleProfiloLetto.getListaFornitori()!=null && eleProfiloLetto.getListaFornitori().size()>0)
				{
					totProfili=eleProfiloLetto.getListaFornitori().size();
				}
				//this.insBilanci.setNumImpegni(totImpegni);
				insProfilo.set("numForn",totProfili);

				StrutturaTerna[] oggettoDettProf=new StrutturaTerna[totProfili];
				for (int i=0; i<totProfili; i++)
				{
					oggettoDettProf[i]=(StrutturaTerna) eleProfiloLetto.getListaFornitori().get(i);
				}
				insProfilo.set("elencaFornitori",oggettoDettProf);

			}
			else
			{
				StrutturaProfiloVO oggettoProf=this.loadDatiProfilo();
				if (eleProfilo!=null )
				{
					if (eleProfilo.getCodBibl()!=null &&  eleProfilo.getCodBibl().trim().length()>0)
					{
						oggettoProf.setCodBibl(eleProfilo.getCodBibl());
					}
					if (eleProfilo.getCodPolo()!=null &&  eleProfilo.getCodPolo().trim().length()>0)
					{
						oggettoProf.setCodPolo(eleProfilo.getCodPolo());
					}
				}
				insProfilo.set("datiProfilo",oggettoProf);
				insProfilo.set("numForn",0);
				StrutturaTerna[] oggettoDettProf=new StrutturaTerna[0];
				insProfilo.set("elencaFornitori",oggettoDettProf);
			}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm insProfilo = (DynaValidatorForm) form;

		try {
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) insProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) insProfilo.get("datiProfilo");
			eleProfilo.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			insProfilo.set("datiProfilo",eleProfilo);

			//Integer[] radioImpegno=(Integer[])insBilanci.get("radioImpegno");

			((InserisciProfiloForm) insProfilo).setConferma(false);
			((InserisciProfiloForm) insProfilo).setDisabilitaTutto(false);

			if (((InserisciProfiloForm) insProfilo).getPressioneBottone().equals("salva")) {
				((InserisciProfiloForm) insProfilo).setPressioneBottone("");

				// se il codice ordine è già valorzzato si deve procedere alla modifica
				if (eleProfilo.getProfilo().getCodice()!=null  && eleProfilo.getProfilo().getCodice().length()>0)
				{
					if (!this.modificaProfilo(eleProfilo)) {
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
					if (!this.inserisciProfilo(eleProfilo)) {
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

					}
				}
			}
			if (((InserisciProfiloForm) insProfilo).getPressioneBottone().equals("cancella")) {
				((InserisciProfiloForm) insProfilo).setPressioneBottone("");
					if (!this.cancellaProfilo(eleProfilo)) {
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
					}
			}
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			((InserisciProfiloForm) insProfilo).setConferma(false);
			((InserisciProfiloForm) insProfilo).setPressioneBottone("");
			((InserisciProfiloForm) insProfilo).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		} catch (Exception e) {
			((InserisciProfiloForm) insProfilo).setConferma(false);
			((InserisciProfiloForm) insProfilo).setPressioneBottone("");
			((InserisciProfiloForm) insProfilo).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciProfiloForm insProfilo = (InserisciProfiloForm) form;

		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			insProfilo.setConferma(false);
			insProfilo.setPressioneBottone("");
    		insProfilo.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciProfiloForm insProfilo = (InserisciProfiloForm) form;

		try {
			ActionMessages errors = new ActionMessages();
			insProfilo.setConferma(true);
			insProfilo.setPressioneBottone("cancella");
    		insProfilo.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			insProfilo.setConferma(false);
			insProfilo.setPressioneBottone("");
    		insProfilo.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


	private StrutturaProfiloVO loadDatiProfilo() throws Exception {
		// aggiungere data di sistema E IL CODICE DELLA BIBLIOTECA
		// ASSEGNAZIONE DELLA data di sistema
		Date dataodierna=new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataOdiernaStr =formato.format(dataodierna);
		// estrapolazione dell'anno
		Calendar c=new GregorianCalendar();
	 	int anno=c.get(Calendar.YEAR);
	 	String annoAttuale="";
	 	annoAttuale=Integer.valueOf(anno).toString();
		//new StrutturaProfiloVO( polo, bibl, prof,  sez,  lin,  pae,  listProf, tipoVar );
	 	StrutturaProfiloVO prof=new StrutturaProfiloVO("", "", new StrutturaCombo("",""), new StrutturaCombo("",""),new StrutturaCombo("",""),new StrutturaCombo("",""),null,"");
		StrutturaTerna eleForn =new StrutturaTerna("", "", "");
		List lista=new ArrayList();
		//lista.add(eleForn);
		prof.setListaFornitori(lista);
		return prof;

	}

	private StrutturaProfiloVO loadDatiINS(StrutturaProfiloVO ele) throws Exception {

		StrutturaProfiloVO eleLetto =null;

		ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
		// carica i criteri di ricerca da passare alla esamina
		String codP=ele.getCodPolo();
		String codB=ele.getCodBibl();
		StrutturaCombo prof=new StrutturaCombo(ele.getProfilo().getCodice(),"");
		StrutturaCombo sez=new StrutturaCombo("","");
		StrutturaCombo lin=new StrutturaCombo("","");
		StrutturaCombo pae=new StrutturaCombo("","");
		String chiama=null;
		String ordina="";
		eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae,  chiama, ordina );

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<StrutturaProfiloVO> recordTrovati = new ArrayList();
		//gestire l'esistenza del risultato e che sia univoco
		try {
			recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaProfili(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (recordTrovati.size()>0)
		{
			eleLetto=recordTrovati.get(0);

		}
		return eleLetto;

	}



	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciProfiloForm insProfilo = (InserisciProfiloForm) form;

		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{

						// carico i risultati della selezione nella variabile da restituire
						request.getSession().setAttribute("attributeListaSuppProfiloVO", this.AggiornaRisultatiListaSupportoDaIns( insProfilo,ricArr));
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
	 * InserisciProfiloAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppProfiloVO AggiornaRisultatiListaSupportoDaIns (InserisciProfiloForm insProfilo, ListaSuppProfiloVO eleRicArr)
	{
		try {
			List<StrutturaProfiloVO> risultati=new ArrayList();
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO)insProfilo.get("datiProfilo");
			risultati.add(eleProfilo);
			eleRicArr.setSelezioniChiamato(risultati);

		} catch (Exception e) {

		}
		return eleRicArr;
	}


	private boolean inserisciProfilo(StrutturaProfiloVO profilo) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciProfilo(profilo);
		return valRitorno;
	}

	private boolean modificaProfilo(StrutturaProfiloVO profilo) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaProfilo(profilo);
		return valRitorno;
	}

	private boolean cancellaProfilo(StrutturaProfiloVO profilo) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaProfilo(profilo);
		return valRitorno;
	}

	private List loadLingue() throws Exception {

    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceLingua());
		return lista;

/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("ITA","ITA - Italiano");
		lista.add(elem);
		elem = new StrutturaCombo("ENG","ENG - Inglese");
		lista.add(elem);
		elem = new StrutturaCombo("FRE","FRE - Francese");
		lista.add(elem);
		elem = new StrutturaCombo("JPN","JPN - Giapponese");
		lista.add(elem);

		//this.esaProfilo.setListaLingue(lista);
		return lista;*/
	}



	private List loadPaesi() throws Exception {

    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese());
		return lista;

	}


}
