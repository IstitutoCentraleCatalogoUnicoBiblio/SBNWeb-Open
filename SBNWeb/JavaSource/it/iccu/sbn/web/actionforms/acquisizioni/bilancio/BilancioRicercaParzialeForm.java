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
package it.iccu.sbn.web.actionforms.acquisizioni.bilancio;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class BilancioRicercaParzialeForm extends ActionForm {


	private static final long serialVersionUID = 8351483857401590034L;
	private String codBibl="";;
	private String esercizio;
	private String capitolo;
	private boolean sessione;
	private boolean visibilitaIndietroLS=false;
	private int elemXBlocchi=10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean LSRicerca=false;

	private List listaTipoImpegno;
	private String tipoImpegno="";
	private boolean gestBil=true;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}



	public String getCapitolo() {
		return capitolo;
	}



	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}



	public String getCodBibl() {
		return codBibl;
	}



	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}



	public String getEsercizio() {
		return esercizio;
	}



	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}



	public boolean isSessione() {
		return sessione;
	}



	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}



	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}



	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
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



	public boolean isLSRicerca() {
		return LSRicerca;
	}



	public void setLSRicerca(boolean ricerca) {
		LSRicerca = ricerca;
	}



	public List getListaTipoImpegno() {
		return listaTipoImpegno;
	}



	public void setListaTipoImpegno(List listaTipoImpegno) {
		this.listaTipoImpegno = listaTipoImpegno;
	}



	public String getTipoImpegno() {
		return tipoImpegno;
	}



	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}



	public boolean isGestBil() {
		return gestBil;
	}



	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}






}
