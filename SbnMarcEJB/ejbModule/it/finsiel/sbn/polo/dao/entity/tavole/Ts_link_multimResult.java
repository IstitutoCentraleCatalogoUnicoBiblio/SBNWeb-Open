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
import it.finsiel.sbn.polo.dao.common.tavole.Ts_link_multimCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Ts_link_multim;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * Test Eseguito con successo
 * @author Antonio
 *
 */
public class Ts_link_multimResult  extends it.finsiel.sbn.polo.dao.common.tavole.Ts_link_multimCommonDao{

    private Ts_link_multim ts_link_multim;
    public Ts_link_multimResult(Ts_link_multim ts_link_multim) throws InfrastructureException {
        super();
        this.valorizzaParametro(ts_link_multim.leggiAllParametro());
        this.ts_link_multim = ts_link_multim;

    }
	/**
    <statement nome="selectPerKey" tipo="select" id="01">
        <fisso>
            WHERE
              id_link_multim = XXXid_link_multim
              AND fl_canc != 'S'
        </fisso>
    </statement>
        <filter name="TS_LINK_MULTIM_selectPerKey"
            condition="id_link_multim = :XXXid_link_multim
                       AND fl_canc != 'S' "/>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ts_link_multim> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TS_LINK_MULTIM_selectPerKey");

			filter.setParameter(Ts_link_multimCommonDao.XXXid_link_multim, opzioni
					.get(Ts_link_multimCommonDao.XXXid_link_multim));

			myOpzioni.remove(Ts_link_multimCommonDao.XXXid_link_multim);

			this.createCriteria(myOpzioni);

			List<Ts_link_multim> result = this.basicCriteria
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
    <statement nome="selectPerKy" tipo="select" id="jenny_02">
        <fisso>
            WHERE
              ky_link_multim = XXXky_link_multim
              AND fl_canc != 'S'
        </fisso>
    </statement>

        <filter name="TS_LINK_MULTIM_selectPerKy"
            condition="ky_link_multim = :XXXky_link_multim
                       AND fl_canc != 'S'"/>

	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
    public List<Ts_link_multim> selectPerKy(HashMap opzioni)
        throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TS_LINK_MULTIM_selectPerKy");

            filter.setParameter(Ts_link_multimCommonDao.XXXky_link_multim, opzioni
                    .get(Ts_link_multimCommonDao.XXXky_link_multim));

            myOpzioni.remove(Ts_link_multimCommonDao.XXXky_link_multim);

            this.createCriteria(myOpzioni);

            List<Ts_link_multim> result = new ArrayList<Ts_link_multim>(this.basicCriteria.list());

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
     *  <statement nome="insert" tipo="insert" id="04">
        <fisso>
            INSERT INTO Ts_link_multim
             (
              fl_canc ,
              uri_multim ,
              ky_link_multim ,
              id_link_multim ,
              ute_ins,
              ute_var,
              ts_ins,
              ts_var,
              immagine
             )
            VALUES
             (
              XXXfl_canc ,
              XXXuri_multim ,
              XXXky_link_multim ,
              XXXid_link_multim ,
              XXXute_ins,
              XXXute_var,
              SYSTIMESTAMP ,
              SYSTIMESTAMP ,
              EMPTY_BLOB()
             )
        </fisso>
    </statement>

     */
    public void insert(Object opzioni) throws InfrastructureException
    {
        log.debug("Ts_link_multim metodo insert invocato ");
        Session session = this.getSession();

        this.beginTransaction();
        Timestamp now = now();
		ts_link_multim.setTS_INS(now);
        ts_link_multim.setTS_VAR(now);

        //session.saveOrUpdate(this.ts_link_multim);
        Long id = (Long) session.save(this.ts_link_multim);


// TEST

// TEST
        this.commitTransaction();
        this.closeSession();


    }

    /*
    <statement nome="cancellaPerKy" tipo="delete" id="09">
    <fisso>
        UPDATE Ts_link_multim
         SET
          ts_var = SYSTIMESTAMP ,
          ute_var = XXXute_var,
          fl_canc = 'S'
        WHERE
          ky_link_multim = XXXky_link_multim
        AND fl_canc != 'S'
    </fisso>
    </statement>
    */
    public void cancellaPerKy(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Ts_link_multim metodo cancellaPerKy invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Ts_link_multim",Ts_link_multim.class);

        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("fl_canc","S");


        buildUpdate.addWhere("ky_link_multim",this.getParametro().get(KeyParameter.XXXky_link_multim),"=");
        buildUpdate.addWhere("fl_canc","S","!=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();




    }
    /*
     *  <statement nome="cancellaLink" tipo="update" id="Jenny_07">
        <fisso>
            UPDATE Ts_link_multim
             SET
              ts_var = SYSTIMESTAMP ,
              ute_var = XXXute_var,
              fl_canc = XXXfl_canc
            WHERE
              id_link_multim = XXXid_link_multim
              AND fl_canc != 'S'
        </fisso>
    </statement>

     */
    public void cancellaLink(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Ts_link_multim metodo cancellaLink invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Ts_link_multim",Ts_link_multim.class);

        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("fl_canc","S");


        buildUpdate.addWhere("ky_link_multim",this.getParametro().get(KeyParameter.XXXky_link_multim),"=");
        buildUpdate.addWhere("fl_canc","S","!=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }
    /*
     *  <statement nome="deletePerKy" tipo="delete" id="08">
        <fisso>
            DELETE FROM Ts_link_multim
            WHERE
              ky_link_multim = XXXky_link_multim
            AND fl_canc != 'S'
        </fisso>
    </statement>

     */
    public void deletePerKy(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Ts_link_multim metodo deletePerKy invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Ts_link_multim",Ts_link_multim.class);

        buildUpdate.addWhere("ky_link_multim",this.getParametro().get(KeyParameter.XXXky_link_multim),"=");
        buildUpdate.addWhere("fl_canc","S","!=");

        int query = buildUpdate.executeDelete();
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Ts_link_multim.class;
	}

}
