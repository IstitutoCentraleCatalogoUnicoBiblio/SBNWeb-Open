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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.Date;

public class RifiutaPrenotazioniScaduteVO extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = 529617221462841955L;

	private Date dataFinePrevista;
	private boolean livelloPolo;

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (dataFinePrevista == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.erogazione.dataFinePrevista");
	}

	public Date getDataFinePrevista() {
		return dataFinePrevista;
	}

	public void setDataFinePrevista(Date dataFinePrevista) {
		this.dataFinePrevista = dataFinePrevista;
	}

	public boolean isLivelloPolo() {
		return livelloPolo;
	}

	public void setLivelloPolo(boolean livelloPolo) {
		this.livelloPolo = livelloPolo;
	}

}
