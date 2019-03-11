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

import it.iccu.sbn.ejb.vo.documentofisico.CodiceNotaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class EsameCollocEsaminaInventarioForm extends ActionForm {


	private static final long serialVersionUID = -2924927047309591112L;
	private InventarioDettaglioVO recInvDett = new InventarioDettaglioVO();
	private CollocazioneVO recColl = new CollocazioneVO();
	private DatiBibliograficiCollocazioneVO reticolo =  null;
	private String codBib;
	private String descrBib;
	private String codSerie;
	private String codInvent;
	private String descrSitAmm;
	private String descrMatInv;
	private String descrStatoConser;
	private String descrTipoFruizione;
	private String descrTipoAcquisizione;
	private String descrSupportoCopia;
	private String descrRiproducibilta;
	private String descrTipoDigit;
	private String descrTecaDigitale;
	private String descrDispDaRemoto;
	private String descrNoDispo;
	private String prov;
	private String descrCodSit;
	private boolean disable;
	private boolean sessione;
	private boolean noReticolo;
	private String folder;
	private String tasto;
	private String ticket;
	private String codPolo;
	private boolean periodico;
	private String isbdDiCollocazione;
	private String versoBiblDescr;
	private String descrCodCarico;
	private List<CodiceNotaVO> listaNote = new ArrayList<CodiceNotaVO>();


	public String getDescrCodCarico() {
		return descrCodCarico;
	}
	public void setDescrCodCarico(String descrCodCarico) {
		this.descrCodCarico = descrCodCarico;
	}

	public List<CodiceNotaVO> getListaNote() {
		return listaNote;
	}
	public void setListaNote(List<CodiceNotaVO> listaNote) {
		this.listaNote = listaNote;
	}

	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
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
	public String getTasto() {
		return tasto;
	}
	public void setTasto(String tasto) {
		this.tasto = tasto;
	}
	public String getDescrMatInv() {
		return descrMatInv;
	}
	public void setDescrMatInv(String descrMatInv) {
		this.descrMatInv = descrMatInv;
	}
	public String getDescrNoDispo() {
		return descrNoDispo;
	}
	public void setDescrNoDispo(String descrNoDispo) {
		this.descrNoDispo = descrNoDispo;
	}
	public String getDescrSitAmm() {
		return descrSitAmm;
	}
	public void setDescrSitAmm(String descrSitAmm) {
		this.descrSitAmm = descrSitAmm;
	}
	public String getDescrStatoConser() {
		return descrStatoConser;
	}
	public void setDescrStatoConser(String descrStatoConser) {
		this.descrStatoConser = descrStatoConser;
	}
	public String getDescrTipoFruizione() {
		return descrTipoFruizione;
	}
	public void setDescrTipoFruizione(String descrTipoFruizione) {
		this.descrTipoFruizione = descrTipoFruizione;
	}
	public String getDescrCodSit() {
		return descrCodSit;
	}
	public void setDescrCodSit(String descrCodSit) {
		this.descrCodSit = descrCodSit;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCodInvent() {
		return codInvent;
	}
	public void setCodInvent(String codInvent) {
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
	public void setCodPolo(String codPolo) {
		this.codPolo=codPolo;

	}
	public String getCodPolo() {
		return codPolo;
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
	public CollocazioneVO getRecColl() {
		return recColl;
	}
	public void setRecColl(CollocazioneVO recColl) {
		this.recColl = recColl;
	}
	public InventarioDettaglioVO getRecInvDett() {
		return recInvDett;
	}
	public void setRecInvDett(InventarioDettaglioVO recInvDett) {
		this.recInvDett = recInvDett;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getDescrSupportoCopia() {
		return descrSupportoCopia;
	}
	public void setDescrSupportoCopia(String descrSupportoCopia) {
		this.descrSupportoCopia = descrSupportoCopia;
	}
	public String getDescrRiproducibilta() {
		return descrRiproducibilta;
	}
	public void setDescrRiproducibilta(String descrRiproducibilta) {
		this.descrRiproducibilta = descrRiproducibilta;
	}
	public String getDescrTipoDigit() {
		return descrTipoDigit;
	}
	public void setDescrTipoDigit(String descrTipoDigit) {
		this.descrTipoDigit = descrTipoDigit;
	}
	public String getDescrTecaDigitale() {
		return descrTecaDigitale;
	}
	public void setDescrTecaDigitale(String descrTecaDigitale) {
		this.descrTecaDigitale = descrTecaDigitale;
	}
	public String getDescrTipoAcquisizione() {
		return descrTipoAcquisizione;
	}
	public void setDescrTipoAcquisizione(String descrTipoAcquisizione) {
		this.descrTipoAcquisizione = descrTipoAcquisizione;
	}
	public boolean isPeriodico() {
		return periodico;
	}
	public void setPeriodico(boolean periodico) {
		this.periodico = periodico;
	}
	public String getIsbdDiCollocazione() {
		return isbdDiCollocazione;
	}
	public void setIsbdDiCollocazione(String isbdDiCollocazione) {
		this.isbdDiCollocazione = isbdDiCollocazione;
	}
	public String getVersoBiblDescr() {
		return versoBiblDescr;
	}
	public void setVersoBiblDescr(String versoBiblDescr) {
		this.versoBiblDescr = versoBiblDescr;
	}
	public String getDescrDispDaRemoto() {
		return descrDispDaRemoto;
	}
	public void setDescrDispDaRemoto(String descrDispDaRemoto) {
		this.descrDispDaRemoto = descrDispDaRemoto;
	}
}
