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

import java.sql.Timestamp;
import java.util.List;

public class BuoniOrdineVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 7042482317707366879L;
	private String ticket;
	private Integer progressivo=0;
	private String utente;
	private String codPolo;
	private int IDBuonoOrd;
	private int IDBil;
	private String codBibl;
	private String denoBibl;
	private String numBuonoOrdine;
	private String dataBuonoOrdine;
	private String statoBuonoOrdine;
	private StrutturaCombo fornitore;
	private FornitoreVO anagFornitore;
	private StrutturaTerna bilancio;
	private double importo;
	private String importoStr;
	private List<OrdiniVO> listaOrdiniBuono;
	private String tipoVariazione;
	private boolean flag_canc=false;
	private ConfigurazioneBOVO configurazione;
	private Timestamp dataUpd;
	private boolean salvaEffettuato=false; // x evitare il controllo di validazione sullo stato= stampato
	private boolean  gestBil=true;


	//costruttore
	public BuoniOrdineVO ()
		{
		//this.fornitore = new StrutturaCombo("","");
		//this.bilancio = new StrutturaTerna("","","");
		}
	;
	public BuoniOrdineVO (String codP, String codB, String num, String data,  String stato, StrutturaCombo forn, StrutturaTerna bil,  double imp, String tipoVar)
	throws Exception {


		this.codPolo = codP;
		this.codBibl = codB;
		this.numBuonoOrdine = num;
		this.dataBuonoOrdine = data;
		this.statoBuonoOrdine = stato;
		this.fornitore = forn;
		this.bilancio = bil;
		this.importo = imp;
		this.tipoVariazione=tipoVar;

}
	public String getChiave() {
		String chiave=getCodBibl()+ "|" + getNumBuonoOrdine();
		chiave=chiave.trim();
		return chiave;
	}
	public String getChiaveBilancio() {
		String chiaveBil=getBilancio().getCodice1()+ "|" + getBilancio().getCodice2()+ "|" +  getBilancio().getCodice3();
		return chiaveBil;
	}
	public String getChiaveBil() {
		String chiaveBil=getBilancio().getCodice1()+ "|" + getBilancio().getCodice2();
		return chiaveBil;
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
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getDataBuonoOrdine() {
		return dataBuonoOrdine;
	}
	public void setDataBuonoOrdine(String dataBuonoOrdine) {
		this.dataBuonoOrdine = dataBuonoOrdine;
	}
	public StrutturaCombo getFornitore() {
		return fornitore;
	}
	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}
	public List<OrdiniVO> getListaOrdiniBuono() {
		return listaOrdiniBuono;
	}
	public void setListaOrdiniBuono(List<OrdiniVO> listaOrdiniBuono) {
		this.listaOrdiniBuono = listaOrdiniBuono;
	}
	public String getNumBuonoOrdine() {
		return numBuonoOrdine;
	}
	public void setNumBuonoOrdine(String numBuonoOrdine) {
		this.numBuonoOrdine = trimAndSet(numBuonoOrdine);
	}
	public String getStatoBuonoOrdine() {
		return statoBuonoOrdine;
	}
	public void setStatoBuonoOrdine(String statoBuonoOrdine) {
		this.statoBuonoOrdine = statoBuonoOrdine;
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
	public int getIDBil() {
		return IDBil;
	}
	public void setIDBil(int bil) {
		IDBil = bil;
	}
	public int getIDBuonoOrd() {
		return IDBuonoOrd;
	}
	public void setIDBuonoOrd(int buonoOrd) {
		IDBuonoOrd = buonoOrd;
	}
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public String getImportoStr() {
		return importoStr;
	}
	public void setImportoStr(String importoStr) {
		this.importoStr = importoStr;
	}
	public FornitoreVO getAnagFornitore() {
		return anagFornitore;
	}
	public void setAnagFornitore(FornitoreVO anagFornitore) {
		this.anagFornitore = anagFornitore;
	}
	public String getDenoBibl() {
		return denoBibl;
	}
	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}
	public ConfigurazioneBOVO getConfigurazione() {
		return configurazione;
	}
	public void setConfigurazione(ConfigurazioneBOVO configurazione) {
		this.configurazione = configurazione;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}
	public boolean isSalvaEffettuato() {
		return salvaEffettuato;
	}
	public void setSalvaEffettuato(boolean salvaEffettuato) {
		this.salvaEffettuato = salvaEffettuato;
	}
	public boolean isGestBil() {
		return gestBil;
	}
	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}


}
