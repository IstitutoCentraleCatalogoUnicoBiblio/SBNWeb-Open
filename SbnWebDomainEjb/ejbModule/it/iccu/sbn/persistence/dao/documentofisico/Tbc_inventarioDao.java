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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloBollettinoVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_autore;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;



public class Tbc_inventarioDao extends DFCommonDAO {

	private static Logger log = Logger.getLogger(Tbc_inventarioDao.class);

	public enum TipoData {
		DATA_INGRESSO,
		DATA_PRIMA_COLL;
	}

	Connection connection = null;

	public Tbc_inventarioDao() {
		super();
	}

	public List<Tbc_inventario> selectAll()
	throws DaoManagerException	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbc_inventario.class);
		List<Tbc_inventario> ret = criteria.list();
		return  ret;
	}
	public List<Tbc_inventario> selectBid(String bid)
	throws DaoManagerException	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbc_inventario.class);
		List<Tbc_inventario> ret = criteria.list();
		return  ret;
	}

	public List<Tbc_inventario> getListaInventari(String bid)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();//HibernateUtil.getSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tb_titolo tit = new Tb_titolo();
			tit.setBid(bid);


			cr.add(Restrictions.eq("b", tit));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventari(String codPolo, String codBib, String bid)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			Tb_titolo tit = new Tb_titolo();
			tit.setBid(bid);

			//cr.add(Restrictions.eq("Cd_serie", serie));
			cr.add(Restrictions.eq("b", tit));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventariTitolo(String bid)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();//HibernateUtil.getSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tb_titolo tit = new Tb_titolo();
			tit.setBid(bid);


			cr.add(Restrictions.eq("b", tit));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.desc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventariTitolo(String codPolo, String codBib, String bid)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			Tb_titolo tit = new Tb_titolo();
			tit.setBid(bid);

			//cr.add(Restrictions.eq("Cd_serie", serie));
			cr.add(Restrictions.eq("b", tit));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.desc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventariTitoloPerServizi(String codPolo, String codBib, String bid)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			Tb_titolo tit = new Tb_titolo();
			tit.setBid(bid);

			//cr.add(Restrictions.eq("Cd_serie", serie));
			cr.add(Restrictions.eq("b", tit));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_sit", "2"));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.desc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventari(String codPolo, String codBib, String codBibO, String codTipOrd,
			int annoOrd, int codOrd, String codBibF)
			throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_bib_ord", codBibO));
			cr.add(Restrictions.eq("cd_tip_ord", codTipOrd));
			cr.add(Restrictions.eq("anno_ord", annoOrd));
			cr.add(Restrictions.eq("cd_ord", codOrd));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventariNonFatturati(String codPolo, String codBib, String codBibO, String codTipOrd,
			int annoOrd, int codOrd, String codBibF)
			throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_bib_ord", codBibO));
			cr.add(Restrictions.eq("cd_tip_ord", codTipOrd));
			cr.add(Restrictions.eq("anno_ord", annoOrd));
			cr.add(Restrictions.eq("cd_ord", codOrd));
			cr.add(Restrictions.eq("cd_bib_fatt", codBibF));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventari(String codPolo, String codBib, String codBibO, String codTipOrd,
			int annoOrd, int codOrd, String codBibF, int annoF, int prgF, int rigaF)
			throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_bib_ord", codBibO));
			cr.add(Restrictions.eq("cd_tip_ord", codTipOrd));
			cr.add(Restrictions.eq("anno_ord", annoOrd));
			cr.add(Restrictions.eq("cd_ord", codOrd));
			cr.add(Restrictions.eq("cd_bib_fatt", codBibF));
			cr.add(Restrictions.eq("anno_fattura", annoF));
			cr.add(Restrictions.eq("prg_fattura", prgF));
			cr.add(Restrictions.eq("riga_fattura", rigaF));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public ScrollableResults getCursorInventari(Tbc_collocazione keyLoc,
			String ord) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbc_inventario.class);

			c.add(Restrictions.eq("key_loc", keyLoc));
			c.add(Restrictions.ne("fl_canc", 'S'));

			if (ValidazioneDati.equals(ord, "S")) 	//seq. coll
				c.addOrder(Order.asc("seq_coll"));

			c.addOrder(Order.asc("cd_serie"));
			c.addOrder(Order.asc("cd_inven"));

			ScrollableResults results = c.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
			return results;

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventari(Tbc_collocazione keyLoc,
			String ord) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbc_inventario.class);

			c.add(Restrictions.eq("key_loc", keyLoc));
			c.add(Restrictions.ne("fl_canc", 'S'));

			if (ValidazioneDati.equals(ord, "S")) 	//seq. coll
				c.addOrder(Order.asc("seq_coll"));

			c.addOrder(Order.asc("cd_serie"));
			c.addOrder(Order.asc("cd_inven"));

			List<Tbc_inventario> results = c.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariStatoConservazione(Tbc_collocazione keyLoc, String ord)
	throws DaoManagerException	{
		try{
			boolean [] ret = {false, false, false, false, false};
			Order ordSerie = null;
			Order ordInv = null;
			Order dataIngr = null;
			Order codSez = null;
			Order ordLoc = null;
			Order orSpec = null;
			Order codLoc = null;
			Order codSpec = null;
			Order keyColl = null;
			//

			if (ord != null){
				if (ord.startsWith("I")){
					ordSerie = Order.asc("cd_serie");
					ordInv = Order.asc("cd_inven");
				}
				if (ord.startsWith("D")){
					dataIngr = Order.asc("data_ingresso");
					ordSerie = Order.asc("cd_serie");
					ordInv = Order.asc("cd_inven");
				}
				if (ord.startsWith("C")){
					codSez = Order.asc("cd_sez");;
					ordLoc = Order.asc("ord_loc");;
					orSpec = Order.asc("ord_spec");;
					codLoc = Order.asc("cd_loc");;
					codSpec = Order.asc("spec_loc");;
					keyColl = Order.asc("key_loc");;
				}
			}
			Order [] inv = {ordSerie, ordInv};
			Order [] dataIngresso = {dataIngr, ordSerie, ordInv};
			Order [] collocazione = {codSez, ordLoc, orSpec, codLoc, codSpec, keyColl};

			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			cr.add(Restrictions.eq("key_loc", keyLoc));
			cr.add(Restrictions.ne("fl_canc", 'S'));

			if (ord != null){
				if (ord.startsWith("I")){
					cr.addOrder(inv[0]);
					cr.addOrder(inv[1]);
				}else if (ord.startsWith("D")){
					cr.addOrder(dataIngresso[0]);
					cr.addOrder(dataIngresso[1]);
					cr.addOrder(dataIngresso[2]);
				}else if (ord.startsWith("C")){
					Criteria crColl = cr.createCriteria("key_loc");
					crColl.addOrder(collocazione[0]);
					crColl.addOrder(collocazione[1]);
					crColl.addOrder(collocazione[2]);
					crColl.addOrder(collocazione[3]);
					crColl.addOrder(collocazione[4]);
				}
			}else{
				cr.addOrder(inv[0]);
				cr.addOrder(inv[1]);
			}
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public ScrollableResults getListaInventariScrollable(Tbc_collocazione keyLoc)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			boolean [] ret = {false, false, false, false, false};
			Order ordSerie = null;
			Order ordInv = null;
			Order seqColl = null;

			Order [] inv = {ordSerie, ordInv};
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			cr.add(Restrictions.eq("key_loc", keyLoc));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			ScrollableResults inventari = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_INSENSITIVE);
			return inventari;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}


	public ScrollableResults getListaInventariScrollable(String codPolo, String codBib, String codSez,
			String loc, String aLoc, boolean esattoColl, String spec, String aSpec, boolean esattoSpec, int keyLoc, String codSerie, int codInv)
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

			Criteria crInv = session.createCriteria(Tbc_inventario.class);
			crInv.add(Restrictions.ne("fl_canc", 'S'));
			crInv.addOrder(Order.asc("cd_serie"));
			crInv.addOrder(Order.asc("cd_inven"));
			Criteria crColl = crInv.createCriteria("key_loc");
			crColl.add(Restrictions.ne("fl_canc", 'S'));
			crColl.add(Restrictions.eq("cd_sez", sezione));
			crColl.addOrder(Order.asc("ord_loc"));
			crColl.addOrder(Order.asc("ord_spec"));
			crColl.addOrder(Order.asc("cd_loc"));
			crColl.addOrder(Order.asc("spec_loc"));
			crColl.addOrder(Order.asc("key_loc"));
			if (keyLoc == 0) {	// primo giro
				crColl.add(intervalloColloc1(loc,  spec, aLoc, aSpec));
			}else{
				crColl.add(intervalloColloc1(loc,  spec, aLoc, aSpec));
				crInv.add(intervalloColloc2(codSerie, codInv));
			}
			ScrollableResults listaColl = crInv.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_INSENSITIVE);

			return listaColl;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public ScrollableResults getListaInventariScrollableAggDisp(String codPolo, String codBib, String codSez,
			String loc, String aLoc, boolean esattoColl, String spec, String aSpec, boolean esattoSpec, int keyLoc, String codSerie, int codInv,
			Timestamp filtroDataDa, Timestamp filtroDataA,
			String filtroCodTipoFruizione, String filtroCodRip, String filtroCodNoDisp, String filtroStatoConservazione)
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

			Criteria crInv = session.createCriteria(Tbc_inventario.class);
			if (filtroCodTipoFruizione != null && !filtroCodTipoFruizione.trim().equals("")) {
				crInv.add(Restrictions.eq("cd_frui", filtroCodTipoFruizione));
			}
			if (filtroCodRip != null && !filtroCodRip.trim().equals("")) {
				crInv.add(Restrictions.eq("cd_riproducibilita", filtroCodRip));
			}
			if (filtroCodNoDisp != null && !filtroCodNoDisp.trim().equals("")) {
				crInv.add(Restrictions.eq("cd_no_disp", filtroCodNoDisp));
			}
			if (filtroStatoConservazione != null && !filtroStatoConservazione.trim().equals("")) {
				crInv.add(Restrictions.eq("stato_con", filtroStatoConservazione));
			}
			if ((filtroDataDa != null && !filtroDataDa.equals("")) &&
					(filtroDataA != null && !filtroDataA.equals(""))){
				crInv.add(Restrictions.between("data_ingresso", filtroDataDa, filtroDataA));
			}
			crInv.add(Restrictions.ne("cd_sit", '3'));
			crInv.add(Restrictions.ne("fl_canc", 'S'));
			crInv.addOrder(Order.asc("cd_serie"));
			crInv.addOrder(Order.asc("cd_inven"));
			Criteria crColl = crInv.createCriteria("key_loc");
			crColl.add(Restrictions.ne("fl_canc", 'S'));
			crColl.add(Restrictions.eq("cd_sez", sezione));
			crColl.addOrder(Order.asc("ord_loc"));
			crColl.addOrder(Order.asc("ord_spec"));
			crColl.addOrder(Order.asc("cd_loc"));
			crColl.addOrder(Order.asc("spec_loc"));
			crColl.addOrder(Order.asc("key_loc"));
			if (keyLoc == 0) {	// primo giro
				crColl.add(intervalloColloc1(loc,  spec, aLoc, aSpec));
			}else{
				crColl.add(intervalloColloc1(loc,  spec, aLoc, aSpec));
				crInv.add(intervalloColloc2(codSerie, codInv));
			}
			ScrollableResults listaColl = crInv.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_INSENSITIVE);

			return listaColl;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventari(String codPolo, String codBib,
			int annoFattura, int progrFattura)
			throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_bib_fatt", codBib));
			cr.add(Restrictions.eq("anno_fattura", annoFattura));
			cr.add(Restrictions.eq("prg_fattura", progrFattura));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public List<Tbc_inventario> getListaInventari(EsameCollocRicercaVO paramRicerca)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(paramRicerca.getCodPolo());
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(paramRicerca.getCodBib());
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(paramRicerca.getCodSerie());
			//			Tb_titolo tit = new Tb_titolo();
			//			tit.setBid(bid);

			//			cr.add(Restrictions.eq("b", tit));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.eq("cd_sit", '1'));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean inserimentoInventario(Tbc_inventario inventario)
	throws DaoManagerException	{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.save(inventario);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tbc_inventario getInventario(String codPolo, String codBib, String codSerie, int codInvent)
	throws DaoManagerException {

		Tbc_inventario rec = null;
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			//
			rec = new Tbc_inventario();
			rec.setCd_serie(serie);
			rec.setCd_inven(codInvent);
			rec = (Tbc_inventario) loadNoLazy(session, Tbc_inventario.class, rec);
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public Tbc_inventario getInventarioDaScaricare(String codPolo, String codBib, String codSerie, int codInvent)
	throws DaoManagerException {

		Tbc_inventario rec = null;
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			//
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.eq("cd_inven", codInvent));
			cr.add(Restrictions.ne("cd_sit", '3'));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			rec = (Tbc_inventario) cr.uniqueResult();
			//			rec = (Tbc_inventario) loadNoProxy(session, Tbc_inventario.class, rec);
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public boolean modificaInventario(Tbc_inventario recInv)
	throws DaoManagerException {
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			Timestamp ts = DaoManager.now();
			recInv.setTs_var(ts);
			session.saveOrUpdate(recInv);
			session.flush();
			ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ret;
	}

	public boolean modificaInventario(int keyLoc, String uteVar)
	throws DaoManagerException {
		boolean ok = false;

		try {
			Timestamp ts = DaoManager.now();
			Session session = this.getCurrentSession();
			String hqlUpdate = "update Tbc_inventario set key_loc = :keyLoc, ts_var = :ts, ute_var = :uteVar where key_loc = :key_loc";
			int updated = session.createQuery(hqlUpdate).setTimestamp("ts", ts)
					.setString("uteVar", uteVar)
					.setInteger("keyLoc", new Integer(keyLoc))
					.setInteger("key_loc", new Integer(keyLoc))
					.executeUpdate();
			ok = (updated > 0);

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

		return ok;
	}

	public int countInventariPerPosseduto(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Number countInv = 0;

		try{
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Criteria c = session.createCriteria(Tbc_inventario.class);
			c.setProjection(Projections.rowCount())
					.add(Restrictions.eq("cd_serie.cd_polo", bib))
					.add(Restrictions.eq("b.id", bid))
					//almaviva5_20140718 segnalazione NAP: posseduto: non vanno contati gli inventari dismessi.
					.add(Restrictions.in("cd_sit", new Character[] { '1', '2' }))
					.add(Restrictions.ne("fl_canc", 'S'));

			Number cnt = (Number) c.uniqueResult();
			countInv = cnt.intValue();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countInv.intValue();
	}

	//query per conteggio inventari ripartiti per diritto di stampa e altro tipo acquisizione
	// Maggio 2013 - modifiche per filtrare la stampa titolixEditore anche per TipoMaterialeInventariabile
	public List<Object []> countInventariPerPossedutoPerDirittoDiStampaOAltroTipoAcq(String codPolo, String codBib, String bid,
			String tipoAcq, String codMateriale, Timestamp dataIngressoDa, Timestamp dataIngressoA)
	throws DaoManagerException {

		List<Object[]> results= null;
		Integer countInv = 0;

		try{
			Map params = new HashMap();
			Session session = this.getCurrentSession();
			StringBuffer query = new StringBuffer ();
			query.append("select  ");

//			Maggio 2013 - Stampa titoli x Editore: modifiche per correggere la select sul canmpo diritto di stampa presente o no
//			(tale valore e' presente nel campo cod.cd_flg2 che ora viene correttamente estratto e paragonato al valore 'S'
//			query.append("CASE  WHEN (select cod.cd_tabella from tb_codici cod  ");
//			query.append("where cod.tp_tabella='CTAC' and cod.cd_tabella=inv.tipo_acquisizione ) = 'P'  ");
			query.append("CASE  WHEN (select cod.cd_flg2 from tb_codici cod  ");
			query.append("where cod.tp_tabella='CTAC' and cod.cd_tabella=inv.tipo_acquisizione ) = 'S'  ");

			query.append("THEN 'diritto di stampa' else 'altro tipo acquisizione'   ");
			query.append("END,   ");
			query.append("count(*)  ");
			query.append("from tbc_inventario inv  ");
			query.append("where inv.fl_canc<>'S'  ");
			query.append("and  inv.cd_bib=:codBib ");
			if (bid!=null && bid.trim().length()!=0)	{
				query.append("and inv.bid =:bid ");
				params.put("bid", bid);
			}
			params.put("codBib", codBib);
//			query.append("and bid = 'SBW0000654'   ");
			/*and inv.data_ingresso between to_date('20120101','YYYYMMDD') and to_date('20130201','YYYYMMDD') */
			/*and inv.tipo_acquisizione = 'A'*/
//			if (tipoAcq!=null && tipoAcq.trim().length()!=0)	{//tipoAcq
//				query.append("and ce.tp_acquisizione =:tipoAcq ");
//				params.put("tipoAcq", tipoAcq);
//			}
			if (dataIngressoDa!=null && dataIngressoA != null)	{//dataIngressoDa a
				query.append(" and inv.data_ingresso BETWEEN  :dataIngressoDa and :dataIngressoA ");
				params.put("dataIngressoDa", dataIngressoDa);
				params.put("dataIngressoA", dataIngressoA);
			}
			if (tipoAcq!=null && tipoAcq.trim().length()!=0)	{//tipoAcq
				query.append("and inv.tipo_acquisizione =:tipoAcq ");
				params.put("tipoAcq", tipoAcq);
			}

			// Maggio 2013 - modifiche per filtrare la stampa titolixEditore anche per TipoMaterialeInventariabile
			if (codMateriale!=null && codMateriale.trim().length()!=0)	{
				query.append("and inv.cd_mat_inv =:codMateriale ");
				params.put("codMateriale", codMateriale);
			}


			query.append("group by 1   ");
			query.append("order by 1;  ");
			SQLQuery queryI = session.createSQLQuery(query.toString());
			queryI.setProperties(params);
//			queryI.addEntity(V_catalogo_editoria.class);
			results = queryI.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}


	//segnalazione ICCU: erogazione servizi: errore nella ricerca per titolo
	//se per la biblioteca erogante ci sono solo inventari dismessi.
	public Integer countInventariPerCollocato(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Integer countInv = 0;

		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			countInv = (Integer)session.createCriteria(Tbc_inventario.class).setProjection(Projections.rowCount())
			.add(Restrictions.eq("cd_serie.cd_polo", bib))
			.add(Restrictions.eq("b", titolo))
			.add(Restrictions.eq("cd_sit", '2'))
			.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countInv;
	}

	public int countInventariPerPossedutoAltreBib(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Number countInv = null;

		try{
			Session session = this.getCurrentSession();

			countInv = (Number)session.createCriteria(Tbc_inventario.class).setProjection(Projections.rowCount())
			.add(Restrictions.ne("cd_serie.cd_polo.cd_biblioteca", codBib))
			.add(Restrictions.eq("b.id", bid))
			.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countInv.intValue();
	}

	public Tb_titolo getTitoloDF(String bidVid)
	throws DaoManagerException {

		Tb_titolo rec = null;
		try{
			Session session = this.getCurrentSession();

			rec = new Tb_titolo();
			//rec.setBid(bidVid);
			rec = (Tb_titolo) loadNoLazy(session, Tb_titolo.class, bidVid);
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}
	public Tb_titolo getTitoloDF1(String bid)
	throws DaoManagerException {

		Tb_titolo rec = null;
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tb_titolo.class);

			cr.add(Restrictions.eq("bid", bid));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			rec = (Tb_titolo) cr.uniqueResult();
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}
	public CodiceVO getTitTitDF(String bid)
	throws DaoManagerException {
		CodiceVO trTitTit = null;
		try {
			Session session = this.getCurrentSession();

			//almaviva5_20150115
			Query q = session.getNamedQuery("titTitDF");
			q.setString("bid", bid);
			Object[] result = (Object[]) q.uniqueResult();
			if (result != null) {
				trTitTit = new CodiceVO();
				trTitTit.setCodice((String) result[0]);
				trTitTit.setDescrizione((String) result[1]);
			}

		} catch (ObjectNotFoundException e) {
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return trTitTit;
	}
	public Tb_autore getAutoreDF(String bidVid)
	throws DaoManagerException {

		Tb_autore rec = null;
		try{
			Session session = this.getCurrentSession();

			rec = new Tb_autore();
			//			rec.setVid(bidVid);
			rec = (Tb_autore) loadNoLazy(session, Tb_autore.class, bidVid);
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public CodiceVO getAutoreTitAut(String bid)
	throws DaoManagerException {

		CodiceVO trTitAut = null;
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tb_autore.class);
			cr.add(Restrictions.ne("fl_canc", 'S'));
			Criteria cr2 = cr.createCriteria("Tr_tit_aut", "ta");
			cr2.add(Restrictions.ne("ta.fl_canc", 'S'));
			cr2.add(Restrictions.ne("ta.tp_responsabilita", '4'));
			cr2.add(Restrictions.eq("ta.B.id", bid));
			cr.addOrder(Order.asc("ta.tp_responsabilita"));
			cr.addOrder(Order.asc("ta.cd_relazione"));
			cr.setMaxResults(1);
			//Tr_tit_aut results = (Tr_tit_aut) cr.uniqueResult();
			Tb_autore results = (Tb_autore) cr.uniqueResult();
			if (results != null) {
				trTitAut = new CodiceVO();
				trTitAut.setCodice(results.getVid());
				trTitAut.setDescrizione(results.getDs_nome_aut());
				session.evict(results);
			}
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return trTitAut;
	}

	public int getMinInventario(String codPolo, String codBib, String codSerie, String dataDa)
			throws DaoManagerException {
		try {
			//data ingresso minima
			Timestamp minData = new Timestamp(DateUtil.toDate(dataDa).getTime());

			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			Criteria c = session.createCriteria(Tbc_inventario.class);
			c.setProjection(Projections.min("cd_inven"))
				.add(Restrictions.eq("cd_serie", serie))
				.add(Restrictions.ge("data_ingresso", minData))
				.add(Restrictions.ne("fl_canc", "S"));
			Number min = (Number) c.uniqueResult();

			return min.intValue();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public int getMaxInventario(String codPolo, String codBib, String codSerie, String dataA)
			throws DaoManagerException {
		try {
			// data max ingresso, si imposta al giorno successivo
			Date maxData = DateUtil.addDay(DateUtil.toDate(dataA), 1);

			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			Criteria c = session.createCriteria(Tbc_inventario.class);
			c.setProjection(Projections.max("cd_inven"))
				.add(Restrictions.eq("cd_serie", serie))
				.add(Restrictions.lt("data_ingresso", new Timestamp(maxData.getTime())))
				.add(Restrictions.ne("fl_canc", "S"));

			Number max = (Number) c.uniqueResult();
			return max.intValue();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariDaA(String codPolo, String codBib, String codSerie, String daNum,
			String aNum)
			throws DaoManagerException	{
		int invDa = Integer.parseInt(daNum);
		int invA = Integer.parseInt(aNum);
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.between("cd_inven", invDa, invA));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}


	public List<Tbc_inventario> getListaInventariDaAStatoConservazione(String codPolo, String codBib, String codSerie, String daNum,
			String aNum, String ord, String statCons, String tipoMat)
			throws DaoManagerException	{
		int invDa = Integer.parseInt(daNum);
		int invA = Integer.parseInt(aNum);
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			Criteria crInv = session.createCriteria(Tbc_inventario.class);
			if (ord != null) {
				crInv.add(Restrictions.eq("cd_serie", serie));
				crInv.add(Restrictions.between("cd_inven", invDa, invA));
				crInv.add(Restrictions.ne("fl_canc", 'S'));
				crInv.add(Restrictions.eq("cd_sit", '2'));
				crInv.add(Restrictions.eq("cd_serie.cd_polo", bib));

				//almaviva5_20150115
				if (ValidazioneDati.isFilled(statCons))
					crInv.add(Restrictions.eq("stato_con", statCons));
				if (ValidazioneDati.isFilled(tipoMat))
					crInv.add(Restrictions.eq("cd_mat_inv", tipoMat));
				//

				if (ord.equals("I")){
					crInv.addOrder(Order.asc("cd_serie"));
					crInv.addOrder(Order.asc("cd_inven"));
				}else if (ord.equals("D")){
					crInv.addOrder(Order.asc("data_ingresso"));
					crInv.addOrder(Order.asc("cd_serie"));
					crInv.addOrder(Order.asc("cd_inven"));
				}else if (ord.equals("C")){
					Criteria crColl = crInv.createCriteria("key_loc");
					crColl.add(Restrictions.ne("fl_canc", 'S'));
					crColl.addOrder(Order.asc("cd_sez"));
					crColl.addOrder(Order.asc("ord_loc"));
					crColl.addOrder(Order.asc("ord_spec"));
					crColl.addOrder(Order.asc("cd_loc"));
					crColl.addOrder(Order.asc("spec_loc"));
					crColl.addOrder(Order.asc("key_loc"));
					crInv.addOrder(Order.asc("cd_serie"));
					crInv.addOrder(Order.asc("cd_inven"));
				}
				//			ScrollableResults listaColl = crInv.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_INSENSITIVE);
				List<Tbc_inventario> results = crInv.list();
				return results;
			}
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return null;
	}

	public List<Tbc_inventario> getListaInventariDaAStatoConservazioneXls(String codPolo, String codBib, String codSerie, String daNum,
			String aNum, String ord)
			throws DaoManagerException	{
		int invDa = Integer.parseInt(daNum);
		int invA = Integer.parseInt(aNum);
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			Criteria crInv = session.createCriteria(Tbc_inventario.class);
			if (ord != null){
				crInv.add(Restrictions.eq("cd_serie", serie));
				crInv.add(Restrictions.between("cd_inven", invDa, invA));
				crInv.add(Restrictions.ne("fl_canc", 'S'));
				crInv.add(Restrictions.eq("cd_serie.cd_polo", bib));
				if (ord.equals("I")){
					crInv.addOrder(Order.asc("cd_serie"));
					crInv.addOrder(Order.asc("cd_inven"));
				}else if (ord.equals("D")){
					crInv.addOrder(Order.asc("data_ingresso"));
					crInv.addOrder(Order.asc("cd_serie"));
					crInv.addOrder(Order.asc("cd_inven"));
				}else if (ord.equals("C")){
					Criteria crColl = crInv.createCriteria("key_loc");
					crColl.add(Restrictions.ne("fl_canc", 'S'));
					crColl.addOrder(Order.asc("cd_sez"));
					crColl.addOrder(Order.asc("ord_loc"));
					crColl.addOrder(Order.asc("ord_spec"));
					crColl.addOrder(Order.asc("cd_loc"));
					crColl.addOrder(Order.asc("spec_loc"));
					crColl.addOrder(Order.asc("key_loc"));
					crInv.addOrder(Order.asc("seq_coll"));
					crInv.addOrder(Order.asc("cd_serie"));
					crInv.addOrder(Order.asc("cd_inven"));
				}
				//			ScrollableResults listaColl = crInv.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_INSENSITIVE);
				List<Tbc_inventario> results = crInv.list();
				return results;
			}
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return null;
	}


	public ScrollableResults getListaInventariDaARegIng(String codPolo, String codBib, String codSerie, String daNum,
			String aNum)
	throws DaoManagerException	{
		ScrollableResults listaInv = null;
		int invDa = Integer.parseInt(daNum);
		int invA = Integer.parseInt(aNum);
		try{
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			listaInv = session.createCriteria(Tbc_inventario.class)
			.add(Restrictions.eq("cd_serie", serie))
			.add(Restrictions.between("cd_inven", invDa, invA))
			.add(Restrictions.ne("fl_canc", 'S'))
			.add(Restrictions.eq("cd_serie.cd_polo", bib))
			.addOrder(Order.asc("cd_serie"))
			.addOrder(Order.asc("cd_inven"))
			.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return listaInv;
	}

	public ScrollableResults getListaInventariScrollableDaASpostColl(String codPolo, String codBib, String codSerie, String daNum,
			String aNum) throws DaoManagerException	{
		int invDa = Integer.parseInt(daNum);
		int invA = Integer.parseInt(aNum);
		try{
			Session session = this.getCurrentSession();


			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.isNotNull("key_loc"));
			cr.add(Restrictions.between("cd_inven", invDa, invA));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));

			ScrollableResults inventari = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_SENSITIVE);

			return inventari;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public ScrollableResults getListaInventariScrollableDaA(String codPolo, String codBib, String codSerie, String daNum,
			String aNum) throws DaoManagerException	{
		int invDa = Integer.parseInt(daNum);
		int invA = Integer.parseInt(aNum);
		try{
			Session session = this.getCurrentSession();


			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.between("cd_inven", invDa, invA));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));

			ScrollableResults inventari = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_SENSITIVE);

			return inventari;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public ScrollableResults getListaInventariScrollableDaAAggDisp(String codPolo, String codBib, String codSerie, String daNum,
			String aNum, Timestamp filtroDataDa, Timestamp filtroDataA,
			String filtroCodTipoFruizione, String filtroCodRip, String filtroCodNoDisp, String filtroStatoConservazione)
	throws DaoManagerException	{
		int invDa = Integer.parseInt(daNum);
		int invA = 0;
		if (aNum != null){
			invA = Integer.parseInt(aNum);
		}
		try{
			Session session = this.getCurrentSession();


			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			if (filtroCodTipoFruizione != null && !filtroCodTipoFruizione.trim().equals("")) {
				cr.add(Restrictions.eq("cd_frui", filtroCodTipoFruizione));
			}
			if (filtroCodRip != null && !filtroCodRip.trim().equals("")) {
				cr.add(Restrictions.eq("cd_riproducibilita", filtroCodRip));
			}
			if (filtroCodNoDisp != null && !filtroCodNoDisp.trim().equals("")) {
				cr.add(Restrictions.eq("cd_no_disp", filtroCodNoDisp));
			}
			if (filtroStatoConservazione != null && !filtroStatoConservazione.trim().equals("")) {
				cr.add(Restrictions.eq("stato_con", filtroStatoConservazione));
			}
			if ((filtroDataDa != null && !filtroDataDa.equals("")) &&
					(filtroDataA != null && !filtroDataA.equals(""))){
				cr.add(Restrictions.between("data_ingresso", filtroDataDa, filtroDataA));
			}
			cr.add(Restrictions.eq("cd_serie", serie));
			if (aNum != null){
				cr.add(Restrictions.between("cd_inven", invDa, invA));
			}else{
				cr.add(Restrictions.eq("cd_inven", invDa));
			}
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));

			ScrollableResults inventari = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_SENSITIVE);

			return inventari;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public ScrollableResults getListaInventariScrollableDaADaScaricare(String codPolo, String codBib, String codSerie, String daNum,
			String aNum) throws DaoManagerException	{
		int invDa = Integer.parseInt(daNum);
		int invA = Integer.parseInt(aNum);
		try{
			Session session = this.getCurrentSession();


			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.between("cd_inven", invDa, invA));
			cr.add(Restrictions.ne("cd_sit", '3'));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));

			ScrollableResults inventari = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_SENSITIVE);

			return inventari;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public ScrollableResults getListaInventariScrollableDataDaA(String codPolo, String codBib,
			String codSerie, int codInv, Timestamp dataDa, Timestamp dataA)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.between("data_ingresso", dataDa, dataA));
			if (codSerie != null){
				cr.add(Restrictions.eq("cd_serie.cd_serie", codSerie));
				cr.add(Restrictions.eq("cd_inven", codInv));
			}
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			ScrollableResults inventari = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.READ).scroll(ScrollMode.SCROLL_SENSITIVE);
			return inventari;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public ScrollableResults getListaInventariScrollableDataDaAAggDisp(String codPolo, String codBib,
			String codSerie, int codInv, Timestamp dataDa, Timestamp dataA,
			String filtroCodTipoFruizione, String filtroCodRip, String filtroCodNoDisp, String filtroStatoConservazione)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			if (filtroCodTipoFruizione != null && !filtroCodTipoFruizione.trim().equals("")) {
				cr.add(Restrictions.eq("cd_frui", filtroCodTipoFruizione));
			}
			if (filtroCodRip != null && !filtroCodRip.trim().equals("")) {
				cr.add(Restrictions.eq("cd_riproducibilita", filtroCodRip));
			}
			if (filtroCodNoDisp != null && !filtroCodNoDisp.trim().equals("")) {
				cr.add(Restrictions.eq("cd_no_disp", filtroCodNoDisp));
			}
			if (filtroStatoConservazione != null && !filtroStatoConservazione.trim().equals("")) {
				cr.add(Restrictions.eq("stato_con", filtroStatoConservazione));
			}
			addTimeStampRange(cr, "data_ingresso", dataDa, dataA);
			if (codSerie != null){
				//				cr.add(Restrictions.eq("cd_serie.cd_serie", codSerie));
				//				cr.add(Restrictions.eq("cd_inven", codInv));

				cr.add(intervalloColloc2(codSerie, codInv));
			}
			cr.add(Restrictions.ne("cd_sit", '3'));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			ScrollableResults inventari = cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.UPGRADE).scroll(ScrollMode.SCROLL_SENSITIVE);
			return inventari;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List getAltreBibInv(String bid)
	throws DaoManagerException	{
		List lista = null;
		try{
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("contaBibliotecheBidInv");
			query.setString("bid", bid);
			lista = query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getAltreBibInv(String bid, String codBib)
	throws DaoManagerException	{
		List lista = null;
		try{
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("contaBibliotecheBidBibInv");
			query.setString("bid", bid);
			query.setString("codBib", codBib);
			lista = query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List getListaCodAcqCodMat(String codPolo, String codBib, String codSerie, String invDa, String invA, String codTipoOrd)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbc_inventario.class);
		try {
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.setProjection(Projections.distinct(Projections.projectionList()
					.add(Projections.property("tipo_acquisizione"))
					.add(Projections.property("cd_mat_inv"))));
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.between("cd_inven", Integer.parseInt(invDa), Integer.parseInt(invA)));
			if (codTipoOrd != null && !codTipoOrd.trim().equals("")){
				cr.add(Restrictions.eq("tipo_acquisizione", codTipoOrd));
			}
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.addOrder(Order.asc("tipo_acquisizione"));
			cr.addOrder(Order.asc("cd_mat_inv"));

			return cr.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List getListaCodAcqCodMatGroupBy(String codPolo, String codBib, String codSerie, String invDa, String invA,
			String dataDa, String dataA , String codTipoOrd)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbc_inventario.class);
		try {
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.setProjection(Projections.distinct(Projections.projectionList()
					.add(Projections.property("tipo_acquisizione"))
					.add(Projections.property("cd_mat_inv"))
					.add(Projections.count("cd_mat_inv"))
					.add(Projections.sum("valore"))
					.add(Projections.groupProperty("tipo_acquisizione"))
					.add(Projections.groupProperty("cd_mat_inv"))));
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.between("cd_inven", Integer.parseInt(invDa), Integer.parseInt(invA)));
			cr.add(Restrictions.between("data_ingresso", DateUtil.toTimestamp(dataDa), DateUtil.toTimestamp(dataA)));
			if (codTipoOrd != null && !codTipoOrd.trim().equals("")){
				cr.add(Restrictions.eq("tipo_acquisizione", codTipoOrd.charAt(0)));
			}
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.addOrder(Order.asc("tipo_acquisizione"));
			return cr.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbc_inventario> getListaInventariNuoviEsemplari(String codPolo, String codBib,
			Timestamp dataDa, Timestamp dataA, String ordinaPer)
			throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);


			//per tutti
			cr.add(Restrictions.between("ts_ins_prima_coll", dataDa, dataA));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			//

			//C data prima collocazione+serie.inv, T titolo, A autore
			if (ordinaPer.endsWith("A")){//ascending
				if (ordinaPer.startsWith("C")){
					cr.addOrder(Order.asc("ts_ins_prima_coll"));
				}else if (ordinaPer.startsWith("T")){
					//ORDER BY this_.KY_CLES1_T,this_.KY_CLES2_T,NVL(this_.AA_PUBB_1, ' '),this_.BID
					cr.createCriteria("key_loc")
					.createCriteria("b")
					.addOrder(Order.asc("ky_cles1_t"))
					.addOrder(Order.asc("ky_cles2_t"))
					.addOrder(Order.asc("aa_pubb_1"))
					.addOrder(Order.asc("bid"));
				}else if (ordinaPer.startsWith("A")){
				}
				cr.addOrder(Order.asc("cd_serie"));
				cr.addOrder(Order.asc("cd_inven"));
			}else if (ordinaPer.endsWith("D")){//descending
				if (ordinaPer.startsWith("C")){
					cr.addOrder(Order.desc("ts_ins_prima_coll"));
				}else if (ordinaPer.startsWith("T")){
					cr.createCriteria("key_loc")
					.createCriteria("b")
					.addOrder(Order.desc("ky_cles1_t"))
					.addOrder(Order.desc("ky_cles2_t"))
					.addOrder(Order.desc("aa_pubb_1"))
					.addOrder(Order.desc("bid"));
				}else if (ordinaPer.startsWith("A")){
				}
				cr.addOrder(Order.desc("cd_serie"));
				cr.addOrder(Order.desc("cd_inven"));
			}
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<InventarioTitoloBollettinoVO> getListaInventariNuoviTitoli(String codPolo, String codBib,
			Timestamp dataDa, Timestamp dataA, String ordinaPer)
			throws DaoManagerException	{
		try{

			Session session = this.getCurrentSession();
			Query query = null;
			if (ordinaPer.endsWith("A")){
				if (ordinaPer.startsWith("C")){
					query = session.getNamedQuery("listaInventariNuoviTitoliBollettinoAsc");
				}else if (ordinaPer.startsWith("T")){
					query = session.getNamedQuery("listaInventariNuoviTitoliBollettinoTitoloAsc");
				}else if (ordinaPer.startsWith("A")){
				}
			}else if (ordinaPer.endsWith("D")){//descending
				if (ordinaPer.startsWith("C")){
					query = session.getNamedQuery("listaInventariNuoviTitoliBollettinoDesc");
				}else if (ordinaPer.startsWith("T")){
					query = session.getNamedQuery("listaInventariNuoviTitoliBollettinoTitoloDesc");
				}else if (ordinaPer.startsWith("A")){
				}
			}
			query.setResultTransformer(Transformers.aliasToBean(InventarioTitoloBollettinoVO.class));

			query.setString("codPolo", codPolo);
			query.setString("codBib", codBib);
			query.setTimestamp("dataDa", dataDa);
			query.setTimestamp("dataA", dataA);
			List results = query.list();

			return results;

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariBuoniCaricoAnnoFatturaNumBuono0(String codPolo, String codBib, int annoF, int progrFattura)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.add(Restrictions.eq("cd_bib_fatt", codBib));
			cr.add(Restrictions.eq("anno_fattura", annoF));
			cr.add(Restrictions.eq("prg_fattura", progrFattura));
			cr.add(Restrictions.eq("num_carico", 0));
			cr.add(Restrictions.ne("cd_sit", '3'));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			//			cr.addOrder(Order.asc("anno_fattura"));
			//			cr.addOrder(Order.asc("prg_fattura"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariBuoniCaricoAnnoFatturaRistampa(String codPolo, String codBib, int annoF, int progrFattura)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.add(Restrictions.eq("cd_bib_fatt", codBib));
			cr.add(Restrictions.eq("anno_fattura", annoF));
			cr.add(Restrictions.eq("prg_fattura", progrFattura));
			cr.add(Restrictions.gt("num_carico", 0));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariBuoniCaricoCodSerieInvDaInvANumCarico0(String codPolo, String codBib, String codSerie, String daInv, String aInv)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.between("cd_inven", Integer.valueOf(daInv), Integer.valueOf(aInv)));
			cr.add(Restrictions.ne("cd_sit", '3'));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("num_carico", 0));
			cr.addOrder(Order.asc("anno_fattura"));
			cr.addOrder(Order.asc("prg_fattura"));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariBuoniCaricoCodSerieNumeroBuonoRistampa(String codPolo, String codBib, String codSerie, String numBuono,
			Date dataCarico)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			if (Integer.valueOf(numBuono) > 0){//è stato indicato il numBuono in input
				cr.add(Restrictions.eq("num_carico", Integer.valueOf(numBuono)));
			}else{
				cr.add(Restrictions.ne("num_carico", Integer.valueOf(0)));//tutti i numBuoni della serie
			}

			// almaviva5_20131118 #5100
			if (dataCarico != null)
				cr.add(Restrictions.eq("data_carico", dataCarico));

			cr.addOrder(Order.asc("num_carico"));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			cr.addOrder(Order.asc("anno_fattura"));
			cr.addOrder(Order.asc("prg_fattura"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariBuoniCaricoCodSerieInvDaInvARistampa(String codPolo, String codBib, String codSerie, String daInv, String aInv)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.between("cd_inven", Integer.valueOf(daInv), Integer.valueOf(aInv)));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.gt("num_carico", 0));
			cr.addOrder(Order.asc("anno_fattura"));
			cr.addOrder(Order.asc("prg_fattura"));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariBuoniCaricoCodSerieNumCarico(String codPolo, String codBib, String codSerie, String numCarico)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.eq("cd_bib_fatt", codBib));
			cr.add(Restrictions.eq("num_carico", numCarico));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariDa(String codPolo, String codBib, String codUtente, String dataDa, String dataA)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);


			cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
			cr.add(Restrictions.eq("cd_sit", '2'));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("ute_var", bib.getCd_polo().getCd_polo()+bib.getCd_biblioteca()+codUtente));
			cr.add(Restrictions.between("ts_var", DateUtil.toTimestamp(dataDa.trim()), DateUtil.toTimestampA(dataA.trim())));
			cr.addOrder(Order.asc("ts_var"));
			cr.addOrder(Order.asc("cd_serie"));
			cr.addOrder(Order.asc("cd_inven"));
			List<Tbc_inventario> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List estraiNumCarico(String codPolo, String codBib, String codSerie)
	throws DaoManagerException	{
		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbc_inventario.class);
		try {
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			cr.setProjection(Projections.distinct(Projections.projectionList()
					.add(Projections.property("num_carico"))));
			cr.add(Restrictions.eq("cd_serie", serie));
			cr.add(Restrictions.gt("num_carico", 0));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.ne("cd_sit", '3'));
			cr.addOrder(Order.asc("num_carico"));

			return cr.list();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tbc_inventario> getListaInventariXls(String codPolo, String codBib,
			Timestamp dataDa, Timestamp dataA, String codSerie, String daInv, String aInv, List listaInv,
			String tipoMat, String statoCon, String digit, String noDispo, String ord, String tipoOperazione, String tipoLista,
			TipoData tipo, boolean escludiDigit, String tipoDigit) throws DaoManagerException	{
		try {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("codPolo", codPolo);
			params.put("codBib", codBib);
			//
			List<Tbc_inventario> results = null;
			Session session = this.getCurrentSession();
			StringBuilder query = new StringBuilder(1024);
			query.append("select inv.* from tbc_inventario inv ");

			//almaviva5_20131003 evolutive google2
			if (ValidazioneDati.equals(digit, "N"))
				//non digitalizzati
				aggiungiFiltroInventariDigitalizzati(query, escludiDigit, tipoDigit);

			//tipoOperazione D
			//tipoLista="nuoviEsemplari" trattamento come inventari ma per data
			//tipoLista="nuoviTitoli" trattamento ad hoc
			if (ValidazioneDati.equals(tipoLista, "nuoviTitoli")) {
				query.append("left outer join  tbc_inventario inv2 on inv.cd_bib = inv2.cd_bib ");
				query.append(" and not inv.cd_inven = inv2.cd_inven ");
				query.append(" and inv.bid = inv2.bid ");
				//almaviva5_20130709
				if (tipo == TipoData.DATA_INGRESSO)
					query.append("and inv2.data_ingresso < :dataDa ");
				else
					query.append("and inv2.ts_ins_prima_coll < :dataDa ");
				params.put("dataDa", dataDa);
			}
			if (tipoOperazione != null && (!tipoOperazione.equals("S")) ) {
				if (ValidazioneDati.equals(tipoOperazione, "N")) {//lista inventari
					try{
						int nInv = createTempTables(listaInv, codPolo, codBib);
						if (nInv > 0) {
							query.append(" inner join  tmp_inventari tmpInv on tmpInv.cd_polo=inv.cd_polo and tmpInv.cd_bib=inv.cd_bib ");
							query.append(" and tmpInv.cd_serie=inv.cd_serie and tmpInv.cd_inven=inv.cd_inven  ");
							query.append("left outer join  tbc_collocazione coll on coll.key_loc=inv.key_loc  ");
							query.append(" and coll.fl_canc<>'S' ");
						}
					}catch (Exception e) {
						throw new DaoManagerException(e);
					}
				} else {//range, data
					query.append("left outer join tbc_collocazione coll on coll.key_loc=inv.key_loc  ");
					query.append(" and coll.fl_canc <> 'S' ");
				}
			} else {//tipoOperazione = S
				query.append("inner join tbc_collocazione coll on coll.key_loc=inv.key_loc  ");
				query.append(" and coll.fl_canc <> 'S' ");
			}
			//where
			query.append("where inv.cd_polo=:codPolo ");
			query.append(" and inv.cd_bib=:codBib ");
			if (ValidazioneDati.equals(tipoOperazione, "R")) {//range di inventari
				query.append(" and inv.cd_serie=:codSerie ");
				query.append(" and inv.cd_inven BETWEEN :daInv and :aInv ");
				params.put("codSerie", codSerie);
				params.put("daInv", Integer.valueOf(daInv));
				params.put("aInv", Integer.valueOf(aInv));
			}
//			if (tipoOperazione!= null && tipoOperazione.equals("N")){//lista di inventari
//				query.append(" and cd_serie=:codSerie ");
//				query.append(" and cd_inven=:daInv ");
//				params.put("codSerie", codSerie);
//				params.put("daInv", Integer.valueOf(daInv));
//			}
			if (ValidazioneDati.in(tipoLista, "nuoviEsemplari", "nuoviTitoli")) {
				//almaviva5_20130709
				if (tipo == TipoData.DATA_INGRESSO)
					query.append(" and inv.data_ingresso BETWEEN :dataDa and :dataA ");
				else
					query.append(" and inv.ts_ins_prima_coll BETWEEN :dataDa and :dataA ");
				params.put("dataDa", dataDa);
				params.put("dataA", dataA);
			}
			//almaviva5_20130709
			if (tipo == TipoData.DATA_INGRESSO)
				query.append(" and inv.cd_sit in ('1', '2') ");
			else
				//prima coll
				query.append(" and inv.cd_sit='2' ");

			query.append(" and inv.fl_canc<>'S' ");
			params.put("codPolo", codPolo);
			params.put("codBib", codBib);
			if (ValidazioneDati.equals(tipoLista, "nuoviTitoli")) {
				query.append(" and inv2.cd_bib is null ");
			}
			if (ValidazioneDati.isFilled(tipoMat)) {
				query.append("and inv.cd_mat_inv =:tipoMat ");
				params.put("tipoMat", tipoMat);
			}
			if (ValidazioneDati.isFilled(statoCon)) {
				query.append("and inv.stato_con =:statoCon ");
				params.put("statoCon", statoCon);
			}
			if (ValidazioneDati.isFilled(digit)) {
				if (digit.equals("S")) {
					query.append("and (inv.digitalizzazione in ('0','1','2') ) ");
				} else if (digit.equals("N")) {
					query.append("and (inv.digitalizzazione isnull or inv.digitalizzazione='') ");// tutti i non digitalizzati
					//almaviva5_20131003 evolutive google2
					if (escludiDigit)
						//escludi tutti gli inventari che hanno un'altra copia digitalizzata
						query.append("and i2 isnull").append(BLANK);
					else
						//includi solo gli inventari che hanno un'altra copia digitalizzata
						query.append("and not i2 isnull").append(BLANK);
				}
			}
			if (ValidazioneDati.isFilled(noDispo)) {
				query.append("and inv.cd_no_disp =:noDispo ");
				params.put("noDispo", noDispo);
			}
			if (ord.equals("I")){
				query.append(" order by inv.cd_serie asc, inv.cd_inven asc");
			}else if (ord.equals("C")){
					query.append("order by coll.cd_sez NULLS FIRST,  ");
					query.append(" coll.ord_loc NULLS FIRST,  ");
					query.append(" coll.ord_spec NULLS FIRST, ");
					query.append(" coll.cd_loc asc, ");
					query.append(" coll.spec_loc asc, ");
					query.append(" coll.key_loc asc, ");
					query.append(" inv.seq_coll asc, ");
					query.append(" inv.cd_serie asc, ");
					query.append(" inv.cd_inven asc");
			}else{//"D"
				query.append("order by data_ingresso asc, inv.cd_bib asc, inv.cd_serie asc, inv.cd_inven asc  ");
			}
			SQLQuery queryI = session.createSQLQuery(query.toString());
			queryI.setProperties(params);
			queryI.addEntity(Tbc_inventario.class);
			results = queryI.list();
			return results;

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}


	private int  createTempTables(List<CodiceVO> listaInv, String codPolo, String codBib) throws Exception {

		log.debug("Creazione tabelle appoggio");
		Connection c = getConnection();

		try {
			StringBuilder query = new StringBuilder();
			query.append("CREATE TEMP TABLE tmp_inventari ");
			query.append(" (\"cd_polo\" CHAR(3) NOT NULL,  ");
			query.append(" \"cd_bib\" CHAR(3) NOT NULL,  ");
			query.append(" \"cd_serie\" CHAR(3) NOT NULL,  ");
			query.append(" \"cd_inven\" INTEGER NOT NULL  ) ");
			query.append(" ON COMMIT DROP ");
			Statement st = c.createStatement();
			st.execute(query.toString());

			int inserted = 0;
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO tmp_inventari").append(" VALUES (?,?,?,?)");

			PreparedStatement ps = c.prepareStatement(sql.toString());

			CodiceVO serieInv = null;
			for (int y = 0; y < listaInv.size(); y++) {
				serieInv = listaInv.get(y);
				ps.setString(1, codPolo);//biblioteca???
				if (serieInv.getTerzo() != null && !serieInv.getTerzo().equals("")){
					ps.setString(2, serieInv.getTerzo());
				}else{
					ps.setString(2, codBib);
				}
				ps.setString(3, serieInv.getCodice());
				ps.setInt(4, Integer.valueOf(serieInv.getDescrizione()));
				ps.addBatch();
			}

			int[] results = ps.executeBatch();
			for (int r = 0; r < results.length; r++)
				inserted += results[r];

			log.debug("Numero inventari inseriti: " + inserted);

			c.close();
			return inserted;

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		}


		public List<Tbc_inventario> getListaInventariTitoloEditoreT(String bid)
		throws DaoManagerException	{
			try{
				Session session = this.getCurrentSession();//HibernateUtil.getSession();
				Criteria cr = session.createCriteria(Tbc_inventario.class);

				Tb_titolo tit = new Tb_titolo();
				tit.setBid(bid);


				cr.add(Restrictions.eq("b", tit));
				cr.add(Restrictions.ne("cd_sit", '3'));
				cr.add(Restrictions.ne("fl_canc", 'S'));
				cr.addOrder(Order.asc("cd_serie"));
				cr.addOrder(Order.desc("cd_inven"));
				List<Tbc_inventario> results = cr.list();
				return results;
			}catch (HibernateException e){
				throw new DaoManagerException(e);
			}
		}
		public List<Tbc_inventario> getListaInventariTitoloEditoreNP(String codPolo, String codBib, String bid)
		throws DaoManagerException	{
			try{
				Session session = this.getCurrentSession();//HibernateUtil.getSession();
				Criteria cr = session.createCriteria(Tbc_inventario.class);

				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(codPolo);
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(codBib);
				bib.setCd_polo(polo);
				Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
				serie.setCd_polo(bib);
				Tb_titolo tit = new Tb_titolo();
				tit.setBid(bid);


				cr.add(Restrictions.eq("b", tit));
				cr.add(Restrictions.ne("cd_sit", '3'));
				cr.add(Restrictions.ne("fl_canc", 'S'));
				cr.add(Restrictions.ne("cd_serie.cd_polo", bib));
				cr.addOrder(Order.asc("cd_serie"));
				cr.addOrder(Order.desc("cd_inven"));
				List<Tbc_inventario> results = cr.list();
				return results;
			}catch (HibernateException e){
				throw new DaoManagerException(e);
			}
		}
		public List<Tbc_inventario> getListaInventariTitoloEditoreP(String codPolo, String codBib, String bid)
		throws DaoManagerException	{
			try{
				Session session = this.getCurrentSession();
				Criteria cr = session.createCriteria(Tbc_inventario.class);

				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(codPolo);
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(codBib);
				bib.setCd_polo(polo);
				Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
				serie.setCd_polo(bib);
				Tb_titolo tit = new Tb_titolo();
				tit.setBid(bid);

				//cr.add(Restrictions.eq("Cd_serie", serie));
				cr.add(Restrictions.eq("b", tit));
				cr.add(Restrictions.ne("fl_canc", 'S'));
				cr.add(Restrictions.ne("cd_sit", '3'));
				cr.add(Restrictions.eq("cd_serie.cd_polo", bib));
				cr.addOrder(Order.asc("cd_serie"));
				cr.addOrder(Order.desc("cd_inven"));
				List<Tbc_inventario> results = cr.list();
				return results;
			}catch (HibernateException e){
				throw new DaoManagerException(e);
			}
		}


	public List<Tbc_inventario> getListaAltriInventariDigitalizzati(String codPolo,
			String codBib, String bid, String serie, String codInv) throws DaoManagerException {
		try {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("codPolo", codPolo);
			params.put("codBib", codBib);
			params.put("bid", bid);
			params.put("codSerie", serie);
			params.put("codInv", Integer.valueOf(codInv));

			Session session = this.getCurrentSession();
			StringBuilder sql = new StringBuilder(1024);
			sql.append("select inv.* from tbc_inventario inv ");
			sql.append("where true ");
			sql.append("and inv.fl_canc<>'S' ");
			sql.append("and inv.cd_sit in ('1', '2') ");
			sql.append("and inv.cd_polo=:codPolo ");
			sql.append("and inv.cd_bib=:codBib ");
			sql.append("and inv.bid=:bid ");
			sql.append("and (inv.cd_serie<>:codSerie or inv.cd_inven<>:codInv) ");
			sql.append("and (inv.digitalizzazione in ('0', '1', '2') ");
			sql.append("	or	inv.cd_no_disp in ( ");
			sql.append("		select cod.cd_tabella ");
			sql.append("		from tb_codici cod ");
			sql.append("		where true ");
			sql.append("		 and cod.tp_tabella = 'CCND' ");
			sql.append("		 and cod.cd_flg2 = 'S' ");
			sql.append("		 and cod.cd_flg4 in ('0', '1', '2') ");
			sql.append(") )");

			SQLQuery query = session.createSQLQuery(sql.toString());
			query.setProperties(params);
			query.addEntity("inv", Tbc_inventario.class);
			return query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<CodiceVO> getListaInventariLegatiBid(String codPolo, String codBib, List<?> listaBid) throws DaoManagerException {
		List<CodiceVO> inventari = new ArrayList<CodiceVO>();

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaInventariBidStampaPatrimonio");
		q.setString("polo", codPolo);
		q.setString("bib", codBib);
		boolean isString = (listaBid.get(0) instanceof String);	//tipo parametro
		for (Object bid : listaBid) {
			q.setParameter("bid", isString ? (String)bid : ((CodiceVO) bid).getCodice() );
			for (Object entity : q.list()) {
				Tbc_inventario i = (Tbc_inventario) entity;
				inventari.add(new CodiceVO(i.getCd_serie().getCd_serie(), String.valueOf(i.getCd_inven())));
				session.evict(i);
			}
		}

		return inventari;
	}

}

