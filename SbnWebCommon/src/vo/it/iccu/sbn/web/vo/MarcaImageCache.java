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
package it.iccu.sbn.web.vo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class MarcaImageCache extends SerializableVO {

	private static final long serialVersionUID = 7061372478027170530L;
	private static Logger log = Logger.getLogger(MarcaImageCache.class);
	private Map<String, List<ImageEntry>> immagini;

	private final class ImageEntry extends SerializableVO {

		private static final long serialVersionUID = -6162746326707171695L;
		private final int hashCode;
		private final byte[] image;

		private final int byteArrayHashCode(byte[] contents) {
			int result = 0;
			int pos = 0;
			for (byte b : contents) {
				result += (b << pos);
				pos = (pos + 8) % 32;
			}
			return result;
		}

		ImageEntry(byte[] image) {
			this.hashCode = byteArrayHashCode(image);
			this.image = image;
		}

		public byte[] getImage() {
			return image;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ImageEntry other = (ImageEntry) obj;
			if (hashCode != other.hashCode)
				return false;
			return true;
		}

	}

	public MarcaImageCache() {
		this.immagini = new HashMap<String, List<ImageEntry>>();
		log.info("Creata cache per immagini marche");
	}

	public void addImages(String mid, List<byte[]> images) {
		if (!immagini.containsKey(mid)) {
			List<ImageEntry> tmp = new ArrayList<ImageEntry>();
			for (byte[] img : images)
				tmp.add(new ImageEntry(img));
			immagini.put(mid, tmp);

		} else {

			List<ImageEntry> lista = immagini.get(mid);
			for (byte[] img : images) {
				ImageEntry entry = new ImageEntry(img);
				if (!lista.contains(entry))
					lista.add(entry);
			}
		}
	}

	public void clearImages(String mid) {
		immagini.remove(mid);
	}

	public void clearAll() {
		immagini.clear();
		log.info("Pulizia cache immagini marche");
	}

	public int addImage(String mid, byte[] image) {

		if (image == null || image.length < 1)
			return -1;

		if (!immagini.containsKey(mid)) {
			List<ImageEntry> lista = new ArrayList<ImageEntry>();
			lista.add(new ImageEntry(image));
			immagini.put(mid, lista);
			return 0;
		} else {
			List<ImageEntry> lista = immagini.get(mid);
			ImageEntry entry = new ImageEntry(image);
			if (!lista.contains(entry))
				lista.add(entry);
			return lista.indexOf(entry);
		}
	}

	public int getImageCount(String mid) {
		if (!immagini.containsKey(mid))
			return 0;

		List<ImageEntry> lista = immagini.get(mid);
		return lista.size();
	}

	public void removeImage(String mid, int index) {
		if (!immagini.containsKey(mid))
			return;
		List<ImageEntry> lista = immagini.get(mid);
		if (index < 0 || index > lista.size() - 1) return;

		lista.remove(index);
	}

	public byte[] getImage(String mid, int index) {
		if (!immagini.containsKey(mid))
			return null;
		List<ImageEntry> lista = immagini.get(mid);
		if (index < 0 || index > lista.size() - 1) return null;
		return lista.get(index).getImage();
	}

}
