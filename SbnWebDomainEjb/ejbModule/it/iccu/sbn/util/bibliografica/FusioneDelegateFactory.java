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
package it.iccu.sbn.util.bibliografica;

import it.iccu.sbn.extension.bibliografica.FusioneDatiGestionaliDelegate;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import org.apache.log4j.Logger;

public class FusioneDelegateFactory {

	private static Logger log = Logger.getLogger(FusioneDelegateFactory.class);

	public static final FusioneDatiGestionaliDelegate getDelegate() throws Exception {
		String delegateClass = CommonConfiguration.getProperty(Configuration.FUSIONE_DELEGATE_CLASS);
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(delegateClass);

		log.info("Creazione delegate: " + delegateClass);
		FusioneDatiGestionaliDelegate delegate = (FusioneDatiGestionaliDelegate) clazz.newInstance();

		return delegate;
	}

}
