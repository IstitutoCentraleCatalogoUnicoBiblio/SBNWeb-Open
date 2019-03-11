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
package it.iccu.sbn.persistence.dao.documentofisico;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.viste.Vc_inventario_coll;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe;
import it.iccu.sbn.vo.custom.documentofisico.InventarioPossedutoVO;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

public class Tbc_collocazioneDao extends DFCommonDAO {

	Connection connection = null;

	public Tbc_collocazioneDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Tbc_collocazione> selectAll()
	throws DaoManagerException	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbc_collocazione.class);
		List<Tbc_collocazione> ret = criteria.list();
		return  ret;
	}
	public List<Tbc_collocazione> selectBid(String bid)
	throws DaoManagerException	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbc_collocazione.class);
		List<Tbc_collocazione> ret = criteria.list();
		return  ret;
	}

	public boolean getCollocazione(/*String codPolo, String codBib,*/
			String codPoloSez, String codBibSez, String codSez,	String codCollo, String specColloc)
	throws DaoManagerException {

		boolean ret = false;
		Tbc_collocazione rec = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPoloSez);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBibSez);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			//
			rec = new Tbc_collocazione();
			rec.setCd_sez(sezione);
//			rec.setCd_loc(codCollo);
//			rec.setSpec_loc(specColloc);
			rec.setOrd_loc(codCollo);
			rec.setOrd_spec(specColloc);
			rec.setFl_canc(' ');
//			rec = (Tbc_collocazione) loadNoProxy(session, Tbc_collocazione.class, rec);
			Example example = Example.create(rec).excludeNone().excludeZeroes();
			Number rec1 = (Number) session.createCriteria(Tbc_collocazione.class)
			.setProjection(Projections.rowCount())
			.add(example)
			.add(Restrictions.eq("cd_sez", sezione)).uniqueResult();
			if (rec1.intValue() > 0){
				ret = true;
			}
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ret;
	}

	public boolean getCollocazione(String codPoloSez, String codBibSez, String codSez,
			String codCollo, String specColloc, int keyLoc)
	throws DaoManagerException {

		boolean ret = false;
		Tbc_collocazione rec = null;
		try{
			Session session = this.getCurrentSession();
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPoloSez);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBibSez);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			Number rec1 = (Number) session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.ne("key_loc", keyLoc))
//			.add(Restrictions.eq("cd_loc", specColloc))//errore
//			.add(Restrictions.eq("spec_loc", codSez))//errore
			.add(Restrictions.eq("ord_loc", codCollo))
			.add(Restrictions.eq("ord_spec", specColloc))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).uniqueResult();

			if (rec1.intValue() > 0){
				ret = true;
			}
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ret;
	}

	public int countAltreCollocazioniPerEsemplare(Tbc_collocazione collocazione)
	throws DaoManagerException {

		try{
			//almaviva5_20110912 #4450 la count delle coll. legate all'esemplare non deve
			//essere filtrata per sezione.
			Session session = this.getCurrentSession();
			Number cnt = (Number) session.createCriteria(Tbc_collocazione.class)
				.add(Restrictions.eq("cd_biblioteca_doc", collocazione.getCd_biblioteca_doc()))
				.add(Restrictions.ne("key_loc", collocazione.getKey_loc()))
				.add(Restrictions.ne("fl_canc", 'S')).setProjection(Projections.rowCount())
				.uniqueResult();

			return cnt.intValue();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}


	public List<Tbc_collocazione> getListaCollocazioni(String bid)
			throws DaoManagerException {

		List<Tbc_collocazione> lista = null;
		try {
			Session session = this.getCurrentSession();

			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			Criteria c = session.createCriteria(Tbc_collocazione.class);
			c.add(Restrictions.eq("b", titolo))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.gt("tot_inv", 0))
					.addOrder(Order.asc("ord_loc"))
					.addOrder(Order.asc("cd_loc"))
					.addOrder(Order.asc("spec_loc"));

			lista = c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

		return lista;
	}

	public List getListaCollocazioniDiEsemplare(String codPoloDoc, String codBibDoc, String bidDoc, int codDoc)
	throws DaoManagerException {
		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPoloDoc);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBibDoc);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bidDoc);
			Tbc_esemplare_titolo esemplare = new Tbc_esemplare_titolo();
			esemplare.setCd_polo(bib);
			esemplare.setB(titolo);
			esemplare.setCd_doc(codDoc);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("cd_biblioteca_doc", esemplare))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))

			.addOrder(Order.asc("cd_sez"))
			.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("ord_spec"))
			.addOrder(Order.asc("cd_loc"))
			.addOrder(Order.asc("spec_loc")).list();


		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCollocDoc(String codPoloDoc, String codBibDoc, String bidDoc, int codDoc)
	throws DaoManagerException {
		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPoloDoc);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBibDoc);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bidDoc);
			Tbc_esemplare_titolo esemplare = new Tbc_esemplare_titolo();
			esemplare.setCd_polo(bib);
			esemplare.setB(titolo);
			esemplare.setCd_doc(codDoc);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("cd_biblioteca_doc", esemplare))
			.add(Restrictions.ne("fl_canc", 'S')).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

public List getListaCollocazioni(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("b", titolo))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("cd_loc"))
			.addOrder(Order.asc("spec_loc"))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib)).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCollocazioni(String codPolo, String codBib, String bid, int codDoc, int keyLoc)
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			Tbc_esemplare_titolo esempl = new Tbc_esemplare_titolo();
			esempl.setCd_polo(bib);
			esempl.setB(titolo);
			esempl.setCd_doc(codDoc);
			lista = session.createCriteria(Tbc_collocazione.class)
//			.add(Restrictions.eq("b", titolo))
			.add(Restrictions.eq("cd_biblioteca_doc", esempl))
			.add(Restrictions.ne("key_loc", keyLoc))
			.add(Restrictions.ne("fl_canc", 'S'))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib)).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String ord, int keyLoc, int limite)
//	public List getListaCollocazioni(String codPolo, String codBib, String codSez,
//			String loc,	String specLoc, int keyLoc, int limite) ddddddddddd
	throws DaoManagerException {
		//tipo richiesta 1

		List lista = null;
		Order ordColl = null;

		if (ord != null){
			if (ord.startsWith("ord_")){
				if (ord.endsWith("desc")){
					ordColl = Order.desc("ord_loc");
				}else if (ord.endsWith("asc")){
					ordColl = Order.asc("ord_loc");
				}
			}
		}

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);


			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.add(Restrictions.or(this.ordLocKeyLoc(loc, keyLoc), this.daOrdLocAOrdLoc(loc, aLoc)))

			.addOrder(ordColl)
			.addOrder(Order.asc("ord_spec"))
			.addOrder(Order.asc("cd_loc"))
			.addOrder(Order.asc("spec_loc"))
			.addOrder(Order.asc("key_loc"))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).setMaxResults(limite).list();


		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCollocazioniEtichette(String codPolo, String codBib, String codSez,
			String loc, String aLoc)
	throws DaoManagerException {

		List lista = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.between("ord_loc", loc, aLoc))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String daSpec, String spec, String ord, int limite)
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("ord_loc", loc))
			.add(Restrictions.between("ord_spec", daSpec, spec))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("cd_loc"))
			.addOrder(Order.asc("spec_loc"))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib)).setMaxResults(limite).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String ord, int limite)
	throws DaoManagerException {

		List lista = null;
		Order ordColl = null;

		if (ord != null){
			if (ord.startsWith("ord_")){
				if (ord.endsWith("desc")){
					ordColl = Order.desc("ord_loc");
				}else if (ord.endsWith("asc")){
					ordColl = Order.asc("ord_loc");
				}
			}
		}

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.between("ord_loc", loc, aLoc))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
//			.addOrder(ordColl)
			.addOrder(ordColl)
			.addOrder(Order.asc("ord_spec"))
			.addOrder(Order.asc("cd_loc"))
			.addOrder(Order.asc("spec_loc"))
			.addOrder(Order.asc("key_loc"))
//			.addOrder(ordSpec)
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).setMaxResults(limite).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String daSpec, String aSpec, String ord, int limite, int keyLoc, String primoCodLoc, String ultOrdLoc)
	throws DaoManagerException {


		//tipo 0, tipo 1, tipo 1, tipo 2,
		List lista = null;
		Order ordLoc = null;
		Order ordSpec = null;
		Order ordColl = null;
		Order ordSpecLoc = null;

		if (ord != null){
			if (ord.startsWith("ord_")){
				if (ord.endsWith("desc")){
					ordLoc = Order.desc("ord_loc");
					ordSpec = Order.desc("ord_spec");
					ordColl = Order.desc("cd_loc");
					ordSpecLoc = Order.desc("spec_loc");
				}else if (ord.endsWith("asc")){
					ordLoc = Order.asc("ord_loc");
					ordSpec = Order.asc("ord_spec");
					ordColl = Order.asc("cd_loc");
					ordSpecLoc = Order.asc("spec_loc");
				}
			}
		}

		try{

			boolean esattoColl = loc.equals(aLoc);
			boolean esattoSpec = daSpec.equals(aSpec);

			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			Criteria cr = session.createCriteria(Tbc_collocazione.class)
			//
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.addOrder(ordLoc)
			.addOrder(ordSpec)
			.addOrder(ordColl)
			.addOrder(ordSpecLoc)
			.addOrder(Order.asc("key_loc"));

			if (keyLoc == 0) {	// primo giro
				if (esattoColl)
					cr.add(Restrictions.eq("ord_loc", loc));
				else
					cr.add(Restrictions.between("ord_loc", loc, aLoc));

				if (esattoSpec)
					cr.add(Restrictions.eq("ord_spec", daSpec));
				else
					cr.add(Restrictions.between("ord_spec", daSpec, aSpec));

			}else{	// segue lista
				if (esattoColl && esattoSpec){
					cr.add(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc));
				}else{
					if (!esattoColl){
						if (ord != null){
							if (ord.startsWith("ord_")){
								if (ord.endsWith("desc")){
									cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), daOrdLocAOrdLocDesc(primoCodLoc, ultOrdLoc)));
								}else{
//									cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), daOrdLocAOrdLoc(loc, aLoc)));
									cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), daOrdLocAOrdLocOrdSpec(loc, aLoc, daSpec)));
								}
							}
						}
					}else{
						if (esattoColl && !esattoSpec){
							cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), ordLocDaSpecASpec(loc, daSpec, aSpec)));
						}
					}
				}
			}
			lista = cr.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).setMaxResults(limite).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public int getCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String daSpec, String aSpec, int keyLoc, String ord, String primoCodLoc, String ultOrdLoc)
	throws DaoManagerException {

		try{
			//tipo 0, tipo 1, tipo 2, tipo 3,
			boolean esattoColl = loc.equals(aLoc);
			boolean esattoSpec = daSpec.equals(aSpec);

			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			Criteria cr = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0));
			if (keyLoc == 0) {	// primo giro
				if (esattoColl)
					cr.add(Restrictions.eq("ord_loc", loc));
				else
					cr.add(Restrictions.between("ord_loc", loc, aLoc));

				if (esattoSpec)
					cr.add(Restrictions.eq("ord_spec", daSpec));
				else
					cr.add(Restrictions.between("ord_spec", daSpec, aSpec));

			}else{	// segue lista
				if (esattoColl && esattoSpec){
					cr.add(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc));
				}else{
					if (!esattoColl){
						if (ord != null && ord.equals("ord_loc desc")){
							cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), daOrdLocAOrdLocDesc(primoCodLoc, ultOrdLoc)));
						}else{
//							cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), daOrdLocAOrdLoc(loc, aLoc)));
							cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), daOrdLocAOrdLocOrdSpec(loc, aLoc, daSpec)));
						}
					}else{
						if (esattoColl && !esattoSpec)
							cr.add(Restrictions.or(ordLocOrdSpecKeyLoc(loc, daSpec, keyLoc), ordLocDaSpecASpec(loc, daSpec, aSpec)));
					}
				}
			}

			Number count = (Number) cr.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
				.add(Restrictions.eq("cd_polo", bib))
				.add(Restrictions.eq("cd_sez", codSez)).uniqueResult();

			return count.intValue();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}

	public List getListaCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, int limite) //lente collocazioni richiesta=6
	throws DaoManagerException {

		return this.getListaCollocazioni(codPolo, codBib, codSez, loc, aLoc, limite, false);
	}

	public List getListaCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, int limite, boolean segue) //lente collocazioni richiesta=7
	throws DaoManagerException {
		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			Criteria criteria = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.projectionList()
					.add(Projections.rowCount(), "numOcc")
					.add(Projections.sum("tot_inv"))
					.add(Projections.groupProperty("cd_loc"), "codLoc")
					.add(Projections.groupProperty("ord_loc")))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.gt("tot_inv", 0));

			if (segue)
				criteria.add(daOrdLocAOrdLoc(loc, aLoc));
			else
				criteria.add(Restrictions.between("ord_loc", loc, aLoc));

			criteria.addOrder(Order.asc("ord_loc"))
						.addOrder(Order.asc("cd_loc"))
						.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
						.add(Restrictions.eq("cd_polo", bib))
						.add(Restrictions.eq("cd_sez", codSez));

			lista = criteria.setMaxResults(limite).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaSpecificazioni(String codPolo, String codBib, String codSez,
			String loc, String daSpec, String aSpec, int limite) //lente collocazioni richiesta=6
	throws DaoManagerException {

		return this.getListaSpecificazioni(codPolo, codBib, codSez, loc, daSpec, aSpec, limite, false);
	}

	public List getListaSpecificazioni(String codPolo, String codBib, String codSez,
			String loc, String daSpec, String aSpec, int limite, boolean segue) //lente collocazioni richiesta=7
	throws DaoManagerException {
		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			Criteria criteria = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.projectionList()
					.add(Projections.rowCount(), "numOcc")
					.add(Projections.sum("tot_inv"))
					.add(Projections.groupProperty("ord_loc"), "ordLoc")
					.add(Projections.groupProperty("cd_loc"), "codLoc")
					.add(Projections.groupProperty("ord_spec"), "ordSpec")
					.add(Projections.groupProperty("spec_loc"), "specLoc"))
					.add(Restrictions.eq("ord_loc", loc))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.gt("tot_inv", 0));

			if (segue)
				criteria.add(daOrdSpecAOrdSpec(daSpec, aSpec));
			else
				criteria.add(Restrictions.between("ord_spec", daSpec, aSpec));

			criteria.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("ord_spec"))
			.addOrder(Order.asc("cd_loc"))
			.addOrder(Order.asc("spec_loc"))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez));

			lista = criteria.setMaxResults(limite).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}


	public List getListaUltCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc) //bottone ult coll
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			lista = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.projectionList()
					.add(Projections.rowCount(), "numOcc")
					.add(Projections.groupProperty("ord_loc"))
					.add(Projections.groupProperty("cd_loc"), "codLoc"))
					.add(Restrictions.between("ord_loc", loc, aLoc))
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.gt("tot_inv", 0))
					.addOrder(Order.desc("ord_loc"))
					.addOrder(Order.desc("cd_loc"))
					.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
					.add(Restrictions.eq("cd_polo", bib))
					.add(Restrictions.eq("cd_sez", codSez)).setMaxResults(DocumentoFisicoCostant.NUM_REC_ULTCOLLSPEC).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}
	public List getListaUltSpecificazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc) //bottone ult coll
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			lista = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.projectionList()
					.add(Projections.rowCount(), ("numOcc"))
					.add(Projections.groupProperty("ord_loc"), "codLoc")
					.add(Projections.groupProperty("cd_loc"), "codLoc")
					.add(Projections.groupProperty("ord_spec"), "specLocUpper")
//					.add(Projections.groupProperty("spec_loc"), "specLocUpper")
					.add(Projections.groupProperty("spec_loc"), "specLoc"))
					.add(Restrictions.between("ord_loc", loc, aLoc))
					.add(Restrictions.ne("fl_canc", 'S'))
					.addOrder(Order.desc("ord_loc"))
					.addOrder(Order.desc("cd_loc"))
					.addOrder(Order.desc("specLocUpper"))
					.addOrder(Order.desc("spec_loc"))
//					.addOrder(Order.asc("ord_spec"))
					.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
					.add(Restrictions.eq("cd_polo", bib))
					.add(Restrictions.eq("cd_sez", codSez)).setMaxResults(DocumentoFisicoCostant.NUM_REC_ULTCOLLSPEC).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

//	public List getListaSpecificazioni(String codPolo, String codBib, String codSez,
//			String loc, String daSpec, String aSpec, int limite) //lente specificazioni richiesta=8
//	throws DaoManagerException {
//
//		List lista = null;
//		try{
//			Session session = this.getCurrentSession();
//
//			Tbf_polo polo = new Tbf_polo();
//			polo.setCd_polo(codPolo);
//			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
//			bib.setCd_polo(polo);
//			bib.setCd_biblioteca(codBib);
//			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
//			sezione.setCd_polo(bib);
//			sezione.setCd_sez(codSez);
//			lista = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.projectionList()
//					.add(Projections.rowCount(), ("numOcc"))
//					.add(Projections.sum("tot_inv"))
//					.add(Projections.groupProperty("ord_loc"), "ordLoc")
//					.add(Projections.groupProperty("cd_loc"), "codLoc")
//					.add(Projections.groupProperty("spec_loc"), "specLocUpper")
//					.add(Projections.groupProperty("spec_loc"), "specLoc"))
//					.add(Restrictions.eq("ord_loc", loc))
//					.add(Restrictions.between("ord_spec", daSpec, aSpec))
//					.add(Restrictions.ne("fl_canc", 'S'))
//					.addOrder(Order.asc("ord_loc"))
//					.addOrder(Order.asc("cd_loc"))
//					.addOrder(Order.asc("specLocUpper"))
//					.addOrder(Order.asc("spec_loc"))
//					.createCriteria("cd_sez")
//					.add(Restrictions.eq("cd_polo", bib))
//					.add(Restrictions.eq("cd_sez", codSez)).setMaxResults(limite).list();
//		}catch (HibernateException e){
//			throw new DaoManagerException(e);
//		}
//		return lista;
//	}

	public List getListaSpecificazioni(String codPolo, String codBib, String codSez,
			String loc, String daSpec, String aSpec, int keyLoc,  int limite) //lente specificazioni richiesta=9
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			lista = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.projectionList()
					.add(Projections.rowCount(), ("numOcc"))
					.add(Projections.sum("tot_inv"))
					.add(Projections.groupProperty("ord_loc"), "ordLoc")
					.add(Projections.groupProperty("cd_loc"), "codLoc")
					.add(Projections.groupProperty("spec_loc"), "specLocUpper")
					.add(Projections.groupProperty("spec_loc"), "specLoc"))
					.add(Restrictions.eq("ord_loc", loc))
					.add(Restrictions.or(this.ordSpecKeyLoc(daSpec, keyLoc), this.daOrdSpecAOrdSpec(daSpec, aSpec)))
					.add(Restrictions.ne("fl_canc", 'S'))
					.addOrder(Order.asc("ord_loc"))
					.addOrder(Order.asc("cd_loc"))
					.addOrder(Order.asc("specLocUpper"))
					.addOrder(Order.asc("spec_loc"))
					.createCriteria("cd_sez")
					.add(Restrictions.eq("cd_polo", bib))
					.add(Restrictions.eq("cd_sez", codSez)).setMaxResults(limite).list();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, int keyLoc)
	throws DaoManagerException {

		List countColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			countColl = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.add(Restrictions.or(this.ordLocKeyLoc(loc, keyLoc), this.daOrdLocAOrdLoc(loc, aLoc)))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).list();





		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countColl;
	}

	public List getCollocazioni(String codPolo, String codBib, String codSez,
			String loc,	String daSpec, String spec)
	throws DaoManagerException {

		List countColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			countColl = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.eq("ord_loc", loc))
			.add(Restrictions.between("ord_spec", daSpec, spec))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib)).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countColl;
	}

	public List getCollocazioni(String codPolo, String codBib, String codSez)
	throws DaoManagerException {

		List countColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			countColl = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countColl;
	}
	public int countGroupCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc)throws DaoManagerException {
		return this.countGroupCollocazioni(codPolo, codBib, codSez, loc, aLoc, false);
	}

	public int countGroupCollocazioni(String codPolo, String codBib, String codSez,
			String loc, String aLoc, boolean segue)
	throws DaoManagerException {

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);

			/*
			countColl = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.between("ord_loc", loc, aLoc))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).list();

			*/

			String sql = "select count(subq.*) as numColl from ";
			sql += "(select coll.ord_loc from tbc_collocazione coll ";
			if (segue)
				sql += " where coll.ord_loc> :loc and coll.ord_loc<=:aLoc ";
			else
				sql += " where coll.ord_loc between :loc and :aLoc ";

			sql += " and coll.fl_canc<>'S' ";
			sql += " and coll.tot_inv>0 ";
			sql += " and coll.cd_polo_sezione=:polo ";
			sql += " and coll.cd_biblioteca_sezione=:bib ";
			sql += " and coll.cd_sez=:sez  ";
			sql += " group by coll.cd_loc, coll.ord_loc) as subq";

			SQLQuery query = session.createSQLQuery(sql);
			//Query query = session.getNamedQuery("esameCollocazioni_contaGroupCollocazioni");
			query.setString("loc", loc);
			query.setString("aLoc", aLoc);
			query.setString("polo", codPolo);
			query.setString("bib", codBib);
			query.setString("sez", codSez);
			Number countColl = (Number) query.uniqueResult();

			return countColl.intValue();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}

	public int countGroupSpecificazioni(String codPolo, String codBib, String codSez,
			String loc, String daSpec, String aSpec)throws DaoManagerException {
		return this.countGroupSpecificazioni(codPolo, codBib, codSez, loc, daSpec, aSpec, false);
	}

	public int countGroupSpecificazioni(String codPolo, String codBib, String codSez,
			String loc, String daSpec, String aSpec, boolean segue)
	throws DaoManagerException {

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);

			String sql = "select count(subq.*) as numSpec from ";
			sql += "(select coll.ord_loc from tbc_collocazione coll ";
			if (segue)
				sql += " where coll.ord_spec > :daSpec and coll.ord_spec<=:aSpec ";
			else
				sql += " where coll.ord_spec between :daSpec and :aSpec ";

			sql += " and coll.ord_loc=:loc ";
			sql += " and coll.fl_canc<>'S' ";
			sql += " and coll.tot_inv>0 ";
			sql += " and coll.cd_polo_sezione=:polo ";
			sql += " and coll.cd_biblioteca_sezione=:bib ";
			sql += " and coll.cd_sez=:sez  ";
			sql += " group by coll.cd_loc, coll.ord_loc, coll.ord_spec, coll.spec_loc) as subq";

			SQLQuery query = session.createSQLQuery(sql);
			//Query query = session.getNamedQuery("esameCollocazioni_contaGroupCollocazioni");
			query.setString("loc", loc);
			query.setString("aSpec", aSpec);
			query.setString("daSpec", daSpec);
			query.setString("polo", codPolo);
			query.setString("bib", codBib);
			query.setString("sez", codSez);
			Number countColl = (Number) query.uniqueResult();

			return countColl.intValue();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}

//	public List getSpecificazioni(String codPolo, String codBib, String codSez,
//			String loc, String daSpec, String aSpec)//count tipoRichiesta=8
//	throws DaoManagerException {
//
//		List countColl = null;
//
//		try{
//			Session session = this.getCurrentSession();
//
//			Tbf_polo polo = new Tbf_polo();
//			polo.setCd_polo(codPolo);
//			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
//			bib.setCd_polo(polo);
//			bib.setCd_biblioteca(codBib);
//			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
//			sezione.setCd_polo(bib);
//			sezione.setCd_sez(codSez);
//			countColl = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
//			.add(Restrictions.eq("ord_loc", loc))
//			.add(Restrictions.between("ord_spec", daSpec, aSpec))
//			.add(Restrictions.ne("fl_canc", 'S'))
//			.add(Restrictions.gt("tot_inv", 0))
//			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
//			.add(Restrictions.eq("cd_polo", bib))
//			.add(Restrictions.eq("cd_sez", codSez)).list();
//
//		}catch (HibernateException e){
//			throw new DaoManagerException(e);
//		}
//		return countColl;
//	}
	public List getSpecificazioni(String codPolo, String codBib, String codSez,
			String loc, String daSpec, String aSpec, int ultKeyLoc)//count tipoRichiesta=9
	throws DaoManagerException {

		List countColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			countColl = session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.eq("ord_loc", loc))
			.add(Restrictions.between("ord_spec", daSpec, aSpec))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("cd_sez", codSez)).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countColl;
	}

	public Tbc_collocazione getCollocazione(int keyLoc)
	throws DaoManagerException {

		Tbc_collocazione rec = null;
		try{
			Session session = this.getCurrentSession();

			rec = (Tbc_collocazione) session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("key_loc", keyLoc)).uniqueResult();

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public Tbc_collocazione getCollocazione(int keyLoc, Tbc_inventario inventario)
	throws DaoManagerException {

		Tbc_collocazione rec = null;
		Tbc_inventario inv = null;
		try{
			Session session = this.getCurrentSession();
			rec = (Tbc_collocazione) session.createCriteria(Tbc_collocazione.class).add(Restrictions.eq("key_loc", keyLoc)).uniqueResult();
			inventario.setKey_loc(rec);
			rec.getTbc_inventario().add(inventario);
			session.saveOrUpdate(inventario);//rec);

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}
	public Tbc_collocazione getCollocazione(Tbc_collocazione coll, Tbc_inventario inv)
	throws DaoManagerException {

		try{
			Session session = this.getCurrentSession();
			Tbc_inventario invRef = (Tbc_inventario) session.get(Tbc_inventario.class, inv);
			invRef.setKey_loc(coll);
			coll.getTbc_inventario().add(invRef);
			session.saveOrUpdate(coll);



		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return coll;
	}

	public Tbc_collocazione inserimentoCollocazione(CollocazioneVO coll)
	throws DaoManagerException	{

		boolean ret = false;
		Session session = this.getCurrentSession();
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());

		try{
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(coll.getCodPoloSez());
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(coll.getCodBibSez());
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(coll.getCodSez());


			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(coll.getBid());

			Tbc_collocazione rec = new Tbc_collocazione();
			if (coll.getCodSistema() != null){
				Tb_classe classe = new Tb_classe();
				classe.setCd_sistema(coll.getCodSistema());
				classe.setCd_edizione(coll.getCodEdizione());
				classe.setClasse(coll.getClasse());
				rec.setCd_sistema(classe);
			}else{
				rec.setCd_sistema(null);
			}
			rec.setCd_sez(sezione);
			if (coll.getBidDoc() != null){
				Tbc_esemplare_titolo esemplare = new Tbc_esemplare_titolo();
				esemplare.setCd_polo(bib);
				Tb_titolo titoloDoc = new Tb_titolo();
				titoloDoc.setBid(coll.getBidDoc());
				esemplare.setB(titoloDoc);
				esemplare.setCd_doc(coll.getCodDoc());
				rec.setCd_biblioteca_doc(esemplare);
			}else{
				rec.setCd_biblioteca_doc(null);
			}
			rec.setB(titolo);
			rec.setCd_loc(coll.getCodColloc());
			rec.setSpec_loc(coll.getSpecColloc());
			rec.setConsis(coll.getConsistenza());
			rec.setTot_inv(coll.getTotInv());
			rec.setIndice(coll.getIndice());
			rec.setOrd_loc(coll.getOrdLoc());
			rec.setOrd_spec(coll.getOrdSpec());
			rec.setTot_inv_prov(coll.getTotInvProv());
			rec.setTs_ins(ts);
			rec.setTs_var(ts);
			rec.setUte_ins(coll.getUteIns());
			rec.setUte_var(coll.getUteIns());
			rec.setFl_canc(coll.getCancDb2i().charAt(0));
			session.save(rec);
			//session.flush();
			return rec;
		}catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	public boolean modificaCollocazione(Tbc_collocazione collocazione)
	throws DaoManagerException {
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(collocazione);//rec);
//			session.flush();
			return true;
		}catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}
	public List getCollocazionePerFormati(String codPoloSez, String codBibSez, String codSez,
			String codColloc, String specColloc)
	throws DaoManagerException {

		boolean ret = false;
		List collocazioni = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPoloSez);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBibSez);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			//
			collocazioni = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("cd_sez", sezione))
			.add(Restrictions.eq("ord_loc", codColloc))
			.add(Restrictions.eq("spec_loc", specColloc))
			.add(Restrictions.ne("fl_canc", 'S')).list();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return collocazioni;
	}

	public int countCollocazioniPerPosseduto(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Number countColl = 0;

		try{
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

			countColl = (Number)session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.eq("cd_sez.cd_polo", bib))
			.add(Restrictions.eq("b.id", bid))
			.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countColl.intValue();
	}

	public int countCollocazioniPerPossedutoAltreBib(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Number countColl = null;

		try{
			Session session = this.getCurrentSession();

			countColl = (Number)session.createCriteria(Tbc_collocazione.class).setProjection(Projections.rowCount())
			.add(Restrictions.ne("cd_sez.cd_polo.cd_biblioteca", codBib))
			.add(Restrictions.eq("b.id", bid))
			.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countColl.intValue();
	}

	public List getListaCollocazioniTitolo(String bid)
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("b", titolo))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0)).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List<Tbc_collocazione> getListaCollocazioniTitolo(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		List lista = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			lista = session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("cd_sez.cd_polo", bib))
			.add(Restrictions.eq("b", titolo))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.addOrder(Order.asc("cd_sez"))
			.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("ord_spec")).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}
	public List getAltreBibColl(String bid)
	throws DaoManagerException	{
		List lista = null;
		try{
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("contaBibliotecheBidColl");//Hibernate/src/it/iccu/sbn/polo/mapping/documentofisico
			query.setString("bid", bid);
			lista = query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}
	public List getAltreBibColl(String bid, String codBib)
	throws DaoManagerException	{
		List lista = null;
		try{
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("contaBibliotecheBidBibColl");
			query.setString("bid", bid);
			query.setString("codBib", codBib);
			lista = query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}
	public List getCollocazioniPerEtichette(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String spec, String aSpec)
	throws DaoManagerException {


		List listaColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);

			listaColl = session.createCriteria(Tbc_collocazione.class)
			.add(intervalloColloc1(loc,  spec, aLoc, aSpec))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
//			.createCriteria("cd_sez")//sezione per prendere codPolo e codBib
//			.add(Restrictions.eq("cd_polo", bib))
//			.add(Restrictions.eq("cd_sez", codSez))
			.add(Restrictions.eq("cd_sez", sezione))
			.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("ord_spec")).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return listaColl;

	}

	public ScrollableResults getCollocazioniPerTopografico(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String spec, String aSpec)
	throws DaoManagerException {

		ScrollableResults listaColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			listaColl = session.createCriteria(Tbc_collocazione.class)
			.add(intervalloColloc1(loc, spec, aLoc, aSpec))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.add(Restrictions.eq("cd_sez", sezione))
			.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("ord_spec"))
			//.list();
			.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return listaColl;
	}

	public List getCollocazioniPerConservazione(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String spec, String aSpec)
	throws DaoManagerException {

		List listaColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			listaColl = session.createCriteria(Tbc_collocazione.class)
			.add(intervalloColloc1(loc, spec, aLoc, aSpec))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.gt("tot_inv", 0))
			.add(Restrictions.eq("cd_sez", sezione))
			.addOrder(Order.asc("ord_loc"))
			.addOrder(Order.asc("ord_spec")).list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return listaColl;
	}

	public List getCollocazioniPerConservazione_vista(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String spec, String aSpec, String ord,
			String statCons, String tipoMat) throws DaoManagerException {

		List listaColl = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);
			Criteria cr = session.createCriteria(Vc_inventario_coll.class);
			cr.add(intervalloColloc1(loc, spec, aLoc, aSpec))
			  .add(Restrictions.ne("fl_canc", 'S'))
			  .add(Restrictions.gt("tot_inv", 0))
			  .add(Restrictions.eq("cd_bib_sez", codBib))
			  .add(Restrictions.eq("cd_sez", sezione.getCd_sez()));

			//almaviva5_20150116
			if (ValidazioneDati.isFilled(statCons))
				cr.add(Restrictions.eq("stato_con", statCons));
			if (ValidazioneDati.isFilled(tipoMat))
				cr.add(Restrictions.eq("cd_mat_inv", tipoMat));
			//

			if (ord.equals("I")){
				cr.addOrder(Order.asc("cd_serie"))
				  .addOrder(Order.asc("cd_inven")).list();
			}else if (ord.equals("D")){
				cr.addOrder(Order.asc("data_ingresso"))
				  .addOrder(Order.asc("cd_serie"))
				  .addOrder(Order.asc("cd_inven")).list();
			}else if (ord.equals("C")){
				cr.addOrder(Order.asc("cd_sez"))
				  .addOrder(Order.asc("ord_loc"))
				  .addOrder(Order.asc("ord_spec"))
				  .addOrder(Order.asc("cd_loc"))
				  .addOrder(Order.asc("spec_loc"))
				  .addOrder(Order.asc("key_loc")).list();
			}
			listaColl = cr.list();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return listaColl;
	}

	public List<Vc_inventario_coll> getCollocStrumentiPatrimonio_vista(
			String codPolo, String codBib, String codSez, String loc,
			String aLoc, String spec, String aSpec, String tipoMat,
			String statoCon, String digit, String noDispo, String ord, boolean escludiDigit, String tipoDigit)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			StringBuilder sql = new StringBuilder(1024);

			Map<String, Object> params = new THashMap<String, Object>();
			params.put("codPolo", codPolo);
			params.put("codBib", codBib);
			params.put("codSez", codSez);
			params.put("coll_da", loc + spec);
			params.put("coll_a", aLoc + aSpec);

			sql.append("select inv.* from vc_inventario_coll inv");

			//almaviva5_20131003 evolutive google2
			if (ValidazioneDati.equals(digit, "N"))
				//non digitalizzati
				aggiungiFiltroInventariDigitalizzati(sql, escludiDigit, tipoDigit);

			sql.append(BLANK).append("where inv.fl_canc<>'S'");
			sql.append(BLANK).append("and inv.tot_inv>0");
			sql.append(BLANK).append("and inv.cd_sit in ('1', '2')");
			sql.append(BLANK).append("and inv.cd_polo=:codPolo");
			sql.append(BLANK).append("and inv.cd_bib=:codBib");
			sql.append(BLANK).append("and inv.cd_sez=:codSez");
			sql.append(BLANK).append("and rpad(inv.ord_loc,80)||rpad(inv.ord_spec,40) between :coll_da and :coll_a");

			if (ValidazioneDati.isFilled(tipoMat)) {
				sql.append(BLANK).append("and inv.cd_mat_inv=:tipoMat");
				params.put("tipoMat", tipoMat);
			}

			if (ValidazioneDati.isFilled(statoCon)) {
				sql.append(BLANK).append("and inv.stato_con=:statoCon");
				params.put("statoCon", statoCon);
			}

			if (ValidazioneDati.isFilled(digit)) {
				if (digit.equals("S")) {
					sql.append(BLANK).append("and (inv.digitalizzazione in ('0','1','2') )");
				} else if (digit.equals("N")) {
					sql.append(BLANK).append("and (inv.digitalizzazione isnull or inv.digitalizzazione='')");// tutti i non digitalizzati
					//almaviva5_20131003 evolutive google2
					if (escludiDigit)
						//escludi tutti gli inventari che hanno un'altra copia digitalizzata
						sql.append(BLANK).append("and i2 isnull");
					else
						//includi solo gli inventari che hanno un'altra copia digitalizzata
						sql.append(BLANK).append("and not i2 isnull");
				}
			}

			if (ValidazioneDati.isFilled(noDispo)) {
				sql.append(BLANK).append("and inv.cd_no_disp=:noDispo");
				params.put("noDispo", noDispo);
			}

			sql.append(BLANK).append("order by");
			if (ord.equals("I")) {
				sql.append(BLANK).append("inv.cd_serie, inv.cd_inven");
			} else if (ord.equals("D")) {
				sql.append(BLANK).append("inv.data_ingresso, inv.cd_serie, inv.cd_inven");
			} else if (ord.equals("C")) {
				sql.append(BLANK).append("inv.cd_sez, inv.ord_loc, inv.ord_spec, inv.cd_loc, inv.spec_loc, inv.key_loc");
			}

			SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("inv", Vc_inventario_coll.class);
			query.setProperties(params);

			return query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}


	public ScrollableResults getCollocazioniScrollable(String codPolo, String codBib, String codSez,
			String loc, String aLoc, String spec, String aSpec, int keyLoc)
	throws DaoManagerException {

		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_collocazione.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
			sezione.setCd_polo(bib);
			sezione.setCd_sez(codSez);

			cr.add(Restrictions.between("ord_loc", loc, aLoc));
			cr.add(Restrictions.between("spec_loc", spec, aSpec));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_sez", sezione));
			if (keyLoc > 0){
				cr.add(Restrictions.ge("key_loc", keyLoc));
			}
			cr.addOrder(Order.asc("key_loc"));
			ScrollableResults listaColl = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_INSENSITIVE);

			return listaColl;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tbc_collocazione getCollocazione(Tbc_sezione_collocazione sezione,
			String codLoc, String codSpec)
	throws DaoManagerException {

		Tbc_collocazione rec = null;
		try{
			Session session = this.getCurrentSession();

			rec = (Tbc_collocazione) session.createCriteria(Tbc_collocazione.class)
			.add(Restrictions.eq("cd_loc", codLoc))
			.add(Restrictions.eq("spec_loc", codSpec))
//			.add(Restrictions.eq("bid", titolo))
//			.add(Restrictions.eq("b", titolo))
			.createCriteria("cd_sez")
			.add(Restrictions.eq("cd_sez", sezione.getCd_sez())).uniqueResult();

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}
    private static final Criterion ordLocKeyLoc(String ordLoc, int keyLoc) {
        StringBuffer sql = new StringBuffer();
        sql.append("({alias}.ord_loc = '");
        sql.append(ordLoc);
        sql.append("' and {alias}.key_loc >");
        sql.append(keyLoc);
        sql.append(")");
        return Restrictions.sqlRestriction(sql.toString());
    }

    private static final Criterion ordSpecKeyLoc(String ordSpec, int keyLoc) {
        StringBuffer sql = new StringBuffer();
        sql.append("({alias}.ord_spec = '");
        sql.append(ordSpec);
        sql.append("' and {alias}.key_loc >");
        sql.append(keyLoc);
        sql.append(")");
        return Restrictions.sqlRestriction(sql.toString());
    }

    private static final Criterion daOrdSpecAOrdSpec(String daOrdSpec, String aOrdSpec) {
        StringBuffer sql = new StringBuffer();
        sql.append("({alias}.ord_spec > '");
        sql.append(daOrdSpec);
        sql.append("' and {alias}.ord_spec <='");
        sql.append(aOrdSpec);
        sql.append("')");
        return Restrictions.sqlRestriction(sql.toString());
    }

    public List<InventarioPossedutoVO> getInventariPosseduti(String codPolo, String codBib, String bid)throws DaoManagerException {

		try{
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("webservice_posseduto");
			query.setString("polo", codPolo);
			query.setString("bib", codBib);
			query.setString("bid", bid);
			query.setResultTransformer(Transformers.aliasToBean(InventarioPossedutoVO.class));

			return query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
}
