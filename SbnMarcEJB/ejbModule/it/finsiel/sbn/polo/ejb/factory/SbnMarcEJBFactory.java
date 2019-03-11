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
package it.finsiel.sbn.polo.ejb.factory;

import it.finsiel.sbn.polo.factoring.util.ResourceLoader;


public class SbnMarcEJBFactory {

	private static SbnMarcEJBFactoryIntf factory;

	static {
		//almaviva5_20140723
		try {
			String factoryClass = ResourceLoader.getPropertyString("EJB_FACTORY_CLASS");
			Class<?> clazz = Class.forName(factoryClass);
			factory = (SbnMarcEJBFactoryIntf) clazz.newInstance();//new JNDISbnMarcEJBFactory();
		} catch (Exception e) {	}
	}

	public static final SbnMarcEJBFactoryIntf getFactory() {
		return factory;
	}

}
