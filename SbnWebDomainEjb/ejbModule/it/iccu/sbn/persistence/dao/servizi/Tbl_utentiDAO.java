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

import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

public class Tbl_utentiDAO extends DaoManager {



	public Tbl_utentiDAO() {
		super();
	}

	public List<Tbl_utenti> selectAll() throws DaoManagerException
	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbl_utenti.class);
		List<Tbl_utenti> ret = criteria.list();
		return  ret;
	}
	public Tbl_utenti select(int id_bibliotecario) throws DaoManagerException
	{
		Session session = this.getCurrentSession();
		return (Tbl_utenti)loadNoLazy(session, Tbl_utenti.class,id_bibliotecario);
	}

	public Tbl_utenti select(String userId,String password, boolean checkPassword) throws DaoManagerException, UtenteNotFoundException
	{

		Session session = this.getCurrentSession();

		Tbl_utenti bib = new Tbl_utenti();
		bib.setCod_utente(userId);
		bib.setPassword(password);
		bib.setFl_canc('N');
		Example example = Example.create(bib)
	    .excludeZeroes()           //exclude zero valued properties
	    .ignoreCase()              //perform case insensitive string comparisons
		.excludeProperty("sesso")
		.excludeProperty("change_password");
//	    .enableLike();             //use like for string comparisons

		if (!checkPassword)
			//escludi controllo password
			example.excludeProperty("password");

		return (Tbl_utenti) session.createCriteria(Tbl_utenti.class).add(example).uniqueResult();
	}

	public Tbl_utenti selectByCodFiscale(String codFiscale, String encryptPwd, boolean checkPassword)
			throws DaoManagerException, UtenteNotFoundException {

		Session session = this.getCurrentSession();

		Criteria c = session.createCriteria(Tbl_utenti.class);
		c.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.eq("cod_fiscale", codFiscale).ignoreCase());
		if (checkPassword)
			c.add(Restrictions.eq("password", encryptPwd));

		return (Tbl_utenti) c.uniqueResult();
	}

	public Tbl_utenti select(String userId) throws DaoManagerException,UtenteNotFoundException
	{
		Session session = this.getCurrentSession();


		Tbl_utenti bib = new Tbl_utenti();
		bib.setCod_utente(userId);
		bib.setFl_canc('N');
		Example example = Example.create(bib)
	    .excludeZeroes()           //exclude zero valued properties
	    .excludeProperty("change_password")  //exclude the property named "color"
	    .ignoreCase();              //perform case insensitive string comparisons
//	    .enableLike();             //use like for string comparisons

		List results = session.createCriteria(Tbl_utenti.class)
	    .add(example)
	    .list();

		if(results.size()==0)
			throw new UtenteNotFoundException();

		return (Tbl_utenti)results.get(0);
	}

	public Set listaBiblioUte(Tbl_utenti ute) throws DaoManagerException,UtenteNotFoundException
	{
		Session session = this.getCurrentSession();
		//Set listaBib = session.createFilter(ute.getTrl_utenti_biblioteca(),"fl_canc<>S");

		Set listaBib = ute.getTrl_utenti_biblioteca();

		return listaBib;
	}


	public boolean update(Tbl_utenti bibliotecario) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try
		{
			bibliotecario.setTs_var(DaoManager.now());
			bibliotecario.setUte_var(""+bibliotecario.getId_utenti());
			//Tbl_utenti tb_polo1 = (Tbl_utenti)loadNoProxy(session, Tbl_utenti.class,new Integer(bibliotecario.getutente_professionale().getId_utente_professionale()));
			session.merge(bibliotecario);
		}catch (org.hibernate.ObjectNotFoundException e)
		{
			return false;
		}
		return true;
	}
	public boolean setLastAccess(Tbl_utenti bibliotecario) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try
		{
			//bibliotecario.setUte_var(bibliotecario.getUserid());
			//Tbf_bibliotecario tb_polo1 = (Tbf_bibliotecario)loadNoProxy(session, Tbf_bibliotecario.class,new Integer(bibliotecario.getId_utente_professionale().getId_utente_professionale()));
			//session.merge(bibliotecario);
		}catch (org.hibernate.ObjectNotFoundException e)
		{
			return false;
		}
		return true;
	}

	public boolean disable(Tbl_utenti bibliotecario) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try
		{
			bibliotecario.setTs_var(DaoManager.now());
			//bibliotecario.setUte_var(bibliotecario.getUserid());
			//Tbl_utenti tb_polo1 = (Tbl_utenti)loadNoProxy(session, Tbl_utenti.class,new Integer(bibliotecario.getId_utente_professionale().getId_utente_professionale()));
			session.merge(bibliotecario);
		}catch (org.hibernate.ObjectNotFoundException e)
		{
			return false;
		}
		return true;
	}

}
