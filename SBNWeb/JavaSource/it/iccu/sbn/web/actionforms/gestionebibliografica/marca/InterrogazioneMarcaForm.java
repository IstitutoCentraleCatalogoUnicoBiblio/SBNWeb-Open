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
package it.iccu.sbn.web.actionforms.gestionebibliografica.marca;

import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.InterrogazioneMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class InterrogazioneMarcaForm extends AbstractBibliotecaForm {

	
	private static final long serialVersionUID = -5539070383634178767L;
		private InterrogazioneMarcaGeneraleVO interrGener = new InterrogazioneMarcaGeneraleVO();
		public String livRicerca;
		public String provenienza;
		AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();

		public String creaMar = "SI";
		public String creaMarLoc = "SI";


		public InterrogazioneMarcaGeneraleVO getInterrGener() {
			return interrGener;
		}

		public void setInterrGener(InterrogazioneMarcaGeneraleVO interrGener) {
			this.interrGener = interrGener;
		}

		public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
			return areaDatiLegameTitoloVO;
		}

		public void setAreaDatiLegameTitoloVO(
				AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
			this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
		}

		public String getProvenienza() {
			return provenienza;
		}

		public void setProvenienza(String provenienza) {
			this.provenienza = provenienza;
		}

		@Override
		public void reset(ActionMapping arg0, HttpServletRequest arg1) {
//			this.interrGener.setRicLocale(false);
//			this.interrGener.setRicIndice(false);
		}

		public String getLivRicerca() {
			return livRicerca;
		}

		public void setLivRicerca(String livRicerca) {
			this.livRicerca = livRicerca;
		}

		public String getCreaMar() {
			return creaMar;
		}

		public void setCreaMar(String creaMar) {
			this.creaMar = creaMar;
		}

		public String getCreaMarLoc() {
			return creaMarLoc;
		}

		public void setCreaMarLoc(String creaMarLoc) {
			this.creaMarLoc = creaMarLoc;
		}

}

