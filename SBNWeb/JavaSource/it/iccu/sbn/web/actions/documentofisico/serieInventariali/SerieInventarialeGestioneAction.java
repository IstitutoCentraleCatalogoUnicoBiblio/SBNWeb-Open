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
package it.iccu.sbn.web.actions.documentofisico.serieInventariali;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.web.actionforms.documentofisico.serieInventariali.SerieInventarialeGestioneForm;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
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
import org.apache.struts.actions.LookupDispatchAction;

public class SerieInventarialeGestioneAction extends LookupDispatchAction {

	private static Log log = LogFactory
	.getLog(SerieInventarialeGestioneAction.class);

	@Override
	protected Map getKeyMethodMap() {
		Map map = new HashMap();

		map.put("documentofisico.bottone.salva", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("documentofisico.bottone.cancIntervallo", "intervalloCanc");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try{
			this.saveToken(request);
			SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm)form;
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();
			// controllo se ho già i dati in sessione;
			if(!myForm.isSessione()){
				myForm.setTicket(Navigation.getInstance(request).getUserTicket());
				myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				myForm.setSessione(true);
			}
			if (request.getAttribute("codBib") != null && request.getAttribute("descrBib") != null){
				myForm.setCodBib((String)request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
				if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("nuova")) {
					myForm.setProv("nuova");
					ModelloDefaultVO modello = this.getModelloDefault(myForm.getCodPolo(), myForm.getCodBib(), myForm.getTicket());
					if (modello != null){
						if (ValidazioneDati.isFilled(modello.getN_serie()) && modello.getN_serie().equals("0")){
							if (myForm.getRecSerie().getCodSerie() != null && myForm.getRecSerie().getCodSerie().trim().equals("")){
								SerieVO serieDettaglio = this.getSerieDettaglio(myForm.getCodPolo(), myForm.getCodBib(),
										myForm.getRecSerie().getCodSerie(), myForm.getTicket());
								if (serieDettaglio != null){
									ActionMessages errors = new ActionMessages();
									errors.add("generico", new ActionMessage("error.documentofisico.laBibliotecaGestisceSoloLaSerieASpazio"));
									this.saveErrors(request, errors);
									return mapping.getInputForward();
								}else{
									myForm.setDisableSerie(true);
								}
							}else{
								myForm.setDisableSerie(true);
							}
						}else if (ValidazioneDati.isFilled(modello.getN_serie()) && modello.getN_serie().equals("1")){
							//la biblioteca gestisce più serie oltre quella a spazio: continua la gestione
						}
					}else{
						//la biblioteca non ha impostato i dati di configurazione
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.recModelloDefaultInesistente"));
						this.saveErrors(request, errors);
						return Navigation.getInstance(request).goBack(true);
					}
					myForm.setDisable(false);
					SerieVO serie = new SerieVO();
					serie.setCodPolo(myForm.getCodPolo());
					serie.setCodBib(myForm.getCodBib());
					serie.setCodSerie("");
//					serie.setCodSerie(null);
					myForm.setFlChiusa(false);
					myForm.setFlDefault(false);
					serie.setDescrSerie("");
					serie.setProgAssInv("0");
					serie.setPregrAssInv("900000000");
					serie.setNumMan("0");
					serie.setDataIngrPregr("");
					serie.setDataIngrMan("");
					serie.setInizioMan1("0");
					serie.setFineMan1("0");
					serie.setDataIngrRis1("");
					serie.setInizioMan2("0");
					serie.setFineMan2("0");
					serie.setDataIngrRis2("");
					serie.setInizioMan3("0");
					serie.setFineMan3("0");
					serie.setDataIngrRis3("");
					serie.setInizioMan4("0");
					serie.setFineMan4("0");
					serie.setDataIngrRis4("");
					serie.setBuonoCarico("");
//
//
					serie.setUteIns(Navigation.getInstance(request).getUtente().getUserId());
					serie.setUteAgg(Navigation.getInstance(request).getUtente().getUserId());
					myForm.setRecSerie(serie);
					//richiamo la lista delle serie della biblioteca
					List listaSerie = this.getListaSerie(myForm.getCodPolo(), myForm.getCodBib(), myForm.getTicket(), myForm);
					myForm.setIntervalloSelez("0");
					return mapping.getInputForward();
				}
				if (request.getAttribute("codSerie") != null) {
					myForm.getRecSerie().setCodSerie((String)request.getAttribute("codSerie"));
					SerieVO serieDettaglio = this.getSerieDettaglio(myForm.getCodPolo(), myForm.getCodBib(),
							myForm.getRecSerie().getCodSerie(), myForm.getTicket());
					if (serieDettaglio != null){
						myForm.setRecSerieOld((SerieVO) serieDettaglio.copy());
						myForm.setRecSerie(serieDettaglio);
						if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("modifica")) {
							myForm.setProv("modifica");
							myForm.setDisable(false);
							myForm.setDisableSerie(true);
							myForm.setDisableBuonoCarico(true);
							if ((myForm.getRecSerie().getInizioMan1() != null
									&& (myForm.getRecSerie().getInizioMan1().equals("0") || myForm.getRecSerie().getInizioMan1().trim().equals(""))
									&& myForm.getRecSerie().getFineMan1() != null && myForm.getRecSerie().getFineMan1().equals("0") || myForm.getRecSerie().getFineMan1().trim().equals(""))

									|| (myForm.getRecSerie().getInizioMan2() != null
											&& (myForm.getRecSerie().getInizioMan2().equals("0") || myForm.getRecSerie().getInizioMan2().trim().equals(""))
											&& myForm.getRecSerie().getFineMan2() != null && myForm.getRecSerie().getFineMan2().equals("0") || myForm.getRecSerie().getFineMan2().trim().equals(""))

											|| (myForm.getRecSerie().getInizioMan3() != null
													&& (myForm.getRecSerie().getInizioMan3().equals("0") || myForm.getRecSerie().getFineMan3().trim().equals(""))
													&& myForm.getRecSerie().getFineMan3() != null && myForm.getRecSerie().getFineMan3().equals("0") || myForm.getRecSerie().getFineMan3().trim().equals(""))

													|| (myForm.getRecSerie().getInizioMan4() != null
															&& (myForm.getRecSerie().getInizioMan4().equals("0") || myForm.getRecSerie().getInizioMan4().trim().equals(""))
															&& myForm.getRecSerie().getFineMan4() != null && myForm.getRecSerie().getFineMan4().equals("0")) || myForm.getRecSerie().getFineMan4().trim().equals("")){
								myForm.setIntervalloSelez("0");
							}
						} else if ((request.getAttribute("prov") != null && request.getAttribute("prov").equals("esamina"))){
							myForm.setProv("esamina");
							myForm.setDisableSerie(true);
							myForm.setDate(true);
							myForm.setDisableBuonoCarico(true);
							myForm.setEsamina(true);
							myForm.setDisable(true);
							myForm.setIntervalloSelez("0");
						}
						if (myForm.getRecSerie().getFlChiusa().equals("1")){
							myForm.setFlChiusa(true);
						}else{
							myForm.setFlChiusa(false);
						}
						if (myForm.getRecSerie().getFlDefault().equals("1")){
							myForm.setFlDefault(true);
						}else{
							myForm.setFlDefault(false);
						}
					}
				}
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (DataException e) {
			if (e.getMessage().equals("recInesistente")){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}else if (e.getMessage().equals("recModelloDefaultInesistente")){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm) form;
		myForm.setIntervalloSelez("0");
		try {
			this.controllaDatiInput(mapping, request, myForm);

			//insert serie
			if (myForm.getProv().equals("nuova")){
				myForm.getRecSerie().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());
				if (this.insertSerie(myForm.getRecSerie(), myForm.getTicket())){
					request.setAttribute("codBib",myForm.getCodBib());
					request.setAttribute("descrBib",myForm.getDescrBib());
					return Navigation.getInstance(request).goBack();
				}
			}else if (myForm.getProv().equals("modifica")){
				//richiesta conferma update serie
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaModifica"));
				this.saveErrors(request, errors);
				this.saveToken(request);

				myForm.setConferma(true);
				myForm.setDisable(true);
				return mapping.getInputForward();
			}
		}	catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}	catch (DataException e) {
			if (e.getMessage().equals("recEsistente")){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
				this.saveErrors(request, errors);
				return Navigation.getInstance(request).goBack(true);
			}

			// Bug esercizio 6657 - Manutenzione almaviva2. Luglio 2018 - INIZIO
			// In caso di inserimento del valore 899999999 nei 2 campi "Progressivo per Assegnazione Automatica"
			// e "Ultimo Numero di Inventario assegnato prima dell'Automazione", non si deve inibire l'operazione
			// ma inviare diagnostico esplicativo e richiedere la conferma esplicita dell'operazione; a quel punto si
			// consente l'inserimento della nuova serie con il valore in oggetto
			if (e.getMessage().equals("progrAutNonPuoEssereUgualePregressoRichConferma")){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setConferma(true);
				myForm.setDisable(true);
				return mapping.getInputForward();
			}
			// Bug esercizio 6657 - Manutenzione almaviva2. Luglio 2018 - FINE
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm) form;
		myForm.setIntervalloSelez("0");
		try {
			if (myForm.getProv() != null && (myForm.getProv().equals("nuova") || myForm.getProv().equals("modifica"))) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaModifica"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setDisable(true);
				myForm.setConferma(true);
				return mapping.getInputForward();
			} else {
				return Navigation.getInstance(request).goBack(true);
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm) form;
		try {
			if (myForm.getProv().equals("modifica")){
//				controllaDatiInput(mapping, request, myForm);
				if (this.updateSerie(myForm.getRecSerie(), myForm.getTicket())){
					request.setAttribute("codBib",myForm.getCodBib());
					request.setAttribute("descrBib",myForm.getDescrBib());
					return Navigation.getInstance(request).goBack();
				}
			}
			if (myForm.isConferma()) {
				return this.ok(mapping, form, request, response);
			} else{
				myForm.setDisable(false);
				myForm.setConferma(false);
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + e.getMessage()));
			this.saveErrors(request, errors);
			myForm.setDisable(false);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	  * it.iccu.sbn.web.actions.documentofisico.serieInventariali
	  * SerieInventarialeGestioneAction.java
	  * controllaDatiInput
	  * void
	  * @param mapping
	  * @param request
	  * @param myForm
	  * @throws NumberFormatException
	 * @throws DataException
	  *
	  *
	 */
	private void controllaDatiInput(ActionMapping mapping, HttpServletRequest request, SerieInventarialeGestioneForm myForm)
	throws NumberFormatException, DataException {
		int ritorno = 0;
		boolean ret = true;
		if (myForm.isFlChiusa()){
			myForm.getRecSerie().setFlChiusa("1");
		}else{
			myForm.getRecSerie().setFlChiusa("0");
		}
		if (myForm.isFlDefault()){
			myForm.getRecSerie().setFlDefault("1");
		}else{
			myForm.getRecSerie().setFlDefault("0");
		}
		myForm.getRecSerie().setUteAgg(Navigation.getInstance(request).getUtente().getFirmaUtente());

		//		//progressivo automatico
		if (myForm.getRecSerie().getProgAssInv() != null){
			if (myForm.getRecSerie().getProgAssInv().trim().equals("")){
				myForm.getRecSerie().setProgAssInv("0");
			}
		}
		if (myForm.getProv() != null && myForm.getProv().equals("modifica")){
			if (myForm.getRecSerieOld().getProgAssInv() != null){
				if (!myForm.getRecSerieOld().getProgAssInv().equals(myForm.getRecSerie().getProgAssInv())){
					if (Integer.parseInt(myForm.getRecSerieOld().getProgAssInv()) > (Integer.parseInt(myForm.getRecSerie().getProgAssInv()))){
						throw new DataException("progrAutNonPuoEssereMinoreDellUltimoAssegnato");
					}
				}
			}

			if (myForm.getRecSerieOld().getPregrAssInv() != null){
				if (!myForm.getRecSerieOld().getPregrAssInv().equals(myForm.getRecSerie().getPregrAssInv())){
					if (Integer.parseInt(myForm.getRecSerieOld().getPregrAssInv()) > (Integer.parseInt(myForm.getRecSerie().getPregrAssInv()))){
						throw new DataException("progrPregrNonPuoEssereMinoreDellUltimoAssegnato");
					}
				}
			}
		}
		if (myForm.getRecSerie().getPregrAssInv() != null && myForm.getRecSerie().getPregrAssInv().trim().length() > 0){
			if(Integer.parseInt(myForm.getRecSerie().getPregrAssInv() ) < DocumentoFisicoCostant.MIN_PROGRESSIVO_INV_PREGRESSO){
				throw new DataException("errPregrAssInvMinore");
			}
		}

		//almaviva5_20101227
		int prgPreInv = ValidazioneDati.isFilled(myForm.getRecSerie().getPregrAssInv()) ? Integer.parseInt(myForm.getRecSerie().getPregrAssInv()) : 0;
		int prgAssInv = ValidazioneDati.isFilled(myForm.getRecSerie().getProgAssInv()) ? Integer.parseInt(myForm.getRecSerie().getProgAssInv()) : 0;
		if ( (prgAssInv + 1) >= prgPreInv) {

			// Bug esercizio 6657 - Manutenzione almaviva2. Luglio 2018 - INIZIO
			// In caso di inserimento del valore 899999999 nei 2 campi "Progressivo per Assegnazione Automatica"
			// e "Ultimo Numero di Inventario assegnato prima dell'Automazione", non si deve inibire l'operazione
			// ma inviare diagnostico esplicativo e richiedere la conferma esplicita dell'operazione; a quel punto si
			// consente l'inserimento della nuova serie con il valore in oggetto
			//throw new DataException("progrAutNonPuoEssereUgualePregresso");
			if (!myForm.isConferma()) {
				throw new DataException("progrAutNonPuoEssereUgualePregressoRichConferma");
			}
			// Bug esercizio 6657 - Manutenzione almaviva2. Luglio 2018 - FINE
		}

		//progressivo manuale
		if (myForm.getRecSerie().getNumMan() != null){
			if (myForm.getRecSerie().getNumMan().trim().equals("")){
				myForm.getRecSerie().setNumMan("0");
			}
			if(Integer.parseInt(myForm.getRecSerie().getNumMan()) > Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
				throw new DataException("errNumManMaggiore");
			}
		}
		controllaDateRiservate(myForm);
		controllaIntervalliRiservati(myForm);
	}

	private void controllaDateRiservate(SerieInventarialeGestioneForm myForm)
			throws DataException {
		Timestamp dataMan = null;
		Timestamp dataIntRis1 = null;
		Timestamp dataIntRis2 = null;
		Timestamp dataIntRis3 = null;
		Timestamp dataIntRis4 = null;
		if (myForm.getRecSerie().getDataIngrMan() != null && !myForm.getRecSerie().getDataIngrMan().trim().equals("")){
			dataMan = DateUtil.toTimestamp(myForm.getRecSerie().getDataIngrMan());
		}
		if (myForm.getRecSerie().getDataIngrRis1() != null && !myForm.getRecSerie().getDataIngrRis1().trim().equals("")){
			dataIntRis1 = DateUtil.toTimestamp(myForm.getRecSerie().getDataIngrRis1());
		}
		if (myForm.getRecSerie().getDataIngrRis2() != null && !myForm.getRecSerie().getDataIngrRis2().trim().equals("")){
			dataIntRis2 = DateUtil.toTimestamp(myForm.getRecSerie().getDataIngrRis2());
		}
		if (myForm.getRecSerie().getDataIngrRis3() != null && !myForm.getRecSerie().getDataIngrRis3().trim().equals("")){
			dataIntRis3 = DateUtil.toTimestamp(myForm.getRecSerie().getDataIngrRis3());
		}
		if (myForm.getRecSerie().getDataIngrRis4() != null && !myForm.getRecSerie().getDataIngrRis4().trim().equals("")){
			dataIntRis4 = DateUtil.toTimestamp(myForm.getRecSerie().getDataIngrRis4());
		}
		//data man
		//Se indicata deve essere minore di tutte le ulteriori date
		//convenzionali definite per gli altri intervalli riservati.
		if (myForm.getRecSerie().getDataIngrMan() != null && !myForm.getRecSerie().getDataIngrMan().trim().equals("") && dataMan != null){
			if (dataIntRis1 != null){
				if (dataMan.after(dataIntRis1)){
					throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
				}
			}

			if (dataIntRis2 != null){
				if (dataMan.after(dataIntRis2)){
					throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
				}
			}
			if (dataIntRis3 != null){
				if (dataMan.after(dataIntRis3)){
					throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
				}
			}
			if (dataIntRis4 != null){
				if (dataMan.after(dataIntRis4)){
					throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
				}
			}

		}
		//data primo intervallo riservato
		/*
		 * Se indicata deve essere maggiore o uguale di  dt_ingr_inv_man
		 * e minore o uguale delle ulteriori date convenzionali definite
		 * per altri intervalli riservati con numerazione più alta
		 */
		if (myForm.getRecSerie().getDataIngrRis1() != null && !myForm.getRecSerie().getDataIngrRis1().trim().equals("") && dataIntRis1 != null){
			if (dataIntRis2 != null){
				if (dataIntRis1.after(dataIntRis2)){
					throw new DataException("dataIntRis1DeveEssereInferioreAlleDateDegliIntSuperiori");
				}
			}

			if (dataIntRis3 != null){
				if (dataIntRis1.after(dataIntRis3)){
					throw new DataException("dataIntRis1DeveEssereInferioreAlleDateDegliIntSuperiori");
				}
			}
			if (dataIntRis4 != null){
				if (dataIntRis1.after(dataIntRis4)){
					throw new DataException("dataIntRis1DeveEssereInferioreAlleDateDegliIntSuperiori");
				}
			}
		}
		//data secondo intervallo riservato
		/*
		 * Se indicata deve essere non minore di  dt_ingr_inv_man
		 * e non maggiore delle ulteriori date convenzionali definite
		 * per altri intervalli riservati con numerazione più alta
		 */
		if (myForm.getRecSerie().getDataIngrRis2() != null && !myForm.getRecSerie().getDataIngrRis2().trim().equals("") && dataIntRis2 != null){
			if (dataIntRis1 != null){
				if (dataIntRis1.after(dataIntRis2)){
					throw new DataException("dataIntRis2DeveEssereSuperioreAlleDateDegliIntInferiori");
				}
			}
			if (dataIntRis3 != null){
				if (dataIntRis2.after(dataIntRis3)){
					throw new DataException("dataIntRis2DeveEssereInferioreAlleDateDegliIntSuperiori");
				}
			}
			if (dataIntRis4 != null){
				if (dataIntRis2.after(dataIntRis4)){
					throw new DataException("dataIntRis2DeveEssereInferioreAlleDateDegliIntSuperiori");
				}
			}
		}
		//data terzo intervallo riservato
		/*
		 * Se indicata deve essere non minore di  dt_ingr_inv_man
		 * e non maggiore delle ulteriori date convenzionali definite
		 * per altri intervalli riservati con numerazione più alta
		 */
		if (myForm.getRecSerie().getDataIngrRis3() != null && !myForm.getRecSerie().getDataIngrRis3().trim().equals("") && dataIntRis3 != null){
			if (dataIntRis1 != null){
				if (dataIntRis1.after(dataIntRis3)){
					throw new DataException("dataIntRis3DeveEssereSuperioreAlleDateDegliIntInferiori");
				}
			}
			if (dataIntRis2 != null){
				if (dataIntRis2.after(dataIntRis3)){
					throw new DataException("dataIntRis3DeveEssereSuperioreAlleDateDegliIntInferiori");
				}
			}
			if (dataIntRis4 != null){
				if (dataIntRis3.after(dataIntRis4)){
					throw new DataException("dataIntRis3DeveEssereInferioreAlleDateDegliIntSuperiori");
				}
			}
		}

		//data quarto intervallo riservato
		/*
		 * Se indicata deve essere non minore di  dt_ingr_inv_man
		 * e non maggiore delle ulteriori date convenzionali definite
		 * per altri intervalli riservati con numerazione più alta
		 */
		if (myForm.getRecSerie().getDataIngrRis4() != null && !myForm.getRecSerie().getDataIngrRis4().trim().equals("") && dataIntRis4 != null){
			if (dataIntRis1 != null){
				if (dataIntRis1.after(dataIntRis4)){
					throw new DataException("dataIntRis4DeveEssereSuperioreAlleDateDegliIntInferiori");
				}
			}
			if (dataIntRis2 != null){
				if (dataIntRis2.after(dataIntRis4)){
					throw new DataException("dataIntRis4DeveEssereSuperioreAlleDateDegliIntInferiori");
				}
			}
			if (dataIntRis3 != null){
				if (dataIntRis3.after(dataIntRis4)){
					throw new DataException("dataIntRis4DeveEssereSuperioreAlleDateDegliIntInferiori");
				}
			}
		}
	}

	private void controllaIntervalliRiservati(
			SerieInventarialeGestioneForm myForm) throws DataException {
		int i1 = 0;
		int f1 = 0;
		int i2 = 0;
		int f2 = 0;
		int i3 = 0;
		int f3 = 0;
		int i4 = 0;
		int f4 = 0;
		if (myForm.getRecSerie().getInizioMan1() != null){
			if (Integer.valueOf(myForm.getRecSerie().getInizioMan1()) > 0){
				if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getInizioMan1())){
					i1 = Integer.valueOf(myForm.getRecSerie().getInizioMan1());
				}else {
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				i1 = Integer.MAX_VALUE;
			}
		}
		if (myForm.getRecSerie().getFineMan1() != null){
			if (Integer.valueOf(myForm.getRecSerie().getFineMan1()) > 0){
				if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getFineMan1())){
					f1 = Integer.valueOf(myForm.getRecSerie().getFineMan1());
				}else {
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				f1 = Integer.MAX_VALUE;
			}
		}
		if (myForm.getRecSerie().getInizioMan2() != null) {
			if (Integer.valueOf(myForm.getRecSerie().getInizioMan2()) > 0){
				if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getInizioMan2())){
					i2 = Integer.valueOf(myForm.getRecSerie().getInizioMan2());
				}else {
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				i2 = Integer.MAX_VALUE;
			}
}
		if (myForm.getRecSerie().getFineMan2() != null){
			if (Integer.valueOf(myForm.getRecSerie().getFineMan2()) > 0){
			if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getFineMan2())){
					f2 = Integer.valueOf(myForm.getRecSerie().getFineMan2());
				}else {
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				f2 = Integer.MAX_VALUE;
			}
		}
		if (myForm.getRecSerie().getInizioMan3() != null){
			if (Integer.valueOf(myForm.getRecSerie().getInizioMan3()) > 0){
			if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getInizioMan3())){
					i3 = Integer.valueOf(myForm.getRecSerie().getInizioMan3());
				}else {
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				i3 = Integer.MAX_VALUE;
			}
		}
		if (myForm.getRecSerie().getFineMan3() != null){
			if (Integer.valueOf(myForm.getRecSerie().getFineMan3()) > 0){
			if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getFineMan3())){
					f3 = Integer.valueOf(myForm.getRecSerie().getFineMan3());
				}else {
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				f3 = Integer.MAX_VALUE;
			}
		}
		if (myForm.getRecSerie().getInizioMan4() != null){
			if (Integer.valueOf(myForm.getRecSerie().getInizioMan4()) > 0){
			if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getInizioMan4())){
					i4 = Integer.valueOf(myForm.getRecSerie().getInizioMan4());
				}else{
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				i4 = Integer.MAX_VALUE;
			}
		}
		if (myForm.getRecSerie().getFineMan4() != null){
			if (Integer.valueOf(myForm.getRecSerie().getFineMan4()) > 0 ){
				if (ValidazioneDati.strIsNumeric(myForm.getRecSerie().getFineMan4())){
					f4 = Integer.valueOf(myForm.getRecSerie().getFineMan4());
				}else {
					throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
				}
			}else{
				f4 = Integer.MAX_VALUE;
			}
		}
		//numero inizio primo intervallo riservato
		// Se indicato deve essere maggiore di num_man,
		if (i1 != Integer.MAX_VALUE){
			if(i1 >= (Integer.parseInt(myForm.getRecSerie().getNumMan()))){
				//deve essere minore di 900000000 ed esterno agli estremi
				if (i1 < 900000000){
					//deve essere minore di fineMan
					if (i1 <= f1){
						//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
						if ((i1 < i2) || (i1 > f2 && i1 < i3) || (i1 > f3 && i1 < i4) || (i1 > i4)){
							if (i1 <= f1 && i1 < i2 && i1 < f2 && i1 < i3 && i1 < f3 && i1 < i4 && i1 < f4){
							}else{
								throw new DataException("inizio1DeveEssereMinoreDiTuttiGliEstremiSuccessivi");
							}
							//Può essere minore di prg_inv_corrente
							if (i1 <= Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
								//ma in questo caso anche fine_man deve esserlo
								if (f1 != Integer.MAX_VALUE){
									if (f1 > Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
										throw new DataException("errDefIntRis1EstrSupDeveEssereMinoreProgrAut");
									}
								}
							}
						}else{
							throw new DataException("numintRis1InizioDeveAppartenereANessunIntRis");
						}
					}else{
						//<=
						throw new DataException("numintRis1InizioDeveEssereMinoreDinumIntRis1Fine");
					}

				}else{
					throw new DataException("num1intRisDeveEssereMinoreDi900000000");
				}
			}else{
				throw new DataException("num1intRisDeveEssereMaggioreDelNumMan");
			}
		}
		//numero fine primo intervallo riservato
		//Obbligatorio se è stato indicato inizio_man;
		if (f1 != Integer.MAX_VALUE){
			if(i1 > 0 && f1 > 0){
				//il valore deve essere maggiore di inizio_man
				if (f1 > i1){
					//in ogni caso minore di 900000000
					if (f1 < 900000000) {
						//minore del minore degli estremi inferiori degli altri eventuali intervalli riservati definiti
						if ((f1 < i2) || (f1 > f2 && f1 < i3) || (f1 > f3 && f1 < i4) || (f1 > i4)){
							if (f1 >= i1 && f1 < i2 && f1 < f2 && f1 < i3 && f1 < f3 && f1 < i4 && f1 < f4){
							}else{
								throw new DataException("fine1DeveEssereMaggioreEstremoPrecedEMinoreDiTuttiGliEstremiSuccessivi");
							}
						}else{

							throw new DataException("num1intRisFineDeveEssereMinoreDelMinoreDegliAltriIntRisFineDefiniti");
						}
					}else{
						throw new DataException("num1intRisFineDeveEssereMinoreDi900000000");
					}
				}else{
					throw new DataException("num1intRisFineDeveEssereMaggioreDiNum1intRisInizio");
				}
			}else{
				throw new DataException("num1intRisFineEObbligSeEPresnum1intRisInizio");
			}
		}

		//numero inizio secondo intervallo riservato
		// Se indicato deve essere maggiore di num_man,
		if (i2 != Integer.MAX_VALUE){
			if(i2 > Integer.parseInt(myForm.getRecSerie().getNumMan())){
				//deve essere minore di 900000000 ed esterno agli estremi
				if (i2 < 900000000){
					//deve essere minore di fineMan
					if (i2 < f2){
						//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
						if ((i2 < i1) || (i2 > f1 && i2 < i3) || (i2 > f3 && i2 < i4) || (i2 > f4)){
							if (i2 > i1 && i2 > f1 && i2 <= f2 && i2 < i3 && i2	< f3 && i2 < i4 && i2 < f4){
							}else{
								throw new DataException("inizio2DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
							}
							//Può essere minore di prg_inv_corrente
							if (i2 <= Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
								//ma in questo caso anche fine_man deve esserlo
								if (f2 != Integer.MAX_VALUE){
									if (f2 > Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
										throw new DataException("errDefIntRis2EstrSupDeveEssereMinoreProgrAut");
									}
								}
							}//nessun else di errore
						}else{
							throw new DataException("numintRis2InizioDeveAppartenereANessunIntRis");
						}
					}else{
						//<=
						throw new DataException("numintRis2InizioDeveEssereMinoreDinumIntRis1Fine");
					}

				}else{
					throw new DataException("num21intRisDeveEssereMinoreDi900000000");
				}
			}else{
				throw new DataException("num2intRisDeveEssereMaggioreDelNumMan");
			}
		}
		//numero fine secondo intervallo riservato
		//Obbligatorio se è stato indicato inizio_man;
		if (f2 != Integer.MAX_VALUE){
			if(i2 > 0 && f2 > 0){
				//il valore deve essere maggiore di inizio_man
				if (f2 > i2){
					//in ogni caso minore di 900000000
					if (f2 < 900000000) {
						//minore del minore degli estremi inferiori degli altri eventuali intervalli riservati definiti
//						if (f2 < f4
//								&& f2 < f3
//								&& f2 > f1){
						if ((f2 < i1) || (f2 > f1 && f2 < i3) || (f2 > f3 && f2 < i4) || (f2 > f4)){
							if (f2 > i1 && f2 > f1 && f2 >= i2 && f2 < i3 && f2 < f3 && f2 < i4 && f2 < f4){
							}else{
								throw new DataException("fine2DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
							}
						}else{
							throw new DataException("num2intRisFineDeveEssereMinoreDelMaggioreDegliAltriIntRisFineDefiniti");
						}
					}else{
						throw new DataException("num2intRisFineDeveEssereMinoreDelMinoreDi900000000");
					}
				}else{
					throw new DataException("num2intRisFineDeveEssereMaggioreDiNum1intRisInizio");
				}
			}else{
				throw new DataException("num21intRisFineEObbligSeEPresnum1intRisInizio");
			}
		}
		//numero inizio terzo intervallo riservato
		// Se indicato deve essere maggiore di num_man,
		if (i3 != Integer.MAX_VALUE){
			if(i3 > Integer.parseInt(myForm.getRecSerie().getNumMan())){
				//deve essere minore di 900000000 ed esterno agli estremi
				if (i3 < 900000000){
					//deve essere minore di fineMan
					if (i3 < f3){
						//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
						if ((i3 < i1) || (i3 > f1 && i3 < i2) || (i3 > f2 && i3 < i4) || (i3 > f4)){
										//Può essere minore di prg_inv_corrente
							if (i3 > i1 && i3 > f1 && i3 > i2 && i3 > f2 && i3 <= f3 && i3 < i4 && i3 < f4){
							}else{
								throw new DataException("inizio3DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
							}
							if (i3 <= Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
								//ma in questo caso anche fine_man deve esserlo
								if (f3 != Integer.MAX_VALUE){
									if (f3 > Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
										throw new DataException("errDefIntRis3EstrSupDeveEssereMinoreProgrAut");
									}
								}
							}
						}else{
							throw new DataException("numintRis3InizioDeveAppartenereANessunIntRis");
						}
					}else{
						//<=
						throw new DataException("numintRis3InizioDeveEssereMinoreDinumIntRis1Fine");
					}

				}else{
					throw new DataException("num3intRisDeveEssereMinoreDi900000000");
				}
			}else{
				throw new DataException("num3intRisDeveEssereMaggioreDelNumMan");
			}
		}
		//numero fine terzo intervallo riservato
		//Obbligatorio se è stato indicato inizio_man;
		if (f3 != Integer.MAX_VALUE){
			if(i3 > 0 && f3 > 0){
				//il valore deve essere maggiore di inizio_man
				if (f3 > i3){
					//in ogni caso minore di 900000000
					if (f3 < 900000000) {
						//minore del minore degli estremi inferiori degli altri eventuali intervalli riservati definiti
						if ((f3 < i1) || (f3 > f1 && f3 < i2) || (f3 > f2 && f3 < i4) || (f3 > f4)){
//						if (f3 < f4
//								&& f3 > f2
//								&& f3 > f1){
							if (f3 > i1	&& f3 > f1 && f3 > i2 && f3	> f2 && f3 >= i3 && f3 < i4	&& f3 < f4){
							}else{
								throw new DataException("fine3DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
							}
						}else{
							throw new DataException("num3intRisFineDeveEssereMinoreDelMaggioreDegliAltriIntRisFineDefiniti");
						}
					}else{
						throw new DataException("num3intRisFineDeveEssereMinoreDelMinoreDi900000000");
					}
				}else{
					throw new DataException("num3intRisFineDeveEssereMaggioreDiNum1intRisInizio");
				}
			}else{
				throw new DataException("num31intRisFineEObbligSeEPresnum1intRisInizio");
			}
		}
		//numero inizio quarto intervallo riservato
		// Se indicato deve essere maggiore di num_man,
		if (i4 != Integer.MAX_VALUE){
			if(i4 > Integer.parseInt(myForm.getRecSerie().getNumMan())){
				//deve essere minore di 900000000 ed esterno agli estremi
				if (i4 < 900000000){
					//deve essere minore di fineMan
					if (i4 <= f4){
						//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
						if ((i4 < i1) || (i4 > f1 && i4 < i2) || (i4 > f2 && i4 < i3) || (i4 > f3)){
							if (i4 > i1 && i4 > f1 && i4 > i2 && i4 > f2 && i4 > f3 && i4 > f3 && i4 <= f4){
							}else{
								throw new DataException("inizio4DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
							}
										//Può essere minore di prg_inv_corrente
							if (i4 <= Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
								//ma in questo caso anche fine_man deve esserlo
								if (f4 != Integer.MAX_VALUE){
									if (f4 > Integer.parseInt(myForm.getRecSerie().getProgAssInv())){
										throw new DataException("errDefIntRis4EstrSupDeveEssereMinoreProgrAut");
									}
								}

							}
						}else{
							throw new DataException("numintRis4InizioDeveAppartenereANessunIntRis");
						}
					}else{
						//<=
						throw new DataException("numintRis4InizioDeveEssereMinoreDinumIntRis4Fine");
					}


				}else{
					throw new DataException("num4intRisDeveEssereMinoreDi900000000");
				}
			}else{
				throw new DataException("num4intRisDeveEssereMaggioreDelNumMan");
			}
		}
		//numero fine quarto intervallo riservato
		//Obbligatorio se è stato indicato inizio_man;
		if (f4 != Integer.MAX_VALUE){
			if(i4 > 0 && f4 > 0){
				//il valore deve essere maggiore di inizio_man
				if (f4 > i4){
					//in ogni caso minore di 900000000
					if (f4 < 900000000) {
						//maggiore degli estremi inferiori degli altri eventuali intervalli riservati definiti che lo precedono
//						if (f4 > f1
//								&& f4 > f2
//								&& f4 > f3){
						if ((f4 < i1) || (f4 > f1 && f4 < i2) || (f4 > f2 && f4 < i3) || (f4 > f3)){
							if (f4 > i1 && f4 > f1 && f4 > i2 && f4 > f2 && f4 > i3 && f4 > f3	&& f4 >= f4){
							}else{
								throw new DataException("fine4DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
							}
						}else{
							throw new DataException("num3intRisFineDeveEssereMinoreDelMaggioreDegliAltriIntRisFineDefiniti");
						}
					}else{
						throw new DataException("num3intRisFineDeveEssereMinoreDelMinoreDi900000000");
					}
				}else{
					throw new DataException("num3intRisFineDeveEssereMaggioreDiNum1intRisInizio");
				}
			}else{
				throw new DataException("num31intRisFineEObbligSeEPresnum1intRisInizio");
			}
		}
//		//controlla sequenza intervalli riservati
//		if (i1 <= f1 && i1 < i2 && i1 < f2 && i1 < i3 && i1 < f3 && i1 < i4 && i1 < f4){
//		}else{
//			throw new DataException("inizio1DeveEssereMinoreDiTuttiGliEstremiSuccessivi");
//		}
//		if (f1 >= i1 && f1 < i2 && f1 < f2 && f1 < i3 && f1 < f3 && f1 < i4 && f1 < f4){
//		}else{
//			throw new DataException("fine1DeveEssereMaggioreEstremoPrecedEMinoreDiTuttiGliEstremiSuccessivi");
//		}
//		if (i2 > i1 && i2 > f1 && i2 <= f2 && i2 < i3 && i2	< f3 && i2 < i4 && i2 < f4){
//		}else{
//			throw new DataException("inizio2DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
//		}
//		if (f2 > i1 && f2 > f1 && f2 >= i2 && f2 < i3 && f2 < f3 && f2 < i4 && f2 < f4){
//		}else{
//			throw new DataException("fine2DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
//		}
//		if (i3 > i1 && i3 > f1 && i3 > i2 && i3 > f2 && i3 <= f3 && i3 < i4 && i3 < f4){
//		}else{
//			throw new DataException("inizio3DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
//		}
//		if (f3 > i1	&& f3 > f1 && f3 > i2 && f3	> f2 && f3 >= i3 && f3 < i4	&& f3 < f4){
//		}else{
//			throw new DataException("fine3DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
//		}
//		if (i4 > i1 && i4 > f1 && i4 > i2 && i4 > f2 && i4 > f3 && i4 > f3 && i4 <= f4){
//		}else{
//			throw new DataException("inizio4DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
//		}
//		if (f4 > i1 && f4 > f1 && f4 > i2 && f4 > f2 && f4 > i3 && f4 > f3	&& f4 >= f4){
//		}else{
//			throw new DataException("fine4DeveEssereMaggioreEstremiPrecedEMinoreDiTuttiGliEstremiSuccessivi");
//		}
	}


	@Override
	protected String getMethodName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String parameter)
			throws Exception {

		SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm) form;

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(SerieInventarialeGestioneForm.SUBMIT_CANCELLA_INTERVALLO) ) {
				String tokens[] = param.split(LinkableTagUtils.SEPARATORE);
				myForm.setIntervalloSelez(tokens[2]);
				return "intervalloCanc";
			}

		}
		return super.getMethodName(mapping, form, request, response, parameter);

	}
	public ActionForward intervalloCanc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm) form;

		if (myForm.getIntervalloSelez() != null){
			if (!myForm.getIntervalloSelez().equals("1")) {
				if (!myForm.getIntervalloSelez().equals("2")) {
					if (!myForm.getIntervalloSelez().equals("3")) {
						if (myForm.getIntervalloSelez().equals("4")) {
							//eliminazione quarto intervallo e prospettazione intervallo vuoto
						}
					}else{
						//eliminazione terzo intervallo, slittamento del quarto sul terzo e prospettazione quarto intervallo vuoto
						//slittamento del quarto sul terzo
						myForm.getRecSerie().setInizioMan3(myForm.getRecSerie().getInizioMan4());
						myForm.getRecSerie().setFineMan3(myForm.getRecSerie().getFineMan4());
						myForm.getRecSerie().setDataIngrRis3(myForm.getRecSerie().getDataIngrRis4());
					}
				}else{
					//eliminazione secondo intervallo, slittamento del terzo sul secondo, del quarto sul terzo e prospettazione intervallo quarto vuoto
					//slittamento del terzo sul secondo
					myForm.getRecSerie().setInizioMan2(myForm.getRecSerie().getInizioMan3());
					myForm.getRecSerie().setFineMan2(myForm.getRecSerie().getFineMan3());
					myForm.getRecSerie().setDataIngrRis2(myForm.getRecSerie().getDataIngrRis3());
					//slittamento del quarto sul terzo
					myForm.getRecSerie().setInizioMan3(myForm.getRecSerie().getInizioMan4());
					myForm.getRecSerie().setFineMan3(myForm.getRecSerie().getFineMan4());
					myForm.getRecSerie().setDataIngrRis3(myForm.getRecSerie().getDataIngrRis4());
			}
			}else{
				//eliminazione primo intervallo,
				//slittamento del secondo sul primo
				myForm.getRecSerie().setInizioMan1(myForm.getRecSerie().getInizioMan2());
				myForm.getRecSerie().setFineMan1(myForm.getRecSerie().getFineMan2());
				myForm.getRecSerie().setDataIngrRis1(myForm.getRecSerie().getDataIngrRis2());
				//slittamento del terzo sul secondo
				myForm.getRecSerie().setInizioMan2(myForm.getRecSerie().getInizioMan3());
				myForm.getRecSerie().setFineMan2(myForm.getRecSerie().getFineMan3());
				myForm.getRecSerie().setDataIngrRis2(myForm.getRecSerie().getDataIngrRis3());
				//slittamento del quarto sul terzo
				myForm.getRecSerie().setInizioMan3(myForm.getRecSerie().getInizioMan4());
				myForm.getRecSerie().setFineMan3(myForm.getRecSerie().getFineMan4());
				myForm.getRecSerie().setDataIngrRis3(myForm.getRecSerie().getDataIngrRis4());
			}
				//prospettazione intervallo quarto vuoto
				myForm.getRecSerie().setInizioMan4("0");
				myForm.getRecSerie().setFineMan4("0");
				myForm.getRecSerie().setDataIngrRis4("");

			myForm.setIntervalloSelez("0");
		}
		this.saveToken(request);
		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm) form;
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private boolean insertSerie(SerieVO serie, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().insertSerie(serie, ticket);
		return ret;
	}

	private boolean updateSerie(SerieVO serie, String ticket) throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().updateSerie(serie, ticket);
		return ret;
	}

	private SerieVO getSerieDettaglio(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		SerieVO rec = null;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		rec = factory.getGestioneDocumentoFisico().getSerieDettaglio(codPolo, codBib, codSez, ticket);
		return rec;
	}

	private ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket) throws Exception {
		ModelloDefaultVO modello;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		modello = factory.getGestioneDocumentoFisico().getModelloDefault(codPolo, codBib, ticket);
		return modello;
	}
	private List getListaSerie(String codPolo, String codBib, String ticket, ActionForm form) throws Exception {
		SerieInventarialeGestioneForm myForm = (SerieInventarialeGestioneForm)form;
		List serie;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		serie = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket);
		return serie;
	}
}
