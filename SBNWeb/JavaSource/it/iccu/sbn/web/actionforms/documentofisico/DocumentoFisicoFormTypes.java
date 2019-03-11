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
package it.iccu.sbn.web.actionforms.documentofisico;

import it.iccu.sbn.web.actionforms.acquisizioni.ordini.EsaminaOrdineModForm;
import it.iccu.sbn.web.actionforms.documentofisico.datiInventari.VaiAModificaCollForm;
import it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni.EsameCollocEsaminaInventarioForm;

import org.apache.struts.action.ActionForm;

public enum DocumentoFisicoFormTypes {

	NOT_VALID,

	ESAME_INVENTARIO		(EsameCollocEsaminaInventarioForm.class),
	MODIFICA_COLLOCAZIONE	(VaiAModificaCollForm.class),
	ESAMINA_ORDINE			(EsaminaOrdineModForm.class);

	private final Class<? extends ActionForm> formClass;

	private DocumentoFisicoFormTypes() {
		formClass = null;
	}

	private DocumentoFisicoFormTypes(Class<? extends ActionForm> formClass) {
		this.formClass = formClass;
	}

	public static final DocumentoFisicoFormTypes getFormType(ActionForm form) {
		for (DocumentoFisicoFormTypes type : DocumentoFisicoFormTypes.values() )
			if (type.formClass != null && type.formClass.isInstance(form))
				return type;

		return NOT_VALID;
	}

}
