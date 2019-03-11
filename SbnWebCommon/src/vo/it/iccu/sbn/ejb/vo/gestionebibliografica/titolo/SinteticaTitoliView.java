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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;


import it.iccu.sbn.ejb.vo.common.LinkableViewVO;

import java.util.Comparator;

public class SinteticaTitoliView extends LinkableViewVO {

	private static final long serialVersionUID = -5068369410359802553L;

	private int numNotizie;
	private int progressivo;
	private String imageUrl;
	private String bid;
	private String tipoMateriale;
	private String tipoRecord;
	private String tipoRecordDesc;
	private String natura;
	// BUG MANTIS 3251 - almaviva2 30.11.2009
	private String dataPubbl1Da;
	private String descrizioneLegami;
	private String tipoRecDescrizioneLegami;
	private String livelloAutorita;
	private String parametri;
	private String tipoAutority;
	private String alfaMateriale;
	private String dataIns;
	private boolean flagCondiviso;
	private boolean localizzato;
	private String titoloSif;

	public static final Comparator<SinteticaTitoliView> ORDINA_PER_PROGRESSIVO = new Comparator<SinteticaTitoliView>() {
		public int compare(SinteticaTitoliView o1, SinteticaTitoliView o2) {
			int myProgr1 = o1.getProgressivo();
			int myProgr2 = o2.getProgressivo();
			return myProgr1 - myProgr2;
		}
	};

	public String getTitolo() {

		// Matcher matcher = patternNewLine.matcher(this.descrizioneLegami);
		// if (!matcher.find())
		// return this.descrizioneLegami;
		//
		// return this.descrizioneLegami.substring(0, matcher.start());
		return titoloSif;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getParametri() {
		return parametri;
	}

	public void setParametri(String parametri) {
		this.parametri = parametri;
	}

	public String getDescrizioneLegami() {
		return descrizioneLegami;
	}

	public void setDescrizioneLegami(String value) {
		this.descrizioneLegami = htmlFilter(value);
		String[] tokens = value.split(HTML_NEW_LINE);
		this.titoloSif = tokens[0];
	}

	public String getImageUrl() {
		// almaviva2 Mantis esercizio 5733 (PISA)
		// Sintetica schede di Liste di Confronto ha immagine non presente. inserita immagine sintVuota.png
		if (imageUrl== null)  {
			return "sintVuota.png";
		}
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String materiale) {
		this.tipoMateriale = materiale;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getAlfaMateriale() {
		return alfaMateriale;
	}

	public void setAlfaMateriale(String alfaMateriale) {
		this.alfaMateriale = alfaMateriale;
	}

	public String getTipoAutority() {
		return tipoAutority;
	}

	public void setTipoAutority(String tipoAutority) {
		this.tipoAutority = tipoAutority;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getTipoRecDescrizioneLegami() {
		return tipoRecDescrizioneLegami;
	}

	public void setTipoRecDescrizioneLegami(String tipoRecDescrizioneLegami) {
		String findXid = findXid(tipoRecDescrizioneLegami);
		this.tipoRecDescrizioneLegami = htmlFilter(findXid);
	}

	public void setTipoRecDescrizioneLegamiSenzaKEY(String tipoRecDescrizioneLegami) {
		this.tipoRecDescrizioneLegami = tipoRecDescrizioneLegami;
	}

	public String getTipoRecordDesc() {
		return tipoRecordDesc;
	}

	public void setTipoRecordDesc(String tipoRecordDesc) {
		this.tipoRecordDesc = tipoRecordDesc;
	}

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public boolean isLocalizzato() {
		return localizzato;
	}

	public void setLocalizzato(boolean localizzato) {
		this.localizzato = localizzato;
	}

	public String getDataPubbl1Da() {
		return dataPubbl1Da;
	}

	public void setDataPubbl1Da(String dataPubbl1Da) {
		this.dataPubbl1Da = dataPubbl1Da;
	}
}
