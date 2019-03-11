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
package it.iccu.sbn.batch.servizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchImportaUtentiVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;

import javax.transaction.UserTransaction;

/**
 * Batch di import utenti Esse3 Se l'utente non è Esse3, chiama la classe
 * superiore.<br>
 * Per il prossimo Aggiornamento di SBNWeb: Eseguire UPDATE tbf_batch
 * update tbf_batch_servizi set class_name='it.iccu.sbn.batch.servizi.BatchImportaUtentiEsse3' where cd_attivita = 'LRI01';
 * ALTER TABLE tbl_utenti ALTER COLUMN cod_matricola TYPE bpchar(25) USING cod_matricola::bpchar;
 * ALTER TABLE sbnweb.tbl_utenti ALTER COLUMN nome TYPE varchar(50) USING nome::varchar;
 * ALTER TABLE sbnweb.tbl_utenti ALTER COLUMN indirizzo_dom TYPE bpchar(80) USING indirizzo_dom::bpchar;
 * ALTER TABLE sbnweb.tbl_utenti ALTER COLUMN indirizzo_res TYPE bpchar(80) USING indirizzo_res::bpchar;
 * @author Luca Ferraro Visardi
 * @version 1.0
 * @since 18/07/2018
 */
public class BatchImportaUtentiEsse3 extends BatchImportaUtenti {
	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {

		return params != null;
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log, UserTransaction tx) throws Exception {
		log.logWriteLine("-----------INIZIO IMPORT UTENTI---------");
		ParametriBatchImportaUtentiVO pbiuVO = (ParametriBatchImportaUtentiVO) params;
		if (!pbiuVO.isEsse3())
			return super.execute(prefissoOutput, params, log, tx);

		return DomainEJBFactory.getInstance().getServiziBMT().importaUtentiESSE3(pbiuVO, log);
	}

}
