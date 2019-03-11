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
package it.iccu.sbn.web.actions.gestionestampe.conservazione;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroConservazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionestampe.conservazione.StampaRegistroConservazioneForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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

public class StampaRegistroConservazioneAction extends RicercaInventariCollocazioniAction {

	private static Log log = LogFactory.getLog(StampaRegistroConservazioneAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerCollocazione", "selColloc");
		map.put("documentofisico.selPerInventari", "selInv");
		map.put("documentofisico.lsSez", "listaSupportoSez");
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		StampaRegistroConservazioneForm currentForm = (StampaRegistroConservazioneForm) form;
		if (!currentForm.isSessione()) {

			try {

			} catch (Exception e) {
				LinkableTagUtils.resetErrors(request);
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.erroreDefault"));

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

			StampaRegistroConservazioneForm myForm = ((StampaRegistroConservazioneForm) form);
			super.unspecified(mapping, myForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			Navigation navi = Navigation.getInstance(request);
			if (!myForm.isSessione()) {
				myForm.setTicket(navi.getUserTicket());
				UserVO utente = navi.getUtente();
				myForm.setCodPolo(utente.getCodPolo());
				myForm.setCodBib(utente.getCodBib());
				myForm.setDescrBib(utente.getBiblioteca());
				log.info("AggiornamentoDisponibilitaAction::unspecified");
				loadDefault(request, mapping, form);

				myForm.setSessione(true);
			}

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			myForm.setListaCodStatoConservazione(caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE,true)));
			myForm.setListaTipoMateriale(caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,true)));
			this.loadTipiOrdinamento(myForm);

			myForm.setSerie("");
			myForm.setStartInventario("0");
			myForm.setEndInventario("0");
			if (navi.isFromBar() ){
				return mapping.getInputForward();
			}
//			myForm.setElencoModelli(getElencoModelli());
//			myForm.setTipoFormato(TipoStampa.HTML.name());

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
			myForm.setElencoModelli(getElencoModelli());
			myForm.setTipoFormato(TipoStampa.PDF.name());
		} catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ ve.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward conferma (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		StampaRegistroConservazioneForm myForm = ((StampaRegistroConservazioneForm) form);
		try{
			StampaRegistroConservazioneVO regConsVO = new StampaRegistroConservazioneVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			regConsVO.setCodPolo(myForm.getCodPolo());
			regConsVO. setCodBib(myForm.getCodBib());
			//
			regConsVO. setTipoOrdinamento(myForm.getTipoOrdinamento());
			//
			regConsVO.setTipoMateriale(myForm.getCodiceTipoMateriale());
			regConsVO.setStatoConservazione(myForm.getCodiceStatoConservazione());
			//			//Ricerca per collocazioni
			if (myForm.getFolder().equals("Collocazioni")){
				myForm.setTipoOperazione("S");
				super.validaInputCollocazioni(mapping, request, myForm);
				regConsVO.setCodPoloSez(myForm.getCodPoloSez());
				regConsVO.setCodBibSez(myForm.getCodBibSez());
				regConsVO.setSezione(myForm.getSezione());
				regConsVO.setDallaCollocazione(myForm.getDallaCollocazione());
				regConsVO.setDallaSpecificazione(myForm.getDallaSpecificazione());
				regConsVO.setAllaCollocazione(myForm.getAllaCollocazione());
				regConsVO.setAllaSpecificazione(myForm.getAllaSpecificazione());
			}

			//Ricerca per range di inventari
			if (myForm.getFolder().equals("RangeInv")){
				myForm.setTipoOperazione("R");
					super.validaInputRangeInventari(mapping, request, myForm);
					//validazione startInv e endInv
					regConsVO.setSerie(myForm.getSerie());
					regConsVO.setEndInventario(myForm.getEndInventario());
					regConsVO.setStartInventario(myForm.getStartInventario());
			}

			//Ricerca per inventari
			if (myForm.getFolder().equals("Inventari")){
				myForm.setTipoOperazione("N");
				super.validaInputInventari(mapping, request, myForm);
				regConsVO.setListaInventari(myForm.getListaInventari());
				regConsVO.setSelezione(myForm.getSelezione());
				if (myForm.getSelezione() != null && (myForm.getSelezione().equals("F"))){
					regConsVO.setNomeFileAppoggioInv(myForm.getNomeFileAppoggioInv());
				}
			}
			if (myForm.getTipoFormato() != null && myForm.getTipoFormato().equals("XLS")){
				if (myForm.getTipoModello() != null && !myForm.getTipoModello().trim().endsWith("xls")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.modelloNonCoincideConFormatoStampa"));

					resetToken(request);
					return mapping.getInputForward();
				}
			}else{
				if (myForm.getTipoModello() != null && myForm.getTipoModello().trim().endsWith("xls")){

					LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.modelloNonCoincideConFormatoStampa"));

					resetToken(request);
					return mapping.getInputForward();
				}
			}

			regConsVO.setTipoOperazione(myForm.getTipoOperazione());
			regConsVO.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE);
			regConsVO.setUser(Navigation.getInstance(request).getUtente().getUserId());
			ModelloStampaVO rec = null;
			for (int index2 = 0; index2 < myForm.getElencoModelli().size(); index2++) {
				ModelloStampaVO recMod = myForm.getElencoModelli().get(index2);
				rec = new ModelloStampaVO();
				if (recMod.getJrxml() != null && recMod.getJrxml().equals(myForm.getTipoModello())){
					regConsVO.setNomeSubReport(recMod.getSubReports().get(0) + ".jasper");
				}
			}

			//ho finito di preparare il VO, ora lo metto nell'arraylist che passerò alla coda.
			List parametri=new ArrayList();
			parametri.add(regConsVO);
			request.setAttribute("DatiVo", parametri);


			String fileJrxml = myForm.getTipoModello();//nome modello
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+fileJrxml;
			//NB: Se voglio memorizzare sul server
			//String pathDownload = basePath+File.separator+"download";
			String pathDownload = StampeUtil.getBatchFilesPath();

			//Se voglio memorizzare in locale

			//codice standard inserimento messaggio di richiesta stampa differita
			StampaDiffVO stam = new StampaDiffVO();
			stam.setTipoStampa(myForm.getTipoFormato());
			stam.setUser(Navigation.getInstance(request).getUtente().getUserId());
			stam.setCodPolo(myForm.getCodPolo());
			stam.setCodBib(myForm.getCodBib());

			stam.setTipoOrdinamento(myForm.getTipoOrdinamento());
			stam.setParametri(parametri);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE);
			stam.setTipoOperazione(myForm.getTipoOperazione());
			stam.setTicket(myForm.getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String idMessaggio = factory.getStampeOnline().stampaRegistroConservazione(stam);


			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			LinkableTagUtils.addError(request, new ActionMessage("errors.finestampa" , idMessaggio));



			//myForm.setDisable(true);
			return mapping.getInputForward();
		} catch (ValidationException e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

		} catch (Exception e) { // altri tipi di errore

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

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


		StampaRegistroConservazioneForm myForm = ((StampaRegistroConservazioneForm) form);
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	private void loadTipiOrdinamento(ActionForm form) throws Exception {

		StampaRegistroConservazioneForm myForm = ((StampaRegistroConservazioneForm) form);
		List lista = new ArrayList();
		CodiceVO rec = new CodiceVO("C","sezione + collocazione + specificazione");
		lista.add(rec);
		rec = new CodiceVO("I","serie + inventario");
		lista.add(rec);
		rec = new CodiceVO("D","data di ingresso + serie + inventario");
		lista.add(rec);
		myForm.setListaTipiOrdinamento(lista);
	}

	private List getElencoModelli() {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List<ModelloStampaVO> listaModelli = factory.getCodici().getModelliStampaPerAttivita(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE);
			return listaModelli;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
}
