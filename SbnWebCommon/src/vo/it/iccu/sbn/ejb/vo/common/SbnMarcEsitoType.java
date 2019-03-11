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
package it.iccu.sbn.ejb.vo.common;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum SbnMarcEsitoType {

	ERRORE_GENERICO("-1"),
	ERRORE_MARSHALLING("101"),
	ERRORE_XML_INESISTENTE("102"),

	OK("0000"),
	ERRORE_ACCESSO_DB("1050"),
	ID_ESISTENTE("3012"),
	TROVATI_SIMILI("3004"),
	TROPPI_ELEMENTI("3003"),
	NON_TROVATO("3001"),
	SIMILE_ESISTENTE("7912"),	//thesauro
	ID_ESISTE_COME_RINVIO("9999"),
	SOGGETTARIO_NON_ABILITATO("3211"),
	LIVELLO_AUTORITA_INSUFFICIENTE("3010"),
	LIVELLO_AUTORITA_NON_SUFFICIENTE("3007"),
	UTENTE_NON_ABILITATO_FORZATURA("3008"),
	CARATTERE_NON_VALIDO("3914"),
	AUTORE_NON_ESISTENTE("3022"),
	AUTORE_NON_TIPO_ENTE("3292"),

	//almaviva5_20120911 #5032
	SISTEMA_EDIZIONE_NON_ABILITATE("3255"),

	EDIZIONE_INCONGRUENTE("3342"),
	DESCRITTORE_ESISTE_COME_RINVIO("3343"),
	TROVATO_SIMILE_EDIZIONE_DIVERSA("3344"),
	DESCRITTORE_CON_LEGAMI_STORICI("3345"),
	DESCRITTORE_ESISTE_FORMA_ACCETTATA("3346"),

	ULTIMA_VARIAZIONE_NON_COINCIDENTE("3014"),

	//almaviva5_20140204 evolutive google3
	SERVER_NON_DISPONIBILE("9876", "Server momentaneamente non raggiungibile"),
	LOCALIZZA_CORREZIONE("7017"),

	//almaviva5_20140409
	TIPO_NUMERO_STD_INESISTENTE("7021"),
	EDIZIONE_DEWEY_INESISTENTE("7022"),

	//almaviva5_20141007 #5650
	ID_NON_VALIDO("3902"),

	ELABORAZIONE_IN_DIFFERITA("3089"),

	//almaviva5_20150915
	LUNGHEZZA_ECCESSIVA("3219"),

	//almaviva5_20171123
	LEGAME_ESISTENTE("3030"),

	SOGGETTO_PORTATO_LIVELLO_ALTRO_UTENTE("3353"),
	SOGGETTO_GIA_MAPPATO("2900"),
	LEGAME_NON_AMMESSO("3079"),

	UTENTE_NON_AUTORIZZATO("4000");

	//////////////////////////////////////////

	private static final Map<String, SbnMarcEsitoType> values;
	private final String esito;
	private final String testoEsito;


	static {
		SbnMarcEsitoType[] codici = SbnMarcEsitoType.class.getEnumConstants();
		values = new THashMap<String, SbnMarcEsitoType>();
		for (int i = 0; i < codici.length; i++)
			values.put(codici[i].esito, codici[i]);
	}

	public static final SbnMarcEsitoType of(String value) {
		SbnMarcEsitoType type = null;
		if (ValidazioneDati.isFilled(value) )
			type = SbnMarcEsitoType.values.get(value.trim() );

		return type != null ? type : ERRORE_GENERICO;
	}

	private SbnMarcEsitoType(String esito) {
		this.esito = esito;
		this.testoEsito = null;
	}

	private SbnMarcEsitoType(String esito, String testoEsito) {
		this.esito = esito;
		this.testoEsito = testoEsito;
	}

	public String getEsito() {
		return esito;
	}

	public String getTestoEsito() {
		return testoEsito;
	}

	public static void main(String... args) {
		//check duplicati
		Set<String> types = new HashSet<String>(SbnMarcEsitoType.values().length);
		System.out.println("--- INIZIO TEST ---");
		for (SbnMarcEsitoType e : SbnMarcEsitoType.values()) {
			String esito = e.getEsito();
			if (types.contains(esito))
				System.out.println("Esito duplicato: " + e.name());
			else
				types.add(esito);
		}
		System.out.println("--- FINE TEST ---");
	}

}
