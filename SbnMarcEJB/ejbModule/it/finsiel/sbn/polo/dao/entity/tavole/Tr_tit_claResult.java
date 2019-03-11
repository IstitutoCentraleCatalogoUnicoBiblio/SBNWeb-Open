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
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.util.BuilderUpdate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tr_tit_claResult  extends TableDao{

    private Tr_tit_cla tr_tit_cla;

	public Tr_tit_claResult(Tr_tit_cla tr_tit_cla) throws InfrastructureException {
        super();
        this.valorizzaParametro(tr_tit_cla.leggiAllParametro());
        this.tr_tit_cla = tr_tit_cla;
   }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  bid = XXXbid
				  AND cd_edizione = XXXcd_edizione
				  AND cd_sistema = XXXcd_sistema
				  AND classe = XXXclasse
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_cla> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_CLA_selectPerKey");

			filter.setParameter(Tr_tit_claResult.XXXbid, opzioni
					.get(Tr_tit_claResult.XXXbid));
			filter.setParameter(Tr_tit_claResult.XXXcd_edizione, opzioni
					.get(Tr_tit_claResult.XXXcd_edizione));
			filter.setParameter(Tr_tit_claResult.XXXcd_sistema, opzioni
					.get(Tr_tit_claResult.XXXcd_sistema));
			filter.setParameter(Tr_tit_claResult.XXXclasse, opzioni
					.get(Tr_tit_claResult.XXXclasse));

			myOpzioni.remove(Tr_tit_claResult.XXXbid);
			myOpzioni.remove(Tr_tit_claResult.XXXcd_edizione);
			myOpzioni.remove(Tr_tit_claResult.XXXcd_sistema);
			myOpzioni.remove(Tr_tit_claResult.XXXclasse);


			List<Tr_tit_cla> result = this.basicCriteria
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
	<statement nome="selectTitoloPerClasse" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
				  cd_edizione = XXXcd_edizione
				  AND cd_sistema = XXXcd_sistema
				  AND classe = XXXclasse
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tr_tit_cla> selectTitoloPerClasse(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_CLA_selectTitoloPerClasse");

			filter.setParameter(Tr_tit_claResult.XXXcd_edizione, opzioni
					.get(Tr_tit_claResult.XXXcd_edizione));
			filter.setParameter(Tr_tit_claResult.XXXcd_sistema, opzioni
					.get(Tr_tit_claResult.XXXcd_sistema));
			filter.setParameter(Tr_tit_claResult.XXXclasse, opzioni
					.get(Tr_tit_claResult.XXXclasse));

			myOpzioni.remove(Tr_tit_claResult.XXXcd_edizione);
			myOpzioni.remove(Tr_tit_claResult.XXXcd_sistema);
			myOpzioni.remove(Tr_tit_claResult.XXXclasse);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_cla",
                    this.basicCriteria, session);


			List<Tr_tit_cla> result = this.basicCriteria
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
	<statement nome="selectPerTitolo" tipo="select" id="Jenny_07">
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
	public List<Tr_tit_cla> selectPerTitolo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_CLA_selectPerTitolo");

			filter.setParameter(Tr_tit_claResult.XXXbid, opzioni
					.get(Tr_tit_claResult.XXXbid));

			myOpzioni.remove(Tr_tit_claResult.XXXbid);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tr_tit_cla",
                    this.basicCriteria, session);

			List<Tr_tit_cla> result = this.basicCriteria
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
	<statement nome="countFrequenzaPerBid" tipo="count" id="10_william">
		<fisso>
			SELECT COUNT(*)
			FROM   tr_tit_cla
			WHERE  fl_canc != 'S'
			and bid =XXXbid
		</fisso>
    </statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countFrequenzaPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_TIT_CLA_countFrequenzaPerBid");

			filter.setParameter(Tr_tit_claResult.XXXbid, opzioni
					.get(Tr_tit_claResult.XXXbid));

			myOpzioni.remove(Tr_tit_claResult.XXXbid);


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
    <statement nome="countTitoloClasse" tipo="count" id="11_william">
			<fisso>
			SELECT COUNT(*) FROM tr_tit_cla
				WHERE fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countTitoloClasse(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TR_TIT_CLA_countTitoloClasse");

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
	 * 	<statement nome="update" tipo="update" id="04_taymer">
			<fisso>
				UPDATE Tr_tit_cla
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = XXXfl_canc
				WHERE
				  bid = XXXbid
				  AND cd_edizione = XXXcd_edizione
				  AND cd_sistema = XXXcd_sistema
				  AND classe = XXXclasse
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_cla metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_cla",Tr_tit_cla.class);

		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		//almaviva5_20090402
		buildUpdate.addProperty("nota_tit_cla", this.getParametro().get(KeyParameter.XXXnota_tit_cla));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizione),"=");
		buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistema),"=");

		String cl = this.getParametro().get(KeyParameter.XXXclasse).toString();
		cl = ValidazioneDati.fillRight(cl, ' ', 30);
        buildUpdate.addWhere("classe",cl,"=");
        buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		if (query < 1)
			throw new InfrastructureException("Errore SQL: Nessuna riga aggiornata");
		this.commitTransaction();
		this.closeSession();
   }

    /*
     *  <statement nome="insert" tipo="insert" id="03">
            <fisso>
                INSERT INTO Tr_tit_cla
                 (
                  classe ,
                  ts_ins ,
                  ute_var ,
                  bid ,
                  ts_var ,
                  fl_canc ,
                  cd_sistema ,
                  ute_ins ,
                  cd_edizione
                 )
                VALUES
                 (
                  XXXclasse ,
                  SYSTIMESTAMP ,
                  XXXute_var ,
                  XXXbid ,
                  SYSTIMESTAMP ,
                  XXXfl_canc ,
                  XXXcd_sistema ,
                  XXXute_ins ,
                  XXXcd_edizione
                 )
            </fisso>
    </statement>

     */
	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tr_tit_cla metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tr_tit_cla.setTS_INS(ts);
        tr_tit_cla.setTS_VAR(ts);
        session.saveOrUpdate(this.tr_tit_cla);
        this.commitTransaction();
        this.closeSession();

	}

	/**
	 * 	<statement nome="updateCancellaPerBid" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Tr_tit_cla
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
        log.debug("Tr_tit_cla metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_cla",Tr_tit_cla.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		if (query < 1)
			throw new InfrastructureException("Errore SQL: Nessuna riga aggiornata");
		this.commitTransaction();
		this.closeSession();

    }

	/**
	 * 	<statement nome="updateCancellaLegame" tipo="update" id="9_taymer">
			<fisso>
				UPDATE Tr_tit_cla
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND cd_edizione = XXXcd_edizionePartenza
				  AND cd_sistema = XXXcd_sistemaPartenza
				  AND classe = XXXclassePartenza
				  <!--AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var-->
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateCancellaLegame(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_cla metodo updateCancellaLegame invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_cla",Tr_tit_cla.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizionePartenza),"=");
		buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistemaPartenza),"=");

        String cl = this.getParametro().get(KeyParameter.XXXclassePartenza).toString();
        cl = ValidazioneDati.fillRight(cl, ' ', 30);
        buildUpdate.addWhere("classe",cl,"=");

		int query = buildUpdate.executeUpdate();
		if (query < 1)
			throw new InfrastructureException("Errore SQL: Nessuna riga aggiornata");
		this.commitTransaction();
		this.closeSession();

    }

	/**
	 * 	<statement nome="updateDisabilita" tipo="update" id="05_taymer">
			<fisso>
				UPDATE Tr_tit_cla
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP ,
				  fl_canc = 'S'
				WHERE
				  bid = XXXbid
				  AND cd_edizione = XXXcd_edizione
				  AND cd_sistema = XXXcd_sistema
				  AND classe = XXXclasse
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateDisabilita(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_cla metodo updateDisabilita invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_cla",Tr_tit_cla.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");


		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizione),"=");
		buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistema),"=");

        String cl = this.getParametro().get(KeyParameter.XXXclasse).toString();
        cl = ValidazioneDati.fillRight(cl, ' ', 30);
        buildUpdate.addWhere("classe",cl,"=");
        buildUpdate.addWhere("ts_var",this.getParametro().get(KeyParameter.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		if (query < 1)
			throw new InfrastructureException("Errore SQL: Nessuna riga aggiornata");
		this.commitTransaction();
		this.closeSession();

    }
    public void selectTitoloPerSoggetto(Object opzioni) {
        // TODO Auto-generated method stub
        // forse esiste un'errore nella chiamata di classe che vado a corregere inserendo
        // selectTitoloPerClasse
    }
    /*
     *  <statement nome="updateLegameClasse" tipo="update" id="Jenny_08">
            <fisso>
                UPDATE Tr_tit_cla
                 SET
                  ute_var = XXXute_var ,
                  ts_var = SYSTIMESTAMP,
                  cd_edizione = XXXcd_edizioneArrivo,
                  cd_sistema = XXXcd_sistemaArrivo,
                  classe = XXXclasseArrivo
                WHERE
                  bid = XXXbid
                  AND cd_edizione = XXXcd_edizionePartenza
                  AND cd_sistema = XXXcd_sistemaPartenza
                  AND classe = XXXclassePartenza
            </fisso>
    </statement>

     */
    public void updateLegameClasse(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tr_tit_cla metodo updateLegameClasse invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tr_tit_cla",Tr_tit_cla.class);

        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizioneArrivo));
        buildUpdate.addProperty("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistemaArrivo));
        buildUpdate.addProperty("classe",this.getParametro().get(KeyParameter.XXXclasseArrivo));

        buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
        buildUpdate.addWhere("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizionePartenza),"=");
        buildUpdate.addWhere("cd_sistema",this.getParametro().get(KeyParameter.XXXcd_sistemaPartenza),"=");

        String cl = this.getParametro().get(KeyParameter.XXXclassePartenza).toString();
        cl = ValidazioneDati.fillRight(cl, ' ', 30);
        buildUpdate.addWhere("classe",cl,"=");


        int query = buildUpdate.executeUpdate();
		if (query < 1)
			throw new InfrastructureException("Errore SQL: Nessuna riga aggiornata");
        this.commitTransaction();
        this.closeSession();

    }

    public int contaTitoliCollegatiClasse(HashMap opzioni) throws InfrastructureException {
    	Session session = this.getSession();
        this.beginTransaction();

        Query query = session.getNamedQuery("contaTitoliCollegatiClasse");
        query.setString("sistema", (String) this.getParametro().get(KeyParameter.XXXcd_sistema));
        query.setString("edizione", (String) this.getParametro().get(KeyParameter.XXXcd_edizione));
        query.setString("simbolo", (String) this.getParametro().get(KeyParameter.XXXclasse));

        Number result = (Number) query.uniqueResult();

        this.commitTransaction();
        this.closeSession();

        return result.intValue();

    }

    public int contaTitoliCollegatiClasseBib(HashMap opzioni) throws InfrastructureException {
    	Session session = this.getSession();
        this.beginTransaction();

        SbnUserType user = (SbnUserType) this.getParametro().get(KeyParameter.XXXcd_utente_amm);

        Query query = session.getNamedQuery("contaTitoliCollegatiClasseBib");
        query.setString("sistema", (String) this.getParametro().get(KeyParameter.XXXcd_sistema));
        query.setString("edizione", (String) this.getParametro().get(KeyParameter.XXXcd_edizione));
        query.setString("simbolo", (String) this.getParametro().get(KeyParameter.XXXclasse));
        query.setString("codPolo", user.getBiblioteca().substring(0, 3));
        query.setString("codBib", user.getBiblioteca().substring(3));

        Number result = (Number) query.uniqueResult();

        this.commitTransaction();
        this.closeSession();

        return result.intValue();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_tit_cla.class;
	}
}
