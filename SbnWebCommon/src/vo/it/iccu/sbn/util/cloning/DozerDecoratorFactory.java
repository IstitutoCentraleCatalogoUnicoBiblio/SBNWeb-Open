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
package it.iccu.sbn.util.cloning;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.lang.reflect.Constructor;

import net.sf.dozer.util.mapping.BeanFactoryIF;

public class DozerDecoratorFactory implements BeanFactoryIF {

	@SuppressWarnings("rawtypes")
	public Object createBean(Object src, Class clazz, String id) {
		try {
			Class<?> dest = Class.forName(id);
			//si gestisce solo l'ereditarietà diretta
			Class zuper = clazz.getSuperclass();

			if (ValidazioneDati.equals(zuper, dest))
				try {
					//cerco un costruttore adatto per istanziare l'oggetto
					//destinazione partendo dal sorgente
					@SuppressWarnings("unchecked")
					Constructor c = clazz.getConstructor(new Class[] {dest});
					return c.newInstance(src);
				} catch (Exception e) {	}

			//si ricade nel cloning dell'oggetto
			return ClonePool.deepCopy(src);

		} catch (Exception e) {
			return null;
		}
	}

}
