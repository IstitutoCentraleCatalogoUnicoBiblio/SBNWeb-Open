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

import java.io.Serializable;


public class StampaFornitoriVO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1110297991846489901L;
	// Attributes
	private String codiceFornitore;
	private String nomeFornitore;
	private String tipoFornitore;
	private String paese;
	private String provincia;
	private String profAcquisti;
	private String unitaOrganizzativa;
	private String polo;
	private String biblioteca;
	private String indirizzo;
	private String cap;
	private String citta;
	private String telefono;
	private String fax;
	private String ricercaLocale;
	private String partitaIVA;
	private String codiceFiscale;
	private String tipoPartner;

    // Constructors
    public StampaFornitoriVO() {
    }


	public String getCodiceFornitore() {
		return codiceFornitore;
	}


	public void setCodiceFornitore(String codiceFornitore) {
		this.codiceFornitore = codiceFornitore;
	}


	public String getNomeFornitore() {
		return nomeFornitore;
	}


	public void setNomeFornitore(String nomeFornitore) {
		this.nomeFornitore = nomeFornitore;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getProfAcquisti() {
		return profAcquisti;
	}

	public void setProfAcquisti(String profAcquisti) {
		this.profAcquisti = profAcquisti;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTipoFornitore() {
		return tipoFornitore;
	}

	public void setTipoFornitore(String tipoFornitore) {
		this.tipoFornitore = tipoFornitore;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
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

	public String getPartitaIVA() {
		return partitaIVA;
	}

	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
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

	public String getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}

	public void setUnitaOrganizzativa(String unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}


	public String getRicercaLocale() {
		return ricercaLocale;
	}


	public void setRicercaLocale(String ricercaLocale) {
		this.ricercaLocale = ricercaLocale;
	}


	public String getBiblioteca() {
		return biblioteca;
	}


	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}


	public String getPolo() {
		return polo;
	}


	public void setPolo(String polo) {
		this.polo = polo;
	}

}

