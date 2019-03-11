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
import it.finsiel.sbn.polo.dao.common.tavole.Ts_proposta_marcCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Ts_note_proposta;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

public class Ts_note_propostaResult extends Ts_proposta_marcCommonDao {

	private Ts_note_proposta ts_note_proposta;
    public Ts_note_propostaResult(Ts_note_proposta ts_note_proposta) throws InfrastructureException  {
		super();
        this.valorizzaParametro(ts_note_proposta.leggiAllParametro());
        this.ts_note_proposta = ts_note_proposta;

		// TODO Auto-generated constructor stub
	}

    /**
        <filter name="TS_NOTE_PROPOSTA_selectPerKey"
                condition="id_proposta = :XXXid_proposta AND progr_risposta = :XXXprogr_risposta "/>
    *
    * @param opzioni
    * @return List
    * @throws InfrastructureException
    */
    public List<Ts_note_proposta> selectPerKey(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TS_NOTE_PROPOSTA_selectPerKey");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXprogr_risposta, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXprogr_risposta));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXprogr_risposta);

            List<Ts_note_proposta> result = this.basicCriteria
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
    <filter name="TS_NOTE_PROPOSTA_selectLastProgRisposta"
            condition="SELECT nvl(MAX(progr_risposta),0) + 1
                      FROM ts_note_proposta
                      WHERE id_proposta = :XXXid_proposta "/>

    *
    * @param opzioni
    * @return List
    * @throws InfrastructureException
    */
    public List<Ts_note_proposta> selectLastProgRisposta(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TS_NOTE_PROPOSTA_selectLastProgRisposta");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ts_note_proposta",
                    this.basicCriteria, session);

            List<Ts_note_proposta> result = this.basicCriteria
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
    <filter name="TS_NOTE_PROPOSTA_selectPropostaPerDestinatario"
            condition="ute_destinatario like :XXXute_destinatario || '%' AND fl_canc != 'S' "/>

    *
    * @param opzioni
    * @return List
    * @throws InfrastructureException
    */
    public List<Ts_note_proposta> selectPropostaPerDestinatario(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TS_NOTE_PROPOSTA_selectPropostaPerDestinatario");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXute_destinatario, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXute_destinatario));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXute_destinatario);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ts_note_proposta",
                    this.basicCriteria, session);

            List<Ts_note_proposta> result = this.basicCriteria
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
    <filter name="TS_NOTE_PROPOSTA_selectNotaPerProposta"
            condition="id_proposta  = :XXXid_proposta AND fl_canc != 'S' "/>

    *
    * @param opzioni
    * @return List
    * @throws InfrastructureException
    */
    public List<Ts_note_proposta> selectNotaPerProposta(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TS_NOTE_PROPOSTA_selectNotaPerProposta");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ts_note_proposta",
                    this.basicCriteria, session);

            List<Ts_note_proposta> result = this.basicCriteria
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
    <filter name="TS_NOTE_PROPOSTA_selectNotaPerPropostaDestinatario"
            condition="id_proposta  = :XXXid_proposta
                      AND ute_destinatario  = :XXXute_destinatario
                      AND fl_canc != 'S' "/>
    *
    * @param opzioni
    * @return List
    * @throws InfrastructureException
    */
    public List<Ts_note_proposta> selectNotaPerPropostaDestinatario(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TS_NOTE_PROPOSTA_selectNotaPerPropostaDestinatario");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta));
            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXute_destinatario, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXute_destinatario));
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ts_note_proposta",
                    this.basicCriteria, session);

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXid_proposta);
            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_note_propostaCommonDao.XXXute_destinatario);

            List<Ts_note_proposta> result = this.basicCriteria
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
     *  <statement nome="update" tipo="update" id="04">
        <fisso>
            UPDATE Ts_note_proposta
             SET
              note_pro = XXXnote_pro ,
              ute_var = XXXute_var ,
              ute_destinatario = XXXute_destinatario ,
              ts_var = SYSTIMESTAMP ,
              fl_canc = XXXfl_canc
            WHERE
              id_proposta = XXXid_proposta
              AND progr_risposta = XXXprogr_risposta
        </fisso>

     */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Ts_note_proposta metodo update invocato ");
        Session session = this.getSession();
        this.beginTransaction();
        BuilderUpdate buildUpdate = new BuilderUpdate(session,"Ts_note_proposta",Ts_note_proposta.class);

        buildUpdate.addProperty("note_pro",this.getParametro().get(KeyParameter.XXXnote_pro));
        buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
        buildUpdate.addProperty("ute_destinatario",this.getParametro().get(KeyParameter.XXXute_destinatario));
        buildUpdate.addProperty("ts_var",now());
        buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));


        buildUpdate.addWhere("id_proposta",this.getParametro().get(KeyParameter.XXXid_proposta),"=");
        buildUpdate.addWhere("progr_risposta",this.getParametro().get(KeyParameter.XXXprogr_risposta),"=");

        int query = buildUpdate.executeUpdate();
        this.commitTransaction();
        this.closeSession();
	}
	/*
     *  <statement nome="insert" tipo="insert" id="03">
        <fisso>
            INSERT INTO Ts_note_proposta
             (
              id_proposta ,
              note_pro ,
              ts_ins ,
              ute_var ,
              ute_destinatario ,
              ts_var ,
              fl_canc ,
              progr_risposta ,
              ute_ins
             )
            VALUES
             (
              XXXid_proposta ,
              XXXnote_pro ,
              SYSTIMESTAMP ,
              XXXute_var ,
              XXXute_destinatario ,
              SYSTIMESTAMP ,
              XXXfl_canc ,
              XXXprogr_risposta ,
              XXXute_ins
             )
        </fisso>
    </statement>

	 */
    public void insert(Object opzioni) throws InfrastructureException
    {
        log.debug("Ts_note_proposta metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		ts_note_proposta.setTS_INS(now);
        ts_note_proposta.setTS_VAR(now);
        session.saveOrUpdate(this.ts_note_proposta);
        this.commitTransaction();
        this.closeSession();
    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Ts_note_proposta.class;
	}

}
