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
package it.iccu.sbn.webservices.servizi.impl;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.webservices.WSRESTBaseImpl;
import it.iccu.sbn.webservices.servizi.WSServiziILLREST;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

public class WSServiziILLRESTImpl extends WSRESTBaseImpl implements
		WSServiziILLREST {

	private static final int PARAM_LENGTH = ILLRequestBuilder.PARAM_BUFFER_ILL.length();

	private static Logger log = Logger.getLogger(WSServiziILLREST.class);

	private Servizi servizi;

	private Servizi getServizi() throws Exception {
		if (servizi != null)
			return servizi;

		servizi = DomainEJBFactory.getInstance().getServizi();
		return servizi;
	}

	private BibliotecaVO getBiblioteca(String isil) throws Exception {
		AmministrazioneBiblioteca biblioteca = DomainEJBFactory.getInstance().getBiblioteca();
		BibliotecaRicercaVO richiesta = new BibliotecaRicercaVO();
		richiesta.setCodiceAna(isil);
		String ticket = SbnSIP2Ticket.getUtenteTicket("XXX", " XX", isil, InetAddress.getLocalHost());
		List<BibliotecaVO> biblioteche = biblioteca.cercaBiblioteche(ticket, richiesta);
		if (ValidazioneDati.size(biblioteche) != 1)
			throw new ApplicationException(SbnErrorTypes.AMM_BIBLIOTECA_NON_TROVATA);

		return ValidazioneDati.first(biblioteche);
	}


	public Response apdu(HttpServletRequest request, HttpHeaders headers, String isil, String query) throws Exception {
		try {
			addClient(request);

			isil = isil.toUpperCase();
			log.debug("ILL Target ISIL: " + isil);

			//ricerca biblioteca per cod.anagrafe
			BibliotecaVO bib = getBiblioteca(isil);
			String ticket = SbnSIP2Ticket.getUtenteTicket(bib.getCod_polo(), bib.getCod_bib(), Constants.UTENTE_WEB_TICKET, InetAddress.getLocalHost());

			int pos = query.lastIndexOf(ILLRequestBuilder.PARAM_BUFFER_ILL);
			String body = (pos < 0) ? query : query.substring(pos + PARAM_LENGTH + 1);
			if (!ValidazioneDati.isFilled(body))
				return Response.status(Status.NO_CONTENT).build();

			String xml = URLDecoder.decode(body, "UTF-8");//ILLRequestBuilder.APDU_XML_ENCODING);

			if (log.isDebugEnabled()) {
				try {
					log.debug("APDU XML: " + xml);
					String path = CommonConfiguration.getProperty(Configuration.ILL_XML_SAVE_PATH, FileUtil.getTempFilesDir() );
					String fileName = path + File.separator + ILLRequestBuilder.getInXMLName(ticket, xml, isil);
					FileUtil.writeStringToFile(fileName, xml);
				} catch (Exception e) {	}
			}

			CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.SRV_ILL_XML_REQUEST, xml, isil);
			CommandResultVO result = getServizi().invoke(command);

			return Response.ok(result.getResult()).build();

		} finally {
			removeClient();
		}
	}

	public static void main(String... args) throws UnsupportedEncodingException {
		String test = ILLRequestBuilder.PARAM_BUFFER_ILL
				+ "=%3C%3Fxml+version%3D%221.0%22+encoding%3D%22iso-8859-1%22%3F%3E%0A%0A%3C%21DOCTYPE+ILL-APDU%3E%0A%0A%3CILL-APDU%3E%0A++%3CILL-Request+ILL-SERVICE-TYPE%3D%22PR%22%3E%0A++++%3CTransaction-Id%3E48815%3C%2FTransaction-Id%3E";
		test = test.substring(test
				.lastIndexOf(ILLRequestBuilder.PARAM_BUFFER_ILL)
				+ PARAM_LENGTH + 1);
		test = URLDecoder.decode(test, "UTF-8");
		System.out.println(test);
	}

}
