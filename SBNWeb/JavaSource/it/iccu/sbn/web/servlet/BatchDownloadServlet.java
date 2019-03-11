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
package it.iccu.sbn.web.servlet;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.web2.navigation.Navigation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BatchDownloadServlet extends HttpServlet {


	private static final long serialVersionUID = 2881956806937131508L;

	private static Logger log = Logger.getLogger(BatchDownloadServlet.class);

	private static final String FILE_ID = "FILEID";
	private static final String IMAGE = "IMAGE";
	private static final String SBNWEB_CSS_REF = "CSS";
	private static final int BUFFER_SIZE = 8192;


	private void readStream(HttpServletResponse response, String pathname,
			String fileName) throws FileNotFoundException, IOException {
		File f = new File(pathname);

		if (!f.exists() ) {	// non esiste!!!
			log.error("file " + fileName + " non esistente");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// imposto il content size
		response.setContentLength((int) f.length() );

		FileInputStream in = new FileInputStream(f);
		OutputStream out = response.getOutputStream();

		byte[] buf = new byte[BUFFER_SIZE];
		int count = 0;
		while ((count = in.read(buf)) >= 0)
			out.write(buf, 0, count);

		in.close();
		out.close();
	}


	private void execute(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (!checkTicket(request) ) {
			log.error("ticket non valido o scaduto");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String fileName = request.getParameter(FILE_ID);
		String imageName = request.getParameter(IMAGE);

		if (ValidazioneDati.strIsNull(fileName) && ValidazioneDati.strIsNull(imageName) ) {
			log.error("fileName non impostato");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// css dinamico per polo
		if (ValidazioneDati.equals(fileName, SBNWEB_CSS_REF) ) {
			downloadCSS(request, response);
			return;
		}

		// immagine dinamica
		if (ValidazioneDati.isFilled(imageName) ) {
			downloadImage(request, response, imageName);
			return;
		}

		fileName = ValidazioneDati.unmaskString(fileName); // decodifico filename da base64
		String pathname =
			fileName.substring(1).indexOf(File.separator) > -1 ? fileName : StampeUtil.getBatchFilesPath() + File.separator + fileName;
		log.info("richiesto download file: " + pathname);

		// imposto content type
		response.setContentType("application/octet-stream");

		// imposto il nome che il browser proporrà per il download (elimino eventuale path)
		String proposedName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		response.addHeader("Content-Disposition", "attachment; filename=" + proposedName);

		response.addHeader("Content-Transfer-Encoding", "binary");

		readStream(response, pathname, fileName);
	}

	private static class Checker extends TicketChecker {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8293196099026543358L;

		static void check(String ticket) {
			checkTicket(ticket);
		}
	}

	private boolean checkTicket(HttpServletRequest request) {
		Navigation navi = Navigation.getInstance(request);
		String ticket = navi.getUserTicket();
		//almaviva5_20170608 fix check autenticazione
		if (!ValidazioneDati.isFilled(ticket))
			return false;
		try {
			Checker.check(ticket);
			return true;
		} catch (TicketExpiredException e) {
			return false;
		}

	}


	private void downloadImage(HttpServletRequest request,
			HttpServletResponse response, String imageName) throws ServletException, IOException {
		String pathname = StampeUtil.getBatchFilesPath() + File.separator + imageName;
		//log.info("richiesto download image: " + pathname);

		// imposto content type
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Transfer-Encoding", "binary");

		readStream(response, pathname, imageName);

	}

	private void downloadCSS(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = "sbnweb.css";
		String pathname = StampeUtil.getBatchFilesPath() + File.separator + fileName;
		log.info("richiesto download CSS: " + pathname);

		// imposto content type
		response.setContentType("text/css");
		// imposto content size
		response.addHeader("Content-Transfer-Encoding", "UTF8");

		readStream(response, pathname, fileName);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.execute(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.execute(req, resp);
	}

}
