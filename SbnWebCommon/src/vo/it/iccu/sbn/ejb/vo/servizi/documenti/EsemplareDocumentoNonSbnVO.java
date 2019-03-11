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
package it.iccu.sbn.ejb.vo.servizi.documenti;

import it.iccu.sbn.ejb.vo.BaseVO;

public class EsemplareDocumentoNonSbnVO extends BaseVO {

	private static final long serialVersionUID = -2513936565872402044L;

	private int idEsemplare;

	private int idDocumento;
	private String codBib;
	private String codPolo;
	private char tipo_doc_lett;
	private long cod_doc_lett;

	private short prg_esemplare;
	private char fonte;
	private String inventario = "";
	private String annata = "";
	private String codNoDisp = "";


	public EsemplareDocumentoNonSbnVO() {
		super();
	}

	public EsemplareDocumentoNonSbnVO(int idDocumento, String codPolo,
			String codBib, char tipo_doc_lett, long cod_doc_lett) {
		super();
		this.idDocumento = idDocumento;
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.tipo_doc_lett = tipo_doc_lett;
		this.cod_doc_lett = cod_doc_lett;
	}

	public EsemplareDocumentoNonSbnVO(DocumentoNonSbnVO dettaglio) {
		this.idDocumento = dettaglio.getIdDocumento();
		this.codPolo = dettaglio.getCodPolo();
		this.codBib = dettaglio.getCodBib();
		this.tipo_doc_lett = dettaglio.getTipo_doc_lett();
		this.cod_doc_lett = dettaglio.getCod_doc_lett();
	}

	public int getIdEsemplare() {
		return idEsemplare;
	}

	public void setIdEsemplare(int idEsemplare) {
		this.idEsemplare = idEsemplare;
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public char getTipo_doc_lett() {
		return tipo_doc_lett;
	}

	public void setTipo_doc_lett(char tipo_doc_lett) {
		this.tipo_doc_lett = tipo_doc_lett;
	}

	public long getCod_doc_lett() {
		return cod_doc_lett;
	}

	public void setCod_doc_lett(long cod_doc_lett) {
		this.cod_doc_lett = cod_doc_lett;
	}

	public short getPrg_esemplare() {
		return prg_esemplare;
	}

	public void setPrg_esemplare(short prg_esemplare) {
		this.prg_esemplare = prg_esemplare;
	}

	public char getFonte() {
		return fonte;
	}

	public void setFonte(char fonte) {
		this.fonte = fonte;
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = trimAndSet(inventario);
	}

	public String getAnnata() {
		return annata;
	}

	public void setAnnata(String annata) {
		this.annata = trimAndSet(annata);
	}

	public String getCodNoDisp() {
		return codNoDisp;
	}

	public void setCodNoDisp(String codNoDisp) {
		this.codNoDisp = trimAndSet(codNoDisp);
	}

	public boolean isNuovo() {
		return (idEsemplare == 0);
	}

	public boolean isCancellato() {
		String flCanc = getFlCanc();
		return (isFilled(flCanc) && flCanc.equalsIgnoreCase("S"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((annata == null) ? 0 : annata.hashCode());
		result = prime * result + ((codBib == null) ? 0 : codBib.hashCode());
		result = prime * result
				+ ((codNoDisp == null) ? 0 : codNoDisp.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result + (int) (cod_doc_lett ^ (cod_doc_lett >>> 32));
		result = prime * result + fonte;
		result = prime * result + idDocumento;
		result = prime * result + idEsemplare;
		result = prime * result
				+ ((inventario == null) ? 0 : inventario.hashCode());
		result = prime * result + prg_esemplare;
		result = prime * result + tipo_doc_lett;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EsemplareDocumentoNonSbnVO other = (EsemplareDocumentoNonSbnVO) obj;
		if (annata == null) {
			if (other.annata != null)
				return false;
		} else if (!annata.equals(other.annata))
			return false;
		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;
		if (isNull(codNoDisp) ) {
			if (isFilled(other.codNoDisp))
				return false;
		} else if (!codNoDisp.equals(other.codNoDisp))
			return false;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
		if (cod_doc_lett != other.cod_doc_lett)
			return false;
		if (fonte != other.fonte)
			return false;
		if (idDocumento != other.idDocumento)
			return false;
		if (idEsemplare != other.idEsemplare)
			return false;
		if (inventario == null) {
			if (other.inventario != null)
				return false;
		} else if (!inventario.equals(other.inventario))
			return false;
		if (prg_esemplare != other.prg_esemplare)
			return false;
		if (tipo_doc_lett != other.tipo_doc_lett)
			return false;
		return true;
	}

}
