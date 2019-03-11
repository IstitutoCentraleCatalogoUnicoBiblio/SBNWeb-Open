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
package it.iccu.sbn.web.actionforms.acquisizioni.sezioni;


import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class ListaSezioniForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 8211350089887916333L;
	private List<SezioneVO> listaSezioni;
	private String[] selectedSezioni;
	private String radioSel;
	private boolean sessione;
	private boolean risultatiPresenti=true;
	private int numSezioni;
	private ListaSuppSezioneVO sifSezioni;

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
		   this.selectedSezioni=new String[0];
		   this.setRisultatiPresenti(true);
	}

	public List<SezioneVO> getListaSezioni() {
		return listaSezioni;
	}

	public void setListaSezioni(List<SezioneVO> listaSezioni) {
		this.listaSezioni = listaSezioni;
	}

	public int getNumSezioni() {
		return numSezioni;
	}

	public void setNumSezioni(int numSezioni) {
		this.numSezioni = numSezioni;
	}

	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}

	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}

	public String[] getSelectedSezioni() {
		return selectedSezioni;
	}

	public void setSelectedSezioni(String[] selectedSezioni) {
		this.selectedSezioni = selectedSezioni;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getRadioSel() {
		return radioSel;
	}

	public void setRadioSel(String radioSel) {
		this.radioSel = radioSel;
	}

	public DescrittoreBloccoVO getUltimoBlocco() {
		return ultimoBlocco;
	}

	public void setUltimoBlocco(DescrittoreBloccoVO ultimoBlocco) {
		this.ultimoBlocco = ultimoBlocco;
	}

	public ListaSuppSezioneVO getSifSezioni() {
		return sifSezioni;
	}

	public void setSifSezioni(ListaSuppSezioneVO sifSezioni) {
		this.sifSezioni = sifSezioni;
	}


}
