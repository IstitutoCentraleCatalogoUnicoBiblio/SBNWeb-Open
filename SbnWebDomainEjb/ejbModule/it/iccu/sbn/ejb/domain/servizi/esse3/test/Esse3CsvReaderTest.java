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
package it.iccu.sbn.ejb.domain.servizi.esse3.test;

import it.iccu.sbn.ejb.domain.servizi.esse3.csv.Esse3DataManager.Esse3OperationType;
import it.iccu.sbn.ejb.domain.servizi.esse3.csv.Esse3DataManagerImpl;

public class Esse3CsvReaderTest {
	// C:\Users\l.ferreroviscardi\Desktop\LUMSA\esse3\UTENTIKOHA20171127000312
	static String path = "C:\\Users\\l.ferreroviscardi\\Desktop\\LUMSA\\esse3\\UTENTIKOHA20171127000312\\koha_data.csv";

	public static void main(String[] args) {
		Esse3DataManagerImpl esse3 = new Esse3DataManagerImpl(Esse3OperationType.INSERT_FROM_CSV, new String());
		esse3.manage("IEI", "LU", path);
	}

}
