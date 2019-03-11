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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.impl;

import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale.AggiornamentoUri;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.bibliografica.Tr_tit_bib;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.jumpmind.symmetric.csv.CsvWriter;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.leggiXID;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;


public class IdDocumentoUriAUCDExecutor extends IdDocumentoAUCDExecutor {

	private static final String MOCK_BID = "<non valido>";

	private int read = 0;
	private int updated = 0;
	private int errors = 0;

	public void execute(AcquisizioneUriCopiaDigitaleVO richiesta,
			UserTransaction tx, BufferedReader r, BufferedWriter w,
			CsvWriter csv, Logger log) throws ApplicationException, IOException {

		final Tr_tit_bib MOCK_LOCK = new Tr_tit_bib();
		MOCK_LOCK.setUri_copia("");
		MOCK_LOCK.setTp_digitalizz(' ');

		super.setRichiesta(richiesta);

		String codPolo = richiesta.getCodPolo();
		String codBib = richiesta.getCodBib();
		String firmaUtente = DaoManager.getFirmaUtente(richiesta.getTicket());

		writeReportHeader(w);

		TitoloDAO dao = new TitoloDAO();
		String line;
		while ((line = r.readLine()) != null) {
			try {
				read++;
				if ((read % 100) == 0)
					BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());

				//inv + uri
				String[] tokens = line.split(AcquisizioneUriCopiaDigitale.REGEX_AUCD_FILE_SEPARATOR);
				if (ValidazioneDati.size(tokens) != 2 ) {
					errors++;
					log.error("formato linea non valido: " + line);
					writeReportRow(w, null, read, MOCK_LOCK, false, "formato linea non valido");
					continue;
				}

				final String bid = tokens[0];
				final String uri = tokens[1];

				DaoManager.begin(tx);
				if (!leggiXID(bid)) {
					errors++;
					writeReportRow(w, bid, read, MOCK_LOCK, false, "identificativo non valido");
					continue;
				}

				Tb_titolo t = dao.getTitoloLazy(bid);
				if (t == null) {
					errors++;
					log.error("titolo non trovato: " + bid);
					writeReportRow(w, bid, read, MOCK_LOCK, false, "non trovato");
					continue;
				}
				boolean condiviso = in(t.getFl_condiviso(), 's', 'S');

				//check localizzazione
				Tr_tit_bib loc = dao.geLocalizzazioneBib(codPolo, codBib, bid);
				if (loc == null || !in(loc.getFl_possesso(), 's', 'S')) {
					errors++;
					DaoManager.rollback(tx);
					writeReportRow(w, bid, read, MOCK_LOCK, false, "titolo non localizzato");
					continue;
				}

				String msg = null;
				//DatiModelloUri uriData = DatiModelloUri.build(codPolo, codBib, bid);
				//String uri = AcquisizioneUriCopiaDigitale.costruisciUri(TipoFileInput.BID, prefix, model, suffix, uriData, eliminaSpaziUri);
				//trattamento uri
				String uri_costruito = trimOrEmpty(loc.getUri_copia());
				AggiornamentoUri aggUri = AcquisizioneUriCopiaDigitale.trattamentoUri(uri_costruito, null, richiesta.isAggiungiUri());
				for (String newUri : uri.split(AcquisizioneUriCopiaDigitale.REGEX_URI_LOC_SEPARATORE) ) {
					aggUri = AcquisizioneUriCopiaDigitale.trattamentoUri(uri_costruito, newUri, richiesta.isAggiungiUri());
					uri_costruito = aggUri.getUri();
				}
				if (uri_costruito.length() > Constants.URI_MAX_LENGTH) {	//uri troppo lungo
					errors++;
					DaoManager.rollback(tx);
					writeReportRow(w, bid, read, loc, false, "URI troppo lungo");
					continue;
				}

				switch (aggUri.getTipoAggiornamento()) {
				case AGGIORNA_SPECIFICHE:
					impostaSpecifiche(loc);
					msg = "specifiche aggiornate";
					break;

				case AGGIUNGI_URI:
				case SOSTITUISCI_URI:
					impostaSpecifiche(loc);
					loc.setUri_copia(uri_costruito);
					msg = "URI aggiornato";
					break;

				case NON_AGGIORNARE:
					errors++;
					DaoManager.rollback(tx);
					writeReportRow(w, bid, read, loc, false, "non aggiornato");
					continue;
				}

				//vero aggiornamento
				loc.setFl_disp_elettr('S');
				loc.setUte_var(firmaUtente);
				loc.setTs_var(DaoManager.now());

				try {
					dao.aggiornaLocalizzazione(loc);
				} catch (DaoManagerException e) {
					errors++;
					DaoManager.rollback(tx);
					log.error("errore aggiornamento titolo: " + bid);
					writeReportRow(w, bid, read, loc, false, "errore aggiornamento");
					continue;
				}
				/*
				//preparazione dati per localizzazione
				AreaDatiLocalizzazioniAuthorityVO adl = preparaDatiLocalizzazione(codPolo, codBib, i);
				if (adl == null) {
					errors++;
					DaoManager.rollback(tx);
					log.error("errore aggiornamento inventario: " + inv.getChiaveInventario());
					writeReportRow(w, inv, read, false, "errore localizzazione");
					continue;
				}

				//localizzazione massiva in polo
				eseguiLocalizzazione(richiesta.getTicket(), adl, log);
				*/
				//tutto ok
				updated++;
				DaoManager.commit(tx);

				//scrittura report
				writeReportRow(w, bid, read, loc, true, msg);
				if (condiviso)
					//solo se titolo condiviso
					writeCSVRow(csv, loc);

			} catch (BatchInterruptedException e) {
				throw new ApplicationException(SbnErrorTypes.BATCH_MANUAL_STOP);

			} catch (Exception e) {
				errors++;
				DaoManager.rollback(tx);
				log.error("errore lettura identificativo: " + line);
				writeReportRow(w, MOCK_BID, read, MOCK_LOCK, false, "errore lettura identificativo");
				continue;
			}
		}

		log.debug("righe lette:          " + read);
		log.debug("righe aggiornate:     " + updated);
		log.debug("righe non aggiornate: " + errors);

		writeReportFooter(w, read, updated, errors);

	}

}
