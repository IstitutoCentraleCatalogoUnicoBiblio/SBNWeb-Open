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
package it.finsiel.sbn.polo.orm.viste;

import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_classe;

public class Vl_classe_the extends Tb_classe {

	private static final long serialVersionUID = 8293289443133995641L;
	private String CD_THE;
	private String DID;
	private short POSIZIONE;

	public String getCD_THE() {
		return CD_THE;
	}

	public void setCD_THE(String cD_THE) {
		CD_THE = cD_THE;
		this.settaParametro(KeyParameter.XXXcd_the, cD_THE);
	}

	public String getDID() {
		return DID;
	}

	public void setDID(String dID) {
		DID = dID;
		this.settaParametro(KeyParameter.XXXdid, dID);
	}

	public short getPOSIZIONE() {
		return POSIZIONE;
	}

	public void setPOSIZIONE(short posiz) {
		POSIZIONE = posiz;
		this.settaParametro(KeyParameter.XXXposizione, posiz);
	}

}
