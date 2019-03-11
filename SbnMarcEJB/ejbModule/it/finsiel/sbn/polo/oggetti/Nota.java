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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_notaResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Tb_nota;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Classe Nota
 * <p>
 * Gestisce l'inserimento e la lettura da tb_nota.
 * </p>
 * @author
 * @author
 *
 * @version 26-mag-03
 */
public class Nota extends Tb_nota {

	private static final long serialVersionUID = 7404168421688842053L;

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.Nota");

    public Nota() {

    }

    /**
     * Metodo per cercare tutte le note legate ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaPerBid(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_notaResult tavola = new Tb_notaResult(this);


        tavola.executeCustom("selectPerBid");
        List v = tavola.getElencoRisultati();

        return v;
    }




    /**
     * GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
     * Metodo per cercare tutte le note legate ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaPerBidTutte(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_notaResult tavola = new Tb_notaResult(this);


        tavola.executeCustom("selectPerBidTutte");
        List v = tavola.getElencoRisultati();

        return v;
    }
//    * GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274



    /** Legge da una tavola il valore del max progr */
    public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//       try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del progresivo da Tb_nota");
//            throw new EccezioneDB(1203);
//        }
    }

    /**
     * Inserisce nel Db una nota, calcola il progressivo in base al tipo nota e al bid.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void inserisci(String bid, String tipoNota, String nota, String ute) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setTP_NOTA(tipoNota);
        setDS_NOTA(nota);
        setUTE_INS(ute);
        setUTE_VAR(ute);
        inserisci(this);
    }
    /**
     * Inserisce nel Db una nota, calcola il progressivo in base al tipo nota e al bid.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void inserisci(Tb_nota nota) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_notaResult tavola = new Tb_notaResult(nota);
        tavola.executeCustom("selectMaxProg");
        int cnt = conta(tavola);
        log.debug("nota cnt: " + cnt);
		nota.setPROGR_NOTA(cnt + 1);
		//almaviva5_20131115 #5440
		tavola = new Tb_notaResult(nota);
        tavola.insert();

    }

    /**
     * Inserisce nel Db una nota, calcola il progressivo in base al tipo nota e al bid.
     * @throws InfrastructureException
     */
    public void update(Tb_nota nota) throws EccezioneDB, InfrastructureException {
        Tb_notaResult tavola = new Tb_notaResult(nota);
        tavola.update(nota);

    }

    /** Cancella fisicamente tutte le note relative ad un titolo.<br>
     * <b>Attenzione</b>: Non utilizza il flag_canc, ma elimina veramente.
     * @param bid
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    // Intervento interno Maggio 2016 per trasformare la gestione delle note con la cancellazione
    // fisica come nel protocollo di Indice mentre prima si utilizzava la cancellazione logica;
    // adeguamento anche per EVOLUTIVA della nota 321 con le due varianti di REP e DB in base al
    // campo tipo della nota stesssa
    public void cancella(String bid, String uteVar) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        //almaviva5_20090918 #3068
        setUTE_VAR(uteVar);
        setFL_CANC("S");
        Tb_notaResult tavola = new Tb_notaResult(this);
        tavola.executeCustom("deletePerBid");
        this.azzeraParametri();
    }


    public Tb_nota cercaPerChiave(String tp_nota, int progr, String bid) throws EccezioneDB, InfrastructureException {
        setBID(bid);
        setTP_NOTA(tp_nota);
        setPROGR_NOTA(progr);
        Tb_notaResult tavola = new Tb_notaResult(this);

        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_nota) v.get(0);
        return null;
    }
}
