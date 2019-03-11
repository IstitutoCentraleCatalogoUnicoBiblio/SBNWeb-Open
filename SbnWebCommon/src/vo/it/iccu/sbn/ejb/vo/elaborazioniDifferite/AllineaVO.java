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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaOutputAllineamentoVO;

import java.util.ArrayList;
import java.util.List;

public class AllineaVO extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -3019497100487321388L;

	//tipo operazione su file xml
	public enum FileXMLTipoOperazione {
		SALVA,
		ALLINEA;
	}

	private String codErr;
	private String testoProtocollo;
	private String versioneIndice;
	private int numListaDaAllineareDocumento;
	private int numListaDaAllineareAutore;
	private int numListaDaAllineareMarca;
	private int numListaDaAllineareLuogo;
	private int numListaDaAllineareTitUniforme;
	private int numListaDaAllineareTitUniformeMus;

	private String tipoAuthorityDaAllineare;
	private String tipoMaterialeDaAllineare;
	private String dataAllineaDa;
	private String dataAllineaA;
	private String idFileAllineamenti;

	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
	private String idFileLocaleAllineamenti;
	private boolean allineamentoDaFileLocale;
	private FileXMLTipoOperazione fileXmlTipoOperazione = FileXMLTipoOperazione.ALLINEA;

	public String getIdFileLocaleAllineamenti() {
		return idFileLocaleAllineamenti;
	}

	public void setIdFileLocaleAllineamenti(String idFileLocaleAllineamenti) {
		this.idFileLocaleAllineamenti = trimAndSet(idFileLocaleAllineamenti);
	}


	public boolean isAllineamentoDaFileLocale() {
		return allineamentoDaFileLocale;
	}

	public void setAllineamentoDaFileLocale(boolean allineamentoDaFileLocale) {
		this.allineamentoDaFileLocale = allineamentoDaFileLocale;
	}


	public FileXMLTipoOperazione getFileXmlTipoOperazione() {
		return fileXmlTipoOperazione;
	}

	public void setFileXmlTipoOperazione(FileXMLTipoOperazione fileXmlTipoOperazione) {
		this.fileXmlTipoOperazione = fileXmlTipoOperazione;
	}


	private List<TracciatoStampaOutputAllineamentoVO> logAnalitico = new ArrayList<TracciatoStampaOutputAllineamentoVO>();


	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public List<TracciatoStampaOutputAllineamentoVO> getLogAnalitico() {
		return logAnalitico;
	}

	public void setLogAnalitico(
			List<TracciatoStampaOutputAllineamentoVO> logAnalitico) {
		this.logAnalitico = logAnalitico;
	}

	public int getNumListaDaAllineareAutore() {
		return numListaDaAllineareAutore;
	}

	public void setNumListaDaAllineareAutore(int numListaDaAllineareAutore) {
		this.numListaDaAllineareAutore = numListaDaAllineareAutore;
	}

	public int getNumListaDaAllineareDocumento() {
		return numListaDaAllineareDocumento;
	}

	public void setNumListaDaAllineareDocumento(int numListaDaAllineareDocumento) {
		this.numListaDaAllineareDocumento = numListaDaAllineareDocumento;
	}

	public int getNumListaDaAllineareLuogo() {
		return numListaDaAllineareLuogo;
	}

	public void setNumListaDaAllineareLuogo(int numListaDaAllineareLuogo) {
		this.numListaDaAllineareLuogo = numListaDaAllineareLuogo;
	}

	public int getNumListaDaAllineareMarca() {
		return numListaDaAllineareMarca;
	}

	public void setNumListaDaAllineareMarca(int numListaDaAllineareMarca) {
		this.numListaDaAllineareMarca = numListaDaAllineareMarca;
	}

	public int getNumListaDaAllineareTitUniforme() {
		return numListaDaAllineareTitUniforme;
	}

	public void setNumListaDaAllineareTitUniforme(int numListaDaAllineareTitUniforme) {
		this.numListaDaAllineareTitUniforme = numListaDaAllineareTitUniforme;
	}

	public int getNumListaDaAllineareTitUniformeMus() {
		return numListaDaAllineareTitUniformeMus;
	}

	public void setNumListaDaAllineareTitUniformeMus(
			int numListaDaAllineareTitUniformeMus) {
		this.numListaDaAllineareTitUniformeMus = numListaDaAllineareTitUniformeMus;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}

	public String getTipoAuthorityDaAllineare() {
		return tipoAuthorityDaAllineare;
	}

	public void setTipoAuthorityDaAllineare(String tipoAuthorityDaAllineare) {
		this.tipoAuthorityDaAllineare = tipoAuthorityDaAllineare;
	}

	public String getVersioneIndice() {
		return versioneIndice;
	}

	public void setVersioneIndice(String versioneIndice) {
		this.versioneIndice = versioneIndice;
	}

	public String getTipoMaterialeDaAllineare() {
		return tipoMaterialeDaAllineare;
	}

	public void setTipoMaterialeDaAllineare(String tipoMaterialeDaAllineare) {
		this.tipoMaterialeDaAllineare = tipoMaterialeDaAllineare;
	}

	public String getIdFileAllineamenti() {
		return idFileAllineamenti;
	}

	public void setIdFileAllineamenti(String idFileAllineamenti) {
		this.idFileAllineamenti = trimAndSet(idFileAllineamenti);
	}

	public String getDataAllineaA() {
		return dataAllineaA;
	}

	public void setDataAllineaA(String dataAllineaA) {
		this.dataAllineaA = dataAllineaA;
	}

	public String getDataAllineaDa() {
		return dataAllineaDa;
	}

	public void setDataAllineaDa(String dataAllineaDa) {
		this.dataAllineaDa = dataAllineaDa;
	}

}
