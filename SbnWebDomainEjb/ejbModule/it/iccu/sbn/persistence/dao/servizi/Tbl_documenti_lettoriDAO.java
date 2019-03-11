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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_disponibilita_precatalogati;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;

import java.util.List;
import java.util.Set;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class Tbl_documenti_lettoriDAO extends ServiziBaseDAO {

	public Tbl_documenti_lettoriDAO() {
		super();
	}

	public Tbl_documenti_lettori select(String polo, String codBib,
			String ordSegn) throws DaoManagerException {

		Session session = this.getCurrentSession();

		Criteria criteria = session.createCriteria(Tbl_documenti_lettori.class);
		criteria.add(Restrictions.eq("cd_bib", creaIdBib(polo, codBib)));
		criteria.add(Restrictions.eq("ord_segnatura", ordSegn));
		criteria.add(Restrictions.ne("fl_canc", 'S'));
		return (Tbl_documenti_lettori) criteria.uniqueResult();
	}

	public void insert(DocumentoNonSbnVO documento) throws DaoManagerException,
			UtenteNotFoundException {
		Session session = this.getCurrentSession();
		Tbl_documenti_lettori doc = ConversioneHibernateVO.toHibernate()
				.documentoNonSbn(documento);
		try {
			setSessionCurrentCfg();
			session.saveOrUpdate(doc);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_documenti_lettori> select(Tbl_documenti_lettori filtro,
			String inventario, String ordinamento, List<String> listaBib)
			throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_documenti_lettori.class);
			c.add(Restrictions.ne("fl_canc", 'S'));

			// almaviva5_20091027 filtro bib metropolitane
			if (!ValidazioneDati.isFilled(listaBib))
				c.add(Restrictions.eq("cd_bib", filtro.getCd_bib()));
			else {
				Criteria c2 = c.createCriteria("cd_bib");
				c2.add(Restrictions.eq("cd_polo.id", filtro.getCd_bib().getCd_polo().getCd_polo()));
				c2.add(Restrictions.in("cd_biblioteca", listaBib));
			}

			char tipo_doc_lett = filtro.getTipo_doc_lett();
			if (ValidazioneDati.isFilled(tipo_doc_lett))
				c.add(Restrictions.eq("tipo_doc_lett", tipo_doc_lett));
			else
				//almaviva5_20160802 servizi ILL
				c.add(Restrictions.in("tipo_doc_lett", new Character[] { 'D', 'P' }));

			char fonte = filtro.getFonte();
			if (ValidazioneDati.isFilled(fonte))
				c.add(Restrictions.eq("fonte", fonte));

			String titolo = filtro.getTitolo();
			if (ValidazioneDati.isFilled(titolo))
				c.add(ricercaPerParoleAND("tidx_vector", titolo));

			String ord_segnatura = filtro.getOrd_segnatura();
			if (ValidazioneDati.isFilled(ord_segnatura))
				c.add(Restrictions.like("ord_segnatura", ord_segnatura,	MatchMode.START));

			Tbl_utenti id_utente = filtro.getId_utenti();
			if (id_utente != null) {
				c.add(Restrictions.eq("id_utenti", id_utente));
			}

			if (ValidazioneDati.isFilled(inventario)) {
				c.createCriteria("Tbl_esemplare_documento_lettore")
						.add(Restrictions.ne("fl_canc", 'S'))
						.add(Restrictions.eq("inventario", inventario));
			}

			// almaviva5_20110713 range date
			addTimeStampRange(c, "ts_ins", filtro.getTs_ins(), filtro.getTs_var());

			//almaviva5_20160331 servizi ill
			//natura
			char natura = filtro.getNatura();
			if (ValidazioneDati.isFilled(natura))
				c.add(Restrictions.eq("natura", natura));

			//numero standard
			Character tp_numero_std = filtro.getTp_numero_std();
			if (ValidazioneDati.isFilled(tp_numero_std))
				c.add(Restrictions.eq("tp_numero_std", tp_numero_std));
			String numero_std = filtro.getNumero_std();
			if (ValidazioneDati.isFilled(numero_std))
				c.add(Restrictions.eq("numero_std", numero_std));

			Character tipoRecord = filtro.getTp_record_uni();
			if (ValidazioneDati.isFilled(tipoRecord))
				c.add(Restrictions.eq("tp_record_uni", tipoRecord));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public Tbl_documenti_lettori esisteSegnatura(Tbl_documenti_lettori filtro)
			throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Tbl_documenti_lettori.class);
			int idDocumento = filtro.getId_documenti_lettore();
			c.add(Restrictions.eq("cd_bib", filtro.getCd_bib()))
					.add(Restrictions.eq("ord_segnatura", filtro.getOrd_segnatura()));
			c.add(Restrictions.eq("tipo_doc_lett", filtro.getTipo_doc_lett()));
			if (idDocumento > 0)
				c.add(Restrictions.ne("id_documenti_lettore", idDocumento));

			return (Tbl_documenti_lettori) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public Tbl_documenti_lettori esisteSegnatura(String codPolo, String codBib, String ordSegn, int idDoc)
		throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Tbl_documenti_lettori.class);
			c.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));
			c.add(Restrictions.eq("ord_segnatura", ordSegn));
			c.add(Restrictions.ne("id_documenti_lettore", idDoc));
			c.add(Restrictions.ne("fl_canc", 'S'));

			List<Tbl_documenti_lettori> list = c.list();
			return ValidazioneDati.isFilled(list) ? list.get(0) : null;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbl_documenti_lettori> esisteDocumentoNelRangeDiSegnatura(
			String codPolo, String codBib,
			Tbl_disponibilita_precatalogati segnatura, String ordinamento)
			throws DaoManagerException {

		Session session = this.getCurrentSession();
		UtilitaDAO utilDao = new UtilitaDAO();
		try {
			Tbf_biblioteca_in_polo bib = utilDao.getBibliotecaInPolo(codPolo,
					codBib);

			Criteria criteria = session
					.createCriteria(Tbl_documenti_lettori.class);
			criteria.add(Restrictions.ne("fl_canc", 'S')).add(
					Restrictions.eq("cd_bib", bib));

			String segn_da = segnatura.getSegn_da();
			String segn_a = segnatura.getSegn_a();

			if (ValidazioneDati.isFilled(segn_da)) {
				criteria.add(Restrictions.ge("ord_segnatura", segn_da));
				criteria.add(Restrictions.le("ord_segnatura", segn_a));
			}

			/*
			 * String cod_frui = segnatura.getCod_frui(); if
			 * (ValidazioneDati.isFilled(cod_frui))
			 * criteria.add(Restrictions.eq("cd_catfrui", cod_frui));
			 *
			 * String cod_no_disp = segnatura.getCod_no_disp(); if
			 * (ValidazioneDati.isFilled(cod_no_disp))
			 * criteria.add(Restrictions.eq("cd_no_disp", cod_no_disp));
			 */if (ordinamento != null && !ordinamento.equals(""))
				createCriteriaOrder(ordinamento, null, criteria);

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public Tbl_documenti_lettori getDocumentoLettore(String codPolo,
			String codBibDocLett, String tipoDocLett, long codDocLett)
			throws DaoManagerException {

		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbl_documenti_lettori.class);
		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBibDocLett);
		Tbl_documenti_lettori docLett = null;

		try {
			criteria.add(Restrictions.eq("cd_bib", bib))
					.add(Restrictions.eq("tipo_doc_lett", tipoDocLett))
					.add(Restrictions.eq("cod_doc_lett", codDocLett));

			docLett = (Tbl_documenti_lettori) criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

		return docLett;
	}

	public Tbl_esemplare_documento_lettore getEsemplareDocumentoLettore(
			String codPolo, String codBibDocLett, String tipoDocLett,
			long codDocLett, short progrEsempDocLett)
			throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria c = session
					.createCriteria(Tbl_esemplare_documento_lettore.class);
			c.add(Restrictions.eq("prg_esemplare", progrEsempDocLett)).add(
					Restrictions.ne("fl_canc", 'S'));

			Criteria c2 = c.createCriteria("id_documenti_lettore");

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBibDocLett);
			c2.add(Restrictions.eq("cd_bib", bib))
					.add(Restrictions.eq("tipo_doc_lett", tipoDocLett))
					.add(Restrictions.eq("cod_doc_lett", codDocLett));

			Tbl_esemplare_documento_lettore esDocLett = (Tbl_esemplare_documento_lettore) c
					.uniqueResult();

			return esDocLett;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_esemplare_documento_lettore> getListaEsemplariDocumentoLettore(
			int idDocumento) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session
					.createCriteria(Tbl_esemplare_documento_lettore.class);
			criteria.add(
					Restrictions.eq("id_documenti_lettore.id", new Integer(
							idDocumento)))
			// .add(Restrictions.ne("fl_canc", 'S'))
					.addOrder(Order.asc("ts_ins"));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_documenti_lettori saveDocumento(Tbl_documenti_lettori doc)
			throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		this.setSessionCurrentCfg();
		try {
			if (doc.getId_documenti_lettore() == 0) {
				Integer id = (Integer) session.save(doc);
				doc.setCod_doc_lett(id);
				session.update(doc);
			} else
				doc = (Tbl_documenti_lettori) session.merge(doc);

			session.flush();
			return doc;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_esemplare_documento_lettore saveEsemplare(
			Tbl_esemplare_documento_lettore edl) throws DaoManagerException,
			DAOConcurrentException {

		Session session = this.getCurrentSession();
		try {
			if (edl.getId_esemplare() == 0) {
				short prg = (short) (maxProgressivoEsemplare(edl
						.getId_documenti_lettore().getId_documenti_lettore()) + 1);
				edl.setPrg_esemplare(prg);
				session.save(edl);
			} else
				edl = (Tbl_esemplare_documento_lettore) session.merge(edl);

			session.flush();
			return edl;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_esemplare_documento_lettore getEsemplareDocumentoLettoreById(
			int idEsemplare) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session
					.createCriteria(Tbl_esemplare_documento_lettore.class);
			criteria.add(Restrictions.eq("id_esemplare", idEsemplare)).add(
					Restrictions.ne("fl_canc", 'S'));

			Tbl_esemplare_documento_lettore esDocLett = (Tbl_esemplare_documento_lettore) criteria
					.uniqueResult();

			return esDocLett;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_documenti_lettori getDocumentoLettoreById(int idDocumento)
			throws DaoManagerException {

		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbl_documenti_lettori.class);
		Tbl_documenti_lettori docLett = null;

		try {
			criteria.add(Restrictions.eq("id_documenti_lettore", idDocumento))
					.add(Restrictions.ne("fl_canc", 'S'));

			docLett = (Tbl_documenti_lettori) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

		return docLett;
	}

	public List<Integer> getListaIDsDocumentoLettore() throws DaoManagerException {

		Session session = this.getCurrentSession();
		Criteria c = session.createCriteria(Tbl_documenti_lettori.class);

		try {
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.setProjection(Projections.property("id_documenti_lettore"));
			c.add(Restrictions.in("tipo_doc_lett", new Character[] {'P', 'D'}));
			c.addOrder(Order.asc("id_documenti_lettore"));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public int countEsemplariDocumento(int idDocumento)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session
					.createCriteria(Tbl_esemplare_documento_lettore.class);
			c.add(Restrictions.eq("id_documenti_lettore.id", idDocumento))
					.add(Restrictions.ne("fl_canc", 'S'))
					.setProjection(Projections.rowCount());

			Number count = (Number) c.uniqueResult();
			return count.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_esemplare_documento_lettore cercaEsemplareLibero(String polo,
			String bib, int idDoc) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			DetachedCriteria sub = DetachedCriteria.forClass(
					Tbl_richiesta_servizio.class, "req");
			sub.add(Restrictions.ne("fl_canc", 'S'));
			sub.add(Restrictions.eq("cod_bib_dest", bib));
			sub.add(Restrictions.eq("cod_stato_mov", 'A'));
			sub.add(Restrictions.eqProperty(
					"req.id_esemplare_documenti_lettore", "ese.id_esemplare"));
			sub.setProjection(Projections
					.property("req.id_esemplare_documenti_lettore"));

			Criteria criteria = session.createCriteria(
					Tbl_esemplare_documento_lettore.class, "ese");
			criteria.add(Restrictions.eq("id_documenti_lettore.id", idDoc))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Subqueries.notExists(sub))
					.addOrder(Order.asc("ese.prg_esemplare"));

			List list = criteria.list();
			if (ValidazioneDati.isFilled(list))
				return (Tbl_esemplare_documento_lettore) list.get(0);

			return null;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public short maxProgressivoEsemplare(int idDoc) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria lock = session
					.createCriteria(Tbl_esemplare_documento_lettore.class);
			lock.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.UPGRADE);
			lock.add(Restrictions.eq("id_documenti_lettore.id", idDoc));
			lock.list();

			Criteria c2 = session
					.createCriteria(Tbl_esemplare_documento_lettore.class);
			c2.setProjection(Projections.max("prg_esemplare"));
			c2.add(Restrictions.eq("id_documenti_lettore.id", idDoc));

			Number max = (Number) c2.uniqueResult();
			return max != null ? max.shortValue() : 0;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_esemplare_documento_lettore cercaEsemplareProssimoAllaRiconsegna(
			String polo, String bib, int idDoc) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("cod_bib_dest", bib));
			criteria.add(Restrictions.in("cod_stato_mov", new String[] { "A", "S" }));
			criteria.add(Restrictions.eq("id_documenti_lettore.id", idDoc));

			ProjectionList proList = Projections.projectionList();
			proList.add(Projections.groupProperty("id_esemplare_documenti_lettore"));
			proList.add(Projections.min("data_fine_prev"), "minDataFine");
			criteria.setProjection(proList);

			criteria.addOrder(Order.asc("minDataFine"));

			List list = criteria.list();
			if (ValidazioneDati.isFilled(list))
				return (Tbl_esemplare_documento_lettore) ((Object[]) list.get(0))[0];

			return null;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaDocumentoRichiestaStoricizzata(Tbl_documenti_lettori doc)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Set<Tbl_esemplare_documento_lettore> esemplari = doc.getTbl_esemplare_documento_lettore();
			for (Tbl_esemplare_documento_lettore e : esemplari) {
				session.delete(e);
			}

			session.delete(doc);
			session.flush();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Integer> getListaDocumentiNonPossedutiSenzaRichiesta(String codPolo, String codBib)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			StringBuilder hql = new StringBuilder(1024);
			hql.append("select id from Tbl_documenti_lettori doc ")
				.append("where doc.fl_canc<>'S' ")
				.append("and doc.cd_bib=:bib ")
				.append("and doc.tipo_doc_lett='D' ")
				.append("and not exists (select 1 from Tbl_richiesta_servizio req where req.id_documenti_lettore.id=doc.id) ")
				.append("and not exists (select 1 from Tbl_dati_richiesta_ill ill where ill.documento.id=doc.id)");

			Query q = session.createQuery(hql.toString());
			q.setEntity("bib", creaIdBib(codPolo, codBib));

			return q.list();

		} catch (Exception he) {
			throw new DaoManagerException(he);
		}

	}

}
