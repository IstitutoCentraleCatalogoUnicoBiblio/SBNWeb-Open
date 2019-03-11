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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_titoloCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_risorsa_elettr;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Category;
import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * Classe Tb_risorsa_elettrResult
 * estende la classe DBTavola
 * <p>
 * Classe che mappa la tavola Tb_risorsa_elettrResult
 * ATTENZIONE! QUESTA CLASSE E' STATA GENERATA AUTOMATICAMENTE. NESSUNA MODIFICA DEVE
 * ESSERE APPORTATA MANUALMENTE, PERCHE' SARA' PERSA IN FUTURO.
 * OGNI AGGIUNTA MANUALE NON E' PERTANTO POSSIBILE.
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 11/6/2014
 */
public class Tb_risorsa_elettrResult extends TableDao {

	//file di log
	static Category log = Category.getInstance ("iccu.box.database.Tb_risorsa_elettrResult");
	private Tb_risorsa_elettr tb_risorsa_elettr;

	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_risorsa_elettrResult () {
		super ();
	}


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_risorsa_elettrResult (Tb_risorsa_elettr risorsa_elettr) {
		super ();
		this.tb_risorsa_elettr = risorsa_elettr;
		valorizzaParametro(risorsa_elettr.leggiAllParametro());
	}

	public Tb_risorsa_elettr getTb_risorsa_elettr() {
		return tb_risorsa_elettr;
	}


	public void setTb_risorsa_elettr(Tb_risorsa_elettr tb_risorsa_elettr) {
		this.tb_risorsa_elettr = tb_risorsa_elettr;
	}


	public void selectPerKey() throws InfrastructureException {
		try {
			HashMap opzioni = this.getParametro();
			String bid = (String) opzioni.get(Tb_titoloCommonDao.XXXbid);

			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_RISORSA_ELETTR_selectPerKey");

			filter.setParameter(Tb_titoloCommonDao.XXXbid, bid);

			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);

			List<Tb_risorsa_elettr> result = new ArrayList<Tb_risorsa_elettr>(this.basicCriteria.list());

			this.commitTransaction();
			this.closeSession();
			this.valorizzaElencoRisultati(result);

		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	public void update(Object opzioni) throws InfrastructureException {
        log.debug("tb_audiovideo metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tb_risorsa_elettr", Tb_risorsa_elettr.class);

		HashMap params = this.getParametro();
		buildUpdate.addProperty("cd_livello", params.get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("tp_risorsa", params.get(KeyParameter.XXXtp_risorsa));
		buildUpdate.addProperty("cd_designazione", params.get(KeyParameter.XXXcd_designazione));
		buildUpdate.addProperty("cd_colore", params.get(KeyParameter.XXXcd_colore));
		buildUpdate.addProperty("cd_dimensione", params.get(KeyParameter.XXXcd_dimensione));
		buildUpdate.addProperty("cd_suono", params.get(KeyParameter.XXXcd_suono));
		buildUpdate.addProperty("cd_bit_immagine", params.get(KeyParameter.XXXcd_bit_immagine));
		buildUpdate.addProperty("cd_formato_file", params.get(KeyParameter.XXXcd_formato_file));
		buildUpdate.addProperty("cd_qualita", params.get(KeyParameter.XXXcd_qualita));
		buildUpdate.addProperty("cd_origine", params.get(KeyParameter.XXXcd_origine));
		buildUpdate.addProperty("cd_compressione", params.get(KeyParameter.XXXcd_compressione));
		buildUpdate.addProperty("cd_riformattazione", params.get(KeyParameter.XXXcd_riformattazione));
		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("bid", params.get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}


	public void insert(Object opzioni) throws InfrastructureException {
        log.debug("risorsa_elettr metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_risorsa_elettr.setTS_INS(now);
		tb_risorsa_elettr.setTS_VAR(now);

        session.saveOrUpdate(this.tb_risorsa_elettr);
        this.commitTransaction();
        this.closeSession();
	}

    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {

        log.debug("tb_risorsa_elettr metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"tb_risorsa_elettr", Tb_risorsa_elettr.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }


	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_risorsa_elettr.class;
	}
}
