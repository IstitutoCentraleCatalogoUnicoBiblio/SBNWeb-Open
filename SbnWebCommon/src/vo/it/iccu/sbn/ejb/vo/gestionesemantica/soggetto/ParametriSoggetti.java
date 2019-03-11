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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ParametriSoggetti extends SerializableVO {

	private static final long serialVersionUID = 409021153845055648L;
	private static final String PARAMETRI_SOGGETTI = "gs.params.SOGG.DES.req";
	private Map<SoggettiParamType, Serializable> params;

	public static ParametriSoggetti retrieve(HttpServletRequest request) {
		return (ParametriSoggetti) request.getAttribute(PARAMETRI_SOGGETTI);
	}

	public static void send(HttpServletRequest request,
			ParametriSoggetti parametri) {
		request.setAttribute(PARAMETRI_SOGGETTI, parametri.clone() );
	}

	public enum ModalitaConfermaType {
		CANCELLA_SOGGETTO,
		CANCELLA_DESCRITTORE,
		CANCELLA_LEGAME,
		FONDI_CAMBIO_EDIZIONE,
		CAMBIO_EDIZIONE;
	}

	public enum SoggettiParamType {

		RICARICA_RETICOLO_XID, TIPO_OGGETTO_PADRE, TIPO_OGGETTO_CID, TIPO_OGGETTO_DID, DID_PADRE,
		ACTION_CALLER, LIVELLO_AUTORITA, LIVELLO_RICERCA, LIVELLO_RICERCA_POLO, ABILITA_RICERCA_INDICE,
		PARAMETRI_RICERCA, OUTPUT_SINTETICA, OUTPUT_SINTETICA_PRIMA, FOLDER_CORRENTE, CODICE_SOGGETTARIO, ELEMENTI_PER_BLOCCO, LISTA_OGGETTI_SELEZIONATI, PROGRESSIVO_SELEZIONATO,
		DATI_BIBLIOGRAFICI, TITOLI_COLLEGATI_POLO, TITOLI_COLLEGATI_BIBLIO, ANALITICA, ANALITICA_INDICE, TIPO_SOGGETTO,
		DATA_INSERIMENTO, DATA_MODIFICA, DATA_MODIFICA_T005,
		DESCRIZIONE_OGGETTO, CID_INDICE,
		CID_RIFERIMENTO, DID_RIFERIMENTO, TESTO_OGGETTO_CORRENTE, TESTO_SOGGETTO, NOTE_AL_LEGAME, TIPO_LEGAME,
		ABILITA_TRASCINAMENTO, TRASCINA_CID_PARTENZA, TRASCINA_CID_ARRIVO,
		TRASCINA_TESTO_PARTENZA, TRASCINA_TESTO_ARRIVO, TRASCINA_OUTPUT_SINTETICA, OGGETTO_CONDIVISO_INDICE,
		ENABLE_ANALITICA_SOGGETTO, DETTAGLIO_DESCRITTORE, DESCRITTORE_MANUALE, NOTE_OGGETTO, HAS_LEGAMI_POLO, HAS_LEGAMI_BIBLIO,
		PAROLE, RICERCA_SOGGETTI_PER_PAROLE, DESCRITTORI_DEL_SOGGETTO, DETTAGLIO_SOGGETTO,
		DID_PARTENZA_LEGAME, DID_PARTENZA_LEGAME_TESTO, DID_PARTENZA_LEGAME_FORMA_NOME, DID_PARTENZA_LEGAME_LIVELLO_AUTORITA, DID_ARRIVO_LEGAME, DID_ARRIVO_LEGAME_TESTO, DID_ARRIVO_LEGAME_FORMA_NOME, DID_ARRIVO_LEGAME_LIVELLO_AUTORITA,
		DID_RADICE_LEGAMI, DATA_MODIFICA_DID_RADICE, MODALITA_CONFERMA, SOGGETTO_SIMILE,
		DETTAGLIO_ID_PARTENZA, DETTAGLIO_ID_ARRIVO;
	}

	public ParametriSoggetti() {
		super();
		this.params = new HashMap<SoggettiParamType, Serializable>();
	}

	public Serializable put(SoggettiParamType type, Serializable value) {
		return this.params.put(type, value);
	}

	public Serializable get(SoggettiParamType type) {
		return this.params.get(type);
	}

	public Serializable remove(SoggettiParamType type) {
		return this.params.remove(type);
	}

	public void clear() {
		this.params.clear();
	}

	public Enumeration<SoggettiParamType> getNames() {
		return Collections.enumeration(this.params.keySet());
	}

}
