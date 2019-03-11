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
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Ts_proposta_marc;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

public class Ts_proposta_marcResult extends Ts_proposta_marcCommonDao {
	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();

	private Ts_proposta_marc ts_proposta_marc= null;

	public Ts_proposta_marcResult(Ts_proposta_marc ts_proposta_marc)  throws InfrastructureException{
        super();
        this.valorizzaParametro(ts_proposta_marc.leggiAllParametro());
        this.ts_proposta_marc = ts_proposta_marc;
	}





	public boolean update(Object opzioni) {
		// TODO Auto-generated method stub
        log.debug("Ts_proposta_marc metodo update invocato Da implementare");

		return true;
	}

	  public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Ts_proposta_marc metodo insert invocato Da implementare");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp now = now();
		ts_proposta_marc.setDT_INOLTRO(now);
        ts_proposta_marc.setTS_INS(now);
        ts_proposta_marc.setTS_VAR(now);
        session.save(this.ts_proposta_marc);
        this.commitTransaction();
        this.closeSession();

	}
//		public void insert(Object opzioni) throws InfrastructureException {
//			// TODO Auto-generated method stub
//	        log.debug("Tb_luogo metodo insert invocato");
//	        Session session = this.getSession();
//	        this.beginTransaction();
//	        tb_luogo.setTS_INS(now());
//	        tb_luogo.setTS_VAR(now());
//	        session.save(this.tb_luogo);
//	        this.commitTransaction();
//	        this.closeSession();
//	<filter name="TS_PROPOSTA_MARC_selectPerKey"
//		condition="id_proposta = :XXXid_proposta
//    			   AND fl_canc != 'S' "/>
//

	public List<Ts_proposta_marc> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TS_PROPOSTA_MARC_selectPerKey");

			filter.setParameter(Ts_proposta_marcCommonDao.XXXid_proposta, opzioni
					.get(Ts_proposta_marcCommonDao.XXXid_proposta));

			myOpzioni.remove(Ts_proposta_marcCommonDao.XXXid_proposta);
			List<Ts_proposta_marc> result = this.basicCriteria
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
//	<filter name="TS_PROPOSTA_MARC_selectPropostaPerData"
//		condition="fl_canc != 'S' "/>

	public List<Ts_proposta_marc> selectPropostaPerData(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TS_PROPOSTA_MARC_selectPropostaPerData");

			List<Ts_proposta_marc> result = this.basicCriteria.list();
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
//	<filter name="TS_PROPOSTA_MARC_selectPropostaPerIdOggetto"
//		condition="fl_canc != 'S'
//				  AND id_oggetto = :XXXid_oggetto" />

	public List<Ts_proposta_marc> selectPropostaPerIdOggetto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TS_PROPOSTA_MARC_selectPropostaPerIdOggetto");

			filter.setParameter(Ts_proposta_marcCommonDao.XXXid_oggetto,
					opzioni.get(Ts_proposta_marcCommonDao.XXXid_oggetto));

			myOpzioni.remove(Ts_proposta_marcCommonDao.XXXid_oggetto);
			List<Ts_proposta_marc> result = this.basicCriteria
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
//	<filter name="TS_PROPOSTA_MARC_selectPropostaPerMittente"
//		condition="fl_canc != 'S'
//				  AND ute_mittente = :XXXute_mittente" />

	public List<Ts_proposta_marc> selectPropostaPerMittente(
			HashMap opzioni) throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TS_PROPOSTA_MARC_selectPropostaPerMittente");

			filter.setParameter(Ts_proposta_marcCommonDao.XXXute_mittente,
					opzioni.get(Ts_proposta_marcCommonDao.XXXute_mittente));

			myOpzioni.remove(Ts_proposta_marcCommonDao.XXXute_mittente);
			List<Ts_proposta_marc> result = this.basicCriteria
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


    public void aggiornaStatoProposta(Object opzioni) {
        // TODO Auto-generated method stub
        log.debug("Ts_proposta_marc metodo aggiornaStatoProposta invocato Da implementare");

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Ts_proposta_marc.class;
	}

}
