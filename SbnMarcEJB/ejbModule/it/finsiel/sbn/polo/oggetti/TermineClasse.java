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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_the_claResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.GestoreLegami;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tr_the_cla;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

public class TermineClasse extends Tr_the_cla {

	private static final long serialVersionUID = -8715663091915336529L;

	public Tr_the_cla getLegameTermineClasse(String idPartenza, String idArrivo)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

		//almaviva5_20141119 edizioni ridotte
		SimboloDewey sd = SimboloDewey.parse(idArrivo);
		String edizione = sd.getEdizione();
		if (sd.isDewey()) {
			edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
	        if (!ValidazioneDati.isFilled(edizione))
	        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
		}

		return getLegameTermineClasse(idPartenza,
				sd.getSistema(),
				edizione,
				sd.getSimbolo());
	}

	public Tr_the_cla getLegameTermineClasse(String idPartenza,
			String cdSistema, String cdEdizione, String classe)
			throws IllegalArgumentException, InvocationTargetException,	Exception {
		Tr_the_cla tr_the_cla = new Tr_the_cla();
		tr_the_cla.setDID(idPartenza);

		tr_the_cla.setCD_SISTEMA(cdSistema + cdEdizione);
		tr_the_cla.setCD_EDIZIONE(cdEdizione);
		tr_the_cla.setCLASSE(classe);

		Tr_the_claResult tavola = new Tr_the_claResult(tr_the_cla);
		tavola.executeCustom("getLegameTermineClasse");
		List risultato = tavola.getElencoRisultati();

		return (Tr_the_cla) (ValidazioneDati.isFilled(risultato) ? risultato.get(0) : null);
}

	public void inserisciLegameTermineClasse(String _t001, String _c2_935,
			LegameElementoAutType autLegato, String cdUtente,
			TimestampHash _timeHash) throws EccezioneSbnDiagnostico,
			InfrastructureException {

		//almaviva5_20141119 edizioni ridotte
		SimboloDewey sd = SimboloDewey.parse(autLegato.getIdArrivo());
		String edizione = sd.getEdizione();
		if (sd.isDewey()) {
			edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
	        if (!ValidazioneDati.isFilled(edizione))
	        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
		}

		String cds = Decodificatore.getCd_tabella("Tb_termine_thesauro", "cd_the", _c2_935.toUpperCase());
		if (cds == null)
			throw new EccezioneSbnDiagnostico(3088, "Codice errato");

		Tr_the_cla tr_the_cla = new Tr_the_cla();
		tr_the_cla.setCD_THE(cds);
		tr_the_cla.setDID(_t001);
		tr_the_cla.setCD_SISTEMA(sd.getSistema());
		tr_the_cla.setCD_EDIZIONE(edizione);
		tr_the_cla.setCLASSE(sd.getSimbolo());

		Tr_the_claResult tavola = new Tr_the_claResult(tr_the_cla);
		Tr_the_cla tr_the_claOld = tavola.get();

		//check timestamp
		if (tr_the_claOld != null) {	//esiste?
			Timestamp tsVar = ConverterDate.SbnDataVarToDate(_timeHash.getTimestamp("tr_the_cla", tr_the_claOld.getUniqueId()));
			if (!tr_the_claOld.getTS_VAR().equals(tsVar))
				throw new EccezioneSbnDiagnostico(3014);

			tr_the_cla = tr_the_claOld;
		} else {
			if (sd.isDewey())
				tr_the_cla.setCD_SISTEMA("D" + edizione);

			tr_the_cla.setTS_INS(TableDao.now());
			tr_the_cla.setUTE_INS(cdUtente);
		}

		tr_the_cla.setPOSIZIONE(GestoreLegami.getPosizioneLegame(autLegato) );

		tr_the_cla.setNOTA_THE_CLA(autLegato.getNoteLegame());
		tr_the_cla.setUTE_VAR(cdUtente);
		tr_the_cla.setFL_CANC("N");

		tavola.save(tr_the_cla);
	}

	public void cancellaLegameTermineClasse(String _t001, String _c2_935, LegameElementoAutType autLegato, String cdUtente,
			TimestampHash _timeHash) throws EccezioneSbnDiagnostico,
			InfrastructureException {

		//almaviva5_20141119 edizioni ridotte
		SimboloDewey sd = SimboloDewey.parse(autLegato.getIdArrivo());
		String edizione = sd.getEdizione();
		if (sd.isDewey()) {
			edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
	        if (!ValidazioneDati.isFilled(edizione))
	        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
		}

		String cds = Decodificatore.getCd_tabella("Tb_termine_thesauro", "cd_the", _c2_935.toUpperCase());
		if (cds == null)
			throw new EccezioneSbnDiagnostico(3088, "Codice errato");

		Tr_the_cla tr_the_cla = new Tr_the_cla();
		tr_the_cla.setCD_THE(cds);
		tr_the_cla.setDID(_t001);
		tr_the_cla.setCD_SISTEMA(sd.getSistema());
		tr_the_cla.setCD_EDIZIONE(edizione);
		tr_the_cla.setCLASSE(sd.getSimbolo());

		Tr_the_claResult tavola = new Tr_the_claResult(tr_the_cla);
		Tr_the_cla tr_the_claOld = tavola.get();

		//check timestamp
		if (tr_the_claOld != null) {	//esiste?
			Timestamp tsVar = ConverterDate.SbnDataVarToDate(_timeHash.getTimestamp("tr_the_cla", tr_the_claOld.getUniqueId()));
			if (!tr_the_claOld.getTS_VAR().equals(tsVar))
				throw new EccezioneSbnDiagnostico(3014);

			tr_the_cla = tr_the_claOld;
		} else
			throw new EccezioneSbnDiagnostico(3029); //legame inesistente

		tr_the_cla.setPOSIZIONE((short) 0);

		//tr_the_cla.setNOTA_THE_CLA(autLegato.getNoteLegame());
		tr_the_cla.setUTE_VAR(cdUtente);
		tr_the_cla.setFL_CANC("S");

		tavola.save(tr_the_cla);
	}

	public int modificaLegameTermineClasse(String _t001, String _c2_935,
			LegameElementoAutType autLegato, String cdUtente,
			TimestampHash _timeHash) throws EccezioneSbnDiagnostico,
			InfrastructureException {

		//almaviva5_20141119 edizioni ridotte
		SimboloDewey sd = SimboloDewey.parse(autLegato.getIdArrivo());
		String edizione = sd.getEdizione();
		if (sd.isDewey()) {
			edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
	        if (!ValidazioneDati.isFilled(edizione))
	        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
		}

		String cds = Decodificatore.getCd_tabella("Tb_termine_thesauro", "cd_the", _c2_935.toUpperCase());
		if (cds == null)
			throw new EccezioneSbnDiagnostico(3088, "Codice errato");

		Tr_the_cla tr_the_cla = new Tr_the_cla();
		tr_the_cla.setCD_THE(cds);
		tr_the_cla.setDID(_t001);
		tr_the_cla.setCD_SISTEMA(sd.getSistema());
		tr_the_cla.setCD_EDIZIONE(edizione);
		tr_the_cla.setCLASSE(sd.getSimbolo());

		Tr_the_claResult tavola = new Tr_the_claResult(tr_the_cla);
		Tr_the_cla tr_the_claOld = tavola.get();

		//check timestamp
		if (tr_the_claOld != null) {	//esiste?
			Timestamp tsVar = ConverterDate.SbnDataVarToDate(_timeHash.getTimestamp("tr_the_cla", tr_the_claOld.getUniqueId()));
			if (!tr_the_claOld.getTS_VAR().equals(tsVar))
				throw new EccezioneSbnDiagnostico(3014);

			tr_the_cla = tr_the_claOld;
		} else
			throw new EccezioneSbnDiagnostico(3029); //legame inesistente

		short oldRank = tr_the_cla.getPOSIZIONE();
		short newRank = GestoreLegami.getPosizioneLegame(autLegato);
		int direction = oldRank - newRank;

		tr_the_cla.setPOSIZIONE(newRank);

		String note = autLegato.getNoteLegame();
		if (ValidazioneDati.isFilled(note))
			tr_the_cla.setNOTA_THE_CLA(note);

		tr_the_cla.setUTE_VAR(cdUtente);
		tr_the_cla.setFL_CANC("N");

		tavola.save(tr_the_cla);

		return direction;
	}

	public void rankLegameTermineClasse(String _t001, String _c2_935,
			LegameElementoAutType autLegato, String cdUtente, SbnTipoOperazione tipoOperazione, int direzione)
			throws EccezioneSbnDiagnostico, InvocationTargetException,
			InfrastructureException, Exception {

		//almaviva5_20141119 edizioni ridotte
		SimboloDewey sd = SimboloDewey.parse(autLegato.getIdArrivo());
		String edizione = sd.getEdizione();
		if (sd.isDewey()) {
			edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
	        if (!ValidazioneDati.isFilled(edizione))
	        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
		}

		String cds = Decodificatore.getCd_tabella("Tb_termine_thesauro", "cd_the", _c2_935.toUpperCase());
		if (cds == null)
			throw new EccezioneSbnDiagnostico(3088, "Codice errato");

		short rank = GestoreLegami.getPosizioneLegame(autLegato);

		Tr_the_cla tr_the_cla = new Tr_the_cla();
		tr_the_cla.setCD_THE(cds);
		tr_the_cla.setDID(_t001);
		tr_the_cla.setCD_SISTEMA(sd.getSistema());
		tr_the_cla.setCD_EDIZIONE(edizione);
		tr_the_cla.setCLASSE(sd.getSimbolo());
		tr_the_cla.setPOSIZIONE(rank);

		Tr_the_claResult tavola = new Tr_the_claResult(tr_the_cla);
		if (direzione < 0)
			tavola.executeCustom("rankLegameTermineClasse2");
		else
			tavola.executeCustom("rankLegameTermineClasse1");

		List risultati = tavola.getElencoRisultati();
		if (!ValidazioneDati.isFilled(risultati) )
			return;

		if (tipoOperazione.getType() == SbnTipoOperazione.MODIFICA_TYPE) {
			Tr_the_cla first = (Tr_the_cla) risultati.get(0);
			if (first.getPOSIZIONE() != rank)
				//non ci sono valori sovrapposti
				return;
		}

		if (tipoOperazione.getType() == SbnTipoOperazione.CANCELLA_TYPE)
			//il prossimo legame prenderà il posto di quello cancellato
			--rank;

		for (Object row : risultati) {
			Tr_the_cla legame = (Tr_the_cla) row;
			legame.setPOSIZIONE(direzione < 0 ? --rank : ++rank);
			legame.setUTE_VAR(cdUtente);
			tavola.save(legame);
		}
	}


	public int countLegamiTermineThesauro(Tb_classe tb_classe) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_the_cla tr_the_cla = new Tr_the_cla();
		tr_the_cla.setCD_SISTEMA(tb_classe.getCD_SISTEMA());
		tr_the_cla.setCD_EDIZIONE(tb_classe.getCD_EDIZIONE());
		tr_the_cla.setCLASSE(tb_classe.getCLASSE());

		Tr_the_claResult tavola = new Tr_the_claResult(tr_the_cla);
		tavola.mergeParametro(this.leggiAllParametro());

		tavola.executeCustom("contaClassePerThesauro");
		return tavola.getCount();
	}

}
