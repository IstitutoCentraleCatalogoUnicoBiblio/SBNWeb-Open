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
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaFornitoriDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaFornitoriVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


/**
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="SBNStampeFornitori"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="SBNStampeFornitori"
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
public class SBNStampeFornitoriBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 163243003536031560L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Acquisizioni acquisizioni;
    private static Logger log = Logger.getLogger(SBNStampeFornitoriBean.class);

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
		}catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}


	public Object elabora(StampaVo input, BatchLogWriter blw)
	throws Exception {
		StampaFornitoriDiffVO stampaVO = (StampaFornitoriDiffVO) input;
		getDownloadFileName().clear(); // da aggiungere per eliminare n nomi di file in output nella sintetica dello stato delle richieste
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";
		StampaVo stampavo=stampaVO.getStampavo();

		String pathDownload = null;
		String downloadLinkPath = stampavo.getDownloadLinkPath();
		String utente = null;
		String fileJrxml = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		fileJrxml = stampavo.getTemplate();
		pathDownload = stampavo.getDownload();
		String ticket = stampavo.getTicket();
		if (stampavo instanceof StampaOnLineVO) {
			//stampa online
			OutputStampaVo output = new OutputStampaVo();
			output.setStato(ConstantsJMS.STATO_OK);
			List listaFornitori = ((StampaOnLineVO)stampavo).getDatiStampa();
			streamRichiestaStampa = effettuaStampa(
				fileJrxml,
				stampavo.getTipoStampa(),
				listaFornitori
			);

			//se tutto è Ok ritorno come stream la stampa generata
			if (streamRichiestaStampa != null) {
				output.setOutput(streamRichiestaStampa);
			} else {
				output.setStato(ConstantsJMS.STATO_ERROR);
			}
			return output;
		} else {
			// Stampa differita
			ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			log.info("path fileJrxml per le stampe fornitori"+fileJrxml);
			System.out.print(""+fileJrxml);
			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampavo).getUser();
			List parametri = (List)((StampaDiffVO)stampavo).getParametri();

			StampaFornitoriVO stampaFornitori = (StampaFornitoriVO)parametri.get(0);
			ListaSuppFornitoreVO lpVO = new  ListaSuppFornitoreVO(
				stampaFornitori.getPolo(),
				stampaFornitori.getBiblioteca(),
				stampaFornitori.getCodiceFornitore(),
				stampaFornitori.getNomeFornitore(),
				stampaFornitori.getProfAcquisti(),
				stampaFornitori.getPaese(),
				stampaFornitori.getTipoFornitore(),
				stampaFornitori.getProvincia(),
				null,
				null
			);
			lpVO.setOrdinamento(stampaVO.getTipoOrdinamento());
			lpVO.setLocale(stampaFornitori.getRicercaLocale());
			lpVO.setStampaForn(true);

			List listaOutput = new ArrayList();
			try{
				listaOutput =getEjbAcq().getRicercaListaFornitori(lpVO);;
			} catch(Exception e) {
				e.printStackTrace();
			}
			// gestione assenza risultati
			if(listaOutput == null || listaOutput.size() == 0){
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
				nameFile = utente +"_log_fornitori_"+idMess+ "." + "txt";
				streamLogStampa = ("La richiesta non ha prodotto risultati perchè assenti").getBytes("UTF8");
				this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
				listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
				elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
				return elaborazioniDifferiteOutputVo;
			}

			try {

				BatchManager.getBatchManagerInstance().checkForInterruption(idMess);

				//vado a stampare
				String tipoStampa=stampavo.getTipoStampa();
				streamRichiestaStampa = effettuaStampa(
						fileJrxml,
						tipoStampa,
						listaOutput
				);

				//metto la stampa generata nella lista dei file da scaricare
				boolean listaEsistente = false;
				if(streamRichiestaStampa !=null){
					try {
						//nameFile = utente +"_fornitori_"+idMess+ "." + tipoStampa;
						nameFile = stampaVO.getFirmaBatch()+ "." + stampavo.getTipoStampa().toLowerCase();
						this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);//idMessaggio
					}catch (Exception ef) {
						throw new Exception("Creazione del file"+ nameFile+"fallita");
					}
					String filename = "";
					for(int index = 0; index < getDownloadFileName().size(); index++){
						listaEsistente = true;
						filename = (getDownloadFileName().get(index));
						listaDownload.add(new DownloadVO(filename, downloadLinkPath+filename));
					}
					if(!listaEsistente){
						throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
					}
					elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
					elaborazioniDifferiteOutputVo.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());
					// Preparare hash map dei parametri
					Map hm = new HashMap();
					String property;
					property = stampaFornitori.getCodiceFornitore();
					if (property != null && property.length() > 0)
						hm.put("Codice fornitore", property);
					property = stampaFornitori.getIndirizzo();
					if (property != null && property.length() > 0)
						hm.put("Indirizzo", property);
					property = stampaFornitori.getPaese();
					if (property != null && property.length() > 0)
						hm.put("Paese", property);
					property = stampaFornitori.getBiblioteca();
					if (property != null && property.length() > 0)
						hm.put("Biblioteca", property);
					property = stampaFornitori.getCitta();
					if (property != null && property.length() > 0)
						hm.put("Citt'", property);
					property = stampaFornitori.getNomeFornitore();
					if (property != null && property.length() > 0)
						hm.put("Nome fornitore", property);
				} else {
					String textListEm="ATTENZIONE: biblioteca ricercata inesistente, cambiare i parametri di ricerca.";
					streamRichiestaStampa = new ByteArrayInputStream(textListEm.getBytes());
				}
			}catch (Exception ef) {
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(ef);
				return elaborazioniDifferiteOutputVo;
			}
			return elaborazioniDifferiteOutputVo;
		}
	}
/*
	public Object elabora(StampaVo stampaVO, String idMessaggio)
	throws Exception {
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		int indiceChar= idMessaggio.indexOf(":");
		String idMess= idMessaggio.substring(indiceChar+1, idMessaggio.length());
		String nameFile = "";

		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		String fileJrxml = null;
		byte[] streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		fileJrxml = stampaVO.getTemplate();
		pathDownload = stampaVO.getDownload();
		String ticket = stampaVO.getTicket();
		if (stampaVO instanceof StampaOnLineVO) {
			//stampa online
			OutputStampaVo output = new OutputStampaVo();
			output.setStato(ConstantsJMS.STATO_OK);
			List listaFornitori = ((StampaOnLineVO)stampaVO).getDatiStampa();
			streamRichiestaStampa = effettuaStampa(
				fileJrxml,
				stampaVO.getTipoStampa(),
				listaFornitori
			);

			//se tutto è Ok ritorno come stream la stampa generata
			if (streamRichiestaStampa != null) {
				output.setOutput(streamRichiestaStampa);
			} else {
				output.setStato(ConstantsJMS.STATO_ERROR);
			}
			return output;
		} else {
			// Stampa differita
			ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			log.info("path fileJrxml per le stampe fornitori"+fileJrxml);
			System.out.print(""+fileJrxml);
			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampaVO).getUser();
			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();

			StampaFornitoriVO stampaFornitori = (StampaFornitoriVO)parametri.get(0);
			ListaSuppFornitoreVO lpVO = new  ListaSuppFornitoreVO(
				stampaFornitori.getPolo(),
				stampaFornitori.getBiblioteca(),
				stampaFornitori.getCodiceFornitore(),
				stampaFornitori.getNomeFornitore(),
				stampaFornitori.getProfAcquisti(),
				stampaFornitori.getPaese(),
				stampaFornitori.getTipoFornitore(),
				stampaFornitori.getProvincia(),
				null,
				null
			);
			lpVO.setOrdinamento(stampaVO.getTipoOrdinamento());
			lpVO.setLocale(stampaFornitori.getRicercaLocale());
			List listaOutput = this.acquisizioni.getRicercaListaFornitori(lpVO);
			try {
				//vado a stampare
				String tipoStampa=stampaVO.getTipoStampa();
				streamRichiestaStampa = effettuaStampa(
						fileJrxml,
						tipoStampa,
						listaOutput
				);

				//metto la stampa generata nella lista dei file da scaricare
				boolean listaEsistente = false;
				if(streamRichiestaStampa !=null){
					try {
						nameFile = utente +"_fornitori_"+idMess+ "." + tipoStampa;
						this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);//idMessaggio
					}catch (Exception ef) {
						throw new Exception("Creazione del file"+ nameFile+"fallita");
					}
					String filename = "";
					for(int index = 0; index < getDownloadFileName().size(); index++){
						listaEsistente = true;
						filename = (String)(getDownloadFileName().get(index));
						listaDownload.add(new DownloadVO(filename, downloadLinkPath+filename));
					}
					if(!listaEsistente){
						throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
					}
					elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
					elaborazioniDifferiteOutputVo.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());
					// Preparare hash map dei parametri
					Map hm = new HashMap();
					String property;
					property = stampaFornitori.getCodiceFornitore();
					if (property != null && property.length() > 0)
						hm.put("Codice fornitore", property);
					property = stampaFornitori.getIndirizzo();
					if (property != null && property.length() > 0)
						hm.put("Indirizzo", property);
					property = stampaFornitori.getPaese();
					if (property != null && property.length() > 0)
						hm.put("Paese", property);
					property = stampaFornitori.getBiblioteca();
					if (property != null && property.length() > 0)
						hm.put("Biblioteca", property);
					property = stampaFornitori.getCitta();
					if (property != null && property.length() > 0)
						hm.put("Citt'", property);
					property = stampaFornitori.getNomeFornitore();
					if (property != null && property.length() > 0)
						hm.put("Nome fornitore", property);
				} else {
					String textListEm="ATTENZIONE: biblioteca ricercata inesistente, cambiare i parametri di ricerca.";
					streamRichiestaStampa =textListEm.getBytes();
				}
			}catch (Exception ef) {
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(ef);
				return elaborazioniDifferiteOutputVo;
			}
			return elaborazioniDifferiteOutputVo;
		}
	}
*/

	/**
	 * This method implements the business logic for the EJB.
	 *
	 * <p>
	 * Make sure that the business logic accounts for asynchronous message
	 * processing. For example, it cannot be assumed that the EJB receives
	 * messages in the order they were sent by the client. Instance pooling
	 * within the container means that messages are not received or processed in
	 * a sequential order, although individual onMessage() calls to a given
	 * message-driven bean instance are serialized.
	 *
	 * <p>
	 * The <code>onMessage()</code> method is required, and must take a single
	 * parameter of type javax.jms.Message. The throws clause (if used) must not
	 * include an application exception. Must not be declared as final or
	 * static.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @throws JMSException
	 * @throws JMSException
	 *
	 * @generated
	 */
	public void onMessage(Message message)  {
//		// Prelevo la coda dove effettuare la reply
//		String propertyName = "";
//		getDownloadFileName().clear();
//		try {
//			Queue dest = (Queue) message.getJMSReplyTo();
//			// Imposto lo stato in EXEC
//			//this.reply(message, dest, "EXEC");
////			OutputStampaVo output = null;
//			ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = null;
//			StampaVo stampaVO=null;
//			try {
//				// Effetuo la elaborazione
////				this.elabora(message);
//				if (message instanceof ObjectMessage) {
//					stampaVO = (StampaVo)((ObjectMessage) message).getObject();
//					elaborazioniDifferiteOutputVo = (ElaborazioniDifferiteOutputVo)this.elabora(stampaVO, message.getJMSMessageID());
//
//					//Imposto l'identificativo del messsaggio affinché questo possa essere recuperato
//					//dall'utente
////					message.setJMSMessageID(message.getJMSCorrelationID());
//
////					output.setMessageID(message.getJMSCorrelationID());
//
//					log.info("Terminata l'elaborazione del messaggio in SBNStampeFornitoriBean");
//					// Notifico la fine dell'elaborazione
//					// DEVO CREARE UN NUOVO MESSAGE
//
//					propertyName = "L'elaborazione é andata a buon fine";
//					//((ObjectMessage) message).setObject(propertyName);
//
////					this.reply(message, dest, output.getStato());//(ObjectMessage, .setObject(propertyName))
////					this.reply(message, dest, output);//(ObjectMessage, .setObject(propertyName))
//					this.reply(message, dest, elaborazioniDifferiteOutputVo);//(ObjectMessage, .setObject(propertyName))
//
//
//
//					log.info("Inoltrato il messaggio alla coda, con stato di elaborazione OK e contenuto del messaggio: "+propertyName);
//				}
//				else {
//					throw new Exception("Failed to init TextMDB");
//				}
//
//			} catch (Exception ex) {
//				// DEVO CREARE UN NUOVO MESSAGE
//				propertyName = "L'elaborazione non é andata a buon fine";
////				((ObjectMessage) message).setObject(propertyName);
//				if (elaborazioniDifferiteOutputVo == null)
//					elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
//				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
//				this.reply(message, dest, elaborazioniDifferiteOutputVo); // "ERROR"
//				log.info("Inoltrato il messaggio alla coda, con stato di elaborazione ERROR e contenuto del messaggio: "+propertyName);
//				log.error(ex.getMessage());
//				new Exception("L'elaborazione non é andata a buon fine!", ex);
//			}
//
//		} catch (Exception ex2) {
//
////				// DEVO CREARE UN NUOVO MESSAGE
////				propertyName = "Non é stato possibile effettuare la reply";
//				log.error(ex2.getMessage());
//				new Exception("Non é stato possibile effettuare la reply", ex2);
//
//		}finally{
////			try{
//////				 DEVO CREARE UN NUOVO MESSAGE
////				propertyName = "Non é stato possibile effettuare la reply";
////				message.setObjectProperty(propertyName, message.getObjectProperty("STATO"));
////			}catch (JMSException jmsE){
////
////				new JMSException("Non é stato possibile effettuare la reply");
////			}
//		}
	}


//	//memorizza lo stream di byte nel percorso perfissato
//    public void scriviFile(String user,  String formatoStampa, byte[] streamByte) throws Exception {
//
//    	String dirSave="/tmp";//C:\\stampe
//		UtilityCastor util= new UtilityCastor();
//		String dataCorr = util.getCurrentDate();
//    	String filename = user +"_fornitori_"+dataCorr+ "." + formatoStampa;//+String.valueOf(System.currentTimeMillis()) + "." + formatoStampa;
////      int indiceDot = fileJrxml.lastIndexOf("/");
//    	File newFile = null;
//    	int i = -1;
//    	while(i <= 0){
//	        try {
//	            newFile = new File(dirSave, filename);
//	        } catch (NullPointerException exception) {
//	        	exception.printStackTrace();
//	        }
//	        if(newFile.exists())
//	        {
//	        	newFile.delete();
//	        }
//	        i=1;
//	        try
//	        {
//	        	newFile.createNewFile();
//	        }
//	        catch(IOException exError)
//	        {
////	            System.out.println("Errore nella creazione del file per il test");
//	            dirSave = "C:\\stampe";
//	            i=0;
//	        }
//    	}
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile, false));
//        bos.write(streamByte, 0, streamByte.length);
//        bos.flush();
//		bos.close();
//		log.info("Terminato lo storage della stampa richiesta di fornitori nella directory indicata"+dirSave);
//    }

    private List ordinaFornitori(List listaSFVO, String arrayFiltri, String tipoOrdinamento){
    	List arrayFornitoriOrd = new ArrayList();
    	List arrayFornitoriOrdD;
    	FornitoreVO appListaSF = null;
    	FornitoreVO listaSF = null;
    	String fornCompFirst = "";
    	String fornCompSecond = "";
    	int fornCodFirst = -1;
    	int fornCodSec = -1;
    	int resultCompare;
    	int elemLista = listaSFVO.size();
    	if(arrayFiltri.equals("codFornitore")){
    		while(listaSFVO.size()>0){
    			if(fornCodFirst == -1){
        			listaSF = (FornitoreVO)listaSFVO.get(0);//iter.next();
    			}
    			fornCodFirst = (Integer.valueOf(listaSF.getCodFornitore())).intValue();
    			listaSFVO.remove(listaSF);
    			if(listaSFVO.size()>0){
    				appListaSF = (FornitoreVO)listaSFVO.get(0);//iter.next();
    				fornCodSec = (Integer.valueOf(appListaSF.getCodFornitore())).intValue();
    				if(fornCodFirst < fornCodSec){
    					arrayFornitoriOrd.add(listaSF);
    					elemLista--;
    					//NB: fornCompareFirst l'ho già rimosso
    					//dall'array finale da restituire
    				}else if(fornCodFirst > fornCodSec){
    					listaSFVO.add(listaSF);
    					fornCodFirst = fornCodSec;
    					listaSFVO.remove(appListaSF);
    				}
    				listaSF = appListaSF;
    			}
    		}
    	}else if(arrayFiltri.equals("nomeFornitore")){
    		while(listaSFVO.size()>0){
    			if(fornCompFirst.equals("") ){
        			listaSF = (FornitoreVO)listaSFVO.get(0);//iter.next();
    			}
    			fornCompFirst = listaSF.getNomeFornitore();
    			listaSFVO.remove(listaSF);
    			if(listaSFVO.size()>0){
    				appListaSF = (FornitoreVO)listaSFVO.get(0);//iter.next();
    				fornCompSecond = appListaSF.getNomeFornitore();
    				resultCompare = fornCompFirst.compareToIgnoreCase(fornCompSecond);
    				if(resultCompare <0){
    					arrayFornitoriOrd.add(listaSF);
    					elemLista--;
    					//NB: fornCompareFirst l'ho già rimosso
    					//dall'array finale da restituire
    				}else if(resultCompare >0){
    					listaSFVO.add(listaSF);
    					fornCompFirst = fornCompSecond;
    					listaSFVO.remove(appListaSF);
    				}
    				listaSF = appListaSF;
    			}
    		}
    	}
    	if(elemLista>0){
    		arrayFornitoriOrd.add(listaSF);
    	}
    	if((tipoOrdinamento.equals("CD")) || (tipoOrdinamento.equals("FD"))){
    		arrayFornitoriOrdD = new ArrayList();
    		elemLista = arrayFornitoriOrd.size();
    		int count = 0;
    		while(elemLista > 0){
    			appListaSF= (FornitoreVO)arrayFornitoriOrd.remove(elemLista-1);
    			arrayFornitoriOrdD.add(count, appListaSF);
    			elemLista--;
    			count++;
    		}
    		return arrayFornitoriOrdD;
    	}
    	return arrayFornitoriOrd;
    }

    public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
    }

}
