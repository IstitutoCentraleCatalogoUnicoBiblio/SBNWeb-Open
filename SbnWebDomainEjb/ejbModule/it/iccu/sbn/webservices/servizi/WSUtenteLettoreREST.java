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
package it.iccu.sbn.webservices.servizi;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("1.0/servizi")
public interface WSUtenteLettoreREST {

	@GET
	@Path("utente/accesso")
	@Produces({ MediaType.APPLICATION_JSON })
	public abstract Response accessoUtenteJSON(@Context HttpServletRequest request,
			@Context HttpHeaders headers, @QueryParam("bib") String cd_bib,
			@QueryParam("uid") String idTessera,
			@QueryParam("pwd") String password,
			@QueryParam("azione") String action,
			@QueryParam("allow") Boolean allowNonMembers) throws Exception;

	@GET
	@Path("utente/accesso")
	@Produces({ MediaType.APPLICATION_XML })
	public abstract Response accessoUtenteXML(@Context HttpServletRequest request,
			@Context HttpHeaders headers, @QueryParam("bib") String cd_bib,
			@QueryParam("uid") String idTessera,
			@QueryParam("pwd") String password,
			@QueryParam("azione") String action,
			@QueryParam("allow") Boolean allowNonMembers) throws Exception;

}
