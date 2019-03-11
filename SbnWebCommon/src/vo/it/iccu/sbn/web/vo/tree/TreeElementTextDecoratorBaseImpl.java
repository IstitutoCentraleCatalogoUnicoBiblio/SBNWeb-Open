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
package it.iccu.sbn.web.vo.tree;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.TreeElementView.TreeElementTextDecorator;

public class TreeElementTextDecoratorBaseImpl extends SerializableVO implements TreeElementTextDecorator {

	private static final long serialVersionUID = 283109795391904546L;

	public String getText(TreeElementView element) {
		String text = element.getText();
		if (!element.isFlagCondiviso()) {
			// i descrittori sono sempre locali
			// Modifica del 2 Ott. 2009 realizzata da almaviva2
			// i Possessori essendo solo locali non hanno necessita della precisazione [loc] come i dati dei descrittori
			boolean isDes = (element.getTipoAuthority() != null) &&
				ValidazioneDati.eqAuthority(element.getTipoAuthority(),
					SbnAuthority.DE, SbnAuthority.TH, SbnAuthority.PP);
			if (!isDes)
				text = "[Loc]\u00A0" + text;
		}

		return text;
	}

}
