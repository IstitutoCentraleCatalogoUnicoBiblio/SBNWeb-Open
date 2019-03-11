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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;

import it.iccu.sbn.ejb.vo.common.RicercaBaseVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RicercaUtenteBibliotecaVO extends RicercaBaseVO<Object> {

	private static final long serialVersionUID = -6315877607853910588L;

	public static final String RICERCA_ESATTO = "int";
	public static final String RICERCA_INIZIO = "ini";
	public static final String RICERCA_PAROLE = "par";

	private int idUte;
	private int idUteUte;
	private String codPoloSer;
	private String codBibSer;
	private String codPoloUte;
	private String codBibUte;
	private String codUte;
	private String codiceAteneo;
	private String matricola;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String dataNascitaA;
	private String codFiscale;
	private String email;
	private String tipoAutorizzazione;
	private String tipoAutorizzazioneDescr = ""; // utile per il report di stampe utenti
	private String dataFineAut;
	private String dataFineAutA;
	private String professione;
	private String professioneDescr = ""; // utile per il report di stampe utenti
	private String occupazione;
	private String occupazioneDescr = ""; // utile per il report di stampe utenti
	private String nazCitta;
	private String nazDescr = ""; // utile per il report di stampe utenti
	private String titStudio;
	private String titStudioDescr = ""; // utile per il report di stampe utenti
	private String materia;
	private String materiaDescr = ""; // utile per il report di stampe utenti
	private String specificita;
	private String specificitaDescr = ""; // utile per il report di stampe utenti
	private String provResidenza;
	private String sesso;
	private String luogoNascita;
	private String tipoPersona;
	private String personaGiuridica = "";
	private String idMateria;

	// indica chi ha attivato la richiesta Utente (Utenti=U Altro=A
	// Stampe lista Utenti=S  Tesserino=T Erogazione Servizi=E)
	private String parametro = null;
	//	indica il radio button per la ricerca (int=intero par=parola ini=inizio)
	private String tipoRicerca;
	// 	indica la ricerca utente in anagrafe
	private boolean ricercaUtentePolo = false;
	private String chiave_ute;

	//almaviva5_20110512 #4446
	private Set<String> idTrovati = new HashSet<String>();

	private List<Integer> idUtentiIgnorati = new ArrayList<Integer>();

	public void clear() {
		chiave_ute = null;
		codBibSer = null;
		codBibUte = null;
		codFiscale = null;
		codPoloSer = null;
		codPoloUte = null;
		codUte = null;
		codiceAteneo = null;
		cognome = null;
		dataFineAut = null;
		dataFineAutA = null;
		dataNascita = null;
		dataNascitaA = null;
		email = null;
		idUte = 0;
		idUteUte = 0;
		luogoNascita = null;
		materia = null;
		matricola = null;
		nazCitta = null;
		nome = null;
		occupazione = null;
		parametro = null;
		professione = null;
		provResidenza = null;
		ricercaUtentePolo = false;
		sesso = null;
		specificita = null;
		tipoAutorizzazione = null;
		tipoPersona = null;
		tipoRicerca = null;
		titStudio = null;
	}

	/**
	 *
	 * @return <strong>true</strong> se sono stai impostati altri criteri di ricerca differenti da
	 * codice fiscale, codice utente, matricola; <strong>false</strong> altrimenti
	 *
	 */
	public boolean impostatiCriteriNonUnivoci() {
		return (isFilled(cognome) && tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_INIZIO))
				|| (isFilled(nome) && tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_INIZIO))
				|| isFilled(dataNascita)
				|| isFilled(email)
				|| isFilled(tipoAutorizzazione)
				|| isFilled(professione)
				|| isFilled(nazCitta)
				|| isFilled(titStudio)
				|| isFilled(provResidenza)
				|| isFilled(sesso)
				|| isFilled(luogoNascita)
				|| (isFilled(matricola) && !isFilled(codiceAteneo))
				|| (!isFilled(matricola) && isFilled(codiceAteneo))
				|| (isFilled(tipoPersona) && isFilled(personaGiuridica))
				|| (isFilled(materia) && "0".compareTo(materia) < 0);
	}

	public boolean impostatiCriteriUnivoci() {
		return impostatoCodiceFiscale()
				|| impostatoCodiceUtente()
				|| impostatoCodiceMatricola()
				|| impostatoNomeCognomeEsatto()
				|| impostataDenominazioneEsatta()
				|| impostataDenominazioneParole()
				|| impostatoNomeCognomeSenzaTipo()
				|| impostatoNominativo()
				|| impostaEmail();
	}

	public boolean impostatoCodiceFiscale() {
		return isFilled(codFiscale);
	}

	public boolean impostaEmail() {
		return isFilled(email);
	}

	public boolean impostatoCodiceUtente() {
		return isFilled(codUte);
	}

	public boolean impostatoCodiceMatricola() {
		return isFilled(matricola) && isFilled(codiceAteneo);
	}

	public boolean impostatoNomeCognomeEsatto() {
		return (isFilled(cognome) && isFilled(nome) && tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_ESATTO));
	}

	public boolean impostataDenominazioneEsatta() {
		return (isFilled(cognome) && !isFilled(nome) && tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_ESATTO));
				//&& tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_ESATTO));
				//&& tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_PAROLE)
	}
	public boolean impostatoNomeCognomeSenzaTipo() {
		return (isFilled(cognome) && isFilled(nome));
	}

	public boolean impostataDenominazioneParole() {
		return (isFilled(cognome) && !isFilled(nome)  && personaGiuridica.equals("S") && tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_PAROLE));
				//&& tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_ESATTO));
				//&& tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_PAROLE)
	}

	public boolean impostatoNominativo() {
		return (isFilled(cognome+nome));
				//&& tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_ESATTO));
				//&& tipoRicerca.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_PAROLE)
	}


	public boolean isRicercaUtentePolo() {
		return ricercaUtentePolo;
	}

	public void setRicercaUtentePolo(boolean ricercaUtentePolo) {
		this.ricercaUtentePolo = ricercaUtentePolo;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public String getCodBibSer() {
		return codBibSer;
	}

	public void setCodBibSer(String codBibSer) {
		this.codBibSer = codBibSer;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = trimOrEmpty(codFiscale).toUpperCase();
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCognomeNome() {
		String composto="";
		if (isFilled(cognome))
		{
			composto=composto + cognome;
		}
		if (isFilled(nome))
		{
			composto=composto + nome;
		}
		return composto;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getNazCitta() {
		return nazCitta;
	}

	public void setNazCitta(String nazCitta) {
		this.nazCitta = nazCitta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProfessione() {
		return professione;
	}

	public void setProfessione(String professione) {
		this.professione = professione;
	}

	public String getProvResidenza() {
		return provResidenza;
	}

	public void setProvResidenza(String provResidenza) {
		this.provResidenza = provResidenza;
	}

	public String getTipoAutorizzazione() {
		return tipoAutorizzazione;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public void setTipoAutorizzazione(String tipoAutorizzazione) {
		this.tipoAutorizzazione = tipoAutorizzazione;
	}

	public String getTitStudio() {
		return titStudio;
	}

	public void setTitStudio(String titStudio) {
		this.titStudio = titStudio;
	}

	public String getCodBibUte() {
		return codBibUte;
	}

	public void setCodBibUte(String codBibUte) {
		this.codBibUte = codBibUte;
	}

	public String getCodPoloUte() {
		return codPoloUte;
	}

	public void setCodPoloUte(String codPoloUte) {
		this.codPoloUte = codPoloUte;
	}

	public String getCodUte() {
		return codUte;
	}

	public void setCodUte(String codUte) {
		this.codUte = trimOrEmpty(codUte);
	}

	public String getCodPoloSer() {
		return codPoloSer;
	}

	public void setCodPoloSer(String codPoloSer) {
		this.codPoloSer = codPoloSer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String mail) {
		email = mail;
	}

	public String getMail() {
		return email;
	}

	public void setMail(String mail) {
		email = mail;
	}

	public int getIdUte() {
		return idUte;
	}

	public void setIdUte(int idUte) {
		this.idUte = idUte;
	}



//	public int getTotBlocchi() {
//		return totBlocchi;
//	}
//
//	public void setTotBlocchi(int totBlocchi) {
//		this.totBlocchi = totBlocchi;
//	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}


	public String getCodiceAteneo() {
		return codiceAteneo;
	}


	public void setCodiceAteneo(String codiceAteneo) {
		this.codiceAteneo = codiceAteneo;
	}


	public int getIdUteUte() {
		return idUteUte;
	}


	public void setIdUteUte(int idUteUte) {
		this.idUteUte = idUteUte;
	}


	public String getTipoPersona() {
		return tipoPersona;
	}


	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}


	public String getDataNascitaA() {
		return dataNascitaA;
	}


	public void setDataNascitaA(String dataNascitaA) {
		this.dataNascitaA = dataNascitaA;
	}


	public String getOccupazione() {
		return occupazione;
	}


	public void setOccupazione(String occupazione) {
		this.occupazione = occupazione;
	}


	public String getSpecificita() {
		return specificita;
	}


	public void setSpecificita(String specificita) {
		this.specificita = specificita;
	}


	public String getDataFineAut() {
		return dataFineAut;
	}


	public void setDataFineAut(String dataFineAut) {
		this.dataFineAut = dataFineAut;
	}


	public String getDataFineAutA() {
		return dataFineAutA;
	}


	public void setDataFineAutA(String dataFineAutA) {
		this.dataFineAutA = dataFineAutA;
	}


	public String getChiave_ute() {
		return chiave_ute;
	}


	public void setChiave_ute(String chiave_ute) {
		this.chiave_ute = trimAndSet(chiave_ute);
	}


	public String getPersonaGiuridica() {
		return personaGiuridica;
	}


	public void setPersonaGiuridica(String personaGiuridica) {
		this.personaGiuridica = personaGiuridica;
	}


	public String getNazDescr() {
		return nazDescr;
	}


	public void setNazDescr(String nazDescr) {
		this.nazDescr = nazDescr;
	}


	public String getProfessioneDescr() {
		return professioneDescr;
	}


	public void setProfessioneDescr(String professioneDescr) {
		this.professioneDescr = professioneDescr;
	}


	public String getTitStudioDescr() {
		return titStudioDescr;
	}


	public void setTitStudioDescr(String titStudioDescr) {
		this.titStudioDescr = titStudioDescr;
	}


	public String getTipoAutorizzazioneDescr() {
		return tipoAutorizzazioneDescr;
	}


	public void setTipoAutorizzazioneDescr(String tipoAutorizzazioneDescr) {
		this.tipoAutorizzazioneDescr = tipoAutorizzazioneDescr;
	}


	public String getIdMateria() {
		return idMateria;
	}


	public void setIdMateria(String idMateria) {
		this.idMateria = idMateria;
	}


	public String getMateria() {
		return materia;
	}


	public void setMateria(String materia) {
		this.materia = materia;
	}


	public String getMateriaDescr() {
		return materiaDescr;
	}


	public void setMateriaDescr(String materiaDescr) {
		this.materiaDescr = materiaDescr;
	}


	public String getOccupazioneDescr() {
		return occupazioneDescr;
	}


	public void setOccupazioneDescr(String occupazioneDescr) {
		this.occupazioneDescr = occupazioneDescr;
	}


	public String getSpecificitaDescr() {
		return specificitaDescr;
	}


	public void setSpecificitaDescr(String specificitaDescr) {
		this.specificitaDescr = specificitaDescr;
	}

	public Set<String> getIdTrovati() {
		return idTrovati;
	}

	public void setIdTrovati(Set<String> idTrovati) {
		this.idTrovati = idTrovati;
	}

	public List<Integer> getIdUtentiIgnorati() {
		return idUtentiIgnorati;
	}

	public void setIdUtentiIgnorati(List<Integer> idUtentiIgnorati) {
		this.idUtentiIgnorati = idUtentiIgnorati;
	}
}
