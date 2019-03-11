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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceNotaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class ModificaInvCollForm extends ActionForm {


	private static final long serialVersionUID = -7543553377253988667L;
	private String action;
	// dati inventario
	private String bid;

	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codSerie;
	private int codInvent;
	private boolean conferma;
	private String chiave;
	private String codPoloSez;
	private String codBibSez;
	private String codSez;
	private String codColloc;
	private String specColloc;
	private String consistenza;
	private String codPoloFattura;
	private String codBibFattura;
	private boolean collocazione = false;
	private String tipoCollocN;
	private String tipoColloc;
	private String codCollocFormato;
	private String codSpecifFormato;
	private String appoggioCampoObb;
	int keyLoc;
	private String numCarico;

	private String descrProven;
	private boolean disableProven;
	private boolean disableTastoProven;
	private boolean disableCodTipOrd;
	private boolean disableDataIns;
	private boolean disableNumFattura;
	private boolean disableDataFattura;
	private boolean disableDataCarico;
	private boolean disableCodCarico;
	private boolean disableNumCarico;

	private boolean disableConsist = false;
	private String numFattura;
	private int prgFattura;
	private int rigaFattura;
	private String dataFattura;
	private String annoFattura;
	private String dataCarico;
	private String descrFornitore;
	private boolean disable = false;
	private boolean disableTastoSalva = false;
	private boolean disableTastoEsemplare = false;
	private boolean disableTastoCancInv = false;
	private boolean disablePerModInvDaNav = false;
	private boolean disableTastoAggiornaInIndice = false;
	private List ListaComboBC = new ArrayList();
	private List listaBuoniCarico = new ArrayList();

	private boolean fattura = false;
	private String folder;

	private String bidColloc;
	private String isbd;
	private String isbdCollocazione;
	private int index;
	private boolean invColl = false;
	private boolean listaInv = false;

	private List listaTipoFruizione;
	private List listaMatCons;
	private List listaStatoCons;
	private List listaTipoOrdine;
	private List listaCodNoDispo;
	private List listaRiproducibilita;
	private List listaSupportoCopia;
	private List listaCodiciNote;
	private List<EtichettaDettaglioVO> listaEtichetteBarcode = new ArrayList<EtichettaDettaglioVO>();

	//
	private List listaTipoDigit;
	private List listaDispDaRemoto;
	private List listaTecaDigitale;
	private String datiAccessoRemoto;
	//

	private List listaMotivoCarico;
	private List listaMotivoScarico;
	private List listaInvCreati;
	private String livColl;
	private String loc;

	private int numInv;
	private boolean noInv = false;

	private boolean ordine = false;

	private String prov;// provenienza da ordine
	private String tipoLista;
	private boolean periodico = false;

	CollocazioneVO recColl = null;
	InventarioVO recInv = new InventarioVO();
	DatiBibliograficiCollocazioneVO reticolo = null;

	private boolean sessione;
	private String specLoc;
	private boolean stampaEtich;// hidden
	private boolean trasferimento;

	private String tipoOperazione;
	private String tasto;
	private String ticket;

	private String catFruiDefault;
	private String tipoAcqDefault;
	private String codNoDispDefault;
	private String codStatoConsDefault;
	private String codPoloProvDefault;
	private String codBibProvDefault;
	private String codProvDefault;
	private String descrProvDefault;
	private String selezRadioNota;
	private boolean abilitaBottoneInviaInIndice;
	private boolean flRicarica;

	public static final String SUBMIT_CANCELLA_NOTA = "mod.inv.coll.canc.nota";
	private List<CodiceNotaVO> listaNoteDinamica = new ArrayList();

	public String getSUBMIT_CANCELLA_NOTA() {
		return SUBMIT_CANCELLA_NOTA;
	}

	public String getSelezRadioNota() {
		return selezRadioNota;
	}

	public void setSelezRadioNota(String selezRadioNota) {
		this.selezRadioNota = selezRadioNota;
	}

	public boolean isNoInv() {
		return noInv;
	}

	public void setNoInv(boolean noInv) {
		this.noInv = noInv;
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

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isStampaEtich() {
		return stampaEtich;
	}

	public void setStampaEtich(boolean stampaEtich) {
		this.stampaEtich = stampaEtich;
	}

	public String getTasto() {
		return tasto;
	}

	public void setTasto(String tasto) {
		this.tasto = tasto;
	}

	public boolean isCollocazione() {
		return collocazione;
	}

	public void setCollocazione(boolean collocazione) {
		this.collocazione = collocazione;
	}

	public boolean isFattura() {
		return fattura;
	}

	public void setFattura(boolean fattura) {
		this.fattura = fattura;
	}

	public boolean isOrdine() {
		return ordine;
	}

	public void setOrdine(boolean ordine) {
		this.ordine = ordine;
	}

	public List getListaCodNoDispo() {
		return listaCodNoDispo;
	}

	public void setListaCodNoDispo(List listaCodNoDispo) {
		this.listaCodNoDispo = listaCodNoDispo;
	}

	public List getListaMatCons() {
		return listaMatCons;
	}

	public void setListaMatCons(List listaMatCons) {
		this.listaMatCons = listaMatCons;
	}

	public List getListaMotivoCarico() {
		return listaMotivoCarico;
	}

	public void setListaMotivoCarico(List listaMotivoCarico) {
		this.listaMotivoCarico = listaMotivoCarico;
	}

	public List getListaMotivoScarico() {
		return listaMotivoScarico;
	}

	public void setListaMotivoScarico(List listaMotivoScarico) {
		this.listaMotivoScarico = listaMotivoScarico;
	}

	public List getListaStatoCons() {
		return listaStatoCons;
	}

	public void setListaStatoCons(List listaStatoCons) {
		this.listaStatoCons = listaStatoCons;
	}

	public List getListaTipoFruizione() {
		return listaTipoFruizione;
	}

	public void setListaTipoFruizione(List listaTipoFruizione) {
		this.listaTipoFruizione = listaTipoFruizione;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isDisableConsist() {
		return disableConsist;
	}

	public void setDisableConsist(boolean disableConsist) {
		this.disableConsist = disableConsist;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getDescrProven() {
		return descrProven;
	}

	public void setDescrProven(String descrProven) {
		this.descrProven = ValidazioneDati.trimOrEmpty(descrProven);
	}

	public InventarioVO getRecInv() {
		return recInv;
	}

	public void setRecInv(InventarioVO recInv) {
		this.recInv = recInv;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public CollocazioneVO getRecColl() {
		return recColl;
	}

	public void setRecColl(CollocazioneVO recColl) {
		this.recColl = recColl;
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

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getLivColl() {
		return livColl;
	}

	public void setLivColl(String livColl) {
		this.livColl = livColl;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public DatiBibliograficiCollocazioneVO getReticolo() {
		return reticolo;
	}

	public void setReticolo(DatiBibliograficiCollocazioneVO reticolo) {
		this.reticolo = reticolo;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public boolean isPeriodico() {
		return periodico;
	}

	public void setPeriodico(boolean periodico) {
		this.periodico = periodico;
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

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getSpecLoc() {
		return specLoc;
	}

	public void setSpecLoc(String specLoc) {
		this.specLoc = specLoc;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public boolean isInvColl() {
		return invColl;
	}

	public void setInvColl(boolean invColl) {
		this.invColl = invColl;
	}

	public List getListaInvCreati() {
		return listaInvCreati;
	}

	public void setListaInvCreati(List listaInvCreati) {
		this.listaInvCreati = listaInvCreati;
	}

	public int getNumInv() {
		return numInv;
	}

	public void setNumInv(int numInv) {
		this.numInv = numInv;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public boolean isDisableCodTipOrd() {
		return disableCodTipOrd;
	}

	public void setDisableCodTipOrd(boolean disableCodTipOrd) {
		this.disableCodTipOrd = disableCodTipOrd;
	}

	public boolean isDisableDataIns() {
		return disableDataIns;
	}

	public void setDisableDataIns(boolean disableDataIns) {
		this.disableDataIns = disableDataIns;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCodBibFattura() {
		return codBibFattura;
	}

	public void setCodBibFattura(String codBibFattura) {
		this.codBibFattura = codBibFattura;
	}

	public String getCodPoloFattura() {
		return codPoloFattura;
	}

	public void setCodPoloFattura(String codPoloFattura) {
		this.codPoloFattura = codPoloFattura;
	}

	public String getDataFattura() {
		return dataFattura;
	}

	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}

	public String getDescrFornitore() {
		return descrFornitore;
	}

	public void setDescrFornitore(String descrFornitore) {
		this.descrFornitore = descrFornitore;
	}

	public boolean isDisableDataFattura() {
		return disableDataFattura;
	}

	public void setDisableDataFattura(boolean disableDataFattura) {
		this.disableDataFattura = disableDataFattura;
	}

	public boolean isDisableNumFattura() {
		return disableNumFattura;
	}

	public void setDisableNumFattura(boolean disableNumFattura) {
		this.disableNumFattura = disableNumFattura;
	}

	public boolean isListaInv() {
		return listaInv;
	}

	public void setListaInv(boolean listaInv) {
		this.listaInv = listaInv;
	}

	public String getTipoLista() {
		return tipoLista;
	}

	public void setTipoLista(String tipoLista) {
		this.tipoLista = tipoLista;
	}

	public int getPrgFattura() {
		return prgFattura;
	}

	public void setPrgFattura(int prgFattura) {
		this.prgFattura = prgFattura;
	}

	public String getIsbdCollocazione() {
		return isbdCollocazione;
	}

	public void setIsbdCollocazione(String isbdCollocazione) {
		this.isbdCollocazione = isbdCollocazione;
	}

	public String getBidColloc() {
		return bidColloc;
	}

	public void setBidColloc(String bidColloc) {
		this.bidColloc = bidColloc;
	}

	public String getNumFattura() {
		return numFattura;
	}

	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}

	public int getRigaFattura() {
		return rigaFattura;
	}

	public void setRigaFattura(int rigaFattura) {
		this.rigaFattura = rigaFattura;
	}

	public String getDataCarico() {
		return dataCarico;
	}

	public void setDataCarico(String dataCarico) {
		this.dataCarico = dataCarico;
	}

	public String getCodColloc() {
		return codColloc;
	}

	public void setCodColloc(String codColloc) {
		this.codColloc = codColloc;
	}

	public String getSpecColloc() {
		return specColloc;
	}

	public void setSpecColloc(String specColloc) {
		this.specColloc = specColloc;
	}

	public int getKeyLoc() {
		return keyLoc;
	}

	public void setKeyLoc(int keyLoc) {
		this.keyLoc = keyLoc;
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

	public List getListaTipoDigit() {
		return listaTipoDigit;
	}

	public void setListaTipoDigit(List listaTipoDigit) {
		this.listaTipoDigit = listaTipoDigit;
	}

	public List getListaTecaDigitale() {
		return listaTecaDigitale;
	}

	public void setListaTecaDigitale(List listaTecaDigitale) {
		this.listaTecaDigitale = listaTecaDigitale;
	}

	public String getDatiAccessoRemoto() {
		return datiAccessoRemoto;
	}

	public void setDatiAccessoRemoto(String datiAccessoRemoto) {
		this.datiAccessoRemoto = datiAccessoRemoto;
	}

	public boolean isDisableProven() {
		return disableProven;
	}

	public void setDisableProven(boolean disableProven) {
		this.disableProven = disableProven;
	}

	public boolean isDisableTastoProven() {
		return disableTastoProven;
	}

	public void setDisableTastoProven(boolean disableTastoProven) {
		this.disableTastoProven = disableTastoProven;
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

	public String getTipoCollocN() {
		return tipoCollocN;
	}

	public void setTipoCollocN(String tipoCollocN) {
		this.tipoCollocN = tipoCollocN;
	}

	public String getDescrProvDefault() {
		return descrProvDefault;
	}

	public void setDescrProvDefault(String descrProvDefault) {
		this.descrProvDefault = descrProvDefault;
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

	public boolean isDisableTastoSalva() {
		return disableTastoSalva;
	}

	public void setDisableTastoSalva(boolean disableTastoSalva) {
		this.disableTastoSalva = disableTastoSalva;
	}

	public boolean isDisableTastoEsemplare() {
		return disableTastoEsemplare;
	}

	public void setDisableTastoEsemplare(boolean disableTastoEsemplare) {
		this.disableTastoEsemplare = disableTastoEsemplare;
	}

	public boolean isDisableTastoCancInv() {
		return disableTastoCancInv;
	}

	public void setDisableTastoCancInv(boolean disableTastoCancInv) {
		this.disableTastoCancInv = disableTastoCancInv;
	}

	public boolean isDisablePerModInvDaNav() {
		return disablePerModInvDaNav;
	}

	public void setDisablePerModInvDaNav(boolean disablePerModInvDaNav) {
		this.disablePerModInvDaNav = disablePerModInvDaNav;
	}

	public boolean isTrasferimento() {
		return trasferimento;
	}

	public void setTrasferimento(boolean trasferimento) {
		this.trasferimento = trasferimento;
	}

	public String getTipoColloc() {
		return tipoColloc;
	}

	public void setTipoColloc(String tipoColloc) {
		this.tipoColloc = tipoColloc;
	}

	public String getCodCollocFormato() {
		return codCollocFormato;
	}

	public void setCodCollocFormato(String codCollocFormato) {
		this.codCollocFormato = codCollocFormato;
	}

	public String getCodSpecifFormato() {
		return codSpecifFormato;
	}

	public void setCodSpecifFormato(String codSpecifFormato) {
		this.codSpecifFormato = codSpecifFormato;
	}

	public CodiceNotaVO getItemNota(int index) {

		// automatically grow List size
		while (index >= this.getListaNoteDinamica().size()) {
			this.getListaNoteDinamica().add(new CodiceNotaVO());
		}
		return this.getListaNoteDinamica().get(index);
	}

	public List<CodiceNotaVO> getListaNoteDinamica() {
		return listaNoteDinamica;
	}

	public void setListaNoteDinamica(List<CodiceNotaVO> listaNoteDinamica) {
		this.listaNoteDinamica = listaNoteDinamica;
	}

	public List getListaCodiciNote() {
		return listaCodiciNote;
	}

	public void setListaCodiciNote(List listaCodiciNote) {
		this.listaCodiciNote = listaCodiciNote;
	}

	public List addListaNoteDinamica(CodiceNotaVO recNota) {
		listaNoteDinamica.add(recNota);
		return listaNoteDinamica;
	}

	public List<EtichettaDettaglioVO> getListaEtichetteBarcode() {
		return listaEtichetteBarcode;
	}

	public void setListaEtichetteBarcode(
			List<EtichettaDettaglioVO> listaEtichetteBarcode) {
		this.listaEtichetteBarcode = listaEtichetteBarcode;
	}

	public String getAppoggioCampoObb() {
		return appoggioCampoObb;
	}

	public void setAppoggioCampoObb(String appoggioCampoObb) {
		this.appoggioCampoObb = appoggioCampoObb;
	}

	public boolean isAbilitaBottoneInviaInIndice() {
		return abilitaBottoneInviaInIndice;
	}

	public void setAbilitaBottoneInviaInIndice(
			boolean abilitaBottoneInviaInIndice) {
		this.abilitaBottoneInviaInIndice = abilitaBottoneInviaInIndice;
	}

	public boolean isFlRicarica() {
		return flRicarica;
	}

	public void setFlRicarica(boolean flRicarica) {
		this.flRicarica = flRicarica;
	}

	public boolean isDisableTastoAggiornaInIndice() {
		return disableTastoAggiornaInIndice;
	}

	public void setDisableTastoAggiornaInIndice(
			boolean disableTastoAggiornaInIndice) {
		this.disableTastoAggiornaInIndice = disableTastoAggiornaInIndice;
	}

	public List getListaDispDaRemoto() {
		return listaDispDaRemoto;
	}

	public void setListaDispDaRemoto(List listaDispDaRemoto) {
		this.listaDispDaRemoto = listaDispDaRemoto;
	}

	public String getAnnoFattura() {
		return annoFattura;
	}

	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
	}

	public List getListaComboBC() {
		return ListaComboBC;
	}

	public void setListaComboBC(List listaComboBC) {
		ListaComboBC = listaComboBC;
	}

	public boolean isDisableDataCarico() {
		return disableDataCarico;
	}

	public void setDisableDataCarico(boolean disableDataCarico) {
		this.disableDataCarico = disableDataCarico;
	}

	public boolean isDisableCodCarico() {
		return disableCodCarico;
	}

	public void setDisableCodCarico(boolean disableCodCarico) {
		this.disableCodCarico = disableCodCarico;
	}

	public boolean isDisableNumCarico() {
		return disableNumCarico;
	}

	public void setDisableNumCarico(boolean disableNumCarico) {
		this.disableNumCarico = disableNumCarico;
	}

	public List getListaBuoniCarico() {
		return listaBuoniCarico;
	}

	public void setListaBuoniCarico(List listaBuoniCarico) {
		this.listaBuoniCarico = listaBuoniCarico;
	}

	public String getNumCarico() {
		return numCarico;
	}

	public void setNumCarico(String numCarico) {
		this.numCarico = numCarico;
	}

}
