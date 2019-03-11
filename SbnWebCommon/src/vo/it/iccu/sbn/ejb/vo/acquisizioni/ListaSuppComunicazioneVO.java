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
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;

import java.util.List;

public class ListaSuppComunicazioneVO extends SerializableVO {

	private static final long serialVersionUID = 8560704876056529642L;

	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String codiceMessaggio;
	private String tipoDocumento;
	private String tipoMessaggio;
	private StrutturaCombo fornitore;
	private StrutturaTerna idDocumento;
	private String statoComunicazione;
	private String dataComunicazioneDa;
	private String dataComunicazioneA;
	private String direzioneComunicazione;
	private String tipoInvioComunicazione;
	private String chiamante;
	private List<ComunicazioneVO> selezioniChiamato;
	private String ordinamento;
	private boolean flag_canc = false;
	private boolean modalitaSif = true;

	private String note;
	private List<FascicoloVO> fascicoli;


	public ListaSuppComunicazioneVO() {
		super();
	}

	public ListaSuppComunicazioneVO(String codP, String codB, String codMsg,
			String tipoDoc, String tipoMsg, StrutturaCombo forn,
			StrutturaTerna idDoc, String statoCom, String dataComDa,
			String dataComA, String dirCom, String tipoInvioCom, String chiama,
			String ordina) throws Exception {
		// if (ese == null) {
		// throw new Exception("Esercizio non valido");
		// }
		this.codPolo = codP;
		this.codBibl = codB;
		this.codPolo = codP;
		this.codBibl = codB;
		this.codiceMessaggio = codMsg;
		this.tipoDocumento = tipoDoc;
		this.tipoMessaggio = tipoMsg;
		this.fornitore = forn;
		this.idDocumento = idDoc;
		this.statoComunicazione = statoCom;
		this.dataComunicazioneDa = dataComDa;
		this.dataComunicazioneA = dataComA;
		this.direzioneComunicazione = dirCom;
		this.tipoInvioComunicazione = tipoInvioCom;
		this.chiamante = chiama;
		this.ordinamento = ordina;
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

	public String getCodiceMessaggio() {
		return codiceMessaggio;
	}

	public void setCodiceMessaggio(String codiceMessaggio) {
		this.codiceMessaggio = codiceMessaggio;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getDataComunicazioneA() {
		return dataComunicazioneA;
	}

	public void setDataComunicazioneA(String dataComunicazioneA) {
		this.dataComunicazioneA = dataComunicazioneA;
	}

	public String getDataComunicazioneDa() {
		return dataComunicazioneDa;
	}

	public void setDataComunicazioneDa(String dataComunicazioneDa) {
		this.dataComunicazioneDa = dataComunicazioneDa;
	}

	public String getDirezioneComunicazione() {
		return direzioneComunicazione;
	}

	public void setDirezioneComunicazione(String direzioneComunicazione) {
		this.direzioneComunicazione = direzioneComunicazione;
	}

	public StrutturaCombo getFornitore() {
		return fornitore;
	}

	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}

	public StrutturaTerna getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(StrutturaTerna idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public List<ComunicazioneVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}

	public void setSelezioniChiamato(
			List<ComunicazioneVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}

	public String getStatoComunicazione() {
		return statoComunicazione;
	}

	public void setStatoComunicazione(String statoComunicazione) {
		this.statoComunicazione = statoComunicazione;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoInvioComunicazione() {
		return tipoInvioComunicazione;
	}

	public void setTipoInvioComunicazione(String tipoInvioComunicazione) {
		this.tipoInvioComunicazione = tipoInvioComunicazione;
	}

	public String getTipoMessaggio() {
		return tipoMessaggio;
	}

	public void setTipoMessaggio(String tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public List<FascicoloVO> getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(List<FascicoloVO> fascicoli) {
		this.fascicoli = fascicoli;
	}

}
