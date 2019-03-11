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

import it.iccu.sbn.ejb.utils.acquisizioni.OrdiniUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;

public class StampaCartRoutingSheetVO extends OrdiniVO {

	private static final long serialVersionUID = 1383987930017672361L;

	private int inventari;
	private int volumi;
	private String cd_bib_google;

	public StampaCartRoutingSheetVO(OrdiniVO ordine) {
		copyCommonProperties(this, ordine);
	}

	public int getInventari() {
		return inventari;
	}

	public void setInventari(int inventari) {
		this.inventari = inventari;
	}

	public int getVolumi() {
		return volumi;
	}

	public void setVolumi(int volumi) {
		this.volumi = volumi;
	}

	public String getName() {
		OrdineCarrelloSpedizioneVO ocs = getOrdineCarrelloSpedizione();
		StringBuilder buf = new StringBuilder(512);
		buf.append(OrdiniUtil.formattaShippingManifest(cd_bib_google, ocs.getDataSpedizione(), ocs.getPrgSpedizione()) );
//		buf.append(" ");
//		buf.append(ocs.getCartName());

		return buf.toString();
	}

	public String getCd_bib_google() {
		return cd_bib_google;
	}

	public void setCd_bib_google(String cd_bib_google) {
		this.cd_bib_google = cd_bib_google;
	}

}
