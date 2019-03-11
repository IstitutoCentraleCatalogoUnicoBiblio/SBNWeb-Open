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


public class AreaDatiLegameTitoloVO   extends SerializableVO {

	// = AreaDatiLegameTitoloVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -6951057819251413113L;
	private String authorityOggettoPartenza;
	private String bidPartenza;
	private String descPartenza;

	private String descPartenzaMsuperiore;

	private String tipMatBidPartenza;
	private String livAutBidPartenza;
	private String naturaBidPartenza;
	private String timeStampBidPartenza;
	private boolean flagCondivisoPartenza;

	private String improntaPartenza;
	private String tipNumStandardPartenza;
	private String valNumStandardPartenza;
	private String tipoDataPubblPartenza;
	private String data1DaPubblPartenza;

	private String tipoOperazione;

	private String authorityOggettoArrivo;
	private String idArrivo;
	private String descArrivo;
	private String naturaBidArrivo;
	private String tipoNomeArrivo;
	private String livAutIdArrivo;
	private String formaIdArrivo;
	private String notaInformativaIdArrivo;
	private String timeStampBidArrivo;
	private boolean flagCondivisoArrivo;
	private boolean classeDewey;

	// Viene utilizzato per l'operazione di sposta legame (cambia solo il bid e rimangono uguali tutte le altre caratteristiche)
	private String idArrivoNew;

	private String tipoLegameNew;
	private boolean flagCondivisoLegame;
	private String sottoTipoLegameNew;
	private String noteLegameNew;
	private boolean superfluoNew;
	private boolean incertoNew;
	private String relatorCodeNew;
	private String tipoResponsNew;
	private String specStrumVociNew;
	private String siciNew;
	private String sequenzaNew;
	private String sequenzaMusicaNew;

	private String tipoLegameOld;
	private String noteLegameOld;
	private boolean superfluoOld;
	private boolean incertoOld;
	private String relatorCodeOld;
	private String tipoResponsOld;
	private String specStrumVociOld;

	private boolean inserimentoPolo;
	private boolean inserimentoIndice;

	private String timeStampBidPartenzaPolo;

	private String bidRientroAnalitica;



	// Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: trascinamento legami autore ma M a N (esempio disco M con tracce N)
	// area utilizzata pre inviare i bid delle N su cui trascinare i legami
	private String[] inferioriDaCatturare;

	public String getTimeStampBidPartenzaPolo() {
		return timeStampBidPartenzaPolo;
	}
	public void setTimeStampBidPartenzaPolo(String timeStampBidPartenzaPolo) {
		this.timeStampBidPartenzaPolo = timeStampBidPartenzaPolo;
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
	public String getNaturaBidPartenza() {
		return naturaBidPartenza;
	}
	public void setNaturaBidPartenza(String naturaBidPartenza) {
		this.naturaBidPartenza = naturaBidPartenza;
	}
	public void setInserimentoPolo(boolean inserimentoPolo) {
		this.inserimentoPolo = inserimentoPolo;
	}
	public String getLivAutBidPartenza() {
		return livAutBidPartenza;
	}
	public void setLivAutBidPartenza(String livAutBidPartenza) {
		this.livAutBidPartenza = livAutBidPartenza;
	}
	public String getTipMatBidPartenza() {
		return tipMatBidPartenza;
	}
	public void setTipMatBidPartenza(String tipMatBidPartenza) {
		this.tipMatBidPartenza = tipMatBidPartenza;
	}
	public String getBidPartenza() {
		return bidPartenza;
	}
	public void setBidPartenza(String bidPartenza) {
		this.bidPartenza = bidPartenza;
	}
	public String getTimeStampBidPartenza() {
		return timeStampBidPartenza;
	}
	public void setTimeStampBidPartenza(String timeStampBidPartenza) {
		this.timeStampBidPartenza = timeStampBidPartenza;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public String getAuthorityOggettoArrivo() {
		return authorityOggettoArrivo;
	}
	public void setAuthorityOggettoArrivo(String authorityOggettoArrivo) {
		this.authorityOggettoArrivo = authorityOggettoArrivo;
	}
	public String getIdArrivo() {
		return idArrivo;
	}
	public void setIdArrivo(String idArrivo) {
		this.idArrivo = idArrivo;
	}
	public boolean isIncertoNew() {
		return incertoNew;
	}
	public void setIncertoNew(boolean incertoNew) {
		this.incertoNew = incertoNew;
	}
	public boolean isIncertoOld() {
		return incertoOld;
	}
	public void setIncertoOld(boolean incertoOld) {
		this.incertoOld = incertoOld;
	}
	public String getNoteLegameNew() {
		return noteLegameNew;
	}
	public void setNoteLegameNew(String noteLegameNew) {
		this.noteLegameNew = noteLegameNew;
	}
	public String getNoteLegameOld() {
		return noteLegameOld;
	}
	public void setNoteLegameOld(String noteLegameOld) {
		this.noteLegameOld = noteLegameOld;
	}
	public String getRelatorCodeNew() {
		return relatorCodeNew;
	}
	public void setRelatorCodeNew(String relatorCodeNew) {
		this.relatorCodeNew = relatorCodeNew;
	}
	public String getRelatorCodeOld() {
		return relatorCodeOld;
	}
	public void setRelatorCodeOld(String relatorCodeOld) {
		this.relatorCodeOld = relatorCodeOld;
	}
	public boolean isSuperfluoNew() {
		return superfluoNew;
	}
	public void setSuperfluoNew(boolean superfluoNew) {
		this.superfluoNew = superfluoNew;
	}
	public boolean isSuperfluoOld() {
		return superfluoOld;
	}
	public void setSuperfluoOld(boolean superfluoOld) {
		this.superfluoOld = superfluoOld;
	}
	public String getTipoLegameNew() {
		return tipoLegameNew;
	}
	public void setTipoLegameNew(String tipoLegameNew) {
		this.tipoLegameNew = tipoLegameNew;
	}
	public String getTipoLegameOld() {
		return tipoLegameOld;
	}
	public void setTipoLegameOld(String tipoLegameOld) {
		this.tipoLegameOld = tipoLegameOld;
	}
	public String getTipoResponsNew() {
		return tipoResponsNew;
	}
	public void setTipoResponsNew(String tipoResponsNew) {
		this.tipoResponsNew = tipoResponsNew;
	}
	public String getTipoResponsOld() {
		return tipoResponsOld;
	}
	public void setTipoResponsOld(String tipoResponsOld) {
		this.tipoResponsOld = tipoResponsOld;
	}
	public String getNaturaBidArrivo() {
		return naturaBidArrivo;
	}
	public void setNaturaBidArrivo(String naturaBidArrivo) {
		this.naturaBidArrivo = naturaBidArrivo;
	}
	public String getSiciNew() {
		return siciNew;
	}
	public void setSiciNew(String siciNew) {
		this.siciNew = siciNew;
	}
	public String getSequenzaNew() {
		return sequenzaNew;
	}
	public void setSequenzaNew(String sequenzaNew) {
		this.sequenzaNew = sequenzaNew;
	}
	public String getSequenzaMusicaNew() {
		return sequenzaMusicaNew;
	}
	public void setSequenzaMusicaNew(String sequenzaMusicaNew) {
		this.sequenzaMusicaNew = sequenzaMusicaNew;
	}
	public String getSottoTipoLegameNew() {
		return sottoTipoLegameNew;
	}
	public void setSottoTipoLegameNew(String sottoTipoLegameNew) {
		this.sottoTipoLegameNew = sottoTipoLegameNew;
	}
	public String getDescArrivo() {
		return descArrivo;
	}
	public void setDescArrivo(String descArrivo) {
		this.descArrivo = descArrivo;
	}
	public String getDescPartenza() {
		return descPartenza;
	}
	public void setDescPartenza(String descPartenza) {
		this.descPartenza = descPartenza;
	}
	public String getTipoNomeArrivo() {
		return tipoNomeArrivo;
	}
	public void setTipoNomeArrivo(String tipoNomeArrivo) {
		this.tipoNomeArrivo = tipoNomeArrivo;
	}
	public boolean isClasseDewey() {
		return classeDewey;
	}
	public void setClasseDewey(boolean classeDewey) {
		this.classeDewey = classeDewey;
	}
	public String getAuthorityOggettoPartenza() {
		return authorityOggettoPartenza;
	}
	public void setAuthorityOggettoPartenza(String authorityOggettoPartenza) {
		this.authorityOggettoPartenza = authorityOggettoPartenza;
	}
	public String getFormaIdArrivo() {
		return formaIdArrivo;
	}
	public void setFormaIdArrivo(String formaIdArrivo) {
		this.formaIdArrivo = formaIdArrivo;
	}
	public String getLivAutIdArrivo() {
		return livAutIdArrivo;
	}
	public void setLivAutIdArrivo(String livAutIdArrivo) {
		this.livAutIdArrivo = livAutIdArrivo;
	}
	public String getNotaInformativaIdArrivo() {
		return notaInformativaIdArrivo;
	}
	public void setNotaInformativaIdArrivo(String notaInformativaIdArrivo) {
		this.notaInformativaIdArrivo = notaInformativaIdArrivo;
	}
	public boolean isFlagCondivisoArrivo() {
		return flagCondivisoArrivo;
	}
	public void setFlagCondivisoArrivo(boolean flagCondivisoArrivo) {
		this.flagCondivisoArrivo = flagCondivisoArrivo;
	}
	public boolean isFlagCondivisoLegame() {
		return flagCondivisoLegame;
	}
	public void setFlagCondivisoLegame(boolean flagCondivisoLegame) {
		this.flagCondivisoLegame = flagCondivisoLegame;
	}
	public boolean isFlagCondivisoPartenza() {
		return flagCondivisoPartenza;
	}
	public void setFlagCondivisoPartenza(boolean flagCondivisoPartenza) {
		this.flagCondivisoPartenza = flagCondivisoPartenza;
	}
	public String getBidRientroAnalitica() {
		return bidRientroAnalitica;
	}
	public void setBidRientroAnalitica(String bidRientroAnalitica) {
		this.bidRientroAnalitica = bidRientroAnalitica;
	}
	public String getTimeStampBidArrivo() {
		return timeStampBidArrivo;
	}
	public void setTimeStampBidArrivo(String timeStampBidArrivo) {
		this.timeStampBidArrivo = timeStampBidArrivo;
	}
	public String getIdArrivoNew() {
		return idArrivoNew;
	}
	public void setIdArrivoNew(String idArrivoNew) {
		this.idArrivoNew = idArrivoNew;
	}
	public String getData1DaPubblPartenza() {
		return data1DaPubblPartenza;
	}
	public void setData1DaPubblPartenza(String data1DaPubblPartenza) {
		this.data1DaPubblPartenza = data1DaPubblPartenza;
	}
	public String getImprontaPartenza() {
		return improntaPartenza;
	}
	public void setImprontaPartenza(String improntaPartenza) {
		this.improntaPartenza = improntaPartenza;
	}
	public String getTipNumStandardPartenza() {
		return tipNumStandardPartenza;
	}
	public void setTipNumStandardPartenza(String tipNumStandardPartenza) {
		this.tipNumStandardPartenza = tipNumStandardPartenza;
	}
	public String getTipoDataPubblPartenza() {
		return tipoDataPubblPartenza;
	}
	public void setTipoDataPubblPartenza(String tipoDataPubblPartenza) {
		this.tipoDataPubblPartenza = tipoDataPubblPartenza;
	}
	public String getValNumStandardPartenza() {
		return valNumStandardPartenza;
	}
	public void setValNumStandardPartenza(String valNumStandardPartenza) {
		this.valNumStandardPartenza = valNumStandardPartenza;
	}
	public String getSpecStrumVociNew() {
		return specStrumVociNew;
	}
	public void setSpecStrumVociNew(String specStrumVociNew) {
		this.specStrumVociNew = specStrumVociNew;
	}
	public String getSpecStrumVociOld() {
		return specStrumVociOld;
	}
	public void setSpecStrumVociOld(String specStrumVociOld) {
		this.specStrumVociOld = specStrumVociOld;
	}
	public String[] getInferioriDaCatturare() {
		return inferioriDaCatturare;
	}
	public void setInferioriDaCatturare(String[] inferioriDaCatturare) {
		this.inferioriDaCatturare = inferioriDaCatturare;
	}
	public String getDescPartenzaMsuperiore() {
		return descPartenzaMsuperiore;
	}
	public void setDescPartenzaMsuperiore(String descPartenzaMsuperiore) {
		this.descPartenzaMsuperiore = descPartenzaMsuperiore;
	}
}
