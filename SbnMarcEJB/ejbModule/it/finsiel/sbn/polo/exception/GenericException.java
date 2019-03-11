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
package it.finsiel.sbn.polo.exception;

import org.apache.log4j.Category;

/**
 * Classe GenericException
 * <p>
 *  Classe che implementa le Eccezioni Generiche che possono sollevarsi
 *  durante l'esecuzione.
 * </p>
 * @author
 * @author
 */
public class GenericException extends Exception
{

	private static final long serialVersionUID = 8491312516550928395L;

	static Category log = Category.getInstance("it.finsiel.sbn.polo.exception.GenericException");

	private Exception exception;
	private String message;
	private int errorcode;

	/**
     * Costruttore - GenericException
	 *
	 * @param void
	 * @return istanza della classe
     */

	public GenericException()
	{
		super();
	}


	public GenericException(String message)
	{
		super(message);
	}

    public GenericException(String message, int errorcode)
	{
		super(message);
		this.errorcode = errorcode;
	}

	/**
     * Costruttore - GenericException
	 *
	 * @param Exception exception
	 * @return istanza della classe
     */

	public GenericException(Exception exception)
	{
		super();
		this.exception = exception;
	}

	public GenericException(Exception exception, String message, int errorcode)
	{
		super(message);
		this.exception = exception;
		this.errorcode = errorcode;
	}

	public GenericException(Exception exception, String message)
	{
		super(message);
		this.exception = exception;
	}

	public static GenericException getInstance()
	{
		return new GenericException();
	}

	public static GenericException getInstance(Exception exception)
	{
		return new GenericException(exception);
	}

	/**
     * toString
	 *
	 * @param  void
	 * @return String stringa messaggio dell'eccezione
     */
	public String toString()
	{
		return exception.toString();
	}

	public void setErrorCode(int errorcode)
	{
		this.errorcode = errorcode;
	}

	public int getErrorCode()
	{
		return this.errorcode;
	}


	/**
	 * Gets the exception.
	 * @return Returns a Exception
	 */
	public Exception getException()
	{
		return exception;
	}

	/**
	 * Sets the exception.
	 * @param exception The exception to set
	 */
	public void setException(Exception exception)
	{
		this.exception = exception;
	}

}
