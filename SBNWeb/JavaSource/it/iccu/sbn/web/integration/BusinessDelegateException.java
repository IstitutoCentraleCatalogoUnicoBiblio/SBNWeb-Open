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
package it.iccu.sbn.web.integration;

public class BusinessDelegateException extends Exception {
	private static final long serialVersionUID = 7222714382926850789L;

	private Exception exception;

	/**
	 * Creates a new BusinessDelegateException wrapping another exception, and
	 * with a detail message.
	 *
	 * @param message
	 *            the detail message.
	 * @param exception
	 *            the wrapped exception.
	 */
	public BusinessDelegateException(String message, Exception exception) {
		super(message);
		this.exception = exception;
		return;
	}

	/**
	 * Creates a BusinessDelegateException with the specified detail message.
	 *
	 * @param message
	 *            the detail message.
	 */
	public BusinessDelegateException(String message) {
		this(message, null);
		return;
	}

	/**
	 * Creates a new BusinessDelegateException wrapping another exception, and
	 * with no detail message.
	 *
	 * @param exception
	 *            the wrapped exception.
	 */
	public BusinessDelegateException(Exception exception) {
		this(null, exception);
		return;
	}

	/**
	 * Gets the wrapped exception.
	 *
	 * @return the wrapped exception.
	 */
	public Exception getException() {
		return exception;
	}

}
