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
package it.iccu.sbn.vo.validators.documentofisico;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStrumentiPatrimonioVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StampaStrumentiPatrimonioValidator extends SerializableVO
		implements Validator<StampaStrumentiPatrimonioVO> {

	private static final long serialVersionUID = -1100439625077463989L;

	private void validaInputDataDaA(String dataDa, String dataA)
			throws ValidationException {

		int codRitorno = -1;
		String from = trimOrEmpty(dataDa);
		String to = trimOrEmpty(dataA);
		if (from.equals("")) {
//			if (to.equals("") || dataA.equals("00/00/0000")
//					|| !dataA.equals("00/00/0000")) {
//				throw new ValidationException("dataDaObbligatoria");
//			}
		} else {
			if (!from.equals("") && dataDa.equals("00/00/0000")) {
				throw new ValidationException("formatoDataDaNonValido");
			} else {
				codRitorno = ValidazioneDati.validaDataPassata(dataDa);
				if (codRitorno != ValidazioneDati.DATA_OK) {
					throw new ValidationException("dataDaErrata");
				} else {
					if (dataA != null && !to.equals("")) {
						if (dataA.equals("00/00/0000")) {
							throw new ValidationException("formatoDataANonValido");
						} else {
							codRitorno = ValidazioneDati.validaDataPassata(dataA);
							if (codRitorno != ValidazioneDati.DATA_OK) {
								throw new ValidationException("dataAErrata");
							} else {
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
								long longData1 = 0;
								long longData2 = 0;

								try {
									Date data1 = simpleDateFormat.parse(from);
									Date data2 = simpleDateFormat.parse(to);
									longData1 = data1.getTime();
									longData2 = data2.getTime();
								} catch (ParseException e) {
									throw new ValidationException("erroreParse");
								}

								if ((longData2 - longData1) < 0) {
									throw new ValidationException(
											"dataDaAErrata");
								}
							}
						}
					}
				}
			}
		}

	}

	public void validate(StampaStrumentiPatrimonioVO t)
			throws ValidationException {

		if (ValidazioneDati.equals(t.getTipoOperazione(), "D")) {
			boolean isRangeIngresso = t.isRangeIngresso();
			boolean isRangePrimaColl = t.isRangePrimaColl();

			if (!isRangeIngresso && !isRangePrimaColl)
				throw new ValidationException("dataDaObbligatoria");

			if (isRangeIngresso && isRangePrimaColl)
				throw new ValidationException(SbnErrorTypes.GDF_SOLO_UN_RANGE_DATE_INVENTARIO);

			if (isRangeIngresso)
				validaInputDataDaA(t.getDataDa(), t.getDataA() );

			if (isRangePrimaColl)
				validaInputDataDaA(t.getDataPrimaCollDa(), t.getDataPrimaCollA() );
		}

	}

}
