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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;

public class ParametriTimestampRangeVO extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = 3210899572003043914L;

	protected String insFrom;
	protected String insTo;
	protected String varFrom;
	protected String varTo;
	protected Timestamp tsIns_da;
	protected Timestamp tsIns_a;
	protected Timestamp tsVar_da;
	protected Timestamp tsVar_a;

	public void validate() throws ValidationException {
		super.validate();

		Timestamp from = null;
		Timestamp to = null;

		// validazione date inserimento
		if (isFilled(insFrom) && ValidazioneDati.validaData(insFrom) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

		if (isFilled(insTo)	&& ValidazioneDati.validaData(insTo) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

		from = DateUtil.toTimestamp(insFrom);
		to = DateUtil.toTimestampA(insTo);

		if (isFilled(insFrom) && isFilled(insTo) )
			if (to.before(from))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

		tsIns_da = from;
		tsIns_a = to;

		from = null;
		to = null;

		// validazione date aggiornamento
		if (isFilled(varFrom) && ValidazioneDati.validaData(varFrom) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

		if (isFilled(varTo)	&& ValidazioneDati.validaData(varTo) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

		from = DateUtil.toTimestamp(varFrom);
		to = DateUtil.toTimestampA(varTo);

		if (isFilled(varFrom) && isFilled(varTo) )
			if (to.before(from))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

		tsVar_da = from != null ? from : tsVar_da;
		tsVar_a = to != null ? to : tsVar_a;
	}

	public String getInsFrom() {
		return insFrom;
	}

	public void setInsFrom(String insFrom) {
		this.insFrom = trimAndSet(insFrom);
	}

	public String getInsTo() {
		return insTo;
	}

	public void setInsTo(String insTo) {
		this.insTo = trimAndSet(insTo);
	}

	public String getVarFrom() {
		return varFrom;
	}

	public void setVarFrom(String varFrom) {
		this.varFrom = trimAndSet(varFrom);
	}

	public String getVarTo() {
		return varTo;
	}

	public void setVarTo(String varTo) {
		this.varTo = trimAndSet(varTo);
	}

	public Timestamp getTsIns_da() {
		return tsIns_da;
	}

	public void setTsIns_da(Timestamp tsInsDa) {
		tsIns_da = tsInsDa;
	}

	public Timestamp getTsIns_a() {
		return tsIns_a;
	}

	public void setTsIns_a(Timestamp tsInsA) {
		tsIns_a = tsInsA;
	}

	public Timestamp getTsVar_da() {
		return tsVar_da;
	}

	public void setTsVar_da(Timestamp tsVarDa) {
		tsVar_da = tsVarDa;
	}

	public Timestamp getTsVar_a() {
		return tsVar_a;
	}

	public void setTsVar_a(Timestamp tsVarA) {
		tsVar_a = tsVarA;
	}

}
