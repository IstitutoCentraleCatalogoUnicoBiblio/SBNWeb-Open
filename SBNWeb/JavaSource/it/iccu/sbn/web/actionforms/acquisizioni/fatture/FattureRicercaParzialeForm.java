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
package it.iccu.sbn.web.actionforms.acquisizioni.fatture;

import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class FattureRicercaParzialeForm extends ActionForm implements AcquisizioniBaseFormIntf{

	private static final long serialVersionUID = 2512094271210427937L;

	private boolean LSRicerca=false;

	private String codiceBibl="";
	private String numFatt;
	private String statoFatt;
	List listaStatoFatt;
	private String dataFattDa;
	private String dataFattA;
	private String annoFatt;
	private String progrFatt;
	private StrutturaCombo fornitore1;
	private FornitoreVO fornitoreVO;

	private String tipoFatt;
	List listaTipoFatt;
	private StrutturaTerna ordine;

	private List listaTipoOrdine;
	private boolean sessione;

	private boolean visibilitaIndietroLS=false;

	private int elemXBlocchi=10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean disabilitaTutto=false;
	private boolean rientroDaSif=false;




		public void reset(ActionMapping mapping, HttpServletRequest request) {
	    }

	    public ActionErrors validate(ActionMapping mapping,
	            HttpServletRequest request) {
	        ActionErrors errors = new ActionErrors();
	        return errors;
	    }

		public String getDataFattA() {
			return dataFattA;
		}

		public void setDataFattA(String dataFattA) {
			this.dataFattA = dataFattA;
		}

		public String getDataFattDa() {
			return dataFattDa;
		}

		public void setDataFattDa(String dataFattDa) {
			this.dataFattDa = dataFattDa;
		}


		public List getListaStatoFatt() {
			return listaStatoFatt;
		}

		public void setListaStatoFatt(List listaStatoFatt) {
			this.listaStatoFatt = listaStatoFatt;
		}

		public List getListaTipoFatt() {
			return listaTipoFatt;
		}

		public void setListaTipoFatt(List listaTipoFatt) {
			this.listaTipoFatt = listaTipoFatt;
		}

		public List getListaTipoOrdine() {
			return listaTipoOrdine;
		}

		public void setListaTipoOrdine(List listaTipoOrdine) {
			this.listaTipoOrdine = listaTipoOrdine;
		}

		public String getNumFatt() {
			return numFatt;
		}

		public void setNumFatt(String numFatt) {
			this.numFatt = numFatt;
		}

		public String getProgrFatt() {
			return progrFatt;
		}

		public void setProgrFatt(String progrFatt) {
			this.progrFatt = progrFatt;
		}

		public String getStatoFatt() {
			return statoFatt;
		}

		public void setStatoFatt(String statoFatt) {
			this.statoFatt = statoFatt;
		}

		public String getTipoFatt() {
			return tipoFatt;
		}

		public void setTipoFatt(String tipoFatt) {
			this.tipoFatt = tipoFatt;
		}


		public boolean isSessione() {
			return sessione;
		}

		public void setSessione(boolean sessione) {
			this.sessione = sessione;
		}

		public int getElemXBlocchi() {
			return elemXBlocchi;
		}

		public void setElemXBlocchi(int elemXBlocchi) {
			this.elemXBlocchi = elemXBlocchi;
		}

		public List getListaTipiOrdinam() {
			return listaTipiOrdinam;
		}

		public void setListaTipiOrdinam(List listaTipiOrdinam) {
			this.listaTipiOrdinam = listaTipiOrdinam;
		}

		public String getTipoOrdinamSelez() {
			return tipoOrdinamSelez;
		}

		public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
			this.tipoOrdinamSelez = tipoOrdinamSelez;
		}

		public boolean isVisibilitaIndietroLS() {
			return visibilitaIndietroLS;
		}

		public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
			this.visibilitaIndietroLS = visibilitaIndietroLS;
		}

		public String getCodiceBibl() {
			return codiceBibl;
		}

		public void setCodiceBibl(String codiceBibl) {
			this.codiceBibl = codiceBibl;
		}

		public StrutturaCombo getFornitore1() {
			return fornitore1;
		}

		public void setFornitore1(StrutturaCombo fornitore1) {
			this.fornitore1 = fornitore1;
		}

		public StrutturaTerna getOrdine() {
			return ordine;
		}

		public void setOrdine(StrutturaTerna ordine) {
			this.ordine = ordine;
		}

		public boolean isLSRicerca() {
			return LSRicerca;
		}

		public void setLSRicerca(boolean ricerca) {
			LSRicerca = ricerca;
		}

		public boolean isDisabilitaTutto() {
			return disabilitaTutto;
		}

		public void setDisabilitaTutto(boolean disabilitaTutto) {
			this.disabilitaTutto = disabilitaTutto;
		}

		public String getAnnoFatt() {
			return annoFatt;
		}

		public void setAnnoFatt(String annoFatt) {
			this.annoFatt = annoFatt;
		}

		public boolean isRientroDaSif() {
			return rientroDaSif;
		}

		public void setRientroDaSif(boolean rientroDaSif) {
			this.rientroDaSif = rientroDaSif;
		}

		public String getCodFornitore() {
			return fornitore1.getCodice();
		}

		public void setCodFornitore(String codFornitore) {
			if (this.fornitore1 == null){
				try {
					this.fornitore1 = new StrutturaCombo();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.fornitore1.setCodice(codFornitore);
		}

		public String getFornitore() {
			return fornitore1.getDescrizione();
		}

		public void setFornitore(String fornitore) {
			if (this.fornitore1 == null){
				try {
					this.fornitore1 = new StrutturaCombo();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.fornitore1.setDescrizione(fornitore);
		}
		public FornitoreVO getFornitoreVO() {
			return fornitoreVO;
		}

		public void setFornitoreVO(FornitoreVO fornitoreVO) {
			if (this.fornitoreVO == null){
				try {
					this.fornitoreVO = new FornitoreVO();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.setFornitoreVO(fornitoreVO);
		}
}
