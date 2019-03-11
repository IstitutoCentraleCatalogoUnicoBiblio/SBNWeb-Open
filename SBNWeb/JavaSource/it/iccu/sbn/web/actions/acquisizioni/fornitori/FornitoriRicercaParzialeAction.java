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
package it.iccu.sbn.web.actions.acquisizioni.fornitori;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.fornitori.FornitoriRicercaParzialeForm;
import it.iccu.sbn.web.actionforms.gestionestampe.periodici.StampaListaFascicoliForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
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

public class FornitoriRicercaParzialeAction extends LookupDispatchAction implements SbnAttivitaChecker{
	//private FornitoriRicercaParzialeForm ricFornitori;



	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.button.indietro","indietro");
		map.put("button.crea.profiliAcquisto","profiloCerca");
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
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(
	            		utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_FORNITORI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		FornitoriRicercaParzialeForm currentForm = (FornitoriRicercaParzialeForm) form;


        // Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)

		// Ottobre 2013: Viene gestito il nuovo campo PresenzaTastoCrea che consente la prospettazione del "Tasto Crea Editore"
		// solo dopo una ricerca a vuoto dell'editore -->
		currentForm.setPresenzaTastoCrea("SI");
		HttpSession session = request.getSession();
		if (session.getAttribute(Constants.CURRENT_MENU).equals("menu.gestionebibliografica.editori")) {
			currentForm.setEditore("SI");
			currentForm.setCreazLegameTitEdit("NO");
			currentForm.setCartiglioEditore("NO");
		} else if (request.getParameter("CARTIGLIOEDITORE") != null) {
			currentForm.setEditore("SI");
			currentForm.setCartiglioEditore("SI");
			currentForm.setCreazLegameTitEdit("NO");
			currentForm.setLSRicerca(true);
			if (request.getAttribute("FornitoreVO") != null) {
				FornitoreVO fornVO = (FornitoreVO)request.getAttribute("FornitoreVO");
				currentForm.setCodForn(fornVO.getCodFornitore());
				currentForm.setNomeForn(fornVO.getNomeFornitore());
			}
		} else if (request.getAttribute("creazLegameTitEdit") != null) {
			currentForm.setEditore("SI");
			currentForm.setCreazLegameTitEdit("SI");
			currentForm.setCartiglioEditore("NO");
			if (request.getAttribute("bid") != null) {
				currentForm.setBid((String) request.getAttribute("bid"));
			}
			if (request.getAttribute("desc") != null) {
				currentForm.setDescr((String) request.getAttribute("desc"));
			}
		} else {
			currentForm.setEditore("NO");
			currentForm.setCartiglioEditore("NO");
			currentForm.setCreazLegameTitEdit("NO");
		}


		try {
			//setto il token per le transazioni successive
			this.saveToken(request);

			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
	            return mapping.getInputForward();

			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}

	        // Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
			if ((session.getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.fornitori") || currentForm.getEditore().equals("SI"))
					&& (currentForm.getListaTipoForn()==null ||
							(currentForm.getListaTipoForn()!=null && currentForm.getListaTipoForn().size()==0))
					&& navi.getActionCaller()==null)	{
				// si proviene dal menu
				Pulisci.PulisciVar(request);
				currentForm.setRicercaLocaleStr("");
			}
			String biblio=navi.getUtente().getCodBib();
			if (biblio!=null
					&& (currentForm.getCodBibl()==null ||
							(currentForm.getCodBibl()!=null && currentForm.getCodBibl().trim().length()==0)))
			{
				currentForm.setCodBibl(biblio);
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				currentForm.setCodBibl(bibScelta.getCod_bib());
			}

			this.loadTipoOrdinamento( currentForm);
			this.loadTipologieFornitore( currentForm);
			this.loadPaese( currentForm);
			this.loadProvincia( currentForm);

			 // Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
			if (currentForm.getEditore().equals("SI")) {
				this.loadRegione(currentForm);
			}

			if (currentForm.getCartiglioEditore().equals("SI")) {
				currentForm.setTipoRicerca("inizio");
			} else {
				currentForm.setTipoRicerca("parole");
			}


			// condizioni di ricerca univoca
			if ( currentForm.getCodForn()!=null && currentForm.getCodForn().trim().length()!=0 ) {
				// ripulitura di tutti gli altri campi e disabilitazione
				currentForm.setNomeForn("");
				currentForm.setTipoForn("");
				currentForm.setProvinciaForn("");
				currentForm.setProfAcqForn("");
				currentForm.setProfAcqFornDes("");
				currentForm.setPaeseForn("");
				currentForm.setRicercaLocale(false);
			}

			//controllo se ho un risultato di una lista di supporto PROFILI richiamata da questa pagina (risultato della simulazione)
			ListaSuppProfiloVO ricProf=(ListaSuppProfiloVO) session.getAttribute("attributeListaSuppProfiloVO");
			if (ricProf!=null && ricProf.getChiamante()!=null && ricProf.getChiamante().equals(mapping.getPath()))
 			{
				if (ricProf!=null && ricProf.getSelezioniChiamato()!=null && ricProf.getSelezioniChiamato().size()!=0 )
				{
					if (ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice()!=null
							&& ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice().length()!=0 )
					{
						currentForm.setProfAcqForn(ricProf.getSelezioniChiamato().get(0).getProfilo().getCodice());
						currentForm.setProfAcqFornDes(ricProf.getSelezioniChiamato().get(0).getProfilo().getDescrizione());
						currentForm.setRientroDaSif(true);

					}
				}
				else
				{
					// pulizia della maschera di ricerca
					currentForm.setProfAcqForn("");
					currentForm.setProfAcqFornDes("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				session.removeAttribute("attributeListaSuppProfiloVO");
				session.removeAttribute("profiliSelected");
				session.removeAttribute("criteriRicercaProfilo");

 			}

			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) session.getAttribute("attributeListaSuppFornitoreVO");

			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
			if (currentForm.getEditore().equals("SI")) {
				navi.setTesto("Ricerca Editore (Produzione editoriale)");
				currentForm.setRicercaLocaleStr("");
				currentForm.setPresenzaTastoCrea("NO");
			}



			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null) {
				// per il layout
				currentForm.setVisibilitaIndietroLS(true);
				// aggiunta SOLO SU SIF FORNITORI l'inibizione sul tasto crea se si proviene da buoni ord - fatture - comunicazioni
				if (ricArr.getChiamante().endsWith("RicercaParziale")
						|| ricArr.getChiamante().equals("/acquisizioni/buoniordine/inserisciBuonoOrdine")
						|| ricArr.getChiamante().equals("/acquisizioni/buoniordine/esaminaBuonoOrdine")
						|| ricArr.getChiamante().equals("/acquisizioni/fatture/inserisciFattura")
						|| ricArr.getChiamante().equals("/acquisizioni/fatture/esaminaFattura")
						|| ricArr.getChiamante().equals("/acquisizioni/comunicazioni/inserisciCom")
						|| ricArr.getChiamante().equals("/acquisizioni/comunicazioni/esaminaCom")
						|| navi.getCallerForm() instanceof StampaListaFascicoliForm) {
					currentForm.setLSRicerca(true); // fai rox 2
				}
				// per il layout fine gestione del caso di richiamo della ricerca non sif
				if (ricArr.isModalitaSif()) {
					List<FornitoreVO> listaFornitori;
					listaFornitori=this.getListaFornitoriVO(ricArr ); // va in errore se non ha risultati
					this.caricaAttributeListaSupp( currentForm, ricArr );
					return mapping.findForward("cerca");
				} else {
					if (!currentForm.isRientroDaSif())
						// per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
					{
						this.caricaAttributeListaSupp( currentForm, ricArr );
					}
					else
					{
						currentForm.setRientroDaSif(false);
					}

					if (ricArr!=null && ricArr.getLocale().equals("1"))
					{
						currentForm.setRicercaLocaleStr("on");
					}
					else
					{
						currentForm.setRicercaLocaleStr("");
					}
					return mapping.getInputForward();
				}
			}
			else
			{
				return mapping.getInputForward();
			}
	}	catch (ValidationException ve) {
			// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaCambi=this.getListaCambiVO(ricArr )
			// assenzaRisultati = 4001;
			if (ve.getError()==4001)
			{
				// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
				ListaSuppFornitoreVO ricArrRec=(ListaSuppFornitoreVO) session.getAttribute("attributeListaSuppFornitoreVO");
				this.caricaAttributeListaSupp( currentForm, ricArrRec );
				//ricFornitori.setVisibilitaIndietroLS(true);
				//return mapping.getInputForward();
				//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
				if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null
						&& ricArrRec.getChiamante().endsWith("RicercaParziale")) {
	 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
					// in tal caso la ricerca senza esito
					// non deve automaticamente presentare la maschera di crea ma emettere il messaggio

					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

					return mapping.getInputForward();
				}
				else
				{
					// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
					// in tal caso la ricerca senza esito
					// deve automaticamente presentare la maschera di crea
					this.passaCriteri( currentForm, request); // imposta il crea con i valori cercati
					return mapping.findForward("crea");
				}

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
		FornitoriRicercaParzialeForm ricFornitori = (FornitoriRicercaParzialeForm) form;
		try {
			String chiama=null;
			if (ricFornitori.isVisibilitaIndietroLS())
			{
				ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				chiama=ricArr.getChiamante();
			}

			// condizioni di ricerca univoca
			if ( ricFornitori.getCodForn()!=null && ricFornitori.getCodForn().trim().length()!=0 ) {
				// ripulitura di tutti gli altri campi e disabilitazione
				ricFornitori.setNomeForn("");
				ricFornitori.setTipoForn("");
				ricFornitori.setProvinciaForn("");
				ricFornitori.setProfAcqForn("");
				ricFornitori.setProfAcqFornDes("");
				ricFornitori.setPaeseForn("");
				ricFornitori.setRicercaLocale(false);
			}


			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			request.getSession().removeAttribute("ultimoBloccoFornitori");
			String codP="";
			String codB="";
			String loc="0";
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			//codB=biblio;
			codB=ricFornitori.getCodBibl();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			codP=polo;
			if (ricFornitori.getRicercaLocaleStr()!=null && ricFornitori.getRicercaLocaleStr().equals("on"))
			{
				ricFornitori.setRicercaLocale(true);
			}
			else
			{
				ricFornitori.setRicercaLocale(false);
			}
			if (ricFornitori.isRicercaLocale())
			{
				loc="1";
			}
			String codForn=ricFornitori.getCodForn();
			String nomeForn=ricFornitori.getNomeForn();
			String codProfAcq=ricFornitori.getProfAcqForn();
			String paeseForn=ricFornitori.getPaeseForn();
			String tipoPForn=ricFornitori.getTipoForn() ;
			String provForn=ricFornitori.getProvinciaForn();
			eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);

			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
			eleRicerca.setRegione(ricFornitori.getRegioneForn());
			eleRicerca.setIsbnEditore(ricFornitori.getIsbnEditore());

			eleRicerca.setElemXBlocchi(ricFornitori.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (ricFornitori.getNomeForn()!=null &&  ricFornitori.getNomeForn().trim().length()>0)
			{
				GeneraChiave keyDuoble = new GeneraChiave(ricFornitori.getNomeForn().trim(),"");
				keyDuoble.estraiChiavi("",ricFornitori.getNomeForn().trim());
				eleRicerca.setChiaveFor(keyDuoble.getKy_cles1_A());
			}

			if (ricFornitori.getTipoOrdinamSelez()!=null && !ricFornitori.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricFornitori.getTipoOrdinamSelez());
			}
			if (ricFornitori.getTipoRicerca()!=null && !ricFornitori.getTipoRicerca().equals(""))
			{
				eleRicerca.setTipoRicerca(ricFornitori.getTipoRicerca());
			}

			request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);

		List<FornitoreVO> listaFornitori;
		listaFornitori=this.getListaFornitoriVO(eleRicerca);

		// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
		request.setAttribute("editore", ricFornitori.getEditore());
		request.setAttribute("creazLegameTitEdit", ricFornitori.getCreazLegameTitEdit());
		request.setAttribute("cartiglioEditore", ricFornitori.getCartiglioEditore());
		request.setAttribute("bid", ricFornitori.getBid());
		request.setAttribute("descr", ricFornitori.getDescr());

		return mapping.findForward("cerca");
		}	catch (ValidationException ve) {
				if (ve.getError() == 4001) {
					ricFornitori.setPresenzaTastoCrea("SI");
				}

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
		FornitoriRicercaParzialeForm ricFornitori = (FornitoriRicercaParzialeForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!ricFornitori.isSessione())
				{
					ricFornitori.setSessione(true);
				}
				return mapping.getInputForward();
			}
		resetToken(request);
		this.passaCriteri( ricFornitori, request);

		// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// Inserito if sulla provenienza dalla nuova voce di Menù Editori (Produzione editoriale)
		request.getSession().setAttribute("editore", ricFornitori.getEditore());

		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}

	}

	private void passaCriteri(FornitoriRicercaParzialeForm ricFornitori, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {
			String chiama=null;
			if (ricFornitori.isLSRicerca())
			{
				ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String codP="";
			String codB="";
			String loc="0";
			if (ricFornitori.getRicercaLocaleStr()!=null && ricFornitori.getRicercaLocaleStr().equals("on"))
			{
				ricFornitori.setRicercaLocale(true);
			}
			else
			{
				ricFornitori.setRicercaLocale(false);
			}
			if (ricFornitori.isRicercaLocale())
			{
				String ticket=Navigation.getInstance(request).getUserTicket();
				// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
				String biblio=Navigation.getInstance(request).getUtente().getCodBib();
				codB=biblio;
				codB=ricFornitori.getCodBibl();
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				codP=polo;
				loc="1";
			}
			String codForn=ricFornitori.getCodForn();
			String nomeForn=ricFornitori.getNomeForn();
			String codProfAcq=ricFornitori.getProfAcqForn();
			String paeseForn=ricFornitori.getPaeseForn();
			String tipoPForn=ricFornitori.getTipoForn() ;
			String provForn=ricFornitori.getProvinciaForn();
			//String chiama=null;
			eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
			//ricerca.add(eleRicerca);
			eleRicerca.setElemXBlocchi(ricFornitori.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			request.getSession().setAttribute("ATTRIBUTEListaSuppFornitoreVO", eleRicerca);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		FornitoriRicercaParzialeForm ricFornitori = (FornitoriRicercaParzialeForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppFornitoreVO ricArr=(ListaSuppFornitoreVO) request.getSession().getAttribute("attributeListaSuppFornitoreVO");

			if (ricArr!=null )
			{
				// gestione del chiamante
				if (ricArr!=null && ricArr.getChiamante()!=null)
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
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private void loadTipologieFornitore(FornitoriRicercaParzialeForm ricFornitori) throws Exception {
		// Modifiche Maggio 2013 Da mail Contardi/Scognamiglio contenente manuale di Interrogazione Produzione editoriale
		// nella lista Tipo fornitore lasciare solo editore commerciale e non commerciale
		if (ricFornitori.getEditore() != null && ricFornitori.getEditore().equals("SI")) {
			List lista = new ArrayList();
			ComboCodDescVO codDesc;
			codDesc = new ComboCodDescVO("", "");
			lista.add(codDesc);
			codDesc = new ComboCodDescVO("E", "EDITORE COMMERCIALE");
			lista.add(codDesc);
			codDesc = new ComboCodDescVO("G", "EDITORE NON COMMERCIALE");
			lista.add(codDesc);
			ricFornitori.setListaTipoForn(lista);
		} else {
			CaricamentoCombo carCombo = new CaricamentoCombo();
			ricFornitori.setListaTipoForn(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_PARTNER)));
		}

//    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//    	ricFornitori.setListaTipoForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoPartner()));
	}

	private void loadProvincia(FornitoriRicercaParzialeForm ricFornitori) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricFornitori.setListaProvinciaForn(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PROVINCE)));
//    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//    	ricFornitori.setListaProvinciaForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceProvince()));

	}

	private void loadRegione(FornitoriRicercaParzialeForm ricFornitori) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricFornitori.setListaRegioneForn(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_REGIONE)));
	}


	private void loadPaese(FornitoriRicercaParzialeForm ricFornitori) throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
		ricFornitori.setListaPaeseForn(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PAESE)));
//    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//    	ricFornitori.setListaPaeseForn(carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese()));
	}


	public ActionForward profiloCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		FornitoriRicercaParzialeForm ricFornitori = (FornitoriRicercaParzialeForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppProfiloVO")==null
			request.getSession().removeAttribute("attributeListaSuppProfiloVO");
			request.getSession().removeAttribute("profiliSelected");
			request.getSession().removeAttribute("criteriRicercaProfilo");

			this.impostaProfiloCerca( ricFornitori, request,mapping);
			return mapping.findForward("profiloCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaProfiloCerca( FornitoriRicercaParzialeForm ricFornitori, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
		// carica i criteri di ricerca da passare alla esamina
		String codP="";
		String codB="";
		String biblio=Navigation.getInstance(request).getUtente().getCodBib();
		codB=biblio;
		codB=ricFornitori.getCodBibl();
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		codP=polo;
		StrutturaCombo prof=new StrutturaCombo(ricFornitori.getProfAcqForn(),ricFornitori.getProfAcqFornDes());
		StrutturaCombo sez=new StrutturaCombo("","");
		StrutturaCombo lin=new StrutturaCombo("","");
		StrutturaCombo pae=new StrutturaCombo("","");
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae,   chiama, ordina );
		request.getSession().setAttribute("attributeListaSuppProfiloVO", eleRicerca);

	}catch (Exception e) {	}
	}


	private List<FornitoreVO> getListaFornitoriVO(ListaSuppFornitoreVO criRicerca) throws Exception
	{
	List<FornitoreVO> listaFornitori;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaFornitori = factory.getGestioneAcquisizioni().getRicercaListaFornitori(criRicerca);
	// prova hibernate
	//listaFornitori = (List<FornitoreVO>) factory.getGestioneAcquisizioni().getRicercaListaFornitoriHib(criRicerca);
	//this.sinCambio.setListaCambi(listaCambi);
	return listaFornitori;
	}

	private void caricaAttributeListaSupp(FornitoriRicercaParzialeForm currentForm, ListaSuppFornitoreVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		if (ricArr!=null && ricArr.getLocale().equals("1"))
		{
			currentForm.setRicercaLocaleStr("on");
		}
		else
		{
			currentForm.setRicercaLocaleStr("");
		}
		currentForm.setCodBibl(ricArr.getCodBibl());
		currentForm.setNomeForn(ricArr.getNomeFornitore());
		currentForm.setTipoForn(ricArr.getTipoPartner());
		currentForm.setProvinciaForn(ricArr.getProvincia());
		currentForm.setPaeseForn(ricArr.getPaese());
		currentForm.setProfAcqForn(ricArr.getCodProfiloAcq());
		currentForm.setCodForn(ricArr.getCodFornitore());
	}catch (Exception e) {	}
	}

	private void caricaParametroTest( HttpServletRequest request, ActionMapping mapping)
	{
	//simulazione di una lista di supporto richiamata da cambiricercaparziale con codvaluta=eur
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String codP="";
		String codB=Navigation.getInstance(request).getUtente().getCodBib();
		String codForn="1122";
		String nomeForn="";
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="1";
		String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama,loc);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}

	private void loadTipoOrdinamento(FornitoriRicercaParzialeForm ricFornitori) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("1","Tipo - nome");
		lista.add(elem);
		elem = new StrutturaCombo("3","Nome fornitore");
		lista.add(elem);
		elem = new StrutturaCombo("4","Unità org. - nome");
		lista.add(elem);
		// Modifiche Maggio 2013 Da mail Contardi/Scognamiglio contenente manuale di Interrogazione Produzione editoriale
		// nella lista il campo indirizzo (sembra inutile)
		if (ricFornitori.getEditore() == null || ricFornitori.getEditore().equals("NO")) {
			elem = new StrutturaCombo("5","Indirizzo");
			lista.add(elem);
		}

		ricFornitori.setListaTipiOrdinam(lista);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione
		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_FORNITORI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		return false;	}
}
