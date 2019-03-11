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
package it.iccu.sbn.web.actionforms.documentofisico.datiInventari;

import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class VaiAListaInventariTitoloForm extends ActionForm {


	private static final long serialVersionUID = 2856355114937953250L;
	private DatiBibliograficiCollocazioneVO reticolo;
	private String bidNotCorr;
	private String titoloNotCorr;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private List listaInventari;
	private List listaBiblioteche;
	private List listaBibliotecheServizi;
	private String selectedInv;
	private String selectedPrg;
	private boolean disable;
	private boolean conferma;
	private boolean noInv;
	private boolean sessione;
	private String ticket;
	private boolean noReticolo;
	private String idInv;
	private boolean interrogazioneEsamina = false;
	private int numInventari;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int elemPerBlocchi=5;
	private int bloccoSelezionato=0;
	private boolean abilitaBottoneCarBlocchi;
	private String tipoOrdinamento;
	private List listaTipiOrdinamento;
	private boolean abilitaBottoneInviaInIndice;
	private boolean flagLoc;

	public String getBidNotCorr() {
		return bidNotCorr;
	}
	public void setBidNotCorr(String bidNotCorr) {
		this.bidNotCorr = bidNotCorr;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}
	public List getListaInventari() {
		return listaInventari;
	}
	public void setListaInventari(List listaInventari) {
		this.listaInventari = listaInventari;
	}
	public boolean isNoInv() {
		return noInv;
	}
	public void setNoInv(boolean noInv) {
		this.noInv = noInv;
	}
	public String getSelectedInv() {
		return selectedInv;
	}
	public void setSelectedInv(String selectedInv) {
		this.selectedInv = selectedInv;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTitoloNotCorr() {
		return titoloNotCorr;
	}
	public void setTitoloNotCorr(String titoloNotCorr) {
		this.titoloNotCorr = titoloNotCorr;
	}
	public int getNumInventari() {
		return numInventari;
	}
	public void setNumInventari(int numInventari) {
		this.numInventari = numInventari;
	}
	public DatiBibliograficiCollocazioneVO getReticolo() {
		return reticolo;
	}
	public void setReticolo(DatiBibliograficiCollocazioneVO reticolo) {
		this.reticolo = reticolo;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public boolean isNoReticolo() {
		return noReticolo;
	}
	public void setNoReticolo(boolean noReticolo) {
		this.noReticolo = noReticolo;
	}
	public boolean isAbilitaBottoneCarBlocchi() {
		return abilitaBottoneCarBlocchi;
	}
	public void setAbilitaBottoneCarBlocchi(boolean abilitaBottoneCarBlocchi) {
		this.abilitaBottoneCarBlocchi = abilitaBottoneCarBlocchi;
	}
	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}
	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}
	public int getElemPerBlocchi() {
		return elemPerBlocchi;
	}
	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.elemPerBlocchi = elemPerBlocchi;
	}
	public String getIdLista() {
		return idLista;
	}
	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}
	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}
	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}
	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}
	public int getTotBlocchi() {
		return totBlocchi;
	}
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}
	public int getTotRighe() {
		return totRighe;
	}
	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}
	public String getIdInv() {
		return idInv;
	}
	public void setIdInv(String idInv) {
		this.idInv = idInv;
	}
//	public void reset(ActionMapping mapping, HttpServletRequest request) {
//		this.selectedInv=null;
// }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if (!this.isConferma()){
			if (request.getParameter("navigation") == null)
				this.selectedInv=null;
		}
	}
	public String getSelectedPrg() {
		return selectedPrg;
	}
	public void setSelectedPrg(String selectedPrg) {
		this.selectedPrg = selectedPrg;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isInterrogazioneEsamina() {
		return interrogazioneEsamina;
	}
	public void setInterrogazioneEsamina(boolean interrogazioneEsamina) {
		this.interrogazioneEsamina = interrogazioneEsamina;
	}
	public List getListaBibliotecheServizi() {
		return listaBibliotecheServizi;
	}
	public void setListaBibliotecheServizi(List listaBibliotecheServizi) {
		this.listaBibliotecheServizi = listaBibliotecheServizi;
	}
	public boolean isAbilitaBottoneInviaInIndice() {
		return abilitaBottoneInviaInIndice;
	}
	public void setAbilitaBottoneInviaInIndice(boolean abilitaBottoneInviaInIndice) {
		this.abilitaBottoneInviaInIndice = abilitaBottoneInviaInIndice;
	}
	public boolean isFlagLoc() {
		return flagLoc;
	}
	public void setFlagLoc(boolean flagLoc) {
		this.flagLoc = flagLoc;
	}
}
