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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.ArrayList;
import java.util.List;

public class ParametriExcelVO extends SerializableVO {

	private static final long serialVersionUID = 274951516661445155L;
	private int elemXBlocchi;
	private String area;
	private String query;
	private String suffisso;
	private Object queryObject;
	private String tipoQuery="H";
	private String nomeFile;
	private List<String> titoli= new ArrayList<String>();
//	private List<StrutturaCombo> intestazioniCol=new ArrayList<StrutturaCombo> () ; // nome colonna, tipo dato
//	private List risultati=new ArrayList();
	private List<StrutturaTerna> totaliCol= new ArrayList<StrutturaTerna> (); // nome totale, tipo dato, dato, num. colonna

	private String tipoCarattereTit="ARIAL";
	private String tipoCarattereInt="ARIAL";
	private String tipoCarattereDat="ARIAL";

	private String stileCarattereTit="BOLD";
	private String stileCarattereInt="BOLD";
	private String stileCarattereDat="NO_BOLD";


	private String dimensioneCarattereTit="14";
	private String dimensioneCarattereInt="12";
	private String dimensioneCarattereDat="10";

	private String spessoreBordoTit="THIN";
	private String spessoreBordoInt="THIN";
	private String spessoreBordoDat="THIN";

	private String orientamentoTit="HORIZONTAL";
	private String orientamentoInt="HORIZONTAL";
	private String orientamentoDat="HORIZONTAL";

	private String colorIntestazioniBckTit="AUTOMATIC";
	private String colorIntestazioniBckInt="AUTOMATIC";
	private String colorIntestazioniBckDat="AUTOMATIC";

	private String colorDatiBckStr="VERY_LIGHT_YELLOW";

	private String bordiTit="ALL";
	private String bordiInt="ALL";
	private String bordiDat="ALL";

	private String allineamentoTit="LEFT";
	private String allineamentoInt="JUSTIFY";
	private String allineamentoDat="JUSTIFY";

	private String orientamentoPagina="LANDSCAPE";

	private String[] parametriQueryHQL = null;
	private List<StrutturaTerna> paramQueryHQL = new ArrayList<StrutturaTerna>();


	private String querySintassi = null;
	private int idREc;




	public ParametriExcelVO (){}


	public int getElemXBlocchi() {
		return elemXBlocchi;
	}


	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}


	public String getNomeFile() {
		return nomeFile;
	}


	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public List<String> getTitoli() {
		return titoli;
	}


	public void setTitoli(List<String> titoli) {
		this.titoli = titoli;
	}


	public List<StrutturaTerna> getTotaliCol() {
		return totaliCol;
	}


	public void setTotaliCol(List<StrutturaTerna> totaliCol) {
		this.totaliCol = totaliCol;
	}


	public Object getQueryObject() {
		return queryObject;
	}


	public void setQueryObject(Object queryObject) {
		this.queryObject = queryObject;
	}


	public String getTipoQuery() {
		return tipoQuery;
	}


	public void setTipoQuery(String tipoQuery) {
		this.tipoQuery = tipoQuery;
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


	public String getColorDatiBckStr() {
		return colorDatiBckStr;
	}


	public void setColorDatiBckStr(String colorDatiBckStr) {
		this.colorDatiBckStr = colorDatiBckStr;
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


	public String getOrientamentoPagina() {
		return orientamentoPagina;
	}


	public void setOrientamentoPagina(String orientamentoPagina) {
		this.orientamentoPagina = orientamentoPagina;
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


	public String getSuffisso() {
		return suffisso;
	}


	public void setSuffisso(String suffisso) {
		this.suffisso = suffisso;
	}


	public int getIdREc() {
		return idREc;
	}


	public void setIdREc(int idREc) {
		this.idREc = idREc;
	}


	public String[] getParametriQueryHQL() {
		return parametriQueryHQL;
	}


	public void setParametriQueryHQL(String[] parametriQueryHQL) {
		this.parametriQueryHQL = parametriQueryHQL;
	}


	public String getQuerySintassi() {
		return querySintassi;
	}


	public void setQuerySintassi(String querySintassi) {
		this.querySintassi = querySintassi;
	}


	public List<StrutturaTerna> getParamQueryHQL() {
		return paramQueryHQL;
	}


	public void setParamQueryHQL(List<StrutturaTerna> paramQueryHQL) {
		this.paramQueryHQL = paramQueryHQL;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	};
}
