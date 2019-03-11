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
 package it.iccu.sbn.ejb.vo.gestionestampe;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.documentofisico.DocumentoFisicoElaborazioniDifferiteOutputVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;

import java.util.ArrayList;
import java.util.List;

public class StampaServiziVO extends DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = 5533297152917204196L;

	public StampaServiziVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	private String dataDa;
	private String dataA;
	private String codBibCollocazione;
	private String collocazione;
	private MovimentoVO richiesta = new MovimentoVO();
	private String descrBib;

	public void validate(List<String> errori) throws ValidationException {
		super.validate();
	}
	//output
	private List lista = new ArrayList<StampaServiziDettagliVO>();
	private List<String> errori = new ArrayList<String>();
	private String msg;

	//modello da utilizzare per la stampa
	private String codModello;

	//formato di esportazione della stampa richiesta
	private String tipoFormato;
	private SubReportVO subReportStorico;


	public MovimentoVO getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(MovimentoVO richiesta) {
		this.richiesta = richiesta;
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

	public String getCodModello() {
		return codModello;
	}

	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public List getLista() {
		return lista;
	}

	public void setLista(List lista) {
		this.lista = lista;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public String getDataDa() {
		return dataDa;
	}

	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}

	public String getDataA() {
		return dataA;
	}

	public void setDataA(String dataA) {
		this.dataA = dataA;
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getCodBibCollocazione() {
		return codBibCollocazione;
	}

	public void setCodBibCollocazione(String codBibCollocazione) {
		this.codBibCollocazione = codBibCollocazione;
	}

	public void setSubReportStorico(SubReportVO subReportStorico) {
		this.subReportStorico = subReportStorico;
	}

	public SubReportVO getSubReportStorico() {
		return subReportStorico;
	}

}
