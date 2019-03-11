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

import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class AreaTabellaOggettiDaCatturareVO extends SerializableVO {

	private static final long serialVersionUID = 7804930870527918211L;


	private String idPadre;
	private String tipoAuthority;
	private String tipoMateriale;
	private String[] inferioriDaCatturare;

	private Map<String, SerializableVO> tabellaOggettiPerCattura;

	private boolean soloCopia;
	private boolean creaNuovoId;
	private boolean soloRadice;

	private boolean copiaReticolo;
	// almaviva2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:
	// L'evolutiva comporta la creazione di una nuova Monografia in tutto identica a quella di partenza con tutti
	// gli elementi del reticolo in cui solo i titolo analitici N saranno nuovi; gli altri saranno gli stessi di prima;
	// la Creazione in oggetto verrà fatta in indice e poi catturata in polo;
	private boolean copiaReticoloConSpoglioCondivisione;
	// MAIL Scognamiglio del 03.01.2017 Collaudo nuova funzionalità copia reticolo con spogli
	// Intervento almaviva2 gennaio 2017
	private String livAutUtente;

	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
	private boolean allineamentoDaFileLocale;


	// almaviva2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
	// la cattura avviene solo per le authority selzionate con il check (come per le W)
	// viene inserito il campo ticket per le chiamate all'oggetto per la cattura dei soggetti
	private String ticket;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	private boolean copiaSpoglioSenzaMadre;
	private String idNewMadre;
	private boolean spoglioCondiviso;

	// Inizio Modifica almaviva2 02.03.2010 - richiesta interna
	// Nuovo flag per gestire la cattura quando si proviene dall'Allinea quindi
	// non si deve richiedere la localizzazione in Indice
	private boolean provenienzaAllineamento;
	// FineModifica almaviva2 02.03.2010 - richiesta interna
	SbnOutputType sbnOutputTypeDaAllineare;

	public SbnOutputType getSbnOutputTypeDaAllineare() {
		return sbnOutputTypeDaAllineare;
	}

	public void setSbnOutputTypeDaAllineare(
			SbnOutputType sbnOutputTypeDaAllineare) {
		this.sbnOutputTypeDaAllineare = sbnOutputTypeDaAllineare;
	}

	public boolean isCreaNuovoId() {
		return creaNuovoId;
	}

	public void setCreaNuovoId(boolean creaNuovoId) {
		this.creaNuovoId = creaNuovoId;
	}

	public boolean isSoloCopia() {
		return soloCopia;
	}

	public void setSoloCopia(boolean soloCopia) {
		this.soloCopia = soloCopia;
	}

	public boolean isSoloRadice() {
		return soloRadice;
	}

	public void setSoloRadice(boolean soloRadice) {
		this.soloRadice = soloRadice;
	}

	public String getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}

	public String getTipoAuthority() {
		return tipoAuthority;
	}

	public void setTipoAuthority(String tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}

	public Map<String, SerializableVO> getTabellaOggettiPerCattura() {
		return tabellaOggettiPerCattura;
	}

	public void setTabellaOggettiPerCattura(Map<String, SerializableVO> tabellaOggettiPerCattura) {
		this.tabellaOggettiPerCattura = tabellaOggettiPerCattura;
	}

	public String[] getInferioriDaCatturare() {
		return inferioriDaCatturare;
	}

	public void setInferioriDaCatturare(String[] inferioriDaCatturare) {
		this.inferioriDaCatturare = inferioriDaCatturare;
	}

	public AreaTabellaOggettiDaCatturareVO() {
		super();
		this.soloCopia = false;
		this.soloRadice = false;
		this.creaNuovoId = false;
		this.copiaReticolo = false;
		this.provenienzaAllineamento = false;
		this.allineamentoDaFileLocale = false;
		this.tabellaOggettiPerCattura = new TreeMap<String, SerializableVO>(stringComparator);
	}

	private static final Comparator<String> stringComparator = new Comparator<String>() {
		public int compare(String s1, String s2) {
			return s1.compareTo(s2);
		}
	};

	public boolean isCopiaReticolo() {
		return copiaReticolo;
	}

	public void setCopiaReticolo(boolean copiaReticolo) {
		this.copiaReticolo = copiaReticolo;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public boolean isProvenienzaAllineamento() {
		return provenienzaAllineamento;
	}

	public void setProvenienzaAllineamento(boolean provenienzaAllineamento) {
		this.provenienzaAllineamento = provenienzaAllineamento;
	}

	public boolean isCopiaSpoglioSenzaMadre() {
		return copiaSpoglioSenzaMadre;
	}

	public void setCopiaSpoglioSenzaMadre(boolean copiaSpoglioSenzaMadre) {
		this.copiaSpoglioSenzaMadre = copiaSpoglioSenzaMadre;
	}

	public String getIdNewMadre() {
		return idNewMadre;
	}

	public void setIdNewMadre(String idNewMadre) {
		this.idNewMadre = idNewMadre;
	}

	public boolean isSpoglioCondiviso() {
		return spoglioCondiviso;
	}

	public void setSpoglioCondiviso(boolean spoglioCondiviso) {
		this.spoglioCondiviso = spoglioCondiviso;
	}

	public boolean isCopiaReticoloConSpoglioCondivisione() {
		return copiaReticoloConSpoglioCondivisione;
	}

	public void setCopiaReticoloConSpoglioCondivisione(
			boolean copiaReticoloConSpoglioCondivisione) {
		this.copiaReticoloConSpoglioCondivisione = copiaReticoloConSpoglioCondivisione;
	}

	public String getLivAutUtente() {
		return livAutUtente;
	}

	public void setLivAutUtente(String livAutUtente) {
		this.livAutUtente = livAutUtente;
	}

	public boolean isAllineamentoDaFileLocale() {
		return allineamentoDaFileLocale;
	}

	public void setAllineamentoDaFileLocale(boolean allineamentoDaFileLocale) {
		this.allineamentoDaFileLocale = allineamentoDaFileLocale;
	}

}
