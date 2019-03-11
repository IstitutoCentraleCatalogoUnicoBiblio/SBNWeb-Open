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
package it.iccu.sbn.web.actionforms.acquisizioni.fornitori;

import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ProfiliAcqForm extends ActionForm {

	private static final long serialVersionUID = 59491314991901101L;
	private List<StrutturaProfiloVO> listaProfili;
	private String[] selectedProfili;
	private boolean sessione;
	private boolean risultatiPresenti=true;
	private int numProfili;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		   this.selectedProfili=new String[0];

	}


	public List<StrutturaProfiloVO> getListaProfili() {
		return listaProfili;
	}


	public void setListaProfili(List<StrutturaProfiloVO> listaProfili) {
		this.listaProfili = listaProfili;
	}




	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}


	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}




	public boolean isSessione() {
		return sessione;
	}


	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}


	public int getNumProfili() {
		return numProfili;
	}


	public void setNumProfili(int numProfili) {
		this.numProfili = numProfili;
	}

	public String[] getSelectedProfili() {
		return selectedProfili;
	}

	public void setSelectedProfili(String[] selectedProfili) {
		this.selectedProfili = selectedProfili;
	}





}
