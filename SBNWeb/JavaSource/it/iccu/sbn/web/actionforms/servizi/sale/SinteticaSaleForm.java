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
package it.iccu.sbn.web.actionforms.servizi.sale;

import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.web.integration.actionforms.servizi.sale.RicercaSaleForm;

import java.util.ArrayList;
import java.util.List;

public class SinteticaSaleForm extends RicercaSaleForm {


	private static final long serialVersionUID = 189034191593459160L;
	private List<SalaVO> sale = new ArrayList<SalaVO>();
	private Integer[] selectedItems;
	private Integer selectedSala;

	public List<SalaVO> getSale() {
		return sale;
	}

	public void setSale(List<SalaVO> sale) {
		this.sale = sale;
	}

	public Integer[] getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Integer[] selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Integer getSelectedSala() {
		return selectedSala;
	}

	public void setSelectedSala(Integer selectedSala) {
		this.selectedSala = selectedSala;
	}

}
