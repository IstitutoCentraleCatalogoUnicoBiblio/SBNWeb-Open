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

import it.iccu.sbn.web.vo.MarcaImageCache;
import it.iccu.sbn.web2.navigation.Navigation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class MarcaImageServlet extends HttpServlet {


	private static final long serialVersionUID = 4560547093789269020L;
	private static Logger log = Logger.getLogger(MarcaImageServlet.class);
	private static final String IMGKEY = "IMGKEY";
	private static final String NOT_VALID_IMAGE = "/images/notvalid.png";
	private static final int BUFFER_SIZE = 8192;

	public MarcaImageServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String key = request.getParameter(IMGKEY);
		if (key == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String token[] = null;
		int index = 0;
		try {
			token = key.split("\\-");
			index = Integer.parseInt(token[1]);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		MemoryCacheImageInputStream in = null;
		Navigation navi = Navigation.getInstance(request);
		MarcaImageCache imageCache = navi.getMarcaImageCache();

		byte[] imgbuf = imageCache.getImage(token[0], index);
		if (imgbuf == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// provo a caricare l'immagine
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgbuf));
		if (image == null) {
			log.warn("Immagine '" + key + "' non valida");
			// se l'immagine non è valida carico una risorsa di default
			InputStream imgfile = this.getServletContext().getResourceAsStream(
					MarcaImageServlet.NOT_VALID_IMAGE);
			in = new MemoryCacheImageInputStream(imgfile);
		} else {
			//carico l'immagine in memoria
			image.flush();
			in = new MemoryCacheImageInputStream(new ByteArrayInputStream(imgbuf));
		}

		// imposto content type
		response.setContentType("application/octet-stream");
		// imposto content size
		response.setContentLength((int) in.length());
		response.addHeader("Content-Transfer-Encoding", "binary");

		OutputStream out = response.getOutputStream();
		byte[] buf = new byte[BUFFER_SIZE];
		int count = 0;
		while ((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		in.close();
		out.close();
	}

	public void init() throws ServletException {
		return;
	}

}
