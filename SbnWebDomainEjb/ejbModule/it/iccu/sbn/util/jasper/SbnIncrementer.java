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
package it.iccu.sbn.util.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.AbstractValueProvider;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.fill.JRIncrementer;

public class SbnIncrementer implements JRIncrementer {

	private final byte calc;

	public SbnIncrementer(byte calc) {
		this.calc = calc;
	}

	public Object increment(JRFillVariable v,
			java.lang.Object expr,
			AbstractValueProvider Provider) throws JRException {
		Integer cnt = (Integer) v.getValue();
		return (cnt != null) ? ++cnt : null;
	}

	public byte getCalc() {
		return calc;
	}

}
