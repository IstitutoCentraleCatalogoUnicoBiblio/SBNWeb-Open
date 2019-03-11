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

package it.iccu.sbn.ejb.vo.gestionebibliografica.autore;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteOriginarioVO;

import java.util.ArrayList;
import java.util.List;


public class DettaglioAutoreGeneraleVO extends SerializableVO {

	// = DettaglioAutoreGeneraleVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 2143535704021169760L;
	private String norme;
	private String agenzia;
	private String isadn;

	private String vid;
	private String livAut;
	private String forma;
	private String tipoNome;
	private String paese;

	private String nome;

	private String lingua;
	private String datazioni;

	private String notaInformativa;
	// potrebbe essere un duplicato del precedente VERIFICARE !!!!!!!!!!!!!!!!!!!!!!!!
	private String notaAlNome;
	private String fonti;

	private List<TabellaNumSTDImpronteOriginarioVO> listaRepertori;
	private List<TabellaNumSTDImpronteAggiornataVO> listaRepertoriNew;

	// VERIFICARE le impostazioni di questi campi con il campo fonte !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private String fonteAgenzia;
	private String fontePaese;
	private String notaCatalogatore;

//	 VERIFICARE come usa il campo RegoleDiCatal !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private String RegoleDiCatal;

	private String dataAgg;
	private String versione;
	private String dataIns;

	private String tipoLegame;

	// Dati eventuali per legami LEGAME_DOCUMENTO_AUTORE
	private String vidPadre;
	private String nominativoPadre;
	private String notaAlLegame;
	private String tipoLegameCastor;


	// Dati eventuali per legami LEGAME_DOCUMENTO_AUTORE
	private String  relatorCode;
	private String responsabilita;
	private boolean incerto;
	private boolean superfluo;

    // Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
	// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
	// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
	//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
    private String specStrumVoci;


	// Dati eventuali per legami LEGAME_AUTORE_MARCA
	private String dataVariazionePadre;

	// Dati eventuali per legami REPERTORI
	private String legamiRepertori;

	public DettaglioAutoreGeneraleVO() {
		super();
		this.listaRepertori = new ArrayList<TabellaNumSTDImpronteOriginarioVO>();
		this.listaRepertoriNew = new ArrayList<TabellaNumSTDImpronteAggiornataVO>();
	}


	public String getDataVariazionePadre() {
		return dataVariazionePadre;
	}
	public void setDataVariazionePadre(String dataVariazionePadre) {
		this.dataVariazionePadre = dataVariazionePadre;
	}
	public String getAgenzia() {
		return agenzia;
	}
	public void setAgenzia(String agenzia) {
		this.agenzia = agenzia;
	}
	public String getDataAgg() {
		return dataAgg;
	}
	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public String getDatazioni() {
		return datazioni;
	}
	public void setDatazioni(String datazioni) {
		this.datazioni = datazioni;
	}
	public String getFonti() {
		return fonti;
	}
	public void setFonti(String fonti) {
		this.fonti = fonti;
	}
	public String getForma() {
		return forma;
	}
	public void setForma(String forma) {
		this.forma = forma;
	}
	public String getIsadn() {
		return isadn;
	}
	public void setIsadn(String isadn) {
		this.isadn = isadn;
	}
	public String getLingua() {
		return lingua;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public String getLivAut() {
		return livAut;
	}
	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNorme() {
		return norme;
	}
	public void setNorme(String norme) {
		this.norme = norme;
	}
	public String getNotaCatalogatore() {
		return notaCatalogatore;
	}
	public void setNotaCatalogatore(String notaCatalogatore) {
		this.notaCatalogatore = notaCatalogatore;
	}
	public String getNotaInformativa() {
		return notaInformativa;
	}
	public void setNotaInformativa(String notaInformativa) {
		this.notaInformativa = notaInformativa;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public String getTipoNome() {
		return tipoNome;
	}
	public void setTipoNome(String tipoNome) {
		this.tipoNome = tipoNome;
	}
	public String getRelatorCode() {
		return relatorCode;
	}
	public void setRelatorCode(String relatorCode) {
		this.relatorCode = relatorCode;
	}
	public String getResponsabilita() {
		return responsabilita;
	}
	public void setResponsabilita(String responsabilita) {
		this.responsabilita = responsabilita;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public boolean isIncerto() {
		return incerto;
	}
	public void setIncerto(boolean incerto) {
		this.incerto = incerto;
	}
	public boolean isSuperfluo() {
		return superfluo;
	}
	public void setSuperfluo(boolean superfluo) {
		this.superfluo = superfluo;
	}
	public String getNominativoPadre() {
		return nominativoPadre;
	}
	public void setNominativoPadre(String nominativoPadre) {
		this.nominativoPadre = nominativoPadre;
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
	public String getVidPadre() {
		return vidPadre;
	}
	public void setVidPadre(String vidPadre) {
		this.vidPadre = vidPadre;
	}
	public String getLegamiRepertori() {
		return legamiRepertori;
	}
	public void setLegamiRepertori(String legamiRepertori) {
		this.legamiRepertori = legamiRepertori;
	}
	public String getTipoLegameCastor() {
		return tipoLegameCastor;
	}
	public void setTipoLegameCastor(String tipoLegameCastor) {
		this.tipoLegameCastor = tipoLegameCastor;
	}
	public String getNotaAlNome() {
		return notaAlNome;
	}
	public void setNotaAlNome(String notaAlNome) {
		this.notaAlNome = notaAlNome;
	}
	public String getFonteAgenzia() {
		return fonteAgenzia;
	}
	public void setFonteAgenzia(String fonteAgenzia) {
		this.fonteAgenzia = fonteAgenzia;
	}
	public String getFontePaese() {
		return fontePaese;
	}
	public void setFontePaese(String fontePaese) {
		this.fontePaese = fontePaese;
	}
	public String getRegoleDiCatal() {
		return RegoleDiCatal;
	}
	public void setRegoleDiCatal(String regoleDiCatal) {
		RegoleDiCatal = regoleDiCatal;
	}
	public String getVersione() {
		return versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
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


	public String getSpecStrumVoci() {
		return specStrumVoci;
	}


	public void setSpecStrumVoci(String specStrumVoci) {
		this.specStrumVoci = specStrumVoci;
	}
}
