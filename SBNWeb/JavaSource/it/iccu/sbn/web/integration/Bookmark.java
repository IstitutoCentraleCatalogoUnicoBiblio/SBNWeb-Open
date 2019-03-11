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
package it.iccu.sbn.web.integration;

public class Bookmark {

	public class Bibliografica {

		public class Titolo {
			public static final String CREA_TITOLO = "CREA_TITOLO";
		}
	}

	public class Acquisizioni {

		public class Ordini {
			public static final String IMPORTA_DATI_ORDINE = "acq.importa.dati.ordine";
		}
	}

	public class Servizi {

		public class ModuloWeb {
			public static final String INSERIMENTO_RICHIESTA = "srv.modulo.web.ins.richiesta";
		}

		public static final String LISTA_MOVIMENTI = "LISTA_MOVIMENTI";
		public static final String EROGAZIONE_RICERCA_ILL = "ErogazioneRicercaILL";
		public static final String RICERCA_SALE = "bookmark.ricerca.sale";
		public static final String PRENOTAZIONE_POSTO = "bookmark.prenot.posto";
		public static final String DETTAGLIO_MOVIMENTO = "srv.mov.dettaglio";
		public static final String DATI_RICHIESTA_ILL = "srv.dati.ill.bookmark";
	}

}
