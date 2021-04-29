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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBStampeFactory;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeUtenti;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO.TipoUtente;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchSollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.extension.sms.SMSResult;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.SollecitiDAO;
import it.iccu.sbn.persistence.dao.servizi.TipoServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.UtilitaDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_solleciti;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.mail.MailUtil.AddressPair;
import it.iccu.sbn.util.mail.servizi.ServiziMail;
import it.iccu.sbn.util.servizi.SollecitiUtil;
import it.iccu.sbn.util.sms.SMSUtil;
import it.iccu.sbn.vo.custom.servizi.ElementoStampaSollecitoDecorator;
import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJBException;
import javax.mail.internet.InternetAddress;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

public class BatchSolleciti {

	private static final char SMS = '1';
	private static final char MAIL = '2';
	private static final char LETTERA = '3';

	private static final Map<TipoUtente, String> intestazione = new HashMap<ElementoStampaSollecitoVO.TipoUtente, String>();
	private static final int MAX_RETRIES = 3;
	//almaviva5_20121024 test errore
	private Tbl_solleciti NON_SOLLECITABILE = new Tbl_solleciti();

	static {
		//almaviva5_20110802 #4447
		intestazione.put(TipoUtente.ENTE, "Spett.le");
		intestazione.put(TipoUtente.MASCHIO, "Egr. Sig.");
		intestazione.put(TipoUtente.FEMMINA, "Egr. Sig.ra");
	}

	private enum TipoInvioSollecito {
		NON_TROVATO,
		SMS_MOBILE,
		SMS_FISSO,
		MAIL,
		LETTERA,
		ERRORE;
	}

	private enum TipoEsito {
		SOLLECITATO,
		NON_SOLLECITATO,
		NON_SOLLECITABILE;
	}

	private final UserTransaction tx;
	private final Logger log;

	private final ParametriBatchSollecitiVO richiesta;
	private final BatchLogWriter blog;

	private RichiesteServizioDAO dao = new RichiesteServizioDAO();
	private TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();
	private UtilitaDAO utilitaDAO = new UtilitaDAO();
	private SollecitiDAO sollecitiDAO = new SollecitiDAO();
	private ServiziMail util = new ServiziMail();
	private ParametriBibliotecaVO parametriBib;
	private ModelloSollecitoVO modello;

	public BatchSolleciti(UserTransaction tx, ParametriBatchSollecitiVO richiesta, BatchLogWriter blog) {
		this.tx = tx;
		this.richiesta = richiesta;
		this.blog = blog;
		log = blog.getLogger();
	}

	public ElaborazioniDifferiteOutputVo eseguiBatchSolleciti() throws ApplicationException, EJBException {

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);

		StringBuilder report = new StringBuilder();
		ScrollableResults cursor = null;
		int totale = 0;

		try {
			try {
				DaoManager.begin(tx);

				String codPolo = richiesta.getCodPolo();
				String codBib = richiesta.getCodBib();

				Tbl_parametri_biblioteca parBib = utilitaDAO.getParametriBiblioteca(codPolo, codBib);
				if (parBib == null)
					throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

				//almaviva5_20150609
				parametriBib = ConversioneHibernateVO.toWeb().parametriBiblioteca(parBib);

				if (parametriBib.getNumeroLettere() < 1) {
					log.error("La biblioteca non ha configurato il numero di avvisi di sollecito");
					output.setStato(ConstantsJMS.STATO_ERROR);
					DaoManager.rollback(tx);
					return output;
				}

				List<String> tipiServizioSollecitabili = tipoServizioDAO.getTipiServizioSollecitabili(codPolo, codBib);

				// non ci sono servizi dichiarati sollecitabili
				if (!ValidazioneDati.isFilled(tipiServizioSollecitabili)) {
					log.error("La biblioteca non ha configurato tipi servizi sollecitabili");
					output.setStato(ConstantsJMS.STATO_ERROR);
					DaoManager.rollback(tx);
					return output;
				}

				//almaviva5_20150610 evolutiva solleciti
				modello = parametriBib.getModelloSollecito();
				modello = (modello != null) ? modello : SollecitiUtil.getModelloSollecitoBase();
				log.debug("modello lettera: " + StringUtils.replaceChars(modello.getModello(), "\r\n", " "));
				log.debug("modello e-mail:  " + StringUtils.replaceChars(modello.getModelloMail(), "\r\n", " "));
				log.debug("modello SMS:     " + StringUtils.replaceChars(modello.getModelloSms(), "\r\n", " "));

				Timestamp scadenza = richiesta.getDataScadenza();
				String firmaUtente = codPolo + codBib + richiesta.getUser();
				Long[] listaRichieste = richiesta.getListaRichieste();
				short numGgRitardo1 = parametriBib.getGgRitardo1();

				cursor = dao.getCursorRichiesteScadute(codPolo, codBib,
						listaRichieste, scadenza, tipiServizioSollecitabili, 0,
						numGgRitardo1);

				List<ElementoStampaSollecitoVO> listaSollecitiPerStampa = new ArrayList<ElementoStampaSollecitoVO>();

				int loop = 0;
				boolean error = false;

				writeSollecitiReportHeader(report, richiesta.getDescrizioneBiblioteca());

				while (cursor.next()) {
					BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());

					Tbl_richiesta_servizio req = (Tbl_richiesta_servizio) cursor.get(0);
					// recupero l'eventuale sollecito per la richiesta corrente
					Tbl_solleciti sollecito = sollecitiDAO.getSollecito(req);
					if (sollecito != null
							&& sollecito.getProgr_sollecito() == parametriBib.getNumeroLettere()
							&& sollecito.getEsito() == 'S') {

						// se parto da sintetica devo comunque ri-stampare la lettera
						if (richiesta.isRichiestaDaSintetica()) {
							ElementoStampaSollecitoVO ess = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, sollecito, "ristampato");
							writeSollecitiReportRow(report, ess, 1);
							totale += listaSollecitiPerStampa.add(new ElementoStampaSollecitoDecorator(ess)) ? 1 : 0;
							continue;
						}

						// richiesta non più sollecitabile
						log.warn("La richiesta n. "
								+ req.getCod_rich_serv()
								+ " non é più sollecitabile secondo i parametri impostati per la Biblioteca");
						ElementoStampaSollecitoVO soll = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, sollecito,
										"non sollecitabile secondo i parametri impostati per la Biblioteca");
						writeSollecitiReportRow(report, soll, 1);
						continue;
					}

					try {
						TipoEsito esito = loopInvio(req, sollecito, report, scadenza, firmaUtente, listaSollecitiPerStampa);
						if (esito == TipoEsito.SOLLECITATO)
							totale++;

					} catch (Exception e) {
						log.error("Errore sollecito su richiesta n. " + req.getCod_rich_serv(), e);
						error = true;
					}

					if (++loop == Constants.BATCH_COMMIT_THRESHOLD || error) {
						// chiudo il cursore e committo la transazione
						long lastId = req.getCod_rich_serv();
						DaoManager.closeCursor(cursor);
						DaoManager.endTransaction(tx, !error);

						loop = 0;
						error = false;
						DaoManager.begin(tx);
						cursor = dao.getCursorRichiesteScadute(codPolo, codBib,
								listaRichieste, scadenza,
								tipiServizioSollecitabili, lastId,
								numGgRitardo1);
						continue;
					}
				}

				DaoManager.closeCursor(cursor);
				DaoManager.commit(tx);

				output.setStato(ConstantsJMS.STATO_OK);

				//procedo con la stampa dei solleciti per lettera
				if (ValidazioneDati.isFilled(listaSollecitiPerStampa))
					output = stampaSolleciti(output, listaSollecitiPerStampa);

			} catch (DaoManagerException e) {
				DaoManager.closeCursor(cursor);
				DaoManager.rollback(tx);
				output.setStato(ConstantsJMS.STATO_ERROR);
				//throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
				log.error("", e);
			} catch (Exception e) {
				DaoManager.closeCursor(cursor);
				DaoManager.rollback(tx);
				output.setStato(ConstantsJMS.STATO_ERROR);
				//throw new EJBException(e);
				log.error("", e);
			}

		} finally {
			completeReportWrite(output, report, totale);
		}

		return output;

	}

	private TipoEsito loopInvio(Tbl_richiesta_servizio req, Tbl_solleciti sollecito,
			StringBuilder report, Timestamp scadenza, String firmaUtente,
			List<ElementoStampaSollecitoVO> listaSollecitiPerStampa)
			throws Exception {
		TipoEsito esito = TipoEsito.NON_SOLLECITATO;
		int retries = 0;
		while (esito == TipoEsito.NON_SOLLECITATO && retries < MAX_RETRIES) {

			BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
			retries++;

			//scelgo il tipo invio di questo sollecito incrociando la conf. di biblioteca
			//con le caratteristiche dell'utente
			TipoInvioSollecito tipoInvio = scegliTipoInvioSollecito(req, sollecito, retries);
			switch (tipoInvio) {
			case ERRORE:
				log.error("Errore sollecito su richiesta n. " + req.getCod_rich_serv());
				ElementoStampaSollecitoVO soll = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, sollecito,
								"Tipo Invio non trovato");
				writeSollecitiReportRow(report, soll, retries);
				continue;
			case NON_TROVATO:
				log.error("Non trovato tipo invio compatibile con impostazioni utente per richiesta n. " + req.getCod_rich_serv());
				ElementoStampaSollecitoVO soll2 = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, sollecito,
								"Tipo Invio non previsto per l'utente");
				writeSollecitiReportRow(report, soll2, retries);
				continue;
			default:
				break;
			}

			esito = invioSollecito(req, sollecito, scadenza, firmaUtente, tipoInvio, report, listaSollecitiPerStampa, retries);
		}

		//almaviva5_20160308 serviziILL
		//in caso di sollecito su una richiesta ill (da fornitrice) si informa il Server ILL
		//con un sollecito di restituzione alla bib. richiedente (F12D).
		if (esito == TipoEsito.SOLLECITATO)
			sollecitaRichiestaILL(req);

		return esito;
	}

	private void sollecitaRichiestaILL(Tbl_richiesta_servizio req) throws Exception {
		MovimentoVO mov = ServiziConversioneVO.daHibernateAWebMovimento(req, Locale.getDefault());
		if (!mov.isRichiestaILL())
			return;

		DatiRichiestaILLVO dati = mov.getDatiILL();
		if (!ILLConfiguration2.getInstance().isRichiestaSollecitabile(dati) )
			return;

		//prepara messaggio
		MessaggioVO msg = new MessaggioVO(dati);
		msg.setDataMessaggio(DaoManager.now());
		msg.setTipoInvio(TipoInvio.INVIATO);
		msg.setStato(StatoIterRichiesta.F12D_SOLLECITO_RESTITUZIONE_PRESTITO.getISOCode());

		//note da modello SMS
		ElementoStampaSollecitoVO soll = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, null, null);
		msg.setNote(SollecitiUtil.costruisciModelloSollecito(modello, TipoModello.SMS, soll, false));

		try {
			ILLAPDU response = ILLRequestBuilder.sollecitoRestituzione(mov, msg);
			ILLRequestBuilder.checkResponse(response);

			//aggiornamento richiesta ill
			dati.setCurrentState(StatoIterRichiesta.F12D_SOLLECITO_RESTITUZIONE_PRESTITO.getISOCode());
			dati.addUltimoMessaggio(msg);
			DomainEJBFactory.getInstance().getServizi().aggiornaRichiesta(richiesta.getTicket(), mov, mov.getIdServizio());

		} catch (Exception e) {
			log.error("Errore notifica server ILL per richiesta n. " + req.getCod_rich_serv(), e);
		}


	}

	private void completeReportWrite(ElaborazioniDifferiteOutputVo output,
			StringBuilder report, int totale) {

		try {
			writeSollecitiReportFooter(report, totale);

			//scrivo log su file
			String fileName = richiesta.getFirmaBatch() + ".htm";
			FileOutputStream out = new FileOutputStream(StampeUtil.getBatchFilesPath() + File.separator + fileName);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.append(report.toString());
			writer.flush();
			writer.close();

			output.addDownload(fileName, fileName);
		} catch (Exception e) {
			log.error("errore scrittura report", e);
		}
	}

	private ElaborazioniDifferiteOutputVo stampaSolleciti(ElaborazioniDifferiteOutputVo output, List<ElementoStampaSollecitoVO> listaSollecitiPerStampa) throws Exception {

		SBNStampeUtenti ejb = DomainEJBStampeFactory.getInstance().getSBNStampeUtenti();

		ParametriBatchSollecitiVO clone = richiesta.copy();
		clone.setOutput(output);
		clone.setListaSollecitiPerStampa(listaSollecitiPerStampa);

		return ejb.elaboraSolleciti(clone, blog);
	}

	private TipoEsito invioSollecito(Tbl_richiesta_servizio req, Tbl_solleciti sollecito,
			Timestamp scadenza, String firmaUtente,
			TipoInvioSollecito tipoInvio, StringBuilder buf,
			List<ElementoStampaSollecitoVO> listaSollecitiPerStampa, int retries) throws Exception {

		Tbl_solleciti prossimo = preparaProssimoSollecito(req, sollecito, scadenza, firmaUtente, tipoInvio);
		if (prossimo == NON_SOLLECITABILE) {// richiesta non sollecitabile
			// se parto da sintetica devo comunque ri-stampare la lettera
			if (richiesta.isRichiestaDaSintetica()) {
				ElementoStampaSollecitoVO ess = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, sollecito, "ristampato");
				writeSollecitiReportRow(buf, ess, retries);
				return (listaSollecitiPerStampa.add(new ElementoStampaSollecitoDecorator(ess) ) ? TipoEsito.SOLLECITATO : TipoEsito.NON_SOLLECITABILE);
			}

			ElementoStampaSollecitoVO ess = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, prossimo, "Non sollecitabile");
			if (sollecito != null)
				ess.setNumSollecito( (sollecito.getProgr_sollecito() + 1) + "");

			writeSollecitiReportRow(buf, ess, retries);
			return TipoEsito.NON_SOLLECITABILE;
		}

		boolean esito = false;

		switch(tipoInvio) {
		case SMS_FISSO:
		case SMS_MOBILE:
			esito = inviaSollecitoSMS(req, prossimo);
			break;

		case MAIL:
			esito = inviaSollecitoMail(req, prossimo);
			break;

		case LETTERA:
			//aggiungi a lista di stampa
			ElementoStampaSollecitoVO ess = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, prossimo, null);
			esito = listaSollecitiPerStampa.add(new ElementoStampaSollecitoDecorator(ess));
			break;
		default:
			break;
		}

		prossimo.setEsito(esito ? 'S' : 'N');
		if (esito)
			try {
				if (sollecito != null)
					sollecitiDAO.deleteSollecito(sollecito, firmaUtente);
				else {
					//almaviva5_20121019 #5154 se, per la richiesta in esame, esistono solo solleciti cancellati
					//l'invio del nuovo sollecito fallisce.
					Tbl_solleciti old = sollecitiDAO.getSollecitoById(prossimo.getCod_rich_serv(), prossimo.getProgr_sollecito());
					if (old != null) {
						prossimo.setUte_ins(old.getUte_ins());
						prossimo.setTs_ins(old.getTs_ins());
						prossimo.setTs_var(old.getTs_var());
					}
				}
				sollecitiDAO.saveSollecito(prossimo);

			} catch (Exception e) {
				//almaviva5_20110316 segnalazione MO1: errore in salvataggio sollecito.
				ElementoStampaSollecitoVO ess = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, prossimo, "Non inviato");
				writeSollecitiReportRow(buf, ess, retries);
				if (tipoInvio == TipoInvioSollecito.LETTERA) //cancello la lettera già preparata
					listaSollecitiPerStampa.remove(new ElementoStampaSollecitoDecorator(ess) );

				throw e;
			}

		ElementoStampaSollecitoVO elem = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, prossimo, null);
		writeSollecitiReportRow(buf, elem, retries);

		return esito ? TipoEsito.SOLLECITATO : TipoEsito.NON_SOLLECITATO;
	}

	private boolean inviaSollecitoMail(Tbl_richiesta_servizio req, Tbl_solleciti prossimo) {
		try {
			AddressPair pair = util.getMailBiblioteca(richiesta.getCodPolo(), richiesta.getCodBib());
			InternetAddress uteMail = util.getMailUtenteMovimento(req);
			if (uteMail ==  null) {
				log.error("Indirizzo mail non impostato per utente " + req.getId_utenti_biblioteca().getId_utenti().getCod_utente());
				return false;
			}

			MailVO mail = new MailVO();
			mail.setFrom(pair.getFrom());
			mail.getReplyTo().add(pair.getReplyTo());
			mail.getTo().add(uteMail);

			//almaviva5_20111122 nome bib su oggetto mail
			StringBuilder subject = new StringBuilder();
			subject.append("Sollecito ")
				.append(prossimo.getProgr_sollecito())
				.append(" per richiesta ")
				.append(richiesta.getCodPolo())
				.append(richiesta.getCodBib())
				.append(' ')
				.append(req.getCod_rich_serv());
			mail.setSubject(subject.toString());

			//almaviva5_20150609 evolutiva solleciti
			//mail.setBody(preparaMailBody(req, prossimo) );
			try {
				ElementoStampaSollecitoVO ess = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, prossimo, null);
				String body = SollecitiUtil.costruisciModelloSollecito(modello, TipoModello.EMAIL, ess, true);
				mail.setBody(body);
				mail.setType(MailUtil.MIME_TYPE_HTML);
			} catch (Exception e) {
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_MODELLO_SOLLECITO);
			}

			log.debug(String.format("invio email sollecito a indirizzo: '%s'", uteMail) );
			return MailUtil.sendMail(mail);

		} catch (Exception e) {
			log.error("Errore invio mail sollecito per richiesta n. " + req.getCod_rich_serv(), e);
			return false;
		}
	}

	protected String preparaMailBody(Tbl_richiesta_servizio req, Tbl_solleciti prossimo) throws Exception {

		ElementoStampaSollecitoVO soll = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, prossimo, null);
		StringBuilder body = new StringBuilder();
		//almaviva5_20110802 #4447
		//body.append("Spett.le signore/a").append("\n");
		body.append(intestazione.get(soll.getTipo())).append("\n");
		body.append(soll.getNomeUtente()).append("\n\n");
		body.append("Si sollecita la restituzione del documento:").append("\n\n");
		body.append(soll.getTitolo()).append("\n\n"); //<area del titolo e dell'indicazione di responsabilità; area della pubblicazione>

		String volume = soll.getVolume();
		if (ValidazioneDati.isFilled(volume) )
			body.append("Volume: ").append(volume).append("\t");
		String anno = soll.getAnno();
		if (ValidazioneDati.isFilled(anno) )
			body.append("Anno: ").append(anno);
		if (ValidazioneDati.isFilled(volume) || ValidazioneDati.isFilled(anno) )
			body.append("\n");

		body.append("Inventario: ").append(soll.getInventario())
			.append("\t").append("Collocazione: ").append(soll.getSegnatura())
			.append("\n\n");

		body.append("Il prestito è scaduto il ").append(soll.getDataScadenza());

		body.append("\n\n").append("Si prega di restituire il documento al più presto.").append("\n");
		body.append("In caso di avvenuta restituzione, ignorare il presente sollecito.");

		//almaviva5_20110405 #4341
		body.append("\n\n").append("Attenzione: Questo è un messaggio generato automaticamente.").append("\n");
		body.append("Si prega pertanto di non rispondere alla casella mittente.");

		//seguito da <vol.> e <anno> (presi dalla richiesta); <inventario> e <collocazione>. Il prestito è scaduto il ... "
		return body.toString();
	}


	private boolean inviaSollecitoSMS(Tbl_richiesta_servizio req, Tbl_solleciti prossimo) {
		try {
			Tbl_utenti utente = req.getId_utenti_biblioteca().getId_utenti();

			char tipoSMS = utente.getAllinea();
			String numTel = tipoSMS == UtenteBibliotecaVO.SMS_SU_MOBILE ? utente.getTel_dom().trim() : utente.getTel_res().trim();

			ElementoStampaSollecitoVO soll = ConversioneHibernateVO.toWeb().elementoStampaSollecito(req, prossimo, null);

			//almaviva5_20150610 evolutiva solleciti
			String sms;
			try {
				sms = SollecitiUtil.costruisciModelloSollecito(modello, TipoModello.SMS, soll, false);
			} catch (Exception e) {
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_MODELLO_SOLLECITO);
			}

			SMSResult result = SMSUtil.send(richiesta.getCodPolo() + richiesta.getCodBib(), numTel, sms, false);
			if (!result.isSuccess())
				log.error("Errore invio SMS sollecito per richiesta n. " + req.getCod_rich_serv() + ": " + result.getMessage());

			return result.isSuccess();

		} catch (Exception e) {
			log.error("Errore invio SMS sollecito per richiesta n. " + req.getCod_rich_serv(), e);
			return false;
		}
	}

	private TipoInvioSollecito scegliTipoInvioSollecito(Tbl_richiesta_servizio req, Tbl_solleciti sollecito, int retries) {

		short numSollecito = sollecito != null ? sollecito.getProgr_sollecito() : 1; // primo sollecito
		//se l'ultimo invio é stato OK devo preparare il sollecito successivo
		if (sollecito != null)
			numSollecito += sollecito.getEsito() == 'S' ? 1 : 0;

		Tbl_utenti utente = req.getId_utenti_biblioteca().getId_utenti();
		TipoInvioSollecito tipoInvioUtente = TipoInvioSollecito.NON_TROVATO;
		// tipo invio definito per l'n-simo sollecito in biblioteca
		String[] tipoInvioBib = getTipoInvioBiblioteca(numSollecito);

		//la var. retries indica quale delle 3 modalità di bib deve essere controllata
		for (int idx = (retries - 1); idx < MAX_RETRIES; idx++) {

			String tipoInvio = tipoInvioBib[idx];
			if (!ValidazioneDati.isFilled(tipoInvio) )	// tipo invio non definito, imposto lettera come default
				return TipoInvioSollecito.LETTERA;

			// incrocio la conf. di biblio con i dati dell'utente
			tipoInvioUtente = getTipoInvioUtente(tipoInvio, utente);
			// se non ho trovato corrispondenza continuo col prossimo tipo invio definito
			// in biblioteca
			if (tipoInvioUtente != TipoInvioSollecito.NON_TROVATO)
				break;
		}

		return tipoInvioUtente;
	}

	private TipoInvioSollecito getTipoInvioUtente(String tipoInvio, Tbl_utenti utente) {

		switch (tipoInvio.charAt(0)) {
		case SMS:
			// vari tipi di sms per utente
			Character tipoSMS = utente.getAllinea();
			switch (tipoSMS) {
			case UtenteBibliotecaVO.SMS_SU_MOBILE:
				return ValidazioneDati.isFilled(utente.getTel_dom()) ? TipoInvioSollecito.SMS_MOBILE
						: TipoInvioSollecito.NON_TROVATO;
			case UtenteBibliotecaVO.SMS_SU_FISSO:
				return ValidazioneDati.isFilled(utente.getTel_res()) ? TipoInvioSollecito.SMS_FISSO
						: TipoInvioSollecito.NON_TROVATO;
			case UtenteBibliotecaVO.NO_SMS:
				return TipoInvioSollecito.NON_TROVATO;
			}
			return TipoInvioSollecito.ERRORE;

		case MAIL:
			return ValidazioneDati.isFilled(ConversioneHibernateVO.toWeb().getEmailUtente(utente)) ? TipoInvioSollecito.MAIL
					: TipoInvioSollecito.NON_TROVATO;
		case LETTERA:
			return ValidazioneDati.isFilled(utente.getIndirizzo_dom())
				|| ValidazioneDati.isFilled(utente.getIndirizzo_res()) ? TipoInvioSollecito.LETTERA
					: TipoInvioSollecito.NON_TROVATO;
		}

		return TipoInvioSollecito.ERRORE;
	}

	private String[] getTipoInvioBiblioteca(short numSollecito) {

		String[] tipoInvioBib = null;
		switch (numSollecito) {
		case 1:
			tipoInvioBib = new String[] {
					parametriBib.getCodModalitaInvio1Sollecito1(),
					parametriBib.getCodModalitaInvio2Sollecito1(),
					parametriBib.getCodModalitaInvio3Sollecito1() };
			break;
		case 2:
			tipoInvioBib = new String[] {
					parametriBib.getCodModalitaInvio1Sollecito2(),
					parametriBib.getCodModalitaInvio2Sollecito2(),
					parametriBib.getCodModalitaInvio3Sollecito2() };
			break;
		case 3:
			tipoInvioBib = new String[] {
					parametriBib.getCodModalitaInvio1Sollecito3(),
					parametriBib.getCodModalitaInvio2Sollecito3(),
					parametriBib.getCodModalitaInvio3Sollecito3() };
			break;
		}
		return tipoInvioBib;
	}

	private Tbl_solleciti preparaProssimoSollecito(Tbl_richiesta_servizio req, Tbl_solleciti sollecito,
			Timestamp dataScadenza, String user, TipoInvioSollecito tipoInvio)
			throws DaoManagerException, DAOConcurrentException {

		Date dataRitardo = null;
		Timestamp now = DaoManager.now();

		//se l'ultimo invio era andato male riciclo il sollecito
		Tbl_solleciti old = sollecito != null && sollecito.getEsito() != 'S' ? sollecito : null;
		Timestamp data_fine_prev = req.getData_fine_prev();

		// Sollecito 1
		dataRitardo = DateUtil.addDay(dataScadenza, -(parametriBib.getGgRitardo1()));

		if (sollecito == null && parametriBib.getNumeroLettere() > 0
				&& data_fine_prev.before(dataRitardo)) {

			sollecito = preparaRigaSollecito(old, 1, req, user, now, tipoInvio, 'S');
			return sollecito;
		}

		// Sollecito 2
		dataRitardo = DateUtil.addDay(dataScadenza, -(parametriBib.getGgRitardo2() + parametriBib.getGgRitardo1()));

		if (sollecito != null
				&& sollecito.getProgr_sollecito() == 1
				&& parametriBib.getNumeroLettere() > sollecito.getProgr_sollecito()
				&& data_fine_prev.before(dataRitardo)) {

			//preparo sollecito 2
			sollecito = preparaRigaSollecito(old, 2, req, user, now, tipoInvio, 'S');
			return sollecito;
		}

		// Sollecito 3
		dataRitardo = DateUtil.addDay(dataScadenza, -(parametriBib.getGgRitardo3()
				+ parametriBib.getGgRitardo2() + parametriBib.getGgRitardo1()));

		if (sollecito != null
				&& sollecito.getProgr_sollecito() == 2
				&& parametriBib.getNumeroLettere() > sollecito.getProgr_sollecito()
				&& data_fine_prev.before(dataRitardo)) {

			//inserisco sollecito 3
			sollecito = preparaRigaSollecito(old, 3, req, user, now, tipoInvio, 'S');
			return sollecito;
		}

		log.debug("Richiesta n. " + req.getCod_rich_serv()	+ " non sollecitata.");
		//almaviva5_20121024 #5154
		sollecito = preparaRigaSollecito(NON_SOLLECITABILE, 0, req, user, now, tipoInvio, 'N');
		return sollecito;
	}

	private Tbl_solleciti preparaRigaSollecito(Tbl_solleciti old, int progr,
			Tbl_richiesta_servizio req, String user, Timestamp now,
			TipoInvioSollecito tipoInvio, char esito) {

		Tbl_solleciti sollecito;
		if (old != null)
			sollecito = old;
		else { //nuovo sollecito
			sollecito = new Tbl_solleciti();
			sollecito.setCod_rich_serv(req);
			sollecito.setProgr_sollecito((short) progr);
			sollecito.setUte_ins(user);
			sollecito.setTs_ins(now);
		}

		StringBuilder note = new StringBuilder();
		note.append("Invio con ");

		switch (tipoInvio) {
		case LETTERA:
			sollecito.setTipo_invio(LETTERA);
			note.append("lettera");
			break;
		case SMS_FISSO:
		case SMS_MOBILE:
			sollecito.setTipo_invio(SMS);
			note.append("sms");
			break;
		case MAIL:
			sollecito.setTipo_invio(MAIL);
			note.append("e-mail");
			break;
		default:
			break;
		}

		sollecito.setEsito(esito);
		note.append("; richiesta batch n. ").append(richiesta.getIdBatch());
		sollecito.setNote(note.toString());
		sollecito.setData_invio(now);
		sollecito.setUte_var(user);
		sollecito.setFl_canc('N');

		return sollecito;
	}

	private void writeSollecitiReportHeader(StringBuilder buf, String descrBib) {

		// if (header) { //inizio report html
		buf.append("<html>");
		buf.append("<head>");
		buf.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		buf.append("<title>Solleciti per Richieste Scadute</title>");
		buf.append("</head>");
		buf.append("<body>");

		buf.append("<h2>SOLLECITI PER RICHIESTE SCADUTE</h2>");
		buf.append("<h3>Biblioteca:&nbsp;").append(descrBib).append("</h3>");
		buf.append("<h4>Data:&nbsp;").append(DateUtil.formattaData(System.currentTimeMillis())).append("</h4>");
		buf.append("<hr/><br/>");

		buf.append("<table width=\"90%\" border=\"1\">");

		buf.append("<tr>");
		buf.append("<th>n.ro</th>");
		buf.append("<th>Scadenza</th>");
		buf.append("<th>Documento</th>");
		buf.append("<th>Titolo</th>");
		buf.append("<th>cod. Utente</th>");
		buf.append("<th>n.ro Sollecito</th>");
		buf.append("<th>Tipo Invio</th>");
		buf.append("<th>Esito</th>");
		buf.append("</tr>");
	}

	private void writeSollecitiReportRow(StringBuilder buf,
			ElementoStampaSollecitoVO soll, int retries) {

		buf.append("<tr align=\"left\" >");
		buf.append("<td>").append(soll.getIdRichiesta()).append("</td>");
		if (retries == 1) {
			buf.append("<td>").append(soll.getDataScadenza()).append("</td>");
			buf.append("<td>").append(soll.getSegnatura()).append("</td>");
			buf.append("<td>").append(soll.getTitolo()).append("</td>");
			buf.append("<td>").append(soll.getCodUtente()).append("</td>");
			buf.append("<td>").append(soll.getNumSollecito()).append("</td>");
		} else
			buf.append("<td colspan=\"5\">&nbsp;</td>");

		buf.append("<td>").append(soll.getTipoInvio()).append("</td>");
		buf.append("<td>").append(soll.getEsito()).append("</td>");
		buf.append("</tr>");
	}

	private void writeSollecitiReportFooter(StringBuilder buf, int totale) {

		// fine report html
		buf.append("</table>");
		buf.append("<br/>");
		buf.append("<h4>Solleciti inviati:&nbsp;").append(totale).append("</h4>");
		buf.append("</body>");
		buf.append("</html>");
	}

}
