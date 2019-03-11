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
package it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni;

import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EsameCollocListeCollocazioniForm extends ActionForm {


	private static final long serialVersionUID = 387783454408165787L;
	private static final int BUFFER_SIZE = 1024;
	EsameCollocRicercaVO paramRicerca;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codSezione;
	private String codColloc;
	private List listaColloc = new ArrayList();
	private List listaCollocSpec = new ArrayList();
	// private List listaUltColl = new ArrayList();
	// private List listaUltSpec = new ArrayList();
	private String listaSupportoColl;
	private String listaSupportoSpec;
	private String selectedColl;
	private String selectedCollSpec;
	private boolean disable;
	private boolean noColl;
	private boolean sessione;
	private String ticket;
	private String action;
	private String richiamo;
	private String prov;
	private String ultLoc;
	private String ultOrdLoc;
	private String ultSpec;
	private String ultKeyLoc;
	private boolean caricoAltriBlocchi;
	private String oldIdLista;
	private List oldLista;
	private List newLista;
	private List listaConUltimoBlocco;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI
			.getDefault());
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int bloccoSelezionato = 0;
	private int bloccoAltraLista = 0;
	private boolean abilitaBottoneCarBlocchi;
	private int elemPerBlocchi = 5;
	private String tipoOrdinam;
	private List listaTipiOrdinamento;
	private String tipoRichiesta;
	private int blocchiTotali;

	//almaviva5_20101215 periodici
	private boolean periodici;
	private boolean kardex;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
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

	public String getCodSezione() {
		return codSezione;
	}

	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public List getListaColloc() {
		return listaColloc;
	}

	public void setListaColloc(List listaColloc) {
		this.listaColloc = listaColloc;
	}

	public boolean isNoColl() {
		return noColl;
	}

	public void setNoColl(boolean noColl) {
		this.noColl = noColl;
	}

	public String getSelectedColl() {
		return selectedColl;
	}

	public void setSelectedColl(String selectedColl) {
		this.selectedColl = selectedColl;
	}

	public EsameCollocRicercaVO getParamRicerca() {
		return paramRicerca;
	}

	public void setParamRicerca(EsameCollocRicercaVO paramRicerca) {
		this.paramRicerca = paramRicerca;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRichiamo() {
		return richiamo;
	}

	public void setRichiamo(String richiamo) {
		this.richiamo = richiamo;
	}

	public List getListaCollocSpec() {
		return listaCollocSpec;
	}

	public void setListaCollocSpec(List listaCollocSpec) {
		this.listaCollocSpec = listaCollocSpec;
	}

	public String getListaSupportoColl() {
		return listaSupportoColl;
	}

	public void setListaSupportoColl(String listaSupportoColl) {
		this.listaSupportoColl = listaSupportoColl;
	}

	public String getListaSupportoSpec() {
		return listaSupportoSpec;
	}

	public void setListaSupportoSpec(String listaSupportoSpec) {
		this.listaSupportoSpec = listaSupportoSpec;
	}

	public String getCodColloc() {
		return codColloc;
	}

	public void setCodColloc(String codColloc) {
		this.codColloc = codColloc;
	}

	public String getSelectedCollSpec() {
		return selectedCollSpec;
	}

	public void setSelectedCollSpec(String selectedCollSpec) {
		this.selectedCollSpec = selectedCollSpec;
	}

	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}

	public EsameCollocListeCollocazioniForm cloneForm() throws Exception {

		// serialize form in byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		byte buf[] = baos.toByteArray();
		oos.close();

		// deserialize form
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		ObjectInputStream ois = new ObjectInputStream(bais);
		EsameCollocListeCollocazioniForm clone = (EsameCollocListeCollocazioniForm) ois
				.readObject();
		ois.close();

		return clone;

	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if (request.getParameter("navigation") == null)
			this.selectedCollSpec = null;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getTipoRichiesta() {
		return tipoRichiesta;
	}

	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}

	public boolean isCaricoAltriBlocchi() {
		return caricoAltriBlocchi;
	}

	public void setCaricoAltriBlocchi(boolean caricoAltriBlocchi) {
		this.caricoAltriBlocchi = caricoAltriBlocchi;
	}

	public String getOldIdLista() {
		return oldIdLista;
	}

	public void setOldIdLista(String oldIdLista) {
		this.oldIdLista = oldIdLista;
	}

	public List getListaConUltimoBlocco() {
		return listaConUltimoBlocco;
	}

	public void setListaConUltimoBlocco(List listaConUltimoBlocco) {
		this.listaConUltimoBlocco = listaConUltimoBlocco;
	}

	public List getOldLista() {
		return oldLista;
	}

	public void setOldLista(List oldLista) {
		this.oldLista = oldLista;
	}

	public List getNewLista() {
		return newLista;
	}

	public void setNewLista(List newLista) {
		this.newLista = newLista;
	}

	public String getUltKeyLoc() {
		return ultKeyLoc;
	}

	public void setUltKeyLoc(String ultKeyLoc) {
		this.ultKeyLoc = ultKeyLoc;
	}

	public int getBloccoAltraLista() {
		return bloccoAltraLista;
	}

	public void setBloccoAltraLista(int bloccoAltraLista) {
		this.bloccoAltraLista = bloccoAltraLista;
	}

	public int getBlocchiTotali() {
		return blocchiTotali;
	}

	public void setBlocchiTotali(int blocchiTotali) {
		this.blocchiTotali = blocchiTotali;
	}

	public String getUltLoc() {
		return ultLoc;
	}

	public void setUltLoc(String ultLoc) {
		this.ultLoc = ultLoc;
	}

	public String getUltSpec() {
		return ultSpec;
	}

	public void setUltSpec(String ultSpec) {
		this.ultSpec = ultSpec;
	}

	public String getUltOrdLoc() {
		return ultOrdLoc;
	}

	public void setUltOrdLoc(String ultOrdLoc) {
		this.ultOrdLoc = ultOrdLoc;
	}

	public String getTipoOrdinam() {
		return tipoOrdinam;
	}

	public void setTipoOrdinam(String tipoOrdinam) {
		this.tipoOrdinam = tipoOrdinam;
	}

	public boolean isPeriodici() {
		return periodici;
	}

	public void setPeriodici(boolean periodici) {
		this.periodici = periodici;
	}

	public boolean isKardex() {
		return kardex;
	}

	public void setKardex(boolean kardex) {
		this.kardex = kardex;
	}
}
