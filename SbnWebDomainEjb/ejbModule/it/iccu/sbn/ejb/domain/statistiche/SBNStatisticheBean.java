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
package it.iccu.sbn.ejb.domain.statistiche;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBase;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.ejb.vo.statistiche.DettVarStatisticaVO;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;
import it.iccu.sbn.persistence.dao.statistiche.StatisticheDao;
import it.iccu.sbn.polo.orm.statistiche.Tb_stat_parameter;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.stampe.excel.ExcelPrintUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class SBNStatisticheBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {

	private static final long serialVersionUID = 8535319226501509922L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private StatisticheSBN statisticheSbn;
	static Logger log = Logger.getLogger(SBNStatisticheBean.class);

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
			this.statisticheSbn = DomainEJBFactory.getInstance().getStatistiche();
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

	public StatisticheSBN getStatisticheSBN() {
		if (statisticheSbn != null)
			return statisticheSbn;

		try {
			this.statisticheSbn = DomainEJBFactory.getInstance().getStatistiche();

			return statisticheSbn;

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
	public Object elabora(StampaVo stampaVO, BatchLogWriter log)
	throws Exception {
		getDownloadFileName().clear();
		Logger logger = log.getLogger();
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";

		//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
		List listaRegConservazione = new ArrayList();


		String tipoStampa = null;
		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(stampaVO);
		try {
			System.out.println("++============================================================================++");
			System.out.println("Statistiche Elaborazioni differite - Inizio di Statistiche.java metodo statistiche");
			System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
			System.out.println("++============================================================================++");

			//			ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo =null;

			String nomeFileLogIntero="";
			String suffisso="statistiche_";

			if (log!=null && log.getFilename()!=null && log.getFilename().getNomeFileVisualizzato()!=null && log.getFilename().getNomeFileVisualizzato().trim().length()>0)
			{
				nomeFileLogIntero=log.getFilename().getNomeFileVisualizzato();
				int pos=nomeFileLogIntero.lastIndexOf(".");
				String nomeFileLogParziale="";
				if (pos>0)
				{
					nomeFileLogParziale=nomeFileLogIntero.substring(0,pos);
				}
				if (!nomeFileLogParziale.equals(""))
				{
					suffisso=nomeFileLogParziale;
				}
			}

			// Crea lista dei file da scaricare
			//			List listaDownload= new ArrayList();

			// TODO impostare il file excel
			String filename = "";
			String ticket = stampaVO.getTicket();
			File crea = null;
			boolean stat = false;
			List parametri = (List) ((StampaDiffVO) stampaVO).getParametri();
			StatisticaVO statisticaVO = (StatisticaVO) parametri.get(0);
			statisticaVO.setIdBatch(stampaVO.getIdBatch());
			String basePath = stampaVO.getBasePath();
			ParametriExcelVO parExc = statisticaVO.getParExc();
			boolean flTxt = (ValidazioneDati.equals(statisticaVO.getFlTxt(), "S"));
			try{
				if (flTxt){
					//
					String[] nome_composto = statisticaVO.getParExc().getArea().trim().split("\\s+");
					String nomeConUnderscore="";
					for (int x=0; x< nome_composto.length; x++) {
						nomeConUnderscore=nomeConUnderscore+nome_composto[x]+"_";
					}
					statisticaVO.getParExc().setArea(nomeConUnderscore);
					//
					filename =  statisticaVO.getParExc().getArea() + statisticaVO.getSeqOrdinamento().toString()+"_"+ statisticaVO.getNomeStatisticaPerLog()+statisticaVO.getIdBatch()+ ".csv";
					statisticaVO.setFileName(filename);
					statisticaVO.setDownloadLinkPath((downloadLinkPath));
					statisticaVO.setDownloadPath(stampaVO.getDownloadPath());
				}
				stat = this.getStatisticheSBN().elaboraStatistica(statisticaVO, ticket, log);
				if (stat){
						try {
							if (flTxt){
								listaDownload.add(new DownloadVO(filename, stampaVO.getDownloadLinkPath()+filename));
								elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
							}else{
								if (statisticaVO.getParExc().getQuery()!=null && statisticaVO.getParExc().getQuery().trim().length()>0 ){
									String nomeXls = "__"+ statisticaVO.getNomeStatisticaPerLog() + statisticaVO.getIdBatch() + ".xls";
									//crea = File.createTempFile(nomeXls, ".xls");
									crea = new File(FileUtil.getTempFilesDir() + File.separator + nomeXls);
									if (crea.canRead())	{
										if (crea.getParentFile().isDirectory())	{
											File[] files = crea.getParentFile().listFiles(); // array dei nomi dei file della direcrtory temporanea che vanno cancelllati se soddisfano le condizioni
											for (File f : files){
												if (f.getName().toLowerCase().endsWith("xls") &&  f.getName().toLowerCase().contains("__") && !f.getName().equals(crea.getName())){
													f.delete();
												}
											}
										}
										crea.deleteOnExit();
									}

									StringBuilder hf = new StringBuilder();
									List<DettVarStatisticaVO> listaVar = new ArrayList<DettVarStatisticaVO>();
									StatisticheDao statDAO = new StatisticheDao();
									if (statisticaVO.getElencoVariabili() != null){
										for (int i = 0; i < statisticaVO.getElencoVariabili().size(); i++) {
											DettVarStatisticaVO v = null;
											DettVarStatisticaVO var = statisticaVO.getElencoVariabili().get(i);
											Tb_stat_parameter variabile = statDAO.getVariabile(Integer.valueOf(var.getIdStatistica()), var.getNomeVar());
											if ((variabile.getId_stat().getId_stat())== Integer.valueOf(var.getIdStatistica())){
												v = new DettVarStatisticaVO();
												v.setTipoVar(var.getTipoVar());
												v.setNomeVar(var.getNomeVar());
												v.setEtichettaNomeVar(var.getEtichettaNomeVar());
												v.setValore(var.getValore());
											}
											listaVar.add(v);
										}
										hf.append(parExc.getQuery());
										if (listaVar.size() > 0) {
											for (int i = 0; i < listaVar.size(); i++) {
												hf.append("\n"+listaVar.get(i).getEtichettaNomeVar()+" : "+ listaVar.get(i).getValore());
											}
										}
									} else
										hf.append(parExc.getQuery());

									parExc.setQuery(hf.toString());

									ExcelPrintUtil.createExcelFile(parExc, statisticaVO.getIntestazioni(), statisticaVO.getRisultati(), crea);
								}
						}
						}catch(Exception exep){
							elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
							nameFile = utente +"_log_statistiche_"+idMess+ "." + "txt";
							streamLogStampa = ("Errore nell'elaborazione della richiesta della statistica, il recupero dei dati restituisce "+exep.getMessage()).getBytes("UTF8");//in byte array//(ef.getMessage().toString()).getBytes();
							this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
							listaDownload.add(new DownloadVO(nomeFileErr, downloadLinkPath+nameFile));
							elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
							return elaborazioniDifferiteOutputVo;
						}
				}
				if (!flTxt){
					filename = crea.getName(); //+".xls";
				}
				// solo in presenza di errori
				if (statisticaVO.getErrori() !=null && statisticaVO.getErrori().size()>0)	{
					filename =suffisso+".htm";
					FileOutputStream streamOut = new FileOutputStream(crea.getParentFile()+File.separator+ filename);
					if (statisticaVO != null){
						for (int i = 0; i < statisticaVO.getErrori().size(); i++) {
							String stringaMsg = statisticaVO.getErrori().get(i);
							stringaMsg = stringaMsg + "<br>";
							streamOut.write(stringaMsg.getBytes());
						}
					}
					streamOut.write("FINE Statistiche".getBytes());
					streamOut.close();
				}
			}catch(Exception exep){
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				nameFile = utente +"_log_statistiche_"+idMess+ "." + "txt";
				streamLogStampa = ("Errore nell'elaborazione della richiesta della statistica, il recupero dei dati restituisce "+exep.getMessage()).getBytes("UTF8");//in byte array//(ef.getMessage().toString()).getBytes();
				this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
				listaDownload.add(new DownloadVO(nomeFileErr, downloadLinkPath+nameFile));
				elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
				return elaborazioniDifferiteOutputVo;
			}

			//			String fileJrxml=statisticaVO.getBasePath()+ File.separator +"jrxml" ;
			if (flTxt){
			}else{

				String fileJrxml=basePath+ File.separator +"jrxml" ;
				String nomeFile=crea.getName();
				File dest=new File(stampaVO.getDownloadPath()+ File.separator + crea.getName());
				try {
					this.copy(crea,dest);
					crea.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listaDownload.add(new DownloadVO(filename,stampaVO.getDownloadPath()+ File.separator + crea.getName()));

				elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
			}

			Map hm = new HashMap();
			hm.put("campo", "valore");
			elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			System.out.println("++============================================================================++");
			System.out.println("Statistiche Elaborazioni differite - Fine di Statistiche.java metodo statistiche");
			System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
			System.out.println("++============================================================================++");
			return elaborazioniDifferiteOutputVo;
		} catch (Exception ex) {
			// DEVO GESTIRE LE ECCEZIONI PERSONALIZZANDO L'ERRORE
			logger.error(ex.getMessage());
			elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
			nameFile = utente +"_log_statistiche_"+idMess+ "." + "txt";
			streamLogStampa = ("Errore nell'elaborazione della richiesta della statistica, il recupero dei dati restituisce "+ex.getMessage()).getBytes("UTF8");//in byte array//(ef.getMessage().toString()).getBytes();

			this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
			listaDownload.add(new DownloadVO(nomeFileErr, downloadLinkPath+nameFile));
			elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);
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

	public static void copy(File src, File dest) throws IOException {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);

            byte[] buffer = new byte[8 * 1024];

            while (true) {
                int bytesRead = in.read(buffer);
                if (bytesRead <= 0) {
                    break;
                }
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //failsafe
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //failsafe
                }
            }
        }
    }

}
