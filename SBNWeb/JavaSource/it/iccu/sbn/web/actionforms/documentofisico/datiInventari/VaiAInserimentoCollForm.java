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

import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneReticoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class VaiAInserimentoCollForm extends ActionForm {


	private static final long serialVersionUID = -3386654081110956124L;
	private EsemplareDettaglioVO recEsempl = new EsemplareDettaglioVO();
	private CollocazioneVO recColl = new CollocazioneVO();
	private DatiBibliograficiCollocazioneVO reticolo =  null;
	private CollocazioneTitoloVO[] reticoloColl =  null;
	private CollocazioneReticoloVO tab2 = new CollocazioneReticoloVO();
	private CollocazioneTitoloVO tab3 = new CollocazioneTitoloVO();
	private EsameCollocRicercaVO paramRicerca = new EsameCollocRicercaVO();
	private InventarioVO recInv = new InventarioVO();
	private SezioneCollocazioneVO recSezione = new SezioneCollocazioneVO();

	private String tipoPrgDefault;
	private String ticket;
	private String bid;
	private String terzaParte;
	public String getTerzaParte() {
		return terzaParte;
	}
	public void setTerzaParte(String terzaParte) {
		this.terzaParte = terzaParte;
	}
	private String titolo;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codSerie;
	private int codInvent;
	private String bidDaTit;
	private String isbdDaTit;
//	private List collRetic;
	private List collLiv;
	private String livColl = "";
	private boolean disable;
	private boolean sessione;
	private String selectedTit;
	private String selectedColl;
	private String folder;
	private String tasto;
	private String tipoColl;
	private String lenteSez;
	private String noSezione = "noSezione"; //noSezione:la segnatura non è visualizzata; siSezione:la segnatura è visualizzata
	private boolean sezNonEsiste = false;
	//tab1
	private String codPoloSez;
	private String codBibSez;
	private String codSez;
	private String descrSez;
	private String descrTipoCollocazione;
	private String codCollocazione;
	private boolean codCollocazioneDisable;
	private String codSpecificazione;
	private boolean codSpecificazioneDisable;
	private String chiave = "";
	private String livello1;
	private String livello2;
	private String livello3;
	private String livelloUnico;
	private String codFormato;
	private String consistenza;

	private int serie;
	private int progressivo;
	private List listaFormatiSezioni;
	private List listaCodiciFormati;
	private FormatiSezioniVO recFormatiSezioni;
	private boolean noFormati;
	private boolean conferma;
	//tab2
	private List listaCollReticolo;
	private boolean noReticolo;
	private boolean noColl;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	//tab2
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int bloccoSelezionato=0;
	//tab3
	private String idLista1;
	private int totBlocchi1;
	private int totRighe1;
	private int bloccoSelezionato1=0;

	private boolean abilitaBottoneCarBlocchi;
	private String tipoOrdinamento;
	private List listaTipiOrdinamento;
	//gestione collocazione veloce
	private String prov;
	private String codPoloProvDefault;
	private String codBibProvDefault;
	private String descrProven;
	private List listaTipoFruizione;
	private List listaMatCons;
	private List listaStatoCons;
	private List listaTipoOrdine;
	private List listaCodNoDispo;
	private List listaRiproducibilita;
	private List listaSupportoCopia;
	private boolean disableDataIns;
	private boolean disablePerModInvDaNav;
	private boolean disableCodTipOrd;
	private boolean periodico;
	private boolean stampaEtich;//hidden

	private String catFruiDefault;
	private String tipoAcqDefault;
	private String codNoDispDefault;
	private String codStatoConsDefault;
	private String codProvDefault;

	private boolean flTab2;

	private boolean disableTastoAvanti;

	public boolean isCodCollocazioneDisable() {
		return codCollocazioneDisable;
	}
	public void setCodCollocazioneDisable(boolean codCollocazioneDisable) {
		this.codCollocazioneDisable = codCollocazioneDisable;
	}
	public String getCodPoloProvDefault() {
		return codPoloProvDefault;
	}
	public void setCodPoloProvDefault(String codPoloProvDefault) {
		this.codPoloProvDefault = codPoloProvDefault;
	}
	public String getCodBibProvDefault() {
		return codBibProvDefault;
	}
	public void setCodBibProvDefault(String codBibProvDefault) {
		this.codBibProvDefault = codBibProvDefault;
	}
	public boolean isAbilitaBottoneCarBlocchi() {
		return abilitaBottoneCarBlocchi;
	}
	public void setAbilitaBottoneCarBlocchi(boolean abilitaBottoneCarBlocchi) {
		this.abilitaBottoneCarBlocchi = abilitaBottoneCarBlocchi;
	}
	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}
	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}
	public int getElemPerBlocchi() {
		return nRec;
	}
	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.nRec = elemPerBlocchi;
	}
	public String getIdLista() {
		return idLista;
	}
	public void setIdLista(String idLista) {
		this.idLista = idLista;
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
	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}
	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
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
	public boolean isNoReticolo() {
		return noReticolo;
	}
	public void setNoReticolo(boolean noReticolo) {
		this.noReticolo = noReticolo;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public int getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	public String getDescrTipoCollocazione() {
		return descrTipoCollocazione;
	}
	public void setDescrTipoCollocazione(String descrTipoCollocazione) {
		this.descrTipoCollocazione = descrTipoCollocazione;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
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
	public String getSelectedTit() {
		return selectedTit;
	}
	public void setSelectedTit(String selectedTit) {
		this.selectedTit = selectedTit;
	}
	public String getTasto() {
		return tasto;
	}
	public void setTasto(String tasto) {
		this.tasto = tasto;
	}
	public String getSelectedColl() {
		return selectedColl;
	}
	public void setSelectedColl(String selectedColl) {
		this.selectedColl = selectedColl;
	}
	public List getCollLiv() {
		return collLiv;
	}
	public void setCollLiv(List collLiv) {
		this.collLiv = collLiv;
	}
	public String getLivColl() {
		return livColl;
	}
	public void setLivColl(String livColl) {
		this.livColl = livColl;
	}
	public String getTipoColl() {
		return tipoColl;
	}
	public void setTipoColl(String tipoColl) {
		this.tipoColl = tipoColl;
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
	public CollocazioneVO getRecColl() {
		return recColl;
	}
	public void setRecColl(CollocazioneVO recColl) {
		this.recColl = recColl;
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
	public List getListaCodiciFormati() {
		return listaCodiciFormati;
	}
	public void setListaCodiciFormati(List listaCodiciFormati) {
		this.listaCodiciFormati = listaCodiciFormati;
	}
	public List getListaFormatiSezioni() {
		return listaFormatiSezioni;
	}
	public void setListaFormatiSezioni(List listaFormatiSezioni) {
		this.listaFormatiSezioni = listaFormatiSezioni;
	}
	public boolean isNoFormati() {
		return noFormati;
	}
	public void setNoFormati(boolean noFormati) {
		this.noFormati = noFormati;
	}
	public FormatiSezioniVO getRecFormatiSezioni() {
		return recFormatiSezioni;
	}
	public void setRecFormatiSezioni(FormatiSezioniVO recFormatiSezioni) {
		this.recFormatiSezioni = recFormatiSezioni;
	}
	public DatiBibliograficiCollocazioneVO getReticolo() {
		return reticolo;
	}
	public void setReticolo(DatiBibliograficiCollocazioneVO reticolo) {
		this.reticolo = reticolo;
	}
	public String getCodCollocazione() {
		return codCollocazione;
	}
	public void setCodCollocazione(String codCollocazione) {
		this.codCollocazione = codCollocazione;
	}
	public String getCodSpecificazione() {
		return codSpecificazione;
	}
	public void setCodSpecificazione(String codSpecificazione) {
		this.codSpecificazione = codSpecificazione;
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
	public String getLivelloUnico() {
		return livelloUnico;
	}
	public void setLivelloUnico(String livelloUnico) {
		this.livelloUnico = livelloUnico;
	}
	public String getCodFormato() {
		return codFormato;
	}
	public void setCodFormato(String codFormato) {
		this.codFormato = codFormato;
	}
	public int getSerie() {
		return serie;
	}
	public void setSerie(int serie) {
		this.serie = serie;
	}
	public String getConsistenza() {
		return consistenza;
	}
	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}
	public String getCodSez() {
		return codSez;
	}
	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}
	public List getListaCollReticolo() {
		return listaCollReticolo;
	}
	public void setListaCollReticolo(List listaCollReticolo) {
		this.listaCollReticolo = listaCollReticolo;
	}
	public boolean isNoColl() {
		return noColl;
	}
	public void setNoColl(boolean noColl) {
		this.noColl = noColl;
	}
	public CollocazioneReticoloVO getTab2() {
		return tab2;
	}
	public void setTab2(CollocazioneReticoloVO tab2) {
		this.tab2 = tab2;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public CollocazioneTitoloVO[] getReticoloColl() {
		return reticoloColl;
	}
	public void setReticoloColl(CollocazioneTitoloVO[] reticoloColl) {
		this.reticoloColl = reticoloColl;
	}
	public CollocazioneTitoloVO getTab3() {
		return tab3;
	}
	public void setTab3(CollocazioneTitoloVO tab3) {
		this.tab3 = tab3;
	}
	public EsemplareDettaglioVO getRecEsempl() {
		return recEsempl;
	}
	public void setRecEsempl(EsemplareDettaglioVO recEsempl) {
		this.recEsempl = recEsempl;
	}
	public String getBidDaTit() {
		return bidDaTit;
	}
	public void setBidDaTit(String bidDaTit) {
		this.bidDaTit = bidDaTit;
	}
	public String getIsbdDaTit() {
		return isbdDaTit;
	}
	public void setIsbdDaTit(String isbdDaTit) {
		this.isbdDaTit = isbdDaTit;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodBibSez() {
		return codBibSez;
	}
	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}
	public String getCodPoloSez() {
		return codPoloSez;
	}
	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}
//	public void reset(ActionMapping mapping, HttpServletRequest request) {
//		this.selectedTit=null;
//	}
	public String getDescrSez() {
		return descrSez;
	}
	public void setDescrSez(String descrSez) {
		this.descrSez = descrSez;
	}
	public EsameCollocRicercaVO getParamRicerca() {
		return paramRicerca;
	}
	public void setParamRicerca(EsameCollocRicercaVO paramRicerca) {
		this.paramRicerca = paramRicerca;
	}
	public int getBloccoSelezionato1() {
		return bloccoSelezionato1;
	}
	public void setBloccoSelezionato1(int bloccoSelezionato1) {
		this.bloccoSelezionato1 = bloccoSelezionato1;
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
	public boolean isSezNonEsiste() {
		return sezNonEsiste;
	}
	public void setSezNonEsiste(boolean sezNonEsiste) {
		this.sezNonEsiste = sezNonEsiste;
	}
	public boolean isCodSpecificazioneDisable() {
		return codSpecificazioneDisable;
	}
	public void setCodSpecificazioneDisable(boolean codSpecificazioneDisable) {
		this.codSpecificazioneDisable = codSpecificazioneDisable;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public InventarioVO getRecInv() {
		return recInv;
	}
	public void setRecInv(InventarioVO recInv) {
		this.recInv = recInv;
	}
	public String getDescrProven() {
		return descrProven;
	}
	public void setDescrProven(String descrProven) {
		this.descrProven = descrProven;
	}
	public List getListaTipoFruizione() {
		return listaTipoFruizione;
	}
	public void setListaTipoFruizione(List listaTipoFruizione) {
		this.listaTipoFruizione = listaTipoFruizione;
	}
	public List getListaMatCons() {
		return listaMatCons;
	}
	public void setListaMatCons(List listaMatCons) {
		this.listaMatCons = listaMatCons;
	}
	public List getListaStatoCons() {
		return listaStatoCons;
	}
	public void setListaStatoCons(List listaStatoCons) {
		this.listaStatoCons = listaStatoCons;
	}
	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}
	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}
	public List getListaCodNoDispo() {
		return listaCodNoDispo;
	}
	public void setListaCodNoDispo(List listaCodNoDispo) {
		this.listaCodNoDispo = listaCodNoDispo;
	}
	public boolean isDisableDataIns() {
		return disableDataIns;
	}
	public void setDisableDataIns(boolean disableDataIns) {
		this.disableDataIns = disableDataIns;
	}
	public boolean isDisableCodTipOrd() {
		return disableCodTipOrd;
	}
	public void setDisableCodTipOrd(boolean disableCodTipOrd) {
		this.disableCodTipOrd = disableCodTipOrd;
	}
	public boolean isPeriodico() {
		return periodico;
	}
	public void setPeriodico(boolean periodico) {
		this.periodico = periodico;
	}
	public boolean isStampaEtich() {
		return stampaEtich;
	}
	public void setStampaEtich(boolean stampaEtich) {
		this.stampaEtich = stampaEtich;
	}
	public List getListaRiproducibilita() {
		return listaRiproducibilita;
	}
	public void setListaRiproducibilita(List listaRiproducibilita) {
		this.listaRiproducibilita = listaRiproducibilita;
	}
	public List getListaSupportoCopia() {
		return listaSupportoCopia;
	}
	public void setListaSupportoCopia(List listaSupportoCopia) {
		this.listaSupportoCopia = listaSupportoCopia;
	}
	public String getTipoPrgDefault() {
		return tipoPrgDefault;
	}
	public void setTipoPrgDefault(String tipoPrgDefault) {
		this.tipoPrgDefault = tipoPrgDefault;
	}
	public String getCatFruiDefault() {
		return catFruiDefault;
	}
	public void setCatFruiDefault(String catFruiDefault) {
		this.catFruiDefault = catFruiDefault;
	}
	public String getTipoAcqDefault() {
		return tipoAcqDefault;
	}
	public void setTipoAcqDefault(String tipoAcqDefault) {
		this.tipoAcqDefault = tipoAcqDefault;
	}
	public String getCodNoDispDefault() {
		return codNoDispDefault;
	}
	public void setCodNoDispDefault(String codNoDispDefault) {
		this.codNoDispDefault = codNoDispDefault;
	}
	public String getCodStatoConsDefault() {
		return codStatoConsDefault;
	}
	public void setCodStatoConsDefault(String codStatoConsDefault) {
		this.codStatoConsDefault = codStatoConsDefault;
	}
	public String getCodProvDefault() {
		return codProvDefault;
	}
	public void setCodProvDefault(String codProvDefault) {
		this.codProvDefault = codProvDefault;
	}
	public boolean isDisableTastoAvanti() {
		return disableTastoAvanti;
	}
	public void setDisableTastoAvanti(boolean disableTastoAvanti) {
		this.disableTastoAvanti = disableTastoAvanti;
	}
	public boolean isDisablePerModInvDaNav() {
		return disablePerModInvDaNav;
	}
	public void setDisablePerModInvDaNav(boolean disablePerModInvDaNav) {
		this.disablePerModInvDaNav = disablePerModInvDaNav;
	}
	public boolean isFlTab2() {
		return flTab2;
	}
	public void setFlTab2(boolean flTab2) {
		this.flTab2 = flTab2;
	}
	public SezioneCollocazioneVO getRecSezione() {
		return recSezione;
	}
	public void setRecSezione(SezioneCollocazioneVO recSezione) {
		this.recSezione = recSezione;
	}

}
