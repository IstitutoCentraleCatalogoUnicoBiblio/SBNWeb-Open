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
package it.iccu.sbn.webservices.periodici;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("1.0/periodici")
public interface WSPeriodiciREST {

	@GET
	@Path("kardex/{bib}/{iddoc}")
	@Produces({ MediaType.APPLICATION_XML })
	public abstract Response getKardexTitolo(@Context HttpServletRequest request,
			@PathParam("bib") String cd_bib,
			@PathParam("iddoc") String idDoc, @Context UriInfo info) throws Exception;

	@GET
	@Path("kardex/{bib}/{iddoc}/collocazione")
	@Produces({ MediaType.APPLICATION_XML })
	public abstract Response getKardexCollocazione(@Context HttpServletRequest request,
			@PathParam("bib") String cd_bib,
			@PathParam("iddoc") String idDoc, @QueryParam("id") String tag950, @Context UriInfo info) throws Exception;

	@GET
	@Path("kardex/{bib}/{iddoc}/inventario")
	@Produces({ MediaType.APPLICATION_XML })
	public abstract Response getKardexInventario(@Context HttpServletRequest request,
			@PathParam("bib") String cd_bib,
			@PathParam("iddoc") String idDoc, @QueryParam("id") String kinv, @Context UriInfo info,
			@QueryParam("disp") boolean withDisponibilita) throws Exception;

}
