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
package it.iccu.sbn.web.actions.elaborazioniDifferite.esporta;

import it.iccu.sbn.batch.unimarc.ExportUnimarcBatch;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotechePoloVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoEstrazioneUnimarc;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoOutput;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta.EstrattoreIdDocumentiForm;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta.EstrattoreIdDocumentiForm.TipoProspettazioneExport;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class EstrattoreIdDocumentiAction extends RicercaInventariCollocazioniAction implements SbnAttivitaChecker {

	private static final String LISTA_BIBLIOTECHE = "sif.bib.export.unimarc";

	private static Logger log = Logger.getLogger(EstrattoreIdDocumentiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("button.prenotaEstr", "prenotaEstrazione");// metodo per esporta
		map.put("tab.daticatalografici", "selezDatiCatalografici");// metodo per selezione tabs
		map.put("tab.posseduto", "selezPosseduto");// metodo per selezione tabs
		map.put("button.cercabiblioteche", "listaSupportoBib");// metodi per cerca (lente)
		map.put("button.tutteLeBiblio", "tutteLeBiblio");
		map.put("documentofisico.bottone.SIFbibliotecarioIns", "SIFbibliotecarioInserimento"); // metodi per cerca bibliotecario inserimento/aggiornamento
		map.put("documentofisico.bottone.SIFbibliotecarioAgg", "SIFbibliotecarioAggiornamento"); // metodi per cerca bibliotecario inserimento/aggiornamento
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		try{
			super.unspecified(mapping, currentForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			// controllo se ho già i dati in sessione;
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			this.saveToken(request);
			// controllo se ho già i dati in sessione;
			if(!currentForm.isSessione())	{
				currentForm.setTicket(Navigation.getInstance(request).getUserTicket());
				currentForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				currentForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				currentForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
				this.loadPagina(form);
				currentForm.setSessione(true);
			}

			if (!currentForm.isInitialized()) {

				EsportaVO espVO = currentForm.getEsporta();

				long extractionTime = ExportUnimarcBatch.getDbLastExtractionTime();
				if (extractionTime > 0)
					currentForm.setExtractionTime((new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Timestamp(extractionTime))));
				else {
					espVO.setExportDB(true); // db mai esportato
					currentForm.setExtractionTime(null);
				}

				currentForm.setCodAttivita(CodiciAttivita.getIstance().ESPORTA_IDENTIFICATIVI_DOCUMENTO);
				currentForm.setTipoFormato(TipoStampa.PDF.name());
				navi.setTesto("Estrai identificativi Doc");

				UserVO utenteCollegato = navi.getUtente();
				setDefaultValues(currentForm, TipoProspettazioneExport.DATI_CATALOGRAFICI, utenteCollegato);
				setDefaultValues(currentForm, TipoProspettazioneExport.POSSEDUTO, utenteCollegato);
				setDefaultValues(currentForm, TipoProspettazioneExport.CLASSI, utenteCollegato);
				setDefaultValues(currentForm, TipoProspettazioneExport.BIBLIOTECHE,	utenteCollegato);

				currentForm.setTipoProspettazione(TipoProspettazioneExport.DATI_CATALOGRAFICI);

				// DEFAULT
				selezDatiCatalografici(mapping, currentForm, request, response);

				currentForm.setInitialized(true);
			}

			// BIBLIOTECHE
			EsportaVO esporta = currentForm.getEsporta();
			List<BibliotecaVO> sifBiblio = (List<BibliotecaVO>) request.getAttribute(LISTA_BIBLIOTECHE);
			if (ValidazioneDati.isFilled(sifBiblio)) {
				esporta.setListaBib(sifBiblio);
				StringBuilder buf = new StringBuilder();
				Iterator<BibliotecaVO> i = sifBiblio.iterator();
				for (;;) {
					buf.append(i.next().getCod_bib());
					if (i.hasNext())
						buf.append(", ");
					else
						break;
				}

				currentForm.setElencoBiblio(buf.toString());

				// almaviva5_20100407 se ho selezionato una sola biblioteca carico di dati di posseduto
				if (currentForm.isEstrattoreIdDoc()) {
					if (ValidazioneDati.size(sifBiblio) == 1 ) {
						BibliotecaVO bibSelezionata = sifBiblio.get(0);
						UserVO utenteCollegato = navi.getUtente();

						CaricamentoCombo carCombo = new CaricamentoCombo();
						if (currentForm.getCodBib() != null && !currentForm.getCodBib().equals(bibSelezionata.getCod_bib())){
							List<SerieVO> listaSerie = getListaSerie(bibSelezionata.getCod_polo(), bibSelezionata.getCod_bib(), utenteCollegato.getTicket());
							if (listaSerie != null && listaSerie.size() > 0){
								currentForm.setListaSerie(listaSerie);
								currentForm.setListaComboSerie(carCombo.loadCodice(listaSerie));
								currentForm.setListaPossedutoSerie(carCombo.loadCodice(getListaSerie(bibSelezionata.getCod_polo(),
										bibSelezionata.getCod_bib(), utenteCollegato.getTicket())));
							}else{
								currentForm.setDisableSerie(true);//non attivo
								currentForm.setDisableDalNum(true);//non attivo
								currentForm.setDisableAlNum(true);//non attivo
								throw new ValidationException("LaBibliotecaSceltaNonHaAlcunaSerieDefinita");
							}
						}else{
							currentForm.setListaPossedutoSerie(carCombo.loadCodice(getListaSerie(bibSelezionata.getCod_polo(),
									bibSelezionata.getCod_bib(), utenteCollegato.getTicket())));
						}

						esporta.setCodPoloSerie(bibSelezionata.getCod_polo());
						esporta.setCodBibSerie(bibSelezionata.getCod_bib());

					} else
						esporta.clearFiltriPosseduto();
				}
			}
			if (currentForm.getCodAttivita() != null && !(currentForm.getCodAttivita().equals(CodiciAttivita.getIstance().ESPORTA_IDENTIFICATIVI_DOCUMENTO))){
				currentForm.setDisableSez(false);
				currentForm.setDisableSerie(false);// attivo
				currentForm.setDisableDalNum(false);// attivo
				currentForm.setDisableAlNum(false);// attivo
			}else{
				if (currentForm.getElencoBiblio() != null && !currentForm.getElencoBiblio().equals("")){
					currentForm.setTastoCancBib(true);//attivo
					currentForm.setDisableSez(false);
					currentForm.setDisableInv(false);
					currentForm.setDisableSerie(false);// attivo
					currentForm.setDisableDalNum(false);// attivo
					currentForm.setDisableAlNum(false);// attivo
				}else{
					currentForm.setTastoCancBib(false);//non attivo
					currentForm.setDisableSez(true);//non attivo
					currentForm.setDisableSerie(true);//non attivo
					currentForm.setDisableDalNum(true);//non attivo
					currentForm.setDisableAlNum(true);//non attivo
				}
			}

			// Manutenzione almaviva2 21.09.2011 -  inserite le chiamate/nuovi campi per la lentina del biblotecario (ute ins e var)
			// richiamata dall'estrattore magno
			if (request.getAttribute("bibliotecario") != null) {
				UtenteVO bibliotecario = (UtenteVO)request.getAttribute("bibliotecario");
				if (currentForm.getTipoCodBibliotec() != null && currentForm.getTipoCodBibliotec().equals("INS")) {
					currentForm.setCodBibliotecIns(bibliotecario.getUsername());
					currentForm.setNomeBibliotecIns(bibliotecario.getNome()+" " + bibliotecario.getCognome());
				}
				if (currentForm.getTipoCodBibliotec() != null && currentForm.getTipoCodBibliotec().equals("AGG")) {
					currentForm.setCodBibliotecAgg(bibliotecario.getUsername());
					currentForm.setNomeBibliotecAgg(bibliotecario.getNome()+" " + bibliotecario.getCognome());
				}
			}


			// Viene settato il token per le transazioni successive
			this.saveToken(request);

		} catch (ValidationException ve) {
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ ve.getMessage()));

		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
		}

		return mapping.getInputForward();
	}


	public ActionForward selezDatiCatalografici(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		currentForm.setTipoProspettazione(TipoProspettazioneExport.DATI_CATALOGRAFICI);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		setDefaultValues(currentForm, TipoProspettazioneExport.DATI_CATALOGRAFICI, utenteCollegato);

		log.debug("TipoProspettazione di EstrattoreIdDocumentiForm: "	+ currentForm.getTipoProspettazione());
		return mapping.getInputForward();
	}

	public ActionForward selezPosseduto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		currentForm.setTipoProspettazione(TipoProspettazioneExport.POSSEDUTO);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		setDefaultValues(currentForm, TipoProspettazioneExport.POSSEDUTO, utenteCollegato);

		log.debug("TipoProspettazione di EstrattoreIdDocumentiForm: "	+ currentForm.getTipoProspettazione());

		if (ValidazioneDati.equals(currentForm.getCodAttivita(), CodiciAttivita.getIstance().ESPORTA_IDENTIFICATIVI_DOCUMENTO)
			&& ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1)
			LinkableTagUtils.addError(request, new ActionMessage("errors.esporta.selezionare.bib"));

		currentForm.getEsporta().setTipoEstrazione(TipoEstrazioneUnimarc.COLLOCAZIONI);
		super.unspecified(mapping, currentForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

		return mapping.getInputForward();
	}

	public ActionForward tutteLeBiblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;

		currentForm.getEsporta().setTipoEstrazione(TipoEstrazioneUnimarc.COLLOCAZIONI);
		super.unspecified(mapping, currentForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		loadPagina(currentForm);
		if (currentForm.getElencoBiblio() != null && !currentForm.getElencoBiblio().equals("")){
			currentForm.setElencoBiblio("");
			currentForm.setTastoCancBib(false);//non attivo
			currentForm.setDisableSez(true);//attivo
			currentForm.setDisableSerie(true);//attivo
			currentForm.setDisableDalNum(true);//attivo
			currentForm.setDisableAlNum(true);//attivo
			currentForm.setElencoBiblio(null);
			currentForm.getEsporta().setListaBib(null);
			}else{
			currentForm.setTastoCancBib(true);// attivo
			currentForm.setDisableSez(false);//non attivo
			currentForm.setDisableInv(false);//non attivo
			currentForm.setDisableSerie(false);//attivo
			currentForm.setDisableDalNum(false);//attivo
			currentForm.setDisableAlNum(false);//attivo
		}
		return mapping.getInputForward();
	}

	public ActionForward selezClassificazioni(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		currentForm.setTipoProspettazione(TipoProspettazioneExport.CLASSI);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		setDefaultValues(currentForm, TipoProspettazioneExport.CLASSI,
				utenteCollegato);

		log.debug("TipoProspettazione di EstrattoreIdDocumentiForm: " + currentForm.getTipoProspettazione());

		return mapping.getInputForward();
	}


	public ActionForward prenotaEstrazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		String idBatch = null;

		try {
			resetToken(request);

			// Prova export
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// Setting biblioteca corrente (6 caratteri) codice polo + codice biblioteca
			UserVO utente = Navigation.getInstance(request).getUtente();
			EsportaVO esportaVO = currentForm.getEsporta().copy();
			esportaVO.setCodPolo(utente.getCodPolo());
			esportaVO.setCodBib(utente.getCodBib());
			esportaVO.setUser(utente.getUserId());

			esportaVO.setCodAttivita(CodiciAttivita.getIstance().ESPORTA_IDENTIFICATIVI_DOCUMENTO);


			// Aggiornamenti/correttivi su esportaVO per le casistiche di Estrattore Magno
			esportaVO.setAncheTitoli01(true);
			esportaVO.setCodScaricoSelez("ALL");
			if (currentForm.isEstrattoreIdDoc()) {
				esportaVO.validate();
				// Intervento L. almaviva2 06.07.2011 - Bug MANTIS 4510 (collaudo) vengono dichiarati i campi da utilizzare per
				// filtrare per data ts_var e data ts_ins (il filtro su data ins e data var ammette solo 4 chr, serve gg/mm/aaaa)
				esportaVO.validaDateTimeStamp("INS", currentForm.getDataInsFrom(),  currentForm.getDataInsTo());
				esportaVO.validaDateTimeStamp("AGG", currentForm.getDataAggFrom(),  currentForm.getDataAggTo());
				esportaVO.setUteIns(currentForm.getCodBibliotecIns());
				esportaVO.setUteVar(currentForm.getCodBibliotecAgg());

				//almaviva5_20121112 evolutive google
				if (esportaVO.getTipoOutput() == TipoOutput.INV && ValidazioneDati.size(esportaVO.getListaBib()) != 1)
					throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);
			}


			// se ho selezionato tutti i materiali é più efficente eliminare il filtro
			String[] materiali = esportaVO.getMateriali();
			if (ValidazioneDati.size(materiali) == EsportaVO.MATERIALI.length) {
				esportaVO.setMateriali(null);
			} else {
				// le collane hanno il tipo materiale a spazio, devo includerlo nel filtro
				if (ValidazioneDati.isFilled(materiali)) {
					if (Arrays.asList(esportaVO.getNature()).contains("C")) {
						List<String> tmp = new ArrayList<String>(Arrays.asList(materiali));
						tmp.add(" "); // aggiungo mat. fittizio
						esportaVO.setMateriali(tmp.toArray(materiali));
					}
				}
			}

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			esportaVO.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			esportaVO.setDownloadPath(downloadPath);
			esportaVO.setDownloadLinkPath("/"); // eliminato

			esportaVO.setTicket(utente.getTicket());

			TipoEstrazioneUnimarc tipoEstrazione = getTipoEstrazione(mapping, currentForm, request, response);//currentForm.getEsporta().getTipoEstrazione();
			if (tipoEstrazione == null)
				return mapping.getInputForward();
			esportaVO.setTipoEstrazione(tipoEstrazione);
			if (!ValidazioneDati.in(tipoEstrazione, TipoEstrazioneUnimarc.ARCHIVIO, TipoEstrazioneUnimarc.FILE)){
				if (ValidazioneDati.equals(currentForm.getCodAttivita(), CodiciAttivita.getIstance().ESPORTA_IDENTIFICATIVI_DOCUMENTO)
						&& ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.esporta.selezionare.bib"));
					request.setAttribute("currentForm", currentForm);
					return mapping.getInputForward();
				}
				//esporta per collocazioni
				if (tipoEstrazione == TipoEstrazioneUnimarc.COLLOCAZIONI) {
					if (currentForm.getCodPoloSez() == null && currentForm.getCodBibSez() == null) {
						LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.premereTastoSezione"));
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}
					currentForm.setFolder("Collocazione");
					//					if (currentForm.getFolder() != null && currentForm.getFolder().equals("Collocazione")){
					super.validaInputCollocazioni(mapping, request, currentForm);
					currentForm.setTipoOperazione("S");
					esportaVO.setCodPoloSez(utente.getCodPolo());
					esportaVO.setCodBibSez(utente.getCodBib());
					esportaVO.setPossCodSez(currentForm.getSezione());
					esportaVO.setPossDallaCollocazione(currentForm.getDallaCollocazione());
					esportaVO.setPossAllaCollocazione(currentForm.getAllaCollocazione());
					esportaVO.setPossSpecificazioneCollDa(currentForm.getDallaSpecificazione());
					esportaVO.setPossSpecificazioneCollA(currentForm.getAllaSpecificazione());
					esportaVO.setTipoCollocazione(currentForm.getTipoColloc());
					if (ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1)
						throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);

					currentForm.setFolder(null);
				} else {
					//					if (currentForm.getFolder() != null && currentForm.getFolder().equals("RangeInv")){
				 	super.validaInputRangeInventari(mapping, request, currentForm);
					currentForm.setTipoOperazione("R");
					esportaVO.setPossSerie(currentForm.getSerie());
					esportaVO.setPossDalNumero(currentForm.getStartInventario());
					esportaVO.setPossAlNumero(currentForm.getEndInventario());
					currentForm.setFolder(null);
					if (ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1)
						throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);
				}

			}
			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), esportaVO, null);

		} catch (ValidationException e) {
			log.error("", e);
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC)
				LinkableTagUtils.addError(request, e);
			else
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMessage()));

			return mapping.getInputForward();
		}

		if (idBatch != null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));
			return mapping.getInputForward();
		} else {
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
			return mapping.getInputForward();
		}

	}


	private TipoEstrazioneUnimarc getTipoEstrazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		TipoEstrazioneUnimarc type = TipoEstrazioneUnimarc.ARCHIVIO;
		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		EsportaVO esporta = currentForm.getEsporta();
		if (ValidazioneDati.isFilled(esporta.getListaBID()))
			return TipoEstrazioneUnimarc.FILE;

		if (ValidazioneDati.isFilled(esporta.getInputFile()))
			return TipoEstrazioneUnimarc.FILE;

		if (ValidazioneDati.isFilled(currentForm.getEsporta().getListaBib()) ) {
			RicercaInventariCollocazioniForm ricForm = (RicercaInventariCollocazioniForm) form;
			boolean coll = false;
			boolean rinv = false;
			try {
				boolean check = ValidazioneDati.isFilled(ricForm.getCodBibSez()) ||
					ValidazioneDati.isFilled(ricForm.getSezione()) ||
					ValidazioneDati.isFilled(ricForm.getTipoColloc()) ||
					ValidazioneDati.isFilled(ricForm.getDallaCollocazione()) ||
					ValidazioneDati.isFilled(ricForm.getAllaCollocazione()) ||
					ValidazioneDati.isFilled(ricForm.getDallaSpecificazione()) ||
					ValidazioneDati.isFilled(ricForm.getAllaSpecificazione());
				if (check) {
					validaInputCollocazioni(mapping, request, currentForm);
					type = TipoEstrazioneUnimarc.COLLOCAZIONI;
					coll = true;
				}

			} catch (Exception e) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				return null;
			}

			try {
				String startInventario = ricForm.getStartInventario();
				String endInventario = ricForm.getEndInventario();
				boolean start = ValidazioneDati.isFilled(startInventario) &&
					ValidazioneDati.strIsNumeric(startInventario) &&
					Integer.parseInt(startInventario) > 0;
				boolean end = ValidazioneDati.isFilled(endInventario) &&
						ValidazioneDati.strIsNumeric(endInventario) &&
						Integer.parseInt(endInventario) > 0;
				String serie = ricForm.getSerie();
				boolean check = (ValidazioneDati.isFilled(serie) || ValidazioneDati.in(serie, "", "   ")) && (start || end);
				if (check) {
					validaInputRangeInventari(mapping, request, currentForm);
					type = TipoEstrazioneUnimarc.SERIE_INVENTARIALE;
					rinv = true;
				}
			} catch (Exception e) {
				LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				return null;
			}

			if (coll && rinv)
				throw new ValidationException(SbnErrorTypes.UNI_SPECIFICARE_SOLO_UN_RANGE_POSSEDUTO);

		}

		return type;

	}



	public void setDefaultValues(EstrattoreIdDocumentiForm currentForm,
			TipoProspettazioneExport tipoProspettazione, UserVO utenteCollegato)
			throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		EsportaVO esportaVO = currentForm.getEsporta();

		List<?> listaCodiciAtenei = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_ATENEI));

		switch (tipoProspettazione) {
		case DATI_CATALOGRAFICI:
			// tab dati catalografici --- default (if null)
			esportaVO.setTipoEstrazione(TipoEstrazioneUnimarc.ARCHIVIO);
			if (currentForm.isInitialized())
				return;

			esportaVO.setTuttoArchivio(true);
			currentForm.setFolder(null);

			currentForm.setListaTipoData(carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE)));
			List<?> listaCodiciLingue = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_LINGUA));
			currentForm.setListaLingue(listaCodiciLingue);
			List<?> listaCodiciPaese = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PAESE));
			currentForm.setListaPaese(listaCodiciPaese);
			List<?> listaCodiciTipoRec = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC));
			currentForm.setListaTipoRecord(listaCodiciTipoRec);
			currentForm.setListaBiblioAteneo(listaCodiciAtenei);

			esportaVO.setMateriali(EsportaVO.MATERIALI);
			esportaVO.setNature(EsportaVO.NATURE);
			esportaVO.setDescTitoloDa("");
			esportaVO.setDescTitoloA("");
			break;

		case POSSEDUTO:
			// tab posseduto
			esportaVO.setTipoEstrazione(TipoEstrazioneUnimarc.COLLOCAZIONI);
			if (currentForm.isInitialized())
				return;

			currentForm.setListaPossedutoSerie(carCombo.loadCodice(getListaSerie(utenteCollegato.getCodPolo(),
							utenteCollegato.getCodBib(), utenteCollegato.getTicket())));
			currentForm.setListaCodTipoFruizione(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE)));
			currentForm.setListaCodNoDispo(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_NON_DISPONIBILITA,true)));
//			if (currentForm.getListaCodNoDispo().size() > 0){
//				CodiceVO rec = new CodiceVO();
//				rec.setCodice("Z");
//				rec.setDescrizione("Nessuno");
//				currentForm.getListaCodNoDispo().add(rec);
//			}else{
//				throw new ValidationException("dropNoDispVuota");
//			}
			currentForm.setListaCodRiproducibilita(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM,true)));
//			if (currentForm.getListaCodRiproducibilita().size() > 0){
//				CodiceVO rec = new CodiceVO();
//				rec.setCodice("Z");
//				rec.setDescrizione("Nessuno");
//				currentForm.getListaCodRiproducibilita().add(rec);
//			}else{
//				throw new ValidationException("dropRiprodVuota");
//			}
			currentForm.setListaCodStatoConservazione(carCombo.loadCodiceDesc(CodiciProvider.getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE,true)));

			//almaviva5_20130904 evolutive google2
			currentForm.setListaTipoDigit(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_DIGITALIZZAZIONE));
			break;
		}
	}

	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// EsameCollocRicercaForm currentForm = (EsameCollocRicercaForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);

			UserVO utente = Navigation.getInstance(request).getUtente();
			SIFListaBibliotechePoloVO richiesta = new SIFListaBibliotechePoloVO(
					utente.getCodPolo(), utente.getCodBib(), true, Integer
							.valueOf(ConstantDefault.ELEMENTI_BLOCCHI
									.getDefault()), LISTA_BIBLIOTECHE);

			return biblio.getSIFListaBibliotechePolo(richiesta);
		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("errors.errore." + e.getMessage()));
			return mapping.getInputForward();
		}
	}

	// /////////////////////////////////////////////////


	private List<SerieVO> getListaSerie(String codPolo, String codBib, String ticket)
			throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<SerieVO> serie = factory.getGestioneDocumentoFisico().getListaSerie(
				codPolo, codBib, ticket);
		if (!ValidazioneDati.isFilled(serie))
			return null;

		return serie;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		if (currentForm.isEstrattoreIdDoc()) {
			// filtri su possesso solo se seleziono una singola bib.
			if (ValidazioneDati.equals(idCheck, "COUNT_BIBLIOTECHE"))
				return (ValidazioneDati.size(currentForm.getEsporta().getListaBib()) == 1);
		}

		return true;
	}

	private void loadPagina(ActionForm form) throws Exception {
		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		currentForm.setDallaCollocazione("");
		currentForm.setAllaSpecificazione("");
		currentForm.setDallaSpecificazione("");
		currentForm.setAllaCollocazione("");

		currentForm.setSerie("");
		currentForm.setStartInventario("");
		currentForm.setEndInventario("");
		currentForm.setSezione("");
	}

	public ActionForward SIFbibliotecarioInserimento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		currentForm.setTipoCodBibliotec("INS");
		try {
			return mapping.findForward("SIFbibliotecario");
		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			return mapping.getInputForward();
		}
	}

	public ActionForward SIFbibliotecarioAggiornamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EstrattoreIdDocumentiForm currentForm = (EstrattoreIdDocumentiForm) form;
		currentForm.setTipoCodBibliotec("AGG");
		try {
			return mapping.findForward("SIFbibliotecario");
		} catch (Exception e) { // altri tipi di errore
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			return mapping.getInputForward();
		}
	}

}
