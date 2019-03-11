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
package it.iccu.sbn.web.actions.servizi.segnature;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;

public abstract class SegnatureAction extends ServiziBaseAction {

	protected void normalizzaSegnatura(RangeSegnatureVO segnatura) {

		String segn_da = ValidazioneDati.trimOrEmpty(OrdinamentoCollocazione2.normalizza(segnatura.getSegnInizio()));
		segn_da = ValidazioneDati.fillRight(segn_da, ' ', RangeSegnatureVO.SEGNATURA_LEN);
		segnatura.setSegnDa(segn_da);

		String segn_a = ValidazioneDati.trimOrEmpty(OrdinamentoCollocazione2.normalizza(segnatura.getSegnFine()));
		segn_a = ValidazioneDati.fillRight(segn_a, 'Z', RangeSegnatureVO.SEGNATURA_LEN);
		segnatura.setSegnA(segn_a);
	}

}
