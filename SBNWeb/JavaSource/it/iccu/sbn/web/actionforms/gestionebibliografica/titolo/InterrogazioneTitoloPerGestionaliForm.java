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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloCartografiaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGraficaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloMusicaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class InterrogazioneTitoloPerGestionaliForm extends
		AbstractBibliotecaForm {


	private static final long serialVersionUID = 5991243708386885977L;
	private InterrogazioneTitoloGeneraleVO interrGener = new InterrogazioneTitoloGeneraleVO();
	private InterrogazioneTitoloCartografiaVO interrCartog = new InterrogazioneTitoloCartografiaVO();
	private InterrogazioneTitoloGraficaVO interrGrafic = new InterrogazioneTitoloGraficaVO();
	private InterrogazioneTitoloMusicaVO interrMusic = new InterrogazioneTitoloMusicaVO();
	AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();
	public String livRicerca;
	public String provenienza;

	public String tipoProspettazione;

	// Campi per l'utilizzo dell'interrogazione titolo filtrata dalle sintetiche
	// ESAMINA
	public String xidDiRicerca;
	public String xidDiRicercaDesc;
	public String tipoMatDiRic;
	public String naturaDiRic;
	public int oggettoDiRicerca;

	// Campi per l'utilizzo dell'interrogazione titolo dal folder di ordini
	private List listaBiblio;
	private String codTipoOrdine;
	private List listaTipoOrdine;
	private String annoOrdine;
	private String numeroOrdine;
	// Campi per l'utilizzo dell'interrogazione titolo dal folder di Inventari
	private String codBib;
	private String codBibOrd;
	private String descrBib;
	private String descrBibOrd;
	private List listaSerie;
	private boolean noSerie;
	private String codSerieDefault;
	private List listaComboSerie = new ArrayList();
	private String serieInventario;
	private int numeroInventario;

	public String getCodTipoOrdine() {
		return codTipoOrdine;
	}

	public void setCodTipoOrdine(String codTipoOrdine) {
		this.codTipoOrdine = codTipoOrdine;
	}

	public List getListaTipoOrdine() {
		return listaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		this.listaTipoOrdine = listaTipoOrdine;
	}

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

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		this.interrGener.setLibretto(false);
		this.interrGener.setLuogoPubblPunt(false);
		this.interrGener.setMatAntico(false);
		this.interrGener.setNomeCollegatoPunt(false);
		this.interrGener.setTitoloPunt(false);
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

	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public List getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}

	public List addListaBiblio(ComboCodDescVO elemBiblio) {
		listaBiblio.add(elemBiblio);
		return listaBiblio;
	}

	public String getAnnoOrdine() {
		return annoOrdine;
	}

	public void setAnnoOrdine(String annoOrdine) {
		this.annoOrdine = annoOrdine;
	}

	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
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

	public List getListaSerie() {
		return listaSerie;
	}

	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}

	public List getListaComboSerie() {
		return listaComboSerie;
	}

	public void setListaComboSerie(List listaComboSerie) {
		this.listaComboSerie = listaComboSerie;
	}

	public boolean isNoSerie() {
		return noSerie;
	}

	public void setNoSerie(boolean noSerie) {
		this.noSerie = noSerie;
	}

	public String getCodSerieDefault() {
		return codSerieDefault;
	}

	public void setCodSerieDefault(String codSerieDefault) {
		this.codSerieDefault = codSerieDefault;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public String getCodBibOrd() {
		return codBibOrd;
	}

	public void setCodBibOrd(String codBibOrd) {
		this.codBibOrd = codBibOrd;
	}

	public String getDescrBibOrd() {
		return descrBibOrd;
	}

	public void setDescrBibOrd(String descrBibOrd) {
		this.descrBibOrd = descrBibOrd;
	}

}
