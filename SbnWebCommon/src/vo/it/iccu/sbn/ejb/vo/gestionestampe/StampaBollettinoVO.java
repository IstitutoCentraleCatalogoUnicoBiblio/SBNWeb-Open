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

import it.iccu.sbn.ejb.vo.documentofisico.DocumentoFisicoElaborazioniDifferiteOutputVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.List;

public class StampaBollettinoVO extends
		DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = -2716406029905468225L;

	private String descrBib;
	private String dataDa;
	private String dataA;
	private boolean nuoviTitoli;
	private boolean nuoviEsemplari;
	private String tipoLista;

	private String intestazione;

	private List lista = new ArrayList<StampaBollettinoDettaglioVO>();
	private String ordinaPer;

	public StampaBollettinoVO(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	public String getIntestazione() {
		return intestazione;
	}

	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
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

	public boolean isNuoviTitoli() {
		return nuoviTitoli;
	}

	public void setNuoviTitoli(boolean nuoviTitoli) {
		this.nuoviTitoli = nuoviTitoli;
	}

	public boolean isNuoviEsemplari() {
		return nuoviEsemplari;
	}

	public void setNuoviEsemplari(boolean nuoviEsemplari) {
		this.nuoviEsemplari = nuoviEsemplari;
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

	public String getTipoLista() {
		return tipoLista;
	}

	public void setTipoLista(String tipoLista) {
		this.tipoLista = tipoLista;
	}

	public String getOrdinaPer() {
		return ordinaPer;
	}

	public void setOrdinaPer(String ordinaPer) {
		this.ordinaPer = ordinaPer;
	}

}
