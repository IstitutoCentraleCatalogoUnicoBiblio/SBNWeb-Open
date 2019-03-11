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
package it.iccu.sbn.ejb.vo.gestionestampe;

import java.io.Serializable;

public class StampaSoggettarioVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3678985569783289921L;
	// Attributes
	private String codiceSoggettario;
	private String dataInserimentoDa;
	private String dataInserimentoA;
	private String dataAggiornamentoDa;
	private String dataAggiornamentoA;


    // Constructors
    public StampaSoggettarioVO() {
    }


	public String getCodiceSoggettario() {
		return codiceSoggettario;
	}


	public void setCodiceSoggettario(String codiceSoggettario) {
		this.codiceSoggettario = codiceSoggettario;
	}


	public String getDataAggiornamentoA() {
		return dataAggiornamentoA;
	}


	public void setDataAggiornamentoA(String dataAggiornamentoA) {
		this.dataAggiornamentoA = dataAggiornamentoA;
	}


	public String getDataAggiornamentoDa() {
		return dataAggiornamentoDa;
	}


	public void setDataAggiornamentoDa(String dataAggiornamentoDa) {
		this.dataAggiornamentoDa = dataAggiornamentoDa;
	}


	public String getDataInserimentoA() {
		return dataInserimentoA;
	}


	public void setDataInserimentoA(String dataInserimentoA) {
		this.dataInserimentoA = dataInserimentoA;
	}


	public String getDataInserimentoDa() {
		return dataInserimentoDa;
	}


	public void setDataInserimentoDa(String dataInserimentoDa) {
		this.dataInserimentoDa = dataInserimentoDa;
	}

}

