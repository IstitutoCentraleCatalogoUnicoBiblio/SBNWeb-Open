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
package it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario;

import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class ProfilazioneAuthoritiesForm extends ActionForm {


	private static final long serialVersionUID = 5486003769868287249L;
	private List<GruppoParametriVO> elencoAuth = new ArrayList<GruppoParametriVO>();
	private List<GruppoParametriVO> elencoSem = new ArrayList<GruppoParametriVO>();
	private List<GruppoParametriVO> elencoMat = new ArrayList<GruppoParametriVO>();
    private List<GruppoParametriVO> backupAuth = new ArrayList<GruppoParametriVO>();
    private List<GruppoParametriVO> backupSem = new ArrayList<GruppoParametriVO>();
    private boolean flagParMat;
	private String[] checked;
	private String codBiblioteca;
	private String nomeUtente;
	private String nomeBiblioteca;
	private String nomeUser;

	private String abilitatoWrite;

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}

	public String getNomeBiblioteca() {
		return nomeBiblioteca;
	}

	public void setNomeBiblioteca(String nomeBiblioteca) {
		this.nomeBiblioteca = nomeBiblioteca;
	}

	public String getNomeUser() {
		return nomeUser;
	}

	public void setNomeUser(String nomeUser) {
		this.nomeUser = nomeUser;
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}

	public String[] getChecked() {
		return checked;
	}

	public void setChecked(String[] checked) {
		this.checked = checked;
	}

	public List<GruppoParametriVO> getElencoAuth() {
		return elencoAuth;
	}

	public void setElencoAuth(List<GruppoParametriVO> elencoAuth) {
		this.elencoAuth = elencoAuth;
	}

	public List<GruppoParametriVO> getElencoSem() {
		return elencoSem;
	}

	public void setElencoSem(List<GruppoParametriVO> elencoSem) {
		this.elencoSem = elencoSem;
	}

	public List<GruppoParametriVO> getBackupAuth() {
		return backupAuth;
	}

	public void setBackupAuth(List<GruppoParametriVO> backupAuth) {
		this.backupAuth = backupAuth;
	}

	public List<GruppoParametriVO> getBackupSem() {
		return backupSem;
	}

	public void setBackupSem(List<GruppoParametriVO> backupSem) {
		this.backupSem = backupSem;
	}

	public List<GruppoParametriVO> getElencoMat() {
		return elencoMat;
	}

	public void setElencoMat(List<GruppoParametriVO> elencoMat) {
		this.elencoMat = elencoMat;
	}

	public boolean isFlagParMat() {
		return flagParMat;
	}

	public void setFlagParMat(boolean flagParMat) {
		this.flagParMat = flagParMat;
	}

	public String getAbilitatoWrite() {
		return abilitatoWrite;
	}

	public void setAbilitatoWrite(String abilitatoWrite) {
		this.abilitatoWrite = abilitatoWrite;
	}

}
