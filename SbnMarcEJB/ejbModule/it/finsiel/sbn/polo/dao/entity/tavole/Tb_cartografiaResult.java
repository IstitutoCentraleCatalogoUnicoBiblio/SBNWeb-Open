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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_cartografiaCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_cartografia;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO da Testare
 * @author Antonio
 *
 */
public class Tb_cartografiaResult extends Tb_cartografiaCommonDao {

    private Tb_cartografia tb_cartografia;

    public Tb_cartografiaResult(Tb_cartografia tb_cartografia) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_cartografia.leggiAllParametro());
        this.tb_cartografia = tb_cartografia;

    }

	/**
	 * <statement nome="selectPerKey" tipo="select"> <fisso> WHERE bid =
	 * XXXbid </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_cartografia> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_CARTOGRAFIA_selectPerKey");

			filter.setParameter(Tb_cartografiaCommonDao.XXXbid, opzioni
					.get(Tb_cartografiaCommonDao.XXXbid));

			myOpzioni.remove(Tb_cartografiaCommonDao.XXXbid);

			List<Tb_cartografia> result = this.basicCriteria
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

	public void insert(Object opzioni) throws InfrastructureException{
        log.debug("Tb_cartografia metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_cartografia.setTS_INS(now);
        tb_cartografia.setTS_VAR(now);
        // DEVE ESSERE SETTATO ALTRIMENTI VA IN ERRORE
        tb_cartografia.setFL_CANC(" ");

        // Modifica almaviva2 2011.02.08 - BUG MANTIS 4156 Si deve invocare il metodo saveOrUpdate per
        // il caso di precedente cancellazione e successiva ricattura
//        session.save(this.tb_cartografia);
        session.saveOrUpdate(this.tb_cartografia);


        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
		<fisso>
			UPDATE Tb_cartografia
			 SET
			  ute_var = XXXute_var ,
			  longitudine_ovest = XXXlongitudine_ovest ,
			  cd_altitudine = XXXcd_altitudine ,
			  latitudine_sud = XXXlatitudine_sud ,
			  cd_colore = XXXcd_colore ,
			  ts_var = SYSTIMESTAMP ,
			  cd_supporto_fisico = XXXcd_supporto_fisico ,
			  tp_scala = XXXtp_scala ,
			  longitudine_est = XXXlongitudine_est ,
			  cd_tiposcala = XXXcd_tiposcala ,
			  tp_pubb_gov = XXXtp_pubb_gov ,
			  cd_livello = XXXcd_livello ,
			  tp_immagine = XXXtp_immagine ,
			  latitudine_nord = XXXlatitudine_nord ,
			  cd_tecnica = XXXcd_tecnica ,
			  cd_meridiano = XXXcd_meridiano ,
			  cd_piattaforma = XXXcd_piattaforma ,
			  cd_forma_pubb = XXXcd_forma_pubb ,
			  cd_forma_cart = XXXcd_forma_cart ,
			  cd_categ_satellite = XXXcd_categ_satellite ,
			  cd_forma_ripr = XXXcd_forma_ripr ,
			  scala_oriz = XXXscala_oriz ,
			  scala_vert = XXXscala_vert
			WHERE
			  bid = XXXbid
			  <!--AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var-->
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException{
        log.debug("Tb_cartografia metodo update invocato da implemantare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_cartografia",Tb_cartografia.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("longitudine_ovest",this.getParametro().get(KeyParameter.XXXlongitudine_ovest));
		buildUpdate.addProperty("cd_altitudine",this.getParametro().get(KeyParameter.XXXcd_altitudine));
		buildUpdate.addProperty("latitudine_sud",this.getParametro().get(KeyParameter.XXXlatitudine_sud));
		buildUpdate.addProperty("cd_colore",this.getParametro().get(KeyParameter.XXXcd_colore));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("cd_supporto_fisico",this.getParametro().get(KeyParameter.XXXcd_supporto_fisico));
		buildUpdate.addProperty("tp_scala",this.getParametro().get(KeyParameter.XXXtp_scala));
		buildUpdate.addProperty("longitudine_est",this.getParametro().get(KeyParameter.XXXlongitudine_est));
		buildUpdate.addProperty("cd_tiposcala",this.getParametro().get(KeyParameter.XXXcd_tiposcala));
		buildUpdate.addProperty("tp_pubb_gov",this.getParametro().get(KeyParameter.XXXtp_pubb_gov));
		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("tp_immagine",this.getParametro().get(KeyParameter.XXXtp_immagine));
		buildUpdate.addProperty("latitudine_nord",this.getParametro().get(KeyParameter.XXXlatitudine_nord));
		buildUpdate.addProperty("cd_tecnica",this.getParametro().get(KeyParameter.XXXcd_tecnica));
		buildUpdate.addProperty("cd_meridiano",this.getParametro().get(KeyParameter.XXXcd_meridiano));
		buildUpdate.addProperty("cd_piattaforma",this.getParametro().get(KeyParameter.XXXcd_piattaforma));
		buildUpdate.addProperty("cd_forma_pubb",this.getParametro().get(KeyParameter.XXXcd_forma_pubb));
		buildUpdate.addProperty("cd_forma_cart",this.getParametro().get(KeyParameter.XXXcd_forma_cart));
		buildUpdate.addProperty("cd_categ_satellite",this.getParametro().get(KeyParameter.XXXcd_categ_satellite));
		buildUpdate.addProperty("cd_forma_ripr",this.getParametro().get(KeyParameter.XXXcd_forma_ripr));
		buildUpdate.addProperty("scala_oriz",this.getParametro().get(KeyParameter.XXXscala_oriz));
		buildUpdate.addProperty("scala_vert",this.getParametro().get(KeyParameter.XXXscala_vert));

		buildUpdate.addProperty("tp_proiezione", this.getParametro().get(KeyParameter.XXXtp_proiezione));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }


    /**
     * 	<statement nome="updateCancella" tipo="update" id="5_taymer">
			<fisso>
				UPDATE Tb_cartografia
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
    public void updateCancella(Object opzioni) throws InfrastructureException{
        log.debug("Tb_cartografia metodo update invocato da implemantare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_cartografia",Tb_cartografia.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_cartografia.class;
	}

}
