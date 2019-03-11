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
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


public class EsaminaFatturaNCForm extends ActionForm  {

	private static final long serialVersionUID = -7669006407099432222L;
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

	private int numRigheFatt;
	private int radioRFattNC;

	private Integer clicNotaPrg;

	private Integer numRigaBottone;

	private FatturaVO datiFatturaNC;
	private FatturaVO datiFatturaOld;
	private List<StrutturaFatturaVO> elencaRigheFatt;
	private boolean sessione;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto=false;
	private List<ListaSuppFatturaVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;
	private boolean controllata = false;
	private boolean pagata = false;
	private boolean fatturaSalvata = false;

	private boolean caricamentoIniziale=false;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );

	private boolean esaminaInibito=false;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// super.reset(mapping, request);
		// this.set("elencaRigheFatt",new StrutturaFatturaVO[0]);
		// this.set("conferma",false);
		// this.set("disabilitaTutto",false);
		// this.set("numRigheFatt",0);
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

	public List<StrutturaFatturaVO> getElencaRigheFatt() {
		return elencaRigheFatt;
	}

	public void setElencaRigheFatt(List<StrutturaFatturaVO> elencaRigheFatt) {
		this.elencaRigheFatt = elencaRigheFatt;
	}

	public boolean isEnableScorrimento() {
		return enableScorrimento;
	}

	public void setEnableScorrimento(boolean enableScorrimento) {
		this.enableScorrimento = enableScorrimento;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public List<ListaSuppFatturaVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(List<ListaSuppFatturaVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
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

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}

	public String getPressioneBottone() {
		return pressioneBottone;
	}

	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
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

	public Integer getClicNotaPrg() {
		return clicNotaPrg;
	}

	public void setClicNotaPrg(Integer clicNotaPrg) {
		this.clicNotaPrg = clicNotaPrg;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
	}

	public boolean isControllata() {
		return controllata;
	}

	public void setControllata(boolean controllata) {
		this.controllata = controllata;
	}

	public boolean isPagata() {
		return pagata;
	}

	public void setPagata(boolean pagata) {
		this.pagata = pagata;
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

	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}

	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}

	public int getRadioRFattNC() {
		return radioRFattNC;
	}

	public void setRadioRFattNC(int radioRFattNC) {
		this.radioRFattNC = radioRFattNC;
	}

	public FatturaVO getDatiFatturaNC() {
		return datiFatturaNC;
	}

	public void setDatiFatturaNC(FatturaVO datiFatturaNC) {
		this.datiFatturaNC = datiFatturaNC;
	}



}
