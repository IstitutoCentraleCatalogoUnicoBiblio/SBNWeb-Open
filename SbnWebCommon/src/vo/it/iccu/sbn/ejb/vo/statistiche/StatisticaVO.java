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
package it.iccu.sbn.ejb.vo.statistiche;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatisticaVO extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = 2983974354984765402L;

	private int prg;
	private String idConfig;
	private String idAreaSezione;
	private String parent;
	private Integer seqOrdinamento;
	private String codiceAttivita;
	private String parametroAttivita;
	private Short codiceModulo;
	private String nomeStatistica;
	private String nomeStatisticaPerLog;
	private String tipoQuery;
	private String query;
	private List<DettVarStatisticaVO> elencoVariabili = new ArrayList<DettVarStatisticaVO>();
	private List<String> parametriRichiestaInput = new ArrayList<String>();
	private ParametriExcelVO parExc = new ParametriExcelVO();
	private String flPoloBiblio;
	private String flTxt;
	private String fileName;

	private List<StrutturaCombo> intestazioni = new ArrayList<StrutturaCombo>();
	public List<StrutturaCombo> getIntestazioni() {
		return intestazioni;
	}

	private List<Serializable> risultati = new ArrayList<Serializable>();

	public List<Serializable> getRisultati() {
		return risultati;
	}

	public void setRisultati(List<Serializable> risultati) {
		this.risultati = risultati;
	}

	public void setIntestazioni(List<StrutturaCombo> intestazioni) {
		this.intestazioni = intestazioni;
	}

	public void validate(List<String> errori) throws ValidationException {
		super.validate();
	}

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	public StatisticaVO() {
	}

	public String getIdConfig() {
		return idConfig;
	}

	public void setIdConfig(String idConfig) {
		this.idConfig = idConfig;
	}

	public String getIdAreaSezione() {
		return idAreaSezione;
	}

	public void setIdAreaSezione(String idAreaSezione) {
		this.idAreaSezione = idAreaSezione;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Integer getSeqOrdinamento() {
		return seqOrdinamento;
	}

	public void setSeqOrdinamento(Integer seqOrdinamento) {
		this.seqOrdinamento = seqOrdinamento;
	}

	public String getCodiceAttivita() {
		return codiceAttivita;
	}

	public void setCodiceAttivita(String codiceAttivita) {
		this.codiceAttivita = codiceAttivita;
	}

	public String getParametroAttivita() {
		return parametroAttivita;
	}

	public void setParametroAttivita(String parametroAttivita) {
		this.parametroAttivita = parametroAttivita;
	}

	public Short getCodiceModulo() {
		return codiceModulo;
	}

	public void setCodiceModulo(Short codiceModulo) {
		this.codiceModulo = codiceModulo;
	}

	public String getNomeStatistica() {
		return nomeStatistica;
	}

	public void setNomeStatistica(String nomeStatistica) {
		this.nomeStatistica = nomeStatistica;
	}

	public String getTipoQuery() {
		return tipoQuery;
	}

	public void setTipoQuery(String tipoQuery) {
		this.tipoQuery = tipoQuery;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public List<DettVarStatisticaVO> getElencoVariabili() {
		return elencoVariabili;
	}

	public void setElencoVariabili(
			List<DettVarStatisticaVO> elencoVariabili) {
		this.elencoVariabili = elencoVariabili;
	}

	public ParametriExcelVO getParExc() {
		return parExc;
	}

	public void setParExc(ParametriExcelVO parExc) {
		this.parExc = parExc;
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

	public String getNomeStatisticaPerLog() {
		return nomeStatisticaPerLog;
	}

	public void setNomeStatisticaPerLog(String nomeStatisticaPerLog) {
		this.nomeStatisticaPerLog = nomeStatisticaPerLog;
	}

	public List<String> getParametriRichiestaInput() {
		return parametriRichiestaInput;
	}

	public void setParametriRichiestaInput(List<String> parametriRichiestaInput) {
		this.parametriRichiestaInput = parametriRichiestaInput;
	}

	public String getFlPoloBiblio() {
		return flPoloBiblio;
	}

	public void setFlPoloBiblio(String flPoloBiblio) {
		this.flPoloBiblio = flPoloBiblio;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFlTxt() {
		return flTxt;
	}

	public void setFlTxt(String flTxt) {
		this.flTxt = flTxt;
	}

}
