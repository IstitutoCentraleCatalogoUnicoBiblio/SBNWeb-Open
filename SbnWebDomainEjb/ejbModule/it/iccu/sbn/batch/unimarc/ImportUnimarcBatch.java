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
package it.iccu.sbn.batch.unimarc;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBStampeFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.domain.documentofisico.CollocazioneBMT;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMT;
import it.iccu.sbn.ejb.domain.documentofisico.InventarioBMT;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.importa.StampaReportImport;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa.ImportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa.ListaDati950VO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiImportSuPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoDatiImport1ParzialeVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.InterrogazioneAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.custom.esporta.QueryData;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class ImportUnimarcBatch extends DaoManager implements BatchExecutor {

	enum StatoRecord {
		DA_INSERIRE('I'),
		DA_CATTURARE('C'),
		PRESENTE('P');

		private final char stato;

		private StatoRecord(char stato) {
			this.stato = stato;
		}

		public char getStato() {
			return stato;
		}

		static StatoRecord of(char value) {
			for (StatoRecord sr : values())
				if (sr.stato == value)
					return sr;
			return null;
		}
	}

	enum Stato950 {
		OK,
		ERRORE,
		PARZIALE,
		DUPLICATO,
		NON_TROVATO;
	}

	private Interrogazione interrogazione;
	private DocumentoFisicoBMT documentoFisico;
	private InventarioBMT inventario;
	private CollocazioneBMT collocazione;
	private AmministrazioneBiblioteca biblioteca;

	private Session session;
	private String ticket;

	private String codPolo;


	private Logger _log;
	private AtomicBoolean status_ok = new AtomicBoolean(false);
	private TitoloDAO titDAO;
	private UserTransaction tx;

	StampaReportImport getEjb() throws Exception {
		return DomainEJBStampeFactory.getInstance().getStampaReportImport();
	}

	private Interrogazione getInterrogazione() throws Exception {
		if (interrogazione != null)
			return interrogazione;
		this.interrogazione = DomainEJBFactory.getInstance().getInterrogazione();
		return interrogazione;
	}

	private DocumentoFisicoBMT getDocumentoFisico() throws Exception {
		if (documentoFisico != null)
			return documentoFisico;
		this.documentoFisico = DomainEJBFactory.getInstance().getDocumentoFisicoBMT();
		return documentoFisico;
	}

	private InventarioBMT getInventario() throws Exception {
		if (inventario != null)
			return inventario;
		this.inventario = DomainEJBFactory.getInstance().getInventarioBMT();
		return inventario;
	}

	private CollocazioneBMT getCollocazione() throws Exception {
		if (collocazione != null)
			return collocazione;

		this.collocazione = DomainEJBFactory.getInstance().getCollocazioneBMT();
		return collocazione;
	}

	private AmministrazioneBiblioteca getBiblioteca() throws Exception {
		if (biblioteca != null)
			return biblioteca;
		this.biblioteca = DomainEJBFactory.getInstance().getBiblioteca();
		return biblioteca;
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log,
			UserTransaction tx) throws Exception {

		richiesta = (ImportaVO) params;
		ticket = richiesta.getTicket();
		codPolo = richiesta.getCodPolo();
		_log = log.getLogger();
		this.tx = tx;

		titDAO = new TitoloDAO();

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);

		try {
			String filename = prefissoOutput + ".htm";
			FileOutputStream fos = new FileOutputStream(richiesta.getDownloadPath() + File.separator + filename);
			streamOut = new PrintWriter(new OutputStreamWriter(fos, "UTF8"), true);
			output.addDownload(filename, richiesta.getDownloadPath() + File.separator + filename);
			writeReportHeader(streamOut);

			switch(richiesta.getStatoImport()) {
			case ImportaVO._STATO_CARICA_RECORD: {
				status_ok.set( (new File(getImportHome() + File.separator + richiesta.getImportFileName())).exists() );

				// 1. caricamento record unimarc su db
				// Modifica almaviva2 05.07.2012
				// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta
				if (richiesta.getNumRichiestaCaricamento().equals("")) {
					richiesta.setNumRichiestaCaricamento(richiesta.getIdBatch());
				}
				_log.debug(String.format("Lettura record da file Unimarc con idBatch=%s e inserimento Nr richiesta=%s",
						richiesta.getIdBatch(), richiesta.getNumRichiestaCaricamento()));
				writeReportRow(streamOut, String.format("Lettura record da file Unimarc con idBatch=%s e inserimento Nr richiesta=%s",
						richiesta.getIdBatch(), richiesta.getNumRichiestaCaricamento()) );

				if (status_ok.get()) {
					status_ok.set( exec_caricaRecord(richiesta.getImportFileName(), richiesta.getIdBatch(), richiesta.getNumRichiestaCaricamento()) == 0 );
				}

				// 2. identificazione autori-collane-soggetti-classi-marche-(...) uguali
				if (status_ok.get()) {
					status_ok.set( exec_checkStatoDati(richiesta.getIdBatch(), richiesta.getNumRichiestaCaricamento()) == 0 );
				}
				break;
			}

			case ImportaVO._STATO_UNI_950: {

				_log.debug(String.format("Trattamento dati posseduto per nr. richiesta: %s", richiesta.getNumRichiesta()));
				writeReportRow(streamOut, String.format("Trattamento dati posseduto per nr. richiesta: %s", richiesta.getNumRichiesta()));

				// 1. dati documento fisico uni.950
				ListaDati950VO dati;
				//dati = exec_datiDocFisico(richiesta.getIdBatch());
				dati = exec_datiDocFisico950(richiesta.getNumRichiesta(), richiesta.getIdBatch());
				status_ok.set(dati != null);
				break;
			}

			// Inizio Modifica almaviva2 27.09.2011 - Classe per il trattamento dei dati bibliografici/semantici della tabella import1
			case ImportaVO._STATO_UNI_700: {

				// Modificati i messaggi di inizio elaborazione come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17 (inserito n. richiesta)

				_log.debug("Trattamento dati autore per nr. richiesta: " + String.valueOf(richiesta.getNumRichiesta()) );
				streamOut.println(("<TR><TD>Trattamento dati bibliografici/semantici per nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiesta())
						+ "</TD></TR>"));
				streamOut.println("<TR><TD>Estrazione Autori - etichetta 7xx</TD></TR>");

				AreaDatiImportSuPoloVO areaDatiImportSuPoloVO = execFase2ter_estraiIdAutori(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));
					status_ok.set( areaDatiImportSuPoloVO != null );

					// almaviva2 Maggio 2015: Evolutiva richiesta da  per dividere il trattamento dell'estrazione Autori in due passi:
					// passo1: estrazione della lista dei soli id per tag '700', '701', '702', '710', '711', '712'
					// passo2: estrazione del record intero per ogni elemento della lista precedentemente creata
					// L'intervento serve perchè l'Import di DISCOTECA DI STATO ha circa 5.000.000 e si va in "out of memory"
					int lista001Size = areaDatiImportSuPoloVO.getListaEtichette001().size();
					String idDaElab="";
					int totRecords = 0;
					for (int i_001 = 0; i_001 < lista001Size; i_001++) {
						if ((++totRecords % 50) == 0)
							BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
						if ((totRecords % 1000) == 0)
							_log.debug("Record elaborati: " + totRecords);

						idDaElab = areaDatiImportSuPoloVO.getListaEtichette001().get(i_001);
						areaDatiImportSuPoloVO = execFase2ter_estraiDatiAutori(areaDatiImportSuPoloVO, idDaElab, log);

						if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray())) {
							areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
							areaDatiImportSuPoloVO = getInterrogazione().trattamentoAutore(areaDatiImportSuPoloVO, ticket);
							for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
								streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
							}
							areaDatiImportSuPoloVO.getListaSegnalazioni().clear();
						} else {
							streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Autori collegati</TD></TR>");
						}
					}

					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

				}
				break;
			}

			case ImportaVO._STATO_UNI_410: {

				_log.debug("Estrazione Titoli collegati diversi dai gerarchici - etichetta 4xx per nr. richiesta: " + richiesta.getNumRichiesta());
				// Modificati i messaggi di inizio elaborazione come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17 (inserito n. richiesta)
				streamOut.println(("<TR><TD>Trattamento dati bibliografici/semantici per nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiesta())
						+ "</TD></TR>"));
				streamOut.println("<TR><TD>Estrazione Titoli collegati diversi dai gerarchici- etichetta 4xx</TD></TR>");

				AreaDatiImportSuPoloVO areaDatiImportSuPoloVO;
				areaDatiImportSuPoloVO = execFase2ter_estraiTitoliCollegati4xx(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti Totale:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstrattiOriginali()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Estratti con Raggruppamento dei duplicati:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstrattiNoDuplicati()) + "</TD></TR>"));
	//				streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));

					status_ok.set( areaDatiImportSuPoloVO != null );
					if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray())) {
						areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
						areaDatiImportSuPoloVO = getInterrogazione().trattamentoTitCollegati4xx(areaDatiImportSuPoloVO, ticket);
						for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
							streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
						}
					} else {
						streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Titoli collegati</TD></TR>");
					}
					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

				}
				break;
			}

			case ImportaVO._STATO_UNI_500: {

				_log.debug("Estrazione Titoli collegati diversi dai gerarchici- etichetta 5xx");
				// Modificati i messaggi di inizio elaborazione come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17 (inserito n. richiesta)
				streamOut.println(("<TR><TD>Trattamento dati bibliografici/semantici per nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiesta())
						+ "</TD></TR>"));
				streamOut.println("<TR><TD>Estrazione Titoli collegati diversi dai gerarchici- etichetta 5xx</TD></TR>");

				// MODIFICA SETTEMBRE 2015 almaviva2; il trattamento delle 5xx deve essesre spezzato
				// a. trattamento delle 500 con le 928 e 929 per i titoli uniformi Musicali (COMPOSIZIONE)
				// b. gli altri 500

				// INZIO TRATTAMENTO con le 928 e 929 per i titoli uniformi Musicali (COMPOSIZIONE)
				AreaDatiImportSuPoloVO areaDatiImportSuPoloVO = execFase2ter_estraiTitoliCollegatiSolo500(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));
					status_ok.set( areaDatiImportSuPoloVO != null );
					if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray())) {
						areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
						areaDatiImportSuPoloVO = getInterrogazione().trattamentoTitCollegati5xx(areaDatiImportSuPoloVO, ticket);
						for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
							streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
						}
					} else {
						streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Titoli collegati</TD></TR>");
					}
					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

					// Intervento settembre 2015 - si inserisce un contatore per i record non trattati su richiesta di
					streamOut.println(("<TR><TD>Oggetti ammesso da UNIMARC ma non gestito da import:  " + String.valueOf(areaDatiImportSuPoloVO.getContAmmessiUnimarcNonGestiti()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti con tag inaspettato:  " + String.valueOf(areaDatiImportSuPoloVO.getContNonAmmessiUnimarc()) + "</TD></TR>"));
				}
				// FINE TRATTAMENTO con le 928 e 929 per i titoli uniformi Musicali (COMPOSIZIONE)

				// INZIO TRATTAMENTO gli altri 500
				areaDatiImportSuPoloVO = execFase2ter_estraiTitoliCollegatiAltri5xx(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));
					status_ok.set( areaDatiImportSuPoloVO != null );
					if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray())) {
						areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
						areaDatiImportSuPoloVO = getInterrogazione().trattamentoTitCollegati5xx(areaDatiImportSuPoloVO, ticket);
						for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
							streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
						}
					} else {
						streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Titoli collegati</TD></TR>");
					}
					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

					// Intervento settembre 2015 - si inserisce un contatore per i record non trattati su richiesta di
					streamOut.println(("<TR><TD>Oggetti ammesso da UNIMARC ma non gestito da import:  " + String.valueOf(areaDatiImportSuPoloVO.getContAmmessiUnimarcNonGestiti()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti con tag inaspettato:  " + String.valueOf(areaDatiImportSuPoloVO.getContNonAmmessiUnimarc()) + "</TD></TR>"));
				}
				// FINE TRATTAMENTO gli altri 500
				break;
			}

			case ImportaVO._STATO_UNI_600: {

				_log.debug("Estrazione Soggetti/Classi collegati - etichetta 6xx");
				// Modificati i messaggi di inizio elaborazione come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17 (inserito n. richiesta)
				streamOut.println(("<TR><TD>Trattamento dati bibliografici/semantici per nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiesta())
						+ "</TD></TR>"));
				streamOut.println("<TR><TD>Estrazione Soggetti/Classi collegati - etichetta 6xx</TD></TR>");

				AreaDatiImportSuPoloVO areaDatiImportSuPoloVO;
				areaDatiImportSuPoloVO = execFase2ter_estraiSogClaCollegati6xx(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));
					status_ok.set( areaDatiImportSuPoloVO != null );
					if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray())) {
						areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());

						areaDatiImportSuPoloVO = getInterrogazione().trattamentoSogClaCollegati6xx(areaDatiImportSuPoloVO, ticket);
						for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
							streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
						}
					} else {
						streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Soggetti/Classi collegati</TD></TR>");
					}
					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

				}
				break;
			}

			case ImportaVO._STATO_UNI_200: {
				// Modifica del 30.05.2012 I documenti vengono trattati separatamente dei loro legami e non
				// sequenzialmente nella 1000 prima dei loro legami

				_log.debug("Estrazione Elenco identificativi da trattare - etichetta 200");
				// Modificati i messaggi di inizio elaborazione come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17 (inserito n. richiesta)
				streamOut.println(("<TR><TD>Trattamento dati bibliografici/semantici per nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiesta())
						+ "</TD></TR>"));
				streamOut.println("<TR><TD>Estrazione Elenco identificativi da trattare - etichetta 200</TD></TR>");

				AreaDatiImportSuPoloVO areaDatiImportSuPoloVO = execFase2ter_estraiEtich001Ordinate(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));
					status_ok.set( areaDatiImportSuPoloVO != null );
					if (isFilled(areaDatiImportSuPoloVO.getListaEtichette001()) ) {
						String idDaElab="";
						int sizeIdDaElab = 0;
						int totRecords = 0;
						int lista001Size = areaDatiImportSuPoloVO.getListaEtichette001().size();
						for (int i_001 = 0; i_001 < lista001Size; i_001++) {
							if ((++totRecords % 50) == 0)
								BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
							if ((totRecords % 1000) == 0)
								_log.debug("Record elaborati: " + totRecords);

							idDaElab = areaDatiImportSuPoloVO.getListaEtichette001().get(i_001);
							sizeIdDaElab = idDaElab.length();
	//						streamOut.println(("<TR><TD>Estrazione etichette Unimarc relative all'oggetto " + idDaElab.substring(1,sizeIdDaElab) + "</TD></TR>"));

							areaDatiImportSuPoloVO = execFase2ter_estraiTitoloPerIdUnimarc(areaDatiImportSuPoloVO, richiesta.getNumRichiesta(), idDaElab.substring(1,sizeIdDaElab));
							if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
								streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));
							} else {
								status_ok.set( areaDatiImportSuPoloVO != null );
								if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray())) {
									areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
									areaDatiImportSuPoloVO = getInterrogazione().trattamentoDocumento(areaDatiImportSuPoloVO, ticket);
									for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
										streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
									}
									areaDatiImportSuPoloVO.getListaSegnalazioni().clear();
									if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
										continue;
									}
								}
							}
						}
					} else {
						streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Documenti e Legami</TD></TR>");
					}
					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

				}

				// Inizio modifica almaviva2 04.12.2012:
				// Questa gestione arriva dal trattamento 1000 da dove è stata asteriscata


				// Modificati i messaggi di inizio elaborazione come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17 (inserito n. richiesta)
				streamOut.println(("<TR><TD>Trattamento dati bibliografici/semantici per nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiesta())
						+ "</TD></TR>"));
				streamOut.println("<TR><TD>Estrazione Elenco identificativi Documenti Senza Titolo (W) da trattare - etichetta 001</TD></TR>");

				areaDatiImportSuPoloVO = execFase2ter_estraiEtich001Inferiori(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));
					status_ok.set( areaDatiImportSuPoloVO != null );
					if (isFilled(areaDatiImportSuPoloVO.getListaEtichette001()) ) {
						String idDaElab="";
						int sizeIdDaElab=0;
						int totRecords = 0;
						int lista001Size = areaDatiImportSuPoloVO.getListaEtichette001().size();
						for (int i_001 = 0; i_001 < lista001Size; i_001++) {
							if ((++totRecords % 50) == 0)
								BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
							if ((totRecords % 1000) == 0)
								_log.debug("Record elaborati: " + totRecords);

							idDaElab = areaDatiImportSuPoloVO.getListaEtichette001().get(i_001);
							sizeIdDaElab = idDaElab.length();
	//						streamOut.println(("<TR><TD>Estrazione etichette Unimarc relative all'oggetto " + idDaElab.substring(1,sizeIdDaElab) + "</TD></TR>"));

							areaDatiImportSuPoloVO = execFase2ter_estraiTitoloPerIdUnimarc(areaDatiImportSuPoloVO, richiesta.getNumRichiesta(), idDaElab.substring(1,sizeIdDaElab));
							if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
								streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));
							} else {
								status_ok.set( areaDatiImportSuPoloVO != null );
								if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray()) ) {
									areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
									areaDatiImportSuPoloVO = getInterrogazione().trattamentoDocumentoInferiore(areaDatiImportSuPoloVO, ticket);
									for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
										streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
									}
									areaDatiImportSuPoloVO.getListaSegnalazioni().clear();
									if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
										continue;
									}
								}
	//							streamOut.println(("<TR><TD>Estrazione etichette Unimarc relative ai legami 461 462 463 relative all'oggetto " + idDaElab.substring(1,sizeIdDaElab) + "</TD></TR>"));
								areaDatiImportSuPoloVO = execFase2ter_estraiLegameSuperiore(areaDatiImportSuPoloVO, richiesta.getNumRichiesta(), idDaElab.substring(1,sizeIdDaElab));
								if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
									streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));
								} else {
									status_ok.set( areaDatiImportSuPoloVO != null );
									if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray()) ) {
										areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
										areaDatiImportSuPoloVO = getInterrogazione().trattamentoLegamiDocumento(areaDatiImportSuPoloVO, idDaElab.substring(0,1), ticket);
										for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
											streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
										}
										areaDatiImportSuPoloVO.getListaSegnalazioni().clear();
									} else {
										streamOut.println(("<TR><TD>ATTENZIONE: non esiste legame a superiore per l'oggetto " + idDaElab.substring(1,sizeIdDaElab) + "</TD></TR>"));
									}
								}
							}
						}
					} else {
						streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Titoli natura W</TD></TR>");
					}
					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

				}
				// Fine modifica almaviva2 04.12.2012:
				break;
			}

			case ImportaVO._STATO_UNI_1000: {

				// Inizio modifica almaviva2 04.12.2012:
				// Questa gestione viene spostata nel trattamento della richiesta 200 cosi da avere in sequenza l'inserimento di tutti i documenti
				//    .... gestione creazione inferiori e legame a superiore contestuale
				// Fine modifica almaviva2 04.12.2012:

				_log.debug("Estrazione Elenco identificativi Documenti da trattare - Ricostruzione reticoli");
				// Modificati i messaggi di inizio elaborazione come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17 (inserito n. richiesta)
				streamOut.println(("<TR><TD>Trattamento dati bibliografici/semantici per nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiesta())
						+ "</TD></TR>"));
				streamOut.println("<TR><TD>Estrazione Elenco identificativi Documenti da trattare - etichetta 001</TD></TR>");
				AreaDatiImportSuPoloVO areaDatiImportSuPoloVO;
				areaDatiImportSuPoloVO = execFase2ter_estraiEtich001OrdinateTutte(richiesta.getNumRichiesta(), log);
				if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
					streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));

				} else {
					streamOut.println(("<TR><TD>Oggetti Estratti:  " + String.valueOf(areaDatiImportSuPoloVO.getContEstratti()) + "</TD></TR>"));
					status_ok.set( areaDatiImportSuPoloVO != null );
					if (isFilled(areaDatiImportSuPoloVO.getListaEtichette001()) ) {
						String idDaElab = "";
						int sizeIdDaElab = 0;
						int totRecords = 0;
						int lista001Size = areaDatiImportSuPoloVO.getListaEtichette001().size();
						_log.debug("Record 001 estratti: " + lista001Size);
						for (int i_001 = 0; i_001 < lista001Size; i_001++) {
							if ((++totRecords % 50) == 0)
								BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
							if ((totRecords % 1000) == 0)
								_log.debug("Record 001 elaborati: " + totRecords);

							idDaElab = areaDatiImportSuPoloVO.getListaEtichette001().get(i_001);
							sizeIdDaElab = idDaElab.length();

	//						areaDatiImportSuPoloVO = execFase2ter_estraiTitoloPerIdUnimarc(areaDatiImportSuPoloVO, richiesta.getNumRichiesta(), idDaElab.substring(1,sizeIdDaElab));
	//						if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
	//							// dopo che le select execFase2ter_estraiEtich001OrdinateTutte è stata modificata inserendo tutti gli oggetti
	//							// ANCHE QUELLI P (trattati nella costruzione dei legami semantici) è possibile che questo errore si
	//							// verifichi ma non sia da segnalare perchè è dovuto al fatto che la creazione dei legami bibliografici
	//							// sia invece ristretta ai soli I
	//							// streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));
	//						} else {
	//							// si elimina in trattamento del documento in quanto le i Doc superiori sono stati inseriti nel trattamento
	//							// delle 200 e gli inferiori sono stati inseriti nel ciclo sopra; rimangono solo i legami diversi dai
	//							// gerarchici e i semantici
	//							streamOut.println(("<TR><TD>Estrazione etichette Unimarc relative ai legami 400 500 e 700 relative all'oggetto " + idDaElab.substring(1,sizeIdDaElab) + "</TD></TR>"));
								areaDatiImportSuPoloVO = execFase2ter_estraiLegamiTitoli(areaDatiImportSuPoloVO, richiesta.getNumRichiesta(), idDaElab.substring(1,sizeIdDaElab));
								if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
									streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));
								} else {
									status_ok.set( areaDatiImportSuPoloVO != null );
									if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray()) ) {
										areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
										areaDatiImportSuPoloVO = getInterrogazione().trattamentoLegamiDocumento(areaDatiImportSuPoloVO, idDaElab.substring(0,1), ticket);
										for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
											streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
										}
										areaDatiImportSuPoloVO.getListaSegnalazioni().clear();
									}
								}
								// almaviva2 - Gennaio 2015: in caso di creazione dei legami (trattamento import 001) se l'oggetto di arrivo del legame
								// non è presente sulla import-id-link non si continua tentando di inserire il lemame successivo ma si
								// interrompe il trattamento cos' da non impostare con il valore "T" il rocord in oggetto; in questo modo
								// dopo aver risolto i problema si potrà richiedere nuovamente il trattamento 001 si record ancora vivi!!!
								if (!areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
									areaDatiImportSuPoloVO = execFase2ter_estraiLegamiTitoliSemantici(areaDatiImportSuPoloVO, richiesta.getNumRichiesta(), idDaElab.substring(1,sizeIdDaElab));
									if (areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
										streamOut.println(("<TR><TD>ATTENZIONE: errore nella select: " + areaDatiImportSuPoloVO.getTestoErrore() + "</TD></TR>"));
									} else {
										status_ok.set( areaDatiImportSuPoloVO != null );
										if (isFilled(areaDatiImportSuPoloVO.getListaTracciatoRecordArray()) ) {
											areaDatiImportSuPoloVO.setNrRichiesta(richiesta.getNumRichiesta());
											areaDatiImportSuPoloVO = getInterrogazione().trattamentoLegamiDocumento(areaDatiImportSuPoloVO, idDaElab.substring(0,1), ticket);
											for (int i = 0; i < areaDatiImportSuPoloVO.getListaSegnalazioni().size(); i++) {
												streamOut.println(areaDatiImportSuPoloVO.getListaSegnalazioni().get(i));
											}
											areaDatiImportSuPoloVO.getListaSegnalazioni().clear();
										}
									}
								}

								// Inizio Intervento interno 07.04.2014 - Richiesta  almaviva
								// alla fine della creazione dei legami si aggiorna il flag stato_id_input con il valore 'T' TRATTATO
								// per indicare che il trattamento bibliografico è stato conpletato così che in una eventuale ripartenza
								// del trattamento i record 200 già esaminati non vengano ripresi in considerazione
								if (!areaDatiImportSuPoloVO.getCodErr().equals("9999")) {
									areaDatiImportSuPoloVO = execFase2ter_update200Trattate(areaDatiImportSuPoloVO, richiesta.getNumRichiesta(),
											idDaElab.substring(1,sizeIdDaElab), log);
								}
								// Fine Intervento interno 07.04.2014 - Richiesta  almaviva
	//						}
						}
					} else {
						streamOut.println("<TR><TD>ATTENZIONE: non ci sono etichette Unimarc che rispondono alla richiesta di Documenti e Legami</TD></TR>");
					}
					streamOut.println(("<TR><TD>Oggetti Inseriti sulla Base Dati:  " + String.valueOf(areaDatiImportSuPoloVO.getContInseritiOK()) + "</TD></TR>"));
					streamOut.println(("<TR><TD>Oggetti Presenti sulla Base Dati da precedenti elaborazioni:  " + String.valueOf(areaDatiImportSuPoloVO.getContOldInsert()) + "</TD></TR>"));

				}
				break;
				// Fine modifica almaviva2 19.06.2012:
			}

			// Fine Modifica almaviva2 27.09.2011 - Classe per il trattamento dei dati bibliografici/semantici della tabella import1
			case ImportaVO._STATO_VERIFICA_BID: {
				// 1. caricamento record unimarc su db
				List<String> listaBid = new ArrayList<String>();
				status_ok.set( exec_verificaBid(richiesta.isRicercaInPolo(), richiesta.getNumRichiestaVerificaBid(), listaBid) == 0 );
				if (status_ok.get()) {
					// file con lista bid da catturare (da indice)
					File fileListaBid = new File(
							richiesta.getDownloadPath() + File.separator +
							"__BidCatturaIndice" + richiesta.getIdBatch() + ".txt");
					FileOutputStream bidOut = new FileOutputStream(fileListaBid.getParentFile()+File.separator+fileListaBid.getName());
					for (int i = 0; i < listaBid.size(); i++) {
						bidOut.write((listaBid.get(i) + "\r\n").getBytes());
					}
					FileUtil.close(bidOut);
					output.addDownload(fileListaBid.getName(), richiesta.getDownloadPath() + File.separator + fileListaBid.getName());
				}
				break;
			}

			// Modifica almaviva2 09.07.2012 - Inserimento nuovo valore per richiesta cancellazione tabelle lavoro per nr_richiesta
			case ImportaVO._STATO_PER_CANCELLAZIONE: {
				String esito="";


				streamOut.println(("<TR><TD>Inizio procedura di cancellazione record relativi a nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiestaCancellazione()) + "</TD></TR>"));

				esito = deleteRecordImportIdLink(richiesta.getNumRichiestaCancellazione(), log);
				streamOut.println(("<TR><TD> " + esito + "</TD></TR>"));
				if (esito.contains("ERROR:")) {
					output.setStato(status_ok.get() ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);
					return output;
				}

				esito = deleteRecordImport950(richiesta.getNumRichiestaCancellazione(), log);
				streamOut.println(("<TR><TD> " + esito + "</TD></TR>"));
				if (esito.contains("ERROR:")) {
					output.setStato(status_ok.get() ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);
					return output;
				}

				esito = deleteRecordImport1(richiesta.getNumRichiestaCancellazione(), log);
				streamOut.println(("<TR><TD> " + esito + "</TD></TR>"));
				if (esito.contains("ERROR:")) {
					output.setStato(status_ok.get() ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);
					return output;
				}
				streamOut.println(("<TR><TD>Fine procedura di cancellazione record relativi a nr. richiesta: "
						+ String.valueOf(richiesta.getNumRichiestaCancellazione()) + "</TD></TR>"));

				status_ok = new AtomicBoolean(true);
				break;
			}

			default:
				//errore non gestito
				status_ok.set(false);
				break;
		}
	
		output.setStato(status_ok.get() ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);
		return output;

		} catch (Exception e) {
			_log.error("", e);
			output.setStato(ConstantsJMS.STATO_ERROR);
			return output;

		} finally {
			endTransaction(tx, status_ok.get() );
			try {
				writeReportFooter(streamOut);
			} catch (Exception e) {	}
			FileUtil.flush(streamOut);
			FileUtil.close(streamOut);
		}
	}

	public static final String getImportHome() throws Exception {
		return CommonConfiguration.getProperty(Configuration.SBNWEB_IMPORT_UNIMARC_HOME, FileUtil.getTempFilesDir() );
	}

	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {
		return true;
	}

	public int exec_caricaRecord(String fileName, String idBatch, String numRichiesta) {
		Scanner scan = null;
		try {
			_log.debug(String.format("[LETTURA RECORD FILE UNIMARC con idBatch=%s e inserimento Nr. richiesta=%s]", idBatch, numRichiesta));

			File f = new File(getImportHome() + File.separator + fileName);
			if (!f.exists())
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
			BatchManager.getBatchManagerInstance().markForDeletion(f);
			_log.debug(String.format("File di input: %s", f.getAbsolutePath()) );
			_log.debug("");

			long fileSize = f.length();
			long readChars = 0;

			FileInputStream in = new FileInputStream(f);
			scan = new Scanner(new BufferedInputStream(in), "UTF8");
			Pattern p = Pattern.compile("\\x1E\\x1D"); //delimitatori unimarc
			scan.useDelimiter(p);

			int totRecords = 0;
			int nrRecords = 0;
			int totaleTags = 0;
			int nrRecordsErrati = 0;
			String currentTag;
			StringBuilder sql = new StringBuilder(1024);
			List<QueryData> inserts = new ArrayList<QueryData>(512);
			int numChiavi;

			MarcFile marcFile = new MarcFile();
			MarcRecord record = new MarcRecord();

			while (scan.hasNext()) {
				String line = scan.next();
				readChars += line.length();

				String nr = String.format("record %06d", ++totRecords);
				if ((totRecords % 100) == 0) {
					BatchManager.getBatchManagerInstance().checkForInterruption(idBatch);
					double pct = Math.min(100f * (double) readChars / fileSize, 100f);
					_log.debug(String.format("Record elaborati: %d (%d etichette) [%.3f%% completato]", totRecords, totaleTags, pct));
				}

				// record trovato
				// Inizio Modifica  almaviva5 11.05.2012 Modificata la gestione della ricerca dei caratteri UTF8
				// io record viene caricato sostituendo il carattere non UTF8 con il carattere  "^" (tilde)
				numChiavi = 0;
				String token = line + "  ";
				if (!testUTF8(token)) {
					nrRecordsErrati++;
					_log.warn(nr + " msg: Formato record non UTF-8");
					 //continue;
					token = token.replaceAll("\\uFFFD", "^");
				}
				// Fine Modifica  almaviva5 11.05.2012 Modificata la gestione della ricerca dei caratteri UTF8


				marcFile.parse(token, _log);
				if (marcFile.getBadRecordsNumber() == 0) {
					try {
						record.loadTags(
								marcFile.getLeader(),
								marcFile.getTagsInfo(),
								marcFile.getData(),
								totRecords,
								_log
						);

						if (record.getData("001").length < 1)
							throw new Exception("record senza identificativo (tag 001).");

					} catch (Exception e) {
						// record errato
						nrRecordsErrati++;
						_log.error(nr + ":");
						_log.error("           msg: " + e.getMessage());
						_log.error("        record: " + token);
						continue;
					}

					if (isFilled(marcFile.getMsgError())) {
						_log.warn(nr + " msg: " + marcFile.getMsgError());
					}

					// prepara la query
					Map<Integer, String> chiavi = record.getChiavi();
					int chiaviSize = ValidazioneDati.size(chiavi);
					inserts.clear();

					while (numChiavi < chiaviSize) {
						currentTag = chiavi.get(numChiavi);
						int tagsCount = ValidazioneDati.size(record.getData(currentTag));
						if (tagsCount < 1) {
							_log.warn(String.format("%s --> id %s, etichetta %s sfondamento limiti buffer: non elaborata.", nr, record.getIdRecord(), currentTag));
							numChiavi++;
							continue;
						}
						for (int j = 0; j < tagsCount; j++) {
							String data = record.getData(currentTag, j, true);
							if (isFilled(data)) {
								//almaviva5_20130724 sostituzione caratteri fine record con spazio
								data = data.replace('\u001E', '\u0020');
								char last = data.charAt(data.length() - 1);
								if (!ValidazioneDati.in(last, '\u0020') )
									_log.warn(String.format("%s --> id %s, etichetta %s lunghezza errata.", nr, record.getIdRecord(), currentTag));

								sql.setLength(0);
								// Modifica almaviva2 09.07.2012
								// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta (nuovo campo
								// su tabella import1 id_batch che contiene il progr batch)
//								sql.append("INSERT INTO import1 (id_input, leader, tag, indicatore1, indicatore2, id_link, dati, nr_richiesta) VALUES (");
								sql.append("INSERT INTO import1 (id_input, leader, tag, indicatore1, indicatore2, id_link, dati, nr_richiesta, id_batch) VALUES (");
								sql.append("'").append(escapeSql(record.getIdRecord())).append("'");
								sql.append(", '").append(record.getLeader()).append("'");
								sql.append(", '").append(currentTag).append("'");
								sql.append(", '").append(escapeSql(record.getIndicatore1(currentTag, j))).append("'");
								sql.append(", '").append(escapeSql(record.getIndicatore2(currentTag, j))).append("'");
								sql.append(", '").append(escapeSql(ValidazioneDati.trunc(record.getIdLink(currentTag, j), 30))).append("'");
								sql.append(", :param");
//								sql.append(", ").append(idBatch);
								sql.append(", ").append(numRichiesta);
								sql.append(", ").append(idBatch);
								sql.append(")");
								//executeInsertUpdate(sql.toString(), data);
								inserts.add(new ImportQueryData(sql.toString(), data));
							} else
								_log.warn(String.format("%s --> id %s, etichetta %s vuota: non elaborata.", nr, record.getIdRecord(), currentTag));

						}
						numChiavi++;
						totaleTags++;
					}
					executeInsertUpdate(inserts);
					//marcFile = null;
					//record = null;

					nrRecords++;

				} else {
					// record errato
					nrRecordsErrati++;
					_log.error(nr + ":");
					_log.error("           msg: " + marcFile.getMsgError());
					_log.error("        record: " + marcFile.getBadRecords());
				}
			}

			// Modificati i messaggi di errore come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17
			_log.debug("");
			_log.debug("*****************");
			_log.debug("- n. records in input    : " + totRecords + " (" + totaleTags + " etichette)");
			_log.debug("- n. records caricati    : " + nrRecords );
			_log.debug("- di cui con segnalazione: " + nrRecordsErrati);
			_log.debug("- n. records scartati    : " + (totRecords - nrRecords) );
			_log.debug("*****************");
			_log.debug("");

			writeReportRow(streamOut, "- n. records in input    : " + totRecords + " (" + totaleTags + " etichette)");
			writeReportRow(streamOut, "- n. records caricati    : " + nrRecords );
			writeReportRow(streamOut, "- di cui con segnalazione: " + nrRecordsErrati);
			writeReportRow(streamOut, "- n. records scartati    : " + (totRecords - nrRecords) );

		} catch (Exception e) {
			_log.error("[Exception] " + e.getMessage(), e);
			return 1;
		} finally {
			try {
				scan.close();
			} catch (Exception e) {	}
		}

		return 0;
	}

	private static final int INVALID_UTF8_CODEPOINT = 0xFFFD;
	private PrintWriter streamOut;
	private ImportaVO richiesta;

	private boolean testUTF8(String token) {
		for (int i = 0; i < token.length(); i++)
			if (token.codePointAt(i) == INVALID_UTF8_CODEPOINT)
				return false;

		return true;
	}

	private StatoRecord cercaTitoloPolo(String tag, String id, boolean checkStardard) throws Exception {
		startSession();
		try {
			if (checkStardard) {
				if (MarcRecord.isStandardSBN(id, tag)) {
					return (titDAO.esisteTitolo(id)) ? StatoRecord.PRESENTE : StatoRecord.DA_INSERIRE;
				} else {
					// ID non SBN standard
					return StatoRecord.DA_INSERIRE; // I
				}
			} else {
				return (titDAO.esisteTitolo(id)) ? StatoRecord.PRESENTE : StatoRecord.DA_INSERIRE;
			}
		} finally {
			endTransaction(tx, true);
		}
	}

	private StatoRecord cercaTitoloIndice(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPassInterrTitVO,
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn)
			throws Exception {

		areaDatiPassReturn = getInterrogazione().ricercaTitoli(areaDatiPassInterrTitVO, ticket);
		if (findError(areaDatiPassReturn)) {
			return StatoRecord.DA_INSERIRE; // I
		} else {
			// trovato
			return StatoRecord.DA_CATTURARE; // C
		}
	}

	StatoRecord impostaRicercaTitolo(
			String tag, String id, InterrogazioneTitoloGeneraleVO interrTitGen,
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPassInterrTitVO,
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn)
			throws Exception {
		interrTitGen.setBid(id);
		// default stringhe vuote
		interrTitGen.setTitolo("");
		interrTitGen.setTipoMateriale("*");
		interrTitGen.setNaturaSelez1("");
		interrTitGen.setNaturaSelez2("");
		interrTitGen.setNaturaSelez3("");
		interrTitGen.setNaturaSelez4("");
		interrTitGen.setTipiRecordSelez("");
		interrTitGen.setPaeseSelez("");
		interrTitGen.setLinguaSelez("");
		interrTitGen.setTipoDataSelez("");
		interrTitGen.setData1A("");
		interrTitGen.setData1Da("");
		interrTitGen.setData2A("");
		interrTitGen.setData2Da("");
		interrTitGen.setNomeCollegato("");
		interrTitGen.setLuogoPubbl("");
		interrTitGen.setResponsabilitaSelez("");
		interrTitGen.setRelazioniSelez("");
		interrTitGen.setSottoNaturaDSelez("");
		interrTitGen.setNumStandardSelez("");
		interrTitGen.setNumStandard1("");
		interrTitGen.setGenereSelez1("");
		interrTitGen.setGenereSelez2("");
		interrTitGen.setGenereSelez3("");
		interrTitGen.setGenereSelez4("");

		if (MarcRecord.isStandardSBN(id, tag)) {
			// ID SBN standard
			// controlla presenza in POLO
			// titoli
			areaDatiPassInterrTitVO.setRicercaPolo(true);
			areaDatiPassInterrTitVO.setRicercaIndice(false);
			areaDatiPassInterrTitVO.setInterTitGen(interrTitGen);
			areaDatiPassReturn = getInterrogazione().ricercaTitoli(areaDatiPassInterrTitVO, ticket);
			if (findError(areaDatiPassReturn)) {
				// non cerca in indice (commento temporaneo 30ago2011)
				return StatoRecord.DA_INSERIRE;
				// controlla presenza in INDICE
//				areaDatiPassInterrTitVO.setRicercaPolo(false);
//				areaDatiPassInterrTitVO.setRicercaIndice(true);
//				areaDatiPassReturn = getInterrogazione().ricercaTitoli(areaDatiPassInterrTitVO, ticket);
//				if (findError(areaDatiPassReturn)) {
//					return StatoRecord.DA_INSERIRE;
//				} else {
//					// trovato in indice
//					return _STATO_REC_CATTURARE;
//				}
			} else {
				// trovato in polo
				return StatoRecord.PRESENTE;
			}
		} else {
			// ID non SBN standard
			return StatoRecord.DA_INSERIRE;
		}
	}

	StatoRecord impostaRicercaAutore(
			String tag, String id, InterrogazioneAutoreGeneraleVO interrAutGen,
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPassInterrAutVO,
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn)
			throws Exception {
		interrAutGen.setVid(id);
		// default stringhe vuote
		interrAutGen.setNome("");
		interrAutGen.setIsadn("");
		// continua
		// vedi SbnGestioneAutoriDao (line 497)
		// ...

		if (MarcRecord.isStandardSBN(id, tag)) {
			// ID SBN standard
			// controlla presenza in POLO
			// autori
			areaDatiPassInterrAutVO.setRicercaPolo(true);
			areaDatiPassInterrAutVO.setRicercaIndice(false);
			areaDatiPassInterrAutVO.setInterrGener(interrAutGen);

			areaDatiPassReturn = getInterrogazione().ricercaAutori(areaDatiPassInterrAutVO, ticket);
			if (findError(areaDatiPassReturn)) {
				// non cerca in indice (commento temporaneo 30ago2011)
				return StatoRecord.DA_INSERIRE;
//				// controlla presenza in INDICE
//				areaDatiPassInterrAutVO.setRicercaPolo(false);
//				areaDatiPassInterrAutVO.setRicercaIndice(true);
//				areaDatiPassReturn = getInterrogazione().ricercaAutori(areaDatiPassInterrAutVO, ticket);
//				if (findError(areaDatiPassReturn)) {
//					return StatoRecord.DA_INSERIRE;
//				} else {
//					// trovato in indice
//					return _STATO_REC_CATTURARE;
//				}
			} else {
				// trovato in polo
				return StatoRecord.PRESENTE;
			}
		} else {
			// ID non SBN standard
			return StatoRecord.DA_INSERIRE;
		}
	}

	public int exec_checkStatoDati(String idBatch, String numRichiesta) {
		try {
			_log.debug("[VERIFICA lo STATO dei DOCUMENTI in Polo]");
			//_log.debug(" * Legenda: P=Presente; I=Da caricare/inserire; C=Da catturare; N=Non definito");
			_log.debug("");

			// --- LISTA BID
			// Modifica almaviva2 09.07.2012
			// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta (nuovo campo
			// su tabella import1 id_batch che contiene il progr batch)
//			List<?> list = getBid(numRichiesta);
			List<?> listaId = getBid(numRichiesta, idBatch);
			int checked = 0;
			List<String> listPresenti = new ArrayList<String>();
			int totaleIds = ValidazioneDati.size(listaId);
			StringBuilder sql = new StringBuilder(1024);
			for (int i = 0; i < totaleIds; i++) {
				String id = (String) listaId.get(i);
				char stato = cercaTitoloPolo("001", id, true).getStato();

				// aggiorna stato su import1
				sql.setLength(0);
				sql.append("update import1 SET stato_id_input='").append(stato).append("' ");
				sql.append("where id_input='").append(escapeSql(id)).append("' and id_batch=").append(idBatch);
				executeInsertUpdate(ImportQueryData.asList(sql.toString(), null));
				StatoRecord statoRecord = StatoRecord.of(stato);

				//_log.debug("id: " + id + " --> " + statoRecord);

				if ((++checked % 100) == 0) {
					BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
					_log.debug(String.format("Stato verificato: %d record di %d.", checked, totaleIds) );
				}

				if (statoRecord == StatoRecord.PRESENTE)
					listPresenti.add(id);
			}
			_log.debug("");
			int totaleIdPresenti = ValidazioneDati.size(listPresenti);
			for (int i = 0; i < totaleIdPresenti; i++) {
				// inserisce bid presenti in polo su import_id_link
				sql.setLength(0);
				sql.append("insert into import_id_link (id_input, id_inserito, nr_richiesta) values ('")
					.append(listPresenti.get(i)).append("', '").append(listPresenti.get(i)).append("', ")
					.append(numRichiesta).append(")");
				executeInsertUpdate(ImportQueryData.asList(sql.toString(), null));
			}


			// Modifica almaviva2 09.07.2012
			// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta (nuovo campo
			// su tabella import1 id_batch che contiene il progr batch)
			// si effettua sempre la statistica x id_batch; se id_batch <> da nr_richiesta si effettua anche il titale.

//			_log.debug("[STATISTICHE DI CARICAMENTO relative al Nr. richesta="+"]");
			_log.debug("[STATISTICHE DI CARICAMENTO relative a Identificativo batch="+ idBatch +"]");
			_log.debug("");

			Object[] record;
			sql.setLength(0);

			// ricava i tag caricati
			sql.append("select tag  as etichetta, ");
			sql.append(" case ");
			sql.append(" when tag  = '001' then 'Numero Record' ");
			sql.append(" when tag  = '005' then 'Data di creazione record' ");
			sql.append(" when tag  = '010' then 'Numero Standard ISBN' ");
			sql.append(" when tag  = '011' then 'Numero Standard ISSN' ");
			sql.append(" when tag  = '012' then 'Impronta editoriale  per materiale antico' ");
			sql.append(" when tag  = '013' then 'Numero Standard ISMN' ");
			sql.append(" when tag  = '020' then 'Numero Standard BNI' ");
			sql.append(" when tag  = '035' then 'Altra Tipologia di Numero Standard' ");
			sql.append(" when tag  = '071' then 'Numero di lastra' ");
			sql.append(" when tag  = '100' then 'Carta d’Identità' ");
			sql.append(" when tag  = '101' then 'Lingua' ");
			sql.append(" when tag  = '102' then 'Paese' ");
			sql.append(" when tag  = '105' then 'Dati Codificati Relativi a Monografie' ");
			sql.append(" when tag  = '110' then 'Dati Codificati Relativi a Periodici' ");

			// Modifica Ottobre 2013 inserita messaggistica tag 115 e 127 prima mancante su report di import
			sql.append(" when tag  = '115' then 'Dati Codificati Videoregistrazioni e Registrazioni' ");
			sql.append(" when tag  = '127' then 'Dati Codificati sulla durata della Registrazione' ");

			sql.append(" when tag  = '200' then 'Area del Titolo e Responsabilità' ");
			sql.append(" when tag  = '205' then 'Area dell’Edizione' ");
			sql.append(" when tag  = '207' then 'Area della Numerazione' ");
			sql.append(" when tag  = '210' then 'Area della Pubblicazione' ");
			sql.append(" when tag  = '215' then 'Area della Collazione' ");
			sql.append(" when tag  = '225' then 'Collezione - Series' ");
			sql.append(" when tag  = '300' then 'Area delle Note' ");

			// Modifica Ottobre 2013 inserita messaggistica tag 306 prima mancante su report di import
			sql.append(" when tag  = '306' then 'Note pertinenti la registrazione e distribuzione Area musicale' ");

			sql.append(" when tag  = '311' then 'Note sui legami con titoli' ");
			sql.append(" when tag  = '312' then 'Note legami con varianti del titolo' ");
			sql.append(" when tag  = '314' then 'Note all’autore di responsabilità principale' ");

			// Modifica Dicembre 2014 inserita messaggistica tag 318 prima mancante su report di import
			sql.append(" when tag  = '318' then 'Note alla rappresentazione (area Musica)' ");

			sql.append(" when tag  = '321' then 'Abstract' ");
			sql.append(" when tag  = '326' then 'Periodicità' ");
			sql.append(" when tag  = '330' then 'Sommario o Abstract' ");
			sql.append(" when tag  = '410' then 'Collezioni' ");
			sql.append(" when tag  = '421' then 'Legame ‘ha per supplemento' ");
			sql.append(" when tag  = '422' then 'Legame ‘supplemento di’' ");
			sql.append(" when tag  = '423' then 'Legame ‘pubblicato con’' ");
			sql.append(" when tag  = '430' then 'Legame ‘continuazione di’' ");
			sql.append(" when tag  = '431' then 'Legame ‘continua in parte con’' ");
			sql.append(" when tag  = '434' then 'Legame ‘ha assorbito’' ");
			sql.append(" when tag  = '447' then 'Legame ‘fuso con’' ");
			sql.append(" when tag  = '451' then 'Legame ‘altra edizione di’' ");
			sql.append(" when tag  = '454' then 'Legame ‘traduzione di’' ");
			sql.append(" when tag  = '461' then 'Legame al superiore (‘fa parte di’ verso livello più alto)' ");
			sql.append(" when tag  = '462' then 'Legame per l’intermedia (‘contienè o ‘fa parte di’ verso livello intermedio)' ");
			sql.append(" when tag  = '463' then 'Legame all’inferiore (‘contienè verso livello più basso)' ");
			sql.append(" when tag  = '464' then 'Legame al titolo analitico' ");
			sql.append(" when tag  = '488' then 'Legame ‘continuazione di’' ");
			sql.append(" when tag  = '500' then 'Titolo uniforme' ");
			sql.append(" when tag  = '510' then 'Titolo parallelo' ");
			sql.append(" when tag  = '517' then 'Variante del titolo' ");
			sql.append(" when tag  = '520' then 'Continuazione di (periodici)' ");
			sql.append(" when tag  = '530' then 'Titolo uniforme (periodici)' ");
			sql.append(" when tag  = '532' then 'Titolo estrapolato' ");
			sql.append(" when tag  = '606' then 'Soggetto' ");
			sql.append(" when tag  = '620' then 'Luogo di pubblicazione (materiale antico)' ");
			sql.append(" when tag  = '676' then 'Classificazione Dewey' ");
			sql.append(" when tag  = '700' then 'Autore personale : Responsabilità Principale ' ");
			sql.append(" when tag  = '701' then 'Autore personale : Responsabilità Alternativa' ");
			sql.append(" when tag  = '702' then 'Autore personale : Responsabilità Secondaria ' ");
			sql.append(" when tag  = '710' then 'Autore Collettivo: Responsabilità Principale ' ");
			sql.append(" when tag  = '711' then 'Autore Collettivo: Responsabilità Alternativa' ");
			sql.append(" when tag  = '712' then 'Autore Collettivo: Responsabilità Secondaria ' ");
			sql.append(" when tag  = '790' then 'Rinvii Autori Personali' ");
			sql.append(" when tag  = '791' then 'Rinvii Autori Collettivi' ");
			sql.append(" when tag  = '801' then 'Fonte di provenienza del record (Polo SBN)' ");
			sql.append(" when tag  = '856' then 'Indirizzo della risorsa elettronica' ");
			sql.append(" when tag  = '899' then 'Denominazione della biblioteca' ");
			sql.append(" when tag  = '921' then 'Marca editoriale (per materiale antico)' ");
			sql.append(" when tag  = '950' then 'Collocazioni ed Inventari' ");
			sql.append(" when tag  = '951' then 'Acquisti' ");
			sql.append(" else ");
			sql.append(" 'tag non previsto' ");
			sql.append(" end, ");
			sql.append(" count(*) as occorrenze from import1   ");
//			strBuild.append(" where nr_richiesta = "+numRichiesta+" ");
			sql.append(" where id_batch = "+idBatch+" ");
			sql.append(" group by 1 ");
			sql.append(" order by 1 ");
			listaId = getRecords(sql);
			for (int i = 0; i < listaId.size(); i++) {
				record = (Object[]) listaId.get(i);
				// nr.record (etichetta - traduzione etichetta)
				_log.debug("nr." + ((Number)record[2]).longValue() + " etichette " + (String)record[0] + " (" + (String)record[1] + ")");
			}

			_log.debug("");
			sql.setLength(0);
			// nr. notizie presenti in polo e da inserire
			sql.append("select stato_id_input  as stato_del_record, ");
			sql.append(" case ");
			sql.append(" when stato_id_input = 'P' then 'Notizie presenti in Polo' ");
			sql.append(" else ");
			sql.append(" 'Notizie da inserire' ");
			sql.append(" end, ");
			sql.append(" count(*) as occorrenze from import1   ");
//			strBuild.append(" where nr_richiesta = "+numRichiesta+" ");
			sql.append(" where id_batch = "+idBatch+" ");
			sql.append(" and tag = '001' ");
			sql.append(" group by 1 ");
			listaId = getRecords(sql);
			for (int i = 0; i < listaId.size(); i++) {
				record = (Object[]) listaId.get(i);
				// nr.occorrenze presenti/inserire (stato del record)
				_log.debug("nr." + ((Number)record[2]).longValue() + " " + (String)record[1] + " (stato record " + (String)record[1] + ")");
			}

			_log.debug("");
			sql.setLength(0);
			// indicatori delle notizie caricate
			// - DOCUMENTI
			sql.append("select SUBSTRING( i1.leader,8,2) as tipo_record,");
			sql.append(" case ");
			sql.append(" when upper(SUBSTRING( i1.leader,8,2)) = 'M2' then");
			sql.append(" case when i1.indicatore1='0' then 'W Opere senza titolo sigificativo'");
			sql.append(" else 'M Monografie a livelli'");
			sql.append(" end");
			sql.append(" when upper(SUBSTRING( i1.leader,8,2))='M0' then 'M Monografie '");
			sql.append(" when upper(SUBSTRING( i1.leader,8,2))='M1' then 'M Monografie di raggruppamento'");
			sql.append(" when upper(SUBSTRING( i1.leader,8,1))='S' then 'S Periodici'");
			sql.append(" when upper(SUBSTRING( i1.leader,8,1))='A' then 'N Spogli'");
			sql.append(" else 'Da verificare'");
			sql.append(" end  as tipo_documento, count(*) as occorrenze from import1 i1");
//			strBuild.append(" where i1.nr_richiesta="+numRichiesta+" ");
			sql.append(" where i1.id_batch="+idBatch+" ");
			sql.append(" and i1.tag='200'");
			sql.append(" group by 1,2");
			sql.append(" order by 2;");

			listaId = getRecords(sql);
			for (int i = 0; i < listaId.size(); i++) {
				record = (Object[]) listaId.get(i);
				// nr.occorrenze tipo documento (tipo record)
				_log.debug("nr." + ((Number)record[2]).longValue() + " " + (String)record[1] + " (tipo record " + (String)record[1] + ")");
			}
			_log.debug("");
			sql.setLength(0);


			// Inizio Eliminati i messaggi statistici come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17


			// Fine Eliminati i messaggi statistici come da Mail almaviva1 del mercoledì 27 giugno 2012 18.17


			_log.debug("");


			if (!idBatch.equals(numRichiesta)) {
				_log.debug("[STATISTICHE DI CARICAMENTO relative al Totale Generale per Nr. richiesta="+numRichiesta+"]");
				_log.debug("");

//				Object[] record;
//				StringBuilder strBuild = new StringBuilder(1024);

				sql.setLength(0);

				// ricava i tag caricati
				sql.append("select tag  as etichetta, ");
				sql.append(" case ");
				sql.append(" when tag  = '001' then 'Numero Record' ");
				sql.append(" when tag  = '005' then 'Data di creazione record' ");
				sql.append(" when tag  = '010' then 'Numero Standard ISBN' ");
				sql.append(" when tag  = '011' then 'Numero Standard ISSN' ");
				sql.append(" when tag  = '012' then 'Impronta editoriale  per materiale antico' ");
				sql.append(" when tag  = '013' then 'Numero Standard ISMN' ");
				sql.append(" when tag  = '020' then 'Numero Standard BNI' ");
				sql.append(" when tag  = '035' then 'Altra Tipologia di Numero Standard' ");
				sql.append(" when tag  = '071' then 'Numero di lastra' ");
				sql.append(" when tag  = '100' then 'Carta d’Identità' ");
				sql.append(" when tag  = '101' then 'Lingua' ");
				sql.append(" when tag  = '102' then 'Paese' ");
				sql.append(" when tag  = '105' then 'Dati Codificati Relativi a Monografie' ");
				sql.append(" when tag  = '110' then 'Dati Codificati Relativi a Periodici' ");
				sql.append(" when tag  = '200' then 'Area del Titolo e Responsabilità' ");
				sql.append(" when tag  = '205' then 'Area dell’Edizione' ");
				sql.append(" when tag  = '207' then 'Area della Numerazione' ");
				sql.append(" when tag  = '210' then 'Area della Pubblicazione' ");
				sql.append(" when tag  = '215' then 'Area della Collazione' ");
				sql.append(" when tag  = '225' then 'Collezione - Series' ");
				sql.append(" when tag  = '300' then 'Area delle Note' ");
				sql.append(" when tag  = '311' then 'Note sui legami con titoli' ");
				sql.append(" when tag  = '312' then 'Note legami con varianti del titolo' ");
				sql.append(" when tag  = '314' then 'Note all’autore di responsabilità principale' ");
				sql.append(" when tag  = '321' then 'Abstract' ");
				sql.append(" when tag  = '326' then 'Periodicità' ");
				sql.append(" when tag  = '330' then 'Sommario o Abstract' ");
				sql.append(" when tag  = '410' then 'Collezioni' ");
				sql.append(" when tag  = '421' then 'Legame ‘ha per supplemento' ");
				sql.append(" when tag  = '422' then 'Legame ‘supplemento di’' ");
				sql.append(" when tag  = '423' then 'Legame ‘pubblicato con’' ");
				sql.append(" when tag  = '430' then 'Legame ‘continuazione di’' ");
				sql.append(" when tag  = '431' then 'Legame ‘continua in parte con’' ");
				sql.append(" when tag  = '434' then 'Legame ‘ha assorbito’' ");
				sql.append(" when tag  = '447' then 'Legame ‘fuso con’' ");
				sql.append(" when tag  = '451' then 'Legame ‘altra edizione di’' ");
				sql.append(" when tag  = '454' then 'Legame ‘traduzione di’' ");
				sql.append(" when tag  = '461' then 'Legame al superiore (‘fa parte di’ verso livello più alto)' ");
				sql.append(" when tag  = '462' then 'Legame per l’intermedia (‘contienè o ‘fa parte di’ verso livello intermedio)' ");
				sql.append(" when tag  = '463' then 'Legame all’inferiore (‘contienè verso livello più basso)' ");
				sql.append(" when tag  = '464' then 'Legame al titolo analitico' ");
				sql.append(" when tag  = '488' then 'Legame ‘continuazione di’' ");
				sql.append(" when tag  = '500' then 'Titolo uniforme' ");
				sql.append(" when tag  = '510' then 'Titolo parallelo' ");
				sql.append(" when tag  = '517' then 'Variante del titolo' ");
				sql.append(" when tag  = '520' then 'Continuazione di (periodici)' ");
				sql.append(" when tag  = '530' then 'Titolo uniforme (periodici)' ");
				sql.append(" when tag  = '532' then 'Titolo estrapolato' ");
				sql.append(" when tag  = '606' then 'Soggetto' ");
				sql.append(" when tag  = '620' then 'Luogo di pubblicazione (materiale antico)' ");
				sql.append(" when tag  = '676' then 'Classificazione Dewey' ");
				sql.append(" when tag  = '700' then 'Autore personale : Responsabilità Principale ' ");
				sql.append(" when tag  = '701' then 'Autore personale : Responsabilità Alternativa' ");
				sql.append(" when tag  = '702' then 'Autore personale : Responsabilità Secondaria ' ");
				sql.append(" when tag  = '710' then 'Autore Collettivo: Responsabilità Principale ' ");
				sql.append(" when tag  = '711' then 'Autore Collettivo: Responsabilità Alternativa' ");
				sql.append(" when tag  = '712' then 'Autore Collettivo: Responsabilità Secondaria ' ");
				sql.append(" when tag  = '790' then 'Rinvii Autori Personali' ");
				sql.append(" when tag  = '791' then 'Rinvii Autori Collettivi' ");
				sql.append(" when tag  = '801' then 'Fonte di provenienza del record (Polo SBN)' ");
				sql.append(" when tag  = '856' then 'Indirizzo della risorsa elettronica' ");
				sql.append(" when tag  = '899' then 'Denominazione della biblioteca' ");
				sql.append(" when tag  = '921' then 'Marca editoriale (per materiale antico)' ");
				sql.append(" when tag  = '950' then 'Collocazioni ed Inventari' ");
				sql.append(" when tag  = '951' then 'Acquisti' ");
				sql.append(" else ");
				sql.append(" 'tag non previsto' ");
				sql.append(" end, ");
				sql.append(" count(*) as occorrenze from import1   ");
				sql.append(" where nr_richiesta = "+numRichiesta+" ");
				sql.append(" group by 1 ");
				sql.append(" order by 1 ");
				listaId = getRecords(sql);
				for (int i = 0; i < listaId.size(); i++) {
					record = (Object[]) listaId.get(i);
					// nr.record (etichetta - traduzione etichetta)
					_log.debug("nr." + ((Number)record[2]).longValue() + " etichette " + (String)record[0] + " (" + (String)record[1] + ")");
				}

				_log.debug("");
				sql.setLength(0);
				// nr. notizie presenti in polo e da inserire
				sql.append("select stato_id_input  as stato_del_record, ");
				sql.append(" case ");
				sql.append(" when stato_id_input = 'P' then 'Notizie presenti in Polo' ");
				sql.append(" else ");
				sql.append(" 'Notizie da inserire' ");
				sql.append(" end, ");
				sql.append(" count(*) as occorrenze from import1   ");
				sql.append(" where nr_richiesta = "+numRichiesta+" ");
				sql.append(" and tag = '001' ");
				sql.append(" group by 1 ");
				listaId = getRecords(sql);
				for (int i = 0; i < listaId.size(); i++) {
					record = (Object[]) listaId.get(i);
					// nr.occorrenze presenti/inserire (stato del record)
					_log.debug("nr." + ((Number)record[2]).longValue() + " " + (String)record[1] + " (stato record " + (String)record[1] + ")");
				}

				_log.debug("");
				sql.setLength(0);
				// indicatori delle notizie caricate
				// - DOCUMENTI
				sql.append("select SUBSTRING( i1.leader,8,2) as tipo_record,");
				sql.append(" case ");
				sql.append(" when upper(SUBSTRING( i1.leader,8,2)) = 'M2' then");
				sql.append(" case when i1.indicatore1='0' then 'W Opere senza titolo sigificativo'");
				sql.append(" else 'M Monografie a livelli'");
				sql.append(" end");
				sql.append(" when upper(SUBSTRING( i1.leader,8,2))='M0' then 'M Monografie '");
				sql.append(" when upper(SUBSTRING( i1.leader,8,2))='M1' then 'M Monografie di raggruppamento'");
				sql.append(" when upper(SUBSTRING( i1.leader,8,1))='S' then 'S Periodici'");
				sql.append(" when upper(SUBSTRING( i1.leader,8,1))='A' then 'N Spogli'");
				sql.append(" else 'Da verificare'");
				sql.append(" end  as tipo_documento, count(*) as occorrenze from import1 i1");
				sql.append(" where i1.nr_richiesta="+numRichiesta+" ");
				sql.append(" and i1.tag='200'");
				sql.append(" group by 1,2");
				sql.append(" order by 2;");

				listaId = getRecords(sql);
				for (int i = 0; i < listaId.size(); i++) {
					record = (Object[]) listaId.get(i);
					// nr.occorrenze tipo documento (tipo record)
					_log.debug("nr." + ((Number)record[2]).longValue() + " " + (String)record[1] + " (tipo record " + (String)record[1] + ")");
				}
				_log.debug("");
				sql.setLength(0);

				_log.debug("");

			}

		} catch (Exception e) {
			_log.error("[Exception] " + e.getMessage(), e);
			return 1;
		}

		return 0;
	}

	public ListaDati950VO exec_datiDocFisico950(String numRichiestaImport, String numRichiestaCorrente) throws IOException {
		ListaDati950VO datiVO = new ListaDati950VO();

		long numSerieInvInsOK = 0;
		long numInvInsOK = 0;
		long numSezCollInsOK = 0;
		long numCollInsOK = 0;
		long numEseInsOK = 0;
		long numSerieInvInsNO = 0;
		long numInvInsNO = 0;
		long numSezCollInsNO = 0;
		long numCollInsNO = 0;
		long numEseInsNO = 0;
		long numSerieInvDupl = 0;
		long numInvDupl = 0;
		long numSezCollDupl = 0;
		long numCollDupl = 0;

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);
			String firmaUtente = DaoManager.getFirmaUtente(ticket);

			sql.append("select count(*) as num from import950 where nr_richiesta_import=").append(numRichiestaImport);
			List<?> list = getRecords(sql);
			sql.setLength(0);
			long numRecords = ((Number)list.get(0)).longValue();
			if (numRecords == 0) {
				// prima elaborazione 950
				// estrapola tutte le 950 del record indicato
				//almaviva5_20120830 da RG: recuperando da import_id_link il BID assegnato in caso di documento inserito
				//almaviva5_20130620 da RG: aggiunto check nr_richiesta su import_id_link
				sql.append("select CASE when c.id_inserito isnull then a.id_input else c.id_inserito END, ");
				sql.append("a.leader, a.indicatore1, a.indicatore2, a.dati,  ");
				sql.append("CASE when c.id_inserito isnull then a.stato_id_input else 'P' END,  ");
				sql.append("a.id  ");
				sql.append("from import1 as a ");
				sql.append("left outer join import_id_link as c on c.id_input=a.id_input and c.nr_richiesta=").append(numRichiestaImport);
				sql.append(" where a.tag='950' and a.nr_richiesta=").append(numRichiestaImport);
			} else {
				// elaborazioni successive 950 (stessa richiesta)
				// estrapola solo le 950 con errori di elaborazione
				//almaviva5_20120830 da RG
				//almaviva5_20130620 da RG: aggiunto check nr_richiesta su import_id_link
				sql.append("select  ");
				sql.append("CASE when c.id_inserito isnull then a.id_input else c.id_inserito END,  ");
				sql.append("a.leader, a.indicatore1, a.indicatore2, a.dati,  ");
				sql.append("CASE when c.id_inserito isnull then a.stato_id_input else 'P' END, ");
				sql.append("a.id ");
				sql.append("from import1 as a  ");
				sql.append("left outer join import950 as b on a.id = b.id_import1 ");
				sql.append("left outer join import_id_link as c on c.id_input=a.id_input and c.nr_richiesta=").append(numRichiestaImport);
				sql.append(" where a.tag='950' and a.nr_richiesta=").append(numRichiestaImport).append(' ');
				sql.append("and ( b.nr_richiesta ISNULL OR ");
				sql.append("b.nr_richiesta=(select max(nr_richiesta) from import950 where nr_richiesta_import=").append(numRichiestaImport).append(") ) ");
				sql.append("and ( b.error isnull OR b.error in (1, 2))");
			}
			String bid;
			String leader;
			char ind1;
			char ind2;
			String dati;
			char statoId;
			long id;

			_log.debug("*********** Trattamento dati del documento fisico (unimarc 950) ***********");
			_log.debug("");

			// --- LISTA DATI DOCUMENTO FISICO (uni.950)
			Marc950 _950;
			list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size < 0)
				throw new Exception("Nessuna etichetta 950 presente con nr. richiesta: " + numRichiestaImport);
			sql = null;

			Stato950 error = Stato950.OK;
			String msgError = null;
			InventarioVO inventarioVO;
			SerieVO serieVO;
			SezioneCollocazioneVO sezioneVO;
			CollocazioneVO collocazioneVO = new CollocazioneVO();
			EsemplareDettaglioVO esemplareVO = new EsemplareDettaglioVO();
			List<?> invCreati;

			boolean serieEsistente;
			boolean invEsistente;
			boolean sezEsistente;
			boolean collEsistente;
			boolean insEsemplare;

			Stato950 stato950 = Stato950.OK; 		//default
			Stato950 statoInventario = Stato950.OK; 	//default
			boolean primoInv = false;
			int keyLoc;
			List<Integer> listaKeyLoc = null;

			String consEse;
			String consEsePrec;
			int codDocEse;
			int numColl950Inserite;
			String codBib950 = null;
			Character condiviso950;

			Set<String> biblioteche = new HashSet<String>();
			Set<String> serieInventariali = new HashSet<String>();
			Set<String> sezioni = new HashSet<String>();

			for (int i = 0; i < size; i++) {
				// cicla su ogni 950
				stato950 = Stato950.OK; 		//default
				statoInventario = Stato950.OK; 	//default
				error = Stato950.OK;			//default
				numColl950Inserite = 0;		//per ogni 950 riparte il conteggio delle collocazioni inserite (serve a procedura di localizzazione)

				consEse = null;
				consEsePrec = null;
				codDocEse = 0;
				int totRecords = 0;

				record = (Object[]) list.get(i);
				bid = (String)record[0];					//id_input
				leader = (String)record[1];					//leader
				ind1 = getCharValue(record[2]);				//indicatore1
				ind2 = getCharValue(record[3]);				//indicatore2
				dati = (String)record[4];					//dati
				statoId = getCharValue(record[5]);			//stato_id_input
				id = ((Number)record[6]).longValue();

				try {
					//_log.debug("");
					//_log.debug("["+bid+"]");
					if ((++totRecords % 50) == 0)
						BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
					if ((totRecords % 1000) == 0)
						_log.debug("Record elaborati: " + totRecords);

					// controlla stato bid
					if (StatoRecord.of(statoId) != StatoRecord.PRESENTE) {
						// bid non presente
						error = Stato950.NON_TROVATO;
						throw new Exception("Documento non presente in base dati: " + bid);
					}

					_950 = new Marc950(bid, dati);

					int numeroElementiColl = _950.getNumeroElementi();
					for (int currColl = 0; currColl < numeroElementiColl; currColl++) {
						// cicla sugli elementi della 950 corrente
						listaKeyLoc = new ArrayList<Integer>();
						insEsemplare = false;

						String codSez950 = _950.getCodSez(currColl);

						int numeroElementiInv = ValidazioneDati.size(_950.getInventario().get(currColl));
//						_log.debug("TOT. INV   : " + invSize);

						for (int currInv = 0; currInv < numeroElementiInv; currInv++) {
							// cicla sugli inventari dell'elemento corrente
							//_log.debug("   seq.coll.: " + _950.getSequenzaCollocazione(k, j));

							serieEsistente = false;
							invEsistente = false;
							sezEsistente = false;
							collEsistente = false;
							primoInv = (currInv == 0);

							// inizializzazione
							inventarioVO = new InventarioVO();
							error = Stato950.OK;				// default - nessun errore
							codBib950 = _950.getCodBiblioteca(currColl, currInv);

							//almaviva5_20140905 check esistenza biblioteca
							if (!biblioteche.contains(codBib950))
								try {
									BibliotecaVO bib = getBiblioteca().getBiblioteca(codPolo, codBib950);
									if (bib == null) {
										error = Stato950.ERRORE;
										_log.error(String.format("biblioteca non definita in polo: '%s%s'", codPolo, codBib950));
										numInvInsNO++;
									} else
										biblioteche.add(codBib950);
								} catch (Exception e) {
									error = Stato950.ERRORE;
									_log.error(String.format("Errore biblioteca in polo: '%s%s' --> ", codPolo, codBib950), e);
									numInvInsNO++;
								}

							// serie inventariale / inventario
							// controllo di esistenza e inserimento

							// serie inventariale
							String codSerie950 = _950.getCodSerie(currColl, currInv);
							//_log.debug(String.format("  serie_inv: '%s'", codSerie950));
							if (error == Stato950.OK) {
								if (serieInventariali.contains(codSerie950)) {
									serieEsistente = true;
									numSerieInvDupl++;
								} else try {
									serieVO = getInventario().getSerieDettaglio(
											codPolo,
											codBib950,
											codSerie950,
											ticket);

									serieEsistente = true;
									serieInventariali.add(codSerie950);
									_log.debug("Serie inventariale (" + serieVO.getCodSerie() + ") già presente");
									numSerieInvDupl++;

								} catch (Exception e) {
									// serie inesistente
									// inserimento serie

									// campi obbligatori
									serieVO = new SerieVO();
									serieVO.setCodPolo(codPolo);
									serieVO.setCodBib(codBib950);
									serieVO.setCodSerie(codSerie950);
									serieVO.setDescrSerie("");
	//								serieVO.setProgAssInv("0");
									serieVO.setProgAssInv("900000000");
									serieVO.setPregrAssInv("900000000");
									serieVO.setNumMan("0");
									serieVO.setDataIngrPregr("");
									serieVO.setDataIngrMan("");
									serieVO.setInizioMan1("0");
									serieVO.setFineMan1("0");
									serieVO.setDataIngrRis1("");
									serieVO.setInizioMan2("0");
									serieVO.setFineMan2("0");
									serieVO.setDataIngrRis2("");
									serieVO.setInizioMan3("0");
									serieVO.setFineMan3("0");
									serieVO.setDataIngrRis3("");
									serieVO.setInizioMan4("0");
									serieVO.setFineMan4("0");
									serieVO.setDataIngrRis4("");
									serieVO.setBuonoCarico("");
									serieVO.setFlChiusa("0");
									serieVO.setFlDefault("0");
									serieVO.setUteIns(firmaUtente);
									serieVO.setUteAgg(firmaUtente);

									try {
										if (!getInventario().insertSerie(serieVO, ticket))
											throw new Exception();

										_log.debug("Serie inventariale (" + serieVO.getCodSerie() + ") inserita");
										serieInventariali.add(codSerie950);
										numSerieInvInsOK++;
									} catch (Exception e2) {
										error = Stato950.ERRORE;
										_log.error("[Serie inventariale] errore inserimento --> ", e2);
										numSerieInvInsNO++;
									}
								}
							}

							// inventario
							if (error == Stato950.OK) {
								try {
									//_log.debug("   num_inven: " + _950.getCodInventario(k, j));
									inventarioVO = getInventario().getInventario(
											codPolo,
											codBib950,
											codSerie950,
											_950.getCodInventario(currColl, currInv),
											Locale.getDefault(),
											ticket);

									invEsistente = true;
									// inventario già esistente - non prosegue nell'elaborazione
									serieEsistente = true;
									sezEsistente = true;
									collEsistente = true;
//									if (serieEsistente && invEsistente) {
//										sezEsistente = true;
//										collEsistente = true;
//									}
									_log.debug(String.format("inventario: %-3s%09d già presente", inventarioVO.getCodSerie(), inventarioVO.getCodInvent()));
									numInvDupl++;

								} catch (Exception e) {
									// inventario inesistente
									// inserimento inventario

									// campi obbligatori inventarioVO
									inventarioVO.setKeyLoc(0);
									inventarioVO.setNumInv(1);
									inventarioVO.setBid(bid);
									inventarioVO.setCodPolo(codPolo);
									inventarioVO.setCodBib(codBib950);
									inventarioVO.setCodSerie(codSerie950);
									inventarioVO.setCodInvent(_950.getCodInventario(currColl, currInv));
									inventarioVO.setSeqColl(_950.getSequenzaCollocazione(currColl, currInv));
									inventarioVO.setPrecInv(_950.getPrecisazioneInventario(currColl, currInv));
									inventarioVO.setDataIngresso(DateUtil.formattaData(_950.getDataIngresso(currColl, currInv)));
									inventarioVO.setUteIns(firmaUtente);
									inventarioVO.setUteAgg(firmaUtente);

									try {
										String nuovoInv = String.format("bid: %-10s; bib: '%-3s'; sez: '%-10s'; coll: '%-24s'; spec: '%-12s'; serie: '%-3s'; num: %09d",
												bid, codBib950, codSez950, _950.getCodLoc(currColl), _950.getSpecLoc(currColl), codSerie950, _950.getCodInventario(currColl, currInv));
										_log.debug("inventario: " + nuovoInv);

										invCreati = getInventario().insertInventario(
												inventarioVO, "N",
												1, //nInv 1: inserisce inventario
												Locale.getDefault(), ticket);
										if (!isFilled(invCreati) )  {
											throw new Exception();
										}

										//_log.debug("Inventario (" + inventarioVO.getCodInvent() + ") inserito");
										numInvInsOK += ValidazioneDati.size(invCreati);

										// ricatturo l'inventario con i valori eventuali di default
										inventarioVO = getInventario().getInventario(
												inventarioVO.getCodPolo(),
												inventarioVO.getCodBib(),
												inventarioVO.getCodSerie(),
												inventarioVO.getCodInvent(),
												Locale.getDefault(), ticket);
										
										boolean inventarioDaCollocare = _950.isCollocato(currColl, currInv);

										if (!inventarioDaCollocare) {
											//inventario precisato, si continua con il prossimo
											continue;
										}

										if (primoInv) {
											// dopo il trattamento del primo inventario gestisce eventuali dati di collocazione

											// sezione di collocazione / collocazione
											// controllo di esistenza e inserimento

											if (error == Stato950.OK) {
												// collocato
												if (sezioni.contains(codSez950)) {
													sezEsistente = true;
													numSezCollDupl++;
												} else try {
														sezioneVO = getCollocazione().getSezioneDettaglio(
																codPolo,
																codBib950,
																codSez950,
																ticket);

														sezEsistente = true;
														//_log.debug("Sezione di collocazione (" + sezioneVO.getCodSezione() + ") già presente");
														sezioni.add(codSez950);
														numSezCollDupl++;

													} catch (Exception e1) {
														// sezione inesistente
														// inserimento sezione di collocazione

														// campi obbligatori
														sezioneVO = new SezioneCollocazioneVO();
														sezioneVO.setCodPolo(codPolo);
														sezioneVO.setCodBib(codBib950);
														sezioneVO.setCodSezione(codSez950.trim());
														sezioneVO.setTipoColloc(DocumentoFisicoCostant.COD_ESPLICITA_NON_STRUTTURATA);
														sezioneVO.setTipoSezione(DocumentoFisicoCostant.COD_MAGAZZINO);
														sezioneVO.setDescrTipoColl("");
														sezioneVO.setDescrSezione("");
														sezioneVO.setNoteSezione("");
														sezioneVO.setClassific("");
														sezioneVO.setUteIns(firmaUtente);
														sezioneVO.setUteAgg(firmaUtente);
	//													sezioneVO.setProgNum();

														try {
															if (!getCollocazione().insertSezione(sezioneVO, ticket)) {
																throw new Exception();
															}
															_log.debug("Sezione di collocazione (" + sezioneVO.getCodSezione() + ") inserita");
															sezioni.add(codSez950);
															numSezCollInsOK++;
														} catch (Exception e2) {
															error = Stato950.ERRORE;
															_log.error("[Sezione di collocazione] errore inserimento --> ", e2);
															numSezCollInsNO++;
														}
													}

												// collocazione
												if (error == Stato950.OK) {

													try {
														collocazioneVO = getCollocazione().getCollocazione(inventarioVO.getKeyLoc(), ticket);
														collEsistente = true;
														//_log.debug("Collocazione (" + collocazioneVO.getCodColloc()	+ ") già presente");
														numCollDupl++;

													} catch (Exception e1) {
														// collocazione inesistente
														// preparazione all'inserimento collocazione

														// campi obbligatori
														collocazioneVO = new CollocazioneVO();
														collocazioneVO.setKeyColloc(0);
														collocazioneVO.setCodPoloSez(codPolo);
														collocazioneVO.setCodBibSez(codBib950);
														collocazioneVO.setCodPolo(codPolo);
														collocazioneVO.setCodBib(codBib950);
														collocazioneVO.setCodSez(codSez950);
														collocazioneVO.setConsistenza(_950.getConsistenzaCollocazione().get(currColl));
														collocazioneVO.setUteIns(firmaUtente);
														collocazioneVO.setUteAgg(firmaUtente);

														collocazioneVO.setBid(_950.getId());
														String coll = _950.getCodLoc(currColl);
														String spec = _950.getSpecLoc(currColl);
														collocazioneVO.setCodColloc(coll);
														collocazioneVO.setSpecColloc(spec);
														collocazioneVO.setOrdLoc(ValidazioneDati.trimOrEmpty(OrdinamentoCollocazione2.normalizza(coll)));
														collocazioneVO.setOrdSpec(ValidazioneDati.trimOrEmpty(OrdinamentoCollocazione2.normalizza(spec)));
													}
												}

											}

										}

										// default per validazione
										inventarioVO.setValore("0.001");
										//almaviva5_20190928 recupero cod. mat. da sotto-campo $e, se non presente si usa il liv. bibliografico
										String codMatInv = _950.getCodMatInv(currColl, currInv);
										if (isFilled(codMatInv)) {
											inventarioVO.setCodMatInv(codMatInv);
										} else {
											String natura = leader.substring(7,8).toUpperCase();
											inventarioVO.setCodMatInv(natura.equals("S") ? "VP" : "VM");
										}
										inventarioVO.setTipoAcquisizione("A");

										inventarioVO.setCodFrui(_950.getCodFruizione(currColl, currInv));
										inventarioVO.setDataCarico("");
										// bypasso il trattamento della fattura (InventarioBMT.java)
										inventarioVO.setNumFattura("");

										try {
											// ins coll
											keyLoc = getInventario().updateInvColl(inventarioVO, collocazioneVO, "M_INV", ticket);
											if (keyLoc < 0) {
												// errore
												throw new Exception("keyLoc < 0");
											}
/*
											if (collocazioneVO.getKeyColloc() == 0)
												_log.debug("Collocazione ("+collocazioneVO.getCodColloc()+") inserita");
											else
												_log.debug("Collocazione ("+collocazioneVO.getCodColloc()+") aggiornata");
*/
											numCollInsOK++;
											numColl950Inserite++;

											// ricatturo la collocazione (per valore di keyColloc)
//											collocazioneVO = getCollocazione().getCollocazione(inventarioVO.getKeyLoc(), ticket);
											collocazioneVO = getCollocazione().getCollocazione(keyLoc, ticket);
											// lista cod collocazioni per gestione esemplare
											listaKeyLoc.add(collocazioneVO.getKeyColloc());
										} catch (Exception e2) {
											error = Stato950.ERRORE;
											if (collocazioneVO.getKeyColloc() == 0)
												_log.error("[Collocazione] errore inserimento --> ", e2);
											else
												_log.error("[Collocazione] errore aggiornamento --> ", e2);
											numCollInsNO++;
										}

									} catch (Exception e2) {
										error = Stato950.ERRORE;
										_log.error("[Inventario] errore inserimento --> ", e2);
										numInvInsNO++;
									}
								}
							}

							if ( serieEsistente && invEsistente && sezEsistente && collEsistente ) {
								// dati già presenti in base dati
								// nessuna operazione da eseguire
								error = Stato950.DUPLICATO;
							}/* else {
								// almeno un dato non presente
								if (error== 0) {
									// nessun errore
									// almeno 1 inserimento eseguito
									error = 2; // parziale
								}
							}*/

							// gestione dei messaggi di errore (DB)
							// inventario ed eventuali dati di collocazione ed esemplare correnti
							switch (error) {
								case OK: // ok
									if (statoInventario==Stato950.ERRORE || statoInventario==Stato950.PARZIALE /*|| statoInventario==_950_duplicata*/) {
										statoInventario = Stato950.PARZIALE;
									} else statoInventario = Stato950.OK;
									break;
								case ERRORE: //errore
									if (statoInventario==Stato950.OK || statoInventario==Stato950.PARZIALE /*|| statoInventario==_950_duplicata*/) {
										statoInventario = Stato950.PARZIALE;
									} else statoInventario = Stato950.ERRORE;
									break;
								case PARZIALE: // elaborazione parziale
									statoInventario = Stato950.PARZIALE;
									break;
								case DUPLICATO: // dati già esistenti
									if (currInv == 0) {
										statoInventario = Stato950.DUPLICATO;
									}
									break;
								case NON_TROVATO:
									statoInventario = Stato950.NON_TROVATO;
									break;
								default:
									break;
							}

						} // fine del ciclo su ogni inventario

						// gestione della consistenza dell'esemplare legata ai dati di collocazione e inventario
						// consistenza di esemplare
						// controllo di esistenza e inserimento
						if (isFilled(listaKeyLoc) ) {
							// inserimento

							if (esemplareVO != null && isFilled(_950.getConsistenzaDocumento(currColl))) {

								// campi obbligatori
								esemplareVO = new EsemplareDettaglioVO();
								esemplareVO.setCodPolo(codPolo);
								esemplareVO.setCodBib(_950.getCodBibColl(currColl));
								esemplareVO.setBid(bid);
								esemplareVO.setConsDoc(_950.getConsistenzaDocumento(currColl));
								esemplareVO.setUteAgg(firmaUtente);

								// codDoc
								codDocEse = (esemplareVO.getConsDoc().equals(consEsePrec))
									? codDocEse	// stesso esemplare: conserva il codDoc precedente
									: 0;		// nuovo esemplare: codDoc di default (per superare la validazione)
								esemplareVO.setCodDoc(codDocEse);
								consEsePrec = esemplareVO.getConsDoc();

								try {
									if (!getCollocazione().insertEsemplare(esemplareVO, listaKeyLoc, ticket)) {
										throw new Exception();
									}

									insEsemplare = true;
									if (codDocEse== 0) {
										_log.debug("Esemplare ("+esemplareVO.getConsDoc()+") inserito con codDoc="+esemplareVO.getCodDoc());
										numEseInsOK++;
									} else {
										_log.debug("Esemplare ("+esemplareVO.getConsDoc()+") aggiornato");
									}

									// conserva codDoc dell'esemplare appena inserito
									codDocEse = esemplareVO.getCodDoc();

								} catch (Exception e3) {
									insEsemplare = false;
									_log.error("[Consistenza di esemplare] errore inserimento --> ", e3);
									numEseInsNO++;
								}

								// gestione dei messaggi di errore (DB)
								// esemplare
								if (insEsemplare) { 	//ok
									if (statoInventario==Stato950.ERRORE || statoInventario==Stato950.PARZIALE /*|| statoInventario==_950_duplicata*/) {
										statoInventario = Stato950.PARZIALE;
									} else statoInventario = Stato950.OK;
								} else {			//errore
									if (statoInventario==Stato950.OK || statoInventario==Stato950.PARZIALE /*|| statoInventario==_950_duplicata*/) {
										statoInventario = Stato950.PARZIALE;
									} else statoInventario = Stato950.ERRORE;
								}
							}
						}

						// gestione dei messaggi di errore (DB)
						// elemento corrente
						if (statoInventario==Stato950.ERRORE) {
							if (stato950==Stato950.OK || stato950==Stato950.PARZIALE /*|| stato950==_950_duplicata*/) {
								stato950 = Stato950.PARZIALE;
							} else stato950 = Stato950.ERRORE;
						} else if (statoInventario==Stato950.PARZIALE) {
							stato950 = Stato950.PARZIALE;
						} else if (statoInventario==Stato950.DUPLICATO) {
							if (currColl == 0) stato950 = Stato950.DUPLICATO;
						} else if (statoInventario==Stato950.OK) {
							if (stato950==Stato950.ERRORE || stato950==Stato950.PARZIALE /*|| stato950==_950_duplicata*/) {
								stato950 = Stato950.PARZIALE;
							} else stato950 = Stato950.OK;
						}

					} // fine ciclo su ogni elemento

				} catch (Exception e) {
					// sql trattamento 950
					// preparazione query per tabella di riepilogo
					if (error==Stato950.NON_TROVATO) {
						// 4 => record inesistente
						stato950 = Stato950.ERRORE;
						msgError = "ERR - Elaborazione non avvenuta [" + escapeSql(e.getMessage()) + "] ";
						_log.error(e.getMessage());
					} else {
						// errore specifico in caso di elaborazione parziale
						if (stato950==Stato950.OK || stato950==Stato950.PARZIALE /*|| stato950==_950_duplicata*/) {
							stato950 = Stato950.PARZIALE;
						} else stato950 = Stato950.ERRORE;
					}
				}

				if (stato950!=Stato950.OK) {
					if (error!=Stato950.NON_TROVATO) {
						msgError = (stato950==Stato950.PARZIALE) ? "WARN - Elaborazione parziale " : "ERR - Elaborazione non avvenuta ";
						if (stato950==Stato950.DUPLICATO) msgError = "OK - Inventari già presenti";
						else if (stato950==Stato950.ERRORE) msgError += "[Errori durante il caricamento]";
					}
				} else
					msgError = "OK - Elaborazione avvenuta correttamente";

				// sql trattamento 950
				// preparazione query per tabella di riepilogo
				String sql950 = ("insert into import950 " +
						"(id_import1, id_input, dati, error, msg_error, nr_richiesta_import, nr_richiesta) values " +
						"("+id+", '"+bid+"', :param, "+stato950.ordinal()+", '"+escapeSql(msgError)+"', "+numRichiestaImport+", "+numRichiestaCorrente+")");

				executeInsertUpdate(ImportQueryData.asList(sql950, dati));

				// -- LOCALIZZAZIONI
				// Localizza per possesso (in Polo e/o Indice) per ogni 950 che
				// ha portato all'inserimento di almeno una collocazione
				if (numColl950Inserite > 0) {
					try {
						AreaDatiLocalizzazioniAuthorityMultiplaVO adlam = new AreaDatiLocalizzazioniAuthorityMultiplaVO();
						AreaDatiVariazioneReturnVO advr = null;
						DatiBibliograficiCollocazioneVO reticolo = getCollocazione().getAnaliticaPerCollocazione(bid, codPolo + codBib950, ticket);
						CollocazioneTitoloVO[] titoliCollocabiliReticolo = reticolo.getListaTitoliLocalizzazione();
						CollocazioneTitoloVO titoloCollocabile = null;
						TreeElementViewTitoli titoloLista = null;
						for (int h = 0; h < titoliCollocabiliReticolo.length; h++) {
							titoloCollocabile = titoliCollocabiliReticolo[h];
							titoloLista = (TreeElementViewTitoli)reticolo.getAnalitica().findElement(titoloCollocabile.getBid());
							condiviso950 = getFlagCondivisioneTitolo(bid);
							AreaDatiLocalizzazioniAuthorityVO al = new AreaDatiLocalizzazioniAuthorityVO();
							al.setPolo(true);
							if (condiviso950 != null) {
								if (Character.toString(condiviso950.charValue()).equalsIgnoreCase("N")) {
									// bid non condiviso
									// localizza solo in polo
									al.setIndice(false);
								} else {
									// bid condiviso
									// localizza anche in indice
									al.setIndice(true);
								}
							}

							al.setIdLoc(bid);
							al.setTipoLoc("Possesso");
							al.setTipoOpe("Localizza");
							al.setNatura(titoloCollocabile.getNatura());
							al.setAuthority("");
							al.setTipoMat(titoloLista.getTipoMateriale());
							al.setCodiceSbn(codPolo + codBib950); // biblioteca operante
							adlam.addListaAreaLocalizVO(al);
						} // fine ciclo su elementi reticolo

						try {
							//almaviva5_20140626 with LV
							//la chiamata a localizzazione multipla va effettuata UNA SOLA VOLTA alla fine del ciclo.
							if (ValidazioneDati.isFilled(adlam.getListaAreaLocalizVO()) )
								advr = getInterrogazione().localizzaAuthorityMultipla(adlam, ticket);
							// gestione errori
		 					if (advr != null && (advr.getCodErr().equals("9999") || advr.getTestoProtocollo() != null)) {
								_log.debug("Localizzazione "+bid+" [elemento "+titoloCollocabile.getBid()+"] non riuscita --> " + advr.getTestoProtocollo());
		 					} else if (advr != null && (!advr.getCodErr().equals("0000"))) {
		 						if (advr.getCodErr().equals("noServerInd")) {
		 							_log.debug("Localizzazione "+bid+" [elemento "+titoloCollocabile.getBid()+"] non riuscita --> Il server di Indice non è attualmente disponibile");
		 						} else if (advr.getCodErr().equals("noServerPol")) {
		 							_log.debug("Localizzazione "+bid+" [elemento "+titoloCollocabile.getBid()+"] non riuscita --> Il server Locale non è attualmente disponibile");
		 						} else if (!advr.getCodErr().equals("7017")) {
		 							_log.debug("Localizzazione "+bid+" [elemento "+titoloCollocabile.getBid()+"] non riuscita non riuscita");
		 						}
		 					}
						} catch (Exception e) {
 							_log.error("Localizzazione "+bid+" [elemento "+titoloCollocabile.getBid()+"] non riuscita non riuscita");
						}

					} catch (Exception e) {
						_log.error("Localizzazione ["+bid+"] non riuscita --> " + e.getMessage());
					}

				} // fine localizza

			} // fine ciclo su ogni 950

		} catch (Exception e) {
			_log.error("[Errore] ", e);
			return null;
		}

		_log.debug("");
		_log.debug("[STATISTICHE DI TRATTAMENTO 950]");
		_log.debug("");
		_log.debug("- SERIE INVENTARIALI");
		_log.debug("  . inserimenti : " + numSerieInvInsOK);
		//_log.debug("  . già presenti: " + numSerieInvDupl);
		_log.debug("  . errori      : " + numSerieInvInsNO);
		_log.debug("- INVENTARI");
		_log.debug("  . inserimenti : " + numInvInsOK);
		_log.debug("  . già presenti: " + numInvDupl);
		_log.debug("  . errori      : " + numInvInsNO);
		_log.debug("- SEZIONI DI COLLOCAZIONE");
		_log.debug("  . inserimenti : " + numSezCollInsOK);
		//_log.debug("  . già presenti: " + numSezCollDupl);
		_log.debug("  . errori      : " + numSezCollInsNO);
		_log.debug("- COLLOCAZIONI");
		_log.debug("  . inserimenti : " + numCollInsOK);
		_log.debug("  . già presenti: " + numCollDupl);
		_log.debug("  . errori      : " + numCollInsNO);
		_log.debug("- ESEMPLARI");
		_log.debug("  . inserimenti : " + numEseInsOK);
//		_log.debug("  . già presenti: " + num);
		_log.debug("  . errori      : " + numEseInsNO);

		writeReportRow(streamOut, "- SERIE INVENTARIALI");
		writeReportRow(streamOut, "  . inserimenti : " + numSerieInvInsOK);
		writeReportRow(streamOut, "  . errori      : " + numSerieInvInsNO);
		writeReportRow(streamOut, "- INVENTARI");
		writeReportRow(streamOut, "  . inserimenti : " + numInvInsOK);
		writeReportRow(streamOut, "  . già presenti: " + numInvDupl);
		writeReportRow(streamOut, "  . errori      : " + numInvInsNO);
		writeReportRow(streamOut, "- SEZIONI DI COLLOCAZIONE");
		writeReportRow(streamOut, "  . inserimenti : " + numSezCollInsOK);
		writeReportRow(streamOut, "  . errori      : " + numSezCollInsNO);
		writeReportRow(streamOut, "- COLLOCAZIONI");
		writeReportRow(streamOut, "  . inserimenti : " + numCollInsOK);
		writeReportRow(streamOut, "  . già presenti: " + numCollDupl);
		writeReportRow(streamOut, "  . errori      : " + numCollInsNO);
		writeReportRow(streamOut, "- ESEMPLARI");
		writeReportRow(streamOut, "  . inserimenti : " + numEseInsOK);
		writeReportRow(streamOut, "  . errori      : " + numEseInsNO);

		_log.debug("");
		StringBuilder strBuild = new StringBuilder(1024);
		Object[] record;
		// - TIPI DI ERRORE
		strBuild.append("select  error  as tipo_di_errore, ");
		strBuild.append(" case ");
		strBuild.append(" when error = '0' then 'Elaborazione correttamente eseguita' ");
		strBuild.append(" when error = '1' then 'Record non elaborati per Documento inesistente in base dati' ");
		strBuild.append(" when error = '2' then 'Elaborazione parziale ' ");
		strBuild.append(" when error = '3' then 'Record non elaborati per Inventari già presenti' ");
		strBuild.append(" else ");
		strBuild.append(" 'Errore non previsto' ");
		strBuild.append(" end, ");
		strBuild.append(" count(*) as occorrenze from import950   ");
		strBuild.append(" where  nr_richiesta         = "+numRichiestaCorrente+" ");
		strBuild.append(" and    nr_richiesta_import  = "+numRichiestaImport+" ");
		strBuild.append(" group  by 1 ");
		try {
			List<?> list = getRecords(strBuild);
			for (int i=0; i<list.size(); i++) {
				record = (Object[]) list.get(i);
				// [tipo errore n] motivo: nr.occorrenze
				_log.debug("[tipo errore " + ((Number)record[0]).intValue() + "] " + (String)record[1] + ": " + ((Number)record[2]).longValue() + " record");
			}
		} catch (Exception e) {	}

		return datiVO;
	}

	public int exec_verificaBid(boolean ricercaPolo, String numRichiesta, List<String> listaBid) {
		try {
			String id_input;
			String id_sbn;
			String sql;
			char stato_new;
			char stato_old;
			List<?> list;
			Object[] record;
			int nrAgg = 0;
			int nrAggNewBid = 0;
			int nrInv = 0;
			StringBuilder strBuild = new StringBuilder(1024);

			_log.debug("[VERIFICA la PRESENZA dei DOCUMENTI]");
			_log.debug(" * Legenda: P=Presente; I=Da caricare/inserire; C=Da catturare; N=Non definito");
			_log.debug("");

			// --- CERCA e AGGIORNA i BID dei documenti che nel frattempo potrebbero essere stati inseriti
			// lista bid dei documenti inseriti
			strBuild.append("select distinct i1.id_input, i1.stato_id_input, i2.id_inserito from import1 i1 ");
			strBuild.append("inner join import_id_link i2 on i1.nr_richiesta=i2.nr_richiesta and i1.id_input=i2.id_input ");
			strBuild.append("where i1.stato_id_input<>'P' and i1.nr_richiesta=").append(numRichiesta).append(" and i1.tag='001'");
			list = getRecords(strBuild);
			for (int i = 0; i < list.size(); i++) {
				record = (Object[]) list.get(i);
				id_input = (String) record[0];
				stato_old = getCharValue(record[1]);
				id_sbn = (String) record[2];

				// aggiorna lo stato e l'eventuale nuovo bid dei documenti inseriti
				sql = "update import1 SET id_input='" + escapeSql(id_sbn) + "', stato_id_input='P' ";
				sql += "where id_input='" + escapeSql(id_input) + "' and nr_richiesta=" + numRichiesta;
				executeInsertUpdate(ImportQueryData.asList(sql, null));

				if (id_input.equals(id_sbn))
					_log.debug(id_input + ": aggiornato da " + stato_old + " a P");
				else {
					_log.debug(id_input + ": aggiornato da " + stato_old + " a P (nuovo BID assegnato: "+id_sbn+")");
					nrAggNewBid++;
				}
				nrAgg++;
			}

			// --- LISTA BID precedentemente mappati come non presenti
			strBuild.setLength(0);
			if (ricercaPolo) {
				// cerca in polo documenti mappati come NON PRESENTI (I, C, N)
				_log.debug("--> in POLO");
				sql = "select distinct id_input, stato_id_input from import1 where stato_id_input<>'P' and nr_richiesta=" + numRichiesta;
			} else {
				// cerca in indice documenti mappati come DA INSERIRE
				_log.debug("--> in INDICE");
				sql = "select distinct id_input, stato_id_input from import1 where stato_id_input='I' and nr_richiesta=" + numRichiesta;
			}
			_log.debug("");

			strBuild.append(sql);
			list = getRecords(strBuild);

			if (ricercaPolo) {
				// cerca in polo
				for (int i = 0; i < list.size(); i++) {
					record = (Object[]) list.get(i);
					id_input = (String)record[0];
					stato_old = getCharValue(record[1]);
					try {
						// restituisce P o I
						stato_new = cercaTitoloPolo(null, id_input, false).getStato();
					} catch (Exception e) {
						stato_new = stato_old;
						_log.error("[Errore] Impossibile aggiornare lo stato del documento " + id_input + ". Errore durante la ricerca in Polo: ", e);
					}
					// aggiorna stato
					sql = "update import1 SET stato_id_input='" + stato_new + "' ";
					sql += "where id_input='" + escapeSql(id_input) + "' and nr_richiesta=" + numRichiesta;
					executeInsertUpdate(ImportQueryData.asList(sql, null));
					if (stato_new == stato_old ||									// nuovo stato != vecchio stato
						StatoRecord.of(stato_new) != StatoRecord.PRESENTE &&	// nuovo stato != P
						StatoRecord.of(stato_old) != StatoRecord.DA_INSERIRE	// vecchio stato != I
					) {
						_log.debug(id_input + ": invariato " + StatoRecord.of(stato_old));
						nrInv++;
					} else {
						_log.debug(id_input + ": aggiornato da " + StatoRecord.of(stato_old) + " a " + StatoRecord.of(stato_new));
						nrAgg++;
					}
				}
			} else {
				// cerca in indice
				InterrogazioneTitoloGeneraleVO interrTitGen = new InterrogazioneTitoloGeneraleVO();
				AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPassInterrTitVO = new AreaDatiPassaggioInterrogazioneTitoloVO();

				// default stringhe vuote
				interrTitGen.setTitolo("");
				interrTitGen.setTipoMateriale("*");
				interrTitGen.setNaturaSelez1("");
				interrTitGen.setNaturaSelez2("");
				interrTitGen.setNaturaSelez3("");
				interrTitGen.setNaturaSelez4("");
				interrTitGen.setTipiRecordSelez("");
				interrTitGen.setPaeseSelez("");
				interrTitGen.setLinguaSelez("");
				interrTitGen.setTipoDataSelez("");
				interrTitGen.setData1A("");
				interrTitGen.setData1Da("");
				interrTitGen.setData2A("");
				interrTitGen.setData2Da("");
				interrTitGen.setNomeCollegato("");
				interrTitGen.setLuogoPubbl("");
				interrTitGen.setResponsabilitaSelez("");
				interrTitGen.setRelazioniSelez("");
				interrTitGen.setSottoNaturaDSelez("");
				interrTitGen.setNumStandardSelez("");
				interrTitGen.setNumStandard1("");
				interrTitGen.setGenereSelez1("");
				interrTitGen.setGenereSelez2("");
				interrTitGen.setGenereSelez3("");
				interrTitGen.setGenereSelez4("");
				interrTitGen.setImpronta1("");
				interrTitGen.setImpronta2("");
				interrTitGen.setImpronta3("");
//				interrTitGen.setRicIndice(true);
//				interrTitGen.setRicLocale(false);
				areaDatiPassInterrTitVO.setRicercaPolo(false);
				areaDatiPassInterrTitVO.setRicercaIndice(true);
//				areaDatiPassInterrTitVO.setTipoOggetto(99);
//				areaDatiPassInterrTitVO.setTipoOggettoFiltrato(99);

				for (int i = 0; i < list.size(); i++) {
					record = (Object[]) list.get(i);
					id_input = (String)record[0];
					stato_old = getCharValue(record[1]);
					interrTitGen.setBid(id_input);
					areaDatiPassInterrTitVO.setInterTitGen(interrTitGen);
					try {
						stato_new = cercaTitoloIndice(areaDatiPassInterrTitVO, null).getStato();
						if (StatoRecord.of(stato_new) == StatoRecord.DA_CATTURARE) {
							// aggiunge a lista bid da catturare
							listaBid.add(id_input);
						}
					} catch (Exception e) {
						stato_new = stato_old;
						_log.error("[Errore] Impossibile aggiornare lo stato del documento " + id_input + ". Errore durante la ricerca in Indice: ", e);
					}
					// aggiorna stato
					sql = "update import1 SET stato_id_input='"+stato_new+"' ";
					sql += "where id_input='" + escapeSql(id_input) + "' and nr_richiesta="+numRichiesta;
					executeInsertUpdate(ImportQueryData.asList(sql, null));
					if (stato_new == stato_old) {
						_log.debug(id_input + ": invariato " + StatoRecord.of(stato_old));
						nrInv++;
					} else {
						_log.debug(id_input + ": aggiornato da " + StatoRecord.of(stato_old) + " a " + StatoRecord.of(stato_new));
						nrAgg++;
					}
				}
			}
			nrInv = list.size() - nrAgg;
			_log.debug("");
			_log.debug("*****************");
			_log.debug("- nr documenti : " + list.size());
			_log.debug("-    invariati : " + nrInv);
			_log.debug("-    aggiornati: " + nrAgg);
			_log.debug("-                di cui " + nrAggNewBid + " nuovi BID assegnati");
			_log.debug("*****************");
			_log.debug("");
			list = null;

		} catch (Exception e) {
			_log.error("[Exception] " + e.getMessage(), e);
			return 1;
		}

		return 0;
	}

	public int execFase3() {
		try {
			Object[] record;

			// autori (uni.7xx)
			_log.debug("---------- AUTORI 7xx ----------");
			List<?> list = getAutori();
			InterrogazioneAutoreGeneraleVO interrAutGen = new InterrogazioneAutoreGeneraleVO();
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPassInterrAutVO = new AreaDatiPassaggioInterrogazioneAutoreVO();
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = null;
			for (int i=0; i<list.size(); i++) {
				record = (Object[]) list.get(i);
				_log.debug("");
				_log.debug("***************************");
				_log.debug(record[0]); //id_link
				_log.debug(record[1]); //tag
				_log.debug(record[2]); //dati
				_log.debug(record[3]); //count

				_log.debug("Autore: " + MarcRecord.getAutoreString((String)record[2]));
				_log.debug("***************************");
				_log.debug("");

				interrAutGen.setVid(((String)record[0]).substring(0, 10)); //elimina eventuale carattere di a capo
				interrAutGen.setNome("");
				interrAutGen.setIsadn("");
				interrAutGen.setDataAnnoNascitaDa("");
				interrAutGen.setDataAnnoNascitaA("");
				interrAutGen.setDataAnnoMorteDa("");
				interrAutGen.setDataAnnoMorteA("");
				interrAutGen.setTipoRicerca("");
				interrAutGen.setTipoNomeBoolean(new boolean[0]);
				areaDatiPassInterrAutVO.setRicercaPolo(true);
				areaDatiPassInterrAutVO.setRicercaIndice(false);
//				areaDatiPassInterrAutVO.setRicercaPolo(false);
//				areaDatiPassInterrAutVO.setRicercaIndice(true);
				areaDatiPassInterrAutVO.setInterrGener(interrAutGen);
				areaDatiPassReturn = getInterrogazione().ricercaAutori(areaDatiPassInterrAutVO, ticket);
				if (findError(areaDatiPassReturn)) {

				}
//				MarcRecord.getAutoreString(record[2])
			}

			// legami (uni.4xx)
			_log.debug("---------- LEGAMI 4xx ----------");
//			list = getLegami();
//			getInterrogazione().ricercaTitoli(areaDatiPass, ticket)
			InterrogazioneTitoloGeneraleVO interrTitGen = new InterrogazioneTitoloGeneraleVO();
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPassInterrTitVO = new AreaDatiPassaggioInterrogazioneTitoloVO();
			areaDatiPassReturn = null;
			for (int i=0; i<list.size(); i++) {
				record = (Object[]) list.get(i);
				_log.debug("");
				_log.debug("***************************");
				_log.debug(record[0]); //id_link
				_log.debug(record[1]); //tag
				_log.debug(record[2]); //dati
				_log.debug(record[3]); //count

				_log.debug("Autore: " + MarcRecord.getAutoreString((String)record[2]));
				_log.debug("***************************");
				_log.debug("");

				interrTitGen.setBid(((String)record[0]).substring(0, 10)); //elimina eventuale carattere di a capo
				interrTitGen.setTitolo("");
				areaDatiPassInterrTitVO.setRicercaPolo(true);
				areaDatiPassInterrTitVO.setRicercaIndice(false);
//				areaDatiPassInterrTitVO.setRicercaPolo(false);
//				areaDatiPassInterrTitVO.setRicercaIndice(true);
				areaDatiPassInterrTitVO.setInterTitGen(interrTitGen);
				areaDatiPassReturn = getInterrogazione().ricercaTitoli(areaDatiPassInterrTitVO, ticket);
				if(findError(areaDatiPassReturn)) {

				}
//				MarcRecord.getTitoloString(record[1], record[2])
			}

			// titoli in relazione (uni.5xx)
			_log.debug("---------- TITOLI in RELAZ 5xx ----------");
//			list = getTitoliRelazione();

		} catch (Exception e) {
			_log.error("[Exception] " + e.getMessage(), e);
			return 1;
		}

		return 0;
	}

	private boolean findError(AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn) {
		if (areaDatiPassReturn == null) {
			_log.error("[ERROR] Connessione mancante");
			return true;
		}

		_log.debug("nr.notizie: " + areaDatiPassReturn.getNumNotizie());

		if (areaDatiPassReturn.getTestoProtocollo() != null) {
			_log.error("[ERROR] testoProtocollo: "+areaDatiPassReturn.getTestoProtocollo());
			return true;
		} else if (areaDatiPassReturn.getCodErr().equals("9999")) {
			_log.error("[ERROR] codErr 9999");
			return true;
		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			_log.error("[ERROR] codErr <> 0000");
			return true;
		} else if (areaDatiPassReturn.getNumNotizie()<=0) {
			_log.error("[ERROR] codErr = " + areaDatiPassReturn.getCodErr() + " / nr.notizie = " + areaDatiPassReturn.getNumNotizie());
			return true;
		}

		return false;
	}

	/**
	 * Lista di bid presenti.
	 * Struttura record: string
	 *
	 * @param numRichiesta numero identificativo della richiesta di prenotazione
	 * @return List
	 * @throws Exception
	 */
	private List<?> getBid(String numRichiesta, String idBtach ) throws Exception {

		// Modifica almaviva2 09.07.2012
		// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta (nuovo campo
		// su tabella import1 id_batch che contiene il progr batch)
		startSession();
		boolean ok = false;
		try {
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select id_input from import1 ");
//			sql.append("where tag='001' and nr_richiesta="+numRichiesta);
			sql.append("where tag='001' and id_batch="+idBtach);
			SQLQuery query = session.createSQLQuery(sql.toString());
			List<?> list = query.list();
			ok = true;
			return list;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		} finally {
			endTransaction(tx, ok);
		}
	}
	/**
	 * Lista di identificativi presenti.
	 * Struttura record: string, string
	 *
	 * @param numRichiesta numero identificativo della richiesta di prenotazione
	 * @return List
	 * @throws Exception
	 */
	List<?> getIdentificativi(String numRichiesta) throws Exception {
		startSession();
		boolean ok = false;
		try {
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select id_link, tag from import1 ");
			sql.append("where (id_link is not null and id_link!='') and nr_richiesta="+numRichiesta);
			sql.append(" group by 1, 2");
			SQLQuery query = session.createSQLQuery(sql.toString());
			List<?> list = query.list();
			ok = true;
			return list;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		} finally {
			endTransaction(tx, ok);
		}
	}

	/**
	 * Lista di record trovati.
	 * Struttura record: string, string, string, int
	 *
	 * @return List
	 * @throws Exception
	 */
	private List<?> getAutori() throws Exception {
		startSession();
		boolean ok = false;
		try {
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select id_link, tag, dati, count(id_link) as num from import1 ");
			sql.append("where tag like '7%' and tag not like '79%' ");
			sql.append("group by 1, 2, 3");
			SQLQuery query = session.createSQLQuery(sql.toString());
			List<?> list = query.list();
			ok = true;
			return list;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		} finally {
			endTransaction(tx, ok);
		}
	}



/*

unificare le queries dei titoli ????????????

select id_link, tag, dati, count(id_link) as num
from import1
where tag like '4%' or tag like '5%'
group by 1, 2, 3
*/
	/**
	 * Lista di record trovati.
	 * Struttura record: string, string, string, int
	 *
	 * @return List
	 * @throws Exception
	 */
	private List<?> getLegami() throws Exception {
		startSession();
		boolean ok = false;
		try {
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select id_link, tag, dati, count(id_link) as num from import1 ");
			sql.append("where tag like '4%' ");
			sql.append("group by 1, 2, 3");
			SQLQuery query = session.createSQLQuery(sql.toString());
			List<?> list = query.list();
			ok = true;
			return list;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		} finally {
			endTransaction(tx, ok);
		}
	}

	/**
	 * Lista di record trovati.
	 * Struttura record: string, string, string, int
	 *
	 * @return List
	 * @throws Exception
	 */
	private List<?> getTitoliRelazione() throws Exception {
		startSession();
		boolean ok = false;
		try {
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select id_link, tag, dati, count(id_link) as num from import1 ");
			sql.append("where tag like '5%' ");
			sql.append("group by 1, 2, 3");
			SQLQuery query = session.createSQLQuery(sql.toString());
			List<?> list = query.list();
			ok = true;
			return list;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		} finally {
			endTransaction(tx, ok);
		}
	}

	/**
	 * Lista di record trovati.
	 *
	 * @return List
	 * @throws Exception
	 */
	private List<?> getRecords(StringBuilder sqlSelect) throws Exception {
		startSession();
		boolean ok = false;
		try {
			SQLQuery query = session.createSQLQuery(sqlSelect.toString());
			List<?> list = query.list();
			ok = true;
			return list;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		} finally {
			endTransaction(tx, ok);
		}
	}

	/**
	 * Esegue insert e update su db.
	 *
	 * @param query query da eseguire
	 * @param param parametri da impostare nella query (null se inesistenti)
	 * @param count nr. di esecuzioni (commit transaction ogni 50)
	 * @throws DaoManagerException
	 */

	static class ImportQueryData implements QueryData {

		public ImportQueryData() {
			super();
		}

		public ImportQueryData(String query, String param) {
			super();
			this.query = query;
			this.param = param;
		}

		static List<QueryData> asList(String query, String param) {
			List<QueryData> list = new ArrayList<QueryData>();
			list.add(new ImportQueryData(query, param));
			return list;
		}

		String query;
		String param;

		public String query() {
			return query;
		}

		public String param() {
			return param;
		}

	}

	private void executeInsertUpdate(List<QueryData> queryData) throws Exception {
		try {
			getDocumentoFisico().executeInsertUpdateImport(queryData);
		} catch (Exception e) {
			throw e;
		}
	}

	private void startSession() throws Exception {
		begin(tx);
		session = this.getCurrentSession();
		session.setCacheMode(CacheMode.IGNORE);
	}

	// Modifica almaviva2 27.09.2011 - Classe per il trattamento dei dati bibliografici/semantici della tabella import1
	// almaviva2 Maggio 2015: Evolutiva richiesta da  per dividere il trattamento dell'estrazione Autori in due passi:
	// passo1: estrazione della lista dei soli id per tag '700', '701', '702', '710', '711', '712'
	// passo2: estrazione del record intero per ogni elemento della lista precedentemente creata
	// L'intervento serve perchè l'Import di DISCOTECA DI STATO ha circa 5.000.000 e si va in "out of memory"
	public AreaDatiImportSuPoloVO execFase2ter_estraiIdAutori(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		try {

			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id from import1 i1");
			sql.append(" where i1.nr_richiesta=").append(numRichiestaImport);
			sql.append(" and i1.stato_id_input = 'I'");
			sql.append(" and i1.tag in ('700', '701', '702', '710', '711', '712')");



			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size < 1)
				return areaVo;

			List<String> listaEtichette001 = new ArrayList<String>(size);
			for (int i = 0; i < size; i++) {
				String id = ((Number) list.get(i)).toString();			//identificativo
				listaEtichette001.add(id);
			}

			areaVo.setListaEtichette001(listaEtichette001);
			areaVo.setContEstratti(size);

		} catch (Exception e) {
			_log.error("[Errore] ", e );
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);

		return areaVo;
	}

	public AreaDatiImportSuPoloVO execFase2ter_estraiDatiAutori(AreaDatiImportSuPoloVO areaVo, String idAutore, BatchLogWriter blw) {

		// AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, i1.dati, ");
			sql.append("calcola_import_id_link(i1.tag, i1.dati) from import1 i1 ");
			sql.append("where i1.id=" + idAutore);

			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);

			for (int i = 0; i < size; i++) {

				// cicla su ogni occorrenza
				record = (Object[]) list.get(i);

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				tag = (String)record[3];					//tag
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2

				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link

				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				if (leader == null) {
					tracciatoImport1VO.setTipoRecord("");
					tracciatoImport1VO.setNatura("");
				} else {
					tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
					tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				}
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] ", e );
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);

		return areaVo;
	}




	// in questa select si estraggono solo i legami con tag 4?? che non sono referenziati ad uno specifico tag 001
	public AreaDatiImportSuPoloVO execFase2ter_estraiTitoliCollegati4xx(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);

		// ultima versione 15.01.2013: con campo titoloFormattato valorizzato con i dati della collana presi dalla rispettiva 210;
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, ");
			sql.append("REPLACE(REPLACE(i1.dati,E'\\x1BH',''),E'\\x1BI','*') as dat1_4xx, ");
			sql.append("CASE when i1.tag='410' then i3.dati else '' end as dati_210, ");

			sql.append(" calcola_import_id_link(i1.tag, i1.dati, i3.dati) as chiaveUnivoca ");

			sql.append("from import1 i1  ");
			sql.append("left outer join import1 i3 ");
			sql.append("on i3.nr_richiesta=i1.nr_richiesta ");
			sql.append("and i3.id_input=i1.id_input ");
			sql.append("and i3.tag='210' ");
			sql.append("left outer join import1 i2 ");
			sql.append("on i2.nr_richiesta=i1.nr_richiesta ");
			sql.append("AND i2.id_input=i1.id_link ");
			sql.append("AND i2.tag='200' ");
			sql.append("WHERE i1.nr_richiesta= " + numRichiestaImport + " ");
			sql.append("and i1.stato_id_input = 'I' ");
			sql.append("and i1.tag like '4%'  ");
			sql.append("and not i1.tag LIKE '46%' ");
			sql.append("and i2.id isnull ");
			sql.append("order by i1.tag, chiaveUnivoca");


			long id;
			String bid;
			String leader;
			String tag="";
			char ind1;
			char ind2;
			String idLink;
			String dati200="";
			String dati210="";
			String titForm="";


			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			areaVo.setContEstrattiOriginali(size);

			for (int i = 0; i < size; i++) {
				// cicla su ogni occorrenza ma esclude i legami 461-462-463 perchè gerarchici e 451-452 perchè il documento sarà anche radice
				// quindi saranno tutti elaborati dall funzione tratta documento e legami
				record = (Object[]) list.get(i);

				// inserito controllo per evitare il trattamento di oggetti con parte dati o tiform = null e dei duplicati
				if (((String)record[7]) == null || ((String)record[9]) == null) {
					continue;
				}

				if (((String)record[3]).equals(tag) && ((String)record[9]).equals(titForm)) {
					continue;
				}

				tag = (String)record[3];					//tag

				if (tag.equals("461") || tag.equals("462") || tag.equals("463")
						||tag.equals("451") || tag.equals("452")) {

				} else {
					bid = (String)record[1];					//id_input = bid della vecchia base dati

					leader = (String)record[2];					//leader
					ind1 = getCharValue(record[4]);				//indicatore1
					ind2 = getCharValue(record[5]);				//indicatore2

					idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
					dati200 = (String)record[7];					//dati
					dati210 = (String)record[8];					//dati
					titForm = (String)record[9];					//titoloFormattato x collana


					dati200 = trattaCaratteriSpeciali(dati200);
					dati210 = trattaCaratteriSpeciali(dati210);

					tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
					tracciatoImport1VO.setId("");
					tracciatoImport1VO.setIdInput(bid);
					tracciatoImport1VO.setTag(tag);
					tracciatoImport1VO.setIndicatore1(ind1);
					tracciatoImport1VO.setIndicatore2(ind2);
					tracciatoImport1VO.setIdLink(idLink);
					tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
					tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
					tracciatoImport1VO.setLevel(leader.substring(8,9));
					tracciatoImport1VO.setDati(dati200);
					tracciatoImport1VO.setDati210(dati210);
					tracciatoImport1VO.setTitoloFormattato(titForm);

					listaTracciatoRecordArray.add(tracciatoImport1VO);
				}
			}
			areaVo.setContEstrattiNoDuplicati(listaTracciatoRecordArray.size());
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] ", e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;
	}

	public AreaDatiImportSuPoloVO execFase2ter_estraiTitoliCollegatiAltri5xx(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();


		// Modifica Dicembre 2014 inserimento dei tag 928 e 929 per estrazione delle informazioni sulla composizione per creare
		// i titoli Uniformi Musicali
		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, i1.dati, ");
			sql.append("calcola_import_id_link(i1.tag, i1.dati) from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport
					+ " and i1.stato_id_input = 'I' and (i1.tag > '500' and i1.tag < '600')"
					+ " order by i1.id_input, i1.tag, i1.dati");



			long id;
			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);

			for (int i = 0; i < size; i++) {
				// cicla su ogni occorrenza
				record = (Object[]) list.get(i);
				tag = (String)record[3];					//tag

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2

				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link
				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
				tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				tracciatoImport1VO.setLevel(leader.substring(8,9));
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] ", e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;
	}

	public AreaDatiImportSuPoloVO execFase2ter_estraiTitoliCollegatiSolo500(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();


		// Modifica Dicembre 2014 inserimento dei tag 928 e 929 per estrazione delle informazioni sulla composizione per creare
		// i titoli Uniformi Musicali
		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, i1.dati, ");
			sql.append("calcola_import_id_link(i1.tag, i1.dati) from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport
					+ " and i1.stato_id_input = 'I' and (i1.tag = '500' or i1.tag = '928' or i1.tag = '929')"
					+ " order by i1.id_input, i1.tag, i1.dati");



			long id;
			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);

			for (int i = 0; i < size; i++) {
				// cicla su ogni occorrenza
				record = (Object[]) list.get(i);
				tag = (String)record[3];					//tag

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2

				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link
				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
				tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				tracciatoImport1VO.setLevel(leader.substring(8,9));
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] ", e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;
	}

	// la parte semantica deve essere inserita sia nel caso di id_input = I che negli altri casi
	public AreaDatiImportSuPoloVO execFase2ter_estraiSogClaCollegati6xx(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, i1.dati, ");
			sql.append("calcola_import_id_link(i1.tag, i1.dati) from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport + " and i1.tag like ('6%')");

			long id;
			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);

			for (int i = 0; i < size; i++) {
				// cicla su ogni occorrenza
				record = (Object[]) list.get(i);
				tag = (String)record[3];					//tag

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2

				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link

				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
				tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				tracciatoImport1VO.setLevel(leader.substring(8,9));
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] ", e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;
	}

	public AreaDatiImportSuPoloVO execFase2ter_estraiTitoloPerIdUnimarc(AreaDatiImportSuPoloVO areaVo, String numRichiestaImport, String bidRicerca) {

		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		// Maggio 2013 - Viene inserita la corretta gestione di impostazione del campo uriDigitalBorn(etichetta 856)
		// modificata la select così da estrarre anche etichetta 856 oltre alle inferiori a 400
		// Gennaio 2015 - per Import di Discoteca di Stato si Importa anche questa nuova etichetta 927 (personaggi/interpreti)
		// Novembre 2015 - per Import di Discoteca di Stato si Importa anche questa nuova etichetta 922 (dati rappresentazione)
		try {

			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, i1.dati, ");
			sql.append("calcola_import_id_link(i1.tag, i1.dati) from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport
					+ " and i1.id_input='" + escapeSql(bidRicerca) + "'"
					+ " and i1.stato_id_input='I'"
					+ " and (i1.tag < '400' or i1.tag in ('856', '922', '927') ) "
					+ " order by i1.tag");

			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0)
				throw new Exception("Nessun record presente con nr. richiesta: " + numRichiestaImport);
			sql = null;

			for (int i = 0; i < size; i++) {

				// cicla su ogni occorrenza
				Object[] record = (Object[]) list.get(i);

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				tag = (String)record[3];					//tag
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2

				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link

				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
				tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
		} catch (Exception e) {
			_log.error("[Errore] ", e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;
	}


	public AreaDatiImportSuPoloVO execFase2ter_estraiLegamiTitoli(AreaDatiImportSuPoloVO areaVo, String numRichiestaImport, String bidRicerca) {

		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);

			// ultima versione 15.01.2013: con campo titoloFormattato valorizzato con i dati della collana presi dalla rispettiva 210;
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, ");
			sql.append("REPLACE(REPLACE(i1.dati,E'\\x1BH',''),E'\\x1BI','*') as dat1_4xx, ");
			sql.append("CASE when i1.tag='410' then i3.dati else '' end as dati_210, ");
			sql.append(" calcola_import_id_link(i1.tag, i1.dati, i3.dati)");
			sql.append("from import1 i1  ");
			sql.append("left outer join import1 i3 ");
			sql.append("on i3.nr_richiesta=i1.nr_richiesta ");
			sql.append("and i3.id_input=i1.id_input ");
			sql.append("and i3.tag='210' ");
			sql.append("WHERE i1.nr_richiesta= " + numRichiestaImport + " and i1.id_input='" + escapeSql(bidRicerca) + "' " );
			sql.append("and i1.stato_id_input = 'I' ");
			sql.append("and (i1.tag like '4%' or i1.tag like '5%' or i1.tag like '7%')  ");
			sql.append("and not i1.tag in ('464') ");
			sql.append("order by i1.tag");

			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0) {
				areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
				return areaVo;
//				throw new Exception("Nessun record presente con nr. richiesta: " + numRichiestaImport);
			}


			sql = null;

			for (int i = 0; i < size; i++) {

				// cicla su ogni occorrenza
				record = (Object[]) list.get(i);

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				tag = (String)record[3];					//tag
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2
				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
//				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link
				chiaveCalcolata = (String)record[9];		//chiave calcolata dalla funzione calcola_import_id_link
				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
				tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;
	}

	public AreaDatiImportSuPoloVO execFase2ter_estraiLegameSuperiore(AreaDatiImportSuPoloVO areaVo, String numRichiestaImport, String bidRicerca) {

		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		// Modifica Settembre 2013:Procedura di import: eliminata la ricerca del legame 463 perchè condizione non possibile
		// che induce messaggistica che crea confusione.
		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, i1.dati, ");
			sql.append("calcola_import_id_link(i1.tag, i1.dati) from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport
					+ " and i1.id_input='" + escapeSql(bidRicerca) + "'"
					+ " and i1.stato_id_input='I'"
//					+ " and (i1.tag = '461' or i1.tag = '462' or i1.tag = '463') order by i1.tag");
					+ " and (i1.tag = '461' or i1.tag = '462') order by i1.tag");

			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0) {
				areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
				return areaVo;
			}


			sql = null;

			for (int i = 0; i < size; i++) {

				// cicla su ogni occorrenza
				record = (Object[]) list.get(i);

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				tag = (String)record[3];					//tag
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2
				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link

				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
				tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;


	}
	public AreaDatiImportSuPoloVO execFase2ter_estraiLegamiTitoliSemantici(AreaDatiImportSuPoloVO areaVo, String numRichiestaImport, String bidRicerca) {

		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		TracciatoDatiImport1ParzialeVO tracciatoImport1VO;
		List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray = new ArrayList<TracciatoDatiImport1ParzialeVO>();

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select i1.id, i1.id_input, i1.leader, i1.tag, i1.indicatore1, i1.indicatore2, i1.id_link, i1.dati, ");
			sql.append("calcola_import_id_link(i1.tag, i1.dati) from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport
					+ " and i1.id_input='" + escapeSql(bidRicerca) + "'"
//					+ " and stato_id_input='I'"
					+ " and i1.tag like '6%'  order by i1.tag");

			String bid;
			String leader;
			String tag;
			char ind1;
			char ind2;
			String idLink;
			String dati;
			String chiaveCalcolata;

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0) {
				areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
				return areaVo;
//				throw new Exception("Nessun record presente con nr. richiesta: " + numRichiestaImport);
			}


			sql = null;

			for (int i = 0; i < size; i++) {

				// cicla su ogni occorrenza
				record = (Object[]) list.get(i);

				bid = (String)record[1];					//id_input = bid della vecchia base dati
				leader = (String)record[2];					//leader
				tag = (String)record[3];					//tag
				ind1 = getCharValue(record[4]);				//indicatore1
				ind2 = getCharValue(record[5]);				//indicatore2
				idLink = (String)record[6]; 				//bid estratto dai dati come aggetto di arrivo del legame ivi descritto
				dati = (String)record[7];					//dati
				chiaveCalcolata = (String)record[8];		//chiave calcolata dalla funzione calcola_import_id_link
				dati = trattaCaratteriSpeciali(dati);

				tracciatoImport1VO = new TracciatoDatiImport1ParzialeVO();
				tracciatoImport1VO.setId("");
				tracciatoImport1VO.setIdInput(bid);
				tracciatoImport1VO.setTag(tag);
				tracciatoImport1VO.setIndicatore1(ind1);
				tracciatoImport1VO.setIndicatore2(ind2);
				tracciatoImport1VO.setIdLink(idLink);
				tracciatoImport1VO.setTipoRecord(leader.substring(6,7));
				tracciatoImport1VO.setNatura(leader.substring(7,8).toUpperCase());
				tracciatoImport1VO.setDati(dati);
				tracciatoImport1VO.setTitoloFormattato(chiaveCalcolata);
				listaTracciatoRecordArray.add(tracciatoImport1VO);

			}
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		areaVo.setListaTracciatoRecordArray(listaTracciatoRecordArray);
		return areaVo;
	}


	public AreaDatiImportSuPoloVO execFase2ter_estraiEtich001Ordinate(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);

			sql.append("select 9, i1.id_input from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport
					+ " and i1.stato_id_input = 'I' and i1.tag = '200' order by id_input");

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0)
				throw new Exception("Nessun record presente con nr. richiesta: " + numRichiestaImport);
			sql = null;

			// cicla su ogni occorrenza per caricare la lista dei documenti da elaborare
			for (int i = 0; i < size; i++) {
				record = (Object[]) list.get(i);
				areaVo.addListaEtichette001(record[0].toString() + (String)record[1]); //id_input = bid della vecchia base dati
			}
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		return areaVo;
	}


	public AreaDatiImportSuPoloVO execFase2ter_estraiEtich001OrdinateTutte(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		// Intervento interno 07.04.2014 - Richiesta  almaviva
		// Vengono estratti solo i record con stato_id_input <> 'T' cioè che non sono stati già trattati
		// (alla fine della creazione dei legami si aggiorna il flag stato_id_input con il valore 'T' TRATTATO
		// per indicare che il trattamento bibliografico è stato conpletato così che in una eventuale ripartenza
		// del trattamento i record 200 già esaminati non vengano ripresi in considerazione)

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);

			sql.append("select 9, i1.id_input from import1 i1 ");
			sql.append("where i1.nr_richiesta=" + numRichiestaImport
//					+ " and i1.stato_id_input = 'I' and i1.tag = '200' order by id_input");
					+ " and i1.stato_id_input <> 'T'"
					+ " and i1.tag = '200' order by id_input");

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0)
				throw new Exception("Nessun record presente con nr. richiesta: " + numRichiestaImport);
			sql = null;

			// cicla su ogni occorrenza per caricare la lista dei documenti da elaborare
			for (int i = 0; i < size; i++) {
				record = (Object[]) list.get(i);
				areaVo.addListaEtichette001(record[0].toString() + (String)record[1]); //id_input = bid della vecchia base dati
			}
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		return areaVo;
	}

	public AreaDatiImportSuPoloVO execFase2ter_update200Trattate(AreaDatiImportSuPoloVO areaVo, String numRichiestaImport, String idDaElab, BatchLogWriter blw) {

		//AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		try {
			// aggiorna stato su import1
			String sql = "update import1 SET stato_id_input='T'";
			sql += " where nr_richiesta=" + numRichiestaImport;
			sql += " and id_input='" + idDaElab + "'" ;
			sql += " and tag = '200'";
			executeInsertUpdate(ImportQueryData.asList(sql, null));
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		return areaVo;
	}



	public AreaDatiImportSuPoloVO execFase2ter_estraiEtich001Inferiori(String numRichiestaImport, BatchLogWriter blw) {

		AreaDatiImportSuPoloVO areaVo = new AreaDatiImportSuPoloVO(richiesta.getIdBatch(), blw);
		areaVo.setCodErr("0000");
		areaVo.setTestoErrore("");
		areaVo.setTestoProtocollo("");

		try {
			Object[] record;
			StringBuilder sql = new StringBuilder(1024);

			// Modifica Settembre 2013: procedura di Import: inserito ordinamento risultato select delle 200 inferiori per id_input
			sql.append("select 0, id_input from import1 ")
					.append("where nr_richiesta = ").append(numRichiestaImport)
					.append(" and stato_id_input = 'I'")
					.append(" and upper(substring(leader,8,1))='M' and tag = '200' and indicatore1 = '0'")
					.append(" order by id_input");

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0)
				throw new Exception("Nessun record presente con nr. richiesta: " + numRichiestaImport);
			sql = null;

			// cicla su ogni occorrenza per caricare la lista dei documenti da elaborare
			for (int i = 0; i < size; i++) {
				record = (Object[]) list.get(i);
				areaVo.addListaEtichette001(record[0].toString() + (String)record[1]); //id_input = bid della vecchia base dati
			}
			areaVo.setContEstratti(size);
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			areaVo.setCodErr("9999");
			areaVo.setTestoErrore(e.getMessage());
			return areaVo;
		}
		return areaVo;
	}

	public String trattaCaratteriSpeciali (String dati) {
		if (dati == null)
			return "";

		// caratteri da sostituire che potrebbero essere presenti nello scarico Unimarc

		dati = dati.replaceAll("\\x1E", "");
		dati = dati.replaceAll("&lt;", "<");
		dati = dati.replaceAll("&gt;", ">");
		dati = dati.replaceAll("&amp;", "&");
		dati = dati.replaceAll("&quot;", "'");
		dati = dati.replaceAll("&apos;", "'");

		dati = dati.replaceAll("\\{92\\}", "'");
		dati = dati.replaceAll("92", "'");

		dati = dati.replaceAll("\\{dot\\}", "c");
		dati = dati.replaceAll("dot", "c");

		dati = dati.replaceAll("\\{mllhring\\}", "°");
		dati = dati.replaceAll("mllhring", "°");

		dati = dati.replaceAll("\\{0E\\}A\\{0F\\}a", "a'");
		dati = dati.replaceAll("Aa", "a'");

		dati = dati.replaceAll("\\{0E\\}A\\{0F\\}e", "e'");
		dati = dati.replaceAll("Ae", "e'");

		dati = dati.replaceAll("\\{0E\\}A\\{0F\\}o", "o'");
		dati = dati.replaceAll("Ao", "o'");

		dati = dati.replaceAll("\\{0E\\}A\\{0F\\}u", "u'");
		dati = dati.replaceAll("Au", "u'");

		dati = dati.replaceAll("\\{0E\\}Au\\{0F\\}a", "ia");
		dati = dati.replaceAll("Aua", "ia");


		dati = dati.replaceAll("\\{0E\\}B\\{0F\\}aa", "à");
		dati = dati.replaceAll("Baa", "à");

		dati = dati.replaceAll("\\{0E\\}B\\{0F\\}e", "e'");
		dati = dati.replaceAll("Be", "e'");


		dati = dati.replaceAll("\\{0E\\}1\\{0F\\}", "'");
		dati = dati.replaceAll("1", "'");


		dati = dati.replaceAll("H\\[I", "<");
		dati = dati.replaceAll("H\\]I", ">");
		return dati;
	}


	public String deleteRecordImport1(String numRichiestaImport, BatchLogWriter blw) {

		String totPrima = "0";
		try {
			Object record;
			StringBuilder sql = new StringBuilder(1024);

			sql.append("select count(id) from import1 where nr_richiesta = " + numRichiestaImport);

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0)
				throw new Exception("Conteggio fallito con nr. richiesta: " + numRichiestaImport);
			sql = null;

			// cicla su ogni occorrenza per caricare la lista dei documenti da elaborare
			for (int i = 0; i < size; i++) {
				record = list.get(i);
				long nn = ((Number)record).longValue();
				totPrima = String.valueOf(nn);
			}
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			return "Errore in lettura di controllo. ERROR: " + e;
		}

		if (totPrima.equals("0")) {
			return "Non ci sono occorrenze da cancellare su tabella import1.";
		}

		String sql="delete from import1 where nr_richiesta=" + numRichiestaImport;
		try {
			executeInsertUpdate(ImportQueryData.asList(sql, null));
		} catch (Exception e) {

			return "Errore in cancellazione. ERROR: " + e;
		}
		return "Cancellazione effettuata correttamente su tabella import1. Occorrenze cancellate: "+ totPrima;
	}

	public String deleteRecordImportIdLink(String numRichiestaImport, BatchLogWriter blw) {

		String totPrima="0";
		try {
			Object record;
			StringBuilder sql = new StringBuilder(1024);

			sql.append("select count(id_record) from import_id_link where nr_richiesta = " + numRichiestaImport);

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0)
				throw new Exception("Conteggio fallito con nr. richiesta: " + numRichiestaImport);
			sql = null;

			// cicla su ogni occorrenza per caricare la lista dei documenti da elaborare
			for (int i = 0; i < size; i++) {
				record = list.get(i);
				long nn = ((Number)record).longValue();
				totPrima = String.valueOf(nn);
			}
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			return "Errore in lettura di controllo. ERROR: " + e;
		}
		if (totPrima.equals("0")) {
			return "Non ci sono occorrenze da cancellare su tabella import_id_link.";
		}

		String sql="delete from import_id_link where nr_richiesta=" + numRichiestaImport;
		try {
			executeInsertUpdate(ImportQueryData.asList(sql, null));
		} catch (Exception e) {

			return "Errore in cancellazione. ERROR: " + e;
		}
		return "Cancellazione effettuata correttamente su tabella import_id_link. Occorrenze cancellate: "+ totPrima;
	}

	public String deleteRecordImport950(String numRichiestaImport, BatchLogWriter blw) {

		String totPrima="0";
		try {
			Object record;
			StringBuilder sql = new StringBuilder(1024);

			sql.append("select count(id) from import950 where nr_richiesta = ").append(numRichiestaImport);

			List<?> list = getRecords(sql);
			int size = ValidazioneDati.size(list);
			if (size == 0)
				throw new Exception("Conteggio fallito con nr. richiesta: " + numRichiestaImport);
			sql = null;

			// cicla su ogni occorrenza per caricare la lista dei documenti da elaborare
			for (int i=0; i< size; i++) {
				record = list.get(i);
				long nn = ((Number)record).longValue();
				totPrima = String.valueOf(nn);
			}
		} catch (Exception e) {
			_log.error("[Errore] " , e);
			return "Errore in lettura di controllo. ERROR: " + e;
		}

		if (totPrima.equals("0")) {
			return "Non ci sono occorrenze da cancellare su tabella import950.";
		}

		String sql="delete from import950 where nr_richiesta=" + numRichiestaImport;
		try {
			executeInsertUpdate(ImportQueryData.asList(sql, null));
		} catch (Exception e) {

			return "Errore in cancellazione. ERROR: " + e;
		}
		return "Cancellazione effettuata correttamente su tabella import950. Occorrenze cancellate: "+ totPrima;
	}

	private void writeReportHeader(Writer w) throws IOException {
		w.append("<html>");
		w.append("<head>");
		w.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		w.append("<title>Importa documenti</title>");
		w.append("</head>");
		w.append("<body>");

		w.append("<h2>Importa documenti</h2>");

		w.append("<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%\">");
	}

	private void writeReportFooter(Writer w) throws IOException {
		w.append("</table>");
		w.append("</body>");
		w.append("</html>");
	}

	private void writeReportRow(Writer w, String msg) throws IOException {
		w.append("<TR><TD>").append(msg).append("</TD></TR>");
	}

	private Character getFlagCondivisioneTitolo(String bid) throws Exception {
		startSession();
		boolean ok = false;
		try {
			Character flag = titDAO.getFlagCondivisioneTitolo(bid);
			ok = true;
			return flag;
		} finally {
			endTransaction(tx, ok);
		}
	}


	private static char getCharValue(Object o) {
		return (o == null) ? Character.MIN_VALUE : ((Character)o).charValue();
	}

	public static String escapeSql(String sql) {
		sql = DaoManager.escapeSql(sql);

		sql = sql.replaceAll("ITICCU", "");
		//sql = sql.replaceAll("ICCU", "");
		sql = sql.trim();

		return sql;
	}

}
