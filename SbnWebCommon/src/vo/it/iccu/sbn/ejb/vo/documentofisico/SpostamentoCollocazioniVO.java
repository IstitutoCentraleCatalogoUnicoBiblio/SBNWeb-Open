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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;

import java.util.ArrayList;
import java.util.List;

public class SpostamentoCollocazioniVO extends
		DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = -1762243798785187965L;

	private String codPoloSezArr;
	private String codBibSezArr;
	private String codSezArr;
	private String tipoSezioneArr;
	private String collProvv;
	private String specProvv;

	private String prestito;
	private String etichette;
	private String codModello;
	private String tipoFormato;
	private String descrBibEtichetta;
	private int numCopie;
	public int getNumCopie() {
		return numCopie;
	}

	public void setNumCopie(int numCopie) {
		this.numCopie = numCopie;
	}

	List listaEtichette = new ArrayList();
	private StampaDiffVO stampaDiffEtichette;

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}

	public SpostamentoCollocazioniVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	//
	private String azione;
	private Tbc_sezione_collocazione sezioneA;
	private Tbc_sezione_collocazione sezioneP;

	public String getCodPoloSezArr() {
		return codPoloSezArr;
	}

	public void setCodPoloSezArr(String codPoloSezArr) {
		this.codPoloSezArr = codPoloSezArr;
	}

	public String getCodBibSezArr() {
		return codBibSezArr;
	}

	public void setCodBibSezArr(String codBibSezArr) {
		this.codBibSezArr = codBibSezArr;
	}

	public String getCodSezArr() {
		return codSezArr;
	}

	public void setCodSezArr(String codSezArr) {
		this.codSezArr = codSezArr;
	}

	public String getPrestito() {
		return prestito;
	}

	public void setPrestito(String prestito) {
		this.prestito = prestito;
	}

	public String getEtichette() {
		return etichette;
	}

	public void setEtichette(String etichette) {
		this.etichette = etichette;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCollProvv() {
		return collProvv;
	}

	public void setCollProvv(String collProvv) {
		this.collProvv = collProvv;
	}

	public String getSpecProvv() {
		return specProvv;
	}

	public void setSpecProvv(String specProvv) {
		this.specProvv = specProvv;
	}

	public List<String> getErrori() {
		return errori;
	}

	public String getTipoSezioneArr() {
		return tipoSezioneArr;
	}

	public void setTipoSezioneArr(String tipoSezioneArr) {
		this.tipoSezioneArr = tipoSezioneArr;
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public Tbc_sezione_collocazione getSezioneA() {
		return sezioneA;
	}

	public void setSezioneA(Tbc_sezione_collocazione sezioneA) {
		this.sezioneA = sezioneA;
	}

	public Tbc_sezione_collocazione getSezioneP() {
		return sezioneP;
	}

	public void setSezioneP(Tbc_sezione_collocazione sezioneP) {
		this.sezioneP = sezioneP;
	}

	public String getStringaToPrint() {

		if (this.msg == null || this.msg.equals("")) {
			return "<br>";
		} else {
			return msg + "<br>";
		}
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List getListaEtichette() {
		return listaEtichette;
	}

	public void setListaEtichette(List listaEtichette) {
		this.listaEtichette = listaEtichette;
	}

	public void setErrori(List<String> errori) {
		this.errori = errori;
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

	public StampaDiffVO getStampaDiffEtichette() {
		return stampaDiffEtichette;
	}

	public void setStampaDiffEtichette(StampaDiffVO stampaDiffEtichette) {
		this.stampaDiffEtichette = stampaDiffEtichette;
	}

	public String getDescrBibEtichetta() {
		return descrBibEtichetta;
	}

	public void setDescrBibEtichetta(String descrBibEtichetta) {
		this.descrBibEtichetta = descrBibEtichetta;
	}
}
