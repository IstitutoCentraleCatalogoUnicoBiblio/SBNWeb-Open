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
package it.iccu.sbn.util.batch;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_batch_servizi;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.SchedulableBatchExecutor;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class BatchUtil {

	public static final BatchExecutor createExecutorInstance(Tbf_batch_servizi config) throws Exception {

		String className = config.getClass_name();
		if (ValidazioneDati.strIsNull(className))
			throw new ValidationException(SbnErrorTypes.BATCH_CONFIGURATION_ERROR);

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Class<?> clazz = loader.loadClass(className);
		return (BatchExecutor) clazz.newInstance();
	}

	public static final SchedulableBatchExecutor createSchedulableExecutorInstance(Tbf_batch_servizi config) throws Exception {

		String className = config.getClass_name();
		if (ValidazioneDati.strIsNull(className))
			throw new ValidationException(SbnErrorTypes.BATCH_CONFIGURATION_ERROR);

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Class<?> clazz = loader.loadClass(className);
		return (SchedulableBatchExecutor) clazz.newInstance();
	}

	public static final boolean schedule(SchedulableBatchVO batch) throws Exception {

		TB_CODICI cod = batch.getConfig();
		AmministrazioneGestioneCodici codici = DomainEJBFactory.getInstance().getCodiciBMT();
		boolean ok = codici.salvaTabellaCodici(cod, true);
		return ok;
	}

}
