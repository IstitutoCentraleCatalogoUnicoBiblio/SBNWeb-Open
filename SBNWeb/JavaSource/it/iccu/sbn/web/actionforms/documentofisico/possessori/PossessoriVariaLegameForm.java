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
//	SBNWeb - Rifacimento ClientServer
//	FORM sintetica autori
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.documentofisico.possessori;

import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;

import org.apache.struts.action.ActionForm;

public class PossessoriVariaLegameForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2631162530229191908L;
	private PossessoriDettaglioVO possDettVO = new PossessoriDettaglioVO();
    private String notaAlLegame ;
    private String descIniziale ;
    private String tipoCollegamento ;
    private String pidLegame ;

	public PossessoriVariaLegameForm() {
		super();
	}

	public PossessoriDettaglioVO getPossDettVO() {
		return possDettVO;
	}

	public void setPossDettVO(PossessoriDettaglioVO possDettVO) {
		this.possDettVO = possDettVO;
	}

	public String getNotaAlLegame() {
		return notaAlLegame;
	}

	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}

	public String getTipoCollegamento() {
		return tipoCollegamento;
	}

	public void setTipoCollegamento(String tipoCollegamento) {
		this.tipoCollegamento = tipoCollegamento;
	}

	public String getDescIniziale() {
		return descIniziale;
	}

	public void setDescIniziale(String descIniziale) {
		this.descIniziale = descIniziale;
	}

	public String getPidLegame() {
		return pidLegame;
	}

	public void setPidLegame(String pidLegame) {
		this.pidLegame = pidLegame;
	}
}
