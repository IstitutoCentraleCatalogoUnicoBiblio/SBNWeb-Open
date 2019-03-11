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

import it.iccu.sbn.ejb.vo.SerializableVO;

public class StampaRichiestaVO extends SerializableVO {

	private static final long serialVersionUID = -7399231402309794695L;

	private String descBib;
	private String idRichiesta;
	private String codStatoRic;
	private String tipoServizio;
	private String codBibUte;
	private String codUte;
	private String cognomeNome;
	private String dataInizioEff;
	private String dataFineEff;
	private String dataProroga;
	private String natura;
	private String costoServizio;
	private String segnatura;
	private String numFascicolo;
	private String numVolume;
	private String annoPeriodico;
	private String bid;
	private String titolo;
	private String codBibInv;
	private String codSerieInv;
	private String codInvenInv;
	// private String flTipoRec;
	private String attivitaCorrente;
	private String dataDiElaborazione;
	private String testoStampaModulo;

	//almaviva5_20110127 #4179
	private String dataInizio;
	private boolean consegnato;
	private String pagine;
	private String datiDocumento;

	//almaviva5_20171122 prenotazione posto
	private boolean prenotazionePosto;
	private String descrizioneSala;
	private String salaOrarioInizio;
	private String salaOrarioFine;
	private String barcodeInv;

	//almaviva5_20190128
	private String dataRichiesta;

	public String getAttivitaCorrente() {
		return attivitaCorrente;
	}

	public void setAttivitaCorrente(String attivitaCorrente) {
		this.attivitaCorrente = attivitaCorrente;
	}

	public String getTestoStampaModulo() {
		return testoStampaModulo;
	}

	public void setTestoStampaModulo(String testoStampaModulo) {
		this.testoStampaModulo = testoStampaModulo;
	}

	public String getDataDiElaborazione() {
		return dataDiElaborazione;
	}

	public void setDataDiElaborazione(String dataDiElaborazione) {
		this.dataDiElaborazione = dataDiElaborazione;
	}

	public String getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public String getCodStatoRic() {
		return codStatoRic;
	}

	public void setCodStatoRic(String codStatoRic) {
		this.codStatoRic = codStatoRic;
	}

	public String getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public String getCodBibUte() {
		return codBibUte;
	}

	public void setCodBibUte(String codBibUte) {
		this.codBibUte = codBibUte;
	}

	public String getCodUte() {
		return codUte;
	}

	public void setCodUte(String codUte) {
		this.codUte = codUte;
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

	public String getDataInizioEff() {
		return dataInizioEff;
	}

	public void setDataInizioEff(String dataInizioEff) {
		this.dataInizioEff = dataInizioEff;
	}

	public String getDataFineEff() {
		return dataFineEff;
	}

	public void setDataFineEff(String dataFineEff) {
		this.dataFineEff = dataFineEff;
	}

	public String getDataProroga() {
		return dataProroga;
	}

	public void setDataProroga(String dataProroga) {
		this.dataProroga = dataProroga;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getCostoServizio() {
		return costoServizio;
	}

	public void setCostoServizio(String costoServizio) {
		this.costoServizio = costoServizio;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}

	public String getNumFascicolo() {
		return numFascicolo;
	}

	public void setNumFascicolo(String numFascicolo) {
		this.numFascicolo = numFascicolo;
	}

	public String getNumVolume() {
		return numVolume;
	}

	public void setNumVolume(String numVolume) {
		this.numVolume = numVolume;
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
		this.bid = bid;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getCodBibInv() {
		return codBibInv;
	}

	public void setCodBibInv(String codBibInv) {
		this.codBibInv = codBibInv;
	}

	public String getCodSerieInv() {
		return codSerieInv;
	}

	public void setCodSerieInv(String codSerieInv) {
		this.codSerieInv = codSerieInv;
	}

	public String getCodInvenInv() {
		return codInvenInv;
	}

	public void setCodInvenInv(String codInvenInv) {
		this.codInvenInv = codInvenInv;
	}

	public String getDescBib() {
		return descBib;
	}

	public void setDescBib(String descBib) {
		this.descBib = trimAndSet(descBib);
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public boolean isConsegnato() {
		return consegnato;
	}

	public void setConsegnato(boolean consegnato) {
		this.consegnato = consegnato;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public String getPagine() {
		return pagine;
	}

	public void setPagine(String pagine) {
		this.pagine = pagine;
	}

	public String getDatiDocumento() {
		return datiDocumento;
	}

	public void setDatiDocumento(String datiDocumento) {
		this.datiDocumento = datiDocumento;
	}

	public boolean isPrenotazionePosto() {
		return prenotazionePosto;
	}

	public void setPrenotazionePosto(boolean prenotazionePosto) {
		this.prenotazionePosto = prenotazionePosto;
	}

	public String getDescrizioneSala() {
		return descrizioneSala;
	}

	public void setDescrizioneSala(String descrizioneSala) {
		this.descrizioneSala = trimAndSet(descrizioneSala);
	}

	public String getSalaOrarioInizio() {
		return salaOrarioInizio;
	}

	public void setSalaOrarioInizio(String salaOrarioInizio) {
		this.salaOrarioInizio = trimAndSet(salaOrarioInizio);
	}

	public String getSalaOrarioFine() {
		return salaOrarioFine;
	}

	public void setSalaOrarioFine(String salaOrarioFine) {
		this.salaOrarioFine = trimAndSet(salaOrarioFine);
	}

	public String getBarcodeInv() {
		return barcodeInv;
	}

	public void setBarcodeInv(String barcodeInv) {
		this.barcodeInv = barcodeInv;
	}

	public String getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(String dataRichiesta) {
		this.dataRichiesta = trimAndSet(dataRichiesta);
	}
}
