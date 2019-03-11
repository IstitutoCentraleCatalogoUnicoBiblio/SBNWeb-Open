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

package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;


public class DettaglioDescrittoreVO extends DettaglioSogDesBaseVO {
	// Dati relativi alla ricerca marca - Canali principali

	private static final long serialVersionUID = -2889931647614898217L;
	private String did;
	private String TipoLegame;
	private String formaNome = "A";
	private int posizione;

	private int soggettiCollegati;
	private int soggettiUtilizzati;

	private String categoriaTermine;

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getTipoLegame() {
		return TipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		TipoLegame = tipoLegame;
	}

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public boolean isRinvio() {
		return (isFilled(formaNome) && formaNome.equalsIgnoreCase("R"));
	}

	public int getPosizione() {
		return posizione;
	}

	public void setPosizione(int posizione) {
		this.posizione = posizione;
	}

	public int getSoggettiCollegati() {
		return soggettiCollegati;
	}

	public void setSoggettiCollegati(int soggettiCollegati) {
		this.soggettiCollegati = soggettiCollegati;
	}

	public int getSoggettiUtilizzati() {
		return soggettiUtilizzati;
	}

	public void setSoggettiUtilizzati(int soggettiUtilizzati) {
		this.soggettiUtilizzati = soggettiUtilizzati;
	}

	public String toString() {
		return "[did: " + did + "]";
	}

	public String getCategoriaTermine() {
		return categoriaTermine;
	}

	public void setCategoriaTermine(String categoriaTermine) {
		this.categoriaTermine = categoriaTermine;
	}

}
