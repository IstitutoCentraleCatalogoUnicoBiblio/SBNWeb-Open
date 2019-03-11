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

public class DettaglioSoggettoGeneraleVO extends SerializableVO {

	private static final long serialVersionUID = 9155729071873421391L;

	private String livAut;

	private String cid;

	private String campoSoggettario;

	private String edizioneSoggettario;

	private String campoStringaSoggetto;

	private String dataAgg;

	private String dataIns;

	// Dati eventuali per legami
	private String bidPadre;

	private String descrizionePadre;

	private String notaAlLegame;

	private String tipoLegameValore;

	private String tipoLegame;

	public String getCampoStringaSoggetto() {
		return campoStringaSoggetto;
	}

	public void setCampoStringaSoggetto(String campoStringaSoggetto) {
		this.campoStringaSoggetto = campoStringaSoggetto;
	}

	public String getCampoSoggettario() {
		return campoSoggettario;
	}

	public void setCampoSoggettario(String campoSoggettario) {
		this.campoSoggettario = campoSoggettario;
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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}

	public String getBidPadre() {
		return bidPadre;
	}

	public void setBidPadre(String bidPadre) {
		this.bidPadre = bidPadre;
	}

	public String getTipoLegameValore() {
		return tipoLegameValore;
	}

	public void setTipoLegameValore(String tipoLegameValore) {
		this.tipoLegameValore = tipoLegameValore;
	}

	public String getDescrizionePadre() {
		return descrizionePadre;
	}

	public void setDescrizionePadre(String descrizionePadre) {
		this.descrizionePadre = descrizionePadre;
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

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

	public boolean isGestisceEdizione() {
		return isFilled(edizioneSoggettario);
	}
}
