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
package it.iccu.sbn.ejb.domain.servizi.esse3.csv;

import it.iccu.sbn.ejb.domain.servizi.esse3.csv.Esse3DataManager.Esse3OperationType;

public class Esse3DataManagerBuilder {

	private Esse3OperationType operationToDo;
	private String ticket;

	public Esse3DataManagerBuilder() {
		super();

	}
	public Esse3DataManagerBuilder setOperationToDo(Esse3OperationType operationToDo) {
		this.operationToDo = operationToDo;
		return this;
	}
	public Esse3DataManagerBuilder setTicket(String ticket) {
		this.ticket = ticket;
		return this;
	}
	public Esse3DataManager build() {
		return new Esse3DataManagerImpl(operationToDo, ticket);
	}

}
