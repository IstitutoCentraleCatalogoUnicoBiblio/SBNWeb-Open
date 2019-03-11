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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.RicercaBaseTimeRangeVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;

import java.util.Date;
import java.util.List;

public class RicercaKardexPeriodicoVO<T> extends RicercaBaseTimeRangeVO<T> {

	private static final long serialVersionUID = 7760119221693512658L;
	private SerieBaseVO oggettoRicerca;
	private BibliotecaVO biblioteca;
	private String dataScroll;

	private Date kardexBegin;
	private Date kardexEnd;

	private TipoRangeKardex tipoRange = TipoRangeKardex.ANNATA_POSSEDUTA;
	private List<Integer> rangeAnnoPubb;

	public RicercaKardexPeriodicoVO(BibliotecaVO bib, SerieBaseVO target) {
		this.biblioteca = bib;
		this.oggettoRicerca = target;
	}

	public RicercaKardexPeriodicoVO() {
		super();
	}

	public SerieBaseVO getOggettoRicerca() {
		return oggettoRicerca;
	}

	public BibliotecaVO getBiblioteca() {
		return biblioteca;
	}

	public void setOggettoRicerca(SerieBaseVO oggettoRicerca) {
		this.oggettoRicerca = oggettoRicerca;
	}

	public void setBiblioteca(BibliotecaVO biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getDataScroll() {
		return dataScroll;
	}

	public void setDataScroll(String dataScroll) {
		this.dataScroll = trimAndSet(dataScroll);
	}

	public TipoRangeKardex getTipoRange() {
		return tipoRange;
	}

	public void setTipoRange(TipoRangeKardex tipoRange) {
		this.tipoRange = tipoRange;
	}

	public List<Integer> getRangeAnnoPubb() {
		return rangeAnnoPubb;
	}

	public void setRangeAnnoPubb(List<Integer> rangeAnnoPubb) {
		this.rangeAnnoPubb = rangeAnnoPubb;
	}

	public Date getKardexBegin() {
		return kardexBegin;
	}

	public void setKardexBegin(Date kardexBegin) {
		this.kardexBegin = kardexBegin;
	}

	public Date getKardexEnd() {
		return kardexEnd;
	}

	public void setKardexEnd(Date kardexEnd) {
		this.kardexEnd = kardexEnd;
	}

	public Date getBegin() {
		return isFilled(dataFrom) ? DateUtil.toDate(dataFrom) : kardexBegin;
	}

	public Date getEnd() {
		return isFilled(dataTo) ? DateUtil.toDate(dataTo) : kardexEnd;
	}

	public boolean isSoloAnnatePossedute() {
		return tipoRange == TipoRangeKardex.ANNATA_POSSEDUTA;
	}

	public void setSoloAnnatePossedute(boolean value) {
		tipoRange = value ? TipoRangeKardex.ANNATA_POSSEDUTA : TipoRangeKardex.DATA_FASCICOLO;
	}

}
