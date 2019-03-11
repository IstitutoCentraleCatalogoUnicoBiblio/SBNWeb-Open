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


/**
 * VO per il dettaglio della stampa registro topografico
 */
public class StampaStrumentiPossedutoDettaglioVO extends StampaStrumentiPatrimonioVO{
	/**
	 *
	 */
	private static final long serialVersionUID = -543900038710172232L;

	private String codBibSez;
	private String sezione;
	private String collocazione;
	private String keyLoc; //non stampare
	private String ordLoc;
	private String ordSpec;
	private String consistenzaColl;
	private String specificazione;
	private String sequenza;
	private String bidInv;
	private String bidDescr;
	private String bidDescrSup;
	private String bidKyCles2;
	private String serie;
	private String inventario;
	private String precisazione;
	private String statoConservazione;
	private String descrStatoConservazione;
	private String fruibilita;
	private String descrFruibilita;
	private String motivoNoDisp;
	private String descrMotivoNoDisp;
	private String copiaDigitale;
	private String tipoDigitalizzazione;
	private String tipoMateriale;
	private String descrTipoMateriale;
	private String barcodeInv;
	private String vid;
	private String descrVid;

	private String totInventari;
	// Attributes

	protected String motivoPrelievo;

	protected String dataPrelievo;

	public StampaStrumentiPossedutoDettaglioVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}

	public StampaStrumentiPossedutoDettaglioVO(String codBibSez, String sezione, String collocazione, String keyLoc, String ordLoc, String ordSpec,
				String consistenzaColl, String specificazione, String sequenza, String bidInv, String bidDescr, String bidDescrSup, String bidKyCles2,String serie, String inventario,
				String precisazione, String statoConservazione, String descrStatoConservazione, String fruibilita, String descrFruibilita, String motivoNoDisp,
				String descrMotivoNoDisp,String copiaDigitale, String tipoDigitalizzazione,String tipoMateriale, String descrTipoMateriale,
				String barcodeInv, String vid, String descrVid,
				String colloc, String totInventari, ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
		this.codBibSez = codBibSez;
		this.sezione = sezione;
		this.collocazione = collocazione;
		this.keyLoc = keyLoc; //non stampare
		this.ordLoc = ordLoc;
		this.ordSpec = ordSpec;
		this.consistenzaColl = consistenzaColl;
		this.specificazione = specificazione;
		this.sequenza = sequenza;
		this.bidInv = bidInv;
		this.bidDescr = bidDescr;
		this.bidDescrSup = bidDescrSup;
		this.bidKyCles2 = bidKyCles2;
		this.serie = serie;
		this.inventario = inventario;
		this.precisazione = precisazione;
		this.statoConservazione = statoConservazione;
		this.descrStatoConservazione = descrStatoConservazione;
		this.fruibilita = fruibilita;
		this.descrFruibilita = descrFruibilita;
		this.motivoNoDisp = motivoNoDisp;
		this.descrMotivoNoDisp = descrMotivoNoDisp;
		this.copiaDigitale = copiaDigitale;
		this.tipoDigitalizzazione = tipoDigitalizzazione;
		this.tipoMateriale = tipoMateriale;
		this.descrTipoMateriale = descrTipoMateriale;
		this.barcodeInv = barcodeInv;
		this.vid = vid;
		this.descrVid = descrVid;

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

	public String getSpecificazione() {
		return specificazione;
	}

	public void setSpecificazione(String specificazione) {
		this.specificazione = specificazione;
	}

	public String getTotInventari() {
		return totInventari;
	}

	public void setTotInventari(String totInventari) {
		this.totInventari = totInventari;
	}

	public String getConsistenzaColl() {
		return consistenzaColl;
	}

	public void setConsistenzaColl(String consistenzaColl) {
		this.consistenzaColl = consistenzaColl;
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

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getOrdSpec() {
		return ordSpec;
	}

	public void setOrdSpec(String ordSpec) {
		this.ordSpec = ordSpec;
	}

	public String getSequenza() {
		return sequenza;
	}

	public void setSequenza(String sequenza) {
		this.sequenza = sequenza;
	}

	public String getBidDescrSup() {
		return bidDescrSup;
	}

	public void setBidDescrSup(String bidDescrSup) {
		this.bidDescrSup = bidDescrSup;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public String getPrecisazione() {
		return precisazione;
	}

	public void setPrecisazione(String precisazione) {
		this.precisazione = precisazione;
	}

	public String getDescrStatoConservazione() {
		return descrStatoConservazione;
	}

	public void setDescrStatoConservazione(String descrStatoConservazione) {
		this.descrStatoConservazione = descrStatoConservazione;
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

	public String getCopiaDigitale() {
		return copiaDigitale;
	}

	public void setCopiaDigitale(String copiaDigitale) {
		this.copiaDigitale = copiaDigitale;
	}

	public String getTipoDigitalizzazione() {
		return tipoDigitalizzazione;
	}

	public void setTipoDigitalizzazione(String tipoDigitalizzazione) {
		this.tipoDigitalizzazione = tipoDigitalizzazione;
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

	public String getDescrTipoMateriale() {
		return descrTipoMateriale;
	}

	public void setDescrTipoMateriale(String descrTipoMateriale) {
		this.descrTipoMateriale = descrTipoMateriale;
	}

	public String getBidKyCles2() {
		return bidKyCles2;
	}

	public void setBidKyCles2(String bidKyCles2) {
		this.bidKyCles2 = bidKyCles2;
	}

	public String getBidInv() {
		return bidInv;
	}

	public void setBidInv(String bidInv) {
		this.bidInv = bidInv;
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

	public String getStatoConservazione() {
		return statoConservazione;
	}

	public void setStatoConservazione(String statoConservazione) {
		this.statoConservazione = statoConservazione;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
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

}
