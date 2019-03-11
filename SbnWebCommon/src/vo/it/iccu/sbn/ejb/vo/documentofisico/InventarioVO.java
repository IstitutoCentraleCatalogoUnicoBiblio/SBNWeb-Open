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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.extension.rfid.KeyInventario;
import it.iccu.sbn.util.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InventarioVO extends SerializableVO implements KeyInventario {

	private static final long serialVersionUID = -7405611198584565833L;

	private String codPolo;
	private String codBib;
	private String codSerie;
	private int codInvent;
	private String codProven;
	private String codPoloProven;
	private String codBibProven;
	private int keyLoc;
	private String bid;
	private String valore;
	private String importo;
	private String numVol;
	private String totLoc;
	private String totInter;
	private String seqColl;
	private String precInv;
	private String annoAbb;
	private String flagDisp;
	private String flagNU;
	private String statoConser;
	private String codFornitore;
	private String codMatInv;
	private String codSit;
	private String sitAmm;
	private String codBibO;
	private String codTipoOrd;
	private String annoOrd;
	private String codOrd;
	private String rigaOrd;
	private String codBibF;
	private String annoFattura;
	private String dataFattura;
	private String numFattura;
	private String progrFattura;
	private String rigaFattura;
	private String codNoDisp;
	private String codFrui;
	private String codPoloScar;
	private String codBibS;
	private String codScarico;
	private String numScarico;
	private String codCarico;
	private String numCarico;
	private String dataCarico;
	private String deliberaScarico;
	private String dataScarico;
	private String dataDelibScar;
	private String sezOld;
	private String locOld;
	private String specOld;
	private String keyLocOld;
	private String cancDB2i;
	private String codSupporto;
	private String dataInsPrimaColl;
	private String uteInsPrimaColl;
	private String dataIns;
	private String dataAgg;
	private String uteIns;
	private String uteAgg;
	// campi di appoggio in fase di insertInvDaControllare
	private String cambioBid;
	private String cambioIsbn;
	// numero di inventari da creare da ordine
	private int numInv;
	private String natura;
	//
	private String descrProven;
	private String descrFornitore;
	private String numOrdine;

	private Locale locale = Locale.getDefault();
	// private String numberFormat="########0.00";
	private String numberFormat = "###,###,###,##0.00";

	// evolutiva
	private String tipoAcquisizione;
	private String supportoCopia;
	private String digitalizzazione;
	private String dispDaRemoto;
	private String idAccessoRemoto;
	private String rifTecaDigitale;
	private String codRiproducibilita;
	private String dataIngresso;
	private String dataRedisp;

	private int idBibScar;
	private String versoBibDescr;
	private String motivoScaricoDescr;

	private String gruppoFascicolo;

	private String descrDispDaRemoto;
	private String descrRifTecaDigitale;
	private List<NotaInventarioVO> listaNote = new ArrayList<NotaInventarioVO>();

	public List<NotaInventarioVO> getListaNote() {
		return listaNote;
	}

	public void setListaNote(List<NotaInventarioVO> listaNote) {
		this.listaNote = listaNote;
	}

	public String getDescrRifTecaDigitale() {
		return descrRifTecaDigitale;
	}

	public void setDescrRifTecaDigitale(String descrRifTecaDigitale) {
		this.descrRifTecaDigitale = descrRifTecaDigitale;
	}

	public InventarioVO() {
	}

	public InventarioVO(KeyInventario kinv) {
		this.codPolo = kinv.getCodPolo();
		this.codBib = kinv.getCodBib();
		this.codSerie = kinv.getCodSerie();
		this.codInvent = kinv.getCodInvent();
	}

	public InventarioVO(InventarioVO inv) throws Exception {
		// sostituisce tutto l'asteriscato
		copyCommonProperties(this, inv);
		// this.codPolo = inv.codPolo;
		// this.codBib = inv.codBib;
		// this.codSerie = inv.codSerie;
		// this.codInvent = inv.codInvent;
		// this.codProven = inv.codProven;
		// this.codPoloProven = inv.codPoloProven;
		// this.codBibProven = inv.codBibProven;
		// this.keyLoc = inv.keyLoc;
		// this.bid = inv.bid;
		// this.valore = inv.valore;
		// this.importo = inv.importo;
		// this.numVol = inv.numVol;
		// this.totLoc = inv.totLoc;
		// this.totInter = inv.totInter;
		// this.seqColl = inv.seqColl;
		// this.precInv = inv.precInv;
		// this.annoAbb = inv.annoAbb;
		// this.flagDisp = inv.flagDisp;
		// this.flagNU = inv.flagNU;
		// this.statoConser = inv.statoConser;
		// this.codFornitore = inv.codFornitore;
		// this.codMatInv = inv.codMatInv;
		// this.codSit = inv.codSit;
		// this.codBibO = inv.codBibO;
		// this.codTipoOrd = inv.codTipoOrd;
		// this.annoOrd = inv.annoOrd;
		// this.codOrd = inv.codOrd;
		// this.rigaOrd = inv.rigaOrd;
		// this.codBibF = inv.codBibF;
		// this.numFattura = inv.numFattura;
		// this.annoFattura = inv.annoFattura;
		// this.dataFattura = inv.dataFattura;
		// this.progrFattura = inv.progrFattura;
		// this.rigaFattura = inv.rigaFattura;
		// this.codNoDisp = inv.codNoDisp;
		// this.codFrui = inv.codFrui;
		// this.codPoloScar = inv.codPoloScar;
		// this.codBibS = inv.codBibS;
		// this.codScarico = inv.codScarico;
		// this.numScarico = inv.numScarico;
		// this.codCarico = inv.codCarico;
		// this.numCarico = inv.numCarico;
		// this.dataCarico = inv.dataCarico;
		// this.deliberaScarico = inv.deliberaScarico;
		// this.dataScarico = inv.dataScarico;
		// this.dataDelibScar = inv.dataDelibScar;
		// this.sezOld = inv.sezOld;
		// this.locOld = inv.locOld;
		// this.specOld = inv.specOld;
		// this.keyLocOld = inv.keyLocOld;
		// this.cancDB2i = inv.cancDB2i;
		// this.codSupporto = inv.codSupporto;
		// this.dataInsPrimaColl = inv.dataInsPrimaColl;
		// this.dataIngresso = inv.dataIngresso;
		// this.uteInsPrimaColl = inv.uteInsPrimaColl;
		// this.dataIns = inv.dataIns;
		// this.dataAgg = inv.dataAgg;
		// this.uteIns = inv.uteIns;
		// this.uteAgg = inv.uteAgg;
		// this.tipoAcquisizione = inv.tipoAcquisizione;
		// this.supportoCopia = inv.supportoCopia;
		// this.digitalizzazione = inv.digitalizzazione;
		// this.dispDaRemoto = inv.dispDaRemoto;
		// this.idAccessoRemoto = inv.idAccessoRemoto;
		// this.rifTecaDigitale = inv.rifTecaDigitale;
		// this.codRiproducibilita = inv.codRiproducibilita;
		// this.dataIngresso = inv.dataIngresso;
		// this.dataRedisp = inv.dataRedisp;
		// //
		// this.tipoAcquisizione = inv.tipoAcquisizione;
		// this.supportoCopia = inv.supportoCopia;
		// this.digitalizzazione = inv.digitalizzazione;
		// this.dispDaRemoto = inv.dispDaRemoto;
		// this.idAccessoRemoto = inv.idAccessoRemoto;
		// this.rifTecaDigitale = inv.rifTecaDigitale;
		// this.codRiproducibilita = inv.codRiproducibilita;
		// this.dataIngresso = inv.dataIngresso;
		// this.dataRedisp = inv.dataRedisp;
	}

	public InventarioVO(int codInvent) throws Exception {
		this.codInvent = codInvent;
	}

	public InventarioVO(String codSerie, int codInvent, String seqColl)
			throws Exception {
		this.codSerie = codSerie;
		this.codInvent = codInvent;
		this.seqColl = seqColl;
	}

	public InventarioVO(String codBib, String codSerie, int codInvent)
			throws Exception {
		this.codBib = codBib;
		this.codSerie = codSerie;
		this.codInvent = codInvent;
	}

	public InventarioVO(int codInvent, String bid, String precInv)
			throws Exception {
		this.codInvent = codInvent;
		this.bid = bid;
		this.precInv = precInv;
	}

	// usata per lista inventari di Collocazione
	public InventarioVO(String codBib, String codSerie, int codInvent,
			String codSit, String seqColl, String prec, String bid, int keyLoc,
			String keyLocOld, String sezOld, String locOld, String specOld)
			throws Exception {

		this.codBib = codBib;
		this.codSerie = codSerie;
		this.codInvent = codInvent;
		this.codSit = codSit;
		this.seqColl = seqColl;
		this.precInv = prec;
		this.bid = bid;
		this.keyLoc = keyLoc;
		this.keyLocOld = keyLocOld;
		this.sezOld = sezOld;
		this.locOld = locOld;
		this.specOld = specOld;
	}

	public InventarioVO(String seqColl, Integer codInv, String codSit,
			String precInv) throws Exception {

		this.seqColl = seqColl;
		this.codInvent = codInv;
		this.codSit = codSit;
		this.precInv = precInv;
	}

	// lista inventari dell'ordine
	public InventarioVO(String codBib, String serie, int inv, String precInv,
			String seqColl, String valore) throws Exception {

		this.codBib = codBib;
		this.codSerie = serie;
		this.codInvent = inv;
		this.seqColl = seqColl;
		this.precInv = precInv;
		this.valore = valore;
	}

	public double getValoreDouble() throws ParseException {
		return getValore(numberFormat, locale);
	}

	public double getImportoDouble() throws ParseException {
		return getImporto(numberFormat, locale);
	}

	public double getValore(String format, Locale locale) throws ParseException {
		return ValidazioneDati
				.getDoubleFromString(valore, format, locale, 9, 2);
	}

	public double getImporto(String format, Locale locale)
			throws ParseException {
		return ValidazioneDati.getDoubleFromString(importo, format, locale, 9,
				2);
	}

	public void setValoreDouble(double valore) {
		setValore(valore, numberFormat, locale);
	}

	public void setValore(double valore, String format, Locale locale) {
		this.valore = ValidazioneDati.getStringFromDouble(valore, format,
				locale);
	}

	public void setImportoDouble(double importo) {
		setImporto(importo, numberFormat, locale);
	}

	public void setImporto(double importo, String format, Locale locale) {
		this.importo = ValidazioneDati.getStringFromDouble(importo, format,
				locale);
	}

	public int getNumInv() {
		return numInv;
	}

	public void setNumInv(int numInv) {
		this.numInv = numInv;
	}

	public String getCambioBid() {
		return cambioBid;
	}

	public void setCambioBid(String cambioBid) {
		this.cambioBid = cambioBid;
	}

	public String getCambioIsbn() {
		return cambioIsbn;
	}

	public void setCambioIsbn(String cambioIsbn) {
		this.cambioIsbn = cambioIsbn;
	}

	public String getAnnoAbb() {
		return annoAbb;
	}

	public void setAnnoAbb(String annoAbb) {
		this.annoAbb = trimAndSet(annoAbb);
	}

	public String getAnnoFattura() {
		return annoFattura;
	}

	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
	}

	public String getAnnoOrd() {
		return annoOrd;
	}

	public void setAnnoOrd(String annoOrd) {
		this.annoOrd = annoOrd;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodBibF() {
		return codBibF;
	}

	public void setCodBibF(String codBibF) {
		this.codBibF = codBibF;
	}

	public String getCodBibO() {
		return codBibO;
	}

	public void setCodBibO(String codBibO) {
		this.codBibO = codBibO;
	}

	public String getCodBibS() {
		return codBibS;
	}

	public void setCodBibS(String codBibS) {
		this.codBibS = codBibS;
	}

	public String getCodCarico() {
		return codCarico;
	}

	public void setCodCarico(String codCarico) {
		this.codCarico = codCarico;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public String getCodFrui() {
		return codFrui;
	}

	public void setCodFrui(String codFrui) {
		this.codFrui = trimOrBlank(codFrui);
	}

	public int getCodInvent() {
		return codInvent;
	}

	public void setCodInvent(int codInvent) {
		this.codInvent = codInvent;
	}

	public String getCodMatInv() {
		return codMatInv;
	}

	public void setCodMatInv(String codMatInv) {
		this.codMatInv = codMatInv;
	}

	public String getCodNoDisp() {
		return codNoDisp;
	}

	public void setCodNoDisp(String codNoDisp) {
		this.codNoDisp = codNoDisp;
	}

	public String getCodOrd() {
		return codOrd;
	}

	public void setCodOrd(String codOrd) {
		this.codOrd = codOrd;
	}

	public String getCodPoloScar() {
		return codPoloScar;
	}

	public void setCodPoloScar(String codPoloScar) {
		this.codPoloScar = codPoloScar;
	}

	public String getCodProven() {
		return codProven;
	}

	public void setCodProven(String codProven) {
		this.codProven = codProven;
	}

	public String getCodScarico() {
		return codScarico;
	}

	public void setCodScarico(String codScarico) {
		this.codScarico = codScarico;
	}

	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}

	public String getCodSit() {
		return codSit;
	}

	public void setCodSit(String codSit) {
		this.codSit = trimAndSet(codSit);
	}

	public String getCodTipoOrd() {
		return codTipoOrd;
	}

	public void setCodTipoOrd(String codTipoOrd) {
		this.codTipoOrd = trimAndSet(codTipoOrd);
	}

	public String getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public String getDataCarico() {
		return dataCarico;
	}

	public void setDataCarico(String dataCarico) {
		this.dataCarico = dataCarico;
	}

	public String getDataDelibScar() {
		return dataDelibScar;
	}

	public void setDataDelibScar(String dataDelibScar) {
		this.dataDelibScar = dataDelibScar;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getDataScarico() {
		return dataScarico;
	}

	public void setDataScarico(String dataScarico) {
		this.dataScarico = dataScarico;
	}

	public String getFlagDisp() {
		return flagDisp;
	}

	public void setFlagDisp(String flagDisp) {
		this.flagDisp = flagDisp;
	}

	public String getFlagNU() {
		return flagNU;
	}

	public void setFlagNU(String flagNU) {
		this.flagNU = flagNU;
	}

	public String getCancDB2i() {
		return cancDB2i;
	}

	public void setCancDB2i(String cancDB2i) {
		this.cancDB2i = cancDB2i;
	}

	public String getImporto() {
		return importo;
	}

	public void setImporto(String importo) {
		if (isNull(importo)) {
			importo = "0";
		} else {
			this.importo = importo;
		}
	}

	public int getKeyLoc() {
		return keyLoc;
	}

	public void setKeyLoc(int keyLoc) {
		this.keyLoc = keyLoc;
	}

	public String getKeyLocOld() {
		return keyLocOld;
	}

	public void setKeyLocOld(String keyLocOld) {
		this.keyLocOld = keyLocOld;
	}

	public String getLocOld() {
		return locOld;
	}

	public void setLocOld(String locOld) {
		this.locOld = locOld;
	}

	public String getNumCarico() {
		return numCarico;
	}

	public void setNumCarico(String numCarico) {
		this.numCarico = numCarico;
	}

	public String getNumScarico() {
		return numScarico;
	}

	public void setNumScarico(String numScarico) {
		this.numScarico = numScarico;
	}

	public String getNumVol() {
		return numVol;
	}

	public void setNumVol(String numVol) {
		this.numVol = numVol;
	}

	public String getProgrFattura() {
		return progrFattura;
	}

	public void setProgrFattura(String progrFattura) {
		this.progrFattura = progrFattura;
	}

	public String getSeqColl() {
		return seqColl;
	}

	public void setSeqColl(String seqColl) {
		this.seqColl = seqColl;
	}

	public String getSezOld() {
		return sezOld;
	}

	public void setSezOld(String sezOld) {
		this.sezOld = sezOld;
	}

	public String getSpecOld() {
		return specOld;
	}

	public void setSpecOld(String specOld) {
		this.specOld = specOld;
	}

	public String getStatoConser() {
		return statoConser;
	}

	public void setStatoConser(String statoConser) {
		this.statoConser = trimOrBlank(statoConser);
	}

	public String getTotInter() {
		return totInter;
	}

	public void setTotInter(String totInter) {
		this.totInter = totInter;
	}

	public String getTotLoc() {
		return totLoc;
	}

	public void setTotLoc(String totLoc) {
		this.totLoc = totLoc;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		if (valore != null && valore.trim().equals("")) {
			this.valore = "0";
		} else {
			this.valore = valore;
		}
	}

	public String getDeliberaScarico() {
		return deliberaScarico;
	}

	public void setDeliberaScarico(String deliberaScarico) {
		this.deliberaScarico = deliberaScarico;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBibProven() {
		return codBibProven;
	}

	public void setCodBibProven(String codBibProven) {
		this.codBibProven = codBibProven;
	}

	public String getCodPoloProven() {
		return codPoloProven;
	}

	public void setCodPoloProven(String codPoloProven) {
		this.codPoloProven = codPoloProven;
	}

	public String getCodSupporto() {
		return codSupporto;
	}

	public void setCodSupporto(String codSupporto) {
		this.codSupporto = codSupporto;
	}

	public String getDataInsPrimaColl() {
		return dataInsPrimaColl;
	}

	public void setDataInsPrimaColl(String dataInsPrimaColl) {
		this.dataInsPrimaColl = dataInsPrimaColl;
	}

	public String getUteAgg() {
		return uteAgg;
	}

	public void setUteAgg(String uteAgg) {
		this.uteAgg = uteAgg;
	}

	public String getUteIns() {
		return uteIns;
	}

	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}

	public String getUteInsPrimaColl() {
		return uteInsPrimaColl;
	}

	public void setUteInsPrimaColl(String uteInsPrimaColl) {
		this.uteInsPrimaColl = uteInsPrimaColl;
	}

	public String getPrecInv() {
		return precInv;
	}

	public void setPrecInv(String precInv) {
		this.precInv = precInv;
	}

	public String getRigaFattura() {
		return rigaFattura;
	}

	public void setRigaFattura(String rigaFattura) {
		this.rigaFattura = rigaFattura;
	}

	public String getRigaOrd() {
		return rigaOrd;
	}

	public void setRigaOrd(String rigaOrd) {
		this.rigaOrd = rigaOrd;
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

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getDescrProven() {
		return descrProven;
	}

	public void setDescrProven(String descrProven) {
		this.descrProven = descrProven;
	}

	public String getNumFattura() {
		return numFattura;
	}

	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}

	public String getDescrFornitore() {
		return descrFornitore;
	}

	public void setDescrFornitore(String descrFornitore) {
		this.descrFornitore = descrFornitore;
	}

	public String getNumOrdine() {
		return numOrdine;
	}

	public void setNumOrdine(String numOrdine) {
		this.numOrdine = numOrdine;
	}

	public String getDataFattura() {
		return dataFattura;
	}

	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}

	public String getDataIngresso() {
		return dataIngresso;
	}

	public void setDataIngresso(String dataIngresso) {
		this.dataIngresso = dataIngresso;
	}

	public String getDataRedisp() {
		return dataRedisp;
	}

	public void setDataRedisp(String dataRedisp) {
		this.dataRedisp = dataRedisp;
	}

	public String getTipoAcquisizione() {
		return tipoAcquisizione;
	}

	public void setTipoAcquisizione(String tipoAcquisizione) {
		this.tipoAcquisizione = trimAndSet(tipoAcquisizione);
	}

	public String getSupportoCopia() {
		return supportoCopia;
	}

	public void setSupportoCopia(String supportoCopia) {
		this.supportoCopia = supportoCopia;
	}

	public String getDigitalizzazione() {
		return digitalizzazione;
	}

	public void setDigitalizzazione(String digitalizzazione) {
		this.digitalizzazione = digitalizzazione;
	}

	public String getIdAccessoRemoto() {
		return idAccessoRemoto;
	}

	public void setIdAccessoRemoto(String idAccessoRemoto) {
		this.idAccessoRemoto = trimOrEmpty(idAccessoRemoto);
	}

	public String getRifTecaDigitale() {
		return rifTecaDigitale;
	}

	public void setRifTecaDigitale(String rifTecaDigitale) {
		this.rifTecaDigitale = rifTecaDigitale;
	}

	public String getCodRiproducibilita() {
		return codRiproducibilita;
	}

	public void setCodRiproducibilita(String codRiproducibilita) {
		this.codRiproducibilita = codRiproducibilita;
	}

	public int getIdBibScar() {
		return idBibScar;
	}

	public void setIdBibScar(int idBibScar) {
		this.idBibScar = idBibScar;
	}

	public String getVersoBibDescr() {
		return versoBibDescr;
	}

	public void setVersoBibDescr(String versoBibDescr) {
		this.versoBibDescr = versoBibDescr;
	}

	public String getMotivoScaricoDescr() {
		return motivoScaricoDescr;
	}

	public void setMotivoScaricoDescr(String motivoScaricoDescr) {
		this.motivoScaricoDescr = motivoScaricoDescr;
	}

	public String getChiaveInventario() {
		StringBuilder buf = new StringBuilder(32);
		buf.append(trimOrEmpty(codBib));
		buf.append(" ");
		buf.append(ValidazioneDati.coalesce(codSerie, "") );
		buf.append(" ");
		buf.append(codInvent);

		return buf.toString();
	}

	public String getChiaveOrdine() {

		if (isFilled(codOrd)) {
			StringBuilder buf = new StringBuilder();
			buf.append(trimOrEmpty(annoOrd));
			buf.append(" ");
			buf.append(trimOrEmpty(codTipoOrd));
			buf.append(" ");
			buf.append(trimOrEmpty(codOrd));
			return buf.toString();
		}
		return "";
	}

	public String getDescrDispDaRemoto() {
		return descrDispDaRemoto;
	}

	public void setDescrDispDaRemoto(String descrDispDaRemoto) {
		this.descrDispDaRemoto = descrDispDaRemoto;
	}

	public String getDispDaRemoto() {
		return dispDaRemoto;
	}

	public void setDispDaRemoto(String dispDaRemoto) {
		this.dispDaRemoto = dispDaRemoto;
	}

	public String getGruppoFascicolo() {
		return gruppoFascicolo;
	}

	public void setGruppoFascicolo(String gruppoFascicolo) {
		this.gruppoFascicolo = trimAndSet(gruppoFascicolo);
	}

	public boolean isCollocato() {
		return trimOrEmpty(codSit).equals(Constants.DocFisico.Inventari.INVENTARIO_COLLOCATO);
	}

	public boolean isPrecisato() {
		return trimOrEmpty(codSit).equals(Constants.DocFisico.Inventari.INVENTARIO_PRECISATO);
	}

	public boolean isDismesso() {
		return trimOrEmpty(codSit).equals(Constants.DocFisico.Inventari.INVENTARIO_DISMESSO);
	}

	public String getSitAmm() {
		return sitAmm;
	}

	public void setSitAmm(String situazioneAmm) {
		this.sitAmm = situazioneAmm;
	}

}
