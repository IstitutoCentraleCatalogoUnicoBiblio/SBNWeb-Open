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


public class DatiLegameTitoloTermineVO extends DatiLegameTitoloAuthSemanticaVO {

	private static final long serialVersionUID = 8887855856691942167L;

	public class LegameTitoloTermineVO extends LegameTitoloAuthSemanticaVO {

		private static final long serialVersionUID = 2736995780366652325L;
		private String did;

		public String getDid() {
			return did;
		}

		public void setDid(String did) {
			this.did = did;
		}

		@Override
		public SbnAuthority getAuthority() {
			return SbnAuthority.TH;
		}

	}

}
