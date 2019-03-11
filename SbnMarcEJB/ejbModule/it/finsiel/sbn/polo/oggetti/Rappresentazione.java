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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_rappresentResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Tb_rappresent;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

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
public class Rappresentazione extends Tb_rappresent {


	private static final long serialVersionUID = -6499621231368947390L;
	private static Category log =
		Category.getInstance("iccu.serversbnmarc.Autore");
	public Rappresentazione() {

	}

	/**
	 * Metodo per cercare la musica con identificativo:
	 * ricerca su Tb_titolo con ID
	 * @throws InfrastructureException
	 */
	public Tb_rappresent cercaPerId(String bid) throws EccezioneDB, InfrastructureException {
		setBID(bid);
		Tb_rappresentResult tavola = new Tb_rappresentResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

		if (v.size() > 0)
			return (Tb_rappresent) v.get(0);
		else
			return null;
	}

    /**
     * Metodo per cercare la rappresentazione con identificativo: ignora il fl_canc
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_rappresent cercaEsistenza(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_rappresentResult tavola = new Tb_rappresentResult(this);


        tavola.executeCustom("verificaEsistenza");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_rappresent) v.get(0);
        else
            return null;
    }

	/**
	 * Inserisce nel Db un musica standard
	 * Accetta come parametri: il tb_musica_std già pronto per essere inserito.
	 * (quindi contenente tipo, num, bid, nota, ...)
	 * @throws InfrastructureException
	 */
	public void inserisci(Tb_rappresent rappresent) throws EccezioneDB, InfrastructureException {
		Tb_rappresentResult tavola = new Tb_rappresentResult(rappresent);
		tavola.insert(rappresent);

	}

    /**
     * Inserisce nel Db un musica standard
     * Accetta come parametri: il tb_musica_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void update(Tb_rappresent rappresent) throws EccezioneDB, InfrastructureException {
        Tb_rappresentResult tavola = new Tb_rappresentResult(rappresent);
        tavola.update(rappresent);

    }

    /**
     * Esegue una cancellazione logica di un elemnto da DB
     * Accetta come parametri: il tb_titolo da cui estrae ute_var e bid.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void cancella(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        Tb_rappresentResult tavola = new Tb_rappresentResult(this);
        tavola.executeCustom("updateCancella");

    }

}
