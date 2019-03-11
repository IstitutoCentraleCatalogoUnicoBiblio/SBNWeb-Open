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
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaUtentiDiffVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchSollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.ejb.vo.servizi.utenti.SinteticaUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.servizi.SollecitiUtil;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;


/**
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="SBNStampeUtenti"
 * 			 acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           transaction-type="Container"
 *           destination-jndi-name="SBNStampeUtenti"
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
public class SBNStampeUtentiBean extends SBNStampeBase {
	/**
	 *
	 */
	private static final long serialVersionUID = -4630818956002557089L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
    private static Logger log = Logger.getLogger(SBNStampeUtentiBean.class);


    Servizi getServizi() throws Exception {
		return DomainEJBFactory.getInstance().getServizi();
	}




	/**
	 * Required method for container to set context.
	 *
	 * @generated
	 */
	public void setMessageDrivenContext(MessageDrivenContext mdc) throws EJBException {
		return;
	}

    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
    	return;
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
		return;
	}


	public Object elabora(StampaVo input, BatchLogWriter blw) throws Exception {
		Logger _log = blw != null ? blw.getLogger() : log;
		StampaUtentiDiffVO stampaVO = (StampaUtentiDiffVO) input;
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

		try {
//				String provaA  = System.getProperty("jboss.server.temp.dir");
			List listaUtenti;
			//rox String fileJrxml = null;
			String tipoStampa = null;
			//rox String pathDownload = null;
			//rox String utente = null;
			// rox fileJrxml = stampaVO.getTemplate();
			tipoStampa = stampavo.getTipoStampa();
			// rox pathDownload = stampaVO.getDownload();
			// rox String downloadLinkPath = stampaVO.getDownloadLinkPath();
			String textListEm="ATTENZIONE: utente ricercato inesistente, cambiare i parametri di ricerca.";
			// rox byte[] streamRichiestaStampa;
			// rox byte[] streamLogStampa;
//				OutputStampaVo output = new OutputStampaVo();
//				output.setStato(ConstantsJMS.STATO_OK);
			if (stampavo instanceof StampaOnLineVO)
			{
				OutputStampaVo output = new OutputStampaVo();
				output.setStato(ConstantsJMS.STATO_OK);
				listaUtenti = ((StampaOnLineVO)stampavo).getDatiStampa();
				streamRichiestaStampa = effettuaStampa(fileJrxml, stampavo.getTipoStampa(), listaUtenti);

				if (streamRichiestaStampa != null) {
					output.setOutput(streamRichiestaStampa);
				}
				else
					output.setStato(ConstantsJMS.STATO_ERROR);
				return output;
			}
			else
			{

				ElaborazioniDifferiteOutputVo elaborazioniDifferiteoutput = new ElaborazioniDifferiteOutputVo(stampaVO);
				elaborazioniDifferiteoutput.setStato(ConstantsJMS.STATO_OK);

				elaborazioniDifferiteoutput.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

				utente = ((StampaDiffVO)stampavo).getUser();
				List parametri = (List)((StampaDiffVO)stampavo).getParametri();

				listaUtenti = new ArrayList();

				RicercaUtenteBibliotecaVO rub = (RicercaUtenteBibliotecaVO) parametri.get(0);
				try{
					listaUtenti = getServizi().getListaUtenti(rub);
					if (ValidazioneDati.isFilled(listaUtenti) )	{
						for(int index = 0; index < listaUtenti.size(); index++)	{
							SinteticaUtenteVO ute=(SinteticaUtenteVO) listaUtenti.get(index);
							if (index == 0)
								ute.setCriteriRicerca(rub);
							if (ValidazioneDati.isFilled(ute.getAteneo()) )
							   	ute.setAteneo(CodiciProvider.cercaDescrizioneCodice(ute.getAteneo().trim(), CodiciType.CODICE_ATENEI, CodiciRicercaType.RICERCA_CODICE_SBN ));
						}
					}
					_log.info("Per ottenere la lista di utenti la ricerca delle informazioni é avvenuta nella tabella : Trl_utenti_biblioteca (" + listaUtenti.size() + " elementi)");
				} catch(Exception e) {
					_log.error("", e);
				}

				// gestione assenza risultati
				if(!ValidazioneDati.isFilled(listaUtenti) ){
					elaborazioniDifferiteoutput.setStato(ConstantsJMS.STATO_OK);
					nameFile = utente +"_log_utenti_"+idMess+ "." + "txt";
					streamLogStampa = ("La richiesta non ha prodotto risultati perchè assenti").getBytes("UTF8");
					this.scriviFile(utente, "txt", streamLogStampa, pathDownload, nameFile);
					listaDownload.add(new DownloadVO(nameFile, downloadLinkPath+nameFile));
					elaborazioniDifferiteoutput.setDownloadList(listaDownload);
					return elaborazioniDifferiteoutput;
				}

				try {
					BatchManager.getBatchManagerInstance().checkForInterruption(idMess);

					//vado a stampare
					streamRichiestaStampa = effettuaStampa(fileJrxml, tipoStampa, listaUtenti);
					boolean listaEsistente = false;
					if(streamRichiestaStampa !=null){

					try {
						//nameFile = utente +"_utenti_"+idMess+ "." + tipoStampa;
						nameFile = stampaVO.getFirmaBatch()+ "." + stampavo.getTipoStampa().toLowerCase();
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
						elaborazioniDifferiteoutput.setDownloadList(listaDownload);
						elaborazioniDifferiteoutput.setDataDiFineElaborazione(DateUtil.getDate()+DateUtil.getTime());
						// Preparare hash map dei parametri
						Map<String, String> hm = new HashMap<String, String>();
						String property;

						property = rub.getCodUte();
						if (property != null && property.length() > 0)
							hm.put("Codice utente", property);

						property = rub.getNome();
						if (property != null && property.length() > 0)
							hm.put("Nome", property);

						property = rub.getCognome();
						if (property != null && property.length() > 0)
							hm.put("Cognome", property);

						property = rub.getEmail();
						if (property != null && property.length() > 0)
							hm.put("Email", property);

						property = rub.getProvResidenza();
						if (property != null && property.length() > 0)
							hm.put("ProvResidenza", property);

						property = rub.getNazCitta();
						if (property != null && property.length() > 0)
							hm.put("ProvResidenza", property);
//
//							property = rub.get();
//							if (property != null && property.length() > 0)
//								hm.put("Biblioteca", property);
//
//							property = rub.getCitta();
//							if (property != null && property.length() > 0)
//								hm.put("Citt'", property);
//
//
//							property = rub.getNomeFornitore();
//							if (property != null && property.length() > 0)
//								hm.put("Nome fornitore", property);

						elaborazioniDifferiteoutput.setParametriDiRicercaMap(hm);
						}else{
							streamRichiestaStampa = new ByteArrayInputStream(textListEm.getBytes());
						}
				}catch (Exception ef) {
//						output.setStato(ConstantsJMS.STATO_ERROR);
					elaborazioniDifferiteoutput.setStato(ConstantsJMS.STATO_ERROR);
//						if(!listaEsistente){
					_log.error("", ef);
					return elaborazioniDifferiteoutput;
//						throw new Exception("Errore nell'elaborazione della richiesta di stampa differita, "+ef.getMessage());
//						}
				}
			return elaborazioniDifferiteoutput;
			}
//				return output;

		} catch (Exception e) {
			_log.error("", e);
			throw e;
		}
	}

	public ElaborazioniDifferiteOutputVo elaboraSolleciti(
			ParametriBatchSollecitiVO stampaVO, BatchLogWriter blog) throws ApplicationException, EJBException {
		//recupero il vo di output già preparatato dalla fase precedente;
		ElaborazioniDifferiteOutputVo output = stampaVO.getOutput();
		List<ElementoStampaSollecitoVO> listaSollecitiPerStampa = stampaVO.getListaSollecitiPerStampa();
		if (!ValidazioneDati.isFilled(listaSollecitiPerStampa)) {
			output.setStato(ConstantsJMS.STATO_ERROR);
			return output;
		}

		String nameFile = "";
		getDownloadFileName().clear();

		String pathDownload = StampeUtil.getBatchFilesPath();
		String utente = null;
		String fileJrxml = null;
		InputStream streamRichiestaStampa;

		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		fileJrxml = stampaVO.getTemplate();

		Logger _log = blog.getLogger();	// log automatico batch

		//almaviva5_20150521 modello sollecito
		ParametriBibliotecaVO parBib;
		try {
			parBib = getServizi().getParametriBiblioteca(stampaVO.getTicket(),
					stampaVO.getCodPolo(), stampaVO.getCodBib());
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);
		}

		ModelloSollecitoVO modello = parBib.getModelloSollecito();
		modello = (modello != null) ? modello : SollecitiUtil.getModelloSollecitoBase();

		List<ComboVO> solleciti = new ArrayList<ComboVO>();
		for (ElementoStampaSollecitoVO ess : listaSollecitiPerStampa)
			try {
				solleciti.add(new ComboVO(SollecitiUtil.costruisciModelloSollecito(modello, TipoModello.LETTERA, ess, true), null));
			} catch (Exception e) {
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_MODELLO_SOLLECITO);
			}

		fileJrxml = "default_sollecito_html.jrxml";
		try {
			String tipoStampa = stampaVO.getTipoStampa();
			streamRichiestaStampa = effettuaStampa(
					fileJrxml,
					tipoStampa,
					solleciti );

			boolean listaEsistente = false;

			if (streamRichiestaStampa != null) {
				try {
					nameFile = stampaVO.getFirmaBatch() + "." + tipoStampa;
					this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);
				}catch (Exception ef) {
					throw new Exception("Creazione del file"+ nameFile+"fallita");
				}
				String filename = "";
				for(int index = 0; index < getDownloadFileName().size(); index++){
					listaEsistente = true;
					filename = (getDownloadFileName().get(index));
					listaDownload.add(new DownloadVO(filename, filename));
				}
				if(!listaEsistente){
					throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
				}
				output.setDownloadList(listaDownload);
				output.setStato(ConstantsJMS.STATO_OK);
			}

		}catch (Exception e) {
			_log.error("", e);
			output.setStato(ConstantsJMS.STATO_ERROR);
			throw new EJBException(e);
		}

		return output;

	}

}
