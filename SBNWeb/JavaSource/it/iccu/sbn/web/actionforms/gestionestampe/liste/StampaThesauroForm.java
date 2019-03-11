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

public class StampaThesauroForm extends ActionForm {

	private static final long serialVersionUID = 94097065357565265L;
	private String elemBlocco;
	private String codThe;
	private String insDal;
	private String insAl;
	private String aggDal;
	private String aggAl;
	private String opzTit;
	private String opzNoteThe;
	private String opzStringa;
	private String opzNote;
	private String opzBibl;
	private String opzRlzn;
	private String opzForme;
	private String opzColl;
	private List listaCodThe;
	private List listaOpzTit;
	private List listaOpzNoteThe;
	private List listaOpzStringa;
	private List listaOpzNote;
	private List listaOpzBibl;
	private List listaOpzRlzn;
	private List listaOpzForme;
	private List listaOpzColl;
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
	public String getOpzTit() {
		return opzTit;
	}
	public void setOpzTit(String opzTit) {
		this.opzTit = opzTit;
	}
	public String getOpzNoteThe() {
		return opzNoteThe;
	}
	public void setOpzNoteThe(String opzNoteThe) {
		this.opzNoteThe = opzNoteThe;
	}
	public String getOpzStringa() {
		return opzStringa;
	}
	public void setOpzStringa(String opzStringa) {
		this.opzStringa = opzStringa;
	}
	public String getOpzNote() {
		return opzNote;
	}
	public void setOpzNote(String opzNote) {
		this.opzNote = opzNote;
	}
	public String getOpzBibl() {
		return opzBibl;
	}
	public void setOpzBibl(String opzBibl) {
		this.opzBibl = opzBibl;
	}

	public String getOpzRlzn() {
		return opzRlzn;
	}
	public void setOpzRlzn(String opzRlzn) {
		this.opzRlzn = opzRlzn;
	}
	public String getOpzForme() {
		return opzForme;
	}
	public void setOpzForme(String opzForme) {
		this.opzForme = opzForme;
	}
	public String getOpzColl() {
		return opzColl;
	}
	public void setOpzColl(String opzColl) {
		this.opzColl = opzColl;
	}
	public List getListaCodThe() {
		return listaCodThe;
	}
	public void setListaCodThe(List listaCodThe) {
		this.listaCodThe = listaCodThe;
	}
	public List getListaOpzTit() {
		return listaOpzTit;
	}
	public void setListaOpzTit(List listaOpzTit) {
		this.listaOpzTit = listaOpzTit;
	}
	public List getListaOpzNoteThe() {
		return listaOpzNoteThe;
	}
	public void setListaOpzNoteThe(List listaOpzNoteThe) {
		this.listaOpzNoteThe = listaOpzNoteThe;
	}
	public List getListaOpzStringa() {
		return listaOpzStringa;
	}
	public void setListaOpzStringa(List listaOpzStringa) {
		this.listaOpzStringa = listaOpzStringa;
	}
	public List getListaOpzNote() {
		return listaOpzNote;
	}
	public void setListaOpzNote(List listaOpzNote) {
		this.listaOpzNote = listaOpzNote;
	}
	public List getListaOpzBibl() {
		return listaOpzBibl;
	}
	public void setListaOpzBibl(List listaOpzBibl) {
		this.listaOpzBibl = listaOpzBibl;
	}
	public List getListaOpzRlzn() {
		return listaOpzRlzn;
	}
	public void setListaOpzRlzn(List listaOpzRlzn) {
		this.listaOpzRlzn = listaOpzRlzn;
	}
	public List getListaOpzForme() {
		return listaOpzForme;
	}
	public void setListaOpzForme(List listaOpzForme) {
		this.listaOpzForme = listaOpzForme;
	}
	public List getListaOpzColl() {
		return listaOpzColl;
	}
	public void setListaOpzColl(List listaOpzColl) {
		this.listaOpzColl = listaOpzColl;
	}
	public String getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}


}
