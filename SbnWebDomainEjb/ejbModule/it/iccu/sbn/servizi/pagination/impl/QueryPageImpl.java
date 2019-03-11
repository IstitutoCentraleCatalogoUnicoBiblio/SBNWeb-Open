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
package it.iccu.sbn.servizi.pagination.impl;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.servizi.pagination.QueryPage;

import java.io.Serializable;
import java.util.List;


public class QueryPageImpl extends SerializableVO implements QueryPage {

	private static final long serialVersionUID = -7374629688986899269L;

	private String idLista;
	private int numBlocco;
	private int totRighe;
	private int startOffset;
	private int rowCount;
	private int lastRow;
	private List<Serializable> lista;
	private int totBlocchi;
	private int maxRighe;

	private final State state;

	public static final boolean isFilled(QueryPageImpl db) {
		return db != null && !db.isEmpty();
	}

	public QueryPageImpl(String idLista, int numBlocco, int totRighe,
			int startOffset, int rowCount, int totBlocchi, int maxRighe,
			List<Serializable> lista, State state) {
		super();
		this.idLista = idLista;
		this.numBlocco = numBlocco;
		this.totRighe = totRighe;
		this.startOffset = startOffset;
		this.rowCount = rowCount;
		this.lista = lista;
		this.state = state;
		this.lastRow = startOffset + rowCount - 1;
		this.totBlocchi = totBlocchi;
		this.maxRighe = maxRighe;
	}

	public QueryPageImpl(QueryPageImpl descrBloccoVO) {
		super();
		this.idLista = descrBloccoVO.idLista;
		this.numBlocco = descrBloccoVO.numBlocco;
		this.totRighe = descrBloccoVO.totRighe;
		this.startOffset = descrBloccoVO.startOffset;
		this.rowCount = descrBloccoVO.rowCount;
		this.lista = descrBloccoVO.lista;
		this.lastRow = descrBloccoVO.startOffset + rowCount - 1;
		this.totBlocchi = descrBloccoVO.totBlocchi;
		this.maxRighe = descrBloccoVO.maxRighe;
		this.state = descrBloccoVO.state;
	}

	/**
	 * @return Returns the idLista.
	 */
	public String getIdLista() {
		return idLista;
	}

	/**
	 * @return Returns the lastRow.
	 */
	public int getLastRow() {
		return lastRow;
	}

	/**
	 * @return Returns the lista.
	 */
	public List<Serializable> getLista() {
		return lista;
	}

	/**
	 * @return Returns the numBlocco.
	 */
	public int getNumBlocco() {
		return numBlocco;
	}

	/**
	 * @return Returns the rowCount.
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @return Returns the startOffset.
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * @return Returns the totRighe.
	 */
	public int getTotRighe() {
		return totRighe;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	@Override
	public boolean isEmpty() {
		return !isFilled(lista);
	}

	public State getState() {
		return state;
	}

}
