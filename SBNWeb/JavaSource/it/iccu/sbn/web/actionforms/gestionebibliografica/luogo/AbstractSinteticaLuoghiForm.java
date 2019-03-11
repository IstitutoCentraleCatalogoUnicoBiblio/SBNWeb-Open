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

package it.iccu.sbn.web.actionforms.gestionebibliografica.luogo;

import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.HashSet;
import java.util.List;

public class AbstractSinteticaLuoghiForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 2758266429449578441L;
	private String myPath;
	private String livRicerca;
	private String livRic;
    private int numBlocco;
    private int totBlocchi;
    private int numLuoghi;


	private String selezRadio;
	private String[] selezCheck;

	HashSet appoggio = new HashSet();

    private List listaSintetica;

	private List listaEsaminaLuo;
	private String esaminaLuoSelez;

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
	public int getNumLuoghi() {
		return numLuoghi;
	}
	public void setNumLuoghi(int numLuoghi) {
		this.numLuoghi = numLuoghi;
	}
	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
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


	public String getEsaminaLuoSelez() {
		return esaminaLuoSelez;
	}
	public void setEsaminaLuoSelez(String esaminaLuoSelez) {
		this.esaminaLuoSelez = esaminaLuoSelez;
	}
	public List getListaEsaminaLuo() {
		return listaEsaminaLuo;
	}
	public void setListaEsaminaLuo(List listaEsaminaLuo) {
		this.listaEsaminaLuo = listaEsaminaLuo;
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

}
