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
package it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ComunicazioniRicercaParzialeForm extends ActionForm implements AcquisizioniBaseFormIntf {

	private static final long serialVersionUID = -3118355846901085632L;
	private String codPolo;
	private String codBibl="";
	private String statoComunicazione;
	List listaStatoComunicazione;
	private String tipoInvioComunicazione;
	private String codMessaggio;
	private String codFornitore;
	private String fornitore;
	private FornitoreVO fornitoreVO;
	private String dataInizio;
	private String dataFine;
	private String annoOrdine;
	private String codiceOrdine;
	private String annoFattura;
	private String progressivoFattura;
	private String tipoDocumento;
	List listaTipoDocumento;
	private String direzioneComunicazione;
	List listaDirezioneComunicazione;
	private String tipoMessaggio;
	List ListaTipoMessaggio;
	private String tipoOrdine;
	List ListaTipoOrdine;
	private boolean sessione;
	private boolean visibilitaIndietroLS=false;
	private int elemXBlocchi=10;
	private String tipoOrdinamSelez;
	private List listaTipiOrdinam;
	private boolean LSRicerca=false;
	private boolean disabilitaTutto=false;
	private boolean rientroDaSif=false;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public String getAnnoFattura() {
		return annoFattura;
	}

	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
	}

	public String getAnnoOrdine() {
		return annoOrdine;
	}

	public void setAnnoOrdine(String annoOrdine) {
		this.annoOrdine = annoOrdine;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public String getCodiceOrdine() {
		return codiceOrdine;
	}

	public void setCodiceOrdine(String codiceOrdine) {
		this.codiceOrdine = codiceOrdine;
	}

	public String getCodMessaggio() {
		return codMessaggio;
	}

	public void setCodMessaggio(String codMessaggio) {
		this.codMessaggio = codMessaggio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDirezioneComunicazione() {
		return direzioneComunicazione;
	}

	public void setDirezioneComunicazione(String direzioneComunicazione) {
		this.direzioneComunicazione = direzioneComunicazione;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public List getListaDirezioneComunicazione() {
		return listaDirezioneComunicazione;
	}

	public void setListaDirezioneComunicazione(List listaDirezioneComunicazione) {
		this.listaDirezioneComunicazione = listaDirezioneComunicazione;
	}

	public List getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public List getListaTipoMessaggio() {
		return ListaTipoMessaggio;
	}

	public void setListaTipoMessaggio(List listaTipoMessaggio) {
		ListaTipoMessaggio = listaTipoMessaggio;
	}

	public List getListaTipoOrdine() {
		return ListaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		ListaTipoOrdine = listaTipoOrdine;
	}

	public String getProgressivoFattura() {
		return progressivoFattura;
	}

	public void setProgressivoFattura(String progressivoFattura) {
		this.progressivoFattura = progressivoFattura;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoMessaggio() {
		return tipoMessaggio;
	}

	public void setTipoMessaggio(String tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
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

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public boolean isVisibilitaIndietroLS() {
		return visibilitaIndietroLS;
	}

	public void setVisibilitaIndietroLS(boolean visibilitaIndietroLS) {
		this.visibilitaIndietroLS = visibilitaIndietroLS;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getStatoComunicazione() {
		return statoComunicazione;
	}

	public void setStatoComunicazione(String statoComunicazione) {
		this.statoComunicazione = statoComunicazione;
	}

	public String getTipoInvioComunicazione() {
		return tipoInvioComunicazione;
	}

	public void setTipoInvioComunicazione(String tipoInvioComunicazione) {
		this.tipoInvioComunicazione = tipoInvioComunicazione;
	}

	public boolean isLSRicerca() {
		return LSRicerca;
	}

	public void setLSRicerca(boolean ricerca) {
		LSRicerca = ricerca;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public List getListaStatoComunicazione() {
		return listaStatoComunicazione;
	}

	public void setListaStatoComunicazione(List listaStatoComunicazione) {
		this.listaStatoComunicazione = listaStatoComunicazione;
	}

	public boolean isRientroDaSif() {
		return rientroDaSif;
	}

	public void setRientroDaSif(boolean rientroDaSif) {
		this.rientroDaSif = rientroDaSif;
	}

	public FornitoreVO getFornitoreVO() {
		return fornitoreVO;
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		this.fornitoreVO = fornitoreVO;
	}


}
