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
package it.iccu.sbn.web.actionforms.acquisizioni.fornitori;

import it.iccu.sbn.ejb.vo.acquisizioni.DatiFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EsaminaFornitoreForm extends ActionForm {


	private static final long serialVersionUID = -3792952658505280146L;
	private String tipoForn;
	private List listaTipoForn;

	private String provinciaForn;
	private List listaProvinciaForn;

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Nuovi campi per la gestione degli editori
	private String editore;
	private String regioneForn;
	private List listaRegioneForn;
	private String isbnEditore1;
	private String isbnEditore2;
	private String isbnEditore3;
	private String selezRadioNumStandard;

	private String paeseForn;
	private List listaPaeseForn;

	private String valuta;
	List listaValuta;

	private FornitoreVO fornitore;
	private DatiFornitoreVO datiFornitore;

	private boolean sessione;

	private boolean conferma = false;
	private String pressioneBottone = null;

	private boolean disabilitaTutto=false;

	private List<ListaSuppFornitoreVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;

	private boolean assenzaFornitoreBibl=false;

	private boolean submitDinamico = false;
	private boolean esaminaInibito=false;
	private boolean gestProf =true;


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// Effettua i controlli solo se non è la prima
		// volta che si entra in questo metodo

		return errors;
	}

	public DatiFornitoreVO getDatiFornitore() {
		return datiFornitore;
	}

	public void setDatiFornitore(DatiFornitoreVO datiFornitore) {
		this.datiFornitore = datiFornitore;
	}

	public FornitoreVO getFornitore() {
		return fornitore;
	}

	public void setFornitore(FornitoreVO fornitore) {
		this.fornitore = fornitore;
	}

	public List getListaPaeseForn() {
		return listaPaeseForn;
	}

	public void setListaPaeseForn(List listaPaeseForn) {
		this.listaPaeseForn = listaPaeseForn;
	}

	public List getListaProvinciaForn() {
		return listaProvinciaForn;
	}

	public void setListaProvinciaForn(List listaProvinciaForn) {
		this.listaProvinciaForn = listaProvinciaForn;
	}

	public List getListaTipoForn() {
		return listaTipoForn;
	}

	public void setListaTipoForn(List listaTipoForn) {
		this.listaTipoForn = listaTipoForn;
	}

	public List getListaValuta() {
		return listaValuta;
	}

	public void setListaValuta(List listaValuta) {
		this.listaValuta = listaValuta;
	}

	public String getPaeseForn() {
		return paeseForn;
	}

	public void setPaeseForn(String paeseForn) {
		this.paeseForn = paeseForn;
	}

	public String getProvinciaForn() {
		return provinciaForn;
	}

	public void setProvinciaForn(String provinciaForn) {
		this.provinciaForn = provinciaForn;
	}

	public String getTipoForn() {
		return tipoForn;
	}

	public void setTipoForn(String tipoForn) {
		this.tipoForn = tipoForn;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public List<ListaSuppFornitoreVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(List<ListaSuppFornitoreVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
	}

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}

	public boolean isSubmitDinamico() {
		return submitDinamico;
	}

	public void setSubmitDinamico(boolean submitDinamico) {
		this.submitDinamico = submitDinamico;
	}

	public boolean isEnableScorrimento() {
		return enableScorrimento;
	}

	public void setEnableScorrimento(boolean enableScorrimento) {
		this.enableScorrimento = enableScorrimento;
	}

	public String getPressioneBottone() {
		return pressioneBottone;
	}

	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
	}

	public boolean isAssenzaFornitoreBibl() {
		return assenzaFornitoreBibl;
	}

	public void setAssenzaFornitoreBibl(boolean assenzaFornitoreBibl) {
		this.assenzaFornitoreBibl = assenzaFornitoreBibl;
	}

	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}

	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}

	public boolean isGestProf() {
		return gestProf;
	}

	public void setGestProf(boolean gestProf) {
		this.gestProf = gestProf;
	}

	public String getRegioneForn() {
		return regioneForn;
	}

	public void setRegioneForn(String regioneForn) {
		this.regioneForn = regioneForn;
	}

	public List getListaRegioneForn() {
		return listaRegioneForn;
	}

	public void setListaRegioneForn(List listaRegioneForn) {
		this.listaRegioneForn = listaRegioneForn;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getIsbnEditore1() {
		return isbnEditore1;
	}

	public void setIsbnEditore1(String isbnEditore1) {
		this.isbnEditore1 = isbnEditore1;
	}

	public String getIsbnEditore2() {
		return isbnEditore2;
	}

	public void setIsbnEditore2(String isbnEditore2) {
		this.isbnEditore2 = isbnEditore2;
	}

	public String getIsbnEditore3() {
		return isbnEditore3;
	}

	public void setIsbnEditore3(String isbnEditore3) {
		this.isbnEditore3 = isbnEditore3;
	}

	public String getSelezRadioNumStandard() {
		return selezRadioNumStandard;
	}

	public void setSelezRadioNumStandard(String selezRadioNumStandard) {
		this.selezRadioNumStandard = selezRadioNumStandard;
	}

	public TabellaNumSTDImpronteVO getItemIsbnEdit(int index) {

        // automatically grow List size
        while (index >= this.fornitore.getListaNumStandard().size()) {
        	this.fornitore.getListaNumStandard().add(new TabellaNumSTDImpronteVO());
        }
        return (TabellaNumSTDImpronteVO)this.fornitore.getListaNumStandard().get(index);
    }


}
