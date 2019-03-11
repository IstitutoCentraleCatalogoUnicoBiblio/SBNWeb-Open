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
package it.iccu.sbn.web.constant;


public class TitoliCollegatiInvoke {


	public final static int TITOLI_COLLEGATI_A_TITOLO = 0;
	public final static int TITOLI_COLLEGATI_A_AUTORE = 1;
	public final static int TITOLI_COLLEGATI_A_MARCA = 2;
	public final static int TITOLI_COLLEGATI_A_LUOGO = 4;
	public final static int TITOLI_COLLEGATI_A_SOGGETTO = 5;
	public final static int TITOLI_COLLEGATI_A_CLASSE = 6;
	public final static int TITOLI_COLLEGATI_A_THESAURO = 7;
	public final static int AUTORI_COLLEGATI_A_TITOLO = 8;
	public final static int AUTORI_COLLEGATI_A_MARCA = 9;
	public final static int MARCHE_COLLEGATE_A_TITOLO = 10;
	public final static int MARCHE_COLLEGATE_A_AUTORE = 11;
	public final static int LOCALIZZAZIONI = 12;

	public final static int SINTETICA = 20;
	public final static int ANALITICA = 21;
	public final static int DETTAGLIO = 22;
	public final static int ANALITICA_DETTAGLIO = 23;

	public final static int LIV_DI_RICERCA_IGNOTO = 42;
	public final static int LIV_DI_RICERCA_INDICE = 40;
	public final static int LIV_DI_RICERCA_POLO = 41;
	public final static int LIV_DI_RICERCA_BIBLIO = 43;

	public final static int LOCAL_POL_GESTIONE = 44; // (BLU) Tutte le Authority purchè presenti in Polo e i Documenti solo per gestione
	public final static int LOCAL_POL_POSSESSO = 45; //  Situazione inesistente in quanto solo una Biblioteca può avere il Possesso di un Documento
	public final static int LOCAL_POL_COMPLETA = 46; //  Situazione inesistente in quanto solo una Biblioteca può avere il Possesso di un Documento
	public final static int LOCAL_BIB_GESTIONE = 47; // (BLU) Tutte le Authority purchè presenti in Polo e i Documenti solo per gestione
	public final static int LOCAL_BIB_POSSESSO = 48; // (ARANCIONE/GIALLO) Solo i Documenti con localizzazione SOLO per Possesso
	public final static int LOCAL_BIB_COMPLETA = 49; // (VERDE) Solo i Documenti con localizzazione per Gestione/Possesso
	public final static int LOCAL_NESSUNA = 50;		 // (ROSSO) Situazione in cui l'oggetto non è localizzata per il Polo


	public final static String livDiRicerca = "a";
	public final static String oggDiRicerca = "b";
	public final static String oggChiamante = "c";
	public final static String xidDiRicerca = "d";
	public final static String xidDiRicercaDesc = "e";
	public final static String codBiblio = "f";
	public final static String visualCall = "g";
	public final static String arrayListSintetica = "h";
	public final static String naturaDiRicerca = "i";
	public final static String tipMatDiRicerca = "l";

	// INIZIO Modifica almaviva2 da richiesta interna:
	// inserimento segnalibro (ANALITICA_PROGRESS) per verificare il passaggio dall'Analitica in Vai a
	public static final String ANALITICA_PROGRESS = "navi.bookmark.analitica.progress";

	// inserimento segnalibro (GESTIONE_DA_SINT_SCHEDE) per verificare il passaggio dalla sintetica schede ed eventualmente
	// aggiornare le tabelle con il trattamento effettuato
	public static final String GESTIONE_DA_SINT_SCHEDE = "navi.bookmark.sintetica.schede";

	//almaviva5_20110520 periodici
	public static final String AreePassaggioSifVO = "AreePassaggioSifVO";

}
