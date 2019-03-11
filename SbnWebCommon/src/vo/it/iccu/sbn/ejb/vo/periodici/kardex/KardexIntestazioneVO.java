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
package it.iccu.sbn.ejb.vo.periodici.kardex;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieEsemplareCollVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieTitoloVO;

public class KardexIntestazioneVO extends SerializableVO {

	private static final long serialVersionUID = -2171146047348248545L;
	private SeriePeriodicoType tipo;
	private SerieOrdineVO ordine;
	private SerieCollocazioneVO collocazione;
	private SerieEsemplareCollVO esemplare;
	private SerieFascicoloVO fascicolo;
	private SerieTitoloVO titolo;
	private String from;
	private String to;
	private String descrizionePeriodico;


	public KardexIntestazioneVO(SerieBaseVO serie, String descrizionePeriodico) {
		super();
		this.descrizionePeriodico = descrizionePeriodico;
		this.tipo = SeriePeriodicoType.fromClass(serie);
		switch (tipo) {
		case COLLOCAZIONE:
			this.collocazione = (SerieCollocazioneVO) serie;
			break;
		case ESEMPLARE:
			this.esemplare = (SerieEsemplareCollVO) serie;
			break;
		case FASCICOLO:
			this.fascicolo = (SerieFascicoloVO) serie;
			break;
		case ORDINE:
			this.ordine = (SerieOrdineVO) serie;
			break;
		case TITOLO:
			this.titolo = (SerieTitoloVO) serie;
			break;
		}
	}

	public SeriePeriodicoType getTipo() {
		return tipo;
	}

	public String getDescrizionePeriodico() {
		return descrizionePeriodico;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public SerieCollocazioneVO getCollocazione() {
		return collocazione;
	}

	public SerieEsemplareCollVO getEsemplare() {
		return esemplare;
	}

	public SerieOrdineVO getOrdine() {
		return ordine;
	}

	public SerieBaseVO getSerie() {
		switch(tipo) {
		case ESEMPLARE:
			return getEsemplare();
		case COLLOCAZIONE:
			return getCollocazione();
		case ORDINE:
			return getOrdine();
		case FASCICOLO:
			return getFascicolo();
		}
		return null;
	}

	public SerieFascicoloVO getFascicolo() {
		return fascicolo;
	}

	public SerieTitoloVO getTitolo() {
		return titolo;
	}

}
