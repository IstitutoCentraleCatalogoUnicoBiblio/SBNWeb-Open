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
package it.iccu.sbn.web.actionforms.acquisizioni.fornitori;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class FornitoriRicercaParzialeForm extends ActionForm {


	private static final long serialVersionUID = -4953861020772257033L;

	private String codBibl = "";

	private String codForn;
	private String nomeForn;

	private String tipoForn;
	private List listaTipoForn;
	private boolean LSRicerca = false;

	private String provinciaForn;
	private List listaProvinciaForn;

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Nuovi campi per la gestione degli editori
	private String editore;
	private String creazLegameTitEdit;
	private String cartiglioEditore;
	private String bid;
	private String descr;
	private String isbnEditore;
	private String regioneForn;
	private List listaRegioneForn;
	private String presenzaTastoCrea;

	private String paeseForn;
	private List listaPaeseForn;

	private String profAcqForn;
	private String profAcqFornDes;
	private List listaProfAcqForn;

	private boolean sessione;
	private boolean visibilitaIndietroLS = false;
	private boolean ricercaLocale = true;
	private String ricercaLocaleStr;

	private int elemXBlocchi = 10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean disabilitaTutto = false;
	private boolean rientroDaSif = false;

	private String tipoRicerca;

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.ricercaLocaleStr = "";
	}

	public String getCodForn() {
		return codForn;
	}

	public void setCodForn(String codForn) {
		this.codForn = codForn;
	}

	public List getListaPaeseForn() {
		return listaPaeseForn;
	}

	public void setListaPaeseForn(List listaPaeseForn) {
		this.listaPaeseForn = listaPaeseForn;
	}

	public List getListaProfAcqForn() {
		return listaProfAcqForn;
	}

	public void setListaProfAcqForn(List listaProfAcqForn) {
		this.listaProfAcqForn = listaProfAcqForn;
	}

	public List getListaProvinciaForn() {
		return listaProvinciaForn;
	}

	public void setListaProvinciaForn(List listaProvinciaForn) {
		this.listaProvinciaForn = listaProvinciaForn;
	}

	public List getListaTipoForn() {
		return listaTipoForn;
	}

	public void setListaTipoForn(List listaTipoForn) {
		this.listaTipoForn = listaTipoForn;
	}

	public String getNomeForn() {
		return nomeForn;
	}

	public void setNomeForn(String nomeForn) {
		this.nomeForn = nomeForn;
	}

	public String getPaeseForn() {
		return paeseForn;
	}

	public void setPaeseForn(String paeseForn) {
		this.paeseForn = paeseForn;
	}

	public String getProfAcqForn() {
		return profAcqForn;
	}

	public void setProfAcqForn(String profAcqForn) {
		this.profAcqForn = profAcqForn;
	}

	public String getProvinciaForn() {
		return provinciaForn;
	}

	public void setProvinciaForn(String provinciaForn) {
		this.provinciaForn = provinciaForn;
	}

	public String getTipoForn() {
		return tipoForn;
	}

	public void setTipoForn(String tipoForn) {
		this.tipoForn = tipoForn;
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

	public boolean isRicercaLocale() {
		return ricercaLocale;
	}

	public void setRicercaLocale(boolean ricercaLocale) {
		this.ricercaLocale = ricercaLocale;
	}

	public String getProfAcqFornDes() {
		return profAcqFornDes;
	}

	public void setProfAcqFornDes(String profAcqFornDes) {
		this.profAcqFornDes = profAcqFornDes;
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

	public String getRicercaLocaleStr() {
		return ricercaLocaleStr;
	}

	public void setRicercaLocaleStr(String ricercaLocaleStr) {
		this.ricercaLocaleStr = ricercaLocaleStr;
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

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public boolean isRientroDaSif() {
		return rientroDaSif;
	}

	public void setRientroDaSif(boolean rientroDaSif) {
		this.rientroDaSif = rientroDaSif;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getRegioneForn() {
		return regioneForn;
	}

	public void setRegioneForn(String regioneForn) {
		this.regioneForn = regioneForn;
	}

	public List getListaRegioneForn() {
		return listaRegioneForn;
	}

	public void setListaRegioneForn(List listaRegioneForn) {
		this.listaRegioneForn = listaRegioneForn;
	}

	public String getIsbnEditore() {
		return isbnEditore;
	}

	public void setIsbnEditore(String isbnEditore) {
		this.isbnEditore = isbnEditore;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getCreazLegameTitEdit() {
		return creazLegameTitEdit;
	}

	public void setCreazLegameTitEdit(String creazLegameTitEdit) {
		this.creazLegameTitEdit = creazLegameTitEdit;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getCartiglioEditore() {
		return cartiglioEditore;
	}

	public void setCartiglioEditore(String cartiglioEditore) {
		this.cartiglioEditore = cartiglioEditore;
	}

	public String getPresenzaTastoCrea() {
		return presenzaTastoCrea;
	}

	public void setPresenzaTastoCrea(String presenzaTastoCrea) {
		this.presenzaTastoCrea = presenzaTastoCrea;
	}

}
