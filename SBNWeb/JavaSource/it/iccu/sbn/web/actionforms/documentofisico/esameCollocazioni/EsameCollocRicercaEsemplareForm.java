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
package it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni;

import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class EsameCollocRicercaEsemplareForm extends ActionForm {


	private static final long serialVersionUID = -6203109889266968257L;
	private EsemplareDettaglioVO recEsempl = new EsemplareDettaglioVO();
	private DatiBibliograficiCollocazioneVO reticolo =  null;
	private CollocazioneTitoloVO[] reticoloTitoli =  null;
	private CollocazioneDettaglioVO recCollDett =  new CollocazioneDettaglioVO();
	private String ticket;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String bid;
	private int keyLoc;
	private String titolo;
	private String codSez;
	private String codCollocazione;
	private String codSpecificazione;

	//
	private List titoliEsempl;
	private List listaEsemplReticolo;
	private String selectedTit;
	private String selectedEsemRetic;

	private boolean tab1;
	private boolean tab2;
	private boolean noColl;
	private boolean disable;
	private boolean sessione;
	private String folder;
	private String tasto;

	private boolean noReticolo;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	//tab1
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int bloccoSelezionato=0;
	//tab2
	private String idLista1;
	private int totBlocchi1;
	private int totRighe1;
	private int bloccoSelezionato1=0;

	private boolean abilitaBottoneCarBlocchi;
	private String tipoOrdinamento;
	private List listaTipiOrdinamento;

	private String codSerie;
	private String codInv;

	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}

	public String getCodInv() {
		return codInv;
	}

	public void setCodInv(String codInv) {
		this.codInv = codInv;
	}

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

	public String getCodCollocazione() {
		return codCollocazione;
	}

	public void setCodCollocazione(String codCollocazione) {
		this.codCollocazione = codCollocazione;
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

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public List getListaEsemplReticolo() {
		return listaEsemplReticolo;
	}

	public void setListaEsemplReticolo(List listaEsemplReticolo) {
		this.listaEsemplReticolo = listaEsemplReticolo;
	}

	public boolean isNoReticolo() {
		return noReticolo;
	}

	public void setNoReticolo(boolean noReticolo) {
		this.noReticolo = noReticolo;
	}

	public EsemplareDettaglioVO getRecEsempl() {
		return recEsempl;
	}

	public void setRecEsempl(EsemplareDettaglioVO recEsempl) {
		this.recEsempl = recEsempl;
	}

	public DatiBibliograficiCollocazioneVO getReticolo() {
		return reticolo;
	}

	public void setReticolo(DatiBibliograficiCollocazioneVO reticolo) {
		this.reticolo = reticolo;
	}

	public String getSelectedEsemRetic() {
		return selectedEsemRetic;
	}

	public void setSelectedEsemRetic(String selectedEsemRetic) {
		this.selectedEsemRetic = selectedEsemRetic;
	}

	public String getSelectedTit() {
		return selectedTit;
	}

	public void setSelectedTit(String selectedTit) {
		this.selectedTit = selectedTit;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public int getElemPerBlocchi() {
		return nRec;
	}
	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.nRec = elemPerBlocchi;
	}
	public String getTasto() {
		return tasto;
	}

	public void setTasto(String tasto) {
		this.tasto = tasto;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public List getTitoliEsempl() {
		return titoliEsempl;
	}

	public void setTitoliEsempl(List titoliEsempl) {
		this.titoliEsempl = titoliEsempl;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public boolean isNoColl() {
		return noColl;
	}

	public void setNoColl(boolean noColl) {
		this.noColl = noColl;
	}

	public CollocazioneTitoloVO[] getReticoloTitoli() {
		return reticoloTitoli;
	}

	public void setReticoloTitoli(CollocazioneTitoloVO[] reticoloTitoli) {
		this.reticoloTitoli = reticoloTitoli;
	}

	public boolean isTab1() {
		return tab1;
	}

	public void setTab1(boolean tab1) {
		this.tab1 = tab1;
	}

	public int getKeyLoc() {
		return keyLoc;
	}

	public void setKeyLoc(int keyLoc) {
		this.keyLoc = keyLoc;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public boolean isTab2() {
		return tab2;
	}

	public void setTab2(boolean tab2) {
		this.tab2 = tab2;
	}

	public CollocazioneDettaglioVO getRecCollDett() {
		return recCollDett;
	}

	public void setRecCollDett(CollocazioneDettaglioVO recCollDett) {
		this.recCollDett = recCollDett;
	}

	public String getIdLista1() {
		return idLista1;
	}

	public void setIdLista1(String idLista1) {
		this.idLista1 = idLista1;
	}

	public int getTotBlocchi1() {
		return totBlocchi1;
	}

	public void setTotBlocchi1(int totBlocchi1) {
		this.totBlocchi1 = totBlocchi1;
	}

	public int getTotRighe1() {
		return totRighe1;
	}

	public void setTotRighe1(int totRighe1) {
		this.totRighe1 = totRighe1;
	}

	public int getBloccoSelezionato1() {
		return bloccoSelezionato1;
	}

	public void setBloccoSelezionato1(int bloccoSelezionato1) {
		this.bloccoSelezionato1 = bloccoSelezionato1;
	}

	public boolean isAbilitaBottoneCarBlocchi() {
		return abilitaBottoneCarBlocchi;
	}

	public void setAbilitaBottoneCarBlocchi(boolean abilitaBottoneCarBlocchi) {
		this.abilitaBottoneCarBlocchi = abilitaBottoneCarBlocchi;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}



}
