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
package it.iccu.sbn.util.config;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

public class CommonConfiguration {

	private static Logger log = Logger.getLogger(CommonConfiguration.class);

	private static final CommonConfiguration instance = new CommonConfiguration();

	private Lock r;
	private Lock w;
	private AtomicBoolean initialized;

	private Map<String, String> properties;
	private Set<ConfigInterceptor> interceptors;

	private enum NotificationType {
		PROPERTY,
		RELOAD;
	}

	private class ChangeNotifier extends Thread {

		private final NotificationType type;
		private final CommonConfiguration instance;
		private final Set<String> changedProperties;
		private final String property;

		public ChangeNotifier(CommonConfiguration instance, Set<String> changedProperties) {
			this.type = NotificationType.RELOAD;
			this.instance = instance;
			this.changedProperties = changedProperties;
			this.property = null;
		}

		public ChangeNotifier(CommonConfiguration instance, String property) {
			this.type = NotificationType.PROPERTY;
			this.instance = instance;
			this.property = property;
			this.changedProperties = null;
		}

		@Override
		public void run() {
			synchronized (instance.interceptors) {
				Iterator<ConfigInterceptor> i = instance.interceptors.iterator();
				while (i.hasNext()) {
					ConfigInterceptor ref = i.next();
					ConfigChangeInterceptor interceptor = ref.getReference();
					if (interceptor == null) {	//eliminato dal gc
						i.remove();
						continue;
					}

					try {
						switch (type) {
						case RELOAD:	// reload lista di property
							interceptor.onConfigReload(changedProperties);
							break;
						case PROPERTY:	// singola property
							if (ref.isInterested(property))
								interceptor.onConfigPropertyChange(property);
							break;
						}

					} catch (Exception e) {
						log.error("", e);
					}
				}
			}

		}
	}

	private CommonConfiguration() {
		ReadWriteLock _lock = new ReentrantReadWriteLock();
		r = _lock.readLock();
		w = _lock.writeLock();
		initialized = new AtomicBoolean(false);

		properties = new THashMap<String, String>();
		interceptors = Collections.synchronizedSet(new THashSet<ConfigInterceptor>());
	}


	private Set<String> initialize() throws Exception {

		if (initialized.get())
			return null;

		Set<String> changedProperties = new THashSet<String>();

		w.lock();
		try {
			//mentre eravamo in attesa del lock un altro
			//thread potrebbe aver aggiornato la config
			if (initialized.get())
				return null;

			initialized.set(false);

			try {
				Properties props = loadProperties();

				Set<Object> keys = props.keySet();
				for (Object k : keys) {
					String key = (String) k;
					//per ogni property cambiata devo prendere nota per
					//notificare gli oggetti registrati come interceptors
					if (noLockingSetProperty(key, props.getProperty(key) ))
						changedProperties.add(key);
				}

				initialized.set(true);

			} catch (Exception e) {
				log.error("", e);
				throw e;
			}

		} finally {
			w.unlock();
		}

		// notifico solo se ci sono stati cambiamenti
		return changedProperties;

	}

	private Properties loadProperties() throws Exception {
		Properties props = new Properties();

		log.debug("Lettura parametri di configurazione hard-coded.");
		InputStream in = getConfigStream(null);
		props.load(in);
		in.close();

		log.debug("Lettura parametri di configurazione personalizzati.");
		in = getConfigStream(System.getProperty("sbnweb.conf.path") );
		props.load(in);
		in.close();

		return props;
	}


	private boolean noLockingSetProperty(String key, String value) throws Exception {

		if (ValidazioneDati.strIsNull(key) )//|| ValidazioneDati.strIsNull(value))
			return false;

		String oldValue = properties.get(key);
		if (ValidazioneDati.isFilled(oldValue) && oldValue.equals(value))
			return false;	// nessun cambiamento

		properties.put(key, value);

		return true;
	}

	private InputStream getConfigStream(String path) throws Exception {

		if (path == null) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			path = this.getClass().getPackage().getName().replace('.', '/');
			String cname = path + "/sbnweb.conf";
			log.debug("caricamento configurazione da: '" + cname + "'");
			return loader.getResourceAsStream(cname);
		}

		String cname = path + File.separator + "sbnweb.conf";
		log.debug("caricamento configurazione da: '" + cname + "'");

		File f = new File(cname);
		return new FileInputStream(f);
	}


	private static final CommonConfiguration getInstance() {
		return instance;
	}

	private static final void check() throws Exception {
		final CommonConfiguration instance = getInstance();
		if (!instance.initialized.get() )
			instance.initialize();
	}

	public static final void reload() throws Exception {
		log.debug("reload configurazione");
		final CommonConfiguration instance = getInstance();
		instance.initialized.set(false);
		Set<String> changedProperties = instance.initialize();
		if (ValidazioneDati.isFilled(changedProperties))
			notifyInterceptorsOnReload(changedProperties);
	}

	public static final String getProperty(String key) throws Exception {
		if (ValidazioneDati.strIsNull(key))
			return null;

		check();
		final CommonConfiguration instance = getInstance();
		instance.r.lock();
		try {
			if (log.isDebugEnabled())
				log.debug("Property richiesta: '" + key +"'");
			return instance.properties.get(key);
		} finally {
			instance.r.unlock();
		}
	}

	public static final String getProperty(String key, String defaultValue) throws Exception {
		if (ValidazioneDati.strIsNull(key))
			return null;

		check();
		final CommonConfiguration instance = getInstance();
		instance.r.lock();
		try {
			if (!instance.properties.containsKey(key))
				return defaultValue;

			if (log.isDebugEnabled())
				log.debug("Property richiesta: '" + key +"'");
			return instance.properties.get(key);
		} finally {
			instance.r.unlock();
		}
	}

	public static final int getPropertyAsInteger(String key, int defaultValue) throws Exception {
		if (ValidazioneDati.strIsNull(key))
			return 0;

		check();
		final CommonConfiguration instance = getInstance();
		instance.r.lock();
		try {
			try {
				if (!instance.properties.containsKey(key))
					return defaultValue;

				if (log.isDebugEnabled())
					log.debug("Property richiesta: '" + key +"'");
				return Integer.valueOf(instance.properties.get(key));

			} catch (NumberFormatException e) {
				return defaultValue;
			}
		} finally {
			instance.r.unlock();
		}
	}

	public static final void addInterceptor(ConfigChangeInterceptor i, String... props) {
		if (i == null)
			return;

		final CommonConfiguration instance = getInstance();
		instance.interceptors.add(new ConfigInterceptor(i, props));
	}

	public static final void removeInterceptor(ConfigChangeInterceptor i) {
		if (i == null)
			return;

		final CommonConfiguration instance = getInstance();
		instance.interceptors.remove(new ConfigInterceptor(i));
	}

	public static final void setProperty(String key, String value) throws Exception {

		if (ValidazioneDati.strIsNull(key) || ValidazioneDati.strIsNull(value))
			return;

		String oldValue = getProperty(key);
		if (ValidazioneDati.isFilled(oldValue) && oldValue.equals(value))
			return;	// nessun cambiamento

		check();
		final CommonConfiguration instance = getInstance();
		instance.w.lock();
		try {
			instance.properties.put(key, value);
		} finally {
			instance.w.unlock();
		}

		notifyInterceptorsOnPropertyChange(key);
	}

	private static final void notifyInterceptorsOnReload(Set<String> changedProperties) {
		instance.new ChangeNotifier(instance, changedProperties).start();
	}

	private static final void notifyInterceptorsOnPropertyChange(String key) {
		instance.new ChangeNotifier(instance, key).start();
	}

	public static final String print() throws Exception {
		check();
		final CommonConfiguration instance = getInstance();
		instance.r.lock();
		try {
			StringBuilder buf = new StringBuilder(4096);
			String ls = System.getProperty("line.separator");
			final Map<String, String> props = instance.properties;
			for (String key : new TreeSet<String>(props.keySet()) )
				buf.append(key).append('=').append(props.get(key)).append(ls);

			return buf.toString();

		} finally {
			instance.r.unlock();
		}
	}

	public static void main(String... args) throws Exception {
		CommonConfiguration.addInterceptor(new ConfigChangeInterceptor() {

			public void onConfigPropertyChange(String key) {
				System.out.println("reload : " + key);
			}

			public void onConfigReload(Set<String> keys) throws Exception {
				return;
			}
		});

		System.out.println(CommonConfiguration.getProperty("TEST"));
		CommonConfiguration.setProperty("TEST_MOD", "test mod");
	}

}
