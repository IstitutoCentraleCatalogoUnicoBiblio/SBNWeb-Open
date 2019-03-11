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

import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.SinteticaImmaginiMarcheView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;


public class SinteticaImmaginiMarcheForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -4048556763769715990L;
	private String livRicerca;
	private List listaSintetica;
	private String[] chiaviImmagini;

	private List<SinteticaImmaginiMarcheView> listaSintImmagini;

	private String selezImg;


	// Aree della sintetica utilizzate per la prospettazione degli oggetti in creazione legame titolo
	private String prospettaDatiOggColl;
	private String idOggColl;
	private String descOggColl;

	private String prospettazionePerLegami;
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();



	public String getSelezImg() {
		return selezImg;
	}

	public void setSelezImg(String selezRadio) {
		this.selezImg = selezRadio;
	}

	public String getLivRicerca() {
		return livRicerca;
	}

	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public void setChiaviImmagini(String[] keyImage) {
		this.chiaviImmagini = keyImage;

	}

	public String[] getChiaviImmagini() {
		return chiaviImmagini;
	}

	public List<SinteticaImmaginiMarcheView> getListaSintImmagini() {
		return listaSintImmagini;
	}

	public void setListaSintImmagini(
			List<SinteticaImmaginiMarcheView> listaSintImmagini) {
		this.listaSintImmagini = listaSintImmagini;
	}

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public String getProspettazionePerLegami() {
		return prospettazionePerLegami;
	}

	public void setProspettazionePerLegami(String prospettazionePerLegami) {
		this.prospettazionePerLegami = prospettazionePerLegami;
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

	public String getProspettaDatiOggColl() {
		return prospettaDatiOggColl;
	}

	public void setProspettaDatiOggColl(String prospettaDatiOggColl) {
		this.prospettaDatiOggColl = prospettaDatiOggColl;
	}

}
