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
package it.iccu.sbn.web.actionforms.acquisizioni.offerte;

import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.GestioneBlocchiVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaOfferteForm extends AbstractBibliotecaForm  {


	private static final long serialVersionUID = -5601984695174542191L;
	private List<OffertaFornitoreVO> listaOfferte;
	private List <OffertaFornitoreVO>  offerteVisualizzate;
	private List <OffertaFornitoreVO>  offerteVisualizzateOld;
	private boolean LSRicerca=false;

	private String ordinamentoScelto;

	private String[] selectedOfferte;
	private String parametroPassato;
	private int numOfferte;

	private boolean sessione;
	private int numRighe=5;
	private int numPagina=1;
	private int posElemento=0;
	private int totPagine=0;
	private boolean risultatiPresenti=true;
	private GestioneBlocchiVO[] sequenzablocchi;
	private boolean visibilitaIndietroLS=false;

	// gestione blocchi
	private int progrForm=0;
	private String livelloRicerca;
	private int totBlocchi;
	private int numNotizie;
	private int numPrimo;
	private int numBlocco;
	private int offset;
	private DescrittoreBloccoVO ultimoBlocco;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selectedOfferte=new String[0];
	}





	public int getNumOfferte() {
		return numOfferte;
	}



	public void setNumOfferte(int numOfferte) {
		this.numOfferte = numOfferte;
	}



	public String getParametroPassato() {
		return parametroPassato;
	}



	public void setParametroPassato(String parametroPassato) {
		this.parametroPassato = parametroPassato;
	}



	public String[] getSelectedOfferte() {
		return selectedOfferte;
	}



	public void setSelectedOfferte(String[] selectedOfferte) {
		this.selectedOfferte = selectedOfferte;
	}

	public List<OffertaFornitoreVO> getListaOfferte() {
		return listaOfferte;
	}

	public void setListaOfferte(List<OffertaFornitoreVO> listaOfferte) {
		this.listaOfferte = listaOfferte;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumRighe() {
		return numRighe;
	}

	public void setNumRighe(int numRighe) {
		this.numRighe = numRighe;
	}

	public List<OffertaFornitoreVO> getOfferteVisualizzate() {
		return offerteVisualizzate;
	}

	public void setOfferteVisualizzate(
			List<OffertaFornitoreVO> offerteVisualizzate) {
		this.offerteVisualizzate = offerteVisualizzate;
	}

	public List<OffertaFornitoreVO> getOfferteVisualizzateOld() {
		return offerteVisualizzateOld;
	}

	public void setOfferteVisualizzateOld(
			List<OffertaFornitoreVO> offerteVisualizzateOld) {
		this.offerteVisualizzateOld = offerteVisualizzateOld;
	}

	public int getPosElemento() {
		return posElemento;
	}

	public void setPosElemento(int posElemento) {
		this.posElemento = posElemento;
	}

	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}

	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}

	public GestioneBlocchiVO[] getSequenzablocchi() {
		return sequenzablocchi;
	}

	public void setSequenzablocchi(GestioneBlocchiVO[] sequenzablocchi) {
		this.sequenzablocchi = sequenzablocchi;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public int getTotPagine() {
		return totPagine;
	}

	public void setTotPagine(int totPagine) {
		this.totPagine = totPagine;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public int getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(int numBlocco) {
		this.numBlocco = numBlocco;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getLivelloRicerca() {
		return livelloRicerca;
	}

	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}

	public DescrittoreBloccoVO getUltimoBlocco() {
		return ultimoBlocco;
	}

	public void setUltimoBlocco(DescrittoreBloccoVO ultimoBlocco) {
		this.ultimoBlocco = ultimoBlocco;
	}

	public int getProgrForm() {
		return progrForm;
	}

	public void setProgrForm(int progrForm) {
		this.progrForm = progrForm;
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
