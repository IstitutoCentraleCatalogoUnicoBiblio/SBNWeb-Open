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

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;

public class RicercaKardexPeriodicoPerCreaOrdineVO<T> extends RicercaKardexPeriodicoVO<T> {

	private static final long serialVersionUID = -4186209531484400502L;

	private int annoAbb;
	private boolean continuativo;

	public RicercaKardexPeriodicoPerCreaOrdineVO(BibliotecaVO bib, SerieOrdineVO target, int annoAbb, boolean continuativo) {
		super(bib, target);
		this.annoAbb = annoAbb;
		this.continuativo = continuativo;
	}

	public int getAnnoAbb() {
		return annoAbb;
	}

	public void setAnnoAbb(int annoAbb) {
		this.annoAbb = annoAbb;
	}

	public boolean isContinuativo() {
		return continuativo;
	}

	public void setContinuativo(boolean continuativo) {
		this.continuativo = continuativo;
	}

}
