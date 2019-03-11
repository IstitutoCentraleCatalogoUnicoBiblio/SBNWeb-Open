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

public class CollocazioneDettaglioVO extends CollocazioneVO {

	private static final long serialVersionUID = -548899630666379671L;
	private String bidDescr;
	private String bidDocDescr;
	private String tipoColloc;
	private String tipoSez;
	private String codCla;
	private int totInvSez;
	private int totInvMax;
	private String descr;
	private String descrTipoColl;
	private String noteSez;

	public CollocazioneDettaglioVO() {
		super();
	}

	public CollocazioneDettaglioVO(CollocazioneVO coll) throws Exception {
		super(coll);
		this.bidDescr = "";
		this.bidDocDescr = "";
		this.tipoColloc = "";
		this.tipoSez = "";
		this.codCla = "";
		this.totInvSez = 0;
		this.totInvMax = 0;
		this.descr = "";
		this.noteSez = "";
		this.descrTipoColl = "";
	}

	public CollocazioneDettaglioVO(CollocazioneVO coll, String bidDescr,
			String bidDocDescr, String tipoColloc, String tipoSez,
			String codCla, int totInvSez, int totInvMax, String descr,
			String noteSez, String descrTipoColl) throws Exception {
		super(coll);
		this.bidDescr = bidDescr;
		this.bidDocDescr = bidDocDescr;
		this.tipoColloc = tipoColloc;
		this.tipoSez = tipoSez;
		this.codCla = codCla;
		this.totInvSez = totInvSez;
		this.totInvMax = totInvMax;
		this.descr = descr;
		this.noteSez = noteSez;
		this.descrTipoColl = descrTipoColl;
	}

	public String getBidDescr() {
		return bidDescr;
	}

	public void setBidDescr(String bidDescr) {
		this.bidDescr = bidDescr;
	}

	public String getCodCla() {
		return codCla;
	}

	public void setCodCla(String codCla) {
		this.codCla = codCla;
	}

	public String getTipoColloc() {
		return tipoColloc;
	}

	public void setTipoColloc(String tipoColloc) {
		this.tipoColloc = tipoColloc;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getNoteSez() {
		return noteSez;
	}

	public void setNoteSez(String noteSez) {
		this.noteSez = noteSez;
	}

	public String getTipoSez() {
		return tipoSez;
	}

	public void setTipoSez(String tipoSez) {
		this.tipoSez = tipoSez;
	}

	public int getTotInvMax() {
		return totInvMax;
	}

	public void setTotInvMax(int totInvMax) {
		this.totInvMax = totInvMax;
	}

	public int getTotInvSez() {
		return totInvSez;
	}

	public void setTotInvSez(int totInvSez) {
		this.totInvSez = totInvSez;
	}

	public String getDescrTipoColl() {
		return descrTipoColl;
	}

	public void setDescrTipoColl(String descrTipoColl) {
		this.descrTipoColl = descrTipoColl;
	}

	public String getBidDocDescr() {
		return bidDocDescr;
	}

	public void setBidDocDescr(String bidDocDescr) {
		this.bidDocDescr = bidDocDescr;
	}
}
