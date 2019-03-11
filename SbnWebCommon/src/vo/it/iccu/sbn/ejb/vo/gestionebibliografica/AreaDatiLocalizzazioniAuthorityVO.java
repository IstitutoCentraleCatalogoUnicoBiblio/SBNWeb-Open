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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiLocalizzazioniAuthorityVO extends SerializableVO {

	private static final long serialVersionUID = 3230826277632065413L;

	private String idLoc;
	private String authority;
	private String natura;
	private String tipoMat;

	private String nomeBiblioteca;
	private String CodiceAnagrafeBiblioteca;
	private String codiceSbn;

	private String fondo;
	private String consistenza;
	private String segnatura;
	private String segnaturaAntica;
	private String note;

	private String dispoFormatoElettr;
	private String indicatoreMutilo;
	private String uriAccesso;
	private String tipoDigitalizzazione;

	private String tipoLoc;
	private SbnTipoLocalizza sbnTipoLoc;
	private String tipoOpe;

	private C899 c899Localizzazioni;

	private boolean isPolo;
	private boolean isIndice;
	private boolean mantieniAllineamento;


	public boolean isMantieniAllineamento() {
		return mantieniAllineamento;
	}

	public void setMantieniAllineamento(boolean mantieniAllineamento) {
		this.mantieniAllineamento = mantieniAllineamento;
	}

	public String getIdLoc() {
		return idLoc;
	}

	public void setIdLoc(String idLoc) {
		this.idLoc = idLoc;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getCodiceAnagrafeBiblioteca() {
		return CodiceAnagrafeBiblioteca;
	}

	public void setCodiceAnagrafeBiblioteca(String codiceAnagrafeBiblioteca) {
		CodiceAnagrafeBiblioteca = codiceAnagrafeBiblioteca;
	}

	public String getCodiceSbn() {
		return codiceSbn;
	}

	public void setCodiceSbn(String codiceSbn) {
		this.codiceSbn = codiceSbn;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public String getDispoFormatoElettr() {
		return dispoFormatoElettr;
	}

	public void setDispoFormatoElettr(String dispoFormatoElettr) {
		this.dispoFormatoElettr = dispoFormatoElettr;
	}

	public String getFondo() {
		return fondo;
	}

	public void setFondo(String fondo) {
		this.fondo = fondo;
	}

	public String getIndicatoreMutilo() {
		return indicatoreMutilo;
	}

	public void setIndicatoreMutilo(String indicatoreMutilo) {
		this.indicatoreMutilo = indicatoreMutilo;
	}

	public String getNomeBiblioteca() {
		return nomeBiblioteca;
	}

	public void setNomeBiblioteca(String nomeBiblioteca) {
		this.nomeBiblioteca = nomeBiblioteca;
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

	public String getTipoLoc() {
		return tipoLoc;
	}

	public void setTipoLoc(String tipoLoc) {
		this.tipoLoc = tipoLoc;
	}

	public String getUriAccesso() {
		return uriAccesso;
	}

	public void setUriAccesso(String uriAccesso) {
		this.uriAccesso = uriAccesso;
	}


	public boolean isIndice() {
		return isIndice;
	}

	public void setIndice(boolean isIndice) {
		this.isIndice = isIndice;
	}

	public boolean isPolo() {
		return isPolo;
	}

	public void setPolo(boolean isPolo) {
		this.isPolo = isPolo;
	}

	public String getTipoOpe() {
		return tipoOpe;
	}

	public void setTipoOpe(String tipoOpe) {
		this.tipoOpe = tipoOpe;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getTipoMat() {
		return tipoMat;
	}

	public void setTipoMat(String tipoMat) {
		this.tipoMat = tipoMat;
	}

	public SbnTipoLocalizza getSbnTipoLoc() {
		return sbnTipoLoc;
	}

	public void setSbnTipoLoc(SbnTipoLocalizza sbnTipoLoc) {
		this.sbnTipoLoc = sbnTipoLoc;
	}

	public C899 getC899Localizzazioni() {
		return c899Localizzazioni;
	}

	public void setC899Localizzazioni(C899 localizzazioni) {
		c899Localizzazioni = localizzazioni;
	}


}
