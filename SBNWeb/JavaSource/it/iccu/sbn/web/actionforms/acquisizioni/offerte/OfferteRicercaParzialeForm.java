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
package it.iccu.sbn.web.actionforms.acquisizioni.offerte;

import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class OfferteRicercaParzialeForm extends ActionForm {

	private static final long serialVersionUID = -3342909653678084156L;
	private String codBibl="";
	private String codOfferta;
	private boolean LSRicerca=false;

	private String titolo;
	private StrutturaCombo fornitore;
	private String desForn;
	private String autore;
	private String classificazione;
	private String offFornitore;

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


	public String getAutore() {
		return autore;
	}


	public void setAutore(String autore) {
		this.autore = autore;
	}


	public String getClassificazione() {
		return classificazione;
	}


	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}


	public String getCodBibl() {
		return codBibl;
	}


	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}



	public String getDesForn() {
		return desForn;
	}


	public void setDesForn(String desForn) {
		this.desForn = desForn;
	}


	public String getOffFornitore() {
		return offFornitore;
	}


	public void setOffFornitore(String offFornitore) {
		this.offFornitore = offFornitore;
	}


	public String getTitolo() {
		return titolo;
	}


	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}


	public int getElemXBlocchi() {
		return elemXBlocchi;
	}


	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}


	public StrutturaCombo getFornitore() {
		return fornitore;
	}


	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
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


	public String getCodOfferta() {
		return codOfferta;
	}


	public void setCodOfferta(String codOfferta) {
		this.codOfferta = codOfferta;
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
