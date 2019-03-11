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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_musicaResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Tb_musica;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Classe Musica.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 6-mar-03
 */
public class Musica extends Tb_musica {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8386537240615522864L;

	public Musica() {

    }

    /**
     * Metodo per cercare la musica con identificativo:
     * ricerca su Tb_titolo con ID
     * @throws InfrastructureException
     */
    public Tb_musica cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        Tb_musicaResult tavola = new Tb_musicaResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_musica) v.get(0);
        else
            return null;
    }

    /**
     * Metodo per cercare la musica con identificativo:
     * ricerca su Tb_titolo con ID
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_musica verificaEsistenza(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_musicaResult tavola = new Tb_musicaResult(this);


        tavola.executeCustom("selectEsistenza");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_musica) v.get(0);
        else
            return null;
    }

    /**
     * Inserisce nel Db un musica standard
     * Accetta come parametri: il tb_musica_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void inserisci(Tb_musica musica) throws EccezioneDB, InfrastructureException {
        Tb_musicaResult tavola = new Tb_musicaResult(musica);
        tavola.insert(musica);

    }

    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        //setTs_var(time);
        Tb_musicaResult tavola = new Tb_musicaResult(this);
        tavola.executeCustom("updateCancella");
    }

    /**
     * Inserisce nel Db un musica standard
     * Accetta come parametri: il tb_musica_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void eseguiUpdate(Tb_musica musica) throws EccezioneDB, InfrastructureException {
        Tb_musicaResult tavola = new Tb_musicaResult(musica);
        tavola.update(musica);

    }


}
