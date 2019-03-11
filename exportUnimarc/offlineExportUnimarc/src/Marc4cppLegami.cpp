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
 *  Created on: 29-dic-2008
 *      Author: Arge
 */

#include <DataField.h>
#include <library/CMisc.h>
#include <library/CString.h>
#include <library/macros.h>
#include <library/CKeyValueVector.h>
#include <Marc4cppLegami.h>
#include <MarcConstants.h>
#include <MarcGlobals.h>
#include <ors/types.h>
#include <Subfield.h>
#include <TbMusica.h>
#include <TbReticoloAut.h>
#include <cctype>
#include <cstring>
#include <iostream>
#include <string>

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

using namespace std;

extern void SignalAnError(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

Marc4cppLegami::Marc4cppLegami(MarcRecord *marcRecord,
		Marc4cppDocumento *marc4cppDocumento, TbTitolo *tbTitolo,
		TbAutore *tbAutore, TbSoggetto* tbSoggetto, TbClasse* tbClasse,
		TbMarca *tbMarca, TbLuogo *tbLuogo, TbMusica *tbMusica,
		TbComposizione *tbComposizione, TbIncipit *tbIncipit,
		TbPersonaggio *tbPersonaggio, TbRappresent *tbRappresent,
		TbaOrdini *tbaOrdini, TrTitTit *trTitTit, CFile* trTitTitRelIn,
		CFile* trTitTitRelOffsetIn, char* offsetBufferTrTitTitRelPtr,
		long elementsTrTitTitRel, CFile* trTitTitInvRelIn,
		CFile* trTitTitInvRelOffsetIn, char* offsetBufferTrTitTitInvRelPtr,
		long elementsTrTitTitInvRel, TrTitBib *trTitBib, TrTitAut *trTitAut,
		TrTitAutRel *trTitAutRel, TrTitCla *trTitCla, TrTitMar *trTitMarRel, TrTitMar *trTitMarTb,
		TrAutAut *trAutAutInv,
		TrTitLuo *trTitLuo,
		TbNumeroStd *tbNumeroStandard,
		TbRepertorio *tbRepertorio,
		TrRepMar *trRepMar,
		TrPerInt *trPerInt,

		TsLinkMultim *tsLinkMultim, TbParola *tbParola,

		TbfBiblioteca *tbfBiblioteca,

		Tb950Inv	*tb950Inv,	// 12/09/14
		Tb950Coll	*tb950Coll,
		Tb977 *tb977,	// 16/12/14

		TbTermineTesauro *tbTermineTesauro,
		TbDescrittore *tbDescrittore, TrSogDes *trSogDes, TrDesDes *trDesDes, // 28/06/2016

		CKeyValueVector *bibliotecheDaNonMostrareIn950KV,

		int keyPlusOffsetPlusLfLength, int trKeyPlusOffsetPlusLfLength,
		int key_length,
		char *polo
		) {
	this->marcRecord = marcRecord;
	this->marc4cppDocumento = marc4cppDocumento;
	this->tbTitolo = tbTitolo;
	this->tbAutore = tbAutore;
	this->tbSoggetto = tbSoggetto;
	this->tbClasse = tbClasse;
	this->tbMarca = tbMarca;
	this->tbLuogo = tbLuogo;
	this->tbMusica = tbMusica;
	this->tbComposizione = tbComposizione;
	this->tbIncipit = tbIncipit;
	this->tbPersonaggio = tbPersonaggio;
	this->tbRappresent = tbRappresent;
	this->tbaOrdini = tbaOrdini;
	this->trTitTit = trTitTit;
	this->trTitTitRelIn = trTitTitRelIn;
	this->trTitTitRelOffsetIn = trTitTitRelOffsetIn;
	this->offsetBufferTrTitTitRelPtr = offsetBufferTrTitTitRelPtr;
	this->elementsTrTitTitRel = elementsTrTitTitRel;
	this->trTitTitInvRelIn = trTitTitInvRelIn;
	this->trTitTitInvRelOffsetIn = trTitTitInvRelOffsetIn;
	this->offsetBufferTrTitTitInvRelPtr = offsetBufferTrTitTitInvRelPtr;
	this->elementsTrTitTitInvRel = elementsTrTitTitInvRel;
	this->trTitBib = trTitBib;
	this->trTitAut = trTitAut;
	this->trTitAutRel = trTitAutRel;
	this->trTitCla = trTitCla;
	this->trTitMarRel = trTitMarRel;
	this->trTitMarTb = trTitMarTb;

	this->trAutAutInv = trAutAutInv;
	this->trTitLuo = trTitLuo;
	this->tbNumeroStandard = tbNumeroStandard;
	this->tbRepertorio = tbRepertorio;
	this->trRepMar = trRepMar;
	this->trPerInt = trPerInt;
	this->tsLinkMultim =tsLinkMultim;
	this->tbParola = tbParola;

	//	this->tbfBibliotecaInPoloKV = tbfBibliotecaInPoloKV;
	this->tbfBiblioteca = tbfBiblioteca;
	this->tb950Inv = tb950Inv;	// 12/09/14
	this->tb950Coll = tb950Coll;
	this->tb977 = tb977;

	this->tbTermineTesauro = tbTermineTesauro;
	this->tbDescrittore	= tbDescrittore;
	this->trSogDes = trSogDes;
	this->trDesDes = trDesDes;


	this->bibliotecheDaNonMostrareIn950KV = bibliotecheDaNonMostrareIn950KV;

	this->keyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
	this->trKeyPlusOffsetPlusLfLength = trKeyPlusOffsetPlusLfLength;
	this->key_length = key_length;
	this->polo = polo;
	init();

}

void Marc4cppLegami::init() {
	legamiVectorKV = new CKeyValueVector(tKVSTRING, tKVVOID);
	siArg = 0;
	tbCodiciKV = new CKeyValueVector(tKVSTRING, tKVSTRING);
	initLicr();

	//	//	trTitAut = new TrTitAut(trTitAutIn, trTitAutOffsetIn, offsetBufferTrTitAutPtr, elementsTrTitAut, trKeyPlusOffsetPlusLfLength, key_length);
	//	trTitAut = new TrTitAut(trTitAutIn, trTitAutOffsetIn,
	//			offsetBufferTrTitAutPtr, elementsTrTitAut,
	//			trKeyPlusOffsetPlusLfLength, key_length + key_length); // 05/10/2009 11.10
	//
	//	trTitBib = new TrTitBib(trTitBibIn, trTitBibOffsetIn,
	//			offsetBufferTrTitBibPtr, elementsTrTitBib,
	//			keyPlusOffsetPlusLfLength, key_length);

}

/*
 * Dati provenienti dalla tb_codici (NABI)
 *
 * 'C' = Collana
 * 'M' = Monografia
 * 'T' = Pubblicato con
 * 'P' = Titolo parallelo
 * 'A' = Titolo di raggruppamento controllato
 * 'D' = Titolo sviluppato o estrapolato
 * 'B' = Titolo di raggruppamento non controllato (Titolo in lingua originale)
 * 'S' = Periodico
 * 'N' = Titolo analitico (spoglio)
 * 'W' = Volume privo di titolo significativo (tipico dell'enciclopedia ed opere in + volumi)
 *
 Legami Titolo/Titolo (LETT)
 01      fa parte di
 02      supplemento di
 03      contiene anche
 04      continuazione di
 05      edizione successiva di
 06      ha per titolo di raggruppamento non controllato
 07      altra edizione di
 08      ha per altro titolo
 09      ha per titolo di raggruppamento controllato
 41      assorbe
 42      si fonde con
 43      continuazione parziale di
 51      contiene
 *
 *
 */
void Marc4cppLegami::initLicr() {
	tbCodiciKV->Add("A08D", "A517D");
	tbCodiciKV->Add("B08D", "B517D");
	tbCodiciKV->Add("C01C", "C410C");
	tbCodiciKV->Add("C04C", "C430C");
	tbCodiciKV->Add("C05C", "C440C");
	tbCodiciKV->Add("C07C", "C451C");
	tbCodiciKV->Add("C08D", "C517D");
	tbCodiciKV->Add("C08P", "C510P");
	tbCodiciKV->Add("C09A", "C500A");
	tbCodiciKV->Add("M01C", "M410C");
	tbCodiciKV->Add("M01M", "M461M"); // Per capire se 461 o 462 NO GOOD. Va verificata la notizia di arrivo
	tbCodiciKV->Add("M01S", "M461S");
	tbCodiciKV->Add("M01R", "M560R");
	tbCodiciKV->Add("M02M", "M422M");
	tbCodiciKV->Add("M02S", "M422S");
	tbCodiciKV->Add("M03T", "M423T");
	tbCodiciKV->Add("M04M", "M430M");
	tbCodiciKV->Add("M04S", "M430S");
//	tbCodiciKV->Add("M05M", "M440M");
//	tbCodiciKV->Add("M05S", "M440S");
	tbCodiciKV->Add("M06B", "M454B");
	tbCodiciKV->Add("M07C", "M451C");
	tbCodiciKV->Add("M07M", "M451M");
	tbCodiciKV->Add("M07S", "M451S");
	tbCodiciKV->Add("M08D", "M517D");
	tbCodiciKV->Add("M08P", "M510P");
	tbCodiciKV->Add("M09A", "M500A");
	tbCodiciKV->Add("M51M", "M463M");
	tbCodiciKV->Add("M51N", "M464N");
	tbCodiciKV->Add("M51W", "M463W");
	tbCodiciKV->Add("N01C", "N410C");
	//    tbCodiciKV->Add("N01M", "N461M");
	tbCodiciKV->Add("N01M", "N463M");
	//    tbCodiciKV->Add("N01N", "N461N"); NO. Errore se esiste
	//    tbCodiciKV->Add("N01S", "N461S");
	tbCodiciKV->Add("N01S", "N463S");
	//    tbCodiciKV->Add("N01W", "N461W");
	tbCodiciKV->Add("N01W", "N463W");
	tbCodiciKV->Add("N06B", "N454B");
	tbCodiciKV->Add("N08D", "N517D");
	tbCodiciKV->Add("N08P", "N510P");
	tbCodiciKV->Add("N09A", "N500A");
	tbCodiciKV->Add("S01C", "S410C");
	tbCodiciKV->Add("S02S", "S422S");
	tbCodiciKV->Add("S04S", "S430S");
	tbCodiciKV->Add("S05S", "S440S");
	tbCodiciKV->Add("S07S", "S451S");
	tbCodiciKV->Add("S08D", "S517D");
	tbCodiciKV->Add("S08P", "S510P");
	//    tbCodiciKV->Add("S09A", "S500A");
	tbCodiciKV->Add("S09A", "S530A");
	tbCodiciKV->Add("S41S", "S434S");
	tbCodiciKV->Add("S42S", "S447S");
	tbCodiciKV->Add("S43S", "S431S");
	tbCodiciKV->Add("S51N", "S464N");
	tbCodiciKV->Add("T06B", "T454B");
	tbCodiciKV->Add("T08D", "T517D");
	tbCodiciKV->Add("T08P", "T510P");
	tbCodiciKV->Add("T09A", "T500A");
	tbCodiciKV->Add("W01C", "W410C");
	tbCodiciKV->Add("W01M", "W461M");
	tbCodiciKV->Add("W01S", "W461S");
	tbCodiciKV->Add("W03T", "W423T");
	tbCodiciKV->Add("W08D", "W517D");
	tbCodiciKV->Add("W09A", "W500A");
	tbCodiciKV->Add("W51N", "W464N");
} // end initLicr


Marc4cppLegami::~Marc4cppLegami() {
	stop();
}

void Marc4cppLegami::stop() {
	clearLegamiVector();
	delete legamiVectorKV;

	//	clearTbCodiciVEctor();
	delete tbCodiciKV;

	//	if (trTitAut)
	//		delete trTitAut;
	//	if (trTitBib)
	//		delete trTitBib;
}

void Marc4cppLegami::clearTbCodiciVEctor() {
	TbCodici *tc;
	if (tbCodiciKV) {

		for (int i = 0; i < tbCodiciKV->Length(); i++) {
			tc = (TbCodici *) tbCodiciKV->GetValue(i);
			delete tc;
		}
		tbCodiciKV->Clear();
	}
} // End Marc4cppLegami::clearLegamiVector

void Marc4cppLegami::clearLegamiVector() {
	DataField *df;
	if (legamiVectorKV) {
		// Dobbiamo rimuovere i DataFields else MEMORY LEAK
		for (int i = 0; i < legamiVectorKV->Length(); i++) {
			df = (DataField *) legamiVectorKV->GetValue(i);
			delete df;
		}
		legamiVectorKV->Clear();
	}
} // End Marc4cppLegami::clearLegamiVector


void Marc4cppLegami::getEntityId(char *entryReticolo, char *bid) {
	char *ptr = strchr(entryReticolo, ':');
	ptr = strchr(ptr + 1, ':');

	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = ptr+1;
	MACRO_COPY_FAST(10);
//	memcpy(bid, ptr + 1, 10);
}

bool Marc4cppLegami::isModerno() {
	//	const char *bid = tbTitolo->getField(tbTitolo->bid);
	//	if (*(bid+3) == 'E') // Antico
	//		return false;
	//	else
	//		return true; // Moderno

	const char *tpMateriale = tbTitolo->getField(tbTitolo->tp_materiale);
	if (*tpMateriale != TP_MATERIALE_ANTICO_E) // Antico
		return true;
	else
		return false; // Moderno


}
/*
bool Marc4cppLegami::isAntico() {
	//	const char *bid = tbTitolo->getField(tbTitolo->bid);
	//	if (*(bid+3) == 'E') // Antico
	//		return true;
	//	else
	//		return false; // Moderno
	const char *tpMateriale = tbTitolo->getField(tbTitolo->tp_materiale);
	if (*tpMateriale == TP_MATERIALE_ANTICO) // Antico
		return true;
	else
		return false; // Moderno
}
*/
bool Marc4cppLegami::isAnticoE(const char *bid)
{
//	char *bid = tbTitolo->getField(tbTitolo->bid);
	if (*(bid+3) == 'E') // Antico
		return true;
	else
		return false; // Moderno
}

bool Marc4cppLegami::isAutore(const char *id)
{
//	char *bid = tbTitolo->getField(tbTitolo->bid);
	if (*(id+3) == 'V') //
		return true;
	else
		return false;
}



// 6-- Subject Analysis Bloc
//	606	Soggetti normali	// Subject headings
void Marc4cppLegami::creaLegamiTitoloSoggetto() {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	DataField *df;

	// Cicliamo sul figli di root per cercare i soggetti
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');

	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);

//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "SOG children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia

		if ((pos = str.find(':')) != string::npos) {
			if (str.find("SOG:", pos + 1) != string::npos) {
				//				std::cout << str << std::endl;

				//entryReticoloPtr = (char *)str.data();
				df = creaLegameTitoloSoggetto((char *) str.data(), pos);
				if (df)
					marcRecord->addDataField(df);

//				if (DATABASE_ID == DATABASE_SBNWEB && IS_TAG_TO_GENERATE(696))
				if (IS_TAG_TO_GENERATE(696))
				{
					df = creaLegameDescrittoreDescrittore();
					if (df)
						marcRecord->addDataField(df);
				}

				if (IS_TAG_TO_GENERATE(699)) // DATABASE_ID == DATABASE_SBNWEB &&
				{
					creaLegameSoggettoVariante();
//					df =
//					if (df)
//						marcRecord->addDataField(df);
				}

			}
		}
		++ch;
	}
	//	std::cout << std::endl;
} // end creaLegamiTitoloSoggetto


void Marc4cppLegami::creaLegamiTitoloClassificazione() {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	DataField *df;
	//printf ("\ncreaLegamiTitoloClassificazione");

	// Cicliamo sul figli di root per cercare le classi
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');

	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);
//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "CLA children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia

		if ((pos = str.find(':')) != string::npos) {
			if (str.find("CLA:", pos + 1) != string::npos) {
				//				std::cout << str << std::endl;

				//entryReticoloPtr = (char *)str.data();
				if (df = creaLegameTitoloClasse((char *) str.data(), pos))
					marcRecord->addDataField(df);
			}
		}
		++ch;
	}
	//	std::cout << std::endl;
} // creaLegamiTitoloClassificazione


void Marc4cppLegami::creaLegamiTitoloMarca() {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	DataField *df;

	// Cicliamo sul figli di root per cercare gli autori
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');

	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);
//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "MAR children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia

		if ((pos = str.find(':')) != string::npos) {
			if (str.find("MAR:", pos + 1) != string::npos) {
				//				std::cout << str << std::endl;
				df = creaLegameTitoloMarca((char *) str.data(), pos);
				if (df)
					marcRecord->addDataField(df);
			}
		}
		++ch;
	}
	//	std::cout << std::endl;

} // End Marc4cppLegami::creaLegamiTitoloMarca


DataField * Marc4cppLegami::creaLegameTitoloMarca(char *entryReticoloPtr,
		int pos) {
	bool retb;

	DataField *df = 0;
	if (!IS_TAG_TO_GENERATE(921))
		return (df);


	// Prendiamo la c�lasse per accedere alla tbClasse
	char mid[10 + 1];
	mid[10] = 0;

	char *BufTailPtr, *aString;
	BufTailPtr = mid;
	aString = entryReticoloPtr + pos + 5;
	MACRO_COPY_FAST(10);
//	memcpy(mid, entryReticoloPtr + pos + 5, 10);

	retb = tbMarca->loadRecord(mid);
	if (!retb)
		return 0; // Record non trovato

	df = creaTag921_Marca();

	return df;
} // End Marc4cppLegami::creaLegameTitoloClasse


DataField * Marc4cppLegami::creaLegameAutoreAutore(char *entryReticoloPtr,	int pos, char *vidPadre) {
	bool retb;
	DataField *df = 0;
	//	DataField *dfDup = 0;

	// Prendiamo la c�lasse per accedere alla tbClasse
	char vid[10 + 1];
	vid[10] = 0;
	char tipoLegame = *(entryReticoloPtr + pos + 5 + 10 + 1);

	char *BufTailPtr, *aString;
	BufTailPtr = vid;
	aString = entryReticoloPtr + pos + 5;
	MACRO_COPY_FAST(10);
//	memcpy(vid, entryReticoloPtr + pos + 5, 10);

	if (true) {

		retb = tbAutore->loadRecord(vid);
		if (!retb)
			return 0; // Record non trovato

		if (*tbAutore->getField(tbAutore->tp_nome_aut) == 'A'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'B'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'C'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'D')
			{
			if (IS_TAG_TO_GENERATE(790))
				df = creaTag790_RinvioAutorePersonale(tipoLegame, vidPadre);
			}
		else
		{
			if (IS_TAG_TO_GENERATE(791))
				df = creaTag791_RinvioAutoreCollettivo(tipoLegame, vidPadre);
		}
	}
	return df;
} // End Marc4cppLegami::creaLegameAutoreAutore






















DataField * Marc4cppLegami::creaLegameTitoloSoggetto(char *entryReticoloPtr,int pos) {
	bool retb;
	DataField *df = 0;
	if (!IS_TAG_TO_GENERATE(606))
		return df;

	// Prendiamo il CID per accedere alla tbSoggetto
	char cid[10 + 1];
	cid[10] = 0;
	//	char bid[10+1];	bid[10]=0;

	memcpy(cid, entryReticoloPtr + pos + 5, 10);

	retb = tbSoggetto->loadRecord(cid);
	if (!retb)
		return 0; // Record non trovato

	//	char *ptr = strchr (entryReticoloPtr, ':');
	//	memcpy (bid, ptr+5, 10);


	//	if (isAntico(rootBid))
	//		df = creaTag620LuogoDiPubblicazione();
	//	else
	df = creaTag606_SoggettiOrdinari();

	return df;
} // End Marc4cppLegami::creaLegameTitoloSoggetto


//	620	Luogo di pubblicazione normalizzato (Libro Antico)
void Marc4cppLegami::creaLegamiTitoloLuogo() {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	DataField *df;

	// Cicliamo sul figli di root per cercare i soggetti
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');
	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString =  (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);

	//memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "SOG children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia
		pos = str.find(':');
		if (pos != string::npos) {
			if (str.find("LUO:", pos + 1) != string::npos) {
				//				std::cout << str << std::endl;
				df = creaLegameTitoloLuogo((char *) str.data(), pos);
				if (df)
					marcRecord->addDataField(df);
			}
		}
		++ch;
	}
	//	std::cout << std::endl;
} // end creaLegamiTitoloLuogo


DataField * Marc4cppLegami::creaLegameTitoloLuogo(char *entryReticoloPtr,
		int pos) {
	bool retb;
	DataField *df = 0;
	if (!IS_TAG_TO_GENERATE(620))
		return df;

	// Prendiamo il CID per accedere alla tbSoggetto
	char lid[10 + 1];
	lid[10] = 0;
	//	char bid[10+1];	bid[10]=0;

	char *BufTailPtr, *aString;
	BufTailPtr = lid;
	aString =  entryReticoloPtr + pos + 5;
	MACRO_COPY_FAST(10);

	//memcpy(lid, entryReticoloPtr + pos + 5, 10);

	retb = tbLuogo->loadRecord(lid);
	if (!retb)
		return 0; // Record non trovato

	df = creaTag620_LuogoDiPubblicazione();

	return df;
} // End Marc4cppLegami::creaLegameTitoloLuogo





DataField * Marc4cppLegami::creaLegameTitoloClasse(char *entryReticoloPtr, int pos) {
	bool retb;
	DataField *df = 0;
	//	DataField *dfDup = 0;

	// Prendiamo la c�lasse per accedere alla tbClasse
//	char classe[34 + 1];
//	classe[34] = 0;
	char classe[36 + 1]; // 02/02/2015
	classe[36] = 0;

	//	memcpy(classe, "D21055.1                          ", 34); 28/05/13 problema lettee accentate in indice
//	memcpy(classe, entryReticoloPtr + pos + 5, 34);


	memcpy(classe, entryReticoloPtr + pos + 5, 36); // 02/02/2015

	retb = tbClasse->loadRecord(classe);
	if (!retb)
		return 0; // Record non trovato

//	if (true) // isDewey()

	if (!polo.Equals("CFI") ) // 31/08/2011 Mantis 4608`
	{
		if (IS_TAG_TO_GENERATE(676))
			df = creaTag676_ClassificazioneDecimaleDewey();
	}
	else
	{

//tbClasse->dumpRecord();


		char *cdSistema =  tbClasse->getField(tbClasse->cd_sistema);

		if (*tbClasse->getField(tbClasse->cd_sistema) == 'D')
			{
			char secondchar = *(cdSistema+1);

			if (!secondchar)
			{ // INDICE  24/02/14. Indice ha codice dewey di un singolo carattere. Mantis 5512
				if (IS_TAG_TO_GENERATE(676))
					df = creaTag676_ClassificazioneDecimaleDewey();
			}
			else
			{ // POLO // 20/01/2014 Fix export NON DEWEY  per polo
				if (secondchar >= '0' && secondchar <= '9')
				{
					if (IS_TAG_TO_GENERATE(676))
						df = creaTag676_ClassificazioneDecimaleDewey();
				}
				else
				{
					if (IS_TAG_TO_GENERATE(686))
						df = creaTag686_AltraClassificazione();
				}

			}
			}
		else
			{
			if (IS_TAG_TO_GENERATE(686))
				df = creaTag686_AltraClassificazione();
			}
	}
	return df;
} // End Marc4cppLegami::creaLegameTitoloClasse










/*
 * Cerca l'autore con responsabilita principale nel dato nodo
 * iterate over children only
 */
int Marc4cppLegami::getVidAutoreResponsabilitaPrincipale(
		const tree<std::string>& tr, CString &autorePrincipale) {
	char id[10 + 1];
	id[10] = 0;
	//	char responsabilita;
	string str;
	int pos = -1;
	//	char *entryReticoloPtr;

	//	tree<std::string>::pre_order_iterator it=tr.begin();
	//	std::cout << "children of :" << *it << std::endl;
	//printf ("..\n");
	tree<std::string>::sibling_iterator ch = tr.begin().begin(); //h1
	while (ch != tr.end().end()) { // h1
		//	   std::cout << (*ch) << std::endl;
		str = *ch;
		if ((pos = str.find(":AUT:")) != -1) {
			if (str.at(pos + 16) == '1') // Controlla se responsabilita principale
			{
				autorePrincipale = (char *) str.data();
				return pos;
			}
		}
		++ch;
	}
	return pos;
} // End haResponsabilitaPrincipale


void Marc4cppLegami::contaSottoLivelliTitolo(const tree<std::string> &reticolo,
		int *sottolivello) {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	//	DataField *df;

	// Cicliamo sul figli di root per cercare i titoli
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');

	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);
	//memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "SOG children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia

		if ((pos = str.find(':')) != string::npos) {
			if (str.find("TIT:", pos + 1) != string::npos) {
				(*sottolivello)++;
				contaSottoLivelliTitolo(ch, sottolivello);
				return; // Ci fermiamo al primo nodo incontrato
			}
		}
		++ch;
	}
	//	std::cout << std::endl;

}


char* Marc4cppLegami::getSottoLivelloTitolo410(
		const tree<std::string> &reticolo, int curSottolivello,
		int sottoLivello) {
	string str;
	unsigned int pos;
	//	char bid[10 + 1];
	//	bid[10] = 0;
	//	DataField *df;

	// Cicliamo sul figli di root per cercare i titoli
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	//	int bidStart = str.find_last_of(':');
	//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "SOG children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	if (sottoLivello == curSottolivello) {
		ch = reticolo.begin();
		str = *ch;
		return (char*) str.data(); // e' stato richiesta root
	}
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		if ((pos = str.find(':')) != string::npos) {
			if (str.find("TIT:", pos + 1) != string::npos) {
				curSottolivello++;
				if (sottoLivello == curSottolivello)
					return (char*)str.data();
				return getSottoLivelloTitolo410(ch, curSottolivello,
						sottoLivello);
			}
		}
		++ch;
	}
	//	std::cout << std::endl;
	return 0;
} // end getSottoLivelloTitolo410



void Marc4cppLegami::print_tree(const tree<std::string>& tr,
		tree<std::string>::pre_order_iterator it,
		tree<std::string>::pre_order_iterator end) {
	if (!tr.is_valid(it))
		return;
	int rootdepth = tr.depth(it);
	std::cout << "-----" << std::endl;
	while (it != end) {
		for (int i = 0; i < tr.depth(it) - rootdepth; ++i)
			std::cout << "  ";
		std::cout << (*it) << std::endl << std::flush;
		++it;
	}
	std::cout << "-----" << std::endl;
}



void Marc4cppLegami::addAutoreConResponsabilitaPrincipale(DataField *df,
		const tree<std::string>& reticolo) {
	Subfield *sf;
	// Troviamo l'autore con responsabilita' principale
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); // Primo figlio di root
	string str;
	int pos;
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia
		if ((pos = str.find(':')) != string::npos) {
			if (str.find("AUT:", pos + 1) != string::npos) {
				// Prendiamo il BID per accedere alla tbTitolo
				TbReticoloAut *tbReticoloAut = new TbReticoloAut( (char *) str.data() + pos + 5);

				if (*tbReticoloAut->getField(tbReticoloAut->tp_responsabilita)
						== TP_RESP_1_RESPONSABILITA_PRINCIPALE) {
					// Carichiamo l'autore con responsabilita' principale
					char *vid = tbReticoloAut->getField(tbReticoloAut->vid);
					if (tbAutore->loadRecord(vid)) {
						CString *s = tbAutore->getFieldString(
								tbAutore->ds_nome_aut);
						s->removeCharacterOccurances('*');
						sf = new Subfield('f', s);
						//sf->setData();
						df->addSubfield(sf);
					}
					delete tbReticoloAut;
					break;
				}
				delete tbReticoloAut;
			}// End if "AUT:"
		}// End if ':'
		++ch;
	} // end while
} // End Marc4cppLegami::addAutoreConResponsabilitaPrincipale




// iterate over children only
bool Marc4cppLegami::haResponsabilitaPrincipale(const tree<std::string>& tr) // print_children
{
	//	char responsabilita;
	string str;
	int pos;
	//	char *entryReticoloPtr;

	//	tree<std::string>::pre_order_iterator it=tr.begin();
	//	std::cout << "children of :" << *it << std::endl;
	//printf ("..\n");
	tree<std::string>::sibling_iterator ch = tr.begin().begin(); //h1
	while (ch != tr.end().end()) { // h1
		//	   std::cout << (*ch) << std::endl;
		str = *ch;
		if ((pos = str.find("AUT:")) != -1) {
			// Prendiamo il BID per accedere alla tbTitolo
			//			entryReticoloPtr = (char *)str.data();
			//			responsabilita = *(entryReticoloPtr+pos+15);
			//			if (responsabilita == '1')
			//			responsabilita = str.at(pos+15);
			if (str.at(pos + 15) == '1') // Controlla se responsabilita principale
				return true;
		}
		++ch;
	}
	return false;
} // End haResponsabilitaPrincipale


// iterate over children only
bool Marc4cppLegami::haUnAutore(const tree<std::string>& tr, char *respPtr) // print_children
{
	//	char responsabilita;
	string str;
	int pos;
	//	char *entryReticoloPtr;

	//	tree<std::string>::pre_order_iterator it=tr.begin();
	//	std::cout << "children of :" << *it << std::endl;
	//printf ("..\n");
	tree<std::string>::sibling_iterator ch = tr.begin().begin(); //h1
	while (ch != tr.end().end()) { // h1
		//	   std::cout << (*ch) << std::endl;
		str = *ch;
		if ((pos = str.find("AUT:")) != -1) {
			// Prendiamo il BID per accedere alla tbTitolo
			//			entryReticoloPtr = (char *)str.data();
			//			responsabilita = *(entryReticoloPtr+pos+15);
			//			if (responsabilita == '1')
			//			responsabilita = str.at(pos+15);

			//			if (str.at(pos + 15) == '1') // Controlla se responsabilita principale
			*respPtr = str.at(pos + 15);
			return true;
		}
		++ch;
	}
	return false;
} // End haResponsabilitaPrincipale

bool Marc4cppLegami::haResponsabilita1o2(const tree<std::string>& tr) // print_children
{
	//	char responsabilita;
	string str;
	int pos;
	//	char *entryReticoloPtr;

	//	tree<std::string>::pre_order_iterator it=tr.begin();
	//	std::cout << "children of :" << *it << std::endl;
	//printf ("..\n");
	tree<std::string>::sibling_iterator ch = tr.begin().begin(); //h1
	while (ch != tr.end().end()) { // h1
		//	   std::cout << (*ch) << std::endl;
		str = *ch;
		if ((pos = str.find("AUT:")) != -1) {
			// Prendiamo il BID per accedere alla tbTitolo
			//			entryReticoloPtr = (char *)str.data();
			//			responsabilita = *(entryReticoloPtr+pos+15);
			//			if (responsabilita == '1')
			//			responsabilita = str.at(pos+15);
			if (str.at(pos + 15) == '1' || str.at(pos + 15) == '2') // Controlla se responsabilita principale
				return true;
		}
		++ch;
	}
	return false;
} // End haResponsabilitaPrincipale





DataField * Marc4cppLegami::creaLegameTitoloAutore(char *vid,
		char tipoResponsabilita, CString * cdRelazione, CString *cdStrumentoMusicale) {
	DataField *df = 0;
	bool retb;
	retb = tbAutore->loadRecord(vid);
	if (!retb)
		return 0; // Record non trovato

	if (tipoResponsabilita == TP_RESP_1_RESPONSABILITA_PRINCIPALE) {
		if (*tbAutore->getField(tbAutore->tp_nome_aut) == 'A'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'B'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'C'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'D')
			{
			if (IS_TAG_TO_GENERATE(700))
				df = creaTag700_NomiPersonaliResponsabilitaPrincipale(cdRelazione, cdStrumentoMusicale);
			}
		else
			{
			if (IS_TAG_TO_GENERATE(710))
				df = creaTag710_NomeDiEnte_ResponsabilitaPrincipale(cdRelazione, cdStrumentoMusicale);
			}
	} else if (tipoResponsabilita == TP_RESP_2_RESPONSABILITA_ALTERNATIVA) {
		if (*tbAutore->getField(tbAutore->tp_nome_aut) == 'A'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'B'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'C'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'D')
			{
			if (IS_TAG_TO_GENERATE(701))
				df = creaTag701_NomiPersonaliResponsabilitaAlternativa(cdRelazione, cdStrumentoMusicale);
			}
		else
			{
			if (IS_TAG_TO_GENERATE(711))
				df = creaTag711_NomeDiEnte_ResponsabilitaAlternativa(cdRelazione, cdStrumentoMusicale);
			}
	} else if (tipoResponsabilita == TP_RESP_3_RESPONSABILITA_SECONDARIA
			|| tipoResponsabilita == TP_RESP_4_RESPONSABILITA_NELLA_PRODUZIONE_MATERIALE
			|| tipoResponsabilita == TP_RESP_0_NOME_CITATO_NEL_DOCUMENTO) // Documenti musicali. 26/01/2010 11.10 Contardi

	{
		if (*tbAutore->getField(tbAutore->tp_nome_aut) == 'A'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'B'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'C'
				|| *tbAutore->getField(tbAutore->tp_nome_aut) == 'D')
//			df = creaTag702_NomiPersonaliResponsabilitaSecondaria(cdRelazione); // 05/08/2010
			{
			if (IS_TAG_TO_GENERATE(702))
//				df = creaTag702_NomiPersonaliResponsabilitaSecondaria(cdRelazione, tipoResponsabilita); // 26/04/2011
				df = creaTag702_NomiPersonaliResponsabilitaSecondaria(cdRelazione, tipoResponsabilita, cdStrumentoMusicale); // 23/01/2015

			}
		else
//			df = creaTag712_NomeDiEnte_ResponsabilitaSecondaria(cdRelazione);
			{
			if (IS_TAG_TO_GENERATE(712))
				df = creaTag712_NomeDiEnte_ResponsabilitaSecondaria(cdRelazione, tipoResponsabilita, cdStrumentoMusicale);
			}
	} else {
		SignalAWarning(__FILE__, __LINE__,
				"Tipo responsabilita' %c sconosciuto", tipoResponsabilita); // *(entryReticoloPtr + pos + 5 + 11)
		return 0;
	}
	return df;
} // End creaLegameTitoloAutore








void Marc4cppLegami::contaOccorrenzeBidInReticoloPer410(
		const tree<std::string> &reticolo, char *bidToSearch, int *occorrenze,
		CKeyValueVector *legamiTitTit410KV, bool root) {
	string str;
	unsigned int pos;
	char bidColl[10 + 1];
	bidColl[10] = 0;
	char *entryReticoloPtr;

	// Cicliamo sul figli di root per cercare i titoli
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	//	int bidStart = str.find_last_of(':');
	//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "SOG children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	char *BufTailPtr, *aString;
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia
		if ((pos = str.find(':')) != string::npos) {
			if (str.find("TIT:", pos + 1) != string::npos) {
				// Prendiamo il BID coll
				entryReticoloPtr = (char *) str.data();

				BufTailPtr = bidColl;
				aString =  entryReticoloPtr + pos + 5;
				MACRO_COPY_FAST(10);
//				memcpy(bidColl, entryReticoloPtr + pos + 5, 10);

				// Cerchiamo solo nei legami che al primo livello sono nella lista delle 410
				if (!strcmp(bidToSearch, bidColl))
					(*occorrenze)++;
				if (!root || (root
						&& (char*) legamiTitTit410KV->GetValueFromKey(bidColl)))
					contaOccorrenzeBidInReticoloPer410(ch, bidToSearch,
							occorrenze, legamiTitTit410KV, false);
			}
		}
		++ch;
	}
	//	std::cout << std::endl;
} // End contaOccorrenzeBidInReticoloPer410





/*
 *
 *
 */
int Marc4cppLegami::getVidFirstAutore(const tree<std::string>& tr,
		CString &autore) {
	char id[10 + 1];
	id[10] = 0;
	//	char responsabilita;
	string str;
	int pos = -1;
	tree<std::string>::sibling_iterator ch = tr.begin().begin(); //h1
	while (ch != tr.end().end()) { // h1
		//	   std::cout << (*ch) << std::endl;
		str = *ch;
		if ((pos = str.find(":AUT:")) != -1) {
			autore.assign((char *) str.data(), str.length());
			return pos;
		}
		++ch;
	}
	return pos;
} // End getFirstVidAutore

















void Marc4cppLegami::creaLegamiTitoloAutore() {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	DataField *df;

	// Cicliamo sul figli di root per cercare gli autori
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');
	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);
//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "AUT children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia

		if ((pos = str.find(':')) != string::npos) {
			if (str.find("AUT:", pos + 1) != string::npos) {
				//				std::cout << str << std::endl;
				//entryReticoloPtr = (char *)str.data();
				if (df = creaLegameTitoloAutore((char *) str.data(), pos)) {
					marcRecord->addDataField(df);

					if (IS_TAG_TO_GENERATE(314)) {
						// Creiamo la nota del legame tra il titolo e l'autore se esiste
						if (df = creaTag314_NotaLegameTitoloAutore(bid,(char *) str.data(), pos))
							marcRecord->addDataField(df);
					}
				}
			}
		}
		++ch;
	}
	//	std::cout << std::endl;

} // End Marc4cppLegami::creaLegamiTitoloAutore

DataField * Marc4cppLegami::creaLegameTitoloAutore(char *entryReticoloPtr,
		int pos) {
	DataField *df = 0;

	// Prendiamo il VID per accedere alla tbAutore
	char vid[10 + 1];
	vid[10] = 0;
	CString cdRelazione, cdStrumentoMusicale;

	char *BufTailPtr, *aString;
	BufTailPtr = vid;
	aString =  entryReticoloPtr + pos + 5;
	MACRO_COPY_FAST(10);

//	memcpy(vid, entryReticoloPtr + pos + 5, 10);

	//memcpy(cdRelazione, entryReticoloPtr + pos + 5 + 11 + 1 + 1, 3);
	int posCdRelazione = pos + 5 + 11 + 1 + 1;
	cdRelazione.AppendString( entryReticoloPtr + posCdRelazione, 3);

	int posCdStrumentoMus = posCdRelazione+7+1;
	if (*(entryReticoloPtr+posCdStrumentoMus) != ',')
	{
		cdStrumentoMusicale = entryReticoloPtr+posCdStrumentoMus;

//		if (cdStrumentoMusicale.GetLastChar() == '\n') // 06/03/2015 problema della , in piu' (mail Roveri)
//			cdStrumentoMusicale.CropRightBy(2); // remove ',' + LF
//		else
//			cdStrumentoMusicale.ExtractLastChar(); // remove ','

		if (cdStrumentoMusicale.GetLastChar() == '\n') // 16/04/2015 mantis 5851 polo PIS
		{
			cdStrumentoMusicale.ExtractLastChar();
			if (cdStrumentoMusicale.GetLastChar() == ',')
				cdStrumentoMusicale.ExtractLastChar();
		}
		else if (cdStrumentoMusicale.GetLastChar() == ',') // 20/04/2015 mail Roveri
			cdStrumentoMusicale.ExtractLastChar();
	}

	// Legame da fare
	df = creaLegameTitoloAutore(vid, *(entryReticoloPtr + pos + 5 + 11), &cdRelazione, &cdStrumentoMusicale);
	return df;
} // End Marc4cppLegami::creaLegameTitoloAutore







bool Marc4cppLegami::elaboraDatiLegamiAlDocumento(
		const tree<std::string>& reticolo) {
	this->reticolo = reticolo;
	clearLegamiVector();
	tree<std::string>::pre_order_iterator it = reticolo.begin();

//tbTitolo->dumpRecord();



	rootBid[10] = 0;
	//    char * bid;
	CString rootRecord;
	rootRecord = tbTitolo->getStringRecordData();

	string str;
	char* entryReticolo;
	str = *it;
	entryReticolo = (char*) str.data();

	getEntityId(entryReticolo, rootBid);

//printf ("\nelaboraDatiLegamiAlDocumento per bid %s", rootBid);



	//  311 Note riguardanti i campi legame
	//	314	Note relative alla responsabilit� intellettuale (note al legame titolo-autore)


	// 4-- Linking Entry Block
	// Troviamo i titoli legati al titolo (root)
	//  410 Collana - Campi legame
	//  422 Unit� "padre" del supplemento
	//	423	Pubblicato con
	//	430 Continua in
	// 	431	Continua in parte in
	//	434	Assorbito da
	//	440	Continuato da
	//	447	Si fonde con xxx per formare
	//	451	Altra edizione nello stesso supporto
	//	454	Traduzione di
	//	461	Legame al livello padre (fa parte di)
	//	462 Legame al livello figlio (comprende?)
	//	463	Legame all'unit� singola
	//	464	Legame all'unit� singola analitica (si riferisce agli spogli)

	// 5-- Related Title Block
	//	500	Titolo uniforme			// Uniform Titles
	//	506	Titolo uniforme			// Titolo dell'Opera 02/03/2017
	//	510	Titolo parallelo		// Variant titles
	//	517	Altre varianti del titolo
	//	530	Titolo chiave (periodici) // Other related titles



	creaLegamiTitoloTitolo();
//printf ("\nTrTitBib sep='%s' dopo creaLegamiTitoloTitolo()", trTitBib->getRecordSeparator()->data());

	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root

	// 6-- Subject Analysis Bloc
	//	606	Soggetti normali	// Subject headings
	//	620	Luogo di pubblicazione normalizzato (Libro Antico)
	creaLegamiTitoloSoggetto();
//printf ("\nTrTitBib sep='%s' dopo creaLegamiTitoloSoggetto()", trTitBib->getRecordSeparator()->data());

	//printf ("\ncreaLegamiTitoloSoggetto()");
	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root


	//	676 Classificazione Decimale Dewey	// Classification
	//	686	Altra classificazione
	creaLegamiTitoloClassificazione();
//printf ("\nTrTitBib sep='%s' dopo creaLegamiTitoloClassificazione()", trTitBib->getRecordSeparator()->data());

	//printf ("\ncreaLegamiTitoloClassificazione)");
	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root



	// 7-- Intellectual Responsibility Block
	// Troviamo gli autori legati al titolo (root)
	//	700 Autori personali - Responsabilit� principale	// Personal Names
	//	701 Autori personali - Responsabilit� alternativa
	//	702 Autori personali - Responsabilit� secondaria

	//	710	Autori collettivi - Responsabilit� principale	// Corporate Body and Meeting Names
	//	711	Autori collettivi - Responsabilit� alternativa
	//	712	Autori collettivi - Responsabilit� secondaria

	creaLegamiTitoloAutore();
//printf ("\ncreaLegamiTitoloAutore)");
	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root
//printf ("\nTrTitBib sep='%s' dopo creaLegamiTitoloAutore()", trTitBib->getRecordSeparator()->data());

	if (DATABASE_ID == DATABASE_SBNWEB && IS_TAG_TO_GENERATE(689))
		creaLegamiTitoliTesauro();

	//	790 Personal name � Alternative form
	//	791 Corporate body name � Alternative form
	creaLegamiAutoreAutore();
//printf ("\ncreaLegamiAutoreAutore)");
	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root
//printf ("\nTrTitBib sep='%s' dopo creaLegamiAutoreAutore()", trTitBib->getRecordSeparator()->data());


	// 899
	creaLegamiTitoloBiblioteca();
//printf ("\ncreaLegamiTitoloBiblioteca)");
	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root
//printf ("\nTrTitBib sep='%s' dopo creaLegamiTitoloBiblioteca()", trTitBib->getRecordSeparator()->data());



	//	921
	creaLegamiTitoloMarca();
	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root

	creaLegamiTitoloLuogo();
	if (strcmp(rootBid, tbTitolo->getField(tbTitolo->bid)))
		tbTitolo->loadRecordFromString(rootRecord.data(), rootRecord.Length()); // Ripristiniamo titolo di root

//tbTitolo->dumpRecord();
	char tipoMateriale = *(tbTitolo->getField(tbTitolo->tp_materiale));

	//	printf ("\ntbTitolo->getField(tbTitolo->bid)=%s", tbTitolo->getField(tbTitolo->bid));

/**
	if (tipoMateriale == TP_MATERIALE_MUSICA ) {
		// 922 RAPPRESENTAZIONE (MAT. MUSICALE)
		//if (tbRappresent->loadRecord(rootBid))
		if (IS_TAG_TO_GENERATE(922))
			if (tbRappresent->existsRecord(rootBid))
				if (tbRappresent->loadNextRecord())
						creaTag922_Rappresentazione();

		// 923 NOTAZIONE (MAT. MUSICALE)

		//	printf ("\nargepf creaTag923_Notazione");
		if (IS_TAG_TO_GENERATE(923))
			if (tbMusica->loadRecord(rootBid))
				creaTag923_Notazione();

		// 926 INCIPIT (MAT. MUSICALE)
		// printf ("\nargepf creaLegamiTitoloIncipit");
		creaLegamiTitoloIncipit();

		// 927 Personaggi e interpreti (MAT. MUSICALE)
		//printf ("\nargepf creaTag927_PersonaggiEInterpreti");

		if (IS_TAG_TO_GENERATE(927))
		{
			if (tbPersonaggio->existsRecordNonUnique(rootBid)) {
			while (tbPersonaggio->loadNextRecord(rootBid))
				creaTag927_PersonaggiEInterpreti();
		}
		}
*/
	if ((tipoMateriale == TP_MATERIALE_MUSICA_U
		|| TP_MATERIALE_AUDIOVISIVO_H
		|| TP_MATERIALE_MODERNO_M	// 30/3/2016
		|| TP_MATERIALE_ANTICO_E	// 30/3/2016
		)
		&& 	IS_TAG_TO_GENERATE(922)
		&& tbRappresent->existsRecord(rootBid)
		&& tbRappresent->loadNextRecord()
		)
		creaTag922_Rappresentazione();

	//	printf ("\nargepf creaTag923_Notazione");
	if (tipoMateriale == TP_MATERIALE_MUSICA_U
		&&	IS_TAG_TO_GENERATE(923)
		&& 	tbMusica->loadRecord(rootBid))
			creaTag923_Notazione();

	if (tipoMateriale == TP_MATERIALE_MUSICA_U)
		creaLegamiTitoloIncipit();

	if ((tipoMateriale == TP_MATERIALE_MUSICA_U
		|| TP_MATERIALE_AUDIOVISIVO_H
		|| TP_MATERIALE_MODERNO_M	// 30/3/2016
		|| TP_MATERIALE_ANTICO_E	// 30/3/2016
			)
		&&	IS_TAG_TO_GENERATE(927)
		&& tbPersonaggio->existsRecordNonUnique(rootBid))
		while (tbPersonaggio->loadNextRecord(rootBid))
			creaTag927_PersonaggiEInterpreti();


// 10/08/2010 12.02 Messo assieme a gestione delle 500
//		// 928 Dati codificati per titolo uniforme musicale
//		//printf ("\nargepf creaTag928_TitoloUniformeMusicale");
//
//		if (tbComposizione->existsRecord(rootBid)) { // tbTitolo->getField(tbTitolo->bid)
//			// 20/01/2010 10.30 NB Quando tratto i documenti qui non ci arrivero' mai in quanto la natura e' 'A'!!
//			// Un di' qualcuno mi spiegherea' come arrivarci.
//			tbComposizione->loadNextRecord(rootBid); // tbTitolo->getField(tbTitolo->bid)
//
//			creaTag928_TitoloUniformeMusicale();
//
//			// 929 COMPOSIZIONE (MAT. MUSICALE)
//			//printf ("\nargepf creaTag928_Composizione");
//			creaTag929_Composizione();
//		}

//	}

	// Gestione degli ordini (961)
	elaboraOrdini();


	return true;
} // End Marc4cppLegami::elaboraDatiLegamiAlDocumento


































void  Marc4cppLegami::creaSubtag7xx(DataField *df) {
	Subfield *sf;
	//string str;
	CString s, bid;
	DataField *dfLegame;

	s = df->getSubfield('1')->getData();
	bid = s.SubstringData(4, 10);

	// Troviamo l'autore per il BID
	// Carichiamo il record della tr_tit_aut
	if (!trTitAut->loadRecord(bid.data()))
		return;
	// Prendiamo il VID
	CString vid;
	vid = trTitAut->getField(trTitAut->vid);

	dfLegame = creaLegameTitoloAutore(
			vid.data(),
			*(trTitAut->getField(trTitAut->tp_responsabilita)),
			trTitAut->getFieldString(trTitAut->cd_relazione),
			trTitAut->getFieldString(trTitAut->cd_strumento_mus)
			);
	if (dfLegame) {
		sf = new Subfield('1', dfLegame->toSubTag());
		//char *subtag = dfLegame->toSubTag();
		//sf->setData();
		df->addSubfield(sf);
		delete dfLegame;
	}

} // End Marc4cppLegami::creaSubtag7xx
















void Marc4cppLegami::creaTag70xNew(DataField *df, CString *ds_nome, char tp_nome,	CString *id) {
	Subfield *sf;
	string str;
	//	const char *vid = tbAutore->getField(tbAutore->vid);
	CString s;
//	const char *nomePtr = ds_nome; // tbAutore->getField(tbAutore->ds_nome_aut);
//	ds_nome->ChangeTo("#_", ' ');

	ds_nome->Strip(ds_nome->trailing, ' ');  // Mantis 0004381. 26/04/2010

	char *nomePtr = ds_nome->data(); // tbAutore->getField(tbAutore->ds_nome_aut);

	char *qualificazionePtr = 0;
//	char *endQualificazionePtr = 0;
//	bool qualificazioneIsData = false;
	//	char *datePtr = 0;
	char *commaPtr = 0;
	char *colonPtr = 0;

	df->setIndicator1(' ');
	//const char *tpNome = tbAutore->getField(tbAutore->tp_nome_aut);

	//	if (*tpNome == 'A' || *tpNome == 'a' || *tpNome == 'B' || *tpNome == 'b')
	if (tp_nome == 'A' || tp_nome == 'a' || tp_nome == 'B' || tp_nome == 'b')
		df->setIndicator2('0');
	else
		df->setIndicator2('1');

	qualificazionePtr = strstr(nomePtr, " <");

	if (qualificazionePtr) // Qualificazione parte dele testo?
	{
		int idx = ds_nome->IndexCharFrom('>', 0, CString::forward);
		if (idx < (ds_nome->Length()-1))
			qualificazionePtr = 0;

	}


// 13/09/2010 11.43 Roveri unimarc_11_08_2010.doc
//	if (tp_nome == 'A' || tp_nome == 'a' || tp_nome == 'B' || tp_nome == 'b') {
//		colonPtr = strstr(nomePtr, ": ");
//		if (colonPtr) {
//			*colonPtr = 0;
//			colonPtr++;
//		}
//	}

//	if (tp_nome == 'C' || tp_nome == 'c' || tp_nome == 'D' || tp_nome == 'd')
//		commaPtr = strstr(nomePtr, ", ");
//
//	if (commaPtr)
//		*commaPtr = 0; // EOS
//
//	if (qualificazionePtr) {
//		//		endQualificazionePtr = strchr(qualificazionePtr, '>');
//		*qualificazionePtr = 0;
//		qualificazionePtr += 1;
//
////		qualificazioneIsData = isQualificazioneData(qualificazionePtr);
//	}


    // Gestiamo il no sort
    nomePtr = ds_nome->data();
int pos;
char *ptr;
if ((pos = ds_nome->First('*')) != -1) // abbiamo almeno un asterisco?
{
    if (pos) // Se non primo carattere
    {
        s = NSB.data(); // No sort begin
        s.AppendString(ds_nome->SubstringData(0, pos), pos);
        s.AppendString(&NSE); // No sort end
        ptr = ds_nome->SubstringData(pos + 1);
        s.AppendString(ptr, ds_nome->getBufferSubStringLength());
        nomePtr = s.data();
    } else
    {
        ds_nome->ExtractFirstChar();
        nomePtr = ds_nome->data();
    }
}

	qualificazionePtr = strstr(nomePtr, " <");
	if (qualificazionePtr) // Qualificazione parte dele testo?
	{
		int idx = ds_nome->IndexCharFrom('>', 0, CString::forward);
		if (idx < (ds_nome->Length())) // -1 05/11/2010 14.11
		{
			*qualificazionePtr = 0;
			qualificazionePtr += 1;
		}

	}

	if (tp_nome == 'C' || tp_nome == 'c' || tp_nome == 'D' || tp_nome == 'd')
		commaPtr = strstr(nomePtr, ", ");
	if (commaPtr)
		*commaPtr = 0; // EOS


	sf = new Subfield('a', nomePtr, strlen(nomePtr));
	//sf->setData(); // tbAutore->getField(tbAutore->ds_nome_aut)
	df->addSubfield(sf);

	if (tp_nome == 'C' || tp_nome == 'c' || tp_nome == 'D' || tp_nome == 'd') {
		if (commaPtr) {
			//			//*commaPtr = ','; // rimetti
			//			sf = new Subfield('b'); // Non includere il separatore ", "
			//			sf->setData(commaPtr+2);

			*commaPtr = ','; // rimetti Richiesta CFI 04/02/2010 14.55
			sf = new Subfield('b', commaPtr, strlen(commaPtr));
			//sf->setData();

			df->addSubfield(sf);
		}

	}

	if (qualificazionePtr) {
//		if (qualificazioneIsData) {
//			sf = new Subfield('f'); // data
//			sf->setData(qualificazionePtr);
//			df->addSubfield(sf);
//		} else {
			char *ptrStart = qualificazionePtr;
			char *ptr;
			int ctr = 0;
			char subfieldId;
			while (ptr = strstr(ptrStart, " ; ")) {
				if (ptr)
					*ptr = 0;

				if (*ptrStart == '<')
				{
					subfieldId = getQualificazioneType(ptrStart+1, ctr, tp_nome);
			}
				else
					subfieldId = getQualificazioneType(ptrStart, ctr, tp_nome);

				if (subfieldId)
				{


					if (!ctr) // *ptrStart == '<'
						sf = new Subfield(subfieldId, (char *)" ", 1); // qualificazione
						//sf->appendData();
					else
						sf = new Subfield(subfieldId, (char *)" ; ", 3); // qualificazione
						//sf->appendData();

					sf->appendData(ptrStart); // , ds_nome->data()-ptrStart+ds_nome->Length()
					df->addSubfield(sf);
				}
				*ptr = ' ';
				ptrStart = ptr += 3;
				ctr++;
			}
			// ultima parte
			if (*ptrStart == '<')
				subfieldId = getQualificazioneType(ptrStart+1, ctr, tp_nome);
			else
				subfieldId = getQualificazioneType(ptrStart, ctr, tp_nome);
			if (subfieldId)
			{
				//		sf = new Subfield('c'); // qualificazione
				if (!ctr) // *ptrStart == '<'
					sf = new Subfield(subfieldId, (char *)" ", 1); // qualificazione
					//sf->appendData();
				else
					sf = new Subfield(subfieldId, (char *)" ; ", 3); // qualificazione
				//sf->appendData();
				sf->appendData(ptrStart, strlen(ptrStart)); // , ds_nome->data()-ptrStart+ds_nome->Length()
				df->addSubfield(sf);
			}


//		}
		*(qualificazionePtr - 1) = ' ';
	}

	if ((tp_nome == 'A' || tp_nome == 'a' || tp_nome == 'B' || tp_nome == 'b') && colonPtr) {
		sf = new Subfield('c', colonPtr, strlen(colonPtr)); // qualificazione
		//sf->setData();
		df->addSubfield(sf);
		*(colonPtr - 1) = ':';
	}

	//	if (endQualificazionePtr)
	//		*endQualificazionePtr = '>';


//	sf->setData(id); // tbAutore->getField(tbAutore->vid)


	char *bid = id->data(); // 28/04/10 // const
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('3', id);
		//sf->setData();
	else // TIPO_SCARICO_OPAC
	{
//		CString id = "IT\\ICCU\\";
		CString id((char *)"IT\\ICCU\\", 8);
		id.AppendString(bid, 4); // Prendi il Polo+1
		id.AppendChar('\\');
		id.AppendString(bid+4, 10-4);	 // Prendi resto del bid
		sf = new Subfield('3', &id);
		//sf->setData();
	}


	df->addSubfield(sf);

} // End creaTag70xNew



/*
VERDONE
=======
Autori personali
----------------

A Nome personale in forma diretta il cui gruppo principale e costituito da un solo elemento.
    Ordine delle qualificazioni
    I. Espressione verbale
    2. Numero d'ordine (per i sovrani, papi, etc.), trascritto sempre in cifre arabe seguite da un punto
    3. Specificazione cronologica

    eg. <regina di Gran Bretagna ; 2.>

B Nome personale in forma diretta il cui gruppo principale e costituito da pin elementi.
    Ordine delle qualificazioni

    I. Espressione verbale
    2. Numero d'ordine (per i sovrani, papi, etc.), trascritto sempre in cifre arabe seguite da un  punto
    3. Specificazione cronologica
    eg. <papa; 2.>

C Nome personale in forma inversa il cui gruppo principale e costituito da un solo elemento.
    Ordine delle qualificazioni

    I. Espressione verbale
    2. Numero d'ordine, trascritto sempre in cifre arabe seguite da un punto
    3. Specificazione cronologica

D Nome personale in forma inversa il cui gruppo principale e costituito da pin elementi.

    Ordine delle qualificazioni

    1. Espressione verbale
    2. Numero d'ordine, trascritto sempre in cifre arabe seguite da un punto
    3. Specificazione cronologica


$c Espressione verbale
$d Numero d'ordine non trattato
$f Specificazione cronologica





Autori collettivi
----------------
E Nome di ente.
    Ordine delle qualifi:azioni

    1. Numero d'ordine, trascritto sempre in cifre arabe seguite da un punto
    2. Qualificazione verbale
    3. Qualificazione geografica
    4. Specificazione cronologica

    eg. <Governo provvisorio ; 1958-1962>
        <Italia ; 1831>

R Nome di ente a carattere temporaneo.

    Ordine delle qualificazioni

    ] . Numero d'ordine, trascritto sempre in cifre arabe seguite da un punto
    2. Specificazione cronologica
    3. Qualificazione geografica
    eg. < 1. ; 1981 ; Firenze>

G Nome di ente subordinato.
    Ordine delle qualificazioni

    1. Numero d'ordine, trascritto sempre in cifre arabe seguite da un punto
    2. Qualificazione verbale
    3. Qualificazione geografica
    4. Specificazione cronologica

    eg. <Governo provvisorio ; 1958-1962>
        <Italia ; 1831>

$c Espressione verbale
$d Numero d'ordine non trattato
$e Qualificazione geografica
$f Specificazione cronologica
 *
 */
char Marc4cppLegami::getQualificazioneType(char *parteDiQualificazione, int nth, char tipoNome)
{
//printf ("\nparteDiQualificazione=%s", parteDiQualificazione);

	// Controllo semantico
	char chr = *parteDiQualificazione, subfieldId = 0;

//	if (*qualificazionePtr == '<')
//		qualificazionePtr++;

	int len = strlen(parteDiQualificazione);
	if (!len)
		return subfieldId;

	chr = *(parteDiQualificazione+len-1);
	if (chr == '>')
		chr = *(parteDiQualificazione+len-2);

// Non a questo livello
//	else
//	{// Qualificazione sequita da testo
////		char * ptr = strchr (parteDiQualificazione, '>');
////		if (ptr)
////			chr = *(ptr-1);
//		return 0; // No subfield id
//	}


	if ( CMisc::isDate(parteDiQualificazione))
		subfieldId = 'f'; // Specificazione cronologica

	else if (chr == '.')
	{
		//*(parteDiQualificazione+len-1) = 0; // EOL
		while (*parteDiQualificazione++)
			if (!isdigit(*parteDiQualificazione))
				break;

//		if (!(*parteDiQualificazione) || *parteDiQualificazione == '>')
		if (!(*parteDiQualificazione) || *parteDiQualificazione == '.')
			subfieldId = 'd'; // Numero d'ordine se numerico
		else
		{
//			subfieldId = 'c'; // espressione verbale
			if (!nth) // 13/10/2010
				subfieldId = 'c'; // espressione verbale
			else
			{
				if (tipoNome == 'E' || tipoNome == 'e' || tipoNome == 'R' || tipoNome == 'r' )
					subfieldId = 'e'; // qualificazione geografica
				else
					subfieldId = 'c'; // espressione verbale
			}

		}
	}

	else if (chr != ' ')
	{
		if (!nth)
			subfieldId = 'c'; // espressione verbale
		else
		{
			if (tipoNome == 'E' || tipoNome == 'e' || tipoNome == 'R' || tipoNome == 'r' )
				subfieldId = 'e'; // qualificazione geografica
			else
				subfieldId = 'c'; // espressione verbale
		}
	}
	else
		subfieldId =  0;

//printf (" - subfieldId=%c", subfieldId);
	return subfieldId;


} // end getQualificazioneType


/*
 * A4A - Legami tra autori in forma (A)ccettata
 * A8R - Legami tra autori in forma (A)ccettata e forma di (R)invio
 *
 * Sembra(dicasi sembra) sufficiente il ciclo sui figli dell' autore al primo livello
 */
void Marc4cppLegami::creaLegamiAutoreAutore() {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	DataField *df;

	char vidPadre[10 + 1];
	vidPadre[10] = 0;

	// Cicliamo sul figli di root per cercare gli autori
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');

	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);
//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "AUT children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia
		if ((pos = str.find(':')) != string::npos) {
			if (str.find("AUT:", pos + 1) != string::npos) {
				//				std::cout << std::endl << str ;
				BufTailPtr = vidPadre;
				aString =  (char *) str.data() + pos + 5;
				MACRO_COPY_FAST(10);
//				memcpy(vidPadre, (char *) str.data() + pos + 5, 10);


				// Andiamo a lavorare sui figli (legami 08/04)
				tree<std::string>::pre_order_iterator ch1 = ch.begin(); // .begin()
				while (ch1 != ch.end()) { // h1
					str = *ch1;
					// Troviamo il separatore della gerarchia
					if ((pos = str.find(':')) != string::npos) { // figlio puo' essere solo un legame ad autore
						df = creaLegameAutoreAutore((char *) str.data(), pos, vidPadre);
						if (df)
							marcRecord->addDataField(df);
					}
					++ch1;
				} // End while ch1


			}
		}
		++ch;
	}

} // End Marc4cppLegami::creaLegamiAutoreAutore





void Marc4cppLegami::addAutoreFormaAccettata(DataField *df, char tipoLegame, char *vidPadre) {
	Subfield *sf;
	// Dal vid_coll troviamo il vid dell'autore in forma accettata
	//const char * vid = tbAutore->getField(tbAutore->vid);

	//const char *vidBase;




//	if (!tbAutore->loadRecord(vid)) {
//	SignalAnError(__FILE__, __LINE__, "Non posso caricare autore con vid base %s", vid);
//	}
	if (!tbAutore->loadRecord(vidPadre)) {
		SignalAnError(__FILE__, __LINE__, "Non posso caricare autore con vid base %s", vidPadre);
	}

	CString *ds_nome = tbAutore->getFieldString(tbAutore->ds_nome_aut);
	ds_nome->removeCharacterOccurances('*'); // Rimuovi tutti gli altri asterischi
	ds_nome->Strip(ds_nome->trailing, ' ');  // Mantis 0004381. 26/04/2010


	if (tipoLegame == '4')
		sf = new Subfield('y');

	else if (tipoLegame == '8')
		sf = new Subfield('z');

	else
		sf = new Subfield('z');

//	sf->setData(tbAutore->getFieldString(tbAutore->ds_nome_aut)); // dsNome
	sf->setData(ds_nome);
	df->addSubfield(sf);

} // End addAutoreFormaAccettata













void Marc4cppLegami::creaTag71xNew(DataField *df, CString *cdRelazione, CString *ds_nome, char tp_nome, CString *id, CString *cdStrumentoMusicale) {
	Subfield *sf;
	string str;
	//	const char *tp_nome;
	char *ptr; // , *breakPtr

	//	CString *sPtr = tbAutore->getFieldString(tbAutore->ds_nome_aut);

	//	tp_nome = tbAutore->getField(tbAutore->tp_nome_aut);
	if (tp_nome == 'E' || tp_nome == 'e' || tp_nome == 'G' || tp_nome == 'g')
		df->setIndicator1('0');
	else
		df->setIndicator1('1');

	if (tp_nome == 'G' || tp_nome == 'g')
		df->setIndicator2('1');
	else
		df->setIndicator2('2');

	ATTValVector<CString *> stringVect;
	bool retb = ds_nome->Split(stringVect, " : ");
	if (!retb)
		return;
	//int idx = -1;
	char subfield = ' ';
	for (int i = 0; i < stringVect.Length(); i++) {
		if (!i) {
			subfield = 'a';

			int idxAsterisco = stringVect.Entry(i)->First('*');
			if (idxAsterisco > 0) // idxAsterisco != -1 && idxAsterisco != 0
			{
				CString s;
				s = NSB.data();
				s.AppendString(stringVect.Entry(i)->SubstringData(0,idxAsterisco), idxAsterisco);

				s.AppendString(&NSE);

				ptr = stringVect.Entry(i)->SubstringData(idxAsterisco + 1);
				s.AppendString(ptr, stringVect.Entry(i)->getBufferSubStringLength());

				stringVect.Entry(i)->assign(&s);
			}
		} else {
			subfield = 'b';
		}

		stringVect.Entry(i)->removeCharacterOccurances('*'); // Rimuovi tutti gli altri asterischi


		char *qualificazionePtr = strstr(stringVect.Entry(i)->data(), " <");
		if (qualificazionePtr) {
			*qualificazionePtr = 0;
			qualificazionePtr += 1;

			sf = new Subfield(subfield);
			if (subfield == 'b')
				sf->setData((char *)" : ", 3); // rimetti punteggiatura

//			sf->appendData(stringVect.Entry(i));
			ptr = stringVect.Entry(i)->data();
			sf->appendData(ptr, qualificazionePtr-ptr-1);
			df->addSubfield(sf);
			char *ptrStart = qualificazionePtr;
			char *ptr;
			int ctr = 0;
			char subfieldId;
			while (ptr = strstr(ptrStart, " ; ")) {
				if (ptr)
					*ptr = 0;

				if (*ptrStart == '<')
					subfieldId = getQualificazioneType(ptrStart+1, ctr, tp_nome);
				else
					subfieldId = getQualificazioneType(ptrStart, ctr, tp_nome);

				if (subfieldId)
				{

					sf = new Subfield(subfieldId); // qualificazione

					if (!ctr) // *ptrStart == '<'
						sf->appendData((char *)" ", 1);
					else
						sf->appendData((char *)" ; ", 3);

					sf->appendData(ptrStart, strlen(ptrStart)); //, stringVect.Entry(i)->data()-ptrStart+stringVect.Entry(i)->Length()
					df->addSubfield(sf);
				}
				*ptr = ' ';
				ptrStart = ptr += 3;
				ctr++;
				}
				// ultima parte
				if (*ptrStart == '<')
					subfieldId = getQualificazioneType(ptrStart+1, ctr, tp_nome);
				else
					subfieldId = getQualificazioneType(ptrStart, ctr, tp_nome);
				if (subfieldId)
				{
					sf = new Subfield(subfieldId); // qualificazione
					//		sf = new Subfield('c'); // qualificazione
					if (!ctr) // *ptrStart == '<'
						sf->appendData((char *)" ", 1);
					else
						if (ctr)
							sf->appendData((char *)" ; ", 3);
					sf->appendData(ptrStart, strlen (ptrStart)); // , stringVect.Entry(i)->data()-ptrStart+stringVect.Entry(i)->Length()
					df->addSubfield(sf);
				}
			*(qualificazionePtr - 1) = ' ';
		}
			 else {
				sf = new Subfield(subfield);
				if (subfield == 'b')
					sf->setData((char *)" : ", 3); // rimetti punteggiatura
				sf->appendData(stringVect.Entry(i));
				df->addSubfield(sf);
			}

	} // End for
//	printf("\nDFF = '%s'", df->toString());


//	stringVect.DeleteAndClear();

//	sf = new Subfield('3', id);

	char *bid = id->data(); // 24/10/11 Mantis 0004696
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('3', id);
	else // TIPO_SCARICO_OPAC
	{
		CString id((char *)"IT\\ICCU\\", 8);
		id.AppendString(bid, 4); // Prendi il Polo+1
		id.AppendChar('\\');
		id.AppendString(bid+4, 10-4);	 // Prendi resto del bid
		sf = new Subfield('3', &id);
	}
	df->addSubfield(sf);




	if (!cdRelazione->IsEmpty()) {
		sf = new Subfield('4', cdRelazione);
		df->addSubfield(sf);

		// 26/01/2015 Gestione del codice strumento musicale (valido anche per la voce  a dire il vero)
		if (cdStrumentoMusicale && cdStrumentoMusicale->Length())
			creaField7xxVoceStrumento(df, cdStrumentoMusicale);
	}


	stringVect.DeleteAndClear(); // 21/11/2012 Fix memory leak

} // End creaTag71xNew











/*
 410 Collana - Campi legame
 422 Unit� "padre" del supplemento
 421 Unit� supplemento di (del padre)
 423	Pubblicato con
 425 Collana
 430 Continua in
 431	Continua in parte in
 434	Assorbito da
 440	Continuato da
 447	Si fonde con xxx per formare
 451	Altra edizione nello stesso supporto
 454	Traduzione di

 461	Legame al livello padre (fa parte di)
 462 	Legame al livello figlio (comprende?) -- si imposta 462 quando il titolo di arrivo del legame � un livello intermedio (ICCU 26.2.2004)
 463	Legame all'unit� singola -- si imposta 463 quando il legame il titolo base � uno spoglio (natura N)

 464	Legame all'unit� singola analitica (si riferisce agli spogli)
 */
void Marc4cppLegami::creaLegamiTitoloTitolo() {
	string str;
	unsigned int pos;
	char bidColl[10 + 1]; bidColl[10] = 0;
	char bid[10 + 1]; bid[10] = 0;

	bool retb;
	char *entryReticoloPtr;
	DataField *df;
	CString legameDb;
	CString legameUnimarc;
	bool legameOk;
	int ctrLegamiTitolo01 = 0;
	// Cicliamo sul figli di root per cercare i titoli collegati
	tree<std::string>::pre_order_iterator it = reticolo.begin();
	CString sequenza;


	//	CKeyValueVector *reticoliTitTit410KV = new CKeyValueVector(tKVSTRING, tKVVOID); // bid, tree<std::string> &reticolo
	CKeyValueVector *sottolivelliTitTit410KV = new CKeyValueVector(	tKVSTRING, tKVINT); // bid, sottolivello;

	CKeyValueVector *legamiTitTit410KV = new CKeyValueVector( tKVSTRING, tKVSTRING); // bid, TbTitolo aStringRecord;
	CKeyValueVector *sequenzeTitTit410KV = new CKeyValueVector( tKVSTRING, tKVSTRING); // bid, sequenza;

	str = *it;
	int bidStart = str.find_last_of(':');

	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *)str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);

//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

//printf ("\ncreaLegamiTitoloTitolo(), bid=%s", bid);


	elabora44x(bid);

	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); // Primo figlio di root
	char natura = *tbTitolo->getField(tbTitolo->cd_natura);
	int sottoLivelliTitolo410 = 0;
	tree<std::string>::sibling_iterator reticoloSottoLivelliTitolo410;
	CString *tpLegamePtr;
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		legameOk = true;
//printf ("\nstr=%s", str.data());
//if (!strcmp(rootBid, "MUS0232990"))
//{
//	++ch;
//	continue;
//}
		// Troviamo il separatore della gerarchia
		if ((pos = str.find(':')) != string::npos) {
			if (str.find("TIT:", pos + 1) != string::npos) {
				// Prendiamo il BID per accedere alla tbTitolo
				entryReticoloPtr = (char *) str.data();
//printf ("entryReticoloPtr = '%s'\n", entryReticoloPtr);
				TbReticoloTit *tbReticoloTit = new TbReticoloTit(entryReticoloPtr + pos + 5);

				char *BufTailPtr, *aString;
				BufTailPtr = bidColl;
				aString =  tbReticoloTit->getField(tbReticoloTit->bid);
				MACRO_COPY_FAST(10);

//				memcpy(bidColl, tbReticoloTit->getField(tbReticoloTit->bid), 10); // entryReticoloPtr+pos+5, 10

				//printf ("\nbidColl=%s", bidColl);

				retb = tbTitolo->loadRecord(bidColl);
				if (!retb) {
					delete tbReticoloTit;
					++ch;
					continue; // Record non trovato
				}
				// Che tipo legame e'?
				legameDb.Clear();

				char naturaBase = *(tbReticoloTit->getField(tbReticoloTit->cd_natura_base));

				//CString tpLegame = tbReticoloTit->getField(tbReticoloTit->tp_legame);
				tpLegamePtr = tbReticoloTit->getFieldString(tbReticoloTit->tp_legame);

				char naturaColl = *(tbReticoloTit->getField(tbReticoloTit->cd_natura_coll));
				//sequenza = tbReticoloTit->getField(tbReticoloTit->sequenza);

				legameDb = naturaBase;
				legameDb.AppendString(tpLegamePtr);
				legameDb.AppendChar(naturaColl);

				// Dal legame db troviamo il tag unimarc
				legameUnimarc = tbCodiciKV->GetValueFromKey(legameDb.data());
				if (legameUnimarc.IsEmpty()) {
					//					printf("Legame unimarc non trovato per '%s'", legameDb);
					delete tbReticoloTit;
					++ch;
					continue;
				}
				// Se c'e' una nota al legame mettiamola
				df = 0; // 03/03/2010
				notaAlLegame_311 = false;
				if (IS_TAG_TO_GENERATE(311) && (df = creaTag311_NotaLegame(bid, bidColl, naturaBase, tpLegamePtr, naturaColl)))
				{
					marcRecord->addDataField(df);
					notaAlLegame_311 = true;
				}
				if (!df && (IS_TAG_TO_GENERATE(312) && (df = creaTag312_NotaLegameVariante(bid, bidColl, naturaBase, tpLegamePtr, naturaColl))))
					marcRecord->addDataField(df);
				else if (!df && IS_TAG_TO_GENERATE(520) && (df = creaTag520_ContinuazioneDiPeriodici(bid, bidColl, naturaBase, tpLegamePtr, naturaColl)))
					marcRecord->addDataField(df);
				else if (!df && IS_TAG_TO_GENERATE(532) && (df = creaTag532_TitoloEstrapolato(bid, bidColl, naturaBase, tpLegamePtr, naturaColl)))
					marcRecord->addDataField(df);
				else if (!df && (IS_TAG_TO_GENERATE(488) && (df = creaTag488_ContinuazioneDi(bid, bidColl,naturaBase, tpLegamePtr, naturaColl))))
					marcRecord->addDataField(df);
				df = 0;
				if (legameUnimarc.IndexSubString("410") > 0) {
					// Salviamoci il titolo gia' caricato in un vettore
					legamiTitTit410KV->Add(bidColl, tbTitolo->getStringRecordData());
					sequenzeTitTit410KV->Add(bidColl, tbReticoloTit->getField(tbReticoloTit->sequenza)); // sequenza.data()

					// Individua il nodo con + figli TIT->TIT
					int sottoLivelli = 1;
					contaSottoLivelliTitolo(ch, &sottoLivelli);

					if (sottoLivelli > sottoLivelliTitolo410) {
						sottoLivelliTitolo410 = sottoLivelli;
						reticoloSottoLivelliTitolo410 = ch;
					}
					//					reticoliTitTit410KV->Add (bidColl, (void *)&ch);
					sottolivelliTitTit410KV->Add(bidColl, sottoLivelli); // sottoLivelliTitolo410
				}

				else if (legameUnimarc.IndexSubString("422") > 0 && IS_TAG_TO_GENERATE(422))
					df = creaTag422_PadreSupplemento();
				else if (legameUnimarc.IndexSubString("423") > 0 && IS_TAG_TO_GENERATE(423))
					df = creaTag423_PubblicatoCon(ch.begin()); // 21/00/2010 Roveri TO0E068423
				else if (legameUnimarc.IndexSubString("430") > 0 && IS_TAG_TO_GENERATE(430))
				{
					df = creaTag430_ContinuazioneDi();
//					if (df)
//					{
//						// In presenzsa di una 430 se esiste una 440 rimuovila
//						//int n440 = marcRecord->existsDataField(440);
//						int n440 = marcRecord->removeDataFields(440);
//					}
				}
				else if (legameUnimarc.IndexSubString("431") > 0 && IS_TAG_TO_GENERATE(431))
					df = creaTag431_ContinuaInParteIn();
				else if (legameUnimarc.IndexSubString("434") > 0 && IS_TAG_TO_GENERATE(434))
					df = creaTag434_AssorbitoDa();
				else if (legameUnimarc.IndexSubString("447") > 0 && IS_TAG_TO_GENERATE(447))
					df = creaTag447_SiFondeConPerFormare();
				else if (legameUnimarc.IndexSubString("451") > 0 && IS_TAG_TO_GENERATE(451))
					df = creaTag451_AltraEdizioneStessoSupporto();
				else if (legameUnimarc.IndexSubString("454") > 0 && IS_TAG_TO_GENERATE(454))
					df = creaTag454_TraduzioneDi(ch);


				else if (legameUnimarc.IndexSubString("461") > 0) // Legame al livello padre (fa parte di)
				{
					//	df = creaTag461FaParteDi(sequenza.data());
					//	elabora46x(bid); // , sequenza.data()
					siArg = ch.begin(); // 15/09/2010

					elabora46y(bid, tbReticoloTit); // , sequenza.data()

					siArg = 0; // 09/02/2011 Mancato reset causava CRASH casuali

					//				if (tpLegame.isEqual("01"))
					ctrLegamiTitolo01++;
				} else if (legameUnimarc.IndexSubString("500") > 0) {
					bool responsabilitaM1o2 = haResponsabilita1o2(reticolo);
					if (IS_TAG_TO_GENERATE(500))
						df = creaTag500_TitoloUniforme(ch, responsabilitaM1o2);

//					else if (IS_TAG_TO_GENERATE(506))
//						df = creaTag506_TitoloDellOpera(ch, responsabilitaM1o2); // 02/03/2017
// 20/06/2017
					else if (IS_TAG_TO_GENERATE(506) || IS_TAG_TO_GENERATE(576))
						df = creaTag_TitoloDellOpera(ch, responsabilitaM1o2);

					// 928 Dati codificati per titolo uniforme musicale
					if (tbComposizione->existsRecord(tbReticoloTit->getField(tbReticoloTit->bid))) {
						tbComposizione->loadNextRecord(tbReticoloTit->getField(tbReticoloTit->bid));
						bool retb = tbMusica->loadRecord(tbReticoloTit->getField(tbReticoloTit->bid));
						if (!retb)
							SignalAWarning(__FILE__, __LINE__,	"Bid di composizione %s non presente in tb_musica" , tbReticoloTit->getField(tbReticoloTit->bid));

//printf ("\ntbTitolo->getField(tbTitolo->bid)=%s", tbTitolo->getField(tbTitolo->bid));
						if (IS_TAG_TO_GENERATE(928))
							creaTag928_TitoloUniformeMusicale();
						// 929 COMPOSIZIONE (MAT. MUSICALE)
						//printf ("\nargepf creaTag928_Composizione");
						if (IS_TAG_TO_GENERATE(929))
							creaTag929_Composizione();
					}
				}
				else if (legameUnimarc.IndexSubString("510") > 0)
					{
					if (IS_TAG_TO_GENERATE(510))
						df = creaTag510_TitoloParallelo();
					}
				else if (legameUnimarc.IndexSubString("517") > 0)
					{
					if (IS_TAG_TO_GENERATE(517))
						df = creaTag517_AltreVariantiTitolo(tbReticoloTit->getFieldString(	tbReticoloTit->tp_legame_musica));
					}
				else if (legameUnimarc.IndexSubString("530") > 0)
					{
					if (IS_TAG_TO_GENERATE(530))
						df = creaTag530_TitoloChiavePeriodici(ch);
					}
				else if (legameUnimarc.IndexSubString("560") > 0 && DATABASE_ID == DATABASE_SBNWEB)
					{
					if (IS_TAG_TO_GENERATE(560))
						df = creaTag560_RaccoltaFattizia(bid);
					}
				else {
					if (!legameUnimarc.FindSubstring("463")
							&& !legameUnimarc.FindSubstring("440")) // 463/440 gestite a parte

						// printf("\nCodice unimarc non gestito per '%s'",	legameUnimarc.data()); 27/02/2013 Genera troppo log!!! Rimosso

					legameOk = false;
				}
				if (legameOk && df)
					marcRecord->addDataField(df);
				delete tbReticoloTit;
			} // End if "TIT:"
		} // End if ':'

		++ch;
	} // end while



//	if (!ctrLegamiTitolo01 && (natura != 'S' && natura != 's')) {
	if (!ctrLegamiTitolo01) { // Cerchiamo se siamo riferiti da, anche per periodici. Mantis 4279
		if (strcmp(tbTitolo->getField(tbTitolo->bid), bid))
			retb = tbTitolo->loadRecord(bid);
		elabora46x(bid, false); // bid ha padri = false
		elabora42x(bid);
	}

	if (sottoLivelliTitolo410) {
		if (IS_TAG_TO_GENERATE(410))
			creaTag410_TitoliCollane(sottolivelliTitTit410KV, legamiTitTit410KV, sequenzeTitTit410KV);
	}


	//	delete reticoliTitTit410KV;
	delete sottolivelliTitTit410KV;
	delete legamiTitTit410KV;
	delete sequenzeTitTit410KV;

} // End Marc4cppLegami::creaLegamiTitoloTitolo









/*
 Tag+Def:		410 Collana - Campi legame
 --------
 Obbligatorieta':	Facoltativo
 Ripetibilita':	Non Ripetibile
 Indicatore1:	Non definito
 Indicatore2:	0 non stampare la nota
 1 stampare la nota. (significa che non viene utilizzata la nota al legame allo
 scopo di definire in modo p�' specifico il tipo di legame)
 Sottocampi:
 1 - Dati di legame


 Senza gestione $i
 */

// TODO Da RIFARE avendo spostato la gestione per il polo altrove
void Marc4cppLegami::creaTag410_InCascata_Indice(int sottoLivelli, const tree<std::string> &reticolo,CKeyValueVector *legamiTitTit410KV, CKeyValueVector *sequenzeTitTit410KV) {

	DataField *df; // , *dfSubtag
	Subfield *sf;
	ATTValVector<CString *> vectPtr;
	TbReticoloTit *tbReticoloTit = 0;
	char* entryReticolo = 0;
	char *ptr = 0;
	char bidLivelloPadre[10 + 1];
	bidLivelloPadre[10] = 0;
	int livelloValido = sottoLivelli;
	CString subString;

	while (livelloValido) {
		// Partiamo dal nodo + basso (che abbia un legame di tipo 01)
		entryReticolo = getSottoLivelloTitolo410(reticolo, 1, livelloValido);
		ptr = strchr(entryReticolo, ':');
		if (!tbReticoloTit)
			tbReticoloTit = new TbReticoloTit(ptr + 5);
		else
			tbReticoloTit->assign(ptr + 5);

		// Gestiamo solo legami di tipo 01
		char *tpLegame = tbReticoloTit->getField(tbReticoloTit->tp_legame);
		if (strcmp("01", tpLegame)) {
			livelloValido--;
			continue;
		}
		// Trovato il primo legame di tipo 01
		break;
	} // end while

	if (!livelloValido)
		return; // Legami non 01

	// Prendiamo il titolo

	entryReticolo = getSottoLivelloTitolo410(reticolo, 1, 1);
	ptr = strchr(entryReticolo, ':');

	char *BufTailPtr, *aString;
	BufTailPtr = bidLivelloPadre;
	aString =  ptr + 5;
	MACRO_COPY_FAST(10);

//	memcpy(bidLivelloPadre, ptr + 5, 10);

	char *stringRecord = legamiTitTit410KV->GetValueFromKey(bidLivelloPadre);
	if (stringRecord)
//		tbTitolo->loadRecordFromString(stringRecord);
		tbTitolo->loadRecordFromString(stringRecord, strlen(stringRecord));

	else
		//tbTitolo->loadRecord(bid);
		tbTitolo->loadRecord(bidLivelloPadre);

	df = new DataField();
	df->setTag((char *)"410");
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');

	//	s = " 001";
	//s = "001"; // 15/01/2010 10.47
	CString s((char *)"001", 3);

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		s.AppendString(bidLivelloPadre, 10);
	else // TIPO_SCARICO_OPAC
	{
		int pos =3;
		s.AppendString((char *)"IT\\ICCU\\", 8);
		if (isAnticoE(bidLivelloPadre))
			pos = 4;
		s.AppendString(bidLivelloPadre, pos); // Prendi il Polo
		s.AppendChar('\\');
		s.AppendString(bidLivelloPadre+pos, 10-pos);	 // Prendi resto del bid
	}

	sf = new Subfield('1', &s);
	df->addSubfield(sf);
	creaSubtag200Area1(df, true, true); // Crea tag 200

	//	dfSubtag = creaSubtag200(df);

	CString seq = sequenzeTitTit410KV->GetValueFromKey(bidLivelloPadre);

	seq.Strip(s.trailing, '\n');
	if (DATABASE_ID != DATABASE_INDICE)
	{
		if (!seq.IsEmpty())
		{
			sf = new Subfield('v', &seq);
			//sf->setData();
			df->addSubfield(sf);
		}
	}
	CString ultimoTitolo, collanaIndice;

	appendSubtag200Area1DolA(&ultimoTitolo, false, true); // 12/10/2010 10.45
	collanaIndice.assign(ultimoTitolo.data(), ultimoTitolo.Length());// = ultimoTitolo.data(); // in caso non ci siano sottocollane

	// Aggiungiamo le $i
	s.Clear();
	for (int i = (livelloValido - 1); i; i--) {
		// Troviamo il bid
		entryReticolo = getSottoLivelloTitolo410(reticolo, 1, i);
		char *ptr = strchr(entryReticolo, ':');
		tbReticoloTit->assign(ptr + 5);
		char *bidSottolivello = tbReticoloTit->getField(tbReticoloTit->bid);
		//		 printf ("\nentry=%s, bid=%s", entryReticolo, bid);
		// Carichiamo il record
		char *stringRecord = legamiTitTit410KV->GetValueFromKey(bidSottolivello);
		//		tbTitolo->loadRecordFromString(stringRecord);
		if (stringRecord)
//			tbTitolo->loadRecordFromString(stringRecord);
			tbTitolo->loadRecordFromString(stringRecord, strlen(stringRecord));

		else
			tbTitolo->loadRecord(bidSottolivello);
//tbTitolo->dumpRecord();
		// Prendiamo il titolo
//		appendSubtag200Area1DolA(&s, false, true);

		if (i>1)
		{
			appendSubtag200Area1DolA(&s, false, false); // 09/09/2010 12.21
		}
		else
		{
			collanaIndice.Clear();
//			appendSubtag200Area1DolA(&collanaIndice, false, false); // 09/09/2010 12.21
			appendSubtag200Area1DolA(&collanaIndice, false, true); // 05/11/2010
			s.AppendString(&collanaIndice);
		}

		// Strippiamo la parte iniziale
		//		 CString subString = s.SubstringData(ultimoTitolo.Length());
		// Strippiamo la parte iniziale ?
		if (s.StartsWith(ultimoTitolo.data()))
			subString = s.SubstringData(ultimoTitolo.Length());
		else
			subString = s.Data();
		ultimoTitolo = s.Data();

		// Prendiamo la sequenza
		seq = sequenzeTitTit410KV->GetValueFromKey(bidSottolivello);
		seq.Strip(s.trailing, '\n');

		if (DATABASE_ID != DATABASE_INDICE)
		{
				// Costruiamo la $i
				sf = new Subfield('i', &subString);
				//sf->setData();
				df->addSubfield(sf);


				if (!seq.IsEmpty()) //  && !s.isEqual("null")
				{
					sf = new Subfield('v', &seq);
					//sf->setData();
					df->addSubfield(sf);
				}
		}

	} // End gestione delle $i


	if (DATABASE_ID == DATABASE_INDICE)
	{ // Gestione indice senza le $i
		sf = df->getSubfield('a'); // Sostituiamo la parte iniziale del titolo con il titolo completo
		if (sf)
		{
			// Gestiamo il no sort
			int pos;
			// ultimoTitolo
			if ((pos = collanaIndice.First('*')) != -1) // abbiamo almeno un asterisco?
			{
				if (pos) // Se non primo carattere
				{
					collanaIndice.Replace(pos, 1, NSE.data());
					collanaIndice.ChangeTo('*', ' '); // Replace all other *
					collanaIndice.PrependString(NSB.data());

				} else
					collanaIndice.ExtractFirstChar();
			}

			sf->setData(&collanaIndice);
		}
		if (!seq.IsEmpty()) //  && !s.isEqual("null")
		{
			// Una sola $v in coda
			sf = new Subfield('v', &seq);
			//sf->setData();
			df->addSubfield(sf);
		}
	}




	// Creiamo il legame all'autore
	CString entryReticoloAutore;
	int pos = getVidFirstAutore(reticolo, entryReticoloAutore);
	if (pos != -1) {
		DataField *dfLegame;
		dfLegame = creaLegameTitoloAutore(entryReticoloAutore.data(), pos);
		if (dfLegame) {
			Subfield * sf;
			sf = new Subfield('1', dfLegame->toSubTag());
			//char *subtag = dfLegame->toSubTag();
			//sf->setData();
			df->addSubfield(sf);
			delete dfLegame;
		}
	}

	marcRecord->addDataField(df);

	// Rirpristina la tbTitolo otiginale
	//	tbTitolo = saveTbTitolo;

	delete tbReticoloTit;
	//	delete locTbTitolo;

} // End creaTag410_InCascata_Indice







/*
 * Esempi di periodi:
 * 		1768
 * 		1768-1769
 * 		"fl. "	// floruit (fiori')
 * 		"m. "	// morto
 * 		"n. "	// nato
 * 		"sec. "	// secolo

 * 		-1768f
 * 		-1768fl
 * 		-1768p
 * 		"-fl. "	// floruit (fiori')
 * 		"-m. "	// morto
 * 		"-n. "	// nato
 * 		"-sec. "	// secolo
 */


//bool Marc4cppLegami::isQualificazioneData(char *qualificazionePtr) {
//	CString s;
//
//	if (*qualificazionePtr == '-')
//		qualificazionePtr++;
//
//	s = qualificazionePtr;
//	if (s.GetLastChar() == '>')
//		s.ExtractLastChar();
//
//	if (s.StartsWith("sec.") || s.EndsWith("sec.")) // secolo
//		return true;
//	if (s.StartsWith("ca.") || s.EndsWith("ca.")) // circa
//		return true;
//	if (s.StartsWith("m.") || s.EndsWith("m.")) // morto
//		return true;
//	if (s.StartsWith("n.") || s.EndsWith("n.")) // nato
//		return true;
//	if (s.StartsWith("fl.") || s.EndsWith("fl."))// floruit (fiori')
//		return true;
//
//	if (isdigit(*qualificazionePtr)
//		&& isdigit(*(qualificazionePtr + 1))
//		&& isdigit(*(qualificazionePtr + 2))
//		&& isdigit(*(qualificazionePtr + 3)))
//		return true;
//
//	return false;
//}




void Marc4cppLegami::creaTag71x(DataField *df, CString *cdRelazione, char tipoResponsabilita, CString *cdStrumentoMusicale) {
	Subfield *sf;
	string str;
	const char *tpNome;
	char *ptr; // , *breakPtr
	//CString s;

	//const char *nomePtr = tbAutore->getField(tbAutore->ds_nome_aut);
	CString *sPtr = tbAutore->getFieldString(tbAutore->ds_nome_aut);

	sPtr->Strip(sPtr->trailing, ' '); // Mantis 0004381. 26/04/2010

	tpNome = tbAutore->getField(tbAutore->tp_nome_aut);
	if (*tpNome == 'E' || *tpNome == 'e' || *tpNome == 'G' || *tpNome == 'g')
		df->setIndicator1('0');
	else
		df->setIndicator1('1'); // tipo nome R, congresso

	df->setIndicator2('2'); // 14/10/2010 Concordato con Paolucci. Non e' possibile intercettare il valore '1' per luoghi o autorita'

	ATTValVector<CString *> stringVect;
	bool retb = sPtr->Split(stringVect, " : ");
	if (!retb)
		return;
	//int idx = -1;
	char subfield = ' ';
	for (int i = 0; i < stringVect.Length(); i++) {
		sPtr = stringVect.Entry(i);
		if (!i) {
			subfield = 'a';
			int idxAsterisco = stringVect.Entry(i)->First('*');
			if (idxAsterisco > 0) // idxAsterisco != -1 && idxAsterisco != 0
			{
				CString s(NSB.data(), NSB.Length());
				s.AppendString(stringVect.Entry(i)->SubstringData(0, idxAsterisco), idxAsterisco);
				s.AppendString(&NSE);
				ptr = stringVect.Entry(i)->SubstringData(idxAsterisco + 1);
				s.AppendString(ptr, stringVect.Entry(i)->getBufferSubStringLength());
				if (s.GetLastChar() == ' ')
					s.ExtractLastChar();
				stringVect.Entry(i)->assign(&s);
			}
		} else {
			subfield = 'b';
		}
		stringVect.Entry(i)->removeCharacterOccurances('*'); // Rimuovi tutti gli altri asterischi
		char *qualificazionePtr = strstr(stringVect.Entry(i)->data(), " <");
		if (qualificazionePtr) // Qualificazione parte dele testo?
		{
			int idx = stringVect.Entry(i)->IndexCharFrom('>', 0, CString::forward);
			if (idx < (stringVect.Entry(i)->Length()-1))
				qualificazionePtr = 0;
		}
		if (qualificazionePtr) {
			*qualificazionePtr = 0;
			qualificazionePtr += 1;

			sf = new Subfield(subfield);
			if (subfield == 'b')
				sf->setData((char *)" : ", 3); // rimetti punteggiatura

			ptr = stringVect.Entry(i)->data();
			sf->appendData(ptr, qualificazionePtr-ptr-1);
			df->addSubfield(sf);
				char *ptrStart = qualificazionePtr;
				char *ptr;
				int ctr = 0;
				char subfieldId;
				while (ptr = strstr(ptrStart, " ; ")) {
					if (ptr)
						*ptr = 0;
					if (*ptrStart == '<')
						subfieldId = getQualificazioneType(ptrStart+1, ctr, *tpNome);
					else
						subfieldId = getQualificazioneType(ptrStart, ctr, *tpNome);
					if (subfieldId)
					{
						sf = new Subfield(subfieldId); // qualificazione
						if (!ctr) // *ptrStart == '<'
							sf->appendData((char *)" ", 1);
						else
							sf->appendData((char *)" ; ", 3);
						sf->appendData(ptrStart, strlen(ptrStart)); // , stringVect.Entry(i)->data()-ptrStart+stringVect.Entry(i)->Length()
						df->addSubfield(sf);
					}
					*ptr = ' ';
					ptrStart = ptr += 3;
					ctr++;
				}
				// ultima parte
				if (*ptrStart == '<')
					subfieldId = getQualificazioneType(ptrStart+1, ctr, *tpNome);
				else
					subfieldId = getQualificazioneType(ptrStart, ctr, *tpNome);
				if (subfieldId)
				{
					sf = new Subfield(subfieldId); // qualificazione
					//		sf = new Subfield('c'); // qualificazione
					if (!ctr) // *ptrStart == '<'
						sf->appendData((char *)" ", 1);
					else
						if (ctr)
							sf->appendData((char *)" ; ", 3);
					sf->appendData(ptrStart, strlen(ptrStart)); // , stringVect.Entry(i)->data()-ptrStart+stringVect.Entry(i)->Length()
					df->addSubfield(sf);
				}
			*(qualificazionePtr - 1) = ' ';
		}
			 else {
				sf = new Subfield(subfield);
				if (subfield == 'b')
					sf->setData((char *)" : ", 3); // rimetti punteggiatura
				sf->appendData(stringVect.Entry(i));
				df->addSubfield(sf);

			}
	} // End for
	stringVect.DeleteAndClear();
	CString *vid = tbAutore->getFieldString(tbAutore->vid); // 28/04/10 // const
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('3', vid);
		//sf->setData();
	else // TIPO_SCARICO_OPAC
	{
//		CString id = "IT\\ICCU\\";
		CString id((char *)"IT\\ICCU\\", 8);
		id.AppendString(vid->Data(), 4); // Prendi il Polo+1
		id.AppendChar('\\');
		id.AppendString(vid->Data()+4, 10-4);	 // Prendi resto del bid
		sf = new Subfield('3', &id);
		//sf->setData();
	}
	df->addSubfield(sf);



	if (cdRelazione) {
		cdRelazione->Strip(cdRelazione->trailing, ' ');
		if (!cdRelazione->IsEmpty())
		{
			sf = new Subfield('4', cdRelazione);
			df->addSubfield(sf);

			// 26/01/2015 Gestione del codice strumento musicale (valido anche per la voce  a dire il vero)
			if (cdStrumentoMusicale && cdStrumentoMusicale->Length())
				creaField7xxVoceStrumento(df, cdStrumentoMusicale);


		}
		else
		{
			if (tipoResponsabilita == TP_RESP_4_RESPONSABILITA_NELLA_PRODUZIONE_MATERIALE
					&& DATABASE_ID == DATABASE_INDICE
					&& TIPO_SCARICO == TIPO_SCARICO_UNIMARC) // UNIMARC Relator code tipografi/editori Mail Contardi 26 Apr 2011
			{
				cdRelazione->assign((char *)"650");
				sf = new Subfield('4', cdRelazione);
				df->addSubfield(sf);
			}

		}

	}
	else
	{
		if (tipoResponsabilita == TP_RESP_4_RESPONSABILITA_NELLA_PRODUZIONE_MATERIALE
			&& DATABASE_ID == DATABASE_INDICE
			&& TIPO_SCARICO == TIPO_SCARICO_UNIMARC) // UNIMARC Relator code tipografi/editori Mail Contardi 26 Apr 2011
		{
			cdRelazione->assign((char *)"650");
			sf = new Subfield('4', cdRelazione);
			df->addSubfield(sf);
		}

	}




//df->dump();
} // End creaTag71x







/*
 * Con gestione $i
 */
void Marc4cppLegami::creaTag410_InCascata_Polo(int sottoLivelli, const tree<std::string> &reticolo,CKeyValueVector *legamiTitTit410KV, CKeyValueVector *sequenzeTitTit410KV)
	{
		DataField *df;
		Subfield *sf;
		CString subString;
		ATTValVector<CString *> vectPtr;
		CString s;
		TbReticoloTit *tbReticoloTit = 0;
		char* entryReticolo = 0;
		char *ptr = 0;
		char bidLivelloPadre[10 + 1];
		bidLivelloPadre[10] = 0;
		CString seq, lastSeq;

		lastSeq = "";

		int livelloValido = sottoLivelli;

		while (livelloValido) {
			// Partiamo dal nodo + basso (che abbia un legame di tipo 01)
			entryReticolo = getSottoLivelloTitolo410(reticolo, 1, livelloValido);
			ptr = strchr(entryReticolo, ':');
			if (!tbReticoloTit)
				tbReticoloTit = new TbReticoloTit(ptr + 5);
			else
				tbReticoloTit->assign(ptr + 5);

			// Gestiamo solo legami di tipo 01
			char *tpLegame = tbReticoloTit->getField(tbReticoloTit->tp_legame);
			if (strcmp("01", tpLegame)) {
				livelloValido--;
				continue;
			}
			// Trovato il primo legame di tipo 01
			break;
		} // end while

		// Prendiamo il titolo

if (!livelloValido)
	return; // Legami non 01

		char *bid = tbReticoloTit->getField(tbReticoloTit->bid);
		char *stringRecord = legamiTitTit410KV->GetValueFromKey(bid);
//		char *stringRecord = legamiTitTit410KV->GetValueFromKey(bidLivelloPadre);

		if (stringRecord)
//			tbTitolo->loadRecordFromString(stringRecord);
			tbTitolo->loadRecordFromString(stringRecord, strlen(stringRecord));

		else
			//tbTitolo->loadRecord(bid);
			tbTitolo->loadRecord(bid);

		df = new DataField();

		df->setTag((char *)"410");
		if (notaAlLegame_311)
			df->setIndicator2('1');
		else
			df->setIndicator2('0');



		//	s = " 001";
		s = "001"; // 15/01/2010 10.47
		entryReticolo = getSottoLivelloTitolo410(reticolo, 1, 1);
		ptr = strchr(entryReticolo, ':');

		char *BufTailPtr, *aString;
		BufTailPtr = bidLivelloPadre;
		aString =  ptr + 5;
		MACRO_COPY_FAST(10);

//		memcpy(bidLivelloPadre, ptr + 5, 10);

		s.AppendString(bidLivelloPadre, 10);

		sf = new Subfield('1', &s);
		df->addSubfield(sf);

	//printf ("DF='%s'", df->toString());

		creaSubtag200Area1(df, true, true); // Crea tag 200

		//	dfSubtag = creaSubtag200(df);

		seq = sequenzeTitTit410KV->GetValueFromKey(bid);
		seq.Strip(seq.trailing, '\n');
	    if (!seq.IsEmpty()) //  && !s.isEqual("null")
	    {
//	        sf = new Subfield('v'); // Mantis 4949. 16/05/2012
//	        sf->setData(&seq);
//	        df->addSubfield(sf);

	        lastSeq.assign(seq.data());
	    }




	//	CString ultimoTitolo = df->getSubfield('a')->getData();
		CString ultimoTitolo;

	//	sf = df->getSubfield('a');
	//	if (sf)
	//		ultimoTitolo = sf->getData();

//tbTitolo->dumpRecord();

	//	appendSubtag200Area1DolA(&ultimoTitolo, false, false); // 09/09/2010 12.21
		appendSubtag200Area1DolA(&ultimoTitolo, false, true); // 12/10/2010 10.45

		// Aggiungiamo le $i
//		s.Clear(); // Mantis 4365
		char *bidSottolivello = 0;

		if (ultimoTitolo.GetLastChar() == ']') // Gestione parentesi quadra [. Mail Bergamin 18/12/2011
			ultimoTitolo.ExtractLastChar();

		bool checkQuadra = false;
		for (int i = (livelloValido - 1); i; i--) {

			// Troviamo il bid
			entryReticolo = getSottoLivelloTitolo410(reticolo, 1, i);
			char *ptr = strchr(entryReticolo, ':');
			tbReticoloTit->assign(ptr + 5);
			bidSottolivello = tbReticoloTit->getField(tbReticoloTit->bid);
			//		 printf ("\nentry=%s, bid=%s", entryReticolo, bid);
			// Carichiamo il record
			char *stringRecord = legamiTitTit410KV->GetValueFromKey(bidSottolivello);
			//		tbTitolo->loadRecordFromString(stringRecord);
			if (stringRecord)
//				tbTitolo->loadRecordFromString(stringRecord);
				tbTitolo->loadRecordFromString(stringRecord, strlen(stringRecord));

			else
				tbTitolo->loadRecord(bidSottolivello);
//tbTitolo->dumpRecord();
			// Prendiamo il titolo
s.Clear(); // Mantis 4365
	        appendSubtag200Area1DolA(&s, false, true);

			if (s.GetLastChar() == ']') // Gestione parentesi quadra [. Mail Bergamin 18/12/2011
			{
				s.ExtractLastChar();
				checkQuadra = true;
			}
			// Strippiamo la parte iniziale
			//		 CString subString = s.SubstringData(ultimoTitolo.Length());
			// Strippiamo la parte iniziale ?
			if (s.StartsWith(ultimoTitolo.data()))
				subString = s.SubstringData(ultimoTitolo.Length());
			else
				subString = s.Data();
			ultimoTitolo.assign(&s); // = s.Data();


			// Prendiamo la sequenza
			seq = sequenzeTitTit410KV->GetValueFromKey(bidSottolivello);
			seq.Strip(s.trailing, '\n');

			// Costruiamo la $i
			sf = new Subfield('i', &subString);
			df->addSubfield(sf);

			if (!seq.IsEmpty()) //  && !s.isEqual("null")
			{
//				sf = new Subfield('v', &seq);
//				df->addSubfield(sf);
				lastSeq.assign(&seq); // Mantis 4949. 16/05/2012
			}
		} // End gestione delle $i


        if (lastSeq.Length()) // Mantis 4949. 16/05/2012
        {
	        sf = new Subfield('v');
	        sf->setData(&seq);
	        df->addSubfield(sf);
        }





//		if (bidSottolivello) // 21/12/2011
//		{ // Sostituire il bid della 001 in $1
//		}

if (checkQuadra)	// Gestione parentesi quadra [. Mail Bergamin 18/12/2011
{
	// Rimuovi quadra della $a
	sf = df->getSubfield('a');
	CString *s = sf->getDataString();
//	long pos = s->First(']');
//	s->ExtractFromTo(pos, pos);
//	printf ("\n%s", s->data());

	long pos = s->First('['); // 28/04/2015
	if (pos != -1)
	{
	s->ExtractFromTo(pos, pos);
	//printf ("\n%s", s->data());
	}
//	else
//	printf ("\nNON TROVO LA QUADRA [ %s per bidSottolivello %s", s->data(), bidSottolivello);
}



		// Creiamo il legame all'autore
		CString entryReticoloAutore;
		int pos = getVidFirstAutore(reticolo, entryReticoloAutore);
		if (pos != -1) {
			DataField *dfLegame;
			dfLegame = creaLegameTitoloAutore(entryReticoloAutore.data(), pos);
			if (dfLegame) {
				Subfield * sf;
				sf = new Subfield('1', dfLegame->toSubTag());
				//char *subtag = dfLegame->toSubTag();
				//sf->setData();
				df->addSubfield(sf);
				delete dfLegame;
			}
		}
		marcRecord->addDataField(df);
		delete tbReticoloTit;
	} // creaTag410_InCascata_Polo



















/*!
\brief <b>Tag 225 - Area della collezione</b>
<table>
<tr><th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>225</td></tr>
<tr><td valign=top>Descrizione</td><td>Area della collezione</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>valore = 2 (indica che il campo e' identico al campo 410)</td></tr>
<tr><td valign=top>Indicatore 2</td><td>spazio</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
		<li>a - Titolo della collana. Se ci sono piu' collane e' il titolo della collana superiore. Non ripetibile.
		<li>d - Titolo parallelo. Ripetibile.
		<li>e - Complemento del titolo. Ripetibile.
		<li>f - Prima indicazione di responsabilita'. Ripetibile.
<!--
		<li>g - Altra indicazione di responsabilit�.  Ripetibile.
		<li>h - Numerazione della sottocollana nella collana superiore. Ripetibile.
		<li>i - Name of a Part
		<li>v - Volume Designation
		<li>x - ISSN of Series
		<li>z - Language of Parallel Title
-->
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppLegami::creaTag225_AreaCollezione(int sottoLivelli,
		const tree<std::string> &reticolo, CKeyValueVector *legamiTitTit410KV,
		CKeyValueVector *sequenzeTitTit410KV) {
	DataField *df, *dfSubtag;
	Subfield *sf;
//	bool retb;
	CString subString;
	ATTValVector<CString *> vectPtr;
	CString s;

	TbReticoloTit *tbReticoloTit = 0;
	char* entryReticolo = 0;
	char *ptr = 0;
	//print_tree(reticolo, reticolo.begin(), reticolo.end());

	int livelloValido = sottoLivelli;

	while (livelloValido) {
		// Partiamo dal nodo + basso (che abbia un legame di tipo 01)
		entryReticolo = getSottoLivelloTitolo410(reticolo, 1, livelloValido);
		ptr = strchr(entryReticolo, ':');
		if (!tbReticoloTit)
			tbReticoloTit = new TbReticoloTit(ptr + 5);
		else
			tbReticoloTit->assign(ptr + 5);

		// Gestiamo solo legami di tipo 01
		char *tpLegame = tbReticoloTit->getField(tbReticoloTit->tp_legame);
		if (strcmp("01", tpLegame)) {
			livelloValido--;
			continue;
		}
		// Trovato il primo legame di tipo 01
		break;
	} // end while

	if (!livelloValido)
		return; // Legami non 01


	//	print_tree(reticolo, reticolo.begin(), reticolo.end());

	// Prendiamo il titolo
	char *bid = tbReticoloTit->getField(tbReticoloTit->bid);
	char *stringRecord = legamiTitTit410KV->GetValueFromKey(bid);

	if (stringRecord)
		tbTitolo->loadRecordFromString(stringRecord, strlen(stringRecord));
	else
		tbTitolo->loadRecord(bid);

//printf ("\nTITOLO");
//tbTitolo->dumpRecord();


	df = new DataField();
	df->setTag((char *)"225");
	df->setIndicator1(INDICATOR_FILLER);
	df->setIndicator2(' ');

	//		appendSubtag200Area1DolA(&s, false, true);
	//		sf = new Subfield('a');
	//		sf->setData(s.data());
	//		df->addSubfield(sf);
	//		addAutoreConResponsabilitaPrincipale(df, reticolo); // della collana e NON della monografia

	dfSubtag = creaSubtag200Area1(df, false, true);

	s = sequenzeTitTit410KV->GetValueFromKey(bid);
	s.Strip(s.trailing, '\n');
	if (!s.IsEmpty()) //  && !s.isEqual("null")
	{
		sf = new Subfield('v', &s);
		//sf->setData();
		df->addSubfield(sf);
	}

//	CString ultimoTitolo = df->getSubfield('a')->getData();
	CString ultimoTitolo;

	// 09/09/2010 12.16
//	sf = df->getSubfield('a');
//	if (sf)
//		ultimoTitolo = sf->getData();

	appendSubtag200Area1DolA(&ultimoTitolo, false, false);

	ultimoTitolo.Strip(s.trailing, ']'); // Gestione parentesi quadra [. Mail Bergamin 18/12/2011

	bool checkQuadra = false;
	// Aggiungiamo le $i
	for (int i = (livelloValido - 1); i; i--) {

		// Troviamo il bid
		entryReticolo = getSottoLivelloTitolo410(reticolo, 1, i);
		char *ptr = strchr(entryReticolo, ':');

		tbReticoloTit->assign(ptr + 5);
		// Gestiamo solo legami di tipo 01
		char *tpLegame = tbReticoloTit->getField(tbReticoloTit->tp_legame);
		if (strcmp("01", tpLegame))
			break; // altro legame


		bid = tbReticoloTit->getField(tbReticoloTit->bid);

		//			 printf ("\nentry=%s, bid=%s", entryReticolo, bid);

		// Carichiamo il record
		char *stringRecord = legamiTitTit410KV->GetValueFromKey(bid);
		//			tbTitolo->loadRecordFromString(stringRecord);
		if (stringRecord)
			//tbTitolo->loadRecordFromString(stringRecord);
			tbTitolo->loadRecordFromString(stringRecord, strlen(stringRecord));

		else
			tbTitolo->loadRecord(bid);


//printf ("\nTITOLO $i livello=%d", i);
//tbTitolo->dumpRecord();

		// Prendiamo il titolo
		CString s;
//		appendSubtag200Area1DolA(&s, false, true);
		appendSubtag200Area1DolA(&s, false, false);
		if (s.GetLastChar() == ']') // Gestione parentesi quadra ]. Mail Bergamin 18/12/2011
		{
			s.ExtractLastChar();
			checkQuadra = true;
		}


		// Strippiamo la parte iniziale ?
		if (s.StartsWith(ultimoTitolo.data()))
			subString = s.SubstringData(ultimoTitolo.Length());
		else
			subString = s.Data();

		ultimoTitolo = s.Data();

		// Costruiamo la $i
		sf = new Subfield('i', &subString);
		//sf->setData();
		df->addSubfield(sf);

		// Prendiamo la sequenza
s.Clear(); // Mantis 4365
		s = sequenzeTitTit410KV->GetValueFromKey(bid);
		s.Strip(s.trailing, '\n');



		if (!s.IsEmpty()) //  && !s.isEqual("null")
		{
			sf = new Subfield('v', &s);
			//sf->setData();
			df->addSubfield(sf);
		}
	}

	if (checkQuadra)	// Gestione parentesi quadra [. Mail Bergamin 18/12/2011
	{
		// Rimuovi quadra della $a
		sf = df->getSubfield('a');
		CString *s = sf->getDataString();
//		long pos = s->First(']');
//		s->ExtractFromTo(pos, pos);
//		printf ("\n%s", s->data());
		long pos = s->First('[');	// 28/04/2015
		if (pos != -1)
			s->ExtractFromTo(pos, pos);
//		else
//			printf ("\nNON TROVO LA QUADRA [ %s per bid %s", s->data(), bid);
	}

	marcRecord->addDataField(df);

	// Rirpristina la tbTitolo otiginale
	//		tbTitolo = saveTbTitolo;

	delete tbReticoloTit;
	//		delete locTbTitolo;

} // End creaTag225_Collezione_OneForAll




/*!
\brief <b>Tag 311 - Note legami tra titoli</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>311</td></tr>
<tr><td valign=top>Descrizione</td><td>Note riguardanti legami tra titoli</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
        <li>9 - identificativo titolo di arrivo del legame. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td>
 il campo viene prodotto se esiste una nota al legame per legami di tipo:
 02, 03, 04 (solo se il titolo di partenza ha natura M o W), 05, 06, 07, 41, 42, 43 dove il titolo di
 partenza del legame e' il titolo del record unimarc.
 </td></tr>
</table>
*/
// Se il legame non � di tipo 03 viene messa in coda al sottocampo la stringa ' : '.
DataField * Marc4cppLegami::creaTag311_NotaLegame(char *bid, char *bidColl,
		char naturaBase, CString * tpLegame, char naturaColl) {

//printf ("\ncreaTag311_NotaLegameVariante, bid=%s, bidcoll=%s", bid, bidColl);

	DataField *df = 0;
	if (
//			(tpLegame->isEqual("01") && DATABASE_ID == DATABASE_INDICE)
			tpLegame->isEqual("01") // 06/04/2011 Mantis 4343
			||(
				(tpLegame->isEqual("02")
				|| tpLegame->isEqual("03")
				|| tpLegame->isEqual("04")	)
				&& (naturaBase == 'M' || naturaBase == 'W')
				)
			|| tpLegame->isEqual("05")
			//|| tpLegame.isEqual("06") // 03/08/2010
			|| tpLegame->isEqual("06")//09/08/2010 rimesso
			|| tpLegame->isEqual("07")
//			|| (tpLegame.isEqual("09") // 03/08/2010
			|| tpLegame->isEqual(CD_LEGAME_41_ASSORBE) // 09/08/2010
			|| tpLegame->isEqual(CD_LEGAME_42_SI_FONDE_CON)
			|| tpLegame->isEqual(CD_LEGAME_43_CONTINUAZIONE_PARZIALE_DI)

	) {
		Subfield *sf;
		bool retb;

		// Costruisci la chiave di ricerca della relazione titolo titolo
		CString key = bid;
		key.AppendString(bidColl, 10);


//printf ("\ncreaTag311NotaLegame Bid=%s", bid);

		// Carichiamo il record a partire da pos
		retb = trTitTit->loadRecord(key.data());
		if (!retb)
			return df; // Record non trovato

		CString *sPtr = trTitTit->getFieldString(trTitTit->nota_tit_tit);

		sPtr->Strip(sPtr->trailing, ' ');
		if (!sPtr->IsEmpty()) //  && !sPtr->isEqual("null")
		{
			df = new DataField((char *)"311", 3);
			sf = new Subfield('a', sPtr);
			df->addSubfield(sf);


			char *bid = trTitTit->getField(trTitTit->bid_coll); // const
			if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
				sf = new Subfield('9', bid, 10);
			else // TIPO_SCARICO_OPAC
			{
				int pos =3;
				CString id((char *)"IT\\ICCU\\", 8);
				if (isAnticoE(bid))
					pos = 4;
				id.AppendString(bid, pos); // Prendi il Polo
				id.AppendChar('\\');
				id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid
				sf = new Subfield('9', &id);
			}
			df->addSubfield(sf);
		}
	}
	return df;
} // End creaTag311NotaLegame


/*!
\brief <b>Tag 312 - Note legami con varianti del titolo</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>312</td></tr>
<tr><td valign=top>Descrizione</td><td>Note legami con varianti del titolo</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
        <li>9 - identificativo titolo di arrivo del legame. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td>
  il campo viene prodotto se esiste una nota al legame nei legami di tipo
 04 (solo se il titolo di partenza ha natura S), 08, 09, dove il titolo di partenza del
 legame e' il titolo del record unimarc.
</td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag312_NotaLegameVariante(char *bid,
		char *bidColl, char naturaBase, CString * tpLegame, char naturaColl) {
	DataField *df = 0;
	//CString s;

	if (	(tpLegame->isEqual("04") && naturaBase == 'S')
			|| tpLegame->isEqual("08")
			||tpLegame->isEqual("09") // 03/08/2010

			) {
		Subfield *sf;
		bool retb;

		// Costruisci la chiave di ricerca della relazione titolo titolo
		CString key = bid;
		key.AppendString(bidColl, 10);

		// Carichiamo il record a partire da pos
		retb = trTitTit->loadRecord(key.data());

//trTitTit->dumpRecord();
//tbTitolo->dumpRecord();

		if (!retb)
			return df; // Record non trovato
		CString *sPtr = trTitTit->getFieldString(trTitTit->nota_tit_tit);
		if (sPtr->isEqual("null"))
			sPtr->Clear();

		if (sPtr->IsEmpty())
			return df;

		df = new DataField((char *)"312", ' ', ' ');

		sPtr->Strip(sPtr->trailing);

		if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC) // Mantis INDICE 5403. Non valido per scarico OPAC
		{
			// Mantis Polo 4979 aggiungiamo il titolo
			CString isbd = tbTitolo->getField(tbTitolo->isbd);
			int pos = isbd.IndexSubString(". -");
			if (pos != -1)
				isbd.CropRightFrom(pos);

//			if(isbd.GetFirstChar() == '*')
//				isbd.ExtractFirstChar();

//			pos = isbd.First('*');
//			if (pos != -1)
//				isbd.CropLeftBy(pos+1);

			if ((pos = isbd.First('*')) != -1) // abbiamo caratteri no sort? // 23/03/2015
			{
				if (pos) // Se non primo carattere
				{
					isbd.Replace(pos, 1, NSE.data());
					isbd.ChangeTo('*', ' '); // Replace all other *

					isbd.PrependString(NSB.data());

				} else
					isbd.ExtractFirstChar();
			}




			sPtr->AppendString((char *)": ");
			sPtr->AppendString(isbd.data());
		}


		sf = new Subfield('a', sPtr);
		df->addSubfield(sf);


		if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
			sf = new Subfield('9', tbTitolo->getFieldString(tbTitolo->bid));
		else // TIPO_SCARICO_OPAC
		{
			char *bid = tbTitolo->getField(tbTitolo->bid); // const
			CString id((char *)"IT\\ICCU\\", 8);
			int pos =3;
			if (isAnticoE(bid))
				pos = 4;

			id.AppendString(bid, pos); // Prendi il Polo+1
			id.AppendChar('\\');
			id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid

			sf = new Subfield('9', &id);
			//sf->setData();
		}
		df->addSubfield(sf);
	}
	return df;
} // End creaTag312NotaLegameVariante


void Marc4cppLegami::creaLegamiTitoliTesauro() {
	string str;
	unsigned int pos;
	char bid[10 + 1];
	bid[10] = 0;
	DataField *df;

	// Cicliamo sul figli di root per cercare gli autori
	tree<std::string>::pre_order_iterator it = reticolo.begin();

	str = *it;
	int bidStart = str.find_last_of(':');
	char *BufTailPtr, *aString;
	BufTailPtr = bid;
	aString = (char *) str.data() + bidStart + 1;
	MACRO_COPY_FAST(10);
//	memcpy(bid, (char *) str.data() + bidStart + 1, 10);

	//	std::cout << "AUT children of :" << *it << std::endl;
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); //h1
	while (ch != reticolo.end().end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia

		if ((pos = str.find(':')) != string::npos) {
			if (str.find("TES:", pos + 1) != string::npos) {
				//				std::cout << str << std::endl;
				//entryReticoloPtr = (char *)str.data();
				if (df = creaLegameTitoloTesauro((char *) str.data(), pos))
					marcRecord->addDataField(df);
			}
		}
		++ch;
	}
	//	std::cout << std::endl;

} // End Marc4cppLegami::creaLegamiTitoliTesauro


DataField * Marc4cppLegami::creaLegameTitoloTesauro(char *entryReticoloPtr, int pos) {
	DataField *df = 0;

	// Prendiamo il DID per accedere alla tb_termine_tesauro
	char did[10 + 1];
	did[10] = 0;
//	CString cdRelazione;

	char *BufTailPtr, *aString;
	BufTailPtr = did;
	aString =  entryReticoloPtr + pos + 5;
	MACRO_COPY_FAST(10);

	bool retb;

	retb = tbTermineTesauro->loadRecord(did);
	if (!retb)
		return 0; // Record non trovato

	df = creaTag689_Tesauro();

	return df;
} // End Marc4cppLegami::creaLegameTitoloTesauro


/*!
\brief <b>Tag 696 - Variante di soggetto (legame desrittore - descrittore)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>696</td></tr>
<tr><td valign=top>Descrizione</td><td>Variante di soggetto</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Prima variante
        <li>x - Varianti successive
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
// 28/06/2016
// Crea legame descrittore descrittore per i descrittori del soggetto in questione
// NB: Record del soggetto gia' caricato
DataField * Marc4cppLegami::creaLegameDescrittoreDescrittore() {
	DataField *df = 0;
	Subfield *sf;


	bool retb;
	long position;
	//long offset;
	char *entryPtr;
	CString entryFile;
	CString *sPtr;


	const char *cid, *cid2, *didSource, *didSource2, *didColl;
	cid = tbSoggetto->getField(tbSoggetto->cid);

	if (trSogDes->existsRecord(cid)) // NonUnique. Offset del CID punta al primo della serie
	{
		while (trSogDes->loadNextRecord())  // Prendiamo un descrittore alla volta
		{
			// Stiamo cambiando CID?
			cid2 = trSogDes->getField(trSogDes->cid);
			if (strcmp (cid, cid2))
				break; // fine

			didSource = trSogDes->getField(trSogDes->did);

//printf ("\nPrendiamo il descrittore: %s", didSource);

//retb = tbDescrittore->loadRecord(didSource);
//if (!retb)
//{
//	SignalAnError(__FILE__, __LINE__, "\nDescrittore base % s per descrittore coll %s", didSource, didColl);
//	return df;
//}
//printf ("\tDesc:'%s'", tbDescrittore->getField(tbDescrittore->ds_descrittore));


			if (trDesDes->existsRecordNonUnique(didSource))
			{
				while (trDesDes->loadNextRecord())  // Prendiamo un descrittore alla volta
				{
					// Stiamo cambiando descrittore
					didSource2 = trDesDes->getField(trDesDes->did_base);
					if (strcmp (didSource, didSource2))
					{
//						printf ("\nDescrittore collegato: %s", "--------");
//						continue;
						break;
					}
					didColl = trDesDes->getField(trDesDes->did_coll);
//printf ("\n\tdescrittore collegato a: %s", didColl);

					retb = tbDescrittore->loadRecord(didColl);
					if (!retb)
					{
						SignalAnError(__FILE__, __LINE__, "\nDescrittore base % s per descrittore coll %s", didSource, didColl);
						continue;
					}

					if (!df)
						df = new DataField((char *)"696", ' ', ' ');

					sPtr = 	tbDescrittore->getFieldString(tbDescrittore->ds_descrittore);
					sf = new Subfield('a', sPtr);
					df->addSubfield(sf);
//printf ("\ttipo Desc:'%s'",  sPtr->data());

				} // end while
			} // End se esiste legame descrittore base descrittore collegato




//			retb = tbDescrittore->loadRecord(trSogDes->getField(trSogDes->did)); // Carica il record del descrittore
//			if (!retb)
//			{
//				SignalAnError(__FILE__, __LINE__, "\nDescrittore % s per soggetto %s", trSogDes->getField(trSogDes->did), cid);
//				continue;
//			}
//			creaTag931_LegameSoggettoDescrittore();
		} // end while

	} // End se esiste descrittore


	return df;
} // End Marc4cppLegami::creaLegameTitoloSoggetto


// 03/04/2018
// subfieldID puo' essere 'y' (varianti di sinonimia) o 'z' varianti storiche
DataField * Marc4cppLegami::creaSoggettoVarianteSS(CString *key_des,  CKeyValueVector *variantiKV, DataField *df, int idx, char subfieldID) {

	CString s, *sPtr;
	Subfield *sf;

//	s = key_des->data();
//	s.ToLower();
//	s.ReplaceChar(0, s.GetFirstChar()-0x20);

	if (_699_sintetica)
	{
		if (!df)
			df = new DataField((char *)"699", ' ', ' ');
	}
	else
		df = new DataField((char *)"699", ' ', ' ');

	sPtr = 	tbDescrittore->getFieldString(tbDescrittore->ds_descrittore);

	if (_699_sintetica)
		sf = new Subfield(subfieldID, (char *)variantiKV->GetValue(idx));
	else
	{
			sf = new Subfield('a', (char *)variantiKV->GetValue(idx)); // variantiKsogVarKV
			df->addSubfield(sf);
			sf = new Subfield(subfieldID, sPtr->data()); //s.data()
	}
	df->addSubfield(sf);

	if (!_699_sintetica)
		marcRecord->addDataField(df);

	while (true)
	{
		idx++;
		if (idx == variantiKV->Length())
			break;

		char *key = (char *)variantiKV->GetKey(idx);
		if (! strcmp(key_des->data(), key))
		{
			if (!_699_sintetica)
			{
			df = new DataField((char *)"699", ' ', ' ');

					sf = new Subfield('a', (char *)variantiKV->GetValue(idx));
			}
			else
				sf = new Subfield(subfieldID, (char *)variantiKV->GetValue(idx));
			df->addSubfield(sf);
			if (!_699_sintetica)
			{
					sf = new Subfield(subfieldID, sPtr->data()); // s.data()
				df->addSubfield(sf);
				marcRecord->addDataField(df);
			}
		}
		else
			break;
	} ;


	return df;
}


/*!
\brief <b>Tag 699 - Variante di soggetto (legame desrittore - descrittore) (da file fornito da CFI)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>696</td></tr>
<tr><td valign=top>Descrizione</td><td>Variante di soggetto (storica o sinonimia)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Prima variante
        <li>x - Varianti successive
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
// DataField *
void Marc4cppLegami::creaLegameSoggettoVariante() {
	DataField *df = 0;
	Subfield *sf;
	char subfieldId;

	bool retb;
	long position;
	//long offset;
	char *entryPtr;
	CString entryFile;
	CString *sPtr;
	CKeyValueVector *scomposizioniKVV = 0;
	CKeyValueVector *compostoNonPreferitoKVV = 0;
	CString s;
	CKeyValueVector *variantiSinonimiaKV = 0;
	CKeyValueVector *variantiStoricheKV = 0;
	bool trovataVariante;


	const char *cid, *cid2, *didSource, *didSource2, *didColl;
//	int cd_soggettario = SOGGETTARIO_NON_DEFINITO;
	cid = tbSoggetto->getField(tbSoggetto->cid);

//printf ("\n\n\tCid: %s, Desc:'%s'", cid, tbSoggetto->getField(tbSoggetto->ds_soggetto));

	if (trSogDes->existsRecord(cid)) // NonUnique. Offset del CID punta al primo della serie
	{
		while (trSogDes->loadNextRecord())  // Prendiamo un descrittore alla volta
		{
			// Stiamo cambiando CID?
			cid2 = trSogDes->getField(trSogDes->cid);
			if (strcmp (cid, cid2))
				break; // fine

			didSource = trSogDes->getField(trSogDes->did);

			//printf ("\nPrendiamo il descrittore: %s", didSource);
			retb = tbDescrittore->loadRecord(didSource);
			if (!retb)
			{
				SignalAnError(__FILE__, __LINE__, "\nDescrittore base % s per descrittore coll %s", didSource, didColl);
				continue;
			}
			CString *key_des = tbDescrittore->getFieldString(tbDescrittore->ky_norm_descritt);
			key_des->Strip(key_des->trailing);
//printf ("\n\tDid: %s, Desc:'%s', key:'%s'", didSource, tbDescrittore->getField(tbDescrittore->ds_descrittore), key_des->data());


			// Vediamo se abbiamo varianti di sinonimia per questo descrittore
			variantiSinonimiaKV = variantiSinonimiaKsogVarKV;
			trovataVariante = false;
			int	idx = variantiSinonimiaKV->GetFirstIndexFromSortedKey(key_des->data());
			if (idx == -1)
			{
				variantiSinonimiaKV = variantiSinonimiaKvarSogKV;
				idx = variantiSinonimiaKV->GetFirstIndexFromSortedKey(key_des->data());
			}

			if (idx > -1)
			{
			df = creaSoggettoVarianteSS(key_des,  variantiSinonimiaKV, df, idx, 'y');
			trovataVariante = true;
			} // End if variante


			// Vediamo se abbiamo varianti storiche per questo descrittore
			variantiStoricheKV = variantiStoricheKsogVarKV;
			idx = variantiStoricheKV->GetFirstIndexFromSortedKey(key_des->data());
			if (idx == -1)
			{
				variantiStoricheKV = variantiStoricheKvarSogKV;
				idx = variantiStoricheKV->GetFirstIndexFromSortedKey(key_des->data());
			}

			if (idx > -1)
			{
			df = creaSoggettoVarianteSS(key_des,  variantiStoricheKV, df, idx, 'z');
			trovataVariante = true;
			} // End if variante

			if (trovataVariante)
				continue; // skip resto dopo aver fatto la variante



			CString value; // 18/04/2018
//			// Vediamo se abbiamo una scomposizione per questo descrittore 26/01/2018
//			ATTValVector <CString *> localVect;
//			ATTValVector <CString *> localVectUC;
//			CString value = scomposizioneSoggettoKV->GetValueFromKey(key_des->data());
//			if (value.Length() > 0 )
//			{
//			// scomponiamo e valori e costruiamo un kvalue vector
//				if (!scomposizioniKVV)
//					scomposizioniKVV = new CKeyValueVector(tKVSTRING, tKVSTRING);
//				value.Split(localVect, '|');
//				for (int i=0; i < localVect.length(); i++)
//					scomposizioniKVV->Add(localVect.Entry(i)->data(), key_des->data());
//				localVect.DeleteAndClear();
//			}
//			else
//			{ // e' un composto non preferito?
				value = compostoNonPreferitoKV->GetValueFromKey(key_des->data());
				if (value.Length() > 0 )
				{
					if (!compostoNonPreferitoKVV)
						compostoNonPreferitoKVV = new CKeyValueVector(tKVSTRING, tKVSTRING);
					compostoNonPreferitoKVV->Add(key_des->data(), value.data());
				}
//			}



		} // end while sog_des
	} // End se esiste descrittore

	// Abbiamo scomposizioni da gestire? 25/01/2018
//	if (scomposizioniKVV) //  && scomposizioniKVV->Length() > 0
//	{

		if (_699_sintetica)
		{
//			if (scomposizioniKVV)
//				df = creaSoggettoCompostoNonPreferito(scomposizioniKVV, df);
			if (compostoNonPreferitoKVV)
				df = creaCompostoNonPreferito(compostoNonPreferitoKVV, df);
			marcRecord->addDataField(df);
		}
		else
		{
//			if (scomposizioniKVV)
//				creaSoggettoCompostoNonPreferito(scomposizioniKVV, 0);
			if (compostoNonPreferitoKVV)
				creaCompostoNonPreferito(compostoNonPreferitoKVV, 0);

		}
//		if (scomposizioniKVV)
//			delete scomposizioniKVV;
		if (compostoNonPreferitoKVV)
			delete compostoNonPreferitoKVV;
//	}


//	return df;
} // End Marc4cppLegami::creaLegameSoggettoVariante



DataField * Marc4cppLegami::creaSoggettoCompostoNonPreferito(CKeyValueVector *scomposizioniKVV, DataField *df=0) {
	Subfield *sf;
	char subfieldId;
//	DataField * df;
	CString s;
	// Ordiniamo le scomposizioni per composto non preferito
			scomposizioniKVV->SortAscendingByKey();

			int ctr = 0;
			int i=1;
			for (; i < scomposizioniKVV->Length(); i++)
			{

				if (!strcmp((char *)scomposizioniKVV->GetKey(i), (char *)scomposizioniKVV->GetKey(i-1)))
				{ // Stringhe uguali
					if (!ctr)
						ctr=2;
					else
						ctr++;
				}
				else
				{ // Stringhe diverse
					if (ctr)
					{
//printf ("\n%s, %d occ., %s", (char *)scomposizioniKVV->GetKey(i-1), ctr, (char *)scomposizioniKVV->GetValue(i-1));
//printf ("\n%s", (char *)scomposizioniKVV->GetKey(i-1));

//						int occ = *((int *)composto_non_preferitoKV->GetValuePtrFromSortedKey((char *)scomposizioniKVV->GetKey(i-1)));

						int occ;
						int *intPtr = (int *)composto_non_preferito_ctr_KV->GetValuePtrFromSortedKey((char *)scomposizioniKVV->GetKey(i-1));
						if (intPtr)
							occ = *(intPtr);

						if (occ && ctr >= occ)
						{
							df = creaSoggettoCNP1(scomposizioniKVV, df, i, ctr);
						}
						ctr = 0;
					}
				}
			} // end for

			if (ctr)
			{
				df = creaSoggettoCNP1(scomposizioniKVV, df, i, ctr);
			}
	return df;
} // End creaSoggettoCompostoNonPreferito


DataField * Marc4cppLegami::creaCompostoNonPreferito(CKeyValueVector *compostoNonPreferitoKVV, DataField *df) {
	ATTValVector <CString *> localVect;
//	ATTValVector <CString *> localVectUC;
	Subfield *sf;

	for (int i=0; i < compostoNonPreferitoKVV->Length(); i++)
	{
//		printf ("\ncomposto non preferito: %s", (char *)compostoNonPreferitoKVV->GetKey(i));

		CString value = (char *)compostoNonPreferitoKVV->GetValue(i);
			value.Split(localVect, '|');
			if (_699_sintetica)
			{
			if (!df)
				df = new DataField((char *)"699", ' ', ' ');

			for (int j=0; j < localVect.length(); j++)
				{
				sf = new Subfield('x', localVect.Entry(j)->data());
//				sf = new Subfield('a', localVect.Entry(j)->data()); // 10/04/2018 Prove per la Roveri
				df->addSubfield(sf);
				}
			}
			else
			{ // 699 estesa

				df = new DataField((char *)"699", ' ', ' ');

				CString s = (char *)compostoNonPreferitoKVV->GetKey(i);
				s.ToLower();
				s.ReplaceChar(0, s.GetFirstChar()-0x20);

				sf = new Subfield('a', s.data());
				df->addSubfield(sf);
				for (int j=0; j < localVect.length(); j++)
					{
					sf = new Subfield('x', localVect.Entry(j)->data());
					df->addSubfield(sf);
					}

// 10/04/2018 Prove per la Roveri
//				for (int j=0; j < localVect.length(); j++)
//					{
//					sf = new Subfield('a', localVect.Entry(j)->data());
//					df->addSubfield(sf);
//					}
//				sf = new Subfield('x', s.data());
//				df->addSubfield(sf);


				marcRecord->addDataField(df);
			}


			localVect.DeleteAndClear();
	}
	if (_699_sintetica)
		return df;
	else
		return 0;
	}



DataField * Marc4cppLegami::creaSoggettoCNP1(CKeyValueVector *scomposizioniKVV, DataField *df, int i, int ctr) {
	Subfield *sf;
	CString s;

	if (_699_sintetica)
	{
		if (!df)
			df = new DataField((char *)"699", ' ', ' ');
		sf = new Subfield('x', (char *)scomposizioniKVV->GetKey(i-1));

	}
	else
	{
		df = new DataField((char *)"699", ' ', ' ');
		sf = new Subfield('a', (char *)scomposizioniKVV->GetKey(i-1));
	}
	df->addSubfield(sf);

	if (!_699_sintetica)
	{
		for (int j=1; j<= ctr; j++)
		{
			s = (char *)scomposizioniKVV->GetValue(i-j);
			s.ToLower();
			s.ReplaceChar(0, s.GetFirstChar()-0x20);

			sf = new Subfield('x', s.data());
			df->addSubfield(sf);
		}
		marcRecord->addDataField(df);
		return 0;
	}
	else
		return df;


// 10/04/2018 Prove per la Roveri
//	if (_699_sintetica)
//		{
//			if (!df)
//				df = new DataField((char *)"699", ' ', ' ');
//			sf = new Subfield('a', (char *)scomposizioniKVV->GetKey(i-1));
//
//		}
//		else
//		{
//			if (!df)
//				df = new DataField((char *)"699", ' ', ' ');
//			for (int j=1; j<= ctr; j++)
//			{
//				s = (char *)scomposizioniKVV->GetValue(i-j);
//				s.ToLower();
//				s.ReplaceChar(0, s.GetFirstChar()-0x20);
//
//				sf = new Subfield('a', s.data());
//				df->addSubfield(sf);
//			}
//		}
//
//		if (!_699_sintetica)
//		{
//			sf = new Subfield('x', (char *)scomposizioniKVV->GetKey(i-1));
//			df->addSubfield(sf);
//			marcRecord->addDataField(df);
//			return 0;
//		}
//		else
//			return df;


}
