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

import it.iccu.sbn.ejb.vo.servizi.serviziweb.ListaServiziVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class SceltaServiziILLForm extends ActionForm {

	private static final long serialVersionUID = -7865367652118028778L;

	private ListaServiziVO listaServizi;

	private String titolo;

	private String segnatura;

	private String utenteCon;

	private String biblioSel;

	private List<ListaServiziVO> listaServILL;

	public ListaServiziVO getListaServizi() {
		return listaServizi;
	}

	public void setListaServizi(ListaServiziVO listaServizi) {
		this.listaServizi = listaServizi;
	}

	public List<ListaServiziVO> getListaServILL() {
		return listaServILL;
	}

	public void setListaServILL(List<ListaServiziVO> listaServILL) {
		this.listaServILL = listaServILL;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}



	public String getBiblioSel() {
		return biblioSel;
	}

	public void setBiblioSel(String biblioSel) {
		this.biblioSel = biblioSel;
	}

	public String getUtenteCon() {
		return utenteCon;
	}

	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}


  /*public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if(this.segnatura != null)
        {
            if (getSegnatura().length() <= 0) {
                errors.add("Segnatura", new ActionMessage(
                        "campo.obbligatorio", "Segnatura"));
            }

        }
        return errors;
    }
*/
}
