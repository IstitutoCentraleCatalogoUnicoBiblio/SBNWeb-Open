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

package it.iccu.sbn.ejb.vo.gestionebibliografica.marca;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

public class InterrogazioneMarcaGeneraleVO extends SerializableVO {

	// = InterrogazioneMarcaGeneraleVO.class.hashCode();


	/**
	 *
	 */
	private static final long serialVersionUID = 1409628556255183399L;
	private String descrizione;
	private String parolaChiave1;
	private String parolaChiave2;
	private String parolaChiave3;
	private String mid;
	private List listaCitazioneStandard;
	private String citazioneStandardSelez;
	private String siglaRepertorio;
	private String motto;

	private String dataInserimentoDa;
	private String dataInserimentoA;
	private String dataAggDa;
	private String dataAggA;

	private int elemXBlocchi;
	private int numPrimo;
	private int maxRighe;
	private List listaTipiOrdinam;
	private String tipoOrdinamSelez;

	private boolean ricLocale;
	private boolean ricLocale_old;
	private boolean ricIndice;
	private boolean ricIndice_old;


	public String getCitazioneStandardSelez() {
		return citazioneStandardSelez;
	}

	public void setCitazioneStandardSelez(String citazioneStandardSelez) {
		this.citazioneStandardSelez = citazioneStandardSelez;
	}

	public String getDataAggA() {
		return dataAggA;
	}

	public void setDataAggA(String dataAggA) {
		this.dataAggA = dataAggA;
	}

	public String getDataAggDa() {
		return dataAggDa;
	}

	public void setDataAggDa(String dataAggDa) {
		this.dataAggDa = dataAggDa;
	}

	public String getDataInserimentoA() {
		return dataInserimentoA;
	}

	public void setDataInserimentoA(String dataInserimentoA) {
		this.dataInserimentoA = dataInserimentoA;
	}

	public String getDataInserimentoDa() {
		return dataInserimentoDa;
	}

	public void setDataInserimentoDa(String dataInserimentoDa) {
		this.dataInserimentoDa = dataInserimentoDa;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public List getListaCitazioneStandard() {
		return listaCitazioneStandard;
	}

	public void setListaCitazioneStandard(List listaCitazioneStandard) {
		this.listaCitazioneStandard = listaCitazioneStandard;
	}

	public List getListaTipiOrdinam() {
		return listaTipiOrdinam;
	}

	public void setListaTipiOrdinam(List listaTipiOrdinam) {
		this.listaTipiOrdinam = listaTipiOrdinam;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public String getParolaChiave1() {
		return parolaChiave1;
	}

	public void setParolaChiave1(String parolaChiave1) {
		this.parolaChiave1 = parolaChiave1;
	}

	public String getParolaChiave2() {
		return parolaChiave2;
	}

	public void setParolaChiave2(String parolaChiave2) {
		this.parolaChiave2 = parolaChiave2;
	}

	public String getParolaChiave3() {
		return parolaChiave3;
	}

	public void setParolaChiave3(String parolaChiave3) {
		this.parolaChiave3 = parolaChiave3;
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

	public String getSiglaRepertorio() {
		return siglaRepertorio;
	}

	public void setSiglaRepertorio(String siglaRepertorio) {
		this.siglaRepertorio = siglaRepertorio;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
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
