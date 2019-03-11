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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class TestElaborazioniDifferiteVo extends SerializableVO {

	private static final long serialVersionUID = 7807564842085414593L;

	String Biblioteca;
	String basePath;
	String downloadPath;
	String downloadLinkPath;

	String procedura;
	String coda;
	int intervalloMillisecondi;

	public int getIntervalloMillisecondi() {
		return intervalloMillisecondi;
	}
	public void setIntervalloMillisecondi(int intervalloMillisecondi) {
		this.intervalloMillisecondi = intervalloMillisecondi;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getBiblioteca() {
		return Biblioteca;
	}
	public void setBiblioteca(String biblioteca) {
		Biblioteca = biblioteca;
	}
	public String getCoda() {
		return coda;
	}
	public void setCoda(String coda) {
		this.coda = coda;
	}
	public String getProcedura() {
		return procedura;
	}
	public void setProcedura(String procedura) {
		this.procedura = procedura;
	}
	public String getDownloadLinkPath() {
		return downloadLinkPath;
	}
	public void setDownloadLinkPath(String downloadLinkPath) {
		this.downloadLinkPath = downloadLinkPath;
	}


}
