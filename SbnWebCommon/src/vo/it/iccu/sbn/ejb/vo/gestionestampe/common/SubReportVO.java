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
package it.iccu.sbn.ejb.vo.gestionestampe.common;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class SubReportVO extends SerializableVO {

	private static final long serialVersionUID = -3634684867096792059L;

	private final String id;
	private final String type;
	private final long offset;
	private final String idBatch;

	public SubReportVO(String idBatch, String id, String type, long offset) {
		this.idBatch = idBatch;
		this.id = id;
		this.type = type;
		this.offset = offset;

	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public long getOffset() {
		return offset;
	}

	public String getIdBatch() {
		return idBatch;
	}

}
