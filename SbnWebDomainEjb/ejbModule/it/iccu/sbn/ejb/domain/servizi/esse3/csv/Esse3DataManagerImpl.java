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
package it.iccu.sbn.ejb.domain.servizi.esse3.csv;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.vo.xml.binding.esse3.PERSONA;
import it.iccu.sbn.web.keygenerator.GeneraChiave;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

public class Esse3DataManagerImpl implements Esse3DataManager {
	
	private Logger log = Logger.getLogger(Esse3DataManager.class);

	private Esse3OperationType operationToDo;
	private String ticket;

	private List<String> utentiInseriti = new ArrayList<String>();
	private List<String> utentiAggiornati = new ArrayList<String>();
	private List<String> errors = new ArrayList<String>();
	private List<String> utentiScartati = new ArrayList<String>();

	private UserTransaction tx;

	public Esse3DataManagerImpl(Esse3OperationType op, String ticket) {
		this.operationToDo = op;
		this.ticket = ticket;
	}
	/**
	 * Genera la chiave di ricerca per il Databse tabella tbl_utenti<br>
	 * @param cognomeNome
	 * @exception Exception
	 **/
	private String generaChiaveRicerca(String cognomeNome_key) throws Exception {
		GeneraChiave generatore = new GeneraChiave();
		generatore.estraiChiavi("", cognomeNome_key);
		return generatore.getKy_cles1_A();
	}

	/**
	 * Legge il CSV di Esse3<br>
	 * Converte il csv salvato in precedenza e e inserisce nel DB
	 *
	 * @param String
	 *            cd_polo
	 * @param String
	 *            cd_biblioteca
	 * @param Strng
	 *            path_csv
	 * @see Esse3DataCsvReader
	 * @version 1.0
	 * @since 18/07/2018
	 */
	private List<UtenteBibliotecaVO> readCSV(String cd_polo, String cd_biblioteca, String path_csv) {
		//Lettura e decodifica del CSV
		Esse3DataCsvReaderImpl bcl = new Esse3DataCsvReaderImpl(cd_polo, cd_biblioteca, path_csv);
		List<UtenteBibliotecaVO> utentiBibliotecaVO = bcl.getUtentiBibliotecaVO();
		if (utentiBibliotecaVO.isEmpty()) {
			errors.add("Nulla è stato importato");
		}
		// Ottengo gli errori della conversione csv esse3
		errors = bcl.getErrors();
		return utentiBibliotecaVO;
	}

	private Servizi getServizi() {
		Servizi servizi = null;
		try {
			servizi = DomainEJBFactory.getInstance().getServizi();
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
		return servizi;
	}

	// inserice il bean del DB
	private boolean insertDB(Servizi servizi, UtenteBibliotecaVO utenteToIns, String cd_polo, String cd_biblioteca) {
		try {
			DaoManager.begin(tx);
			
			Timestamp now = DaoManager.now();
			String firmaUtenteIns = DaoManager.getFirmaUtente(this.ticket);
			utenteToIns.setUteIns(firmaUtenteIns);
			utenteToIns.setUteVar(firmaUtenteIns);
			utenteToIns.setTsIns(now);
			utenteToIns.setTsVar(now);
			utenteToIns.setLastAccess(now);
			utenteToIns.setChangePassword("N");
			utenteToIns.setFlCanc("N");
			utenteToIns.setImportato(true);
			utenteToIns.setCodBibSer(utenteToIns.getCodiceBiblioteca());
			String cognomeNome_key = utenteToIns.getCognomeNome();
			String chiaveUte = generaChiaveRicerca(cognomeNome_key);
			utenteToIns.setChiaveUte(chiaveUte);
			AutorizzazioniVO autorizzazioni = calcolaAutorizzazioni( utenteToIns.getAutorizzazioni().getCodTipoAutor(), cd_polo, cd_biblioteca);

			if(autorizzazioni != null)
				utenteToIns.setAutorizzazioni(autorizzazioni);

			servizi.insertUtente(ticket, utenteToIns);

			if (operationToDo == Esse3OperationType.UPDATE_FROM_CSV || operationToDo == Esse3OperationType.UPDATE_FROM_MODEL)
				utentiInseriti.add(utenteToIns.getCodiceUtente());

			return true;
			
		} catch (Exception e) {
			log.error("Errore " + utenteToIns.getCodiceUtente() + " inserimento: ");
			log.error(utenteToIns, e);
			String errorMessage = " --> Errore utente: " + utenteToIns.getCodiceUtente() + "\n";
			errorMessage += ("Utente: " + utenteToIns.toString() + "\n");
			errorMessage += (e.getMessage() + "\n -----------------");
			errors.add(errorMessage);
			utentiScartati.add("Inserimento id: " + utenteToIns.getCodiceUtente() + " cod_fiscale: " + utenteToIns.getAnagrafe().getCodFiscale());
			DaoManager.rollback(tx);
			return false;
		}
	}

	private AutorizzazioniVO calcolaAutorizzazioni(String autorizzazione_utente, String cd_polo, String cd_biblioteca) {
		AutorizzazioniVO autorizzazioni = null;
		try {
			if (ValidazioneDati.isFilled(autorizzazione_utente)) {
				AutorizzazioneVO tipoAutorizzazione = getServizi().getTipoAutorizzazione(ticket, cd_polo, cd_biblioteca,
						autorizzazione_utente);
				if (tipoAutorizzazione != null) {
					autorizzazioni = new AutorizzazioniVO(tipoAutorizzazione);
				}

			}
		} catch (RemoteException e) {
			errors.add("Errore in servizi, impossibile connettersi ai servizi ticket: " + ticket);
			log.error("", e);
		}
		return autorizzazioni;
	}
	private boolean updateDB(Servizi servizi, UtenteBibliotecaVO utenteToUpdate, String cd_polo, String cd_biblioteca) {
		try {
			DaoManager.begin(tx);
			
			//Verifico se il codice utente è presente nella base dati.
			UtenteBaseVO utenteVO = servizi.getUtente(ticket, utenteToUpdate.getCodiceUtente());
			UtenteBibliotecaVO utente = null;
			if (utenteVO == null) {
				//non trovato, lancio eccezione per inserirlo
				throw new ResourceNotFoundException("utente: " + utenteToUpdate.getCodiceUtente() + " non trovato");

			} else {
				//Preparo un oggetto di ricerca e lo cerco
				RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
				ricerca.setCodPolo(utenteToUpdate.getCodPolo());
				ricerca.setCodUte(utenteToUpdate.getCodiceUtente());

				
				ricerca.setCodBib(utenteVO.getCodBib());
				
				 utente = servizi.getDettaglioUtente(ticket, ricerca, null, null);
				 if(utente == null) {
					//Errore da inserire
					throw new ResourceNotFoundException("utente: " + utenteToUpdate.getCodiceUtente() + " non trovato");
				 }
			}

			//trovato
			utente.setCognome(utenteToUpdate.getCognome());
			utente.setNome(utenteToUpdate.getNome());
			utente.setAnagrafe(utenteToUpdate.getAnagrafe());
			utente.setProfessione(utenteToUpdate.getProfessione());
			utente.setAutorizzazioni(utenteToUpdate.getAutorizzazioni());
			utente.setBibliopolo(utenteToUpdate.getBibliopolo());

			Timestamp now = DaoManager.now();
			utente.setTsVar(now);

			String cognomeNome_key = utente.getCognomeNome();
			String chiaveUte = generaChiaveRicerca(cognomeNome_key);
			utente.setChiaveUte(chiaveUte);
			if(!cd_biblioteca.equals(utente.getCodBibSer())) {
				// fix iscrizione multipla esse3
				throw new ApplicationException("Utente iscritto ad altra biblioteca ESSE3");
				//utente.setCodBibSer(cd_biblioteca);
				//servizi.importaUtente(ticket, utente);
			} else {
				servizi.updateUtente(ticket, utente);

			}
			return true;
			
		} catch (ResourceNotFoundException e) {
			DaoManager.rollback(tx);
			insertDB(servizi, utenteToUpdate, cd_polo, cd_biblioteca);
			return false;
			
		}catch (Exception e) {
			log.error("Errore aggiornamento: " + utenteToUpdate.getCodiceUtente() + ": ");
			log.error(utenteToUpdate, e);
			String errorMessage = " --> Errore utente: " + utenteToUpdate.getCodiceUtente() + "\n";
			errorMessage += ("Utente: " + utenteToUpdate.toString() + "\n");
			errorMessage += (e.getMessage() + "\n -----------------");
			errors.add(errorMessage);
			utentiScartati.add("Aggiornamento id: " + utenteToUpdate.getCodiceUtente() + " cod_fiscale: " + utenteToUpdate.getAnagrafe().getCodFiscale());
			DaoManager.rollback(tx);
			return false;
		}
	}

	/**
	 * Import dati da Esse3 ad SbnWeb<br>
	 * Converte il csv salvato in precedenza ed inserisce nel DB
	 *
	 * @param String
	 *            cd_polo
	 * @param String
	 *            cd_biblioteca
	 * @param Strng
	 *            path_csv
	 * @version 1.0
	 * @since 18/07/2018
	 */
	private boolean importData(String cd_polo, String cd_biblioteca, String path_csv) {
		Servizi servizi = getServizi();
		Boolean isImported = false;
		if (servizi == null || !ValidazioneDati.isFilled(ticket)) {
			errors.add("Errore in servizi, impossibile connettersi ai servizi ticket: " + ticket);
			return false;
		}
		//Arriva a null, conversione in VO
		List<UtenteBibliotecaVO> utenti = readCSV(cd_polo, cd_biblioteca, path_csv);
		for (UtenteBibliotecaVO utenteToIns : utenti) {

			boolean isInserted = insertDB(servizi, utenteToIns, cd_polo, cd_biblioteca);
			if (isInserted) {
				utentiInseriti.add(utenteToIns.getCodiceUtente());
			}
		}
		return utentiInseriti.size() > 0;
	}

	/**
	 * Update dei dati da Esse3 ad SbnWeb<br>
	 *
	 * @param String
	 *            cd_polo
	 * @param String
	 *            cd_biblioteca
	 * @param String
	 *            data
	 *
	 * @version 1.0
	 * @since 21/09/2018
	 */
	private boolean updateData(String cd_polo, String cd_biblioteca, String path_csv) {
		Servizi servizi = getServizi();

		if (servizi == null || !ValidazioneDati.isFilled(ticket)) {
			errors.add("Errore in servizi, impossibile connettersi ai servizi ticket: " + ticket);
			return false;
		}
		List<UtenteBibliotecaVO> utentiBibliotecaVO = readCSV(cd_polo, cd_biblioteca, path_csv);
		//errors = bcl.getErrors();
		for (UtenteBibliotecaVO utente: utentiBibliotecaVO) {
			try {
				DaoManager.begin(tx);				
				boolean isUpdated = updateDB(servizi, utente, cd_polo, cd_biblioteca);
				if(isUpdated) {
					utentiAggiornati.add(utente.getCodiceUtente());
				}
				DaoManager.commit(tx);
				
			} catch (Exception e) {
				DaoManager.rollback(tx);
			}
		}

		return (utentiAggiornati.size() > 0 || utentiInseriti.size() > 0);
	}
	public boolean updatePersone(String cd_polo, String cd_biblioteca, List<PERSONA> persone) {
		final Servizi servizi = getServizi();
		final Esse3DataPersonaReader bcl = new Esse3DataPersonaReader(cd_polo, cd_biblioteca, persone);
		final List<UtenteBibliotecaVO> utentiBibliotecaVO = bcl.getUtentiBibliotecaVO();
		errors = bcl.getErrors();
		for (UtenteBibliotecaVO utente : utentiBibliotecaVO) {
			boolean isUpdated = updateDB(servizi, utente, cd_polo, cd_biblioteca);
			if(isUpdated) {
				utentiAggiornati.add(utente.getCodiceUtente());
			}
		}

		return (utentiAggiornati.size() > 0 || utentiInseriti.size() > 0);

	}
	/**
	 * Manager di import dati da Esse3 ad SbnWeb<br>
	 * In base al Esse3OperationType con cui è stato dichiarato esegue determinate
	 * istruzioni
	 *
	 * @param String
	 *            cd_polo
	 * @param String
	 *            cd_biblioteca
	 * @param Object
	 *            data (ci aspettiamo il path del CSV)
	 * @return
	 * @version 1.0
	 * @param tx 
	 * @since 18/07/2018
	 */
	public boolean manage(String cd_polo, String cd_biblioteca, Object data, UserTransaction tx, Logger _log) {
		log.debug( operationToDo + " data per " + cd_polo + cd_biblioteca);
		this.tx = tx;
		if (_log != null)
			this.log = _log;
		
		switch (operationToDo) {
		case INSERT_FROM_CSV:
			return importData(cd_polo, cd_biblioteca, (String) data);
		case UPDATE_FROM_CSV:
			return updateData(cd_polo, cd_biblioteca, (String) data);
		case UPDATE_FROM_MODEL:
			return updatePersone(cd_polo, cd_biblioteca,  (List<PERSONA>) data);

		default:
			return false;
		}
	}

	public List<String> getUtentiInseriti() {
		return utentiInseriti;
	}

	public List<String> getErrors() {
		return errors;
	}

	public List<String> getUtentiScartati() {
		return utentiScartati;
	}
	public List<String> getUtentiAggiornati() {
		return utentiAggiornati;
	}
}
