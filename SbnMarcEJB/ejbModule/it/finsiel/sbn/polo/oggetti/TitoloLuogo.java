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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_luoResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_luo;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe TitoloLuogo.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-mar-03
 */
public class TitoloLuogo extends Tr_tit_luo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7095769493124851608L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.TitoloLuogo");


    public TitoloLuogo() {

    }

    public Tr_tit_luo estraiTitoloLuogo(String bid, String lid) throws IllegalArgumentException, InvocationTargetException, Exception {


        Tr_tit_luo tl = new Tr_tit_luo();
        tl.setBID(bid);
        tl.setLID(lid);
        Tr_tit_luoResult tabella = new Tr_tit_luoResult(tl);
        tabella.executeCustom("selectPerBidELid");
        List v = tabella.getElencoRisultati();

        if (v.size() == 0)
            return null;
        return (Tr_tit_luo) v.get(0);
    }

    public Tr_tit_luo estraiTitoloLuogo(String bid, String lid, String tipoLegame) throws EccezioneDB, InfrastructureException {

        Tr_tit_luo tl = new Tr_tit_luo();
        tl.setBID(bid);
        tl.setLID(lid);
        tl.setTP_LUOGO(tipoLegame);
        Tr_tit_luoResult tabella = new Tr_tit_luoResult(tl);
        tabella.selectPerKey(tl.leggiAllParametro());
        List v = tabella.getElencoRisultati();

        if (v.size() == 0)
            return null;
        return (Tr_tit_luo) v.get(0);
    }

    public Tr_tit_luo verificaEsistenza(String bid, String lid, String tp_luogo) throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_tit_luo tl = new Tr_tit_luo();
        tl.setBID(bid);
        tl.setLID(lid);
        tl.setTP_LUOGO(tp_luogo);
        Tr_tit_luoResult tabella = new Tr_tit_luoResult(tl);
        tabella.executeCustom("verificaEsistenza");
        List v = tabella.getElencoRisultati();

        if (v.size() == 0)
            return null;
        return (Tr_tit_luo) v.get(0);
    }

    /**
     * Method estraiTitoliLuogo.
     * @param bid
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List estraiTitoliLuogo(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo titoloLuogo = new Tr_tit_luo();
        titoloLuogo.setBID(bid);
        Tr_tit_luoResult tavola = new Tr_tit_luoResult(titoloLuogo);

        tavola.executeCustom("selectPerTitolo");
        List TableDaoResult = tavola.getElencoRisultati();

        return TableDaoResult;
    }
    /**
     * Method estraiTitoliLuogo.
     * @param bid
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List estraiTitoliLuogoTipo(String bid,String tipoLuogo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo titoloLuogo = new Tr_tit_luo();
        titoloLuogo.setBID(bid);
        titoloLuogo.setTP_LUOGO(tipoLuogo);
        Tr_tit_luoResult tavola = new Tr_tit_luoResult(titoloLuogo);


        tavola.executeCustom("selectPerTitolo");
        List TableDaoResult = tavola.getElencoRisultati();

        return TableDaoResult;
    }

    /**
     * Method updateTitoliLuogo.
     * @param idPartenza
     * @param idArrivo
     * @param user
     * @param sbnUser
     * @param vettoreDiLegami
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateTitoliLuogo(
        String idPartenza,
        String idArrivo,
        String user,
        SbnUserType sbnUser,
        String bid)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo titoloLuogo = new Tr_tit_luo();
        titoloLuogo.settaParametro(TableDao.XXXidPartenza, idPartenza);
        titoloLuogo.settaParametro(TableDao.XXXidArrivo, idArrivo);
        titoloLuogo.setUTE_VAR(user);
        titoloLuogo.setBID(bid);
        titoloLuogo.setTS_VAR(titoloLuogo.getTS_VAR());
        Tr_tit_luoResult tavola = new Tr_tit_luoResult(titoloLuogo);
        tavola.executeCustom("updateLegameLuogo");

        Titolo titolo = new Titolo();
        Tb_titolo tb_titolo = titolo.estraiTitoloPerID(titoloLuogo.getBID());
        if (tb_titolo != null) {
            tb_titolo.setUTE_VAR(user);
            tb_titolo.setTS_VAR(titoloLuogo.getTS_VAR());
            Tb_titoloResult tb_titoloResult = new Tb_titoloResult(tb_titolo);
            tb_titoloResult.executeCustom("updateTitolo");
            AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tb_titolo);
            allineamentoTitolo.setTr_tit_luo(true);
            TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
            titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
        }
    }

    /**
     * Method cercaLegamiLuogoTitolo.
     * @param _id_partenza
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaLegamiLuogoTitolo(String idPartenza) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo tr_tit_luo = new Tr_tit_luo();
        tr_tit_luo.setLID(idPartenza);
        Tr_tit_luoResult tavola = new Tr_tit_luoResult(tr_tit_luo);


        tavola.executeCustom("selectPerLuogo");
        return tavola;
    }

    public void cancellaPerBid(String bid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo tit_luo = new Tr_tit_luo();
        tit_luo.setBID(bid);
        tit_luo.setUTE_VAR(ute_var);
        Tr_tit_luoResult tavola = new Tr_tit_luoResult(tit_luo);
        tavola.executeCustom("updateCancellaPerBid");
    }

    public void cancellaLegameTitoloLuogo(String id_partenza, String bid, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo tit_luo = new Tr_tit_luo();
        tit_luo.setBID(bid);
        tit_luo.setLID(id_partenza);
        tit_luo.setUTE_VAR(user);
        Tr_tit_luoResult tavola = new Tr_tit_luoResult(tit_luo);
        tavola.executeCustom("updateCancellaLegameTitLuo");
    }

    /**
     * Inserisce un legame titolo luogo. Verifca che già non esista, magari cancellato.
     * Se esiste lo modifica
     * @param tl
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void inserisci(Tr_tit_luo tl) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luoResult tabella = new Tr_tit_luoResult(tl);


        Tr_tit_luo tlEs = verificaEsistenza(tl.getBID(), tl.getLID() ,tl.getTP_LUOGO());
        if (tlEs != null) {
            tl.setTS_VAR(tlEs.getTS_VAR());
            tabella.update(tl);
        } else {
            tabella.insert(tl);
        }
    }

    public void update(Tr_tit_luo tl) throws EccezioneDB, InfrastructureException {
        Tr_tit_luoResult tabella = new Tr_tit_luoResult(tl);


        tabella.update(tl);

    }

}
