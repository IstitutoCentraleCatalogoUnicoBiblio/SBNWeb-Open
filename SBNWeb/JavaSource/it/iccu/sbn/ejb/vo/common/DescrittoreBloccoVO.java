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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

@SuppressWarnings("unchecked")
public class DescrittoreBloccoVO extends SerializableVO {

	private static final long serialVersionUID = -1927276129742300941L;
	protected String idLista;
	private int numBlocco;
	private int totRighe;
	private int startOffset;
	private int rowCount;
	private int lastRow;
	protected List lista;
	private int totBlocchi;
	private int maxRighe;

	public static final boolean isFilled(DescrittoreBloccoVO db) {
		return db != null && !db.isEmpty();
	}

	public DescrittoreBloccoVO(String idLista, int numBlocco, int totRighe,
			int startOffset, int rowCount, int totBlocchi, int maxRighe,
			List lista) {
		super();
		this.idLista = idLista;
		this.numBlocco = numBlocco;
		this.totRighe = totRighe;
		this.startOffset = startOffset;
		this.rowCount = rowCount;
		this.lista = lista;
		this.lastRow = startOffset + rowCount - 1;
		this.totBlocchi = totBlocchi;
		this.maxRighe = maxRighe;
	}

	public DescrittoreBloccoVO(DescrittoreBloccoVO descrBloccoVO) {
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
	public List getLista() {
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

}
