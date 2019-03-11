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

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class RichiestaServizioLocForm extends ListaMovimentiForm {

	private static final long serialVersionUID = -8952170374928110538L;

	public enum RichiestaListaMovimentiType {
		CANCELLA, RIFIUTA, PRENOTAZIONE;
	}

	private String successoRich;
	private String tariffa;
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
	private String	dataLimInteresse;
	private String	spesaMax;
	private String	volInter;
	private String	numFasc;
	private String	annoPeriodico;
	private String	noteUte;
	private String servizio;


	private String dataDisponibDocumento;//18 private String dataMov;
	private String dataPrevRitiroDocumento;//16
	private String intcopia;

	private String sala;
	private String posto;

	private MovimentoListaVO detMov = new MovimentoListaVO();
	//contiene i campi da caricare sulla jsp
	private List<ServizioWebDatiRichiestiVO> mostraCampi;

	private boolean lettura;
	private String salvaNoteUtente;
	private MovimentoListaVO movimentoSalvato = new MovimentoListaVO();

	private List<ComboCodDescVO> dataPrevRitDoc;

	private String apintCopie = "";
	private int  totCopieRich = 0;

	private boolean copyright;

	private DatiRichiestaILLVO dati;
	private List<TB_CODICI> listaPaesi;
	private List<TB_CODICI> listaProvincia;

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

	//dati passati al dettaglio
	private MovimentoListaVO mov = new MovimentoListaVO();
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		codSelMov = new Long[] {};
	}

	public MovimentoListaVO getMov() {
		return mov;
	}

	public void setMov(MovimentoListaVO mov) {
		this.mov = mov;
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

	public void setMostraCampi(List<ServizioWebDatiRichiestiVO> campiVisJsp) {
		this.mostraCampi = campiVisJsp;
	}

	public List<ServizioWebDatiRichiestiVO> getMostraCampi() {
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

	public void setIntcopia(String intcopia) {
		this.intcopia = intcopia;
	}

	public String getIntcopia() {
		return intcopia;
	}

	public String getTariffa() {
		return tariffa;
	}

	public void setTariffa(String tariffa) {
		this.tariffa = tariffa;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setSalvaNoteUtente(String salvaNoteUtente) {
		this.salvaNoteUtente = salvaNoteUtente;
	}

	public String getSalvaNoteUtente() {
		return salvaNoteUtente;
	}

	public void setMovimentoSalvato(MovimentoListaVO movimentoSalvato) {
		this.movimentoSalvato = movimentoSalvato;
	}

	public MovimentoListaVO getMovimentoSalvato() {
		return movimentoSalvato;
	}

	public void setLettura(boolean lettura) {
		this.lettura = lettura;
	}

	public boolean isLettura() {
		return lettura;
	}

	public void setDataPrevRitDoc(List<ComboCodDescVO> dataPrevRitDoc) {
		this.dataPrevRitDoc = dataPrevRitDoc;
	}

	public List<ComboCodDescVO> getDataPrevRitDoc() {
		return dataPrevRitDoc;
	}

	public String getApintCopie() {
		return apintCopie;
	}

	public void setApintCopie(String apintCopie) {
		this.apintCopie = apintCopie;
	}

	public int getTotCopieRich() {
		return totCopieRich;
	}

	public void setTotCopieRich(int totCopieRich) {
		this.totCopieRich = totCopieRich;
	}

	public boolean isCopyright() {
		return copyright;
	}

	public void setCopyright(boolean copyright) {
		this.copyright = copyright;
	}

	public DatiRichiestaILLVO getDati() {
		return dati;
	}

	public void setDati(DatiRichiestaILLVO dati) {
		this.dati = null;
		if (dati != null)
			this.dati = dati;
	}

	public List<TB_CODICI> getListaPaesi() {
		return listaPaesi;
	}

	public void setListaPaesi(List<TB_CODICI> listaPaesi) {
		this.listaPaesi = listaPaesi;
	}

	public List<TB_CODICI> getListaProvincia() {
		return listaProvincia;
	}

	public void setListaProvincia(List<TB_CODICI> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}

}
