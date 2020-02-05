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

import java.util.Comparator;


public class InventarioListeVO extends InventarioVO implements
		Comparable<InventarioListeVO> {

	public static final Comparator<InventarioListeVO> ORDINAMENTO_ORDINE_INVENTARIO = new Comparator<InventarioListeVO>() {
		public int compare(InventarioListeVO i1, InventarioListeVO i2) {
			int cmp = Integer.valueOf(i1.getAnnoOrd()) -  Integer.valueOf(i2.getAnnoOrd());
			cmp = (cmp != 0) ? cmp : i1.getCodTipoOrd().compareTo(i2.getCodTipoOrd());
			cmp = (cmp != 0) ? cmp : Integer.valueOf(i1.getCodOrd()) -  Integer.valueOf(i2.getCodOrd());
			cmp = (cmp != 0) ? cmp : i1.getCodSerie().compareTo(i2.getCodSerie());
			cmp = (cmp != 0) ? cmp : i1.getCodInvent() - i2.getCodInvent();

			return cmp;
		}
	};

	private static final long serialVersionUID = -755646293971535114L;
	private String separatore;
	private String descr;
	// progressivo per liste inventari
	private int prg;
	// campi per registro ingresso e possessori
	private String codSez;
	private String codLoc;
	private String codSpec;
	// campi per possessori
	private String codColl;
	private String notaColl;
	private String legame;
	private String bidOrd;
	private String isbdOrd;


	public String getCodColl() {
		return codColl;
	}

	public void setCodColl(String codColl) {
		this.codColl = codColl;
	}

	public String getNotaColl() {
		return notaColl;
	}

	public void setNotaColl(String notaColl) {
		this.notaColl = notaColl;
	}

	public InventarioListeVO() {

	}

	public InventarioListeVO(int codInvent) throws Exception {
		super(codInvent);
	}

	public InventarioListeVO(InventarioVO recInv) throws Exception {
		super(recInv);
	}

	// Lista Inventari non collocati
	public InventarioListeVO(int prg, int codInvent, String bid,
			String precInv, String descr) throws Exception {
		super(codInvent, bid, precInv);
		this.descr = descr;
	}

	// //Lista Inventari ordine
	// public InventarioListeVO(int prg, String codBib, String serie, int inv,
	// String precInv, String seqColl, String descr, String valore)
	// throws Exception {
	// super(codBib, serie, inv, seqColl, precInv, valore);
	// this.descr = descr;
	// }
	// Lista Inventari ordine
	public InventarioListeVO(int prg, InventarioVO inv, String descr,
			String separatore) throws Exception {
		super(inv);
		this.separatore = separatore;
		this.descr = descr;
	}

	// Lista Inventari ordini
	public InventarioListeVO(int prg, String codTipoOrd, String annoOrd,
			String codOrd, String bidOrd, String isbdOrd, InventarioVO inv,
			String descr, String separatore) throws Exception {
		super(inv);

		this.setCodTipoOrd(codTipoOrd);
		this.setAnnoOrd(annoOrd);
		this.setCodOrd(codOrd);
		this.bidOrd = bidOrd;
		this.isbdOrd = isbdOrd;
		this.separatore = separatore;
		this.descr = descr;
	}

	// Lista Inventari di Collocazione
	public InventarioListeVO(int prg, String codBib, String codSerie,
			int codInvent, String codSit, String seqColl, String prec,
			String bid, int keyLoc, Integer keyLocOld, String sezOld,
			String locOld, String specOld, String descr) throws Exception {
		super(codBib, codSerie, codInvent, codSit, seqColl, prec, bid, keyLoc,
				keyLocOld, sezOld, locOld, specOld);
		this.descr = descr;
	}

	// Lista Inventari Registro Ingresso
	public InventarioListeVO(InventarioVO recInv, String descr, String codSez,
			String codLoc, String codSpec) throws Exception {
		super(recInv);
		this.descr = descr;
		this.codSez = codSez;
		this.codLoc = codLoc;
		this.codSpec = codSpec;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public String getSeparatore() {
		return separatore;
	}

	public void setSeparatore(String separatore) {
		this.separatore = separatore;
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getCodLoc() {
		return codLoc;
	}

	public void setCodLoc(String codLoc) {
		this.codLoc = trimAndSet(codLoc);
	}

	public String getCodSpec() {
		return codSpec;
	}

	public void setCodSpec(String codSpec) {
		this.codSpec = trimAndSet(codSpec);
	}

	public String getLegame() {
		return legame;
	}

	public void setLegame(String legame) {
		this.legame = legame;
	}

	public int compareTo(InventarioListeVO o) {
		return (this.prg - o.prg);
	}

	public String getBidOrd() {
		return bidOrd;
	}

	public void setBidOrd(String bidOrd) {
		this.bidOrd = bidOrd;
	}

	public String getIsbdOrd() {
		return isbdOrd;
	}

	public void setIsbdOrd(String isbdOrd) {
		this.isbdOrd = isbdOrd;
	}

	public String getDescrizioneColl() {
		if (this.getKeyLoc() > 0) {
			StringBuilder buf = new StringBuilder();
			buf.append(codSez);
			buf.append(" ");
			buf.append(codLoc);
			buf.append(" ");
			buf.append(codSpec);
			return buf.toString();
		}
		return "";
	}

	public String getChiaveInventario() {
		StringBuilder buf = new StringBuilder();
		buf.append(trimOrEmpty(getCodBib()));
		buf.append(" ");
		buf.append(getCodSerie());
		buf.append(" ");
		buf.append(getCodInvent());

		return buf.toString();
	}

}
