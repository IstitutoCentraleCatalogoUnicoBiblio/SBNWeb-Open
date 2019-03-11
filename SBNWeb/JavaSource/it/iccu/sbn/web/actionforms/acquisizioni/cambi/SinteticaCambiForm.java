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
package it.iccu.sbn.web.actionforms.acquisizioni.cambi;

import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaCambiForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 3523490862603946818L;
	private List<CambioVO> listaCambi;
	private String[] selectedCambi;
	private String parametroPassato;
	private int numCambi;
	private String numCambi2;
	private String valuta;
	private boolean sessione;
	List listaValuta;
	private boolean LSRicerca=false;
	private String ordinamentoScelto;

	private String action;
	private boolean risultatiPresenti=true;
	private boolean visibilitaIndietroLS=false;

	// gestione blocchi
	private int progrForm=0;
	private String livelloRicerca;
	//private int totBlocchi;
	private int numNotizie;
//	private int numBlocco;
	private DescrittoreBloccoVO ultimoBlocco;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		//this.setSelectedCambi(new String[0]);
		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}



	public void reset(ActionMapping mapping, HttpServletRequest request) {
			this.selectedCambi=new String[0];
	 }


	public List getListaValuta() {
		return listaValuta;
	}


	public void setListaValuta(List listaValuta) {
		this.listaValuta = listaValuta;
	}


	public int getNumCambi() {
		return numCambi;
	}


	public void setNumCambi(int numCambi) {
		this.numCambi = numCambi;
	}


	public String getParametroPassato() {
		return parametroPassato;
	}


	public void setParametroPassato(String parametroPassato) {
		this.parametroPassato = parametroPassato;
	}


	public String[] getSelectedCambi() {
		return selectedCambi;
	}


	public void setSelectedCambi(String[] selectedCambi) {
		this.selectedCambi = selectedCambi;
	}


	public String getValuta() {
		return valuta;
	}


	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}


	public String getNumCambi2() {
		return numCambi2;
	}


	public void setNumCambi2(String numCambi2) {
		this.numCambi2 = numCambi2;
	}


	public List<CambioVO> getListaCambi() {
		return listaCambi;
	}



	public void setListaCambi(List<CambioVO> listaCambi) {
		this.listaCambi = listaCambi;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}





	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}



	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}



	public String getLivelloRicerca() {
		return livelloRicerca;
	}



	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}



	public int getNumNotizie() {
		return numNotizie;
	}



	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}



	public int getProgrForm() {
		return progrForm;
	}



	public void setProgrForm(int progrForm) {
		this.progrForm = progrForm;
	}



	public DescrittoreBloccoVO getUltimoBlocco() {
		return ultimoBlocco;
	}



	public void setUltimoBlocco(DescrittoreBloccoVO ultimoBlocco) {
		this.ultimoBlocco = ultimoBlocco;
	}



	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}



	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}



	public boolean isLSRicerca() {
		return LSRicerca;
	}



	public void setLSRicerca(boolean ricerca) {
		LSRicerca = ricerca;
	}



	public String getOrdinamentoScelto() {
		return ordinamentoScelto;
	}



	public void setOrdinamentoScelto(String ordinamentoScelto) {
		this.ordinamentoScelto = ordinamentoScelto;
	}

}
