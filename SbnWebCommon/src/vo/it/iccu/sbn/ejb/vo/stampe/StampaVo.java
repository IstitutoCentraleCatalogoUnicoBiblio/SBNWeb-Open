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
package it.iccu.sbn.ejb.vo.stampe;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriTimestampRangeVO;

public class StampaVo extends ParametriTimestampRangeVO {

	private static final long serialVersionUID = -1382391946780086563L;

	/**
	 * Tipo stampa da generare
	 */
	private String tipoStampa;

	/**
	 * Tipo di ordinamento dei dati da stampare
	 * scelto dall'utente
	 */
	private String tipoOrdinamento;

	/**
	 * JRXML
	 * contiene solo il nome del template da utilizzare
	 * bisogna ricavarselo
	 */
	private String template;
	private String template2;

	/**
	 * DOWNLOAD
	 * contiene solo il percorso della cartella da utilizzare
	 * come storage dei file generati con i report
	 */
	private String download;


	/**
	 * Data di richiesta
	 * Generata dal sistema
	 */
	private String data;

	/**
	 * contiene il codice ATTIVITA
	 */

	private String tipoOperazione;
	/**
	 * JRXML
	 * contiene solo il nome del template da utilizzare
	 * bisogna ricavarselo
	 */
	private String templateBarCode;
	private int numCopie;

//	/**
//	*rappresenta un oggetto formattato sotto forma di un outputStream di tipo testo,
//	*da utilizzare per le immagini dei report
//	*/
//	private PrintWriter objectPrint;

	public String getTemplateBarCode() {
		return templateBarCode;
	}

	public int getNumCopie() {
		return numCopie;
	}

	public void setNumCopie(int numCopie) {
		this.numCopie = numCopie;
	}

	public void setTemplateBarCode(String templateBarCode) {
		this.templateBarCode = templateBarCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {//Date
		this.data = data;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		//il template DEVE avere sempre il suffiso .jrxml
		if(!template.endsWith(".jrxml")){
			template=template+".jrxml";
		}
		this.template = template;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getTipoStampa() {
		return tipoStampa;
	}

	public void setTipoStampa(String tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getTemplate2() {
		return template2;
	}

	public void setTemplate2(String template2) {
		this.template2 = template2;
	}

}
