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

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;

import java.sql.Timestamp;
import java.util.List;

public class ParametriBatchSollecitiVO extends StampaVo {

	private static final long serialVersionUID = -3393225794166744980L;

	private Timestamp dataScadenza;
	private Long[] listaRichieste;
	private String descrizioneBiblioteca;
	private ElaborazioniDifferiteOutputVo output;
	private List<ElementoStampaSollecitoVO> listaSollecitiPerStampa;

	public Timestamp getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Timestamp dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Long[] getListaRichieste() {
		return listaRichieste;
	}

	public void setListaRichieste(Long[] listaRichieste) {
		this.listaRichieste = listaRichieste;
	}

	public String getDescrizioneBiblioteca() {
		return descrizioneBiblioteca;
	}

	public void setDescrizioneBiblioteca(String descrizioneBiblioteca) {
		this.descrizioneBiblioteca = descrizioneBiblioteca;
	}

	public ElaborazioniDifferiteOutputVo getOutput() {
		return output;
	}

	public void setOutput(ElaborazioniDifferiteOutputVo output) {
		this.output = output;
	}

	public List<ElementoStampaSollecitoVO> getListaSollecitiPerStampa() {
		return listaSollecitiPerStampa;
	}

	public void setListaSollecitiPerStampa(
			List<ElementoStampaSollecitoVO> listaSollecitiPerStampa) {
		this.listaSollecitiPerStampa = listaSollecitiPerStampa;
	}

	public boolean isRichiestaDaSintetica() {
		return isFilled(listaRichieste);
	}

}
