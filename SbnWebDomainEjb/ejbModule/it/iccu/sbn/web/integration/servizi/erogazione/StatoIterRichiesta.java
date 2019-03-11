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
package it.iccu.sbn.web.integration.servizi.erogazione;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.Map;

public enum StatoIterRichiesta {
	CUSTOM,

	RICHIESTA_SERVIZIO								("01", 1),
	SCARICO_DA_MAGAZZINO							("02"),
	CONSEGNA_DOCUMENTO_AL_LETTORE					("03"),
	RESTITUZIONE_DOCUMENTO							("04"),
	COLLOCAZIONE_PUNTO_DEPOSITO						("05"),
	RICOLLOCAZIONE_A_MAGAZZINO						("06"),
	RICEZIONE_PRESSO_UFFICIO_FOTORIPRODUZIONE		("07"),
	CONSEGNA_COPIE									("08"),

	CONTROLLO_DISPONIBILITA							("CD"),

	PRENOTAZIONE_POSTO								("PP", 1),
/*
	REGISTRAZIONE_ARRIVO_DOCUMENTO,
	SPEDIZIONE_A_BIBLIOTECA_PRESTANTE,
	SPEDIZIONE_A_BIBLIOTECA_RICHIEDENTE,
	RESTITUZIONE_PER_BIBLIOTECA_RICHIEDENTE,
	REGISTRAZIONE_RICHIESTA_DA_ALTRA_BIBLIOTECA,
	IMPOSTA_DATA_INIZIO,
	IMPOSTA_DATA_FINE,

	NUMERO_PRENOTAZIONI_PER_DOCUMENTO,
	NUMERO_PRENOTAZIONI_TOTALE,
	ESISTONO_PRENOTAZIONI_PER_DOCUMENTO,

	RICOLLOCAZIONE_PUNTO_DEPOSITO,
*/

	//almaviva5_20151204
	F100_DEFINIZIONE_RICHIESTA_DA_UTENTE			("F100"),
	F111_DEFINIZIONE_RICHIESTA						("F111"),
	F112A_PROPOSTA_DI_ANNULLAMENTO					("F112A"),
	F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA			("F113A"),
	F113B_ACCETTAZIONE_CONDIZIONE_SU_RICHIESTA 		("F113B"),
	F114_ARRIVO_MATERIALE							("F114"),
	F115_RICHIESTA_DI_RINNOVO_PRESTITO				("F115"),
	F116_RESTITUZIONE_MATERIALE						("F116"),
	F116A_PERDITA_DEL_MATERIALE_PRESTATO			("F116A"),
	F116B_MATERIALE_DANNEGGIATO						("F116B"),
	F118_INVIO_A_BIB_DESTINATARIA					("F118"),
	F1211_RICHIESTA_CONDIZIONATA					("F1211", true, true, 0),
	F1212_RICHIESTA_NON_SODDISFACIBILE				("F1212"),
	F1213_LISTA_POSSIBILI_DESTINATARI				("F1213"),
	F1214_ACCETTAZIONE_RICHIESTA					("F1214"),
	F1215_PRENOTAZIONE_DOCUMENTO					("F1215"),
	F1218_TERMINE_SCADUTO							("F1218"),
	F1220_RIFIUTO_ANNULLAMENTO						("F1220"),
	F1221_CONFERMA_ANNULLAMENTO						("F1221"),
	F127_SPEDIZIONE_MATERIALE						("F127"),
	F128_RICEZIONE_MATERIALE						("F128"),
	F129_CONFERMA_RINNOVO							("F129"),
	F12A_NEGAZIONE_RINNOVO							("F12A"),
	F12D_SOLLECITO_RESTITUZIONE_PRESTITO			("F12D"),
	F12E_RICHIESTA_RESTITUZIONE_URGENTE				("F12E"),
	F127A_STIMA_DEI_COSTI							("F127A", true, false, 0);

	private static Map<String, StatoIterRichiesta> values;

	static {
		StatoIterRichiesta[] stati = StatoIterRichiesta.class.getEnumConstants();
		values = new THashMap<String, StatoIterRichiesta>();
		for (int i = 0; i < stati.length; i++)
			values.put(stati[i].isoCode, stati[i]);
	}

	public static final StatoIterRichiesta of(String value) {
		if (!ValidazioneDati.isFilled(value) )
			return null;

		StatoIterRichiesta stato = StatoIterRichiesta.values.get(value.trim() );
		return stato != null ? stato : CUSTOM;
	}

	private final String isoCode;
	private final boolean withNotes;
	private final boolean withCondition;
	private final int position;

	public String getISOCode() {
		return isoCode;
	}

	public boolean withNotes() {
		return withNotes;
	}

	public boolean withCondition() {
		return withCondition;
	}

	public int getPosition() {
		return position;
	}

	public boolean isILL() {
		return isoCode.startsWith("F");
	}

	private StatoIterRichiesta() {
		this.isoCode = String.format("%02d", ordinal() + 1);
		this.withNotes = false;
		this.withCondition = false;
		this.position = 0;
	}

	private StatoIterRichiesta(String state) {
		this(state, false, false, 0);
	}

	private StatoIterRichiesta(String state, int position) {
		this(state, false, false, position);
	}

	private StatoIterRichiesta(String state, boolean withNotes) {
		this(state, withNotes, false, 0);
	}

	private StatoIterRichiesta(String state, boolean withNotes, boolean withCondition, int position) {
		this.isoCode = state;
		this.withNotes = withNotes;
		this.withCondition = withCondition;
		this.position = position;
	}

}
