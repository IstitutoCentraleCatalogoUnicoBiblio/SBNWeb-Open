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

import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale.DatiModelloUri;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale.TipoFileInput;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.vo.validators.AbstractValidator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class AcquisizioneUriCopiaDigitaleValidator extends AbstractValidator<AcquisizioneUriCopiaDigitaleVO> {

	private static final long serialVersionUID = 1234120873166615638L;

	public void validate(AcquisizioneUriCopiaDigitaleVO target) throws ValidationException {
		String tipoInput = target.getTipoInput();
		if (!isFilled(tipoInput))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "label.documentofisico.uri.tipoInput");

		String file = target.getInputFile();
		if (!FileUtil.exists(file))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "label.selezionafile");

		String digit = target.getTipoDigit();
		if (!isFilled(digit))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "documentofisico.tipoDigitalizzazioneT");

		TipoFileInput type = TipoFileInput.values()[Integer.valueOf(tipoInput)];
		String model = target.getModel();

		if (type.wantsModel()) {
			if (!isFilled(target.getPrefisso()))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "label.documentofisico.uri.prefix");

			if (!isFilled(model))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "label.documentofisico.uri.model");

			testUri(target);
		} else {
			if (isFilled(model))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "label.documentofisico.uri.model");
		}

		if (!type.hasInv()) {
			String teca = target.getTecaDigitale();
			if (isFilled(teca))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "documentofisico.rifTecaDigitaleT");

			String dispDaRemoto = target.getDispDaRemoto();
			if (isFilled(dispDaRemoto))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "documentofisico.dispDaRemotoT");
		}
	}

	public String testUri(AcquisizioneUriCopiaDigitaleVO target) throws ValidationException {
		try {
			DatiModelloUri data = null;
			TipoFileInput type = TipoFileInput.values()[Integer.valueOf(target.getTipoInput())];
			switch (type) {
			case Inventario:
			case ShippingManifest:
				InventarioVO inv = new InventarioVO();
				inv.setCodPolo(target.getCodPolo() );
				inv.setCodBib(target.getCodBib() );
				inv.setCodSerie("   ");
				inv.setCodInvent(5377);
				inv.setBid("ABC1234567");
				data = DatiModelloUri.build(inv);
				break;

			case BID:
				data = DatiModelloUri.build(target.getCodPolo(), target.getCodBib(), "ABC1234567");
				break;

			default:
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "label.documentofisico.uri.tipoInput");

			}
			String model = target.getModel();
			return AcquisizioneUriCopiaDigitale.costruisciUri(type, target.getPrefisso(), model, target.getSuffisso(), data, true);

		} catch (ApplicationException e) {
			throw new ValidationException(e.getErrorCode(), e.getLabels());
		}
	}

}
