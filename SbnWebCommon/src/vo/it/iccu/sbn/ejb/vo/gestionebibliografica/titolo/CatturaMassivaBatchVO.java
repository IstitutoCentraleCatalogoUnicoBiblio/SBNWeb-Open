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

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaOutputAllineamentoVO;

import java.util.ArrayList;
import java.util.List;

public class CatturaMassivaBatchVO extends
		ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -3928765005469508413L;

	private List<String> listaBidDaCatturare;
	private String codErr;
	private String testoProtocollo;
	private List<TracciatoStampaOutputAllineamentoVO> logAnalitico = new ArrayList<TracciatoStampaOutputAllineamentoVO>();

	// Intervento almaviva2 Novembre 2013 INTERROGAZIONE MASSIVA - per gestione lista bid locali da confrontare con oggetti di Indice
	private boolean callInterrogPerCreazListe = false;

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}

	public List<String> getListaBidDaCatturare() {
		return listaBidDaCatturare;
	}

	public void setListaBidDaCatturare(List<String> list) {
		this.listaBidDaCatturare = list;
	}

	public List<TracciatoStampaOutputAllineamentoVO> getLogAnalitico() {
		return logAnalitico;
	}

	public void setLogAnalitico(
			List<TracciatoStampaOutputAllineamentoVO> logAnalitico) {
		this.logAnalitico = logAnalitico;
	}

	public boolean isCallInterrogPerCreazListe() {
		return callInterrogPerCreazListe;
	}

	public void setCallInterrogPerCreazListe(boolean callInterrogPerCreazListe) {
		this.callInterrogPerCreazListe = callInterrogPerCreazListe;
	}

}
