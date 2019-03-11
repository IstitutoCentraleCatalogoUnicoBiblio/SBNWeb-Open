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
package it.iccu.sbn.ejb.utils.unicode;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.Map;


/**
 *  utility per convertire UCS/Unicode in caratteri per ordinamento tutti Maiuscoli
 *
 */
public class OrdinamentoUnicode {

	private static final OrdinamentoUnicode instance = new OrdinamentoUnicode();

	private Map<Integer, String> codePoints;


	public static final OrdinamentoUnicode getInstance() {
		return instance;
	}

    public OrdinamentoUnicode() {
		super();
		try {
			codePoints = SbnUnicodeMapping.getInstance().getCodePoints();
		} catch (InfrastructureException e) {
			e.printStackTrace();
		}
	}

	public String convert(String data) {
		if (data == null)
			return null;
    	return internalConvert(data.toCharArray());
    }

    /**
     * @param data      UCS/Unicode data
     * @return char[] - caratteri per odinamento
     */

    private String internalConvert(char[] data) {

    	int length = data.length;
    	int capacity = (length * 3) / 2 + 1;
		StringBuilder sb = new StringBuilder(capacity);

    	for(int i = 0; i < length; i++) {
    		char c = data[i];
    		int d = map(c);

    		if (d < 0) {
    			continue;
    		} else {
    			if (d < 256) {
    				sb.append((char)d);
    			} else {
    				sb.append((char)(d / 256));
    				sb.append((char)(d % 256));
    			}
    		}
    	}

    	return sb.toString();
    }

    protected int map(int i) {

    	if (i >= 0x61 && i <= 0x7A)
    		return (i - 0x20); //minuscole-->maiuscole

    	if (i == 0x20 || (i >= 0x41 && i <= 0x5A) || (i >= 0x30 && i <= 0x39)) return i;

//		if (i == 0x3C || i == 0x3E)
//			return i; 	// <, >

		/*

    	switch (i) {
    		case 0x0027: return 0x20;    // ' ->ritorna spazio
    		case 0x0023: return 0x20;    // # ->ritorna spazio
    		case 0x0026: return 0x20;    // & ->ritorna spazio
    		case 0x003C: return 0x3C;    // <
    		case 0x003E: return 0x3E;    // >
    		case 0x005E: return 0x20;    // ^ ->ritorna spazio
    		case 0x0060: return 0x20;    // ` ->ritorna spazio
            case 0x002D: return -1;      // -
    	}

    	*/

    	String ordKey = codePoints.get(i);
    	if (ValidazioneDati.notEmpty(ordKey))
    		return ordKey.codePointAt(0);

    	return -1;
    }

}
