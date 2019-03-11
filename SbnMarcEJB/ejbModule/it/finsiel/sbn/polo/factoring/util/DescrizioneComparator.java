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

import java.util.Comparator;

/**
 * CLASSE PER il compare di dati presenti nell'oggetto SortedInfo
 * <p/>
 * Compare solo se si tratta di due stringhe (serve per descrizioni)
 * <p/>
 *
 * @author
 * @author
 */
public class DescrizioneComparator implements Comparator {

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object obj_1, Object obj_2)
	{
		if (obj_1 instanceof String &&
			obj_2 instanceof String)
		{
			String string_1 = (String)obj_1 ;
			String string_2 = (String)obj_2 ;

			return (string_1.compareToIgnoreCase(string_2)) ;
		}

		return 0;
	}

}
