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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite.AggiornamentoDisponibilitaForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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

public class AggiornamentoDisponibilitaAction extends RicercaInventariCollocazioniAction {


	private static Log log = LogFactory.getLog(AggiornamentoDisponibilitaAction.class);
	private CaricamentoCombo caricaCombo = new CaricamentoCombo();

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("documentofisico.selPerRangeInv", "selRangeInv");
		map.put("documentofisico.selPerCollocazione", "selColloc");
		map.put("documentofisico.selPerInventari", "selInv");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.ok", "ok");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		AggiornamentoDisponibilitaForm currentForm = (AggiornamentoDisponibilitaForm) form;
		if (!currentForm.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
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
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try{
		// setto il token per le transazioni successive
		this.saveToken(request);

		AggiornamentoDisponibilitaForm myForm = (AggiornamentoDisponibilitaForm) form;
		super.unspecified(mapping, myForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

		// controllo se ho già i dati in sessione;
		if (!myForm.isSessione()) {
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			log.info("AggiornamentoDisponibilitaAction::unspecified");
			loadDefault(request, mapping, form);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
//			myForm.setListaCodTipoFruizione(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodiceCategoriaDiFruizione()));
			myForm.setListaCodTipoFruizione(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE,true)));
			myForm.setListaCodNoDispo(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_NON_DISPONIBILITA,true)));
			if (myForm.getListaCodNoDispo().size() > 0){
				CodiceVO rec = new CodiceVO();
				rec.setCodice("Z");
				rec.setDescrizione("Nessuno");
				myForm.getListaCodNoDispo().add(rec);
			}else{
				throw new ValidationException("dropNoDispVuota");
			}
			myForm.setListaCodRiproducibilita(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM,true)));
			if (myForm.getListaCodRiproducibilita().size() > 0){
				CodiceVO rec = new CodiceVO();
				rec.setCodice("Z");
				rec.setDescrizione("Nessuno");
				myForm.getListaCodRiproducibilita().add(rec);
			}else{
				throw new ValidationException("dropRiprodVuota");
			}
//			myForm.setListaStatus(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodiceStatoConservazione()));
			myForm.setListaStatoCons(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE,true)));
			myForm.setListaTipoDigit(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_TIPO_DIGITALIZZAZIONE,true)));
			// filtri
			myForm.setListaLivAut(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_LIVELLO_AUTORITA,true)));

			// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
			myForm.setListaTipoData(this.caricaCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE, true)));

			List<ComboCodDescVO> listaRidotta = caricaNature();
//			myForm.setListaNatura(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodiceNatura()));
			myForm.setListaNatura(listaRidotta);
			//			}
			myForm.setFiltroDataIngressoDa("00/00/0000");
			myForm.setFiltroDataIngressoA("00/00/0000");
			myForm.setNomeFileAppoggio(null);
			myForm.setSerie("");
			myForm.setStartInventario("0");
			myForm.setEndInventario("0");
			myForm.setTipoOperazione("R");

			myForm.setSessione(true);
		}


		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		myForm.setListaCodTipoFruizione(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE,true)));
		myForm.setListaCodNoDispo(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_NON_DISPONIBILITA,true)));
		if (myForm.getListaCodNoDispo().size() > 0){
			CodiceVO rec = new CodiceVO();
			rec.setCodice("Z");
			rec.setDescrizione("Nessuno");
			myForm.getListaCodNoDispo().add(rec);
		}else{
			throw new ValidationException("dropNoDispVuota");
		}
		myForm.setListaCodRiproducibilita(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM,true)));
		if (myForm.getListaCodRiproducibilita().size() > 0){
			CodiceVO rec = new CodiceVO();
			rec.setCodice("Z");
			rec.setDescrizione("Nessuno");
			myForm.getListaCodRiproducibilita().add(rec);
		}else{
			throw new ValidationException("dropRiprodVuota");
		}
		myForm.setListaStatoCons(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE,true)));
		myForm.setListaTipoDigit(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_TIPO_DIGITALIZZAZIONE,true)));
		// filtri
		myForm.setListaLivAut(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(CodiciType.CODICE_LIVELLO_AUTORITA,true)));
//		myForm.setListaNatura(this.caricaCombo.loadCodiceDesc(factory.getCodici().getCodiceNatura()));
		List<ComboCodDescVO> listaRidotta = caricaNature();
		myForm.setListaNatura(listaRidotta);
		// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
		myForm.setListaTipoData(this.caricaCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE, true)));

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

	} catch (ValidationException e) {
		ActionMessages errors = new ActionMessages();
		errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
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

	/**
	  * it.iccu.sbn.web.actions.documentofisico.elaborazioniDifferite
	  * AggiornamentoDisponibilitaAction.java
	  * caricaNature
	  * List<ComboCodDescVO>
	  * @return
	  *
	  *
	 */
	private List<ComboCodDescVO> caricaNature() {
		List<ComboCodDescVO> listaRidotta = new ArrayList<ComboCodDescVO>();
		ComboCodDescVO elem = null;
		elem = new ComboCodDescVO("","");
		listaRidotta.add(elem);
		elem = new ComboCodDescVO("M","Monografie (M, W)");
		listaRidotta.add(elem);
		elem = new ComboCodDescVO("S","Periodici");
		listaRidotta.add(elem);
		return listaRidotta;
	}

	public ActionForward listaSupportoBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		AggiornamentoDisponibilitaForm myForm = (AggiornamentoDisponibilitaForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_AGGIORNAMENTO_DISPONIBILITA_INVENTARI, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
//			return mapping.findForward("chiudi");
			return Navigation.getInstance(request).goBack(true);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		AggiornamentoDisponibilitaForm myForm = (AggiornamentoDisponibilitaForm) form;
		try {

			AggDispVO aggDispVO = new AggDispVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			aggDispVO.setCodPolo(myForm.getCodPolo());
			aggDispVO.setCodBib(myForm.getCodBib());
			//
			aggDispVO.setTicket(myForm.getTicket());
			if (myForm.getCodTipoFruizione().trim().equals("")
					&& myForm.getCodNoDispo().trim().equals("")
					&& myForm.getCodRip().trim().equals("")
					&& myForm.getCodiceStatoConservazione().trim().equals("")
					&& myForm.getCodDigitalizzazione().trim().equals("")){
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("error.documentofisico.validaInvIndicareAlmenoUnoTraCodFruiECodNoDisp"));
				this.saveErrors(request, errors);
				resetToken(request);
				request.setAttribute("currentForm", myForm);
				return mapping.getInputForward();
			}

			if (myForm.getCodTipoFruizione().trim().equals("")){
				aggDispVO.setCodTipoFruizione("");
			}else{
				aggDispVO.setCodTipoFruizione(myForm.getCodTipoFruizione());
			}
			if (myForm.getCodNoDispo().trim().equals("")){
				aggDispVO.setCodNoDispo("");
			}else{
				aggDispVO.setCodNoDispo(myForm.getCodNoDispo());
			}
			if (myForm.getCodRip().trim().equals("")){
				aggDispVO.setCodRip("");
			}else{
				aggDispVO.setCodRip(myForm.getCodRip());
			}
			if (myForm.getCodiceStatoConservazione().trim().equals("")){
				aggDispVO.setCodiceStatoConservazione("");
			}else{
				aggDispVO.setCodiceStatoConservazione(myForm.getCodiceStatoConservazione());
			}
			if (myForm.getCodDigitalizzazione().trim().equals("")){
				aggDispVO.setCodDigitalizzazione("");
			}else{
				aggDispVO.setCodDigitalizzazione(myForm.getCodDigitalizzazione());
			}
			//filtri
			if (myForm.getFiltroNatura().trim().equals("")){
				aggDispVO.setFiltroCodNatura("");
			}else{
				aggDispVO.setFiltroCodNatura(myForm.getFiltroNatura());
			}
			if (myForm.getFiltroLivAut().trim().equals("")){
				aggDispVO.setFiltroLivAut("");
			}else{
				aggDispVO.setFiltroLivAut(myForm.getFiltroLivAut());
			}

			// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
			if (myForm.getTipoData().trim().equals("")){
				aggDispVO.setTipoDataPubb("");
			}else{
				aggDispVO.setTipoDataPubb(myForm.getTipoData());
			}
			if (myForm.getAaPubbFrom().trim().equals("")){
				aggDispVO.setAaPubbFrom("");
			}else{
				aggDispVO.setAaPubbFrom(myForm.getAaPubbFrom());
			}
			if (myForm.getAaPubbTo().trim().equals("")){
				aggDispVO.setAaPubbTo("");
			}else{
				if (myForm.getAaPubbFrom().trim().equals("")){
					ActionMessages errors = new ActionMessages();
					errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.documentofisico.mancaData2"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				} else {
					aggDispVO.setAaPubbTo(myForm.getAaPubbTo());
				}
			}


			if (myForm.getFiltroTipoFruizione().trim().equals("")){
				aggDispVO.setFiltroCodTipoFruizione("");
			}else{
				aggDispVO.setFiltroCodTipoFruizione(myForm.getFiltroTipoFruizione());
			}
			if (myForm.getFiltroNoDispo().trim().equals("")){
				aggDispVO.setFiltroCodNoDisp("");
			}else{
				aggDispVO.setFiltroCodNoDisp(myForm.getFiltroNoDispo());
			}
			if (myForm.getFiltroRip().trim().equals("")){
				aggDispVO.setFiltroCodRip("");
			}else{
				aggDispVO.setFiltroCodRip(myForm.getFiltroRip());
			}
			if (myForm.getFiltroStatoConservazione().trim().equals("")){
				aggDispVO.setFiltroStatoConservazione("");
			}else{
				aggDispVO.setFiltroStatoConservazione((myForm.getFiltroStatoConservazione()));
			}
			if (myForm.getFiltroDataIngressoDa() != null &&
					(!myForm.getFiltroDataIngressoDa().trim().equals("") && myForm.getFiltroDataIngressoDa().equals("00/00/0000"))){
				ActionMessages errors = new ActionMessages();
				errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.formatoDataDaNonValido"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}	else if (myForm.getFiltroDataIngressoDa() != null &&
					(!myForm.getFiltroDataIngressoDa().trim().equals("") && !myForm.getFiltroDataIngressoDa().equals("00/00/0000"))){

						int codRitorno = ValidazioneDati.validaDataPassata(myForm.getFiltroDataIngressoDa());
						if (codRitorno != ValidazioneDati.DATA_OK){
							ActionMessages errors = new ActionMessages();
							errors = new ActionMessages();
							errors.add("generico", new ActionMessage("error.documentofisico.dataDaErrata"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
//				myForm.setStartInventario("0");
//				myForm.setEndInventario("0");
//				myForm.setCodPoloSez(null);
//				myForm.setCodBibSez(null);
//				myForm.setListaInventari(null);
//				myForm.setTipoOperazione("D");
			}
			aggDispVO.setFiltroDataIngressoDa(myForm.getFiltroDataIngressoDa());
			aggDispVO.setFiltroDataIngressoA(myForm.getFiltroDataIngressoA());


//			if (myForm.getTipoOperazione() == null	||
//					(myForm.getTipoOperazione() != null && !myForm.getTipoOperazione().equals("D"))){
				//Ricerca per collocazioni
				if (myForm.getFolder().equals("Collocazioni")){
					if (myForm.getCodPoloSez() == null && myForm.getCodBibSez() == null){
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.premereTastoSezione"));
						this.saveErrors(request, errors);
						request.setAttribute("currentForm", myForm);
						return mapping.getInputForward();
					}
					super.validaInputCollocazioni(mapping, request, myForm);
					myForm.setTipoOperazione("S");
					aggDispVO.setCodPoloSez(myForm.getCodPoloSez());
					aggDispVO.setCodBibSez(myForm.getCodBibSez());
					aggDispVO.setSezione(myForm.getSezione());
					aggDispVO.setDallaCollocazione(myForm.getDallaCollocazione());
					aggDispVO.setDallaSpecificazione(myForm.getDallaSpecificazione());
					aggDispVO.setAllaCollocazione(myForm.getAllaCollocazione());
					aggDispVO.setAllaSpecificazione(myForm.getAllaSpecificazione());
				}

				//			//Ricerca per range di inventari
				if (myForm.getFolder().equals("RangeInv")){
					super.validaInputRangeInventari(mapping, request, myForm);
					myForm.setTipoOperazione("R");
					//validazione startInv e endInv
					aggDispVO.setSerie(myForm.getSerie());
					aggDispVO.setEndInventario(myForm.getEndInventario());
					aggDispVO.setStartInventario(myForm.getStartInventario());
				}

				//				//Ricerca per inventari
				if (myForm.getFolder().equals("Inventari")){
					myForm.setTipoOperazione("N");
					super.validaInputInventari(mapping, request, myForm);
					aggDispVO.setListaInventari(myForm.getListaInventari());
					aggDispVO.setSelezione(myForm.getSelezione());
					if (myForm.getSelezione() != null && (myForm.getSelezione().equals("F"))){
						aggDispVO.setNomeFileAppoggioInv(myForm.getNomeFileAppoggioInv());
					}
				}
//			}
			aggDispVO.setTipoOperazione(myForm.getTipoOperazione());

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			// INIZIO PRENOTAZIONE AGGIORNAMENTO

			//
			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			aggDispVO.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			log.info("download path: " + downloadPath);
			String downloadLinkPath = "/";
			aggDispVO.setDownloadPath(downloadPath);
			aggDispVO.setDownloadLinkPath(downloadLinkPath);
			aggDispVO.setTicket(Navigation.getInstance(request).getUserTicket());
			aggDispVO.setUser(Navigation.getInstance(request).getUtente().getUserId());
			aggDispVO.setCodAttivita(CodiciAttivita.getIstance().GDF_AGGIORNAMENTO_DISPONIBILITA_INVENTARI);
			String s = factory.getElaborazioniDifferite().aggiornaDisponibilita(aggDispVO);

			if (s == null) {
				ActionMessages errors = new ActionMessages();
				errors.add("Attenzione", new ActionMessage("error.documentofisico.prenotAggiornamentoDisponibilitaFallita"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

			ActionMessages errors = new ActionMessages();
			errors.add("Attenzione", new ActionMessage("error.documentofisico.prenotAggiornamentoDisponibilitaOk", s.toString()));
			this.saveErrors(request, errors);
			resetToken(request);
			myForm.setDisable(true);
			return mapping.getInputForward();
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

}
