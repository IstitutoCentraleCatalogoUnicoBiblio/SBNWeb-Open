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

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaThesauroPoloForm extends ActionForm {

	private static final long serialVersionUID = 105694703541005182L;
	private String elemBlocco;
	private String codThe;
	private String insDal;
	private String insAl;
	private String aggDal;
	private String aggAl;
	private String opzNoteThe;
	private String opzTerminiBiblio;
	private String opzNoteTerminiColl;
	private String opzLegamiTitoloDiBiblio;
	private String opzTerminiColl;
	private String opzTitoli;
	private String opzFormeRinvio;
	private String tipoModello;
	private List listaCodThe;
	private List listaOpzNoteThe;
	private List listaOpzTerminiBiblio;
	private List listaOpzNoteTerminiColl;
	private List listaOpzLegamiTitoloDiBiblio;
	private List listaOpzTerminiColl;
	private List listaOpzTitoli;
	private List listaOpzFormeRinvio;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private String tipoRicerca;
	private boolean sessione;
	private String tipoFormato;

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
	public String getCodThe() {
		return codThe;
	}
	public void setCodThe(String codThe) {
		this.codThe = codThe;
	}

	public String getInsDal() {
		return insDal;
	}
	public void setInsDal(String insDal) {
		this.insDal = insDal;
	}
	public String getInsAl() {
		return insAl;
	}
	public void setInsAl(String insAl) {
		this.insAl = insAl;
	}

	public String getAggDal() {
		return aggDal;
	}
	public void setAggDal(String aggDal) {
		this.aggDal = aggDal;
	}

	public String getAggAl() {
		return aggAl;
	}
	public void setAggAl(String aggAl) {
		this.aggAl = aggAl;
	}


//	public String getOpzTit() {
//		return opzTit;
//	}
//	public void setOpzTit(String opzTit) {
//		this.opzTit = opzTit;
//	}

	public String getOpzNoteThe() {
		return opzNoteThe;
	}
	public void setOpzNoteThe(String opzNoteThe) {
		this.opzNoteThe = opzNoteThe;
	}

//	public String getOpzStringa() {
//		return opzStringa;
//	}
//	public void setOpzStringa(String opzStringa) {
//		this.opzStringa = opzStringa;
//	}
//
//	public String getOpzNote() {
//		return opzNote;
//	}
//	public void setOpzNote(String opzNote) {
//		this.opzNote = opzNote;
//	}

	public List getListaCodThe() {
		return listaCodThe;
	}
	public void setListaCodThe(List listaCodThe) {
		this.listaCodThe = listaCodThe;
	}

//	public List getListaOpzTit() {
//		return listaOpzTit;
//	}
//	public void setListaOpzTit(List listaOpzTit) {
//		this.listaOpzTit = listaOpzTit;
//	}

	public List getListaOpzNoteThe() {
		return listaOpzNoteThe;
	}
	public void setListaOpzNoteThe(List listaOpzNoteThe) {
		this.listaOpzNoteThe = listaOpzNoteThe;
	}

//	public List getListaOpzStringa() {
//		return listaOpzStringa;
//	}
//	public void setListaOpzStringa(List listaOpzStringa) {
//		this.listaOpzStringa = listaOpzStringa;
//	}
//
//	public List getListaOpzNote() {
//		return listaOpzNote;
//	}
//	public void setListaOpzNote(List listaOpzNote) {
//		this.listaOpzNote = listaOpzNote;
//	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public List getListaOpzFormeRinvio() {
		return listaOpzFormeRinvio;
	}
	public void setListaOpzFormeRinvio(List listaOpzFormeRinvio) {
		this.listaOpzFormeRinvio = listaOpzFormeRinvio;
	}
	public List getListaOpzLegamiTitoloDiBiblio() {
		return listaOpzLegamiTitoloDiBiblio;
	}
	public void setListaOpzLegamiTitoloDiBiblio(
			List listaOpzLegamiTitoloDiBiblio) {
		this.listaOpzLegamiTitoloDiBiblio = listaOpzLegamiTitoloDiBiblio;
	}
	public List getListaOpzNoteTerminiColl() {
		return listaOpzNoteTerminiColl;
	}
	public void setListaOpzNoteTerminiColl(List listaOpzNoteTerminiColl) {
		this.listaOpzNoteTerminiColl = listaOpzNoteTerminiColl;
	}
	public List getListaOpzTerminiBiblio() {
		return listaOpzTerminiBiblio;
	}
	public void setListaOpzTerminiBiblio(List listaOpzTerminiBiblio) {
		this.listaOpzTerminiBiblio = listaOpzTerminiBiblio;
	}
	public List getListaOpzTerminiColl() {
		return listaOpzTerminiColl;
	}
	public void setListaOpzTerminiColl(List listaOpzTerminiColl) {
		this.listaOpzTerminiColl = listaOpzTerminiColl;
	}
	public List getListaOpzTitoli() {
		return listaOpzTitoli;
	}
	public void setListaOpzTitoli(List listaOpzTitoli) {
		this.listaOpzTitoli = listaOpzTitoli;
	}
	public String getOpzFormeRinvio() {
		return opzFormeRinvio;
	}
	public void setOpzFormeRinvio(String opzFormeRinvio) {
		this.opzFormeRinvio = opzFormeRinvio;
	}
	public String getOpzLegamiTitoloDiBiblio() {
		return opzLegamiTitoloDiBiblio;
	}
	public void setOpzLegamiTitoloDiBiblio(String opzLegamiTitoloDiBiblio) {
		this.opzLegamiTitoloDiBiblio = opzLegamiTitoloDiBiblio;
	}
	public String getOpzNoteTerminiColl() {
		return opzNoteTerminiColl;
	}
	public void setOpzNoteTerminiColl(String opzNoteTerminiColl) {
		this.opzNoteTerminiColl = opzNoteTerminiColl;
	}
	public String getOpzTerminiBiblio() {
		return opzTerminiBiblio;
	}
	public void setOpzTerminiBiblio(String opzTerminiBiblio) {
		this.opzTerminiBiblio = opzTerminiBiblio;
	}
	public String getOpzTerminiColl() {
		return opzTerminiColl;
	}
	public void setOpzTerminiColl(String opzTerminiColl) {
		this.opzTerminiColl = opzTerminiColl;
	}
	public String getOpzTitoli() {
		return opzTitoli;
	}
	public void setOpzTitoli(String opzTitoli) {
		this.opzTitoli = opzTitoli;
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

}
