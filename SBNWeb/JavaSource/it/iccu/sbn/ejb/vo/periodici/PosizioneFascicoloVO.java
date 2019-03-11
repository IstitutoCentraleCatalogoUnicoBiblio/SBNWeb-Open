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

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;

public class PosizioneFascicoloVO extends SerializableVO {


	private static final long serialVersionUID = 7322157909181093987L;
	private final FascicoloDecorator fascicolo;
	private final DescrittoreBloccoVO blocco;

	public DescrittoreBloccoVO getBlocco() {
		return blocco;
	}

	public PosizioneFascicoloVO(DescrittoreBloccoVO blocco,	FascicoloDecorator fascicolo) {
		super();
		this.blocco = blocco;
		this.fascicolo = fascicolo;
	}

	public FascicoloDecorator getFascicolo() {
		return fascicolo;
	}

}
