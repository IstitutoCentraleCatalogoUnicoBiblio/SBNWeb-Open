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
package it.iccu.sbn.ejb.vo.gestionesemantica.stampe;

import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;

public class ParametriStampaSoggettarioVO extends ParametriStampaVO {

	private static final long serialVersionUID = -5007460376143007028L;

	private String codSoggettario;
	private String edizioneSoggettario;
	private boolean stampaDescrittoriManuali;
	private boolean stampaSoloStringaSoggetto;

	private String testoFrom;
	private String testoTo;
	private boolean condiviso;

	private SubReportVO subReportSoggetti;

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = codSoggettario;
	}

	public boolean isStampaDescrittoriManuali() {
		return stampaDescrittoriManuali;
	}

	public void setStampaDescrittoriManuali(boolean stampaDescrittoriManuali) {
		this.stampaDescrittoriManuali = stampaDescrittoriManuali;
	}

	public boolean isStampaSoloStringaSoggetto() {
		return stampaSoloStringaSoggetto;
	}

	public void setStampaSoloStringaSoggetto(boolean stampaSoloStringaSoggetto) {
		this.stampaSoloStringaSoggetto = stampaSoloStringaSoggetto;
	}

	public void setSubReportSoggetti(SubReportVO subReportSoggetti) {
		this.subReportSoggetti = subReportSoggetti;
	}

	public SubReportVO getSubReportSoggetti() {
		return subReportSoggetti;
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

	public String getTestoFrom() {
		return testoFrom;
	}

	public void setTestoFrom(String testoFrom) {
		this.testoFrom = trimAndSet(testoFrom);
	}

	public String getTestoTo() {
		return testoTo;
	}

	public void setTestoTo(String testoTo) {
		this.testoTo = trimAndSet(testoTo);
	}

	public boolean isCondiviso() {
		return condiviso;
	}

	public void setCondiviso(boolean condiviso) {
		this.condiviso = condiviso;
	}

}
