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
package it.iccu.sbn.ejb.vo.servizi.batch;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ElementoStampaSollecitoVO extends SerializableVO {

	private static final long serialVersionUID = 351446547071568512L;

	public enum TipoUtente {
		MASCHIO,
		FEMMINA,
		ENTE;
	}

	private String idRichiesta;
	private String segnatura;
	private String inventario;
	private String titolo;
	private String codUtente;
	private String nomeUtente;
	private String dataPrestito;
	private String dataScadenza;
	private String numSollecito;
	private String tipoInvio;
	private String esito;

	private String tipoServizio;
	private String indirizzo;

	private String volume;
	private String anno;

	private TipoUtente tipo;

	//almaviva5_20120528 #5003
	protected String cap;
	protected String citta;
	protected String provincia;
	protected String nazione;

	public ElementoStampaSollecitoVO(ElementoStampaSollecitoVO ess) {
		super();
		this.idRichiesta = ess.idRichiesta;
		this.segnatura = ess.segnatura;
		this.inventario = ess.inventario;
		this.titolo = ess.titolo;
		this.codUtente = ess.codUtente;
		this.nomeUtente = ess.nomeUtente;
		this.dataPrestito = ess.dataPrestito;
		this.dataScadenza = ess.dataScadenza;
		this.numSollecito = ess.numSollecito;
		this.tipoInvio = ess.tipoInvio;
		this.esito = ess.esito;
		this.tipoServizio = ess.tipoServizio;
		this.indirizzo = ess.indirizzo;
		this.cap = ess.cap;
		this.citta = ess.citta;
		this.volume = ess.volume;
		this.anno = ess.anno;
		this.tipo = ess.tipo;
		this.provincia = ess.provincia;
		this.nazione = ess.nazione;
	}

	public ElementoStampaSollecitoVO() {
		super();
	}


	public TipoUtente getTipo() {
		return tipo;
	}

	public void setTipo(TipoUtente tipo) {
		this.tipo = tipo;
	}

	public String getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = trimAndSet(segnatura);
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = trimAndSet(titolo);
	}

	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = trimAndSet(codUtente);
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = trimAndSet(nomeUtente);
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getNumSollecito() {
		return numSollecito;
	}

	public void setNumSollecito(String numSollecito) {
		this.numSollecito = numSollecito;
	}

	public String getTipoInvio() {
		return tipoInvio;
	}

	public void setTipoInvio(String tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = trimAndSet(esito);
	}

	public String getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = trimAndSet(tipoServizio);
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = trimAndSet(indirizzo);
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = trimAndSet(cap);
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = trimAndSet(citta);
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = trimAndSet(inventario);
	}

	public String getDataPrestito() {
		return dataPrestito;
	}

	public void setDataPrestito(String dataPrestito) {
		this.dataPrestito = trimAndSet(dataPrestito);
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = trimAndSet(volume);
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = trimAndSet(anno);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idRichiesta == null) ? 0 : idRichiesta.hashCode());
		result = prime * result
				+ ((numSollecito == null) ? 0 : numSollecito.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementoStampaSollecitoVO other = (ElementoStampaSollecitoVO) obj;
		if (idRichiesta == null) {
			if (other.idRichiesta != null)
				return false;
		} else if (!idRichiesta.equals(other.idRichiesta))
			return false;
		if (numSollecito == null) {
			if (other.numSollecito != null)
				return false;
		} else if (!numSollecito.equals(other.numSollecito))
			return false;
		return true;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = trimAndSet(provincia);
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = trimAndSet(nazione);
	}

}
