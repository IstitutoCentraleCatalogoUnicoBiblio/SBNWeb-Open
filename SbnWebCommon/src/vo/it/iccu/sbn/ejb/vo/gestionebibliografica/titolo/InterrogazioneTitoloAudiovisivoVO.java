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
// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;


public class InterrogazioneTitoloAudiovisivoVO   extends SerializableVO {

	private static final long serialVersionUID = 5818352540922985769L;

	private List listaTipoVideo;
	private String tipoVideoSelez;

	private List listaFormaPubblDistr;
	private String FormaPubblDistrSelez;

	private List listaTecnicaVideoFilm;
	private String TecnicaVideoFilmSelez;

	private List listaFormaPubblicazione;
	private String FormaPubblicazioneSelez;

	private List listaVelocita;
	private String VelocitaSelez;

	public List getListaTipoVideo() {
		return listaTipoVideo;
	}
	public void setListaTipoVideo(List listaTipoVideo) {
		this.listaTipoVideo = listaTipoVideo;
	}
	public String getTipoVideoSelez() {
		return tipoVideoSelez;
	}
	public void setTipoVideoSelez(String tipoVideoSelez) {
		this.tipoVideoSelez = tipoVideoSelez;
	}
	public List getListaFormaPubblDistr() {
		return listaFormaPubblDistr;
	}
	public void setListaFormaPubblDistr(List listaFormaPubblDistr) {
		this.listaFormaPubblDistr = listaFormaPubblDistr;
	}
	public String getFormaPubblDistrSelez() {
		return FormaPubblDistrSelez;
	}
	public void setFormaPubblDistrSelez(String formaPubblDistrSelez) {
		FormaPubblDistrSelez = formaPubblDistrSelez;
	}
	public List getListaTecnicaVideoFilm() {
		return listaTecnicaVideoFilm;
	}
	public void setListaTecnicaVideoFilm(List listaTecnicaVideoFilm) {
		this.listaTecnicaVideoFilm = listaTecnicaVideoFilm;
	}
	public String getTecnicaVideoFilmSelez() {
		return TecnicaVideoFilmSelez;
	}
	public void setTecnicaVideoFilmSelez(String tecnicaVideoFilmSelez) {
		TecnicaVideoFilmSelez = tecnicaVideoFilmSelez;
	}
	public List getListaFormaPubblicazione() {
		return listaFormaPubblicazione;
	}
	public void setListaFormaPubblicazione(List listaFormaPubblicazione) {
		this.listaFormaPubblicazione = listaFormaPubblicazione;
	}
	public String getFormaPubblicazioneSelez() {
		return FormaPubblicazioneSelez;
	}
	public void setFormaPubblicazioneSelez(String formaPubblicazioneSelez) {
		FormaPubblicazioneSelez = formaPubblicazioneSelez;
	}
	public List getListaVelocita() {
		return listaVelocita;
	}
	public void setListaVelocita(List listaVelocita) {
		this.listaVelocita = listaVelocita;
	}
	public String getVelocitaSelez() {
		return VelocitaSelez;
	}
	public void setVelocitaSelez(String velocitaSelez) {
		VelocitaSelez = velocitaSelez;
	}
}


