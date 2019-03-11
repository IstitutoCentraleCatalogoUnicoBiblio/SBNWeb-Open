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

package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;


public class InterrogazioneTitoloElettronicoVO   extends SerializableVO {

	private static final long serialVersionUID = -5223404883414830821L;

	// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
	// Gestione nuovi campi specifici per etichetta 135
	private List listaTipoRisorsaElettronica;
	private String tipoRisorsaElettronicaSelez;

	private List listaIndicazioneSpecificaMateriale;
	private String indicazioneSpecificaMaterialeSelez;


	public List getListaTipoRisorsaElettronica() {
		return listaTipoRisorsaElettronica;
	}
	public void setListaTipoRisorsaElettronica(List listaTipoRisorsaElettronica) {
		this.listaTipoRisorsaElettronica = listaTipoRisorsaElettronica;
	}
	public String getTipoRisorsaElettronicaSelez() {
		return tipoRisorsaElettronicaSelez;
	}
	public void setTipoRisorsaElettronicaSelez(String tipoRisorsaElettronicaSelez) {
		this.tipoRisorsaElettronicaSelez = tipoRisorsaElettronicaSelez;
	}
	public List getListaIndicazioneSpecificaMateriale() {
		return listaIndicazioneSpecificaMateriale;
	}
	public void setListaIndicazioneSpecificaMateriale(
			List listaIndicazioneSpecificaMateriale) {
		this.listaIndicazioneSpecificaMateriale = listaIndicazioneSpecificaMateriale;
	}
	public String getIndicazioneSpecificaMaterialeSelez() {
		return indicazioneSpecificaMaterialeSelez;
	}
	public void setIndicazioneSpecificaMaterialeSelez(
			String indicazioneSpecificaMaterialeSelez) {
		this.indicazioneSpecificaMaterialeSelez = indicazioneSpecificaMaterialeSelez;
	}
}
