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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_soggettoCommonDao;
import it.finsiel.sbn.polo.dao.common.viste.Vl_soggetto_desCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_des;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_soggetto_desResult extends Vl_soggetto_desCommonDao{

    public Vl_soggetto_desResult(Vl_soggetto_des vl_soggetto_des) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_soggetto_des.leggiAllParametro());
    }
     /*
    <statement nome="selectLegameDescrittore" tipo="select" id="Jenny_03">
        <fisso>
            WHERE
            did = XXXdid
            AND fl_canc != 'S'
        </fisso>
    </statement>
    <filter name="VL_SOGGETTO_DES_selectLegameDescrittore"
            condition="did = :XXXdid
                       AND fl_canc != 'S' "/>
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_soggetto_des> selectLegameDescrittore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_soggetto_des.class);

			String[] dd = (String[]) myOpzioni.get(Tb_soggettoCommonDao.XXXdid_IN);
			if (!ValidazioneDati.isFilled(dd)) {
				Filter filter = session.enableFilter("VL_SOGGETTO_DES_selectLegameDescrittore");
				filter.setParameter(Vl_soggetto_desCommonDao.XXXdid, opzioni.get(Vl_soggetto_desCommonDao.XXXdid));
				myOpzioni.remove(Vl_soggetto_desCommonDao.XXXdid);

			} else {
				this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
				this.basicCriteria.add(Restrictions.in("DID", dd));
				myOpzioni.remove(Vl_soggetto_desCommonDao.XXXdid_IN);
			}


            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_soggetto_des",
                    this.basicCriteria, session);

			List<Vl_soggetto_des> result = this.basicCriteria.list();
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
    <statement nome="countSoggettoPerDescrittore" tipo="count" id="Jenny_04">
            <fisso>
                SELECT COUNT (*) FROM Vl_soggetto_des
                WHERE
                  did = XXXdid
                  AND fl_canc != 'S'
            </fisso>
    </statement>

    <filter name="VL_SOGGETTO_DES_countSoggettoPerDescrittore"
            condition="did = :XXXdid
                       AND fl_canc != 'S' "/>

	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countSoggettoPerDescrittore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_soggetto_des.class);

			String[] dd = (String[]) myOpzioni.get(Tb_soggettoCommonDao.XXXdid_IN);
			if (!ValidazioneDati.isFilled(dd)) {
				Filter filter = session.enableFilter("VL_SOGGETTO_DES_countSoggettoPerDescrittore");
				filter.setParameter(Vl_soggetto_desCommonDao.XXXdid, opzioni.get(Vl_soggetto_desCommonDao.XXXdid));
				myOpzioni.remove(Vl_soggetto_desCommonDao.XXXdid);

			} else {
				this.basicCriteria.add(Restrictions.ne("FL_CANC", 'S'));
				this.basicCriteria.add(Restrictions.in("DID", dd));
				myOpzioni.remove(Vl_soggetto_desCommonDao.XXXdid_IN);
			}

			this.createCriteria(myOpzioni);
			this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
					"Vl_soggetto_des", this.basicCriteria, session);

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

//  almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
//  inserisco un chiamata con HQL
    // ORDER BY CID 										order_1 (cid)
    // ORDER BY KY_CLES1_S, KY_CLES2_S, CID 				order_2 (soggetto)
    public  List<Vl_soggetto_des> cercaDidMultpli(
    		String cid_1,
    		String cid_2,
    		String cid_3,
    		String cid_4,
    		int campi,
    		String tipoOrd) throws InfrastructureException {
        // TODO Auto-generated method stub
    	String ordinamento= "";
    	if((tipoOrd != null) && (tipoOrd.equals("order_1") ))
    		ordinamento = "ORDER BY CID";
    	else
    		ordinamento = "ORDER BY KY_CLES1_S, KY_CLES2_S, CID";

        log.debug("Vl_soggetto_des metodo cercaDidMultpli invocato");
        Session session = this.getSession();
        this.beginTransaction();
        List<Vl_soggetto_des> result = null;
        if(campi==2){

	        Query query = session.createQuery(
	            	"from Vl_soggetto_des as A " +
	            	"where A.DID = :XXXcid_1 and A.CID in " +
	            	"(select B.CID from Vl_soggetto_des as B " +
	            	"where B.DID = :XXXcid_2 ) " + ordinamento
	            	);
	        query.setParameter("XXXcid_1",cid_1);
	        query.setParameter("XXXcid_2",cid_2);
	        result = query.list();
	        this.commitTransaction();
	        this.closeSession();
        }

        if(campi==3){
	        Query query = session.createQuery(
	            	"from Vl_soggetto_des as A " +
	            	"where A.DID = :XXXcid_1 and A.CID in " +
	            	"(select B.CID from Vl_soggetto_des as B " +
	            	"where B.DID = :XXXcid_2 " +
	            	"and B.CID in " +
	            	"(select C.CID from Vl_soggetto_des as  C " +
	            	"where C.DID = :XXXcid_3 ))" + ordinamento );
	        query.setParameter("XXXcid_1",cid_1);
	        query.setParameter("XXXcid_2",cid_2);
	        query.setParameter("XXXcid_3",cid_3);
	        result = query.list();
	        this.commitTransaction();
	        this.closeSession();
        }
        if(campi==4){
	        Query query = session.createQuery(
	            	"from Vl_soggetto_des A " +
	            	"where A.DID = :XXXcid_1 and A.CID in " +
	            	"(select B.CID from Vl_soggetto_des as B " +
	            	"where B.DID = :XXXcid_2 " +
	            	"and B.CID in " +
	            	"(select C.CID from Vl_soggetto_des as C " +
	            	"where C.DID = :XXXcid_3 " +
	            	"and C.CID in " +
	            	"(select D.CID from Vl_soggetto_des as D " +
	            	"where D.DID = :XXXcid_4 )))" + ordinamento );
	        query.setParameter("XXXcid_1",cid_1);
	        query.setParameter("XXXcid_2",cid_2);
	        query.setParameter("XXXcid_3",cid_3);
	        query.setParameter("XXXcid_4",cid_4);
	        result = query.list();
	        this.commitTransaction();
	        this.closeSession();
        }


        return result;




    }

}
