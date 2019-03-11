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
package it.iccu.sbn.web.actionforms.acquisizioni.sezioni;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SezioniRicercaParzialeForm extends ActionForm {

	private static final long serialVersionUID = 3754142947025619477L;
	private String codBiblio = "";
	private List listaCodBiblio;
	private String codSezione;
	private String nomeSezione;
	private boolean sessione;
	private boolean visibilitaIndietroLS = false;
	private int elemXBlocchi = 10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean LSRicerca = false;
	private ListaSuppSezioneVO sifSezioni;
	private boolean gestSez = true;
	private boolean soloChiuse = false;
	private String soloChiuseStr;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.soloChiuseStr = "";
	}

	public String getCodBiblio() {
		return codBiblio;
	}

	public void setCodBiblio(String codBiblio) {
		this.codBiblio = codBiblio;
	}

	public String getCodSezione() {
		return codSezione;
	}

	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}

	public List getListaCodBiblio() {
		return listaCodBiblio;
	}

	public void setListaCodBiblio(List listaCodBiblio) {
		this.listaCodBiblio = listaCodBiblio;
	}

	public String getNomeSezione() {
		return nomeSezione;
	}

	public void setNomeSezione(String nomeSezione) {
		this.nomeSezione = nomeSezione;
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

	public ListaSuppSezioneVO getSifSezioni() {
		return sifSezioni;
	}

	public void setSifSezioni(ListaSuppSezioneVO sifSezioni) {
		this.sifSezioni = sifSezioni;
	}

	public boolean isGestSez() {
		return gestSez;
	}

	public void setGestSez(boolean gestSez) {
		this.gestSez = gestSez;
	}

	public boolean isSoloChiuse() {
		return soloChiuse;
	}

	public void setSoloChiuse(boolean soloChiuse) {
		this.soloChiuse = soloChiuse;
	}

	public String getSoloChiuseStr() {
		return soloChiuseStr;
	}

	public void setSoloChiuseStr(String soloChiuseStr) {
		this.soloChiuseStr = soloChiuseStr;
	}

}
