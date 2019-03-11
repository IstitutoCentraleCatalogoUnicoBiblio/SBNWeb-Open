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
import it.finsiel.sbn.polo.orm.Tb_titset_2;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * Classe Tb_titset_2Result
 * estende la classe DBTavola
 * <p>
 * Classe che mappa la tavola Tb_titset_2Result
 * ATTENZIONE! QUESTA CLASSE E' STATA GENERATA AUTOMATICAMENTE. NESSUNA MODIFICA DEVE
 * ESSERE APPORTATA MANUALMENTE, PERCHE' SARA' PERSA IN FUTURO.
 * OGNI AGGIUNTA MANUALE NON E' PERTANTO POSSIBILE.
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 19/5/2016
 */
public class Tb_titset_2Result extends TableDao {

	//Attributi

	private Tb_titset_2 tb_titset_2;

	//file di log
	static Category log = Category.getInstance ("iccu.box.database.Tb_titset_2Result");


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_titset_2Result () {
		super ();
	}


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_titset_2Result ( Tb_titset_2 tb_titset_2 ) {
		super ();
		this.tb_titset_2 = tb_titset_2 ;
		valorizzaParametro(tb_titset_2.leggiAllParametro());
	}

	/** Restituisce il bean */
	public Tb_titset_2 getTb_titset_2() {
		return tb_titset_2;
	}

	/** Setta il bean */
	public void setTb_titset_2( Tb_titset_2 tb_titset_2 ) {
		this.tb_titset_2 = tb_titset_2;
	}

	public void selectPerKey() throws InfrastructureException {
		try {
			HashMap opzioni = this.getParametro();
			String bid = (String) opzioni.get(Tb_titoloCommonDao.XXXbid);
			log.debug("select titset2 per ID: " + bid);

			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_TITSET_2_selectPerKey");

			filter.setParameter(Tb_titoloCommonDao.XXXbid, bid);

			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);

			List<Tb_titset_2> result = new ArrayList<Tb_titset_2>(this.basicCriteria.list());

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

	public void insert(Object opzioni) throws InfrastructureException {
        log.debug("Tb_titset_2 metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tb_titset_2.setTS_INS(ts);
        tb_titset_2.setTS_VAR(ts);
        session.saveOrUpdate(this.tb_titset_2);
        this.commitTransaction();
        this.closeSession();
	}

	public void update(Object opzioni) throws InfrastructureException {
        log.debug("Tb_titset_2 metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tb_titset_2", getTarget() );

		Map params = this.getParametro();
		//buildUpdate.addProperty("s231_numero_sezione", params.get(KeyParameter.XXXs231_numero_sezione));
		//buildUpdate.addProperty("s231_nome_sezione", params.get(KeyParameter.XXXs231_nome_sezione));
		buildUpdate.addProperty("s231_forma_opera", params.get(KeyParameter.XXXs231_forma_opera));
		buildUpdate.addProperty("s231_data_opera", params.get(KeyParameter.XXXs231_data_opera));
		//buildUpdate.addProperty("s231_paese_opera", params.get(KeyParameter.XXXs231_paese_opera));
		//buildUpdate.addProperty("s231_lingua_originale", params.get(KeyParameter.XXXs231_lingua_originale));
		buildUpdate.addProperty("s231_altre_caratteristiche", params.get(KeyParameter.XXXs231_altre_caratteristiche));

		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("bid", params.get(KeyParameter.XXXbid), "=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}

    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {

        log.debug("Tb_titset_2 metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titset_2", getTarget() );

		buildUpdate.addProperty("ute_var", this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", "S");

		buildUpdate.addWhere("bid", this.getParametro().get(KeyParameter.XXXbid), "=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
    }


	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_titset_2.class;
	}

}
