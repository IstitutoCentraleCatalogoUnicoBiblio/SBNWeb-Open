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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO  extends SerializableVO {

	// = AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -4841560931127730805L;
	private String livelloTrovato;
    private TreeElementViewTitoli treeElementViewTitoli;
    private String codErr;
    private String testoProtocollo;
    private SBNMarc sbnMarcType;

    private String timeStampRadice;

	public String getTimeStampRadice() {
		return timeStampRadice;
	}

	public void setTimeStampRadice(String timeStampRadice) {
		this.timeStampRadice = timeStampRadice;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO() {
		super();
		this.codErr = "";
		this.testoProtocollo = "";
	}

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getLivelloTrovato() {
		return livelloTrovato;
	}

	public void setLivelloTrovato(String livelloTrovato) {
		this.livelloTrovato = livelloTrovato;
	}

	public TreeElementViewTitoli getTreeElementViewTitoli() {
		return treeElementViewTitoli;
	}

	public void setTreeElementViewTitoli(TreeElementViewTitoli treeElementViewTitoli) {
		this.treeElementViewTitoli = treeElementViewTitoli;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}

	public SBNMarc getSbnMarcType() {
		return sbnMarcType;
	}

	public void setSbnMarcType(SBNMarc sbnMarcType) {
		this.sbnMarcType = sbnMarcType;
	}


}
