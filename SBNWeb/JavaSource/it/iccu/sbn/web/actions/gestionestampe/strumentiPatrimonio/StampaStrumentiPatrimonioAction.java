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
package it.iccu.sbn.web.actions.gestionestampe.strumentiPatrimonio;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.DocumentoFisicoElaborazioniDifferiteOutputVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStrumentiPatrimonioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.validators.documentofisico.StampaStrumentiPatrimonioValidator;
import it.iccu.sbn.web.actionforms.gestionestampe.strumentiPatrimonio.StampaStrumentiPatrimonioForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class StampaStrumentiPatrimonioAction extends RicercaInventariCollocazioniAction {

	private static Log log = LogFactory.getLog(StampaStrumentiPatrimonioAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerCollocazione", "selColloc");
		map.put("documentofisico.selPerInventari", "selInv");
		map.put("documentofisico.selData", "selData");
		map.put("documentofisico.selPerIdentificativiTitoli", "selIdentificativiTitoli");
		map.put("documentofisico.lsSez", "listaSupportoSez");
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");

		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		StampaStrumentiPatrimonioForm currentForm = (StampaStrumentiPatrimonioForm) form;
		if (!currentForm.isSessione()) {
			try {

			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try{
			// setto il token per le transazioni successive
			this.saveToken(request);

			StampaStrumentiPatrimonioForm currentForm = ((StampaStrumentiPatrimonioForm) form);
			super.unspecified(mapping, currentForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			if (!currentForm.isSessione()) {
				Navigation navi = Navigation.getInstance(request);
				currentForm.setTicket(navi.getUserTicket());
				UserVO utente = navi.getUtente();
				currentForm.setCodPolo(utente.getCodPolo());
				currentForm.setCodBib(utente.getCodBib());
				currentForm.setDescrBib(utente.getBiblioteca());
				log.info("AggiornamentoDisponibilitaAction::unspecified");
				loadDefault(request, mapping, form);
				currentForm.setModPrel(false);
				currentForm.setSessione(true);
			}
			//almaviva5_20130709
			currentForm.setCodAttivita(CodiciAttivita.getIstance().GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO);

			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			currentForm.setListaCodStatoConservazione(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE,true)));
			currentForm.setListaTipoMateriale(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,true)));
			currentForm.setListaCodNoDispo(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_NON_DISPONIBILITA,true)));
			if (currentForm.getListaCodNoDispo().size() > 0){
				CodiceVO rec = new CodiceVO();
				rec.setCodice("Z");
				rec.setDescrizione("Nessuno");
				currentForm.getListaCodNoDispo().add(rec);
			}else{
				throw new ValidationException("dropNoDispVuota");
			}
			this.loadTipiOrdinamento(currentForm);

			currentForm.setSerie("");
			currentForm.setStartInventario("0");
			currentForm.setEndInventario("0");
			if (Navigation.getInstance(request).isFromBar() ){
				return mapping.getInputForward();
			}
//			currentForm.setElencoModelli(getElencoModelli());
			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				currentForm.setCodBib((String) request.getAttribute("codBib"));
				currentForm.setDescrBib((String)request.getAttribute("descrBib"));
			}
			currentForm.setModRegistri(true);

			currentForm.setElencoModelli(getElencoModelli());
			currentForm.setTipoFormato(TipoStampa.XLS.name());
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

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StampaStrumentiPatrimonioForm currentForm = ((StampaStrumentiPatrimonioForm) form);
		String dataPrelievo = currentForm.getDataPrelievo();
		String motivoPrelievo = currentForm.getMotivoPrelievo();
		request.setAttribute("currentForm", currentForm);
		if (currentForm.isModPrel()) {
			if (!isFilled(dataPrelievo)) {
				// almaviva2 - Intervento interno per inviare errori - 26 gennaio 2017
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.indicareDataPrelievo"));
				resetToken(request);
				return mapping.getInputForward();
			}
			if (!isFilled(motivoPrelievo)) {
				// almaviva2 - Intervento interno per inviare errori - 26 gennaio 2017
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.indicareMotivoPrelievo"));
				resetToken(request);
				return mapping.getInputForward();
			}
		}
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<DocumentoFisicoElaborazioniDifferiteOutputVO> parametri = new ArrayList<DocumentoFisicoElaborazioniDifferiteOutputVO>();
		StampaDiffVO stampa = new StampaDiffVO();
		try {
			//Ricerca per Data
			StampaStrumentiPatrimonioVO spVO = currentForm.getRichiesta();
			if (currentForm.getTipoModello().equals("default_posseduto")){
				stampa.setTipoRegistro("possedutoXls");
			}else {
				//(currentForm.getTipoModello().equals("default_registro_strumenti_patrimonio")){				}
				stampa.setTipoRegistro("patrimonioXls");
			}
				String folder = currentForm.getFolder();
				if (folder.equals("Data")) {
					currentForm.setTipoOperazione("D");
					//super.validaInputDataDaA(mapping, request, currentForm);
					spVO.setDataDa(currentForm.getDataDa());
					spVO.setDataA(currentForm.getDataA());
					//almaviva5_20130709
					spVO.setDataPrimaCollDa(currentForm.getDataPrimaCollDa());
					spVO.setDataPrimaCollA(currentForm.getDataPrimaCollA());
					if (currentForm.getCheck().equals("nuoviTitoli")){
						spVO.setNuoviTitoli(true);
						spVO.setNuoviEsemplari(false);
					}
					if (currentForm.getCheck().equals("nuoviEsemplari")){
						spVO.setNuoviEsemplari(true);
						spVO.setNuoviTitoli(false);
					}
				}else{
					if (folder.equals("Collocazioni")){
						currentForm.setTipoOperazione("S");
						super.validaInputCollocazioni(mapping, request, currentForm);
						spVO.setCodPoloSez(currentForm.getCodPoloSez());
						spVO.setCodBibSez(currentForm.getCodBibSez());
						spVO.setSezione(currentForm.getSezione());
						spVO.setDallaCollocazione(currentForm.getDallaCollocazione());
						spVO.setDallaSpecificazione(currentForm.getDallaSpecificazione());
						spVO.setAllaCollocazione(currentForm.getAllaCollocazione());
						spVO.setAllaSpecificazione(currentForm.getAllaSpecificazione());
					}

					//Ricerca per range di inventari
					if (folder.equals("RangeInv")){
						currentForm.setTipoOperazione("R");
						super.validaInputRangeInventari(mapping, request, currentForm);
						//validazione startInv e endInv
						spVO.setSerie(currentForm.getSerie());
						spVO.setEndInventario(currentForm.getEndInventario());
						spVO.setStartInventario(currentForm.getStartInventario());
					}

					//Ricerca per inventari
					if (folder.equals("Inventari")){
						currentForm.setTipoOperazione("N");
						super.validaInputInventari(mapping, request, currentForm);
						spVO.setListaInventari(currentForm.getListaInventari());
						spVO.setSelezione(currentForm.getSelezione());
						if (currentForm.getSelezione() != null && (currentForm.getSelezione().equals("F"))){
							spVO.setNomeFileAppoggioInv(currentForm.getNomeFileAppoggioInv());
						}
					}
					// almaviva2 Settembre 2017: modifica a Stampa Strumenti Patrimonio per consentire la stampa per una lista di titoli proveniente
					// da file o dalla valorizzazione dei campindi bid sulla mappa
					//Ricerca per Titoli(lista bid o bid in in griglia)
					if (folder.equals("IdentificativiTitoli")){
						super.validaInputIdentificativiTitoli(mapping, request, currentForm);
						currentForm.setTipoOperazione("B");
						spVO.setSelezione(currentForm.getSelezione());
						if (currentForm.getListaBid() != null && currentForm.getListaBid().size() > 0) {
							spVO.setTipoOperazione("");
							spVO.setListaBid(currentForm.getListaBid());
						} else {
							if (currentForm.getListaBidDaFile() != null && currentForm.getListaBidDaFile().size() > 0) {
								spVO.setListaBid(currentForm.getListaBidDaFile());
							}
						}
						if (currentForm.getSelezione() != null && (currentForm.getSelezione().equals("F"))){
							spVO.setNomeFileAppoggioBid(currentForm.getNomeFileAppoggioBid());
						}
					}
				}
				spVO.setCodPolo(currentForm.getCodPolo());
				spVO.setCodBib(currentForm.getCodBib());
				//
				spVO.setTipoOrdinamento(currentForm.getTipoOrdinamento());
				//
				if (currentForm.getCodiceTipoMateriale() != null && !currentForm.getCodiceTipoMateriale().trim().equals("")){
					spVO.setTipoMateriale(currentForm.getCodiceTipoMateriale());
					spVO.setDescrizioneTP(CodiciProvider.cercaDescrizioneCodice(currentForm.getCodiceTipoMateriale(),
							CodiciType.CODICE_NON_DISPONIBILITA,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					spVO.setTipoMateriale("");
					spVO.setDescrizioneTP("");
				}
				if (currentForm.getCodiceStatoConservazione() != null && !currentForm.getCodiceStatoConservazione().trim().equals("")){
					spVO.setStatoConservazione(currentForm.getCodiceStatoConservazione());
					spVO.setDescrizioneSC(CodiciProvider.cercaDescrizioneCodice(currentForm.getCodiceStatoConservazione(),
							CodiciType.CODICE_STATI_DI_CONSERVAZIONE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					spVO.setStatoConservazione("");
					spVO.setDescrizioneSC("");
				}
//				if (currentForm.getCodDigitalizzazione() != null && !currentForm.getCodDigitalizzazione().trim().equals("")){
//					spVO.setDigitalizzazione(currentForm.getCodDigitalizzazione());
//					spVO.setDescrizioneDigitalizzazione(currentForm.getCodDigitalizzazione());
//				}else{
//					spVO.setDigitalizzazione("");
//					spVO.setDescrizioneDigitalizzazione("");
//				}
				spVO.setDescrizioneDigitalizzazione(spVO.getDigitalizzazione());
				if (currentForm.getCodNoDispo() != null && !currentForm.getCodNoDispo().trim().equals("")){
					spVO.setNoDispo(currentForm.getCodNoDispo());
					spVO.setDescrizioneND(CodiciProvider.cercaDescrizioneCodice(currentForm.getCodNoDispo(),
							CodiciType.CODICE_NON_DISPONIBILITA,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					spVO.setNoDispo("");
					spVO.setDescrizioneND("");
				}
				//
				spVO.setCodAttivita(CodiciAttivita.getIstance().GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO);
				spVO.setTipoOperazione(currentForm.getTipoOperazione());
				spVO.setUser(Navigation.getInstance(request).getUtente().getUserId());
			if (currentForm.isModPrel()){
//				spVO.setCodAttivita(CodiciAttivita.getIstance().GDF_MODELLO_PRELIEVO);
				currentForm.setModelloPrelievo(getElencoModelloMP());

				for (int index2 = 0; index2 < currentForm.getModelloPrelievo().size(); index2++) {
					ModelloStampaVO recMod = currentForm.getModelloPrelievo().get(index2);
					if (recMod.getJrxml() != null){
						spVO.setNomeSubReportMP(recMod.getJrxml() + ".jasper");
						currentForm.setTipoModello2(recMod.getJrxml()+".jrxml");
					}
				}
			}
			//
			if (currentForm.isModRegistri()){
				for (int index2 = 0; index2 < currentForm.getElencoModelli().size(); index2++) {
					ModelloStampaVO recMod = currentForm.getElencoModelli().get(index2);
					if (recMod.getJrxml() != null && recMod.getJrxml().equals(currentForm.getTipoModello())){
						spVO.setNomeSubReport(recMod.getSubReports().get(0) + ".jasper");
					}
				}
			}
			parametri.add(spVO);
			request.setAttribute("DatiVo", parametri);
//			if (currentForm.getTipoFormato() != null && currentForm.getTipoFormato().equals("XLS")){
//				if (currentForm.getTipoModello() != null && !currentForm.getTipoModello().trim().endsWith("xls")){
//					ActionMessages errors = new ActionMessages();
//					errors.add("Attenzione", new ActionMessage("error.documentofisico.modelloNonCoincideConFormatoStampa"));
//					this.saveErrors(request, errors);
//					resetToken(request);
//					return mapping.getInputForward();
//				}
//			}else{
//				if (currentForm.getTipoModello() != null && currentForm.getTipoModello().trim().endsWith("xls")){
//					ActionMessages errors = new ActionMessages();
//					errors.add("Attenzione", new ActionMessage("error.documentofisico.modelloNonCoincideConFormatoStampa"));
//					this.saveErrors(request, errors);
//					resetToken(request);
//					return mapping.getInputForward();
//				}
//			}
			//ho finito di preparare il VO, ora lo metto nell'arraylist che passerò alla coda.
			String fileJrxml = currentForm.getTipoModello();//nome modello
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
			//NB: Se voglio memorizzare sul server
			//String pathDownload = basePath+File.separator+"download";
			String pathDownload = StampeUtil.getBatchFilesPath();
			String fileJrxml2 = null;
			String pathJrxml2 = null;
			//Se voglio memorizzare in locale

			//codice standard inserimento messaggio di richiesta stampa differita
			if (currentForm.isModPrel()){
				fileJrxml2 = currentForm.getTipoModello2();//nome modello
				pathJrxml2 = basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml2;
				stampa.setModelloPrelievo(true);
				stampa.setDataPrelievo(dataPrelievo);
				stampa.setMotivoPrelievo(motivoPrelievo);
				stampa.setTemplate2(pathJrxml2);
			}
			stampa.setTipoStampa(currentForm.getTipoFormato());
			stampa.setUser(Navigation.getInstance(request).getUtente().getUserId());
			stampa.setCodPolo(currentForm.getCodPolo());
			stampa.setCodBib(currentForm.getCodBib());

			stampa.setTipoOrdinamento(currentForm.getTipoOrdinamento());
			stampa.setParametri(parametri);
			stampa.setTemplate(pathJrxml);
			stampa.setDownload(pathDownload);
			stampa.setDownloadLinkPath("/");
			stampa.setTipoOperazione(currentForm.getTipoOperazione());
			stampa.setTicket(currentForm.getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stampa.setData(dataCorr);

			String idMessaggio = null;
			if (currentForm.isModRegistri()){
//				if (stam.getTipoRegistro().equals("bollettino")){
//				}else if (stam.getTipoRegistro().equals("conservazione")){
//					stam.setCodAttivita(CodiciAttivita.getIstance().GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO);
//				}else if (stam.getTipoRegistro().equals("possedutoXls")){
//					stam.setCodAttivita(CodiciAttivita.getIstance().GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO);
//				}
				stampa.setCodAttivita(CodiciAttivita.getIstance().GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO);
				//almaviva5_20130709
				spVO.validate(new StampaStrumentiPatrimonioValidator());
				idMessaggio = factory.getStampeOnline().stampaStrumentiPatrimonio(stampa);
			}

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));

			this.saveErrors(request, errors);

			//currentForm.setDisable(true);
			return mapping.getInputForward();
		} catch (ValidationException e) { // altri tipi di errore
			if (e.getErrorCode() != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				this.saveErrors(request, errors);
			}

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
		}
		return mapping.getInputForward();
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
//			return mapping.findForward("indietro");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		StampaStrumentiPatrimonioForm currentForm = ((StampaStrumentiPatrimonioForm) form);
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO, currentForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private void loadTipiOrdinamento(ActionForm form) throws Exception {

		StampaStrumentiPatrimonioForm currentForm = ((StampaStrumentiPatrimonioForm) form);
		List<CodiceVO> lista = new ArrayList<CodiceVO>();
		CodiceVO rec = new CodiceVO("C","sezione + collocazione + specificazione");
		lista.add(rec);
		rec = new CodiceVO("I","serie + inventario");
		lista.add(rec);
		rec = new CodiceVO("D","data di ingresso + serie + inventario");
		lista.add(rec);
		currentForm.setListaTipiOrdinamento(lista);
	}

	private List<ModelloStampaVO> getElencoModelli() {
		List<ModelloStampaVO> listaMod = null;
		try {
			List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO);
			listaMod = new ArrayList<ModelloStampaVO>();
			for (int i = 0; i < listaModelli.size(); i++) {
				ModelloStampaVO recMod = listaModelli.get(i);
				if (recMod.getJrxml()!= null && recMod.getJrxml().equals("default_bollettino")){
					continue;
				}
				listaMod.add(recMod);
			}
			return listaMod;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ModelloStampaVO>();
	}

	private List<ModelloStampaVO> getElencoModelloMP() {
		try {
			List<ModelloStampaVO> listaModelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_MODELLO_PRELIEVO);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ModelloStampaVO>();
	}

}
