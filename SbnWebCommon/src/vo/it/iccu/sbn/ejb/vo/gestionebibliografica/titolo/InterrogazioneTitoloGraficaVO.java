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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

public class InterrogazioneTitoloGraficaVO   extends SerializableVO {

	// = InterrogazioneTitoloGraficaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -4014868536913894185L;
	private List listaDesignSpecMater;
	private String designSpecMaterSelez;

	private List listaSupportoPrimario;
	private String supportoPrimarioSelez;

	private List listaIndicatoreColore;
	private String indicatoreColoreSelez;

	private List listaIndicatoreTecnicaDisegno;
	private String indicatoreTecnicaSelez1Disegno;
	private String indicatoreTecnicaSelez2Disegno;
	private String indicatoreTecnicaSelez3Disegno;

	private List listaIndicatoreTecnicaGrafica;
	private String indicatoreTecnicaSelez1Grafica;
	private String indicatoreTecnicaSelez2Grafica;
	private String indicatoreTecnicaSelez3Grafica;

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
	public String getIndicatoreTecnicaSelez1Disegno() {
		return indicatoreTecnicaSelez1Disegno;
	}
	public void setIndicatoreTecnicaSelez1Disegno(
			String indicatoreTecnicaSelez1Disegno) {
		this.indicatoreTecnicaSelez1Disegno = indicatoreTecnicaSelez1Disegno;
	}
	public String getIndicatoreTecnicaSelez1Grafica() {
		return indicatoreTecnicaSelez1Grafica;
	}
	public void setIndicatoreTecnicaSelez1Grafica(
			String indicatoreTecnicaSelez1Grafica) {
		this.indicatoreTecnicaSelez1Grafica = indicatoreTecnicaSelez1Grafica;
	}
	public String getIndicatoreTecnicaSelez2Disegno() {
		return indicatoreTecnicaSelez2Disegno;
	}
	public void setIndicatoreTecnicaSelez2Disegno(
			String indicatoreTecnicaSelez2Disegno) {
		this.indicatoreTecnicaSelez2Disegno = indicatoreTecnicaSelez2Disegno;
	}
	public String getIndicatoreTecnicaSelez2Grafica() {
		return indicatoreTecnicaSelez2Grafica;
	}
	public void setIndicatoreTecnicaSelez2Grafica(
			String indicatoreTecnicaSelez2Grafica) {
		this.indicatoreTecnicaSelez2Grafica = indicatoreTecnicaSelez2Grafica;
	}
	public String getIndicatoreTecnicaSelez3Disegno() {
		return indicatoreTecnicaSelez3Disegno;
	}
	public void setIndicatoreTecnicaSelez3Disegno(
			String indicatoreTecnicaSelez3Disegno) {
		this.indicatoreTecnicaSelez3Disegno = indicatoreTecnicaSelez3Disegno;
	}
	public String getIndicatoreTecnicaSelez3Grafica() {
		return indicatoreTecnicaSelez3Grafica;
	}
	public void setIndicatoreTecnicaSelez3Grafica(
			String indicatoreTecnicaSelez3Grafica) {
		this.indicatoreTecnicaSelez3Grafica = indicatoreTecnicaSelez3Grafica;
	}
	public List getListaIndicatoreTecnicaDisegno() {
		return listaIndicatoreTecnicaDisegno;
	}
	public void setListaIndicatoreTecnicaDisegno(
			List listaIndicatoreTecnicaDisegno) {
		this.listaIndicatoreTecnicaDisegno = listaIndicatoreTecnicaDisegno;
	}
	public List getListaIndicatoreTecnicaGrafica() {
		return listaIndicatoreTecnicaGrafica;
	}
	public void setListaIndicatoreTecnicaGrafica(
			List listaIndicatoreTecnicaGrafica) {
		this.listaIndicatoreTecnicaGrafica = listaIndicatoreTecnicaGrafica;
	}

}
