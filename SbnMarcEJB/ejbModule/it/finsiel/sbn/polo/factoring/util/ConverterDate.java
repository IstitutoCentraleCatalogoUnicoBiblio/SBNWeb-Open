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

import java.sql.Timestamp;
import java.util.Date;

public class ConverterDate {

	public ConverterDate() {
		super();
	}

	public static Date SbnDataVarToDate(SbnDatavar dataVar) {
		return new Date(dataVar.getMillis());
	}

	public static Timestamp SbnDataVarToDate(String timestamp) {
		if (!ValidazioneDati.isFilled(timestamp))
			return null;

		SbnDatavar sbn = new SbnDatavar(timestamp);
		Timestamp ts = new Timestamp(sbn.getMillis());
		//almaviva5_20100209 imposto anche i nanosecondi
		ts.setNanos(sbn.getNanos());
		return ts;
	}

}
