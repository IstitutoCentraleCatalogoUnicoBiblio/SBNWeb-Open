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
package it.iccu.sbn.command;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.Serializable;
import java.util.Collection;

public final class CommandResultVO extends SerializableVO {

	private static final long serialVersionUID = -7747715240085181667L;

	private final String id;
	private final String ticket;
	private final CommandType command;
	private final Serializable result;
	private final Throwable error;

	public static final CommandResultVO build(CommandInvokeVO command,
			Serializable result, Throwable error) {
		return new CommandResultVO(command, result, error);
	}

	public static final CommandResultVO build(CommandInvokeVO command,
			Serializable result) {
		return new CommandResultVO(command, result, null);
	}

	public static CommandResultVO build(CommandInvokeVO command) {
		return build(command, null, null);
	}

	public String getId() {
		return id;
	}

	public String getTicket() {
		return ticket;
	}

	public CommandType getCommand() {
		return command;
	}

	public Serializable getResult() {
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> getResultAsCollection(Class<T> clazz) {
		if (result == null)
			return null;

		if (result instanceof Collection)
			return (Collection<T>) result;

		throw new ClassCastException();
	}

	public Throwable getError() {
		return error;
	}

	private CommandResultVO(CommandInvokeVO command, Serializable result, Throwable error) {
		super();
		this.id = command.getId();
		this.ticket = command.getTicket();
		this.command = command.getCommand();
		this.result = result;
		this.error = error;
	}

	public void throwError() throws Exception {
		if (error != null)
			throw (Exception)error;
	}

}
