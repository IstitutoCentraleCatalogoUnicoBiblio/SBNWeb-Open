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
package it.iccu.sbn.web.actionforms.acquisizioni.fornitori;

import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ListaSuppFornitoriForm extends ActionForm {


	private static final long serialVersionUID = -5592173185154954813L;
	private List<FornitoreVO> listaFornitori;
	private String selectedFornitori;
	private String parametroPassato;
	private int numFornitori;
	private boolean sessione;
	private int numRighe=5;
	private int numPagina=1;
	private int posElemento=0;
	private int totPagine=0;
	private int numRigheOld=5;
	private int posElementoOld=0;
	private int numPaginaOld=1;
	private boolean risultatiPresenti=true;
	private StrutturaCombo ordinaLista;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selectedFornitori="";
 }



	public int getNumFornitori() {
		return numFornitori;
	}

	public void setNumFornitori(int numFornitori) {
		this.numFornitori = numFornitori;
	}

	public String getParametroPassato() {
		return parametroPassato;
	}

	public void setParametroPassato(String parametroPassato) {
		this.parametroPassato = parametroPassato;
	}


	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public List<FornitoreVO> getListaFornitori() {
		return listaFornitori;
	}

	public void setListaFornitori(List<FornitoreVO> listaFornitori) {
		this.listaFornitori = listaFornitori;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPaginaOld() {
		return numPaginaOld;
	}

	public void setNumPaginaOld(int numPaginaOld) {
		this.numPaginaOld = numPaginaOld;
	}

	public int getNumRighe() {
		return numRighe;
	}

	public void setNumRighe(int numRighe) {
		this.numRighe = numRighe;
	}

	public int getNumRigheOld() {
		return numRigheOld;
	}

	public void setNumRigheOld(int numRigheOld) {
		this.numRigheOld = numRigheOld;
	}

	public int getPosElemento() {
		return posElemento;
	}

	public void setPosElemento(int posElemento) {
		this.posElemento = posElemento;
	}

	public int getPosElementoOld() {
		return posElementoOld;
	}

	public void setPosElementoOld(int posElementoOld) {
		this.posElementoOld = posElementoOld;
	}

	public int getTotPagine() {
		return totPagine;
	}

	public void setTotPagine(int totPagine) {
		this.totPagine = totPagine;
	}

	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}

	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}

	public StrutturaCombo getOrdinaLista() {
		return ordinaLista;
	}

	public void setOrdinaLista(StrutturaCombo ordinaLista) {
		this.ordinaLista = ordinaLista;
	}

	public String getSelectedFornitori() {
		return selectedFornitori;
	}

	public void setSelectedFornitori(String selectedFornitori) {
		this.selectedFornitori = selectedFornitori;
	}

}
