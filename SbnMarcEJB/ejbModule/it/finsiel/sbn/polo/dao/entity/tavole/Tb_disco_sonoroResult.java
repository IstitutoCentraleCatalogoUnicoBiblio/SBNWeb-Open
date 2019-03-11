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
import it.finsiel.sbn.polo.orm.Tb_disco_sonoro;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Category;
import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * Classe Tb_disco_sonoroResult
 * estende la classe DBTavola
 * <p>
 * Classe che mappa la tavola Tb_disco_sonoroResult
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
public class Tb_disco_sonoroResult extends TableDao {

	//file di log
	static Category log = Category.getInstance ("iccu.box.database.Tb_disco_sonoroResult");

	private Tb_disco_sonoro  tb_disco_sonoro;


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_disco_sonoroResult () {
		super ();
	}


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_disco_sonoroResult ( Tb_disco_sonoro tb_disco_sonoro ) {
		super ();
		this.tb_disco_sonoro = tb_disco_sonoro;
		valorizzaParametro(tb_disco_sonoro.leggiAllParametro());
	}


	public Tb_disco_sonoro getTb_disco_sonoro() {
		return tb_disco_sonoro;
	}


	public void setTb_disco_sonoro(Tb_disco_sonoro tb_disco_sonoro) {
		this.tb_disco_sonoro = tb_disco_sonoro;
	}


	public void selectPerKey() throws InfrastructureException {
		try {
			HashMap opzioni = this.getParametro();
			String bid = (String) opzioni.get(Tb_titoloCommonDao.XXXbid);

			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_DISCO_SONORO_selectPerKey");

			filter.setParameter(Tb_titoloCommonDao.XXXbid, bid);

			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);

			List<Tb_disco_sonoro> result = this.basicCriteria.list();

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
        log.debug("Tb_disco_sonoro metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tb_disco_sonoro", Tb_disco_sonoro.class);

		HashMap params = this.getParametro();
		buildUpdate.addProperty("cd_livello", params.get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("cd_forma", params.get(KeyParameter.XXXcd_forma));
		buildUpdate.addProperty("cd_velocita", params.get(KeyParameter.XXXcd_velocita));
		buildUpdate.addProperty("tp_suono", params.get(KeyParameter.XXXtp_suono));
		buildUpdate.addProperty("cd_pista", params.get(KeyParameter.XXXcd_pista));
		buildUpdate.addProperty("cd_dimensione", params.get(KeyParameter.XXXcd_dimensione));
		buildUpdate.addProperty("cd_larg_nastro", params.get(KeyParameter.XXXcd_larg_nastro));
		buildUpdate.addProperty("cd_configurazione", params.get(KeyParameter.XXXcd_configurazione));
		buildUpdate.addProperty("cd_mater_accomp_1", params.get(KeyParameter.XXXcd_mater_accomp_1));
		buildUpdate.addProperty("cd_mater_accomp_2", params.get(KeyParameter.XXXcd_mater_accomp_2));
		buildUpdate.addProperty("cd_mater_accomp_3", params.get(KeyParameter.XXXcd_mater_accomp_3));
		buildUpdate.addProperty("cd_mater_accomp_4", params.get(KeyParameter.XXXcd_mater_accomp_4));
		buildUpdate.addProperty("cd_mater_accomp_5", params.get(KeyParameter.XXXcd_mater_accomp_5));
		buildUpdate.addProperty("cd_mater_accomp_6", params.get(KeyParameter.XXXcd_mater_accomp_6));
		buildUpdate.addProperty("cd_tecnica_regis", params.get(KeyParameter.XXXcd_tecnica_regis));
		buildUpdate.addProperty("cd_riproduzione", params.get(KeyParameter.XXXcd_riproduzione));
		buildUpdate.addProperty("tp_disco", params.get(KeyParameter.XXXtp_disco));
		buildUpdate.addProperty("tp_taglio", params.get(KeyParameter.XXXtp_taglio));
		buildUpdate.addProperty("tp_materiale", params.get(KeyParameter.XXXtp_materiale));
		buildUpdate.addProperty("durata", params.get(KeyParameter.XXXdurata));
		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("bid", params.get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}


	public void insert(Object opzioni) throws InfrastructureException {
        log.debug("Tb_disco_sonoro metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
        tb_disco_sonoro.setTS_INS(now);
        tb_disco_sonoro.setTS_VAR(now);
        session.saveOrUpdate(this.tb_disco_sonoro);
        this.commitTransaction();
        this.closeSession();
	}

    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {

        log.debug("tb_disco_sonoro metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "tb_disco_sonoro", Tb_disco_sonoro.class);

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
		return Tb_disco_sonoro.class;
	}
}
