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
package it.iccu.sbn.web.actionforms.servizi.segnature;


import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaSegnatureForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -7116413782968745816L;
	private boolean sessione = false;
	boolean conferma = false;
	private List listaSegnature;

	private Integer[] codSelSegn=new Integer[0];;
	private Integer[] codSelSegnConferma;

	private String codSelSegnSing="";
	private RangeSegnatureVO anaSegn = new RangeSegnatureVO(0,0, "", "", "", "", "", "");
	private Integer[] selectedOccup;


	private boolean abilitaBlocchi;
	private int totBlocchi;




	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	//	codSelSegn = new Integer[]{};
	}


	/**
	 * @return Returns the totBlocchi.
	 */
	public int getTotBlocchi() {
		return totBlocchi;
	}
	/**
	 * @param totBlocchi The totBlocchi to set.
	 */
	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}
	public void setAbilitaBlocchi(boolean b) {
		abilitaBlocchi = b;
	}
	/**
	 * @return Returns the abilitaBlocchi.
	 */
	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public Integer[] getCodSelSegn() {
		return codSelSegn;
	}

	public void setCodSelSegn(Integer[] codSelSegn) {
		this.codSelSegn = codSelSegn;
	}

	public List getListaSegnature() {
		return listaSegnature;
	}

	public void setListaSegnature(List listaSegnature) {
		this.listaSegnature = listaSegnature;
	}

	public RangeSegnatureVO getAnaSegn() {
		return anaSegn;
	}

	public void setAnaSegn(RangeSegnatureVO anaSegn) {
		this.anaSegn = anaSegn;
	}

	public String getCodSelSegnSing() {
		return codSelSegnSing;
	}

	public void setCodSelSegnSing(String codSelSegnSing) {
		this.codSelSegnSing = codSelSegnSing;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}


	public Integer[] getCodSelSegnConferma() {
		return codSelSegnConferma;
	}


	public void setCodSelSegnConferma(Integer[] codSelSegnConferma) {
		this.codSelSegnConferma = codSelSegnConferma;
	}


	public Integer[] getSelectedOccup() {
		return selectedOccup;
	}


	public void setSelectedOccup(Integer[] selectedOccup) {
		this.selectedOccup = selectedOccup;
	}

}
