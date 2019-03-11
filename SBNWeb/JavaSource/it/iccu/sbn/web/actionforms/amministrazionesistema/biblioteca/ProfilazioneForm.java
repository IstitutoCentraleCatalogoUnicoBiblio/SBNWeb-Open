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


	private static final long serialVersionUID = -1146004360508329268L;
	private String[] checkItemSelez = null;
	private String[] checkBackup = null;
	private ProfilazioneTreeElementView profilazioneTreeElementView  = new ProfilazioneTreeElementView();
	private List<GruppoParametriVO> elencoParAuth = new ArrayList<GruppoParametriVO>();
	private List<GruppoParametriVO> elencoParSem = new ArrayList<GruppoParametriVO>();
	private List<GruppoParametriVO> elencoParMateriali = new ArrayList<GruppoParametriVO>();
	private boolean flagAuth;
	private boolean flagMat;
	private List<ComboVO> elencoBib = new ArrayList<ComboVO>();
	private String selezioneBib;
	private String nomeBib;
	private String codBib;
	private String recapito;
	private String provenienza;
	private String id;
	private String salvataggio = "FALSE";
	private String conferma = "FALSE";
	private ProfilazioneTreeElementView rootBackup  = new ProfilazioneTreeElementView();
	private String abilitatoWrite;

	private String modelloOp;
	private String modello;
	private List<ComboVO> elencoModelli = new ArrayList<ComboVO>();
	private String nuovoModello;

	//almaviva5_20140211 evolutive google3
	private String[] selezioniCheck;


	public String getAbilitatoWrite() {
		return abilitatoWrite;
	}

	public void setAbilitatoWrite(String abilitatoWrite) {
		this.abilitatoWrite = abilitatoWrite;
	}

	public String getConferma() {
		return conferma;
	}

	public void setConferma(String conferma) {
		this.conferma = conferma;
	}

	public ProfilazioneTreeElementView getRootBackup() {
		return rootBackup;
	}

	public void setRootBackup(ProfilazioneTreeElementView rootBackup) {
		this.rootBackup = rootBackup;
	}

	public String getSalvataggio() {
		return salvataggio;
	}

	public void setSalvataggio(String salvataggio) {
		this.salvataggio = salvataggio;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getNomeBib() {
		return nomeBib;
	}

	public void setNomeBib(String nomeBib) {
		this.nomeBib = nomeBib;
	}

	public List<ComboVO> getElencoBib() {
		return elencoBib;
	}

	public void setElencoBib(List<ComboVO> elencoBib) {
		this.elencoBib = elencoBib;
	}

	public String getSelezioneBib() {
		return selezioneBib;
	}

	public void setSelezioneBib(String selezioneBib) {
		this.selezioneBib = selezioneBib;
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

	public String[] getCheckBackup() {
		return checkBackup;
	}

	public void setCheckBackup(String[] checkBackup) {
		this.checkBackup = checkBackup;
	}

	public String getRecapito() {
		return recapito;
	}

	public void setRecapito(String recapito) {
		this.recapito = recapito;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModelloOp() {
		return modelloOp;
	}

	public void setModelloOp(String modelloOp) {
		this.modelloOp = modelloOp;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public List<ComboVO> getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ComboVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public String getNuovoModello() {
		return nuovoModello;
	}

	public void setNuovoModello(String nuovoModello) {
		this.nuovoModello = nuovoModello;
	}

	public String[] getSelezioniCheck() {
		return selezioniCheck;
	}

	public void setSelezioniCheck(String[] selezioniCheck) {
		this.selezioniCheck = selezioniCheck;
	}
}
