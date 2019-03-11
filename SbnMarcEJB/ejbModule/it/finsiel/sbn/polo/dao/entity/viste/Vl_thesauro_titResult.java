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
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_thesauro_tit;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_thesauro_titResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao{

    public Vl_thesauro_titResult(Vl_thesauro_tit vl_thesauro_tit) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_thesauro_tit.leggiAllParametro());
    }
     /*
    <statement nome="selectThesauroPerTitolo" tipo="select" id="02">
            <fisso>
                WHERE
                bid = XXXbid
                AND fl_canc != 'S'
            </fisso>
    </statement>
    <filter name="VL_THESAURO_TIT_selectThesauroPerTitolo"
            condition="bid = :XXXbid
                      AND fl_canc != 'S' "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_thesauro_tit> selectThesauroPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget()).setResultTransformer(this.basicCriteria.DISTINCT_ROOT_ENTITY);
			//this.basicCriteria = session.createCriteria(Vl_soggetto_tit.class);
			Filter filter = session.enableFilter("VL_THESAURO_TIT_selectThesauroPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            // NO FILTRI this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_thesauro_tit",
                    this.basicCriteria, session);

			List<Vl_thesauro_tit> result = this.basicCriteria.list();

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
    <statement nome="countPerTitolo" tipo="count" id="Jenny_02">
            <fisso>
            SELECT COUNT (*) FROM Vl_thesauro_tit
                WHERE bid= XXXbid
                AND fl_canc != 'S'
            </fisso>
    </statement>

    <filter name="VL_THESAURO_TIT_countPerTitolo"
            condition="bid = :XXXbid
                       AND fl_canc != 'S' "/>
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countPerTitolo(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_THESAURO_TIT_countPerTitolo");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXbid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXbid);

            this.createCriteria(myOpzioni);
            // NO FILTRI this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_thesauro_tit", this.basicCriteria, session);


            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList()
                      .add( Projections.rowCount())).uniqueResult();

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

    public  List<Vl_thesauro_tit> cercaTitColl(HashMap opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
    	HashMap myOpzioni = (HashMap) opzioni.clone();
        log.debug("Vl_thesauro_tit metodo countTitColl invocato");
        Session session = this.getSession();
        this.beginTransaction();
        List<Vl_thesauro_tit> result = null;


        //select count(*) from vl_soggetto_tit a
        //	where a.cid = 'CSWC000030' and a.bid in
        //	(select distinct(b.bid) from vl_soggetto_tit b
        //	  where b.cid = 'CSWC000030' )

        Query query = session.createQuery(
            	"from Vl_thesauro_tit as A " +
            	"where A.DID = :XXXdid and A.BID in " +
            	"(select distinct(B.BID) from Vl_thesauro_tit as B  " +
            	"where B.DID = :XXXdid_1 ) "
            	);
        query.setParameter("XXXdid",opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXdid));
        query.setParameter("XXXdid_1",opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXdid));
        myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_thesauro_titCommonDao.XXXdid);

        result = query.list();
        this.commitTransaction();
        this.closeSession();
        return result;

    }

    @Override
    public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_thesauro_tit.class;
	}

}
