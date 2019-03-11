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
package it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

public class DefaultVO extends BaseVO {

	private static final long serialVersionUID = -5987577822137943161L;

	private int idDefault;
	private int idGruppoDefault;

	private String indice;
	private String nome;
	private String bundle;
	private String dimensione;
	private String codice_attivita;
	private int seq_ordinamento;

	private String selezione;
	private String[] selezioneMulti;
	private List listaOpzioni = new ArrayList();

	private String tipo; // Tipo di oggetto da visualizzare nella JSP (Es.: Opzioni, multiOpzioni...)
	private String tipoDB; // Tipo di oggetto sul database (Es. Radio, String, Check...)

	private String defaultSistema;
	private String[] defaultSistemaMulti;
	private String provenienza;

	private ConstantDefault _default;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = trimAndSet(nome);
	}

	public List getListaOpzioni() {
		return listaOpzioni;
	}

	public void setListaOpzioni(List listaOpzioni) {
		this.listaOpzioni = listaOpzioni;
	}

	public String getDefaultSistema() {
		return defaultSistema;
	}

	public void setDefaultSistema(String defaultSistema) {
		this.defaultSistema = defaultSistema;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getSelezione() {
		return selezione;
	}

	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String[] getSelezioneMulti() {
		return selezioneMulti;
	}

	public void setSelezioneMulti(String[] selezioneMulti) {
		this.selezioneMulti = selezioneMulti;
	}

	public String[] getDefaultSistemaMulti() {
		return defaultSistemaMulti;
	}

	public void setDefaultSistemaMulti(String[] defaultSistemaMulti) {
		this.defaultSistemaMulti = defaultSistemaMulti;
	}

	public String getDimensione() {
		return dimensione;
	}

	public void setDimensione(String dimensione) {
		this.dimensione = dimensione;
	}

	public String getCodice_attivita() {
		return codice_attivita;
	}

	public void setCodice_attivita(String codice_attivita) {
		this.codice_attivita = codice_attivita;
	}

	public int getSeq_ordinamento() {
		return seq_ordinamento;
	}

	public void setSeq_ordinamento(int seq_ordinamento) {
		this.seq_ordinamento = seq_ordinamento;
	}

	public int getIdDefault() {
		return idDefault;
	}

	public void setIdDefault(int idDefault) {
		this.idDefault = idDefault;
	}

	public int getIdGruppoDefault() {
		return idGruppoDefault;
	}

	public void setIdGruppoDefault(int idGruppoDefault) {
		this.idGruppoDefault = idGruppoDefault;
	}

	public String getTipoDB() {
		return tipoDB;
	}

	public void setTipoDB(String tipoDB) {
		this.tipoDB = tipoDB;
	}

	public ConstantDefault get_default() {
		return _default;
	}

	public void set_default(ConstantDefault _default) {
		this._default = _default;
	}

}
