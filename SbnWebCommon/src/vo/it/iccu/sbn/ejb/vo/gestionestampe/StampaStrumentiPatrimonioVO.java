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
import java.util.Locale;

/**
 * @author VO per la stampa del registro Topografico.Struttura utilizzata sia in
 *         input che in output a fronte della valorizzazione dell'attributo
 *         lista che conterra il dettaglio della stampa
 */
public class StampaStrumentiPatrimonioVO extends
		DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = -7066350997985012826L;

	// Attributes
	private List<StampaStrumentiPatrimonioDettaglioVO> lista = new ArrayList<StampaStrumentiPatrimonioDettaglioVO>();

	private String descrBib;

	protected Locale locale = Locale.getDefault();
	protected String numberFormat = "###,###,###,##0.00";
	private int contInput;
	private int contOutput;
	private String tipoOrdinamento;
	private String esemplariTitoli;

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	private String dataPrimaCollDa;
	private String dataPrimaCollA;

	//almaviva5_20131003 evolutive google2
	private boolean escludiDigit;
	private String tipoDigit;

	// almaviva2 Settembre 2017: modifica a Stampa Strumenti Patrimonio per consentire la stampa per una lista di titoli proveniente
	// da file o dalla valorizzazione dei campindi bid sulla mappa
	private List listaBid;

	public StampaStrumentiPatrimonioVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public StampaStrumentiPatrimonioVO(String codPolo, String codeBiblioteca,
			String sez, String coll1, String spec1, String coll2, String spec2,
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
		this.codPolo = codPolo;
		this.codBib = codeBiblioteca;
		this.sezione = sez;
		this.dallaCollocazione = coll1;
		this.dallaSpecificazione = spec1;
		this.allaCollocazione = coll2;
		this.allaSpecificazione = spec2;
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

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	public int getContInput() {
		return contInput;
	}

	public void setContInput(int contInput) {
		this.contInput = contInput;
	}

	public int getContOutput() {
		return contOutput;
	}

	public void setContOutput(int contOutput) {
		this.contOutput = contOutput;
	}

	public List<StampaStrumentiPatrimonioDettaglioVO> getLista() {
		return lista;
	}

	public void setLista(List<StampaStrumentiPatrimonioDettaglioVO> lista) {
		this.lista = lista;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getEsemplariTitoli() {
		return esemplariTitoli;
	}

	public void setEsemplariTitoli(String esemplariTitoli) {
		this.esemplariTitoli = esemplariTitoli;
	}

	public String getDataPrimaCollDa() {
		return dataPrimaCollDa;
	}

	public void setDataPrimaCollDa(String dataPrimaCollDa) {
		this.dataPrimaCollDa = dataPrimaCollDa;
	}

	public String getDataPrimaCollA() {
		return dataPrimaCollA;
	}

	public void setDataPrimaCollA(String dataPrimaCollA) {
		this.dataPrimaCollA = dataPrimaCollA;
	}

	public boolean isRangeIngresso() {
		String dataDa = this.getDataDa();
		String dataA = this.getDataA();
		return isFilled(dataDa) || isFilled(dataA);
	}

	public boolean isRangePrimaColl() {
		String dataPrimaCollDa = this.getDataPrimaCollDa();
		String dataPrimaCollA = this.getDataPrimaCollA();
		return isFilled(dataPrimaCollDa) || isFilled(dataPrimaCollA);
	}

	public String getFrom() {
		final String dataDa = getDataDa();
		return isFilled(dataDa) ? dataDa : isFilled(dataPrimaCollDa) ? dataPrimaCollDa : null;
	}

	public String getTo() {
		final String dataA = getDataA();
		return isFilled(dataA) ? dataA : isFilled(dataPrimaCollA) ? dataPrimaCollA : null;
	}

	public boolean isEscludiDigit() {
		return escludiDigit;
	}

	public void setEscludiDigit(boolean escludiDigit) {
		this.escludiDigit = escludiDigit;
	}

	public String getTipoDigit() {
		return tipoDigit;
	}

	public void setTipoDigit(String tipoDigit) {
		this.tipoDigit = tipoDigit;
	}

	public List getListaBid() {
		return listaBid;
	}

	public void setListaBid(List listaBid) {
		this.listaBid = listaBid;
	}

	public List addListaBid(String newBid) {
		listaBid.add(newBid);
		return listaBid;
	}
}
