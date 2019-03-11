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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class AreaDatiCatturaReticoloVO extends SerializableVO {

	private static final long serialVersionUID = -5222931557375571107L;

	private String idOggetto;
	private String versioneOggetto;
	private String tipoAuthority;
	private String tipoMateriale;
	private String livAut;
	private String natura;
	private String stato;
	private DatiDocType datiDocType;
	private TitAccessoType titAccessoType;
	private DatiElementoType datiElementoType;

	private LegamiType legamiType;
	private LegameElementoAutType legameElementoAutType;
	private LegameDocType legameDocType;
	private LegameTitAccessoType legameTitAccessoType;

	private String notaEsplicativa;

	// almaviva2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
	// la cattura avviene solo per le authority selzionate con il check (come per le W)
	private String corrispondenzaCidIndCidPol;


	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
	private boolean allineamentoDaFileLocale;

	public String getCorrispondenzaCidIndCidPol() {
		return corrispondenzaCidIndCidPol;
	}

	public void setCorrispondenzaCidIndCidPol(String corrispondenzaCidIndCidPol) {
		this.corrispondenzaCidIndCidPol = corrispondenzaCidIndCidPol;
	}

	public DatiDocType getDatiDocType() {
		return datiDocType;
	}

	public void setDatiDocType(DatiDocType datiDocType) {
		this.datiDocType = datiDocType;
	}

	public String getIdOggetto() {
		return idOggetto;
	}

	public void setIdOggetto(String idOggetto) {
		this.idOggetto = idOggetto;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getTipoAuthority() {
		return tipoAuthority;
	}

	public void setTipoAuthority(String tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}

	public TitAccessoType getTitAccessoType() {
		return titAccessoType;
	}

	public void setTitAccessoType(TitAccessoType titAccessoType) {
		this.titAccessoType = titAccessoType;
	}

	public DatiElementoType getDatiElementoType() {
		return datiElementoType;
	}

	public void setDatiElementoType(DatiElementoType datiElementoType) {
		this.datiElementoType = datiElementoType;
	}

	public LegamiType getLegamiType() {
		return legamiType;
	}

	public void setLegamiType(LegamiType legamiType) {
		this.legamiType = legamiType;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
	}

	public String getVersioneOggetto() {
		return versioneOggetto;
	}

	public void setVersioneOggetto(String versioneOggetto) {
		this.versioneOggetto = versioneOggetto;
	}

	public LegameDocType getLegameDocType() {
		return legameDocType;
	}

	public void setLegameDocType(LegameDocType legameDocType) {
		this.legameDocType = legameDocType;
	}

	public LegameElementoAutType getLegameElementoAutType() {
		return legameElementoAutType;
	}

	public void setLegameElementoAutType(
			LegameElementoAutType legameElementoAutType) {
		this.legameElementoAutType = legameElementoAutType;
	}

	public LegameTitAccessoType getLegameTitAccessoType() {
		return legameTitAccessoType;
	}

	public void setLegameTitAccessoType(
			LegameTitAccessoType legameTitAccessoType) {
		this.legameTitAccessoType = legameTitAccessoType;
	}

	public String getNotaEsplicativa() {
		return notaEsplicativa;
	}

	public void setNotaEsplicativa(String notaEsplicativa) {
		this.notaEsplicativa = notaEsplicativa;
	}

	public boolean isAllineamentoDaFileLocale() {
		return allineamentoDaFileLocale;
	}

	public void setAllineamentoDaFileLocale(boolean allineamentoDaFileLocale) {
		this.allineamentoDaFileLocale = allineamentoDaFileLocale;
	}

}
