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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AggiornaDirittiUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.AutorizzazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.ServiziDAO;
import it.iccu.sbn.persistence.dao.servizi.Trl_diritti_utenteDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_autorizzazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_diritti_utente;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

public class BatchAggiornamentoDirittiUtente {

	private final AggiornaDirittiUtenteVO richiesta;
	private final UserTransaction tx;
	private final Logger log;
	private final String firmaUtente;

	private AutorizzazioniDAO autDAO = new AutorizzazioniDAO();
	private ServiziDAO srvDAO = new ServiziDAO();
	private UtentiDAO uteDAO = new UtentiDAO();
	private Trl_diritti_utenteDAO dirDAO = new Trl_diritti_utenteDAO();

	public BatchAggiornamentoDirittiUtente(UserTransaction tx,
			AggiornaDirittiUtenteVO richiesta, BatchLogWriter blog) {
		this.tx = tx;
		this.richiesta = richiesta;
		this.log = blog.getLogger();
		//almaviva5_20131011 #5416
		this.firmaUtente = DaoManager.getFirmaUtente(richiesta.getTicket());
	}

	private Set<Integer> getIdServiziAutGiaAssociati(List<ElementoSinteticaServizioVO> autsrv) {
		Set<Integer> idServizi = new HashSet<Integer>();
		Iterator<ElementoSinteticaServizioVO> i = autsrv.iterator();
		while (i.hasNext())
			idServizi.add(i.next().getIdServizio());

		return idServizi;
	}

	private Set<Integer> getIdServiziDaAssociare(List<ElementoSinteticaServizioVO> servizi) {
		Set<Integer> idServizi = new HashSet<Integer>();

		Iterator<ElementoSinteticaServizioVO> i = servizi.iterator();
		while (i.hasNext())
			idServizi.add(i.next().getIdServizio());

		return idServizi;
	}

	public AggiornaDirittiUtenteVO eseguiBatch() {

		Calendar now = Calendar.getInstance();
		AggiornaDirittiUtenteVO output = richiesta.copy();

		try {
			try {
				DaoManager.begin(tx);
				AutorizzazioneVO aut = richiesta.getAutorizzazione();
				Tbl_tipi_autorizzazioni autorizzazione = ServiziConversioneVO.daWebAHibernateAnagrafeAutorizzazioni(aut);

				List<ElementoSinteticaServizioVO> listaServizi = aut.getElencoServizi();

				try {
					List<ElementoSinteticaServizioVO> serviziAutGiaPresenti = this.getListaServiziAutorizzazione(aut.getCodPolo(),aut.getCodBiblioteca(),aut.getCodAutorizzazione());
					Set<Integer> idServiziGiaAss = getIdServiziAutGiaAssociati(serviziAutGiaPresenti);
					Set<Integer> idServiziDaAss = getIdServiziDaAssociare(aut.getElencoServizi());

					// Scorro lista servizi da inserire
					for (ElementoSinteticaServizioVO srv : listaServizi) {

						if (!idServiziGiaAss.contains(srv.getIdServizio())) {
							DaoManager.begin(tx);
							// Si tratta di un nuovo servizio. Va inserito
							Tbl_servizio tbl_servizio = srvDAO.getServizioBiblioteca(srv.getCodPolo(),
									srv.getCodBiblioteca(), srv.getTipServizio(), srv.getCodServizio());
							Trl_autorizzazioni_servizi diritto =
								autDAO.getServizioAutorizzazione(
											srv.getCodPolo(),
											srv.getCodBiblioteca(),
											aut.getIdAutorizzazione(),
											srv.getIdServizio(),
											aut.getCodAutorizzazione(), 'S');
							if (diritto != null) {
								diritto.setFl_canc('N');
								diritto.setUte_var(firmaUtente);
								autDAO.aggiornamentoAutorizzazioneServizio(diritto);
							} else {
								Trl_autorizzazioni_servizi aut_srv = new Trl_autorizzazioni_servizi();
								aut_srv.setFl_canc('N');
								aut_srv.setTs_ins(aut.getTsIns());
								aut_srv.setUte_ins(firmaUtente);
								aut_srv.setUte_var(firmaUtente);
								aut_srv.setId_servizio(tbl_servizio);
								aut_srv.setId_tipi_autorizzazione(autorizzazione);

								autDAO.inserimentoAutorizzazioneServizio(aut_srv);
							}
							output.getErrori().add("ELABORAZIONE: aggiunto il diritto: '" + tbl_servizio.getCod_servizio() + "' all'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "'");

							// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI CON GLI UTENTI
							// LEGGO Trl_diritti_utente CON ID SERVIZIO
							// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESO COD_AUTORIZZAZIONE E inserisco I DIRiTTI
							List<Integer> listaUtenti = uteDAO.getListaIdUtentiByAut(srv.getCodPolo(), srv.getCodBiblioteca(), aut.getCodAutorizzazione() );
							DaoManager.commit(tx);

							loopUtente(log, now, output, aut, tbl_servizio, srv, listaUtenti);
						}
					}

					// Scorro lista servizi gia associati per stabilire se qualche
					// servizio va cancellato

					loopServizio(log, output, aut, idServiziDaAss, serviziAutGiaPresenti);
					output.getErrori().add("OK. L'aggiornamento dei diritti degli utenti associati all'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "' è andata a buon fine");
					DaoManager.commit(tx);

					BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());

				} catch (DaoManagerException e) {
					output.getErrori().add("ERRORE. L'aggiornamento dei diritti degli utenti associati all'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "' non è andato a buon fine.");
					log.error("", e);
					DaoManager.rollback(tx);
					throw new ApplicationException(e);
				}

			  } catch (Exception e) {
				  output.getErrori().add("ERRORE. L'aggiornamento dei diritti degli utenti associati all'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "' non è andato a buon fine.");
					log.error("", e);
					DaoManager.rollback(tx);
			  }

		} catch (Exception e) {
			  	output.getErrori().add("ERRORE. L'aggiornamento dei diritti degli utenti associati all'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "'  non è andato a buon fine.");
				log.error("", e);

		}
		return output;

	}

	private void loopServizio(Logger log, AggiornaDirittiUtenteVO output,
			AutorizzazioneVO aut, Set<Integer> idServiziDaAss,
			List<ElementoSinteticaServizioVO> serviziAutGiaPresenti)
			throws Exception, DaoManagerException {


		for (ElementoSinteticaServizioVO ess : serviziAutGiaPresenti) {
			DaoManager.begin(tx);

			if (!idServiziDaAss.contains(ess.getIdServizio())) {
				// L'associazione è da cancellare in quanto non più
				// contenuta tra i servizi da associare
				Trl_autorizzazioni_servizi aut_srv = autDAO.getServizioAutorizzazione(
						ess.getCodPolo(),
						ess.getCodBiblioteca(),
						aut.getIdAutorizzazione(),
						ess.getIdServizio(),
						aut.getCodAutorizzazione(), 'N');
				if (aut_srv == null) {
					DaoManager.rollback(tx);
					output.getErrori().add("ERRORE. Diritto: '" + ess.getDesServizio() + "' dall'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "' non trovato.");
					continue;
				}

				aut_srv.setFl_canc('S');
				aut_srv.setUte_var(firmaUtente);
				autDAO.aggiornamentoAutorizzazioneServizio(aut_srv);
				output.getErrori().add("ELABORAZIONE: cancellato il diritto: '" + aut_srv.getId_servizio().getDescr() + "' dall'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "'");

				// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI CON GLI UTENTI
				// LEGGO Trl_diritti_utente CON ID SERVIZIO
				// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESO COD_AUTORIZZAZIONE E FLAGGO I DIRiTTI

				// N.B. poichè tale cancellazione impatta solo su quelli ereditati (perchè hanno cod_aut valorizzato) lascia inalterati i personalizzati
				// RICHIESTA CONTARDI DEL 03.05.10 - vince sempre il diritto personalizzato rispetto a quello ereditato per la stessa famiglia di servizio
				// anche nel caso di cancellazione di diritti, ma questa parte del sw dovrebbe andare bene come è


				List<Integer[]> diritti = dirDAO.getIdDirittiUtentePerAut(aut_srv.getId_servizio().getId_tipo_servizio().getCd_bib(),
								aut_srv.getId_servizio_id_servizio(),
								aut.getCodAutorizzazione(), false);
				DaoManager.commit(tx);

				if(ValidazioneDati.isFilled(diritti) ) {
					// si devono flaggare i diritti_utente degli utenti con la specifica autorizzazione
					for (int i = 0; i < diritti.size(); i++) {
						Object[] key = diritti.get(i);
						DaoManager.begin(tx);
						Trl_diritti_utente diritto = dirDAO.getDirittoUtenteByID((Integer)key[0], (Integer)key[1]);
						if (diritto == null) {
							DaoManager.rollback(tx);
							output.getErrori().add("ERRORE. L'aggiornamento dei diritti dell'utente id. '" + key[0] + "' non è andato a buon fine.");
							continue;
						}
						diritto.setFl_canc('S');
						diritto.setUte_var(firmaUtente);
						uteDAO.aggiornaDirittoUtente(diritto);
						DaoManager.commit(tx);
					}
					output.getErrori().add("ELABORAZIONE: cancellati per tutti gli utenti il diritto: '" + aut_srv.getId_servizio().getDescr() + "' rimosso dall'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "'");
				}

			    try {
			    	DaoManager.begin(tx);

					// RICHIESTA CONTARDI DEL 03.05.10: Segnalazione di non cancellazione per gli utenti con diritti personalizzati afferenti alla stessa famiglia di servizio
					List<Trl_diritti_utente> idUtenti = uteDAO
							.verificaEsistenzaServizioDirittoUtenteBibliotecaSameFamily(
									aut_srv.getId_servizio().getId_tipo_servizio().getCd_bib(),
									aut_srv.getId_servizio_id_servizio(),
									aut.getCodAutorizzazione(),
									aut_srv.getId_servizio().getId_tipo_servizio().getId_tipo_servizio());

			    	//output.getErrori().add("ELABORAZIONE: UTENTE: '"+ute_single.getCod_utente() + "' mantenuto il diritto: '" + dirittoEsistente.getId_servizio().getDescr() + "' afferente allo stesso servizio:'"+ tbl_servizio.getId_tipo_servizio().getCod_tipo_serv()+"' del diritto da eliminare per l'autorizzazione: '" + aggDirUteVO.getAutorizzazione().getCodAutorizzazione() + "'");
					if (ValidazioneDati.isFilled(idUtenti) ) {
						StringBuilder buf = new StringBuilder(1024);
						for (int u = 0; u < idUtenti.size(); u++) {
							Trl_diritti_utente eleUte = idUtenti.get(u);
							buf.setLength(0);
							buf.append("ELABORAZIONE: UTENTE: ");

							Tbl_utenti utente = eleUte.getId_utenti();
							buf.append("'").append(utente.getCod_utente().trim()).append("'");

							Tbl_servizio servizio = eleUte.getId_servizio();
							buf.append(" mantenuto il diritto ").append(servizio.getDescr().trim()).append("'");
							buf.append(" afferente allo stesso servizio: ").append(servizio.getId_tipo_servizio().getCod_tipo_serv()).append("'");
							buf.append("' del diritto da eliminare per l'autorizzazione: '").append(richiesta.getAutorizzazione().getCodAutorizzazione()).append("'");

							output.getErrori().add(buf.toString());
						}
					}

					DaoManager.commit(tx);

				} catch (Exception e) {
					log.error("", e);
				}
			} else {
				//*************************************************************************************************************************
				// TALE CONDIZIONE ALTERNATIVA E' STATA AGGIUNTA TRANSITORIAMENTE PER ADEGUARE RAPIDAMENTE IL CAMPO COD_TIP_AUT SE EREDITATO
				// POTRA' ESSERE RIMOSSA A REGIME
				//*************************************************************************************************************************
				// L'associazione è già presente
				// aggiorno solo il codice autorizzazione
				// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI CON GLI UTENTI
				// LEGGO Trl_diritti_utente CON ID SERVIZIO
				// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESSO COD_AUTORIZZAZIONE
				DaoManager.begin(tx);
				Trl_autorizzazioni_servizi aut_srv = autDAO.getServizioAutorizzazione(
						ess.getCodPolo(),
						ess.getCodBiblioteca(),
						aut.getIdAutorizzazione(),
						ess.getIdServizio(),
						aut.getCodAutorizzazione(), 'N');

				List<Integer[]> diritti = dirDAO.getIdDirittiUtentePerAut(aut_srv.getId_servizio().getId_tipo_servizio().getCd_bib(),
						aut_srv.getId_servizio_id_servizio(),
						aut.getCodAutorizzazione(), false);
				DaoManager.commit(tx);
				if (ValidazioneDati.isFilled(diritti) ) {
					for (int i = 0; i < diritti.size(); i++) {
						DaoManager.begin(tx);
						Object[] key = diritti.get(i);
						Trl_diritti_utente diritto = dirDAO.getDirittoUtenteByID((Integer)key[0], (Integer)key[1]);
						if (diritto == null) {
							DaoManager.rollback(tx);
							output.getErrori().add("ERRORE. L'aggiornamento dei diritti dell'utente id. '" + key[0] + "' non è andato a buon fine.");
							continue;
						}
						diritto.setUte_var(firmaUtente);
						diritto.setCod_tipo_aut(aut.getCodAutorizzazione().trim()); //20.01.10
						uteDAO.aggiornaDirittoUtente(diritto);
						DaoManager.commit(tx);
					}
				}
			}

		}
	}

	private List<ElementoSinteticaServizioVO> getListaServiziAutorizzazione(
			String codPolo, String codBib, String codAut) throws DaoManagerException {

		List<ElementoSinteticaServizioVO> output = new ArrayList<ElementoSinteticaServizioVO>();

		List<Trl_autorizzazioni_servizi> listSrvAut = autDAO.getListaServiziAutorizzazione(codPolo, codBib, codAut);
		for (Trl_autorizzazioni_servizi aut_srv : listSrvAut)
			output.add(ServiziConversioneVO.daHibernateAWebServizioAutorizzazione(aut_srv, 0) );

		return output;
	}

	private void loopUtente(Logger log, Calendar now,
			AggiornaDirittiUtenteVO output, AutorizzazioneVO aut,
			Tbl_servizio tbl_servizio, ElementoSinteticaServizioVO srv,
			List<Integer> listaUtenti) throws Exception {

		for (int idUtente : listaUtenti) {

			DaoManager.begin(tx);
			Tbl_utenti utente = uteDAO.getUtenteAnagraficaById(idUtente);
			if (utente == null) {
				DaoManager.rollback(tx);
				output.getErrori().add("ERRORE. L'aggiornamento dei diritti dell'utente id. '" + idUtente + "' non è andato a buon fine.");
				continue;
			}

			Date dataInizioAut = now.getTime();
			Date dataFineAut = null;
			Date dataSospInizio = null;
			Date dataSospFine = null;

			//ricerca della data di fine autorizzazione
			if(utente.getTrl_utenti_biblioteca() != null)
				try {
					Object[] elencoUteBibl = utente.getTrl_utenti_biblioteca().toArray();
					for (int j = 0; j < elencoUteBibl.length; j++) {
						Trl_utenti_biblioteca uteBib = (Trl_utenti_biblioteca) elencoUteBibl[j];
						if (uteBib.getCod_tipo_aut().trim().equals(aut.getCodAutorizzazione().trim())
							&& uteBib.getCd_biblioteca().getCd_biblioteca().equals(srv.getCodBiblioteca())) {
							//almaviva5_20110314 #4297
							dataFineAut = uteBib.getData_fine_aut() != null ? uteBib.getData_fine_aut() : DateUtil.lastDayOfYear(now.get(Calendar.YEAR));
							dataSospInizio = uteBib.getData_inizio_sosp();
							dataSospFine = uteBib.getData_fine_sosp();

							//se l'utente é già scaduto imposto la data inizio del diritto
							//alla data di inizio aut per mantenere la coerenza con gli altri diritti
							if (dataFineAut.before(dataInizioAut))
								dataInizioAut = uteBib.getData_inizio_aut() != null ? uteBib.getData_inizio_aut() : utente.getData_reg();

							break;
						}
					}
				} catch (Exception e) {
					log.error("", e);
				}

			// controllo L'ESISTENZA di un record non cancellato che appartenga alla stessa famiglia del servizio a cui il diritto preso in considerazione appartiene
			Trl_diritti_utente diritto = uteDAO.verificaEsistenzaServizioDirittoUtenteBiblioteca(utente, tbl_servizio);
			if (diritto != null) {
				// esiste un diritto legato alla stessa famiglia del servizio
				if (diritto.getId_servizio_id_servizio() != tbl_servizio.getId_servizio() ) {
					// diritto da cancellare logicamente per inserire il nuovo diritto della stessa famiglia
					//cancellazione logica di quello trovato

					// RICHIESTA CONTARDI DEL 03.05.10 - vince sempre il diritto personalizzato rispetto a quello ereditato per la stessa famiglia di servizio
					// diritto della stessa famiglia già associato come personalizzazione: non faccio nulla
					output.getErrori().add("ELABORAZIONE: UTENTE: '"+utente.getCod_utente() + "' mantenuto il diritto: '" + diritto.getId_servizio().getDescr() + "' afferente allo stesso servizio:'"+ tbl_servizio.getId_tipo_servizio().getCod_tipo_serv()+"' del diritto da inserire per l'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "'");

				}
			}
			if (diritto == null) {// RICHIESTA CONTARDI DEL 03.05.10 (IN ASSENZA DI PERSONALIZZAZIONI)
				// controllo esistenza del nuovo diritto per verificare se presente ma cancellato logicamente
				diritto = uteDAO.verificaEsistenzaDirittoUtenteBiblioteca(utente, tbl_servizio);
				//diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte, diritto);
				//tbl_utenti utente, Tbl_servizio servizio, 	Trl_diritti_utente diritto_utente
				if (diritto == null) {
					diritto = new Trl_diritti_utente();
					diritto.setFl_canc('N');
					diritto.setTs_ins(aut.getTsIns());
					diritto.setUte_ins(firmaUtente);
					diritto.setUte_var(firmaUtente);

					diritto.setData_inizio_serv(dataInizioAut);
					diritto.setData_fine_serv(dataFineAut);
					diritto.setData_inizio_sosp(dataSospInizio);
					diritto.setData_fine_sosp(dataSospFine);

					diritto.setId_servizio_id_servizio(tbl_servizio.getId_servizio());
					diritto.setNote("");
					diritto.setCod_tipo_aut(aut.getCodAutorizzazione().trim()); //20.01.10
					uteDAO.inserisciDirittoUtente(utente ,tbl_servizio, diritto);

					output.getErrori().add("ELABORAZIONE: UTENTE: '"+ utente.getCod_utente() + "' aggiunto il diritto: '" + diritto.getId_servizio().getDescr() + "' ereditato dall'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "'");

				} else 	{
					if (diritto.getFl_canc() == 'S') {
						diritto.setFl_canc('N');
						// date da aggiornare
						diritto.setData_inizio_serv(dataInizioAut);
						diritto.setData_fine_serv(dataFineAut);
						diritto.setData_inizio_sosp(dataSospInizio);
						diritto.setData_fine_sosp(dataSospFine);

						diritto.setCod_tipo_aut(aut.getCodAutorizzazione().trim()); //20.01.10
						uteDAO.aggiornaDirittoUtente(diritto);

						output.getErrori().add("ELABORAZIONE: UTENTE: '"+utente.getCod_utente() + "' aggiunto il diritto: '" + diritto.getId_servizio().getDescr() + "' ereditato dall'autorizzazione: '" + richiesta.getAutorizzazione().getCodAutorizzazione() + "'");
					}
				}

			}
			DaoManager.commit(tx);
			BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
		}
	}

}
