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
import it.finsiel.sbn.polo.dao.common.tavole.Tbf_biblioteca_in_poloCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tbf_biblioteca_in_poloResult extends Tbf_biblioteca_in_poloCommonDao {

    public Tbf_biblioteca_in_poloResult(Tbf_biblioteca_in_polo tb_biblioteca) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_biblioteca.leggiAllParametro());
    }

	/**
	 * <statement nome="selectPerKey" tipo="select"> <fisso> WHERE cd_biblioteca =
	 * XXXcd_biblioteca AND cd_polo = XXXcd_polo AND fl_canc != 'S' </fisso>
	 * </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tbf_biblioteca_in_polo> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TBF_BIBLIOTECA_IN_POLO_selectPerKey");

			filter.setParameter(Tbf_biblioteca_in_poloCommonDao.XXXcd_polo, opzioni
					.get(Tbf_biblioteca_in_poloCommonDao.XXXcd_polo));
			filter.setParameter(Tbf_biblioteca_in_poloCommonDao.XXXcd_biblioteca, opzioni
					.get(Tbf_biblioteca_in_poloCommonDao.XXXcd_biblioteca));

			myOpzioni.remove(Tbf_biblioteca_in_poloCommonDao.XXXcd_polo);
			myOpzioni.remove(Tbf_biblioteca_in_poloCommonDao.XXXcd_biblioteca);
			List<Tbf_biblioteca_in_polo> result = this.basicCriteria
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
	 * <statement nome="selectPerCd_Polo" tipo="select"> <fisso> WHERE cd_polo =
	 * XXXcd_polo AND fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return
	 * @throws InfrastructureException
	 */
	public List<Tbf_biblioteca_in_polo> selectPerCd_Polo(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TBF_BIBLIOTECA_IN_POLO_selectPerCd_Polo");
			filter.setParameter(Tbf_biblioteca_in_poloCommonDao.XXXcd_polo, opzioni
					.get(Tb_autoreResult.XXXcd_polo));
			myOpzioni.remove(Tb_autoreResult.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tbf_biblioteca_in_polo",
                    this.basicCriteria, session);

			List<Tbf_biblioteca_in_polo> result = this.basicCriteria
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
	 * <statement nome="selectAll" tipo="select" id="marco_08"> <fisso> WHERE
	 * fl_canc != 'S' </fisso> </statement>
	 *
	 * @param opzioni
	 * @return
	 * @throws InfrastructureException
	 */
	public List<Tbf_biblioteca_in_polo> selectAll() throws InfrastructureException {
		try {
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			session.enableFilter("TBF_BIBLIOTECA_IN_POLO_selectAll");

			List<Tbf_biblioteca_in_polo> result = this.basicCriteria
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
	 * <statement nome="selectPerAnagrafe" tipo="select" id="Jenny_02"> <fisso>
	 * WHERE cd_ana_biblioteca = XXXcd_ana_biblioteca AND fl_canc != 'S'
	 * </fisso> </statement>
	 *
	 * @param opzioni
	 * @return
	 * @throws InfrastructureException
	 */
	public List<Tbf_biblioteca_in_polo> selectPerAnagrafe(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TBF_BIBLIOTECA_IN_POLO_selectPerAnagrafe");
			filter.setParameter(Tbf_biblioteca_in_poloCommonDao.XXXcd_ana_biblioteca, opzioni
					.get(Tbf_biblioteca_in_poloCommonDao.XXXcd_ana_biblioteca));
			myOpzioni.remove(Tbf_biblioteca_in_poloCommonDao.XXXcd_ana_biblioteca);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tbf_biblioteca_in_polo",
                    this.basicCriteria, session);

			List<Tbf_biblioteca_in_polo> result = this.basicCriteria
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
	 * <statement nome="selectPerPoloLike" tipo="select" id="Jenny_03"> <fisso>
	 * WHERE cd_polo LIKE UPPER(XXXcd_polo)||'%' AND fl_canc != 'S' </fisso>
	 * </statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tbf_biblioteca_in_polo> selectPerPoloLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session
					.enableFilter("TBF_BIBLIOTECA_IN_POLO_selectPerPoloLike");
			filter.setParameter(Tbf_biblioteca_in_poloCommonDao.XXXcd_polo, opzioni
					.get(Tbf_biblioteca_in_poloCommonDao.XXXcd_polo));
			myOpzioni.remove(Tbf_biblioteca_in_poloCommonDao.XXXcd_polo);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tbf_biblioteca_in_polo",
                    this.basicCriteria, session);

			List<Tbf_biblioteca_in_polo> result = this.basicCriteria
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

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tbf_biblioteca_in_polo.class;
	}

}
