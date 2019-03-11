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
package it.iccu.sbn.vo.custom.servizi.sale;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.util.ArrayList;
import java.util.List;

public class GruppoPostiDecorator extends GruppoPostiVO {

	private static final long serialVersionUID = -7370790245874033307L;

	private List<String> descrCategorieMediazione = new ArrayList<String>();

	public GruppoPostiDecorator(GruppoPostiVO gp) {
		super(gp);
		for (String cm : this.getCategorieMediazione())
			try {
				String descr = CodiciProvider.cercaDescrizioneCodice(cm, CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE,
					CodiciRicercaType.RICERCA_CODICE_SBN);
				descrCategorieMediazione.add(descr);
			} catch (Exception e) {	}
	}

	public List<String> getDescrCategorieMediazione() {
		return descrCategorieMediazione;
	}

}
