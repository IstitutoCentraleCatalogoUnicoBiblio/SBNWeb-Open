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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_incipitResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Tb_incipit;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Classe Incipit
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 19-mar-03
 */
public class Incipit extends Tb_incipit {


    /**
	 * 
	 */
	private static final long serialVersionUID = -5168566434832683877L;

	public Incipit() {

    }

    /**
     * Metodo per cercare l'incipit per identificativo
     * @throws InfrastructureException
     */
    public Tb_incipit cercaPerId(String bid,String num, String numero_p_mov) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        setNUMERO_MOV(num);
        setNUMERO_P_MOV(numero_p_mov);
        Tb_incipitResult tavola = new Tb_incipitResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_incipit) v.get(0);
        else
            return null;
    }

    /**
     * Metodo per cercare l'incipit per identificativo, ignora il fl_canc
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_incipit cercaEsistenza(Tb_incipit inc) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_incipitResult tavola = new Tb_incipitResult(inc);


        tavola.executeCustom("verificaEsistenza");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_incipit) v.get(0);
        else
            return null;
    }

    /**
     * Metodo per cercare l'incipit con identificativo del titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaPerBid(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_incipitResult tavola = new Tb_incipitResult(this);


        tavola.executeCustom("selectPerBid");
        List v = tavola.getElencoRisultati();

        return v;
    }

    /**
     * Inserisce nel Db un musica standard
     * Accetta come parametri: il tb_incipit già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws InfrastructureException
     */
    public void inserisci(Tb_incipit musica) throws EccezioneDB, InfrastructureException {
        Tb_incipitResult tavola = new Tb_incipitResult(musica);
        tavola.insert(musica);

    }

    public void update(Tb_incipit musica) throws EccezioneDB, InfrastructureException {
        Tb_incipitResult tavola = new Tb_incipitResult(musica);
        tavola.update(musica);

    }

    /**
     * Esegue una cancellazione logica di un elemnto da DB
     * Accetta come parametri: il tb_titolo da cui estrae ute_var e bid.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        Tb_incipitResult tavola = new Tb_incipitResult(this);
        tavola.executeCustom("updateCancella");
    }

}
