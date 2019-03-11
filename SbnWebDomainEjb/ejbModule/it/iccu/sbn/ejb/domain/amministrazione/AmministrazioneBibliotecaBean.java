/**
 *
 */
package it.iccu.sbn.ejb.domain.amministrazione;

import it.iccu.sbn.batch.unimarc.SbnUnimarcBIDExtractor;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.custom.BibliotecaSearch;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.CheckVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBibliotecheVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecaDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_poloDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.ServiziIllDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_gruppo_attivita_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_biblioteca_ill;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.amministrazione.ProfilazioneUtil;
import it.iccu.sbn.util.amministrazione.ProfilazioneUtil.Origine;
import it.iccu.sbn.util.profiler.SbnWebProfileCache;
import it.iccu.sbn.vo.custom.Biblioteca;
import it.iccu.sbn.vo.custom.amministrazione.MergedParAut;
import it.iccu.sbn.vo.custom.amministrazione.MergedParMat;
import it.iccu.sbn.vo.custom.amministrazione.OrderedTreeElement;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;


/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition -->
 * @ejb.bean name="AmministrazioneBiblioteca"
 *           description="A session bean named AmministrazioneBiblioteca"
 *           display-name="AmministrazioneBiblioteca"
 *           jndi-name="sbnWeb/AmministrazioneBiblioteca"
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

public abstract class AmministrazioneBibliotecaBean extends AmministrazioneBaseBean implements AmministrazioneBiblioteca {

	private static final long serialVersionUID = 5983426042968528695L;
	private static Logger log = Logger.getLogger(AmministrazioneBiblioteca.class);

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
		log.info("creato ejb");
		return;
	}

	private SessionContext ctx;


	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
		return;
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
	public void createBiblioteca(String cd_polo,Biblioteca biblioteca) throws DaoManagerException {
		Tbf_biblioteca_in_poloDao bibliotecaDao = new Tbf_biblioteca_in_poloDao();
		Tbf_biblioteca_in_polo tbf_biblioteca_in_polo = bibliotecaDao.select(cd_polo,biblioteca.getCd_biblioteca());
		if(tbf_biblioteca_in_polo!= null)
		{
			return;
		}
		Tbf_poloDao poloDao = new Tbf_poloDao();
		Tbf_polo tb_polo = poloDao.select(cd_polo);

		Tbf_biblioteca_in_polo biblioteca_polo = new Tbf_biblioteca_in_polo();
		biblioteca_polo.setCd_biblioteca(biblioteca.getCd_biblioteca());
		biblioteca_polo.setCd_polo(tb_polo);
		biblioteca_polo.setFl_canc('S');
		biblioteca_polo.setUte_var(biblioteca.getUte_var());
		biblioteca_polo.setTs_var(DaoManager.now());
		biblioteca_polo.setUte_ins(biblioteca.getUte_var());
		biblioteca_polo.setTs_ins(biblioteca_polo.getTs_var());


		/**TODO
		 * MANCA GESTIONE ANAGRAFICA BIBLIOTECA
		*/

		bibliotecaDao.save(biblioteca_polo);
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
	public void updateBiblioteca(String cd_polo,Biblioteca biblioteca) throws DaoManagerException {
		Tbf_biblioteca_in_poloDao bibliotecaDao = new Tbf_biblioteca_in_poloDao();

		Tbf_biblioteca_in_polo biblioteca_polo = bibliotecaDao.select(cd_polo,biblioteca.getCd_biblioteca());
		if(biblioteca_polo == null)
		{
			return;
		}
		biblioteca_polo.setUte_var(biblioteca.getUte_var());
		biblioteca_polo.setTs_var(DaoManager.now());


		/**TODO
		 * MANCA GESTIONE ANAGRAFICA BIBLIOTECA
		*/

		bibliotecaDao.update(biblioteca_polo);
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
	public void updateBiblioteca(BibliotecaVO bibliotecaVO)
	throws ApplicationException {
		Tbf_bibliotecaDao bibliotecaDao = new Tbf_bibliotecaDao();

		Tbf_biblioteca biblioteca = ConversioneHibernateVO.toHibernate().anagraficaBiblioteca(null, bibliotecaVO);

		try {
			bibliotecaDao.update(biblioteca);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}



	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public void getAttivitaBibliotecario(int id_bibliotecario) throws DaoManagerException
	{
		//Trf_attivita_bibliotecariDao dao = new Trf_attivita_bibliotecariDao();
		//dao.select(id_bibliotecario);
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
	public SbnUserType getUserSbnMarc(String ticket)throws DaoManagerException
	{
		SbnUserType sbnusertype = new SbnUserType();
		sbnusertype.setUserId("000181");
		sbnusertype.setBiblioteca("CSW FI");
		return sbnusertype;
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
	public List getAllBiblioteche(String cd_polo) throws DaoManagerException
	{
		List ret = new ArrayList();
		Tbf_biblioteca_in_poloDao bibliotecaDao = new Tbf_biblioteca_in_poloDao();
		List biblioteche = bibliotecaDao.selectAll();
		for(int index=0; index<biblioteche.size(); index++)
		{
			Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo) biblioteche.get(index);
			if (!bib.getCd_polo().getCd_polo().equals(cd_polo)) continue;

			ComboCodDescVO biblio = new ComboCodDescVO();
			biblio.setCodice(bib.getCd_biblioteca());
			biblio.setDescrizione(bib.getId_biblioteca().getNom_biblioteca());
			ret.add(biblio);
		}

		return ret;
	}

	public List<BibliotecaVO> getListaBibliotecheAffiliatePerAttivita(String codPolo, String codBib, String codAttivita,
		boolean mostraBibCorrente) throws DaoManagerException
	{
		List<BibliotecaVO> output = new ArrayList<BibliotecaVO>();
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			if (codBib.equals(Constants.ROOT_BIB))
				codAttivita = null;
			List<Tbf_biblioteca> elenco = dao.getBibliotecheAffiliatePerAttivita(codPolo, codBib, codAttivita);

			// Ho trovato almeno una biblioteca affiliata. Devo includere la biblioteca chiamante.
			if (mostraBibCorrente && !codBib.equals(Constants.ROOT_BIB) || ValidazioneDati.isFilled(elenco) && !codBib.equals(Constants.ROOT_BIB))  {
				Tbf_biblioteca filtri = new Tbf_biblioteca();
				filtri.setCd_polo(codPolo);
				filtri.setCd_bib(codBib);
				List<Tbf_biblioteca> bibChiamante = dao.select(filtri);
				if (ValidazioneDati.isFilled(bibChiamante) )
					elenco.addAll(0, bibChiamante);
			}

			int prg = 0;
			for (Tbf_biblioteca bibDB : elenco) {
				BibliotecaVO bib = new BibliotecaVO();
				bib.setPrg(++prg);
				bib.setCod_polo(codPolo);
				bib.setCod_bib(bibDB.getCd_bib());
				bib.setNom_biblioteca(bibDB.getNom_biblioteca());
				output.add(bib);
			}
			return output;
    	}
		catch (DaoManagerException e){
			log.error("", e);
			return null;
		}

	}

	public List<BibliotecaVO> getListaBibliotecheSistemaMetropolitano(
			String codPolo, String codBib) throws DaoManagerException {

		String cdSistema = null;
		List<BibliotecaVO> output = new ArrayList<BibliotecaVO>();
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			Tbf_biblioteca bib = dao.getBiblioteca(codPolo, codBib);
			if (bib == null)
				return output;

			//controllo che sia registrata in polo
			Set<?> bibPolo = bib.getTbf_biblioteca_in_polo();
			if (ValidazioneDati.isFilled(bibPolo)) {
				Tbf_biblioteca_in_polo b = (Tbf_biblioteca_in_polo) bibPolo.iterator().next();
				cdSistema = b.getCd_sistema_metropolitano();
			}

			//Devo restituire comunque la bib chiamante
			BibliotecaVO bibVO = new BibliotecaVO();
			bibVO.setPrg(1);
			bibVO.setCod_polo(bib.getCd_polo());
			bibVO.setCod_bib(bib.getCd_bib());
			bibVO.setNom_biblioteca(bib.getNom_biblioteca());
			bibVO.setCodSistemaMetropolitano(cdSistema);
			output.add(bibVO);

			if (!ValidazioneDati.isFilled(cdSistema))
				return output;

			List<Tbf_biblioteca> elenco = dao.getListaBibliotecheSistemaMetropolitano(codPolo, codBib, cdSistema);

			int prg = 1;
			for (Tbf_biblioteca bibDB : elenco) {
				BibliotecaVO bibOut = new BibliotecaVO();
				bibOut.setPrg(++prg);
				bibOut.setCod_bib(bibDB.getCd_bib());
				bibOut.setCod_polo(bibDB.getCd_polo());
				bibOut.setCodSistemaMetropolitano(cdSistema);
				bibOut.setNom_biblioteca(bibDB.getNom_biblioteca());
				output.add(bibOut);
			}
			return output;

		} catch (DaoManagerException e) {
			log.error("", e);
			return null;
		}

	}


	public List<BibliotecaVO> getListaBibliotechePolo(String codPolo) throws DaoManagerException {
		List<BibliotecaVO> output = new ArrayList<BibliotecaVO>();
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			List<Tbf_biblioteca> biblioteche = dao.getListaBibliotechePolo(codPolo);

			int prg = 0;
			for (Tbf_biblioteca bibDB : biblioteche) {
				BibliotecaVO bib = ConversioneHibernateVO.toWeb().anagraficaBiblioteca(bibDB);
				bib.setPrg(++prg);

				output.add(bib);
			}
			return output;
    	}
		catch (DaoManagerException e){
			log.error("", e);
			return null;
		}

	}

	public List<BibliotecaVO> getListaBibliotecheAteneoInPolo(String codPolo, String ateneo) throws DaoManagerException {
		List<BibliotecaVO> output = new ArrayList<BibliotecaVO>();
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			List<Tbf_biblioteca> elenco = dao.getListaBibliotecheAteneoInPolo(codPolo, ateneo);

			int prg = 0;
			for (Tbf_biblioteca bibDB : elenco) {
				BibliotecaVO bib = ConversioneHibernateVO.toWeb().anagraficaBiblioteca(bibDB);
				bib.setPrg(++prg);

				output.add(bib);
			}
			return output;
    	}
		catch (DaoManagerException e){
			log.error("", e);
			return null;
		}

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
	 public List getBiblioteche(BibliotecaSearch search)
			throws DaoManagerException {

		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		Tbf_biblioteca bib = new Tbf_biblioteca();

		bib.setCd_bib(search.getCodBib());
		bib.setCd_polo(search.getCodPolo());
		if (!ValidazioneDati.strIsNull(search.getTipoBib()))
			bib.setTipo_biblioteca(search.getTipoBib().charAt(0));
		bib.setChiave_ente(search.getEnteApp());
		bib.setNom_biblioteca(search.getNomeBib());
		bib.setProvincia(search.getProvincia());
		bib.setPaese(search.getPaese());

		List<Tbf_biblioteca> listaBib = dao.select(bib);
		List<BibliotecaVO> result = new ArrayList<BibliotecaVO>();
		int size = ValidazioneDati.size(listaBib);
		for (int b = 0; b < size; b++) {
			Tbf_biblioteca bibTrovata = listaBib.get(b);
			result.add(ConversioneHibernateVO.toWeb().anagraficaBiblioteca(bibTrovata));
		}
		return result;
	}

	 public BibliotecaVO getBiblioteca(String codPolo, String codBib) throws DaoManagerException {
		 Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		 Tbf_biblioteca bib = dao.getBiblioteca(codPolo, codBib);
		 if (bib == null)
			 return null;
		 return ConversioneHibernateVO.toWeb().anagraficaBiblioteca(bib);
	 }


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
	public List getComboBibliotecheSBN() throws DaoManagerException
	{
		List ret = new ArrayList();
		Tbf_bibliotecaDao biblioteca = new Tbf_bibliotecaDao();
		List biblioteche = biblioteca.selectAll();
		int size = ValidazioneDati.size(biblioteche);
		for (int index = 0; index < size; index++)
		{
			Tbf_biblioteca bib = (Tbf_biblioteca) biblioteche.get(index);

			ComboCodDescVO biblio = new ComboCodDescVO();
			biblio.setCodice(bib.getCd_ana_biblioteca());
			biblio.setDescrizione(bib.getNom_biblioteca());
			ret.add(biblio);
		}

		return ret;
	}

	public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List parametri) throws RemoteException, ApplicationException, DaoManagerException,
			DataException, ValidationException {

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		stampeOnline.setParametriInput(parametri);
		switch (tipoStampa) {
		case STAMPA_LISTA_BIBLIOTECHE:
			stampeOnline.setRigheDatiDB(stampaListaBiblioteche(ticket, parametri));
			break;
		default:
			throw new ValidationException("Tipo stampa non previsto");
		}

		return stampeOnline;
	}

	public List stampaListaBiblioteche(String ticket, List parametri)
	throws ResourceNotFoundException, ApplicationException, DaoManagerException, RemoteException {

	if (ValidazioneDati.isFilled(parametri) && ticket != null) {
		StampaBibliotecheVO sbvo = (StampaBibliotecheVO)parametri.get(0);
		String polo = sbvo.getPolo();
//		String biblio = sbvo.getCodiceBiblioteca();
		String nomeBiblio = sbvo.getNomeBiblioteca();
		String biblio = sbvo.getTipoBiblioteca();
		String enteApp = sbvo.getEnteDiAppartenenza();
		String paese = sbvo.getPaese();
		String prov = sbvo.getProvincia();

		BibliotecaSearch bib = new BibliotecaSearch();
		bib.setCodBib(biblio);
		bib.setNomeBib(nomeBiblio);
		bib.setCodPolo(polo);
//		bib.setTipoBib(tipoBiblio);
		bib.setEnteApp(enteApp);
		bib.setPaese(paese);
		bib.setProvincia(prov);

		List listaBiblioteche = new ArrayList();

		listaBiblioteche = this.getBiblioteche(bib);
		return listaBiblioteche;
	} else {
		throw new ApplicationException("Errore nei parametri di Stampa Lista Biblioteche");
		}
	}

	public OrderedTreeElement getElencoAttivita(java.lang.String ticket)
	throws DaoManagerException, RemoteException {
		List attivita = null;
		List parametri = null;
		List authorities = null;
		String codiceAttivita, descrizioneAttivita, idAttivita, codiceAttivitaParent;

		//AmministrazionePoloDao dao = new AmministrazionePoloDao();
		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		OrderedTreeElement ote = new OrderedTreeElement("rootNode", "");

		try {

			attivita = dao.loadCodiciAttivitaPolo();

			// Troviamo le attivita di primo livello
			int size = ValidazioneDati.size(attivita);
			for (int index = 0; index < size; index++) {
				Tbf_attivita tbf_attivita = (Tbf_attivita) attivita.get(index);

				codiceAttivita = tbf_attivita.getCd_attivita();
				descrizioneAttivita = tbf_attivita.getId_attivita_sbnmarc().getDs_attivita();
				codiceAttivitaParent = tbf_attivita.getCd_funzione_parent();
				if (codiceAttivitaParent == null) {
					 //ote.addElement(new String(descrizioneAttivita), new String(codiceAttivita + ": " + descrizioneAttivita));
					ote.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK",  new String(codiceAttivita), tbf_attivita.getPrg_ordimanento() + "");
				}
			}

//			if (ote.getElements().size() > 0)
//				ote.sortByValue();

			// Per ogni attivita di primo livello troviamo figli (niente nipoti per le attivita')
			int size2 = ValidazioneDati.size(ote.getElements());
			for (int j=0; j< size2; j++)
			{
				OrderedTreeElement ote1 = ote.getElements().get(j);

				OrderedTreeElement oteChildren = new OrderedTreeElement("node"+Integer.toString(j), "");

				for (int index = 0; index < size; index++) {
					Tbf_attivita tbf_attivita = (Tbf_attivita) attivita.get(index);

					codiceAttivitaParent = tbf_attivita.getCd_funzione_parent();

					if (codiceAttivitaParent != null &&  ((codiceAttivitaParent).compareTo( ((String)ote1.getValue()).substring(0, 5) ) == 0))
		        	{
						codiceAttivita = tbf_attivita.getCd_attivita();
						descrizioneAttivita = tbf_attivita.getId_attivita_sbnmarc().getDs_attivita();
		//				oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita + ": " + descrizioneAttivita));
//						oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK", new String(codiceAttivita));
						oteChildren.addElementNoSort(new String(descrizioneAttivita), new String(codiceAttivita), "CHECK", new String(codiceAttivita), tbf_attivita.getPrg_ordimanento() + "");
		        	}
				}
		        if (ValidazioneDati.isFilled(oteChildren.getElements()) )
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

	class StringComparator implements Comparator<GruppoParametriVO>
	{
	public final int compare ( GruppoParametriVO a, GruppoParametriVO b )
	   {
		String sa = a.getCodice();
		String sb = b.getCodice();
		return( (sa).compareToIgnoreCase( sb )); // Ascending

	   } // end compare
	} // end class StringComparator

	class OrdinaEdizioni implements Comparator<String>
	{
	public final int compare ( String a, String b )
	   {
		return( (a).compareToIgnoreCase( b )); // Ascending

	   } // end compare
	} // end class StringComparator

	public List getElencoParametri(String codBib) throws DaoManagerException, RemoteException {
		Tbf_poloDao pdao = new Tbf_poloDao();
		Tbf_parametro parametriPolo = pdao.getParametri(pdao.getIdParametro());

		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		int idParametro = dao.getIdParametro(codBib);
		Tbf_parametro parametriBib = dao.getParametri(idParametro);
		Set<Tbf_par_auth> parAuth = parametriBib.getTbf_par_auth();
		Set<Tbf_par_sem> parSem = parametriBib.getTbf_par_sem();
		Set<Tbf_par_mat> parMat = parametriBib.getTbf_par_mat();

		if (!isFilled(parAuth)) {
			parametriBib = parametriPolo;
			parAuth = parametriBib.getTbf_par_auth();
		}
		/*//almaviva5_20130418
		if (semantica.length == 0) {
			parametri = dao.getParametri(parametriPolo);
			semantica = ((Tbf_parametro)parametri.get(0)).getTbf_par_sem().toArray();
		}
		*/
		if (!isFilled(parMat)) {
			parametriBib = parametriPolo;
			parMat = parametriBib.getTbf_par_mat();
		}

		//almaviva5_20140507 evolutive google3
		List<MergedParAut> mergedAut = ProfilazioneUtil.mergeParAuth(parametriPolo.getTbf_par_auth(), parAuth, null);
		List<MergedParMat> mergedMat = ProfilazioneUtil.mergeParMat(parametriPolo.getTbf_par_mat(), parMat, null);

		List authorities = dao.getAuthorities();
		List materiali = dao.getMateriali();

		List<GruppoParametriVO> elencoAuth = new ArrayList<GruppoParametriVO>();
		List<GruppoParametriVO> elencoSem = new ArrayList<GruppoParametriVO>();
		List<GruppoParametriVO> elencoParMat = new ArrayList<GruppoParametriVO>();
		List output = new ArrayList();

		//Authority:
		for (MergedParAut aut : mergedAut) {
			GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
			String codice = aut.getCd_par_auth();
			// Troviamo la descrizione dell'authority
			String descrizione = "";
			int size = ValidazioneDati.size(authorities);
			for (int ik=0; ik < size; ik++) {
				Tb_codici tb_codici = (Tb_codici)authorities.get(ik);
				if (tb_codici.getCd_tabella().startsWith(codice))
					descrizione = tb_codici.getDs_tabella();
			}
			GruppoParametriVO.setCodice(codice);
			GruppoParametriVO.setDescrizione(descrizione);
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			char parChar = aut.getTp_abil_auth();
			parametro.setDescrizione("profilo.polo.parametri.abil");
			parametro.setIndex("0");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getTp_abil_auth_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parChar = aut.getFl_abil_legame();
			parametro.setDescrizione("profilo.polo.parametri.abil_legame");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getFl_abil_legame_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parChar = aut.getFl_leg_auth();
			parametro.setDescrizione("profilo.polo.parametri.leg_auth");
			parametro.setIndex("2");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getFl_leg_auth_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			String par = aut.getCd_livello();
			parametro.setDescrizione("profilo.polo.parametri.livello");
			parametro.setIndex("3");
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			List<TB_CODICI> elencoCodici = null;
			try {
				elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
			}
			catch (Exception e){
				log.error("", e);
			}
			int size2 = ValidazioneDati.size(elencoCodici);
			for (int k = 1; k < size2; k++) {
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

//			parametro = new ParametroVO();
//			par = aut.getCd_contr_sim();
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
			parChar = aut.getFl_abil_forzat();
			parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
			parametro.setIndex("4");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(aut.getFl_abil_forzat_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			parChar = aut.getSololocale();
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
			parametro.setCongelato(aut.getSololocale_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

			GruppoParametriVO.setElencoParametri(elencoParametri);
			elencoAuth.add(GruppoParametriVO);
		}

		// Ordinamento degli authority e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoAuth, new StringComparator());

		int size = ValidazioneDati.size(elencoAuth);
		for (int g = 0; g < size; g++) {
			elencoAuth.get(g).setIndice(g + "");
		}

		output.add(elencoAuth);

		//Par_mat:
		for (MergedParMat mat : mergedMat) {
			GruppoParametriVO GruppoParametriVO = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
			char codice = mat.getCd_par_mat();
			// Troviamo la descrizione dei materiali
			String descrizione = "";
			for (int ik=0; ik < materiali.size(); ik++) {
				Tb_codici tb_codici = (Tb_codici)materiali.get(ik);
				if (tb_codici.getCd_tabella().charAt(0) == codice)
					descrizione = tb_codici.getDs_tabella();
			}
			GruppoParametriVO.setCodice(codice + "");
			GruppoParametriVO.setDescrizione(descrizione);
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			ParametroVO parametro = new ParametroVO();

			char parChar = mat.getTp_abilitaz();
			parametro.setDescrizione("profilo.polo.parametri.abil");
			parametro.setIndex("0");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(mat.getTp_abilitaz_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

//			parametro = new ParametroVO();
//			String par = mat.getCd_contr_sim();
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
			parChar = mat.getFl_abil_forzat();
			parametro.setDescrizione("profilo.polo.parametri.abil_forzat");
			parametro.setIndex("1");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(mat.getFl_abil_forzat_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

			parametro = new ParametroVO();
			String par = mat.getCd_livello();
			parametro.setDescrizione("profilo.polo.parametri.livello");
			parametro.setIndex("2");
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			CodiciType cod = null;
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
			parChar = mat.getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex("3");
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S')
				parametro.setSelezione("Si'");
			else if (parChar == 'N')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			parametro.setCongelato(mat.getSololocale_Origine() != Origine.BIBLIOTECA ? "TRUE" : "FALSE");

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
		for (Tbf_par_sem sem : parSem) {
			GruppoParametriVO gp = new GruppoParametriVO();
			List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();

			String codice = "";
			String descrizione = "";
			List<String> opzioniRadio = new ArrayList<String>();
			opzioniRadio.add("Si'");
			opzioniRadio.add("No");
			int index = 0;
			String sistema = "";

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
			parametro.setIndex(index + "");
			index++;
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
					sistema = tabCodice.getCd_tabella();
					parametro.setSelezione(tabCodice.getDs_tabella());
				}
			}
//			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("TESTO");
			elencoParametri.add(parametro);

			if (sem.getTp_tabella_codici().trim().equals("SCLA")) {
				parametro = new ParametroVO();
				parametro.setIndex(index + "");
				index++;
				if (sem.getCd_tabella_codici().trim().equals(it.iccu.sbn.util.Constants.Semantica.Classi.SISTEMA_CLASSE_DEWEY)) {
					parametro.setDescrizione("profilo.biblioteca.semantica.edizione");
					parametro.setTipo("CHECK");
					List<String> elencoEdizioni = new ArrayList<String>();
					List<String> edizioniBiblioteca = dao.getElencoEdizioni(codBib, sistema);
					cod = null;
					elencoCodici = null;
					try {
						elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_CLASSE);
					}
					catch (Exception e){
						log.error("", e);
					}
					for (int k = 1; k < elencoCodici.size(); k++) {
						TB_CODICI tabCodice = elencoCodici.get(k);
						elencoEdizioni.add(tabCodice.getCd_unimarc());
					}
					Collections.sort(elencoEdizioni, new OrdinaEdizioni());
					List<CheckVO> elencoCheck = new ArrayList<CheckVO>();
					if (ValidazioneDati.isFilled(edizioniBiblioteca) ) {
						for (int u = 0; u < elencoEdizioni.size(); u++) {
							CheckVO check = new CheckVO();
							String edUnimarc = elencoEdizioni.get(u);
							check.setDescrizione(edUnimarc);
							check.setIndice(u + "");

							//almaviva5_20141117 edizioni ridotte
							TB_CODICI cd = null;
							try {
								cd = CodiciProvider.cercaCodice(edUnimarc, CodiciType.CODICE_EDIZIONE_CLASSE, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
							} catch (Exception e) {
								log.error("", e);
							}
							if (cd == null)
								throw new ApplicationException(SbnErrorTypes.GS_EDIZIONE_DEWEY_NON_VALIDA, edUnimarc);
							String edizione = cd.getCd_tabellaTrim();
							if (!ValidazioneDati.isFilled(edizione))
								throw new ApplicationException(SbnErrorTypes.GS_EDIZIONE_DEWEY_NON_VALIDA, edUnimarc);
							if (edizioniBiblioteca.contains(edizione))
								check.setSelezione(true);
							else
								check.setSelezione(false);
							elencoCheck.add(check);
						}
					}
					else {
						for (int u = 0; u <elencoEdizioni.size(); u++) {
							CheckVO check = new CheckVO();
							check.setDescrizione(elencoEdizioni.get(u));
							check.setIndice(u + "");
							check.setSelezione(false);
							elencoCheck.add(check);
						}
					}
					parametro.setElencoCheck(elencoCheck);
				}
				else {
					parametro.setDescrizione("profilo.biblioteca.semantica.edizioneunica");
					parametro.setTipo("MESSAGGIO");
				}
				elencoParametri.add(parametro);
			}

			parametro = new ParametroVO();
			char parChar = ' ';
			if (sem.getTp_tabella_codici().trim().equals("SCLA"))
				parChar = dao.getUtilizzatoScla(codBib, sistema);
			else if (sem.getTp_tabella_codici().trim().equals("SOGG"))
				parChar = dao.getUtilizzatoSogg(codBib, sistema);
			else if (sem.getTp_tabella_codici().trim().equals("STHE"))
				parChar = dao.getUtilizzatoSthe(codBib, sistema);
			parametro.setDescrizione("profilo.biblioteca.semantica.utilizzato");
			parametro.setIndex(index + "");
			index++;
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S' || parChar == '1')
				parametro.setSelezione("Si'");
			else if (parChar == 'N' || parChar == '0')
				parametro.setSelezione("No");
			parametro.setTipo("RADIO");
			elencoParametri.add(parametro);
			/* almaviva5_20101011 #3929
			if (!sem.getTp_tabella_codici().trim().equals("SCLA")) {
				parametro = new ParametroVO();
				parChar = ' ';
				if (sem.getTp_tabella_codici().trim().equals("SOGG"))
					parChar = dao.getRecuperoSogg(codBib, sistema);
				else if (sem.getTp_tabella_codici().trim().equals("STHE"))
					parChar = dao.getRecuperoSthe(codBib, sistema);
				parametro.setDescrizione("profilo.biblioteca.semantica.recupero");
				parametro.setIndex(index + "");
				index++;
				parametro.setRadioOptions(opzioniRadio);
				if (parChar == 'S' || parChar == '1')
					parametro.setSelezione("Si'");
				else if (parChar == 'N' || parChar == '0')
					parametro.setSelezione("No");
				parametro.setTipo("RADIO");
				elencoParametri.add(parametro);
			}
			*/
			parametro = new ParametroVO();
			parChar = sem.getSololocale();
			parametro.setDescrizione("profilo.polo.parametri.sololocale");
			parametro.setIndex(index + "");
			index++;
			parametro.setRadioOptions(opzioniRadio);
			if (parChar == 'S' || parChar == '1')
				parametro.setSelezione("Si'");
			else if (parChar == 'N' || parChar == '0')
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

	public List<ComboVO> getElencoBiblioteche()
	throws DaoManagerException, RemoteException {
		try {
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			List<Tbf_biblioteca> elenco = dao.getElencoBiblioteche();
			for (int i = 0; i < elenco.size(); i++) {
				ComboVO combo = new ComboVO();
				Tbf_biblioteca biblio = elenco.get(i);
				combo.setCodice(biblio.getCd_bib());
				combo.setDescrizione(biblio.getNom_biblioteca());
				elencoCombo.add(combo);
			}
			return elencoCombo;
		}
		catch (DaoManagerException e) {
			log.error("", e);
			return null;
		}
	}

	public boolean isCentroSistema(String codPolo, String codBib)
	throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			return dao.isCentroSistema(codPolo, codBib);
		}
		catch (DaoManagerException e) {
			log.error("", e);
			return false;
		}
	}

	public String[] getElencoAttivitaProfilo(String codBib)
	throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			List<Trf_gruppo_attivita_polo> elenco = dao.loadAttivitaGruppoBiblioteca(codBib);
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

	public int setProfiloBiblioteca(String codiceBib, String codiceUtente,
			  List<String> elencoAttivita,
			  List<GruppoParametriVO> elencoAuthorities,
			  List<GruppoParametriVO> elencoMateriali,
			  List<GruppoParametriVO> elencoSemantica) throws DaoManagerException, DataException, RemoteException {

		try {
			// ************* ATTIVITA' *************

			// Se non esiste il gruppo attivitÃ , lo creiamo.
			Tbf_bibliotecaDao biblioDao = new Tbf_bibliotecaDao();
			int idGruppo = biblioDao.creaGruppoAttivitaBiblioteca(codiceBib);

			//Rimuovo, se esistono, i dati del gruppo dalla tabella Trf_gruppo_attivita_polo
			biblioDao.eliminaAttivitaDelGruppo(idGruppo);
			biblioDao.inserisciAttivitaDelGruppo(elencoAttivita, idGruppo);
			biblioDao.pulisciGruppoAttitaPadri(idGruppo);
			biblioDao.attivaProfiloAttivita(codiceBib, idGruppo);
			List<String> elencoUtentiAggiornati = biblioDao.aggiornaProfiloUtenti(codiceBib, idGruppo, elencoAttivita);

			// ************* PARAMETRI *************

			// Se non esiste il profilo dei parametri lo creiamo.
			int idParametro = biblioDao.creaProfiloParametroPolo(codiceBib, codiceUtente);
			boolean rimuoviAuth = false;
			boolean rimuoviMat = false;
			boolean rimuoviSem = false;
			// Inserisco i relativi parametri:
			if (elencoAuthorities != null && elencoAuthorities.size() >0)
				biblioDao.inserisciParAuth(elencoAuthorities, idParametro);
			else {
				biblioDao.rimuoviParAuth(idParametro);
				rimuoviAuth = true;
			}
			if (elencoMateriali != null && elencoMateriali.size() >0)
				biblioDao.inserisciParMat(elencoMateriali, idParametro);
			else {
				biblioDao.rimuoviParMat(idParametro);
				rimuoviMat = true;
			}
			int esito = 0;
			if (elencoSemantica != null)
				esito = biblioDao.inserisciParSem(elencoSemantica, idParametro, codiceBib, codiceUtente);
			else {
				biblioDao.rimuoviParSem(idParametro);
				rimuoviSem = true;
			}
			if (esito == 3 || esito == 4) {
				throw new DataException(esito + "");
			}
			//Rimuovo i parametri dei bibliotecari appartenenti alla biblioteca:
			if (rimuoviAuth || rimuoviMat || rimuoviSem)
				biblioDao.rimuoviParametriBibliotecari(codiceBib, rimuoviAuth, rimuoviMat, rimuoviSem);

			// ************* ATTIVO IL GRUPPO PARAMETRI *************
			biblioDao.attivaProfiloParametri(codiceBib, idParametro);

			//almaviva5_20130417 #5269
			biblioDao.aggiornaParametriBibliotecari(codiceBib);

			String urlUtenti = "";
			for (int j = 0; j < elencoUtentiAggiornati.size(); j++) {
				urlUtenti = urlUtenti + elencoUtentiAggiornati.get(j);
				if (j != elencoUtentiAggiornati.size() - 1 )
					urlUtenti = urlUtenti + ",";
			}
			biblioDao.clearCache("amministrazione");
			this.rimuoviProfilo(urlUtenti); // comunico a SbnMarc che il profilo vecchio non Ã¨ piÃ¹ valido
			SbnWebProfileCache.getInstance().clear(elencoUtentiAggiornati);
			return esito;
		}
		catch (Exception e) {
			ctx.setRollbackOnly();
			log.error("", e);
			return 2;
		}
	}

	private void rimuoviProfilo(String userId) throws DaoManagerException {
		try {
			getGateway().service("rimuoviProfili", userId);
		}
		catch (Exception e) {
			log.error("", e);
		}
	}

	public List<String> controllaAttivita(String codBib, List<String> elencoAttivita) throws DaoManagerException, RemoteException {

		try {
			Tbf_bibliotecaDao biblioDao = new Tbf_bibliotecaDao();
			return biblioDao.controllaAttivita(codBib, elencoAttivita);
		}
		catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public List<BibliotecaVO> cercaBiblioteche(String ticket, BibliotecaRicercaVO richiesta) throws DaoManagerException, RemoteException {
		checkTicket(ticket);
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			List<Tbf_biblioteca> elenco = dao.cercaBiblioteche(richiesta);
			int size = ValidazioneDati.size(elenco);
			List<BibliotecaVO> output = new ArrayList<BibliotecaVO>(size);

			for (int i = 0; i < size; i++) {
				Tbf_biblioteca biblioteca = elenco.get(i);
				BibliotecaVO bibVO = ConversioneHibernateVO.toWeb().anagraficaBiblioteca(biblioteca);
				bibVO.setPrg(i + 1);

				//almaviva5_20100202 #2579
				impostaBibliotecaCentroSistema(biblioteca, bibVO);

				bibVO.setTipo_biblioteca(CodiciProvider.cercaDescrizioneCodice(biblioteca.getTipo_biblioteca(),
					CodiciType.CODICE_TIPI_BIBLIOTECA, CodiciRicercaType.RICERCA_CODICE_SBN) );

				output.add(bibVO);
			}

			//almaviva5_20100202 #3492 ord. per indirizzo composto
			if (ValidazioneDati.equals(richiesta.getOrdinamento(), "indirizzo")) {
				Collections.sort(output, BibliotecaVO.ORDINAMENTO_PER_INDIRIZZO_COMPOSTO);
				int progr = 0;
				for (BibliotecaVO b : output)
					b.setPrg(++progr);
			}

			return output;
		}
		catch (DaoManagerException e){
			log.error("", e);
			return null;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

    private void impostaBibliotecaCentroSistema(Tbf_biblioteca biblioteca, BibliotecaVO bibVO) throws DaoManagerException {

		List<Tbf_biblioteca_in_polo> bibInPolo = new ArrayList<Tbf_biblioteca_in_polo>(biblioteca.getTbf_biblioteca_in_polo());
		if (ValidazioneDati.isFilled(bibInPolo) ) {
			bibVO.setAbilitata(true);
			Tbf_biblioteca_in_polo bibPolo = bibInPolo.get(0);
			bibVO.setGruppo(ValidazioneDati.trimOrEmpty(bibPolo.getCd_ana_biblioteca()) );
			//almaviva5_20091015
			bibVO.setCodSistemaMetropolitano(bibPolo.getCd_sistema_metropolitano());
		}

		/*	almaviva5_20100709 codice commentato in attesa di verifiche
		else {
			bibVO.setAbilitata(false);
			bibVO.setGruppo("");
			bibVO.setFlag_bib("N");
			bibVO.setCod_bib_cs("");
			return;
		}

    	//almaviva5_20100202 cerca bib. centro sistema
		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		Tbf_biblioteca cdBibCS = dao.getBibliotecaCentroSistema(bibVO.getCod_polo(), bibVO.getCod_bib());
		if (cdBibCS == null) {
			bibVO.setCod_bib_cs(biblioteca.getCd_bib());
			boolean affiliata = dao.esisteBibliotecaAffiliata(bibVO.getCod_polo(), bibVO.getCod_bib());
			if (affiliata)
				//la bib. Ã© centro sistema
				bibVO.setFlag_bib("C");
			 else
				//la bib. Ã© centro sistema ma non ha bib. affiliate attive
				bibVO.setFlag_bib("D");

		} else {
			//la bib. Ã© affiliata
			bibVO.setFlag_bib("A");
			bibVO.setCod_bib_cs(cdBibCS.getCd_bib());
		}

		*/
	}

	public List<BibliotecaVO> getBibliotecheCentroSistema() throws DaoManagerException, RemoteException {
    	try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			List<Tbf_biblioteca> elenco = dao.getBibliotecheCentroSistema();
			List<BibliotecaVO> output = new ArrayList<BibliotecaVO>();
			for (int i = 0; i < elenco.size(); i++) {
				BibliotecaVO bib = new BibliotecaVO();
				Tbf_biblioteca bibDB = elenco.get(i);
				bib.setCod_bib(bibDB.getCd_bib());
				bib.setNom_biblioteca(bibDB.getNom_biblioteca());
				output.add(bib);
			}
			return output;
    	}
		catch (DaoManagerException e){
			log.error("", e);
			return null;
		}
    }

    public String getCodicePoloCorrente() throws DaoManagerException, RemoteException {
    	try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			return dao.getCodicePoloCorrente();
    	}
		catch (DaoManagerException e){
			log.error("", e);
			return null;
		}
    }

    public BibliotecaVO creaBiblioteca(BibliotecaVO bib, String utenteInseritore, boolean forzaInserimento, boolean abilitazione, String codPoloCorrente)
	throws DaoManagerException, ApplicationException, RemoteException {
    	try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			//almaviva5_20120529 #5005
			if (ValidazioneDati.isFilled(bib.getCd_ana_biblioteca()) && dao.esisteCodiceAnagrafe(bib))
				throw new ApplicationException(SbnErrorTypes.AMM_COD_ANAGRAFE_BIBLIOTECA_ESISTENTE, bib.getCd_ana_biblioteca());

			BibliotecaVO updatedBib = dao.inserisciBiblioteca(bib, utenteInseritore, forzaInserimento, abilitazione, codPoloCorrente);

			//servizi ILL
			BibliotecaILLVO bibILL = bib.getBibliotecaILL();
			if (!bibILL.isNuovo() ||
					(bibILL.isNuovo() && !bibILL.isCancellato() ) ) {
				bibILL.setBiblioteca(updatedBib);
				bibILL.setUteVar(utenteInseritore);
				ServiziIllDAO sdao = new ServiziIllDAO();
				Tbl_biblioteca_ill tbl_bib_ill = sdao.getBibliotecaByIsil(bibILL.getIsil(), false);	// anche cancellato
				tbl_bib_ill = ConversioneHibernateVO.toHibernate().bibliotecaILL(tbl_bib_ill, bibILL);
				tbl_bib_ill = sdao.aggiornaBiblioteca(tbl_bib_ill);
				updatedBib.setBibliotecaILL(ConversioneHibernateVO.toWeb().bibliotecaILL(tbl_bib_ill, updatedBib));
			}

			return updatedBib;

		} catch (ApplicationException e) {
			throw e;

    	} catch (ConstraintViolationException e) {
			String cdBib = ValidazioneDati.trimOrEmpty(bib.getCod_polo()) + ValidazioneDati.trimOrEmpty(bib.getCod_bib());
			throw new ApplicationException(SbnErrorTypes.AMM_BIBLIOTECA_ESISTENTE, ValidazioneDati.isFilled(cdBib) ? cdBib : bib.getCd_ana_biblioteca());

    	} catch (DaoManagerException e) {
			log.error("", e);
			return null;

    	} catch (Exception e) {
			log.error("", e);
			return null;
		}
    }

	public BibliotecaVO caricaBiblioteca(int idBiblioteca)
			throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			Tbf_biblioteca biblioteca = dao.caricaBiblioteca(idBiblioteca);
			BibliotecaVO bibVO = ConversioneHibernateVO.toWeb().anagraficaBiblioteca(biblioteca);
			// almaviva5_20100202 cerca bib. centro sistema
			impostaBibliotecaCentroSistema(biblioteca, bibVO);

			ServiziIllDAO sidao = new ServiziIllDAO();
			Tbl_biblioteca_ill bibILL = sidao.getBibliotecaByIsil(bibVO.getIsil());
			if (bibILL != null)
				bibVO.setBibliotecaILL(ConversioneHibernateVO.toWeb().bibliotecaILL(bibILL, bibVO));

			return bibVO;

		} catch (DaoManagerException e) {
			log.error("", e);
			return null;
		}
	}

    public boolean controllaAbilitazioneBiblioteca(int idBib) throws DaoManagerException, RemoteException {
    	try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			return dao.controllaAbilitazioneBiblioteca(idBib);
    	}
		catch (DaoManagerException e){
			log.error("", e);
			return false;
		}
    }

	public List<String> getElencoModelli() throws DaoManagerException, RemoteException {

		try {
			Tbf_bibliotecaDao biblioDao = new Tbf_bibliotecaDao();
			return biblioDao.getElencoModelli();
		}
		catch (Exception e) {
			log.error("", e);
			return new ArrayList<String>();
		}
	}

	public String[] getElencoAttivitaProfiloModello(String idModello)
	throws DaoManagerException, RemoteException {
		try {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
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

	public List getElencoParametriModello(String idModello) throws DaoManagerException, RemoteException {
		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		int idParametro = dao.getModello(idModello).getId_parametro().getId_parametro();
		Tbf_parametro parametri = dao.getParametri(idParametro);
		Object auth[] = parametri.getTbf_par_auth().toArray();
		Object semantica[] = parametri.getTbf_par_sem().toArray();
		Object parMat[] = parametri.getTbf_par_mat().toArray();

//		int parametriPolo = dao.getParametroPolo();
//		if (auth.length == 0) {
//			parametri = dao.getParametri(parametriPolo);
//			auth = ((Tbf_parametro)parametri.get(0)).getTbf_par_auth().toArray();
//		}
//		if (semantica.length == 0) {
//			parametri = dao.getParametri(parametriPolo);
//			semantica = ((Tbf_parametro)parametri.get(0)).getTbf_par_sem().toArray();
//		}
//		if (parMat.length == 0) {
//			parametri = dao.getParametri(parametriPolo);
//			parMat = ((Tbf_parametro)parametri.get(0)).getTbf_par_mat().toArray();
//		}

		List authorities = dao.getAuthorities();
		List materiali = dao.getMateriali();

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
				Tb_codici tb_codici = (Tb_codici)authorities.get(ik);
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
			CodiciType cod = null;
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
				Tb_codici tb_codici = (Tb_codici)materiali.get(ik);
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
			CodiciType cod = null;
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
			int index = 0;
			String sistema = "";

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
			parametro.setIndex(index + "");
			index++;
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
					sistema = tabCodice.getCd_tabella();
					parametro.setSelezione(tabCodice.getDs_tabella());
				}
			}
//			parametro.setElencoScelte(elencoCombo);
			parametro.setTipo("TESTO");
			elencoParametri.add(parametro);

//			if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SCLA")) {
//				parametro = new ParametroVO();
//				parametro.setIndex(index + "");
//				index++;
//				if (((Tbf_par_sem)semantica[i]).getCd_tabella_codici().trim().equals("D")) {
//					parametro.setDescrizione("profilo.biblioteca.semantica.edizione");
//					parametro.setTipo("CHECK");
//					List<String> elencoEdizioni = new ArrayList<String>();
//					List<String> edizioniBiblioteca = dao.getElencoEdizioni(codBib, sistema);
//					cod = null;
//					elencoCodici = null;
//					try {
//						cod = CodiciType.fromString("SCLA");
//						elencoCodici = CodiciProvider.getCodici(cod);
//					}
//					catch (Exception e){
//						log.error("", e);
//					}
//					for (int k = 1; k < elencoCodici.size(); k++) {
//						TB_CODICI tabCodice = elencoCodici.get(k);
//						elencoEdizioni.add(tabCodice.getCd_unimarc());
//					}
//					Collections.sort(elencoEdizioni, new OrdinaEdizioni());
//					List<CheckVO> elencoCheck = new ArrayList<CheckVO>();
//					if (edizioniBiblioteca.size()>0) {
//						for (int u = 0; u <elencoEdizioni.size(); u++) {
//							CheckVO check = new CheckVO();
//							check.setDescrizione(elencoEdizioni.get(u));
//							check.setIndice(u + "");
//							if (edizioniBiblioteca.contains(elencoEdizioni.get(u)))
//								check.setSelezione(true);
//							else
//								check.setSelezione(false);
//							elencoCheck.add(check);
//						}
//					}
//					else {
//						for (int u = 0; u <elencoEdizioni.size(); u++) {
//							CheckVO check = new CheckVO();
//							check.setDescrizione(elencoEdizioni.get(u));
//							check.setIndice(u + "");
//							check.setSelezione(false);
//							elencoCheck.add(check);
//						}
//					}
//					parametro.setElencoCheck(elencoCheck);
//				}
//				else {
//					parametro.setDescrizione("profilo.biblioteca.semantica.edizioneunica");
//					parametro.setTipo("MESSAGGIO");
//				}
//				elencoParametri.add(parametro);
//			}
//
//			parametro = new ParametroVO();
//			char parChar = ' ';
//			if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SCLA"))
//				parChar = dao.getUtilizzatoScla(codBib, sistema);
//			else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SOGG"))
//				parChar = dao.getUtilizzatoSogg(codBib, sistema);
//			else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("STHE"))
//				parChar = dao.getUtilizzatoSthe(codBib, sistema);
//			parametro.setDescrizione("profilo.biblioteca.semantica.utilizzato");
//			parametro.setIndex(index + "");
//			index++;
//			parametro.setRadioOptions(opzioniRadio);
//			if (parChar == 'S' || parChar == '1')
//				parametro.setSelezione("Si'");
//			else if (parChar == 'N' || parChar == '0')
//				parametro.setSelezione("No");
//			parametro.setTipo("RADIO");
//			elencoParametri.add(parametro);
//
//			if (!((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SCLA")) {
//				parametro = new ParametroVO();
//				parChar = ' ';
//				if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("SOGG"))
//					parChar = dao.getRecuperoSogg(codBib, sistema);
//				else if (((Tbf_par_sem)semantica[i]).getTp_tabella_codici().trim().equals("STHE"))
//					parChar = dao.getRecuperoSthe(codBib, sistema);
//				parametro.setDescrizione("profilo.biblioteca.semantica.recupero");
//				parametro.setIndex(index + "");
//				index++;
//				parametro.setRadioOptions(opzioniRadio);
//				if (parChar == 'S' || parChar == '1')
//					parametro.setSelezione("Si'");
//				else if (parChar == 'N' || parChar == '0')
//					parametro.setSelezione("No");
//				parametro.setTipo("RADIO");
//				elencoParametri.add(parametro);
//			}
//
//			parametro = new ParametroVO();
//			parChar = ((Tbf_par_sem)semantica[i]).getSololocale();
//			parametro.setDescrizione("profilo.polo.parametri.sololocale");
//			parametro.setIndex(index + "");
//			index++;
//			parametro.setRadioOptions(opzioniRadio);
//			if (parChar == 'S' || parChar == '1')
//				parametro.setSelezione("Si'");
//			else if (parChar == 'N' || parChar == '0')
//				parametro.setSelezione("No");
//			parametro.setTipo("RADIO");
//			elencoParametri.add(parametro);
//
//			GruppoParametriVO.setElencoParametri(elencoParametri);
//			elencoSem.add(GruppoParametriVO);
		}

		// Ordinamento dei materiali e inserimento dell'indice di visualizzazione:
		Collections.sort(elencoSem, new StringComparator());

		for (int g = 0; g < elencoSem.size(); g++) {
			elencoSem.get(g).setIndice(g + "");
		}

		output.add(elencoSem);

		return output;

	}

	public long export(EsportaVO esporta, String filePathName, BatchLogWriter blw) throws ValidationException, EJBException {
		try {
			SbnUnimarcBIDExtractor extractor = new SbnUnimarcBIDExtractor(esporta, blw);
			return extractor.extract(filePathName);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

}