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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_numero_stdResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_num_stdResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_num_std;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe NumeroStd
 * <p>
 * Contiene i metodi di accesso per il db
 * </p>
 *
 * @author
 * @author
 *
 * @version 31-dic-02
 */
public class NumeroStd extends Tb_numero_std {


    /**
	 * 
	 */
	private static final long serialVersionUID = -7851688971570894888L;

	public NumeroStd() {

    }

    /**
     * Metodo per cercare il titolo con numero di identificativo:
     * ricerca su Tb_numero_std con ID
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaNumeroPerBid(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);

        Tb_numero_stdResult tavola = new Tb_numero_stdResult(this);


        tavola.executeCustom("selectPerBid");
        List v = tavola.getElencoRisultati();

        return v;
    }
    /**
      * Metodo per cercare il titolo con numero di identificativo:
      * ricerca su Tb_numero_std con ID
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
      */
    public List cercaNumeroPerISBN(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_numero_stdResult tavola = new Tb_numero_stdResult(this);

        tavola.executeCustom("SelectISBN");
        List v = tavola.getElencoRisultati();

        return v;
    }

    /**
      * Metodo per cercare il titolo con numero di identificativo:
      * ricerca su Tb_numero_std con ID
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
      */
    public Tb_numero_std verificaEsistenza(String bid, String tp_num, String num) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setTP_NUMERO_STD(tp_num);
        setNUMERO_STD(num);
        Tb_numero_stdResult tavola = new Tb_numero_stdResult(this);


        tavola.executeCustom("selectEsistenzaID");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_numero_std) v.get(0);
        else
            return null;
    }

    /**
     * Inserisce nel Db un numero standard
     * Prima verifica che non ce ne sia già uno uguale.
     * Accetta come parametri: il tb_numero_std già pronto per essere inserito.
     * (quindi contenente tipo, num, bid, nota, ...)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void inserisci(Tb_numero_std numero) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_numero_stdResult tavola = new Tb_numero_stdResult(numero);


        Tb_numero_std num1 = verificaEsistenza(numero.getBID(),numero.getTP_NUMERO_STD().trim(),numero.getNUMERO_STD().trim());
        if (num1 != null) {
//            if (num1.getFL_CANC().equals("S")) {
//                update(numero);
//            } else {
//                throw new EccezioneSbnDiagnostico(3330,"Num std duplicato");
//            }
        	numero.setUTE_INS(num1.getUTE_INS());
        	numero.setTS_INS(num1.getTS_INS());
        	update(numero);
        } else
        	tavola.insert(numero);

    }

    public void update(Tb_numero_std numero) throws EccezioneDB, InfrastructureException {
        Tb_numero_stdResult tavola = new Tb_numero_stdResult(numero);
        tavola.update(numero);

    }
    public void cancellaPerBid(String bid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setUTE_VAR(ute_var);
        Tb_numero_stdResult tavola = new Tb_numero_stdResult(this);
        tavola.executeCustom("updateCancellaPerBid");
    }

    public List cercaTitoliPerNumStd(String num, String tipoStd, String paeseStd, String nota, String bid, String aa_pubb1)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Vl_titolo_num_std tn = new Vl_titolo_num_std();
        tn.setNUMERO_STD(num);
        tn.setTP_NUMERO_STD(Decodificatore.getCd_tabella("Tb_numero_std", "tp_numero_std", tipoStd));
        tn.setCD_PAESE_STD(paeseStd);
        tn.setNOTA_NUMERO_STD(nota);
        tn.setBID(bid);
        tn.setAA_PUBB_1(aa_pubb1);
        Vl_titolo_num_stdResult tavola = new Vl_titolo_num_stdResult(tn);
        tavola.executeCustom("selectPerNumero");
        //Deve essere realizzata: campi fissi sono tipo e numero, gli altri sono opzionali.
        List v = tavola.getElencoRisultati();

        return v;
    }

    /**
     * Metodo per cercare il titolo con numero di identificativo: ignora il fl_canc
     * ricerca su Tb_numero_std con ID
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List estraiTuttiPerBid(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tb_numero_stdResult tavola = new Tb_numero_stdResult(this);
        tavola.executeCustom("selectTuttiPerBid");
        List v = tavola.getElencoRisultati();

        return v;
    }

    public void cancella(Tb_numero_std numero) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_numero_stdResult tavola = new Tb_numero_stdResult(numero);
        tavola.executeCustom("updateCancella");
    }
}
