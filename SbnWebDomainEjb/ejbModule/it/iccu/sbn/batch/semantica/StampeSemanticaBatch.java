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
package it.iccu.sbn.batch.semantica;

import it.iccu.sbn.ejb.DomainEJBStampeFactory;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSemantica;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeTerminiThesauro;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.vo.domain.CodiciAttivita;

import javax.transaction.UserTransaction;


public class StampeSemanticaBatch implements BatchExecutor {

	private SBNStampeSemantica stampe;

	private SBNStampeSemantica getEjb() throws Exception {
		if (stampe != null)
			return stampe;

		stampe = DomainEJBStampeFactory.getInstance().getSBNStampeSemantica();
		return stampe;
	}

	private SBNStampeTerminiThesauro getThesauro() throws Exception {
		return DomainEJBStampeFactory.getInstance().getSBNStampeTerminiThesauro();
	}


	public boolean validateInput(
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {

		return true;
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log, UserTransaction tx)
			throws Exception {

		String attivita = params.getCodAttivita();
		if (ValidazioneDati.equals(attivita, CodiciAttivita.getIstance().STAMPA_THESAURO_POLO))
			return (ElaborazioniDifferiteOutputVo) getThesauro().elabora((StampaVo) params, log);

		ElaborazioniDifferiteOutputVo output = (ElaborazioniDifferiteOutputVo)getEjb().elabora((ParametriStampaVO)params, log);
		return output;
	}

}
