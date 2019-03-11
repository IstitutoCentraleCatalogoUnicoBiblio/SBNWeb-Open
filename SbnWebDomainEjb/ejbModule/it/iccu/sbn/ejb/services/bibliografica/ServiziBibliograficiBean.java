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
package it.iccu.sbn.ejb.services.bibliografica;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnEJB;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnHttp;
import it.iccu.sbn.SbnMarcFactory.factory.ServerHttp;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneTitoliDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnScaricaFileAllineamentiDaIndiceDao;
import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.allineamenti.Allineamenti;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.allineamenti.LocalizzazioneMassiva;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.bibliografica.ImportazioneLegamiBidAltroId;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ImportaLegamiBidAltroIdVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.LocalizzazioneMassivaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.FusioneMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.custom.Credentials;
import it.iccu.sbn.vo.custom.bibliografica.AreaDatiServizioInterrTitoloCusVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.domain.bibliografica.AreaDatiServizioInterrTitoloDomVO;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="ServiziBibliografici" description="A session bean named
 *           ServiziBibliografici" display-name="ServiziBibliografici"
 *           jndi-name="sbnWeb/ServiziBibliografici" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class ServiziBibliograficiBean extends TicketChecker implements ServiziBibliografici {

	private static final long serialVersionUID = 2788295398256501168L;

	private static Logger log = Logger.getLogger(ServiziBibliografici.class);

	private AmministrazionePolo polo;
	private AmministrazioneBibliotecario bibliotecario;
	private Profiler profiler;

	private SessionContext ctx;

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {

	}

	public void setSessionContext(SessionContext ctx) throws EJBException,
			RemoteException {
		this.ctx = ctx;
		try {
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
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
		ParametriHttp paramIndice = this.polo.getIndice();
//		FactorySbn indice = new FactorySbn(new ServerHttp(paramIndice,
//				getCredentials()));
		FactorySbn indice = new FactorySbnHttp("Indice", new ServerHttp(paramIndice,
				getCredentials()));
		return indice;
	}

	private FactorySbn getFactoryPolo() throws RemoteException,
			DaoManagerException, SbnMarcException {

		FactorySbn indice = new FactorySbnEJB("polo");;
		//FactorySbn indice = new FactorySbnJMS(new ServerJMS(getCredentials()));

		return indice;
	}

	private SbnUserType getSbnUserType(String ticket) throws RemoteException,
			DaoManagerException {
		SbnUserType sbn = this.bibliotecario.getUserSbnMarc(ticket);
		return sbn;
	}

	private FactorySbn getFactoryIndiceAllinea() throws Exception {
		ParametriHttp paramIndice = this.polo.getIndice();
		//almaviva5_20111222 segnalazione TO0: aumentato timeout per risposta indice in allineamento da 15 minuti a 1 ora.
		int timeout = CommonConfiguration.getPropertyAsInteger(Configuration.SBNMARC_INDICE_ALLINEA_REQUEST_TIMEOUT, 0);
		if (timeout > 0)
			paramIndice.setHTTP_REQUEST_TIMEOUT(timeout); //1 ora.

		FactorySbn indice = new FactorySbnHttp("Indice", new ServerHttp(paramIndice, getCredentials()));
		return indice;
	}

	// Servizio che su bid riporta i dati relativi al titolo
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public AreaDatiServizioInterrTitoloCusVO servizioDatiTitoloPerBid(
			String bid, String ticket)
			throws DAOException {
		try {
			SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));

			AreaDatiServizioInterrTitoloDomVO risposta = new AreaDatiServizioInterrTitoloDomVO();
			risposta = gestioneTitoli.servizioDatiTitoloPerBid(bid);
			AreaDatiServizioInterrTitoloCusVO ret = new AreaDatiServizioInterrTitoloCusVO();

			if (risposta.getCodErr().equals("0000")) {
				ret.setBid(risposta.getBid());
				ret.setCodErr(risposta.getCodErr());
				ret.setLivAut(risposta.getLivAut());
				ret.setNatura(risposta.getNatura());
				ret.setTestoProtocollo(risposta.getTestoProtocollo());
				ret.setTimeStamp(risposta.getTimeStamp());
				ret.setTipoAuthority(risposta.getTipoAuthority());
				ret.setTipoMateriale(risposta.getTipoMateriale());
				ret.setTitoloResponsabilita(risposta.getTitoloResponsabilita());
				ret.setCodPaese(risposta.getCodPaese());
				ret.setArrCodLingua(risposta.getArrCodLingua());
			} else {
				ret.setBid(risposta.getBid());
				ret.setCodErr(risposta.getCodErr());
				ret.setTestoProtocollo(risposta.getTestoProtocollo());
			}
			return ret;
		} catch (RemoteException e) {
			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {
			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new DAOException(e);
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
	public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List parametri) throws ApplicationException,
			DataException, ValidationException, EJBException {

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		stampeOnline.setParametriInput(parametri);

		switch (tipoStampa) {
			default:
				break;
		}

		return stampeOnline;
	}

	public ElaborazioniDifferiteOutputVo localizzazioneMassiva(
			LocalizzazioneMassivaVO richiesta, BatchLogWriter blw) throws ApplicationException, ValidationException,
			EJBException {
		LocalizzazioneMassiva locMassiva = new LocalizzazioneMassiva(ctx.getUserTransaction(), richiesta, blw);
		return locMassiva.execute();
}

	public ElaborazioniDifferiteOutputVo allineamenti(
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter blw) throws ApplicationException, ValidationException,
			EJBException {

		UserTransaction tx = ctx.getUserTransaction();
		ElaborazioniDifferiteOutputVo out = new ElaborazioniDifferiteOutputVo(params);

		try {
			//almaviva5_20140626 #5589 #5595
			DaoManager.begin(tx);
			Allineamenti allineamenti = new Allineamenti(getFactoryIndiceAllinea(),
					getFactoryPolo(), getSbnUserType(params.getTicket()), tx);
			String prefissoOutput = params.getFirmaBatch();

			String cdAttivita = params.getCodAttivita();
			if (cdAttivita.equals(CodiciAttivita.getIstance().ALLINEAMENTI_1032)) {
				AllineaVO allineaVo = (AllineaVO) params;
				out = allineamenti.allinea(prefissoOutput, allineaVo, blw);
}

			if (cdAttivita.equals(CodiciAttivita.getIstance().CATTURA_MASSIVA)) {
				CatturaMassivaBatchVO catturaMassivaBatchVO = (CatturaMassivaBatchVO) params;
				out =  allineamenti.catturaMassivaBatch(prefissoOutput, catturaMassivaBatchVO, blw);
			}

			if (cdAttivita.equals(CodiciAttivita.getIstance().FUSIONE_MASSIVA)) {
				FusioneMassivaBatchVO fusioneMassivaBatchVO = (FusioneMassivaBatchVO) params;
				out =  allineamenti.fusioneMassivaBatch(prefissoOutput, fusioneMassivaBatchVO, blw);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			DaoManager.endTransaction(tx, ValidazioneDati.equals(out.getStato(), ConstantsJMS.STATO_OK));
		}

		return out;
	}

	public ElaborazioniDifferiteOutputVo importaLegamiBidOcn(ImportaLegamiBidAltroIdVO richiesta, BatchLogWriter blw)
			throws ApplicationException, ValidationException, RemoteException {

		ImportazioneLegamiBidAltroId importa = new ImportazioneLegamiBidAltroId(ctx.getUserTransaction(), richiesta, blw);
		return importa.execute();
	};

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DAOException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 *
	 */
	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
	public AllineaVO scaricaFileAllineamentoBaseLocale(
			AllineaVO areaDatiPass, String ticket)
	throws DAOException {
		try {
			checkTicket(ticket);
			SbnScaricaFileAllineamentiDaIndiceDao scaricaFileAllineamentiDaIndice = new SbnScaricaFileAllineamentiDaIndiceDao(
					getFactoryIndice(), getFactoryPolo(),
					getSbnUserType(ticket));

			AllineaVO risposta = scaricaFileAllineamentiDaIndice.trasferisciFileInPolo(areaDatiPass);

			return risposta;

		} catch (RemoteException e) {

			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (DaoManagerException e) {

			log.error(e.getMessage());
			throw new DAOException(e);
		} catch (SbnMarcException e) {

			e.printStackTrace();
			log.error(e.getMessage());
			throw new DAOException(e);
		}

	}

}
