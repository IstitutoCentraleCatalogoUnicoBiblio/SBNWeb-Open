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
package it.iccu.sbn.web.integration.actionforms.servizi;

import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm;

import org.apache.struts.action.ActionForm;

public enum ServiziFormTypes {

	NOT_VALID,

	LISTA_MOVIMENTI					(ListaMovimentiForm.class);

	private final Class<? extends ActionForm> formClass;

	private ServiziFormTypes() {
		formClass = null;
	}

	private ServiziFormTypes(Class<? extends ActionForm> formClass) {
		this.formClass = formClass;
	}

	public static final ServiziFormTypes getFormType(ActionForm form) {
		for (ServiziFormTypes type : ServiziFormTypes.values() )
			if (type.formClass != null && type.formClass.isInstance(form))
				return type;

		return NOT_VALID;
	}

}
