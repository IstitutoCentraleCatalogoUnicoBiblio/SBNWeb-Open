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
package it.iccu.sbn.ejb.domain.bibliografica;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnEJB;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnHttpIndice;
import it.iccu.sbn.SbnMarcFactory.factory.ServerHttp;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAccorpamentoDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAllAuthorityDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAllineamentiIndicePoloDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAutoriDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneImportSuPoloDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneLuoghiDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneMarcheDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneProposteDiCorrezioneDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneTitoliDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnStampaSchedeDao;
import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiImportSuPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioCancAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazionePropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCondividereVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiVariazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiVariazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiVariazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioElenchiListeConfrontoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiScambiaResponsLegameTitAutVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.CountLegamiSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTitoliEditoreDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTitoliEditoreVO;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_fornitoriDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.persistence.dao.bibliografica.LocalizzaDAO;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.semantica.SemanticaDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.bibliografica.viste.V_catalogo_editoria;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.vo.custom.Credentials;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="Interrogazione" description="A session bean named
 *           Interrogazione" display-name="Semantica"
 *           jndi-name="sbnWeb/Interrogazione" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class InterrogazioneBean extends TicketChecker implements Interrogazione {

	private static final long serialVersionUID = 1646219628141311751L;
	private static Logger logger = Logger.getLogger(Interrogazione.class);

	private it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo polo;
	private it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario bibliotecario;
	private it.iccu.sbn.ejb.domain.bibliografica.Profiler profiler;

	private Reference<FactorySbn> factoryIndice;
	private Reference<FactorySbn> factoryPolo;

	private Tba_fornitoriDao daoEditore;
	private Tbc_inventarioDao daoInventario;
	private Tbf_biblioteca_in_poloDao daoBib;
	private SemanticaDAO daoSemantica;



	private Semantica semantica;

	private SessionContext ctx;

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

	public void setSessionContext(SessionContext ctx) throws EJBException,
	RemoteException {
		this.ctx = ctx;
		daoEditore = new Tba_fornitoriDao() ;
		daoInventario = new Tbc_inventarioDao() ;
		daoBib = new Tbf_biblioteca_in_poloDao() ;
		daoSemantica = new SemanticaDAO() ;

		try {
			DomainEJBFactory factory = DomainEJBFactory.getInstance();

			semantica = factory.getSemantica();

			this.polo = factory.getPolo();
			this.bibliotecario = factory.getBibliotecario();
			this.profiler = factory.getProfiler();

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	private Credentials getCredentials() throws RemoteException,
	DaoManagerException {
		Credentials credentials = this.polo.getCredentials();
		return credentials;
	}

	private FactorySbn getFactoryIndice() throws RemoteException,
	DaoManagerException, SbnMarcException {
		if (factoryIndice == null || factoryIndice.get() == null) {
			ParametriHttp paramIndice = this.polo.getIndice();
			factoryIndice = new SoftReference<FactorySbn>(new FactorySbnHttpIndice("INDICE", new ServerHttp(paramIndice, getCredentials())) );
		}
		return factoryIndice.get();
	}

	private FactorySbn getFactoryPolo() throws RemoteException,
	DaoManagerException, SbnMarcException {
		if (factoryPolo == null || factoryPolo.get() == null) {
			factoryPolo = new SoftReference<FactorySbn>(new FactorySbnEJB("polo") );
		}

		return factoryPolo.get();
	}

	private SbnUserType getSbnUserType(String ticket) throws RemoteException,
	DaoManagerException {
		SbnUserType sbn = this.bibliotecario.getUserSbnMarc(ticket);
		return sbn;
	}

	// AREA RICHIESTA PROGRESSIVO SBN (Bid-Vid ...)
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiPassaggioGetIdSbnVO getIdSbn(
			AreaDatiPassaggioGetIdSbnVO areaDatiPass, String ticket)
	throws DAOException {

		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));

			AreaDatiPassaggioGetIdSbnVO risposta = new AreaDatiPassaggioGetIdSbnVO();
			risposta = gestioneAllAuthority.getIdSbn(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}
	}

	// AREA ACCORPAMENTO FRA OGGETTI
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiAccorpamentoReturnVO richiestaAccorpamento(
			AreaDatiAccorpamentoVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAccorpamentoDao gestioneAccorpamento = new SbnGestioneAccorpamentoDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiAccorpamentoReturnVO risposta = new AreaDatiAccorpamentoReturnVO();
			risposta = gestioneAccorpamento.richiestaAccorpamento(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiAccorpamentoReturnVO richiestaSpostamentoLegami(
			AreaDatiAccorpamentoVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAccorpamentoDao gestioneAccorpamento = new SbnGestioneAccorpamentoDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiAccorpamentoReturnVO risposta = new AreaDatiAccorpamentoReturnVO();
			risposta = gestioneAccorpamento
			.richiestaSpostamentoLegami(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	// AREA LOCALIZZAZIONI
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO cercaLocalizzazioni(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass,
			boolean soloPresenzaPolo, String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneAllAuthority.cercaLocalizzazioni(areaDatiPass,
					soloPresenzaPolo);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO localizzaAuthorityMultipla(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			risposta = gestioneAllAuthority
			.localizzaAuthorityMultipla(areaDatiPass);
			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	public AreaDatiVariazioneReturnVO localizzaUnicoXML(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			risposta = gestioneAllAuthority
			.localizzaUnicoXML(areaDatiPass);
			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO localizzaAuthority(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (areaDatiPass.getTipoLoc().equals("Gestione")) {
				if (this.profiler.isOkAttivita("Localizza",
						"Localizza per gestione", ticket)) {
					sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
				}
			} else if (areaDatiPass.getTipoLoc().equals("Possesso")) {
				if (this.profiler.isOkAttivita("Localizza",
						"Localizza per posseduto", ticket)) {
					sbnTipoLoc = SbnTipoLocalizza.POSSESSO;
				}
			} else {
				if (this.profiler.isOkAttivita("Localizza",
						"Localizza per gestione", ticket)
						&& this.profiler.isOkAttivita("Localizza",
								"Localizza per posseduto", ticket)) {
					sbnTipoLoc = SbnTipoLocalizza.TUTTI;
				}
			}

			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getIdLoc());
				return risposta;
			}

			risposta = gestioneAllAuthority.localizzaAuthority(areaDatiPass);
			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	// AREA CANCELLAZIONE AUTHORITY
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiPassaggioCancAuthorityVO cancellaAuthority(
			AreaDatiPassaggioCancAuthorityVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioCancAuthorityVO risposta = new AreaDatiPassaggioCancAuthorityVO();
			risposta = gestioneAllAuthority.cancellaAuthority(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	// AREA CATTURA
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiVariazioneReturnVO catturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",	"Localizza per gestione", ticket)
					&& this.profiler.isOkAttivita("Localizza", "Localizza per posseduto", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.TUTTI;
			}

        	// Inizio Intervento google3: Per cattura non viene chiamata la verifica delle abilitazioni
        	// serve solo il Cerca + Localizza per Gestione
			if (this.profiler.isOkAttivita("Localizza",	"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			// Fine Intervento google3

			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getIdPadre());
				return risposta;
			}

			// MAIL Scognamiglio del 03.01.2017 Collaudo nuova funzionalità copia reticolo con spogli
			// Intervento almaviva2 gennaio 2017
			areaDatiPass.setLivAutUtente(this.profiler.getLivelloDocumento(SbnMateriale.valueOf("M"), ticket));


			areaDatiPass.setTicket(ticket);
			risposta = gestioneAllAuthority.catturaReticolo(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO catturaAutore(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest per l'oggetto "
						+ areaDatiPass.getIdPadre());
				return risposta;
			}

			risposta = gestioneAllAuthority.catturaAutore(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO catturaMarca(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest per l'oggetto "
						+ areaDatiPass.getIdPadre());
				return risposta;
			}

			risposta = gestioneAllAuthority.catturaMarca(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO catturaLuogo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest per l'oggetto "
						+ areaDatiPass.getIdPadre());
				return risposta;
			}

			risposta = gestioneAllAuthority.catturaLuogo(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO scatturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			risposta = gestioneAllAuthority.scatturaReticolo(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO inserisciReticoloCatturato(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			risposta = gestioneAllAuthority
			.inserisciReticoloCatturato(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaTabellaOggettiDaCondividereVO ricercaDocumentoPerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaTabellaOggettiDaCondividereVO risposta = new AreaTabellaOggettiDaCondividereVO();
			risposta = gestioneAllAuthority
			.ricercaDocumentoPerCondividi(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaTabellaOggettiDaCondividereVO ricercaAutorePerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaTabellaOggettiDaCondividereVO risposta = new AreaTabellaOggettiDaCondividereVO();
			risposta = gestioneAllAuthority
			.ricercaAutorePerCondividi(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}



	// AREA TITOLI
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoli(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneTitoli.ricercaTitoli(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}


	// AREA trattamentoDocumento per import Unimarc su DB di POLO
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiImportSuPoloVO trattamentoDocumento(AreaDatiImportSuPoloVO areaDatiPass, String ticket)
	throws DAOException {
		BatchLogWriter blw = (BatchLogWriter)areaDatiPass.getBatchLogWriter();
		try {
			checkTicket(ticket);
			SbnGestioneImportSuPoloDao gestioneImportSuPoloDao = new SbnGestioneImportSuPoloDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket), blw );
			areaDatiPass = gestioneImportSuPoloDao.trattamentoDocumento(areaDatiPass);

		}	catch (Exception e) {
			blw.logWriteException(e);
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoErrore(e.getMessage());
		}

		return areaDatiPass;

	}

	// AREA trattamentoDocumentoInferiore per import Unimarc su DB di POLO
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiImportSuPoloVO trattamentoDocumentoInferiore(AreaDatiImportSuPoloVO areaDatiPass, String ticket)
	throws DAOException {
		BatchLogWriter blw = (BatchLogWriter)areaDatiPass.getBatchLogWriter();
		try {
			checkTicket(ticket);
			SbnGestioneImportSuPoloDao gestioneImportSuPoloDao = new SbnGestioneImportSuPoloDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket), blw );
			areaDatiPass = gestioneImportSuPoloDao.trattamentoDocumentoInferiore(areaDatiPass);

		}	catch (Exception e) {
			blw.logWriteException(e);
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoErrore(e.getMessage());
		}

		return areaDatiPass;

	}
	// AREA trattamentoDocumento per import Unimarc su DB di POLO
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiImportSuPoloVO trattamentoLegamiDocumento(AreaDatiImportSuPoloVO areaDatiPass, String livelloGerarchiche, String ticket)
	throws DAOException {
		BatchLogWriter blw = (BatchLogWriter)areaDatiPass.getBatchLogWriter();
		try {
			checkTicket(ticket);

			SbnGestioneImportSuPoloDao gestioneImportSuPoloDao = new SbnGestioneImportSuPoloDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket),
					blw );
			areaDatiPass = gestioneImportSuPoloDao.trattamentoLegamiDocumento(areaDatiPass, livelloGerarchiche);

		}	catch (Exception e) {
			blw.logWriteException(e);
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoErrore(e.getMessage());
		}

		return areaDatiPass;

	}


	public AreaDatiImportSuPoloVO trattamentoAutore(AreaDatiImportSuPoloVO areaDatiPass, String ticket)
	throws DAOException {
		BatchLogWriter blw = (BatchLogWriter)areaDatiPass.getBatchLogWriter();
		try {
			checkTicket(ticket);

			SbnGestioneImportSuPoloDao gestioneImportSuPoloDao = new SbnGestioneImportSuPoloDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket),
					blw );
			areaDatiPass = gestioneImportSuPoloDao.trattamentoAutore(areaDatiPass);

		}	catch (Exception e) {
			blw.logWriteException(e);
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoErrore(e.getMessage());
		}

		return areaDatiPass;

	}

	public AreaDatiImportSuPoloVO trattamentoTitCollegati4xx(AreaDatiImportSuPoloVO areaDatiPass, String ticket)
	throws DAOException {
		BatchLogWriter blw = (BatchLogWriter)areaDatiPass.getBatchLogWriter();
		try {
			checkTicket(ticket);

			SbnGestioneImportSuPoloDao gestioneImportSuPoloDao = new SbnGestioneImportSuPoloDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket),
					blw );
			areaDatiPass = gestioneImportSuPoloDao.trattamentoTitCollegati4xx(areaDatiPass);

		}	catch (Exception e) {
			blw.logWriteException(e);
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoErrore(e.getMessage());
		}

		return areaDatiPass;

	}

	public AreaDatiImportSuPoloVO trattamentoTitCollegati5xx(AreaDatiImportSuPoloVO areaDatiPass, String ticket)
	throws DAOException {
		BatchLogWriter blw = (BatchLogWriter)areaDatiPass.getBatchLogWriter();
		try {
			checkTicket(ticket);

			SbnGestioneImportSuPoloDao gestioneImportSuPoloDao = new SbnGestioneImportSuPoloDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket),
					blw );
			areaDatiPass = gestioneImportSuPoloDao.trattamentoTitCollegati5xx(areaDatiPass);

		}	catch (Exception e) {
			blw.logWriteException(e);
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoErrore(e.getMessage());
		}

		return areaDatiPass;

	}

	public AreaDatiImportSuPoloVO trattamentoSogClaCollegati6xx(AreaDatiImportSuPoloVO areaDatiPass, String ticket)
	throws DAOException {
		BatchLogWriter blw = (BatchLogWriter)areaDatiPass.getBatchLogWriter();
		try {
			checkTicket(ticket);

			SbnGestioneImportSuPoloDao gestioneImportSuPoloDao = new SbnGestioneImportSuPoloDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket),
					blw );
			areaDatiPass = gestioneImportSuPoloDao.trattamentoSogClaCollegati6xx(areaDatiPass);

		}	catch (Exception e) {
			blw.logWriteException(e);
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoErrore(e.getMessage());
		}

		return areaDatiPass;

	}



	// AREA SCHEDE TITOLI

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiPassaggioElenchiListeConfrontoVO getElenchiListeConfronto(
			AreaDatiPassaggioElenchiListeConfrontoVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioElenchiListeConfrontoVO risposta = new AreaDatiPassaggioElenchiListeConfrontoVO();
			risposta = gestioneTitoli.getElenchiListeConfronto(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioSchedaDocCiclicaVO getSchedaDocCiclica(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioSchedaDocCiclicaVO risposta = new AreaDatiPassaggioSchedaDocCiclicaVO();
			risposta = gestioneTitoli.getSchedaDocCiclica(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioSchedaDocCiclicaVO insertTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioSchedaDocCiclicaVO risposta = new AreaDatiPassaggioSchedaDocCiclicaVO();
			risposta = gestioneTitoli.insertTbReportIndice(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	public AreaDatiPassaggioSchedaDocCiclicaVO cancellaTabelleTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioSchedaDocCiclicaVO risposta = new AreaDatiPassaggioSchedaDocCiclicaVO();
			risposta = gestioneTitoli.cancellaTabelleTbReportIndice(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}
	}


	// Modifica almaviva2 04.08.2010 - Gestione periodici nuovo servizio per ricavare, dato un bid,  tutti i legami
	// fra periodici sia verso l'alto che verso il basso
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLegamiFraPeriodici(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneTitoli.ricercaLegamiFraPeriodici(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoliPerGestionali(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneTitoli.ricercaTitoliPerGestionali(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoTitoliPerBID(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			logger.debug("richiesta analitica per bid: " + areaDatiPass.getBidRicerca());
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
			risposta = gestioneTitoli.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoTitoli(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneTitoli.getNextBloccoTitoli(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO inserisciTitolo(
			AreaDatiVariazioneTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// INIZIO ==================================================================
			// Controllo profilo per varizione per tipo materiale
			String tipoMateriale = areaDatiPass.getDetTitoloPFissaVO().getTipoMat();

			// Intervento settembre 2015: per materiale H si deve verificare, in caso di assenza abilitazione allo steso se c'è abilitazione
			// almeno alla Musica perchè in questo caso si puo comunque inviare la variazione per il solo MusicaType
			areaDatiPass.setAbilPerTipoMatMusicale(false);
			if (tipoMateriale.equals("H")) {
				if (this.profiler.isAbilitazioneTipoMate("H", ticket)) {
					areaDatiPass.setAbilPerTipoMat(true);
				} else {
					if (this.profiler.isAbilitazioneTipoMate("U", ticket)) {
						areaDatiPass.setAbilPerTipoMat(false);
						areaDatiPass.setAbilPerTipoMatMusicale(true);
					} else {
						areaDatiPass.setAbilPerTipoMat(false);
					}
				}
			} else {
				if (this.profiler.isAbilitazioneTipoMate(tipoMateriale, ticket)) {
					areaDatiPass.setAbilPerTipoMat(true);
				} else {
					areaDatiPass.setAbilPerTipoMat(false);
				}
			}

			// FINE ==================================================================


			// INIZIO ==================================================================
			// Controllo profilo per localizzazione oggetto
			// solo per le catalogazioni in Indice in Polo non esite localizzazione per Gestione quindi è inutile
			if (areaDatiPass.isInserimentoIndice()) {
				SbnTipoLocalizza sbnTipoLoc = null;
				if (this.profiler.isOkAttivita("Localizza",	"Localizza per gestione", ticket)
						&& this.profiler.isOkAttivita("Localizza", "Localizza per posseduto", ticket)) {
					sbnTipoLoc = SbnTipoLocalizza.TUTTI;
				}
				if (sbnTipoLoc == null) {
					risposta.setCodErr("9999");
					risposta.setTestoProtocollo("Utente non abilitato alla localizzazione "
							+ "per Gest/Poss per l'oggetto " + areaDatiPass.getDetTitoloPFissaVO().getBid());
					return risposta;
				}
			}

			// FINE ==================================================================

			risposta = gestioneTitoli.inserisciTitolo(areaDatiPass);
			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO inserisciLegameTitolo(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)
					&& this.profiler.isOkAttivita("Localizza",
							"Localizza per posseduto", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.TUTTI;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getIdArrivo());
				return risposta;
			}

			risposta = gestioneTitoli.inserisciLegameTitolo(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO trascinaLegameAutore(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(), getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",	"Localizza per gestione", ticket)
					&& this.profiler.isOkAttivita("Localizza", "Localizza per posseduto", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.TUTTI;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getIdArrivo());
				return risposta;
			}

			risposta = gestioneTitoli.trascinaLegameAutore(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO scambiaResponsabilitaLegameTitoloAutore(
			AreaDatiScambiaResponsLegameTitAutVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			risposta = gestioneTitoli
			.scambiaResponsabilitaLegameTitoloAutore(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	// AREA AUTORI
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutori(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneAutori.ricercaAutori(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoAutorePerVid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			logger.debug("richiesta analitica per vid: " + areaDatiPass.getBidRicerca());
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
			risposta = gestioneAutori.creaRichiestaAnaliticoAutorePerVid(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoAutori(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneAutori.getNextBloccoAutori(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO inserisciAutore(
			AreaDatiVariazioneAutoreVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));

			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			String livello = areaDatiPass.getDettAutoreVO().getLivAut();
			boolean esito = this.profiler.isOkControlloLivelloAutorita(livello,
					SbnAuthority.AU, ticket);
			if (!esito) {
				risposta.setCodErr("livAutInvalido");
				return risposta;
			}

			if (areaDatiPass.isConferma()) {
				if (!this.profiler.isForzaturaAuthority(SbnAuthority.AU,
						ticket)) {
					risposta.setCodErr("ins037");
					return risposta;
				}
			}

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getDettAutoreVO().getVid());
				return risposta;
			}

			risposta = gestioneAutori.inserisciAutore(areaDatiPass);
			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutoriCollegati(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneAutori.ricercaAutoriCollegati(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO collegaElementoAuthority(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();

			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getIdArrivo());
				return risposta;
			}

			risposta = gestioneAutori.collegaElementoAuthority(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO creaFormaRinvio(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getIdArrivo());
				return risposta;
			}

			risposta = gestioneAutori.creaFormaRinvio(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO scambiaForma(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			risposta = gestioneAutori.scambiaForma(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	// AREA MARCHE
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarche(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneMarcheDao gestioneMarche = new SbnGestioneMarcheDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneMarche.ricercaMarche(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoMarchePerMid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneMarcheDao gestioneMarche = new SbnGestioneMarcheDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
			risposta = gestioneMarche
			.creaRichiestaAnaliticoMarchePerMid(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoMarche(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneMarcheDao gestioneMarche = new SbnGestioneMarcheDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneMarche.getNextBloccoMarche(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO inserisciMarca(
			AreaDatiVariazioneMarcaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneMarcheDao gestioneMarche = new SbnGestioneMarcheDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getDettMarcaVO().getMid());
				return risposta;
			}

			risposta = gestioneMarche.inserisciMarca(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarcheCollegate(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneMarcheDao gestioneMarche = new SbnGestioneMarcheDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneMarche.ricercaMarcheCollegate(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLuoghi(
			AreaDatiPassaggioInterrogazioneLuogoVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneLuoghiDao gestioneLuoghi = new SbnGestioneLuoghiDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneLuoghi.ricercaLuoghi(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoLuoghi(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneLuoghiDao gestioneLuoghi = new SbnGestioneLuoghiDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			risposta = gestioneLuoghi.getNextBloccoLuoghi(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoLuoghiPerLid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass,
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneLuoghiDao gestioneLuoghi = new SbnGestioneLuoghiDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO risposta = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
			risposta = gestioneLuoghi
			.creaRichiestaAnaliticoLuoghiPerLid(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazioneReturnVO inserisciLuogo(
			AreaDatiVariazioneLuogoVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneLuoghiDao gestioneLuoghi = new SbnGestioneLuoghiDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazioneReturnVO risposta = new AreaDatiVariazioneReturnVO();
			// Controllo profilo per localizzazione oggetto
			SbnTipoLocalizza sbnTipoLoc = null;
			if (this.profiler.isOkAttivita("Localizza",
					"Localizza per gestione", ticket)) {
				sbnTipoLoc = SbnTipoLocalizza.GESTIONE;
			}
			if (sbnTipoLoc == null) {
				risposta.setCodErr("9999");
				risposta
				.setTestoProtocollo("Utente non abilitato alla localizzazione "
						+ "per Gest/Poss per l'oggetto "
						+ areaDatiPass.getDettLuogoVO().getLid());
				return risposta;
			}

			risposta = gestioneLuoghi.inserisciLuogo(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AllineaVO richiediListaAllineamenti(
			AllineaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllineamentiIndicePoloDao gestioneAllineamentiIndicePolo = new SbnGestioneAllineamentiIndicePoloDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AllineaVO risposta = new AllineaVO();
			risposta = gestioneAllineamentiIndicePolo
			.richiediListaAllineamenti(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public CatturaMassivaBatchVO  catturaMassivaBatch(
			CatturaMassivaBatchVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			CatturaMassivaBatchVO risposta = new CatturaMassivaBatchVO();

			SbnGestioneAllineamentiIndicePoloDao gestioneAllineamentiIndicePolo = new SbnGestioneAllineamentiIndicePoloDao(
						getFactoryIndice(), getFactoryPolo(),
						getSbnUserType(ticket));
			risposta = gestioneAllineamentiIndicePolo.catturaMassivaBatch(areaDatiPass);

			return risposta;

		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AllineaVO allineaBaseLocale(
			AllineaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllineamentiIndicePoloDao gestioneAllineamentiIndicePolo = new SbnGestioneAllineamentiIndicePoloDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AllineaVO risposta = new AllineaVO();
			risposta = gestioneAllineamentiIndicePolo
			.allineaBaseLocale(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AllineaVO allineamentoRepertoriDaIndice(
			String ticket) throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneAllineamentiIndicePoloDao gestioneAllineamentiIndicePolo = new SbnGestioneAllineamentiIndicePoloDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AllineaVO risposta = gestioneAllineamentiIndicePolo.allineamentoRepertoriDaIndice(ticket);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiPropostaDiCorrezioneVO cercaPropostaDiCorrezione(
			AreaDatiPropostaDiCorrezioneVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneProposteDiCorrezioneDao gestioneProposteDiCorrezione = new SbnGestioneProposteDiCorrezioneDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiPropostaDiCorrezioneVO risposta = new AreaDatiPropostaDiCorrezioneVO();
			risposta = gestioneProposteDiCorrezione
			.cercaPropostaDiCorrezione(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
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
	 *
	 */
	public AreaDatiVariazionePropostaDiCorrezioneVO inserisciPropostaDiCorrezione(
			AreaDatiVariazionePropostaDiCorrezioneVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnGestioneProposteDiCorrezioneDao gestioneProposteDiCorrezione = new SbnGestioneProposteDiCorrezioneDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));
			AreaDatiVariazionePropostaDiCorrezioneVO risposta = new AreaDatiVariazionePropostaDiCorrezioneVO();
			risposta = gestioneProposteDiCorrezione
			.inserisciPropostaDiCorrezione(areaDatiPass);

			return risposta;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}

	// AREA CATTURA
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaParametriStampaSchedeVo schedulatorePassiStampaSchede(
			AreaParametriStampaSchedeVo areaDatiPass, String ticket, BatchLogWriter log)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnStampaSchedeDao stampaSchede = new SbnStampaSchedeDao(getFactoryIndice(),
					new FactorySbnEJB("polo"),//getFactoryPolo(),
					getSbnUserType(ticket));

			areaDatiPass = stampaSchede.schedulatorePassiStampaSchede(areaDatiPass, log);

			return areaDatiPass;
		} catch (RemoteException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			logger.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			logger.error(e.getMessage());
			throw new DAOException(e);
		}

	}


	public StampaTitoliEditoreVO getTitoliEditoreXls(StampaTitoliEditoreVO input, String ticket, BatchLogWriter log)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		StampaTitoliEditoreVO output = new StampaTitoliEditoreVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
		Tbf_biblioteca_in_polo biblioteca = null;
		String descrBib = null;
		// fare getBiblioteca
		TitoloVO rec = null;
		StampaTitoliEditoreDettaglioVO steDettaglio = null;
		List listaInv = new ArrayList();
		try{
			//			output = this.controllaInputStRegCons(input, output);

			daoBib = new Tbf_biblioteca_in_poloDao();
			biblioteca = daoBib.select(input.getCodPolo(), input.getCodBib());
			if (biblioteca != null){
				descrBib = biblioteca.getDs_biblioteca();
			}else{
				descrBib = "Descrizione biblioteca mancante";
			}
			if (output != null){
				steDettaglio = new StampaTitoliEditoreDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				daoEditore = new Tba_fornitoriDao();
				List<V_catalogo_editoria> listaEditori = daoEditore.getEditori_vistaCatalogoEditoria(input.getCodPolo(), input.getCodBib(),
						input.getCodEditore(),	input.getIsbn(),
						input.getRegione(),	input.getProvincia(),
						input.getDataPubbl1Da(), input.getDataPubbl1A(), input.getTipoRecord(),	input.getLingua(), input.getNatura(),
						input.getCheckTipoPosseduto(), input.getCheckEditore(),	input.getCheckTipoRicerca(), input.getPaese());


					if (ValidazioneDati.isFilled(listaEditori) ) {
						trattamentoDatiVistaEditori(input, output, listaEditori, ticket);
					}
					output.setDescrBib(descrBib);
				}
		}catch (ValidationException e) {
			e.printStackTrace();
			throw new DataException (e);
		}catch (DaoManagerException e) {
			e.printStackTrace();
			throw new DataException (e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException (e);
		}
		return output;
	}

	private void trattamentoDatiVistaEditori(StampaTitoliEditoreVO input,
			StampaTitoliEditoreVO output, List<V_catalogo_editoria> listaEditori, String ticket)
	throws RemoteException, DaoManagerException {

		StampaTitoliEditoreDettaglioVO steDettaglio = null;
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		Timestamp dataIngressoDa = null;
		Timestamp dataIngressoA = null;
		if (input.getDataIngressoDa() != null &&
				(!input.getDataIngressoDa().trim().equals("") && !input.getDataIngressoDa().equals("00/00/0000"))){
			dataIngressoDa = (DateUtil.toTimestamp(input.getDataIngressoDa()));
			if (!input.getDataIngressoA().trim().equals("") && !input.getDataIngressoA().equals("00/00/0000")){
				dataIngressoA = (DateUtil.toTimestampA(input.getDataIngressoA()));
			}else{
				if (input.getDataIngressoA().trim().equals("") || input.getDataIngressoA().equals("00/00/0000")){
					dataIngressoA = (ts);
				}
			}
		}

		boolean daStampare;
		int size = ValidazioneDati.size(listaEditori);
		for (int i = 0; i < size; i++) {

			daStampare = true;

			//per ogni bid scrivo tanti record quanti sono gli inventari associati
			try{
				BatchManager.getBatchManagerInstance().checkForInterruption(input.getIdBatch());

				V_catalogo_editoria titolo = listaEditori.get(i);
				if (titolo != null) {
					steDettaglio = new StampaTitoliEditoreDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					//mettere controlli sul batch come su patrimonio
					if (titolo.getBid() != null){
						//dati inventario
						// Maggio 2013 - modifiche per filtrare la stampa titolixEditore anche per TipoMaterialeInventariabile
						daoInventario = new Tbc_inventarioDao();
						List<Object []>  numCountInvBib = daoInventario.countInventariPerPossedutoPerDirittoDiStampaOAltroTipoAcq(input.getCodPolo(), input.getCodBib(),
								titolo.getBid(), input.getTipoAcq(), input.getCodiceTipoMateriale(), dataIngressoDa, dataIngressoA);
						if (numCountInvBib.size() > 0){
							for (int ii = 0; ii < numCountInvBib.size(); ii++) {
								CodiceVO rec = new CodiceVO((String)numCountInvBib.get(ii)[0], numCountInvBib.get(ii)[1].toString());								if (rec.getCodice() != null && rec.getCodice().startsWith("diritto")){
									if (rec.getDescrizione() != null && Integer.valueOf(rec.getDescrizione()) > 0){
										steDettaglio.setDirittoDiStampa(rec.getDescrizione());
									}else{
										steDettaglio.setDirittoDiStampa("");
									}
								}else if (rec.getCodice() != null && rec.getCodice().startsWith("altro")){
									if (rec.getDescrizione() != null && Integer.valueOf(rec.getDescrizione()) > 0){
										steDettaglio.setAltriTipiAcq(rec.getDescrizione());
									}else{
										steDettaglio.setAltriTipiAcq("");
									}
								}
							}

						}else if (numCountInvBib.size() == 0){
							steDettaglio.setDirittoDiStampa("");
							steDettaglio.setAltriTipiAcq("");
						}
						//dati fornitore
						steDettaglio.setEditore(String.valueOf(titolo.getCod_fornitore()));
						steDettaglio.setNomeEditore(titolo.getNom_fornitore().trim());
						//dati titolo
						//almaviva5_20131003
						buildCittaPaese(steDettaglio, titolo);


						steDettaglio.setPaese(titolo.getPaese());
						steDettaglio.setDescrPaese(titolo.getPaese().trim());

						steDettaglio.setIsbn(titolo.getIsbn().trim());
						steDettaglio.setDataPubbl(titolo.getAa_pubb_1());
						steDettaglio.setLingua(titolo.getCd_lingua_1());
						steDettaglio.setBid(titolo.getBid());
						TitoloDAO dao = new TitoloDAO();
						String isbd = dao.concatenaAreeTitolo(titolo.getBid(), new String[] {"200", "210", "215"}, 87, true);
						steDettaglio.setTitolo(isbd);
						steDettaglio.setTipoRecord(""+titolo.getTp_record_uni());
						steDettaglio.setDescrTipoRecord(CodiciProvider.cercaDescrizioneCodice(titolo.getTp_record_uni(),
								CodiciType.CODICE_GENERE_MATERIALE_UNIMARC,
								CodiciRicercaType.RICERCA_CODICE_SBN));

						// Maggio 2013 - Modifiche a Stampa titoli x editore x inserire il controllo su presenza filtro x classificazione;
						// impostazione filtro e controllo risultati
						steDettaglio.setClasse("");
						steDettaglio.setSimbolo("");
						if (ValidazioneDati.isFilled(input.getSistema())) {
							CatSemClassificazioneVO classificazione = null;
							daoSemantica = new SemanticaDAO();
							CountLegamiSemanticaVO numLegami = daoSemantica.countLegamiSemantica(titolo.getBid(), "D");
							if (numLegami != null) {
								//classi
								if (numLegami.getCountLegamiClasse() > 0){
									classificazione = trattaClasificazioneTitEdi(ticket, steDettaglio, classificazione,	numLegami);
									SinteticaClasseVO classe = classificazione.getListaClassi().get(0);
									if (classe.getSistema().equals(input.getSistema())
											&& classe.getSimbolo().contains(input.getSimbolo())) {
										steDettaglio.setClasse(classe.getIdentificativoClasse().substring(0, 3));
//										steDettaglio.setSimbolo(classificazione.getListaClassi().get(0).getIdentificativoClasse().substring(3, 5));
										steDettaglio.setSimbolo(classe.getIdentificativoClasse().substring(3));
									} else {
										daStampare = false;
									}
								} else {
									daStampare = false;
								}
							}
						}
					}
				}else{
					output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
					output.getErrori().add("lista titoli editore vuota");
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
				output.getErrori().add("ECCEZIONE: " + e.getMessage());
			}
			if (daStampare)
				output.getLista().add(steDettaglio);
		}
		output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
		output.getErrori().add("Ora fine = " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
		return ;
	}

	private void buildCittaPaese(StampaTitoliEditoreDettaglioVO steDettaglio, V_catalogo_editoria titolo) throws Exception {
		StringBuilder buf = new StringBuilder(64);
		String citta = ValidazioneDati.trimOrEmpty(titolo.getCitta());
		String prov = ValidazioneDati.trimOrEmpty(titolo.getProvincia());
		String reg = ValidazioneDati.trimOrEmpty(titolo.getDs_regione());
		String paese = ValidazioneDati.trimOrEmpty(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_PAESE, titolo.getPaese()));
		if (ValidazioneDati.isFilled(citta))
			buf.append(citta);
		if (ValidazioneDati.isFilled(citta) && ValidazioneDati.isFilled(prov))
			buf.append(" (").append(prov).append(')');
		if (!ValidazioneDati.isFilled(citta) && ValidazioneDati.isFilled(prov))
			buf.append(titolo.getDs_provincia());

		steDettaglio.setComune(citta);
		steDettaglio.setDescrComune(buf.toString());

		buf.setLength(0);
		if (ValidazioneDati.isFilled(reg))
			buf.append(reg);
		if (ValidazioneDati.isFilled(reg) && ValidazioneDati.isFilled(paese))
			buf.append(" - ").append(paese);
		if (!ValidazioneDati.isFilled(reg) && ValidazioneDati.isFilled(paese))
			buf.append(paese);

		steDettaglio.setProvincia(titolo.getProvincia());
		steDettaglio.setDescrProvincia(buf.toString());

	}

	/**
	 * @param ticket
	 * @param bollettinoDettaglio
	 * @param classificazione
	 * @param numLegami
	 * @return
	 */
	private CatSemClassificazioneVO trattaClasificazioneTitEdi(String ticket,
			StampaTitoliEditoreDettaglioVO steDettaglio,
			CatSemClassificazioneVO classificazione,
			CountLegamiSemanticaVO numLegami) {
		try{
			classificazione = semantica.ricercaClassiPerBidCollegato(true, numLegami.getBid(), "", "", ticket, 0);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (classificazione != null && classificazione.getListaClassi().size() > 0){
			CodiceVO rec = null;
			for (int index2 = 0; index2 < classificazione.getListaClassi().size(); index2++) {
				SinteticaClasseVO classif = classificazione.getListaClassi().get(0);
				rec = new CodiceVO();
				rec.setCodice("");
				rec.setDescrizione(classif.getIdentificativoClasse());
				steDettaglio.getIndiciClassificazione().add(rec);
			}
		}else{
			steDettaglio.setIndiciClassificazione(null);
		}
		return classificazione;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ApplicationException {
		try {
			if (command == null)
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

			command.validate();
			String ticket = command.getTicket();
			checkTicket(ticket);

			switch (command.getCommand() ) {
			case GB_LOCALIZZAZIONI_PENDENTI:
				LocalizzaDAO dao = new LocalizzaDAO();
				int locPendenti = dao.countLocalizzazioniPendenti();
				return CommandResultVO.build(command, locPendenti, null);

			default:
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_INVALID_COMMAND);
}

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);
		} catch (ValidationException e) {
			throw new ApplicationException(e.getErrorCode());
		} catch (Exception e) {
			logger.error("", e);
			ctx.setRollbackOnly();
			if (e instanceof SbnBaseException)
				return CommandResultVO.build(command, null, e);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}


}
