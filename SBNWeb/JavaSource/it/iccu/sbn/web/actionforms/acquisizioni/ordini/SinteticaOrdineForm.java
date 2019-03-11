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
package it.iccu.sbn.web.actionforms.acquisizioni.ordini;

import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaOrdineForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 7206100396810174368L;
	// SINTETICA
	private List<OrdiniVO> listaOrdini;
	private String[] selectedOrdini;
	private int numOrdini;
	private boolean sessione;
	private boolean LSRicerca=false;
	private String ordinamentoScelto;
	private String denoBibl;
	private String prov;
	private String bidNotiziaCorrente;


public String getBidNotiziaCorrente() {
		return bidNotiziaCorrente;
	}
	public void setBidNotiziaCorrente(String bidNotiziaCorrente) {
		this.bidNotiziaCorrente = bidNotiziaCorrente;
	}
	/*	private int numRighe=5;
	private int numPagina=1;
	private int posElemento=0;
	private int totPagine=0;
	private int numRigheOld=5;
	private int posElementoOld=0;
	private int numPaginaOld=1;*/
	private StrutturaCombo ordinaLista;
	private String[] selectedOrdiniChiave;

	private boolean risultatiPresenti=true;
	private boolean visibilitaIndietroLS=false;
	private boolean provenienzaVAIA=false;
	private boolean gestBil=true;

	// gestione blocchi
	private int progrForm=0;
	private String livelloRicerca;
	//private int totBlocchi;
	private int numNotizie;
//	private int numBlocco;
	private DescrittoreBloccoVO ultimoBlocco;

	private boolean abilitaBlocchi;
	private int totBlocchi; //???

	/**
	 * @return Returns the totBlocchi.
	 */
	public int getTotBlocchi() {
		return totBlocchi;
	}
	/**
	 * @param totBlocchi The totBlocchi to set.
	 */
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}
	public void setAbilitaBlocchi(boolean b) {
		abilitaBlocchi = b;
	}
	/**
	 * @return Returns the abilitaBlocchi.
	 */
	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}


	public String[] getSelectedOrdini() {
		return selectedOrdini;
	}

	public void setSelectedOrdini(String[] selectedOrdini) {
		this.selectedOrdini = selectedOrdini;
	}


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

			return errors;
	}


	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selectedOrdini=new String[0];
	}

	public int getNumOrdini() {
		return numOrdini;
	}


	public void setNumOrdini(int numOrdini) {
		this.numOrdini = numOrdini;
	}


	public boolean isSessione() {
		return sessione;
	}


	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}



	public List<OrdiniVO> getListaOrdini() {
		return listaOrdini;
	}

	public void setListaOrdini(List<OrdiniVO> listaOrdini) {
		this.listaOrdini = listaOrdini;
	}

	public StrutturaCombo getOrdinaLista() {
		return ordinaLista;
	}

	public void setOrdinaLista(StrutturaCombo ordinaLista) {
		this.ordinaLista = ordinaLista;
	}

	public String[] getSelectedOrdiniChiave() {
		return selectedOrdiniChiave;
	}

	public void setSelectedOrdiniChiave(String[] selectedOrdiniChiave) {
		this.selectedOrdiniChiave = selectedOrdiniChiave;
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
	public boolean isProvenienzaVAIA() {
		return provenienzaVAIA;
	}
	public void setProvenienzaVAIA(boolean provenienzaVAIA) {
		this.provenienzaVAIA = provenienzaVAIA;
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
	public String getDenoBibl() {
		return denoBibl;
	}
	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}
	public boolean isGestBil() {
		return gestBil;
	}
	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}



}
