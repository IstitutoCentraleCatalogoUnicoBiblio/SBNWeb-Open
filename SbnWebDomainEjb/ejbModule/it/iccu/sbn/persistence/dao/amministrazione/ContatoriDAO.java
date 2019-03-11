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
package it.iccu.sbn.persistence.dao.amministrazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_contatore;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ContatoriDAO extends DaoManager {

	private static final String ROOT_BIB = " __";
	private static final int DEFAULT_DISATTIVAZIONE_UTENTE = 180;
	private static final int DEFAULT_RINNOVO_PASSWORD = 90;

	private static Tbf_contatore preparaContatore(String codPolo, String codBib,
			String cdCont, int anno, String key1, int lim_max, String firmaUtente) {
		Tbf_contatore cont;
		cont = new Tbf_contatore();
		cont.setCd_polo(creaIdBib(codPolo, codBib));
		cont.setCd_cont(cdCont);
		cont.setAnno(anno);
		String key_1 = ValidazioneDati.fillRight(ValidazioneDati.trimOrEmpty(key1), ' ', 3);
		cont.setKey1(key_1);
		cont.setLim_max(lim_max);
		cont.setAttivo('S');
		cont.setFl_canc('N');
		cont.setUte_ins(firmaUtente);
		cont.setUte_var(firmaUtente);
		cont.setTs_ins(now());
		return cont;
	}

	public static final int getDisattivazioneUtenteDays(String polo) throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_contatore.class);
		criteria.add(Restrictions.eq("cd_polo", DaoManager.creaIdBib(polo, ROOT_BIB)))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("cd_cont", "USR"));

		Tbf_contatore cont = (Tbf_contatore) criteria.uniqueResult();
		if (cont == null)
			return CommonConfiguration.getPropertyAsInteger(Configuration.DEFAULT_DISATTIVAZIONE_UTENTE, DEFAULT_DISATTIVAZIONE_UTENTE);
		else
			return cont.getLim_max();
	}


	public static final int getRinnovoPasswordDays(String polo) throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_contatore.class);
		criteria.add(Restrictions.eq("cd_polo", DaoManager.creaIdBib(polo, ROOT_BIB)))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("cd_cont", "PWD"));

		Tbf_contatore cont = (Tbf_contatore) criteria.uniqueResult();
		if (cont == null)
			return CommonConfiguration.getPropertyAsInteger(Configuration.DEFAULT_RINNOVO_PASSWORD, DEFAULT_RINNOVO_PASSWORD);
		else
			return cont.getLim_max();
	}


	public static final int getRinnovoPasswordUtenteLettore() throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_contatore.class);
		criteria.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("cd_cont", "PWL"));

		Tbf_contatore cont = (Tbf_contatore) criteria.uniqueResult();
		if (cont == null)
			return CommonConfiguration.getPropertyAsInteger(Configuration.DEFAULT_RINNOVO_PASSWORD, DEFAULT_RINNOVO_PASSWORD);
		else
			return cont.getLim_max();
	}


	public static final int getDisattivazioneUtenteLettore(String codPolo) throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_contatore.class);
		criteria.add(Restrictions.eq("cd_polo", DaoManager.creaIdBib(codPolo, ROOT_BIB)))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("cd_cont", "USL"));

		Tbf_contatore cont = (Tbf_contatore) criteria.uniqueResult();
		if (cont == null)
			return CommonConfiguration.getPropertyAsInteger(Configuration.DEFAULT_DISATTIVAZIONE_UTENTE, DEFAULT_DISATTIVAZIONE_UTENTE);
		else
			return cont.getLim_max();
	}


	public static final int getNextValue(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1, boolean create) throws Exception {
		return getNextValue(ticket, codPolo, codBib, cdCont, anno, key1, create, 0);
	}


	public static final int getNextValue(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1, boolean create, int lim_max) throws Exception {
		return incrementValue(ticket, codPolo, codBib, cdCont, anno, key1, create, 1, lim_max);
	}


	public static final int incrementValue(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1, boolean create, int delta) throws Exception {
		return incrementValue(ticket, codPolo, codBib, cdCont, anno, key1, create, delta, 0);
	}


	public static final int incrementValue(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1, boolean create, int delta, int lim_max) throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_contatore.class);
		Tbf_biblioteca_in_polo bib = DaoManager.creaIdBib(codPolo, codBib);
		c.add(Restrictions.eq("cd_polo", bib))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("attivo", 'S'))
				.add(Restrictions.eq("cd_cont", cdCont));

		if (anno > 0)
			c.add(Restrictions.eq("anno", anno));

		if (ValidazioneDati.isFilled(key1))
			c.add(Restrictions.eq("key1", key1));

		String firmaUtente = getFirmaUtente(ticket);
		Tbf_contatore cont = (Tbf_contatore) c.setLockMode(LockMode.UPGRADE).uniqueResult();
		if (cont == null) {
			if (!create)
				//throw new ApplicationException(SbnErrorTypes.ERROR_DB_READ_SEQUENCE);
				return -1;

			cont = preparaContatore(codPolo, codBib, cdCont, anno, key1, lim_max, firmaUtente);
		}

		int last = cont.getUltimo_prg();
		int next = last + delta;
		int max = cont.getLim_max() > 0 ? cont.getLim_max() : Integer.MAX_VALUE;
		if (next > max) {
			next = 1;
			int anno_cont = cont.getAnno();
			if (anno_cont > 0)
				cont.setAnno(++anno_cont);
		}

		cont.setUltimo_prg(next);
		cont.setUte_var(firmaUtente);
		session.saveOrUpdate(cont);

		return next;

	}


	public static final int getAndIncrement(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1, boolean create, int delta) throws Exception {
		return getAndIncrement(ticket, codPolo, codBib, cdCont, anno, key1, create, delta, 0);
	}


	public static final int getAndIncrement(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1, boolean create, int delta, int lim_max) throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_contatore.class);
		Tbf_biblioteca_in_polo bib = DaoManager.creaIdBib(codPolo, codBib);
		c.add(Restrictions.eq("cd_polo", bib))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("attivo", 'S'))
				.add(Restrictions.eq("cd_cont", cdCont));

		if (anno > 0)
			c.add(Restrictions.eq("anno", anno));

		if (ValidazioneDati.isFilled(key1))
			c.add(Restrictions.eq("key1", key1));

		String firmaUtente = getFirmaUtente(ticket);
		Tbf_contatore cont = (Tbf_contatore) c.setLockMode(LockMode.UPGRADE).uniqueResult();
		if (cont == null) {
			if (!create)
				//throw new ApplicationException(SbnErrorTypes.ERROR_DB_READ_SEQUENCE);
				return -1;

			cont = preparaContatore(codPolo, codBib, cdCont, anno, key1, lim_max, firmaUtente);
		}

		int last = cont.getUltimo_prg();
		int next = last + delta;
		int max = cont.getLim_max() > 0 ? cont.getLim_max() : Integer.MAX_VALUE;
		if (next > max) {
			next = 1;
			int anno_cont = cont.getAnno();
			if (anno_cont > 0)
				cont.setAnno(++anno_cont);
		}

		cont.setUltimo_prg(next);
		cont.setUte_var(firmaUtente);
		session.saveOrUpdate(cont);

		return last;

	}


	public static final int getCurrentValue(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1) throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_contatore.class);
		Tbf_biblioteca_in_polo bib = DaoManager.creaIdBib(codPolo, codBib);
		c.add(Restrictions.eq("cd_polo", bib))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("attivo", 'S'))
				.add(Restrictions.eq("cd_cont", cdCont));

		if (anno > 0)
			c.add(Restrictions.eq("anno", anno));

		if (ValidazioneDati.isFilled(key1))
			c.add(Restrictions.eq("key1", key1));

		Tbf_contatore cont = (Tbf_contatore) c.uniqueResult();
		if (cont == null)
			//throw new ApplicationException(SbnErrorTypes.ERROR_DB_READ_SEQUENCE);
			return -1;

		return cont.getUltimo_prg();

	}

	public static final Tbf_contatore getContatore(String ticket, String codPolo, String codBib, String cdCont, int anno, String key1) throws Exception {
		ContatoriDAO dao = new ContatoriDAO();
		Session session = dao.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_contatore.class);
		Tbf_biblioteca_in_polo bib = DaoManager.creaIdBib(codPolo, codBib);
		c.add(Restrictions.eq("cd_polo", bib))
				.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("attivo", 'S'))
				.add(Restrictions.eq("cd_cont", cdCont));

		if (anno > 0)
			c.add(Restrictions.eq("anno", anno));

		if (ValidazioneDati.isFilled(key1))
			c.add(Restrictions.eq("key1", key1));

		return (Tbf_contatore) c.uniqueResult();
	}

}
