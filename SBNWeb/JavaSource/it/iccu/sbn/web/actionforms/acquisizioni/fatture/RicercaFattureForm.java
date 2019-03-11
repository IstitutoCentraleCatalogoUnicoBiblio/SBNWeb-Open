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
package it.iccu.sbn.web.actionforms.acquisizioni.fatture;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class RicercaFattureForm extends ActionForm {

/**
	 * 
	 */
	private static final long serialVersionUID = -8462449633685847571L;
private String numFatt;
private String statoFatt;
List listaStatoFatt;
private String dataFattDa;
private String dataFattA;
private String progrFatt;
private String fornitore;

private String tipoFatt;
List listaTipoFatt;

private String tipoOrdine;
private List listaTipoOrdine;

private String annoOrd;
private String numOrd;


	public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

	public String getDataFattA() {
		return dataFattA;
	}

	public void setDataFattA(String dataFattA) {
		this.dataFattA = dataFattA;
	}

	public String getDataFattDa() {
		return dataFattDa;
	}

	public void setDataFattDa(String dataFattDa) {
		this.dataFattDa = dataFattDa;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public List getListaStatoFatt() {
		return listaStatoFatt;
	}

	public void setListaStatoFatt(List listaStatoFatt) {
		this.listaStatoFatt = listaStatoFatt;
	}

	public List getListaTipoFatt() {
		return listaTipoFatt;
	}

	public void setListaTipoFatt(List listaTipoFatt) {
		this.listaTipoFatt = listaTipoFatt;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

	public String getNumFatt() {
		return numFatt;
	}

	public void setNumFatt(String numFatt) {
		this.numFatt = numFatt;
	}

	public String getProgrFatt() {
		return progrFatt;
	}

	public void setProgrFatt(String progrFatt) {
		this.progrFatt = progrFatt;
	}

	public String getStatoFatt() {
		return statoFatt;
	}

	public void setStatoFatt(String statoFatt) {
		this.statoFatt = statoFatt;
	}

	public String getTipoFatt() {
		return tipoFatt;
	}

	public void setTipoFatt(String tipoFatt) {
		this.tipoFatt = tipoFatt;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}



}
