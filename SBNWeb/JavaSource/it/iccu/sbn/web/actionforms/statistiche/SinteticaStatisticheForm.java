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

import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class SinteticaStatisticheForm extends ActionForm {


	private static final long serialVersionUID = -2097641973802224153L;
	int bloccoCorrente;
	private boolean abilitaBlocchi;
    private int maxRighe = 200000;
    private int totRighe;
    private String idLista = "";

    private int totBlocchi;
	private String selezRadio;

	private String ordinamento;
	private String folder;
	private boolean disable;
	private String descrArea;


	//tab1
	private List<StatisticaVO> elencoStatistiche = new ArrayList<StatisticaVO>();

	private String abilitaScrittura = "";
	//
	//tab2
	private ParametriExcelVO paramExcel = new ParametriExcelVO();
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

	private List<StrutturaTerna> listaCarattere;

	private List listaTipologieRiga;
	private String tipologiaRiga;

	private StrutturaQuater tipoRiga;
	//

	public String getAbilitaScrittura() {
		return abilitaScrittura;
	}

	public void setAbilitaScrittura(String abilitaScrittura) {
		this.abilitaScrittura = abilitaScrittura;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getBloccoCorrente() {
		return bloccoCorrente;
	}

	public void setBloccoCorrente(int bloccoCorrente) {
		this.bloccoCorrente = bloccoCorrente;
	}

	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public List<StatisticaVO> getElencoStatistiche() {
		return elencoStatistiche;
	}

	public void setElencoStatistiche(List<StatisticaVO> elencoStatistiche) {
		this.elencoStatistiche = elencoStatistiche;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public List getListaTipoCarattereStr() {
		return listaTipoCarattereStr;
	}

	public void setListaTipoCarattereStr(List listaTipoCarattereStr) {
		this.listaTipoCarattereStr = listaTipoCarattereStr;
	}

	public String getTipoCarattereTit() {
		return tipoCarattereTit;
	}

	public void setTipoCarattereTit(String tipoCarattereTit) {
		this.tipoCarattereTit = tipoCarattereTit;
	}

	public String getTipoCarattereInt() {
		return tipoCarattereInt;
	}

	public void setTipoCarattereInt(String tipoCarattereInt) {
		this.tipoCarattereInt = tipoCarattereInt;
	}

	public String getTipoCarattereDat() {
		return tipoCarattereDat;
	}

	public void setTipoCarattereDat(String tipoCarattereDat) {
		this.tipoCarattereDat = tipoCarattereDat;
	}

	public List getListaStileCarattereStr() {
		return listaStileCarattereStr;
	}

	public void setListaStileCarattereStr(List listaStileCarattereStr) {
		this.listaStileCarattereStr = listaStileCarattereStr;
	}

	public String getStileCarattereTit() {
		return stileCarattereTit;
	}

	public void setStileCarattereTit(String stileCarattereTit) {
		this.stileCarattereTit = stileCarattereTit;
	}

	public String getStileCarattereInt() {
		return stileCarattereInt;
	}

	public void setStileCarattereInt(String stileCarattereInt) {
		this.stileCarattereInt = stileCarattereInt;
	}

	public String getStileCarattereDat() {
		return stileCarattereDat;
	}

	public void setStileCarattereDat(String stileCarattereDat) {
		this.stileCarattereDat = stileCarattereDat;
	}

	public List getListaDimensioneCarattereStr() {
		return listaDimensioneCarattereStr;
	}

	public void setListaDimensioneCarattereStr(List listaDimensioneCarattereStr) {
		this.listaDimensioneCarattereStr = listaDimensioneCarattereStr;
	}

	public String getDimensioneCarattereTit() {
		return dimensioneCarattereTit;
	}

	public void setDimensioneCarattereTit(String dimensioneCarattereTit) {
		this.dimensioneCarattereTit = dimensioneCarattereTit;
	}

	public String getDimensioneCarattereInt() {
		return dimensioneCarattereInt;
	}

	public void setDimensioneCarattereInt(String dimensioneCarattereInt) {
		this.dimensioneCarattereInt = dimensioneCarattereInt;
	}

	public String getDimensioneCarattereDat() {
		return dimensioneCarattereDat;
	}

	public void setDimensioneCarattereDat(String dimensioneCarattereDat) {
		this.dimensioneCarattereDat = dimensioneCarattereDat;
	}

	public List getListaSpessoreBordoStr() {
		return listaSpessoreBordoStr;
	}

	public void setListaSpessoreBordoStr(List listaSpessoreBordoStr) {
		this.listaSpessoreBordoStr = listaSpessoreBordoStr;
	}

	public String getSpessoreBordoTit() {
		return spessoreBordoTit;
	}

	public void setSpessoreBordoTit(String spessoreBordoTit) {
		this.spessoreBordoTit = spessoreBordoTit;
	}

	public String getSpessoreBordoInt() {
		return spessoreBordoInt;
	}

	public void setSpessoreBordoInt(String spessoreBordoInt) {
		this.spessoreBordoInt = spessoreBordoInt;
	}

	public String getSpessoreBordoDat() {
		return spessoreBordoDat;
	}

	public void setSpessoreBordoDat(String spessoreBordoDat) {
		this.spessoreBordoDat = spessoreBordoDat;
	}

	public List getListaOrientamentoStr() {
		return listaOrientamentoStr;
	}

	public void setListaOrientamentoStr(List listaOrientamentoStr) {
		this.listaOrientamentoStr = listaOrientamentoStr;
	}

	public String getOrientamentoTit() {
		return orientamentoTit;
	}

	public void setOrientamentoTit(String orientamentoTit) {
		this.orientamentoTit = orientamentoTit;
	}

	public String getOrientamentoInt() {
		return orientamentoInt;
	}

	public void setOrientamentoInt(String orientamentoInt) {
		this.orientamentoInt = orientamentoInt;
	}

	public String getOrientamentoDat() {
		return orientamentoDat;
	}

	public void setOrientamentoDat(String orientamentoDat) {
		this.orientamentoDat = orientamentoDat;
	}

	public List getListaColorIntestazioniBckStr() {
		return listaColorIntestazioniBckStr;
	}

	public void setListaColorIntestazioniBckStr(
			List listaColorIntestazioniBckStr) {
		this.listaColorIntestazioniBckStr = listaColorIntestazioniBckStr;
	}

	public String getColorIntestazioniBckTit() {
		return colorIntestazioniBckTit;
	}

	public void setColorIntestazioniBckTit(String colorIntestazioniBckTit) {
		this.colorIntestazioniBckTit = colorIntestazioniBckTit;
	}

	public String getColorIntestazioniBckInt() {
		return colorIntestazioniBckInt;
	}

	public void setColorIntestazioniBckInt(String colorIntestazioniBckInt) {
		this.colorIntestazioniBckInt = colorIntestazioniBckInt;
	}

	public String getColorIntestazioniBckDat() {
		return colorIntestazioniBckDat;
	}

	public void setColorIntestazioniBckDat(String colorIntestazioniBckDat) {
		this.colorIntestazioniBckDat = colorIntestazioniBckDat;
	}

	public List getListaColorDatiBckStr() {
		return listaColorDatiBckStr;
	}

	public void setListaColorDatiBckStr(List listaColorDatiBckStr) {
		this.listaColorDatiBckStr = listaColorDatiBckStr;
	}

	public String getColorDatiBckStr() {
		return colorDatiBckStr;
	}

	public void setColorDatiBckStr(String colorDatiBckStr) {
		this.colorDatiBckStr = colorDatiBckStr;
	}

	public List getListaBordiStr() {
		return listaBordiStr;
	}

	public void setListaBordiStr(List listaBordiStr) {
		this.listaBordiStr = listaBordiStr;
	}

	public String getBordiTit() {
		return bordiTit;
	}

	public void setBordiTit(String bordiTit) {
		this.bordiTit = bordiTit;
	}

	public String getBordiInt() {
		return bordiInt;
	}

	public void setBordiInt(String bordiInt) {
		this.bordiInt = bordiInt;
	}

	public String getBordiDat() {
		return bordiDat;
	}

	public void setBordiDat(String bordiDat) {
		this.bordiDat = bordiDat;
	}

	public List getListaAllineamentoStr() {
		return listaAllineamentoStr;
	}

	public void setListaAllineamentoStr(List listaAllineamentoStr) {
		this.listaAllineamentoStr = listaAllineamentoStr;
	}

	public String getAllineamentoTit() {
		return allineamentoTit;
	}

	public void setAllineamentoTit(String allineamentoTit) {
		this.allineamentoTit = allineamentoTit;
	}

	public String getAllineamentoInt() {
		return allineamentoInt;
	}

	public void setAllineamentoInt(String allineamentoInt) {
		this.allineamentoInt = allineamentoInt;
	}

	public String getAllineamentoDat() {
		return allineamentoDat;
	}

	public void setAllineamentoDat(String allineamentoDat) {
		this.allineamentoDat = allineamentoDat;
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

	public List<StrutturaTerna> getListaCarattere() {
		return listaCarattere;
	}

	public void setListaCarattere(List<StrutturaTerna> listaCarattere) {
		this.listaCarattere = listaCarattere;
	}

	public List getListaTipologieRiga() {
		return listaTipologieRiga;
	}

	public void setListaTipologieRiga(List listaTipologieRiga) {
		this.listaTipologieRiga = listaTipologieRiga;
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

	public ParametriExcelVO getParamExcel() {
		return paramExcel;
	}

	public void setParamExcel(ParametriExcelVO paramExcel) {
		this.paramExcel = paramExcel;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getDescrArea() {
		return descrArea;
	}

	public void setDescrArea(String descrArea) {
		this.descrArea = descrArea;
	}

}
