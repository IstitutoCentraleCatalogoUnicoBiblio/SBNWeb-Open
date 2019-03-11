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

import it.iccu.sbn.ejb.vo.stampe.StampaVo;

import java.util.List;

public abstract class ParametriStampaVO extends StampaVo {

	private static final long serialVersionUID = -9099890978262638888L;

	private String descrizioneBiblioteca;
	private boolean online;
	private boolean stampaTitoli;
	private boolean stampaNoteLegameTitoli;
	private boolean stampaNote;
	private String modello;
	private String tipoStampa;
	private List<ElementoStampaSemanticaVO> output;

	private boolean stampaSoloUtilizzati;

	public boolean isStampaTitoli() {
		return stampaTitoli;
	}

	public void setStampaTitoli(boolean stampaTitoli) {
		this.stampaTitoli = stampaTitoli;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public List<ElementoStampaSemanticaVO> getOutput() {
		return output;
	}

	public void setOutput(List<ElementoStampaSemanticaVO> output) {
		this.output = output;
	}

	public boolean isStampaNote() {
		return stampaNote;
	}

	public void setStampaNote(boolean stampaNote) {
		this.stampaNote = stampaNote;
	}

	public String getDescrizioneBiblioteca() {
		return descrizioneBiblioteca;
	}

	public void setDescrizioneBiblioteca(String descrizioneBiblioteca) {
		this.descrizioneBiblioteca = descrizioneBiblioteca;
	}

	public boolean isStampaNoteLegameTitoli() {
		return stampaNoteLegameTitoli;
	}

	public void setStampaNoteLegameTitoli(boolean stampaNoteLegameTitoli) {
		this.stampaNoteLegameTitoli = stampaNoteLegameTitoli;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getTipoStampa() {
		return tipoStampa;
	}

	public void setTipoStampa(String tipoStampa) {
		this.tipoStampa = trimAndSet(tipoStampa);
	}

	public boolean isStampaSoloUtilizzati() {
		return stampaSoloUtilizzati;
	}

	public void setStampaSoloUtilizzati(boolean stampaSoloUtilizzati) {
		this.stampaSoloUtilizzati = stampaSoloUtilizzati;
	}

}
