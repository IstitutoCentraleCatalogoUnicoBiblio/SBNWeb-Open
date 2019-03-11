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
package it.iccu.sbn.vo.custom.acquisizioni.ordini;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;

public class OrdineCarrelloSpedizioneDecorator extends OrdineCarrelloSpedizioneVO {

	private static final long serialVersionUID = 4054582911383779677L;

	public OrdineCarrelloSpedizioneDecorator(OrdineCarrelloSpedizioneVO ocs) {
		super(ocs);
	}

	public String getData() {
		return (dataSpedizione == null ? null : DateUtil.formattaData(dataSpedizione));
	}

	public void setData(String data) {
		this.dataSpedizione = DateUtil.toDate(trimOrEmpty(data));
	}

	public String getPrg() {
		return isFilled(this.prgSpedizione) ? String.valueOf(this.prgSpedizione) : "";
	}

	public void setPrg(String prog) {
		this.prgSpedizione = isNumeric(prog) ? Short.valueOf(prog) : 0;
	}


}
