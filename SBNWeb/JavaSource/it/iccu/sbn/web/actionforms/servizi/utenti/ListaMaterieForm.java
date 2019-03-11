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
package it.iccu.sbn.web.actionforms.servizi.utenti;

import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaMaterieForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 5665561616765375457L;

	private boolean sessione = false;

	private UtenteBibliotecaVO anagraficaUtente = new UtenteBibliotecaVO();
	private List<MateriaVO> listaMaterie = new ArrayList<MateriaVO>();

	private Integer indiciSelezionate[];



	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);

//		List<MateriaVO> materie = this.getAnagraficaUtente().getProfessione().getMaterie();
//		if (materie!=null)
//			for (MateriaVO m: materie) {
//				m.setSelezionato("");
//			}
		indiciSelezionate = new Integer[]{};
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public List<MateriaVO> getListaMaterie() {
		return listaMaterie;
	}

	public void setListaMaterie(List<MateriaVO> listaMaterie) {
		this.listaMaterie = listaMaterie;
	}

	public UtenteBibliotecaVO getAnagraficaUtente() {
		return anagraficaUtente;
	}

	public void setAnagraficaUtente(UtenteBibliotecaVO anagraficaUtente) {
		this.anagraficaUtente = anagraficaUtente;
	}

	public Integer[] getIndiciSelezionate() {
		return indiciSelezionate;
	}

	public void setIndiciSelezionate(Integer[] indiciSelezionate) {
		this.indiciSelezionate = indiciSelezionate;
	}

}
