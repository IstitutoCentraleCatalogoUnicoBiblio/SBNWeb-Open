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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AreaDatiImportSuPoloVO extends SerializableVO {

	private static final long serialVersionUID = -5800975318628486957L;

	private String codErr;
	private String testoErrore;
	private String testoProtocollo;
	private List<String> listaSegnalazioni = new ArrayList<String>();
	private String nrRichiesta;

	private List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray;
	private List<String> listaEtichette001;

	private boolean natureW;
	private CreaType creaTypeW;

	private final Serializable blw;

	private int contEstratti = 0;
	private int contInseritiOK = 0;
	private int contOldInsert = 0;

	private int contEstrattiOriginali = 0;
	private int contEstrattiNoDuplicati = 0;

	private int contAmmessiUnimarcNonGestiti = 0;
	private int contNonAmmessiUnimarc = 0;

	private final String idBatch;

	public AreaDatiImportSuPoloVO(String idBatch, Serializable blw) {
		this.idBatch = idBatch;
		// almaviva5_20120614
		this.blw = blw;
	}

	public List<TracciatoDatiImport1ParzialeVO> getListaTracciatoRecordArray() {
		return listaTracciatoRecordArray;
	}

	public void setListaTracciatoRecordArray(
			List<TracciatoDatiImport1ParzialeVO> listaTracciatoRecordArray) {
		this.listaTracciatoRecordArray = listaTracciatoRecordArray;
	}

	public List addListaTracciatoRecordArray(
			TracciatoDatiImport1ParzialeVO newLine) {
		listaTracciatoRecordArray.add(newLine);
		return listaTracciatoRecordArray;
	}

	public List<String> getListaEtichette001() {
		return listaEtichette001;
	}

	public void setListaEtichette001(List<String> listaEtichette001) {
		this.listaEtichette001 = listaEtichette001;
	}

	public List addListaEtichette001(String newBid) {
		if (listaEtichette001 == null) {
			listaEtichette001 = new ArrayList<String>();
		}

		listaEtichette001.add(newBid);
		return listaEtichette001;
	}

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getTestoErrore() {
		return testoErrore;
	}

	public void setTestoErrore(String testoErrore) {
		this.testoErrore = testoErrore;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}

	public String getNrRichiesta() {
		return nrRichiesta;
	}

	public void setNrRichiesta(String nrRichiesta) {
		this.nrRichiesta = nrRichiesta;
	}

	public List<String> getListaSegnalazioni() {
		return listaSegnalazioni;
	}

	public void setListaSegnalazioni(List<String> listaSegnalazioni) {
		this.listaSegnalazioni = listaSegnalazioni;
	}

	public List addListaSegnalazioni(String newLine) {
		listaSegnalazioni.add(newLine);
		return listaSegnalazioni;
	}

	public boolean isNatureW() {
		return natureW;
	}

	public void setNatureW(boolean natureW) {
		this.natureW = natureW;
	}

	public CreaType getCreaTypeW() {
		return creaTypeW;
	}

	public void setCreaTypeW(CreaType creaTypeW) {
		this.creaTypeW = creaTypeW;
	}

	public Serializable getBatchLogWriter() {
		return blw;
	}

	public int getContEstratti() {
		return contEstratti;
	}

	public void setContEstratti(int contEstratti) {
		this.contEstratti = contEstratti;
	}

	public int getContInseritiOK() {
		return contInseritiOK;
	}

	public void setContInseritiOK(int contInseritiOK) {
		this.contInseritiOK = contInseritiOK;
	}

	public int getContOldInsert() {
		return contOldInsert;
	}

	public void setContOldInsert(int contOldInsert) {
		this.contOldInsert = contOldInsert;
	}

	public int getContEstrattiOriginali() {
		return contEstrattiOriginali;
	}

	public void setContEstrattiOriginali(int contEstrattiOriginali) {
		this.contEstrattiOriginali = contEstrattiOriginali;
	}

	public int getContEstrattiNoDuplicati() {
		return contEstrattiNoDuplicati;
	}

	public void setContEstrattiNoDuplicati(int contEstrattiNoDuplicati) {
		this.contEstrattiNoDuplicati = contEstrattiNoDuplicati;
	}

	public int getContAmmessiUnimarcNonGestiti() {
		return contAmmessiUnimarcNonGestiti;
	}

	public void setContAmmessiUnimarcNonGestiti(int contAmmessiUnimarcNonGestiti) {
		this.contAmmessiUnimarcNonGestiti = contAmmessiUnimarcNonGestiti;
	}

	public int getContNonAmmessiUnimarc() {
		return contNonAmmessiUnimarc;
	}

	public void setContNonAmmessiUnimarc(int contNonAmmessiUnimarc) {
		this.contNonAmmessiUnimarc = contNonAmmessiUnimarc;
	}

	public String getIdBatch() {
		return idBatch;
	}

}
