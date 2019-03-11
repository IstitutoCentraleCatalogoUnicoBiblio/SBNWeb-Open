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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Comparator;

public class SinteticaClassePerLegameTitVO extends SerializableVO {

	private static final long serialVersionUID = 351306699953641427L;
	private int progr;
	private String notazione;
	private String descrizione;

	public static final Comparator<SinteticaClassePerLegameTitVO> ORDINA_PER_PROGRESSIVO = new Comparator<SinteticaClassePerLegameTitVO>() {
		public int compare(SinteticaClassePerLegameTitVO o1,
				SinteticaClassePerLegameTitVO o2) {
			int p1 = o1.getProgr();
			int p2 = o2.getProgr();
			return p1 - p2;
		}
	};

	public SinteticaClassePerLegameTitVO() {
		super();
	}

	public SinteticaClassePerLegameTitVO(int progr, String notazione,
			String descrizione) throws Exception {

		this.progr = progr;
		this.notazione = notazione;
		this.descrizione = descrizione;

	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getNotazione() {
		return notazione;
	}

	public void setNotazione(String notazione) {
		this.notazione = notazione;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

}
