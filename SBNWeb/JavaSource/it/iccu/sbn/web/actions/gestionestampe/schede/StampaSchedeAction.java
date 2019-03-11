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
package it.iccu.sbn.web.actions.gestionestampe.schede;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.schede.StampaSchedeForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


//public class StampaSchedeAction extends ReportAction {
public class StampaSchedeAction  extends RicercaInventariCollocazioniAction {

	private static Logger log = Logger.getLogger(StampaSchedeAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerCollocazione", "selColloc");
		map.put("documentofisico.selPerInventari", "selInv");
		map.put("documentofisico.selPerIdentificativiTitoli", "selIdentificativiTitoli");
		map.put("documentofisico.bottone.conferma","conferma");
		map.put("documentofisico.bottone.indietro","indietro");
		map.put("button.caricaFileIdentificativi","caricaFileIdentificativi");
		map.put("stampa.schede.tag.identificativo", "selezIdentificativo");


		map.put("gestionestampe.lsBib", "listaSupportoBib");


		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaSchedeForm stampaSchedeForm = (StampaSchedeForm) form;

		// Intervento del 11.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
		// Eliminata combo con tutte le biblio ed inserito cartiglio come in stampe di Documento Fisico
		if (request.getAttribute("codBibDaLista") != null) {
			BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
			request.setAttribute("codBib", bib.getCod_bib());
			request.setAttribute("descrBib", bib.getNom_biblioteca());
			stampaSchedeForm.setCodiceBibl(bib.getCod_bib());
			stampaSchedeForm.setDescrBibl(bib.getNom_biblioteca());
		}

		// Modifica almaviva2 03.01.2011 (BUON COMPLEANNO !!) BUG MANTIS 4040
		// si aggiunge if su PROVENIENZA che viene altrimenti ripetuto dal metodo successivo, si modifica anche l'if successivo e
		// si imposta il campo selezione al valore N (StampaSchedeAction-unspecified)
		if (request.getParameter("PROVENIENZA") != null) {
			if (request.getParameter("PROVENIENZA").equals("ANALITICA")) {
				stampaSchedeForm.setProvenienza("ANALITICA");
			}
		}

		super.unspecified(mapping, stampaSchedeForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

		if (stampaSchedeForm.getProvenienza() == null) {
			stampaSchedeForm.setProvenienza("");
		}

		if (stampaSchedeForm.getProvenienza().equals("ANALITICA")) {
			if (request.getAttribute("bid") != null) {
				stampaSchedeForm.setNumIdentificativo01((String) request.getAttribute("bid"));
				stampaSchedeForm.setSelezione("N");
			}
		}

		//VALUTAZIONE SCELTA DEL TAB
		if (stampaSchedeForm.getFolder() == null) {

			stampaSchedeForm.setCodiceBibl(Navigation.getInstance(request).getUtente().getCodBib());
			stampaSchedeForm.setDescrBibl(Navigation.getInstance(request).getUtente().getBiblioteca());
			stampaSchedeForm.setCodicePolo(Navigation.getInstance(request).getUtente().getCodPolo());
			stampaSchedeForm.setTicket(Navigation.getInstance(request).getUtente().getTicket());

			// Inizializzazione dal modello generico
			ModelloDefaultVO modello = null;
			try{
				modello = this.getModelloDefault(Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(), Navigation.getInstance(request).getUtente().getTicket());
			} catch (ValidationException e) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				this.saveErrors(request, errors);
			} catch (DataException e) {
				if (e.getMessage() != null && e.getMessage().equals("recModelloDefaultInesistente")){
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.controllareConfigurazioneDocumentoFisico"));
					this.saveErrors(request, errors);
					stampaSchedeForm.setDisable(true);
				}else{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
					this.saveErrors(request, errors);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			if (modello != null){
				if (modello.getFormatoStampa().equals("HTM")){
					stampaSchedeForm.setTipoFormato(TipoStampa.HTML.name());
				}else{
					stampaSchedeForm.setTipoFormato(modello.getFormatoStampa());
				}
				stampaSchedeForm.setChkCatAttAutori(ValidazioneDati.equalsIgnoreCase(modello.getSch_autori(), "S"));
				stampaSchedeForm.setChkCatAttClassificazioni(ValidazioneDati.equalsIgnoreCase(modello.getSch_classi(), "S"));
				stampaSchedeForm.setChkCatAttEditori(ValidazioneDati.equalsIgnoreCase(modello.getSch_edit(), "S"));
				stampaSchedeForm.setChkCatAttPossessori(ValidazioneDati.equalsIgnoreCase(modello.getSch_poss(), "S"));
				stampaSchedeForm.setChkCatAttSoggetti(ValidazioneDati.equalsIgnoreCase(modello.getSch_soggetti(), "S"));
				stampaSchedeForm.setChkCatAttTitoli(ValidazioneDati.equalsIgnoreCase(modello.getSch_titoli(), "S"));
				stampaSchedeForm.setChkCatAttTopografico(ValidazioneDati.equalsIgnoreCase(modello.getSch_topog(), "S"));

				stampaSchedeForm.setChkStampaTitNonPoss(ValidazioneDati.equalsIgnoreCase(modello.getFl_non_inv(), "S"));


				if (modello.getFl_tipo_leg() != null && modello.getFl_tipo_leg().equals("")) {
					if (modello.getFl_tipo_leg().equals("T")){
						stampaSchedeForm.setTipoAutore("01");
					}else if (modello.getFl_tipo_leg().equals("P")){
						stampaSchedeForm.setTipoAutore("02");
					}else if (modello.getFl_tipo_leg().equals("S")){
						stampaSchedeForm.setTipoAutore("03");
					}
				} else {
					stampaSchedeForm.setTipoAutore("01");
				}


				stampaSchedeForm.setChkCollSchClassificazioni(modello.getFl_coll_cla().equalsIgnoreCase("S"));
				stampaSchedeForm.setChkCollSchEditori(modello.getFl_coll_edi().equalsIgnoreCase("S"));
				stampaSchedeForm.setChkCollSchPossessori(modello.getFl_coll_poss().equalsIgnoreCase("S"));
				stampaSchedeForm.setChkCollSchPrincipale(modello.getFl_coll_aut().equalsIgnoreCase("S"));
				stampaSchedeForm.setChkCollSchRichiami(modello.getFlg_coll_richiamo().equalsIgnoreCase("S"));
				stampaSchedeForm.setChkCollSchSoggetti(modello.getFl_coll_sog().equalsIgnoreCase("S"));
				stampaSchedeForm.setChkCollSchTitoli(modello.getFl_coll_tit().equalsIgnoreCase("S"));
				stampaSchedeForm.setChkCollSchTopografico(modello.getFl_coll_top().equalsIgnoreCase("S"));

				stampaSchedeForm.setNumCollSchClassificazioni(Integer.valueOf(modello.getN_copie_cla()));
				stampaSchedeForm.setNumCollSchEditori(Integer.valueOf(modello.getN_copie_edi()));
				stampaSchedeForm.setNumCollSchPossessori(Integer.valueOf(modello.getN_copie_poss()));
				stampaSchedeForm.setNumCollSchPrincipale(Integer.valueOf(modello.getN_copie_aut()));
				stampaSchedeForm.setNumCollSchRichiami(Integer.valueOf(modello.getN_copie_richiamo()));
				stampaSchedeForm.setNumCollSchSoggetti(Integer.valueOf(modello.getN_copie_sog()));
				stampaSchedeForm.setNumCollSchTitoli(Integer.valueOf(modello.getN_copie_tit()));
				stampaSchedeForm.setNumCollSchTopografico(Integer.valueOf(modello.getN_copie_top()));
			}


			// CHIAMATA ALL'EJB DI AMMINISTRAZIONE PER LISTA BIBLIOTECHE DEL POLO
//			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//			try {
//				stampaSchedeForm.setListaBiblio(factory.getGestioneBibliografica()
//						.getComboBibliotechePolo(stampaSchedeForm.getCodicePolo(), stampaSchedeForm.getTicket()));
//			} catch (RemoteException e1) {
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + e1.getMessage()));
//				this.saveErrors(request, errors);
//			}
//
//			ComboCodDescVO comboCodDescVO = new ComboCodDescVO();
//			comboCodDescVO.setCodice("");
//			comboCodDescVO.setDescrizione("");
//			stampaSchedeForm.addListaBiblio(comboCodDescVO);
//			Collections.sort(stampaSchedeForm.getListaBiblio(), ComboCodDescVO.ORDINA_PER_CODICE);


			stampaSchedeForm.setListaNatura(loadListaNatura());
			stampaSchedeForm.setListaStatus(loadListaStatus());
			stampaSchedeForm.setListaTipoAutore(loadListaTipoAutore());
			stampaSchedeForm.setElemBlocco(null);
			stampaSchedeForm.setTipoFormato("PDF");
			if (stampaSchedeForm.getProvenienza().equals("ANALITICA")) {
				stampaSchedeForm.setFolder("IdentificativiTitoli");
			} else {
				stampaSchedeForm.setFolder("RangeInv");
			}
		}
		stampaSchedeForm.setChkCatAttAutori(true);
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaSchedeForm stampaSchedeForm;
		HttpSession httpSession = request.getSession();
		stampaSchedeForm = (StampaSchedeForm) form;
		try {
			request.setAttribute("parametroPassato", stampaSchedeForm.getElemBlocco());
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaSchedeForm stampaSchedeForm = ((StampaSchedeForm) form);
		try{

			AreaParametriStampaSchedeVo areaParametriStampaSchedeVo = new AreaParametriStampaSchedeVo(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

			if (stampaSchedeForm.getFolder().equals("Collocazioni")){
				if (stampaSchedeForm.getCodPoloSez() == null && stampaSchedeForm.getCodBibSez() == null){
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.premereTastoSezione"));
					this.saveErrors(request, errors);
//					request.setAttribute("currentForm", stampaSchedeForm);
					return mapping.getInputForward();
				}
				super.validaInputCollocazioni(mapping, request, stampaSchedeForm);
				stampaSchedeForm.setTipoOperazione("S");
				areaParametriStampaSchedeVo.setCodPoloSez(stampaSchedeForm.getCodPoloSez());
				areaParametriStampaSchedeVo.setCodBibSez(stampaSchedeForm.getCodBibSez());
				areaParametriStampaSchedeVo.setSezione(stampaSchedeForm.getSezione());
				areaParametriStampaSchedeVo.setDallaCollocazione(stampaSchedeForm.getDallaCollocazione());
				areaParametriStampaSchedeVo.setDallaSpecificazione(stampaSchedeForm.getDallaSpecificazione());
				areaParametriStampaSchedeVo.setAllaCollocazione(stampaSchedeForm.getAllaCollocazione());
				areaParametriStampaSchedeVo.setAllaSpecificazione(stampaSchedeForm.getAllaSpecificazione());
			}

			//			//Ricerca per range di inventari
			if (stampaSchedeForm.getFolder().equals("RangeInv")){
				super.validaInputRangeInventari(mapping, request, stampaSchedeForm);
				stampaSchedeForm.setTipoOperazione("R");
				//validazione startInv e endInv
				areaParametriStampaSchedeVo.setSerie(stampaSchedeForm.getSerie());
				areaParametriStampaSchedeVo.setEndInventario(stampaSchedeForm.getEndInventario());
				areaParametriStampaSchedeVo.setStartInventario(stampaSchedeForm.getStartInventario());
			}

			//				//Ricerca per inventari
			if (stampaSchedeForm.getFolder().equals("Inventari")){
				super.validaInputInventari(mapping, request, stampaSchedeForm);
				stampaSchedeForm.setTipoOperazione("N");
				areaParametriStampaSchedeVo.setListaInventari(stampaSchedeForm.getListaInventari());
				areaParametriStampaSchedeVo.setSelezione(stampaSchedeForm.getSelezione());
				if (stampaSchedeForm.getSelezione() != null && (stampaSchedeForm.getSelezione().equals("F"))){
					areaParametriStampaSchedeVo.setNomeFileAppoggioInv(stampaSchedeForm.getNomeFileAppoggioInv());
				}
			}
			areaParametriStampaSchedeVo.setTipoOperazione(stampaSchedeForm.getTipoOperazione());
			if (stampaSchedeForm.getFolder().equals("IdentificativiTitoli")) {
				super.validaInputIdentificativiTitoli(mapping, request, stampaSchedeForm);
				stampaSchedeForm.setTipoOperazione("N");
				areaParametriStampaSchedeVo.setSelezione(stampaSchedeForm.getSelezione());
				if (stampaSchedeForm.getListaBid() != null && stampaSchedeForm.getListaBid().size() > 0) {
					areaParametriStampaSchedeVo.setTipoOperazione("");
					areaParametriStampaSchedeVo.setListaBid(stampaSchedeForm.getListaBid());
				} else {
					if (stampaSchedeForm.getListaBidDaFile() != null && stampaSchedeForm.getListaBidDaFile().size() > 0) {
						areaParametriStampaSchedeVo.setListaBid(stampaSchedeForm.getListaBidDaFile());
					}
				}
				if (stampaSchedeForm.getSelezione() != null && (stampaSchedeForm.getSelezione().equals("F"))){
					areaParametriStampaSchedeVo.setNomeFileAppoggioBid(stampaSchedeForm.getNomeFileAppoggioBid());
				}
			}
			// Impostazione parametri di Stampa
			areaParametriStampaSchedeVo.setCodErr("0000");
			areaParametriStampaSchedeVo.setTicket(stampaSchedeForm.getTicket());
			areaParametriStampaSchedeVo.setCodPolo(stampaSchedeForm.getCodicePolo());
			areaParametriStampaSchedeVo.setCodBib(stampaSchedeForm.getCodiceBibl());
			areaParametriStampaSchedeVo.setInventariMultipli(stampaSchedeForm.isChkStampaPiuInventari());
			areaParametriStampaSchedeVo.setTitoliNonPosseduti(stampaSchedeForm.isChkStampaTitNonPoss());

			areaParametriStampaSchedeVo.setRichListaPerAutore(stampaSchedeForm.isChkCatAttAutori());
			if (stampaSchedeForm.getTipoAutore().equals("01")) {
				areaParametriStampaSchedeVo.setTipoResponsabilitaRich("TUTTI");
			} else if (stampaSchedeForm.getTipoAutore().equals("02")) {
				areaParametriStampaSchedeVo.setTipoResponsabilitaRich("RESP1");
			} else if (stampaSchedeForm.getTipoAutore().equals("03")) {
				areaParametriStampaSchedeVo.setTipoResponsabilitaRich("RESP2");
			}
			areaParametriStampaSchedeVo.setRichListaPerClassificazione(stampaSchedeForm.isChkCatAttClassificazioni());
			areaParametriStampaSchedeVo.setRichListaPerEditore(stampaSchedeForm.isChkCatAttEditori());
			areaParametriStampaSchedeVo.setRichListaPerPossessore(stampaSchedeForm.isChkCatAttPossessori());
			areaParametriStampaSchedeVo.setRichListaPerSoggetto(stampaSchedeForm.isChkCatAttSoggetti());
			areaParametriStampaSchedeVo.setRichListaPerTitolo(stampaSchedeForm.isChkCatAttTitoli());
			areaParametriStampaSchedeVo.setRichListaPerTopografico(stampaSchedeForm.isChkCatAttTopografico());

			areaParametriStampaSchedeVo.setNumCopiePerAutore(stampaSchedeForm.getNumCollSchPrincipale());

			areaParametriStampaSchedeVo.setNumCopiePerClassificazione(stampaSchedeForm.getNumCollSchClassificazioni());
			areaParametriStampaSchedeVo.setNumCopiePerEditore(stampaSchedeForm.getNumCollSchEditori());
			areaParametriStampaSchedeVo.setNumCopiePerPossessore(stampaSchedeForm.getNumCollSchPossessori());
			areaParametriStampaSchedeVo.setNumCopiePerSoggetto(stampaSchedeForm.getNumCollSchSoggetti());
			areaParametriStampaSchedeVo.setNumCopiePerTitolo(stampaSchedeForm.getNumCollSchTitoli());
			areaParametriStampaSchedeVo.setNumCopiePerTopografico(stampaSchedeForm.getNumCollSchTopografico());
			areaParametriStampaSchedeVo.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE);


			if (stampaSchedeForm.isChkCatAttAutori() ||
					stampaSchedeForm.isChkCatAttClassificazioni() ||
						stampaSchedeForm.isChkCatAttEditori() ||
							stampaSchedeForm.isChkCatAttPossessori() ||
								stampaSchedeForm.isChkCatAttSoggetti() ||
								stampaSchedeForm.isChkCatAttTitoli() ||
								stampaSchedeForm.isChkCatAttTopografico()){

			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.scegliereAlmenoUnCatalogo"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			// Inizio Nuova gestione Batch

			// Fine Nuova gestione Batch


//			String codBib = Navigation.getInstance(request).getUtente().getCodBib();
//			String codPolo=Navigation.getInstance(request).getUtente().getCodPolo();
			//user
			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();
			areaParametriStampaSchedeVo.setUser(utente);

			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			//TODO gv prendere il valore dalla jsp
			String pathJrxml=basePath+File.separator+"jrxml"+File.separator+"scheda_template.jrxml";

			//ho finito di preparare il VO, ora lo metto nell'arraylist che passerò alla coda.
			List parametri=new ArrayList();
			parametri.add(areaParametriStampaSchedeVo);
			request.setAttribute("DatiVo", parametri);

			String pathDownload = StampeUtil.getBatchFilesPath();

			//codice standard inserimento messaggio di richiesta stampa differita
			StampaDiffVO stam = new StampaDiffVO();


			UserVO ute = Navigation.getInstance(request).getUtente();
			stam.setCodPolo(ute.getCodPolo());
			stam.setCodBib(ute.getCodBib());
			stam.setUser(ute.getUserId());
			stam.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE);

			stam.setTipoStampa(stampaSchedeForm.getTipoFormato());
			stam.setTipoOrdinamento("");
			stam.setParametri(parametri);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setTipoOperazione("STAMPA_SCHEDE");
			stam.setTicket(Navigation.getInstance(request).getUserTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);


			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			String idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ute.getTicket(), stam, null);

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
			this.saveErrors(request, errors);

			stampaSchedeForm.setDisable(true);
		} catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	private List loadListaNatura() throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		List lista = new ArrayList();

		// Intervento del 11.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
		// devono essere caricati solo i Documenti M,W,S
//		lista = carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceNaturaBibliografica());
		lista = carCombo.loadComboCodiciDescNatura(factory.getCodici().getCodiceNaturaBibliografica(), "Solo_documenti_per_stampe", "");

		return lista;
	}

	private List loadListaStatus() throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		List lista = new ArrayList();
		List listaNew = new ArrayList();
		lista = carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita());
		// Intervento del 11.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
		// non devono essere caricati solo i livelli 96 e 97

		for (int i=0; i<lista.size(); i++) {

			ComboCodDescVO appo =(ComboCodDescVO) lista.get(i);
			if (appo.getCodice().equals("96") || appo.getCodice().equals("97") ) {
				continue;
			} else {
				listaNew.add(appo);
			}
		}
		return listaNew;
	}

	private List loadListaTipoAutore() throws Exception {
		List lista = new ArrayList();

		StrutturaCombo elem = new StrutturaCombo("01","Tutti gli autori");
		lista.add(elem);

		elem = new StrutturaCombo("02","Solo principale");
		lista.add(elem);

		// Inizio intervento almaviva2 19.04.2012 richiesta Contardi
		// se puo richiedere "Tutti" o "Solo autori principali" (in questo caso si stampano solo RESP 1 e 2)
//		elem = new StrutturaCombo("03","Solo secondari");
//		lista.add(elem);
		// Fine intervento almaviva2 19.04.2012 richiesta Contardi


		return lista;
	}

	public ActionForward caricaFileIdentificativi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StampaSchedeForm stampaSchedeForm = (StampaSchedeForm) form;

		byte[] imgbuf;
		try {
			imgbuf = stampaSchedeForm.getUploadImmagine().getFileData();

			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(imgbuf)));
			if (reader == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.erroreAcquisizioneBidDaFile"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			List<String> listaBidDaFile = new ArrayList<String>();

			String line = null;
			String bidEccedenti = "";
			while ( (line = reader.readLine() ) != null ) {
				if (line.length() == 10) {
					listaBidDaFile.add(line);
				} else {
					bidEccedenti = bidEccedenti + line + " ";
				}
			}


			stampaSchedeForm.setListaBidDaFile(listaBidDaFile);

			ActionMessages errors = new ActionMessages();

			if (bidEccedenti.length() > 0) {
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFileSegnErrore",
						stampaSchedeForm.getUploadImmagine().getFileName(),
						bidEccedenti));
			} else {
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile", stampaSchedeForm.getUploadImmagine().getFileName()));
			}

			this.saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage(
					"errors.gestioneBibliografica.marImgNotValid"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}



	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaSchedeForm stampaSchedeForm = (StampaSchedeForm) form;
		try {

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
				SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(
						Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(),
						CodiciAttivita.getIstance().GA_STAMPA_REGISTRO_DI_INGRESSO,
						stampaSchedeForm.getNRec(),
						"codBibDaLista");
				return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

}
