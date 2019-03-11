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
package it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca;

import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class ProfilazioneMaterialiForm extends ActionForm {


	private static final long serialVersionUID = 7911534641502280717L;
	private List<GruppoParametriVO> elencoParMat = new ArrayList<GruppoParametriVO>();
	private List<GruppoParametriVO> backupParMat = new ArrayList<GruppoParametriVO>();
	private String[] checked;
	private boolean flagParAuth;
	private String nomeBiblio;

	private String abilitatoWrite;

	public String getAbilitatoWrite() {
		return abilitatoWrite;
	}

	public void setAbilitatoWrite(String abilitatoWrite) {
		this.abilitatoWrite = abilitatoWrite;
	}

	public boolean isFlagParAuth() {
		return flagParAuth;
	}

	public void setFlagParAuth(boolean flagParAuth) {
		this.flagParAuth = flagParAuth;
	}

	public String[] getChecked() {
		return checked;
	}

	public void setChecked(String[] checked) {
		this.checked = checked;
	}

	public List<GruppoParametriVO> getElencoParMat() {
		return elencoParMat;
	}

	public void setElencoParMat(List<GruppoParametriVO> elencoParMat) {
		this.elencoParMat = elencoParMat;
	}

	public List<GruppoParametriVO> getBackupParMat() {
		return backupParMat;
	}

	public void setBackupParMat(List<GruppoParametriVO> backupParMat) {
		this.backupParMat = backupParMat;
	}

	public String getNomeBiblio() {
		return nomeBiblio;
	}

	public void setNomeBiblio(String nomeBiblio) {
		this.nomeBiblio = nomeBiblio;
	}

}
