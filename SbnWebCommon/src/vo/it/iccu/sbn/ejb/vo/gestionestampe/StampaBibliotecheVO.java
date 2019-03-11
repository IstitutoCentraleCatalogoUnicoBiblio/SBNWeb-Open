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

public class StampaBibliotecheVO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -1195941461586190239L;
	// Attributes
	private String tipoBiblioteca;
	private String nomeBiblioteca;
	private String enteDiAppartenenza;
	private String provincia;
	private String paese;
	private String polo;
	private String indirizzo;
	private String dataInserimento;
	private String dataAggiornamento;
	private String codiceBiblioteca;
	private String unitaOrganizzativa;
	private String casellaPostale;
	private String cap;
	private String telefono;
	private String fax;
	private String note;
	private String partitaIVA;
	private String codiceFiscale;
	private String email;
	private String codCSR;
	private String codUt1;
	private String codUt2;
	private String flagBiblioteca;
	private String localita;
	private String chiaveNome;

    // Constructors
    public StampaBibliotecheVO() {
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

	public String getChiaveNome() {
		return chiaveNome;
	}

	public void setChiaveNome(String chiaveNome) {
		this.chiaveNome = chiaveNome;
	}

	public String getCodCSR() {
		return codCSR;
	}

	public void setCodCSR(String codCSR) {
		this.codCSR = codCSR;
	}

	public String getCodiceBiblioteca() {
		return codiceBiblioteca;
	}

	public void setCodiceBiblioteca(String codiceBiblioteca) {
		this.codiceBiblioteca = codiceBiblioteca;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCodUt1() {
		return codUt1;
	}

	public void setCodUt1(String codUt1) {
		this.codUt1 = codUt1;
	}

	public String getCodUt2() {
		return codUt2;
	}

	public void setCodUt2(String codUt2) {
		this.codUt2 = codUt2;
	}

	public String getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String mail) {
		email = mail;
	}

	public String getEnteDiAppartenenza() {
		return enteDiAppartenenza;
	}

	public void setEnteDiAppartenenza(String enteDiAppartenenza) {
		this.enteDiAppartenenza = enteDiAppartenenza;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFlagBiblioteca() {
		return flagBiblioteca;
	}

	public void setFlagBiblioteca(String flagBiblioteca) {
		this.flagBiblioteca = flagBiblioteca;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getNomeBiblioteca() {
		return nomeBiblioteca;
	}

	public void setNomeBiblioteca(String nomeBiblioteca) {
		this.nomeBiblioteca = nomeBiblioteca;
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

	public String getPartitaIVA() {
		return partitaIVA;
	}

	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
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

	public String getTipoBiblioteca() {
		return tipoBiblioteca;
	}

	public void setTipoBiblioteca(String tipoBiblioteca) {
		this.tipoBiblioteca = tipoBiblioteca;
	}

	public String getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}

	public void setUnitaOrganizzativa(String unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}

}

