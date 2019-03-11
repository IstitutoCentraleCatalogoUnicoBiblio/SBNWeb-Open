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
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.jumpmind.symmetric.csv.CsvWriter;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;

public class InventarioUriAUCDExecutor extends InventarioAUCDExecutor {

	private static final InventarioVO MOCK_INV = new InventarioVO();

	private int read = 0;
	private int updated = 0;
	private int errors = 0;

	@Override
	public void execute(AcquisizioneUriCopiaDigitaleVO richiesta,
			UserTransaction tx, BufferedReader r, BufferedWriter w,
			CsvWriter csv, Logger log) throws ApplicationException, IOException {

		setRichiesta(richiesta);

		Set<String> listaBid = new LinkedHashSet<String>();

		String codPolo = richiesta.getCodPolo();
		String codBib = richiesta.getCodBib();

		writeReportHeader(w);

		Tbc_inventarioDao dao = new Tbc_inventarioDao();
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
					writeReportRow(w, MOCK_INV, read, false, "formato linea non valido");
					continue;
				}

				final String kinv = tokens[0];
				final String uri = tokens[1];

				DaoManager.begin(tx);
				InventarioVO inv = InventarioRFIDParser.parse(kinv);
				//check biblioteca inventario
				if (!checkBibliotecaInventario(inv) ) {
					errors++;
					writeReportRow(w, inv, read, false, "inventario di altra biblioteca");
					continue;
				}

				inv.setCodPolo(codPolo);
				inv.setCodBib(codBib);
				Tbc_inventario i = dao.getInventario(codPolo, codBib, inv.getCodSerie(), inv.getCodInvent());
				if (i == null) {
					errors++;
					log.error("inventario non trovato: " + inv.getChiaveInventario());
					writeReportRow(w, inv, read, false, "non trovato");
					continue;
				}

				String msg = null;
				inv.setBid(i.getB().getBid());	//bid per uri
				//trattamento uri
				String uri_costruito = trimOrEmpty(i.getId_accesso_remoto());
				AggiornamentoUri aggUri = AcquisizioneUriCopiaDigitale.trattamentoUri(uri_costruito, null, richiesta.isAggiungiUri());
				for (String newUri : uri.split(AcquisizioneUriCopiaDigitale.REGEX_URI_LOC_SEPARATORE) ) {
					aggUri = AcquisizioneUriCopiaDigitale.trattamentoUri(uri_costruito, newUri, richiesta.isAggiungiUri());
					uri_costruito = aggUri.getUri();
				}
				if (uri_costruito.length() > Constants.URI_MAX_LENGTH) {	//uri troppo lungo
					errors++;
					DaoManager.rollback(tx);
					writeReportRow(w, inv, read, false, "URI troppo lungo");
					continue;
				}

				switch (aggUri.getTipoAggiornamento()) {
				case AGGIORNA_SPECIFICHE:
					impostaSpecifiche(i);
					msg = "specifiche aggiornate";
					break;

				case AGGIUNGI_URI:
				case SOSTITUISCI_URI:
					impostaSpecifiche(i);
					i.setId_accesso_remoto(uri_costruito);
					msg = "URI aggiornato";
					break;

				case NON_AGGIORNARE:
					errors++;
					DaoManager.rollback(tx);
					writeReportRow(w, inv, read, false, "non aggiornato");
					continue;
				}

				//vero aggiornamento
				boolean ok = dao.modificaInventario(i);
				if (!ok) {
					errors++;
					DaoManager.rollback(tx);
					log.error("errore aggiornamento inventario: " + inv.getChiaveInventario());
					writeReportRow(w, inv, read, false, "errore aggiornamento");
					continue;
				}

				//rilettura inventario
				inv = ConversioneHibernateVO.toWeb().inventario(null, i, Locale.getDefault());
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
				listaBid.add(inv.getBid());

				//scrittura report
				writeReportRow(w, inv, read, true, msg);
				//writeCSVRow(csv, inv);

			} catch (BatchInterruptedException e) {
				throw new ApplicationException(SbnErrorTypes.BATCH_MANUAL_STOP);

			} catch (Exception e) {
				errors++;
				DaoManager.rollback(tx);
				log.error("errore lettura identificativo: " + line);
				writeReportRow(w, MOCK_INV, read, false, "errore lettura identificativo");
				continue;
			}
		}

		log.debug("righe lette:          " + read);
		log.debug("righe aggiornate:     " + updated);
		log.debug("righe non aggiornate: " + errors);

		writeReportFooter(w, read, updated, errors);

		if (updated > 0 && isFilled(listaBid))
			aggiornaLocalizzazionePolo(tx, listaBid, csv, log);

	}

}
