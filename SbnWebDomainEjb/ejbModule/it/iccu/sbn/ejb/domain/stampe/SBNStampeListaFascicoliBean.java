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

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBN;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaListaFascicoliVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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


/**
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="SBNRegistroConservazione"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="SBNRegistroConservazione"
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
public class SBNStampeListaFascicoliBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 8535319226501509922L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private PeriodiciSBN periodiciSBN;
	static Logger log = Logger.getLogger(SBNStampeListaFascicoliBean.class);

	/**
	 * Required method for container to set context.
	 *
	 * @generated
	 */
	public void setMessageDrivenContext(MessageDrivenContext mdc) throws EJBException {
		this.ctx = mdc;
	}

	/**
	 * Required removal method for message-driven beans. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void ejbRemove() throws EJBException {
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
			this.periodiciSBN = DomainEJBFactory.getInstance().getPeriodici();
			this.setupJMS();
		} catch (NamingException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
//		} catch (RemoteException e) {

//			log.error("", e);
//			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}

	public PeriodiciSBN getPeriodiciSBN() {
		if (periodiciSBN != null)
			return periodiciSBN;

		try {
			this.periodiciSBN = DomainEJBFactory.getInstance().getPeriodici();

			return periodiciSBN;

		} catch (NamingException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
//		} catch (RemoteException e) {

//			log.error("", e);
//			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}
	public Object elabora(StampaVo stampaVO, BatchLogWriter blw)
	throws Exception {
		getDownloadFileName().clear();
		Logger logger = blw.getLogger();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
		List lf = new ArrayList();


		String fileJrxml = null;
		String tipoStampa = null;
		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {
			fileJrxml = stampaVO.getTemplate();
			pathDownload = stampaVO.getDownload();
			tipoStampa = stampaVO.getTipoStampa();
			String ticket = stampaVO.getTicket();
			String textListEm="ATTENZIONE: fascicoli richiesti non presenti, cambiare i parametri di ricerca";
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			logger.info("path fileJrxml per le stampa lista fascicoli "+fileJrxml);
			System.out.print(""+fileJrxml);

			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampaVO).getUser();
			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();
			StampaListaFascicoliVO stampaLCVO = (StampaListaFascicoliVO)parametri.get(0);
			boolean listaFascicoli = false;

			try{
				listaFascicoli = this.getPeriodiciSBN().getListaFascicoliPerStampa(stampaLCVO, ticket, blw);
				if (stampaLCVO == null || stampaLCVO.getLista().size() == 0 ){
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					nameFile = utente +"_log_lista_fascicoli_"+idMess+ "." + "txt";
					streamLogStampa = ("La richiesta non ha prodotto risultati").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					elaborazioniDifferiteOutputVo.addDownload(nameFile, downloadLinkPath + nameFile);

					return elaborazioniDifferiteOutputVo;
				}
//				for(int k= 0; k<stampaLCVO.getLista().size(); k++){
//					//l'assegnazione che segue serve solo a generare una eccezione se c'è un problema
//					StampaListaFascicoliVO listaLF = (StampaListaFascicoliVO)stampaLCVO.getLista().get(k);
//				}
			}catch(Exception exep){
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(exep);

				return elaborazioniDifferiteOutputVo;
			}
			try{

				stampaLCVO.setDataDiElaborazione(DateUtil.formattaData(DaoManager.now().getTime()));
				if (stampaLCVO.getOrdinamento() != null){
					if (stampaLCVO.getOrdinamento().equals("01")){
						stampaLCVO.setOrdinamento("Alfabetico");
					}else if (stampaLCVO.getOrdinamento().equals("02")){
						stampaLCVO.setOrdinamento("Cronologico (data ordine)");
					}else if (stampaLCVO.getOrdinamento().equals("03")){
						stampaLCVO.setOrdinamento("Fornitore (nome fornitore)");
					}else if (stampaLCVO.getOrdinamento().equals("04")){
						stampaLCVO.setOrdinamento("Stato amministrativo");
					}else if (stampaLCVO.getOrdinamento().equals("05")){
						stampaLCVO.setOrdinamento("Tipologia ordine");
					}
				}
				if (stampaLCVO.getStampaNote() != null){
					if (stampaLCVO.getStampaNote().equals("01")){
						stampaLCVO.setStampaNote("No");
					}else if (stampaLCVO.getStampaNote().equals("02")){
						stampaLCVO.setStampaNote("Sì");
					}
				}
				if (stampaLCVO.getStatoFascicolo() != null){
					if (stampaLCVO.getStatoFascicolo().equals("01")){
						stampaLCVO.setStatoFascicolo("Fascicoli attesi");
					}else if (stampaLCVO.getStatoFascicolo().equals("02")){
						stampaLCVO.setStatoFascicolo("Fascicoli attesi ed in lacuna");
					}else if (stampaLCVO.getStatoFascicolo().equals("03")){
						stampaLCVO.setStatoFascicolo("Fascicoli in lacuna");
					}else if (stampaLCVO.getStatoFascicolo().equals("04")){
						stampaLCVO.setStatoFascicolo("Schedone abbonamento");
					}
				}
				stampaLCVO.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());

				lf.add(stampaLCVO);

				boolean listaEsistente = false;
				if(stampaLCVO.getLista().size()>0){
					//genero la stampa
					streamRichiestaStampa = effettuaStampa(
							stampaVO.getTemplate(),
							stampaVO.getTipoStampa(),
							lf);
					//metto la stampa generata nella lista dei file da scaricare
					if(streamRichiestaStampa !=null){
						try {
							nameFile =  stampaVO.getFirmaBatch() +"_lista_fascicoli_"+idMess+ "." + tipoStampa;
							this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);
						}catch (Exception ef) {
							throw new Exception("Creazione del file"+ nameFile+"fallita");
						}
						String filename = "";

						for(int index = 0; index < getDownloadFileName().size(); index++){
							listaEsistente = true;
							filename = (getDownloadFileName().get(index));
							elaborazioniDifferiteOutputVo.addDownload(filename, downloadLinkPath + filename);
						}
						if(!listaEsistente){
							throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
						}

						elaborazioniDifferiteOutputVo.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());
						// Preparare hash map dei parametri
						Map<String, String> hm = new HashMap<String, String>();
						String property;
						//sriivo é il nome del parametro risultante dalla chiamata del servizio
						property = stampaLCVO.getCodBib();
						if (property != null && property.length() > 0) {
							hm.put("Codice biblioteca", property);
						}

						property = stampaLCVO.getCodPolo();
						if (property != null && property.length() > 0) {
							hm.put("Polo", property);
						}
						elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

					}else{
						streamRichiestaStampa = new ByteArrayInputStream(textListEm.getBytes());
					}
				}
			}catch (Exception ef) {
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(ef);
				return elaborazioniDifferiteOutputVo;
			}
			return elaborazioniDifferiteOutputVo;
		} catch (Exception ex) {
			// DEVO GESTIRE LE ECCEZIONI PERSONALIZZANDO L'ERRORE
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			blw.logWriteException(ex);

			return elaborazioniDifferiteOutputVo;
		}
	}
	/**
	 * This method implements the business logic for the EJB.
	 *
	 */
	public void onMessage(Message message)  {	}

	public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
	}

}
