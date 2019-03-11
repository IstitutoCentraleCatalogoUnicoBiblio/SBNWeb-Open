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

import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.List;

public class StatisticheVO extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -4303641598986846870L;

	private String tipoOperazione;

	private List<Integer> datiInput;

	private String ticket;

	private List<String> errori = new ArrayList<String>();

	private String msg;

	private boolean adeguamentoPrezzo = false;

	private boolean servizioPicos = false;

	private String kanno = ""; // anno_abb per i rinnovi PICOS

	private double prezzoBil = 0.00; // prezzo ordine per i rinnovi PICOS

	private int annoBil = 0; // anno su cui imputare il bilancio per i rinnovi
								// PICOS

	private int capitoloBil = 0; // anno su cui imputare il capitolo per i
									// rinnovi PICOS

	private boolean esitoRinnovoPicos = false;

	private boolean soloCreazioneInventari = false;

	private boolean aggiornamentoRinnovo = true;

	private StrutturaTerna ordineNuovo = null;

	private ParametriExcelVO parExc = new ParametriExcelVO();

	public StatisticheVO() {
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

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public List<Integer> getDatiInput() {
		return datiInput;
	}

	public void setDatiInput(List<Integer> datiInput) {
		this.datiInput = datiInput;
	}

	public boolean isAdeguamentoPrezzo() {
		return adeguamentoPrezzo;
	}

	public void setAdeguamentoPrezzo(boolean adeguamentoPrezzo) {
		this.adeguamentoPrezzo = adeguamentoPrezzo;
	}

	public String getKanno() {
		return kanno;
	}

	public void setKanno(String kanno) {
		this.kanno = kanno;
	}

	public int getAnnoBil() {
		return annoBil;
	}

	public void setAnnoBil(int annoBil) {
		this.annoBil = annoBil;
	}

	public boolean isServizioPicos() {
		return servizioPicos;
	}

	public void setServizioPicos(boolean servizioPicos) {
		this.servizioPicos = servizioPicos;
	}

	public double getPrezzoBil() {
		return prezzoBil;
	}

	public void setPrezzoBil(double prezzoBil) {
		this.prezzoBil = prezzoBil;
	}

	public boolean isEsitoRinnovoPicos() {
		return esitoRinnovoPicos;
	}

	public void setEsitoRinnovoPicos(boolean esitoRinnovoPicos) {
		this.esitoRinnovoPicos = esitoRinnovoPicos;
	}

	public StrutturaTerna getOrdineNuovo() {
		return ordineNuovo;
	}

	public void setOrdineNuovo(StrutturaTerna ordineNuovo) {
		this.ordineNuovo = ordineNuovo;
	}

	public boolean isSoloCreazioneInventari() {
		return soloCreazioneInventari;
	}

	public void setSoloCreazioneInventari(boolean soloCreazioneInventari) {
		this.soloCreazioneInventari = soloCreazioneInventari;
	}

	public boolean isAggiornamentoRinnovo() {
		return aggiornamentoRinnovo;
	}

	public void setAggiornamentoRinnovo(boolean aggiornamentoRinnovo) {
		this.aggiornamentoRinnovo = aggiornamentoRinnovo;
	}

	public int getCapitoloBil() {
		return capitoloBil;
	}

	public void setCapitoloBil(int capitoloBil) {
		this.capitoloBil = capitoloBil;
	}

	public ParametriExcelVO getParExc() {
		return parExc;
	}

	public void setParExc(ParametriExcelVO parExc) {
		this.parExc = parExc;
	}

}
