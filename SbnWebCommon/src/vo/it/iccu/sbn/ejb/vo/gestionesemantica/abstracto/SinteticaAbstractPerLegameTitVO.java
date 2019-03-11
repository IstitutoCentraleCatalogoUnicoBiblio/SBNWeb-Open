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
package it.iccu.sbn.ejb.vo.gestionesemantica.abstracto;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

public class SinteticaAbstractPerLegameTitVO extends SBNMarcCommonVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -1517805326684050209L;
	private int progr;
	private String descrizione;
	private String livelloAutorita;


	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public SinteticaAbstractPerLegameTitVO() {
		super();
	}

//	public static final Comparator ORDINA_PER_PROGRESSIVO = new Comparator() {
//		public int compare(Object o1, Object o2) {
//			int p1 = ((SinteticaClasseVO)o1).getProgr();
//			int p2 = ((SinteticaClasseVO)o2).getProgr();
//			return p1-p2;
//		}
//	};

	public SinteticaAbstractPerLegameTitVO(int progr, String descrizione) throws Exception {
		this.progr = progr;
		this.descrizione = descrizione;

	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
