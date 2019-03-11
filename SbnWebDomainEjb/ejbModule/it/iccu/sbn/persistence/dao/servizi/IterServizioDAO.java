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
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.servizi.Tbl_controllo_iter;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class IterServizioDAO extends DaoManager {

	private RichiesteServizioDAO richiesteDAO  = new RichiesteServizioDAO();
	private AttivitaBibliotecarioDAO bibliotecarioDAO = new AttivitaBibliotecarioDAO();
	private TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();

	private void cancellaAttivitaIter(List<Tbl_iter_servizio> lstIter, int progr_iter_corrente) {
		Tbl_iter_servizio iter = lstIter.get(progr_iter_corrente-1);
		iter.setFl_canc('S');

	}


	private void aggiornaProgressiviAttivitaIter(List<Tbl_iter_servizio> lstIter, TipoAggiornamentoIter tipoOperazione) {
		Iterator<Tbl_iter_servizio> iterator = lstIter.iterator();
		Tbl_iter_servizio iter = null;
		while (iterator.hasNext()) {
			iter = iterator.next();
			switch (tipoOperazione) {
				case CANCELLAZIONE:
					iter.setProgr_iter((short)(iter.getProgr_iter()-1));
					break;
				case INSERIMENTO:
					iter.setProgr_iter((short)(iter.getProgr_iter()+1));
					break;
				default:;
			}
		}
	}


	public void aggiornaIterServizio(Tbl_iter_servizio iterServizio)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(iterServizio);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void scambioIter(int id_tipo_servizio, int progressivo, TipoAggiornamentoIter tipoOp)
	throws DaoManagerException {
		//Leggo il TIPO SERVIZIO
		Tbl_tipo_servizio tipo_servizio = tipoServizioDAO.getTipoServizioById(id_tipo_servizio);
		//Leggo le attività associate al tipo servizio
		List<Tbl_iter_servizio> listaIter = this.getListaIterServizio(tipo_servizio, 'N');

		if ( (progressivo==1 && tipoOp.equals(TipoAggiornamentoIter.SU)) || (progressivo==listaIter.size() && tipoOp.equals(TipoAggiornamentoIter.GIU)) )
			throw new DaoManagerException("Progressivo scelto incompatibile con il tipo di operazione");

		Tbl_iter_servizio iterScelto  = listaIter.get(progressivo-1);
		Tbl_iter_servizio iterScambio = null;
		switch (tipoOp) {
			case SU:
				iterScambio = listaIter.get(progressivo-2);
				break;
			case GIU:
				iterScambio = listaIter.get(progressivo);
				break;
			default:;
		}
		//scambio i due controlli scambiando i loro progressivi
		short progrScelto = iterScelto.getProgr_iter();
		iterScelto.setProgr_iter(iterScambio.getProgr_iter());
		iterScambio.setProgr_iter(progrScelto);
	}


	public void aggiornaIterServizio(int id_tipo_servizio, int progr_iter_corrente, Tbl_iter_servizio iterServizio, TipoAggiornamentoIter tipoOperazione)
	throws DaoManagerException {
		//Leggo il TIPO SERVIZIO
		Tbl_tipo_servizio tipo_servizio = tipoServizioDAO.getTipoServizioById(id_tipo_servizio);
		//Leggo le attività associate al tipo servizio
		List<Tbl_iter_servizio> listaIter= this.getListaIterServizio(tipo_servizio, 'N');


		switch (tipoOperazione) {
			case CANCELLAZIONE:
				//richiesto progr_iter_corrente
				if (progr_iter_corrente==0)
					throw new DaoManagerException("Richiesto progressivo iter da cancellare");
				this.cancellaAttivitaIter(listaIter, progr_iter_corrente);
				if (listaIter.size()>1 && progr_iter_corrente<listaIter.size()) {
					//se non ho cancellato l'unico della lista e non ho cancellato l'ultimo
					this.aggiornaProgressiviAttivitaIter(listaIter.subList(progr_iter_corrente, listaIter.size()), tipoOperazione);
				}
				tipoServizioDAO.aggiornamentoTipoServizio(tipo_servizio);
				break;
			case INSERIMENTO:
				//richiesto iterServizio e progr_iter_corrente
				if (iterServizio==null)
					throw new DaoManagerException("Richiesto Tbl_iter_servizio da inserire");
				if (progr_iter_corrente==0)
					throw new DaoManagerException("Richiesto progressivo iter per inserimento");
				this.aggiornaProgressiviAttivitaIter(listaIter.subList(progr_iter_corrente-1, listaIter.size()), tipoOperazione);
				tipoServizioDAO.aggiornamentoTipoServizio(tipo_servizio);

				iterServizio.setProgr_iter((short)progr_iter_corrente);
				this.aggiornaIterServizio(iterServizio);
				//listaIter.add(progr_iter_corrente-1, iterServizio);
				//tipo_servizio.getTbl_iter_servizio().add(iterServizio);
				break;
			case AGGIUNTA:
				//richiesto iterServizio
				if (iterServizio==null)
					throw new DaoManagerException("Richiesto Tbl_iter_servizio da aggiungere");
				if (listaIter.size()>0) {
					iterServizio.setProgr_iter((short)(listaIter.get(listaIter.size()-1).getProgr_iter()+1));
				} else {
					iterServizio.setProgr_iter((short)1);
				}
				this.aggiornaIterServizio(iterServizio);
				//listaIter.add(iterServizio);
				//tipo_servizio.getTbl_iter_servizio().add(iterServizio);

				break;
			case MODIFICA:
				this.aggiornaIterServizio(iterServizio);
				break;
			default:;
		}

		//this.aggiornamentoTipoServizio(tipo_servizio);
	}


	public boolean cancellaIterServizio(int id_tipo_servizio, List<Short> progressiviIter, String utenteVar)
	throws DaoManagerException {
		//Leggo il TIPO SERVIZIO
		Tbl_tipo_servizio tipo_servizio = tipoServizioDAO.getTipoServizioById(id_tipo_servizio);
		//Leggo le attività associate al tipo servizio
		List<Tbl_iter_servizio> listaIter = this.getListaIterServizio(tipo_servizio, 'N');

		Iterator<Tbl_iter_servizio> i = listaIter.iterator();
		short progressivoCorrente = 0;

		//almaviva5_20160628 cancella tutti gli iter se non sono specificati i progressivi
		boolean deleteAll = !ValidazioneDati.isFilled(progressiviIter);

		boolean flgErr = false;
		while (i.hasNext()) {
			Tbl_iter_servizio iter = i.next();
			if (!deleteAll && !progressiviIter.contains(iter.getProgr_iter()) ) {
				continue;
			}

			if (!richiesteDAO.esistonoRichiestePer(iter)) {
				//Iter da cancellare
				iter.setFl_canc('S');
				iter.setUte_var(utenteVar);
				//iter.setTs_var(DaoManager.now());
				this.cancellaControlloIter(iter, utenteVar);
				bibliotecarioDAO.cancellaAttivitaBibliotecario(iter, utenteVar);
			} else {
				//Iter non da cancellare o perchè non è stato cancellato o perchè esistono movimenti/richieste attivi
				progressivoCorrente++;
				if (iter.getProgr_iter() != progressivoCorrente) {
					iter.setProgr_iter(progressivoCorrente);
				}
				flgErr = true;
			}
		}
		if (flgErr == true) {
			return false;
		} else {
			return true;
		}
	}


	public List<Tbl_iter_servizio> getListaIterServizio(Tbl_tipo_servizio tipo_servizio, char stato)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			if (tipo_servizio!=null) {
				Query query = session.createFilter(tipo_servizio.getTbl_iter_servizio(), "where fl_canc=:stato order by progr_iter asc");
				query.setCharacter("stato", stato);
				return query.list();
			} else return new ArrayList<Tbl_iter_servizio>();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_iter_servizio> getListaIterServizio(String codPolo,
			String codBib, String codTipoServ) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_iter_servizio.class);
			c.add(Restrictions.ne("fl_canc", 'S'));

			if (!ValidazioneDati.isFilled(codTipoServ)) {
				//iter relativi a tutti i servizi associati alla bib.
				List<Tbl_tipo_servizio> listaTipiServizio = tipoServizioDAO.getListaTipiServizio(codPolo, codBib);
				if (ValidazioneDati.isFilled(listaTipiServizio)) {
					c.add(Restrictions.in("id_tipo_servizio", listaTipiServizio))
							.addOrder(Order.asc("cod_attivita"))
							.addOrder(Order.asc("progr_iter"));
				}
				else
					return new ArrayList<Tbl_iter_servizio>();
			}
			else {
				//iter relativi al servizio specificato
				Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServ);
				c.add(Restrictions.eq("id_tipo_servizio", tipoServizio))
						.addOrder(Order.asc("progr_iter"));
			}

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	/**
	 *
	 * @param tipoServizio
	 * @param codAttivita
	 * @return
	 * @throws DaoManagerException
	 */
	public Tbl_iter_servizio getIterServizio(String codPolo, String codBib,
			String codTipoServ, String codAttivita) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_iter_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));

			//iter relativi al servizio specificato
			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServ);
			Set iterServizioSet = tipoServizio.getTbl_iter_servizio();
			Query query = session.createFilter(iterServizioSet, "where fl_canc!='S' and cod_attivita = :codAttivita");
			query.setString("codAttivita", codAttivita);
			Tbl_iter_servizio iterServizio = (Tbl_iter_servizio)query.uniqueResult();

			return iterServizio;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_iter_servizio getIterServizio(Tbl_tipo_servizio tipoServizio, String codAttivita)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria c = session.createCriteria(Tbl_iter_servizio.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.eq("id_tipo_servizio", tipoServizio));
			c.add(Restrictions.eq("cod_attivita", codAttivita));
			Tbl_iter_servizio iterServizio = (Tbl_iter_servizio)c.uniqueResult();

			return iterServizio;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	/**
	 *
	 * @param idTipoServizio
	 * @param codAttivita
	 * @return Istanza di Tbl_iter_servizio
	 * @throws DaoManagerException
	 */
	public Tbl_iter_servizio getIterServizio(int idTipoServizio, String codAttivita)
	throws DaoManagerException {
		try {
			//Ricavo il tipo servizio
			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizioById(idTipoServizio);

			return this.getIterServizio(tipoServizio, codAttivita);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_iter_servizio getIterServizioById(int idIterServizio)
	throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			return (Tbl_iter_servizio)loadNoLazy(session, Tbl_iter_servizio.class, new Integer(idIterServizio));
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_controllo_iter getControlloIter(Tbl_iter_servizio iterServizio, int codiceControllo)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Set controlloIterSet = iterServizio.getTbl_controllo_iter();
			Query query = session.createFilter(controlloIterSet, "where fl_canc!='S' and cod_controllo=:codControllo");
			query.setInteger("codControllo", codiceControllo);
			return (Tbl_controllo_iter)query.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_controllo_iter getControlloIter(int idTipoServizio, String codAttivita, int codControllo, String flCanc)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbl_iter_servizio iterServizio = this.getIterServizio(idTipoServizio, codAttivita);
			if (iterServizio!=null) {
				Set controlloIterSet = iterServizio.getTbl_controllo_iter();
				Query query = session.createFilter(controlloIterSet, "where fl_canc=:flCanc and cod_controllo=:codControllo");
				query.setInteger("codControllo", codControllo);
				query.setString("flCanc",        flCanc);
				return (Tbl_controllo_iter)query.uniqueResult();
			} else return null;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List getControlloIter(int idTipoServizio, String codAttivita)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbl_iter_servizio iterServizio = this.getIterServizio(idTipoServizio, codAttivita);
			if (iterServizio!=null) {
				Set controlloIterSet = iterServizio.getTbl_controllo_iter();
				Query query = session.createFilter(controlloIterSet, "where fl_canc!='S' order by progr_fase asc");

				return query.list();
			}
			else {
				return new ArrayList();
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}
	public List<Tbl_controllo_iter> getControlloIter(Tbl_iter_servizio iterServizio)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			if (iterServizio!=null) {
				Set controlloIterSet = iterServizio.getTbl_controllo_iter();
				Query query = session.createFilter(controlloIterSet, "where fl_canc!='S' order by progr_fase");
				return query.list();
			}
			else {
				return new ArrayList();
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public void scambioControlliIter(int idTipoServizio, String codAttivita, short progressivo, TipoAggiornamentoIter tipoOp)
	throws DaoManagerException {
		Tbl_iter_servizio iterServizio = this.getIterServizio(idTipoServizio, codAttivita);
		if (iterServizio==null)
			throw new DaoManagerException("Iter servizio non caricato dalla base dati. Tipo servizio:"+idTipoServizio+"  Attività:"+codAttivita);

		List<Tbl_controllo_iter> listaControlli = this.getControlloIter(iterServizio);

		if ( (progressivo==1 && tipoOp.equals(TipoAggiornamentoIter.SU)) || (progressivo==listaControlli.size() && tipoOp.equals(TipoAggiornamentoIter.GIU)) )
			throw new DaoManagerException("Progressivo scelto incompatibile con il tipo di operazione");

		Tbl_controllo_iter controlloScelto  = listaControlli.get(progressivo-1);
		Tbl_controllo_iter controlloScambio = null;
		switch (tipoOp) {
			case SU:
				controlloScambio = listaControlli.get(progressivo-2);
				break;
			case GIU:
				controlloScambio = listaControlli.get(progressivo);
				break;
			default:;
		}
		//scambio i due controlli scambiando i loro progressivi
		short progrScelto = controlloScelto.getProgr_fase();
		controlloScelto.setProgr_fase(controlloScambio.getProgr_fase());
		controlloScambio.setProgr_fase(progrScelto);
	}


	public void aggiornamentoControlloIter(Tbl_controllo_iter controllo, int idTipoServizio,
											String codAttivita, TipoAggiornamentoIter tipoOperazione, short posizioneInserimento, boolean ripristino)
	throws DaoManagerException {
		Tbl_iter_servizio iterServizio = this.getIterServizio(idTipoServizio, codAttivita);
		if (iterServizio==null)
			throw new DaoManagerException("Iter servizio non caricato dalla base dati. Tipo servizio:"+idTipoServizio+"  Attività:"+codAttivita);

		List<Tbl_controllo_iter> listaControlli = this.getControlloIter(iterServizio);

		if (controllo==null) throw new DaoManagerException("Controllo iter null");

		//Si tratta di un re-inserimento di una riga cancellata logicamente, quindi già presente in tabella
		//ripristino il flag canc a 'N'
		if (ripristino)
			controllo.setFl_canc('N');

		switch (tipoOperazione) {
		case INSERIMENTO:
			if (posizioneInserimento<=0)
				throw new DaoManagerException("Posizione per inserimento controllo non pervenuta");
			this.aggiornaProgressiviFaseControllo(listaControlli.subList(posizioneInserimento-1, listaControlli.size()), tipoOperazione);
			this.aggiornaIterServizio(iterServizio);

			controllo.setProgr_fase(posizioneInserimento);
			this.aggiornamentoControlloIter(controllo);
			break;
		case AGGIUNTA:
			if (listaControlli.size()==0)	controllo.setProgr_fase((short)1);
			else	controllo.setProgr_fase((short)(listaControlli.get(listaControlli.size()-1).getProgr_fase()+1));
			this.aggiornamentoControlloIter(controllo);
			break;
		case MODIFICA:
			this.aggiornamentoControlloIter(controllo);
			break;
		default:;
		}
	}


	public void aggiornamentoControlloIter(Tbl_controllo_iter controllo_iter)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			session.saveOrUpdate(controllo_iter);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	private void aggiornaProgressiviFaseControllo(List<Tbl_controllo_iter> lstControlli, TipoAggiornamentoIter tipoOperazione) {
		Iterator<Tbl_controllo_iter> iterator = lstControlli.iterator();
		Tbl_controllo_iter controllo = null;
		while (iterator.hasNext()) {
			controllo = iterator.next();
			switch (tipoOperazione) {
				case INSERIMENTO:
					controllo.setProgr_fase((short)(controllo.getProgr_fase()+1));
					break;
				default:;
			}
		}
	}


	public void cancellaControlloIter(List<String> codiciControllo, int idTipoServizio, String codAttivita, String utenteVar)
	throws DaoManagerException {
		if (codiciControllo.size()==0) return;
		try{
			Tbl_iter_servizio iterServizio = this.getIterServizio(idTipoServizio, codAttivita);
			if (iterServizio==null)
				throw new DaoManagerException("Iter servizio non caricato dalla base dati. Tipo servizio:"+idTipoServizio+"  Attività:"+codAttivita);

			List<Tbl_controllo_iter> listaControlli = this.getControlloIter(iterServizio);
			if (listaControlli.size()==0)
				throw new DaoManagerException("Controlli non caricati dalla base dati. Iter servizio:"+iterServizio.getId_iter_servizio()+"  Attività:"+codAttivita);

			Iterator<Tbl_controllo_iter> iterator = listaControlli.iterator();
			Tbl_controllo_iter controllo=null;
			short progr_fase=0;
			while (iterator.hasNext()) {
				controllo=iterator.next();
				if (codiciControllo.contains(new Short(controllo.getCod_controllo()).toString())) {
					//Il controllo è da cancellare
					controllo.setFl_canc('S');
					controllo.setUte_var(utenteVar);
					controllo.setTs_var(DaoManager.now());
				} else {
					//Il controllo non è da cancellare
					progr_fase++;
					if (controllo.getProgr_fase()!=progr_fase) {
						controllo.setProgr_fase(progr_fase);
					}
				}
			}
			this.aggiornaIterServizio(iterServizio);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaControlloIter(Tbl_controllo_iter controlloIter, String utenteVar)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try{
			controlloIter.setFl_canc('S');
			controlloIter.setUte_var(utenteVar);
			session.saveOrUpdate(controlloIter);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaControlloIter(Tbl_iter_servizio iterServizio, int codiceControllo, String utenteVar)
	throws DaoManagerException {
		Tbl_controllo_iter controlloIter = this.getControlloIter(iterServizio, codiceControllo);
		if (controlloIter != null) {
			this.cancellaControlloIter(controlloIter, utenteVar);
		} else
			throw new DaoManagerException("Controllo iter non trovato in base dati. Codice: "+codiceControllo);
	}

	private void cancellaControlloIter(Tbl_iter_servizio iterServizio, String utenteVar)
	throws DaoManagerException {
		List<Tbl_controllo_iter> controlli = this.getControlloIter(iterServizio);
		for (Tbl_controllo_iter controllo : controlli) {
			controllo.setFl_canc('S');
			controllo.setUte_var(utenteVar);
		}
	}


	public Tbl_iter_servizio getIterServizio(String codPolo, String codBib,
			String codTipoServ, int progrIter) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_iter_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));

			//iter relativi al servizio specificato
			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServ);
			criteria.add(Restrictions.eq("id_tipo_servizio", tipoServizio));
			criteria.add(Restrictions.eq("progr_iter", (short)progrIter));

			return (Tbl_iter_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public int contaMovimentiAttiviPerIter(int id) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_richiesta_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.in("cod_stato_mov", new String[]{"A", "S"}))
					.add(Restrictions.eq("id_iter_servizio.id", id))
					.setProjection(Projections.rowCount());

			Number count = (Number) criteria.uniqueResult();
			return count.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
