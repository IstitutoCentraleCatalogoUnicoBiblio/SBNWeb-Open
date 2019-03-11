/**
 *
 */
package it.iccu.sbn.ejb.domain.amministrazione;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO.ParametroType;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.exception.UtenteNotProfiledException;
import it.iccu.sbn.extension.auth.bibliotecario.BibLoginResponse;
import it.iccu.sbn.extension.auth.bibliotecario.BibLoginResponse.LoginResult;
import it.iccu.sbn.persistence.dao.amministrazione.ContatoriDAO;
import it.iccu.sbn.persistence.dao.amministrazione.IntranetRangeDAO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecarioDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_poloDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_utenti_professionali_webDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_intranet_range;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;
import it.iccu.sbn.polo.orm.amministrazione.Trf_gruppo_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_profilo_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;
import it.iccu.sbn.servizi.bibliotecario.auth.BibliotecarioLoginService;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Base64Util;
import it.iccu.sbn.util.amministrazione.ProfilazioneUtil;
import it.iccu.sbn.util.amministrazione.ProfilazioneUtil.Origine;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.profiler.SbnWebProfileCache;
import it.iccu.sbn.vo.custom.amministrazione.Default;
import it.iccu.sbn.vo.custom.amministrazione.MergedParAut;
import it.iccu.sbn.vo.custom.amministrazione.MergedParMat;
import it.iccu.sbn.vo.custom.amministrazione.OrderedTreeElement;
import it.iccu.sbn.vo.custom.amministrazione.UserProfile;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.verisignlabs.irisserv.areg.ARegUtils;
import com.verisignlabs.irisserv.areg.CIDR;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;


/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 * @ejb.bean name="AmministrazioneBibliotecario"
 *           description="A session bean named AmministrazioneBibliotecario"
 *           display-name="AmministrazioneBibliotecario"
 *           jndi-name="sbnWeb/AmministrazioneBibliotecario"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type = "remote"
 *
 * @ejb.util
 *   generate="no"
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class AmministrazioneBibliotecarioBean extends AmministrazioneBaseBean implements AmministrazioneBibliotecario {

	private static final long serialVersionUID = 8540398692909652725L;

	private static final Logger log = Logger.getLogger(AmministrazioneBibliotecarioBean.class);

	private AmministrazionePolo amministrazionePolo;

	private AmministrazioneBiblioteca amministrazioneBiblioteca;

	private SessionContext ctx;

    /**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @ejb.create-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
		return;
	}

	private AmministrazionePolo getAmministrazionePolo() {
		if (amministrazionePolo != null)
			return amministrazionePolo;

		try {
			this.amministrazionePolo = DomainEJBFactory.getInstance().getPolo();
			return amministrazionePolo;

		} catch (CreateException e) {
			log.error("", e);
			throw new EJBException("300",e);
		} catch (NamingException e) {
			log.error("", e);
			throw new EJBException("300",e);
		} catch (RemoteException e) {
			throw new EJBException("300",(Exception) e.detail);
		}

	}

	private AmministrazioneBiblioteca getAmministrazioneBiblioteca() throws Exception {

		if (amministrazioneBiblioteca != null)
			return amministrazioneBiblioteca;

		this.amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBiblioteca;

	}



	private String assegnaTicket(UserProfile user) throws EJBException {
		String ticket = generateUniqueTicket(user.getPolo(), user.getBiblioteca(), user.getUserName() );
		log.debug(user.getUserName() + " - ticket assegnato: " + ticket);
		return ticket;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public SbnUserType getUserSbnMarc(String ticket) throws DaoManagerException
	{
		/**
		 * Composizione ticket
		 * CodPolo_CodBib_UserId_timestampNumberRandom
		 */
		String[] param = ticket.split("_");
		/**
		 * param[0] = COD POLO
		 * param[1] = COD BIB
		 * param[2] = COD USERID
		 */
		SbnUserType sbnusertype = new SbnUserType();
		sbnusertype.setUserId(new String(Base64Util.decode(param[2])) );
		sbnusertype.setBiblioteca(param[0]+param[1]);
		return sbnusertype;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws UtenteNotFoundException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public UserProfile login(String userId, String plainPwd, InetAddress addr)
			throws DaoManagerException, UtenteNotFoundException, UtenteNotProfiledException {

		log.debug("tentativo login per utente: " + userId);

		if (!ValidazioneDati.isFilled(plainPwd))
			throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_FOUND);

		try {
			BibLoginResponse loginResponse = BibliotecarioLoginService.login(userId, plainPwd);

			if (loginResponse.getStatus() == LoginResult.NOT_FOUND)
				throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_FOUND);

			if (loginResponse.getStatus() == LoginResult.DISABLED)
				throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NON_ACTIVE);

			Tbf_utenti_professionali_webDao dao = new Tbf_utenti_professionali_webDao();
			Tbf_utenti_professionali_web utente = dao.select(userId);
			if (utente == null)
				throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_FOUND);

			//almaviva5_20090727 utente cancellato
			if (utente.getFl_canc() == 'S')
				throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NON_ACTIVE);

			UserProfile profile = new UserProfile();

			Tbf_anagrafe_utenti_professionali utenteProf = utente.getId_utente_professionale();

			Set<?> utentiProf = utenteProf.getTrf_utente_professionale_biblioteca();
			if (utentiProf == null)	//non trovato
				throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_FOUND);

			Trf_utente_professionale_biblioteca bibUteProf = (Trf_utente_professionale_biblioteca) ValidazioneDati.first(utentiProf);

			if (bibUteProf != null) {
				// almaviva5_20080219
				profile.setIdUtenteProfessionale(utenteProf.getId_utente_professionale());

				Tbf_biblioteca_in_polo bib = bibUteProf.getCd_polo();

				//almaviva5_20120228 check biblioteca
				if (bib.getFl_canc() == 'S' || bib.getId_biblioteca().getFl_canc() == 'S')
					throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_FOUND);

				profile.setBiblioteca(bib.getCd_biblioteca());
				profile.setPolo(bib.getCd_polo().getCd_polo());
				profile.setDescrizioneBiblioteca(bib.getDs_biblioteca());
				profile.setUserName(userId);
				profile.setPassword(plainPwd);

				String nome_cognome = ValidazioneDati.trimOrEmpty(utenteProf.getNome())	+ " " + ValidazioneDati.trimOrEmpty(utenteProf.getCognome());
				profile.setNome_cognome(nome_cognome);

				//almaviva5_20100107
				boolean remoto = isUtenteRemoto(profile.getPolo(), profile.getBiblioteca(), addr);
				profile.setRemoto(remoto);
				log.debug(userId + " - utente remoto: " + remoto);
			} else
				throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_FOUND);

			profile = SbnWebProfileCache.getInstance().getProfile(profile);
			profile.setTicket(assegnaTicket(profile) );
			profile.setPassword(plainPwd);

			if (loginResponse.getStatus() == LoginResult.AUTH_DATA_EXPIRED) {
				//cambio password
				profile.setNewPassword(true);
				return profile;
			}

			addTicket(profile.getTicket(), addr);

			return profile;

		}catch (UtenteNotFoundException e) {
			throw e;
		} catch (UtenteNotProfiledException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws UtenteNotFoundException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public boolean changePassword(String userId, String password)
			throws DaoManagerException, UtenteNotFoundException	{
		// cifro la password;
		PasswordEncrypter crypt = new PasswordEncrypter(password);
		String encryptPwd = crypt.encrypt(password);

		Tbf_utenti_professionali_webDao bibDao = new Tbf_utenti_professionali_webDao();
		Tbf_utenti_professionali_web bib = bibDao.select(userId);
		bib.setPassword(encryptPwd);
		bib.setChange_password('N');
		return bibDao.update(bib);
	}

	class StringComparator implements Comparator<Object> {
		public final int compare(Object a, Object b) {

			if (a instanceof GruppoParametriVO
					&& b instanceof GruppoParametriVO) {
				GruppoParametriVO gp = (GruppoParametriVO) a;
				String sa = gp.getCodice();
				gp = (GruppoParametriVO) b;
				String sb = gp.getCodice();
				return ((sa).compareToIgnoreCase(sb)); // Ascending
			} else {
				boolean x = false;
				boolean y = false;
				UtenteVO gp = (UtenteVO) a;
				String sa = "";
				if (gp.getUsername() != null)
					sa = gp.getUsername();
				else
					x = true;
				String sb = "";
				gp = (UtenteVO) b;
				if (gp.getUsername() != null)
					sb = gp.getUsername();
				else
					y = true;
				if (x && !y)
					return -1;
				if (!x && y)
					return 1;
				if (x && y)
					return 0;
				return ((sa).compareTo(sb)); // Ascending
			}

		} // end compare

	} // end class StringComparator

	public List<UtenteVO> cercaUtenti(String cognome, String nome, String username, String biblioteca, String dataAccesso, String abilitato, String ordinamento) throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			List<Tbf_anagrafe_utenti_professionali> elenco = dao.cercaUtenteProfessionaleWeb(cognome.trim(), nome.trim(), username.trim(), biblioteca, dataAccesso, abilitato, ordinamento);
			List<UtenteVO> output = new ArrayList<UtenteVO>();
			for (int i = 0; i < elenco.size(); i++) {
				Tbf_anagrafe_utenti_professionali utente = elenco.get(i);
				UtenteVO utenteVO = new UtenteVO();
				List<Trf_utente_professionale_biblioteca> elencoBibUtente = new ArrayList<Trf_utente_professionale_biblioteca>(utente.getTrf_utente_professionale_biblioteca());
				String bibliotecaUtente = "";
				if (elencoBibUtente.size() > 0 && elencoBibUtente.get(0) != null) {
					Trf_utente_professionale_biblioteca bibUtente = elencoBibUtente.get(0);
					bibliotecaUtente = bibliotecaUtente + bibUtente.getCd_polo().getId_biblioteca().getNom_biblioteca().trim();
				}
//				for (int j=0; j<elencoBibUtente.size(); j++) {
//					Trf_utente_professionale_biblioteca bibUtente = elencoBibUtente.get(j);
//					if (j == (elencoBibUtente.size() - 1))
//						bibliotecaUtente = bibliotecaUtente + bibUtente.getCd_polo().getId_biblioteca().getNom_biblioteca().trim();
//					else
//						bibliotecaUtente = bibliotecaUtente + bibUtente.getCd_polo().getId_biblioteca().getNom_biblioteca().trim() + ", ";
//				}
				utenteVO.setBiblioteca(bibliotecaUtente);
				utenteVO.setCognome(utente.getCognome().toUpperCase());
				utenteVO.setNome(utente.getNome().toUpperCase());
				utenteVO.setId(utente.getId_utente_professionale());
				utenteVO.setIndice(i + "");
				if (utente.getTbf_utenti_professionali_web() != null) {
					utenteVO.setUsername(utente.getTbf_utenti_professionali_web().getUserid());
					utenteVO.setAbilitato("SI'");
					utenteVO.setDataAccesso(DateUtil.formattaDataOra(utente.getTbf_utenti_professionali_web().getLast_access()));
				}
				else
					utenteVO.setAbilitato("NO");
				output.add(utenteVO);
			}
			if (ordinamento.equals("username"))
				Collections.sort(output, new StringComparator());
			return output;
		}
		catch (DaoManagerException e){
			log.error("", e);
			return null;
		}
	}

	public OrderedTreeElement getElencoAttivita(java.lang.String idUtente)
	throws DaoManagerException, RemoteException {
		List attivita = null;
		List parametri = null;
		List authorities = null;
		String codiceAttivita, descrizioneAttivita, idAttivita, codiceAttivitaParent;

		Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
		OrderedTreeElement ote = new OrderedTreeElement("rootNode", "");

		try {

			attivita = dao.loadCodiciAttivitaBiblioteca(idUtente);

			// Troviamo le attivita di primo livello
			for (int index = 0; index < attivita.size(); index++) {
				Tbf_attivita tbf_attivita = (Tbf_attivita) attivita.get(index);

				codiceAttivita = tbf_attivita.getCd_attivita();
				descrizioneAttivita = tbf_attivita.getId_attivita_sbnmarc().getDs_attivita();
				codiceAttivitaParent = tbf_attivita.getCd_funzione_parent();
				if (codiceAttivitaParent == null) {
					 //ote.addElement(new String(descrizioneAttivita), new String(codiceAttivita + ": " + descrizioneAttivita));
//					ote.addElement(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK",  new String(codiceAttivita));
					ote.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK",  new String(codiceAttivita), tbf_attivita.getPrg_ordimanento() + "");
				}
			}

//			if (ote.getElements().size() > 0)
//				ote.sortByValue();

			// Per ogni attivita di primo livello troviamo figli (niente nipoti per le attivita')
			for (int j=0; j< ote.getElements().size(); j++)
			{
				OrderedTreeElement ote1 = ote.getElements().get(j);

				OrderedTreeElement oteChildren = new OrderedTreeElement("node"+Integer.toString(j), "");

				for (int index = 0; index < attivita.size(); index++) {
					Tbf_attivita tbf_attivita = (Tbf_attivita) attivita.get(index);

					codiceAttivitaParent = tbf_attivita.getCd_funzione_parent();

					if (codiceAttivitaParent != null &&  ((codiceAttivitaParent).compareTo( ((String)ote1.getValue()).substring(0, 5) ) == 0))
		        	{
						codiceAttivita = tbf_attivita.getCd_attivita();
						descrizioneAttivita = tbf_attivita.getId_attivita_sbnmarc().getDs_attivita();
		//				oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita + ": " + descrizioneAttivita));
//						oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita + ": " + descrizioneAttivita));
						oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK", new String(codiceAttivita), tbf_attivita.getPrg_ordimanento() + "");
		        	}
				}
		        if (oteChildren.getElements().size() > 0)
		        {
//			        oteChildren.sort();
//		        	oteChildren.sortBySortValue();
			        ote.addChild(oteChildren,  (String)ote1.getKey());
		        }
		        else
		        	oteChildren = null; // destroy

			} // end for j
		} catch (DaoManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ote;
	} // End getElencoAttivita

	public List getElencoParametri(String idUtente) throws DaoManagerException, RemoteException {
		Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
		int idParametro = dao.getIdParametro(idUtente);
		Tbf_parametro parametri = dao.getParametri(idParametro);
		Set<Tbf_par_auth> parAuth = parametri.getTbf_par_auth();
		Set<Tbf_par_sem> parSem = parametri.getTbf_par_sem();
		Set<Tbf_par_mat> parMat = parametri.getTbf_par_mat();

		//almaviva5_20140702 segnalazione PAL: i parametri per i nuovi utenti vanno impostati a livello 01
		boolean isParAutNew = false;
		boolean isParMatNew = false;

		int parBib = dao.getParametroBibliotecaUtente(idUtente);
		Tbf_parametro parametriBib = dao.getParametri(parBib);
		if (!isFilled(parAuth)) {
			isParAutNew = true;
			parAuth = parametriBib.getTbf_par_auth();
		}
		/*
		if (!isFilled(parSem)) {
			parSem = parametriBib.getTbf_par_sem();
		}
		*/
		if (!isFilled(parMat)) {
			isParMatNew = true;
			parMat = parametriBib.getTbf_par_mat();
		}

		//almaviva5_20140218 evolutive google3
		Tbf_poloDao pdao = new Tbf_poloDao();
		Tbf_parametro parametriPolo = dao.getParametri(pdao.getIdParametro());
		List<MergedParAut> mergedAut = ProfilazioneUtil.mergeParAuth(parametriPolo.getTbf_par_auth(), parametriBib.getTbf_par_auth(), parAuth);
		List<MergedParMat> mergedMat = ProfilazioneUtil.mergeParMat(parametriPolo.getTbf_par_mat(), parametriBib.getTbf_par_mat(), parMat);

		List<Tb_codici> authorities = dao.getAuthorities();
		List<Tb_codici> materiali = dao.getMateriali();

		List<GruppoParametriVO> elencoAut = new ArrayList<GruppoParametriVO>();
		List<GruppoParametriVO> elencoSem = new ArrayList<GruppoParametriVO>();
		List<GruppoParametriVO> elencoMat = new ArrayList<GruppoParametriVO>();
		List output = new ArrayList();

		//Authority:
		for (MergedParAut aut : mergedAut) {
			GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
			String codice = ((Tbf_par_auth)aut).getCd_par_auth();
			// Troviamo la descrizione dell'authority
			String descrizione = "";
			for (int ik=0; ik < authorities.size(); ik++) {
				Tb_codici tb_codici = authorities.get(ik);
				if (tb_codici.getCd_tabella().startsWith(codice))
					descrizione = tb_codici.getDs_tabella();
			}
			GruppoParametriVO.setCodice(codice);
			GruppoParametriVO.setDescrizione(descrizione);
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			char parChar = ((Tbf_par_auth)aut).getTp_abil_auth();
			parametro.setDescrizione("profilo.polo.parametri.abil");
			parametro.setIndex("0");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getTp_abil_auth_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)aut).getFl_abil_legame();
			parametro.setDescrizione("profilo.polo.parametri.abil_legame");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getFl_abil_legame_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)aut).getFl_leg_auth();
			parametro.setDescrizione("profilo.polo.parametri.leg_auth");
			parametro.setIndex("2");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getFl_leg_auth_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parametro.setType(ParametroType.LIVELLO_AUTORITA);
			String par = ((Tbf_par_auth)aut).getCd_livello();
			parametro.setDescrizione("profilo.polo.parametri.livello");
			parametro.setIndex("3");
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			List<TB_CODICI> elencoCodici = null;
			try {
				elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
//				if (aut.getCd_livello_Origine() != Origine.UTENTE)
//					elencoCodici = ProfilazioneUtil.livelloSoglia(elencoCodici, par);
			} catch (Exception e) {
				log.error("", e);
			}
			for (int k = 1; k < elencoCodici.size(); k++) {
				TB_CODICI tabCodice = elencoCodici.get(k);
				Integer cod1 = Integer.parseInt(tabCodice.getCd_tabella().trim()); //Codice della tabella CODICI
				Integer cod2 = dao.getLivelloParametro(idParametro); //Codice impostato per il parametro
				if (cod1 <= cod2) {
					ComboVO combo = new ComboVO();
					combo.setDescrizione(tabCodice.getCd_tabella() + " " + tabCodice.getDs_tabella());
					combo.setCodice(tabCodice.getCd_tabella());
					elencoCombo.add(combo);
				}
				if (tabCodice.getCd_tabella().trim().equals(par.trim()))
					parametro.setSelezione(tabCodice.getCd_tabella());

			}
			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("MENU");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getCd_livello_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

//			parametro = new ParametroVO();
//			par = ((Tbf_par_auth)auth[i]).getCd_contr_sim();
//			parametro.setDescrizione("profilo.polo.parametri.contr_sim");
//			parametro.setIndex("4");
//			List<String> opzioniSimil = new ArrayList<String>();
//			opzioniSimil.add("000");
//			opzioniSimil.add("001");
//			opzioniSimil.add("002");
//			parametro.setRadioOptions(opzioniSimil);
//			if (par.trim().equals("0"))
//				parametro.setSelezione("000");
//			else if (par.trim().equals("1"))
//				parametro.setSelezione("001");
//			else if (par.trim().equals("2"))
//				parametro.setSelezione("002");
//			parametro.setTipo("RADIO");
//			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)aut).getFl_abil_forzat();
			parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
			parametro.setIndex("4");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getFl_abil_forzat_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parChar = ((Tbf_par_auth)aut).getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex("5");
			parametro.setRadioOptions(opzioniRadio);
			if (codice.trim().equals("PP") || codice.trim().equals("TH")) {
				parametro.setSelezione("Si'");
				parametro.setCongelato("TRUE");
			}
			else {
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
			}
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getSololocale_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			GruppoParametriVO.setElencoParametri(elencoParametri);
			elencoAut.add(GruppoParametriVO);
		}

		// Ordinamento degli authority e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoAut, new StringComparator());

		for (int g = 0; g < elencoAut.size(); g++) {
			elencoAut.get(g).setIndice(g + "");
		}

		output.add(elencoAut);

		//Par_mat:
		for (MergedParMat mat : mergedMat) {
			GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
			char codice = ((Tbf_par_mat)mat).getCd_par_mat();
			// Troviamo la descrizione dei materiali
			String descrizione = "";
			for (int ik=0; ik < materiali.size(); ik++) {
				Tb_codici tb_codici = materiali.get(ik);
				if (tb_codici.getCd_tabella().charAt(0) == codice)
					descrizione = tb_codici.getDs_tabella();
			}
			GruppoParametriVO.setCodice(codice + "");
			GruppoParametriVO.setDescrizione(descrizione);
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			char parChar = ((Tbf_par_mat)mat).getTp_abilitaz();
			parametro.setDescrizione("profilo.polo.parametri.abil");
			parametro.setIndex("0");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(mat.getTp_abilitaz_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

//			parametro = new ParametroVO();
//			String par = ((Tbf_par_mat)parMat[i]).getCd_contr_sim();
//			parametro.setDescrizione("profilo.polo.parametri.contr_sim");
//			parametro.setIndex("1");
//			List<String> opzioniSimil = new ArrayList<String>();
//			opzioniSimil.add("000");
//			opzioniSimil.add("001");
//			opzioniSimil.add("002");
//			parametro.setRadioOptions(opzioniSimil);
//			if (par.trim().equals("0"))
//				parametro.setSelezione("000");
//			else if (par.trim().equals("1"))
//				parametro.setSelezione("001");
//			else if (par.trim().equals("2"))
//				parametro.setSelezione("002");
//			parametro.setTipo("RADIO");
//			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			parChar = ((Tbf_par_mat)mat).getFl_abil_forzat();
			parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(mat.getFl_abil_forzat_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parametro.setType(ParametroType.LIVELLO_AUTORITA);
			String par = ((Tbf_par_mat)mat).getCd_livello();
			parametro.setDescrizione("profilo.polo.parametri.livello");
			parametro.setIndex("2");
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			List<TB_CODICI> elencoCodici = null;
			try {
				elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
//				if (mat.getCd_livello_Origine() != Origine.UTENTE)
//					elencoCodici = ProfilazioneUtil.livelloSoglia(elencoCodici, par);
			} catch (Exception e) {
				log.error("", e);
			}
			for (int k = 1; k < elencoCodici.size(); k++) {
				TB_CODICI tabCodice = elencoCodici.get(k);
				Integer cod1 = Integer.parseInt(tabCodice.getCd_tabella().trim()); //Codice della tabella CODICI
				Integer cod2 = dao.getLivelloParametro(idParametro); //Codice impostato per il parametro
				if (cod1 <= cod2) {
					ComboVO combo = new ComboVO();
					combo.setDescrizione(tabCodice.getCd_tabella() + " " + tabCodice.getDs_tabella());
					combo.setCodice(tabCodice.getCd_tabella());
					elencoCombo.add(combo);
				}
				if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
					parametro.setSelezione(tabCodice.getCd_tabella());
				}
			}
			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("MENU");
			elencoParametri.add(parametro);
			parametro.setCongelato(mat.getCd_livello_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parChar = ((Tbf_par_mat)mat).getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex("3");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(mat.getSololocale_Origine() != Origine.UTENTE ? "TRUE" : "FALSE");

			GruppoParametriVO.setElencoParametri(elencoParametri);
			elencoMat.add(GruppoParametriVO);

		}

		// Ordinamento dei materiali e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoMat, new StringComparator());

		for (int g = 0; g < elencoMat.size(); g++) {
			elencoMat.get(g).setIndice(g + "");
		}

		output.add(elencoMat);

		//Semantica:
		for (Tbf_par_sem sem : parSem) {
			GruppoParametriVO gp = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();

			String codice = "";
			String descrizione = "";
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			if (sem.getTp_tabella_codici().trim().equals("SCLA")) {
				codice = sem.getTp_tabella_codici();
				descrizione = "profilo.polo.parametri.classificazioni";
			}
			else if (sem.getTp_tabella_codici().trim().equals("SOGG")) {
				codice = sem.getTp_tabella_codici();
				descrizione = "profilo.polo.parametri.soggetti";
			}
			else if (sem.getTp_tabella_codici().trim().equals("STHE")) {
				codice = sem.getTp_tabella_codici();
				descrizione = "profilo.polo.parametri.thesauri";
			}
			gp.setCodice(codice);
			gp.setDescrizione(descrizione);
			String par = sem.getCd_tabella_codici();
			gp.setCd_tabella(par);

			if (sem.getTp_tabella_codici().trim().equals("SCLA"))
				parametro.setDescrizione("profilo.polo.parametri.classificazione");
			else if (sem.getTp_tabella_codici().trim().equals("SOGG"))
				parametro.setDescrizione("profilo.polo.parametri.soggetto");
			else if (sem.getTp_tabella_codici().trim().equals("STHE"))
				parametro.setDescrizione("profilo.polo.parametri.thesauro");
			parametro.setIndex("0");
//			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			CodiciType cod = null;
			List<TB_CODICI> elencoCodici = null;
			try {
				cod = CodiciType.fromString(codice);
				elencoCodici = CodiciProvider.getCodici(cod);
			}
			catch (Exception e){
				log.error("", e);
			}
			for (int k = 1; k < elencoCodici.size(); k++) {
				TB_CODICI tabCodice = elencoCodici.get(k);
//				ComboVO combo = new ComboVO();
//				combo.setDescrizione(tabCodice.getDs_tabella());
//				combo.setCodice(tabCodice.getCd_tabella());
//				elencoCombo.add(combo);
				if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
					parametro.setSelezione(tabCodice.getDs_tabella());
				}
			}
//			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("TESTO");
			elencoParametri.add(parametro);

			parametro = new ParametroVO();
			char parChar = sem.getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);

			gp.setElencoParametri(elencoParametri);
			elencoSem.add(gp);
		}

		// Ordinamento dei materiali e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoSem, new StringComparator());

		for (int g = 0; g < elencoSem.size(); g++) {
			elencoSem.get(g).setIndice(g + "");
		}

		output.add(elencoSem);

		//almaviva5_20140702 segnalazione PAL: i parametri per i nuovi utenti vanno impostati a livello 01
		if (isParAutNew)
			ProfilazioneUtil.resetLivelloParametri(elencoAut);
		if (isParMatNew)
			ProfilazioneUtil.resetLivelloParametri(elencoMat);

		return output;

	}

	public String[] getElencoAttivitaProfilo(String idUtente)
	throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			List<Trf_profilo_biblioteca> elenco = dao.loadAttivitaProfiloBibliotecario(idUtente);
			if (elenco != null) {
				String[] output = new String[elenco.size()];
				for (int i = 0; i < elenco.size(); i++) {
					String codiceAtt = elenco.get(i).getCd_attivita().getCd_attivita();
					output[i] = codiceAtt;
				}
				return output;
			}
			return null;
		}
		catch (DaoManagerException e) {
			log.error("", e);
			return null;
		}
	}

	public boolean setProfiloBibliotecario(String idUtente, String codiceUtenteInseritore,
			  List<String> elencoAttivita,
			  List<GruppoParametriVO> elencoAuthorities,
			  List<GruppoParametriVO> elencoMateriali,
			  List<GruppoParametriVO> elencoSemantica) throws DaoManagerException, RemoteException {

		try {
			// ************* ATTIVITA' *************

			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			int idProfilo = dao.getProfiloBibliotecario(idUtente);

			//Rimuovo, se esistono, i dati del profilo dalla tabella Trf_profilo_biblioteca
			dao.eliminaAttivitaDelProfilo(idProfilo);
			dao.inserisciAttivitaDelProfilo(elencoAttivita, idProfilo, codiceUtenteInseritore);
			dao.pulisciProfiloAttitaPadri(idProfilo);
//			biblioDao.attivaProfiloAttivita(codiceBib, idProfilo);

			// ************* PARAMETRI *************
			int idParametro = dao.creaProfiloParametroBibliotecario(idUtente, codiceUtenteInseritore);

			// Se non esiste il profilo dei parametri lo creiamo.
			// Inserisco i relativi parametri:
			if (ValidazioneDati.isFilled(elencoAuthorities) )
				dao.inserisciParAuth(elencoAuthorities, idParametro);
			else
				dao.rimuoviParAuth(idParametro);

			if (ValidazioneDati.isFilled(elencoMateriali) )
				dao.inserisciParMat(elencoMateriali, idParametro);
			else
				dao.rimuoviParMat(idParametro);

			if (ValidazioneDati.isFilled(elencoSemantica) )
				dao.inserisciParSem(elencoSemantica, idParametro);
			else
				dao.rimuoviParSem(idParametro);

			// ************* ATTIVO IL GRUPPO PARAMETRI *************
			dao.attivaProfiloParametri(idUtente, idParametro, Integer.valueOf(codiceUtenteInseritore));
			Tbf_utenti_professionali_web utenteProf = dao.getDatiUtente(idUtente);

			dao.clearCache("amministrazione");
			this.rimuoviProfilo(utenteProf.getUserid() ); // comunico a SbnMarc che il profilo vecchio non è più valido
			SbnWebProfileCache.getInstance().clear(ValidazioneDati.asSingletonList(utenteProf.getUserid()));
			return true;
		}
		catch (Exception e) {
			ctx.setRollbackOnly();
			log.error("", e);
			return false;
		}
	}

	private void rimuoviProfilo(String userId) throws DaoManagerException {
		try {
			getGateway().service("rimuoviProfilo", userId.trim());
		}
		catch (Exception e) {
			log.error("", e);
		}
	}

	public List<String> getNomeBibliotecario(String idUtente)
	throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			return dao.getNomeBibliotecario(Integer.parseInt(idUtente) );
		}
		catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public UtenteVO creaBibliotecario(UtenteVO bibliotecario, int utenteInseritore, boolean forzaInserimento, boolean abilitazione) throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			return dao.inserisciBibliotecario(bibliotecario, utenteInseritore, forzaInserimento, abilitazione);
		}
		catch (Exception e) {
			log.error("", e);
			bibliotecario.setInserito(1);
			return bibliotecario;
		}
	}

	public UtenteVO caricaBibliotecario(int idUtente) throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			return dao.caricaBibliotecario(idUtente);
		}
		catch (Exception e) {
			log.error("", e);
			UtenteVO bibliotecario = new UtenteVO();
			bibliotecario.setInserito(1);
			return bibliotecario;
		}
	}

	public boolean controllaAbilitazioneBibliotecario(int idUtente) throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			return dao.controllaAbilitazioneBibliotecario(idUtente);
		}
		catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

	public int getDurataPassword() throws DaoManagerException, RemoteException {
		try {
			String cdPolo = getAmministrazionePolo().getInfoPolo().getCd_polo();
			return ContatoriDAO.getRinnovoPasswordDays(cdPolo);
		}
		catch (Exception e) {
			log.error("", e);
			return 0;
		}
	}

	public void removeUserTicket(String ticket) throws DaoManagerException, RemoteException {
		removeTicket(ticket);
	}

	public String[] getElencoAttivitaProfiloModello(String idModello)
	throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			List<Trf_gruppo_attivita_polo> elenco = dao.loadAttivitaProfiloModello(idModello);
			if (elenco != null) {
				String[] output = new String[elenco.size()];
				for (int i = 0; i < elenco.size(); i++) {
					String codiceAtt = elenco.get(i).getId_attivita_polo().getCd_attivita().getCd_attivita();
					output[i] = codiceAtt;
				}
				return output;
			}
			return null;
		}
		catch (DaoManagerException e) {
			log.error("", e);
			return null;
		}
	}

	public boolean setModelloBibliotecario(String nomeModello, boolean nuovo, String codiceUtenteInseritore,
			  List<String> elencoAttivita,
			  List<GruppoParametriVO> elencoAuthorities,
			  List<GruppoParametriVO> elencoMateriali,
			  List<GruppoParametriVO> elencoSemantica) throws DaoManagerException, RemoteException {

		try {
			// ************* ATTIVITA' *************

			Tbf_bibliotecarioDao biblioDao = new Tbf_bibliotecarioDao();

			Tbf_modello_profilazione_bibliotecario modelloDB = new Tbf_modello_profilazione_bibliotecario();

			Tbf_gruppo_attivita profilo = new Tbf_gruppo_attivita();
			Tbf_parametro parametro = new Tbf_parametro();

			if (!nuovo) {
				modelloDB = biblioDao.getModello(nomeModello);
				profilo = modelloDB.getId_gruppo_attivita();
				parametro = modelloDB.getId_parametro();
				//Rimuovo, se esistono, i dati del profilo dalla tabella Trf_profilo_biblioteca
				biblioDao.eliminaAttivitaDelGruppo(profilo.getId_gruppo_attivita_polo());
			}
			else {
				profilo = biblioDao.creaProfiloModello(nomeModello);
				parametro = biblioDao.creaParametroModello(codiceUtenteInseritore);
				modelloDB = biblioDao.creaModello(nomeModello, profilo, parametro, codiceUtenteInseritore);
			}

			int idParametro = parametro.getId_parametro();

			biblioDao.inserisciAttivitaDelGruppo(elencoAttivita, profilo);

			biblioDao.pulisciGruppoAttivitaPadri(profilo);

			// ************* PARAMETRI *************

			// Se non esiste il profilo dei parametri lo creiamo.
			// Inserisco i relativi parametri:
			if (elencoAuthorities != null && elencoAuthorities.size() >0)
				biblioDao.inserisciParAuth(elencoAuthorities, idParametro);
			else
				biblioDao.rimuoviParAuth(idParametro);
			if (elencoMateriali != null && elencoMateriali.size() >0)
				biblioDao.inserisciParMat(elencoMateriali, idParametro);
			else
				biblioDao.rimuoviParMat(idParametro);
			if (elencoSemantica != null)
				biblioDao.inserisciParSem(elencoSemantica, idParametro);
			else
				biblioDao.rimuoviParSem(idParametro);

			return true;
		}
		catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

   public List<String> getElencoModelli() throws DaoManagerException, RemoteException {

		try {
			Tbf_bibliotecarioDao biblioDao = new Tbf_bibliotecarioDao();
			return biblioDao.getElencoModelli();
		}
		catch (Exception e) {
			log.error("", e);
			return new ArrayList<String>();
		}
   }

		public List getElencoParametriModello(String nomeModello) throws DaoManagerException, RemoteException {
			Tbf_bibliotecarioDao dao = new Tbf_bibliotecarioDao();
			int idParametro = dao.getModello(nomeModello).getId_parametro().getId_parametro();
			Tbf_parametro parametri = dao.getParametri(idParametro);
			Object auth[] = parametri.getTbf_par_auth().toArray();
			Object semantica[] = parametri.getTbf_par_sem().toArray();
			Object parMat[] = parametri.getTbf_par_mat().toArray();

//			int parBib = dao.getParametroBibliotecaUtente(idUtente);
//			if (auth.length == 0) {
//				parametri = dao.getParametri(parBib);
//				auth = ((Tbf_parametro)parametri.get(0)).getTbf_par_auth().toArray();
//			}
//			if (semantica.length == 0) {
//				parametri = dao.getParametri(parBib);
//				semantica = ((Tbf_parametro)parametri.get(0)).getTbf_par_sem().toArray();
//			}
//			if (parMat.length == 0) {
//				parametri = dao.getParametri(parBib);
//				parMat = ((Tbf_parametro)parametri.get(0)).getTbf_par_mat().toArray();
//			}

			List<Tb_codici> authorities = dao.getAuthorities();
			List<Tb_codici> materiali = dao.getMateriali();

			List<GruppoParametriVO> elencoAuth = new ArrayList<GruppoParametriVO>();
			List<GruppoParametriVO> elencoSem = new ArrayList<GruppoParametriVO>();
			List<GruppoParametriVO> elencoParMat = new ArrayList<GruppoParametriVO>();
			List output = new ArrayList();

			//Authority:
			for (int i = 0; i < auth.length; i++) {
				GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
				List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
				String codice = ((Tbf_par_auth)auth[i]).getCd_par_auth();
				// Troviamo la descrizione dell'authority
				String descrizione = "";
				for (int ik=0; ik < authorities.size(); ik++) {
					Tb_codici tb_codici = authorities.get(ik);
					if (tb_codici.getCd_tabella().startsWith(codice))
						descrizione = tb_codici.getDs_tabella();
				}
				GruppoParametriVO.setCodice(codice);
				GruppoParametriVO.setDescrizione(descrizione);
				List<String> opzioniRadio = new ArrayList<String>();
				opzioniRadio.add("Si'");
				opzioniRadio.add("No");
				ParametroVO parametro = new ParametroVO();

				char parChar = ((Tbf_par_auth)auth[i]).getTp_abil_auth();
				parametro.setDescrizione("profilo.polo.parametri.abil");
				parametro.setIndex("0");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				parChar = ((Tbf_par_auth)auth[i]).getFl_abil_legame();
				parametro.setDescrizione("profilo.polo.parametri.abil_legame");
				parametro.setIndex("1");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				parChar = ((Tbf_par_auth)auth[i]).getFl_leg_auth();
				parametro.setDescrizione("profilo.polo.parametri.leg_auth");
				parametro.setIndex("2");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				String par = ((Tbf_par_auth)auth[i]).getCd_livello();
				parametro.setDescrizione("profilo.polo.parametri.livello");
				parametro.setIndex("3");
				List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
				List<TB_CODICI> elencoCodici = null;
				try {
					elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
				} catch (Exception e){
					log.error("", e);
				}
				for (int k = 1; k < elencoCodici.size(); k++) {
					TB_CODICI tabCodice = elencoCodici.get(k);
					Integer cod1 = Integer.parseInt(tabCodice.getCd_tabella().trim()); //Codice della tabella CODICI
					Integer cod2 = dao.getLivelloParametro(idParametro); //Codice impostato per il parametro
					if (cod1 <= cod2) {
						ComboVO combo = new ComboVO();
						combo.setDescrizione(tabCodice.getCd_tabella() + " " + tabCodice.getDs_tabella());
						combo.setCodice(tabCodice.getCd_tabella());
						elencoCombo.add(combo);
					}
					if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
						parametro.setSelezione(tabCodice.getCd_tabella());
					}
				}
				parametro.setElencoScelte(elencoCombo);
				parametro.setTipo("MENU");
				elencoParametri.add(parametro);

//				parametro = new ParametroVO();
//				par = ((Tbf_par_auth)auth[i]).getCd_contr_sim();
//				parametro.setDescrizione("profilo.polo.parametri.contr_sim");
//				parametro.setIndex("4");
//				List<String> opzioniSimil = new ArrayList<String>();
//				opzioniSimil.add("000");
//				opzioniSimil.add("001");
//				opzioniSimil.add("002");
//				parametro.setRadioOptions(opzioniSimil);
//				if (par.trim().equals("0"))
//					parametro.setSelezione("000");
//				else if (par.trim().equals("1"))
//					parametro.setSelezione("001");
//				else if (par.trim().equals("2"))
//					parametro.setSelezione("002");
//				parametro.setTipo("RADIO");
//				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				parChar = ((Tbf_par_auth)auth[i]).getFl_abil_forzat();
				parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
				parametro.setIndex("4");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				parChar = ((Tbf_par_auth)auth[i]).getSololocale();
				parametro.setDescrizione("profilo.polo.parametri.sololocale");
				parametro.setIndex("5");
				parametro.setRadioOptions(opzioniRadio);
				if (codice.trim().equals("PP") || codice.trim().equals("TH")) {
					parametro.setSelezione("Si'");
					parametro.setCongelato("TRUE");
				}
				else {
					if (parChar == 'S')
						parametro.setSelezione("Si'");
					else if (parChar == 'N')
						parametro.setSelezione("No");
				}
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				GruppoParametriVO.setElencoParametri(elencoParametri);
				elencoAuth.add(GruppoParametriVO);
			}

			// Ordinamento degli authority e inserimento dell'indice di visualizzazione:
			Collections.sort(elencoAuth, new StringComparator());

			for (int g = 0; g < elencoAuth.size(); g++) {
				elencoAuth.get(g).setIndice(g + "");
			}

			output.add(elencoAuth);

			//Par_mat:
			for (int i = 0; i < parMat.length; i++) {
				GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
				List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
				char codice = ((Tbf_par_mat)parMat[i]).getCd_par_mat();
				// Troviamo la descrizione dei materiali
				String descrizione = "";
				for (int ik=0; ik < materiali.size(); ik++) {
					Tb_codici tb_codici = materiali.get(ik);
					if (tb_codici.getCd_tabella().charAt(0) == codice)
						descrizione = tb_codici.getDs_tabella();
				}
				GruppoParametriVO.setCodice(codice + "");
				GruppoParametriVO.setDescrizione(descrizione);
				List<String> opzioniRadio = new ArrayList<String>();
				opzioniRadio.add("Si'");
				opzioniRadio.add("No");
				ParametroVO parametro = new ParametroVO();

				char parChar = ((Tbf_par_mat)parMat[i]).getTp_abilitaz();
				parametro.setDescrizione("profilo.polo.parametri.abil");
				parametro.setIndex("0");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

//				parametro = new ParametroVO();
//				String par = ((Tbf_par_mat)parMat[i]).getCd_contr_sim();
//				parametro.setDescrizione("profilo.polo.parametri.contr_sim");
//				parametro.setIndex("1");
//				List<String> opzioniSimil = new ArrayList<String>();
//				opzioniSimil.add("000");
//				opzioniSimil.add("001");
//				opzioniSimil.add("002");
//				parametro.setRadioOptions(opzioniSimil);
//				if (par.trim().equals("0"))
//					parametro.setSelezione("000");
//				else if (par.trim().equals("1"))
//					parametro.setSelezione("001");
//				else if (par.trim().equals("2"))
//					parametro.setSelezione("002");
//				parametro.setTipo("RADIO");
//				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				parChar = ((Tbf_par_mat)parMat[i]).getFl_abil_forzat();
				parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
				parametro.setIndex("1");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				String par = ((Tbf_par_mat)parMat[i]).getCd_livello();
				parametro.setDescrizione("profilo.polo.parametri.livello");
				parametro.setIndex("2");
				List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
				List<TB_CODICI> elencoCodici = null;
				try {
					elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
				}
				catch (Exception e){
					log.error("", e);
				}
				for (int k = 1; k < elencoCodici.size(); k++) {
					TB_CODICI tabCodice = elencoCodici.get(k);
					Integer cod1 = Integer.parseInt(tabCodice.getCd_tabella().trim()); //Codice della tabella CODICI
					Integer cod2 = dao.getLivelloParametro(idParametro); //Codice impostato per il parametro
					if (cod1 <= cod2) {
						ComboVO combo = new ComboVO();
						combo.setDescrizione(tabCodice.getCd_tabella() + " " + tabCodice.getDs_tabella());
						combo.setCodice(tabCodice.getCd_tabella());
						elencoCombo.add(combo);
					}
					if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
						parametro.setSelezione(tabCodice.getCd_tabella());
					}
				}
				parametro.setElencoScelte(elencoCombo);
				parametro.setTipo("MENU");
				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				parChar = ((Tbf_par_mat)parMat[i]).getSololocale();
				parametro.setDescrizione("profilo.polo.parametri.sololocale");
				parametro.setIndex("3");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				GruppoParametriVO.setElencoParametri(elencoParametri);
				elencoParMat.add(GruppoParametriVO);

			}

			// Ordinamento dei materiali e inserimento dell'indice di visualizzazione:
			Collections.sort(elencoParMat, new StringComparator());

			for (int g = 0; g < elencoParMat.size(); g++) {
				elencoParMat.get(g).setIndice(g + "");
			}

			output.add(elencoParMat);

			//Semantica:
			for (int i = 0; i < semantica.length; i++) {
				GruppoParametriVO gp = new GruppoParametriVO();
				List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();

				String codice = "";
				String descrizione = "";
				List<String> opzioniRadio = new ArrayList<String>();
				opzioniRadio.add("Si'");
				opzioniRadio.add("No");
				ParametroVO parametro = new ParametroVO();

				if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SCLA")) {
					codice = ((Tbf_par_sem)semantica[i]).getTp_tabella_codici();
					descrizione = "profilo.polo.parametri.classificazioni";
				}
				else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SOGG")) {
					codice = ((Tbf_par_sem)semantica[i]).getTp_tabella_codici();
					descrizione = "profilo.polo.parametri.soggetti";
				}
				else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("STHE")) {
					codice = ((Tbf_par_sem)semantica[i]).getTp_tabella_codici();
					descrizione = "profilo.polo.parametri.thesauri";
				}
				gp.setCodice(codice);
				gp.setDescrizione(descrizione);
				String par = ((Tbf_par_sem)semantica[i]).getCd_tabella_codici();
				gp.setCd_tabella(par);

				if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SCLA"))
					parametro.setDescrizione("profilo.polo.parametri.classificazione");
				else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SOGG"))
					parametro.setDescrizione("profilo.polo.parametri.soggetto");
				else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("STHE"))
					parametro.setDescrizione("profilo.polo.parametri.thesauro");
				parametro.setIndex("0");
//				List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
				CodiciType cod = null;
				List<TB_CODICI> elencoCodici = null;
				try {
					cod = CodiciType.fromString(codice);
					elencoCodici = CodiciProvider.getCodici(cod);
				}
				catch (Exception e){
					log.error("", e);
				}
				for (int k = 1; k < elencoCodici.size(); k++) {
					TB_CODICI tabCodice = elencoCodici.get(k);
//					ComboVO combo = new ComboVO();
//					combo.setDescrizione(tabCodice.getDs_tabella());
//					combo.setCodice(tabCodice.getCd_tabella());
//					elencoCombo.add(combo);
					if (tabCodice.getCd_tabella().trim().equals(par.trim())) {
						parametro.setSelezione(tabCodice.getDs_tabella());
					}
				}
//				parametro.setElencoScelte(elencoCombo);
				parametro.setTipo("TESTO");
				elencoParametri.add(parametro);

				parametro = new ParametroVO();
				char parChar = ((Tbf_par_sem)semantica[i]).getSololocale();
				parametro.setDescrizione("profilo.polo.parametri.sololocale");
				parametro.setIndex("1");
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S')
					parametro.setSelezione("Si'");
				else if (parChar == 'N')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);

				gp.setElencoParametri(elencoParametri);
				elencoSem.add(gp);
			}

			// Ordinamento dei materiali e inserimento dell'indice di visualizzazione:
			Collections.sort(elencoSem, new StringComparator());

			for (int g = 0; g < elencoSem.size(); g++) {
				elencoSem.get(g).setIndice(g + "");
			}

			output.add(elencoSem);

			return output;

		}

		public boolean isUtenteRemoto(String codPolo, String codBib, InetAddress addr) throws EJBException {
			if (addr == null)
				return false;
			if (addr.isLoopbackAddress())	//utente da localhost
				return false;

			IntranetRangeDAO dao = new IntranetRangeDAO();
			try {
				List<Tbf_intranet_range> listaRange = dao.getListaRangePerBiblioteca(codPolo, codBib);
				if (!ValidazioneDati.isFilled(listaRange))
					return false;

				for (Tbf_intranet_range range : listaRange) {

					CIDR cidr = CIDR.generateCIDR(ValidazioneDati.trimOrEmpty(range.getAddress()) + "/" + range.getBitmask());
					log.debug("ip range: " + cidr.getPrefix() + " --> " + cidr.getEndAddress());

					BigInteger userAddr = ipToNumber(addr);
					BigInteger start    = ipToNumber(cidr.getPrefix());
					BigInteger end      = ipToNumber(cidr.getEndAddress());

					boolean contains = (userAddr.compareTo(start) >= 0 && userAddr.compareTo(end) <= 0);
					if (contains)
						return false;	// utente locale
				}

				//non trovato, é dichiarato come remoto
				return true;

			} catch (Exception e) {
				log.error("", e);
				throw new EJBException(e);
			}

		}

		private BigInteger ipToNumber(InetAddress addr) {
			BigInteger userAddr = BigInteger.ZERO;
			if (addr instanceof Inet4Address)	//IPv4
				userAddr = BigInteger.valueOf(ARegUtils.ipv4AddressToInt(addr));
			if (addr instanceof Inet6Address)	//IPv6
				userAddr = ARegUtils.ipv6AddressToBigInteger(addr);

			return userAddr;
		}

		public Map<String, Default> reloadDefault(int idUtenteProfessionale) throws EJBException {
			try {
			return SbnWebProfileCache.getInstance().reloadDefault(idUtenteProfessionale);

			} catch (DaoManagerException e) {
				log.error("", e);
				throw new EJBException(e);
			} catch (UtenteNotFoundException e) {
				log.error("", e);
				throw new EJBException(e);
			} catch (UtenteNotProfiledException e) {
				log.error("", e);
				throw new EJBException(e);
			}
		}

		public void resetRootPassword() throws DaoManagerException, EJBException {
			PasswordEncrypter crypt = new PasswordEncrypter(Constants.USER_ROOT);
			String pwd = crypt.encrypt(Constants.USER_ROOT);
			Tbf_utenti_professionali_webDao	dao = new Tbf_utenti_professionali_webDao();
			dao.resetRootPassword(pwd);
			SbnWebProfileCache.getInstance().clear(Arrays.asList(Constants.USER_ROOT));
		}

		public UserProfile getProfile(String ticket) throws DaoManagerException, UtenteNotFoundException, UtenteNotProfiledException {
			return SbnWebProfileCache.getInstance().getProfile(ticket);
		}

}