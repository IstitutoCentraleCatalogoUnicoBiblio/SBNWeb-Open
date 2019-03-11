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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RepertorioTitoloUniforme extends Tr_rep_tit{


    /**
	 * 
	 */
	private static final long serialVersionUID = 3872570040752193794L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti");
	private boolean filtriValorizzati;

	public RepertorioTitoloUniforme(){

   }

    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
    public Tr_rep_tit valorizzaFiltri(Tr_rep_tit tr_rep_tit, CercaDatiAutType cerca) {
        filtriValorizzati = true;
        if (cerca == null)
            return tr_rep_tit;
        if (cerca.getT005_Range() != null) {
        	int filtro = cerca.getT005_Range().getTipoFiltroDate();
        	if (filtro < 2){
	            tr_rep_tit.settaParametro(TableDao.XXXdata_var_Da,cerca.getT005_Range().getDataDa().toString());
	            tr_rep_tit.settaParametro(TableDao.XXXdata_var_A,cerca.getT005_Range().getDataA().toString());
        	} else  if (filtro == 2){
	            tr_rep_tit.settaParametro(TableDao.XXXdata_ins_Da,cerca.getT005_Range().getDataDa().toString());
	            tr_rep_tit.settaParametro(TableDao.XXXdata_ins_A,cerca.getT005_Range().getDataA().toString());
        	}
        }
        return tr_rep_tit;
    }

    /** Legge da una tavola il valore del COUNT(*) */
    private int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }

	public int contaRepertorioPerTitolo(
		String idArrivo,
		CercaDatiAutType cercaRepertorio) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(idArrivo);
		Tr_rep_titResult tavola = new Tr_rep_titResult(this);

        if (!filtriValorizzati)
            valorizzaFiltri(cercaRepertorio);
        tavola.executeCustom("countRepertorioPerTitolo");
        int n = conta(tavola);

        return n;
	}

	/**
	 * Method cercaRepertorioPerMarca.
	 * @param idArrivo
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaRepertorioPerTitolo(String idArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(idArrivo);
        Tr_rep_titResult tr_titResult = new Tr_rep_titResult(this);


        tr_titResult.executeCustom("selectRepertorioPerTitolo");
        return tr_titResult;
	}

	/**
	* Method cercaCitazioniInRepertori.
	* Se avessi una vista Vl_repertorio_tit potrei resituire un elenco di repertori legati
	* al titolo. invece restituisce un vettore di Tr_rep_tit.
	* @param vid
	* @return TableDao di Tr_rep_tit
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	*/
	public List cercaCitazioniInRepertori(String bid, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
		TableDao tavola = cercaRepertorioPerTitolo(bid);
        List v = tavola.getElencoRisultati();

		return v;
	}

    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_rep_tit tit_luo = new Tr_rep_tit();
        tit_luo.setBID(bid);
        tit_luo.setUTE_VAR(ute_var);
        Tr_rep_titResult tavola = new Tr_rep_titResult(tit_luo);
        tavola.executeCustom("updateCancellaPerBid");
    }

   	public boolean controllaEsistenzaLegame(Tr_rep_tit legame, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_rep_titResult tavola = new Tr_rep_titResult(legame);

		tavola.executeCustom("selectPerKeyCancellato");
        List vec = tavola.getElencoRisultati();

		for (int i=0;i<vec.size();i++){
			Tr_rep_tit rep_tit = (Tr_rep_tit) vec.get(i);
			timeHash.putTimestamp("Tr_rep_tit",rep_tit.getBID()+rep_tit.getID_REPERTORIO(),new SbnDatavar( rep_tit.getTS_VAR()));
		}
		return vec.size()>0;

	}

    public Tr_rep_tit estraiLegame(String bid, int id_repertorio) throws EccezioneDB, InfrastructureException {
        Tr_rep_tit rt = new Tr_rep_tit();
        rt.setBID(bid);
        rt.setID_REPERTORIO(id_repertorio);
        Tr_rep_titResult tavola = new Tr_rep_titResult(rt);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(rt.leggiAllParametro()));
        List vec = tavola.getElencoRisultati();

        if (vec.size()>0) return (Tr_rep_tit)vec.get(0);
        return null;

    }

    public void cancellaLegamiRepertorio(String idRepertorio, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        setID_REPERTORIO(Long.parseLong(idRepertorio));
        setUTE_VAR(user);
        Tr_rep_titResult tavola = new Tr_rep_titResult(this);
        tavola.executeCustom("cancellaLegamiRepertorio");
    }

}
