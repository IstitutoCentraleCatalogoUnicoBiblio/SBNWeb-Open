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
package it.iccu.sbn.web.actionforms.acquisizioni.buoniordine;

import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaBuonoOrdineForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 873841935487898934L;

	private List<BuoniOrdineVO> listaBuoniOrdine;
	// private List <BuoniOrdineVO> buoniVisualizzati;
	// private List <BuoniOrdineVO> buoniVisualizzatiOld;

	private String[] selectedBuoni;
	private String parametroPassato;
	private int numBuoni;
	private String ordinamentoScelto;

	private boolean sessione;

	/*
	 * private int numRighe=5; private int numPagina=1; private int
	 * posElemento=0; private int totPagine=0; private GestioneBlocchiVO[]
	 * sequenzablocchi;
	 */

	// gestione blocchi
	private int progrForm = 0;
	private String livelloRicerca;
	// private int totBlocchi;
	private int numNotizie;
	// private int numBlocco;
	private DescrittoreBloccoVO ultimoBlocco;

	private boolean risultatiPresenti = true;
	private boolean visibilitaIndietroLS = false;
	private boolean LSRicerca = false;

	private ListaSuppBuoniOrdineVO parametri;

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selectedBuoni = new String[0];
	}

	public String getParametroPassato() {
		return parametroPassato;
	}

	public void setParametroPassato(String parametroPassato) {
		this.parametroPassato = parametroPassato;
	}

	public String[] getSelectedBuoni() {
		return selectedBuoni;
	}

	public void setSelectedBuoni(String[] selectedBuoni) {
		this.selectedBuoni = selectedBuoni;
	}

	public int getNumBuoni() {
		return numBuoni;
	}

	public void setNumBuoni(int numBuoni) {
		this.numBuoni = numBuoni;
	}

	public void prova() {
		this.setNumBuoni(99);
	}

	public List<BuoniOrdineVO> getListaBuoniOrdine() {
		return listaBuoniOrdine;
	}

	public void setListaBuoniOrdine(List<BuoniOrdineVO> listaBuoniOrdine) {
		this.listaBuoniOrdine = listaBuoniOrdine;
	}

	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}

	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
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

	public void setParametri(ListaSuppBuoniOrdineVO parametri) {
		this.parametri = parametri;

	}

	public ListaSuppBuoniOrdineVO getParametri() {
		return parametri;
	}

}
