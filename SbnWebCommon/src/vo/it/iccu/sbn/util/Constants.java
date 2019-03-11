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
package it.iccu.sbn.util;


public class Constants {

	public static class DocFisico {

		public static class Inventari {

			public static final String INVENTARIO_CANCELLATO = "0";
			public static final char INVENTARIO_CANCELLATO_CHR = INVENTARIO_CANCELLATO.charAt(0);

			public static final String INVENTARIO_PRECISATO = "1";
			public static final char INVENTARIO_PRECISATO_CHR = INVENTARIO_PRECISATO.charAt(0);

			public static final String INVENTARIO_COLLOCATO = "2";
			public static final char INVENTARIO_COLLOCATO_CHR = INVENTARIO_COLLOCATO.charAt(0);

			public static final String INVENTARIO_DISMESSO = "3";
			public static final char INVENTARIO_DISMESSO_CHR = INVENTARIO_DISMESSO.charAt(0);
		}
	}

	public static class Semantica {

		public static class Soggetti {

			public static final String REGEX_SEPARATORE_TERMINI_SOGGETTO = "\\s-\\s";
			public static final String SEPARATORE_TERMINI_SOGGETTO = " - ";

			public static final String SOGGETTARIO_FIRENZE_TUTTE = "FE";
			public static final String SOGGETTARIO_FIRENZE_NUOVA_ED = "FN";
			public static final String SOGGETTARIO_FIRENZE = "FI";

			public static final String[] SOGGETTARI_FIRENZE = new String[] {
				SOGGETTARIO_FIRENZE,
				SOGGETTARIO_FIRENZE_NUOVA_ED,
				SOGGETTARIO_FIRENZE_TUTTE
			};
			
			public static final int POSIZIONE_DESCRITTORE_MANUALE = 99;
		}

		public static class Classi {

			public static final String SISTEMA_CLASSE_DEWEY = "D";

		}
	}

	public static class Servizi {

		public static class Utenti {

			public static final String UTENTE_TIPO_SBNWEB = "S";
			public static final char UTENTE_TIPO_SBNWEB_CHR = UTENTE_TIPO_SBNWEB.charAt(0);
			public static final String UTENTE_TIPO_ESTERNO = "E";
			public static final char UTENTE_TIPO_ESTERNO_CHR = UTENTE_TIPO_ESTERNO.charAt(0);
			public static final int MAX_PASSWORD_LENGTH = 30;

		}
		
		public static class Movimenti {
			
			public static final String NUMBER_FORMAT_PREZZI = "###,###,###,##0.00";
			
		}
	}
}
