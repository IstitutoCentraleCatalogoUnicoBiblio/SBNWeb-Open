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


public class DatiLegameTitoloClasseVO extends DatiLegameTitoloAuthSemanticaVO {

	private static final long serialVersionUID = 5925176691580419605L;

	public class LegameTitoloClasseVO extends LegameTitoloAuthSemanticaVO {

		private static final long serialVersionUID = -4994904915132802709L;
		private String codSistemaClassificazione;
		private String identificativoClasse;

		public LegameTitoloClasseVO() {
			super();
		}

		public LegameTitoloClasseVO(String codSistemaClassificazione,
				String identificativoClasse, String notaLegame) {
			super();
			this.codSistemaClassificazione = codSistemaClassificazione;
			this.identificativoClasse = identificativoClasse;
			this.notaLegame = notaLegame;
		}

		public String getCodSistemaClassificazione() {
			return codSistemaClassificazione;
		}

		public void setCodSistemaClassificazione(String codSistemaClassificazione) {
			this.codSistemaClassificazione = codSistemaClassificazione;
		}

		public String getIdentificativoClasse() {
			return identificativoClasse;
		}

		public void setIdentificativoClasse(String identificativoClasse) {
			this.identificativoClasse = identificativoClasse;
		}

		@Override
		public SbnAuthority getAuthority() {
			return SbnAuthority.CL;
		}

	}

}
