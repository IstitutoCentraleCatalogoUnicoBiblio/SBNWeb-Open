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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

public class OrdiniVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 4039947064996648606L;
	private String ticket;
	private Integer progressivo = 0;
	private String utente;

	private int IDOrd;
	private int IDBil;
	private int IDSez;
	private int IDVal;
	private String codPolo;
	private String codBibl;
	private String codOrdine;
	private String annoOrdine;
	private String tipoOrdine;
	private String dataOrdine;
	private String dataStampaOrdine;
	private String noteOrdine;
	private int numCopieOrdine;
	private boolean continuativo;
	private String statoOrdine;
	private String codDocOrdine;
	private String codTipoDocOrdine;
	private String codUrgenzaOrdine;
	private String codRicOffertaOrdine;
	private String idOffertaFornOrdine;
	private StrutturaCombo fornitore;
	private FornitoreVO anagFornitore;
	private boolean fornitoreLocalizzato = false;
	private String noteFornitore;
	private String tipoInvioOrdine;
	private StrutturaTerna bilancio;
	private String codPrimoOrdine;
	private String annoPrimoOrdine;
	private String valutaOrdine;
	private String prezzoOrdineStr;
	private String prezzoEuroOrdineStr;
	private double prezzoOrdine;
	private double prezzoEuroOrdine;
	private String paeseOrdine;
	private String sezioneAcqOrdine;
	private String codBibliotecaSuggOrdine;
	private String codSuggBiblOrdine;
	private StrutturaCombo titolo;
	private String titoloIsbn;
	private String titoloIssn;
	private List<StrutturaTerna> numStandardArr; // tipo, numero, denotipo
	private String statoAbbOrdine;
	private String periodoValAbbOrdine;
	private String annoAbbOrdine;
	private String numFascicoloAbbOrdine;
	private String dataPubblFascicoloAbbOrdine;
	private String annataAbbOrdine;
	private int numVolAbbOrdine;
	private String dataFineAbbOrdine;
	private String regTribOrdine;
	private String naturaOrdine;
	private boolean stampato;
	private boolean rinnovato;
	private StrutturaTerna rinnovoOrigine; // tipo, anno, cod
	private String tipoVariazione;
	private String dataChiusura;
	private List<StrutturaInventariOrdVO> righeInventariRilegatura;
	private boolean flag_canc = false;
	private boolean gestBil = true;
	private boolean gestSez = true;
	private boolean gestProf = true;
	private String provenienza;
	private boolean riapertura = false;
	private Timestamp dataUpd;
	String esistenzaInventariLegati = ""; // 0=inventari assenti, 1=inventari presenti

	//almaviva5_20121115 evolutive google
	private String cd_tipo_lav;
	private OrdineCarrelloSpedizioneVO ordineCarrelloSpedizione;
	private Integer cd_forn_google;

	public OrdiniVO() {
		super();
	};

	public OrdiniVO(String codP, String codB, String codOrd, String annoOrd,
			String tipoOrd, String dataOrd, String noteOrd, int numCopieOrd,
			boolean cont, String statoOrd, String codDocOrd,
			String codTipoDocOrd, String codUrgenzaOrd,
			String codRicOffertaOrd, String idOffertaFornOrd,
			StrutturaCombo forn, String noteForn, String tipoInvioOrd,
			StrutturaTerna bil, String codPrimoOrd, String annoPrimoOrd,
			String valutaOrd, double prezzoOrd, double prezzoEuroOrd,
			String paeseOrd, String sezioneAcqOrd, String codBibliotecaSuggOrd,
			String codSuggBiblOrd, StrutturaCombo tit, String statoAbbOrd,
			String periodoValAbbOrd, String annoAbbOrd,
			String numFascicoloAbbOrd, String dataPubblFascicoloAbbOrd,
			String annataAbbOrd, int numVolAbbOrd, String dataFineAbbOrd,
			String regTribOrd, String naturaOrd, boolean rinn, boolean stamp,
			String tipoVar) throws Exception {
		/*
		 * if (tit == null) { throw new Exception("Titolo non valido"); } if
		 * (forn == null) { throw new Exception("Fornitore non valido"); } if
		 * (bil == null) { throw new Exception("Bilancio non valido"); } if
		 * (tipo == null) { throw new Exception("tipo ordine non valido"); }
		 */
		this.codPolo = codP;
		this.codBibl = codB;
		this.codOrdine = codOrd;
		this.annoOrdine = annoOrd;
		this.tipoOrdine = tipoOrd;
		this.dataOrdine = dataOrd;
		this.noteOrdine = noteOrd;
		this.numCopieOrdine = numCopieOrd;
		this.continuativo = cont;
		this.statoOrdine = statoOrd;
		this.codDocOrdine = codDocOrd;
		this.codTipoDocOrdine = codTipoDocOrd;
		this.codUrgenzaOrdine = codUrgenzaOrd;
		this.codRicOffertaOrdine = codRicOffertaOrd;
		this.idOffertaFornOrdine = idOffertaFornOrd;
		this.fornitore = forn;
		this.noteFornitore = noteForn;
		this.tipoInvioOrdine = tipoInvioOrd;
		this.bilancio = bil;
		this.codPrimoOrdine = codPrimoOrd;
		this.annoPrimoOrdine = annoPrimoOrd;
		this.valutaOrdine = valutaOrd;
		this.prezzoOrdine = prezzoOrd;
		this.prezzoEuroOrdine = prezzoEuroOrd;
		this.paeseOrdine = paeseOrd;
		this.sezioneAcqOrdine = sezioneAcqOrd;
		this.codBibliotecaSuggOrdine = codBibliotecaSuggOrd;
		this.codSuggBiblOrdine = codSuggBiblOrd;
		this.titolo = tit;
		this.statoAbbOrdine = statoAbbOrd;
		this.periodoValAbbOrdine = periodoValAbbOrd;
		this.annoAbbOrdine = annoAbbOrd;
		this.numFascicoloAbbOrdine = numFascicoloAbbOrd;
		this.dataPubblFascicoloAbbOrdine = dataPubblFascicoloAbbOrd;
		this.annataAbbOrdine = annataAbbOrd;
		this.numVolAbbOrdine = numVolAbbOrd;
		this.dataFineAbbOrdine = dataFineAbbOrd;
		this.regTribOrdine = regTribOrd;
		this.naturaOrdine = naturaOrd;
		this.rinnovato = rinn;
		this.stampato = stamp;
		this.tipoVariazione = tipoVar;
	}

	public String getCoppiaOrdinamento() {
		String coppiaOrdinamento = fillLeft(
				String.valueOf(fornitore.getCodice()).trim(), '0', 10)
				+ "|" + tipoOrdine;
		return coppiaOrdinamento;
	}

	private String fillLeft(String src, char car, int limit) {

		if (src == null)
			return null;
		if (src.length() >= limit)
			return src;

		int diff = limit - src.length();

		String tmp = "";
		for (int i = 0; i < diff; i++) {
			tmp += car;
		}
		return tmp + src;
	}

	public String getChiave() {
		String chiave = getCodBibl() + "|" + getTipoOrdine() + "|"
				+ getAnnoOrdine() + "|" + getCodOrdine();
		chiave = chiave.trim();
		return chiave;
	}

	public String getChiaveBil() {
		String chiaveBil = getBilancio().getCodice1() + "|"
				+ getBilancio().getCodice2();
		return chiaveBil;
	}

	public String getAnnataAbbOrdine() {
		return annataAbbOrdine;
	}

	public void setAnnataAbbOrdine(String annataAbbOrdine) {
		this.annataAbbOrdine = trimAndSet(annataAbbOrdine);
	}

	public String getAnnoAbbOrdine() {
		return annoAbbOrdine;
	}

	public void setAnnoAbbOrdine(String annoAbbOrdine) {
		this.annoAbbOrdine = trimAndSet(annoAbbOrdine);
	}

	public String getAnnoOrdine() {
		return annoOrdine;
	}

	public void setAnnoOrdine(String annoOrdine) {
		this.annoOrdine = annoOrdine;
	}

	public String getAnnoPrimoOrdine() {
		return annoPrimoOrdine;
	}

	public void setAnnoPrimoOrdine(String annoPrimoOrdine) {
		this.annoPrimoOrdine = annoPrimoOrdine;
	}

	public StrutturaTerna getBilancio() {
		return bilancio;
	}

	public void setBilancio(StrutturaTerna bilancio) {
		this.bilancio = bilancio;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodBibliotecaSuggOrdine() {
		return codBibliotecaSuggOrdine;
	}

	public void setCodBibliotecaSuggOrdine(String codBibliotecaSuggOrdine) {
		this.codBibliotecaSuggOrdine = codBibliotecaSuggOrdine;
	}

	public String getCodDocOrdine() {
		return codDocOrdine;
	}

	public void setCodDocOrdine(String codDocOrdine) {
		this.codDocOrdine = codDocOrdine;
	}

	public String getCodOrdine() {
		return codOrdine;
	}

	public void setCodOrdine(String codOrdine) {
		this.codOrdine = codOrdine;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodPrimoOrdine() {
		return codPrimoOrdine;
	}

	public void setCodPrimoOrdine(String codPrimoOrdine) {
		this.codPrimoOrdine = codPrimoOrdine;
	}

	public String getCodRicOffertaOrdine() {
		return codRicOffertaOrdine;
	}

	public void setCodRicOffertaOrdine(String codRicOffertaOrdine) {
		this.codRicOffertaOrdine = codRicOffertaOrdine;
	}

	public String getCodSuggBiblOrdine() {
		return codSuggBiblOrdine;
	}

	public void setCodSuggBiblOrdine(String codSuggBiblOrdine) {
		this.codSuggBiblOrdine = codSuggBiblOrdine;
	}

	public String getCodTipoDocOrdine() {
		return codTipoDocOrdine;
	}

	public void setCodTipoDocOrdine(String codTipoDocOrdine) {
		this.codTipoDocOrdine = codTipoDocOrdine;
	}

	public String getCodUrgenzaOrdine() {
		return codUrgenzaOrdine;
	}

	public void setCodUrgenzaOrdine(String codUrgenzaOrdine) {
		this.codUrgenzaOrdine = codUrgenzaOrdine;
	}

	public boolean isContinuativo() {
		return continuativo;
	}

	public void setContinuativo(boolean continuativo) {
		this.continuativo = continuativo;
	}

	public String getDataFineAbbOrdine() {
		return dataFineAbbOrdine;
	}

	public void setDataFineAbbOrdine(String dataFineAbbOrdine) {
		this.dataFineAbbOrdine = trimAndSet(dataFineAbbOrdine);
	}

	public String getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(String dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public String getDataPubblFascicoloAbbOrdine() {
		return dataPubblFascicoloAbbOrdine;
	}

	public void setDataPubblFascicoloAbbOrdine(
			String dataPubblFascicoloAbbOrdine) {
		this.dataPubblFascicoloAbbOrdine = trimAndSet(dataPubblFascicoloAbbOrdine);
	}

	public StrutturaCombo getFornitore() {
		return fornitore;
	}

	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}

	public String getIdOffertaFornOrdine() {
		return idOffertaFornOrdine;
	}

	public void setIdOffertaFornOrdine(String idOffertaFornOrdine) {
		this.idOffertaFornOrdine = idOffertaFornOrdine;
	}

	public String getNaturaOrdine() {
		return naturaOrdine;
	}

	public void setNaturaOrdine(String naturaOrdine) {
		this.naturaOrdine = naturaOrdine;
	}

	public String getNoteFornitore() {
		return noteFornitore;
	}

	public void setNoteFornitore(String noteFornitore) {
		this.noteFornitore = noteFornitore;
	}

	public String getNoteOrdine() {
		return noteOrdine;
	}

	public void setNoteOrdine(String noteOrdine) {
		this.noteOrdine = noteOrdine;
	}

	public int getNumCopieOrdine() {
		return numCopieOrdine;
	}

	public void setNumCopieOrdine(int numCopieOrdine) {
		this.numCopieOrdine = numCopieOrdine;
	}

	public String getNumFascicoloAbbOrdine() {
		return numFascicoloAbbOrdine;
	}

	public void setNumFascicoloAbbOrdine(String numFascicoloAbbOrdine) {
		this.numFascicoloAbbOrdine = trimAndSet(numFascicoloAbbOrdine);
	}

	public int getNumVolAbbOrdine() {
		return numVolAbbOrdine;
	}

	public void setNumVolAbbOrdine(int numVolAbbOrdine) {
		this.numVolAbbOrdine = numVolAbbOrdine;
	}

	public String getPaeseOrdine() {
		return paeseOrdine;
	}

	public void setPaeseOrdine(String paeseOrdine) {
		this.paeseOrdine = paeseOrdine;
	}

	public String getPeriodoValAbbOrdine() {
		return periodoValAbbOrdine;
	}

	public void setPeriodoValAbbOrdine(String periodoValAbbOrdine) {
		this.periodoValAbbOrdine = periodoValAbbOrdine;
	}

	public String getRegTribOrdine() {
		return regTribOrdine;
	}

	public void setRegTribOrdine(String regTribOrdine) {
		this.regTribOrdine = regTribOrdine;
	}

	public String getSezioneAcqOrdine() {
		return sezioneAcqOrdine;
	}

	public void setSezioneAcqOrdine(String sezioneAcqOrdine) {
		this.sezioneAcqOrdine = sezioneAcqOrdine;
	}

	public String getStatoAbbOrdine() {
		return statoAbbOrdine;
	}

	public void setStatoAbbOrdine(String statoAbbOrdine) {
		this.statoAbbOrdine = statoAbbOrdine;
	}

	public String getStatoOrdine() {
		return statoOrdine;
	}

	public void setStatoOrdine(String statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public String getTipoInvioOrdine() {
		return tipoInvioOrdine;
	}

	public void setTipoInvioOrdine(String tipoInvioOrdine) {
		this.tipoInvioOrdine = tipoInvioOrdine;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public StrutturaCombo getTitolo() {
		return titolo;
	}

	public void setTitolo(StrutturaCombo titolo) {
		this.titolo = titolo;
	}

	public String getValutaOrdine() {
		return valutaOrdine;
	}

	public void setValutaOrdine(String valutaOrdine) {
		this.valutaOrdine = valutaOrdine;
	}

	public boolean isStampato() {
		return stampato;
	}

	public void setStampato(boolean stampato) {
		this.stampato = stampato;
	}

	public String getTipoVariazione() {
		return tipoVariazione;
	}

	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public boolean isRinnovato() {
		return rinnovato;
	}

	public void setRinnovato(boolean rinnovato) {
		this.rinnovato = rinnovato;
	}

	public Integer getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public int getIDOrd() {
		return IDOrd;
	}

	public void setIDOrd(int ord) {
		IDOrd = ord;
	}

	public int getIDBil() {
		return IDBil;
	}

	public void setIDBil(int bil) {
		IDBil = bil;
	}

	public int getIDSez() {
		return IDSez;
	}

	public void setIDSez(int sez) {
		IDSez = sez;
	}

	public int getIDVal() {
		return IDVal;
	}

	public void setIDVal(int val) {
		IDVal = val;
	}

	public String getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(String dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public boolean isFlag_canc() {
		return flag_canc;
	}

	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}

	public double getPrezzoEuroOrdine() {
		return prezzoEuroOrdine;
	}

	public void setPrezzoEuroOrdine(double prezzoEuroOrdine) {
		this.prezzoEuroOrdine = prezzoEuroOrdine;
	}

	public String getPrezzoEuroOrdineStr() {
		return prezzoEuroOrdineStr;
	}

	public void setPrezzoEuroOrdineStr(String prezzoEuroOrdineStr) {
		this.prezzoEuroOrdineStr = prezzoEuroOrdineStr;
	}

	public double getPrezzoOrdine() {
		return prezzoOrdine;
	}

	public void setPrezzoOrdine(double prezzoOrdine) {
		this.prezzoOrdine = prezzoOrdine;
	}

	public String getPrezzoOrdineStr() {
		return prezzoOrdineStr;
	}

	public void setPrezzoOrdineStr(String prezzoOrdineStr) {
		this.prezzoOrdineStr = prezzoOrdineStr;
	}

	public List<StrutturaInventariOrdVO> getRigheInventariRilegatura() {
		return righeInventariRilegatura;
	}

	public void setRigheInventariRilegatura(
			List<StrutturaInventariOrdVO> righeInventariRilegatura) {
		this.righeInventariRilegatura = righeInventariRilegatura;
	}

	public String getDataStampaOrdine() {
		return dataStampaOrdine;
	}

	public void setDataStampaOrdine(String dataStampaOrdine) {
		this.dataStampaOrdine = dataStampaOrdine;
	}

	public boolean isGestBil() {
		return gestBil;
	}

	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}

	public boolean isGestProf() {
		return gestProf;
	}

	public void setGestProf(boolean gestProf) {
		this.gestProf = gestProf;
	}

	public boolean isGestSez() {
		return gestSez;
	}

	public void setGestSez(boolean gestSez) {
		this.gestSez = gestSez;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public boolean isFornitoreLocalizzato() {
		return fornitoreLocalizzato;
	}

	public void setFornitoreLocalizzato(boolean fornitoreLocalizzato) {
		this.fornitoreLocalizzato = fornitoreLocalizzato;
	}

	public FornitoreVO getAnagFornitore() {
		return anagFornitore;
	}

	public void setAnagFornitore(FornitoreVO anagFornitore) {
		this.anagFornitore = anagFornitore;
	}

	public String getTitoloIsbn() {
		return titoloIsbn;
	}

	public void setTitoloIsbn(String titoloIsbn) {
		this.titoloIsbn = titoloIsbn;
	}

	public String getTitoloIssn() {
		return titoloIssn;
	}

	public void setTitoloIssn(String titoloIssn) {
		this.titoloIssn = titoloIssn;
	}

	public boolean isRiapertura() {
		return riapertura;
	}

	public void setRiapertura(boolean riapertura) {
		this.riapertura = riapertura;
	}

	public Timestamp getDataUpd() {
		return dataUpd;
	}

	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}

	public StrutturaTerna getRinnovoOrigine() {
		return rinnovoOrigine;
	}

	public void setRinnovoOrigine(StrutturaTerna rinnovoOrigine) {
		this.rinnovoOrigine = rinnovoOrigine;
	}

	public String getEsistenzaInventariLegati() {
		return esistenzaInventariLegati;
	}

	public void setEsistenzaInventariLegati(String esistenzaInventariLegati) {
		this.esistenzaInventariLegati = esistenzaInventariLegati;
	}

	public List<StrutturaTerna> getNumStandardArr() {
		return numStandardArr;
	}

	public void setNumStandardArr(List<StrutturaTerna> numStandardArr) {
		this.numStandardArr = numStandardArr;
	}

	public String getPrezzoStr() {
		String prezzoStr = null;
		try {
			double prezzoStrAppo = this.getPrezzoEuroOrdine();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setGroupingSeparator('.');
			dfs.setDecimalSeparator(',');
			// controllo formattazione con virgola separatore dei decimali
			DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
			// importo
			String stringa = df.format(prezzoStrAppo);
			NumberFormat formatIT = NumberFormat.getCurrencyInstance();
			String strCurrency = "\u20AC ";
			strCurrency = strCurrency + stringa;
			// Number imp=formatIT.parse(strCurrency); // va in errore se non è
			// riconosciuto come formato euro
			prezzoStr = formatIT.format(prezzoStrAppo);
			prezzoStr = prezzoStr.substring(2); // elimina il simbolo

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prezzoStr;
	}

	public String getCd_tipo_lav() {
		return cd_tipo_lav;
	}

	public void setCd_tipo_lav(String cd_tipo_lav) {
		this.cd_tipo_lav = cd_tipo_lav;
	}

	public OrdineCarrelloSpedizioneVO getOrdineCarrelloSpedizione() {
		return ordineCarrelloSpedizione;
	}

	public void setOrdineCarrelloSpedizione(
			OrdineCarrelloSpedizioneVO ordineCarrelloSpedizione) {
		this.ordineCarrelloSpedizione = ordineCarrelloSpedizione;
	}

	public boolean isSpedito() {
		OrdineCarrelloSpedizioneVO ocs = this.ordineCarrelloSpedizione;
		boolean spedito = ocs != null && !ocs.getFlCanc().equals("S") && ocs.getTsVar() != null;
		return spedito || (stampato && !isGoogle() );
	}

	public boolean isGoogle() {
		if (!ValidazioneDati.equals(tipoOrdine, "R") )
			return false;

		if (fornitore == null)
			return false;

		String cdForn = fornitore.getCodice();
		if (!isNumeric(cdForn) || !isFilled(cd_forn_google))
			return false;

		return Integer.valueOf(cdForn).intValue() == cd_forn_google;
	}

	public String getChiaveOrdine() {
		StringBuilder buf = new StringBuilder(512);
		buf.append(annoOrdine);
		buf.append(" ");
		buf.append(tipoOrdine);
		buf.append(" ");
		buf.append(codOrdine);

		return buf.toString();
	}

	public Integer getCd_forn_google() {
		return cd_forn_google;
	}

	public void setCd_forn_google(Integer cd_forn_google) {
		this.cd_forn_google = cd_forn_google;
	}

}
