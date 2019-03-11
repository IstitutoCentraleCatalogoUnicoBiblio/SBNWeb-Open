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
package it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa;

import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;

public class OrdineStampaOnlineVO extends StampaOnLineVO {

	private static final long serialVersionUID = -3674014290817952748L;

	private OrdiniVO ordine;
	private String tipoStampaOrdine;
	private String motivoPrelievo;
	private String dataPrelievo;

	public OrdiniVO getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdiniVO ordine) {
		this.ordine = ordine;
	}

	public String getTipoStampaOrdine() {
		return tipoStampaOrdine;
	}

	public void setTipoStampaOrdine(String tipoStampaOrdine) {
		this.tipoStampaOrdine = tipoStampaOrdine;
	}

	public String getMotivoPrelievo() {
		return motivoPrelievo;
	}

	public void setMotivoPrelievo(String motivoPrelievo) {
		this.motivoPrelievo = trimAndSet(motivoPrelievo);
	}

	public String getDataPrelievo() {
		return dataPrelievo;
	}

	public void setDataPrelievo(String dataPrelievo) {
		this.dataPrelievo = trimAndSet(dataPrelievo);
	}

}
