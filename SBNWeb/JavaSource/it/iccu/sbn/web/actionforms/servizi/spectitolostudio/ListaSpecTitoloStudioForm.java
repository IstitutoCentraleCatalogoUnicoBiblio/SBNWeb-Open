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
package it.iccu.sbn.web.actionforms.servizi.spectitolostudio;


import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;


public class ListaSpecTitoloStudioForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -762261240927904597L;
	private boolean sessione = false;
	private boolean conferma = false;
	private RicercaTitoloStudioVO datiRicerca = new RicercaTitoloStudioVO();
	private SpecTitoloStudioVO  anaSpecialita = new SpecTitoloStudioVO();
	private String[] codSelSpecialita=new String[0];
	private String codSelSpecialitaSing="";
	private List listaSpecialita=new ArrayList();
	private String[] selectedSpecialita;

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

	public SpecTitoloStudioVO getAnaSpecialita() {
		return anaSpecialita;
	}
	public void setAnaSpecialita(SpecTitoloStudioVO anaSpecialita) {
		this.anaSpecialita = anaSpecialita;
	}
	public String[] getCodSelSpecialita() {
		return codSelSpecialita;
	}
	public void setCodSelSpecialita(String[] codSelSpecialita) {
		this.codSelSpecialita = codSelSpecialita;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public List getListaSpecialita() {
		return listaSpecialita;
	}
	public void setListaSpecialita(List listaSpecialita) {
		this.listaSpecialita = listaSpecialita;
	}
	public String getCodSelSpecialitaSing() {
		return codSelSpecialitaSing;
	}
	public void setCodSelSpecialitaSing(String codSelSpecialitaSing) {
		this.codSelSpecialitaSing = codSelSpecialitaSing;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public RicercaTitoloStudioVO getDatiRicerca() {
		return datiRicerca;
	}
	public void setDatiRicerca(RicercaTitoloStudioVO datiRicerca) {
		this.datiRicerca = datiRicerca;
	}
	public String[] getSelectedSpecialita() {
		return selectedSpecialita;
	}
	public void setSelectedSpecialita(String[] selectedSpecialita) {
		this.selectedSpecialita = selectedSpecialita;
	}

}
