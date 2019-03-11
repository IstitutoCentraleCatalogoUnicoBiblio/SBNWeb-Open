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
//	SBNWeb - Rifacimento ClientServer
//	FORM sintetica titoli
//	almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.web.actions.gestionebibliografica.utility.TabellaEsaminaVO;

import java.util.List;

public class SinteticaSchedeTitoliForm extends AbstractSinteticaTitoliForm {


	private static final long serialVersionUID = 7575079910068335121L;

	private Integer linkProgressivo;

	//	 Aree della sintetica utilizzate per interrogare in indice con l'area passata da interrogazione
	AreaDatiPassaggioInterrogazioneTitoloVO datiInterrTitolo = new AreaDatiPassaggioInterrogazioneTitoloVO();

	TabellaEsaminaVO tabellaEsaminaVO = new TabellaEsaminaVO();

	private String prospettaDatiOggColl;
	private String idOggColl;
	private String descOggColl;
	private String tipologiaTastiera;

	private String messaggisticaDiLavorazione;

	// Giuseppe - inizio
	private String elencoBibliotecheSelezionate;
	private List<BibliotecaVO> filtroBib;
	// Giuseppe - fine

	public String creaDoc = "SI";
	public String creaDocLoc = "SI";

	//	 Aree della sintetica utilizzate per la prospettazione dei titoli collegati (utilizzo come Sif)
	private AreePassaggioSifVO areePassSifVo = new AreePassaggioSifVO();
	private String utilizzoComeSif;

	// Aree della sintetica utilizzate per la prospettazione dei simili in variazione descrizione
	private String livRicerca;
	private String prospettazioneSimili;


	AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPassCiclicaVO = new AreaDatiPassaggioSchedaDocCiclicaVO();
	public String statoLavorRecordDesc;
	public String statoConfrontoDesc;

	// Aree della sintetica utilizzate per la prospettazione degli oggetti in creazione legame titolo
	private String prospettazionePerLegami;
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();

	// Aree della sintetica utilizzate per la prospettazione degli oggetti da chiamata dell'area SERVIZI
	private String prospettazionePerServizi;

	// Aree della sintetica utilizzate per la prospettazione degli oggetti da chiamata dell'area SERVIZI
	private String prospettazionePerMovimentiUtente;


	// Aree della sintetica utilizzate per la prospettazione dei simili in condividi notizia catalogata in locale
	// con il sitema centrale
	private String prospettaSimiliPerCondividi;

	// Aree della sintetica utilizzate per la prospettazione degli oggetti da chiamata dell'area SERVIZI
	private String ritornoDaEsterna;


	AreaDatiVariazioneTitoloVO areaDatiPassPerConfVariazione = new AreaDatiVariazioneTitoloVO();

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public String getProspettazionePerLegami() {
		return prospettazionePerLegami;
	}

	public void setProspettazionePerLegami(String prospettazionePerLegami) {
		this.prospettazionePerLegami = prospettazionePerLegami;
	}

	public AreaDatiVariazioneTitoloVO getAreaDatiPassPerConfVariazione() {
		return areaDatiPassPerConfVariazione;
	}

	public void setAreaDatiPassPerConfVariazione(
			AreaDatiVariazioneTitoloVO areaDatiPassPerConfVariazione) {
		this.areaDatiPassPerConfVariazione = areaDatiPassPerConfVariazione;
	}

	public String getProspettazioneSimili() {
		return prospettazioneSimili;
	}

	public void setProspettazioneSimili(String prospettazioneSimili) {
		this.prospettazioneSimili = prospettazioneSimili;
	}

	public String getLivRicerca() {
		return livRicerca;
	}

	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
	}

	public AreaDatiPassaggioInterrogazioneTitoloVO getDatiInterrTitolo() {
		return datiInterrTitolo;
	}

	public void setDatiInterrTitolo(
			AreaDatiPassaggioInterrogazioneTitoloVO datiInterrTitolo) {
		this.datiInterrTitolo = datiInterrTitolo;
	}

	public AreePassaggioSifVO getAreePassSifVo() {
		return areePassSifVo;
	}

	public void setAreePassSifVo(AreePassaggioSifVO areePassSifVo) {
		this.areePassSifVo = areePassSifVo;
	}

	public String getUtilizzoComeSif() {
		return utilizzoComeSif;
	}

	public void setUtilizzoComeSif(String utilizzoComeSif) {
		this.utilizzoComeSif = utilizzoComeSif;
	}

	public String getProspettaDatiOggColl() {
		return prospettaDatiOggColl;
	}

	public void setProspettaDatiOggColl(String prospettaDatiOggColl) {
		this.prospettaDatiOggColl = prospettaDatiOggColl;
	}

	public String getDescOggColl() {
		return descOggColl;
	}

	public void setDescOggColl(String descOggColl) {
		this.descOggColl = descOggColl;
	}

	public String getIdOggColl() {
		return idOggColl;
	}

	public void setIdOggColl(String idOggColl) {
		this.idOggColl = idOggColl;
	}

	public String getProspettazionePerServizi() {
		return prospettazionePerServizi;
	}

	public void setProspettazionePerServizi(String prospettazionePerServizi) {
		this.prospettazionePerServizi = prospettazionePerServizi;
	}

	public String getProspettaSimiliPerCondividi() {
		return prospettaSimiliPerCondividi;
	}

	public void setProspettaSimiliPerCondividi(String prospettaSimiliPerCondividi) {
		this.prospettaSimiliPerCondividi = prospettaSimiliPerCondividi;
	}

	public String getTipologiaTastiera() {
		return tipologiaTastiera;
	}

	public void setTipologiaTastiera(String tipologiaTastiera) {
		this.tipologiaTastiera = tipologiaTastiera;
	}

	public Integer getLinkProgressivo() {
		return linkProgressivo;
	}

	public void setLinkProgressivo(Integer linkProgressivo) {
		this.linkProgressivo = linkProgressivo;
	}

	public String getRitornoDaEsterna() {
		return ritornoDaEsterna;
	}

	public void setRitornoDaEsterna(String ritornoDaEsterna) {
		this.ritornoDaEsterna = ritornoDaEsterna;
	}

	public TabellaEsaminaVO getTabellaEsaminaVO() {
		return tabellaEsaminaVO;
	}

	public void setTabellaEsaminaVO(TabellaEsaminaVO tabellaEsaminaVO) {
		this.tabellaEsaminaVO = tabellaEsaminaVO;
	}

	public String getProspettazionePerMovimentiUtente() {
		return prospettazionePerMovimentiUtente;
	}

	public void setProspettazionePerMovimentiUtente(
			String prospettazionePerMovimentiUtente) {
		this.prospettazionePerMovimentiUtente = prospettazionePerMovimentiUtente;
	}

	public SinteticaSchedeTitoliForm() {
		super();
	}

	public String getCreaDoc() {
		return creaDoc;
	}

	public void setCreaDoc(String creaDoc) {
		this.creaDoc = creaDoc;
	}

	public String getCreaDocLoc() {
		return creaDocLoc;
	}

	public void setCreaDocLoc(String creaDocLoc) {
		this.creaDocLoc = creaDocLoc;
	}

	// Giuseppe - inizio
	public String getElencoBibliotecheSelezionate() {
		return elencoBibliotecheSelezionate;
	}

	public void setElencoBibliotecheSelezionate(String elencoBibliotecheSelezionate) {
		this.elencoBibliotecheSelezionate = elencoBibliotecheSelezionate;
	}
	// Giuseppe - fine

	public void setFiltroBib(List<BibliotecaVO> filtroBib) {
		this.filtroBib = filtroBib;
	}

	public List<BibliotecaVO> getFiltroBib() {
		return filtroBib;
	}

	public AreaDatiPassaggioSchedaDocCiclicaVO getAreaDatiPassCiclicaVO() {
		return areaDatiPassCiclicaVO;
	}

	public void setAreaDatiPassCiclicaVO(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPassCiclicaVO) {
		this.areaDatiPassCiclicaVO = areaDatiPassCiclicaVO;
	}

	public String getStatoLavorRecordDesc() {
		return statoLavorRecordDesc;
	}

	public void setStatoLavorRecordDesc(String statoLavorRecordDesc) {
		this.statoLavorRecordDesc = statoLavorRecordDesc;
	}

	public String getStatoConfrontoDesc() {
		return statoConfrontoDesc;
	}

	public void setStatoConfrontoDesc(String statoConfrontoDesc) {
		this.statoConfrontoDesc = statoConfrontoDesc;
	}

	public String getMessaggisticaDiLavorazione() {
		return messaggisticaDiLavorazione;
	}

	public void setMessaggisticaDiLavorazione(String messaggisticaDiLavorazione) {
		this.messaggisticaDiLavorazione = messaggisticaDiLavorazione;
	}

}
