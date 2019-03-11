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

import java.util.ArrayList;
import java.util.List;

public class DettaglioMarcaGeneraleVO extends SerializableVO {

	// = DettaglioMarcaGeneraleVO.class.hashCode();
// Dati relativi alla ricerca marca - Canali principali

	/**
	 *
	 */
	private static final long serialVersionUID = 8411838422603077734L;

	private String livAut;

	private String campoCodiceRep1Old;
	private String campoProgressivoRep1Old;
	private String campoCodiceRep2Old;
	private String campoProgressivoRep2Old;
	private String campoCodiceRep3Old;
	private String campoProgressivoRep3Old;

	private String campoCodiceRep1;
	private String campoProgressivoRep1;
	private String campoCodiceRep2;
	private String campoProgressivoRep2;
	private String campoCodiceRep3;
	private String campoProgressivoRep3;

	private String mid;
	private String desc;

	private String motto;
	private String parChiave1;
	private String parChiave2;
	private String parChiave3;
	private String parChiave4;
	private String parChiave5;

	private String nota;

	private String dataAgg;
	private String versione;
	private String dataIns;

	private String tipoLegame;

	// Dati eventuali per legami ALLA MARCA
	private String midPadre;
	private String descrizionePadre;
	private String dataVariazionePadre;
	private String notaAlLegame;
	private String tipoLegameCastor;
	private List listaImmagini = new ArrayList();

	public String getDataAgg() {
		return dataAgg;
	}
	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLivAut() {
		return livAut;
	}
	public void setLivAut(String livAut) {
		this.livAut = livAut;
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
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public String getParChiave1() {
		return parChiave1;
	}
	public void setParChiave1(String parChiave1) {
		this.parChiave1 = parChiave1;
	}
	public String getParChiave2() {
		return parChiave2;
	}
	public void setParChiave2(String parChiave2) {
		this.parChiave2 = parChiave2;
	}
	public String getParChiave3() {
		return parChiave3;
	}
	public void setParChiave3(String parChiave3) {
		this.parChiave3 = parChiave3;
	}
	public String getParChiave4() {
		return parChiave4;
	}
	public void setParChiave4(String parChiave4) {
		this.parChiave4 = parChiave4;
	}
	public String getParChiave5() {
		return parChiave5;
	}
	public void setParChiave5(String parChiave5) {
		this.parChiave5 = parChiave5;
	}
	public String getMidPadre() {
		return midPadre;
	}
	public void setMidPadre(String midPadre) {
		this.midPadre = midPadre;
	}

	public String getDescrizionePadre() {
		return descrizionePadre;
	}
	public void setDescrizionePadre(String descrizionePadre) {
		this.descrizionePadre = descrizionePadre;
	}
	public String getNotaAlLegame() {
		return notaAlLegame;
	}
	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}
	public String getDataVariazionePadre() {
		return dataVariazionePadre;
	}
	public void setDataVariazionePadre(String dataVariazionePadre) {
		this.dataVariazionePadre = dataVariazionePadre;
	}
	public String getCampoCodiceRep1() {
		return campoCodiceRep1;
	}
	public void setCampoCodiceRep1(String campoCodiceRep1) {
		this.campoCodiceRep1 = campoCodiceRep1;
	}
	public String getCampoCodiceRep2() {
		return campoCodiceRep2;
	}
	public void setCampoCodiceRep2(String campoCodiceRep2) {
		this.campoCodiceRep2 = campoCodiceRep2;
	}
	public String getCampoCodiceRep3() {
		return campoCodiceRep3;
	}
	public void setCampoCodiceRep3(String campoCodiceRep3) {
		this.campoCodiceRep3 = campoCodiceRep3;
	}
	public String getCampoProgressivoRep1() {
		return campoProgressivoRep1;
	}
	public void setCampoProgressivoRep1(String campoProgressivoRep1) {
		this.campoProgressivoRep1 = campoProgressivoRep1;
	}
	public String getCampoProgressivoRep2() {
		return campoProgressivoRep2;
	}
	public void setCampoProgressivoRep2(String campoProgressivoRep2) {
		this.campoProgressivoRep2 = campoProgressivoRep2;
	}
	public String getCampoProgressivoRep3() {
		return campoProgressivoRep3;
	}
	public void setCampoProgressivoRep3(String campoProgressivoRep3) {
		this.campoProgressivoRep3 = campoProgressivoRep3;
	}
	public String getTipoLegameCastor() {
		return tipoLegameCastor;
	}
	public void setTipoLegameCastor(String tipoLegameCastor) {
		this.tipoLegameCastor = tipoLegameCastor;
	}
	public String getTipoLegame() {
		return tipoLegame;
	}
	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}
	public String getVersione() {
		return versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
	}
	public String getCampoCodiceRep1Old() {
		return campoCodiceRep1Old;
	}
	public void setCampoCodiceRep1Old(String campoCodiceRep1Old) {
		this.campoCodiceRep1Old = campoCodiceRep1Old;
	}
	public String getCampoCodiceRep2Old() {
		return campoCodiceRep2Old;
	}
	public void setCampoCodiceRep2Old(String campoCodiceRep2Old) {
		this.campoCodiceRep2Old = campoCodiceRep2Old;
	}
	public String getCampoCodiceRep3Old() {
		return campoCodiceRep3Old;
	}
	public void setCampoCodiceRep3Old(String campoCodiceRep3Old) {
		this.campoCodiceRep3Old = campoCodiceRep3Old;
	}
	public String getCampoProgressivoRep1Old() {
		return campoProgressivoRep1Old;
	}
	public void setCampoProgressivoRep1Old(String campoProgressivoRep1Old) {
		this.campoProgressivoRep1Old = campoProgressivoRep1Old;
	}
	public String getCampoProgressivoRep2Old() {
		return campoProgressivoRep2Old;
	}
	public void setCampoProgressivoRep2Old(String campoProgressivoRep2Old) {
		this.campoProgressivoRep2Old = campoProgressivoRep2Old;
	}
	public String getCampoProgressivoRep3Old() {
		return campoProgressivoRep3Old;
	}
	public void setCampoProgressivoRep3Old(String campoProgressivoRep3Old) {
		this.campoProgressivoRep3Old = campoProgressivoRep3Old;
	}
	public List getListaImmagini() {
		return listaImmagini;
	}
	public void setListaImmagini(List listaImmagini) {
		this.listaImmagini = listaImmagini;
	}

}
