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

import gnu.trove.THashMap;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_sog_bibResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_sog_tit_bibResult;
import it.finsiel.sbn.polo.dao.vo.MaxLivelloLegameSog;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.GestoreLegami;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_sog_tit_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author
 *
 */
public class SoggettoTitolo extends Tr_tit_sog_bib {


	private static final long serialVersionUID = 4114754436461693997L;

	private static Logger log = Logger.getLogger("it.finsiel.sbn.polo.oggetti.SoggettoTitolo");

	private Map<String, String> maxLivelloAutSogg = new THashMap();

    public SoggettoTitolo() {
        super();
    }


	/**
	 * Method estraiTitoloSoggetto.
	 * @param spostaId
	 * @param idPartenza
	 * @return Tr_tit_sog_bib
	 * @throws InfrastructureException
	 */
	public Tr_tit_sog_bib estraiTitoloSoggetto(String spostaId, String idPartenza) throws EccezioneDB, InfrastructureException {

		Tr_tit_sog_bib titoloSoggetto = new Tr_tit_sog_bib();

        titoloSoggetto.setCID(idPartenza);
        titoloSoggetto.setBID(spostaId);
        Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(titoloSoggetto);


        tavola.valorizzaElencoRisultati(tavola.selectPerKeyPerFusione(titoloSoggetto.leggiAllParametro()));
        List TableDaoResult = tavola.getElencoRisultati();

        if (TableDaoResult.size()>0)
            return (Tr_tit_sog_bib)TableDaoResult.get(0);
		return null;
	}

	/**
	 * Method estraiTitoliSoggetto.
	 * @param string
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List estraiTitoliSoggetto(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_sog_bib titoloSoggetto = new Tr_tit_sog_bib();
        titoloSoggetto.setBID(bid);
        Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(titoloSoggetto);


        tavola.executeCustom("selectPerTitolo");
        List TableDaoResult = tavola.getElencoRisultati();

        return TableDaoResult;
 	}


	/**
	 * Method updateTitoliSoggetto.
	 * @param idPartenza
	 * @param soggettoArrivo
	 * @param user
	 * @param vettoreDiLegami
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void updateTitoliSoggetto(
		String idPartenza,
		Tb_soggetto soggettoArrivo,
		String user,
		String bid,
		TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_tit_sog_bib titoloSoggetto = new Tr_tit_sog_bib();
		titoloSoggetto.settaParametro(TableDao.XXXidPartenza, idPartenza);
		titoloSoggetto.settaParametro(TableDao.XXXidArrivo, soggettoArrivo.getCID());
		titoloSoggetto.setBID(bid);
		titoloSoggetto.setUTE_VAR(user);
		titoloSoggetto.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_sog_bib",bid+idPartenza)));
		//almaviva5_20140221
		titoloSoggetto.setCD_SOGG(soggettoArrivo.getCD_SOGGETTARIO());
		Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(titoloSoggetto);

		tavola.executeCustom("updateLegameSoggetto");

		Titolo titolo = new Titolo();
		Tb_titolo tb_titolo = titolo.estraiTitoloPerID(bid);
        if (tb_titolo != null) {
    		tb_titolo.setUTE_VAR(user);
    		tb_titolo.setTS_VAR(titoloSoggetto.getTS_VAR());
    		Tb_titoloResult tb_titoloResult = new Tb_titoloResult(tb_titolo);

    		tb_titoloResult.executeCustom("updateTitolo");

            AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tb_titolo);
            allineamentoTitolo.setTr_tit_sog(true);
            TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
            titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
        }
	}



	/**
	 * Method cercaLegamiSoggettoTitolo.
	 * @param _id_partenza
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaLegamiSoggettoTitolo(String idPartenza) throws IllegalArgumentException, InvocationTargetException, Exception {
		setCID(idPartenza);
        Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(this);


        tavola.executeCustom("selectTitoloPerSoggetto");
		return tavola;
	}


    public void cancellaPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_sog_bib tsb = new Tr_tit_sog_bib();
        tsb.setBID(bid);
        tsb.setUTE_VAR(ute_var);
        Tr_tit_sog_bibResult dao = new Tr_tit_sog_bibResult(tsb);
        dao.executeCustom("updateCancellaPerBid");
    }
	/**
	 * Method cancellaLegameTitoloSoggetto.
	 * @param _id_partenza
	 * @param string
	 * @param _user
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaLegameTitoloSoggetto(
		String id_partenza,
		String bid,
		String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_sog_bib tit_sog = new Tr_tit_sog_bib();
        tit_sog.setBID(bid);
        tit_sog.setCID(id_partenza);
        tit_sog.setUTE_VAR(user);
        Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(tit_sog);
        tavola.executeCustom("updateCancellaLegameTitSog");
	}

    public void inserisci(Tr_tit_sog_bib ts) throws EccezioneDB, InfrastructureException {
        Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(ts);
        tavola.insert(ts);

    }

    public void update(Tr_tit_sog_bib ts) throws EccezioneDB, InfrastructureException {
        Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(ts);
        tavola.update(null);

    }

	public List verificaEsistenzainVista(
	    String bid,
	    String cid,
	    String cd_polo,
	    String cd_biblioteca
		) throws IllegalArgumentException, InvocationTargetException, Exception {
	        Vl_sog_tit_bib vl_sog_tit_bib = new Vl_sog_tit_bib();
	        vl_sog_tit_bib.setBID(bid);
	        vl_sog_tit_bib.setCID(cid);
	        vl_sog_tit_bib.setCD_POLO(cd_polo);
	        vl_sog_tit_bib.setCD_BIBLIOTECA(cd_biblioteca);

			Vl_sog_tit_bibResult tavola = new Vl_sog_tit_bibResult(vl_sog_tit_bib);
			tavola.executeCustom("verifica_esistenza");

	        List TableDaoResult = tavola.getElencoRisultati();

	        return TableDaoResult;
 	}

	//almaviva5_20091103
	public boolean verificaEsistenzaLegame(String bid, String cid)
		throws IllegalArgumentException, InvocationTargetException, Exception {

		Tr_tit_sog_bib titsog = new Tr_tit_sog_bib();
		titsog.setBID(bid);
		titsog.setCID(cid);
		Tr_tit_sog_bibResult dao = new Tr_tit_sog_bibResult(titsog);

		dao.executeCustom("selectPerKeyEsistenza");

		return (dao.getCount() > 0);
	}

	public SbnLivello cercaMaxLivelloAutLegameSoggettario(Tb_soggetto soggetto, String bid)
		throws IllegalArgumentException, InvocationTargetException, Exception {

		if (bid == null)
			return SbnLivello.VALUE_0;	//livello 05

		String cdSogg = soggetto.getCD_SOGGETTARIO();

		String maxLivAut = maxLivelloAutSogg.get(cdSogg);
		if (ValidazioneDati.isFilled(maxLivAut))
			return SbnLivello.valueOf(maxLivAut);

		log.debug("lanciato cercaMaxLivelloAutLegameSoggettario() per : " + cdSogg + ", " + bid);

		Vl_sog_tit_bib vl_sog_tit_bib = new Vl_sog_tit_bib();
        vl_sog_tit_bib.setBID(bid);
        vl_sog_tit_bib.setCD_SOGGETTARIO(cdSogg);

		Vl_sog_tit_bibResult tavola = new Vl_sog_tit_bibResult(vl_sog_tit_bib);
		tavola.executeCustom("cercaMaxLivelloAutLegameSoggettario");
        List result = tavola.getElencoRisultati();
        if (!ValidazioneDati.isFilled(result))
        	maxLivAut = SbnLivello.VALUE_0.toString();	//livello 05
        else {
            MaxLivelloLegameSog out = (MaxLivelloLegameSog) result.get(0);
            maxLivAut = out.getMaxLivAut();
        }

        maxLivelloAutSogg.put(cdSogg, maxLivAut);
        return SbnLivello.valueOf(maxLivAut);
	}

	//row[0]=bid
	//row[1]=sogg.
	//row[2]=ute_var
	//row[3]=livAut
	public MaxLivelloLegameSog cercaUteVarMaxLivelloAutLegameSoggettario(Tb_soggetto soggetto, String bid)
		throws IllegalArgumentException, InvocationTargetException, Exception {

		if (bid == null)
			return null;

		Vl_sog_tit_bib vl_sog_tit_bib = new Vl_sog_tit_bib();
	    vl_sog_tit_bib.setBID(bid);
	    vl_sog_tit_bib.setCD_SOGGETTARIO(soggetto.getCD_SOGGETTARIO());

		Vl_sog_tit_bibResult dao = new Vl_sog_tit_bibResult(vl_sog_tit_bib);
		dao.executeCustom("cercaMaxLivelloAutLegameSoggettario");
	    List result = dao.getElencoRisultati();
	    if (!ValidazioneDati.isFilled(result))
	    	return null;

	    MaxLivelloLegameSog row = (MaxLivelloLegameSog) result.get(0);
		return row;
	}


	public int getPosizioneLegame(Tb_soggetto tb_soggetto,	String bid) throws Exception {
		//almaviva5_20120507 evolutive CFI
		if (bid == null)
			return 0;

		Tr_tit_sog_bib tr_tit_sog_bib = new Tr_tit_sog_bib();
		tr_tit_sog_bib.setBID(bid);
		tr_tit_sog_bib.setCID(tb_soggetto.getCID());

		Tr_tit_sog_bibResult dao = new Tr_tit_sog_bibResult(tr_tit_sog_bib);
		dao.executeCustom("selectPerKey");
		List<Tr_tit_sog_bib> legami = dao.getElencoRisultati();

		Tr_tit_sog_bib legame = ValidazioneDati.first(legami);

		return legame != null ? legame.getPOSIZIONE() : 0;
	}


	public void rankLegameTitoloSoggetto(String bid,
			LegameElementoAutType legameAut, SbnUserType sbnUser,
			SbnTipoOperazione tipoOperazione, int direzione) throws Exception {

		short rank = GestoreLegami.getPosizioneLegame(legameAut);
		String cdUtente = sbnUser.getBiblioteca() + sbnUser.getUserId();

		Tr_tit_sog_bib tsb = new Tr_tit_sog_bib();
		tsb.setBID(bid);
		tsb.setCID(legameAut.getIdArrivo());
		tsb.setPOSIZIONE(rank);

		Tr_tit_sog_bibResult dao = new Tr_tit_sog_bibResult(tsb);
		if (direzione < 0)
			dao.executeCustom("rankLegameTitoloSoggettoDown");
		else
			dao.executeCustom("rankLegameTitoloSoggettoUp");

		List<Tr_tit_sog_bib> risultati = dao.getElencoRisultati();
		if (!ValidazioneDati.isFilled(risultati) )
			return;

		if (tipoOperazione.getType() == SbnTipoOperazione.MODIFICA_TYPE) {
			Tr_tit_sog_bib first = ValidazioneDati.first(risultati);
			if (first.getPOSIZIONE() != rank)
				//non ci sono valori sovrapposti
				return;
		}

		if (tipoOperazione.getType() == SbnTipoOperazione.CANCELLA_TYPE)
			//il prossimo legame prenderà il posto di quello cancellato
			--rank;

		for (Tr_tit_sog_bib legame : risultati) {
			legame.setPOSIZIONE(direzione < 0 ? --rank : ++rank);
			legame.setUTE_VAR(cdUtente);
			dao = new Tr_tit_sog_bibResult(legame);
			dao.update(null);
		}

	}

}
