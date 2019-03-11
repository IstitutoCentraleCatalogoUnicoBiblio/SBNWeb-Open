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
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTitoliEditoreVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
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
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


/**
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="SBNRegistroTopografico"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="SBNRegistroTopografico"
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
public class SBNStampeTitoliEditoreBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 8535319226501509922L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Interrogazione interrogazione;
	static Logger log = Logger.getLogger(SBNStampeTitoliEditoreBean.class);

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

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (RemoteException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}

	public Interrogazione getInterrogazione() {
		if (interrogazione != null)
			return interrogazione;

		try {
			this.interrogazione = DomainEJBFactory.getInstance().getInterrogazione();

			return interrogazione;

		} catch (NamingException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (RemoteException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
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
		Logger _logger = blw.getLogger();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
		List listaRegistroXls = new ArrayList();


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
			String textListEm="ATTENZIONE: titoli editori ricercato inesistente, cambiare i parametri di ricerca";
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			System.out.print(""+fileJrxml);

			StampaTitoliEditoreVO registroXls = null;

			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampaVO).getUser();

			_logger.info("path fileJrxml per titoli editori Xls: "+fileJrxml);

			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();
			StampaTitoliEditoreVO stampaTEVO = null;
			stampaTEVO = (StampaTitoliEditoreVO)parametri.get(0);
			stampaTEVO.setIdBatch(stampaVO.getIdBatch());
			try{
				registroXls = this.getInterrogazione().getTitoliEditoreXls(stampaTEVO, ticket, blw);
				if (registroXls == null || registroXls.getLista().size() == 0) {
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					nameFile = utente +"_log_titoli_editoreXls_"+idMess+ "." + "txt";
					streamLogStampa = ("La richiesta non ha prodotto risultati").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					elaborazioniDifferiteOutputVo.addDownload(nameFile, downloadLinkPath + nameFile);

					return elaborazioniDifferiteOutputVo;
				}
			}catch(Exception exep){

				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(exep);

				return elaborazioniDifferiteOutputVo;
			}
			try{
				//mi serve poi per sapere se ho creato almeno un file
				registroXls.setCodPolo(stampaTEVO.getCodPolo());
				registroXls.setCodBib(stampaTEVO.getCodBib());
				registroXls.setCodBib(stampaTEVO.getCodBib());
				registroXls.setDescrEditore(stampaTEVO.getDescrEditore());
				registroXls.setIsbn(stampaTEVO.getIsbn());
				if (stampaTEVO.getRegione() != null && !stampaTEVO.getRegione().equals("")){
					registroXls.setRegione(stampaTEVO.getRegione() + " " +CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getRegione().toString(),
							CodiciType.CODICE_REGIONE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setRegione(stampaTEVO.getRegione());
				}

				if (stampaTEVO.getPaese() != null && !stampaTEVO.getPaese().equals("")){
					registroXls.setPaese(stampaTEVO.getPaese() + " " +CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getPaese().toString(),
							CodiciType.CODICE_PAESE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setPaese(stampaTEVO.getPaese());
				}


				if (stampaTEVO.getProvincia() != null && !stampaTEVO.getProvincia().equals("")){
					registroXls.setProvincia(stampaTEVO.getProvincia() + " " +CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getProvincia().toString(),
							CodiciType.CODICE_PROVINCE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setProvincia(stampaTEVO.getProvincia());
				}
				registroXls.setDataPubbl1Da(stampaTEVO.getDataPubbl1Da());
				registroXls.setDataPubbl1A(stampaTEVO.getDataPubbl1A());
				if (stampaTEVO.getTipoRecord() != null && !stampaTEVO.getTipoRecord().equals("")){
				registroXls.setTipoRecord(stampaTEVO.getTipoRecord() + " " +CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getTipoRecord().toString(),
						CodiciType.CODICE_GENERE_MATERIALE_UNIMARC,
						CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setTipoRecord("");
				}
				if (stampaTEVO.getLingua() != null && !stampaTEVO.getLingua().equals("")){
					registroXls.setLingua(stampaTEVO.getLingua() + " " +CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getLingua().toString(),
							CodiciType.CODICE_LINGUA,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setLingua(stampaTEVO.getLingua());
				}
				if (stampaTEVO.getNatura() != null && !stampaTEVO.getNatura().equals("")){
					registroXls.setNatura(stampaTEVO.getNatura() + " " +CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getNatura().toString(),
							CodiciType.CODICE_NATURA_BIBLIOGRAFICA,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setNatura(stampaTEVO.getNatura());
				}
				registroXls.setDataIngressoDa(stampaTEVO.getDataIngressoDa());
				registroXls.setDataIngressoA(stampaTEVO.getDataIngressoA());
				if (stampaTEVO.getTipoAcq() != null && !stampaTEVO.getTipoAcq().equals("")){
					registroXls.setTipoAcq(stampaTEVO.getTipoAcq() + " "
							+ CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getTipoAcq().toString(),
							CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setTipoAcq(stampaTEVO.getTipoAcq());
				}

				// Maggio 2013 - modifiche per filtrare la stampa titolixEditore anche per TipoMaterialeInventariabile
				// e qinserire nella parte di intestazione della stampa la sua valorizzazione
				if (stampaTEVO.getCodiceTipoMateriale() != null && !stampaTEVO.getCodiceTipoMateriale().equals("")){
					registroXls.setCodiceTipoMateriale(stampaTEVO.getCodiceTipoMateriale() + " "
							+ CodiciProvider.cercaDescrizioneCodice(stampaTEVO.getCodiceTipoMateriale().toString(),
							CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					registroXls.setCodiceTipoMateriale(stampaTEVO.getCodiceTipoMateriale());
				}

				registroXls.setSistema(stampaTEVO.getSistema());
				registroXls.setSimbolo(stampaTEVO.getSimbolo());
				registroXls.setCheckTipoPosseduto(stampaTEVO.getCheckTipoPosseduto());
				registroXls.setDataDiElaborazione(DateUtil.formattaDataOra(DaoManager.now()));
				registroXls.setCodPolo(stampaTEVO.getCodPolo());
				registroXls.setCodBib(stampaTEVO.getCodBib());//

				registroXls.setDataDiElaborazione(DateUtil.formattaData(DaoManager.now().getTime()));
				registroXls.setCodAttivita(stampaTEVO.getCodAttivita());
				registroXls.setUser(stampaTEVO.getUser());
				String codBiblio = "";
				String codSerie = "";
				registroXls.setNomeSubReport(stampaTEVO.getNomeSubReport());
				listaRegistroXls.add(registroXls);

				boolean listaEsistente = false;
				//genero la stampa
				streamRichiestaStampa = effettuaStampa(
						stampaVO.getTemplate(),
						stampaVO.getTipoStampa(),
						listaRegistroXls);
				//metto la stampa generata nella lista dei file da scaricare
				if(streamRichiestaStampa !=null){
					try {
						nameFile =  stampaVO.getFirmaBatch() +"_titoli_editore_"+idMess+ "." + tipoStampa;
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
					Map hm = new HashMap();
					String property;
					//sriivo é il nome del parametro risultante dalla chiamata del servizio
					property = stampaTEVO.getCodBib();
					if (property != null && property.length() > 0) {
						hm.put("Codice biblioteca", property);
					}

					property = stampaTEVO.getCodPolo();
					if (property != null && property.length() > 0) {
						hm.put("Polo", property);
					}
					elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

				}else{
					streamRichiestaStampa = new ByteArrayInputStream(textListEm.getBytes());
				}
			}catch (Exception ef) {

				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(ef);

				return elaborazioniDifferiteOutputVo;
			}
		}catch (Exception ef) {

			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			blw.logWriteException(ef);

			return elaborazioniDifferiteOutputVo;
		}
		return elaborazioniDifferiteOutputVo;
		}


	private String impostaValoreCheck(String checkOut, boolean data){
		//argomento preso dai parametri dioutput
		String valCheck = "1";
		String notValCheck = "0";
		if(data){
			valCheck = "0";
			notValCheck = "1";
		}
		if((checkOut != null) && (!(checkOut.trim()).equals(("")))){	//Se il valore che arriva é 1(nel caso dei check)o diverso da null o non stringa vuota(nel caso viene passato un dato) allora l'elemento non esiste allora lascio 1 che indica presenza di un errore
			return valCheck;
		}else{					//altrimenti il valore che arriva é 0(per i check)o una stringa (per gli elementi che non son check),  allora l'elemento  esiste e metto 0 che indica che l'errore non c'é
			return notValCheck;
		}

	}

	private String impostaCheck(String valueOut, String checkIn){////argomento preso dai parametri dioutput
		String valCheck = "0";//1
		if((valueOut != null) && !((valueOut.trim()).equals(""))){
			valCheck = "1";//0
		}else if((checkIn != null) && (checkIn.equals("1"))){
			valCheck = valueCheck("   ");
		}
		return valCheck;
	}

	private String valueCheck(String check){//argomento preso dai parametri di input
		String valore = "0";
		if(check.equals("on")){
			valore = "1";
		}else if(check.equals("1")){
			valore = "";
		}
		return valore;
	}

	/**
	 * This method implements the business logic for the EJB.
	 *
	 */
	public void onMessage(Message message)  {}

	public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
	}
}
