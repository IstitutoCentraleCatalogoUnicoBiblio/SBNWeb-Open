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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_composizioneResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Tb_composizione;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe composizione.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 6-mar-03
 */
public class Composizione extends Tb_composizione {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6919889710120529597L;
	private static Category log =
        Category.getInstance("iccu.serversbnmarc.Autore");

    public Composizione() {

    }

    /**
     * Metodo per cercare la composizione con identificativo:
     * ricerca su Tb_titolo con ID
     * @throws InfrastructureException
     */
    public Tb_composizione cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        Tb_composizioneResult tavola = new Tb_composizioneResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_composizione) v.get(0);
        else
            return null;
    }

    /**
     * Metodo per cercare la composizione con identificativo:
     * ricerca su Tb_titolo con ID
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_composizione cercaEsistenza(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_composizioneResult tavola = new Tb_composizioneResult(this);


        tavola.executeCustom("verificaEsistenza");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_composizione) v.get(0);
        else
            return null;
    }

    /**
     * Inserisce nel Db un composizione standard
     * Accetta come parametri: il tb_composizione_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void inserisci(Tb_composizione composizione) throws EccezioneDB, InfrastructureException {
        Tb_composizioneResult tavola = new Tb_composizioneResult(composizione);

        tavola.insert(composizione);

    }

    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        Tb_composizioneResult tavola = new Tb_composizioneResult(this);
        tavola.executeCustom("updateCancella");
    }

    /**
     * Inserisce nel Db un composizione standard
     * Accetta come parametri: il tb_composizione_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void eseguiUpdate(Tb_composizione composizione) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_composizioneResult tavola = new Tb_composizioneResult(composizione);

        tavola.executeCustomUpdate("update");

    }

    /**
     * Inserisce nel Db un composizione standard
     * Accetta come parametri: il tb_composizione_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateVariazione(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_composizione comp = new Tb_composizione();
        comp.setBID(bid);
        Tb_composizioneResult tavola = new Tb_composizioneResult(comp);


        tavola.executeCustomUpdate("updateVariazione");

    }

    public TableDao cercaPerCreaIsbd(String ts_var,String lettera) throws IllegalArgumentException, InvocationTargetException, Exception {
        settaParametro(TableDao.XXXultima_variazione,ts_var);
        settaParametro(TableDao.XXXlettera,lettera);
        Tb_composizioneResult tavola = new Tb_composizioneResult(this);


        tavola.executeCustom("selectCreazioneIsbd","order_bid");
        return tavola;
    }


}
