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
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRichiestaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.NamingException;


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
public class SBNStampeRichiestaBean extends SBNStampeBase implements MessageDrivenBean, MessageListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1354104643416107737L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	MessageDrivenContext ctx = null;
	private Inventario inventario;


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

	public Object elabora(StampaVo stampaVO, BatchLogWriter log)
	throws Exception {
		getDownloadFileName().clear();
		try{
			InputStream streamRichiestaStampa = null;
			String textListEm="ATTENZIONE: richiesta ricercata inesistente, cambiare i parametri di ricerca";
			String template = null;
			List<MovimentoListaVO> listaOutput = new ArrayList<MovimentoListaVO>();
			if (stampaVO instanceof StampaOnLineVO) {
				OutputStampaVo output = new OutputStampaVo();
				StampaOnLineVO input = (StampaOnLineVO)stampaVO;
				output.setStato(ConstantsJMS.STATO_OK);
				//prendo la singola richiesta da stampare
				MovimentoListaVO datiStampa = (MovimentoListaVO) input.getDatiStampa().get(0);

				if (((StampaOnLineVO)stampaVO).getTemplate() != null && !((StampaOnLineVO)stampaVO).getTemplate().trim().equals("")){
					template = stampaVO.getTemplate();
				}

				if (template != null && !template.equals("") ){
					if(datiStampa != null){
						List<StampaRichiestaVO> lista = preparaDatiRichiestaPerTemplate(
								stampaVO, listaOutput, template);
						//genero la stampa
						streamRichiestaStampa = effettuaStampa(
								stampaVO.getTemplate(),
								stampaVO.getTipoStampa(),
								lista);
						//metto la stampa generata nella lista dei file da scaricare
						if(streamRichiestaStampa !=null){
							output.setOutput(streamRichiestaStampa);
						}else{
							streamRichiestaStampa = new ByteArrayInputStream(textListEm.getBytes());
						}
					}
					return output;
				}
				return output;
			}
			return template;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/**
	 * @param stampaVO
	 * @param listaOutput
	 * @return
	 * @throws ValidationException
	 */
	@SuppressWarnings("unchecked")
	private List<StampaRichiestaVO> preparaDatiRichiestaPerTemplate(
			StampaVo stampaVO, List listaOutput, String template)
			throws ValidationException, DataException {
		Tbf_biblioteca_in_poloDao daoBib = new Tbf_biblioteca_in_poloDao();
		String descrBib;
		Tbf_biblioteca_in_polo biblioteca = null;
		List<StampaRichiestaVO> listaConEtichetteMoltiplicate;
		List<StampaRichiestaVO> listaRichiesta = new ArrayList<StampaRichiestaVO>();
		StampaOnLineVO params = (StampaOnLineVO) stampaVO;
		MovimentoListaVO req = (MovimentoListaVO) ValidazioneDati.first(params.getDatiStampa());
		try {
			//almaviva5_20101206 #4027
			biblioteca = daoBib.select(req.getCodPolo(), req.getCodBibOperante());
		} catch (DaoManagerException e) {}
		if (biblioteca != null) {
			descrBib = biblioteca.getDs_biblioteca();
		} else {
			descrBib = "Descrizione biblioteca mancante";
		}

		int size = ValidazioneDati.size(params.getDatiStampa());
		for (int i = 0; i < size; i++){
			StampaRichiestaVO sr = new StampaRichiestaVO();
			if (template != null) {
				sr.setDescBib(descrBib);
				sr.setIdRichiesta(String.valueOf(req.getIdRichiesta()));
				sr.setCodStatoRic(req.getStato_richiesta());
				sr.setTipoServizio(req.getTipoServizio());
				sr.setCodBibUte("");
				sr.setCodUte(req.getCodUte().trim());
				sr.setCognomeNome("   " + req.getCognomeNome().trim());

				//almaviva5_20110127 #4179
				sr.setDataInizio(DateUtil.formattaData(req.getDataInizioPrev().getTime()));
				sr.setConsegnato(req.isConsegnato());
				sr.setPagine(req.getIntervalloCopia());
				sr.setDatiDocumento(req.getDatiInventario());

				if (req.getDataInizioEff() != null)
					sr.setDataInizioEff(DateUtil.formattaData(req.getDataInizioEff().getTime()));
				else
					sr.setDataInizioEff(sr.getDataInizio());

				if (req.getDataFinePrev() != null){
				sr.setDataFineEff(DateUtil.formattaData(req.getDataFinePrev().getTime()));
				}else{
					sr.setDataFineEff("");
				}
				if (req.getDataProroga() != null){
					sr.setDataProroga(DateUtil.formattaData(req.getDataProroga().getTime()));
				}else{
					sr.setDataProroga("");
				}
				sr.setCostoServizio(req.getCostoServizioString());
				sr.setSegnatura(req.getSegnatura());
				if (req.getNatura() != null && req.getNatura().equals("S")){
					sr.setNatura(req.getNatura());
					sr.setNumFascicolo(req.getNumFascicolo());
					sr.setNumVolume(req.getNumVolume());
					sr.setAnnoPeriodico(req.getAnnoPeriodico());
				}else{
					sr.setNatura(null);
					sr.setNumFascicolo("");
					sr.setNumVolume("");
					sr.setAnnoPeriodico("");
				}
				sr.setBid(req.getBid());
				sr.setTitolo(req.getTitolo());
				sr.setCodBibInv(req.getCodBibInv());
				sr.setCodSerieInv(req.getCodSerieInv());
				sr.setCodInvenInv(req.getCodInvenInv());
				sr.setDataDiElaborazione(DateUtil.formattaData(DaoManager.now().getTime()));

				sr.setAttivitaCorrente(req.getAttivita().trim());
				sr.setTestoStampaModulo(req.getTestoStampaModulo().trim());

				//almaviva5_20171122 prenotazioni posto
				if (req.isWithPrenotazionePosto()) {
					PrenotazionePostoVO pp = req.getPrenotazionePosto();
					sr.setPrenotazionePosto(true);
					sr.setDescrizioneSala(pp.getPosto().getSala().getDescrizione());
					sr.setSalaOrarioInizio(DateUtil.formattaOrario(pp.getTs_inizio()));
					sr.setSalaOrarioFine(DateUtil.formattaOrario(pp.getTs_fine()));
				}

				if (req.isRichiestaSuInventario()) {
					String keyinv = ConversioneHibernateVO.toWeb().chiaveInventario(req.getCodBibInv(), req.getCodSerieInv(),
							Integer.valueOf(req.getCodInvenInv()));
					sr.setBarcodeInv(keyinv);
				} else
					sr.setBarcodeInv(req.getDatiInventario());

				//almaviva5_20190128
				sr.setDataRichiesta(req.getDataRichiestaNoOraString());
			}
			listaRichiesta.add(sr);
		}
		//
		List<StampaRichiestaVO> lista = null;
		int numCopie = 0;
		if (stampaVO instanceof StampaOnLineVO) {
			StampaOnLineVO input = params;
			numCopie = input.getNumCopie();
		}else{
			StampaDiffVO input = ((StampaDiffVO)stampaVO);
			numCopie = input.getNumCopie();
		}
		listaConEtichetteMoltiplicate = new ArrayList<StampaRichiestaVO>();
		lista = calcolaRichiestePerNumCopie(listaConEtichetteMoltiplicate, lista, listaRichiesta, numCopie);
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
	private List<StampaRichiestaVO> calcolaRichiestePerNumCopie(
			List<StampaRichiestaVO> listaConEtichetteMoltiplicate,
			List<StampaRichiestaVO> lista,
			List<StampaRichiestaVO> listaEti, int numCopie)
			throws ValidationException {
		if (numCopie < 1)
			throw new ValidationException("controllareNumeroCopie");

		if (numCopie > 1) {

			for (int indez = 0; indez < listaEti.size(); indez++) {
				StampaRichiestaVO rec = listaEti.get(indez);
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
	 * This method implements the business logic for the EJB.
	 *
	 */
	public void onMessage(Message message)  {}

	public void setSessionContext(javax.ejb.SessionContext ctx) throws javax.ejb.EJBException, java.rmi.RemoteException{
	}

}
