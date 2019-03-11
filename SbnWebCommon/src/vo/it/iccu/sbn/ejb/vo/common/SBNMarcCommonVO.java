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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;


public abstract class SBNMarcCommonVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 2700820687199720227L;
	private String idLista;
	private String esito;
	private String testoEsito;
	private String dataInserimento;
	private String dataVariazione;
	private String T005;

	private int maxRighe;
	private int totRighe;
	private int numBlocco;
	private int numNotizie;
	private int totBlocchi;
	private int numPrimo;

	private boolean condiviso;
	private boolean livelloPolo;
	private boolean cattura;

	public SBNMarcCommonVO(SBNMarcCommonVO other) {
		super();
		this.idLista = other.idLista;
		this.esito = other.esito;
		this.testoEsito = other.testoEsito;
		this.dataInserimento = other.dataInserimento;
		this.dataVariazione = other.dataVariazione;
		this.T005 = other.T005;
		this.maxRighe = other.maxRighe;
		this.totRighe = other.totRighe;
		this.numBlocco = other.numBlocco;
		this.numNotizie = other.numNotizie;
		this.totBlocchi = other.totBlocchi;
		this.numPrimo = other.numPrimo;
		this.condiviso = other.condiviso;
		this.livelloPolo = other.livelloPolo;
		this.cattura = other.cattura;
	}

	public SBNMarcCommonVO() {
		super();
	}

	public boolean isLivelloPolo() {
		return livelloPolo;
	}

	public void setLivelloPolo(boolean livelloPolo) {
		this.livelloPolo = livelloPolo;
	}

	public boolean isCattura() {
		return cattura;
	}

	public void setCattura(boolean cattura) {
		this.cattura = cattura;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getEsito() {
		return esito;
	}

	public SbnMarcEsitoType getEsitoType() throws InfrastructureException {
		if (!isFilled(esito))
			throw new InfrastructureException("Errore esito SBNMarc");

		return SbnMarcEsitoType.of(esito);
	}

	public boolean isEsitoOk() throws InfrastructureException {
		return getEsitoType() == SbnMarcEsitoType.OK;
	}

	public boolean isEsitoNonTrovato() throws InfrastructureException {
		return getEsitoType() == SbnMarcEsitoType.NON_TROVATO;
	}

	public boolean isEsitoTroppiElementi() throws InfrastructureException {
		return (getEsitoType() == SbnMarcEsitoType.TROPPI_ELEMENTI);
	}

	public boolean isEsitoTrovatiSimili() throws InfrastructureException {
		return (getEsitoType() == SbnMarcEsitoType.TROVATI_SIMILI);
	}

	public boolean isEsitoIDEsistente() throws InfrastructureException {
		return (getEsitoType() == SbnMarcEsitoType.ID_ESISTENTE);
	}

	public boolean isEsitoEsisteComeRinvio() throws InfrastructureException {
		return (getEsitoType() == SbnMarcEsitoType.ID_ESISTE_COME_RINVIO);
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public String getTestoEsito() {
		return testoEsito;
	}

	public void setTestoEsito(String testoEsito) {
		this.testoEsito = testoEsito;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public boolean isCondiviso() {
		return condiviso;
	}

	public void setCondiviso(boolean condiviso) {
		this.condiviso = condiviso;
	}

}
