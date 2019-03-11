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

public enum ConstantDefault {

	//generico
	LANGUAGE														("it"),
	ELEMENTI_BLOCCHI												("10"),
	ELEMENTI_BLOCCHI_TIT_COLL										("10"),

	//======================================
	// AREA GESTIONE BIBLIOGRAFICA - TITOLO (Etichette in AmministrazioneSistemaLabels.properties)
	//======================================
	// Schermata Ricerca titolo (01_GestBibTitolo)
	RIC_TIT_ELEMENTI_BLOCCHI				("10"),
	RIC_TIT_FORMATO_LISTA					("001"),
	RIC_TIT_ORDINAMENTO						("2"),
	RIC_TIT_PUNTUALE						("FALSE"),
	RIC_TIT_LIVELLO_POLO					("TRUE"),	// Gruppo 1 check
	RIC_TIT_LIVELLO_INDICE					("TRUE"),	// Gruppo 1 check

	// Schermata Crea titolo (01_GestBibCreaTitolo)
	CREA_TIT_NATURA1						(""),
//	CREA_TIT_NATURA2						(""),
//	CREA_TIT_NATURA3						(""),
//	CREA_TIT_NATURA4						(""),
	CREA_TIT_TIPO_MATERIALE					("M"),
	CREA_TIT_TIPO_RECORD					("a"),
	CREA_TIT_LIVELLO_AUTORITA				("51"),
	CREA_TIT_PAESE							("IT"),
	CREA_TIT_LINGUA							("ITA"),
	CREA_TIT_TIPO_NUMERO_STANDARD			("I"),

	//======================================
	// AREA GESTIONE BIBLIOGRAFICA - AUTORE (Etichette in AmministrazioneSistemaLabels.properties)
	//======================================
	// Schermata Ricerca autore (01_GestBibAutori)
	RIC_AUT_ELEMENTI_BLOCCHI				("15"),
	RIC_AUT_ORDINAMENTO						("1"), // 1 NOME + TIPO NOME, 2 ULTIMO AGG + NOME, 3 IDENTIFICATIVO

	RIC_AUT_LIVELLO_POLO					("TRUE"),	// Gruppo 10 check
	RIC_AUT_LIVELLO_INDICE					("TRUE"),	// Gruppo 10 check

	RIC_AUT_NOME_INIZIO						("TRUE"),  	// Gruppo 11 radio
	RIC_AUT_NOME_PUNTUALE					("FALSE"), 	// Gruppo 11 radio
	RIC_AUT_NOME_PAROLE						("FALSE"), 	// Gruppo 11 radio

	RIC_AUT_TIPO_NOME_TUTTI					("TRUE"),  	// Gruppo 5 radio
	RIC_AUT_TIPO_NOME_PERSONALE				("FALSE"), 	// Gruppo 5 radio
	RIC_AUT_TIPO_NOME_COLLETTIVO			("FALSE"), 	// Gruppo 5 radio

	RIC_AUT_FORMA_TUTTI						("TRUE"),  	// Gruppo 12 radio
	RIC_AUT_FORMA_ACCETTATA					("FALSE"),	// Gruppo 12 radio
	RIC_AUT_FORMA_RINVIO					("FALSE"),  // Gruppo 12 radio

	// Schermata Crea autore (01_GestBibCreaAutore)
	CREA_AUT_LIVELLO_AUTORITA				("51"),

	CREA_AUT_TIPO_NOME 						("C"),// Come opzione


	//======================================
	// AREA GESTIONE BIBLIOGRAFICA - MARCHE (Etichette in AmministrazioneSistemaLabels.properties)
	//======================================
	// Interrogazione marca
	RIC_MAR_ELEMENTI_BLOCCHI				("15"),
	RIC_MAR_ORDINAMENTO						("1"), 	// Descrizione
	RIC_MAR_LIVELLO_POLO					("TRUE"),	// Gruppo 14 check
	RIC_MAR_LIVELLO_INDICE					("TRUE"),	// Gruppo 14 check

	// Creazione Marca
	CREA_MAR_LIVELLO_AUTORITA				("51"),


//	..continua
	//======================================
	// AREA GESTIONE BIBLIOGRAFICA - LUOGHI (Etichette in AmministrazioneSistemaLabels.properties)
	//======================================
	// Interrogazione luoghi
	RIC_LUO_ELEMENTI_BLOCCHI				("15"),
	RIC_LUO_ORDINAMENTO						("1"), // Denominazione
	RIC_LUO_LIVELLO_POLO					("TRUE"),	// Gruppo 15 check
	RIC_LUO_LIVELLO_INDICE					("TRUE"),	// Gruppo 15 check
	RIC_LUO_PUNTUALE						("FALSE"),

	// Creazione luogo
	CREA_LUO_LIVELLO_AUTORITA				("51"),
	CREA_LUO_PAESE							("IT"),

	//======================================
	// AREA GESTIONE SEMANTICA - (Etichette in AmministrazioneSistemaLabels.properties)
	//======================================
	//SOGGETTI - ricerca
	RIC_SOG_TESTO_INIZIALE					("TRUE"),
	RIC_SOG_TESTO_INTERO					("FALSE"),
	RIC_SOG_TESTO_PAROLA					("FALSE"),
	RIC_SOG_POS_DESCR						("0"),
	RIC_SOG_ELEMENTI_BLOCCHI				("15"),
	RIC_SOG_ORDINAMENTO						("2"),
	RIC_SOG_LIVELLO_POLO					("TRUE"),
	RIC_SOG_LIVELLO_INDICE					("FALSE"),
	//SOGGETTI - crea
	CREA_SOG_TIPO_SOGG						("1"), // soggetto comune
	CREA_SOG_LIVELLO_AUTORITA				("51"),

	//DESCRITTORE - ricerca
	RIC_DES_TESTO_INIZIALE					("TRUE"),
	RIC_DES_TESTO_INTERO					("FALSE"),
//	RIC_DES_ELEMENTI_BLOCCHI				("15"),
//	RIC_DES_ORDINAMENTO						("2"),
//	RIC_DES_LIVELLO_POLO					("TRUE"),
//	RIC_DES_LIVELLO_INDICE					("FALSE"),
	CREA_DES_DES_FORMA_TERMINE				("A"), //Accettata	 (FODE)
	CREA_DES_DES_TIPO_LEGAME				("BT"), //Ha come termine più esteso (LEDD)

	//CLASSI - ricerca
	RIC_CLA_SIMBOLO_PUNTUALE				("FALSE"),
	RIC_CLA_ELEMENTI_BLOCCHI				("15"),
	RIC_CLA_SIMBOLO_ORDINAMENTO				("1"),
//	RIC_CLA_EQVERB_ORDINAMENTO				("2"),
	RIC_CLA_LIVELLO_POLO					("TRUE"),
	RIC_CLA_LIVELLO_INDICE					("FALSE"),
	//CLASSI - crea
	CREA_CLA_LIVELLO_AUTORITA				("51"),

	//THESAURI - ricerca
	RIC_THE_TERMINE_PUNTUALE				("FALSE"),
	RIC_THE_ELEMENTI_BLOCCHI				("15"),
	RIC_THE_ORDINAMENTO						("2"),
	//CLASSI - crea
	CREA_THE_LIVELLO_AUTORITA				("51"),
	CREA_THE_FORMA_TERMINE					("A"), //Accettata	 (FODE)
	CREA_THE_THE_TIPO_LEGAME				("BT"), //Ha come termine più esteso (STLT)

	//======================================
	// AREA GESTIONE DOCUMENTO FISICO - (Etichette in AmministrazioneSistemaLabels.properties)
	//======================================
	GDF_TP_PRG_INV						("C"), //Progressivo automatico (CTNI) - accessionamento mat. Corrente
	GDF_CD_SERIE						(""),  //Codice serie - non impostato
	GDF_SEZ_COLL						(""),  //Codice sezione di collocazione - non impostato
	GDF_CAT_FRUIZIONE					(""),  //Categoria di fruizione (LCFR) - non impostato
	GDF_TP_ACQUISIZIONE					(""),  //Tipo di acquisizione (CTAC) - non impostato
	GDF_STATO_CONSERVAZIONE				("B"),  //Stato di conservazione (CSCO)- buono
	GDF_CD_NO_DISPONIBILITA				(""),  //Codice di non disponiblità (CCND) - non impostato
	GDF_CD_PROVENIENZA					(""),  //Codice di provenienza - non impostato
	GDF_ESA_COLL_ELEM_BLOCCHI			("15"),  //Esame collocazioni - elementi per blocco
	GDF_ESA_COLL_ORDINAMENTO			("CD"),    //Esame collocazioni - ordinamento lista (OCCO)
	GDF_RIC_POSS_ELEM_BLOCCHI			("15"),  //Ricerca Possessori - elementi per blocco
	GDF_RIC_POSS_ORDINAMENTO			("2"),  //Ricerca Possessori - ordinamento lista (OCPP)
    GDF_RIC_POSS_TESTO_NOME				("inizio"),	//Ricerca Possessori - Tipo ricerca per testo
	GDF_RIC_POSS_NOME_INIZIO			("TRUE"),
	GDF_RIC_POSS_NOME_PUNTUALE			("FALSE"),
	GDF_RIC_POSS_NOME_PAROLE			("FALSE"),
	GDF_RIC_POSS_TIPO_NOME_TUTTI		("TRUE"),  	// Ricerca Possessori - Tipo nome (gruppo di radio)
	GDF_RIC_POSS_TIPO_NOME_PERSONALE	("FALSE"),
	GDF_RIC_POSS_TIPO_NOME_COLLETTIVO	("FALSE"),
	GDF_RIC_POSS_FORMA_TUTTI			("TRUE"),  	// Ricerca Possessori - Forma nome (gruppo di radio)
	GDF_RIC_POSS_FORMA_ACCETTATA		("FALSE"),
	GDF_RIC_POSS_FORMA_RINVIO			("FALSE"),
	GDF_RIC_SOLO_DI_BIBLIOTECA			("FALSE"),
	GDF_CREA_POSS_LIVELLO_AUTORITA		("51"),
	GDF_CREA_POSS_TIPO_NOME 			("C"),	// Come opzione
	GDF_MODELLO_ETICH		 			("", EditorType.USER),
	GDF_NRO_COPIE_ETICH		 			("1"),



	//======================================
	// AREA ACQUISIZIONI - (Etichette in AmministrazioneSistemaLabels.properties)
	//======================================
	GA_RIC_ORD_ELEM_BLOCCHI				("15"), //Ricerca ordini - elementi per blocco
	GA_RIC_ORD_ORDINAMENTO				("3"),  //Ricerca ordini - ordinamento lista ()
	GA_RIC_ORD_STATO_APERTO				("TRUE"),	// Gruppo  check
	GA_RIC_ORD_STATO_CHIUSO				("FALSE"),	// Gruppo  check
	GA_RIC_ORD_STATO_ANNULLATO			("FALSE"),	// Gruppo  check

	GA_CREA_ORD_FORN_TIPO_A				(""),  //Crea ordine - fornitore di default per tipo A  ()
	GA_CREA_ORD_FORN_TIPO_V				(""),  //Crea ordine - fornitore di default per tipo V  ()
	GA_CREA_ORD_FORN_TIPO_L				(""),  //Crea ordine - fornitore di default per tipo L  ()
	GA_CREA_ORD_FORN_TIPO_D				(""),  //Crea ordine - fornitore di default per tipo D  ()
	GA_CREA_ORD_FORN_TIPO_C				(""),  //Crea ordine - fornitore di default per tipo C  ()
	GA_CREA_ORD_FORN_TIPO_R				(""),  //Crea ordine - fornitore di default per tipo R  ()

	GA_CREA_ORD_ESERCIZIO				(""),  //Crea ordine - Esercizio  ()
	GA_CREA_ORD_CAPITOLO				(""),  //Crea ordine - Capitolo  ()
	GA_CREA_ORD_SEZIONE					(""),  //Crea ordine - Sezione  ()


	//======================================
	// AREA SERVIZI
	//======================================

	SER_FOLDER_EROGAZIONE				("S"),					//folder iniziale sulla mappa erogazione

	SER_RIC_MOV_SVOLGIMENTO				("L"),					//folder Richieste/Movimenti - Tipologia servizio
	SER_RIC_MOV_STATO_MOV				(""),					//folder Richieste/Movimenti - Stato movimento
	SER_RIC_MOV_STATO_RICH				(""),					//folder Richieste/Movimenti - Stato richiesta
	SER_RIC_MOV_TIPO_SERV				(""),					//folder Richieste/Movimenti - Tipo servizio
	SER_RIC_MOV_MOD_EROG				(""),					//folder Richieste/Movimenti - mod. erogazione
	SER_RIC_MOV_ATTIVITA				(""),					//folder Richieste/Movimenti - attività
	SER_RIC_MOV_ORD_LISTA				("01"),					//folder Richieste/Movimenti - Ordinamento lista
	SER_RIC_MOV_ELEM_BLOCCHI			("15"),					//folder Richieste/Movimenti - elementi per blocco
	SER_RIC_MOV_SEZIONE_COLL			(""),					//folder Richieste/Movimenti - sezione di collocazione

	SER_RIC_ORD_LISTA_PRENOTAZIONI		("11"),					//Ordinamento lista Prenotazioni
	SER_RIC_ORD_LISTA_GIACENZE			("17"),					//Ordinamento lista Giacenze
	SER_RIC_ORD_LISTA_PROROGHE			("19"),					//Ordinamento lista Proroghe
	SER_RIC_ORD_LISTA_SOLLECITI			("23"),					//Ordinamento lista Solleciti

	SER_RIC_UTE_ELEM_BLOCCHI			("15"),					//ricerca utenti - elementi per blocco
	SER_RIC_UTE_ORD_LISTA				("1"),					//ricerca utenti - ordinamento

	SER_INS_UTE_NAZIONE					(""),					//creazione utente - nazionalità
	SER_INS_UTE_TIPO_AUT				(""),					//creazione utente - tipo autorizzazione

	SER_RIC_ILL_FOLDER					("R"),					//folder default ricerca richieste ill

	//======================================

	FORMATO_STAMPA													("PDF"),
	//Acquisizioni
	ACQ_TIPO_ORDINE													("R"),
	ACQ_CAPITOLO													("1"),
	ACQ_PAESE														("IT"),

	TIPO_NUMERO_STANDARD											("B"),
	TITOLO_NATURA1													("A"),
	TITOLO_NATURA2													("D"),
	TITOLO_NATURA3													("P"),
	TITOLO_NATURA4													("W"),
	LIVELLO_RICERCA_INDICE											("FALSE"),
	LIVELLO_RICERCA_POLO											("FALSE"),
	RICERCA_TUTTI_NOMI												("TRUE"),
	RICERCA_NOME_PERSONALE											("TRUE"),
	RICERCA_NOME_COLLETTIVO											("FALSE"),
	CODICE_SOGGETTARIO												("FIR"),
	NATURA_CREA_TIT													("D"),


	//Periodici
	PER_KARDEX_ELEM_BLOCCHI											("10");

	public enum EditorType {
		BIB,
		USER;
	}

	private final String _default;
	private final EditorType[] _editor;

	public String getDefault() {
		return _default;
	}

	public int getDefaultAsNumber() {
		try {
			return Integer.valueOf(_default);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private ConstantDefault(String _default) {
		this._default = _default;
		this._editor = new EditorType[] { EditorType.BIB, EditorType.USER };;
	}

	private ConstantDefault(String _default, EditorType... _editor) {
		this._default = _default;
		this._editor = _editor;
	}

	public EditorType[] getEditor() {
		return _editor;
	}
}
