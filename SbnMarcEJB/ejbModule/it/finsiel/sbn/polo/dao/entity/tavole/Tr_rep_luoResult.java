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
import it.finsiel.sbn.polo.orm.Tr_rep_luo;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Classe Tr_rep_luoResult
 * estende la classe DBTavola
 * <p>
 * Classe che mappa la tavola Tr_rep_luoResult
 * ATTENZIONE! QUESTA CLASSE E' STATA GENERATA AUTOMATICAMENTE. NESSUNA MODIFICA DEVE
 * ESSERE APPORTATA MANUALMENTE, PERCHE' SARA' PERSA IN FUTURO.
 * OGNI AGGIUNTA MANUALE NON E' PERTANTO POSSIBILE.
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 4/5/2015
 */
public class Tr_rep_luoResult extends TableDao {

	//Attributi

	private Tr_rep_luo tr_rep_luo;

	//file di log
	static Category log = Category.getInstance ("iccu.box.database.Tr_rep_luoResult");


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tr_rep_luoResult () {
		super ();
	}


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tr_rep_luoResult ( Tr_rep_luo tr_rep_luo ) {
		super ();
		this.valorizzaParametro(tr_rep_luo.leggiAllParametro());
		this.tr_rep_luo = tr_rep_luo ;
	}

	/** Restituisce il bean */
	public Tr_rep_luo getTr_rep_luo() {
		return tr_rep_luo;
	}

	/** Setta il bean */
	public void setTr_rep_luo( Tr_rep_luo tr_rep_luo ) {
		this.tr_rep_luo = tr_rep_luo;
	}


	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tr_rep_luo.class;
	}

	public List<Tr_rep_luo> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());

			basicCriteria.add(Restrictions.ne("FL_CANC", "S"));
			basicCriteria.add(Restrictions.eq("ID_REPERTORIO", opzioni.get(XXXid_repertorio)));
			basicCriteria.add(Restrictions.eq("LID", opzioni.get(XXXlid)));

			myOpzioni.remove(XXXid_repertorio);
			myOpzioni.remove(XXXlid);
			List<Tr_rep_luo> result = this.basicCriteria.list();
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

	public List<Tr_rep_luo> selectPerKeyCancellato(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());

			basicCriteria.add(Restrictions.eq("ID_REPERTORIO", opzioni.get(XXXid_repertorio)));
			basicCriteria.add(Restrictions.eq("LID", opzioni.get(XXXlid)));

			myOpzioni.remove(XXXid_repertorio);
			myOpzioni.remove(XXXlid);
			List<Tr_rep_luo> result = this.basicCriteria.list();
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

	public boolean insert(Object opzioni) throws InfrastructureException {
        log.debug("Tr_rep_luo metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
        tr_rep_luo.setTS_INS(now);
        tr_rep_luo.setTS_VAR(now);
        session.save(this.tr_rep_luo);
        this.commitTransaction();
        this.closeSession();

		return true;
	}

	public boolean update(Object opzioni) throws InfrastructureException {
        log.debug("Tr_rep_luo metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tr_rep_luo", Tr_rep_luo.class);

		Map params = this.getParametro();
		buildUpdate.addProperty("NOTA_REP_LUO", params.get(KeyParameter.XXXnota_rep_luo));
		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("id_repertorio",params.get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("lid",params.get(KeyParameter.XXXlid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

        return true;
	}

	public void cancellaLegamiRepertorio(Object opzioni) throws InfrastructureException {
        log.debug("Tr_rep_luo metodo cancellaLegamiRepertorio invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tr_rep_luo", Tr_rep_luo.class);

		buildUpdate.addProperty("ute_var", this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", "S");

		buildUpdate.addWhere("id_repertorio",this.getParametro().get(KeyParameter.XXXid_repertorio),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire
	// i campi nota informativa , nota catalogatore e legame a repertori
	public List<Tr_rep_luo> selectPerLuogo(HashMap opzioni)
		throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TR_REP_LUO_selectPerLuogo");

			filter.setParameter(XXXlid, opzioni.get(XXXlid));

			myOpzioni.remove(XXXlid);
		    this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
		            "Tr_rep_luo",
		            this.basicCriteria, session);

			List<Tr_rep_luo> result = this.basicCriteria.list();
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


}
