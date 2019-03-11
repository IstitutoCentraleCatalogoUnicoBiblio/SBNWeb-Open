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
package it.iccu.sbn.web.actionforms.acquisizioni.fatture;


import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class ListaFattureForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -5937881287958851134L;
	private List<FatturaVO> listaFatture;
	private String[] selectedFatture;
	private String radioSel;
	private boolean sessione;
	private boolean risultatiPresenti=true;
	private int numFatture;
	private ListaSuppFatturaVO sifFatture;
	private boolean noteCredito=false;

	//private int totBlocchi;
//	private int numBlocco;
	private DescrittoreBloccoVO ultimoBlocco;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		   this.selectedFatture=new String[0];
		   this.setRisultatiPresenti(true);
	}

	public List<FatturaVO> getListaFatture() {
		return listaFatture;
	}

	public void setListaFatture(List<FatturaVO> listaFatture) {
		this.listaFatture = listaFatture;
	}

	public int getNumFatture() {
		return numFatture;
	}

	public void setNumFatture(int numFatture) {
		this.numFatture = numFatture;
	}

	public String getRadioSel() {
		return radioSel;
	}

	public void setRadioSel(String radioSel) {
		this.radioSel = radioSel;
	}

	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}

	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}

	public String[] getSelectedFatture() {
		return selectedFatture;
	}

	public void setSelectedFatture(String[] selectedFatture) {
		this.selectedFatture = selectedFatture;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public ListaSuppFatturaVO getSifFatture() {
		return sifFatture;
	}

	public void setSifFatture(ListaSuppFatturaVO sifFatture) {
		this.sifFatture = sifFatture;
	}

	public DescrittoreBloccoVO getUltimoBlocco() {
		return ultimoBlocco;
	}

	public void setUltimoBlocco(DescrittoreBloccoVO ultimoBlocco) {
		this.ultimoBlocco = ultimoBlocco;
	}

	public boolean isNoteCredito() {
		return noteCredito;
	}

	public void setNoteCredito(boolean noteCredito) {
		this.noteCredito = noteCredito;
	}






}
