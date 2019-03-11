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
package it.iccu.sbn.web.actionforms.servizi.serviziweb;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class SelezioneBibliotecaForm extends ActionForm {

	private static final long serialVersionUID = 7690963143513538555L;
	private BibliotecaVO biblio = new BibliotecaVO();
	private BibliotecaVO biblioAuto = new BibliotecaVO();
	private List listaBiblio;
	private List listaBiblioAuto;
	private String utenteCon;
	private Integer id_ute_bib;
	private String insRichOpac;
	private String ambiente;

	public String getUtenteCon() {
		return utenteCon;
	}

	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}

	public BibliotecaVO getBiblio() {
		return biblio;
	}

	public void setBiblio(BibliotecaVO biblio) {
		this.biblio = biblio;
	}

    public List getListaBiblio() {
        return listaBiblio;
    }

    public void setListaBiblio(List listaBiblio) {
        this.listaBiblio = listaBiblio;
    }

    public List  getListaBiblioAuto() {
        return listaBiblioAuto;
    }

    public void setListaBiblioAuto(List listaBiblioAuto) {
        this.listaBiblioAuto = listaBiblioAuto;
    }

	public void setBiblioAuto(BibliotecaVO biblioAuto) {
		this.biblioAuto = biblioAuto;
	}

	public BibliotecaVO getBiblioAuto() {
		return biblioAuto;
	}

	public void setId_ute_bib(Integer id_ute_bib) {
		this.id_ute_bib = id_ute_bib;
	}

	public Integer getId_ute_bib() {
		return id_ute_bib;
	}

	public void setInsRichOpac(String insRichOpac) {
		this.insRichOpac = insRichOpac;
	}

	public String getInsRichOpac() {
		return insRichOpac;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getAmbiente() {
		return ambiente;
	}


}
