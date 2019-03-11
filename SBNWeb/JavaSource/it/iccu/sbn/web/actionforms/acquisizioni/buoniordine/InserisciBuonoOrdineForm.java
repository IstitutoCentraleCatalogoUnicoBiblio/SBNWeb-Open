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
package it.iccu.sbn.web.actionforms.acquisizioni.buoniordine;

import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class InserisciBuonoOrdineForm extends DynaValidatorForm implements
		AcquisizioniBaseFormIntf {


	private static final long serialVersionUID = -663844938184003484L;

	private BuoniOrdineVO buono;

	private String statoBuono;
	private List listaStatoBuono;

	private String tipoOrdine;
	private List listaTipoOrdine;

	private String stato;
	private List listaStato;

	private int eleIns = 999; // indice elemento inserito

	private int numOrdini;
	private List<OrdiniVO> elencaBuoni;
	private int[] radioOrd;

	private boolean sessione;
	private boolean numAuto = true;

	private boolean conferma = false;
	private String pressioneBottone = null;

	private boolean disabilitaTutto = false;
	private boolean visibilitaIndietroLS = false;
	private boolean gestBil = true;

	private String codFornitore;

	private String fornitore;
	private FornitoreVO fornitoreVO;

	private ListaSuppBuoniOrdineVO parametri = new ListaSuppBuoniOrdineVO();

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// super.reset(mapping, request);
		// this.set("elencaImpegni",new BilancioDettVO[0]);
		// this.set("visibilitaIndietroLS",false);
		this.set("conferma", false);
		this.set("disabilitaTutto", false);
		// this.set("numImpegni",0);

	}

	public BuoniOrdineVO getBuono() {
		return buono;
	}

	public void setBuono(BuoniOrdineVO buono) {
		this.buono = buono;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public List<OrdiniVO> getElencaBuoni() {
		return elencaBuoni;
	}

	public void setElencaBuoni(List<OrdiniVO> elencaBuoni) {
		this.elencaBuoni = elencaBuoni;
	}

	public List getListaStato() {
		return listaStato;
	}

	public void setListaStato(List listaStato) {
		this.listaStato = listaStato;
	}

	public List getListaStatoBuono() {
		return listaStatoBuono;
	}

	public void setListaStatoBuono(List listaStatoBuono) {
		this.listaStatoBuono = listaStatoBuono;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

	public int getNumOrdini() {
		return numOrdini;
	}

	public void setNumOrdini(int numOrdini) {
		this.numOrdini = numOrdini;
	}

	public String getPressioneBottone() {
		return pressioneBottone;
	}

	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
	}

	public int[] getRadioOrd() {
		return radioOrd;
	}

	public void setRadioOrd(int[] radioOrd) {
		this.radioOrd = radioOrd;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getStatoBuono() {
		return statoBuono;
	}

	public void setStatoBuono(String statoBuono) {
		this.statoBuono = statoBuono;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public int getEleIns() {
		return eleIns;
	}

	public void setEleIns(int eleIns) {
		this.eleIns = eleIns;
	}

	public boolean isNumAuto() {
		return numAuto;
	}

	public void setNumAuto(boolean numAuto) {
		this.numAuto = numAuto;
	}

	public boolean isGestBil() {
		return gestBil;
	}

	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}

	// public String getCodFornitore() {
	// return codFornitore;
	// }
	//
	// public String getFornitore() {
	// return fornitore;
	// }
	//
	// public void setCodFornitore(String codFornitore) {
	// this.codFornitore = codFornitore;
	//
	// }
	//
	// public void setFornitore(String fornitore) {
	// this.fornitore = fornitore;
	// }

	public String getCodFornitore() {
		BuoniOrdineVO bo = (BuoniOrdineVO) this.get("buono");
		return bo.getFornitore().getCodice();
	}

	public String getFornitore() {
		BuoniOrdineVO bo = (BuoniOrdineVO) this.get("buono");
		return bo.getFornitore().getDescrizione();
	}

	public void setCodFornitore(String codFornitore) {
		BuoniOrdineVO bo = (BuoniOrdineVO) this.get("buono");
		bo.getFornitore().setCodice(codFornitore);

	}

	public void setFornitore(String fornitore) {
		BuoniOrdineVO bo = (BuoniOrdineVO) this.get("buono");
		bo.getFornitore().setDescrizione(fornitore);
	}

	public FornitoreVO getFornitoreVO() {
		BuoniOrdineVO bo = (BuoniOrdineVO) this.get("buono");
		return bo.getAnagFornitore();
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		BuoniOrdineVO bo = (BuoniOrdineVO) this.get("buono");
		bo.setAnagFornitore(fornitoreVO);
	}

	public void setParametri(ListaSuppBuoniOrdineVO parametri) {
		this.parametri = parametri;
	}

	public ListaSuppBuoniOrdineVO getParametri() {
		return parametri;
	}

}
