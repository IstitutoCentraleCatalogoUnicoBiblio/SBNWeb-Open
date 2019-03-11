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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.importa;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBase;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa.ListaDati950VO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.stampe.excel.ExcelPrintUtil;

import java.io.File;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


/**
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="StampaReportImport"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="StampaReportImport"
 *           message-selector="STATO='HELD'"
 *
 * @ejb.resource-ref
 *   res-ref-name="jms/QCF"
 *   res-type="javax.jms.QueueConnectionFactory"
 *   res-auth="Container"
 *
 * @ejb.transaction="Supports"
 * @jboss.destination-jndi-name name="queue/A"
 * @jboss.resource-ref
 * 		res-ref-name="jms/QCF"
 * 		jndi-name="ConnectionFactory"
 * <!-- end-xdoclet-definition -->
 * @generated
 *
 */
public class StampaReportImportBean extends SBNStampeBase  {
	/**
	 *
	 */
	private static final long serialVersionUID = 5381991885729447552L;
	/**
	 *
	 */

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Interrogazione interrogazione;
	private static Logger log = Logger.getLogger(StampaReportImportBean.class);

	/**
	 * Required method for container to set context.
	 *
	 * @generated
	 */
	public void setMessageDrivenContext(MessageDrivenContext mdc) throws EJBException {
		// TODO Auto-generated method stub
		this.ctx = mdc;
	}

	/**
	 * Required removal method for message-driven beans. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void ejbRemove() throws EJBException {
		System.out.println("TextMDB.ejbRemove, this=" + hashCode());
		closeJMS();
	}

	/**
	 * Required creation method for message-driven beans.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method <!-- end-xdoclet-definition -->
	 * @generated
	 */
	public void ejbCreate() {
		try {
			this.interrogazione = DomainEJBFactory.getInstance().getInterrogazione();
			this.setupJMS();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}

	public Object elabora(StampaVo stampaVO, BatchLogWriter blw)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		getDownloadFileName().clear();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
		List listaOutput = new ArrayList();

		//qui metterò i dati da stampare
		List listaEtichette = new ArrayList();

		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {

			pathDownload = stampaVO.getDownload();
			String ticket = stampaVO.getTicket();
			if (stampaVO instanceof StampaOnLineVO) {
				OutputStampaVo output = new OutputStampaVo();
				output.setStato(ConstantsJMS.STATO_OK);
				listaEtichette = ((StampaOnLineVO)stampaVO).getDatiStampa();
				//vado a stampare
				streamRichiestaStampa = effettuaStampa(
						stampaVO.getTemplate(),
						stampaVO.getTipoStampa(),
						listaEtichette);
				if (streamRichiestaStampa != null) {
					output.setOutput(streamRichiestaStampa);
				} else {
					output.setStato(ConstantsJMS.STATO_ERROR);
				}
				return output;
			} else {
				// Stampa differita
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

				log.info("path fileJrxml per le stampe bibliografiche: "+stampaVO.getTemplate());
				System.out.print(""+stampaVO.getTemplate());
				elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

				String modalita = ((StampaDiffVO)stampaVO).getModalita();
				utente = ((StampaDiffVO)stampaVO).getFirmaBatch();
				List parametri = (List)((StampaDiffVO)stampaVO).getParametri();

				StrutturaCombo strutNull = new StrutturaCombo("", "");
				AreaParametriStampaSchedeVo datiStampaSchede = (AreaParametriStampaSchedeVo)parametri.get(0);
				try{
	/*
					listaOutput = this.getInventario().getEtichette(stampaEtichette, tipoOperazione, ticket);
					if(listaOutput == null || listaOutput.size() == 0){
						//						throw new DataException ("Errore nel recupero dei dati, l'output é nullo");
						elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
						nameFile = utente +"_log_etichette_"+idMess+ "." + "txt";
						streamLogStampa = ("La richiesta non ha prodotto risultati per inventari collocati. Controllare il corrispondente log").getBytes("UTF8");
						this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
						listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
						elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
						return elaborazioniDifferiteOutputVo;
					}
	 */

					ParametriExcelVO defaultParams = ExcelPrintUtil.getExcelParametriDefault();

					List<StrutturaCombo> intestazioni = null;
					List<ListaDati950VO> lista950 = null;
					File xlsFile = null;

					ExcelPrintUtil.createExcelFile(defaultParams, intestazioni, lista950, xlsFile);

					datiStampaSchede.setIdBatch(((StampaDiffVO)stampaVO).getIdBatch());
					AreaParametriStampaSchedeVo listaOut = this.getInterrogazione().schedulatorePassiStampaSchede(datiStampaSchede, ticket, blw);

					// Modifiche almaviva2 per gestire sia la Stampa Schede che la StampaCataloghi - 20.01.2010

					// Modifica almaviva2 BUG MANTIS 4404 (esercizio)
					// Si controlla listaOut.getListaBid() per verificare il valore null e non andare in eccezione
					// ma inviare la messaggistica corretta
					if(listaOut.getListaBid() == null){
						if (datiStampaSchede.getCodAttivita().equals("ZG200")) {
							// caso di Stampa Cataloghi
							nameFile = utente +"_log_stampa_cataloghi_"+idMess+ "." + "txt";
						} else if (datiStampaSchede.getCodAttivita().equals("C5020")) {
							// caso di Stampa Schede
							nameFile = utente +"_log_stampa_schede_"+idMess+ "." + "txt";
						}
						elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
						streamLogStampa = ("L'elaborazione non ha prodotto risultati per i prametri introdotti. Controllare il corrispondente log").getBytes("UTF8");
						this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
						listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
						elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
						return elaborazioniDifferiteOutputVo;
					}

					if(listaOut.getListaBid().size() < 1){
						if (datiStampaSchede.getCodAttivita().equals("ZG200")) {
							// caso di Stampa Cataloghi
							nameFile = utente +"_log_stampa_cataloghi_"+idMess+ "." + "txt";
						} else if (datiStampaSchede.getCodAttivita().equals("C5020")) {
							// caso di Stampa Schede
							nameFile = utente +"_log_stampa_schede_"+idMess+ "." + "txt";
						}
						elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
						streamLogStampa = ("L'elaborazione non ha prodotto risultati per inventari richiesti. Controllare il corrispondente log").getBytes("UTF8");
						this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
						listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
						elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
						return elaborazioniDifferiteOutputVo;
					}
					if (datiStampaSchede.getCodErr() == null
							|| !datiStampaSchede.getCodErr().equals("0000")) {
						throw new DataException (datiStampaSchede.getTestoProtocollo());
					}
				} catch(Exception exep) {
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
					blw.logWriteException(exep);
					return elaborazioniDifferiteOutputVo;
				}


				try {
					//l'arraylist con i VO da passare a Jasper è pronto in datiStampaSchede
					//i metodi tipo  getListaPerAutoreSchedeVO() restituiscono gli array pronti per jasper

					//mi serve poi per sapere se ho creato almeno un file
					boolean listaEsistente = false;

					if (datiStampaSchede.getCodAttivita().equals("C5020")){
						//inizio blocco ListaPerAutore
						if(datiStampaSchede.getListaPerAutoreSchedeVO().size()>0){
							//tutto il codice di questo IF va ripetuto per gli altri array

							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerAutoreSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_schede_PerAutore_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerAutore

//						inizio blocco ListaPerClassificazione
						if(datiStampaSchede.getListaPerClassificazioneSchedeVO().size()>0){
							//tutto il codice di questo IF va ripetuto per gli altri array

							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerClassificazioneSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_schede_PerClassificazioni_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerClassificazione

//						inizio blocco ListaPerEditore
						if(datiStampaSchede.getListaPerEditoreSchedeVO().size()>0){
							//tutto il codice di questo IF va ripetuto per gli altri array

							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerEditoreSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_schede_PerEditore_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerEditore

//						inizio blocco ListaPerPossessore
						if(datiStampaSchede.getListaPerPossessoreSchedeVO().size()>0){
							//tutto il codice di questo IF va ripetuto per gli altri array

							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerPossessoreSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_schede_PerPossessore_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerPossessore

//						inizio blocco ListaPerSoggetto
						if(datiStampaSchede.getListaPerSoggettoSchedeVO().size()>0){
							//tutto il codice di questo IF va ripetuto per gli altri array

							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerSoggettoSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_schede_PerSoggetto_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerSoggetto

//						inizio blocco ListaPerTitolo
						if(datiStampaSchede.getListaPerTitoloSchedeVO().size()>0){
							//tutto il codice di questo IF va ripetuto per gli altri array

							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerTitoloSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_schede_PerTitolo_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerTitolo

//						inizio blocco ListaPerTopografico
						if(datiStampaSchede.getListaPerTopograficoSchedeVO().size()>0){
							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerTopograficoSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_schede_PerTopografico_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerTopografico
					} else if (datiStampaSchede.getCodAttivita().equals("ZG200")){
//						inizio blocco ListaPerTitolo
						if(datiStampaSchede.getListaPerTitoloSchedeVO().size()>0){
							//genero la stampa
							streamRichiestaStampa = effettuaStampa(
									stampaVO.getTemplate(),
									stampaVO.getTipoStampa(),
									datiStampaSchede.getListaPerTitoloSchedeVO());
							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_catalografico_PerTitolo_"+idMess+ "." + stampaVO.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampaVO.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								listaEsistente = true;
								listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							}
						}
//						fine blocco ListaPerTitolo
					}



					if(!listaEsistente){
						throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
					}
					//fine blocco ListaPerAutore
					elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
					elaborazioniDifferiteOutputVo.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());

				}catch (Exception ef) {
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
					blw.logWriteException(ef);

					return elaborazioniDifferiteOutputVo;
				}
				return elaborazioniDifferiteOutputVo;
			}

		} catch (Exception ex) {
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			blw.logWriteException(ex);
			return elaborazioniDifferiteOutputVo;
		}
	}

	/**
	 * This method implements the business logic for the EJB.
	 *
	 */

	public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
	}

	public Interrogazione getInterrogazione() {
		if (interrogazione != null)
			return interrogazione;

		try {
			this.interrogazione = DomainEJBFactory.getInstance().getInterrogazione();

			return interrogazione;

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}

}
