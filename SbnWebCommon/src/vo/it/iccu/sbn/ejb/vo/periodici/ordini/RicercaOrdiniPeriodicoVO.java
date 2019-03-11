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
package it.iccu.sbn.ejb.vo.periodici.ordini;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.RicercaBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;

public class RicercaOrdiniPeriodicoVO extends RicercaBaseVO<SerieOrdineVO> {

	private static final long serialVersionUID = 1950186040549085560L;

	private SerieBaseVO oggettoRicerca;
	private BibliotecaVO biblioteca;

	public SerieBaseVO getOggettoRicerca() {
		return oggettoRicerca;
	}

	public void setOggettoRicerca(SerieBaseVO oggettoRicerca) {
		this.oggettoRicerca = oggettoRicerca;
	}

	public BibliotecaVO getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(BibliotecaVO biblioteca) {
		this.biblioteca = biblioteca;
	}

	public RicercaOrdiniPeriodicoVO(BibliotecaVO biblioteca,
			SerieBaseVO oggettoRicerca) {
		super();
		this.biblioteca = biblioteca;
		this.oggettoRicerca = oggettoRicerca;
	}



}
