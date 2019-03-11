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
package it.iccu.sbn.web.actionforms.amministrazionesistema.codici;

import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;

public class GestioneCodiceForm extends ActionForm {


	private static final long serialVersionUID = -5632749999762612784L;
	private boolean initialized = false;
	private CodiceConfigVO configCodici;
	private CodiceVO dettaglio;
	private Set<String> codiciKeySet = new HashSet<String>();

	private List<ComboCodDescVO> listaCodiciP;
	private List<ComboCodDescVO> listaCodiciC;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public CodiceConfigVO getConfigCodici() {
		return configCodici;
	}

	public void setConfigCodici(CodiceConfigVO configCodici) {
		this.configCodici = configCodici;
	}

	public CodiceVO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(CodiceVO dettaglio) {
		this.dettaglio = dettaglio;
	}

	public int getDsUlterioreRowCount() {
		return (int)Math.ceil(configCodici.getDs_ulteriore_length() / 85);

	}

	public Set<String> getCodiciKeySet() {
		return codiciKeySet;
	}

	public void setCodiciKeySet(Set<String> codiciKeySet) {
		this.codiciKeySet = codiciKeySet;
	}

	public List<ComboCodDescVO> getListaCodiciC() {
		return listaCodiciC;
	}

	public void setListaCodiciC(List<ComboCodDescVO> listaCodiciC) {
		this.listaCodiciC = listaCodiciC;
	}

	public List<ComboCodDescVO> getListaCodiciP() {
		return listaCodiciP;
	}

	public void setListaCodiciP(List<ComboCodDescVO> listaCodiciP) {
		this.listaCodiciP = listaCodiciP;
	}
}
