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
package it.iccu.sbn.util.jasper;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.io.EOFException;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

import org.apache.log4j.Logger;



public class JRSerializedObjectDataSource extends JRAbstractBeanDataSource {

	private Logger log;

	private Serializable current;
	private BatchCollectionSerializer bcs;
	private final String type;
	private final long offset;
	private final String idBatch;

	private final int reportLogThreshold;

	public JRSerializedObjectDataSource(SubReportVO sub) throws JRException {
		super(false);
		this.type = sub.getType();
		this.offset = sub.getOffset();
		this.bcs = BatchCollectionSerializer.forReport(sub);
		this.idBatch = sub.getIdBatch();
		this.log = Logger.getLogger(bcs.getFirmaBatch() );
		try {
			reportLogThreshold = CommonConfiguration.getPropertyAsInteger(Configuration.JASPER_REPORT_LOG_THRESHOLD, -1);
			bcs.seek(type, offset);
		} catch (Exception e) {
			throw new JRException(e);
		}
	}

	public void moveFirst() throws JRException {
		try {
			bcs.seek(type, offset);
		} catch (Exception e) {
			throw new JRException(e);
		}
	}

	public Object getFieldValue(JRField field) throws JRException {
		return getFieldValue(current, field);
	}

	public boolean next() throws JRException {
		try {
			current = bcs.readNextVO(type);
			boolean filled = (current != null);

			long read = bcs.getRead();
			if (filled && reportLogThreshold > 0) {
				if ((read % reportLogThreshold) == 0 && log.isDebugEnabled()) {
					log.debug(String.format("idBatch: %s; Rendered objects: %d", idBatch, read) );
					log.debug(SerializableVO.dump(current, true));
				}
			}

			if ((read % 1000) == 0)
				BatchManager.getBatchManagerInstance().checkForInterruption(idBatch);

			return filled;

		} catch (EOFException e) {
			return false;
		} catch (Exception e) {
			throw new JRException(e);
		}

	}

}
