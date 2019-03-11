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
package it.iccu.sbn.web.actions.gestionestampe.ingresso;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.ingresso.StampaRegistroIngressoForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class StampaRegistroIngressoAction extends RicercaInventariCollocazioniAction {

	private static Logger log = Logger.getLogger(StampaRegistroIngressoAction.class);

	private void loadCodTipoOrdine(ActionForm form) throws Exception {
		StampaRegistroIngressoForm myForm = (StampaRegistroIngressoForm) form;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo caricaCombo = new CaricamentoCombo();
		myForm.setListaCodTipoOrdine(caricaCombo
				.loadComboCodiciDesc(factory.getCodici().getCodici(
						CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE)));
	}

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("gestionestampe.lsBib", "listaSupportoBib");
		map.put("reg.selRegistroIngresso", "selRegistroIngresso");
		map.put("reg.selRegistroStatistiche", "selRegistroStatistiche");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try{
			StampaRegistroIngressoForm myForm = (StampaRegistroIngressoForm) form;
			super.unspecified(mapping, myForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			//questi valori vanno messi nel form
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			//
			this.loadCodTipoOrdine(form);
			myForm.setTipoDiStampa("RegistroIngresso");
			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				myForm.setCodBib((String) request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
			}


			myForm.setTipoFormato(TipoStampa.XLS.name());



		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaRegistroIngressoForm myForm = (StampaRegistroIngressoForm) form;
		try {
//			return mapping.findForward("indietro");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaRegistroIngressoForm myForm = (StampaRegistroIngressoForm) form;
		try {

//			this.checkForm(request, form, mapping);
				super.validaInputRangeInventari(mapping, request, myForm);
				//validazione startInv e endInv
				List inputForStampeService=new ArrayList();
				if (myForm.getTipoDiStampa() != null && myForm.getTipoDiStampa().equals("RegistroIngresso")){
					StampaRegistroVO stampaVO = new StampaRegistroVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					stampaVO.setCodPolo(myForm.getCodPolo());
					stampaVO.setCodeBiblioteca(myForm.getCodBib());
					stampaVO.setSerieInv(myForm.getSerie());
					stampaVO.setCodeInvA(myForm.getEndInventario());
					stampaVO.setCodeInvDa(myForm.getStartInventario());
					stampaVO.setDataDa(myForm.getRegingroDataDa());
					stampaVO.setDataA(myForm.getRegingroDataA());
					//
//					stampaVO.setChkDataInventario("1");
//					stampaVO.setChkCodeInventario("1");
//					stampaVO.setChkCodeFornitore("1");
//					stampaVO.setChkTipoOrdine("1");
//					stampaVO.setChkTitolo("1");
//					stampaVO.setChkCodeMateriale("1");
//					stampaVO.setChkValore("1");
//					stampaVO.setChkPrecisazioni(("1"));
//					stampaVO.setChkImporto("1");
//					stampaVO.setChkBid("1");
//					stampaVO.setChkNroFattura("1");
//					stampaVO.setChkDataFattura("1");
//					stampaVO.setChkCollocazione("1");
					inputForStampeService.add(stampaVO);
					myForm.setElencoModelli(getElencoModelli());
					if (myForm.getElencoModelli() != null && myForm.getElencoModelli().size() > 0){
						for (int index = 0; index < myForm.getElencoModelli().size(); index++) {
							ModelloStampaVO rec = (ModelloStampaVO) getElencoModelli().get(index);
							if (rec.getDescrizione().equals("con dati fattura")){
								myForm.setModello(rec);
							}
						}
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.modelloNonPresenteInBaseDati"));

						return mapping.getInputForward();
					}
				}else if (myForm.getTipoDiStampa() != null && myForm.getTipoDiStampa().equals("StatisticheRegistro")){
					StampaRegistroVO stampaVO	= new StampaRegistroVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					if (ValidazioneDati.validaDataPassata(myForm.getRegingroDataDa().trim())!=0 ||
							myForm.getRegingroDataDa().equals("00/00/0000"))	{
						if (myForm.getRegingroDataDa().trim() == null || !myForm.getRegingroDataDa().trim().equals("")){
							throw new ValidationException("dataDaErrataDa");
						}
					}
					if (ValidazioneDati.validaDataPassata(myForm.getRegingroDataA().trim())!=0 ||
							myForm.getRegingroDataA().equals("00/00/0000"))	{
						if (myForm.getRegingroDataA().trim() == null || !myForm.getRegingroDataA().trim().equals("")){
							throw new ValidationException("dataDaErrataA");
						}
					}else if (ValidazioneDati.validaDataPassata(myForm.getRegingroDataA().trim())!=0 ||
							!myForm.getRegingroDataA().equals("00/00/0000")){
						//confronta date

						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
						  long longData1 = 0;
						  long longData2 = 0;

						try {
							  Date data1 = simpleDateFormat.parse(myForm.getRegingroDataDa().trim());
							  Date data2 = simpleDateFormat.parse(myForm.getRegingroDataA().trim());
							  longData1 = data1.getTime();
							  longData2 = data2.getTime();
						  } catch (ParseException e) {


								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreParse"));
								;
								resetToken(request);
								return mapping.getInputForward();
						  }

						  if ((longData2 - longData1) < 0) {


								LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.dataDaAErrata"));
								;
								resetToken(request);
								return mapping.getInputForward();
						  }

							//
					}



					stampaVO.setCodPolo(myForm.getCodPolo());
					stampaVO.setCodeBiblioteca(myForm.getCodBib());
					stampaVO.setSerieInv(myForm.getSerie());
					stampaVO.setCodeInvA(myForm.getEndInventario());
					stampaVO.setCodeInvDa(myForm.getStartInventario());
					stampaVO.setDataDa(myForm.getRegingroDataDa());
					stampaVO.setDataA(myForm.getRegingroDataA());
					stampaVO.setCodeTipoOrdine(myForm.getCodTipoOrdine());
					inputForStampeService.add(stampaVO);
					myForm.setElencoModelli(getElencoModelloStatistiche());
					if (myForm.getElencoModelli() != null && myForm.getElencoModelli().size() > 0){
						for (int index = 0; index < myForm.getElencoModelli().size(); index++) {
							ModelloStampaVO rec = (ModelloStampaVO) getElencoModelli().get(index);
							if (rec.getDescrizione().startsWith("Statistiche")){
								myForm.setModello(rec);
							}
						}
					}else{

						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.modelloNonPresenteInBaseDati"));

						return mapping.getInputForward();
					}
				}
				UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
				String utente= user.getUserId();


				String tipoFormato=myForm.getTipoFormato();

				request.setAttribute("DatiVo", inputForStampeService);//  ListaBuoniOrdine
				request.setAttribute("TipoFormato", tipoFormato);

				String fileJrxml = myForm.getModello().getJrxml()+".jrxml";
				String basePath=this.servlet.getServletContext().getRealPath(File.separator);

				String pathJrxml = null;
				if(myForm.getTipoDiStampa().equals("RegistroIngresso")){
					pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+ fileJrxml;
				}else if(myForm.getTipoDiStampa().equals("StatisticheRegistro")){
					pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+ fileJrxml;
				}
				//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
				String pathDownload = StampeUtil.getBatchFilesPath();

				//codice standard inserimento messaggio di richiesta stampa differita
				StampaDiffVO stam = new StampaDiffVO();
				stam.setTipoStampa(tipoFormato);
				stam.setUser(utente);
				stam.setCodPolo(myForm.getCodPolo());
				stam.setCodBib(myForm.getCodBib());
				stam.setParametri(inputForStampeService);
				stam.setTemplate(pathJrxml);
				stam.setDownload(pathDownload);
				stam.setDownloadLinkPath("/");
				stam.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_REGISTRO_DI_INGRESSO);
				stam.setTicket(myForm.getTicket());
				UtilityCastor util= new UtilityCastor();
				String dataCorr = util.getCurrentDate();
				stam.setData(dataCorr);
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				String idMessaggio = null;
				stam.setTipoOperazione("STAMPA_REGISTRO_INGRESSO");
				if(myForm.getTipoDiStampa().equals("RegistroIngresso")){
					stam.setTipoOrdinamento("REGISTRO");
					idMessaggio = factory.getStampeOnline().stampaRegistroIngresso(stam);
				}else if(myForm.getTipoDiStampa().equals("StatisticheRegistro")){
					stam.setTipoOrdinamento("STATISTICHE");
					idMessaggio = factory.getStampeOnline().stampaStatisticheRegistroIngresso(stam);
//
//					LinkableTagUtils.addError(request, new ActionMessage("errors.stampa.nondisponibile"));
//					;
//					return mapping.getInputForward();
				}


				if (idMessaggio != null) {
					idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
					LinkableTagUtils.addError(request, new ActionMessage("errors.finestampa" , idMessaggio));

				}else{
					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.prenotStampaNonEffettuata"));

					resetToken(request);
					return mapping.getInputForward();
				}
				//myForm.setDisable(true);
		} catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ ve.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();//mapping.findForward("ABCDEFGHI");
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_REGISTRO_DI_INGRESSO);
			return listaModelli;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private List getElencoModelloStatistiche() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_REGISTRO_DI_INGRESSO);
			return listaModelli;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaRegistroIngressoForm myForm = (StampaRegistroIngressoForm) form;
		myForm = (StampaRegistroIngressoForm) form;
		try {

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
				SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(),
				CodiciAttivita.getIstance().GA_STAMPA_REGISTRO_DI_INGRESSO, myForm.getNRec(), "codBibDaLista");
				return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

	public ActionForward selRegistroIngresso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaRegistroIngressoForm myForm = (StampaRegistroIngressoForm) form;
		myForm.setTipoDiStampa("RegistroIngresso");

		myForm.setSerie("");
		myForm.setStartInventario("0");
		myForm.setEndInventario("0");

		return mapping.getInputForward();
	}

	public ActionForward selRegistroStatistiche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaRegistroIngressoForm myForm = (StampaRegistroIngressoForm) form;
		myForm.setTipoDiStampa("StatisticheRegistro");

		myForm.setSerie("");
		myForm.setStartInventario("0");
		myForm.setEndInventario("0");

//
//		LinkableTagUtils.addError(request, new ActionMessage("errors.stampa.nondisponibile"));
//		;
		return mapping.getInputForward();
	}



}
