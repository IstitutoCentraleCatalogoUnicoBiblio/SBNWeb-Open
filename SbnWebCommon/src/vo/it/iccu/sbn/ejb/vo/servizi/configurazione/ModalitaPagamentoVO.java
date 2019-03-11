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
package it.iccu.sbn.ejb.vo.servizi.configurazione;

import it.iccu.sbn.ejb.vo.BaseVO;

public class ModalitaPagamentoVO extends BaseVO {

	private static final long serialVersionUID = 5167057197488714619L;
	private String cdPolo;
	private String cdBib;
	private String descrizione;
	private short  codModPagamento;
	private int    id;


	public ModalitaPagamentoVO() {
	}

	public ModalitaPagamentoVO(BaseVO base) {
		super(base);
	}

	@Override
	public boolean equals(Object obj) {
		final ModalitaPagamentoVO other=(ModalitaPagamentoVO)obj;

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		if (cdPolo==null) {
			if (other.cdPolo != null) return false;
		}
		else if (!cdPolo.equals(other.cdPolo)) return false;

		if (cdBib==null){
			if (other.cdBib != null) return false;
		}
		else if (!cdBib.equals(other.cdBib)) return false;

		if (descrizione==null){
			if (other.descrizione != null) return false;
		}
		else if (!descrizione.equals(other.descrizione)) return false;


		return true;
	}


	public String getCdPolo() {
		return cdPolo;
	}

	public void setCdPolo(String cdPolo) {
		this.cdPolo = cdPolo;
	}

	public String getCdBib() {
		return cdBib;
	}

	public void setCdBib(String cdBib) {
		this.cdBib = cdBib;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public short getCodModPagamento() {
		return codModPagamento;
	}

	public void setCodModPagamento(short codModPagamento) {
		this.codModPagamento = codModPagamento;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
