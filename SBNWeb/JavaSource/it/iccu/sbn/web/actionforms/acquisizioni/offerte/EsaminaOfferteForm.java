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
package it.iccu.sbn.web.actionforms.acquisizioni.offerte;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EsaminaOfferteForm extends ActionForm {

	private static final long serialVersionUID = 3081069622493586660L;
	private String scegliTAB;
	private List listaLingue;
	private String lingue;
	private List listaPaesi;
	private String paesi;
	private List listaTipoProvenienza;
	private String tipoProvenienza;
	private List listaStatoSuggOfferta;
	private String statoSuggOfferta;
	private List listaTipoDataOfferta;
	private String tipoDataOfferta;
	private List listaTipoPrezzoOfferta;
	private String tipoPrezzoOfferta;
	private List listaTipoResponsabilita;
	private String tipoResponsabilita;




	private List listaAutori;
	private int numRigheAut;
	private List listaSerie;
	private int numRigheSer;
	private List listaSoggetto;
	private int numRigheSog;
	private List listaClassificazione;
	private int numRigheCla;
	private List listaNoteEDI;
	private int numRigheNot;
	private List listaISBD;
	private int numRigheIsb;


	private OffertaFornitoreVO datiOffertaForn;

	private boolean caricamentoIniziale=false;


	private boolean sessione;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto=false;
	private List<ListaSuppOffertaFornitoreVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public String getScegliTAB() {
		return scegliTAB;
	}

	public void setScegliTAB(String scegliTAB) {
		this.scegliTAB = scegliTAB;
	}

	public String getLingue() {
		return lingue;
	}

	public void setLingue(String lingue) {
		this.lingue = lingue;
	}

	public List getListaLingue() {
		return listaLingue;
	}

	public void setListaLingue(List listaLingue) {
		this.listaLingue = listaLingue;
	}

	public List getListaPaesi() {
		return listaPaesi;
	}

	public void setListaPaesi(List listaPaesi) {
		this.listaPaesi = listaPaesi;
	}

	public String getPaesi() {
		return paesi;
	}

	public void setPaesi(String paesi) {
		this.paesi = paesi;
	}

	public OffertaFornitoreVO getDatiOffertaForn() {
		return datiOffertaForn;
	}

	public void setDatiOffertaForn(OffertaFornitoreVO datiOffertaForn) {
		this.datiOffertaForn = datiOffertaForn;
	}

	public List getListaAutori() {
		return listaAutori;
	}

	public void setListaAutori(List listaAutori) {
		this.listaAutori = listaAutori;
	}

	public List getListaClassificazione() {
		return listaClassificazione;
	}

	public void setListaClassificazione(List listaClassificazione) {
		this.listaClassificazione = listaClassificazione;
	}

	public List getListaISBD() {
		return listaISBD;
	}

	public void setListaISBD(List listaISBD) {
		this.listaISBD = listaISBD;
	}

	public List getListaNoteEDI() {
		return listaNoteEDI;
	}

	public void setListaNoteEDI(List listaNoteEDI) {
		this.listaNoteEDI = listaNoteEDI;
	}

	public List getListaSerie() {
		return listaSerie;
	}

	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}

	public List getListaSoggetto() {
		return listaSoggetto;
	}

	public void setListaSoggetto(List listaSoggetto) {
		this.listaSoggetto = listaSoggetto;
	}

	public int getNumRigheAut() {
		return numRigheAut;
	}

	public void setNumRigheAut(int numRigheAut) {
		this.numRigheAut = numRigheAut;
	}

	public int getNumRigheCla() {
		return numRigheCla;
	}

	public void setNumRigheCla(int numRigheCla) {
		this.numRigheCla = numRigheCla;
	}

	public int getNumRigheIsb() {
		return numRigheIsb;
	}

	public void setNumRigheIsb(int numRigheIsb) {
		this.numRigheIsb = numRigheIsb;
	}

	public int getNumRigheNot() {
		return numRigheNot;
	}

	public void setNumRigheNot(int numRigheNot) {
		this.numRigheNot = numRigheNot;
	}

	public int getNumRigheSer() {
		return numRigheSer;
	}

	public void setNumRigheSer(int numRigheSer) {
		this.numRigheSer = numRigheSer;
	}

	public int getNumRigheSog() {
		return numRigheSog;
	}

	public void setNumRigheSog(int numRigheSog) {
		this.numRigheSog = numRigheSog;
	}

	public List getListaStatoSuggOfferta() {
		return listaStatoSuggOfferta;
	}

	public void setListaStatoSuggOfferta(List listaStatoSuggOfferta) {
		this.listaStatoSuggOfferta = listaStatoSuggOfferta;
	}

	public List getListaTipoDataOfferta() {
		return listaTipoDataOfferta;
	}

	public void setListaTipoDataOfferta(List listaTipoDataOfferta) {
		this.listaTipoDataOfferta = listaTipoDataOfferta;
	}

	public List getListaTipoPrezzoOfferta() {
		return listaTipoPrezzoOfferta;
	}

	public void setListaTipoPrezzoOfferta(List listaTipoPrezzoOfferta) {
		this.listaTipoPrezzoOfferta = listaTipoPrezzoOfferta;
	}

	public List getListaTipoProvenienza() {
		return listaTipoProvenienza;
	}

	public void setListaTipoProvenienza(List listaTipoProvenienza) {
		this.listaTipoProvenienza = listaTipoProvenienza;
	}

	public String getStatoSuggOfferta() {
		return statoSuggOfferta;
	}

	public void setStatoSuggOfferta(String statoSuggOfferta) {
		this.statoSuggOfferta = statoSuggOfferta;
	}

	public String getTipoDataOfferta() {
		return tipoDataOfferta;
	}

	public void setTipoDataOfferta(String tipoDataOfferta) {
		this.tipoDataOfferta = tipoDataOfferta;
	}

	public String getTipoPrezzoOfferta() {
		return tipoPrezzoOfferta;
	}

	public void setTipoPrezzoOfferta(String tipoPrezzoOfferta) {
		this.tipoPrezzoOfferta = tipoPrezzoOfferta;
	}

	public String getTipoProvenienza() {
		return tipoProvenienza;
	}

	public void setTipoProvenienza(String tipoProvenienza) {
		this.tipoProvenienza = tipoProvenienza;
	}

	public List getListaTipoResponsabilita() {
		return listaTipoResponsabilita;
	}

	public void setListaTipoResponsabilita(List listaTipoResponsabilita) {
		this.listaTipoResponsabilita = listaTipoResponsabilita;
	}

	public String getTipoResponsabilita() {
		return tipoResponsabilita;
	}

	public void setTipoResponsabilita(String tipoResponsabilita) {
		this.tipoResponsabilita = tipoResponsabilita;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
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

	public boolean isEnableScorrimento() {
		return enableScorrimento;
	}

	public void setEnableScorrimento(boolean enableScorrimento) {
		this.enableScorrimento = enableScorrimento;
	}

	public List<ListaSuppOffertaFornitoreVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(
			List<ListaSuppOffertaFornitoreVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
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




}
