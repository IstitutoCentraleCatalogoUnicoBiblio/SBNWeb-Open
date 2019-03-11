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
package it.iccu.sbn.ejb.vo.gestionesemantica.stampe;

import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;

import java.util.List;

public class ElementoStampaDescrittoreVO extends ElementoStampaSogDesVO {

	private static final long serialVersionUID = 9206709549952884104L;

	private String formaNome;
	private String tipoLegame;

	private List<ElementoStampaSoggettoVO> soggettiCollegati;
	private List<ElementoStampaDescrittoreVO> legamiDescrittori;

	private SubReportVO subReportDescrittori;

	private SubReportVO subReportSoggetti;

	public String getFormaNome() {
		return formaNome;
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = trimAndSet(formaNome);
	}

	public List<ElementoStampaDescrittoreVO> getLegamiDescrittori() {
		return legamiDescrittori;
	}

	public void setLegamiDescrittori(
			List<ElementoStampaDescrittoreVO> legamiDescrittori) {
		this.legamiDescrittori = legamiDescrittori;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public void setSoggettiCollegati(List<ElementoStampaSoggettoVO> soggetti) {
		this.soggettiCollegati = soggetti;
	}

	public List<ElementoStampaSoggettoVO> getSoggettiCollegati() {
		return soggettiCollegati;
	}

	public void setSubReportDescrittori(SubReportVO subDes) {
		this.subReportDescrittori = subDes;
	}

	public void setSubReportSoggetti(SubReportVO subSogg) {
		this.subReportSoggetti = subSogg;
	}

	public SubReportVO getSubReportDescrittori() {
		return subReportDescrittori;
	}

	public SubReportVO getSubReportSoggetti() {
		return subReportSoggetti;
	}

}
