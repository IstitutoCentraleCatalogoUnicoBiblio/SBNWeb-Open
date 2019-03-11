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
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.documentofisico.SpostamentoCollocazioniVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaEtichetteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.gestionestampe.etichette.FormattazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.etichette.FormattazioneVOCampoEtichetta;
import it.iccu.sbn.ejb.vo.gestionestampe.etichette.FormattazioneVOImmagini;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.documentofisico.Tbf_modelli_stampeDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.SbnStampe;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.apache.log4j.Logger;


/**
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="SBNEtichette"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="SBNEtichette"
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
/**
 * @author Administrator
 *
 */
public class SBNStampeEtichetteBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 1354104643416107737L;


	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	protected MessageDrivenContext ctx = null;
	private Inventario inventario;
	private static Logger log = Logger.getLogger(SBNStampeEtichetteBean.class);

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
		log.debug("TextMDB.ejbRemove, this=" + hashCode());
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
		return;
	}

	public Inventario getInventario() {
		if (inventario != null)
			return inventario;

		try {
			this.inventario = DomainEJBFactory.getInstance().getInventario();

			return inventario;

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
	@SuppressWarnings("unchecked")
	public Object elabora(StampaVo stampaVO, BatchLogWriter blw)
	throws Exception {
		getDownloadFileName().clear();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		int indiceChar= stampaVO.getIdBatch().indexOf(":");
		String idMess= stampaVO.getIdBatch().substring(indiceChar+1, stampaVO.getIdBatch().length());
		String nameFile = "";

		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		String fileJrxml = null;
		String fileJrxmlBarCode = null;
		InputStream streamRichiestaStampa = null;
		byte[] streamLogStampa;
		fileJrxml = stampaVO.getTemplate();
		fileJrxmlBarCode = stampaVO.getTemplateBarCode();
		pathDownload = stampaVO.getDownload();
		String ticket = stampaVO.getTicket();
		String textListEm="ATTENZIONE: etichette ricercato inesistente, cambiare i parametri di ricerca";
		String template = null;
		String templateBarCode = null;

		if (stampaVO instanceof StampaOnLineVO)
			return stampaOnline(stampaVO, textListEm, template, templateBarCode);

		if (blw != null) {
			Logger logger = blw.getLogger();
			logger.info("path fileJrxml per le stampe etichette: "+fileJrxml);
			logger.info("path fileJrxmlBarCode per le stampe etichette: "+fileJrxmlBarCode);
		}
		// Stampa differita
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);

		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

		utente = ((StampaDiffVO)stampaVO).getFirmaBatch();
		List parametri = (List)((StampaDiffVO)stampaVO).getParametri();

		Object op = parametri.get(0);//rp aggiunto per gestire un altro tipo di VO in input oltre a StampaEtichetteVO
		List listaOutput = null;
		if (op instanceof StampaEtichetteVO) {

			StampaEtichetteVO stampaEtichette = (StampaEtichetteVO)parametri.get(0);
			String tipoOperazione = stampaEtichette.getTipoOperazione();

			//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
			listaOutput = new ArrayList<EtichettaDettaglioVO>();
			try{
				listaOutput = this.getInventario().getEtichette(stampaEtichette, tipoOperazione, ticket);
				if (!ValidazioneDati.isFilled(listaOutput) ) {
					//throw new DataException ("Errore nel recupero dei dati, l'output é nullo");
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					nameFile = utente +"_log_etichette_"+idMess+ "." + "txt";
					streamLogStampa = ("La richiesta non ha prodotto risultati per inventari collocati. Controllare il corrispondente log").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					elaborazioniDifferiteOutputVo.addDownload(nameFile, downloadLinkPath+nameFile);

					return elaborazioniDifferiteOutputVo;
				}

			} catch (Exception e) {
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(e);

				return elaborazioniDifferiteOutputVo;
			}
		}else if (op instanceof SpostamentoCollocazioniVO){
			SpostamentoCollocazioniVO stampaEtichette = (SpostamentoCollocazioniVO)parametri.get(0);
			//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
			listaOutput = new ArrayList();
			try{
				listaOutput = stampaEtichette.getListaEtichette();
				if(listaOutput == null || listaOutput.size() == 0){
					//						throw new DataException ("Errore nel recupero dei dati, l'output é nullo");
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
					nameFile = utente +"_log_etichette_"+idMess+ "." + "txt";
					streamLogStampa = ("La richiesta non ha prodotto risultati per inventari collocati. Controllare il corrispondente log").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					elaborazioniDifferiteOutputVo.addDownload(nameFile, downloadLinkPath+nameFile);

					return elaborazioniDifferiteOutputVo;
				}
				for(int k= 0; k<listaOutput.size(); k++){
					//l'assegnazione che segue serve solo a generare una eccezione se c'è un problema
					EtichettaDettaglioVO dettaglioEtichetta = (EtichettaDettaglioVO)listaOutput.get(k);
				}
			} catch(Exception exep) {
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(exep);

				return elaborazioniDifferiteOutputVo;
			}
		}

		//ho controllato tutto l'array list che devo stampare e ora passo a stamparlo
		try {
			if (op instanceof StampaEtichetteVO){
				StampaEtichetteVO stampaEtichette = (StampaEtichetteVO)parametri.get(0);
			}else if (op instanceof SpostamentoCollocazioniVO){
				SpostamentoCollocazioniVO stampaEtichette = (SpostamentoCollocazioniVO)parametri.get(0);
			}
			List listaConEtichetteMoltiplicate = null;
			//
			if (((StampaDiffVO)stampaVO).getTemplate() != null && !((StampaDiffVO)stampaVO).getTemplate().trim().equals("")){
				template = ((StampaDiffVO)stampaVO).getTemplate();
			}
			if (((StampaDiffVO)stampaVO).getTemplateBarCode() != null && !((StampaDiffVO)stampaVO).getTemplateBarCode().trim().equals("")){
				templateBarCode = ((StampaDiffVO)stampaVO).getTemplateBarCode();
			}
			if (template != null && !template.equals("") ){
				getDownloadFileName().clear();
				JasperDesign jasperDesign = null;
				//importo il modello nel VO etic
				FormattazioneVO etic = this.readFormattazione(stampaVO.getCodPolo(), stampaVO.getCodBib(), stampaVO.getTemplate());
				//prepararo l'arraylist listaEtichette
				//in questa fase gestisco l'eventuale "concatena" richiesto dell'utente
				//
				//mi creo al volo il Design
				jasperDesign=this.creaModello(etic);
				List le = this.preparaDatiPerJasper(listaOutput, etic);
				//
				try{
					//mi creo al volo il Design
					String tipoStampa=stampaVO.getTipoStampa();
					boolean listaEsistente = false;
					List<EtichettaDettaglioVO> lista = null;
					if (ValidazioneDati.isFilled(le) && le.get(0) instanceof EtichettaDettaglioVO){
					if(listaOutput.size()>0){
						lista = preparaDatiEtichettaPerTemplate(
								stampaVO, le, template, templateBarCode);
							//vado a stampare
							streamRichiestaStampa = effettuaStampa(
									jasperDesign,
									tipoStampa,
									lista);

							//metto la stampa generata nella lista dei file da scaricare
							listaEsistente = false;
							if(streamRichiestaStampa !=null){
								try {
									nameFile = utente +"_etichette_"+idMess+ "." + tipoStampa;
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
							}
						}
					}

				}catch (Exception ef) {
					elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
					blw.logWriteException(ef);

					return elaborazioniDifferiteOutputVo;
				}
			}
			if (templateBarCode != null && !templateBarCode.equals("") ){
				getDownloadFileName().clear();
				try{
					//mi creo al volo il Design
					String tipoStampa=stampaVO.getTipoStampa();
					boolean listaEsistente = false;
					if(listaOutput.size()>0){
						List<EtichettaDettaglioVO> lista = preparaDatiEtichettaPerTemplateBarCode(
								stampaVO, listaOutput, template, templateBarCode);
						//genero la stampa

						streamRichiestaStampa = effettuaStampa(
								stampaVO.getTemplateBarCode(),
								/*stampaVO.getTipoStampa()*/"PDF",
								lista);
						//metto la stampa generata nella lista dei file da scaricare
						if(streamRichiestaStampa !=null){
							try {
								nameFile =  stampaVO.getFirmaBatch() +"_etichette_barcode_"+idMess+ "." + /*tipoStampa*/"PDF";
								this.scriviFile(utente,/*tipoStampa*/"PDF", streamRichiestaStampa, pathDownload, nameFile);
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
//								property = stampaEtichette.getCodBib();
							property = stampaVO.getCodBib();
							if (property != null && property.length() > 0) {
								hm.put("Codice biblioteca", property);
							}

//								property = stampaEtichette.getCodPolo();
							property = stampaVO.getCodPolo();
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
			}


		}catch (Exception ef) {
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			blw.logWriteException(ef);

			return elaborazioniDifferiteOutputVo;
		}
		return elaborazioniDifferiteOutputVo;

	}

	private Object stampaOnline(StampaVo stampaVO, String textListEm,
			String template, String templateBarCode)
			throws DaoManagerException, JRException, Exception,
			ValidationException {
		InputStream streamRichiestaStampa;
		OutputStampaVo output = new OutputStampaVo();
		StampaOnLineVO input = (StampaOnLineVO)stampaVO;
		output.setStato(ConstantsJMS.STATO_OK);
		//prendo la singola etichetta da stampare
		List<EtichettaDettaglioVO> listaOutput = new ArrayList<EtichettaDettaglioVO>();
		int etichetteVuoteIniziali=0;
		List<EtichettaDettaglioVO> datiStampa = (List<EtichettaDettaglioVO>) input.getDatiStampa().get(0);
		if (datiStampa != null && datiStampa.size() > 0){
			for (int idx = 0 ; idx < datiStampa.size(); idx++){
				EtichettaDettaglioVO etichetta = datiStampa.get(idx);
				//metto i dati da stampare in un List (gli oggetti dell'arraylist devono essere di tipo EtichettaDettaglioVO)

				//in questa fase inserisco prima tante etichette vuote quante me ne sono state richieste
				try{
					etichetteVuoteIniziali=new Integer(input.getEtichetteVuoteIniziali()).intValue();
				} catch(NumberFormatException e) {
					etichetteVuoteIniziali=0;
				}
				EtichettaDettaglioVO etichettaVuota=new EtichettaDettaglioVO();
				for(int i=0;i<etichetteVuoteIniziali;i++){
					listaOutput.add(etichettaVuota);
				}

				//accodo l'etichetta da stampare
				listaOutput.add(etichetta);
			}
		}
		FormattazioneVO etic = null;
		if (((StampaOnLineVO)stampaVO).getTemplate() != null && !((StampaOnLineVO)stampaVO).getTemplate().trim().equals("")
				&& !((StampaOnLineVO)stampaVO).getTemplate().equals(".jrxml")){
			template = stampaVO.getTemplate();
			etic = this.readFormattazione(stampaVO.getCodPolo(), stampaVO.getCodBib(), template);
			//mi creo al volo il Design
			JasperDesign jasperDesign=this.creaModello(etic);

			//prepararo l'arraylist listaEtichette
			//in questa fase gestisco l'eventuale "concatena" richiesto dell'utente
			List listaEtichette1 = null;
			//almaviva5_20090708
			//List listaEtichette = new ArrayList();
			List listaEtichette = this.preparaDatiPerJasper(listaOutput, etic);
			if (Integer.valueOf(input.getNumCopie()) > 1 ){
				for(int i = 1; i < Integer.valueOf(input.getNumCopie()); i++){
					for(int j = 0; j < listaOutput.size(); j++){
						EtichettaDettaglioVO elementoLetto=listaOutput.get(j);
						List appoListaOutput = new ArrayList();
						appoListaOutput.add(elementoLetto);
						listaEtichette1=this.preparaDatiPerJasper(appoListaOutput, etic);
						if (listaEtichette1.size() > 0){
							EtichettaDettaglioVO etichetta1 = (EtichettaDettaglioVO)listaEtichette1.get(etichetteVuoteIniziali);
							listaEtichette.add(etichetta1);
						}
					}
				}
			}

			if (template != null && !template.equals("") && !template.equals(".jrxml")){
				//stampo usando il design generato al volo
				streamRichiestaStampa = effettuaStampa(
						jasperDesign,
						stampaVO.getTipoStampa(),
						listaEtichette);
				//se tutto è Ok ritorno come stream la stampa generata
				if (streamRichiestaStampa != null) {
					output.setOutput(streamRichiestaStampa);
				} else {
					output.setStato(ConstantsJMS.STATO_ERROR);
				}
				return output;
			}
		}

		if (((StampaOnLineVO)stampaVO).getTemplateBarCode() != null && !((StampaOnLineVO)stampaVO).getTemplateBarCode().trim().equals("")){
			templateBarCode = stampaVO.getTemplateBarCode();
//				etic = this.readFormattazione(stampaVO.getCodPolo(), stampaVO.getCodBib(), templateBarCode);
		}

		if (ValidazioneDati.isFilled(templateBarCode) ) {
			//mi creo al volo il Design
			//String tipoStampa=stampaVO.getTipoStampa();
			if (ValidazioneDati.isFilled(listaOutput) ) {
				List<EtichettaDettaglioVO> lista = preparaDatiEtichettaPerTemplateBarCode(
						stampaVO, listaOutput, template, templateBarCode);
				//genero la stampa
				streamRichiestaStampa = effettuaStampa(
						stampaVO.getTemplateBarCode(),
						/*stampaVO.getTipoStampa()*/"PDF",
						lista);
				//metto la stampa generata nella lista dei file da scaricare
				if(streamRichiestaStampa !=null){
					output.setOutput(streamRichiestaStampa);
				}else{
					streamRichiestaStampa = new ByteArrayInputStream(textListEm.getBytes() );
				}
			}
			return output;
		}
		return output;
	}

	/**
	 * @param stampaVO
	 * @param listaOutput
	 * @return
	 * @throws ValidationException
	 */
	private List<EtichettaDettaglioVO> preparaDatiEtichettaPerTemplate(
			StampaVo stampaVO, List listaOutput, String template, String templateBarCode)
			throws ValidationException {
		List listaConEtichetteMoltiplicate;
		List<EtichettaDettaglioVO> listaEti = new ArrayList<EtichettaDettaglioVO>();
		for (int i = 0; i < listaOutput.size(); i++){
			EtichettaDettaglioVO eti = (EtichettaDettaglioVO)listaOutput.get(i);
			EtichettaDettaglioVO etiInv = new EtichettaDettaglioVO();
			if (template != null){
				etiInv = eti;
			}
			listaEti.add(etiInv);
		}
		//
		List<EtichettaDettaglioVO> lista = null;
			StampaDiffVO input = ((StampaDiffVO)stampaVO);
			int numCopie = input.getNumCopie();
			listaConEtichetteMoltiplicate = new ArrayList<EtichettaDettaglioVO>();
			lista = calcolaEtichettePerNumCopie(
					listaConEtichetteMoltiplicate, lista,
					listaEti, numCopie);
		return lista;
	}

	/**
	 * @param stampaVO
	 * @param listaOutput
	 * @return
	 * @throws ValidationException
	 */
	private List<EtichettaDettaglioVO> preparaDatiEtichettaPerTemplateBarCode(
			StampaVo stampaVO, List listaOutput, String template, String templateBarCode)
			throws ValidationException {

		List<EtichettaDettaglioVO> listaEti = new ArrayList<EtichettaDettaglioVO>();
		for (int i = 0; i < listaOutput.size(); i++) {
			EtichettaDettaglioVO eti = (EtichettaDettaglioVO)listaOutput.get(i);
			EtichettaDettaglioVO etiInv = new EtichettaDettaglioVO();
			if (templateBarCode != null) {

				etiInv.setSerie(ValidazioneDati.fillRight(eti.getSerie(), ' ', 3) );
				etiInv.setInventario(ValidazioneDati.fillLeft(eti.getInventario(), '0', 9) );
				//almaviva5_20140108 #5644
				String kinv = ConversioneHibernateVO.toWeb().chiaveInventario(
						stampaVO.getCodBib(), eti.getSerie(),
						Integer.valueOf(eti.getInventario()));
				//setto su biblioteca perchè sui campi serie ed inventario viene fatto il compare
				etiInv.setBiblioteca(kinv);
			}
			listaEti.add(etiInv);
		}
		//
		List<EtichettaDettaglioVO> lista = null;
		int numCopie = 0;
		if (stampaVO instanceof StampaOnLineVO) {
			StampaOnLineVO input = ((StampaOnLineVO)stampaVO);
			numCopie = input.getNumCopie();
		}else{
			StampaDiffVO input = ((StampaDiffVO)stampaVO);
			numCopie = input.getNumCopie();
		}
		List<EtichettaDettaglioVO> listaConEtichetteMoltiplicate = new ArrayList<EtichettaDettaglioVO>();
		lista = calcolaEtichettePerNumCopie(
				listaConEtichetteMoltiplicate, lista,
				listaEti, numCopie);
		return lista;
	}

	/**
	 * @param listaConEtichetteMoltiplicate
	 * @param listaOut
	 * @param lista
	 * @param listaEti
	 * @param numCopie
	 * @return
	 * @throws ValidationException
	 */
	private List<EtichettaDettaglioVO> calcolaEtichettePerNumCopie(
			List<EtichettaDettaglioVO> listaConEtichetteMoltiplicate,
			List<EtichettaDettaglioVO> lista,
			List<EtichettaDettaglioVO> listaEti, int numCopie)
			throws ValidationException {
		if (numCopie < 1)
			throw new ValidationException("controllareNumeroCopie");

		if (numCopie > 1) {

			for (int indez = 0; indez < listaEti.size(); indez++) {
				EtichettaDettaglioVO rec = listaEti.get(indez);
				listaConEtichetteMoltiplicate.add(rec);
				for (int i = 1; i < numCopie; i++)
					listaConEtichetteMoltiplicate.add(rec);
			}

			lista = listaConEtichetteMoltiplicate;
		}
		else
			lista = listaEti;

		return lista;
	}

	/**
	 * copio selettivamente i campi di listaInput secondo le regole
	 * di concatenamento decise dall'utente (contenute in etic)
	 *
	 * @param listaInput
	 * @param etic
	 * @return
	 */
	private List preparaDatiPerJasper(List listaInput, FormattazioneVO etic) {
		//creo l'arraylist da restituire
		List dati=new ArrayList();
		for(int indice=0; indice < listaInput.size(); indice++){
			EtichettaDettaglioVO elementoLetto=(EtichettaDettaglioVO)listaInput.get(indice);
			EtichettaDettaglioVO elementoScritto=new EtichettaDettaglioVO();
			String bufferTesto=""; //inizializzo il contenitore del testo
			for(int iCampo=etic.getCampiEtichetta().size()-1;iCampo>-1;iCampo--){
				//scorro l'elenco dei campi all'indietro
				FormattazioneVOCampoEtichetta campo = etic.getCampiEtichetta().get(iCampo);
				if(campo.isPresente() || campo.isConcatena()){

					if(!this.getCampoText(elementoLetto, campo.getNomeCampo()).equals("")){
						if(bufferTesto.equals("")){
							bufferTesto=this.getCampoText(elementoLetto, campo.getNomeCampo());
						} else {
							bufferTesto=this.getCampoText(elementoLetto, campo.getNomeCampo())+" "+bufferTesto;
						}
					}
					if (campo.isConcatena()){
						if (campo.getNomeCampo() != null && campo.getNomeCampo().equalsIgnoreCase("sequenza")){
							if (!bufferTesto.equals("")){
								if (!bufferTesto.trim().startsWith("/")){
									bufferTesto = " / " + bufferTesto;
								}
							}
						}
					}

					if(!campo.isConcatena()){
						//il campo va presentato (contiene già il concatenamento)
						this.setCampoText(elementoScritto, campo.getNomeCampo(), bufferTesto);
						//riazzero il buffer per i campi che precedono
						bufferTesto="";
					}
				}
			}


			//aggiungo l'elemento preparato in output
			dati.add(elementoScritto);
		}
		return dati;
	}

	/**
	 * mette nel campo opportuno di elementoScritto la stringa bufferTesto
	 * nomeCampo è il nome del campo in cui inserire la stringa
	 *
	 * @param elementoScritto
	 * @param nomeCampo
	 * @param bufferTesto
	 */
	private void setCampoText(EtichettaDettaglioVO elementoScritto, String nomeCampo, String bufferTesto) {
		if(nomeCampo.equals("biblioteca")) elementoScritto.setBiblioteca(bufferTesto);
		if(nomeCampo.equals("sezione")) elementoScritto.setSezione(bufferTesto);
		if(nomeCampo.equals("collocazione")) elementoScritto.setCollocazione(bufferTesto);
		if(nomeCampo.equals("specificazione")) elementoScritto.setSpecificazione(bufferTesto);
		if(nomeCampo.equals("sequenza")) elementoScritto.setSequenza(bufferTesto);
		if(nomeCampo.equals("serie")) elementoScritto.setSerie(bufferTesto);
		if(nomeCampo.equals("inventario")) elementoScritto.setInventario(bufferTesto);
	}

	/**
	 * ritorna il campo che si chiama nomeCampo dall'elemento elementoLetto
	 *
	 * @param elementoLetto
	 * @param nomeCampo
	 * @return
	 */
	private String getCampoText(EtichettaDettaglioVO elementoLetto, String nomeCampo) {
		String campo=null;
		if(nomeCampo.equals("biblioteca")) campo=elementoLetto.getBiblioteca();
		if(nomeCampo.equals("sezione")) campo=elementoLetto.getSezione();
		if(nomeCampo.equals("collocazione")) campo=elementoLetto.getCollocazione();
		if(nomeCampo.equals("specificazione")) campo=elementoLetto.getSpecificazione();
		if(nomeCampo.equals("sequenza")) campo=elementoLetto.getSequenza();
		if(nomeCampo.equals("serie")) campo=elementoLetto.getSerie();
		if(nomeCampo.equals("inventario")) campo=elementoLetto.getInventario();
		if(campo==null) return "";
		return campo.trim();
	}

	/**
	 * questo metodo legge dal db (accedendo con codPolo, codBib e codModello)
	 * un modello di stampa per le etichette e lo carica un una struttura
	 * FormattazioneVO
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codModello
	 * @return FormattazioneVO
	 * @throws DaoManagerException
	 * @throws JRException
	 */
	private FormattazioneVO readFormattazione(String codPolo, String codBib, String codModello) throws DaoManagerException, JRException {
		//leggo dal db il modello richiesto
		Tbf_modelli_stampeDao daoModello = new Tbf_modelli_stampeDao();
		String codModelloOk=codModello.replace(".jrxml", "");
		Tbf_modelli_stampe recModello = daoModello.getModello(codPolo, codBib, codModelloOk);
		String modello=recModello.getCampi_valori_del_form();

		//importo l' "stringone" letto dal DB in un oggetto FormattazioneVO
		FormattazioneVO etic=new FormattazioneVO();
		etic.importaModello(modello);

		//lo restituisco
		return etic;
	}

	/**
	 * Questo metodo genera al volo un Design Jasper sulla base di un oggetto
	 * FormattazioneVO etic (etichetta) già caricato
	 *
	 * @param etic la specificazione di come va stampata l'etichetta
	 * @return design generato
	 * @throws DaoManagerException
	 * @throws JRException
	 */
	private JasperDesign creaModello(FormattazioneVO etic) throws DaoManagerException, JRException {
		//genero "on the fly" il Design jasper
		JasperDesign jasperDesign = new JasperDesign();
		jasperDesign.setName("etichette");

		//conversione unità di misura scelta
		float fattoreDiConversione=StampeUtil.fattoreDiConversione(etic.getUnitaDiMisura());
		//-----------------
		int larghezzaPagina=StampeUtil.converti(etic.getLarghezzaPagina(), fattoreDiConversione);
		int altezzaPagina=StampeUtil.converti(etic.getAltezzaPagina(), fattoreDiConversione);
		int margineSup=StampeUtil.converti(etic.getMargineSup(), fattoreDiConversione);
		int margineSin=StampeUtil.converti(etic.getMargineSin(), fattoreDiConversione);
		int margineDes=StampeUtil.converti(etic.getMargineDes(), fattoreDiConversione);
		int margineInf=StampeUtil.converti(etic.getMargineInf(), fattoreDiConversione);
		//-----------------
		int altezzaEtichetta=StampeUtil.converti(etic.getAltezzaEtichetta(), fattoreDiConversione);
		int larghezzaEtichetta=StampeUtil.converti(etic.getLarghezzaEtichetta(), fattoreDiConversione);
		int margineSupEtichetta=StampeUtil.converti(etic.getMargineSupEtichetta(), fattoreDiConversione);
		int margineSinEtichetta=StampeUtil.converti(etic.getMargineSinEtichetta(), fattoreDiConversione);
		int margineDesEtichetta=StampeUtil.converti(etic.getMargineDesEtichetta(), fattoreDiConversione);
		int margineInfEtichetta=StampeUtil.converti(etic.getMargineInfEtichetta(), fattoreDiConversione);
		int spaziaturaEtichetteX=StampeUtil.converti(etic.getSpaziaturaEtichetteX(), fattoreDiConversione);
		int spaziaturaEtichetteY=StampeUtil.converti(etic.getSpaziaturaEtichetteY(), fattoreDiConversione);
		//-----------------
		//campi calcolati necessari
		int larghezzaUtilePagina=larghezzaPagina-margineSin-margineDes;
		int etichettePerRiga = larghezzaUtilePagina / (larghezzaEtichetta+spaziaturaEtichetteX);

		//Definiamo tutti i Fields
		FormattazioneVOCampoEtichetta currentElement;
		FormattazioneVOImmagini currentImage;
		JRDesignField field=null;
		Iterator it=etic.getCampiEtichetta().iterator();
		while (it.hasNext()) {
			currentElement=(FormattazioneVOCampoEtichetta)it.next();
			if(currentElement.isPresente() && !currentElement.isConcatena()){
				//vanno inseriti i soli campi presenti e senza concatena acceso
				field = new JRDesignField();
				field.setName(currentElement.getNomeCampo());
				field.setValueClass(java.lang.String.class);
				jasperDesign.addField(field);
			}
		}

		//creo lo stile Arial_Normal e lo imposto come defalut
		JRDesignStyle normalStyle = new JRDesignStyle();
		normalStyle.setName("Arial_Normal");
		normalStyle.setDefault(true);
		//almaviva5_20150523
		String font = StampeUtil.mapFont("Arial");
		normalStyle.setFontName(font);
		normalStyle.setFontSize(10);
		normalStyle.setPdfFontName("Helvetica");
		normalStyle.setPdfEncoding("Cp1252");
		normalStyle.setPdfEmbedded(false);
		jasperDesign.addStyle(normalStyle);

		//dimensioni pagina
		jasperDesign.setPageHeight(altezzaPagina);
		jasperDesign.setPageWidth(larghezzaPagina);
		jasperDesign.setColumnWidth(larghezzaEtichetta+spaziaturaEtichetteX);
		jasperDesign.setColumnCount(etichettePerRiga);

		//margini pagina
		jasperDesign.setTopMargin(margineSup);
		jasperDesign.setBottomMargin(margineInf);
		jasperDesign.setLeftMargin(margineSin);
		jasperDesign.setRightMargin(margineDes);

		//la direzione con cui si procede nella stampa (per righe o per colonne)
		jasperDesign.setPrintOrder(PrintOrderEnum.HORIZONTAL);

		//mi creo una banda vuota per usare per gli elementi assenti
		JRDesignBand emptyBand = new JRDesignBand();

		//Title
		jasperDesign.setTitle(emptyBand);

		//PageHeader
		jasperDesign.setPageHeader(emptyBand);

		//Column header
		jasperDesign.setColumnHeader(emptyBand);

		//Detail
		JRDesignBand detailBand = new JRDesignBand();
		detailBand.setHeight(altezzaEtichetta+spaziaturaEtichetteY);

		JRDesignTextField textField;
		JRDesignImage image;
		JRDesignExpression expression;

		//bordo etichetta
		if(etic.isBordiEtichetta()){
			JRDesignRectangle rectangle = new JRDesignRectangle();
			rectangle.setX(spaziaturaEtichetteX/2);
			rectangle.setY(spaziaturaEtichetteY/2);
			rectangle.setWidth(larghezzaEtichetta); //larghezza senza margini
			rectangle.setHeight(altezzaEtichetta); //altezza senza margini
			rectangle.setRadius(5); //arrotondamento angoli
			detailBand.addElement(rectangle);
		}

		//reinizializzazione iterator
		it=null;
		currentElement=null;
		it=etic.getCampiEtichetta().iterator();

		//ciclo che riempie l'etichetta con i campi selezionati
		//ogni iterazione corrisponde ad un campo
		while (it.hasNext()) {
			currentElement=(FormattazioneVOCampoEtichetta)it.next();
			if(currentElement.isPresente() && !currentElement.isConcatena()){
				//vanno gestiti i soli campi presenti e senza concatena acceso
				textField= new JRDesignTextField();
				int fieldX=StampeUtil.converti(currentElement.getX(), fattoreDiConversione);
				textField.setX(fieldX+margineSinEtichetta+spaziaturaEtichetteX/2);
				int fieldY=StampeUtil.converti(currentElement.getY(), fattoreDiConversione);
				textField.setY(fieldY+margineSupEtichetta+spaziaturaEtichetteY/2);

				// testo verticale
				if (currentElement.isVerticale()) {
					textField.setRotation(RotationEnum.RIGHT);
					textField.setWidth(altezzaEtichetta-margineSupEtichetta-fieldY-margineInfEtichetta);//fino al margine inferiore
				} else {
					// testo orizzontale
					textField.setWidth(larghezzaEtichetta-margineSinEtichetta-fieldX-margineDesEtichetta);//fino al margine dx
				}
				textField.setHeight(new Integer(currentElement.getPunti()).intValue()+3); //punti carattere +3 (??)

				//se imposto direttamente nome e dimensione font, non serve definire uno stile
				font = currentElement.getFont();
				textField.setFontSize(new Integer(currentElement.getPunti()).intValue());
				textField.setPdfFontName(
						StampeUtil.ttf2pdfFontName(
								font,
								currentElement.isGrassetto(),
								currentElement.isCorsivo())
				);
				//testo a capo
				textField.setStretchWithOverflow(true);
				//almaviva5_20150523
				font = StampeUtil.mapFont(font);
				textField.setFontName(font);

				//matching field name (serve a popolare i campi con i dati)
				expression = new JRDesignExpression();
				//expression.setValueClass(java.lang.String.class);
				String nomeCampo="$F{"+currentElement.getNomeCampo()+"}";
				expression.setText("( "+nomeCampo+"==null ? \" \" : "+nomeCampo+" )");
				textField.setExpression(expression);

				//posizione (allineamento)
				if(currentElement.isVerticale()){
					if(currentElement.getPosizione().equals("01")){
						//sinistra
						textField.setVerticalAlignment(VerticalAlignEnum.TOP);
					} else if(currentElement.getPosizione().equals("02")){
						//destra
						textField.setVerticalAlignment(VerticalAlignEnum.BOTTOM);
					} else if(currentElement.getPosizione().equals("03")){
						//centrato
						textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
					} else if(currentElement.getPosizione().equals("04")){
						//giustificato
						textField.setVerticalAlignment(VerticalAlignEnum.JUSTIFIED);
					}
				} else {
					if(currentElement.getPosizione().equals("01")){
						//sinistra
						textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
					} else if(currentElement.getPosizione().equals("02")){
						//destra
						textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
					} else if(currentElement.getPosizione().equals("03")){
						//centrato
						textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
					} else if(currentElement.getPosizione().equals("04")){
						//giustificato
						textField.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
					}
				}

				//finito
				detailBand.addElement(textField);
			}
		}

		//gestisco ora tutte le immagini
		it = etic.getCampiImmagine().iterator();
		while (it.hasNext()) {
			currentImage=(FormattazioneVOImmagini)it.next();
			if (currentImage.isPresente()) {
				image= new JRDesignImage(null);

				int fieldX=StampeUtil.converti(currentElement.getX(), fattoreDiConversione);
				image.setX(fieldX);
				int fieldY=StampeUtil.converti(currentElement.getY(), fattoreDiConversione);
				image.setY(fieldY);

				//se imposto direttamente nome e dimensione font, non serve definire uno stile
				JRDesignExpression express = new JRDesignExpression();
				express.setValueClass(java.lang.String.class);

				image.setExpression(express);
				detailBand.addElement(image);
			}

		}

		//detailBand.setSplitAllowed(true);
		detailBand.setSplitType(SplitTypeEnum.IMMEDIATE);

		((JRDesignSection)jasperDesign.getDetailSection()).addBand(detailBand);
		//jasperDesign.setDetail(detailBand);

		//Column footer
		jasperDesign.setColumnFooter(emptyBand);

		//Page footer
		jasperDesign.setPageFooter(emptyBand);

		//Summary
		jasperDesign.setSummary(emptyBand);

		return jasperDesign;
	}

	private InputStream effettuaStampa(JasperDesign jasperDesign, String tipoStampa, List listaRicIngresso) throws Exception{
		InputStream streamRichiestaStampa = null;
		if(listaRicIngresso.size() > 0){
			SbnStampe sbn = new SbnStampe(jasperDesign);
			sbn.setFormato(TipoStampa.toTipoStampa(tipoStampa));
			Map parametri=new HashMap();
			streamRichiestaStampa = sbn.stampa(listaRicIngresso, parametri);
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

}
