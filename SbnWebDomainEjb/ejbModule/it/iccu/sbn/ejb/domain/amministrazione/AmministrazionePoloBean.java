/**
 *
 */
package it.iccu.sbn.ejb.domain.amministrazione;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.AttivitaAbilitateType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SottoAttivita;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ActionPathVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.AreaBatchVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ElaborazioniDifferiteConfig;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.TipoAttivita;
import it.iccu.sbn.ejb.vo.validation.JSScriptVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniDAO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_batch_serviziDAO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecarioDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_poloDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_batch_servizi;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_coda_jms;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_js_script;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_gruppo_attivita_polo;
import it.iccu.sbn.servizi.ProfilerManager;
import it.iccu.sbn.servizi.ProfilerManagerMBean;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.batch.BatchManagerMBean;
import it.iccu.sbn.servizi.batch.job.BatchCleanerControllerJob;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;
import it.iccu.sbn.util.profiler.SbnWebProfileCache;
import it.iccu.sbn.util.sync.ReadWriteLockedWrapper;
import it.iccu.sbn.util.validation.JsScriptSession;
import it.iccu.sbn.util.validation.JsScriptSessionImpl;
import it.iccu.sbn.vo.custom.Credentials;
import it.iccu.sbn.vo.custom.Polo;
import it.iccu.sbn.vo.custom.amministrazione.BatchAttivazioneVO;
import it.iccu.sbn.vo.custom.amministrazione.OrderedTreeElement;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.domain.ParametriPoloVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.mail.internet.InternetAddress;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.jboss.mx.util.MBeanServerLocator;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc --> * <!--
 * begin-xdoclet-definition -->
 *
 * @ejb.bean name="AmministrazionePolo"
 * 		description="A session bean named AmministrazionePolo"
 * 		display-name="AmministrazionePolo"
 *      jndi-name="sbnWeb/AmministrazionePolo"
 *      type="Stateless"
 *      transaction-type="Container"
 *      view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class AmministrazionePoloBean extends AmministrazioneBaseBean implements AmministrazionePolo {

	private static final long serialVersionUID = -6895499094650443759L;

	private static Logger log = Logger.getLogger(AmministrazionePoloBean.class);

	private static final transient ParametriPoloVO conf = ReadWriteLockedWrapper.wrap(ParametriPoloVO.class);

	private static final String ATTIVITA_IGNORATE_PATH = "/META-INF/attivita_ignorate.properties";

	private Profiler profiler;
	private AmministrazioneMail amministrazioneMail;

	private String buildTime;

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
		return;
	}

	public AmministrazioneMail getAmministrazioneMail() {
		if (amministrazioneMail != null)
			return amministrazioneMail;

		try {
			amministrazioneMail = DomainEJBFactory.getInstance().getAmministrazioneMail();
		} catch (CreateException e) {
			log.error("", e);
			throw new EJBException("300",e);
		} catch (NamingException e) {
			log.error("", e);
			throw new EJBException("300",e);
		} catch (RemoteException e) {
			log.error("", e);
			throw new EJBException("300",e);
		}

		return amministrazioneMail;
	}


	public void setSessionContext(SessionContext ctx) throws EJBException,
			RemoteException {

		this.ctx = ctx;
		/*
		try {
			this.loadParameterPolo();
		} catch (DaoManagerException e) {
			log.error("", e);
		}
		*/
		if (conf.getURL_INDICE() != null)
			log.debug("ServerSBN.URL_SERVLET: '" + conf.getURL_INDICE() + "'");

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

	private Profiler getProfiler() {
		if (profiler != null)
			return profiler;

		try {
			this.profiler = DomainEJBFactory.getInstance().getProfiler();
			return profiler;

		} catch (Exception e) {
			log.error(e);
			return null;
		}

	}

	private boolean isSbnMarcPoloDeployed() {
		try {
			MBeanServer mbserver = MBeanServerLocator.locateJBoss();
			ObjectName name = new ObjectName("sbn.service:name=SbnMarcPoloBeans");
			ObjectInstance instance = mbserver.getObjectInstance(name);

			return (instance != null);

		} catch (Exception e) {
			log.error("isSbnMarcPoloDeployed(): " +  e.toString() );
			return false;
		}
	}

	private String getSbnMarcBuildTime() throws DaoManagerException {
		try {
			if (ValidazioneDati.isFilled(buildTime))
				return buildTime;

			if (isSbnMarcPoloDeployed() ) {
				buildTime = (String) getGateway().service("buildTime");

				return buildTime;
			}

			return DateUtil.now();
		}
		catch (Exception e) {
			resetGateway();
			log.error("", e);
			return DateUtil.now();
		}
	}

	public PoloVO getInfoPolo() throws DaoManagerException {
		Tbf_poloDao dao = new Tbf_poloDao();
		List<Tbf_polo> polo = dao.selectAll();
		Tbf_polo tb_polo = polo.get(0);
		PoloVO poloVO = new PoloVO(tb_polo);
		poloVO.setSbnMarcBuildTime(getSbnMarcBuildTime() );

		//almaviva5_20140905
		String buildTime = DateUtil.now();
		String buildNumber = "0";
		try {
			Properties properties = new Properties() ;
			properties.load(this.getClass().getResourceAsStream("/build_time.properties"));
			buildTime = properties.getProperty(Constants.BUILD_TIME);
			buildNumber = properties.getProperty(Constants.BUILD_NUMBER);

		} catch (Exception e) {}

		poloVO.setSbnWebBuildTime(buildTime);
		poloVO.setSbnWebBuildNumber(buildNumber);

		return poloVO;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws RemoteException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */

	public void loadParameterPolo() throws DaoManagerException {
		Tbf_poloDao dao = new Tbf_poloDao();
		List<Tbf_polo> polo = dao.selectAll();
		Tbf_polo tb_polo = polo.get(0);

		Credentials credentials = new Credentials();
		credentials.setUsername(tb_polo.getUsername());
		credentials.setPassword(tb_polo.getPassword());
		conf.setCredentials(credentials);

		conf.setURL_INDICE(tb_polo.getUrl_indice() );
		conf.setURL_POLO(tb_polo.getUrl_polo() );

		SbnUserType sbnusertype = new SbnUserType();
		sbnusertype.setUserId(tb_polo.getUsername_polo());
		sbnusertype.setBiblioteca(tb_polo.getCd_polo() + "   ");
		conf.setSbnusertype(sbnusertype);

		try {
			boolean isDebug = Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.SBNMARC_LOCAL_DEBUG, Boolean.FALSE.toString()));
			String urlPolo = !isDebug ? conf.getURL_POLO() : CommonConfiguration.getProperty(Configuration.DEBUG_SBNMARC_URL, conf.getURL_POLO() );
			//almaviva5_20140116
			if (isDebug) {
				String url_indice = CommonConfiguration.getProperty(Configuration.DEBUG_SBNMARC_URL_INDICE, "" );
				if (ValidazioneDati.isFilled(url_indice))
					conf.setURL_INDICE(url_indice);

				//almaviva5_20160523
				String usr = CommonConfiguration.getProperty(Configuration.DEBUG_SBNMARC_USER_INDICE, "" );
				if (ValidazioneDati.isFilled(usr))
					conf.getCredentials().setUsername(usr);
				String pwd = CommonConfiguration.getProperty(Configuration.DEBUG_SBNMARC_PASSWORD_INDICE, "" );
				if (ValidazioneDati.isFilled(pwd))
					conf.getCredentials().setPassword(pwd);
			}

			CommonConfiguration.setProperty(FactorySbn.URL_POLO, urlPolo);
			CommonConfiguration.setProperty(FactorySbn.URL_INDICE, conf.getURL_INDICE());

			//almaviva5_20150707
			CommonConfiguration.setProperty(FactorySbn.INDICE_USER, conf.getCredentials().getUsername());
			CommonConfiguration.setProperty(FactorySbn.INDICE_PWD, conf.getCredentials().getPassword());

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws RemoteException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public void createPolo(Polo polo) throws DaoManagerException,
			RemoteException {
		Tbf_poloDao dao = new Tbf_poloDao();
		Tbf_polo tb_polo = new Tbf_polo();
		tb_polo.setCd_polo(polo.getCd_polo());
		tb_polo.setUrl_indice(polo.getUrl_indice());
		tb_polo.setUsername(polo.getUsername());
		tb_polo.setPassword(polo.getPassword());
		tb_polo.setTs_var(DaoManager.now());
		tb_polo.setUte_var(polo.getUte_var());
		dao.saveOrUpdate(tb_polo);
		// ;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws RemoteException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public ParametriHttp getPolo() throws DaoManagerException, RemoteException {
		try {
			ParametriHttp parametri = new ParametriHttp();
			parametri.setURL_SERVLET(CommonConfiguration.getProperty(FactorySbn.URL_POLO));
			return parametri;
		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws RemoteException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public ParametriHttp getIndice() throws DaoManagerException, RemoteException {
		try {
			ParametriHttp parametri = new ParametriHttp();
			parametri.setURL_SERVLET(CommonConfiguration.getProperty(FactorySbn.URL_INDICE));

			//almaviva5_20111215 proxy indice
			boolean useProxy = Boolean.valueOf(CommonConfiguration.getProperty(Configuration.SBNMARC_INDICE_USE_PROXY, "false"));
			parametri.setUSE_PROXY(useProxy);
			parametri.setPROXY_URL(CommonConfiguration.getProperty(Configuration.SBNMARC_INDICE_PROXY_URL, "localhost"));
			parametri.setPROXY_PORT(CommonConfiguration.getPropertyAsInteger(Configuration.SBNMARC_INDICE_PROXY_PORT, 8080));
			parametri.setPROXY_USER(CommonConfiguration.getProperty(Configuration.SBNMARC_INDICE_PROXY_USERNAME));
			parametri.setPROXY_PWD(CommonConfiguration.getProperty(Configuration.SBNMARC_INDICE_PROXY_PASSWORD));
			//
			// almaviva5_20201021 timeout connessione
			parametri.setHTTP_CONNECTION_TIMEOUT(CommonConfiguration.getPropertyAsInteger(Configuration.HTTP_CONNECTION_TIMEOUT, 30 * 1000));
			parametri.setHTTP_REQUEST_TIMEOUT(CommonConfiguration.getPropertyAsInteger(Configuration.HTTP_REQUEST_TIMEOUT, 15 * 60 * 1000));
			parametri.setHTTP_NUMERO_TENTATIVI(CommonConfiguration.getPropertyAsInteger(Configuration.HTTP_NUMERO_TENTATIVI, 2));

			log.debug("parametri HTTP: " + parametri);

			return parametri;
		} catch (Exception e) {
			throw new DaoManagerException(e);
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws RemoteException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public Credentials getCredentials() throws DaoManagerException,
			RemoteException {
		return conf.getCredentials();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public SbnUserType getUserSbnMarc() throws DaoManagerException {
		return conf.getSbnusertype();
	}


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public void loadCodiciAttivita() throws DaoManagerException {
		Class<?> cls = null;
		CodiciAttivita codici = null;

		Tbf_poloDao dao = new Tbf_poloDao();
		List<Tbf_attivita> attivita = dao.loadCodiciAttivita(); // Carica tutti i codici attività

		try {
			for (Tbf_attivita tbf_attivita : attivita) {

				String classe_java_sbnmarc = tbf_attivita.getClasse_java_sbnmarc();
				if (ValidazioneDati.strIsNull(classe_java_sbnmarc))
					continue;

				// Instanzia oggetto di classe CodiciAttivita se ancora non instanziato
				if (cls == null) {
					cls = Class.forName("it.iccu.sbn.vo.domain.CodiciAttivita");
					codici = (CodiciAttivita) cls.newInstance();
				}

				// Per ogni campo valorizzo il suo contenuto con i dati del DB
				// sovrascrivendo quelli dichiarati esplicitamente
				try {
					Field fld = cls.getField(classe_java_sbnmarc.substring(classe_java_sbnmarc.lastIndexOf('.') + 1));
				fld.set(codici, tbf_attivita.getCd_attivita().trim());
				} catch (NoSuchFieldException e) {
					continue;
			}
			}
			CodiciAttivita.save(codici);

		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public List<BatchAttivazioneVO> getBatchServiziAll() throws DaoManagerException {
		List<BatchAttivazioneVO> result = new ArrayList<BatchAttivazioneVO>();

		Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
		List<Tbf_batch_servizi> list = dao.selectAll();

			for (Tbf_batch_servizi tbf_batch : list)
			try {
				BatchAttivazioneVO batch = new BatchAttivazioneVO();

				Tbf_attivita cd_attivita = tbf_batch.getCd_attivita();
				batch.setJobName(cd_attivita.getCd_attivita());
				batch.setGroupName(cd_attivita.getCd_attivita());
				batch.setTriggerName(cd_attivita.getCd_attivita());
				batch.setCod_attivita(cd_attivita.getCd_attivita());
				batch.setDescrizione(cd_attivita.getId_attivita_sbnmarc().getDs_attivita());
				batch.setCronExpression(tbf_batch.getId_coda_input().getCron_expression() );
				batch.setCoda_input(tbf_batch.getId_coda_input().getNome_jms());

				result.add(batch);

		} catch (ObjectNotFoundException e) {
			log.error("errore caricamento configurazione batch: ", e);
		}

		Collections.sort(result);
		return result;
	}


	/**
	 * Interroga la coda per prelevare i messaggi in stato "OK" and "ERROR"
	 *
	 * @param Map parametri
	 *  KEY,VALUE
	 *
	 *  Attualemente interroga su una coda
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */

	public List<Message> getRichieste(String ticket, Queue queue, String baseSelector, Map<String, Object> param)
		throws DaoManagerException, EJBException {
		List<Message> receive = null;

		try {
			JMSUtil jms = new JMSUtil(JNDIUtil.getContext() );

			String messageSelector = this.parseMessageSelector(param);
	    		if (ValidazioneDati.isFilled(messageSelector))
	    			messageSelector = ValidazioneDati.isFilled(baseSelector) ? (baseSelector + " and " + messageSelector) : messageSelector;
	    		else
	    			messageSelector = ValidazioneDati.isFilled(baseSelector) ? baseSelector : null;

	    	log.info("getRichieste() coda: " + queue.getQueueName() + " selector: " + messageSelector);

	    	if (!ValidazioneDati.isFilled(messageSelector) )
		    	receive = jms.browseQueue(queue);
	    	else
	    		receive = jms.browseQueue(queue, messageSelector);

	    	return receive;

		} catch (NamingException e) {
			log.error("", e);
			throw new DaoManagerException(e);
		} catch (JMSException e) {
			log.error("", e);
			throw new DaoManagerException(e);
		}
	}

	private String parseMessageSelector(Map<String, Object> params)	{
		StringBuilder selector = new StringBuilder();
		Iterator<String> i = params.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			Object value =  params.get(key);
			if (value instanceof Number)
				selector.append(key).append("=").append(value).append(" ");
			else
				selector.append(key).append("='").append(value).append("' ");

			if (i.hasNext())
				selector.append("AND ");
		}
		return selector.toString();
	}

	static class StringComparator implements Comparator<GruppoParametriVO> {
		public final int compare(GruppoParametriVO gp1, GruppoParametriVO gp2) {

			String sa = gp1.getCodice();
			String sb = gp2.getCodice();
			return ((sa).compareToIgnoreCase(sb)); // Ascending

		} // end compare
	} // end class StringComparator


	public List getElencoParametri() throws DaoManagerException, RemoteException {
		Tbf_poloDao dao = new Tbf_poloDao();
		int idParametro = dao.getIdParametro();
		Tbf_parametro parametri = dao.getParametri(idParametro);
		Object auth[] = parametri.getTbf_par_auth().toArray();
		Object semantica[] = parametri.getTbf_par_sem().toArray();
		Object parMat[] = parametri.getTbf_par_mat().toArray();
		if (auth.length == 0) {
			parametri = dao.getParametri(1);
			auth = parametri.getTbf_par_auth().toArray();
		}
		if (semantica.length == 0) {
			parametri = dao.getParametri(1);
			semantica = parametri.getTbf_par_sem().toArray();
		}
		if (parMat.length == 0) {
			parametri = dao.getParametri(1);
			parMat = parametri.getTbf_par_mat().toArray();
		}

		List<Tb_codici> authorities = dao.getAuthorities();
		List<Tb_codici> materiali = dao.getMateriali();

		List<GruppoParametriVO> elencoAuth = new ArrayList<GruppoParametriVO>();
		List<GruppoParametriVO> elencoSem = new ArrayList<GruppoParametriVO>();
		List<GruppoParametriVO> elencoParMat = new ArrayList<GruppoParametriVO>();
		List output = new ArrayList();

		//Authority:
		for (int i = 0; i < auth.length; i++) {
			GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
			String codice = ((Tbf_par_auth)auth[i]).getCd_par_auth();
			// Troviamo la descrizione dell'authority
			String descrizione = "";
			for (int ik = 0; ik < authorities.size(); ik++) {
				Tb_codici tb_codici = authorities.get(ik);
				if (tb_codici.getCd_tabella().startsWith(codice))
					descrizione = tb_codici.getDs_tabella();
			}
			GruppoParametriVO.setCodice(codice);
			GruppoParametriVO.setDescrizione(descrizione);
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			char parChar = ((Tbf_par_auth)auth[i]).getTp_abil_auth();
			parametro.setDescrizione("profilo.polo.parametri.abil");
			parametro.setIndex("0");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)auth[i]).getFl_abil_legame();
			parametro.setDescrizione("profilo.polo.parametri.abil_legame");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)auth[i]).getFl_leg_auth();
			parametro.setDescrizione("profilo.polo.parametri.leg_auth");
			parametro.setIndex("2");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			String par = ((Tbf_par_auth)auth[i]).getCd_livello();
			parametro.setDescrizione("profilo.polo.parametri.livello");
			parametro.setIndex("3");
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			List<TB_CODICI> elencoCodici = null;
			try {
				elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
			}
			catch (Exception e){
				log.error("", e);
			}
			for (int k = 1; k < elencoCodici.size(); k++) {
				TB_CODICI tabCodice = elencoCodici.get(k);
				Integer cod1 = Integer.parseInt(tabCodice.getCd_tabella().trim()); //Codice della tabella CODICI
				Integer cod2 = dao.getLivelloParametro(idParametro); //Codice impostato per il parametro
				if (cod1 <= cod2) {
					ComboVO combo = new ComboVO();
					combo.setDescrizione(tabCodice.getCd_tabella() + " " + tabCodice.getDs_tabella());
					combo.setCodice(tabCodice.getCd_tabella());
					elencoCombo.add(combo);
				}
				if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
					parametro.setSelezione(tabCodice.getCd_tabella());
				}
			}
			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("MENU");
			elencoParametri.add(parametro);

//			parametro = new ParametroVO();
//			par = ((Tbf_par_auth)auth[i]).getCd_contr_sim();
//			parametro.setDescrizione("profilo.polo.parametri.contr_sim");
//			parametro.setIndex("4");
//			List<String> opzioniSimil = new ArrayList<String>();
//			opzioniSimil.add("000");
//			opzioniSimil.add("001");
//			opzioniSimil.add("002");
//			parametro.setRadioOptions(opzioniSimil);
//			if (par.trim().equals("0"))
//				parametro.setSelezione("000");
//			else if (par.trim().equals("1"))
//				parametro.setSelezione("001");
//			else if (par.trim().equals("2"))
//				parametro.setSelezione("002");
//			parametro.setTipo("RADIO");
//			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)auth[i]).getFl_abil_forzat();
			parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
			parametro.setIndex("4");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)auth[i]).getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex("5");
			parametro.setRadioOptions(opzioniRadio);
			if (codice.trim().equals("PP") || codice.trim().equals("TH")) {
				parametro.setSelezione("Si'");
				parametro.setCongelato("TRUE");
			}
			else {
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
			}
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			GruppoParametriVO.setElencoParametri(elencoParametri);
			elencoAuth.add(GruppoParametriVO);
		}

		// Ordinamento degli authority e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoAuth, new StringComparator());

		for (int g = 0; g < elencoAuth.size(); g++) {
			elencoAuth.get(g).setIndice(g + "");
		}

		output.add(elencoAuth);

		//Par_mat:
		for (int i = 0; i < parMat.length; i++) {
			GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
			char codice = ((Tbf_par_mat)parMat[i]).getCd_par_mat();
			// Troviamo la descrizione dei materiali
			String descrizione = "";
			for (int ik = 0; ik < materiali.size(); ik++) {
				Tb_codici tb_codici = materiali.get(ik);
				if (tb_codici.getCd_tabella().charAt(0) == codice)
					descrizione = tb_codici.getDs_tabella();
			}
			GruppoParametriVO.setCodice(codice + "");
			GruppoParametriVO.setDescrizione(descrizione);
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			char parChar = ((Tbf_par_mat)parMat[i]).getTp_abilitaz();
			parametro.setDescrizione("profilo.polo.parametri.abil");
			parametro.setIndex("0");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

//			parametro = new ParametroVO();
//			String par = ((Tbf_par_mat)parMat[i]).getCd_contr_sim();
//			parametro.setDescrizione("profilo.polo.parametri.contr_sim");
//			parametro.setIndex("1");
//			List<String> opzioniSimil = new ArrayList<String>();
//			opzioniSimil.add("000");
//			opzioniSimil.add("001");
//			opzioniSimil.add("002");
//			parametro.setRadioOptions(opzioniSimil);
//			if (par.trim().equals("0"))
//				parametro.setSelezione("000");
//			else if (par.trim().equals("1"))
//				parametro.setSelezione("001");
//			else if (par.trim().equals("2"))
//				parametro.setSelezione("002");
//			parametro.setTipo("RADIO");
//			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_mat)parMat[i]).getFl_abil_forzat();
			parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			String par = ((Tbf_par_mat)parMat[i]).getCd_livello();
			parametro.setDescrizione("profilo.polo.parametri.livello");
			parametro.setIndex("2");
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			List<TB_CODICI> elencoCodici = null;
			try {
				elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
			}
			catch (Exception e){
				log.error("", e);
			}
			for (int k = 1; k < elencoCodici.size(); k++) {
				TB_CODICI tabCodice = elencoCodici.get(k);
				Integer cod1 = Integer.parseInt(tabCodice.getCd_tabella().trim()); //Codice della tabella CODICI
				Integer cod2 = dao.getLivelloParametro(idParametro); //Codice impostato per il parametro
				if (cod1 <= cod2) {
					ComboVO combo = new ComboVO();
					combo.setDescrizione(tabCodice.getCd_tabella() + " " + tabCodice.getDs_tabella());
					combo.setCodice(tabCodice.getCd_tabella());
					elencoCombo.add(combo);
				}
				if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
					parametro.setSelezione(tabCodice.getCd_tabella());
				}
			}
			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("MENU");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_mat)parMat[i]).getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex("3");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			GruppoParametriVO.setElencoParametri(elencoParametri);
			elencoParMat.add(GruppoParametriVO);

		}

		// Ordinamento dei materiali e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoParMat, new StringComparator());

		for (int g = 0; g < elencoParMat.size(); g++) {
			elencoParMat.get(g).setIndice(g + "");
		}

		output.add(elencoParMat);

		//Semantica:
		for (int i = 0; i < semantica.length; i++) {
			GruppoParametriVO gp = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();

			String codice = "";
			String descrizione = "";
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SCLA")) {
				codice = ((Tbf_par_sem)semantica[i]).getTp_tabella_codici();
				descrizione = "profilo.polo.parametri.classificazioni";
			}
			else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SOGG")) {
				codice = ((Tbf_par_sem)semantica[i]).getTp_tabella_codici();
				descrizione = "profilo.polo.parametri.soggetti";
			}
			else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("STHE")) {
				codice = ((Tbf_par_sem)semantica[i]).getTp_tabella_codici();
				descrizione = "profilo.polo.parametri.thesauri";
			}
			gp.setCodice(codice);
			gp.setDescrizione(descrizione);
			String par = ((Tbf_par_sem)semantica[i]).getCd_tabella_codici();
			gp.setCd_tabella(par);

			if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SCLA"))
				parametro.setDescrizione("profilo.polo.parametri.classificazione");
			else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SOGG"))
				parametro.setDescrizione("profilo.polo.parametri.soggetto");
			else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("STHE"))
				parametro.setDescrizione("profilo.polo.parametri.thesauro");
			parametro.setIndex("0");
//			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			CodiciType cod = null;
			List<TB_CODICI> elencoCodici = null;
			try {
				cod = CodiciType.fromString(codice);
				elencoCodici = CodiciProvider.getCodici(cod);
			}
			catch (Exception e){
				log.error("", e);
			}
			for (int k = 1; k < elencoCodici.size(); k++) {
				TB_CODICI tabCodice = elencoCodici.get(k);
//				ComboVO combo = new ComboVO();
//				combo.setDescrizione(tabCodice.getDs_tabella());
//				combo.setCodice(tabCodice.getCd_tabella());
//				elencoCombo.add(combo);
				if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
					parametro.setSelezione(tabCodice.getDs_tabella());
				}
			}
//			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("TESTO");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			char parChar = ((Tbf_par_sem)semantica[i]).getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			gp.setElencoParametri(elencoParametri);
			elencoSem.add(gp);
		}

		// Ordinamento dei materiali e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoSem, new StringComparator());

		for (int g = 0; g < elencoSem.size(); g++) {
			elencoSem.get(g).setIndice(g + "");
		}

		output.add(elencoSem);

		return output;

	}

	public String[] getElencoAttivitaProfilo()
	throws DaoManagerException, RemoteException {
		try {
			Tbf_poloDao dao = new Tbf_poloDao();
			List<Trf_gruppo_attivita_polo> elenco = dao.loadAttivitaGruppoPolo();
			if (elenco != null) {
				String[] output = new String[elenco.size()];
				for (int i = 0; i < elenco.size(); i++) {
					String codiceAtt = elenco.get(i).getId_attivita_polo().getCd_attivita().getCd_attivita();
					output[i] = codiceAtt;
				}
				return output;
			}
			return null;
		}
		catch (DaoManagerException e) {
			log.error("", e);
			return null;
		}
	}

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
	public OrderedTreeElement getElencoAttivita(java.lang.String ticket)
			throws DaoManagerException, RemoteException {
		List attivita = null;


		String codiceAttivita, descrizioneAttivita, codiceAttivitaParent;

//		AmministrazionePoloDao dao = new AmministrazionePoloDao();
		Tbf_poloDao dao = new Tbf_poloDao();
		OrderedTreeElement ote = new OrderedTreeElement("rootNode", "");

		try {

//			attivita = dao.getCodiciAttivita();
			attivita = dao.loadCodiciAttivitaDelPolo();
//			parametri = dao.getParametri();


			// Troviamo le attivita di primo livello
			for (int index = 0; index < attivita.size(); index++) {
				Tbf_attivita tbf_attivita = (Tbf_attivita) attivita.get(index);

				codiceAttivita = tbf_attivita.getCd_attivita();
				descrizioneAttivita = tbf_attivita.getId_attivita_sbnmarc().getDs_attivita();
				codiceAttivitaParent = tbf_attivita.getCd_funzione_parent();
				if (codiceAttivitaParent == null) {
					 //ote.addElement(new String(descrizioneAttivita), new String(codiceAttivita + ": " + descrizioneAttivita));
					ote.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK",  new String(codiceAttivita), tbf_attivita.getPrg_ordimanento() + "");
				}
			}

//			if (ote.getElements().size() > 0)
//				ote.sortByValue();

			// Per ogni attivita di primo livello troviamo figli (niente nipoti per le attivita')
			for (int j=0; j< ote.getElements().size(); j++)
			{
				OrderedTreeElement ote1 = ote.getElements().get(j);

				OrderedTreeElement oteChildren = new OrderedTreeElement("node"+Integer.toString(j), "");

				for (int index = 0; index < attivita.size(); index++) {
					Tbf_attivita tbf_attivita = (Tbf_attivita) attivita.get(index);

					codiceAttivitaParent = tbf_attivita.getCd_funzione_parent();

					if (codiceAttivitaParent != null &&  ((codiceAttivitaParent).compareTo( ((String)ote1.getValue()).substring(0, 5) ) == 0))
		        	{
						codiceAttivita = tbf_attivita.getCd_attivita();
						descrizioneAttivita = tbf_attivita.getId_attivita_sbnmarc().getDs_attivita();
//						oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita + ": " + descrizioneAttivita));
						oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK", new String(codiceAttivita), tbf_attivita.getPrg_ordimanento() + "");
		        	}
				}
		        if (oteChildren.getElements().size() > 0)
		        {
//			        oteChildren.sort();
//		        	oteChildren.sortBySortValue();
			        ote.addChild(oteChildren,  (String)ote1.getKey());
		        }
		        else
		        	oteChildren = null; // destroy

			} // end for j
		} catch (DaoManagerException e) {
			log.error("", e);
		}
		return ote;
	} // End getElencoAttivita

	public boolean setProfiloPolo(String codicePolo, String codiceUtente,
								  List<String> elencoAttivita,
								  List<GruppoParametriVO> elencoAuthorities,
								  List<GruppoParametriVO> elencoMateriali,
								  List<GruppoParametriVO> elencoSemantica) throws DaoManagerException, RemoteException {

		try {
		// ************* ATTIVITA' *************

			// Se non esiste il gruppo attività, lo creiamo. Il gruppo personalizzato avrà come ds_name "POLO".
			Tbf_poloDao dao = new Tbf_poloDao();
			int idGruppoNew = dao.creaGruppoAttivitaPolo(codicePolo);
			int idGruppoOld = dao.getIdGruppoAttivita();

			//Rimuovo, se esistono, i dati del gruppo dalla tabella Trf_gruppo_attivita_polo
			dao.eliminaAttivitaDelGruppo(idGruppoNew);
			dao.inserisciAttivitaDelGruppo(elencoAttivita, idGruppoNew);
			dao.pulisciGruppoAttivitaPadri(idGruppoNew);
			dao.attivaProfiloAttivita(idGruppoNew);
			// ************* AGGIORNO LE BIBLIOTECHE *************
			dao.aggiornaProfiloBiblioteche(idGruppoOld, idGruppoNew, elencoAttivita, codiceUtente);
			// ************* AGGIORNO I BIBLIOTECARI *************
			List<String> elencoUtentiAggiornati = dao.aggiornaProfiliBibliotecari(elencoAttivita);

		// ************* PARAMETRI *************

			// Se non esiste il profilo dei parametri lo creiamo.
			int idParametro = dao.creaProfiloParametroPolo(codicePolo, codiceUtente);
			// Inserisco i relativi parametri:
			if (ValidazioneDati.isFilled(elencoAuthorities) )
				dao.inserisciParAuth(elencoAuthorities, idParametro);
			else {
				dao.rimuoviParAuth(idParametro);
			}
			if (ValidazioneDati.isFilled(elencoMateriali) )
				dao.inserisciParMat(elencoMateriali, idParametro);
			else {
				dao.rimuoviParMat(idParametro);
			}
			if (ValidazioneDati.isFilled(elencoSemantica) )
				dao.inserisciParSem(elencoSemantica, idParametro);
			else {
				dao.rimuoviParSem(idParametro);
			}
			// ************* ATTIVO IL PROFILO *************
			dao.attivaProfiloParametri(idParametro);

			//almaviva5_20130417 #5269
			dao.aggiornaParametriBiblioteche(codicePolo, codiceUtente);

			dao.clearCache("amministrazione");
			if (ValidazioneDati.isFilled(elencoUtentiAggiornati)) {
				String urlUtenti = ValidazioneDati.formatValueList(elencoUtentiAggiornati, ",");
				rimuoviProfilo(urlUtenti); // comunico a SbnMarc che il profilo vecchio non è più valido
				SbnWebProfileCache.getInstance().clear(elencoUtentiAggiornati);
			}
			return true;
		}
		catch (Exception e) {
			ctx.setRollbackOnly();
			log.error("", e);
			return false;
		}

	}

	private void rimuoviProfilo(String userId) throws DaoManagerException {
		try {
			if (isSbnMarcPoloDeployed() ) {
				getGateway().service("rimuoviProfili", userId);
			}
		}
		catch (Exception e) {
			resetGateway();
			log.error("", e);
		}
	}

	public void reloadCodiciSbnMarc() throws EJBException {
		try {
			if (isSbnMarcPoloDeployed() ) {
				getGateway().service("reloadCodici");
			}
		}
		catch (Exception e) {
			resetGateway();
			log.error("", e);
			throw new EJBException(e);
		}
	}

	public void rimuoviProfiliSbnMarc() throws EJBException {
		try {
			if (isSbnMarcPoloDeployed() ) {
				getGateway().service("rimuoviProfili");
			}
		}
		catch (Exception e) {
			resetGateway();
			log.error("", e);
			throw new EJBException(e);
		}
	}

	public List<String> controllaAttivita(List<String> elencoAttivita) throws DaoManagerException, RemoteException {

		try {
			Tbf_poloDao poloDao = new Tbf_poloDao();
			return poloDao.controllaAttivita(elencoAttivita);
		}
		catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public void clearDBCache() throws DaoManagerException {
		Tbf_poloDao poloDao = new Tbf_poloDao();
		SessionFactory factory = poloDao.getFactory();
		Map<String, CollectionMetadata> roleMap = factory.getAllCollectionMetadata();
		for (String roleName : roleMap.keySet()) {
			factory.evictCollection(roleName);
			log.info("clearDBCache: " + roleName);
		}

		Map<String, ClassMetadata> entityMap = factory.getAllClassMetadata();
		for (String entityName : entityMap.keySet()) {
			factory.evictEntity(entityName);
			log.info("clearDBCache: " + entityName);
		}

		factory.evictQueries();
	}

	private SbnProfileType lookupProfiloIndice() {
		try {
			ProfilerManagerMBean pm = ProfilerManager.getProfilerManagerInstance();
			return pm.getProfiloIndice(false);

		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public void checkProfiloIndicePolo() throws DaoManagerException, RemoteException {
		try {
			log.info("Inizio verifica delle attivita' dell'indice con quelle del polo...");
			Tbf_poloDao dao = new Tbf_poloDao();
			List<Trf_attivita_polo> attivitaPolo = dao.loadAttivitaDelPolo();
			int attPoloSize = ValidazioneDati.size(attivitaPolo);
			SbnProfileType profiloIndice = this.lookupProfiloIndice();

			//Serializzo l'oggetto (in caso di mancanza della connessione verso l'indice:
//			IndiceVO indice = new IndiceVO(profiloIndice);
//			FileOutputStream underlyingStream = new FileOutputStream("C:\\temp\\test");
//			ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);
//			serializer.writeObject(indice);

			//Deserializzo l'oggetto (in caso di mancanza della connessione verso l'indice:
//			FileInputStream underlyingStream = new FileInputStream("C:\\temp\\test");
//			ObjectInputStream deserializer = new ObjectInputStream(underlyingStream);
//			IndiceVO indice = (IndiceVO)deserializer.readObject();
//			SbnProfileType profiloIndice = indice.getProfilo();

			//almaviva5_20101117 elenco attività ignorate
			String[] attivitaIgnorate = caricaAttivitaIgnorate();

			AttivitaAbilitateType[] elencoAttivitaIndice = profiloIndice.getAttivitaAbilitate();

			List<String> attivitaAggiunte = new ArrayList<String>();
			List<Trf_attivita_polo> attivitaRimosse = new ArrayList<Trf_attivita_polo>();

			//Controllo lato polo:
			for (int i =0; i < attPoloSize; i++) {
				if (attivitaPolo.get(i).getCd_attivita().getId_attivita_sbnmarc().getTp_attivita().equals("S")) {
					String descAttivitaPolo = OrdinamentoCollocazione2.normalizza(attivitaPolo.get(i).getCd_attivita().getId_attivita_sbnmarc().getDs_attivita());
					boolean trovata = false;
					for (int j=0; j < elencoAttivitaIndice.length; j++) {
						AttivitaAbilitateType att = elencoAttivitaIndice[j];
						if (att.getSottoAttivitaCount() > 0) {
							SottoAttivita[] elencoSottoAttivita = att.getSottoAttivita();
							for (int n = 0; n < elencoSottoAttivita.length; n++) {
								SottoAttivita sottoAttivita = elencoSottoAttivita[n];
								String descAttivitaIndice = OrdinamentoCollocazione2.normalizza(sottoAttivita.getContent());
								if (descAttivitaIndice.equals(descAttivitaPolo)) {
									trovata = true;
									break;
								}
							}
						}
						if (trovata)
							break;
						String descAttivitaIndice = OrdinamentoCollocazione2.normalizza(att.getAttivita().getContent());
						if (descAttivitaIndice.equals(descAttivitaPolo)) {
							trovata = true;
							break;
						}
					}
					if (!trovata)
						attivitaRimosse.add(attivitaPolo.get(i));
				}
			}

			//Controllo lato indice:
			for (int j=0; j < elencoAttivitaIndice.length; j++) {
				AttivitaAbilitateType att = elencoAttivitaIndice[j];
				boolean trovata = false;
				if (att.getSottoAttivitaCount() > 0) {
					SottoAttivita[] elencoSottoAttivita = att.getSottoAttivita();
					for (int n = 0; n < elencoSottoAttivita.length; n++) {
						SottoAttivita sottoAttivita = elencoSottoAttivita[n];
						String descAttivitaIndice = OrdinamentoCollocazione2.normalizza(sottoAttivita.getContent());
						for (int i =0; i < attPoloSize; i++) {
							String descAttivitaPolo = OrdinamentoCollocazione2.normalizza(attivitaPolo.get(i).getCd_attivita().getId_attivita_sbnmarc().getDs_attivita());
							if (descAttivitaIndice.equals(descAttivitaPolo)) {
								trovata = true;
								break;
							}
						}
						if (!trovata && !ValidazioneDati.in(descAttivitaIndice, attivitaIgnorate))
							attivitaAggiunte.add(descAttivitaIndice);
					}
				}
				String descAttivitaIndice = OrdinamentoCollocazione2.normalizza(att.getAttivita().getContent());
				for (int i =0; i < attPoloSize; i++) {
					String descAttivitaPolo = OrdinamentoCollocazione2.normalizza(attivitaPolo.get(i).getCd_attivita().getId_attivita_sbnmarc().getDs_attivita());
					if (descAttivitaIndice.equals(descAttivitaPolo)) {
						trovata = true;
						break;
					}
				}
				if (!trovata && !ValidazioneDati.in(descAttivitaIndice, attivitaIgnorate))
					attivitaAggiunte.add(descAttivitaIndice);
			}


//          ************** Inizio Controlli rimozione delle attivita' non piu' presenti nel profilo indice: **************
			CodiciAttivita codiciAttivita = CodiciAttivita.getIstance();
			final String creaAuth = codiciAttivita.CREA_ELEMENTO_DI_AUTHORITY_1017;
			final String modificaAuth = codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026;
			final String creaDoc = codiciAttivita.CREA_DOCUMENTO_1016;
			final String modificaDoc = codiciAttivita.MODIFICA_1019;

			List<Trf_attivita_polo> elencoAttivitaProfiloPolo = dao.loadAttivitaProfiloPolo();

			List<Trf_attivita_polo> elencoAttivitaEliminabili = new ArrayList<Trf_attivita_polo>();
			List<String> elencoAttivitaNonEliminabili = new ArrayList<String>();

			if (attivitaRimosse.size() > 0) {
				for (int i = 0; i < attivitaRimosse.size(); i++) {
					Trf_attivita_polo attivitaRimossa = attivitaRimosse.get(i);
					boolean trovata = false;
					//Controllo se l'attivita' fa parte del profilo del polo:
					for (int u = 0; u < elencoAttivitaProfiloPolo.size(); u++) {
						Trf_attivita_polo attivitaProfiloPolo = elencoAttivitaProfiloPolo.get(u);
						if (attivitaRimossa.equals(attivitaProfiloPolo)) {
							trovata = true;
							if (attivitaRimossa.getCd_attivita().getCd_attivita().equals(creaAuth) ||
								attivitaRimossa.getCd_attivita().getCd_attivita().equals(modificaAuth)) {
								//Controllo se i parametri di Authority sono solo locali:
								if (!dao.checkSoloLocaleAuthPolo())
									elencoAttivitaNonEliminabili.add("\n" + attivitaProfiloPolo.getCd_attivita() + " " + attivitaProfiloPolo.getCd_attivita().getId_attivita_sbnmarc().getDs_attivita());
							}
							else if (attivitaRimossa.getCd_attivita().getCd_attivita().equals(creaDoc) ||
								attivitaRimossa.getCd_attivita().getCd_attivita().equals(modificaDoc)) {
								//Controllo se i parametri dei Materiali sono solo locali:
								if (!dao.checkSoloLocaleMatPolo())
									elencoAttivitaNonEliminabili.add("\n" + attivitaProfiloPolo.getCd_attivita() + " " + attivitaProfiloPolo.getCd_attivita().getId_attivita_sbnmarc().getDs_attivita());
							}
							else
								elencoAttivitaNonEliminabili.add("\n" + attivitaProfiloPolo.getCd_attivita() + " " + attivitaProfiloPolo.getCd_attivita().getId_attivita_sbnmarc().getDs_attivita());
							break;
						}
					}
					if (!trovata)
						elencoAttivitaEliminabili.add(attivitaRimossa);
				}
			}

			String emailPolo = ValidazioneDati.trimOrEmpty(dao.getPolo().getEmail());
			if (elencoAttivitaNonEliminabili.size() > 0) {
				if (!ValidazioneDati.isFilled(emailPolo)) {
					log.warn("Impossibile inviare mail all'amministratore di polo. Verificare l'indirizzo sul database");
					return;
				}
				// Invia una mail all'amministratore con elencate le attivita che dovranno essere rimosse:
				InternetAddress address = new InternetAddress(emailPolo);
				getAmministrazioneMail().inviaMail(address, address,
						"SBNWEB: Comunicazione",
						"Messaggio da parte di SBNWEB.\nAlcune attivita', appartenenti al profilo del polo, non sono piu' gestite dall'indice:\n" +
						elencoAttivitaNonEliminabili.toString().replace("[", "").replace("]", ""));
				log.warn("Alcune attivita', appartenenti al profilo del polo, non sono piu' gestite dall'indice! Contattato l'amministratore di sistema.");
				// Mi fermo qui.
				return;
			}

//          ************** Fine Controlli Rimozione delle attivita' non piu' presenti nel profilo indice. **************

//			************** Inizio controlli per l'aggiunta delle nuove attivita' per il polo: **************
			// Controllo l'esistenza su sistema di tutte le nuove attivita'
			boolean checkAtt = dao.controllaEsistenzaAttivita(attivitaAggiunte);
			if (!checkAtt) {
				if (!ValidazioneDati.isFilled(emailPolo)) {
					log.info("Impossibile inviare mail all'amministratore di polo. Verificare l'indirizzo sul database");
					return;
				}
				for (int w =0; w < attivitaAggiunte.size(); w++) {
					String attAgg = attivitaAggiunte.get(w);
					attAgg= "\n" + attAgg;
					attivitaAggiunte.add(w, attAgg);
					attivitaAggiunte.remove(w+1);
				}
				//Invio una mail all'amministratore dicendo che esistono nuove attivita non compatibili col sistema.
				InternetAddress address = new InternetAddress(emailPolo);
				int inviaMail = getAmministrazioneMail().inviaMail(address, address,
						"SBNWEB: Comunicazione",
						"Messaggio da parte di SBNWEB.\nDurante il controllo del profilo dell'indice, alcune attivita' risultano non gestite dal sistema.\n" +
						attivitaAggiunte.toString().replace("[", "").replace("]", "") +
						"\n\nOperazione di controllo profili sospesa.");

				if (inviaMail != 0)
					log.error("Impossibile inviare mail all'indirizzo " + emailPolo);

				log.error("Rilevata, dal profilo indice, un'attivita' non gestita dal sistema! Contattare l'amministratore di sistema.");
				//Mi fermo qui.
				return;
			}
//			************** Fine controlli per l'aggiunta delle nuove attivita' per il polo. **************

//			************** Eseguo le operazioni di eliminazione e aggiunta attivita' **************
			if (elencoAttivitaEliminabili.size() > 0) {
				dao.eliminaAttivitaPolo(elencoAttivitaEliminabili);
				log.info("Sono state eliminate, per il polo, alcune attivita' escluse attualmente dall'indice.");
			}
			//Aggiungo le attivita nella tabella Trf_attivita_polo:
			if (attivitaAggiunte.size() > 0) {
				dao.aggiungiAttivitaPolo(attivitaAggiunte);
				log.info("Sono state aggiunte, per il polo, alcune attivita' attualmente gestite dall'indice.");
			}

			log.info("Fine verifica delle attivita' dell'indice con quelle del polo.");

		} catch (Exception e) {
			log.error("", e);
			log.error("ERRORE DURANTE CONTROLLO PROFILI INDICE/POLO!! Contattare l'amministratore di sistema.");
		}
	}

	private synchronized String[] caricaAttivitaIgnorate() throws Exception {
		Properties p = new Properties();
		p.load(AmministrazionePoloBean.class.getResourceAsStream(ATTIVITA_IGNORATE_PATH));
		String[] tmp = p.getProperty("attivita_ignorate").split(",");
		String[] out = new String[tmp.length];
		int idx = 0;
		for (String att : tmp)
			out[idx++] = OrdinamentoCollocazione2.normalizza(att);

		log.debug("Attività ignorate: " + out.length);
		return out;
	}


	public List<CodaJMSVO> getListaCodeBatch() throws DaoManagerException, EJBException	{
		List<CodaJMSVO> result = new ArrayList<CodaJMSVO>();

		try {
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			List<Tbf_coda_jms> list = dao.selectCodeJMS(false);

			for (Tbf_coda_jms codaJms : list) {

				CodaJMSVO coda = new CodaJMSVO(codaJms);
				result.add(coda);
			}

		} catch (Exception e) {
			log.error("", e);
		}

		return result;
	}

	public List<String> getListaCodeBatchOutput() throws DaoManagerException, EJBException	{
		List<String> result = new ArrayList<String>();

		try {
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			result = dao.selectCodeJMSOutput();

		} catch (Exception e) {
			log.error("", e);
		}

		return result;
	}

	public CodaJMSVO getCodaBatch(int idCoda) throws DaoManagerException, EJBException {

		CodaJMSVO coda = null;

		try {
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			Tbf_coda_jms codaJms = dao.selectCodeJMS(idCoda);
			if (codaJms == null)
				return null;
			coda = new CodaJMSVO(codaJms);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}

		return coda;
	}

	public String prenotaElaborazioneDifferita(
			ParametriRichiestaElaborazioneDifferitaVO richiesta,
			Map<String, Object> properties)
			throws ValidationException, ApplicationException {
		return prenotaElaborazioneDifferita(richiesta, properties, null);
	}

	public String prenotaElaborazioneDifferita(
			ParametriRichiestaElaborazioneDifferitaVO richiesta,
			Map<String, Object> properties,
			Validator<? extends ParametriRichiestaElaborazioneDifferitaVO> validator)
			throws ValidationException, ApplicationException {

		Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
		Tbf_bibliotecarioDao bibDao = new Tbf_bibliotecarioDao();

		if (richiesta == null)
			throw new ValidationException(SbnErrorTypes.BATCH_INVALID_PARAMS);

		if (validator != null)
			richiesta.validate(validator);
		else
			richiesta.validate();

		try {
			if (!getProfiler().isOkAttivita(richiesta.getTicket(), richiesta.getCodAttivita()) )
				throw new ApplicationException(SbnErrorTypes.USER_NOT_AUTHORIZED);

			Tbf_batch_servizi batch = dao.selectBatchByCodAttivita(richiesta.getCodAttivita());
			if (batch == null)
				throw new ValidationException(SbnErrorTypes.BATCH_CONFIGURATION_ERROR);

			JMSUtil jms = new JMSUtil(JNDIUtil.getContext());
			Tbf_coda_jms id_coda_input = batch.getId_coda_input();

			String codaInput = id_coda_input.getNome_jms();
			String codaOutput = batch.getNome_coda_output();
			if (ValidazioneDati.strIsNull(codaInput) || ValidazioneDati.strIsNull(codaOutput) )
				throw new ValidationException(SbnErrorTypes.BATCH_INVALID_PARAMS);

			richiesta.setNomeCodaJMS(codaInput);
			richiesta.setNomeCodaJMSOutput(codaOutput);

			String visibilita = String.valueOf(batch.getVisibilita());
			richiesta.setVisibilita(visibilita);
			richiesta.setIdCoda(id_coda_input.getId_coda());

			String nomeBibliotecario = bibDao.getNomeBibliotecario(richiesta.getUser());
			richiesta.setCognomeNome(nomeBibliotecario);

			String idBatch = jms.prenotaBatch(id_coda_input.getId_coda(), richiesta.getNomeCodaJMS(), richiesta, properties);
			return idBatch;

		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public int eliminaRichiesteElaborazioniDifferite(String[] idBatch, Boolean deleteOutputs) throws ValidationException, ApplicationException {

		int deleted = 0;
		try {
			if (!ValidazioneDati.isFilled(idBatch))
				throw new ValidationException(SbnErrorTypes.BATCH_INVALID_PARAMS);

			//creo il selector per tutti i messaggi da recuperare
			Iterator<String> i = Arrays.asList(idBatch).iterator();
			StringBuilder buf = new StringBuilder();
			for (;;) {
				buf.append(ConstantsJMS.ID_BATCH).append("=");
				buf.append(i.next());
				if (i.hasNext())
					buf.append(" OR ");
				else
					break;
			}

			String selector = buf.toString();
			log.info("eliminaRichiesteElaborazioniDifferite() selector: " + selector);

			InitialContext ctx = JNDIUtil.getContext();
			JMSUtil jms = new JMSUtil(ctx);
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			List<Tbf_coda_jms> listaCode = dao.selectCodeJMS(true);

			List<Message> messages = new ArrayList<Message>();
			Set<String> code = new THashSet<String>();

			// cerco sulle code di input
			for (Tbf_coda_jms codaJms : listaCode) {
				String qname = ValidazioneDati.trimOrEmpty(codaJms.getNome_jms());
				if (code.contains(qname))
					continue;
				Queue queue = (Queue) ctx.lookup(qname);
				messages.addAll(jms.receiveQueue(queue, selector) );
				code.add(qname);
			}

			//cerco sulle code di output
			List<String> codeJMSOutput = dao.selectCodeJMSOutput();
			for (String coda : codeJMSOutput) {
				Queue queue = (Queue) ctx.lookup(coda);
				messages.addAll(jms.receiveQueue(queue, selector) );
			}

			BatchManagerMBean bm = BatchManager.getBatchManagerInstance();
			//leggo default se non é impostato il flag
			if (deleteOutputs == null)
				deleteOutputs = bm.isUserDeletionDeleteOutputs();

			deleted = ValidazioneDati.size(messages);

			if (deleted > 0)
				//cancello eventuali processi in esecuzione
				for (Message msg : messages) {
					ObjectMessage objMsg = (ObjectMessage)msg;
					//almaviva5_20100422
					Serializable obj = objMsg.getObject();
					if (!(obj instanceof ElaborazioniDifferiteOutputVo))
						continue;

					ElaborazioniDifferiteOutputVo output = (ElaborazioniDifferiteOutputVo) obj;
					bm.removeRunningJob(output.getIdBatch());

					// cancello i file di output, se richiesto
					if (deleteOutputs) {
						List<DownloadVO> downloadList = output.getDownloadList();
						if (!ValidazioneDati.isFilled(downloadList))
							continue;

						for (DownloadVO file : downloadList)
							BatchCleanerControllerJob.deleteBatchDownloadFile(file);
					}
				}

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return deleted;
	}

	public String getDescrizioneAttivita(String codAttivita) {

		try {
			Tbf_poloDao dao = new Tbf_poloDao();
			Tbf_attivita attivita = dao.getAttivita(codAttivita);
		if (attivita == null)
			return null;
		return attivita.getId_attivita_sbnmarc().getDs_attivita();

		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void forceBatchStart(String idBatch) throws ValidationException, ApplicationException {

		try {
			if (!ValidazioneDati.isFilled(idBatch))
				throw new ValidationException(SbnErrorTypes.BATCH_INVALID_PARAMS);

			StringBuilder buf = new StringBuilder();
			buf.append(ConstantsJMS.ID_BATCH).append("=").append(idBatch);
			buf.append(" and ");
			buf.append(ConstantsJMS.STATO).append("='").append(ConstantsJMS.STATO_SEND).append("'");

			String selector = buf.toString();
			log.info("forceBatchStart() selector: " + selector);

			InitialContext ctx = JNDIUtil.getContext();
			JMSUtil jms = new JMSUtil(ctx);
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			List<Tbf_coda_jms> listaCode = dao.selectCodeJMS(true);

			// cerco sulle code di input
			Set<String> code = new THashSet<String>();
			for (Tbf_coda_jms codaJms : listaCode) {
				String qname = ValidazioneDati.trimOrEmpty(codaJms.getNome_jms());
				if (code.contains(qname))
					continue;

				Queue queue = (Queue) ctx.lookup(qname);
				List<Message> messages = jms.receiveQueue(queue, selector);
				code.add(qname);
				if (!ValidazioneDati.isFilled(messages))
					continue;

				Message msg = ValidazioneDati.first(messages);
				jms.startBatch(new CodaJMSVO(codaJms), msg);
			}

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public void forceBatchStop(String idBatch) throws ValidationException, ApplicationException {
		try {
			BatchManager.getBatchManagerInstance().removeRunningJob(idBatch);
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e);
		}
	}

	public Tbf_batch_servizi selectBatchByCodAttivita(String cod_attivita) throws ValidationException, ApplicationException {
		try {
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			Tbf_batch_servizi batch = dao.selectBatchByCodAttivita(cod_attivita);
			if (batch == null)
				throw new ValidationException(SbnErrorTypes.BATCH_CONFIGURATION_ERROR);

			return batch;
		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}

	}

	public ElaborazioniDifferiteOutputVo cercaRichiestaElaborazioneDifferita(String ticket, String idBatch) throws ValidationException, ApplicationException {

		ElaborazioniDifferiteOutputVo output = null;
		try {
			log.info("cercaRichiestaElaborazioneDifferita() idBatch: " + idBatch);

			InitialContext ctx = JNDIUtil.getContext();
			Tbf_batch_serviziDAO dao = new Tbf_batch_serviziDAO();
			List<Tbf_coda_jms> listaCode = dao.selectCodeJMS(true);

			List<Message> messages = new ArrayList<Message>();
			Set<String> code = new THashSet<String>();
			Map<String, Object> param = new THashMap<String, Object>();

			param.put(ConstantsJMS.ID_BATCH, Integer.valueOf(idBatch) );

			// cerco sulle code di input
			for (Tbf_coda_jms codaJms : listaCode) {
				String qname = ValidazioneDati.trimOrEmpty(codaJms.getNome_jms());
				if (code.contains(qname))
					continue;
				Queue queue = (Queue) ctx.lookup(qname);
				messages.addAll(getRichieste(ticket, queue, null, param));
				if (ValidazioneDati.isFilled(messages))
					break;

				code.add(qname);
			}

			//cerco sulle code di output
			List<String> codeJMSOutput = dao.selectCodeJMSOutput();
			for (String coda : codeJMSOutput) {
				Queue queue = (Queue) ctx.lookup(coda);
				messages.addAll(getRichieste(ticket, queue, null, param));
				if (ValidazioneDati.isFilled(messages))
					break;
			}

			if (!ValidazioneDati.isFilled(messages))
				return null;

			Message msg = ValidazioneDati.first(messages);
			Object object = ((ObjectMessage) msg).getObject();
			ParametriRichiestaElaborazioneDifferitaVO params = (ParametriRichiestaElaborazioneDifferitaVO) object;
			String stato = msg.getStringProperty(ConstantsJMS.STATO);
			output = new ElaborazioniDifferiteOutputVo(params);
			output.setStato(stato != null ? stato : ConstantsJMS.STATO_SEND);

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return output;
	}

	public List<Map<String, Object>> executeSQLCommand(String ticket, String command) throws ValidationException, ApplicationException, EJBException {
		checkTicket(ticket);

		GenericJDBCAcquisizioniDAO dao = new GenericJDBCAcquisizioniDAO();
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			c = dao.getConnection();
			st = c.createStatement();
			boolean isRs = st.execute(command);
			if (!isRs && st.getUpdateCount() < 0) //vuoto
				return null;

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			rs = st.getResultSet();
			if (rs == null) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				columns.put("updated", st.getUpdateCount());
			    rows.add(columns);
			    return rows;
			}

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
			    Map<String, Object> columns = new LinkedHashMap<String, Object>();

			    int dup_lbl = 0;
			    for (int i = 1; i <= columnCount; i++) {
					Object value = rs.getObject(i);
					String label = metaData.getColumnLabel(i);
					//almaviva5_20150528 fix duplicazione nome colonna
					if (columns.keySet().contains(label))
						label += "_" + (++dup_lbl);
					columns.put(label, value != null ? value : "NULL");
				}

			    rows.add(columns);
			}

			while (st.getMoreResults() ) {
				rs = st.getResultSet();
				metaData = rs.getMetaData();
				columnCount = metaData.getColumnCount();
			}

			return rows;

		} catch (SQLException e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e.getLocalizedMessage());
		} finally {
			dao.close(rs);
			dao.close(st);
			dao.close(c);
		}
	}

	private void pulisciConfig(TipoAttivita tipo, ElaborazioniDifferiteConfig config, List<BatchAttivazioneVO> listaBatch) {

		if (tipo == null)
			return;

		Iterator<AreaBatchVO> iArea = config.getAree().iterator();
		while (iArea.hasNext()) {
			AreaBatchVO area = iArea.next();

			if (!ValidazioneDati.isFilled(area.getAttivita())) {
				iArea.remove();
				continue;
			}

			Iterator<ActionPathVO> iAttivita = area.getAttivita().iterator();
			while (iAttivita.hasNext()) {
				ActionPathVO ap = iAttivita.next();
				if (ap.getTipo() != tipo) {
					iAttivita.remove();
					continue;
				}
				//almaviva5_20100730 sostituisco la descrizione statica con quella del db
				if (ValidazioneDati.isFilled(listaBatch))
					for (BatchAttivazioneVO ba : listaBatch)
						if (ValidazioneDati.equals(ba.getCod_attivita(), ap.getCodAttivita()) ) {
							ap.setDescrizione(ba.getDescrizione());
							ap.setStatico(false); // non statico
							break;
						}
			}

			if (!ValidazioneDati.isFilled(area.getAttivita()))
				iArea.remove();
		}
	}

	public ElaborazioniDifferiteConfig getConfigurazioneElaborazioniDifferite(TipoAttivita tipo) throws EJBException {



		//if (configRef != null && configRef.get() != null)
		//	return configRef.get();

		log.debug("getConfigurazioneElaborazioniDifferite() type: " + tipo);
		ElaborazioniDifferiteConfig config = new ElaborazioniDifferiteConfig();

		List<AreaBatchVO> aree = config.getAree();
		AreaBatchVO area;
		List<ActionPathVO> attivita;
		CodiciAttivita codiciAttivita = CodiciAttivita.getIstance();

		//gestione bibliografica
		area = new AreaBatchVO();
		area.setId("batch.area.gest.biblio");

		attivita = area.getAttivita();
		attivita.add(new ActionPathVO(codiciAttivita.ALLINEAMENTI_1032, "batch.allineamenti", "/gestionebibliografica/utility/richiestaAllineamenti.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.ALLINEAMENTO_REPERTORI, "batch.allineamenti.repertori", "/gestionebibliografica/utility/richiestaAllineamenti.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.FUSIONE_MASSIVA, "batch.fusione.massiva", "/gestionebibliografica/utility/richiestaAllineamenti.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.CATTURA_MASSIVA, "batch.cattura.massiva", "/gestionebibliografica/utility/richiestaAllineamenti.do", TipoAttivita.FUNZIONE));
		//almaviva5_20130917 evolutive google2
		attivita.add(new ActionPathVO(codiciAttivita.IMPORTA_URI_COPIA_DIGITALE, null, "/documentofisico/elaborazioniDifferite/acquisizioneUriCopiaDigitale.do", TipoAttivita.FUNZIONE));

		//almaviva5_20140204 evolutive google3
		attivita.add(new ActionPathVO(codiciAttivita.LOCALIZZAZIONE_MASSIVA, null, "/gestionebibliografica/utility/richiestaLocalizzazione.do", TipoAttivita.FUNZIONE));

		//almaviva5_20161004 evolutiva oclc
		attivita.add(new ActionPathVO(codiciAttivita.IMPORTA_RELAZIONI_BID_ALTRO_ID, null, "/gestionebibliografica/utility/importaLegamiBidAltroId.do", TipoAttivita.FUNZIONE));

		//stampe bibliografica
		attivita.add(new ActionPathVO(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE, "batch.area.gest.biblio.stampaSchede", "/gestionestampe/schede/stampaSchede.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI, "batch.area.gest.biblio.stampaCataloghi", "/elaborazioniDifferite/esporta/esporta.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(CodiciAttivita.getIstance().STAMPA_TITOLI_EDITORI, "batch.area.gest.biblio.stampaTitoliEditore", "/gestionestampe/editori/stampaTitoliEditore.do", TipoAttivita.STAMPA));
		aree.add(area);

		//semantica
		area = new AreaBatchVO();
		area.setId("batch.area.semantica");
		attivita = area.getAttivita();
		attivita.add(new ActionPathVO(codiciAttivita.CANCELLAZIONE_SOGGETTI_INUTILIZZATI, "batch.area.semantica.sogg.inutilizzati", "/gestionesemantica/soggetto/PrenotaElaborazioneDifferita.do?CDATTIVITA="
			+ codiciAttivita.CANCELLAZIONE_SOGGETTI_INUTILIZZATI, TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.CANCELLAZIONE_DESCRITTORI_INUTILIZZATI, "batch.area.semantica.descr.inutilizzati", "/gestionesemantica/soggetto/PrenotaElaborazioneDifferita.do?CDATTIVITA="
			+ codiciAttivita.CANCELLAZIONE_DESCRITTORI_INUTILIZZATI, TipoAttivita.FUNZIONE));

		//stampe semantica
		attivita.add(new ActionPathVO(CodiciAttivita.getIstance().STAMPA_SOGGETTARIO, "batch.area.semantica.stampaSoggettario", "/gestionestampe/semantica/prenotaStampaSemantica.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(CodiciAttivita.getIstance().STAMPA_DESCRITTORI, "batch.area.semantica.stampaDescrittori", "/gestionestampe/semantica/prenotaStampaSemantica.do", TipoAttivita.STAMPA));
		//attivita.add(new ActionPathVO(CodiciAttivita.getIstance().STAMPA_SISTEMA_CLASSIFICAZIONE, "batch.area.semantica.stampaSistemaClassificazione", "/gestionestampe/classi/stampaSistemaClasse.do", TipoAttivita.STAMPA));
		//attivita.add(new ActionPathVO(CodiciAttivita.getIstance().STAMPA_THESAURO_POLO, "batch.area.semantica.stampaThesauro", "/gestionestampe/semantica/stampaThesauroPolo.do", TipoAttivita.STAMPA));
		aree.add(area);

		//doc. fisico
		area = new AreaBatchVO();
		area.setId("batch.area.documentoFisico");
		attivita = area.getAttivita();
		attivita.add(new ActionPathVO(codiciAttivita.GDF_SPOSTAMENTO_COLLOCAZIONI, "batch.area.documentoFisico.spostaColl", "/documentofisico/elaborazioniDifferite/spostamentoCollocazioni.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.GDF_AGGIORNAMENTO_DISPONIBILITA_INVENTARI, "batch.area.documentoFisico.aggDisp", "/documentofisico/elaborazioniDifferite/aggiornamentoDisponibilita.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.GDF_SCARICO_INVENTARIALE, "batch.area.documentoFisico.scarInv", "/documentofisico/elaborazioniDifferite/scaricoInventariale.do", TipoAttivita.FUNZIONE));

		//stampe doc. fisico
		attivita.add(new ActionPathVO(codiciAttivita.GDF_ETICHETTE, "batch.area.documentoFisico.etichette", "/gestionestampe/etichette/stampaEtichette.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GA_STAMPA_REGISTRO_DI_INGRESSO, "batch.area.documentoFisico.ingresso", "/gestionestampe/ingresso/stampaRegistroIngresso.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GDF_STAMPA_REGISTRO_TOPOGRAFICO, "batch.area.documentoFisico.topografico", "/gestionestampe/topografico/stampaRegistroTopografico.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE, "batch.area.documentoFisico.conservazione", "/gestionestampe/conservazione/stampaRegistroConservazione.do", TipoAttivita.STAMPA));
//		attivita.add(new ActionPathVO(codiciAttivita.GDF_BOLLETTINO_NUOVE_ACCESSIONI, "batch.area.documentoFisico.bollettino", "/gestionestampe/bollettino/stampaBollettino.do", TipoAttivita.STAMPA));		attivita.add(new ActionPathVO(codiciAttivita.GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO, "batch.area.documentoFisico.strumentiPatrimonio", "/gestionestampe/strumentiPatrimonio/stampaStrumentiPatrimonio.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO, "batch.area.documentoFisico.strumentiPatrimonio", "/gestionestampe/strumentiPatrimonio/stampaStrumentiPatrimonio.do", TipoAttivita.STAMPA));
		aree.add(area);

		//acquisizioni
		area = new AreaBatchVO();
		area.setId("batch.area.acquisizioni");
		attivita = area.getAttivita();
		//stampe acquisizioni
		attivita.add(new ActionPathVO(codiciAttivita.GA_STAMPA_ORDINE, "batch.area.acquisizioni.ordini", "/gestionestampe/ordini/stampaOrdini.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GA_STAMPA_FORNITORI_DI_BIBLIOTECA, "batch.area.acquisizioni.fornitori", "/gestionestampe/fornitori/stampaFornitori.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GA_STAMPA_BOLLETTARIO, "batch.area.acquisizioni.ordiniRilegatura", "/gestionestampe/bollettario/stampaBollettario.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GA_STAMPA_RIPARTIZIONE_SPESE, "batch.area.acquisizioni.statistiche", "/gestionestampe/spese/ripartizioneSpese.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.GA_STAMPA_BUONI_DI_CARICO, "batch.area.acquisizioni.buoni", "/gestionestampe/buoni/stampaBuoniCarico.do", TipoAttivita.STAMPA));

		//almaviva5_20121128 evolutive google
		attivita.add(new ActionPathVO(codiciAttivita.GA_STAMPA_SHIPPING_MANIFEST, null, "/gestionestampe/ordini/stampaOrdini.do", TipoAttivita.STAMPA));
		aree.add(area);

		//servizi
		area = new AreaBatchVO();
		area.setId("batch.area.servizi");
		attivita = area.getAttivita();
		//stampe servizi
		attivita.add(new ActionPathVO(codiciAttivita.SERVIZI_STAMPA_UTENTE, "batch.area.servizi.utenti", "/gestionestampe/utenti/stampaUtenti.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.STAMPA_SERVIZI_CORRENTI, "batch.area.servizi.serviziCorrenti", "/gestionestampe/servizi/stampaServizi.do", TipoAttivita.STAMPA));
		attivita.add(new ActionPathVO(codiciAttivita.STAMPA_SERVIZI_STORICO, "batch.area.servizi.serviziStorico", "/gestionestampe/servizi/stampaServizi.do", TipoAttivita.STAMPA));
		aree.add(area);

//		//amministrazione
//		area = new AreaBatchVO();
//		area.setId("batch.area.amministrazione");
//		//stampe servizi
//		attivita.add(new ActionPathVO(codiciAttivita.SERVIZI_STAMPA_UTENTE, "batch.area.amministrazione.biblioteche", "/gestionestampe/biblioteche/stampaBiblioteche.do", TipoAttivita.STAMPA));


		//unimarc
		area = new AreaBatchVO();
		area.setId("batch.area.unimarc");
		attivita = area.getAttivita();
		attivita.add(new ActionPathVO(codiciAttivita.ESPORTA_DOCUMENTI_1040, "batch.area.unimarc.esporta", "/elaborazioniDifferite/esporta/esporta.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.ESPORTA_IDENTIFICATIVI_DOCUMENTO, "batch.area.unimarc.esportaIdDoc", "/elaborazioniDifferite/esporta/estrattoreIdDocumenti.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.ESPORTA_ELEMENTI_DI_AUTHORITY_1041, null, "/elaborazioniDifferite/esporta/estrattoreIdAuthority.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.IMPORTA_DOCUMENTI_1037, "batch.area.unimarc.importa", "/elaborazioniDifferite/importa/importa.do", TipoAttivita.FUNZIONE));
		aree.add(area);

		//servizi
		area = new AreaBatchVO();
		area.setId("batch.area.servizi");
		attivita = area.getAttivita();
		attivita.add(new ActionPathVO(codiciAttivita.SERVIZI_SOLLECITI, "batch.servizi.solleciti", "/elaborazioniDifferite/invioElaborazioniDifferite.do?CDATTIVITA="
				+ codiciAttivita.SERVIZI_SOLLECITI, TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.SERVIZI_ARCHIVIAZIONE_MOVLOC, "batch.area.servizi.archiviazione", "/servizi/elaborazioniDifferite/archiviazioneMovLoc.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.SRV_DIRITTI_UTENTE, "batch.area.servizi.rinnovoAutorizzazioniUtente", "/servizi/elaborazioniDifferite/rinnovoAutorizzazioniUtente.do", TipoAttivita.FUNZIONE));
		//almaviva5_20120116 importa utenti
		attivita.add(new ActionPathVO(codiciAttivita.SRV_IMPORTA_UTENTI, "batch.servizi.importaUtenti", "/servizi/elaborazioniDifferite/importaUtenti.do", TipoAttivita.FUNZIONE));
		attivita.add(new ActionPathVO(codiciAttivita.SRV_RIFIUTO_PRENOTAZIONI_SCADUTE, "batch.servizi.rifiutaPrenotazioniScadute", "/elaborazioniDifferite/invioElaborazioniDifferite.do?CDATTIVITA="
				+ codiciAttivita.SRV_RIFIUTO_PRENOTAZIONI_SCADUTE, TipoAttivita.FUNZIONE));
		aree.add(area);

		//periodici
		area = new AreaBatchVO();
		area.setId("batch.area.periodici");
		attivita = area.getAttivita();
		attivita.add(new ActionPathVO(codiciAttivita.PERIODICI, "batch.area.periodici.stampaListaFascicoli", "/gestionestampe/periodici/stampaListaFascicoli.do", TipoAttivita.STAMPA));
		aree.add(area);


		//almaviva5_20100730 carico descrizioni batch da db
		List<BatchAttivazioneVO> listaBatch;
		try {
			listaBatch = getBatchServiziAll();
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new EJBException(e);
		}

		//filtro tipo attivita richiesta
		pulisciConfig(tipo, config, listaBatch);

		//configRef = new WeakReference<ElaborazioniDifferiteConfig>(config);
		//return configRef.get();
		return config;
	}

	public Map<String, Object> execScript(JsScriptSession executor, String scriptKey,
			Map<String, Object> params) throws ApplicationException,
			EJBException {
		try {
			JsScriptSessionImpl impl = (JsScriptSessionImpl) executor;
			Map<String, CompiledScript> scripts = impl.getScripts();

			boolean isDebug = Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.SBNMARC_LOCAL_DEBUG, "false"));
			if (isDebug)
				scripts.clear();

			CompiledScript script = scripts.get(scriptKey);
			if (script == null) {
				Tbf_poloDao dao = new Tbf_poloDao();
				Tbf_js_script js_script = dao.loadScript(scriptKey);
				if (js_script == null)
					throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

				JSScriptVO scriptVO = ConversioneHibernateVO.toWeb().jsScript(js_script);

				Compilable compiler = (Compilable) impl.getEngine();
				script = compiler.compile(scriptVO.getScript());
				scripts.put(scriptKey, script);
			}

			ScriptContext scriptContext = impl.getCtx();
			Bindings bindings = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);

			if (ValidazioneDati.isFilled(params))
				bindings.putAll(params);

			script.eval(bindings);
			return new HashMap<String, Object>(bindings);

		} catch (ScriptException e) {
			throw new ApplicationException(SbnErrorTypes.AMM_SCRIPT_ERROR, e.getCause().getMessage());
		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e);
		}

	}

}
