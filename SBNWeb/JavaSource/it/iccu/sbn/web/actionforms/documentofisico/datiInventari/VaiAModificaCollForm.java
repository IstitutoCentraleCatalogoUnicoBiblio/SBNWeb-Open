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
package it.iccu.sbn.web.actionforms.documentofisico.datiInventari;

import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class VaiAModificaCollForm extends ActionForm {


	private static final long serialVersionUID = 6001350329920367442L;
	private String bid;
	private String terzaParte;

	public String getTerzaParte() {
		return terzaParte;
	}
	public void setTerzaParte(String terzaParte) {
		this.terzaParte = terzaParte;
	}
	private int codInvent;
	private String codPolo;
	private String codBib;
	private String codSerie;
	private String codPoloSez;
	private String codBibSez;
	private String codSez;
	private String codCollocazione;
	private String codSpecificazione;
	private boolean codSpecificazioneDisable;
	private boolean codCollocazioneDisable;
	private String chiave;
	private String codFormato;
	private String consistenza;
	private boolean conferma;
	private boolean sezNonEsiste = false;

	private String descrBib;
	private boolean disable;
	private boolean disablePerModInvDaNav;
	private String descrSez;
	private String descrTipoCollocazione;

	private int keyLoc;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );

	private String lenteSez;
	private String livello1;
	private String livello2;
	private String livello3;
	private String livello4;
	private List listaFormati;
	private List listaCodiciFormati;
	private List listaFormatiSezioni;

	private String noSezione;
	private boolean noReticolo;
	private boolean noFormati;

	EsameCollocRicercaVO paramRicerca = new EsameCollocRicercaVO();
	private int progressivo;
	private int progrNumCalcolato;
	private int progrSerieCalcolata;

	private CollocazioneDettaglioVO recCollDett = new CollocazioneDettaglioVO();
	private CollocazioneDettaglioVO collocazioneOriginale = new CollocazioneDettaglioVO();
	private DatiBibliograficiCollocazioneVO reticolo =  null;
	private CollocazioneTitoloVO[] reticoloColl =  null;
	private InventarioVO recInv =  new InventarioVO();
	private FormatiSezioniVO recFormatiSezioni;
	private SezioneCollocazioneVO recSezione = new SezioneCollocazioneVO();

	private int serie;
	private boolean sessione;

	private String titolo;
	private String tipoColl;
	private String ticket;

	private String prov;
	private boolean abilitaBottoneInviaInIndice;
	private String tasto;
	private String msg;

	private boolean provTastoEsemplare = false;
	//


	public boolean isCodCollocazioneDisable() {
		return codCollocazioneDisable;
	}
	public void setCodCollocazioneDisable(boolean codCollocazioneDisable) {
		this.codCollocazioneDisable = codCollocazioneDisable;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getLenteSez() {
		return lenteSez;
	}
	public void setLenteSez(String lenteSez) {
		this.lenteSez = lenteSez;
	}
	public String getNoSezione() {
		return noSezione;
	}
	public void setNoSezione(String noSezione) {
		this.noSezione = noSezione;
	}
	public String getTipoColl() {
		return tipoColl;
	}
	public void setTipoColl(String tipoColl) {
		this.tipoColl = tipoColl;
	}
//	public CollocazioneDettaglioVO getRecColl() {
//		return recColl;
//	}
//	public void setRecColl(CollocazioneDettaglioVO recColl) {
//		this.recColl = recColl;
//	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public int getCodInvent() {
		return codInvent;
	}
	public void setCodInvent(int codInvent) {
		this.codInvent = codInvent;
	}
	public String getCodSerie() {
		return codSerie;
	}
	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	public String getCodCollocazione() {
		return codCollocazione;
	}
	public void setCodCollocazione(String codCollocazione) {
		this.codCollocazione = codCollocazione;
	}
	public String getCodFormato() {
		return codFormato;
	}
	public void setCodFormato(String codFormato) {
		this.codFormato = codFormato;
	}
	public String getCodSez() {
		return codSez;
	}
	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}
	public String getCodSpecificazione() {
		return codSpecificazione;
	}
	public void setCodSpecificazione(String codSpecificazione) {
		this.codSpecificazione = codSpecificazione;
	}
	public String getConsistenza() {
		return consistenza;
	}
	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}
	public String getDescrTipoCollocazione() {
		return descrTipoCollocazione;
	}
	public void setDescrTipoCollocazione(String descrTipoCollocazione) {
		this.descrTipoCollocazione = descrTipoCollocazione;
	}
	public List getListaCodiciFormati() {
		return listaCodiciFormati;
	}
	public void setListaCodiciFormati(List listaCodiciFormati) {
		this.listaCodiciFormati = listaCodiciFormati;
	}
	public List getListaFormati() {
		return listaFormati;
	}
	public void setListaFormati(List listaFormati) {
		this.listaFormati = listaFormati;
	}
	public String getLivello1() {
		return livello1;
	}
	public void setLivello1(String livello1) {
		this.livello1 = livello1;
	}
	public String getLivello2() {
		return livello2;
	}
	public void setLivello2(String livello2) {
		this.livello2 = livello2;
	}
	public String getLivello3() {
		return livello3;
	}
	public void setLivello3(String livello3) {
		this.livello3 = livello3;
	}
	public String getLivello4() {
		return livello4;
	}
	public void setLivello4(String livello4) {
		this.livello4 = livello4;
	}
	public boolean isNoFormati() {
		return noFormati;
	}
	public void setNoFormati(boolean noFormati) {
		this.noFormati = noFormati;
	}
	public int getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}
	public void setRecFormatiSezioni(FormatiSezioniVO recFormatiSezioni) {
		this.recFormatiSezioni = recFormatiSezioni;
	}
//	public String getSerie() {
//		return serie;
//	}
//	public void setSerie(String serie) {
//		this.serie = serie;
//	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getDescrSez() {
		return descrSez;
	}
	public void setDescrSez(String descrSez) {
		this.descrSez = descrSez;
	}
	public int getKeyLoc() {
		return keyLoc;
	}
	public void setKeyLoc(int keyLoc) {
		this.keyLoc = keyLoc;
	}
	public boolean isNoReticolo() {
		return noReticolo;
	}
	public void setNoReticolo(boolean noReticolo) {
		this.noReticolo = noReticolo;
	}
	public DatiBibliograficiCollocazioneVO getReticolo() {
		return reticolo;
	}
	public void setReticolo(DatiBibliograficiCollocazioneVO reticolo) {
		this.reticolo = reticolo;
	}
	public CollocazioneTitoloVO[] getReticoloColl() {
		return reticoloColl;
	}
	public void setReticoloColl(CollocazioneTitoloVO[] reticoloColl) {
		this.reticoloColl = reticoloColl;
	}
	public FormatiSezioniVO getRecFormatiSezioni() {
		return recFormatiSezioni;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public EsameCollocRicercaVO getParamRicerca() {
		return paramRicerca;
	}
	public void setParamRicerca(EsameCollocRicercaVO paramRicerca) {
		this.paramRicerca = paramRicerca;
	}
	public InventarioVO getRecInv() {
		return recInv;
	}
	public void setRecInv(InventarioVO recInv) {
		this.recInv = recInv;
	}
	public CollocazioneDettaglioVO getRecCollDett() {
		return recCollDett;
	}
	public void setRecCollDett(CollocazioneDettaglioVO recCollDett) {
		this.recCollDett = recCollDett;
	}
	public List getListaFormatiSezioni() {
		return listaFormatiSezioni;
	}
	public void setListaFormatiSezioni(List listaFormatiSezioni) {
		this.listaFormatiSezioni = listaFormatiSezioni;
	}
	public int getSerie() {
		return serie;
	}
	public void setSerie(int serie) {
		this.serie = serie;
	}
	public String getCodPoloSez() {
		return codPoloSez;
	}
	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}
	public String getCodBibSez() {
		return codBibSez;
	}
	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}
	public boolean isSezNonEsiste() {
		return sezNonEsiste;
	}
	public void setSezNonEsiste(boolean sezNonEsiste) {
		this.sezNonEsiste = sezNonEsiste;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public boolean isCodSpecificazioneDisable() {
		return codSpecificazioneDisable;
	}
	public void setCodSpecificazioneDisable(boolean codSpecificazioneDisable) {
		this.codSpecificazioneDisable = codSpecificazioneDisable;
	}
	public int getProgrNumCalcolato() {
		return progrNumCalcolato;
	}
	public void setProgrNumCalcolato(int progrNumCalcolato) {
		this.progrNumCalcolato = progrNumCalcolato;
	}
	public int getProgrSerieCalcolata() {
		return progrSerieCalcolata;
	}
	public void setProgrSerieCalcolata(int progrSerieCalcolata) {
		this.progrSerieCalcolata = progrSerieCalcolata;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public boolean isDisablePerModInvDaNav() {
		return disablePerModInvDaNav;
	}
	public void setDisablePerModInvDaNav(boolean disablePerModInvDaNav) {
		this.disablePerModInvDaNav = disablePerModInvDaNav;
	}
	public boolean isAbilitaBottoneInviaInIndice() {
		return abilitaBottoneInviaInIndice;
	}
	public void setAbilitaBottoneInviaInIndice(boolean abilitaBottoneInviaInIndice) {
		this.abilitaBottoneInviaInIndice = abilitaBottoneInviaInIndice;
	}
	public String getTasto() {
		return tasto;
	}
	public void setTasto(String tasto) {
		this.tasto = tasto;
	}
	public SezioneCollocazioneVO getRecSezione() {
		return recSezione;
	}
	public void setRecSezione(SezioneCollocazioneVO recSezione) {
		this.recSezione = recSezione;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isProvTastoEsemplare() {
		return provTastoEsemplare;
	}
	public void setProvTastoEsemplare(boolean provTastoEsemplare) {
		this.provTastoEsemplare = provTastoEsemplare;
	}
	public CollocazioneDettaglioVO getCollocazioneOriginale() {
		return collocazioneOriginale;
	}
	public void setCollocazioneOriginale(
			CollocazioneDettaglioVO collocazioneOriginale) {
		this.collocazioneOriginale = collocazioneOriginale;
	}

}
