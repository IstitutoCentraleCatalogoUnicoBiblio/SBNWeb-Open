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
package it.iccu.sbn.web.actionforms.documentofisico.serieInventariali;

import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SerieInventarialeGestioneForm extends ActionForm {


	private static final long serialVersionUID = -7214527502585416851L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private SerieVO recSerie = new SerieVO();
	private SerieVO recSerieOld = new SerieVO();
	private List listaTipoSerie;
	private boolean disableDataPregr;
	private boolean disableDataMan;

	private boolean disableDataIntRis1;
	private boolean disableIntRis2Inizio;
	private boolean disableIntRis2Fine;
	private boolean disableDataIntRis2;
	private boolean disableIntRis3Inizio;
	private boolean disableIntRis3Fine;
	private boolean disableDataIntRis3;
	private boolean disableIntRis4Inizio;
	private boolean disableIntRis4Fine;
	private boolean disableDataIntRis4;
	private boolean disableFlDefault;
	//

	private boolean disableBuonoCarico;
	private boolean disable;
	private boolean flChiusa;
	private boolean flDefault;
	private boolean disableSerie=false;
	private boolean esamina=false;
	private boolean sessione;
	private String tastoOk;
	private boolean conferma;
	private boolean tastoAggiorna=false;
	private boolean salva;
	private boolean date=false;
	private String ticket;
	private String prov;
	public static final String SUBMIT_CANCELLA_INTERVALLO = "serie.inventariale.gestione.canc.intervallo";
	private String primoIntervallo;
	private String secondoIntervallo;
	private String terzoIntervallo;
	private String quartoIntervallo;
	private String intervalloSelez;


	public String getIntervalloSelez() {
		return intervalloSelez;
	}

	public void setIntervalloSelez(String intervalloSelez) {
		this.intervalloSelez = intervalloSelez;
	}



	public String getSUBMIT_CANCELLA_INTERVALLO() {
		return SUBMIT_CANCELLA_INTERVALLO;
	}

	public String getTicket() {
		return ticket;
	}



	public void setTicket(String ticket) {
		this.ticket = ticket;
	}



	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}



	public String getCodBib() {
		return codBib;
	}



	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}



	public boolean isConferma() {
		return conferma;
	}



	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}



	public boolean isDate() {
		return date;
	}



	public void setDate(boolean date) {
		this.date = date;
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



	public boolean isDisableSerie() {
		return disableSerie;
	}



	public void setDisableSerie(boolean disableSerie) {
		this.disableSerie = disableSerie;
	}



	public boolean isEsamina() {
		return esamina;
	}



	public void setEsamina(boolean esamina) {
		this.esamina = esamina;
	}



	public boolean isFlChiusa() {
		return flChiusa;
	}



	public void setFlChiusa(boolean flChiusa) {
		this.flChiusa = flChiusa;
	}



	public List getListaTipoSerie() {
		return listaTipoSerie;
	}



	public void setListaTipoSerie(List listaTipoSerie) {
		this.listaTipoSerie = listaTipoSerie;
	}



	public SerieVO getRecSerie() {
		return recSerie;
	}



	public void setRecSerie(SerieVO recSerie) {
		this.recSerie = recSerie;
	}



	public boolean isSalva() {
		return salva;
	}



	public void setSalva(boolean salva) {
		this.salva = salva;
	}



	public boolean isSessione() {
		return sessione;
	}



	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}



	public boolean isTastoAggiorna() {
		return tastoAggiorna;
	}



	public void setTastoAggiorna(boolean tastoAggiorna) {
		this.tastoAggiorna = tastoAggiorna;
	}



	public String getTastoOk() {
		return tastoOk;
	}



	public void setTastoOk(String tastoOk) {
		this.tastoOk = tastoOk;
	}



	public String getCodPolo() {
		return codPolo;
	}



	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}



	public boolean isFlDefault() {
		return flDefault;
	}



	public void setFlDefault(boolean flDefault) {
		this.flDefault = flDefault;
	}



	public String getProv() {
		return prov;
	}



	public void setProv(String prov) {
		this.prov = prov;
	}

	public boolean isDisableDataIntRis1() {
		return disableDataIntRis1;
	}



	public void setDisableDataIntRis1(boolean disableDataIntRis1) {
		this.disableDataIntRis1 = disableDataIntRis1;
	}



	public boolean isDisableIntRis2Inizio() {
		return disableIntRis2Inizio;
	}



	public void setDisableIntRis2Inizio(boolean disableIntRis2Inizio) {
		this.disableIntRis2Inizio = disableIntRis2Inizio;
	}



	public boolean isDisableIntRis2Fine() {
		return disableIntRis2Fine;
	}



	public void setDisableIntRis2Fine(boolean disableIntRis2Fine) {
		this.disableIntRis2Fine = disableIntRis2Fine;
	}



	public boolean isDisableDataIntRis2() {
		return disableDataIntRis2;
	}



	public void setDisableDataIntRis2(boolean disableDataIntRis2) {
		this.disableDataIntRis2 = disableDataIntRis2;
	}



	public boolean isDisableIntRis3Inizio() {
		return disableIntRis3Inizio;
	}



	public void setDisableIntRis3Inizio(boolean disableIntRis3Inizio) {
		this.disableIntRis3Inizio = disableIntRis3Inizio;
	}



	public boolean isDisableIntRis3Fine() {
		return disableIntRis3Fine;
	}



	public void setDisableIntRis3Fine(boolean disableIntRis3Fine) {
		this.disableIntRis3Fine = disableIntRis3Fine;
	}



	public boolean isDisableDataIntRis3() {
		return disableDataIntRis3;
	}



	public void setDisableDataIntRis3(boolean disableDataIntRis3) {
		this.disableDataIntRis3 = disableDataIntRis3;
	}



	public boolean isDisableIntRis4Inizio() {
		return disableIntRis4Inizio;
	}



	public void setDisableIntRis4Inizio(boolean disableIntRis4Inizio) {
		this.disableIntRis4Inizio = disableIntRis4Inizio;
	}



	public boolean isDisableIntRis4Fine() {
		return disableIntRis4Fine;
	}



	public void setDisableIntRis4Fine(boolean disableIntRis4Fine) {
		this.disableIntRis4Fine = disableIntRis4Fine;
	}



	public boolean isDisableDataIntRis4() {
		return disableDataIntRis4;
	}



	public void setDisableDataIntRis4(boolean disableDataIntRis4) {
		this.disableDataIntRis4 = disableDataIntRis4;
	}



	public boolean isDisableFlDefault() {
		return disableFlDefault;
	}



	public void setDisableFlDefault(boolean disableFlDefault) {
		this.disableFlDefault = disableFlDefault;
	}



	public boolean isDisableDataPregr() {
		return disableDataPregr;
	}



	public void setDisableDataPregr(boolean disableDataPregr) {
		this.disableDataPregr = disableDataPregr;
	}



	public boolean isDisableDataMan() {
		return disableDataMan;
	}



	public void setDisableDataMan(boolean disableDataMan) {
		this.disableDataMan = disableDataMan;
	}



	public SerieVO getRecSerieOld() {
		return recSerieOld;
	}



	public void setRecSerieOld(SerieVO recSerieOld) {
		this.recSerieOld = recSerieOld;
	}

	public String getSecondoIntervallo() {
		return secondoIntervallo;
	}



	public void setSecondoIntervallo(String secondoIntervallo) {
		this.secondoIntervallo = secondoIntervallo;
	}



	public String getTerzoIntervallo() {
		return terzoIntervallo;
	}



	public void setTerzoIntervallo(String terzoIntervallo) {
		this.terzoIntervallo = terzoIntervallo;
	}



	public String getPrimoIntervallo() {
		return primoIntervallo;
	}



	public void setPrimoIntervallo(String primoIntervallo) {
		this.primoIntervallo = primoIntervallo;
	}



	public String getQuartoIntervallo() {
		return quartoIntervallo;
	}



	public void setQuartoIntervallo(String quartoIntervallo) {
		this.quartoIntervallo = quartoIntervallo;
	}

	public boolean isDisableBuonoCarico() {
		return disableBuonoCarico;
	}

	public void setDisableBuonoCarico(boolean disableBuonoCarico) {
		this.disableBuonoCarico = disableBuonoCarico;
	}





}
