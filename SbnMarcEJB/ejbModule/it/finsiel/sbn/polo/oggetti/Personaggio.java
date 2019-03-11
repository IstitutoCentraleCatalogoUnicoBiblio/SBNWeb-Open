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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_personaggioResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_per_intResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Tb_personaggio;
import it.finsiel.sbn.polo.orm.Tr_per_int;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe personaggio.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 6-mar-03
 */
public class Personaggio extends Tb_personaggio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8034418229656918625L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.Autore");
    public Personaggio() {

    }

    /**
     * Metodo per cercare la personaggio con identificativo:
     * ricerca su Tb_titolo con ID
     * @throws InfrastructureException
     */
    public Tb_personaggio cercaPerId(int id) throws EccezioneDB, InfrastructureException {
        setID_PERSONAGGIO(id);
        Tb_personaggioResult tavola = new Tb_personaggioResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_personaggio) v.get(0);
        else
            return null;
    }

    /**
     * Metodo per cercare la personaggio per bid del titolo legato
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaPerBid(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_personaggioResult tavola = new Tb_personaggioResult(this);


        tavola.executeCustom("selectPerBid");
        List v = tavola.getElencoRisultati();

        return v;
    }

    /**
     * Inserisce nel Db un personaggio standard
     * Accetta come parametri: il tb_personaggio_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void inserisci(Tb_personaggio personaggio) throws EccezioneDB, InfrastructureException {
        Tb_personaggioResult tavola = new Tb_personaggioResult(personaggio);
        tavola.insert(this);

    }

    /**
     * Inserisce una relazione tra un incipit e un titolo.
     * @throws InfrastructureException
     */
    public void inserisciRelazione(Tr_per_int rel) throws EccezioneDB, InfrastructureException {
        Tr_per_intResult tavola = new Tr_per_intResult(rel);
        tavola.insert(rel);

    }

    /**
     * Elimina relazione tra un incipit e un titolo.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void cancellaRelazione(int id_personaggio, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_per_int rel = new Tr_per_int();
        rel.setID_PERSONAGGIO(id_personaggio);
        rel.setUTE_VAR(ute_var);
        Tr_per_intResult tavola = new Tr_per_intResult(rel);
        tavola.executeCustom("updateCancellaPerId");
    }


    public Tr_per_int cercaInterprete(int id_personaggio) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_per_int rel = new Tr_per_int();
        rel.setID_PERSONAGGIO(id_personaggio);
        Tr_per_intResult tavola = new Tr_per_intResult(rel);


        tavola.executeCustom("selectPerPersonaggio");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tr_per_int) v.get(0);
        else
            return null;
    }

    /**
     * Cancella logicamente nel Db tutti i personaggi legati ad un titolo
     * Prima cancella tutti i legami tr_per_int relativi ad ogni personaggio.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        List v = cercaPerBid(bid);
        for (int i=0;i<v.size();i++) {
            cancellaRelazione((int)((Tb_personaggio)v.get(i)).getID_PERSONAGGIO(),ute_var);
        }
        setBID(bid);
        setUTE_VAR(ute_var);
        Tb_personaggioResult tavola = new Tb_personaggioResult(this);
        tavola.executeCustom("updateCancella");
    }

}
