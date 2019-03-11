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
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_mar_bib;
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
public class Tr_mar_bibResult  extends it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao{

    private Tr_mar_bib tr_mar_bib;

	public Tr_mar_bibResult(Tr_mar_bib tr_mar_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_mar_bib.leggiAllParametro());
        this.tr_mar_bib = tr_mar_bib;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
				WHERE
				  cd_biblioteca = XXXcd_biblioteca
				  AND cd_polo = XXXcd_polo
				  AND mid = XXXmid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_mar_bib> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_MAR_BIB_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_biblioteca, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_biblioteca));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_biblioteca);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid);

			List<Tr_mar_bib> result = this.basicCriteria
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
	<statement nome="selectPerPolo" tipo="select" id="Carlo_01">
		<fisso>
				WHERE
                  cd_polo = XXXcd_polo
				  AND mid = XXXmid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_mar_bib> selectPerPolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_MAR_BIB_selectPerPolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_mar_bib",
                    this.basicCriteria, session);

			List<Tr_mar_bib> result = this.basicCriteria
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
	<statement nome="selectPerPoloDiverso" tipo="select" id="Jenny_05">
		<fisso>
				WHERE
				  cd_polo != XXXcd_polo
				  AND mid = XXXmid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_mar_bib> selectPerPoloDiverso(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_MAR_BIB_selectPerPoloDiverso");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_mar_bib",
                    this.basicCriteria, session);

			List<Tr_mar_bib> result = this.basicCriteria
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
	<statement nome="selectPerAllineamento" tipo="select" id="07_taymer">
		<fisso>
				WHERE
				  cd_polo != XXXcd_polo
				  AND mid = XXXmid
				  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_mar_bib> selectPerAllineamento(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_MAR_BIB_selectPerAllineamento");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_mar_bib",
                    this.basicCriteria, session);

			List<Tr_mar_bib> result = this.basicCriteria
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
	<statement nome="selectPerFiltraLocalizza" tipo="select" id="Carlo_06">
		<fisso>
				WHERE
				  mid = XXXmid
		</fisso>
		<opzionale dipende="XXXcd_polo"> AND cd_polo = XXXcd_polo</opzionale>
		<opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca IN (</opzionale>
		<opzionale dipende="XXXcdBiblio" ciclico="S" separatore=",">XXXcdBiblio</opzionale>
		<opzionale dipende="XXXcd_biblioteca">)</opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_mar_bib> selectPerFiltraLocalizza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_MAR_BIB_selectPerFiltraLocalizza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_mar_bib",
                    this.basicCriteria, session);

			List<Tr_mar_bib> result = this.basicCriteria
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
	<statement nome="verificaEsistenza" tipo="select" id="09_taymer">
		<fisso>
				WHERE
				  cd_biblioteca = XXXcd_biblioteca
				  AND cd_polo = XXXcd_polo
				  AND mid = XXXmid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_mar_bib> verificaEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_MAR_BIB_verificaEsistenza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_biblioteca, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_biblioteca));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_biblioteca);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_mar_bibCommonDao.XXXmid);
			List<Tr_mar_bib> result = this.basicCriteria
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
	 * 	<statement nome="update" tipo="update" id="04">
		<fisso>
				UPDATE Tr_mar_bib
				 SET
				  fl_allinea = XXXfl_allinea ,
				  ute_var = XXXute_var ,
				  fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = XXXfl_canc
				WHERE
				  cd_biblioteca = XXXcd_biblioteca
				  AND cd_polo = XXXcd_polo
				  AND mid = XXXmid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_mar_bib metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_mar_bib",Tr_mar_bib.class);

		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
		buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");

		buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}
    /*
     *  <statement nome="insert" tipo="insert" id="03">
        <fisso>
                INSERT INTO Tr_mar_bib
                 (
                  fl_allinea ,
                  ts_ins ,
                  ute_var ,
                  fl_allinea_sbnmarc ,
                  cd_polo ,
                  ts_var ,
                  cd_biblioteca ,
                  mid ,
                  fl_canc ,
                  ute_ins
                 )
                VALUES
                 (
                  XXXfl_allinea ,
                  SYSTIMESTAMP ,
                  XXXute_var ,
                  XXXfl_allinea_sbnmarc ,
                  XXXcd_polo ,
                  SYSTIMESTAMP ,
                  XXXcd_biblioteca ,
                  XXXmid ,
                  XXXfl_canc ,
                  XXXute_ins
                 )
            </fisso>
    </statement>

     */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_mar_bib metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_mar_bib.setTS_INS(now);
        tr_mar_bib.setTS_VAR(now);
        session.saveOrUpdate(this.tr_mar_bib);
        this.commitTransaction();
        this.closeSession();
	}

	/**
	 * 	<statement nome="updatePerModifica" tipo="update" id="04">
		<fisso>
			UPDATE Tr_mar_bib
			 SET
			  fl_allinea = XXXfl_allinea ,
			  fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc ,
			  ute_var = XXXute_var ,
			  fl_canc = XXXfl_canc ,
			  ts_var = SYSTIMESTAMP
			WHERE
			  cd_polo = XXXcd_polo
			  AND mid = XXXmid
		</fisso>
		<opzionale dipende="XXXfl_allinea_sbnmarc"> AND fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc </opzionale>
		<opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca = XXXcd_biblioteca</opzionale>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void updatePerModifica(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_mar_bib metodo updatePerModifica invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_mar_bib",Tr_mar_bib.class);

		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
		buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");

		if(this.getParametro().containsKey(KeyParameter.XXXcd_biblioteca))
			buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
		if(this.getParametro().containsKey(KeyParameter.XXXfl_allinea_sbnmarc))
			buildUpdate.addWhere("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
   }
    /*
     *  <statement nome="updateTuttiFlag" tipo="update" id="8_taymer">
        <fisso>
            UPDATE Tr_mar_bib
             SET
              ute_var = XXXute_var ,
              ts_var = SYSTIMESTAMP ,
              fl_allinea = XXXfl_allinea,
              fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc
            WHERE
              mid = XXXmid
              AND cd_biblioteca = XXXcd_biblioteca
              AND cd_polo = XXXcd_polo
        </fisso>
    </statement>

     */
    public void updateTuttiFlag(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_mar_bib metodo updateTuttiFlag invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_mar_bib",Tr_mar_bib.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
        buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));


        buildUpdate.addWhere("mid",this.getParametro().get(KeyParameter.XXXmid),"=");
        buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
        buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_mar_bib.class;
	}
}
