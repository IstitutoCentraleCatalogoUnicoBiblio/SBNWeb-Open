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

public class ConfigurazioneORDVO extends SerializableVO {

	private static final long serialVersionUID = 4513578586434543522L;
	private String ticket;
	private String codPolo;
	private String codBibl; // char 3 cod_bib
	private String denoBibl;
	private boolean gestioneBilancio; // char 1 Y,N
	private boolean gestioneSezione; // char 1 Y,N
	private boolean gestioneProfilo; // char 1 Y,N
	private String codiceProfilo; // cod_prac numeric (10,0)
	private String codiceSezione; // cod_sezione CHAR 7
	private StrutturaTerna chiaveBilancio; // esercizio E capitolo NUMERC 4
											// cod_mat CHAR 1
	private StrutturaCombo[] fornitoriDefault = new StrutturaCombo[6]; // cod_fornitore
																		// INTEGER
	private String[] codiciFornitore = new String[6];
	private boolean ordiniAperti; // char 1 Y,N
	private boolean ordiniChiusi; // char 1 Y,N
	private boolean ordiniAnnullati; // char 1 Y,N
	private String allineamento; // char 1 A,R,N
	private boolean flag_canc = false;
	private Timestamp dataUpd;
	private String utente;

	private String cd_bib_google;
	private Integer cd_forn_google;

	// costruttore
	public ConfigurazioneORDVO() throws Exception {
		this.fornitoriDefault[0] = new StrutturaCombo("A", "");
		this.fornitoriDefault[1] = new StrutturaCombo("L", "");
		this.fornitoriDefault[2] = new StrutturaCombo("D", "");
		this.fornitoriDefault[3] = new StrutturaCombo("V", "");
		this.fornitoriDefault[4] = new StrutturaCombo("C", "");
		this.fornitoriDefault[5] = new StrutturaCombo("R", "");
	};

	public ConfigurazioneORDVO(String codP, String codB, boolean gestBil,
			boolean gestSez, boolean gestProf, String codProf, String codSez,
			StrutturaTerna kBil, String[] codForn, boolean ordAperti,
			boolean ordChiusi, boolean ordAnnullati, String allineam)
			throws Exception {
		this.fornitoriDefault[0] = new StrutturaCombo("A", codForn[0]);
		this.fornitoriDefault[1] = new StrutturaCombo("L", codForn[1]);
		this.fornitoriDefault[2] = new StrutturaCombo("D", codForn[2]);
		this.fornitoriDefault[3] = new StrutturaCombo("V", codForn[3]);
		this.fornitoriDefault[4] = new StrutturaCombo("C", codForn[4]);
		this.fornitoriDefault[5] = new StrutturaCombo("R", codForn[5]);
		this.codPolo = codP;
		this.codBibl = codB;
		this.gestioneBilancio = gestBil;
		this.gestioneSezione = gestSez;
		this.gestioneProfilo = gestProf;
		this.codiceProfilo = codProf;
		this.codiceSezione = codSez;
		this.chiaveBilancio = kBil;
		this.ordiniAperti = ordAperti;
		this.ordiniChiusi = ordChiusi;
		this.ordiniAnnullati = ordAnnullati;
		this.allineamento = allineam;

	}

	public ConfigurazioneORDVO(OrdiniVO o) {
		this.codPolo = o.getCodPolo();
		this.codBibl = o.getCodBibl();
		this.ticket = o.getTicket();
	}

	public StrutturaTerna getChiaveBilancio() {
		return chiaveBilancio;
	}

	public void setChiaveBilancio(StrutturaTerna chiaveBilancio) {
		this.chiaveBilancio = chiaveBilancio;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodiceProfilo() {
		return codiceProfilo;
	}

	public void setCodiceProfilo(String codiceProfilo) {
		this.codiceProfilo = codiceProfilo;
	}

	public String getCodiceSezione() {
		return codiceSezione;
	}

	public void setCodiceSezione(String codiceSezione) {
		this.codiceSezione = codiceSezione;
	}

	public String[] getCodiciFornitore() {
		return codiciFornitore;
	}

	public void setCodiciFornitore(String[] codiciFornitore) {
		this.codiciFornitore = codiciFornitore;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public StrutturaCombo[] getFornitoriDefault() {
		return fornitoriDefault;
	}

	public void setFornitoriDefault(StrutturaCombo[] fornitoriDefault) {
		this.fornitoriDefault = fornitoriDefault;
	}

	public boolean isGestioneBilancio() {
		return gestioneBilancio;
	}

	public void setGestioneBilancio(boolean gestioneBilancio) {
		this.gestioneBilancio = gestioneBilancio;
	}

	public boolean isGestioneProfilo() {
		return gestioneProfilo;
	}

	public void setGestioneProfilo(boolean gestioneProfilo) {
		this.gestioneProfilo = gestioneProfilo;
	}

	public boolean isGestioneSezione() {
		return gestioneSezione;
	}

	public void setGestioneSezione(boolean gestioneSezione) {
		this.gestioneSezione = gestioneSezione;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getAllineamento() {
		return allineamento;
	}

	public void setAllineamento(String allineamento) {
		this.allineamento = allineamento;
	}

	public boolean isOrdiniAnnullati() {
		return ordiniAnnullati;
	}

	public void setOrdiniAnnullati(boolean ordiniAnnullati) {
		this.ordiniAnnullati = ordiniAnnullati;
	}

	public boolean isOrdiniAperti() {
		return ordiniAperti;
	}

	public void setOrdiniAperti(boolean ordiniAperti) {
		this.ordiniAperti = ordiniAperti;
	}

	public boolean isOrdiniChiusi() {
		return ordiniChiusi;
	}

	public void setOrdiniChiusi(boolean ordiniChiusi) {
		this.ordiniChiusi = ordiniChiusi;
	}

	public boolean isFlag_canc() {
		return flag_canc;
	}

	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}

	public String getDenoBibl() {
		return denoBibl;
	}

	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}

	public Timestamp getDataUpd() {
		return dataUpd;
	}

	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getCd_bib_google() {
		return cd_bib_google;
	}

	public void setCd_bib_google(String cd_bib_google) {
		this.cd_bib_google = trimAndSet(cd_bib_google);
	}

	public Integer getCd_forn_google() {
		return cd_forn_google;
	}

	public void setCd_forn_google(Integer cd_forn_google) {
		this.cd_forn_google = cd_forn_google;
	}

}
