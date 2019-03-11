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
package it.iccu.sbn.vo.custom.amministrazione;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ParametriAuthorityVO extends SerializableVO {

	private static final long serialVersionUID = -7945128700342628937L;

	private String tipoAuthority;
	private String abilitaAuthority;
	private String abilitaLegamiDoc;
	private String reticoloLegamiDoc;
	private int livelloAut;
	private String abilitatoForzatura;
	private boolean sololocale;

	public ParametriAuthorityVO(String tipoAuthority, String abilitaAuthority,
			String abilitaLegamiDoc, String reticoloLegamiDoc, int livelloAut,
			String abilitatoForzatura, boolean sololocale) {
		super();
		this.tipoAuthority = tipoAuthority;
		this.abilitaAuthority = abilitaAuthority;
		this.abilitaLegamiDoc = abilitaLegamiDoc;
		this.reticoloLegamiDoc = reticoloLegamiDoc;
		this.livelloAut = livelloAut;
		this.abilitatoForzatura = abilitatoForzatura;
		this.sololocale = sololocale;
	}

	public String getAbilitaAuthority() {
		return abilitaAuthority;
	}

	public void setAbilitaAuthority(String abilitaAuthority) {
		this.abilitaAuthority = abilitaAuthority;
	}

	public String getAbilitaLegamiDoc() {
		return abilitaLegamiDoc;
	}

	public void setAbilitaLegamiDoc(String abilitaLegamiDoc) {
		this.abilitaLegamiDoc = abilitaLegamiDoc;
	}

	public String getAbilitatoForzatura() {
		return abilitatoForzatura;
	}

	public void setAbilitatoForzatura(String abilitatoForzatura) {
		this.abilitatoForzatura = abilitatoForzatura;
	}

	public int getLivelloAut() {
		return livelloAut;
	}

	public void setLivelloAut(int livelloAut) {
		this.livelloAut = livelloAut;
	}

	public String getReticoloLegamiDoc() {
		return reticoloLegamiDoc;
	}

	public void setReticoloLegamiDoc(String reticoloLegamiDoc) {
		this.reticoloLegamiDoc = reticoloLegamiDoc;
	}

	public boolean isSololocale() {
		return sololocale;
	}

	public void setSololocale(boolean sololocale) {
		this.sololocale = sololocale;
	}

	public String getTipoAuthority() {
		return tipoAuthority;
	}

	public void setTipoAuthority(String tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}
}
