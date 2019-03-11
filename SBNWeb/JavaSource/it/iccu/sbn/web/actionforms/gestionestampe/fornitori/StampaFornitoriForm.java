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
package it.iccu.sbn.web.actionforms.gestionestampe.fornitori;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class StampaFornitoriForm extends ActionForm{//  DynaValidatorForm

	private static final long serialVersionUID = 6989189802601818465L;
	private String tipoFormato;
	private String tipoModello;
	private String elemBlocco;
	private String nomForn;
	private List listaTpForn;
	private String tpForn;
	private List listaProv;
	private String prov;
	private List listaPaese;
	private String paese;
	private List listaProfAcq;
	private String profAcq;
	private String codForn;
	private boolean sessione;
	private boolean ricercaLocale = false;
	private String codBib;
	private String descrBib;
	private String tipoOrdinamSelez;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private List listaTipiOrdinamento;
	private String codPolo;


	private List listaBiblioteche;

	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getElemBlocco() {
		return elemBlocco;
	}
	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}
	public String getNomForn() {
		return nomForn;
	}
	public void setNomForn(String nomForn) {
		this.nomForn = nomForn;
	}
	public void setListaTpForn(List listaTpForn) {
		this.listaTpForn = listaTpForn;
	}
	public List getListaTpForn() {
		return listaTpForn;
	}
	public String getTpForn() {
		return tpForn;
	}
	public void setTpForn(String tpForn) {
		this.tpForn = tpForn;
	}
	public List getListaProv() {
		return listaProv;
	}
	public void setListaProv(List listaProv) {
		this.listaProv = listaProv;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public List getListaPaese() {
		return listaPaese;
	}
	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public List getListaProfAcq() {
		return listaProfAcq;
	}
	public void setListaProfAcq(List listaProfAcq) {
		this.listaProfAcq = listaProfAcq;
	}
	public String getProfAcq() {
		return profAcq;
	}
	public void setProfAcq(String profAcq) {
		this.profAcq = profAcq;
	}
	public String getCodForn() {
		return codForn;
	}
	public void setCodForn(String codForn) {
		this.codForn = codForn;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public List getElencoModelli() {
		return elencoModelli;
	}
	public void setElencoModelli(List tipiModelli) {
		elencoModelli = tipiModelli;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public boolean isRicercaLocale() {
		return ricercaLocale;
	}

	public void setRicercaLocale(boolean ricercaLocale) {
		this.ricercaLocale = ricercaLocale;
	}
	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}
	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}
	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}
	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.ricercaLocale= false;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
}
