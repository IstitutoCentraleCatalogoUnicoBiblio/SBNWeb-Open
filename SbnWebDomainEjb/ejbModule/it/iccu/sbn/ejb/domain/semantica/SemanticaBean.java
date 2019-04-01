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
package it.iccu.sbn.ejb.domain.semantica;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnEJB;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnHttpIndice;
import it.iccu.sbn.SbnMarcFactory.factory.ServerHttp;
import it.iccu.sbn.SbnMarcFactory.factory.semantica.SbnAbstractDAO;
import it.iccu.sbn.SbnMarcFactory.factory.semantica.SbnClassiDAO;
import it.iccu.sbn.SbnMarcFactory.factory.semantica.SbnSoggettiDAO;
import it.iccu.sbn.SbnMarcFactory.factory.semantica.SbnThesauriDAO;
import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.SbnMarcFactory.util.semantica.CatturaReticoloSoggetto;
import it.iccu.sbn.SbnMarcFactory.util.semantica.DettaglioOggettoSemantico;
import it.iccu.sbn.SbnMarcFactory.util.semantica.ReticoloDescrittori;
import it.iccu.sbn.SbnMarcFactory.util.semantica.ReticoloSoggetti;
import it.iccu.sbn.SbnMarcFactory.util.semantica.ReticoloThesauro;
import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.A686;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionesemantica.CountLegamiSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.PosizioneDescrittore;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.CreaVariaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.RicercaAbstractListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.RicercaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO.LegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.LegameTitoloAuthSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ContaSoggettiCollegatiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineLegameTitoloSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineModificaSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.FondiSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriBatchSoggettoBaseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriCancellazioneSoggettiNonUtilizzatiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaSistemaClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaTitoliCollegatiVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.amministrazione.ContatoriDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.semantica.SemanticaDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_contatore;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_descrittore;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_sistemi_classi_biblioteche;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.custom.Credentials;
import it.iccu.sbn.vo.validators.semantica.CreaVariaSogDesValidator;
import it.iccu.sbn.vo.validators.semantica.RicercaComuneValidator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc --> * <!--
 * begin-xdoclet-definition -->
 *
 * @ejb.bean name="Semantica" description="A session bean named Semantica"
 *           display-name="Semantica" jndi-name="sbnWeb/Semantica"
 *           type="Stateless" transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class SemanticaBean extends TicketChecker implements Semantica {

	private static final long serialVersionUID = 1796462003112110964L;

	private static Logger log = Logger.getLogger(Semantica.class);

	private static final String VALORE_NULLO = "--";
	private static final int BATCH_COMMIT_THRESHOLD = 1;

	private AmministrazionePolo polo;
	private AmministrazioneBibliotecario bibliotecario;
	private Profiler profiler;

	private Reference<FactorySbn> factoryIndice;
	private Reference<FactorySbn> factoryPolo;

	private SessionContext ctx;


	private AmministrazionePolo getPolo() {
		if (polo != null)
			return polo;

		try {
			this.polo = DomainEJBFactory.getInstance().getPolo();
			return polo;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	private AmministrazioneBibliotecario getBibliotecario() {
		if (bibliotecario != null)
			return bibliotecario;

		try {
			this.bibliotecario = DomainEJBFactory.getInstance().getBibliotecario();
			return bibliotecario;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	private Profiler getProfiler() {
		if (profiler != null)
			return profiler;

		try {
			this.profiler = DomainEJBFactory.getInstance().getProfiler();
			return profiler;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
	}

	public void ejbActivate() throws EJBException, RemoteException {
		log.info("ejbActivate");
	}

	public void ejbPassivate() throws EJBException, RemoteException {
		log.info("ejbPassivate");
	}

	public void ejbRemove() throws EJBException, RemoteException {
		log.info("ejbRemove");
	}

	private Credentials getCredentials() throws RemoteException,
			DaoManagerException {
		Credentials credentials = getPolo().getCredentials();
		return credentials;
	}

	private FactorySbn getFactoryIndice() throws RemoteException,
			DaoManagerException, SbnMarcException {
		if (factoryIndice == null || factoryIndice.get() == null) {
			ParametriHttp paramIndice = getPolo().getIndice();
			factoryIndice = new SoftReference<FactorySbn>(new FactorySbnHttpIndice("INDICE", new ServerHttp(paramIndice, getCredentials())) );
		}
		return factoryIndice.get();
	}

	private FactorySbn getFactoryPolo() throws RemoteException,
			DaoManagerException, SbnMarcException {
		if (factoryPolo == null || factoryPolo.get() == null) {
			factoryPolo = new SoftReference<FactorySbn>(new FactorySbnEJB("POLO"));
		}

		return factoryPolo.get();
	}

	private SbnUserType getSbnUserType(String ticket) throws RemoteException,
			DaoManagerException {
		SbnUserType sbn = getBibliotecario().getUserSbnMarc(ticket);
		return sbn;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public ContaSoggettiCollegatiVO contaSoggettiPerDidCollegato(
			boolean livelloPolo, String Did, String ticket) throws DAOException {

		ContaSoggettiCollegatiVO risultati = null;
		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.ricercaSoggettiPerDidCollegato(livelloPolo, Did,
					10, PosizioneDescrittore.QUALUNQUE_POSIZIONE, false, TipoOrdinamento.PER_ID, null);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			risultati = new ContaSoggettiCollegatiVO();

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			risultati.setTotRighe(totRighe);
			risultati.setEsito(esito);
			risultati.setTestoEsito(testoEsito);
		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);

		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

		return risultati;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaSoggettoListaVO ricercaSoggettiPerDidCollegato(
			boolean livelloPolo, String did, String ticket, int elementiBlocco,
			TipoOrdinamento tipoOrdinamento, String edizione)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			//almaviva5_20120906 evolutive CFI
			SbnEdizioneSoggettario _edizione = ValidazioneDati.isFilled(edizione) ?
				SbnEdizioneSoggettario.valueOf(edizione) :
				null;
			SBNMarc risposta = server.ricercaSoggettiPerDidCollegato(livelloPolo, did,
					elementiBlocco, null, false, tipoOrdinamento, _edizione);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			RicercaSoggettoListaVO lista = new RicercaSoggettoListaVO();

			SbnResponseType response = risposta.getSbnMessage().getSbnResponse();
			int totRighe = response.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			int maxRighe = response.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = response.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			lista.setEsito(response.getSbnResult().getEsito());
			lista.setTestoEsito(response.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			lista.setRisultati(server.creaListaSinteticaSoggetti(risposta,
					livelloPolo, SemanticaDAO.codPoloFromTicket(ticket),
					SemanticaDAO.codBibFromTicket(ticket)));

			return lista;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}
		// return null;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaSoggettoListaVO ricercaDescrittoriPerDidCollegato(
			boolean livelloPolo, String Did, String ticket, int elementiBlocco)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );


			SBNMarc risposta = server.ricercaDescrittoriPerDidCollegato(livelloPolo,
					Did, elementiBlocco);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			RicercaSoggettoListaVO lista = new RicercaSoggettoListaVO();

			int totRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			int maxRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			lista.setEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito());
			lista.setTestoEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			lista.setRisultati(server.creaListaSinteticaSoggetti(risposta,
					livelloPolo, SemanticaDAO.codPoloFromTicket(ticket),
					SemanticaDAO.codBibFromTicket(ticket)));

			return lista;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	@SuppressWarnings("unchecked")
	public CatSemSoggettoVO ricercaSoggettiPerBidCollegato(boolean livelloPolo,
			String Bid, String codSoggettario, String ticket, int elementiBlocco)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			SBNMarc risposta = server.ricercaSoggettiPerBidCollegato(livelloPolo, Bid,
					codSoggettario, true, elementiBlocco);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			CatSemSoggettoVO lista = new CatSemSoggettoVO();

			SbnOutputType sbnOutput =
				risposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput();
			int totRighe = sbnOutput.getTotRighe();
			int maxRighe = sbnOutput.getMaxRighe();
			String idLista = sbnOutput.getIdLista();
			SbnResultType sbnResult =
				risposta.getSbnMessage().getSbnResponse().getSbnResult();
			lista.setEsito(sbnResult.getEsito());
			lista.setTestoEsito(sbnResult.getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			List listaSogg = server.creaListaSinteticaSoggetti(risposta,
					livelloPolo, SemanticaDAO.codPoloFromTicket(ticket),
					SemanticaDAO.codBibFromTicket(ticket));

			//almaviva5_20091030 calcolo max livello legame soggettario
			Map<String, String> maxLivelloAutSogg = lista.getMaxLivelloAutSogg();
			for (Object o : listaSogg) {
				ElementoSinteticaSoggettoVO e = (ElementoSinteticaSoggettoVO) o;
				String cdSogg = e.getCodiceSoggettario();
				String maxLivAut = maxLivelloAutSogg.get(cdSogg);
				String livAutElemento = e.getMaxLivAutLegame();
				if (maxLivAut == null || livAutElemento.compareTo(maxLivAut) > 0)
					maxLivelloAutSogg.put(cdSogg, livAutElemento);
			}


			lista.setListaSoggetti(listaSogg);
			return lista;

		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	public CatSemSoggettoVO ricercaSoggettiPerBidCollegato2(boolean livelloPolo,
			String bid, String codSoggettario, String ticket, int elementiBlocco)
			throws DAOException {
		//senza checkTicket(ticket) per batch bollettino nuove accessioni
		try {
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			//almaviva5_20130403 #5294
			SBNMarc risposta = server.ricercaSoggettiPerBidCollegato(livelloPolo, bid,
					codSoggettario, false, elementiBlocco);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			CatSemSoggettoVO lista = new CatSemSoggettoVO();

			SbnOutputType sbnOutput =
				risposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput();
			int totRighe = sbnOutput.getTotRighe();
			int maxRighe = sbnOutput.getMaxRighe();
			String idLista = sbnOutput.getIdLista();
			SbnResultType sbnResult =
				risposta.getSbnMessage().getSbnResponse().getSbnResult();
			lista.setEsito(sbnResult.getEsito());
			lista.setTestoEsito(sbnResult.getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			List listaSogg = server.creaListaSinteticaSoggetti(risposta,
					livelloPolo, SemanticaDAO.codPoloFromTicket(ticket),
					SemanticaDAO.codBibFromTicket(ticket));

			//almaviva5_20091030 calcolo max livello legame soggettario
			Map<String, String> maxLivelloAutSogg = lista.getMaxLivelloAutSogg();
			for (Object o : listaSogg) {
				ElementoSinteticaSoggettoVO e = (ElementoSinteticaSoggettoVO) o;
				String cdSogg = e.getCodiceSoggettario();
				String maxLivAut = maxLivelloAutSogg.get(cdSogg);
				String livAutElemento = e.getMaxLivAutLegame();
				if (maxLivAut == null || livAutElemento.compareTo(maxLivAut) > 0)
					maxLivelloAutSogg.put(cdSogg, livAutElemento);
			}


			lista.setListaSoggetti(listaSogg);
			return lista;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */

	public CatSemClassificazioneVO ricercaClassiPerBidCollegato(
			boolean livelloPolo, String Bid, String codClassificazione,
			String codEdizione, String ticket, int elementiBlocco)
			throws DAOException, ValidationException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.ricercaClassiPerBidCollegato(livelloPolo, Bid,
					codClassificazione, codEdizione, elementiBlocco);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			CatSemClassificazioneVO lista = new CatSemClassificazioneVO();

			int totRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			int maxRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			lista.setEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito());
			lista.setTestoEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			lista.setListaClassi(server.creaListaSinteticaClassi(risposta, livelloPolo));
			return lista;
		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaAbstractListaVO ricercaAbstractPerBidCollegato(
			RicercaAbstractVO datiRicerca, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnAbstractDAO server = new SbnAbstractDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.ricercaAbstractPerBidCollegato(datiRicerca);

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			RicercaAbstractListaVO lista = new RicercaAbstractListaVO();

			int totRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			int maxRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			lista.setEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito());
			lista.setTestoEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				// int numBlocchi = (totRighe / maxRighe) + 1;
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			lista.setRisultati(server.creaListaSinteticaAbstract(risposta));

			return lista;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */

	public RicercaClasseListaVO cercaClassi(RicercaClassiVO datiRicerca,
			String ticket) throws DAOException, ValidationException {
		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.cercaClassi(datiRicerca);

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			RicercaClasseListaVO lista = new RicercaClasseListaVO();

			SbnOutputType sbnOutput =
				risposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput();
			int totRighe = sbnOutput.getTotRighe();
			int maxRighe = sbnOutput.getMaxRighe();
			String idLista = sbnOutput.getIdLista();

			SbnResultType sbnResult = risposta.getSbnMessage().getSbnResponse().getSbnResult();
			lista.setEsito(sbnResult.getEsito());
			lista.setTestoEsito(sbnResult.getTestoEsito());
			if (totRighe > 0) {
				// int numBlocchi = (totRighe / maxRighe) + 1;
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			lista.setRisultati(server.creaListaSinteticaClassi(risposta, datiRicerca.isPolo()));

			return lista;

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaSoggettoListaVO ricercaSoggetti(
			RicercaComuneVO ricerca, String ticket) throws DAOException, ValidationException {

		try {
			checkTicket(ticket);

			ricerca.validate(new RicercaComuneValidator() );

			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );

			SBNMarc risposta = server.ricercaSoggetti(ricerca);

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			RicercaSoggettoListaVO lista = new RicercaSoggettoListaVO();

			SbnResponseType sbnResponse = risposta.getSbnMessage().getSbnResponse();
			SbnOutputType sbnOutput = sbnResponse.getSbnResponseTypeChoice().getSbnOutput();
			int totRighe = sbnOutput.getTotRighe();
			int maxRighe = sbnOutput.getMaxRighe();
			String idLista = sbnOutput.getIdLista();
			lista.setEsito(sbnResponse.getSbnResult().getEsito());
			lista.setTestoEsito(sbnResponse.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			lista.setRisultati(server.creaListaSinteticaSoggetti(risposta,
					ricerca.isPolo(), SemanticaDAO.codPoloFromTicket(ticket),
					SemanticaDAO.codBibFromTicket(ticket)));

			return lista;
		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	} // End ricercaSoggetti

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	@SuppressWarnings("unchecked")
	public RicercaSoggettoListaVO ricercaSoggettiPerDescrittore(
			RicercaComuneVO ricercaComune, String ticket) throws DataException, ValidationException {
		try {
			checkTicket(ticket);
			ricercaComune.validate(new RicercaComuneValidator() );
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );
			SBNMarc risposta = server.ricercaSoggettiPerDescrittore(ricercaComune);
			if (risposta == null)
				throw new DataException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			RicercaSoggettoListaVO lista = new RicercaSoggettoListaVO();

			int totRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			if (totRighe == 0) {
				lista.setEsito(risposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getEsito());
				lista.setTestoEsito(risposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getTestoEsito());

				return lista;
			}

			if (totRighe != 1) {
				if (controllaDuplicazioneDescrittore(server.creaListaSinteticaSoggetti(risposta,
						ricercaComune.isPolo(),
						SemanticaDAO.codPoloFromTicket(ticket),
						SemanticaDAO.codBibFromTicket(ticket)) ))
					throw new DataException(SbnErrorTypes.GS_DESCRITTORE_DUPLICATO);
				else
					throw new DataException(SbnErrorTypes.GS_DESCRITTORE_PRESENTE_ALTRI_SOGGETTARI);
			}


			if (totRighe != 1)
				throw new DataException("Incongruenza sulla Base Dati: trovato più di un descrittore");

			ElementoSinteticaDescrittoreVO descrittore = server
					.creaElementoListaDescrittori(risposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice()
							.getSbnOutput(), 0, 0, ricercaComune.isPolo(),
							SemanticaDAO.codPoloFromTicket(ticket),
							SemanticaDAO.codBibFromTicket(ticket));

			// se il did è in forma di rinvio uso il suo descrittore accettato
			String did = descrittore.isRinvio() ? descrittore.getDidFormaAccettata() : descrittore.getDid();
			if (did == null)
				throw new DataException("Il descrittore in forma di rinvio non possiede forma accettata e soggetti collegati");

			String edizione = ricercaComune.getEdizioneSoggettario();
			SbnEdizioneSoggettario _edizione = ValidazioneDati.isFilled(edizione) ?
				SbnEdizioneSoggettario.valueOf(edizione) :
				null;

			SBNMarc soggettiCollegati = server.ricercaSoggettiPerDidCollegato(
					ricercaComune.isPolo(), did, Integer.parseInt(ricercaComune
							.getElemBlocco()), ricercaComune
							.getRicercaPerUnDescrittore(),
					((RicercaSoggettoDescrittoriVO) ricercaComune
							.getRicercaSoggetto()).isUtilizzati(),
							ricercaComune.getOrdinamentoSoggetto(), _edizione);
			if (soggettiCollegati == null)
				throw new DataException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			totRighe = soggettiCollegati.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			int maxRighe = soggettiCollegati.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = soggettiCollegati.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			lista.setEsito(soggettiCollegati.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito());
			lista.setTestoEsito(soggettiCollegati.getSbnMessage()
					.getSbnResponse().getSbnResult().getTestoEsito());

			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}
			lista.setRisultati(server.creaListaSinteticaSoggetti(
					soggettiCollegati, ricercaComune.isPolo(), SemanticaDAO
							.codPoloFromTicket(ticket), SemanticaDAO
							.codBibFromTicket(ticket)));

			return lista;
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
			throw new DataException(e);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DataException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DataException(e);
		}

	} // End ricercaSoggettiPerDescrittore

	private boolean controllaDuplicazioneDescrittore(List<ElementoSinteticaDescrittoreVO> sintetica) {

		for (ElementoSinteticaDescrittoreVO descrittore : sintetica) {
			for (ElementoSinteticaDescrittoreVO descrittore2 : sintetica) {
				if (!descrittore.getDid().equals(descrittore2.getDid()) &&
						descrittore.getCodiceSoggettario().equals(descrittore2.getCodiceSoggettario())	)
					return true; // cod soggettario duplicato
			}
		}

		return false;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	@SuppressWarnings("unchecked")
	public SBNMarcCommonVO getNextBloccoSoggetti(SBNMarcCommonVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			SBNMarc risposta = server.getNextBloccoSoggetti(areaDatiPass);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			int numPrimo = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getNumPrimo();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			SBNMarcCommonVO areaDatiPassReturn;
			if (areaDatiPass instanceof RicercaSoggettoListaVO) {
				areaDatiPassReturn = new RicercaSoggettoListaVO();
				((RicercaSoggettoListaVO) areaDatiPassReturn)
						.setRisultati(server.creaListaSinteticaSoggetti(
								risposta, areaDatiPass.isLivelloPolo(),
								SemanticaDAO.codPoloFromTicket(ticket),
								SemanticaDAO.codBibFromTicket(ticket)));
			} else {
				areaDatiPassReturn = new CatSemSoggettoVO();
				((CatSemSoggettoVO) areaDatiPassReturn).setListaSoggetti(server
						.creaListaSinteticaSoggetti(risposta, areaDatiPass
								.isLivelloPolo(), SemanticaDAO
								.codPoloFromTicket(ticket), SemanticaDAO
								.codBibFromTicket(ticket)));
			}

			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setNumPrimo(numPrimo);
			areaDatiPassReturn.setNumNotizie(risposta.getSbnMessage()
					.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
					.getElementoAutCount());

			areaDatiPassReturn.setEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getEsito());
			areaDatiPassReturn.setTestoEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getTestoEsito());

			return areaDatiPassReturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaThesauroListaVO getNextBloccoTermini(
			RicercaThesauroListaVO areaDatiPass, String ticket)
			throws DAOException {
		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			SBNMarc risposta = server.getNextBloccoTermini(areaDatiPass);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			int numPrimo = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getNumPrimo();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			RicercaThesauroListaVO areaDatiPassReturn = new RicercaThesauroListaVO();
			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setNumPrimo(numPrimo);
			areaDatiPassReturn.setRisultati(server
					.creaListaSinteticaThesauro(risposta));
			areaDatiPassReturn.setEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getEsito());
			areaDatiPassReturn.setTestoEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getTestoEsito());
			areaDatiPassReturn.setNumNotizie(risposta.getSbnMessage()
					.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
					.getElementoAutCount());

			return areaDatiPassReturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaSoggettoListaVO getNextBloccoDescrittori(
			RicercaSoggettoListaVO areaDatiPass, String ticket)
			throws DAOException {
		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			SBNMarc risposta = server.getNextBloccoDescrittori(areaDatiPass);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			int numPrimo = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getNumPrimo();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			RicercaSoggettoListaVO areaDatiPassReturn = new RicercaSoggettoListaVO();
			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setNumPrimo(numPrimo);
			areaDatiPassReturn.setRisultati(server.creaListaSinteticaSoggetti(
					risposta, areaDatiPass.isLivelloPolo(), SemanticaDAO
							.codPoloFromTicket(ticket), SemanticaDAO
							.codBibFromTicket(ticket)));
			areaDatiPassReturn.setEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getEsito());
			areaDatiPassReturn.setTestoEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getTestoEsito());
			areaDatiPassReturn.setNumNotizie(risposta.getSbnMessage()
					.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
					.getElementoAutCount());

			return areaDatiPassReturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */

	public SBNMarcCommonVO getNextBloccoClassi(SBNMarcCommonVO areaDatiPass,
			String ticket) throws DAOException, ValidationException {
		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.getNextBloccoClassi(areaDatiPass);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			int numPrimo = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getNumPrimo();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			SBNMarcCommonVO areaDatiPassReturn;
			if (areaDatiPass instanceof RicercaClasseListaVO) {
				areaDatiPassReturn = new RicercaClasseListaVO();
				((RicercaClasseListaVO) areaDatiPassReturn).setRisultati(server
						.creaListaSinteticaClassi(risposta, areaDatiPass
								.isLivelloPolo()));
			} else {
				areaDatiPassReturn = new CatSemClassificazioneVO();
				((CatSemClassificazioneVO) areaDatiPassReturn)
						.setListaClassi(server.creaListaSinteticaClassi(
								risposta, areaDatiPass.isLivelloPolo()));
			}
			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setNumPrimo(numPrimo);
			areaDatiPassReturn.setEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getEsito());
			areaDatiPassReturn.setTestoEsito(risposta.getSbnMessage()
					.getSbnResponse().getSbnResult().getTestoEsito());
			areaDatiPassReturn.setNumNotizie(risposta.getSbnMessage()
					.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
					.getElementoAutCount());

			return areaDatiPassReturn;
		} catch (ValidationException e) {
			throw e;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	@SuppressWarnings("unchecked")
	public CreaVariaSoggettoVO creaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws DAOException, ValidationException {

		try {
			checkTicket(ticket);

			richiesta.validate(new CreaVariaSogDesValidator() );

			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaSoggettoVO newCid = new CreaVariaSoggettoVO();
			List<ElementoSinteticaSoggettoVO> listaSimili = ValidazioneDati.emptyList();

			SBNMarc risposta = server.creaSoggetto(richiesta);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			SbnMarcEsitoType esitoType = SbnMarcEsitoType.of(esito);

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newCid.setTotRighe(totRighe);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

				listaSimili = server.creaListaSinteticaSoggetti(risposta, richiesta.isLivelloPolo(),
						SemanticaDAO.codPoloFromTicket(ticket), SemanticaDAO.codBibFromTicket(ticket));

				newCid.setCid(datiElemento.getT001());
				if (esitoType == SbnMarcEsitoType.OK) {
					A100 t100 = datiElemento.getT100();
					//almaviva5_20120917 #5111 nel caso di cambio edizione l'indice invia esito OK ma con lista simili
					//quindi senza t001 e t005
					if (t100 != null) {
						String dataIns = SBNMarcUtil.converteDataSBN(t100.getA_100_0().toString());
						newCid.setDataInserimento(dataIns);
						newCid.setDataVariazione(SBNMarcUtil.converteDataVariazione(datiElemento.getT005()));
						newCid.setT005(datiElemento.getT005());
					} else {
						esito = SbnMarcEsitoType.TROVATO_SIMILE_EDIZIONE_DIVERSA.getEsito();
						//almaviva5_20160421 #6163
						newCid.setCambioEdizione(true);
						if (!ValidazioneDati.isFilled(listaSimili)) {
							//almaviva5_20180420 l'indice invia ora una lista vuota in caso di update dell'edizione del soggetto
							listaSimili = generaListaSoggettoComeSimile(ticket,	richiesta.isLivelloPolo(), richiesta.getCid());
						}
					}

				}

			} else {
				//soggetto non creato
				if (ValidazioneDati.in(esitoType,
						SbnMarcEsitoType.ID_ESISTENTE,
						SbnMarcEsitoType.SOGGETTO_GIA_MAPPATO) ) {
					//restituisce il cid duplicato
					listaSimili = generaListaSoggettoComeSimile(ticket,	richiesta.isLivelloPolo(), richiesta.getCid());
				}
			}

			newCid.setEsito(esito);
			newCid.setTestoEsito(testoEsito);
			newCid.setListaSimili(listaSimili);

			return newCid;

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	private List<ElementoSinteticaSoggettoVO> generaListaSoggettoComeSimile(String ticket, boolean livelloPolo,
			String cid) throws DAOException, InfrastructureException {
		AnaliticaSoggettoVO analitica = creaAnaliticaSoggettoPerCid(livelloPolo, cid, ticket, false);
		if (analitica.isEsitoOk()) {
			return ValidazioneDati.asList(new ElementoSinteticaSoggettoVO(analitica.getDettaglio()));
		}
		return ValidazioneDati.emptyList();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaSoggettoVO importaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws DAOException {

		try {
			checkTicket(ticket);

			richiesta.validate(new CreaVariaSogDesValidator() );

			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaSoggettoVO newCid = new CreaVariaSoggettoVO();

			SBNMarc risposta = server.importaSoggetto(richiesta);

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newCid.setTotRighe(totRighe);
			newCid.setEsito(esito);
			newCid.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				newCid.setCid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
							.getT100().getA_100_0().toString());
					newCid.setDataInserimento(dataIns);
					newCid.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					newCid.setT005(datiElemento.getT005());
				}
			}

			return newCid;
		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	@SuppressWarnings("unchecked")
	public CreaVariaSoggettoVO importaSoggettoConDescrittori(
			CreaVariaSoggettoVO richiesta, TreeElementViewSoggetti reticolo,
			String ticket) throws DAOException {

		try {
			checkTicket(ticket);

			richiesta.validate(new CreaVariaSogDesValidator() );

			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );
			CatturaReticoloSoggetto cattura = new CatturaReticoloSoggetto(server, getProfiler(), ticket);
			CreaVariaSoggettoVO newCid = new CreaVariaSoggettoVO();

			// SBNMarc risposta =
			// cattura.importExportReticoloSoggetto(richiesta, reticolo);
			SBNMarc risposta = cattura.eseguiCatturaReticoloSoggetto(reticolo, 2, richiesta, ticket);

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newCid.setTotRighe(totRighe);
			newCid.setEsito(esito);
			newCid.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				newCid.setCid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataIns = SBNMarcUtil.converteDataSBN(datiElemento.getT100().getA_100_0().toString());
					newCid.setDataInserimento(dataIns);
					newCid.setDataVariazione(SBNMarcUtil.converteDataVariazione(datiElemento.getT005()));
					newCid.setT005(datiElemento.getT005());

					// attivo condivisione tra polo e indice su questo cid
					DatiCondivisioneSoggettoVO datiCondivisione = new DatiCondivisioneSoggettoVO();
					datiCondivisione.setCidPolo(datiElemento.getT001());
					datiCondivisione.setCidIndice(datiElemento.getT001());
					datiCondivisione.setOrigineSoggetto(OrigineSoggetto.INDICE);
					datiCondivisione.setOrigineLegame(OrigineLegameTitoloSoggetto.NESSUNO);
					datiCondivisione.setOrigineModifica(OrigineModificaSoggetto.NESSUNO);
					Timestamp ts = DaoManager.now();
					String firmaUtente = SemanticaDAO.getFirmaUtente(ticket);
					datiCondivisione.setTsIns(ts);
					//datiCondivisione.setTsVar(ts);
					datiCondivisione.setUteIns(firmaUtente);
					datiCondivisione.setUteVar(firmaUtente);
					SemanticaDAO dao = new SemanticaDAO();

					List<DatiCondivisioneSoggettoVO> listaDatiCondivisione = new ArrayList<DatiCondivisioneSoggettoVO>();
					listaDatiCondivisione.add(datiCondivisione);
					dao.aggiornaDatiCondivisioneSoggetto(listaDatiCondivisione);

				}
			}

			if (ValidazioneDati.in(SbnMarcEsitoType.of(esito),
					SbnMarcEsitoType.TROVATI_SIMILI,
					SbnMarcEsitoType.TROVATO_SIMILE_EDIZIONE_DIVERSA) )
				newCid.setListaSimili(server.creaListaSinteticaSoggetti(
						risposta, richiesta.isLivelloPolo(), SemanticaDAO
								.codPoloFromTicket(ticket), SemanticaDAO
								.codBibFromTicket(ticket)));

			return newCid;

		} catch (InfrastructureException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (Exception e) {
			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaSoggettoVO catturaSoggetto(CreaVariaSoggettoVO richiesta,
			String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaSoggettoVO newCid = new CreaVariaSoggettoVO();

			SBNMarc risposta = server.importaSoggetto(richiesta);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newCid.setTotRighe(totRighe);
			newCid.setEsito(esito);
			newCid.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				newCid.setCid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
							.getT100().getA_100_0().toString());
					newCid.setDataInserimento(dataIns);
					newCid.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					newCid.setT005(datiElemento.getT005());
				}
			}

			return newCid;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	@SuppressWarnings("unchecked")
	public CreaVariaSoggettoVO variaSoggetto(CreaVariaSoggettoVO soggetto,
			String ticket) throws DAOException {

		try {
			checkTicket(ticket);

			soggetto.validate(new CreaVariaSogDesValidator() );

			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaSoggettoVO newCid = soggetto.copy(); //new CreaVariaSoggettoVO();

			SBNMarc risposta = server.variaSoggetto(soggetto);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newCid.setLivelloPolo(soggetto.isLivelloPolo());
			newCid.setTotRighe(totRighe);
			newCid.setEsito(esito);
			newCid.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

				newCid.setCid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					A100 t100 = datiElemento.getT100();
					if (t100 != null)
						newCid.setDataInserimento(SBNMarcUtil.converteDataSBN(t100.getA_100_0().toString()));
					String t005 = datiElemento.getT005();
					if (t005 != null) {
						newCid.setDataVariazione(SBNMarcUtil.converteDataVariazione(t005));
						newCid.setT005(t005);
					}
				}

				if (ValidazioneDati.in(SbnMarcEsitoType.of(esito),
						SbnMarcEsitoType.TROVATI_SIMILI,
						SbnMarcEsitoType.TROVATO_SIMILE_EDIZIONE_DIVERSA) )
					newCid.setListaSimili(server.creaListaSinteticaSoggetti(
							risposta, soggetto.isLivelloPolo(), SemanticaDAO
									.codPoloFromTicket(ticket), SemanticaDAO
									.codBibFromTicket(ticket)));
			}

			if (newCid.isEsitoOk()) {
				// aggiorno i dati di condivisione
				SemanticaDAO dao = new SemanticaDAO();
				List<DatiCondivisioneSoggettoVO> datiCondivisione = dao.getDatiCondivisioneSoggetto(newCid.getCid(), null, null);
				if (ValidazioneDati.isFilled(datiCondivisione) ) {
					Timestamp ts = DaoManager.now();
					String firmaUtente = SemanticaDAO.getFirmaUtente(ticket);
					for (DatiCondivisioneSoggettoVO condivisione : datiCondivisione) {
						condivisione.setOrigineModifica(OrigineModificaSoggetto.POLO);
						condivisione.setUteVar(firmaUtente);
						//condivisione.setTsVar(ts);
					}
					//dao.aggiornaDatiCondivisioneSoggetto(datiCondivisione);
				}
			}

			return newCid;

		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);

		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO variaDescrittore(
			CreaVariaDescrittoreVO descrittore, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaDescrittoreVO newDid = new CreaVariaDescrittoreVO();

			SBNMarc risposta = server.variaDescrittore(descrittore);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newDid.setTotRighe(totRighe);
			newDid.setEsito(esito);
			newDid.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				newDid.setDid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// newDid.setDataInserimento(dataIns);
					newDid.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					newDid.setT005(datiElemento.getT005());
				}
			}

			return newDid;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO creaLegameSoggettoDescrittore(
			String cidPadre, String categoriaSoggetto, String T005,
			String livello, String did, String note, boolean condiviso,
			boolean livelloPolo, String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );
			CreaVariaDescrittoreVO newDid = new CreaVariaDescrittoreVO();

			SBNMarc risposta = server.creaLegameSoggettoDescrittore(cidPadre,
					categoriaSoggetto, T005, livello, did, note, condiviso,
					livelloPolo);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newDid.setTotRighe(totRighe);
			newDid.setEsito(esito);
			newDid.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				newDid.setDid(did);
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
							.getT100().getA_100_0().toString());
					newDid.setDataInserimento(dataIns);
					newDid.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					newDid.setT005(datiElemento.getT005());
				}
			}

			return newDid;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloSoggettoVO creaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );
			DatiLegameTitoloSoggettoVO legameOut = new DatiLegameTitoloSoggettoVO();

			legame.setOperazione(TipoOperazioneLegame.CREA);
			SBNMarc risposta = server.gestioneLegameTitoloSoggetto(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice().getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					legameOut.setDataVariazione(SBNMarcUtil.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			if (legameOut.isEsitoOk() && legame.isLivelloPolo()	&& legame.isImporta()) { // condivisione legame
				SemanticaDAO dao = new SemanticaDAO();
				for (LegameTitoloAuthSemanticaVO lts : legame.getLegami()) {
					LegameTitoloSoggettoVO datiLegame = (LegameTitoloSoggettoVO) lts;
					List<DatiCondivisioneSoggettoVO> datiCondivisione = dao.getDatiCondivisioneSoggetto(datiLegame.getCid(), null, " ");
					if (!ValidazioneDati.isFilled(datiCondivisione) )
						continue;

					DatiCondivisioneSoggettoVO nuovaCondivisione = ValidazioneDati.first(datiCondivisione).copy();
					nuovaCondivisione.setBid(legame.getBid());

					String firmaUtente = SemanticaDAO.getFirmaUtente(ticket);
					nuovaCondivisione.setUteIns(firmaUtente);
					nuovaCondivisione.setUteVar(firmaUtente);
					Timestamp ts = DaoManager.now();
					nuovaCondivisione.setTsIns(ts);
					//nuovaCondivisione.setTsVar(ts);
					nuovaCondivisione.setOrigineLegame(OrigineLegameTitoloSoggetto.INDICE);
					dao.aggiornaDatiCondivisioneSoggetto(ValidazioneDati.asList(nuovaCondivisione) );
				}
			}

			return legameOut;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloSoggettoVO invioInIndiceLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			DatiLegameTitoloSoggettoVO legameOut = new DatiLegameTitoloSoggettoVO();

			legame.setOperazione(TipoOperazioneLegame.CREA);
			SBNMarc risposta = server.gestioneLegameTitoloSoggetto(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			if (legameOut.isEsitoOk() && !legame.isLivelloPolo()) { // condivisione
				// legame
				SemanticaDAO dao = new SemanticaDAO();
				for (LegameTitoloAuthSemanticaVO lts : legame.getLegami()) {
					LegameTitoloSoggettoVO datiLegame = (LegameTitoloSoggettoVO) lts;
					List<DatiCondivisioneSoggettoVO> datiCondivisione = dao.getDatiCondivisioneSoggetto(null, datiLegame.getCid(), " ");
					if (!ValidazioneDati.isFilled(datiCondivisione) )
						continue;

					DatiCondivisioneSoggettoVO nuovaCondivisione = ValidazioneDati.first(datiCondivisione).copy();
					nuovaCondivisione.setBid(legame.getBid());

					Timestamp ts = DaoManager.now();
					String firmaUtente = SemanticaDAO.getFirmaUtente(ticket);
					nuovaCondivisione.setUteIns(firmaUtente);
					nuovaCondivisione.setUteVar(firmaUtente);
					nuovaCondivisione.setTsIns(ts);
					//nuovaCondivisione.setTsVar(ts);
					nuovaCondivisione.setOrigineLegame(OrigineLegameTitoloSoggetto.POLO);
					dao.aggiornaDatiCondivisioneSoggetto(ValidazioneDati.asList(nuovaCondivisione) );
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloSoggettoVO modificaInvioInIndiceLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			DatiLegameTitoloSoggettoVO legameOut = new DatiLegameTitoloSoggettoVO();

			legame.setOperazione(TipoOperazioneLegame.MODIFICA);
			SBNMarc risposta = server.gestioneLegameTitoloSoggetto(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloSoggettoVO modificaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			DatiLegameTitoloSoggettoVO legameOut = new DatiLegameTitoloSoggettoVO();

			legame.setOperazione(TipoOperazioneLegame.MODIFICA);
			SBNMarc risposta = server.gestioneLegameTitoloSoggetto(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloSoggettoVO cancellaLegameTitoloSoggetto(
			DatiLegameTitoloSoggettoVO legame, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );

			DatiLegameTitoloSoggettoVO legameOut = new DatiLegameTitoloSoggettoVO();

			legame.setOperazione(TipoOperazioneLegame.CANCELLA);
			SBNMarc risposta = server.gestioneLegameTitoloSoggetto(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice().getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					legameOut.setDataVariazione(SBNMarcUtil.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			if (legameOut.isEsitoOk() && legame.isLivelloPolo()) { // cancello condivisione legame

				// preparo il VO per la cancellazione contestuale in indice
				DatiLegameTitoloSoggettoVO legameIndice = legame.copy();
				legameIndice.setLivelloPolo(false); // in indice
				legameIndice.setT005(null); // l'indice ricaricherà il timestamp
				List<LegameTitoloAuthSemanticaVO> legamiInIndice = legameIndice.getLegami();
				legamiInIndice.clear();

				SemanticaDAO dao = new SemanticaDAO();
				// cancello condivisioni in polo
				for (LegameTitoloAuthSemanticaVO lts : legame.getLegami()) {
					LegameTitoloSoggettoVO datiLegame = (LegameTitoloSoggettoVO) lts;
					List<DatiCondivisioneSoggettoVO> datiCondivisione =
							dao.getDatiCondivisioneSoggetto(datiLegame.getCid(), null, legame.getBid());
					if (!ValidazioneDati.isFilled(datiCondivisione) ) {
						continue;
					}

					DatiCondivisioneSoggettoVO condivisioneEliminata = ValidazioneDati.first(datiCondivisione).copy();
					condivisioneEliminata.setBid(legame.getBid());
					condivisioneEliminata.setFlCanc("S");
					String firmaUtente = SemanticaDAO.getFirmaUtente(ticket);
					condivisioneEliminata.setUteVar(firmaUtente);
					//condivisioneEliminata.setTsVar(DaoManager.now());

					dao.aggiornaDatiCondivisioneSoggetto(ValidazioneDati.asList(condivisioneEliminata));

					// preparo il legame per la cancellazione in indice (solo se esportato)
					//legamiInIndice.add(legameIndice.new LegameTitoloSoggettoVO(condivisioneEliminata.getCidIndice(), ""));
					legamiInIndice.add(legameIndice.new LegameTitoloSoggettoVO(condivisioneEliminata.getCidPolo(), ""));
				}

				// provo la cancellazione in indice
				if (ValidazioneDati.isFilled(legamiInIndice) ) {
					risposta = server.gestioneLegameTitoloSoggetto(legameIndice);
					if (risposta == null)
						throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
				}

			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO cancellaLegameSoggettoDescrittore(
			String cidPadre, String categoriaSoggetto, String T005,
			String livello, String did, String note, boolean condiviso,
			boolean livelloPolo, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaDescrittoreVO newDid = new CreaVariaDescrittoreVO();

			SBNMarc risposta = server.cancellaLegameSoggettoDescrittore(cidPadre,
					categoriaSoggetto, T005, livello, did, note, condiviso,
					livelloPolo);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newDid.setTotRighe(totRighe);
			newDid.setEsito(esito);
			newDid.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				newDid.setDid(did);
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
							.getT100().getA_100_0().toString());
					newDid.setDataInserimento(dataIns);
					newDid.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					newDid.setT005(datiElemento.getT005());
				}
			}

			return newDid;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO creaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaDescrittoreVO legameOut = new CreaVariaDescrittoreVO();

			SBNMarc risposta = server.creaLegameDescrittori(legame.getDidPartenza(),
					legame.getDidPartenzaFormaNome(), legame.getT005(), legame
							.getDidPartenzaLivelloAut(), legame.getDidArrivo(),
					legame.getTipoLegame(), legame.getNotaLegame(), legame
							.isCondiviso(), legame.isLivelloPolo());
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				legameOut.setDid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataVar = SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005());
					// String dataIns =
					// SBNMarcUtil.converteDataSBN(datiElemento.getT100().
					// getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(dataVar);
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO scambioFormaDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaDescrittoreVO legameOut = new CreaVariaDescrittoreVO();

			SBNMarc risposta = server.scambioFormaDescrittori(legame.getDidPartenza(),
					legame.getDidPartenzaFormaNome(), legame.getT005(), legame
							.getDidPartenzaLivelloAut(), legame.getDidArrivo(),
					legame.getTipoLegame(), legame.getNotaLegame(), legame
							.isCondiviso(), legame.isLivelloPolo());

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				legameOut.setDid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataVar = SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005());
					// String dataIns =
					// SBNMarcUtil.converteDataSBN(datiElemento.getT100().
					// getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(dataVar);
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO modificaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaDescrittoreVO legameOut = new CreaVariaDescrittoreVO();

			SBNMarc risposta = server.modificaLegameDescrittori(
					legame.getDidPartenza(), legame.getDidPartenzaFormaNome(),
					legame.getT005(), legame.getDidPartenzaLivelloAut(), legame
							.getDidArrivo(), legame.getTipoLegame(), legame
							.getNotaLegame(), legame.isCondiviso(), legame
							.isLivelloPolo());
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				legameOut.setDid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataVar = SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005());
					// String dataIns =
					// SBNMarcUtil.converteDataSBN(datiElemento.getT100().
					// getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(dataVar);
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO cancellaLegameDescrittori(
			DatiLegameDescrittoreVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			CreaVariaDescrittoreVO legameOut = new CreaVariaDescrittoreVO();

			SBNMarc risposta = server.cancellaLegameDescrittori(
					legame.getDidPartenza(), legame.getDidPartenzaFormaNome(),
					legame.getT005(), legame.getDidPartenzaLivelloAut(), legame
							.getDidArrivo(), legame.getTipoLegame(), legame
							.getNotaLegame(), legame.isCondiviso(), legame
							.isLivelloPolo());
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				legameOut.setDid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataVar = SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005());
					// String dataIns =
					// SBNMarcUtil.converteDataSBN(datiElemento.getT100().
					// getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(dataVar);
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaDescrittoreVO creaDescrittoreManuale(
			CreaVariaDescrittoreVO richiesta, String ticket)
			throws ValidationException, DAOException {

		try {
			checkTicket(ticket);
			richiesta.validate(new CreaVariaSogDesValidator() );
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );

			SBNMarc risposta = server.creaDescrittoreManuale(richiesta);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			DettaglioOggettoSemantico dettagli = new DettaglioOggettoSemantico(ticket);
			return dettagli.fillCreaVariaDescrittoreVO(risposta);

		} catch (ValidationException e) {
			throw e;

		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);

		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @param completo
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AnaliticaSoggettoVO creaAnaliticaSoggettoPerCid(boolean livelloPolo,
			String codCid, String ticket, boolean completo) throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );


			SBNMarc risposta = server.creaAnaliticaSoggettoPerCid(livelloPolo, codCid);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			AnaliticaSoggettoVO dati = new AnaliticaSoggettoVO();
			dati.setEsito(esito);
			dati.setTestoEsito(testoEsito);
			dati.setLivelloPolo(livelloPolo);

			if (!dati.isEsitoOk())
				return dati;

			ReticoloSoggetti reticolo = new ReticoloSoggetti(ticket);
			TreeElementViewSoggetti root = new TreeElementViewSoggetti();

			root.setLivelloPolo(livelloPolo);
			reticolo.setReticolo(risposta, root, completo);

			dati.setReticolo(root);
			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
			if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
				String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
						.getT100().getA_100_0().toString());
				dati.setDataInserimento(dataIns);
				dati.setDataVariazione(SBNMarcUtil
						.converteDataVariazione(datiElemento.getT005()));
				dati.setT005(datiElemento.getT005());
			}

			return dati;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @param completo
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AnaliticaSoggettoVO creaAnaliticaSoggettoPerDid(boolean livelloPolo,
			String codDid, int startId, String ticket, boolean completo) throws DAOException {
		// TODO Auto-generated method stub
		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );


			SBNMarc risposta = server.creaAnaliticaSoggettoPerDid(livelloPolo, codDid);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			AnaliticaSoggettoVO dati = new AnaliticaSoggettoVO();
			dati.setEsito(esito);
			dati.setTestoEsito(testoEsito);
			dati.setLivelloPolo(livelloPolo);

			if (!dati.isEsitoOk())
				return dati;

			ReticoloDescrittori reticolo = new ReticoloDescrittori(ticket);
			TreeElementViewSoggetti root = new TreeElementViewSoggetti();
			root.setLivelloPolo(livelloPolo);
			reticolo.setReticolo(risposta, root, startId, completo);

			dati.setReticolo(root);
			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
			if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
				String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
						.getT100().getA_100_0().toString());
				dati.setDataInserimento(dataIns);
				dati.setDataVariazione(SBNMarcUtil
						.converteDataVariazione(datiElemento.getT005()));
				dati.setT005(datiElemento.getT005());
			} else
				return dati;

			// almaviva5_20080212
			if (root.hasChildren())
				caricaSottoReticoliDescrittori(server, dati, reticolo, root, completo);
			else
				root.setPlusVisible(false);

			return dati;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	private void caricaSottoReticoliDescrittori(SbnSoggettiDAO server,
			AnaliticaSoggettoVO dati, ReticoloDescrittori reticolo,
			TreeElementViewSoggetti root, boolean completo) throws DAOException,
			DaoManagerException {

		List<TreeElementViewSoggetti> tmpChildren = new ArrayList<TreeElementViewSoggetti>();
		List<TreeElementView> children = root.getChildren();
		for (TreeElementView sottoNodo : children) {

			String tipoLegame = ((TreeElementViewSoggetti) sottoNodo)
					.getDatiLegame().toString();
			if (tipoLegame.equals("UF") || tipoLegame.equals("USE")) {
				tmpChildren.add((TreeElementViewSoggetti) sottoNodo);
				continue;
			}

			SBNMarc risposta = server.creaAnaliticaSoggettoPerDid(root
					.isLivelloPolo(), sottoNodo.getKey());
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			if (SbnMarcEsitoType.of(esito) != SbnMarcEsitoType.OK) {
				dati.setEsito(esito);
				dati.setTestoEsito(testoEsito);
				return;
			}

			TreeElementViewSoggetti tmpRoot = new TreeElementViewSoggetti();
			tmpRoot.setLivelloPolo(root.isLivelloPolo());
			reticolo.setReticolo(risposta, tmpRoot, completo);
			tmpChildren.add(tmpRoot);
		}

		if (tmpChildren.size() > 0)
			for (int child = 0; child < children.size(); child++) {
				TreeElementViewSoggetti tmp = (TreeElementViewSoggetti) children
						.get(child);
				tmp.adoptChildren(tmpChildren.get(child));
				// tmp.setExplored(true);
			}

		root.open();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @param authority
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaSoggettoDescrittore(boolean livelloPolo, String xid,	String ticket, SbnAuthority authority) throws DAOException {

		boolean result = false;
		String testoEsito;

		try {
			checkTicket(ticket);

			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );


			SBNMarc risposta = server.cancellaSoggettoDescrittore(livelloPolo, xid, authority);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			testoEsito = sbnResult.getTestoEsito();

			result = (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK);

			if (result && livelloPolo) { // condivisione legame
				SemanticaDAO dao = new SemanticaDAO();
				List<DatiCondivisioneSoggettoVO> datiCondivisione = dao.getDatiCondivisioneSoggetto(xid, null, " ");
				if (ValidazioneDati.isFilled(datiCondivisione)) {
					DatiCondivisioneSoggettoVO dcs = ValidazioneDati.first(datiCondivisione);
					//server.cancellaSoggettoDescrittore(false, dcs.getCidIndice(), SbnAuthority.SO);
					server.cancellaSoggettoDescrittore(false, dcs.getCidPolo(), SbnAuthority.SO);

					dcs.setFlCanc("S");	// cancella condivisione
					dao.aggiornaDatiCondivisioneSoggetto(datiCondivisione);
				}
			}

		} catch (Exception e) {
			throw new DAOException(e);
		}

		if (!result) {
			throw new DAOException(testoEsito);
		}

		return result;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AreaDatiAccorpamentoReturnVO trascinaTitoliTraSoggetti(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );
			AreaDatiAccorpamentoReturnVO areareturn = new AreaDatiAccorpamentoReturnVO();

			SBNMarc risposta = server.trascinaTitoliTraSoggetti(livelloPolo, area);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			areareturn.setCodiceRitorno(esito);
			areareturn.setIdErrore(testoEsito);
			areareturn.setIdOk(esito);

			return areareturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AreaDatiAccorpamentoReturnVO fondiSoggetti(boolean livelloPolo,
			AreaDatiAccorpamentoVO area, String ticket) throws DAOException {

		try {
			checkTicket(ticket);

			FondiSoggettoVO fs = (FondiSoggettoVO) area;
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );
			AreaDatiAccorpamentoReturnVO areareturn = new AreaDatiAccorpamentoReturnVO();

			SBNMarc risposta = server.fondiSoggetti(livelloPolo, fs);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			areareturn.setCodiceRitorno(esito);
			areareturn.setIdErrore(testoEsito);
			areareturn.setIdOk(esito);

			if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
				// devo aggiornare le condivisioni esistenti
				SemanticaDAO dao = new SemanticaDAO();
				String cidPartenza = fs.getIdElementoEliminato();
				List<DatiCondivisioneSoggettoVO> datiCondivisione = dao.getDatiCondivisioneSoggetto(cidPartenza, null, null);

				if (ValidazioneDati.isFilled(datiCondivisione) ) {
					// cancello tutte le righe sul vecchio cid
					Timestamp ts = DaoManager.now();
					String firmaUtente = SemanticaDAO.getFirmaUtente(ticket);
					List<DatiCondivisioneSoggettoVO> datiCondivisioneNew = new ArrayList<DatiCondivisioneSoggettoVO>();
					for (DatiCondivisioneSoggettoVO vecchiaCondivisione : datiCondivisione) {

						DatiCondivisioneSoggettoVO nuovaCondivisione = vecchiaCondivisione.copy();
						// aggiungo righe per nuovo cid
						nuovaCondivisione.setCidPolo(fs.getIdElementoAccorpante());
						nuovaCondivisione.setUteIns(firmaUtente);
						vecchiaCondivisione.setUteVar(firmaUtente);
						nuovaCondivisione.setTsIns(ts);
						//vecchiaCondivisione.setTsVar(ts);
						nuovaCondivisione.setFlCanc("N");

						datiCondivisioneNew.add(nuovaCondivisione);

						// cancello vecchia condivisione
						vecchiaCondivisione.setOrigineModifica(OrigineModificaSoggetto.POLO);
						vecchiaCondivisione.setUteVar(firmaUtente);
						//vecchiaCondivisione.setTsVar(ts);
						vecchiaCondivisione.setFlCanc("S");
					}
					datiCondivisione.addAll(datiCondivisioneNew);
					dao.aggiornaDatiCondivisioneSoggetto(datiCondivisione);
				}

				//almaviva5_20120419 evolutive CFI
				String edizione = fs.getEdizioneSoggettoAccorpante();
				if (ValidazioneDati.isFilled(edizione)) {
					AnaliticaSoggettoVO analitica = creaAnaliticaSoggettoPerCid(livelloPolo, fs.getIdElementoAccorpante(), ticket, false);
					if (analitica.getEsitoType() == SbnMarcEsitoType.OK) {

						CreaVariaSoggettoVO soggetto = new CreaVariaSoggettoVO(analitica.getDettaglio());
						soggetto.setEdizioneSoggettario(edizione); //entrambe
						variaSoggetto(soggetto, ticket);
					}

				}

			}

			return areareturn;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AreaDatiAccorpamentoReturnVO richiestaAccorpamentoSoggetti(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );
			AreaDatiAccorpamentoReturnVO areareturn = new AreaDatiAccorpamentoReturnVO();

			SBNMarc risposta = server.richiestaAccorpamentoSoggetti(livelloPolo, area);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			areareturn.setCodiceRitorno(esito);
			areareturn.setIdErrore(testoEsito);
			areareturn.setIdOk(esito);

			return areareturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AreaDatiAccorpamentoReturnVO richiestaAccorpamentoClassi(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws DAOException {
		try {
			checkTicket(ticket);
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket) );
			AreaDatiAccorpamentoReturnVO areareturn = new AreaDatiAccorpamentoReturnVO();

			SBNMarc risposta = server.richiestaAccorpamentoSoggetti(livelloPolo, area);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			areareturn.setCodiceRitorno(esito);
			areareturn.setIdErrore(testoEsito);
			areareturn.setIdOk(esito);

			return areareturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaClasseVO analiticaClasse(boolean livelloPolo,
			String simbolo, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			CreaVariaClasseVO classe = new CreaVariaClasseVO();

			SBNMarc risposta = server.analiticaClasse(livelloPolo, simbolo);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			classe.setTotRighe(totRighe);
			classe.setEsito(esito);
			classe.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				ClasseType datiElemento = (ClasseType) elementoAut
						.getDatiElementoAut();

				if (SbnMarcEsitoType.of(esito) != SbnMarcEsitoType.OK) {
					//throw new DAOException(testoEsito);
					return classe;
				}

				classe.setT001(datiElemento.getT001());
				classe.setT005(datiElemento.getT005());
				classe.setLivello(datiElemento.getLivelloAut().toString());
				classe.setLivelloPolo(livelloPolo);
				if (livelloPolo) {
					classe.setCondiviso(datiElemento.getCondiviso() == DatiElementoTypeCondivisoType.S);
					//almaviva5_20090401 #2780
					classe.setUlterioreTermine(datiElemento.getUltTermine());
				}

				String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
						.getT100().getA_100_0().toString());
				classe.setDataInserimento(dataIns);
				classe.setDataVariazione(SBNMarcUtil
						.converteDataVariazione(datiElemento.getT005()));

				A676 a676 = datiElemento.getClasseTypeChoice().getT676();
				if (a676 != null) {
					// dewey
					// classe.setCodSistemaClassificazione(a676.getA_676());
					classe.setCodSistemaClassificazione("D");
					classe.setSimbolo(a676.getA_676());
					classe.setCodEdizioneDewey(a676.getV_676().toString());
					classe.setDescrizione(a676.getC_676());
					classe.setCostruito(a676.getC9_676() == SbnIndicatore.S);

				} else {
					// non dewey
					A686 a686 = datiElemento.getClasseTypeChoice().getT686();
					classe.setCodSistemaClassificazione(a686.getC2_686());
					classe.setSimbolo(a686.getA_686());
					classe.setDescrizione(a686.getC_686());
					classe.setCodEdizioneDewey("");
				}

				if (livelloPolo) {
					classe.setNumTitoliPolo(datiElemento.getNum_tit_coll());
					classe.setNumTitoliBiblio(datiElemento.getNum_tit_coll_bib());
				}
			}
			return classe;

		} catch (RemoteException e) {
			log.error("", e.detail);
			throw new DAOException((Exception)e.detail);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaClasseVO creaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws DAOException, ValidationException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			CreaVariaClasseVO newSimbolo = new CreaVariaClasseVO();

			SBNMarc risposta = server.creaClasse(richiesta
					.getCodSistemaClassificazione(), richiesta
					.getCodEdizioneDewey(), richiesta.getDescrizione(),
					richiesta.getLivello(), richiesta.getUlterioreTermine(),
					richiesta.getSimbolo(), richiesta.isCostruito(),
					richiesta.isLivelloPolo(), richiesta.isForzaCreazione() );
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newSimbolo.setTotRighe(totRighe);
			newSimbolo.setEsito(esito);
			newSimbolo.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				ClasseType datiElemento = (ClasseType) elementoAut
						.getDatiElementoAut();

				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {

					//String dataIns = SBNMarcUtil.converteDataSBN(datiElemento.getT100().getA_100_0().toString());
					String dataAgg = SBNMarcUtil.converteDataVariazione(datiElemento.getT005());

					newSimbolo.setT001(datiElemento.getT001());
					newSimbolo.setDataInserimento(dataAgg);
					newSimbolo.setDataVariazione(dataAgg);
					newSimbolo.setT005(datiElemento.getT005());
					if (richiesta.isLivelloPolo())
						newSimbolo.setCondiviso(datiElemento.getCondiviso() == DatiElementoTypeCondivisoType.S);
				}

				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.TROVATI_SIMILI) // trovato simile
					newSimbolo.setListaSimili(server.creaListaSinteticaClassi(risposta, richiesta.isLivelloPolo()));
			}

			return newSimbolo;
		} catch (ValidationException e) {
			throw e;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaClasseVO importaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			CreaVariaClasseVO newSimbolo = new CreaVariaClasseVO();

			SBNMarc risposta = server.importaClasse(richiesta
					.getCodSistemaClassificazione(), richiesta
					.getCodEdizioneDewey(), richiesta.getDescrizione(),
					richiesta.getLivello(), richiesta.getUlterioreTermine(),
					richiesta.getSimbolo(), richiesta.isCostruito(),
					richiesta.isLivelloPolo());
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newSimbolo.setTotRighe(totRighe);
			newSimbolo.setEsito(esito);
			newSimbolo.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				ClasseType datiElemento = (ClasseType) elementoAut
						.getDatiElementoAut();

				if (SbnMarcEsitoType.of(esito) != SbnMarcEsitoType.OK)
					throw new DAOException(testoEsito);

				String dataIns = SBNMarcUtil.converteDataVariazione(datiElemento.getT005());
				newSimbolo.setT001(datiElemento.getT001());
				newSimbolo.setDataInserimento(dataIns);
				newSimbolo.setDataVariazione(dataIns);
				newSimbolo.setT005(datiElemento.getT005());
			}

			return newSimbolo;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaClasseVO catturaClasse(CreaVariaClasseVO richiesta,
			String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			CreaVariaClasseVO newSimbolo = new CreaVariaClasseVO();

			SBNMarc risposta = server.catturaClasse(richiesta
					.getCodSistemaClassificazione(), richiesta
					.getCodEdizioneDewey(), richiesta.getDescrizione(),
					richiesta.getLivello(), richiesta.getUlterioreTermine(),
					richiesta.getSimbolo(), richiesta.isCostruito(),
					richiesta.isLivelloPolo());
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newSimbolo.setTotRighe(totRighe);
			newSimbolo.setEsito(esito);
			newSimbolo.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				ClasseType datiElemento = (ClasseType) elementoAut
						.getDatiElementoAut();

				if (SbnMarcEsitoType.of(esito) != SbnMarcEsitoType.OK) {
					throw new DAOException(testoEsito);
				}

				String dataIns = SBNMarcUtil
						.converteDataVariazione(datiElemento.getT005());
				newSimbolo.setT001(datiElemento.getT001());
				newSimbolo.setDataInserimento(dataIns);
				newSimbolo.setDataVariazione(dataIns);
				newSimbolo.setT005(datiElemento.getT005());
			}

			return newSimbolo;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaClasseVO variaClasse(CreaVariaClasseVO classe, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));
			CreaVariaClasseVO classeVariata = (CreaVariaClasseVO) classe
					.clone();

			SBNMarc risposta = server.variaClasse(classe);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			classeVariata.setEsito(esito);
			classeVariata.setTestoEsito(testoEsito);

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				ClasseType datiElemento = (ClasseType) elementoAut
						.getDatiElementoAut();

				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {

					classeVariata.setTotRighe(totRighe);
					classeVariata.setDataVariazione(SBNMarcUtil.converteDataVariazione(datiElemento.getT005()));
					classeVariata.setT005(datiElemento.getT005());

					classeVariata.setT001(datiElemento.getT001());
					if (classe.isLivelloPolo()) {
						classeVariata.setCondiviso(datiElemento.getCondiviso() == DatiElementoTypeCondivisoType.S);
						classe.setNumTitoliPolo(datiElemento.getNum_tit_coll());
						classe.setNumTitoliBiblio(datiElemento.getNum_tit_coll_bib());
					}
				}
			}

			return classeVariata;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AreaDatiAccorpamentoReturnVO trascinaTitoliTraClassi(
			boolean livelloPolo, AreaDatiAccorpamentoVO area, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));
			AreaDatiAccorpamentoReturnVO areareturn = new AreaDatiAccorpamentoReturnVO();

			SBNMarc risposta = server.trascinaTitoliTraClassi(livelloPolo, area);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			areareturn.setCodiceRitorno(esito);
			areareturn.setIdErrore(testoEsito);
			areareturn.setIdOk(esito);

			return areareturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaClasse(boolean livelloPolo, String xid, String ticket)
			throws DAOException {

		boolean result = false;
		String testoEsito;

		try {
			checkTicket(ticket);

			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.cancellaClasse(livelloPolo, xid);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			testoEsito = sbnResult.getTestoEsito();

			result = (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK);

		} catch (Exception e) {
			throw new DAOException(e);
		}

		if (!result) {
			throw new DAOException(testoEsito);
		}

		return result;

	}

	// THESAURO
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaThesauroListaVO ricercaThesauro(
			ThRicercaComuneVO ricercaComune, String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			if (ricercaComune.getOperazione() instanceof ThRicercaTitoliCollegatiVO)
				return ricercaTitoliCollegatiTermini(server, ricercaComune);

			SBNMarc risposta = server.ricercaThesauro(ricercaComune);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			RicercaThesauroListaVO lista = new RicercaThesauroListaVO();

			int totRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			int maxRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			lista.setEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito());
			lista.setTestoEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}

			lista.setRisultati(server.creaListaSinteticaThesauro(risposta));
			return lista;

		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	} // End ricercaThesauro

	private RicercaThesauroListaVO ricercaTitoliCollegatiTermini(
			SbnThesauriDAO server, ThRicercaComuneVO ricercaComune)
			throws DAOException, DaoManagerException {

		SBNMarc risposta = null;
		RicercaThesauroListaVO lista = new RicercaThesauroListaVO();
		SemanticaDAO dao = new SemanticaDAO();

		ThRicercaTitoliCollegatiVO parametri = ricercaComune
				.getRicercaTitoliCollegati();
		ricercaComune.setRicercaTitoliCollegati(null);
		ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_ESATTA);
		String[] termini = parametri.getTermini();

		int contaNonTrovati = 0;
		List<ElementoSinteticaThesauroVO> listaTermini = new ArrayList<ElementoSinteticaThesauroVO>();
		for (String termine : termini) {

			ThRicercaDescrittoreVO ricerca = new ThRicercaDescrittoreVO();
			ricerca.setTestoDescr(termine);
			ricercaComune.setRicercaThesauroDescrittore(ricerca);
			risposta = server.ricercaThesauro(ricercaComune);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			String esito = risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito();
			if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.NON_TROVATO) { // non trovato
				ElementoSinteticaThesauroVO didNonTrovato = new ElementoSinteticaThesauroVO();
				didNonTrovato.setCodThesauro(VALORE_NULLO);
				didNonTrovato.setDid(VALORE_NULLO);
				didNonTrovato.setLivelloAutorita(VALORE_NULLO);
				didNonTrovato.setTermine(termine);
				listaTermini.add(didNonTrovato);
				contaNonTrovati++;
				continue;
			}

			if (SbnMarcEsitoType.of(esito) != SbnMarcEsitoType.OK)
				continue;

			SbnOutputType sbnOutput = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput();
			ElementoSinteticaThesauroVO didTrovato = server
					.creaElementoListaThesauro(sbnOutput, 0, 1);

			didTrovato.setNumTerminiCollegati(dao
					.countTerminiCollegati(didTrovato.getDid()));
			listaTermini.add(didTrovato);

		}

		lista.setIdLista(null);
		lista.setNumBlocco(1);
		lista.setTotBlocchi(1);

		int size = ValidazioneDati.size(listaTermini);
		if (contaNonTrovati != size) {

			lista.setEsito(SbnMarcEsitoType.OK.getEsito());
			lista.setTestoEsito("OK");
			lista.setMaxRighe(size);
			lista.setTotRighe(size);
			lista.setNumNotizie(size);
			lista.setRisultati(listaTermini);

		} else {
			lista.setEsito(SbnMarcEsitoType.NON_TROVATO.getEsito()); // non ho trovato niente
			lista.setTestoEsito("Nessun elemento trovato");
			lista.setMaxRighe(0);
			lista.setTotRighe(0);
			lista.setNumNotizie(0);
			lista.setRisultati(null);
		}

		return lista;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CatSemThesauroVO ricercaTerminiPerBidCollegato(boolean livelloPolo,
			String bid, String codThesauro, String ticket, int elementiBlocco)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			SBNMarc risposta = server.ricercaTerminiPerBidCollegato(livelloPolo, bid,
					codThesauro, elementiBlocco);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			CatSemThesauroVO lista = new CatSemThesauroVO();

			SbnResponseType sbnResponse = risposta.getSbnMessage().getSbnResponse();
			SbnOutputType sbnOutput = sbnResponse.getSbnResponseTypeChoice().getSbnOutput();
			int totRighe = sbnOutput.getTotRighe();
			int maxRighe = sbnOutput.getMaxRighe();
			String idLista = sbnOutput.getIdLista();
			lista.setEsito(sbnResponse.getSbnResult().getEsito());
			lista.setTestoEsito(sbnResponse.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
				lista.getBlocchiCaricati().add(1); // primo blocco già caricato
			}
			lista.setListaTermini(server.creaListaSinteticaThesauro(risposta));

			return lista;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public RicercaThesauroListaVO ricercaTerminiPerDidCollegato(
			boolean livelloPolo, String ticket, String did, int elementiBlocco)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			SBNMarc risposta = server.ricercaTerminiPerDidCollegato(livelloPolo, did,
					elementiBlocco);

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			RicercaThesauroListaVO lista = new RicercaThesauroListaVO();

			int totRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			int maxRighe = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = risposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			lista.setEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito());
			lista.setTestoEsito(risposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));

				lista.setIdLista(idLista);
				lista.setMaxRighe(maxRighe);
				lista.setTotRighe(totRighe);
				lista.setNumBlocco(1);
				lista.setNumNotizie(totRighe);
				lista.setTotBlocchi(numBlocchi);
			}

			lista.setRisultati(server.creaListaSinteticaThesauro(risposta));
			return lista;

		} catch (RemoteException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			log.error("", e);
			throw new DAOException(e);
		}

	} // End ricercaTerminiPerDidCollegato

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaTermineVO gestioneTermineThesauro(
			CreaVariaTermineVO termine, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			SBNMarc risposta = server.gestioneTermineThesauro(termine);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			DettaglioOggettoSemantico dettagli = new DettaglioOggettoSemantico(ticket);
			CreaVariaTermineVO termineVariato = dettagli
					.fillCreaVariaTermineVO(risposta);
			if (termineVariato.isEsitoTrovatiSimili())
				termineVariato.setListaSimili(server
						.creaListaSinteticaThesauro(risposta));

			return termineVariato;

		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaTermineVO gestioneLegameTermini(
			DatiLegameTerminiVO legameDaCreare, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			CreaVariaTermineVO legameOut = new CreaVariaTermineVO();

			SbnTipoOperazione sbnTipoOperazione = null;
			switch (legameDaCreare.getOperazione()) {
			case CREA:
				sbnTipoOperazione = SbnTipoOperazione.CREA;
				break;
			case MODIFICA:
				sbnTipoOperazione = SbnTipoOperazione.MODIFICA;
				break;
			case CANCELLA:
				sbnTipoOperazione = SbnTipoOperazione.CANCELLA;
				break;
			case SCAMBIA:
				sbnTipoOperazione = SbnTipoOperazione.SCAMBIOFORMA;
				break;
			}

			SBNMarc risposta = server.gestioneLegameTermini(legameDaCreare,
					sbnTipoOperazione);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				legameOut.setDid(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					String dataVar = SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005());
					// String dataIns =
					// SBNMarcUtil.converteDataSBN(datiElemento.getT100().
					// getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(dataVar);
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AreaDatiAccorpamentoReturnVO fondiTerminiThesauro(
			boolean livelloPolo, DatiFusioneTerminiVO datiFusione, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));
			AreaDatiAccorpamentoReturnVO areareturn = new AreaDatiAccorpamentoReturnVO();

			SBNMarc risposta = server.fondiTerminiThesauro(livelloPolo, datiFusione);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			areareturn.setCodiceRitorno(esito);
			areareturn.setIdErrore(testoEsito);
			areareturn.setIdOk(esito);

			return areareturn;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public AnaliticaThesauroVO creaAnaliticaThesauroPerDid(boolean livelloPolo,
			String did, int startIdx, String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.creaAnaliticaThesauroPerDid(livelloPolo, did);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			AnaliticaThesauroVO dati = new AnaliticaThesauroVO();
			dati.setEsito(esito);
			dati.setTestoEsito(testoEsito);
			dati.setLivelloPolo(livelloPolo);

			if (!dati.isEsitoOk())
				return dati;

			TreeElementViewSoggetti root = new TreeElementViewSoggetti();
			root.setLivelloPolo(livelloPolo);
			ReticoloThesauro reticolo = new ReticoloThesauro(ticket);
			reticolo.setReticolo(risposta, root);

			dati.setReticolo(root);
			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
			if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
				String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
						.getT100().getA_100_0().toString());
				dati.setDataInserimento(dataIns);
				dati.setDataVariazione(SBNMarcUtil
						.converteDataVariazione(datiElemento.getT005()));
				dati.setT005(datiElemento.getT005());
			} else
				return dati;

			// almaviva5_20080212
			if (root.hasChildren())
				caricaSottoReticoliThesauro(server, dati, reticolo, root);
			else
				root.setPlusVisible(false);

			return dati;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	private void caricaSottoReticoliThesauro(SbnThesauriDAO server,
			AnaliticaThesauroVO dati, ReticoloThesauro reticolo,
			TreeElementViewSoggetti root) throws DAOException {

		List<TreeElementViewSoggetti> tmpChildren = new ArrayList<TreeElementViewSoggetti>();
		List<TreeElementView> children = root.getChildren();
		for (TreeElementView sottoNodo : children) {

			String tipoLegame = ((TreeElementViewSoggetti) sottoNodo)
					.getDatiLegame().toString();
			if (tipoLegame.equals("UF") || tipoLegame.equals("USE")) {
				tmpChildren.add((TreeElementViewSoggetti) sottoNodo);
				continue;
			}

			SBNMarc risposta = server.creaAnaliticaThesauroPerDid(root
					.isLivelloPolo(), sottoNodo.getKey());
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();
			if (SbnMarcEsitoType.of(esito) != SbnMarcEsitoType.OK) {
				dati.setEsito(esito);
				dati.setTestoEsito(testoEsito);
				return;
			}

			TreeElementViewSoggetti tmpRoot = new TreeElementViewSoggetti();
			tmpRoot.setLivelloPolo(root.isLivelloPolo());
			reticolo.setReticolo(risposta, tmpRoot);
			tmpChildren.add(tmpRoot);
		}

		if (tmpChildren.size() > 0)
			for (int child = 0; child < children.size(); child++) {
				TreeElementViewSoggetti tmp = (TreeElementViewSoggetti) children
						.get(child);
				tmp.adoptChildren(tmpChildren.get(child));
				// tmp.setExplored(true);
			}

		root.open();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public SBNMarcCommonVO gestioneLegameTitoloTermini(
			DatiLegameTitoloTermineVO legame, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));
			DatiLegameTitoloTermineVO legameOut = (DatiLegameTitoloTermineVO) legame
					.clone();

			SBNMarc risposta = server.gestioneLegameTitoloTerminiClassi(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloClasseVO creaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			DatiLegameTitoloClasseVO legameOut = new DatiLegameTitoloClasseVO();

			legame.setOperazione(TipoOperazioneLegame.CREA);
			SBNMarc risposta = server.gestioneLegameTitoloClasse(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloClasseVO invioInIndiceLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			DatiLegameTitoloClasseVO legameOut = new DatiLegameTitoloClasseVO();

			legame.setOperazione(TipoOperazioneLegame.CREA);
			SBNMarc risposta = server.gestioneLegameTitoloClasse(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloClasseVO modificaInvioInIndiceLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			DatiLegameTitoloClasseVO legameOut = new DatiLegameTitoloClasseVO();

			legame.setOperazione(TipoOperazioneLegame.MODIFICA);
			SBNMarc risposta = server.gestioneLegameTitoloClasse(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloClasseVO modificaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			DatiLegameTitoloClasseVO legameOut = new DatiLegameTitoloClasseVO();

			legame.setOperazione(TipoOperazioneLegame.MODIFICA);
			SBNMarc risposta = server.gestioneLegameTitoloClasse(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public DatiLegameTitoloClasseVO cancellaLegameTitoloClasse(
			DatiLegameTitoloClasseVO legame, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			DatiLegameTitoloClasseVO legameOut = new DatiLegameTitoloClasseVO();

			legame.setOperazione(TipoOperazioneLegame.CANCELLA);
			SBNMarc risposta = server.gestioneLegameTitoloClasse(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);
			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getDocumentoCount() > 0) {
				DocumentoType elementoDoc = sbnOutPut.getDocumento(0);
				DatiDocType datiElemento = elementoDoc.getDocumentoTypeChoice()
						.getDatiDocumento();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					// String dataIns = SBNMarcUtil.converteDataSBN(datiElemento
					// .getT100().getA_100_0().toString());
					// legameOut.setDataInserimento(dataIns);
					legameOut.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return legameOut;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaAbstractVO creaLegameTitoloAbstract(
			CreaVariaAbstractVO richiesta, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnAbstractDAO server = new SbnAbstractDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			CreaVariaAbstractVO newAbstract = new CreaVariaAbstractVO();

			SBNMarc risposta = server.creaLegameTitoloAbstract(richiesta);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newAbstract.setTotRighe(totRighe);
			newAbstract.setEsito(esito);
			newAbstract.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {

				if (SbnMarcEsitoType.of(esito) != SbnMarcEsitoType.OK) {
					throw new DAOException(testoEsito);
				}

			}

			return newAbstract;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public CreaVariaAbstractVO variaLegameTitoloAbstract(
			CreaVariaAbstractVO abstracto, String ticket) throws DAOException {

		try {
			checkTicket(ticket);
			SbnAbstractDAO server = new SbnAbstractDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));

			CreaVariaAbstractVO newAbstract = new CreaVariaAbstractVO();

			SBNMarc risposta = server.variaLegameTitoloAbstract(abstracto);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			newAbstract.setTotRighe(totRighe);
			newAbstract.setEsito(esito);
			newAbstract.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut
						.getDatiElementoAut();

				newAbstract.setT001(datiElemento.getT001());
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					newAbstract.setDataVariazione(SBNMarcUtil
							.converteDataVariazione(datiElemento.getT005()));
					newAbstract.setT005(datiElemento.getT005());
				}
			}

			return newAbstract;
		} catch (RemoteException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			throw new DAOException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaLegameTitoloAbstract(String bid, String ticket)
			throws DAOException {

		boolean result = false;
		String testoEsito;

		try {
			checkTicket(ticket);

			SbnAbstractDAO server = new SbnAbstractDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(ticket));


			SBNMarc risposta = server.cancellaLegameTitoloAbstract(bid);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			testoEsito = sbnResult.getTestoEsito();

			result = (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK);

		} catch (Exception e) {
			throw new DAOException(e);
		}

		if (!result) {
			throw new DAOException(testoEsito);
		}

		return result;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public void aggiornaDatiCondivisioneSoggetto(
			List<DatiCondivisioneSoggettoVO> datiCondivisione, String ticket)
			throws DAOException {

		try {
			checkTicket(ticket);

			SemanticaDAO dao = new SemanticaDAO();

			if (ValidazioneDati.isFilled(datiCondivisione)) {
				dao.aggiornaDatiCondivisioneSoggetto(datiCondivisione);
				dao.attivaCondivisioneSoggetto(ValidazioneDati.first(datiCondivisione).getCidPolo(),
						SemanticaDAO.getFirmaUtente(ticket));
			}
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		}
	}

	public boolean isDescrittoreAutomaticoPerAltriSoggetti(String ticket, String did) throws DAOException {
		try {
			checkTicket(ticket);

			SemanticaDAO dao = new SemanticaDAO();
			return dao.isDescrittoreAutomatico(did);

		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DAOException(e);
		}
	}

	public ElaborazioniDifferiteOutputVo eseguiStampa(ParametriStampaVO params,
			BatchLogWriter log) throws ApplicationException {
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);
		return output;
	}

	public CommandResultVO migrazioneAggiornaSoggetti(CommandInvokeVO command) throws Exception {

		ParametriBatchSoggettoBaseVO pbsb = (ParametriBatchSoggettoBaseVO) command.getParams()[0];
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(pbsb);
		output.setStato(ConstantsJMS.STATO_ERROR);
		UserTransaction tx = null;
		int updated = 0;

		try {
			String ticket = command.getTicket();
			BatchLogWriter blw = (BatchLogWriter) command.getParams()[1];
			Logger _log = blw.getLogger();

			SemanticaDAO dao = new SemanticaDAO();
			SbnSoggettiDAO server = new SbnSoggettiDAO(getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket) );

			String codSoggettario = pbsb.getCodSoggettario();
			_log.debug("Soggettario: " + codSoggettario);
			String fromCid = pbsb.getFromCid();
			String toCid = pbsb.getToCid();
			if (ValidazioneDati.isFilled(fromCid))
				_log.debug("Filtro cid >= " + fromCid);
			if (ValidazioneDati.isFilled(toCid))
				_log.debug("Filtro cid <= " + toCid);

			tx = ctx.getUserTransaction();
			int cnt = 0;
			List<String> listaSoggetti = new ArrayList<String>(4096);

			if (pbsb.isFromFile()) {
				String inputFile = pbsb.getInputFile();
				_log.debug("Input file: " + inputFile);

				File f = new File(inputFile);
				BatchManager.getBatchManagerInstance().markForDeletion(f);
				leggiFileIdentificativi(f, listaSoggetti);
				cnt = ValidazioneDati.size(listaSoggetti);

			} else {
				DaoManager.begin(tx);
				ScrollableResults cursor = dao.cursor_ListaTotaleSoggetti(codSoggettario, fromCid, toCid);

				while (cursor.next()) {
					listaSoggetti.add(cursor.getString(0));
					if ((++cnt % 100) == 0)
						BatchManager.getBatchManagerInstance().checkForInterruption(pbsb.getIdBatch());
				}
				cursor.close();
				DaoManager.commit(tx);
			}

			_log.debug("Soggetti da aggiornare: " + cnt);

			if (!ValidazioneDati.isFilled(listaSoggetti)) {
				_log.error("Nessun soggetto trovato.");
				return CommandResultVO.build(command, output, null);
			}

			int err = 0;
			long start = System.currentTimeMillis();
			CreaVariaSogDesValidator v = new CreaVariaSogDesValidator();
			for (String cid : listaSoggetti)
				try {
					DaoManager.begin(tx);
					Tb_soggetto s = dao.getSoggettoById(cid);
					if (s == null) {
						_log.error("errore lettura: " + cid);
						err++;
						DaoManager.rollback(tx);
						continue;
					}

					CreaVariaSoggettoVO richiesta = ConversioneHibernateVO.toWeb().creaVariaSoggetto(s);
					richiesta.validate(v);

					SBNMarc risposta = server.variaSoggetto(richiesta);
					if (risposta == null)
						throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

					SbnMessageType sbnMessage = risposta.getSbnMessage();
					SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
					SbnResultType sbnResult = sbnResponse.getSbnResult();
					String esito = sbnResult.getEsito();
					String testoEsito = sbnResult.getTestoEsito();

					if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
						updated++;
						_log.debug("aggiornato cid: " + cid);
						DaoManager.commit(tx);

						if ((updated % 100) == 0) {
							double end = (double)(System.currentTimeMillis() - start) / 1000;
							_log.debug(String.format("n. %d soggetti aggiornati (in %.3f secondi)", updated, end) );
							BatchManager.getBatchManagerInstance().checkForInterruption(pbsb.getIdBatch());
							start = System.currentTimeMillis();
						}

					} else {
						err++;
						_log.error("errore aggiornamento cid: " + cid + ": " + testoEsito);
						DaoManager.rollback(tx);
					}

				} catch (BatchInterruptedException e) {
					throw e;
				} catch (Exception e) {
					err++;
					_log.error("errore aggiornamento cid: " + cid, e);
					DaoManager.rollback(tx);
				}

			_log.debug("totale cid aggiornati: " + updated);
			_log.debug("totale cid non aggiornati: " + err);

		} catch (Exception e) {
			DaoManager.rollback(tx);
			return CommandResultVO.build(command, output, e);
		}

		output.setStato( (updated > 0) ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);

		return CommandResultVO.build(command, output, null);

	}

	private void leggiFileIdentificativi(File f, List<String> output) throws Exception {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String cid = null;
			while ((cid = reader.readLine()) != null) {
				if (!ValidazioneDati.leggiXID(cid))
					continue;

				output.add(cid);
			}

		} finally {
			reader.close();
		}
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ApplicationException {
		try {
			if (command == null)
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

			command.validate();
			String ticket = command.getTicket();
			checkTicket(ticket);

			switch (command.getCommand() ) {
			case SEM_LISTA_CLASSI_COLLEGATE_TERMINE:
				return ricercaClassiPerTermineCollegato(command);
			case SEM_GESTIONE_LEGAME_TERMINE_CLASSE:
				return gestioneLegameTermineClasse(command);
			case SEM_CAMBIO_EDIZIONE_SOGGETTO:
				return eseguiCambioEdizioneSoggetto(command);
			case SEM_AGGIORNAMENTO_MASSIVO_SOGGETTI:
				return migrazioneAggiornaSoggetti(command);
			case SEM_DATI_CONDIVISIONE_SOGGETTO:
				return getDatiCondivisioneSoggetto(command);
			//almaviva5_20121026 #4768
			case SEM_VERIFICA_SISTEMA_EDIZIONE_BIBLIOTECA:
				return verificaSistemaEdizionePerBiblioteca(command);
			//almaviva5_20130128 #5238
			case SEM_LISTA_SISTEMA_EDIZIONE_BIBLIOTECA:
				return getListaSistemaEdizionePerBiblioteca(command);
			case SEM_AGGIORNA_DATI_CONDIVISIONE_SOGGETTO:
				return aggiornaDatiCondivisioneSoggetto(command);
			//almaviva5_20141008 #5650
			case SEM_VERIFICA_CID_CREAZIONE_INDICE:
				return verificaCidPerCreazioneIndice(command);

			default:
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_INVALID_COMMAND);
			}

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);
		} catch (ValidationException e) {
			throw new ApplicationException(e.getErrorCode());
		} catch (Exception e) {
			log.error("", e);
			ctx.setRollbackOnly();
			if (e instanceof SbnBaseException)
				return CommandResultVO.build(command, null, e);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}

	private CommandResultVO aggiornaDatiCondivisioneSoggetto(CommandInvokeVO command) throws Exception {

		String ticket = command.getTicket();
		List<DatiCondivisioneSoggettoVO> dcs = (List<DatiCondivisioneSoggettoVO>) command.getParams()[0];
		SemanticaDAO dao = new SemanticaDAO();

		if (ValidazioneDati.isFilled(dcs) ) {
			dao.aggiornaDatiCondivisioneSoggetto(dcs);
			dao.attivaCondivisioneSoggetto(ValidazioneDati.first(dcs).getCidPolo(), SemanticaDAO.getFirmaUtente(ticket));
		}

		return CommandResultVO.build(command, true, null);
	}


	private CommandResultVO eseguiCambioEdizioneSoggetto(CommandInvokeVO command) throws Exception {

		DettaglioSoggettoVO oldDettaglio = (DettaglioSoggettoVO) command.getParams()[0];
		String newCid = (String) command.getParams()[1];

		boolean livelloPolo = oldDettaglio.isLivelloPolo();
		String ticket = command.getTicket();
		boolean deleted = cancellaSoggettoDescrittore(livelloPolo, oldDettaglio.getCid(), ticket, SbnAuthority.SO);
		if (!deleted)
			throw new ApplicationException(SbnErrorTypes.GS_GENERIC);

		AnaliticaSoggettoVO analitica = creaAnaliticaSoggettoPerCid(livelloPolo, newCid, ticket, false);
		if (analitica.getEsitoType() != SbnMarcEsitoType.OK)
			return CommandResultVO.build(command, analitica, null);

		CreaVariaSoggettoVO soggetto = new CreaVariaSoggettoVO(analitica.getDettaglio());
		soggetto.setEdizioneSoggettario("E"); //entrambe

		CreaVariaSoggettoVO soggVariato = variaSoggetto(soggetto, ticket);

		return CommandResultVO.build(command, soggVariato, null);

	}

	private CommandResultVO gestioneLegameTermineClasse(CommandInvokeVO command) throws DAOException {

		try {
			DatiLegameTermineClasseVO legame = (DatiLegameTermineClasseVO) command.getParams()[0];
			SbnThesauriDAO server = new SbnThesauriDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(command.getTicket()));
			DatiLegameTermineClasseVO legameOut = (DatiLegameTermineClasseVO) legame.clone();

			SBNMarc risposta = server.gestioneLegameTermineClasse(legame);
			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());
			SbnMessageType sbnMessage = risposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();
			legameOut.setTotRighe(totRighe);

			legameOut.setEsito(esito);
			legameOut.setTestoEsito(testoEsito);

			if (sbnOutPut.getElementoAutCount() > 0) {
				ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
				if (SbnMarcEsitoType.of(esito) == SbnMarcEsitoType.OK) {
					legameOut.setDataVariazione(SBNMarcUtil.converteDataVariazione(datiElemento.getT005()));
					legameOut.setT005(datiElemento.getT005());
				}
			}

			return CommandResultVO.build(command, legameOut, null);

		} catch (RemoteException e) {

			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			log.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	private CommandResultVO ricercaClassiPerTermineCollegato(CommandInvokeVO command) throws Exception {
		RicercaClassiTermineVO richiesta = (RicercaClassiTermineVO) command.getParams()[0];

		try {
			SbnClassiDAO server = new SbnClassiDAO(getFactoryIndice(),
					getFactoryPolo(), getSbnUserType(command.getTicket()));

			SBNMarc risposta;
			int numBlocco = richiesta.getNumBlocco();
			String idLista = richiesta.getIdLista();

			if (ValidazioneDati.isFilled(idLista)) {
				//almaviva5_20111105 carica blocco
				RicercaClasseListaVO blocco = new RicercaClasseListaVO();
				blocco.setNumPrimo(numBlocco);
				blocco.setMaxRighe(Integer.valueOf(richiesta.getElemBlocco()) );
				blocco.setIdLista(idLista);
				blocco.setLivelloPolo(true);
				risposta = server.getNextBloccoClassi(blocco);
			} else
				//prima ricerca
				risposta = server.ricercaClassiPerBidCollegato(richiesta.getDid(),
						richiesta.getCodSistemaClassificazione(), richiesta.getCodEdizioneDewey(),
						richiesta.getElemBlocco() );

			if (risposta == null)
				throw new DAOException(SbnMarcEsitoType.SERVER_NON_DISPONIBILE.getTestoEsito());

			ClassiCollegateTermineVO result = new ClassiCollegateTermineVO();

			SbnResponseType sbnResponse = risposta.getSbnMessage().getSbnResponse();
			SbnOutputType sbnOutput = sbnResponse.getSbnResponseTypeChoice().getSbnOutput();
			int totRighe = sbnOutput.getTotRighe();
			int maxRighe = sbnOutput.getMaxRighe();
			idLista = sbnOutput.getIdLista();
			result.setEsito(sbnResponse.getSbnResult().getEsito());
			result.setTestoEsito(sbnResponse.getSbnResult().getTestoEsito());
			if (totRighe > 0) {
				int numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));

				result.setIdLista(idLista);
				result.setMaxRighe(maxRighe);
				result.setTotRighe(totRighe);
				result.setNumBlocco(numBlocco > 0 ? numBlocco : 1);
				result.setNumNotizie(totRighe);
				result.setTotBlocchi(numBlocchi);
			}

			result.setRisultati(server.creaListaSinteticaClassi(risposta, true));

			//in creazione legame titolo-termine vanno eliminate le classi già legate al titolo
			if (result.isEsitoOk()
				&& richiesta.isEscludiClassiLegateTitolo()
				&& ValidazioneDati.isFilled(richiesta.getBid())) {

				CatSemClassificazioneVO classi = ricercaClassiPerBidCollegato(
						true, richiesta.getBid(), null, null,
						command.getTicket(),
						CommonConfiguration.getPropertyAsInteger(Configuration.MAX_RESULT_ROWS, 4000));

				if (classi.isEsitoOk()) {
					//eliminazione duplicati
					List<SinteticaClasseVO> listaClassi = classi.getListaClassi();
					Iterator<SinteticaClasseVO> i = result.getRisultati().iterator();
					int progr = sbnOutput.getNumPrimo() - 1; //primo elemento del blocco
					while (i.hasNext()) {
						SinteticaClasseVO sc = i.next();
						if (listaClassi.contains(sc))
							i.remove();
						else
							sc.setProgr(++progr); //rinumerazione
					}
				}
			}

			return CommandResultVO.build(command, result, null);

		} catch (RemoteException e) {

			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			log.error("", e);
			log.error(e.getMessage());
			throw new DAOException(e);
		}

	}


	/**
	 *
	 * @throws RemoteException
	 * @throws ApplicationException
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 */
	public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List parametri) throws ApplicationException,
			DataException, ValidationException, EJBException {

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		stampeOnline.setParametriInput(parametri);
		if(tipoStampa==StampaType.STAMPA_SISTEMA_CLASSIFICAZIONE) {
			return eseguiStampaSistemaClassificazione(stampeOnline);
		}else if(tipoStampa==StampaType.STAMPA_TERMINI_THESAURO) {
			return eseguiStampaTerminiThesauro(stampeOnline);
		}else{
			return stampeOnline;
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws RemoteException
	 * @throws ApplicationException
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List<ElementoStampaSemanticaVO> stampeBatch(String ticket, StampaType tipoStampa,
			ParametriStampaVO parametriStampa) throws ApplicationException,
			DataException, ValidationException, EJBException {

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		StampeOnlineVO risultato  = null;

		List<ParametriStampaVO> parametri = new ArrayList<ParametriStampaVO>();
		parametri.add(parametriStampa);
		stampeOnline.setParametriInput(parametri);

		if(tipoStampa==StampaType.STAMPA_SISTEMA_CLASSIFICAZIONE) {
			risultato = eseguiStampaSistemaClassificazione(stampeOnline);
			return risultato.getRigheDatiDB();
		}else if(tipoStampa==StampaType.STAMPA_TERMINI_THESAURO){
			risultato = eseguiStampaTerminiThesauro(stampeOnline);
			return risultato.getRigheDatiDB();
		} else {
			return null;
		}
	}

	private StampeOnlineVO eseguiStampaTerminiThesauro(StampeOnlineVO stampeOnline) throws ApplicationException {
		try {
			log.info("Stampa termini thesauro");
			ParametriStampaTerminiThesauroVO parametri = (ParametriStampaTerminiThesauroVO) stampeOnline
					.getParametriInput().get(0);

			SemanticaDAO dao = new SemanticaDAO();
			List<ElementoStampaTerminiThesauroVO> output = dao.stampaTerminiThesauro(parametri);
			stampeOnline.setRigheDatiDB(new ArrayList(output) );
			return stampeOnline;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	private StampeOnlineVO eseguiStampaSistemaClassificazione(StampeOnlineVO stampeOnline) throws ApplicationException {
		try {
			log.info("Stampa sistema classificazione");
			ParametriStampaSistemaClassificazioneVO parametri =
				(ParametriStampaSistemaClassificazioneVO) stampeOnline.getParametriInput().get(0);

			SemanticaDAO dao = new SemanticaDAO();
			List<ElementoStampaClassificazioneVO> output = dao.stampaSistemaClassificazione(parametri);
			stampeOnline.setRigheDatiDB(new ArrayList(output) );
			return stampeOnline;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public CountLegamiSemanticaVO countLegamiSemantica(String bid, String area)
			throws ApplicationException, DataException, ValidationException,
			EJBException {

		try {
			if (ValidazioneDati.strIsNull(bid))
				throw new ValidationException("Parametro bid non valido");
			SemanticaDAO dao = new SemanticaDAO();
			return dao.countLegamiSemantica(bid, area);
		} catch (DaoManagerException e) {
			e.printStackTrace();
			throw new DataException(e);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}

	public ElaborazioniDifferiteOutputVo cancellaSoggettiNonUtilizzati(
			ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta,
			BatchLogWriter blw) throws ApplicationException, EJBException {

		UserTransaction tx = null;
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);
		String fileName = null;
		int totaleCancellati = 0;
		Logger _log = blw.getLogger();

		SemanticaDAO dao = new SemanticaDAO();
		Writer report = null;

		try {
			int loop = 0;
			int toCommit = 0;
			boolean error = false;	//flag errore cancellazione
			String firmaUte = richiesta.getCodPolo() + richiesta.getCodBib() + richiesta.getUser();
			fileName = richiesta.getFirmaBatch() + ".htm";

			String cdSogg = CodiciProvider.cercaDescrizioneCodice(richiesta.getCodSoggettario(), CodiciType.CODICE_SOGGETTARIO,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			String edizione = CodiciProvider.cercaDescrizioneCodice(richiesta.getEdizioneSoggettario(), CodiciType.CODICE_SOGGETTARIO,
					CodiciRicercaType.RICERCA_CODICE_SBN);

			//scrivo log su file
			FileOutputStream out = new FileOutputStream(StampeUtil.getBatchFilesPath() + File.separator + fileName);
			report = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

			writeCancellaSoggettiNonUtilizzatiReport(report, cdSogg, edizione, true, null, null, false, 0);

			tx = ctx.getUserTransaction();
			DaoManager.begin(tx);

			// primo cursore
			ScrollableResults cursore = dao.cursor_CancellaSoggettiNonUtilizzati(richiesta, null);
			// ciclo
			while (cursore.next() ) {

				try {	//controllo interruzione manuale
					BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
				} catch (BatchInterruptedException e) {
					cursore.close();
				}

				Tb_soggetto soggetto = (Tb_soggetto) cursore.get(0);
				Timestamp ts = DaoManager.now();
				ElementoStampaSoggettoVO soggVO = ConversioneHibernateVO.toWeb().elementoStampaSoggettoDecodificato(soggetto);
				try {
					dao.cancellaSoggettoNonUtilizzato(soggetto, firmaUte, ts);
					++toCommit;
					writeCancellaSoggettiNonUtilizzatiReport(report, null, null, false, soggVO, "Cancellato", false, 0);

				} catch (Exception e) {
					_log.error("Errore cancellazione CID: " + soggVO.getId(), e);
					writeCancellaSoggettiNonUtilizzatiReport(report, null, null, false, soggVO, "Non cancellato. Consultare il log per dettagli", false, 0);
					error = true;
				}

				if (++loop == BATCH_COMMIT_THRESHOLD || error) {
					// chiudo il cursore e committo la transazione
					String lastCid = soggetto.getCid();
					cursore.close();
					if (!error)	{// tx ok?
						DaoManager.commit(tx);
						// totale dei soggetti cancellati
						totaleCancellati += loop;
					} else
						DaoManager.rollback(tx);
					// scrivo sul log del batch i soggetti effettivamente cancellati
					toCommit = 0;
					loop = 0;
					error = false;

					//riapro il cursore con il nuovo filtro su cid
					DaoManager.begin(tx);
					cursore = dao.cursor_CancellaSoggettiNonUtilizzati(richiesta, lastCid);
					continue;
				}
			}
			cursore.close();
			DaoManager.commit(tx);

			// scrivo sul log i dati residui
			totaleCancellati += toCommit;

			blw.logWriteLine("Soggetti cancellati: " + totaleCancellati);

		} catch (Exception e) {
			DaoManager.rollback(tx);
			blw.logWriteException(e);
			output.setStato(ConstantsJMS.STATO_ERROR);

		} finally {
			if (report != null)
				try {
					writeCancellaSoggettiNonUtilizzatiReport(report, null, null, false, null, null, true, totaleCancellati);
					output.addDownload(fileName, fileName);
					report.flush();
					report.close();
				} catch (IOException e) {
					blw.logWriteException(e);
				}

		}

		return output;
	}

	private void writeCancellaSoggettiNonUtilizzatiReport(
			Writer buf, String cdSogg, String edizione, boolean header,
			ElementoStampaSoggettoVO soggetto, String msg, boolean footer, int totale) throws IOException {

		if (header) {	//inizio report html
			buf.append("<html>");
			buf.append("<head>");
			buf.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
			buf.append("<title>Cancellazione Soggetti Non Utilizzati</title>");
			buf.append("</head>");
			buf.append("<body>");

			buf.append("<h2>CANCELLAZIONE SOGGETTI NON UTILIZZATI</h2>");
			buf.append("<h3>Soggettario:&nbsp;").append(cdSogg);
			if (ValidazioneDati.isFilled(edizione))
				buf.append("Edizione:&nbsp;").append(StringEscapeUtils.escapeHtml(edizione));
			buf.append("</h3>");
			buf.append("<hr/><br/>");

			buf.append("<table width=\"90%\" border=\"1\">");

			buf.append("<tr>");
			buf.append("<th>CID</th>");
			buf.append("<th>Edizione</th>");
			buf.append("<th>Testo</th>");
			buf.append("<th>Tipo</th>");
			buf.append("<th>Livello</th>");
			buf.append("<th>Esito</th>");
			buf.append("</tr>");
		}

		if (soggetto != null) {	//riga soggetto
			buf.append("<tr align=\"left\" >");
			buf.append("<td>").append(soggetto.getId()).append("</td>");
			buf.append("<td>").append(StringEscapeUtils.escapeHtml(soggetto.getEdizioneSoggettario())).append("</td>");
			buf.append("<td>").append(StringEscapeUtils.escapeHtml(soggetto.getTesto())).append("</td>");
			buf.append("<td>").append(soggetto.getTipoSoggetto()).append("</td>");
			buf.append("<td>").append(soggetto.getLivelloAutorita()).append("</td>");
			buf.append("<td>").append(ValidazioneDati.isFilled(msg) ? msg : "&nbsp;").append("</td>");
			buf.append("</tr>");
		}

		if (footer) {	//fine report html
			buf.append("</table>");
			buf.append("<br/>");
			buf.append("<h4>Soggetti cancellati:&nbsp;").append(String.valueOf(totale)).append("</h4>");
			buf.append("</body>");
			buf.append("</html>");
		}

	}

	public ElaborazioniDifferiteOutputVo cancellaDescrittoriNonUtilizzati(
			ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta,
			BatchLogWriter blw) throws ApplicationException, EJBException {

		UserTransaction tx = null;
		ScrollableResults cursor = null;
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);
		SemanticaDAO dao = new SemanticaDAO();
		int totaleCancellati = 0;
		Logger _log = blw.getLogger();
		String fileName = null;
		Writer report = null;

		try {
			int loop = 0;
			int toCommit = 0;
			boolean error = false;	//flag errore cancellazione
			String firmaUte = richiesta.getCodPolo() + richiesta.getCodBib() + richiesta.getUser();

			String cdSogg = CodiciProvider.cercaDescrizioneCodice(richiesta.getCodSoggettario(), CodiciType.CODICE_SOGGETTARIO,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			String edizione = CodiciProvider.cercaDescrizioneCodice(richiesta.getEdizioneSoggettario(), CodiciType.CODICE_SOGGETTARIO,
					CodiciRicercaType.RICERCA_CODICE_SBN);

			//scrivo log su file
			fileName = richiesta.getFirmaBatch() + ".htm";
			FileOutputStream out = new FileOutputStream(StampeUtil.getBatchFilesPath() + File.separator + fileName);
			report = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

			writeCancellaDescrittoriNonUtilizzatiReport(report, cdSogg, edizione, true, null, null, false, 0);

			tx = ctx.getUserTransaction();
			DaoManager.begin(tx);
			int cnt = 0;
			List<String> listaDescrittori = new ArrayList<String>(4096);
			// cursore
			cursor = dao.cursor_CancellaDescrittoriNonUtilizzati(richiesta);
			// ciclo
			while (cursor.next() ) {
				listaDescrittori.add((String) cursor.get(0));
				if ((++cnt % 100) == 0)
					BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
			}
			_log.debug("Descrittori da aggiornare: " + cnt);
			DaoManager.closeCursor(cursor);
			DaoManager.commit(tx);

			for (String did : listaDescrittori) {
				BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
				DaoManager.begin(tx);
				Tb_descrittore descrittore = dao.getDescrittoreById(did);
				if (descrittore == null)
					continue;

				Timestamp ts = DaoManager.now();
				ElementoStampaDescrittoreVO desVO = ConversioneHibernateVO.toWeb().elementoStampaDescrittoreDecodificato(descrittore);
				try {
					dao.cancellaDescrittoreNonUtilizzato(descrittore, firmaUte, ts);
					toCommit++;
					writeCancellaDescrittoriNonUtilizzatiReport(report, null, null, false, desVO, "Cancellato", false, 0);

				} catch (Exception e) {
					_log.error("Errore cancellazione DID: " + desVO.getId(), e);
					writeCancellaDescrittoriNonUtilizzatiReport(report, null, null, false, desVO, "Non cancellato. Consultare il log per dettagli", false, 0);
					error = true;
				}

				if (++loop == BATCH_COMMIT_THRESHOLD || error) {
					// chiudo il cursore e committo la transazione
					if (!error)	{// tx ok?
						DaoManager.commit(tx);
						// totale dei descrittori cancellati
						totaleCancellati += loop;
					} else
						DaoManager.rollback(tx);
					// scrivo sul log del batch i descrittori effettivamente cancellati
					toCommit = 0;
					loop = 0;
					error = false;
					continue;
				}
			}

			DaoManager.commit(tx);

			// scrivo sul log i dati residui
			totaleCancellati += toCommit;

			blw.logWriteLine("Descrittori cancellati: " + totaleCancellati);

			return output;

		} catch (Exception e) {
			DaoManager.rollback(tx);
			blw.logWriteException(e);
			output.setStato(ConstantsJMS.STATO_ERROR);

		} finally {
			DaoManager.closeCursor(cursor);
			if (report != null)
				try {
					writeCancellaDescrittoriNonUtilizzatiReport(report, null, null, false, null, null, true, totaleCancellati);
					output.addDownload(fileName, fileName);
					report.flush();
					report.close();
				} catch (IOException e) {
					blw.logWriteException(e);
				}

		}

		return output;

	}

	private void writeCancellaDescrittoriNonUtilizzatiReport(
			Writer buf, String cdSogg, String edizione, boolean header,
				ElementoStampaDescrittoreVO descr, String msg, boolean footer, int totale) throws IOException {

		if (header) {	//inizio report html
			buf.append("<html>");
			buf.append("<head>");
			buf.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
			buf.append("<title>Cancellazione Descrittori Non Utilizzati</title>");
			buf.append("</head>");
			buf.append("<body>");

			buf.append("<h2>CANCELLAZIONE DESCRITTORI NON UTILIZZATI</h2>");
			buf.append("<h3>Soggettario:&nbsp;").append(cdSogg).append("</h3>");
			buf.append("<hr/><br/>");

			buf.append("<table width=\"90%\" border=\"1\">");

			buf.append("<tr>");
			buf.append("<th>DID</th>");
			buf.append("<th>Edizione</th>");
			buf.append("<th>Testo</th>");
			buf.append("<th>Forma</th>");
			buf.append("<th>Livello</th>");
			buf.append("<th>Esito</th>");
			buf.append("</tr>");
		}

		if (descr != null) {	//riga descrittore
			buf.append("<tr align=\"left\" >");
			buf.append("<td>").append(descr.getId()).append("</td>");
			buf.append("<td>").append(StringEscapeUtils.escapeHtml(descr.getEdizioneSoggettario())).append("</td>");
			buf.append("<td>").append(StringEscapeUtils.escapeHtml(descr.getTesto())).append("</td>");
			buf.append("<td>").append(descr.getFormaNome()).append("</td>");
			buf.append("<td>").append(descr.getLivelloAutorita()).append("</td>");
			buf.append("<td>").append(ValidazioneDati.isFilled(msg) ? msg : "&nbsp;").append("</td>");
			buf.append("</tr>");
		}

		if (footer) {	//fine report html
			buf.append("</table>");
			buf.append("<br/>");
			buf.append("<h4>Descrittori cancellati:&nbsp;").append(String.valueOf(totale)).append("</h4>");
			buf.append("</body>");
			buf.append("</html>");
		}

	}


	private CommandResultVO getDatiCondivisioneSoggetto(CommandInvokeVO command) throws Exception {
		SemanticaDAO dao = new SemanticaDAO();
		try {
			String cidPolo = (String) command.getParams()[0];
			String cidIndice = (String) command.getParams()[1];
			String bid = (String) command.getParams()[2];

			List<DatiCondivisioneSoggettoVO> datiCondivisione = dao.getDatiCondivisioneSoggetto(cidPolo, cidIndice, bid);

			return CommandResultVO.build(command, new ArrayList<DatiCondivisioneSoggettoVO>(datiCondivisione), null);

		} catch (Exception e) {
			return CommandResultVO.build(command, null, e);
		}
	}


	private CommandResultVO verificaSistemaEdizionePerBiblioteca(CommandInvokeVO command) throws Exception {
		//almaviva5_20121026 #4768
		SemanticaDAO dao = new SemanticaDAO();
		try {
			String codPolo = (String) command.getParams()[0];
			String codBib = (String) command.getParams()[1];
			String codSistema = (String) command.getParams()[2];
			String edizione = (String) command.getParams()[3];
			//almaviva5_20141117 edizione ridotta
			if (ValidazioneDati.isFilled(edizione)) {
				TB_CODICI cod = CodiciProvider.cercaCodice(edizione, CodiciType.CODICE_EDIZIONE_CLASSE, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
				if (cod == null)
					throw new ApplicationException(SbnErrorTypes.GS_EDIZIONE_DEWEY_NON_VALIDA, edizione);
				edizione = cod.getCd_tabellaTrim();
				if (!ValidazioneDati.isFilled(edizione))
					throw new ApplicationException(SbnErrorTypes.GS_EDIZIONE_DEWEY_NON_VALIDA, edizione);
			}

			int cnt = dao.verificaSistemaEdizionePerBiblioteca(codPolo, codBib, codSistema, edizione);

			return CommandResultVO.build(command, (cnt > 0), null);

		} catch (Exception e) {
			return CommandResultVO.build(command, null, e);
		}
	}

	private CommandResultVO getListaSistemaEdizionePerBiblioteca(CommandInvokeVO command) throws Exception {
		//almaviva5_20121026 #4768
		SemanticaDAO dao = new SemanticaDAO();
		try {
			ArrayList<String> output = new ArrayList<String>();
			String codPolo = (String) command.getParams()[0];
			String codBib = (String) command.getParams()[1];
			String codSistema = (String) command.getParams()[2];
			String edizione = (String) command.getParams()[3];
			List<Tr_sistemi_classi_biblioteche> edizioni = dao.getListaSistemaEdizionePerBiblioteca(codPolo, codBib, codSistema, edizione);
			for (Tr_sistemi_classi_biblioteche scb : edizioni)
				output.add(ValidazioneDati.trimOrEmpty(scb.getCd_edizione()));

			return CommandResultVO.build(command, output, null);

		} catch (Exception e) {
			return CommandResultVO.build(command, null, e);
		}
	}

	private CommandResultVO verificaCidPerCreazioneIndice(CommandInvokeVO command) throws Exception {
		//almaviva5_20141008 #5650
		String cid = (String) command.getParams()[0];
		Set<String> poli = new HashSet<String>();
		String cdPolo = getPolo().getInfoPolo().getCd_polo();
		poli.add(cdPolo);

		//check polo ausiliario
		Tbf_contatore c = ContatoriDAO.getContatore(command.getTicket(), cdPolo, Constants.ROOT_BIB, SbnAuthority.SO.toString(), 0, null);
		if (c != null) {
			String polo = ValidazioneDati.trimOrNull(c.getKey1());
			if (polo != null)
				poli.add(polo);
		}

		//controllo cid
		String poloCid = cid.substring(0, 3);
		String newCid = poli.contains(poloCid) ? cid : SBNMarcUtil.SBNMARC_DEFAULT_ID;
		return CommandResultVO.build(command, newCid, null);

	}

}
