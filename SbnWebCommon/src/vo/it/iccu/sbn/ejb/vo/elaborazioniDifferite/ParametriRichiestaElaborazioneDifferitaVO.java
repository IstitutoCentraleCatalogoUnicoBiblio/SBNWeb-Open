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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;

import java.io.Serializable;

public abstract class ParametriRichiestaElaborazioneDifferitaVO extends SerializableVO {

	private static final long serialVersionUID = 4222760287600799804L;

	protected String codPolo;
	protected String codBib;

	private String user;
	private String cognomeNome;

	private String basePath;
	private String downloadPath;
	private String downloadLinkPath;

	private String idBatch = "0";
	private String codAttivita;

	private String codaJms;
	private String nomeCodaJMSOutput;
	private String visibilita;

	private int idCoda;

	private String ticket;

	private String nomeSubReport;

	private String nomeSubReportMP;

	private Serializable payload;


	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (isNull(codPolo))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codPolo");
		if (isNull(codBib))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codBib");
		if (isNull(user) || user.length() > 6)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "user");
		if (isNull(codAttivita))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codAttivita");
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getDownloadLinkPath() {
		return downloadLinkPath;
	}

	public void setDownloadLinkPath(String downloadLinkPath) {
		this.downloadLinkPath = downloadLinkPath;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getFirmaBatch() throws ValidationException {
		validate();
		String firma = String.format("%s_%s_%s_%08d",
				trimAndSet(codAttivita),
				trimAndSet(codPolo),
				trimAndSet(codBib),
				Integer.parseInt(idBatch)).replaceAll("\\s+", "_");
		return firma;
	}

	public String getIdBatch() {
		return idBatch;
	}

	public void setIdBatch(String idBatch) {
		this.idBatch = idBatch;
	}

	public String getCodAttivita() {
		return codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}

	public void setNomeCodaJMS(String codaJms) {
		this.codaJms = trimAndSet(codaJms);
	}

	public String getNomeCodaJMS() {
		return codaJms;
	}

	@Override
	public String toString() {
		return "[ID_CODA=" + idCoda +", ID_BATCH=" + idBatch + ", COD_ATTIVITA=" + codAttivita + "]";
	}

	public String getVisibilita() {
		return visibilita;
	}

	public void setVisibilita(String visibilita) {
		this.visibilita = visibilita;
	}

	public int getIdCoda() {
		return idCoda;
	}

	public void setIdCoda(int idCoda) {
		this.idCoda = idCoda;
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

	public String getNomeCodaJMSOutput() {
		return nomeCodaJMSOutput;
	}

	public void setNomeCodaJMSOutput(String nomeCodaJMSOutput) {
		this.nomeCodaJMSOutput = nomeCodaJMSOutput;
	}

	public final String getTicket() {
		return ticket;
	}

	public final void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getNomeSubReport() {
		return nomeSubReport;
	}

	public void setNomeSubReport(String nomeSubReport) {
		this.nomeSubReport = nomeSubReport;
	}

	public String getNomeSubReportMP() {
		return nomeSubReportMP;
	}

	public void setNomeSubReportMP(String nomeSubReportMP) {
		this.nomeSubReportMP = nomeSubReportMP;
	}

	public Serializable getPayload() {
		return payload;
	}

	public void setPayload(Serializable payload) {
		this.payload = payload;
	}

	public static final void fill(ParametriRichiestaElaborazioneDifferitaVO params, String attivita, UserVO user) {
		params.codPolo = user.getCodPolo();
		params.codBib = user.getCodBib();
		params.ticket = user.getTicket();
		params.user = user.getUserId();
		params.codAttivita = attivita;
	}

}
