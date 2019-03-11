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

import it.iccu.sbn.ejb.vo.periodici.fascicolo.StatoFascicolo;
import it.iccu.sbn.ejb.vo.periodici.previsionale.PeriodicitaFascicoloType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RicercaKardexPeriodicoPerStampaOrdineVO<T> extends RicercaKardexPeriodicoVO<T> {

	private static final long serialVersionUID = 9197757629276257919L;

	private Set<StatoFascicolo> filtroStatoFascicolo = new HashSet<StatoFascicolo>();

	private Character stato_ordine;
	private PeriodicitaFascicoloType cd_per;
	private boolean includiNote;


	public RicercaKardexPeriodicoPerStampaOrdineVO() {
		super();
	}

	public Set<StatoFascicolo> getFiltroStatoFascicolo() {
		return Collections.unmodifiableSet(filtroStatoFascicolo);
	}

	public void addFiltroStatoFascicolo(StatoFascicolo spt) {
		filtroStatoFascicolo.add(spt);
	}

	public void removeFiltroStatoFascicolo(StatoFascicolo spt) {
		filtroStatoFascicolo.remove(spt);
	}

	public Character getStato_ordine() {
		return stato_ordine;
	}

	public void setStato_ordine(Character stato_ordine) {
		this.stato_ordine = stato_ordine;
	}

	public PeriodicitaFascicoloType getCd_per() {
		return cd_per;
	}

	public void setCd_per(PeriodicitaFascicoloType cd_per) {
		this.cd_per = cd_per;
	}

	public boolean isIncludiNote() {
		return includiNote;
	}

	public void setIncludiNote(boolean includiNote) {
		this.includiNote = includiNote;
	}

}
