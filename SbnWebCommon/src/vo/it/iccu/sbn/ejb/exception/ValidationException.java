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
package it.iccu.sbn.ejb.exception;

import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.List;

public class ValidationException extends SbnBaseException {

	private static final long serialVersionUID = -7135785165459142833L;

	public static int successo = 2;
	public static int errore = 1;
	public static int validazione = 0;
	public static int diagnostico = 3;

	public static int erroreSoggetto = 0;
	public static int erroreClassificazione = 0;
	public static int erroreDescrittore = 1;
	public static int erroreValidazione = 2;
	public static int erroreDiagnostico = 3;
	public static int erroreNoDigitazione = 4;
	public static int erroreThesauro = 5;

	private List<?> lista;
	private int error;
	private String msg;
	private String bid;
	private String isbd;
	private String precInv;

	public ValidationException(SbnErrorTypes errorCode) {
		super(errorCode);
	}

	public ValidationException(SbnErrorTypes errorCode, String msg) {
		super(errorCode, msg);
		this.msg = msg;
	}

	public ValidationException(SbnErrorTypes errorCode, String... labels) {
		super(errorCode, labels);
	}

	public ValidationException(int error) {
		super();
		this.error = error;
	}

	public ValidationException(String message, Throwable cause, int error) {
		super(message);
		this.detail = cause;
		this.error = error;
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, int error) {
		super(message);
		this.error = error;
	}

	public ValidationException(String message, int error,
			SbnErrorTypes errorCode) {
		super(message);
		this.error = error;
		setErrorCode(errorCode);

	}

	public ValidationException(String message, int error, String msg,
			String bid, String isbd) {
		super(message);
		this.error = error;
		this.msg = msg;
		this.bid = bid;
		this.isbd = isbd;

		//almaviva5_20140624 #4631
		labels = new String[] { bid, isbd };
	}

	public ValidationException(String message, int error, String msg,
			String bid, String isbd, String precInv) {
		super(message);
		this.error = error;
		this.msg = msg;
		this.bid = bid;
		this.isbd = isbd;
		this.precInv = precInv;

		//almaviva5_20140624 #4631
		labels = new String[] { bid, isbd, precInv };
	}

	public ValidationException(String message, int error, String msg,
			List<?> lista) {
		super(message);
		this.error = error;
		this.msg = msg;
		this.lista = lista;
	}

	public ValidationException(String message, int error, String msg) {
		super(message);
		this.error = error;
		this.msg = msg;
	}

	public ValidationException(Throwable cause, int error) {
		super();
		this.detail = cause;
		this.error = error;
	}

	public ValidationException(Exception e) {
        super(e.getMessage());
        if(e != null)
  		  this.detail = e.getCause();
	}

	public ValidationException(ValidationException[] exceptions) {
		super();
		for (ValidationException ve : exceptions)
			super.addException(ve);
	}

	public int getError() {
		return this.error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public static int getDiagnostico() {
		return diagnostico;
	}

	public static int getErrore() {
		return errore;
	}

	public static int getErroreClassificazione() {
		return erroreClassificazione;
	}

	public static int getErroreDescrittore() {
		return erroreDescrittore;
	}

	public static int getErroreDiagnostico() {
		return erroreDiagnostico;
	}

	public static int getErroreNoDigitazione() {
		return erroreNoDigitazione;
	}

	public static int getErroreSoggetto() {
		return erroreSoggetto;
	}

	public static int getErroreValidazione() {
		return erroreValidazione;
	}

	public static int getSuccesso() {
		return successo;
	}

	public static int getValidazione() {
		return validazione;
	}

	public List<?> getLista() {
		return lista;
	}

	public void setLista(List<?> lista) {
		this.lista = lista;
	}

	public String getBid() {
		return bid;
	}

	public String getIsbd() {
		return isbd;
	}

	public String getPrecInv() {
		return precInv;
	}

	public void setPrecInv(String precInv) {
		this.precInv = precInv;
	}

}
