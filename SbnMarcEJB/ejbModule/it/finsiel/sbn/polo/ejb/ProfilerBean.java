/**
 *
 */
package it.finsiel.sbn.polo.ejb;

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.profile.ProfilerCache;
import it.finsiel.sbn.polo.factoring.profile.ValidatorContainerObject;
import it.finsiel.sbn.polo.factoring.profile.ValidatorContainerUser;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc --> * <!--
 * begin-xdoclet-definition -->
 *
 * @ejb.bean name="Profiler" description="A session bean named Profiler"
 *           display-name="Profiler" jndi-name="sbnMarc/Profiler"
 *           type="Stateless" transaction-type="Container"
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */

@Stateless(name="Profiler")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProfilerBean implements Profiler {

	private static Logger log = Logger.getLogger(ProfilerBean.class);

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Verifica se il soggetto richiesto è un polo <br>
	 *            Semplice: Se i primi tre caratteri sono XXX si tratta di un
	 *            utente fisico creato in fase di amministrazione, altrimenti è
	 *            un codice polo <br>
	 * @param codice
	 *            (polo/gruppo/utente)
	 * @return boolean (true=polo, false = no polo)
	 */
	public boolean isPolo(String codice) {
		// EMULO sempre un utente
		return false;

		/*
		 * boolean is_polo = false;
		 *
		 * if (codice.length() > 3) codice = codice.substring(0, 3);
		 *
		 * // Se codice XXX si tratta di un utente della struttura if
		 * (codice.equals("XXX") == true) is_polo = false; else is_polo = true;
		 *
		 * return (is_polo);
		 */
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Restituisce il codice livello di un utente per uno specifico
	 *            tipo di materiale
	 */
	public Tbf_par_mat getParametriUtentePerMateriale(String codice,
			String tipoMateriale) {
		List v = getParametriMateriale(codice);
		Tbf_par_mat par_mat;
		for (int i = 0; i < v.size(); i++) {
			par_mat = (Tbf_par_mat) v.get(i);
			if (par_mat.getCd_par_mat() == tipoMateriale.charAt(0))
				return par_mat;
		}
		return null;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Ritorna oggetto parametri utente <br>
	 *            Torna un oggetto di tipo Tb_parametro Metodo a disposizione
	 *            dei FACTORING (Sbnmarc e non) <br>
	 * @param codice
	 *            polo
	 * @return oggetto complesso con informazioni
	 */
	public Tbf_parametro getTb_parametro(String codice) {
		List elenco_parametri = null;
		Tbf_parametro elemento = null;

		ValidatorContainerObject contenitore = getAbilitazioni(codice);

		if (contenitore != null) {
			elenco_parametri = contenitore.getElenco_parametri();

			if ((elenco_parametri != null) && (elenco_parametri.size() > 0))
				elemento = (Tbf_parametro) elenco_parametri.get(0);
		}
		return (elemento);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Restituisce il codice livello di un utente per uno specifico
	 *            tipo di authority
	 */
	public Tbf_par_auth getParametriUtentePerAuthority(String codice,
			String tipoAuthority) {
		List v = getParametriAuthority(codice);
		Tbf_par_auth par_aut;
		for (int i = 0; i < v.size(); i++) {
			par_aut = (Tbf_par_auth) v.get(i);
			if (par_aut.getCd_par_auth().equals(tipoAuthority))
				return par_aut;
		}
		return null;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Ritorna oggetto con abilitazioni COMPLETE per gruppo/utente <br>
	 *            Intuisce dalla composizione del codice ricevuto se si tratta
	 *            di un utente o di un gruppo di tipo (Polo) Metodo a
	 *            disposizione dei FACTORING (Sbnmarc e non) Se i primi tre
	 *            caratteri del codice sono XXX si tratta di un utente della
	 *            struttura (utilizza I.Diretta o Forms amministrazione) Else si
	 *            tratta di un polo SBNMARC <br>
	 * @param codice
	 *            (polo/gruppo/utente)
	 * @return oggetto complesso con informazioni
	 */
	public ValidatorContainerObject getAbilitazioni(String codice) {
		ValidatorContainerObject findit = null;
		String codice_polo;

		// Se il codice non è in formato standard non perdo tempo
		//log.debug("codice da abilitare: " + codice);
		if (codice.length() >= 3) {
			codice_polo = codice.substring(0, 3);

			// Se polo allora è un gruppo
			if (isPolo(codice_polo) == true) {
				//log.debug("codice di gruppo ");
				// findit = getAbilitazioniGruppo(codice_polo);
			}
			// Se non è un polo allora è un utente
			else {
				//log.debug("codice utente ");
				findit = getAbilitazioniUtente(codice);
			}
		}

		return (findit);
	}

	public ValidatorContainerObject getAbilitazioniPolo(String cd_polo) throws EJBException {
		ValidatorContainerObject findit = null;

		// Se il codice non è in formato standard non perdo tempo
		if (cd_polo.length() != 3)
			throw new EJBException("codice polo errato");

		findit = ProfilerCache.getPoloProfile(cd_polo, false);
		return (findit);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Informa se l'attività per il gruppo/polo richiesto è abilitata
	 *            (VERSIONE CODICE ATTIVITA NUMERICO) <br>
	 *            Intuisce dalla composizione del codice ricevuto se si tratta
	 *            di un utente o di un gruppo di tipo (Polo) Metodo a
	 *            disposizione dei FACTORING (Sbnmarc e non) Se i primi tre
	 *            caratteri del codice sono XXX si tratta di un utente della
	 *            struttura (utilizza I.Diretta o Forms amministrazione) Else si
	 *            tratta di un polo SBNMARC <br>
	 * @param codice
	 *            (polo/gruppo/utente)
	 * @param codice
	 *            attivita
	 * @return trovato = true/ non trovato = false
	 */
	public boolean verificaAttivitaID(String codice, String codice_attivita)
			throws EccezioneSbnDiagnostico {
		boolean found = false;

		// Se il codice non è in formato standard non perdo tempo
		// if (codice.length() >= 6) {
		if (codice.length() >= 3) {
			// Se polo allora è un gruppo
			String codice_polo = codice.substring(0, 3);
			// String codice_biblio = codice.substring(3, 6);

			// if (isPolo(codice_polo) == true) {
			// // if
			// //
			// (getBibliotechePoloHash(codice_polo).containsKey(codice_biblio))
			// // {
			// found = verificaAttivitaPolo(codice_polo, codice_attivita);
			// // } else {
			// // throw new EccezioneSbnDiagnostico(4022,"Il codice biblioteca
			// // non identifica una biblioteca esistente nel polo");
			// // }
			// }
			// // Se non è un polo allora è un utente
			// else
			found = verificaAttivitaUtenteID(codice, codice_attivita);
		}
		return (found);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Informa se l'attività per il gruppo/polo richiesto è abilitata <br>
	 *            Intuisce dalla composizione del codice ricevuto se si tratta
	 *            di un utente o di un gruppo di tipo (Polo) Metodo a
	 *            disposizione dei FACTORING (Sbnmarc e non) Se i primi tre
	 *            caratteri del codice sono XXX si tratta di un utente della
	 *            struttura (utilizza I.Diretta o Forms amministrazione) Else si
	 *            tratta di un polo SBNMARC <br>
	 * @param codice
	 *            (polo/gruppo/utente)
	 * @return trovato = true/ non trovato = false
	 */
	public boolean verificaAttivitaDesc(String codice,
			String descrizione_attivita) {
		boolean found = false;

		// Se polo allora è un gruppo
		String codice_polo;

		// Se il codice non è in formato standard non perdo tempo
		if (codice.length() > 3)
			codice_polo = codice.substring(0, 3);
		else
			codice_polo = codice;

		// if (isPolo(codice_polo) == true)
		// found = verificaAttivitaPolo(codice_polo, descrizione_attivita);
		// // Se non è un polo allora è un utente
		// else
		found = verificaAttivitaUtenteDesc(codice, descrizione_attivita);

		return (found);
	}

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 */
	public boolean isInGruppo(String utente, String gruppo) {
		// List gruppi;
		// if (isPolo(utente)) {
		// gruppi = getElencoGruppiGruppo(utente);
		// } else {
		// gruppi = getElencoGruppiUtente(utente);
		// }
		// for (int i = 0; i < gruppi.size(); i++) {
		// if (gruppo.equals(((Tb_gruppo) gruppi.get(i)).getDS_GRUPPO())) {
		// return true;
		// }
		// }
		return false;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Metodo Verifica se l'utente è un amministratore
	 *            <p>
	 *            Necessario per gestione form Statistiche
	 *            </p>
	 * @param codice
	 *            utente (12)
	 * @return boolean true (amministratore) false
	 */
	public boolean isAmministratore(String codice) {
		boolean esito = false;

		// Se il codice non è in formato standard non perdo tempo
		// if (codice.length() >= 3) {
		// String codice_polo = codice.substring(0, 3);
		//
		// // L'amminstratore non può essere un POLO...
		// if (isPolo(codice) == false) {
		// ValidatorContainerUser findit = (ValidatorContainerUser)
		// getAbilitazioniUtente(codice);
		// List elenco_gruppi_utente = findit
		// .getElenco_gruppi_utente();
		//
		// for (int ind_gruppo = 0; ind_gruppo < elenco_gruppi_utente
		// .size(); ind_gruppo++) {
		// Tb_gruppo read_gruppo = (Tb_gruppo) elenco_gruppi_utente
		// .get(ind_gruppo);
		//
		// if (read_gruppo.getDS_GRUPPO().equalsIgnoreCase(
		// "Amministratore") == true) {
		// esito = true;
		// break;
		// }
		// }
		// }
		// }

		return (esito);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Ritorna vettore parametri semantici <br>
	 *            torna elenco oggetti costruiti in base all'albero delle
	 *            abilitazioni <br>
	 * @param codice
	 *            polo
	 * @return vettore con i dati richiesti
	 */
	public List getParametriSemantica(String codice) {
		List return_List = new ArrayList();

		ValidatorContainerObject contenitore = getAbilitazioni(codice);

		if ((contenitore != null)
				&& (contenitore.getElenco_parametri_sem() != null))
			return_List = contenitore.getElenco_parametri_sem();

		return (return_List);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Ritorna vettore parametri semantici <br>
	 *            torna elenco oggetti costruiti in base all'albero delle
	 *            abilitazioni <br>
	 * @param codice
	 *            utente
	 * @return vettore con i dati richiesti
	 */
	public List getAttivitaFigli(String codice_soggetto,
			String codice_attivita) {
		List elenco_attivita_figli = new ArrayList(); // id_tipo_att_base

		// Mi prendo tutte le abilitazioni
		ValidatorContainerObject info_obj = getAbilitazioni(codice_soggetto);

		if (info_obj != null) {
			elenco_attivita_figli = info_obj.getAttivitaFigli(codice_attivita);
		}
		return (elenco_attivita_figli);
	}

	private List getParametriMateriale(String codice) {
		List return_List = new ArrayList();

		ValidatorContainerObject contenitore = getAbilitazioni(codice);

		if ((contenitore != null)
				&& (contenitore.getElenco_parametri_materiale() != null))
			return_List = contenitore.getElenco_parametri_materiale();

		return (return_List);
	}

	private List getParametriAuthority(String codice) {

		List return_List = new ArrayList();

		ValidatorContainerObject contenitore = getAbilitazioni(codice);

		if ((contenitore != null)
				&& (contenitore.getElenco_parametri_authority() != null))
			return_List = contenitore.getElenco_parametri_authority();

		return (return_List);
	}

	private ValidatorContainerObject getAbilitazioniUtente(String cd_utente_amm) {
		String userId = cd_utente_amm.substring(6).trim();
		return ProfilerCache.getUserProfile(userId, false);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Ritorna vettore parametri semantici <br>
	 *            torna elenco oggetti costruiti in base all'albero delle
	 *            abilitazioni <br>
	 * @param codice
	 *            utente
	 * @return vettore con i dati richiesti
	 */
	public String getCodicePolo() {
		return ProfilerCache.getPolo().getCd_polo();
	}

	private boolean verificaAttivitaUtenteDesc(String cd_utente_amm,
			String descrizione_attivita) {
		boolean found = false;

		ValidatorContainerObject informazione = getAbilitazioniUtente(cd_utente_amm);

		if (informazione != null) {
			for (int ind_cerca = 0; ind_cerca < informazione
					.getElenco_attivita().size(); ind_cerca++) {
				Tbf_attivita current = (Tbf_attivita) informazione
						.getElenco_attivita().get(ind_cerca);

				if (current.getId_attivita_sbnmarc() != null
						&& current.getId_attivita_sbnmarc().getDs_attivita()
								.trim().equals(descrizione_attivita) == true) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	private boolean verificaAttivitaUtenteID(String cd_utente_amm,
			String codice_attivita) {
		boolean found = false;

		ValidatorContainerObject informazione = getAbilitazioniUtente(cd_utente_amm);

		if (informazione != null) {
			if (informazione.get_attivita(codice_attivita) != null) {
				found = true;
			}
		}
		return found;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Ritorna vettore parametri semantici <br>
	 *            torna elenco oggetti costruiti in base all'albero delle
	 *            abilitazioni <br>
	 * @param codice
	 *            utente
	 * @return vettore con i dati richiesti
	 */
	public Tbf_biblioteca_in_polo getBiblioteca(String cod_polo,
			String cod_biblio) {

		try {
			return ProfilerCache.getBiblioteca(cod_polo, cod_biblio);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Inizializzazione patrimonio anagrafe amministrazione <br>
	 *            legge i dati su cui attivare i permessi (tutto in fase
	 *            iniziale) <br>
	 * @param connessione
	 *            al db
	 * @return nessuno
	 */
	public void init() {

	} // End init

	public void reloadProfili() {
		log.info("reloadProfili");
		rimuoviProfili(null);
	}

	public void reloadProfilo(String userId) {
		ProfilerCache.getUserProfile(userId, true);
	}

	public void rimuoviProfili(String userId) {
		log.info("rimuoviProfili()");

		try {
			ProfilerCache.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rimuoviProfilo(String userId) {
		log.info("rimuoviProfilo() per " + userId);
		ProfilerCache.clear();
	}

	public void reloadCodici() {
		log.info("reloadCodici()");
		try {
			Decodificatore.clear();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Ritorna vettore parametri semantici <br>
	 *            torna elenco oggetti costruiti in base all'albero delle
	 *            abilitazioni <br>
	 * @param codice
	 *            utente
	 * @return vettore con i dati richiesti
	 */
	public Tbf_bibliotecario getUtente(String codice_utente) {
		Tbf_bibliotecario return_utente = null;

		// Mi prendo tutte le abilitazioni
		ValidatorContainerUser info_utente = (ValidatorContainerUser) getAbilitazioniUtente(codice_utente);

		if ((info_utente != null)
				&& (info_utente.getTbf_bibliotecario() != null))
			return_utente = info_utente.getTbf_bibliotecario();
		// else
		// {
		// // Carica profilo utente in albero JNDI
		// return_utente = this.loadProfilo(codice_utente);
		// // Troviamo l'utente
		// }

		return return_utente;
	}

}
