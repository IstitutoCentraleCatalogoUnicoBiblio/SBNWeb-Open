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

package it.iccu.sbn.ejb.vo.gestionebibliografica.altre;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

public class InterrogazioneLuogoGeneraleVO  extends SerializableVO {

	// = InterrogazioneLuogoGeneraleVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 957296083165039621L;
	private String denominazione;
	private String tipoRicerca;
	private String lid;

	private List listaPaese;
	private String paeseSelez;


	private int elemXBlocchi;
	private int numPrimo;
	private int maxRighe;
	private List listaTipiOrdinam;
	private String tipoOrdinamSelez;
	private List listaFormatoLista;
	private String formatoListaSelez;

	private boolean ricLocale;
	private boolean ricLocale_old;
	private boolean ricIndice;
	private boolean ricIndice_old;

	public boolean isRicIndice_old() {
		return ricIndice_old;
	}
	public void setRicIndice_old(boolean ricIndice_old) {
		this.ricIndice_old = ricIndice_old;
	}
	public boolean isRicLocale_old() {
		return ricLocale_old;
	}
	public void setRicLocale_old(boolean ricLocale_old) {
		this.ricLocale_old = ricLocale_old;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public int getElemXBlocchi() {
		return elemXBlocchi;
	}
	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}
	public String getFormatoListaSelez() {
		return formatoListaSelez;
	}
	public void setFormatoListaSelez(String formatoListaSelez) {
		this.formatoListaSelez = formatoListaSelez;
	}
	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public List getListaFormatoLista() {
		return listaFormatoLista;
	}
	public void setListaFormatoLista(List listaFormatoLista) {
		this.listaFormatoLista = listaFormatoLista;
	}
	public List getListaTipiOrdinam() {
		return listaTipiOrdinam;
	}
	public void setListaTipiOrdinam(List listaTipiOrdinam) {
		this.listaTipiOrdinam = listaTipiOrdinam;
	}
	public int getMaxRighe() {
		return maxRighe;
	}
	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}
	public int getNumPrimo() {
		return numPrimo;
	}
	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}
	public boolean isRicIndice() {
		return ricIndice;
	}
	public void setRicIndice(boolean ricIndice) {
		this.ricIndice = ricIndice;
	}
	public boolean isRicLocale() {
		return ricLocale;
	}
	public void setRicLocale(boolean ricLocale) {
		this.ricLocale = ricLocale;
	}
	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}
	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}
	public String getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public List getListaPaese() {
		return listaPaese;
	}
	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}
	public String getPaeseSelez() {
		return paeseSelez;
	}
	public void setPaeseSelez(String paeseSelez) {
		this.paeseSelez = paeseSelez;
	}

	public void save() {
		this.ricLocale_old = this.ricLocale;
		this.ricIndice_old = this.ricIndice;
	}

	public void restore()
	{
		this.ricLocale = this.ricLocale_old;
		this.ricIndice = this.ricIndice_old;
	}

}
