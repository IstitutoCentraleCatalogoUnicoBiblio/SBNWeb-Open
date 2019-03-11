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


/**
 * This class implements an exception which can wrapped a lower-level exception.
 *
 */
public class SchedulableException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = -7287214282765130165L;
private Exception exception;

  /**
   * Creates a new SchedulerException wrapping another exception, and with a detail message.
   * @param message the detail message.
   * @param exception the wrapped exception.
   */
  public SchedulableException(String message, Exception exception) {
    super(message);
    this.exception = exception;
    return;
  }

  /**
   * Creates a SchedulerException with the specified detail message.
   * @param message the detail message.
   */
  public SchedulableException(String message) {
    this(message, null);
    return;
  }

  /**
   * Creates a new SchedulerException wrapping another exception, and with no detail message.
   * @param exception the wrapped exception.
   */
  public SchedulableException(Exception exception) {
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

  /**
   * Retrieves (recursively) the root cause exception.
   *
   * @return the root cause exception.
   */
  public Exception getRootCause() {
    if (exception instanceof SchedulableException) {
      return ((SchedulableException) exception).getRootCause();
    }
    return exception == null ? this : exception;
  }

  public String toString() {
    if (exception instanceof SchedulableException) {
      return ((SchedulableException) exception).toString();
    }
    return exception == null ? super.toString() : exception.toString();
  }
}
