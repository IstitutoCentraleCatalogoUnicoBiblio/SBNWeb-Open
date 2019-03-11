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

import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class VaiAInserimentoInvForm extends ActionForm {


	private static final long serialVersionUID = 553291636849424852L;
	private InventarioVO recInv = new InventarioVO();
	private SerieVO recSerie = new SerieVO();
	private TitoloVO recTitolo = new TitoloVO();
	private DatiBibliograficiCollocazioneVO reticolo;
	private List listaInvTitolo = new ArrayList();
	private List listaComboSerie = new ArrayList();
	private List listaSerie = new ArrayList();
	private String ticket;
	//array inventari creati
	private List listaInvCreati = new ArrayList();
	//numero inventario inserito per tipo op. N o S
	private String codPolo;
	private String codBib;
	private String descrBib;
	//numero inventario inserito per tipo op. N o S
	private int    codInv = 0;
	private String tipoOperazione;
	private String tipoPrgDefault;
	private String codSerieDefault;
	private String test;//validazione onchange su serie o tipoOp

	//numero inventari da assegnare
	private int nInv;
	private String bid;
	private String titolo;
	private String serie;
//	private String precInv1;
	//inventari da assegnare da ordini
	private String codTipoOrd;
	private int annoOrd;
	private int codOrd;
	private String prov;
	private String codBibO;
	private int codFornitore;
	private String descrFornitore;

	private boolean disable;
	private boolean disableDataIngresso;
	private boolean sessione;
	private boolean noSerie;
	private boolean noReticolo;
	private boolean conferma = false;
	private String tastoOk;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );

	public int getAnnoOrd() {
		return annoOrd;
	}
	public void setAnnoOrd(int annoOrd) {
		this.annoOrd = annoOrd;
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
	public String getCodBibO() {
		return codBibO;
	}
	public void setCodBibO(String codBibO) {
		this.codBibO = codBibO;
	}
	public int getCodFornitore() {
		return codFornitore;
	}
	public void setCodFornitore(int codFornitore) {
		this.codFornitore = codFornitore;
	}
	public int getCodInv() {
		return codInv;
	}
	public void setCodInv(int codInv) {
		this.codInv = codInv;
	}
	public int getCodOrd() {
		return codOrd;
	}
	public void setCodOrd(int codOrd) {
		this.codOrd = codOrd;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodTipoOrd() {
		return codTipoOrd;
	}
	public void setCodTipoOrd(String codTipoOrd) {
		this.codTipoOrd = codTipoOrd;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
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
	public List getListaComboSerie() {
		return listaComboSerie;
	}
	public void setListaComboSerie(List listaComboSerie) {
		this.listaComboSerie = listaComboSerie;
	}
	public List getListaInvCreati() {
		return listaInvCreati;
	}
	public void setListaInvCreati(List listaInvCreati) {
		this.listaInvCreati = listaInvCreati;
	}
	public List getListaInvTitolo() {
		return listaInvTitolo;
	}
	public void setListaInvTitolo(List listaInvTitolo) {
		this.listaInvTitolo = listaInvTitolo;
	}
	public List getListaSerie() {
		return listaSerie;
	}
	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}
	public int getNInv() {
		return nInv;
	}
	public void setNInv(int inv) {
		nInv = inv;
	}
	public boolean isNoSerie() {
		return noSerie;
	}
	public void setNoSerie(boolean noSerie) {
		this.noSerie = noSerie;
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
	public SerieVO getRecSerie() {
		return recSerie;
	}
	public void setRecSerie(SerieVO recSerie) {
		this.recSerie = recSerie;
	}
	public DatiBibliograficiCollocazioneVO getReticolo() {
		return reticolo;
	}
	public void setReticolo(DatiBibliograficiCollocazioneVO reticolo) {
		this.reticolo = reticolo;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTastoOk() {
		return tastoOk;
	}
	public void setTastoOk(String tastoOk) {
		this.tastoOk = tastoOk;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getDescrFornitore() {
		return descrFornitore;
	}
	public void setDescrFornitore(String descrFornitore) {
		this.descrFornitore = descrFornitore;
	}
	public boolean isNoReticolo() {
		return noReticolo;
	}
	public void setNoReticolo(boolean noReticolo) {
		this.noReticolo = noReticolo;
	}
	public boolean isDisableDataIngresso() {
		return disableDataIngresso;
	}
	public void setDisableDataIngresso(boolean disableDataIngresso) {
		this.disableDataIngresso = disableDataIngresso;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public String getTipoPrgDefault() {
		return tipoPrgDefault;
	}
	public void setTipoPrgDefault(String tipoPrgDefault) {
		this.tipoPrgDefault = tipoPrgDefault;
	}
	public String getCodSerieDefault() {
		return codSerieDefault;
	}
	public void setCodSerieDefault(String codSerieDefault) {
		this.codSerieDefault = codSerieDefault;
	}
	public TitoloVO getRecTitolo() {
		return recTitolo;
	}
	public void setRecTitolo(TitoloVO recTitolo) {
		this.recTitolo = recTitolo;
	}

}
