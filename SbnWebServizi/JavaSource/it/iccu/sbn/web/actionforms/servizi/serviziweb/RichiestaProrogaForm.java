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
package it.iccu.sbn.web.actionforms.servizi.serviziweb;

import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class RichiestaProrogaForm extends ListaMovimentiForm {

	private static final long serialVersionUID = -3071041111034538360L;

	private String successoRich;
	private int tariffa;
	private int pagineRiprod;
	private String supporto;
	private String cod_biblio;
	private String bibsel;
	private String utenteCon;
	private String ambiente;

	private String autore;
	private String titolo;
	private String anno;

	private String dataRic;
	private String dataMaxProroga;

	private String dataMinProroga;
	private  String dataMaxProrogabile;
	private String	dataLimInteresse;
	private String	spesaMax;
	private String	volInter;
	private String	numFasc;
	private String	annoPeriodico;
	private String	noteUte;
	private String servizio;


	private String dataDisponibDocumento;//18 private String dataMov;
	private String dataPrevRitiroDocumento;//16
	private int intcopia;
	private int intcopiaFine;
	private String sala;
	private String posto;

	private MovimentoListaVO detMov = new MovimentoListaVO();
	//contiene i campi da caricare sulla jsp
	private List mostraCampi;
	private List dataPrevRitDoc;

	public void setDataPrevRitDoc(List dataPrevRitDoc) {
		this.dataPrevRitDoc = dataPrevRitDoc;
	}

	public List getDataPrevRitDoc() {
		return dataPrevRitDoc;
	}
	public String getDataRic() {
		return dataRic;
	}

	public void setDataRic(String setDataRic) {
		this.dataRic = setDataRic;
	}

	public String getDataLimInteresse() {
		return dataLimInteresse;
	}

	public void setDataLimInteresse(String dataLimInteresse) {
		this.dataLimInteresse = dataLimInteresse;
	}

	public String getSpesaMax() {
		return spesaMax;
	}

	public void setSpesaMax(String spesaMax) {
		this.spesaMax = spesaMax;
	}

	public String getVolInter() {
		return volInter;
	}

	public void setVolInter(String volInter) {
		this.volInter = volInter;
	}

	public String getNumFasc() {
		return numFasc;
	}

	public void setNumFasc(String numFasc) {
		this.numFasc = numFasc;
	}

	public String getAnnoPeriodico() {
		return annoPeriodico;
	}

	public void setAnnoPeriodico(String annoPeriodico) {
		this.annoPeriodico = annoPeriodico;
	}

	public String getNoteUte() {
		return noteUte;
	}

	public void setNoteUte(String noteUte) {
		this.noteUte = noteUte;
	}

	public String getSuccessoRich() {
		return successoRich;
	}

	public void setSuccessoRich(String successoRich) {
		this.successoRich = successoRich;
	}


	public int getPagineRiprod() {
		return pagineRiprod;
	}

	public void setPagineRiprod(int pagineRiprod) {
		this.pagineRiprod = pagineRiprod;
	}

	public String getSupporto() {
		return supporto;
	}

	public void setSupporto(String supporto) {
		this.supporto = supporto;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		codSelMov = new Long[] {};
	}

	public int getListaMovRicUteSize() {
		return (listaMovRicUte == null ? 0 : listaMovRicUte.size());
	}

	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}

	public String getUtenteCon() {
		return utenteCon;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getAutore() {
		return autore;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getAnno() {
		return anno;
	}

	public void setServizio(String servizio) {
		this.servizio = servizio;
	}

	public String getServizio() {
		return servizio;
	}

	public void setTariffa(int tariffa) {
		this.tariffa = tariffa;
	}

	public int getTariffa() {
		return tariffa;
	}

	public void setBibsel(String bibsel) {
		this.bibsel = bibsel;
	}

	public String getBibsel() {
		return bibsel;
	}

	public void setCod_biblio(String cod_biblio) {
		this.cod_biblio = cod_biblio;
	}

	public String getCod_biblio() {
		return cod_biblio;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getSala() {
		return sala;
	}

	public void setPosto(String posto) {
		this.posto = posto;
	}

	public String getPosto() {
		return posto;
	}

	public void setMostraCampi(List campiVisJsp) {
		this.mostraCampi = campiVisJsp;
	}

	public List getMostraCampi() {
		return mostraCampi;
	}

	public void setDataDisponibDocumento(String dataDisponibDocumento) {
		this.dataDisponibDocumento = dataDisponibDocumento;
	}

	public String getDataDisponibDocumento() {
		return dataDisponibDocumento;
	}

	public void setDataPrevRitiroDocumento(String dataPrevRitiroDocumento) {
		this.dataPrevRitiroDocumento = dataPrevRitiroDocumento;
	}

	public String getDataPrevRitiroDocumento() {
		return dataPrevRitiroDocumento;
	}

	public void setDetMov(MovimentoListaVO detMov) {
		this.detMov = detMov;
	}

	public MovimentoListaVO getDetMov() {
		return detMov;
	}

	public void setIntcopiaFine(int intcopiaFine) {
		this.intcopiaFine = intcopiaFine;
	}

	public int getIntcopiaFine() {
		return intcopiaFine;
	}

	public void setIntcopia(int intcopia) {
		this.intcopia = intcopia;
	}

	public int getIntcopia() {
		return intcopia;
	}

	public void setDataMaxProroga(String dataMaxProroga) {
		this.dataMaxProroga = dataMaxProroga;
	}

	public String getDataMaxProroga() {
		return dataMaxProroga;
	}

	public void setDataMinProroga(String dataMinProroga) {
		this.dataMinProroga = dataMinProroga;
	}

	public String getDataMinProroga() {
		return dataMinProroga;
	}

	public void setDataMaxProrogabile(String dataMaxProrogabile) {
		this.dataMaxProrogabile = dataMaxProrogabile;
	}

	public String getDataMaxProrogabile() {
		return dataMaxProrogabile;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getAmbiente() {
		return ambiente;
	}



}
