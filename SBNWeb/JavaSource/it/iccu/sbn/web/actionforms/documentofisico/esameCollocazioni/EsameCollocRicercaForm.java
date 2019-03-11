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
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class EsameCollocRicercaForm extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = 7438958583355480424L;

	private EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();

	private String codSerie;
	private String codRfid;
	private String codBibSez;
	private String codSez;
	private int codInvent;
	private String codLoc;
	private String codSpec;
	private List listaBiblioteche;
//	private boolean esattoColl = false;
//	private boolean esattoSpec = false;
	private boolean disableEsattoColl;
	private boolean disableEsattoSpec;
	private boolean disableSez;
	private boolean disableSpec;
	private boolean disableTastoInvColl;
	private boolean disableTastoSez;
	private boolean disableTastoColl;
	private boolean disableTastoSpec;
	private boolean sessione;
	private String noSezione;
	private boolean noColl;
	private String action;// hidden
	private String test;// hidden
	private String ticket;
	private int nRec = Integer.valueOf(ConstantDefault.GDF_ESA_COLL_ELEM_BLOCCHI.getDefault());
	private String tipoOrdinamento;
	private List listaTipiOrdinamento;

	//almaviva5_20120222 rfid
	private boolean rfidEnabled;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isDisableTastoColl() {
		return disableTastoColl;
	}

	public void setDisableTastoColl(boolean disableTastoColl) {
		this.disableTastoColl = disableTastoColl;
	}

	public boolean isDisableTastoSpec() {
		return disableTastoSpec;
	}

	public void setDisableTastoSpec(boolean disableTastoSpec) {
		this.disableTastoSpec = disableTastoSpec;
	}

	public int getElemPerBlocchi() {
		return nRec;
	}

	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.nRec = elemPerBlocchi;
	}


	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}

	public boolean isNoColl() {
		return noColl;
	}

	public void setNoColl(boolean noColl) {
		this.noColl = noColl;
	}

	public String getNoSezione() {
		return noSezione;
	}

	public void setNoSezione(String noSezione) {
		this.noSezione = noSezione;
	}

	public EsameCollocRicercaVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(EsameCollocRicercaVO ricerca) {
		this.ricerca = ricerca;
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

	public boolean isDisableSpec() {
		return disableSpec;
	}

	public void setDisableSpec(boolean disableSpec) {
		this.disableSpec = disableSpec;
	}

	public boolean isDisableSez() {
		return disableSez;
	}

	public void setDisableSez(boolean disableSez) {
		this.disableSez = disableSez;
	}

	public boolean isDisableTastoSez() {
		return disableTastoSez;
	}

	public void setDisableTastoSez(boolean disableTastoSez) {
		this.disableTastoSez = disableTastoSez;
	}

	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// this.ricerca.setCodSez("");
		// this.ricerca.setCodInvent(0);
		//this.setListaComboSerie(this.caricaCombo.loadCodice(this.getListaSerie
		// ()));
	}

	public boolean isDisableTastoInvColl() {
		return disableTastoInvColl;
	}

	public void setDisableTastoInvColl(boolean disableTastoInvColl) {
		this.disableTastoInvColl = disableTastoInvColl;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public int getCodInvent() {
		return codInvent;
	}

	public void setCodInvent(int codInvent) {
		this.codInvent = codInvent;
	}

	public String getCodLoc() {
		return codLoc;
	}

	public void setCodLoc(String codLoc) {
		this.codLoc = codLoc;
	}

	public String getCodSpec() {
		return codSpec;
	}

	public void setCodSpec(String codSpec) {
		this.codSpec = codSpec;
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getCodBibSez() {
		return codBibSez;
	}

	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}

	public String getCodPoloSez() {
		return codPoloSez;
	}

	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}

	public boolean isDisableEsattoColl() {
		return disableEsattoColl;
	}

	public void setDisableEsattoColl(boolean disableEsattoColl) {
		this.disableEsattoColl = disableEsattoColl;
	}

	public boolean isDisableEsattoSpec() {
		return disableEsattoSpec;
	}

	public void setDisableEsattoSpec(boolean disableEsattoSpec) {
		this.disableEsattoSpec = disableEsattoSpec;
	}

	public String getCodRfid() {
		return codRfid;
	}

	public void setCodRfid(String codRfid) {
		this.codRfid = codRfid;
	}

	public List getListaBiblioteche() {
		return listaBiblioteche;
	}

	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}

	public boolean isRfidEnabled() {
		return rfidEnabled;
	}

	public void setRfidEnabled(boolean rfidEnabled) {
		this.rfidEnabled = rfidEnabled;
	}
}
