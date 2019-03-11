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
package it.iccu.sbn.web.actions.gestionestampe.spese;


import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSpeseVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RipartizioneSpeseDiffVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.sif.AttivazioneSIFListaClassiCollegateVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.spese.RipartizioneSpeseForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class RipartizioneSpeseAction extends ReportAction implements SbnAttivitaChecker {


	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("gestionestampe.lsBib", "listaSupportoBib");
//		map.put("button.stampa","stampa");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.label.sezione","sezioneCerca");
		map.put("ordine.label.bilancio","bilancioCerca");
		map.put("button.classific","listaSupportoClassi");

		return map;
	}



	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
		RipartizioneSpeseForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
		RipartizioneSpeseForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
		RipartizioneSpeseForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());

		//Settaggio biblioteche
/*		if(request.getAttribute("codBib") != null ) {
			// provengo dalla lista biblioteche
			// carico la lista relativa al codice selezionato
			RipartizioneSpeseForm.setCodBib((String)request.getAttribute("codBib"));
		}
*/		BibliotecaVO bibScelta=(BibliotecaVO) request.getAttribute("codBib");
		if (bibScelta!=null && bibScelta.getCod_bib()!=null && RipartizioneSpeseForm.getCodBib()!=null )
		{
			RipartizioneSpeseForm.setCodBib(bibScelta.getCod_bib());
			RipartizioneSpeseForm.setDescrBib(bibScelta.getNom_biblioteca());
		}
		this.loadTipoOrdine(RipartizioneSpeseForm);
		List lista = new ArrayList();
		lista = this.loadTipiOrdinamento();
		RipartizioneSpeseForm.setListaTipiOrdinamento(lista);
/*		RipartizioneSpeseForm.setCodForn("");;
		RipartizioneSpeseForm.setNomForn("");
		RipartizioneSpeseForm.setTpForn("");
		RipartizioneSpeseForm.setPaese("");
		RipartizioneSpeseForm.setProv("");
		RipartizioneSpeseForm.setProfAcq("");
*///		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		RipartizioneSpeseForm.setElencoModelli(getElencoModelli());
		RipartizioneSpeseForm.setTipoFormato(TipoStampa.HTML.name());

		this.loadNatura( RipartizioneSpeseForm);
		this.loadTipoImpegno( RipartizioneSpeseForm);
		this.loadTipoMaterialeInventariale(RipartizioneSpeseForm);
		this.loadSupporto(RipartizioneSpeseForm);
		this.loadTipiRecordTitoli(RipartizioneSpeseForm);
		this.loadRangeDewey(RipartizioneSpeseForm);

		List arrListaPaesi=this.loadPaesi();
		RipartizioneSpeseForm.setListaPaesi(arrListaPaesi);

		List arrListaLingue=this.loadLingue();
		RipartizioneSpeseForm.setListaLingue(arrListaLingue);


		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utente = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utente.getCodPolo(), utente.getCodBib(), null);

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
					RipartizioneSpeseForm.setCodFornitore(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
					RipartizioneSpeseForm.setFornitore(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
				}
			}
			else
			{
				// pulizia della maschera di ricerca
				RipartizioneSpeseForm.setCodFornitore("");
				RipartizioneSpeseForm.setFornitore("");
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
					RipartizioneSpeseForm.setEsercizio(ricBil.getSelezioniChiamato().get(0).getEsercizio());
					RipartizioneSpeseForm.setCapitolo(ricBil.getSelezioniChiamato().get(0).getCapitolo());
					//ricOrdini.setTipoImpegno(ricBil.getSelezioniChiamato().get(0).getImpegno());
					RipartizioneSpeseForm.setTipoImpegno(ricBil.getSelezioniChiamato().get(0).getSelezioneImp());
					//RipartizioneSpeseForm.setRientroDaSif(true);
				}
			}
			else
			{
				// pulizia della maschera di ricerca
				RipartizioneSpeseForm.setEsercizio("");
				RipartizioneSpeseForm.setCapitolo("");
				RipartizioneSpeseForm.setTipoImpegno("");
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

					String testoListaSezioni="";
					String idListaSezioni="";
					RipartizioneSpeseForm.setSezione("");
					if (ricSez.getSelezioniChiamato().size()>1)
					{
						for (int m=0; m<ricSez.getSelezioniChiamato().size(); m++)
						{
							if (m==0)
							{
								idListaSezioni=idListaSezioni +"'";
							}
							testoListaSezioni=testoListaSezioni + ricSez.getSelezioniChiamato().get(m).getCodiceSezione(); // getDescrizioneSezione
							idListaSezioni=idListaSezioni +  ricSez.getSelezioniChiamato().get(m).getCodiceSezione();
							if (m < ricSez.getSelezioniChiamato().size()-1)
							{
								testoListaSezioni=testoListaSezioni+ " - ";
								idListaSezioni=idListaSezioni +"','";
							}
							if (m==ricSez.getSelezioniChiamato().size()-1)
							{
								idListaSezioni=idListaSezioni +"'";
							}
						}

					}

					if (ricSez.getSelezioniChiamato().size()==1)
					{
						RipartizioneSpeseForm.setSezione(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
						testoListaSezioni="";
						idListaSezioni="";
					}

					RipartizioneSpeseForm.setTestoListaSezioni(testoListaSezioni);
					RipartizioneSpeseForm.setIdListaSezioni(idListaSezioni);
					//RipartizioneSpeseForm.setRientroDaSif(true);
				}
			}
			else
			{
				// pulizia della maschera di ricerca
				RipartizioneSpeseForm.setSezione("");
				RipartizioneSpeseForm.setTestoListaSezioni("");
				RipartizioneSpeseForm.setIdListaSezioni("");

			}

			// il reset dell'attributo di sessione deve avvenire solo dal chiamante
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			request.getSession().removeAttribute("sezioniSelected");
			request.getSession().removeAttribute("criteriRicercaSezione");

			}
/*
		Disegno dis=new Disegno();
		String args[]= new String[3];
		args[0]="2";
		args[1]="4";
		args[2]="6";


		dis.main(args);*/
		return mapping.getInputForward();
	}
	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
		try {
			request.setAttribute("parametroPassato", RipartizioneSpeseForm.getElemBlocco());
//			NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
//			return mapping.findForward("indietro");//rosa
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, 10, "codBib");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
		try {
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

			this.impostaFornitoreCerca( RipartizioneSpeseForm, request,mapping);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( RipartizioneSpeseForm RipartizioneSpeseForm, HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=RipartizioneSpeseForm.getCodBib();
		String codForn=RipartizioneSpeseForm.getCodFornitore();
		String nomeForn=RipartizioneSpeseForm.getFornitore();
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
		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
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
			this.impostaBilancioCerca( RipartizioneSpeseForm, request,mapping);
			return mapping.findForward("bilancioCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private void impostaBilancioCerca(RipartizioneSpeseForm RipartizioneSpeseForm,  HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=RipartizioneSpeseForm.getCodBib();
		String ese=RipartizioneSpeseForm.getEsercizio();
		String cap=RipartizioneSpeseForm.getCapitolo();
		String imp=RipartizioneSpeseForm.getTipoImpegno();
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppBilancioVO", eleRicerca);

	}catch (Exception e) {	}
	}

	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
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
			this.impostaSezioneCerca( RipartizioneSpeseForm, request,mapping);
			return mapping.findForward("sezioneCerca");
			//return mapping.findForward("sezioneLista");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaSezioneCerca(RipartizioneSpeseForm RipartizioneSpeseForm,  HttpServletRequest request, ActionMapping mapping)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=RipartizioneSpeseForm.getCodBib();
		String codSez=RipartizioneSpeseForm.getSezione();
		String desSez="";
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
	}catch (Exception e) {	}
	}


	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;

		Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		UserVO utenteAbil = Navigation.getInstance(request).getUtente();
		try {
			utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utenteAbil.getCodPolo(), utenteAbil.getCodBib(), null);

		}   catch (UtenteNotAuthorizedException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("messaggio.info.noaut"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();//return null;

		}

		try {
//
			// validazione


			if (RipartizioneSpeseForm.getCodBib()!=null && RipartizioneSpeseForm.getCodBib().length()!=0)
			{
				if (RipartizioneSpeseForm.getCodBib().length()>3)
				{
					throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
				}
			}

			if (RipartizioneSpeseForm.getDataOrdineDa()!=null && RipartizioneSpeseForm.getDataOrdineDa().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(RipartizioneSpeseForm.getDataOrdineDa()) && RipartizioneSpeseForm.getDataOrdineDa().length()==4)
				{
					String strAnnata=RipartizioneSpeseForm.getDataOrdineDa();
					RipartizioneSpeseForm.setDataOrdineDa("01/01/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(RipartizioneSpeseForm.getDataOrdineDa())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}
			}
			if (RipartizioneSpeseForm.getDataOrdineA()!=null && RipartizioneSpeseForm.getDataOrdineA().length()!=0)
			{
				// controllo che non sia presente l'indicazione del solo anno
				if (strIsNumeric(RipartizioneSpeseForm.getDataOrdineA()) && RipartizioneSpeseForm.getDataOrdineA().length()==4)
				{
					String strAnnata=RipartizioneSpeseForm.getDataOrdineA();
					RipartizioneSpeseForm.setDataOrdineA("31/12/" + strAnnata);
				}
				// controllo congruenza
				if (validaDataPassata(RipartizioneSpeseForm.getDataOrdineA())!=0)
				{
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}

			}

			if (RipartizioneSpeseForm.getAnnoOrdine()!=null && RipartizioneSpeseForm.getAnnoOrdine().length()!=0)
			{
				// controllo congruenza
				if (!strIsNumeric(RipartizioneSpeseForm.getAnnoOrdine().trim()))
				{
					throw new ValidationException("sezioneerroreCampoAnnoOrdineNumerico",
							ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineNumerico);
				}

				if (RipartizioneSpeseForm.getAnnoOrdine().trim().length()!=4)
				{
					throw new ValidationException("sezioneerroreCampoAnnoOrdineEccedente",
							ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineEccedente);
				}

			}
			if (RipartizioneSpeseForm.getTipoOrdine()!=null && RipartizioneSpeseForm.getTipoOrdine().length()!=0)
			{
				if (strIsNumeric(RipartizioneSpeseForm.getTipoOrdine()))
				{
					throw new ValidationException("ordinierroreCampoTipoOrdineAlfabetico",
							ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
				}
				if (RipartizioneSpeseForm.getTipoOrdine().length()!=1)
				{
					throw new ValidationException("ordinierroreCampoTipoOrdineEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
				}
			}
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// validazione di esistenza: ricerca fornitore
			String idForn="";
			List<FornitoreVO> listaForn=null;
			if (RipartizioneSpeseForm.getCodFornitore()!=null && RipartizioneSpeseForm.getCodFornitore().length()!=0)
			{
				ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
				// carica i criteri di ricerca da passare al metodo
				eleRicerca.setCodPolo(RipartizioneSpeseForm.getCodPolo());
				eleRicerca.setCodBibl(RipartizioneSpeseForm.getCodBib());
				eleRicerca.setCodFornitore(RipartizioneSpeseForm.getCodFornitore());
				String loc="0"; // ricerca sempre locale
				eleRicerca.setLocale(loc);

				listaForn=factory.getGestioneAcquisizioni().getRicercaListaFornitori(eleRicerca);
				if (listaForn!=null && listaForn.size()==1)
				{
					idForn=listaForn.get(0).getCodFornitore();
					RipartizioneSpeseForm.setCodFornitore(idForn);
					RipartizioneSpeseForm.setFornitore(listaForn.get(0).getNomeFornitore());
				}
				else
				{
					throw new ValidationException("ordineIncongruenzaFornitoreInesistente", ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
				}
			}
			//  validazione di esistenza:ricerca sezione
			int idSez=0;
			List<SezioneVO> listaSez=null;
			if (RipartizioneSpeseForm.getSezione()!=null && RipartizioneSpeseForm.getSezione().length()!=0)
			{
				ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
				// carica i criteri di ricerca da passare al metodo
				eleRicerca.setCodPolo(RipartizioneSpeseForm.getCodPolo());
				eleRicerca.setCodBibl(RipartizioneSpeseForm.getCodBib());
				eleRicerca.setCodiceSezione(RipartizioneSpeseForm.getSezione());

				listaSez=factory.getGestioneAcquisizioni().getRicercaListaSezioni(eleRicerca);
				if (listaSez!=null && listaSez.size()==1)
				{
					idSez=listaSez.get(0).getIdSezione();
				}
				else
				{
					throw new ValidationException("ordineIncongruenzaSezioneInesistente", ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
				}
				// obbligatorietà delle date o esercizio in caso di ristampa
				if ((RipartizioneSpeseForm.getSezione()==null || (RipartizioneSpeseForm.getSezione()!=null && RipartizioneSpeseForm.getSezione().length()==0)) && (RipartizioneSpeseForm.getDataOrdineA()==null || (RipartizioneSpeseForm.getDataOrdineA()!=null && RipartizioneSpeseForm.getDataOrdineA().trim().length()==0)) &&  (RipartizioneSpeseForm.getDataOrdineDa()==null || (RipartizioneSpeseForm.getDataOrdineDa()!=null &&  RipartizioneSpeseForm.getDataOrdineDa().trim().length()==0)) && (RipartizioneSpeseForm.getEsercizio()==null || (RipartizioneSpeseForm.getEsercizio()!=null && RipartizioneSpeseForm.getEsercizio().length()==0)) )
				{
					throw new ValidationException("indicaDataEsercizio", ValidationExceptionCodici.indicaDataEsercizio);
				}


			}
			// validazione di esistenza:ricerca bilancio e capitolo
			int idBil=0;
			String imp="";
			List<BilancioVO> listaBil=null;
			if ((RipartizioneSpeseForm.getEsercizio()!=null && RipartizioneSpeseForm.getEsercizio().length()!=0) || (RipartizioneSpeseForm.getCapitolo()!=null && RipartizioneSpeseForm.getCapitolo().length()!=0) ||  (RipartizioneSpeseForm.getTipoImpegno()!=null && RipartizioneSpeseForm.getTipoImpegno().length()!=0) )
			{
				ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
				// carica i criteri di ricerca da passare alla esamina
				eleRicerca.setCodPolo(RipartizioneSpeseForm.getCodPolo());
				eleRicerca.setCodBibl(RipartizioneSpeseForm.getCodBib());
				if  (RipartizioneSpeseForm.getEsercizio()!=null && RipartizioneSpeseForm.getEsercizio().length()!=0)
				{
					eleRicerca.setEsercizio(RipartizioneSpeseForm.getEsercizio());
				}
				if (RipartizioneSpeseForm.getCapitolo()!=null && RipartizioneSpeseForm.getCapitolo().length()!=0 )
				{
					eleRicerca.setCapitolo(RipartizioneSpeseForm.getCapitolo());
				}
				if (RipartizioneSpeseForm.getTipoImpegno()!=null && RipartizioneSpeseForm.getTipoImpegno().length()!=0 )
				{
					imp=RipartizioneSpeseForm.getTipoImpegno();
					eleRicerca.setImpegno(RipartizioneSpeseForm.getTipoImpegno());
				}

				// carica i criteri di ricerca da passare al metodo

				listaBil=factory.getGestioneAcquisizioni().getRicercaListaBilanci(eleRicerca);
				if (listaBil!=null && listaBil.size()==1)
				{
					idBil=listaBil.get(0).getIDBil();
				}
				else if (listaBil!=null && listaBil.size()>1)
				{
					idBil=0;
				}
				else
				{
					throw new ValidationException("Bilancio inesistente");
				}

			}

			boolean criteriMinimiEsistenza=false;

			ListaSuppSpeseVO stampaRipartoSpeseVO = new ListaSuppSpeseVO();
			// scelta modello

			//stampaRipartoSpeseVO.setTipoReport("1");
			//stampaRipartoSpeseVO.setTipoReport("2"); //
			if  (RipartizioneSpeseForm.getEsercizio()!=null && RipartizioneSpeseForm.getEsercizio().length()!=0)
			{
				criteriMinimiEsistenza=true;
				stampaRipartoSpeseVO.setEsercizio(RipartizioneSpeseForm.getEsercizio());
			}
			if (RipartizioneSpeseForm.getCapitolo()!=null && RipartizioneSpeseForm.getCapitolo().length()!=0 )
			{
				stampaRipartoSpeseVO.setCapitolo(RipartizioneSpeseForm.getCapitolo());
			}
			if (RipartizioneSpeseForm.getTipoImpegno()!=null && RipartizioneSpeseForm.getTipoImpegno().length()!=0 )
			{
				stampaRipartoSpeseVO.setImp(RipartizioneSpeseForm.getTipoImpegno());
			}
			if (idBil>0 )
			{
				stampaRipartoSpeseVO.setImp(imp);
				stampaRipartoSpeseVO.setIdBil(idBil);
				stampaRipartoSpeseVO.setEsercizio(RipartizioneSpeseForm.getEsercizio());
				stampaRipartoSpeseVO.setCapitolo(RipartizioneSpeseForm.getCapitolo());
			}

			if (idSez>0 )
			{
				stampaRipartoSpeseVO.setIdSez(idSez);
				stampaRipartoSpeseVO.setSezione(RipartizioneSpeseForm.getSezione());
			}
			if (RipartizioneSpeseForm.getIdListaSezioni()!=null && RipartizioneSpeseForm.getIdListaSezioni().length()!=0)
			{
				stampaRipartoSpeseVO.setIdListaSezioni(RipartizioneSpeseForm.getIdListaSezioni());
			}

			if (!idForn.equals("") )
			{
				stampaRipartoSpeseVO.setIdForn(Integer.parseInt(idForn));
				stampaRipartoSpeseVO.setCodFornitore(RipartizioneSpeseForm.getCodFornitore());
				stampaRipartoSpeseVO.setFornitore(RipartizioneSpeseForm.getFornitore());
			}
			if (RipartizioneSpeseForm.getNatura() != null && RipartizioneSpeseForm.getNatura().length()>0)
				stampaRipartoSpeseVO.setNaturaOrdine(RipartizioneSpeseForm.getNatura());

			if (RipartizioneSpeseForm.getTipoMaterialeInv() != null && RipartizioneSpeseForm.getTipoMaterialeInv().length()>0)
			{
				stampaRipartoSpeseVO.setTipoMaterialeInv(RipartizioneSpeseForm.getTipoMaterialeInv());
				this.cercaCodTipoMaterialeInventariale(RipartizioneSpeseForm.getTipoMaterialeInv(), RipartizioneSpeseForm);
				//stampaRipartoSpeseVO.setTipoMaterialeInvDescr(RipartizioneSpeseForm.getTipoMaterialeInvDescr());
			}

			if (RipartizioneSpeseForm.getSupporto() != null && RipartizioneSpeseForm.getSupporto().length()>0)
			{
				stampaRipartoSpeseVO.setSupporto(RipartizioneSpeseForm.getSupporto());
				//stampaRipartoSpeseVO.setSupportoDescr(RipartizioneSpeseForm.getSupportoDescr());
				this.cercaCodSupporto(RipartizioneSpeseForm.getSupporto(), RipartizioneSpeseForm);

			}
			if (RipartizioneSpeseForm.getLingue() != null && RipartizioneSpeseForm.getLingue().length()>0)
			{
				stampaRipartoSpeseVO.setLingua(RipartizioneSpeseForm.getLingue());
				this.cercaCodLingua(RipartizioneSpeseForm.getLingue(), RipartizioneSpeseForm);
			}

			if (RipartizioneSpeseForm.getPaesi() != null && RipartizioneSpeseForm.getLingue().length()>0)
			{
				stampaRipartoSpeseVO.setPaese(RipartizioneSpeseForm.getPaesi());
				this.cercaCodPaese(RipartizioneSpeseForm.getPaesi(), RipartizioneSpeseForm);
			}

			if (RipartizioneSpeseForm.getCodBib() != null)
				stampaRipartoSpeseVO.setCodBibl(RipartizioneSpeseForm.getCodBib());
			if (RipartizioneSpeseForm.getDataOrdineDa()!=null && RipartizioneSpeseForm.getDataOrdineDa().length()!=0)
			{
				stampaRipartoSpeseVO.setDataOrdineDa(RipartizioneSpeseForm.getDataOrdineDa());
				criteriMinimiEsistenza=true;

			}
			if (RipartizioneSpeseForm.getDataOrdineA()!=null && RipartizioneSpeseForm.getDataOrdineA().length()!=0)
			{
				stampaRipartoSpeseVO.setDataOrdineA(RipartizioneSpeseForm.getDataOrdineA());
				criteriMinimiEsistenza=true;

			}

			if (RipartizioneSpeseForm.getAnnoOrdine()!=null && RipartizioneSpeseForm.getAnnoOrdine().length()!=0)
			{
				stampaRipartoSpeseVO.setAnno(RipartizioneSpeseForm.getAnnoOrdine());
				criteriMinimiEsistenza=true;
			}

			if (!criteriMinimiEsistenza)
			{
				throw new ValidationException("ricercaDaRaffinareTemp",
						ValidationExceptionCodici.ricercaDaRaffinareTemp);
			}

			if (RipartizioneSpeseForm.getTipoOrdine() != null)
				stampaRipartoSpeseVO.setTipoOrdine(RipartizioneSpeseForm.getTipoOrdine());

			String polo = Navigation.getInstance(request).getUtente().getCodPolo();
			String bibl = Navigation.getInstance(request).getUtente().getCodBib();//this.calcolaCodBib(request);
			String denoBibl = Navigation.getInstance(request).getUtente().getBiblioteca();//this.calcolaCodBib(request);

			stampaRipartoSpeseVO.setCodPolo(polo);
			stampaRipartoSpeseVO.setCodBibl(bibl);
			stampaRipartoSpeseVO.setDenoBibl(denoBibl);


			if (RipartizioneSpeseForm.getCodBib()!=null && RipartizioneSpeseForm.getCodBib().length()>0)
			{
				stampaRipartoSpeseVO.setCodBibl(RipartizioneSpeseForm.getCodBib());
				if (RipartizioneSpeseForm.getDescrBib()!=null && RipartizioneSpeseForm.getDescrBib().trim().length()>0 )
				{
					stampaRipartoSpeseVO.setDenoBibl(RipartizioneSpeseForm.getDescrBib());
				}
				else
				{
					stampaRipartoSpeseVO.setDenoBibl("");
				}
			}


			String ordinamFile = RipartizioneSpeseForm.getTipoOrdinamSelez();
			stampaRipartoSpeseVO.setOrdinamento(ordinamFile);


			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			//List<RigheVO> risultato=factory.getGestioneAcquisizioni().ripartoSpese(stampaRipartoSpeseVO);

			// scelta modello di stampa
			stampaRipartoSpeseVO.setTipoReport("1");
			if (RipartizioneSpeseForm.getTipoModello().equals("default_riparto_spese1"))
			{
				stampaRipartoSpeseVO.setTipoReport("1");
			}
			if (RipartizioneSpeseForm.getTipoModello().equals("default_riparto_spese2"))
			{
				stampaRipartoSpeseVO.setTipoReport("2");
			}
			if (RipartizioneSpeseForm.getTipoModello().equals("default_riparto_spese3"))
			{
				stampaRipartoSpeseVO.setTipoReport("3");
			}
			if (RipartizioneSpeseForm.getTipoModello().equals("default_riparto_spese4"))
			{
				stampaRipartoSpeseVO.setTipoReport("4");
			}

			if (RipartizioneSpeseForm.getRangeDewey()!=null && !RipartizioneSpeseForm.getRangeDewey().equals(""))
			{
				stampaRipartoSpeseVO.setRangeDewey(RipartizioneSpeseForm.getRangeDewey());
			}

			stampaRipartoSpeseVO.setOrdNOinv(false);
			if (RipartizioneSpeseForm.getOrdiniNOinv().equals("1"))
			{
				stampaRipartoSpeseVO.setOrdNOinv(true);
			}



			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();

			List inputForStampeService=new ArrayList();
			inputForStampeService.add(stampaRipartoSpeseVO);

			String tipoFormato=RipartizioneSpeseForm.getTipoFormato();

			request.setAttribute("DatiVo", inputForStampeService);//  ListaBuoniOrdine
			request.setAttribute("TipoFormato", tipoFormato);

			String fileJrxml = RipartizioneSpeseForm.getTipoModello()+".jrxml";
			//if (stampaRipartoSpeseVO.getTipoReport().equals("2"))
			//{
			//	fileJrxml = "default_riparto_spese2"+".jrxml";
			//}


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
			stam.setTipoOperazione("STAMPA_SPESE");
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

/*			String idMessaggio = factory.getStampeOnline().stampaSpese(stam);

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));

			this.saveErrors(request, errors);
*/
			// nuova gestione  batch

			//FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			RipartizioneSpeseDiffVO richiesta = new RipartizioneSpeseDiffVO();
			richiesta.setCodPolo(polo);
			richiesta.setCodBib(bibl);
			richiesta.setUser(utente);
			richiesta.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE);
			richiesta.setParametri(inputForStampeService);
			richiesta.setTipoOrdinamento(ordinamFile);
			richiesta.setStampavo(stam);
			String ticket=Navigation.getInstance(request).getUserTicket();

			String s =  null;
			try {
				s = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ticket, richiesta, null);
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
				errors.add("Attenzione", new ActionMessage("errors.acquisizioni.prenotFallita"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("errors.acquisizioni.prenotOk", s.toString()));
			this.saveErrors(request, errors);



//				return mapping.getInputForward();
		}	catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}	catch (Exception e){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
	}
		return mapping.getInputForward();//return null;
	}

//	private void stampa(String fileJrxml,String tipoFormato,HttpServletResponse response, DatiFornitoreVO datiF) throws Exception
//	{
//		List inputForStampeService=new ArrayList();
//
//		for (int k = 1; k<4; k++){
//			FornitoreVO	vo	 = new FornitoreVO ("", "", "codFornitore", "nomeFornitore", "unitaOrg", "indirizzo", "cap", "citta", "casellaPostale", "telefono", "fax", "", "partitaIva", "codiceFiscale", "email", " paese", "tipoPForn", "provForn",  "tipoVar", datiF, "biblFornitore");
//			inputForStampeService.add(vo);
//		}
//
//		String basePath=this.servlet.getServletContext().getRealPath(File.separator);
//
////		percorso dei file template: webroot/jrxml/
////		percorso dei file jasper compilati: webroot/jasper/
////		String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
////		percorso dei file template: webroot/jrxml/
//		String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
//
//		SbnStampaFornitori sbn = new SbnStampaFornitori(pathJrxml);
//		sbn.setFormato(TipoStampa.toTipoStampa(tipoFormato));
//		byte [] streamByte = sbn.stampa(inputForStampeService, pathJrxml);
//
//		ServletOutputStream servletOutputStream =response.getOutputStream();
//		response = getContentTypeResponse(TipoStampa.toTipoStampa(tipoFormato), response);
//		servletOutputStream.write(streamByte);
//		servletOutputStream.flush();
//		servletOutputStream.close();
//	}


	public HttpServletResponse getContentTypeResponse(TipoStampa tipoFormato, HttpServletResponse response){
		String tipo = "";
		switch (tipoFormato) {
		case PDF:
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=prova.pdf");

			break;
	  case RTF:
			response.setContentType("application/rtf");
			response.setHeader("Content-disposition", "inline; filename=prova.rtf");

	        break;
	  case XLS:
			response.setContentType("application/msexcel");
			response.setHeader("Content-disposition", "inline; filename=prova.xls");

			break;
	  case HTML:
			response.setContentType("text/html");

			 break;
	  case CSV:
			response.setContentType("text/csv");
			response.setHeader("Content-disposition", "inline; filename=prova.csv");

	        break;
	  default:
			response.setContentType("application/pdf");
	}
		return response;
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE);
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
		rec = new CodiceVO("3","Fornitore (solo per ripartizione per ordini)");
		lista.add(rec);
		rec = new CodiceVO("4","Bid (solo per ripartizione per ordini)");
		lista.add(rec);
		rec = new CodiceVO("5","Bilancio (solo per ripartizione per ordini)");
		lista.add(rec);

		return lista;
	}

	public ActionForward listaSupportoClassi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
		try {
			ClassiDelegate classi = ClassiDelegate.getInstance(request);
			//almaviva5_20111114 #4694 aggiunto filtro sist.classe
			AttivazioneSIFListaClassiCollegateVO richiesta = new AttivazioneSIFListaClassiCollegateVO("TO01050705", "prova", null, 10, "classe");
			return classi.getSIFListaClassiCollegate(mapping, richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}



	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
		boolean searchOtherBib = RipartizioneSpeseForm.isRicercaLocale();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!RipartizioneSpeseForm.isSessione())
				{
					RipartizioneSpeseForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			resetToken(request);
			request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("biblio",RipartizioneSpeseForm.getCodBib());
			request.setAttribute("descr",RipartizioneSpeseForm.getDescrBib());
			if(searchOtherBib){
				return mapping.getInputForward();
			}
			else{
				return mapping.findForward("lenteBiblio");
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public  boolean strIsNumeric(String data) {
		return (Pattern.matches("[0-9&&[^a-z]]+", data));
	}

	private String calcolaCodBib(HttpServletRequest request)throws Exception {
		String bibl;
		String bibl1 = Navigation.getInstance(request).getUtente().getCodBib();
		UserVO ute= (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
		String bibl2 = ute.getCodBib();
		if(!(bibl2.equals(bibl1)) || (bibl2==null)){
			bibl = bibl2;
		}else{
			bibl= bibl1;
		}
		return bibl;
	}

	private void getBiblioteca(HttpSession httpSession,List ret, HttpServletRequest request, ActionForm form) throws Exception
	{
		RipartizioneSpeseForm RipartizioneSpeseForm = (RipartizioneSpeseForm) form;
		/// emulazione da togliere quando imp la funzione di login
		//applico algoritmo per l'identificazione della lista bib
		if (ret.size() == 1) {
			RipartizioneSpeseForm.setCodBib(((CodiceVO)ret.get(0)).getCodice());
			RipartizioneSpeseForm.setDescrBib("Biblioteca principale dell'ICCU");
		} else if (ret.size() > 1) {
			//mi chiedo se il codBib della biblio operante è nella lista delle biblioteche
			//prospetto la mappa con codBib, lente e descr  disable=false e carico la lista delle sezioni
			//della prima biblioteca della lista
			RipartizioneSpeseForm.setCodBib((this.searchBibliotecaOperante(ret,httpSession).getCodice()));
		}else {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.stampaFornitori.campoNonImp"));
			this.saveErrors(request, errors);
		}
	}

	private CodiceVO searchBibliotecaOperante(List listaBiblioteca,HttpSession session)
	{
		CodiceVO ret = (CodiceVO)listaBiblioteca.get(0);
		String codBiblioOperante = (String) session.getAttribute("CODICE");
		for(int index = 0; index<listaBiblioteca.size(); index++)
		{
			if(((CodiceVO)listaBiblioteca.get(index)).getCodice().equals(codBiblioOperante))
			{
				ret = (CodiceVO)listaBiblioteca.get(index);
				break;
			}
		}
		return ret;
	}
	private void loadTipoOrdine(RipartizioneSpeseForm RipartizioneSpeseForm) throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		//RipartizioneSpeseForm.setListaTipoOrdine(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine()));


		CaricamentoCombo carCombo = new CaricamentoCombo();
		List arrListaTipoOrdine=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoOrdine());

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);

		//tck 2565 esclusione di D, L,C
		for (int j=0;  j < arrListaTipoOrdine.size(); j++)
		{
			ComboCodDescVO eleTipoOrd= (ComboCodDescVO) arrListaTipoOrdine.get(j);
			if (eleTipoOrd.getCodice().equals("A") || eleTipoOrd.getCodice().equals("V")) // || eleTipoOrd.getCodice().equals("R")
			{
				elem = new StrutturaCombo(eleTipoOrd.getCodice(), eleTipoOrd.getCodice() +" - " + eleTipoOrd.getDescrizione());
				lista.add(elem);
			}
		}
		RipartizioneSpeseForm.setListaTipoOrdine(lista);


	}

    private void loadNatura(RipartizioneSpeseForm RipartizioneSpeseForm) throws Exception {
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		RipartizioneSpeseForm.setListaNatura(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceNaturaOrdine()));
	}

    private void loadTipoImpegno(RipartizioneSpeseForm RipartizioneSpeseForm) throws Exception {
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	RipartizioneSpeseForm.setListaTipoImpegno(carCombo.loadComboCodiciDescACQ (factory.getCodici().getCodiceTipoMateriale()));
	}

	private List loadLingue() throws Exception {
		CaricamentoCombo carCombo = new CaricamentoCombo();
    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceLingua());
		return lista;
	}

   private void cercaCodLingua(String cod, RipartizioneSpeseForm form) throws Exception {
	   RipartizioneSpeseForm RipartizioneSpeseForm = form;
	   	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		RipartizioneSpeseForm.setLingueDescr(factory.getCodici().cercaDescrizioneCodice(cod, CodiciType.CODICE_LINGUA,CodiciRicercaType.RICERCA_CODICE_SBN ));
		}


	private List loadPaesi() throws Exception {
    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese());
		return lista;
	}

   private void cercaCodPaese(String cod, RipartizioneSpeseForm form) throws Exception {
	   RipartizioneSpeseForm RipartizioneSpeseForm = form;
	   	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		RipartizioneSpeseForm.setPaesiDescr(factory.getCodici().cercaDescrizioneCodice(cod, CodiciType.CODICE_PAESE,CodiciRicercaType.RICERCA_CODICE_SBN ));
		}

    private void loadTipoMaterialeInventariale(RipartizioneSpeseForm RipartizioneSpeseForm) throws Exception {
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	RipartizioneSpeseForm.setListaTipoMaterialeInv(carCombo.loadComboCodiciDescACQ (factory.getCodici().getCodiceTipoMaterialeInventariale()));
	}

    private void loadSupporto(RipartizioneSpeseForm RipartizioneSpeseForm) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	RipartizioneSpeseForm.setListaSupporto(carCombo.loadComboCodiciDescACQ (factory.getCodici().getCodiceSupportoCopia()));
	}
    private void loadTipiRecordTitoli(RipartizioneSpeseForm RipartizioneSpeseForm) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	RipartizioneSpeseForm.setListaTipoRecord(carCombo.loadComboCodiciDescACQ (factory.getCodici().getCodiceGenereMaterialeUnimarc()));
	}

	private void loadRangeDewey(RipartizioneSpeseForm RipartizioneSpeseForm) throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","   ");
		lista.add(elem);
		elem = new StrutturaCombo("0","000-099");
		lista.add(elem);
		elem = new StrutturaCombo("1","100-199");
		lista.add(elem);
		elem = new StrutturaCombo("2","200-299");
		lista.add(elem);
		elem = new StrutturaCombo("3","300-399");
		lista.add(elem);
		elem = new StrutturaCombo("4","400-499");
		lista.add(elem);
		elem = new StrutturaCombo("5","500-599");
		lista.add(elem);
		elem = new StrutturaCombo("6","600-699");
		lista.add(elem);
		elem = new StrutturaCombo("7","700-799");
		lista.add(elem);
		elem = new StrutturaCombo("8","800-899");
		lista.add(elem);
		elem = new StrutturaCombo("9","900-999");
		lista.add(elem);

		RipartizioneSpeseForm.setListaRangeDewey(lista);
	}
   private void cercaCodTipoMaterialeInventariale(String cod, RipartizioneSpeseForm form) throws Exception {
   	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
   	RipartizioneSpeseForm RipartizioneSpeseForm = form;
	RipartizioneSpeseForm.setTipoMaterialeInvDescr(factory.getCodici().cercaDescrizioneCodice(cod, CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,CodiciRicercaType.RICERCA_CODICE_SBN ));
	}

   private void cercaCodSupporto(String cod, RipartizioneSpeseForm form) throws Exception {
	   	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	   	RipartizioneSpeseForm RipartizioneSpeseForm = form;
		RipartizioneSpeseForm.setSupportoDescr(factory.getCodici().cercaDescrizioneCodice(cod, CodiciType.CODICE_SUPPORTO_COPIA,CodiciRicercaType.RICERCA_CODICE_SBN ));
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

	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			request.setAttribute("stampa", "stampaOnLine");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
		return unspecified(mapping, form, request, response);
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("STAMPASPESE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				// 04.12.08 e.printStackTrace();
				log.error(e);
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}
// da ripristinare se si mette il tag statistiche
		if (idCheck.equals("STAMPASTAT") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				//utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STATISTICHE_TEMPI, utente.getCodPolo(), utente.getCodBib(), null);
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE, utente.getCodPolo(), utente.getCodBib(), null);
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
