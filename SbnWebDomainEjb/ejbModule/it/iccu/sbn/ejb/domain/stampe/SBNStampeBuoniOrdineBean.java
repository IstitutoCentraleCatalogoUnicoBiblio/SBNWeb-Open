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
package it.iccu.sbn.ejb.domain.stampe;


import gnu.trove.THashMap;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuoniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuonoOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.OrdineStampaOnlineVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaOrdiniDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStrumentiPatrimonioDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


public class SBNStampeBuoniOrdineBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = -5366897334453604042L;


	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Acquisizioni acquisizioni;
	private static Logger log = Logger.getLogger(SBNStampeBuoniOrdineBean.class);

	Acquisizioni getEjbAcq() throws Exception {
		return DomainEJBFactory.getInstance().getAcquisizioni();
	}



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
			this.acquisizioni = DomainEJBFactory.getInstance().getAcquisizioni();
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

	public Object elabora(StampaVo input, BatchLogWriter blw)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		StampaOrdiniDiffVO stampaVO = (StampaOrdiniDiffVO) input;
		getDownloadFileName().clear(); //da aggiungere per eliminare n nomi di file in output nella sintetica dello stato delle richieste
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";
		StampaVo stampavo=stampaVO.getStampavo();

		//qui metterò i dati da stampare
		List datiDaStampare = new ArrayList();

		String pathDownload = null;
		String downloadLinkPath = stampavo.getDownloadLinkPath();
		String utente = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {
			pathDownload = stampavo.getDownload();

			if (stampavo instanceof StampaOnLineVO)
				return eseguiStampaOnline(stampavo);
			else {
				// Stampa differita
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

				log.info("path fileJrxml per la stampa buoni ordine: "+stampavo.getTemplate());
				System.out.print(""+stampavo.getTemplate());
				elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

				String modalita = ((StampaDiffVO)stampavo).getModalita();
				utente = ((StampaDiffVO)stampavo).getUser();
				List parametri = (List)((StampaDiffVO)stampavo).getParametri();
				//TODO cance prendere l'elemento zero dai paramentri, che deve contenere quanto occorre per chiamare il servizio
				//***VO richiesta=(***VO)parametri.get(0);
				ListaSuppOrdiniVO lpVO=(ListaSuppOrdiniVO)parametri.get(0);
				try{
					List listaOutput = new ArrayList();
					//TODO cance chiamare il servizio per riempire lista output con quanto richiesto
					List listaOutputAppo = new ArrayList();
					try{
					listaOutputAppo =getEjbAcq().getRicercaListaOrdini(lpVO);
					} catch(Exception e) {
						e.printStackTrace();
					}
					// gestione assenza risultati
					if(listaOutputAppo == null || listaOutputAppo.size() == 0){
						elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
						nameFile = utente +"_log_buoni_ordine_"+idMess+ "." + "txt";
						streamLogStampa = ("La richiesta non ha prodotto risultati perchè assenti o corrispondenti a tipologie di ordini in stato chiuso/annullato").getBytes("UTF8");
						this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
						listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
						elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
						return elaborazioniDifferiteOutputVo;
					}


					ConfigurazioneBOVO confBO=new ConfigurazioneBOVO();
					confBO.setCodBibl(stampaVO.getCodBib());
					confBO.setCodPolo(stampaVO.getCodPolo());
					ConfigurazioneBOVO configurazione=getEjbAcq().loadConfigurazione(confBO);
					if (configurazione==null)
					{
						configurazione=confBO;
						configurazione.setUtente(utente);
						//throw new Exception("non esiste una configurazione di stampa per la biblioteca operante.");
					}
					//List<StampaBuoniVO> risultatoPerStampa=this.impostaBuonoOrdineDaStampare(configurazione, listaOutputAppo,"ORD", stampaVO.getTicket(), utente);
					Boolean ristampa=false;
					if (lpVO.isStampato())
					{
						ristampa=true;
					}

					List<StampaBuoniVO> risultatoPerStampa=getEjbAcq().impostaBuoniOrdineDaStampare( configurazione, listaOutputAppo, "ORD", ristampa,  stampaVO.getTicket(), utente, lpVO.getDenoBiblStampe());
					listaOutput=risultatoPerStampa;
					datiDaStampare=listaOutput;
					BatchManager.getBatchManagerInstance().checkForInterruption(idMess);

					//TODO GVCANCE
				} catch(Exception exep) {
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
					blw.logWriteException(exep);
					return elaborazioniDifferiteOutputVo;
				}


				try {
					//mi serve poi per sapere se ho creato almeno un file
					boolean listaEsistente = false;

					if(datiDaStampare.size()>0){

						List pezziDatiDaStampare=null;
						int k=0;
						if (((List <ListaSuppOrdiniVO>)stampaVO.getParametri()).get(0).isStampaFiledistinti()){
							// stampa in file distinti
							for (k=0; k< datiDaStampare.size(); k++)	{
								pezziDatiDaStampare=new ArrayList();
								pezziDatiDaStampare.add(datiDaStampare.get(k));
								//genero la stampa
								streamRichiestaStampa = effettuaStampa(
										stampavo.getTemplate(),
										stampavo.getTipoStampa(),
										pezziDatiDaStampare);  //  datiDaStampare
								//metto la stampa generata nella lista dei file da scaricare
								if(streamRichiestaStampa !=null){
									try {
										// aggiungo nel nome il codice fornitore e il progressivo del file
										nameFile = stampaVO.getFirmaBatch() +"_Forn_"+ ((StampaBuoniVO)datiDaStampare.get(k)).getAnagFornitore().getCodFornitore().trim()+ "_nr_" +String.valueOf(k)+  "." + stampavo.getTipoStampa().toLowerCase();
										this.scriviFile(utente, stampavo.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
									}catch (Exception ef) {
										throw new Exception("Creazione del file"+ nameFile+"fallita");
									}
								}
							} // fine for
							String filename = "";
							for(int index = 0; index < getDownloadFileName().size(); index++){
								listaEsistente = true;
								filename = (getDownloadFileName().get(index));
								listaDownload.add(new DownloadVO(filename, downloadLinkPath+filename));
							}
						}else{
							//genero la stampa file unico
							streamRichiestaStampa = effettuaStampa(
									stampavo.getTemplate(),
									stampavo.getTipoStampa(),
									datiDaStampare);  //  datiDaStampare

							//metto la stampa generata nella lista dei file da scaricare
							if(streamRichiestaStampa !=null){
								try {
									//nameFile = utente +"_buoni_ordine_"+idMess+ "." + stampavo.getTipoStampa().toLowerCase();
									nameFile = stampaVO.getFirmaBatch()+ "." + stampavo.getTipoStampa().toLowerCase();
									this.scriviFile(utente, stampavo.getTipoStampa(), streamRichiestaStampa, pathDownload, nameFile);
								}catch (Exception ef) {
									throw new Exception("Creazione del file"+ nameFile+"fallita");
								}
								String filename = "";
								for(int index = 0; index < getDownloadFileName().size(); index++){
									listaEsistente = true;
									filename = (getDownloadFileName().get(index));
									listaDownload.add(new DownloadVO(filename, downloadLinkPath+filename));
								}
							}
						}

					}

					if(!listaEsistente){
						throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
					}
					elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
					elaborazioniDifferiteOutputVo.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());
					// elaborazione andata a buon fine (codice aggiunto da almaviva4 per la gestione dello stato di stampato)
					if(listaEsistente && datiDaStampare!=null && datiDaStampare.size()>0)
					{
						boolean risultato=false;
						try {
							risultato=getEjbAcq().gestioneStampaOrdini(((StampaBuoniVO)datiDaStampare.get(0)).getListaOggDaStampare(),null,"ORD",((StampaBuoniVO)datiDaStampare.get(0)).getUtente(),"");
						}catch (Exception ef) {
 							throw new Exception("errore nell'aggiornamento del check stampato ");
						}
 						if (!risultato)
 						{
 							throw new Exception("errore nell'aggiornamento del check stampato ");
 						}
					}


				}catch (Exception ef) {
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
					blw.logWriteException(ef);
					return elaborazioniDifferiteOutputVo;
				}
				return elaborazioniDifferiteOutputVo;
			}

		} catch (Exception ex) {
			log.error(ex.getMessage());
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			nameFile = utente +"_log_buoni_ordine_"+idMess+ "." + "txt";
			listaDownload.add(new DownloadVO(nomeFileErr, downloadLinkPath+nameFile));
			elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
			return elaborazioniDifferiteOutputVo;
		}
	}



	private Object eseguiStampaOnline(StampaVo params) throws Exception {
		Map<String, Object> props = new THashMap<String, Object>();
		InputStream streamRichiestaStampa;
		OutputStampaVo output = new OutputStampaVo();
		output.setStato(ConstantsJMS.STATO_OK);
		List datiDaStampare = ((StampaOnLineVO) params).getDatiStampa();

		String op = params.getTipoOperazione();//
		if (ValidazioneDati.equals(op, StampaType.STAMPA_ORDINE_RILEGATURA.name()) ) {

			StampaBuonoOrdineVO sbo = (StampaBuonoOrdineVO) datiDaStampare.get(0);
			OrdiniVO ordine = sbo.getListaOrdiniDaStampare().get(0);
			CommandInvokeVO cmd = CommandInvokeVO.build(params.getTicket(), CommandType.ACQ_STAMPA_ORDINE_RILEGATURA, ordine);
			CommandResultVO result = getEjbAcq().invoke(cmd);
			result.throwError();
			datiDaStampare = (List) result.getResult();
		}

		if (ValidazioneDati.equals(op, StampaType.STAMPA_PATRON_CARD.name()) ) {

			StampaBuonoOrdineVO sbo = (StampaBuonoOrdineVO) datiDaStampare.get(0);
//			OrdiniVO ordine = sbo.getListaOrdiniDaStampare().get(0);
//			CommandInvokeVO cmd = CommandInvokeVO.build(params.getTicket(), CommandType.ACQ_STAMPA_PATRON_CARD, ordine);
//			CommandResultVO result = getEjbAcq().invoke(cmd);
//			result.throwError();
			datiDaStampare = sbo.getListaOrdiniDaStampare();
		}

		if (ValidazioneDati.equals(op, StampaType.STAMPA_CART_ROUTING_SHEET.name()) ) {

			StampaBuonoOrdineVO sbo = (StampaBuonoOrdineVO) datiDaStampare.get(0);
			OrdiniVO ordine = sbo.getListaOrdiniDaStampare().get(0);
			CommandInvokeVO cmd = CommandInvokeVO.build(params.getTicket(), CommandType.ACQ_STAMPA_CART_ROUTING_SHEET, ordine);
			CommandResultVO result = getEjbAcq().invoke(cmd);
			result.throwError();
			datiDaStampare = ValidazioneDati.asSingletonList(result.getResult());
		}

		//almaviva5_20130412 evolutive google
		if (ValidazioneDati.equals(op, StampaType.STAMPA_MODULO_PRELIEVO.name()) ) {

			StampaBuonoOrdineVO sbo = (StampaBuonoOrdineVO) datiDaStampare.get(0);
			OrdiniVO ordine = sbo.getListaOrdiniDaStampare().get(0);
			OrdineStampaOnlineVO oso = (OrdineStampaOnlineVO) params;
			oso.setOrdine(ordine);
			CommandInvokeVO cmd = CommandInvokeVO.build(params.getTicket(), CommandType.ACQ_STAMPA_MODULO_PRELIEVO, oso);
			CommandResultVO result = getEjbAcq().invoke(cmd);
			result.throwError();
			datiDaStampare = (List) result.getResult();
			StampaStrumentiPatrimonioDettaglioVO sspd = (StampaStrumentiPatrimonioDettaglioVO) ValidazioneDati.first(datiDaStampare);
			props.put("start", sspd.getContInput());
		}

		// vado a stampare
		streamRichiestaStampa = effettuaStampa(params.getTemplate(),
				params.getTipoStampa(), datiDaStampare, props);
		if (streamRichiestaStampa != null) {
			output.setOutput(streamRichiestaStampa);
		} else {
			output.setStato(ConstantsJMS.STATO_ERROR);
		}
		return output;
	}


	/**
	 * This method implements the business logic for the EJB.
	 *
	 */
	public void onMessage(Message message)  {
	}

	public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
	}




}
