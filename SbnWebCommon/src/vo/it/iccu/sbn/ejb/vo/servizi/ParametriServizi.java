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
package it.iccu.sbn.ejb.vo.servizi;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ParametriServizi extends SerializableVO {

	private static final long serialVersionUID = -7661993523547437152L;

	private static final String PARAMETRI_SERVIZI = "srv.params.servizi";
	private Map<ParamType, Serializable> params;

	public static ParametriServizi retrieve(HttpServletRequest request) {
		return (ParametriServizi) request.getAttribute(PARAMETRI_SERVIZI);
	}

	public static void send(HttpServletRequest request, ParametriServizi parametri) {
		request.setAttribute(PARAMETRI_SERVIZI, parametri);
	}

	public enum TipoOperazione {
		SALVA,
		SALVA_E_SCEGLI,
		CANCELLA,

		CREA_PRENOTAZIONE,
		RIFIUTA,
		ALLINEA_DATI_ILL,
		CREA_MOVIMENTO_LOCALE,
		INVIA_MESSAGGIO,
		CONDIZIONE,
		ARRIVO_MATERIALE,
		SELEZIONE_SERVIZIO_LOCALE;
	}

	public enum ModalitaCercaType {
		CERCA,
		CERCA_PER_EROGAZIONE,
		CERCA_PER_STAMPA_SERVIZI_CORRENTI
	}

	public enum ModalitaGestioneType {
		ESAMINA,
		GESTIONE,
		GESTIONE_SIF,
		GESTIONE_SIF_BATCH,//giro stampa servizi correnti
		CANCELLA,
		CREA,
		SPOSTA,
		CREA_SIF,
		CREA_OPAC;
	}

	public enum ParamType {

		MODALITA_CERCA_DOCUMENTO,
		MODALITA_GESTIONE_DOCUMENTO,

		LISTA_DOCUMENTI,
		LISTA_ESEMPLARI,
		DETTAGLIO_DOCUMENTO,
		DETTAGLIO_ESEMPLARE,

		PARAMETRI_RICERCA,
		PARAMETRI_SIF_DOCNONSBN,
		BIBLIOTECA,
		DETTAGLIO_MOVIMENTO,

		LISTA_SALE,
		DETTAGLIO_SALA,
		GRUPPO_POSTI_SALA,
		PRENOTAZIONE_POSTO,
		GRIGLIA_CALENDARIO,

		MODELLO_CALENDARIO,
		INTERVALLO_CALENDARIO,
		MODALITA_GESTIONE_PRENOT_POSTO;
	}

	public ParametriServizi() {
		super();
		this.params = new HashMap<ParamType, Serializable>();
	}

	public Serializable put(ParamType type, Serializable value) {
		return this.params.put(type, value);
	}

	public Serializable get(ParamType type) {
		return this.params.get(type);
	}

	public Serializable remove(ParamType type) {
		return this.params.remove(type);
	}

	public void clear() {
		this.params.clear();
	}

	public Enumeration<ParamType> getNames() {
		return Collections.enumeration(this.params.keySet());
	}

}
