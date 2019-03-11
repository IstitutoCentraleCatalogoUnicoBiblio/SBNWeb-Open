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
package it.iccu.sbn.web.actionforms.documentofisico.modelliEtichette;

import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class ModelliEtichetteListaForm extends ActionForm {


	private static final long serialVersionUID = 6691697920454217798L;
	private ModelloEtichetteVO recModello;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private List listaBiblioteche;
	private List listaModelli;
	private String selectedModello;
	private boolean disable;
	private boolean noModello;
	private String action;
	private String richiamo;
	private boolean sessione;
	private boolean conferma;
	private String richiesta = null;
	private String ticket;

	private int numModelli;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int bloccoSelezionato=0;
	private boolean abilitaBottoneCarBlocchi;

	public int getElemPerBlocchi() {
		return nRec;
	}
	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.nRec = elemPerBlocchi;
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
	public ModelloEtichetteVO getRecModello() {
		return recModello;
	}
	public void setRecModello(ModelloEtichetteVO recModello) {
		this.recModello = recModello;
	}
	public List getListaModelli() {
		return listaModelli;
	}
	public void setListaModelli(List listaModelli) {
		this.listaModelli = listaModelli;
	}
	public String getSelectedModello() {
		return selectedModello;
	}
	public void setSelectedModello(String selectedModello) {
		this.selectedModello = selectedModello;
	}
	public boolean isNoModello() {
		return noModello;
	}
	public void setNoModello(boolean noModello) {
		this.noModello = noModello;
	}
	public int getNumModelli() {
		return numModelli;
	}
	public void setNumModelli(int numModelli) {
		this.numModelli = numModelli;
	}
}
