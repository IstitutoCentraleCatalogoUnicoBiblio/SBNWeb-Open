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
package it.iccu.sbn.batch.servizi.sale;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.servizi.batch.ServiziBMT;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.ejb.vo.servizi.batch.RifiutaPrenotazioniScaduteVO;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web2.util.Constants;

import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class ChiusuraPrenotazioniPostoJob extends SerializableVO implements StatefulJob {

	private static final long serialVersionUID = -7768361190480551025L;

	private static Logger log = Logger.getLogger(ChiusuraPrenotazioniPostoJob.class);

	static Reference<ServiziBMT> servizi = new Reference<ServiziBMT>() {
		@Override
		protected ServiziBMT init() throws Exception {
			return DomainEJBFactory.getInstance().getServiziBMT();
		}};

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		try {
			log.debug("inizio chiusura prenotazioni posto...");

			PoloVO polo = DomainEJBFactory.getInstance().getPolo().getInfoPolo();

			RifiutaPrenotazioniScaduteVO params = new RifiutaPrenotazioniScaduteVO();
			params.setTicket(SbnSIP2Ticket.getUtenteTicket(polo.getCd_polo(), Constants.ROOT_BIB, Constants.SBNDIF_USER, InetAddress.getLocalHost()));
			params.setCodPolo(polo.getCd_polo());
			params.setCodBib(Constants.ROOT_BIB);
			params.setUser(Constants.SBNDIF_USER);
			params.setCodAttivita(CodiciAttivita.getIstance().SRV_RIFIUTO_PRENOTAZIONI_SCADUTE);
			params.setIdBatch("" + IdGenerator.getId());

			params.setDataFinePrevista(LocalDate.now().minusDays(1).toDate());
			params.setLivelloPolo(true);
			// flag per invocazione automatica da scheduler
			params.setAutomatico(true);

			servizi.get().rifiutaPrenotazioniScadute(params, null);

			log.debug("fine chiusura prenotazioni posto.");

		} catch (Exception e) {
			log.error("" ,e);
		}
	}

}
