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

import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class EsaminaOrdineForm extends OrdineBaseForm {

	private static final long serialVersionUID = -3313441993416714599L;
	private String scegliTAB;
	private List listaNatura;
	private String natura;

	private String valuta;

	private List listaStato;
	private String stato;

	private String contin; // VARIABILE LATO CLIENT PER CHECK

	private List listaTipoImpegno;
	private String tipoImpegno;

	private List listaTipoInvio;
	private String tipoInvio;

	private List listaUrg;
	private String urg;

	private List listaPeriodo;
	private String periodo;

	private String provenienza;

	private String denoBibl;
	private String bidNotiziaCorrente;

	public String getBidNotiziaCorrente() {
		return bidNotiziaCorrente;
	}

	public void setBidNotiziaCorrente(String bidNotiziaCorrente) {
		this.bidNotiziaCorrente = bidNotiziaCorrente;
	}

	private boolean sessione;
	private boolean operazioneModifica = false;
	private boolean ordineApertoAbilitaInput = false;
	private boolean biblioNONCentroSistema = true;
	private boolean disabilitazioneSezioneAbbonamento = true;
	private boolean submitDinamico = false;
	private boolean statiCDL = false;
	private boolean disabilitaTutto = false;
	private boolean visibilitaIndietroLS = false;
	private boolean provenienzaVAIA = false;
	private boolean ordineCompletato = false;
	private boolean adeguamentoPrezzo = false;

	private boolean gestBil = true;
	private boolean gestSez = true;
	private boolean gestProf = true;

	private boolean caricamentoIniziale = false;

	private int radioInv;

	private List<StrutturaInventariOrdVO> elencaInventari;
	private int numRigheInv;

	private boolean bottoneAssociaInvPremuto = false;

	/* private EsaminaOrdineRForm esaordR = new EsaminaOrdineRForm(); */

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo
		/*
		 * if (this.isSubmitDinamico()) { if (datiOrdine.isContinuativo()) {
		 * datiOrdine.setContinuativo(true);
		 *
		 * } else { datiOrdine.setContinuativo(false);
		 *
		 * }
		 *
		 * }
		 */
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		/*
		 * if (!this.isCaricamentoIniziale() ) {
		 * datiOrdine.setContinuativo(false); } else if (
		 * this.isSubmitDinamico() ) { datiOrdine.setContinuativo(false); } else
		 * if ( !this.isConferma() && this.isSubmitDinamico()) {
		 * datiOrdine.setContinuativo(false); } else if ( !this.isConferma()) {
		 * datiOrdine.setContinuativo(false); }
		 */
	}

	public List getListaNatura() {
		return listaNatura;
	}

	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}

	public List getListaPeriodo() {
		return listaPeriodo;
	}

	public void setListaPeriodo(List listaPeriodo) {
		this.listaPeriodo = listaPeriodo;
	}

	public List getListaStato() {
		return listaStato;
	}

	public void setListaStato(List listaStato) {
		this.listaStato = listaStato;
	}

	public List getListaTipoImpegno() {
		return listaTipoImpegno;
	}

	public void setListaTipoImpegno(List listaTipoImpegno) {
		this.listaTipoImpegno = listaTipoImpegno;
	}

	public List getListaTipoInvio() {
		return listaTipoInvio;
	}

	public void setListaTipoInvio(List listaTipoInvio) {
		this.listaTipoInvio = listaTipoInvio;
	}

	public List getListaUrg() {
		return listaUrg;
	}

	public void setListaUrg(List listaUrg) {
		this.listaUrg = listaUrg;
	}

//	public List getListaValuta() {
//		return listaValuta;
//	}
//
//	public void setListaValuta(List listaValuta) {
//		this.listaValuta = listaValuta;
//	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getScegliTAB() {
		return scegliTAB;
	}

	public void setScegliTAB(String scegliTAB) {
		this.scegliTAB = scegliTAB;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}

	public String getTipoInvio() {
		return tipoInvio;
	}

	public void setTipoInvio(String tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	public String getUrg() {
		return urg;
	}

	public void setUrg(String urg) {
		this.urg = urg;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isOrdineApertoAbilitaInput() {
		return ordineApertoAbilitaInput;
	}

	public void setOrdineApertoAbilitaInput(boolean ordineApertoAbilitaInput) {
		this.ordineApertoAbilitaInput = ordineApertoAbilitaInput;
	}

	public boolean isBiblioNONCentroSistema() {
		return biblioNONCentroSistema;
	}

	public void setBiblioNONCentroSistema(boolean biblioNONCentroSistema) {
		this.biblioNONCentroSistema = biblioNONCentroSistema;
	}

	public boolean isOperazioneModifica() {
		return operazioneModifica;
	}

	public void setOperazioneModifica(boolean operazioneModifica) {
		this.operazioneModifica = operazioneModifica;
	}

	public boolean isDisabilitazioneSezioneAbbonamento() {
		return disabilitazioneSezioneAbbonamento;
	}

	public void setDisabilitazioneSezioneAbbonamento(
			boolean disabilitazioneSezioneAbbonamento) {
		this.disabilitazioneSezioneAbbonamento = disabilitazioneSezioneAbbonamento;
	}

	public boolean isSubmitDinamico() {
		return submitDinamico;
	}

	public void setSubmitDinamico(boolean submitDinamico) {
		this.submitDinamico = submitDinamico;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
	}

	public boolean isProvenienzaVAIA() {
		return provenienzaVAIA;
	}

	public void setProvenienzaVAIA(boolean provenienzaVAIA) {
		this.provenienzaVAIA = provenienzaVAIA;
	}

	public int getRadioInv() {
		return radioInv;
	}

	public void setRadioInv(int radioInv) {
		this.radioInv = radioInv;
	}

	public int getNumRigheInv() {
		return numRigheInv;
	}

	public void setNumRigheInv(int numRigheInv) {
		this.numRigheInv = numRigheInv;
	}

	public boolean isStatiCDL() {
		return statiCDL;
	}

	public void setStatiCDL(boolean statiCDL) {
		this.statiCDL = statiCDL;
	}

	public List<StrutturaInventariOrdVO> getElencaInventari() {
		return elencaInventari;
	}

	public void setElencaInventari(
			List<StrutturaInventariOrdVO> elencaInventari) {
		this.elencaInventari = elencaInventari;
	}

	public boolean isOrdineCompletato() {
		return ordineCompletato;
	}

	public void setOrdineCompletato(boolean ordineCompletato) {
		this.ordineCompletato = ordineCompletato;
	}

	public boolean isBottoneAssociaInvPremuto() {
		return bottoneAssociaInvPremuto;
	}

	public void setBottoneAssociaInvPremuto(boolean bottoneAssociaInvPremuto) {
		this.bottoneAssociaInvPremuto = bottoneAssociaInvPremuto;
	}

	public String getDenoBibl() {
		return denoBibl;
	}

	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}

	public String getContin() {
		return contin;
	}

	public void setContin(String contin) {
		this.contin = contin;
	}

	public boolean isAdeguamentoPrezzo() {
		return adeguamentoPrezzo;
	}

	public void setAdeguamentoPrezzo(boolean adeguamentoPrezzo) {
		this.adeguamentoPrezzo = adeguamentoPrezzo;
	}

	public boolean isGestBil() {
		return gestBil;
	}

	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}

	public boolean isGestProf() {
		return gestProf;
	}

	public void setGestProf(boolean gestProf) {
		this.gestProf = gestProf;
	}

	public boolean isGestSez() {
		return gestSez;
	}

	public void setGestSez(boolean gestSez) {
		this.gestSez = gestSez;
	}

	/*
	 * public EsaminaOrdineRForm getEsaordR() { return esaordR; }
	 *
	 *
	 * public void setEsaordR(EsaminaOrdineRForm esaordR) { this.esaordR =
	 * esaordR; }
	 */

}
