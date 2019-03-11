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
package it.iccu.sbn.web.actionforms.documentofisico.sezioniCollocazioni;

import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SezioniCollocazioniListaForm extends ActionForm {


	private static final long serialVersionUID = 25636421560530677L;
	private SezioneCollocazioneVO recSez;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private List listaBiblioteche;
	private List listaSezioni;
	private String selectedSez;
	private boolean disable;
	private boolean noSezione;
	private String action;
	private String richiamo;
	private boolean sessione;
	private boolean conferma;
	private String richiesta = null;
	private String ticket;

	private int numSezioni;

//	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private int nRec =  Integer.valueOf("9999");
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int bloccoSelezionato=0;
	private boolean abilitaBottoneCarBlocchi;
	private String tipoOrdinamento;
	private List listaTipiOrdinamento;

	public int getElemPerBlocchiSez() {
		return nRec;
	}
	public void setElemPerBlocchiSez(int elemPerBlocchi) {
		this.nRec = elemPerBlocchi;
	}
	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}
	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}
	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}
	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if (request.getParameter("navigation") == null)
			this.selectedSez=null;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	public String getSelectedSez() {
		return selectedSez;
	}
	public void setSelectedSez(String selectedSez) {
		this.selectedSez = selectedSez;
	}
	public boolean isNoSezione() {
		return noSezione;
	}
	public void setNoSezione(boolean noSezione) {
		this.noSezione = noSezione;
	}
	public List getListaSezioni() {
		return listaSezioni;
	}
	public void setListaSezioni(List listaSezioni) {
		this.listaSezioni = listaSezioni;
	}
	public String getRichiamo() {
		return richiamo;
	}
	public void setRichiamo(String richiamo) {
		this.richiamo = richiamo;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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
	public int getNumSezioni() {
		return numSezioni;
	}
	public void setNumSezioni(int numSezioni) {
		this.numSezioni = numSezioni;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public String getIdLista() {
		return idLista;
	}
	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
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
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
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
	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}
	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}
	public boolean isAbilitaBottoneCarBlocchi() {
		return abilitaBottoneCarBlocchi;
	}
	public void setAbilitaBottoneCarBlocchi(boolean abilitaBottoneCarBlocchi) {
		this.abilitaBottoneCarBlocchi = abilitaBottoneCarBlocchi;
	}
	public SezioneCollocazioneVO getRecSez() {
		return recSez;
	}
	public void setRecSez(SezioneCollocazioneVO recSez) {
		this.recSez = recSez;
	}
}
