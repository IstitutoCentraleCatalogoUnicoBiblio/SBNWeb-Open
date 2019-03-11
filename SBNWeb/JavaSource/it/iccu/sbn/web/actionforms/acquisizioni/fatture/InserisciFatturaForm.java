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

import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class InserisciFatturaForm extends DynaValidatorForm implements AcquisizioniBaseFormIntf{

	private static final long serialVersionUID = -8775034667238824481L;
	private String tipoOrdine;
	private List listaTipoOrdine;

	private String tipoImpegno;
	private List listaTipoImpegno;

	private String iva;
	private List listaIva;

	private String tipoFatt;
	List listaTipoFatt;

	private String valuta;
	List listaValuta;

	private String statoFatt;
	List listaStatoFatt;

	private Integer clicNotaPrg;

	private Integer numRigaBottone;

	private int numRigheFatt;

	private int radioRFatt;

	private boolean sessione;

	private FatturaVO datiFattura;

	private FatturaVO datiFatturaOld;

	private List<StrutturaFatturaVO> elencaRigheFatt;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto=false;
	private boolean visibilitaIndietroLS=false;
	private boolean gestBil=true;


	private boolean fatturaSalvata = false;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String codFornitore;
	private String fornitore;
	private FornitoreVO fornitoreVO;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.set("conferma",false);
		this.set("disabilitaTutto",false);
		//this.set("visibilitaIndietroLS",false);
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public FatturaVO getDatiFattura() {
		return datiFattura;
	}

	public void setDatiFattura(FatturaVO datiFattura) {
		this.datiFattura = datiFattura;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public List<StrutturaFatturaVO> getElencaRigheFatt() {
		return elencaRigheFatt;
	}

	public void setElencaRigheFatt(List<StrutturaFatturaVO> elencaRigheFatt) {
		this.elencaRigheFatt = elencaRigheFatt;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public List getListaIva() {
		return listaIva;
	}

	public void setListaIva(List listaIva) {
		this.listaIva = listaIva;
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

	public List getListaTipoImpegno() {
		return listaTipoImpegno;
	}

	public void setListaTipoImpegno(List listaTipoImpegno) {
		this.listaTipoImpegno = listaTipoImpegno;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

	public List getListaValuta() {
		return listaValuta;
	}

	public void setListaValuta(List listaValuta) {
		this.listaValuta = listaValuta;
	}

	public int getNumRigheFatt() {
		return numRigheFatt;
	}

	public void setNumRigheFatt(int numRigheFatt) {
		this.numRigheFatt = numRigheFatt;
	}

	public String getPressioneBottone() {
		return pressioneBottone;
	}

	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
	}

	public int getRadioRFatt() {
		return radioRFatt;
	}

	public void setRadioRFatt(int radioRFatt) {
		this.radioRFatt = radioRFatt;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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

	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public Integer getClicNotaPrg() {
		return clicNotaPrg;
	}

	public void setClicNotaPrg(Integer clicNotaPrg) {
		this.clicNotaPrg = clicNotaPrg;
	}

	public boolean isFatturaSalvata() {
		return fatturaSalvata;
	}

	public void setFatturaSalvata(boolean fatturaSalvata) {
		this.fatturaSalvata = fatturaSalvata;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}

	public Integer getNumRigaBottone() {
		return numRigaBottone;
	}

	public void setNumRigaBottone(Integer numRigaBottone) {
		this.numRigaBottone = numRigaBottone;
	}

	public FatturaVO getDatiFatturaOld() {
		return datiFatturaOld;
	}

	public void setDatiFatturaOld(FatturaVO datiFatturaOld) {
		this.datiFatturaOld = datiFatturaOld;
	}

	public boolean isGestBil() {
		return gestBil;
	}

	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}

	public String getCodFornitore() {
		FatturaVO fattura = (FatturaVO)this.get("datiFattura");
		return fattura.getFornitoreFattura().getCodice();
	}

	public void setCodFornitore(String codFornitore) {
		FatturaVO fattura = (FatturaVO)this.get("datiFattura");
		fattura.getFornitoreFattura().setCodice(codFornitore);

	}

	public String getFornitore() {
		FatturaVO fattura = (FatturaVO)this.get("datiFattura");
		return fattura.getFornitoreFattura().getDescrizione();
	}

	public void setFornitore(String fornitore) {
		FatturaVO fattura = (FatturaVO)this.get("datiFattura");
		fattura.getFornitoreFattura().setDescrizione(fornitore);
	}

	public FornitoreVO getFornitoreVO() {
		FatturaVO fattura = (FatturaVO)this.get("datiFattura");
		return fattura.getAnagFornitore();
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		FatturaVO fattura = (FatturaVO)this.get("datiFattura");
		fattura.setAnagFornitore(fornitoreVO);
	}


}
