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
package it.iccu.sbn.util.cloning;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.extension.cloning.ActualCloner;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.sf.dozer.util.mapping.DozerBeanMapper;

import org.apache.log4j.Logger;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class ClonePool {

	private static final DozerBeanMapper mapper = new DozerBeanMapper();
	private static final ClonePool instance;

	private static Logger log = Logger.getLogger(ClonePool.class);
	private BlockingQueue<ActualCloner> pool;

	private GsonBuilder gsonBuilder;

	static {
		instance = new ClonePool();
	}

	private ClonePool() {
		try {
			int poolSize = CommonConfiguration.getPropertyAsInteger(Configuration.CLONER_POOL_SIZE, 1);
			String nomeClasse = CommonConfiguration.getProperty(Configuration.CLONER_CLASS);

			Class<?> classe = this.getClass().getClassLoader().loadClass(nomeClasse);

			log.info("Creazione pool [CLONER_CLASS: " + nomeClasse + ", POOL_SIZE: " + poolSize + "]");
			this.pool = new ArrayBlockingQueue<ActualCloner>(poolSize);
			for (int p = 0; p < poolSize; p++) {
				ActualCloner cloner = (ActualCloner) classe.newInstance();
				if (cloner.init())
					this.pool.offer(cloner);
			}

			// almaviva5_20100728 inject dei custom mapping dozer
			List<String> maps = new ArrayList<String>();
			String xmlPath = this.getClass().getPackage().getName().replace('.', '/') + "/dozer-custom-mappings.xml";
			maps.add(xmlPath);
			mapper.setMappingFiles(maps);

			//almaviva5_20150715
			gsonBuilder = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {

				public boolean shouldSkipField(FieldAttributes f) {
					Class<?> clazz = f.getDeclaringClass();
					return clazz == UniqueIdentifiableVO.class &&
						ValidazioneDati.in(f.getName(), "uniqueId", "creationTime");
				}

				public boolean shouldSkipClass(Class<?> c) {
					return false;
				}
			});

		} catch (Exception e) {
			log.error("Errore creazione ClonePool: ", e);
		}
	}

	public static final <T> T deepCopy(T src) throws Exception {

		if (src == null)
			return null;

		ActualCloner cloner = instance.pool.take();
		if (cloner == null)
			throw new Exception("Errore clone pool");
		try {
			int size = instance.pool.size();
			if (log.isDebugEnabled()) {
				StringBuilder buf = new StringBuilder(256);
				buf.append("thread id ")
					.append(Thread.currentThread().getId())
					.append(" acquisisce cloner per ")
					.append(src.getClass().getSimpleName())
					.append('@')
					.append(Integer.toHexString(src.hashCode()))
					.append(" (")
					.append(size)
					.append(" disponibili)");

				log.debug(buf.toString());
			}

			if (size == 0)
				log.warn("ClonePool interamente occupato (0 cloner disponibili)");

			T copy = cloner.deepCopy(src);
			return copy;

		} catch (Exception e) {
			log.error("Errore serializzazione: ", e);
			return null;
		} finally {
			instance.pool.put(cloner);
		}

	}

	public static final boolean copyCommonProperties(Serializable dest,
			Serializable source) {
		try {
			mapper.map(source, dest);
			return true;

		} catch (Exception e) {
			log.error("Errore copyCommonProperties: ", e);
			return false;
		}
	}

	public static final String marshal(final Serializable target) throws Exception {
		if (target == null)
			return null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEncoder xml = new XMLEncoder(baos);
		xml.writeObject(target);
		xml.close();
		baos.close();

		return baos.toString("UTF-8");
	}

	public static final Serializable unmarshal(final String xml) throws Exception {

		if (!ValidazioneDati.isFilled(xml))
			return null;

		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
		XMLDecoder dec = new XMLDecoder(bais);
		Object output = dec.readObject();

		dec.close();
		bais.close();

		return (Serializable) output;
	}

	public static String toJson(Serializable src, Type type) {
		if (src == null)
			return null;

		Gson gson = instance.gsonBuilder.create();
		return type != null ? gson.toJson(src, type) : gson.toJson(src);
	}

	public static String toJson(Serializable src) {
		return toJson(src, null);
	}

	public static <T> T fromJson(String src, Class<T> type) {
		if (!ValidazioneDati.isFilled(src))
			return null;

		Gson gson = instance.gsonBuilder.create();
		return gson.fromJson(src, type);
	}

	public static <T> T fromJson(String src, Type type) {
		if (!ValidazioneDati.isFilled(src))
			return null;

		Gson gson = instance.gsonBuilder.create();
		return gson.fromJson(src, type);
	}

}
