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
package it.iccu.sbn.web.actionforms.amministrazionesistema.polo;

import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ProfilazioneSemanticaForm extends ActionForm {


	private static final long serialVersionUID = -200482159005792249L;
	private List<GruppoParametriVO> elencoParSemantica = new ArrayList<GruppoParametriVO>();
	private String tipologia;
	private String[] checkedAtt;
	private List<GruppoParametriVO> backupParSemantica = new ArrayList<GruppoParametriVO>();
	private boolean flagParMateriali;
	private String nuovo;
	private List<ComboVO> elencoScelteNuovo = new ArrayList<ComboVO>();
	private String selezioneNuovo;
	private String temp;
	private boolean checkTemp;

	public boolean isCheckTemp() {
		return checkTemp;
	}
	public void setCheckTemp(boolean checkTemp) {
		this.checkTemp = checkTemp;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getNuovo() {
		return nuovo;
	}
	public void setNuovo(String nuovo) {
		this.nuovo = nuovo;
	}
	public List<ComboVO> getElencoScelteNuovo() {
		return elencoScelteNuovo;
	}
	public void setElencoScelteNuovo(List<ComboVO> elencoScelteNuovo) {
		this.elencoScelteNuovo = elencoScelteNuovo;
	}
	public String getSelezioneNuovo() {
		return selezioneNuovo;
	}
	public void setSelezioneNuovo(String selezioneNuovo) {
		this.selezioneNuovo = selezioneNuovo;
	}
	public boolean isFlagParMateriali() {
		return flagParMateriali;
	}
	public void setFlagParMateriali(boolean flagParMateriali) {
		this.flagParMateriali = flagParMateriali;
	}
	public List<GruppoParametriVO> getElencoParSemantica() {
		return elencoParSemantica;
	}
	public void setElencoParSemantica(
			List<GruppoParametriVO> elencoParSemantica) {
		this.elencoParSemantica = elencoParSemantica;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public List<GruppoParametriVO> getBackupParSemantica() {
		return backupParSemantica;
	}
	public void setBackupParSemantica(
			List<GruppoParametriVO> backupParSemantica) {
		this.backupParSemantica = backupParSemantica;
	}
	public String[] getCheckedAtt() {
		return checkedAtt;
	}
	public void setCheckedAtt(String[] checkedAtt) {
		this.checkedAtt = checkedAtt;
	}
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		for (int i= 0; i<this.elencoParSemantica.size(); i++) {
			GruppoParametriVO gruppo = this.elencoParSemantica.get(i);
			if (gruppo.isSelezioneCheck())
				gruppo.setSelezioneCheck(false);
		}
		super.reset(mapping, request);
	}

}
