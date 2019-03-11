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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class DocumentoVO extends SerializableVO {

	private static final long serialVersionUID = 5403863022575970879L;

	private String documento;
	private String numero;
	private String autRilascio;

	public DocumentoVO(String documento, String numero, String autRilascio)	throws Exception {
		super();
		this.documento = trimAndSet(documento);
		this.numero = trimAndSet(numero);
		this.autRilascio = trimAndSet(autRilascio);
	}

	public DocumentoVO() {
		this.clear();
	}

	public boolean isCompleto() {
		return isFilled(documento) && isFilled(numero) && isFilled(autRilascio);
	}

	public void clear() {
		this.documento = "";
		this.numero = "";
		this.autRilascio = "";
	}

	public String getAutRilascio() {
		return autRilascio;
	}

	public void setAutRilascio(String autRilascio) {
		this.autRilascio = trimAndSet(autRilascio);
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = trimAndSet(documento);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = trimAndSet(numero);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DocumentoVO other = (DocumentoVO) obj;
		if (autRilascio == null) {
			if (other.autRilascio != null)
				return false;
		} else if (!autRilascio.equals(other.autRilascio))
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public void validate() throws ValidationException {

		if (!isCompleto() && (isFilled(documento) || isFilled(numero) || isFilled(autRilascio)))
			throw new ValidationException(SbnErrorTypes.SRV_ERRORE_DOCUMENTO_INCOMPLETO);

		super.validate();
	}

}
