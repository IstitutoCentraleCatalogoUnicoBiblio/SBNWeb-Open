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
package it.iccu.sbn.ejb.domain.servizi.batch;

import it.iccu.sbn.batch.servizi.BatchImportaUtenti;
import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi.AllineamentoBibliotecheILL;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi.BatchAggiornamentoDirittiUtente;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi.BatchSolleciti;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi.RifiutaPrenotazioniScadute;
import it.iccu.sbn.ejb.domain.servizi.esse3.csv.Esse3DataManager.Esse3OperationType;
import it.iccu.sbn.ejb.domain.servizi.esse3.csv.Esse3DataManagerBuilder;
import it.iccu.sbn.ejb.domain.servizi.esse3.csv.Esse3DataManagerImpl;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQAreeIsbdVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AggiornaDirittiUtenteVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ArchiviazioneMovLocVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriAllineamentoBibliotecheILLVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchImportaUtentiVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchSollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.batch.RifiutaPrenotazioniScaduteVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.AutorizzazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.SaleDAO;
import it.iccu.sbn.persistence.dao.servizi.ServiziDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_documenti_lettoriDAO;
import it.iccu.sbn.persistence.dao.servizi.Trl_diritti_utenteDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_prenotazione_posto;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_solleciti;
import it.iccu.sbn.polo.orm.servizi.Tbl_storico_richieste_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_autorizzazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_diritti_utente;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ConvertToWeb;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.logging.FileLog;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.vo.xml.binding.esse3.PERSONA;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;


public class ServiziBMTBean extends TicketChecker implements ServiziBMT {

	private static final long serialVersionUID = -6862576144461262964L;

	private static Logger log = Logger.getLogger(ServiziBMTBean.class);
	private SessionContext ctx;

	public void ejbCreate() {
		return;
	}

	private Acquisizioni getEjbAcq() throws Exception {
		return DomainEJBFactory.getInstance().getAcquisizioni();
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
	}

	public void ejbActivate() throws EJBException, RemoteException {
		return;
	}

	public void ejbPassivate() throws EJBException, RemoteException {
		return;
	}

	public void ejbRemove() throws EJBException, RemoteException {
		return;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ApplicationException {
		try {
			if (command == null)
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

			command.validate();
			log.info(command);
			//String ticket = command.getTicket();
			//checkTicket(ticket);

			switch (command.getCommand() ) {
			case BATCH_SOLLECITI:
				ParametriBatchSollecitiVO parametri = (ParametriBatchSollecitiVO) command.getParams()[0];
				ElaborazioniDifferiteOutputVo output = batchSolleciti(parametri);
				return CommandResultVO.build(command, output, null);

			default:
				break;
			}

			return null;

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);
		} catch (ValidationException e) {
			throw new ApplicationException(e.getErrorCode());
		} catch (Exception e) {
			log.error("", e);
			return CommandResultVO.build(command, null, e);
		}

	}

	private ElaborazioniDifferiteOutputVo batchSolleciti(
			ParametriRichiestaElaborazioneDifferitaVO parametri)
			throws ApplicationException {
		//UserTransaction tx = ctx.getUserTransaction();
		try {
			//BatchSolleciti batch = new BatchSolleciti(tx);
			//return batch.execute(parametri);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
		return null;
	}

	public ElaborazioniDifferiteOutputVo rifiutaPrenotazioniScadute(RifiutaPrenotazioniScaduteVO params, BatchLogWriter blw)
			throws SbnBaseException {
		RifiutaPrenotazioniScadute batch = new RifiutaPrenotazioniScadute(ctx.getUserTransaction(), params, blw);
		return batch.execute();
	}

	public RinnovoDirittiDiffVO gestioneDifferitaRinnovoDiritti(RinnovoDirittiDiffVO rinnDirVO ) throws ApplicationException
	{
		UserTransaction tx = ctx.getUserTransaction();
		Timestamp ts = DaoManager.now();

		RinnovoDirittiDiffVO output=rinnDirVO;
		try {
			boolean ret = false;
			UtentiDAO utentiDAO = new UtentiDAO();

			List listaDiritti;
			Trl_diritti_utente diritto;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			try {
				DaoManager.begin(tx);

				//checkTicket(rinnDirVO.getTicket());
				// Scorrro array codici utente
				if (rinnDirVO.getTipoRinnModalitaDiff().equals("T") || rinnDirVO.getTipoRinnModalitaDiff().equals("D")) // selezione rinnovo di autorizzazioni selezionate
				{
					// ciclo sulle autorizzazioni selezionate

					if (rinnDirVO.getElencoAutSel()!=null && rinnDirVO.getElencoAutSel().size()>0)
					{
						for (int r = 0; r < rinnDirVO.getElencoAutSel().size(); r++)
						{

							String dataRinnovoOpz2=rinnDirVO.getElencoAutSel().get(r).getDescrizione();
							String autorizzazioneInRinnovo=rinnDirVO.getElencoAutSel().get(r).getCodice();
							String messaggio="********************************************************";
							output.getErrori().add(messaggio);
							messaggio="";
							if (!ValidazioneDati.strIsNull(autorizzazioneInRinnovo))
							{
								messaggio=messaggio + "RINNOVO AUTORIZZAZIONE ";
								messaggio=messaggio+ "' "+autorizzazioneInRinnovo + "' ";
							}
							else
							{
								messaggio=messaggio + "RINNOVO UTENTI SENZA AUTORIZZAZIONE SPECIFICA ";
							}


							if (!ValidazioneDati.strIsNull(dataRinnovoOpz2))
							{
								messaggio=messaggio+ " ALLA DATA: " + dataRinnovoOpz2;
							}
							output.getErrori().add(messaggio);
							String parametroAut=autorizzazioneInRinnovo;
							if (ValidazioneDati.strIsNull(autorizzazioneInRinnovo))
							{
								parametroAut="*";
							}

							DaoManager.begin(tx);

							List allUteBiblAut=utentiDAO.getIdAllUtentiByAut(rinnDirVO.getCodPolo(), rinnDirVO.getCodBib(), parametroAut);
							// TODO mettere tutti gli id utente in rinnDirVO.getCodUtente()
							if (allUteBiblAut!=null && allUteBiblAut.size()>0)
							{
								rinnDirVO.setCodUtente(new String[allUteBiblAut.size()]);
								for (int y = 0; y < allUteBiblAut.size(); y++)
								{
									rinnDirVO.getCodUtente()[y]=String.valueOf(allUteBiblAut.get(y));
								}
							}
							// solo per test
							//rinnDirVO.setCodUtente(new String[1]);
							//rinnDirVO.getCodUtente()[0]="215";
							//rinnDirVO.getCodUtente()[1]="51";

							//RinnovoDirittiDiffVO output=this.subRinnovoDirittiDifferita( rinnDirVO,  output,  transaction,  utentiDAO,  format,  listaDiritti,  diritto,  dataRinnovoOpz2,  ts );

							for (int i = 0; i < rinnDirVO.getCodUtente().length; i++) {

								DaoManager.begin(tx);

								boolean uteOk=true;
								Date dataRinnovo;
								String dataFineAutUteOld="";
								// leggere utente, se non esiste messaggio
								Trl_utenti_biblioteca uteBibl=utentiDAO.getUtenteBibliotecaById(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
								String codUtente=rinnDirVO.getCodUtente()[i]; // id utente
								if (uteBibl.getId_utenti()!=null && uteBibl.getId_utenti().getCod_utente()!=null && uteBibl.getId_utenti().getCod_utente().trim().length()>0 )
								{
									codUtente=uteBibl.getId_utenti().getCod_utente().trim();
								}
								if (uteBibl!=null && uteBibl.getId_utenti_biblioteca()>0 && uteBibl.getFl_canc()!='S' )
								{

									try {
										dataRinnovo = format.parse(dataRinnovoOpz2);
										// controllare se la data di  rinnovo è inferiore a quella precedente e inferiore a quella corrente
										int ritorna=0;
										if (!ValidazioneDati.strIsNull(dataRinnovoOpz2)
												&& !ValidazioneDati.strIsNull(dateToString(Calendar.getInstance().getTime()))) {
											ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz2, dateToString(Calendar.getInstance().getTime()),"4");
											if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
												 uteOk=false;
												output.getErrori().add("ERRORE. Il rinnovo autorizzazione non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella odierna.");
												 DaoManager.rollback(tx);
												 break; // interruzione generale
											}
										}
										if (!ValidazioneDati.strIsNull(dataRinnovoOpz2)
												&& !ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut()))) {
											ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz2, dateToString(uteBibl.getData_fine_aut()),"4");
											if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
												 uteOk=false;
												output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella precedente.");
												 DaoManager.rollback(tx);
												 continue;
											}
										}

										if (!ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut())))
										{
											// salvo la vecchia data perchè verranno modificati solo i diritti
											// che hanno la  vecchia data di scadenza dell'autorizzazione
											dataFineAutUteOld=dateToString(uteBibl.getData_fine_aut());
											uteBibl.setData_fine_aut(dataRinnovo);
											uteBibl.setUte_var(rinnDirVO.getUser());
											uteBibl.setTs_var(ts);
											utentiDAO.updateUtentiBib(uteBibl);
										}
										else
										{
											uteOk=false;
											output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine per l'inesistenza di una precedente data di scadenza da rinnovare.");
											DaoManager.rollback(tx);
											continue;
										}


									  } catch (Exception e) {
										  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
										  DaoManager.rollback(tx);
										 uteOk=false;
			  							 log.error("", e);
										 continue;

									  }
									// ottengo la lista diritti utente associata all'utente corrente
									listaDiritti = utentiDAO.getListaDirittiUtente(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
									// ciclo sui diritti utente per aggiornarli
									Iterator iterator = listaDiritti.iterator();
									while (iterator.hasNext()) {
										diritto = (Trl_diritti_utente) iterator.next();
										int ritorna=0;
										if (!ValidazioneDati.strIsNull(dataRinnovoOpz2)
												&& !ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv()))) {
											ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz2, dateToString(diritto.getData_fine_serv()),"4");
											if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
												 uteOk=false;
												output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quelle esistenti sui diritti.");
												 DaoManager.rollback(tx);
												 break;
											}
										}
										dataRinnovo = format.parse(dataRinnovoOpz2);
										try {
											if (!ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv())))
											{
												int ritorno=0;
												ritorno = ValidazioneDati.confrontaLeDate(dataFineAutUteOld, dateToString(diritto.getData_fine_serv()),"2");
												// modifica dei soli diritti con data scad=dataFineAutUteOld
												if (ritorno == ValidazioneDati.DATA1_EQ_DATA2) {
													diritto.setData_fine_serv(dataRinnovo);
													diritto.setUte_var(rinnDirVO.getUser());
													diritto.setTs_var(ts);
													utentiDAO.aggiornaDirittoUtente(diritto);
												}
											}
		/*									else
											{
												uteOk=false;
												output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non è andato a buon fine per l'inesistenza sui diritti di una precedente data di scadenza da rinnovare.");
												transaction.rollback();
												break;
											}*/

										  } catch (Exception e) {
											  uteOk=false;
											log.error("", e);
											DaoManager.rollback(tx);
											break;
										  }
									}
									if (!uteOk)
									{
										  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
										//	transaction.rollback();
											 continue;
									}
									else
									{
										output.getErrori().add("OK. Per il codice utente : " + codUtente + " \u00E8 stata rinnovata l'autorizzazione");
										DaoManager.commit(tx);
									}

								}
								else
								{
									output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 inesistente.");
									DaoManager.rollback(tx);
									 continue;
								}
							}
							ret = true;
						} // fine for delle autorizzazioni
					}
				} // fine caso T


				if (rinnDirVO.getTipoRinnModalitaDiff().equals("U")) // selezione rinnovo di tutti gli utenti
				{
					String dataRinnovoOpz3=rinnDirVO.getDataRinnovoOpz3();
					if (!ValidazioneDati.strIsNull(dataRinnovoOpz3))
					{
						output.getErrori().add("DATA DI RINNOVO AUTORIZZAZIONE UTENTI: " + dataRinnovoOpz3);
					}
					// TODO mettere tutti gli id utente in rinnDirVO.getCodUtente()
					List allUteBibl=utentiDAO.getIdAllUtenti(rinnDirVO.getCodPolo(), rinnDirVO.getCodBib());
					if (allUteBibl!=null && allUteBibl.size()>0)
					{
						rinnDirVO.setCodUtente(new String[allUteBibl.size()]);
						for (int y = 0; y < allUteBibl.size(); y++)
						{
							rinnDirVO.getCodUtente()[y]=String.valueOf(allUteBibl.get(y));
						}
					}
					// solo per test
					//rinnDirVO.setCodUtente(new String[1]);
					//rinnDirVO.getCodUtente()[0]="3";
					//rinnDirVO.getCodUtente()[1]="51";

					//RinnovoDirittiDiffVO output=this.subRinnovoDirittiDifferita( rinnDirVO,  output,  transaction,  utentiDAO,  format,  listaDiritti,  diritto,  dataRinnovoOpz3,  ts );

					for (int i = 0; i < rinnDirVO.getCodUtente().length; i++) {
						DaoManager.begin(tx);
						boolean uteOk=true;
						Date dataRinnovo;
						String dataFineAutUteOld="";
						// leggere utente, se non esiste messaggio
						Trl_utenti_biblioteca uteBibl=utentiDAO.getUtenteBibliotecaById(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
						String codUtente=rinnDirVO.getCodUtente()[i]; // id utente
						if (uteBibl.getId_utenti()!=null && uteBibl.getId_utenti().getCod_utente()!=null && uteBibl.getId_utenti().getCod_utente().trim().length()>0 )
						{
							codUtente=uteBibl.getId_utenti().getCod_utente().trim();
						}
						if (uteBibl!=null && uteBibl.getId_utenti_biblioteca()>0 && uteBibl.getFl_canc()!='S' )
						{

							try {
								dataRinnovo = format.parse(dataRinnovoOpz3);
								// controllare se la data di  rinnovo è inferiore a quella precedente e inferiore a quella corrente
								int ritorna=0;
								if (!ValidazioneDati.strIsNull(dataRinnovoOpz3)
										&& !ValidazioneDati.strIsNull(dateToString(Calendar.getInstance().getTime()))) {
									ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz3, dateToString(Calendar.getInstance().getTime()),"4");
									if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
										 uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo autorizzazione non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella odierna.");
										 DaoManager.rollback(tx);
										 break; // interruzione generale
									}
								}
								if (!ValidazioneDati.strIsNull(dataRinnovoOpz3)
										&& !ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut()))) {
									ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz3, dateToString(uteBibl.getData_fine_aut()),"4");
									if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
										 uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella precedente.");
										 DaoManager.rollback(tx);
										 continue;
									}
								}

								if (!ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut())))
								{
									// salvo la vecchia data perchè verranno modificati solo i diritti
									// che hanno la  vecchia data di scadenza dell'autorizzazione
									dataFineAutUteOld=dateToString(uteBibl.getData_fine_aut());
									uteBibl.setData_fine_aut(dataRinnovo);
									uteBibl.setUte_var(rinnDirVO.getUser());
									uteBibl.setTs_var(ts);
									utentiDAO.updateUtentiBib(uteBibl);
								}
								else
								{
									uteOk=false;
									output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine per l'inesistenza di una precedente data di scadenza da rinnovare.");
									DaoManager.rollback(tx);
									continue;
								}


							  } catch (Exception e) {
								  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
								  DaoManager.rollback(tx);
								 uteOk=false;
	  							 log.error("", e);
								 continue;

							  }
							// ottengo la lista diritti utente associata all'utente corrente
							listaDiritti = utentiDAO.getListaDirittiUtente(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
							// ciclo sui diritti utente per aggiornarli
							Iterator iterator = listaDiritti.iterator();
							while (iterator.hasNext()) {
								diritto = (Trl_diritti_utente) iterator.next();
								int ritorna=0;
								if (!ValidazioneDati.strIsNull(dataRinnovoOpz3)
										&& !ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv()))) {
									ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz3, dateToString(diritto.getData_fine_serv()),"4");
									if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
										 uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quelle esistenti sui diritti.");
										 DaoManager.rollback(tx);
										 break;
									}
								}
								dataRinnovo = format.parse(dataRinnovoOpz3);
								try {
									if (!ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv())))
									{
										int ritorno=0;
										ritorno = ValidazioneDati.confrontaLeDate(dataFineAutUteOld, dateToString(diritto.getData_fine_serv()),"2");
										// modifica dei soli diritti con data scad=dataFineAutUteOld
										if (ritorno == ValidazioneDati.DATA1_EQ_DATA2) {
											diritto.setData_fine_serv(dataRinnovo);
											diritto.setUte_var(rinnDirVO.getUser());
											diritto.setTs_var(ts);
											utentiDAO.aggiornaDirittoUtente(diritto);
										}
									}
/*									else
									{
										uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non è andato a buon fine per l'inesistenza sui diritti di una precedente data di scadenza da rinnovare.");
										transaction.rollback();
										break;
									}*/

								  } catch (Exception e) {
									  uteOk=false;
									log.error("", e);
									DaoManager.rollback(tx);
									break;
								  }
							}
							if (!uteOk)
							{
								  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
								//	transaction.rollback();
									 continue;
							}
							else
							{
								output.getErrori().add("OK. Per il codice utente : " + codUtente + " \u00E8 stata rinnovata l'autorizzazione");
								DaoManager.commit(tx);
							}

						}
						else
						{
							output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 inesistente.");
							DaoManager.rollback(tx);
							 continue;
						}


					}
					ret = true;
				} // fine caso U
				if (rinnDirVO.getTipoRinnModalitaDiff().equals("L")) // rinnovo di tutti gli utenti selezionati da lista utenti
				{
					String dataRinnovoOpz3=rinnDirVO.getDataRinnovoAut();
					if (!ValidazioneDati.strIsNull(dataRinnovoOpz3))
					{
						output.getErrori().add("DATA DI RINNOVO AUTORIZZAZIONE UTENTI: " + dataRinnovoOpz3);
					}

					//RinnovoDirittiDiffVO output=this.subRinnovoDirittiDifferita( rinnDirVO,  output,  transaction,  utentiDAO,  format,  listaDiritti,  diritto,  dataRinnovoOpz3,  ts );

					for (int i = 0; i < rinnDirVO.getCodUtente().length; i++) {

						DaoManager.begin(tx);

						boolean uteOk=true;
						Date dataRinnovo;
						String dataFineAutUteOld="";
						// leggere utente, se non esiste messaggio
						Trl_utenti_biblioteca uteBibl=utentiDAO.getUtenteBibliotecaById(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
						String codUtente=rinnDirVO.getCodUtente()[i]; // id utente
						if (uteBibl.getId_utenti()!=null && uteBibl.getId_utenti().getCod_utente()!=null && uteBibl.getId_utenti().getCod_utente().trim().length()>0 )
						{
							codUtente=uteBibl.getId_utenti().getCod_utente().trim();
						}
						if (uteBibl!=null && uteBibl.getId_utenti_biblioteca()>0 && uteBibl.getFl_canc()!='S' )
						{

							try {
								dataRinnovo = format.parse(dataRinnovoOpz3);
								// controllare se la data di  rinnovo è inferiore a quella precedente e inferiore a quella corrente
								int ritorna=0;
								if (!ValidazioneDati.strIsNull(dataRinnovoOpz3)
										&& !ValidazioneDati.strIsNull(dateToString(Calendar.getInstance().getTime()))) {
									ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz3, dateToString(Calendar.getInstance().getTime()),"4");
									if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
										 uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo autorizzazione non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella odierna.");
										 DaoManager.rollback(tx);
										 break; // interruzione generale
									}
								}
								if (!ValidazioneDati.strIsNull(dataRinnovoOpz3)
										&& !ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut()))) {
									ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz3, dateToString(uteBibl.getData_fine_aut()),"4");
									if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
										 uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella precedente.");
										 DaoManager.rollback(tx);
										 continue;
									}
								}

								if (!ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut())))
								{
									// salvo la vecchia data perchè verranno modificati solo i diritti
									// che hanno la  vecchia data di scadenza dell'autorizzazione
									dataFineAutUteOld=dateToString(uteBibl.getData_fine_aut());
									uteBibl.setData_fine_aut(dataRinnovo);
									uteBibl.setUte_var(rinnDirVO.getUser());
									uteBibl.setTs_var(ts);
									utentiDAO.updateUtentiBib(uteBibl);
								}
								else
								{
									uteOk=false;
									output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine per l'inesistenza di una precedente data di scadenza da rinnovare.");
									DaoManager.rollback(tx);
									continue;
								}


							  } catch (Exception e) {
								  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
								  DaoManager.rollback(tx);
								 uteOk=false;
	  							 log.error("", e);
								 continue;

							  }
							// ottengo la lista diritti utente associata all'utente corrente
							listaDiritti = utentiDAO.getListaDirittiUtente(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
							// ciclo sui diritti utente per aggiornarli
							Iterator iterator = listaDiritti.iterator();
							while (iterator.hasNext()) {
								diritto = (Trl_diritti_utente) iterator.next();
								int ritorna=0;
								if (!ValidazioneDati.strIsNull(dataRinnovoOpz3)
										&& !ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv()))) {
									ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoOpz3, dateToString(diritto.getData_fine_serv()),"4");
									if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
										 uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quelle esistenti sui diritti.");
										 DaoManager.rollback(tx);
										 break;
									}
								}
								dataRinnovo = format.parse(dataRinnovoOpz3);
								try {
									if (!ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv())))
									{
										int ritorno=0;
										ritorno = ValidazioneDati.confrontaLeDate(dataFineAutUteOld, dateToString(diritto.getData_fine_serv()),"2");
										// modifica dei soli diritti con data scad=dataFineAutUteOld
										if (ritorno == ValidazioneDati.DATA1_EQ_DATA2) {
											diritto.setData_fine_serv(dataRinnovo);
											diritto.setUte_var(rinnDirVO.getUser());
											diritto.setTs_var(ts);
											utentiDAO.aggiornaDirittoUtente(diritto);
										}
									}
/*									else
									{
										uteOk=false;
										output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non è andato a buon fine per l'inesistenza sui diritti di una precedente data di scadenza da rinnovare.");
										transaction.rollback();
										break;
									}*/

								  } catch (Exception e) {
									  uteOk=false;
									log.error("", e);
									DaoManager.rollback(tx);
									break;
								  }
							}
							if (!uteOk)
							{
								  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
								//	transaction.rollback();
									 continue;
							}
							else
							{
								output.getErrori().add("OK. Per il codice utente : " + codUtente + " \u00E8 stata rinnovata l'autorizzazione");
								DaoManager.commit(tx);
							}

						}
						else
						{
							output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 inesistente.");
							DaoManager.rollback(tx);
							 continue;
						}


					}
					ret = true;
				} // fine caso L

				if (tx.getStatus() == Status.STATUS_ACTIVE){
					DaoManager.commit(tx);
				}
			  } catch (Exception e) {
				  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione non \u00E8 andato a buon fine.");
					log.error("", e);
					DaoManager.rollback(tx);

			  }

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return output;
	}

//****************************
	public RinnovoDirittiDiffVO subRinnovoDirittiDifferita(RinnovoDirittiDiffVO rinnDirVO, RinnovoDirittiDiffVO output, UserTransaction tx, UtentiDAO utentiDAO, SimpleDateFormat format, List listaDiritti, Trl_diritti_utente diritto, String dataRinnovoStr, Timestamp ts  ) throws ApplicationException
	{
		try {
			for (int i = 0; i < rinnDirVO.getCodUtente().length; i++) {

				DaoManager.begin(tx);

				boolean uteOk=true;
				Date dataRinnovo;
				String dataFineAutUteOld="";
				// leggere utente, se non esiste messaggio
				Trl_utenti_biblioteca uteBibl=utentiDAO.getUtenteBibliotecaById(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
				String codUtente=rinnDirVO.getCodUtente()[i]; // id utente
				if (uteBibl.getId_utenti()!=null && uteBibl.getId_utenti().getCod_utente()!=null && uteBibl.getId_utenti().getCod_utente().trim().length()>0 )
				{
					codUtente=uteBibl.getId_utenti().getCod_utente().trim();
				}
				if (uteBibl!=null && uteBibl.getId_utenti_biblioteca()>0 && uteBibl.getFl_canc()!='S' )
				{

					try {
						dataRinnovo = format.parse(dataRinnovoStr);
						// controllare se la data di  rinnovo è inferiore a quella precedente e inferiore a quella corrente
						int ritorna=0;
						if (!ValidazioneDati.strIsNull(dataRinnovoStr)
								&& !ValidazioneDati.strIsNull(dateToString(Calendar.getInstance().getTime()))) {
							ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoStr, dateToString(Calendar.getInstance().getTime()),"4");
							if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
								 uteOk=false;
								output.getErrori().add("ERRORE. Il rinnovo autorizzazione non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella odierna.");
								 DaoManager.rollback(tx);
								 break; // interruzione generale
							}
						}
						if (!ValidazioneDati.strIsNull(dataRinnovoStr)
								&& !ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut()))) {
							ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoStr, dateToString(uteBibl.getData_fine_aut()),"4");
							if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
								 uteOk=false;
								output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quella precedente.");
								 DaoManager.rollback(tx);
								 continue;
							}
						}

						if (!ValidazioneDati.strIsNull(dateToString(uteBibl.getData_fine_aut())))
						{
							// salvo la vecchia data perchè verranno modificati solo i diritti
							// che hanno la  vecchia data di scadenza dell'autorizzazione
							dataFineAutUteOld=dateToString(uteBibl.getData_fine_aut());
							uteBibl.setData_fine_aut(dataRinnovo);
							uteBibl.setUte_var(rinnDirVO.getUser());
							uteBibl.setTs_var(ts);
							utentiDAO.updateUtentiBib(uteBibl);
						}
						else
						{
							uteOk=false;
							output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine per l'inesistenza di una precedente data di scadenza da rinnovare.");
							DaoManager.rollback(tx);
							continue;
						}


					  } catch (Exception e) {
						  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
						  DaoManager.rollback(tx);
						 uteOk=false;
							 log.error("", e);
						 continue;

					  }
					// ottengo la lista diritti utente associata all'utente corrente
					listaDiritti = utentiDAO.getListaDirittiUtente(new Integer(rinnDirVO.getCodUtente()[i]).intValue());
					// ciclo sui diritti utente per aggiornarli
					Iterator iterator = listaDiritti.iterator();
					while (iterator.hasNext()) {
						diritto = (Trl_diritti_utente) iterator.next();
						int ritorna=0;
						if (!ValidazioneDati.strIsNull(dataRinnovoStr)
								&& !ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv()))) {
							ritorna = ValidazioneDati.confrontaLeDate(dataRinnovoStr, dateToString(diritto.getData_fine_serv()),"4");
							if (ritorna != ValidazioneDati.DATA1_LT_DATA2) {
								 uteOk=false;
								output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 la data di rinnovo \u00E8 inferiore a quelle esistenti sui diritti.");
								 DaoManager.rollback(tx);
								 break;
							}
						}
						dataRinnovo = format.parse(dataRinnovoStr);
						try {
							if (!ValidazioneDati.strIsNull(dateToString(diritto.getData_fine_serv())))
							{
								int ritorno=0;
								ritorno = ValidazioneDati.confrontaLeDate(dataFineAutUteOld, dateToString(diritto.getData_fine_serv()),"2");
								// modifica dei soli diritti con data scad=dataFineAutUteOld
								if (ritorno == ValidazioneDati.DATA1_EQ_DATA2) {
									diritto.setData_fine_serv(dataRinnovo);
									diritto.setUte_var(rinnDirVO.getUser());
									diritto.setTs_var(ts);
									utentiDAO.aggiornaDirittoUtente(diritto);
								}
							}
		/*									else
							{
								uteOk=false;
								output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non è andato a buon fine per l'inesistenza sui diritti di una precedente data di scadenza da rinnovare.");
								transaction.rollback();
								break;
							}*/

						  } catch (Exception e) {
							  uteOk=false;
							log.error("", e);
							DaoManager.rollback(tx);
							break;
						  }
					}
					if (!uteOk)
					{
						  output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine.");
						//	transaction.rollback();
							 continue;
					}
					else
					{
						output.getErrori().add("OK. Per il codice utente : " + codUtente + " \u00E8 stata rinnovata l'autorizzazione");
						DaoManager.commit(tx);
					}

				}
				else
				{
					output.getErrori().add("ERRORE. Il rinnovo dell'autorizzazione per il codice utente : " + codUtente + " non \u00E8 andato a buon fine perch\u00E8 inesistente.");
					DaoManager.rollback(tx);
					 continue;
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return output;
	}

//****************************

	public ArchiviazioneMovLocVO gestioneDifferitaArchiviazioneMovLoc(ArchiviazioneMovLocVO richiesta, BatchLogWriter blw)
			throws ApplicationException {
		UserTransaction tx = ctx.getUserTransaction();
		Tbl_richiesta_servizio currReq = null;
		List<String> errori = richiesta.getErrori();
		Logger _log = blw.getLogger();

		try {
			DaoManager.begin(tx);

			errori.add("SVECCHIAMENTO MOVIMENTI LOCALI");
			//almaviva5_20130424 segnalazione ICCU: bib. affiliata
			String codBibArch = richiesta.getCodBibArchiviazione();
			String dataInizioArch = richiesta.getDataInizio();
			String dataSvecchiamento = richiesta.getDataSvecchiamento();

			errori.add("CODICE BIBLIOTECA:  " + codBibArch);
			errori.add("DATA INIZIO:        " + dataInizioArch);
			errori.add("DATA FINE:          " + dataSvecchiamento);
			errori.add("DATA EFFETTUAZIONE: " + dateToString(DaoManager.now()));

			RichiesteServizioDAO dao = new RichiesteServizioDAO();
			Tbl_documenti_lettoriDAO ddao = new Tbl_documenti_lettoriDAO();

			Timestamp dataInizio = null;
    		Timestamp dataLimite = null;
    		Set<Integer> listaIdPrenotPosto = new HashSet<Integer>();
			// individuazione richieste da archiviare
    		List<Long> listaIdRichieste = Collections.emptyList();
	    	try {
	    		if (ValidazioneDati.isFilled(dataInizioArch) )
	    			dataInizio = DateUtil.toTimestamp(dataInizioArch);
	    		if (ValidazioneDati.isFilled(dataSvecchiamento) )
					dataLimite = DateUtil.withTimeAtEndOfDay(DateUtil.toDate(dataSvecchiamento));

				listaIdRichieste = dao.getListaRichiesteLocaliDaStoricizzare(richiesta.getCodPolo(), codBibArch, dataInizio, dataLimite);
				_log.debug("richieste estratte: " + ValidazioneDati.size(listaIdRichieste) );
			} catch (Exception e) {
				_log.error("", e);
			}

			int totMovSvecch = 0;
			int totErrori = 0;
			int totDocCancellati = 0;
			int totPrenRifiutati = 0;

			for (long idReq : listaIdRichieste) {
				try {
					DaoManager.begin(tx);

					Tbl_richiesta_servizio req = dao.getMovimentoById(idReq);
					currReq = req;

					// almaviva5_20160310 servizi ILL
					Tbl_dati_richiesta_ill dati_richiesta_ill = req.getDati_richiesta_ill();
					// check stato richiesta ill
					if (dati_richiesta_ill != null) {
						DatiRichiestaILLVO datiILL = ConversioneHibernateVO.toWeb().datiRichiestaILL(dati_richiesta_ill, null);
						if (!ILLConfiguration2.getInstance().isRichiestaStoricizzabile(datiILL, dataLimite))
							continue;
					}

					List<Tbl_solleciti> listaSolleciti = dao.getListaSolleciti(req);

					int numeroSolleciti = ValidazioneDati.size(listaSolleciti);

					// inserimento
					Tbl_storico_richieste_servizio srs = new Tbl_storico_richieste_servizio();
					if (richiesta.getCodPolo() != null) {
						srs.setCd_polo(richiesta.getCodPolo()); // archMovLocVO.getCodPolo()
					}
					if (codBibArch != null) {
						srs.setCd_bib(codBibArch);
					}

					srs.setCod_rich_serv(new BigDecimal(req.getCod_rich_serv())); // cod_rich_serv

					Trl_utenti_biblioteca utenteBib = req.getId_utenti_biblioteca();
					if (utenteBib != null
							&& utenteBib.getCd_biblioteca() != null
							&& utenteBib.getCd_biblioteca().getCd_biblioteca() != null) {
						srs.setCod_bib_ut(utenteBib.getCd_biblioteca().getCd_biblioteca()); // "id_utenti_biblioteca
					}
					if (utenteBib != null && utenteBib.getId_utenti_biblioteca() > 0) {
						srs.setCod_utente(new BigDecimal(utenteBib.getId_utenti_biblioteca())); // "id_utenti_biblioteca
						Tbl_utenti utente = utenteBib.getId_utenti();
						String denominazione = trimOrEmpty(trimOrEmpty(utente.getCognome()) + " " + trimOrEmpty(utente.getNome()));
						srs.setDenominazione(ValidazioneDati.trunc(denominazione, 255));
					}
					Tbl_servizio codServ = req.getId_servizio();
					Tbl_tipo_servizio tipoSrv = codServ.getId_tipo_servizio();
					if (codServ != null && tipoSrv != null) {
						// aggiunta la descrizione separata dal spazio
						srs.setCod_tipo_serv(tipoSrv.getCod_tipo_serv()	+ " "
								+ CodiciProvider.cercaDescrizioneCodice(tipoSrv.getCod_tipo_serv(),
										CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN)); // id_servizio
					} else
						srs.setCod_tipo_serv("");

					if (req.getData_richiesta() != null) {
						srs.setData_richiesta(req.getData_richiesta()); // data_richiesta
					}
//					if (req.getNum_volume() != null) {
//						srs.setNum_volume(req.getNum_volume());
//					}
					if (req.getNum_fasc() != null) {
						srs.setNum_fasc(req.getNum_fasc());
					}
					if (req.getNum_pezzi() != null) {
						srs.setNum_pezzi(Short.valueOf(req.getNum_pezzi()));
					}
					if (req.getNote_ut() != null) {
						srs.setNote_ut(req.getNote_ut());
					}
					if (req.getPrezzo_max() != null) {
						srs.setPrezzo_max(req.getPrezzo_max());
					}
					if (req.getCosto_servizio() != null) {
						srs.setCosto_servizio(req.getCosto_servizio());
					}
					if (req.getData_massima() != null) {
						srs.setData_massima(req.getData_massima());
					}
					if (req.getNote_bibliotecario() != null) {
						srs.setNote_bibl(req.getNote_bibliotecario());
					}
					if (req.getData_proroga() != null) {
						srs.setData_proroga(req.getData_proroga());
					}
					if (req.getData_in_prev() != null) {
						srs.setData_in_prev(req.getData_in_prev());
					}
					if (req.getData_fine_prev() != null) {
						srs.setData_fine_prev(req.getData_fine_prev());
					}
					if (ValidazioneDati.isFilled(req.getFl_svolg()) ) {
						srs.setFlag_svolg(req.getFl_svolg());
					}
					if (req.getCopyright() != null) {
						srs.setCopyright(req.getCopyright().charValue());
					}
					if (ValidazioneDati.isFilled(req.getCod_erog())) {
						srs.setDescr_erog(CodiciProvider.cercaDescrizioneCodice(req.getCod_erog(),
										CodiciType.CODICE_MODALITA_EROGAZIONE,
										CodiciRicercaType.RICERCA_CODICE_SBN));
					}
					if (ValidazioneDati.isFilled(req.getCod_stato_rich())) {
						srs.setDescr_stato_ric(CodiciProvider.cercaDescrizioneCodice(String.valueOf(req.getCod_stato_rich()),
								CodiciType.CODICE_STATO_RICHIESTA,
								CodiciRicercaType.RICERCA_CODICE_SBN));
					}
					Tbl_supporti_biblioteca supp = req.getId_supporti_biblioteca();
					if (supp != null && supp.getCod_supporto() != null) {
						srs.setDescr_supporto(CodiciProvider.cercaDescrizioneCodice(supp.getCod_supporto(),
										CodiciType.CODICE_SUPPORTO_COPIA,
										CodiciRicercaType.RICERCA_CODICE_SBN));
					}
					if (req.getCod_risp() != null
							&& String.valueOf(req.getCod_risp()).length() > 0) {
						srs.setDescr_risp(String.valueOf(req.getCod_risp()));
					}
					if (req.getId_modalita_pagamento() != null
							&& req.getId_modalita_pagamento().getCod_mod_pag() > 0) {
						srs.setDescr_mod_pag(String.valueOf(req.getId_modalita_pagamento().getCod_mod_pag()));
					}
					if (ValidazioneDati.isFilled(req.getFl_tipo_rec())) {
						srs.setFlag_pren(req.getFl_tipo_rec());
					} else
						srs.setFlag_pren(' ');

					Tbl_documenti_lettori doc = req.getId_documenti_lettore();
					if (doc != null
							&& ValidazioneDati.isFilled(doc.getTipo_doc_lett())) {
						srs.setTipo_doc_lett(doc.getTipo_doc_lett());
					}
					if (doc != null && doc.getCod_doc_lett() > 0) {
						srs.setCod_doc_lett(new BigDecimal(doc.getCod_doc_lett())); // id_documenti_lettore
					} else
						srs.setCod_doc_lett(new BigDecimal(0)); // id_documenti_lettore

					if (req.getId_esemplare_documenti_lettore() != null
							&& req.getId_esemplare_documenti_lettore().getPrg_esemplare() > 0) {
						srs.setPrg_esemplare(req.getId_esemplare_documenti_lettore().getPrg_esemplare()); // tbl_esemplare_documento_lettore
					} else
						srs.setPrg_esemplare((short) 0);

					// if(eleRicServ.getId_documenti_lettore()!=null &&
					// eleRicServ.getId_documenti_lettore().getCod_serie()!=null)
					Tbc_inventario inv = req.getCd_polo_inv();
					if (inv != null && inv.getCd_serie() != null
							&& inv.getCd_serie().getCd_serie() != null) {
						srs.setCod_serie(inv.getCd_serie().getCd_serie()); // alternativa
																			// eleRicServ.getId_documenti_lettore().getCod_serie()
					}
					// if(eleRicServ.getId_documenti_lettore()!=null &&
					// eleRicServ.getId_documenti_lettore().getCod_inven()>0)
					if (inv != null && inv.getCd_inven() > 0) {
						srs.setCod_inven(inv.getCd_inven()); // alternativa
																// eleRicServ.getId_documenti_lettore().getCod_inven()
					}
					if (req.getFl_condiz() != null) {
						srs.setFlag_condiz(req.getFl_condiz().charValue());
					}
					if (req.getCod_tipo_serv_alt() != null) {
						srs.setCod_tipo_serv_alt(req.getCod_tipo_serv_alt());
					}

					if (req.getCod_bib_dest() != null) {
						srs.setCod_bib_dest(req.getCod_bib_dest());
					}

					// *** per i documenti lettore
					if (doc != null && doc.getTitolo() != null) {
						//almaviva5_20160825 #6261 pulizia campo titolo
						srs.setTitolo(StringUtils.normalizeSpace(doc.getTitolo()));
					}
					if (doc != null && doc.getAutore() != null) {
						srs.setAutore(doc.getAutore());
					}
					if (doc != null && doc.getEditore() != null) {
						srs.setEditore(doc.getEditore());
					}
					if (doc != null && doc.getAnno_edizione() != null) {
						srs.setAnno_edizione(doc.getAnno_edizione().toString());
					}
					if (doc != null && doc.getLuogo_edizione() != null) {
						srs.setLuogo_edizione(doc.getLuogo_edizione());
					}
					if (doc != null && doc.getAnnata() != null) {
						srs.setAnnata(doc.getAnnata());
					}
					// *** per gli inventari

					if (inv != null && inv.getB() != null
							&& inv.getB().getIsbd() != null) {
						TitoloACQAreeIsbdVO aree = getEjbAcq().getAreeIsbdTitolo(inv.getB().getBid());

						if (aree != null && aree.getArea200Titolo() != null) {
							//almaviva5_20160825 #6261 pulizia campo titolo
							String titolo = StringUtils.normalizeSpace(aree.getArea200Titolo());
							//almaviva5_20150121 segnalazione RML
							srs.setTitolo(ValidazioneDati.trunc(titolo, 240) );
						}
						else
							//almaviva5_20110113 #3988
							srs.setTitolo(ServiziConstant.MSG_INVENTARIO_CANCELLATO);
						if (aree != null && aree.getArea205Edizione() != null) {
							// almaviva5_20110113 migr. MO1: troncamento editore a 50
							srs.setEditore(ValidazioneDati.trunc(aree.getArea205Edizione(), 50));
						}
					}

//					if (req.getNum_volume() != null) {
//						srs.setNum_vol_mon(req.getNum_volume());
//					}
					if (req.getData_in_eff() != null) {
						srs.setData_in_eff(req.getData_in_eff());
					}
					if (req.getData_fine_eff() != null) {
						srs.setData_fine_eff(req.getData_fine_eff());
					}
					if (req.getNum_rinnovi() != null) {
						srs.setNum_rinnovi(req.getNum_rinnovi());
					}
					if (req.getNote_bibliotecario() != null) {
						srs.setNote_bibliotecario(req.getNote_bibliotecario());
					}
					if (ValidazioneDati.isFilled(req.getCod_stato_mov())) {
						srs.setDescr_stato_mov(CodiciProvider.cercaDescrizioneCodice(String.valueOf(req.getCod_stato_mov()),
										CodiciType.CODICE_STATO_MOVIMENTO,
										CodiciRicercaType.RICERCA_CODICE_SBN));
					}
					if (req.getFl_inoltro() != null
							&& String.valueOf(req.getFl_inoltro()).length() > 0) {
						srs.setFlag_tipo_serv(req.getFl_inoltro().charValue()); // ??????
					}

					// dati calcolati
					srs.setNum_solleciti((short) 0);
					if (numeroSolleciti > 0) {
						srs.setNum_solleciti((short) numeroSolleciti);
						srs.setData_ult_soll(listaSolleciti.get(0).getData_invio());
					}

					//prenotazioni posto
					Set<Tbl_prenotazione_posto> prenotazioni_posti = req.getPrenotazioni_posti();
					for (Tbl_prenotazione_posto prenot_posto : prenotazioni_posti) {
						PrenotazionePostoDecorator pp = new PrenotazionePostoDecorator(ConvertToWeb.Sale.prenotazione(prenot_posto, null));
						srs.setId_prenot_posto(pp.getId_prenotazione());
						srs.setDescr_sala(pp.getDescrizionePosto());
						srs.setDescr_stato_prenot_posto(pp.getDescrizioneStato());
						srs.setTs_prenot_posto_inizio(pp.getTs_inizio());
						srs.setTs_prenot_posto_fine(pp.getTs_fine());

						listaIdPrenotPosto.add(pp.getId_prenotazione());
					}

					srs.setUte_ins(req.getUte_ins());
					srs.setTs_ins(req.getTs_ins());
					srs.setUte_var(req.getUte_var());
					srs.setTs_var(req.getTs_var());

					srs.setFl_canc('N');
					boolean esito = dao.inserimentoRichiestaLocaleArchiviata(srs);

					if (esito) {
						// almaviva5_20160310 servizi ill
						if (dati_richiesta_ill != null) {
							req.setDati_richiesta_ill(null);
							dao.eliminaDatiRichiestaILLStoricizzata(dati_richiesta_ill);
							//dati doc da richiesta ill (se sostituita sul mov. locale da inventario)
							if (doc == null)
								doc = dati_richiesta_ill.getDocumento();
						}

						dao.eliminaRichiestaLocaleStoricizzata(req);

						// eliminazione doc non posseduti o creati dal server ILL
						if (doc != null && (doc.getTipo_doc_lett() == 'D' || doc.getFonte() == 'I') ) {
							ddao.eliminaDocumentoRichiestaStoricizzata(doc);
							totDocCancellati++;
						}

						String msg = formattaDoc(req);
						if (req.getFl_tipo_rec() == 'P') {
							errori.add("PROGR.PRENOTAZIONE: " + msg);
							totPrenRifiutati++;
						} else {
							errori.add("PROGR.RICHIESTA: " + msg);
							totMovSvecch++;
						}

						DaoManager.commit(tx);

					} else {
						totErrori++;
						DaoManager.rollback(tx);
					}

				} catch (Exception e) {
					String msg = formattaDoc(currReq);
					errori.add("ERRORE SVECCHIAMENTO PROGRESSIVO: " + msg);
					_log.error("", e);
					totErrori++;
					DaoManager.rollback(tx);

					continue;
				}
			}

			//cancellazione prenotazioni posto senza mov associati
			SaleDAO sdao = new SaleDAO();
			for (Integer ppid : listaIdPrenotPosto)
				try {
					DaoManager.begin(tx);
					Tbl_prenotazione_posto pp = sdao.getPrenotazionePostoById(ppid);
					if (sdao.countRichiesteCollegatePrenotazione(pp) == 0)
						sdao.eliminaPrenotazionePostoStoricizzata(pp);
					DaoManager.commit(tx);

				} catch (Exception e) {
					_log.error("", e);
					DaoManager.rollback(tx);

					continue;
				}

			//cancellazione documenti non posseduti e non legati a richieste ILL
			DaoManager.begin(tx);
			List<Integer> documenti = ddao.getListaDocumentiNonPossedutiSenzaRichiesta(richiesta.getCodPolo(), codBibArch);
			for (Integer idDocumento : documenti)
				try {
					DaoManager.begin(tx);
					Tbl_documenti_lettori doc = ddao.getDocumentoLettoreById(idDocumento);
					ddao.eliminaDocumentoRichiestaStoricizzata(doc);
					totDocCancellati++;
					DaoManager.commit(tx);

				} catch (Exception e) {
					_log.error("", e);
					DaoManager.rollback(tx);

					continue;
				}

			errori.add("MOVIMENTI SVECCHIATI: " + String.valueOf(totMovSvecch));
			errori.add("PRENOTAZIONI RIFIUTATE: " + String.valueOf(totPrenRifiutati));
			errori.add("DOCUMENTI ELIMINATI: " + String.valueOf(totDocCancellati));
			errori.add("ERRORI: " + String.valueOf(totErrori));
			DaoManager.commit(tx);

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return richiesta;
	}

	private String formattaDoc(Tbl_richiesta_servizio req) {
		if (req == null)
			return "";

		StringBuilder buf = new StringBuilder(512);
		try {
		buf.append(req.getCod_rich_serv());
		buf.append(", utente: ");
		buf.append(ValidazioneDati.trimOrEmpty(req.getId_utenti_biblioteca().getId_utenti().getCod_utente()));
		buf.append(", documento: ");
		buf.append(ServiziUtil.formattaChiaveDocumento(req));
		buf.append(", coll: ");
		buf.append(req.getId_documenti_lettore() != null ?
				ValidazioneDati.trimOrEmpty(req.getId_documenti_lettore().getSegnatura()) :
				ServiziUtil.formattaSegnaturaCollocazione(req));
		} catch (Exception e) {	}

		String msg = buf.toString();
		return msg;
	}



	public AggiornaDirittiUtenteVO gestioneDifferitaAggiornamentoDirittiUtente(
			AggiornaDirittiUtenteVO richiesta, BatchLogWriter blog) throws ApplicationException {

		BatchAggiornamentoDirittiUtente batch = new BatchAggiornamentoDirittiUtente(ctx.getUserTransaction(), richiesta, blog);
		return batch.eseguiBatch();
	}



	private boolean aggiornamentoServiziAssociati(
			AutorizzazioneVO recAutorizzazione,
			Tbl_tipi_autorizzazioni autorizzazione) throws DataException,
			ApplicationException {


		ElementoSinteticaServizioVO servizio = null;
		Tbl_servizio tbl_servizio = null;
		List listaServizi;
		Trl_autorizzazioni_servizi autorizzazione_servizio = null;

		listaServizi = recAutorizzazione.getElencoServizi();
		List serviziAutGiaPresenti = null;

		try {
			AutorizzazioniDAO autDAO = new AutorizzazioniDAO();
			ServiziDAO serviziDAO = new ServiziDAO();
			UtentiDAO utentiDao = new UtentiDAO();
			Trl_diritti_utenteDAO trl_dirDAO = new Trl_diritti_utenteDAO();
			serviziAutGiaPresenti = autDAO
					.getListaServiziAutorizzazione(recAutorizzazione
							.getCodPolo(),
							recAutorizzazione.getCodBiblioteca(),
							recAutorizzazione.getCodAutorizzazione());
			Set idServiziGiaAss = null;//getIdServiziAutGiaAssociati(serviziAutGiaPresenti);
			Set idServiziDaAss = null;//getIdServiziDaAssociare((List) recAutorizzazione.getElencoServizi());

			// Scorro lista servizi da inserire
			Iterator iterator = listaServizi.iterator();
			while (iterator.hasNext()) {
				servizio = (ElementoSinteticaServizioVO) iterator.next();

				if (!idServiziGiaAss.contains(new Integer(servizio
						.getIdServizio()))) {
					// Si tratta di un nuovo servizio. Va inserito
					tbl_servizio = serviziDAO.getServizioBiblioteca(servizio
							.getCodPolo(), servizio.getCodBiblioteca(),
							servizio.getTipServizio(), servizio
									.getCodServizio());
					Trl_autorizzazioni_servizi esisteDirCanc = autDAO
							.getServizioAutorizzazione(servizio.getCodPolo(),
									servizio.getCodBiblioteca(),
									recAutorizzazione.getIdAutorizzazione(),
									servizio.getIdServizio(),
									recAutorizzazione.getCodAutorizzazione(), 'S');
					if (esisteDirCanc!=null )
					{
						// flegga
						esisteDirCanc.setFl_canc('N');
						esisteDirCanc.setTs_var(recAutorizzazione.getTsVar());
						esisteDirCanc.setUte_var(recAutorizzazione.getUteVar());
						autDAO.aggiornamentoAutorizzazioneServizio(esisteDirCanc);
					}
					else
					{
						// inserisci
						autorizzazione_servizio = new Trl_autorizzazioni_servizi();
						autorizzazione_servizio.setFl_canc('N');
						autorizzazione_servizio.setTs_ins(recAutorizzazione
								.getTsIns());
						autorizzazione_servizio.setTs_var(recAutorizzazione
								.getTsVar());
						autorizzazione_servizio.setUte_ins(recAutorizzazione
								.getUteIns());
						autorizzazione_servizio.setUte_var(recAutorizzazione
								.getUteVar());
						autorizzazione_servizio.setId_servizio(tbl_servizio);
						autorizzazione_servizio.setId_tipi_autorizzazione(autorizzazione);

						autDAO.inserimentoAutorizzazioneServizio(autorizzazione_servizio);
					}
					// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI CON GLI UTENTI
					// LEGGO Trl_diritti_utente CON ID SERVIZIO
					// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESO COD_AUTORIZZAZIONE E inserisco I DIRiTTI
					//Tbl_utenti id_utenti, Tbl_servizio
					List<Tbl_utenti> listaUtenti= utentiDao.getUtenteByAut(servizio.getCodPolo(), servizio.getCodBiblioteca(), recAutorizzazione.getCodAutorizzazione(), false);
					Date dataFineAut=null;
					Date dataInizioAut=null;
					Date dataSospInizio=null;
					Date dataSospFine=null;

					for (int i = 0; i < listaUtenti.size(); i++) {
						Tbl_utenti ute_single= listaUtenti.get(i);
						//ricerca della data di fine autorizzazione
						if(listaUtenti.get(i).getTrl_utenti_biblioteca()!= null)
		                {
						    try
		                    {
							Object[] elencoUteBibl=listaUtenti.get(i).getTrl_utenti_biblioteca().toArray();
							for (int j = 0; j<elencoUteBibl.length; j++) {
								Trl_utenti_biblioteca eleUteBibl = (Trl_utenti_biblioteca) elencoUteBibl[j];
								if (eleUteBibl.getCod_tipo_aut().trim().equals(recAutorizzazione.getCodAutorizzazione().trim()) && eleUteBibl.getCd_biblioteca().getCd_biblioteca().equals(servizio.getCodBiblioteca()))
								{
									dataFineAut=eleUteBibl.getData_fine_aut();
									dataInizioAut=eleUteBibl.getData_inizio_aut();
									dataSospInizio=eleUteBibl.getData_inizio_sosp();
									dataSospFine=eleUteBibl.getData_fine_sosp();
								}
							}
							} catch (Exception e) {
								log.error("", e);
							}
		                }

						//listaUtenti.setFl_canc('N');
// DA QUI
						// controllo L'ESISTENZA di un record non cancellato che appartenga alla stessa famiglia del servizio a cui il diritto preso in considerazione appartiene
						Trl_diritti_utente dirittoEsistente = utentiDao.verificaEsistenzaServizioDirittoUtenteBiblioteca(ute_single, tbl_servizio);
						//Trl_diritti_utente diritto = utentiDao.verificaEsistenzaDirittoUtenteBiblioteca(ute_single, tbl_servizio);
						//diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte, diritto);
						//tbl_utenti utente, Tbl_servizio servizio, 	Trl_diritti_utente diritto_utente
						if (dirittoEsistente!=null)
						{
							// esiste un diritto legato alla stessa famiglia del servizio
							if (dirittoEsistente.getId_servizio_id_servizio()==tbl_servizio.getId_servizio() )
							{
								//  diritto già associato come personalizzazione: non faccio nulla
							}
							if (dirittoEsistente.getId_servizio_id_servizio()!=tbl_servizio.getId_servizio() )
							{
								// diritto da cancellare logicamente per inserire il nuovo diritto della stessa famiglia
								//cancellazione logica di quello trovato

								// RICHIESTA CONTARDI DEL 03.05.10 - vince sempre il diritto personalizzato rispetto a quello ereditato per la stessa famiglia di servizio
								// diritto della stessa famiglia già associato come personalizzazione: non faccio nulla
/*								dirittoEsistente.setFl_canc('S');
								dirittoEsistente.setTs_var(recAutorizzazione.getTsVar());
								dirittoEsistente.setUte_var(recAutorizzazione.getUteVar());
								utentiDao.aggiornaDirittoUtente(dirittoEsistente);
*/								//TODO recuperare le eventuali date per passarle al nuovo
							}
						}
						if (dirittoEsistente==null) // RICHIESTA CONTARDI DEL 03.05.10 (IN ASSENZA DI PERSONALIZZAZIONI)
						{
							// controllo esistenza del nuovo diritto per verificare se presente ma cancellato logicamente
							Trl_diritti_utente diritto = utentiDao.verificaEsistenzaDirittoUtenteBiblioteca(ute_single, tbl_servizio);
							//diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte, diritto);
							//tbl_utenti utente, Tbl_servizio servizio, 	Trl_diritti_utente diritto_utente
							if (diritto==null)
							{
								Trl_diritti_utente dirittoNew= new Trl_diritti_utente();
								dirittoNew.setFl_canc('N');
								dirittoNew.setTs_ins(recAutorizzazione.getTsIns());
								dirittoNew.setTs_var(recAutorizzazione.getTsVar());
								dirittoNew.setUte_ins(recAutorizzazione.getUteIns());
								dirittoNew.setUte_var(recAutorizzazione.getUteVar());
								dirittoNew.setData_inizio_serv(Calendar.getInstance().getTime());
								dirittoNew.setData_fine_serv(null);
								if (dataFineAut!=null)
								{
									dirittoNew.setData_fine_serv(dataFineAut);
								}
								dirittoNew.setData_inizio_sosp(null);
								if (dataSospInizio!=null)
								{
									dirittoNew.setData_inizio_sosp(dataSospInizio);
								}
								dirittoNew.setData_fine_sosp(null);
								if (dataSospFine!=null)
								{
									dirittoNew.setData_fine_sosp(dataSospFine);
								}
								dirittoNew.setId_servizio_id_servizio(new Integer(autorizzazione_servizio.getId_servizio_id_servizio()));
								dirittoNew.setNote("");
								dirittoNew.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione().trim()); //20.01.10
								utentiDao.inserisciDirittoUtente(ute_single,tbl_servizio, dirittoNew);
							}
							else
							{
								if (diritto.getFl_canc()=='S')
								{
									diritto.setFl_canc('N');
									// date da aggiornare
									diritto.setData_inizio_serv(Calendar.getInstance().getTime());
									if (dataFineAut!=null)
									{
										diritto.setData_fine_serv(dataFineAut);
									}
									if (dataSospInizio!=null)
									{
										diritto.setData_inizio_sosp(dataSospInizio);
									}
									diritto.setData_fine_sosp(null);
									if (dataSospFine!=null)
									{
										diritto.setData_fine_sosp(dataSospFine);
									}
									diritto.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione().trim()); //20.01.10
									utentiDao.aggiornaDirittoUtente(diritto);
								}
							}

						}

/*						// controllo esistenza del nuovo diritto per verificare se presente ma cancellato logicamente
						Trl_diritti_utente diritto = utentiDao.verificaEsistenzaDirittoUtenteBiblioteca(ute_single, tbl_servizio);
						//diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte, diritto);
						//tbl_utenti utente, Tbl_servizio servizio, 	Trl_diritti_utente diritto_utente
						if (diritto==null)
						{
							Trl_diritti_utente dirittoNew= new Trl_diritti_utente();
							dirittoNew.setFl_canc('N');
							dirittoNew.setTs_ins(recAutorizzazione.getTsIns());
							dirittoNew.setTs_var(recAutorizzazione.getTsVar());
							dirittoNew.setUte_ins(recAutorizzazione.getUteIns());
							dirittoNew.setUte_var(recAutorizzazione.getUteVar());
							dirittoNew.setData_inizio_serv(Calendar.getInstance().getTime());
							dirittoNew.setData_fine_serv(null);
							if (dataFineAut!=null)
							{
								dirittoNew.setData_fine_serv(dataFineAut);
							}
							dirittoNew.setData_inizio_sosp(null);
							if (dataSospInizio!=null)
							{
								dirittoNew.setData_inizio_sosp(dataSospInizio);
							}
							dirittoNew.setData_fine_sosp(null);
							if (dataSospFine!=null)
							{
								dirittoNew.setData_fine_sosp(dataSospFine);
							}
							dirittoNew.setId_servizio_id_servizio(new Integer(autorizzazione_servizio.getId_servizio_id_servizio()));
							dirittoNew.setNote("");
							dirittoNew.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione().trim()); //20.01.10
							utentiDao.inserisciDirittoUtente(ute_single,tbl_servizio, dirittoNew);
						}
						else
						{
							if (diritto.getFl_canc()=='S')
							{
								diritto.setFl_canc('N');
								// date da aggiornare
								diritto.setData_inizio_serv(Calendar.getInstance().getTime());
								if (dataFineAut!=null)
								{
									diritto.setData_fine_serv(dataFineAut);
								}
								if (dataSospInizio!=null)
								{
									diritto.setData_inizio_sosp(dataSospInizio);
								}
								diritto.setData_fine_sosp(null);
								if (dataSospFine!=null)
								{
									diritto.setData_fine_sosp(dataSospFine);
								}
								diritto.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione().trim()); //20.01.10
								utentiDao.aggiornaDirittoUtente(diritto);
							}
						}
*/// A QUI

					}

				}
			}

			// Scorro lista servizi gia associati per stabilire se qualche
			// servizio va cancellato
			Iterator iteratorGiaAss = serviziAutGiaPresenti.iterator();
			while (iteratorGiaAss.hasNext()) {
				autorizzazione_servizio = (Trl_autorizzazioni_servizi) iteratorGiaAss.next();

				if (!idServiziDaAss.contains(new Integer(
						autorizzazione_servizio.getId_servizio_id_servizio()))) {
					// L'associazione è da cancellare in quanto non più
					// contenuta tra i servizi da associare
					autorizzazione_servizio.setFl_canc('S');
					autorizzazione_servizio.setTs_var(recAutorizzazione
							.getTsVar());
					autorizzazione_servizio.setUte_var(recAutorizzazione
							.getUteVar());
					autDAO.aggiornamentoAutorizzazioneServizio(autorizzazione_servizio);
					// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI CON GLI UTENTI
					// LEGGO Trl_diritti_utente CON ID SERVIZIO
					// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESSO COD_AUTORIZZAZIONE E FLAGGO I DIRiTTI

					// N.B. poichè tale cancellazione impatta solo su quelli ereditati (perchè hanno cod_aut valorizzato) lascia inalterati i personalizzati
					// RICHIESTA CONTARDI DEL 03.05.10 - vince sempre il diritto personalizzato rispetto a quello ereditato per la stessa famiglia di servizio
					// anche nel caso di cancellazione di diritti, ma questa parte del sw dovrebbe andare bene come è
					List<Trl_diritti_utente>  ute_dir=trl_dirDAO.getDirittoUtenteByID(autorizzazione_servizio.getId_servizio().getId_tipo_servizio().getCd_bib(),new Integer(autorizzazione_servizio.getId_servizio_id_servizio()),recAutorizzazione.getCodAutorizzazione(),false);
					if(ute_dir!=null && ute_dir.size()>0 )
					{
						// si devono flaggare i diritti_utente degli utenti con la specifica autorizzazione
						for (int i = 0; i < ute_dir.size(); i++) {
							Trl_diritti_utente ute_dirSingle= ute_dir.get(i);
							ute_dirSingle.setFl_canc('S');
							utentiDao.aggiornaDirittoUtente(ute_dirSingle);
						}
					}
				}
				else
				{
					//*************************************************************************************************************************
					// TALE CONDIZIONE ALTERNATIVA E' STATA AGGIUNTA TRANSITORIAMENTE PER ADEGUARE RAPIDAMENTE IL CAMPO COD_TIP_AUT SE EREDITATO
					// POTRA' ESSERE RIMOSSA A REGIME
					//*************************************************************************************************************************
					// L'associazione è già presente
					// aggiorno solo il codice autorizzazione
					// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI CON GLI UTENTI
					// LEGGO Trl_diritti_utente CON ID SERVIZIO
					// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESSO COD_AUTORIZZAZIONE
					List<Trl_diritti_utente>  ute_dir=trl_dirDAO.getDirittoUtenteByID(autorizzazione_servizio.getId_servizio().getId_tipo_servizio().getCd_bib(), new Integer(autorizzazione_servizio.getId_servizio_id_servizio()),recAutorizzazione.getCodAutorizzazione(),false);
					if(ute_dir!=null && ute_dir.size()>0 )
					{
						for (int i = 0; i < ute_dir.size(); i++) {
							Trl_diritti_utente ute_dirSingle= ute_dir.get(i);
							ute_dirSingle.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione().trim()); //20.01.10
							utentiDao.aggiornaDirittoUtente(ute_dirSingle);
						}
					}
				}

			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return true;
	}


	private static final String dateToString(java.util.Date data) {
		if (data == null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String value = format.format(data);

		return value;
	}

	public ElaborazioniDifferiteOutputVo eseguiBatchSolleciti(ParametriBatchSollecitiVO params, BatchLogWriter blog)
		throws ApplicationException, EJBException {
		BatchSolleciti batch = new BatchSolleciti(ctx.getUserTransaction(), params, blog);
		return batch.eseguiBatchSolleciti();
	}

	//almaviva5_20120228 migrazione TO0
	public void fixSegnatureDocNoSbn(String ticket) throws ApplicationException, EJBException {

		Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();
		UserTransaction tx = ctx.getUserTransaction();

		String fileName = System.getProperty("user.home") + File.separator + "fixSegnatureDocNoSbn.log";
		FileLog fl = new FileLog("fixSegnatureDocNoSbn", fileName, false);
		Logger _log = fl.getLogger();
		long cnt = 0;

		try {
			_log.debug("PROCEDURA FIX SEGNATURA DOCUMENTI NON SBN");

			DaoManager.begin(tx);
			List<Integer> ids = dao.getListaIDsDocumentoLettore();
			DaoManager.commit(tx);

			if (!ValidazioneDati.isFilled(ids))
				return;

			String uteVar = DaoManager.getFirmaUtente(ticket);

			for (int id : ids)
			try {
				DaoManager.begin(tx);
				Tbl_documenti_lettori doc = dao.getDocumentoLettoreById(id);
				if (doc == null) {
					_log.warn("Documento n. " + id + " non trovato.");
					DaoManager.rollback(tx);
					continue;
				}

				String segn = ValidazioneDati.trimOrEmpty(doc.getSegnatura());
				if (!ValidazioneDati.isFilled(segn)) {
					_log.warn("Il documento n. " + id + " non ha una segnatura valida.");
					DaoManager.rollback(tx);
					continue;
				}
				String ordSegn = ValidazioneDati.trimOrEmpty(OrdinamentoCollocazione2.normalizza(segn));
				if (ValidazioneDati.equals(ordSegn, ValidazioneDati.trimOrEmpty(doc.getOrd_segnatura()))) {
					DaoManager.rollback(tx);
					continue;	//segnatura uguale?
				}

				Tbf_biblioteca_in_polo bib = doc.getCd_bib();
				Tbl_documenti_lettori other = dao.esisteSegnatura(bib.getCd_polo().getCd_polo(),
						bib.getCd_biblioteca(),	ordSegn, doc.getId_documenti_lettore());
				if (other != null) {
					_log.warn("Il documento n. " + id + " ha una segnatura equivalente al documento n. " + other.getId_documenti_lettore() + ". Non aggiornato.");
					DaoManager.rollback(tx);
					continue;
				}

				doc.setOrd_segnatura(ordSegn);
				doc.setUte_var(uteVar);
				dao.saveDocumento(doc);
				DaoManager.commit(tx);
				_log.debug("Documento n. " + id + " aggiornato: '" + segn + "' --> '" + ordSegn +"'");

				cnt++;

			} catch (Exception e) {
				DaoManager.rollback(tx);
				_log.error("Errore aggiornamento documento n. " + id + ": ", e);
				continue;
			}

		} catch (Exception e) {
			DaoManager.rollback(tx);
			_log.error("Errore aggiornamento: ", e);
		} finally {
			_log.debug("Aggiornati " + cnt + " documenti.");
		}

	}

	public ElaborazioniDifferiteOutputVo allineaServerILL(ParametriAllineamentoBibliotecheILLVO params, BatchLogWriter blw)
			throws SbnBaseException {

		AllineamentoBibliotecheILL batch = new AllineamentoBibliotecheILL(ctx.getUserTransaction(), params, blw);
		return batch.allinea();
	}

	public ElaborazioniDifferiteOutputVo importaUtentiESSE3(ParametriBatchImportaUtentiVO pbiuVO, BatchLogWriter log)
			throws SbnBaseException {

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(pbiuVO);
		output.setStato(ConstantsJMS.STATO_ERROR);

		UserTransaction tx = ctx.getUserTransaction();
		try {
			DaoManager.begin(tx);

			String fileName = pbiuVO.getImportFileName();
			String path = BatchImportaUtenti.getImportaUtentiHome() + File.separator;
			log.logWriteLine("Utenti di esse3");
			Esse3DataManagerImpl esse3 = (Esse3DataManagerImpl) new Esse3DataManagerBuilder()
					.setOperationToDo(Esse3OperationType.UPDATE_FROM_CSV).setTicket(pbiuVO.getTicket()).build();
			esse3.manage(pbiuVO.getCodPolo(), pbiuVO.getCodBib(), path + File.separator + fileName);
			List<String> errors = esse3.getErrors();
			List<String> utentiAggiornati = esse3.getUtentiAggiornati();
			List<String> utentiInseriti = esse3.getUtentiInseriti();

			writeLog("---- Statistiche dati WS Esse3");

				writeLog("Utenti Aggiornati: ", utentiAggiornati);
				writeLog("Utenti Inseriti: ", utentiInseriti);
				writeLog("Errori: ", errors);

			writeLog("----");
			output.setStato((errors.size() > 0 && (utentiInseriti.size() == 0 || utentiAggiornati.size() == 0)) ? ConstantsJMS.STATO_ERROR : ConstantsJMS.STATO_OK);

		} catch (Exception e) {
			log.getLogger().error("", e);
			DaoManager.rollback(tx);
		} finally {
			DaoManager.endTransaction(tx, true);
		}

		return output;
	}

	private void writeLog(String message, List<String> list) {
		log.info(message);
		for(String string: list) {
			log.info(string);
		}

	}


	private void writeLog(String message) {
		writeLog(message, Collections.<String>emptyList());
	}



	public Boolean aggiornaUtentiESSE3(String codPolo, String codBib, String ticket, List<PERSONA> persone)
			throws SbnBaseException {
		writeLog("------------- INIZIO ELABORAZIONE DATI WS da chiamata ESSE3 -----------------");
		UserTransaction tx = ctx.getUserTransaction();
		boolean hasImported;
		try {
			DaoManager.begin(tx);

			Esse3DataManagerImpl manager = new Esse3DataManagerImpl(Esse3OperationType.UPDATE_FROM_MODEL, ticket);

			hasImported = manager.manage(codPolo, codBib, persone);

			List<String> errors = manager.getErrors();
			List<String> utentiAggiornati = manager.getUtentiAggiornati();
			List<String> utentiInseriti = manager.getUtentiInseriti();

			writeLog("---- Statistiche dati WS Esse3");

				writeLog("Utenti Aggiornati: ", utentiAggiornati);
				writeLog("Utenti Inseriti: ", utentiInseriti);
				writeLog("Errori: ", errors);

			writeLog("----");
			return hasImported;
		} catch (Exception e) {
			DaoManager.rollback(tx);
			log.error(e);
		return false;
		} finally {
			DaoManager.endTransaction(tx, true);

		}
	}

}
