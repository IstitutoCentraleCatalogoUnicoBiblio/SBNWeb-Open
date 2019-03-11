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
package it.iccu.sbn.web.actions.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import it.iccu.sbn.web2.navigation.Navigation;

public class SbnDownloadAction extends DownloadAction {

	private static final String FILE_DOWNLOAD_DATA = "attr.file.download.data";
	private static final String FILE_DOWNLOAD_NAME = "attr.file.download.name";

	public static final ActionForward downloadFile(HttpServletRequest request, String fileName, byte[] fileData) {
		Navigation navi = Navigation.getInstance(request);
		HttpSession session = request.getSession();
		session.setAttribute(FILE_DOWNLOAD_NAME, fileName);
		session.setAttribute(FILE_DOWNLOAD_DATA, fileData);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		session.setAttribute(Navigation.ACTION_WITH_FORM, false);
		return mapping.findForward("fileDownloadSbn");
	}

	public static final ActionForward downloadFile(HttpServletRequest request, String fileName, File fileRef) {
		Navigation navi = Navigation.getInstance(request);
		HttpSession session = request.getSession();
		session.setAttribute(FILE_DOWNLOAD_NAME, fileName);
		session.setAttribute(FILE_DOWNLOAD_DATA, fileRef);
		ActionMapping mapping = navi.getCache().getCurrentElement().getMapping();

		session.setAttribute(Navigation.ACTION_WITH_FORM, false);
		return mapping.findForward("fileDownloadSbn");
	}

	public class BytesStreamInfo implements StreamInfo {

		private byte[] bytes = null;
		private File fileRef = null;

		private BytesStreamInfo(File fileRef) {
			super();
			this.fileRef = fileRef;
		}

		private BytesStreamInfo(byte[] data) {
			super();
			this.bytes = data;
		}

		public String getContentType() {
			return "application/octet-stream";
		}

		public InputStream getInputStream() throws IOException {
			InputStream is = null;
			if (fileRef != null)
				is = new FileInputStream(fileRef);
			else
				is = new ByteArrayInputStream(bytes);

            BufferedInputStream bis = new BufferedInputStream(is);
            return bis;
		}

	}

	@Override
	protected StreamInfo getStreamInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		String fileName = (String) session.getAttribute(FILE_DOWNLOAD_NAME);
		response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.addHeader("Content-Transfer-Encoding", "binary");

		Serializable attribute = (Serializable) session.getAttribute(FILE_DOWNLOAD_DATA);

		session.removeAttribute(FILE_DOWNLOAD_NAME);
		session.removeAttribute(FILE_DOWNLOAD_DATA);

		if (attribute instanceof File)
			return new BytesStreamInfo((File)attribute);
		else
			return new BytesStreamInfo((byte[])attribute);
	}

}
