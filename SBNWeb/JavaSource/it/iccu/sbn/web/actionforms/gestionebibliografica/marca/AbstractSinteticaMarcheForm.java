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

package it.iccu.sbn.web.actionforms.gestionebibliografica.marca;

import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AbstractSinteticaMarcheForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 483072804205227142L;
	private String myPath;
	private String livRicerca;
	private String livRic;
    private int numBlocco;
    private int totBlocchi;
    private int numMarche;


	private String selezRadio;
	private String[] selezCheck;

	private Set<Integer> appoggio = new HashSet();

    private List listaSintetica;

    private String radioMarca;

	private List listaEsaminaMar;
	private String esaminaMarSelez;

	public String getEsaminaMarSelez() {
		return esaminaMarSelez;
	}

	public void setEsaminaMarSelez(String esaminaMarSelez) {
		this.esaminaMarSelez = esaminaMarSelez;
	}

	public List getListaEsaminaMar() {
		return listaEsaminaMar;
	}

	public void setListaEsaminaMar(List listaEsaminaMar) {
		this.listaEsaminaMar = listaEsaminaMar;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public String getLivRic() {
		return livRic;
	}

	public void setLivRic(String livRic) {
		this.livRic = livRic;
	}

	public String getLivRicerca() {
		return livRicerca;
	}

	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
	}

	public String getMyPath() {
		return myPath;
	}

	public void setMyPath(String myPath) {
		this.myPath = myPath;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumMarche() {
		return numMarche;
	}

	public void setNumMarche(int numMarche) {
		this.numMarche = numMarche;
	}

	public String getRadioMarca() {
		return radioMarca;
	}

	public void setRadioMarca(String radioMarca) {
		this.radioMarca = radioMarca;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public Set<Integer> getAppoggio() {
		return appoggio;
	}

	public void setAppoggio(Set<Integer> appoggio) {
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




}
