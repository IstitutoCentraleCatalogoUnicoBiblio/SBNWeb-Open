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

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_titResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_bResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;

import java.lang.reflect.InvocationTargetException;
import java.util.List;



/**
 * Classe TitoloTitolo
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 15-mag-03
 */
public class TitoloTitolo {


    /** Costruttore
     *
     * @param conn La connessione al DB utilizzata
     */
    public TitoloTitolo() {

    }

    /**
     * Esegue l'update di cancellazione
     *
     * @param uteVar
     * @param bid_base
     * @param bid_coll
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateCancella(String uteVar, String bid_base, String bid_coll) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_COLL(bid_coll);
        tt.setBID_BASE(bid_base);
        tt.setUTE_VAR(uteVar);
        updateCancella(tt);
    }

    /**
     * Esegue l'update di cancellazione
     *
     * @param uteVar
     * @param bid_base
     * @param bid_coll
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateCancella(Tr_tit_tit tt)  throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustomUpdate("updateDisabilita");
    }

    /** Esegue l'udate di cancellazione, se non viene modificata alcuna riga genera eccezione
     *
     * @param uteVar
     * @param bid_base
     * @param bid_coll
     * @param timestamp data di variazione nel db
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateCancella(String uteVar, String bid_base, String bid_coll, String timestamp)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_COLL(bid_coll);
        tt.setBID_BASE(bid_base);
        tt.setUTE_VAR(uteVar);
        tt.setTS_VAR(ConverterDate.SbnDataVarToDate(timestamp));
        updateCancella(tt); //[TODO da cambiare con timestamp
    }

    public void inserisciLegame(Tr_tit_tit tt) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);


        tabella.executeCustom("selectCancellato");
        List v = tabella.getElencoRisultati();
        if (v.size() > 0) {
            tt.setTS_VAR(((Tr_tit_tit) v.get(0)).getTS_VAR());
            tabella.executeCustomUpdate("update");
        } else
            tabella.insert(tt);

    }

    public void inserisciLegame(Vl_titolo_tit_b vl) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setUTE_INS(vl.getUTE_INS());
        tt.setUTE_VAR(vl.getUTE_VAR());
        tt.setBID_BASE(vl.getBID_BASE());
        tt.setBID_COLL(vl.getBID());
        tt.setCD_NATURA_BASE(vl.getCD_NATURA_BASE());
        tt.setCD_NATURA_COLL(vl.getCD_NATURA_COLL());
        tt.setNOTA_TIT_TIT(vl.getNOTA_TIT_TIT());
        tt.setSEQUENZA(vl.getSEQUENZA());
        tt.setSEQUENZA_MUSICA(vl.getSEQUENZA_MUSICA());
        tt.setSICI(vl.getSICI());
        tt.setTP_LEGAME(vl.getTP_LEGAME());
        tt.setTP_LEGAME_MUSICA(vl.getTP_LEGAME_MUSICA());
        inserisciLegame(tt);
    }

    public void aggiornaLinkModificaNatura(Tb_titolo titolo, String tp_legame) throws IllegalArgumentException, InvocationTargetException, Exception {


        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_COLL(titolo.getBID());
        tt.setUTE_VAR(titolo.getUTE_VAR());
        tt.setTP_LEGAME(tp_legame);
        tt.setCD_NATURA_COLL(titolo.getCD_NATURA());


        // Inizio modifica almaviva2; in caso di cambio natura non viene impostata tutta la parte della condivisione
        tt.setFL_CONDIVISO(titolo.getFL_CONDIVISO());
        tt.setTS_CONDIVISO(TableDao.now());
        tt.setUTE_CONDIVISO(titolo.getUTE_VAR());
        // Fine modifica almaviva2; in caso di cambio natura non viene impostata tutta la parte della condivisione

        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustom("updateModificaNatura");

    }

    public void aggiornaMusica(Tb_titolo titolo, String tp_legame) throws IllegalArgumentException, InvocationTargetException, Exception {


        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_COLL(titolo.getBID());
        tt.setUTE_VAR(titolo.getUTE_VAR());
        tt.setTP_LEGAME(tp_legame);
        tt.setCD_NATURA_COLL(titolo.getCD_NATURA());
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustom("updateModificaMusica");

    }

    /**
      * @param string
      * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
      */
    public List cercaLegamiPerBidColl(String bid_coll, String tp_legame) throws IllegalArgumentException, InvocationTargetException, Exception {


        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_COLL(bid_coll);
        tt.setTP_LEGAME(tp_legame);
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustom("selectPerBidColl");
        List v = tabella.getElencoRisultati();

        return v;
    }


    //bug mantis 4980 (sbnweb) - evolutva 15/05/2012
    //ai poli è consentito fondere W su M o viceversa solo se entrambi i titoli hanno lo stesso padre,
    //a interfaccia diretta è sempre consentito
  	public List cercaPadrePerBid(String bid_base) throws IllegalArgumentException, InvocationTargetException, Exception {

  		Tr_tit_tit tt = new Tr_tit_tit();
  		tt.setBID_BASE(bid_base);
  		Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
  		tabella.executeCustom("selectBidPadre");
  		List v = tabella.getElencoRisultati();
  		return v;
  	}



 // Inizio modifica almaviva2 04.07.2012 - i legami a raccolta fattizia vanno spostati dal titolo fuso a quello di arrivo
    public List cercaLegamiPerBidBaseNaturaColl(String bid_base, String natura_coll) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(bid_base);
        tt.setCD_NATURA_COLL(natura_coll);
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustom("selectPerBidBaseNaturaColl");
        List v = tabella.getElencoRisultati();

        return v;
    }
 // Fine modifica almaviva2 04.07.2012 - i legami a raccolta fattizia vanno spostati dal titolo fuso a quello di arrivo


    /**
      * @param string
      * @return TableDao di Vl_titolo_tit_c
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
      */
    public List cercaLegamiPerBidCollNaturaColl(String bid_coll,String natura_base) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_c tt = new Vl_titolo_tit_c();
        tt.setCD_NATURA_BASE(natura_base);
        tt.setBID_COLL(bid_coll);
        Vl_titolo_tit_cResult tabella = new Vl_titolo_tit_cResult(tt);


        tabella.executeCustom("selectPerBidCollENaturaBase");
        List v = tabella.getElencoRisultati();

        return v;
    }


    /**
      * @param string
      * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
      */
    public List cercaLegamiMusica(String bid_base) throws IllegalArgumentException, InvocationTargetException, Exception {


        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(bid_base);

		// Inizio Modifica almaviva2 BUG MANTIS 4486 esercizio : mentre in cercaLegamiMusica viene impostato il campo bid_base
        // nella select succesiva (selectMusica) viene utilizzato il bid_coll che essendo non valorizzato manda in eccezione
        // tutta l'elaborazione; viene quindi impostato anche il sudetto campo con il bid corretto
        tt.setBID_COLL(bid_base);
        // Fine Modifica almaviva2 BUG MANTIS 4486 esercizio


        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustom("selectMusica");
        List v = tabella.getElencoRisultati();

        return v;
    }

    public List cercaRelTitUniMus(String bid_base) throws IllegalArgumentException, InvocationTargetException, Exception {


        Vl_titolo_tit_b tt = new Vl_titolo_tit_b();
        tt.setBID_BASE(bid_base);
        Vl_titolo_tit_bResult tabella = new Vl_titolo_tit_bResult(tt);
        tabella.executeCustom("selectRelTitUniMus");
        List v = tabella.getElencoRisultati();

        return v;
    }

    /**
      * @param string
      * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
      */
    public int contaLegamiPerBidColl(String bid_coll) throws IllegalArgumentException, InvocationTargetException, Exception {


        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_COLL(bid_coll);
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustom("countPerBidColl");
        return conta(tabella);
    }

    /** Legge da una tavola il valore del COUNT(*) */
    public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();


//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//
//        } catch (SQLException ecc) {
//            throw new EccezioneDB(1203);
//        }
    }

}
