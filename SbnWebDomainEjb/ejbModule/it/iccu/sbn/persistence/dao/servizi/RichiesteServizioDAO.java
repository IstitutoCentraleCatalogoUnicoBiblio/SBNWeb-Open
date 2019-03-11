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

import gnu.trove.THashSet;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.FiltroCollocazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_messaggio;
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_solleciti;
import it.iccu.sbn.polo.orm.servizi.Tbl_storico_richieste_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Vl_richiesta_servizio;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class RichiesteServizioDAO extends ServiziBaseDAO {

	private UtilitaDAO serviziDAO = new UtilitaDAO();
	private TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();
	private ServiziDAO servizioDAO = new ServiziDAO();
	private Tbc_inventarioDao inventarioDAO = new Tbc_inventarioDao();
	private UtentiDAO utentiDAO = new UtentiDAO();
	private Tbl_documenti_lettoriDAO documentiDAO = new Tbl_documenti_lettoriDAO();

	public void aggiornaRichiesta(Tbl_richiesta_servizio nuovaRichiesta, long codRichDaCancellare, String uteVar)
	throws DaoManagerException, DAOConcurrentException {
		this.cancellaRichieste(new Long[]{codRichDaCancellare}, uteVar);
		this.aggiornaRichiesta(nuovaRichiesta);
	}

	public Tbl_richiesta_servizio aggiornaRichiesta(Tbl_richiesta_servizio richiesta)
	throws DaoManagerException, DAOConcurrentException {
		Session session = this.getCurrentSession();
		try {
			if (richiesta.getCod_rich_serv() == 0)
				//nuovo
				session.save(richiesta);
			else
				//update
				richiesta = (Tbl_richiesta_servizio) session.merge(richiesta);

			session.flush();
			return richiesta;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaRichieste(Long[] codRichieste, String uteVar)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		List<Tbl_richiesta_servizio> listaRichieste = getListaRichieste(codRichieste);
		Iterator<Tbl_richiesta_servizio> iterator = listaRichieste.iterator();
		Timestamp now = DaoManager.now();
		while (iterator.hasNext()) {
			Tbl_richiesta_servizio richiesta = iterator.next();
			richiesta.setCod_stato_mov('E');
			richiesta.setCod_stato_rich('D');
			//richiesta.setFl_canc('S');
			richiesta.setUte_var(uteVar);
			richiesta.setTs_var(now);
			session.saveOrUpdate(richiesta);
		}
	}

	public void rifiutaRichieste(Long[] codRichieste, String uteVar)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		List<Tbl_richiesta_servizio> listaRichieste = getListaRichieste(codRichieste);
		Timestamp now = DaoManager.now();
		for (Tbl_richiesta_servizio richiesta : listaRichieste) {
			richiesta.setCod_stato_mov('C');
			richiesta.setCod_stato_rich('B');
			//richiesta.setFl_canc('S');
			richiesta.setUte_var(uteVar);
			richiesta.setTs_var(now);
			richiesta.getPrenotazioni_posti().clear();
			session.saveOrUpdate(richiesta);
		}
	}


	public List<Tbl_richiesta_servizio> getListaRichieste(Long[] codRichieste)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.in("cod_rich_serv", codRichieste));
			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_richiesta_servizio> getListaRichiesteGiornaliere(String codPolo, String codBib,
													String codTipoServizio, String codServizio,
													String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		Calendar oggi = Calendar.getInstance();
		oggi.set(Calendar.HOUR_OF_DAY, 0);
		oggi.set(Calendar.MINUTE,      0);
		oggi.set(Calendar.SECOND,      0);
		Timestamp dataDA = new Timestamp(oggi.getTimeInMillis());

		oggi.set(Calendar.HOUR_OF_DAY, 23);
		oggi.set(Calendar.MINUTE,      59);
		oggi.set(Calendar.SECOND,      59);
		Timestamp dataA  = new Timestamp(oggi.getTimeInMillis());

		try{
			Tbl_servizio servizio = servizioDAO.getServizioBiblioteca(codPolo, codBib, codTipoServizio, codServizio);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_servizio",   servizio))
					.add(Restrictions.ne("cod_stato_mov", "E"))
					.add(Restrictions.between("data_richiesta", dataDA, dataA));

			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getNumeroRichiesteGiornaliereTipoServizio(String codPolo, String codBib,
			String codTipoServizio) throws DaoManagerException {
		Session session = this.getCurrentSession();

		Calendar oggi = Calendar.getInstance();
		oggi.set(Calendar.HOUR_OF_DAY, 0);
		oggi.set(Calendar.MINUTE, 0);
		oggi.set(Calendar.SECOND, 0);
		Timestamp dataDA = new Timestamp(oggi.getTimeInMillis());

		oggi.set(Calendar.HOUR_OF_DAY, 23);
		oggi.set(Calendar.MINUTE, 59);
		oggi.set(Calendar.SECOND, 59);
		Timestamp dataA = new Timestamp(oggi.getTimeInMillis());

		try {

			Criteria criteria = session
					.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.ne("cod_stato_mov", "E"))
					.add(Restrictions.between("data_richiesta", dataDA, dataA))
					.setProjection(Projections.rowCount());

			//criterio su tipo servizio
			Criteria c2 = criteria.createCriteria("id_servizio");
			c2.add(Restrictions.ne("fl_canc", 'S'));
			Criteria c3 = c2.createCriteria("id_tipo_servizio");
			c3.add(Restrictions.eq("cod_tipo_serv", codTipoServizio));
			c3.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_richiesta_servizio> getListaRichieste(String codPolo, String codBib,
								String codTipoServizio, String codStatoRich,
								String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			//Get tipo_servizio
			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServizio);

			//lista servizio per tipo servizio
			Criteria criteria = session.createCriteria(Tbl_servizio.class);
			criteria.add( Restrictions.eq("id_tipo_servizio", tipoServizio) )
					.add( Restrictions.ne("fl_canc", 'S') );
			List<Tbl_servizio> listaServizi = criteria.list();

			//lista richieste per servizio
			criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("fl_tipo_rec", 'R'))
				.add(Restrictions.in("id_servizio", listaServizi));
			if (codStatoRich!=null && !codStatoRich.equalsIgnoreCase(""))
				criteria.add(Restrictions.eq("cod_stato_rich", codStatoRich));

			if (ordinamento!=null && !ordinamento.equals("")){
				createCriteriaOrder(ordinamento, null, criteria);
			}

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Long> getListaRichiesteLocaliDaStoricizzare(String codPolo, String codBib, Timestamp dataInizio, Timestamp dataLimite)
			throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			// lista  richieste della biblioteca indicata evase/rifiutate/annullate (cancellate?) entro la data indicata nella richiesta di archiviazione
			Criteria c = session.createCriteria(Tbl_richiesta_servizio.class);

			c.add(Restrictions.eq("cod_bib_dest", codBib));
			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.and(Restrictions.eq("cod_stato_rich", 'H'), Restrictions.eq("cod_stato_mov", 'C')) );
			or.add(Restrictions.and(Restrictions.eq("cod_stato_rich", 'B'), Restrictions.eq("cod_stato_mov", 'P')) );
			or.add(Restrictions.and(Restrictions.eq("cod_stato_rich", 'D'), Restrictions.eq("cod_stato_mov", 'C')) );
			or.add(Restrictions.and(Restrictions.eq("cod_stato_rich", 'B'), Restrictions.eq("cod_stato_mov", 'C')) );
			or.add(Restrictions.eq("cod_stato_mov", 'E') );	//mov annullato
			c.add(or);

			if (dataInizio != null)
				c.add(Restrictions.ge("ts_var", dataInizio));
			if (dataLimite != null)
				c.add(Restrictions.le("ts_var", dataLimite));

			c.setProjection(Projections.projectionList().add(Projections.property("cod_rich_serv")));
			c.addOrder(Order.asc("cod_rich_serv"));

			List<Long> out = new ArrayList<Long>(c.list());

			return out;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaRichiestaLocaleStoricizzata(Tbl_richiesta_servizio richiestaServ)
	throws DaoManagerException
	{
		Session session = this.getCurrentSession();
		try {
			//session.clear();
			session.delete(richiestaServ);
			session.flush(); // non rimuovere
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaSollecitiRichiesteLocaliStoricizzate(List<Tbl_solleciti> elencoSolleciti)
	throws DaoManagerException
	{
		Session session = this.getCurrentSession();
		try {
			for (int i = 0; i < elencoSolleciti.size(); i++) {
				session.clear();
				session.delete(elencoSolleciti.get(i));
				session.flush();  // non rimuovere
			}

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean inserimentoRichiestaLocaleArchiviata(Tbl_storico_richieste_servizio richiestaLocaleDaArchiviare)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(richiestaLocaleDaArchiviare);
			session.flush();
			return ret = true; // non rimuovere
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}


	public List<Vl_richiesta_servizio> getListaRichiesteScadute(
			String codPolo, String codBib, Timestamp now, String[] tipoServ, String ordinamento, int ggPrimoSoll, boolean escludiSollecitiProrogati)
			throws DaoManagerException {
		try {
			// lista richieste
			Criteria c = preparaCriteriaRichiesteScadute(codPolo, codBib, null,
					now, Arrays.asList(tipoServ), 0, ggPrimoSoll,
					escludiSollecitiProrogati);
			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public ScrollableResults getCursorRichiesteScadute(
			String codPolo, String codBib, Long[] listaRichieste, Timestamp now, List<String> tipoServ, long startId, int ggPrimoSoll)
			throws DaoManagerException {
		try {
			// lista richieste
			Criteria c = preparaCriteriaRichiesteScadute(codPolo, codBib,
					listaRichieste, now, tipoServ, startId, ggPrimoSoll, false);
			return c.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	private Criteria preparaCriteriaRichiesteScadute(String codPolo,
			String codBib, Long[] listaRichieste, Timestamp now,
			List<String> tipoServ, long startId, int ggPrimoSoll,
			boolean escludiSollecitiProrogati) throws DaoManagerException {

		//filtro su data scadenza + gg tolleranza 1° sollecito
		Timestamp primoSoll = DateUtil.addDay(now, -ggPrimoSoll);

		//sub query per richieste già sollecitate
		DetachedCriteria sub = DetachedCriteria.forClass(Tbl_solleciti.class, "sol");
		sub.setProjection(Projections.property("sol.progr_sollecito"));
		sub.add(Restrictions.ne("sol.fl_canc", 'S'));
		sub.add(Restrictions.eqProperty("sol.cod_rich_serv.id", "req.cod_rich_serv"));
		sub.add(Restrictions.eq("sol.esito", 'S'));

		//almaviva5_20160412 #6113
		if (escludiSollecitiProrogati) {
			//si escludono i mov sollecitati se sono stati nel frattempo rinnovati
			sub.add(Restrictions.ne("req.cod_stato_rich", 'N'));	//prorogata
			sub.add(Restrictions.lt("req.data_fine_prev", primoSoll));
		}

		Criteria c = getCurrentSession().createCriteria(Vl_richiesta_servizio.class, "req");

		Criterion or = Restrictions.or(Subqueries.exists(sub), Restrictions.lt("req.data_fine_prev", primoSoll) );
		Criterion statoMov = Restrictions.not(Restrictions.in("req.cod_stato_mov", ServiziConstant.STATI_MOVIMENTO_NON_ATTIVI));
		//Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

		c.add(Restrictions.ne("req.fl_canc", 'S'))
				.add(Restrictions.eq("req.fl_tipo_rec", 'R'))
				.add(Restrictions.eq("req.cod_attivita", "03"))
				.add(statoMov)
				.add(or)
				.addOrder(Order.asc("req.cod_rich_serv"));

		// filtro per id richieste selez. da sintetica
		if (ValidazioneDati.isFilled(listaRichieste))
			c.add(Restrictions.in("req.cod_rich_serv", listaRichieste));

		if (startId > 0)
			c.add(Restrictions.gt("req.cod_rich_serv", new Long(startId)));

		/*
		Criteria criteria2 =
			criteria.createCriteria("req.id_servizio")
					.createCriteria("id_tipo_servizio")
					.add(Restrictions.eq("cd_bib", bib));
		*/
		c.add(Restrictions.eq("req.cd_polo", codPolo));
		c.add(Restrictions.eq("req.cd_bib", codBib));

		if (ValidazioneDati.isFilled(tipoServ) )
			//criteria2.add(Restrictions.in("cod_tipo_serv", tipoServ));
			c.add(Restrictions.in("req.cod_tipo_serv", tipoServ));

		return c;
	}

	public List<Tbl_richiesta_servizio> getListaRichiestePerAttivitaSucc(String codPolo, String codBib,
			String codTipoServizio, String codErog,
			short progrAttivita, String ordinamento) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			//Get tipo_servizio
			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServizio);

			//lista servizio per tipo servizio
			Criteria criteria = session.createCriteria(Tbl_iter_servizio.class);
			criteria.add(Restrictions.eq("id_tipo_servizio", tipoServizio))
					.add(Restrictions.lt("progr_iter",       new Short(progrAttivita)))
					.add(Restrictions.ne("fl_canc",          'S') );
			criteria.addOrder(Order.desc("progr_iter"));
			List<Tbl_iter_servizio> listaIterServizio = criteria.list();

			//filtro la lista degli iter ottenuta considerando solo le righe fino
			//alla prima con campo obbl='S'. Si tenga conto che la select fatta era
			//order by progr_iter DESC
			serviziDAO.filtraListaIterServizioPerAttivitaSucc(listaIterServizio);

			//lista richieste per gli iter servizio selezionati
			criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.ne("fl_canc",     'S'))
					.add(Restrictions.eq("fl_tipo_rec", 'R'))
					.add(Restrictions.in("id_iter_servizio", listaIterServizio));
			if (codErog!=null && !codErog.equalsIgnoreCase(""))
				criteria.add(Restrictions.eq("cod_erog", codErog));

			if (ordinamento!=null && !ordinamento.equals("")){
				createCriteriaOrder(ordinamento, null, criteria);
			}

			return criteria.list();
		} catch (HibernateException he) {
		throw new DaoManagerException(he);
		}
	}

	private Query prepareQueryFiltriTematici(String codPolo, String codBib, String fl_svolg,
			String codTipoServizio, String statoMov, String statoRich, String codErog,
			String codAttivita, Timestamp tsFrom, Timestamp tsTo,
			boolean attuale, String ordinamento,
			String codBibInv, String codSerie, String codInv, String segnatura,
			String codBibDocLett, String tipoDocLett, String codDocLett, String progrEsempDocLett,
			boolean isStampa, FiltroCollocazioneVO filtroColl) throws DaoManagerException {

		Session session = this.getCurrentSession();
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();

		//Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(codPolo, codBib);

		Tbc_inventario inventario = null;
		if (ValidazioneDati.isFilled(codInv) ){
			inventario = inventarioDAO.getInventario(codPolo,
					codBibInv, codSerie, Integer.valueOf(codInv));
		}

		Tbl_esemplare_documento_lettore esDocLett = null;
		if (ValidazioneDati.isFilled(codDocLett) ){
			esDocLett = documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, Long.valueOf(codDocLett), Short.valueOf(progrEsempDocLett));
		}

		hql.append("select req from Vl_richiesta_servizio req ");

		//filtro collocazione
		if (!filtroColl.isEmpty() ) {
			hql.append("join req.cd_polo_inv inv ");
		}

		hql.append("where req.fl_canc<>'S' ");
		hql.append("and req.cd_polo=:polo and req.cd_bib=:bib ");
		params.put("polo", codPolo);
		params.put("bib", codBib);

		//svolgimento
		if (ValidazioneDati.isFilled(fl_svolg)) {
			hql.append("and req.fl_svolg=:fl_svolg ");
			params.put("fl_svolg", fl_svolg);
		}

		//tipo servizio
		if (ValidazioneDati.isFilled(codTipoServizio)) {
			hql.append("and req.id_servizio.id_tipo_servizio.cod_tipo_serv=:cod_tipo_serv ");
			params.put("cod_tipo_serv", codTipoServizio);
		}

		//almaviva5_20110907 #4070, #4071, #4610 in stampa stato movimento/richiesta non filtrati

		if (!isStampa) {	// in erogazione
			//Stato movimento
			if (ValidazioneDati.isFilled(statoMov)) {
				hql.append("and req.cod_stato_mov=:cod_stato_mov ");
				params.put("cod_stato_mov", statoMov);
			} else
				// Stato mov. vuoto. chiusi e prenotazioni sempre esclusi (se non impostato stato rich.)
				if (!ValidazioneDati.isFilled(statoRich))
					hql.append("and req.cod_stato_mov not in ('C', 'P', 'E') ");

			//Stato richiesta
			if (ValidazioneDati.isFilled(statoRich)) {

				//almaviva5_20100319 se chiedo rich. accettate (='G') aggiungo anche richieste prorogate,
				//in attesa di proroga e non prorogate (cod. 'S' del 19/3/2010)
				if (ValidazioneDati.equals(statoRich, "G"))
					hql.append("and req.cod_stato_rich in ('G', 'N', 'S', 'P') ");
				else {
					hql.append("and req.cod_stato_rich=:cod_stato_rich ");
					params.put("cod_stato_rich", statoRich);
				}

				//almaviva5_20100308 #3605
				if (ValidazioneDati.equals(statoRich, "H"))	//se richiesta conclusa escludo mov. chiusi
					hql.append("and req.cod_stato_mov <>'C' ");
			} else
				// Stato rich. vuoto immesse, attesa proroga e concluse sempre escluse (se non impostato stato mov.)
				if (!ValidazioneDati.isFilled(statoMov))
					//almaviva5_20100319 #3605
					//hql.append("and req.cod_stato_rich not in ('A', 'P', 'H') ");
					hql.append("and req.cod_stato_rich not in ('A') ");

		} else {	//in stampa
			//Stato movimento
			if (ValidazioneDati.isFilled(statoMov)) {
				hql.append("and req.cod_stato_mov=:cod_stato_mov ");
				params.put("cod_stato_mov", statoMov);
			}

			//Stato richiesta
			if (ValidazioneDati.isFilled(statoRich)) {
				hql.append("and req.cod_stato_rich=:cod_stato_rich ");
				params.put("cod_stato_rich", statoRich);
			}
		}

		//Codice erogazione
		if (ValidazioneDati.isFilled(codErog)) {
			hql.append("and req.cod_erog=:cod_erog ");
			params.put("cod_erog", codErog);
		}

		//Codice attivita
		if (ValidazioneDati.isFilled(codAttivita)) {
			if (attuale) {
				hql.append("and req.id_iter_servizio.cod_attivita=:cod_attivita ");
				params.put("cod_attivita", codAttivita);
			} else {
				//attivita successiva, seleziono tutti i passi opzionali
				List<String> listaCodAttivita = selezionaPassiIterPrecedenti(codPolo, codBib, codTipoServizio, codAttivita);

				if (ValidazioneDati.isFilled(listaCodAttivita)) {
					hql.append("and req.id_iter_servizio.cod_attivita in (:cod_attivita) ");
					params.put("cod_attivita", listaCodAttivita);
				} else {
					hql.append("and req.id_iter_servizio.cod_attivita=:cod_attivita ");
					params.put("cod_attivita", codAttivita);
				}
			}

		}

		//almaviva5_20100309 range date
		//almaviva5_20110620 #4363
		String range = null;
		if (isStampa) {
			if (tsFrom != null || tsTo != null) {
				StringBuilder buf = new StringBuilder();
				buf.append('(');
				//cancellate, respinte, rifiutate hanno range su ts_ins
				buf.append('(').append("req.data_in_eff is null and ").append(addTimeStampRange("req.data_richiesta", tsFrom, tsTo)).append(')');
				buf.append(" or (").append(addTimeStampRange("req.data_in_eff", tsFrom, tsTo)).append(')');
				buf.append(')');
				range = buf.toString();
			}
		} else
			range = addTimeStampRange("req.data_fine_prev", tsFrom, tsTo);

		if (range != null)
			hql.append("and ").append(range);

		//inventario
		if (inventario != null){
			hql.append("and req.cd_polo_inv=:cd_polo_inv ");
			params.put("cd_polo_inv", inventario);
		}

		//segnatura
		if (esDocLett != null){
			hql.append("and req.id_esemplare_documenti_lettore=:id_esemplare_documenti_lettore ");
			params.put("id_esemplare_documenti_lettore", esDocLett);
		}

		//filtro collocazione
		if (!filtroColl.isEmpty() ) {
			hql.append("and inv.key_loc.cd_sez.cd_sez=:codSez ");
			params.put("codSez", filtroColl.getCodSezione().toUpperCase() );
		}

		if (ValidazioneDati.isFilled(ordinamento)) {
			String ord = createHQLOrder(ordinamento, "req");
			if (ord != null)
				hql.append(" ").append(ord);
		}

		Query query = session.createQuery(hql.toString() );
		//query.setEntity("bib", bib);
		query.setProperties(params);

		return query;

	}

	public List<Tbl_richiesta_servizio> getListaRichiesteFiltriTematici(String codPolo, String codBib, String svolgimento,
			String codTipoServizio, String statoMov, String statoRich, String codErog,
			String codAttivita, Timestamp dataFinePrevDa, Timestamp dataFinePrevA,
			boolean attuale, String ordinamento,
			String codBibInv, String codSerie, String codInv, String segnatura,
			String codBibDocLett, String tipoDocLett, String codDocLett, String progrEsempDocLett,
			boolean isStampa, FiltroCollocazioneVO filtroColl) throws DaoManagerException {

		try {
			Query q = prepareQueryFiltriTematici(codPolo, codBib, svolgimento,
					codTipoServizio, statoMov, statoRich, codErog, codAttivita,
					dataFinePrevDa, dataFinePrevA, attuale, ordinamento,
					codBibInv, codSerie, codInv, segnatura, codBibDocLett,
					tipoDocLett, codDocLett, progrEsempDocLett, isStampa, filtroColl);

			return q.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public ScrollableResults cursor_getListaRichiesteFiltriTematici(String codPolo, String codBib, String svolgimento,
			String codTipoServizio, String statoMov, String statoRich, String codErog,
			String codAttivita, Timestamp dataFinePrevDa, Timestamp dataFinePrevA,
			boolean attuale, String ordinamento,
			String codBibInv, String codSerie, String codInv, String segnatura,
			String codBibDocLett, String tipoDocLett, String codDocLett, String progrEsempDocLett, boolean isStampa,
			FiltroCollocazioneVO filtroColl) throws DaoManagerException {

		try {
			Query q = prepareQueryFiltriTematici(codPolo, codBib, svolgimento,
					codTipoServizio, statoMov, statoRich, codErog, codAttivita,
					dataFinePrevDa, dataFinePrevA, attuale, ordinamento,
					codBibInv, codSerie, codInv, segnatura, codBibDocLett,
					tipoDocLett, codDocLett, progrEsempDocLett, isStampa,
					filtroColl);

			return q.setCacheMode(CacheMode.IGNORE).setFetchSize(100).scroll(ScrollMode.FORWARD_ONLY);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	private List<String> selezionaPassiIterPrecedenti(String codPolo,
			String codBib, String codTipoServ, String codAttivita) throws DaoManagerException {
		Tbl_iter_servizio myIter = new Tbl_iter_servizio();
		List<String> listaCodAttivita = new ArrayList<String>();
		IterServizioDAO idao = new IterServizioDAO();

		if (!ValidazioneDati.isFilled(codTipoServ)) {
			//se non impostato il tipo servizio chiamo ricorsivamente il metodo
			//per ogni tipo servizio definito in biblioteca
			List<Tbl_tipo_servizio> tipiServizio = tipoServizioDAO.getListaTipiServizio(codPolo, codBib);
			if (!ValidazioneDati.isFilled(tipiServizio))
				return listaCodAttivita;

			Set<String> tmp = new THashSet<String>(); //set per scartare i duplicati
			for (Tbl_tipo_servizio ts : tipiServizio) {
				codTipoServ = ts.getCod_tipo_serv();
				tmp.addAll(selezionaPassiIterPrecedenti(codPolo, codBib, codTipoServ, codAttivita));
			}
			return new ArrayList<String>(tmp);
		}

		List<Tbl_iter_servizio> listaIter = idao.getListaIterServizio(codPolo, codBib, codTipoServ);
		if (!ValidazioneDati.isFilled(listaIter))
			return listaCodAttivita;

		//seleziono il passo obbligatorio immediatamente precedente a quello specificato
		//e tutti i passi opzionali trovati tra loro.
		for (int idx = (listaIter.size() - 1); idx >= 0; idx--) {
			Tbl_iter_servizio iter = listaIter.get(idx);
			if (iter.getCod_attivita().equals(codAttivita))
				myIter = iter;

			if (iter.getProgr_iter() >= myIter.getProgr_iter())
				continue;

			listaCodAttivita.add(iter.getCod_attivita());

			if (iter.getObbl() == 'S') { //passo obbligatorio, ho finito
				break;
			} else //passo opzionale, continuo
				continue;
		}

		return listaCodAttivita;
	}


	public List<Tbl_storico_richieste_servizio> getListaRichiesteFiltriTematiciStorico(String codPolo, String codBib,
			Timestamp tsFrom, Timestamp tsTo, String codUte, String ordinamento) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();
			StringBuilder hql = new StringBuilder();
			Map<String, Object> params = new HashMap<String, Object>();

			hql.append("from Tbl_storico_richieste_servizio req where req.fl_canc<>'S' ");
			hql.append("and req.cd_polo=:codPolo and req.cd_bib=:codBib ");

			//almaviva5_20100609 cod utente
			if (ValidazioneDati.isFilled(codUte))
				hql.append(" and req.cod_utente=").append(codUte);

			//almaviva5_20120906 #4829
			if (tsFrom != null || tsTo != null) {
				StringBuilder buf = new StringBuilder();
				buf.append('(');
				//cancellate, respinte, rifiutate hanno range su ts_ins
				buf.append('(').append("req.data_in_eff is null and ").append(addTimeStampRange("req.data_richiesta", tsFrom, tsTo)).append(')');
				buf.append(" or (").append(addTimeStampRange("req.data_in_eff", tsFrom, tsTo)).append(')');
				buf.append(')');

				hql.append("and ").append(buf.toString() );
			}


			if (ValidazioneDati.isFilled(ordinamento)) {
				String ord = createHQLOrder(ordinamento, "req");
				if (ord != null)
					hql.append(" ").append(ord);
			}

			Query query = session.createQuery(hql.toString() );
			query.setString("codBib", codBib);
			query.setString("codPolo", codPolo);
			query.setProperties(params);

			return query.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Object[]> getIdsRichiesteFiltriTematiciStorico(String codPolo, String codBib,
			Timestamp tsFrom, Timestamp tsTo, String codUte, String ordinamento) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();
			StringBuilder hql = new StringBuilder();
			Map<String, Object> params = new HashMap<String, Object>();

			hql.append("select req.cd_polo, req.cd_bib, req.cod_rich_serv ");
			hql.append("from Tbl_storico_richieste_servizio req where req.fl_canc<>'S' ");
			hql.append("and req.cd_polo=:codPolo and req.cd_bib=:codBib ");

			//almaviva5_20100609 cod utente
			if (ValidazioneDati.isFilled(codUte))
				hql.append(" and req.cod_utente=").append(codUte);

			//almaviva5_20120906 #4829
			if (tsFrom != null || tsTo != null) {
				StringBuilder buf = new StringBuilder();
				buf.append('(');
				//cancellate, respinte, rifiutate hanno range su ts_ins
				buf.append('(').append("req.data_in_eff is null and ").append(addTimeStampRange("req.data_richiesta", tsFrom, tsTo)).append(')');
				buf.append(" or (").append(addTimeStampRange("req.data_in_eff", tsFrom, tsTo)).append(')');
				buf.append(')');

				hql.append("and ").append(buf.toString() );
			}


			if (ValidazioneDati.isFilled(ordinamento)) {
				String ord = createHQLOrder(ordinamento, "req");
				if (ord != null)
					hql.append(" ").append(ord);
			}

			Query query = session.createQuery(hql.toString() );
			query.setString("codBib", codBib);
			query.setString("codPolo", codPolo);
			query.setProperties(params);

			return query.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_richiesta_servizio> getListaRichiestePerSegnatura(
			String codPolo, String codBibDocLett, String tipoDocLett,
			long codDocLett, short progrEsempDocLett, String ordinamento, MovimentoVO filtroMov)
			throws DaoManagerException {

		Session session = this.getCurrentSession();

		try {
			Tbl_esemplare_documento_lettore esDocLett = documentiDAO
					.getEsemplareDocumentoLettore(codPolo, codBibDocLett,
							tipoDocLett, codDocLett, progrEsempDocLett);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			if (esDocLett != null) {
				criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
						.add(Restrictions.ne("fl_canc", 'S'))
						// imposto i criteri sugli stati della richiesta e del movimento
						.add(impostaFiltroStatoRicMov(filtroMov));

				if (ValidazioneDati.isFilled(ordinamento) )
					createCriteriaOrder(ordinamento, null, criteria);

			}
			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_richiesta_servizio> getListaRichiestePerInventario(String codPolo, String codBib,
	String codSerie, int codInv, String ordinamento, MovimentoVO filtroMov) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo,
					codBib, codSerie, codInv);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			criteria.add(Restrictions.eq("cd_polo_inv", inventario))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(impostaFiltroStatoRicMov(filtroMov));

			if (ValidazioneDati.isFilled(ordinamento) )
				createCriteriaOrder(ordinamento, null, criteria);

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public boolean esistonoMovimentiAttivi(String codPolo,
			String codBibDocLett, String tipoDocLett, long codDocLett,
			short prgEsemplare, int annata) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			if (prgEsemplare > 0) {
				Tbl_esemplare_documento_lettore esDocLett = documentiDAO
						.getEsemplareDocumentoLettore(codPolo, codBibDocLett,
								tipoDocLett, codDocLett, prgEsemplare);
				criteria.add(Restrictions.eq("id_esemplare_documenti_lettore",esDocLett));
			} else {
				Tbl_documenti_lettori docLettore = documentiDAO
						.getDocumentoLettore(codPolo, codBibDocLett,
								tipoDocLett, codDocLett);
				criteria.add(Restrictions.eq("id_documenti_lettore", docLettore));
			}

			criteria.add(Restrictions.ne("fl_canc", 'S'))
					// 22 marzo 2010: eliminato stato S (documento disponibile, movimento non ancora completato)
					.add(Restrictions.in("cod_stato_mov", new String[] {"A"}))
					.setProjection(Projections.rowCount());

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esistonoMovimentiAttivi(String codPolo, String codBib,
			String codSerie, int codInv, int annata) throws DaoManagerException {

		Session session = this.getCurrentSession();

		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBib, codSerie, codInv);
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			criteria.add(Restrictions.eq("cd_polo_inv",   inventario))
					.add(Restrictions.ne("fl_canc",       'S'))
					// 22 marzo 2010: eliminato stato S (documento disponibile, movimento non ancora completato)
					.add(Restrictions.in("cod_stato_mov", new String[]{"A"}))
					.setProjection(Projections.rowCount());

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esistonoMovimentiAttiviPerUtente(String codPolo,
			String codBib, String codBibUte, String codUtente,
			String codBibDocLett, String tipoDocLett, long codDocLett,
			short progrEsempDocLett, int annata) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbl_esemplare_documento_lettore esDocLett =
				documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett,
							tipoDocLett, codDocLett, progrEsempDocLett);
			Trl_utenti_biblioteca utenteBib = utentiDAO.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);

			//escludo mov chiusi (C) e annullati (E)
			Criterion statoMov = Restrictions.not(Restrictions.in("cod_stato_mov", ServiziConstant.STATI_MOVIMENTO_NON_ATTIVI));
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
					.add(Restrictions.eq("id_utenti_biblioteca", utenteBib))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(statoMov)
					.setProjection(Projections.rowCount());

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esistonoMovimentiAttiviPerUtente(String codPolo, String codBib, String codBibUte, String codUtente,
			String codBibInv, String codSerie, int codInv, int annata)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBibInv, codSerie, codInv);
			Trl_utenti_biblioteca utenteBib = utentiDAO.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);

			//escludo mov chiusi (C) e annullati (E)
			Criterion statoMov = Restrictions.not(Restrictions.in("cod_stato_mov", ServiziConstant.STATI_MOVIMENTO_NON_ATTIVI));
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			criteria.add(Restrictions.eq("cd_polo_inv", inventario))
					.add(Restrictions.eq("id_utenti_biblioteca", utenteBib))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(statoMov)
					.setProjection(Projections.rowCount());

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getNumeroMovimentiAttivi(String codPolo, String codBib, String codSerie, int codInv)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBib, codSerie, codInv);
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			criteria.add(Restrictions.eq("cd_polo_inv",   inventario))
					.add(Restrictions.ne("fl_canc",       'S'))
					.add(Restrictions.eq("cod_stato_mov", "A"))
					.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esistonoPrenotazioni(String codPolo, String codBibDocLett, String tipoDocLett,
										long codDocLett, short progrEsempDocLett, Timestamp data, int annata)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_esemplare_documento_lettore esDocLett = documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, codDocLett, progrEsempDocLett);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
					.add(Restrictions.ne("fl_canc",                        'S'))
					.add(Restrictions.eq("fl_tipo_rec",                    'P'))
					.add(Restrictions.eq("cod_stato_mov",                  'P'))
					.setProjection(Projections.rowCount());

			if (data != null)
				criteria.add(Restrictions.le("data_in_prev", data));

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public boolean esistonoPrenotazioni(String codPolo, String codBibOperante,
									String codUtente, String codBibUtente,
									String codBibDocLett, String tipoDocLett,
									long codDocLett, short progrEsempDocLett,
									long idRichiesta, int annata)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_esemplare_documento_lettore esDocLett = documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, codDocLett, progrEsempDocLett);
			Trl_utenti_biblioteca ute_bib = utentiDAO.getUtenteBiblioteca(codPolo, codBibUtente, codUtente, codBibOperante);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_utenti_biblioteca",           ute_bib))
					.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
					.add(Restrictions.ne("fl_canc",                        'S'))
					.add(Restrictions.eq("cod_stato_mov",                  'P'))
					.add(Restrictions.eq("fl_tipo_rec",                    'P'))
					.setProjection(Projections.rowCount());

			if (idRichiesta > 0)
				criteria.add(Restrictions.ne("cod_rich_serv", idRichiesta));

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public boolean esistonoPrenotazioni(String codPolo, String codBibOperante,
										String codUtente, String codBibUtente,
										String codBibInv, String codSerie,
										int codInv, long idRichiesta, int annata)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBibInv, codSerie, codInv);
			Trl_utenti_biblioteca ute_bib = utentiDAO.getUtenteBiblioteca(codPolo, codBibUtente, codUtente, codBibOperante);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_utenti_biblioteca", ute_bib))
					.add(Restrictions.eq("cd_polo_inv",          inventario))
					.add(Restrictions.ne("fl_canc",              'S'))
					.add(Restrictions.eq("cod_stato_mov",        'P'))
					.add(Restrictions.eq("fl_tipo_rec",          'P'))
					.setProjection(Projections.rowCount());

			if (idRichiesta > 0)
				criteria.add(Restrictions.ne("cod_rich_serv", idRichiesta));

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esistonoPrenotazioni(String codPolo, String codBib,
										String codSerie, int codInv, Timestamp data, int annata)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBib, codSerie, codInv);//UtilitaDAO.creaIdInv(codPolo, codBib, codSerie, codInv);
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			criteria.add(Restrictions.eq("cd_polo_inv",  inventario))
					.add(Restrictions.ne("fl_canc",      'S'))
					.add(Restrictions.eq("fl_tipo_rec",  'P'))
					.add(Restrictions.eq("cod_stato_mov",  'P'))
					.setProjection(Projections.rowCount());

			if (data != null)
				criteria.add(Restrictions.le("data_in_prev", data));

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_richiesta_servizio rifiutaRichiesta(long idRichiesta, String uteVar) throws DaoManagerException {
		Session session = this.getCurrentSession();
		Tbl_richiesta_servizio richiesta = getMovimentoById(idRichiesta);
		richiesta.setCod_stato_mov('C');
		richiesta.setCod_stato_rich('B');
		richiesta.setUte_var(uteVar);
		//richiesta.getPrenotazioni_posti().clear();

		session.saveOrUpdate(richiesta);
		session.flush();

		return richiesta;
	}

	public int getNumeroPrenotazioni(String codPolo, String codBibDocLett, String tipoDocLett,
									long codDocLett, short progrEsempDocLett, int annata)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_esemplare_documento_lettore esDocLett = documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, codDocLett, progrEsempDocLett);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
						.add(Restrictions.ne("fl_canc",      'S'))
						.add(Restrictions.eq("cod_stato_mov",  'P'))
						.add(Restrictions.eq("fl_tipo_rec",  'P'))
						.setProjection(Projections.rowCount());

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public int getNumeroPrenotazioni(String codPolo, String codBib,
									String codSerie, int codInv, int annata)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBib, codSerie, codInv);//UtilitaDAO.creaIdInv(codPolo, codBib, codSerie, codInv);
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			criteria.add(Restrictions.eq("cd_polo_inv",  inventario))
						.add(Restrictions.ne("fl_canc",      'S'))
						.add(Restrictions.eq("cod_stato_mov",  'P'))
						.add(Restrictions.eq("fl_tipo_rec",  'P'))
						.setProjection(Projections.rowCount());

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public int getNumeroPrenotazioni()
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);

			criteria.add(Restrictions.ne("fl_canc",      'S'))
					.add(Restrictions.eq("cod_stato_mov",'P'))
					.add(Restrictions.eq("fl_tipo_rec",  'P'))
					.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}



	public List<Vl_richiesta_servizio> getListaPrenotazioniPerSegnatura(String codPolo, String codBibDocLett, String tipoDocLett,
			long codDocLett, short progrEsempDocLett, String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbl_esemplare_documento_lettore esDocLett = documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, codDocLett, progrEsempDocLett);

			Criteria criteria = session.createCriteria(Vl_richiesta_servizio.class);
			if (esDocLett != null) {
				criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
						.add(Restrictions.ne("fl_canc",         'S'))
						.add(Restrictions.eq("fl_tipo_rec",     'P'))
						.add(Restrictions.eq("cod_stato_mov",  'P'));

				if (ValidazioneDati.isFilled(ordinamento) )
					createCriteriaOrder(ordinamento, null, criteria);

			}
			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Vl_richiesta_servizio> getListaPrenotazioniPerInventario(String codPolo, String codBib,
													String codSerie, int codInv, String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBib, codSerie, codInv);

			Criteria criteria = session.createCriteria(Vl_richiesta_servizio.class);
			if (inventario != null) {
				criteria.add(Restrictions.eq("cd_polo_inv", inventario))
						.add(Restrictions.ne("fl_canc",         'S'))
						.add(Restrictions.eq("fl_tipo_rec",     'P'))
						.add(Restrictions.eq("cod_stato_mov",  'P'));

				if (ValidazioneDati.isFilled(ordinamento) )
					createCriteriaOrder(ordinamento, null, criteria);

			}
			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Vl_richiesta_servizio> getListaPrenotazioni(String codBibDest, String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Vl_richiesta_servizio.class);

			criteria.add(Restrictions.ne("fl_canc",         'S'))
					.add(Restrictions.eq("fl_tipo_rec",     'P'))
					.add(Restrictions.eq("cod_stato_mov",  'P'))
					.add(Restrictions.eq("cod_bib_dest",    codBibDest));
			if (ValidazioneDati.isFilled(ordinamento) )
				createCriteriaOrder(ordinamento, null, criteria);
			else
				criteria.addOrder(Order.asc("data_richiesta"));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}


	}

	public List<Vl_richiesta_servizio> getListaPrenotazioni(String codBibDest, String codTipoServ, Timestamp dataInizioPrev)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria c = session.createCriteria(Vl_richiesta_servizio.class);

			c.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("fl_tipo_rec", 'P'))
				.add(Restrictions.eq("cod_stato_mov", 'P'));

			if (ValidazioneDati.isFilled(codBibDest))
				c.add(Restrictions.eq("cod_bib_dest", codBibDest));

			if (ValidazioneDati.isFilled(codTipoServ)) {
				Criteria c2 = c.createCriteria("id_servizio");
				c2.add(Restrictions.ne("fl_canc", 'S'));
				Criteria c3 = c2.createCriteria("id_tipo_servizio");
				c3.add(Restrictions.eq("cod_tipo_serv", codTipoServ));
			}

			if (dataInizioPrev != null)
				c.add(Restrictions.ge("data_in_prev", dataInizioPrev));

			c.addOrder(Order.asc("data_richiesta"));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}


	}


	public List<Vl_richiesta_servizio> getListaProroghe(String codBibDest, String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Vl_richiesta_servizio.class);

			criteria.add(Restrictions.ne("fl_canc",         'S'))
					//almaviva5_20100421 #3610
					//.add(Restrictions.or(Restrictions.eq("cod_stato_rich",  'N'),Restrictions.eq("cod_stato_rich",  'P')))
					.add(Restrictions.eq("cod_stato_rich",  'P'))
					.add(Restrictions.eq("cod_bib_dest",    codBibDest));
			if (ValidazioneDati.isFilled(ordinamento) )
				createCriteriaOrder(ordinamento, null, criteria);
			else
				criteria.addOrder(Order.asc("cod_rich_serv"));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}


	}

	public List<Tbl_richiesta_servizio> getListaMovimentiAttivi(String codBibDest, String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cod_bib_dest",    codBibDest))
					.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
										  				   Restrictions.in("cod_stato_rich", new String[]{"A","C","G","I","M","N","P","R"})
										 				  ),
										  Restrictions.or(Restrictions.isNull("cod_stato_mov"),
												  		  Restrictions.in("cod_stato_mov", new String[]{"A","S"})
												  		 )
										 )
						);

			if (ValidazioneDati.isFilled(ordinamento) )
				createCriteriaOrder(ordinamento, null, criteria);
			else
				criteria.addOrder(Order.asc("cod_rich_serv"));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Vl_richiesta_servizio> getListaMovimentiAttiviPerGiacenze(String codBibDest)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {

			Criteria criteria = session.createCriteria(Vl_richiesta_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cod_bib_dest",    codBibDest))
					.add(Restrictions.in("cod_stato_mov", new String[]{"A", "S"}))
					//almaviva5_20110408 #4350
					.add(Restrictions.in("cod_stato_rich", new Character[]{'G', 'N', 'S', 'P'}))
					.add(Restrictions.ne("fl_tipo_rec", 'P')); //no prenotazioni

			//prelazioni scadute
			Conjunction inizioPrev = new Conjunction();
			inizioPrev.add(Restrictions.isNull("data_in_eff"));	//mai consegnato al lettore
			inizioPrev.add(Restrictions.sqlRestriction("to_char({alias}.data_in_prev, 'yyyyMMDD') < to_char(now(), 'yyyyMMDD')"));

			///coll. punto deposito scaduti (consultazioni)
			Conjunction finePrev = new Conjunction();
			finePrev.add(Restrictions.eq("cod_attivita", StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO.getISOCode()));	//coll. punto deposito
			finePrev.add(Restrictions.sqlRestriction("to_char({alias}.data_fine_prev, 'yyyyMMDD') < to_char(now(), 'yyyyMMDD')"));

			criteria.add(Restrictions.or(inizioPrev, finePrev));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}


	}


	/**
	 * Controlla se per un dato iter esistono delle richieste aperte.<br/>
	 * Questi gli stati considerati.<br/>
	 * Per i movimenti:<br/>
	 * <ul>
	 * <li>A</li><li>C</li><li>G</li><li>I</li><li>M</li><li>N</li><li>P</li><li>R</li>
	 * </ul>
	 * Altri stati relativi ai movimenti sono:<br/>
	 * <ul>
	 * <li>C</li><li>E</li>
	 * </ul>
	 * <br/>
	 * Per le richieste:<br/>
	 * <ul>
	 * <li>S</li><li>A</li>
	 * </ul>
	 * Altri stati relativi alle richieste:<br/>
	 * <ul>
	 * <li>B</li><li>D</li><li>F</li><li>H</li>
	 * </ul>
	 * @param iter
	 * @return
	 * @throws DaoManagerException
	 */
	public boolean esistonoRichiestePer(Tbl_iter_servizio iter)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_iter_servizio", iter))
					.add(Restrictions.ne("fl_canc", 'S'))
					// 22 marzo 2010: da verificare (richiamato per cancellazione iter
					.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
										  				   Restrictions.in("cod_stato_rich", new String[]{"A","C","G","I","M","N","P","R"})
										 				  ),
										  Restrictions.or(Restrictions.isNull("cod_stato_mov"),
												  		  Restrictions.in("cod_stato_mov", new String[]{"A","S"})
												  		 )
										 )
						);

			return (criteria.list().size()>0);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public boolean esistonoRichiestePer(Tbl_supporti_biblioteca supporto)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			Criteria c = session.createCriteria(Tbl_richiesta_servizio.class);
			c.setProjection(Projections.rowCount());
			c.add(Restrictions.eq("id_supporti_biblioteca", supporto))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
										  				   Restrictions.in("cod_stato_rich", new String[]{"A","C","G","I","M","N","P","R"})
										 				  ),
										  Restrictions.or(Restrictions.isNull("cod_stato_mov"),
												  		  Restrictions.in("cod_stato_mov", new String[]{"A","S", "P"})
												  		 )
										 )
						);

			Number cnt = (Number) c.uniqueResult();
			return (cnt.intValue() > 0);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esistonoRichiestePer(Tbl_modalita_pagamento modalita) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria c = session.createCriteria(Tbl_richiesta_servizio.class);
			c.add(Restrictions.eq("id_modalita_pagamento", modalita))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.and(Restrictions.and(Restrictions.isNotNull("cod_stato_rich"),
										  				   Restrictions.in("cod_stato_rich", new String[]{"A","C","G","I","M","N","P","R"})
										 				  ),
										  Restrictions.or(Restrictions.isNull("cod_stato_mov"),
												  		  Restrictions.in("cod_stato_mov", new String[]{"A","S"})
												  		 )
										 )
						);

			c.setProjection(Projections.rowCount());
			Number cnt = (Number) c.uniqueResult();
			return (cnt.intValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public long getMaxCodiceRichiesta()
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		long codiceRichiesta=0;

		try {
			String queryStr="select max(a.cod_rich_serv) from Tbl_richiesta_servizio a where fl_canc!='S'";
			Query query = session.createQuery(queryStr);
			Long maxCodRich = (Long)query.uniqueResult();

			if (maxCodRich!=null) codiceRichiesta=maxCodRich;

			return codiceRichiesta;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_richiesta_servizio getMovimentoAttivo(String codPolo,
			String codBib, String codBibInv, String codSerie, int codInv, int annata) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			//Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(codPolo, codBib);
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBibInv, codSerie, codInv);
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("cd_polo_inv", inventario))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.in("cod_stato_mov", new String[] {"A", "S" }));
					//.createCriteria("id_servizio")
					//.createCriteria("id_tipo_servizio")
					//.add(Restrictions.eq("cd_bib", bib));

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			criteria.addOrder(Order.desc("data_fine_prev"));
			List<Tbl_richiesta_servizio> list = criteria.list();
			return ValidazioneDati.isFilled(list) ? list.get(0) : null;

			//return (Tbl_richiesta_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_richiesta_servizio getMovimentoAttivo(String codPolo,
			String codBib, String codBibDocLett, String tipoDocLett,
			long codDocLett, short progrEsempDocLett, int annata) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbl_esemplare_documento_lettore esDocLett =
				documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, codDocLett, progrEsempDocLett);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
					.add(Restrictions.ne("fl_canc", 'S'))
					//almaviva5_20100427 movimenti attivi solo con stato =='A'
					.add(Restrictions.eq("cod_stato_mov", 'A'));
					//.add(Restrictions.in("cod_stato_mov", new String[] {"A", "S" }));

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			criteria.addOrder(Order.desc("data_fine_prev"));
			List<Tbl_richiesta_servizio> list = criteria.list();
			return ValidazioneDati.isFilled(list) ? list.get(0) : null;

			//return (Tbl_richiesta_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_richiesta_servizio getMovimento(String codPolo,
			String codBib, String codBibInv, String codSerie, int codInv) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(codPolo, codBib);
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBibInv, codSerie, codInv);
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("cd_polo_inv", inventario))
					.add(Restrictions.ne("fl_canc", 'S'))
					.createCriteria("id_servizio")
					.createCriteria("id_tipo_servizio")
					.add(Restrictions.eq("cd_bib", bib));

			return (Tbl_richiesta_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_richiesta_servizio getMovimento(String codPolo,
			String codBib, String codBibDocLett, String tipoDocLett,
			long codDocLett, short progrEsempDocLett) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(codPolo, codBib);
			Tbl_esemplare_documento_lettore esDocLett =
				documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, codDocLett, progrEsempDocLett);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
					.add(Restrictions.ne("fl_canc", 'S'))
					.createCriteria("id_servizio")
					.createCriteria("id_tipo_servizio")
					.add(Restrictions.eq("cd_bib", bib));

			return (Tbl_richiesta_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_richiesta_servizio getMovimentoById(long idRichiesta) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			return (Tbl_richiesta_servizio) session.get(Tbl_richiesta_servizio.class, new Long (idRichiesta));

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_richiesta_servizio getUltimoMovimento(String codPolo,
			String codBib, String codBibInv, String codSerie, int codInv, int annata) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(codPolo, codBib);
			Tbc_inventario inventario = inventarioDAO.getInventario(codPolo, codBibInv, codSerie, codInv);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("cd_polo_inv", inventario))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.ne("cod_stato_mov", 'E'))
					.add(Restrictions.isNotNull("data_fine_eff"))
					.addOrder(Order.desc("data_fine_eff"))
					.setMaxResults(1)
					.createCriteria("id_servizio")
					.createCriteria("id_tipo_servizio")
					.add(Restrictions.eq("cd_bib", bib));


			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			return (Tbl_richiesta_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_richiesta_servizio getUltimoMovimento(String codPolo,
			String codBib, String codBibDocLett, String tipoDocLett,
			long codDocLett, short progrEsempDocLett, int annata) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(codPolo, codBib);
			Tbl_esemplare_documento_lettore esDocLett =
				documentiDAO.getEsemplareDocumentoLettore(codPolo, codBibDocLett, tipoDocLett, codDocLett, progrEsempDocLett);

			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.eq("id_esemplare_documenti_lettore", esDocLett))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.ne("cod_stato_mov", 'E'))
					.add(Restrictions.isNotNull("data_fine_eff"))
					.addOrder(Order.desc("data_fine_eff"))
					.setMaxResults(1)
					.createCriteria("id_servizio")
					.createCriteria("id_tipo_servizio")
					.add(Restrictions.eq("cd_bib", bib));

			//almaviva5_20100518 richieste su periodici
			if (annata > 0)
				criteria.add(Restrictions.eq("anno_period", new BigDecimal(annata)));

			return (Tbl_richiesta_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getNumeroSolleciti(Tbl_richiesta_servizio richiesta)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_solleciti.class);

			criteria.add(Restrictions.eq("cod_rich_serv", richiesta))
					.add(Restrictions.ne("fl_canc", 'S'))
					.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getNumeroSollecitiById(long idRichiesta) throws DaoManagerException {
		//almaviva5_20160526 #6113
		Session session = getCurrentSession();
		Tbl_richiesta_servizio rs = (Tbl_richiesta_servizio) session.get(Tbl_richiesta_servizio.class, idRichiesta);
		return getNumeroSolleciti(rs);
	}

	public List<Tbl_solleciti> getListaSolleciti(Tbl_richiesta_servizio richiesta)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_solleciti.class);

			criteria.add(Restrictions.eq("cod_rich_serv", richiesta))
					.add(Restrictions.ne("fl_canc", 'S'))
					.addOrder(Order.desc("data_invio"));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_solleciti getUltimoSollecito(Tbl_richiesta_servizio richiesta) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_solleciti.class);

			c.add(Restrictions.eq("cod_rich_serv", richiesta))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("esito", 'S'))
				.addOrder(Order.desc("data_invio"))
				.setMaxResults(1);

			return (Tbl_solleciti) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_storico_richieste_servizio getStoricoRichiestaById(String codPolo,
			String codBib, long cod_rich_serv)throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_storico_richieste_servizio.class);

			c.add(Restrictions.eq("cod_rich_serv", new BigDecimal(cod_rich_serv)))
				.add(Restrictions.eq("cd_polo", codPolo))
				.add(Restrictions.eq("cd_bib", codBib));

			return (Tbl_storico_richieste_servizio) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_dati_richiesta_ill getDatiRichiestaIll(int id_ill) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			return (Tbl_dati_richiesta_ill) session.get(Tbl_dati_richiesta_ill.class, id_ill);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_dati_richiesta_ill aggiornaDatiRichiestaILL(
			Tbl_dati_richiesta_ill dati_ill) throws DaoManagerException,
			DAOConcurrentException {
		Session session = this.getCurrentSession();
		try {
			if (dati_ill.getId_dati_richiesta() == 0)
				//nuovo
				session.save(dati_ill);
			else
				//update
				dati_ill = (Tbl_dati_richiesta_ill) session.merge(dati_ill);

			session.flush();
			return dati_ill;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_messaggio aggiornaMessaggio(
			Tbl_messaggio msg) throws DaoManagerException,
			DAOConcurrentException {
		Session session = this.getCurrentSession();
		try {
			if (msg.getId_messaggio() == 0)
				//nuovo
				session.save(msg);
			else
				//update
				msg = (Tbl_messaggio) session.merge(msg);

			session.flush();
			return msg;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_dati_richiesta_ill getDatiRichiestaIll(String tid, char ruolo, String reqId, String resId) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_dati_richiesta_ill.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.eq("fl_ruolo", ruolo));
			c.add(Restrictions.eq("transactionId", new Long(tid)));
			if (ValidazioneDati.isFilled(reqId))
				c.add(Restrictions.eq("requesterId", reqId));
			if (ValidazioneDati.isFilled(resId))
				c.add(Restrictions.eq("responderId", resId));

			return (Tbl_dati_richiesta_ill) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_dati_richiesta_ill> getListaDatiRichiestaIll(long tid,
			String reqId, String resId, String clientId, RuoloBiblioteca ruolo,
			String statoILL, String codAttivitaLoc, String codStatoMov,
			int idDocumento) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_dati_richiesta_ill.class, "dri");
			c.add(Restrictions.ne("fl_canc", 'S'));
			boolean hasId = ValidazioneDati.isFilled(tid);
			boolean hasStatoILL = ValidazioneDati.isFilled(statoILL);
			if (hasId)
				c.add(Restrictions.eq("transactionId", tid));
			else {
				if (ValidazioneDati.isFilled(clientId))
					c.add(Restrictions.eq("clientId", clientId));

				//almaviva5_20160414
				if (hasStatoILL)
					c.add(Restrictions.eq("cd_stato", statoILL));
				else {
					//esclude gli stati F113A, F1218, F1221, F1212
					c.add(Restrictions.not(Restrictions.in("cd_stato", new String[] {
							StatoIterRichiesta.F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA.getISOCode(),
							StatoIterRichiesta.F1218_TERMINE_SCADUTO.getISOCode(),
							StatoIterRichiesta.F1221_CONFERMA_ANNULLAMENTO.getISOCode(),
							StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE.getISOCode() })));
				}
				if (idDocumento > 0) {
					//filtro su doc. non sbn legato
					Criteria c2 = c.createCriteria("documento", "doc");
					c2.add(Restrictions.ne("doc.fl_canc", 'S'));
					c2.add(Restrictions.idEq(idDocumento));
				}

				//subquery per esclusione mov. locali chiusi/annullati
				DetachedCriteria subMovChiusi = DetachedCriteria.forClass(Tbl_richiesta_servizio.class, "rs2");
				subMovChiusi.add(Restrictions.eqProperty("rs2.cod_rich_serv", "dri.richiesta.id"));
				subMovChiusi.setProjection(Projections.property("rs2.cod_rich_serv"));
				subMovChiusi.add(Restrictions.ne("rs2.fl_canc", 'S'));
				subMovChiusi.add(Restrictions.not(Restrictions.in("rs2.cod_stato_mov", new Character[] { 'C', 'E' })));

				//filtri su richiesta locale
				boolean hasStatoMov = ValidazioneDati.isFilled(codStatoMov);
				boolean hasAttivitaLoc = ValidazioneDati.isFilled(codAttivitaLoc);

				if (hasStatoMov || hasAttivitaLoc) {
					Criteria c2 = c.createCriteria("richiesta", "rs");
					c2.add(Restrictions.ne("rs.fl_canc", 'S'));

					if (hasAttivitaLoc) {
						c2.add(Restrictions.eq("rs.cod_attivita", codAttivitaLoc));
						// se consegna al lettore, escludere mov chiusi/annullati
						if (!hasStatoILL
								&& ValidazioneDati.in(StatoIterRichiesta.of(codAttivitaLoc),
										StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE,
										StatoIterRichiesta.RESTITUZIONE_DOCUMENTO)) {
							c.add(Restrictions.or(Restrictions.isNull("richiesta"), Subqueries.exists(subMovChiusi)));
						}
					}

					if (hasStatoMov)
						c2.add(Restrictions.eq("rs.cod_stato_mov", codStatoMov));
				} else {
					switch (ruolo) {
					case FORNITRICE:
						if (!hasStatoILL)
							c.add(Restrictions.or(Restrictions.isNull("richiesta"), Subqueries.exists(subMovChiusi)));
						break;
					case RICHIEDENTE:
						if (!hasStatoILL)
							c.add(Restrictions.isNull("data_fine"));
						break;
					default:
						break;
					}
				}
			}

			if (ValidazioneDati.isFilled(reqId))
				c.add(Restrictions.eq("requesterId", reqId));
			if (ValidazioneDati.isFilled(resId))
				c.add(Restrictions.eq("responderId", resId));
			if (ruolo != null)
				c.add(Restrictions.eq("fl_ruolo", ruolo.getFl_ruolo() ));

			c.addOrder(Order.desc("transactionId"));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaDatiRichiestaILLStoricizzata(Tbl_dati_richiesta_ill dati_richiesta_ill)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
/*			Iterator<Tbl_messaggio> i = dati_richiesta_ill.getMessaggio().iterator();
			while (i.hasNext()) {
				Tbl_messaggio msg = i.next();
				i.remove();
				//session.delete(msg);
			}
	*/
			session.delete(dati_richiesta_ill);
			session.flush();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
