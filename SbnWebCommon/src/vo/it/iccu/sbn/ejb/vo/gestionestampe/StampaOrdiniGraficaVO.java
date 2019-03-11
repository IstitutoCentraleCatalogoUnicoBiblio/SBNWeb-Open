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
package it.iccu.sbn.ejb.vo.gestionestampe;

import java.util.List;

public class StampaOrdiniGraficaVO {

	private List listaDesignSpecMater;
	private String designSpecMaterSelez;

	private List listaSupportoPrimario;
	private String supportoPrimarioSelez;

	private List listaIndicatoreColore;
	private String indicatoreColoreSelez;

	private List listaIndicatoreTecnica;
	private String indicatoreTecnicaSelez1;
	private String indicatoreTecnicaSelez2;
	private String indicatoreTecnicaSelez3;

	private List listaDesignatoreFunzione;
	private String designatoreFunzioneSelez;
	public String getDesignatoreFunzioneSelez() {
		return designatoreFunzioneSelez;
	}
	public void setDesignatoreFunzioneSelez(String designatoreFunzioneSelez) {
		this.designatoreFunzioneSelez = designatoreFunzioneSelez;
	}
	public String getDesignSpecMaterSelez() {
		return designSpecMaterSelez;
	}
	public void setDesignSpecMaterSelez(String designSpecMaterSelez) {
		this.designSpecMaterSelez = designSpecMaterSelez;
	}
	public String getIndicatoreColoreSelez() {
		return indicatoreColoreSelez;
	}
	public void setIndicatoreColoreSelez(String indicatoreColoreSelez) {
		this.indicatoreColoreSelez = indicatoreColoreSelez;
	}
	public String getIndicatoreTecnicaSelez1() {
		return indicatoreTecnicaSelez1;
	}
	public void setIndicatoreTecnicaSelez1(String indicatoreTecnicaSelez1) {
		this.indicatoreTecnicaSelez1 = indicatoreTecnicaSelez1;
	}
	public String getIndicatoreTecnicaSelez2() {
		return indicatoreTecnicaSelez2;
	}
	public void setIndicatoreTecnicaSelez2(String indicatoreTecnicaSelez2) {
		this.indicatoreTecnicaSelez2 = indicatoreTecnicaSelez2;
	}
	public String getIndicatoreTecnicaSelez3() {
		return indicatoreTecnicaSelez3;
	}
	public void setIndicatoreTecnicaSelez3(String indicatoreTecnicaSelez3) {
		this.indicatoreTecnicaSelez3 = indicatoreTecnicaSelez3;
	}
	public List getListaDesignatoreFunzione() {
		return listaDesignatoreFunzione;
	}
	public void setListaDesignatoreFunzione(List listaDesignatoreFunzione) {
		this.listaDesignatoreFunzione = listaDesignatoreFunzione;
	}
	public List getListaDesignSpecMater() {
		return listaDesignSpecMater;
	}
	public void setListaDesignSpecMater(List listaDesignSpecMater) {
		this.listaDesignSpecMater = listaDesignSpecMater;
	}
	public List getListaIndicatoreColore() {
		return listaIndicatoreColore;
	}
	public void setListaIndicatoreColore(List listaIndicatoreColore) {
		this.listaIndicatoreColore = listaIndicatoreColore;
	}
	public List getListaIndicatoreTecnica() {
		return listaIndicatoreTecnica;
	}
	public void setListaIndicatoreTecnica(List listaIndicatoreTecnica) {
		this.listaIndicatoreTecnica = listaIndicatoreTecnica;
	}
	public List getListaSupportoPrimario() {
		return listaSupportoPrimario;
	}
	public void setListaSupportoPrimario(List listaSupportoPrimario) {
		this.listaSupportoPrimario = listaSupportoPrimario;
	}
	public String getSupportoPrimarioSelez() {
		return supportoPrimarioSelez;
	}
	public void setSupportoPrimarioSelez(String supportoPrimarioSelez) {
		this.supportoPrimarioSelez = supportoPrimarioSelez;
	}

}
