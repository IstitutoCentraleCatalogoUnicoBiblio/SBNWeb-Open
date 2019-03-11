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

import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.DettaglioMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import org.apache.struts.upload.FormFile;

public class DettaglioMarcaForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -743357707907630419L;

	private String bidPerRientroAnalitica;

    private DettaglioMarcaGeneraleVO dettMarcaVO = new DettaglioMarcaGeneraleVO();

	private String[] selezCheck;

    private FormFile uploadImmagine;

	private String tipoProspettazione;
	private boolean flagCondiviso;

	// Elenco oggetti per prospettazione descrizione/arrayList descrizioni/combo
	// per il riempimento delle liste di decodifica
    private List listaLivAut;
	private String descLivAut;
    private List listaRepertori;
	private String descRepertori1;
	private String descRepertori2;
	private String descRepertori3;

	private String campoCodiceRep1Mod;
	private String campoProgressivoRep1Mod;
	private String campoCodiceRep2Mod;
	private String campoProgressivoRep2Mod;
	private String campoCodiceRep3Mod;
	private String campoProgressivoRep3Mod;

	// Aree della sintetica utilizzate per la prospettazione degli oggetti in creazione legame titolo
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();

    public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public String getDescLivAut() {
		return descLivAut;
	}

	public void setDescLivAut(String descLivAut) {
		this.descLivAut = descLivAut;
	}

	public List getListaLivAut() {
		return listaLivAut;
	}

	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public DettaglioMarcaGeneraleVO getDettMarcaVO() {
		return dettMarcaVO;
	}

	public void setDettMarcaVO(DettaglioMarcaGeneraleVO dettMarcaVO) {
		this.dettMarcaVO = dettMarcaVO;
	}

	public List getListaRepertori() {
		return listaRepertori;
	}

	public void setListaRepertori(List listaRepertori) {
		this.listaRepertori = listaRepertori;
	}

	public String getCampoCodiceRep1Mod() {
		return campoCodiceRep1Mod;
	}

	public void setCampoCodiceRep1Mod(String campoCodiceRep1Mod) {
		this.campoCodiceRep1Mod = campoCodiceRep1Mod;
	}

	public String getCampoCodiceRep2Mod() {
		return campoCodiceRep2Mod;
	}

	public void setCampoCodiceRep2Mod(String campoCodiceRep2Mod) {
		this.campoCodiceRep2Mod = campoCodiceRep2Mod;
	}

	public String getCampoCodiceRep3Mod() {
		return campoCodiceRep3Mod;
	}

	public void setCampoCodiceRep3Mod(String campoCodiceRep3Mod) {
		this.campoCodiceRep3Mod = campoCodiceRep3Mod;
	}

	public String getCampoProgressivoRep1Mod() {
		return campoProgressivoRep1Mod;
	}

	public void setCampoProgressivoRep1Mod(String campoProgressivoRep1Mod) {
		this.campoProgressivoRep1Mod = campoProgressivoRep1Mod;
	}

	public String getCampoProgressivoRep2Mod() {
		return campoProgressivoRep2Mod;
	}

	public void setCampoProgressivoRep2Mod(String campoProgressivoRep2Mod) {
		this.campoProgressivoRep2Mod = campoProgressivoRep2Mod;
	}

	public String getCampoProgressivoRep3Mod() {
		return campoProgressivoRep3Mod;
	}

	public void setCampoProgressivoRep3Mod(String campoProgressivoRep3Mod) {
		this.campoProgressivoRep3Mod = campoProgressivoRep3Mod;
	}

	public String getDescRepertori1() {
		return descRepertori1;
	}

	public void setDescRepertori1(String descRepertori1) {
		this.descRepertori1 = descRepertori1;
	}

	public String getDescRepertori2() {
		return descRepertori2;
	}

	public void setDescRepertori2(String descRepertori2) {
		this.descRepertori2 = descRepertori2;
	}

	public String getDescRepertori3() {
		return descRepertori3;
	}

	public void setDescRepertori3(String descRepertori3) {
		this.descRepertori3 = descRepertori3;
	}

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}

	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}

	public String getBidPerRientroAnalitica() {
		return bidPerRientroAnalitica;
	}

	public void setBidPerRientroAnalitica(String bidPerRientroAnalitica) {
		this.bidPerRientroAnalitica = bidPerRientroAnalitica;
	}

	public String[] getSelezCheck() {
		return selezCheck;
	}

	public void setSelezCheck(String[] selezCheck) {
		this.selezCheck = selezCheck;
	}


}
