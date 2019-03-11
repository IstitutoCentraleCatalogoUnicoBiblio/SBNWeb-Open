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
package it.iccu.sbn.web.actionforms.statistiche;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class DettaglioVariabiliStatisticheForm extends ActionForm {



	private static final long serialVersionUID = 6462268360709223536L;
	private String abilitaScrittura = "";
	private boolean disable = false;
	private StatisticaVO statistica;
	private List listaCodiciVO = new ArrayList();
	private List ListaBiblioVO;
	private String tipoCombo;
	private String elencoBiblio;
	private boolean  tastoCancBib;
	private List<BibliotecaVO> listaBib;
	private String errore;
	private String obbligatorio;




//
	public String getAbilitaScrittura() {
		return abilitaScrittura;
	}

	public void setAbilitaScrittura(String abilitaScrittura) {
		this.abilitaScrittura = abilitaScrittura;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public StatisticaVO getStatistica() {
		return statistica;
	}

	public void setStatistica(StatisticaVO statistica) {
		this.statistica = statistica;
	}

	public List getListaCodiciVO() {
		return listaCodiciVO;
	}

	public void setListaCodiciVO(List listaCodiciVO) {
		this.listaCodiciVO = listaCodiciVO;
	}

	public String getTipoCombo() {
		return tipoCombo;
	}

	public void setTipoCombo(String tipoCombo) {
		this.tipoCombo = tipoCombo;
	}

	public List getListaBiblioVO() {
		return ListaBiblioVO;
	}

	public void setListaBiblioVO(List listaBiblioVO) {
		ListaBiblioVO = listaBiblioVO;
	}

	public String getElencoBiblio() {
		return elencoBiblio;
	}

	public void setElencoBiblio(String elencoBiblio) {
		this.elencoBiblio = elencoBiblio;
	}

	public boolean isTastoCancBib() {
		return tastoCancBib;
	}

	public void setTastoCancBib(boolean tastoCancBib) {
		this.tastoCancBib = tastoCancBib;
	}

	public List<BibliotecaVO> getListaBib() {
		return listaBib;
	}

	public void setListaBib(List<BibliotecaVO> listaBib) {
		this.listaBib = listaBib;
	}

	public String getErrore() {
		return errore;
	}

	public void setErrore(String errore) {
		this.errore = errore;
	}

	public String getObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}


}
