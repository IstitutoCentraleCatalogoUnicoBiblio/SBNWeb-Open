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
package it.iccu.sbn.polo.orm.periodici;

import it.iccu.sbn.polo.orm.Tb_base;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;

import java.util.Date;

public class Tbp_fascicolo extends Tb_base {

	private static final long serialVersionUID = -3534276640798759479L;

	private Tb_titolo titolo;
	private int fid;
	private String sici;
	private String ean;
	private Date data_conv_pub;
	private Character cd_per;
	private Character cd_tipo_fasc;
	private String data_pubb;
	private String descrizione;
	private String annata;
	private Short num_volume;
	private Integer num_in_fasc;
	private Date data_in_pubbl;
	private Integer num_fi_fasc;
	private Date data_fi_pubbl;
	private String note;
	private String num_alter;
	private String bid_link;
	private Integer fid_link;
	private Short anno_pub;

	private java.util.Set Tbp_esemplare_fascicolo = new java.util.HashSet();

	private java.util.Set Trp_messaggio_fascicolo = new java.util.HashSet();

	public Tb_titolo getTitolo() {
		return titolo;
	}

	public void setTitolo(Tb_titolo titolo) {
		this.titolo = titolo;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getSici() {
		return sici;
	}

	public void setSici(String sici) {
		this.sici = sici;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public Date getData_conv_pub() {
		return data_conv_pub;
	}

	public void setData_conv_pub(Date data_conv_pub) {
		this.data_conv_pub = data_conv_pub;
	}

	public Character getCd_per() {
		return cd_per;
	}

	public void setCd_per(Character cd_per) {
		this.cd_per = cd_per;
	}

	public Character getCd_tipo_fasc() {
		return cd_tipo_fasc;
	}

	public void setCd_tipo_fasc(Character cd_tipo_fasc) {
		this.cd_tipo_fasc = cd_tipo_fasc;
	}

	public String getData_pubb() {
		return data_pubb;
	}

	public void setData_pubb(String data_pubb) {
		this.data_pubb = data_pubb;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getAnnata() {
		return annata;
	}

	public void setAnnata(String annata) {
		this.annata = annata;
	}

	public Short getNum_volume() {
		return num_volume;
	}

	public void setNum_volume(Short num_volume) {
		this.num_volume = num_volume;
	}

	public Integer getNum_in_fasc() {
		return num_in_fasc;
	}

	public void setNum_in_fasc(Integer num_in_fasc) {
		this.num_in_fasc = num_in_fasc;
	}

	public Date getData_in_pubbl() {
		return data_in_pubbl;
	}

	public void setData_in_pubbl(Date data_in_pubbl) {
		this.data_in_pubbl = data_in_pubbl;
	}

	public Integer getNum_fi_fasc() {
		return num_fi_fasc;
	}

	public void setNum_fi_fasc(Integer num_fi_fasc) {
		this.num_fi_fasc = num_fi_fasc;
	}

	public Date getData_fi_pubbl() {
		return data_fi_pubbl;
	}

	public void setData_fi_pubbl(Date data_fi_pubbl) {
		this.data_fi_pubbl = data_fi_pubbl;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNum_alter() {
		return num_alter;
	}

	public void setNum_alter(String num_alter) {
		this.num_alter = num_alter;
	}

	public String getBid_link() {
		return bid_link;
	}

	public void setBid_link(String bid_link) {
		this.bid_link = bid_link;
	}

	public Integer getFid_link() {
		return fid_link;
	}

	public void setFid_link(Integer fid_link) {
		this.fid_link = fid_link;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
		result = prime * result + fid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tbp_fascicolo other = (Tbp_fascicolo) obj;
		if (titolo == null) {
			if (other.titolo != null)
				return false;
		} else if (!titolo.equals(other.titolo))
			return false;
		if (fid != other.fid)
			return false;
		return true;
	}

	public java.util.Set getTbp_esemplare_fascicolo() {
		return Tbp_esemplare_fascicolo;
	}

	public void setTbp_esemplare_fascicolo(java.util.Set tbp_esemplare_fascicolo) {
		Tbp_esemplare_fascicolo = tbp_esemplare_fascicolo;
	}

	public java.util.Set getTrp_messaggio_fascicolo() {
		return Trp_messaggio_fascicolo;
	}

	public void setTrp_messaggio_fascicolo(java.util.Set trp_messaggio_fascicolo) {
		Trp_messaggio_fascicolo = trp_messaggio_fascicolo;
	}

	@Override
	public String toString() {
		return (titolo != null ? titolo.getBid() + " " : "") + fid;
	}

	public Short getAnno_pub() {
		return anno_pub;
	}

	public void setAnno_pub(Short anno_pub) {
		this.anno_pub = anno_pub;
	}

}
