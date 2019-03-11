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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe TitoloMarca.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-mar-03
 */
public class TitoloMarca extends Tr_tit_mar {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1947730425251695486L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.TitoloMarca");

    public TitoloMarca() {
    }

    /**
     * Esegue la ricerca per marca
     * viene utilizzata in fase di validazione per cancellazione
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloMarca(String idMarca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_mar tr_tit_mar = new Tr_tit_mar();
        tr_tit_mar.setMID(idMarca);
        Tr_tit_marResult tavola = new Tr_tit_marResult(tr_tit_mar);

        tavola.executeCustom("selectPerMarca");
        return tavola;
    }

	/**
	 * Method estraiTitoloMarca.
	 * @param string
	 * @param _id_partenza
	 * @return Object
	 * @throws InfrastructureException
	 */
	public Tr_tit_mar estraiTitoloMarca(String mid, String bid) throws EccezioneDB, InfrastructureException {
		Tr_tit_mar tr_tit_mar = new Tr_tit_mar();
        tr_tit_mar.setMID(mid);
        tr_tit_mar.setBID(bid);
        Tr_tit_marResult tavola = new Tr_tit_marResult(tr_tit_mar);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(tr_tit_mar.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size()>0)
            return (Tr_tit_mar)v.get(0);
        return null;
     }


	/**
	 * Method estraiTitoliMarca.
	 * @param string
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List estraiTitoliMarca(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_mar titoloMarca = new Tr_tit_mar();
        titoloMarca.setBID(bid);
        Tr_tit_marResult tavola = new Tr_tit_marResult(titoloMarca);


        tavola.executeCustom("selectPerTitolo");
        List TableDaoResult = tavola.getElencoRisultati();

        return TableDaoResult;
	}

	/**
	 * Method updateTitoliMarca.
	 * @param idPartenza
	 * @param idArrivo
	 * @param user
	 * @param sbnUser
	 * @param vettoreDiLegami
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void updateTitoliMarca(
		String idPartenza,
		String idArrivo,
		String user,
		SbnUserType sbnUser,
		String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_tit_mar titoloMarca = new Tr_tit_mar();
		titoloMarca.settaParametro(TableDao.XXXidPartenza,idPartenza);
		titoloMarca.settaParametro(TableDao.XXXidArrivo,idArrivo);
		titoloMarca.setBID(bid);
		titoloMarca.setUTE_VAR(user);
//		titoloMarca.setTS_VAR(titoloMarca.getTS_VAR());
		Tr_tit_marResult tavola = new Tr_tit_marResult(titoloMarca);

		tavola.executeCustom("updateLegameMarca");

		Titolo titolo = new Titolo();
		Tb_titolo tb_titolo = titolo.estraiTitoloPerID(titoloMarca.getBID());
        if (tb_titolo != null) {
    		tb_titolo.setUTE_VAR(user);
    //		tb_titolo.setTS_VAR(titoloMarca.getTS_VAR());
    		Tb_titoloResult tb_titoloResult = new Tb_titoloResult(tb_titolo);

    		tb_titoloResult.executeCustom("updateTitolo");

    		AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tb_titolo);
            allineamentoTitolo.setTr_tit_mar(true);
            TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
            titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
        }
	}

	/**
	 * Method cercaLegamiMarcaTitolo.
	 * @param _id_partenza
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaLegamiMarcaTitolo(String idPartenza) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_tit_mar tr_tit_mar = new Tr_tit_mar();
		tr_tit_mar.setMID(idPartenza);
        Tr_tit_marResult tavola = new Tr_tit_marResult(tr_tit_mar);


        tavola.executeCustom("selectPerMarca");
		return tavola;
	}

    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_mar tit_luo = new Tr_tit_mar();
        tit_luo.setBID(bid);
        tit_luo.setUTE_VAR(ute_var);
        Tr_tit_marResult tavola = new Tr_tit_marResult(tit_luo);
        tavola.executeCustom("updateCancellaPerBid");
    }

    public void inserisci(Tr_tit_mar tm) throws EccezioneDB, InfrastructureException {
        Tr_tit_marResult tavola = new Tr_tit_marResult(tm);


        tavola.insert(tm);

    }

    public void update(Tr_tit_mar tm) throws EccezioneDB, InfrastructureException {
        Tr_tit_marResult tavola = new Tr_tit_marResult(tm);


        tavola.update(tm);

    }

	public void cancellaLegameTitoloMarca(
		String id_partenza,
		String bid,
		String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_mar tit_mar = new Tr_tit_mar();
        tit_mar.setBID(bid);
        tit_mar.setMID(id_partenza);
        tit_mar.setUTE_VAR(user);
        Tr_tit_marResult tavola = new Tr_tit_marResult(tit_mar);
        tavola.executeCustom("updateCancellaLegameTitMar");
	}


}
