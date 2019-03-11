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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class ElaborazioniDifferiteOutputVo extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = 2226502564718114301L;

	private String stato;
	private List<DownloadVO> downloadList = new ArrayList<DownloadVO>();
	private Map<?, ?> parametriDiRicercaMap;
	private String dataDiElaborazione; // Inizio
	private String dataDiFineElaborazione; // Fine
	private Timestamp startTime;
	private Timestamp endTime;

	protected ElaborazioniDifferiteOutputVo() {
		super();
	}

	public ElaborazioniDifferiteOutputVo(ParametriRichiestaElaborazioneDifferitaVO parametri) {
		super();
		copyCommonProperties(this, parametri);
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public List<DownloadVO> getDownloadList() {
		return downloadList;
	}

	public void addDownload(String nomeFileVisualizzato, String linkToDownload) {
		DownloadVO dwn = new DownloadVO(nomeFileVisualizzato, linkToDownload);
		if (!this.downloadList.contains(dwn))
			this.downloadList.add(dwn);
	}

	public String getDataDiElaborazione() {
		return dataDiElaborazione;
	}

	public void setDataDiElaborazione(String dataDiElaborazione) {
		this.dataDiElaborazione = dataDiElaborazione;
	}

	public Map<?, ?> getParametriDiRicercaMap() {
		return parametriDiRicercaMap;
	}

	public void setParametriDiRicercaMap(Map<?, ?> parametriDiRicercaMap) {
		this.parametriDiRicercaMap = parametriDiRicercaMap;
	}

	public String getDataDiFineElaborazione() {
		return dataDiFineElaborazione;
	}

	public void setDataDiFineElaborazione(String dataDiFineElaborazione) {
		this.dataDiFineElaborazione = dataDiFineElaborazione;
	}

	public void setDownloadList(List<DownloadVO> downloadList) {
		this.downloadList = downloadList;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();

		//almaviva5_20111115 check file duplicati
		if (!isFilled(downloadList))
			return;

		//set ordinato
		LinkedHashSet<DownloadVO> tmp = new LinkedHashSet<DownloadVO>(downloadList);
		downloadList = new ArrayList<DownloadVO>(tmp);
	}

	public static void main (String[] args) throws ValidationException {
		ElaborazioniDifferiteOutputVo edo = new ElaborazioniDifferiteOutputVo();
		edo.setCodPolo("XXX");
		edo.setCodBib("YYY");
		edo.setUser("XYZ");
		edo.setCodAttivita("X0001");

		edo.addDownload("pippo.txt", "xxxxxxxxxxxxxxxx");
		edo.addDownload("pluto.txt", "xxxxxxxxxxxxxxxx");
		edo.addDownload("paperino.txt", "xxxxxxxxxxxxxxxx");
		edo.addDownload("pippo.txt", "xxxxxxxxxxxxxxxx");

		edo.validate();

		System.out.println(edo);
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

}
