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

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;

import java.util.List;

public class ListaSuppFornitoreProfiloVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -2183901607000529844L;
	private int elemXBlocchi;
	private String codPolo;
	private String codBibl;
	private String paese;
	private StrutturaCombo lingua;
	private StrutturaCombo sezione;
	private StrutturaCombo profilo;
	private String tipoPartner;
	private String chiamante;
	private String locale;
	private List<FornitoreVO> selezioniChiamato;
	private String ordinamento;
	private String ticket;
	private boolean flag_canc=false;


	public ListaSuppFornitoreProfiloVO (){}


	public String getChiamante() {
		return chiamante;
	}


	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}


	public String getCodBibl() {
		return codBibl;
	}


	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}


	public String getCodPolo() {
		return codPolo;
	}


	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}


	public int getElemXBlocchi() {
		return elemXBlocchi;
	}


	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}


	public boolean isFlag_canc() {
		return flag_canc;
	}


	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}


	public StrutturaCombo getLingua() {
		return lingua;
	}


	public void setLingua(StrutturaCombo lingua) {
		this.lingua = lingua;
	}


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}


	public String getOrdinamento() {
		return ordinamento;
	}


	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}


	public String getPaese() {
		return paese;
	}


	public void setPaese(String paese) {
		this.paese = paese;
	}


	public StrutturaCombo getProfilo() {
		return profilo;
	}


	public void setProfilo(StrutturaCombo profilo) {
		this.profilo = profilo;
	}


	public List<FornitoreVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}


	public void setSelezioniChiamato(List<FornitoreVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}


	public StrutturaCombo getSezione() {
		return sezione;
	}


	public void setSezione(StrutturaCombo sezione) {
		this.sezione = sezione;
	}


	public String getTicket() {
		return ticket;
	}


	public void setTicket(String ticket) {
		this.ticket = ticket;
	}


	public String getTipoPartner() {
		return tipoPartner;
	}


	public void setTipoPartner(String tipoPartner) {
		this.tipoPartner = tipoPartner;
	};
/*	public ListaSuppFornitoreProfiloVO(String codP, String codB, String codForn, String nomeForn, String codProfAcq,String paeseForn, String tipoPForn,String provForn, String chiama, String loc)
			throws Exception {
		if (tit == null) {
			throw new Exception("Titolo non valido");
		}
		if (forn == null) {
			throw new Exception("Fornitore non valido");
		}
		if (bil == null) {
			throw new Exception("Bilancio non valido");
		}
		if (tipo == null) {
			throw new Exception("tipo ordine non valido");
		}
		this.codPolo=codP;
		this.codBibl=codB;
		this.codFornitore=codForn;
		this.nomeFornitore=nomeForn;
		this.codProfiloAcq=codProfAcq;
		this.paese=paeseForn;
		this.tipoPartner=tipoPForn ;
		this.provincia=provForn;
		this.chiamante = chiama;
		this.locale = loc;
	}
*/


}
