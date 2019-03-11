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

import java.util.ArrayList;
import java.util.List;


public class StampaEtichetteVO extends DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = 8000746515229926224L;

	public StampaEtichetteVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}

	//modello da utilizzare per la stampa del barCode
	private String barCode;

	//output
	private String descrBibEtichetta;
	private List<String> errori = new ArrayList<String>();
	private String msg;

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}
	//stampare le etichette saltando stampaDa posizioni
	private int stampaDa;

	//modello da utilizzare per la stampa
	private String codModello;

	//formato di esportazione della stampa richiesta
	private String tipoFormato;

	public String getCodModello() {
		return codModello;
	}
	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}
	public int getStampaDa() {
		return stampaDa;
	}
	public void setStampaDa(int stampaDa) {
		this.stampaDa = stampaDa;
	}
	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
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
	public String getDescrBibEtichetta() {
		return descrBibEtichetta;
	}
	public void setDescrBibEtichetta(String descrBibEtichetta) {
		this.descrBibEtichetta = descrBibEtichetta;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

}
