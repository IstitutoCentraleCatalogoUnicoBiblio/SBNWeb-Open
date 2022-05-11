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
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Vl_titolo_tit_bResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao{

    public Vl_titolo_tit_bResult(Vl_titolo_tit_b vl_titolo_tit_b) throws InfrastructureException {
        super();
        this.valorizzaParametro(vl_titolo_tit_b.leggiAllParametro());
    }

     /*
    <statement nome="selectTitoloPerLegame" tipo="select" id="02">
            <fisso>
                WHERE
                bid_base = XXXbid_base
            </fisso>
    </statement>
    <filter name="VL_TITOLO_TIT_B_selectTitoloPerLegame"
                condition="bid_base = :XXXbid_base "/>

	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vl_titolo_tit_b> selectTitoloPerLegame(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectTitoloPerLegame");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

			List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectDocumentoPerLegame" tipo="select" id="02Bis_taymer">
            <fisso>
                WHERE
                bid_base = XXXbid_base AND
                cd_natura != 'A'
            </fisso>
    </statement>
    <filter name="VL_TITOLO_TIT_B_selectDocumentoPerLegame"
                condition="bid_base = :XXXbid_base AND  cd_natura != 'A' "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectDocumentoPerLegame(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectDocumentoPerLegame");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectTitoloPerTitoloAccesso" tipo="select" id="03">
    <!-- Questa select serve per la selezione di titoli legati (FORSE E' UGUALE A QUELLA DOPO) -->
            <fisso>
                WHERE
                bid_base = XXXbid_base
                AND (
                cd_natura = 'D'
                OR cd_natura = 'B'
                OR cd_natura = 'P'
                OR cd_natura = 'T' )
            </fisso>
    </statement>
    </statement>
        <filter name="VL_TITOLO_TIT_B_selectTitoloPerTitoloAccesso"
                condition="bid_base = :XXXbid_base
                            AND (
                            cd_natura = 'D'
                            OR cd_natura = 'B'
                            OR cd_natura = 'P'
                            OR cd_natura = 'T' ) "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectTitoloPerTitoloAccesso(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectTitoloPerTitoloAccesso");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectPerTitAccesso" tipo="select" id="04">
    <!-- Questa select serve per la ricerca di titoli partendo da titolo di accesso -->
            <fisso>
                WHERE
                bid_base = XXXbid_base
                AND (
                cd_natura = 'D'
                OR cd_natura = 'B'
                OR cd_natura = 'P'
                OR cd_natura = 'T' )
            </fisso>
    </statement>
    </statement>
    </statement>
        <filter name="VL_TITOLO_TIT_B_selectPerTitAccesso"
                condition="bid_base = :XXXbid_base
                            AND (
                            cd_natura = 'D'
                            OR cd_natura = 'B'
                            OR cd_natura = 'P'
                            OR cd_natura = 'T' ) "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectPerTitAccesso(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectPerTitAccesso");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectPerTitUniforme" tipo="select" id="05">
            <fisso>
                WHERE
                bid_base = XXXbid_base
                AND (
                cd_natura = 'D'
                OR cd_natura = 'B'
                OR cd_natura = 'P'
                OR cd_natura = 'T' )
            </fisso>
    </statement>
        <filter name="VL_TITOLO_TIT_B_selectPerTitUniforme"
                condition="bid_base = :XXXbid_base
                            AND (
                            cd_natura = 'D'
                            OR cd_natura = 'B'
                            OR cd_natura = 'P'
                            OR cd_natura = 'T' ) "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectPerTitUniforme(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectPerTitUniforme");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectPerDocumento" tipo="select" id="06">
            <fisso>
                WHERE
                bid_base = XXXbid_base
                AND cd_natura IN( 'D' , 'B' , 'P' , 'T' )
            </fisso>
    </statement>
    </statement>
        <filter name="VL_TITOLO_TIT_B_selectPerDocumento"
                condition="bid_base = :XXXbid_base
                            AND cd_natura IN( 'D' , 'B' , 'P' , 'T' ) "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectPerDocumento(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectPerDocumento");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="countPerTitAccesso" tipo="count" id="07">
            <fisso>
                SELECT COUNT(*) FROM Vl_titolo_tit_b
                WHERE
                bid_base = XXXbid_base
                AND cd_natura IN( 'D' , 'B' , 'P' , 'T' )
            </fisso>
    </statement>
             <filter name="VL_TITOLO_TIT_B_countPerTitAccesso"
                condition="bid_base = :XXXbid_base
                            AND cd_natura IN( 'D' , 'B' , 'P' , 'T' ) "/>

     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerTitAccesso(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_countPerTitAccesso");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b", this.basicCriteria, session);


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
    <statement nome="countPerTitUniforme" tipo="count" id="08">
            <fisso>
                SELECT COUNT(*) FROM Vl_titolo_tit_b
                WHERE
                bid_base = XXXbid_base
                AND cd_natura IN( 'D' , 'B' , 'P' , 'T' )
            </fisso>
    </statement>
        <filter name="VL_TITOLO_TIT_B_countPerTitUniforme"
                condition="bid_base = :XXXbid_base
                            AND cd_natura IN( 'D' , 'B' , 'P' , 'T' ) "/>

     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerTitUniforme(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_countPerTitUniforme");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b", this.basicCriteria, session);


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
    <statement nome="countPerDocumento" tipo="count" id="09">
            <fisso>
                SELECT COUNT(*) FROM Vl_titolo_tit_b
                WHERE
                bid_base = XXXbid_base
                AND cd_natura IN( 'D' , 'B' , 'P' , 'T' )
            </fisso>
    </statement>
        <filter name="VL_TITOLO_TIT_B_countPerDocumento"
                condition="bid_base = :XXXbid_base
                            AND cd_natura IN( 'D' , 'B' , 'P' , 'T' ) "/>

     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerDocumento(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_countPerDocumento");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b", this.basicCriteria, session);


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
    <statement nome="selectLegameSuperiore" tipo="select" id="1_barbara">
            <fisso>
                WHERE bid_base = XXXbid_base
                AND fl_canc !='S'
                AND tp_legame = '01'
                AND cd_natura_coll != 'C'
            </fisso>
            </statement>
        <filter name="VL_TITOLO_TIT_B_selectLegameSuperiore"
                condition="bid_base = :XXXbid_base
                            AND fl_canc !='S'
                            AND tp_legame = '01'
                            AND cd_natura_coll != 'C' "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectLegameSuperiore(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectLegameSuperiore");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectMonografiaSuperiore" tipo="select" id="12_taymer">
            <fisso>
                WHERE bid_base = XXXbid_base
                AND fl_canc !='S'
                AND tp_legame = '01'
                AND cd_natura_coll IN ('M','S')
            </fisso>
        <filter name="VL_TITOLO_TIT_B_selectMonografiaSuperiore"
                condition="bid_base = :XXXbid_base
                            AND fl_canc !='S'
                            AND tp_legame = '01'
                            AND cd_natura_coll IN ('M','S') "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectMonografiaSuperiore(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectMonografiaSuperiore");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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

    // Inizio Modifica almaviva2 BUG MANTIS 4501 esercizio : inserita nuova select VL_TITOLO_TIT_B_selectMonografiaSuperioreBis
	// per consentire doppio legame M01S
	public List<Vl_titolo_tit_b> selectMonografiaSuperioreBis(HashMap opzioni)
	    throws InfrastructureException {
		try {
		    HashMap myOpzioni = (HashMap) opzioni.clone();
		    Session session = this.getSession();
		    this.beginTransaction();
		    this.basicCriteria = session.createCriteria(getTarget());
		    Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectMonografiaSuperioreBis");
//		    Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectMonografiaSuperiore");

		    filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
		            opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

		    myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

		    this.createCriteria(myOpzioni);
		    this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
		            "Vl_titolo_tit_b",
		            this.basicCriteria, session);

		    List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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

	private static final String[] NATURE_SUP = new String[] {"M", "C", "S"};
	public List<Vl_titolo_tit_b> cercaTitoloSuperiore(String bid)
		throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.basicCriteria.add(Restrictions.ne("FL_CANC", "S"));
			this.basicCriteria.add(Restrictions.eq("BID_BASE", bid));
			this.basicCriteria.add(Restrictions.eq("TP_LEGAME", "01"));
			this.basicCriteria.add(Restrictions.in("CD_NATURA_COLL", NATURE_SUP));

			List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectCollane" tipo="select" id="13_marco">
        <fisso>
            WHERE fl_canc != 'S'
            AND   bid_base = XXXbid_base
            AND   tp_legame = '01'
            AND   cd_natura_coll  = 'C'
        </fisso>
        <filter name="VL_TITOLO_TIT_B_selectCollane"
                condition="fl_canc != 'S'
                            AND   bid_base = :XXXbid_base
                            AND   tp_legame = '01'
                            AND   cd_natura_coll  = 'C' "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectCollane(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectCollane");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectRelTitUniMus" tipo="select" id="16_taymer">
        <fisso>
            WHERE  fl_canc != 'S'
              AND  bid_coll = XXXbid_coll
              AND tp_legame = '09'
              AND cd_natura_coll = 'A'
              AND tp_materiale = 'U'
        </fisso>
    </statement>
        <filter name="VL_TITOLO_TIT_B_selectRelTitUniMus"
                condition="fl_canc != 'S'
                            AND  bid_coll = :XXXbid_coll
                            AND tp_legame = '09'
                            AND cd_natura_coll = 'A'
                            AND tp_materiale = 'U' "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectRelTitUniMus(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectRelTitUniMus");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid_base);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="selectPerDocumentoPerTpLegame" tipo="select" id="17_taymer">
            <fisso>
                WHERE
                fl_canc != 'S'
                AND
                bid = XXXbid
                AND tp_legame_musica = XXXtp_legame_musica
            </fisso>
    </statement>
        <filter name="VL_TITOLO_TIT_B_selectPerDocumentoPerTpLegame"
                condition="fl_canc != 'S'
                            AND bid = :XXXbid
                            AND tp_legame_musica = :XXXtp_legame_musica "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectPerDocumentoPerTpLegame(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectPerDocumentoPerTpLegame");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="countPerDocumentoPerTpLegame" tipo="count" id="18_taymer">
            <fisso>
            SELECT COUNT(*) FROM Vl_titolo_tit_bCommonDao
                WHERE
                fl_canc != 'S'
                AND
                bid = XXXbid
                AND tp_legame_musica = XXXtp_legame_musica
            </fisso>
        <filter name="VL_TITOLO_TIT_B_countPerDocumentoPerTpLegame"
                condition=" fl_canc != 'S'
                            AND bid = XXXbid
                            AND tp_legame_musica = XXXtp_legame_musica "/>


     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerDocumentoPerTpLegame(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_countPerDocumentoPerTpLegame");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXbid);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b", this.basicCriteria, session);


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
    <statement nome="selectPerNomeLikePerTpLegame" tipo="select" id="19_taymer">
            <fisso>
                WHERE
                fl_canc !='S'
                AND tp_legame_musica = XXXtp_legame_musica
                AND
                (
                    ( ky_cles1_t  LIKE XXXstringaLike1||'%'
            </fisso>
    </statement>
        <filter name="VL_TITOLO_TIT_B_selectPerNomeLikePerTpLegame"
                condition="fl_canc !='S'
                            AND tp_legame_musica = :XXXtp_legame_musica
                            AND
                            (
                                ( ky_cles1_t  LIKE :XXXstringaLike1||'%' "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectPerNomeLikePerTpLegame(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectPerNomeLikePerTpLegame");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));
            //filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaLike1,
            //        opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaLike1));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);
            //myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaLike1);
            this.kycleslike=false;
            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="countPerNomeLikePerTpLegame" tipo="count" id="20_taymer">
            <fisso>
                SELECT COUNT (*) FROM vl_titolo_tit_b
                WHERE
                fl_canc !='S'
                AND tp_legame_musica = XXXtp_legame_musica
                AND
                (
                    ( ky_cles1_t  LIKE XXXstringaLike1||'%'
            </fisso>
        <filter name="VL_TITOLO_TIT_B_countPerNomeLikePerTpLegame"
                condition="fl_canc !='S'
                            AND tp_legame_musica = :XXXtp_legame_musica
                            AND
                            (
                                ( ky_cles1_t  LIKE :XXXstringaLike1||'%' "/>

     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerNomeLikePerTpLegame(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_countPerNomeLikePerTpLegame");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));
            //filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaLike1,
            //        opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaLike1));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);
            //myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaLike1);
            this.kycleslike=false;

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b", this.basicCriteria, session);


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
    <statement nome="selectPerNomeEsattoPerTpLegame" tipo="select" id="21_taymer">
            <fisso>
                WHERE
                fl_canc !='S'
                AND tp_legame_musica = XXXtp_legame_musica
                AND
                (
                    ( ky_cles1_t = XXXstringaEsatta1
                     AND ky_cles2_t = XXXstringaEsatta2
                   )
                   OR
                   ( ky_cles1_ct  = XXXstringaEsatta1
                   AND ky_cles2_ct = XXXstringaEsatta2
                    )
                 )
            </fisso>
        <filter name="VL_TITOLO_TIT_B_selectPerNomeEsattoPerTpLegame"
                condition="fl_canc !='S'
                            AND tp_legame_musica = :XXXtp_legame_musica
                            AND
                            (
                                ( ky_cles1_t = :XXXstringaEsatta1
                                 AND ky_cles2_t = :XXXstringaEsatta2
                               )
                               OR
                               ( ky_cles1_ct  = :XXXstringaEsatta1
                               AND ky_cles2_ct = :XXXstringaEsatta2
                                )
                             ) "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectPerNomeEsattoPerTpLegame(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectPerNomeEsattoPerTpLegame");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta1));

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta2,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta2));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta1);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta2);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="countPerNomeEsattoPerTpLegame" tipo="count" id="22_taymer">
            <fisso>
                SELECT COUNT (*) FROM vl_titolo_tit_b
                WHERE
                fl_canc !='S'
                AND tp_legame_musica = XXXtp_legame_musica
                AND
                (
                    ( ky_cles1_t = XXXstringaEsatta1
                     AND ky_cles2_t = XXXstringaEsatta2
                   )
                   OR
                   ( ky_cles1_ct  = XXXstringaEsatta1
                   AND ky_cles2_ct = XXXstringaEsatta2
                    )
                 )
            </fisso>
        <filter name="VL_TITOLO_TIT_B_countPerNomeEsattoPerTpLegame"
                condition="fl_canc !='S'
                            AND tp_legame_musica = :XXXtp_legame_musica
                            AND
                            (
                                ( ky_cles1_t = :XXXstringaEsatta1
                                 AND ky_cles2_t = :XXXstringaEsatta2
                               )
                               OR
                               ( ky_cles1_ct  = :XXXstringaEsatta1
                               AND ky_cles2_ct = :XXXstringaEsatta2
                                )
                             ) "/>

     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerNomeEsattoPerTpLegame(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_countPerNomeEsattoPerTpLegame");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta1));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta2,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta2));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta1);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaEsatta2);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b", this.basicCriteria, session);


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
    <statement nome="selectPerCletPerTpLegame" tipo="select" id="23_taymer">
            <fisso>
                WHERE
                fl_canc !='S'
                AND tp_legame_musica = XXXtp_legame_musica
                AND
                    ky_cles1_t LIKE XXXstringaClet1 || '%'
                    AND ky_clet2_t  = XXXstringaClet2
            </fisso>
        <filter name="VL_TITOLO_TIT_B_selectPerCletPerTpLegame"
                condition="fl_canc !='S'
                            AND tp_legame_musica = :XXXtp_legame_musica
                            AND
                                ky_cles1_t LIKE :XXXstringaClet1 || '%'
                                AND ky_clet2_t  = :XXXstringaClet2 "/>
     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_titolo_tit_b> selectPerCletPerTpLegame(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_selectPerCletPerTpLegame");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet1));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet2,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet2));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet1);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet2);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b",
                    this.basicCriteria, session);

            List<Vl_titolo_tit_b> result = this.basicCriteria.list();
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
    <statement nome="countPerCletPerTpLegame" tipo="count" id="24_taymer">
            <fisso>
                SELECT COUNT (*) FROM vl_titolo_tit_b
                WHERE
                fl_canc !='S'
                AND tp_legame_musica = XXXtp_legame_musica
                AND
                    ky_cles1_t LIKE XXXstringaClet1 || '%'
                    AND ky_clet2_t  = XXXstringaClet2
            </fisso>
        <filter name="VL_TITOLO_TIT_B_countPerCletPerTpLegame"
                condition="fl_canc !='S'
                            AND tp_legame_musica = :XXXtp_legame_musica
                            AND
                                ky_cles1_t LIKE :XXXstringaClet1 || '%'
                                AND ky_clet2_t  = :XXXstringaClet2 "/>

     * @param opzioni
     * @return Integer
     * @throws InfrastructureException
     */
    public Integer countPerCletPerTpLegame(HashMap opzioni)
    throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_TITOLO_TIT_B_countPerCletPerTpLegame");


            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet1,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet1));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet2,
                    opzioni.get(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet2));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXtp_legame_musica);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet1);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vl_titolo_tit_bCommonDao.XXXstringaClet2);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_titolo_tit_b", this.basicCriteria, session);


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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_titolo_tit_b.class;
	}

}
