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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;

public final class CommandInvokeVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = -5176624441352487327L;

	private final String id = getCommandId();
	private final String ticket;
	private final CommandType command;
	private final Serializable[] params;

	public static final CommandInvokeVO build(String ticket,
			CommandType command, Serializable... params) {
		return new CommandInvokeVO(ticket, command, params);
	}

	public CommandInvokeVO(String ticket, CommandType command, Serializable... params) {
		super();
		this.ticket = ticket;
		this.command = command;
		this.params = params;
	}

	private final String getCommandId() {
		StringBuilder buf = new StringBuilder();
		buf.append(uniqueId).append('-').append(creationTime.getTime());
		return buf.toString();
	}

	public CommandInvokeVO(String ticket, CommandType command) {
		super();
		this.ticket = ticket;
		this.command = command;
		this.params = null;
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

	public Serializable[] getParams() {
		return params;
	}

	@Override
	public void validate() throws ValidationException {
		if (!isFilled(ticket))
			throw new ValidationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);
		if (command == null)
			throw new ValidationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

		Class<?>[] signature = command.getSignature();
		if (signature == null)
			return;

		if (params == null)
			throw new ValidationException(SbnErrorTypes.COMMAND_INVOKE_METHOD_SIGNATURE);
		if (signature.length != params.length)
			throw new ValidationException(SbnErrorTypes.COMMAND_INVOKE_METHOD_SIGNATURE);

		for (int p = 0; p < params.length; p++) {
			Serializable param = params[p];
			if (param != null && !signature[p].isInstance(param))
				throw new ValidationException(SbnErrorTypes.COMMAND_INVOKE_METHOD_SIGNATURE);
		}

	}

}


