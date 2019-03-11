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
package it.iccu.sbn.web.integration.actionforms.servizi.erogazione;

import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SollecitiVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class InserimentoRichiestaForm extends ListaMovimentiForm {


	private static final long serialVersionUID = 8050772965580132243L;
	private String successoRich;
	private int pagineRiprod;
	private String cod_biblio;
	private String bibsel;
	private String utenteCon;
	private String autore;
	private String titolo;
	private String anno;



	//contiene tutti i campi del Modulo Richiesta
	private List mostraCampi;
	private String dataRic;
	private String	volInter;
	private String	numFasc;
	private String intcopia;
	private String	noteUte;
	private String	spesaMax;
	private int tariffa;
	private String	dataLimInteresse;
	private String dataPrevRitiroDocumento;
	private String dataDisponibDocumento;
	private String listaModalitaErogazione;
	private String listaSupporti;
	private String sala;
	private String posto;
	private String	annoPeriodico;
	public List getMostraCampi() {
		return mostraCampi;
	}
	public void setMostraCampi(List campiVisJsp) {
		this.mostraCampi = campiVisJsp;
	}
	public String getDataRic() {
		return dataRic;
	}
	public void setDataRic(String setDataRic) {
		this.dataRic = setDataRic;
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
	public String getIntcopia() {
		return intcopia;
	}
	public void setIntcopia(String intcopia) {
		this.intcopia = intcopia;
	}
	public String getNoteUte() {
		return noteUte;
	}
	public void setNoteUte(String noteUte) {
		this.noteUte = noteUte;
	}
	public String getSpesaMax() {
		return spesaMax;
	}
	public void setSpesaMax(String spesaMax) {
		this.spesaMax = spesaMax;
	}
	public int getTariffa() {
		return tariffa;
	}
	public void setTariffa(int tariffa) {
		this.tariffa = tariffa;
	}
	public String getDataLimInteresse() {
		return dataLimInteresse;
	}
	public void setDataLimInteresse(String dataLimInteresse) {
		this.dataLimInteresse = dataLimInteresse;
	}
	public String getDataPrevRitiroDocumento() {
		return dataPrevRitiroDocumento;
	}
	public void setDataPrevRitiroDocumento(String dataPrevRitiroDocumento) {
		this.dataPrevRitiroDocumento = dataPrevRitiroDocumento;
	}
	public String getDataDisponibDocumento() {
		return dataDisponibDocumento;
	}
	public void setDataDisponibDocumento(String dataDisponibDocumento) {
		this.dataDisponibDocumento = dataDisponibDocumento;
	}
	public String getListaModalitaErogazione() {
		return listaModalitaErogazione;
	}
	public void setListaModalitaErogazione(String listaModalitaErogazione) {
		this.listaModalitaErogazione = listaModalitaErogazione;
	}
	public String getListaSupporti() {
		return listaSupporti;
	}
	public void setListaSupporti(String listaSupporti) {
		this.listaSupporti = listaSupporti;
	}
	public String getSala() {
		return sala;
	}
	public void setSala(String sala) {
		this.sala = sala;
	}
	public String getPosto() {
		return posto;
	}
	public void setPosto(String posto) {
		this.posto = posto;
	}
	public String getAnnoPeriodico() {
		return annoPeriodico;
	}

	public void setAnnoPeriodico(String annoPeriodico) {
		this.annoPeriodico = annoPeriodico;
	}



	private String servizio;

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

	// Indica il chiamante. Utilizzato nel caso la Action relativa alla lista
	// movimenti
	// è chiamata da un'altra linea funzionale differente dai servizi
	private String chiamante;

	// descrizione segnatura per la quale fare la ricerca
	private String segnaturaRicerca;
	// sessione
	private boolean sessione = false;
	// conferma pre operazioni
	private boolean conferma = false;
	// booleano per gestione presenza eventuali solleciti
	private boolean mostraSolleciti = false;

	// liste e selezioni corpo pagina
	private List<MovimentoListaVO> listaMovRicUte;
	private String solleciti;
	private List<SollecitiVO> listaSollecitiUte = new ArrayList<SollecitiVO>();
	private Long[] codSelMov;
	private String codSelMovSing;
	private List<MovimentoListaVO> listaRichiesteScadute;
	private String codRichiestaScadutaSel;

	//dati passati al dettaglio
	private MovimentoListaVO mov = new MovimentoListaVO();
	//dati nuova richiesta creata
	private MovimentoVO nuovaRichiesta;

	//gestione blocchi
	private boolean abilitaBlocchi;
	private int totBlocchi;
	private String livelloRicerca;

	//filtri per nuova richiesta di servizio
	private String tipoServ;

	private String progrAtt;
	private String statoMov;
	private String codBibInv;
	private String codSerieInv;
	private String codInvenInv;
	private String desXRicSeg;
	private String codUtente;
	private String codBibUtente;
	private String codServNuovaRich;

	private List<ComboVO> elencoBib = new ArrayList<ComboVO>();

	public List<ComboVO> getElencoBib() {
		return elencoBib;
	}

	public void setElencoBib(List<ComboVO> elencoBib) {
		this.elencoBib = elencoBib;
	}

	private MovimentoVO anaMov = new MovimentoVO();
	private String supporto;

	public MovimentoVO getAnaMov() {
		return anaMov;
	}

	public void setAnaMov(MovimentoVO anaMov) {
		this.anaMov = anaMov;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		codSelMov = new Long[] {};
	}

	public boolean isAbilitaBlocchi() {
		return abilitaBlocchi;
	}

	public void setAbilitaBlocchi(boolean abilitaBlocchi) {
		this.abilitaBlocchi = abilitaBlocchi;
	}

	public Long[] getCodSelMov() {
		return codSelMov;
	}

	public void setCodSelMov(Long[] codSelMov) {
		this.codSelMov = codSelMov;
	}

	public String getCodSelMovSing() {
		return codSelMovSing;
	}

	public void setCodSelMovSing(String codSelMovSing) {
		this.codSelMovSing = codSelMovSing;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}


	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public List<MovimentoListaVO> getListaMovRicUte() {
		return listaMovRicUte;
	}

	public void setListaMovRicUte(List<MovimentoListaVO> listaMovRicUte) {
		this.listaMovRicUte = listaMovRicUte;
	}

	public MovimentoListaVO getMov() {
		return mov;
	}

	public void setMov(MovimentoListaVO mov) {
		this.mov = mov;
	}

	public String getProgrAtt() {
		return progrAtt;
	}

	public void setProgrAtt(String progrAtt) {
		this.progrAtt = progrAtt;
	}

	public String getStatoMov() {
		return statoMov;
	}

	public void setStatoMov(String statoMov) {
		this.statoMov = statoMov;
	}

	public String getTipoServ() {
		return tipoServ;
	}

	public void setTipoServ(String tipoServ) {
		this.tipoServ = tipoServ;
	}

	public List<SollecitiVO> getListaSollecitiUte() {
		return listaSollecitiUte;
	}

	public void setListaSollecitiUte(List<SollecitiVO> listaSollecitiUte) {
		this.listaSollecitiUte = listaSollecitiUte;
	}

	public String getSolleciti() {
		return solleciti;
	}

	public void setSolleciti(String solleciti) {
		this.solleciti = solleciti;
	}

	public String getLivelloRicerca() {
		return livelloRicerca;
	}

	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}

	public String getCodBibInv() {
		return codBibInv;
	}

	public void setCodBibInv(String codBibInv) {
		this.codBibInv = codBibInv;
	}

	public String getCodSerieInv() {
		return codSerieInv;
	}

	public void setCodSerieInv(String codSerieInv) {
		this.codSerieInv = codSerieInv != null ? codSerieInv.toUpperCase() : null;
	}

	public String getCodInvenInv() {
		return codInvenInv;
	}

	public void setCodInvenInv(String codInvenInv) {
		this.codInvenInv = codInvenInv;
	}

	public String getDesXRicSeg() {
		return desXRicSeg;
	}

	public void setDesXRicSeg(String desXRicSeg) {
		this.desXRicSeg = desXRicSeg;
	}

	public int getListaMovRicUteSize() {
		return (listaMovRicUte == null ? 0 : listaMovRicUte.size());
	}

	public String getCodServNuovaRich() {
		return codServNuovaRich;
	}

	public void setCodServNuovaRich(String codServNuovaRich) {
		this.codServNuovaRich = codServNuovaRich;
	}

	public String getSegnaturaRicerca() {
		return segnaturaRicerca;
	}

	public void setSegnaturaRicerca(String segnaturaRicerca) {
		this.segnaturaRicerca = segnaturaRicerca;
	}

	public List<MovimentoListaVO> getListaRichiesteScadute() {
		return listaRichiesteScadute;
	}

	public void setListaRichiesteScadute(
			List<MovimentoListaVO> listaRichiesteScadute) {
		this.listaRichiesteScadute = listaRichiesteScadute;
	}

	public String getCodRichiestaScadutaSel() {
		return codRichiestaScadutaSel;
	}

	public void setCodRichiestaScadutaSel(String codRichiestaScadutaSel) {
		this.codRichiestaScadutaSel = codRichiestaScadutaSel;
	}

	public boolean isMostraSolleciti() {
		return mostraSolleciti;
	}

	public void setMostraSolleciti(boolean mostraSolleciti) {
		this.mostraSolleciti = mostraSolleciti;
	}

	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = codUtente;
	}

	public String getCodBibUtente() {
		return codBibUtente;
	}

	public void setCodBibUtente(String codBibUtente) {
		this.codBibUtente = codBibUtente;
	}

	public String getChiamante() {
		return chiamante;
	}

	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}

	public MovimentoVO getNuovaRichiesta() {
		return nuovaRichiesta;
	}

	public void setNuovaRichiesta(MovimentoVO nuovaRichiesta) {
		this.nuovaRichiesta = nuovaRichiesta;
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
	public String getSupporto() {
		return supporto;
	}
	public void setSupporto(String supporto) {
		this.supporto = supporto;
	}


}
