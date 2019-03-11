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
package it.iccu.sbn.vo.validators.periodici;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.PeriodicitaFascicoloType;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class FascicoloValidator extends SerializableVO implements Validator<FascicoloVO> {

	private static final long serialVersionUID = -3143948005102609547L;
	private static final String MULTIPLO = "M";

	private final SeriePeriodicoType type;

	public FascicoloValidator(SeriePeriodicoType type) {
		this.type = type;
	}

	public void validate(FascicoloVO target) throws ValidationException {
		FascicoloVO f = target;
		String cd_per = f.getCd_per();

		if (f instanceof FascicoloDecorator) {
			FascicoloDecorator fd = (FascicoloDecorator) f;

			//almaviva5_20111102 #4708
			ValidationException[] exceptions = fd.getValidationExceptions();
			if (isFilled(exceptions))
				throw new ValidationException(exceptions);
		}

		if (!isFilled(cd_per))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.kardex.tipo.per");

		PeriodicitaFascicoloType tipoPeriodicita = PeriodicitaFascicoloType.fromString(cd_per);
		if (tipoPeriodicita == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.kardex.tipo.per");

		if (!isFilled(f.getCd_tipo_fasc()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.kardex.tipo_fasc");

		if (length(f.getNote()) > 240)
			throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, "note");

		boolean isMultiplo = isMultiplo(target);

		if (f.getData_in_pubbl() == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.kardex.data.conv.fasc");

		if (isMultiplo) { //fascicolo multiplo
			if (f.getData_fi_pubbl() == null)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.kardex.data.conv.fasc");

			if (f.getData_in_pubbl().after(f.getData_fi_pubbl()) )
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

			if (!checkIntervallo(f.getNum_in_fasc(), f.getNum_fi_fasc()) )	//intervallo fascicoli
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_INTERVALLO_NUM_FASCICOLO);

			if (isFilled(f.getNum_alter()))
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_NUM_ALTER_FASCICOLO_MULTIPLO);

		}

		if (!isMultiplo) {

			//almaviva5_20101015 fix migrazione NAP
			if (ValidazioneDati.equals(f.getData_in_pubbl(), f.getData_fi_pubbl()) )
				f.setData_fi_pubbl(null);
			if (ValidazioneDati.equals(f.getNum_in_fasc(), f.getNum_fi_fasc()) )
				f.setNum_fi_fasc(null);
			if (isFilled(f.getNum_in_fasc()) && isFilled(f.getNum_alter())
					&& ValidazioneDati.equals(f.getNum_in_fasc().toString(), f.getNum_alter()) )
				f.setNum_alter(null);
			//

			if (f.getData_fi_pubbl() != null)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "Data_fi_pubbl");

			if (isFilled(f.getNum_fi_fasc()) )
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "Num_fi_fasc");

			if (isFilled(f.getNum_in_fasc()) && isFilled(f.getNum_alter()) )
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_NUM_ALTER_FASCICOLO_ALTERNATIVI);

			//almaviva5_20110622 segn. ICCU: per periodicita K annuale, L biennale o M triennale
			//il numero fascicolo non è obbligatorio
			//almaviva5_20160615 segnalazione ICCU: aggiunti altri stati (altro, irregolare, ecc.)
			if (!ValidazioneDati.in(tipoPeriodicita,
					PeriodicitaFascicoloType.ANNUALE,
					PeriodicitaFascicoloType.BIENNALE,
					PeriodicitaFascicoloType.TRIENNALE,
					PeriodicitaFascicoloType.ALTRO,
					PeriodicitaFascicoloType.IRREGOLARE,
					PeriodicitaFascicoloType.VARIABILE,
					PeriodicitaFascicoloType.SCONOSCIUTO) )
				if (!isFilled(f.getNum_in_fasc()) && !isFilled(f.getNum_alter()) )
					throw new ValidationException(SbnErrorTypes.PER_ERRORE_NUM_ALTER_FASCICOLO_ALMENO_UNO);
		}

		EsemplareFascicoloVO ef = f.getEsemplare();
		if (ef == null || ef.isCancellato())
			return;

//		if (ef.getData_arrivo() == null)
//			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "Data_arrivo");
		if (length(ef.getNote()) > 240)
			throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, "note esemplare");

		//almaviva5_20110211 #4221
		if ((ef.getCod_ord() == 0 && ef.getId_ordine() == 0) && type == SeriePeriodicoType.ORDINE)
			throw new ValidationException(SbnErrorTypes.PER_ASSOCIARE_ORDINE);

		if (ef.getCd_inven() == 0 &&
				ValidazioneDati.in(type, SeriePeriodicoType.ESEMPLARE, SeriePeriodicoType.COLLOCAZIONE) )
			throw new ValidationException(SbnErrorTypes.PER_ASSOCIARE_INVENTARIO);

		if (ef.getCd_inven() == 0 && isFilled(ef.getGrp_fasc()))
			throw new ValidationException(SbnErrorTypes.PER_GRUPPO_VALIDO_SOLO_PER_INVENTARIO);

		if (f instanceof FascicoloDecorator) {
			FascicoloDecorator fd = (FascicoloDecorator) f;

			//almaviva5_20111102 #4708
			ValidationException[] exceptions = fd.getValidationExceptions();
			if (isFilled(exceptions))
				throw new ValidationException(exceptions);

			if (fd.isSmarrito() && fd.isRilegatura())
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_SMARRITO_RILEGATURA_ALTERNATIVI);

			//smarrito = S; rilegatura = R
			if (fd.isSmarrito())
				ef.setCd_no_disp('S');
			else
				if (fd.isRilegatura())
					ef.setCd_no_disp('R');
				else
					ef.setCd_no_disp(null);
		}
	}

	private boolean checkIntervallo(Integer i1, Integer i2) {
		if (!isFilled(i1) || !isFilled(i2) )
			return false;

		return (i1.intValue() < i2.intValue());
	}

	public static final boolean isMultiplo(FascicoloVO f) {
		return ValidazioneDati.equals(f.getCd_tipo_fasc(), MULTIPLO);
	}

}
