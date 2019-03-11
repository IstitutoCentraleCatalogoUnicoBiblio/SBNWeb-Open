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

package it.iccu.sbn.web.actionforms.gestionesemantica.thesauro;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.HashSet;
import java.util.List;

public abstract class AbstractSinteticaThesauriForm extends
		SemanticaBaseForm {


	private static final long serialVersionUID = -1690466573410057259L;

	private String myPath;

	private int totBlocchi;

	private int numNotizie;

	private int numPrimo;

	private int numBlocco;

	private List listaSintetica;

	private String selezRadio;

	private String[] selezCheck;

	private HashSet<Integer> appoggio = new HashSet<Integer>();

	private List listaEsaminaTit;

	private String esaminaTitSelez;

	public String getEsaminaTitSelez() {
		return esaminaTitSelez;
	}

	public void setEsaminaTitSelez(String esaminaTitSelez) {
		this.esaminaTitSelez = esaminaTitSelez;
	}

	public List getListaEsaminaTit() {
		return listaEsaminaTit;
	}

	public void setListaEsaminaTit(List listaEsaminaTit) {
		this.listaEsaminaTit = listaEsaminaTit;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public String getMyPath() {
		return myPath;
	}

	public void setMyPath(String myPath) {
		this.myPath = myPath;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

//	public SinteticaTitoliView getSintTitView() {
//		return sintTitView;
//	}
//
//	public void setSintTitView(SinteticaTitoliView sintTitView) {
//		this.sintTitView = sintTitView;
//	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
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

	public HashSet<Integer> getAppoggio() {
		return appoggio;
	}

	public void setAppoggio(HashSet<Integer> appoggio) {
		this.appoggio = appoggio;
	}

}
