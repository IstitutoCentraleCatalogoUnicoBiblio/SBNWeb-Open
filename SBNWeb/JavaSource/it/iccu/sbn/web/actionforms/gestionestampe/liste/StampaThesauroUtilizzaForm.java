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

public class StampaThesauroUtilizzaForm extends ActionForm {

	private static final long serialVersionUID = -8940034674699851811L;
	private String elemBlocco;
	private String codThe;
	private String insDal;
	private String insAl;
	private String aggDal;
	private String aggAl;
	private String opz;
	private String opzSmpTit;
	private String opzSmpNoteTit;
	private String opzSmpStringa;
	private String opzSmpNote;
	private String opzSmpThe;
	private List listaCodThe;
	private List listaOpzSmpTit;
	private List listaOpzSmpNoteTit;
	private List listaOpzSmpStringa;
	private List listaOpzSmpNote;
	private List listaOpzSmpThe;
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
	public String getCodThe() {
		return codThe;
	}
	public void setCodThe(String codThe) {
		this.codThe = codThe;
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
	public String getOpzSmpTit() {
		return opzSmpTit;
	}
	public void setOpzSmpTit(String opzSmpTit) {
		this.opzSmpTit = opzSmpTit;
	}
	public String getOpzSmpNoteTit() {
		return opzSmpNoteTit;
	}
	public void setOpzSmpNoteTit(String opzSmpNoteTit) {
		this.opzSmpNoteTit = opzSmpNoteTit;
	}
	public String getOpzSmpStringa() {
		return opzSmpStringa;
	}
	public void setOpzSmpStringa(String opzSmpStringa) {
		this.opzSmpStringa = opzSmpStringa;
	}
	public String getOpzSmpNote() {
		return opzSmpNote;
	}
	public void setOpzSmpNote(String opzSmpNote) {
		this.opzSmpNote = opzSmpNote;
	}
	public String getOpzSmpThe() {
		return opzSmpThe;
	}
	public void setOpzSmpThe(String opzSmpThe) {
		this.opzSmpThe = opzSmpThe;
	}
	public List getListaCodThe() {
		return listaCodThe;
	}
	public void setListaCodThe(List listaCodThe) {
		this.listaCodThe = listaCodThe;
	}
	public List getListaOpzSmpTit() {
		return listaOpzSmpTit;
	}
	public void setListaOpzSmpTit(List listaOpzSmpTit) {
		this.listaOpzSmpTit = listaOpzSmpTit;
	}
	public List getListaOpzSmpNoteTit() {
		return listaOpzSmpNoteTit;
	}
	public void setListaOpzSmpNoteTit(List listaOpzSmpNoteTit) {
		this.listaOpzSmpNoteTit = listaOpzSmpNoteTit;
	}
	public List getListaOpzSmpStringa() {
		return listaOpzSmpStringa;
	}
	public void setListaOpzSmpStringa(List listaOpzSmpStringa) {
		this.listaOpzSmpStringa = listaOpzSmpStringa;
	}
	public List getListaOpzSmpNote() {
		return listaOpzSmpNote;
	}
	public void setListaOpzSmpNote(List listaOpzSmpNote) {
		this.listaOpzSmpNote = listaOpzSmpNote;
	}
	public List getListaOpzSmpThe() {
		return listaOpzSmpThe;
	}
	public void setListaOpzSmpThe(List listaOpzSmpThe) {
		this.listaOpzSmpThe = listaOpzSmpThe;
	}
	public String getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}


}
