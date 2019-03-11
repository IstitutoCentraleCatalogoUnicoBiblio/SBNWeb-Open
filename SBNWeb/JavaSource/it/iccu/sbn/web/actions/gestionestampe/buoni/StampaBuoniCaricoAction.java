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
package it.iccu.sbn.web.actions.gestionestampe.buoni;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBuoniCaricoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.buoni.StampaBuoniCaricoForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

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

public class StampaBuoniCaricoAction extends RicercaInventariCollocazioniAction {

	private static Log log = LogFactory.getLog(StampaBuoniCaricoAction.class);



	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.selPerFattura", "selFattura");
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerNumBuonoCarico", "selPerNumBuonoCarico");
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		StampaBuoniCaricoForm currentForm = (StampaBuoniCaricoForm) form;
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

			StampaBuoniCaricoForm currentForm = ((StampaBuoniCaricoForm) form);
			super.unspecified(mapping, currentForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			if (!currentForm.isSessione()) {
				currentForm.setTicket(Navigation.getInstance(request).getUserTicket());
				currentForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				currentForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				currentForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				log.info("AggiornamentoDisponibilitaAction::unspecified");
				loadDefault(request, mapping, form);

				currentForm.setSessione(true);
			}

			this.loadTipiOrdinamento(currentForm);

			if (currentForm.getFolder() != null && (currentForm.getFolder().trim().equals("Fattura"))){
				currentForm.setSerie("");
				currentForm.setStartInventario("0");
				currentForm.setEndInventario("0");
				currentForm.setRistampaDaInv(false);
				currentForm.setBuonoCarico("0");
				currentForm.setRistampaDaNumBuono(false);
			}else if (currentForm.getFolder() != null && (currentForm.getFolder().trim().equals("NumeroBuono"))){
				currentForm.setRistampaDaNumBuono(true);
				currentForm.setDisableRistampaNumBuono(true);
				currentForm.setSerie("");
				currentForm.setStartInventario("0");
				currentForm.setEndInventario("0");
				currentForm.setAnnoFattura("0");
				currentForm.setRistampaDaInv(false);
				currentForm.setProgrFattura("0");
				currentForm.setBuonoCarico("0");
				currentForm.setRistampaDaFattura(false);
			}else if (currentForm.getFolder() != null && (currentForm.getFolder().trim().equals("RangeInv"))){
				currentForm.setAnnoFattura("0");
				currentForm.setProgrFattura("0");
				currentForm.setRistampaDaFattura(false);
				currentForm.setBuonoCarico("0");
				currentForm.setRistampaDaNumBuono(false);
			}

			if (Navigation.getInstance(request).isFromBar() ){
				return mapping.getInputForward();
			}
			currentForm.setElencoModelli(getElencoModelli());
			currentForm.setTipoFormato(TipoStampa.HTML.name());

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
			currentForm.setElencoModelli(getElencoModelli());
			currentForm.setTipoFormato(TipoStampa.PDF.name());
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

	public ActionForward conferma (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaBuoniCaricoForm currentForm = (StampaBuoniCaricoForm) form;
		try{
			List inputForStampeService=new ArrayList();

			StampaBuoniCaricoVO buoniCaricoVO = new StampaBuoniCaricoVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			buoniCaricoVO.setCodPolo(currentForm.getCodPolo());
			buoniCaricoVO.setCodBib(currentForm.getCodBib());
			//
			buoniCaricoVO. setTipoOrdinamento(currentForm.getTipoOrdinamento());
			//
//			buoniCaricoVO.setNumeroBuono(currentForm.getNumeroBuono()); sulla mappa di c/s non c'è numero buono in input
			if (currentForm.getFolder() != null && currentForm.getFolder().equals("Fattura")) {
				ActionForward forward = validaInputFattura(mapping, request, currentForm);
				if (forward != null)
					return forward;

				buoniCaricoVO.setAaFattura(currentForm.getAnnoFattura());
				buoniCaricoVO.setFattura(currentForm.getNumFattura());
				currentForm.setTipoOperazione("F");
			}else if (currentForm.getFolder() != null && currentForm.getFolder().equals("NumeroBuono")) {
				currentForm.setRistampaDaNumBuono(true);
				currentForm.setDisableRistampaNumBuono(true);
				if (!ValidazioneDati.strIsNull(currentForm.getBuonoCarico())) {
					if (!ValidazioneDati.strIsNumeric(currentForm.getBuonoCarico())) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.numBuonoNumerico"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
					if (currentForm.getBuonoCarico().length() > 9) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.numBuonoMax9"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}
				}
				buoniCaricoVO.setSerie(currentForm.getSerie());
				buoniCaricoVO.setBuonoCarico(currentForm.getBuonoCarico());
				buoniCaricoVO.setRistampaDaNumBuono(true);
				//almaviva5_20131118 #5100
				String dataCarico = currentForm.getDataCarico();
				if (ValidazioneDati.isFilled(dataCarico)) {
					dataCarico = dataCarico.trim();
					if (ValidazioneDati.DATA_OK != ValidazioneDati.validaData(dataCarico) )
						throw new ValidationException("validaInvDataCaricoErrata", ValidationException.errore);

					buoniCaricoVO.setDataCarico(dataCarico);
				}

			} else if (currentForm.getFolder() != null && currentForm.getFolder().equals("RangeInv")){
				//Ricerca per collocazioni
				currentForm.setTipoOperazione("R");
				super.validaInputRangeInventari(mapping, request, currentForm);
				//validazione startInv e endInv
				buoniCaricoVO.setSerie(currentForm.getSerie());
				buoniCaricoVO.setEndInventario(currentForm.getEndInventario());
				buoniCaricoVO.setStartInventario(currentForm.getStartInventario());
			}

			buoniCaricoVO.setTipoOperazione(currentForm.getTipoOperazione());
			buoniCaricoVO.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_BUONI_DI_CARICO);
			buoniCaricoVO.setUser(Navigation.getInstance(request).getUtente().getUserId());
			//ho finito di preparare il VO, ora lo metto nell'arraylist che passerò alla coda.

			if (currentForm.getElencoModelli() != null && currentForm.getElencoModelli().size() > 0){
//				for (int index = 0; index < currentForm.getElencoModelli().size(); index++) {
//					ModelloStampaVO rec = (ModelloStampaVO) getElencoModelli().get(index);
//					currentForm.setModello(rec);
//				}
				for (int index2 = 0; index2 < currentForm.getElencoModelli().size(); index2++) {
					ModelloStampaVO recMod = (ModelloStampaVO) getElencoModelli().get(index2);
					if (recMod.getJrxml() != null){
						buoniCaricoVO.setNomeSubReport(recMod.getSubReports().get(0) + ".jasper");
						currentForm.setTipoModello(recMod.getJrxml()+".jrxml");
					}
				}
			}else{
				ActionMessages errors = new ActionMessages();
				errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.modelloNonPresenteInBaseDati"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			if (currentForm.isRistampaDaFattura()){
				buoniCaricoVO.setRistampaDaFattura(true);
				buoniCaricoVO.setRistampaDaInv(false);
			}
			if (currentForm.isRistampaDaInv()){
				buoniCaricoVO.setRistampaDaInv(true);
				buoniCaricoVO.setRistampaDaFattura(false);
			}

			List parametri=new ArrayList();
			parametri.add(buoniCaricoVO);
			request.setAttribute("DatiVo", parametri);


			parametri.add(buoniCaricoVO);


			UserVO user = (UserVO)request.getSession().getAttribute(Constants.UTENTE_KEY);
			String utente= user.getUserId();


			String tipoFormato=currentForm.getTipoFormato();

			request.setAttribute("DatiVo", inputForStampeService);
			request.setAttribute("TipoFormato", tipoFormato);

			String fileJrxml = currentForm.getTipoModello();//nome modello
//			String fileJrxml = currentForm.getModello().getJrxml()+".jrxml";
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);

			String pathJrxml = null;
			pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+ fileJrxml;
			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathDownload = StampeUtil.getBatchFilesPath();


			//codice standard inserimento messaggio di richiesta stampa differita
			StampaDiffVO stam = new StampaDiffVO();
			stam.setTipoStampa(currentForm.getTipoFormato());
			stam.setUser(Navigation.getInstance(request).getUtente().getUserId());
			stam.setCodPolo(currentForm.getCodPolo());
			stam.setCodBib(currentForm.getCodBib());

			stam.setTipoOrdinamento("");
			stam.setParametri(parametri);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setCodAttivita(CodiciAttivita.getIstance().GA_STAMPA_BUONI_DI_CARICO);
			stam.setTipoOperazione(currentForm.getTipoOperazione());
			stam.setTicket(currentForm.getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String idMessaggio = factory.getStampeOnline().stampaBuoniCarico(stam);

			ActionMessages errors = new ActionMessages();
			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));

			this.saveErrors(request, errors);


			currentForm.setDisable(true);
			return mapping.getInputForward();
		} catch (ValidationException e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
		}
		return mapping.getInputForward();
	}

	private ActionForward validaInputFattura(ActionMapping mapping,
			HttpServletRequest request, StampaBuoniCaricoForm currentForm) {
		if (!ValidazioneDati.strIsNull(currentForm.getAnnoFattura())) {
			if (!ValidazioneDati.strIsNumeric(currentForm.getAnnoFattura())) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.annoFatturaNumerico"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				return mapping.getInputForward();
			}else{
				if (currentForm.getAnnoFattura().equals("0")){
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.annoFatturaDeveEssereMaggioreDi0"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
			if (currentForm.getAnnoFattura().length() > 4) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.annoFatturaMax4"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		if (!ValidazioneDati.strIsNull(currentForm.getNumFattura())) {
			if (currentForm.getNumFattura().length() > 20) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.validaInvNumFatturaEccedente"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}else{
			if (currentForm.getNumFattura().trim().equals("")) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.validaInvNumFatturaObbligatorio"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

		}

		int prgFattura = 0;
		if (!ValidazioneDati.strIsNull(currentForm.getNumFattura())) {
			if (ValidazioneDati.strIsNull(currentForm.getAnnoFattura())){
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.annoFatturaObbligatorio"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			ListaSuppFatturaVO ricercaFatture=new ListaSuppFatturaVO();
			ricercaFatture.setCodBibl(currentForm.getCodBib());
			ricercaFatture.setNumFattura(currentForm.getNumFattura());
			ricercaFatture.setAnnoFattura(currentForm.getAnnoFattura());
			ricercaFatture.setTipoFattura("F");
			ricercaFatture.setTicket(currentForm.getTicket());
			List listaFattura = null;
			try{
				listaFattura = this.getRicercaListaFatture(ricercaFatture);
			} catch (ValidationException ve) {
				ActionMessages errors = new ActionMessages();
				if (ve.getMessage() != null && ve.getMessage().equals("assenzaRisultati")){
					errors.add("generico", new ActionMessage("error.documentofisico.fatturaNonTrovata"));
				}
				this.saveErrors(request, errors);
				this.saveToken(request);
				return mapping.getInputForward();
			} catch (DataException ve) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico."+ ve.getMessage()));
				this.saveErrors(request, errors);
				this.saveToken(request);
				return mapping.getInputForward();
			} catch (Exception e) { // altri tipi di errore
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return null;
	}


	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return Navigation.getInstance(request).goBack();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		StampaBuoniCaricoForm currentForm = ((StampaBuoniCaricoForm) form);
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE, currentForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private void loadTipiOrdinamento(ActionForm form) throws Exception {

		StampaBuoniCaricoForm currentForm = ((StampaBuoniCaricoForm) form);
		List lista = new ArrayList();
		CodiceVO rec = new CodiceVO("Inv","serie + inventario");
		lista.add(rec);
		rec = new CodiceVO("Data","data di ingresso + serie + inventario");
		lista.add(rec);
		rec = new CodiceVO("Coll","sezione + collocazione + specificazione");
		lista.add(rec);
		currentForm.setListaTipiOrdinamento(lista);
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GA_STAMPA_BUONI_DI_CARICO);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private List getRicercaListaFatture(ListaSuppFatturaVO fattura)
	throws Exception   {

		List risultato;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		risultato = factory.getGestioneAcquisizioni().getRicercaListaFatture(fattura);
		return risultato;

	}

}
