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

import it.iccu.sbn.ejb.vo.SerializableVO;

public class CodiciNormalizzatiVO extends SerializableVO {

	private static final long serialVersionUID = -4573600813612380161L;

	private String codSez;
	private String daColl;
	private String aColl;
	private String daSpec;
	private String aSpec;
	private boolean esattoColl;
	private boolean esattoSpec;

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getDaColl() {
		return daColl;
	}

	public void setDaColl(String daColl) {
		this.daColl = daColl;
	}

	public String getAColl() {
		return aColl;
	}

	public void setAColl(String coll) {
		aColl = coll;
	}

	public String getDaSpec() {
		return daSpec;
	}

	public void setDaSpec(String daSpec) {
		this.daSpec = daSpec;
	}

	public String getASpec() {
		return aSpec;
	}

	public void setASpec(String spec) {
		aSpec = spec;
	}

	public boolean isEsattoColl() {
		return esattoColl;
	}

	public void setEsattoColl(boolean esattoColl) {
		this.esattoColl = esattoColl;
	}

	public boolean isEsattoSpec() {
		return esattoSpec;
	}

	public void setEsattoSpec(boolean esattoSpec) {
		this.esattoSpec = esattoSpec;
	}

	@Override
	public String toString() {
		return "[daColl: '" + daColl +
				"', aColl: '" + aColl +
				"', esattoColl: " + esattoColl +
				", daSpec: '" + daSpec +
				"', allaSpec: '" + aSpec +
				"', esattoSpec: " + esattoSpec +
				"]";
	}



}
