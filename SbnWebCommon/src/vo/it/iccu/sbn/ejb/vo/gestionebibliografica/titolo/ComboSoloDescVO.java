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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Comparator;

public class ComboSoloDescVO extends SerializableVO {

	private static final long serialVersionUID = 3320356477834249574L;

    public static final Comparator<ComboSoloDescVO> ORDINA_PER_DESCRIZIONE = new Comparator<ComboSoloDescVO>() {
    	private OrdinamentoUnicode u = new OrdinamentoUnicode();
    	public int compare(ComboSoloDescVO o1, ComboSoloDescVO o2) {
    		String descr1 = trimOrEmpty(u.convert(o1.descrizione));
    		String descr2 = trimOrEmpty(u.convert(o2.descrizione));
    		return descr1.compareTo(descr2);
    	}
    };

	private String descrizione;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

}
