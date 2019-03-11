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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.LinkableViewVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;

import java.util.List;

public class EsameSeriePeriodicoVO extends LinkableViewVO {

	private static final long serialVersionUID = -6250219123559364783L;
	private String bid;
	private String numero_std;

	protected String cd_per;
	protected String isbd;

	private List<ElementoSeriePeriodicoVO> serie = ValidazioneDati.emptyList();

	private static final SerieBaseVO search(List<ElementoSeriePeriodicoVO> list, int id) {
		if (isFilled(list) )
			for (ElementoSeriePeriodicoVO esp : list) {
				SerieBaseVO found = esp.search(id);
				if (found != null)
					return found;
			}

		return null;
	}

	public SerieBaseVO search(int uniqueId) {
		int id = Integer.valueOf(uniqueId);
		return search(serie, id);
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getNumero_std() {
		return numero_std;
	}

	public void setNumero_std(String numero_std) {
		this.numero_std = numero_std;
	}

	public List<ElementoSeriePeriodicoVO> getSerie() {
		return serie;
	}

	public void setSerie(List<ElementoSeriePeriodicoVO> listaConSerie) {
		this.serie = listaConSerie;
	}

	public String getCd_per() {
		return cd_per;
	}

	public void setCd_per(String tipoPeriodicita) {
		this.cd_per = trimAndSet(tipoPeriodicita);
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = trimAndSet(isbd);
	}

	@Override
	public boolean isEmpty() {
		return !isFilled(serie);
	}

}
