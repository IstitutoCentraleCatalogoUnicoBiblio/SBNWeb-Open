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
import it.finsiel.sbn.polo.orm.Tb_titset_1;
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
 * Classe Tb_titset_1Result
 * estende la classe DBTavola
 * <p>
 * Classe che mappa la tavola Tb_titset_1Result
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
public class Tb_titset_1Result extends TableDao {

	//Attributi

	private Tb_titset_1 tb_titset_1;

	//file di log
	static Category log = Category.getInstance ("iccu.box.database.Tb_titset_1Result");


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_titset_1Result () {
		super ();
	}


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_titset_1Result ( Tb_titset_1 tb_titset_1 ) {
		super ();
		this.tb_titset_1 = tb_titset_1 ;
		valorizzaParametro(tb_titset_1.leggiAllParametro());
	}

	/** Restituisce il bean */
	public Tb_titset_1 getTb_titset_1() {
		return tb_titset_1;
	}

	/** Setta il bean */
	public void setTb_titset_1( Tb_titset_1 tb_titset_1 ) {
		this.tb_titset_1 = tb_titset_1;
	}


	public void selectPerKey() throws InfrastructureException {
		try {
			HashMap opzioni = this.getParametro();
			String bid = (String) opzioni.get(Tb_titoloCommonDao.XXXbid);
			log.debug("select titolo area 0 per ID: " + bid);

			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_TITSET_1_selectPerKey");

			filter.setParameter(Tb_titoloCommonDao.XXXbid, bid);

			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);

			List<Tb_titset_1> result = new ArrayList<Tb_titset_1>(this.basicCriteria.list());

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
        log.debug("Tb_titset_1 metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titset_1", Tb_titset_1.class);

		Map params = this.getParametro();
		buildUpdate.addProperty("s105_tp_testo_letterario", params.get(KeyParameter.XXXs105_tp_testo_letterario));
		buildUpdate.addProperty("s140_tp_testo_letterario", params.get(KeyParameter.XXXs140_tp_testo_letterario));

		buildUpdate.addProperty("s125_indicatore_testo", params.get(KeyParameter.XXXs125_indicatore_testo));

		buildUpdate.addProperty("s181_tp_forma_contenuto_1", params.get(KeyParameter.XXXs181_tp_forma_contenuto_1));
		buildUpdate.addProperty("s181_cd_tipo_contenuto_1", params.get(KeyParameter.XXXs181_cd_tipo_contenuto_1));
		buildUpdate.addProperty("s181_cd_movimento_1", params.get(KeyParameter.XXXs181_cd_movimento_1));
		buildUpdate.addProperty("s181_cd_dimensione_1", params.get(KeyParameter.XXXs181_cd_dimensione_1));
		buildUpdate.addProperty("s181_cd_sensoriale_1_1", params.get(KeyParameter.XXXs181_cd_sensoriale_1_1));
		buildUpdate.addProperty("s181_cd_sensoriale_2_1", params.get(KeyParameter.XXXs181_cd_sensoriale_2_1));
		buildUpdate.addProperty("s181_cd_sensoriale_3_1", params.get(KeyParameter.XXXs181_cd_sensoriale_3_1));
		buildUpdate.addProperty("s181_tp_forma_contenuto_2", params.get(KeyParameter.XXXs181_tp_forma_contenuto_2));
		buildUpdate.addProperty("s181_cd_tipo_contenuto_2", params.get(KeyParameter.XXXs181_cd_tipo_contenuto_2));
		buildUpdate.addProperty("s181_cd_movimento_2", params.get(KeyParameter.XXXs181_cd_movimento_2));
		buildUpdate.addProperty("s181_cd_dimensione_2", params.get(KeyParameter.XXXs181_cd_dimensione_2));
		buildUpdate.addProperty("s181_cd_sensoriale_1_2", params.get(KeyParameter.XXXs181_cd_sensoriale_1_2));
		buildUpdate.addProperty("s181_cd_sensoriale_2_2", params.get(KeyParameter.XXXs181_cd_sensoriale_2_2));
		buildUpdate.addProperty("s181_cd_sensoriale_3_2", params.get(KeyParameter.XXXs181_cd_sensoriale_3_2));
		buildUpdate.addProperty("s182_tp_mediazione_1", params.get(KeyParameter.XXXs182_tp_mediazione_1));
		buildUpdate.addProperty("s182_tp_mediazione_2", params.get(KeyParameter.XXXs182_tp_mediazione_2));
		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));

		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		//almaviva5_20150923 sbnmarc v2.01
		buildUpdate.addProperty("s183_tp_supporto_1", params.get(KeyParameter.XXXs183_tp_supporto_1));
		buildUpdate.addProperty("s183_tp_supporto_2", params.get(KeyParameter.XXXs183_tp_supporto_2));

		//almaviva5_20170726 sbnmarc v2.03
		buildUpdate.addProperty("s210_ind_pubblicato", params.get(KeyParameter.XXXs210_ind_pubblicato));

		buildUpdate.addWhere("bid", params.get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}


	public void insert(Object opzioni) throws InfrastructureException {
        log.debug("Tb_titset_1 metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tb_titset_1.setTS_INS(ts);
        tb_titset_1.setTS_VAR(ts);
        session.saveOrUpdate(this.tb_titset_1);
        this.commitTransaction();
        this.closeSession();
	}

    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {

        log.debug("Tb_titset_1 metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titset_1", getTarget() );

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
		return Tb_titset_1.class;
	}
}
