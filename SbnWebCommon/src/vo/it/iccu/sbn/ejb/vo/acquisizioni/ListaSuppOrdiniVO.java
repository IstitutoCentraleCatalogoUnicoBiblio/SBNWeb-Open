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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;

import java.util.List;

public class ListaSuppOrdiniVO extends SerializableVO {

	private static final long serialVersionUID = 274951516661445155L;

	private int elemXBlocchi;
	private int IDOrd;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String denoBiblStampe; // non per filtro ma per passaggio di dato
									// per stampe
	private String codBiblAffil;
	private String codOrdine;
	private List<String> bidList;
	private List<Integer> idOrdList;
	private String annoOrdine;
	private String tipoOrdine;
	private String dataOrdineDa; // dataUscita per bollettario
	private String dataOrdineA;
	private String dataStampaOrdineDa; // dataRientro per bollettario
	private String dataStampaOrdineA;
	private String continuativo;
	private String statoOrdine; //
	private StrutturaCombo fornitore;
	private String tipoInvioOrdine;
	private StrutturaTerna bilancio;
	private String sezioneAcqOrdine;
	private StrutturaCombo titolo;
	private String dataFineAbbOrdineDa; // dataRientroPresunta per bollettario
	private String dataFineAbbOrdineA;
	private String naturaOrdine;
	private boolean rinnovato = false;
	private boolean stampato = false;
	private String chiamante;
	private List<OrdiniVO> selezioniChiamato;
	private String[] statoOrdineArr;
	private String[] tipoOrdineArr;
	private String ordinamento;
	private boolean flag_canc = false;
	private OrdiniVO ordineDuplicatoRinnovato;
	private boolean modalitaSif = true;
	private boolean bollettarioSTP = false;
	private boolean soloInRilegatura = false;
	private boolean attivatoDaRicerca = false;
	private String stampatoStr = "";
	private String rinnovatoStr = "";
	private boolean stampaFiledistinti = false;
	private Boolean ordNOinv = false; // per la ricerca di ordini privi di
										// nventari vedi metodo ripartoSpese di
										// generic

	private InventarioVO inventarioCollegato;

	public ListaSuppOrdiniVO() {
		super();
	};

	public ListaSuppOrdiniVO(String codP, String codB, String codBAff,
			String codOrd, String annoOrd, String tipoOrd, String dataOrdDa,
			String dataOrdA, String cont, String statoOrd, StrutturaCombo forn,
			String tipoInvioOrd, StrutturaTerna bil, String sezioneAcqOrd,
			StrutturaCombo tit, String dataFineAbbOrdDa, String dataFineAbbOrdA,
			String naturaOrd, String chiama, String[] statoOrdArr,
			Boolean rinn, Boolean stamp) throws Exception {

		this.codPolo = codP;
		this.codBibl = codB;
		this.codBiblAffil = codBAff;
		this.codOrdine = codOrd;
		this.annoOrdine = annoOrd;
		this.tipoOrdine = tipoOrd;
		this.dataOrdineDa = dataOrdDa;
		this.dataOrdineA = dataOrdA;
		this.continuativo = cont;
		this.statoOrdine = statoOrd;
		this.fornitore = forn;
		this.tipoInvioOrdine = tipoInvioOrd;
		this.bilancio = bil;
		this.sezioneAcqOrdine = sezioneAcqOrd;
		this.titolo = tit;
		this.dataFineAbbOrdineDa = dataFineAbbOrdDa;
		this.dataFineAbbOrdineA = dataFineAbbOrdA;
		this.naturaOrdine = naturaOrd;
		this.chiamante = chiama;
		this.statoOrdineArr = statoOrdArr;
		this.rinnovato = rinn;
		this.stampato = stamp;

	}

	public ListaSuppOrdiniVO(OrdiniVO o) {
		this.codPolo = o.getCodPolo();
		this.codBibl = o.getCodBibl();
		this.IDOrd = o.getIDOrd();
	}

	public String getAnnoOrdine() {
		return annoOrdine;
	}

	public void setAnnoOrdine(String annoOrdine) {
		this.annoOrdine = annoOrdine;
	}

	public StrutturaTerna getBilancio() {
		return bilancio;
	}

	public void setBilancio(StrutturaTerna bilancio) {
		this.bilancio = bilancio;
	}

	public String getChiamante() {
		return chiamante;
	}

	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
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

	public StrutturaCombo getFornitore() {
		return fornitore;
	}

	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}

	public String getNaturaOrdine() {
		return naturaOrdine;
	}

	public void setNaturaOrdine(String naturaOrdine) {
		this.naturaOrdine = naturaOrdine;
	}

	public List<OrdiniVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}

	public void setSelezioniChiamato(List<OrdiniVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}

	public String getSezioneAcqOrdine() {
		return sezioneAcqOrdine;
	}

	public void setSezioneAcqOrdine(String sezioneAcqOrdine) {
		this.sezioneAcqOrdine = sezioneAcqOrdine;
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

	public String getDataFineAbbOrdineA() {
		return dataFineAbbOrdineA;
	}

	public void setDataFineAbbOrdineA(String dataFineAbbOrdineA) {
		this.dataFineAbbOrdineA = dataFineAbbOrdineA;
	}

	public String getDataFineAbbOrdineDa() {
		return dataFineAbbOrdineDa;
	}

	public void setDataFineAbbOrdineDa(String dataFineAbbOrdineDa) {
		this.dataFineAbbOrdineDa = dataFineAbbOrdineDa;
	}

	public String getDataOrdineA() {
		return dataOrdineA;
	}

	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}

	public String getDataOrdineDa() {
		return dataOrdineDa;
	}

	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}

	public String getContinuativo() {
		return continuativo;
	}

	public void setContinuativo(String continuativo) {
		this.continuativo = continuativo;
	}

	public String getCodBiblAffil() {
		return codBiblAffil;
	}

	public void setCodBiblAffil(String codBiblAffil) {
		this.codBiblAffil = codBiblAffil;
	}

	public String[] getStatoOrdineArr() {
		return statoOrdineArr;
	}

	public void setStatoOrdineArr(String[] statoOrdineArr) {
		this.statoOrdineArr = statoOrdineArr;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public boolean isRinnovato() {
		return rinnovato;
	}

	public void setRinnovato(boolean rinnovato) {
		this.rinnovato = rinnovato;
	}

	public boolean isStampato() {
		return stampato;
	}

	public void setStampato(boolean stampato) {
		this.stampato = stampato;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public boolean isFlag_canc() {
		return flag_canc;
	}

	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}

	public String getDataStampaOrdineA() {
		return dataStampaOrdineA;
	}

	public void setDataStampaOrdineA(String dataStampaOrdineA) {
		this.dataStampaOrdineA = dataStampaOrdineA;
	}

	public String getDataStampaOrdineDa() {
		return dataStampaOrdineDa;
	}

	public void setDataStampaOrdineDa(String dataStampaOrdineDa) {
		this.dataStampaOrdineDa = dataStampaOrdineDa;
	}

	public OrdiniVO getOrdineDuplicatoRinnovato() {
		return ordineDuplicatoRinnovato;
	}

	public void setOrdineDuplicatoRinnovato(OrdiniVO ordineDuplicatoRinnovato) {
		this.ordineDuplicatoRinnovato = ordineDuplicatoRinnovato;
	}

	public int getIDOrd() {
		return IDOrd;
	}

	public void setIDOrd(int ord) {
		IDOrd = ord;
	}

	public List<String> getBidList() {
		return bidList;
	}

	public void setBidList(List<String> bidList) {
		this.bidList = bidList;
	}

	public boolean isModalitaSif() {
		return modalitaSif;
	}

	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}

	public List<Integer> getIdOrdList() {
		return idOrdList;
	}

	public void setIdOrdList(List<Integer> idOrdList) {
		this.idOrdList = idOrdList;
	}

	public boolean isBollettarioSTP() {
		return bollettarioSTP;
	}

	public void setBollettarioSTP(boolean bollettarioSTP) {
		this.bollettarioSTP = bollettarioSTP;
	}

	public boolean isSoloInRilegatura() {
		return soloInRilegatura;
	}

	public void setSoloInRilegatura(boolean soloInRilegatura) {
		this.soloInRilegatura = soloInRilegatura;
	}

	public String[] getTipoOrdineArr() {
		return tipoOrdineArr;
	}

	public void setTipoOrdineArr(String[] tipoOrdineArr) {
		this.tipoOrdineArr = tipoOrdineArr;
	}

	public String getDenoBiblStampe() {
		return denoBiblStampe;
	}

	public void setDenoBiblStampe(String denoBiblStampe) {
		this.denoBiblStampe = denoBiblStampe;
	}

	public boolean isAttivatoDaRicerca() {
		return attivatoDaRicerca;
	}

	public void setAttivatoDaRicerca(boolean attivatoDaRicerca) {
		this.attivatoDaRicerca = attivatoDaRicerca;
	}

	public String getRinnovatoStr() {
		return rinnovatoStr;
	}

	public void setRinnovatoStr(String rinnovatoStr) {
		this.rinnovatoStr = rinnovatoStr;
	}

	public String getStampatoStr() {
		return stampatoStr;
	}

	public void setStampatoStr(String stampatoStr) {
		this.stampatoStr = stampatoStr;
	}

	public boolean isStampaFiledistinti() {
		return stampaFiledistinti;
	}

	public void setStampaFiledistinti(boolean stampaFiledistinti) {
		this.stampaFiledistinti = stampaFiledistinti;
	}

	public Boolean getOrdNOinv() {
		return ordNOinv;
	}

	public void setOrdNOinv(Boolean ordNOinv) {
		this.ordNOinv = ordNOinv;
	}

	public InventarioVO getInventarioCollegato() {
		return inventarioCollegato;
	}

	public void setInventarioCollegato(InventarioVO inventarioCollegato) {
		this.inventarioCollegato = inventarioCollegato;
	}
}
