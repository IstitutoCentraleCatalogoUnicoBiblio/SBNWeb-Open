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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;

public class DettaglioClasseGeneraleVO extends SerializableVO {

	private static final long serialVersionUID = -2560874271896340114L;
	private String livAut;
	private String identificativo;
	private String descrizione;

	private String dataAgg;
	private String dataIns;

	// Dati eventuali per legami
	private String idPadre;
	private String descrizionePadre;
	private String tipoLegameValore;

	private String tipoLegame;
	private SimboloDeweyVO dewey = new SimboloDeweyVO();

	private String notaAlLegame;

	public String getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
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

	public String getDescrizionePadre() {
		return descrizionePadre;
	}

	public void setDescrizionePadre(String descrizionePadre) {
		this.descrizionePadre = trimAndSet(descrizionePadre);
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativoClasse) throws ValidationException {
		this.identificativo = trimAndSet(identificativoClasse);
		this.dewey = SimboloDeweyVO.parse(identificativoClasse);
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public String getTipoLegameValore() {
		return tipoLegameValore;
	}

	public void setTipoLegameValore(String tipoLegameValore) {
		this.tipoLegameValore = tipoLegameValore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public SimboloDeweyVO getDewey() {
		return dewey;
	}

	public void setDewey(SimboloDeweyVO dewey) {
		this.dewey = dewey;
	}

	public String getNotaAlLegame() {
		return notaAlLegame;
	}

	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = trimAndSet(notaAlLegame);
	}
}
