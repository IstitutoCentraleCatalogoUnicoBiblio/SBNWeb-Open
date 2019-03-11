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
package it.iccu.sbn.ejb.domain.servizi.serviziweb;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.ElencoDirittiUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.ListaServiziVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.extension.auth.utente.LoginResponse;
import it.iccu.sbn.extension.auth.utente.LoginRequest.LoginRequestType;
import it.iccu.sbn.extension.auth.utente.LoginResponse.LoginResult;
import it.iccu.sbn.persistence.dao.amministrazione.ContatoriDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.AutorizzazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.SegnatureDAO;
import it.iccu.sbn.persistence.dao.servizi.ServiziDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_documenti_lettoriDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_utentiDAO;
import it.iccu.sbn.persistence.dao.servizi.Trl_diritti_utenteDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_disponibilita_precatalogati;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_autorizzazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_diritti_utente;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.servizi.utenti.auth.UtenteLoginService;
import it.iccu.sbn.util.Constants;
import it.iccu.sbn.util.Constants.Servizi;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.web.exception.SbnBaseException;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="ServiziWeb" description="A session bean named ServiziWeb"
 *           display-name="ServiziWeb" jndi-name="sbnWeb/ServiziWeb"
 *           type="Stateless" transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class ServiziWebBean extends TicketChecker implements ServiziWeb {

	private static final long serialVersionUID = -641665204682003882L;
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	private static Logger log = Logger.getLogger(ServiziWeb.class);

	private AmministrazioneBibliotecario amministrazioneBibliotecario;
	private AmministrazionePolo amministrazionePolo;
	private Random rnd = new java.security.SecureRandom();



	public void ejbCreate() {
	}

	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {

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
	//LFV 13/07/2018
	private List<BibliotecaVO> prepareBiblioteche(Set biblioteche) {
		List<BibliotecaVO> listaBiblio = new ArrayList<BibliotecaVO>();
		Iterator<?> iterator = biblioteche.iterator();
		while (iterator.hasNext()) {
			Trl_utenti_biblioteca uteBib = (Trl_utenti_biblioteca) iterator.next();
			BibliotecaVO biblio = new BibliotecaVO();
			biblio.setCod_bib(uteBib.getCd_biblioteca().getCd_biblioteca());
			biblio.setNom_biblioteca(uteBib.getCd_biblioteca().getDs_biblioteca());
			biblio.setCod_polo(uteBib.getCd_biblioteca().getCd_polo().toString());
			biblio.setId_utenti_biblioteca(uteBib.getId_utenti_biblioteca());
			listaBiblio.add(biblio);
		}
		return listaBiblio;
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
	 */
	public UtenteWeb login(String ticket, String userId, String password,
			String sip2_ticket, InetAddress inetAddress) throws DaoManagerException,
			UtenteNotFoundException, EJBException {

		//LFV 13/07/2018
		LoginResponse loginResponse = doLoginService(ticket, userId, password);
		if (loginResponse.getStatus() == LoginResult.NOT_FOUND)
			throw new UtenteNotFoundException("Utente e/o password non validi");
		//Login OK
		UtenteBaseVO utenteVO = (UtenteBaseVO) loginResponse.getUtente();
		Tbl_utentiDAO utenteDao = new Tbl_utentiDAO();

		//Ottengo l'utente e le biblioteche
		Tbl_utenti utente = utenteDao.select(utenteVO.getIdUtente());
		Set<?> listaBibUte = utenteDao.listaBiblioUte(utente);
		List<BibliotecaVO> listaBiblio = prepareBiblioteche(listaBibUte);

		UtenteWeb uteWeb = ConversioneHibernateVO.toWeb().utente(utente);

		//Se la psw è da cambiare lo notifico alla jsp
		uteWeb.setChangePassword(loginResponse.getStatus() == LoginResult.AUTH_DATA_EXPIRED ? 'S' : 'N');

		uteWeb.setPassword(password);
		uteWeb.setListaBiblio(listaBiblio);

		//LFV 13/07/2018 SIP2 morto
		uteWeb.setSIP2(false);

		return uteWeb;
	}

	public UtenteWeb biblioDest() throws DaoManagerException, UtenteNotFoundException, RemoteException {
		BibliotecaVO biblio1 = new BibliotecaVO();
		biblio1.setCod_bib("1");
		biblio1.setNom_biblioteca("Biblioteca Destinataria 1 EJB");
		BibliotecaVO biblio2 = new BibliotecaVO();
		biblio2.setCod_bib("2");
		biblio2.setNom_biblioteca("Biblioteca Destinataria 2 EJB");
		List<BibliotecaVO> listaBiblio = new ArrayList<BibliotecaVO>();
		listaBiblio.add(biblio1);
		listaBiblio.add(biblio2);
		UtenteWeb utente = new UtenteWeb();
		utente.setListabibDest(listaBiblio);
		return utente;//qui va la chiamata a hibernate????
	}

	public DocumentoNonSbnVO ricercaPerSegnatura(String polo, String codBib, String segnatura)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {

		Tbl_documenti_lettoriDAO docDao = new Tbl_documenti_lettoriDAO();
		String ordSegn = OrdinamentoCollocazione2.normalizza(segnatura);
		Tbl_documenti_lettori doc = docDao.select(polo,codBib,ordSegn);
		if (doc == null)
			return null;

		List<Tbl_esemplare_documento_lettore> esemplari =
			docDao.getListaEsemplariDocumentoLettore(doc.getId_documenti_lettore());

		List<EsemplareDocumentoNonSbnVO> listaEsemplari = new ArrayList<EsemplareDocumentoNonSbnVO>();
		if (ValidazioneDati.isFilled(esemplari)) {
			int progr = 0;
			for (Tbl_esemplare_documento_lettore e : esemplari)
				listaEsemplari.add(ConversioneHibernateVO.toWeb().esemplareDocumentoNonSbn(++progr, e));
		}

		return ConversioneHibernateVO.toWeb().documentoNonSbn(doc, listaEsemplari);
	}

	public boolean inserisciNuovoDoc(DocumentoNonSbnVO documento)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {

		Tbl_documenti_lettoriDAO docDao = new Tbl_documenti_lettoriDAO();
		docDao.insert(documento);



		return true;//qui va la chiamata a hibernate????
	}

	public UtenteWeb tipoDoc()
     throws DaoManagerException, UtenteNotFoundException, RemoteException {

		DocumentoNonSbnVO tipoDocumento1 = new DocumentoNonSbnVO();
		DocumentoNonSbnVO tipoDocumento2 = new DocumentoNonSbnVO();

		List<DocumentoNonSbnVO> tipoDoc = new ArrayList<DocumentoNonSbnVO>();

		tipoDocumento1.setCod_tipo_doc("1");
		tipoDocumento1.setDesc_tipo_doc("TIPO 1 EJB");
		tipoDocumento2.setCod_tipo_doc("2");
		tipoDocumento2.setDesc_tipo_doc("TIPO 2 EJB");

		tipoDoc.add(tipoDocumento1);
		tipoDoc.add(tipoDocumento2);
		UtenteWeb utente = new UtenteWeb();
		utente.setListaTipoDoc(tipoDoc);
		return utente;//qui va la chiamata a hibernate????


	}
	public UtenteWeb modErog()
     throws DaoManagerException, UtenteNotFoundException, RemoteException {

		ListaServiziVO modErog1 = new ListaServiziVO();
		ListaServiziVO modErog2 = new ListaServiziVO();
		ListaServiziVO modErog3 = new ListaServiziVO();
		ListaServiziVO modErog4 = new ListaServiziVO();
		List<ListaServiziVO> modErog = new ArrayList<ListaServiziVO>();
		modErog1.setCod_mod_erog("1");
		modErog1.setDescr_mod_erog("spedizione pony express EJB");
		modErog2.setCod_mod_erog("2");
		modErog2.setDescr_mod_erog("spedizione tramite posta celere EJB");
		modErog3.setCod_mod_erog("3");
		modErog3.setDescr_mod_erog("consegna in sede EJB");
		modErog4.setCod_mod_erog("4");
		modErog4.setDescr_mod_erog("spedizione tramite posta celere EJB");

		modErog.add(modErog1);
		modErog.add(modErog2);
		modErog.add(modErog3);
		modErog.add(modErog4);
		UtenteWeb utente = new UtenteWeb();
		utente.setModErogazione(modErog);
		return utente;//qui va la chiamata a hibernate????


	}
	public UtenteWeb supporto()
     throws DaoManagerException, UtenteNotFoundException, RemoteException {

		ListaServiziVO supporto1 = new ListaServiziVO();
		ListaServiziVO supporto2 = new ListaServiziVO();
		List<ListaServiziVO> listaSupp = new ArrayList<ListaServiziVO>();
		supporto1.setCod_supporto("1");
		supporto1.setDescr_supporto("immagine a 256 dpi EJB");
		supporto2.setCod_supporto("2");
		supporto2.setDescr_supporto("immagine a 512 dpi EJB");
		listaSupp.add(supporto1);
		listaSupp.add(supporto2);
		UtenteWeb utente = new UtenteWeb();
		utente.setSupporto(listaSupp);
		return utente;//qui va la chiamata a hibernate????


	}

	public UtenteWeb listaServ(Integer idUte)
     throws DaoManagerException, UtenteNotFoundException, RemoteException {

		Trl_diritti_utenteDAO dirUteDAO = new Trl_diritti_utenteDAO();
		List<ListaServiziVO> dirittiUtente = dirUteDAO.select(idUte);

		UtenteWeb utente = new UtenteWeb();
		utente.setListaServ(dirittiUtente);
		return utente;//qui va la chiamata a hibernate????


	}
	public UtenteWeb listaServILL()
     throws DaoManagerException, UtenteNotFoundException, RemoteException {

		List<ListaServiziVO> listaServizi = new ArrayList<ListaServiziVO>();
		ListaServiziVO serv1 = new ListaServiziVO();
		ListaServiziVO serv2 = new ListaServiziVO();
		serv1.setServizio("Prestito Interbibliotecario");
		listaServizi.add(serv1);
		serv2.setServizio("Prestito Interbibliotecario per Utenti");
		listaServizi.add(serv2);

		UtenteWeb utente = new UtenteWeb();
		utente.setListaServILL(listaServizi);
		return utente;//qui va la chiamata a hibernate????


	}

	public List dirittiUtente(Integer idUtente)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {


		UtentiDAO dirittiUtenteDao = new UtentiDAO();
		List dirittiUte = dirittiUtenteDao.getListaDirittiUtente(idUtente);
		List dirittiUtente = new ArrayList();
		Iterator iterator = dirittiUte.iterator();
		while (iterator.hasNext()) {
			Trl_diritti_utente dirUte = (Trl_diritti_utente)iterator.next();
			ElencoDirittiUtenteVO diritti = new ElencoDirittiUtenteVO();
			diritti.setIdServizio(dirUte.getId_servizio().getId_servizio());
			diritti.setServizi(dirUte.getId_servizio().getDescr());

			diritti.setScadenza(DateUtil.formattaData(dirUte.getData_fine_serv()));
			diritti.setSospesoDal(DateUtil.formattaData(dirUte.getData_inizio_sosp()));
			diritti.setSospesoAl(DateUtil.formattaData(dirUte.getData_fine_sosp()));
			dirittiUtente.add(diritti);
		}

		return dirittiUtente;//qui va la chiamata a hibernate????


	}

	public List dirittiDocumento(DocumentoNonSbnVO datiDocumento)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {


		SegnatureDAO dirittiDocumentoDao = new SegnatureDAO();
		Tbl_disponibilita_precatalogati dispPrecat = new Tbl_disponibilita_precatalogati();
		String segnNorm = OrdinamentoCollocazione2.normalizza(datiDocumento.getSegnatura());
		dispPrecat.setSegn_da(segnNorm);
		List dirittiDoc = dirittiDocumentoDao.select(datiDocumento.getCodPolo(),datiDocumento.getCodBib(),dispPrecat,null);
		List dirittiDocu = new ArrayList();
		Iterator iterator = dirittiDoc.iterator();
		RangeSegnatureVO diritti=null;
		while (iterator.hasNext()) {
			Tbl_disponibilita_precatalogati catFrui = (Tbl_disponibilita_precatalogati)iterator.next();
			diritti = new RangeSegnatureVO(catFrui.getId_disponibilita_precatalogati(),catFrui.getCod_segn(),catFrui.getSegn_inizio(),catFrui.getSegn_fine(),catFrui.getSegn_da(),catFrui.getSegn_a(),catFrui.getCod_frui(),catFrui.getCod_no_disp());

		}
		ServiziDAO dao = new ServiziDAO();
		dao.getServiziAttivi(datiDocumento.getCodPolo()," "+datiDocumento.getCodBib(),diritti.getCodFruizione());
		return dirittiDocu;//qui va la chiamata a hibernate????

	}
	// almaviva 2009
	public boolean cambioPwd(String ticket, String userId, String pwdNew) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException{
		//cifro la password;
		PasswordEncrypter crypt = new PasswordEncrypter(pwdNew);
		String encryptPwd = crypt.encrypt(pwdNew);
		//
		UtentiDAO dao  = new UtentiDAO();
		Tbl_utenti utente;
		try {
			utente = dao.getUtente(userId);
			if (utente == null) {
				return false;
			} else{
				//aggiorno il campo "password" contestualmente procedo
				//ad aggiornare anche i campi flag password "change_password" e
				//ultima variazione della password "Ts_var_password"
				utente.setPassword(encryptPwd);
				utente.setChange_password('N');
				utente.setTs_var_password(DaoManager.now());
				utente.setUte_var(DaoManager.getFirmaUtente(ticket));
				dao.aggiornaUtente(utente);
				return true;
			}
		} catch (DaoManagerException e) {

			log.error("", e);
			return false;
		}

	}

	// almaviva 2009
	public boolean Logout(String userId, String password) throws RemoteException,UtenteNotFoundException,DefaultNotFoundException{

		UtentiDAO dao  = new UtentiDAO();
		Tbl_utenti utente;
		try {
			//seleziono utente
			utente = dao.getUtente(userId);
			if (utente== null ){
				return false;
			} else{
				//disconnessione utente
				return true;
			}
		} catch (DaoManagerException e) {

			log.error("", e);
			return false;
		}


	}
	// almaviva 2009
	public UtenteWeb recuperoPassword(String username) throws RemoteException,
			UtenteNotFoundException, DefaultNotFoundException {

		UtenteWeb uteWeb = null;
		UtentiDAO dao  = new UtentiDAO();
		Tbl_utenti utente;

		try {
			//seleziono l'utente
			utente = dao.getUtente(username);

			if (utente == null)
				utente = dao.getUtente(ServiziUtil.espandiCodUtente(username));

			//almaviva5_20170704 #6438
			if (utente == null)
				utente = dao.getUtenteByCodFiscale(username);

			if (utente == null)
				return uteWeb;

			uteWeb = ConversioneHibernateVO.toWeb().utente(utente);

			if (!ValidazioneDati.equals(uteWeb.getTipoUtente(), Servizi.Utenti.UTENTE_TIPO_SBNWEB) ) {
				//check operazione non supportata
				return uteWeb;
			}

			//valorizzo vo
			dao.setSessionCurrentCfg();

			//almaviva5_20110120
			int gg_max_ute = ContatoriDAO.getDisattivazioneUtenteLettore(uteWeb.getCodPolo() );	//gg scadenza utente
			//inizio controllo scadenza userid
			Timestamp now = DaoManager.now();
			Timestamp lastAccess = utente.getLast_access();

			lastAccess = gg_max_ute > 0 ? DateUtil.addDay(lastAccess, gg_max_ute) : now;

			if (now.after(lastAccess) ) {
				// se l'utenza non è scaduta si procede all' aggiornamento
				// del campo last_access
				uteWeb.setScaduto(true);
				return uteWeb;
			}

			//almaviva5_20110118 #4152
			uteWeb.setPostaElettronica(utente.getInd_posta_elettr());
			uteWeb.setPostaElettronica2(utente.getInd_posta_elettr2());
			String mail = ConversioneHibernateVO.toWeb().getEmailUtente(utente);
			if (!ValidazioneDati.isFilled(mail))
				return uteWeb;

			String cod_fiscale = utente.getCod_fiscale();
			uteWeb.setCodFiscale(cod_fiscale);
			uteWeb.setIdUtente(utente.getId_utenti());
			utente.setChange_password('S');

			//almaviva5_20101005 #3921
			String tmpPwd = ValidazioneDati.trunc(ValidazioneDati.isFilled(cod_fiscale) ?
				ValidazioneDati.trimOrEmpty(rnd.nextInt(89) + 10 + cod_fiscale) + IdGenerator.getId() :
				PasswordEncrypter.randomPassword(),
				Constants.Servizi.Utenti.MAX_PASSWORD_LENGTH);
			PasswordEncrypter crypt = new PasswordEncrypter(tmpPwd);
			String encryptPwd = crypt.encrypt(tmpPwd);

			utente.setPassword(encryptPwd);
			utente.setChange_password('S');
			//almaviva5_20101028 #3921
			uteWeb.setPassword(tmpPwd);

			//almaviva5_20160315
			uteWeb.setNome(utente.getNome());
			uteWeb.setCognome(utente.getCognome());

			return uteWeb;

		} catch (DaoManagerException e) {
			log.error("", e);
			return null;
		} catch (Exception e) {
			log.error("", e);
			throw new EJBException(e);
		}

	}
	public List<BibliotecaVO> getListaBibAutoregistrazione(String cdpolo)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {
		List<BibliotecaVO> listaBiblioteche = new ArrayList<BibliotecaVO>();

		UtentiDAO dao = new UtentiDAO();
		List listabib = dao.getListaBibAutoregistrazione(cdpolo);

		Iterator<?> iterator = listabib.iterator();
		while (iterator.hasNext()) {
			Object[] biblio = (Object[])iterator.next();
			BibliotecaVO b = new BibliotecaVO();
			b.setCod_bib((String) biblio[0]);
			b.setNom_biblioteca((String) biblio[1]);
			listaBiblioteche.add(b);
		}

		return listaBiblioteche;//qui va la chiamata a hibernate????
	}

	// almaviva 2009
	public List listaBiblioIscritto(String cdpolo, Integer idutente)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {
		List listaBilbioteche = new ArrayList();

		UtentiDAO bib = new UtentiDAO();
		List listabib = bib.listaBiblioIscritto(cdpolo,idutente);

		Iterator iterator = listabib.iterator();
		while (iterator.hasNext()) {
			Object[] biblio = (Object[])iterator.next();
			BibliotecaVO b = new BibliotecaVO();
			b.setCod_polo(cdpolo);
			b.setCod_bib((String) biblio[0]);
			b.setNom_biblioteca((String) biblio[1]);
			b.setId_utenti_biblioteca((Integer) biblio[2]);
			listaBilbioteche.add(b);
		}

		return listaBilbioteche;//qui va la chiamata a hibernate????
	}

	// almaviva 2009
	public List listaBiblioAuto(String cdpolo, Integer idutente, List cdBib)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {
		List output = new ArrayList();

		UtentiDAO bib = new UtentiDAO();
		List listabib = bib.listaBiblioAuto(cdpolo,idutente,cdBib);

		Iterator iterator = listabib.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			Object[] biblio = (Object[])iterator.next();
			BibliotecaVO b = new BibliotecaVO();
			b.setCod_polo(cdpolo);
			b.setCod_bib((String) biblio[0]);
			b.setNom_biblioteca((String) biblio[1]);
			b.setId_utenti_biblioteca((Integer) biblio[2]);
			b.setPrg(count++);
			output.add(b);
		}

		return output;//qui va la chiamata a hibernate????
	}


	// almaviva 2009
	public List listaBiblioNonIscr(String cdpolo, Integer idutente, List cdBib)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {
		List listaBilbioteche = new ArrayList();

		UtentiDAO bib = new UtentiDAO();
		List listabib = bib.listaBiblioNonIscr(cdpolo,idutente,cdBib);
		int count = 0;
		Iterator iterator = listabib.iterator();
		while (iterator.hasNext()) {
			Object[] biblio = (Object[])iterator.next();
			BibliotecaVO b = new BibliotecaVO();
			b.setCod_bib((String) biblio[0]);
			b.setNom_biblioteca((String) biblio[1]);
			//b.setId_utenti_biblioteca((Integer) biblio[2]);
			b.setPrg(count++);
			listaBilbioteche.add(b);
		}

		return listaBilbioteche;//qui va la chiamata a hibernate????
	}


	// almaviva 2009
	public boolean inserimentoUtenteWeb(UtenteBibliotecaVO utente)
	    throws DaoManagerException, UtenteNotFoundException, RemoteException {

		//inserimento utente lettore lato web
		DaoManager insUte = new UtentiDAO();


		return true;//qui va la chiamata a hibernate????
	}

	// almaviva 2009
	public boolean updateUtentiBib(Integer idUte) throws RemoteException,
		UtenteNotFoundException, DefaultNotFoundException {

		UtentiDAO dao = new UtentiDAO();

		try {
			//seleziono l'utente
			Trl_utenti_biblioteca uteBib = dao.getUtenteBibliotecaById(idUte);

			if (uteBib == null)
				return false;
			else {
				//valorizzo vo
				if (uteBib.getFl_canc() == 'S') {
					uteBib.setFl_canc('N');
					return dao.updateUtentiBib(uteBib);
				} else
					return false;

			}
		} catch (DaoManagerException e) {
			log.error("", e);
			return false;
		}

	}

	// almaviva 2009
	//public UtenteWeb esistenzaUtenteWeb(String codfiscale)
	public UtenteWeb esistenzaUtenteWeb(String codfiscale, String mail)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {
		//
		UtenteWeb ute = null;
		UtentiDAO dao  = new UtentiDAO();
		Tbl_utenti utente = null;
		//
		try {
			//seleziono l'utente
			utente = dao.esistenzaUtenteWeb(codfiscale, mail);

			if (utente == null ) {
				//almaviva5_20120924 #5117 controllo su utente cancellato
				List<Tbl_utenti> utenti = dao.ricercaUtenteBasePolo(0, null, codfiscale, null, null, null, null);
				utente = ValidazioneDati.first(utenti);
			}

			if (utente == null )
				return null;
			else {//se risulta già iscritto valorizzo vo
				return ConversioneHibernateVO.toWeb().utente(utente);
			}
		} catch (DaoManagerException e) {

			log.error("", e);
			return ute;
		}

	}

	private List preparaListaServizi(List elencoAnagServizio,
			UtenteBibliotecaVO utente) {
		if (elencoAnagServizio == null && elencoAnagServizio.size() == 0)
			return null;
		int index = 0;
		List elencoServizi = new ArrayList();
		while (index < elencoAnagServizio.size()) {
			ElementoSinteticaServizioVO serAna = (ElementoSinteticaServizioVO) elencoAnagServizio
					.get(index);
			ServizioVO serLst = new ServizioVO();
			serLst.setCodPolo(utente.getCodPoloSer());
			serLst.setCodBib(utente.getCodBibSer());
			serLst.setCodPoloUte(utente.getCodPolo());
			serLst.setCodBibUte(utente.getCodiceBiblioteca());
			serLst.setCodUte(utente.getCodiceUtente());
			if (utente.getIdUtente() != null)
				serLst.setIdUtente(Integer.valueOf(utente.getIdUtente()));
			else
				serLst.setIdUtente(0);
			serLst.setCodice(serAna.getTipServizio());
			serLst.setServizio(serAna.getCodServizio());
			serLst.setDescrizione(serAna.getDesServizio());
			serLst.setStato(ServizioVO.NEW);
			serLst.setCancella("");
			serLst.setProgressivo(serAna.getProgressivo());
			serLst.setIdServizio(serAna.getIdServizio());
			serLst.setAutorizzazione(serAna.getCodAut());
			elencoServizi.add(serLst);
			index++;
		}
		return elencoServizi;
	}

	public List getListaServiziAutorizzazione(String ticket, UtenteBibliotecaVO utente) throws EJBException {
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		try {
			List serviziAut = autDAO.getListaServiziAutorizzazione(utente.getCodPolo(),
					utente.getCodBibSer(), '*');

			List listaOutput = new ArrayList();
			for (int i = 0; i < serviziAut.size(); i++) {

				ElementoSinteticaServizioVO servizioVO = ServiziConversioneVO
						.daHibernateAWebServizioAutorizzazione(
								(Trl_autorizzazioni_servizi) serviziAut.get(i),
								i + 1);
				listaOutput.add(servizioVO);
			}

			return preparaListaServizi(listaOutput, utente);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	// almaviva 2009
	public List<BibliotecaVO> controlloBibRicOpac(String cdpolo, Integer idutente, String cdBib)
    throws DaoManagerException, UtenteNotFoundException, RemoteException {
		List<BibliotecaVO> listaBiblioteche = new ArrayList<BibliotecaVO>();

		UtentiDAO dao = new UtentiDAO();
		List<?> listabib = dao.controlloBibRicOpac(cdpolo,idutente,cdBib);

		Iterator<?> iterator = listabib.iterator();
		while (iterator.hasNext()) {
			Object[] biblio = (Object[])iterator.next();
			//Integer idute = (Integer)iterator.next();
			BibliotecaVO b = new BibliotecaVO();
			//b.setId_utenti_biblioteca((Integer) idute);
			b.setId_utenti_biblioteca((Integer) biblio[0]);
			b.setCod_bib((String) biblio[1]);
			b.setNom_biblioteca((String) biblio[2]);

			listaBiblioteche.add(b);
		}


		return listaBiblioteche;//qui va la chiamata a hibernate????
	}

	// almaviva 2009;
	public int getLimMax( String polo ) throws DaoManagerException, UtenteNotFoundException, RemoteException {
		try {
			int gg_max_pwd = ContatoriDAO.getDisattivazioneUtenteLettore(polo);
			return gg_max_pwd;
		} catch (Exception e) {
			throw new RemoteException("", e);
		}
	}

	private AmministrazioneBibliotecario getAmministrazioneBibliotecario() throws Exception {

		if (amministrazioneBibliotecario != null)
			return amministrazioneBibliotecario;

		this.amministrazioneBibliotecario = DomainEJBFactory.getInstance().getBibliotecario();

		return amministrazioneBibliotecario;
	}

	// almaviva 2009
	public boolean setRemote(UtenteWeb utente, String polo, String cdbib, InetAddress inetAddress) throws DaoManagerException, UtenteNotFoundException, EJBException {

		//setto flgRemote
		boolean rit;

		try {
			rit = getAmministrazioneBibliotecario().isUtenteRemoto(polo, cdbib, inetAddress);
		} catch (Exception e) {
			rit = false;
			throw new EJBException(e);
		}
		//
		return rit;
	}

	// almaviva 2009;
	public int contaRangeSegnature(String polo, String codBib) throws DaoManagerException, UtenteNotFoundException, RemoteException {
		int range;
		SegnatureDAO dao  = new SegnatureDAO();
		//
		try {
			//selezione Range Segnatura
			range = dao.contaRangeSegnature(polo, codBib);
			//ritorna un Range Segnatura per la bib selezionata
			return range;

		} catch (DaoManagerException e) {
			log.error("", e);
			return 0;
		}
	}

	private LoginResponse doLoginService(String ticket, String userId, String password) {
		try {
			LoginResponse response = UtenteLoginService.login(ticket, userId, password, LoginRequestType.LOGIN);
			log.debug("login(): " + response.toString());
			return response;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

	}
	private void doLogoutService(String ticket, String userId) {
		try {
			UtenteLoginService.logout(ticket, userId);
			log.debug("logout() ");

		} catch (SbnBaseException e) {
			log.error("", e);
		}
	}

}
