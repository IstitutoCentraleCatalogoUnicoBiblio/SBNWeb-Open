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
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.ejb.vo.stampe.StrutturaTerna;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;

/**
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="SBNStampeTerminiThesauro" acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue" transaction-type="Container"
 *           destination-jndi-name="SBNStampeTerminiThesauro"
 *           message-selector="STATO='HELD'"
 *
 * @ejb.resource-ref res-ref-name="jms/QCF"
 *                   res-type="javax.jms.QueueConnectionFactory"
 *                   res-auth="Container"
 *
 * @ejb.transaction="Supports"
 * @jboss.destination-jndi-name name="queue/A"
 * @jboss.resource-ref res-ref-name="jms/QCF" jndi-name="ConnectionFactory" <!--
 *                     end-xdoclet-definition -->
 * @generated
 *
 */
public class SBNStampeTerminiThesauroBean extends SBNStampeBase {
	/**
	 *
	 */
	private static final long serialVersionUID = 3219265979463412649L;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> The context for the
	 * message-driven bean, set by the EJB container.
	 *
	 * @generated
	 */
	private Semantica semantica;
	private static Logger log = Logger
			.getLogger(SBNStampeTerminiThesauroBean.class);

	private Semantica getSemantica() {
		try {
			if (semantica != null)
				return semantica;

			this.semantica = DomainEJBFactory.getInstance().getSemantica();

			return semantica;
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

	/**
	 * Required method for container to set context.
	 *
	 * @generated
	 */
	public void setMessageDrivenContext(MessageDrivenContext mdc)
			throws EJBException {

	}

	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {

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

	public Object elabora(StampaVo stampaVO, BatchLogWriter blw)
			throws Exception {
		String nomeFileErr = "Elaborazione fallita, per maggiori dettagli aprire il link";
		String idMess = stampaVO.getIdBatch();
		String nameFile = "";

		String pathDownload = null;
		String downloadLinkPath = stampaVO.getDownloadLinkPath();
		String utente = null;
		String fileJrxml = null;
		InputStream streamRichiestaStampa;
		byte[] streamLogStampa;
		fileJrxml = stampaVO.getTemplate();
		pathDownload = stampaVO.getDownload();
		String ticket = stampaVO.getTicket();
		if (stampaVO instanceof StampaOnLineVO) {
			OutputStampaVo output = new OutputStampaVo();
			StampaOnLineVO input = (StampaOnLineVO) stampaVO;
			output.setStato(ConstantsJMS.STATO_OK);
			// metto i dati da stampare in un List
			List listaTerminiThesauro = input.getDatiStampa();
			streamRichiestaStampa = effettuaStampa(fileJrxml,
					stampaVO.getTipoStampa(), listaTerminiThesauro);
			// se tutto è Ok ritorno come stream la stampa generata
			if (streamRichiestaStampa != null) {
				output.setOutput(streamRichiestaStampa);
			} else {
				output.setStato(ConstantsJMS.STATO_ERROR);
			}
			return output;
		} else {
			// Stampa differita
			ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(
					stampaVO);
			output.setStato(ConstantsJMS.STATO_OK);

			log.info("path fileJrxml per le stampe termini thesauro"
					+ fileJrxml);
			System.out.print("" + fileJrxml);
			output.setDataDiElaborazione(DateUtil
					.getDate() + DateUtil.getTime());

			utente = ((StampaDiffVO) stampaVO).getUser();
			List parametri = (List) ((StampaDiffVO) stampaVO)
					.getParametri();

			StampaTerminiThesauroVO thesauro = (StampaTerminiThesauroVO) parametri
					.get(0);

			// ParametriStampaVO parSta;
			ParametriStampaTerminiThesauroVO parStaTermThes = new ParametriStampaTerminiThesauroVO();

			// DA RIEMPIRE PER COMPLETARE
			parStaTermThes.setCodBib(thesauro.getCodBib());
			parStaTermThes.setCodPolo(thesauro.getCodPolo());
			parStaTermThes.setCodThesauro(thesauro.getCodeThesauro());
			parStaTermThes.setDescrizioneBiblioteca(thesauro.getDenBib());
			parStaTermThes.setDescrizioneThesauro(thesauro.getDescThesauro());
			parStaTermThes.setSoloTerminiBiblioteca(false);// thesauro.getsoloTerminiBiblioteca
			parStaTermThes.setSoloLegamiTitoloInseritiDaBiblioteca(false);// thesauro.getSoloLegamiTitoloInseritiDaBiblio);
			parStaTermThes.setStampaNoteLegameTitoli(false); // thesauro.getNoteLegameTitoli
			parStaTermThes.setStampaTerminiCollegati(Boolean
					.getBoolean(thesauro.isRelazAltriTerm()));// Boolean.parseBoolean(thesauro.isRelazAltriTerm()));//
			parStaTermThes.setStampaNoteTerminiCollegati(false);// thesauro.getNoteTerminiCollegati
			parStaTermThes.setStampaTitoli(Boolean.parseBoolean(thesauro.isStampaTitoli()));// Boolean.parseBoolean(thesauro.isStampaTitoli()));//
			parStaTermThes.setStampaFormeRinvio(false);// thesauro.getStampaFormeRinvio
			List<ElementoStampaSemanticaVO> elementsStSeman = new ArrayList<ElementoStampaSemanticaVO>();

			// qui metto i dati nudi e crudi restituiti dal servizio che
			// gestisce l'area
			List listaOutput = new ArrayList();
			StampaTerminiThesauroVO thesauroDaStam = new StampaTerminiThesauroVO();
			elementsStSeman = getSemantica().stampeBatch(ticket,
					StampaType.STAMPA_TERMINI_THESAURO, parStaTermThes);
			int count = 0;
			int dimArrayElemThesauro = elementsStSeman.size();
			boolean nuovoThesauro = false;
			if (dimArrayElemThesauro > 0) {
				ElementoStampaTerminiThesauroVO elemStampa = new ElementoStampaTerminiThesauroVO();
				elemStampa = (ElementoStampaTerminiThesauroVO) elementsStSeman
						.get(count);
				thesauroDaStam = impostaThesauroDaStampare(elemStampa,
						parStaTermThes);

				nuovoThesauro = false;
				while ((dimArrayElemThesauro > (count))) {
					if (nuovoThesauro) {
						thesauroDaStam = impostaThesauroDaStampare(elemStampa,
								parStaTermThes);// , legamiTitoloUnThesauro,
												// legameTermineUnThesauro);//,
												// confBO, presenzaProt) ;
					}
					StrutturaTerna strutturaLTit = null;
					List legamiTitolo = new ArrayList();

					LegameTitoloVO singleTitolo;
					List<LegameTitoloVO> lisLT = elemStampa.getLegamiTitoli();// .subList(0,
																				// sizeo);
					if (lisLT != null) {// .size() > 0){
						for (Iterator it = lisLT.iterator(); it.hasNext();) {
							singleTitolo = (LegameTitoloVO) it.next();
							strutturaLTit = new StrutturaTerna(
									singleTitolo.getBid(),
									singleTitolo.getTitolo(),
									singleTitolo.getNotaLegame());
							legamiTitolo.add(strutturaLTit);
						}
					}
					JRRewindableDataSource titoli = new JRBeanCollectionDataSource(
							legamiTitolo);// elemStampa.getLegami());//legami
					thesauroDaStam.setTitoli(titoli);

					StrutturaTerna strutturaLTerm = null;
					List legamiTerms = new ArrayList();// elemStampa.getLegamiTermini());
					LegameTermineVO singleTermine;
					List<LegameTermineVO> lisLTer = elemStampa
							.getLegamiTermini();
					String appS1;
					String appS2;
					String appS3;
					if (lisLTer != null) {// lisLTer.size() > 0){
						for (Iterator it = lisLTer.iterator(); it.hasNext();) {
							singleTermine = (LegameTermineVO) it.next();
							appS1 = new String(singleTermine.getDid());
							appS2 = new String(singleTermine.getTipoLegame());
							appS3 = new String(singleTermine.getTesto());
							strutturaLTerm = new StrutturaTerna(appS1, appS2,
									appS3);// singleTermine.getDid(),
											// singleTermine.getTipoLegame(),
											// singleTermine.getTesto());
							legamiTerms.add(strutturaLTerm);
						}
					}

					JRRewindableDataSource termini = new JRBeanCollectionDataSource(
							legamiTerms);// legamiTermini
					thesauroDaStam.setTermini(termini);
					listaOutput.add(thesauroDaStam);
					nuovoThesauro = true;
					count++;

					thesauroDaStam = new StampaTerminiThesauroVO();
					if (dimArrayElemThesauro > (count)) {
						elemStampa = new ElementoStampaTerminiThesauroVO();
						elemStampa = (ElementoStampaTerminiThesauroVO) elementsStSeman
								.get(count);
					}
				}
			}

			try {
				String tipoStampa = stampaVO.getTipoStampa();
				streamRichiestaStampa = effettuaStampa(fileJrxml, tipoStampa,
						listaOutput);
				boolean listaEsistente = false;

				if (streamRichiestaStampa != null) {
					try {
						nameFile = stampaVO.getFirmaBatch() + "." + tipoStampa;
						this.scriviFile(utente, tipoStampa,
								streamRichiestaStampa, pathDownload, nameFile);
					} catch (Exception ef) {
						throw new Exception("Creazione del file" + nameFile
								+ "fallita");
					}
					String filename = "";
					for (int index = 0; index < getDownloadFileName().size(); index++) {
						listaEsistente = true;
						filename = (getDownloadFileName().get(index));
						output.addDownload(filename, downloadLinkPath + filename);
					}
					if (!listaEsistente) {
						throw new Exception(
								"elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
					}

					output
							.setDataDiFineElaborazione(DateUtil.getDate()
									+ DateUtil.getTime());

					// Preparare hash map dei parametri (perchè?? e perché solo
					// qui??)
					Map hm = new HashMap();
					String property;

					property = thesauro.getDenBib();
					if (property != null && property.length() > 0) {
						hm.put("Denominazione biblioteca", property);
					}

					property = thesauro.getDescThesauro();
					if (property != null && property.length() > 0) {
						hm.put("Descrizione thesauro", property);
					}
					output
							.setParametriDiRicercaMap(hm);
				}
			} catch (Exception ef) {
				output.setStato(ConstantsJMS.STATO_ERROR);
				blw.logWriteException(ef);
				return output;
			}
			return output;
		}
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
	 *
	 * @throws JMSException
	 * @throws JMSException
	 *
	 * @generated
	 */

	public StampaTerminiThesauroVO impostaThesauroDaStampare(
			ElementoStampaTerminiThesauroVO elemStampa,
			ParametriStampaTerminiThesauroVO parStaTerThes) {

		boolean bapp = false;
		StampaTerminiThesauroVO stampa = new StampaTerminiThesauroVO();
		// String denominazione della biblioteca
		stampa.setDenBib(parStaTerThes.getDescrizioneBiblioteca());

		stampa.setDescThesauro(parStaTerThes.getDescrizioneThesauro());
		stampa.setDateInsDa(parStaTerThes.getTsIns_da());
		stampa.setDateInsA(parStaTerThes.getTsIns_a());
		stampa.setDateAggDa(parStaTerThes.getTsVar_da());
		stampa.setDateAggA(parStaTerThes.getTsVar_a());
		bapp = parStaTerThes.isStampaTitoli();
		stampa.setStampaTitoli(String.valueOf(bapp));// parStaTerThes.isStampaTitoli());//Boolean.valueOf(parStaTerThes.isStampaTitoli()));
		bapp = parStaTerThes.isStampaNote();
		stampa.setStampaStringaThes(String.valueOf(bapp));// Boolean.valueOf(parStaTerThes.isStampaNote()));
		bapp = parStaTerThes.isSoloTerminiBiblioteca();
		stampa.setThesauriBiblio(String.valueOf(bapp));// Boolean.valueOf(parStaTerThes.isSoloTerminiBiblioteca()));
		stampa.setRelazAltriTerm(String.valueOf(parStaTerThes
				.isStampaTerminiCollegati()));// Boolean.valueOf(parStaTerThes.isStampaTerminiCollegati()));
		stampa.setCodBib(parStaTerThes.getCodBib());
		stampa.setCodPolo(parStaTerThes.getCodPolo());

		stampa.setStampaNoteTitle(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setStampaNoteThes(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setRelazAltreForm(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setStampaTermBiblio(String.valueOf(false));// Boolean.valueOf(false));
		stampa.setTermine(elemStampa.getTesto());
		stampa.setDid(elemStampa.getId());
		stampa.setNote(elemStampa.getNote());

		return stampa;
	}

}
