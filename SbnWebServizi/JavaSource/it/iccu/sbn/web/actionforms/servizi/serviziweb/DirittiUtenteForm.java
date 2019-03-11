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
package it.iccu.sbn.web.actionforms.servizi.serviziweb;

import it.iccu.sbn.ejb.vo.servizi.serviziweb.ElencoDirittiUtenteVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class DirittiUtenteForm extends ActionForm {

	private static final long serialVersionUID = 6252829573761394058L;

	private String servizi;
	private String scadenza;
	private String sospesoDal;
	private String sospesoAl;
	private ElencoDirittiUtenteVO elencoDiritti;
	private List listaDir;
	private String utenteCon;
	private String biblioSel;
	private String ambiente;

	public List getListaDir() {
		return listaDir;
	}
	public void setListaDir(List listaDir) {
		this.listaDir = listaDir;
	}
	public ElencoDirittiUtenteVO getElencoDiritti() {
		return elencoDiritti;
	}
	public void setElencoDiritti(ElencoDirittiUtenteVO elencoDiritti) {
		this.elencoDiritti = elencoDiritti;
	}
	public String getScadenza() {
		return scadenza;
	}
	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}
	public String getServizi() {
		return servizi;
	}
	public void setServizi(String servizi) {
		this.servizi = servizi;
	}
	public String getSospesoAl() {
		return sospesoAl;
	}
	public void setSospesoAl(String sospesoAl) {
		this.sospesoAl = sospesoAl;
	}
	public String getSospesoDal() {
		return sospesoDal;
	}
	public void setSospesoDal(String sospesoDal) {
		this.sospesoDal = sospesoDal;
	}
	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}
	public String getUtenteCon() {
		return utenteCon;
	}
	public void setBiblioSel(String biblioSel) {
		this.biblioSel = biblioSel;
	}
	public String getBiblioSel() {
		return biblioSel;
	}
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}
	public String getAmbiente() {
		return ambiente;
	}



}
