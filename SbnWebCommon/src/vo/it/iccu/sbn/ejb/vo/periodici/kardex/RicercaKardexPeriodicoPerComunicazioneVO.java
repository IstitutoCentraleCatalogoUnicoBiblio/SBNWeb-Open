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

import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;

public class RicercaKardexPeriodicoPerComunicazioneVO<T> extends RicercaKardexPeriodicoVO<T> {

	private static final long serialVersionUID = -5232276636295694648L;
	private ComunicazioneVO comunicazione;

	public ComunicazioneVO getComunicazione() {
		return comunicazione;
	}

	public void setComunicazione(ComunicazioneVO comuniczione) {
		this.comunicazione = comuniczione;
	}

	public RicercaKardexPeriodicoPerComunicazioneVO() {
		super();
	}

	public RicercaKardexPeriodicoPerComunicazioneVO(BibliotecaVO bib,
			SerieBaseVO target, ComunicazioneVO com) {
		super(bib, target);
		this.comunicazione = com;
	}

}
