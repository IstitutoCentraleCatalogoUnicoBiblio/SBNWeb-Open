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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.web.constant.ConstantDefault;

public class BibliotecaRicercaVO extends SerializableVO {

	private static final long serialVersionUID = -5575420298993527344L;

	public enum BibliotecaType {
		TUTTE,
		SBN,
		NON_SBN;
	}

	public enum RicercaBibliotecaType {
		TUTTE,
		NON_ABILITATE,
		ABILITATE,
		CENTRO_SISTEMA,
		AFFILIATE;
	}

	private String codiceBib;
	private String codiceAna;
	private String codicePolo;
	private String nome;
	private String indirizzo;
	private String cap;
	private String citta;
	private String provincia;
	private BibliotecaType bibinpolo;
	private String tipoBib;
	private RicercaBibliotecaType flagSbn;
	private String ordinamento;
	private int elemPerBlocchi = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	private String codSistemaMetro;

	private RuoloBiblioteca ruoloILL;
	private String isilBibRichiedente;

	public String getCodiceBib() {
		return codiceBib;
	}

	public void setCodiceBib(String codiceBib) {
		this.codiceBib = codiceBib;
	}

	public String getCodiceAna() {
		return codiceAna;
	}

	public void setCodiceAna(String codiceAna) {
		this.codiceAna = codiceAna;
	}

	public String getCodicePolo() {
		return codicePolo;
	}

	public void setCodicePolo(String codicePolo) {
		this.codicePolo = codicePolo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public BibliotecaType getBibinpolo() {
		return bibinpolo;
	}

	public void setBibinpolo(BibliotecaType bibinpolo) {
		this.bibinpolo = bibinpolo;
	}

	public String getTipoBib() {
		return tipoBib;
	}

	public void setTipoBib(String tipoBib) {
		this.tipoBib = tipoBib;
	}

	public RicercaBibliotecaType getFlagSbn() {
		return flagSbn;
	}

	public void setFlagSbn(RicercaBibliotecaType flagSbn) {
		this.flagSbn = flagSbn;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public int getElemPerBlocchi() {
		return elemPerBlocchi;
	}

	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.elemPerBlocchi = elemPerBlocchi;
	}

	public String getCodSistemaMetro() {
		return codSistemaMetro;
	}

	public void setCodSistemaMetro(String codSistemaMetro) {
		this.codSistemaMetro = codSistemaMetro;
	}

	public RuoloBiblioteca getRuoloILL() {
		return ruoloILL;
	}

	public void setRuoloILL(RuoloBiblioteca ruoloILL) {
		this.ruoloILL = ruoloILL;
	}

	public String getIsilBibRichiedente() {
		return isilBibRichiedente;
	}

	public void setIsilBibRichiedente(String isilBibRichiedente) {
		this.isilBibRichiedente = isilBibRichiedente;
	}

}
