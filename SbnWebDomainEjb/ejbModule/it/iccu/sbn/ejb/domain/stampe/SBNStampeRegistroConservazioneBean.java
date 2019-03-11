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
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroConservazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroConservazioneVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
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
import javax.ejb.SessionContext;
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
public class SBNStampeRegistroConservazioneBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 8535319226501509922L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Inventario inventario;
	static Logger log = Logger.getLogger(SBNStampeRegistroConservazioneBean.class);
	private Codici codici;

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
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			this.inventario = factory.getInventario();
			codici = factory.getCodici();

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
	public Codici getCodici() {
		if (codici != null)
			return codici;

		try {
			this.codici = DomainEJBFactory.getInstance().getCodici();

			return codici;

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
		Logger logger = blw.getLogger();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idBatch = stampaVO.getIdBatch();
		String nameFile = "";

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
		List listaRegConservazione = new ArrayList();


		String fileJrxml = null;
		String tipoStampa = null;
		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {
			fileJrxml = stampaVO.getTemplate();
			pathDownload = stampaVO.getDownload();
			tipoStampa = stampaVO.getTipoStampa();
			String ticket = stampaVO.getTicket();
			String textListEm="ATTENZIONE: registro di conservazione ricercato inesistente, cambiare i parametri di ricerca";
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			logger.info("path fileJrxml per le stampe registro di conservazione"+fileJrxml);
			System.out.print(""+fileJrxml);

			StampaRegistroConservazioneVO registro = null;

			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampaVO).getUser();
			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();
			StampaRegistroConservazioneVO stampaRCVO = (StampaRegistroConservazioneVO)parametri.get(0);
			//almaviva5_20150115
			stampaRCVO.setIdBatch(idBatch);
			stampaRCVO.setTicket(ticket);

			try{
				registro = this.getInventario().getRegistroConservazione(stampaRCVO, ticket, blw);
				if (registro == null || registro.getLista().size() == 0 ){
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					nameFile = utente +"_log_registro_conservazione_"+idBatch+ "." + "txt";
					streamLogStampa = ("La richiesta non ha prodotto risultati").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
					elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
					return elaborazioniDifferiteOutputVo;
				}
				for(int k= 0; k<registro.getLista().size(); k++){
					//l'assegnazione che segue serve solo a generare una eccezione se c'è un problema
					StampaRegistroConservazioneVO dettaglioRegistro = registro.getLista().get(k);
				}
			}catch(Exception exep){
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(exep);
				return elaborazioniDifferiteOutputVo;
			}
			try{
				//mi serve poi per sapere se ho creato almeno un file
				String intestaz = "";



				StampaRegistroConservazioneDettaglioVO elemListaVO;
				StampaRegistroConservazioneVO recOut = new StampaRegistroConservazioneVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				recOut.setCodPolo(stampaRCVO.getCodPolo());
				recOut.setCodBib(stampaRCVO.getCodBib());//
				recOut.setDescrBib(registro.getDescrBib());//
				recOut.setSezione(registro.getSezione());//
				recOut.setCodAttivita(stampaRCVO.getCodAttivita());
				recOut.setUser(stampaRCVO.getUser());
				recOut.setDataDiElaborazione(DateUtil.formattaData(DaoManager.now().getTime()));
				if (stampaRCVO.getTipoOperazione() != null){
					if (stampaRCVO.getTipoOperazione().equals("R")){
						recOut.setSerie(stampaRCVO.getSerie());//
						recOut.setStartInventario(stampaRCVO.getStartInventario());//
						recOut.setEndInventario(stampaRCVO.getEndInventario());//
						recOut.setSezione(null);//
						recOut.setListaInventari(null);//
					}else if (stampaRCVO.getTipoOperazione().equals("S")){
						recOut.setSerie(null);//
						recOut.setSezione(stampaRCVO.getSezione());//
						recOut.setDallaCollocazione(stampaRCVO.getDallaCollocazione()+" "+stampaRCVO.getDallaSpecificazione());//
						recOut.setAllaCollocazione(stampaRCVO.getAllaCollocazione()+" "+stampaRCVO.getAllaSpecificazione());//);//
						recOut.setListaInventari(null);//
					}else if (stampaRCVO.getTipoOperazione().equals("N")){
						recOut.setSerie(null);
						recOut.setSezione(null);
						recOut.setListaInventari(stampaRCVO.getListaInventari());
					}
				}
				if (stampaRCVO.getStatoConservazione() != null && !stampaRCVO.getStatoConservazione().equals("")){
					recOut.setStatoConservazione(this.getCodici().cercaDescrizioneCodice(stampaRCVO.getStatoConservazione(),
							CodiciType.CODICE_STATI_DI_CONSERVAZIONE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					recOut.setStatoConservazione("");
				}

				if (stampaRCVO.getStatoConservazione() != null && !stampaRCVO.getTipoMateriale().equals("")){
					recOut.setTipoMateriale(this.getCodici().cercaDescrizioneCodice(stampaRCVO.getTipoMateriale(),
							CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
				}else{
					recOut.setTipoMateriale("");
				}
				if (stampaRCVO.getTipoOrdinamento() != null){
					if (stampaRCVO.getTipoOrdinamento().equals("I")){
						recOut.setTipoOrdinamento("serie, inventario");
					}else if (stampaRCVO.getTipoOrdinamento().equals("D")){
						recOut.setTipoOrdinamento("data di ingresso, serie, inventario");
					}else if (stampaRCVO.getTipoOrdinamento().equals("C")){
						recOut.setTipoOrdinamento("sezione, collocazione, specificazione");
					}
				}
				String codBiblio = "";
				String codSerie = "";
				for(int indice=0; indice < registro.getLista().size(); indice++){
					elemListaVO = registro.getLista().get(indice);
					recOut.getLista().add(elemListaVO);

				}
				recOut.setNomeSubReport(stampaRCVO.getNomeSubReport());
				listaRegConservazione.add(recOut);

				boolean listaEsistente = false;
				if(registro.getLista().size()>0){
					//genero la stampa
					streamRichiestaStampa = effettuaStampa(
							stampaVO.getTemplate(),
							stampaVO.getTipoStampa(),
							listaRegConservazione);
					//metto la stampa generata nella lista dei file da scaricare
					if(streamRichiestaStampa !=null){
						try {
							nameFile =  stampaVO.getFirmaBatch() +"_registro_conservazione_"+idBatch+ "." + tipoStampa;
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
						Map hm = new HashMap();
						String property;
						//sriivo é il nome del parametro risultante dalla chiamata del servizio
						property = stampaRCVO.getCodBib();
						if (property != null && property.length() > 0) {
							hm.put("Codice biblioteca", property);
						}

						property = stampaRCVO.getCodPolo();
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

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
	}

}
