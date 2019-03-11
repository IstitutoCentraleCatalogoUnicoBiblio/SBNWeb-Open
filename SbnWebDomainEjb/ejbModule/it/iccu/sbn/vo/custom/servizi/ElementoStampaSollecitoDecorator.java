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
package it.iccu.sbn.vo.custom.servizi;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;

public class ElementoStampaSollecitoDecorator extends ElementoStampaSollecitoVO {

	private static final long serialVersionUID = 5178413531386741714L;

	private final String descrizioneProvincia;
	private final String descrizioneNazione;

	public ElementoStampaSollecitoDecorator(ElementoStampaSollecitoVO ess) throws Exception {
		super(ess);
		descrizioneProvincia = CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_PROVINCE, this.provincia);
		descrizioneNazione = CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_PAESE, this.nazione);
	}

	public String getDescrizioneProvincia() {
		return descrizioneProvincia;
	}

	public String getDescrizioneNazione() {
		return descrizioneNazione;
	}

	public String getCittaNazione() {
		StringBuilder buf = new StringBuilder(255);
		if (isFilled(cap))
			buf.append(cap).append(' ');
		buf.append(citta);
		if (isFilled(provincia))
			buf.append(" (").append(provincia).append(')');
		if (isFilled(nazione))
			buf.append(", ").append(descrizioneNazione);

		return buf.toString();
	}

}
