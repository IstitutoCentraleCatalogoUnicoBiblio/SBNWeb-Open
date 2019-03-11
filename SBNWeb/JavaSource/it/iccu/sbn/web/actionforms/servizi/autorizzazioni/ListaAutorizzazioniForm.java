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
package it.iccu.sbn.web.actionforms.servizi.autorizzazioni;

import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaAutorizzazioniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 4263817523757654432L;
	private boolean sessione = false;
	boolean conferma = false;
	private List listaAutorizzazioni=new ArrayList();
	private String[] codSelAut=new String[0];
	private String codSelAutSing="";
	private AutorizzazioneVO autAna = new AutorizzazioneVO("","","","");
	private RicercaAutorizzazioneVO autRicerca = new RicercaAutorizzazioneVO();
	private String[] selectedAutorizzazioni;

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
	public List getListaAutorizzazioni() {
		return listaAutorizzazioni;
	}
	public void setListaAutorizzazioni(List listaAutorizzazioni) {
		this.listaAutorizzazioni = listaAutorizzazioni;
	}
	public String[] getCodSelAut() {
		return codSelAut;
	}
	public void setCodSelAut(String[] codSelAut) {
		this.codSelAut = codSelAut;
	}
	public AutorizzazioneVO getAutAna() {
		return autAna;
	}
	public void setAutAna(AutorizzazioneVO autAna) {
		this.autAna = autAna;
	}
	public String getCodSelAutSing() {
		return codSelAutSing;
	}
	public void setCodSelAutSing(String codSelAutSing) {
		this.codSelAutSing = codSelAutSing;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public RicercaAutorizzazioneVO getAutRicerca() {
		return autRicerca;
	}
	public void setAutRicerca(RicercaAutorizzazioneVO autRicerca) {
		this.autRicerca = autRicerca;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request){
		//this.codSelAut=new String[]{};
		//this.selectedAutorizzazioni=new String[0];
		}
	public String[] getSelectedAutorizzazioni() {
		return selectedAutorizzazioni;
	}
	public void setSelectedAutorizzazioni(String[] selectedAutorizzazioni) {
		this.selectedAutorizzazioni = selectedAutorizzazioni;
	}
}
