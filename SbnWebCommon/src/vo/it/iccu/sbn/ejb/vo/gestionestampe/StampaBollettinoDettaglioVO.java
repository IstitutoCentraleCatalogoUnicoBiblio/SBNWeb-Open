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

import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.List;

public class StampaBollettinoDettaglioVO extends StampaBollettinoVO{

	/**
	 *
	 */
	private static final long serialVersionUID = -8692006759933883200L;

	public StampaBollettinoDettaglioVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	// Attributes
	private String bid;
	private String isbd;
	private String natura;
	private String descrCatFrui;
	private String inventario;
	private String collocazione;
	private List<CodiceVO> soggetti = new ArrayList();
	private List<CodiceVO> indiciClassificazione = new ArrayList();
	private String ordinaPer;

	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getIsbd() {
		return isbd;
	}
	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}
	public String getDescrCatFrui() {
		return descrCatFrui;
	}
	public void setDescrCatFrui(String descrCatFrui) {
		this.descrCatFrui = descrCatFrui;
	}
	public String getCollocazione() {
		return collocazione;
	}
	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}
	public String getInventario() {
		return inventario;
	}
	public void setInventario(String inventario) {
		this.inventario = inventario;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}
	public String getOrdinaPer() {
		return ordinaPer;
	}
	public void setOrdinaPer(String ordinaPer) {
		this.ordinaPer = ordinaPer;
	}
	public List<CodiceVO> getSoggetti() {
		return soggetti;
	}
	public void setSoggetti(List<CodiceVO> soggetti) {
		this.soggetti = soggetti;
	}
	public List<CodiceVO> getIndiciClassificazione() {
		return indiciClassificazione;
	}
	public void setIndiciClassificazione(List<CodiceVO> indiciClassificazione) {
		this.indiciClassificazione = indiciClassificazione;
	}

}
