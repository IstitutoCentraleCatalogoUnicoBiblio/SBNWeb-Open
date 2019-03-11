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
package it.iccu.sbn.ejb.vo.documentofisico;

public class InventarioOrdineVO extends InventarioVO {

	private static final long serialVersionUID = -1066228219192112762L;

	private String titNatura;
	private String titIsbd;
	private String descrFornitore;
	private String dataFattura;

	public InventarioOrdineVO() {
		super();
	}

	public InventarioOrdineVO(InventarioVO inv) throws Exception {
		super(inv);
		this.titNatura = "";
		this.titIsbd = "";
		this.descrFornitore = "";
		this.dataFattura = "";
	}

	public InventarioOrdineVO(InventarioVO inv, String titNatura,
			String titIsbd, String dataFattura, String descrFornitore)
			throws Exception {
		super(inv);
		this.titNatura = titNatura;
		this.titIsbd = titIsbd;
		this.dataFattura = dataFattura;
		this.descrFornitore = descrFornitore;
	}

	public String getTitIsbd() {
		return titIsbd;
	}

	public void setTitIsbd(String titIsbd) {
		this.titIsbd = titIsbd;
	}

	public String getTitNatura() {
		return titNatura;
	}

	public void setTitNatura(String titNatura) {
		this.titNatura = titNatura;
	}

	public String getDataFattura() {
		return dataFattura;
	}

	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}

	public String getDescrFornitore() {
		return descrFornitore;
	}

	public void setDescrFornitore(String descrFornitore) {
		this.descrFornitore = descrFornitore;
	}
}
