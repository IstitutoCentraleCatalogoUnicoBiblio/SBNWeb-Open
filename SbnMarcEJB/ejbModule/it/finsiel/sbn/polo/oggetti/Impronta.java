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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_improntaResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_impResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_imp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Classe Impronta.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 28-feb-2003
 */
public class Impronta extends Tb_impronta {

	private static final long serialVersionUID = -6335613954885752729L;
	protected static Logger log = Logger.getLogger("sbnmarcPolo");

    public Impronta() {
    }

    public void inserisci(Tb_impronta impronta) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_improntaResult tavola = new Tb_improntaResult(impronta);

        // Inizio intervento almaviva2 Mantis 3572 09.03.2010 -nel caso di impronta presente ma cancellata logicamente
        // si deve effettuare l'update
        Tb_impronta impr1 = verificaEsistenza(impronta.getBID(),impronta.getIMPRONTA_1(), impronta.getIMPRONTA_2(), impronta.getIMPRONTA_3());
        if (impr1 != null) {
            if (impr1.getFL_CANC().equals("S")) {
                update(impronta);
                return;
            } else {
                throw new EccezioneSbnDiagnostico(3330,"Impronta duplicata");
            }
        }
        // Fine intervento almaviva2 Mantis 3572 09.03.2010
        tavola.insert(impronta);

    }

    public void update(Tb_impronta impronta) throws EccezioneDB, InfrastructureException {
        Tb_improntaResult tavola = new Tb_improntaResult(impronta);
        tavola.update(impronta);

    }

    public Tb_impronta cercaPerChiavi(String bid, String imp1, String imp2, String imp3) throws EccezioneDB, InfrastructureException {
        setBID(bid);

        setIMPRONTA_1(imp1);
        setIMPRONTA_2(imp2);
        setIMPRONTA_3(imp3);
        Tb_improntaResult tavola = new Tb_improntaResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_impronta) v.get(0);
        return null;
    }

    public Tb_impronta verificaEsistenza(String bid, String imp1, String imp2, String imp3) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setIMPRONTA_1(imp1);
        setIMPRONTA_2(imp2);
        setIMPRONTA_3(imp3);
        Tb_improntaResult tavola = new Tb_improntaResult(this);


        tavola.executeCustom("verificaEsistenza");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_impronta) v.get(0);
        return null;
    }

    public List cercaPerBid(String chiave) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(chiave);
        Tb_improntaResult tavola = new Tb_improntaResult(this);


        tavola.executeCustom("selectPerBid");
        List v = tavola.getElencoRisultati();

        return v;
    }

    public void cancellaPerBid(String bid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        Tb_improntaResult tavola = new Tb_improntaResult(this);
        tavola.executeCustom("updateCancellaPerBid");
    }
    /**
     * Ricerca per un insieme di impronta_2
     * Se il bid è diverso da null cerca i titoli diversi da quel bid.
     * @param impronte Vettore di stringhe contenenti le impronte da confrontare
     * @return un vettore di Vl_titolo_imp che soddisfano il confronto.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaPerSecondaImpronta(List impronte, String bid, String cd_natura) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_imp tit = new Vl_titolo_imp();
        tit.setBID(bid);
        tit.setCD_NATURA(cd_natura);

//        Inizio almaviva2 27 luglio 2009 La SELECT non era implementata
//        for (int i = 0; i < impronte.size(); i++)
//            tit.settaParametro("XXXinput_impronta_" + i, (String) impronte.get(i));
        if (impronte.size() > 0) {
        	//almaviva5_20090728
        	//tit.settaParametro(":XXXimpronta_2", (String) impronte.get(0));
        	tit.settaParametro(KeyParameter.XXXimpronta_2, impronte.get(0));
        }
//      Fine almaviva2 27 luglio 2009 La SELECT non era implementata
        Vl_titolo_impResult tavola = new Vl_titolo_impResult(tit);

        tavola.executeCustom("selectPerImprontaDue");
        List v = tavola.getElencoRisultati();

        return v;
    }
    /**
     * Metodo per cercare il titolo con numero di identificativo: ignora il fl_canc
     * ricerca su Tb_impronta per Bid
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List estraiTuttiPerBid(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_improntaResult tavola = new Tb_improntaResult(this);


        tavola.executeCustom("selectTuttiPerBid");
        List v = tavola.getElencoRisultati();

        return v;
    }

    public void cancella(Tb_impronta impronta) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_improntaResult tavola = new Tb_improntaResult(impronta);
        tavola.executeCustom("updateCancella");
    }
}
