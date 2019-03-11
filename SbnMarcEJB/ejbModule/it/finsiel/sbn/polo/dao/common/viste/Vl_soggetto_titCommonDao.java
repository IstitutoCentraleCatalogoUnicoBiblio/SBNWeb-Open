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
package it.finsiel.sbn.polo.dao.common.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_soggettoCommonDao;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.criterion.Restrictions;

public class Vl_soggetto_titCommonDao extends Tb_soggettoCommonDao {


    protected boolean kycleslike = false;

	public Vl_soggetto_titCommonDao() {
        super();
	}


	public void createCriteria(HashMap opzioni) throws InfrastructureException
	{
		try {
			Class cl = Vl_soggetto_titCommonDao.class;//this.getClass();
			Method[] metodi = cl.getDeclaredMethods();
			for(int index =0; index<metodi.length; index++){
				if(metodi[index].getName().startsWith("set")){
						metodi[index].invoke(this,new Object[] { opzioni});
					//log.debug(metodi[index].getName());
				}
			}
            super.createCriteria(opzioni);

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InfrastructureException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InfrastructureException(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InfrastructureException(e);
		}
	}

    public void setFiltroSoggettarioGestito(HashMap opzioni) throws InfrastructureException {

    	//almaviva5_20091030
		SbnUserType user = (SbnUserType) opzioni.get(XXXcercaPerFiltroUteSoggGestione);
		if (user == null)
			return;

		String codPolo = user.getBiblioteca().substring(0, 3);
		String codBib  = user.getBiblioteca().substring(3);

		StringBuilder sql = new StringBuilder(512);
		sql.append("exists (");
		sql.append(" select sogg.cd_sogg from tr_soggettari_biblioteche sogg");
		sql.append(" inner join tbf_utenti_professionali_web userweb on userweb.userid='").append(user.getUserId()).append("'");
		sql.append(" inner join tbf_bibliotecario userbib on userbib.id_utente_professionale=userweb.id_utente_professionale");
		sql.append(" inner join tbf_par_auth param on param.id_parametro=userbib.id_parametro");
		sql.append(" where sogg.fl_att='1'");
		//sql.append(" and param.cd_par_auth='SO' and param.fl_leg_auth='S' and param.fl_abil_legame='S'");
		//almaviva5_20140224 evolutive google3
		sql.append(" and param.cd_par_auth='SO' and param.fl_leg_auth='S'");
		sql.append(" and sogg.cd_polo='").append(codPolo).append("' and sogg.cd_biblioteca='").append(codBib).append("'");
		sql.append(" and sogg.cd_sogg={alias}.CD_SOGGETTARIO");
		sql.append(")");

		basicCriteria.add(Restrictions.sqlRestriction(sql.toString()));

		opzioni.remove(XXXcercaPerFiltroUteSoggGestione);
    }

}

