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
package it.iccu.sbn.web.actions.gestionestampe.servizi;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaServiziVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.servizi.StampaServiziForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.integration.action.erogazione.ErogazioneRicercaAction;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ErogazioneRicercaForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class StampaServiziAction extends ErogazioneRicercaAction {

	private static Logger log = Logger.getLogger(StampaServiziAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("gestionestampe.lsBib", "listaSupportoBib");

		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StampaServiziForm currentForm = (StampaServiziForm) form;

		Navigation navi = Navigation.getInstance(request);
		MovimentoVO mov = currentForm.getAnaMov();
		if (navi.isFromBar() ) {
			//cambia nome bottone dinamicamente
			if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziCorrenti")){
				navi.setDescrizioneX("Stampa Servizi Correnti");
				navi.setTesto("Stampa Servizi Correnti");
			}else if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziStorico")){
				navi.setDescrizioneX("Stampa Storico Servizi");
				navi.setTesto("Stampa Storico Servizi");
			}
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
			if (mov.getCodBibDocLett() != null && !mov.getCodBibDocLett().equals("")){
				mov.setCodBibInv("");
				mov.setCodSerieInv("");
				mov.setCodInvenInv("");
			}
			return mapping.getInputForward();
		}
		try{
			String stampaSC = null;
			stampaSC = request.getParameter("STAMPASERVIZI");
			if (stampaSC != null){
				request.setAttribute("STAMPASERVIZI", "STAMPASERVIZI");
			}
			stampaSC = request.getParameter("INVRICERCA");
			if (stampaSC != null){
				request.setAttribute("INVRICERCA", "INVRICERCA");
			}
			stampaSC = request.getParameter("SEGNATURARICERCA");
			if (stampaSC != null){
				request.setAttribute("INVRICERCA", "");
			}




			ActionForward forward = super.unspecified(mapping, currentForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			if (!currentForm.isSessione1()){
				//questi valori vanno messi nel form
				currentForm.setTicket(navi.getUserTicket());
				currentForm.setCodPolo(navi.getUtente().getCodPolo());
				currentForm.setCodBib(navi.getUtente().getCodBib());
				currentForm.setDescrBib(navi.getUtente().getBiblioteca());
				//
				if (request.getParameter(Constants.CODICE_ATTIVITA) != null
						&& request.getParameter(Constants.CODICE_ATTIVITA).equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI)){
					currentForm.setTipoDiStampa("ServiziCorrenti");

					navi.setDescrizioneX("Stampa Servizi Correnti");
					navi.setTesto("Stampa Servizi Correnti");
					this.comboSvolgimento(currentForm);

					//almaviva5_20110907 #4070, #4071, #4610 Stati movimento/richiesta non filtrati
					CaricamentoCombo carCombo = new CaricamentoCombo();
					List<TB_CODICI> listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_STATO_RICHIESTA);
					currentForm.setLstStatiRichiesta(carCombo.loadComboCodiciDesc(listaCodice));

					listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_STATO_MOVIMENTO);
					currentForm.setLstStatiMovimento(carCombo.loadComboCodiciDesc(listaCodice));

					//almaviva5_20130424 locale
					mov.setFlSvolg("L");

				}else if (request.getParameter(Constants.CODICE_ATTIVITA) != null
						&& request.getParameter(Constants.CODICE_ATTIVITA).equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_STORICO)){
					currentForm.setTipoDiStampa("ServiziStorico");

					navi.setDescrizioneX("Stampa Storico Servizi");
					navi.setTesto("Stampa Storico Servizi");
				}

				if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziStorico")){
					this.comboTipoRichiesta(currentForm);
				}


				mov.setAttivitaAttuale(true);
				currentForm.setTipoFormato(TipoStampa.XLS.name());
				currentForm.setDataDa("");
				currentForm.setDataA("");

				if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziStorico")){
					currentForm.setElencoModelli(getElencoModelli(CodiciAttivita.getIstance().STAMPA_SERVIZI_STORICO));
				}else{
					currentForm.setElencoModelli(getElencoModelli(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI));
				}
				currentForm.setSessione1(true);
			}
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
			if (mov.getCodBibDocLett() != null && !mov.getCodBibDocLett().equals("")){
				mov.setCodBibInv("");
				mov.setCodSerieInv("");
				mov.setCodInvenInv("");
			}
			if (!forward.equals(mapping.getInputForward())){
					return forward;
				}
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}
	/**
	  * it.iccu.sbn.web.actions.gestionestampe.servizi
	  * StampaServiziAction.java
	  * comboTipoRichiesta
	  * void
	  * @param currentForm
	  *
	  *
	 */
	private void comboTipoRichiesta(StampaServiziForm currentForm) {
		if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziStorico")){
			if (currentForm.getLstStatiRichiesta() != null && currentForm.getLstStatiRichiesta().size()>0){
				List<ComboCodDescVO> listaRidotta = new ArrayList<ComboCodDescVO>();
				ComboCodDescVO elem = new ComboCodDescVO("","");
				listaRidotta.add(elem);
				for (int i = 1; i < currentForm.getSvolgimentiSelezionati().size(); i++) {
					elem = currentForm.getSvolgimentiSelezionati().get(i);
					if (elem.getCodice().equals("D") || elem.getCodice().equals("H") ||
							elem.getCodice().equals("B") || elem.getCodice().equals("F")){
						listaRidotta.add(elem);
					}
				}
				currentForm.setLstStatiRichiesta(listaRidotta);
			}
		}
	}
	/**
	  * it.iccu.sbn.web.actions.gestionestampe.servizi
	  * StampaServiziAction.java
	  * comboSvolgimento
	  * void
	  * @param currentForm
	  *
	  *
	 */
	private void comboSvolgimento(StampaServiziForm currentForm) {
		//almaviva5_20150716 #5932
		List<ComboCodDescVO> svolgimentiSelezionati = currentForm.getSvolgimentiSelezionati();
		if (ValidazioneDati.isFilled(svolgimentiSelezionati) ) {
			svolgimentiSelezionati = CaricamentoCombo.cutFirst(svolgimentiSelezionati);
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaServiziForm currentForm = (StampaServiziForm) form;
		request.setAttribute("STAMPASERVIZI","STAMPASERVIZI");
		try {

			List inputForStampeService = new ArrayList();

			StampaServiziVO stampaVO = new StampaServiziVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			stampaVO.setCodPolo(currentForm.getCodPolo());
			stampaVO.setCodBib(currentForm.getCodBib());
			MovimentoVO mov = currentForm.getAnaMov();
			if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziCorrenti")){
				if (!ValidazioneDati.isFilled(mov.getFlSvolg())){
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.svolgimentoNonImpostato"));

					resetToken(request);
					request.setAttribute("currentForm", currentForm);
					return mapping.getInputForward();
				}
			}
			stampaVO.setRichiesta(mov);
			if (ValidazioneDati.equals(mov.getFlSvolg(), ErogazioneRicercaForm.LOCALE)){
				stampaVO.getRichiesta().setFlSvolg("L");
			}
////////////////////////////////
			if (!currentForm.isNoData()){//se conferma=false
				if (currentForm.getDataDa().trim().equals("") && currentForm.getDataA().trim().equals("")){

					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.intervalloDataNonImpostato"));

					currentForm.setNoData(true);
					currentForm.setDisable(true);
					currentForm.setConferma(true);
					return mapping.getInputForward();
				}
			}
			currentForm.setConferma(false);
			ActionForward forward = confermaStampa(mapping, request, response, currentForm,
					inputForStampeService, stampaVO, mov);
			if (forward != null){
				return forward;
			}
		} catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ ve.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();//mapping.findForward("ABCDEFGHI");
	}

	/**
	  * it.iccu.sbn.web.actions.gestionestampe.servizi
	  * StampaServiziAction.java
	  * confermaStampa
	  * void
	  * @param mapping
	  * @param request
	  * @param response
	  * @param currentForm
	  * @param inputForStampeService
	  * @param stampaVO
	  * @param mov
	  * @throws ValidationException
	  * @throws Exception
	  * @throws NamingException
	  * @throws RemoteException
	  * @throws CreateException
	  *
	  *
	 */
	protected ActionForward confermaStampa(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			StampaServiziForm currentForm, List inputForStampeService,
			StampaServiziVO stampaVO, MovimentoVO mov)
			throws ValidationException, Exception, NamingException,
			RemoteException, CreateException {
		try {

			int codRitorno = -1;
			if (!currentForm.getDataDa().trim().equals("") && currentForm.getDataDa().equals("00/00/0000")){

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.formatoDataDaNonValido"));

				return mapping.getInputForward();
			}else if (!currentForm.getDataDa().trim().equals("")){
				codRitorno = ValidazioneDati.validaDataPassata(currentForm.getDataDa());
				if (codRitorno != ValidazioneDati.DATA_OK){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataDaErrata"));

					return mapping.getInputForward();
				}else{
					if (currentForm.getDataA() != null && !currentForm.getDataA().trim().equals("")) {
						if (currentForm.getDataA().equals("00/00/0000")){

							LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.formatoDataANonValido"));

							return mapping.getInputForward();
						}else{
							codRitorno = ValidazioneDati.validaDataPassata(currentForm.getDataA());
							if (codRitorno != ValidazioneDati.DATA_OK){

								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataAErrata"));

								return mapping.getInputForward();
							}else{
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
								long longData1 = 0;
								long longData2 = 0;

								try {
									Date data1 = simpleDateFormat.parse(currentForm.getDataDa().trim());
									Date data2 = simpleDateFormat.parse(currentForm.getDataA().trim());
									longData1 = data1.getTime();
									longData2 = data2.getTime();
								} catch (ParseException e) {

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreParse"));

									resetToken(request);
									return mapping.getInputForward();
								}

								if ((longData2 - longData1) < 0) {

									LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataDaAErrata"));

									resetToken(request);
									return mapping.getInputForward();
								}
							}
						}
					}
				}
			}
			////////////////////////////////
			DateUtil.validaRangeDate(currentForm.getDataDa(), currentForm.getDataA());
			mov.setDataInizioEff(DateUtil.toTimestamp(currentForm.getDataDa()));
			mov.setDataFineEff(DateUtil.toTimestampA(currentForm.getDataA()));
			//settaggio dati richiesta
			stampaVO.setDataDa(currentForm.getDataDa());
			stampaVO.setDataA(currentForm.getDataA());


			if ((mov.getCodBibInv() != null) && ((mov.getCodSerieInv() != null )
					&& (mov.getCodInvenInv() != null && !mov.getCodInvenInv().equals("")))){
				//passo il tipo ricerca e la provenienza
				currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_INVENTARIO);
				ActionForward forward = super.cerca(mapping, currentForm, request, response);
				if (forward != null){
					stampaVO.getRichiesta().setCodBibInv(mov.getCodBibInv());
					stampaVO.getRichiesta().setCodSerieInv(mov.getCodSerieInv());
					stampaVO.getRichiesta().setCodInvenInv(mov.getCodInvenInv());
				}
			}

			if (!((mov.getCodBibInv() != null) && ((mov.getCodSerieInv() != null )
					&& (mov.getCodInvenInv() != null && !mov.getCodInvenInv().equals(""))))){
				if (currentForm.getSegnaturaRicerca() != null && !currentForm.getSegnaturaRicerca().equals("")){
					if (mov.getCodBibDocLett() != null && !mov.getCodBibDocLett().equals("")){
						if (mov.getCodDocLet() != null && !mov.getCodDocLet().equals("")){
							//provengo da ricerca
							stampaVO.setCollocazione(currentForm.getSegnaturaRicerca());
							stampaVO.getRichiesta().setCodBibDocLett(mov.getCodBibDocLett());
							stampaVO.getRichiesta().setTipoDocLett(mov.getTipoDocLett());
							stampaVO.getRichiesta().setCodDocLet(mov.getCodDocLet());
							stampaVO.getRichiesta().setProgrEsempDocLet(mov.getProgrEsempDocLet());
						}else{
							if (currentForm.getSegnaturaRicerca() != null && !currentForm.getSegnaturaRicerca().equals("")){
								//passo il tipo ricerca e la provenienza
								currentForm.setTipoRicerca(RicercaRichiesteType.RICERCA_PER_SEGNATURA);
								ActionForward forward = super.cerca(mapping, currentForm, request, response);
								if (forward != null) {
									if (!mapping.getInputForward().getPath().equals(forward.getPath()))
										return forward;

									mov = currentForm.getAnaMov();
									stampaVO.setCollocazione(currentForm.getSegnaturaRicerca());
									stampaVO.getRichiesta().setCodBibDocLett(mov.getCodBibDocLett());
									stampaVO.getRichiesta().setTipoDocLett(mov.getTipoDocLett());
									stampaVO.getRichiesta().setCodDocLet(mov.getCodDocLet());
									stampaVO.getRichiesta().setProgrEsempDocLet(mov.getProgrEsempDocLet());
								}
							}
						}
					}
					else{
						throw new ValidationException("Indicare la biblioteca");
					}
				}
			}



			if (currentForm.getElencoModelli() != null && currentForm.getElencoModelli().size() > 0){
				ModelloStampaVO rec = null;
				for (int index = 0; index < currentForm.getElencoModelli().size(); index++) {
					if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziStorico")){
						rec = (ModelloStampaVO) getElencoModelli(CodiciAttivita.getIstance().STAMPA_SERVIZI_STORICO).get(index);
					}else{
						rec = (ModelloStampaVO) getElencoModelli(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI).get(index);
					}
					currentForm.setModello(rec);
				}
			}else{

				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.modelloNonPresenteInBaseDati"));

				return mapping.getInputForward();
			}


			inputForStampeService.add(stampaVO);
			//			List parametri=new ArrayList();
			//			parametri.add(stampaEtichetteVO);
			//			request.setAttribute("DatiVo", parametri);

			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();


			String tipoFormato = currentForm.getTipoFormato();

			request.setAttribute("DatiVo", inputForStampeService);
			request.setAttribute("TipoFormato", tipoFormato);

			String fileJrxml = currentForm.getModello().getJrxml()+".jrxml";
			String basePath = this.servlet.getServletContext().getRealPath(File.separator);

			String pathJrxml = null;
			pathJrxml = basePath + File.separator + "jrxml" + File.separator + File.separator + fileJrxml;
			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathDownload = StampeUtil.getBatchFilesPath();

			//codice standard inserimento messaggio di richiesta stampa differita
			StampaDiffVO stam = new StampaDiffVO();
			stam.setTipoStampa(tipoFormato);
			stam.setUser(utente);
			stam.setCodPolo(currentForm.getCodPolo());
			stam.setCodBib(currentForm.getCodBib());
			stam.setParametri(inputForStampeService);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziStorico")){
				stam.setCodAttivita(CodiciAttivita.getIstance().STAMPA_SERVIZI_STORICO);
				stam.setTipoOperazione("STAMPA_SERVIZI_STORICO");
			}else if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziCorrenti")){
				stam.setCodAttivita(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI);
				stam.setTipoOperazione("STAMPA_SERVIZI_CORRENTI");
			}else{
				throw new ValidationException("tipoStampaNonImpostato");
			}
			stam.setTicket(currentForm.getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			String idMessaggio = null;
			if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziStorico")){
				idMessaggio = factory.getStampeOnline().stampaServiziStorico(stam);
			}else if (currentForm.getTipoDiStampa() != null && currentForm.getTipoDiStampa().equals("ServiziCorrenti")){
				idMessaggio = factory.getStampeOnline().stampaServiziCorrenti(stam);
			}


			if (idMessaggio != null && !idMessaggio.equals("0")) {
				idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
				LinkableTagUtils.addError(request, new ActionMessage("errors.finestampa" , idMessaggio));

			}else{
				//almaviva5_20151012 #5998
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.prenotStampaNonEffettuata"));

				resetToken(request);
				return mapping.getInputForward();
			}
			currentForm.setDisable(true);
		}catch (Exception e) { // altri tipi di errore
		return mapping.getInputForward();
	}
	return null;
}

	private List getElencoModelli(String codAtt) {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = null;
			if (codAtt != null){
				if (codAtt.equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI)){
					listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI);
				}else{
					listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().STAMPA_SERVIZI_STORICO);
				}
			}
			return listaModelli;
		} catch (Exception e) {
			log.error("", e);
		}
		return new ArrayList();
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			Navigation navi = Navigation.getInstance(request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(navi.getUtente().getCodPolo(),
					navi.getUtente().getCodBib(),
			CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI, 0, "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaServiziForm myForm = (StampaServiziForm) form;
		try{
			return this.conferma(mapping, myForm, request, response);
		}
		catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + ve.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaServiziForm myForm = (StampaServiziForm) form;
		try {
			myForm.setNoData(false);
			myForm.setConferma(false);
			myForm.setDisable(false);
			return mapping.getInputForward();
		}	catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

}
