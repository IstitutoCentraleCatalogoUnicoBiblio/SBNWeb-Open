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
package it.iccu.sbn.servizi;

import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnHttp;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnHttpIndice;
import it.iccu.sbn.SbnMarcFactory.factory.SbnProfileDao;
import it.iccu.sbn.SbnMarcFactory.factory.ServerHttp;
import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMail;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.bibliografica.Repertorio;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.extension.sms.SMSResult;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.profiler.SbnWebProfileCache;
import it.iccu.sbn.util.sms.SMSUtil;
import it.iccu.sbn.util.validation.JsScriptExecutor;
import it.iccu.sbn.vo.custom.Credentials;
import it.iccu.sbn.vo.custom.amministrazione.MailProperties;
import it.iccu.sbn.web2.util.Constants;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.NamingException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.system.ServiceMBeanSupport;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author Antonio
 * @author almaviva
 *
 */
public class ProfilerManager extends ServiceMBeanSupport implements ProfilerManagerMBean, HierarchyEventListener {

	private static Logger log = Logger.getLogger(ProfilerManager.class);

	private Level sbnwebLoggingLevel = Level.DEBUG;
	private Level sbnmarcLoggingLevel = Level.DEBUG;
	private Level hibLoggingLevel = Level.DEBUG;
	private SbnProfileType profiloIndice;

	private Timestamp serverStartupTimestamp;

	private AmministrazionePolo amministrazionePolo;
	private AmministrazioneBibliotecario amministrazioneBibliotecario;

	private static final String[] HIBERNATE_LOGGERS = new String[] { "org.hibernate.SQL", "STDOUT" };
	private static final String[] SBNMARC_LOGGERS = new String[] {
			"it.finsiel.", "iccu.serversbnmarc.", "iccu.box.",
			"iccu.amministrazione.", "sbnmarcPolo", "iccu.sbnmarcserver." };
	private static final String[] SBNWEB_LOGGERS = new String[] { "it.iccu.", "sbnmarc" };

	private AmministrazioneMail amministrazioneMail;


	public static final ProfilerManagerMBean getProfilerManagerInstance()
			throws Exception {

		MBeanServer mbserver = MBeanServerLocator.locateJBoss();
		ObjectName name = new ObjectName("sbn:service=ProfilerManager");
		ProfilerManagerMBean pm =
			(ProfilerManagerMBean) MBeanProxyExt.create(ProfilerManagerMBean.class, name, mbserver);

		return pm;
	}

	private AmministrazionePolo getAmministrazionePolo() throws Exception {

		if (amministrazionePolo != null)
			return amministrazionePolo;

		amministrazionePolo = DomainEJBFactory.getInstance().getPolo();

		return amministrazionePolo;

	}

	private AmministrazioneMail getAmministrazioneMail() throws Exception {

		if (amministrazioneMail != null)
			return amministrazioneMail;

		amministrazioneMail = DomainEJBFactory.getInstance().getAmministrazioneMail();
		return amministrazioneMail;
	}


	private AmministrazioneBibliotecario getAmministrazioneBibliotecario() throws Exception {

		if (amministrazioneBibliotecario != null)
			return amministrazioneBibliotecario;

		amministrazioneBibliotecario = DomainEJBFactory.getInstance().getBibliotecario();

		return amministrazioneBibliotecario;

	}

	public ProfilerManager() {
		log.info("=== ProfilerManager.Constructor() ...");
	}

	public void startService() throws Exception {

		try {
			log.info("=== ProfilerManager.startService()");
			//almaviva5_20100129
			//la conf. di log4j viene caricata dopo la partenza del servizio
			//quindi per intercettare la creazione dei logger é necessario impostare
			//un listener per modificare i livelli di logging.
			Logger.getRootLogger().getLoggerRepository().addHierarchyEventListener(this);

			setRedeployWarningActive(false);

			this.reload();

		} finally {
			//almaviva5_20140120
			serverStartupTimestamp = DaoManager.now();
		}
	}

	public void stopService() throws Exception {
		log.info("=== ProfilerManager.stopService()");
	}

	public void reload() throws Exception {

		CommonConfiguration.reload();

		//almaviva5_20100511
		String lang = CommonConfiguration.getProperty(Constants.LOCALE_LANG, Locale.ITALY.getLanguage());
		String country = CommonConfiguration.getProperty(Constants.LOCALE_COUNTRY, Locale.ITALY.getCountry());
		Locale.setDefault(new Locale(lang, country));

		getAmministrazionePolo().loadParameterPolo();
		this.clearCache();
		this.loadRepertori();

		this.getProfiloIndice(true);

		this.checkProfiloIndicePolo();
		this.reloadCodici();

		try {
			getAmministrazioneMail().reload();
		} catch (Exception e) {
			log.error("", e);
		}

		reloadLoggerConf();

		SbnWebProfileCache.getInstance().clear();

		//almaviva5_20150605 pulizia cache script
		JsScriptExecutor.clear();
	}

	private void reloadLoggerConf() throws Exception {
		//almaviva5_20100118 livelli log sbnweb
		String debug = Level.DEBUG.toString();
		setSbnWebLoggingLevel(CommonConfiguration.getProperty("LOG_LEVEL_SBNWEB", debug));
		setSbnMarcLoggingLevel(CommonConfiguration.getProperty("LOG_LEVEL_SBNMARC", debug));
		setHibernateLoggingLevel(CommonConfiguration.getProperty("LOG_LEVEL_HIBERNATE", debug));
	}

	public void rimuoviProfiliSbnMarc() {
		log.info("=== ProfilerManager.rimuoviProfiliSbnMarc()");
		try {
			getAmministrazionePolo().rimuoviProfiliSbnMarc();
			SbnWebProfileCache.getInstance().clear();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void clearCache() {
		try {
			getAmministrazionePolo().clearDBCache();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void reloadCodici() {
		log.info("=== ProfilerManager.reloadCodici()");
		//almaviva5_20111104 la modifica dei codici può influenzare i profili
		SbnWebProfileCache.getInstance().clear();
		CodiciProvider.invalidate();
		try {
			getAmministrazionePolo().reloadCodiciSbnMarc();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/*
	 * Inizializza i Repertori
	 */
	private void loadRepertori() {
		log.info("=== ProfilerManager.loadRepertori()");
		try {
			Repertorio rep = DomainEJBFactory.getInstance().getRepertorio();
			rep.bindRepertori();

		} catch (NamingException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		} catch (CreateException e) {
			log.error("", e);
		} catch (DaoManagerException e) {
			log.error("", e);
		}

	}

	public SbnProfileType getProfiloIndice(boolean force) {
		log.info("=== ProfilerManager.loadProfile()");
		if (!force && profiloIndice != null)
			return profiloIndice;

		profiloIndice = recoveryProfile();
		return profiloIndice;
	}

	private void checkProfiloIndicePolo() {
		try {
			getAmministrazionePolo().checkProfiloIndicePolo();

		} catch (Exception e) {
			log.error("", e);
		}

	}

	private SbnProfileType recoveryProfile() {
		try {
			AmministrazionePolo amministrazione = getAmministrazionePolo();

			amministrazione.loadCodiciAttivita(); // Carica i codici attività da DB e li mette sull'albero jndi

			//amministrazione.loadParameterPolo();
			Credentials credentials = amministrazione.getCredentials();
			ParametriHttp paraIndice = amministrazione.getIndice();
			SbnUserType profile = amministrazione.getUserSbnMarc();

			boolean isDebug = Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.SBNMARC_LOCAL_DEBUG, "false"));
			if (isDebug)
				return null;

			FactorySbnHttp server = new FactorySbnHttpIndice("INDICE", new ServerHttp(paraIndice, credentials) );
			SbnProfileDao profiler = new SbnProfileDao(server, profile, profile);
			SbnProfileType poloProfile = profiler.profile();

			return poloProfile;

		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

	private void internalSetLoggingLevel(String[] filters, Level newLevel) {

		if (!ValidazioneDati.isFilled(filters) )
			return;

		Enumeration<?> loggers = Logger.getRootLogger().getLoggerRepository().getCurrentLoggers();
		while (loggers.hasMoreElements() ) {
			Logger logRef = (Logger) loggers.nextElement();
			String name = logRef.getName().toLowerCase();

			for (String filter : filters) {
				if (filter.endsWith(".")) {
					if (name.startsWith(filter.toLowerCase()))
						logRef.setLevel(newLevel);
				} else
					if (name.equals(filter.toLowerCase()))
						logRef.setLevel(newLevel);
			}
		}
	}

	private String xmlPrettyPrint(String xml) throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.parse(new InputSource(new StringReader(xml)));
		Document doc =  parser.getDocument();

        OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(80);
        format.setIndenting(true);
        format.setIndent(2);

        Writer output = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(doc);

        return output.toString();
	}


	public String getHibernateLoggingLevel() {
		return hibLoggingLevel.toString();
	}

	public String getSbnMarcLoggingLevel() {
		return sbnmarcLoggingLevel.toString();
	}

	public String getSbnWebLoggingLevel() {
		return sbnwebLoggingLevel.toString();
	}

	public void setHibernateLoggingLevel(String level) throws Exception {
		Level newLevel = Level.toLevel(level, Level.DEBUG);

		hibLoggingLevel = newLevel;
		internalSetLoggingLevel(HIBERNATE_LOGGERS, hibLoggingLevel);
		CommonConfiguration.setProperty("LOG_LEVEL_HIBERNATE", hibLoggingLevel.toString());
	}

	public void setSbnMarcLoggingLevel(String level) throws Exception {
		Level newLevel = Level.toLevel(level, Level.DEBUG);

		sbnmarcLoggingLevel = newLevel;
		internalSetLoggingLevel(SBNMARC_LOGGERS, sbnmarcLoggingLevel);
		CommonConfiguration.setProperty("LOG_LEVEL_SBNMARC", sbnmarcLoggingLevel.toString());
	}

	public void setSbnWebLoggingLevel(String level) throws Exception {
		Level newLevel = Level.toLevel(level, Level.DEBUG);

		sbnwebLoggingLevel = newLevel;
		internalSetLoggingLevel(SBNWEB_LOGGERS, sbnwebLoggingLevel);
		CommonConfiguration.setProperty("LOG_LEVEL_SBNWEB", sbnwebLoggingLevel.toString());
	}

	public String printXMLProfiloIndice() throws Exception {
		SbnProfileType profile = getProfiloIndice(false);
		if (profile == null)
			return "";

		Writer out = new StringWriter();
		profile.marshal(out);
		String xml = out.toString();

		return StringEscapeUtils.escapeHtml(xmlPrettyPrint(xml));
	}

	public void addAppenderEvent(Category category, Appender appender) {
		try {
			reloadLoggerConf();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void removeAppenderEvent(Category category, Appender appender) {
		return;
	}

	public boolean isRedeployWarningActive() throws Exception {
		return Boolean.valueOf(CommonConfiguration.getProperty(Configuration.REDEPLOY_WARNING_MESSAGE, "FALSE"));
	}

	public void setRedeployWarningActive(boolean flag) throws Exception {
		CommonConfiguration.setProperty(Configuration.REDEPLOY_WARNING_MESSAGE, String.valueOf(flag) );
	}

	public void resetRootPassword() throws Exception {
		getAmministrazioneBibliotecario().resetRootPassword();
	}

	public void testMailServer(String toAddress) throws Exception {
		MailUtil.testMailServer(toAddress);
	}

	public void testSMSProvider(String rcvNumber, String text) throws Exception {
		SMSResult sms = SMSUtil.send("sbnweb", rcvNumber, text, true);
		log.debug(sms);

	}

	public String printConfiguration() throws Exception {
		return CommonConfiguration.print();
	}

	public String getServerStartupTimestamp() {
		//almaviva5_20140120
		if (serverStartupTimestamp == null)
			serverStartupTimestamp = DaoManager.now();

		return serverStartupTimestamp.toString();
	}

	public boolean isIndiceAvailable() throws Exception {
		//almaviva5_20140303 evolutive google3
		return FactorySbnHttpIndice.isAvailable();
	}

	public void setIndiceAvailable(boolean avail) throws Exception {
		//almaviva5_20140303 evolutive google3
		FactorySbnHttpIndice.setAvailable(avail);
	}

	public void setConfigurationProperty(String key, String value)
			throws Exception {
		if (ValidazioneDati.isFilled(key) && ValidazioneDati.notEmpty(key))
			CommonConfiguration.setProperty(key, value);
	}

	public String printMailConfiguration() throws Exception {
		MailProperties mp = getAmministrazioneMail().getPoloMailProperties();
		if (mp == null)
			return "";
		
		return mp.toString();
	}

}
