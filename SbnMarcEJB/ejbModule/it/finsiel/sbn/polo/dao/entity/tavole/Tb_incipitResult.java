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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_incipitCommonDao;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_incipit;
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
public class Tb_incipitResult extends Tb_incipitCommonDao {

    private Tb_incipit tb_incipit;

    public Tb_incipitResult(Tb_incipit tb_incipit) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_incipit.leggiAllParametro());
        this.tb_incipit = tb_incipit;
    }

	/**
	 * <statement nome="selectPerKey" tipo="select" id="01"> <fisso> WHERE bid =
	 * XXXbid AND numero_mov = XXXnumero_mov AND numero_p_mov = XXXnumero_p_mov
	 * </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_incipit> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_INCIPIT_selectPerKey");

			filter.setParameter(Tb_incipitCommonDao.XXXbid, opzioni
					.get(Tb_incipitCommonDao.XXXbid));
			filter.setParameter(Tb_incipitCommonDao.XXXnumero_mov, opzioni
					.get(Tb_incipitCommonDao.XXXnumero_mov));
			filter.setParameter(Tb_incipitCommonDao.XXXnumero_p_mov, opzioni
					.get(Tb_incipitCommonDao.XXXnumero_p_mov));

			myOpzioni.remove(Tb_incipitCommonDao.XXXbid);
			myOpzioni.remove(Tb_incipitCommonDao.XXXnumero_mov);
			myOpzioni.remove(Tb_incipitCommonDao.XXXnumero_p_mov);
			List<Tb_incipit> result = this.basicCriteria
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
	 * <statement nome="selectPerBid" tipo="select" id="05_taymer"> <fisso>
	 * WHERE bid = XXXbid AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_incipit> selectPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_INCIPIT_selectPerBid");

			filter.setParameter(Tb_incipitCommonDao.XXXbid, opzioni
					.get(Tb_incipitCommonDao.XXXbid));

			myOpzioni.remove(Tb_incipitCommonDao.XXXbid);
			List<Tb_incipit> result = this.basicCriteria
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
	 * <statement nome="verificaEsistenza" tipo="select" id="06_taymer"> <fisso>
	 * WHERE bid = XXXbid AND numero_mov = XXXnumero_mov AND numero_p_mov =
	 * XXXnumero_p_mov </fisso> </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_incipit> verificaEsistenza(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TB_INCIPIT_verificaEsistenza");

			filter.setParameter(Tb_incipitCommonDao.XXXbid, opzioni
					.get(Tb_incipitCommonDao.XXXbid));
			filter.setParameter(Tb_incipitCommonDao.XXXnumero_mov, opzioni
					.get(Tb_incipitCommonDao.XXXnumero_mov));
			filter.setParameter(Tb_incipitCommonDao.XXXnumero_p_mov, opzioni
					.get(Tb_incipitCommonDao.XXXnumero_p_mov));

			myOpzioni.remove(Tb_incipitCommonDao.XXXbid);
			myOpzioni.remove(Tb_incipitCommonDao.XXXnumero_mov);
			myOpzioni.remove(Tb_incipitCommonDao.XXXnumero_p_mov);
			List<Tb_incipit> result = this.basicCriteria
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
        log.debug("Tb_incipit metodo insert invocato ");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		tb_incipit.setTS_INS(now);
        tb_incipit.setTS_VAR(now);
        session.save(this.tb_incipit);
        this.commitTransaction();
        this.closeSession();


	}

	/**
	 * 	<statement nome="update" tipo="update" id="04_taymer">
		<fisso>
			UPDATE Tb_incipit
			 SET
			  ute_var = XXXute_var ,
			  numero_comp = XXXnumero_comp ,
			  nome_personaggio = XXXnome_personaggio ,
			  numero_p_mov = XXXnumero_p_mov ,
			  ds_contesto = XXXds_contesto ,
			  cd_forma = XXXcd_forma ,
			  ts_var = SYSTIMESTAMP ,
			  alterazione = XXXalterazione ,
			  tempo_mus = XXXtempo_mus ,
			  chiave_mus = XXXchiave_mus ,
			  cd_tonalita = XXXcd_tonalita ,
			  tp_indicatore = XXXtp_indicatore ,
			  bid_letterario = XXXbid_letterario ,
			  misura = XXXmisura ,
			  fl_canc = ' ' ,
			  registro_mus = XXXregistro_mus
			WHERE
			  bid = XXXbid
			  AND numero_mov = XXXnumero_mov
			  AND numero_p_mov = XXXnumero_p_mov
		</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_incipit metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_incipit", getTarget() );

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("numero_comp",this.getParametro().get(KeyParameter.XXXnumero_comp));
		buildUpdate.addProperty("nome_personaggio",this.getParametro().get(KeyParameter.XXXnome_personaggio));
		buildUpdate.addProperty("numero_p_mov",this.getParametro().get(KeyParameter.XXXnumero_p_mov));
		buildUpdate.addProperty("ds_contesto",this.getParametro().get(KeyParameter.XXXds_contesto));
		buildUpdate.addProperty("cd_forma",this.getParametro().get(KeyParameter.XXXcd_forma));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("alterazione",this.getParametro().get(KeyParameter.XXXalterazione));
		buildUpdate.addProperty("tempo_mus",this.getParametro().get(KeyParameter.XXXtempo_mus));
		buildUpdate.addProperty("chiave_mus",this.getParametro().get(KeyParameter.XXXchiave_mus));
		buildUpdate.addProperty("cd_tonalita",this.getParametro().get(KeyParameter.XXXcd_tonalita));
		buildUpdate.addProperty("tp_indicatore",this.getParametro().get(KeyParameter.XXXtp_indicatore));
		buildUpdate.addProperty("bid_letterario",this.getParametro().get(KeyParameter.XXXbid_letterario));
		buildUpdate.addProperty("misura",this.getParametro().get(KeyParameter.XXXmisura));

		// Manutenzione Settembre 2016 Bug esercizio 0006270 almaviva2: Mancato salvataggio strumento per incipit
		// Viene inserita la valorizzazione del campo Strumento anche nel caso in cui il numero delgli strumenti
		// non sia valorizzato (prima si faceva solo per maggiore di 0)
		// ERRORE NELLA VALORIZZAZIONE !!!!!
		buildUpdate.addProperty("registro_mus",this.getParametro().get(KeyParameter.XXXregistro_mus));

		buildUpdate.addProperty("fl_canc"," ");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("numero_mov",this.getParametro().get(KeyParameter.XXXnumero_mov),"=");
		buildUpdate.addWhere("numero_p_mov",this.getParametro().get(KeyParameter.XXXnumero_p_mov),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();


	}

	/**
	 * 	<statement nome="updateCancella" tipo="update" id="06_taymer">
			<fisso>
				UPDATE Tb_incipit
				 SET
				  fl_canc = 'S' ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND fl_canc != 'S'
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void updateCancella(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_incipit metodo updateCancella invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_incipit", getTarget() );

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc","S");

		buildUpdate.addWhere("bid",this.getParametro().get(KeyParameter.XXXbid),"=");
		buildUpdate.addWhere("fl_canc","S","!=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_incipit.class;
	}
}
