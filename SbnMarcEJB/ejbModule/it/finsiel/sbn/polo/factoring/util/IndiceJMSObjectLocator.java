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

import java.io.Serializable;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class IndiceJMSObjectLocator {

	public IndiceJMSObjectLocator() {
		super();
		// TODO Auto-generated constructor stub
	}
    public static Serializable lookup(String name) throws ServiceLocatorException
    {
    	try {
			return JMSObjectLocator.getInstance().lookup(name);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceLocatorException(e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceLocatorException(e);
		}
    }
	public static void bind(String name, Serializable content) throws ServiceLocatorException
    {
		try {
			JMSObjectLocator.getInstance().bind(name, content);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceLocatorException(e);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceLocatorException(e);
		}
    }

}
