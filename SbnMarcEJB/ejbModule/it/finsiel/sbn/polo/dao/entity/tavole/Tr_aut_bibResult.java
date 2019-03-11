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
import it.finsiel.sbn.polo.orm.Tr_aut_bib;
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
public class Tr_aut_bibResult extends it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao{

    private Tr_aut_bib tr_aut_bib;

	public Tr_aut_bibResult(Tr_aut_bib tr_aut_bib) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_aut_bib.leggiAllParametro());
        this.tr_aut_bib = tr_aut_bib;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
				WHERE
				  cd_biblioteca = XXXcd_biblioteca
				  AND cd_polo = XXXcd_polo
				  AND vid = XXXvid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_bib> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_selectPerKey");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_biblioteca, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_biblioteca));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_biblioteca);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);
			List<Tr_aut_bib> result = this.basicCriteria
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
				  AND vid = XXXvid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_bib> selectPerPolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_selectPerPolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_bib",
                    this.basicCriteria, session);

			List<Tr_aut_bib> result = this.basicCriteria
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
	<statement nome="selectPerPoloDiverso" tipo="select" id="Jenny_06">
		<fisso>
				WHERE
				  cd_polo != XXXcd_polo
				  AND vid = XXXvid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_bib> selectPerPoloDiverso(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_selectPerPoloDiverso");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_bib",
                    this.basicCriteria, session);

			List<Tr_aut_bib> result = this.basicCriteria
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
	<statement nome="selectPerAllineamento" tipo="select" id="10_taymer">
		<fisso>
				WHERE
				  cd_polo != XXXcd_polo
				  AND vid = XXXvid
				  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_bib> selectPerAllineamento(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_selectPerAllineamento");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_bib",
                    this.basicCriteria, session);

			List<Tr_aut_bib> result = this.basicCriteria
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
	<statement nome="verificaEsistenza" tipo="select" id="12_taymer">
		<fisso>
				WHERE
				  cd_biblioteca = XXXcd_biblioteca
				  AND cd_polo = XXXcd_polo
				  AND vid = XXXvid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_bib> verificaEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_verificaEsistenza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_biblioteca, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_biblioteca));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_biblioteca);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXcd_polo);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);

			List<Tr_aut_bib> result = this.basicCriteria
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
	<statement nome="selectPerFiltraLocalizza" tipo="select" id="Carlo_02">
		<fisso>
				WHERE
				  vid = XXXvid
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
	public List<Tr_aut_bib> selectPerFiltraLocalizza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_selectPerFiltraLocalizza");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_bib",
                    this.basicCriteria, session);

			List<Tr_aut_bib> result = this.basicCriteria
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
	<statement nome="selectPerFlagAllinea" tipo="select" id="15_taymer">
		<fisso>
				WHERE
				  fl_canc != 'S'
				  AND fl_allinea = XXXfl_allinea
				  AND vid = XXXvid
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_bib> selectPerFlagAllinea(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_selectPerFlagAllinea");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXfl_allinea, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXfl_allinea));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXfl_allinea);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_bib",
                    this.basicCriteria, session);

			List<Tr_aut_bib> result = this.basicCriteria
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
	<statement nome="selectPerFlagAllineaDiverso" tipo="select" id="15_taymer">
		<fisso>
				WHERE
				  fl_canc != 'S'
				  AND fl_allinea != XXXfl_allinea
				  AND vid = XXXvid
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_aut_bib> selectPerFlagAllineaDiverso(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_AUT_BIB_selectPerFlagAllineaDiverso");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid));
			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXfl_allinea, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXfl_allinea));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXvid);
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Tr_aut_bibCommonDao.XXXfl_allinea);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_aut_bib",
                    this.basicCriteria, session);

			List<Tr_aut_bib> result = this.basicCriteria
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
				UPDATE Tr_aut_bib
				 SET
				  fl_allinea = XXXfl_allinea ,
				  ute_var = XXXute_var ,
				  fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = XXXfl_canc
				WHERE
				  cd_biblioteca = XXXcd_biblioteca
				  AND cd_polo = XXXcd_polo
				  AND vid = XXXvid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_aut_bib metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_bib",Tr_aut_bib.class);

		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
		buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");


		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}
	/*
     *  <statement nome="insert" tipo="insert" id="03">
        <fisso>
                INSERT INTO Tr_aut_bib
                 (
                  fl_allinea ,
                  ts_ins ,
                  ute_var ,
                  fl_allinea_sbnmarc ,
                  cd_polo ,
                  ts_var ,
                  vid ,
                  cd_biblioteca ,
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
                  XXXvid ,
                  XXXcd_biblioteca ,
                  XXXfl_canc ,
                  XXXute_ins
                 )
            </fisso>

	 */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_aut_bib metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_aut_bib.setTS_INS(now);
        tr_aut_bib.setTS_VAR(now);
        session.saveOrUpdate(this.tr_aut_bib);
        this.commitTransaction();
        this.closeSession();
	}

    /**
     * 		<statement nome="updatePerDelocalizza" tipo="update" id="Dani_01">
		<fisso>
			UPDATE Tr_aut_bib
			 SET
			  fl_canc = 'S',
  			  fl_allinea = ' ',
              fl_allinea_sbnmarc = ' ',
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP
			WHERE
			  cd_polo = XXXcd_polo
			  AND vid = XXXvid
		</fisso>
		<opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca = XXXcd_biblioteca</opzionale>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updatePerDelocalizza(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_bib metodo updatePerDelocalizza invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_bib",Tr_aut_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("fl_allinea"," ");
		buildUpdate.addProperty("fl_allinea_sbnmarc"," ");

		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		if(this.getParametro().containsKey(KeyParameter.XXXcd_biblioteca))
			buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
   }

    /**
     * 	<statement nome="updatePerModifica" tipo="update" id="Jenny_05">
		<fisso>
			UPDATE Tr_aut_bib
			 SET
			  fl_allinea = XXXfl_allinea ,
  			  fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc ,
  			  fl_canc = XXXfl_canc ,
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP
			WHERE
			  cd_polo = XXXcd_polo
			  AND vid = XXXvid
		</fisso>
		<opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca = XXXcd_biblioteca</opzionale>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updatePerModifica(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_bib metodo updatePerModifica invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_bib",Tr_aut_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
		buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");
		if(this.getParametro().containsKey(KeyParameter.XXXcd_biblioteca))
			buildUpdate.addWhere("XXXcd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
   }

    /**
     * 	<statement nome="updateTuttiFlag" tipo="update" id="11_taymer">
		<fisso>
			UPDATE Tr_aut_bib
			 SET
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_allinea = XXXfl_allinea,
  			  fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc
			WHERE
			  vid = XXXvid
			  AND cd_biblioteca = XXXcd_biblioteca
			  AND cd_polo = XXXcd_polo
		</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateTuttiFlag(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_bib metodo updateTuttiFlag invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_bib",Tr_aut_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_allinea",this.getParametro().get(KeyParameter.XXXfl_allinea));
		buildUpdate.addProperty("fl_allinea_sbnmarc",this.getParametro().get(KeyParameter.XXXfl_allinea_sbnmarc));

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
		buildUpdate.addWhere("cd_biblioteca",this.getParametro().get(KeyParameter.XXXcd_biblioteca),"=");
		buildUpdate.addWhere("cd_polo",this.getParametro().get(KeyParameter.XXXcd_polo),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }

    /**
     * 	<statement nome="updateFlCancCancellazione" tipo="update" id="14_taymer">
		<fisso>
			UPDATE Tr_aut_bib
			SET
			  ute_var = XXXute_var ,
			  ts_var = SYSTIMESTAMP ,
			  fl_canc = 'S'
			WHERE
			  vid = XXXvid
		</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateFlCancCancellazione(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_aut_bib metodo updateFlCancCancellazione invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_aut_bib",Tr_aut_bib.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }
	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_aut_bib.class;
	}
}
