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

import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaBibliotecheAffiliateAcqForm  extends  AbstractBibliotecaForm    {


	private static final long serialVersionUID = 8481204005624985248L;
	//inizio
	private List<ComboVO> listaBiblAffOld;
	private List<StrutturaCombo> listaBiblAff;
	private String[] selectedBiblAff;
	private String radioSel;
	private boolean sessione;
	private boolean risultatiPresenti=true;
	private int numBibl;
//	private ListaSuppOrdiniVO sifOrdini;
//	private DescrittoreBloccoVO ultimoBlocco;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );


	public void reset(ActionMapping mapping, HttpServletRequest request) {
		   this.selectedBiblAff=new String[0];
		   this.setRisultatiPresenti(true);
	}
	public int getNumBibl() {
		return numBibl;
	}
	public void setNumBibl(int numBibl) {
		this.numBibl = numBibl;
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
	public String[] getSelectedBiblAff() {
		return selectedBiblAff;
	}
	public void setSelectedBiblAff(String[] selectedBiblAff) {
		this.selectedBiblAff = selectedBiblAff;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public List<StrutturaCombo> getListaBiblAff() {
		return listaBiblAff;
	}
	public void setListaBiblAff(List<StrutturaCombo> listaBiblAff) {
		this.listaBiblAff = listaBiblAff;
	}
	public List<ComboVO> getListaBiblAffOld() {
		return listaBiblAffOld;
	}
	public void setListaBiblAffOld(List<ComboVO> listaBiblAffOld) {
		this.listaBiblAffOld = listaBiblAffOld;
	}

//	 fine


}
