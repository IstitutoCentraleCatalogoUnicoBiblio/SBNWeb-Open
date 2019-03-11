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
package it.iccu.sbn.web.actionforms.acquisizioni.gare;

import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.PartecipantiGaraVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class EsaminaGaraForm extends DynaValidatorForm {


	private static final long serialVersionUID = -8144445720807064849L;
	private String tipoInvio;
	private List listaTipoInvio;

	private String statoPartecipanteGara;
	private List listaStatoPartecipanteGara;

	private GaraVO richOff;

	private int numRighePartecipanti;
	private int[] radioPartecipante;

	private String statoRichiestaOfferta;
	private List listaStatoRichiestaOfferta;

	private List<PartecipantiGaraVO> elencaRighePartecipanti;

	private boolean caricamentoIniziale=false;

	private boolean sessione;

	private boolean conferma = false;
	private String pressioneBottone = null;

	private boolean disabilitaTutto=false;

	private List<ListaSuppGaraVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;

	private Integer clicNotaPrg;
	private Integer clicRispPrg;
	private boolean esaminaInibito=false;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		//super.reset(mapping, request);
		//  this.set("elencaImpegni",new BilancioDettVO[0]);
		//this.set("conferma",false);
		//this.set("disabilitaTutto",false);
		//this.set("numImpegni",0);

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

	public List<PartecipantiGaraVO> getElencaRighePartecipanti() {
		return elencaRighePartecipanti;
	}

	public void setElencaRighePartecipanti(
			List<PartecipantiGaraVO> elencaRighePartecipanti) {
		this.elencaRighePartecipanti = elencaRighePartecipanti;
	}

	public boolean isEnableScorrimento() {
		return enableScorrimento;
	}

	public void setEnableScorrimento(boolean enableScorrimento) {
		this.enableScorrimento = enableScorrimento;
	}



	public List getListaStatoPartecipanteGara() {
		return listaStatoPartecipanteGara;
	}

	public void setListaStatoPartecipanteGara(List listaStatoPartecipanteGara) {
		this.listaStatoPartecipanteGara = listaStatoPartecipanteGara;
	}

	public List getListaStatoRichiestaOfferta() {
		return listaStatoRichiestaOfferta;
	}

	public void setListaStatoRichiestaOfferta(List listaStatoRichiestaOfferta) {
		this.listaStatoRichiestaOfferta = listaStatoRichiestaOfferta;
	}

	public List getListaTipoInvio() {
		return listaTipoInvio;
	}

	public void setListaTipoInvio(List listaTipoInvio) {
		this.listaTipoInvio = listaTipoInvio;
	}

	public int getNumRighePartecipanti() {
		return numRighePartecipanti;
	}

	public void setNumRighePartecipanti(int numRighePartecipanti) {
		this.numRighePartecipanti = numRighePartecipanti;
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

	public int[] getRadioPartecipante() {
		return radioPartecipante;
	}

	public void setRadioPartecipante(int[] radioPartecipante) {
		this.radioPartecipante = radioPartecipante;
	}

	public GaraVO getRichOff() {
		return richOff;
	}

	public void setRichOff(GaraVO richOff) {
		this.richOff = richOff;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getStatoPartecipanteGara() {
		return statoPartecipanteGara;
	}

	public void setStatoPartecipanteGara(String statoPartecipanteGara) {
		this.statoPartecipanteGara = statoPartecipanteGara;
	}

	public String getStatoRichiestaOfferta() {
		return statoRichiestaOfferta;
	}

	public void setStatoRichiestaOfferta(String statoRichiestaOfferta) {
		this.statoRichiestaOfferta = statoRichiestaOfferta;
	}

	public String getTipoInvio() {
		return tipoInvio;
	}

	public void setTipoInvio(String tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
	}

	public List<ListaSuppGaraVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(List<ListaSuppGaraVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
	}

	public Integer getClicNotaPrg() {
		return clicNotaPrg;
	}

	public void setClicNotaPrg(Integer clicNotaPrg) {
		this.clicNotaPrg = clicNotaPrg;
	}

	public Integer getClicRispPrg() {
		return clicRispPrg;
	}

	public void setClicRispPrg(Integer clicRispPrg) {
		this.clicRispPrg = clicRispPrg;
	}

	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}

	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}



}
