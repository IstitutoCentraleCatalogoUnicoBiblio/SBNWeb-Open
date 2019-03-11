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

package it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

public class EsportaFromBibliograficaVO extends ParametriRichiestaElaborazioneDifferitaVO {

//	static final long serialVersionUID = EsportaVO.class.hashCode();
	String basePath;
	String downloadPath;
	String downloadLinkPath;

	private boolean fromPolo = false;
	private String[] listBid = null;

	/**
	 *
	 */
	private static final long serialVersionUID = 8793081975044011634L;

	private String biblioteca;
	public String getBiblioteca() {
		return biblioteca;
	}
	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public void validaParametriGener() throws ValidationException {
//		if (this.ricLocale == false && this.ricIndice == false) {
//			throw new ValidationException("livRicObblig", ValidationExceptionCodici.livRicObblig);
//		}
	}

	public void save() {
//		this.ricLocale_old = this.ricLocale;
//		this.ricIndice_old = this.ricIndice;
//
//		this.titoloPunt_old = this.titoloPunt;
//		this.matAntico_old = this.matAntico;
//		this.luogoPubblPunt_old = this.luogoPubblPunt;
//		this.nomeCollegatoPunt_old = this.nomeCollegatoPunt;
//		this.libretto_old = this.libretto;
	}

	public void restore()	{
//		this.ricLocale = this.ricLocale_old;
//		this.ricIndice = this.ricIndice_old;
//
//		this.titoloPunt = this.titoloPunt_old;
//		this.matAntico = this.matAntico_old;
//		this.luogoPubblPunt = this.luogoPubblPunt_old;
//		this.nomeCollegatoPunt = this.nomeCollegatoPunt_old;
//		this.libretto = this.libretto_old;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getDownloadLinkPath() {
		return downloadLinkPath;
	}
	public void setDownloadLinkPath(String downloadLinkPath) {
		this.downloadLinkPath = downloadLinkPath;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	/**
	 * @return Returns the fromPolo.
	 */
	public boolean isFromPolo() {
		return fromPolo;
	}
	/**
	 * @param fromPolo The fromPolo to set.
	 */
	public void setFromPolo(boolean fromPolo) {
		this.fromPolo = fromPolo;
	}
	/**
	 * @return Returns the listBid.
	 */
	public String[] getListBid() {
		return listBid;
	}
	/**
	 * @param listBid The listBid to set.
	 */
	public void setListBid(String[] listBid) {
		this.listBid = listBid;
	}

}
