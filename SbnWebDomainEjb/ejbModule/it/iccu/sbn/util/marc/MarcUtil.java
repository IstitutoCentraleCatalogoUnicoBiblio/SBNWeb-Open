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
package it.iccu.sbn.util.marc;

import java.util.Scanner;

import org.marc4j.marc.DataField;
import org.marc4j.marc.MarcFactory;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.length;

public class MarcUtil {

	static MarcFactory marcFactory = MarcFactory.newInstance();

	static DataField DUMMY = marcFactory.newDataField();

	public static DataField string2field(String tag, String dati) {
		return string2field(tag, dati, '$');
	}

	public static DataField string2field(String tag, String dati, char separator) {

		if (!isFilled(tag) || !isFilled(dati))
			return DUMMY;

		DataField f = marcFactory.newDataField();
		f.setTag(tag);
		Scanner scanner = new Scanner(dati);
		try {
			scanner.useDelimiter("\\" + separator);
			while (scanner.hasNext()) {
				String subfield = scanner.next();
				if (length(subfield) < 2)
					continue;

				f.addSubfield(marcFactory.newSubfield(subfield.charAt(0), subfield.substring(1)));
			}
		} finally {
			scanner.close();
		}


		return f;
	}

}
