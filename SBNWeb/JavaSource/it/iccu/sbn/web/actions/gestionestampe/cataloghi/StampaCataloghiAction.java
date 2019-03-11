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
package it.iccu.sbn.web.actions.gestionestampe.cataloghi;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.cataloghi.StampaCataloghiForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
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
public class StampaCataloghiAction  extends RicercaInventariCollocazioniAction {

	private int focusDa = 0;

	private static Logger log = Logger.getLogger(StampaCataloghiAction.class);



	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
//		map.put("documentofisico.selPerRangeInv", "selRangeInv");
//		map.put("documentofisico.selPerCollocazione", "selColloc");
//		map.put("documentofisico.selPerInventari", "selInv");
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
		StampaCataloghiForm stampaCataloghiForm = (StampaCataloghiForm) form;

		// Intervento del 15.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
		// Eliminata combo con tutte le biblio ed inserito cartiglio come in stampe di Documento Fisico
		if (request.getAttribute("codBibDaLista") != null) {
			BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
			request.setAttribute("codBib", bib.getCod_bib());
			request.setAttribute("descrBib", bib.getNom_biblioteca());
			stampaCataloghiForm.setCodiceBibl(bib.getCod_bib());
			stampaCataloghiForm.setDescrBibl(bib.getNom_biblioteca());
		} else {
			request.setAttribute("codBib", stampaCataloghiForm.getCodiceBibl());
		}


		super.unspecified(mapping, stampaCataloghiForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

		if (stampaCataloghiForm.getProvenienza() == null) {
			stampaCataloghiForm.setProvenienza("");
		}

		if (request.getParameter("PROVENIENZA") != null) {
			if (request.getParameter("PROVENIENZA").equals("ANALITICA")) {
				stampaCataloghiForm.setProvenienza("ANALITICA");
				if (request.getAttribute("bid") != null) {
					stampaCataloghiForm.setNumIdentificativo01((String) request.getAttribute("bid"));

				}
			}
		}


		//VALUTAZIONE SCELTA DEL TAB
		if (stampaCataloghiForm.getFolder() == null) {

			stampaCataloghiForm.setCodiceBibl(Navigation.getInstance(request).getUtente().getCodBib());
			stampaCataloghiForm.setCodicePolo(Navigation.getInstance(request).getUtente().getCodPolo());
			stampaCataloghiForm.setTicket(Navigation.getInstance(request).getUtente().getTicket());

			// Inizializzazione dal modello generico
			ModelloDefaultVO modello = this.getModelloDefault(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(), Navigation.getInstance(request).getUtente().getTicket());
			if (modello != null){
				if (modello.getFormatoStampa().equals("HTM")){
					stampaCataloghiForm.setTipoFormato(TipoStampa.HTML.name());
				}else{
					stampaCataloghiForm.setTipoFormato(modello.getFormatoStampa());
				}
				stampaCataloghiForm.setChkCatAttAutori(ValidazioneDati.equalsIgnoreCase(modello.getSch_autori(), "S"));
				stampaCataloghiForm.setChkCatAttClassificazioni(ValidazioneDati.equalsIgnoreCase(modello.getSch_classi(), "S"));
				stampaCataloghiForm.setChkCatAttEditori(ValidazioneDati.equalsIgnoreCase(modello.getSch_edit(), "S"));
				stampaCataloghiForm.setChkCatAttPossessori(ValidazioneDati.equalsIgnoreCase(modello.getSch_poss(), "S"));
				stampaCataloghiForm.setChkCatAttSoggetti(ValidazioneDati.equalsIgnoreCase(modello.getSch_soggetti(), "S"));
				stampaCataloghiForm.setChkCatAttTitoli(ValidazioneDati.equalsIgnoreCase(modello.getSch_titoli(), "S"));
				stampaCataloghiForm.setChkCatAttTopografico(ValidazioneDati.equalsIgnoreCase(modello.getSch_topog(), "S"));

				if (modello.getFl_tipo_leg() != null && modello.getFl_tipo_leg().equals("")) {
					if (modello.getFl_tipo_leg().equals("T")){
						stampaCataloghiForm.setTipoAutore("01");
					}else if (modello.getFl_tipo_leg().equals("P")){
						stampaCataloghiForm.setTipoAutore("02");
					}else if (modello.getFl_tipo_leg().equals("S")){
						stampaCataloghiForm.setTipoAutore("03");
					}
				} else {
					stampaCataloghiForm.setTipoAutore("01");
				}
			}


			// CHIAMATA ALL'EJB DI AMMINISTRAZIONE PER LISTA BIBLIOTECHE DEL POLO
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			try {
				stampaCataloghiForm.setListaBiblio(factory.getGestioneBibliografica()
						.getComboBibliotechePolo(stampaCataloghiForm.getCodicePolo(), stampaCataloghiForm.getTicket()));
			} catch (RemoteException e1) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.gestioneBibliografica." + e1.getMessage()));
				this.saveErrors(request, errors);
			}

			ComboCodDescVO comboCodDescVO = new ComboCodDescVO();
			comboCodDescVO.setCodice("");
			comboCodDescVO.setDescrizione("");
			stampaCataloghiForm.addListaBiblio(comboCodDescVO);
			Collections.sort(stampaCataloghiForm.getListaBiblio(), ComboCodDescVO.ORDINA_PER_CODICE);

			this.caricaComboTitoli(stampaCataloghiForm);
			stampaCataloghiForm.setElemBlocco(null);
			stampaCataloghiForm.setTipoFormato("PDF");
			if (stampaCataloghiForm.getProvenienza().equals("ANALITICA")) {
				stampaCataloghiForm.setFolder("IdentificativiTitoli");
			} else {
				stampaCataloghiForm.setFolder("RangeInv");
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaCataloghiForm stampaCataloghiForm;
		HttpSession httpSession = request.getSession();
		stampaCataloghiForm = (StampaCataloghiForm) form;
		try {
			request.setAttribute("parametroPassato", stampaCataloghiForm.getElemBlocco());
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaCataloghiForm stampaCataloghiForm = ((StampaCataloghiForm) form);
		try{

			AreaParametriStampaSchedeVo areaParametriStampaSchedeVo = new AreaParametriStampaSchedeVo(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

			if (stampaCataloghiForm.getFolder().equals("Collocazioni")){
				if (stampaCataloghiForm.getCodPoloSez() == null && stampaCataloghiForm.getCodBibSez() == null){
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.premereTastoSezione"));
					this.saveErrors(request, errors);
//					request.setAttribute("currentForm", stampaCataloghiForm);
					return mapping.getInputForward();
				}
				super.validaInputCollocazioni(mapping, request, stampaCataloghiForm);
				stampaCataloghiForm.setTipoOperazione("S");
				areaParametriStampaSchedeVo.setCodPoloSez(stampaCataloghiForm.getCodPoloSez());
				areaParametriStampaSchedeVo.setCodBibSez(stampaCataloghiForm.getCodBibSez());
				areaParametriStampaSchedeVo.setSezione(stampaCataloghiForm.getSezione());
				areaParametriStampaSchedeVo.setDallaCollocazione(stampaCataloghiForm.getDallaCollocazione());
				areaParametriStampaSchedeVo.setDallaSpecificazione(stampaCataloghiForm.getDallaSpecificazione());
				areaParametriStampaSchedeVo.setAllaCollocazione(stampaCataloghiForm.getAllaCollocazione());
				areaParametriStampaSchedeVo.setAllaSpecificazione(stampaCataloghiForm.getAllaSpecificazione());
			}

			//Ricerca per range di inventari
			if (stampaCataloghiForm.getFolder().equals("RangeInv")){
				super.validaInputRangeInventari(mapping, request, stampaCataloghiForm);
				stampaCataloghiForm.setTipoOperazione("R");
				//validazione startInv e endInv
				areaParametriStampaSchedeVo.setSerie(stampaCataloghiForm.getSerie());
				areaParametriStampaSchedeVo.setEndInventario(stampaCataloghiForm.getEndInventario());
				areaParametriStampaSchedeVo.setStartInventario(stampaCataloghiForm.getStartInventario());
			}

			//Ricerca per inventari
			if (stampaCataloghiForm.getFolder().equals("Inventari")){
				super.validaInputInventari(mapping, request, stampaCataloghiForm);
				stampaCataloghiForm.setTipoOperazione("N");
				areaParametriStampaSchedeVo.setListaInventari(stampaCataloghiForm.getListaInventari());
				areaParametriStampaSchedeVo.setSelezione(stampaCataloghiForm.getSelezione());
				if (stampaCataloghiForm.getSelezione() != null && (stampaCataloghiForm.getSelezione().equals("F"))){
					areaParametriStampaSchedeVo.setNomeFileAppoggioInv(stampaCataloghiForm.getNomeFileAppoggioInv());
				}
			}
			areaParametriStampaSchedeVo.setTipoOperazione(stampaCataloghiForm.getTipoOperazione());
			if (stampaCataloghiForm.getFolder().equals("IdentificativiTitoli")) {
				super.validaInputIdentificativiTitoli(mapping, request, stampaCataloghiForm);
				stampaCataloghiForm.setTipoOperazione("N");
				if (stampaCataloghiForm.getListaBid() != null && stampaCataloghiForm.getListaBid().size() > 0) {
					areaParametriStampaSchedeVo.setTipoOperazione("");
					areaParametriStampaSchedeVo.setListaBid(stampaCataloghiForm.getListaBid());
				} else {
					if (stampaCataloghiForm.getListaBidDaFile() != null && stampaCataloghiForm.getListaBidDaFile().size() > 0) {
						areaParametriStampaSchedeVo.setListaBid(stampaCataloghiForm.getListaBidDaFile());
					}
				}
				if (stampaCataloghiForm.getSelezione() != null && (stampaCataloghiForm.getSelezione().equals("F"))){
					areaParametriStampaSchedeVo.setNomeFileAppoggioBid(stampaCataloghiForm.getNomeFileAppoggioBid());
				}
				if (stampaCataloghiForm.getSelezione() != null && (stampaCataloghiForm.getSelezione().equals("T"))){
					// selezione dei filtri titoli
					areaParametriStampaSchedeVo.getParametriSelezioneOggBiblioVO().setGenereSelez(stampaCataloghiForm.getDescGenere());
					areaParametriStampaSchedeVo.getParametriSelezioneOggBiblioVO().setLinguaSelez(stampaCataloghiForm.getDescLingua());
					areaParametriStampaSchedeVo.getParametriSelezioneOggBiblioVO().setLivAutSelez(stampaCataloghiForm.getDescLivAut());
					areaParametriStampaSchedeVo.getParametriSelezioneOggBiblioVO().setNaturaSelez(stampaCataloghiForm.getNatura());
					areaParametriStampaSchedeVo.getParametriSelezioneOggBiblioVO().setPaeseSelez(stampaCataloghiForm.getDescPaese());
					areaParametriStampaSchedeVo.getParametriSelezioneOggBiblioVO().setTipoDataSelez(stampaCataloghiForm.getDescTipoData());
				}
			}
			// Impostazione parametri di Stampa
			areaParametriStampaSchedeVo.setCodErr("0000");
			areaParametriStampaSchedeVo.setTicket(stampaCataloghiForm.getTicket());
			areaParametriStampaSchedeVo.setCodPolo(stampaCataloghiForm.getCodicePolo());
			areaParametriStampaSchedeVo.setCodBib(stampaCataloghiForm.getCodiceBibl());

			if (stampaCataloghiForm.isIntestTitoloAdAutore()) {
				areaParametriStampaSchedeVo.setIntestTitoloAdAutore("SI");
			} else {
				areaParametriStampaSchedeVo.setIntestTitoloAdAutore("NO");
			}
			if (stampaCataloghiForm.isTitoloCollana()) {
				areaParametriStampaSchedeVo.setTitoloCollana("SI");
			} else {
				areaParametriStampaSchedeVo.setTitoloCollana("NO");
			}
			if (stampaCataloghiForm.isTitoliAnalitici()) {
				areaParametriStampaSchedeVo.setTitoliAnalitici("SI");
			} else {
				areaParametriStampaSchedeVo.setTitoliAnalitici("NO");
			}
			if (stampaCataloghiForm.isDatiCollocazione()) {
				areaParametriStampaSchedeVo.setDatiCollocazione("SI");
			} else {
				areaParametriStampaSchedeVo.setDatiCollocazione("NO");
			}

			areaParametriStampaSchedeVo.setRichListaPerAutore(stampaCataloghiForm.isChkCatAttAutori());
			if (stampaCataloghiForm.getTipoAutore().equals("01")) {
				areaParametriStampaSchedeVo.setTipoResponsabilitaRich("TUTTI");
			} else if (stampaCataloghiForm.getTipoAutore().equals("02")) {
				areaParametriStampaSchedeVo.setTipoResponsabilitaRich("RESP1");
			} else if (stampaCataloghiForm.getTipoAutore().equals("03")) {
				areaParametriStampaSchedeVo.setTipoResponsabilitaRich("RESP2");
			}
			areaParametriStampaSchedeVo.setRichListaPerClassificazione(stampaCataloghiForm.isChkCatAttClassificazioni());
			areaParametriStampaSchedeVo.setRichListaPerEditore(stampaCataloghiForm.isChkCatAttEditori());
			areaParametriStampaSchedeVo.setRichListaPerPossessore(stampaCataloghiForm.isChkCatAttPossessori());
			areaParametriStampaSchedeVo.setRichListaPerSoggetto(stampaCataloghiForm.isChkCatAttSoggetti());
			areaParametriStampaSchedeVo.setRichListaPerTitolo(stampaCataloghiForm.isChkCatAttTitoli());
			areaParametriStampaSchedeVo.setRichListaPerTopografico(stampaCataloghiForm.isChkCatAttTopografico());

			areaParametriStampaSchedeVo.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI);

			//user
			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();
			areaParametriStampaSchedeVo.setUser(utente);

			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			//TODO gv prendere il valore dalla jsp
			String pathJrxml=basePath+File.separator+"jrxml"+File.separator+"default_catalografico.jrxml";

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
			stam.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI);

			stam.setTipoStampa(stampaCataloghiForm.getTipoFormato());
			stam.setTipoOrdinamento("");
			stam.setParametri(parametri);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setTipoOperazione("STAMPA_CATALOGHI");
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

			stampaCataloghiForm.setDisable(true);
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

//	private List loadListaNatura() throws Exception {
//
//		CaricamentoCombo carCombo = new CaricamentoCombo();
//		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//
//		List lista = new ArrayList();
//		lista = carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceNaturaBibliografica());
//		return lista;
//	}

//	private List loadListaStatus() throws Exception {
//
//		CaricamentoCombo carCombo = new CaricamentoCombo();
//		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//
//		List lista = new ArrayList();
//		lista = carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita());
//		return lista;
//	}

//	private List loadListaTipoAutore() throws Exception {
//		List lista = new ArrayList();
//		StrutturaCombo elem = new StrutturaCombo("01","Tutti gli autori");
//		lista.add(elem);
//		elem = new StrutturaCombo("02","Solo principale");
//		lista.add(elem);
//		elem = new StrutturaCombo("03","Solo secondari");
//		lista.add(elem);
//		return lista;
//	}

	public ActionForward caricaFileIdentificativi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StampaCataloghiForm stampaCataloghiForm = (StampaCataloghiForm) form;

		byte[] imgbuf;
		try {
			imgbuf = stampaCataloghiForm.getUploadImmagine().getFileData();

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


			stampaCataloghiForm.setListaBidDaFile(listaBidDaFile);

			ActionMessages errors = new ActionMessages();

			if (bidEccedenti.length() > 0) {
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFileSegnErrore",
						stampaCataloghiForm.getUploadImmagine().getFileName(),
						bidEccedenti));
			} else {
				errors.add("Attenzione", new ActionMessage(
						"errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile", stampaCataloghiForm.getUploadImmagine().getFileName()));
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


	private void caricaComboTitoli(StampaCataloghiForm stampaCataloghiForm)
		throws RemoteException, CreateException, NamingException {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		stampaCataloghiForm.setListaNatura(carCombo.loadComboCodiciDescNatura(factory.getCodici().getCodiceNaturaBibliografica(), "", ""));
		stampaCataloghiForm.setListaLivAut(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLivelloAutorita()));
		stampaCataloghiForm.setListaLingua(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceLingua()));
		stampaCataloghiForm.setListaPaese(carCombo.loadComboCodiciDesc(factory.getCodici().getCodicePaese()));
		stampaCataloghiForm.setListaGenere(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceGenerePubblicazione()));
		stampaCataloghiForm.setListaTipoData(carCombo.loadComboCodiciDesc(factory.getCodici().getCodiceTipoDataPubblicazione()));
	}


}
