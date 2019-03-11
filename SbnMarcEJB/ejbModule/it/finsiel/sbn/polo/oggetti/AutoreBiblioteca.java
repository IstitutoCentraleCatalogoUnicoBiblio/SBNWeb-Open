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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_bibResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tr_aut_bib;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

public class AutoreBiblioteca extends Tr_aut_bib {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5293049677752131212L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.AutoreBiblioteca");


    public AutoreBiblioteca() {
    }

    public boolean inserisciTr_aut_bib(String user, String Vid, String cd_polo, String biblioteca)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_aut_bib tr_aut_bib = new Tr_aut_bib();
        tr_aut_bib.setVID(Vid);
        tr_aut_bib.setCD_POLO(cd_polo);
        tr_aut_bib.setCD_BIBLIOTECA(biblioteca);
        tr_aut_bib.setFL_CANC(" ");
        tr_aut_bib.setFL_ALLINEA(" ");
        tr_aut_bib.setFL_ALLINEA_SBNMARC(" ");
        SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis());
        tr_aut_bib.setTS_INS(ConverterDate.SbnDataVarToDate(data_operazione));
        tr_aut_bib.setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
        tr_aut_bib.setUTE_VAR(user);
        tr_aut_bib.setUTE_INS(user);
        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(tr_aut_bib);
        tr_aut_bibResult.executeCustom("verificaEsistenza");
        if (tr_aut_bibResult.getElencoRisultati().size()>0)
            tr_aut_bibResult.update(tr_aut_bib);
        else
            tr_aut_bibResult.insert(tr_aut_bib);
        return true;
    }

    public boolean verificaEsistenzaTr_aut_bib(String Vid, String cd_polo, String biblioteca)
        throws EccezioneDB, InfrastructureException {

        Tr_aut_bib tr_aut_bib = new Tr_aut_bib();
        tr_aut_bib.setVID(Vid);
        tr_aut_bib.setCD_POLO(cd_polo);
        tr_aut_bib.setCD_BIBLIOTECA(biblioteca);
        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(tr_aut_bib);
        tr_aut_bibResult.selectPerKey(tr_aut_bib.leggiAllParametro());
        List resultTableDao = tr_aut_bibResult.getElencoRisultati();
        if (resultTableDao.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verificaEsistenzaTr_aut_bib(String Vid, String cd_polo) throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_aut_bib tr_aut_bib = new Tr_aut_bib();
        tr_aut_bib.setVID(Vid);
        tr_aut_bib.setCD_POLO(cd_polo);
        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(tr_aut_bib);
        tr_aut_bibResult.executeCustom("selectPerPolo");
        List resultTableDao = tr_aut_bibResult.getElencoRisultati();
        if (resultTableDao.size() != 0) {
            return true;
        } else {
            return false;
        }
    }


    public List cercaFlagAllineaDiverso(String Vid, String fl_allinea) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_bib tr_aut_bib = new Tr_aut_bib();
        tr_aut_bib.setVID(Vid);
        tr_aut_bib.setFL_ALLINEA(fl_allinea);
        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(tr_aut_bib);
        tr_aut_bibResult.executeCustom("selectPerFlagAllineaDiverso");
        List resultTableDao = tr_aut_bibResult.getElencoRisultati();
        return resultTableDao;
    }

/**
     * metodo utilizzato per la verifica della non esistenza di localizzazioni
     * diverse dal polo dell'utente
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
     */
    public boolean verificaLocalizzazioniTr_aut_bib(String Vid, String cd_polo) throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_aut_bib tr_aut_bib = new Tr_aut_bib();
        tr_aut_bib.setVID(Vid);
        tr_aut_bib.setCD_POLO(cd_polo);
        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(tr_aut_bib);
        tr_aut_bibResult.executeCustom("selectPerPoloDiverso");
        List resultTableDao = tr_aut_bibResult.getElencoRisultati();
        if (resultTableDao.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean cancellaTr_aut_bib(String user, String Vid, String cd_polo, String cd_biblioteca)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_aut_bib tr_aut_bib = new Tr_aut_bib();
        tr_aut_bib.setVID(Vid);
        tr_aut_bib.setFL_CANC("S");
        tr_aut_bib.setCD_POLO(cd_polo);
        tr_aut_bib.setCD_BIBLIOTECA(cd_biblioteca);
        SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis());
        tr_aut_bib.setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
        tr_aut_bib.setUTE_VAR(user);
        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(tr_aut_bib);
        tr_aut_bibResult.executeCustom("updatePerDelocalizza");
        return true;
    }

    public boolean allineaTr_aut_bib(String user, Tb_autore autore, String cd_polo, String cd_biblioteca)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_aut_bib tr_aut_bib = new Tr_aut_bib();
        tr_aut_bib.setVID(autore.getVID());
        tr_aut_bib.setFL_ALLINEA(" ");
        tr_aut_bib.setFL_ALLINEA_SBNMARC(" ");
        tr_aut_bib.setCD_POLO(cd_polo);
        tr_aut_bib.setCD_BIBLIOTECA(cd_biblioteca);
        tr_aut_bib.setUTE_VAR(user);
        if (autore.getFL_CANC().equals("S"))
        {
            tr_aut_bib.setFL_CANC("S");
        }
        // MARCO RANIERI MODIFICA
        // AGGIUNGO SPAZIO SE FLAG CANC NON ATTIVO
        else
        {
            tr_aut_bib.setFL_CANC(" ");
        }

        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(tr_aut_bib);
        tr_aut_bibResult.executeCustom("updatePerModifica");
        int nroRec = tr_aut_bibResult.getRowsUpdate();
        log.info("aggiornamenti eseguiti " + nroRec);
        if (nroRec != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean aggiornaPerAllinea(String user, String Vid, String cd_polo, String cd_biblioteca)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        setVID(Vid);
        setFL_ALLINEA("S");
        setCD_POLO(cd_polo);
        setCD_BIBLIOTECA(cd_biblioteca);
        setUTE_VAR(user);
        Tr_aut_bibResult tr_aut_bibResult = new Tr_aut_bibResult(this);
        tr_aut_bibResult.executeCustom("updatePerModifica");
        int nroRec = tr_aut_bibResult.getRowsUpdate();
        log.info("aggiornamenti eseguiti " + nroRec);
        if (nroRec != 0) {
            return true;
        } else
            return false;
    }

    public List cercaPerAllineamento(String bid, String polo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_bib tr_tit_bib = new Tr_aut_bib();
        tr_tit_bib.setVID(bid);
        tr_tit_bib.setCD_POLO(polo);
        Tr_aut_bibResult tr_tit_bibResult = new Tr_aut_bibResult(tr_tit_bib);
        tr_tit_bibResult.executeCustom("selectPerAllineamento");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();
        return resultTableDao;
    }

    public void aggiornaTuttiFlAllinea(Tr_aut_bib tb) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_bibResult tr_tit_bibResult = new Tr_aut_bibResult(tb);
        tr_tit_bibResult.executeCustom("updateTuttiFlag");
    }

    public void aggiornaFlagCancCancellazione(String vidCancellato,String utente)
    throws IllegalArgumentException, InvocationTargetException, Exception {
    Tr_aut_bib tb = new Tr_aut_bib();
    tb.setUTE_VAR(utente);
    tb.setVID(vidCancellato);
    Tr_aut_bibResult tr_tit_bibResult = new Tr_aut_bibResult(tb);
    tr_tit_bibResult.executeCustom("updateFlCancCancellazione");
    }
}
