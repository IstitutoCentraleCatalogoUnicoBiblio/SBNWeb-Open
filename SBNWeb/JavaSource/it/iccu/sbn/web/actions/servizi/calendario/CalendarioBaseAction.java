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
package it.iccu.sbn.web.actions.servizi.calendario;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.CalendarioDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import javax.servlet.http.HttpServletRequest;

public abstract class CalendarioBaseAction extends ServiziBaseAction {

	public CalendarioBaseAction() {
		super();
	}

	protected void checkCongruenzaCalendarioBib(HttpServletRequest request, ModelloCalendarioVO modello) throws Exception {
		if (modello.getTipo() == TipoCalendario.BIBLIOTECA)
			return;

		ModelloCalendarioVO modelloBib = CalendarioDelegate.getInstance(request).getCalendarioBiblioteca(modello.getCodPolo(), modello.getCodBib());
		if (modelloBib == null)
			throw new ApplicationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_BIB_NON_TROVATO);

		//CalendarioUtil.checkCongruenzaCalendario(modello, modelloBib);
	}

}
