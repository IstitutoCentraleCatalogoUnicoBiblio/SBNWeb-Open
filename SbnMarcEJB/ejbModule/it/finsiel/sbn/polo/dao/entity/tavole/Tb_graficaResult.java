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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_graficaCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_grafica;
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
public class Tb_graficaResult extends Tb_graficaCommonDao {

    private Tb_grafica tb_grafica;

    public Tb_graficaResult(Tb_grafica tb_grafica) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_grafica.leggiAllParametro());
        this.tb_grafica = tb_grafica;
    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01_taymer"> <fisso>
	 * WHERE bid = XXXbid AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_grafica> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_GRAFICA_selectPerKey");

			filter.setParameter(Tb_graficaCommonDao.XXXbid, opzioni
					.get(Tb_graficaCommonDao.XXXbid));

			myOpzioni.remove(Tb_graficaCommonDao.XXXbid);
			List<Tb_grafica> result = this.basicCriteria
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
        log.debug("Tb_grafica metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_grafica.setTS_INS(now);
        tb_grafica.setTS_VAR(now);
        // DEVE ESSERE SETTATO ALTRIMENTI VA IN ERRORE
        tb_grafica.setFL_CANC(" ");

        // Modifica almaviva2 2011.02.08 - BUG MANTIS 4156 Si deve invocare il metodo saveOrUpdate per
        // il caso di precedente cancellazione e successiva ricattura
//        session.save(this.tb_grafica);
        session.saveOrUpdate(this.tb_grafica);

        this.commitTransaction();
        this.closeSession();


	}
	/**
	<statement nome="delete" tipo="delete" id="02">
	<fisso>
		DELETE FROM Tb_grafica
		WHERE
		  bid = XXXbid
	</fisso>
</statement>
*/
    public void delete(Object opzioni) throws InfrastructureException {
        Session session = this.getSession();
        this.beginTransaction();
        session.delete(this.tb_grafica);
		this.commitTransaction();
		this.closeSession();
    }

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
		<fisso>
			UPDATE Tb_grafica
			 SET
			  cd_livello = XXXcd_livello ,
			  cd_colore = XXXcd_colore ,
			  cd_tecnica_dis_1 = XXXcd_tecnica_dis_1 ,
			  ute_var = XXXute_var ,
			  cd_supporto = XXXcd_supporto ,
			  cd_design_funz = XXXcd_design_funz ,
			  ts_var = SYSTIMESTAMP ,
			  cd_tecnica_sta_3 = XXXcd_tecnica_sta_3 ,
			  cd_tecnica_sta_2 = XXXcd_tecnica_sta_2 ,
			  cd_tecnica_sta_1 = XXXcd_tecnica_sta_1 ,
			  cd_tecnica_dis_3 = XXXcd_tecnica_dis_3 ,
			  tp_materiale_gra = XXXtp_materiale_gra ,
			  cd_tecnica_dis_2 = XXXcd_tecnica_dis_2
			WHERE
			  bid = XXXbid
			  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_grafica metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_grafica",Tb_grafica.class);

		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("cd_colore",this.getParametro().get(KeyParameter.XXXcd_colore));
		buildUpdate.addProperty("cd_tecnica_dis_1",this.getParametro().get(KeyParameter.XXXcd_tecnica_dis_1));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("cd_supporto",this.getParametro().get(KeyParameter.XXXcd_supporto));
		buildUpdate.addProperty("cd_design_funz",this.getParametro().get(KeyParameter.XXXcd_design_funz));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("cd_tecnica_sta_3",this.getParametro().get(KeyParameter.XXXcd_tecnica_sta_3));
		buildUpdate.addProperty("cd_tecnica_sta_2",this.getParametro().get(KeyParameter.XXXcd_tecnica_sta_2));
		buildUpdate.addProperty("cd_tecnica_sta_1",this.getParametro().get(KeyParameter.XXXcd_tecnica_sta_1));
		buildUpdate.addProperty("cd_tecnica_dis_3",this.getParametro().get(KeyParameter.XXXcd_tecnica_dis_3));
		buildUpdate.addProperty("tp_materiale_gra",this.getParametro().get(KeyParameter.XXXtp_materiale_gra));
		buildUpdate.addProperty("cd_tecnica_dis_2",this.getParametro().get(KeyParameter.XXXcd_tecnica_dis_2));



		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}

	/**
	 * 	<statement nome="updateCancella" tipo="update" id="5_taymer">
			<fisso>
				UPDATE Tb_grafica
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_grafica metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_grafica",Tb_grafica.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_grafica.class;
	}

}
