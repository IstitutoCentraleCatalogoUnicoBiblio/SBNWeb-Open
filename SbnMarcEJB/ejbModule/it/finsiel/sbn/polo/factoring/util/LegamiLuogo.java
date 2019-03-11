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
package it.finsiel.sbn.polo.factoring.util;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_luo_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_luogo_luoResult;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tr_luo_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_luo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LegamiLuogo extends GestoreLegami {


    Tr_luo_luoResult	tav_luo_luo		= null;
    Vl_luogo_luoResult 	vista_luo_luo 	= null;

    private String uteIns;
    private String uteVar;
    private String tipoLegame;


    public LegamiLuogo(String uteIns,String uteVar,String tpLegame) {
        this.uteIns=uteIns;
        this.uteVar=uteVar;
        this.tipoLegame=tpLegame;
    }

    /**
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @see iccu.box.utility.GestoreLegami#cercaLegami(String)
     */
    protected List cercaLegami(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_luogo_luo	luogo = new Vl_luogo_luo();
		luogo.setLID_1(id);
		if (vista_luo_luo == null){
			vista_luo_luo = new Vl_luogo_luoResult(luogo);
		}
		vista_luo_luo.executeCustom("selectLuogoPerRinvii");
		List returnVec = new ArrayList();
		List resultSet = vista_luo_luo.getResponse();
		Tb_luogo tb_luogo;
        for (int i = 0; i < resultSet.size(); i++) {
            tb_luogo = (Tb_luogo) resultSet.get(i);
            returnVec.add(tb_luogo.getLID());
        }
        return returnVec;

    }

    /**
     * @throws InfrastructureException
     * @throws InfrastructureException
     * @see iccu.box.utility.GestoreLegami#inserisciLegame(String, String)
     */
    protected void inserisciLegame(String id_1, String id_2) throws InfrastructureException{
		Tr_luo_luo tr_luo_luo = new Tr_luo_luo();
		tr_luo_luo.setTP_LEGAME(getTipoLegame());
		tr_luo_luo.setUTE_VAR(getUteVar());
		tr_luo_luo.setUTE_INS(getUteIns());
		tr_luo_luo.setLID_BASE(id_1);
		tr_luo_luo.setLID_COLL(id_2);
		tr_luo_luo.setFL_CANC(" ");
		if (tav_luo_luo == null){
			tav_luo_luo = new Tr_luo_luoResult(tr_luo_luo);

		}
		//tav_luo_luo.setTr_luo_luo(tr_luo_luo);
		tav_luo_luo.insert(tr_luo_luo);
    }

    public String getTipoLegame() {
        return tipoLegame;
    }
    public String getUteIns() {
        return uteIns;
    }
    public String getUteVar() {
        return uteVar;
    }


}
