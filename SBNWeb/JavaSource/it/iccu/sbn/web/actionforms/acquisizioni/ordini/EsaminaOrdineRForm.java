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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class EsaminaOrdineRForm extends DynaValidatorForm {


	private static final long serialVersionUID = 3410815678650027450L;

	public enum TipoOperazione {
		SALVA,
		INV_DUPLICATO;
	}

	public static final String LOCK_VOLUME = "lockVol";
	public static final String LISTA_INVENTARI = "elencaInventari";
	public static final String SELECTED = "radioInv";

	private final Timestamp creationTime;

	private String scegliTAB;
	private OrdiniVO datiOrdine;

	private List listaNatura;
	private String natura;

	private List listaValuta;
	private String valuta;

	private List listaStato;
	private String stato;

	private List listaSerie;
	private String serie;

	private List listaTipoImpegno;
	private String tipoImpegno;

	private List listaTipoInvio;
	private String tipoInvio;

	private List listaUrg;
	private String urg;

	private List listaPeriodo;
	private String periodo;

	private String provenienza;

	private Integer clicNotaPrg;

	private boolean sessione;
	private boolean conferma = false;
	private boolean operazioneModifica = false;
	private boolean ordineApertoAbilitaInput = false;
	private boolean biblioNONCentroSistema = true;
	private boolean disabilitazioneSezioneAbbonamento = true;
	private boolean submitDinamico = false;

	private boolean disabilitaTutto = false;
	private boolean visibilitaIndietroLS = false;
	private boolean provenienzaVAIA = false;

	private int radioInv;
	private List<StrutturaInventariOrdVO> elencaInventari;
	private List<StrutturaInventariOrdVO> listaInventariOld = new ArrayList<StrutturaInventariOrdVO>();
	private int numRigheInv;

	private boolean caricamentoIniziale = false;

	private boolean inserimento = false;

	private String data;
	private List<ComboVO> listaTipoData;
	private String tipoData;

	private boolean rfidEnabled;
	private TipoOperazione operazione;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return new ActionErrors();
	}

	{
		//almaviva5_20131009 evolutive google2
		this.creationTime = DaoManager.now();
	}

	public OrdiniVO getDatiOrdine() {
		return datiOrdine;
	}

	public void setDatiOrdine(OrdiniVO datiOrdine) {
		this.datiOrdine = datiOrdine;
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

	public List getListaValuta() {
		return listaValuta;
	}

	public void setListaValuta(List listaValuta) {
		this.listaValuta = listaValuta;
	}

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

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
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

	public int getNumRigheInv() {
		return numRigheInv;
	}

	public void setNumRigheInv(int numRigheInv) {
		this.numRigheInv = numRigheInv;
	}

	public int getRadioInv() {
		return radioInv;
	}

	public void setRadioInv(int radioInv) {
		this.radioInv = radioInv;
	}

	public boolean isInserimento() {
		return inserimento;
	}

	public void setInserimento(boolean inserimento) {
		this.inserimento = inserimento;
	}

	public Integer getClicNotaPrg() {
		return clicNotaPrg;
	}

	public void setClicNotaPrg(Integer clicNotaPrg) {
		this.clicNotaPrg = clicNotaPrg;
	}

	public List<StrutturaInventariOrdVO> getElencaInventari() {
		return elencaInventari;
	}

	public void setElencaInventari(
			List<StrutturaInventariOrdVO> elencaInventari) {
		this.elencaInventari = elencaInventari;
	}

	public List getListaSerie() {
		return listaSerie;
	}

	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = ValidazioneDati.trimOrNull(data);
	}

	public List<ComboVO> getListaTipoData() {
		return listaTipoData;
	}

	public void setListaTipoData(List<ComboVO> listaTipoData) {
		this.listaTipoData = listaTipoData;
	}

	public String getTipoData() {
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public boolean isRfidEnabled() {
		return rfidEnabled;
	}

	public void setRfidEnabled(boolean rfidEnabled) {
		this.rfidEnabled = rfidEnabled;
	}

	public List<StrutturaInventariOrdVO> getListaInventariOld() {
		return listaInventariOld;
	}

	public void setListaInventariOld(
			List<StrutturaInventariOrdVO> listaInventariOld) {
		this.listaInventariOld = listaInventariOld;
	}

	public TipoOperazione getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazione operazione) {
		this.operazione = operazione;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

}
