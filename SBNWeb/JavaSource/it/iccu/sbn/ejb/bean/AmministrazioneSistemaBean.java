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
package it.iccu.sbn.ejb.bean;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.SbnBusinessSessionBean;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.AmministrazioneSistema;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiciPermessiType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.custom.BibliotecaSearch;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.persistence.dao.common.HibernateUtil;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.pagination.QueryPage;
import it.iccu.sbn.servizi.pagination.QueryPaginator;
import it.iccu.sbn.servizi.pagination.test.TestRangeCollQueryLogic;
import it.iccu.sbn.servizi.pagination.test.TestRangeCollQueryParams;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;


public class AmministrazioneSistemaBean extends SbnBusinessSessionBean implements AmministrazioneSistema {

	private static final long serialVersionUID = 7115062117397526582L;

	private static Logger log = Logger.getLogger(AmministrazioneSistema.class);

    private AmministrazioneBiblioteca amministrazioneBiblioteca;
    private AmministrazioneBibliotecario amministrazioneBibliotecario;
    private AmministrazioneGestioneCodici amministrazioneGestioneCodici;
	private AmministrazionePolo amministrazionePolo;

	private AmministrazionePolo getAmministrazionePolo() throws Exception {

		if (amministrazionePolo != null)
			return amministrazionePolo;

		amministrazionePolo = DomainEJBFactory.getInstance().getPolo();

		return amministrazionePolo;

	}

	private AmministrazioneBiblioteca getAmministrazioneBiblioteca() throws Exception {

		if (amministrazioneBiblioteca != null)
			return amministrazioneBiblioteca;

		this.amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBiblioteca;

	}

	private AmministrazioneBibliotecario getAmministrazioneBibliotecario() throws Exception {

		if (amministrazioneBibliotecario != null)
			return amministrazioneBibliotecario;

		this.amministrazioneBibliotecario = DomainEJBFactory.getInstance().getBibliotecario();

		return amministrazioneBibliotecario;
	}

	private AmministrazioneGestioneCodici getAmministrazioneGestioneCodici() throws Exception {

		if (amministrazioneGestioneCodici != null)
			return amministrazioneGestioneCodici;

		this.amministrazioneGestioneCodici = DomainEJBFactory.getInstance().getAmministrazioneGestioneCodici();

		return amministrazioneGestioneCodici;
	}


	public void ejbCreate() {
		log.info("creato ejb");
		return;
	}

    public BibliotecaVO getBiblioteca(String Cod_polo,String Cod_bib) throws EJBException {
    	BibliotecaVO Biblio = null;
        BibliotecaSearch search = new BibliotecaSearch();
    	search.setCodBib(Cod_bib);
    	search.setCodPolo(Cod_polo);

        try {
        	List<BibliotecaVO> results = getAmministrazioneBiblioteca().getBiblioteche(search);
        	if(results.size()>0)
        		Biblio = results.get(0);
        } catch (InfrastructureException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (DaoManagerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (InfrastructureException e) {
				e.printStackTrace();
			}
		}
        return Biblio;
    }

    public List<BibliotecaVO> getBiblioteche(BibliotecaSearch search,int offset) throws EJBException {
        List<BibliotecaVO> elencoBiblio = null;

        try {
        	Session session = HibernateUtil.getSession();

        	BibliotecaVO bib = new BibliotecaVO();
        	bib.setCod_bib(search.getCodBib());
        	bib.setCod_polo(search.getCodPolo());
        	bib.setTipo_biblioteca(search.getTipoBib());
			bib.setChiave_ente(search.getEnteApp());
			bib.setNom_biblioteca(search.getNomeBib());
			bib.setProvincia(search.getProvincia());
			bib.setPaese(search.getPaese());

			Criteria cr = session.createCriteria(BibliotecaVO.class);
        	cr.add(Example.create(bib).excludeZeroes().enableLike(MatchMode.ANYWHERE));
        	if (bib.getCod_bib() != null)
				cr.add(Expression.like("cod_bib", "%" + bib.getCod_bib() + "%"));
			if (bib.getCod_polo() != null)
				cr.add(Expression.like("cod_polo", "%" + bib.getCod_polo() + "%"));

        	elencoBiblio = cr.list();

        } catch (InfrastructureException e) {
			e.printStackTrace();
		} finally{
			try {
				HibernateUtil.closeSession();
			} catch (InfrastructureException e) {
				e.printStackTrace();
			}
		}
        return elencoBiblio;
    }

    public List<ComboCodDescVO> getBibliotecheCentroSistema(String ticket) throws EJBException {
    	String codPolo="";

    	try {
    		checkTicket(ticket);
	    	if (ticket != null) {
	    		codPolo = ticket.substring(0, ticket.indexOf("_"));
	    		return getAmministrazioneBiblioteca().getAllBiblioteche(codPolo);
	    	}

	    	return null;

		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}

    }

    public DescrittoreBloccoVO getListaBibliotecheAffiliatePerAttivita(String ticket, String codPolo, String codBib, String codAttivita, int elemBlocco) throws EJBException {

    	try {
    		checkTicket(ticket);
    		List<BibliotecaVO> output = getAmministrazioneBiblioteca().getListaBibliotecheAffiliatePerAttivita(codPolo, codBib, codAttivita, false);
    		return saveBlocchi(ticket, output, elemBlocco);

    	} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public List<ComboVO> getListaComboBibliotecheAffiliatePerAttivita(String ticket, String codPolo, String codBib, String codAttivita) throws EJBException {

    	try {
    		checkTicket(ticket);
    		List<BibliotecaVO> elenco = getAmministrazioneBiblioteca().getListaBibliotecheAffiliatePerAttivita(codPolo, codBib, codAttivita, true);
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			for (int i = 0; i < elenco.size(); i++) {
				ComboVO combo = new ComboVO();
				BibliotecaVO biblio = elenco.get(i);
				combo.setCodice(biblio.getCod_bib());
				combo.setDescrizione(biblio.getNom_biblioteca());
				elencoCombo.add(combo);
			}

    		return elencoCombo;

    	} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

	public DescrittoreBloccoVO getListaBibliotechePolo(String ticket,
			String codPolo, int elementiPerBlocco)
			throws EJBException {
		try {
			checkTicket(ticket);
			List<BibliotecaVO> output = getAmministrazioneBiblioteca().getListaBibliotechePolo(codPolo);
			return saveBlocchi(ticket, output, elementiPerBlocco);

		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

    public void updateBiblioteca(BibliotecaVO bibliotecaVO) throws EJBException {
		try {
			getAmministrazioneBiblioteca().updateBiblioteca(bibliotecaVO);
		} catch (ApplicationException e) {
			throw new EJBException((Exception) e.detail);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public DescrittoreBloccoVO cercaUtenti(String ticket, String cognome, String nome, String username, String biblioteca, String dataAccesso, String abilitato, String ordinamento, int numElemBlocco) throws EJBException, DaoManagerException, RemoteException {
		try {
			List<UtenteVO> elenco = getAmministrazioneBibliotecario().cercaUtenti(cognome, nome, username, biblioteca, dataAccesso, abilitato, ordinamento);
			DescrittoreBloccoVO blocco1 = this.saveBlocchi(ticket, elenco,
					numElemBlocco);
			return blocco1;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public List<ComboVO> getElencoBiblioteche() throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBiblioteca().getElencoBiblioteche();
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public DescrittoreBloccoVO cercaConfigTabelleCodici(String ticket, String user, int numElemBlocco) throws EJBException, DaoManagerException, RemoteException {
		try {
			List<CodiceConfigVO> elenco = getAmministrazioneGestioneCodici().cercaConfigTabelleCodici(user);
			DescrittoreBloccoVO blocco1 = this.saveBlocchi(ticket, elenco, numElemBlocco);
			return blocco1;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public DescrittoreBloccoVO cercaTabellaCodici(String ticket, int numElemBlocco, String cdTabella) throws EJBException, DaoManagerException, RemoteException {
		try {
			List<CodiceVO> elenco = getAmministrazioneGestioneCodici().cercaTabellaCodici(cdTabella);
			DescrittoreBloccoVO blocco1 = this.saveBlocchi(ticket, elenco, numElemBlocco);
			return blocco1;

		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public boolean isCentroSistema(String polo, String bib) throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBiblioteca().isCentroSistema(polo, bib);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public UtenteVO creaBibliotecario(UtenteVO bibliotecario, int utenteInseritore, boolean forzaInserimento, boolean abilitazione) throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBibliotecario().creaBibliotecario(bibliotecario, utenteInseritore, forzaInserimento, abilitazione);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

    public UtenteVO caricaBibliotecario(int idUtente) throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBibliotecario().caricaBibliotecario(idUtente);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public boolean controllaAbilitazioneBibliotecario(int idUtente) throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBibliotecario().controllaAbilitazioneBibliotecario(idUtente);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public int getDurataPassword() throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBibliotecario().getDurataPassword();
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public DescrittoreBloccoVO cercaBiblioteche(String ticket, BibliotecaRicercaVO richiesta) throws EJBException, DaoManagerException, RemoteException {
		try {
			List<BibliotecaVO> elenco = getAmministrazioneBiblioteca().cercaBiblioteche(ticket, richiesta);
			DescrittoreBloccoVO blocco1 = this.saveBlocchi(ticket, elenco, richiesta.getElemPerBlocchi() );
			return blocco1;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public List<BibliotecaVO> getBibliotecheCentroSistema() throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBiblioteca().getBibliotecheCentroSistema();
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public String getCodicePoloCorrente() throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBiblioteca().getCodicePoloCorrente();
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

	public BibliotecaVO creaBiblioteca(BibliotecaVO biblioteca, String utenteInseritore, boolean forzaInserimento, boolean abilitazione, String codPoloCorrente) throws DaoManagerException, ApplicationException, EJBException {
		try {
			return getAmministrazioneBiblioteca().creaBiblioteca(biblioteca, utenteInseritore, forzaInserimento, abilitazione, codPoloCorrente);
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

    public BibliotecaVO caricaBiblioteca(int idBiblioteca) throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBiblioteca().caricaBiblioteca(idBiblioteca);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public boolean controllaAbilitazioneBiblioteca(int idBib) throws EJBException, DaoManagerException, RemoteException {
		try {
			return getAmministrazioneBiblioteca().controllaAbilitazioneBiblioteca(idBib);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public void removeUserTicket(String ticket) throws EJBException, DaoManagerException, RemoteException {
		try {
			getAmministrazioneBibliotecario().removeUserTicket(ticket);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new DaoManagerException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public boolean salvaTabellaCodici(CodiceVO codice, boolean validate) throws ValidationException, EJBException {
		try {
			return getAmministrazioneGestioneCodici().salvaTabellaCodici(codice, validate);
		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public PoloVO getPolo() {
    	try {
			return getAmministrazionePolo().getInfoPolo();
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public boolean abilitaTabella(String ticket, CodiceConfigVO config,	CodiciPermessiType permessi) throws EJBException {
		try {
			return getAmministrazioneGestioneCodici().abilitaTabella(ticket, config, permessi);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    public List<ComboVO> getListaComboBibliotecheSistemaMetropolitano(String ticket, String codPolo, String codBib) throws EJBException {
    	try {
    		checkTicket(ticket);
    		List<BibliotecaVO> elenco = getAmministrazioneBiblioteca().getListaBibliotecheSistemaMetropolitano(codPolo, codBib);
			List<ComboVO> elencoCombo = new ArrayList<ComboVO>();
			for (int i = 0; i < elenco.size(); i++) {
				ComboVO combo = new ComboVO();
				BibliotecaVO biblio = elenco.get(i);
				combo.setCodice(biblio.getCod_bib());
				combo.setDescrizione(biblio.getNom_biblioteca());
				elencoCombo.add(combo);
			}

    		return elencoCombo;

    	} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
   }

    public DescrittoreBloccoVO getListaBibliotecheSistemaMetropolitano(String ticket, String codPolo, String codBib, int elemBlocco) throws EJBException {
    	try {
    		checkTicket(ticket);
    		List<BibliotecaVO> elenco = getAmministrazioneBiblioteca().getListaBibliotecheSistemaMetropolitano(codPolo, codBib);
    		return saveBlocchi(ticket, elenco, elemBlocco);

    	} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
   }

    public QueryPage testPaginazione() throws EJBException {
    	try {
			CodiciNormalizzatiVO nrg = NormalizzaRangeCollocazioni.normalizzaCollSpec(null, " ", null, false, null, null, false, null);
			//TestRangeCollQueryParams params = new TestRangeCollQueryParams("CFI", " CF", "GEN", nrg.getDaColl(), nrg.getAColl());
			TestRangeCollQueryParams params = new TestRangeCollQueryParams("SBW", " IC", "2009", nrg.getDaColl(), nrg.getAColl());

			return QueryPaginator.paginate(TestRangeCollQueryLogic.class, params, ConstantDefault.ELEMENTI_BLOCCHI.getDefaultAsNumber());
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

}
