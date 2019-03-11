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
package it.iccu.sbn.webservices.documentofisico.impl;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.webservices.WSRESTBaseImpl;
import it.iccu.sbn.webservices.documentofisico.WSDocumentoFisicoREST;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WSDocumentoFisicoRESTImpl extends WSRESTBaseImpl implements WSDocumentoFisicoREST {

	private SbnwebType posseduto(String cd_bib, String bid, boolean withDisponibilita, boolean withFascicoli)
			throws ValidationException, RemoteException, DaoManagerException, NamingException, CreateException,
			UnknownHostException, ApplicationException, Exception {
		if (!ValidazioneDati.leggiXID(bid))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "bid");

		String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
		String codPolo = DomainEJBFactory.getInstance().getPolo().getInfoPolo().getCd_polo();

		String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getLocalHost());
		CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.GDF_POSSEDUTO_DOCUMENTO_WS, codPolo, codBib,
				bid, withDisponibilita, withFascicoli);
		CommandResultVO result = DomainEJBFactory.getInstance().getCollocazione().invoke(command);
		result.throwError();

		return (SbnwebType) result.getResult();
	}

	public Response getPossedutoXML(HttpServletRequest request, String cd_bib, String bid, boolean withDisponibilita, boolean withFascicoli) throws Exception {
		try {
			addClient(request);
			SbnwebType result = posseduto(cd_bib, bid, withDisponibilita, withFascicoli);

			return Response.ok(result).build();

		} catch (SbnBaseException e) {
			return Response.ok().entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();

		} catch (Exception e) {
			throw new WebApplicationException(e);

		} finally {
			removeClient();
		}
	}

	public Response getPossedutoJSON(HttpServletRequest request, String cd_bib, String bid, boolean withDisponibilita, boolean withFascicoli) throws Exception {
		try {
			addClient(request);
			SbnwebType result = posseduto(cd_bib, bid, withDisponibilita, withFascicoli);

			return Response.ok(ClonePool.toJson(result, SbnwebType.class)).build();

		} catch (SbnBaseException e) {
			SbnwebType errore = ConversioneHibernateVO.toXML().formattaErrore(e);
			return Response.ok().entity(ClonePool.toJson(errore, SbnwebType.class)).build();

		} catch (Exception e) {
			throw new WebApplicationException(e);

		} finally {
			removeClient();
		}
	}

}
