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

public class StampaDescrittoriSoggettoForm extends ActionForm {

	private static final long serialVersionUID = 7818407817721765930L;
	private String elemBlocco;
	private String codSogg;
	private String insDal;
	private String insAl;
	private String aggDal;
	private String aggAl;
	private String opz;
	private String opzMan;
	private String opzLoc;
	private String opzColl;
	private String opzRelzn;
	private String opzBibl;
	private String opzForme;
	private String opzStringa;
	private String opzNote;
	private List listaCodSogg;
	private List listaOpzMan;
	private List listaOpzLoc;
	private List listaOpzColl;
	private List listaOpzRelzn;
	private List listaOpzBibl;
	private List listaOpzForme;
	private List listaOpzStringa;
	private List listaOpzSmpThe;
	private List listaOpzNote;
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

	public String getCodSogg() {
		return codSogg;
	}
	public void setCodSogg(String codSogg) {
		this.codSogg = codSogg;
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


	public String getOpzMan() {
		return opzMan;
	}
	public void setOpzMan(String opzMan) {
		this.opzMan = opzMan;
	}


	public String getOpzLoc() {
		return opzLoc;
	}
	public void setOpzLoc(String opzLoc) {
		this.opzLoc = opzLoc;
	}

	public String getOpzColl() {
		return opzColl;
	}
	public void setOpzColl(String opzColl) {
		this.opzColl = opzColl;
	}

	public String getOpzRelzn() {
		return opzRelzn;
	}
	public void setOpzRelzn(String opzRelzn) {
		this.opzRelzn = opzRelzn;
	}


	public String getOpzBibl() {
		return opzBibl;
	}
	public void setOpzBibl(String opzBibl) {
		this.opzBibl = opzBibl;
	}

	public String getOpzForme() {
		return opzForme;
	}
	public void setOpzForme(String opzForme) {
		this.opzForme = opzForme;
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
		this.opzNote= opzNote;
	}



	public List getListaCodSogg() {
		return listaCodSogg;
	}
	public void setListaCodSogg(List listaCodSogg) {
		this.listaCodSogg = listaCodSogg;
	}

	public List getListaOpzMan() {
		return listaOpzMan;
	}
	public void setListaOpzMan(List listaOpzMan) {
		this.listaOpzMan = listaOpzMan;
	}

	public List getListaOpzLoc() {
		return listaOpzLoc;
	}
	public void setListaOpzLoc(List listaOpzLoc) {
		this.listaOpzLoc = listaOpzLoc;
	}

	public List getListaOpzColl() {
		return listaOpzColl;
	}
	public void setListaOpzColl(List listaOpzColl) {
		this.listaOpzColl = listaOpzColl;
	}

	public List getListaOpzRelzn() {
		return listaOpzRelzn;
	}
	public void setListaOpzRelzn(List listaOpzRelzn) {
		this.listaOpzRelzn = listaOpzRelzn;
	}


	public List getListaOpzBibl() {
		return listaOpzBibl;
	}
	public void setListaOpzBibl(List listaOpzBibl) {
		this.listaOpzBibl = listaOpzBibl;
	}

	public List getListaOpzForme() {
		return listaOpzForme;
	}
	public void setListaOpzForme(List listaOpzForme) {
		this.listaOpzForme = listaOpzForme;
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


	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}


}
