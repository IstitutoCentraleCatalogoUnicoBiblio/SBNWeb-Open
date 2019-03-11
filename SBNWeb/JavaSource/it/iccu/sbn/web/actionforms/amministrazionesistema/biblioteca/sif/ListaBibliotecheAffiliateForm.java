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
package it.iccu.sbn.web.actionforms.amministrazionesistema.biblioteca.sif;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheBaseVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ListaBibliotecheAffiliateForm extends ActionForm {


	private static final long serialVersionUID = -3760897693445454685L;
	private boolean sessione = false;
	private int bloccoCorrente;
	private int totRighe;
	private int totBlocchi;
	private int maxRighe;
	private String idLista;
	private List<BibliotecaVO> listaBiblioteche;
	private Integer selectedBiblio;
	private Integer[] multiBiblio;
	private SIFListaBibliotecheBaseVO attivazioneSIF;

	public SIFListaBibliotecheBaseVO getAttivazioneSIF() {
		return attivazioneSIF;
	}

	public void setAttivazioneSIF(SIFListaBibliotecheBaseVO attivazioneSIF) {
		this.attivazioneSIF = attivazioneSIF;
	}

	public List<BibliotecaVO> getListaBiblioteche() {
		return listaBiblioteche;
	}

	public void setListaBiblioteche(List<BibliotecaVO> listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}

	public int getBloccoCorrente() {
		return bloccoCorrente;
	}

	public void setBloccoCorrente(int bloccoCorrente) {
		this.bloccoCorrente = bloccoCorrente;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public Integer getSelectedBiblio() {
		return selectedBiblio;
	}

	public void setSelectedBiblio(Integer selectedBiblio) {
		this.selectedBiblio = selectedBiblio;
	}

	public Integer[] getMultiBiblio() {
		return multiBiblio;
	}

	public void setMultiBiblio(Integer[] multiBiblio) {
		this.multiBiblio = multiBiblio;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.multiBiblio = null;
	}

}
