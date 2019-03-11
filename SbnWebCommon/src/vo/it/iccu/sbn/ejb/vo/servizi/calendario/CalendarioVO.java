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
package it.iccu.sbn.ejb.vo.servizi.calendario;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarioVO extends BaseVO {

	private static final long serialVersionUID = 7149342540748147287L;

	private String codPolo;
	private String codBib;
	private int id;
	private Date inizio;
	private Date fine;
	private String descrizione;

	private List<ElementoCalendarioVO> elementi = new ArrayList<ElementoCalendarioVO>();

	protected TipoCalendario tipo = TipoCalendario.BIBLIOTECA;



	public CalendarioVO() {
		super();
	}

	public CalendarioVO(Date inizio, Date fine) {
		super();
		this.inizio = inizio;
		this.fine = fine;
	}

	public CalendarioVO(CalendarioVO c) {
		super(c);
		this.codPolo = c.codPolo;
		this.codBib = c.codBib;
		this.id = c.id;
		this.inizio = c.inizio;
		this.fine = c.fine;
		this.descrizione = c.descrizione;
		this.elementi = c.elementi;
		this.tipo = c.tipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getInizio() {
		return inizio;
	}

	public void setInizio(Date inizio) {
		this.inizio = DateUtil.removeTime(inizio);
	}

	public Date getFine() {
		return fine;
	}

	public void setFine(Date fine) {
		this.fine = DateUtil.removeTime(fine);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descr) {
		this.descrizione = trimAndSet(descr);
	}

	public List<ElementoCalendarioVO> getElementi() {
		return elementi;
	}

	public void setElementi(List<ElementoCalendarioVO> elementi) {
		this.elementi = elementi;
	}

	public boolean isNuovo() {
		return (id == 0);
	}

	public TipoCalendario getTipo() {
		return tipo;
	}

	public void setTipo(TipoCalendario tipo) {
		this.tipo = tipo;
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

	@Override
	public String toString() {
		return "[id=" + id + ", " + (inizio != null ? "inizio=" + inizio + ", " : "")
				+ (fine != null ? "fine=" + fine : "") + "]";
	}

}
