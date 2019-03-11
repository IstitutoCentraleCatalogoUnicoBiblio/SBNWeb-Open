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

package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractSinteticaTitoliForm extends
		AbstractBibliotecaForm implements Serializable {


	private static final long serialVersionUID = -6500512014406200226L;

	private String myPath;

	private int numPrimo;

	// attributi speciali
	private boolean serializeListaSintetica = true;
	private boolean clone = false;
	private transient int numBlocco;
	private transient int totBlocchi;
	private transient int numNotizie;
	private transient List listaSintetica;
	private transient String[] selezCheck = null;
	private transient HashSet appoggio = new HashSet();
	private transient String selezRadio;
	//


	private List listaEsaminaTit;
	private List listaGestisciTit;

	private String esaminaTitSelez;
	private String gestisciTitSelez;

	public String getEsaminaTitSelez() {
		return esaminaTitSelez;
	}

	public List getListaGestisciTit() {
		return listaGestisciTit;
	}

	public void setListaGestisciTit(List listaGestisciTit) {
		this.listaGestisciTit = listaGestisciTit;
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

	@Override
	public int getNumPrimo() {
		return numPrimo;
	}

	@Override
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

	public HashSet getAppoggio() {
		return appoggio;
	}

	public void setAppoggio(HashSet appoggio) {
		this.appoggio = appoggio;
	}


	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		if (!this.serializeListaSintetica) return;

		s.writeInt(this.numBlocco);
		s.writeInt(this.totBlocchi);
		s.writeInt(this.numNotizie);
		s.writeObject(this.listaSintetica);
		s.writeObject(this.selezCheck);
		s.writeObject(this.appoggio);
		s.writeObject(this.selezRadio);
	}

	private void readObject(ObjectInputStream s) throws IOException,
			ClassNotFoundException {
		s.defaultReadObject();
		if (!this.serializeListaSintetica) return;

		this.numBlocco      = s.readInt();
		this.totBlocchi     = s.readInt();
		this.numNotizie     = s.readInt();
		this.listaSintetica = (List) s.readObject();
		this.selezCheck     = (String[]) s.readObject();
		this.appoggio       = (HashSet) s.readObject();
		this.selezRadio     = (String) s.readObject();
	}

	public AbstractSinteticaTitoliForm cloneForm(boolean serializeListaSintetica) throws Exception {

		try {
			this.serializeListaSintetica = serializeListaSintetica;
			AbstractSinteticaTitoliForm clone =
				ClonePool.deepCopy(this);
			clone.serializeListaSintetica = true;
			clone.clone = true;
			clone.selezCheck = null;
			clone.appoggio = new HashSet();
			return clone;
		} finally {
			this.serializeListaSintetica = true;
		}
	}

	public boolean isClone() {
		return clone;
	}

	public String getGestisciTitSelez() {
		return gestisciTitSelez;
	}

	public void setGestisciTitSelez(String gestisciTitSelez) {
		this.gestisciTitSelez = gestisciTitSelez;
	}


}
