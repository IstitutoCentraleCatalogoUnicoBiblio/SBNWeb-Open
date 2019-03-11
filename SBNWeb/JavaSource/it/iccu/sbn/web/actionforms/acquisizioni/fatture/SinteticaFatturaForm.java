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
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaFatturaForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -539694719073755593L;
	private List<FatturaVO> listaFatture;
	private String[] selectedFatture;
	private String parametroPassato;
	private int numFatture;
	private boolean LSRicerca=false;

	private boolean sessione;
	private boolean risultatiPresenti=true;
	private boolean visibilitaIndietroLS=false;
	private boolean abilitaEsamina=true;
	private String ordinamentoScelto;

	// gestione blocchi
	private int progrForm=0;
	private String livelloRicerca;
	//private int totBlocchi;
	private int numNotizie;
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
	}


	public int getNumFatture() {
		return numFatture;
	}

	public void setNumFatture(int numFatture) {
		this.numFatture = numFatture;
	}

	public String getParametroPassato() {
		return parametroPassato;
	}

	public void setParametroPassato(String parametroPassato) {
		this.parametroPassato = parametroPassato;
	}

	public String[] getSelectedFatture() {
		return selectedFatture;
	}

	public void setSelectedFatture(String[] selectedFatture) {
		this.selectedFatture = selectedFatture;
	}
	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}
	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}
	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}
	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public List<FatturaVO> getListaFatture() {
		return listaFatture;
	}
	public void setListaFatture(List<FatturaVO> listaFatture) {
		this.listaFatture = listaFatture;
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
	public boolean isAbilitaEsamina() {
		return abilitaEsamina;
	}
	public void setAbilitaEsamina(boolean abilitaEsamina) {
		this.abilitaEsamina = abilitaEsamina;
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
