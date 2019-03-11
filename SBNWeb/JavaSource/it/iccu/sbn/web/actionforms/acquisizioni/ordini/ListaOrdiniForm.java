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
package it.iccu.sbn.web.actionforms.acquisizioni.ordini;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class ListaOrdiniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 6460961712458418704L;
	private List<OrdiniVO> listaOrdini = new ArrayList<OrdiniVO>();
	private String[] selectedOrdini;
	private String radioSel;
	private boolean sessione;
	private boolean risultatiPresenti = true;
	private int numOrdini;
	private ListaSuppOrdiniVO sifOrdini;
	private boolean provInterroga = false;
	private boolean provBo = false;
	private List<OrdiniVO> listaSinteticaCompleta = new ArrayList<OrdiniVO>();
	private List<OrdiniVO> listaSintetica = new ArrayList<OrdiniVO>();
	private String codBiblioSelez;
	private List<StrutturaCombo> listaCodBib;
	private int elemXBlocchi = 10;
	private String tipoOrdinamSelez = "8";
	private String[] statoArr = new String[0];

	// private int totBlocchi;
	// private int numBlocco;
	private DescrittoreBloccoVO ultimoBlocco;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selectedOrdini = new String[0];
		this.setRisultatiPresenti(true);
	}

	public List<OrdiniVO> getListaOrdini() {
		return listaOrdini;
	}

	public void setListaOrdini(List<OrdiniVO> listaOrdini) {
		this.listaOrdini = listaOrdini;
	}

	public int getNumOrdini() {
		return numOrdini;
	}

	public void setNumOrdini(int numOrdini) {
		this.numOrdini = numOrdini;
	}

	public String getRadioSel() {
		return radioSel;
	}

	public void setRadioSel(String radioSel) {
		this.radioSel = radioSel;
	}

	public boolean isRisultatiPresenti() {
		return risultatiPresenti;
	}

	public void setRisultatiPresenti(boolean risultatiPresenti) {
		this.risultatiPresenti = risultatiPresenti;
	}

	public String[] getSelectedOrdini() {
		return selectedOrdini;
	}

	public void setSelectedOrdini(String[] selectedOrdini) {
		this.selectedOrdini = selectedOrdini;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public DescrittoreBloccoVO getUltimoBlocco() {
		return ultimoBlocco;
	}

	public void setUltimoBlocco(DescrittoreBloccoVO ultimoBlocco) {
		this.ultimoBlocco = ultimoBlocco;
	}

	public ListaSuppOrdiniVO getSifOrdini() {
		return sifOrdini;
	}

	public void setSifOrdini(ListaSuppOrdiniVO sifOrdini) {
		this.sifOrdini = sifOrdini;
	}

	public boolean isProvInterroga() {
		return provInterroga;
	}

	public void setProvInterroga(boolean provInterroga) {
		this.provInterroga = provInterroga;
	}

	public boolean isProvBo() {
		return provBo;
	}

	public void setProvBo(boolean provBo) {
		this.provBo = provBo;
	}

	public List<OrdiniVO> getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List<OrdiniVO> listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public List<OrdiniVO> getListaSinteticaCompleta() {
		return listaSinteticaCompleta;
	}

	public void setListaSinteticaCompleta(List<OrdiniVO> listaSinteticaCompleta) {
		this.listaSinteticaCompleta = listaSinteticaCompleta;
	}

	public String getCodBiblioSelez() {
		return codBiblioSelez;
	}

	public void setCodBiblioSelez(String codBiblioSelez) {
		this.codBiblioSelez = codBiblioSelez;
	}

	public List<StrutturaCombo> getListaCodBib() {
		return listaCodBib;
	}

	public void setListaCodBib(List<StrutturaCombo> listaCodBib) {
		this.listaCodBib = listaCodBib;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public String[] getStatoArr() {
		return statoArr;
	}

	public void setStatoArr(String[] statoArr) {
		this.statoArr = statoArr;
	}

}
