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
package it.iccu.sbn.web.actionforms.acquisizioni.documenti;

import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaDocForm extends AbstractBibliotecaForm {



	private static final long serialVersionUID = -2426839412693653247L;
	private List<DocumentoVO> listaDocumenti;
	private boolean LSRicerca=false;

	private String[] selectedDocumenti;
	private String parametroPassato;
	private int numDocumenti;
	private String statoSuggerimento;
	List listaStatoSuggerimento;
	private String ordinamentoScelto;

	private boolean sessione;
/*	private int numRighe=5;
	private int numPagina=1;
	private int posElemento=0;
	private int totPagine=0;
	private GestioneBlocchiVO[] sequenzablocchi;
*/

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

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selectedDocumenti=new String[0];
	}

	public List getListaStatoSuggerimento() {
		return listaStatoSuggerimento;
	}

	public void setListaStatoSuggerimento(List listaStatoSuggerimento) {
		this.listaStatoSuggerimento = listaStatoSuggerimento;
	}

	public int getNumDocumenti() {
		return numDocumenti;
	}

	public void setNumDocumenti(int numDocumenti) {
		this.numDocumenti = numDocumenti;
	}

	public String getParametroPassato() {
		return parametroPassato;
	}

	public void setParametroPassato(String parametroPassato) {
		this.parametroPassato = parametroPassato;
	}

	public String[] getSelectedDocumenti() {
		return selectedDocumenti;
	}

	public void setSelectedDocumenti(String[] selectedDocumenti) {
		this.selectedDocumenti = selectedDocumenti;
	}

	public String getStatoSuggerimento() {
		return statoSuggerimento;
	}

	public void setStatoSuggerimento(String statoSuggerimento) {
		this.statoSuggerimento = statoSuggerimento;
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


	public void setListaDocumenti(List<DocumentoVO> listaDocumenti) {
		this.listaDocumenti = listaDocumenti;
	}


	public List<DocumentoVO> getListaDocumenti() {
		return listaDocumenti;
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

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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
