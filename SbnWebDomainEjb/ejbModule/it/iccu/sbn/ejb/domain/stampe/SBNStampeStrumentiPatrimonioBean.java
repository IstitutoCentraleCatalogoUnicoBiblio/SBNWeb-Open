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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.OrdineStampaOnlineVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStrumentiPatrimonioDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStrumentiPatrimonioVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_inventari;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.web2.util.Constants;

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
public class SBNStampeStrumentiPatrimonioBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 8535319226501509922L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private Inventario inventario;
	static Logger log = Logger.getLogger(SBNStampeStrumentiPatrimonioBean.class);

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
			this.inventario = DomainEJBFactory.getInstance().getInventario();
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
	public Object elabora(StampaVo stampaVO, BatchLogWriter blw)
	throws Exception {
		//almaviva5_20130412 evolutive google
		if (stampaVO instanceof StampaOnLineVO)
			return elaboraStampaOnline(stampaVO);

		getDownloadFileName().clear();
		Logger _logger = blw.getLogger();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
		List<StampaStrumentiPatrimonioVO> listaRegistroXls = new ArrayList<StampaStrumentiPatrimonioVO>();


		String fileJrxml = null;
		String tipoStampa = null;
		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		String tipoRegistro = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {
			fileJrxml = stampaVO.getTemplate();
			pathDownload = stampaVO.getDownload();
			tipoStampa = stampaVO.getTipoStampa();
			String ticket = stampaVO.getTicket();
			String textListEm="ATTENZIONE: registro posseduto ricercato inesistente, cambiare i parametri di ricerca";
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

			System.out.print(""+fileJrxml);

			StampaStrumentiPatrimonioVO registroXls = null;

			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampaVO).getUser();
			tipoRegistro = ((StampaDiffVO)stampaVO).getTipoRegistro();

			if (tipoRegistro != null && tipoRegistro.equals("possedutoXls")){
				_logger.info("path fileJrxml per registro possedutoXls: "+fileJrxml);
			}else if (tipoRegistro != null && tipoRegistro.equals("patrimonioXls")){
				_logger.info("path fileJrxml per registro patrimonioXls: "+fileJrxml);
			}
			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();
			StampaStrumentiPatrimonioVO stampaSPVO = null;
			stampaSPVO = (StampaStrumentiPatrimonioVO)parametri.get(0);
			stampaSPVO.setIdBatch(stampaVO.getIdBatch());
			try{
				registroXls = this.getInventario().getStrumentiPatrimonioXls(stampaSPVO, tipoRegistro, ticket, blw);
				if (registroXls == null || registroXls.getLista().size() == 0) {
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					if (tipoRegistro != null && tipoRegistro.equals("possedutoXls")){
						nameFile = utente +"_log_possedutoXls_"+idMess+ "." + "txt";
					}else if (tipoRegistro != null && tipoRegistro.equals("patrimonioXls")){
						nameFile = utente +"_log_patrimonioXls_"+idMess+ "." + "txt";
					}
					streamLogStampa = ("La richiesta non ha prodotto risultati").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
					elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
					return elaborazioniDifferiteOutputVo;
				}else{
					if (((StampaDiffVO)stampaVO).isModelloPrelievo()){
						Integer prgContatore = null;
						int numInventari = registroXls.getLista().size();
						String codPolo = ((StampaDiffVO)stampaVO).getCodPolo();
						String codBib = ((StampaDiffVO)stampaVO).getCodBib();
						//accedere alla tabella tbf_contatore con codPolo, codBib e cod_cont='MPR', prendere ultimo_prg
						//e aggiornarlo incrementandolo con il numInventari (size della listaInventari)
						//n.b. prgContatore è il numero progressivo letto non il calcolato
						//e viene passato a jasper come stampaSPVO.countInput, variabile numerica da incrementare;
						//questo dato calcolato è presente soltanto in stampa
						prgContatore = this.getInventario().getContatore(codPolo, codBib, Constants.PROGRESSIVO_MODULO_PRELIEVO,
								numInventari, ticket);
						if (prgContatore != null && prgContatore >= 0){
							stampaSPVO.setContInput(prgContatore);
							stampaSPVO.setContOutput(prgContatore+numInventari);
						} else{
						/* RG20130116 il contatore è opzionale. Se non esistente in DB viene ignorato e non
						 * riportato sulla stampa del modulo prelievo

							streamLogStampa = ("Il progressivo contatore è null o 0").getBytes("UTF8");
							this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
							listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
							elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
							return elaborazioniDifferiteOutputVo;
						*/
							stampaSPVO.setContInput(-1);
						}


					}
				}
			}catch(Exception exep){
				_logger.error("", exep);
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(exep);
				return elaborazioniDifferiteOutputVo;
			}
			try {
				if (ValidazioneDati.in(tipoRegistro, "possedutoXls", "patrimonioXls")) {
					StampaStrumentiPatrimonioVO recOut = stampaSPVO.copy(); //new StampaStrumentiPatrimonioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					recOut.setCodPolo(stampaSPVO.getCodPolo());
					recOut.setCodBib(stampaSPVO.getCodBib());//
					recOut.setDescrBib(stampaSPVO.getDescrBib());//
					recOut.setSerie(stampaSPVO.getSerie());//
					recOut.setStartInventario(stampaSPVO.getStartInventario());//
					recOut.setEndInventario(stampaSPVO.getEndInventario());//
					recOut.setDataDa(stampaSPVO.getDataDa());//
					recOut.setDataA(stampaSPVO.getDataA());//
					if(stampaSPVO.isNuoviEsemplari()){
						recOut.setEsemplariTitoli("Nuovi esemplari");
					}else if(stampaSPVO.isNuoviTitoli()){
						recOut.setEsemplariTitoli("Nuovi titoli");
					}

					recOut.setDataDiElaborazione(DateUtil.formattaData(DaoManager.now().getTime()));
					recOut.setCodAttivita(stampaSPVO.getCodAttivita());
					recOut.setUser(stampaSPVO.getUser());
					recOut.setCodPoloSez(stampaSPVO.getCodPoloSez());
					recOut.setCodBibSez(stampaSPVO.getCodBibSez());
					recOut.setSezione(stampaSPVO.getSezione());
					recOut.setDallaCollocazione(stampaSPVO.getDallaCollocazione());
					recOut.setDallaSpecificazione(stampaSPVO.getDallaSpecificazione());
					recOut.setAllaCollocazione(stampaSPVO.getAllaCollocazione());
					recOut.setAllaSpecificazione(stampaSPVO.getAllaSpecificazione());
					recOut.setDescrizioneSC(stampaSPVO.getDescrizioneSC());
					recOut.setDescrizioneND(stampaSPVO.getDescrizioneND());
					recOut.setDescrizioneTP(stampaSPVO.getDescrizioneTP());
					recOut.setDescrizioneDigitalizzazione(stampaSPVO.getDescrizioneDigitalizzazione());
					recOut.setDescrBib(registroXls.getDescrBib());
					if (((StampaDiffVO)stampaVO).isModelloPrelievo()){
						for (int i = 0; i < registroXls.getLista().size(); i++) {
							StampaStrumentiPatrimonioDettaglioVO rec = registroXls.getLista().get(i);
							rec.setMotivoPrelievo(((StampaDiffVO)stampaVO).getMotivoPrelievo());
							rec.setDataPrelievo(((StampaDiffVO)stampaVO).getDataPrelievo());
							rec.setContInput(stampaSPVO.getContInput());
						}
					}
					recOut.setLista(registroXls.getLista());
					String codBiblio = "";
					String codSerie = "";
					recOut.setNomeSubReport(stampaSPVO.getNomeSubReport());
					if (((StampaDiffVO)stampaVO).isModelloPrelievo()){
						recOut.setNomeSubReportMP(stampaSPVO.getNomeSubReportMP());
					}
					listaRegistroXls.add(recOut);

					boolean listaEsistente = false;
					if (tipoRegistro != null && (tipoRegistro.equals("possedutoXls")
							|| tipoRegistro.equals("patrimonioXls"))){
						//genero la stampa del modello del posseduto - google
						streamRichiestaStampa = effettuaStampa(
								stampaVO.getTemplate(),
								stampaVO.getTipoStampa(),
								listaRegistroXls);
						//metto la stampa generata nella lista dei file da scaricare
						if(streamRichiestaStampa !=null){
							try {
								if (tipoRegistro != null && tipoRegistro.equals("possedutoXls")){
									nameFile =  stampaVO.getFirmaBatch() +"_registro_posseduto_"+idMess+ "." + tipoStampa;
								}else{
									nameFile =  stampaVO.getFirmaBatch() +"_registro_patrimonio_"+idMess+ "." + tipoStampa;
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
							Map hm = new HashMap();
							String property;
							//sriivo é il nome del parametro risultante dalla chiamata del servizio
							if (tipoRegistro != null && (tipoRegistro.equals("possedutoXls")
									|| tipoRegistro.equals("patrimonioXls"))){
								property = stampaSPVO.getCodBib();
								if (property != null && property.length() > 0) {
									hm.put("Codice biblioteca", property);
								}

								property = stampaSPVO.getCodPolo();
								if (property != null && property.length() > 0) {
									hm.put("Polo", property);
								}
							}
							elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

						}else{
							streamRichiestaStampa = new ByteArrayInputStream(textListEm.getBytes());
						}
					}
				}

			}catch (Exception ef) {
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(ef);
				return elaborazioniDifferiteOutputVo;
			}

			if (((StampaDiffVO)stampaVO).isModelloPrelievo()){
				getDownloadFileName().clear();
				try{
					boolean listaEsistente = false;
					//genero la stampa
					StampaStrumentiPatrimonioVO ssp = listaRegistroXls.get(0);
					tipoStampa = "PDF";
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("start", stampaSPVO.getContInput());
					streamRichiestaStampa = effettuaStampa(
							stampaVO.getTemplate2(),
							"PDF",
							ssp.getLista(), params);//listaRegTopograficoXls);
					//metto la stampa generata nella lista dei file da scaricare
					if(streamRichiestaStampa !=null){
						try {
							nameFile =  stampaVO.getFirmaBatch() +"_modulo_prelievo_"+idMess+ "." + tipoStampa;
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
						property = stampaSPVO.getCodBib();
						if (property != null && property.length() > 0) {
							hm.put("Codice biblioteca", property);
						}

						property = stampaSPVO.getCodPolo();
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
			}else{
				return elaborazioniDifferiteOutputVo;
			}

		} catch (Exception ex) {
			// DEVO GESTIRE LE ECCEZIONI PERSONALIZZANDO L'ERRORE

			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			blw.logWriteException(ex);
			return elaborazioniDifferiteOutputVo;
		}
	}


	private Object elaboraStampaOnline(StampaVo stampaVO) throws Exception {

		OrdineStampaOnlineVO sol = (OrdineStampaOnlineVO) stampaVO;
		OrdiniVO ordine = (OrdiniVO) ValidazioneDati.first(sol.getDatiStampa());

		Tba_ordiniDao dao = new Tba_ordiniDao();

		List<StampaStrumentiPatrimonioDettaglioVO> output = new ArrayList<StampaStrumentiPatrimonioDettaglioVO>();
		List<Tra_ordine_inventari> inventari = dao.getListaInventariRilegatura(ordine.getIDOrd());

		Integer prgContatore = null;
		int numInventari = ValidazioneDati.size(inventari);
		String codPolo = ordine.getCodPolo();
		String codBib = ordine.getCodBibl();

		String dataPrelievo = sol.getDataPrelievo();
		String motivo = sol.getMotivoPrelievo();
		if (!ValidazioneDati.isFilled(motivo)) {
			TB_CODICI cod = CodiciProvider.cercaCodice(ordine.getCd_tipo_lav(),
					CodiciType.CODICE_NON_DISPONIBILITA,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			motivo = ValidazioneDati.isFilled(cod.getCd_flg3()) ? cod.getCd_flg3() : cod.getDs_tabella();
		}
		prgContatore = getInventario().getContatore(codPolo, codBib, Constants.PROGRESSIVO_MODULO_PRELIEVO, numInventari, sol.getTicket());
		int start = (prgContatore != null && prgContatore >= 0) ? prgContatore : -1;

		for (Tra_ordine_inventari oi : inventari) {
			StampaStrumentiPatrimonioDettaglioVO sspd =
				getInventario().elementoStrumentiPatrimonioDettaglio(sol.getTicket(), oi.getCd_polo(), "possedutoXls");

			if (ValidazioneDati.isFilled(dataPrelievo) )
				sspd.setDataPrelievo(dataPrelievo);
			else
				sspd.setDataPrelievo(DateUtil.formattaData(oi.getData_uscita()));

			sspd.setMotivoPrelievo(motivo);

			sspd.setContInput(start);

			output.add(sspd);
		}

		return output;
	}

	public OutputStampaVo stampaModuloPrelievo(StampaOnLineVO stampaVO) {
		OutputStampaVo output = new OutputStampaVo();
		InventarioVO inv = (InventarioVO) stampaVO.getDatiStampa().get(0);
		String motivo = ValidazioneDati.trimOrEmpty((String) stampaVO.getDatiStampa().get(1));

		Tbc_inventarioDao dao = new Tbc_inventarioDao();
		try {
			output.setStato(ConstantsJMS.STATO_ERROR);

			StampaStrumentiPatrimonioDettaglioVO sspd = getInventario().elementoStrumentiPatrimonioDettaglio(
					stampaVO.getTicket(),
					dao.getInventario(inv.getCodPolo(), inv.getCodBib(), inv.getCodSerie(), inv.getCodInvent()),
					"possedutoXls");

			sspd.setDataPrelievo(DateUtil.formattaData(DaoManager.now()));
			sspd.setMotivoPrelievo(motivo);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("start", -1);
			InputStream streamRichiestaStampa = effettuaStampa(stampaVO.getTemplate(), stampaVO.getTipoStampa(),
					ValidazioneDati.asSingletonList(sspd), params);

			output.setOutput(streamRichiestaStampa);

			output.setStato(ConstantsJMS.STATO_OK);

		} catch (Exception e) {
			log.error("", e);
		}

		return output;
	}

	/**
	 * This method implements the business logic for the EJB.
	 *
	 */
	public void onMessage(Message message)  {}

	public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
	}
}
