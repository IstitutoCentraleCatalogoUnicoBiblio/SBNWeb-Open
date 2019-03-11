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

import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class BuoniRicercaParzialeForm extends ActionForm implements	AcquisizioniBaseFormIntf {

	private static final long serialVersionUID = -6156908671927630301L;
	private String codPolo = "";
	private String codBibl = "";
	private String numBuonoDa;
	private String numBuonoA;
	private String dataBuonoDa;
	private String dataBuonoA;

	private String tipoOrdine;
	private List listaTipoOrdine;
	private String anno;
	private String numero;
	private String esercizio;
	private String capitolo;
	private String codFornitore;
	private String fornitore;
	private FornitoreVO fornitoreVO;

	private boolean sessione;
	private boolean visibilitaIndietroLS = false;
	private boolean LSRicerca = false;

	private int elemXBlocchi = 10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean disabilitaTutto = false;
	private boolean rientroDaSif = false;
	private boolean gestBil = true;

	private ListaSuppBuoniOrdineVO parametri = new ListaSuppBuoniOrdineVO();

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public String getDataBuonoA() {
		return dataBuonoA;
	}

	public void setDataBuonoA(String dataBuonoA) {
		this.dataBuonoA = dataBuonoA;
	}

	public String getDataBuonoDa() {
		return dataBuonoDa;
	}

	public void setDataBuonoDa(String dataBuonoDa) {
		this.dataBuonoDa = dataBuonoDa;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

	public String getNumBuonoA() {
		return numBuonoA;
	}

	public void setNumBuonoA(String numBuonoA) {
		this.numBuonoA = numBuonoA;
	}

	public String getNumBuonoDa() {
		return numBuonoDa;
	}

	public void setNumBuonoDa(String numBuonoDa) {
		this.numBuonoDa = numBuonoDa;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.AcquisizioniBaseFormIntf
	 * #getFornitore()
	 */
	public String getFornitore() {
		return fornitore;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.AcquisizioniBaseFormIntf
	 * #setFornitore(java.lang.String)
	 */
	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.AcquisizioniBaseFormIntf
	 * #getCodFornitore()
	 */
	public String getCodFornitore() {
		return codFornitore;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.AcquisizioniBaseFormIntf
	 * #setCodFornitore(java.lang.String)
	 */
	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
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

	public boolean isRientroDaSif() {
		return rientroDaSif;
	}

	public void setRientroDaSif(boolean rientroDaSif) {
		this.rientroDaSif = rientroDaSif;
	}

	public boolean isGestBil() {
		return gestBil;
	}

	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public FornitoreVO getFornitoreVO() {
		return fornitoreVO;
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		this.fornitoreVO = fornitoreVO;
	}

	public ListaSuppBuoniOrdineVO getParametri() {
		return parametri;
	}

	public void setParametri(ListaSuppBuoniOrdineVO parametri) {
		this.parametri = parametri;
	}

}
