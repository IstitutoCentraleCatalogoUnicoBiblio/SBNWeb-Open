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
package it.iccu.sbn.web.actionforms.statistiche;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class StatisticheForm extends ActionForm {

	private static final long serialVersionUID = -2207351977045946007L;
	private String codBibl="";
	private String queryInput="";

	private boolean sessione;
	private boolean visibilitaIndietroLS=false;
	private int elemXBlocchi=10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean rientroDaSif=false;
	private List listaQueryStr;
	private String queryStr;
	private String tipoQueryStr;

	private List<StrutturaTerna> listaCarattere;

	private List listaTipologieRiga;
	private String tipologiaRiga;

	private StrutturaQuater tipoRiga;


	private List listaTipoCarattereStr;
	private String tipoCarattereTit="ARIAL";
	private String tipoCarattereInt="ARIAL";
	private String tipoCarattereDat="ARIAL";

	private List listaStileCarattereStr;
	private String stileCarattereTit="BOLD";
	private String stileCarattereInt="BOLD";
	private String stileCarattereDat="NO_BOLD";


	private List listaDimensioneCarattereStr;
	private String dimensioneCarattereTit="14";
	private String dimensioneCarattereInt="12";
	private String dimensioneCarattereDat="10";

	private List listaSpessoreBordoStr;
	private String spessoreBordoTit="THIN";
	private String spessoreBordoInt="THIN";
	private String spessoreBordoDat="THIN";

	private List listaOrientamentoStr;
	private String orientamentoTit="HORIZONTAL";
	private String orientamentoInt="HORIZONTAL";
	private String orientamentoDat="HORIZONTAL";

	private List listaColorIntestazioniBckStr;
	private String colorIntestazioniBckTit="AUTOMATIC";
	private String colorIntestazioniBckInt="AUTOMATIC";
	private String colorIntestazioniBckDat="AUTOMATIC";

	private List listaColorDatiBckStr;
	private String colorDatiBckStr="VERY_LIGHT_YELLOW";

	private List listaBordiStr;
	private String bordiTit="ALL";
	private String bordiInt="ALL";
	private String bordiDat="ALL";

	private List listaAllineamentoStr;
	private String allineamentoTit="LEFT";
	private String allineamentoInt="JUSTIFY";
	private String allineamentoDat="JUSTIFY";

	private List listaOrientamentoPagina;
	private String orientamentoPagina="LANDSCAPE";


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


	public boolean isRientroDaSif() {
		return rientroDaSif;
	}


	public void setRientroDaSif(boolean rientroDaSif) {
		this.rientroDaSif = rientroDaSif;
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


	public String getQueryInput() {
		return queryInput;
	}


	public void setQueryInput(String queryInput) {
		this.queryInput = queryInput;
	}





	public String getColorDatiBckStr() {
		return colorDatiBckStr;
	}


	public void setColorDatiBckStr(String colorDatiBckStr) {
		this.colorDatiBckStr = colorDatiBckStr;
	}





	public List<StrutturaTerna> getListaCarattere() {
		return listaCarattere;
	}


	public void setListaCarattere(List<StrutturaTerna> listaCarattere) {
		this.listaCarattere = listaCarattere;
	}





	public List getListaQueryStr() {
		return listaQueryStr;
	}


	public void setListaQueryStr(List listaQueryStr) {
		this.listaQueryStr = listaQueryStr;
	}


	public List getListaTipologieRiga() {
		return listaTipologieRiga;
	}


	public void setListaTipologieRiga(List listaTipologieRiga) {
		this.listaTipologieRiga = listaTipologieRiga;
	}




	public String getQueryStr() {
		return queryStr;
	}


	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}




	public String getTipologiaRiga() {
		return tipologiaRiga;
	}


	public void setTipologiaRiga(String tipologiaRiga) {
		this.tipologiaRiga = tipologiaRiga;
	}


	public StrutturaQuater getTipoRiga() {
		return tipoRiga;
	}


	public void setTipoRiga(StrutturaQuater tipoRiga) {
		this.tipoRiga = tipoRiga;
	}


	public List getListaDimensioneCarattereStr() {
		return listaDimensioneCarattereStr;
	}


	public void setListaDimensioneCarattereStr(List listaDimensioneCarattereStr) {
		this.listaDimensioneCarattereStr = listaDimensioneCarattereStr;
	}


	public List getListaStileCarattereStr() {
		return listaStileCarattereStr;
	}


	public void setListaStileCarattereStr(List listaStileCarattereStr) {
		this.listaStileCarattereStr = listaStileCarattereStr;
	}


	public List getListaTipoCarattereStr() {
		return listaTipoCarattereStr;
	}


	public void setListaTipoCarattereStr(List listaTipoCarattereStr) {
		this.listaTipoCarattereStr = listaTipoCarattereStr;
	}


	public List getListaAllineamentoStr() {
		return listaAllineamentoStr;
	}


	public void setListaAllineamentoStr(List listaAllineamentoStr) {
		this.listaAllineamentoStr = listaAllineamentoStr;
	}


	public List getListaBordiStr() {
		return listaBordiStr;
	}


	public void setListaBordiStr(List listaBordiStr) {
		this.listaBordiStr = listaBordiStr;
	}


	public List getListaColorDatiBckStr() {
		return listaColorDatiBckStr;
	}


	public void setListaColorDatiBckStr(List listaColorDatiBckStr) {
		this.listaColorDatiBckStr = listaColorDatiBckStr;
	}


	public List getListaColorIntestazioniBckStr() {
		return listaColorIntestazioniBckStr;
	}


	public void setListaColorIntestazioniBckStr(
			List listaColorIntestazioniBckStr) {
		this.listaColorIntestazioniBckStr = listaColorIntestazioniBckStr;
	}


	public List getListaOrientamentoStr() {
		return listaOrientamentoStr;
	}


	public void setListaOrientamentoStr(List listaOrientamentoStr) {
		this.listaOrientamentoStr = listaOrientamentoStr;
	}


	public List getListaSpessoreBordoStr() {
		return listaSpessoreBordoStr;
	}


	public void setListaSpessoreBordoStr(List listaSpessoreBordoStr) {
		this.listaSpessoreBordoStr = listaSpessoreBordoStr;
	}


	public String getAllineamentoDat() {
		return allineamentoDat;
	}


	public void setAllineamentoDat(String allineamentoDat) {
		this.allineamentoDat = allineamentoDat;
	}


	public String getAllineamentoInt() {
		return allineamentoInt;
	}


	public void setAllineamentoInt(String allineamentoInt) {
		this.allineamentoInt = allineamentoInt;
	}


	public String getAllineamentoTit() {
		return allineamentoTit;
	}


	public void setAllineamentoTit(String allineamentoTit) {
		this.allineamentoTit = allineamentoTit;
	}


	public String getBordiDat() {
		return bordiDat;
	}


	public void setBordiDat(String bordiDat) {
		this.bordiDat = bordiDat;
	}


	public String getBordiInt() {
		return bordiInt;
	}


	public void setBordiInt(String bordiInt) {
		this.bordiInt = bordiInt;
	}


	public String getBordiTit() {
		return bordiTit;
	}


	public void setBordiTit(String bordiTit) {
		this.bordiTit = bordiTit;
	}


	public String getColorIntestazioniBckDat() {
		return colorIntestazioniBckDat;
	}


	public void setColorIntestazioniBckDat(String colorIntestazioniBckDat) {
		this.colorIntestazioniBckDat = colorIntestazioniBckDat;
	}


	public String getColorIntestazioniBckInt() {
		return colorIntestazioniBckInt;
	}


	public void setColorIntestazioniBckInt(String colorIntestazioniBckInt) {
		this.colorIntestazioniBckInt = colorIntestazioniBckInt;
	}


	public String getColorIntestazioniBckTit() {
		return colorIntestazioniBckTit;
	}


	public void setColorIntestazioniBckTit(String colorIntestazioniBckTit) {
		this.colorIntestazioniBckTit = colorIntestazioniBckTit;
	}


	public String getDimensioneCarattereDat() {
		return dimensioneCarattereDat;
	}


	public void setDimensioneCarattereDat(String dimensioneCarattereDat) {
		this.dimensioneCarattereDat = dimensioneCarattereDat;
	}


	public String getDimensioneCarattereInt() {
		return dimensioneCarattereInt;
	}


	public void setDimensioneCarattereInt(String dimensioneCarattereInt) {
		this.dimensioneCarattereInt = dimensioneCarattereInt;
	}


	public String getDimensioneCarattereTit() {
		return dimensioneCarattereTit;
	}


	public void setDimensioneCarattereTit(String dimensioneCarattereTit) {
		this.dimensioneCarattereTit = dimensioneCarattereTit;
	}


	public String getOrientamentoDat() {
		return orientamentoDat;
	}


	public void setOrientamentoDat(String orientamentoDat) {
		this.orientamentoDat = orientamentoDat;
	}


	public String getOrientamentoInt() {
		return orientamentoInt;
	}


	public void setOrientamentoInt(String orientamentoInt) {
		this.orientamentoInt = orientamentoInt;
	}


	public String getOrientamentoTit() {
		return orientamentoTit;
	}


	public void setOrientamentoTit(String orientamentoTit) {
		this.orientamentoTit = orientamentoTit;
	}


	public String getSpessoreBordoDat() {
		return spessoreBordoDat;
	}


	public void setSpessoreBordoDat(String spessoreBordoDat) {
		this.spessoreBordoDat = spessoreBordoDat;
	}


	public String getSpessoreBordoInt() {
		return spessoreBordoInt;
	}


	public void setSpessoreBordoInt(String spessoreBordoInt) {
		this.spessoreBordoInt = spessoreBordoInt;
	}


	public String getSpessoreBordoTit() {
		return spessoreBordoTit;
	}


	public void setSpessoreBordoTit(String spessoreBordoTit) {
		this.spessoreBordoTit = spessoreBordoTit;
	}


	public String getStileCarattereDat() {
		return stileCarattereDat;
	}


	public void setStileCarattereDat(String stileCarattereDat) {
		this.stileCarattereDat = stileCarattereDat;
	}


	public String getStileCarattereInt() {
		return stileCarattereInt;
	}


	public void setStileCarattereInt(String stileCarattereInt) {
		this.stileCarattereInt = stileCarattereInt;
	}


	public String getStileCarattereTit() {
		return stileCarattereTit;
	}


	public void setStileCarattereTit(String stileCarattereTit) {
		this.stileCarattereTit = stileCarattereTit;
	}


	public String getTipoCarattereDat() {
		return tipoCarattereDat;
	}


	public void setTipoCarattereDat(String tipoCarattereDat) {
		this.tipoCarattereDat = tipoCarattereDat;
	}


	public String getTipoCarattereInt() {
		return tipoCarattereInt;
	}


	public void setTipoCarattereInt(String tipoCarattereInt) {
		this.tipoCarattereInt = tipoCarattereInt;
	}


	public String getTipoCarattereTit() {
		return tipoCarattereTit;
	}


	public void setTipoCarattereTit(String tipoCarattereTit) {
		this.tipoCarattereTit = tipoCarattereTit;
	}


	public List getListaOrientamentoPagina() {
		return listaOrientamentoPagina;
	}


	public void setListaOrientamentoPagina(List listaOrientamentoPagina) {
		this.listaOrientamentoPagina = listaOrientamentoPagina;
	}


	public String getOrientamentoPagina() {
		return orientamentoPagina;
	}


	public void setOrientamentoPagina(String orientamentoPagina) {
		this.orientamentoPagina = orientamentoPagina;
	}


	public String getTipoQueryStr() {
		return tipoQueryStr;
	}


	public void setTipoQueryStr(String tipoQueryStr) {
		this.tipoQueryStr = tipoQueryStr;
	}


}
