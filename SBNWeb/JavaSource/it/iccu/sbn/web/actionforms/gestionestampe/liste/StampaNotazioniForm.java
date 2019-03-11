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
package it.iccu.sbn.web.actionforms.gestionestampe.liste;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaNotazioniForm extends ActionForm {

	private static final long serialVersionUID = 4816481348322923234L;
	private String elemBlocco;
	private String codCla;
	private String insDal;
	private String insAl;
	private String aggDal;
	private String aggAl;
	private String opz;
	private String opzColl;
	private String opzNotzn;
	private String opzNtznPrpr;
	private List listaCodCla;
	private List listaOpzColl;
	private List listaOpzNotzn;
	private List listaOpzNtznPrpr;
	private String tipoRicerca;
	private String tipoFormato;

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getElemBlocco() {
		return elemBlocco;
	}
	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}
	public String getCodCla() {
		return codCla;
	}
	public void setCodCla(String codCla) {
		this.codCla = codCla;
	}
	public String getInsDal() {
		return insDal;
	}
	public void setInsDal(String insDal) {
		this.insDal = insDal;
	}
	public String getInsAl() {
		return insAl;
	}
	public void setInsAl(String insAl) {
		this.insAl = insAl;
	}
	public String getAggDal() {
		return aggDal;
	}
	public void setAggDal(String aggDal) {
		this.aggDal = aggDal;
	}
	public String getAggAl() {
		return aggAl;
	}
	public void setAggAl(String aggAl) {
		this.aggAl = aggAl;
	}
	public String getOpzColl() {
		return opzColl;
	}
	public void setOpzColl(String opzColl) {
		this.opzColl = opzColl;
	}
	public String getOpzNotzn() {
		return opzNotzn;
	}
	public void setOpzNotzn(String opzNotzn) {
		this.opzNotzn = opzNotzn;
	}

	public String getOpzNtznPrpr() {
		return opzNtznPrpr;
	}
	public void setOpzNtznPrpr(String opzNtznPrpr) {
		this.opzNtznPrpr = opzNtznPrpr;
	}
	public List getListaCodCla() {
		return listaCodCla;
	}
	public void setListaCodCla(List listaCodCla) {
		this.listaCodCla = listaCodCla;
	}

	public List getListaOpzColl() {
		return listaOpzColl;
	}
	public void setListaOpzColl(List listaOpzColl) {
		this.listaOpzColl = listaOpzColl;
	}

	public List getListaOpzNotzn() {
		return listaOpzNotzn;
	}
	public void setListaOpzNotzn(List listaOpzNotzn) {
		this.listaOpzNotzn = listaOpzNotzn;
	}
	public List getListaOpzNtznPrpr() {
		return listaOpzNtznPrpr;
	}
	public void setListaOpzNtznPrpr(List listaOpzNtznPrpr) {
		this.listaOpzNtznPrpr = listaOpzNtznPrpr;
	}
	public String getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}


}
