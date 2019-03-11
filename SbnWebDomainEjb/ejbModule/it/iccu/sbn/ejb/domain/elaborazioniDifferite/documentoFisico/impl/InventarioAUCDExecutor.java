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
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale.DatiModelloUri;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale.TipoFileInput;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.polo.orm.bibliografica.Tr_tit_bib;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.jumpmind.symmetric.csv.CsvWriter;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;

public class InventarioAUCDExecutor extends AUCDExecutorBaseImpl {

	private static final InventarioVO MOCK_INV = new InventarioVO();

	private int read = 0;
	private int updated = 0;
	private int errors = 0;

	public void execute(AcquisizioneUriCopiaDigitaleVO richiesta,
			UserTransaction tx, BufferedReader r, BufferedWriter w,
			CsvWriter csv, Logger log) throws ApplicationException, IOException {

		setRichiesta(richiesta);

		Set<String> listaBid = new LinkedHashSet<String>();

		String codPolo = richiesta.getCodPolo();
		String codBib = richiesta.getCodBib();
		String prefix = richiesta.getPrefisso();
		String model = richiesta.getModel();
		String suffix = richiesta.getSuffisso();

		boolean eliminaSpaziUri = richiesta.isEliminaSpaziUri();

		writeReportHeader(w);

		Tbc_inventarioDao dao = new Tbc_inventarioDao();
		String line;
		while ((line = r.readLine()) != null) {
			try {
				read++;
				if ((read % 100) == 0)
					BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());

				DaoManager.begin(tx);
				InventarioVO inv = InventarioRFIDParser.parse(line);
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
				String uri = AcquisizioneUriCopiaDigitale.costruisciUri(TipoFileInput.Inventario, prefix, model, suffix, DatiModelloUri.build(inv), eliminaSpaziUri);
				//trattamento uri
				String id_accesso_remoto = trimOrEmpty(i.getId_accesso_remoto());
				AggiornamentoUri aggUri = AcquisizioneUriCopiaDigitale.trattamentoUri(id_accesso_remoto, uri, richiesta.isAggiungiUri());
				String nuovoUri = aggUri.getUri();
				if (nuovoUri.length() > Constants.URI_MAX_LENGTH) {	//uri troppo lungo
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
					i.setId_accesso_remoto(nuovoUri);
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

	protected void aggiornaLocalizzazionePolo(UserTransaction tx, Set<String> listaBid, CsvWriter csv, Logger log)
			throws ApplicationException, IOException {

		AcquisizioneUriCopiaDigitaleVO richiesta = getRichiesta();
		String firmaUtente = DaoManager.getFirmaUtente(richiesta.getTicket());
		String codPolo = richiesta.getCodPolo();
		String codBib = richiesta.getCodBib();
		String digit = richiesta.getTipoDigit();

		TitoloDAO tdao = new TitoloDAO();
		Tbc_inventarioDao idao = new Tbc_inventarioDao();

		for (String bid : listaBid) {
			try {
				DaoManager.begin(tx);
				//check localizzazione
				Tr_tit_bib loc = tdao.geLocalizzazioneBib(codPolo, codBib, bid);
				if (loc == null || !in(loc.getFl_possesso(), 's', 'S')) {
					DaoManager.rollback(tx);
					log.error("titolo non localizzato: " + bid);
					continue;
				}
				String locUri = trimOrEmpty(loc.getUri_copia());
				List<Tbc_inventario> inventari = idao.getListaInventariTitolo(codPolo, codBib, bid);
				//ricostruzione uri da inventari del titolo
				for (Tbc_inventario inv : inventari) {
					String id_accesso_remoto = trimOrEmpty(inv.getId_accesso_remoto());
					if (!isFilled(id_accesso_remoto))
						continue;

					for (String uri : id_accesso_remoto.split(AcquisizioneUriCopiaDigitale.REGEX_URI_LOC_SEPARATORE)) {
						//uri multipli già su inventario?
						AggiornamentoUri aggUri = AcquisizioneUriCopiaDigitale.trattamentoUri(locUri, uri, true);	//sempre in aggiunta
						locUri = aggUri.getUri();
					}
				}

				if (locUri.length() > Constants.URI_MAX_LENGTH) {	//uri troppo lungo
					DaoManager.rollback(tx);
					log.error("URI troppo lungo per titolo: " + bid);
					continue;
				}

				boolean condiviso = in(loc.getB().getFl_condiviso(), 's', 'S');

				loc.setUri_copia(locUri);
				loc.setUte_var(firmaUtente);
				loc.setTs_var(DaoManager.now());
				loc.setFl_disp_elettr('S');
				if (isFilled(digit))
					loc.setTp_digitalizz(digit.charAt(0));

				tdao.aggiornaLocalizzazione(loc);
				DaoManager.commit(tx);

				if (condiviso)
					//solo se titolo condiviso
					writeCSVRow(csv, loc);

			} catch (Exception e) {
				DaoManager.rollback(tx);
				log.error("errore aggiornamento identificativo: " + bid);
				continue;
			}
		}

	}

	protected boolean checkBibliotecaInventario(InventarioVO inv) {

		String codPolo = inv.getCodPolo();
		AcquisizioneUriCopiaDigitaleVO richiesta = getRichiesta();
		if (isFilled(codPolo) && !codPolo.equals(richiesta.getCodPolo()))
			return false;

		String codBib = inv.getCodBib();
		if (isFilled(codBib) && !codBib.equals(richiesta.getCodBib()))
			return false;

		return true;
	}

	protected void impostaSpecifiche(Tbc_inventario i) {
		AcquisizioneUriCopiaDigitaleVO richiesta = getRichiesta();
		String digit = richiesta.getTipoDigit();
		String teca = richiesta.getTecaDigitale();
		String dispDaRemoto = richiesta.getDispDaRemoto();

		if (isFilled(digit))
			i.setDigitalizzazione(digit.charAt(0));

		if (isFilled(teca))
			i.setRif_teca_digitale(teca);

		if (isFilled(dispDaRemoto))
			i.setDisp_copia_digitale(dispDaRemoto);

	}

	protected void writeReportHeader(Writer w) throws IOException {
		w.append("<!DOCTYPE html>");
		w.append("<html>");
		w.append("<head>");
		w.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		w.append("<title>Importazione URI copia digitale</title>");
		w.append("</head>");
		w.append("<body>");

		w.append("<h2>Importa URI copia digitale</h2>");
		AcquisizioneUriCopiaDigitaleVO richiesta = getRichiesta();
		w.append("<h4>Biblioteca:&nbsp;").append(richiesta.getCodPolo()).append(richiesta.getCodBib()).append("</h4>");
		w.append("<h4>Data:&nbsp;").append(DateUtil.formattaData(System.currentTimeMillis())).append("</h4>");
		w.append("<hr/><br/>");

		w.append("<table width=\"90%\" border=\"1\">");

		w.append("<tr>");
		w.append("<th>n.ro</th>");
		w.append("<th>Inventario</th>");
		w.append("<th>BID</th>");
		w.append("<th>URI</th>");
		w.append("<th>Digitalizzazione</th>");
		w.append("<th>Esito</th>");
		w.append("<th>Messaggio</th>");
		w.append("</tr>");
	}

	protected void writeReportRow(Writer w, InventarioVO inv, int idx, boolean esito, String msg) throws IOException {
		w.append("<tr>");
		w.append("<td>").append(String.valueOf(idx)).append("</td>");
		w.append("<td>").append(coalesce(inv.getChiaveInventario(), HTML_NBSP) ).append("</td>");
		w.append("<td>").append(coalesce(inv.getBid(), HTML_NBSP) ).append("</td>");
		w.append("<td>").append(coalesce(inv.getIdAccessoRemoto(), HTML_NBSP) ).append("</td>");

		String digit = null;
		try {
			digit = CodiciProvider.cercaDescrizioneCodice(inv.getDigitalizzazione(),
					CodiciType.CODICE_TIPO_DIGITALIZZAZIONE,
					CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {}
		w.append("<td>").append(coalesce(digit, HTML_NBSP)).append("</td>");

		w.append("<td>").append(esito ? "OK" : "ERROR").append("</td>");
		w.append("<td>").append(coalesce(msg, HTML_NBSP)).append("</td>");
	}

	protected void writeReportFooter(Writer w, int read, int updated, int errors) throws IOException {
		w.append("</table>");
		w.append("<br/>");
		w.append("<h4>inventari letti:&nbsp;").append(String.valueOf(read)).append("</h4>");
		w.append("<h4>inventari aggiornati:&nbsp;").append(String.valueOf(updated)).append("</h4>");
		w.append("<h4>inventari non aggiornati:&nbsp;").append(String.valueOf(errors)).append("</h4>");
		w.append("</body>");
		w.append("</html>");
	}

	protected void writeCSVRow(CsvWriter csv, Tr_tit_bib loc) throws IOException {
		AcquisizioneUriCopiaDigitaleVO richiesta = getRichiesta();
		if (!richiesta.isPreparaFileIndice() || csv == null)
			return;

		csv.write(richiesta.getCodPolo());
		csv.write(richiesta.getCodBib(), true);
		csv.write(loc.getB().getBid());
		csv.write(loc.getTp_digitalizz() != null ?  loc.getTp_digitalizz().toString() : "");
		csv.write(loc.getUri_copia());
		csv.endRecord();
	}

}
