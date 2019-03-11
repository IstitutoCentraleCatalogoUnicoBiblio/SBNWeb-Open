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

package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Comparator;
import java.util.List;


public class SinteticaLocalizzazioniView  extends SerializableVO {

	// = SinteticaLocalizzazioniView.class.hashCode();
// Dati relativi alla ricerca marca - Canali principali

	/**
	 *
	 */
	private static final long serialVersionUID = -2345128608586738437L;
	private int numNotizie;
	private int progressivo;
	private String IDSbn;
	private String IDAnagrafe;
	private String descrBiblioteca;
	private String tipoLoc;
	private List listaTipoLoc;
	private String fondo;
	private String segnatura;
	private String segnaturaAntica;
	private String consistenza;
	private String note;
	private String formatoElettronico;
	private String valoreM;
	private String uriCopiaElettr;
	private String tipoDigitalizzazione;
	private String codBiblioteca;
	// Modifica almaviva2 14.10.2010 BUG MANTIS 3930 per ogni elemento di lista si salva anche tutta l'etichetta
	// C899 per inviarla in ciclo in Indice nel caso di invio in Indice delle localizzazioni presenti in Polo
	private C899 c899Localizzazioni;

    /**
     * Comparator that can be used for a case insensitive sort of
     * <code>LabelValueBean</code> objects.
     */
    public static final Comparator sortListaSinteticaLoc = new Comparator() {
    	public int compare(Object o1, Object o2) {
    		int myProgr1 = ((SinteticaLocalizzazioniView) o1).getProgressivo();
    		int myProgr2 = ((SinteticaLocalizzazioniView) o2).getProgressivo();
    		return myProgr1-myProgr2;
    	}
    };

	public int getNumNotizie() {
		return numNotizie;
	}
	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}
	public int getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}
	public String getDescrBiblioteca() {
		return descrBiblioteca;
	}
	public void setDescrBiblioteca(String descrBiblioteca) {
		this.descrBiblioteca = descrBiblioteca;
	}
	public String getIDAnagrafe() {
		return IDAnagrafe;
	}
	public void setIDAnagrafe(String anagrafe) {
		IDAnagrafe = anagrafe;
	}
	public String getIDSbn() {
		return IDSbn;
	}
	public void setIDSbn(String sbn) {
		IDSbn = sbn;
	}
	public String getTipoLoc() {
		return tipoLoc;
	}
	public void setTipoLoc(String tipoLoc) {
		this.tipoLoc = tipoLoc;
	}
	public String getConsistenza() {
		return consistenza;
	}
	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}
	public String getValoreM() {
		return valoreM;
	}
	public void setValoreM(String valoreM) {
		this.valoreM = valoreM;
	}
	public String getFondo() {
		return fondo;
	}
	public void setFondo(String fondo) {
		this.fondo = fondo;
	}
	public String getFormatoElettronico() {
		return formatoElettronico;
	}
	public void setFormatoElettronico(String formatoElettronico) {
		this.formatoElettronico = formatoElettronico;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getSegnatura() {
		return segnatura;
	}
	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}
	public String getSegnaturaAntica() {
		return segnaturaAntica;
	}
	public void setSegnaturaAntica(String segnaturaAntica) {
		this.segnaturaAntica = segnaturaAntica;
	}
	public String getTipoDigitalizzazione() {
		return tipoDigitalizzazione;
	}
	public void setTipoDigitalizzazione(String tipoDigitalizzazione) {
		this.tipoDigitalizzazione = tipoDigitalizzazione;
	}
	public String getUriCopiaElettr() {
		return uriCopiaElettr;
	}
	public void setUriCopiaElettr(String uriCopiaElettr) {
		this.uriCopiaElettr = uriCopiaElettr;
	}
	public String getCodBiblioteca() {
		return codBiblioteca;
	}
	public void setCodBiblioteca(String codBiblioteca) {
		this.codBiblioteca = codBiblioteca;
	}
	public C899 getC899Localizzazioni() {
		return c899Localizzazioni;
	}
	public void setC899Localizzazioni(C899 c899Localizzazioni) {
		this.c899Localizzazioni = c899Localizzazioni;
	}
	public List getListaTipoLoc() {
		return listaTipoLoc;
	}
	public void setListaTipoLoc(List listaTipoLoc) {
		this.listaTipoLoc = listaTipoLoc;
	}


}
