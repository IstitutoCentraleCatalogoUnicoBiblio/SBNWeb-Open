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

import it.iccu.sbn.ejb.vo.amministrazionesistema.ProfilazioneTreeElementView;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ProfilazioneForm extends ActionForm {


	private static final long serialVersionUID = 6788787491992009123L;
	private String[] checkItemSelez = null;
	private ProfilazioneTreeElementView profilazioneTreeElementView  = new ProfilazioneTreeElementView();
	private List<GruppoParametriVO> elencoParAuth = new ArrayList<GruppoParametriVO>();
	private List<GruppoParametriVO> elencoParSem = new ArrayList<GruppoParametriVO>();
	private List<GruppoParametriVO> elencoParMateriali = new ArrayList<GruppoParametriVO>();
	private boolean flagAuth;
	private boolean flagMat;
	private String idUtente;
	private String utente;
	private String bibli;
	private String usernam;
	private String provenienza;
	private String abilitatoWrite;

	private String modelloOp;
	private String modello;
	private List<ComboVO> elencoModelli = new ArrayList<ComboVO>();
	private String nuovoModello;

	//almaviva5_20111207 #4722
	private String codBiblioteca;
	private String[] selezioniCheck;

	public String getNuovoModello() {
		return nuovoModello;
	}

	public void setNuovoModello(String nuovoModello) {
		this.nuovoModello = nuovoModello;
	}

	public List<ComboVO> getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ComboVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public boolean isFlagAuth() {
		return flagAuth;
	}

	public void setFlagAuth(boolean flagAuth) {
		this.flagAuth = flagAuth;
	}

	public boolean isFlagMat() {
		return flagMat;
	}

	public void setFlagMat(boolean flagMat) {
		this.flagMat = flagMat;
	}

	public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
/*
        if(this.userName != null && this.password != null)
        {
            if (getUserName().length() <= 0) {
                errors.add("User Name", new ActionMessage(
                        "campo.obbligatorio", "Utente"));
            }
            if (getPassword().length() <= 0) {
                errors.add("password", new ActionMessage(
                        "campo.obbligatorio", "Password"));
            }
        }
*/
        return errors;
    }

	public String[] getCheckItemSelez() {
		return checkItemSelez;
	}

	public void setCheckItemSelez(String[] checkItemSelez) {
		this.checkItemSelez = checkItemSelez;
	}

	public ProfilazioneTreeElementView getProfilazioneTreeElementView() {
		return profilazioneTreeElementView;
	}

	public void setProfilazioneTreeElementView(
			ProfilazioneTreeElementView profilazioneTreeElementView) {
		this.profilazioneTreeElementView = profilazioneTreeElementView;
	}

	public List<GruppoParametriVO> getElencoParAuth() {
		return elencoParAuth;
	}

	public void setElencoParAuth(List<GruppoParametriVO> elencoParAuth) {
		this.elencoParAuth = elencoParAuth;
	}

	public List<GruppoParametriVO> getElencoParSem() {
		return elencoParSem;
	}

	public void setElencoParSem(List<GruppoParametriVO> ElencoParSem) {
		this.elencoParSem = ElencoParSem;
	}

	public List<GruppoParametriVO> getElencoParMateriali() {
		return elencoParMateriali;
	}

	public void setElencoParMateriali(List<GruppoParametriVO> ElencoParMateriali) {
		this.elencoParMateriali = ElencoParMateriali;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		if (request.getParameter("checkItemSelez") == null && request.getMethod().toString().equals("POST"))
			checkItemSelez = null;
		super.reset(mapping, request);
	}

	public String getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(String idUtente) {
		this.idUtente = idUtente;
	}

	public String getBibli() {
		return bibli;
	}

	public void setBibli(String bibli) {
		this.bibli = bibli;
	}

	public String getUsernam() {
		return usernam;
	}

	public void setUsernam(String usernam) {
		this.usernam = usernam;
	}

	public String getAbilitatoWrite() {
		return abilitatoWrite;
	}

	public void setAbilitatoWrite(String abilitatoWrite) {
		this.abilitatoWrite = abilitatoWrite;
	}

	public String getModelloOp() {
		return modelloOp;
	}

	public void setModelloOp(String modelloOp) {
		this.modelloOp = modelloOp;
	}

	public String getCodBiblioteca() {
		return codBiblioteca;
	}

	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}

	public String[] getSelezioniCheck() {
		return selezioniCheck;
	}

	public void setSelezioniCheck(String[] selezioniCheck) {
		this.selezioniCheck = selezioniCheck;
	}

}
