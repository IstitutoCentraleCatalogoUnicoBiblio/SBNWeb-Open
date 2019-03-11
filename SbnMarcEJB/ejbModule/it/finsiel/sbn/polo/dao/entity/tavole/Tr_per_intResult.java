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
import it.finsiel.sbn.polo.orm.Tr_per_int;
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
public class Tr_per_intResult extends TableDao{

    private Tr_per_int tr_per_int;

	public Tr_per_intResult(Tr_per_int tr_per_int) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_per_int.leggiAllParametro());
        this.tr_per_int = tr_per_int;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
			WHERE
			  id_personaggio = XXXid_personaggio
			  AND vid = XXXvid
			  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_per_int> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_PER_INT_selectPerKey");

			filter.setParameter(Tr_per_intResult.XXXid_personaggio, opzioni
					.get(Tr_per_intResult.XXXid_personaggio));
			filter.setParameter(Tr_per_intResult.XXXvid, opzioni
					.get(Tr_per_intResult.XXXvid));

			myOpzioni.remove(Tr_per_intResult.XXXid_personaggio);
			myOpzioni.remove(Tr_per_intResult.XXXvid);


			List<Tr_per_int> result = this.basicCriteria
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
	<statement nome="selectPerPersonaggio" tipo="select" id="05_taymer">
		<fisso>
			WHERE
			  id_personaggio = XXXid_personaggio
			  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_per_int> selectPerPersonaggio(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_PER_INT_selectPerPersonaggio");

			filter.setParameter(Tr_per_intResult.XXXid_personaggio, opzioni
					.get(Tr_per_intResult.XXXid_personaggio));

			myOpzioni.remove(Tr_per_intResult.XXXid_personaggio);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_per_int",
                    this.basicCriteria, session);

			List<Tr_per_int> result = this.basicCriteria
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
            INSERT INTO Tr_per_int
             (
              fl_canc ,
              vid ,
              ute_var ,
              ute_ins ,
              id_personaggio ,
              ts_var ,
              ts_ins
             )
            VALUES
             (
              XXXfl_canc ,
              XXXvid ,
              XXXute_var ,
              XXXute_ins ,
              XXXid_personaggio ,
              SYSTIMESTAMP ,
              SYSTIMESTAMP
             )
        </fisso>
    </statement>

     */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_per_int metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_per_int.setTS_INS(now);
        tr_per_int.setTS_VAR(now);
        session.saveOrUpdate(this.tr_per_int);
        this.commitTransaction();
        this.closeSession();
	}

	/**
	 * 	<statement nome="updateCancellaPerId" tipo="update" id="06_taymer">
		<fisso>
			UPDATE Tr_per_int
			 SET
			  fl_canc = 'S' ,
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP
			WHERE
			  id_personaggio = XXXid_personaggio
			  AND fl_canc != 'S'
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void updateCancellaPerId(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_per_int metodo updateCancellaPerId invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_per_int",Tr_per_int.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("id_personaggio",this.getParametro().get(KeyParameter.XXXid_personaggio),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_per_int.class;
	}

}
