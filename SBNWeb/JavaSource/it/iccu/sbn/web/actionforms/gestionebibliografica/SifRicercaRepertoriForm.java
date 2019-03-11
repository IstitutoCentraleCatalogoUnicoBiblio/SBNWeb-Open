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
//	FORM sintetica titoli
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.gestionebibliografica;

import it.iccu.sbn.ejb.vo.gestionebibliografica.SifRicercaRepertoriVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;


public class SifRicercaRepertoriForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -5071799439911997668L;
	private String testoRicerca;
	private String tipoRicercaSelez;
	private List listaTipoRicerca;
	private String oggRicercaDesc;
	private String oggRicercaSigl;

	private SifRicercaRepertoriVO sifRicRep = new SifRicercaRepertoriVO();
	private String selezRadio;

	private List listaRepertori;

	public List getListaTipoRicerca() {
		return listaTipoRicerca;
	}

	public void setListaTipoRicerca(List listaTipoRicerca) {
		this.listaTipoRicerca = listaTipoRicerca;
	}

	public String getOggRicercaDesc() {
		return oggRicercaDesc;
	}

	public void setOggRicercaDesc(String oggRicercaDesc) {
		this.oggRicercaDesc = oggRicercaDesc;
	}

	public String getOggRicercaSigl() {
		return oggRicercaSigl;
	}

	public void setOggRicercaSigl(String oggRicercaSigl) {
		this.oggRicercaSigl = oggRicercaSigl;
	}

	public SifRicercaRepertoriVO getSifRicRep() {
		return sifRicRep;
	}

	public void setSifRicRep(SifRicercaRepertoriVO sifRicRep) {
		this.sifRicRep = sifRicRep;
	}

	public String getTestoRicerca() {
		return testoRicerca;
	}

	public void setTestoRicerca(String testoRicerca) {
		this.testoRicerca = testoRicerca;
	}

	public String getTipoRicercaSelez() {
		return tipoRicercaSelez;
	}

	public void setTipoRicercaSelez(String tipoRicercaSelez) {
		this.tipoRicercaSelez = tipoRicercaSelez;
	}

	public List getListaRepertori() {
		return listaRepertori;
	}

	public void setListaRepertori(List listaRepertori) {
		this.listaRepertori = listaRepertori;
	}

	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}





}
