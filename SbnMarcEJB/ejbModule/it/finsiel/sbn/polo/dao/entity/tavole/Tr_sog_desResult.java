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
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
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
public class Tr_sog_desResult  extends TableDao{

    private Tr_sog_des tr_sog_des;

	public Tr_sog_desResult(Tr_sog_des tr_sog_des) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_sog_des.leggiAllParametro());
        this.tr_sog_des = tr_sog_des;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  cid = XXXcid
				  AND did = XXXdid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_sog_des> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_SOG_DES_selectPerKey");

			filter.setParameter(Tr_sog_desResult.XXXcid, opzioni
					.get(Tr_sog_desResult.XXXcid));
			filter.setParameter(Tr_sog_desResult.XXXdid, opzioni
					.get(Tr_sog_desResult.XXXdid));

			myOpzioni.remove(Tr_sog_desResult.XXXcid);
			myOpzioni.remove(Tr_sog_desResult.XXXdid);


			List<Tr_sog_des> result = this.basicCriteria
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
	<statement nome="selectDescrittorePerSoggetto" tipo="select" id="Jenny_07">
			<fisso>
				WHERE
				  cid = XXXcid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_sog_des> selectDescrittorePerSoggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_SOG_DES_selectDescrittorePerSoggetto");

			filter.setParameter(Tr_sog_desResult.XXXcid, opzioni
					.get(Tr_sog_desResult.XXXcid));

			myOpzioni.remove(Tr_sog_desResult.XXXcid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_sog_des",
                    this.basicCriteria, session);

			List<Tr_sog_des> result = this.basicCriteria
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
	<statement nome="selectLegameDescrittore" tipo="select" id="Jenny_08">
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
	public List<Tr_sog_des> selectLegameDescrittore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_SOG_DES_selectLegameDescrittore");

			filter.setParameter(Tr_sog_desResult.XXXdid, opzioni
					.get(Tr_sog_desResult.XXXdid));

			myOpzioni.remove(Tr_sog_desResult.XXXdid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_sog_des",
                    this.basicCriteria, session);

			List<Tr_sog_des> result = this.basicCriteria
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
	<statement nome="countSoggettoPerDescrittore" tipo="count" id="Jenny_09">
			<fisso>
				SELECT COUNT (*) FROM Tr_sog_des
				WHERE
				  did = XXXdid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
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
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_SOG_DES_countSoggettoPerDescrittore");

			filter.setParameter(Tr_sog_desResult.XXXdid, opzioni
					.get(Tr_sog_desResult.XXXdid));

			myOpzioni.remove(Tr_sog_desResult.XXXdid);


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
	<statement nome="verificaEsistenzaId" tipo="select" id="10_Taymer">
			<fisso>
				WHERE
				  cid = XXXcid
				  AND did = XXXdid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List<Tr_sog_des>
	 * @throws InfrastructureException
	 */
	public List<Tr_sog_des> verificaEsistenzaId(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_SOG_DES_verificaEsistenzaId");

			filter.setParameter(Tr_sog_desResult.XXXcid, opzioni
					.get(Tr_sog_desResult.XXXcid));
			filter.setParameter(Tr_sog_desResult.XXXdid, opzioni
					.get(Tr_sog_desResult.XXXdid));

			myOpzioni.remove(Tr_sog_desResult.XXXcid);
			myOpzioni.remove(Tr_sog_desResult.XXXdid);


			List<Tr_sog_des> result = this.basicCriteria.list();
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
	 * 	<statement nome="update" tipo="update" id="04">
			<fisso>
				UPDATE Tr_sog_des
				 SET
				  fl_canc = XXXfl_canc ,
				  ute_var = XXXute_var ,
				  fl_primavoce = XXXfl_primavoce ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  cid = XXXcid
				  AND did = XXXdid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_sog_des metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_sog_des",Tr_sog_des.class);

		HashMap params = this.getParametro();
		buildUpdate.addProperty("ute_var",params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("fl_canc",params.get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_primavoce",params.get(KeyParameter.XXXfl_primavoce));

		//almaviva5_20120516 evolutive CFI
		buildUpdate.addProperty("fl_posizione", params.get(KeyParameter.XXXfl_posizione));


		buildUpdate.addWhere("cid",params.get(KeyParameter.XXXcid),"=");
		buildUpdate.addWhere("did",params.get(KeyParameter.XXXdid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}
	/*
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Tr_sog_des
                 (
                  fl_canc ,
                  ute_var ,
                  ute_ins ,
                  fl_primavoce ,
                  did ,
                  ts_var ,
                  ts_ins ,
                  cid
                 )
                VALUES
                 (
                  XXXfl_canc ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXfl_primavoce ,
                  XXXdid ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP ,
                  XXXcid
                 )
            </fisso>
    </statement>

	 */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_sog_des metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tr_sog_des.setTS_INS(ts);
        tr_sog_des.setTS_VAR(ts);
        session.saveOrUpdate(this.tr_sog_des);
        this.commitTransaction();
        this.closeSession();

	}

    /**
     * 	<statement nome="updateFl_canc" tipo="update" id="Jenny_06">
			<fisso>
				UPDATE Tr_sog_des
				 SET
				  ute_var = XXXute_var ,
				  fl_canc = XXXfl_canc ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  cid = XXXcid
				  AND did = XXXdid
				  AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateFl_canc(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_sog_des metodo updateFl_canc invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_sog_des",Tr_sog_des.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("cid",this.getParametro().get(KeyParameter.XXXcid),"=");
		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     * 	<statement nome="updatePrimaVoce" tipo="update" id="Jenny_05">
			<fisso>
				UPDATE Tr_sog_des
				 SET
				  ute_var = XXXute_var ,
				  fl_primavoce = XXXfl_primavoce ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  cid = XXXcid
				  AND did = XXXdid
				  AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updatePrimaVoce(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_sog_des metodo updatePrimaVoce invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_sog_des",Tr_sog_des.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("fl_primavoce",this.getParametro().get(KeyParameter.XXXfl_primavoce));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("cid",this.getParametro().get(KeyParameter.XXXcid),"=");
		buildUpdate.addWhere("did",this.getParametro().get(KeyParameter.XXXdid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }


//  almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
//  inserisco un chiamata con HQL
    // and 0 = 0 ORDER BY this_.CID
    // and 0 = 0 ORDER BY this_.KY_CLES1_S, this_.KY_CLES2_S, this_.CID
    public  List<Tr_sog_des> cercaDidMultpli(
    		String cid_1,
    		String cid_2,
    		String cid_3,
    		String cid_4,
    		int campi,
    		String tipoOrd) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_sog_des metodo cercaDidMultpli invocato");
        Session session = this.getSession();
        this.beginTransaction();
        List<Tr_sog_des> result = null;
        if(campi==2){

	        Query query = session.createQuery(
	            	"from Tr_sog_des as A " +
	            	"where " +
	            	"not A.FL_CANC='S' " +
	            	"and A.DID = :XXXcid_1 and A.CID in " +
	            	"(select B.CID from Tr_sog_des as B " +
	            	"where B.DID = :XXXcid_2 " +
	            	"and not B.FL_CANC='S' ) "
	            	);
	        query.setParameter("XXXcid_1",cid_1);
	        query.setParameter("XXXcid_2",cid_2);
	        result = query.list();
	        this.commitTransaction();
	        this.closeSession();
        }

        if(campi==3){
	        Query query = session.createQuery(
	            	"from Tr_sog_des as A " +
	            	"where A.DID = :XXXcid_1 " +
	            	"and not A.FL_CANC='S' and A.CID in " +
	            	"(select B.CID from Tr_sog_des as B " +
	            	"where B.DID = :XXXcid_2 " +
	            	"and not B.FL_CANC='S' and B.CID in " +
	            	"(select C.CID from Tr_sog_des as  C " +
	            	"where C.DID = :XXXcid_3 and not C.FL_CANC='S' ))");
	        query.setParameter("XXXcid_1",cid_1);
	        query.setParameter("XXXcid_2",cid_2);
	        query.setParameter("XXXcid_3",cid_3);
	        result = query.list();
	        this.commitTransaction();
	        this.closeSession();
        }
        if(campi==4){
	        Query query = session.createQuery(
	            	"from Tr_sog_des A " +
	            	"where A.DID = :XXXcid_1 " +
	            	"and not A.FL_CANC='S' " +
	            	"and A.CID in " +
	            	"(select B.CID from Tr_sog_des as B " +
	            	"where B.DID = :XXXcid_2 " +
	            	"and not B.FL_CANC='S' " +
	            	"and B.CID in " +
	            	"(select C.CID from Tr_sog_des as C " +
	            	"where C.DID = :XXXcid_3 " +
	            	"and not C.FL_CANC='S' " +
	            	"and C.CID in " +
	            	"(select D.CID from Tr_sog_des as D " +
	            	"where D.DID = :XXXcid_4 " +
	            	"and not D.FL_CANC='S' )))" );
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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_sog_des.class;
	}

}
