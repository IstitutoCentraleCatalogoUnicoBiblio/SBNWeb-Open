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
import it.finsiel.sbn.polo.orm.Tb_audiovideo;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Category;
import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * Classe Tb_audiovideoResult
 * estende la classe DBTavola
 * <p>
 * Classe che mappa la tavola Tb_audiovideoResult
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
public class Tb_audiovideoResult extends TableDao {

	//file di log
	static Category log = Category.getInstance ("iccu.box.database.Tb_audiovideoResult");

	private Tb_audiovideo tb_audiovideo;


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_audiovideoResult () {
		super ();
	}


	/**
	 * Costruttore della classe - restituisce un'istanza della classe
	 * @param void
	 */
	public Tb_audiovideoResult ( Tb_audiovideo tb_audiovideo ) {
		super ();
		this.tb_audiovideo = tb_audiovideo;
		valorizzaParametro(tb_audiovideo.leggiAllParametro());
	}

	public Tb_audiovideo getTb_audiovideo() {
		return tb_audiovideo;
	}


	public void setTb_audiovideo(Tb_audiovideo tb_audiovideo) {
		this.tb_audiovideo = tb_audiovideo;
	}


	public void selectPerKey() throws InfrastructureException {
		try {
			HashMap opzioni = this.getParametro();
			String bid = (String) opzioni.get(Tb_titoloCommonDao.XXXbid);

			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TB_AUDIOVIDEO_selectPerKey");

			filter.setParameter(Tb_titoloCommonDao.XXXbid, bid);

			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);

			List<Tb_audiovideo> result = new ArrayList<Tb_audiovideo>(this.basicCriteria.list());

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
		BuilderUpdate buildUpdate = new BuilderUpdate(session, "Tb_audiovideo", Tb_audiovideo.class);

		HashMap params = this.getParametro();
		buildUpdate.addProperty("cd_livello", params.get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("tp_mater_audiovis", params.get(KeyParameter.XXXtp_mater_audiovis));
		buildUpdate.addProperty("lunghezza", params.get(KeyParameter.XXXlunghezza));
		buildUpdate.addProperty("cd_colore", params.get(KeyParameter.XXXcd_colore));
		buildUpdate.addProperty("cd_suono", params.get(KeyParameter.XXXcd_suono));
		buildUpdate.addProperty("tp_media_suono", params.get(KeyParameter.XXXtp_media_suono));
		buildUpdate.addProperty("cd_dimensione", params.get(KeyParameter.XXXcd_dimensione));
		buildUpdate.addProperty("cd_forma_video", params.get(KeyParameter.XXXcd_forma_video));
		buildUpdate.addProperty("cd_tecnica", params.get(KeyParameter.XXXcd_tecnica));
		buildUpdate.addProperty("tp_formato_film", params.get(KeyParameter.XXXtp_formato_film));
		buildUpdate.addProperty("cd_mat_accomp_1", params.get(KeyParameter.XXXcd_mat_accomp_1));
		buildUpdate.addProperty("cd_mat_accomp_2", params.get(KeyParameter.XXXcd_mat_accomp_2));
		buildUpdate.addProperty("cd_mat_accomp_3", params.get(KeyParameter.XXXcd_mat_accomp_3));
		buildUpdate.addProperty("cd_mat_accomp_4", params.get(KeyParameter.XXXcd_mat_accomp_4));
		buildUpdate.addProperty("cd_forma_regist", params.get(KeyParameter.XXXcd_forma_regist));
		buildUpdate.addProperty("tp_formato_video", params.get(KeyParameter.XXXtp_formato_video));
		buildUpdate.addProperty("cd_materiale_base", params.get(KeyParameter.XXXcd_materiale_base));
		buildUpdate.addProperty("cd_supporto_second", params.get(KeyParameter.XXXcd_supporto_second));
		buildUpdate.addProperty("cd_broadcast", params.get(KeyParameter.XXXcd_broadcast));
		buildUpdate.addProperty("tp_generazione", params.get(KeyParameter.XXXtp_generazione));
		buildUpdate.addProperty("cd_elementi", params.get(KeyParameter.XXXcd_elementi));
		buildUpdate.addProperty("cd_categ_colore", params.get(KeyParameter.XXXcd_categ_colore));
		buildUpdate.addProperty("cd_polarita", params.get(KeyParameter.XXXcd_polarita));
		buildUpdate.addProperty("cd_pellicola", params.get(KeyParameter.XXXcd_pellicola));
		buildUpdate.addProperty("tp_suono", params.get(KeyParameter.XXXtp_suono));
		buildUpdate.addProperty("tp_stampa_film", params.get(KeyParameter.XXXtp_stampa_film));
		buildUpdate.addProperty("cd_deteriore", params.get(KeyParameter.XXXcd_deteriore));
		buildUpdate.addProperty("cd_completo", params.get(KeyParameter.XXXcd_completo));
		buildUpdate.addProperty("dt_ispezione", params.get(KeyParameter.XXXdt_ispezione));
		buildUpdate.addProperty("ute_var", params.get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var", now());
		buildUpdate.addProperty("fl_canc", params.get(KeyParameter.XXXfl_canc));


		buildUpdate.addWhere("bid", params.get(KeyParameter.XXXbid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}


	public void insert(Object opzioni) throws InfrastructureException {
        log.debug("tb_audiovideo metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
        tb_audiovideo.setTS_INS(now);
        tb_audiovideo.setTS_VAR(now);

        session.saveOrUpdate(this.tb_audiovideo);
        this.commitTransaction();
        this.closeSession();
	}

    public void updateCancellaPerBid(Object opzioni) throws InfrastructureException {

        log.debug("tb_audiovideo metodo updateCancellaPerBid invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"tb_audiovideo", Tb_audiovideo.class);

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
		return Tb_audiovideo.class;
	}
}
