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
package it.iccu.sbn.ejb.vo.periodici;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;

public class EsameSeriePeriodicoDecorator extends EsameSeriePeriodicoVO {

	private static final long serialVersionUID = 8000687407942637372L;
	private static final String BR = "<br />";

	private final String titolo;
	private final String descrizioneLegami;
	private String tipoPeriodicita;
	private DescrittoreBloccoVO blocco;

	public EsameSeriePeriodicoDecorator(EsameSeriePeriodicoVO esame) {
		copyCommonProperties(this, esame);

		//almaviva5_20110310 #4250
		int pos = isbd.indexOf(BR);
		if (pos > -1) {
			titolo = isbd.substring(0, pos);
			descrizioneLegami = isbd.substring(pos + BR.length());
		} else {
			titolo = isbd;
			descrizioneLegami = "";
		}

		try {
			tipoPeriodicita = CodiciProvider.cercaDescrizioneCodice(cd_per, CodiciType.CODICE_PERIODICITA, CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			return;
		}

	}

	public String getTipoPeriodicita() {
		return tipoPeriodicita;
	}

	public String getDescrizioneLegami() {
		String findXid = findXid(descrizioneLegami);
		return htmlFilter(findXid);
	}

	public String getTitolo() {
		String findXid = findXid(titolo);
		return htmlFilter(findXid);
	}

	public DescrittoreBloccoVO getBlocco() {
		return blocco;
	}

	public void setBlocco(DescrittoreBloccoVO blocco) {
		this.blocco = blocco;
	}

	@Override
	public boolean isEmpty() {
		return !DescrittoreBloccoVO.isFilled(blocco);
	}

}
