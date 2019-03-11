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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.List;

public class AdeguamentoCalcoliVO extends
		ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = 743211038274736690L;
	private String codPolo;
	private String codBib;

	private String tipoOperazione;
	private List<Integer> datiInput;

	private List<String> errori = new ArrayList<String>();
	private String msg;
	private boolean adeguamentoPrezzo = false;

	public AdeguamentoCalcoliVO() {
	}

	public List<String> getErrori() {
		return errori;
	}

	public void setErrori(List<String> errori) {
		this.errori = errori;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public List<Integer> getDatiInput() {
		return datiInput;
	}

	public void setDatiInput(List<Integer> datiInput) {
		this.datiInput = datiInput;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public boolean isAdeguamentoPrezzo() {
		return adeguamentoPrezzo;
	}

	public void setAdeguamentoPrezzo(boolean adeguamentoPrezzo) {
		this.adeguamentoPrezzo = adeguamentoPrezzo;
	};
}
