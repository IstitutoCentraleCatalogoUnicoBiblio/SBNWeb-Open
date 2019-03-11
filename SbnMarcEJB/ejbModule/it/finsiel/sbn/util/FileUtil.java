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
package it.finsiel.sbn.util;

import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtil {

	private static final int BUFFER_SIZE = 8 * 1024;

	private static final boolean transfer(InputStream input, OutputStream output) throws Exception {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(input);
			bos = new BufferedOutputStream(output);
			byte[] buf = new byte[BUFFER_SIZE];
			int read = -1;
            while ( (read = bis.read(buf) ) > 0)
                bos.write(buf, 0, read);

		} catch (Exception e) {
			throw e;
		} finally {
			close(bis);
			close(bos);
		}
		return true;

	}


	public static final String getTemporaryFileName() {
		StringBuilder buf = new StringBuilder(255);
		buf.append(System.getProperty("java.io.tmpdir")).append(File.separator);
		buf.append("sbnweb_").append(System.currentTimeMillis()).append(".tmp");
		return buf.toString();
	}


	public static final boolean uploadFile(InputStream input, String output,
			Object validator) throws Exception {

		return transfer(input, new FileOutputStream(output, true));
	}


	public static final byte[] getData(InputStream input) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
		if (transfer(input, baos))
			return baos.toByteArray();
		return null;
	}

	public static final void close(Closeable c) {
		if (c != null)
			try {
				c.close();
			} catch (IOException e) {}
	}


	public static final boolean exists(String file) {
		if (!ValidazioneDati.isFilled(file))
			return false;

		return (new File(file)).exists();
	}


	public static final void flush(Flushable f) {
		if (f != null)
			try {
				f.flush();
			} catch (IOException e) {}
	}

}
