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
package it.iccu.sbn.web.actionforms.periodici.esame;

import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.EsameSeriePeriodicoVO;
import it.iccu.sbn.web.actionforms.periodici.PeriodiciListaForm;

public class RicercaFascicoliForm extends PeriodiciListaForm {


	private static final long serialVersionUID = -5624203274082746016L;
	private DatiBibliograficiPeriodicoVO datiBibliografici;
	private EsameSeriePeriodicoVO esame;
	private String bidSelezionato;

	public DatiBibliograficiPeriodicoVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(DatiBibliograficiPeriodicoVO dbp) {
		this.datiBibliografici = dbp;
	}

	public EsameSeriePeriodicoVO getEsame() {
		return esame;
	}

	public void setEsame(EsameSeriePeriodicoVO esame2) {
		this.esame = esame2;
	}

	public String getBidSelezionato() {
		return bidSelezionato;
	}

	public void setBidSelezionato(String bidSelezionato) {
		this.bidSelezionato = bidSelezionato;
	}



}
