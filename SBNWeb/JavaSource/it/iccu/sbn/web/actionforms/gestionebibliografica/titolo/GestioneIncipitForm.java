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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioIncipitVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;


public class GestioneIncipitForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -6932220910723287840L;

	DettaglioIncipitVO dettaglioIncipitVO = new DettaglioIncipitVO();

	private int progrDettIncipit;

	private String tipoProspettazione;
	private String idOggColl;
	private String descOggColl;

	private String voceStrumento;
	private String voceStrumentoNum;

	private List listaVoceStrumento;
	private List listaFormaMusicale;
	private List listaTonalita;

	public DettaglioIncipitVO getDettaglioIncipitVO() {
		return dettaglioIncipitVO;
	}
	public void setDettaglioIncipitVO(DettaglioIncipitVO dettaglioIncipitVO) {
		this.dettaglioIncipitVO = dettaglioIncipitVO;
	}
	public List getListaFormaMusicale() {
		return listaFormaMusicale;
	}
	public void setListaFormaMusicale(List listaFormaMusicale) {
		this.listaFormaMusicale = listaFormaMusicale;
	}
	public List getListaTonalita() {
		return listaTonalita;
	}
	public void setListaTonalita(List listaTonalita) {
		this.listaTonalita = listaTonalita;
	}
	public List getListaVoceStrumento() {
		return listaVoceStrumento;
	}
	public void setListaVoceStrumento(List listaVoceStrumento) {
		this.listaVoceStrumento = listaVoceStrumento;
	}
	public String getDescOggColl() {
		return descOggColl;
	}
	public void setDescOggColl(String descOggColl) {
		this.descOggColl = descOggColl;
	}
	public String getIdOggColl() {
		return idOggColl;
	}
	public void setIdOggColl(String idOggColl) {
		this.idOggColl = idOggColl;
	}
	public String getTipoProspettazione() {
		return tipoProspettazione;
	}
	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}
	public int getProgrDettIncipit() {
		return progrDettIncipit;
	}
	public void setProgrDettIncipit(int progrDettIncipit) {
		this.progrDettIncipit = progrDettIncipit;
	}
	public String getVoceStrumento() {
		return voceStrumento;
	}
	public void setVoceStrumento(String voceStrumento) {
		this.voceStrumento = voceStrumento;
	}
	public String getVoceStrumentoNum() {
		return voceStrumentoNum;
	}
	public void setVoceStrumentoNum(String voceStrumentoNum) {
		this.voceStrumentoNum = voceStrumentoNum;
	}

}
