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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_improntaCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_impronta;
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
public class Tb_improntaResult extends Tb_improntaCommonDao {

    private Tb_impronta tb_impronta;

    public Tb_improntaResult(Tb_impronta tb_impronta) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_impronta.leggiAllParametro());
        this.tb_impronta = tb_impronta;
    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01"> <fisso> WHERE bid =
	 * XXXbid AND fl_canc != 'S' AND impronta_1 = XXXimpronta_1 AND impronta_2 =
	 * XXXimpronta_2 AND impronta_3 = XXXimpronta_3 </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_impronta> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_IMPRONTA_selectPerKey");

			filter.setParameter(Tb_improntaCommonDao.XXXbid, opzioni
					.get(Tb_improntaCommonDao.XXXbid));
			filter.setParameter(Tb_improntaCommonDao.XXXimpronta_1, opzioni
					.get(Tb_improntaCommonDao.XXXimpronta_1));
			filter.setParameter(Tb_improntaCommonDao.XXXimpronta_2, opzioni
					.get(Tb_improntaCommonDao.XXXimpronta_2));
			filter.setParameter(Tb_improntaCommonDao.XXXimpronta_3, opzioni
					.get(Tb_improntaCommonDao.XXXimpronta_3));

			myOpzioni.remove(Tb_improntaCommonDao.XXXbid);
			myOpzioni.remove(Tb_improntaCommonDao.XXXimpronta_1);
			myOpzioni.remove(Tb_improntaCommonDao.XXXimpronta_2);
			myOpzioni.remove(Tb_improntaCommonDao.XXXimpronta_3);
			List<Tb_impronta> result = this.basicCriteria
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
	 * <statement nome="verificaEsistenza" tipo="select" id="7_taymer"> <fisso>
	 * WHERE bid = XXXbid AND impronta_1 = XXXimpronta_1 AND impronta_2 =
	 * XXXimpronta_2 AND impronta_3 = XXXimpronta_3 </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_impronta> verificaEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_IMPRONTA_verificaEsistenza");

			filter.setParameter(Tb_improntaCommonDao.XXXbid, opzioni
					.get(Tb_improntaCommonDao.XXXbid));
			filter.setParameter(Tb_improntaCommonDao.XXXimpronta_1, opzioni
					.get(Tb_improntaCommonDao.XXXimpronta_1));
			filter.setParameter(Tb_improntaCommonDao.XXXimpronta_2, opzioni
					.get(Tb_improntaCommonDao.XXXimpronta_2));
			filter.setParameter(Tb_improntaCommonDao.XXXimpronta_3, opzioni
					.get(Tb_improntaCommonDao.XXXimpronta_3));

			myOpzioni.remove(Tb_improntaCommonDao.XXXbid);
			myOpzioni.remove(Tb_improntaCommonDao.XXXimpronta_1);
			myOpzioni.remove(Tb_improntaCommonDao.XXXimpronta_2);
			myOpzioni.remove(Tb_improntaCommonDao.XXXimpronta_3);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_impronta",
                    this.basicCriteria, session);

			List<Tb_impronta> result = this.basicCriteria
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
	 * <statement nome="selectPerBid" tipo="select" id="8_taymer"> <fisso> WHERE
	 * bid = XXXbid AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_impronta> selectPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_IMPRONTA_selectPerBid");

			filter.setParameter(Tb_improntaCommonDao.XXXbid, opzioni
					.get(Tb_improntaCommonDao.XXXbid));

			myOpzioni.remove(Tb_improntaCommonDao.XXXbid);
			List<Tb_impronta> result = this.basicCriteria
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
	 * <statement nome="selectTuttiPerBid" tipo="select" id="9_taymer"> <fisso>
	 * WHERE bid = XXXbid </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_impronta> selectTuttiPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_IMPRONTA_selectTuttiPerBid");

			filter.setParameter(Tb_improntaCommonDao.XXXbid, opzioni
					.get(Tb_improntaCommonDao.XXXbid));

			myOpzioni.remove(Tb_improntaCommonDao.XXXbid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_impronta",
                    this.basicCriteria, session);

			List<Tb_impronta> result = this.basicCriteria
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

	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_impronta metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_impronta.setTS_INS(now);
        tb_impronta.setTS_VAR(now);
        session.save(this.tb_impronta);
        this.commitTransaction();
        this.closeSession();


	}

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
			<fisso>
				UPDATE Tb_impronta
				 SET
				  ute_var = XXXute_var ,
				  nota_impronta = XXXnota_impronta ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND impronta_1 = XXXimpronta_1
				  AND impronta_2 = XXXimpronta_2
				  AND impronta_3 = XXXimpronta_3
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_impronta metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_impronta",Tb_impronta.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("nota_impronta",this.getParametro().get(KeyParameter.XXXnota_impronta));
		buildUpdate.addProperty("ts_var",now());

		// Inizio intervento almaviva2 Mantis 3572 09.03.2010 non viene updatato il flag canc
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		// Fine intervento almaviva2 Mantis 3572 09.03.2010


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("impronta_1",this.getParametro().get(KeyParameter.XXXimpronta_1),"=");
		buildUpdate.addWhere("impronta_2",this.getParametro().get(KeyParameter.XXXimpronta_2),"=");
		buildUpdate.addWhere("impronta_3",this.getParametro().get(KeyParameter.XXXimpronta_3),"=");


		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}

    /**
     * 	<statement nome="updateCancellaPerBid" tipo="update" id="5_taymer">
			<fisso>
				UPDATE Tb_impronta
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_impronta metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_impronta",Tb_impronta.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancella" tipo="update" id="6_taymer">
	<!-- da modificare con le nuove chiavi -->
			<fisso>
				UPDATE Tb_impronta
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
				  AND impronta_1 = XXXimpronta_1
				  AND impronta_2 = XXXimpronta_2
				  AND impronta_3 = XXXimpronta_3
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_impronta metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_impronta",Tb_impronta.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");
		buildUpdate.addWhere("impronta_1",this.getParametro().get(KeyParameter.XXXimpronta_1),"=");
		buildUpdate.addWhere("impronta_2",this.getParametro().get(KeyParameter.XXXimpronta_2),"=");
		buildUpdate.addWhere("impronta_3",this.getParametro().get(KeyParameter.XXXimpronta_3),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_impronta.class;
	}
}
