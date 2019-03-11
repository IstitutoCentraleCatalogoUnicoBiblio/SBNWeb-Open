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

import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieEsemplareCollVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;

import java.util.Comparator;

public class ElementoSeriePeriodicoVO extends SerieBaseVO {

	private static final long serialVersionUID = 16870743003725718L;

	public static final Comparator<ElementoSeriePeriodicoVO> ORDINAMENTO_SERIE_PERIODICO = new Comparator<ElementoSeriePeriodicoVO>() {

		public int compare(ElementoSeriePeriodicoVO o1, ElementoSeriePeriodicoVO o2) {
			return o1.compareTo(o2);
		}
	};

	private SeriePeriodicoType tipo;
	private String bid;
	private String fid;
	private SerieEsemplareCollVO esemplare;
	private SerieCollocazioneVO collocazione;
	private SerieOrdineVO ordine;

	public SeriePeriodicoType getTipo() {
		return tipo;
	}

	public void setTipo(SeriePeriodicoType tipo) {
		this.tipo = tipo;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public SerieEsemplareCollVO getEsemplare() {
		return esemplare;
	}

	public void setEsemplare(SerieEsemplareCollVO esemplare) {
		this.esemplare = esemplare;
	}

	public SerieCollocazioneVO getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(SerieCollocazioneVO collocazione) {
		this.collocazione = collocazione;
	}

	public SerieOrdineVO getOrdine() {
		return ordine;
	}

	public void setOrdine(SerieOrdineVO ordine) {
		this.ordine = ordine;
	}

	public int compareTo(ElementoSeriePeriodicoVO o) {
		int cmp = tipo.compareTo(o.tipo);
		//i comparatori sono invertiti di default
		cmp = (cmp == 0 && esemplare != null && o.esemplare != null) ? -esemplare.compareTo(o.esemplare) : cmp;
		cmp = (cmp == 0 && collocazione != null && o.collocazione != null) ? -collocazione.compareTo(o.collocazione) : cmp;
		cmp = (cmp == 0 && ordine != null && o.ordine != null) ? -ordine.compareTo(o.ordine) : cmp;

		cmp = (cmp == 0) ? super.compareTo(o) : cmp;

		return cmp;
	}

	public SerieBaseVO getActualType() {
		switch (tipo) {
		case ESEMPLARE:
			return esemplare;
		case COLLOCAZIONE:
			return collocazione;
		case ORDINE:
			return ordine;
		}

		return null;
	}

}
