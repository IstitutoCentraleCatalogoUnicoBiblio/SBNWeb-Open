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
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_tit_luo;
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
public class Tr_tit_luoResult  extends it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao{

    private Tr_tit_luo tr_tit_luo;

	public Tr_tit_luoResult(Tr_tit_luo tr_tit_luo) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_tit_luo.leggiAllParametro());
        this.tr_tit_luo = tr_tit_luo;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND lid = XXXlid
				  AND tp_luogo = XXXtp_luogo
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_luo> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_LUO_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXtp_luogo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXtp_luogo));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXtp_luogo);
			List<Tr_tit_luo> result = this.basicCriteria
					.list();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		} finally
		{
			this.commitTransaction();
			this.closeSession();

		}
	}

	/**
	<statement nome="selectPerBidELid" tipo="select" id="12_taymer">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND lid = XXXlid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_luo> selectPerBidELid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_LUO_selectPerBidELid");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_luo",
                    this.basicCriteria, session);

			List<Tr_tit_luo> result = this.basicCriteria
					.list();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		} finally
		{
			this.commitTransaction();
			this.closeSession();

		}
	}

	/**
	<statement nome="verificaEsistenza" tipo="select" id="11_taymer">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND lid = XXXlid
			</fisso>
			<opzionale dipende="XXXtp_luogo"> AND tp_luogo = XXXtp_luogo </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_luo> verificaEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_LUO_verificaEsistenza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid);

			this.createCriteria(myOpzioni);

			List<Tr_tit_luo> result = this.basicCriteria
					.list();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		} finally
		{
			this.commitTransaction();
			this.closeSession();

		}
	}

	/**
	<statement nome="selectPerTitolo" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
			<opzionale dipende="XXXtp_luogo"> AND tp_luogo = XXXtp_luogo </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_luo> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_LUO_selectPerTitolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXbid);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_luo",
                    this.basicCriteria, session);

			List<Tr_tit_luo> result = this.basicCriteria
					.list();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		} finally
		{
			this.commitTransaction();
			this.closeSession();

		}
	}

	/**
	<statement nome="selectPerLuogo" tipo="select" id="Dani_01">
			<fisso>
				WHERE
				  lid = XXXlid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_luo> selectPerLuogo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_LUO_selectPerLuogo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_tit_luoCommonDao.XXXlid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_luo",
                    this.basicCriteria, session);

			List<Tr_tit_luo> result = this.basicCriteria
					.list();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		} finally
		{
			this.commitTransaction();
			this.closeSession();

		}
	}

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
			<fisso>
				UPDATE Tr_tit_luo
				 SET
				  fl_canc = XXXfl_canc ,
				  ute_var = XXXute_var ,
				  nota_tit_luo = XXXnota_tit_luo ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND lid = XXXlid
				  AND tp_luogo = XXXtp_luogo
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_luo metodo update invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_luo",Tr_tit_luo.class);

		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("nota_tit_luo",this.getParametro().get(KeyParameter.XXXnota_tit_luo));
		buildUpdate.addProperty("ts_var",now());

		// Intervento almaviva2 06.09.02012 per Bug Mantis esercizio 5089
		// Inserita la valorizzazione del campo tipo che corrisponde al relator code
		buildUpdate.addProperty("tp_luogo",this.getParametro().get(KeyParameter.XXXtp_luogo));
		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("lid",this.getParametro().get(KeyParameter.XXXlid),"=");



		// Intervento almaviva2 06.09.02012 per Bug Mantis esercizio 5089
		// Eliminato il controllo di uguaglianza fra il relator code da inserire con quello da modificare
		// che di fatto inibiva la modifica
//		buildUpdate.addWhere("tp_luogo",this.getParametro().get(KeyParameter.XXXtp_luogo),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }
    /*
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Tr_tit_luo
                 (
                  fl_canc ,
                  ute_var ,
                  ute_ins ,
                  bid ,
                  tp_luogo ,
                  lid ,
                  nota_tit_luo ,
                  ts_var ,
                  ts_ins
                 )
                VALUES
                 (
                  XXXfl_canc ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXbid ,
                  XXXtp_luogo ,
                  XXXlid ,
                  XXXnota_tit_luo ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP
                 )
            </fisso>
    </statement>

     */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_tit_luo metodo insert invocato ");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_tit_luo.setTS_INS(now);
        tr_tit_luo.setTS_VAR(now);
        session.saveOrUpdate(this.tr_tit_luo);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="updateDisabilita" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tr_tit_luo
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid = XXXbid AND lid = XXXlid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void updateDisabilita(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_luo metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_luo",Tr_tit_luo.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("lid",this.getParametro().get(KeyParameter.XXXlid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	/**
	 * 	<statement nome="updateLegameLuogo" tipo="update" id="Jenny_08">
			<fisso>
				UPDATE Tr_tit_luo
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP,
				  nota_tit_luo = XXXnota_tit_luo ,
				  tp_luogo = XXXtp_luogo ,
				  lid = XXXidArrivo
				WHERE
				  bid = XXXbid
				  AND lid = XXXidPartenza
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateLegameLuogo(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_luo metodo updateLegameLuogo invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_luo",Tr_tit_luo.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addProperty("nota_tit_luo",this.getParametro().get(KeyParameter.XXXnota_tit_luo));
		buildUpdate.addProperty("tp_luogo",this.getParametro().get(KeyParameter.XXXtp_luogo));
		buildUpdate.addProperty("lid",this.getParametro().get(KeyParameter.XXXidArrivo));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("lid",this.getParametro().get(KeyParameter.XXXidPartenza),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	/**
	 * 	<statement nome="updateCancellaPerBid" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Tr_tit_luo
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
        log.debug("Tr_tit_luo metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_luo",Tr_tit_luo.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	/**
	 * 	<statement nome="updateCancellaLegameTitLuo" tipo="update" id="Jenny_10">
			<fisso>
				UPDATE Tr_tit_luo
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				AND lid = XXXlid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateCancellaLegameTitLuo(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_luo metodo updateCancellaLegameTitLuo invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_luo",Tr_tit_luo.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("lid",this.getParametro().get(KeyParameter.XXXlid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_tit_luo.class;
	}
}
