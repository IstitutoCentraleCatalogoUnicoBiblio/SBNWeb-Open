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
package it.iccu.sbn.web.actionforms.servizi.calendario;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.web.integration.actionforms.servizi.ServiziBaseForm;

import java.util.ArrayList;
import java.util.List;

public class CalendarioForm extends ServiziBaseForm {


	private static final long serialVersionUID = 7628722839454851690L;
	private ModelloCalendarioVO modello = new ModelloCalendarioVO();
	private CalendarioVO calendario = new CalendarioVO();
	private Integer selected;

	private List<TB_CODICI> listaCategorieMediazione = new ArrayList<TB_CODICI>();

	public ModelloCalendarioVO getModello() {
		return modello;
	}

	public void setModello(ModelloCalendarioVO modello) {
		this.modello = modello;
	}

	public CalendarioVO getCalendario() {
		return calendario;
	}

	public void setCalendario(CalendarioVO calendario) {
		this.calendario = calendario;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	public List<TB_CODICI> getListaCategorieMediazione() {
		return listaCategorieMediazione;
	}

	public void setListaCategorieMediazione(List<TB_CODICI> listaCategorieMediazione) {
		this.listaCategorieMediazione = listaCategorieMediazione;
	}

}
