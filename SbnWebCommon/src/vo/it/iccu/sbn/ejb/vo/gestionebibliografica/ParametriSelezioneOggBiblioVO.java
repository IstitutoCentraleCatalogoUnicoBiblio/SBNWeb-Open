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

package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ParametriSelezioneOggBiblioVO   extends SerializableVO {

	private static final long serialVersionUID = 4761354156447491806L;


// Dati relativi alla ricerca titolo - Canali principali
	private String naturaSelez;
	private String genereSelez;
	private String tipoDataSelez;
	private String livAutSelez;
	private String linguaSelez;
	private String paeseSelez;


	public String getNaturaSelez() {
		return naturaSelez;
	}
	public void setNaturaSelez(String naturaSelez) {
		this.naturaSelez = naturaSelez;
	}
	public String getGenereSelez() {
		return genereSelez;
	}
	public void setGenereSelez(String genereSelez) {
		this.genereSelez = genereSelez;
	}
	public String getTipoDataSelez() {
		return tipoDataSelez;
	}
	public void setTipoDataSelez(String tipoDataSelez) {
		this.tipoDataSelez = tipoDataSelez;
	}
	public String getLivAutSelez() {
		return livAutSelez;
	}
	public void setLivAutSelez(String livAutSelez) {
		this.livAutSelez = livAutSelez;
	}
	public String getLinguaSelez() {
		return linguaSelez;
	}
	public void setLinguaSelez(String linguaSelez) {
		this.linguaSelez = linguaSelez;
	}
	public String getPaeseSelez() {
		return paeseSelez;
	}
	public void setPaeseSelez(String paeseSelez) {
		this.paeseSelez = paeseSelez;
	}
}
