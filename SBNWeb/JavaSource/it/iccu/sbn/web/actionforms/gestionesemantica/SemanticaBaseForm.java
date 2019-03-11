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
package it.iccu.sbn.web.actionforms.gestionesemantica;

import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;

import org.apache.struts.action.ActionForm;

public abstract class SemanticaBaseForm extends ActionForm {


	private static final long serialVersionUID = 8000512671386976708L;
	private String biblioteca = "XXXAMM";
	private String userId = "00001";
	private int maxRighe = 15;
	private String paginaCorrente = "1";
	private String tipoOrd = "1";
	private String tipoOutput = "001";
	private int totRighe;
	private int bloccoSelezionato = 1;
	private String idLista = "";
	private int numPrimo = 0;
	private ParametriSoggetti parametriSogg = new ParametriSoggetti();
	private ParametriThesauro parametriThes = new ParametriThesauro();

	private OggettoRiferimentoVO oggettoRiferimento = new OggettoRiferimentoVO();

	public ParametriThesauro getParametriThes() {
		return parametriThes;
	}

	public void setParametriThes(ParametriThesauro parametriThes) {
		this.parametriThes = parametriThes;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public String getPaginaCorrente() {
		return paginaCorrente;
	}

	public void setPaginaCorrente(String paginaCorrente) {
		this.paginaCorrente = paginaCorrente;
	}

	public String getTipoOrd() {
		return tipoOrd;
	}

	public void setTipoOrd(String tipoOrd) {
		this.tipoOrd = tipoOrd;
	}

	public String getTipoOutput() {
		return tipoOutput;
	}

	public void setTipoOutput(String tipoOutput) {
		this.tipoOutput = tipoOutput;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTotPagineOut() {
		int np = 0;
		try {
			np = getTotRighe() / getMaxRighe();
			if (getTotRighe() % getMaxRighe() > 0) {
				np++;
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		return np;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public ParametriSoggetti getParametriSogg() {
		return parametriSogg;
	}

	public void setParametriSogg(ParametriSoggetti parametriSogg) {
		this.parametriSogg = parametriSogg;
	}

	public OggettoRiferimentoVO getOggettoRiferimento() {
		return oggettoRiferimento;
	}

	public void setOggettoRiferimento(OggettoRiferimentoVO oggettoRiferimento) {
		this.oggettoRiferimento = oggettoRiferimento;
	}

}
