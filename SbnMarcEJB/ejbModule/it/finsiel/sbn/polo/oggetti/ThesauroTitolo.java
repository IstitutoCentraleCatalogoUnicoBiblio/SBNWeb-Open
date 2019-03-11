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
import it.finsiel.sbn.polo.dao.entity.tavole.Trs_termini_titoli_bibliotecheResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_the_tit_bibResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Trs_termini_titoli_biblioteche;
import it.finsiel.sbn.polo.orm.viste.Vl_the_tit_bib;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 *
 */
public class ThesauroTitolo extends Trs_termini_titoli_biblioteche{


    /**
	 * 
	 */
	private static final long serialVersionUID = 5635212101674111819L;

	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti");

	private boolean filtriValorizzati;

    public ThesauroTitolo() {

    }


	/**
	 * Method estraiTitoloThesauro.
	 * @param spostaId
	 * @param idPartenza
	 * @return Tr_tit_sog_bib
	 * @throws InfrastructureException
	 */
	public Trs_termini_titoli_biblioteche estraiTitoloThesauro(String spostaId, String idPartenza) throws EccezioneDB, InfrastructureException {
		Trs_termini_titoli_biblioteche titoloThesauro = new Trs_termini_titoli_biblioteche();
		titoloThesauro.setDID(idPartenza);
		titoloThesauro.setBID(spostaId);
        Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(titoloThesauro);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(titoloThesauro.leggiAllParametro()));
        List TableDaoResult = tavola.getElencoRisultati();

        if (TableDaoResult.size()>0)
            return (Trs_termini_titoli_biblioteche)TableDaoResult.get(0);
		return null;
	}

	/**
	 * Method estraiTitoliThesauro
	 * @param string
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List estraiTitoliThesauro(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
		Trs_termini_titoli_biblioteche titoloThesauro = new Trs_termini_titoli_biblioteche();
		titoloThesauro.setBID(bid);
        Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(titoloThesauro);


        tavola.executeCustom("selectPerTitolo");
        List TableDaoResult = tavola.getElencoRisultati();

        return TableDaoResult;
 	}


	/**
	 * Method updateTitoliThesauro.
	 * @param idPartenza
	 * @param idArrivo
	 * @param user
	 * @param vettoreDiLegami
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void updateTitoliThesauro(
		String idPartenza,
		String idArrivo,
		String user,
		String bid,
		TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
		Trs_termini_titoli_biblioteche titoloThesauro = new Trs_termini_titoli_biblioteche();
		titoloThesauro.settaParametro(TableDao.XXXidPartenza,idPartenza);
		titoloThesauro.settaParametro(TableDao.XXXidArrivo,idArrivo);
		titoloThesauro.setBID(bid);
		titoloThesauro.setUTE_VAR(user);
		titoloThesauro.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Trs_termini_titoli_biblioteche",bid+idPartenza)));
		Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(titoloThesauro);

		tavola.executeCustom("updateLegameThesauro");

		Titolo titolo = new Titolo();
		Tb_titolo tb_titolo = titolo.estraiTitoloPerID(bid);
        if (tb_titolo != null) {
    		tb_titolo.setUTE_VAR(user);
    		tb_titolo.setTS_VAR(titoloThesauro.getTS_VAR());
    		Tb_titoloResult tb_titoloResult = new Tb_titoloResult(tb_titolo);

    		tb_titoloResult.executeCustom("updateTitolo");

            AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tb_titolo);
            allineamentoTitolo.setTrs_termini_titoli_biblioteche(true);
            TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
            titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
        }
	}



	/**
	 * Method cercaLegamiThesauroTitolo.
	 * @param _id_partenza
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaLegamiThesauroTitolo(String idPartenza) throws IllegalArgumentException, InvocationTargetException, Exception {
		setDID(idPartenza);
		Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(this);
        tavola.executeCustom("selectTitoloPerThesauro");
		return tavola;
	}


    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Trs_termini_titoli_biblioteche titoloThesauro = new Trs_termini_titoli_biblioteche();
    	titoloThesauro.setBID(bid);
    	titoloThesauro.setUTE_VAR(ute_var);
    	Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(titoloThesauro);
        tavola.executeCustom("updateCancellaPerBid");
    }
	/**
	 * Method cancellaLegameTitoloThesauro.
	 * @param _id_partenza
	 * @param string
	 * @param _user
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaLegameTitoloThesauro(
		String id_partenza,
		String bid,
		String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		Trs_termini_titoli_biblioteche titoloThesauro = new Trs_termini_titoli_biblioteche();
		titoloThesauro.setBID(bid);
		titoloThesauro.setDID(id_partenza);
		titoloThesauro.setUTE_VAR(user);
		Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(titoloThesauro);
        tavola.executeCustom("updateCancellaLegameTitThe");
	}

    public void inserisci(Trs_termini_titoli_biblioteche ts) throws EccezioneDB, InfrastructureException {
    	Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(ts);
        tavola.insert(ts);

    }

    public void update(Trs_termini_titoli_biblioteche ts) throws EccezioneDB, InfrastructureException {
    	Trs_termini_titoli_bibliotecheResult tavola = new Trs_termini_titoli_bibliotecheResult(ts);
        tavola.update(ts);

    }
	public List verificaEsistenzainVista(
	    String bid,
	    String did,
	    String cd_polo,
	    String cd_biblioteca
		) throws IllegalArgumentException, InvocationTargetException, Exception {
			Vl_the_tit_bib vl_the_tit_bib = new Vl_the_tit_bib();
	        vl_the_tit_bib.setBID(bid);
	        vl_the_tit_bib.setDID(did);
	        vl_the_tit_bib.setCD_POLO(cd_polo);
	        vl_the_tit_bib.setCD_BIBLIOTECA(cd_biblioteca);

	        Vl_the_tit_bibResult tavola = new Vl_the_tit_bibResult(vl_the_tit_bib);
			tavola.executeCustom("verifica_esistenza");

	        List TableDaoResult = tavola.getElencoRisultati();

	        return TableDaoResult;
 	}

}
