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
package it.iccu.sbn.ejb.vo.periodici;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.util.periodici.PeriodiciUtil;

public class KardexPeriodicoDecorator extends KardexPeriodicoVO {


	private static final long serialVersionUID = -4378791291660739391L;
	private final KardexPeriodicoVO original;
	private DescrittoreBloccoVO blocco;
	private long[] timestamps;
	private long timeFrom = 0;
	private long timeTo = Long.MAX_VALUE;

	private int[] ids;

	public KardexPeriodicoDecorator(KardexPeriodicoVO kardex) {
		super(kardex);
		original = kardex;
		//copyCommonProperties(this, kardex);
	}

	public DescrittoreBloccoVO getBlocco() {
		return blocco;
	}

	public void setBlocco(DescrittoreBloccoVO blocco) {
		this.blocco = blocco;
	}

	@Override
	public boolean isEmpty() {
		return !DescrittoreBloccoVO.isFilled(blocco);
	}

	public long[] getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(long[] timestamps) {
		this.timestamps = timestamps;
	}

	public long getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(long timeFrom) {
		this.timeFrom = timeFrom;
	}

	public long getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(long timeTo) {
		this.timeTo = timeTo;
	}

	public String getIntervalloAnnate() {
		if (!isFilled(rangeAnnoPubb))
			return "";

		int first = ValidazioneDati.first(rangeAnnoPubb);
		//almaviva5_20111028 #4705
		if (first == -1)
			return "";

		return PeriodiciUtil.formattaAnnateFascicolo(rangeAnnoPubb);
	}

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public KardexPeriodicoVO getOriginal() {
		return original;
	}

}
