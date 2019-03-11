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
package it.iccu.sbn.web.actionforms.servizi.serviziweb;

import it.iccu.sbn.ejb.vo.servizi.serviziweb.ListaServiziVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class RiproduzioneForm extends ActionForm {

	private static final long serialVersionUID = 6426926836283383294L;

	private String dataRichiesta;

	private ListaServiziVO serv = new ListaServiziVO();
	private String spesaMassima;
	private String volumeInterresse;
	private String numFascicolo;
	private String annoPeriodico;

	private String noteUtente;
	private String titolo;
	private String segnatura;
	private String pagineDaRiprod;
	private List modErogazione;

	public ListaServiziVO getServ() {
		return serv;
	}

	public void setServ(ListaServiziVO serv) {
		this.serv = serv;
	}

	public List getModErogazione() {
		return modErogazione;
	}

	public void setModErogazione(List modErogazione) {
		this.modErogazione = modErogazione;
	}

	public String getPagineDaRiprod() {
		return pagineDaRiprod;
	}

	public void setPagineDaRiprod(String pagineDaRiprod) {
		this.pagineDaRiprod = pagineDaRiprod;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getAnnoPeriodico() {
		return annoPeriodico;
	}

	public void setAnnoPeriodico(String annoPeriodico) {
		this.annoPeriodico = annoPeriodico;
	}

	public String getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(String dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public String getNoteUtente() {
		return noteUtente;
	}

	public void setNoteUtente(String noteUtente) {
		this.noteUtente = noteUtente;
	}

	public String getNumFascicolo() {
		return numFascicolo;
	}

	public void setNumFascicolo(String numFascicolo) {
		this.numFascicolo = numFascicolo;
	}

	public String getSpesaMassima() {
		return spesaMassima;
	}

	public void setSpesaMassima(String spesaMassima) {
		this.spesaMassima = spesaMassima;
	}

	public String getVolumeInterresse() {
		return volumeInterresse;
	}

	public void setVolumeInterresse(String volumeInterresse) {
		this.volumeInterresse = volumeInterresse;
	}

	/*
	 * public ActionErrors validate(ActionMapping mapping, HttpServletRequest
	 * request) { ActionErrors errors = new ActionErrors();
	 *
	 * if(this.titolo != null) { if (getTitolo().length() <= 0) {
	 * errors.add("Titolo", new ActionMessage( "campo.obbligatorio", "Titolo"));
	 * }
	 *
	 * } return errors; }
	 */
}
