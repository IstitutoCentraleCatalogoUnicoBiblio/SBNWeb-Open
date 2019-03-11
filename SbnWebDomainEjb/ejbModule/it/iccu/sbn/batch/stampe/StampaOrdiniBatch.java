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
package it.iccu.sbn.batch.stampe;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBStampeFactory;
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBuoniOrdine;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaOrdiniDiffVO;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.vo.domain.CodiciAttivita;

import javax.transaction.UserTransaction;

public class StampaOrdiniBatch implements BatchExecutor {

	private Acquisizioni getAcquisizioni() throws Exception {
		return DomainEJBFactory.getInstance().getAcquisizioni();
	}

	private SBNStampeBuoniOrdine getEjb() throws Exception {
		return DomainEJBStampeFactory.getInstance().getSBNStampeBuoniOrdine();
	}


	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter blw, UserTransaction tx)
			throws Exception {
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params) ;

		//almaviva5_20121127 evolutive google
		String codAttivita = params.getCodAttivita();
		if (ValidazioneDati.equals(codAttivita, CodiciAttivita.getIstance().GA_STAMPA_SHIPPING_MANIFEST) )
			return stampaShippingManifest(params, blw);
		//

		blw.logWriteLine("Sto eseguendo la stampa degli ordini");
		output = (ElaborazioniDifferiteOutputVo) getEjb().elabora((StampaOrdiniDiffVO) params, blw);
		return output;
	}

	private ElaborazioniDifferiteOutputVo stampaShippingManifest(
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter blw) throws Exception {

		blw.logWriteLine("Sto eseguendo la stampa Shipping Manifest");
		CommandInvokeVO cmd = CommandInvokeVO.build(params.getTicket(), CommandType.ACQ_STAMPA_SHIPPING_MANIFEST, params, blw);
		CommandResultVO result = getAcquisizioni().invoke(cmd);
		result.throwError();

		return (ElaborazioniDifferiteOutputVo) result.getResult();
	}


	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {

		return true;
	}

}
