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

import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnHttpIndice;
import it.iccu.sbn.ejb.vo.SerializableVO;

import org.jboss.logging.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class BatchCheckIndiceConnectionJob extends SerializableVO implements StatefulJob {

	private static final long serialVersionUID = -4706663966182993242L;

	private static final Logger log = Logger.getLogger(BatchCheckIndiceConnectionJob.class);

	public void execute(JobExecutionContext ctx) throws JobExecutionException {

		try {
			//almaviva5_20140228 evolutive google3
			if (!FactorySbnHttpIndice.isAvailable()) {
				log.debug("check connessione indice...");
				FactorySbnHttpIndice.check();
			}

		} catch (Exception e) {
			throw new JobExecutionException(e);
		}

	}

}
