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
package it.iccu.sbn.util.sms;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.UTF8Decomposer;
import it.iccu.sbn.extension.sms.SMSProvider;
import it.iccu.sbn.extension.sms.SMSResult;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.sms.impl.DummySMSProvider;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

import org.apache.log4j.Logger;

public class SMSUtil {

	private static Logger log = Logger.getLogger(SMSUtil.class);

	private static SMSProvider DUMMY = new DummySMSProvider();

	private static List<SMSProvider> providers = new ArrayList<SMSProvider>();

	static {
		Iterator<SMSProvider> i = ServiceRegistry.lookupProviders(SMSProvider.class);
		while (i.hasNext()) {
			SMSProvider provider = i.next();
			log.debug("SMSProvider impl: " + provider);
			providers.add(provider);
		}
	}

	public static final SMSResult send(String mittente, String numTelefono, String testo, boolean notifica) throws ApplicationException {
		SMSResult result = new SMSResult();
		try {
			SMSProvider provider = getProvider();
			if (provider == null) {
				return DUMMY.send(mittente, numTelefono, testo, notifica);
			}

			String norm = normalize(testo);
			if (!ValidazioneDati.isFilled(norm)) {
				result.setSuccess(false);
				result.setMessage("Testo del messaggio non valido");
				return result;
			}

			result = provider.send(mittente, numTelefono, norm, notifica);

		} catch (Exception e) {
			log.error("", e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}

		return result;

	}


	private static String normalize(final String text) throws UnsupportedEncodingException {
		//almaviva5_20130208 #5254 eliminazione accenti con apostrofo
		String norm = UTF8Decomposer.getInstance().decompose(text);
		norm = norm.replaceAll("[\\u0300\\u0301]", "'");	//accenti (grave & acuto)
		norm = norm.replaceAll("[^\\p{ASCII}]", "");	//non ASCII

		return norm;
	}

	private static SMSProvider getProvider() throws Exception {

		if (providers.size() > 1) {
			log.warn("Troppi SMSProvider definiti");
			return DUMMY;
		}

		SMSProvider provider = ValidazioneDati.first(providers);
		if (provider == null) {
			//fallback
			provider = getFallbackProvider();
		}

		return provider;
	}

	static SMSProvider fb_provider = null;

	private static SMSProvider getFallbackProvider() throws Exception {

		if (fb_provider == null) {
			String providerClass = CommonConfiguration.getProperty(Configuration.SMS_PROVIDER_CLASS, DUMMY.getClass().getCanonicalName());
			Class<?> classe = Thread.currentThread().getContextClassLoader().loadClass(providerClass);
			log.info("Creazione SMSProvider: " + providerClass);
			fb_provider = (SMSProvider) classe.newInstance();
		}

		return fb_provider;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(normalize("àèìòù \u00D8\u00D9\u00DA\u00DB\u00DC"));
	}

}
