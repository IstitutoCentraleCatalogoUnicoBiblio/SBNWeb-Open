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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

public class RichiestaOpacVO extends SerializableVO {

	private static final long serialVersionUID = -7405611198584565833L;

	public enum TipoRichiesta {
		RICHIESTA_ILL,
		INVENTARIO,
		SALA;
	}

	private String codPoloOpac;
	private String codBibOpac;
	private String denBibOpac;
	private String codSerieOpac;
	private int codInventOpac;
	private String segnatureOpac;
	private List listaBiblioOpac;
	// dati del servizio richiesto
	private String servizio;
	private String descrServizio;
	private String codServizio;
	// documento
	private String bibIscr;
	private String autore;
	private String titolo;
	private String anno;
	private String luogoEdizione;
	private String editore;
	private String natura;

	private String tipoDocLet;
	private String codDocLet;
	private String progrEsempDocLet;

	private TipoRichiesta tipo = TipoRichiesta.INVENTARIO;
	private String tipoMediazione;
	private InventarioVO inventario;

	//
	public void setCodPoloOpac(String codPoloOpac) {
		this.codPoloOpac = codPoloOpac;
	}

	public String getCodPoloOpac() {
		return codPoloOpac;
	}

	public void setCodBibOpac(String codBibOpac) {
		this.codBibOpac = codBibOpac;
	}

	public String getCodBibOpac() {
		return codBibOpac;
	}

	public boolean isCompleta() {
		switch (tipo) {
		case SALA:
			return isFilled(codBibOpac) && isFilled(tipoMediazione);

		case INVENTARIO:
		case RICHIESTA_ILL:
			return isFilled(codBibOpac) && length(codSerieOpac) > 0 && codInventOpac > 0;
		}

		return false;
	}

	public void setCodSerieOpac(String codSerieOpac) {
		this.codSerieOpac = codSerieOpac;
	}

	public String getCodSerieOpac() {
		return codSerieOpac;
	}

	public void setCodInventOpac(int codInventOpac) {
		this.codInventOpac = codInventOpac;
	}

	public int getCodInventOpac() {
		return codInventOpac;
	}

	public void setSegnatureOpac(String segnatureOpac) {
		this.segnatureOpac = segnatureOpac;
	}

	public String getSegnatureOpac() {
		return segnatureOpac;
	}

	public void setListaBiblioOpac(List listaBiblioOpac) {
		this.listaBiblioOpac = listaBiblioOpac;
	}

	public List getListaBiblioOpac() {
		return listaBiblioOpac;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getAutore() {
		return autore;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getAnno() {
		return anno;
	}

	public void setBibIscr(String bibIscr) {
		this.bibIscr = bibIscr;
	}

	public String getBibIscr() {
		return bibIscr;
	}

	public void setServizio(String strings) {
		this.servizio = strings;
	}

	public String getServizio() {
		return servizio;
	}

	public void setDescrServizio(String descrServizio) {
		this.descrServizio = descrServizio;
	}

	public String getDescrServizio() {
		return descrServizio;
	}

	public void setCodServizio(String codServizio) {
		this.codServizio = codServizio;
	}

	public String getCodServizio() {
		return codServizio;
	}

	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = luogoEdizione;
	}

	public String getLuogoEdizione() {
		return luogoEdizione;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getEditore() {
		return editore;
	}

	public void setNatura(String tipoDoc) {
		this.natura = tipoDoc;
	}

	public String getNatura() {
		return natura;
	}

	public void setCodDocLet(String codDocLet) {
		this.codDocLet = codDocLet;
	}

	public String getCodDocLet() {
		return codDocLet;
	}

	public void setProgrEsempDocLet(String progrEsempDocLet) {
		this.progrEsempDocLet = progrEsempDocLet;
	}

	public String getProgrEsempDocLet() {
		return progrEsempDocLet;
	}

	public void setDenBibOpac(String denBibOpac) {
		this.denBibOpac = denBibOpac;
	}

	public String getDenBibOpac() {
		return denBibOpac;
	}

	public String getTipoDocLet() {
		return tipoDocLet;
	}

	public void setTipoDocLet(String tipoDocLet) {
		this.tipoDocLet = tipoDocLet;
	}

	public TipoRichiesta getTipo() {
		return tipo;
	}

	public void setTipo(TipoRichiesta tipo) {
		this.tipo = tipo;
	}

	public String getTipoMediazione() {
		return tipoMediazione;
	}

	public void setTipoMediazione(String tipoMediazione) {
		this.tipoMediazione = tipoMediazione;
	}

	public InventarioVO getInventario() {
		return inventario;
	}

	public void setInventario(InventarioVO inventario) {
		this.inventario = inventario;
	}

}
