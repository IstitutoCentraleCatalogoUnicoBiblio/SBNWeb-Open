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
package it.iccu.sbn.ejb;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.blocchi.DescrittoreBlocchiUtil;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;

import java.util.List;

import javax.ejb.EJBException;

@SuppressWarnings("rawtypes")
public abstract class SbnBusinessSessionBean extends
		AbstractStatelessSessionBean {


	private static final long serialVersionUID = 8384678594626831445L;

	protected DescrittoreBloccoVO saveBlocchi(String ticket, List sintetica,
			int elemBlocco) {

		return DescrittoreBlocchiUtil.saveBlocchi(ticket, sintetica, elemBlocco, null);
	}

	protected DescrittoreBloccoVO saveBlocchi(String ticket, List sintetica,
			int elemBlocco, DescrittoreBloccoInterceptor interceptor) {

		return DescrittoreBlocchiUtil.saveBlocchi(ticket, sintetica, elemBlocco, interceptor);
	}

	protected DescrittoreBloccoVO appendBlocchi(String ticket, List sintetica,
			int elemBlocco, String idLista, int ultimoBlocco) {

		return DescrittoreBlocchiUtil.appendBlocchi(ticket, sintetica, elemBlocco, idLista, ultimoBlocco, null);
	}

	public DescrittoreBloccoVO nextBlocco(String ticket, String idLista,
			int numBlocco) throws EJBException, ValidationException {

		return DescrittoreBlocchiUtil.nextBlocco(ticket, idLista, numBlocco);
	}

	public void clearBlocchiIdLista(String ticket, String idLista)
			throws EJBException, ValidationException {

		DescrittoreBlocchiUtil.clearBlocchiIdLista(ticket, idLista);
	}

	public void clearBlocchiAll(String ticket) throws EJBException, ValidationException {
		DescrittoreBlocchiUtil.clearBlocchiAll(ticket);
	}

}
