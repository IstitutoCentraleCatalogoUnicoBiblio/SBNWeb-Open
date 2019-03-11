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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_graficaResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_grafica;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Classe Grafica
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 13-mar-03
 */
public class Grafica extends Tb_grafica {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3036463444809809746L;

	public Grafica() {

    }

    /**
     * Metodo per cercare la Grafica con identificativo:
     * ricerca su Tb_titolo con ID
     * @throws InfrastructureException
     */
    public Tb_grafica cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        Tb_graficaResult tavola = new Tb_graficaResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_grafica) v.get(0);
        else
            return null;
    }

    /**
     * Inserisce nel Db un Grafica standard
     * Accetta come parametri: il tb_Grafica_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void inserisci(Tb_grafica grafica) throws EccezioneDB, InfrastructureException {
        Tb_graficaResult tavola = new Tb_graficaResult(grafica);
        tavola.insert(grafica);

    }


 // BUG MANTIS 3648  almaviva2 adeguato comportamento a quello di Cartografia passando anche il timestamp ed elaborandolo
    public void cancellaPerBid(String bid,String ute_var, SbnDatavar timestamp) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);

        //setTs_var(time);
        setTS_VAR(ConverterDate.SbnDataVarToDate(timestamp));


        Tb_graficaResult tavola = new Tb_graficaResult(this);
        tavola.executeCustom("updateCancella");
    }

    public void update(Tb_grafica grafica) throws EccezioneDB, InfrastructureException {
        Tb_graficaResult tavola = new Tb_graficaResult(grafica);

        tavola.update(grafica);

    }
}
