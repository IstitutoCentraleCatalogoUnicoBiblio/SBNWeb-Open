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

import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.vo.SerializableVO;


public class AreaDatiVariazioneTitoloVO  extends SerializableVO {

	// = AreaDatiVariazioneTitoloVO.class.hashCode();


	private static final long serialVersionUID = 1067668543482461534L;

	private boolean flagCondiviso;

	private boolean modifica;
	private boolean conferma;
	private boolean primoBloccoSimili;
	private boolean inserimentoPolo;
	private boolean inserimentoIndice;

	private boolean variazioneNotaAntico;
	private boolean variazioneCompAntico;
	private boolean variazioneTuttiComp;

	private int profilo;
	private boolean abilPerTipoMat;
	private boolean AbilPerTipoMatMusicale;


	DettaglioTitoloParteFissaVO detTitoloPFissaVO = new DettaglioTitoloParteFissaVO();
	DettaglioTitoloCartografiaVO detTitoloCarVO = new DettaglioTitoloCartografiaVO();
	DettaglioTitoloGraficaVO detTitoloGraVO = new DettaglioTitoloGraficaVO();
	DettaglioTitoloMusicaVO detTitoloMusVO = new DettaglioTitoloMusicaVO();
	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	DettaglioTitoloAudiovisivoVO detTitoloAudVO = new DettaglioTitoloAudiovisivoVO();
	DettaglioTitoloElettronicoVO detTitoloEleVO = new DettaglioTitoloElettronicoVO();

	// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
	// viene esteso anche al Materiale Moderno e Antico
	DettaglioTitoloModAntVO detTitoloModAntVO = new DettaglioTitoloModAntVO();


	private String NaturaTitoloDaVariare;

	// Area dati per inserimento contestuale dei legami ad inferiore o
	// dei legami presenti in polo ad oggetti condivisi che vanno condivisi con l'Indice
	private boolean legameInf;
	private String bidArrivo;
	private String tipoLegame;
	private String noteLegame;
	private String sequenza;
	//	Inizio intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
	private String sici;
	//	Fine intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->

	private boolean legameDaCondividere;
	private LegamiType legamiTypeDaCondividere;

	private String timeStampPolo;

	private String bidTemporaneo;

	// evolutive ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata
	// viene estesa alla N nuova la localizzazione per possesso della madre
	private boolean propagaLocMadre;

	public String getBidArrivo() {
		return bidArrivo;
	}
	public void setBidArrivo(String bidArrivo) {
		this.bidArrivo = bidArrivo;
	}
	public boolean isLegameInf() {
		return legameInf;
	}
	public void setLegameInf(boolean legameInf) {
		this.legameInf = legameInf;
	}
	public String getNaturaTitoloDaVariare() {
		return NaturaTitoloDaVariare;
	}
	public void setNaturaTitoloDaVariare(String naturaTitoloDaVariare) {
		NaturaTitoloDaVariare = naturaTitoloDaVariare;
	}
	public DettaglioTitoloCartografiaVO getDetTitoloCarVO() {
		return detTitoloCarVO;
	}
	public void setDetTitoloCarVO(DettaglioTitoloCartografiaVO detTitoloCarVO) {
		this.detTitoloCarVO = detTitoloCarVO;
	}
	public DettaglioTitoloGraficaVO getDetTitoloGraVO() {
		return detTitoloGraVO;
	}
	public void setDetTitoloGraVO(DettaglioTitoloGraficaVO detTitoloGraVO) {
		this.detTitoloGraVO = detTitoloGraVO;
	}
	public DettaglioTitoloMusicaVO getDetTitoloMusVO() {
		return detTitoloMusVO;
	}
	public void setDetTitoloMusVO(DettaglioTitoloMusicaVO detTitoloMusVO) {
		this.detTitoloMusVO = detTitoloMusVO;
	}
	public DettaglioTitoloParteFissaVO getDetTitoloPFissaVO() {
		return detTitoloPFissaVO;
	}
	public void setDetTitoloPFissaVO(DettaglioTitoloParteFissaVO detTitoloPFissaVO) {
		this.detTitoloPFissaVO = detTitoloPFissaVO;
	}
	public boolean isModifica() {
		return modifica;
	}
	public void setModifica(boolean modifica) {
		this.modifica = modifica;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isInserimentoIndice() {
		return inserimentoIndice;
	}
	public void setInserimentoIndice(boolean inserimentoIndice) {
		this.inserimentoIndice = inserimentoIndice;
	}
	public boolean isInserimentoPolo() {
		return inserimentoPolo;
	}
	public void setInserimentoPolo(boolean inserimentoPolo) {
		this.inserimentoPolo = inserimentoPolo;
	}
	public boolean isPrimoBloccoSimili() {
		return primoBloccoSimili;
	}
	public void setPrimoBloccoSimili(boolean primoBloccoSimili) {
		this.primoBloccoSimili = primoBloccoSimili;
	}
	public boolean isVariazioneCompAntico() {
		return variazioneCompAntico;
	}
	public void setVariazioneCompAntico(boolean variazioneCompAntico) {
		this.variazioneCompAntico = variazioneCompAntico;
	}
	public boolean isVariazioneNotaAntico() {
		return variazioneNotaAntico;
	}
	public void setVariazioneNotaAntico(boolean variazioneNotaAntico) {
		this.variazioneNotaAntico = variazioneNotaAntico;
	}
	public boolean isVariazioneTuttiComp() {
		return variazioneTuttiComp;
	}
	public void setVariazioneTuttiComp(boolean variazioneTuttiComp) {
		this.variazioneTuttiComp = variazioneTuttiComp;
	}
	public String getNoteLegame() {
		return noteLegame;
	}
	public void setNoteLegame(String noteLegame) {
		this.noteLegame = noteLegame;
	}
	public String getSequenza() {
		return sequenza;
	}
	public void setSequenza(String sequenza) {
		this.sequenza = sequenza;
	}
	public String getTipoLegame() {
		return tipoLegame;
	}
	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}
	public int getProfilo() {
		return profilo;
	}
	public void setProfilo(int profilo) {
		this.profilo = profilo;
	}
	public boolean isAbilPerTipoMat() {
		return abilPerTipoMat;
	}
	public void setAbilPerTipoMat(boolean abilPerTipoMat) {
		this.abilPerTipoMat = abilPerTipoMat;
	}
	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}
	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public String getNaturaProfilazione()
	{
		String natura;
		if (this.isModifica()) {
			natura = this.getNaturaTitoloDaVariare();
		} else {
			natura = this.getDetTitoloPFissaVO().getNatura();
		}
		return natura;
	}
	public String getTimeStampPolo() {
		return timeStampPolo;
	}
	public void setTimeStampPolo(String timeStampPolo) {
		this.timeStampPolo = timeStampPolo;
	}
	public String getBidTemporaneo() {
		return bidTemporaneo;
	}
	public void setBidTemporaneo(String bidTemporaneo) {
		this.bidTemporaneo = bidTemporaneo;
	}
	public boolean isLegameDaCondividere() {
		return legameDaCondividere;
	}
	public void setLegameDaCondividere(boolean legameDaCondividere) {
		this.legameDaCondividere = legameDaCondividere;
	}
	public LegamiType getLegamiTypeDaCondividere() {
		return legamiTypeDaCondividere;
	}
	public void setLegamiTypeDaCondividere(LegamiType legamiTypeDaCondividere) {
		this.legamiTypeDaCondividere = legamiTypeDaCondividere;
	}
	public String getSici() {
		return sici;
	}
	public void setSici(String sici) {
		this.sici = sici;
	}
	public DettaglioTitoloAudiovisivoVO getDetTitoloAudVO() {
		return detTitoloAudVO;
	}
	public void setDetTitoloAudVO(DettaglioTitoloAudiovisivoVO detTitoloAudVO) {
		this.detTitoloAudVO = detTitoloAudVO;
	}
	public DettaglioTitoloElettronicoVO getDetTitoloEleVO() {
		return detTitoloEleVO;
	}
	public void setDetTitoloEleVO(DettaglioTitoloElettronicoVO detTitoloEleVO) {
		this.detTitoloEleVO = detTitoloEleVO;
	}
	public boolean isAbilPerTipoMatMusicale() {
		return AbilPerTipoMatMusicale;
	}
	public void setAbilPerTipoMatMusicale(boolean abilPerTipoMatMusicale) {
		AbilPerTipoMatMusicale = abilPerTipoMatMusicale;
	}
	public boolean isPropagaLocMadre() {
		return propagaLocMadre;
	}
	public void setPropagaLocMadre(boolean propagaLocMadre) {
		this.propagaLocMadre = propagaLocMadre;
	}
	public DettaglioTitoloModAntVO getDetTitoloModAntVO() {
		return detTitoloModAntVO;
	}
	public void setDetTitoloModAntVO(DettaglioTitoloModAntVO detTitoloModAntVO) {
		this.detTitoloModAntVO = detTitoloModAntVO;
	}


}
