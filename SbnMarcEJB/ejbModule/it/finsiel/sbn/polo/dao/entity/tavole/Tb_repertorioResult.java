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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_repertorioCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tb_repertorioResult extends Tb_repertorioCommonDao {
	private Tb_repertorio tb_repertorio= null;
    public Tb_repertorioResult(Tb_repertorio tb_repertorio) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_repertorio.leggiAllParametro());
        this.tb_repertorio = tb_repertorio;
    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01"> <fisso> WHERE
	 * id_repertorio = XXXid_repertorio AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_repertorio> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_REPERTORIO_selectPerKey");

			filter.setParameter(Tb_repertorioCommonDao.XXXid_repertorio, opzioni
					.get(Tb_repertorioCommonDao.XXXid_repertorio));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXid_repertorio);
			List<Tb_repertorio> result = this.basicCriteria
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
	 * <statement nome="selectPerCd_sig_repertorio" tipo="select" id="jenny_02">
	 * <fisso> WHERE cd_sig_repertorio = XXXcd_sig_repertorio AND fl_canc != 'S'
	 * </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_repertorio> selectPerCd_sig_repertorio(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_REPERTORIO_selectPerCd_sig_repertorio");

			filter.setParameter(Tb_repertorioCommonDao.XXXcd_sig_repertorio, opzioni
					.get(Tb_repertorioCommonDao.XXXcd_sig_repertorio));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXcd_sig_repertorio);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_repertorio",
                    this.basicCriteria, session);

			List<Tb_repertorio> result = this.basicCriteria
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

	// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
	// nota informativa , nota catalogatore e legame a repertor
	public List<Tb_repertorio> selectPerCd_sig_repertorio_tp_repertorio(HashMap opzioni)
		throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_REPERTORIO_selectPerCd_sig_repertorio_tp_repertorio");

			filter.setParameter(Tb_repertorioCommonDao.XXXcd_sig_repertorio, opzioni
					.get(Tb_repertorioCommonDao.XXXcd_sig_repertorio));
			filter.setParameter(Tb_repertorioCommonDao.XXXtp_repertorio, opzioni
					.get(Tb_repertorioCommonDao.XXXtp_repertorio));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXcd_sig_repertorio);
			myOpzioni.remove(Tb_repertorioCommonDao.XXXtp_repertorio);
		    this.basicCriteria = Parameter.setOrdinamento(opzioni,
		            "Tb_repertorio",
		            this.basicCriteria, session);

			List<Tb_repertorio> result = this.basicCriteria
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


	public List<Tb_repertorio> selectPerCd_sig_repertorioAncheCancellato(
			HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_REPERTORIO_selectPerCd_sig_repertorioAncheCancellato");

			filter.setParameter(Tb_repertorioCommonDao.XXXcd_sig_repertorio,
					opzioni.get(Tb_repertorioCommonDao.XXXcd_sig_repertorio));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXcd_sig_repertorio);
			this.basicCriteria = Parameter.setOrdinamento(opzioni,
					"Tb_repertorio", this.basicCriteria, session);

			List<Tb_repertorio> result = this.basicCriteria.list();
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
	 * <statement nome="selectPerNomeLike" tipo="select" id="Jenny_06"> <fisso>
	 * WHERE fl_canc !='S' AND (UPPER(ds_repertorio) LIKE UPPER(
	 * XXXstringaLike)||'%') </fisso> <opzionale dipende="XXXdata_var_Da"> AND
	 * ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
	 * <opzionale dipende="XXXdata_var_A"> AND ts_var &lt;=
	 * to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_A"> AND ts_ins
	 * &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_repertorio> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_REPERTORIO_selectPerNomeLike");

			filter.setParameter(Tb_repertorioCommonDao.XXXstringaLike, opzioni
					.get(Tb_repertorioCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXstringaLike);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_repertorio",
                    this.basicCriteria, session);

			List<Tb_repertorio> result = this.basicCriteria
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
	 * <statement nome="countPerNomeLike" tipo="count" id="Jenny_07"> <fisso>
	 * SELECT COUNT (*) FROM tb_repertorio WHERE fl_canc !='S' AND
	 * (UPPER(ds_repertorio) LIKE UPPER( XXXstringaLike)||'%') </fisso>
	 * <opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;=
	 * to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_Da"> AND
	 * ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
	 * <opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;=
	 * to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
	 *
	 * </statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_REPERTORIO_countPerNomeLike");

			filter.setParameter(Tb_repertorioCommonDao.XXXstringaLike, opzioni
					.get(Tb_repertorioCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXstringaLike);
			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(Projections.rowCount()))
					.uniqueResult();
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
	 * <statement nome="selectPerParoleNome" tipo="select" id="Jenny_08">
	 * <fisso> WHERE fl_canc !='S' AND CONTAINS(ds_repertorio, XXXparola1 ) > 0
	 * </fisso> <opzionale dipende="XXXparola2"> AND CONTAINS(ds_repertorio,
	 * XXXparola2 ) > 0 </opzionale> <opzionale dipende="XXXparola3"> AND
	 * CONTAINS(ds_repertorio, XXXparola3 ) > 0 </opzionale> <opzionale
	 * dipende="XXXparola4"> AND CONTAINS(ds_repertorio, XXXparola4 ) > 0
	 * </opzionale> <opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;=
	 * to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_Da"> AND
	 * ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
	 * <opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;=
	 * to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
	 *
	 * </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_repertorio> selectPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("TB_REPERTORIO_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_REPERTORIO_selectPerParoleNome");
				filter.setParameter(Tb_repertorioCommonDao.XXXparola1, opzioni
						.get(Tb_repertorioCommonDao.XXXparola1));
				myOpzioni.remove(Tb_repertorioCommonDao.XXXparola1);
			}
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_repertorio",
                    this.basicCriteria, session);

			List<Tb_repertorio> result = this.basicCriteria
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
	 * <statement nome="countPerParoleNome" tipo="count" id="Jenny_09"> <fisso>
	 * SELECT COUNT (*) FROM tb_repertorio WHERE fl_canc !='S' AND
	 * CONTAINS(ds_repertorio, XXXparola1 ) > 0 </fisso> <opzionale
	 * dipende="XXXparola2"> AND CONTAINS(ds_repertorio, XXXparola2 ) > 0
	 * </opzionale> <opzionale dipende="XXXparola3"> AND CONTAINS(ds_repertorio,
	 * XXXparola3 ) > 0 </opzionale> <opzionale dipende="XXXparola4"> AND
	 * CONTAINS(ds_repertorio, XXXparola4 ) > 0 </opzionale> <opzionale
	 * dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_var_A"> AND ts_var
	 * &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_A"> AND ts_ins
	 * &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public Integer countPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("TB_REPERTORIO_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_REPERTORIO_countPerParoleNome");
				filter.setParameter(Tb_repertorioCommonDao.XXXparola1, opzioni
						.get(Tb_repertorioCommonDao.XXXparola1));
				myOpzioni.remove(Tb_repertorioCommonDao.XXXparola1);
			}
			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(Projections.rowCount()))
					.uniqueResult();
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
	 * <statement nome="selectPerNomeEsatto" tipo="select" id="Jenny_10">
	 * <fisso> WHERE fl_canc !='S' AND (UPPER(ds_repertorio) = UPPER(
	 * XXXstringaEsatta)||'%') </fisso> <opzionale dipende="XXXdata_var_Da"> AND
	 * ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
	 * <opzionale dipende="XXXdata_var_A"> AND ts_var &lt;=
	 * to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_A"> AND ts_ins
	 * &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_repertorio> selectPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_REPERTORIO_selectPerNomeEsatto");

			filter.setParameter(Tb_repertorioCommonDao.XXXstringaEsatta, opzioni
					.get(Tb_repertorioCommonDao.XXXstringaEsatta));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXstringaEsatta);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_repertorio",
                    this.basicCriteria, session);

			List<Tb_repertorio> result = this.basicCriteria
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
	 * <statement nome="countPerNomeEsatto" tipo="count" id="Jenny_11"> <fisso>
	 * SELECT COUNT (*) FROM tb_repertorio WHERE fl_canc !='S' AND
	 * (UPPER(ds_repertorio) = UPPER( XXXstringaEsatta)||'%') </fisso>
	 * <opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;=
	 * to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_Da"> AND
	 * ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
	 * <opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;=
	 * to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_REPERTORIO_countPerNomeEsatto");

			filter.setParameter(Tb_repertorioCommonDao.XXXstringaEsatta, opzioni
					.get(Tb_repertorioCommonDao.XXXstringaEsatta));

			myOpzioni.remove(Tb_repertorioCommonDao.XXXstringaEsatta);
			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(Projections.rowCount()))
					.uniqueResult();
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
	 * 	<statement nome="updateVersione" tipo="update" id="12_taymer">
			<fisso>
				UPDATE Tb_repertorio
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  id_repertorio = XXXid_repertorio
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateVersione(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_repertorio metodo updateVersione invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_repertorio",Tb_repertorio.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
	 public void insert(Object opzioni) throws InfrastructureException {

	        log.debug("Tb_repertorio metodo insert invocato");
	        Session session = this.getSession();
	        this.beginTransaction();
	        Timestamp now = now();
			tb_repertorio.setTS_INS(now);
	        tb_repertorio.setTS_VAR(now);
	        session.saveOrUpdate(this.tb_repertorio);
	        this.commitTransaction();
	        this.closeSession();
		}

	public void update(Tb_repertorio repertorio) throws InfrastructureException {

        log.debug("Tb_repertorio metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();

        tb_repertorio.setTS_VAR(now());
        session.saveOrUpdate(this.tb_repertorio);
        this.commitTransaction();
        this.closeSession();
	}

	public void setTipoRepertorio(Map<String, Object> opzioni) {
		if (opzioni.containsKey(KeyParameter.XXXtp_repertorio)) {
			this.basicCriteria.add(Restrictions.eq("TP_REPERTORIO", opzioni.get(KeyParameter.XXXtp_repertorio) ));
		}
	}

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_repertorio.class;
	}

}
