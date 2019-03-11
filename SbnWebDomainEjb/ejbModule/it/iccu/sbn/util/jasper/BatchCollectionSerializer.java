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
package it.iccu.sbn.util.jasper;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.util.file.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class BatchCollectionSerializer {

	private static final String TMP_PATH = FileUtil.getTempFilesDir();
	private static final int REPORT_STOP_SIGN = -5377;
	private static final int BUFFER_SIZE = 65536;

	private final class FileReference {

		private final File file;
		private final RandomAccessFile stream;

		public FileReference(File file, RandomAccessFile stream) {
			this.file = file;
			this.stream = stream;
		}

		public File getFile() {
			return file;
		}

		public RandomAccessFile getStream() {
			return stream;
		}
	}

	private final class FastByteArrayOutputStream extends ByteArrayOutputStream {

		public FastByteArrayOutputStream(int bufferSize) {
			super(bufferSize);
		}

		@Override
		public byte[] toByteArray() {
			return super.buf;
		}
	}

	private final class CustomObjectInputStream extends ObjectInputStream {
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

	private static final Map<String, BatchCollectionSerializer> serializers = new THashMap<String, BatchCollectionSerializer>();

	private final Map<String, FileReference> files = new THashMap<String, FileReference>();
	private final ByteArrayOutputStream baos = new FastByteArrayOutputStream(BUFFER_SIZE);

	private String id;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private String idBatch = null;

	private long read;
	private long written;

	private String firmaBatch;


	public static final BatchCollectionSerializer forBatch(ParametriRichiestaElaborazioneDifferitaVO params) {
		String id = params.getIdBatch();
		BatchCollectionSerializer jcs = serializers.get(id);
		if (jcs == null) {
			jcs = new BatchCollectionSerializer(params, UUID.randomUUID().toString());
			serializers.put(id, jcs);
		}

		return jcs;
	}

	public static final BatchCollectionSerializer forReport(SubReportVO sub) {

		String idb = sub.getIdBatch();
		BatchCollectionSerializer jcs = serializers.get(idb);

		return jcs;
	}

	public static final synchronized void deleteBatchTempFiles(String idBatch) {
		if (!ValidazioneDati.isFilled(idBatch))
			return;

		BatchCollectionSerializer jcs = serializers.get(idBatch);
		if (jcs == null)
			return;

		try {
			Iterator<Entry<String, FileReference>> i = jcs.files.entrySet().iterator();
			while (i.hasNext()) {
				FileReference ref = i.next().getValue();
				FileUtil.close(ref.getStream());
				try {
					ref.getFile().delete();
				} catch (Exception e) {}

				i.remove();
			}
		} finally {
			serializers.remove(idBatch);
		}
	}


	private BatchCollectionSerializer(ParametriRichiestaElaborazioneDifferitaVO params, String id) {
		this.idBatch = params.getIdBatch();
		this.id = id;
		try {
			this.firmaBatch = params.getFirmaBatch();
		} catch (ValidationException e) {
			this.firmaBatch = this.idBatch;
		}
	}

	/**
	 * Registra un tipo report per la scrittura su file
	 * @param type Identificativo del tipo report da inizializzare
	 * @return L'offset iniziale di scrittura sul file temporaneo
	 * @throws IOException
	 */
	public SubReportVO startReport(String type) throws IOException {
		RandomAccessFile f = getFileRef(type);
		long len = f.length();
		f.seek(len);
		return new SubReportVO(idBatch, id, type, len);
	}

	/**
	 * Scive su file il flag di fine report per il tipo report specificato
	 * @param type Identificativo del tipo report
	 * @throws IOException
	 */
	public void endReport(String type) throws IOException {
		RandomAccessFile f = getFileRef(type);
		f.writeInt(REPORT_STOP_SIGN);
	}

	public void seek(String type, long offset) throws IOException {
		RandomAccessFile f = getFileRef(type);
		f.seek(offset);
	}

	public void writeVO(String type, Serializable obj) throws IOException {

		baos.reset();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		FileUtil.close(oos);

		int len = baos.size();
		RandomAccessFile f = getFileRef(type);
		//scrive prima la dimensione dell'oggetto (4bytes)
		f.writeInt(len);
		//scrive oggetto serializzato
		f.write(baos.toByteArray(), 0, len);

		written++;

	}

	public Serializable readNextVO(String type) throws Exception {

		RandomAccessFile f = getFileRef(type);
		try {
			int len = f.readInt();
			if (len == REPORT_STOP_SIGN) // ho raggiunto la fine di una regione
				return null;

			byte[] buf = getBuffer(len);
			f.read(buf, 0, len);

			ByteArrayInputStream bais = new ByteArrayInputStream(buf);
			ObjectInputStream ois = new CustomObjectInputStream(bais, Thread.currentThread().getContextClassLoader());

			Serializable newInstance = (Serializable) ois.readObject();
			FileUtil.close(ois);

			read++;

			return newInstance;

		} catch (EOFException e) {
			return null;
		}
	}

	private RandomAccessFile getFileRef(String type) throws IOException {
		FileReference ref = files.get(type);
		if (ref == null) {
			String fileName = TMP_PATH + File.separator + "sbnweb_" + id + "_" + type + ".tmp";
			File file = new File(fileName);
			ref = new FileReference(file, new RandomAccessFile(file, "rw") );
			files.put(type, ref);
		}

		return ref.getStream();
	}

	private byte[] getBuffer(int len) {
		if (buffer.length < len)
			buffer = new byte[len];
		return buffer;
	}


	public String getId() {
		return id;
	}

	public long getRead() {
		return read;
	}

	public long getWritten() {
		return written;
	}

	public String getFirmaBatch() {
		return firmaBatch;
	}

	/*

	public void close() throws IOException {
		for (Entry<String, RandomAccessFile> e : files.entrySet())
			e.getValue().close();

	}

	public void close(String type) throws IOException {
		RandomAccessFile f = files.get(type);
		if (f != null)
			f.close();
	}

    public static void main(String[] args) throws Exception {

    	BatchCollectionSerializer bcs = BatchCollectionSerializer.forBatch("111");
    	SubReportVO start = bcs.startReport("COLLOCAZIONI");
    	System.out.println(start);

    	ComboVO c0 = new ComboVO("pippo", "pluto");
    	ComboCodDescVO c1 = new ComboCodDescVO("lallo", "lillo");
    	ComboSoloDescVO c2 = new ComboSoloDescVO();

    	bcs.writeVO("COLLOCAZIONI", c0);
    	bcs.writeVO("COLLOCAZIONI", c1);
    	bcs.writeVO("COLLOCAZIONI", c2);

    	bcs.seek("COLLOCAZIONI", 0);
    	Serializable o = null;
    	while ( (o = bcs.readNextVO("COLLOCAZIONI")) != null)
    		System.out.println(o);

    	bcs.startReport("INV");
    	bcs.writeVO("INV", c0);
    	bcs.writeVO("INV", c1);
    	bcs.writeVO("INV", c2);

    	bcs.seek("INV", 0);
    	while ( (o = bcs.readNextVO("INV")) != null)
    		System.out.println(o);

    	BatchCollectionSerializer.deleteBatchTempFiles("111");
    }

    */

}
