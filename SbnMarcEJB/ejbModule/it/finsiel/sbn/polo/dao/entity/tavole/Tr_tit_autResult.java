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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tr_tit_autResult   extends TableDao{

    private Tr_tit_aut tr_tit_aut;

	public Tr_tit_autResult(Tr_tit_aut tr_tit_aut) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_tit_aut.leggiAllParametro());
        this.tr_tit_aut = tr_tit_aut;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND cd_relazione = XXXcd_relazione
				  AND tp_responsabilita = XXXtp_responsabilita
				  AND vid = XXXvid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_aut> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectPerKey");

			filter.setParameter(Tr_tit_autResult.XXXbid, opzioni.get(Tr_tit_autResult.XXXbid));
			filter.setParameter(Tr_tit_autResult.XXXcd_relazione, opzioni.get(Tr_tit_autResult.XXXcd_relazione));
			filter.setParameter(Tr_tit_autResult.XXXtp_responsabilita, opzioni.get(Tr_tit_autResult.XXXtp_responsabilita));
			filter.setParameter(Tr_tit_autResult.XXXvid, opzioni.get(Tr_tit_autResult.XXXvid));

			myOpzioni.remove(Tr_tit_autResult.XXXbid);
			myOpzioni.remove(Tr_tit_autResult.XXXcd_relazione);
			myOpzioni.remove(Tr_tit_autResult.XXXtp_responsabilita);
			myOpzioni.remove(Tr_tit_autResult.XXXvid);


			List<Tr_tit_aut> result = this.basicCriteria
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

    // Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
	// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
	// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
	public List<Tr_tit_aut> selectPerRespRelazione(HashMap opzioni)
		throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectPerRespRelazione");

			filter.setParameter(Tr_tit_autResult.XXXbid, opzioni.get(Tr_tit_autResult.XXXbid));

			myOpzioni.remove(Tr_tit_autResult.XXXbid);

			List<Tr_tit_aut> result = this.basicCriteria
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
	<statement nome="selectPerAutore" tipo="select" id="01">
			<fisso>
				WHERE
				  vid = XXXvid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_aut> selectPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectPerAutore");

			filter.setParameter(Tr_tit_autResult.XXXvid, opzioni.get(Tr_tit_autResult.XXXvid));

			myOpzioni.remove(Tr_tit_autResult.XXXvid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni, "Tr_tit_aut", this.basicCriteria, session);

			List<Tr_tit_aut> result = this.basicCriteria.list();
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
	<statement nome="countPerAutore" tipo="count" id="01">
			<fisso>
				SELECT COUNT(*) FROM Tr_tit_aut
				WHERE
				  vid = XXXvid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerAutore(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_countPerAutore");

			filter.setParameter(Tr_tit_autResult.XXXvid, opzioni.get(Tr_tit_autResult.XXXvid));

			myOpzioni.remove(Tr_tit_autResult.XXXvid);


			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.rowCount())).uniqueResult();
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
	<statement nome="selectPerTitolo" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_aut> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectPerTitolo");

			filter.setParameter(Tr_tit_autResult.XXXbid, opzioni.get(Tr_tit_autResult.XXXbid));

			myOpzioni.remove(Tr_tit_autResult.XXXbid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,  "Tr_tit_aut", this.basicCriteria, session);


			List<Tr_tit_aut> result = this.basicCriteria.list();
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
	<statement nome="selectPerBideVid" tipo="select" id="09_taymer">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND vid = XXXvid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_aut> selectPerBideVid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectPerBideVid");

			filter.setParameter(Tr_tit_autResult.XXXbid, opzioni.get(Tr_tit_autResult.XXXbid));
			filter.setParameter(Tr_tit_autResult.XXXvid, opzioni.get(Tr_tit_autResult.XXXvid));

			myOpzioni.remove(Tr_tit_autResult.XXXbid);
			myOpzioni.remove(Tr_tit_autResult.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,   "Tr_tit_aut",  this.basicCriteria, session);


			List<Tr_tit_aut> result = this.basicCriteria.list();
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

    //  Inizio manutenzione riportata da Indice Su SbnWEB BUG 2982
	// Modifica Mantis 1353 Aggiunto metodo
	/**
	<statement nome="selectPerKeyResp" tipo="select" id="09_taymer">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND vid = XXXvid
				  AND vid = XXXvid
				  AND tp_responsabilita = XXXtp_responsabilita
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_aut> selectPerKeyResp(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectPerKeyResp");

			filter.setParameter(Tr_tit_autResult.XXXbid, opzioni.get(Tr_tit_autResult.XXXbid));
			filter.setParameter(Tr_tit_autResult.XXXvid, opzioni.get(Tr_tit_autResult.XXXvid));
			filter.setParameter(Tr_tit_autResult.XXXtp_responsabilita, opzioni.get(Tr_tit_autResult.XXXtp_responsabilita));

			myOpzioni.remove(Tr_tit_autResult.XXXbid);
			myOpzioni.remove(Tr_tit_autResult.XXXvid);
			myOpzioni.remove(Tr_tit_autResult.XXXtp_responsabilita);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni, "Tr_tit_aut",  this.basicCriteria, session);


			List<Tr_tit_aut> result = this.basicCriteria.list();
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

    //  Fine manutenzione riportata da Indice Su SbnWEB BUG 2982





	/**
	<statement nome="selectEsistenzaPerBideVid" tipo="select" id="09_taymer">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND vid = XXXvid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_aut> selectEsistenzaPerBideVid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectEsistenzaPerBideVid");

			filter.setParameter(Tr_tit_autResult.XXXbid, opzioni.get(Tr_tit_autResult.XXXbid));
			filter.setParameter(Tr_tit_autResult.XXXvid, opzioni.get(Tr_tit_autResult.XXXvid));

			myOpzioni.remove(Tr_tit_autResult.XXXbid);
			myOpzioni.remove(Tr_tit_autResult.XXXvid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,    "Tr_tit_aut",  this.basicCriteria, session);


			List<Tr_tit_aut> result = this.basicCriteria.list();
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
	<statement nome="selectPerKeyCancellato" tipo="select" id="10_taymer">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND cd_relazione = XXXcd_relazione
				  AND tp_responsabilita = XXXtp_responsabilita
				  AND vid = XXXvid
				  AND fl_canc = 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_aut> selectPerKeyCancellato(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_AUT_selectPerKeyCancellato");

			filter.setParameter(Tr_tit_autResult.XXXbid, opzioni.get(Tr_tit_autResult.XXXbid));
			filter.setParameter(Tr_tit_autResult.XXXcd_relazione, opzioni.get(Tr_tit_autResult.XXXcd_relazione));

			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
			// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
			//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
			filter.setParameter(Tr_tit_autResult.XXXcd_strumento_mus, opzioni.get(Tr_tit_autResult.XXXcd_strumento_mus));

			filter.setParameter(Tr_tit_autResult.XXXtp_responsabilita, opzioni.get(Tr_tit_autResult.XXXtp_responsabilita));
			filter.setParameter(Tr_tit_autResult.XXXvid, opzioni.get(Tr_tit_autResult.XXXvid));

			myOpzioni.remove(Tr_tit_autResult.XXXbid);
			myOpzioni.remove(Tr_tit_autResult.XXXcd_relazione);
			myOpzioni.remove(Tr_tit_autResult.XXXcd_strumento_mus);
			myOpzioni.remove(Tr_tit_autResult.XXXtp_responsabilita);
			myOpzioni.remove(Tr_tit_autResult.XXXvid);

            this.basicCriteria = Parameter.setOrdinamento(myOpzioni, "Tr_tit_aut", this.basicCriteria, session);

			List<Tr_tit_aut> result = this.basicCriteria.list();
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
				UPDATE Tr_tit_aut
				 SET
				  ute_var = XXXute_var ,
				  nota_tit_aut = XXXnota_tit_aut ,
				  cd_strumento_mus = XXXcd_strumento_mus ,
				  ts_var = SYSTIMESTAMP ,
				  fl_incerto = XXXfl_incerto ,
				  fl_superfluo = XXXfl_superfluo ,
				  fl_canc = XXXfl_canc
				WHERE
				  bid = XXXbid
				  AND cd_relazione = XXXcd_relazione
				  AND tp_responsabilita = XXXtp_responsabilita
				  AND vid = XXXvid
				  <!-- Lo rimuovo perchè se fanno prima una cancellazione poi un inserimento non va.
				  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var -->
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_tit_aut metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_aut",Tr_tit_aut.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("nota_tit_aut",this.getParametro().get(KeyParameter.XXXnota_tit_aut));
		buildUpdate.addProperty("cd_strumento_mus",this.getParametro().get(KeyParameter.XXXcd_strumento_mus));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_incerto",this.getParametro().get(KeyParameter.XXXfl_incerto));
		buildUpdate.addProperty("fl_superfluo",this.getParametro().get(KeyParameter.XXXfl_superfluo));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cd_relazione",this.getParametro().get(KeyParameter.XXXcd_relazione),"=");
		buildUpdate.addWhere("tp_responsabilita",this.getParametro().get(KeyParameter.XXXtp_responsabilita),"=");
		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}
	/*
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Tr_tit_aut
                 (
                  tp_responsabilita ,
                  ute_var ,
                  ts_ins ,
                  nota_tit_aut ,
                  bid ,
                  cd_relazione ,
                  cd_strumento_mus ,
                  ts_var ,
                  vid ,
                  fl_incerto ,
                  fl_superfluo ,
                  fl_canc ,
                  ute_ins
                 )
                VALUES
                 (
                  XXXtp_responsabilita ,
                  XXXute_var ,
                  SYSTIMESTAMP ,
                  XXXnota_tit_aut ,
                  XXXbid ,
                  XXXcd_relazione ,
                  XXXcd_strumento_mus ,
                  SYSTIMESTAMP ,
                  XXXvid ,
                  XXXfl_incerto ,
                  XXXfl_superfluo ,
                  XXXfl_canc ,
                  XXXute_ins
                 )
            </fisso>
    </statement>

	 */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_tit_aut metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tr_tit_aut.setTS_INS(now);
        tr_tit_aut.setTS_VAR(now);
        session.saveOrUpdate(this.tr_tit_aut);
        this.commitTransaction();
        this.closeSession();

	}


	/**
	 * 	<statement nome="updateDisabilita" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tr_tit_aut
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid = XXXbid AND vid = XXXvid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void updateDisabilita(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_aut metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_aut",Tr_tit_aut.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");
		buildUpdate.addWhere("vid",this.getParametro().get(TableDao.XXXvid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(TableDao.XXXts_var),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
    /*
    <statement nome="delete" tipo="delete" id="02">
    <fisso>
        DELETE FROM Tr_tit_aut
        WHERE
          bid = XXXbid
          AND cd_relazione = XXXcd_relazione
          AND tp_responsabilita = XXXtp_responsabilita
          AND vid = XXXvid
    </fisso>
    </statement>
    */



    /*
    <statement nome="deletePerVidEBid" tipo="delete" id="02">
        <fisso>
            DELETE FROM Tr_tit_aut
            WHERE
              bid = XXXbid
              AND vid = XXXvid
        </fisso>
    </statement>
    */
	public void deletePerVidEBid(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_aut metodo deletePerVidEBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_aut",Tr_tit_aut.class);


        buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
        buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
        int query = buildUpdate.executeDelete();
        this.commitTransaction();
        this.closeSession();

    }

    /**
	<statement nome="updateLegameAutore" tipo="update" id="Jenny_07">
	<fisso>
		UPDATE Tr_tit_aut
		 SET
		  ute_var = XXXute_var ,
		  ts_var = SYSTIMESTAMP,
		  vid = XXXidArrivo
		WHERE
		  bid = XXXbid
		  AND vid = XXXidPartenza
		  AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
	</fisso>
</statement>
     * @throws InfrastructureException
*/
    public void updateLegameAutore(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_aut metodo updateLegameAutore invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_aut",Tr_tit_aut.class);

		buildUpdate.addProperty("vid",this.getParametro().get(KeyParameter.XXXidArrivo));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXidPartenza),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

/**
 * 	<statement nome="updateCancellaPerBid" tipo="update" id="8_taymer">
			<fisso>
				UPDATE Tr_tit_aut
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
    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_aut metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_aut",Tr_tit_aut.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

/**
    <statement nome="updateCancellaLegameTitAut" tipo="update" id="Jenny_10">
	<fisso>
		UPDATE Tr_tit_aut
		 SET
		 fl_canc = 'S' ,
		 ute_var = XXXute_var ,
		 ts_var = SYSTIMESTAMP
		WHERE
		  bid = XXXbid
		AND vid = XXXvid
	</fisso>
</statement>
 * @throws InfrastructureException
*/
    public void updateCancellaLegameTitAut(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_aut metodo updateCancellaLegameTitAut invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_aut",Tr_tit_aut.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("vid",this.getParametro().get(KeyParameter.XXXvid),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_tit_aut.class;
	}
}
