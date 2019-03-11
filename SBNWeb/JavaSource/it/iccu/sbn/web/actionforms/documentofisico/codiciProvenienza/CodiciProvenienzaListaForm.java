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
package it.iccu.sbn.web.actionforms.documentofisico.codiciProvenienza;

import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CodiciProvenienzaListaForm extends ActionForm {


	private static final long serialVersionUID = -5521388279824945630L;
	private ProvenienzaInventarioVO recProvInv = new ProvenienzaInventarioVO();
	private List listaProvInv;
	private List listaBiblioteche;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String selectedCod;
	private boolean disable;
	private boolean noCod;
	private boolean sessione;
	private boolean noProvInv;
	private boolean conferma;
	private String richiamo;
	private String action;
	private String ticket;

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
	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}
	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
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
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
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
	public boolean isNoCod() {
		return noCod;
	}
	public void setNoCod(boolean noCod) {
		this.noCod = noCod;
	}
	public String getSelectedCod() {
		return selectedCod;
	}
	public void setSelectedCod(String selectedCod) {
		this.selectedCod = selectedCod;
	}
	public ProvenienzaInventarioVO getRecProvInv() {
		return recProvInv;
	}
	public void setRecProvInv(ProvenienzaInventarioVO recProvInv) {
		this.recProvInv = recProvInv;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getRichiamo() {
		return richiamo;
	}
	public void setRichiamo(String richiamo) {
		this.richiamo = richiamo;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isNoProvInv() {
		return noProvInv;
	}
	public void setNoProvInv(boolean noProvInv) {
		this.noProvInv = noProvInv;
	}
	public List getListaProvInv() {
		return listaProvInv;
	}
	public void setListaProvInv(List listaProvInv) {
		this.listaProvInv = listaProvInv;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
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
	public int getElemPerBlocchi() {
		return elemPerBlocchi;
	}
	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.elemPerBlocchi = elemPerBlocchi;
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
			this.selectedCod=null;
	}

}
