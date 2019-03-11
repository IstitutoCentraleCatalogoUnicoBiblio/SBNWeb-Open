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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_aut_mar;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tr_aut_marResult  extends TableDao{

    private Tr_aut_mar tr_aut_mar;

	public Tr_aut_marResult(Tr_aut_mar tr_aut_mar) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_aut_mar.leggiAllParametro());
        this.tr_aut_mar = tr_aut_mar;
   }

    /**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  mid = XXXmid
				  AND vid = XXXvid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_mar> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_MAR_selectPerKey");

			filter.setParameter(Tr_aut_marResult.XXXmid, opzioni
					.get(Tr_aut_marResult.XXXmid));
			filter.setParameter(Tr_aut_marResult.XXXvid, opzioni
					.get(Tr_aut_marResult.XXXvid));

			myOpzioni.remove(Tr_aut_marResult.XXXmid);
			myOpzioni.remove(Tr_aut_marResult.XXXvid);


			List<Tr_aut_mar> result = this.basicCriteria
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
	<statement nome="selectAutoreMarca" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				  vid = XXXvid
				  AND fl_canc = XXXfl_canc
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_mar> selectAutoreMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_MAR_selectAutoreMarca");

			filter.setParameter(Tr_aut_marResult.XXXvid, opzioni
					.get(Tr_aut_marResult.XXXvid));
			filter.setParameter(Tr_aut_marResult.XXXfl_canc, opzioni
					.get(Tr_aut_marResult.XXXfl_canc));

			myOpzioni.remove(Tr_aut_marResult.XXXvid);
			myOpzioni.remove(Tr_aut_marResult.XXXfl_canc);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_mar",
                    this.basicCriteria, session);

			List<Tr_aut_mar> result = this.basicCriteria
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
	public List<Tr_aut_mar> selectAutoriMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TR_AUT_MAR_selectAutoriMarca");

			filter.setParameter(Tr_aut_marResult.XXXmid, opzioni
					.get(Tr_aut_marResult.XXXmid));
			filter.setParameter(Tr_aut_marResult.XXXfl_canc, opzioni
					.get(Tr_aut_marResult.XXXfl_canc));

			myOpzioni.remove(Tr_aut_marResult.XXXmid);
			myOpzioni.remove(Tr_aut_marResult.XXXfl_canc);

			this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
					"Tr_aut_mar", this.basicCriteria, session);

			List<Tr_aut_mar> result = this.basicCriteria
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
	<statement nome="selectEsistenza" tipo="select" id="09_Taymer">
			<fisso>
				WHERE
				  mid = XXXmid
				  AND vid = XXXvid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_mar> selectEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_MAR_selectEsistenza");

			filter.setParameter(Tr_aut_marResult.XXXvid, opzioni
					.get(Tr_aut_marResult.XXXvid));
			filter.setParameter(Tr_aut_marResult.XXXmid, opzioni
					.get(Tr_aut_marResult.XXXmid));

			myOpzioni.remove(Tr_aut_marResult.XXXvid);
			myOpzioni.remove(Tr_aut_marResult.XXXmid);


			List<Tr_aut_mar> result = this.basicCriteria
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
	<statement nome="selectPerMarca" tipo="select" id="10_Taymer">
			<fisso>
				WHERE
				  mid = XXXmid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_mar> selectPerMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_MAR_selectPerMarca");

			filter.setParameter(Tr_aut_marResult.XXXmid, opzioni
					.get(Tr_aut_marResult.XXXmid));

			myOpzioni.remove(Tr_aut_marResult.XXXmid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_mar",
                    this.basicCriteria, session);


			List<Tr_aut_mar> result = this.basicCriteria
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
	<statement nome="selectPerAutore" tipo="select" id="11_Taymer">
			<fisso>
				WHERE
				  vid = XXXvid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_mar> selectPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_MAR_selectPerAutore");

			filter.setParameter(Tr_aut_marResult.XXXvid, opzioni
					.get(Tr_aut_marResult.XXXvid));

			myOpzioni.remove(Tr_aut_marResult.XXXvid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_mar",
                    this.basicCriteria, session);

			List<Tr_aut_mar> result = this.basicCriteria
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
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Tr_aut_mar
                 (
                  fl_canc ,
                  vid ,
                  ute_var ,
                  ute_ins ,
                  mid ,
                  nota_aut_mar ,
                  ts_var ,
                  ts_ins
                 )
                VALUES
                 (
                  'N' ,
                  XXXvid ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXmid ,
                  XXXnota_aut_mar ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP
                 )
            </fisso>
    </statement>

	 */
	public boolean insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_aut_mar metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_aut_mar.setTS_INS(now);
        tr_aut_mar.setTS_VAR(now);
        session.save(this.tr_aut_mar);
        this.commitTransaction();
        this.closeSession();

		return true;
	}

	/**
	 * 	<statement nome="update" tipo="update" id="04">
			<fisso>
				UPDATE Tr_aut_mar
				 SET
				  ute_var = XXXute_var ,
				  nota_aut_mar = XXXnota_aut_mar ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = XXXfl_canc
				WHERE
				  mid = XXXmid
				  AND vid = XXXvid
			</fisso>
	</statement>

	 * @param opzioni
	 * @return
	 * @throws InfrastructureException
	 */
	public boolean update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_aut_mar metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_mar",Tr_aut_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("nota_aut_mar",this.getParametro().get(KeyParameter.XXXnota_aut_mar));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
		return true;
	}

    /**
     * 	<statement nome="updatePerModifica" tipo="update" id="Jenny_08">
			<fisso>
				UPDATE Tr_aut_mar
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = XXXfl_canc
				WHERE
				  mid = XXXmid
				  AND vid = XXXvid
				  AND
				  fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updatePerModifica(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_mar metodo updatePerModifica invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_mar",Tr_aut_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		//almaviva5_20040422 #2789
		//buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
		//almaviva5_20040422 #2789
		//buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancella" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tr_aut_mar
				 SET
				  fl_canc = 'S' ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  mid = XXXmid
				  AND vid = XXXvid
				  AND
				  fl_canc != 'S'
				  <!-- Togliere il commento quando Jenny avra' fatto la gestione dei timestamp -->
				  <!-- AND ts_var = XXXts_var -->
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_mar metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_mar",Tr_aut_mar.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
    /*
     *  <statement nome="cancellaLegamePerMid" tipo="update" id="12_Taymer">
            <fisso>
                UPDATE Tr_aut_mar
                 SET
                  fl_canc = 'S' ,
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP
                WHERE
                  mid = XXXmid
                  AND
                  fl_canc != 'S'
            </fisso>

     */
    public void cancellaLegamePerMid() throws InfrastructureException {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        log.debug("Tr_aut_mar metodo cancellaLegamePerMid invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_mar",Tr_aut_mar.class);

        buildUpdate.addProperty("fl_canc","S");
        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());

        buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
        buildUpdate.addWhere("fl_canc","S","!=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_aut_mar.class;
	}

}
