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
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.gestionebibliografica.altre;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteOriginarioVO;

import java.util.ArrayList;
import java.util.List;


public class DettaglioLuogoGeneraleVO extends SerializableVO {

	// = DettaglioLuogoGeneraleVO.class.hashCode();

// Dati relativi alla ricerca marca - Canali principali

	/**
	 *
	 */
	private static final long serialVersionUID = 8038776968452672396L;
	private String lid;
	private String denomLuogo;
	private String livAut;
	private String forma;
	private String paese;

	// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
	// nota informativa , nota catalogatore e legame a repertori
	private String notaInformativa;
	private String notaCatalogatore;
	private String legamiRepertori;
	private List<TabellaNumSTDImpronteOriginarioVO> listaRepertori;
	private List<TabellaNumSTDImpronteAggiornataVO> listaRepertoriNew;

	private String versione;

	// Dati eventuali per legami
	private String idPadre;
	private String descrizionePadre;
	private String notaAlLegame;
	private String tipoLegameValore;

	private String tipoLegame;

	private String relatorCode;

	public DettaglioLuogoGeneraleVO() {
		super();
		this.listaRepertori = new ArrayList<TabellaNumSTDImpronteOriginarioVO>();
		this.listaRepertoriNew = new ArrayList<TabellaNumSTDImpronteAggiornataVO>();
	}


	public String getDescrizionePadre() {
		return descrizionePadre;
	}
	public void setDescrizionePadre(String descrizionePadre) {
		this.descrizionePadre = descrizionePadre;
	}
	public String getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}
	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public String getNotaAlLegame() {
		return notaAlLegame;
	}
	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}
	public String getTipoLegame() {
		return tipoLegame;
	}
	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}
	public String getDenomLuogo() {
		return denomLuogo;
	}
	public void setDenomLuogo(String denomLuogo) {
		this.denomLuogo = denomLuogo;
	}
	public String getTipoLegameValore() {
		return tipoLegameValore;
	}
	public void setTipoLegameValore(String tipoLegameValore) {
		this.tipoLegameValore = tipoLegameValore;
	}
	public String getRelatorCode() {
		return relatorCode;
	}
	public void setRelatorCode(String relatorCode) {
		this.relatorCode = relatorCode;
	}
	public String getLivAut() {
		return livAut;
	}
	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}
	public String getVersione() {
		return versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
	}
	public String getForma() {
		return forma;
	}
	public void setForma(String forma) {
		this.forma = forma;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public String getNotaInformativa() {
		return notaInformativa;
	}
	public void setNotaInformativa(String notaInformativa) {
		this.notaInformativa = notaInformativa;
	}
	public String getNotaCatalogatore() {
		return notaCatalogatore;
	}
	public void setNotaCatalogatore(String notaCatalogatore) {
		this.notaCatalogatore = notaCatalogatore;
	}

	public String getLegamiRepertori() {
		return legamiRepertori;
	}
	public void setLegamiRepertori(String legamiRepertori) {
		this.legamiRepertori = legamiRepertori;
	}

	public List<TabellaNumSTDImpronteOriginarioVO> getListaRepertori() {
		return listaRepertori;
	}
	public void setListaRepertori(List<TabellaNumSTDImpronteOriginarioVO> listaRepertori) {
		this.listaRepertori = listaRepertori;
	}

	public List addListaRepertori(TabellaNumSTDImpronteOriginarioVO tabRep) {
		listaRepertori.add(tabRep);
		return listaRepertori;
	}


	public List<TabellaNumSTDImpronteAggiornataVO> getListaRepertoriNew() {
		return listaRepertoriNew;
	}


	public void setListaRepertoriNew(
			List<TabellaNumSTDImpronteAggiornataVO> listaRepertoriNew) {
		this.listaRepertoriNew = listaRepertoriNew;
	}
	public List addListaRepertoriNew(TabellaNumSTDImpronteAggiornataVO tabRep) {
		listaRepertoriNew.add(tabRep);
		return listaRepertoriNew;
	}

}
