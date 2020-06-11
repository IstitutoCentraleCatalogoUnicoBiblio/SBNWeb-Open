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
package it.iccu.sbn.persistence.dao.servizi;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_specificita_titoli_studio;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_diritti_utente;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.wrap;

public class UtentiDAO extends ServiziBaseDAO {

	private UtilitaDAO dao        = new UtilitaDAO();
	private ServiziDAO serviziDAO = new ServiziDAO();

	private Example createRicercaUtentiExample(Trl_utenti_biblioteca uteBib, String alias, Criteria criteria, Boolean ricercaSuPolo, String tipoRic )
	{
		Example example = Example.create(uteBib);
		example.setPropertySelector(NOT_EMPTY);
		Tbl_utenti utente = uteBib.getId_utenti();
		if (utente != null) {
			//almaviva5_20110427 #4398
			//if (!ValidazioneDati.isFilled(utente.getCod_fiscale()))
				utente.setFl_canc('N');

			example = Example.create(utente);

			example.ignoreCase();
			example.setPropertySelector(NOT_EMPTY);
			// escludo questo campo perchè definito not-null ma non previsto
			// in ricerca
			example.excludeProperty("sesso");
/*			if (uteBib.getId_utenti().getChiave_ute()!= null)
			{
				example.excludeProperty("cognome"); // 09.02.10
			}
*/
			if (ricercaSuPolo) {
				example.excludeProperty("cd_bib");
				//almaviva5_20111220 segnalazione TO0
				example.excludeProperty("cd_bib_iscrizione");
			}
			if (ValidazioneDati.in(tipoRic, "like", "polo"))
				example.enableLike(MatchMode.START);

			//almaviva5_20160108 #6072 per ricerca su parole va usato il campo 'tidx_vector'
			if (ValidazioneDati.in(tipoRic, "parole")) {
				example.excludeProperty("cognome");
				example.excludeProperty("nome");
				example.excludeProperty("chiave_ute");
			}

			//almaviva5_20150428
			example.excludeProperty("ind_posta_elettr");
			example.excludeProperty("ind_posta_elettr2");

			Criteria c = criteria.createCriteria("id_utenti", alias);
			c.add(example);
		}
		// aggiungo tipo ordinamento richiesto

		return example;
	}

	private void createCriteriaMatricola(Trl_utenti_biblioteca uteBib,Criteria c) {
		if (uteBib.getId_utenti()!=null && ValidazioneDati.isFilled(uteBib.getId_utenti().getCod_matricola()))
		{
			//per la ricerca esatta
			c.add(Restrictions.eq("ute.cod_matricola", uteBib.getId_utenti().getCod_matricola()));
		}

	}

	private void createCriteriaEmail(Trl_utenti_biblioteca uteBib, Criteria c) {
		if (uteBib.getId_utenti() != null
				&& ValidazioneDati.isFilled(uteBib.getId_utenti().getInd_posta_elettr())) {
			// almaviva5_20150428 doppia mail
			String email = uteBib.getId_utenti().getInd_posta_elettr().trim();
			c.add(Restrictions.or(Restrictions.eq("ute.ind_posta_elettr", email).ignoreCase(),
					Restrictions.eq("ute.ind_posta_elettr2", email).ignoreCase()));
		}

	}

	private void createCriteriaProfessione(Trl_utenti_biblioteca uteBib, Criteria c) {
		Tbl_occupazioni prof = uteBib.getId_occupazioni();
		//almaviva5_20140905 #5634
		if (prof != null && ValidazioneDati.isFilled(prof.getOccupazione()) ) {
			c.createAlias("id_occupazioni", "prof");
			c.add(Restrictions.eq("prof.professione", prof.getProfessione()));
			c.add(Restrictions.eq("prof.occupazione", prof.getOccupazione()));
			c.add(Restrictions.ne("prof.fl_canc", 'S'));
		}
	}

	private void createCriteriaTitoloStudio(Trl_utenti_biblioteca uteBib, Criteria c)	{
		Tbl_specificita_titoli_studio specTitStudio = uteBib.getId_specificita_titoli_studio();
		//almaviva5_20140905 #5634 eliminata join per specificità assente
		if (specTitStudio != null && ValidazioneDati.isFilled(specTitStudio.getSpecif_tit()) ) {
			c.createAlias("id_specificita_titoli_studio", "titStudio");
			c.add(Restrictions.eq("titStudio.tit_studio", specTitStudio.getTit_studio()));
			c.add(Restrictions.eq("titStudio.specif_tit", specTitStudio.getSpecif_tit()));
			c.add(Restrictions.ne("titStudio.fl_canc", 'S'));
		}
	}

	private void createCriteriaMateria(Trl_utenti_biblioteca uteBib, Criteria c, int idMateria)	{

		DetachedCriteria dc = DetachedCriteria.forClass(Tbl_utenti.class , "ute_mat");
		dc.setProjection(Property.forName("ute_mat.id_utenti"));
		dc.add(Restrictions.ne("ute_mat.fl_canc", 'S'));
		DetachedCriteria c2 = dc.createCriteria("Trl_materie_utenti");
		c2.add(Restrictions.ne("fl_canc", 'S'));
		c2.add(Restrictions.eq("id_materia.id", idMateria));
		c2.add(Property.forName("ute.id_utenti").eqProperty("id_utenti"));

		c.add(Subqueries.exists(dc));



		//c.add(Subqueries.in("ute.id_utenti", dc));

	}

	public Trl_utenti_biblioteca getUtenteBibliotecaById(int id_utente)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Trl_utenti_biblioteca utente = (Trl_utenti_biblioteca) loadNoLazy(session, Trl_utenti_biblioteca.class, new Integer(id_utente));
			return utente;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public int getNumUtenteBibliotecaByIdUtente(Tbl_utenti utente)
			throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Set utenti_biblioteca = utente.getTrl_utenti_biblioteca();

			Query utenti = session.createFilter(utenti_biblioteca, "select count(*) where id_utenti=:utente and fl_canc<>'S' ");
			utenti.setEntity("utente", utente);

			Number cnt = (Number)utenti.uniqueResult();
			return cnt.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}



	public String calcolaNextCodUtente(String codBib) throws DaoManagerException
	{
		try {
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("calcolaCodUtente");
			Long codUte = (Long) query.uniqueResult();
			return String.format("%2s%010d", codBib.trim(), codUte).trim();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void inserimentoUtente(Tbl_utenti utente) throws DaoManagerException {
		try {
			setSessionCurrentCfg();
			Session session = this.getCurrentSession();
			session.saveOrUpdate(utente);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void aggiornaUtente(Tbl_utenti utenti) throws DaoManagerException {
		try {
			setSessionCurrentCfg();
			Session session = this.getCurrentSession();
			session.saveOrUpdate(utenti);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}



	public void aggiornaUtenteBiblioteca(Trl_utenti_biblioteca utente_biblioteca) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(utente_biblioteca);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void inserimentoUtenteBiblioteca(Trl_utenti_biblioteca utenteBib, Tbl_utenti utente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_biblioteca_in_polo bib =
				(Tbf_biblioteca_in_polo) loadNoLazy(session, Tbf_biblioteca_in_polo.class, utenteBib.getCd_biblioteca());
			utenteBib.setCd_biblioteca(bib);
			utenteBib.setId_utenti(utente);
			//utente.getTrl_utenti_biblioteca().add(utenteBib);
			session.saveOrUpdate(utenteBib);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	public Tbl_utenti getUtenteAnagraficaById(int idUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			return (Tbl_utenti) loadNoLazy(session, Tbl_utenti.class, new Integer(idUtente));

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	public int verificaMovimentiUtente(int id_utente) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Trl_utenti_biblioteca utente = this.getUtenteBibliotecaById(id_utente);
			Set<?> tbl_richiesta_servizio = utente.getTbl_richiesta_servizio();

			// creo un filtro sulle richieste dell'utente
			//Query richieste = session.createFilter(tbl_richiesta_servizio, "select count(*) where fl_canc='N' and (cod_stato_rich is not null and cod_stato_rich in('A','C','G','I','M','N','P','R')) and (cod_stato_mov is null or cod_stato_mov in ('A','S','P'))");
			// tck 3909
			String condizioniQuery="select count(*) where fl_canc='N'";
			condizioniQuery=condizioniQuery + " and not (cod_stato_rich='H' and cod_stato_mov='C')";
			condizioniQuery=condizioniQuery + " and not (cod_stato_rich='B' and cod_stato_mov='P')";
			condizioniQuery=condizioniQuery + " and not (cod_stato_rich='D' and cod_stato_mov='C')";
			condizioniQuery=condizioniQuery + " and not (cod_stato_rich='B' and cod_stato_mov='C')";
			condizioniQuery=condizioniQuery + " and not (cod_stato_mov='E')";

			Query richieste = session.createFilter(tbl_richiesta_servizio, condizioniQuery);


			return ((Long)richieste.list().get(0)).intValue();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getNumMovimentiAttiviPerServizioUtente(int idUtenteBiblioteca, int idServizio)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			Trl_utenti_biblioteca utente = this.getUtenteBibliotecaById(idUtenteBiblioteca);
			Set<?> tbl_richiesta_servizio = utente.getTbl_richiesta_servizio();

			Query richieste = session.createFilter(tbl_richiesta_servizio,	"select count(*) where id_servizio=:idServizio and fl_canc='N' and (cod_stato_rich is not null and cod_stato_rich in('A','C','G','I','M','N','P','R')) and (cod_stato_mov is null or cod_stato_mov in ('A','S','P'))");
			richieste.setInteger("idServizio", idServizio);

			return ((Long)richieste.list().get(0)).intValue();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public boolean esistonoUtentiCon(String codPolo, String codBib, String codAutorizzazione)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		UtilitaDAO dao = new UtilitaDAO();

		try {
			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Trl_utenti_biblioteca.class);
			criteria.add(Restrictions.eq("cd_biblioteca", bib))
					.add(Restrictions.eq("cod_tipo_aut".trim(), codAutorizzazione.trim()).ignoreCase())
					.add(Restrictions.eq("fl_canc", 'N'))
					.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public boolean esistonoUtentiConOcc(String codPolo, String codBib, int idOcc)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		UtilitaDAO dao = new UtilitaDAO();

		try {
			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Trl_utenti_biblioteca.class);
			criteria.add(Restrictions.eq("cd_biblioteca", bib))
					.add(Restrictions.eq("id_occupazioni.id_occupazioni", idOcc))
					.add(Restrictions.eq("fl_canc", 'N'))
					.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public boolean esistonoUtentiConSpecTit(String codPolo, String codBib, int idSpecTit)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		UtilitaDAO dao = new UtilitaDAO();

		try {
			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Trl_utenti_biblioteca.class);
			criteria.add(Restrictions.eq("cd_biblioteca", bib))
					.add(Restrictions.eq("id_specificita_titoli_studio.id_specificita_titoli_studio", idSpecTit))
					.add(Restrictions.eq("fl_canc", 'N'))
					.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}


	public boolean isUtenteAutorizzato(String codPolo, String codBibUte,
			String codUtente, String codBib, String codTipoServ,
			String codServ, Timestamp when)	throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();
			Tbl_utenti utente = getUtente(codPolo, codBibUte, codUtente, codBib);
			if (utente == null)
				return false;

			Set<?> diritti = utente.getTrl_diritti_utente();
			// Definizione filtro
			StringBuilder filtro = new StringBuilder("select count(*) where fl_canc!='S'");
			filtro.append(" and (data_inizio_serv is null or :data >= data_inizio_serv)");
			filtro.append(" and (data_fine_serv is null or :data <= data_fine_serv)");
			/*
			filtro.append(" and ((data_inizio_sosp is null and data_fine_sosp is null) or" +
				" (data_inizio_sosp is not null and data_fine_sosp is not null and not" +
				" :data between data_inizio_sosp and data_fine_sosp))");
			*/
			filtro.append(" and id_servizio.cod_servizio = :codServ");
			filtro.append(" and id_servizio.id_tipo_servizio.cod_tipo_serv = :codTipoServ");
			filtro.append(" and id_servizio.id_tipo_servizio.cd_bib = :bib");

			Query query = session.createFilter(diritti, filtro.toString());
			query.setEntity("bib", UtilitaDAO.creaIdBib(codPolo, codBib) );
			query.setTimestamp("data", when);
			query.setString("codServ", codServ);
			query.setString("codTipoServ", codTipoServ);

			Number rowCount = (Number) query.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}


	public boolean isUtenteSospeso(String codPolo, String codBibUte,
			String codUtente, String codBib, String codTipoServ,
			String codServ, Timestamp data) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();
			Tbl_utenti utente = getUtente(codPolo, codBibUte, codUtente, codBib);
			if (utente == null)
				return false;

			Set<?> diritti = utente.getTrl_diritti_utente();
			// Definizione filtro
			StringBuilder filtro = new StringBuilder("select count(*) where fl_canc!='S'");
			filtro.append(" and (data_inizio_sosp is not null and data_fine_sosp is not null and" +
					" :data between data_inizio_sosp and data_fine_sosp)");
			filtro.append(" and id_servizio.cod_servizio = :codServ");
			filtro.append(" and id_servizio.id_tipo_servizio.cod_tipo_serv = :codTipoServ");
			filtro.append(" and id_servizio.id_tipo_servizio.cd_bib = :bib");

			Query query = session.createFilter(diritti, filtro.toString());
			query.setEntity("bib", UtilitaDAO.creaIdBib(codPolo, codBib));
			query.setTimestamp("data", data);
			query.setString("codServ", codServ);
			query.setString("codTipoServ", codTipoServ);

			Number rowCount = (Number) query.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}


	public List<Tbl_servizio> getServiziAttivi(String codPolo, String codBibUte, String codUtente,
			String codBib, Timestamp data)
	throws DaoManagerException {
		List<Trl_diritti_utente> dirittiUtenteAttivi = this.getDirittiUtenteAttivi(codPolo, codBibUte, codUtente, codBib, data);
		List<Tbl_servizio> serviziAttivi = new ArrayList<Tbl_servizio>();

		for (Trl_diritti_utente diritto : dirittiUtenteAttivi) {
			Tbl_servizio servizio = diritto.getId_servizio();
			if (!serviziAttivi.contains(servizio)) {
				serviziAttivi.add(servizio);
			}
		}

		return serviziAttivi;
	}


	public List<Trl_diritti_utente> getDirittiUtenteAttivi(String codPolo,
			String codBibUte, String codUtente, String codBib, Timestamp data)
			throws DaoManagerException {
		Tbl_utenti utente = getUtente(codPolo, codBibUte, codUtente, codBib);
		if (utente == null)
			return ValidazioneDati.emptyList();

		Set<Trl_diritti_utente> diritti = utente.getTrl_diritti_utente();

		Session session = this.getCurrentSession();
		//Definizione filtro
		StringBuilder filtro = new StringBuilder("where fl_canc<>'S' and id_servizio.id_tipo_servizio.cd_bib = :bib");
		if (data != null) {
			filtro.append(" and (data_inizio_serv is null or :data>=data_inizio_serv)")
				.append(" and (data_fine_serv is null   or :data<=data_fine_serv)")
				.append(" and ( (data_inizio_sosp is null and data_fine_sosp is null)")
				.append(" or (data_inizio_sosp is not null and data_fine_sosp is not null and not :data between data_inizio_sosp and data_fine_sosp) )");
		}
		Query query = session.createFilter(diritti, filtro.toString());
		query.setEntity("bib", creaIdBib(codPolo, codBib) );
		if (data != null) {
			query.setTimestamp("data", data);
		}

		return query.list();

	}


	public Tbl_utenti getUtente(String codPolo, String codBibUte, String codUtente, String codBib)
	throws DaoManagerException {
		Trl_utenti_biblioteca utente_biblioteca = getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);
		if (utente_biblioteca != null) {
			return utente_biblioteca.getId_utenti();
		}
		else return null;
	}


	public Tbl_utenti getUtente(String codUtente) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_utenti.class);
			criteria.add(Restrictions.eq("cod_utente", codUtente).ignoreCase())
					.add(Restrictions.ne("fl_canc", 'S'));

			Tbl_utenti utente = (Tbl_utenti) criteria.uniqueResult();
			return utente;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_utenti getUtenteByCodFiscale(String codFiscale) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_utenti.class);
			criteria.add(Restrictions.eq("cod_fiscale", codFiscale).ignoreCase())
					.add(Restrictions.ne("fl_canc", 'S'));

			Tbl_utenti utente = (Tbl_utenti) criteria.uniqueResult();
			return utente;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List getUtenteByAut(String codPolo, String codBib, String codAut, boolean del) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Tbl_utenti.class);
			if (del)
				c.add(Restrictions.eq("fl_canc", 'S'));
			else
				c.add(Restrictions.eq("fl_canc", 'N'));

			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);


			if (codAut!=null && codAut.trim().length()!=0 )
			{
				DetachedCriteria dc = DetachedCriteria.forClass(Trl_utenti_biblioteca.class , "child_ute");
				dc.setProjection(Property.forName("child_ute.id_utenti.id_utenti"));
				dc.add(Restrictions.eq("child_ute.cod_tipo_aut".trim(),codAut.trim()).ignoreCase());
				dc.add(Restrictions.eq("child_ute.fl_canc", 'N'));
				dc.add(Restrictions.eq("cd_biblioteca", bib));
				c.add(Subqueries.exists(dc));
				c.add(Property.forName("id_utenti").in( dc));
			}

			List utenti = c.list();
			return utenti;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Integer> getListaIdUtentiByAut(String codPolo, String codBib, String codAut) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Tbl_utenti.class);
			c.setProjection(Projections.property("id_utenti"));
			c.add(Restrictions.ne("fl_canc", 'S'));

			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);

			if (ValidazioneDati.isFilled(codAut) ) {
				DetachedCriteria dc = DetachedCriteria.forClass(Trl_utenti_biblioteca.class , "child_ute");
				dc.setProjection(Property.forName("child_ute.id_utenti.id_utenti"));
				dc.add(Restrictions.eq("child_ute.cod_tipo_aut".trim(),codAut.trim()).ignoreCase());
				dc.add(Restrictions.eq("child_ute.fl_canc", 'N'));
				dc.add(Restrictions.eq("cd_biblioteca", bib));
				c.add(Subqueries.exists(dc));
				c.add(Property.forName("id_utenti").in(dc));
			}

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List getIdAllUtenti(String codPolo, String codBib) throws DaoManagerException {
		List lista;
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_utenti_biblioteca.class);
			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);
			criteria.add(Restrictions.eq("cd_biblioteca", bib));
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			//criteria.setProjection(Property.forName("id_utenti.id_utenti"));
			criteria.setProjection(Property.forName("id_utenti_biblioteca"));
			lista=criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
		return lista;
	}

	public List getIdAllUtentiByAut(String codPolo, String codBib, String codAut ) throws DaoManagerException {
		List lista;
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_utenti_biblioteca.class);
			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);
			criteria.add(Restrictions.eq("cd_biblioteca", bib));
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			if (codAut.equals("*"))
			{
				criteria.add(Restrictions.or(Restrictions.isNull("cod_tipo_aut"), Restrictions.eq("cod_tipo_aut".trim(),"")));
			}
			else
			{
				criteria.add(Restrictions.eq("cod_tipo_aut",codAut.toUpperCase()).ignoreCase());
			}
			criteria.setProjection(Property.forName("id_utenti_biblioteca"));
			lista=criteria.list();
			return lista;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}



	public Trl_utenti_biblioteca getUtenteBiblioteca(String codPolo,
			String codBibUte, String codUtente, String codBib)
			throws DaoManagerException {

		Session session = this.getCurrentSession();

		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		try {
			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.add(Restrictions.eq("cd_biblioteca", bib));
			c.add(Restrictions.ne("fl_canc", 'S'));

			Criteria c2 = c.createCriteria("id_utenti");
			c2.add(Restrictions.eq("cod_utente", codUtente).ignoreCase());
			c2.add(Restrictions.ne("fl_canc", 'S'));

			// almaviva5_20111220 segnalazione TO0
			if (ValidazioneDati.isFilled(codBibUte))
				c2.add(Restrictions.eq("cd_bib_iscrizione", codBibUte));

			return (Trl_utenti_biblioteca) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_richiesta_servizio> getListaRichiestePerUtenteScadute(String codPolo, String codBibUte,
														String codUtente, String codBib, String ordinamento)
	throws DaoManagerException {
		Trl_utenti_biblioteca utenteBiblioteca = this.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			if (utenteBiblioteca!=null) {
				criteria.add(Restrictions.eq("id_utenti_biblioteca", utenteBiblioteca))
						.add(Restrictions.ne("fl_canc", 'S'))
						.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
											  				   Restrictions.in("cod_stato_rich", new String[]{"A","C","G","I","M","N","P","R"})
											 				  ),
											  Restrictions.or(Restrictions.isNull("cod_stato_mov"),
													  		  Restrictions.in("cod_stato_mov", new String[]{"A","S","P"})
													  		 )
											 )
							)
						.add(Restrictions.lt("data_fine_prev", DaoManager.now()));
				if (ordinamento!=null && !ordinamento.equals("")){
					createCriteriaOrder(ordinamento, null, criteria);
				}
				return criteria.list();
			} else return new ArrayList();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_richiesta_servizio> getListaRichiestePerUtente(String codPolo, String codBibUte,
			String codUtente, String codBib, String ordinamento, MovimentoVO filtroMov) throws DaoManagerException {
		Trl_utenti_biblioteca utenteBiblioteca = this.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);

		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_richiesta_servizio.class);
			if (utenteBiblioteca != null) {

				c.add(Restrictions.eq("id_utenti_biblioteca", utenteBiblioteca))
						.add(Restrictions.ne("fl_canc", 'S'))
						// imposto i criteri sugli stati della richiesta e del movimento
						.add(impostaFiltroStatoRicMov(filtroMov));

				if (ValidazioneDati.isFilled(ordinamento)) {
					createCriteriaOrder(ordinamento, null, c);
				}

				if (filtroMov != null) {
					if (filtroMov.isRichiestaSuInventario()) {
						Tbc_inventarioDao inventarioDAO = new Tbc_inventarioDao();
						Tbc_inventario inventario = inventarioDAO
								.getInventario(codPolo, codBib,
										filtroMov.getCodSerieInv(),
										Integer.parseInt(filtroMov.getCodInvenInv()));
						c.add(Restrictions.eq("cd_polo_inv", inventario));

					} else if (filtroMov.isRichiestaSuSegnatura()) {
						Tbl_documenti_lettoriDAO documentiDAO = new Tbl_documenti_lettoriDAO();
						Tbl_esemplare_documento_lettore esDocLett = documentiDAO
								.getEsemplareDocumentoLettore(codPolo,
										filtroMov.getCodBibDocLett(),
										filtroMov.getTipoDocLett(),
										Long.parseLong(filtroMov.getCodDocLet()),
										Short.parseShort(filtroMov.getProgrEsempDocLet()));
						c.add(Restrictions.eq("id_esemplare_documenti_lettore",	esDocLett));

					}

				}

				return c.list();

			} else
				return new ArrayList<Tbl_richiesta_servizio>();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getNumeroRichiestePerUtenteTipoServizio(String codPolo, String codBibUte,
			String codUtente, String codBib, MovimentoVO filtroMov) throws DaoManagerException {

		Trl_utenti_biblioteca utenteBib = this.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);

		Session session = this.getCurrentSession();
		try {
			if (utenteBib == null)
				return 0;

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_utenti_biblioteca", utenteBib))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.in("cod_stato_rich", new String[]{"A","C","G","I","M","N","P","R","S"}))
					.setProjection(Projections.rowCount());

			//criterio su tipo servizio
			Criteria c2 = criteria.createCriteria("id_servizio");
			c2.add(Restrictions.ne("fl_canc", 'S'));
			Criteria c3 = c2.createCriteria("id_tipo_servizio");
			c3.add(Restrictions.eq("cod_tipo_serv", filtroMov.getCodTipoServ()));
			c3.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();


		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_richiesta_servizio> getListaMovimentiPerUtente(String codPolo, String codBibUte,
			String codUtente, String codBib, String ordinamento) throws DaoManagerException {
		Trl_utenti_biblioteca utenteBiblioteca = this.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);
		UtilitaDAO dao = new UtilitaDAO();

		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			if (utenteBiblioteca!=null) {
				criteria.add(Restrictions.eq("id_utenti_biblioteca",  utenteBiblioteca))
						.add(Restrictions.ne("fl_canc",               'S'))
						.add(Restrictions.in("cod_stato_mov",         new String[]{"A","S"}));

				if (ordinamento!=null && !ordinamento.equals("")){
					createCriteriaOrder(ordinamento, null, criteria);
				}
				return criteria.list();
			} else return new ArrayList();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getNumeroMovimentiPerUtenteTipoServizio(String codPolo, String codBibUte,
			String codUtente, String codBib, MovimentoVO filtroMov) throws DaoManagerException {

		Trl_utenti_biblioteca utenteBib = this.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);

		Session session = this.getCurrentSession();
		try {
			if (utenteBib == null)
				return 0;

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_utenti_biblioteca",  utenteBib))
					.add(Restrictions.ne("fl_canc",               'S'))
					.add(Restrictions.in("cod_stato_mov",         new String[]{"A","S"}))
					.add(Restrictions.ne("cod_stato_rich",        'H'))	//non conclusa
			  		.setProjection(Projections.rowCount());

			//criterio su tipo servizio
			Criteria c2 = criteria.createCriteria("id_servizio");
			c2.add(Restrictions.ne("fl_canc", 'S'));
			Criteria c3 = c2.createCriteria("id_tipo_servizio");
			c3.add(Restrictions.eq("cod_tipo_serv", filtroMov.getCodTipoServ()));
			c3.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_richiesta_servizio> getListaMovimentiPerUtenteTipoServizio(String codPolo, String codBibUte,
			String codUtente, String codBib, MovimentoVO filtroMov) throws DaoManagerException {

		Trl_utenti_biblioteca utenteBib = this.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);

		Session session = this.getCurrentSession();
		try {
			if (utenteBib == null)
				return new ArrayList<Tbl_richiesta_servizio>();

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_utenti_biblioteca",  utenteBib))
					.add(Restrictions.ne("fl_canc",               'S'))
					.add(Restrictions.in("cod_stato_mov",         new String[]{"A","S"}))
					.add(Restrictions.ne("cod_stato_rich",        'H'));	//non conclusa

			//criterio su tipo servizio
			Criteria c2 = criteria.createCriteria("id_servizio");
			c2.add(Restrictions.ne("fl_canc", 'S'));
			Criteria c3 = c2.createCriteria("id_tipo_servizio");
			c3.add(Restrictions.eq("cod_tipo_serv", filtroMov.getCodTipoServ()));
			c3.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List getListaDirittiUtente(int id_utente) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Trl_utenti_biblioteca utente = this.getUtenteBibliotecaById(id_utente);
			Set diritti_utente = utente.getId_utenti().getTrl_diritti_utente();
			// creo un filtro sui diritti della biblioteca a cui è iscritto
			// l'utente
			Query diritti = session.createFilter(
					diritti_utente,
					"where fl_canc<>'S' and"
							+ " id_servizio.id_tipo_servizio.cd_bib = :bib")
					.setEntity("bib", utente.getCd_biblioteca());
			return diritti.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}




	public void inserisciDirittoUtente(Tbl_utenti utente, Tbl_servizio servizio,
			Trl_diritti_utente diritto_utente) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();
			diritto_utente.setId_utenti(utente);
			diritto_utente.setId_servizio(servizio);
			if (diritto_utente.getData_inizio_serv() == null) {
				diritto_utente.setData_inizio_serv(DaoManager.now());
			}
			if (diritto_utente.getData_fine_serv() == null) {
				diritto_utente.setData_fine_serv(DateUtil.addYear(diritto_utente.getData_inizio_serv(), 1));
			}
			session.saveOrUpdate(diritto_utente);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Trl_diritti_utente getDirittoUtenteBiblioteca(String codPolo, String codBibUte, String codUtente, String codBib,
											String codTipoServ, String codServ)
	throws DaoManagerException {
		try {
			Tbl_utenti utente     = this.getUtente(codPolo, codBibUte, codUtente, codBib);
			Tbl_servizio servizio = serviziDAO.getServizioBiblioteca(codPolo, codBib, codTipoServ, codServ);

			return this.getDirittoUtenteBiblioteca(utente, servizio);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Trl_diritti_utente getDirittoUtenteBiblioteca(Tbl_utenti id_utenti, Tbl_servizio servizio) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_diritti_utente.class);
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			criteria.add(Restrictions.eq("id_utenti", id_utenti));
			criteria.add(Restrictions.eq("id_servizio", servizio));

			return (Trl_diritti_utente) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public int cancellaTuttiDirittiUtenteBiblioteca(Trl_utenti_biblioteca utenteBib) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo cd_biblioteca = utenteBib.getCd_biblioteca();
			Tbl_utenti id_utenti = utenteBib.getId_utenti();
			String sqlString="update versioned Trl_diritti_utente u set u.fl_canc='S' where u.id_utenti=:ute " +
					"and u.id_servizio.id_tipo_servizio.cd_bib=:bib";
			Query query = session.createQuery(sqlString);
			query.setEntity("ute", id_utenti);
			query.setEntity("bib", cd_biblioteca);
			return query.executeUpdate();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public Trl_diritti_utente verificaEsistenzaDirittoUtenteBiblioteca(Tbl_utenti id_utenti, Tbl_servizio servizio) throws DaoManagerException {
		try {
			// cerca il diritto legato all'utente anche se cancellato logicamente
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_diritti_utente.class);
			criteria.add(Restrictions.eq("id_utenti", id_utenti));
			criteria.add(Restrictions.eq("id_servizio", servizio));
			Trl_diritti_utente diritto = (Trl_diritti_utente) criteria.uniqueResult();
			return diritto;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public Trl_diritti_utente verificaEsistenzaServizioDirittoUtenteBiblioteca(Tbl_utenti id_utenti, Tbl_servizio servizio) throws DaoManagerException {
		try {
			// cerca il diritto legato all'utente appartenente alla stessa famiglia del servizio del diritto passato
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_diritti_utente.class);
			criteria.add(Restrictions.eq("id_utenti", id_utenti));
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			criteria.add(Restrictions.or(Restrictions.isNull("cod_tipo_aut"), Restrictions.eq("cod_tipo_aut".trim(),"")));
			criteria.createCriteria("id_servizio").add(Restrictions.eq("id_tipo_servizio.id_tipo_servizio", servizio.getId_tipo_servizio().getId_tipo_servizio()));
			Trl_diritti_utente diritto = (Trl_diritti_utente) criteria.uniqueResult();
			return diritto;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List <Trl_diritti_utente> verificaEsistenzaServizioDirittoUtenteBibliotecaSameFamily(Tbf_biblioteca_in_polo bib, int idDir, String codAut, int tipoServ) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Trl_diritti_utente.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.ne("id_servizio_id_servizio", idDir));
			c.add(Restrictions.or(Restrictions.isNull("cod_tipo_aut"), Restrictions.eq("cod_tipo_aut".trim(),"")));

			if (ValidazioneDati.isFilled(codAut) ) {
				DetachedCriteria dcUte = DetachedCriteria.forClass(Trl_utenti_biblioteca.class , "child_ute");
				dcUte.setProjection(Property.forName("child_ute.id_utenti.id_utenti"));
				dcUte.add(Restrictions.eq("child_ute.cod_tipo_aut".trim(),codAut.trim()).ignoreCase());
				dcUte.add(Restrictions.eq("child_ute.fl_canc", 'N'));
				dcUte.add(Restrictions.eq("cd_biblioteca", bib));
				c.add(Subqueries.exists(dcUte));
				c.add(Property.forName("id_utenti_id_utenti").in(dcUte));
			}

			c.createCriteria("id_servizio").add(Restrictions.eq("id_tipo_servizio.id_tipo_servizio", tipoServ));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}


	public void aggiornaDirittoUtente(Trl_diritti_utente diritto) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(diritto);
			session.flush();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Integer> ricercaUtentiLike(Trl_utenti_biblioteca uteBib, String tipoOrd, RicercaUtenteBibliotecaVO ricerca)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			//uteBib.setFl_canc('N');
			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.setProjection(Projections.property("id_utenti_biblioteca"));

			boolean ricercaSuPolo = ricerca.isRicercaUtentePolo();
			if (!ricercaSuPolo )	{
				c.add(Restrictions.eq("cd_biblioteca", uteBib.getCd_biblioteca()));
				c.add(Restrictions.ne("fl_canc", 'S'));
			}

			if (ValidazioneDati.isFilled(uteBib.getCod_tipo_aut()) )
				c.add(Restrictions.eq("cod_tipo_aut".trim(), uteBib.getCod_tipo_aut().trim()).ignoreCase());

			this.createRicercaUtentiExample(uteBib, "ute", c, ricercaSuPolo, "like");

			// aggiungo criterio su email
			this.createCriteriaEmail(uteBib, c);

			// aggiungo criterio su matricola
			this.createCriteriaMatricola(uteBib, c);

			// aggiungo criterio su professione
			this.createCriteriaProfessione(uteBib, c);

			// aggiungo criterio su titolo di studio
			this.createCriteriaTitoloStudio(uteBib, c);

			//almaviva5_20120229 #4682 materia
			String mat = ricerca.getMateria();
			if (ValidazioneDati.isFilled(mat)) {
				if (ValidazioneDati.strIsNumeric(mat)) {
					int idMateria = Integer.valueOf(mat);
					if (idMateria > 0)
						this.createCriteriaMateria(uteBib, c, idMateria);
				}
			}

			//almaviva5_20120301 #4682 range date
			this.createCriteriaDate(c, ricerca);

			// aggiungo tipo ordinamento richiesto
			if (ValidazioneDati.isFilled(tipoOrd) )
				createCriteriaOrderIC(tipoOrd, "ute", c);

			return c.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Integer> ricercaUtentiParole(Trl_utenti_biblioteca uteBib, String parole, String tipoOrd, RicercaUtenteBibliotecaVO ricerca)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			//uteBib.setFl_canc('N');

			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.setProjection(Projections.property("id_utenti_biblioteca"));
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			boolean ricercaSuPolo = ricerca.isRicercaUtentePolo();
			if (!ricercaSuPolo)
				c.add(Restrictions.eq("cd_biblioteca", uteBib.getCd_biblioteca()));

			c.add(Restrictions.ne("fl_canc", 'S'));
			if (ValidazioneDati.isFilled(uteBib.getCod_tipo_aut()) )
				c.add(Restrictions.eq("cod_tipo_aut".trim(), uteBib.getCod_tipo_aut().trim()).ignoreCase());

			if (ValidazioneDati.isFilled(parole) ) {
				DetachedCriteria childCriteriaUte = DetachedCriteria.forClass(Tbl_utenti.class , "child_ute");
				childCriteriaUte.setProjection(Property.forName("child_ute.id_utenti"));
				//childCriteriaUte.add(Restrictions.eq("child_ute.persona_giuridica", 'S')); // LA RICERCA PER PAROLE SI APPLICA SOLO AGLI ENTI
				childCriteriaUte.add(Restrictions.ne("child_ute.fl_canc", 'S'));

				childCriteriaUte.add(ricercaPerParoleAND("tidx_vector", parole));

				//c.add(Subqueries.exists(childCriteriaUte));
				c.add(Property.forName("id_utenti.id_utenti").in(childCriteriaUte));
				// aggiungo tipo ordinamento fisso
				//c.createCriteria("id_utenti").addOrder(Order.asc("cognome").ignoreCase());
				//.addOrder(Order.asc("nome").ignoreCase());

			}

			this.createRicercaUtentiExample(uteBib, "ute", c, ricercaSuPolo, "parole");

			// aggiungo criterio su professione
			this.createCriteriaProfessione(uteBib, c);

			// aggiungo criterio su titolo di studio
			this.createCriteriaTitoloStudio(uteBib, c);

			//almaviva5_20120229 #4682 materia
			String mat = ricerca.getMateria();
			if (ValidazioneDati.isFilled(mat)) {
				if (ValidazioneDati.strIsNumeric(mat)) {
					int idMateria = Integer.valueOf(mat);
					if (idMateria > 0)
						this.createCriteriaMateria(uteBib, c, idMateria);
				}
			}

			//almaviva5_20120301 #4682 range date
			this.createCriteriaDate(c, ricerca);

			// aggiungo tipo ordinamento richiesto
			if (ValidazioneDati.isFilled(tipoOrd) )
				createCriteriaOrder(tipoOrd, "ute", c);

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Integer> ricercaUtentiExact(Trl_utenti_biblioteca uteBib, String tipoOrd, RicercaUtenteBibliotecaVO ricerca)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.setProjection(Projections.property("id_utenti_biblioteca"));
			boolean ricercaSuPolo = ricerca.isRicercaUtentePolo();
			if (!ricercaSuPolo)
				c.add(Restrictions.eq("cd_biblioteca", uteBib.getCd_biblioteca()));

			c.add(Restrictions.ne("fl_canc", 'S'));
			if (ValidazioneDati.isFilled(uteBib.getCod_tipo_aut()))
				c.add(Restrictions.eq("cod_tipo_aut".trim(), uteBib.getCod_tipo_aut().trim()).ignoreCase());

			uteBib.setFl_canc('N');
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			this.createRicercaUtentiExample(uteBib, "ute", c, ricercaSuPolo, "");

			// aggiungo criterio su email
			this.createCriteriaEmail(uteBib, c);


			// aggiungo criterio su professione
			this.createCriteriaProfessione(uteBib, c);

			// aggiungo criterio su titolo di studio
			this.createCriteriaTitoloStudio(uteBib, c);

			//almaviva5_20120229 #4682 materia
			String mat = ricerca.getMateria();
			if (ValidazioneDati.isFilled(mat)) {
				if (ValidazioneDati.strIsNumeric(mat)) {
					int idMateria = Integer.valueOf(mat);
					if (idMateria > 0)
						this.createCriteriaMateria(uteBib, c, idMateria);
				}
			}

			//almaviva5_20120301 #4682 range date
			this.createCriteriaDate(c, ricerca);

			// aggiungo tipo ordinamento richiesto
			if (ValidazioneDati.isFilled(tipoOrd) )
				createCriteriaOrder(tipoOrd, "ute", c);

			return c.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	private void createCriteriaDate(Criteria c,	RicercaUtenteBibliotecaVO ricerca) {

		// data aut
		DaoManager.addDateRange(c, "data_fine_aut",
				DateUtil.toDate(ricerca.getDataFineAut()),
				DateUtil.toDate(ricerca.getDataFineAutA()));

		// data nascita
		DaoManager.addDateRange(c, "ute.data_nascita",
				DateUtil.toDate(ricerca.getDataNascita()),
				DateUtil.toDate(ricerca.getDataNascitaA()));

		//almaviva5_20120506 #4872 la data di nascita esclude gli utenti di tipo ente
		if (ValidazioneDati.isFilled(ricerca.getDataNascita())
				|| ValidazioneDati.isFilled(ricerca.getDataNascitaA()) )
			c.add(Restrictions.ne("ute.persona_giuridica", 'S'));
	}

	public List<Integer> ricercaUtentiPolo(Trl_utenti_biblioteca uteBib, String tipoOrd)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.setProjection(Projections.property("id_utenti_biblioteca"));
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.add(Restrictions.ne("fl_canc", 'S'));
			uteBib.setFl_canc('N');
			createRicercaUtentiExample(uteBib, "ute", c, true, "polo");

			// aggiungo criterio su email
			this.createCriteriaEmail(uteBib, c);

			// aggiungo criterio su matricola
			this.createCriteriaMatricola(uteBib, c);

			if (ValidazioneDati.isFilled(tipoOrd) )
				createCriteriaOrder(tipoOrd, "ute", c);

			return c.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_utenti> ricercaUtentiPoloChiaveUte(Tbl_utenti ute, String tipoRic,  Boolean ricercaSuPolo)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_utenti.class);
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.add(Restrictions.ne("fl_canc", 'S'));
			ute.setFl_canc('N');
			Example example = Example.create(ute);
			example.ignoreCase();
			example.setPropertySelector(NOT_EMPTY);
			example.excludeProperty("sesso");
			if (ricercaSuPolo) {
				example.excludeProperty("cd_bib");
				//almaviva5_20111220 segnalazione TO0
				example.excludeProperty("cd_bib_iscrizione");
			}
			if (ValidazioneDati.isFilled(tipoRic))
			{
				if (tipoRic.equals(RicercaUtenteBibliotecaVO.RICERCA_INIZIO))
				{
					example.enableLike(MatchMode.START);
				} else	if (tipoRic.equals(RicercaUtenteBibliotecaVO.RICERCA_PAROLE))
				{
					example.enableLike(MatchMode.ANYWHERE);
				} else	if (tipoRic.equals(RicercaUtenteBibliotecaVO.RICERCA_ESATTO))
				{
					example.enableLike(MatchMode.EXACT);
				}
			}
			c.add(example);
			return c.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}



	public List<Tbl_utenti> ricercaUtenteBasePolo(int idute, String codUte,
			String codFisc, String tipoOrd, String email, String ateneo,
			String matricola) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_utenti.class);
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			//c.setMaxResults(1);


			if (idute > 0)
				c.add(Restrictions.eq("id_utenti", idute));

			if (ValidazioneDati.isFilled(codUte))
				c.add(Restrictions.eq("cod_utente", codUte).ignoreCase());

			if (ValidazioneDati.isFilled(codFisc))
				c.add(Restrictions.eq("cod_fiscale", codFisc.trim()).ignoreCase());
			else
				//almaviva5_20110427 #4398
				c.add(Restrictions.ne("fl_canc", 'S'));

			if (ValidazioneDati.isFilled(email) ) {
				//almaviva5_20150428 doppia mail
				email = email.trim();
				c.add(Restrictions.or(Restrictions.eq("ind_posta_elettr", email).ignoreCase(),
						Restrictions.eq("ind_posta_elettr2", email).ignoreCase()) );
			}

			if (ValidazioneDati.isFilled(ateneo))
				c.add(Restrictions.eq("cod_ateneo", ateneo.trim()));

			if (ValidazioneDati.isFilled(matricola))
				c.add(Restrictions.eq("cod_matricola", matricola.trim()));

			if (ValidazioneDati.isFilled(tipoOrd))
				createCriteriaOrder(tipoOrd, "", c);

			//return (Tbl_utenti)c.uniqueResult();
			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Trl_utenti_biblioteca> ricercaLocalizzazioneCancellata(int idUtente)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.add(Restrictions.eq("fl_canc", 'S'));
			c.add(Restrictions.eq("id_utenti.id_utenti", idUtente));
			//uteBib.setFl_canc('N');
			//Example example = this.createRicercaUtentiExample(uteBib, "ute", criteria);
			//example.enableLike(MatchMode.START);
			return c.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	/**
	 * @param utente  istanza di Tbl_utenti
	 * @return
	 * @throws DaoManagerException
	 */
	public List<Integer> ricercaUtentiPolo(Tbl_utenti utente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.setProjection(Projections.property("id_utenti_biblioteca"));

			if (utente.getId_utenti() > 0)
			{
				c.add(Restrictions.ne("fl_canc", 'S'))
				.createCriteria("id_utenti")
				.add(Restrictions.eq("id_utenti",  utente.getId_utenti()))
				.add(Restrictions.eq("cod_utente",  wrap(utente.getCod_utente())).ignoreCase())
				.add(Restrictions.eq("cod_fiscale",  wrap(utente.getCod_fiscale())).ignoreCase())
				.add(Restrictions.ne("fl_canc",      'S'));
			}
			else if (isFilled(utente.getCod_utente()) )
			{
				c.add(Restrictions.ne("fl_canc", 'S'))
				.createCriteria("id_utenti")
				.add(Restrictions.eq("cod_utente",  utente.getCod_utente()).ignoreCase())
				.add(Restrictions.eq("cod_fiscale",  wrap(utente.getCod_fiscale())).ignoreCase())
				.add(Restrictions.ne("fl_canc",      'S'));
			}
			else if (isFilled(utente.getCod_fiscale()) )
			{
				c.add(Restrictions.ne("fl_canc", 'S'))
				.createCriteria("id_utenti")
				.add(Restrictions.eq("cod_fiscale",  utente.getCod_fiscale()).ignoreCase())
				.add(Restrictions.ne("fl_canc",      'S'));
			}
			else if (ValidazioneDati.isFilled(utente.getInd_posta_elettr()) )
			{
				// almaviva5_20150428 doppia mail
				String email = utente.getInd_posta_elettr().trim();
				c.add(Restrictions.ne("fl_canc", 'S'))
				.createCriteria("id_utenti")
				.add(Restrictions.or(Restrictions.eq("ind_posta_elettr", email).ignoreCase(),
						Restrictions.eq("ind_posta_elettr2", email).ignoreCase()))
				.add(Restrictions.ne("fl_canc",      'S'));
			}
			else if (utente.getPersona_giuridica()=='S' &&  isFilled(utente.getCognome()) )
			{
				c.add(Restrictions.ne("fl_canc", 'S'))
				.createCriteria("id_utenti")
				.add(Restrictions.eq("cognome",  utente.getCognome()).ignoreCase())
				.add(Restrictions.ne("fl_canc",      'S'));
			}
			else
			{
				c.add(Restrictions.ne("fl_canc", 'S'))
				.createCriteria("id_utenti")
				.add(Restrictions.ne("fl_canc",      'S'));
			}
			List<Integer> utenti = c.list();

			return utenti;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Trl_utenti_biblioteca getBibliotecaImportata(String codPolo, String codBib, String codPoloImp, String codBibImp)
	throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bibOpe = dao.getBibliotecaInPolo(codPolo, codBib);
			//Tbf_biblioteca_in_polo bibImportata = dao.getBibliotecaInPolo(codPoloImp, codBibImp);

			Criteria criteria = session.createCriteria(Trl_utenti_biblioteca.class);
			criteria.add(Restrictions.eq("cd_biblioteca", bibOpe))
					//.add(Restrictions.ne("fl_canc", 'S'))
					.createCriteria("id_utenti")
							.add(Restrictions.eq("cod_polo_bib", codPoloImp))
							.add(Restrictions.eq("cod_bib",      codBibImp));
							//.add(Restrictions.ne("fl_canc",      'S'));

			return (Trl_utenti_biblioteca) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	//Massimilano almaviva Metodo "recuperoPassword"updateUtentiBib
	public boolean updateUtentiBib(Trl_utenti_biblioteca uteBib) throws DaoManagerException {
		try {
			setSessionCurrentCfg();
			Session session = this.getCurrentSession();
			session.saveOrUpdate(uteBib);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return true;
	}


	//
	//Massimilano almaviva Metodo
	public Tbl_utenti esistenzaUtenteWeb(String codfiscale, String mail) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			//
			Criteria c = session.createCriteria(Tbl_utenti.class);
			c.add(Restrictions.ne("fl_canc", 'S'));

			if (!ValidazioneDati.isFilled(mail))
				c.add(Restrictions.eq("cod_fiscale", codfiscale).ignoreCase());

			if (!ValidazioneDati.isFilled(codfiscale))
				// almaviva5_20150428 doppia mail
				c.add(Restrictions.or(Restrictions.eq("ind_posta_elettr", mail).ignoreCase(),
						Restrictions.eq("ind_posta_elettr2", mail).ignoreCase()));

			Tbl_utenti utente = (Tbl_utenti) c.uniqueResult();

			return utente;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	//Massimilano almaviva Metodo "listaBiblio"
	public List listaBiblioIscritto(String cdpolo, Integer codUte) throws DaoManagerException {
		//
		Session session = this.getCurrentSession();
		try {
			/*
			//inserire
			String sql = "select DISTINCT(c.cd_bib), a.nom_biblioteca, e.id_utenti_biblioteca  from tbf_biblioteca a " +
			"inner join tbf_biblioteca_in_polo b on a.id_biblioteca=b.id_biblioteca "  +
			"inner join  tbl_parametri_biblioteca c on a.cd_bib = c.cd_bib " +
			"inner join  tbl_tipi_autorizzazioni  d on a.cd_bib = d.cd_bib " +
			"inner join  trl_utenti_biblioteca  e on d.cd_bib = e.cd_biblioteca " +
			"where a.cd_polo =:polo " +
			"and e.id_utenti =:codUte " +
			"and a.fl_canc <> 'S' " +
			"and b. fl_canc <> 'S' " +
			"and d.fl_canc <> 'S' " +
			"and e.fl_canc <> 'S' " +
			"and c.ammessa_autoregistrazione_utente = 'S' " +
			"and d.flag_aut = '*' " ;
			*/

			StringBuilder sql = new StringBuilder();
			sql.append("select DISTINCT(uteBib.cd_biblioteca), bib.nom_biblioteca, uteBib.id_utenti_biblioteca from tbf_biblioteca bib ");
			sql.append("inner join tbf_biblioteca_in_polo bibPolo on bibPolo.id_biblioteca=bib.id_biblioteca ");
			sql.append("inner join trl_utenti_biblioteca uteBib on uteBib.cd_biblioteca = bib.cd_bib ");
			sql.append("where bib.cd_polo =:polo ");
			sql.append("and uteBib.id_utenti =:codUte ");
			sql.append("and bib.fl_canc<>'S' ");
			sql.append("and bibPolo.fl_canc<>'S' ");
			sql.append("and uteBib.fl_canc<>'S' ");

			SQLQuery query = session.createSQLQuery(sql.toString());
			query.setString("polo", cdpolo);
			query.setInteger("codUte", codUte);
			query.addScalar("cd_biblioteca", Hibernate.STRING);
			query.addScalar("nom_biblioteca", Hibernate.STRING);
			query.addScalar("id_utenti_biblioteca", Hibernate.INTEGER);
			//
			return query.list();
			//
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List getListaBibAutoregistrazione(String cdpolo) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {

			//inserire
			String sql = "select DISTINCT c.cd_bib, a.nom_biblioteca from  tbf_biblioteca a " +
			"INNER JOIN tbf_biblioteca_in_polo b on a.id_biblioteca=b.id_biblioteca "  +
			"inner join  tbl_parametri_biblioteca c on a.cd_bib = c.cd_bib " +
			"inner join  tbl_tipi_autorizzazioni  d on a.cd_bib = d.cd_bib " +
			"where a.cd_polo =:polo " +
			"and a.fl_canc <> 'S' " +
			"and b. fl_canc <> 'S' " +
			"and d.fl_canc <> 'S' " +
			"and c.ammessa_autoregistrazione_utente = 'S' " +
			"and d.flag_aut = '*' " +
			"order by  c.cd_bib";
			//
			SQLQuery query = session.createSQLQuery(sql);
			query.setString("polo", cdpolo);
			query.addScalar("cd_bib", Hibernate.STRING);
			query.addScalar("nom_biblioteca", Hibernate.STRING);

			//
			return query.list();
			//
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}
	//
	//Massimilano almaviva Metodo "listaBiblio"

	public List listaBiblioAuto(String cdpolo, Integer codUte, List cdBib) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			//select se iscritto ma cancellato
			String sql = "select DISTINCT(c.cd_bib), a.nom_biblioteca, e.id_utenti_biblioteca from tbf_biblioteca a " +
			"INNER JOIN tbf_biblioteca_in_polo b on a.id_biblioteca=b.id_biblioteca "  +
			"inner join  tbl_parametri_biblioteca c on a.cd_bib = c.cd_bib " +
			"inner join  tbl_tipi_autorizzazioni  d on a.cd_bib = d.cd_bib " +
			"inner join  trl_utenti_biblioteca  e on d.cd_bib = e.cd_biblioteca " +
			"where a.cd_polo =:polo " +
			"and e.id_utenti =:codUte AND e.fl_canc = 'S'" +
			"and e.cd_biblioteca not in(:cdBib) " +
			"and a.fl_canc <> 'S' " +
			"and b. fl_canc <> 'S' " +
			"and d.fl_canc <> 'S' " +
			"and c.ammessa_autoregistrazione_utente = 'S' " +
			"and d.flag_aut = '*' " ;
			//
			SQLQuery query = session.createSQLQuery(sql);
			query.setString("polo", cdpolo);
			query.setInteger("codUte", codUte);
			query.setParameterList("cdBib", cdBib);
			query.addScalar("cd_bib", Hibernate.STRING);
			query.addScalar("nom_biblioteca", Hibernate.STRING);
			query.addScalar("id_utenti_biblioteca", Hibernate.INTEGER);
			//
			return query.list();
			//
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

//Massimilano almaviva Metodo "listaBiblio"

	public List listaBiblioNonIscr(String cdpolo, Integer codUte, List cdBib) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			//select se non iscritto
			String sql = "Select DISTINCT(c.cd_bib), a.nom_biblioteca from tbf_biblioteca a " +
			"INNER JOIN tbf_biblioteca_in_polo b on a.id_biblioteca=b.id_biblioteca " +
			"inner join  tbl_parametri_biblioteca c on a.cd_bib = c.cd_bib " +
			"inner join  tbl_tipi_autorizzazioni  d on a.cd_bib = d.cd_bib " +
			"inner join  trl_utenti_biblioteca  e on d.cd_bib = e.cd_biblioteca " +
			"where a.cd_polo =:polo  " +
			"and e.cd_biblioteca not in(:cdBib) " +
			"and e.id_utenti <>:codUte " +
            "and e.fl_canc <> 'S' " +
            "and a.fl_canc <> 'S'  " +
			"and b. fl_canc <> 'S' " +
			"and d.fl_canc <> 'S' " +
			"and c.ammessa_autoregistrazione_utente = 'S' " +
			"and d.flag_aut = '*' " ;

			SQLQuery query = session.createSQLQuery(sql);
			query.setString("polo", cdpolo);
			query.setParameterList("cdBib", cdBib);
			query.setInteger("codUte", codUte);
			query.addScalar("cd_bib", Hibernate.STRING);
			query.addScalar("nom_biblioteca", Hibernate.STRING);
			//
			return query.list();
			//
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	//Massimilano almaviva 11/2009
	public List controlloBibRicOpac(String polo, Integer codUte,String cdbib) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			String sql = "select a.id_utenti_biblioteca, a.cd_biblioteca, b.nom_biblioteca  from trl_utenti_biblioteca  a, tbf_biblioteca b " +
			"where a.cd_polo =:polo " +
			"and a.id_utenti =:codUte " +
			"and a.cd_biblioteca =:cdbib " +
			"and a.cd_polo = b.cd_polo " +
			"and a.cd_biblioteca = b.cd_bib " +
			"and a.fl_canc <> 'S' "  +
			"and b.fl_canc <> 'S' " ;

			SQLQuery query = session.createSQLQuery(sql);
			query.setString("polo", polo);
			query.setInteger("codUte", codUte);
			query.setString("cdbib", cdbib);
			query.addScalar("id_utenti_biblioteca", Hibernate.INTEGER);
			query.addScalar("cd_biblioteca", Hibernate.STRING);
			query.addScalar("nom_biblioteca", Hibernate.STRING);
			//
			return query.list();
			//
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean checkEsistenzaUtente(String codfiscale, String mail, String[] ateneo_mat,
			String idUte) throws DaoManagerException {

		Session session = this.getCurrentSession();

		try {
			Criteria c = session.createCriteria(Tbl_utenti.class);
			c.setProjection(Projections.rowCount());
			//almaviva5_20110427 #4398
			//c.add(Restrictions.ne("fl_canc", 'S'));

			if (ValidazioneDati.isFilled(codfiscale) )
				c.add(Restrictions.eq("cod_fiscale", codfiscale).ignoreCase());

			if (ValidazioneDati.isFilled(mail) )
				// almaviva5_20150428 doppia mail
				c.add(Restrictions.or(Restrictions.eq("ind_posta_elettr", mail).ignoreCase(),
						Restrictions.eq("ind_posta_elettr2", mail).ignoreCase()));

			if (ValidazioneDati.isFilled(idUte) )
				c.add(Restrictions.ne("id", new Integer(idUte)));

			//almaviva5_20110428 #4400
			if (ateneo_mat != null) {
				c.add(Restrictions.eq("cod_ateneo", ateneo_mat[0]).ignoreCase());
				c.add(Restrictions.eq("cod_matricola", ateneo_mat[1]).ignoreCase());
			}

			Number cnt = (Number) c.uniqueResult();
			return (cnt.intValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<String> getListaAltreBibPerAutoregistrazione(String codPolo, int idUtente) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			//select se non iscritto
			String sql = "select DISTINCT(bp.cd_biblioteca) " +
					"from tbf_biblioteca_in_polo bp " +
					"inner join tbl_parametri_biblioteca param on param.cd_bib=bp.cd_biblioteca " +
					"inner join tbl_tipi_autorizzazioni aut on aut.cd_bib = bp.cd_biblioteca " +
					"where bp.cd_polo=:polo and bp.cd_biblioteca not in (' __') " +
					"and param.ammessa_autoregistrazione_utente = 'S' " +
					"and aut.flag_aut = '*' and aut.fl_canc<>'S' " +
					"and not exists (select ub.id_utenti_biblioteca  " +
					"from trl_utenti_biblioteca ub where ub.id_utenti=:ute " +
					"and ub.fl_canc<>'S' and ub.cd_polo=bp.cd_polo and ub.cd_biblioteca=bp.cd_biblioteca)";

			SQLQuery query = session.createSQLQuery(sql);
			query.setString("polo", codPolo);
			query.setInteger("ute", idUtente);
			query.addScalar("cd_biblioteca", Hibernate.STRING);

			return query.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_utenti> checkMailUtente(String idUte, String mail1, String mail2) throws DaoManagerException {

		Session session = this.getCurrentSession();

		try {
			Criteria c = session.createCriteria(Tbl_utenti.class);
			c.add(Restrictions.ne("fl_canc", 'S'));

			if (ValidazioneDati.isFilled(mail1) )
				c.add(Restrictions.eq("ind_posta_elettr", mail1).ignoreCase());

			if (ValidazioneDati.isFilled(mail2) )
				// almaviva5_20150428 doppia mail
				c.add(Restrictions.eq("ind_posta_elettr2", mail2).ignoreCase());

			if (ValidazioneDati.isFilled(idUte) )
				c.add(Restrictions.ne("id", new Integer(idUte)));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaDirittoUtenteBiblioteca(int idUtente, int idServizio,
			String firmaUtente) throws DaoManagerException {

		Session session = this.getCurrentSession();

		try {
			Criteria c = session.createCriteria(Trl_diritti_utente.class);
			c.add(Restrictions.ne("fl_canc", 'S'));

			if (ValidazioneDati.isFilled(idUtente) )
				c.add(Restrictions.eq("id_utenti.id_utenti", idUtente));

			if (ValidazioneDati.isFilled(idServizio) )
				c.add(Restrictions.eq("id_servizio.id_servizio", idServizio));

			List<Trl_diritti_utente> diritti = c.list();
			int size = ValidazioneDati.size(diritti);
			for (int d = 0; d < size; d++) {
				Trl_diritti_utente diritto = diritti.get(d);
				diritto.setFl_canc('S');
				diritto.setUte_var(firmaUtente);
				session.update(diritto);

				if ((d % 20) == 0)
					session.flush();
			}

			session.flush();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Trl_utenti_biblioteca getUtenteBibByCodFiscale(String codPolo, String codBibUte, String codFiscale,
			String codBib) throws DaoManagerException {

		Session session = this.getCurrentSession();

		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		try {
			Criteria c = session.createCriteria(Trl_utenti_biblioteca.class);
			c.add(Restrictions.eq("cd_biblioteca", bib));
			c.add(Restrictions.ne("fl_canc", 'S'));

			Criteria c2 = c.createCriteria("id_utenti");
			c2.add(Restrictions.eq("cod_fiscale", codFiscale).ignoreCase());
			c2.add(Restrictions.ne("fl_canc", 'S'));

			// almaviva5_20111220 segnalazione TO0
			if (ValidazioneDati.isFilled(codBibUte))
				c2.add(Restrictions.eq("cd_bib_iscrizione", codBibUte));

			return (Trl_utenti_biblioteca) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_utenti getUtenteByIsil(String isil) throws DaoManagerException {
		//almaviva5_20151109 servizi ill
		Session session = this.getCurrentSession();

		try {
			Criteria c = session.createCriteria(Tbl_utenti.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.eq("persona_giuridica", 'S'));

			String[] tokens = isil.split("\\u002D");
			switch (tokens.length) {
			case 1:	//senza codice
				c.add(Restrictions.eq("codice_anagrafe", tokens[0]).ignoreCase());
				break;
			default:
				c.add(Restrictions.eq("paese_res", tokens[0]));
				c.add(Restrictions.eq("codice_anagrafe", tokens[1]).ignoreCase());
				break;
			}

			return (Tbl_utenti) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

}
