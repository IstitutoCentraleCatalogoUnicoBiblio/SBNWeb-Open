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
package it.iccu.sbn.web.actionforms.documentofisico.possessori;

import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;

import org.apache.struts.action.ActionForm;

public class PossessoriDettaglioForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = -9117003929464748460L;
	private PossessoriDettaglioVO possDettVO = new PossessoriDettaglioVO();
    private String appoNome;
    private String appoNota;

	public PossessoriDettaglioForm() {
		super();
	}

	public PossessoriDettaglioVO getPossDettVO() {
		return possDettVO;
	}

	public void setPossDettVO(PossessoriDettaglioVO possDettVO) {
		this.possDettVO = possDettVO;
	}

	public String getAppoNota() {
		return appoNota;
	}

	public void setAppoNota(String appoNota) {
		this.appoNota = appoNota;
	}

	public String getAppoNome() {
		return appoNome;
	}

	public void setAppoNome(String appoNome) {
		this.appoNome = appoNome;
	}
}
