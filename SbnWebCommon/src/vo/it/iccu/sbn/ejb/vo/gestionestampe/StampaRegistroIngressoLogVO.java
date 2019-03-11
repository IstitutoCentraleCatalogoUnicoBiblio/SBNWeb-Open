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

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

public class StampaRegistroIngressoLogVO extends StampaRegistroVO {

	private static final long serialVersionUID = -2524467543599242958L;

	public static final String LOG = "LOG";

	private String nroInvLog;
	private String chkDataInventario;
	private String chkTipoOrdine;
	private String chkValore;
	private String chkBid;
	private String chkCollocazione;// no output
	private String chkCodeInventario;
	private String chkTitolo;
	private String chkPrecisazioni;
	private String chkNroFattura;// no output
	private String chkCodeFornitore;
	private String chkCodeMateriale;
	private String chkImporto;// o prezzo
	private String chkDataFattura;// no output
	private String chkCodBib;
	private String chkCodSerie;
	private String chkProvenienza;
	private String chkTAcq;

	private boolean ricLocale;
	private boolean ricLocale_old;
	private boolean ricIndice;
	private boolean ricIndice_old;
	private String intestazione;

	public StampaRegistroIngressoLogVO(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public StampaRegistroIngressoLogVO(String codPolo, String codeBiblioteca, String serieInv, String codeInvDa,
			String codeInvA, String dataDa, String dataA, String registro,
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(codPolo, codeBiblioteca, serieInv, codeInvDa, codeInvA, dataDa, dataA, registro, input);
	}

	public StampaRegistroIngressoLogVO(String codPolo, String codeBiblioteca, String serieInv, String codeInvDa,
			String codeInvA, String dataDa, String dataA, String registro, String chkDataInventario,
			String chkTipoOrdine, String chkValore, String chkBid, String chkCollocazione, String chkCodeInventario,
			String chkTitolo, String chkPrecisazioni, String chkNroFattura, String chkCodeFornitore,
			String chkCodeMateriale, String chkImporto, String chkDataFattura, String chkCodBib, String chkCodSerie,
			String chkProvenienza, String chkTAcq, ParametriRichiestaElaborazioneDifferitaVO input) {
		super(codPolo, codeBiblioteca, serieInv, codeInvDa, codeInvA, dataDa, dataA, registro, input);
		this.chkDataInventario = chkDataInventario;
		this.chkTipoOrdine = chkTipoOrdine;
		this.chkValore = chkValore;
		this.chkBid = chkBid;
		this.chkCollocazione = chkCollocazione;// no output
		this.chkCodeInventario = chkCodeInventario;
		this.chkTitolo = chkTitolo;
		this.chkPrecisazioni = chkPrecisazioni;
		this.chkNroFattura = chkNroFattura;// no output
		this.chkCodeFornitore = chkCodeFornitore;
		this.chkCodeMateriale = chkCodeMateriale;
		this.chkImporto = chkImporto;
		this.chkDataFattura = chkDataFattura;// no output
		this.chkCodBib = chkCodBib;
		this.chkCodSerie = chkCodSerie;
		this.chkProvenienza = chkProvenienza;
		this.chkTAcq = chkTAcq;
	}

	public boolean isRicIndice() {
		return ricIndice;
	}

	public void setRicIndice(boolean ricIndice) {
		this.ricIndice = ricIndice;
	}

	public boolean isRicLocale() {
		return ricLocale;
	}

	public void setRicLocale(boolean ricLocale) {
		this.ricLocale = ricLocale;
	}

	public boolean isRicIndice_old() {
		return ricIndice_old;
	}

	public void setRicIndice_old(boolean ricIndice_old) {
		this.ricIndice_old = ricIndice_old;
	}

	public boolean isRicLocale_old() {
		return ricLocale_old;
	}

	public void setRicLocale_old(boolean ricLocale_old) {
		this.ricLocale_old = ricLocale_old;
	}

	public void save() {
		this.ricLocale_old = this.ricLocale;
		this.ricIndice_old = this.ricIndice;
	}

	public void restore() {
		this.ricLocale = this.ricLocale_old;
		this.ricIndice = this.ricIndice_old;
	}

	public String getChkCodBib() {
		return chkCodBib;
	}

	public void setChkCodBib(String chkCodBib) {
		this.chkCodBib = chkCodBib;
	}

	public String getChkCodSerie() {
		return chkCodSerie;
	}

	public void setChkCodSerie(String chkCodSerie) {
		this.chkCodSerie = chkCodSerie;
	}

	public String getChkProvenienza() {
		return chkProvenienza;
	}

	public void setChkProvenienza(String chkProvenienza) {
		this.chkProvenienza = chkProvenienza;
	}

	public String getChkDataInventario() {
		return chkDataInventario;
	}

	public void setChkDataInventario(String chkDataInventario) {
		this.chkDataInventario = chkDataInventario;
	}

	public String getChkTipoOrdine() {
		return chkTipoOrdine;
	}

	public void setChkTipoOrdine(String chkTipoOrdine) {
		this.chkTipoOrdine = chkTipoOrdine;
	}

	public String getChkValore() {
		return chkValore;
	}

	public void setChkValore(String chkValore) {
		this.chkValore = chkValore;
	}

	public String getChkBid() {
		return chkBid;
	}

	public void setChkBid(String chkBid) {
		this.chkBid = chkBid;
	}

	public String getChkCollocazione() {
		return chkCollocazione;
	}

	public void setChkCollocazione(String chkCollocazione) {
		this.chkCollocazione = chkCollocazione;
	}

	public String getChkCodeInventario() {
		return chkCodeInventario;
	}

	public void setChkCodeInventario(String chkCodeInventario) {
		this.chkCodeInventario = chkCodeInventario;
	}

	public String getChkTitolo() {
		return chkTitolo;
	}

	public void setChkTitolo(String chkTitolo) {
		this.chkTitolo = chkTitolo;
	}

	public String getChkPrecisazioni() {
		return chkPrecisazioni;
	}

	public void setChkPrecisazioni(String chkPrecisazioni) {
		this.chkPrecisazioni = chkPrecisazioni;
	}

	public String getChkNroFattura() {
		return chkNroFattura;
	}

	public void setChkNroFattura(String chkNroFattura) {
		this.chkNroFattura = chkNroFattura;
	}

	public String getChkCodeFornitore() {
		return chkCodeFornitore;
	}

	public void setChkCodeFornitore(String chkCodeFornitore) {
		this.chkCodeFornitore = chkCodeFornitore;
	}

	public String getChkCodeMateriale() {
		return chkCodeMateriale;
	}

	public void setChkCodeMateriale(String chkCodeMateriale) {
		this.chkCodeMateriale = chkCodeMateriale;
	}

	public String getChkImporto() {
		return chkImporto;
	}

	public void setChkImporto(String chkImporto) {
		this.chkImporto = chkImporto;
	}

	public String getChkDataFattura() {
		return chkDataFattura;
	}

	public void setChkDataFattura(String chkDataFattura) {
		this.chkDataFattura = chkDataFattura;
	}

	public String getChkTAcq() {
		return chkTAcq;
	}

	public void setChkTAcq(String chkTAcq) {
		this.chkTAcq = chkTAcq;
	}

	public String getIntestazione() {
		return intestazione;
	}

	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
	}

	public String toString() {
		String stringaOggetto = "";
		stringaOggetto = "codPolo: " + this.getCodPolo() + '\r' + '\n' + "codeBiblioteca: " + this.getCodeBiblioteca()
				+ '\r' + '\n' + "serieInv: " + this.getSerieInv() + '\r' + '\n' + "codeInvDa: " + this.getCodeInvDa()
				+ '\r' + '\n' + "codeInvA: " + this.getCodeInvA() + '\r' + '\n' + "dataDa: " + this.getDataDa() + '\r'
				+ '\n' + "dataA: " + this.getDataA() + '\r' + '\n' + "registro: " + this.getRegistro() + '\r' + '\n' +

				// "dataIns: "+this.getDataIns()+'\r'+'\n'+ "nroInv:
				// "+this.getNroInv()+'\r'+'\n'+
				// "descrProven: "+this.getDescrProven()+'\r'+'\n'+"codTipoAcq:
				// "+this.getCodTipoAcq()+'\r'+'\n'+"bid:
				// "+this.getBid()+'\r'+'\n'+
				// "titolo: "+this.getTitolo()+'\r'+'\n'+"codTipoMat:
				// "+this.getCodTipoMat()+'\r'+'\n'+"valore:
				// "+this.getValore()+'\r'+'\n'+

				// "precisazione: "+this.getPrecisazione()+'\r'+'\n'+ "prezzo:
				// "+this.getPrezzo()+'\r'+'\n'+
				// "numFattura: "+this.getNumFattura()+'\r'+'\n'+"dataFattura:
				// "+this.getDataFattura()+'\r'+'\n'+"codCollocazione:
				// "+this.getCodCollocazione()+'\r'+'\n'+
				// "data: "+this.getData()+'\r'+'\n'+

				"chkData inventario: " + this.getChkDataInventario() + '\r' + '\n' + "chkTipoOrdine: "
				+ this.getChkTipoOrdine() + '\r' + '\n' + "chkValore: " + this.getChkValore() + '\r' + '\n' + "chkBid: "
				+ this.getChkBid() + '\r' + '\n' + "chkCollocazione: " + this.getChkCollocazione() + '\r' + '\n'
				+ "chkCodeInventario: " + this.getChkCodeInventario() + '\r' + '\n' + "chkTitolo: "
				+ this.getChkTitolo() + '\r' + '\n' + "chkPrecisazioni: " + this.getChkPrecisazioni() + '\r' + '\n'
				+ "chkNroFattura: " + this.getChkNroFattura() + '\r' + '\n' + "chkCodeFornitore: " + '\r' + '\n'
				+ this.getChkCodeFornitore() + '\r' + '\n' + "chkCodeMateriale: " + this.getChkCodeMateriale() + '\r'
				+ '\n' + "chkImporto: " + this.getChkImporto() + '\r' + '\n' + "chkDataFattura: "
				+ this.getChkDataFattura() + '\r' + '\n' + "chkCodBib: " + this.getChkCodBib() + '\r' + '\n'
				+ "chkCodSerie: " + this.getChkCodSerie();
		return stringaOggetto;
	}

	public String getNroInvLog() {
		return nroInvLog;
	}

	public void setNroInvLog(String nroInvLog) {
		this.nroInvLog = nroInvLog;
	}
}
