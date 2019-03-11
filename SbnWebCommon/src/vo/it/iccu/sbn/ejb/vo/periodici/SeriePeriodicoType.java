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
import it.iccu.sbn.ejb.vo.periodici.esame.SerieFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieInventarioVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieTitoloVO;

public enum SeriePeriodicoType {

	ESEMPLARE(SerieEsemplareCollVO.class),
	COLLOCAZIONE(SerieCollocazioneVO.class),
	ORDINE(SerieOrdineVO.class),
	FASCICOLO(SerieFascicoloVO.class),
	TITOLO(SerieTitoloVO.class),
	INVENTARIO(SerieInventarioVO.class);


	private final Class<? extends SerieBaseVO> clazz;

	private SeriePeriodicoType(Class<? extends SerieBaseVO> clazz) {
		this.clazz = clazz;
	}

	public static final SeriePeriodicoType fromClass(SerieBaseVO serie) {

		for (SeriePeriodicoType spt : SeriePeriodicoType.values())
			if (spt.clazz.isAssignableFrom(serie.getClass()))
				return spt;

		return null;
	}

}
