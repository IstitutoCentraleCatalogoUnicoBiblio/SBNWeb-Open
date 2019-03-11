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
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
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
public class Tb_rappresentResult extends TableDao{

    private Tb_rappresent tb_rappresent;

    public Tb_rappresentResult(Tb_rappresent tb_rappresent) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_rappresent.leggiAllParametro());
        this.tb_rappresent = tb_rappresent;
    }

	/**
	<statement nome="selectPerKey" tipo="select" id="01">
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
	public List<Tb_rappresent> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_RAPPRESENT_selectPerKey");

			filter.setParameter(Tb_rappresentResult.XXXbid, opzioni
					.get(Tb_rappresentResult.XXXbid));

			myOpzioni.remove(Tb_rappresentResult.XXXbid);

			List<Tb_rappresent> result = this.basicCriteria
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
	<statement nome="verificaEsistenza" tipo="select" id="02">
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
	public List<Tb_rappresent> verificaEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_RAPPRESENT_verificaEsistenza");

			filter.setParameter(Tb_rappresentResult.XXXbid, opzioni
					.get(Tb_rappresentResult.XXXbid));

			myOpzioni.remove(Tb_rappresentResult.XXXbid);

			List<Tb_rappresent> result = this.basicCriteria
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
        log.debug("Tb_rappresent metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_rappresent.setTS_INS(now);
        tb_rappresent.setTS_VAR(now);

        // Inizio intervento almaviva2 BUG MANTIS esercizio 4406 (polo NAP) sostituito il save con saveOrUpdate
        // per i casi in cui l'oggetto fosse già presente sulla Base Dati (precedente cattura);
//        session.save(this.tb_rappresent);
        session.saveOrUpdate(this.tb_rappresent);
        // Fine intervento almaviva2 BUG MANTIS esercizio 4406

        this.commitTransaction();
        this.closeSession();


	}

	/**
	 * 	<statement nome="update" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tb_rappresent
				 SET
				  ds_occasione = XXXds_occasione ,
				  ute_var = XXXute_var ,
				  ds_luogo_rapp = XXXds_luogo_rapp ,
				  tp_genere = XXXtp_genere ,
				  ds_teatro = XXXds_teatro ,
				  nota_rapp = XXXnota_rapp ,
				  ds_periodo = XXXds_periodo ,
				  aa_rapp = XXXaa_rapp ,
				  ts_var = SYSTIMESTAMP,
				  fl_canc = ' '
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_rappresent metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_rappresent",Tb_rappresent.class);

		buildUpdate.addProperty("ds_occasione",this.getParametro().get(KeyParameter.XXXds_occasione));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ds_luogo_rapp",this.getParametro().get(KeyParameter.XXXds_luogo_rapp));
		buildUpdate.addProperty("tp_genere",this.getParametro().get(KeyParameter.XXXtp_genere));
		buildUpdate.addProperty("ds_teatro",this.getParametro().get(KeyParameter.XXXds_teatro));
		buildUpdate.addProperty("nota_rapp",this.getParametro().get(KeyParameter.XXXnota_rapp));
		buildUpdate.addProperty("ds_periodo",this.getParametro().get(KeyParameter.XXXds_periodo));
		buildUpdate.addProperty("aa_rapp",this.getParametro().get(KeyParameter.XXXaa_rapp));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc"," ");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}

	/**
	 * 	<statement nome="updateCancella" tipo="update" id="06_taymer">
			<fisso>
				UPDATE Tb_rappresent
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
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_rappresent metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_rappresent",Tb_rappresent.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_rappresent.class;
	}
}
