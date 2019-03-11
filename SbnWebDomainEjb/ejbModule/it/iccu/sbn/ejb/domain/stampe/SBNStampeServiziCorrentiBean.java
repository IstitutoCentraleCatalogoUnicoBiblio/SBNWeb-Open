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
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaServiziCorrentiVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaServiziVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.domain.CodiciAttivita;

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
 * @ejb.bean name="SBNserviziCorrenti"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="SBNserviziCorrenti"
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
public class SBNStampeServiziCorrentiBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 8535319226501509922L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Servizi servizi;
	static Logger log = Logger.getLogger(SBNStampeServiziCorrentiBean.class);

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
			this.servizi = DomainEJBFactory.getInstance().getServizi();
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

	public Servizi getServizi() {

		if (servizi != null)
			return servizi;

		try {
			this.servizi = DomainEJBFactory.getInstance().getServizi();

			return servizi;

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
	throws Exception {
		getDownloadFileName().clear();
		Logger _log = blw.getLogger();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
		List lista = new ArrayList();


		String fileJrxml = null;
		String tipoStampa = null;
		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		String codAtt = null;
		String descrCodAtt = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {
			fileJrxml = stampaVO.getTemplate();
			pathDownload = stampaVO.getDownload();
			tipoStampa = stampaVO.getTipoStampa();
			String ticket = stampaVO.getTicket();
			String textListEm="ATTENZIONE: serviziCorrenti ricercato inesistente, cambiare i parametri di ricerca";
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			_log.info("path fileJrxml per la stampa servizi: " + fileJrxml);

			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampaVO).getUser();
			codAtt = ((StampaDiffVO)stampaVO).getCodAttivita();
			descrCodAtt = ((StampaDiffVO)stampaVO).getTipoOperazione();
			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();
			StampaServiziVO sscVO = (StampaServiziVO)parametri.get(0);
			sscVO.setUser(utente);
			sscVO.setCodAttivita(codAtt);
			sscVO.setIdBatch(stampaVO.getIdBatch());

			try{
				List<StampaServiziCorrentiVO> listeTematiche = null;
				SubReportVO listaStorico = null;
				if (codAtt.equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI)) {
					listeTematiche = this.getServizi().getStampaListeTematiche(ticket, sscVO.getRichiesta(), sscVO.getRichiesta().isAttivitaAttuale());
					_log.debug("Richieste trovate: " + ValidazioneDati.size(listeTematiche) );
					sscVO.setLista(listeTematiche);
				}else if (codAtt.equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_STORICO)) {
					//almaviva5_20130515 #5312
					listaStorico = this.getServizi().getStampaListeTematicheStorico(ticket, sscVO);
					if (listaStorico != null) {
						sscVO.setSubReportStorico(listaStorico);
						sscVO.setLista(ValidazioneDati.asSingletonList(new StampaServiziCorrentiVO(stampaVO) ));
					}
				}
				if (sscVO == null || sscVO.getLista().size() == 0 ){
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					if (codAtt.equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI)){
						nameFile = utente +"_log_serviziCorrenti_"+idMess+ "." + "txt";
					}else{
						nameFile = utente +"_log_serviziStoricizzati_"+idMess+ "." + "txt";
					}
					streamLogStampa = ("La richiesta non ha prodotto risultati").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
					elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
					return elaborazioniDifferiteOutputVo;
				}

			}catch(Exception exep){
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(exep);
				return elaborazioniDifferiteOutputVo;
			}
			try{
				//mi serve poi per sapere se ho creato almeno un file
				boolean listaEsistente = false;
				Object listaConDatiRichiesta = null;
				if(sscVO.getLista().size()>0){
					if (codAtt.equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI)){
						//listaConDatiRichiesta = new ArrayList<Object>();
						for(int index = 0; index < sscVO.getLista().size(); index++){
							StampaServiziCorrentiVO sscd =(StampaServiziCorrentiVO) sscVO.getLista().get(index);
							if (sscVO.getRichiesta().getFlSvolg()!= null && sscVO.getRichiesta().getFlSvolg().equals("L")){
								sscd.setSvolgimento("Locale");
							}
							sscd.setTipoServizio(sscVO.getRichiesta().getCodTipoServ());
							sscd.setModalitaErog(sscVO.getRichiesta().getCodErogAlt());
							sscd.setAttivita(sscVO.getRichiesta().getCodAttivita());
							sscd.setStatoMov(sscVO.getRichiesta().getCodStatoMov());
							sscd.setStatoRich(sscVO.getRichiesta().getCodStatoRic());
							sscd.setInventario(sscVO.getRichiesta().getCodBibInv()+" "+ sscVO.getRichiesta().getCodSerieInv()+" "+ sscVO.getRichiesta().getCodInvenInv());
							sscd.setCodBibCollocazione(sscVO.getRichiesta().getCodBibDocLett()+" "+sscVO.getCodBibCollocazione());
							sscd.setCollocazione(sscVO.getCollocazione());
							sscd.setDataDa(sscVO.getDataDa());
							sscd.setDataA(sscVO.getDataA());
							sscd.setData(DateUtil.getDate()+DateUtil.getTime());
							//listaConDatiRichiesta.add(sscd);
						}
						listaConDatiRichiesta = sscVO.getLista();
					}else if (codAtt.equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_STORICO)){
//						listaConDatiRichiesta = new ArrayList<Object>();
//						for(int index = 0; index < sscVO.getLista().size(); index++){
//							StampaServiziStoricizzatiVO sscd =(StampaServiziStoricizzatiVO) sscVO.getLista().get(index);
//							sscd.setInventario(sscVO.getRichiesta().getCodBibInv()+" "+ sscVO.getRichiesta().getCodSerieInv()+" "+ sscVO.getRichiesta().getCodInvenInv());
//							sscd.setCodBibCollocazione(sscVO.getRichiesta().getCodBibDocLett()+" "+sscVO.getCodBibCollocazione());
//							sscd.setCollocazione(sscVO.getCollocazione());
//							sscd.setDataDa(sscVO.getDataDa());
//							sscd.setDataA(sscVO.getDataA());
//							sscd.setData(DateUtil.getDate()+DateUtil.getTime());
//							listaConDatiRichiesta.add(sscd);
//						}
						listaConDatiRichiesta = sscVO.getSubReportStorico();
					}
				}
				//genero la stampa
				streamRichiestaStampa = effettuaStampa(
						stampaVO.getTemplate(),
						stampaVO.getTipoStampa(),
						listaConDatiRichiesta);

				//metto la stampa generata nella lista dei file da scaricare
				if(streamRichiestaStampa !=null){
					try {
						if (codAtt.equals(CodiciAttivita.getIstance().STAMPA_SERVIZI_CORRENTI)){
							nameFile =  stampaVO.getFirmaBatch() +"_servizi_correnti." + tipoStampa;
						}else{
							nameFile =  stampaVO.getFirmaBatch() +"_servizi_storico." + tipoStampa;
						}
						this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);
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
					Map<String, String> hm = new HashMap<String, String>();
					String property;
					//sriivo é il nome del parametro risultante dalla chiamata del servizio
					property = sscVO.getCodBib();
					if (property != null && property.length() > 0) {
						hm.put("Codice biblioteca", property);
					}

					property = sscVO.getCodPolo();
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
			return elaborazioniDifferiteOutputVo;
		} catch (Exception ex) {
			// DEVO GESTIRE LE ECCEZIONI PERSONALIZZANDO L'ERRORE
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			blw.logWriteException(ex);
			return elaborazioniDifferiteOutputVo;
		}
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
