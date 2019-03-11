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
package it.iccu.sbn.servizi.batch.job;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_batch_servizi;
import it.iccu.sbn.servizi.batch.SchedulableBatchExecutor;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.util.batch.BatchUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.logging.SbnBatchDumpStyle;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BatchScheduledControllerJob extends SerializableVO implements Job {

	private static final long serialVersionUID = -6060676693481577290L;

	private static final Logger log = Logger.getLogger(BatchScheduledControllerJob.class);

	private AmministrazionePolo getAmministrazionePolo() throws Exception {

		return DomainEJBFactory.getInstance().getPolo();
	}

	private AmministrazioneGestioneCodici getCodici() throws Exception {
		return DomainEJBFactory.getInstance().getCodiciBMT();
	}


	public void execute(JobExecutionContext ctx) throws JobExecutionException {

		try {
			String jobName = ctx.getJobDetail().getName();
			TB_CODICI cod = getCodici().caricaCodice(jobName, CodiciType.CODICE_BATCH_PIANIFICABILE);
			if (cod == null)
				throw new JobExecutionException();

			SchedulableBatchVO config = new SchedulableBatchVO(cod);
			if (!config.isActive())
				return;

			String cd_attivita = config.getCd_attivita();
			log.debug("prenotazione automatica batch '" + jobName + "' per attività: " + cd_attivita);
			Tbf_batch_servizi bs = getAmministrazionePolo().selectBatchByCodAttivita(cd_attivita);
			SchedulableBatchExecutor executor = BatchUtil.createSchedulableExecutorInstance(bs);

			log.debug("Utente default batch configurato: " + config.getUser() );
			ParametriRichiestaElaborazioneDifferitaVO params = executor.buildActivationParameters(config, config.getParams());

			String idBatch = config.getIdBatch();
			if (isFilled(idBatch)) {
				//controllo bach precedente
				ElaborazioniDifferiteOutputVo prev = getAmministrazionePolo().cercaRichiestaElaborazioneDifferita(params.getTicket(), idBatch);
				if (prev != null) {
					//check stato batch OK/ERROR
					if (!ValidazioneDati.in(prev.getStato(),
							ConstantsJMS.STATO_OK,
							ConstantsJMS.STATO_ERROR)) {
						log.warn("il batch " + idBatch + " non è ancora concluso. Impossibile inserire una nuova prenotazione.");
						return;
					}
				}
			}

			log.debug("parametri attivazione: " +  ReflectionToStringBuilder.toString(params, new SbnBatchDumpStyle()) );

			idBatch = getAmministrazionePolo().prenotaElaborazioneDifferita(params, null);

			config.setIdBatch(idBatch);
			getCodici().salvaTabellaCodici(config.getConfig(), false);

		} catch (Exception e) {
			throw new JobExecutionException(e);
		}

	}

}
