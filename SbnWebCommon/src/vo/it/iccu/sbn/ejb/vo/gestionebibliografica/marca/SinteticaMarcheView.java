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
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.gestionebibliografica.marca;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SinteticaMarcheView extends SerializableVO {

	// = SinteticaMarcheView.class.hashCode();


// Dati relativi alla ricerca marca - Canali principali

	/**
	 *
	 */
	private static final long serialVersionUID = -4958671357916422778L;

	private int numNotizie;

	private int progressivo;

	private String imageUrl;

	private String descrizioneLegami;

	private String livelloAutorita;

	private String parametri;

	private String tipoAutority;

	private String dataIns;


	private String mid;
	private String nome;
	private String motto;
	private String citazione;
	private boolean flagCondiviso;

	private String selezRadio;
	private String keyMidNomeFinto;

	private List listaImmagini = new ArrayList();

    /**
     * Comparator that can be used for a case insensitive sort of
     * <code>LabelValueBean</code> objects.
     */
    public static final Comparator sortListaSinteticaMar = new Comparator() {
    	public int compare(Object o1, Object o2) {
    		int myProgr1 = ((SinteticaMarcheView) o1).getProgressivo();
    		int myProgr2 = ((SinteticaMarcheView) o2).getProgressivo();
    		return myProgr1-myProgr2;
    	}
    };


	public String getCitazione() {
		return citazione;
	}
	public void setCitazione(String citazione) {
		this.citazione = citazione;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSelezRadio() {
		return selezRadio;
	}
	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}
	public String getKeyMidNome() {
		return this.mid + this.nome;
	}
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public String getDescrizioneLegami() {
		return descrizioneLegami;
	}
	public void setDescrizioneLegami(String descrizioneLegami) {
		this.descrizioneLegami = descrizioneLegami;
	}
	public String getImageUrl() {
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
	public int getNumNotizie() {
		return numNotizie;
	}
	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}
	public String getParametri() {
		return parametri;
	}
	public void setParametri(String parametri) {
		this.parametri = parametri;
	}
	public int getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}
	public String getTipoAutority() {
		return tipoAutority;
	}
	public void setTipoAutority(String tipoAutority) {
		this.tipoAutority = tipoAutority;
	}
	public String getKeyMidNomeFinto() {
		return keyMidNomeFinto;
	}
	public void setKeyMidNomeFinto(String keyMidNomeFinto) {
		this.keyMidNomeFinto = keyMidNomeFinto;
	}

	public List getListaImmagini() {
		return listaImmagini;
	}

	public void setListaImmagini(List listaImmagini) {
		this.listaImmagini = listaImmagini;
	}
	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}
	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

}
