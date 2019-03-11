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
package it.iccu.sbn.periodici.vo;

import java.io.Serializable;
import java.util.List;

public class SchedaPeriodicoVO implements Serializable {

	private static final long serialVersionUID = -810242775453294918L;

	private String knide = "";
	private String bidsbn = "";
	private String natura = "";
	private String issn = "";
	private String lingua = "";
	private String paese = "";
	private String tipod = "";
	private String anno;
	private String anno2;
	private String genere = "";
	private String periodic = "";
	private String isbd = "";
	private String cles = "";
	private List<SchedaInventarioVO> schedaInventario;
	private List<SchedaSintesiVO> schedaSintesi;
	private List<SchedaAbbonamentoVO> schedaAbbonamento;

	public SchedaPeriodicoVO() {
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getAnno2() {
		return anno2;
	}

	public void setAnno2(String anno2) {
		this.anno2 = anno2;
	}

	public String getBidsbn() {
		return bidsbn;
	}

	public void setBidsbn(String bidsbn) {
		this.bidsbn = bidsbn;
	}

	public String getCles() {
		return cles;
	}

	public void setCles(String cles) {
		this.cles = cles;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getKnide() {
		return knide;
	}

	public void setKnide(String knide) {
		this.knide = knide;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getPeriodic() {
		return periodic;
	}

	public void setPeriodic(String periodic) {
		this.periodic = periodic;
	}

	public List<SchedaAbbonamentoVO> getSchedaAbbonamento() {
		return schedaAbbonamento;
	}

	public void setSchedaAbbonamento(List<SchedaAbbonamentoVO> schedaAbbonamento) {
		this.schedaAbbonamento = schedaAbbonamento;
	}

	public List<SchedaInventarioVO> getSchedaInventario() {
		return schedaInventario;
	}

	public void setSchedaInventario(List<SchedaInventarioVO> schedaInventgario) {
		this.schedaInventario = schedaInventgario;
	}

	public List<SchedaSintesiVO> getSchedaSintesi() {
		return schedaSintesi;
	}

	public void setSchedaSintesi(List<SchedaSintesiVO> schedaSintesi) {
		this.schedaSintesi = schedaSintesi;
	}

	public String getTipod() {
		return tipod;
	}

	public void setTipod(String tipod) {
		this.tipod = tipod;
	}

}
