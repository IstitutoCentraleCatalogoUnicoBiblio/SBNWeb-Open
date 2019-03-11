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
package it.iccu.sbn.web.actions.gestionestampe.periodici;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaListaFascicoliVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.StatoFascicolo;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerStampaOrdineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.validators.periodici.RicercaKardexPeriodicoValidator;
import it.iccu.sbn.web.actionforms.gestionestampe.periodici.StampaListaFascicoliForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
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

public class StampaListaFascicoliAction extends RicercaInventariCollocazioniAction {

	private static Log log = LogFactory.getLog(StampaListaFascicoliAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("documentofisico.selFornitore", "selFornitore");
		map.put("documentofisico.selOrdine", "selOrdine");
		map.put("button.conferma","conferma");
		map.put("button.indietro","indietro");
		return map;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try{
			// setto il token per le transazioni successive
			this.saveToken(request);

			StampaListaFascicoliForm myForm = ((StampaListaFascicoliForm) form);
			super.unspecified(mapping, myForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			Navigation navi = Navigation.getInstance(request);
			if (!myForm.isSessione()) {
				UserVO utente = navi.getUtente();
				myForm.setTicket(utente.getTicket());
				myForm.setCodPolo(utente.getCodPolo());
				myForm.setCodBib(utente.getCodBib());
				myForm.setDescrBib(utente.getBiblioteca());
				log.info("StampaListaFascicoliAction::unspecified");
				loadDefault(request, mapping, form);

				myForm.setSessione(true);
			}

			//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
			// o di ritorno da lista ordini
			ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)request.getAttribute("passaggioListaSuppOrdiniVO");
			if (ricOrd==null){ // test di ritorno da lista ordini
				ricOrd=(ListaSuppOrdiniVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			}

			if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))	{
				if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 ){
					String chiave = ricOrd.getSelezioniChiamato().get(0).getChiave();
					if (chiave!=null && chiave.length()!=0 ){
						String [] ord = chiave.split("\\|");
						if (ord.length > 0 && ord.length == 4){
							myForm.setAnno(ord[2]);
							myForm.setTipo(ord[1]);
							myForm.setCodice(ord[3]);
						}
					}
				}
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
 			}

			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						myForm.setCodFornitore(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						myForm.setFornitore(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					myForm.setCodFornitore("");
					myForm.setFornitore("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

 			}

			CaricamentoCombo caricaCombo = new CaricamentoCombo();
			myForm.setListaTipo(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_ORDINE, true)));
			myForm.setListaStatoOrdine(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_STATO_ORDINE, true)));
			myForm.setListaTipoOrdine(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_ORDINE, true)));
			myForm.setListaPeriodicita(caricaCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_PERIODICITA, true)));

			myForm.setListaStatoFascicolo(this.loadStatoFascicolo());
			myForm.setListaOrdinamento(this.loadOrdinamento());
			myForm.setListaStampaNote(this.loadStampaNote());

			if (!ValidazioneDati.in(myForm.getFolder(), "Fornitore", "Ordine") )
				super.selFornitore(mapping, myForm, request, response);

			if (navi.isFromBar())
				return mapping.getInputForward();

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


			myForm.setElencoModelli(CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().STAMPA_FASCICOLI));
			myForm.setTipoFormato(TipoStampa.XLS.name());

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
		StampaListaFascicoliForm myForm = ((StampaListaFascicoliForm) form);
		try{
			StampaListaFascicoliVO inputVO = new StampaListaFascicoliVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			//tipo stampa obbligatorio
			String statoFascicolo = myForm.getStatoFascicolo();
			if (!ValidazioneDati.isFilled(statoFascicolo) ) {
				throw new ValidationException("statoFascicoloObbligatorio");
			}else{
				inputVO.setStatoFascicolo(statoFascicolo);
			}

			//anno iniziale
			if (myForm.getAnnoIniziale() == null || (ValidazioneDati.strIsAlfabetic(myForm.getAnnoIniziale().trim()))){
				throw new ValidationException(("annoInizialeFormatoNumerico"));
			}else{
				if (!myForm.getAnnoIniziale().trim().equals("")){
					if (myForm.getAnnoIniziale().trim().length()!= 4){
						throw new ValidationException(("annoInizialeLunghezzaInt4"));
					}
					inputVO.setAnnoIniziale(myForm.getAnnoIniziale());
				}
			}
			//anno finale
			if (myForm.getAnnoFinale() == null || (ValidazioneDati.strIsAlfabetic(myForm.getAnnoFinale().trim()))){
				throw new ValidationException(("annoInizialeFormatoNumerico"));
			}else{
				if (!myForm.getAnnoFinale().trim().equals("")){
					if (myForm.getAnnoFinale().trim().length()!= 4){
						throw new ValidationException(("annoFinaleLunghezzaInt4"));
					}
					inputVO.setAnnoFinale(myForm.getAnnoFinale());
				}
			}

			inputVO.setCodPolo(myForm.getCodPolo());
			inputVO.setCodBib(myForm.getCodBib());

			String cd_per = myForm.getPeriodicita();
			if (myForm.getFolder().equals("Fornitore")){
				myForm.setTipoOperazione("F");
				inputVO.setCodFornitore(myForm.getCodFornitore());
				inputVO.setDescrFornitore(myForm.getFornitore());
				inputVO.setPeriodicita(cd_per);
				inputVO.setStatoOrdine(myForm.getStatoOrdine());
				inputVO.setTipoOrdine(myForm.getTipoOrdine());
				//dati ordine ripuliti
				inputVO.setAnnoOrd("");
				inputVO.setTipoOrd("");
				inputVO.setCodiceOrd("");
			}
			if (myForm.getFolder().equals("Ordine")){
				myForm.setTipoOperazione("O");

				if (myForm.getAnno()!= null && myForm.getAnno().trim().equals("")){
					if (myForm.getTipo()!= null && myForm.getTipo().trim().equals("")){
						if (myForm.getCodice()!= null && myForm.getCodice().trim().equals("")){
						}else{
							throw new ValidationException(("indicareAnnoTipoNumero"));
						}
					}else{
						throw new ValidationException(("indicareAnnoTipoNumero"));
					}
				}else{
					if (myForm.getTipo()!= null && myForm.getTipo().trim().equals("")){
						throw new ValidationException(("indicareAnnoTipoNumero"));
					}else{
						if (myForm.getCodice()!= null && myForm.getCodice().trim().equals("")){
							throw new ValidationException(("indicareAnnoTipoNumero"));
						}else{
						}
					}
					if (myForm.getAnno() == null || (ValidazioneDati.strIsAlfabetic(myForm.getAnno().trim()))){
						throw new ValidationException(("annoOrdineFormatoNumerico"));
					}else{
						if (!myForm.getAnno().trim().equals("")){
							if (myForm.getAnno().trim().length()!= 4){
								throw new ValidationException(("annoOrdineLunghezzaInt4"));
							}
							inputVO.setAnnoOrd(myForm.getAnno());
						}
					}
					if (myForm.getCodice() == null || (ValidazioneDati.strIsAlfabetic(myForm.getCodice().trim()))){
						throw new ValidationException(("codiceFormatoNumerico"));
					}else{
						if (!myForm.getCodice().trim().equals("")){
							if (myForm.getCodice().trim().length()> 9){
								throw new ValidationException(("codiceLunghezzaMax9"));
							}
							inputVO.setCodiceOrd(myForm.getCodice());
						}
					}
					inputVO.setTipoOrd(myForm.getTipo());

				}
				//dati fornitore ripuliti
				inputVO.setCodFornitore("");
				inputVO.setDescrFornitore("");
				inputVO.setPeriodicita("");
				inputVO.setStatoOrdine("");
				inputVO.setTipoOrdine("");
			}

			/*
			if (myForm.getTipoFormato() != null && myForm.getTipoFormato().equals("XLS")){
				if (myForm.getTipoModello() != null && !myForm.getTipoModello().trim().endsWith("xls")){
					ActionMessages errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("error.documentofisico.modelloNonCoincideConFormatoStampa"));
					this.saveErrors(request, errors);
					resetToken(request);
					return mapping.getInputForward();
				}
			}
			*/
			//ordinamento
			inputVO.setOrdinamento(myForm.getOrdinamento());
			//stampaNote
			inputVO.setStampaNote(myForm.getStampaNote());
			//dati modello
			inputVO.setTipoOperazione(myForm.getTipoOperazione());
			inputVO.setCodAttivita(CodiciAttivita.getIstance().STAMPA_FASCICOLI);
			UserVO utente = Navigation.getInstance(request).getUtente();
			inputVO.setUser(utente.getUserId());
			for (int index2 = 0; index2 < myForm.getElencoModelli().size(); index2++) {
				ModelloStampaVO recMod = myForm.getElencoModelli().get(index2);
				if (recMod.getJrxml() != null){
					inputVO.setNomeSubReport(recMod.getSubReports().get(0) + ".jasper");
					myForm.setTipoModello(recMod.getJrxml()+".jrxml");
				}
			}

			//ho finito di preparare il VO, ora lo metto nell'arraylist che passerò alla coda.
			List parametri=new ArrayList();
			parametri.add(inputVO);
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
			stam.setUser(utente.getUserId());
			stam.setCodPolo(myForm.getCodPolo());
			stam.setCodBib(myForm.getCodBib());

			stam.setTipoOrdinamento(myForm.getOrdinamento());
			stam.setParametri(parametri);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
			stam.setDownloadLinkPath("/");
			stam.setCodAttivita(CodiciAttivita.getIstance().STAMPA_FASCICOLI);
			stam.setTipoOperazione(myForm.getTipoOperazione());
			stam.setTicket(myForm.getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);

			//almaviva5_20120615
			inputVO.setParametri(preparaParametriRicerca(request, form, inputVO));

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			String idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), stam, null);

			idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO
			LinkableTagUtils.addError(request, new ActionMessage("errors.finestampa" , idMessaggio));

			myForm.setDisable(true);
			return mapping.getInputForward();

		} catch (ValidationException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error == SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			else
				LinkableTagUtils.addError(request, e);

		} catch (ApplicationException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error == SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));
			else
				LinkableTagUtils.addError(request, e);

		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
		}
		return mapping.getInputForward();
	}

	private RicercaKardexPeriodicoPerStampaOrdineVO preparaParametriRicerca(
			HttpServletRequest request, ActionForm form, StampaListaFascicoliVO inputVO) throws ValidationException {

		RicercaKardexPeriodicoPerStampaOrdineVO ricerca = new RicercaKardexPeriodicoPerStampaOrdineVO();
		StampaListaFascicoliForm currentForm = ((StampaListaFascicoliForm) form);
		String statoFascicolo = currentForm.getStatoFascicolo();

		switch (Integer.valueOf(statoFascicolo)) {
		case 1:	//Fascicoli attesi
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.ATTESO);
			break;

		case 2:	//Fascicoli attesi ed in lacuna
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.ATTESO);
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.LACUNA);
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.SOLLECITO);
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.RECLAMO);
			break;

		case 3:	//Fascicoli in lacuna
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.LACUNA);
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.SOLLECITO);
			ricerca.addFiltroStatoFascicolo(StatoFascicolo.RECLAMO);
			break;

		case 4:	//Schedone abbonamento
			break;

		default:
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.label.stampa");
		}

		UserVO utente = Navigation.getInstance(request).getUtente();
		inputVO.setDescrBib(utente.getBiblioteca() );

		SerieOrdineVO ordine = new SerieOrdineVO();
		if (ValidazioneDati.equals(inputVO.getTipoOperazione(), "O")) {
    		try {
				ordine.setCod_bib_ord(inputVO.getCodBib());
				ordine.setAnno_ord(Integer.valueOf(inputVO.getAnnoOrd()));
				ordine.setCod_tip_ord(inputVO.getTipoOrd().charAt(0));
				ordine.setCod_ord(Integer.valueOf(inputVO.getCodiceOrd()));

			} catch (Exception e) {
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "documentofisico.selOrdine");
			}

    		if (ordine.isEmpty())
    			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "documentofisico.selOrdine");
		}

		if (ValidazioneDati.equals(inputVO.getTipoOperazione(), "F"))
			try {
				ricerca.setStato_ordine(ValidazioneDati.isFilled(currentForm.getStatoOrdine()) ?
						currentForm.getStatoOrdine().charAt(0) : null);
				ordine.setCod_tip_ord(ValidazioneDati.isFilled(currentForm.getTipoOrdine()) ?
						currentForm.getTipoOrdine().charAt(0) : '\u0000');
				String codFornitore = inputVO.getCodFornitore();
				if (ValidazioneDati.isFilled(codFornitore))
					ordine.setId_fornitore(Integer.valueOf(codFornitore) );
			} catch (Exception e) {
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "ordine.label.fornitore");
			}


		BibliotecaVO biblioteca = (new BibliotecaVO());
		biblioteca.setCod_polo(inputVO.getCodPolo());
		biblioteca.setCod_bib(inputVO.getCodBib());

		ricerca.setBiblioteca(biblioteca);
		ricerca.setOggettoRicerca(ordine);

		String inizio = inputVO.getAnnoIniziale();
		if (ValidazioneDati.isFilled(inizio))
			ricerca.setDataFrom(inizio);
		String fine = inputVO.getAnnoFinale();
		if (ValidazioneDati.isFilled(fine))
			ricerca.setDataTo(fine);

		ricerca.setIncludiNote(ValidazioneDati.equals(currentForm.getStampaNote(), "02"));

		ricerca.validate(new RicercaKardexPeriodicoValidator());

		return ricerca;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {

		return mapping.getInputForward();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		return false;
	}

	private List loadStatoFascicolo() throws Exception {
		List lista = new ArrayList();
		lista.add(new StrutturaCombo("01", "Fascicoli attesi"));
		lista.add(new StrutturaCombo("02", "Fascicoli attesi ed in lacuna"));
		lista.add(new StrutturaCombo("03", "Fascicoli in lacuna"));
		lista.add(new StrutturaCombo("04", "Schedone abbonamento"));
		return lista;
	}

	private List loadOrdinamento() throws Exception {
		List lista = new ArrayList();
		lista.add(new StrutturaCombo("01", "Fornitore (nome fornitore)"));
		lista.add(new StrutturaCombo("02", "Cronologico (data ordine)"));
		lista.add(new StrutturaCombo("03", "Tipologia ordine"));
		lista.add(new StrutturaCombo("04", "Stato amministrativo"));
		return lista;
	}

	private List loadStampaNote() throws Exception {
		List lista = new ArrayList();
		lista.add(new StrutturaCombo("01", "No"));
		lista.add(new StrutturaCombo("02", "Sì"));
		return lista;
	}

}
