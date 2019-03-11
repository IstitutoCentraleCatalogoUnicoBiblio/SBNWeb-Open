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

import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_biblioteca_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Restrictions;

public class ServiziIllDAO extends ServiziBaseDAO {

	public Tbl_biblioteca_ill getBibliotecaByIsil(String idb) throws DaoManagerException {
		return getBibliotecaByIsil(idb, true);
	}

	public Tbl_biblioteca_ill getBibliotecaByIsil(String idb, boolean soloAttivi) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbl_biblioteca_ill.class);
		if (soloAttivi)
			c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("cd_isil", idb));
		return (Tbl_biblioteca_ill) c.uniqueResult();
	}

	public Tbl_biblioteca_ill aggiornaBiblioteca(Tbl_biblioteca_ill biblioteca_ill) throws DaoManagerException, DAOConcurrentException {
		Session session = this.getCurrentSession();
		try {
			if (biblioteca_ill.getId_biblioteca_ill() == 0) {
				biblioteca_ill.setTs_ins(now());
				biblioteca_ill.setUte_ins(biblioteca_ill.getUte_var());
				session.save(biblioteca_ill);
			} else
				//update
				biblioteca_ill = (Tbl_biblioteca_ill) session.merge(biblioteca_ill);

			return biblioteca_ill;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbf_biblioteca> getCodAnagrafeBibliotecheILLFornitrici(
			Collection<String> codiciAnagrafe) throws DaoManagerException,
			DAOConcurrentException {

		Session session = this.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_biblioteca.class);
		//c.setProjection(Projections.projectionList().add(Projections.property("cd_ana_biblioteca")));
		//c.setResultTransformer(new TrimResultTransformer());

		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.in("cd_ana_biblioteca", codiciAnagrafe));

		Criteria c2 = c.createCriteria("bibliotecheIll", "ill");
		c2.add(Restrictions.ne("ill.fl_canc", 'S'));
		c2.add(Restrictions.in("ill.fl_ruolo", new Character[] { 'E', 'D' }));

		return c.list();
	}

	public void cancellaRichiestaILL(int idRichiestaILL, String firmaUtente)
			throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		Tbl_dati_richiesta_ill dati_ill = (Tbl_dati_richiesta_ill) session.get(Tbl_dati_richiesta_ill.class, idRichiestaILL);
		Tbl_richiesta_servizio rs = dati_ill.getRichiesta();
		if (rs != null) {
			//si scollega la richiesta locale, mantenendo traccia su un campo di storico.
			dati_ill.setRichiesta(null);
			dati_ill.setCod_rich_serv_old(rs.getCod_rich_serv());
		}
		dati_ill.setUte_var(firmaUtente);
		dati_ill.setFl_canc('S');

		session.update(dati_ill);

	}

}
