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
//	FORM sintetica autori
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.gestionebibliografica.autore;

import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.HashSet;
import java.util.List;

public class AbstractSinteticaAutoriForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -1781888499545756598L;
	private String myPath;
	private String livRicerca;
	private String livRic;
    private int numBlocco;
    private int totBlocchi;
    private int numAutori;



	private String selezRadio;
	private String[] selezCheck;

	HashSet appoggio = new HashSet();

    private List listaSintetica;

    private String radioAutore;

	private List listaEsaminaAut;
	private List listaGestisciAut;

	private String esaminaAutSelez;
	private String gestisciAutSelez;

	public List getListaSintetica() {
		return listaSintetica;
	}
	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}
	public String getLivRicerca() {
		return livRicerca;
	}
	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
	}
	public int getNumBlocco() {
		return numBlocco;
	}
	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumAutori() {
		return numAutori;
	}
	public void setNumAutori(int numAutori) {
		this.numAutori = numAutori;
	}
	public String getRadioAutore() {
		return radioAutore;
	}

	public void setRadioAutore(String radioAutore) {
		this.radioAutore = radioAutore;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}
	public String getLivRic() {
		return livRic;
	}
	public void setLivRic(String livRic) {
		this.livRic = livRic;
	}

	public String getMyPath() {
		return myPath;
	}

	public void setMyPath(String myPath) {
		this.myPath = myPath;
	}

	public String getEsaminaAutSelez() {
		return esaminaAutSelez;
	}

	public void setEsaminaAutSelez(String esaminaAutSelez) {
		this.esaminaAutSelez = esaminaAutSelez;
	}

	public List getListaEsaminaAut() {
		return listaEsaminaAut;
	}

	public void setListaEsaminaAut(List listaEsaminaAut) {
		this.listaEsaminaAut = listaEsaminaAut;
	}
	public HashSet getAppoggio() {
		return appoggio;
	}
	public void setAppoggio(HashSet appoggio) {
		this.appoggio = appoggio;
	}
	public String[] getSelezCheck() {
		return selezCheck;
	}
	public void setSelezCheck(String[] selezCheck) {
		this.selezCheck = selezCheck;
	}
	public String getSelezRadio() {
		return selezRadio;
	}
	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}
	public String getGestisciAutSelez() {
		return gestisciAutSelez;
	}
	public void setGestisciAutSelez(String gestisciAutSelez) {
		this.gestisciAutSelez = gestisciAutSelez;
	}
	public List getListaGestisciAut() {
		return listaGestisciAut;
	}
	public void setListaGestisciAut(List listaGestisciAut) {
		this.listaGestisciAut = listaGestisciAut;
	}

}
