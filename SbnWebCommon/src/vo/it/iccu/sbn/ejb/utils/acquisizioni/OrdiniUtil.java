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
package it.iccu.sbn.ejb.utils.acquisizioni;

import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;

import java.util.Date;

public class OrdiniUtil {

	private static final String DEFAULT_FILE_EXTENSION = "txt";

	public static final OrdineCarrelloSpedizioneVO preparaOrdineCarrelloSpedizione(
			OrdiniVO ordine, String firmaUtente) {
		OrdineCarrelloSpedizioneVO ocs = new OrdineCarrelloSpedizioneVO();
		ocs.setIdOrdine(ordine.getIDOrd());
		ocs.setDataSpedizione(new Date());
		ocs.setPrgSpedizione((short) 1);
		ocs.setUteIns(firmaUtente);
		ocs.setUteVar(firmaUtente);
		ocs.setFlCanc("N");

		return ocs;
	}

	public static final String formattaCartName(String cd_bib_google, Integer next) {
		if (next == null)
			return cd_bib_google + "...";

		return String.format("%s%03d", cd_bib_google, next);
	}

	public static final String formattaShippingManifest(String cd_bib_google, Date dt_spedizione, short prg_spedizione) {
		return String.format("%s_%tY%tm%td_%02d.%s", cd_bib_google,
				dt_spedizione, dt_spedizione, dt_spedizione, prg_spedizione, DEFAULT_FILE_EXTENSION);
	}

}
