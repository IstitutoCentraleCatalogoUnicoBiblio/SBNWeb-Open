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
package it.iccu.sbn.web.actions.gestionestampe.bollettino;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBollettinoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.bollettino.StampaBollettinoForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StampaBollettinoAction extends SinteticaLookupDispatchAction {

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		map.put("gestionestampe.lsBib", "listaSupportoBib");
		return map;
	}
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try{
			StampaBollettinoForm myForm = (StampaBollettinoForm) form;
//			super.unspecified(mapping, myForm, request, response);
//			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			//questi valori vanno messi nel form
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			//
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

			myForm.setDataDa("");
			myForm.setDataA("");
			myForm.setElencoModelli(getElencoModelli());
			myForm.setTipoFormato(TipoStampa.PDF.name());
			myForm.setCheck("nuoviEsemplari");
			this.loadTipiOrdinamento(form);
			myForm.setTipoOrdinamento(((CodiceVO) myForm.getListaTipiOrdinamento().get(0)).getDescrizione());

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaBollettinoForm myForm = (StampaBollettinoForm) form;
		try {
//			return mapping.findForward("indietro");
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward conferma(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StampaBollettinoForm myForm = (StampaBollettinoForm) form;
		try {

			ActionMessages errors = null;

			int codRitorno = -1;
			if (myForm.getDataDa().trim().equals("")){
				if (myForm.getDataA().trim().equals("")
						|| myForm.getDataA().equals("00/00/0000")
						|| !myForm.getDataA().equals("00/00/0000")){
					errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.dataDaObbligatoria"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}else{
				if (!myForm.getDataDa().trim().equals("") && myForm.getDataDa().equals("00/00/0000")){
					errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.formatoDataDaNonValido"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}else{
					codRitorno = ValidazioneDati.validaDataPassata(myForm.getDataDa());
					if (codRitorno != ValidazioneDati.DATA_OK){
						errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.dataDaErrata"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}else{
						if (myForm.getDataA() != null && !myForm.getDataA().trim().equals("")) {
							if (myForm.getDataA().equals("00/00/0000")){
								errors = new ActionMessages();
								errors.add("generico", new ActionMessage("error.documentofisico.formatoDataANonValido"));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}else{
								codRitorno = ValidazioneDati.validaDataPassata(myForm.getDataA());
								if (codRitorno != ValidazioneDati.DATA_OK){
										errors = new ActionMessages();
										errors.add("generico", new ActionMessage("error.documentofisico.dataAErrata"));
										this.saveErrors(request, errors);
										return mapping.getInputForward();
								}else{
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
									  long longData1 = 0;
									  long longData2 = 0;

									try {
										  Date data1 = simpleDateFormat.parse(myForm.getDataDa().trim());
										  Date data2 = simpleDateFormat.parse(myForm.getDataA().trim());
										  longData1 = data1.getTime();
										  longData2 = data2.getTime();
									  } catch (ParseException e) {
											errors = new ActionMessages();
											errors.add("Attenzione", new ActionMessage("error.documentofisico.erroreParse"));
											this.saveErrors(request, errors);
											resetToken(request);
											return mapping.getInputForward();
									  }

									  if ((longData2 - longData1) < 0) {
											errors = new ActionMessages();
											errors.add("Attenzione", new ActionMessage("error.documentofisico.dataDaAErrata"));
											this.saveErrors(request, errors);
											resetToken(request);
											return mapping.getInputForward();
									  }
								}
							}
						}
					}
				}
			}


			//validazione startInv e endInv
			List inputForStampeService=new ArrayList();
			StampaBollettinoVO stampaVO	= new StampaBollettinoVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			stampaVO.setCodPolo(myForm.getCodPolo());
			stampaVO.setCodBib(myForm.getCodBib());
			stampaVO.setDataDa(myForm.getDataDa());
			stampaVO.setDataA(myForm.getDataA());
			stampaVO.setOrdinaPer(myForm.getTipoOrdinamento());
			if (myForm.getCheck().equals("nuoviTitoli")){
				stampaVO.setNuoviTitoli(true);
				stampaVO.setNuoviEsemplari(false);
//				stampaVO.setNuoviTitoli(true);
//				stampaVO.setNuoviEsemplari(false);
//				myForm.setCheck("nuoviEsemplari");
//				errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("error.documentofisico.funzioneAlMomentoNonDisponibile"));
//				this.saveErrors(request, errors);
//				return mapping.getInputForward();
			}
			if (myForm.getCheck().equals("nuoviEsemplari")){
				stampaVO.setNuoviEsemplari(true);
				stampaVO.setNuoviTitoli(false);
			}
			inputForStampeService.add(stampaVO);
			if (myForm.getElencoModelli() != null && myForm.getElencoModelli().size() > 0){
				for (int index = 0; index < myForm.getElencoModelli().size(); index++) {
					ModelloStampaVO rec = (ModelloStampaVO) getElencoModelli().get(index);
					myForm.setModello(rec);
				}
			}else{
				errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.modelloNonPresenteInBaseDati"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}



			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();


			String tipoFormato=myForm.getTipoFormato();

			request.setAttribute("DatiVo", inputForStampeService);
			request.setAttribute("TipoFormato", tipoFormato);

			String fileJrxml = myForm.getModello().getJrxml()+".jrxml";
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);

			String pathJrxml = null;
			pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+ fileJrxml;
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
			stam.setCodAttivita(CodiciAttivita.getIstance().GDF_BOLLETTINO_NUOVE_ACCESSIONI);//momentaneo
			stam.setTicket(myForm.getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			String idMessaggio = null;
			stam.setTipoOperazione("STAMPA_BOLLETTINO");
			idMessaggio = factory.getStampeOnline().stampaBollettino(stam);

			errors = new ActionMessages();
			if (idMessaggio != null) {
				idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
				errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
				this.saveErrors(request, errors);
			}else{
				errors.add("Attenzione", new ActionMessage("error.documentofisico.prenotStampaNonEffettuata"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
			myForm.setDisable(true);
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
		return mapping.getInputForward();//mapping.findForward("ABCDEFGHI");
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_BOLLETTINO_NUOVE_ACCESSIONI);
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
		StampaBollettinoForm myForm = (StampaBollettinoForm) form;
		myForm = (StampaBollettinoForm) form;
		try {

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_BOLLETTINO_NUOVE_ACCESSIONI, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void loadTipiOrdinamento(ActionForm form) throws Exception {
		StampaBollettinoForm myForm = (StampaBollettinoForm) form;
		List lista = new ArrayList();
		CodiceVO rec = new CodiceVO("CA","Data prima collocazione + serie + inv Asc");
		lista.add(rec);
		rec = new CodiceVO("CD","Data prima collocazione + serie + inv Desc");
		lista.add(rec);
		rec = new CodiceVO("TA","Titolo Asc");
		lista.add(rec);
		rec = new CodiceVO("TD","Titolo Desc");
		lista.add(rec);
//		rec = new CodiceVO("AA","Autore Asc");
//		lista.add(rec);
//		rec = new CodiceVO("AD","Autore Desc");
//		lista.add(rec);
		myForm.setListaTipiOrdinamento(lista);
	}
}
