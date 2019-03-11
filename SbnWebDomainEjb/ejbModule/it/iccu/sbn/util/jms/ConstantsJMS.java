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
package it.iccu.sbn.util.jms;

import java.lang.reflect.Field;
import java.util.HashSet;

public class ConstantsJMS {

	public static final String ID_CODA                          = "ID_CODA";
	public static final String ID_BATCH							= "ID_BATCH";
	public static final String ID_BLOCCO						= "ID_BLOCCO";
	public static final String STATO							= "STATO";
	public static final String COD_ATTIVITA						= "COD_ATTIVITA";
	public static final String DATA								= "DATA";
	public static final String LOG								= "LOG";
	public static final String DATA_ELABORAZIONE				= "DATA_ELABORAZIONE";
	public static final String DATA_FINE_ELABORAZIONE			= "DATA_FINE_ELABORAZIONE";
	public static final String BIBLIOTECA   			        = "BIBLIOTECA";
	public static final String BIBLIOTECARIO			        = "BIBLIOTECARIO";
	public static final String DATA_ORA_RICHIESTA        		= "DATA_ORA_RICHIESTA";
	public static final String DATA_ORA_ESECUZIONE_PROGRAMMATA	= "DATA_ORA_ESECUZIONE_PROGRAMMATA";
	public static final String CODA   			        		= "CODA";
	public static final String VISIBILITA		        		= "VISIBILITA";
	public static final String VISIBILITA_POLO					= "P";
	public static final String VISIBILITA_BIB					= "B";
	public static final String AZIONE   			        	= "AZIONE";

	public static final String STATO_SEND  = "SEND";
	public static final String STATO_HELD  = "HELD";
	public static final String STATO_EXEC  = "EXEC";
	public static final String STATO_OK    = "OK";
	public static final String STATO_ERROR = "ERROR";

	public static final String JMSCorrelationID					= "JMSCorrelationID";
	public static final String JMSTimestamp 					= "JMSTimestamp";

	public static final int RECEIVE_TIMEOUT					= 5 * 1000;

	public enum BatchStateType {

		SEND		(ConstantsJMS.STATO_SEND),
		HELD		(ConstantsJMS.STATO_HELD),
		EXEC		(ConstantsJMS.STATO_EXEC),
		OK			(ConstantsJMS.STATO_OK),
		ERROR		(ConstantsJMS.STATO_ERROR);

		private final String state;

		private BatchStateType(String state) {
			this.state = state;
		}

		public String getState() {
			return state;
		}

	}

	public static final String AZIONE_ELABORAZIONE_DIFFERITA  = "ELABORAZIONE_DIFFERITA";


	private static HashSet<String> consts;

	static {
		consts = new HashSet<String>();
		Field[] fields = ConstantsJMS.class.getFields();
		for (Field f : fields)
			consts.add(f.getName());

	}

	public static final boolean contains(String key) {
		return consts.contains(key);
	}

}
