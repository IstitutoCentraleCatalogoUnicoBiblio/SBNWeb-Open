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
package it.iccu.sbn.web.actionforms.servizi.materieinteresse;

import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class ListaMaterieInteresseForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -4949214463418017568L;
	private boolean sessione = false;
	private boolean conferma = false;

	private List<MateriaVO> listaMaterie;
	private String[]             codSelMateria=new String[0];
	private String               codSelMateriaSing="";
	private RicercaMateriaVO     datiRicerca = new RicercaMateriaVO();
	private String[] selectedMatInt;

	private boolean abilitaBlocchi;
	private int totBlocchi;

	/**
	 * @return Returns the totBlocchi.
	 */
	public int getTotBlocchi() {
		return totBlocchi;
	}
	/**
	 * @param totBlocchi The totBlocchi to set.
	 */
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}
	public void setAbilitaBlocchi(boolean b) {
		abilitaBlocchi = b;
	}
	/**
	 * @return Returns the abilitaBlocchi.
	 */
	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String[] getCodSelMateria() {
		return codSelMateria;
	}
	public void setCodSelMateria(String[] codSelMateria) {
		this.codSelMateria = codSelMateria;
	}
	public List<MateriaVO> getListaMaterie() {
		return listaMaterie;
	}
	public void setListaMaterie(List<MateriaVO> listaMaterie) {
		this.listaMaterie = listaMaterie;
	}
	public String getCodSelMateriaSing() {
		return codSelMateriaSing;
	}
	public void setCodSelMateriaSing(String codSelMateriaSing) {
		this.codSelMateriaSing = codSelMateriaSing;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public RicercaMateriaVO getDatiRicerca() {
		return datiRicerca;
	}
	public void setDatiRicerca(RicercaMateriaVO datiRicerca) {
		this.datiRicerca = datiRicerca;
	}
	public String[] getSelectedMatInt() {
		return selectedMatInt;
	}
	public void setSelectedMatInt(String[] selectedMatInt) {
		this.selectedMatInt = selectedMatInt;
	}

}
