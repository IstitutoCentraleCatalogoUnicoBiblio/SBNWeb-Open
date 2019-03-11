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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;

import java.util.ArrayList;
import java.util.List;

public class AreaDatiPassaggioInterrogazioneTitoloVO extends SerializableVO {

	private static final long serialVersionUID = -6189417014954380558L;

	// Campi per memorizzare il tipo di sintetica per utilizzo come SIF
	private int elemXBlocchi;
	private String formatoListaSelez;
	private String tipoOrdinamSelez;

	private int oggChiamante;
	private int tipoOggetto;
	private int tipoOggettoFiltrato;
	private String oggDiRicerca;
	private boolean ricercaPolo;
	private boolean ricercaIndice;
	private boolean controlliFormali;

	// Intervento interno 26.11.2009 - almaviva2 - per gestire il filtre
	// sulle Biblioteche a seguito di intervento almaviva5_20091007 Inserimento filtro
	// per Biblioteca
	private List<BibliotecaVO> filtroLocBib = new ArrayList<BibliotecaVO>();

	// Campi per la ricerca dei titoli collegati ad oggetti
	private String naturaTitBase;
	private String tipMatTitBase;
	private String codiceLegame;
	private String codiceSici;

	// Campi per la ricerca dei titoli filtrati da un precedente esamina da
	// autori/marche
	public String xidDiRicerca;
	public String xidDiRicercaDesc;
	public String tipoAut;

	// Campi per la ricerca dei titoli musicali per
	// localizzazione/Fondo/Segnatura
	public String RicercaPerGest;
	public String tipoProspettazioneGestionali;
	private String codPolo;
	private String codBiblio;
	private String serieInventario;
	private int numeroInventario;
	private String codTipoOrdine;
	private String annoOrdine;
	private String numeroOrdine;

	// ========================================================================================

	InterrogazioneTitoloGeneraleVO interTitGen = new InterrogazioneTitoloGeneraleVO();
	InterrogazioneTitoloMusicaVO interTitMus = new InterrogazioneTitoloMusicaVO();
	InterrogazioneTitoloGraficaVO interTitGra = new InterrogazioneTitoloGraficaVO();
	InterrogazioneTitoloCartografiaVO interTitCar = new InterrogazioneTitoloCartografiaVO();

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale
	// Audiovisivo/Discosonoro
	InterrogazioneTitoloAudiovisivoVO interTitAud = new InterrogazioneTitoloAudiovisivoVO();
	InterrogazioneTitoloElettronicoVO interTitEle = new InterrogazioneTitoloElettronicoVO();

	public InterrogazioneTitoloCartografiaVO getInterTitCar() {
		return interTitCar;
	}

	public void setInterTitCar(InterrogazioneTitoloCartografiaVO interTitCar) {
		this.interTitCar = interTitCar;
	}

	public InterrogazioneTitoloGeneraleVO getInterTitGen() {
		return interTitGen;
	}

	public void setInterTitGen(InterrogazioneTitoloGeneraleVO interTitGen) {
		this.interTitGen = interTitGen;
	}

	public InterrogazioneTitoloGraficaVO getInterTitGra() {
		return interTitGra;
	}

	public void setInterTitGra(InterrogazioneTitoloGraficaVO interTitGra) {
		this.interTitGra = interTitGra;
	}

	public InterrogazioneTitoloMusicaVO getInterTitMus() {
		return interTitMus;
	}

	public void setInterTitMus(InterrogazioneTitoloMusicaVO interTitMus) {
		this.interTitMus = interTitMus;
	}

	public int getOggChiamante() {
		return oggChiamante;
	}

	public void setOggChiamante(int oggChiamante) {
		this.oggChiamante = oggChiamante;
	}

	public String getOggDiRicerca() {
		return oggDiRicerca;
	}

	public void setOggDiRicerca(String oggDiRicerca) {
		this.oggDiRicerca = oggDiRicerca;
	}

	public int getTipoOggetto() {
		return tipoOggetto;
	}

	public void setTipoOggetto(int tipoOggetto) {
		this.tipoOggetto = tipoOggetto;
	}

	public boolean isRicercaIndice() {
		return ricercaIndice;
	}

	public void setRicercaIndice(boolean ricercaIndice) {
		this.ricercaIndice = ricercaIndice;
	}

	public boolean isRicercaPolo() {
		return ricercaPolo;
	}

	public void setRicercaPolo(boolean ricercaPolo) {
		this.ricercaPolo = ricercaPolo;
	}

	public String getNaturaTitBase() {
		return naturaTitBase;
	}

	public void setNaturaTitBase(String naturaTitBase) {
		this.naturaTitBase = naturaTitBase;
	}

	public String getTipMatTitBase() {
		return tipMatTitBase;
	}

	public void setTipMatTitBase(String tipMatTitBase) {
		this.tipMatTitBase = tipMatTitBase;
	}

	public String getCodiceLegame() {
		return codiceLegame;
	}

	public void setCodiceLegame(String codiceLegame) {
		this.codiceLegame = codiceLegame;
	}

	public String getCodiceSici() {
		return codiceSici;
	}

	public void setCodiceSici(String codiceSici) {
		this.codiceSici = codiceSici;
	}

	public String getTipoAut() {
		return tipoAut;
	}

	public void setTipoAut(String tipoAut) {
		this.tipoAut = tipoAut;
	}

	public String getXidDiRicerca() {
		return xidDiRicerca;
	}

	public void setXidDiRicerca(String xidDiRicerca) {
		this.xidDiRicerca = xidDiRicerca;
	}

	public String getXidDiRicercaDesc() {
		return xidDiRicercaDesc;
	}

	public void setXidDiRicercaDesc(String xidDiRicercaDesc) {
		this.xidDiRicercaDesc = xidDiRicercaDesc;
	}

	public boolean isControlliFormali() {
		return controlliFormali;
	}

	public void setControlliFormali(boolean controlliFormali) {
		this.controlliFormali = controlliFormali;
	}

	public int getTipoOggettoFiltrato() {
		return tipoOggettoFiltrato;
	}

	public void setTipoOggettoFiltrato(int tipoOggettoFiltrato) {
		this.tipoOggettoFiltrato = tipoOggettoFiltrato;
	}

	public String getRicercaPerGest() {
		return RicercaPerGest;
	}

	public void setRicercaPerGest(String ricercaPerGest) {
		RicercaPerGest = ricercaPerGest;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getFormatoListaSelez() {
		return formatoListaSelez;
	}

	public void setFormatoListaSelez(String formatoListaSelez) {
		this.formatoListaSelez = formatoListaSelez;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public String getTipoProspettazioneGestionali() {
		return tipoProspettazioneGestionali;
	}

	public void setTipoProspettazioneGestionali(
			String tipoProspettazioneGestionali) {
		this.tipoProspettazioneGestionali = tipoProspettazioneGestionali;
	}

	public String getCodBiblio() {
		return codBiblio;
	}

	public void setCodBiblio(String codBiblio) {
		this.codBiblio = codBiblio;
	}

	public int getNumeroInventario() {
		return numeroInventario;
	}

	public void setNumeroInventario(int numeroInventario) {
		this.numeroInventario = numeroInventario;
	}

	public String getSerieInventario() {
		return serieInventario;
	}

	public void setSerieInventario(String serieInventario) {
		this.serieInventario = serieInventario;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getAnnoOrdine() {
		return annoOrdine;
	}

	public void setAnnoOrdine(String annoOrdine) {
		this.annoOrdine = annoOrdine;
	}

	public String getCodTipoOrdine() {
		return codTipoOrdine;
	}

	public void setCodTipoOrdine(String codTipoOrdine) {
		this.codTipoOrdine = codTipoOrdine;
	}

	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	public List<BibliotecaVO> getFiltroLocBib() {
		return filtroLocBib;
	}

	public void setFiltroLocBib(List<BibliotecaVO> filtroLocBib) {
		this.filtroLocBib = filtroLocBib;
	}

	public InterrogazioneTitoloAudiovisivoVO getInterTitAud() {
		return interTitAud;
	}

	public void setInterTitAud(InterrogazioneTitoloAudiovisivoVO interTitAud) {
		this.interTitAud = interTitAud;
	}

	public InterrogazioneTitoloElettronicoVO getInterTitEle() {
		return interTitEle;
	}

	public void setInterTitEle(InterrogazioneTitoloElettronicoVO interTitEle) {
		this.interTitEle = interTitEle;
	}

	public void clear() {
		// almaviva2 Evolutiva Novembre 2014 per Gestione Area0
		this.setInterTitGen(null);
		this.setInterTitCar(null);
		this.setInterTitGra(null);
		this.setInterTitMus(null);
		this.setInterTitAud(null);
		this.setInterTitEle(null);
	}

}
