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
 package it.iccu.sbn.ejb.vo.servizi.erogazione;

import static it.iccu.sbn.util.Constants.Servizi.Movimenti.NUMBER_FORMAT_PREZZI;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovimentoVO extends BaseVO {

	private static final long serialVersionUID = -679339342048300980L;

	public static final String STATO_MOVIMENTO_ATTIVO    = "A";
	public static final String STATO_MOVIMENTO_CHIUSO    = "C";
	public static final String STATO_MOVIMENTO_ANNULLATO = "E";
	public static final String STATO_PRENOTAZIONE        = "P";
	public static final String STATO_MOVIMENTO_DOC_DISP_SERV_NON_TERMINATO = "S";
	public static final String STATO_RICHIESTA_IMMESSA = "A";

	public static final short  MAX_RINNOVI = 3;


	private boolean nuovoMov;
//	gestisce i blocchi e l'ordinamento liste	dataFineEffString
	private String tipoOrdinamento;
	private int elemPerBlocchi;
	private boolean attivitaAttuale;

	private List<?> lstTipiOrdinamento;


	private String intervalloCopia = "";
	private RichiestaRecordType flTipoRec;
	private String codBibOperante;
	private String codBibPrelievo;
	private String codBibRestituzione;
	private String noteBibliotecario = "";
	private String costoServizio;
	private String codTipoServ;
	protected String codBibInv;
	protected String codSerieInv;
	protected String codInvenInv;
	protected String codBibDocLett;
	protected String tipoDocLett;
	protected String codDocLet = "";
	protected String progrEsempDocLet = "";
	private String codBibUte;
	private String codUte;
	private String codRichServ;
	//rosa
	private String descrBib;
	private String codBibRichiedente; //ILL
	private String descrBibRichiedente;
	private String codBibFornitrice; //ILL
	private String descrBibFornitrice;
	private String email = "";
	//

	protected String segnatura;

	private String numFascicolo;
	private String numVolume = "";
	private String annoPeriodico = "";
	private String codTipoServRich;
	private String codStatoMov;
    private String codStatoRic;
	protected Timestamp dataInizioEff;
	private Timestamp dataFineEff;
	private short numRinnovi;
	private String codServ;
	private int idServizio;
	private String progrIter;
	private String bid = "";
	private String codAttivita;
	private String codAttivitaSucc;
	protected Timestamp dataRichiesta;
	private String numPezzi = "";
	private String noteUtente = "";
	private String prezzoMax = "";
	private Date dataMax;
	private Date dataProroga;
	protected Timestamp dataInizioPrev;

	protected Timestamp dataFinePrev;
	private String flSvolg;
	private String copyright;
	private String cod_erog;

	private String cat_fruizione;
	private String cat_riproduzione;

	private PrenotazionePostoVO prenotazionePosto;

	private String codSupporto;
	private String codRisp;
	private String codModPag;
	private String flCondiz;
	private String codErogAlt;
	private String codBibDest;
	private String codPolo;
	private String natura;

	private int idUtenteBib;
	private int idDocumento;

	//almaviva5_20141113 servizi ill
	private DatiRichiestaILLVO datiILL;

	//attributi per la conversione dei valori decimali
	private Locale locale = Locale.getDefault();

	protected boolean consegnato = false;

	protected long idRichiesta = 0;

	public boolean isNuovo() {
		return (idRichiesta == 0);
	}

	public boolean isRichiestaILL() {
		return ValidazioneDati.equals(flSvolg, "I") && (datiILL != null );//&& isFilled(datiILL.getTransactionId()) );
	}

	public boolean isRichiedenteRichiestaILL() {
		return isRichiestaILL() && datiILL != null && datiILL.isRichiedente();
	}

	public boolean isFornitriceRichiestaILL() {
		return isRichiestaILL() && datiILL != null && datiILL.isFornitrice();
	}

	public boolean isRichiestaLocale() {
		return !isRichiestaILL();
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = trimAndSet(codPolo);
	}

	public void clearMovimento() throws Exception {
		this.setProgr(0);
		this.idRichiesta = 0;
		this.nuovoMov = true;
		this.intervalloCopia = "";
		this.flTipoRec = RichiestaRecordType.FLAG_RICHIESTA;
		this.codBibOperante = "";
		this.codBibPrelievo = "";
		this.codBibRestituzione = "";
		this.noteBibliotecario = "";
		this.costoServizio = "";
		this.codTipoServ = "";
		this.codBibInv = "";
		this.codSerieInv = "";
		this.codInvenInv = "";
		this.codBibDocLett = "";
		this.tipoDocLett = "";
		this.codDocLet = "";
		this.progrEsempDocLet = "";
		this.codBibUte = "";
		this.codUte = "";
		this.codRichServ = "";
		this.numFascicolo = "";
		this.numVolume = "";
		this.annoPeriodico = "";
		this.codTipoServRich = "";
		this.codStatoMov = "";
		this.codStatoRic = "";
		this.dataInizioEff = null;
		this.dataFineEff = null;
		this.numRinnovi = 0;
		this.codServ = "";
		this.progrIter = "";
		this.bid = "";
		this.codAttivita = "";
		this.codAttivitaSucc = "";
		this.dataRichiesta = null;
		this.numPezzi = "";
		this.noteUtente = "";
		this.prezzoMax = "";
		this.dataMax = null;
		this.dataProroga = null;
		this.dataInizioPrev = null;
		this.dataInizioPrev = null;
		this.dataFinePrev = null;
		this.flSvolg = "";
		this.copyright = "";
		this.cod_erog = "";
		this.codSupporto = "";
		this.codRisp = "";
		this.codModPag = "";
		this.flCondiz = "";
		this.codErogAlt = "";
		this.codBibDest = "";
	}

	public MovimentoVO() {
		super();
	}

	public MovimentoVO(MovimentoVO mov)	{
		super(mov);
	    this.nuovoMov = mov.nuovoMov;
	    this.tipoOrdinamento = mov.tipoOrdinamento;
	    this.elemPerBlocchi = mov.elemPerBlocchi;
	    this.attivitaAttuale = mov.attivitaAttuale;
	    this.lstTipiOrdinamento = mov.lstTipiOrdinamento;
	    this.intervalloCopia = mov.intervalloCopia;
	    this.flTipoRec = mov.flTipoRec;
	    this.codBibOperante = mov.codBibOperante;
	    this.codBibPrelievo = mov.codBibPrelievo;
	    this.codBibRestituzione = mov.codBibRestituzione;
	    this.noteBibliotecario = mov.noteBibliotecario;
	    this.costoServizio = mov.costoServizio;
	    this.codTipoServ = mov.codTipoServ;
	    this.codBibInv = mov.codBibInv;
	    this.codSerieInv = mov.codSerieInv;
	    this.codInvenInv = mov.codInvenInv;
	    this.codBibDocLett = mov.codBibDocLett;
	    this.tipoDocLett = mov.tipoDocLett;
	    this.codDocLet = mov.codDocLet;
	    this.progrEsempDocLet = mov.progrEsempDocLet;
	    this.codBibUte = mov.codBibUte;
	    this.codUte = mov.codUte;
	    this.codRichServ = mov.codRichServ;
	    this.descrBib = mov.descrBib;
	    this.codBibRichiedente = mov.codBibRichiedente;
	    this.descrBibRichiedente = mov.descrBibRichiedente;
	    this.codBibFornitrice = mov.codBibFornitrice;
	    this.descrBibFornitrice = mov.descrBibFornitrice;
	    this.email = mov.email;
	    this.segnatura = mov.segnatura;
	    this.numFascicolo = mov.numFascicolo;
	    this.numVolume = mov.numVolume;
	    this.annoPeriodico = mov.annoPeriodico;
	    this.codTipoServRich = mov.codTipoServRich;
	    this.codStatoMov = mov.codStatoMov;
	    this.codStatoRic = mov.codStatoRic;
	    this.dataInizioEff = mov.dataInizioEff;
	    this.dataFineEff = mov.dataFineEff;
	    this.numRinnovi = mov.numRinnovi;
	    this.codServ = mov.codServ;
	    this.progrIter = mov.progrIter;
	    this.bid = mov.bid;
	    this.codAttivita = mov.codAttivita;
	    this.codAttivitaSucc = mov.codAttivitaSucc;
	    this.dataRichiesta = mov.dataRichiesta;
	    this.numPezzi = mov.numPezzi;
	    this.noteUtente = mov.noteUtente;
	    this.prezzoMax = mov.prezzoMax;
	    this.dataMax = mov.dataMax;
	    this.dataProroga = mov.dataProroga;
	    this.dataInizioPrev = mov.dataInizioPrev;
	    this.dataFinePrev = mov.dataFinePrev;
	    this.flSvolg = mov.flSvolg;
	    this.copyright = mov.copyright;
	    this.cod_erog = mov.cod_erog;
	    this.cat_fruizione = mov.cat_fruizione;
	    this.cat_riproduzione = mov.cat_riproduzione;
	    this.prenotazionePosto = mov.prenotazionePosto;
	    this.codSupporto = mov.codSupporto;
	    this.codRisp = mov.codRisp;
	    this.codModPag = mov.codModPag;
	    this.flCondiz = mov.flCondiz;
	    this.codErogAlt = mov.codErogAlt;
	    this.codBibDest = mov.codBibDest;
	    this.codPolo = mov.codPolo;
	    this.natura = mov.natura;
	    this.idUtenteBib = mov.idUtenteBib;
	    this.idDocumento = mov.idDocumento;
	    this.idRichiesta = mov.idRichiesta;
	    this.locale = mov.locale;
	    this.consegnato = mov.consegnato;

	    //almaviva5_20150127 servizi ill
	    this.datiILL = mov.datiILL;
	    this.idServizio = mov.idServizio;
	}

	public boolean isWithPrenotazionePosto() {
		return this.prenotazionePosto != null && !this.prenotazionePosto.isCancellato();
	}

	public String getAnnoPeriodico() {
		return annoPeriodico;
	}

	public void setAnnoPeriodico(String annoPeriodico) {
		this.annoPeriodico = annoPeriodico;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = trimOrEmpty(bid);
	}

	public String getCodAttivita() {
		return codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = trimAndSet(codAttivita);
	}

	public String getCodBibInv() {
		return codBibInv;
	}

	public void setCodBibInv(String codBibInv) {
		this.codBibInv = toUpperCase(codBibInv);
	}

	public String getCodBibUte() {
		return codBibUte;
	}

	public void setCodBibUte(String codBibUte) {
		this.codBibUte = toUpperCase(codBibUte);
	}

	public String getCodDocLet() {
		return codDocLet;
	}

	public void setCodDocLet(String codDocLet) {
		this.codDocLet = codDocLet;
	}

	public String getCodServ() {
		return codServ;
	}

	public void setCodServ(String codServ) {
		this.codServ = codServ;
	}

	public int getIdServizio() {
		return idServizio;
	}

	public void setIdServizio(int idServizio) {
		this.idServizio = idServizio;
	}

	public String getCodTipoServ() {
		return codTipoServ;
	}

	public void setCodTipoServ(String codTipoServ) {
		this.codTipoServ = codTipoServ;
	}

	public String getCodTipoServRich() {
		return codTipoServRich;
	}

	public void setCodTipoServRich(String codTipoServRich) {
		this.codTipoServRich = codTipoServRich;
	}

	public String getCodUte() {
		return codUte;
	}

	public void setCodUte(String codUte) {
		this.codUte = trimAndSet(codUte);
	}

	public String getCostoServizio() {
		return costoServizio;
	}

	public String getCostoServizioString() {
		return costoServizio;
	}

	public double getCostoServizioDouble() throws ParseException {
		return getCostoServizio(NUMBER_FORMAT_PREZZI, this.locale);
	}

	public double getCostoServizio(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(costoServizio,format,locale);
	}

	public void setCostoServizio(String costoServizio) {
		this.costoServizio = costoServizio;
	}

	public void setCostoServizioString(String costoServizio) {
		this.costoServizio = costoServizio;
	}

	public void setCostoServizio(double costoServizio) {
		setCostoServizio(costoServizio, NUMBER_FORMAT_PREZZI, locale);
	}

	public void setCostoServizio(double costoServizio, String format, Locale locale) {
		this.costoServizio = ValidazioneDati.getStringFromDouble(costoServizio, format, locale);
	}
	public String getNoteBibliotecario() {
		return noteBibliotecario;
	}

	public void setNoteBibliotecario(String noteBibliotecario) {
		this.noteBibliotecario = trimOrEmpty(noteBibliotecario);
	}

	public String getNumFascicolo() {
		return numFascicolo;
	}

	public void setNumFascicolo(String numFascicolo) {
		this.numFascicolo = trimOrEmpty(numFascicolo);
	}

	public short getNumRinnovi() {
		return numRinnovi;
	}

	public void setNumRinnovi(short numRinnovi) {
		this.numRinnovi = numRinnovi;
	}

	public String getNumVolume() {
		return numVolume;
	}

	public void setNumVolume(String numVolume) {
		this.numVolume = trimAndSet(numVolume);
	}

	public boolean isNuovoMov() {
		return nuovoMov;
	}

	public void setNuovoMov(boolean nuovoMov) {
		this.nuovoMov = nuovoMov;
	}

	public String getProgrIter() {
		return progrIter;
	}

	public void setProgrIter(String progrIter) {
		this.progrIter = progrIter;
	}

	public String getTipoDocLett() {
		return tipoDocLett;
	}

	public void setTipoDocLett(String tipoDocLett) {
		this.tipoDocLett = tipoDocLett;
	}

	public String getCodBibDocLett() {
		return codBibDocLett;
	}

	public void setCodBibDocLett(String codBibDocLett) {
		this.codBibDocLett = toUpperCase(codBibDocLett);
	}

	public String getCodInvenInv() {
		return codInvenInv;
	}

	public void setCodInvenInv(String codInvenInv) {
		this.codInvenInv = codInvenInv;
	}

	public String getCodSerieInv() {
		return codSerieInv;
	}

	public void setCodSerieInv(String codSerieInv) {
		this.codSerieInv = toUpperCase(codSerieInv);
	}

	public String getCod_erog() {
		return cod_erog;
	}

	public void setCod_erog(String cod_erog) {
		this.cod_erog = trimAndSet(cod_erog);
	}

	public String getCodBibDest() {
		return codBibDest;
	}

	public void setCodBibDest(String codBibDest) {
		this.codBibDest = toUpperCase(codBibDest);
	}

	public String getCodModPag() {
		return codModPag;
	}

	public void setCodModPag(String codModPag) {
		this.codModPag = codModPag;
	}

	public String getCodRichServ() {
		return codRichServ;
	}

	public void setCodRichServ(String codRichServ) {
		this.codRichServ = codRichServ;
	}


	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = trimAndSet(segnatura);
	}


	public String getCodRisp() {
		return codRisp;
	}

	public void setCodRisp(String codRisp) {
		this.codRisp = codRisp;
	}

	public String getCodSupporto() {
		return codSupporto;
	}

	public void setCodSupporto(String codSupporto) {
		this.codSupporto = trimAndSet(codSupporto);
	}

	public String getCodErogAlt() {
		return codErogAlt;
	}

	public void setCodErogAlt(String codErogAlt) {
		this.codErogAlt = codErogAlt;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getFlCondiz() {
		return flCondiz;
	}

	public void setFlCondiz(String flCondiz) {
		this.flCondiz = flCondiz;
	}

	public String getFlSvolg() {
		return flSvolg;
	}

	public void setFlSvolg(String flSvolg) {
		this.flSvolg = flSvolg;
	}

	public RichiestaRecordType getFlTipoRec() {
		return flTipoRec;
	}

	public void setFlTipoRec(RichiestaRecordType flTipoRec) {
		this.flTipoRec = flTipoRec;
	}

	public String getNoteUtente() {
		return noteUtente;
	}

	public void setNoteUtente(String noteUtente) {
		this.noteUtente = trimOrEmpty(noteUtente);
	}

	public String getNumPezzi() {
		return numPezzi;
	}

	public short getNumPezziShort() throws ParseException {
		return Short.parseShort(numPezzi);
	}

	public void setNumPezzi(String numPezzi) {
		this.numPezzi = trimOrEmpty(numPezzi);
	}

	public void setNumPezzi(short numPezzi) {
		setNumPezzi(Short.toString(numPezzi));
	}

	public String getPrezzoMax() {
		return prezzoMax;
	}

	public double getPrezzoMaxDouble() throws ParseException {
		return getPrezzoMax(NUMBER_FORMAT_PREZZI, locale);
	}

	public double getPrezzoMax(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(prezzoMax,format,locale);
	}

	public void setPrezzoMax(String prezzoMax) {
		this.prezzoMax = trimOrEmpty(prezzoMax);
	}

	public void setPrezzoMax(double prezzoMax) {
		setPrezzoMax(prezzoMax, NUMBER_FORMAT_PREZZI, locale);
	}

	public void setPrezzoMax(double prezzoMax, String format, Locale locale) {
		this.prezzoMax = ValidazioneDati.getStringFromDouble(prezzoMax, format, locale);
	}

	public String getDataFineEffString() {
		return (dataFineEff==null ? null : DateUtil.formattaDataOra(dataFineEff.getTime()));
	}

	public Timestamp getDataFineEff() {
		return dataFineEff;
	}

	public void setDataFineEffString(String data) {
		this.dataFineEff = this.fromStringtoTimestamp(data);
	}

	public String getDataFineEffNoOraString() {
		return (dataFineEff==null ? null : DateUtil.formattaData(dataFineEff.getTime()));
	}

	public void setDataFineEff(Timestamp dataFineEff) {
		this.dataFineEff = dataFineEff;
	}

	public String getDataFinePrevString() {
		return (dataFinePrev == null ? null : DateUtil.formattaDataOra(dataFinePrev.getTime()));
	}

	public String getDataFinePrevNoOraString() {
		return (dataFinePrev == null ? null : DateUtil.formattaData(dataFinePrev.getTime()));
	}

	public Timestamp getDataFinePrev() {
		return dataFinePrev;
	}

	public void setDataFinePrevString(String data) {
		this.dataFinePrev = this.fromStringtoTimestamp(data);
	}
	public void setDataFinePrevNoOraString(String data) {
		this.dataFinePrev = this.fromStringtoTimestamp(data);
	}

	public void setDataFinePrev(Timestamp dataFinePrev) {
		this.dataFinePrev = dataFinePrev;
	}

	public String getDataInizioEffString() {
		return (dataInizioEff == null ? null : DateUtil.formattaDataOra(dataInizioEff.getTime()));
	}

	public String getDataInizioEffNoOraString() {
		return (dataInizioEff == null ? null : DateUtil.formattaData(dataInizioEff.getTime()));
	}

	public Timestamp getDataInizioEff() {
		return dataInizioEff;
	}

	public void setDataInizioEffString(String data) {
		this.dataInizioEff = this.fromStringtoTimestamp(data);
	}

	public void setDataInizioEffNoOraString(String data) {
		this.dataInizioEff = this.fromStringtoTimestamp(data);
	}

	public void setDataInizioEff(Timestamp dataInizioEff) {
		this.dataInizioEff = dataInizioEff;
	}

	public String getDataInizioPrevString() {
		return (dataInizioPrev == null ? null : DateUtil.formattaDataOra(dataInizioPrev.getTime()));
	}

	public String getDataInizioPrevNoOraString() {
		return (dataInizioPrev == null ? null : DateUtil.formattaData(dataInizioPrev.getTime()));
	}

	public Timestamp getDataInizioPrev() {
		return dataInizioPrev;
	}

	public void setDataInizioPrevString(String data) {
		this.dataInizioPrev = this.fromStringtoTimestamp(data);
	}

	public void setDataInizioPrevNoOraString(String data) {
		this.dataInizioPrev = this.fromStringtoTimestamp(data);
	}

	public void setDataInizioPrev(Timestamp dataInizioPrev) {
		this.dataInizioPrev = dataInizioPrev;
	}

	public Date getDataMax() {
		return dataMax;
	}

	public String getDataMaxString() {
		return (dataMax==null ? null : DateUtil.formattaData(dataMax.getTime()));
	}

	public void setDataMaxString(String data) {
		this.dataMax = this.fromStringToDate(data);
	}

	public void setDataMax(Date dataMax) {
		this.dataMax = dataMax;
	}

	public Date getDataProroga() {
		return dataProroga;
	}

	public String getDataProrogaString() {
		return (dataProroga == null ? null : DateUtil.formattaData(dataProroga.getTime()));
	}

	public void setDataProrogaString(String data) {
		this.dataProroga = this.fromStringToDate(data);
	}

	public void setDataProroga(Date dataProroga) {
		this.dataProroga = dataProroga;
	}

	public String getDataRichiestaString() {
		return (dataRichiesta == null ? null : DateUtil.formattaDataOra(dataRichiesta.getTime()));
	}

	public String getDataRichiestaNoOraString() {
		return (dataRichiesta == null ? null : DateUtil.formattaData(dataRichiesta.getTime()));
	}

	public Timestamp getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Timestamp dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public String getProgrEsempDocLet() {
		return progrEsempDocLet;
	}

	public void setProgrEsempDocLet(String progrEsempDocLet) {
		this.progrEsempDocLet = progrEsempDocLet;
	}

	public int getElemPerBlocchi() {
		return elemPerBlocchi;
	}

	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.elemPerBlocchi = elemPerBlocchi;
	}

	public List<?> getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(List<?> lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getCodBibOperante() {
		return codBibOperante;
	}

	public void setCodBibOperante(String codBibOperante) {
		this.codBibOperante = codBibOperante;
	}

	public String getCodBibPrelievo() {
		return codBibPrelievo;
	}

	public void setCodBibPrelievo(String codBibPerelievo) {
		this.codBibPrelievo = toUpperCase(codBibPerelievo);
	}

	public String getCodBibRestituzione() {
		return codBibRestituzione;
	}

	public void setCodBibRestituzione(String codBibRestituzione) {
		this.codBibRestituzione = toUpperCase(codBibRestituzione);
	}

	public String getCodStatoMov() {
		return codStatoMov;
	}

	public void setCodStatoMov(String codStatoMov) {
		this.codStatoMov = codStatoMov;
	}

	public String getCodStatoRic() {
		return codStatoRic;
	}

	public void setCodStatoRic(String codStatoRic) {
		this.codStatoRic = codStatoRic;
	}

	public String getCat_fruizione() {
		return cat_fruizione;
	}

	public void setCat_fruizione(String catFruizione) {
		cat_fruizione = catFruizione;
	}

	public String getCat_riproduzione() {
		return cat_riproduzione;
	}

	public void setCat_riproduzione(String catRiproduzione) {
		cat_riproduzione = trimAndSet(catRiproduzione);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((annoPeriodico == null) ? 0 : annoPeriodico.hashCode());
		result = prime * result + (attivitaAttuale ? 1231 : 1237);
		result = prime * result + ((bid == null) ? 0 : bid.hashCode());
		result = prime * result
				+ ((cat_fruizione == null) ? 0 : cat_fruizione.hashCode());
		result = prime
				* result
				+ ((cat_riproduzione == null) ? 0 : cat_riproduzione.hashCode());
		result = prime * result
				+ ((codAttivita == null) ? 0 : codAttivita.hashCode());
		result = prime * result
				+ ((codAttivitaSucc == null) ? 0 : codAttivitaSucc.hashCode());
		result = prime * result
				+ ((codBibDest == null) ? 0 : codBibDest.hashCode());
		result = prime * result
				+ ((codBibDocLett == null) ? 0 : codBibDocLett.hashCode());
		result = prime
				* result
				+ ((codBibFornitrice == null) ? 0 : codBibFornitrice.hashCode());
		result = prime * result
				+ ((codBibInv == null) ? 0 : codBibInv.hashCode());
		result = prime * result
				+ ((codBibOperante == null) ? 0 : codBibOperante.hashCode());
		result = prime * result
				+ ((codBibPrelievo == null) ? 0 : codBibPrelievo.hashCode());
		result = prime
				* result
				+ ((codBibRestituzione == null) ? 0 : codBibRestituzione
						.hashCode());
		result = prime
				* result
				+ ((codBibRichiedente == null) ? 0 : codBibRichiedente
						.hashCode());
		result = prime * result
				+ ((codBibUte == null) ? 0 : codBibUte.hashCode());
		result = prime * result
				+ ((codDocLet == null) ? 0 : codDocLet.hashCode());
		result = prime * result
				+ ((codErogAlt == null) ? 0 : codErogAlt.hashCode());
		result = prime * result
				+ ((codInvenInv == null) ? 0 : codInvenInv.hashCode());
		result = prime * result
				+ ((codModPag == null) ? 0 : codModPag.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result
				+ ((codRichServ == null) ? 0 : codRichServ.hashCode());
		result = prime * result + ((codRisp == null) ? 0 : codRisp.hashCode());
		result = prime * result
				+ ((codSerieInv == null) ? 0 : codSerieInv.hashCode());
		result = prime * result + ((codServ == null) ? 0 : codServ.hashCode());
		result = prime * result
				+ ((codStatoMov == null) ? 0 : codStatoMov.hashCode());
		result = prime * result
				+ ((codStatoRic == null) ? 0 : codStatoRic.hashCode());
		result = prime * result
				+ ((codSupporto == null) ? 0 : codSupporto.hashCode());
		result = prime * result
				+ ((codTipoServ == null) ? 0 : codTipoServ.hashCode());
		result = prime * result
				+ ((codTipoServRich == null) ? 0 : codTipoServRich.hashCode());
		result = prime * result + ((codUte == null) ? 0 : codUte.hashCode());
		result = prime * result
				+ ((cod_erog == null) ? 0 : cod_erog.hashCode());
		result = prime * result
				+ ((copyright == null) ? 0 : copyright.hashCode());
		result = prime * result
				+ ((costoServizio == null) ? 0 : costoServizio.hashCode());
		result = prime * result
				+ ((dataFineEff == null) ? 0 : dataFineEff.hashCode());
		result = prime * result
				+ ((dataFinePrev == null) ? 0 : dataFinePrev.hashCode());
		result = prime * result
				+ ((dataInizioEff == null) ? 0 : dataInizioEff.hashCode());
		result = prime * result
				+ ((dataInizioPrev == null) ? 0 : dataInizioPrev.hashCode());
		result = prime * result + ((dataMax == null) ? 0 : dataMax.hashCode());
		result = prime * result
				+ ((dataProroga == null) ? 0 : dataProroga.hashCode());
		result = prime * result
				+ ((dataRichiesta == null) ? 0 : dataRichiesta.hashCode());
		result = prime * result
				+ ((descrBib == null) ? 0 : descrBib.hashCode());
		result = prime
				* result
				+ ((descrBibFornitrice == null) ? 0 : descrBibFornitrice
						.hashCode());
		result = prime
				* result
				+ ((descrBibRichiedente == null) ? 0 : descrBibRichiedente
						.hashCode());
		result = prime * result + elemPerBlocchi;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((flCondiz == null) ? 0 : flCondiz.hashCode());
		result = prime * result + ((flSvolg == null) ? 0 : flSvolg.hashCode());
		result = prime * result
				+ ((flTipoRec == null) ? 0 : flTipoRec.hashCode());
		result = prime * result + (int) (idRichiesta ^ (idRichiesta >>> 32));
		result = prime
				* result
				+ ((intervalloCopia == null) ? 0 : intervalloCopia
						.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime
				* result
				+ ((lstTipiOrdinamento == null) ? 0 : lstTipiOrdinamento
						.hashCode());
		result = prime
				* result
				+ ((noteBibliotecario == null) ? 0 : noteBibliotecario
						.hashCode());
		result = prime * result
				+ ((noteUtente == null) ? 0 : noteUtente.hashCode());
		result = prime * result
				+ ((numFascicolo == null) ? 0 : numFascicolo.hashCode());
		result = prime * result
				+ ((numPezzi == null) ? 0 : numPezzi.hashCode());
		result = prime * result + numRinnovi;
		result = prime * result
				+ ((numVolume == null) ? 0 : numVolume.hashCode());
		result = prime
				* result
				+ ((NUMBER_FORMAT_PREZZI == null) ? 0 : NUMBER_FORMAT_PREZZI
						.hashCode());
		result = prime * result + (nuovoMov ? 1231 : 1237);
		result = prime * result
				+ ((prezzoMax == null) ? 0 : prezzoMax.hashCode());
		result = prime
				* result
				+ ((progrEsempDocLet == null) ? 0 : progrEsempDocLet.hashCode());
		result = prime * result
				+ ((progrIter == null) ? 0 : progrIter.hashCode());
		result = prime * result
				+ ((segnatura == null) ? 0 : segnatura.hashCode());
		result = prime * result
				+ ((tipoDocLett == null) ? 0 : tipoDocLett.hashCode());
		result = prime * result
				+ ((tipoOrdinamento == null) ? 0 : tipoOrdinamento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovimentoVO other = (MovimentoVO) obj;
		if (annoPeriodico == null) {
			if (other.annoPeriodico != null)
				return false;
		} else if (!annoPeriodico.equals(other.annoPeriodico))
			return false;
		if (attivitaAttuale != other.attivitaAttuale)
			return false;
		if (bid == null) {
			if (other.bid != null)
				return false;
		} else if (!bid.equals(other.bid))
			return false;
		if (cat_fruizione == null) {
			if (other.cat_fruizione != null)
				return false;
		} else if (!cat_fruizione.equals(other.cat_fruizione))
			return false;
		if (cat_riproduzione == null) {
			if (other.cat_riproduzione != null)
				return false;
		} else if (!cat_riproduzione.equals(other.cat_riproduzione))
			return false;
		if (codAttivita == null) {
			if (other.codAttivita != null)
				return false;
		} else if (!codAttivita.equals(other.codAttivita))
			return false;
		if (codAttivitaSucc == null) {
			if (other.codAttivitaSucc != null)
				return false;
		} else if (!codAttivitaSucc.equals(other.codAttivitaSucc))
			return false;
		if (codBibDest == null) {
			if (other.codBibDest != null)
				return false;
		} else if (!codBibDest.equals(other.codBibDest))
			return false;
		if (codBibDocLett == null) {
			if (other.codBibDocLett != null)
				return false;
		} else if (!codBibDocLett.equals(other.codBibDocLett))
			return false;
		if (codBibFornitrice == null) {
			if (other.codBibFornitrice != null)
				return false;
		} else if (!codBibFornitrice.equals(other.codBibFornitrice))
			return false;
		if (codBibInv == null) {
			if (other.codBibInv != null)
				return false;
		} else if (!codBibInv.equals(other.codBibInv))
			return false;
		if (codBibOperante == null) {
			if (other.codBibOperante != null)
				return false;
		} else if (!codBibOperante.equals(other.codBibOperante))
			return false;
		if (codBibPrelievo == null) {
			if (other.codBibPrelievo != null)
				return false;
		} else if (!codBibPrelievo.equals(other.codBibPrelievo))
			return false;
		if (codBibRestituzione == null) {
			if (other.codBibRestituzione != null)
				return false;
		} else if (!codBibRestituzione.equals(other.codBibRestituzione))
			return false;
		if (codBibRichiedente == null) {
			if (other.codBibRichiedente != null)
				return false;
		} else if (!codBibRichiedente.equals(other.codBibRichiedente))
			return false;
		if (codBibUte == null) {
			if (other.codBibUte != null)
				return false;
		} else if (!codBibUte.equals(other.codBibUte))
			return false;
		if (codDocLet == null) {
			if (other.codDocLet != null)
				return false;
		} else if (!codDocLet.equals(other.codDocLet))
			return false;
		if (codErogAlt == null) {
			if (other.codErogAlt != null)
				return false;
		} else if (!codErogAlt.equals(other.codErogAlt))
			return false;
		if (codInvenInv == null) {
			if (other.codInvenInv != null)
				return false;
		} else if (!codInvenInv.equals(other.codInvenInv))
			return false;
		if (codModPag == null) {
			if (other.codModPag != null)
				return false;
		} else if (!codModPag.equals(other.codModPag))
			return false;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
		if (codRichServ == null) {
			if (other.codRichServ != null)
				return false;
		} else if (!codRichServ.equals(other.codRichServ))
			return false;
		if (codRisp == null) {
			if (other.codRisp != null)
				return false;
		} else if (!codRisp.equals(other.codRisp))
			return false;
		if (codSerieInv == null) {
			if (other.codSerieInv != null)
				return false;
		} else if (!codSerieInv.equals(other.codSerieInv))
			return false;
		if (codServ == null) {
			if (other.codServ != null)
				return false;
		} else if (!codServ.equals(other.codServ))
			return false;
		if (codStatoMov == null) {
			if (other.codStatoMov != null)
				return false;
		} else if (!codStatoMov.equals(other.codStatoMov))
			return false;
		if (codStatoRic == null) {
			if (other.codStatoRic != null)
				return false;
		} else if (!codStatoRic.equals(other.codStatoRic))
			return false;
		if (codSupporto == null) {
			if (other.codSupporto != null)
				return false;
		} else if (!codSupporto.equals(other.codSupporto))
			return false;
		if (codTipoServ == null) {
			if (other.codTipoServ != null)
				return false;
		} else if (!codTipoServ.equals(other.codTipoServ))
			return false;
		if (codTipoServRich == null) {
			if (other.codTipoServRich != null)
				return false;
		} else if (!codTipoServRich.equals(other.codTipoServRich))
			return false;
		if (codUte == null) {
			if (other.codUte != null)
				return false;
		} else if (!codUte.equals(other.codUte))
			return false;
		if (cod_erog == null) {
			if (other.cod_erog != null)
				return false;
		} else if (!cod_erog.equals(other.cod_erog))
			return false;
		if (copyright == null) {
			if (other.copyright != null)
				return false;
		} else if (!copyright.equals(other.copyright))
			return false;
		if (costoServizio == null) {
			if (other.costoServizio != null)
				return false;
		} else if (!costoServizio.equals(other.costoServizio))
			return false;
		/*	//almaviva5_20100426 le date sono escluse dal controllo
		if (dataFineEff == null) {
			if (other.dataFineEff != null)
				return false;
		} else if (!dataFineEff.equals(other.dataFineEff))
			return false;
		if (dataFinePrev == null) {
			if (other.dataFinePrev != null)
				return false;
		} else if (!dataFinePrev.equals(other.dataFinePrev))
			return false;
		if (dataInizioEff == null) {
			if (other.dataInizioEff != null)
				return false;
		} else if (!dataInizioEff.equals(other.dataInizioEff))
			return false;
		if (dataInizioPrev == null) {
			if (other.dataInizioPrev != null)
				return false;
		} else if (!dataInizioPrev.equals(other.dataInizioPrev))
			return false;
		*/
		if (dataMax == null) {
			if (other.dataMax != null)
				return false;
		} else if (!dataMax.equals(other.dataMax))
			return false;
		if (dataProroga == null) {
			if (other.dataProroga != null)
				return false;
		} else if (!dataProroga.equals(other.dataProroga))
			return false;
		if (dataRichiesta == null) {
			if (other.dataRichiesta != null)
				return false;
		} else if (!dataRichiesta.equals(other.dataRichiesta))
			return false;
		if (descrBib == null) {
			if (other.descrBib != null)
				return false;
		} else if (!descrBib.equals(other.descrBib))
			return false;
		if (descrBibFornitrice == null) {
			if (other.descrBibFornitrice != null)
				return false;
		} else if (!descrBibFornitrice.equals(other.descrBibFornitrice))
			return false;
		if (descrBibRichiedente == null) {
			if (other.descrBibRichiedente != null)
				return false;
		} else if (!descrBibRichiedente.equals(other.descrBibRichiedente))
			return false;
		if (elemPerBlocchi != other.elemPerBlocchi)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (flCondiz == null) {
			if (other.flCondiz != null)
				return false;
		} else if (!flCondiz.equals(other.flCondiz))
			return false;
		if (flSvolg == null) {
			if (other.flSvolg != null)
				return false;
		} else if (!flSvolg.equals(other.flSvolg))
			return false;
		if (flTipoRec == null) {
			if (other.flTipoRec != null)
				return false;
		} else if (!flTipoRec.equals(other.flTipoRec))
			return false;
		if (idRichiesta != other.idRichiesta)
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (lstTipiOrdinamento == null) {
			if (other.lstTipiOrdinamento != null)
				return false;
		} else if (!lstTipiOrdinamento.equals(other.lstTipiOrdinamento))
			return false;
		if (noteBibliotecario == null) {
			if (other.noteBibliotecario != null)
				return false;
		} else if (!noteBibliotecario.equals(other.noteBibliotecario))
			return false;
		if (noteUtente == null) {
			if (other.noteUtente != null)
				return false;
		} else if (!noteUtente.equals(other.noteUtente))
			return false;
		if (numFascicolo == null) {
			if (other.numFascicolo != null)
				return false;
		} else if (!numFascicolo.equals(other.numFascicolo))
			return false;
		if (numPezzi == null) {
			if (other.numPezzi != null)
				return false;
		} else if (!numPezzi.equals(other.numPezzi))
			return false;
		if (numRinnovi != other.numRinnovi)
			return false;
		if (numVolume == null) {
			if (other.numVolume != null)
				return false;
		} else if (!numVolume.equals(other.numVolume))
			return false;
		if (nuovoMov != other.nuovoMov)
			return false;
		if (prezzoMax == null) {
			if (other.prezzoMax != null)
				return false;
		} else if (!prezzoMax.equals(other.prezzoMax))
			return false;
		if (progrEsempDocLet == null) {
			if (other.progrEsempDocLet != null)
				return false;
		} else if (!progrEsempDocLet.equals(other.progrEsempDocLet))
			return false;
		if (progrIter == null) {
			if (other.progrIter != null)
				return false;
		} else if (!progrIter.equals(other.progrIter))
			return false;
		if (segnatura == null) {
			if (other.segnatura != null)
				return false;
		} else if (!segnatura.equals(other.segnatura))
			return false;
		if (tipoDocLett == null) {
			if (other.tipoDocLett != null)
				return false;
		} else if (!tipoDocLett.equals(other.tipoDocLett))
			return false;
		if (tipoOrdinamento == null) {
			if (other.tipoOrdinamento != null)
				return false;
		} else if (!tipoOrdinamento.equals(other.tipoOrdinamento))
			return false;
		if (intervalloCopia == null) {
			if (other.intervalloCopia != null)
				return false;
		} else if (!intervalloCopia.equals(other.intervalloCopia))
			return false;
		return true;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getCodAttivitaSucc() {
		return codAttivitaSucc;
	}

	public void setCodAttivitaSucc(String codAttivitaSucc) {
		this.codAttivitaSucc = codAttivitaSucc;
	}

//	public boolean isMovimento() {
//		return (this.flTipoRec!=null && this.flTipoRec.equals(MovimentoVO.FLAG_MOVIMENTO)    ? true : false);
//	}

	public boolean isPrenotazione() {
		return (flTipoRec == RichiestaRecordType.FLAG_PRENOTAZIONE);
	}

	public boolean isRichiesta() {
		return (flTipoRec == RichiestaRecordType.FLAG_RICHIESTA);
	}

	public boolean isRichiestaSuInventario() {
		try {
			return isFilled(codBibInv) && isFilled(codInvenInv) && (Integer.parseInt(codInvenInv) > 0);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isPeriodico() {
		return ValidazioneDati.equals(natura, "S");
	}

	public boolean isRichiestaSuSegnatura() {
		try {
			return isFilled(codBibDocLett) && isFilled(codDocLet) && (Integer.parseInt(codDocLet) > 0);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isAttivo() {
		//mov != annullato
		//ric = accettata, prorogata, non prorogata, in attesa di proroga
		return !ValidazioneDati.in(codStatoMov, "E")
			&& !ValidazioneDati.in(codStatoRic, "B", "D", "F");
	}

	private Timestamp fromStringtoTimestamp(String data) {
		if (isFilled(data)) {
			java.util.Date newDate = DateUtil.getDataOra(data);
			if (newDate == null)
				newDate = DateUtil.toDate(data);
			return (newDate == null) ? null : new java.sql.Timestamp(newDate.getTime());
		}

		return null;
	}

	private Date fromStringToDate(String data) {
		java.util.Date newDate = DateUtil.toDate(data);
		return (newDate == null) ? null : new java.sql.Date(newDate.getTime());
	}

	public boolean isAttivitaAttuale() {
		return attivitaAttuale;
	}

	public void setAttivitaAttuale(boolean attivitaAttuale) {
		this.attivitaAttuale = attivitaAttuale;
	}

	public void setIdRichiesta(long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public long getIdRichiesta() {
		return idRichiesta;
	}

	public String getDatiDocumento() {
		if (isRichiestaSuInventario())
			//return codBibInv + " " + codSerieInv + " " + codInvenInv + " - " + segnatura;
			//return segnatura;
			return codBibInv + " " + segnatura;
		if (isRichiestaSuSegnatura()) {
			//return codBibDocLett + " " + tipoDocLett + codDocLet + "-" + progrEsempDocLet + " - " + segnatura;
			//return segnatura;
			if (isFilled(segnatura))
				return codBibDocLett + " " + segnatura;
			else
				return getDatiInventario();
		}
		return "";
	}

	public String getDatiInventario() {
		if (isRichiestaSuInventario())
			return codBibInv + " " + codSerieInv + " " + codInvenInv;

		if (isRichiestaSuSegnatura())
			return codBibDocLett + " " + tipoDocLett + codDocLet + "-" + progrEsempDocLet;

		return "";
	}

	public String getCodBibRichiedente() {
		return codBibRichiedente;
	}

	public void setCodBibRichiedente(String codBibRichiedente) {
		this.codBibRichiedente = codBibRichiedente;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}


	public String getDescrBibRichiedente() {
		return descrBibRichiedente;
	}

	public void setDescrBibRichiedente(String descrBibRichiedente) {
		this.descrBibRichiedente = descrBibRichiedente;
	}

	public String getCodBibFornitrice() {
		return codBibFornitrice;
	}

	public void setCodBibFornitrice(String codBibFornitrice) {
		this.codBibFornitrice = codBibFornitrice;
	}

	public String getDescrBibFornitrice() {
		return descrBibFornitrice;
	}

	public void setDescrBibFornitrice(String descrBibFornitrice) {
		this.descrBibFornitrice = descrBibFornitrice;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = trimOrEmpty(email);
	}

	public PrenotazionePostoVO getPrenotazionePosto() {
		return prenotazionePosto;
	}

	public void setPrenotazionePosto(PrenotazionePostoVO prenotazionePosto) {
		this.prenotazionePosto = prenotazionePosto;
	}

	public String getIntervalloCopia() {
		return intervalloCopia;
	}

	public void setIntervalloCopia(String intervalloCopia) {
		this.intervalloCopia = trimAndSet(intervalloCopia);
	}

	public String getTsVarString() {
		return (tsVar==null ? null : DateUtil.formattaDataOra(tsVar.getTime()));
	}

	public String getTsVarNoOraString() {
		return (tsVar==null ? null : DateUtil.formattaData(tsVar.getTime()));
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = trimAndSet(natura);
	}

	public boolean isConsegnato() {
		return consegnato;
	}

	public void setConsegnato(boolean consegnato) {
		this.consegnato = consegnato;
	}

	public int getIdUtenteBib() {
		return idUtenteBib;
	}

	public void setIdUtenteBib(int idUtente) {
		this.idUtenteBib = idUtente;
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public DatiRichiestaILLVO getDatiILL() {
		return datiILL;
	}

	public void setDatiILL(DatiRichiestaILLVO datiILL) {
		this.datiILL = datiILL;
	}

}
