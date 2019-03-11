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
package it.iccu.sbn.ejb.utils.menu;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.model.attivita.MenuAttivitaChecker;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UserWrapper;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.web.vo.UserVO;

public class ServiziILLBibMenuChecker implements MenuAttivitaChecker {

	static Reference<Servizi> servizi = new Reference<Servizi>() {
		@Override
		protected Servizi init() throws Exception {
			return DomainEJBFactory.getInstance().getServizi();
		}};

	public boolean check(UserWrapper uw) {
		UserVO user = uw.getUser();
		try {
			ParametriBibliotecaVO parametriBiblioteca = servizi.get().getParametriBiblioteca(user.getTicket(),
					user.getCodPolo(), user.getCodBib());
			if (parametriBiblioteca == null)
				return false;

			//la biblioteca gestisce i servizi ILL
			return parametriBiblioteca.isServiziILLAttivi();

		} catch (Exception e) {
			return false;
		}
	}

}
