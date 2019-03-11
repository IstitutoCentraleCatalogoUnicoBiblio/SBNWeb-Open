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
package it.iccu.sbn.util.periodici;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.constant.PeriodiciConstants;

import java.util.Arrays;
import java.util.List;

public class Progressivo extends SerializableVO {

	private static final long serialVersionUID = -2298908054869367129L;

	private boolean custom;
	private final String initialValue;
	private final String model;
	private String currentValue;
	private List<String> values = ValidazioneDati.emptyList();
	private List<String> lowerValues = ValidazioneDati.emptyList();

	public Progressivo(String initial, String model) {
		super();

		this.initialValue = initial.toLowerCase();
		this.model = model;
		this.currentValue = initial;

		if (isFilled(model)) {
			custom = true;
			values = Arrays.asList(model.split(PeriodiciConstants.SEPARATORE_NUMERAZIONE));
			for (String v : values)
				lowerValues.add(v.toLowerCase());
			this.currentValue = isFilled(initialValue) ? values.get(lowerValues.indexOf(initialValue)) : "";
		}
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public String getInitialValue() {
		return initialValue;
	}

	public int getInitialValuePosition() {
		if (!custom && isNumeric(initialValue))
			return new Integer(initialValue);

		return lowerValues.indexOf(initialValue) + 1;
	}

	public String getModel() {
		return model;
	}

	public String incrementAndGet() {
		if (!custom && isNumeric(currentValue)) {
			currentValue = String.valueOf(new Integer(currentValue) + 1);
			return currentValue;
		}

		if (!isFilled(currentValue)) {
			currentValue = isFilled(values) ? values.get(0) : "";
			return currentValue;
		}

		int idx = values.indexOf(currentValue);
		if (idx < 0)
			return null;

		int next = idx + 1;
		if (next == values.size())
			next = 0;

		currentValue = values.get(next);

		return currentValue;
	}

	public void restart() {
		if (!custom && isNumeric(currentValue)) {
			currentValue = "0";
			return;
		}

		currentValue = "";
	}

	public boolean isCustom() {
		return custom;
	}

	public List<String> getValues() {
		return values;
	}

}
