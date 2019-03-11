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

import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.CheckVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ProfilazioneSemanticaForm extends ActionForm {


	private static final long serialVersionUID = -7346210438695620998L;
	private List<GruppoParametriVO> elencoParSemantica = new ArrayList<GruppoParametriVO>();
	private String tipologia;
	private String[] checkedAtt;
	private List<GruppoParametriVO> backupParSemantica = new ArrayList<GruppoParametriVO>();
	private boolean flagParMateriali;
	private String nuovo;
	private List<ComboVO> elencoScelteNuovo = new ArrayList<ComboVO>();
	private List<CheckVO> elencoEdizioni = new ArrayList<CheckVO>();
	private String selezioneNuovo;
	private String nomeBib;
	private String temp;
	private boolean checkTemp;

	private String abilitatoWrite;

	public String getAbilitatoWrite() {
		return abilitatoWrite;
	}
	public void setAbilitatoWrite(String abilitatoWrite) {
		this.abilitatoWrite = abilitatoWrite;
	}
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
			if (gruppo.getAcceso().equals("TRUE")) {
				for (int z = 0; z <gruppo.getElencoParametri().size(); z++) {
					ParametroVO parametri = gruppo.getElencoParametri().get(z);
					for (int p = 0; p < parametri.getElencoCheck().size(); p++) {
						CheckVO check = parametri.getElencoCheck().get(p);
	//					String test = "elencoParSemantica[" + i + "].elencoParametri[" + z + "].elencoCheck[" + p + "].selezione";
	//					if (check.isSelezione() && request.getParameter(test) == null)
						if (check.isSelezione())
							check.setSelezione(false);
					}
				}
			}
		}
		super.reset(mapping, request);
//		Enumeration names = request.getParameterNames();
//		while (names.hasMoreElements()) {
//			String element = (String) names.nextElement();
//			System.out.println(element + ": " +request.getParameter(element));
//		}
	}
	public String getNomeBib() {
		return nomeBib;
	}
	public void setNomeBib(String nomeBib) {
		this.nomeBib = nomeBib;
	}
	public List<CheckVO> getElencoEdizioni() {
		return elencoEdizioni;
	}
	public void setElencoEdizioni(List<CheckVO> elencoEdizioni) {
		this.elencoEdizioni = elencoEdizioni;
	}

}
