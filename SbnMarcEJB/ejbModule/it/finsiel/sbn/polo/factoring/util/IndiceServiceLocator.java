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
package it.finsiel.sbn.polo.factoring.util;

import it.finsiel.sbn.polo.exception.ServiceLocatorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

/**
 * <p>
 * IndiceServiceLocator
 * <p>
 * Questa classe di utilità è stata inserita allo scopo di fornire una interfaccia
 * ulteriore a quella fornita dal ServiceLocator.
 * Ogni metodo statico è stato definito per recuperare quelle informazioni
 * richieste dall'albero JNDI impostando il contesto specifico definito per l'Indice.
 *
 * @author
 * @version 1.1
 */
public class IndiceServiceLocator {
	/**
	* Log per il DEBUG
	*/
	static Category log = Category.getInstance(IndiceServiceLocator.class);
	/**
	 * Context properties
	 */
	public static final String PROPERTY_CONTEXT	= "property";
	/**
	 * Context sql
	 */
	public static final String TAVOLE_CONTEXT 	= "tavole";
	/**
	 * Context exception
	 */
	public static final String ERRORI_CONTEXT = "errori";
	/**
	 * Context generic objects
	 */
	public static final String OBJECT_CONTEXT 	= "object";
	/**
	 * Context codici tb_codici
	 */
	public static final String CODICI_CONTEXT 	= "codici";
	/**
	 * Context root for INDICE
	 */
	public static final String INDICE_CONTEXT 	= "INDICE";
	/**
	 * Context test
	 */
	public static final String TEST_CONTEXT = "test";

	/**
	 * Context separator
	 */
	public static final char CONTEXT_SEPARATOR	= '/';

	/**
	 * UserTransaction
	 */
	private static final String USER_TRANSACTION 	= "java:comp/UserTransaction";


	public static Enumeration getPropertyNames() throws ServiceLocatorException
	{
		List list = new ArrayList();
		try
		{
  			Context propertyContext = (Context)ServiceLocator.getInstance().getObject(
  				INDICE_CONTEXT + CONTEXT_SEPARATOR + PROPERTY_CONTEXT);
  			NamingEnumeration jndi = propertyContext.list("");
			while (jndi.hasMore()) {
    			NameClassPair nc = (NameClassPair)jndi.next();
    			list.add(nc.getName());
    		}
    	} catch (NamingException ne) {
			ne.printStackTrace();
		} catch (ServiceLocatorException sle) {
			sle.printStackTrace();
		}
		return Collections.enumeration(list);

	}


	/**
	 * Ritorna una proprietà di sistema mappata nell'albero JNDI
	 *
	 * @param name nome jndi della proprietà
	 * @return valore della proprietà
	 */
	public static String getProperty(String name) throws ServiceLocatorException
	{

        ServiceLocator.getInstance().getString(INDICE_CONTEXT + CONTEXT_SEPARATOR + PROPERTY_CONTEXT + CONTEXT_SEPARATOR + name);
		return ServiceLocator.getInstance().getString(
			INDICE_CONTEXT + CONTEXT_SEPARATOR + PROPERTY_CONTEXT + CONTEXT_SEPARATOR + name);
	}

	/**
	 * Ritorna una transazione utente
	 * @return UserTransaction
	 */
	public static UserTransaction getUserTransaction() throws ServiceLocatorException
	{
		return (UserTransaction)ServiceLocator.getInstance().getObject(getProperty("USERTRANSACTION_JNDINAME"));
	}


}
