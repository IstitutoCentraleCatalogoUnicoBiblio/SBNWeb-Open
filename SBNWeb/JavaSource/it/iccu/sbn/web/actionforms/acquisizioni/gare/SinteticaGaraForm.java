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
package it.iccu.sbn.web.actionforms.acquisizioni.gare;

import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaGaraForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -7213559166912528119L;
	private List<GaraVO> listaRichiesteOfferta;
	private List <GaraVO>  richiesteVisualizzate;
	private List <GaraVO>  richiesteVisualizzateOld;
	private boolean LSRicerca=false;
	private String ordinamentoScelto;


	private String[] selectedRichieste;
	private String parametroPassato;
	private int numRichieste;

	private String statoRichiestaOfferta;
	private List listaStatoRichiestaOfferta;

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
		this.selectedRichieste=new String[0];
	}

	public List<GaraVO> getListaRichiesteOfferta() {
		return listaRichiesteOfferta;
	}


	public void setListaRichiesteOfferta(List<GaraVO> listaRichiesteOfferta) {
		this.listaRichiesteOfferta = listaRichiesteOfferta;
	}


	public List getListaStatoRichiestaOfferta() {
		return listaStatoRichiestaOfferta;
	}


	public void setListaStatoRichiestaOfferta(List listaStatoRichiestaOfferta) {
		this.listaStatoRichiestaOfferta = listaStatoRichiestaOfferta;
	}




	public int getNumRichieste() {
		return numRichieste;
	}


	public void setNumRichieste(int numRichieste) {
		this.numRichieste = numRichieste;
	}




	public String getParametroPassato() {
		return parametroPassato;
	}


	public void setParametroPassato(String parametroPassato) {
		this.parametroPassato = parametroPassato;
	}




	public List<GaraVO> getRichiesteVisualizzate() {
		return richiesteVisualizzate;
	}


	public void setRichiesteVisualizzate(List<GaraVO> richiesteVisualizzate) {
		this.richiesteVisualizzate = richiesteVisualizzate;
	}


	public List<GaraVO> getRichiesteVisualizzateOld() {
		return richiesteVisualizzateOld;
	}


	public void setRichiesteVisualizzateOld(
			List<GaraVO> richiesteVisualizzateOld) {
		this.richiesteVisualizzateOld = richiesteVisualizzateOld;
	}


	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}


	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}


	public String[] getSelectedRichieste() {
		return selectedRichieste;
	}


	public void setSelectedRichieste(String[] selectedRichieste) {
		this.selectedRichieste = selectedRichieste;
	}




	public boolean isSessione() {
		return sessione;
	}


	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}


	public String getStatoRichiestaOfferta() {
		return statoRichiestaOfferta;
	}


	public void setStatoRichiestaOfferta(String statoRichiestaOfferta) {
		this.statoRichiestaOfferta = statoRichiestaOfferta;
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



}
