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
package it.iccu.sbn.ejb.vo.servizi.utenti;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SinteticaUtenteVO extends BaseVO {

	private static final long serialVersionUID = -4435107121236926952L;
	private int progressivo;
	private String poloErogante;
	private String bibErogante;
	private int idUtente;
	private String polo;
	private String biblioteca;
	private String codice;
	private String biblioDiUteBiblio;
	private String poloDiUteBiblio;
	private String descrizione;
	private String indirizzo;
	private String provincia;
	private String cap;
	private String citta;
	private String cittaRes="";
	private String tipo;
	private String telefono;
	private String ateneo;
	private String matricola;
	private String luogoNascita;
	private String dataNascita;
	private Date dataNascitaDate;  // necessario all'ordinamento del report
	private String email;
	private int idAutorizzazione;
	private String codiceAutorizzazione;
	private String descrizioneAutorizzazione;
	private String scadenzaAutorizzazione;
	private Date scadenzaAutorizzazioneDate;  // necessario all'ordinamento del report
	private String flgAutorizzazione;
	private RicercaUtenteBibliotecaVO criteriRicerca; // per la stampa

	private int idUtenteAna;



	public SinteticaUtenteVO(String polo, String bib, String cod, String des,
			String indi, String prov, String paese, String tipo, String aff,
			String ateneo, String luogoNascita, String dataNascita,
			String email, int idAutorizzazione, String codiceAutorizzazione,
			String descrizioneAutorizzazione, String scadenzaAutorizzazione)
			throws Exception {
		if (polo == null) {
			throw new Exception("Codice polo non valido");
		}
		if (bib == null) {
			throw new Exception("Codice biblioteca non valido");
		}
		if (cod == null) {
			throw new Exception("Codice utente non valido");
		}
		if (des == null) {
			throw new Exception("Descrizione non valida");
		}
		this.polo = polo;
		this.biblioteca = bib;
		this.codice = cod;
		this.descrizione = des;
		this.indirizzo = indi;
		this.provincia = prov;
		this.citta = prov;
		this.tipo = tipo;
		this.telefono = aff;
		this.ateneo = ateneo;
		this.luogoNascita = luogoNascita;
		this.dataNascita = dataNascita;
		this.email = email;
		this.codiceAutorizzazione = codiceAutorizzazione;
		this.descrizioneAutorizzazione = descrizioneAutorizzazione;
		this.scadenzaAutorizzazione = scadenzaAutorizzazione;
	}

	public void clearSintUte() {
		this.polo = "";
		this.biblioteca = "";
		this.codice = "";
		this.descrizione = "";
		this.indirizzo = "";
		this.provincia = "";
		this.citta = "";
		this.cap="";
		this.cittaRes = "";
		this.tipo = "";
		this.telefono = "";
		this.ateneo = "";
		this.matricola="";
		this.luogoNascita = "";
		this.dataNascita = "";
		this.email = "";
		this.codiceAutorizzazione = "";
		this.descrizioneAutorizzazione = "";
		this.scadenzaAutorizzazione = "";
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = trimAndSet(codice);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = trimAndSet(indirizzo);
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = trimAndSet(provincia);
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = trimAndSet(citta);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String aff) {
		this.telefono = aff;
	}

	public SinteticaUtenteVO() {
		super();
	}

	public String getBibErogante() {
		return bibErogante;
	}

	public void setBibErogante(String bibErogante) {
		this.bibErogante = bibErogante;
	}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public String getPoloErogante() {
		return poloErogante;
	}

	public void setPoloErogante(String poloErogante) {
		this.poloErogante = poloErogante;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public String getBiblioDiUteBiblio() {
		return biblioDiUteBiblio;
	}

	public void setBiblioDiUteBiblio(String biblioDiUteBiblio) {
		this.biblioDiUteBiblio = biblioDiUteBiblio;
	}

	public String getPoloDiUteBiblio() {
		return poloDiUteBiblio;
	}

	public void setPoloDiUteBiblio(String poloDiUteBiblio) {
		this.poloDiUteBiblio = poloDiUteBiblio;
	}

	public String getAteneo() {
		return ateneo;
	}

	public void setAteneo(String ateneo) {
		this.ateneo = ateneo;
	}

	public String getCodiceAutorizzazione() {
		return codiceAutorizzazione;
	}

	public void setCodiceAutorizzazione(String codAutor) {
		codiceAutorizzazione = codAutor;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
		this.dataNascitaDate = stringToDate(dataNascita);

	}

	public String getDescrizioneAutorizzazione() {
		return descrizioneAutorizzazione;
	}

	public void setDescrizioneAutorizzazione(String descrizioneAutorizzazione) {
		this.descrizioneAutorizzazione = descrizioneAutorizzazione;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String mail) {
		email = mail;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getScadenzaAutorizzazione() {
		return scadenzaAutorizzazione;
	}

	public void setScadenzaAutorizzazione(String scadenzaAutorizzazione) {
		this.scadenzaAutorizzazione = scadenzaAutorizzazione;
		this.scadenzaAutorizzazioneDate = stringToDate(scadenzaAutorizzazione);
	}

	public int getIdAutorizzazione() {
		return idAutorizzazione;
	}

	public void setIdAutorizzazione(int idAutorizzazione) {
		this.idAutorizzazione = idAutorizzazione;
	}

	public String getFlgAutorizzazione() {
		return flgAutorizzazione;
	}

	public void setFlgAutorizzazione(String flgAutorizzazione) {
		this.flgAutorizzazione = flgAutorizzazione;
	}

	public String getCittaRes() {
		return cittaRes;
	}

	public void setCittaRes(String cittaRes) {
		this.cittaRes = cittaRes;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public RicercaUtenteBibliotecaVO getCriteriRicerca() {
		return criteriRicerca;
	}

	public void setCriteriRicerca(RicercaUtenteBibliotecaVO criteriRicerca) {
		this.criteriRicerca = criteriRicerca;
	}

	private static final java.util.Date stringToDate(java.lang.String data) {
		if (data == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date value;
		try {
			value = format.parse(data);
		} catch (ParseException e) {
			return null;
		}

		return value;
	}

	public Date getDataNascitaDate() {
		return dataNascitaDate;
	}

	public void setDataNascitaDate(Date dataNascitaDate) {
		this.dataNascitaDate = dataNascitaDate;
	}

	public Date getScadenzaAutorizzazioneDate() {
		return scadenzaAutorizzazioneDate;
	}

	public void setScadenzaAutorizzazioneDate(Date scadenzaAutorizzazioneDate) {
		this.scadenzaAutorizzazioneDate = scadenzaAutorizzazioneDate;
	}

	public int getIdUtenteAna() {
		return idUtenteAna;
	}

	public void setIdUtenteAna(int idUtenteAna) {
		this.idUtenteAna = idUtenteAna;
	}

}
