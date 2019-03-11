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

public class CartellinoVO {

	private String tessera="";
	private String nomeCognome="";
	private String biblioteca="";
	private String dataIscrizione="";
	private String professione="";
	private String occupazione="";
	private String titoloStudio="";
	private String specStudio="";

	private String indirizzoResidenza="";
	private String cittaResidenza="";
	private String capResidenza="";
	private String provinciaResidenza="";
	private String nazioneResidenza="";
	private String telefonoResidenza="";

	private String indirizzoDomicilio="";
	private String cittaDomicilio="";
	private String capDomicilio="";
	private String provinciaDomicilio="";
	private String telefonoDomicilio=""; // cellulare

	private String luogoNascita="";
	private String dataNascita="";

	private String tipoDocumento="";
	private String numeroDocumento="";
	private String docRilasciatoDa="";

	private String tipoAutorizzazione="";
	private String dataAutorizzazione="";
	private String scadenzaAutorizzazione="";

	private String email="";
	private String ateneo="";
	private String matricola="";


	public String getBiblioteca() {
		return biblioteca;
	}
	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}
	public String getCapDomicilio() {
		return capDomicilio;
	}
	public void setCapDomicilio(String capDomicilio) {
		this.capDomicilio = capDomicilio;
	}
	public String getCapResidenza() {
		return capResidenza;
	}
	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}
	public String getCittaDomicilio() {
		return cittaDomicilio;
	}
	public void setCittaDomicilio(String cittaDomicilio) {
		this.cittaDomicilio = cittaDomicilio;
	}
	public String getCittaResidenza() {
		return cittaResidenza;
	}
	public void setCittaResidenza(String cittaResidenza) {
		this.cittaResidenza = cittaResidenza;
	}
	public String getDataAutorizzazione() {
		return dataAutorizzazione;
	}
	public void setDataAutorizzazione(String dataAutorizzazione) {
		this.dataAutorizzazione = dataAutorizzazione;
	}
	public String getDataIscrizione() {
		return dataIscrizione;
	}
	public void setDataIscrizione(String dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getDocRilasciatoDa() {
		return docRilasciatoDa;
	}
	public void setDocRilasciatoDa(String docRilasciatoDa) {
		this.docRilasciatoDa = docRilasciatoDa;
	}
	public String getIndirizzoDomicilio() {
		return indirizzoDomicilio;
	}
	public void setIndirizzoDomicilio(String indirizzoDomicilio) {
		this.indirizzoDomicilio = indirizzoDomicilio;
	}
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public String getLuogoNascita() {
		return luogoNascita;
	}
	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getNazioneResidenza() {
		return nazioneResidenza;
	}
	public void setNazioneResidenza(String nazioneResidenza) {
		this.nazioneResidenza = nazioneResidenza;
	}
	public String getNomeCognome() {
		return nomeCognome;
	}
	public void setNomeCognome(String nomeCognome) {
		this.nomeCognome = nomeCognome;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getTessera() {
		return tessera;
	}
	public void setTessera(String tessera) {
		this.tessera = tessera;
	}
	public String getOccupazione() {
		return occupazione;
	}
	public void setOccupazione(String occupazione) {
		this.occupazione = occupazione;
	}
	public String getProfessione() {
		return professione;
	}
	public void setProfessione(String professione) {
		this.professione = professione;
	}
	public String getProvinciaDomicilio() {
		return provinciaDomicilio;
	}
	public void setProvinciaDomicilio(String provinciaDomicilio) {
		this.provinciaDomicilio = provinciaDomicilio;
	}
	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}
	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}
	public String getScadenzaAutorizzazione() {
		return scadenzaAutorizzazione;
	}
	public void setScadenzaAutorizzazione(String scadenzaAutorizzazione) {
		this.scadenzaAutorizzazione = scadenzaAutorizzazione;
	}
	public String getSpecStudio() {
		return specStudio;
	}
	public void setSpecStudio(String specStudio) {
		this.specStudio = specStudio;
	}
	public String getTelefonoDomicilio() {
		return telefonoDomicilio;
	}
	public void setTelefonoDomicilio(String telefonoDomicilio) {
		this.telefonoDomicilio = telefonoDomicilio;
	}
	public String getTelefonoResidenza() {
		return telefonoResidenza;
	}
	public void setTelefonoResidenza(String telefonoResidenza) {
		this.telefonoResidenza = telefonoResidenza;
	}
	public String getTipoAutorizzazione() {
		return tipoAutorizzazione;
	}
	public void setTipoAutorizzazione(String tipoAutorizzazione) {
		this.tipoAutorizzazione = tipoAutorizzazione;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getTitoloStudio() {
		return titoloStudio;
	}
	public void setTitoloStudio(String titoloStudio) {
		this.titoloStudio = titoloStudio;
	}
	public String getAteneo() {
		return ateneo;
	}
	public void setAteneo(String ateneo) {
		this.ateneo = ateneo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMatricola() {
		return matricola;
	}
	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}



}
