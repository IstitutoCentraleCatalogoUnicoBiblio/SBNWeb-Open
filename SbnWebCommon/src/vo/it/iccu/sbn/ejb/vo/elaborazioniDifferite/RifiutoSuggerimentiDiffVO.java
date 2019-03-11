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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;

public class RifiutoSuggerimentiDiffVO extends
		ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -3019497100487321388L;

	OperazioneSuOrdiniMassivaVO operazioneSuOrdini;
	private Object parametri;
	private String tipoOrdinamento;

	public Object getParametri() {
		return parametri;
	}

	public void setParametri(Object parametri) {
		this.parametri = parametri;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public OperazioneSuOrdiniMassivaVO getOperazioneSuOrdini() {
		return operazioneSuOrdini;
	}

	public void setOperazioneSuOrdini(
			OperazioneSuOrdiniMassivaVO operazioneSuOrdini) {
		this.operazioneSuOrdini = operazioneSuOrdini;
	}

}
