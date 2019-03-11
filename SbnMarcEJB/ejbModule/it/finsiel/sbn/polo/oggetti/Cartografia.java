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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_cartografiaResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_cartografia;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Classe Cartografia
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 13-mar-03
 */
public class Cartografia extends Tb_cartografia {


    /**
	 * 
	 */
	private static final long serialVersionUID = 8473670979533526781L;

	public Cartografia() {

    }

    /**
     * Metodo per cercare la Cartografia con identificativo:
     * ricerca su Tb_titolo con ID
     * @throws InfrastructureException
     */
    public Tb_cartografia cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        Tb_cartografiaResult tavola = new Tb_cartografiaResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_cartografia) v.get(0);
        else
            return null;
    }

    /**
     * Inserisce nel Db un Cartografia standard
     * Accetta come parametri: il tb_Cartografia_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void inserisci(Tb_cartografia Cartografia) throws EccezioneDB, InfrastructureException {
        Tb_cartografiaResult tavola = new Tb_cartografiaResult(Cartografia);
        tavola.insert(Cartografia);

    }
    public void cancellaPerBid(String bid,String ute_var,SbnDatavar timestamp) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        setTS_VAR(ConverterDate.SbnDataVarToDate(timestamp));
        Tb_cartografiaResult tavola = new Tb_cartografiaResult(this);


        tavola.executeCustomUpdate("updateCancella");

    }

    /**
     * Inserisce nel Db un Cartografia standard
     * Accetta come parametri: il tb_Cartografia_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void update(Tb_cartografia Cartografia) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_cartografiaResult tavola = new Tb_cartografiaResult(Cartografia);


        tavola.executeCustomUpdate("update");

    }
}
