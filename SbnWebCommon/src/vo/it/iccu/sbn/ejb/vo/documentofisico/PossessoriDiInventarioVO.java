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

import java.sql.Timestamp;

public class PossessoriDiInventarioVO extends SerializableVO {

	private static final long serialVersionUID = -8534176059822233950L;

	private String codPolo;
	private String codBib;
	private String codSerie;
	private String codInv;
	private Timestamp dataIns;
	private Timestamp dataAgg;
	private String uteIns;
	private String uteAgg;
	private String notaLegame;
	private String codLegame;
	private String pid;
	private String forma;
	private String nome;
	private String fl_canc;

	public PossessoriDiInventarioVO() {
		super();
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}

	public String getCodInv() {
		return codInv;
	}

	public void setCodInv(String codInv) {
		this.codInv = codInv;
	}

	public String getUteIns() {
		return uteIns;
	}

	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}

	public String getUteAgg() {
		return uteAgg;
	}

	public void setUteAgg(String uteAgg) {
		this.uteAgg = uteAgg;
	}

	public String getNotaLegame() {
		return notaLegame;
	}

	public void setNotaLegame(String notaLegame) {
		this.notaLegame = trimAndSet(notaLegame);
	}

	public String getCodLegame() {
		return codLegame;
	}

	public void setCodLegame(String codLegame) {
		this.codLegame = codLegame;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = trimAndSet(nome);
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public Timestamp getDataIns() {
		return dataIns;
	}

	public void setDataIns(Timestamp dataIns) {
		this.dataIns = dataIns;
	}

	public Timestamp getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(Timestamp dataAgg) {
		this.dataAgg = dataAgg;
	}

}
