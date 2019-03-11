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
//	SBNWeb - Rifacimento ClientServer
//	FORM sintetica autori
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.documentofisico.possessori;

import it.iccu.sbn.ejb.vo.documentofisico.TreeElementViewPossessori;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class PossessoriAnaliticaForm extends ActionForm {

/**
	 * 
	 */
	private static final long serialVersionUID = 3122958551953849918L;

	//	private Integer linkProgressivo;
//	private int nRec;
//	private int elemXBlocchi;
//	private int numPrimo;
//	private int maxRighe;
//	private List listaTipiOrdinam;
//	private String tipoOrdinamSelez;
//	private List listaFormatoLista;
//	private String formatoListaSelez;
//	private int bloccoSelezionato=0;
//	private String idLista;
//	private int totBlocchi;
//	private int totRighe;
//	private int elemPerBlocchi;
//	private boolean abilitaBottoneCarBlocchi;
	private TreeElementViewPossessori treeElementViewPossessori;

	private String selezRadio;
	private String[] selezCheck;


	private List listaPossessori ;

//	public Integer getLinkProgressivo() {
//		return linkProgressivo;
//	}
//
//	public void setLinkProgressivo(Integer linkProgressivo) {
//		this.linkProgressivo = linkProgressivo;
//	}
//
//	public int getNRec() {
//		return nRec;
//	}
//
//	public void setNRec(int rec) {
//		nRec = rec;
//	}
//
//	public int getElemXBlocchi() {
//		return elemXBlocchi;
//	}
//
//	public void setElemXBlocchi(int elemXBlocchi) {
//		this.elemXBlocchi = elemXBlocchi;
//	}
//
//	public int getNumPrimo() {
//		return numPrimo;
//	}
//
//	public void setNumPrimo(int numPrimo) {
//		this.numPrimo = numPrimo;
//	}
//
//	public int getMaxRighe() {
//		return maxRighe;
//	}
//
//	public void setMaxRighe(int maxRighe) {
//		this.maxRighe = maxRighe;
//	}
//
//	public List getListaTipiOrdinam() {
//		return listaTipiOrdinam;
//	}
//
//	public void setListaTipiOrdinam(List listaTipiOrdinam) {
//		this.listaTipiOrdinam = listaTipiOrdinam;
//	}
//
//	public String getTipoOrdinamSelez() {
//		return tipoOrdinamSelez;
//	}
//
//	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
//		this.tipoOrdinamSelez = tipoOrdinamSelez;
//	}
//
//	public List getListaFormatoLista() {
//		return listaFormatoLista;
//	}
//
//	public void setListaFormatoLista(List listaFormatoLista) {
//		this.listaFormatoLista = listaFormatoLista;
//	}
//
//	public String getFormatoListaSelez() {
//		return formatoListaSelez;
//	}
//
//	public void setFormatoListaSelez(String formatoListaSelez) {
//		this.formatoListaSelez = formatoListaSelez;
//	}
//
//	public int getBloccoSelezionato() {
//		return bloccoSelezionato;
//	}
//
//	public void setBloccoSelezionato(int bloccoSelezionato) {
//		this.bloccoSelezionato = bloccoSelezionato;
//	}
//
//	public String getIdLista() {
//		return idLista;
//	}
//
//	public void setIdLista(String idLista) {
//		this.idLista = idLista;
//	}
//
//	public int getTotBlocchi() {
//		return totBlocchi;
//	}
//
//	public void setTotBlocchi(int totBlocchi) {
//		this.totBlocchi = totBlocchi;
//	}
//
//	public int getTotRighe() {
//		return totRighe;
//	}
//
//	public void setTotRighe(int totRighe) {
//		this.totRighe = totRighe;
//	}
//
//	public int getElemPerBlocchi() {
//		return elemPerBlocchi;
//	}
//
//	public void setElemPerBlocchi(int elemPerBlocchi) {
//		this.elemPerBlocchi = elemPerBlocchi;
//	}
//
//	public boolean isAbilitaBottoneCarBlocchi() {
//		return abilitaBottoneCarBlocchi;
//	}
//
//	public void setAbilitaBottoneCarBlocchi(boolean abilitaBottoneCarBlocchi) {
//		this.abilitaBottoneCarBlocchi = abilitaBottoneCarBlocchi;
//	}

	public List getListaPossessori() {
		return listaPossessori;
	}

	public void setListaPossessori(List listaPossessori) {
		this.listaPossessori = listaPossessori;
	}

	public String getSelezRadio() {
		return selezRadio;
	}

	public void setSelezRadio(String selezRadio) {
		this.selezRadio = selezRadio;
	}

	public String[] getSelezCheck() {
		return selezCheck;
	}

	public void setSelezCheck(String[] selezCheck) {
		this.selezCheck = selezCheck;
	}

	public TreeElementViewPossessori getTreeElementViewPossessori() {
		return treeElementViewPossessori;
	}

	public void setTreeElementViewPossessori(
			TreeElementViewPossessori treeElementViewPossessori) {
		this.treeElementViewPossessori = treeElementViewPossessori;
	}

}
