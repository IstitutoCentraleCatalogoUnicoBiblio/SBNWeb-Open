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
package it.iccu.sbn.ejb.vo.amministrazionesistema.codici;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.List;

public class CodiceFlagConfigVO extends SerializableVO {

	private static final long serialVersionUID = -7612592166852168787L;
	private static final int MAX_FLAG_LENGTH = 255;

	private boolean used = false;
	private String label;
	private int length;
	private CodiceFlagValueType type = CodiceFlagValueType.ALL;
	private int flg;
	private boolean allow_blank = false;
	private List<ComboCodDescVO> values;

	public static final CodiceFlagConfigVO build(int flg, String expression) throws ValidationException {
		CodiceFlagConfigVO config = new CodiceFlagConfigVO(expression);
		config.flg = flg;
		return config;
	}

	private CodiceFlagConfigVO(String expression) throws ValidationException {
		if (isNull(expression))
			return; // non usato

		String name = "Flg" + flg;

		try {
			String[] tokens = expression.trim().split("\\|");
			if (tokens.length != 4 && tokens.length != 5)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE, name);

			label = tokens[0];
			length = Integer.valueOf(tokens[1]);
			if (length > MAX_FLAG_LENGTH)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, name);

			used = (length > 0);
			type = CodiceFlagValueType.valueOf(tokens[2]);
			allow_blank = (CodiceBlankType.valueOf(tokens[3]) == CodiceBlankType.BLANK);
			if (type == CodiceFlagValueType.LIST)
				values = configureComboList(tokens[4], allow_blank);

		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE, "flag" + flg);
		}

	}

	private List<ComboCodDescVO> configureComboList(String field, boolean blank)
			throws ValidationException {
		try {
			List<ComboCodDescVO> output = new ArrayList<ComboCodDescVO>();
			if (blank)
				output.add(new ComboCodDescVO("", ""));

			String[] pairs = field.split(",");
			for (String pair : pairs) {
				String[] combo = pair.split("\\\\\\\\");
				output.add(new ComboCodDescVO(combo[0], combo[1]));
			}
			return output;

		} catch (Exception e) {
			throw new ValidationException(
					SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
		}
	}

	public boolean isUsed() {
		return used;
	}

	public String getLabel() {
		return label;
	}

	public int getLength() {
		return length;
	}

	public CodiceFlagValueType getType() {
		return type;
	}

	public int getFlg() {
		return flg;
	}

	public boolean isAllow_blank() {
		return allow_blank;
	}

	public List<ComboCodDescVO> getValues() {
		return values;
	}

	private void validateComboList(String value, List<ComboCodDescVO> values) throws ValidationException {
		if (!isFilled(values))
			throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, label);

		for (ComboCodDescVO combo : values) {
			if (ValidazioneDati.equals(value, combo.getCodice()))
				return;
		}

		throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, label);
	}

	public void validate(String flag) throws ValidationException {
		super.validate();
		if (used) {
			if (isNull(flag) && !allow_blank)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_CAMPO_OBBLIGATORIO, label);

			if (length(flag) > length)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, label);

			switch (type) {
			case ALPHA:
				if (isFilled(flag) && !isAlphabetic(flag))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, label);
				break;
			case NUMERIC:
				if (isFilled(flag) && !isNumeric(flag))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, label);
				break;
			case LIST:
				validateComboList(flag, values);
				break;
			}
		}
	}

}
