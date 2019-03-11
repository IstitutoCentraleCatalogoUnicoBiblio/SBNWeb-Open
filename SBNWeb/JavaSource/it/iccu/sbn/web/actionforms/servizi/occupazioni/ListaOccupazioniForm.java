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
package it.iccu.sbn.web.actionforms.servizi.occupazioni;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class ListaOccupazioniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -4895997489309671825L;
	private boolean sessione = false;
	boolean conferma = false;
	private List listaOccupazioni;
	private String[] codSelOccup=new String[0];;
	private String codSelOccupSing="";
	private RicercaOccupazioneVO anaOccup = new RicercaOccupazioneVO();
	private String[] selectedOccup;

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

	public RicercaOccupazioneVO getAnaOccup() {
		return anaOccup;
	}

	public void setAnaOccup(RicercaOccupazioneVO anaOccup) {
		this.anaOccup = anaOccup;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public List getListaOccupazioni() {
		return listaOccupazioni;
	}

	public void setListaOccupazioni(List listaOccupazioni) {
		this.listaOccupazioni = listaOccupazioni;
	}

	public String[] getCodSelOccup() {
		return codSelOccup;
	}

	public void setCodSelOccup(String[] codSelOccup) {
		this.codSelOccup = codSelOccup;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getCodSelOccupSing() {
		return codSelOccupSing;
	}

	public void setCodSelOccupSing(String codSelOccupSing) {
		this.codSelOccupSing = codSelOccupSing;
	}
	public String[] getSelectedOccup() {
		return selectedOccup;
	}
	public void setSelectedOccup(String[] selectedOccup) {
		this.selectedOccup = selectedOccup;
	}

}
