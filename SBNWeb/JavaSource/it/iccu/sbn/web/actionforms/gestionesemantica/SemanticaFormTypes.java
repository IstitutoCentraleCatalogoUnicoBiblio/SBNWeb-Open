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
package it.iccu.sbn.web.actionforms.gestionesemantica;

import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AnaliticaDescrittoreForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AnaliticaSoggettoForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.EsaminaDescrittoreForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.GestioneDescrittoreForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.ListaSoggettiForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.RicercaSoggettoForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.SinteticaDescrittoriForm;

import org.apache.struts.action.ActionForm;

public enum SemanticaFormTypes {

	NOT_VALID,

	RICERCA_SOGGETTO				(RicercaSoggettoForm.class),
	SINTETICA_SOGGETTI				(ListaSoggettiForm.class),
	ANALITICA_SOGGETTO				(AnaliticaSoggettoForm.class),

	SINTETICA_DESCRITTORI			(SinteticaDescrittoriForm.class),
	ANALITICA_DESCRITTORE			(AnaliticaDescrittoreForm.class),
	ESAMINA_DESCRITTORE				(EsaminaDescrittoreForm.class),
	GESTIONE_DESCRITTORE			(GestioneDescrittoreForm.class),

	CATALOGAZIONE_SEMANTICA			(CatalogazioneSemanticaForm.class);

	private final Class<? extends ActionForm> formClass;

	private SemanticaFormTypes() {
		formClass = null;
	}

	private SemanticaFormTypes(Class<? extends ActionForm> formClass) {
		this.formClass = formClass;
	}

	public static final SemanticaFormTypes getFormType(ActionForm form) {
		for (SemanticaFormTypes type : SemanticaFormTypes.values() )
			if (type.formClass != null && type.formClass.isInstance(form))
				return type;

		return NOT_VALID;
	}

}
