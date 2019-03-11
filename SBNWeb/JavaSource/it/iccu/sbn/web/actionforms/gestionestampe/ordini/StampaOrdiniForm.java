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
package it.iccu.sbn.web.actionforms.gestionestampe.ordini;

import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class StampaOrdiniForm extends ActionForm implements
		AcquisizioniBaseFormIntf {


	private static final long serialVersionUID = -134304548291703046L;
	private String codPolo;
	private String codBibl;
	private String descrBib;
	private String dataOrdineDa;
	private String dataOrdineA;
	private String tipoRicerca;
	private boolean sessione;
	private String tipoFormato;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinamento;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private String tipoModello;
	private String[] tipoArr;
	private String codFornitore;
	private String fornitore;
	private FornitoreVO fornitoreVO;
	private String fileRisultato;

	private boolean initialized;
	private StampaVo parametri = new StampaVo();

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.tipoArr = new String[0];
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getDataOrdineDa() {
		return dataOrdineDa;
	}

	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}

	public String getDataOrdineA() {
		return dataOrdineA;
	}

	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public List<ComboCodDescVO> getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public String getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}

	public String[] getTipoArr() {
		return tipoArr;
	}

	public void setTipoArr(String[] tipoArr) {
		this.tipoArr = tipoArr;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public String getFileRisultato() {
		return fileRisultato;
	}

	public void setFileRisultato(String fileRisultato) {
		this.fileRisultato = fileRisultato;
	}

	public FornitoreVO getFornitoreVO() {
		return fornitoreVO;
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		this.fornitoreVO = fornitoreVO;
	}

	public StampaVo getParametri() {
		return parametri;
	}

	public void setParametri(StampaVo parametri) {
		this.parametri = parametri;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

}
