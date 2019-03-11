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
package it.iccu.sbn.web.actions.gestionesemantica.utility;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;

public final class VerificaOggettoPerSoggettazione {

	private static final String NATURE_AMMESSE_PER_SOGGETTAZIONE = "M,S,N,W,R";

	public static final boolean isSoggettabile(TreeElementViewTitoli reticolo) {

		if (reticolo == null)
			return true;

		String natura = reticolo.getNatura();

		// solo queste nature
		if (!isNaturaAmmessa(natura) )
			return false;
/*
 		//almaviva5_20140203
 		String tipoMateriale = reticolo.getTipoMateriale();
		String tipoRecord = reticolo.getAreaDatiDettaglioOggettiVO()
				.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
				.getTipoRec();

		if (ValidazioneDati.isFilled(tipoRecord))
			if (ValidazioneDati.in(tipoRecord, "c", "d"))
				return false;

		// non si soggetta musica
		if (ValidazioneDati.isFilled(tipoMateriale)	&& tipoMateriale.equals("U"))
			return false;
*/
		return true;
	}

	public static final boolean isNaturaAmmessa(String natura) {
		//almaviva5_20130520 #5316 aggiunta natura R
		if (ValidazioneDati.isFilled(natura) && NATURE_AMMESSE_PER_SOGGETTAZIONE.indexOf(natura) < 0)
			return false;
		return true;
	}

}
