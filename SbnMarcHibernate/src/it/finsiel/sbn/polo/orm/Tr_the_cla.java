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
package it.finsiel.sbn.polo.orm;

public class Tr_the_cla extends OggettoServerSbnMarc {

	private static final long serialVersionUID = 8293289443133995641L;

	private String CD_SISTEMA;
	private String CD_EDIZIONE;
	private String CLASSE;
	private String CD_THE;
	private String DID;
	private short POSIZIONE;
	private String NOTA_THE_CLA;
    private String CL_KEY;

	public String getCL_KEY() {
		return CL_KEY;
	}

	public void setCL_KEY(String cL_KEY) {
		CL_KEY = cL_KEY;
	}

	public String getNOTA_THE_CLA() {
		return NOTA_THE_CLA;
	}

	public void setNOTA_THE_CLA(String nOTA_THE_CLA) {
		this.settaParametro(KeyParameter.XXXnota_the_cla, nOTA_THE_CLA);
		NOTA_THE_CLA = nOTA_THE_CLA;
	}

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

	public void setCD_SISTEMA(String value) {
		this.CD_SISTEMA = value;
		this.settaParametro(KeyParameter.XXXcd_sistema, value);
	}

	public String getCD_SISTEMA() {
		return CD_SISTEMA;
	}

	public void setCD_EDIZIONE(String value) {
		this.CD_EDIZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_edizione, value);
	}

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public void setCLASSE(String value) {
		this.CLASSE = value;
		this.settaParametro(KeyParameter.XXXclasse, value);
	}

	public String getCLASSE() {
		return CLASSE;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((CD_EDIZIONE == null) ? 0 : CD_EDIZIONE.hashCode());
		result = prime * result
				+ ((CD_SISTEMA == null) ? 0 : CD_SISTEMA.hashCode());
		result = prime * result + ((CD_THE == null) ? 0 : CD_THE.hashCode());
		result = prime * result + ((CLASSE == null) ? 0 : CLASSE.hashCode());
		result = prime * result + ((DID == null) ? 0 : DID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tr_the_cla other = (Tr_the_cla) obj;
		if (CD_EDIZIONE == null) {
			if (other.CD_EDIZIONE != null)
				return false;
		} else if (!CD_EDIZIONE.equals(other.CD_EDIZIONE))
			return false;
		if (CD_SISTEMA == null) {
			if (other.CD_SISTEMA != null)
				return false;
		} else if (!CD_SISTEMA.equals(other.CD_SISTEMA))
			return false;
		if (CD_THE == null) {
			if (other.CD_THE != null)
				return false;
		} else if (!CD_THE.equals(other.CD_THE))
			return false;
		if (CLASSE == null) {
			if (other.CLASSE != null)
				return false;
		} else if (!CLASSE.equals(other.CLASSE))
			return false;
		if (DID == null) {
			if (other.DID != null)
				return false;
		} else if (!DID.equals(other.DID))
			return false;
		return true;
	}

	@Override
	public String getUniqueId() {
		StringBuilder id = new StringBuilder();
		id.append(CD_THE).append(DID).append(CD_SISTEMA).append(CD_EDIZIONE).append(CLASSE);
		return id.toString();
	}



}
