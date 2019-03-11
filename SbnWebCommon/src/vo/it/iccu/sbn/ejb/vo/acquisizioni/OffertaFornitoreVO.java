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

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;

import java.sql.Timestamp;
import java.util.List;



public class OffertaFornitoreVO  extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 6516540872499914566L;
	private Integer progressivo=0;
	private String utente;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private int IDOff;
	private StrutturaCombo tipoProvenienza;
	private String identificativoOfferta;
	private String dataOfferta;
	private StrutturaCombo fornitore;
	private String statoOfferta;
	private String valutaOfferta;
	private String tipoPrezzo;
	private String prezzo;
	private String informazioniPrezzo;
	private String tipoData; //
	private String data; //
	private StrutturaCombo paese;
	private StrutturaCombo lingua;
	private String codiceStandard;
	private String numeroStandard;
	private StrutturaCombo bid;
	private String naturaBid;
	private StrutturaCombo chiaveTitoloIsbd;
	private String tipoVariazione;
	private List<OffertaFornitoreAutoreVO> offFornAut;
	private List<OffertaFornitoreClassificazioneVO> offFornClass;
	private List<OffertaFornitoreIsbdVO> offFornIsbd;
	private List<OffertaFornitoreNoteEdiVO> offFornNote;
	private List<OffertaFornitoreSerieVO> offFornSerie;
	private List<OffertaFornitoreSoggettoVO> offFornSogg;
	private boolean flag_canc=false;
	private Timestamp dataUpd;



	public OffertaFornitoreVO (){};
	public OffertaFornitoreVO (String codP, String codB, StrutturaCombo tipoProv, String idOff, String dataOff, StrutturaCombo forn, String statoOff, String valOff, String tipoPre, String pre, String infPre, String tipData, String data, StrutturaCombo pae , StrutturaCombo ling, String codStand, String numStand, StrutturaCombo bidOff, StrutturaCombo KTitIsdb) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.codPolo=codP;
		this.codBibl=codB;
		this.tipoProvenienza=tipoProv;
		this.identificativoOfferta=idOff;
		this.dataOfferta=dataOff;
		this.fornitore=forn;
		this.statoOfferta=statoOff;
		this.valutaOfferta=valOff;
		this.tipoPrezzo=tipoPre;
		this.prezzo=pre;
		this.informazioniPrezzo=infPre;
		this.tipoData=tipData;
		this.data=data;
		this.paese=pae;
		this.lingua=ling;
		this.codiceStandard=codStand;
		this.numeroStandard=numStand;
		this.bid=bidOff;
		this.chiaveTitoloIsbd=KTitIsdb;

	}

	public String getChiave() {
		String chiave=getCodBibl()+ "|" +  getIdentificativoOfferta() ;
		chiave=chiave.trim();
		return chiave;
	}
	public StrutturaCombo getBid() {
		return bid;
	}
	public void setBid(StrutturaCombo bid) {
		this.bid = bid;
	}
	public StrutturaCombo getChiaveTitoloIsbd() {
		return chiaveTitoloIsbd;
	}
	public void setChiaveTitoloIsbd(StrutturaCombo chiaveTitoloIsbd) {
		this.chiaveTitoloIsbd = chiaveTitoloIsbd;
	}
	public String getCodBibl() {
		return codBibl;
	}
	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}
	public String getCodiceStandard() {
		return codiceStandard;
	}
	public void setCodiceStandard(String codiceStandard) {
		this.codiceStandard = codiceStandard;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDataOfferta() {
		return dataOfferta;
	}
	public void setDataOfferta(String dataOfferta) {
		this.dataOfferta = dataOfferta;
	}
	public StrutturaCombo getFornitore() {
		return fornitore;
	}
	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}
	public String getIdentificativoOfferta() {
		return identificativoOfferta;
	}
	public void setIdentificativoOfferta(String identificativoOfferta) {
		this.identificativoOfferta = identificativoOfferta;
	}
	public String getInformazioniPrezzo() {
		return informazioniPrezzo;
	}
	public void setInformazioniPrezzo(String informazioniPrezzo) {
		this.informazioniPrezzo = informazioniPrezzo;
	}
	public StrutturaCombo getLingua() {
		return lingua;
	}
	public void setLingua(StrutturaCombo lingua) {
		this.lingua = lingua;
	}
	public String getNumeroStandard() {
		return numeroStandard;
	}
	public void setNumeroStandard(String numeroStandard) {
		this.numeroStandard = numeroStandard;
	}
	public List<OffertaFornitoreAutoreVO> getOffFornAut() {
		return offFornAut;
	}
	public void setOffFornAut(List<OffertaFornitoreAutoreVO> offFornAut) {
		this.offFornAut = offFornAut;
	}
	public List<OffertaFornitoreClassificazioneVO> getOffFornClass() {
		return offFornClass;
	}
	public void setOffFornClass(
			List<OffertaFornitoreClassificazioneVO> offFornClass) {
		this.offFornClass = offFornClass;
	}
	public List<OffertaFornitoreIsbdVO> getOffFornIsbd() {
		return offFornIsbd;
	}
	public void setOffFornIsbd(List<OffertaFornitoreIsbdVO> offFornIsbd) {
		this.offFornIsbd = offFornIsbd;
	}
	public List<OffertaFornitoreNoteEdiVO> getOffFornNote() {
		return offFornNote;
	}
	public void setOffFornNote(List<OffertaFornitoreNoteEdiVO> offFornNote) {
		this.offFornNote = offFornNote;
	}
	public List<OffertaFornitoreSerieVO> getOffFornSerie() {
		return offFornSerie;
	}
	public void setOffFornSerie(List<OffertaFornitoreSerieVO> offFornSerie) {
		this.offFornSerie = offFornSerie;
	}
	public List<OffertaFornitoreSoggettoVO> getOffFornSogg() {
		return offFornSogg;
	}
	public void setOffFornSogg(List<OffertaFornitoreSoggettoVO> offFornSogg) {
		this.offFornSogg = offFornSogg;
	}
	public StrutturaCombo getPaese() {
		return paese;
	}
	public void setPaese(StrutturaCombo paese) {
		this.paese = paese;
	}
	public String getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}
	public String getStatoOfferta() {
		return statoOfferta;
	}
	public void setStatoOfferta(String statoOfferta) {
		this.statoOfferta = statoOfferta;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoData() {
		return tipoData;
	}
	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}
	public String getTipoPrezzo() {
		return tipoPrezzo;
	}
	public void setTipoPrezzo(String tipoPrezzo) {
		this.tipoPrezzo = tipoPrezzo;
	}

	public String getTipoVariazione() {
		return tipoVariazione;
	}
	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}
	public String getValutaOfferta() {
		return valutaOfferta;
	}
	public void setValutaOfferta(String valutaOfferta) {
		this.valutaOfferta = valutaOfferta;
	}
	public StrutturaCombo getTipoProvenienza() {
		return tipoProvenienza;
	}
	public void setTipoProvenienza(StrutturaCombo tipoProvenienza) {
		this.tipoProvenienza = tipoProvenienza;
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
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public int getIDOff() {
		return IDOff;
	}
	public void setIDOff(int off) {
		IDOff = off;
	}
	public String getNaturaBid() {
		return naturaBid;
	}
	public void setNaturaBid(String naturaBid) {
		this.naturaBid = naturaBid;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}


}
