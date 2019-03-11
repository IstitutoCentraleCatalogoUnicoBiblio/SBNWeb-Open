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

import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class OrdineRicercaParzialeForm extends ActionForm implements
		AcquisizioniBaseFormIntf {

	private static final long serialVersionUID = -3153149430783115644L;
	private boolean LSRicerca = false;
	private List listaContinuativo;
	private String continuativo;
	private String tipoOrdine;
	private List listaTipoOrdine;
	private String codiceBibl = "";
	private String denoBibl = "";
	private String codPolo = "";
	private String biblAffil;
	private List<StrutturaCombo> listaBiblAffil;
	private String sezione;
	private String codFornitore;
	private String fornitore;
	private String natura;
	private List listaNatura;
	private String dataOrdineDa;
	private String dataOrdineA;
	private String dataOrdineAbbDa;
	private String dataOrdineAbbA;
	private String stato;
	private String[] statoArr = new String[0];
	private String annoOrdine;
	private String dataStampaOrdineDa;
	private String dataStampaOrdineA;

	private List listaStato;
	private String tipoInvio;
	private List listaTipoInvio;
	private String numero;
	private String esercizio;
	private String capitolo;
	private List listaTipoImpegno;
	private String tipoImpegno;

	private boolean rinnovato = false;
	private boolean stampato = false;
	private boolean aperto = false;
	private boolean chiuso = false;
	private boolean annullato = false;
	private String stampatoStr = "";
	private String rinnovatoStr = "";

	private boolean caricamentoIniziale = false;
	private StrutturaTerna puntuale;
	private StrutturaCombo titolo;

	private boolean sessione;
	private boolean visibilitaIndietroLS = false;
	private boolean provenienzaVAIA = false;

	private int elemXBlocchi = 10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean submitDinamico = false;
	private boolean disabilitaTutto = false;
	private boolean disabilitaFornitore = false;
	private boolean rientroDaSif = false;

	private boolean gestBil = true;
	private boolean gestSez = true;
	private boolean gestProf = true;
	private FornitoreVO fornitoreVO;

	// almaviva5_20121113 evolutive google
	private String rfidChiaveInventario;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// this.statoArr=new String[0];
		this.rinnovato = false;
		this.stampato = false;

	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}

	public String getContinuativo() {
		return continuativo;
	}

	public void setContinuativo(String continuativo) {
		this.continuativo = continuativo;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

	public List getListaContinuativo() {
		return listaContinuativo;
	}

	public void setListaContinuativo(List listaContinuativo) {
		this.listaContinuativo = listaContinuativo;
	}

	public String getCodiceBibl() {
		return codiceBibl;
	}

	public void setCodiceBibl(String codiceBibl) {
		this.codiceBibl = codiceBibl;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public List getListaNatura() {
		return listaNatura;
	}

	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getDataOrdineA() {
		return dataOrdineA;
	}

	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}

	public String getDataOrdineDa() {
		return dataOrdineDa;
	}

	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}

	public String getDataOrdineAbbA() {
		return dataOrdineAbbA;
	}

	public void setDataOrdineAbbA(String dataOrdineAbbA) {
		this.dataOrdineAbbA = dataOrdineAbbA;
	}

	public String getDataOrdineAbbDa() {
		return dataOrdineAbbDa;
	}

	public void setDataOrdineAbbDa(String dataOrdineAbbDa) {
		this.dataOrdineAbbDa = dataOrdineAbbDa;
	}

	public List getListaStato() {
		return listaStato;
	}

	public void setListaStato(List listaStato) {
		this.listaStato = listaStato;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public List getListaTipoInvio() {
		return listaTipoInvio;
	}

	public void setListaTipoInvio(List listaTipoInvio) {
		this.listaTipoInvio = listaTipoInvio;
	}

	public String getTipoInvio() {
		return tipoInvio;
	}

	public void setTipoInvio(String tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public List<StrutturaCombo> getListaBiblAffil() {
		return listaBiblAffil;
	}

	public void setListaBiblAffil(List<StrutturaCombo> listaBiblAffil) {
		this.listaBiblAffil = listaBiblAffil;
	}

	public String getBiblAffil() {
		return biblAffil;
	}

	public void setBiblAffil(String biblAffil) {
		this.biblAffil = biblAffil;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public List getListaTipoImpegno() {
		return listaTipoImpegno;
	}

	public void setListaTipoImpegno(List listaTipoImpegno) {
		this.listaTipoImpegno = listaTipoImpegno;
	}

	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}

	public String[] getStatoArr() {
		return statoArr;
	}

	public void setStatoArr(String[] statoArr) {
		this.statoArr = statoArr;
	}

	public boolean isProvenienzaVAIA() {
		return provenienzaVAIA;
	}

	public void setProvenienzaVAIA(boolean provenienzaVAIA) {
		this.provenienzaVAIA = provenienzaVAIA;
	}

	public StrutturaCombo getTitolo() {
		return titolo;
	}

	public void setTitolo(StrutturaCombo titolo) {
		this.titolo = titolo;
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

	public boolean isRinnovato() {
		return rinnovato;
	}

	public void setRinnovato(boolean rinnovato) {
		this.rinnovato = rinnovato;
	}

	public boolean isStampato() {
		return stampato;
	}

	public void setStampato(boolean stampato) {
		this.stampato = stampato;
	}

	public boolean isAnnullato() {
		return annullato;
	}

	public void setAnnullato(boolean annullato) {
		this.annullato = annullato;
	}

	public boolean isAperto() {
		return aperto;
	}

	public void setAperto(boolean aperto) {
		this.aperto = aperto;
	}

	public boolean isChiuso() {
		return chiuso;
	}

	public void setChiuso(boolean chiuso) {
		this.chiuso = chiuso;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
	}

	public boolean isLSRicerca() {
		return LSRicerca;
	}

	public void setLSRicerca(boolean ricerca) {
		LSRicerca = ricerca;
	}

	public String getAnnoOrdine() {
		return annoOrdine;
	}

	public void setAnnoOrdine(String annoOrdine) {
		this.annoOrdine = annoOrdine;
	}

	public String getDataStampaOrdineDa() {
		return dataStampaOrdineDa;
	}

	public void setDataStampaOrdineDa(String dataStampaOrdineDa) {
		this.dataStampaOrdineDa = dataStampaOrdineDa;
	}

	public String getDataStampaOrdineA() {
		return dataStampaOrdineA;
	}

	public void setDataStampaOrdineA(String dataStampaOrdineA) {
		this.dataStampaOrdineA = dataStampaOrdineA;
	}

	public StrutturaTerna getPuntuale() {
		return puntuale;
	}

	public void setPuntuale(StrutturaTerna puntuale) {
		this.puntuale = puntuale;
	}

	public boolean isSubmitDinamico() {
		return submitDinamico;
	}

	public void setSubmitDinamico(boolean submitDinamico) {
		this.submitDinamico = submitDinamico;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public boolean isDisabilitaFornitore() {
		return disabilitaFornitore;
	}

	public void setDisabilitaFornitore(boolean disabilitaFornitore) {
		this.disabilitaFornitore = disabilitaFornitore;
	}

	public boolean isRientroDaSif() {
		return rientroDaSif;
	}

	public void setRientroDaSif(boolean rientroDaSif) {
		this.rientroDaSif = rientroDaSif;
	}

	public String getDenoBibl() {
		return denoBibl;
	}

	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
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

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getRinnovatoStr() {
		return rinnovatoStr;
	}

	public void setRinnovatoStr(String rinnovatoStr) {
		this.rinnovatoStr = rinnovatoStr;
	}

	public String getStampatoStr() {
		return stampatoStr;
	}

	public void setStampatoStr(String stampatoStr) {
		this.stampatoStr = stampatoStr;
	}

	public FornitoreVO getFornitoreVO() {
		return fornitoreVO;
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		this.fornitoreVO = fornitoreVO;
	}

	public String getRfidChiaveInventario() {
		return rfidChiaveInventario;
	}

	public void setRfidChiaveInventario(String rfidChiaveInventario) {
		this.rfidChiaveInventario = rfidChiaveInventario;
	}
}
