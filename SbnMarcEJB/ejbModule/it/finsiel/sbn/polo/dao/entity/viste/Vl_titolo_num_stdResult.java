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
package it.finsiel.sbn.polo.dao.entity.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_num_stdCommonDao;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_num_std;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;


/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_titolo_num_stdResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_num_stdCommonDao{

	private Vl_titolo_num_std vl_titolo_num_std;

	public Vl_titolo_num_stdResult(Vl_titolo_num_std vl_titolo_num_std) throws InfrastructureException {
        super();
        this.vl_titolo_num_std = vl_titolo_num_std;
        this.valorizzaParametro(vl_titolo_num_std.leggiAllParametro());
    }
/*
<filter name="VL_TITOLO_NUM_STD_selectPerNumero"
		condition="numero_std = :XXXnumero_std AND tp_numero_std = :XXXtp_numero_std"/>
*/
	public List<Vl_titolo_num_std> selectPerNumero(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_TITOLO_NUM_STD_selectPerNumero");

			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXnumero_std, opzioni.get(Vl_titolo_num_stdCommonDao.XXXnumero_std));
			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXtp_numero_std, opzioni.get(Vl_titolo_num_stdCommonDao.XXXtp_numero_std));

			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXnumero_std);
			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXtp_numero_std);
			this.createCriteria(myOpzioni);
			List<Vl_titolo_num_std> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}
	
/*
<filter name="VL_TITOLO_NUM_STD_selectPerListaNumeri"
		condition="numero_std in ( :XXXnumero_std  ) AND
				   tp_numero_std = :XXXtp_numero_std"/>
*/
	public List<Vl_titolo_num_std> selectPerListaNumeri(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session
					.createCriteria(Vl_titolo_num_std.class);
			Filter filter = session
					.enableFilter("VL_TITOLO_NUM_STD_selectPerListaNumeri");

			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXnumero_std, opzioni
					.get(Vl_titolo_num_stdCommonDao.XXXnumero_std));
			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXtp_numero_std,
					opzioni.get(Vl_titolo_num_stdCommonDao.XXXtp_numero_std));

			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXnumero_std);
			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXtp_numero_std);
			this.createCriteria(myOpzioni);
			List<Vl_titolo_num_std> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}


/*
<filter name="VL_TITOLO_NUM_STD_selectPerBid"
		condition="fl_canc != 'S' AND bid = :XXXbid	AND numero_std is not null"/>
*/
	public List<Vl_titolo_num_std> selectPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session
					.createCriteria(Vl_titolo_num_std.class);
			Filter filter = session
					.enableFilter("VL_TITOLO_NUM_STD_selectPerBid");

			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXbid, opzioni
					.get(Vl_titolo_num_stdCommonDao.XXXbid));

			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXbid);
			this.createCriteria(myOpzioni);
			List<Vl_titolo_num_std> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

/*
<filter name="VL_TITOLO_NUM_STD_selectPerLastra"
		condition="tp_numero_std = :XXXtp_numero_std"/>
*/
	public List<Vl_titolo_num_std> selectPerLastra(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session
					.createCriteria(Vl_titolo_num_std.class);
			Filter filter = session
					.enableFilter("VL_TITOLO_NUM_STD_selectPerLastra");

			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXtp_numero_std,
					opzioni.get(Vl_titolo_num_stdCommonDao.XXXtp_numero_std));

			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXtp_numero_std);
			this.createCriteria(myOpzioni);
			List<Vl_titolo_num_std> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

/*
<filter name="VL_TITOLO_NUM_STD_countPerLastra"
		condition="tp_numero_std = :XXXtp_numero_std"/>
*/
	public Integer countPerLastra(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());

			Filter filter = session.enableFilter("VL_TITOLO_NUM_STD_countPerLastra");

			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXtp_numero_std, opzioni
					.get(Vl_titolo_num_stdCommonDao.XXXtp_numero_std));

			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXtp_numero_std);
			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(Projections.rowCount()))
					.uniqueResult();

			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	
    // Inizio manutenzione correttiva 27.09.2019 Bug MANTIS 7124 
    // Vedi anche Protocollo Indice:MANTIS 2108 poich� non fa la count non imposta "numRecord"
    // Il campo ricercaSenzaCount non deve essere impostato altrimenti vengono portati in sintetica solo le prime 10 (maxrighe)
    // richieste perdendo la paginazione sulle successive occorrenze.
	public Integer countPerNumero(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());

			Filter filter = session.enableFilter("VL_TITOLO_NUM_STD_countPerNumero");
			
			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXnumero_std, opzioni.get(Vl_titolo_num_stdCommonDao.XXXnumero_std));
			filter.setParameter(Vl_titolo_num_stdCommonDao.XXXtp_numero_std, opzioni.get(Vl_titolo_num_stdCommonDao.XXXtp_numero_std));

			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXnumero_std);
			myOpzioni.remove(Vl_titolo_num_stdCommonDao.XXXtp_numero_std);
			
			
			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(Projections.rowCount()))
					.uniqueResult();

			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

  // Fine manutenzione correttiva 27.09.2019 Bug MANTIS 7124 
	
	
	
	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_titolo_num_std.class;
	}

}
