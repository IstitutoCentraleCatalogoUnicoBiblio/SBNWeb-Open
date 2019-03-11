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
package it.iccu.sbn.util.cloning.impl;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.extension.cloning.ActualCloner;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.ref.SoftReference;

import org.apache.log4j.Logger;

public class JDKActualCloner extends UniqueIdentifiableVO implements ActualCloner {

	private static final long serialVersionUID = -2336589106283897834L;

	private static class CustomObjectInputStream extends ObjectInputStream {
		private ClassLoader classLoader;

		public CustomObjectInputStream(InputStream in, ClassLoader classLoader)
				throws IOException {
			super(in);
			if (classLoader != null)
				this.classLoader = classLoader;
			else
				this.classLoader = Thread.currentThread().getContextClassLoader();
		}

		protected Class<?> resolveClass(ObjectStreamClass desc)
				throws ClassNotFoundException {
			return Class.forName(desc.getName(), false, classLoader);
		}

	}

	private Logger log;
	private int BUFFER_SIZE;
	private int objectCnt;

	private SoftReference<NoSyncByteArrayOutputStream> bufferRef;


	private NoSyncByteArrayOutputStream getBuffer() {
		NoSyncByteArrayOutputStream out = bufferRef.get();
		if (out == null) {
			if (log.isInfoEnabled())
				log.warn("buffer garbaged (" + objectCnt + " calls)");
			out = new NoSyncByteArrayOutputStream(BUFFER_SIZE);
			bufferRef = new SoftReference<NoSyncByteArrayOutputStream>(out);
			objectCnt = 0;
		}
		return out;

	}

	@SuppressWarnings("unchecked")
	public <T> T deepCopy(T src) throws Exception {

		NoSyncByteArrayOutputStream baos = getBuffer();

		objectCnt++;

		baos.reset();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(src);
		byte buf[] = baos.toByteArray();
		oos.close();

		NoSyncByteArrayInputStream bais = new NoSyncByteArrayInputStream(buf);
		ObjectInputStream ois = new CustomObjectInputStream(bais, src.getClass().getClassLoader());

		Object newInstance = ois.readObject();
		ois.close();

		if (log.isDebugEnabled())
			log.debug(src.getClass().getSimpleName() + "@"
					+ Integer.toHexString(src.hashCode()) + " clone size: "
					+ baos.size() + " bytes");

		return (T) newInstance;
	}

	public boolean init() {
		try {
			this.log = Logger.getLogger(this.getClass().getCanonicalName() + '-' + uniqueId);
			this.BUFFER_SIZE = CommonConfiguration.getPropertyAsInteger(Configuration.JBAC_BUFFER_SIZE, 4096);
			this.bufferRef = new SoftReference<NoSyncByteArrayOutputStream>(new NoSyncByteArrayOutputStream(BUFFER_SIZE));

			log.debug("init()");

		} catch (Exception e) {
			log.error("", e);
			return false;
		}
		return true;
	}

}
