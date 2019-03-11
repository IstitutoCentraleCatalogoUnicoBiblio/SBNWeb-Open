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

import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ListeInventariForm extends ActionForm {


	private static final long serialVersionUID = 2420723557334396295L;
	EsameCollocRicercaVO paramRicerca = new EsameCollocRicercaVO();
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codSerie;
	private String codSez;
	private String codLoc;
	private String specLoc;
	private List listaInventari;
	private List listaBiblioteche;
	private String selectedInv;
	private boolean disable;
	private boolean noInv;
	private boolean sessione;
	private String ticket;
	private String prov;
	private String tipoLista;
	private int keyLoc;
	private boolean conferma;

	//parametri ordine
	private String codBibO;
	private String codTipoOrd;
	private int annoOrd;
	private int codOrd;
	private String codBibF;
	private String bidOrd;
	private String isbdOrd;
	private String bid;
	private String isbd;

	//parametri fattura
	private String codBibFattura;
	private int annoFattura;
	private int progrFattura;
	private int rigaFattura;
	private double prezzo;
	private String tipoOperazione;

	//possessori
	private String pid;
	private String descrPid;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String idLista;
	private int totBlocchi;
	private int totRighe;
	private int bloccoSelezionato=0;
	private boolean abilitaBottoneCarBlocchi;
	private boolean caricoAltriBlocchi;
	private List newLista;
	private List oldLista;
	private int blocchiTotali;
	private String ultCodSerie;
	private String ultCodInv;

	private int numInventari;

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
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}
	public List getListaInventari() {
		return listaInventari;
	}
	public void setListaInventari(List listaInventari) {
		this.listaInventari = listaInventari;
	}
	public boolean isNoInv() {
		return noInv;
	}
	public void setNoInv(boolean noInv) {
		this.noInv = noInv;
	}
	public String getSelectedInv() {
		return selectedInv;
	}
	public void setSelectedInv(String selectedInv) {
		this.selectedInv = selectedInv;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getCodSerie() {
		return codSerie;
	}
	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}
	public int getNumInventari() {
		return numInventari;
	}
	public void setNumInventari(int numInventari) {
		this.numInventari = numInventari;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public EsameCollocRicercaVO getParamRicerca() {
		return paramRicerca;
	}
	public void setParamRicerca(EsameCollocRicercaVO paramRicerca) {
		this.paramRicerca = paramRicerca;
	}
//	public void reset(ActionMapping mapping, HttpServletRequest request) {
//		this.setSelectedInv("");
//	       }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if (request.getParameter("navigation") == null)
			this.selectedInv=null;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
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
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
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
	public int getAnnoFattura() {
		return annoFattura;
	}
	public void setAnnoFattura(int annoFattura) {
		this.annoFattura = annoFattura;
	}
	public int getAnnoOrd() {
		return annoOrd;
	}
	public void setAnnoOrd(int annoOrd) {
		this.annoOrd = annoOrd;
	}
	public String getCodBibF() {
		return codBibF;
	}
	public void setCodBibF(String codBibF) {
		this.codBibF = codBibF;
	}
	public String getCodBibFattura() {
		return codBibFattura;
	}
	public void setCodBibFattura(String codBibFattura) {
		this.codBibFattura = codBibFattura;
	}
	public int getCodOrd() {
		return codOrd;
	}
	public void setCodOrd(int codOrd) {
		this.codOrd = codOrd;
	}
	public String getCodTipoOrd() {
		return codTipoOrd;
	}
	public void setCodTipoOrd(String codTipoOrd) {
		this.codTipoOrd = codTipoOrd;
	}
	public int getProgrFattura() {
		return progrFattura;
	}
	public void setProgrFattura(int progrFattura) {
		this.progrFattura = progrFattura;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCodBibO() {
		return codBibO;
	}
	public void setCodBibO(String codBibO) {
		this.codBibO = codBibO;
	}
	public int getKeyLoc() {
		return keyLoc;
	}
	public void setKeyLoc(int keyLoc) {
		this.keyLoc = keyLoc;
	}
	public String getCodSez() {
		return codSez;
	}
	public void setCodSez(String codSez) {
		this.codSez = codSez;
	}
	public String getCodLoc() {
		return codLoc;
	}
	public void setCodLoc(String codLoc) {
		this.codLoc = codLoc;
	}
	public String getSpecLoc() {
		return specLoc;
	}
	public void setSpecLoc(String specLoc) {
		this.specLoc = specLoc;
	}
	public String getTipoLista() {
		return tipoLista;
	}
	public void setTipoLista(String tipoLista) {
		this.tipoLista = tipoLista;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getIsbd() {
		return isbd;
	}
	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}
	public String getBidOrd() {
		return bidOrd;
	}
	public void setBidOrd(String bidOrd) {
		this.bidOrd = bidOrd;
	}
	public String getIsbdOrd() {
		return isbdOrd;
	}
	public void setIsbdOrd(String isbdOrd) {
		this.isbdOrd = isbdOrd;
	}
	public int getRigaFattura() {
		return rigaFattura;
	}
	public void setRigaFattura(int rigaFattura) {
		this.rigaFattura = rigaFattura;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public double getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getDescrPid() {
		return descrPid;
	}
	public void setDescrPid(String descrPid) {
		this.descrPid = descrPid;
	}
	public boolean isCaricoAltriBlocchi() {
		return caricoAltriBlocchi;
	}
	public void setCaricoAltriBlocchi(boolean caricoAltriBlocchi) {
		this.caricoAltriBlocchi = caricoAltriBlocchi;
	}
	public List getNewLista() {
		return newLista;
	}
	public void setNewLista(List newLista) {
		this.newLista = newLista;
	}
	public int getBlocchiTotali() {
		return blocchiTotali;
	}
	public void setBlocchiTotali(int blocchiTotali) {
		this.blocchiTotali = blocchiTotali;
	}
	public List getOldLista() {
		return oldLista;
	}
	public void setOldLista(List oldLista) {
		this.oldLista = oldLista;
	}
	public String getUltCodSerie() {
		return ultCodSerie;
	}
	public void setUltCodSerie(String ultCodSerie) {
		this.ultCodSerie = ultCodSerie;
	}
	public String getUltCodInv() {
		return ultCodInv;
	}
	public void setUltCodInv(String ultCodInv) {
		this.ultCodInv = ultCodInv;
	}
}
