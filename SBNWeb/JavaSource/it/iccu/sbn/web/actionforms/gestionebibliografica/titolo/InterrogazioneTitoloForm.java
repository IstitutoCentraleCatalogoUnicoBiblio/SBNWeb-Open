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
//		FORM
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloAudiovisivoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloCartografiaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloElettronicoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGraficaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloMusicaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

public class InterrogazioneTitoloForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -779718654534989295L;
	private InterrogazioneTitoloGeneraleVO interrGener = new InterrogazioneTitoloGeneraleVO();
	private InterrogazioneTitoloCartografiaVO interrCartog = new InterrogazioneTitoloCartografiaVO();
	private InterrogazioneTitoloGraficaVO interrGrafic = new InterrogazioneTitoloGraficaVO();
	private InterrogazioneTitoloMusicaVO interrMusic = new InterrogazioneTitoloMusicaVO();
	private InterrogazioneTitoloAudiovisivoVO interrAudiovisivo = new InterrogazioneTitoloAudiovisivoVO();
	private InterrogazioneTitoloElettronicoVO interrElettronico = new InterrogazioneTitoloElettronicoVO();

	public String livRicerca;
	public String provenienza;

	public String creaDoc = "SI";
	public String creaDocLoc = "SI";

	public String ritornoDaEsterna;
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO	= new AreaDatiLegameTitoloVO();


	// Campi per l'utilizzo dell'interrogazione titolo filtrata dalle sintetiche ESAMINA
	public String xidDiRicerca;
	public String xidDiRicercaDesc;
	public String tipoMatDiRic;
	public String naturaDiRic;
	public int oggettoDiRicerca;

	public String presenzaRicLocale = "";

	//	 Inizio Gestione oggetti per carcamento bid per catalogazione ciclica in Indice
	public String presenzaLoadFile = "NO";
	private FormFile uploadImmagine;
    private List listaBidDaFile;

//	 Intervento interno 26.11.2009 - almaviva2 - per gestire il filtre sulle Biblioteche a seguito di intervento almaviva5_20091007 Inserimento filtro per Biblioteca
    public String elencoBiblioMetropolitane = "";
	private List<BibliotecaVO> filtroLocBib = new ArrayList<BibliotecaVO>();

	private boolean initialized;

	// Dicembre 2015 almaviva2 : in fase di creazione di uno spoglio sotto una M si porta tutto il dettaglio
	// del titolo madre così da preimpostare tutti i campi della N che salvo alcune rare eccezioni saranno gli stessi
	private DettaglioTitoloCompletoVO dettTitComMadreVO;



	public int getOggettoDiRicerca() {
		return oggettoDiRicerca;
	}

	public void setOggettoDiRicerca(int oggettoDiRicerca) {
		this.oggettoDiRicerca = oggettoDiRicerca;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public AreaDatiLegameTitoloVO getAreaDatiLegameTitoloVO() {
		return areaDatiLegameTitoloVO;
	}

	public void setAreaDatiLegameTitoloVO(
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO) {
		this.areaDatiLegameTitoloVO = areaDatiLegameTitoloVO;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public InterrogazioneTitoloCartografiaVO getInterrCartog() {
		return interrCartog;
	}

	public void setInterrCartog(InterrogazioneTitoloCartografiaVO interrCartog) {
		this.interrCartog = interrCartog;
	}

	public InterrogazioneTitoloGraficaVO getInterrGrafic() {
		return interrGrafic;
	}

	public void setInterrGrafic(InterrogazioneTitoloGraficaVO interrGrafic) {
		this.interrGrafic = interrGrafic;
	}

	public InterrogazioneTitoloMusicaVO getInterrMusic() {
		return interrMusic;
	}

	public void setInterrMusic(InterrogazioneTitoloMusicaVO interrMusic) {
		this.interrMusic = interrMusic;
	}

	public InterrogazioneTitoloGeneraleVO getInterrGener() {
		return interrGener;
	}

	public void setInterrGener(InterrogazioneTitoloGeneraleVO interrGener) {
		this.interrGener = interrGener;
	}

	public String getLivRicerca() {
		return livRicerca;
	}

	public void setLivRicerca(String livRicerca) {
		this.livRicerca = livRicerca;
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

	public String getNaturaDiRic() {
		return naturaDiRic;
	}

	public void setNaturaDiRic(String naturaDiRic) {
		this.naturaDiRic = naturaDiRic;
	}

	public String getTipoMatDiRic() {
		return tipoMatDiRic;
	}

	public void setTipoMatDiRic(String tipoMatDiRic) {
		this.tipoMatDiRic = tipoMatDiRic;
	}

	public String getRitornoDaEsterna() {
		return ritornoDaEsterna;
	}

	public void setRitornoDaEsterna(String ritornoDaEsterna) {
		this.ritornoDaEsterna = ritornoDaEsterna;
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

	public List getListaBidDaFile() {
		return listaBidDaFile;
	}

	public void setListaBidDaFile(List listaBidDaFile) {
		this.listaBidDaFile = listaBidDaFile;
	}

	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}

	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}

	public String getPresenzaRicLocale() {
		return presenzaRicLocale;
	}

	public void setPresenzaRicLocale(String presenzaRicLocale) {
		this.presenzaRicLocale = presenzaRicLocale;
	}

	public String getPresenzaLoadFile() {
		return presenzaLoadFile;
	}

	public void setPresenzaLoadFile(String presenzaLoadFile) {
		this.presenzaLoadFile = presenzaLoadFile;
	}

	public String getElencoBiblioMetropolitane() {
		return elencoBiblioMetropolitane;
	}

	public void setElencoBiblioMetropolitane(String elencoBiblioMetropolitane) {
		this.elencoBiblioMetropolitane = elencoBiblioMetropolitane;
	}
	public List<BibliotecaVO> getFiltroLocBib() {
		return filtroLocBib;
	}
	public void setFiltroLocBib(List<BibliotecaVO> filtroLocBib) {
		this.filtroLocBib = filtroLocBib;
	}

	public InterrogazioneTitoloAudiovisivoVO getInterrAudiovisivo() {
		return interrAudiovisivo;
	}

	public void setInterrAudiovisivo(
			InterrogazioneTitoloAudiovisivoVO interrAudiovisivo) {
		this.interrAudiovisivo = interrAudiovisivo;
	}

	public InterrogazioneTitoloElettronicoVO getInterrElettronico() {
		return interrElettronico;
	}

	public void setInterrElettronico(
			InterrogazioneTitoloElettronicoVO interrElettronico) {
		this.interrElettronico = interrElettronico;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public DettaglioTitoloCompletoVO getDettTitComMadreVO() {
		return dettTitComMadreVO;
	}

	public void setDettTitComMadreVO(DettaglioTitoloCompletoVO dettTitComVO) {
		this.dettTitComMadreVO = dettTitComVO;
	}


}
