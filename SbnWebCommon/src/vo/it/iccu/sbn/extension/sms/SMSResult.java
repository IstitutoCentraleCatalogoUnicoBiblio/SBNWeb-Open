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
package it.iccu.sbn.extension.sms;

import java.io.Serializable;

/**
 * Risultato di un invio SMS tramite l'interfaccia {@link SMSProvider}
 * @author iccu
 *
 */
public class SMSResult implements Serializable {

	private static final long serialVersionUID = 3905218450658283909L;

	private boolean success = false;
	private int errorCode;
	private String message;

	public SMSResult() {
		super();
	}

	public SMSResult(boolean success) {
		super();
		this.success = success;
	}

	private int msgCount;

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SMSResult [success=" + success + ", errorCode=" + errorCode
				+ ", " + (message != null ? "message=" + message + ", " : "")
				+ "msgCount=" + msgCount + "]";
	}

}
