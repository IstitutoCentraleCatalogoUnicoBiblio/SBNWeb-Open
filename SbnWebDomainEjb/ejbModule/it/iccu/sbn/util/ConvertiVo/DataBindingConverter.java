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
package it.iccu.sbn.util.ConvertiVo;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.polo.orm.Tb_base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;

public abstract class DataBindingConverter extends SerializableVO {

	private static final long serialVersionUID = -7595700368395461084L;

	protected Character getFlag(String value, boolean nullable) {
		if (isFilled(value))
			return value.charAt(0);

		return nullable ? null : Character.MIN_VALUE;
	}

	protected BigDecimal getBigDecimal(Number input, boolean nullable) {
		if (!isFilled(input) )
			if (nullable)
				return null;
			else
				return BigDecimal.ZERO;

		return new BigDecimal(input.doubleValue());

	}

	protected static void fillBaseHibernate(Tb_base hVO, BaseVO webVO) {
		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_ins(webVO.getUteIns());

		hVO.setTs_var(webVO.getTsVar());
		hVO.setUte_var(webVO.getUteVar());

		hVO.setFl_canc(!isFilled(webVO.getFlCanc()) ? 'N' : webVO.getFlCanc().charAt(0));
	}

	protected static void fillBaseWeb(Tb_base hVO, BaseVO webVO) {
		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());
		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()) );
	}

	public final boolean assignEntityID(Serializable entity, String idName, Number value) {
		try {
			Field field = entity.getClass().getDeclaredField(idName);
			field.setAccessible(true);
			field.set(entity, value);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
