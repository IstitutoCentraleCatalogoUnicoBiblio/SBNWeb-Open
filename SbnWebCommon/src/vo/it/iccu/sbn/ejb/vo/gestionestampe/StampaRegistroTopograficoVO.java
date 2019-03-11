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

import java.util.ArrayList;
import java.util.List;

/**
 * @author VO per la stampa del registro Topografico.Struttura utilizzata sia in
 *         input che in output a fronte della valorizzazione dell'attributo
 *         lista che conterra il dettaglio della stampa
 */
public class StampaRegistroTopograficoVO extends DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = -7066350997985012826L;

	public static final String COLLOCAZIONI = "COLLOCAZIONI";

	public static final String INVENTARI = "INVENTARI";

	// Attributes
	private String descrBib;

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	private int sizeColl;
	private SubReportVO recCollocazione = null;

	/**
	 *
	 */

	public StampaRegistroTopograficoVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}

	public StampaRegistroTopograficoVO(String codPolo, String codeBiblioteca,
			String descrBib, String sez, String coll1, String spec1,
			String coll2, String spec2,
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
		this.codPolo = codPolo;
		this.codBib = codeBiblioteca;
		this.descrBib = descrBib;
		this.sezione = sez;
		this.dallaCollocazione = coll1;
		this.dallaSpecificazione = spec1;
		this.allaCollocazione = coll2;
		this.allaSpecificazione = spec2;
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

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
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

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public void setCollocazioniCount(int sizeColl) {
		this.sizeColl = sizeColl;
	}

	public int getCollocazioniCount() {
		return sizeColl;
	}

	public SubReportVO getRecCollocazione() {
		return recCollocazione;
	}

	public void setRecCollocazione(SubReportVO collocazione) {
		this.recCollocazione = collocazione;
	}


}
