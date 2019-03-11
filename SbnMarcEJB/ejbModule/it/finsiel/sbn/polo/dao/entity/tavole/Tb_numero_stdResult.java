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
import it.finsiel.sbn.polo.orm.Tb_numero_std;
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
public class Tb_numero_stdResult extends TableDao {


    private Tb_numero_std tb_numero_std;

    public Tb_numero_stdResult(Tb_numero_std tb_numero_std) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_numero_std.leggiAllParametro());
        this.tb_numero_std = tb_numero_std;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND numero_std = XXXnumero_std
				  AND tp_numero_std = XXXtp_numero_std
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_numero_std> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NUMERO_STD_selectPerKey");

			filter.setParameter(Tb_numero_stdResult.XXXbid, opzioni
					.get(Tb_numero_stdResult.XXXbid));
			filter.setParameter(Tb_numero_stdResult.XXXnumero_std, opzioni
					.get(Tb_numero_stdResult.XXXnumero_std));
			filter.setParameter(Tb_numero_stdResult.XXXtp_numero_std, opzioni
					.get(Tb_numero_stdResult.XXXtp_numero_std));

			myOpzioni.remove(Tb_numero_stdResult.XXXbid);
			myOpzioni.remove(Tb_numero_stdResult.XXXnumero_std);
			myOpzioni.remove(Tb_numero_stdResult.XXXtp_numero_std);


			List<Tb_numero_std> result = this.basicCriteria
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
	<statement nome="selectEsistenzaID" tipo="select" id="02">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND numero_std = XXXnumero_std
				  AND tp_numero_std = XXXtp_numero_std
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_numero_std> selectEsistenzaID(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NUMERO_STD_selectEsistenzaID");

			filter.setParameter(Tb_numero_stdResult.XXXbid, opzioni
					.get(Tb_numero_stdResult.XXXbid));
			filter.setParameter(Tb_numero_stdResult.XXXnumero_std, opzioni
					.get(Tb_numero_stdResult.XXXnumero_std));
			filter.setParameter(Tb_numero_stdResult.XXXtp_numero_std, opzioni
					.get(Tb_numero_stdResult.XXXtp_numero_std));

			myOpzioni.remove(Tb_numero_stdResult.XXXbid);
			myOpzioni.remove(Tb_numero_stdResult.XXXnumero_std);
			myOpzioni.remove(Tb_numero_stdResult.XXXtp_numero_std);


			List<Tb_numero_std> result = this.basicCriteria
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
	<statement nome="selectPerBid" tipo="select" id="03">
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
	public List<Tb_numero_std> selectPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NUMERO_STD_selectPerBid");

			filter.setParameter(Tb_numero_stdResult.XXXbid, opzioni
					.get(Tb_numero_stdResult.XXXbid));

			myOpzioni.remove(Tb_numero_stdResult.XXXbid);


			List<Tb_numero_std> result = this.basicCriteria
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
	<statement nome="selectTuttiPerBid" tipo="select" id="11_taymer">
			<fisso>
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_numero_std> selectTuttiPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NUMERO_STD_selectTuttiPerBid");

			filter.setParameter(Tb_numero_stdResult.XXXbid, opzioni
					.get(Tb_numero_stdResult.XXXbid));

			myOpzioni.remove(Tb_numero_stdResult.XXXbid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_numero_std",
                    this.basicCriteria, session);


			List<Tb_numero_std> result = this.basicCriteria
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
	 * 	<statement nome="update" tipo="update" id="06_taymer">
			<fisso>
				UPDATE Tb_numero_std
				 SET
				  numero_lastra = XXXnumero_lastra ,
				  ute_var = XXXute_var ,
				  nota_numero_std = XXXnota_numero_std ,
				  ts_var = SYSTIMESTAMP ,
				  cd_paese = XXXcd_paese
				WHERE
				  bid = XXXbid
				  AND numero_std = XXXnumero_std
				  AND tp_numero_std = XXXtp_numero_std
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_numero_std metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_numero_std",Tb_numero_std.class);

		buildUpdate.addProperty("numero_lastra",this.getParametro().get(KeyParameter.XXXnumero_lastra));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("nota_numero_std",this.getParametro().get(KeyParameter.XXXnota_numero_std));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("cd_paese",this.getParametro().get(KeyParameter.XXXcd_paese));

		// Inizio Modifica almaviva2 11.05.2009 Bug 2893 non viene updatato il flag canc
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		// Fine Modifica almaviva2 11.05.2009 Bug 2893 non viene updatato il flag canc

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("numero_std",this.getParametro().get(KeyParameter.XXXnumero_std),"=");
		buildUpdate.addWhere("tp_numero_std",this.getParametro().get(KeyParameter.XXXtp_numero_std),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_numero_std metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_numero_std.setTS_INS(now);
        tb_numero_std.setTS_VAR(now);
        session.saveOrUpdate(this.tb_numero_std);
        this.commitTransaction();
        this.closeSession();


	}

    /**
     * 	<statement nome="updateCancellaPerBid" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Tb_numero_std
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
        log.debug("Tb_numero_std metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_numero_std",Tb_numero_std.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateCancella" tipo="update" id="10_taymer">
			<fisso>
				UPDATE Tb_numero_std
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND numero_std = XXXnumero_std
				  AND tp_numero_std = XXXtp_numero_std
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_numero_std metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_numero_std",Tb_numero_std.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("numero_std",this.getParametro().get(KeyParameter.XXXnumero_std),"=");
		buildUpdate.addWhere("tp_numero_std",this.getParametro().get(KeyParameter.XXXtp_numero_std),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    public void SelectISBN() {
        // TODO Auto-generated method stub

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_numero_std.class;
	}
}
