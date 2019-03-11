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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_musicaCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_musica;
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
public class Tb_musicaResult extends Tb_musicaCommonDao {

    private Tb_musica tb_musica;

    public Tb_musicaResult(Tb_musica tb_musica) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_musica.leggiAllParametro());
        this.tb_musica = tb_musica;
    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01"> <fisso> WHERE bid =
	 * XXXbid AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_musica> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_MUSICA_selectPerKey");

			filter
					.setParameter(Tb_musicaCommonDao.XXXbid, opzioni
							.get(Tb_musicaCommonDao.XXXbid));

			myOpzioni.remove(Tb_musicaCommonDao.XXXbid);
			List<Tb_musica> result = this.basicCriteria
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
	 * <statement nome="selectEsistenza" tipo="select" id="06_taymer"> <fisso>
	 * WHERE bid = XXXbid </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_musica> selectEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_MUSICA_selectEsistenza");

			filter
					.setParameter(Tb_musicaCommonDao.XXXbid, opzioni
							.get(Tb_musicaCommonDao.XXXbid));

			myOpzioni.remove(Tb_musicaCommonDao.XXXbid);
			List<Tb_musica> result = this.basicCriteria
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
        log.debug("Tb_musica metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_musica.setTS_INS(now);
        tb_musica.setTS_VAR(now);
        tb_musica.setFL_CANC(" ");

        // Modifica almaviva2 2011.02.08 - BUG MANTIS 4156 Si deve invocare il metodo saveOrUpdate per
        // il caso di precedente cancellazione e successiva ricattura
//        session.save(this.tb_musica);
        session.saveOrUpdate(this.tb_musica);

        this.commitTransaction();
        this.closeSession();


	}

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
		<fisso>
			UPDATE Tb_musica
			 SET
			  datazione = XXXdatazione ,
			  ute_var = XXXute_var ,
			  cd_materia = XXXcd_materia ,
			  ts_var = SYSTIMESTAMP ,
			  notazione_musicale = XXXnotazione_musicale ,
			  fl_palinsesto = XXXfl_palinsesto ,
			  cd_livello = XXXcd_livello ,
			  ds_conservazione = XXXds_conservazione ,
			  ds_legatura = XXXds_legatura ,
			  cd_presentazione = XXXcd_presentazione ,
			  fl_composito = XXXfl_composito ,
			  tp_testo_letter = XXXtp_testo_letter ,
			  cd_stesura = XXXcd_stesura ,
			  tp_elaborazione = XXXtp_elaborazione ,
			  ds_org_anal = XXXds_org_anal ,
			  ds_illustrazioni = XXXds_illustrazioni ,
			  ds_org_sint = XXXds_org_sint
			WHERE
			  bid = XXXbid
			  <!--AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var-->
		</fisso>
	</statement>
	 * @throws InfrastructureException

	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_musica metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_musica",Tb_musica.class);

		buildUpdate.addProperty("datazione",this.getParametro().get(KeyParameter.XXXdatazione));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("cd_materia",this.getParametro().get(KeyParameter.XXXcd_materia));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("notazione_musicale",this.getParametro().get(KeyParameter.XXXnotazione_musicale));
		buildUpdate.addProperty("fl_palinsesto",this.getParametro().get(KeyParameter.XXXfl_palinsesto));
		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("ds_conservazione",this.getParametro().get(KeyParameter.XXXds_conservazione));
		buildUpdate.addProperty("ds_legatura",this.getParametro().get(KeyParameter.XXXds_legatura));
		buildUpdate.addProperty("cd_presentazione",this.getParametro().get(KeyParameter.XXXcd_presentazione));
		buildUpdate.addProperty("fl_composito",this.getParametro().get(KeyParameter.XXXfl_composito));
		buildUpdate.addProperty("tp_testo_letter",this.getParametro().get(KeyParameter.XXXtp_testo_letter));
		buildUpdate.addProperty("cd_stesura",this.getParametro().get(KeyParameter.XXXcd_stesura));
		buildUpdate.addProperty("tp_elaborazione",this.getParametro().get(KeyParameter.XXXtp_elaborazione));
		buildUpdate.addProperty("ds_org_anal",this.getParametro().get(KeyParameter.XXXds_org_anal));
		buildUpdate.addProperty("ds_illustrazioni",this.getParametro().get(KeyParameter.XXXds_illustrazioni));
		buildUpdate.addProperty("ds_org_sint",this.getParametro().get(KeyParameter.XXXds_org_sint));

		//buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}

	/**
	 * 	<statement nome="updateCancella" tipo="update" id="5_taymer">
			<fisso>
				UPDATE Tb_musica
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
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_musica metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_musica",Tb_musica.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
   }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_musica.class;
	}

}
