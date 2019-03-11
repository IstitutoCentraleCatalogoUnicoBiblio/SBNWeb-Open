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
import it.finsiel.sbn.polo.orm.Trs_termini_titoli_biblioteche;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class Trs_termini_titoli_bibliotecheResult  extends TableDao{

    private Trs_termini_titoli_biblioteche trs_termini_titoli_biblioteche;

	public Trs_termini_titoli_bibliotecheResult(Trs_termini_titoli_biblioteche trs_termini_titoli_biblioteche) throws InfrastructureException {
        super();
        this.valorizzaParametro(trs_termini_titoli_biblioteche.leggiAllParametro());
        this.trs_termini_titoli_biblioteche = trs_termini_titoli_biblioteche;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND did = XXXdid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Trs_termini_titoli_biblioteche> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TRS_TERMINI_TITOLI_BIBLIOTECHE_selectPerKey");

			filter.setParameter(Trs_termini_titoli_bibliotecheResult.XXXdid, opzioni
					.get(Trs_termini_titoli_bibliotecheResult.XXXdid));
			filter.setParameter(Trs_termini_titoli_bibliotecheResult.XXXbid, opzioni
					.get(Trs_termini_titoli_bibliotecheResult.XXXbid));

			myOpzioni.remove(Trs_termini_titoli_bibliotecheResult.XXXdid);
			myOpzioni.remove(Trs_termini_titoli_bibliotecheResult.XXXbid);


			List<Trs_termini_titoli_biblioteche> result = this.basicCriteria
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
	<statement nome="selectTitoloPerThesauro" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				  did = XXXdid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Trs_termini_titoli_biblioteche> selectTitoloPerThesauro(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TRS_TERMINI_TITOLI_BIBLIOTECHE_selectTitoloPerThesauro");

			filter.setParameter(Trs_termini_titoli_bibliotecheResult.XXXdid, opzioni
					.get(Trs_termini_titoli_bibliotecheResult.XXXdid));

			myOpzioni.remove(Trs_termini_titoli_bibliotecheResult.XXXdid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Trs_termini_titoli_biblioteche",
                    this.basicCriteria, session);

			List<Trs_termini_titoli_biblioteche> result = this.basicCriteria
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
	<statement nome="selectPerTitolo" tipo="select" id="Jenny_07">
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
	public List<Trs_termini_titoli_biblioteche> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TRS_TERMINI_TITOLI_BIBLIOTECHE_selectPerTitolo");

			filter.setParameter(Trs_termini_titoli_bibliotecheResult.XXXbid, opzioni
					.get(Trs_termini_titoli_bibliotecheResult.XXXbid));

			myOpzioni.remove(Trs_termini_titoli_bibliotecheResult.XXXbid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Trs_termini_titoli_biblioteche",
                    this.basicCriteria, session);

			List<Trs_termini_titoli_biblioteche> result = this.basicCriteria
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
	<statement nome="countFrequenzaPerBid" tipo="count" id="10_william">
		<fisso>
			SELECT COUNT(*)
			FROM   Trs_termini_titoli_biblioteche
			WHERE  fl_canc != 'S'
			and bid =XXXbid
		</fisso>
    </statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countFrequenzaPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TRS_TERMINI_TITOLI_BIBLIOTECHE_countFrequenzaPerBid");

			filter.setParameter(Trs_termini_titoli_bibliotecheResult.XXXbid, opzioni
					.get(Trs_termini_titoli_bibliotecheResult.XXXbid));

			myOpzioni.remove(Trs_termini_titoli_bibliotecheResult.XXXbid);

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

	/**
    <statement nome="countTitoloThesauro" tipo="count" id="11_william">
			<fisso>
			SELECT COUNT(*) FROM Trs_termini_titoli_biblioteche
				WHERE fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countTitoloThesauro(HashMap opzioni)
			throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TRS_TERMINI_TITOLI_BIBLIOTECHE_countTitoloThesauro");

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

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
			<fisso>
				UPDATE Trs_termini_titoli_biblioteche
				 SET
				  fl_canc = XXXfl_canc ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND did = XXXdid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub

        log.debug("Trs_termini_titoli_biblioteche metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Trs_termini_titoli_biblioteche",Trs_termini_titoli_biblioteche.class);


// almaviva ATTENZIONE CAMPI AGGINUTIVI ATTUALMENTE NON GESTITI VENGONO INSERITI ARBITRARIAMENTE
		//buildUpdate.addProperty("cd_biblioteca", this.getParametro().get(KeyParameter.XXXcd_biblioteca));
	    //buildUpdate.addProperty("cd_polo", this.getParametro().get(KeyParameter.XXXcd_polo));
		buildUpdate.addProperty("cd_the", this.getParametro().get(KeyParameter.XXXcd_the));
		buildUpdate.addProperty("nota_termine_titoli_biblioteca", this.getParametro().get(KeyParameter.XXXnota_termine_titoli_biblioteca));
//		 almaviva ATTENZIONE CAMPI AGGINUTIVI ATTUALMENTE NON GESTITI VENGONO INSERITI ARBITRARIAMENTE

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }
    /*
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Trs_termini_titoli_biblioteche
                 (
                  fl_canc ,
                  ute_var ,
                  ute_ins ,
                  bid ,
                  ts_var ,
                  ts_ins ,
                  did
                 )
                VALUES
                 (
                  XXXfl_canc ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXbid ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP ,
                  XXXdid
                 )
            </fisso>
    </statement>

     */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Trs_termini_titoli_biblioteche metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		trs_termini_titoli_biblioteche.setTS_INS(now);
        trs_termini_titoli_biblioteche.setTS_VAR(now);
        session.saveOrUpdate(this.trs_termini_titoli_biblioteche);
        this.commitTransaction();
        this.closeSession();

	}

    /**
     * 	<statement nome="updateDisabilita" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Trs_termini_titoli_biblioteche
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid = XXXbid AND did = XXXdid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
   public void updateDisabilita(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Trs_termini_titoli_biblioteche metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Trs_termini_titoli_biblioteche",Trs_termini_titoli_biblioteche.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateLegameThesauro" tipo="update" id="Jenny_08">
			<fisso>
				UPDATE Trs_termini_titoli_biblioteche
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP,
				  did = XXXidArrivo
				WHERE
				  bid = XXXbid
				  AND did = XXXidPartenza
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
   public void updateLegameThesauro(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Trs_termini_titoli_biblioteche metodo updateLegameThesauro invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Trs_termini_titoli_biblioteche",Trs_termini_titoli_biblioteche.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("did",this.getParametro().get(KeyParameter.XXXidArrivo));


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXidPartenza),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancellaPerBid" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Trs_termini_titoli_biblioteche
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
   public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Trs_termini_titoli_biblioteche metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Trs_termini_titoli_biblioteche",Trs_termini_titoli_biblioteche.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancellaLegameTitThe" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Trs_termini_titoli_biblioteche
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				AND did = XXXdid
				AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaLegameTitThe(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Trs_termini_titoli_biblioteche metodo updateCancellaLegameTitThe invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Trs_termini_titoli_biblioteche",Trs_termini_titoli_biblioteche.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Trs_termini_titoli_biblioteche.class;
	}
}
