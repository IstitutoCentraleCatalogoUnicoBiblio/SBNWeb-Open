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
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;

import org.apache.struts.action.ActionForm;

public class EsameCollocGestioneEsemplareForm extends ActionForm {


	private static final long serialVersionUID = -3228175099834450943L;
	private EsemplareDettaglioVO recEsempl = new EsemplareDettaglioVO();
	private DatiBibliograficiCollocazioneVO reticolo =  new DatiBibliograficiCollocazioneVO();
	private CollocazioneDettaglioVO recColl = new CollocazioneDettaglioVO();

	private String dataAgg;
	private String dataIns;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String bid;
	private String bidDoc;
	private String bidDocDescr;
	private int codDoc;
	private int keyLoc;
	private String titoloBid;
	private String ticket;
	private String consistenzaEsemplare;
	private String tipoOperazione;
	private boolean sessione;
	private boolean disable;
	private boolean disablConsist;
	private boolean esamina = false;
	private boolean nuovo = false;
	private String codSerie;
	private String codInv;
	private String codSez;
	private String codColl;
	private String codSpec;
	private String prov;
	private boolean abilitaBottoneInviaInIndice;

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
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	private boolean conferma;

	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public String getConsistenzaEsemplare() {
		return consistenzaEsemplare;
	}
	public void setConsistenzaEsemplare(String consistenzaEsemplare) {
		this.consistenzaEsemplare = consistenzaEsemplare;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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
	public String getTitoloBid() {
		return titoloBid;
	}
	public void setTitoloBid(String titoloBid) {
		this.titoloBid = titoloBid;
	}
	public int getCodDoc() {
		return codDoc;
	}
	public void setCodDoc(int codDoc) {
		this.codDoc = codDoc;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public EsemplareDettaglioVO getRecEsempl() {
		return recEsempl;
	}
	public void setRecEsempl(EsemplareDettaglioVO recEsempl) {
		this.recEsempl = recEsempl;
	}
	public String getDataAgg() {
		return dataAgg;
	}
	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public boolean isDisablConsist() {
		return disablConsist;
	}
	public void setDisablConsist(boolean disablConsist) {
		this.disablConsist = disablConsist;
	}
	public boolean isEsamina() {
		return esamina;
	}
	public void setEsamina(boolean esamina) {
		this.esamina = esamina;
	}
	public String getBidDoc() {
		return bidDoc;
	}
	public void setBidDoc(String bidDoc) {
		this.bidDoc = bidDoc;
	}
	public String getBidDocDescr() {
		return bidDocDescr;
	}
	public void setBidDocDescr(String bidDocDescr) {
		this.bidDocDescr = bidDocDescr;
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
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public boolean isNuovo() {
		return nuovo;
	}
	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}
	public DatiBibliograficiCollocazioneVO getReticolo() {
		return reticolo;
	}
	public void setReticolo(DatiBibliograficiCollocazioneVO reticolo) {
		this.reticolo = reticolo;
	}
	public String getCodColl() {
		return codColl;
	}
	public void setCodColl(String codColl) {
		this.codColl = codColl;
	}
	public String getCodSez() {
		return codSez;
	}
	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}
	public String getCodSpec() {
		return codSpec;
	}
	public void setCodSpec(String codSpec) {
		this.codSpec = codSpec;
	}
	public CollocazioneDettaglioVO getRecColl() {
		return recColl;
	}
	public void setRecColl(CollocazioneDettaglioVO recColl) {
		this.recColl = recColl;
	}
	public boolean isAbilitaBottoneInviaInIndice() {
		return abilitaBottoneInviaInIndice;
	}
	public void setAbilitaBottoneInviaInIndice(boolean abilitaBottoneInviaInIndice) {
		this.abilitaBottoneInviaInIndice = abilitaBottoneInviaInIndice;
	}
}
