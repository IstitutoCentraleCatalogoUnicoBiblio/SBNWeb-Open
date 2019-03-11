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
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_nota;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tb_notaResult extends TableDao{

    private final Tb_nota tb_nota;

    public Tb_notaResult(Tb_nota tb_nota) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_nota.leggiAllParametro());
        this.tb_nota = new Tb_nota(tb_nota);
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="01">
		<fisso>
			WHERE
			  bid = XXXbid
			  AND progr_nota = XXXprogr_nota
			  AND tp_nota = XXXtp_nota
			  AND fl_canc != 'S'
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_nota> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NOTA_selectPerKey");

			filter.setParameter(Tb_notaResult.XXXbid, opzioni
					.get(Tb_notaResult.XXXbid));
			filter.setParameter(Tb_notaResult.XXXprogr_nota, opzioni
					.get(Tb_notaResult.XXXprogr_nota));
			filter.setParameter(Tb_notaResult.XXXtp_nota, opzioni
					.get(Tb_notaResult.XXXtp_nota));

			myOpzioni.remove(Tb_notaResult.XXXbid);
			myOpzioni.remove(Tb_notaResult.XXXprogr_nota);
			myOpzioni.remove(Tb_notaResult.XXXtp_nota);

			this.createCriteria(myOpzioni);

			List<Tb_nota> result = this.basicCriteria
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
	<statement nome="selectMaxProg" tipo="select_campi" id="05_taymer">
		<fisso>
			SELECT MAX(progr_nota)+1
			FROM tb_nota
            WHERE
			bid = XXXbid AND tp_nota = XXXtp_nota
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer selectMaxProg(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NOTA_selectMaxProg");

			filter.setParameter(Tb_notaResult.XXXbid, opzioni
					.get(Tb_notaResult.XXXbid));
			filter.setParameter(Tb_notaResult.XXXtp_nota, opzioni
					.get(Tb_notaResult.XXXtp_nota));

			myOpzioni.remove(Tb_notaResult.XXXbid);
			myOpzioni.remove(Tb_notaResult.XXXtp_nota);

			//almaviva5_20131120 #5440
			//this.createCriteria(myOpzioni);

			this.basicCriteria.setProjection(Projections.projectionList().add(Projections.max("PROGR_NOTA")));
			Number result = (Number) basicCriteria.uniqueResult();
			this.commitTransaction();
			this.closeSession();
			return result == null ? 0 : result.intValue();
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="selectPerBid" tipo="select" id="06_taymer">
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
	public List<Tb_nota> selectPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NOTA_selectPerBid");

			filter.setParameter(Tb_notaResult.XXXbid, opzioni
					.get(Tb_notaResult.XXXbid));

			myOpzioni.remove(Tb_notaResult.XXXbid);

			this.createCriteria(myOpzioni);

			List<Tb_nota> result = this.basicCriteria
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

//   GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
	/**
	<statement nome="selectPerBid" tipo="select" id="06_taymer">
		<fisso>
            WHERE
			bid = XXXbid
		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_nota> selectPerBidTutte(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_NOTA_selectPerBidTutte");

			filter.setParameter(Tb_notaResult.XXXbid, opzioni.get(Tb_notaResult.XXXbid));

			myOpzioni.remove(Tb_notaResult.XXXbid);

			this.createCriteria(myOpzioni);

			this.basicCriteria.addOrder(Order.asc("TP_NOTA")).addOrder(Order.asc("PROGR_NOTA"));

			List<Tb_nota> result = this.basicCriteria.list();
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
//  GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

	public void insert() throws InfrastructureException {
        log.debug("Tb_nota metodo insert invocato: " + tb_nota);
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tb_nota.setTS_INS(ts);
        tb_nota.setTS_VAR(ts);
        tb_nota.setFL_CANC(" ");
        session.save(tb_nota);
        this.commitTransaction();
        this.closeSession();
	}

	/**
	 * 	<statement nome="update" tipo="update" id="04">
		<fisso>
			UPDATE Tb_nota
			 SET
			  ds_nota = XXXds_nota ,
			  ts_ins = XXXts_ins ,
			  ute_var = XXXute_var ,
			  ts_var = XXXts_var ,
			  fl_canc = XXXfl_canc ,
			  ute_ins = XXXute_ins ,
			WHERE
			  bid = XXXbid
			  AND progr_nota = XXXprogr_nota
			  AND tp_nota = XXXtp_nota
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
        log.debug("Tb_nota metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_nota",Tb_nota.class);

		buildUpdate.addProperty("ds_nota",this.getParametro().get(KeyParameter.XXXds_nota));
		buildUpdate.addProperty("ts_ins",this.getParametro().get(KeyParameter.XXXts_ins));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("ute_ins",this.getParametro().get(KeyParameter.XXXute_ins));

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("progr_nota",this.getParametro().get(KeyParameter.XXXprogr_nota),"=");
		buildUpdate.addWhere("tp_nota",this.getParametro().get(KeyParameter.XXXtp_nota),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}

	//almaviva5_20090918 #3068
    // Intervento interno Maggio 2016 per trasformare la gestione delle note con la cancellazione
    // fisica come nel protocollo di Indice mentre prima si utilizzava la cancellazione logica;
    // adeguamento anche per EVOLUTIVA della nota 321 con le due varianti di REP e DB in base al
    // campo tipo della nota stesssa
// 	public void deletePerBid(Object opzioni) throws InfrastructureException {
//        log.debug("Tb_nota metodo deletePerBid invocato");
//        Session session = this.getSession();
//        this.beginTransaction();
//		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tb_nota", Tb_nota.class);
//		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
//		buildUpdate.addProperty("ts_var",now());
//		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
//
//		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
//
//		int query = buildUpdate.executeUpdate();
//		this.commitTransaction();
//		this.closeSession();
//    }

 	public void deletePerBid(Object opzioni) throws InfrastructureException {
		try {
			log.debug("Tb_nota metodo deleteFISICAPerBid invocato");
			String bidDaCancellare = (String) this.getParametro().get(KeyParameter.XXXbid);
			Session session = getCurrentSession();
			// Query query = session.createQuery("delete from Tb_nota where bid.id=:bid");
			Query query = session.createQuery("delete from Tb_nota where bid=:bid");
			query.setString("bid", bidDaCancellare);
			//return query.executeUpdate();
			int valore = query.executeUpdate();
			return;

		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}



	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_nota.class;
	}
}
