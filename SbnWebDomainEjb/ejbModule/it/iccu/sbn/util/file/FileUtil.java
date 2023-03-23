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
package it.iccu.sbn.util.file;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.mozilla.intl.chardet.nsDetector;


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
		buf.append(getTempFilesDir()).append(File.separator);
		buf.append("sbnweb_").append(System.currentTimeMillis()).append(".tmp");
		return buf.toString();
	}

	public static String getTempFilesDir() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getUserHomeDir() {
		return System.getProperty("user.home");
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

	public static String guessCharset(File f) throws IOException {
		//almaviva5_20140826 segnalazione TO0
		FileInputStream in = null;
		try {
			nsDetector detector = new nsDetector();
			in = new FileInputStream(f);
			byte[] buf = new byte[BUFFER_SIZE];
			int len = 0;
			int read = 0;
			boolean done = false;
			while ((len = in.read(buf, 0, buf.length)) != -1) {
				read += len;
				if (!done)
					done = detector.DoIt(buf, len, false);
				else
					if (read > BUFFER_SIZE)
						break;
			}
			detector.DataEnd();
			String[] charsets = detector.getProbableCharsets();
			return ValidazioneDati.isFilled(charsets) ? charsets[0] : "UTF-8";

		} finally {
			close(in);
		}
	}

	public static String streamToString(InputStream in) throws IOException {

	    String body = null;
	    StringBuilder buf = new StringBuilder(BUFFER_SIZE);
	    BufferedReader r = null;

	    try {
	        if (in != null) {
	            r = new BufferedReader(new InputStreamReader(in));
	            char[] buffer = new char[BUFFER_SIZE];
	            int read = -1;
	            while ((read = r.read(buffer)) > 0) {
	                buf.append(buffer, 0, read);
	            }
	        } else {
	            buf.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (r != null) {
	            try {
	                r.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = buf.toString();
	    return body;
	}

	public static boolean writeStringToFile(String fileName, String content) throws Exception {
		return writeStringToFile(fileName, content, "UTF-8");
	}

	public static boolean writeStringToFile(String fileName, String content, String encoding) throws Exception {
		return transfer(new ByteArrayInputStream(content.getBytes(encoding)), new FileOutputStream(fileName));
	}

}
