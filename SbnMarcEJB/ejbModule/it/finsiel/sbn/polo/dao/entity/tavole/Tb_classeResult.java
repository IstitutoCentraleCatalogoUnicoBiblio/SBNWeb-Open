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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_classeCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * TODO da Testare
 * @author Antonio
 *
 */
public class Tb_classeResult extends Tb_classeCommonDao {

    private Tb_classe tb_classe;

    public Tb_classeResult(Tb_classe tb_classe) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_classe.leggiAllParametro());
        this.tb_classe = tb_classe;
    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01"> <fisso> WHERE
	 * cd_edizione = XXXcd_edizione AND cd_sistema = XXXcd_sistema AND classe =
	 * XXXclasse AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_classe> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_CLASSI_selectPerKey");

			filter.setParameter(Tb_classeCommonDao.XXXcd_edizione, opzioni
					.get(Tb_classeCommonDao.XXXcd_edizione));
			filter.setParameter(Tb_classeCommonDao.XXXcd_sistema, opzioni
					.get(Tb_classeCommonDao.XXXcd_sistema));
			filter.setParameter(Tb_classeCommonDao.XXXclasse, opzioni
					.get(Tb_classeCommonDao.XXXclasse));

			myOpzioni.remove(Tb_classeCommonDao.XXXcd_edizione);
			myOpzioni.remove(Tb_classeCommonDao.XXXcd_sistema);
			myOpzioni.remove(Tb_classeCommonDao.XXXclasse);
			List<Tb_classe> result = this.basicCriteria
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
	 * <statement nome="selectEsistenzaId" tipo="select" id="Jenny_05"> <fisso>
	 * WHERE cd_edizione = XXXcd_edizione AND cd_sistema = XXXcd_sistema AND
	 * classe = XXXclasse </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_classe> selectEsistenzaId(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_CLASSI_selectEsistenzaId");

			filter.setParameter(Tb_classeCommonDao.XXXcd_edizione, opzioni
					.get(Tb_classeCommonDao.XXXcd_edizione));
			filter.setParameter(Tb_classeCommonDao.XXXcd_sistema, opzioni
					.get(Tb_classeCommonDao.XXXcd_sistema));
			filter.setParameter(Tb_classeCommonDao.XXXclasse, opzioni
					.get(Tb_classeCommonDao.XXXclasse));

			myOpzioni.remove(Tb_classeCommonDao.XXXcd_edizione);
			myOpzioni.remove(Tb_classeCommonDao.XXXcd_sistema);
			myOpzioni.remove(Tb_classeCommonDao.XXXclasse);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_classe",
                    this.basicCriteria, session);

			List<Tb_classe> result = this.basicCriteria
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
	 * <statement nome="selectPerNomeLike" tipo="select" id="Jenny_06"> <fisso>
	 * WHERE fl_canc !='S' AND classe LIKE XXXstringaLike||'%' </fisso>
	 * <opzionale dipende="XXXsistema"> AND cd_sistema = XXXsistema </opzionale>
	 * <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;=
	 * XXXlivello_aut_da </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND
	 * cd_livello &lt;= XXXlivello_aut_a </opzionale> <opzionale
	 * dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_var_A"> AND ts_var
	 * &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_A"> AND ts_ins
	 * &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXcd_edizione"> AND cd_edizione = XXXcd_edizione</opzionale>
	 *
	 * </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_classe> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_CLASSI_selectPerNomeLike");

			filter.setParameter(Tb_classeCommonDao.XXXstringaLike, opzioni
					.get(Tb_classeCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_classeCommonDao.XXXstringaLike);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_classe",
                    this.basicCriteria, session);

			List<Tb_classe> result = this.basicCriteria
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
	 * <statement nome="selectPerParoleNome" tipo="select" id="Jenny_08">
	 * <fisso> WHERE fl_canc !='S' AND CONTAINS(ds_classe, XXXparola1 ) > 0
	 * </fisso> <opzionale dipende="XXXparola2"> AND CONTAINS(ds_classe,
	 * XXXparola2 ) > 0 </opzionale> <opzionale dipende="XXXparola3"> AND
	 * CONTAINS(ds_classe, XXXparola3 ) > 0 </opzionale> <opzionale
	 * dipende="XXXparola4"> AND CONTAINS(ds_classe, XXXparola4 ) > 0
	 * </opzionale> <opzionale dipende="XXXsistema"> AND cd_sistema = XXXsistema
	 * </opzionale> <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;=
	 * XXXlivello_aut_da </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND
	 * cd_livello &lt;= XXXlivello_aut_a </opzionale> <opzionale
	 * dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_var_A"> AND ts_var
	 * &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_A"> AND ts_ins
	 * &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXcd_edizione"> AND cd_edizione = XXXcd_edizione</opzionale>
	 * </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_classe> selectPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)){
				filter = session.enableFilter("TB_CLASSI_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_CLASSI_selectPerParoleNome");
				filter.setParameter(Tb_classeCommonDao.XXXparola1, opzioni.get(Tb_classeCommonDao.XXXparola1));
				myOpzioni.remove(Tb_classeCommonDao.XXXparola1);
			}


			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_classe",
                    this.basicCriteria, session);
			List<Tb_classe> result = new ArrayList<Tb_classe>(this.basicCriteria.list());
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
	 * SELECT COUNT (*) FROM tb_classe WHERE fl_canc !='S' AND classe LIKE
	 * XXXstringaLike||'%' </fisso> <opzionale dipende="XXXsistema"> AND
	 * cd_sistema = XXXsistema </opzionale> <opzionale
	 * dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da
	 * </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;=
	 * XXXlivello_aut_a </opzionale> <opzionale dipende="XXXdata_var_Da"> AND
	 * ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
	 * <opzionale dipende="XXXdata_var_A"> AND ts_var &lt;=
	 * to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_A"> AND ts_ins
	 * &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXcd_edizione"> AND cd_edizione = XXXcd_edizione</opzionale>
	 * </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_CLASSI_countPerNomeLike");

			filter.setParameter(Tb_classeCommonDao.XXXstringaLike, opzioni
					.get(Tb_classeCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_classeCommonDao.XXXstringaLike);
			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();
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
	 * SELECT COUNT (*) FROM tb_classe WHERE fl_canc !='S' AND
	 * CONTAINS(ds_classe, XXXparola1 ) > 0 </fisso> <opzionale
	 * dipende="XXXparola2"> AND CONTAINS(ds_classe, XXXparola2 ) > 0
	 * </opzionale> <opzionale dipende="XXXparola3"> AND CONTAINS(ds_classe,
	 * XXXparola3 ) > 0 </opzionale> <opzionale dipende="XXXparola4"> AND
	 * CONTAINS(ds_classe, XXXparola4 ) > 0 </opzionale> <opzionale
	 * dipende="XXXsistema"> AND cd_sistema = XXXsistema </opzionale> <opzionale
	 * dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da
	 * </opzionale> <opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;=
	 * XXXlivello_aut_a </opzionale> <opzionale dipende="XXXdata_var_Da"> AND
	 * ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
	 * <opzionale dipende="XXXdata_var_A"> AND ts_var &lt;=
	 * to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da ,
	 * 'yyyy-mm-dd') </opzionale> <opzionale dipende="XXXdata_ins_A"> AND ts_ins
	 * &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale> <opzionale
	 * dipende="XXXcd_edizione"> AND cd_edizione = XXXcd_edizione</opzionale>
	 * </statement>
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
			if (isSessionPostgreSQL(session)){
				filter = session.enableFilter("TB_CLASSI_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_CLASSI_countPerParoleNome");
				filter.setParameter(Tb_classeCommonDao.XXXparola1, opzioni.get(Tb_classeCommonDao.XXXparola1));
				myOpzioni.remove(Tb_classeCommonDao.XXXparola1);
			}


			this.createCriteria(myOpzioni);
			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();
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

	public void insert(Object opzioni) throws InfrastructureException
	{
        log.debug("Tb_classeResult metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tb_classe.setTS_INS(ts);
        tb_classe.setTS_VAR(ts);

        session.saveOrUpdate(this.tb_classe);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="updatePerModifica" tipo="update" id="Jenny_10">
			<fisso>
				UPDATE Tb_classe
				 SET
				  cd_livello = XXXcd_livello ,
				  fl_costruito = XXXfl_costruito ,
				  ute_var = XXXute_var ,
				  ds_classe = XXXds_classe ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  cd_edizione = XXXcd_edizione
				  AND cd_sistema = XXXcd_sistema
				  AND classe = XXXclasse
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updatePerModifica(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_classeResult metodo updatePerModifica invocato ");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_classe",Tb_classe.class);

		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("fl_costruito",this.getParametro().get(KeyParameter.XXXfl_costruito));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ds_classe",this.getParametro().get(KeyParameter.XXXds_classe));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));
		//almaviva5_20090401 #2780
		buildUpdate.addProperty("ult_term",this.getParametro().get(KeyParameter.XXXult_term));
		buildUpdate.addProperty("ky_classe_ord",this.getParametro().get(KeyParameter.XXXky_classe_ord));

		buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizione),"=");
		buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistema),"=");
    //buildUpdate.addWhere("classe",this.getParametro().get(KeyParameter.XXXclasse),"=");
    // DEVO NECESSARIAMENTE FORZARE LA LUNGHEZZA DELLA classe A 31 CHAR
    // PERCHE ESSENDO UN tipo it.finsiel.sbn.util.DataTypeCHAR e anche una chiave primaria
    // non funziona nel addWhere di generazione ( è consigliabile altra soluzione)
    String cl = this.getParametro().get(KeyParameter.XXXclasse).toString();
    for(int i=cl.length();i<=30;i++)
        cl = cl + " ";
    buildUpdate.addWhere("classe",cl,"=");
    buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");


		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="update" tipo="update" id="04">
			<fisso>
				UPDATE Tb_classe
				 SET
				  cd_livello = XXXcd_livello ,
				  fl_costruito = XXXfl_costruito ,
				  ute_var = XXXute_var ,
				  ts_ins = XXXts_ins ,
				  ds_classe = XXXds_classe ,
				  ts_var = XXXts_var ,
				  fl_canc = XXXfl_canc ,
				  fl_speciale = XXXfl_speciale ,
				  ute_ins = XXXute_ins ,
				WHERE
				  cd_edizione = XXXcd_edizione
				  AND cd_sistema = XXXcd_sistema
				  AND classe = XXXclasse
			</fisso>
	</statement>
     * @param opzioni
     * @throws InfrastructureException
     */
    public void update(Object opzioni) throws InfrastructureException{
        log.debug("Tb_classeResult metodo update invocato da implemantare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_classe",Tb_classe.class);

		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("fl_costruito",this.getParametro().get(KeyParameter.XXXfl_costruito));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_ins",this.getParametro().get(KeyParameter.XXXts_ins));
		buildUpdate.addProperty("ds_classe",this.getParametro().get(KeyParameter.XXXds_classe));
		buildUpdate.addProperty("ts_var",this.getParametro().get(KeyParameter.XXXts_var));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("fl_speciale",this.getParametro().get(KeyParameter.XXXfl_speciale));
		buildUpdate.addProperty("ute_ins",this.getParametro().get(KeyParameter.XXXute_ins));
		//almaviva5_20090401 #2780
		buildUpdate.addProperty("ult_term",this.getParametro().get(KeyParameter.XXXult_term));
		buildUpdate.addProperty("ky_classe_ord",this.getParametro().get(KeyParameter.XXXky_classe_ord));

		buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizione),"=");
		buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistema),"=");
    //buildUpdate.addWhere("classe",this.getParametro().get(KeyParameter.XXXclasse),"=");
    // DEVO NECESSARIAMENTE FORZARE LA LUNGHEZZA DELLA classe A 31 CHAR
    // PERCHE ESSENDO UN tipo it.finsiel.sbn.util.DataTypeCHAR e anche una chiave primaria
    // non funziona nel addWhere di generazione ( è consigliabile altra soluzione)
    String cl = this.getParametro().get(KeyParameter.XXXclasse).toString();
    for(int i=cl.length();i<=30;i++)
        cl = cl + " ";
    buildUpdate.addWhere("classe",cl,"=");
    buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }
    /**
	<statement nome="cancellaClasse" tipo="update" id="Jenny_11">
	<fisso>
		UPDATE Tb_classe
		 SET
		  ute_var = XXXute_var ,
		  fl_canc = 'S' ,
		  ts_var = SYSTIMESTAMP
		WHERE
		  cd_edizione = XXXcd_edizione
		  AND cd_sistema = XXXcd_sistema
		  AND classe = XXXclasse
	</fisso>
</statement>
     */

    public void cancellaClasse(Object opzioni) throws InfrastructureException{
        log.debug("Tb_classeResult metodo cancellaClasse invocato da implemantare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_classe",Tb_classe.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizione),"=");
		buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistema),"=");
    //buildUpdate.addWhere("classe",this.getParametro().get(KeyParameter.XXXclasse),"=");
        // DEVO NECESSARIAMENTE FORZARE LA LUNGHEZZA DELLA classe A 31 CHAR
        // PERCHE ESSENDO UN tipo it.finsiel.sbn.util.DataTypeCHAR e anche una chiave primaria
        // non funziona nel addWhere di generazione ( è consigliabile altra soluzione)
        String cl = this.getParametro().get(KeyParameter.XXXclasse).toString();
        for(int i=cl.length();i<=30;i++)
            cl = cl + " ";
        buildUpdate.addWhere("classe",cl,"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     * 	<statement nome="updateVersione" tipo="update" id="12_taymer">
			<fisso>
				UPDATE Tb_classe
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  cd_edizione = XXXcd_edizione
				  AND cd_sistema = XXXcd_sistema
				  AND classe = XXXclasse
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>
     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateVersione(Object opzioni) throws InfrastructureException{
        log.debug("Tb_classeResult metodo updateVersione invocato da implemantare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_classe",Tb_classe.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizione),"=");
		buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistema),"=");
    //buildUpdate.addWhere("classe",this.getParametro().get(KeyParameter.XXXclasse),"=");
        // DEVO NECESSARIAMENTE FORZARE LA LUNGHEZZA DELLA classe A 31 CHAR
        // PERCHE ESSENDO UN tipo it.finsiel.sbn.util.DataTypeCHAR e anche una chiave primaria
        // non funziona nel addWhere di generazione ( è consigliabile altra soluzione)
        String cl = this.getParametro().get(KeyParameter.XXXclasse).toString();
        for(int i=cl.length();i<=30;i++)
            cl = cl + " ";
        buildUpdate.addWhere("classe",cl,"=");

    buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_classe.class;
	}

}
