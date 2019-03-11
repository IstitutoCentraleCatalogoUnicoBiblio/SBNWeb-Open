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
package it.iccu.sbn.vo.domain.pagination;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.servizi.pagination.QueryExecutionLogic;
import it.iccu.sbn.servizi.pagination.QueryExecutionParams;

public class PaginationInternalParamsVO extends SerializableVO {

	private static final long serialVersionUID = 4991253791369227694L;
	private final QueryExecutionLogic logic;
	private final QueryExecutionParams params;
	private final String idLista;
	private final int pageSize;
	private final int pageCount;
	private final int rowCount;

	public PaginationInternalParamsVO(QueryExecutionLogic logic,
			QueryExecutionParams params, String idLista, int pageSize,
			int pageCount, int rowCount) {
		this.logic = logic;
		this.params = params;
		this.idLista = idLista;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.rowCount = rowCount;

	}

	public QueryExecutionLogic getLogic() {
		return logic;
	}

	public QueryExecutionParams getParams() {
		return params;
	}

	public String getIdLista() {
		return idLista;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getRowCount() {
		return rowCount;
	}

}
