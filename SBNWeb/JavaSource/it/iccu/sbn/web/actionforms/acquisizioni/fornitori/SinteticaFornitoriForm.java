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
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaFornitoriForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -7334231815698729696L;
	private List<FornitoreVO> listaFornitori;
	private String[] selectedFornitori;
	private String parametroPassato;
	private int numFornitori;
	private boolean sessione;
	private boolean cercaInPolo=false;
	private boolean LSRicerca=false;
	private String ordinamentoScelto;


	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Nuovi campi per la gestione degli editori
	private String editore;
	private String creazLegameTitEdit;
	private String gestLegameTitEdit;
	private String cartiglioEditore;
	private String bid;
	private String descr;


/*	private int numRighe=5;
	private int numPagina=1;
	private int posElemento=0;
	private int totPagine=0;
	private int numRigheOld=5;
	private int posElementoOld=0;
	private int numPaginaOld=1;
*/

	// gestione blocchi
	private int progrForm=0;
	private String livelloRicerca;
	//private int totBlocchi;
	private int numNotizie;
//	private int numBlocco;
	private DescrittoreBloccoVO ultimoBlocco;


	private boolean risultatiPresenti=true;
	private StrutturaCombo ordinaLista;
	private boolean visibilitaIndietroLS=false;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selectedFornitori=new String[0];
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

	public String[] getSelectedFornitori() {
		return selectedFornitori;
	}

	public void setSelectedFornitori(String[] selectedFornitori) {
		this.selectedFornitori = selectedFornitori;
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

	public boolean isCercaInPolo() {
		return cercaInPolo;
	}

	public void setCercaInPolo(boolean cercaInPolo) {
		this.cercaInPolo = cercaInPolo;
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

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getCreazLegameTitEdit() {
		return creazLegameTitEdit;
	}

	public void setCreazLegameTitEdit(String creazLegameTitEdit) {
		this.creazLegameTitEdit = creazLegameTitEdit;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getGestLegameTitEdit() {
		return gestLegameTitEdit;
	}

	public void setGestLegameTitEdit(String gestLegameTitEdit) {
		this.gestLegameTitEdit = gestLegameTitEdit;
	}

	public String getCartiglioEditore() {
		return cartiglioEditore;
	}

	public void setCartiglioEditore(String cartiglioEditore) {
		this.cartiglioEditore = cartiglioEditore;
	}

}
