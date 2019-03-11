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
import it.finsiel.sbn.polo.orm.viste.Vl_repertorio_mar;

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
public class Vl_repertorio_marResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao{

    public Vl_repertorio_marResult(Vl_repertorio_mar vl_repertorio_mar) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_repertorio_mar.leggiAllParametro());
    }

     /*
    <statement nome="selectPerMid" tipo="select" id="01">
        <fisso>
                WHERE
                    mid = XXXmid
                AND fl_canc != 'S'
            </fisso>
    </statement>
    <filter name="VL_REPERTORIO_MAR_selectPerMid"
            condition="mid = :XXXmid
                      AND fl_canc != 'S' "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_repertorio_mar> selectPerMid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vl_repertorio_mar.class);
			Filter filter = session.enableFilter("VL_REPERTORIO_MAR_selectPerMid");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_repertorio_mar",
                    this.basicCriteria, session);

			List<Vl_repertorio_mar> result = this.basicCriteria.list();
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
    <statement nome="selectPerRepertorio" tipo="select" id="01">
        <fisso>
                WHERE
                    mid = XXXmid AND
                    cd_sig_repertorio = XXXcd_sig_repertorio AND
                    progr_repertorio = XXXprogr_repertorio
                    AND fl_canc != 'S'
            </fisso>

    <filter name="VL_REPERTORIO_MAR_selectPerRepertorio"
            condition="mid = :XXXmid AND
                       cd_sig_repertorio = :XXXcd_sig_repertorio AND
                       progr_repertorio = :XXXprogr_repertorio
                       AND fl_canc != 'S' "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_repertorio_mar> selectPerRepertorio(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_repertorio_mar.class);
            Filter filter = session.enableFilter("VL_REPERTORIO_MAR_selectPerRepertorio");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid,
                   opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_repertorio_mar",
                   this.basicCriteria, session);

            List<Vl_repertorio_mar> result = this.basicCriteria.list();
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
    <statement nome="selectPerRepertorioCitazione" tipo="select" id="01">
        <fisso>
                WHERE
                    cd_sig_repertorio = XXXcd_sig_repertorio AND
                    progr_repertorio = XXXprogr_repertorio
                    AND fl_canc != 'S'
            </fisso>
    </statement>

    <filter name="VL_REPERTORIO_MAR_selectPerRepertorioCitazione"
            condition="cd_sig_repertorio = :XXXcd_sig_repertorio AND
                       progr_repertorio = :XXXprogr_repertorio
                       AND fl_canc != 'S' "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_repertorio_mar> selectPerRepertorioCitazione(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_repertorio_mar.class);
            Filter filter = session.enableFilter("VL_REPERTORIO_MAR_selectPerRepertorioCitazione");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_repertorio_mar",
                   this.basicCriteria, session);

            List<Vl_repertorio_mar> result = this.basicCriteria.list();
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
    <statement nome="countRepertorioPerCitazione" tipo="count" id="01">
        <fisso>
            SELECT COUNT (*) FROM vl_repertorio_mar
                WHERE
                  cd_sig_repertorio = XXXcd_sig_repertorio
                  AND progr_repertorio = XXXprogr_repertorio
                AND fl_canc != 'S'
            </fisso>
    <filter name="VL_REPERTORIO_MAR_countRepertorioPerCitazione"
            condition="cd_sig_repertorio = XXXcd_sig_repertorio
                       AND progr_repertorio = XXXprogr_repertorio
                       AND fl_canc != 'S' "/>

	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
    public Integer countRepertorioPerCitazione(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_repertorio_mar.class);
            Filter filter = session.enableFilter("VL_REPERTORIO_MAR_countRepertorioPerCitazione");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXprogr_repertorio);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_repertorio_mar", this.basicCriteria, session);


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


    /*
    <statement nome="contaCitazioniInRepertorio" tipo="count" id="01">
        <fisso>
                SELECT COUNT (*) FROM vl_repertorio_mar
                WHERE
                  cd_sig_repertorio = XXXcd_sig_repertorio
                  AND mid = XXXmid
                AND fl_canc != 'S'
            </fisso>
    </statement>
    <filter name="VL_REPERTORIO_MAR_contaCitazioniInRepertorio"
            condition="cd_sig_repertorio = :XXXcd_sig_repertorio
                       AND mid = :XXXmid
                       AND fl_canc != 'S' "/>
     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer contaCitazioniInRepertorio(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(Vl_repertorio_mar.class);
            Filter filter = session.enableFilter("VL_REPERTORIO_MAR_contaCitazioniInRepertorio");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXcd_sig_repertorio);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_repertorio_marCommonDao.XXXmid);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_repertorio_mar", this.basicCriteria, session);


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


}
