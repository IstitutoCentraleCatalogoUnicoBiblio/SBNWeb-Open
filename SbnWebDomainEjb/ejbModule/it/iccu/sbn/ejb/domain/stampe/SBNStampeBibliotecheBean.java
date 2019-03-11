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
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.custom.BibliotecaSearch;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBibliotecheVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
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
import javax.ejb.MessageDrivenContext;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
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
public class SBNStampeBibliotecheBean extends SBNStampeBase {

	private static final long serialVersionUID = 6971053718139296078L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private MessageDrivenContext ctx = null;
	private AmministrazioneBiblioteca amministrazioneBib;
	private static Logger log = Logger.getLogger(SBNStampeBibliotecheBean.class);


	public void setSessionContext(SessionContext arg0) throws EJBException,
	RemoteException {

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
			this.amministrazioneBib = DomainEJBFactory.getInstance().getBiblioteca();
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

	public Object elabora(StampaVo stampaVO, BatchLogWriter blw)
	throws RemoteException, Exception {
		String nomeFileErr ="Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess= stampaVO.getIdBatch();
		String nameFile = "";

		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		String fileJrxml = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		fileJrxml = stampaVO.getTemplate();
		pathDownload = stampaVO.getDownload();
		String ticket = stampaVO.getTicket();
		if (stampaVO instanceof StampaOnLineVO) {
			//stampa online
			OutputStampaVo output = new OutputStampaVo();
			output.setStato(ConstantsJMS.STATO_OK);
			List listaBib = ((StampaOnLineVO)stampaVO).getDatiStampa();
			streamRichiestaStampa = effettuaStampa(
					fileJrxml,
					stampaVO.getTipoStampa(),
					avviaOrdinamento(listaBib, stampaVO.getTipoOrdinamento())
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

			log.info("path fileJrxml per le stampe utenti"+fileJrxml);
			System.out.print(""+fileJrxml);
			elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

			utente = ((StampaDiffVO)stampaVO).getUser();
			List parametri = (List)((StampaDiffVO)stampaVO).getParametri();

			StampaBibliotecheVO stampaBiblioteche = (StampaBibliotecheVO)parametri.get(0);
			String polo = stampaBiblioteche.getPolo();
			String nomeBiblioteca = stampaBiblioteche.getNomeBiblioteca();
			BibliotecaSearch bib = new BibliotecaSearch();
			bib.setNomeBib(nomeBiblioteca);
			bib.setCodPolo(polo);

			//qui metto i dati nudi e crudi restituiti dal servizio che gestisce l'area
			List listaOutput = new ArrayList();
			try{
				listaOutput = this.amministrazioneBib.getBiblioteche(bib);//getAllBiblioteche// this.amministrazioneBib.getAllBiblioteche(polo);//getComboBibliotecheSBN();//this.servizi.getListaUtenti((RicercaUtenteBibliotecaVO) parametri.get(0));
				if(listaOutput == null ){
					throw new DataException ("Errore nel recupero dei dati, l'output é nullo");
				}
				for(int k= 0; k<listaOutput.size(); k++){
					//l'assegnazione che segue serve solo a generare una eccezione se c'è un problema
					BibliotecaVO dettaglioBib = (BibliotecaVO)listaOutput.get(k);
				}
			} catch(Exception exep) {
				elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(exep);
				return elaborazioniDifferiteOutputVo;
			}
			//ho controllato tutto l'array list che devo stampare e ora passo a stamparlo
			try {
				//vado a stampare
				String tipoStampa=stampaVO.getTipoStampa();
				streamRichiestaStampa = effettuaStampa(
						fileJrxml,
						tipoStampa,
						avviaOrdinamento(listaOutput, stampaVO.getTipoOrdinamento())
				);

				//metto la stampa generata nella lista dei file da scaricare
				boolean listaEsistente = false;
				if(streamRichiestaStampa !=null){
					try {
						nameFile = utente +"_biblioteche_"+idMess+ "." + tipoStampa;
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
					property = stampaBiblioteche.getCodiceBiblioteca();
					if (property != null && property.length() > 0)
						hm.put("CodiceBiblioteca", property);

					property = stampaBiblioteche.getEnteDiAppartenenza();
					if (property != null && property.length() > 0)
						hm.put("Nome", property);
					elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);
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

	private List avviaOrdinamento(List listaBib, String tipoOrdinamento) {

		List arrayBibliotecheOr = new ArrayList();
		if((tipoOrdinamento.equals("BIBLIO"))){ //||(tipoOrdinamento.equals("CD"))
			arrayBibliotecheOr =this.ordinaBiblioteche(listaBib,  tipoOrdinamento);//"codFornitore",
		}else{
			if((tipoOrdinamento.equals("POLO"))){ //||(tipoOrdinamento.equals("FD"))
				arrayBibliotecheOr =this.ordinaBiblioteche(listaBib,  tipoOrdinamento);//"nomeFornitore"
			}else if (tipoOrdinamento.equals("")){
				arrayBibliotecheOr = listaBib;
			}
		}
		return arrayBibliotecheOr;
	}

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


	//memorizza lo stream di byte nel percorso perfissato
//	public void scriviFile(String user,  String formatoStampa, byte[] streamByte) throws Exception, RemoteException {//byte[] jasperReport, String pathJasper

//	String dirSave="/tmp";//C:\\stampe
//	UtilityCastor util= new UtilityCastor();
//	String dataCorr = util.getCurrentDate();
//	String filename = user +"_biblioteche_"+dataCorr+ "." + formatoStampa;//String.valueOf(System.currentTimeMillis()) + "." + formatoStampa;
////	int indiceDot = fileJrxml.lastIndexOf("/");
//	File newFile = null;
//	int i = -1;
//	while(i <= 0){
//	try {
//	newFile = new File(dirSave, filename);
//	} catch (NullPointerException exception) {
//	exception.printStackTrace();
//	}
//	if(newFile.exists())
//	{
//	newFile.delete();
//	}
//	i=1;
//	try
//	{
//	newFile.createNewFile();
//	}
//	catch(IOException exError)
//	{
//	//            System.out.println("Errore nella creazione del file per il test");
//	dirSave = "C:\\stampe";
//	i=0;
//	}
//	}
//	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile, false));
//	bos.write(streamByte, 0, streamByte.length);
//	bos.flush();
//	bos.close();
//	}

	private List ordinaBiblioteche(List listaBibVO, String tipoOrdinamento){//String arrayFiltri,
		List arrayBibliotecheOrd = new ArrayList();
		List arrayBibliotecheOrdD;
		BibliotecaVO appListaSF = null;
		BibliotecaVO listaSF = null;
		String bibCompFirst = "a";
		String bibCompSecond = "a";
//		int bibCodFirst = -1;
//		int bibCodSec = -1;
		String bibCodFirst = "a";
		String bibCodSec = "a";
		int resultCompare;
		int elemLista = listaBibVO.size();
		if(tipoOrdinamento.equals("BIBLIO")){
			while(listaBibVO.size()>0){
				if(bibCodFirst.equals("a")){// == -1){
					listaSF = (BibliotecaVO)listaBibVO.get(0);//iter.next();
				}
				bibCodFirst = listaSF.getCod_bib();//(Integer.valueOf(listaSF.getCod_bib())).intValue();
				listaBibVO.remove(listaSF);
				if(listaBibVO.size()>0){
					appListaSF = (BibliotecaVO)listaBibVO.get(0);//iter.next();
					bibCodSec = appListaSF.getCod_bib();//listaSF.getCod_bib();// (Integer.valueOf(listaSF.getCod_bib())).intValue();
//					if(bibCodFirst.equals(bibCodSec)){
//					listaBibVO.add(listaSF);
////					bibCodFirst = bibCodSec;
//					listaBibVO.remove(appListaSF);
//					listaBibVO.remove(listaSF);
//					if(listaBibVO.size()>0)
//					listaSF = (BibliotecaVO)listaBibVO.get(0);
//					}
					if(bibCodFirst.compareToIgnoreCase(bibCodSec) < 0){
						arrayBibliotecheOrd.add(listaSF);
						elemLista--;
						listaSF = appListaSF;
						//NB: fornCompareFirst l'ho già rimosso
						//dall'array finale da restituire
					}else if(bibCodFirst.compareToIgnoreCase(bibCodSec) >= 0){
						if(bibCodFirst.equals(bibCodSec)){
							arrayBibliotecheOrd.add(listaSF);
							elemLista--;
						}else{
							listaBibVO.add(listaSF);
						}

						bibCodFirst = bibCodSec;
						listaBibVO.remove(appListaSF);
						listaSF = appListaSF;
					}
//					listaSF = appListaSF;
				}
			}
		}else if(tipoOrdinamento.equals("POLO")){
			while(listaBibVO.size()>0){
				if(bibCompFirst.equals("a") ){
					listaSF = (BibliotecaVO)listaBibVO.get(0);//iter.next();
				}
				bibCompFirst = listaSF.getCod_polo();
				listaBibVO.remove(listaSF);
				if(listaBibVO.size()>0){
					appListaSF = (BibliotecaVO)listaBibVO.get(0);//iter.next();
					bibCompSecond = appListaSF.getCod_polo();
					resultCompare = bibCompFirst.compareToIgnoreCase(bibCompSecond);
//					if(resultCompare == 0){
////					listaBibVO.add(listaSF);
////					bibCodFirst = bibCodSec;
//					listaBibVO.remove(appListaSF);
//					listaBibVO.remove(listaSF);
//					if(listaBibVO.size()>0)
//					listaSF = (BibliotecaVO)listaBibVO.get(0);
//					}
					if(resultCompare <0){
						arrayBibliotecheOrd.add(listaSF);
						elemLista--;
						listaSF = appListaSF;
						//NB: fornCompareFirst l'ho già rimosso
						//dall'array finale da restituire
					}else if(resultCompare >= 0){
						if(bibCodFirst.equals(bibCodSec)){
							arrayBibliotecheOrd.add(listaSF);
							elemLista--;
						}else{
							listaBibVO.add(listaSF);
						}
						bibCompFirst = bibCompSecond;
						listaBibVO.remove(appListaSF);
						listaSF = appListaSF;
					}
				}
			}
		}
		if(elemLista>0){
			arrayBibliotecheOrd.add(listaSF);
		}
		return arrayBibliotecheOrd;
	}
}


