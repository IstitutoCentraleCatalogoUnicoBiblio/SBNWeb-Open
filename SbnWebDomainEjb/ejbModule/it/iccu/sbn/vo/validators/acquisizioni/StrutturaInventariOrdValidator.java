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
package it.iccu.sbn.vo.validators.acquisizioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.Date;

public class StrutturaInventariOrdValidator extends SerializableVO implements
		Validator<StrutturaInventariOrdVO> {

	private static final long serialVersionUID = -7732797393078796794L;

	private final OrdiniVO ordine;


	public StrutturaInventariOrdValidator(OrdiniVO ordine) {
		super();
		this.ordine = ordine;
	}

	public OrdiniVO getOrdine() {
		return ordine;
	}

	public void validate(StrutturaInventariOrdVO target)	throws ValidationException {

		try {
			String rfid = target.getRfid();
			if (isFilled(rfid) ){
				if (isFilled(target.getSerie()) || isFilled(target.getNumero()))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "RFID");

				try {
					InventarioVO inv = InventarioRFIDParser.parse(rfid.toUpperCase());
					target.setSerie(inv.getCodSerie());
					target.setNumero(inv.getCodInvent() + "");
					if (isFilled(inv.getCodPolo()))
						target.setCodPolo(inv.getCodPolo());
					if (isFilled(inv.getCodBib()))
						target.setCodBibl(inv.getCodBib());

					target.setRfid(null);

				} catch (ValidationException e) {
					throw e;
				} catch (Exception e) {
					throw new ValidationException(e);
				}
			}

			if (!isNumeric(target.getNumero()))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "ordine.label.numInv" );

			Date dtUscita = DateUtil.toDate(target.getDataUscita());
			Date dtRientroPresunta = DateUtil.toDate(target.getDataRientroPresunta());
			Date dtRientroEff = DateUtil.toDate(target.getDataRientro());

			if (dtUscita == null && (dtRientroPresunta != null || dtRientroEff != null))
				throw new ValidationException(SbnErrorTypes.ACQ_DATA_USCITA_OBBLIGATORIA);

			if (dtRientroEff != null && !ordine.isSpedito())
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "ordine.label.rilegaturaDataRientro");

			if (dtUscita != null) {
				//data uscita <= data rientro presunta
				if (dtRientroPresunta != null && dtUscita.compareTo(dtRientroPresunta) > 0)
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

				//data uscita <= data rientro effettiva
				if (dtRientroEff != null && dtUscita.compareTo(dtRientroEff) > 0)
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

				//almaviva5_20130410 #5287 controllo inibito
				//data rientro presunta <= data rientro effettiva
				//if (dtRientroPresunta != null && dtRientroEff != null && dtRientroPresunta.compareTo(dtRientroEff) > 0)
				//	throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

			}

		} catch (ValidationException ve) {
			ve.addException(new ValidationException(SbnErrorTypes.ACQ_POSIZIONE_INVENTARIO,
					target.getPosizione().toString(), target.getVolume().toString()) );
			throw ve;
		}

	}

}
