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
package it.iccu.sbn.web.actionforms.amministrazionesistema.gestioneDefault;

import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.GruppoVO;
import it.iccu.sbn.web.constant.ConstantDefault.EditorType;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class GestioneDefaultForm extends ActionForm {


	private static final long serialVersionUID = 8543128323035967399L;

	private List<GruppoVO> campi = new ArrayList<GruppoVO>();

	private String idarea;
	private String utilizzo;

	//almaviva5_20140701 #3198
	private EditorType editor;

	public String getUtilizzo() {
		return utilizzo;
	}

	public void setUtilizzo(String utilizzo) {
		this.utilizzo = utilizzo;
	}

	public String getIdarea() {
		return idarea;
	}

	public void setIdarea(String idarea) {
		this.idarea = idarea;
	}

	public List<GruppoVO> getCampi() {
		return campi;
	}

	public void setCampi(List<GruppoVO> campi) {
		this.campi = campi;
	}

	public void setEditor(EditorType editor) {
		this.editor = editor;
	}

	public EditorType getEditor() {
		return editor;
	}

}
