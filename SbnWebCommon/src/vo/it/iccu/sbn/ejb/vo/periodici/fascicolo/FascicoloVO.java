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
package it.iccu.sbn.ejb.vo.periodici.fascicolo;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.extension.cloning.Copyable;

import java.util.Date;

public class FascicoloVO extends BaseVO implements Copyable<FascicoloVO> {

	private static final long serialVersionUID = -4048470579566971092L;

	private String codPolo;
	protected String codBib;

	//dati fascicolo
	private String bid;
	protected int fid;
	private String sici;
	private String ean;
	private Date data_conv_pub;
	protected String cd_per;
	protected String cd_tipo_fasc;
	private String data_pubb;
	private String descrizione;
	protected String annata;
	protected Short num_volume;
	protected Integer num_in_fasc;
	protected Date data_in_pubbl;
	protected Integer num_fi_fasc;
	protected Date data_fi_pubbl;
	private String note;
	protected String num_alter;
	private String bid_link;
	private Integer fid_link;

	protected EsemplareFascicoloVO esemplare;

	protected StatoFascicolo stato = StatoFascicolo.ATTESO;
	private boolean sollecitato = false;
	private boolean ritardo = false;


	public FascicoloVO(FascicoloVO f) {
		super(f);
		this.codPolo = f.codPolo;
		this.codBib = f.codBib;
		this.bid = f.bid;
		this.fid = f.fid;
		this.sici = f.sici;
		this.ean = f.ean;
		this.data_conv_pub = f.data_conv_pub;
		this.cd_per = f.cd_per;
		this.cd_tipo_fasc = f.cd_tipo_fasc;
		this.data_pubb = f.data_pubb;
		this.descrizione = f.descrizione;
		this.annata = f.annata;
		this.num_volume = f.num_volume;
		this.num_in_fasc = f.num_in_fasc;
		this.data_in_pubbl = f.data_in_pubbl;
		this.num_fi_fasc = f.num_fi_fasc;
		this.data_fi_pubbl = f.data_fi_pubbl;
		this.note = f.note;
		this.num_alter = f.num_alter;
		this.bid_link = f.bid_link;
		this.fid_link = f.fid_link;
		this.stato = f.stato;
		this.sollecitato = f.sollecitato;
		this.ritardo = f.ritardo;

		this.esemplare = f.esemplare != null ? new EsemplareFascicoloVO(f.esemplare) : null;
	}

	public boolean isNuovo() {
		return (fid == 0);
	}

	public boolean isCancellato() {
		return ValidazioneDati.equalsIgnoreCase(flCanc, "S");
	}

	public FascicoloVO() {
		super();
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
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
		this.sici = trimAndSet(sici);
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = trimAndSet(ean);
	}

	public Date getData_conv_pub() {
		return data_conv_pub;
	}

	public void setData_conv_pub(Date data_conv_pub) {
		this.data_conv_pub = data_conv_pub;
	}

	public String getCd_per() {
		return cd_per;
	}

	public void setCd_per(String cd_per) {
		this.cd_per = trimAndSet(cd_per);
	}

	public String getCd_tipo_fasc() {
		return cd_tipo_fasc;
	}

	public void setCd_tipo_fasc(String cd_tipo_fasc) {
		this.cd_tipo_fasc = trimAndSet(cd_tipo_fasc);
	}

	public String getData_pubb() {
		return data_pubb;
	}

	public void setData_pubb(String data_pubb) {
		this.data_pubb = trimAndSet(data_pubb);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public String getAnnata() {
		return annata;
	}

	public void setAnnata(String annata) {
		this.annata = trimAndSet(annata);
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
		this.note = trimAndSet(note);
	}

	public String getNum_alter() {
		return num_alter;
	}

	public void setNum_alter(String num_alter) {
		this.num_alter = trimAndSet(num_alter);
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

	public EsemplareFascicoloVO getEsemplare() {
		return esemplare;
	}

	public void setEsemplare(EsemplareFascicoloVO esemplare) {
		this.esemplare = esemplare;
	}

	public StatoFascicolo getStato() {
		return stato;
	}

	public void setStato(StatoFascicolo stato) {
		this.stato = stato;
	}

	public boolean isSollecitato() {
		return sollecitato;
	}

	public void setSollecitato(boolean sollecitato) {
		this.sollecitato = sollecitato;
	}

	public boolean isRitardo() {
		return ritardo;
	}

	public void setRitardo(boolean ritardo) {
		this.ritardo = ritardo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bid == null) ? 0 : bid.hashCode());
		result = prime * result
				+ ((cd_tipo_fasc == null) ? 0 : cd_tipo_fasc.hashCode());
		result = prime * result
				+ ((data_conv_pub == null) ? 0 : data_conv_pub.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FascicoloVO other = (FascicoloVO) obj;
		if (bid == null) {
			if (other.bid != null)
				return false;
		} else if (!bid.equals(other.bid))
			return false;
		if (cd_tipo_fasc == null) {
			if (other.cd_tipo_fasc != null)
				return false;
		} else if (!cd_tipo_fasc.equals(other.cd_tipo_fasc))
			return false;
		if (data_conv_pub == null) {
			if (other.data_conv_pub != null)
				return false;
		} else if (!data_conv_pub.equals(other.data_conv_pub))
			return false;
		return true;
	}

	public FascicoloVO copyThis() {
		return new FascicoloVO(this);
	}

}
