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
package it.iccu.sbn.vo.custom.servizi;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.web.constant.ServiziConstant;

public class CodTipoServizio extends TB_CODICI {

	private static final long serialVersionUID = 5203681504194231194L;

	public static final CodTipoServizio of(TB_CODICI cod) {
		if (cod == null)
			return null;

		return new CodTipoServizio(cod);
	}

	private CodTipoServizio(TB_CODICI cod) {
		super(cod);
	}

	public String getFamiglia() {
		return getCd_flg3();
	}

	public boolean isLocale() {
		return ValidazioneDati.equals(getCd_flg4(), "L");
	}

	public boolean richiedeModalitaErogazione() {
		return ValidazioneDati.equals(getCd_flg5(), "S");
	}

	public boolean richiedeSupporto() {
		return ValidazioneDati.equals(getCd_flg6(), "S");
	}

	public boolean richiedeIter() {
		return ValidazioneDati.equals(getCd_flg7(), "S");
	}

	public RuoloBiblioteca getRuolo() {
		return RuoloBiblioteca.fromString(getCd_flg8());
	}

	public boolean isILL() {
	/*
		if (!isLocale()) {
			try {
				//verifica che il servizio definito sia legato a un servizio ISO-ILL
				ILLServiceType servizioILL = ServiziUtil.decodificaTipoServizioILL(getCd_tabellaTrim());
				return (servizioILL != null);
			} catch (Exception e) {	}
		}
		return false;
	*/
		return !isLocale();
	}

	public boolean isRiproduzione() {
		return ValidazioneDati.equals(getFamiglia(), ServiziConstant.FAMIGLIA_SRV_RIPRODUZIONE);
	}

	public boolean isPrestito() {
		return ValidazioneDati.equals(getFamiglia(), ServiziConstant.FAMIGLIA_SRV_PRESTITO);
	}

	public boolean isConsultazione() {
		return ValidazioneDati.equals(getFamiglia(), ServiziConstant.FAMIGLIA_SRV_CONSULTAZIONE);
	}


}
