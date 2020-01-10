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
/*
* Marc4cppCollocazione.cpp
 *
 *  Created on: 3-gen-2009
 *      Author: Arge
 */

#include "Marc4cppCollocazione.h"
#include "BinarySearch.h"
#include "../include/library/CTokenizer.h"
#include "TbfBibliotecaInPolo.h"
#include "Tb950Inv.h"
#include "../include/library/CString.h"
#include "library/CMisc.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"

//#define DUMP

extern CKeyValueVector *codiciNotaKV;

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(const OrsChar *Module, OrsInt Line,		const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line,		const OrsChar * MsgFmt, ...);

char *padding[] = { // const
		//	"                       ",
		"                      ",
		"                     ",
		"                    ",
		"                   ",
		"                  ",
		"                 ",
		"                ",
		"               ",
		"              ",
		"             ",
		"            ",
		"           ",
		"          ",
		"         ",
		"        ",
		"       ",
		"      ",
		"     ",
		"    ",
		"   ",
		"  ",
		" ",
		"" };
int paddingSize[] = {
        22,
        21,
        20,
        19,
        18,
        17,
        16,
        15,
        14,
        13,
        12,
        11,
        10,
        9,
        8,
        7,
        6,
        5,
        4,
        3,
        2,
        1,
        0};


Marc4cppCollocazione::Marc4cppCollocazione(
		MarcRecord *marcRecord,	Marc4cppDocumento *marc4cppDocumento,
		Marc4cppLegami *marc4cppLegami,
//		CKeyValueVector *tbfBibliotecaKV,
		CKeyValueVector *tbcSezioneCollocazioneQuadreKV, TbTitolo *tbTitolo,
		Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, Tb950Ese *tb950Ese,
		TbfBiblioteca *tbfBiblioteca,
		TbcNotaInv *tbcNotaInv,
		TbcPossessoreProvenienza *tbcPossessoreProvenienza,
		TrcPossProvInventari *trcPossProvInventari,
		TbcSezioneCollocazione *tbcSezioneCollocazione,

		CKeyValueVector *sezioniDiCollocazioneDaNonMostrareIn960KV,
		CKeyValueVector *bibliotecheDaNonMostrareIn950KV
		) {

	this->marcRecord = marcRecord;
	this->marc4cppDocumento = marc4cppDocumento;
	this->marc4cppLegami = marc4cppLegami;
	//this->tbfBibliotecaKV = tbfBibliotecaKV;
	this->tbcSezioneCollocazioneQuadreKV = tbcSezioneCollocazioneQuadreKV, this->tbTitolo = tbTitolo;
	this->tb950Inv = tb950Inv;
	this->tb950Coll = tb950Coll;
	this->tb950Ese = tb950Ese;
	this->tbfBiblioteca = tbfBiblioteca;
	this->tbcNotaInv = tbcNotaInv;
	this->sezioniDiCollocazioneDaNonMostrareIn960KV = sezioniDiCollocazioneDaNonMostrareIn960KV;
	this->bibliotecheDaNonMostrareIn950KV = bibliotecheDaNonMostrareIn950KV;
	this->tbcPossessoreProvenienza = tbcPossessoreProvenienza;
	this->tbcSezioneCollocazione = tbcSezioneCollocazione;
	this->trcPossProvInventari = trcPossProvInventari;

	init();
}

bool Marc4cppCollocazione::init() {
	bibliotecheKV = new CKeyValueVector(tKVSTRING, tKVVOID); // tKVVOID cast to Biblio950

	// 03/05/13 Vamnno caricati a livello globale dinamicamente in quanto ogni polo gestisce i suoi codici e descrizione

	// da tb_codiici CNOT
// Gestionme dinamica (bug ,mantis 5302)
//	codiciNotaKV = new CKeyValueVector(tKVSTRING, tKVSTRING);
//    codiciNotaKV->Add("AA", "Copia autografa");
//    codiciNotaKV->Add("TA", "Dedica dell'autore");
//    codiciNotaKV->Add("TP", "Testo con postille");

//    // Richiesta CFI
//    sezioniDiCollocazioneDaNonMostrareKV->Add("P.GIO", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("P.PER", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("P.PU ", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("P.RIV", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("V.ACC", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("V.BAN", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("V.COL", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("V.CON", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("V.GR ", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("V.MIS", "");
//    sezioniDiCollocazioneDaNonMostrareKV->Add("V.MNL", "");
//

	curCdBib = "";
	doingTag = -1;
	return true;
}

Marc4cppCollocazione::~Marc4cppCollocazione() {
	stop();
} // End ~Marc4cppCollocazione

void Marc4cppCollocazione::stop() {
	clearBiblio950Vect(); // // Cancella i valori assegnati a void *
	delete bibliotecheKV;

	//delete codiciNotaKV;

//	delete sezioniDiCollocazioneDaNonMostrareKV;

}

// Cancella i valori assegnati a void *
void Marc4cppCollocazione::clearBiblio950Vect() {
	// Cancella i valori assegnati a void *
	for (int i = 0; i < bibliotecheKV->Length(); i++) {
		Biblio950 *biblio950 = (Biblio950 *) bibliotecheKV->GetValue(i);
		//printf ("\nCancella %s", biblio950->getDescBiblioteca());
		delete biblio950;
	}

	bibliotecheKV->Clear();
} // End clearBiblio950Vect


void Marc4cppCollocazione::creaTags950Collocazione(Collocazione950 *collocazione950, DataField *df) {
	Subfield *sf;
	char *codsez, *collocazione, *specificazione, *key_loc_coll;
	CString *conscollSPtr;
	Tb950Coll *tb950CollLoc;

	tb950CollLoc = collocazione950->getTb950Coll();
	//tb950Coll->stripRecordFieldsTrailing();

	conscollSPtr = tb950CollLoc->getFieldString(tb950CollLoc->tbcol_consis);
	codsez = tb950CollLoc->getField(tb950CollLoc->tbcol_cd_sez);
	collocazione = tb950CollLoc->getField(tb950CollLoc->tbcol_cd_loc);
	specificazione = tb950CollLoc->getField(tb950CollLoc->tbcol_spec_loc);

	key_loc_coll = tb950CollLoc->getField(tb950CollLoc->tbcol_key_loc);

	// Costruiamo il sottocampo della collocazione
//	if (*conscoll != '$') // 29/01/2010 10.49
	if (!conscollSPtr->IsEmpty() && conscollSPtr->GetFirstChar() != '$') // 04/02/2011 Mail Rossana
	{
	sf = new Subfield('c'); // consistenza di collocazione
	CString *sPtr =  tb950CollLoc->getFieldString(tb950CollLoc->tbcol_consis);
	sPtr->Strip(sPtr->trailing, ' ');  // Mail calogiuri 17/07/2017
	sf->setData(sPtr);
	df->addSubfield(sf);
	}
//	else
//	{
//		printf ("\nEmpty conscoll");
//	}

	sf = new Subfield('d'); // dati di collocazione




	// 14/01/2011 13.22 Ripristinare come prima del 22/12/2010 perche' NAP funziona,
	//  ma tutti gli altri Poli NO.
	// Con Rossana abbiamo visto che le specifiche erano errate.

		CString s = tb950CollLoc->getField(tb950CollLoc->cd_biblioteca_sezione);	//	3chr
		s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_sez));	// 10 chr
		s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_loc));	// 24 chr
		int len = strlen(collocazione);
		if (len < 24)
			s.AppendString(padding[len], paddingSize[len]);
		s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_spec_loc)); // spec_loc 12 chr
		s.Strip(s.trailing, ' ');
		if (!tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_edizione)->IsEmpty())
			s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_edizione)); // 2 chr
		if (!tb950CollLoc->getFieldString(tb950CollLoc->tbcol_classe)->IsEmpty()) {
			s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_classe)); // 31 chr
			if (s.GetLastChar() == '\n')
				s.ExtractLastChar();
		}

	sf->setData(&s);
	df->addSubfield(sf);


	// Per ogni collocazione cicliamo sui suoi inventari
	creaTags950Inventari(collocazione950->getTb950InvVect(), df, tb950CollLoc);


//	if (marc4cppDocumento->isAntico())
//		creaTag316_NotaAllaCopiaPosseduta(df);
} // end creaTags950Collocazione


























/*


bool Marc4cppCollocazione::creaTag960InventariNonCollocati(char *cdBib,
		Biblio950 *biblio950) {
	//Tb950Inv * tb950Inv;
	DataField *df;
	Subfield *sf;
	CString inventario;

	char indicatore_2 = livelloGerarchico;
	df = new DataField();
	df->setTag("960");
	df->setIndicator1(' ');
	df->setIndicator2(indicatore_2);

	sf = new Subfield('a'); // descrizione della biblioteca (non ripetibile)
	CString biblio = biblio950->getDescBiblioteca();
	biblio.Strip(biblio.trailing, ' ');
	sf->setData(&biblio);
	df->addSubfield(sf);

	// Gestione inventari non collocati
	// ================================
	ATTValVector<Tb950Inv *> *tb950InvNonCollocatiVect = biblio950->getTb950InvNonCollocatiVect();

	creaTags950Inventari(tb950InvNonCollocatiVect, df, 0);

//printf ("\nDF_NON_coll=%s", df->toString());

	marcRecord->addDataField(df);
	return true;
} // End creaTag960InventariNonCollocati

*/












/*
 *
 VECCHIA
	 cd_biblioteca       0-1
	 cd_serie            2-4
	 cd_inv              5-13
	 seq_coll            14-43
	 cod_no_disp         44-45
	 cd_frui             46-47
	 cd_supporto         48-49
	 stato_con           50-51
	 cd_riproducibilit�  52-53
	 sez_old             54-63
	 loc_old             64-87
	 spec_old            88-99
	 precis_inv          100-fine campo


25/11/2009 14.37
	cd_biblioteca         0-1
	cd_serie              2-4
	cd_inv                5-13
	seq_coll              14-43
	cod_no_disp           44-45
	cd_frui               46-47
	cd_supporto           48-49
	stato_con             50-51
	cd_riproducibilit�    52-53
	precis_inv            54-fine campo
 */
void Marc4cppCollocazione::costruisciInventarioE960(Tb950Inv * tb950InvPtr,	CString &inventario, DataField *df950) {
	CString s;
	inventario.Clear();

//	char * cd_bib = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_bib)+1;
//	char * cd_serie = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_serie);
	char * cd_inven = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_inven);
	char * seq_coll = tb950InvPtr->getField(tb950InvPtr->tbinv_seq_coll);
	char * cd_no_disp = tb950InvPtr->getField(tb950InvPtr->tbinv_cod_no_disp);
	char * cd_frui = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_frui);
	//char * cd_supporto = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_supporto);
	char * cd_mat_inv = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_mat_inv);
	char * stato_con = tb950InvPtr->getField(tb950InvPtr->tbinv_stato_con);
	char * cd_riproducibilita = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_riproducibilita);
	char * precis_inv = tb950InvPtr->getField(tb950InvPtr->tbinv_precis_inv);


	inventario.AppendString(tb950InvPtr->getField(tb950InvPtr->tbinv_cd_bib)+1, 2);  // 0-1
	inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_serie)); // 2-4

	s = cd_inven;
	s.leftPadding('0', 9);
	inventario.AppendString(&s); 	// 5-13

	s = seq_coll; 											// 14-43
	s.rightPadding(' ', 30);
	inventario.AppendString(&s);

//	if (*cd_no_disp && strncmp(cd_no_disp, "null", 4))		// Pos 44-45
	if (*cd_no_disp)		// Pos 44-45
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cod_no_disp));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale

//	if (*cd_frui && strncmp(cd_frui, "null", 4))			// Pos 46-47
	if (*cd_frui)			// Pos 46-47
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_frui));
	else
		inventario.AppendString((char *)"  "), 2; // Campo va riempito in quanto posizionale

//	if (*cd_supporto && strncmp(cd_supporto, "null", 4))	// Pos 48-49
//	if (*cd_supporto)	// Pos 48-49
//		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_supporto));
	if (*cd_mat_inv)	// Pos 48-49
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_mat_inv));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale

//	if (*stato_con && strncmp(stato_con, "null", 4))		// Pos 50-51
	if (*stato_con)		// Pos 50-51
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_stato_con));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale

//	if (*cd_riproducibilita && strncmp(cd_riproducibilita, "null", 4))	// Pos 52-53
	if (*cd_riproducibilita)	// Pos 52-53
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_riproducibilita));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale


//	if (*precis_inv && strncmp(precis_inv, "null", 4))		// Pos 54
	if (*precis_inv && *precis_inv != '$')		// Pos 54
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_precis_inv));
	else
		inventario.AppendChar(' '); // Campo va riempito in quanto posizionale


	if ((marc4cppDocumento->isAnticoModernoUndefined() == 1) && df950)
		creaTag316_NotaAllaCopiaPosseduta(tb950InvPtr, df950);
} // End Marc4cppCollocazione::costruisciInventarioE960






/*
 * Aggiunge una biblioteca all'elenco di tutte le biblioteche univoche della 950 con la descrizione
 */
bool Marc4cppCollocazione::addBiblioteca(char *cdBib) {
	//char *cdBib;
	char* dsBibliotecaPtr;
	// Trova descrizione biblioteca

//	TbfBibliotecaInPolo *tbfBibliotecaInPolo = (TbfBibliotecaInPolo*) tbfBibliotecaKV->GetValueFromKey(cdBiblioteca);
//	if (!tbfBibliotecaInPolo) {
//		//	    SignalAWarning(__FILE__, __LINE__, "Descrizione biblioteca non trovata per '%s'", cdBiblioteca);
//		SignalAnError(	__FILE__, __LINE__,	"Biblioetca mancante per biblioteca '%s', bid '%s' e key_loc %s", cdBiblioteca, tb950Inv->getField(tb950Inv->bid),	tb950Inv->getField(tb950Inv->tbinv_key_loc));
//		descrizioneBib = cdBiblioteca;
//	} else
//		descrizioneBib = tbfBibliotecaInPolo->getField(	tbfBibliotecaInPolo->ds_biblioteca);


	//CString dsBiblioteca;
    CString key = marc4cppDocumento->getPolo(); // prendi il polo
    key.AppendString(cdBib); // prendi la biblioteca

    if (tbfBiblioteca->loadRecord(key.data()))
        {
    	dsBibliotecaPtr = tbfBiblioteca->getField(tbfBiblioteca->nom_biblioteca);
        }
    else
    {
		SignalAnError(	__FILE__, __LINE__,	"Biblioetca mancante per biblioteca '%s', bid '%s' e key_loc %s", cdBib, tb950Inv->getField(tb950Inv->bid),	tb950Inv->getField(tb950Inv->tbinv_key_loc));
		dsBibliotecaPtr = cdBib;
    }


	curBiblio950 = new Biblio950(cdBib, dsBibliotecaPtr);
	bibliotecheKV->Add(cdBib, curBiblio950);
	curCdBib = cdBib;
	return true;
} // End addBiblioteca











void Marc4cppCollocazione::costruisciInventarioE(Tb950Inv * tb950InvPtr, Tb950Coll *tb950CollPtr, CString &inventario, DataField *df) {
	CString numinventario, s, *sPtr;
	char * cd_bib =  tb950InvPtr->getField(tb950InvPtr->tbinv_cd_bib); // const
//	char * cd_serie = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_serie); // const

	if (IS_TAG_TO_GENERATE(317))
	{
		// Gestione Possessori provenienza 27/01/2010 11.38
		numinventario = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_inven);
		numinventario.leftPadding('0', 10);
		CString keyInvetarioPossProv = (char*)cd_bib;
		keyInvetarioPossProv.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_serie));
		keyInvetarioPossProv.AppendString(&numinventario);

//			if (trcPossProvInventari->existsRecord(keyInvetarioPossProv.data()))
		if (trcPossProvInventari->existsRecordNonUnique(keyInvetarioPossProv.data()))
			creaPossessore(tb950InvPtr, tb950CollPtr, keyInvetarioPossProv.data());
	}


	if (doingTag == DOING_TAG_960)
		return costruisciInventarioE960(tb950InvPtr, inventario, df);
	// 30/03/2010
	// Dobbiamo costruire anche la 956 per il riferimento al digitale?

//tb950InvPtr->dumpRecord();
/** 18/12/2019 creata apposito metodo indipendente per 956
~~
	s = tb950InvPtr->getField(tb950InvPtr->bid);
	sPtr = tb950InvPtr->getFieldString(tb950InvPtr->tbinv_id_accesso_remoto);
	if (sPtr->IsEmpty())
		sPtr = tb950InvPtr->getFieldString(tb950InvPtr->tbinv_rif_teca_digitale);
	if (!sPtr->IsEmpty())
	{
		addFields956(tb950InvPtr, tb950CollPtr);
	}
**/



	char * cd_sit = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_sit); // 24/09/2009 11.33
	char * cd_no_disp = tb950InvPtr->getField(tb950InvPtr->tbinv_cod_no_disp);
	//char * cd_supporto = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_supporto);
	char * cd_mat_inv = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_mat_inv);
	char * stato_con = tb950InvPtr->getField(tb950InvPtr->tbinv_stato_con);
	char * cd_riproducibilita = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_riproducibilita);
	char * sequenza = tb950InvPtr->getField(tb950InvPtr->tbinv_seq_coll);
	char * precisinv = tb950InvPtr->getField(tb950InvPtr->tbinv_precis_inv);

	numinventario = tb950InvPtr->getField(tb950InvPtr->tbinv_cd_inven);
	numinventario.leftPadding('0', 9);

	//	CMisc::formatDate1(tb950InvPtr->getField(tb950InvPtr->tbinv_data_ingresso), dateBuf);

	inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_bib)); // 0-2
	inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_serie));	// 3-5
	inventario.AppendString(&numinventario);	// 6-14

	if (*cd_sit == '2') // 24/09/2009 11.34			// 15
		inventario.AppendChar('5');
	else
		inventario.AppendChar('2');


//	if (*cd_no_disp && strncmp(cd_no_disp, "null", 4))
	if (*cd_no_disp )								// 16-17
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cod_no_disp));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale

//	if (*cd_supporto && strncmp(cd_supporto, "null", 4))
//	if (*cd_supporto)								// 18-19
//	inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_supporto));
	if (*cd_mat_inv) // Mantis 5037 collaudo
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_mat_inv));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale

//	if (*stato_con && strncmp(stato_con, "null", 4))
	if (*stato_con)									// 20-21
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_stato_con));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale

//	if (*cd_riproducibilita && strncmp(cd_riproducibilita, "null", 4))
	if (*cd_riproducibilita)						// 22-23
		inventario.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_riproducibilita));
	else
		inventario.AppendString((char *)"  ", 2); // Campo va riempito in quanto posizionale

	  s = sequenza;                       // 24-43	Fix 08/02/2010 14.16
	  s.rightPadding(' ', 20);
	  inventario.AppendString(&s);

//	if (*sequenza)									// 24-43
//		inventario.AppendString(sequenza);
//	else
//		inventario.AppendString("                    "); // Campo va riempito in quanto posizionale

	if (*precisinv != '$')
	{
		CString * sPtr = tb950InvPtr->getFieldString(tb950InvPtr->tbinv_precis_inv);
		// 19/07/2017 mail Calogiuri del 17/07/2017
		sPtr->Strip(inventario.trailing);
		inventario.AppendString(sPtr);
	}

	if ((marc4cppDocumento->isAnticoModernoUndefined() == 1) && df)
		if (IS_TAG_TO_GENERATE(316))
			creaTag316_NotaAllaCopiaPosseduta(tb950InvPtr, df); // , precisinv
} // End Marc4cppCollocazione::costruisciInventarioE



bool Marc4cppCollocazione::addFields956(Tb950Inv * tb950Inv, Tb950Coll *tb950CollPtr)
{
	if (!IS_TAG_TO_GENERATE(956))
		return false;


	Subfield *sf;
	CString inventario;
	if (!df956)
	{
		df956 = new DataField(); // Creazione della 956 solo quando esiste in quancto ne esistono poche
		df956->setTag((char *)"956");
		sf = new Subfield('a');
		df956->addSubfield(sf);
	}

	sf = new Subfield('b'); // digitalizzazione
	sf->setData(tb950Inv->getFieldString(tb950Inv->tbinv_digitalizzazione));
	df956->addSubfield(sf);

	sf = new Subfield('c'); // riferimento teca digitale
	sf->setData(tb950Inv->getFieldString(tb950Inv->tbinv_rif_teca_digitale));
	df956->addSubfield(sf);

	sf = new Subfield('d'); // disponibilita' copia digitale
	sf->setData(tb950Inv->getFieldString(tb950Inv->tbinv_disp_copia_digitale));
	df956->addSubfield(sf);

	sf = new Subfield('e'); // inventario
	inventario  = tb950Inv->getField(tb950Inv->tbinv_cd_bib);
	inventario.ExtractFirstChar(); // Macke cd bib 2 char long
	inventario.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_cd_serie));

	//	inventario.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_cd_inven));
	CString cdInven = tb950Inv->getField(tb950Inv->tbinv_cd_inven); // 24/05/2011 Richiesta Giangregorio/Calogiuri
	cdInven.leftPadding('0', 9);
	inventario.AppendString(cdInven.data());

	sf->setData(&inventario);
	df956->addSubfield(sf);

	sf = new Subfield('u'); // id accesso remoto
	CString *sPtr = tb950Inv->getFieldString(tb950Inv->tbinv_id_accesso_remoto);
	sPtr->Strip(CString::trailing, ' ');
	sf->setData(sPtr);
	df956->addSubfield(sf);

	// 07/02/2012. Aggiungere la collocazione dell/inventario per gestione linik multimediale (accesso remoto)
	// Richiesta mail Calogiuri, Giangregorio del 03/02/2012
	// Collocazione in $z. Eqiivalente $d in 956
	// TODO $z

	if (DATABASE_ID == DATABASE_SBNWEB && tb950CollPtr)
	{
		char *collocazione = tb950CollPtr->getField(tb950CollPtr->tbcol_cd_loc);

		sf = new Subfield('z'); // dati di collocazione

			CString s = tb950CollPtr->getField(tb950CollPtr->cd_biblioteca_sezione);	//	3chr
			s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_cd_sez));	// 10 chr
			s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_cd_loc));	// 24 chr
			int len = strlen(collocazione);
			if (len < 24)
				s.AppendString(padding[len], paddingSize[len]);
			s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_spec_loc)); // spec_loc 12 chr
			s.Strip(s.trailing, ' ');
			if (!tb950CollPtr->getFieldString(tb950CollPtr->tbcol_cd_edizione)->IsEmpty())
				s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_cd_edizione)); // 2 chr
			if (!tb950CollPtr->getFieldString(tb950CollPtr->tbcol_classe)->IsEmpty()) {
				s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_classe)); // 31 chr
				if (s.GetLastChar() == '\n')
					s.ExtractLastChar();
			}
		sf->setData(&s);
		df956->addSubfield(sf);
	}






	return true;
} // End addFields956

/*
 I tipi di collocazioni previsti in SBNWeb sono 8: di seguito la lista (come da tabella codici Tipo di collocazione  (CTCO)):

M       esplicita non strutturata
T       esplicita strutturata
N       magazzino non a formato
Z       sistema di classificazione

A       chiave titolo
C       continuazione
F       magazzino formato
*/
void Marc4cppCollocazione::crea960CollocazioneNormalizzata(CString &s, Tb950Inv * tb950Inv, Tb950Coll *tb950Coll) // , CString *sPtr
{
	CString sTmp, *sPtr;
	//char *ptr;
	s.Clear(); // 23/06/2010 entrava sporco a volte

	// Verifichiamo se il codice sezione e' da presentare

	sTmp.AppendString(tb950Coll->getFieldString(tb950Coll->tbcol_cd_sez));
	sTmp.Strip(sTmp.trailing, ' ');
	sTmp.Strip(sTmp.trailing, '.');

//tb950Inv->dumpRecord();
//tb950Coll->dumpRecord();

	CString key = tb950Coll->getField(tb950Coll->cd_biblioteca_sezione);
	key.AppendString(tb950Coll->getFieldString(tb950Coll->tbcol_cd_sez));
	if (tbcSezioneCollocazioneQuadreKV->GetValueFromSortedKey(key.data())) {
		sTmp.PrependChar('[');
		sTmp.AppendChar(']');
	}

	// Determiniamo se e' una sezione a formato od esplicita
	key = tb950Coll->getField(tb950Coll->tbcol_cd_sez);
	key.leftPadding('0', 10);

	key.AppendString(tb950Coll->getFieldString(tb950Coll->cd_biblioteca_sezione));
	sPtr = marc4cppDocumento->getPoloString();
	key.AppendString(sPtr);
	bool retb = tbcSezioneCollocazione->loadRecord(key.data());
//tbcSezioneCollocazione->dumpRecord();
	bool sezioneEsplicita = false;
	if (retb)
	{
		char cd_colloc = *tbcSezioneCollocazione->getField(tbcSezioneCollocazione->cd_colloc);
		if (cd_colloc == 'M' || cd_colloc == 'N' || cd_colloc == 'T' || cd_colloc == 'Z')
			sezioneEsplicita = true;
		else
			sezioneEsplicita = false;
//printf ("\ncd_colloc=%c sezioneEsplicita=%d", cd_colloc, sezioneEsplicita);
	}


//	else
//	{
//		printf ("ERRORE crea960CollocazioneNormalizzata: sezione '%s' non trovata", key.data());
//	}

	// Prendi la sezione
	if (marc4cppDocumento->getPoloId() == POLO_CFI)
	{
		CString sTmp1 = tb950Coll->getField(tb950Coll->tbcol_cd_sez);
		sTmp1.Strip(sTmp1.trailing, ' ');
		if (!sezioniDiCollocazioneDaNonMostrareIn960KV->existsKey(sTmp1.data()))
			s.AppendString(&sTmp1); // Mettiamo sezione solo se NON presente il lista
	}
	else
	{
		s.AppendString(&sTmp);
	}


	sTmp.Clear();
	sTmp = tb950Coll->getField(tb950Coll->tbcol_cd_loc);

//	s.AppendString("[ARGE tbcol_cd_loc = '");
//	s.AppendString(sTmp.data());
//	s.AppendString("'ARGE]");
	if (sezioneEsplicita)
		sTmp.Strip(sTmp.trailing, ' '); // remove trailing spaces
	else
		sTmp.removeCharacterOccurances(' '); // Remove also spaces in between



	bool hasFinalZero = sTmp.GetLastChar() == '0' ? true : false;
	if (sTmp.IsEmpty() && hasFinalZero) {
		sTmp.AppendChar('0'); // se il numero era 0 (con uno o + digiti) lasciare almeno uno zero finale
	}

	if (sTmp.Length()) {
		if (sezioneEsplicita)
		{
//			if (s.Length() && s.GetLastChar() != '.')
			// 16/3 Richiesta CFI. Giliberto dice che se non c'e' la sezione di collocazione non va neanche il punto.
			//      loro sanno conre identificare la collocazione
			if (s.Length() && s.GetLastChar() != '.')
				s.AppendChar('.');
		}
		else
			s.AppendChar(' ');

		s.AppendString(&sTmp);
	}


	sTmp = tb950Coll->getField(tb950Coll->tbcol_spec_loc);
	sTmp.Strip(sTmp.trailing, ' ');

	hasFinalZero = sTmp.GetLastChar() == '0' ? true : false;

	if (sezioneEsplicita)
	{
		sTmp.Strip(sTmp.leading, '0');
		if (sTmp.IsEmpty() && hasFinalZero)
			sTmp.AppendChar('0'); // se il numero era 0 (con uno o + digiti) lasciare almeno uno zero finale
	}
	else
		sTmp.leftPadding('0', 5);

	if (!sTmp.IsEmpty()) {
		if (s.Length() && s.GetLastChar() != '.')
		{
			if (sezioneEsplicita)
				s.AppendChar('.');
			else
				s.AppendChar(' ');
		}
		s.AppendString(&sTmp);
	}

	if (sezioneEsplicita)
	{
		if (!tb950Coll->getFieldString(tb950Coll->tbcol_classe)->IsEmpty()) {
			sTmp = tb950Coll->getField(tb950Coll->tbcol_classe);
			sTmp.Strip(sTmp.trailing, ' ');

			if (!sTmp.IsEmpty()) {
				if (s.GetLastChar() != '.')
				{
					if (sezioneEsplicita)
						s.AppendChar('.');
					else
						s.AppendChar(' ');
				}
				s.AppendString(&sTmp);
			}
			//			s.AppendString(sPtr->data());
		}
	}

	// 09/09/2010 10.53 Segnalazione mail di Giliberto del 08/09/10
	// Rimosso test su sezioneEsplicita. Quindi riporto la seq_coll anche se sezione non esplicita
	sTmp = tb950Inv->getField(tb950Inv->tbinv_seq_coll);
	sTmp.Strip(sTmp.trailing, ' ');

	sTmp.Strip(sTmp.leading, '0');
	if (!sTmp.IsEmpty()) {
		if (s.GetLastChar() != '.')
		{
			if (sezioneEsplicita)
				s.AppendChar('.');
			else
				s.AppendChar(' ');
		}
	s.AppendString(&sTmp);
	}




/*
12/05/10 - Richiesta Pasqualetti del 06/05/10
	sTmp = tb950Inv->getField(tb950Inv->tbinv_sez_old);
	sTmp.Strip(sTmp.trailing, ' ');
	if (!sTmp.IsEmpty()) { //  && sTmp.Compare("null")
		if (s.GetLastChar() != '.')
			s.AppendChar('.');
		s.AppendString(sTmp.data()); // sez_old
	}

	sTmp = tb950Inv->getField(tb950Inv->tbinv_loc_old);
	sTmp.Strip(sTmp.trailing, ' ');
	if (!sTmp.IsEmpty()) { //  && sTmp.Compare("null")
		if (s.GetLastChar() != '.')
			s.AppendChar('.');
		s.AppendString(sTmp.data()); // loc_old
	}
	sTmp = tb950Inv->getField(tb950Inv->tbinv_spec_old);
	sTmp.Strip(sTmp.trailing, ' ');
	if (!sTmp.IsEmpty()) { //  && sTmp.Compare("null")
		if (s.GetLastChar() != '.')
			s.AppendChar('.');
		s.AppendString(sTmp.data()); // spec_old
	}
*/
} // End crea960CollocazioneNormalizzata











/*!
\brief <b>Tag 316 - Nota alla copia posseduta</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>316</td></tr>
<tr><td valign=top>Descrizione</td><td>Nota alla copia posseduta</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Precis di inv. Non ripetibile.
        <li>d - Collocazione. Non ripetibile.
        <li>5 - Istituzione della copia posseduta. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppCollocazione::creaTag316_NotaAllaCopiaPosseduta(Tb950Inv * tb950InvPtr, DataField *df950)
	{
	DataField *df;
	Subfield *sf; // , *sfDf950

	CString *precisDiInv = tb950InvPtr->getFieldString(tb950InvPtr->tbinv_precis_inv);
	if (precisDiInv->IsEmpty() || precisDiInv->StartsWith("$"))
		return;

	df = new DataField((char *)"316", 3);

	//sfDf950 = df950->getSubfield('c');
	sf = new Subfield('a');
	sf->setData(precisDiInv);
	df->addSubfield(sf);

	CString s;
	sf = df950->getSubfield('d');
	char *ptr = 0;
	if (sf)
		ptr = sf->getData(); // Collocazione

	// Troviamo il paese e codice anagrafe a partire dal codice polo/biblioteca
	CString key = marc4cppDocumento->getPolo(); // prendi il polo
	key.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_cd_bib)); // prendi la biblioteca
	if (tbfBiblioteca->loadRecord(key.data()))
		{
		s = tbfBiblioteca->getField(tbfBiblioteca->paese);
		s.AppendChar('-');
		s.AppendString(tbfBiblioteca->getFieldString(tbfBiblioteca->cd_ana_biblioteca));
		}
	else
		s = "???-??????";
	s.AppendString((char *)", ", 2);
	if (ptr)
		s.AppendString(ptr + 3, sf->getDataLength()-3); // skip biblioteca
	sf = new Subfield('5'); // identificazione della copia (Intitution to which the field applies da Unimarc)
	s.Strip(s.trailing, ' ');
	sf->setData(&s);
	df->addSubfield(sf);
	marcRecord->addDataField(df);
	} // end creaTag316_NotaAllaCopiaPosseduta





/*!
\brief <b>Tag 317 - Nota di provenienza</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>317</td></tr>
<tr><td valign=top>Descrizione</td><td>Nota di provenienza</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
        <li>5 - Istituzione della copia posseduta. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
bool Marc4cppCollocazione::creaPossessore(Tb950Inv * tb950InvPtr, Tb950Coll *tb950CollPtr, char *keyInvetarioPossProv)
	{
	CString s, stmp;
	Subfield *sf, *sf5;
	DataField *df;
	char cd_legame;
	CString cdRelazione;


	// Creiamo le etichette 317 - NOTE DI PROVENIENZA
	// -----------------------------------------------
	while (trcPossProvInventari->loadNextRecord(keyInvetarioPossProv))
		{
			if (!tbcPossessoreProvenienza->loadRecord(trcPossProvInventari->getFieldString(trcPossProvInventari->pid)->data()))
			{
				continue; // 29/05/2019. Mantis 0006974 Per evitare squadrature della basedati
			}



		cd_legame = *trcPossProvInventari->getField(trcPossProvInventari->cd_legame);

		df  = new DataField((char *)"317", INDICATOR_UNDEFINED, INDICATOR_UNDEFINED);

		// $a testo della nota. Non repetibile.
		// ------------------------------------
		//	Il testo della nota viene costruito come di seguito descritto:
		// 		- testo �Provenienza: � o �Possessore: � (coerentemente al tipo di relazione tra possessore e inventario registrato nel campo trc_poss_prov_inventari.cd_legame�),seguito dal
		// 		- contenuto del campo tbc_possessore_provenienza.ds_nome_aut al netto degli spazi in testa e coda, seguito dalla
		//		- stringa �. � seguito dal
		//		- contenuto del campo trc_poss_prov_inventari.nota al netto degli spazi in testa e coda.

		if ( cd_legame == 'R')
		{
			s = "Provenienza: ";
			cdRelazione = "320";
		}
		else if (cd_legame  == 'P')
		{
			s = "Possessore: ";
			cdRelazione = "390";
		}
		else
		{
			s = "Codice legame invalido (";
			s.AppendChar(*trcPossProvInventari->getField(trcPossProvInventari->cd_legame));
			s.AppendChar(')');
		}
		// Prendiamo il PID carichiamo il record da TbcPossessoreProvenienza

// 29/05/2019
//		printf ("\npid=%s", trcPossProvInventari->getFieldString(trcPossProvInventari->pid)->data());
//
//		if (!tbcPossessoreProvenienza->loadRecord(trcPossProvInventari->getFieldString(trcPossProvInventari->pid)->data()))
//		{
//			delete df;
//			return false;
//		}



		sf = new Subfield('a');
		CString *ptr = tbcPossessoreProvenienza->getFieldString(tbcPossessoreProvenienza->ds_nome_aut);
		ptr->Strip(ptr->both, ' ');
		ptr->ChangeTo('*', ' ');

		s.AppendString(ptr);
		s.AppendString((char *)". ", 2);

		ptr = trcPossProvInventari->getFieldString(trcPossProvInventari->nota);
		if (!ptr->IsEmpty())
			ptr->Strip(ptr->both, ' ');

		s.AppendString( ptr);

		sf->setData(&s);
		df->addSubfield(sf);

		// $5 identificativo della copia cui si riferisce la nota. Non repetibile.
		// -----------------------------------------------------------------------
		// Per identificativo la copia si sceglie di utilizzare
		// la collocazione, il contenuto del sottocampo risulta quindi dall�accodamento,
		// eseguito previa eliminazione degli spazi in testa e coda dei seguenti campi:
		//  - "cd_polo_sezione",
		//  - "cd_biblioteca_sezione",
		//  - "cd_sez",
		//  - "cd_loc",
		//  - "spec_loc" (da recuperare da tbc_collocazione imponendo la condizione tbc_collocazione.key_loc= tbc_inventario.key_loc) e
		//  - "seq_coll" (da recuperare da tbc_inventario), solo se in seq_coll e' presente almeno un carattere diverso da spazio dopo "spec_loc" inserire il carattere �/�.
		//  Esempio:
		//		"cd_polo_sezione"="CFI",
		//		"cd_biblioteca_sezione"=" CF",
		//		"cd_sez"="Guicc.", 1.4/2.16
		//		"cd_loc"="1.4",
		//		"spec_loc"=" ",
		//		"seq_coll"="2.16" ?
		//	$5CFICFGuicc.1.4/2.16
		//
		// Controlliamo se abbiamo la localizzazione per questo inventario
		sf5 = 0;
		if (tb950CollPtr)
		{
			sf5 = new Subfield('5');
			s = marc4cppDocumento->getPolo(); // Non lo prendo da tabella ma da parametro. Il polo non cambia mai
		    if (marc4cppDocumento->getPoloId() != POLO_RML) // 04/03/2010 16.34
			{
			    s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->cd_biblioteca_sezione)); // prendi la biblioteca 09/03/10
			    // 25/02/2010
			    if (tbfBiblioteca->loadRecord(s.data()))
			        {
			        s = tbfBiblioteca->getField(tbfBiblioteca->paese);
			        s.AppendChar('-');
			        s.AppendString(
			        		tbfBiblioteca->getFieldString(tbfBiblioteca->cd_ana_biblioteca));
			        }
			    else
			        s = "???-??????";
			}
		    else
		    {
		    	char *ptr = tb950CollPtr->getField(tb950CollPtr->cd_biblioteca_sezione)+1;
			    s.AppendString(ptr, tb950CollPtr->getFieldLength(tb950CollPtr->cd_biblioteca_sezione)-1	); // prendi la biblioteca 09/03/10 (solo 2 caratteri)
		    }


			if (marc4cppDocumento->getPoloId() != POLO_RML) // 09/03/2010
				s.AppendChar(' '); // 25/02/2010
			s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_cd_sez));
			s.Strip(s.trailing, ' ');
			if (marc4cppDocumento->getPoloId() != POLO_RML) // 09/03/2010
				s.AppendChar(' '); // 25/02/2010

			s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_cd_loc));
			s.Strip(s.trailing, ' ');

			// controlliamo che le key_loc coincidano
			if (tb950CollPtr->getFieldString(tb950CollPtr->tbcol_key_loc)->Equals(tb950InvPtr->getField(tb950InvPtr->tbinv_key_loc)))
			{
			if (marc4cppDocumento->getPoloId() != POLO_RML) // 09/03/2010
				s.AppendChar(' '); // 25/02/2010
			s.AppendString(tb950CollPtr->getFieldString(tb950CollPtr->tbcol_spec_loc));
			s.Strip(s.trailing, ' ');
			}
			else
				SignalAnError(
						__FILE__, __LINE__,
						"KeyLoc non coincidono  tb950Coll.key_loc='%s', tb950InvPtr.key_loc='%s'",
						tb950CollPtr->getField(tb950CollPtr->tbcol_key_loc),
						tb950InvPtr->getField(tb950InvPtr->tbinv_key_loc));

			if (!tb950InvPtr->getFieldString(tb950InvPtr->tbinv_seq_coll)->IsEmpty())
			{
				s.AppendChar('/');
				s.AppendString(tb950InvPtr->getFieldString(tb950InvPtr->tbinv_seq_coll));
			}

			sf5->setData(&s);
			df->addSubfield(sf5);
		}
		marcRecord->addDataField(df);

		// Crea il tag per le relazioni inventari-possessori (702/712)
		// -----------------------------------------------------------
		// Per ogni relazione inventari-possessori va inoltre prodotto un tag autore (702 o 712)
		// utilizzando i dati descrittivi del possessore (tabella tbc_possessore_provenienza)
		// riportando in $4 (relator code) il valore corrispondente al tipo di relazione presente
		// con l�inventario (390 se possessore, 320 se provenienza) e in $5 l�identificativo della
		// copia cos� come descritto per la $5 del tag 317.

		ptr = tbcPossessoreProvenienza->getFieldString(tbcPossessoreProvenienza->ds_nome_aut);
		ptr->ChangeTo('*', ' ');

		//char *ds_nome = ptr->data();
		char tp_nome = *tbcPossessoreProvenienza->getField(tbcPossessoreProvenienza->tp_nome_pp);
		//pid = (char *)tbcPossessoreProvenienza->getFieldString(tbcPossessoreProvenienza->pid);
		if (tp_nome == 'A' || tp_nome == 'B' || tp_nome == 'C' || tp_nome == 'D')
		{
			df  = new DataField((char *)"702", INDICATOR_UNDEFINED, INDICATOR_UNDEFINED);
			marc4cppLegami->creaTag70xNew(	df, ptr, tp_nome, tbcPossessoreProvenienza->getFieldString(tbcPossessoreProvenienza->pid));

			// Codice di relazione
			Subfield *sf1 = new Subfield('4');
			sf1->setData(&cdRelazione);
			df->addSubfield(sf1);
		}
		else
		{
			df  = new DataField((char *)"712", INDICATOR_UNDEFINED, INDICATOR_UNDEFINED);
			marc4cppLegami->creaTag71xNew(df, &cdRelazione, tbcPossessoreProvenienza->getFieldString(tbcPossessoreProvenienza->ds_nome_aut), tp_nome, tbcPossessoreProvenienza->getFieldString(tbcPossessoreProvenienza->pid), 0);

		}

		if (sf5)
		{
			Subfield *sf1 = new Subfield('5');
			sf1->setData(sf5->getDataString());
			df->addSubfield(sf1);
		}

		if (df)
			marcRecord->addDataField(df);

		} // end while note all'inventario
	} // End creaPossessore




/*!
\brief <b>Tag 950 - Dati gestionali</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>950</td></tr>
<tr><td valign=top>Descrizione</td><td>Dati gestionali</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
Livello gerqrchico:
    <UL>
        <li> 0 = senza livelli (monografie senza livelli e periodici)
        <li> 1 = livello alto (monografia superiore)
        <li> 2 = livello basso (monografia inferiore o intermedia)
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - descrizione della biblioteca
        <li> b - consistenza dell'esemplare
        <li> c - consistenza della collocazione
        <li> d - dati di collocazione
        <li> e - dati di inventariazione
        <li> f - categoria di fruizione
        <li> h - data di ingresso
        <li> i - data di accessionamento
        <li> l - note all'inventario
        <li> m - ?? (dedica)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
bool Marc4cppCollocazione::creaTag950_Gestionale(char *bid) {


	//Biblio950 *curBiblio;
	Tb950Inv * tb950Inv;
	Tb950Ese * tb950Ese;
	DataField *df;
	Subfield *sf;
	char * cdBib;
	doingTag = DOING_TAG_950;

	CString inventario; // , *sPtr
	const char *consese;

	CString biblio;
	df956 = 0; // Ensure data field pointer is initialized

	// Cicliamo sulle biblioteche
	// Una 950 per biblioteca
	for (int i = 0; i < bibliotecheKV->Length(); i++) {
		cdBib = (char *) bibliotecheKV->GetKey(i);

		// Controlla se biblioteca da escludere da 950
		if (bibliotecheDaNonMostrareIn950KV->existsKey(cdBib))
			continue;

		curBiblio950 = (Biblio950 *) bibliotecheKV->GetValue(i);
//		char indicatore_2 = ; //elaboraLivelloGerarchicoNotizia(bid );

		df = new DataField();
		df->setTag((char *)"950");

		df->setIndicator1(' ');
		df->setIndicator2(livelloGerarchico);

		sf = new Subfield('a'); // descrizione della biblioteca (non ripetibile)
		biblio = curBiblio950->getDescBiblioteca();
		biblio.Strip(biblio.trailing, ' ');
		sf->setData(&biblio);
		df->addSubfield(sf);

		// Gestione inventari non collocati
		// ================================
		creaTags950Inventari(curBiblio950->getTb950InvNonCollocatiVect(), df, 0);

//df->dump();

		// Gestione collocazioni senza inventario (PRECISATO)
		// =======================================
		// Per ogni collocazione
		CKeyValueVector * kvv = curBiblio950->getCollocazione950VectKV();
		Collocazione950 * coll;
		char *keyLoc;
		CKeyValue *kv;
		for (int k = 0; k < kvv->Length(); k++) {
			kv = kvv->Get(k);
			coll = (Collocazione950 *)kv->Val.v;

			Tb950Coll *tbColl = coll->getTb950Coll();
//printf ("creaTag950_Gestionale");
//tbColl->dumpRecord();
			keyLoc = tbColl->getField(tbColl->tbcol_key_loc);
			// Controlliamo se collocazione senza inventario
			if (*keyLoc)
			{
				// Troviamo la keyloc negli inventari collocati
				ATTValVector<Tb950Inv *> *tb950InvCollocatiVect = curBiblio950->getTb950InvCollocatiVect();
				char *invKeyLoc;
				int j;
				for (j = 0; j < tb950InvCollocatiVect->Length(); j++) {
					tb950Inv = tb950InvCollocatiVect->Entry(j);
					invKeyLoc = tb950Inv->getField(tb950Inv->tbinv_key_loc);
					if (!strcmp(keyLoc, invKeyLoc))
						break;
				}
				if (j == tb950InvCollocatiVect->Length()) // Nessun invetario per questa collocazione
				{
					creaTags950Collocazione_noInv(coll, df);
//df->dump();
				}
			}
			else
			{
				creaTags950Collocazione_noInv(coll, df);
//df->dump();
			}
		} // End for collocazioni senza inventario

		// Gestione collocazioni CON inventario
		// =======================================
		for (int k = 0; k < kvv->Length(); k++) {
			kv = kvv->Get(k);
			coll = (Collocazione950 *)kv->Val.v;


			Tb950Coll *tbColl = coll->getTb950Coll();
			keyLoc = tbColl->getField(tbColl->tbcol_key_loc);

//printf ("\ncollocazioni CON inventario: bid=%s keyLoc=%s", kv->Key.p, keyLoc);


			// Controlliamo se collocazione con inventario
			if (*keyLoc)
			{
				// Troviamo la keyloc negli inventari collocati
				ATTValVector<Tb950Inv *> *tb950InvCollocatiVect = curBiblio950->getTb950InvCollocatiVect();
				char *invKeyLoc;
				int j;
				for (j = 0; j < tb950InvCollocatiVect->Length(); j++) {
					tb950Inv = tb950InvCollocatiVect->Entry(j);
					invKeyLoc = tb950Inv->getField(tb950Inv->tbinv_key_loc);
					if (!strcmp(keyLoc, invKeyLoc))
					{
						// Crea collocazione
						creaTags950Collocazione_noInv(coll, df);
//df->dump();
						// Crea Inventario collocato
//						inventario.Clear(); // 27/01/2010 14.10
//						costruisciInventarioE(tb950Inv, 0, inventario, 0);

					creaTags950Inventario(tb950Inv, tbColl, df);
//df->dump();

//						sf = new Subfield('e'); // dati di inventariazione
//						sf->setData(&inventario);
//						df->addSubfield(sf);
					}
				}
			}
			else
			{
				creaTags950Collocazione_noInv(coll, df);
//df->dump();
			}
		} // End for collocazioni CON inventario


		if (IS_TAG_TO_GENERATE(950))	// 21/05/2013
		{

			if (esportaSoloInventariCollocati) // 31/05/2018
			{
//printf ("\nesportaSoloInventariCollocati");

				DataField* dfic = new DataField();
				dfic->setTag((char *)"950");

				dfic->setIndicator1(' ');
				dfic->setIndicator2(livelloGerarchico);


				// Abbiamo almeno una collocazione?
				sf = df->getSubfield('d');
				if (sf)
				{
					ATTValVector<Subfield*> *sfv =  df->getSubfields();

					bool inCollocazione=false;
					bool inInventario=false;
					char sfCode;

					// Con almeno una collocazione rimuoviamo tutti gli inventati senza collocazione
					for (int i=0; i < sfv->Length(); i++)
					{
						sf = sfv->Entry(i);

						sfCode = sf->getCode();

						if (sfCode == 'a')
							dfic->addSubfield(new Subfield(sfCode, sf->getData()));

						else if (sf->getCode() == 'd') // Inziamo una collocazione?
						{
							inCollocazione=true;
							inInventario=false;
							dfic->addSubfield(new Subfield(sfCode, sf->getData()));
						}
						else if (sf->getCode() == 'e')
						{
							if (inCollocazione == false || (inCollocazione == true && inInventario == true)) // Siamo dentro una collocazione
							{
								inInventario=false; // Non esportare inventario
							}
							else
							{ // esporta inventario perche' collocato
								inInventario=true;
								dfic->addSubfield(new Subfield(sfCode, sf->getData()));
							}
						}
						else
						{
							if (inInventario==true)
							{ // Esporta inventario
								dfic->addSubfield(new Subfield(sfCode, sf->getData()));
							}

						}
//						printf ("\n\tSubfield '%c'=%s", sf->getCode(), sf->getData());
					}

					// Abbiamo almeno una collocazione?
					delete df; // cancelliamo datafield non filtrato
					sf = dfic->getSubfield('d');
					if (sf)
						marcRecord->addDataField(dfic);
					else
						delete dfic;

				}
				else
				{
					delete df; 	// cancelliamo datafield non filtrato
					delete dfic;// cancelliamo datafield filtrato

				}




			}
			else
			{
				// 11/03/2015 Controlla che ci sia almeno un sottocampo
				ATTValVector<Subfield*> *sfv =  df->getSubfields();
				if (sfv->Length() > 1) // Skip se solo biblioteca senza inventari/collocazioni/esemplari
					marcRecord->addDataField(df);
				else
					delete df;
			}

		}
		else
			delete df;	// 22/05/2013

// 18/12/2019 creato apposito metodo
//		if (df956)
//		{
//			Subfield *sf = df956->getSubfield('a'); // descrizione della biblioteca (non ripetibile)
//			sf->setData(&biblio);
//
//			marcRecord->addDataField(df956);
//			df956 = 0;
//		}

	} // End for bibliotecheKV->Length()


	return true;
} // End creaTag950_Gestionale




#define NEW_950

#ifndef NEW_950

bool Marc4cppCollocazione::elaboraDatiCollocazione(char *bid, char livelloGerarchico) {
	//DataField *df;
	clearBiblio950Vect();
	CString cdBib;
	char *recordBib; // *recordBid,
	//	char *newBib;
	curCdBib = "";
	this->livelloGerarchico = livelloGerarchico;

//	printf ("\nelaboraDatiCollocazione per bid=%s", bid);
//	if (!strcmp(bid, "MUS0232990"))
//		return true;


	bool retb;
	//int ctr=0;
	// Abbiamo inventari per questo titolo (BID)?
	if (tb950Inv->existsRecordNonUnique(bid)) {
		// Bid con inventari
		while (tb950Inv->loadNextRecord(bid)) // bid
		{
			// Cambio biblioteca?
			recordBib = tb950Inv->getField(tb950Inv->tbinv_cd_bib);

			if (
					strcmp(recordBib, curCdBib.data())  	// se biblioteca diversa &&
					//&& strcmp(recordBib, "null")				// se biblioteca != null
					&& *recordBib				// se biblioteca != null
					) {
				addBiblioteca(recordBib);
			}
			//if (!ctr)
			curBiblio950->addInventario(tb950Inv, tb950Coll, tb950Ese);
			//ctr++;
		} // end while loadNextRecord


// 21/05/2013 per poter generare le 956 indipendentemente dalle 95x		if (IS_TAG_TO_GENERATE(950))
			retb = creaTag950_Gestionale(bid);
		if (IS_TAG_TO_GENERATE(960))
			retb = creaTag960_Gestionale(bid);
		return true;

	} else if (tb950Coll->existsRecord(bid)) // non ci sono inventari per BID. Abbiamo delle collocazioni per BID?
	{ // BID senza inventari ma con collocazioni
		while (tb950Coll->loadNextRecord(bid)) {
			if (strcmp(tb950Coll->getField(tb950Coll->bid), bid)) { // Stop al cambio BID o EOF
				if (IS_TAG_TO_GENERATE(950))
					retb = creaTag950_Gestionale(bid);
				if (IS_TAG_TO_GENERATE(960))
					retb = creaTag960_Gestionale(bid);
				return true;
			}
			// Cambio biblioteca?
			recordBib = tb950Coll->getField(tb950Coll->cd_biblioteca_sezione);
			if (strcmp(recordBib, curCdBib.data()))
				addBiblioteca(recordBib);
			curBiblio950->addCollocazione(tb950Coll, tb950Ese);
		}
		if (IS_TAG_TO_GENERATE(950))
			retb = creaTag950_Gestionale(bid);
		if (IS_TAG_TO_GENERATE(960))
			retb = creaTag960_Gestionale(bid);
		return true;
	}
	return false;
} // End elaboraDatiCollocazione


#else

bool Marc4cppCollocazione::elaboraDatiCollocazione(char *bid, char livelloGerarchico) {
	// 27/03/2015 do nothing if not required
	if (!IS_TAG_TO_GENERATE(950) &&
		!IS_TAG_TO_GENERATE(956) &&
		!IS_TAG_TO_GENERATE(960)
		)
		return true;



	clearBiblio950Vect();
	CString cdBib;
	char *recordBib;
	curCdBib = "";
	this->livelloGerarchico = livelloGerarchico;


	// Per ogni biblioteca
	// Troviamo tutti gli inventari NON COLLOCATI e COLLOCATI
	if (tb950Inv->existsRecordNonUnique(bid)) {
		// Bid con inventari
		while (tb950Inv->loadNextRecord(bid)) // bid
		{
			// Cambio biblioteca?
			recordBib = tb950Inv->getField(tb950Inv->tbinv_cd_bib);
			if (
					strcmp(recordBib, curCdBib.data())  	// se biblioteca diversa &&
					//&& strcmp(recordBib, "null")				// se biblioteca != null
					&& *recordBib				// se biblioteca != null
					) {
				addBiblioteca(recordBib);
			}
			curBiblio950->addInventario_coll_noColl(tb950Inv, tb950Coll, tb950Ese); //
		} // end while loadNextRecord

	}
	curCdBib.Clear();

	// Troviamo tutte le collocazioni per il bid in questione
	if (tb950Coll->existsRecord(bid))
	{
		while (tb950Coll->loadNextRecord(bid)) {

//printf("Troviamo tutte le collocazioni");
//tb950Coll->dumpRecord();


			if (strcmp(tb950Coll->getField(tb950Coll->bid), bid)) // Stop al cambio BID o EOF
				break;
			// Cambio biblioteca?
			recordBib = tb950Coll->getField(tb950Coll->cd_biblioteca_sezione);
			if (strcmp(recordBib, curCdBib.data()))
			{
				// La biblioteca e' gia' instanziata?
				curBiblio950 = (Biblio950*)bibliotecheKV->GetValueFromKey(recordBib);
				if (!curBiblio950)
					addBiblioteca(recordBib);
			}
			curBiblio950->addCollocazione_any(tb950Coll, tb950Ese);
		} // End while
	}


	// Troviamo tutti gli esemplari per il bid dell'inventario
	//Collocazione950 *collocazione950;

	CKeyValueVector *kvv;
	CKeyValue *kv;
	for (int i=0; i < bibliotecheKV->Length(); i++)
	{
		curBiblio950 = (Biblio950*)bibliotecheKV->GetValue(i);

		kvv = curBiblio950->getCollocazione950VectKV();

		for (int j=0; j < kvv->Length(); j++)
		{
			kv = kvv->Get(j);
			if(strlen(kv->Key.p)) // Check if bidDoc
			{
				if (!strcmp(bid, kv->Key.p)) // bid e bid_doc devono essere uguali per appartenere ad esemplare
				{
//					printf ("\nTrovato esemplare: bid=%s, bid_doc=%s", bid, kv->Key.p);

					Collocazione950 *c950 =  (Collocazione950*)kv->Val.v;
					Tb950Coll *coll = c950->getTb950Coll();

					char *bidDoc = kv->Key.p;
					char *colCdDoc = coll->getField(coll->tbcol_cd_doc); // cd_doc
					char *colCdBib = coll->getField(coll->cd_biblioteca_sezione);

					// Troviamo l'esemplare
					char *eseCdDoc, *eseCdBib;
					if (tb950Ese->existsRecord(bidDoc))
					{
						// Cicliamo sui bidese cercando il cdDoc ed il codice biblioteca
						while (tb950Ese->loadNextRecord(bidDoc))
						{
							eseCdDoc = tb950Ese->getField(tb950Ese->tbese_cd_doc);
							if (strcmp(eseCdDoc, colCdDoc)) // stesso cdDoc?
								continue; // Not found


							eseCdBib =  tb950Ese->getField(tb950Ese->cd_biblioteca);
							if (strcmp(eseCdBib, colCdBib))
								continue;

							CString eseKey = eseCdDoc;
							eseKey.AppendString(eseCdBib);
							curBiblio950->addEsemplare(eseKey.data(), tb950Ese->getField(tb950Ese->tbese_cons_doc));
							break;
						}
					}
					else
					{ // non esiste
					}
				}
				else
				{
//					printf ("\nTrovato esemplare MA NON per questo bid: bid=%s bid_doc=%s", bid, kv->Key.p);
				}

			}
		}
	}


// Dump info sulle collocazioni/inventari/esemplari
#ifdef DUMP
	for (int i=0; i < bibliotecheKV->Length(); i++)
	{
		curBiblio950 = (Biblio950*)bibliotecheKV->GetValue(i);
		printf ("\n\nBiblioteca '%s' %s", curBiblio950->getIdBiblioteca(), curBiblio950->getDescBiblioteca());
		curBiblio950->dump();
	}
#endif

	bool retb=false;
	// 21/05/2013 per poter generare le 956 indipendentemente dalle 95x		if (IS_TAG_TO_GENERATE(950))
// 18/12/2019 Mi genera doppioni 316/317
//			retb = creaTag950_Gestionale(bid);
//			if (!retb)
//				return retb;

		if (IS_TAG_TO_GENERATE(950))
		{
			bool retb = creaTag950_Gestionale(bid);
			if (!retb)
				return retb;
		}

//18/12/2019 Dobbiamo gestire la 956 indipendentemente dalla 950!!
		if (IS_TAG_TO_GENERATE(956))
			retb = creaTag956_digitale(bid);
//-----------
		if (IS_TAG_TO_GENERATE(960))
			retb = creaTag960_Gestionale(bid);

	return retb;
} // End elaboraDatiCollocazione

#endif




void Marc4cppCollocazione::creaTags950Collocazione_noInv(Collocazione950 *collocazione950, DataField *df) {
	Subfield *sf;
	char *codsez, *collocazione, *specificazione, *key_loc_coll;
	CString *conscollSPtr;
	Tb950Coll *tb950CollLoc;

	tb950CollLoc = collocazione950->getTb950Coll();

	conscollSPtr = tb950CollLoc->getFieldString(tb950CollLoc->tbcol_consis);
	codsez = tb950CollLoc->getField(tb950CollLoc->tbcol_cd_sez);
	collocazione = tb950CollLoc->getField(tb950CollLoc->tbcol_cd_loc);
	specificazione = tb950CollLoc->getField(tb950CollLoc->tbcol_spec_loc);

	key_loc_coll = tb950CollLoc->getField(tb950CollLoc->tbcol_key_loc);

	// Esiste un esemplare?
	CString eseKey = tb950CollLoc->getField(tb950CollLoc->tbcol_cd_doc);
	eseKey.AppendString(tb950CollLoc->getField(tb950CollLoc->cd_biblioteca_sezione));

//printf ("\ncurBiblio950=%s", curBiblio950->getIdBiblioteca());
//printf ("\nesekey=%s", eseKey.data());

//	char * esemplare = curBiblio950->findEsemplare950(eseKey);
	CString esemplare = curBiblio950->findEsemplare950(eseKey);

//	if (esemplare && *esemplare != '$') // 01/04/2015
	if (!esemplare.IsEmpty() && esemplare.GetFirstChar() != '$') // 01/04/2015
	{
		sf = new Subfield('b'); // consistenza di esemplare
		esemplare.Strip(esemplare.trailing, ' '); // Mail calogiuri 17/07/2017
		sf->appendData(&esemplare);
		df->addSubfield(sf);
	}

	// Costruiamo il sottocampo della collocazione
	if (!conscollSPtr->IsEmpty() && conscollSPtr->GetFirstChar() != '$') // 04/02/2011 Mail Rossana
	{
	sf = new Subfield('c'); // consistenza di collocazione
	CString *sPtr =  tb950CollLoc->getFieldString(tb950CollLoc->tbcol_consis);
	sPtr->Strip(sPtr->trailing, ' ');  // Mail calogiuri 17/07/2017
	sf->setData(sPtr);
	df->addSubfield(sf);
	}
	sf = new Subfield('d'); // dati di collocazione
	CString s = tb950CollLoc->getField(tb950CollLoc->cd_biblioteca_sezione);	//	3chr
	s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_sez));	// 10 chr
	s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_loc));	// 24 chr
	int len = strlen(collocazione);
	if (len < 24)
		s.AppendString(padding[len], paddingSize[len]);
	s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_spec_loc)); // spec_loc 12 chr
	s.Strip(s.trailing, ' ');
	if (!tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_edizione)->IsEmpty())
		s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_cd_edizione)); // 2 chr
	if (!tb950CollLoc->getFieldString(tb950CollLoc->tbcol_classe)->IsEmpty()) {
		s.AppendString(tb950CollLoc->getFieldString(tb950CollLoc->tbcol_classe)); // 31 chr
		if (s.GetLastChar() == '\n')
			s.ExtractLastChar();
	}

	sf->setData(&s);
	df->addSubfield(sf);

} // end creaTags950Collocazione_noInv



/*!
\brief <b>Tag 960 - Gestionale</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>960</td></tr>
<tr><td valign=top>Descrizione</td><td>Gestionale</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
Livello gerqrchico:
    <UL>
        <li> 0 = senza livelli (monografie senza livelli e periodici)
        <li> 1 = livello alto (monografia superiore)
        <li> 2 = livello basso (monografia inferiore o intermedia)
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - descrizione della biblioteca. Non ripetibile.
        <li> b - consistenza esemplare. Non ripetibile.
        <li> c - consistenza collocazione. Ripetibile.
        <li> d - collocazione. Ripetibile.
        <li> e - dati di inventario. Ripetibile.
        <li> f - collocazione definitiva. Ripetibile.
        <li> g - collocazione normalizzata. Ripetibile.
        <li> h - data di ingresso. Ripetibile.
        <li> i - data di accessionamento. Ripetibile.
        <li> l - Note all'inventario. Ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/


void Marc4cppCollocazione::creaTags950Inventario(Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, DataField *df) {
	Subfield *sf;
	CString inventario;
	CString *sPtr, *sezPtr, *locPtr, *specPtr;
	//char *ptr;
	CString sTmp;
		char dateBuf[8 + 1];  //04/11/2009 14.27 Secondo documento unimarc_sbnweb.doc la data non va inclusa
		*(dateBuf + 8) = 0;

	inventario.Clear();
	costruisciInventarioE(tb950Inv, tb950Coll, inventario, df);

//tb950Inv->dumpRecord();

	// Dati di inventario
	sf = new Subfield('e');
	sf->setData(&inventario);
	df->addSubfield(sf);

	if (doingTag == DOING_TAG_960) {

		CString s;
		// 06/07/09
		// Dati di Inventario

		sezPtr = tb950Inv->getFieldString(tb950Inv->tbinv_sez_old);
		locPtr = tb950Inv->getFieldString(tb950Inv->tbinv_loc_old);
		specPtr = tb950Inv->getFieldString(tb950Inv->tbinv_spec_old);

//			if (!sPtr->IsEmpty())
		if (!sezPtr->IsEmpty()
			|| !locPtr->IsEmpty()
			|| !specPtr->IsEmpty())
		{
			// Collocazione definitiva
			// -----------------------
			sf = new Subfield('f');

			s.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_sez_old));
			s.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_loc_old));
			s.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_spec_old));

			sf->setData(&s); // sPtr->data()
			df->addSubfield(sf);
		}
		// Collocazione normalizzata $g, ex $z ex 'f'
		// -------------------------------------------
		if (tb950Coll != 0)
		{
			crea960CollocazioneNormalizzata(s, tb950Inv, tb950Coll); // , sPtr
			sf = new Subfield('g'); //
			sf->setData(&s);
			df->addSubfield(sf);
		}

	} // end if DOING_TAG_960
	else
	{
		// Categoria di fruizione
		//const char * cd_frui = tb950Inv->getField(tb950Inv->tbinv_cd_frui);
		sf = new Subfield('f');
		sf->setData(tb950Inv->getFieldString(tb950Inv->tbinv_cd_frui));
		df->addSubfield(sf);
	}

	// Data di ingresso, ex cd_supporto
	// ----------------
//tb950Inv->dumpRecord();
		sPtr = tb950Inv->getFieldString(tb950Inv->tbinv_data_ingresso);
	//		if (strcmp(tb950Inv->getField(tb950Inv->tbinv_cd_supporto), "null"))
//		if (strcmp(sPtr->data(), "null") && !sPtr->IsEmpty())
	if (sPtr && !sPtr->IsEmpty())
	{
		sf = new Subfield('h');
		CMisc::formatDate1(sPtr->data(), dateBuf);
		sf->setData(dateBuf, 8);
		df->addSubfield(sf);
	}

	// Data di accessionamento. Ex tbinv_stato_con
//			sPtr = tb950Inv->getFieldString(tb950Inv->tbinv_stato_con);
	sPtr = tb950Inv->getFieldString(tb950Inv->tbinv_ts_ins_prima_coll);
	//		if (strcmp(tb950Inv->getField(tb950Inv->tbinv_stato_con), "null"))
//		if (strcmp(sPtr->data(), "null") && !sPtr->IsEmpty())
	if (sPtr && !sPtr->IsEmpty())
	{
		sf = new Subfield('i');
		CMisc::formatDate1(sPtr->data(), dateBuf);
		sf->setData(dateBuf, 8);
		df->addSubfield(sf);
	}

	// Abbiamo note all'invetario?
	CString key;
	key = tb950Inv->getField(tb950Inv->tbinv_cd_bib); // Costruiamo la chiave di ricerca
	key.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_cd_serie));

	sTmp = tb950Inv->getField(tb950Inv->tbinv_cd_inven);
	sTmp.leftPadding('0', 10);
	key.AppendString(&sTmp);


	// Ci possono essere + note per lo stesso inventario
	if (tbcNotaInv->existsRecordNonUnique(key.data()))
	{
		while (tbcNotaInv->loadNextRecord(key.data()))
//			bool retb = tbcNotaInv->loadRecord(key.data());
//			if (retb)
	{
		// La nota
		sf = new Subfield('l');
		CString *sPtr = tbcNotaInv->getFieldString(tbcNotaInv->ds_nota_libera);
		sPtr->Strip(sPtr->trailing, ' '); // mail Calogiur 17/07/2017
		sf->setData(sPtr);
		df->addSubfield(sf);

		// Il codice nota in chiaro
		sf = new Subfield('m');
		sTmp = tbcNotaInv->getField(tbcNotaInv->cd_nota);
		sTmp.Strip(sTmp.trailing, ' ');
		char *descCdNota = codiciNotaKV->GetValueFromKey(sTmp.data());
		if (!descCdNota)
			sTmp.AppendString((char *)"? Codice Tipo nota invalido", 27);
		else
			sTmp = descCdNota;
		sf->setData(&sTmp);
		df->addSubfield(sf);
	}
	}// end if note inventario

} // End creaTags950Inventario

void Marc4cppCollocazione::creaTags950Inventari(ATTValVector<Tb950Inv *> *tb950InvVect, DataField *df, Tb950Coll *tb950Coll) {
	Tb950Inv * tb950Inv;

	for (int ki = 0; ki < tb950InvVect->Length(); ki++) {
		//tb950Inv = tb950InvVect->Entry(ki);
		creaTags950Inventario(tb950InvVect->Entry(ki), tb950Coll, df);
	} // End for INVENTARI
} // End creaTags950Inventari





#define NEW_960

#ifndef NEW_950

bool Marc4cppCollocazione::creaTag960_Gestionale(char *bid) {
	Biblio950 *biblio950;
	//	Tb950Inv * tb950Inv;
	Tb950Ese * tb950Ese;
	DataField *df;
	Subfield *sf;
	char * cdBib;
	bool retb;

	doingTag = DOING_TAG_960;

	CString inventario;
	const char *consese;

	// 06/07/09 Per ogni biblioteca genero una 960 per gli inventari non collocati ed una 960 per ogni collocazione
	// Cicliamo sulle biblioteche
	for (int i = 0; i < bibliotecheKV->Length(); i++) {

		cdBib = (char *) bibliotecheKV->GetKey(i);

		// Controlla se biblioteca da escludere da 960
		if (bibliotecheDaNonMostrareIn950KV->existsKey(cdBib))
			continue;


		biblio950 = (Biblio950 *) bibliotecheKV->GetValue(i);
		char indicatore_2 = livelloGerarchico;

		if (biblio950->getTb950InvNonCollocatiVect()->length())
			retb = creaTag960InventariNonCollocati(cdBib, biblio950);

		/*
		 * NMB: Gestione collocazioni senza inventario per bid di collcoazione da prametrizzare in funzione del Polo
		 // Gestione collocazioni senza inventario per bid di collcoazione
		 // ==============================================================
		 CKeyValueVector * collocazione950SenzaInventariVectKV = biblio950->getCollocazione950SenzaInventariVectKV();
		 for (int j=0; j < collocazione950SenzaInventariVectKV->Length(); j++)
		 {
		 creaTags950Collocazione((Collocazione950 *)collocazione950SenzaInventariVectKV->GetValue(j), df);
		 }
		 */

		// Gestione delle collocazioni senza esemplare
		// ===========================================
		CKeyValueVector * collocazione950SenzaEsemplareVectKV =	biblio950->getCollocazione950SenzaEsemplareVectKV();
		for (int j = 0; j < collocazione950SenzaEsemplareVectKV->Length(); j++) {

			df = new DataField();
			df->setTag("960");
			df->setIndicator1(' ');
			df->setIndicator2(indicatore_2);
			sf = new Subfield('a'); // descrizione della biblioteca (non ripetibile)
			CString biblio = biblio950->getDescBiblioteca();
			biblio.Strip(biblio.trailing, ' ');
			sf->setData(&biblio);
			df->addSubfield(sf);

			creaTags950Collocazione((Collocazione950 *) collocazione950SenzaEsemplareVectKV->GetValue(j), df);

//printf ("\nDFcoll=%s", df->toString());
			marcRecord->addDataField(df);

		}

		// Gestione Esemplari / Collocazioni / Inventari
		// =============================================
		CKeyValueVector * esemplare950VectKV = biblio950->getEsemplare950VectKV();
		Esemplari950 *esemplare950;

		for (int j = 0; j < esemplare950VectKV->Length(); j++) {

			// Costruiamo il sottocampo dell'esemplare
			esemplare950 = (Esemplari950*) esemplare950VectKV->GetValue(j);
			tb950Ese = esemplare950->getTb950Ese();
			consese = tb950Ese->getField(tb950Ese->tbese_cons_doc);

//if (!strcmp (bid, "SBL0135988"))
//tb950Ese->dumpRecord();

			// Per ogni collocazione di esemplare
			ATTValVector<Collocazione950 *> *collocazione950Vect = esemplare950->getCollocazione950Vect();
			for (int k = 0; k < collocazione950Vect->Length(); k++) {
				df = new DataField();
				df->setTag("960");
				df->setIndicator1(' ');
				df->setIndicator2(indicatore_2);
				sf = new Subfield('a'); // descrizione della biblioteca (non ripetibile)
				CString biblio = biblio950->getDescBiblioteca();
				biblio.Strip(biblio.trailing, ' ');
				sf->setData(&biblio);
				df->addSubfield(sf);

				if (*consese != '$')
				{
					sf = new Subfield('b'); // consistenza dell'esemplare
					sf->setData(tb950Ese->getFieldString(tb950Ese->tbese_cons_doc)); // consese
					df->addSubfield(sf);
				}

				creaTags950Collocazione(collocazione950Vect->Entry(k), df);

//printf ("\nDFese=%s", df->toString());
				marcRecord->addDataField(df);
			} // End for COLLOCAZIONI
		} // End for ESEMPLARI

		//	marcRecord->addDataField(df);
	} // End for bibliotecheKV->Length()


//printf ("\nRECORD=%s", marcRecord->toString());

	return true;
} // End creaTags960

#else

bool Marc4cppCollocazione::creaTag960_Gestionale(char *bid) {
	//Biblio950 *biblio950;
	//	Tb950Inv * tb950Inv;
	Tb950Ese * tb950Ese;
	DataField *df;
	Subfield *sf;
	char * cdBib;
	bool retb;

	doingTag = DOING_TAG_960;


	CString inventario;
	const char *consese;

	CKeyValueVector *collocazineGiaFattaKV = new CKeyValueVector(tKVSTRING, tKVSTRING);




	// 06/07/09 Per ogni biblioteca genero una 960 per gli inventari non collocati ed una 960 per ogni collocazione
	// Cicliamo sulle biblioteche
	for (int i = 0; i < bibliotecheKV->Length(); i++) {

		cdBib = (char *) bibliotecheKV->GetKey(i);
		collocazineGiaFattaKV->Clear();

		// Controlla se biblioteca da escludere da 960
		if (bibliotecheDaNonMostrareIn950KV->existsKey(cdBib))
			continue;


		curBiblio950 = (Biblio950 *) bibliotecheKV->GetValue(i);
		CString descBiblio = curBiblio950->getDescBiblioteca();
		descBiblio.Strip(descBiblio.trailing, ' ');

		char indicatore_2 = livelloGerarchico;

//		df = new DataField();
//		df->setTag("960");
//		df->setIndicator1(' ');
//		df->setIndicator2(indicatore_2);
//
//		sf = new Subfield('a'); // descrizione della biblioteca (non ripetibile)
//		descBiblio.Strip(descBiblio.trailing, ' ');
//		df->addSubfield(sf);
//		sf->setData(&descBiblio);

		// Gestione inventari non collocati
		// ================================
		//Tb950Inv * tb950Inv;
		DataField *df;
		Subfield *sf;
		CString inventario;


		// Gestione inventari non collocati
		// ================================
		ATTValVector<Tb950Inv *> *tb950InvNonCollocatiVect = curBiblio950->getTb950InvNonCollocatiVect();

		if (tb950InvNonCollocatiVect->Length())
		{
			df = new DataField();
			df->setTag((char *)"960");
			df->setIndicator1(' ');
			df->setIndicator2(indicatore_2);

			sf = new Subfield('a');
			sf->setData(&descBiblio);
			df->addSubfield(sf);

			creaTags950Inventari(tb950InvNonCollocatiVect, df, 0);
			marcRecord->addDataField(df);
		}




		// Gestione collocazioni senza inventario 27/03/2015
		// =======================================
		// Per ogni collocazione
		CKeyValueVector * kvv = curBiblio950->getCollocazione950VectKV();
		Collocazione950 * coll;
		char *keyLoc;
		CKeyValue *kv;

		ATTValVector<Tb950Inv *> *tb950InvCollocatiVect = curBiblio950->getTb950InvCollocatiVect();
		for (int k = 0; k < kvv->Length(); k++) {
			kv = kvv->Get(k);
			coll = (Collocazione950 *)kv->Val.v;

			Tb950Coll *tbColl = coll->getTb950Coll();
			keyLoc = tbColl->getField(tbColl->tbcol_key_loc);
			// Controlliamo se collocazione senza inventario
			if (*keyLoc)
			{
				// Troviamo la keyloc negli inventari collocati
				char *invKeyLoc;
				int j;
				Tb950Inv *tb950InvCollV;
				for (j = 0; j < tb950InvCollocatiVect->Length(); j++) {
					tb950InvCollV = tb950InvCollocatiVect->Entry(j);
					invKeyLoc = tb950InvCollV->getField(tb950InvCollV->tbinv_key_loc);
					if (!strcmp(keyLoc, invKeyLoc))
						break;
				}
				if (j == tb950InvCollocatiVect->Length()) // Nessun invetario per questa collocazione
				{
					collocazineGiaFattaKV->Add(keyLoc, keyLoc);

					df = new DataField();
					df->setTag((char *)"960");
					df->setIndicator1(' ');
					df->setIndicator2(indicatore_2);

					sf = new Subfield('a');
					sf->setData(&descBiblio);
					df->addSubfield(sf);

					creaTags950Collocazione_noInv(coll, df);
					marcRecord->addDataField(df);

				}
			}
//02/04/2015			else
//				creaTags950Collocazione_noInv(coll, df);

		} // End for collocazioni senza inventario




		// Gestione collocazioni CON inventario 27/03/2015
		// =======================================
		for (int k = 0; k < kvv->Length(); k++) {
			kv = kvv->Get(k);
			coll = (Collocazione950 *)kv->Val.v;

			Tb950Coll *tbColl = coll->getTb950Coll();
			keyLoc = tbColl->getField(tbColl->tbcol_key_loc);
			// Controlliamo se collocazione con inventario
			if (*keyLoc)
			{
				// Troviamo la keyloc negli inventari collocati
				char *invKeyLoc;
				int j;
				Tb950Inv *tb950InvCollV;
				for (j = 0; j < tb950InvCollocatiVect->Length(); j++) {
					tb950InvCollV = tb950InvCollocatiVect->Entry(j);
					invKeyLoc = tb950InvCollV->getField(tb950InvCollV->tbinv_key_loc);
					if (!strcmp(keyLoc, invKeyLoc))
					{
						collocazineGiaFattaKV->Add(keyLoc, keyLoc);

						df = new DataField();
						df->setTag((char *)"960");
						df->setIndicator1(' ');
						df->setIndicator2(indicatore_2);

						sf = new Subfield('a');
						sf->setData(&descBiblio);
						df->addSubfield(sf);

						// Crea collocazione
						creaTags950Collocazione_noInv(coll, df);
//						// Crea Inventario collocato
						creaTags950Inventario(tb950InvCollV, tbColl, df);

						marcRecord->addDataField(df);
					}
				}
			}
//02/04/2015			else
//				creaTags950Collocazione_noInv(coll, df);
		} // End for collocazioni CON inventario



		// Gestione delle collocazioni senza esemplare
		// ===========================================
		// Per ogni collocazione

		CString eseKey;
		Tb950Coll *tbColl;
		for (int k = 0; k < kvv->Length(); k++) {
			kv = kvv->Get(k);
			coll = (Collocazione950 *)kv->Val.v;
			tbColl = coll->getTb950Coll();
			keyLoc = tbColl->getField(tbColl->tbcol_key_loc);

			// Esiste un esemplare?
			eseKey = tbColl->getField(tbColl->tbcol_cd_doc);
			eseKey.AppendString(tbColl->getField(tbColl->cd_biblioteca_sezione));
			char * esemplare = curBiblio950->findEsemplare950(eseKey);
			if (esemplare)
				continue; // skip

// Se collocazione gia' trattata con o senza inventario skip
//			Tb950Inv *tb950InvCollV;
//			int j;
//			for (j = 0; j < tb950InvCollocatiVect->Length(); j++) {
//				tb950InvCollV = tb950InvCollocatiVect->Entry(j);
//				char *invKeyLoc = tb950InvCollV->getField(tb950InvCollV->tbinv_key_loc);
//				if (!strcmp(invKeyLoc, keyLoc))
//					break;
//			}
//			if (j < tb950InvCollocatiVect->Length()) // collocazione gia trattata
//				continue;

			if (collocazineGiaFattaKV->GetValueFromKey(keyLoc))
				continue;

			df = new DataField();
			df->setTag((char *)"960");
			df->setIndicator1(' ');
			df->setIndicator2(indicatore_2);

			sf = new Subfield('a');
			sf->setData(&descBiblio);
			df->addSubfield(sf);

			creaTags950Collocazione(coll, df);

			marcRecord->addDataField(df);
		} // End for COLLOCAZIONI senza esemplare


		// Gestione delle collocazioni con esemplare
		// =========================================
		for (int k = 0; k < kvv->Length(); k++) {
			kv = kvv->Get(k);
			coll = (Collocazione950 *)kv->Val.v;
			tbColl = coll->getTb950Coll();
			keyLoc = tbColl->getField(tbColl->tbcol_key_loc);

			// Esiste un esemplare?
			eseKey = tbColl->getField(tbColl->tbcol_cd_doc);
			eseKey.AppendString(tbColl->getField(tbColl->cd_biblioteca_sezione));
			char * esemplare = curBiblio950->findEsemplare950(eseKey);
			if (!esemplare)
				continue;

// Se collocazione gia' trattata con o senza inventario skip

			if (collocazineGiaFattaKV->GetValueFromKey(keyLoc))
				continue;


			df = new DataField();
			df->setTag((char *)"960");
			df->setIndicator1(' ');
			df->setIndicator2(indicatore_2);

			sf = new Subfield('a');
			sf->setData(&descBiblio);
			df->addSubfield(sf);

			if (*esemplare != '$')
			{
				sf = new Subfield('b'); // consistenza dell'esemplare
				sf->appendData(esemplare);
				df->addSubfield(sf);
			}
			creaTags950Collocazione(coll, df);
			marcRecord->addDataField(df);
		} // End for COLLOCAZIONI con esemplare

	} // End for bibliotecheKV->Length()


//printf ("\nRECORD=%s", marcRecord->toString());

	delete collocazineGiaFattaKV;
	return true;
} // End creaTag960_Gestionale

#endif






bool Marc4cppCollocazione::creaTag956_digitale(char *bid) {

	//Biblio950 *curBiblio;
	Tb950Inv * tb950Inv;
//	Tb950Ese * tb950Ese;
	DataField *df;
	Subfield *sf;
	char * cdBib;
//	doingTag = DOING_TAG_950;

	CString inventario; // , *sPtr
//	const char *consese;

	CString biblio;
	df956 = 0; // Ensure data field pointer is initialized

	// Cicliamo sulle biblioteche
	// Una 950 per biblioteca
	for (int i = 0; i < bibliotecheKV->Length(); i++) {
		cdBib = (char *) bibliotecheKV->GetKey(i);

		// Controlla se biblioteca da escludere da 950
		if (bibliotecheDaNonMostrareIn950KV->existsKey(cdBib))
			continue;

		curBiblio950 = (Biblio950 *) bibliotecheKV->GetValue(i);
//		char indicatore_2 = ; //elaboraLivelloGerarchicoNotizia(bid );


		// Gestione collocazioni senza inventario (PRECISATO)
		// =======================================
		// Per ogni collocazione
		CKeyValueVector * kvv = curBiblio950->getCollocazione950VectKV();
		Collocazione950 * coll;
		char *keyLoc;
		CKeyValue *kv;

		// Gestione collocazioni CON inventario
		// =======================================
		for (int k = 0; k < kvv->Length(); k++) {
			kv = kvv->Get(k);
			coll = (Collocazione950 *)kv->Val.v;


			Tb950Coll *tbColl = coll->getTb950Coll();
			keyLoc = tbColl->getField(tbColl->tbcol_key_loc);

//printf ("\ncollocazioni CON inventario: bid=%s keyLoc=%s", kv->Key.p, keyLoc);


			// Controlliamo se collocazione con inventario
			if (*keyLoc)
			{
				// Troviamo la keyloc negli inventari collocati
				ATTValVector<Tb950Inv *> *tb950InvCollocatiVect = curBiblio950->getTb950InvCollocatiVect();
				char *invKeyLoc;
				int j;
				for (j = 0; j < tb950InvCollocatiVect->Length(); j++) {
					tb950Inv = tb950InvCollocatiVect->Entry(j);
					invKeyLoc = tb950Inv->getField(tb950Inv->tbinv_key_loc);
					if (!strcmp(keyLoc, invKeyLoc))
					{

//~~~					creaTags950Inventario(tb950Inv, tbColl, df);
//					s = tb950Inv->getField(tb950Inv->bid);
					CString *sPtr;
					sPtr = tb950Inv->getFieldString(tb950Inv->tbinv_id_accesso_remoto);
					if (sPtr->IsEmpty())
						sPtr = tb950Inv->getFieldString(tb950Inv->tbinv_rif_teca_digitale);
					if (!sPtr->IsEmpty())
						{
				//tb950CollPtr->dumpRecord();
						addFields956(tb950Inv, tbColl);


						Subfield *sf = df956->getSubfield('a'); // descrizione della biblioteca (non ripetibile)
						sf->setData(&biblio);
						marcRecord->addDataField(df956);
						df956 = 0;
						}
					}
				}
			}
		} // End for collocazioni CON inventario

	} // End for bibliotecheKV->Length()
	return true;
} // End creaTag956_digitale
