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
package it.finsiel;

import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.trimOrNull;

import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.finsiel.sbn.exception.ApplicationException;
import it.finsiel.sbn.polo.ejb.factory.SbnMarcEJBFactory;
import it.finsiel.sbn.polo.ejb.factory.SbnMarcEJBFactoryIntf;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.util.MarshallingUtil;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;



/**
 * Servlet implementation class for Servlet: SbnMarcTest
 *
 * @web.servlet name="SbnMarcTest" display-name="SbnMarcTest"
 *
 * @web.servlet-mapping url-pattern="/SbnMarcTest"
 *
 */
public class SbnMarcTest extends HttpServlet {

	private static final long serialVersionUID = -1496235322466081952L;

	private static Logger log = Logger.getLogger("sbnmarcPolo");

	private SbnMarcEJBFactoryIntf factory;

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public SbnMarcTest() {
		super();
	}

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String azione = request.getParameter("azione");
		if (ValidazioneDati.equals(azione, "isbdMusica")) {
			calcolaIsbdMusica(request, response);
			return;
		}

		if (azione != null) {
			String userId = request.getParameter("userId");
			gestioneProfilazione(azione, userId, response);
			return;
		}

		String xml_text = request.getParameter("testo_xml");
		String result;

		try {
			try {
				result = factory.getGateway().execute(xml_text);
			} catch (ApplicationException e) {
				log.error("", e);
				result = e.getMessage();
			}

			response.setContentType("text/xml");
			PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			out.print(result);
			out.close();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void calcolaIsbdMusica(HttpServletRequest request, HttpServletResponse response) {
		
		final String a_929 = trimOrNull(request.getParameter("numOrdine"));
		final String b_929 = trimOrNull(request.getParameter("numOpera"));
		final String c_929 = trimOrNull(request.getParameter("numCatTem"));
		final String e_929 = trimOrNull(request.getParameter("tonalita"));
		final String f_929 = trimOrNull(request.getParameter("sezioni"));
		final String g_929 = trimOrNull(request.getParameter("titOrdinam"));
		final String h_929 = trimOrNull(request.getParameter("titEstratto"));
		final String i_929 = trimOrNull(request.getParameter("appellativo"));
		final String[] a_928 = request.getParameterValues("formeMusicale");
		final String b_928 = trimOrNull(request.getParameter("orgSintComp"));
		final String c_928 = trimOrNull(request.getParameter("orgAnalComp"));

		final String output = this.calcolaIsbdMusica(a_929, b_929, c_929, e_929, f_929,
				g_929, h_929, i_929, a_928, b_928, c_928);

		//almaviva5_20140711 #5606
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			out.print(output);
			out.close();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	private String calcolaIsbdMusica(String a_929, String b_929, String c_929, String e_929, String f_929, String g_929,
			String h_929, String i_929, String[] a_928, String b_928, String c_928) {
		try {
			String isbd = factory.getGateway().definisciIsbdTitUniMusicale(a_929, b_929, c_929, e_929, f_929,
					g_929, h_929, i_929, a_928, b_928, c_928);

			final BigDecimal schemaVersion = ResourceLoader.getPropertyBigDecimal("SCHEMA_VERSION");

			SBNMarc sbnmarc = new SBNMarc();
			SbnMessageType message = new SbnMessageType();
			SbnResponseType response = new SbnResponseType();
			SbnResultType result = new SbnResultType();
			result.setTestoEsito(isbd);
			SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
			SbnOutputType output = new SbnOutputType();
			result.setEsito("0000");
			sbnmarc.setSbnMessage(message);
			sbnmarc.setSbnUser(FormatoErrore.FAKE_SBN_USER);
			sbnmarc.setSchemaVersion(schemaVersion);
			message.setSbnResponse(response);
			response.setSbnResult(result);
			response.setSbnResponseTypeChoice(responseChoice);
			responseChoice.setSbnOutput(output);

			return MarshallingUtil.marshal(sbnmarc);

		} catch (SbnMarcDiagnosticoException e) {
			return FormatoErrore.preparaMessaggioErrore(e.getErrorID());
		} catch (EccezioneSbnDiagnostico e) {
			return FormatoErrore.preparaMessaggioErrore(e.getErrorID());
		} catch (Exception e) {
			log.error("", e);
			return FormatoErrore.preparaMessaggioErrore(56);
		}

	}

	/*
	 * (non-Java-doc)
	 *
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String azione = request.getParameter("azione");

		if (azione != null) {
			String userId = request.getParameter("userId");
			gestioneProfilazione(azione, userId, response);
			return;
		}

		String xml_text = request.getParameter("testo_xml");

		if (xml_text == null) {
			try {
				BufferedReader breader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
				StringBuffer xml_text_buffer = new StringBuffer();
				String line = null;
				while ((line = breader.readLine()) != null)
					xml_text_buffer.append(line);

				xml_text = xml_text_buffer.toString();

			} catch (IOException e) {
				log.error("", e);
			}

		} else if (xml_text.equals("KEEP_ALIVE"))
			xml_text = "KEEP_ALIVE";

		try {
			String result;

			try {
				result = factory.getGateway().execute(xml_text);
			} catch (ApplicationException e) {
				log.error("", e);
				result = e.getMessage();
			}

			response.setContentType("text/xml");
			PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			out.print(result);
			out.close();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {

		super.init();
		factory = SbnMarcEJBFactory.getFactory();
		Decodificatore.reload();
	}

	private void gestioneProfilazione(String azione, String userId, HttpServletResponse response)
			throws ServletException {

		try {
			Serializable result = factory.getGateway().service(azione, userId);
			if (result != null && result instanceof String
					&& ValidazioneDati.isFilled((String) result)) {
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
				out.print(result);
				out.close();
			}

		} catch (Exception e) {
			log.error("", e);
		}

	}

}
