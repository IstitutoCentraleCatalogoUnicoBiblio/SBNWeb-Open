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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_claResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * @author
 *
 */
public class TitoloClasse extends Tr_tit_cla {

	private static final long serialVersionUID = 1311975607906658126L;

	public TitoloClasse() {
    }

    /**
     * Method estraiTitoloClasse.
     * @param string
     * @param _id_partenza
     * @return Object
     * @throws InfrastructureException
     */
    public Tr_tit_cla estraiTitoloClasse(String bid, String cd_sistema, String cd_edizione, String classe)
        throws EccezioneDB, InfrastructureException {
        setCD_SISTEMA(cd_sistema);
        setCD_EDIZIONE(cd_edizione);
        setCLASSE(classe);
        setBID(bid);
        Tr_tit_claResult tavola = new Tr_tit_claResult(this);
        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();
        if (v.size() > 0)
            return (Tr_tit_cla) v.get(0);
        return null;
    }

    public Tr_tit_cla estraiTitoloClasse(String bid, String idclasse) throws EccezioneDB, InfrastructureException, EccezioneSbnDiagnostico {
    	//almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(idclasse);
        String sistema = sd.getSistema();
		String edizione = sd.getEdizione();
		//almaviva5_20090414
        if (sd.isDewey()) {
        	edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	sistema = sd.getSistema() + edizione;
        }
        String simbolo = sd.getSimbolo();

    	//almaviva5_20090414
    	if (sd.isDewey())
    		return estraiTitoloClasse(
    				bid,
    				sistema,
    				edizione,
    				simbolo);
    	else
    		return estraiTitoloClasse(
    				bid,
    				sistema,
    				"  ",
    				simbolo);
    }

    public List estraiTitoliClasse(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        Tr_tit_claResult tavola = new Tr_tit_claResult(this);
        tavola.executeCustom("selectPerTitolo");
        List TableDaoResult = tavola.getElencoRisultati();

        return TableDaoResult;
    }

    public void inserisci(Tr_tit_cla tc) throws EccezioneDB, InfrastructureException {
        Tr_tit_claResult tabella = new Tr_tit_claResult(tc);
        tabella.insert(tc);
    }

    public void update(Tr_tit_cla tc) throws EccezioneDB, InfrastructureException {
        Tr_tit_claResult tabella = new Tr_tit_claResult(tc);
        tabella.update(tc);

    }
    /**
     * Method updateTitoliClasse.
     * @param _id_partenza
     * @param _id_arrivo
     * @param _user
     * @param _sbnUser
     * @param vettoreDiLegami
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateTitoliClasse(
        Tr_tit_cla tit_cla,
        String idPartenza,
        String idArrivo,
        String user,
        String bid,
        TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_cla tc = new Tr_tit_cla();

        //almaviva5_20141114 edizioni ridotte
        SimboloDewey sdPartenza = SimboloDewey.parse(idPartenza);
        if (sdPartenza.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sdPartenza.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	tc.settaParametro(TableDao.XXXcd_sistemaPartenza, sdPartenza.getSistema() + edizione);
        	tc.settaParametro(TableDao.XXXcd_edizionePartenza, edizione);
        } else {
        	tc.settaParametro(TableDao.XXXcd_sistemaPartenza, sdPartenza.getSistema());
        	tc.settaParametro(TableDao.XXXcd_edizionePartenza, sdPartenza.getEdizione());
        }
        tc.settaParametro(TableDao.XXXclassePartenza, sdPartenza.getSimbolo());

        SimboloDewey sdArrivo = SimboloDewey.parse(idArrivo);
        if (sdArrivo.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sdArrivo.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	tc.settaParametro(TableDao.XXXcd_sistemaArrivo, sdArrivo.getSistema() + edizione);
        	tc.settaParametro(TableDao.XXXcd_edizioneArrivo, edizione);
        } else {
        	tc.settaParametro(TableDao.XXXcd_sistemaArrivo, sdArrivo.getSistema());
        	tc.settaParametro(TableDao.XXXcd_edizioneArrivo, sdArrivo.getEdizione());
        }
        tc.settaParametro(TableDao.XXXclasseArrivo, sdArrivo.getSimbolo());

        tc.setBID(bid);
        tc.setUTE_VAR(user);
        tc.setTS_VAR(tit_cla.getTS_VAR());
        Tr_tit_claResult tavola = new Tr_tit_claResult(tc);

        tavola.executeCustomUpdate("updateLegameClasse");

        Titolo titolo = new Titolo();
        Tb_titolo tb_titolo = titolo.estraiTitoloPerID(tc.getBID());
        tb_titolo.setUTE_VAR(user);
        tb_titolo.setTS_VAR(tc.getTS_VAR());
        Tb_titoloResult tb_titoloResult = new Tb_titoloResult(tb_titolo);

        tb_titoloResult.executeCustomUpdate("updateTitolo");

        AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tb_titolo);
        allineamentoTitolo.setTr_tit_cla(true);
        TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
        titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);

    }

    public void cancellaPerBid(String bid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_cla tit_luo = new Tr_tit_cla();
        tit_luo.setBID(bid);
        tit_luo.setUTE_VAR(ute_var);
        Tr_tit_claResult tavola = new Tr_tit_claResult(tit_luo);
        tavola.executeCustom("updateCancellaPerBid");
    }

    public void cancellaLegame(String bid, String id_partenza, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        //almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(id_partenza);
        //almaviva5_20090414
        if (sd.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	setCD_SISTEMA(sd.getSistema() + edizione);
        	setCD_EDIZIONE(edizione);
        } else {
        	setCD_SISTEMA(sd.getSistema());
            setCD_EDIZIONE("  ");
        }
    	setCLASSE(sd.getSimbolo());

        setUTE_VAR(ute_var);
        Tr_tit_claResult tavola = new Tr_tit_claResult(this);
        tavola.executeCustom("updateCancellaLegame");
    }

    /**
     * metodo utilizzato per
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List estraiLegamiTitoloClasse(String idclasse) throws IllegalArgumentException, InvocationTargetException, Exception {
    	//almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(idclasse);
         //almaviva5_20090414
        if (sd.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	setCD_SISTEMA(sd.getSistema() + edizione);
        	setCD_EDIZIONE(edizione);
        } else {
        	setCD_SISTEMA(sd.getSistema());
            setCD_EDIZIONE("  ");
        }
    	setCLASSE(sd.getSimbolo());

        Tr_tit_claResult tavola = new Tr_tit_claResult(this);
        tavola.executeCustom("selectTitoloPerClasse");
        List vettoreRisultati = tavola.getElencoRisultati();

        return vettoreRisultati;
    }

}
