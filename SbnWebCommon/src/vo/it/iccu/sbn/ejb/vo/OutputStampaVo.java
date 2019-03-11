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
package it.iccu.sbn.ejb.vo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;

public class OutputStampaVo extends SerializableVO {

	private static final long serialVersionUID = -3223845255535253882L;

	private String stato;
	private InputStream output;
	// utile per visualizzare immagini dei report
	private JasperPrint jasPr;
	// utile per visualizzare immagini dei report
	private JRHtmlExporter htmlExport;
	private List downloadList;
	private Map parametriDiRicercaMap;
	private String dataDiElaborazione;

	public InputStream getOutput() {
		return output;
	}

	public void setOutput(InputStream streamRichiestaStampa) {
		this.output = streamRichiestaStampa;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public JasperPrint getJasPr() {
		return jasPr;
	}

	public void setJasPr(JasperPrint jasPr) {
		this.jasPr = jasPr;
	}

	public List getDownloadList() {
		return downloadList;
	}

	public void setDownloadList(List downloadList) {
		this.downloadList = downloadList;
	}

	public String getDataDiElaborazione() {
		return dataDiElaborazione;
	}

	public void setDataDiElaborazione(String dataDiElaborazione) {
		this.dataDiElaborazione = dataDiElaborazione;
	}

	public Map getParametriDiRicercaMap() {
		return parametriDiRicercaMap;
	}

	public void setParametriDiRicercaMap(Map parametriDiRicercaMap) {
		this.parametriDiRicercaMap = parametriDiRicercaMap;
	}

	public JRHtmlExporter getHtmlExport() {
		return htmlExport;
	}

	public void setHtmlExport(JRHtmlExporter htmlExport) {
		this.htmlExport = htmlExport;
	}
}
