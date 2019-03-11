/**
 *
 */
package it.iccu.sbn.ejb.domain.bibliografica;

import it.iccu.sbn.ejb.model.unimarcmodel.AttivitaAbilitateType;
import it.iccu.sbn.ejb.model.unimarcmodel.C2_250;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriAuthorityType;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriConfigType;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriDocumentiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.model.unimarcmodel.SistemaClassificazione;
import it.iccu.sbn.ejb.model.unimarcmodel.SottoAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAbilitaOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.iccu.sbn.servizi.ProfilerManager;
import it.iccu.sbn.servizi.ProfilerManagerMBean;
import it.iccu.sbn.util.profiler.SbnWebProfileCache;
import it.iccu.sbn.vo.custom.amministrazione.ParametriAuthorityVO;
import it.iccu.sbn.vo.custom.amministrazione.ParametriDocumentiVO;
import it.iccu.sbn.vo.custom.amministrazione.UserProfile;
import it.iccu.sbn.vo.custom.profilo.AttivitaBibliotecario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc --> * <!--
 * begin-xdoclet-definition -->
 *
 * @ejb.bean name="Profiler" description="A session bean named Profiler"
 *           display-name="Profiler" jndi-name="sbnWeb/Profiler"
 *           type="Stateless" transaction-type="Container" view-type = "remote"
 *
 * @ejb.util generate="no"
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class ProfilerBean implements SessionBean {

	private static final long serialVersionUID = 185075437859270218L;

	private static Logger log = Logger.getLogger(ProfilerBean.class);

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
		return;
	}

	private SbnProfileType getProfile(String ticket) throws Exception {
		UserProfile utente = SbnWebProfileCache.getInstance().getProfile(ticket);
		SbnProfileType profile = utente.getProfile();
		return profile;
	}

	private SbnProfileType getProfileIndice() throws Exception {

		ProfilerManagerMBean pm = ProfilerManager.getProfilerManagerInstance();
		SbnProfileType profile = pm.getProfiloIndice(false);
		return profile;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Metodo di recupero delle funzioni abilitate presenti sul
	 *            profilo dell'utente
	 */
	private List<AttivitaBibliotecario> getFunzioniAbilitate(String ticket) throws Exception {
		List<AttivitaBibliotecario> ret = new ArrayList<AttivitaBibliotecario>();
		AttivitaAbilitateType[] listaAttivita = this.getAttivita(ticket);

		for (int i = 0; i < listaAttivita.length; i++) {
			AttivitaAbilitateType attivita = listaAttivita[i];

			AttivitaBibliotecario attBib = new AttivitaBibliotecario(attivita
					.getAttivita().getCodAttivita().trim(), attivita
					.getAttivita().getContent().trim());

			SottoAttivita[] listaSottoAttivita = attivita.getSottoAttivita();

			// SOTTOATTIVITA
			for (int j = 0; j < listaSottoAttivita.length; j++) {
				AttivitaBibliotecario sottoAttivita = new AttivitaBibliotecario(
						listaSottoAttivita[j].getCodAttivita().trim(),
						listaSottoAttivita[j].getContent().trim());
				attBib.addSottoAttivita(sottoAttivita);
			}
			ret.add(attBib);
		}
		return ret;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean method stub
	 */
	public boolean isOkAttivita(String ticket, String codiceFunzione) throws EJBException {
		try {
			List<AttivitaBibliotecario> attivita = this.getFunzioniAbilitate(ticket);

			for (AttivitaBibliotecario attBib : attivita) {
				if (attBib.getCodAttivita().equals(codiceFunzione)
						|| attBib.getSottoAttivita().containsKey(codiceFunzione))
					return true;
			}
			return false;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void getAttivitaAbilitate(String ticket, String codiceFunzione, String tipoOggetto) {
		return;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean method stub
	 */
	public String getBibliotecaUtente(String ticket) throws Exception {
		return this.getProfile(ticket).getBibliotecaUtente();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean method stub
	 */

	public SbnProfileType getProfilo(String ticket) throws Exception {
		if (ValidazioneDati.isFilled(ticket))
			return this.getProfile(ticket);
		else
			return this.getProfileIndice();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * @return la lista delle attivita abilitate per l'utente
	 */
	public AttivitaAbilitateType[] getAttivita(String ticket) {
		try {
			return this.getProfilo(ticket).getAttivitaAbilitate();
		}  catch (Exception e) {
			log.error("", e);
			return new AttivitaAbilitateType[0];
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * @return la lista delle parametrizzazioni per i documenti
	 */
	public ParametriDocumentiType[] getParametriDocumenti(String ticket) {
		try {
			return this.getProfilo(ticket).getParametriDocumenti();
		} catch (Exception e) {
			log.error("", e);
			return new ParametriDocumentiType[0];
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * @return la lista delle parametrizzazioni per le authority
	 */
	public ParametriAuthorityType[] getParametriAuthority(String ticket) {
		try {
			return this.getProfilo(ticket).getParametriAuthority();
		} catch (Exception e) {
			log.error("", e);
			return new ParametriAuthorityType[0];
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * @return i parametri di configurazione generali
	 */
	public ParametriConfigType getParametriGenerali(String ticket) {
		try {
			//almaviva5_20130305 parametri bib per semantica
			if (ValidazioneDati.trimOrEmpty(ticket).matches("\\w{3}\\s\\w{2}"))
				return getParametriGeneraliBiblioteca(ticket);

			return this.getProfilo(ticket).getParametri();
		} catch (NullPointerException nex) {
			return null;
		} catch (Exception e) {

			log.error("", e);
			return null;
		}
	}

	private ParametriConfigType getParametriGeneraliBiblioteca(String ticket) throws Exception {
		//almaviva5_20130305 parametri bib per semantica
		ParametriConfigType output = new ParametriConfigType();
		Tbf_biblioteca_in_poloDao  dao = new Tbf_biblioteca_in_poloDao();
		Tbf_biblioteca_in_polo bib = dao.select(ticket.substring(0, 3), ticket.substring(3, 6) );
		@SuppressWarnings("unchecked")
		Iterator<Tbf_par_sem> i = ((Set<Tbf_par_sem>) bib.getId_parametro().getTbf_par_sem()).iterator();
		while (i.hasNext()) {
			Tbf_par_sem tps = i.next();
			CodiciType type = CodiciType.fromString(tps.getTp_tabella_codici());
			boolean locale = ValidazioneDati.isFilled(tps.getSololocale()) ? (tps.getSololocale().charValue() == 'S') : true;
			switch (type) {
			case CODICE_SISTEMA_CLASSE:
				SistemaClassificazione sc = new SistemaClassificazione();
				sc.setContent(tps.getCd_tabella_codici());
				sc.setSololocale(locale);
				output.addSistemaClassificazione(sc);
				break;

			case CODICE_SOGGETTARIO:
			case CODICE_THESAURO:
				C2_250 sogg = new C2_250();
				sogg.setContent(tps.getCd_tabella_codici());
				sogg.setSololocale(locale);
				output.addC2_250(sogg);
				break;

			default:
				continue;
			}
		}

		return output;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Cerca il livello dell'utente possieduto per una determinata
	 *            autorithy
	 *
	 * @param authority
	 *            authority per la quale serve di conoscere il livello
	 *            dell'utente
	 * @return il livello utente per l'authority specificata
	 */
	public String getLivelloAuthority(SbnAuthority authority, String ticket) {
		String livello = null;
		ParametriAuthorityType[] parametriAut = getParametriAuthority(ticket);

		for (int i = 0; (i < parametriAut.length) && (livello == null); i++) {
			if (parametriAut[i].getTipoAuthority().toString().equals(authority.toString())) {
				SbnLivello livelloSbn = parametriAut[i].getLivelloAut();
				livello = livelloSbn.toString();
			}
		}

		// Se l'utente non è abilitato per parametri di
		// authority, verrebbe restituito un null che
		// provocherebbe un'eccezione nel momento in cui
		// si tentasse di convertire il tipo String in
		// un int: quindi in quel caso restituisce un tipo
		// String("0") che, convertito in int, equivale a 0.
		if (livello == null) {
			return new String("0");
		} else {
			return livello;
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Indica se l'utente è abilitato alla forzatura del tipo
	 *            authority
	 *
	 * @param authority
	 *            tipo authority
	 *
	 * @return true l'utente è abilitato a fare la forzatura
	 */
	public boolean isForzaturaAuthority(SbnAuthority authority, String ticket) {
		Boolean forzatura = null;
		ParametriAuthorityType[] parametriAut = getParametriAuthority(ticket);

		for (int i = 0; (i < parametriAut.length) && (forzatura == null); i++) {
			if (parametriAut[i].getTipoAuthority().toString().equals(authority.toString())) {

				if (parametriAut[i].getAbilitatoForzatura().toString().equals(SbnIndicatore.S.toString()))
					forzatura = new Boolean(true);
				else
					forzatura = new Boolean(false);
			}
		}

		if (forzatura == null)
			forzatura = new Boolean(false);

		return forzatura.booleanValue();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean method stub
	 */
	public boolean isAbilitazioneAuthority(SbnAuthority authority,
			String ticket) {

		int type = authority.getType();
		for (ParametriAuthorityType pat : getParametriAuthority(ticket) )
			if (pat.getTipoAuthority().getType() == type)
				return (pat.getAbilitaAuthority().getType() == SbnAbilitaOggetto.S_TYPE);

		return false;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated True se l'utente è abilitato alla gestione di un determinato
	 *            tipo materiale.
	 *
	 * @param tipoMateriale
	 * @return boolean
	 */
	public boolean isAbilitazioneTipoMate(String tipoMateriale, String ticket) {
		//almaviva5_20151008 #5992
		if (!ValidazioneDati.isFilled(tipoMateriale))
			return false;

		int type = SbnMateriale.valueOf(tipoMateriale).getType();
		for (ParametriDocumentiType pdt : getParametriDocumenti(ticket) )
			if (pdt.getTipoMateriale().getType() == type)
				return (pdt.getAbilitaOggetto().getType() == SbnAbilitaOggetto.S_TYPE);

		return false;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * @param authority
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean getProfiloTipoAuthority(SbnAuthority authority,	String ticket) {
		String livello = null;
		ParametriAuthorityType[] parametriAut = getParametriAuthority(ticket);

		for (int i = 0; (i < parametriAut.length) && (livello == null); i++) {
			if (parametriAut[i].getTipoAuthority().toString().equals(authority.toString()))
				return true;
		}

		return false;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Cerca il livello dell'utente possieduto per un determinato
	 *            tipo materiale
	 *
	 * @param materiale
	 *            tipo materiale per il quale serve conoscere il livello
	 *            dell'utente
	 * @return il livello utente per il tipo materiale specificato
	 */
	public String getLivelloDocumento(SbnMateriale materiale, String ticket) {
		String livello = null;
		ParametriDocumentiType[] parametriDoc = getParametriDocumenti(ticket);

		for (int i = 0; (i < parametriDoc.length) && (livello == null); i++) {
			if (parametriDoc[i].getTipoMateriale().toString().equals(materiale.toString())) {
				SbnLivello livelloSbn = parametriDoc[i].getLivelloAut();
				livello = livelloSbn.toString();
			}
		}

		// Se l'utente non è abilitato per parametri di
		// authority, verrebbe restituito un null che
		// provocherebbe un'eccezione nel momento in cui
		// si tentasse di convertire il tipo String in
		// un int: quindi in quel caso restituisce un tipo
		// String("0") che, convertito in int, equivale a 0.
		if (livello == null) {
			return new String("0");
		} else {
			return livello;
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * @param attivitaUtente
	 *            DOCUMENT ME!
	 * @param sottoAttivitaUtente
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isOkAttivita(String attivitaUtente, String sottoAttivitaUtente, String ticket) {
		boolean esito = false;

		try {
			AttivitaAbilitateType[] listaAttivita = this.getAttivita(ticket);

			// ATTIVITA:
			for (int i = 0; i < listaAttivita.length; i++) {
				AttivitaAbilitateType attivita = listaAttivita[i];

				if (attivita.getAttivita().getContent().equals(attivitaUtente)) {
					SottoAttivita[] listaSottoAttivita = attivita.getSottoAttivita(); // Prelievo delle sottoattivita:

					// SOTTOATTIVITA
					for (int j = 0; j < listaSottoAttivita.length; j++) {
						if (listaSottoAttivita[j].getContent().equals(sottoAttivitaUtente))
							esito = true;
					}
				}
			}

		} catch (NumberFormatException e1) {

		}

		return esito;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Indica se l'utente ha l'attivita-sottoattivita abilitata.
	 *            author: manuel
	 *
	 * @param attivitaUtente
	 *            attivita dell'utente richiesta
	 * @param sottoAttivitaUtente
	 *            sottoattivita dell'utente richiesta
	 *
	 * @return true l'utente ha l'abilitazione per l'attivita-sottoattivita
	 */
	public boolean isOkAttivitaConSottoAttivita(String attivitaUtente,
			String sottoAttivitaUtente, String ticket) {
		boolean esito = false;

		AttivitaAbilitateType[] listaAttivita = this.getAttivita(ticket);

		// ATTIVITA richiesta
		int i = 0;

		while (!(esito) && (i < listaAttivita.length)) {
			AttivitaAbilitateType attivita = listaAttivita[i];

			if (attivita.getAttivita().equals(attivitaUtente)) {
				SottoAttivita[] listaSottoAttivita = attivita.getSottoAttivita(); // Prelievo delle sottoattivita:

				// SOTTOATTIVITA richiesta
				for (int j = 0; j < listaSottoAttivita.length; j++) {
					if (listaSottoAttivita[j].getContent().equals(sottoAttivitaUtente)) {
						esito = true;
						break;
					}
				}
			}
			i++;
		}

		return esito;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Controlla il livello di autorita di un elemento con il livello
	 *            di autorita dell'utente author: manuel
	 *
	 * @param livello
	 *            livello di autorita di un elemento
	 * @param tipoAuthority
	 *            tipo authority dell'elemento
	 *
	 * @return true, il livello dell'elemento e' minoe all'livello dell'utente
	 */
	public boolean isOkControlloLivelloAutorita(String livello, SbnAuthority tipoAuthority, String ticket) {
		boolean esito = true;

		try {
			if (livello != null) {
				int liv = Integer.parseInt(livello.trim());

				if (getLivelloAuthority(tipoAuthority, ticket) != null) {
					int livUtente = Integer.parseInt(getLivelloAuthority(tipoAuthority, ticket));

					if (liv > livUtente) {
						esito = false;
					}
				} else {
					esito = false;
				}
			}
		} catch (NumberFormatException e1) {
			// System.out.println("errore livello di autorita");
		}

		return esito;
	}

	private boolean isOkControlloLivelloDocumento(String livello,
			SbnMateriale materiale, String ticket) {
		boolean esito = true;

		try {
			if (livello != null) {
				int liv = Integer.parseInt(livello.trim());

				String livelloProfilo;

				if ((livelloProfilo = getLivelloDocumento(materiale, ticket)) != null) {
					int livUtente = Integer.parseInt(livelloProfilo);

					if (liv > livUtente) {
						esito = false;
					}
				} else {
					esito = false;
				}
			}
		} catch (NumberFormatException e1) {
			// System.out.println("errore livello di autorita");
		}

		return esito;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Controlla il livello di autorita di un elemento con il livello
	 *            di autorita dell'utente author: manuel
	 *
	 * @param livello
	 *            livello di autorita di un elemento
	 * @param tipoAuthority
	 *            tipo authority dell'elemento
	 *
	 * @return true, il livello dell'elemento e' minoe all'livello dell'utente
	 */
	public boolean isOkAttivitaUser(String codiceAttivita, String ticket) {

		boolean esito = false;

		try {
			// Prelevo tutte le Attivita con le sue sottoAttivita
			AttivitaAbilitateType[] listaAttivita = this.getAttivita(ticket);

			// ATTIVITA:
			for (int i = 0; i < listaAttivita.length; i++) {
				AttivitaAbilitateType attivita = listaAttivita[i];

				if (attivita.getAttivita().getCodAttivita().equals(codiceAttivita)) {
					esito = true;
					break;
				}
				SottoAttivita[] listaSottoAttivita = attivita.getSottoAttivita();

				// SOTTOATTIVITA
				for (int j = 0; j < listaSottoAttivita.length; j++) {
					if (listaSottoAttivita[j].getCodAttivita().equals(codiceAttivita)) {
						esito = true;
						break;
					}
				}
			}
		} catch (NumberFormatException e1) {
			esito = false;
		}
		return esito;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated Controlla se l'utente e abilitato ad eseguire l'attivita per
	 *            il tipo oggetto specificato
	 *
	 * @param codiceAttivita
	 *            Codice Attivita da verificare
	 * @param livello
	 *            livello di autorita assegnato dall'utente verso l'oggetto
	 * @param tipoOggetto
	 *            tipo elemento su cui controllare l'abilitazione ( sono
	 *            accettati solo oggetti di tipo SbnMateriale,SbnAuthority)
	 *
	 * @param ticket
	 *            valore assegnato all'utente in fase di login
	 *
	 * @return true, il livello dell'elemento e' minoe all'livello dell'utente
	 */
	public boolean isOkAttivitaUser(String codiceAttivita, String livello,
			Object tipoOggetto, String ticket) {

		boolean esito = false;

		try {

			if (isOkAttivitaUser(codiceAttivita, ticket)) {
				if (tipoOggetto instanceof SbnAuthority)
					esito = this.isOkControlloLivelloAutorita(livello,  (SbnAuthority) tipoOggetto, ticket);

				else if (tipoOggetto instanceof SbnMateriale)
					esito = this.isOkControlloLivelloDocumento(livello,	(SbnMateriale) tipoOggetto, ticket);
				else {
					esito = false;
				}
			}
		} catch (NumberFormatException e1) {
			esito = false;
		}
		return esito;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 */
	public boolean isSemanticaIndice(String cod_soggettario, String ticket) {
		// EMULAZIONE
		return false;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 */
	public int getLivelloDocumento(String natura, String tipoMateriale,	String ticket) throws Exception {
		// INIZIO
		//=========================================================================================
		// Controllo profilo per variazione in base alla natura dell'oggetto

		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		// Modifica almaviva2 del 05/08/2011 - Intervento interno per eliminare il controllo passando per la getParametriGenerali
		// (che contiene il livello di autorità del POLO) e sostituendolo con la getLivelloDocumento richiedendolo con il tipo materiale = M
		// in tutti i casi (nature) in cui tale valore è spazio o null
//		switch (natura.charAt(0)) {
//		case 'A':
//			if (natura.equals("AUM")) {
//				return Integer.parseInt(this.getLivelloAuthority(SbnAuthority.UM, ticket));
//			} else {
//				return Integer.parseInt(this.getLivelloAuthority(SbnAuthority.TU, ticket));
//			}
//
//		case 'C':
//		case 'R':
//		case 'M':
//		case 'S':
//		case 'W':
//		case 'N':
//			SbnMateriale sbnMateriale = null;
//			int livello;
//			if (!tipoMateriale.equals("")) {
//				sbnMateriale = SbnMateriale.valueOf(tipoMateriale);
//			}
//
//			if (sbnMateriale != null) {
//				livello = Integer.parseInt(this.getLivelloDocumento(
//						sbnMateriale, ticket));
//			} else {
//				livello = Integer.parseInt(this.getParametriGenerali(ticket)
//						.getLivelloAutDoc().toString().trim());
//			}
//			return livello;
//
//		case 'B':
//		case 'D':
//		case 'P':
//		case 'T':
//			return Integer.parseInt(this.getParametriGenerali(ticket)
//					.getLivelloAutDoc().toString().trim());
//		}

		switch (natura.charAt(0)) {
		case 'A':
			if (natura.equals("AUM")) {
				return Integer.parseInt(this.getLivelloAuthority(SbnAuthority.UM, ticket));
			} else {
				return Integer.parseInt(this.getLivelloAuthority(SbnAuthority.TU, ticket));
			}

		case 'C':
		case 'R':
		case 'M':
		case 'S':
		case 'W':
		case 'N':
		case 'B':
		case 'D':
		case 'P':
		case 'T':
			SbnMateriale sbnMateriale = null;
			int livello;
			if (!tipoMateriale.equals("")) {
				sbnMateriale = SbnMateriale.valueOf(tipoMateriale);
			}

			if (sbnMateriale != null) {
				livello = Integer.parseInt(this.getLivelloDocumento(
						sbnMateriale, ticket));
			} else {
				// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
				// sbnMateriale = SbnMateriale.VALUE_0;
				sbnMateriale = SbnMateriale.valueOf("M");
				livello = Integer.parseInt(this.getLivelloDocumento(
						sbnMateriale, ticket));
			}
			return livello;
		}

		throw new Exception();
	}

	public boolean isLivelloDocumento(String natura, String tipoMateriale,
			String livelloDocumento, String ticket) throws Exception {
		int codiceLivelloUtente = this.getLivelloDocumento(natura,
				tipoMateriale, ticket);
		if (codiceLivelloUtente >= Integer.parseInt(livelloDocumento))
			return true;
		return false;
	}

	// Inizio modifica almaviva2 09.07.2010 Nuovo metodo per Authority (TU/UM)
	public boolean isLivelloAuthority(SbnAuthority authority,
			String livelloDocumento, String ticket) throws Exception {
		String codiceLivelloUtente = this.getLivelloAuthority(authority, ticket);
		if (Integer.parseInt(codiceLivelloUtente) >= Integer.parseInt(livelloDocumento))
			return true;
		return false;
	}

	// Inizio modifica almaviva2 09.07.2010 Nuovo metodo per Authority (TU/UM)

	//almaviva5_20140128 evolutive google3
	public boolean isAuthoritySoloLocale(String ticket, SbnAuthority auth) throws EJBException {
		try {
			UserProfile utente = SbnWebProfileCache.getInstance().getProfile(ticket);
			Map<String, ParametriAuthorityVO> params = utente.getParametriAuthority();
			if (!ValidazioneDati.isFilled(params) )
				throw new UtenteNotAuthorizedException();

			ParametriAuthorityVO paramAuthority = params.get(auth.toString());
			if (paramAuthority == null)
				throw new UtenteNotAuthorizedException();

			return paramAuthority.isSololocale();
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public boolean isTipoMaterialeSoloLocale(String ticket, String tipoMateriale) throws EJBException {
		try {
			UserProfile utente = SbnWebProfileCache.getInstance().getProfile(ticket);
			ParametriDocumentiVO paramDocumenti = utente.getParametriDocumenti().get(tipoMateriale);
			if (paramDocumenti == null)
				throw new UtenteNotAuthorizedException();

			return paramDocumenti.isSololocale();
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

}
