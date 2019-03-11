/**
 * TODO Da Testare
 * @author Antonio
 *
 */
package it.finsiel.sbn.polo.dao.entity.tavole;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_termini_termini;
import it.finsiel.sbn.util.BuilderUpdate;
import it.finsiel.sbn.util.Expression;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

public class Tr_termini_terminiResult extends it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao{

    private Tr_termini_termini tr_termini_termini;

	public Tr_termini_terminiResult(Tr_termini_termini tr_termini_termini) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_termini_termini.leggiAllParametro());
        this.tr_termini_termini = tr_termini_termini;
    }

	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  did_base = XXXdid_base
				  AND did_coll = XXXdid_coll
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_termini_termini> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TERMINI_TERMINI_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll);
			List<Tr_termini_termini> result = this.basicCriteria
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
	<statement nome="selectLegamePerDid" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
					1=1
			</fisso>
			<opzionale dipende="XXXdid_base"> AND did_base = XXXdid_base</opzionale>
			<opzionale dipende="XXXdid_coll"> AND did_coll = XXXdid_coll</opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_termini_termini> selectLegamePerDid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TR_TERMINI_TERMINI_selectLegamePerDid");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_termini_termini",
                    this.basicCriteria, session);

			List<Tr_termini_termini> result = this.basicCriteria
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
	<statement nome="cercaLegamiPerDid" tipo="select" id="Jenny_08">
			<fisso>
				WHERE
				  did_base = XXXdid_base
				  OR did_coll = XXXdid_coll

			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_termini_termini> cercaLegamiPerDid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TERMINI_TERMINI_cercaLegamiPerDid");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_termini_termini",
                    this.basicCriteria, session);

			List<Tr_termini_termini> result = this.basicCriteria
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
	<statement nome="cercaDescrittoreDescrittore" tipo="select" id="Jenny_09">
			<fisso>
				WHERE
				  ( did_base = XXXdid_base
				  AND did_coll = XXXdid_coll)
				OR ( did_base = XXXdid_coll
				  AND did_coll = XXXdid_base)
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_termini_termini> cercaTerminiTermini(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TERMINI_TERMINI_cercaTerminiTermini");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_termini_termini",
                    this.basicCriteria, session);

			List<Tr_termini_termini> result = this.basicCriteria
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
	 *
	 *
	<statement nome="cercaLegamiDescrittoreDescrittore" tipo="select" id="Jenny_09">
			<fisso>
				WHERE
				  ( did_base = XXXdid_base
				  OR did_coll = XXXdid_coll)
				OR ( did_base = XXXdid_coll
				  AND did_coll = XXXdid_base)
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_termini_termini> cercaLegamiTerminiTermini(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TERMINI_TERMINI_cercaLegamiTerminiTermini");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_termini_terminiCommonDao.XXXdid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_termini_termini",
                    this.basicCriteria, session);

			List<Tr_termini_termini> result = this.basicCriteria
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
                INSERT INTO Tr_termini_termini
                 (
                  fl_canc ,
                  did_base ,
                  ute_var ,
                  ute_ins ,
                  tipo_coll ,
                  ts_var ,
                  ts_ins ,
                  did_coll
                 )
                VALUES
                 (
                  XXXfl_canc ,
                  XXXdid_base ,
                  XXXute_var ,
                  XXXute_ins ,
                  XXXtipo_coll ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP ,
                  XXXdid_coll
                 )
            </fisso>

	 */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_termini_termini metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_termini_termini.setTS_INS(now);
        tr_termini_termini.setTS_VAR(now);
        session.saveOrUpdate(this.tr_termini_termini);
        this.commitTransaction();
        this.closeSession();

	}

    public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub

        log.debug("Tr_termini_termini metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_termini_termini",Tr_termini_termini.class);

        buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
        buildUpdate.addProperty("tipo_coll",this.getParametro().get(KeyParameter.XXXtipo_coll));
        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
//        Expression esp = new Expression();
//        esp.addProperty("did_base",this.getParametro().get(KeyParameter.XXXdid_base),"=");
//        Expression esp1 = new Expression();
//        esp1.addProperty("did_coll",this.getParametro().get(KeyParameter.XXXdid_coll),"=");
//
//        buildUpdate.addExpressionOr(esp);
//        buildUpdate.addExpressionOr(esp1);

        buildUpdate.addWhere("did_base",this.getParametro().get(KeyParameter.XXXdid_base),"=");
        buildUpdate.addWhere("did_coll",this.getParametro().get(KeyParameter.XXXdid_coll),"=");


        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }

    public void updateold(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_termini_termini metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_termini_termini.setTS_INS(now);
        tr_termini_termini.setTS_VAR(now);
        session.update(this.tr_termini_termini);
        this.commitTransaction();
        this.closeSession();

    }

    /**
     * 	<statement nome="cancellaLegame" tipo="update" id="Jenny_05">
			<fisso>
				UPDATE Tr_termini_termini
				 SET
				  fl_canc = XXXfl_canc ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  did_base = XXXdid_base
				  AND did_coll = XXXdid_coll
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void cancellaLegami(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_termini_termini metodo cancellaLegami invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_termini_termini",Tr_termini_termini.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("did_base",this.getParametro().get(KeyParameter.XXXdid_base),"=");
		buildUpdate.addWhere("did_coll",this.getParametro().get(KeyParameter.XXXdid_coll),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="cancellaLegameDescrittore" tipo="update" id="07">
			<fisso>
				UPDATE Tr_termini_termini
				 SET
				  fl_canc = 'S' ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  did_base = XXXdid_base
				  AND did_coll = XXXdid_coll
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void cancellaLegameTermini(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_termini_termini metodo cancellaLegameTermini invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_termini_termini",Tr_termini_termini.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("did_base",this.getParametro().get(KeyParameter.XXXdid_base),"=");
		buildUpdate.addWhere("did_coll",this.getParametro().get(KeyParameter.XXXdid_coll),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
    public void updateModifica(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_termini_termini metodo updateModifica invocato");
        Session session = this.getSession();
        this.beginTransaction();

        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_termini_termini",Tr_termini_termini.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        //buildUpdate.addProperty("nota_termini_termini",this.getParametro().get(KeyParameter.XXXnota_termini_termini));

        Expression exp = null;

        exp = new Expression("0");
        exp.addProperty("did_base",this.getParametro().get(KeyParameter.XXXdid_1),"=");
        exp.addProperty("did_coll",this.getParametro().get(KeyParameter.XXXdid_2),"=");
        buildUpdate.addExpressionOr(exp);
        exp = new Expression("1");
        exp.addProperty("did_coll",this.getParametro().get(KeyParameter.XXXdid_1),"=");
        exp.addProperty("did_base",this.getParametro().get(KeyParameter.XXXdid_2),"=");
        buildUpdate.addExpressionOr(exp);

        buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_termini_termini.class;
	}

}
