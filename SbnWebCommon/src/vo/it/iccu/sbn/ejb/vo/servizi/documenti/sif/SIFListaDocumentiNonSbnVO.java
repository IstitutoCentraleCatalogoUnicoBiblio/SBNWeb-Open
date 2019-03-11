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
package it.iccu.sbn.ejb.vo.servizi.documenti.sif;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

public class SIFListaDocumentiNonSbnVO extends SerializableVO {

	private static final long serialVersionUID = -6359744730157310626L;

	protected String codPolo;
	protected String codBib;
	protected List<String> listaBib;
	protected String requestAttribute;
	protected int elementiPerBlocco = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	private DocumentoNonSbnVO documento;
	private String segnatura;
	private boolean puntuale = false;

	private TipoSIF tipoSIF = TipoSIF.DOCUMENTO_POSSEDUTO;
	private String titolo;

	private String tipoServizio;

	public enum TipoSIF  {
		DOCUMENTO_POSSEDUTO,
		DOCUMENTO_ALTRA_BIB,
	}

	public SIFListaDocumentiNonSbnVO() {
		super();
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public int getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public void setElementiPerBlocco(int elementiPerBlocco) {
		this.elementiPerBlocco = elementiPerBlocco;
	}

	public String getRequestAttribute() {
		return requestAttribute;
	}

	public void setRequestAttribute(String requestAttribute) {
		this.requestAttribute = requestAttribute;
	}

	public DocumentoNonSbnVO getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoNonSbnVO documento) {
		this.documento = documento;
	}

	public List<String> getListaBib() {
		return listaBib;
	}

	public void setListaBib(List<String> listaBib) {
		this.listaBib = listaBib;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = trimAndSet(segnatura);
	}

	public boolean isPuntuale() {
		return puntuale;
	}

	public void setPuntuale(boolean puntuale) {
		this.puntuale = puntuale;
	}

	public TipoSIF getTipoSIF() {
		return tipoSIF;
	}

	public void setTipoSIF(TipoSIF tipoSIF) {
		this.tipoSIF = tipoSIF;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = trimAndSet(titolo);
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public String getTipoServizio() {
		return tipoServizio;
	}

}
