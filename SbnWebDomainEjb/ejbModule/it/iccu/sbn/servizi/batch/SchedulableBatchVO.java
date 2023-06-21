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
package it.iccu.sbn.servizi.batch;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/*
 * cd_tabella	job_name.
 * cd_flg2		codice attività.
 * cd_flg3		espressione di attivazione.
 * cd_flg4		utente che prenota il batch
 * cd_flg5		id batch.
 * cd_flg6		ultimo avvio.
 * cd_flg7		parziale/totale.
 * cd_flg8		ultimo arresto.
 *
 * almaviva5_20150330
 * cd_flg9		tipo schedulazione.
 * cd_flg10		ulteriori parametri.
 *
 * almaviva5_20160308
 * cd_flg11		tipo scarico|lista biblioteche
 */

public class SchedulableBatchVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = -7803792221970476948L;
	protected final TB_CODICI config;

	public enum ScheduleType {
		TIME,
		CRON;
	}

	public SchedulableBatchVO(TB_CODICI config) {
		this.config = config.copy();
	}

	public SchedulableBatchVO() {
		this.config = new TB_CODICI();
		this.config.setTp_tabella(CodiciType.CODICE_BATCH_PIANIFICABILE.getTp_Tabella());
	}

	public String getJobName() {
		return config.getCd_tabellaTrim();
	}

	public void setJobName(String name) {
		config.setCd_tabella(name);
	}

	public String getCd_attivita() {
		return trimOrEmpty(config.getCd_flg2());
	}

	public void setCd_attivita(String attivita) {
		config.setCd_flg2(attivita);
	}

	public String getActivationExpr() {
		return trimOrEmpty(config.getCd_flg3());
	}

	public void setActivationExpr(String expr) {
		config.setCd_flg3(expr);
	}

	public String getUser() {
		return trimOrEmpty(config.getCd_flg4());
	}

	public void setUser(String user) {
		config.setCd_flg4(user);
	}

	public void setIdBatch(String id) {
		config.setCd_flg5(id);
	}

	public String getIdBatch() {
		return trimOrEmpty(config.getCd_flg5());
	}

	public void setLatestStart(Timestamp start) {
		config.setCd_flg6(DateUtil.formattaDataCompletaTimestamp(start));
	}

	public Timestamp getLatestStart() {
		return DateUtil.toTimestampISO8601(config.getCd_flg6());
	}

	public boolean isParziale() {
		return ValidazioneDati.in(trimOrEmpty(config.getCd_flg7()), "P", "p");
	}

	public void setLatestSuccessfulEnd(Timestamp end) {
		config.setCd_flg8(DateUtil.formattaDataCompletaTimestamp(end));
	}

	public Timestamp getLatestSuccessfulEnd() {
		return DateUtil.toTimestampISO8601(config.getCd_flg8());
	}

	public ScheduleType getScheduleType() {
		return ScheduleType.valueOf(ValidazioneDati.coalesce(config.getCd_flg9(), ScheduleType.CRON.name()));
	}

	public void setScheduleType(ScheduleType type) {
		config.setCd_flg9(type.toString());
	}

	public Serializable getParams() {
		return trimOrEmpty(config.getCd_flg10());
	}

	public void setParams(String params) {
		config.setCd_flg10(params);
	}

	public boolean isActive() {
		Date fineValidita = config.getDt_fine_validita();
		return (fineValidita == null || fineValidita.after(creationTime));
	}

	public TB_CODICI getConfig() {
		return config;
	}

}
