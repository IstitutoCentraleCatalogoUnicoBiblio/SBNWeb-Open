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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * VO per il dettaglio della stampa registro topografico
 */
public class StampaStrumentiPatrimonioDettaglioVO extends
		StampaStrumentiPatrimonioVO {

	private static final long serialVersionUID = -543900038710172232L;

	// ////////////////////////
	private String codBibSez = "";
	private String sezione = "";
	private String collocazione = "";
	private String specificazione = "";
	private String sequenza = "";
	private String ordLoc = "";
	private String ordSpec = "";
	private String keyLoc = "";
	private String codBibSerie = "";
	private String serie = "";
	private String inventario = "";
	private String consistenzaColl = "";
	private Date dataIngressoDate;
	private String prezzo = "";
	private String valore = "";
	private String provenienza = "";
	private String codTipoAcq = "";
	private String descrTipoAcq = "";
	private String codTipoMat = "";
	private String descrTipoMat = "";
	private String precisazione = "";
	private String fruibilita = "";
	private String descrFruibilita = "";
	private String motivoNoDisp = "";
	private String descrMotivoNoDisp = "";
	private String statoConservazione = "";
	private String descrStatoConservazione = "";
	private String tipoDigitalizzazione = "";
	private String copiaDigitale = "";
	private String descrDigitalizzazione = "";
	private String descrCopiaDigitale = "";
	private String bidInv = "";
	private String bidDescr = "";
	private String bidKyCles2t = "";
	private String bidDescrSup = "";
	private String natura = "";
	private String barcodeInv = "";
	private String vid = "";
	private String descrVid = "";
	private String descrVidSecondari = "";
	private String dataPubbl = "";
	// private Date dataPubblDate = "";
	private String tipoRecord = "";
	private String simboloClassificazione = "";
	private String codSistemaEdizione = "";
	private String primoSoggetto = "";
	private String codiceSoggettario = "";
	private String edizione = "";
	private String paese = "";
	private String lingua = "";
	private String descrPaese = "";
	private String descrLingua = "";
	private String motivoPrelievo = "";
	private String dataPrelievo = "";
	private String area215 = "";
	private List<CodiceVO> soggetti = new ArrayList<CodiceVO>();
	private List<CodiceVO> indiciClassificazione = new ArrayList<CodiceVO>();

	private boolean collocato;
	private String bidColl;

	public StampaStrumentiPatrimonioDettaglioVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public StampaStrumentiPatrimonioDettaglioVO(String codBibSez,
			String sezione, String collocazione, String specificazione,
			String sequenza, String ordLoc, String ordSpec, String keyLoc,
			String codBibSerie, String serie, String inventario,
			String consistenzaColl, Date dataIngressoDate, String prezzo,
			String prezzoDouble, String valore, String valoreDouble,
			String provenienza, String codTipoAcq, String descrTipoAcq,
			String codTipoMat, String descrTipoMat, String precisazione,
			String fruibilita, String descrFruibilita, String motivoNoDisp,
			String descrMotivoNoDisp, String statoConservazione,
			String descrStatoConservazione, String tipoDigitalizzazione,
			String copiaDigitale, String descrDigitalizzazione,
			String descrCopiaDigitale, String bidInv, String bidDescr,
			String bidKyCles2t, String bidDescrSup, String natura, String vid,
			String barcodeInv, String descrVid, String descrVidSecondari,
			String dataPubbl,/* Date dataPubblDate, */String tipoRecord,
			String simboloClassificazione, String codSistemaEdizione,
			String primoSoggetto, String codiceSoggettario, String edizione,
			String paese, String lingua, String descrPaese, String descrLingua,
			String motivoPrel, String dataPrel, String area215,
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
		this.codBibSez = codBibSez;
		this.sezione = sezione;
		this.collocazione = collocazione;
		this.specificazione = specificazione;
		this.sequenza = sequenza;
		this.ordLoc = ordLoc; // non stampare
		this.ordSpec = ordSpec; // non stampare
		this.keyLoc = keyLoc; // non stampare
		this.codBibSerie = codBibSerie;
		this.serie = serie;
		this.inventario = inventario;
		this.consistenzaColl = consistenzaColl;
		this.dataIngressoDate = dataIngressoDate;
		this.prezzo = prezzo;
		this.valore = valore;
		this.provenienza = provenienza;
		this.codTipoAcq = codTipoAcq;
		this.descrTipoAcq = descrTipoAcq;
		this.codTipoMat = codTipoMat;
		this.descrTipoMat = descrTipoMat;
		this.precisazione = precisazione;
		this.fruibilita = fruibilita;
		this.descrFruibilita = descrFruibilita;
		this.motivoNoDisp = motivoNoDisp;
		this.descrMotivoNoDisp = descrMotivoNoDisp;
		this.statoConservazione = statoConservazione;
		this.descrStatoConservazione = descrStatoConservazione;
		this.tipoDigitalizzazione = tipoDigitalizzazione;
		this.copiaDigitale = copiaDigitale;
		this.descrDigitalizzazione = descrDigitalizzazione;
		this.descrCopiaDigitale = descrCopiaDigitale;
		this.bidInv = bidInv;
		this.bidDescr = bidDescr;
		this.bidKyCles2t = bidKyCles2t; // non stampare
		this.bidDescrSup = bidDescrSup;
		this.natura = natura;
		this.barcodeInv = barcodeInv;
		this.vid = vid;
		this.descrVid = descrVid;
		this.descrVidSecondari = descrVidSecondari;
		this.dataPubbl = dataPubbl;
		// this.dataPubblDate = dataPubblDate;
		this.tipoRecord = tipoRecord;
		this.simboloClassificazione = simboloClassificazione;
		this.codSistemaEdizione = codSistemaEdizione;
		this.primoSoggetto = primoSoggetto;
		this.codiceSoggettario = codiceSoggettario;
		this.edizione = edizione;
		this.paese = paese;
		this.lingua = lingua;
		this.descrPaese = descrPaese;
		this.descrLingua = descrLingua;
		this.dataPrelievo = dataPrel;
		this.motivoPrelievo = motivoPrel;
		this.area215 = area215;
	}

	public String getOrdLoc() {
		return ordLoc;
	}

	public void setOrdLoc(String ordLoc) {
		this.ordLoc = ordLoc;
	}

	public String getKeyLoc() {
		return keyLoc;
	}

	public void setKeyLoc(String keyLoc) {
		this.keyLoc = keyLoc;
	}

	public String getBidInv() {
		return bidInv;
	}

	public void setBidInv(String bidInv) {
		this.bidInv = bidInv;
	}

	public String getBidKyCles2t() {
		return bidKyCles2t;
	}

	public void setBidKyCles2t(String bidKyCles2t) {
		this.bidKyCles2t = bidKyCles2t;
	}

	public String getBidDescrSup() {
		return bidDescrSup;
	}

	public void setBidDescrSup(String bidDescrSup) {
		this.bidDescrSup = bidDescrSup;
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public String getSequenza() {
		return sequenza;
	}

	public void setSequenza(String sequenza) {
		this.sequenza = sequenza;
	}

	public String getPrecisazione() {
		return precisazione;
	}

	public void setPrecisazione(String precisazione) {
		this.precisazione = precisazione;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getStatoConservazione() {
		return statoConservazione;
	}

	public void setStatoConservazione(String statoConservazione) {
		this.statoConservazione = statoConservazione;
	}

	public String getDescrStatoConservazione() {
		return descrStatoConservazione;
	}

	public void setDescrStatoConservazione(String descrStatoConservazione) {
		this.descrStatoConservazione = descrStatoConservazione;
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getSpecificazione() {
		return specificazione;
	}

	public void setSpecificazione(String specificazione) {
		this.specificazione = specificazione;
	}

	public String getOrdSpec() {
		return ordSpec;
	}

	public void setOrdSpec(String ordSpec) {
		this.ordSpec = ordSpec;
	}

	public String getCodTipoAcq() {
		return codTipoAcq;
	}

	public void setCodTipoAcq(String codTipoAcq) {
		this.codTipoAcq = codTipoAcq;
	}

	public String getDescrTipoAcq() {
		return descrTipoAcq;
	}

	public void setDescrTipoAcq(String descrTipoAcq) {
		this.descrTipoAcq = descrTipoAcq;
	}

	public String getCodTipoMat() {
		return codTipoMat;
	}

	public void setCodTipoMat(String codTipoMat) {
		this.codTipoMat = codTipoMat;
	}

	public String getDescrTipoMat() {
		return descrTipoMat;
	}

	public void setDescrTipoMat(String descrTipoMat) {
		this.descrTipoMat = descrTipoMat;
	}

	public String getFruibilita() {
		return fruibilita;
	}

	public void setFruibilita(String fruibilita) {
		this.fruibilita = fruibilita;
	}

	public String getDescrFruibilita() {
		return descrFruibilita;
	}

	public void setDescrFruibilita(String descrFruibilita) {
		this.descrFruibilita = descrFruibilita;
	}

	public String getMotivoNoDisp() {
		return motivoNoDisp;
	}

	public void setMotivoNoDisp(String motivoNoDisp) {
		this.motivoNoDisp = motivoNoDisp;
	}

	public String getDescrMotivoNoDisp() {
		return descrMotivoNoDisp;
	}

	public void setDescrMotivoNoDisp(String descrMotivoNoDisp) {
		this.descrMotivoNoDisp = descrMotivoNoDisp;
	}

	public String getTipoDigitalizzazione() {
		return tipoDigitalizzazione;
	}

	public void setTipoDigitalizzazione(String tipoDigitalizzazione) {
		this.tipoDigitalizzazione = tipoDigitalizzazione;
	}

	public String getCopiaDigitale() {
		return copiaDigitale;
	}

	public void setCopiaDigitale(String copiaDigitale) {
		this.copiaDigitale = copiaDigitale;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getBarcodeInv() {
		return barcodeInv;
	}

	public void setBarcodeInv(String barcodeInv) {
		this.barcodeInv = barcodeInv;
	}

	public String getDescrVid() {
		return descrVid;
	}

	public void setDescrVid(String descrVid) {
		this.descrVid = descrVid;
	}

	public String getDescrVidSecondari() {
		return descrVidSecondari;
	}

	public void setDescrVidSecondari(String descrVidSecondari) {
		this.descrVidSecondari = descrVidSecondari;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getCodSistemaEdizione() {
		return codSistemaEdizione;
	}

	public void setCodSistemaEdizione(String codSistemaEdizione) {
		this.codSistemaEdizione = codSistemaEdizione;
	}

	public String getPrimoSoggetto() {
		return primoSoggetto;
	}

	public void setPrimoSoggetto(String primoSoggetto) {
		this.primoSoggetto = primoSoggetto;
	}

	public String getEdizione() {
		return edizione;
	}

	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	//
	public double getValoreDouble() throws ParseException {
		return getValore(numberFormat, locale);
	}

	public double getValore(String format, Locale locale) throws ParseException {
		return ValidazioneDati
				.getDoubleFromString(valore, format, locale, 9, 2);
	}

	public void setValoreDouble(double valore) {
		setValore(valore, numberFormat, locale);
	}

	public void setValore(double valore, String format, Locale locale) {
		this.valore = ValidazioneDati.getStringFromDouble(valore, format,
				locale);
	}

	//
	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public double getPrezzoDouble() throws ParseException {
		return getPrezzo(numberFormat, locale);
	}

	public double getPrezzo(String format, Locale locale) throws ParseException {
		return ValidazioneDati
				.getDoubleFromString(prezzo, format, locale, 9, 2);
	}

	public void setPrezzoDouble(double prezzo) {
		setPrezzo(prezzo, numberFormat, locale);
	}

	public void setPrezzo(double prezzo, String format, Locale locale) {
		this.prezzo = ValidazioneDati.getStringFromDouble(prezzo, format,
				locale);
	}

	public Date getDataIngressoDate() {
		return dataIngressoDate;
	}

	public void setDataIngressoDate(Date dataIngressoDate) {
		this.dataIngressoDate = dataIngressoDate;
	}

	public String getDataPubbl() {
		return dataPubbl;
	}

	public void setDataPubbl(String dataPubbl) {
		this.dataPubbl = dataPubbl;
	}

	public String getBidDescr() {
		return bidDescr;
	}

	public void setBidDescr(String bidDescr) {
		this.bidDescr = bidDescr;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getConsistenzaColl() {
		return consistenzaColl;
	}

	public void setConsistenzaColl(String consistenzaColl) {
		this.consistenzaColl = consistenzaColl;
	}

	public String getSerie() {
		return serie;
	}

	public String getCodBibSez() {
		return codBibSez;
	}

	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getMotivoPrelievo() {
		return motivoPrelievo;
	}

	public void setMotivoPrelievo(String motivoPrelievo) {
		this.motivoPrelievo = motivoPrelievo;
	}

	public String getDataPrelievo() {
		return dataPrelievo;
	}

	public void setDataPrelievo(String dataPrelievo) {
		this.dataPrelievo = dataPrelievo;
	}

	public String getArea215() {
		return area215;
	}

	public void setArea215(String area215) {
		this.area215 = area215;
	}

	public List<CodiceVO> getSoggetti() {
		return soggetti;
	}

	public void setSoggetti(List<CodiceVO> soggetti) {
		this.soggetti = soggetti;
	}

	public List<CodiceVO> getIndiciClassificazione() {
		return indiciClassificazione;
	}

	public void setIndiciClassificazione(
			List<CodiceVO> indiciClassificazione) {
		this.indiciClassificazione = indiciClassificazione;
	}

	public boolean isCollocato() {
		return collocato;
	}

	public void setCollocato(boolean collocato) {
		this.collocato = collocato;
	}

	public String getBidColl() {
		return bidColl;
	}

	public void setBidColl(String bidColl) {
		this.bidColl = bidColl;
	}

	public String getDescrDigitalizzazione() {
		return descrDigitalizzazione;
	}

	public void setDescrDigitalizzazione(String descrDigitalizzazione) {
		this.descrDigitalizzazione = descrDigitalizzazione;
	}

	public String getDescrCopiaDigitale() {
		return descrCopiaDigitale;
	}

	public void setDescrCopiaDigitale(String descrCopiaDigitale) {
		this.descrCopiaDigitale = descrCopiaDigitale;
	}

	public String getDescrPaese() {
		return descrPaese;
	}

	public void setDescrPaese(String descrPaese) {
		this.descrPaese = descrPaese;
	}

	public String getDescrLingua() {
		return descrLingua;
	}

	public void setDescrLingua(String descrLingua) {
		this.descrLingua = descrLingua;
	}

	public String getSimboloClassificazione() {
		return simboloClassificazione;
	}

	public void setSimboloClassificazione(String simboloClassificazione) {
		this.simboloClassificazione = simboloClassificazione;
	}

	public String getCodiceSoggettario() {
		return codiceSoggettario;
	}

	public void setCodiceSoggettario(String codiceSoggettario) {
		this.codiceSoggettario = codiceSoggettario;
	}

	public String getCodBibSerie() {
		return codBibSerie;
	}

	public void setCodBibSerie(String codBibSerie) {
		this.codBibSerie = codBibSerie;
	}

	public String getChiaveCollocazione() {
		StringBuilder buf = new StringBuilder(256);
		buf.append(trimOrBlank(sezione));
		buf.append(" ");
		buf.append(trimOrBlank(collocazione));
		buf.append(" ");
		buf.append(trimOrBlank(specificazione));
		buf.append(" ");
		buf.append(trimOrBlank(sequenza));

		return buf.toString();
	}
}
