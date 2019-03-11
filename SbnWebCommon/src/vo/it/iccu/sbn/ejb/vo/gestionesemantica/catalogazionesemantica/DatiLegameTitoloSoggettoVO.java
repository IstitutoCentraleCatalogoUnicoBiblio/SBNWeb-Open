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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;


public class DatiLegameTitoloSoggettoVO extends DatiLegameTitoloAuthSemanticaVO {

	private static final long serialVersionUID = 368895049823056481L;

	public class LegameTitoloSoggettoVO extends LegameTitoloAuthSemanticaVO {

		private static final long serialVersionUID = 4705156817855980776L;
		private String cid;

		public String getCid() {
			return cid;
		}

		public void setCid(String cid) {
			this.cid = cid;
		}

		public LegameTitoloSoggettoVO() {
			super();
		}

		public LegameTitoloSoggettoVO(String cid, String notaLegame) {
			super();
			this.cid = cid;
			this.notaLegame = notaLegame;
		}

		public String toString() {
			return "[cid: " + cid + " notaLegame: " +  notaLegame + "]";
		}

		@Override
		public SbnAuthority getAuthority() {
			return SbnAuthority.SO;
		}

	}

	private boolean importa = false;


	public boolean isImporta() {
		return importa;
	}

	public void setImporta(boolean importa) {
		this.importa = importa;
	}

}
