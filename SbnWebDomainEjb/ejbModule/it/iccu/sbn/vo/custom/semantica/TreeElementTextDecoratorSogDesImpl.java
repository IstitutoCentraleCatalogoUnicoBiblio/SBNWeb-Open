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
package it.iccu.sbn.vo.custom.semantica;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.tree.TreeElementTextDecoratorBaseImpl;

public class TreeElementTextDecoratorSogDesImpl extends	TreeElementTextDecoratorBaseImpl {

	private static final long serialVersionUID = 7780503711326639215L;

	private static final char NOBREAK_SPACE = '\u00A0';

	@Override
	public String getText(TreeElementView element) {
		TreeElementViewSoggetti node = (TreeElementViewSoggetti) element;
		SbnAuthority authority = node.getTipoAuthority();
		if (authority == null)
			return super.getText(element);

		switch (authority.getType()) {
		case SbnAuthority.SO_TYPE: {
			DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) node.getDettaglio();
			element.setFlagCondiviso(dettaglio.isCondiviso());
			return super.getText(node);
		}

		case SbnAuthority.DE_TYPE: {
			if (node.isRoot() )
				return super.getText(node);

			String legame = node.getDatiLegame() != null ? node.getDatiLegame().toString() : null;
			DettaglioDescrittoreVO dettaglio = (DettaglioDescrittoreVO) node.getDettaglio();
			String edizione = dettaglio.getEdizioneSoggettario();
			StringBuilder buf = new StringBuilder(160);
			if (isFilled(legame) && !ValidazioneDati.in(legame, "931"))
				buf.append(legame).append(NOBREAK_SPACE);

			if (isFilled(edizione))
				buf.append(edizione).append(NOBREAK_SPACE);

			buf.append(dettaglio.getTesto());
			node.setText(buf.toString());
			break;
		}

		default:
			break;
		}

		return super.getText(element);
	}

}
