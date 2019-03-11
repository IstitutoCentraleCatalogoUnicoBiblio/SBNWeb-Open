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
package it.iccu.sbn.vo.custom.amministrazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class BatchAttivazioneVO extends SerializableVO implements Comparable<BatchAttivazioneVO> {

	private static final long serialVersionUID = -7021838610852184586L;
	private String cod_attivita = "";
	private String descrizione = "";
	private String jobName = "";
	private String groupName = "";
	private String triggerName = "";
	private String cronExpression = "";
	private String coda_input;

	private Object message;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = trimAndSet(groupName);
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = trimAndSet(jobName);
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = trimAndSet(triggerName);
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = trimAndSet(cronExpression);
	}

	public String getCoda_input() {
		return coda_input;
	}

	public void setCoda_input(String coda_input) {
		this.coda_input = trimAndSet(coda_input);
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public String getCod_attivita() {
		return cod_attivita;
	}

	public void setCod_attivita(String cod_attivita) {
		this.cod_attivita = trimAndSet(cod_attivita);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public int compareTo(BatchAttivazioneVO o) {
		return ValidazioneDati.compare(this.descrizione, o.descrizione);
	}

}
