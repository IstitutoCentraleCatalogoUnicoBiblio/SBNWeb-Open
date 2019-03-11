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
package it.iccu.sbn.web.actions.documentofisico.elaborazioniDifferite;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioniUltCollSpecVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.SpostamentoCollocazioniVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite.SpostamentoCollocazioniForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.NavigazioneDocFisico;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
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

public class SpostamentoCollocazioniAction extends RicercaInventariCollocazioniAction {

	private static Log log = LogFactory.getLog(SpostamentoCollocazioniAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.lsSezA", "listaSupportoSezArr");
		map.put("documentofisico.lsCollProvv", "listaSupportoCollProvv");
		map.put("documentofisico.lsSpecProvv", "listaSupportoSpecProvv");
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerCollocazione", "selColloc");
		map.put("documentofisico.nessunaSezione", "nessunaSezione");
		map.put("documentofisico.nessunaCollocazione", "nessunaCollocazione");
		map.put("documentofisico.bottone.ok", "prenota");
		map.put("documentofisico.bottone.indietro", "chiudi");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		SpostamentoCollocazioniForm currentForm = (SpostamentoCollocazioniForm) form;
		if (!currentForm.isSessione()) {
			//Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {

			} catch (Exception e) {
				LinkableTagUtils.resetErrors(request);
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
		for (Object o : request.getParameterMap().keySet()) {
			String param = (String) o;
			log.info("param: " + param + " = " + request.getParameter(param));
		}
		if (myForm.isCollProvv() || myForm.isSpecProvv()){
		}else{
			super.unspecified(mapping, myForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		}
		try{
			this.saveToken(request);
			if (Navigation.getInstance(request).isFromBar() ){
				return mapping.getInputForward();
			}
			// controllo se ho già i dati in sessione;
			if(!myForm.isSessione())	{
				myForm.setTicket(Navigation.getInstance(request).getUserTicket());
				myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				request.getSession().setAttribute("lineaFunzionale", "docFisEsame");
				loadDefault(request, mapping, form);
				myForm.setSessione(true);
			}
			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				myForm.setCodBib((String) request.getAttribute("codBib"));
				//
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
			}
			if (!myForm.isCollProvv()){
				if (!myForm.isSpecProvv()){
					myForm.setCodPoloSezArrivo(null);
					myForm.setCodBibSezArrivo(null);
					myForm.setCodSezArrivo(null);
					myForm.setRecSezioneA(null);
					myForm.setCollocazioneProvvisoria(null);
					myForm.setSpecificazioneProvvisoria(null);
				}
			}
			if (myForm.isAllaSez()){
				myForm.setAllaSez(false);
				myForm.setRecSezioneA((SezioneCollocazioneVO)request.getAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA));
				myForm.setCodPoloSezArrivo(myForm.getRecSezioneA().getCodPolo());
				myForm.setCodBibSezArrivo(myForm.getRecSezioneA().getCodBib());
				myForm.setCodSezArrivo(myForm.getRecSezioneA().getCodSezione());
				myForm.setTipoSezioneArrivo(myForm.getRecSezioneA().getTipoSezione());
				myForm.setDisableSezioneDiArrivo(true);
				if (myForm.getRecSezioneA().getTipoSezione() != null
						&& !myForm.getRecSezioneA().getTipoSezione().equals(DocumentoFisicoCostant.COD_TEMPORANEA)){
					myForm.setDisableCollProvv(true);
					myForm.setDisableSpecProvv(true);
					myForm.setDisableTastoCollProvv(true);
					myForm.setDisableTastoSpecifProvv(true);
					myForm.setFlNessunaCollocazione(true);
				}else{
					myForm.setDisableCollProvv(false);
					myForm.setDisableSpecProvv(false);
					myForm.setDisableTastoCollProvv(false);
					myForm.setDisableTastoSpecifProvv(false);
					myForm.setFlNessunaCollocazione(false);
				}
				myForm.setCollocazioneProvvisoria(null);
				myForm.setSpecificazioneProvvisoria(null);
				myForm.setFlNessunaSezione(false);
				//
			}
			//		ritorno da lente dallaCollocazione
			if (myForm.isCollProvv()){
				CollocazioniUltCollSpecVO coll = (CollocazioniUltCollSpecVO)request.getAttribute("scelColl");
				myForm.setCollocazioneProvvisoria(coll.getCodColloc().trim());
				myForm.setSpecificazioneProvvisoria("");
				myForm.setCollProvv(false);
				return mapping.getInputForward();
			}

			//		ritorno da lente Specificazione
			if (myForm.isSpecProvv()){
				CollocazioniUltCollSpecVO spec = (CollocazioniUltCollSpecVO)request.getAttribute("scelColl");
				myForm.setSpecificazioneProvvisoria(spec.getSpecColloc().trim());
				myForm.setSpecProvv(false);
				return mapping.getInputForward();
			}
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}
	public ActionForward prenota(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
			SezioneCollocazioneVO sezioneA = null;
			SezioneCollocazioneVO sezioneP = null;
			try {
				if (myForm.getFolder() != null && !myForm.getFolder().equals("RangeInv")){
					//questi sono controlli che venivano fatti soltanto dal lato ejb
					//e le segnalazioni venivano emesse soltanto nel log
					//sono state inserite anche qui per un riscontro immediato delle incongruenze della richiesta
					sezioneP = this.getSezioneDettaglio(myForm.getCodPoloSez(), myForm.getCodBibSez(),
							myForm.getSezione().trim(),	myForm.getTicket());
					if (sezioneP != null){
						myForm.setRecSezioneP(sezioneP);
						if (sezioneP.getTipoSezione()!= null &&  sezioneP.getTipoSezione().trim().equals("T")){
							if ((myForm.getCodPoloSezArrivo() != null && !myForm.getCodPoloSezArrivo().equals(""))
									&& (myForm.getCodPoloSezArrivo() != null && !myForm.getCodPoloSezArrivo().equals(""))){
								//sezione di arrivo di input presente
								throw new ValidationException("sezPtemporaneaNonIndicareSezA ", ValidationException.errore);
								//							La sezione di partenza è temporanea --> la sezione di arrivo non deve essere indicata
							}else{
								//sezione di arrivo di input non presente
								myForm.setCodPoloSezArrivo(null);
								myForm.setCodBibSezArrivo(null);
								myForm.setRecSezioneA(null);
								//ricollocazione
							}
						}else{
							if ((myForm.getCodPoloSezArrivo() == null)
									&& (myForm.getCodPoloSezArrivo() == null)){
								//sezione di arrivo di input non presente
								throw new ValidationException("sezPdefinitivaSezAobbligatoria ", ValidationException.errore);
								//La sezione di partenza è definitiva --> la sezione di arrivo è obbligatoria
							}else{
								//sezione di arrivo di input presente
								sezioneA = this.getSezioneDettaglio(myForm.getCodPoloSezArrivo(), myForm.getCodBibSezArrivo(),
										myForm.getCodSezArrivo().trim(), myForm.getTicket());
								if (sezioneA != null){
									myForm.setRecSezioneA(sezioneA);
									if (sezioneA.getTipoSezione() != null && sezioneA.getTipoSezione().equals("T")){
										//spostamentoTemporaneo
									}else{
										//le sezioni non sono temporanee, il tipo collocazione di partenza e arrivo
										//devono essere deve coincidere
//										if (sezioneP.getTipoColloc() != sezioneA.getTipoColloc()){
										if (!sezioneP.getTipoColloc().equals(sezioneA.getTipoColloc())){
											if (!sezioneA.getTipoColloc().equals("M")){
												throw new ValidationException("tipoCollocSezPnonCoinciceConTitpoCollocSezA ", ValidationException.errore);
												//Il tipo di collocazione delle sezioni di collocazione di partenza e di arrivo non coincidono
											}
										}else{
											//spostamentoDefinitivo
										}
									}
								}
							}
							if (sezioneA != null){
								if (sezioneP.getCodSezione().trim().equals(sezioneA.getCodSezione().trim())){
									throw new ValidationException("sezPcoincideConSezA ", ValidationException.errore);
									//La sezione di partenza coincide con la sezione di arrivo");
								}
							}
						}
					}
				}
				if (myForm.isFlNessunaSezione()){//ricolloca a sezione definitiva
					//ricollocazione
					myForm.setCodPoloSezArrivo(null);
					myForm.setCodBibSezArrivo(null);
					myForm.setCodSezArrivo(null);
					myForm.setRecSezioneA(null);
					myForm.setCollocazioneProvvisoria(null);
					myForm.setSpecificazioneProvvisoria(null);
					myForm.setDisableCollProvv(false);
					myForm.setDisableSpecProvv(false);
					myForm.setDisableSezioneDiArrivo(false);
				}
				if (myForm.isFlNessunaCollocazione()){
					myForm.setCollocazioneProvvisoria(null);
					myForm.setSpecificazioneProvvisoria(null);
				}
				if (myForm.getCodSezArrivo() == null
						&& (myForm.getSezione() == null
								&& myForm.getSerie() == null)){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.noSelectionPerPrenot"));

				}
				// S ... selezione intervallo di collocazione
				SezioneCollocazioneVO recSezioneP = myForm.getRecSezioneP();
				if (myForm.getFolder().equals("Collocazioni")){
					super.validaInputCollocazioni(mapping, request, myForm);
					myForm.setTipoOperazione("S");
					if (myForm.getRecSezioneP().getTipoSezione().equals("T")){
						if ((myForm.getCodSezArrivo() != null && !myForm.getCodSezArrivo().trim().equals(""))){
							//sezione di arrivo di input presente

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezPTemporanea"));

							myForm.setCodPoloSezArrivo(null);
							myForm.setCodBibSezArrivo(null);
							myForm.setCodSezArrivo(null);
							myForm.setRecSezioneA(null);
							myForm.setCollocazioneProvvisoria(null);
							myForm.setSpecificazioneProvvisoria(null);
							return mapping.getInputForward();
						}
					}else {
						if ((myForm.getCodSezArrivo() == null)){
							//sezione di arrivo di input presente

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezPDefinitiva"));

							return mapping.getInputForward();
						}
					}
					//				if (myForm.getCodPoloSezArrivo() != null && myForm.getCodBibSezArrivo() != null){
					//					if (myForm.getRecSezioneA() != null && !myForm.getRecSezioneA().getTipoColloc().equals("T")){
					//						if (!myForm.getRecSezioneP().getTipoColloc().equals(myForm.getRecSezioneA().getTipoColloc())){
					//
					//							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ilTipoCollocDiPartenzaEDiArrivoNonCoincidono"));
					//
					//							return mapping.getInputForward();
					//						}
					//					}
					//				}
					if (myForm.getCodPoloSezArrivo() != null && myForm.getCodBibSezArrivo() != null){
						if (myForm.getRecSezioneA() != null && !myForm.getRecSezioneA().getTipoSezione().equals("T")){
							if (myForm.getRecSezioneA() != null && !myForm.getRecSezioneA().getTipoColloc().equals("M")){
								if (!myForm.getRecSezioneP().getTipoSezione().equals(myForm.getRecSezioneA().getTipoSezione())){

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.ilTipoCollocDiPartenzaEDiArrivoNonCoincidono"));

									return mapping.getInputForward();
								}
							}
						}
					}
					if (myForm.getSezione() != null && myForm.getCodSezArrivo() != null){
						if (myForm.getSezione().trim().equals(myForm.getCodSezArrivo().trim())){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezioneDiPartenzaESezioneDiArrivoCoincidono"));

							return mapping.getInputForward();
						}
					}
				}
				//Ricerca per range di inventari
				if (myForm.getFolder().equals("RangeInv")){
					super.validaInputRangeInventari(mapping, request, myForm);
					myForm.setTipoOperazione("R");
					//validazione startInv e endInv
					myForm.setSerie(myForm.getSerie());
					myForm.setEndInventario(myForm.getEndInventario());
					myForm.setStartInventario(myForm.getStartInventario());

					//almaviva5_20090923
					//				if (myForm.getCodBibSezArrivo() != null && myForm.getCodBibSezArrivo().equals("")
					//						&& myForm.getCodPoloSezArrivo() == null && myForm.getCodBibSezArrivo() == null){
					boolean checkSez = ValidazioneDati.strIsNull(myForm.getCodBibSezArrivo())
					|| ValidazioneDati.strIsNull(myForm.getCodBibSezArrivo());
					if (!myForm.isFlNessunaSezione() && checkSez) {

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezioneArrivo"));

						return mapping.getInputForward();
					}
				}


				if (myForm.getFolder().equals("Collocazioni")){
					if (myForm.getCodPoloSez() == null && myForm.getCodBibSez() == null){

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezionePartenza"));

						return mapping.getInputForward();
					}
					if (myForm.getRecSezioneP() != null && !myForm.getRecSezioneP().getTipoSezione().equals("T")){
						if (myForm.getCodPoloSezArrivo() == null && myForm.getCodBibSezArrivo() == null){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezioneArrivo"));

							return mapping.getInputForward();
						}
					}
				}
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				// INIZIO PRENOTAZIONE ALLINEAMENTO
				SpostamentoCollocazioniVO spostaColl = new SpostamentoCollocazioniVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				//criteri spostamento
				spostaColl.setCodPolo(myForm.getCodPolo());
				spostaColl.setCodBib(myForm.getCodBib());
				spostaColl.setTipoOperazione(myForm.getTipoOperazione());
				//I
				if (myForm.getFolder().equals("Collocazioni")){
					spostaColl.setCodPoloSez(myForm.getCodPoloSez());
					spostaColl.setCodBibSez(myForm.getCodBibSez());
					spostaColl.setSezione(myForm.getSezione());
					spostaColl.setDallaCollocazione(myForm.getDallaCollocazione());
					spostaColl.setDallaSpecificazione(myForm.getDallaSpecificazione());
					spostaColl.setAllaCollocazione(myForm.getAllaCollocazione());
					spostaColl.setAllaSpecificazione(myForm.getAllaSpecificazione());
					//S - a
					if (recSezioneP != null && !recSezioneP.getTipoSezione().equals("T")){
						if (myForm.getCodSezArrivo() != null && myForm.getCodSezArrivo().trim().equals("")){
							spostaColl.setCodPoloSezArr(myForm.getCodPolo());
							spostaColl.setCodBibSezArr(myForm.getCodBib());
						}else{
							spostaColl.setCodPoloSezArr(myForm.getCodPoloSezArrivo());
							spostaColl.setCodBibSezArr(myForm.getCodBibSezArrivo());
						}
						spostaColl.setCodSezArr(myForm.getCodSezArrivo());
					}else{
						spostaColl.setCodPoloSezArr(null);
						spostaColl.setCodBibSezArr(null);
						spostaColl.setCodSezArr(null);
					}
				}
				if (myForm.getFolder().equals("RangeInv")){
					spostaColl.setSerie(myForm.getSerie());
					spostaColl.setStartInventario(myForm.getStartInventario());
					spostaColl.setEndInventario(myForm.getEndInventario());
					if (myForm.getCodSezArrivo() != null && myForm.getCodSezArrivo().trim().equals("")){
						spostaColl.setCodPoloSezArr(myForm.getCodPolo());
						spostaColl.setCodBibSezArr(myForm.getCodBib());
					}else{
						spostaColl.setCodPoloSezArr(myForm.getCodPoloSezArrivo());
						spostaColl.setCodBibSezArr(myForm.getCodBibSezArrivo());
					}
					spostaColl.setCodSezArr(myForm.getCodSezArrivo());
				}
				if (myForm.isFlNessunaCollocazione()){
					spostaColl.setCollProvv(null);
					spostaColl.setSpecProvv(null);
				}else{
					if (myForm.getCollocazioneProvvisoria() != null){
						spostaColl.setCollProvv(myForm.getCollocazioneProvvisoria());
						spostaColl.setSpecProvv(myForm.getSpecificazioneProvvisoria());
					}
				}
				//
				if (myForm.isPrestito()){
					spostaColl.setPrestito(DocumentoFisicoCostant.PRESTITO);
				}else{
					spostaColl.setPrestito(null);
				}
				//
				String basePath=this.servlet.getServletContext().getRealPath(File.separator);
				spostaColl.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				log.info("download path: " + downloadPath);
				//
				if (myForm.isRistampa()){
					spostaColl.setEtichette(DocumentoFisicoCostant.ETICHETTE);
					ModelloDefaultVO modello = this.getModelloDefault(Navigation.getInstance(request).getUtente().getCodPolo(),
							myForm.getCodBib(), Navigation.getInstance(request).getUtente().getTicket());
					if (modello == null){
						spostaColl.setCodModello("");
						spostaColl.setTipoFormato(TipoStampa.HTML.name());
						spostaColl.setDescrBibEtichetta("");
					}else{
						spostaColl.setCodModello(modello.getCodModello());
						spostaColl.setTipoFormato(modello.getFormatoStampa());
						if (modello.getDescrBibModello() != null && !modello.getDescrBibModello().trim().equals("")){
							spostaColl.setDescrBibEtichetta(modello.getDescrBibModello());
						}else{
							spostaColl.setDescrBibEtichetta(myForm.getDescrBib());
						}
					}
					spostaColl.setNumCopie(myForm.getNumCopie());
					//ho finito di preparare il VO con i dati della richiesta utente, ora lo metto nell'arraylist che passerò alla coda.
					List<SpostamentoCollocazioniVO> parametri=new ArrayList<SpostamentoCollocazioniVO>();
					parametri.add(spostaColl);
					request.setAttribute("DatiVo", parametri);
					//codice standard inserimento messaggio di richiesta stampa differita
					StampaDiffVO stam = new StampaDiffVO();
					stam.setTipoStampa(spostaColl.getTipoFormato());
					stam.setUser(Navigation.getInstance(request).getUtente().getUserId());
					stam.setCodPolo(spostaColl.getCodPolo());
					stam.setCodBib(spostaColl.getCodBib());
					stam.setTipoOrdinamento("");
					stam.setParametri(parametri);
					stam.setTemplate(spostaColl.getCodModello());
					stam.setNumCopie(spostaColl.getNumCopie());
					stam.setDownload(downloadPath);
					stam.setDownloadLinkPath("/");
					stam.setUser(Navigation.getInstance(request).getUtente().getUserId());
					stam.setCodAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE);
					stam.setTipoOperazione("STAMPA_ETICHETTE");
					stam.setTicket(spostaColl.getTicket());
					UtilityCastor util= new UtilityCastor();
					String dataCorr = util.getCurrentDate();
					stam.setData(dataCorr);
					spostaColl.setStampaDiffEtichette(stam);
					//
				}else{
					spostaColl.setStampaDiffEtichette(null);
					spostaColl.setEtichette(null);
				}
				//
				String downloadLinkPath = "/";
				spostaColl.setDownloadPath(downloadPath);
				spostaColl.setDownloadLinkPath(downloadLinkPath);
				spostaColl.setTicket(Navigation.getInstance(request).getUserTicket());
				spostaColl.setUser(Navigation.getInstance(request).getUtente().getUserId());
				spostaColl.setCodAttivita(CodiciAttivita.getIstance().GDF_SPOSTAMENTO_COLLOCAZIONI);
				String s = factory.getElaborazioniDifferite().spostaCollocazioni(spostaColl);
				//
				//
				if (s == null) {

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.prenotSpostamentoCollocazioniFallita"));

					resetToken(request);
					return mapping.getInputForward();
				}


				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.prenotSpostamentoCollocazioniOk", s.toString()));

				resetToken(request);
				myForm.setDisable(true);
				return mapping.getInputForward();

			} catch (ValidationException e) { // altri tipi di errore
				log.error("", e);
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				resetToken(request);
				return mapping.getInputForward();

		} catch (DataException e) {
			log.error("", e);
			if (sezioneP == null){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezPNonEsistente"));

				myForm.setAllaSez(false);
				myForm.setSezANonEsiste(true);
				return this.listaSupportoSezArr(mapping, form,	request, response);
			}
			if (sezioneA == null){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezANonEsistente"));

				myForm.setAllaSez(false);
				myForm.setSezANonEsiste(true);
				return this.listaSupportoSezArr(mapping, form,	request, response);
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			this.saveToken(request);
				return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			resetToken(request);
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try{
//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_SPOSTAMENTO_COLLOCAZIONI, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoSezArr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
		SezioneCollocazioneVO sezione = null;
		myForm.setAllaSez(true);
		try {
			if (myForm.isSezANonEsiste()){
				myForm.setSezANonEsiste(false);
				request.setAttribute("chiamante",mapping.getPath());
				request.setAttribute("codBib",myForm.getCodBib());
				request.setAttribute("descrBib",myForm.getDescrBib());
				return mapping.findForward("lenteSez");
			}else{
				if (myForm.getCodPoloSezArrivo() == null && myForm.getCodBibSezArrivo() == null && myForm.getCodSezArrivo().length()>0
						|| myForm.getCodPoloSezArrivo() != null && myForm.getCodBibSezArrivo() != null && myForm.getCodSezArrivo().trim().length()>0){
					//ho digitato la sezione senza passare dalla lista
					//imposto i parametri e faccio get della sezione
					sezione = this.getSezioneDettaglio(myForm.getCodPolo(), myForm.getCodBib(), myForm.getCodSezArrivo().trim(), myForm.getTicket());
					myForm.setAllaSez(true);
					myForm.setSezANonEsiste(true);
					//se la sezione esiste imposto NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA e faccio return "this.unspecified(mapping, form,	request, response)"
					if (sezione != null && myForm.isSezANonEsiste()){
						request.setAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA, sezione);
						myForm.setSezANonEsiste(false);
						return this.unspecified(mapping, form, request, response);
					}else{
						request.setAttribute("chiamante",mapping.getPath());
						request.setAttribute("codBib",myForm.getCodBib());
						request.setAttribute("descrBib",myForm.getDescrBib());
						return mapping.findForward("lenteSez");
					}
				}else{
					myForm.setAllaSez(true);
					myForm.setSezANonEsiste(false);
					request.setAttribute("chiamante",mapping.getPath());
					request.setAttribute("codBib",myForm.getCodBib());
					request.setAttribute("descrBib",myForm.getDescrBib());
					return mapping.findForward("lenteSez");
				}
			}
		} catch (DataException e) {
			if (sezione == null){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.sezNonEsistente"));

				myForm.setAllaSez(false);
				myForm.setSezANonEsiste(true);
				return this.listaSupportoSezArr(mapping, form,	request, response);
			}

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward nessunaSezione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
		myForm.setFlNessunaSezione(true);
		myForm.setCodPoloSezArrivo(null);
		myForm.setCodBibSezArrivo(null);
		myForm.setCodSezArrivo(null);
		myForm.setRecSezioneA(null);
		myForm.setFlNessunaCollocazione(true);
		myForm.setDisableCollProvv(false);
		myForm.setDisableSpecProvv(false);
		return mapping.getInputForward();
	}
	public ActionForward listaSupportoCollProvv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			myForm.setCollProvv(false);
			return mapping.getInputForward();
		}
		try {
			EsameCollocRicercaVO ricerca =  new EsameCollocRicercaVO();
			ricerca.setCodPolo(myForm.getCodPolo());
			ricerca.setCodBib(myForm.getCodBib());
			ricerca.setCodPoloSez(myForm.getCodPoloSezArrivo());
			ricerca.setCodBibSez(myForm.getCodBibSezArrivo());
			ricerca.setCodSez(myForm.getCodSezArrivo());
			ricerca.setCodLoc(myForm.getCollocazioneProvvisoria().trim());
			ricerca.setCodSpec("");
			ricerca.setASpec("");
			request.setAttribute("chiamante", mapping.getPath());
			request.setAttribute("codBib",myForm.getCodBib());
			request.setAttribute("descrBib",myForm.getDescrBib());
			ricerca.setOrdLst("CA");
			request.setAttribute("paramRicerca", ricerca);
			request.setAttribute("listaCollSupp","listaCollSupp");
			myForm.setCollProvv(true);
			return mapping.findForward("lenteColloc");

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);
		}
	}

	public ActionForward listaSupportoSpecProvv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() ){
			myForm.setSpecProvv(false);
			return mapping.getInputForward();
		}
		try {
			EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
			ricerca.setCodPolo(myForm.getCodPolo());
			ricerca.setCodBib(myForm.getCodBib());
			ricerca.setCodPoloSez(myForm.getCodPoloSezArrivo());
			ricerca.setCodBibSez(myForm.getCodBibSezArrivo());
			ricerca.setCodSez(myForm.getCodSezArrivo());
			ricerca.setCodLoc(myForm.getCollocazioneProvvisoria().trim());
			ricerca.setCodSpec(myForm.getSpecificazioneProvvisoria().trim());
			ricerca.setCodPolo(myForm.getCodPolo());
			ricerca.setCodBib(myForm.getCodBib());
			request.setAttribute("chiamante",mapping.getPath());
			request.setAttribute("codBib",myForm.getCodBib());
			request.setAttribute("descrBib",myForm.getDescrBib());
			request.setAttribute("listaSpecSupp","listaSpecSupp");
			request.setAttribute("listaSpecSuppProvv","listaSpecSuppProvv");
			ricerca.setOrdLst("CA");
			request.setAttribute("paramRicerca", ricerca);
			myForm.setSpecProvv(true);
			return mapping.findForward("lenteSpecif");

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
	}

	public ActionForward nessunaCollocazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SpostamentoCollocazioniForm myForm = (SpostamentoCollocazioniForm) form;
		myForm.setCollocazioneProvvisoria("");
		myForm.setSpecificazioneProvvisoria("");
		myForm.setFlNessunaCollocazione(true);
		return mapping.getInputForward();
	}
	private SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SezioneCollocazioneVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}
	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}



}
