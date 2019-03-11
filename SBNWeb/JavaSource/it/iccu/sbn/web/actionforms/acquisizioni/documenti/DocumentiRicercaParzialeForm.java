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
package it.iccu.sbn.web.actionforms.acquisizioni.documenti;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class DocumentiRicercaParzialeForm extends ActionForm {

	private static final long serialVersionUID = 7790567833947106328L;
	private String codBibl="";
	private List listaCodBibl;
	private String codUtenteBibl;
	private String codUtenteProg;
	private String codDoc;
	private String titoloDoc;
	private String dataInizio;
	private String dataFine;
	private String statoSuggerimento;
	private List listaStatoSuggerimento;
	private boolean LSRicerca=false;

	private boolean sessione;
	private boolean visibilitaIndietroLS=false;
	private int elemXBlocchi=10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean rientroDaSif=false;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodDoc() {
		return codDoc;
	}

	public void setCodDoc(String codDoc) {
		this.codDoc = codDoc;
	}

	public String getCodUtenteBibl() {
		return codUtenteBibl;
	}

	public void setCodUtenteBibl(String codUtenteBibl) {
		this.codUtenteBibl = codUtenteBibl;
	}

	public String getCodUtenteProg() {
		return codUtenteProg;
	}

	public void setCodUtenteProg(String codUtenteProg) {
		this.codUtenteProg = codUtenteProg;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public List getListaCodBibl() {
		return listaCodBibl;
	}

	public void setListaCodBibl(List listaCodBibl) {
		this.listaCodBibl = listaCodBibl;
	}

	public List getListaStatoSuggerimento() {
		return listaStatoSuggerimento;
	}

	public void setListaStatoSuggerimento(List listaStatoSuggerimento) {
		this.listaStatoSuggerimento = listaStatoSuggerimento;
	}

	public String getStatoSuggerimento() {
		return statoSuggerimento;
	}

	public void setStatoSuggerimento(String statoSuggerimento) {
		this.statoSuggerimento = statoSuggerimento;
	}

	public String getTitoloDoc() {
		return titoloDoc;
	}

	public void setTitoloDoc(String titoloDoc) {
		this.titoloDoc = titoloDoc;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public List getListaTipiOrdinam() {
		return listaTipiOrdinam;
	}

	public void setListaTipiOrdinam(List listaTipiOrdinam) {
		this.listaTipiOrdinam = listaTipiOrdinam;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public boolean isLSRicerca() {
		return LSRicerca;
	}

	public void setLSRicerca(boolean ricerca) {
		LSRicerca = ricerca;
	}

	public boolean isRientroDaSif() {
		return rientroDaSif;
	}

	public void setRientroDaSif(boolean rientroDaSif) {
		this.rientroDaSif = rientroDaSif;
	}
}
