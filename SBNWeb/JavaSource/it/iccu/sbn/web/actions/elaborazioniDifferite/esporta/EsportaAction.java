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

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.batch.unimarc.ExportUnimarcBatch;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotechePoloVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoEstrazioneUnimarc;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta.EsportaForm;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta.EsportaForm.TipoProspettazioneExport;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.actions.gestionebibliografica.isbd.akros.EccezioneSbnDiagnostico;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.util.FileUtil;
import it.iccu.sbn.web.util.FormattazioneModificata;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class EsportaAction extends RicercaInventariCollocazioniAction implements SbnAttivitaChecker {

	private static final String LISTA_BIBLIOTECHE = "sif.bib.export.unimarc";

	private static Logger log = Logger.getLogger(EsportaAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();

		map.put("button.prenota", "esporta");// metodo per esporta
		map.put("tab.daticatalografici", "selezDatiCatalografici");// metodo per selezione tabs
		map.put("tab.posseduto", "selezPosseduto");// metodo per selezione tabs
		map.put("tab.classificazioni", "selezClassificazioni");// metodo per selezione tabs
		map.put("button.cercabiblioteche", "listaSupportoBib");// metodi per cerca (lente)
		map.put("button.caricafile", "caricaFile");// metodo per caricamento files
		map.put("button.tutteLeBiblio", "tutteLeBiblio");

		map.put("gestionestampe.lsBib", "cartiglioListaBib");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		EsportaForm currentForm = (EsportaForm) form;
		try{

			// Intervento del 15.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
			// Eliminata combo con tutte le biblio ed inserito cartiglio come in stampe di Documento Fisico
			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
				currentForm.setCodBib(bib.getCod_bib());
				currentForm.setDescrBib(bib.getNom_biblioteca());
			} else {
				request.setAttribute("codBib", currentForm.getCodBib());
			}


			super.unspecified(mapping, currentForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			// controllo se ho già i dati in sessione;
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			this.saveToken(request);
			// controllo se ho già i dati in sessione;
			UserVO utente = navi.getUtente();
			if(!currentForm.isSessione())	{
				currentForm.setTicket(navi.getUserTicket());
				currentForm.setCodBib(utente.getCodBib());
				currentForm.setCodPolo(utente.getCodPolo());
				currentForm.setDescrBib(utente.getBiblioteca());
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

				// Inizio Modifica almaviva2 febbraio 2010 per utilizzare oggetto
				// anche per stampa cataloghi.
				if (ValidazioneDati.equals(request.getParameter(Constants.CODICE_ATTIVITA),CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)) {
					currentForm.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI);
					currentForm.setTipoFormato(TipoStampa.PDF.name());
					navi.setTesto("Stampa cataloghi");
					currentForm.setCodBib(utente.getCodBib());
					currentForm.setDescrBib(utente.getBiblioteca());
					currentForm.setTitoloCollana(true);
					currentForm.setDatiCollocazione(true);
					currentForm.setIntestTitoloAdAutore(true);
				} else
					currentForm.setCodAttivita(CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040);

				// Fine Modifica almaviva2 febbraio 2010 per utilizzare oggetto
				// anche per stampa cataloghi.

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

				// almaviva5_20100407 se ho selezionato una sola biblioteca carico di dati
				// di posseduto
				if (currentForm.isUnimarc() ) {
					if (ValidazioneDati.size(sifBiblio) == 1 ) {
						BibliotecaVO bib = ValidazioneDati.first(sifBiblio);
						//almaviva5_20131024 segnalazione RMR
						currentForm.setCodBib(bib.getCod_bib());
						currentForm.setDescrBib(bib.getNom_biblioteca());
						UserVO utenteCollegato = navi.getUtente();

						CaricamentoCombo carCombo = new CaricamentoCombo();
						if (currentForm.getCodBib() != null && !currentForm.getCodBib().equals(bib.getCod_bib())){
							List<SerieVO> listaSerie = getListaSerie(bib.getCod_polo(), bib.getCod_bib(), utenteCollegato.getTicket());
							if (listaSerie != null && listaSerie.size() > 0){
								currentForm.setListaSerie(listaSerie);
								currentForm.setListaComboSerie(carCombo.loadCodice(listaSerie));
								currentForm.setListaPossedutoSerie(carCombo.loadCodice(getListaSerie(bib.getCod_polo(),
										bib.getCod_bib(), utenteCollegato.getTicket())));
							}else{
								currentForm.setDisableSerie(true);//non attivo
								currentForm.setDisableDalNum(true);//non attivo
								currentForm.setDisableAlNum(true);//non attivo
								throw new ValidationException("LaBibliotecaSceltaNonHaAlcunaSerieDefinita");
							}
						}else{
							currentForm.setListaPossedutoSerie(carCombo.loadCodice(getListaSerie(bib.getCod_polo(),
									bib.getCod_bib(), utenteCollegato.getTicket())));
						}

						esporta.setCodPoloSerie(bib.getCod_polo());
						esporta.setCodBibSerie(bib.getCod_bib());

					} else
						esporta.clearFiltriPosseduto();
				}

			}
			if (currentForm.getCodAttivita() != null && !(currentForm.getCodAttivita().equals(CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040))){
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
			// Viene settato il token per le transazioni successive
			this.saveToken(request);

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


	public ActionForward selezDatiCatalografici(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EsportaForm currentForm = (EsportaForm) form;
		currentForm.setTipoProspettazione(TipoProspettazioneExport.DATI_CATALOGRAFICI);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		setDefaultValues(currentForm, TipoProspettazioneExport.DATI_CATALOGRAFICI, utenteCollegato);

		log.debug("TipoProspettazione di EsportaForm: "	+ currentForm.getTipoProspettazione());

		return mapping.getInputForward();
	}

	public ActionForward selezPosseduto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsportaForm currentForm = (EsportaForm) form;
		currentForm.setTipoProspettazione(TipoProspettazioneExport.POSSEDUTO);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		setDefaultValues(currentForm, TipoProspettazioneExport.POSSEDUTO, utenteCollegato);

		log.debug("TipoProspettazione di EsportaForm: "	+ currentForm.getTipoProspettazione());

		if (ValidazioneDati.equals(currentForm.getCodAttivita(), CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040)
			&& ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.esporta.selezionare.bib"));
			this.saveErrors(request, errors);
		}
		currentForm.getEsporta().setTipoEstrazione(TipoEstrazioneUnimarc.COLLOCAZIONI);
		super.unspecified(mapping, currentForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

		return mapping.getInputForward();
	}

	public ActionForward tutteLeBiblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EsportaForm currentForm = (EsportaForm) form;

		currentForm.getEsporta().setTipoEstrazione(TipoEstrazioneUnimarc.COLLOCAZIONI);
		super.unspecified(mapping, currentForm, request, response);
		this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
		loadPagina(currentForm);
		if (currentForm.getElencoBiblio() != null
				&& !currentForm.getElencoBiblio().equals("")) {
			currentForm.setElencoBiblio("");
			currentForm.setTastoCancBib(false);// non attivo
			currentForm.setDisableSez(true);// attivo
			currentForm.setDisableSerie(true);// attivo
			currentForm.setDisableDalNum(true);// attivo
			currentForm.setDisableAlNum(true);// attivo
			currentForm.setElencoBiblio(null);
			currentForm.getEsporta().setListaBib(null);
		} else {
			currentForm.setTastoCancBib(true);// attivo
			currentForm.setDisableSez(false);// non attivo
			currentForm.setDisableInv(false);// non attivo
			currentForm.setDisableSerie(false);// attivo
			currentForm.setDisableDalNum(false);// attivo
			currentForm.setDisableAlNum(false);// attivo
		}
		// currentForm.setTastoCancBib(false);
		return mapping.getInputForward();
	}

	public ActionForward selezClassificazioni(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		EsportaForm currentForm = (EsportaForm) form;
		currentForm.setTipoProspettazione(TipoProspettazioneExport.CLASSI);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		setDefaultValues(currentForm, TipoProspettazioneExport.CLASSI, utenteCollegato);

		log.debug("TipoProspettazione di EsportaForm: "	+ currentForm.getTipoProspettazione());

		return mapping.getInputForward();
		// return super.execute(mapping, form, request, response);
	}


	public ActionForward esporta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsportaForm currentForm = (EsportaForm) form;
		String idBatch = null;
		ActionMessages errors = new ActionMessages();

		try {
			resetToken(request);

			// Prova export
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// Setting biblioteca corrente (6 caratteri)
			// codice polo + codice biblioteca
			Navigation navi = Navigation.getInstance(request);
			UserVO utente = navi.getUtente();
			EsportaVO esportaVO = currentForm.getEsporta().copy();
			esportaVO.setCodPolo(utente.getCodPolo());
			esportaVO.setCodBib(utente.getCodBib());
			esportaVO.setUser(utente.getUserId());

			esportaVO.setCodAttivita(CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040);

			if (currentForm.isUnimarc() ) {
				esportaVO.validate();
				//almaviva5_20130226 imposta etichette da esportate
				ExportUnimarcBatch.impostaEtichetteDaEsportare(esportaVO);
			}

			// se ho selezionato tutti i materiali é più efficente
			// eliminare il filtro
			String[] materiali = esportaVO.getMateriali();
			if (ValidazioneDati.size(materiali) == EsportaVO.MATERIALI.length)
				esportaVO.setMateriali(null);
			else
				// le collane hanno il tipo materiale a spazio, devo includerlo nel
				// filtro
				if (ValidazioneDati.isFilled(materiali)) {
					if (Arrays.asList(esportaVO.getNature()).contains("C")) {
						List<String> tmp = new ArrayList<String>(Arrays.asList(materiali));
						tmp.add(" "); // aggiungo mat. fittizio
						esportaVO.setMateriali(tmp.toArray(materiali));
					}
				}

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			esportaVO.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			esportaVO.setDownloadPath(downloadPath);
			esportaVO.setDownloadLinkPath("/"); // eliminato

			esportaVO.setTicket(utente.getTicket());

			// Inizio Modifica almaviva2 febbraio 2010 per utilizzare oggetto anche per stampa cataloghi e classi.
			if (currentForm.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)) {

				// Intervento del 25.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
				// Modifiche/controlli per attivazione stampa catalogo per Soggetti e Classi
				if (esportaVO.getTipoCatalogo().equals("SOG") && esportaVO.getCodSoggettario().equals("")) {
					errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,
							"La stampa catalogo per Soggetto può essere richiesta per un solo Soggettario - impostarlo con l'apposita selezione"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				if (esportaVO.getCodSoggettario().length() > 0 && !esportaVO.getTipoCatalogo().equals("SOG")) {
					esportaVO.setCodSoggettario("");
				}

				if (esportaVO.getTipoCatalogo().equals("CLA") && esportaVO.getCodSistemaClassificazione().equals("")) {
					errors = new ActionMessages();
					errors.add("Attenzione", new ActionMessage("errors.gestioneBibliografica.testoProtocollo" ,
							"La stampa catalogo per Classe può essere richiesta per un solo sistema di Classificazione - impostarlo con l'apposita selezione"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				if (esportaVO.getCodSistemaClassificazione().length() > 0 && !esportaVO.getTipoCatalogo().equals("CLA")) {
					esportaVO.setCodSistemaClassificazione("");
				}


				esportaVO.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI);

				AreaParametriStampaSchedeVo areaParametriStampaSchedeVo = new AreaParametriStampaSchedeVo(
						FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				//almaviva5_20121001 #5127
				areaParametriStampaSchedeVo.setDescrizioneBiblioteca(utente.getBiblioteca());

				if (esportaVO.getListaBID() != null) {
					areaParametriStampaSchedeVo.setListaBid(esportaVO.getListaBID());
				} else {
					// inizio impostazione dei dati relativi ai campi descrizione titolo
					String stringaLike = "";
					String nome = "";
					// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
					// gli estremi di filtro non sono più solo per titolo ma la tabella su cui applicare dipende dal catalogo selezionato
					if (esportaVO.getCatalogoSelezDa().equals("")) {
						esportaVO.setCatalogoSelezA("");
					} else {

						if (!isFilled(esportaVO.getCatalogoSelezA())) {
							esportaVO.setCatalogoSelezA(esportaVO.getCatalogoSelezDa() + "z");
						} else {
//							if (esportaVO.getCatalogoSelezDa().equals(esportaVO.getCatalogoSelezA())) {
								esportaVO.setCatalogoSelezA(esportaVO.getCatalogoSelezA() + "z");
//							}
						}

						if (!esportaVO.getTipoCatalogo().equals("CLA")) {
							nome = FormattazioneModificata.rimuoviCaratteriSpec(esportaVO.getCatalogoSelezDa());
							nome = FormattazioneModificata.rimuoviPuntegg(nome);
							nome = nome.trim().toUpperCase();
							esportaVO.setCatalogoSelezDa(nome);

							nome = FormattazioneModificata.rimuoviCaratteriSpec(esportaVO.getCatalogoSelezA());
							nome = FormattazioneModificata.rimuoviPuntegg(nome);
							nome = nome.trim().toUpperCase();
							esportaVO.setCatalogoSelezA(nome);
						}

						// restituisce titolo senza articoli dopo asterisco
						// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
						// questi controlli vanno fatti solo se il catalogo selezionato è TIT
						if (esportaVO.getTipoCatalogo().equals("TIT")) {
							stringaLike = FormattazioneModificata.dopoAsterisco(esportaVO.getCatalogoSelezDa());
							try {
								nome = FormattazioneModificata.formatta(stringaLike);
							} catch (EccezioneSbnDiagnostico e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
							// Manutenzione BUG Mantis (Collaudo) 5167 - 05-11-2012
							// Viene eliminato il controllo su lunghezza non inferiore a 3 nel filtro per estremi del titolo
							// perchè tale limite è valido solo nel caso che la ricerca venga effettuata con il Protocollo
							// mentre qui si usano le select su tabelle DB
//								if (nome.length() < 3) {
//									errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.prenotazione.failed"));
//									this.saveErrors(request, errors);
//									return mapping.getInputForward();
//								}
							if (nome.length() > 50) {
								nome = nome.substring(0, 50);
							}
							esportaVO.setDescTitoloDa(nome);
							esportaVO.setCatalogoSelezDa(nome);


							stringaLike = FormattazioneModificata.dopoAsterisco(esportaVO.getCatalogoSelezA());
							try {
								nome = FormattazioneModificata.formatta(stringaLike);
							} catch (EccezioneSbnDiagnostico e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));
								this.saveErrors(request, errors);
								return mapping.getInputForward();
							}
							if (nome.length() > 50) {
								nome = nome.substring(0, 50);
							}
							esportaVO.setDescTitoloA(nome);
							esportaVO.setCatalogoSelezA(nome);
						}
					}
				}

				if (currentForm.getTipoProspettazione() == TipoProspettazioneExport.POSSEDUTO) {
					//esporta per collocazioni
					if (esportaVO.getTipoEstrazione() == TipoEstrazioneUnimarc.COLLOCAZIONI) {
						if (currentForm.getCodPoloSez() == null && currentForm.getCodBibSez() == null){
							errors.add("generico", new ActionMessage("error.documentofisico.premereTastoSezione"));
							this.saveErrors(request, errors);
							request.setAttribute("currentForm", currentForm);
							super.unspecified(mapping, currentForm, request, response);
							this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));
							return mapping.getInputForward();
						}
						super.validaInputCollocazioni(mapping, request, currentForm);
						areaParametriStampaSchedeVo.setTipoOperazione("S");

						esportaVO.setCodPoloSez(utente.getCodPolo());
						esportaVO.setCodBibSez(utente.getCodBib());
						esportaVO.setPossCodSez(currentForm.getSezione());
						esportaVO.setPossDallaCollocazione(currentForm.getDallaCollocazione());
						esportaVO.setPossAllaCollocazione(currentForm.getAllaCollocazione());
						esportaVO.setPossSpecificazioneCollDa(currentForm.getDallaSpecificazione());
						esportaVO.setPossSpecificazioneCollA(currentForm.getAllaSpecificazione());
						esportaVO.setTipoCollocazione(currentForm.getTipoColloc());



						areaParametriStampaSchedeVo.setCodPoloSez(utente.getCodPolo());
						areaParametriStampaSchedeVo.setCodBibSez(utente.getCodBib());
						areaParametriStampaSchedeVo.setSezione(currentForm.getSezione());
						// almaviva2 16.02.2011:Intervento interno su mail almaviva: correzione impostazione campi perchè effettuata in maniera errata;
						areaParametriStampaSchedeVo.setDallaCollocazione(currentForm.getDallaCollocazione());
						areaParametriStampaSchedeVo.setAllaCollocazione(currentForm.getAllaCollocazione());
						areaParametriStampaSchedeVo.setDallaSpecificazione(currentForm.getDallaSpecificazione());
						areaParametriStampaSchedeVo.setAllaSpecificazione(currentForm.getAllaSpecificazione());
						esportaVO.setTipoCollocazione(currentForm.getTipoColloc());
					} else if (esportaVO.getTipoEstrazione() == TipoEstrazioneUnimarc.SERIE_INVENTARIALE) {
						//esporta per range di inventari
						super.validaInputRangeInventari(mapping, request, currentForm);
						areaParametriStampaSchedeVo.setTipoOperazione("R");
						areaParametriStampaSchedeVo.setSerie(currentForm.getSerie());
						areaParametriStampaSchedeVo.setEndInventario(currentForm.getEndInventario());
						areaParametriStampaSchedeVo.setStartInventario(currentForm.getStartInventario());
					} else {
						errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneBibliografica.testoProtocollo",
						"Selezionare il tipo di filtri da utilizzare (Collocazione o Serie inventariale)"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();

					}

				} else if (currentForm.getTipoProspettazione().toString().equals("CLASSIFICAZIONI")) {
					errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneBibliografica.testoProtocollo",
					"La funzione richiesta attualmente non è disponibile; utilizzare i filtri per DATI CATALOGRAFICI"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				areaParametriStampaSchedeVo.setEsportaVO(esportaVO);

				// Impostazione parametri di Stampa
				areaParametriStampaSchedeVo.setCodErr("0000");
				areaParametriStampaSchedeVo.setTicket(utente.getTicket());
				areaParametriStampaSchedeVo.setCodPolo(utente.getCodPolo());
				areaParametriStampaSchedeVo.setCodBib(utente.getCodBib());

				areaParametriStampaSchedeVo.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI);

				if (currentForm.isIntestTitoloAdAutore()) {
					areaParametriStampaSchedeVo.setIntestTitoloAdAutore("SI");
				} else {
					areaParametriStampaSchedeVo.setIntestTitoloAdAutore("NO");
				}
				if (currentForm.isTitoloCollana()) {
					areaParametriStampaSchedeVo.setTitoloCollana("SI");
				} else {
					areaParametriStampaSchedeVo.setTitoloCollana("NO");
				}
				if (currentForm.isTitoliAnalitici()) {
					areaParametriStampaSchedeVo.setTitoliAnalitici("SI");
				} else {
					areaParametriStampaSchedeVo.setTitoliAnalitici("NO");
				}
				if (currentForm.isDatiCollocazione()) {
					areaParametriStampaSchedeVo.setDatiCollocazione("SI");
				} else {
					areaParametriStampaSchedeVo.setDatiCollocazione("NO");
				}

				// Intervento del 19.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
				// Valorizzazione dei campi biblio con il valore restituito dal sif delle biblio (inserito cartiglio come in stampe di Documento Fisico)
				// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
				esportaVO.setCodBib(currentForm.getCodBib());
				areaParametriStampaSchedeVo.setCodBib(currentForm.getCodBib());

				if (esportaVO.getTipoCatalogo().equals("TIT")) {
					areaParametriStampaSchedeVo.setRichListaPerTitolo(true);
				} else if (esportaVO.getTipoCatalogo().equals("AUT")) {
					areaParametriStampaSchedeVo.setRichListaPerAutore(true);
				} else if (esportaVO.getTipoCatalogo().equals("TIP")) {
					areaParametriStampaSchedeVo.setRichListaPerTipografo(true);
				} else if (esportaVO.getTipoCatalogo().equals("POS")) {
					areaParametriStampaSchedeVo.setRichListaPerPossessore(true);
				} else if (esportaVO.getTipoCatalogo().equals("SOG")) {
					areaParametriStampaSchedeVo.setRichListaPerSoggetto(true);
				} else if (esportaVO.getTipoCatalogo().equals("CLA")) {
					areaParametriStampaSchedeVo.setRichListaPerClassificazione(true);
				}else if (esportaVO.getTipoCatalogo().equals("EDI")) {
					areaParametriStampaSchedeVo.setRichListaPerEditore(true);
				}

				// user
				UserVO user = utente;
				areaParametriStampaSchedeVo.setUser(user.getUserId());

				// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
				basePath = this.servlet.getServletContext().getRealPath(File.separator);
				String pathJrxml = basePath + File.separator + "jrxml" + File.separator + "default_catalografico.jrxml";

				// ho finito di preparare il VO, ora lo metto nell'arraylist che
				// passerò alla coda.
				List parametri = new ArrayList();
				parametri.add(areaParametriStampaSchedeVo);
				request.setAttribute("DatiVo", parametri);

				String pathDownload = StampeUtil.getBatchFilesPath();

				// codice standard inserimento messaggio di richiesta stampa
				// differita
				StampaDiffVO stam = new StampaDiffVO();

				UserVO ute = utente;
				stam.setCodPolo(ute.getCodPolo());
				stam.setCodBib(ute.getCodBib());
				stam.setUser(ute.getUserId());
				stam.setCodAttivita(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI);

				stam.setTipoStampa(currentForm.getTipoFormato());
				stam.setTipoOrdinamento("");
				stam.setParametri(parametri);
				stam.setTemplate(pathJrxml);
				stam.setDownload(pathDownload);
				stam.setDownloadLinkPath("/");
				stam.setTipoOperazione("STAMPA_CATALOGHI");
				stam.setTicket(navi.getUserTicket());
				UtilityCastor util = new UtilityCastor();
				String dataCorr = util.getCurrentDate();
				stam.setData(dataCorr);

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(ute.getTicket(), stam, null);
			} else {
				//esporta
				TipoEstrazioneUnimarc type = getTipoEstrazione(mapping, form, request, response);
				if (type == null)
					return mapping.getInputForward();
				esportaVO.setTipoEstrazione(type);
				if (!ValidazioneDati.in(type,
								TipoEstrazioneUnimarc.ARCHIVIO,
								TipoEstrazioneUnimarc.FILE)) {
					if (ValidazioneDati.equals(currentForm.getCodAttivita(), CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040)
							&& ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1) {
						errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.esporta.selezionare.bib"));
						this.saveErrors(request, errors);
						request.setAttribute("currentForm", currentForm);
						return mapping.getInputForward();
					}
					//esporta per collocazioni
					if (type == TipoEstrazioneUnimarc.COLLOCAZIONI) {
						if (currentForm.getCodPoloSez() == null && currentForm.getCodBibSez() == null){
							errors.add("generico", new ActionMessage("error.documentofisico.premereTastoSezione"));
							this.saveErrors(request, errors);
							request.setAttribute("currentForm", currentForm);
							return mapping.getInputForward();
						}
						currentForm.setFolder("Collocazione");

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
						if (ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1){
							throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);
						}
						currentForm.setFolder(null);
					}else{
						super.validaInputRangeInventari(mapping, request, currentForm);
						currentForm.setTipoOperazione("R");
						esportaVO.setPossSerie(currentForm.getSerie());
						esportaVO.setPossDalNumero(currentForm.getStartInventario());
						esportaVO.setPossAlNumero(currentForm.getEndInventario());
						currentForm.setFolder(null);
						if (ValidazioneDati.size(currentForm.getEsporta().getListaBib()) != 1){
							throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);
						}
					}

				}
				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(), esportaVO, null);
			}

		} catch (ValidationException e) {
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.ERROR_GENERIC) {
				LinkableTagUtils.addError(request, e);
				return mapping.getInputForward();
			}

			if (currentForm.getTipoProspettazione() == TipoProspettazioneExport.POSSEDUTO){
				errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
				this.saveErrors(request, errors);
				request.setAttribute("currentForm", currentForm);
				return mapping.getInputForward();
			}
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico." + e.getMsg()));

			return mapping.getInputForward();
		}

		if (idBatch != null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.prenotazione.ok", idBatch));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.prenotazione.failed"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}


	private TipoEstrazioneUnimarc getTipoEstrazione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		TipoEstrazioneUnimarc type = TipoEstrazioneUnimarc.ARCHIVIO;
		EsportaForm currentForm = (EsportaForm) form;
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

	public void setDefaultValues(EsportaForm currentForm,
			TipoProspettazioneExport tipoProspettazione, UserVO utenteCollegato)
			throws Exception {

		if (currentForm.isInitialized())
			return;

		CaricamentoCombo carCombo = new CaricamentoCombo();
		EsportaVO esportaVO = currentForm.getEsporta();
		List<?> listaCodiciAtenei = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_ATENEI));

		switch (tipoProspettazione) {
		case DATI_CATALOGRAFICI:
			// tab dati catalografici --- default (if null)
			esportaVO.setTipoEstrazione(TipoEstrazioneUnimarc.ARCHIVIO);
			esportaVO.setTuttoArchivio(true);
			currentForm.setFolder(null);

			currentForm.setListaCodScarico(CaricamentoCombo.cutFirst(CodiciProvider.getCodici(CodiciType.CODICE_TIPO_ESTRAZIONE_UNIMARC)));
			currentForm.setListaTipoData(carCombo.loadComboCodiciDesc(CodiciProvider
					.getCodici(CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE)));

			List<?> listaCodiciLingue = carCombo.loadComboCodiciDesc(CodiciProvider
					.getCodici(CodiciType.CODICE_LINGUA));
			currentForm.setListaLingue(listaCodiciLingue);

			List<?> listaCodiciPaese = carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(CodiciType.CODICE_PAESE));
			currentForm.setListaPaese(listaCodiciPaese);

			List<?> listaCodiciTipoRec = carCombo.loadComboCodiciDesc(CodiciProvider
					.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC));
			currentForm.setListaTipoRecord(listaCodiciTipoRec);

			currentForm.setListaBiblioAteneo(listaCodiciAtenei);

			esportaVO.setMateriali(EsportaVO.MATERIALI);
			esportaVO.setNature(EsportaVO.NATURE);
			esportaVO.setDescTitoloDa("");
			esportaVO.setDescTitoloA("");
			esportaVO.setCatalogoSelezDa("");
			esportaVO.setCatalogoSelezA("");

			currentForm.setListaTipoCatalogo(getValuesComboTipoCatalogo());
			// Intervento del 25.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
			// Modifiche per attivazione stampa catalogo per Soggetti e Classi
			caricaSoggettariClassificazioni(currentForm, utenteCollegato.getTicket());
			break;

		case POSSEDUTO:
			// tab posseduto
			esportaVO.setTipoEstrazione(TipoEstrazioneUnimarc.COLLOCAZIONI);
			currentForm.setListaPossedutoSerie(carCombo.loadCodice(getListaSerie(utenteCollegato.getCodPolo(),
							utenteCollegato.getCodBib(), utenteCollegato.getTicket())));
			break;

		case BIBLIOTECHE:
			// tab biblioteche
			currentForm.setListaBiblioAteneo(listaCodiciAtenei);
			break;

		case CLASSI:
			// tab classificazioni
			currentForm.setListaClassSistema(carCombo.loadComboCodiciDesc(CodiciProvider
							.getCodici(CodiciType.CODICE_SISTEMA_CLASSE)));
			break;
		}
	}

	public ActionForward caricaFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsportaForm currentForm = (EsportaForm) form;
		EsportaVO esportaVO = currentForm.getEsporta();

		FormFile file = currentForm.getFileIdList();
		if (file == null || file.getFileSize() == 0)
			return mapping.getInputForward();

		try {
			switch (esportaVO.getTipoInput()) {
			case BID:
				return caricaFileBid(mapping, form, request);
			case INV:
				return caricaFileInventari(mapping, form, request);
			}

			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));
			return mapping.getInputForward();
		}

	}


	private ActionForward caricaFileInventari(ActionMapping mapping, ActionForm form,
			HttpServletRequest request) throws Exception {

		EsportaForm currentForm = (EsportaForm) form;
		EsportaVO esporta = currentForm.getEsporta();
		if (ValidazioneDati.size(esporta.getListaBib()) != 1)
			throw new ValidationException(SbnErrorTypes.UNI_TROPPE_BIBLIOTECHE_PER_RANGE);

		FormFile file = currentForm.getFileIdList();

		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

		String codBib = ValidazioneDati.first(esporta.getListaBib()).getCod_bib();
		int inserted = 0;
		int dropped = 0;
		String id = null;
		while ((id = reader.readLine()) != null) {
			try {
				InventarioVO inv = InventarioRFIDParser.parse(id);
				//check biblioteca selezionata
				String bibInv = inv.getCodBib();
				if (ValidazioneDati.isFilled(bibInv) && !bibInv.equals(codBib)) {
					dropped++;
					continue;
				}

				inserted++;

			} catch (ValidationException e) {
				dropped++;
				log.error("identificativo rfid errato: " + id);
			}
		}

		reader.close();

		if (inserted == 0) { // nessun inv
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));
			return mapping.getInputForward();
		}

		String output = FileUtil.getTemporaryFileName();
		FileUtil.uploadFile(file, output, null);
		esporta.setInputFile(output);

		LinkableTagUtils.addError(request,
				new ActionMessage("errors.esporta.caricaFile", file.getFileName(), inserted, dropped));

		if (!currentForm.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI))
			esporta.setTipoEstrazione(TipoEstrazioneUnimarc.FILE);

		return mapping.getInputForward();
	}


	private ActionForward caricaFileBid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request) throws Exception {
		EsportaForm currentForm = (EsportaForm) form;
		EsportaVO esportaVO = currentForm.getEsporta();

		FormFile file = currentForm.getFileIdList();
		List<String> listaId = new ArrayList<String>(file.getFileSize() / 10); // bid=10

		byte[] buf = file.getFileData();

		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));

		int inserted = 0;
		int dropped = 0;
		String bid = null;
		while ((bid = reader.readLine()) != null) {
			if (!ValidazioneDati.leggiXID(bid)) {
				dropped++;
				continue;
			} else {
				inserted++;
				listaId.add(bid);
			}
		}

		reader.close();

		if (!ValidazioneDati.isFilled(listaId)) { // nessun bid
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico.contenutoFileNonValido"));
			return mapping.getInputForward();
		}

		esportaVO.setListaBID(listaId);

		LinkableTagUtils.addError(request,
				new ActionMessage("errors.esporta.caricaFile", file.getFileName(), inserted, dropped));

		if (!currentForm.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI))
			esportaVO.setTipoEstrazione(TipoEstrazioneUnimarc.FILE);

		return mapping.getInputForward();
	}


	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// EsameCollocRicercaForm currentForm = (EsameCollocRicercaForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);

			UserVO utente = Navigation.getInstance(request).getUtente();
			SIFListaBibliotechePoloVO richiesta = new SIFListaBibliotechePoloVO(utente.getCodPolo(), utente.getCodBib(), true, Integer
							.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault()), LISTA_BIBLIOTECHE);

			return biblio.getSIFListaBibliotechePolo(richiesta);
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.errore." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}


//	public ActionForward validaInputRangeInventari(ActionMapping mapping,
//			HttpServletRequest request, ActionForm form) throws Exception {
//
//		EsportaForm currentForm = (EsportaForm) form;
//		request.setAttribute("currentForm", form);
//
//		// try{
//		// validazione startInv e endInv
//		if (currentForm.getEsporta().getPossSerie() != null) {
//			if (currentForm.getEsporta().getPossSerie().length() > 3) {
//				throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codSerieEccedente");
//			}
//		} else {
//			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "ERRORE: codSerie = null");
//		}
//		if (!ValidazioneDati.strIsNull(currentForm.getEsporta()
//				.getPossDalNumero())) {
//			if (ValidazioneDati.strIsNumeric(currentForm.getEsporta()
//					.getPossDalNumero())) {
//				if ((currentForm.getEsporta().getPossDalNumero()).length() > 9) {
//					throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventDaEccedente");
//				}
//			} else {
//				throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventDaDeveEssereNumerico");
//			}
//		} else {
//			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "errInvObbl");
//		}
//		if (!ValidazioneDati.strIsNull(currentForm.getEsporta()
//				.getPossAlNumero())) {
//			if (ValidazioneDati.strIsNumeric(currentForm.getEsporta()
//					.getPossAlNumero())) {
//				if ((currentForm.getEsporta().getPossAlNumero()).length() > 9) {
//					throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventAEccedente");
//				}
//			} else {
//				throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "codInventADeveEssereNumerico");
//			}
//		} else {
//			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "errInvObbl");
//		}
//		if (currentForm.getEsporta().getPossAlNumero() != null
//				&& currentForm.getEsporta().getPossDalNumero() != null) {
//			if (Integer.valueOf(currentForm.getEsporta().getPossAlNumero()) < Integer
//					.valueOf(currentForm.getEsporta().getPossDalNumero())) {
//				throw new ValidationException(SbnErrorTypes.GDF_GENERIC,
//						"codInventDaDeveEssereMinoreDiCodInventA");
//			}
//		} else {
//			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "ERRORE: codInvent = null");
//		}
//		if (!ValidazioneDati.strIsNull(currentForm.getEsporta()
//				.getPossDalNumero())
//				&& !currentForm.getEsporta().getPossDalNumero().trim().equals("0")
//				&& ValidazioneDati.strIsNumeric(currentForm.getEsporta().getPossDalNumero())
//				&& Integer.valueOf(currentForm.getEsporta().getPossDalNumero()) >= 0) {
//		} else {
//			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "indicareIDueEstremiDellIntervallo");
//		}
//		return mapping.getInputForward();
//	}
//
//	public ActionForward validaInputCollocazioni(ActionMapping mapping,
//			HttpServletRequest request, ActionForm form) throws Exception {
//		EsportaForm currentForm = (EsportaForm) form;
//		request.setAttribute("currentForm", form);
//		CodiciNormalizzatiVO collSpec = null;
//		EsportaVO esporta = currentForm.getEsporta();
//		if (esporta.getCodBibSez() != null) {
//			collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(
//					esporta.getPossTipoSezione(), esporta.getPossDallaCollocazione(),
//					esporta.getPossAllaCollocazione(), false,
//					esporta.getPossSpecificazioneCollDa(),
//					esporta.getPossSpecificazioneCollA(),
//					false, "");
//		} else {
//			throw new ValidationException(SbnErrorTypes.GDF_GENERIC, "premereTastoSezionePartenza");
//		}
//		if ((collSpec.getDaColl() + collSpec.getDaSpec()).compareTo((collSpec
//				.getAColl() + collSpec.getASpec())) <= 0) {
//			// la prima minore uguale della seconda
//		} else {
//			throw new ValidationException(SbnErrorTypes.GDF_GENERIC,
//					"dallaCollocazioneDeveEssereMinoreOUgualeAllaCollocazione");
//		}
//
//		return mapping.getInputForward();
//	}

	private List getListaSerie(String codPolo, String codBib, String ticket)
			throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List serie = factory.getGestioneDocumentoFisico().getListaSerie(codPolo, codBib, ticket);
		if (!ValidazioneDati.isFilled(serie))
			return null;

		return serie;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		EsportaForm currentForm = (EsportaForm) form;
		if (currentForm.isUnimarc()) {
			// filtri su possesso solo se seleziono una singola bib.
			if (ValidazioneDati.equals(idCheck, "COUNT_BIBLIOTECHE"))
				return (ValidazioneDati.size(currentForm.getEsporta().getListaBib()) == 1);
		}

		return true;
	}

	private void loadPagina(ActionForm form) throws Exception {
		EsportaForm currentForm = (EsportaForm) form;
		currentForm.setDallaCollocazione("");
		currentForm.setAllaSpecificazione("");
		currentForm.setDallaSpecificazione("");
		currentForm.setAllaCollocazione("");

		currentForm.setSerie("");
		currentForm.setStartInventario("");
		currentForm.setEndInventario("");
		currentForm.setSezione("");
	}

	private List<ComboCodDescVO> getValuesComboTipoCatalogo() {
		it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO comboDescVO = new it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO();
		List<ComboCodDescVO> testCombo = new ArrayList<ComboCodDescVO>();
		comboDescVO.setCodice("AUT");
		comboDescVO.setDescrizione("Autore");
		testCombo.add(comboDescVO);

		// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
		comboDescVO = new it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO();
		comboDescVO.setCodice("TIP");
		comboDescVO.setDescrizione("Editore/Tipografo");
		testCombo.add(comboDescVO);

		comboDescVO = new it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO();
		comboDescVO.setCodice("TIT");
		comboDescVO.setDescrizione("Titolo");
		testCombo.add(comboDescVO);
		comboDescVO = new it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO();
		comboDescVO.setCodice("POS");
		comboDescVO.setDescrizione("Possessori/Provenienze");
		testCombo.add(comboDescVO);
		comboDescVO = new it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO();
		comboDescVO.setCodice("SOG");
		comboDescVO.setDescrizione("Soggetti");
		testCombo.add(comboDescVO);
		comboDescVO = new it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO();
		comboDescVO.setCodice("CLA");
		comboDescVO.setDescrizione("Classi");
		testCombo.add(comboDescVO);

		// Intervento del 15.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
		// Eliminata combo con tutte le biblio ed inserito cartiglio come in stampe di Documento Fisico
		comboDescVO = new it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO();
		comboDescVO.setCodice("EDI");
		comboDescVO.setDescrizione("Editori (Produzione Editoriale)");
		testCombo.add(comboDescVO);

		return testCombo;
	}

	private void caricaSoggettariClassificazioni(ActionForm form, String ticket) throws Exception {
		EsportaForm currentForm = (EsportaForm) form;
		List<ComboCodDescVO> listaSoggettari = CaricamentoComboSemantica.loadComboSoggettario(ticket, true);
		if (ValidazioneDati.size(listaSoggettari) == 2)	// solo un soggettario
			listaSoggettari = CaricamentoCombo.cutFirst(listaSoggettari);
		currentForm.setListaSoggettari(listaSoggettari);

		List<ComboCodDescVO> listaClassi = CaricamentoComboSemantica.loadComboSistemaClassificazione(ticket, true);
		listaClassi.add(new ComboCodDescVO());
		currentForm.setListaSistemiClassificazione(listaClassi);
		currentForm.getEsporta().setCodSistemaClassificazione("");
	}




	public ActionForward cartiglioListaBib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsportaForm currentForm = (EsportaForm) form;
		try {

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
				SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(
						Navigation.getInstance(request).getUtente().getCodPolo(),
						Navigation.getInstance(request).getUtente().getCodBib(),
						CodiciAttivita.getIstance().GA_STAMPA_REGISTRO_DI_INGRESSO,
						currentForm.getNRec(),
						"codBibDaLista");
				return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}



}
