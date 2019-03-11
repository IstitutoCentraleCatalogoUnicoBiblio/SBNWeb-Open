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
package it.iccu.sbn.web.actionforms.gestionestampe.biblioteche;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaBibliotecheForm extends ActionForm {

	private static final long serialVersionUID = 3230526237375086627L;
	private String tipoFormato;
	private String tipoModello;
	private String elemBlocco;
	//private boolean biblioteca;
	private String biblioteca;
	private List listaTipoBiblioteca;
	private String tipoBiblioteca;
	private String nomeBiblioteca;
	private String enteDiAppartenenza;
	private List listaProvincia;
	private String provincia;
	private List listaPaese;
	private String paese;
	private String polo;
	private boolean sessione;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
//	public boolean isBiblioteca() {
//		return biblioteca;
//	}
//	public void setBiblioteca(boolean biblioteca) {
//		this.biblioteca = biblioteca;
//	}
	public String getElemBlocco() {
		return elemBlocco;
	}
	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}
	public String getEnteDiAppartenenza() {
		return enteDiAppartenenza;
	}
	public void setEnteDiAppartenenza(String enteDiAppartenenza) {
		this.enteDiAppartenenza = enteDiAppartenenza;
	}
	public List getListaPaese() {
		return listaPaese;
	}
	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}
	public List getListaProvincia() {
		return listaProvincia;
	}
	public void setListaProvincia(List listaProvincia) {
		this.listaProvincia = listaProvincia;
	}
	public List getListaTipoBiblioteca() {
		return listaTipoBiblioteca;
	}
	public void setListaTipoBiblioteca(List listaTipoBiblioteca) {
		this.listaTipoBiblioteca = listaTipoBiblioteca;
	}
	public String getNomeBiblioteca() {
		return nomeBiblioteca;
	}
	public void setNomeBiblioteca(String nomeBiblioteca) {
		this.nomeBiblioteca = nomeBiblioteca;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public String getPolo() {
		return polo;
	}
	public void setPolo(String polo) {
		this.polo = polo;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getTipoBiblioteca() {
		return tipoBiblioteca;
	}
	public void setTipoBiblioteca(String tipoBiblioteca) {
		this.tipoBiblioteca = tipoBiblioteca;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public List<ComboCodDescVO> getElencoModelli() {
		return elencoModelli;
	}
	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}
	public String getBiblioteca() {
		return biblioteca;
	}
	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}
}
