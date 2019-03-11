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
package it.iccu.sbn.web.actionforms.gestionestampe.servizi;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ErogazioneRicercaForm;

import java.util.ArrayList;
import java.util.List;

public class StampaServiziForm extends ErogazioneRicercaForm{


	private static final long serialVersionUID = 3189521859900103603L;
	private String codPolo;
	private String codBib;
	private String tipoModello;
	private String tipoDiStampa;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private ModelloStampaVO modello;
	private String ticket;
	private boolean disable;
	private boolean noData;

	private String tipoFormato;
	private boolean sessione1 = false;
	private String descrBib;
	private List listaBiblioteche;
	//
	private String dataDa;
	private String dataA;

	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public String getTipoDiStampa() {
		return tipoDiStampa;
	}
	public void setTipoDiStampa(String tipoDiStampa) {
		this.tipoDiStampa = tipoDiStampa;
	}
	public List<ComboCodDescVO> getElencoModelli() {
		return elencoModelli;
	}
	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}
	public ModelloStampaVO getModello() {
		return modello;
	}
	public void setModello(ModelloStampaVO modello) {
		this.modello = modello;
	}
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
	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}
	public boolean isSessione1() {
		return sessione1;
	}
	public void setSessione1(boolean sessione1) {
		this.sessione1 = sessione1;
	}
	public String getDataDa() {
		return dataDa;
	}
	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}
	public String getDataA() {
		return dataA;
	}
	public void setDataA(String dataA) {
		this.dataA = dataA;
	}
	public boolean isNoData() {
		return noData;
	}
	public void setNoData(boolean noData) {
		this.noData = noData;
	}

}
