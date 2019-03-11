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

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriBatchSoggettoBaseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriCancellazioneSoggettiNonUtilizzatiVO;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.domain.CodiciAttivita;

import javax.transaction.UserTransaction;

public class CancellazioneSoggettiInutilizzatiBatch implements BatchExecutor {

	private Semantica semantica;

	private Semantica getBMTEjb() throws Exception {
		if (semantica != null)
			return semantica;

		semantica = DomainEJBFactory.getInstance().getSemanticaBMT();
		return semantica;
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter blw,
			UserTransaction tx) throws Exception {

		ParametriBatchSoggettoBaseVO pbsb = (ParametriBatchSoggettoBaseVO) params;

		CommandType tipoOperazione = pbsb.getTipoOperazione();
		if (tipoOperazione != null) {
			CommandInvokeVO invoke = new CommandInvokeVO(pbsb.getTicket(), tipoOperazione, pbsb, blw);
			CommandResultVO result = getBMTEjb().invoke(invoke);
			result.throwError();

			return (ElaborazioniDifferiteOutputVo) result.getResult();

		}

		String codAttivita = params.getCodAttivita();
		if (ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().CANCELLAZIONE_SOGGETTI_INUTILIZZATI)) {

			ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta =
				(ParametriCancellazioneSoggettiNonUtilizzatiVO) params;
			ElaborazioniDifferiteOutputVo output = getBMTEjb().cancellaSoggettiNonUtilizzati(richiesta, blw);
			output.setStato(ConstantsJMS.STATO_OK);
			return output;
		}

		if (ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().CANCELLAZIONE_DESCRITTORI_INUTILIZZATI)) {

			ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta =
				(ParametriCancellazioneSoggettiNonUtilizzatiVO) params;
			ElaborazioniDifferiteOutputVo output = getBMTEjb().cancellaDescrittoriNonUtilizzati(richiesta, blw);
			output.setStato(ConstantsJMS.STATO_OK);
			return output;
		}

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);
		output.setStato(ConstantsJMS.STATO_ERROR);
		blw.getLogger().error("codice Attivita '" + codAttivita + "' non gestito.");
		return output;
	}

	public boolean validateInput(
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {
		return true;
	}

}
