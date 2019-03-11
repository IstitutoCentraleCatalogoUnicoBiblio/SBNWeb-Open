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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_autore_autResult;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tr_aut_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_aut;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe LegamiAutore.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 15-nov-02
 */
public class LegamiAutore extends GestoreLegami {
    Tr_aut_autResult tav_aut_aut = null;
    Vl_autore_autResult vista_aut_aut = null;
    private String uteIns;
    private String uteVar;
    private String tipoLegame;


    public LegamiAutore(String uteIns,String uteVar,String tpLegame) {
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
        Vl_autore_aut autore = new Vl_autore_aut();
        autore.setVID_1(id);
        autore.setTP_LEGAME(tipoLegame);
        if (vista_aut_aut == null) {
            vista_aut_aut = new Vl_autore_autResult(autore);
        }
        //vista_aut_aut.setVl_autore_aut(autore);
        vista_aut_aut.executeCustom("selectAutorePerLegame");
        List returnVec = new ArrayList();
        List resultSet = vista_aut_aut.getResponse();
        Tb_autore aut;
        for (int i = 0; i < resultSet.size(); i++) {
            aut = (Tb_autore) resultSet.get(i);
            returnVec.add(aut.getVID());
        }
        return returnVec;
    }

    /**
     * @throws InfrastructureException
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @see iccu.box.utility.GestoreLegami#inserisciLegame(String, String)
     */
    protected void inserisciLegame(String id_1, String id_2) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_aut autaut = new Tr_aut_aut();
        autaut.setTP_LEGAME(getTipoLegame());
        autaut.setUTE_INS(getUteIns());
        autaut.setUTE_VAR(getUteVar());
        autaut.setVID_BASE(id_1);
        autaut.setVID_COLL(id_2);
        autaut.setFL_CANC(" ");
        if (tav_aut_aut == null) {
            tav_aut_aut = new Tr_aut_autResult(autaut);
        }
        //tav_aut_aut.setTr_aut_aut(autaut);
        try {
			tav_aut_aut.executeCustom("selectEsistenza");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List resultSet = tav_aut_aut.getResponse();

        int size = resultSet.size();
        if (size>0) {
            tav_aut_aut.executeCustom("updateInsert");
        } else {
            tav_aut_aut.insert(autaut);
        }
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
