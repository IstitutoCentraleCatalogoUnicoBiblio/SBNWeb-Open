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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_parolaCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_parola;
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
public class Tb_parolaResult extends Tb_parolaCommonDao {

    private Tb_parola tb_parola;

    public Tb_parolaResult(Tb_parola tb_parola) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_parola.leggiAllParametro());
        this.tb_parola = tb_parola;

    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01"> <fisso> WHERE
	 * id_parola = XXXid_parola AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_parola> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_PAROLA_selectPerKey");

			filter.setParameter(Tb_parolaCommonDao.XXXid_parola, opzioni
					.get(Tb_parolaCommonDao.XXXid_parola));

			myOpzioni.remove(Tb_parolaCommonDao.XXXid_parola);
			List<Tb_parola> result = this.basicCriteria
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
	 * <statement nome="verificaEsistenzaParola" tipo="select" id="01"> <fisso>
	 * WHERE UPPER (parola) =XXXparola AND mid = XXXmid </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_parola> verificaEsistenzaParola(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_PAROLA_verificaEsistenzaParola");

			filter.setParameter(Tb_parolaCommonDao.XXXid_parola, opzioni
					.get(Tb_parolaCommonDao.XXXid_parola));
			filter
					.setParameter(Tb_parolaCommonDao.XXXmid, opzioni
							.get(Tb_parolaCommonDao.XXXmid));

			myOpzioni.remove(Tb_parolaCommonDao.XXXid_parola);
			myOpzioni.remove(Tb_parolaCommonDao.XXXmid);
			List<Tb_parola> result = this.basicCriteria
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
	 * <statement nome="selectPerNome" tipo="select" id="07"> <fisso> WHERE
	 * UPPER (parola) LIKE '%'||UPPER(XXXparola1)||'%' AND fl_canc != 'S'
	 * </fisso> <opzionale dipende="XXXparola2"> OR UPPER (parola) LIKE
	 * '%'||UPPER(XXXparola2)||'%' </opzionale> <opzionale dipende="XXXparola3">
	 * OR UPPER (parola) LIKE '%'||UPPER(XXXparola3)||'%' </opzionale>
	 * <opzionale dipende="XXXparola4"> OR UPPER (parola) LIKE
	 * '%'||UPPER(XXXparola4)||'%' </opzionale> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_parola> selectPerNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_PAROLA_selectPerNome");

			filter.setParameter(Tb_parolaCommonDao.XXXparola1, opzioni
					.get(Tb_parolaCommonDao.XXXparola1));

			myOpzioni.remove(Tb_parolaCommonDao.XXXparola1);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_parola",
                    this.basicCriteria, session);

			List<Tb_parola> result = this.basicCriteria
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
	 * <statement nome="selectPerMarca" tipo="select" id="03"> <fisso> WHERE mid =
	 * XXXmid AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_parola> selectPerMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_PAROLA_selectPerMarca");

			filter
					.setParameter(Tb_parolaCommonDao.XXXmid, opzioni
							.get(Tb_parolaCommonDao.XXXmid));

			myOpzioni.remove(Tb_parolaCommonDao.XXXmid);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_parola",
                    this.basicCriteria, session);

			List<Tb_parola> result = this.basicCriteria
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
	 * <statement nome="selectParolaMarca" tipo="select" id="03"> <fisso> WHERE
	 * mid = XXXmid AND parola = XXXparola AND fl_canc != 'S' </fisso>
	 * </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_parola> selectParolaMarca(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_PAROLA_selectParolaMarca");

			filter
					.setParameter(Tb_parolaCommonDao.XXXmid, opzioni
							.get(Tb_parolaCommonDao.XXXmid));
			filter.setParameter(Tb_parolaCommonDao.XXXparola, opzioni
					.get(Tb_parolaCommonDao.XXXparola));

			myOpzioni.remove(Tb_parolaCommonDao.XXXmid);
			myOpzioni.remove(Tb_parolaCommonDao.XXXparola);
            this.basicCriteria = Parameter.setOrdinamento(opzioni,
                    "Tb_parola",
                    this.basicCriteria, session);

			List<Tb_parola> result = this.basicCriteria
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
        log.debug("Tb_parola metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_parola.setTS_INS(now);
        tb_parola.setTS_VAR(now);
        session.save(this.tb_parola);
        this.commitTransaction();
        this.closeSession();


	}
/*
 *  <statement nome="delete" tipo="delete" id="02">
            <fisso>
                DELETE FROM Tb_parola
                WHERE
                  id_parola = XXXid_parola
            </fisso>
    </statement>

 */
    public void delete(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_parola metodo delete invocato");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_parola",Tb_parola.class);

        buildUpdate.addWhere("id_parola",this.getParametro().get(KeyParameter.XXXid_parola),"=");
        buildUpdate.addWhere("fl_canc","S","!=");

        int query = buildUpdate.executeDelete();
        this.commitTransaction();
        this.closeSession();

    }

    /**
     * 	<statement nome="updatePerCancella" tipo="update" id="Jenny_06">
			<fisso>
				UPDATE Tb_parola
				 SET
				 ute_var = XXXute_var,
				 ts_var = SYSTIMESTAMP,
				 fl_canc = XXXfl_canc
				WHERE
				  id_parola = XXXid_parola
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updatePerCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_parola metodo updatePerCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_parola",Tb_parola.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("id_parola",this.getParametro().get(KeyParameter.XXXid_parola),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_parola.class;
	}
}
