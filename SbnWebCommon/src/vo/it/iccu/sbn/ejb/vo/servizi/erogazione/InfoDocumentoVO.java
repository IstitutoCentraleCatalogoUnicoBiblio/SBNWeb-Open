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
package it.iccu.sbn.ejb.vo.servizi.erogazione;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;

public class InfoDocumentoVO extends SerializableVO {

	private static final long serialVersionUID = -3199970851862384478L;

	private InventarioTitoloVO inventarioTitoloVO;
	private DocumentoNonSbnVO documentoNonSbnVO;
	private String titolo;
	private String segnatura;
	private String codFrui;
	private String codNoDisp;
	private String annoPeriodico;

	public InfoDocumentoVO() {
		super();
		this.inventarioTitoloVO = new InventarioTitoloVO();
		this.documentoNonSbnVO = new DocumentoNonSbnVO();
	}

	public InfoDocumentoVO(InventarioTitoloVO inventarioTitoloVO) {
		super();
		this.inventarioTitoloVO = inventarioTitoloVO;
	}

	public InfoDocumentoVO(InventarioTitoloVO inventarioTitoloVO, String titolo, String segnatura) {
		super();
		this.inventarioTitoloVO = inventarioTitoloVO;
		this.titolo = titolo;
		this.segnatura = segnatura;
	}

	public InventarioTitoloVO getInventarioTitoloVO() {
		return inventarioTitoloVO;
	}

	public void setInventarioTitoloVO(InventarioTitoloVO inventarioTitoloVO) {
		this.inventarioTitoloVO = inventarioTitoloVO;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = trimAndSet(titolo);
	}

	public String getSegnatura() {
		if (isFilled(segnatura))
			return segnatura;

		if (isRichiestaSuInventario() )
			return inventarioTitoloVO.getCodLoc();
		if (isRichiestaSuSegnatura() )
			return documentoNonSbnVO.getSegnatura();

		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = trimAndSet(segnatura);
	}

	public String getCodFrui() {
		return codFrui;
	}

	public void setCodFrui(String codFrui) {
		this.codFrui = codFrui;
	}

	public String getCodNoDisp() {
		return codNoDisp;
	}

	public void setCodNoDisp(String codNoDisp) {
		this.codNoDisp = codNoDisp;
	}

	public DocumentoNonSbnVO getDocumentoNonSbnVO() {
		return documentoNonSbnVO;
	}

	public void setDocumentoNonSbnVO(DocumentoNonSbnVO documentoNonSbnVO) {
		this.documentoNonSbnVO = documentoNonSbnVO;
	}

	public boolean isRichiestaSuInventario() {
		return (inventarioTitoloVO != null
			&& isFilled(inventarioTitoloVO.getCodBib())
			&& inventarioTitoloVO.getCodInvent() > 0);
	}

	public boolean isRichiestaSuSegnatura() {
		return (documentoNonSbnVO != null
				&& isFilled(documentoNonSbnVO.getCodBib())
				&& documentoNonSbnVO.getCod_doc_lett() > 0);
	}

	public void clear() {
		this.inventarioTitoloVO = null;
		this.documentoNonSbnVO = null;
		this.titolo = null;
		this.segnatura = null;
		this.codFrui = null;
		this.codNoDisp = null;
	}

	public String getAnnoPeriodico() {
		return annoPeriodico;
	}

	public void setAnnoPeriodico(String annoPeriodico) {
		this.annoPeriodico = trimAndSet(annoPeriodico);
	}

}
