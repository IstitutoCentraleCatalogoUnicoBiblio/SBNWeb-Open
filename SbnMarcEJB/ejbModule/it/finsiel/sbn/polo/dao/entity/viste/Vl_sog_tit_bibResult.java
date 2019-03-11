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
import it.finsiel.sbn.polo.dao.vo.MaxLivelloLegameSog;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_sog_tit_bib;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
	public class Vl_sog_tit_bibResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao{

    public Vl_sog_tit_bibResult(Vl_sog_tit_bib vl_sog_tit_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_sog_tit_bib.leggiAllParametro());
    }

    public List cercaMaxLivelloAutLegameSoggettario(HashMap opzioni)
    throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();

			Query query = session.getNamedQuery("cercaMaxLivelloAutLegameSoggettario");
			query.setString("bid", (String) opzioni.get(XXXbid));
			query.setString("sogg", (String) opzioni.get(XXXcd_soggettario));
			query.setResultTransformer(Transformers.aliasToBean(MaxLivelloLegameSog.class));

			opzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXbid);
			opzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_soggettario);

			List result = query.list();

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
		<filter name="VL_SOG_TIT_BIB_verifica_esistenza"
				condition="bid = :XXXbid
							AND cid = :XXXcid
							AND cd_soggettario = :XXXcd_soggettario
							AND cd_polo = :XXXcd_polo
							AND cd_biblioteca = :XXXcd_biblioteca
				"/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_sog_tit_bib> verifica_esistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_SOG_TIT_BIB_verifica_esistenza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_polo,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_biblioteca,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_biblioteca));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_polo);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_biblioteca);

            // NO FILTRI this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_sog_tit_bib",
                    this.basicCriteria, session);

			List<Vl_sog_tit_bib> result = this.basicCriteria.list();
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
// 	<filter name="VL_SOG_TIT_BIB_count_tit_coll_bib"
//	condition="cid = :XXXcid
//		AND cd_polo = :XXXcd_polo
//		AND cd_biblioteca = :XXXcd_biblioteca "/>
//	/*
//	 * @param opzioni
//	 * @return Integer
//	 * @throws InfrastructureException
//	 */
    public Integer countTitCollBib(HashMap opzioni)
    throws InfrastructureException {
        try {
            Session session = this.getSession();
            this.beginTransaction();
            /*
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_SOG_TIT_BIB_count_tit_coll_bib");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_polo,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_polo));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_biblioteca,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_biblioteca));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_polo);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_sog_tit_bibCommonDao.XXXcd_biblioteca);
            // NO FILTRI this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_sog_tit_bib", this.basicCriteria, session);


            Integer result = (Integer) this.basicCriteria.setProjection(
                    Projections.projectionList()
                      .add( Projections.rowCount())).uniqueResult();

            */

            Query query = session.getNamedQuery("contaTitoliCollegatiSoggettoBib");
            query.setString("codPolo", (String) opzioni.get(XXXcd_polo));
            query.setString("codBib", (String) opzioni.get(XXXcd_biblioteca));
            query.setString("cid", (String) opzioni.get(XXXcid));

            opzioni.remove(XXXcd_polo);
            opzioni.remove(XXXcd_biblioteca);
            opzioni.remove(XXXcid);

            Number result = (Number) query.uniqueResult();

            this.commitTransaction();
            this.closeSession();
            return result.intValue();

        } catch (InfrastructureException ife) {
            throw ife;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InfrastructureException();
        }
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_sog_tit_bib.class;
	}

}
