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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.File;

public class DownloadVO extends SerializableVO {

	private static final long serialVersionUID = -1764225996819204127L;
	private static final String[] SIZE_UNITS = new String[] {"B", "KB", "MB", "GB" };

	private final String nomeFileVisualizzato;
	private final String linkToDownload;
	private final String base64Link;

	public DownloadVO(String nomeFileVisualizzato, String linkToDownload) {
		super();
		this.nomeFileVisualizzato = nomeFileVisualizzato;
		this.linkToDownload = linkToDownload;
		this.base64Link = ValidazioneDati.dumpBytes(this.linkToDownload.getBytes());
	}

	public String getLinkToDownload() {
		return linkToDownload;
	}

	public String getNomeFileVisualizzato() {
		return nomeFileVisualizzato;
	}

	public String getBase64Link() {
		return base64Link;
	}

	public String getReadableSize() {
		try {
			//visualizza le dimensioni del file in formato leggibile
			String pathname =
				linkToDownload.substring(1).indexOf(File.separator) > -1 ?
					linkToDownload :
					StampeUtil.getBatchFilesPath() + File.separator + linkToDownload;

			File f = new File(pathname);
			double size = f.length();
			int mod = 1024;	//1Kb
			int u = 0;

			do {
				size /= mod;
				u++;
			} while (size >= mod);

		    return String.format("%01.2f %s", size, SIZE_UNITS[u]);

		} catch (Exception e) {
			return "--";
		}
	}


	@Override
	public int hashCode() {
		return base64Link.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DownloadVO other = (DownloadVO) obj;
		if (!base64Link.equals(other.base64Link))
			return false;

		return true;
	}

	public static void main(String[] args) {
		DownloadVO d = new DownloadVO("test", "/tmp/storico_unload.dmp");
		System.out.println(d.getBase64Link());
	}

}
