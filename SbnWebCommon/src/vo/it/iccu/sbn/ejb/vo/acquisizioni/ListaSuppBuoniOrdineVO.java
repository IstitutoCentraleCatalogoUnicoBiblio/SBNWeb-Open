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

import java.util.List;

public class ListaSuppBuoniOrdineVO extends SerializableVO {

	private static final long serialVersionUID = -3977394053497166314L;

	public enum TipoOperazione {
		GESTIONE,
		CERCA_DA_ORDINE,
		CREA_DA_ORDINE,
		SCEGLI_DA_ORDINE,
		ESAMINA_DA_ORDINE;
	}

	private String codPolo;
	private String codBibl;
	private List<Integer> idBuoOrdList;
	private String numBuonoOrdineDa;
	private String numBuonoOrdineA;
	private String dataBuonoOrdineDa;
	private String dataBuonoOrdineA;
	private String statoBuonoOrdine;
	private StrutturaTerna ordine;
	private StrutturaCombo fornitore;
	private StrutturaTerna bilancio;
	private String chiamante;
	private List<BuoniOrdineVO> selezioniChiamato;
	private String ordinamento;
	private String ticket;
	private int elemXBlocchi;
	private boolean flag_canc = false;
	private boolean modalitaSif = true;

	private TipoOperazione tipoOperazione = TipoOperazione.GESTIONE;
	private OrdiniVO datiOrdine;

	// costruttore
	public ListaSuppBuoniOrdineVO() {
		super();
	};

	public ListaSuppBuoniOrdineVO(String codP, String codB, String numDa,
			String numA, String dataDa, String dataA, String stato,
			StrutturaTerna ord, StrutturaCombo forn, StrutturaTerna bil,
			String chiama, String ordina) throws Exception {

		this.codPolo = codP;
		this.codBibl = codB;
		this.numBuonoOrdineDa = numDa;
		this.numBuonoOrdineA = numA;
		this.dataBuonoOrdineDa = dataDa;
		this.dataBuonoOrdineA = dataA;
		this.statoBuonoOrdine = stato;
		this.ordine = ord;
		this.fornitore = forn;
		this.bilancio = bil;
		this.chiamante = chiama;
		this.ordinamento = ordina;
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

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getDataBuonoOrdineA() {
		return dataBuonoOrdineA;
	}

	public void setDataBuonoOrdineA(String dataBuonoOrdineA) {
		this.dataBuonoOrdineA = dataBuonoOrdineA;
	}

	public String getDataBuonoOrdineDa() {
		return dataBuonoOrdineDa;
	}

	public void setDataBuonoOrdineDa(String dataBuonoOrdineDa) {
		this.dataBuonoOrdineDa = dataBuonoOrdineDa;
	}

	public StrutturaCombo getFornitore() {
		return fornitore;
	}

	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}

	public String getNumBuonoOrdineA() {
		return numBuonoOrdineA;
	}

	public void setNumBuonoOrdineA(String numBuonoOrdineA) {
		this.numBuonoOrdineA = numBuonoOrdineA;
	}

	public String getNumBuonoOrdineDa() {
		return numBuonoOrdineDa;
	}

	public void setNumBuonoOrdineDa(String numBuonoOrdineDa) {
		this.numBuonoOrdineDa = numBuonoOrdineDa;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public StrutturaTerna getOrdine() {
		return ordine;
	}

	public void setOrdine(StrutturaTerna ordine) {
		this.ordine = ordine;
	}

	public String getStatoBuonoOrdine() {
		return statoBuonoOrdine;
	}

	public void setStatoBuonoOrdine(String statoBuonoOrdine) {
		this.statoBuonoOrdine = statoBuonoOrdine;
	}

	public List<BuoniOrdineVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}

	public void setSelezioniChiamato(List<BuoniOrdineVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
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

	public boolean isFlag_canc() {
		return flag_canc;
	}

	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}

	public boolean isModalitaSif() {
		return modalitaSif;
	}

	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}

	public List<Integer> getIdBuoOrdList() {
		return idBuoOrdList;
	}

	public void setIdBuoOrdList(List<Integer> idBuoOrdList) {
		this.idBuoOrdList = idBuoOrdList;
	}

	public TipoOperazione getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(TipoOperazione sifDaOrdine) {
		this.tipoOperazione = sifDaOrdine;
	}

	public OrdiniVO getDatiOrdine() {
		return datiOrdine;
	}

	public void setDatiOrdine(OrdiniVO datiOrdine) {
		this.datiOrdine = datiOrdine;
	}

}
