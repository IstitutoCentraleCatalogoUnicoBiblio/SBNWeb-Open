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
package it.finsiel.sbn.polo.dao.entity.tavole;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
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
public class Tr_tit_bibResult extends it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao{

    private Tr_tit_bib tr_tit_bib;
	public Tr_tit_bibResult(Tr_tit_bib tr_tit_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_tit_bib.leggiAllParametro());
        this.tr_tit_bib = tr_tit_bib;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
			WHERE
			  bid = XXXbid
			  AND cd_biblioteca = XXXcd_biblioteca
			  AND cd_polo = XXXcd_polo
			  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectPerKey");


			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);

			List<Tr_tit_bib> result = this.basicCriteria
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
    <filter name="TR_TIT_BIB_selectPerPolo"
        condition="bid = :XXXbid
        AND cd_polo = :XXXcd_polo
        AND fl_canc != 'S' "/>
	 */
    public  List<Tr_tit_bib>  selectPerPolo(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TR_TIT_BIB_selectPerPolo");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

            List<Tr_tit_bib> result = this.basicCriteria
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


	/**
	<statement nome="selectPerPoloDiverso" tipo="select" id="10_taymer">
		<fisso>
				WHERE
				  cd_polo != XXXcd_polo
				  AND bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectPerPoloDiverso(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectPerPoloDiverso");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

			List<Tr_tit_bib> result = this.basicCriteria
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

	/**
	<statement nome="selectPerBid" tipo="select" id="12_taymer">
		<fisso>
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectPerBid");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);

			List<Tr_tit_bib> result = this.basicCriteria
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

	/**
	<statement nome="countPerBid" tipo="count" id="18_dani">
		<fisso>
			SELECT count(*) FROM tr_tit_bib
			WHERE fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_countPerBid");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();

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

	/**
	<statement nome="selectPerAllineamento" tipo="select" id="14_taymer">
		<fisso>
				WHERE
				  cd_polo != XXXcd_polo
				  AND bid = XXXbid
				  AND fl_gestione != 'N'
				  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectPerAllineamento(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectPerAllineamento");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

			List<Tr_tit_bib> result = this.basicCriteria
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

	/**
	<statement nome="verificaLocalizzazioni" tipo="select" id="16_taymer">
		<fisso>
			WHERE
			  bid = XXXbid
			  AND cd_polo = XXXcd_polo
			  AND fl_canc != 'S'
			  AND fl_gestione != 'N'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> verificaLocalizzazioni(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_verificaLocalizzazioni");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

			List<Tr_tit_bib> result = this.basicCriteria
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
    /**
//    <filter name="TR_TIT_BIB_verificaLocalizzazioniPerBiblioteca"
//        condition="bid = :XXXbid
//                   AND cd_biblioteca = :XXXcd_biblioteca
//                   AND fl_canc != 'S'
//                   AND fl_possesso = 'S'"/>
    </statement>
     *
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     * ATTUALMENTE SUPERATA DALLA CHIAMATA IN CASCATA CON FILTRO SU TR_TIT_BIB
     */
    public List<Tr_tit_bib> verificaLocalizzazioniPerBiblioteca(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TR_TIT_BIB_verificaLocalizzazioniPerBiblioteca");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

            List<Tr_tit_bib> result = this.basicCriteria
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

	/**
	<statement nome="selectPerPoloUguale" tipo="select" id="19_dani">
		<fisso>
				WHERE
				  cd_polo = XXXcd_polo
				  AND bid = XXXbid
				  AND fl_gestione != 'N'
				  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectPerPoloUguale(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectPerPoloUguale");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

			List<Tr_tit_bib> result = this.basicCriteria
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

	/**
	<statement nome="selectCancellato" tipo="select" id="21_taymer">
		<fisso>
			WHERE
			  bid = XXXbid
			  AND cd_biblioteca = XXXcd_biblioteca
			  AND cd_polo = XXXcd_polo
			  AND fl_canc = 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectCancellato(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectCancellato");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

			List<Tr_tit_bib> result = this.basicCriteria
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

	/**
	<statement nome="selectPerPoloBibliotecaUguale" tipo="select" id="22_taymer">
		<fisso>
				WHERE
				  cd_polo = XXXcd_polo
				  AND cd_biblioteca = XXXcd_biblioteca
				  AND bid = XXXbid
				  AND fl_gestione != 'N'
				  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectPerPoloBibliotecaUguale(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectPerPoloBibliotecaUguale");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_biblioteca);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

			List<Tr_tit_bib> result = this.basicCriteria
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

	/**
	<statement nome="selectPerFlagAllinea" tipo="select" id="25_taymer">
		<fisso>
				WHERE
				  fl_canc != 'S'
				  AND fl_allinea = XXXfl_allinea
				  AND bid = XXXbid
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_bib> selectPerFlagAllinea(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_BIB_selectPerFlagAllinea");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXfl_allinea, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXfl_allinea));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_bibCommonDao.XXXfl_allinea);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_bib",
                    this.basicCriteria, session);

			List<Tr_tit_bib> result = this.basicCriteria
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


    /**
     * 	<statement nome="update" tipo="update" id="04_taymer">
		<fisso>
			UPDATE Tr_tit_bib
			 SET
			  fl_canc = XXXfl_canc ,
			  ute_var = XXXute_var ,
			  ds_fondo = XXXds_fondo ,
			  ds_antica_segn = XXXds_antica_segn ,
			  fl_allinea_cla = XXXfl_allinea_cla ,
			  ts_var = SYSTIMESTAMP ,
			  uri_copia = XXXuri_copia ,
			  fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc ,
			  fl_possesso = XXXfl_possesso ,
			  fl_gestione = XXXfl_gestione ,
			  ds_segn = XXXds_segn ,
			  fl_allinea = XXXfl_allinea ,
			  ds_consistenza = XXXds_consistenza ,
			  fl_mutilo = XXXfl_mutilo ,
			  nota_tit_bib = XXXnota_tit_bib ,
			  fl_allinea_rep = XXXfl_allinea_rep ,
			  fl_disp_elettr = XXXfl_disp_elettr ,
			  tp_digitalizz = XXXtp_digitalizz ,
			  fl_allinea_luo = XXXfl_allinea_luo ,
			  fl_allinea_sog = XXXfl_allinea_sog
			WHERE
			  bid = XXXbid
			  AND cd_biblioteca = XXXcd_biblioteca
			  AND cd_polo = XXXcd_polo
		</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ds_fondo",this.getParametro().get(KeyParameter.XXXds_fondo));
		buildUpdate.addProperty("ds_antica_segn",this.getParametro().get(KeyParameter.XXXds_antica_segn));
		buildUpdate.addProperty("fl_allinea_cla",this.getParametro().get(KeyParameter.XXXfl_allinea_cla));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("uri_copia",this.getParametro().get(KeyParameter.XXXuri_copia));
		buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));
		buildUpdate.addProperty("fl_possesso",this.getParametro().get(KeyParameter.XXXfl_possesso));
		buildUpdate.addProperty("fl_gestione",this.getParametro().get(KeyParameter.XXXfl_gestione));
		buildUpdate.addProperty("ds_segn",this.getParametro().get(KeyParameter.XXXds_segn));
		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
		buildUpdate.addProperty("ds_consistenza",this.getParametro().get(KeyParameter.XXXds_consistenza));
		buildUpdate.addProperty("fl_mutilo",this.getParametro().get(KeyParameter.XXXfl_mutilo));
		buildUpdate.addProperty("nota_tit_bib",this.getParametro().get(KeyParameter.XXXnota_tit_bib));
		buildUpdate.addProperty("fl_allinea_rep",this.getParametro().get(KeyParameter.XXXfl_allinea_rep));
		buildUpdate.addProperty("fl_disp_elettr",this.getParametro().get(KeyParameter.XXXfl_disp_elettr));
		buildUpdate.addProperty("tp_digitalizz",this.getParametro().get(KeyParameter.XXXtp_digitalizz));
		buildUpdate.addProperty("fl_allinea_luo",this.getParametro().get(KeyParameter.XXXfl_allinea_luo));
		buildUpdate.addProperty("fl_allinea_sog",this.getParametro().get(KeyParameter.XXXfl_allinea_sog));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}


    /*
     *  <statement nome="insert" tipo="insert" id="03">
        <fisso>
            INSERT INTO Tr_tit_bib
             (
              fl_canc ,
              ute_var ,
              ds_fondo ,
              ds_antica_segn ,
              fl_allinea_cla ,
              ts_var ,
              uri_copia ,
              ute_ins ,
              fl_allinea_sbnmarc ,
              fl_possesso ,
              cd_biblioteca ,
              cd_polo ,
              ts_ins ,
              fl_gestione ,
              ds_segn ,
              fl_allinea ,
              ds_consistenza ,
              fl_mutilo ,
              nota_tit_bib ,
              fl_allinea_rep ,
              fl_disp_elettr ,
              bid ,
              tp_digitalizz ,
              fl_allinea_luo ,
              fl_allinea_sog
             )
            VALUES
             (
              XXXfl_canc ,
              XXXute_var ,
              XXXds_fondo ,
              XXXds_antica_segn ,
              XXXfl_allinea_cla ,
              SYSTIMESTAMP ,
              XXXuri_copia ,
              XXXute_ins ,
              XXXfl_allinea_sbnmarc ,
              XXXfl_possesso ,
              XXXcd_biblioteca ,
              XXXcd_polo ,
              SYSTIMESTAMP ,
              XXXfl_gestione ,
              XXXds_segn ,
              XXXfl_allinea ,
              XXXds_consistenza ,
              XXXfl_mutilo ,
              XXXnota_tit_bib ,
              XXXfl_allinea_rep ,
              XXXfl_disp_elettr ,
              XXXbid ,
              XXXtp_digitalizz ,
              XXXfl_allinea_luo ,
              XXXfl_allinea_sog
             )
        </fisso>
    </statement>

     */
    public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_tit_bib.setTS_INS(now);
        tr_tit_bib.setTS_VAR(now);
        session.saveOrUpdate(this.tr_tit_bib);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 *
	 <statement nome="updatePerModifica" tipo="update" id="Jenny_07">
		<fisso>
			UPDATE Tr_tit_bib
			 SET
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_gestione = XXXfl_gestione ,
			  fl_possesso = XXXfl_possesso ,
			  fl_canc = XXXfl_canc
			WHERE
			  bid = XXXbid
			  AND cd_biblioteca = XXXcd_biblioteca
			  AND cd_polo = XXXcd_polo
			  AND fl_canc != 'S'
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updatePerModifica(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo updatePerModifica invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(Tr_tit_bibCommonDao.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_gestione",this.getParametro().get(Tr_tit_bibCommonDao.XXXfl_gestione));
		buildUpdate.addProperty("fl_possesso",this.getParametro().get(Tr_tit_bibCommonDao.XXXfl_possesso));
        buildUpdate.addProperty("fl_canc",this.getParametro().get(Tr_tit_bibCommonDao.XXXfl_canc));


		buildUpdate.addWhere("bid",this.getParametro().get(Tr_tit_bibCommonDao.XXXbid),"=");
		buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(Tr_tit_bibCommonDao.XXXcd_biblioteca),"=");
		buildUpdate.addWhere("cd_polo",this.getParametro().get(Tr_tit_bibCommonDao.XXXcd_polo),"=");
		buildUpdate.addWhere("fl_canc","S","!=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 		<statement nome="updatePerModificaBiblioteca" tipo="update" id="Carlo_03">
		<fisso>
			UPDATE Tr_tit_bib
			 SET
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_allinea = XXXfl_allinea,
			  fl_canc = XXXfl_canc,
  			  fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc ,
			  fl_allinea_luo = XXXfl_allinea_luo ,
			  fl_allinea_sog = XXXfl_allinea_sog ,
			  fl_allinea_cla = XXXfl_allinea_cla ,
			  fl_allinea_rep = XXXfl_allinea_rep
			WHERE
			  bid = XXXbid
			  AND cd_biblioteca = XXXcd_biblioteca
			  AND cd_polo = XXXcd_polo
		</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updatePerModificaBiblioteca(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo updatePerModificaBiblioteca invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));
		buildUpdate.addProperty("fl_allinea_luo",this.getParametro().get(KeyParameter.XXXfl_allinea_luo));
		buildUpdate.addProperty("fl_allinea_sog",this.getParametro().get(KeyParameter.XXXfl_allinea_sog));
		buildUpdate.addProperty("fl_allinea_cla",this.getParametro().get(KeyParameter.XXXfl_allinea_cla));
		buildUpdate.addProperty("fl_allinea_rep",this.getParametro().get(KeyParameter.XXXfl_allinea_rep));


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     *	<statement nome="updateT899" tipo="update" id="Jenny_06">
		<fisso>
			UPDATE Tr_tit_bib
			 SET
			  ute_var = XXXute_var ,
			  ds_fondo = XXXds_fondo ,
			  ds_antica_segn = XXXds_antica_segn ,
			  ts_var = SYSTIMESTAMP ,
			  uri_copia = XXXuri_copia ,
			  ds_segn = XXXds_segn ,
			  tp_digitalizz = XXXtp_digitalizz ,
			  ds_consistenza = XXXds_consistenza ,
			  fl_mutilo = XXXfl_mutilo ,
			  nota_tit_bib = XXXnota_tit_bib,
			  fl_canc = XXXfl_canc
			WHERE
			  bid = XXXbid
			  AND cd_biblioteca = XXXcd_biblioteca
			  AND cd_polo = XXXcd_polo
		</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateT899(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo updateT899 invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ds_fondo",this.getParametro().get(KeyParameter.XXXds_fondo));
		buildUpdate.addProperty("ds_antica_segn",this.getParametro().get(KeyParameter.XXXds_antica_segn));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("uri_copia",this.getParametro().get(KeyParameter.XXXuri_copia));
		buildUpdate.addProperty("ds_segn",this.getParametro().get(KeyParameter.XXXds_segn));

		// Inizio Modifica del 11.12.2013 per riportare in Polo anche il campo fl_disp_elettr prima DIMENTICATO !!!
		buildUpdate.addProperty("fl_disp_elettr",this.getParametro().get(KeyParameter.XXXfl_disp_elettr));
		 // FIne Modifica del 11.12.2013 per riportare in Polo anche il campo fl_disp_elettr prima DIMENTICATO !!!

		buildUpdate.addProperty("tp_digitalizz",this.getParametro().get(KeyParameter.XXXtp_digitalizz));
		buildUpdate.addProperty("ds_consistenza",this.getParametro().get(KeyParameter.XXXds_consistenza));
		buildUpdate.addProperty("fl_mutilo",this.getParametro().get(KeyParameter.XXXfl_mutilo));
		buildUpdate.addProperty("nota_tit_bib",this.getParametro().get(KeyParameter.XXXnota_tit_bib));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updatePerModificaPoloDiverso" tipo="update" id="20_taymer">
		<fisso>
			UPDATE Tr_tit_bib
			 SET
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_canc = XXXfl_canc ,
			  fl_allinea = XXXfl_allinea
			WHERE
			  bid = XXXbid
			  AND fl_canc != 'S'
			  AND fl_gestione != 'N'
		</fisso>
		<opzionale dipende="XXXcd_polo">AND cd_polo != XXXcd_polo</opzionale>
	</statement>

     *
     */
    public void updatePerModificaPoloDiverso(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo updatePerModificaPoloDiverso invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");
		buildUpdate.addWhere("fl_gestione","N","!=");
		if(this.getParametro().containsKey(KeyParameter.XXXcd_polo))
			buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
    /**
     * 	<statement nome="updateCancellaPerBid" tipo="update" id="11_taymer">
			<fisso>
				UPDATE Tr_tit_bib
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }




    /*
     *  <statement nome="updateTuttiFlag" tipo="update" id="15_taymer">
        <fisso>
            UPDATE Tr_tit_bib
             SET
              ute_var = XXXute_var ,
              ts_var = SYSTIMESTAMP,
              fl_allinea = XXXfl_allinea,
              fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc ,
              fl_allinea_luo = XXXfl_allinea_luo ,
              fl_allinea_sog = XXXfl_allinea_sog ,
              fl_allinea_cla = XXXfl_allinea_cla ,
              fl_allinea_rep = XXXfl_allinea_rep
            WHERE
              bid = XXXbid
              AND cd_biblioteca = XXXcd_biblioteca
              AND cd_polo = XXXcd_polo
        </fisso>
    </statement>

     */
    public void updateTuttiFlag(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo updateTuttiFlag invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
        buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));
        buildUpdate.addProperty("fl_allinea_luo",this.getParametro().get(KeyParameter.XXXfl_allinea_luo));
        buildUpdate.addProperty("fl_allinea_sog",this.getParametro().get(KeyParameter.XXXfl_allinea_sog));
        buildUpdate.addProperty("fl_allinea_cla",this.getParametro().get(KeyParameter.XXXfl_allinea_cla));
        buildUpdate.addProperty("fl_allinea_rep",this.getParametro().get(KeyParameter.XXXfl_allinea_rep));


        buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
        buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
        buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");


        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }
    /*
     *  <statement nome="updateAllineaPerBid" tipo="update" id="17_taymer">
            <fisso>
                UPDATE Tr_tit_bib
                 SET
                 fl_allinea = XXXfl_allinea ,
                 ute_var = XXXute_var ,
                 ts_var = SYSTIMESTAMP
                WHERE
                  bid = XXXbid
            </fisso>
    </statement>

     */
    public void updateAllineaPerBid(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_bib metodo updateAllineaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_bib",Tr_tit_bib.class);

        buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());


        buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");


        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_tit_bib.class;
	}
}
