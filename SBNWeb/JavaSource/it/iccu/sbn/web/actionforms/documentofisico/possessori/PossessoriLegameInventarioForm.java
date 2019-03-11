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

import org.apache.struts.action.ActionForm;

public class PossessoriLegameInventarioForm extends ActionForm {


    /**
	 * 
	 */
	private static final long serialVersionUID = -4738844535595733897L;
	private String notaAlLegame ;
    private String descBib ;
    private String codeBib ;

    private String pidOrigine;
    private String nomeOrigine;

    private String codeLegame;
    private String prov;
    private String codeSerie ;
    private String codeInv ;

	public PossessoriLegameInventarioForm() {
		super();
	}

	public String getNotaAlLegame() {
		return notaAlLegame;
	}

	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}

	public String getPidOrigine() {
		return pidOrigine;
	}

	public void setPidOrigine(String pidOrigine) {
		this.pidOrigine = pidOrigine;
	}

	public String getDescBib() {
		return descBib;
	}

	public void setDescBib(String descBib) {
		this.descBib = descBib;
	}

	public String getCodeBib() {
		return codeBib;
	}

	public void setCodeBib(String codeBib) {
		this.codeBib = codeBib;
	}

	public String getNomeOrigine() {
		return nomeOrigine;
	}

	public void setNomeOrigine(String nomeOrigine) {
		this.nomeOrigine = nomeOrigine;
	}

	public String getCodeLegame() {
		return codeLegame;
	}

	public void setCodeLegame(String codeLegame) {
		this.codeLegame = codeLegame;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCodeSerie() {
		return codeSerie;
	}

	public void setCodeSerie(String codeSerie) {
		this.codeSerie = codeSerie;
	}

	public String getCodeInv() {
		return codeInv;
	}

	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}
}
