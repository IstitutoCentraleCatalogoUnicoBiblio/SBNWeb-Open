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
package it.iccu.sbn.web.actionforms.gestionestampe.semantica;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaSoggettarioForm extends ActionForm {

	private static final long serialVersionUID = 633989796631551711L;
	private String elemBlocco;
	private String codSogg;
	private String insDal;
	private String insAl;
	private String aggDal;
	private String aggAl;
	private String opzMan;
	private String opzColl;
	private String opzLegame;
	private String opzBibl;
	private String opzStringa;
	private String opzNote;
	private List listaCodSogg;
	private List listaOpzMan;
	private List listaOpzColl;
	private List listaOpzLegame;
	private List listaOpzBibl;
	private List listaOpzStringa;
	private List listaOpzNote;
	private String tipoRicerca;
	private boolean sessione;
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


	public String getOpzColl() {
		return opzColl;
	}
	public void setOpzColl(String opzColl) {
		this.opzColl = opzColl;
	}

	public String getOpzLegame() {
		return opzLegame;
	}
	public void setOpzLegame(String opzLegame) {
		this.opzLegame = opzLegame;
	}


	public String getOpzBibl() {
		return opzBibl;
	}
	public void setOpzBibl(String opzBibl) {
		this.opzBibl = opzBibl;
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



	public List getListaOpzColl() {
		return listaOpzColl;
	}
	public void setListaOpzColl(List listaOpzColl) {
		this.listaOpzColl = listaOpzColl;
	}

	public List getListaOpzLegame() {
		return listaOpzLegame;
	}
	public void setListaOpzLegame(List listaOpzLegame) {
		this.listaOpzLegame = listaOpzLegame;
	}


	public List getListaOpzBibl() {
		return listaOpzBibl;
	}
	public void setListaOpzBibl(List listaOpzBibl) {
		this.listaOpzBibl = listaOpzBibl;
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
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}


}
