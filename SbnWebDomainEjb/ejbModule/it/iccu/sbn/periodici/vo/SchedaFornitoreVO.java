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
package it.iccu.sbn.periodici.vo;

import java.io.Serializable;
import java.sql.Timestamp;


public class SchedaFornitoreVO implements Serializable {

	private static final long serialVersionUID = 6956396723006277909L;
	private Integer progressivo = 0;
	private String utente;
	private String codPolo;
	private String codBibl;
	private String codFornitore;
	private String nomeFornitore;
	private String unitaOrg;
	private String indirizzo;
	private String indirizzoComposto;
	private String casellaPostale;
	private String citta;
	private String cap;
	private String telefono;
	private String fax;
	private String note;
	private String partitaIva;
	private String codiceFiscale;
	private String email;
	private String paese;
	private String tipoPartner;
	private String provincia;
	private String tipoVariazione;
	private String bibliotecaFornitore; // codice della biblioteca importata fra
										// i fornitori
	private String bibliotecaFornitoreCodPolo; // codice polo della biblioteca
												// importata fra i fornitori
	private boolean flag_canc = false;
	private double importoFornitoreStampa;
	private Timestamp dataUpd;
	private String chiaveFor;

	public SchedaFornitoreVO() {
	};

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

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCasellaPostale() {
		return casellaPostale;
	}

	public void setCasellaPostale(String casellaPostale) {
		this.casellaPostale = casellaPostale;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNomeFornitore() {
		return nomeFornitore;
	}

	public void setNomeFornitore(String nomeFornitore) {
		this.nomeFornitore = nomeFornitore;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoPartner() {
		return tipoPartner;
	}

	public void setTipoPartner(String tipoPartner) {
		this.tipoPartner = tipoPartner;
	}

	public String getUnitaOrg() {
		return unitaOrg;
	}

	public void setUnitaOrg(String unitaOrg) {
		this.unitaOrg = unitaOrg;
	}

	public String getTipoVariazione() {
		return tipoVariazione;
	}

	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}

	public String getBibliotecaFornitore() {
		return bibliotecaFornitore;
	}

	public void setBibliotecaFornitore(String bibliotecaFornitore) {
		this.bibliotecaFornitore = bibliotecaFornitore;
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

	public String getIndirizzoComposto() {
		return indirizzoComposto;
	}

	public void setIndirizzoComposto(String indirizzoComposto) {
		this.indirizzoComposto = indirizzoComposto;
	}

	public double getImportoFornitoreStampa() {
		return importoFornitoreStampa;
	}

	public void setImportoFornitoreStampa(double importoFornitoreStampa) {
		this.importoFornitoreStampa = importoFornitoreStampa;
	}

	public String getBibliotecaFornitoreCodPolo() {
		return bibliotecaFornitoreCodPolo;
	}

	public void setBibliotecaFornitoreCodPolo(String bibliotecaFornitoreCodPolo) {
		this.bibliotecaFornitoreCodPolo = bibliotecaFornitoreCodPolo;
	}

	public Timestamp getDataUpd() {
		return dataUpd;
	}

	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}

	public String getChiaveFor() {
		return chiaveFor;
	}

	public void setChiaveFor(String chiaveFor) {
		this.chiaveFor = chiaveFor;
	}

}
