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
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroIngressoLogVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStatisticheRegistroDettaglioAcqVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStatisticheRegistroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
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
 * @ejb.bean name="SBNRegistriIngresso"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="SBNRegistriIngresso"
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
public class SBNStampeRegistriIngressoBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 8535319226501509922L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Inventario inventario;
	static Logger log = Logger.getLogger(SBNStampeRegistriIngressoBean.class);

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
		this.setupJMS();
		return;
	}

	public Object elabora(StampaVo stampaVO, BatchLogWriter blw)
	throws Exception {
		Logger logger = blw.getLogger();
		getDownloadFileName().clear();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		int indiceChar= stampaVO.getIdBatch().indexOf(":");
		String idMess= stampaVO.getIdBatch().substring(indiceChar+1, stampaVO.getIdBatch().length());
		String nameFile = "";
		Timestamp ts = null;

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area

		//qui metterò i dati da stampare (per le due stampe in questo caso)
		List listaRegIngresso = new ArrayList();
		List listaLogRegIngresso = new ArrayList();
		List listaStatisticheRegIngresso = new ArrayList();

		String fileJrxml = null;
		String tipoStampa = null;
		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		InputStream streamRichiestaStampa;
		InputStream streamLogStampa;

		String tipoRicerca= stampaVO.getTipoOrdinamento();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {
			fileJrxml = stampaVO.getTemplate();
			pathDownload = stampaVO.getDownload();
			tipoStampa = stampaVO.getTipoStampa();
			String polo = stampaVO.getCodPolo();
			String bibl = stampaVO.getCodBib();
			String ticket = stampaVO.getTicket();
			String textListEm="ATTENZIONE: registro di ingresso ricercato inesistente, cambiare i parametri di ricerca.";
			// Stampa differita: occorre fare una distinzione tra registro di ingresso e statistiche di registro
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			logger.info("path fileJrxml per le stampe registro di ingresso: " + fileJrxml);
			System.out.print(""+fileJrxml);

			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			String modalita = ((StampaDiffVO)stampaVO).getModalita();
			utente = ((StampaDiffVO)stampaVO).getUser();
			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();
			StampaStatisticheRegistroVO statistiche = null;



			if(tipoRicerca.equals("REGISTRO")){
				StampaRegistroVO stampaRegistro = new StampaRegistroVO(stampaVO);
				stampaRegistro = (StampaRegistroVO)parametri.get(0);
				stampaRegistro.setIdBatch(stampaVO.getIdBatch());

				StampaRegistroVO listaOutput = this.getInventario().getRegistroIngresso(stampaRegistro, ticket, blw);

				if(listaOutput.getSizeInv() == 0){
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					nameFile = utente +"_log_registro_ingresso_"+idMess+ "." + "txt";
					streamLogStampa = new ByteArrayInputStream(("La richiesta non ha prodotto risultati").getBytes("UTF8"));
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					elaborazioniDifferiteOutputVo.addDownload(nameFile, downloadLinkPath+nameFile);

					return elaborazioniDifferiteOutputVo;
				}
				//ho controllato tutto l'array list che devo stampare e ora passo a stamparlo
				StampaRegistroVO recRegistro = null;
				StampaRegistroIngressoLogVO recLog = null;
				StampaRegistroVO recDettaglio = null;
				String codBiblio = "";
				String codSerie = "";
				try {
					if (listaOutput != null && listaOutput.getSizeInv() > 0){

						//					String intestaz = "";//Biblioteca: codeBiblioteca	Serie: serieInv	da inv: codeInvDa	a inv.: codeInvA	Data Ingresso da: dataDa	a: dataA
						//					for(int indice=0; indice < listaOutput.getSizeInv(); indice++){
						boolean ignoraInvAssente = false;
						boolean ignoraInvEsatto = false;
						recRegistro = new StampaRegistroVO(stampaVO);
						//il seguito sostituisce intestazione
//						recRegistro.setCodeBiblioteca(listaOutput.getCodBib());
//						recRegistro.setDescrBib(listaOutput.getDescrBib());
//						recRegistro.setSerieInv(listaOutput.getSerieInv());
//						recRegistro.setCodeInvDa(listaOutput.getCodeInvDa());
//						recRegistro.setCodeInvA(listaOutput.getCodeInvA());
//						recRegistro.setDataDa(listaOutput.getDataDa());
//						recRegistro.setDataA(listaOutput.getDataA());
//						recRegistro.setData(DateUtil.formattaData(DaoManager.now()));
						recRegistro.setRecInventario(listaOutput.getRecInventario());
						recRegistro.setRecLog(listaOutput.getRecLog());
						listaLogRegIngresso.add(recRegistro);
						streamRichiestaStampa = effettuaStampa(fileJrxml, tipoStampa, recRegistro.getRecInventario(), tipoRicerca);

						boolean listaEsistente = false;
						if(streamRichiestaStampa !=null){
							//						String jrxmlLog=fileJrxml.replace("default_reg_ingresso1.jrxml","default_reg_ingressolog1.jrxml").replace("default_reg_ingresso2.jrxml","default_reg_ingressolog2.jrxml");
							String jrxmlLog=fileJrxml.replace("default_reg_ingresso2.jrxml","default_reg_ingressolog2.jrxml");
							streamLogStampa = effettuaStampa(jrxmlLog, tipoStampa, recRegistro.getRecLog(), tipoRicerca);
							try {
								nameFile =  stampaVO.getFirmaBatch() +"_registro_ingresso_"+idMess+ "." + tipoStampa;
								this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);

								if(streamLogStampa !=null){

									nameFile =  stampaVO.getFirmaBatch() +"_log_registro_ingresso_"+idMess+ "." + tipoStampa;
									this.scriviFile(utente, tipoStampa, streamLogStampa, pathDownload, nameFile);
								}
							}catch (Exception ef) {
								throw new Exception("Creazione del file"+ nameFile+"fallita");
							}
							String filename = "";

							for(int index = 0; index < getDownloadFileName().size(); index++){
								listaEsistente = true;
								filename = (getDownloadFileName().get(index));
								elaborazioniDifferiteOutputVo.addDownload(filename, downloadLinkPath+filename);
							}
							if(!listaEsistente){
								throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
							}

							elaborazioniDifferiteOutputVo.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());
							// Preparare hash map dei parametri
							Map hm = new HashMap();
							String property;
							//sriivo é il nome del parametro risultante dalla chiamata del servizio
							property = listaOutput.getCodeBiblioteca();
							if (property != null && property.length() > 0) {
								hm.put("Codice biblioteca", property);
							}

							property = listaOutput.getCodPolo();
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
			}else if(tipoRicerca.equals("STATISTICHE")){

				StampaRegistroVO stampaStatisticheVO = new StampaRegistroVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				stampaStatisticheVO = (StampaRegistroVO)parametri.get(0);
				stampaStatisticheVO.setIdBatch(stampaVO.getIdBatch());

				try{
					statistiche = this.getInventario().getStatisticheRegistroIngresso(stampaStatisticheVO, ticket);

					if(statistiche == null || statistiche.getListaTipoAcq().size() == 0 ){
						elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
						nameFile = utente +"_log_statistiche_registro_"+idMess+ "." + "txt";
						streamLogStampa = new ByteArrayInputStream(("La richiesta non ha prodotto risultati").getBytes("UTF8"));
						this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
						elaborazioniDifferiteOutputVo.addDownload(nameFile, downloadLinkPath+nameFile);

						return elaborazioniDifferiteOutputVo;
					}
					for(int k= 0; k<statistiche.getListaTipoAcq().size(); k++){
						//l'assegnazione che segue serve solo a generare una eccezione se c'è un problema
						StampaRegistroVO dettaglioStatistiche = statistiche.getListaTipoAcq().get(k);
					}
				}catch(Exception exep){
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
					blw.logWriteException(exep);

					return elaborazioniDifferiteOutputVo;
				}


				try {
					String intestaz = "";



					StampaStatisticheRegistroDettaglioAcqVO elemListaStampaStatisticheVO;
					StampaRegistroVO rec = (StampaRegistroVO)parametri.get(0);//parametri della richiesta
					StampaStatisticheRegistroVO recOut = new StampaStatisticheRegistroVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					recOut.setCodeBiblioteca(stampaStatisticheVO.getCodeBiblioteca());//
					recOut.setDescrBib(stampaStatisticheVO.getDescrBib());//
					recOut.setSerieInv(stampaStatisticheVO.getSerieInv());//
					recOut.setCodeInvDa(stampaStatisticheVO.getCodeInvDa());//
					recOut.setCodeInvA(stampaStatisticheVO.getCodeInvA());//
					recOut.setDataDa(stampaStatisticheVO.getDataDa());//
					recOut.setDataA(stampaStatisticheVO.getDataA());//
					recOut.setData(DateUtil.formattaData(DaoManager.now().getTime()));
					recOut.setCodPolo(stampaStatisticheVO.getCodPolo());
					recOut.setCodBib(stampaStatisticheVO.getCodBib());
					recOut.setCodAttivita(stampaStatisticheVO.getCodAttivita());
					recOut.setUser(stampaStatisticheVO.getUser());
					String codBiblio = "";
					String codSerie = "";
					for(int indice=0; indice < statistiche.getListaTipoAcq().size(); indice++){
						elemListaStampaStatisticheVO = statistiche.getListaTipoAcq().get(indice);
						//						codBiblio = rec.getCodeBiblioteca();
						//						codSerie = rec.getSerieInv();
						//						intestaz = "Biblioteca: "+codBiblio+"	Serie: "+codSerie+"	da inv: "+rec.getCodeInvDa();
						//						intestaz = intestaz+"	a inv.: " + rec.getCodeInvA();
						//						intestaz = intestaz+"	Data Ingresso da: "+rec.getDataDa();
						//						intestaz = intestaz +"	a:   " + rec.getDataA();
						//						if (rec.getCodeTipoOrdine() != null && rec.getCodeTipoOrdine().equals("")){
						//							intestaz = intestaz +"	codice modalità acquisizione non impostato";
						//						}else{
						//							intestaz = intestaz +"	codice modalità acquisizione :  " + rec.getCodeTipoOrdine();
						//						}
						recOut.getListaTipoAcq().add(elemListaStampaStatisticheVO);

					}
					listaStatisticheRegIngresso.add(recOut);

					streamRichiestaStampa = effettuaStampa(fileJrxml, tipoStampa, listaStatisticheRegIngresso, tipoRicerca);

					boolean listaEsistente = false;
					if(streamRichiestaStampa !=null){
						String jrxmlLog=fileJrxml.replace("default_statistiche_reg_ingresso.jrxml", "default_statistiche_reg_ingresso.jrxml");
						try {
							nameFile =  stampaVO.getFirmaBatch() +"_statistiche_registro_"+idMess+ "." + tipoStampa;
							this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);
						}catch (Exception ef) {
							throw new Exception("Creazione del file"+ nameFile+"fallita");
						}
						String filename = "";

						for(int index = 0; index < getDownloadFileName().size(); index++){
							listaEsistente = true;
							filename = (getDownloadFileName().get(index));
							elaborazioniDifferiteOutputVo.addDownload(filename, downloadLinkPath+filename);
						}
						if(!listaEsistente){
							throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
						}

						elaborazioniDifferiteOutputVo.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());
						// Preparare hash map dei parametri
						Map hm = new HashMap();
						String property;
						//sriivo é il nome del parametro risultante dalla chiamata del servizio
						property = stampaStatisticheVO.getCodeBiblioteca();
						if (property != null && property.length() > 0) {
							hm.put("Codice biblioteca", property);
						}

						property = stampaStatisticheVO.getCodPolo();
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
			}
			return elaborazioniDifferiteOutputVo;
		} catch (Exception ex) {
			// DEVO GESTIRE LE ECCEZIONI PERSONALIZZANDO L'ERRORE
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			blw.logWriteException(ex);

			return elaborazioniDifferiteOutputVo;
		}
	}

//	private String impostaValoreCheck(String checkOut, boolean data){
//		//argomento preso dai parametri dioutput
//		String valCheck = "1";
//		String notValCheck = "0";
//		if(data){
//			valCheck = "0";
//			notValCheck = "1";
//		}
//		if((checkOut != null) && (!(checkOut.trim()).equals(("")))){	//Se il valore che arriva é 1(nel caso dei check)o diverso da null o non stringa vuota(nel caso viene passato un dato) allora l'elemento non esiste allora lascio 1 che indica presenza di un errore
//			return valCheck;
//		}else{					//altrimenti il valore che arriva é 0(per i check)o una stringa (per gli elementi che non son check),  allora l'elemento  esiste e metto 0 che indica che l'errore non c'é
//			return notValCheck;
//		}
//
//	}

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

	private InputStream effettuaStampa(String fileJrxml, String tipoStampa, Object dati, String tipoRicerca) throws Exception{
		InputStream streamRichiestaStampa = null;
		if(tipoRicerca.equals("REGISTRO")){
			if(dati != null ){
				SbnStampe sbn = new SbnStampe(fileJrxml);
				sbn.setFormato(TipoStampa.toTipoStampa(tipoStampa));
				Map parametri=new HashMap();
				streamRichiestaStampa = sbn.stampa(dati, parametri);
			}
		}else if(tipoRicerca.equals("STATISTICHE")){
			if(((List)dati).size() > 0){
				SbnStampe sbn = new SbnStampe(fileJrxml);
				sbn.setFormato(TipoStampa.toTipoStampa(tipoStampa));
				Map parametri=new HashMap();
				//poiché il Design per Registro Statistiche richiede dei subreport passo
				//a jasper il parametro jasperDir (path completo della directory
				//dove si trovano i jasper files, senza lo slash finale)
				String jasperDir=fileJrxml.replace("jrxml", "jasper");
				int i1=jasperDir.lastIndexOf(File.separator);
				if(i1>0){
					jasperDir=jasperDir.substring(0, i1);
				}
				parametri.put(JASPER_DIR, TMP_PATH + File.separator);
				streamRichiestaStampa = sbn.stampa(dati, parametri);
			}
		}
		return streamRichiestaStampa;
	}

	/**
	 * This method implements the business logic for the EJB.
	 *
	 */
	public void onMessage(Message message)  {}

	public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
	}

	public Inventario getInventario() {
		if (inventario != null)
			return inventario;

		try {
			this.inventario = DomainEJBFactory.getInstance().getInventario();

			return inventario;

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
