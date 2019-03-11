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
package it.iccu.sbn.web.actionforms.gestionestampe.semantica;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class PrenotaStampaSemanticaForm extends ActionForm {


	private static final long serialVersionUID = -1518744276184257865L;
	private List<CodiceVO> listaSiNo;
	private boolean sessione;
	private String tipoFormato;
	private ParametriStampaVO parametri;
	private List<ComboCodDescVO> listaSoggettari;
	private List<ComboCodDescVO> listaThesauri;
	private List<ComboCodDescVO> listaClassi;

	private List<TB_CODICI> listaEdizioni;

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public List<CodiceVO> getListaSiNo() {
		return listaSiNo;
	}

	public void setListaSiNo(List<CodiceVO> listaSiNo) {
		this.listaSiNo = listaSiNo;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public ParametriStampaVO getParametri() {
		return parametri;
	}

	public void setParametri(ParametriStampaVO parametri) {
		this.parametri = parametri;
	}

	public void setListaSoggettari(List<ComboCodDescVO> listaSoggettari) {
		this.listaSoggettari = listaSoggettari;

	}

	public List<ComboCodDescVO> getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaThesauri(List<ComboCodDescVO> listaThesauri) {
		this.listaThesauri = listaThesauri;
	}

	public List<ComboCodDescVO> getListaThesauri() {
		return listaThesauri;
	}

	public void setListaSistemiClassificazione(List<ComboCodDescVO> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

	public List<ComboCodDescVO> getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List<ComboCodDescVO> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

}
