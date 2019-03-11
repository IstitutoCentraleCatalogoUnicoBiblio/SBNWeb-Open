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
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SezioniCollocazioniFormatiListaForm extends ActionForm {


	private static final long serialVersionUID = -7522087133378372054L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codSez;
	private String descrSez;
	private SezioneCollocazioneVO sezione = new SezioneCollocazioneVO();
	private List listaBiblioteche;
	private List listaFormatiSezione;
	private boolean forSez;
	private String selectedFor;
	private boolean disable;
	private boolean noFormati;
	private String action;
	private String richiamo;
	private boolean sessione;
	private boolean conferma;
	private String richiesta = null;
	private String ticket;
	private String prov;
	private String tipoLista;
	private boolean esamina = false;

	public boolean isEsamina() {
		return esamina;
	}
	public void setEsamina(boolean esamina) {
		this.esamina = esamina;
	}
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int bloccoSelezionato=0;
	private boolean abilitaBottoneCarBlocchi;
	private int elemPerBlocchi=5;
	private String tipoOrdinamento;
	private List listaTipiOrdinamento;

	public boolean isAbilitaBottoneCarBlocchi() {
		return abilitaBottoneCarBlocchi;
	}
	public void setAbilitaBottoneCarBlocchi(boolean abilitaBottoneCarBlocchi) {
		this.abilitaBottoneCarBlocchi = abilitaBottoneCarBlocchi;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}
	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodSez() {
		return codSez;
	}
	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public String getDescrSez() {
		return descrSez;
	}
	public void setDescrSez(String descrSez) {
		this.descrSez = descrSez;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public int getElemPerBlocchi() {
		return elemPerBlocchi;
	}
	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.elemPerBlocchi = elemPerBlocchi;
	}
	public boolean isForSez() {
		return forSez;
	}
	public void setForSez(boolean forSez) {
		this.forSez = forSez;
	}
	public String getIdLista() {
		return idLista;
	}
	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}
	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}
	public List getListaFormatiSezione() {
		return listaFormatiSezione;
	}
	public void setListaFormatiSezione(List listaFormatiSezione) {
		this.listaFormatiSezione = listaFormatiSezione;
	}
	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}
	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}
	public boolean isNoFormati() {
		return noFormati;
	}
	public void setNoFormati(boolean noFormati) {
		this.noFormati = noFormati;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public String getRichiamo() {
		return richiamo;
	}
	public void setRichiamo(String richiamo) {
		this.richiamo = richiamo;
	}
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
	public String getSelectedFor() {
		return selectedFor;
	}
	public void setSelectedFor(String selectedFor) {
		this.selectedFor = selectedFor;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
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
//	public void reset(ActionMapping mapping, HttpServletRequest request) {
//		this.selectedFor=null;
// }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if (request.getParameter("navigation") == null)
			this.selectedFor=null;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getTipoLista() {
		return tipoLista;
	}
	public void setTipoLista(String tipoLista) {
		this.tipoLista = tipoLista;
	}
	public SezioneCollocazioneVO getSezione() {
		return sezione;
	}
	public void setSezione(SezioneCollocazioneVO sezione) {
		this.sezione = sezione;
	}

}
