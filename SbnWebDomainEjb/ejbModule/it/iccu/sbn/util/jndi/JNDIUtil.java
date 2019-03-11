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
package it.iccu.sbn.util.jndi;

import java.rmi.Remote;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDIUtil {

	public static final InitialContext getContext() throws NamingException {
		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		p.put(Context.URL_PKG_PREFIXES, "jboss.naming:org.jnp.interfaces");
		//p.put(javax.naming.Context.PROVIDER_URL, "localhost");
		//p.put("jnp.partitionName", "DefaultPartition"); // partition name.
		return new InitialContext(p);
	}

	public static final Object lookup(String jndiName, Class<? extends Remote> narrowTo)
			throws javax.naming.NamingException {

		InitialContext ctx = getContext();
		try {
			Object objRef = ctx.lookup(jndiName);
			if (Remote.class.isAssignableFrom(narrowTo))
				return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
			else
				return objRef;
		} finally {
			ctx.close();
		}
	}

	public static final Object lookupHome(Hashtable<String, String> env, String jndiName, Class<?> narrowTo)
			throws javax.naming.NamingException {

		InitialContext initialContext = env != null ? new InitialContext(env) : getContext();
		try {
			Object objRef = initialContext.lookup(jndiName);
			// only narrow if necessary
			if (java.rmi.Remote.class.isAssignableFrom(narrowTo))
				return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
			else
				return objRef;
		} finally {
			initialContext.close();
		}
	}

}
