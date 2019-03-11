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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_composizioneCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_composizione;
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
public class Tb_composizioneResult extends Tb_composizioneCommonDao {

    private Tb_composizione tb_composizione;

    public Tb_composizioneResult(Tb_composizione tb_composizione) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_composizione.leggiAllParametro());
        this.tb_composizione = tb_composizione;
    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01"> <fisso> WHERE bid =
	 * XXXbid AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_composizione> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_COMPOSIZIONE_selectPerKey");

			filter.setParameter(Tb_composizioneCommonDao.XXXbid, opzioni
					.get(Tb_composizioneCommonDao.XXXbid));

			myOpzioni.remove(Tb_composizioneCommonDao.XXXbid);
			List<Tb_composizione> result = this.basicCriteria
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
	 * <statement nome="verificaEsistenza" tipo="select" id="6_taymer"> <fisso>
	 * WHERE bid = XXXbid </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_composizione> verificaEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_COMPOSIZIONE_verificaEsistenza");

			filter.setParameter(Tb_composizioneCommonDao.XXXbid, opzioni
					.get(Tb_composizioneCommonDao.XXXbid));

			myOpzioni.remove(Tb_composizioneCommonDao.XXXbid);
			List<Tb_composizione> result = this.basicCriteria
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
	 * <statement nome="selectCreazioneIsbd" tipo="select" id="7_taymer">
	 * <fisso> WHERE fl_canc != 'S' </fisso> <opzionale
	 * dipende="XXXultima_variazione"> AND to_char(ts_var,'yyyymmddhh24miss.FF')
	 * &lt; XXXultima_variazione </opzionale> <opzionale dipende="XXXlettera">
	 * AND ky_ord_ric like XXXlettera || '%' </opzionale> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_composizione> selectCreazioneIsbd(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TB_COMPOSIZIONE_selectCreazioneIsbd");
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_composizione",
                    this.basicCriteria, session);
			List<Tb_composizione> result = this.basicCriteria
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
	 * 	<statement nome="update" tipo="update" id="04_taymer">
			<fisso>
				UPDATE Tb_composizione
				 SET
				  ky_app_nor_pre = XXXky_app_nor_pre ,
				  datazione = XXXdatazione ,
				  numero_cat_tem = XXXnumero_cat_tem ,
				  aa_comp_2 = XXXaa_comp_2 ,
				  ky_est_clet = XXXky_est_clet ,
				  ky_ord_clet = XXXky_ord_clet ,
				  aa_comp_1 = XXXaa_comp_1 ,
				  ute_var = XXXute_var ,
				  cd_forma_3 = XXXcd_forma_3 ,
				  numero_opera = XXXnumero_opera ,
				  cd_forma_2 = XXXcd_forma_2 ,
				  cd_forma_1 = XXXcd_forma_1 ,
				  numero_ordine = XXXnumero_ordine ,
				  ts_var = SYSTIMESTAMP ,
				  ky_ord_pre = XXXky_ord_pre ,
				  ky_app_clet = XXXky_app_clet ,
				  ky_ord_ric = XXXky_ord_ric ,
				  ky_est_nor_pre = XXXky_est_nor_pre ,
				  cd_tonalita = XXXcd_tonalita ,
				  ky_app_den = XXXky_app_den ,
				  ds_sezioni = XXXds_sezioni ,
				  ky_est_den = XXXky_est_den ,
				  ky_ord_nor_pre = XXXky_ord_nor_pre ,
				  ky_app_pre = XXXky_app_pre ,
				  ky_app_ric = XXXky_app_ric ,
				  ky_est_pre = XXXky_est_pre ,
				  ky_est_ric = XXXky_est_ric ,
				  ky_ord_den = XXXky_ord_den
				WHERE
				  bid = XXXbid
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param composizione
	 * @throws InfrastructureException
	 */
    public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_composizione metodo update invocato Da implementare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_composizione",Tb_composizione.class);

		buildUpdate.addProperty("ky_app_nor_pre",this.getParametro().get(KeyParameter.XXXky_app_nor_pre));
		buildUpdate.addProperty("datazione",this.getParametro().get(KeyParameter.XXXdatazione));
		buildUpdate.addProperty("numero_cat_tem",this.getParametro().get(KeyParameter.XXXnumero_cat_tem));
		buildUpdate.addProperty("aa_comp_2",this.getParametro().get(KeyParameter.XXXaa_comp_2));
		buildUpdate.addProperty("ky_est_clet",this.getParametro().get(KeyParameter.XXXky_est_clet));
		buildUpdate.addProperty("ky_ord_clet",this.getParametro().get(KeyParameter.XXXky_ord_clet));
		buildUpdate.addProperty("aa_comp_1",this.getParametro().get(KeyParameter.XXXaa_comp_1));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("cd_forma_3",this.getParametro().get(KeyParameter.XXXcd_forma_3));
		//buildUpdate.addProperty("",this.getParametro().get(KeyParameter.XXXky_app_nor_pre));
		buildUpdate.addProperty("numero_opera",this.getParametro().get(KeyParameter.XXXnumero_opera));
		buildUpdate.addProperty("cd_forma_2",this.getParametro().get(KeyParameter.XXXcd_forma_2));
		buildUpdate.addProperty("cd_forma_1",this.getParametro().get(KeyParameter.XXXcd_forma_1));
		buildUpdate.addProperty("numero_ordine",this.getParametro().get(KeyParameter.XXXnumero_ordine));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("ky_ord_pre",this.getParametro().get(KeyParameter.XXXky_ord_pre));
		buildUpdate.addProperty("ky_app_clet",this.getParametro().get(KeyParameter.XXXky_app_clet));
		buildUpdate.addProperty("ky_ord_ric",this.getParametro().get(KeyParameter.XXXky_ord_ric));
		buildUpdate.addProperty("ky_est_nor_pre",this.getParametro().get(KeyParameter.XXXky_est_nor_pre));
		buildUpdate.addProperty("cd_tonalita",this.getParametro().get(KeyParameter.XXXcd_tonalita));
		buildUpdate.addProperty("ky_app_den",this.getParametro().get(KeyParameter.XXXky_app_den));
		buildUpdate.addProperty("ds_sezioni",this.getParametro().get(KeyParameter.XXXds_sezioni));
		buildUpdate.addProperty("ky_est_den",this.getParametro().get(KeyParameter.XXXky_est_den));
		buildUpdate.addProperty("ky_ord_nor_pre",this.getParametro().get(KeyParameter.XXXky_ord_nor_pre));
		buildUpdate.addProperty("ky_app_pre",this.getParametro().get(KeyParameter.XXXky_app_pre));
		buildUpdate.addProperty("ky_app_ric",this.getParametro().get(KeyParameter.XXXky_app_ric));
		buildUpdate.addProperty("ky_est_pre",this.getParametro().get(KeyParameter.XXXky_est_pre));
		buildUpdate.addProperty("ky_est_ric",this.getParametro().get(KeyParameter.XXXky_est_ric));
		buildUpdate.addProperty("ky_ord_den",this.getParametro().get(KeyParameter.XXXky_ord_den));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateVariazioneEDen" tipo="update" id="09_taymer">
			<fisso>
				UPDATE Tb_composizione
				 SET
				  ts_var = SYSTIMESTAMP ,
				  ky_app_den = XXXky_app_den ,
				  ky_est_den = XXXky_est_den ,
				  ky_ord_den = XXXky_ord_den
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>

     * @param composizione
     * @throws InfrastructureException
     */
    public void updateVariazioneEDen(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_composizione metodo updateVariazioneEDen invocato Da implementare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_composizione",Tb_composizione.class);

		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("ky_app_den",this.getParametro().get(KeyParameter.XXXky_app_den));
		buildUpdate.addProperty("ky_est_den",this.getParametro().get(KeyParameter.XXXky_est_den));
		buildUpdate.addProperty("ky_ord_den",this.getParametro().get(KeyParameter.XXXky_ord_den));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
   }


    /**
     * 	<statement nome="updateVariazione" tipo="update" id="08_taymer">
			<fisso>
				UPDATE Tb_composizione
				 SET
				  ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateVariazione(HashMap map) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_composizione metodo updateVariazione invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_composizione",Tb_composizione.class);

		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     *
     * @param opzioni
     * @throws InfrastructureException
     */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_composizione metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_composizione.setTS_INS(now);
        tb_composizione.setTS_VAR(now);
        tb_composizione.setFL_CANC(" ");
        session.save(this.tb_composizione);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="updateCancella" tipo="update" id="5_taymer">
			<fisso>
				UPDATE Tb_composizione
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
        log.debug("Tb_composizione metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_composizione",Tb_composizione.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_composizione.class;
	}

}
