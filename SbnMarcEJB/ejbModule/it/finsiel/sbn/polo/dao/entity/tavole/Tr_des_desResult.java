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
import it.finsiel.sbn.polo.orm.Tr_des_des;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

public class Tr_des_desResult extends it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao{

    private Tr_des_des tr_des_des;

	public Tr_des_desResult(Tr_des_des tr_des_des) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_des_des.leggiAllParametro());
        this.tr_des_des = tr_des_des;
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
	public List<Tr_des_des> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_DES_DES_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll);
			List<Tr_des_des> result = this.basicCriteria
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
	public List<Tr_des_des> selectLegamePerDid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TR_DES_DES_selectLegamePerDid");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_des_des",
                    this.basicCriteria, session);

			List<Tr_des_des> result = this.basicCriteria
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
	public List<Tr_des_des> cercaLegamiPerDid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_DES_DES_cercaLegamiPerDid");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_des_des",
                    this.basicCriteria, session);

			List<Tr_des_des> result = this.basicCriteria
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
	public List<Tr_des_des> cercaDescrittoreDescrittore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_DES_DES_cercaDescrittoreDescrittore");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_des_des",
                    this.basicCriteria, session);

			List<Tr_des_des> result = this.basicCriteria
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

//
//SELECT * FROM VL_DESCRITTORE_DES WHERE ( did = 'CSWD000363'
//				    AND TP_FORMA_DES ='R')
//				OR ( did = 'CSWD000359'
//				  and did_1 = 'CSWD000363');


	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_des_des> cercaLegamiDescrittoreDescrittore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_DES_DES_cercaLegamiDescrittoreDescrittore");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_base);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_des_desCommonDao.XXXdid_coll);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_des_des",
                    this.basicCriteria, session);

			List<Tr_des_des> result = this.basicCriteria
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
                INSERT INTO Tr_des_des
                 (
                  fl_canc ,
                  did_base ,
                  ute_var ,
                  ute_ins ,
                  tp_legame ,
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
                  XXXtp_legame ,
                  SYSTIMESTAMP ,
                  SYSTIMESTAMP ,
                  XXXdid_coll
                 )
            </fisso>

	 */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_des_des metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tr_des_des.setTS_INS(ts);
        tr_des_des.setTS_VAR(ts);
        session.saveOrUpdate(this.tr_des_des);
        this.commitTransaction();
        this.closeSession();

	}

    public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub

        log.debug("Tr_des_des metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_des_des",Tr_des_des.class);

        buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
        buildUpdate.addProperty("tp_legame",this.getParametro().get(KeyParameter.XXXtp_legame));
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
        log.debug("Tr_des_des metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_des_des.setTS_INS(now);
        tr_des_des.setTS_VAR(now);
        session.update(this.tr_des_des);
        this.commitTransaction();
        this.closeSession();

    }

    /**
     * 	<statement nome="cancellaLegame" tipo="update" id="Jenny_05">
			<fisso>
				UPDATE Tr_des_des
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
        log.debug("Tr_des_des metodo cancellaLegami invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_des_des",Tr_des_des.class);

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
				UPDATE Tr_des_des
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
    public void cancellaLegameDescrittore(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_des_des metodo cancellaLegameDescrittore invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_des_des",Tr_des_des.class);

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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_des_des.class;
	}
}
