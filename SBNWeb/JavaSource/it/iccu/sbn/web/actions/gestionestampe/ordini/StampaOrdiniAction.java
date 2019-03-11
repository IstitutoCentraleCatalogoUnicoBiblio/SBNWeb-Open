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
package it.iccu.sbn.web.actions.gestionestampe.ordini;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.StampaShippingManifestVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaOrdiniDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.custom.acquisizioni.ordini.OrdineCarrelloSpedizioneDecorator;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.validators.acquisizioni.StampaShippingManifestValidator;
import it.iccu.sbn.web.actionforms.gestionestampe.ordini.StampaOrdiniForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StampaOrdiniAction extends AcquisizioniBaseAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		map.put("ricerca.button.calcoli", "calcoli");
		map.put("ordine.label.fornitore","fornitoreCerca");

		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		StampaOrdiniForm currentForm = (StampaOrdiniForm) form;

		UserVO utente = navi.getUtente();
		currentForm.setCodBibl(utente.getCodBib());
		currentForm.setCodPolo(utente.getCodPolo());
		currentForm.setDescrBib(utente.getBiblioteca());


		if (!currentForm.isInitialized()) {
			StampaVo parametri = null;
			String codAttivita = request.getParameter(Constants.CODICE_ATTIVITA);
			if (ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().GA_STAMPA_SHIPPING_MANIFEST)) {
				StampaShippingManifestVO ssm = new StampaShippingManifestVO();
				OrdineCarrelloSpedizioneVO ocs = new OrdineCarrelloSpedizioneVO();
				ssm.setOrdineCarrelloSpedizione(new OrdineCarrelloSpedizioneDecorator(ocs) );
				parametri = ssm;
			} else
				parametri = new StampaOrdiniDiffVO();

			parametri.setCodAttivita(codAttivita);
			currentForm.setParametri(parametri);
			currentForm.setInitialized(true);

		}

		//Settaggio biblioteche
/*		if(request.getAttribute("codBib") != null ) {
			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			stampaBollettarioForm.setCodBib((String)request.getAttribute("codBib"));
		} */
		BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBib");
		if (bibScelta!=null && bibScelta.getCod_bib()!=null && currentForm.getCodBibl()!=null )
		{
			currentForm.setCodBibl(bibScelta.getCod_bib());
			currentForm.setDescrBib(bibScelta.getNom_biblioteca());
		}
		currentForm.setTipoRicerca("1");
		List lista = new ArrayList();
		lista = this.loadTipiOrdinamento();
		currentForm.setTipoRicerca("stampa");
		currentForm.setFileRisultato("unico");
		currentForm.setListaTipiOrdinamento(lista);
/*		stampaBollettarioForm.setCodForn("");;
		stampaBollettarioForm.setNomForn("");
		stampaBollettarioForm.setTpForn("");
		stampaBollettarioForm.setPaese("");
		stampaBollettarioForm.setProv("");
		stampaBollettarioForm.setProfAcq("");
*/
//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		//ricOrdini.setElencoModelli(createFakeDataSource());
		currentForm.setElencoModelli(getElencoModelli());
		currentForm.setTipoFormato(TipoStampa.PDF.name());

//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
		}
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

		return mapping.getInputForward();
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaOrdiniForm currentForm = (StampaOrdiniForm) form;
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

			this.impostaFornitoreCerca( currentForm, request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( StampaOrdiniForm ricOrdini, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=ricOrdini.getCodBibl();
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

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaOrdiniForm currentForm = (StampaOrdiniForm) form;
		try {
//			return mapping.findForward("indietro");//rosa
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private List loadTipiOrdinamento() throws Exception {
		List lista = new ArrayList();
		CodiceVO rec = new CodiceVO("1","Data ascendente");
		lista.add(rec);
		rec = new CodiceVO("2","Data discendente");
		lista.add(rec);
/*		rec = new CodiceVO("FA","Fornitore  Ascendente");
		lista.add(rec);
		rec = new CodiceVO("FD","Fornitore  Discendente");
		lista.add(rec);
*/		return lista;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaOrdiniForm currentForm = (StampaOrdiniForm) form;
		try {
	        FactoryEJBDelegate factory =FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_STAMPA_ORDINE, 10, "codBib");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward calcoli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaOrdiniForm currentForm = (StampaOrdiniForm) form;
		try {
// 26.07.10 disabilitazione bottone nascosto
/*                AdeguamentoCalcoliVO adegua = new AdeguamentoCalcoliVO();
                adegua.setCodPolo(ricOrdini.getCodPolo());
                adegua.setCodBib(ricOrdini.getCodBibl());

                String basePath=this.servlet.getServletContext().getRealPath(File.separator);
				adegua.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				log.info("download path: " + downloadPath);
				String downloadLinkPath = "/";
				//adegua.setDatiInput(listaIDOrdini);
				adegua.setDownloadPath(downloadPath);
				adegua.setDownloadLinkPath(downloadLinkPath);
				adegua.setTicket(Navigation.getInstance(request).getUserTicket());
				adegua.setUser(Navigation.getInstance(request).getUtente().getFirmaUtente());

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

                //String s = factory.getElaborazioniDifferite().adeguaCalcoli(adegua);

				// nuova gestione  batch

				// new gestione batch inizio
				AdeguamentoCalcoliDiffVO richiesta = new AdeguamentoCalcoliDiffVO();
				richiesta.setCodPolo(adegua.getCodPolo());
				richiesta.setCodBib(adegua.getCodBib());
				richiesta.setUser(Navigation.getInstance(request).getUtente().getUserId());
				richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BILANCIO);
				//richiesta.setParametri(inputForStampeService);
				//richiesta.setTipoOrdinamento(ordinamFile);
				richiesta.setAdeguamentoCalcoli(adegua);
				String ticket=Navigation.getInstance(request).getUserTicket();

				//AmministrazionePolo  anministrazionePolo;

				String s =  null;
				try {
					s = getEjb().prenotaElaborazioneDifferita((ParametriRichiestaElaborazioneDifferitaVO) richiesta, null);
				} catch (ApplicationException e) {
					if (e.getErrorCode().getErrorMessage().equals("USER_NOT_AUTHORIZED"))
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (s == null) {
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotBatchKO"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}

				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("error.acquisizioni.prenotazioneBatchOK", s.toString()));
				this.saveErrors(request, errors);
				resetToken(request);
*/
				return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaOrdiniForm currentForm = (StampaOrdiniForm) form;

		String codAttivita = currentForm.getParametri().getCodAttivita();
		if (ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().GA_STAMPA_SHIPPING_MANIFEST))
			return prenota(mapping, currentForm, request, response, codAttivita);

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utenteAbil = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}


		try {
//			// validazione

			//boolean criteriMinimiEsistenza=false;

			if (currentForm.getCodBibl()!=null && currentForm.getCodBibl().length()!=0)
			{
				if (currentForm.getCodBibl().length()>3)
				{
					throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
				}
			}

			if (currentForm.getDataOrdineDa()!=null && currentForm.getDataOrdineDa().trim().length()!=0)
			{
				//criteriMinimiEsistenza=true;

				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(currentForm.getDataOrdineDa().trim()) && currentForm.getDataOrdineDa().trim().length()==4)
				{
					String strAnnata=currentForm.getDataOrdineDa().trim();
					currentForm.setDataOrdineDa("01/01/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(currentForm.getDataOrdineDa().trim())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}
			}
			if (currentForm.getDataOrdineA()!=null && currentForm.getDataOrdineA().trim().length()!=0)
			{
				//criteriMinimiEsistenza=true;

				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(currentForm.getDataOrdineA().trim()) && currentForm.getDataOrdineA().trim().length()==4)
				{
					String strAnnata=currentForm.getDataOrdineA().trim();
					currentForm.setDataOrdineA("31/12/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(currentForm.getDataOrdineA().trim())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}
			if (currentForm.getCodFornitore()!=null &&   currentForm.getCodFornitore().trim().length()!=0)
			{
				if (!strIsNumeric(currentForm.getCodFornitore()) && currentForm.getCodFornitore().trim().length()!=0)
				{
					throw new ValidationException("ordinierroreCampoCodFornitoreNumerico",
						ValidationExceptionCodici.ordinierroreCampoCodFornitoreNumerico);
				}
			}
/*			if (ricOrdini.getTipoArr()!= null && ricOrdini.getTipoArr().length>0)
			{
				criteriMinimiEsistenza=true;
			}*/

/*			if (!criteriMinimiEsistenza)
			{
				throw new ValidationException("ricercaDaRaffinare",
						ValidationExceptionCodici.ricercaDaRaffinare);
			}
*/
			ListaSuppOrdiniVO stampaOrdiniVO = new ListaSuppOrdiniVO();

			if (currentForm.getCodBibl() != null)
				stampaOrdiniVO.setCodBibl(currentForm.getCodBibl());
			if (currentForm.getDataOrdineDa() != null)
				stampaOrdiniVO.setDataOrdineDa(currentForm.getDataOrdineDa().trim());
			if (currentForm.getDataOrdineA() != null)
				stampaOrdiniVO.setDataOrdineA(currentForm.getDataOrdineA().trim());

			if (currentForm.getTipoArr()!= null && currentForm.getTipoArr().length>0)
			{
				stampaOrdiniVO.setTipoOrdineArr(currentForm.getTipoArr());
			}
			// vanno esclusi dalla stampa gli ordini annullati
			// Tck 2559 (20/04/09): un ordine chiuso deve essere escluso dalla stampa
			//tck 2559  Si stampano solo quelli aperti, se si tratta di un ordine di acquisto; invece la lettera di ringraziamento per i doni vale sia per gli ordini aperti che per quelli chiusi.

			String[] statoOrdArr;
			//String[] statoOrdArr=new String[2];
			//String[] statoOrdArr=new String[1];
			//statoOrdArr[0]="A";
			// 08.09.2010 solo la ristampa può operare sugli ordini chiusi
			if (currentForm.getTipoRicerca()!= null && currentForm.getTipoRicerca().trim().equals("ristampa"))
			{
				statoOrdArr=new String[2];
				statoOrdArr[0]="A";
				statoOrdArr[1]="C";
			}
			else
			{
				statoOrdArr=new String[1];
				statoOrdArr[0]="A";
			}

			stampaOrdiniVO.setStatoOrdineArr(statoOrdArr);

			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
			String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
			String denoBibl = Navigation.getInstance(request).getUtente().getBiblioteca();//this.calcolaCodBib(request);

			if (currentForm.getCodBibl()!=null && currentForm.getCodBibl().length()>0 )
			{
				bibl= currentForm.getCodBibl();
			}
			if (currentForm.getCodPolo()!=null && currentForm.getCodPolo().length()>0 )
			{
				polo= currentForm.getCodPolo();
			}
			if (currentForm.getDescrBib()!=null && currentForm.getDescrBib().length()>0 )
			{
				denoBibl= currentForm.getDescrBib();
			}

			stampaOrdiniVO.setCodPolo(polo);
			stampaOrdiniVO.setCodBibl(bibl);
			stampaOrdiniVO.setDenoBiblStampe(denoBibl);
			if (currentForm.getCodFornitore()!=null &&   currentForm.getCodFornitore().trim().length()!=0)
			{
				stampaOrdiniVO.setFornitore(new StrutturaCombo("",""));
				stampaOrdiniVO.getFornitore().setCodice(currentForm.getCodFornitore().trim());
			}

			// ATTIVATO DA RICERCA per la gestione dei boolean stampato e rinnovato
			stampaOrdiniVO.setAttivatoDaRicerca(true);

			// attenzione via batch non vanno esclusi gli stampati ma a seconda del check si deve provvedere alla ristampa
			if (currentForm.getTipoRicerca()!= null && currentForm.getTipoRicerca().trim().equals("stampa"))
			{
				stampaOrdiniVO.setStampatoStr("00");
				stampaOrdiniVO.setStampato(false);
				//stampaOrdiniVO.setOrdinamento("9"); // ordinamento per cod forn e tipo ordi, cod ord
			}
			if (currentForm.getTipoRicerca()!= null && currentForm.getTipoRicerca().trim().equals("ristampa"))
			{
				stampaOrdiniVO.setStampatoStr("01");
				stampaOrdiniVO.setStampato(true);
				//stampaOrdiniVO.setOrdinamento("9"); // ordinamento per cod forn e tipo ordi, , cod ord  e data ord

				// obbligatorietà delle date in caso di ristampa
				if ((currentForm.getDataOrdineA()==null || (currentForm.getDataOrdineA()!=null && currentForm.getDataOrdineA().trim().length()==0)) &&  (currentForm.getDataOrdineDa()==null || (currentForm.getDataOrdineDa()!=null &&  currentForm.getDataOrdineDa().trim().length()==0)))
				{
					throw new ValidationException("indicaData",
							ValidationExceptionCodici.indicaData);
				}
			}
			if (currentForm.getFileRisultato()!= null && currentForm.getFileRisultato().trim().equals("unico"))
			{
				stampaOrdiniVO.setStampaFiledistinti(false);
			}
			if (currentForm.getFileRisultato()!= null && currentForm.getFileRisultato().trim().equals("distinti"))
			{
				stampaOrdiniVO.setStampaFiledistinti(true);
			}

			String ordinamFile = currentForm.getTipoOrdinamSelez();
			if (ordinamFile!=null && ordinamFile.trim().equals("1"))
			{
				stampaOrdiniVO.setOrdinamento("9");
			}
			if (ordinamFile!=null && ordinamFile.trim().equals("2"))
			{
				stampaOrdiniVO.setOrdinamento("10");
			}
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


/*			List<OrdiniVO> risultato=null;
			int numRis=0;
			try {
				//risultato=factory.getGestioneAcquisizioni().getRicercaListaOrdini(stampaOrdiniVO);
				numRis=factory.getGestioneAcquisizioni().ValidaPreRicercaOrdini(stampaOrdiniVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//if (risultato==null)
			if (numRis==0)
			{
				//assenza risultati
				throw new ValidationException("assenzaRisultati",
						ValidationExceptionCodici.assenzaRisultati);

			}
*/
			// lettura configurazione
			// lancio metodo di aggregazione di buoni di ordine x STAMPE CHE RICEVE IN
			// INPUT : CONFIGURAZIONE E LA LISTA DI ORDINI TROVATI o LISTA BUONI ORDINE TROVATI, TIPO OGGETTI=B-buoni, O-ordini
			// OUTPUT: ARRAY LIST DI BUONI ORDINE X STAMPE CON TUTTO ESPLICITO
			// in base a lingua, tipo ordine ecc..base compila tutti campi che devono andare in stampa
			// in base a tipo

/*			ConfigurazioneBOVO confBO=new ConfigurazioneBOVO();
			confBO.setCodBibl(ricOrdini.getCodBibl());
			confBO.setCodPolo(ricOrdini.getCodPolo());
			ConfigurazioneBOVO configurazione=this.loadConfigurazione(confBO);

			List<StampaBuoniVO> risultatoPerStampa=impostaBuonoOrdineDaStampare(configurazione, risultato,"ORD", ricOrdini,  request);
*/
			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();

			List inputForStampeService=new ArrayList();
			inputForStampeService.add(stampaOrdiniVO);

			String tipoFormato=currentForm.getTipoFormato();

			request.setAttribute("DatiVo", inputForStampeService);//  ListaBuoniOrdine
			request.setAttribute("TipoFormato", tipoFormato);

			String fileJrxml = currentForm.getTipoModello()+".jrxml"; // corretto da rox aggiunto .jrxml 04.12.08
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);

//				 percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
			//NB: Se voglio memorizzare sul server
//				String pathDownload = basePath+File.separator+"download";
			String pathDownload = StampeUtil.getBatchFilesPath();

			//Se voglio memorizzare in locale

//				String percRel=request.getServletPath();
//				StringBuffer percUrl=request.getRequestURL();
//				String percorsoOK = percUrl.toString();
//				int indice = percorsoOK.indexOf(percRel);
//				pathDownload = percorsoOK.substring(0, indice);

			//codice standard inserimento messaggio di richiesta stampa differita
			StampaDiffVO stam = new StampaDiffVO();
			stam.setTipoStampa(tipoFormato);
			stam.setUser(utente);
			stam.setCodPolo(polo);
			stam.setCodBib(bibl);

			stam.setTipoOrdinamento(ordinamFile);
			stam.setParametri(inputForStampeService);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setTipoOperazione("STAMPA_BUONI_ORDINE");
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

/*			String idMessaggio = factory.getStampeOnline().stampaBuoniOrdine(stam);

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
			this.saveErrors(request, errors);
*/
			// nuova gestione  batch

			StampaOrdiniDiffVO richiesta = new StampaOrdiniDiffVO();
			richiesta.setCodPolo(polo);
			richiesta.setCodBib(bibl);
			richiesta.setUser(utente);
			richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE);
			richiesta.setParametri(inputForStampeService);
			richiesta.setTipoOrdinamento(ordinamFile);
			richiesta.setStampavo(stam);
			String ticket=Navigation.getInstance(request).getUserTicket();

			String idBatch =  null;
			try {
				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ticket, richiesta, null);
			} catch (ApplicationException e) {
				if (e.getErrorCode() == SbnErrorTypes.USER_NOT_AUTHORIZED) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("messaggio.info.noautOP"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			if (idBatch == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("errors.acquisizioni.prenotFallita"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.acquisizioni.prenotOk", idBatch.toString()));
			this.saveErrors(request, errors);



//				return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		}catch (Exception e){
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
	}
		return mapping.getInputForward();

	}

	private ActionForward prenota(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String codAttivita) throws Exception {

		StampaOrdiniForm currentForm = (StampaOrdiniForm) form;
		StampaVo parametri = currentForm.getParametri();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		try {
			UserVO utente = Navigation.getInstance(request).getUtente();
			parametri.setTicket(utente.getTicket());
			parametri.setCodPolo(utente.getCodPolo());
			parametri.setCodBib(utente.getCodBib());
			parametri.setUser(utente.getUserId());
			parametri.validate();

			Validator<? extends ParametriRichiestaElaborazioneDifferitaVO> validator = null;
			if (ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().GA_STAMPA_SHIPPING_MANIFEST))
				validator = new StampaShippingManifestValidator();

			/*
			List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(parametri.getCodAttivita());
			if (!ValidazioneDati.isFilled(modelli)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.stampe.modelli.conf"));
				resetToken(request);
				return mapping.getInputForward();
			}

			String jrxml = ValidazioneDati.first(modelli).getJrxml();

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml = basePath + File.separator + "jrxml" + File.separator + jrxml;
			parametri.setTemplate(pathJrxml);
			parametri.setTipoStampa(currentForm.getTipoFormato());
			*/
			String idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(),
					parametri, null, validator);
			if (idBatch == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
				resetToken(request);
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok" , idBatch));
		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
	}

	public int validaDataPassata(String data) {
		int DATA_OK=0;
		int DATA_ERRATA=1;
		int DATA_MAGGIORE=2;
		int DATA_PASSATA_ERRATA=3;

		int codRitorno=-1;
		if (data==null) {
			codRitorno=DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			//DateParser.parseDate(data);
			// l'istruzione sottostante va in errore se non non riesce a fare il parsing del rispetto del formato
			java.util.Date date = format.parse(data);
			if (java.util.regex.Pattern.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", data)) {
				Date oggi = new Date(System.currentTimeMillis());
				//if (date.after(oggi)) {
				//	codRitorno=DATA_MAGGIORE;
                //    throw new Exception(); // data > data odierna
				//}
				codRitorno=DATA_OK;
				return codRitorno; // tutto OK
			} else {
                codRitorno = DATA_ERRATA;
				throw new Exception(); // formato data errato
			}
		} catch (Exception e) {
			return codRitorno;
		}
	}

	public  boolean strIsNumeric(String data) {
		return (Pattern.matches("[0-9&&[^a-z]]+", data));
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

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("STAMPA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				// 04.12.08 e.printStackTrace();
				log.error(e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
		return false;
	}


}
