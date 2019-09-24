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
package it.iccu.sbn.web.exception;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.TransactionRolledbackException;

public abstract class SbnBaseException extends TransactionRolledbackException {

	private static final long serialVersionUID = 6534598661536984760L;

	private final String incidentId = RandomIdGenerator.getId();
	private SbnErrorTypes errorCode;

	protected String[] labels;

	private List<SbnBaseException> exceptions = new ArrayList<SbnBaseException>();

	{
		addException(this);
	}

	public SbnBaseException(SbnErrorTypes errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		if (!ValidazioneDati.isFilled(labels) && ValidazioneDati.isFilled(message))
			labels = new String[] { message };
	}

	public SbnBaseException(SbnErrorTypes errorCode, Throwable cause) {
		super();
		this.detail = cause;
		this.errorCode = errorCode;
	}

	public SbnBaseException(SbnErrorTypes errorCode, String message, Throwable cause) {
		super(message);
		this.detail = cause;
		this.errorCode = errorCode;
	}

	public SbnBaseException() {
		this.errorCode = SbnErrorTypes.ERROR_GENERIC;
	}

	public SbnBaseException(String message) {
		super(message);
		this.errorCode = SbnErrorTypes.ERROR_GENERIC;
	}

	public SbnErrorTypes getErrorCode() {
		return errorCode;
	}

	protected void setErrorCode(SbnErrorTypes errorCode) {
		this.errorCode = errorCode;
	}

	public SbnBaseException(SbnErrorTypes errorCode) {
		this.errorCode = errorCode;
	}

	public SbnBaseException(SbnErrorTypes errorCode, String... labels) {
		super(labels[0]);
		this.errorCode = errorCode;
		this.labels = labels;
	}

	@Override
	public String toString() {
		String trace = super.toString();
		if (!ValidazioneDati.isFilled(exceptions))
			return trace;

		String lsep = System.getProperty("line.separator");
		StringBuilder buf = new StringBuilder();
		int idx = 0;
		for (SbnBaseException sbe : exceptions) {
			buf.append(getClass().getName());
			buf.append(String.format("[%04d/%s]", sbe.getErrorCode().getIntCode(), sbe.getErrorCode().name()));
			if (++idx == 1)
				buf.append(" (incidentId: ").append(incidentId).append(" )");

			String message = sbe.getLocalizedMessage();
			if (message != null)
				buf.append(": ").append(message);

			buf.append(lsep);
		}

		buf.append(trace);

		return buf.toString();
	}

	public String[] getLabels() {
		return labels;
	}

	public String getIncidentId() {
		return incidentId;
	}

	public List<SbnBaseException> getExceptions() {
		return exceptions;
	}

	public boolean addException(SbnBaseException sbe) {
		return exceptions.add(sbe);
	}

}
