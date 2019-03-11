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
package it.iccu.sbn.web.actionforms.acquisizioni.profiliacquisto;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ProfiliRicercaParzialeForm extends ActionForm {

	private static final long serialVersionUID = 4563748580395041504L;
	private String codBibl="";
	private String codSezione;
	private String descrizioneSez;
	private String codProfilo;
	private String descrizioneProf;
	private boolean LSRicerca=false;

	private boolean sessione;

	private boolean visibilitaIndietroLS=false;

	private int elemXBlocchi=10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean disabilitaTutto=false;
	private boolean rientroDaSif=false;
	private boolean gestProf=true;



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


	public String getCodProfilo() {
		return codProfilo;
	}


	public void setCodProfilo(String codProfilo) {
		this.codProfilo = codProfilo;
	}


	public String getCodSezione() {
		return codSezione;
	}


	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}


	public String getDescrizioneProf() {
		return descrizioneProf;
	}


	public void setDescrizioneProf(String descrizioneProf) {
		this.descrizioneProf = descrizioneProf;
	}


	public String getDescrizioneSez() {
		return descrizioneSez;
	}


	public void setDescrizioneSez(String descrizioneSez) {
		this.descrizioneSez = descrizioneSez;
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


	public boolean isSessione() {
		return sessione;
	}


	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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


	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}


	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}


	public boolean isRientroDaSif() {
		return rientroDaSif;
	}


	public void setRientroDaSif(boolean rientroDaSif) {
		this.rientroDaSif = rientroDaSif;
	}


	public boolean isGestProf() {
		return gestProf;
	}


	public void setGestProf(boolean gestProf) {
		this.gestProf = gestProf;
	}





}
