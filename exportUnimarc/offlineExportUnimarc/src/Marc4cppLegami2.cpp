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
 * Marc4cppLegami.cpp
 *
 *  Created on: 29-dic-2008
 *      Author: Arge

 -------
 Caso di notizia documento (M, S) SOLO ARRIVO di legami 01.
 Non ha relazioni 01 ad altri titoli
 Quindi puo essere arrivo per le 462, 463, 464. NON puo avere  461 per non e' partenza di nessun legame 01

 Controllo se qualcuno mi fa riferimento con legame 01 e natura M e W o N
 Se SI per ogni riferimento cre una 46x. Con x = 4 se il riferimento e di natura N
 3 se il riferimento NON e a sua volta riferimento di legame 01 con nature M o W
 2 se il riferimento e a sua volta riferimento di legame 01 con natura M o W
 vedi CFI0007917
 -------

 Caso di notizia documento (M, S, W, N) PARTENZA di legami 01.
 Ha relazioni 01M, 01S ad altro titolo. Ce ne puo essere uno solo.

 Creo un legame 46x alla notizia di arrivo   con x = 1 se la notizia di arrivo arrivo NON HA a sua volta relazioni 01M, 01S ad altro titolo
 = 2 se la notizia di arrivo arrivo HA a sua volta relazioni 01M, 01S ad altro titolo

 Controllo se qualcuno mi fa riferimento con legame 01 e natura M e W o N
 Se SI per ogni riferimento cre una 46x. Con x = 4 se il riferimento e di natura N
 3 se il riferimento NON e a sua volta riferimento di legame 01 con nature M o W
 2 se il riferimento e a sua volta riferimento di legame 01 con natura M o W

 Vedi CFI0209015
 ---------



#
# Gestione livelli gerarchici
# ------------------
# 461 legame gerarchico ad una monografia superiore di livello massimo (SET).
#   Il legame pu� provenire dal SUBSET o PIECE.
# 462 Legame gerarchico ad una monografia intermedia (SUBSET).
#   Il legame pu� provenire dal SET o PIECE.
# 463 Legame gerarchico ad una monografia inferiore (PIECE (volume fisico) e SPOGLIO).
#   Legame al PIECE se proveniente da SET o SUBSET,
#   Legame allo SPOGLIO se proveniente dal PIECE
# 464 Legame gerarchico ad una monografia inferiore (PIECE).
#   Legame al PIECE se proveniente dallo SPOGLIO (natura N),
# Codice notizia da mettere nel leader
#   0 non gerarchica  (no relazioni a 46x)
#   1 notizia top
#   2 notizia intermedia o base (verso intermedia o top)
#
# SET = Opera di livello superiore
# SUBSET = Opera di livello intermedio
# PIECE = Opera di livello inferiore
# SPOGLIO = recensione dell'opera di livello inferiore
#
#             +-- SPOGLIO <-
#         463 |            | 464
#             |       +----+
#             +-> PIECE----------------+
#         463 |     | ^----------+     | 461
#             +---+ |462         |     |
#                 | +-------     |463  |
#                 |        |     |     |
#             +-- SUBSET <-+     |     |
#         461 |            | 462 |     |
#             +-->SET  ----+     |     |
#             |     +------------+     |
#             +------------------------+
#

SBL0456271  # Storia di Firenze / Robert Davidsohn

# Contiene: (462 e 463)

# Legami a notizie intermedie (SUBSET)
#-------------------------------------
# 4: I primordi della civilt� fiorentina                                 #462 SBL0467948  4: I ?primordi della civilt� fiorentina
# 2 : 2: Guelfi e ghibellini                                             #462 SBL0360199  2: ?Guelfi e ghibellini
# 4 : 4: I primordi della civilt� fiorentina                             #462 RML0057137  4: I ?primordi della civilt� fiorentina

# Legami a notizie base (PIECE) che non hanno intermedie
#-------------------------------------
# Indici / Robert Davidsohn ; a cura di Lucia Belmonte e Linda Clerici   #463 SBL0601589  Indici Robert Davidsohn a cura di Lucia Belmonte e Linda Clerici
# 1 : 1: Le origini / Robert Davidsohn                                   #463 SBL0456272  1: Le ?origini Robert Davidsohn 1
# 3 : 3: Le ultime lotte contro l'impero / Robert Davidsohn              #463 SBL0460844  3: Le ?ultime lotte contro l'impero Robert Davidsohn 3
# 3 : 3: Le ultime lotte contro l'impero / Robert Davidsohn              #463 RML0057136  3: Le ?ultime lotte contro l'impero Robert Davidsohn 3
# 4.3 : 4.3: Le ultime lotte contro l'Impero                             #463 SBL0617803  4.3: Le ?ultime lotte contro l'Impero 4.3
# 8 : 8: Indici / a cura di Lucia Belmonte e Linda Clerici               #463 UFI0252243  ?8: ?Indici a cura di Lucia Belmonte e Linda Clerici 8

SBL0360199 # Guelfi e ghibellini.

# Fa parte di: (461) opera superiore
#   Storia di Firenze / Robert Davidsohn                                 =461   1$1 001SBL0456271$1 2001 $aStoria di Firenze$fRobert Davidsohn. $v2

# Contiene: (463)
# 2.2: L'egemonia guelfa e la vittoria del popolo / Robert Davidsohn     =463   1$1 001NAP0051172$1 2001 $aH2.2: L'Iegemonia guelfa e la vittoria del popolo$fRobert Davidsohn. $v2
# 1: Lotte sveve / Robert Davidsohn                                      =463   1$1 001NAP0065158$1 2001 $aH2.1: ILotte sveve$fRobert Davidsohn. $v1
# 1 : 2.1: Lotte sveve / Robert Davidsohn                                =463   1$1 001RML0057134$1 2001 $aH1: ILotte sveve$fRobert Davidsohn. $v1
# 2.2: L'egemonia guelfa e la vittoria del popolo / Robert Davidsohn     =463   1$1 001SBL0360200$1 2001 $aH2.2: L'Iegemonia guelfa e la vittoria del popolo$fRobert Davidsohn. $v


RML0057134  # Lotte sveve
# Fa parte di: (462) opera intermedia
#    1 : Guelfi e ghibellini                                             462 SBL0360199 Guelfi e ghibellini 1
#

# fine livelli gerarchici
# ------------------

*/


#include "Marc4cppLegami.h"
#include "BinarySearch.h"
#include <bits/stringfwd.h>
#include "TbReticoloTit.h"
#include "../include/library/CTokenizer.h"
#include "TbfBibliotecaInPolo.h"
#include "../include/library/CString.h"
#include "library/CMisc.h"
#include "MarcGlobals.h"
#include "TbReticoloAut.h"
#include <stdlib.h>
//#include <stdio.h>

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

//#ifdef EVOLUTIVA_POLO_2014
	extern CKeyValueVector *codiciEclaKV; // 10/12/14
//#endif
using namespace std;

extern void SignalAnError(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);




void Marc4cppLegami::creaLegamiTitoloPersonaggi() {
	//char bid[10+1];	bid[10]=0;
	if (!IS_TAG_TO_GENERATE(927))
		return;

	const char *bid = tbTitolo->getField(tbTitolo->bid);

	if (tbPersonaggio->existsRecord(bid)) {
		while (tbPersonaggio->loadNextRecord(bid)) {
			//			// Stop al cambio BID
			//			if (strcmp(tbPersonaggio->getField(tbPersonaggio->bid), bid))
			//			{
			//				break; // Trovato NO
			//			}
			// Crea la 927
			creaTag927_PersonaggiEInterpreti();
		} // end while
	}
} // End Marc4cppLegami::creaLegamiTitoloPersonaggi



































void Marc4cppLegami::elaboraOrdini() {

	// Il campo viene definito solo per la nature M, W, S.
	char natura = *(tbTitolo->getField(tbTitolo->cd_natura));
	if (natura != NATURA_M_MONOGRAFIA
		&& natura != NATURA_S_PERIODICO
		&& natura != NATURA_W_VOLUME_PRIVO_DI_TITOLO_SIGNIFICATIVO)
		return;

	// Abbiamo ordini per questo titolo (BID)?
	const char *bid = tbTitolo->getField(tbTitolo->bid);
	bool retb;

//	if (tbaOrdini->existsRecord(bid)) {
	if (tbaOrdini->existsRecordNonUnique(bid)) {
		while (tbaOrdini->loadNextRecord(bid)) // bid
		{
			char ordineContinuativo = *(tbaOrdini->getField(tbaOrdini->continuativo));
			const char *recordBid = tbaOrdini->getField(tbaOrdini->bid);
			if (strcmp(recordBid, bid)) { // Stop al cambio BID o EOF
				break;
			}
			if (IS_TAG_TO_GENERATE(951))
				retb = creaTag951_Ordine();

			if (IS_TAG_TO_GENERATE(961))
			{
//tbaOrdini->dumpRecord();
				CString *sPtr = tbaOrdini->getFieldString(tbaOrdini->cod_1ord);
				CString *statoOrdinePtr = tbaOrdini->getFieldString(tbaOrdini->stato_ordine);
				if (natura == NATURA_S_PERIODICO
					&& ordineContinuativo == '1'
					&& (sPtr->IsEmpty() || sPtr->isEqual("0")) // Controlliamo che si il primo ordine	// 16/07/2012 Fix per null
					&& (statoOrdinePtr->GetFirstChar() == 'A' || statoOrdinePtr->GetFirstChar() == 'C') // 16/07/2012 Fix per stato ordine
					)
					retb = creaTag961_Ordine();
			}
		}
	}

} // end elaboraOrdini







DataField * Marc4cppLegami::creaSubtag200(DataField *df) {
	Subfield *sf;
	string str;
	//	DataField *df200;

//printf ("\nMarc4cppLegami::creaSubtag200");

	CString s;
//	s = " 001";
	s = "001"; // 15/01/2010 13.47

//	s.AppendString(tbTitolo->getField(tbTitolo->bid));	// 28/04/10

	char *bid = tbTitolo->getField(tbTitolo->bid); // 28/04/10
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		s.AppendString(bid, 10);
	else // TIPO_SCARICO_OPAC
	{
		int pos =3;

//		CString id = "IT\\ICCU\\";
		CString id((char *)"IT\\ICCU\\", 8);
		if (isAnticoE(bid))
			pos = 4;
		id.AppendString(bid, pos); // Prendi il Polo
		id.AppendChar('\\');
		id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid
		s.AppendString(id.data());
	}



	sf = new Subfield('1', &s);
	df->addSubfield(sf);


// Testing embedded per xml
	if (!df->embedded)
		df->embedded = new Embedded();

	ControlField *cf = new ControlField((char *)"001");
	cf->setData(&s);
	df->embedded->addControlField(cf);


	return creaSubtag200Area1(df, true, true); // Crea tag 200

} // End Marc4cppLegami::creaSubtag200

void Marc4cppLegami::creaLegamiTitoloIncipit() {
	//char bid[10+1];	bid[10]=0;
	if (!IS_TAG_TO_GENERATE(926))
		return;

	const char *bid = tbTitolo->getField(tbTitolo->bid);

//	if (tbIncipit->existsRecord(bid)) {
	if (tbIncipit->existsRecordNonUnique(bid)) {
		while (tbIncipit->loadNextRecord(bid)) {
			//			// Stop al cambio BID
			//			if (strcmp(tbIncipit->getField(tbIncipit->bid), bid))
			//			{
			//				break; // Trovato NO
			//			}
			// Crea la 926
				creaTag926_Incipit();
		} // end while
	}
} // End Marc4cppLegami::creaLegamiTitoloIncipit
















































































void Marc4cppLegami::creatTag7xxNidificato(DataField *df, CString *bid)
{
	DataField *df7xx;
	long position;
	char *entryPtr;
	CString entryFile;
	Subfield *sf;

//printf ("\nMarc4cppLegami::creatTag7xxNidificato");

	// BID potrebbe essere non in reticolo
	bool retb;
	if (trTitAutRel->getOffsetBufferTbPtr())
		retb = BinarySearch::search(
				trTitAutRel->getOffsetBufferTbPtr(),
				trTitAutRel->getElementsTb(),
				trTitAutRel->getKeyPlusOffsetPlusLfLength(),
				bid->data(),
				trTitAutRel->getKeyLength(),
				position,
				&entryPtr);
	else
	{
		retb = BinarySearch::search(
				trTitAutRel->getTbOffsetIn(),
				trTitAutRel->getElementsTb(),
				trTitAutRel->getKeyPlusOffsetPlusLfLength(),
				bid->data(),
				trTitAutRel->getKeyLength(), position, &entryFile);

				entryPtr = entryFile.data();
	}


	if (!retb)
		return;




//	CString *lineRead = new CString (2048);
	CString *lineRead = new CString (4096);
//	CString *lineRead = &FixedLengthLine;

	// Dalla posizione prendiamo l'offset
//	long offset = atol (entryPtr+trTitAutRel->getKeyLength()); // offsetBuffertrTitAutRelPtr+position
//	long offset;
	int offset; // 18/03/2014 32 bit

	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
		//memcpy (&offset, entryPtr+trTitAutRel->getKeyLength(), 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+trTitAutRel->getKeyLength())); // 24/03/2015
	else
		offset = atoi (entryPtr+trTitAutRel->getKeyLength()); // OFFSET in ASCII




	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/autore
	trTitAutRel->getTbIn()->SeekTo(offset);
	if (!lineRead->ReadLineWithPrefixedMaxSize(trTitAutRel->getTbIn()))
        SignalAnError(__FILE__, __LINE__, "read failed");

//printf ("\nlineRead = '%s'", lineRead->data());

	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer *Tokenizer = new CTokenizer("|");
	OrsChar *Token;

	Tokenizer->Assign(lineRead->data());

	Token = Tokenizer->GetToken(); // Remove root
	CString s;
	ATTValVector<Subfield*> *subfieldsVector;



	while(*(Token = Tokenizer->GetToken()))
	{

		TbReticoloAut *tbReticoloAut = new TbReticoloAut(Token);
		// Legami solo a tipo di responsabilita' 1
        df7xx = creaLegameTitoloAutore(
        		tbReticoloAut->getField(tbReticoloAut->vid),
        		*tbReticoloAut->getField(tbReticoloAut->tp_responsabilita),
        		tbReticoloAut->getFieldString(tbReticoloAut->cd_relazione),
        		tbReticoloAut->getFieldString(tbReticoloAut->cd_strumento_mus)
        		);

        if (df7xx) {
            subfieldsVector = df7xx->getSubfields();
            Subfield* sf7xx;
            // s = " ";
            s.Clear();
            s.AppendString(df7xx->getTagString());
            s.AppendChar(df7xx->getIndicator1());
            s.AppendChar(df7xx->getIndicator2());

//printf ("\nSubfield data =%s", s.data());
            sf = new Subfield('1', &s);
            df->addSubfield(sf);

            for (int i = 0; i < subfieldsVector->length(); i++) {
                sf7xx = subfieldsVector->Entry(i);
                sf = new Subfield(sf7xx->getCode(), sf7xx->getDataString());
                df->addSubfield(sf);
            }

            delete df7xx;
        }

        delete tbReticoloAut; // 15/11/2010

	}
	delete Tokenizer;
	delete lineRead;
} // End Crea creatTag7xxNidificati()






void Marc4cppLegami::appendSubtag200Area1DolA(CString *destS, bool fieldTag = true, bool keepNoSort = true) {
	//Subfield *sf;
	string str;
//	DataField *df200;
//	CString s;

	// Costruiamo la 200 standard
	CString * strIndiceAree = tbTitolo->getFieldString(tbTitolo->indice_isbd);
	ATTValVector<CString *> areeVect;
	bool retb;
	retb = strIndiceAree->Split(areeVect, ';');
	int length; // , startOffset
	char *areaStartPtr, *areaEndPtr, *ptr; // , *areaNotePtr


	areaStartPtr = (char *) tbTitolo->getField(tbTitolo->isbd);
	if (areeVect.Length() > 1 && !areeVect.Entry(1)->IsEmpty()) //  !areeVect.Last()->IsEmpty()
		//length = atoi(areeVect.Entry(1)->data() + 4) - 3;
		length = atoi(areeVect.Entry(1)->data() + 4) - 4 -1; // 05/10/2010
	else {
		length = strlen(areaStartPtr); //

	}

	//  07/10/2009 11.33
	if (!keepNoSort) {
		// Togliamo il no sort
		ptr = strchr(areaStartPtr, '*');
		if (ptr) {
			length -= (ptr + 1 - areaStartPtr);
			areaStartPtr = ptr + 1;
		}
		if (*(areaStartPtr + length - 1) == '-')
			length -= 2;
	}

	areaEndPtr = areaStartPtr + length; // ' - ' + EOS
	*areaEndPtr = 0;


	if (ptr = strstr (areaStartPtr, " : "))
	{
		length = ptr - areaStartPtr;
		*ptr = 0;
	}
	if (ptr = strstr (areaStartPtr, " / ")) // 19/10/2010
	{
		length = ptr - areaStartPtr;
		*ptr = 0;
	}

	destS->AppendString(areaStartPtr, length); // 09/09/2010 11.56

	if (ptr)
		*ptr = ' ';


	//
//	df200 = marc4cppDocumento->creaTag200(areaStartPtr, areaEndPtr);
	areeVect.DeleteAndClear();
//
//	// Cicliamo sui sottocampi e popoliamo il vettore
//	ATTValVector<Subfield*> *subfieldsVector;
//	subfieldsVector = df200->getSubfields();
//	Subfield* sf200;
//
//	if (fieldTag) {
//		// destS->AppendString(" "); 15/01/2010 13.50
//		destS->AppendString(df200->getTag());
//		destS->AppendChar(df200->getIndicator1());
//		destS->AppendChar(df200->getIndicator2());
//
//		//		sf = new Subfield('1');
//		//		sf->setData(s.Data());
//		//		df->addSubfield(sf);
//
//	}
//
//	if (subfieldsVector->Length()) // 29/01/20010
//	{
//		for (int i = 0; i < 1; i++) // subfieldsVector->length(), solo $a
//		{
//			sf200 = subfieldsVector->Entry(i);
//			//		sf = new Subfield(sf200->getCode());
//			//		sf->setData(sf200->getData());
//			//		df->addSubfield(sf);
//			destS->AppendString(sf200->getData());
//		}
//	}
//	delete df200;

	//return df; // Set tutto ok df Popolato
} // End Marc4cppLegami::appendSubtag200Area1$a












DataField * Marc4cppLegami::creaSubtag200Area1(DataField *df, bool fieldTag = true, bool keepNoSort = true) {
	Subfield *sf;
	string str;
	DataField *df200;
	CString s;

//	s = tbTitolo->getField(tbTitolo->bid);

	// Costruiamo la 200 standard
	CString * strIndiceAree = tbTitolo->getFieldString(tbTitolo->indice_isbd);


	ATTValVector<CString *> areeVect;
	bool retb;
	retb = strIndiceAree->Split(areeVect, ';');
	int length; // , startOffset
	char *areaStartPtr, *areaEndPtr, *ptr; // , *areaNotePtr


	areaStartPtr = (char *) tbTitolo->getField(tbTitolo->isbd);
	if (areeVect.Length() > 1 && !areeVect.Entry(1)->IsEmpty()) //  !areeVect.Last()->IsEmpty()
//		length = atoi(areeVect.Entry(1)->data() + 4) - 3; //
		length = atoi(areeVect.Entry(1)->data() + 4) - 4 -1; // '. - '
	else {
		length = strlen(areaStartPtr); //

	}

	//  07/10/2009 11.33
	if (!keepNoSort) {
		// Togliamo il no sort
		ptr = strchr(areaStartPtr, '*');
		if (ptr) {
			length -= (ptr + 1 - areaStartPtr);
			areaStartPtr = ptr + 1;
		}
		if (*(areaStartPtr + length - 1) == '-')
			length -= 2;
	}

//	areaEndPtr = areaStartPtr + length -1; // ' - ' + EOS
	areaEndPtr = areaStartPtr + length ; // 24/09/20010 ANA0000041

	*areaEndPtr = 0;
	s.assign(areaStartPtr);

	df200 = marc4cppDocumento->creaTag200_AreaTitoloEResponsabilita(&s); // areaStartPtr, areaEndPtr
	areeVect.DeleteAndClear();

	// Cicliamo sui sottocampi e popoliamo il vettore
	ATTValVector<Subfield*> *subfieldsVector;
	subfieldsVector = df200->getSubfields();
	Subfield* sf200;

	DataField *dfLocale = 0;

	if (fieldTag) {
		//s = " "; // 15/01/2010 10.47
		s.Clear();
		s.AppendString(df200->getTagString());
		s.AppendChar(df200->getIndicator1());
		s.AppendChar(df200->getIndicator2());

		sf = new Subfield('1', &s);
		//sf->setData();
		df->addSubfield(sf);

		if (df->embedded)
		{
		dfLocale = new DataField(df200->getTagString(), df200->getIndicator1(), df200->getIndicator2());
		df->embedded->addDataField(dfLocale);
		}

	}

	for (int i = 0; i < subfieldsVector->length(); i++) {
		sf200 = subfieldsVector->Entry(i);

		sf = new Subfield(sf200->getCode(), sf200->getDataString());
		//ptr = sf200->getData();
		//sf->setData();
		df->addSubfield(sf);

		if (dfLocale)
		{
			sf = new Subfield(sf200->getCode(), sf200->getDataString());
			//sf->setData();
			dfLocale->addSubfield(sf);
		}


	}
	delete df200;

	return df; // Set tutto ok df Popolato
} // End Marc4cppLegami::creaSubtag200Area1















void Marc4cppLegami::elabora46y(char *bid, TbReticoloTit *tbReticoloTit)
{
	CString legame;
//printf ("\nMarc4cppLegami::elabora46y, bid=%s", bid);


	//	char buf[80];
	char naturaBase, naturaColl;
	DataField *df;
	bool bidHaPadre = false;

	CString *bidReticoloPtr = tbReticoloTit->getFieldString(tbReticoloTit->bid);
	// E' un legame valido?
	if (!tbReticoloTit->getFieldString(tbReticoloTit->tp_legame)->isEqual(CD_LEGAME_01_FA_PARTE_DI)) // !TokenTpLegame.isEqual(CD_LEGAME_01_FA_PARTE_DI)
		return; // Non e' un legame di tipo valido

	// E' un legame ad una natura valida?
	naturaBase = tbReticoloTit->getFieldString(tbReticoloTit->cd_natura_base)->GetFirstChar(); // TokenCdNaturaBase.GetFirstChar();
	naturaColl = tbReticoloTit->getFieldString(tbReticoloTit->cd_natura_coll)->GetFirstChar(); // TokenCdNaturaBase.GetFirstChar();

	if (	naturaBase != NATURA_M_MONOGRAFIA &&
			naturaBase != NATURA_W_VOLUME_PRIVO_DI_TITOLO_SIGNIFICATIVO &&
			naturaBase != NATURA_N_TITOLO_ANALITICO
			)
		return; // Non e' una natura valida
	// Abbiamo trovato un riferimento di partenza
	//	     crea una 46x. Con x = 	4 se il riferimento e' di natura N
	//								3 se il riferimento NON e a sua volta riferimento di legame 01 con nature M o W
	//								2 se il riferimento e a sua volta riferimento di legame 01 con natura M o W
	//								1 se il riferimento non e' partenza di legame 01

	// Carichiamo il titolo che fa riferimento a
	tbTitolo->loadRecord(bidReticoloPtr->data());
	df = 0;
	if (!isPartenzaLegame01MW(bidReticoloPtr->data()))
	{
		if (IS_TAG_TO_GENERATE(461))
		{
		if (df = creaTag461_FaParteDi_NotiziaSuperiore(tbReticoloTit->getFieldString(tbReticoloTit->sequenza))) //
			marcRecord->addDataField(df);
		}
		bidHaPadre = true;
//printf ("\ncreaTag461_FaParteDi_NotiziaSuperiore");
// Arge 27/01/2011 per problema 463 con IEI, IEI0307468
//tbTitolo->loadRecord(bid);

	}
//	else if (isRiferimentoLegame01MW(bidReticoloPtr->data()))
	else if (isRiferimentoLegame01MW_2(bidReticoloPtr->data(), bid))
	{

		//if (naturaColl == 'S')  // Legame a titolo superiore e non itermedio Mantis 0004555
		if (naturaColl == 'S' || (naturaBase == 'W' && naturaColl == 'M')) // 10/03/2016
		{
			if (IS_TAG_TO_GENERATE(461))
//				df = creaTag461_FaParteDi_NotiziaSuperiore(tbReticoloTit->getFieldString(tbReticoloTit->sequenza));
				if (df = creaTag461_FaParteDi_NotiziaSuperiore(tbReticoloTit->getFieldString(tbReticoloTit->sequenza))) // 14/02/2018
					marcRecord->addDataField(df); // Mail Roveri  lunedì 18 febbraio 2013 16.55
		}
		else
		{
			if (IS_TAG_TO_GENERATE(462))
				if (df = creaTag462_LegameALivelloIntermedio(bidReticoloPtr, tbReticoloTit->getFieldString(tbReticoloTit->sequenza)))
					marcRecord->addDataField(df);
		}
	}
	else
	{
		if (naturaColl == 'S')  // Mail Roveri  lunedì 18 febbraio 2013 16.55 // Legame a titolo superiore
		{
			if (IS_TAG_TO_GENERATE(461))
				df = creaTag461_FaParteDi_NotiziaSuperiore(tbReticoloTit->getFieldString(tbReticoloTit->sequenza));
					marcRecord->addDataField(df);
		}

	}

	// Legami che fanno riferimenti al BID
	// 462/463/464
	if (DATABASE_ID == DATABASE_INDICE)
	{
//if (!strcmp(rootBid, "MUS0232990"))
//	return;

		if (bidHaPadre)
		{
			elabora46x(bid, bidHaPadre);
//			printf ("\nelabora46x");
		}
	}
	else
	{
		tbTitolo->loadRecord(bid); // 07/10/2011 Mantis 0004657

		elabora46x(bid, bidHaPadre);
	}
} // End Marc4cppLegami::elabora46y()



















/*

 */
void Marc4cppLegami::elabora44x(char *bid) {
	if (!IS_TAG_TO_GENERATE(440) && !IS_TAG_TO_GENERATE(441))
		return;
//	int ret = *(tagsToGenerateBufferPtr + 440);

	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit

	CString legame;
	char *entryPtr;
	char naturaBase;
	OrsChar *Token;

	OrsChar *tokenLegamePtr;
	long tokenLegameLength;
	DataField *df;

	char natura = *(tbTitolo->getField(tbTitolo->cd_natura));
	if (natura != 'M' && natura != 'C' && natura != 'S') {
		return;
	}

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
/*
// 12/10/2010 CFI0214931
	if (offsetBufferTrTitTitRelPtr)
		//retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtr);
		retb = BinarySearch::search(offsetBufferTrTitTitRelPtr, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitRelOffsetIn, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}
*/

	if (offsetBufferTrTitTitInvRelPtr)
		//retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtr);
		retb = BinarySearch::search(offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitInvRelOffsetIn, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}

	if (!retb) {
		return; // NON E' ARRIVO DI LEGAMI
	}

	// TITOLO ARRIVO DI LEGAMI.
	// Gestiamo solo quelli di tipo 02

	CString *sPtr = new CString(4096);

	// Dalla posizione prendiamo l'offset
//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII



	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitInvRelIn->SeekTo(offset);
	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitInvRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");

	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	Tokenizer.GetToken(); // Skip bid riferito da

	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase,			TokenCdNaturaColl, TokenSequenza;
	const char *commaSep = ",";
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);

		//TokenBid = tokenizerLegame.GetToken();
		//TokenTpLegame = tokenizerLegame.GetToken();


		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(); //TokenTpLegameMusica

		//TokenCdNaturaBase = tokenizerLegame.GetToken();
		//TokenCdNaturaColl = tokenizerLegame.GetToken();

		naturaBase = *tokenizerLegame.GetToken();
		if (naturaBase != 'M' && naturaBase != 'C' && naturaBase != 'S')
			continue; // Non e' una natura valida

		tokenizerLegame.GetToken(); // naturaColl = *

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenSequenza.assign( tokenLegameLength, tokenLegamePtr);

		TokenSequenza.Strip(TokenSequenza.trailing, '\n');




		if (!TokenTpLegame.isEqual("04") && !TokenTpLegame.isEqual("43")) // 27/10/2017
			continue; // Non e' un legame di tipo valido

		// Abbiamo trovato un riferimento di partenza

		// Carichiamo il titolo che fa riferimento a
		df = 0;

		// Troviamo la relazione inversa tit_tit
		tbTitolo->loadRecord(TokenBid.data());

		if (TokenTpLegame.isEqual("04"))
		{
			if (!IS_TAG_TO_GENERATE(440))
				continue;
			df = creaTag440_ContinuatoCon();
		}
		else
		{
			if (!IS_TAG_TO_GENERATE(441))
				continue;
			df = creaTag441_HaPerContinuazioneParziale(); // 27/10/2017 legame 43
		}
		// Aggiungiamo la $v
		if (df && TokenSequenza.Length()) { // Mantis 3550
			Subfield *sf = new Subfield('v', &TokenSequenza);
			//sf->setData();
			df->addSubfield(sf);
		}
		if (df)
			marcRecord->addDataField(df);
	} // End while
	delete sPtr;
} // end elabora44x


bool Marc4cppLegami::getBidColl(char *bid, CString *bidColl)
{
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit

	CString legame;
	char *entryPtr;
	char naturaBase, naturaColl;
	OrsChar *Token;

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
	if (offsetBufferTrTitTitRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitRelPtr, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitRelOffsetIn, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}

	if (!retb)
		return false; // non e' partenza di legami

	// Controlliamo che sia di tipo 01
	// Dalla posizione prendiamo l'offset
	CString * sPtr = new CString(4096);

	//offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitRelIn->SeekTo(offset);

	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");


	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");



	CString TokenBid, TokenTpLegame; // , TokenTpLegameMusica, TokenCdNaturaBase, TokenCdNaturaColl
	Tokenizer.GetToken(); // Skip bid riferito da

	//char CdNaturaBase, CdNaturaColl;
	const char *commaSep = ",";
	OrsChar *tokenLegamePtr;
	long tokenLegameLength;
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual(CD_LEGAME_01_FA_PARTE_DI)) // "01"
			continue; // Non e' un legame di tipo valido
		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken();

		if (
			((naturaBase == 'C' && naturaColl == 'C') || (naturaBase != 'C' && naturaColl != 'C'))
			&&
			((naturaBase == 'S' && naturaColl == 'S') || (naturaBase != 'S'))
			)
		{
			bidColl->AppendString(&TokenBid);
		delete sPtr;
			return true;
		}
	} // End while

	delete sPtr;
	return false;
} // End getBidColl



/*
 * Trova se Partenza di legame 01 con natura M o W
 */
bool Marc4cppLegami::isPartenzaLegame01MW(char *bid) {
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit

	//CString legame;
	char *entryPtr;
	char naturaBase, naturaColl;
	OrsChar *Token;


	// Dal BID cerchiamo l'offset del file di relazione ai titoli

	CString entryFile;
	if (offsetBufferTrTitTitRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitRelPtr, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitRelOffsetIn, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}

	if (!retb)
		return false; // non e' partenza di legami

	// Controlliamo che sia di tipo 01 a M o W
	// Dalla posizione prendiamo l'offset
	CString * sPtr = new CString(4096);
	//offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
		//memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atol (entryPtr+key_length); // OFFSET in ASCII

	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitRelIn->SeekTo(offset);

	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");


	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase, TokenCdNaturaColl;
	Tokenizer.GetToken(); // Skip bid riferito da
	const char * commaSep = ",";
	long tokenLegameLength;
	OrsChar *tokenLegamePtr;
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual(CD_LEGAME_01_FA_PARTE_DI))
			continue; // Non e' un legame di tipo valido

		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken();

		if (naturaBase == 'M' && naturaColl == 'C')
			continue;

		delete sPtr;
		return true; // 07/09/2010 Faccio parte di ...

	} // End while

	// Nessuna partenza di tipo 01 M o W dal bid in questione
	delete sPtr;
	return false;

} // End isPartenzaLegame01MW



int Marc4cppLegami::contaBidColl(char *bid)
{
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit
//	CString legame;
	//	char buf[80];
	char *entryPtr;
	char naturaBase, naturaColl;
	OrsChar *Token;
	int count = 0;

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
	if (offsetBufferTrTitTitInvRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitInvRelOffsetIn, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb) {
		return count;
	}

	// Dalla posizione prendiamo l'offset
//	CString * sPtr = new CString(4096); //
	CString * sPtr = new CString(8192); // 30/07/2012
	sPtr->SetResizeValueBy(16384); // 8192

//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitInvRelIn->SeekTo(offset);
	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitInvRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");


	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase, TokenCdNaturaColl;

	Tokenizer.GetToken(); // Skip bid riferito da
	const char *commaSep = ",";
	OrsChar *tokenLegamePtr;
	long tokenLegameLength;
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);


		tokenizerLegame.GetToken(); //TokenBid.assign(tokenLength, tokenizerLegame.GetToken(IS_NOT_QUOTED_TOKEN, commaSep, &tokenLength));

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual("01"))
			continue; // Non e' un legame di tipo valido
		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken();

		if (
			((naturaBase == 'C' && naturaColl == 'C') || (naturaBase != 'C' && naturaColl != 'C'))
			&&
			((naturaBase == 'S' && naturaColl == 'S') || (naturaBase != 'S'))
			)
		{
			count++;
		}

	} // End while

	// Nessun riferimento di tipo 01 M o W al bid in questione
	delete sPtr;
	return count;
} // End contaBidColl


int Marc4cppLegami::contaBidBase(char *bid)
{
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit
	CString legame;
	//	char buf[80];
	char *entryPtr;
	char naturaBase, naturaColl;
	OrsChar *Token;
//	long tokenLegameLength;
	int count = 0;

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
	if (offsetBufferTrTitTitRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitRelPtr, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitRelOffsetIn, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb) {
		return count;
	}

	// Dalla posizione prendiamo l'offset
	CString * sPtr = new CString(4096);
//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
		//memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII


	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitRelIn->SeekTo(offset);
	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitRelIn))
	{
        SignalAnError(__FILE__, __LINE__, "read failed");
	}


	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase, TokenCdNaturaColl;
	const char *commaSep = ",";
	long tokenLegameLength;
	OrsChar *tokenLegamePtr;

	Tokenizer.GetToken(); // Skip bid riferito da
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);


		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual(CD_LEGAME_01_FA_PARTE_DI))
			continue; // Non e' un legame di tipo valido
		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken();

		if (
			((naturaBase == 'C' && naturaColl == 'C') || (naturaBase != 'C' && naturaColl != 'C'))
			&&
			((naturaBase == 'S' && naturaColl == 'S') || (naturaBase != 'S'))
			)
		{
			count++;
		}

	} // End while

	// Nessun riferimento di tipo 01 M o W al bid in questione
	delete sPtr;
	return count;
} // End contaBidBase







/*
 Caso di notizia documento (M, S) SOLO ARRIVO di legami 01.

 Controllo se qualcuno mi fa riferimento con legame 02 e natura base M e S
 Se SI per ogni riferimento con una 421

 */
void Marc4cppLegami::elabora42x(char *bid) {
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit
	CString legame;

	//	char buf[80];
	char *entryPtr;
	char naturaBase;
	OrsChar *Token;
	DataField *df;

	char natura = *(tbTitolo->getField(tbTitolo->cd_natura));

//tbTitolo->dumpRecord();

// argentino 22/03/13
//	if (natura != 'M' && natura != 'S') {
//		return;
//	}

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
	if (offsetBufferTrTitTitInvRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitInvRelOffsetIn, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}

	if (!retb) {
		return; // NON E' ARRIVO DI LEGAMI
	}
	// TITOLO ARRIVO DI LEGAMI.
	// Gestiamo solo quelli di tipo 02
//	CString *sPtr = new CString(4096); //
	CString *sPtr = new CString(8192); // 30/10/2012
	sPtr->SetResizeValueBy(16384); // incrementiamo di Nk alla volta

//	CString *sPtr = &FixedLengthLine;

	// Dalla posizione prendiamo l'offset
//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitInvRelIn->SeekTo(offset);
	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitInvRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");

	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	Tokenizer.GetToken(); // Skip bid riferito da

	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase,		TokenCdNaturaColl, TokenSequenza;
	const char *commaSep = ",";
	OrsChar *tokenLegamePtr;
	long tokenLegameLength;
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual("02"))
			continue; // Non e' un legame di tipo valido

		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		tokenizerLegame.GetToken(); // naturaColl = *

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenSequenza.assign( tokenLegameLength, tokenLegamePtr);
		TokenSequenza.Strip(TokenSequenza.trailing, '\n');


//		naturaBase = TokenCdNaturaBase.GetFirstChar();
		if (naturaBase != 'M' && naturaBase != 'S')
			continue; // Non e' una natura valida

		if (DATABASE_ID == DATABASE_INDICE) // 29/09/2010
			continue;


		// Abbiamo trovato un riferimento di partenza
		// Carichiamo il titolo che fa riferimento a
		if (IS_TAG_TO_GENERATE(421))
		{
			tbTitolo->loadRecord(TokenBid.data());
			df = creaTag421_SupplementoDi();
			// Aggiungiamo la $v
			if (TokenSequenza.Length()) { //  && !TokenSequenza.isEqual("null")
				Subfield *sf = new Subfield('v', &TokenSequenza);
				//sf->setData();
				df->addSubfield(sf);
			}
			marcRecord->addDataField(df);
		}
	} // End while
	delete sPtr;
} // end elabora42x


/*!
\brief <b>Tag 421 - Legame titolo supplemento di</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>421</td></tr>
<tr><td valign=top>Descrizione</td><td>Legame titolo supplemento di</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/



DataField * Marc4cppLegami::creaTag421_SupplementoDi() {
	DataField *df, *dfSubtag;
	df = new DataField();
	df->setIndicator2('1');
	df->setTag((char *)"421");
	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag421_SupplementoDi


/*!
\brief <b>Tag 440 - Continuato con</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>440</td></tr>
<tr><td valign=top>Descrizione</td><td>Continuato con</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag440_ContinuatoCon() {
	DataField *df, *dfSubtag;
	df = new DataField();
	df->setIndicator1('1');
	df->setTag((char *)"440");

	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag440_ContinuatoDa

DataField * Marc4cppLegami::creaTag441_HaPerContinuazioneParziale() {
	DataField *df, *dfSubtag;
	df = new DataField();
	df->setIndicator1('1');
	df->setTag((char *)"441");

	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag440_ContinuatoDa



/*!
\brief <b>Tag 461 - Fa parte di livello piu' elevato (SET)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>461</td></tr>
<tr><td valign=top>Descrizione</td><td>Fa parte di livello piu' elevato (SET)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
        <li>1 - Tag 7xx embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag461_FaParteDi_NotiziaSuperiore(CString *sequenza) { //
	DataField *df, *df7xx; // , *df200; //, *dfSubtag
	Subfield *sf;
	string str;
	ATTValVector<Subfield*> *subfieldsVector;
	CString s;

//printf ("\ncreaTag461_FaParteDi_NotiziaSuperiore");


	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('0');
	else
		df->setIndicator2('1');
	df->setTag((char *)"461");

	creaSubtag200(df);

	if (!(sf = df->getSubfield('v')))
	{
		if (sequenza &&!sequenza->IsEmpty())
		{
			Subfield *sf = new Subfield('v', sequenza);
			//sf->setData();
			df->addSubfield(sf);
		}
	}

	if (siArg != 0 && TIPO_SCARICO == TIPO_SCARICO_OPAC) // 29/04
	{
		unsigned int pos;
//str = *siArg;
//printf ("\nsiArg = %s", str.data());

		while (siArg != siArg.end()) { // h1
			str = *siArg;

//printf ("\nsiArg = %s", str.data());

			// Troviamo il separatore della gerarchia

			if ((pos = str.find(':')) != string::npos) {
				if (str.find("AUT:", pos + 1) != string::npos) {
					//					std::cout << std::endl << str << std::endl;
					df7xx = creaLegameTitoloAutore((char *) str.data(), pos);
					if (df7xx) {
						subfieldsVector = df7xx->getSubfields();
						Subfield* sf7xx;
						// s = " ";
						s.Clear(); // 15/01/2010 13.50
						s.AppendString(df7xx->getTagString());
						s.AppendChar(df7xx->getIndicator1());
						s.AppendChar(df7xx->getIndicator2());

						sf = new Subfield('1', &s);
						//sf->setData();
						df->addSubfield(sf);

						for (int i = 0; i < subfieldsVector->length(); i++) {
							sf7xx = subfieldsVector->Entry(i);

							sf = new Subfield(sf7xx->getCode(), sf7xx->getDataString());
							//sf->setData();
							df->addSubfield(sf);
						}
						delete df7xx;
					}
				}
			}
			++siArg;
		}
		siArg = 0; // CLEAR just in case we forget reassignement
	}


	//	marcRecord->addDataField(df);
	return df;
} // End creaTag461_FaParteDi_NotiziaSuperiore


/*!
\brief <b>Tag 462 - Legame a livello intermedio</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>462</td></tr>
<tr><td valign=top>Descrizione</td><td>Legame a livello intermedio ("Comprende" se parte da notizia padre e "Fa parte di" se parte da notizia figlia)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
        <li>1 - Tag 7xx embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag462_LegameALivelloIntermedio(CString *bid, CString *sequenza)
{
	DataField *df=0; // , *dfSubtag
	//	Subfield *sf;

//	printf ("\ncreaTag462_LegameALivelloIntermedio, bid=%s", bid->data());
//	if (!strcmp(rootBid, "MUS0232990"))
//		return df;


	df = new DataField((char *)"462", 3);
	if (notaAlLegame_311)
		df->setIndicator2('0');
	else
		df->setIndicator2('1');



	creaSubtag200(df);

	if (!sequenza->IsEmpty())
	{
		Subfield *sf = new Subfield('v', sequenza);
		//sf->setData();
		df->addSubfield(sf);
	}
	//		return df;



	creatTag7xxNidificato(df, bid);

	//	marcRecord->addDataField(df);
	return df;
} // end creaTag462_LegameALivelloIntermedio


/*
  Sottocampi:
 1 - Dati di legame (Ripetibile)
 OPPURE
 a - Autore
 c - Luogo di pubblicazione
 d - Data di publicazione
 e - Edizione
 h - Numero della sezione o parte
 i - Nome della sezione o parte
 p - Descrizione fisica
 t - Titolo
 u - Uniform Resource Locator
 v - Numero di volume
 x - International Standard Serial Number (ISSN)
 y - International Standard Book Number/International Standard Music Number (ISBN/ISMN)
 z - CODEN
 0 - Bibliographic Record Identifier
 3 - Authority Record Number
 5 - Institute to Which Field Applies
 */

/*!
\brief <b>Tag 463 - Legame all'unita' singola (da spoglio a pezzo fisico)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>463</td></tr>
<tr><td valign=top>Descrizione</td><td>Legame all'unita' singola (Pezzo fisico)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
        <li>v - Sequenza.
        <li>[1 - Tag 7xx embedded. PER SCARICO OPAC INDICE]
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/



DataField * Marc4cppLegami::creaTag463_PezzoFisico(CString *bid, CString *sequenza)
{
	DataField *df, *df7xx;
	Subfield *sf;
	ATTValVector<Subfield*> *subfieldsVector;
	CString s;
	string str;

	unsigned int pos;
	char *entryReticoloPtr;

	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('0');
	else
		df->setIndicator2('1');
	df->setTag((char *)"463");

	creaSubtag200(df);


	if (!sequenza->IsEmpty())
	{
		Subfield *sf = new Subfield('v', sequenza);
		//sf->setData();
		df->addSubfield(sf);
	}

if (TIPO_SCARICO == TIPO_SCARICO_OPAC) // 20/09/2010
{

	// 20/09/2010 Gestione delle 7xx nidificate
	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); // Primo figlio di root

	TbReticoloTit *tbReticoloTit = 0;

	while (ch != reticolo.end().end()) { // h1
		// Cerchiamo il titolo linkato
		str = *ch;

//printf ("\nElemento del reticolo: %s", str.data());

		if ((pos = str.find(':')) != string::npos) {
			if (str.find("TIT:", pos + 1) != string::npos) {

				entryReticoloPtr = (char *) str.data();

				if (!tbReticoloTit)
					tbReticoloTit = new TbReticoloTit(entryReticoloPtr + pos + 5);
				else
					tbReticoloTit->assign(entryReticoloPtr + pos + 5);

				// E' il titolo che cerchiamo? Dobbiamo lavorare sullo stesso bid che ci e' stato passato
				if (tbReticoloTit->getFieldString(tbReticoloTit->bid)->isEqual(bid->data()))
				{

//printf ("\nTrovato titolo, bid reticolo: %s, bid titolo=%s", tbReticoloTit->getFieldString(tbReticoloTit->bid)->Data(), bid->data());

					tree<std::string>::sibling_iterator node = ch.begin();
					while (node != node.end()) { // h1
						str = *node;
//printf ("\nNodo del reticolo: %s", str.data());

						// Troviamo il separatore della gerarchia

						if ((pos = str.find(':')) != string::npos) {
							if (str.find("AUT:", pos + 1) != string::npos) {
								//					std::cout << std::endl << str << std::endl;
								df7xx = creaLegameTitoloAutore((char *) str.data(), pos);
								if (df7xx) {
									subfieldsVector = df7xx->getSubfields();
									Subfield* sf7xx;
									// s = " ";
									s.Clear(); // 15/01/2010 13.50
									s.AppendString(df7xx->getTagString());
									s.AppendChar(df7xx->getIndicator1());
									s.AppendChar(df7xx->getIndicator2());

									sf = new Subfield('1', &s);
									//sf->setData();
									df->addSubfield(sf);

									for (int i = 0; i < subfieldsVector->length(); i++) {
										sf7xx = subfieldsVector->Entry(i);

										sf = new Subfield(sf7xx->getCode(), sf7xx->getDataString());
										//sf->setData();
										df->addSubfield(sf);
									}
									delete df7xx;
								}
							}
						}
						++node;
					}
					break; // titolo fatto. possiamo uscire 17/11/14 (mail roveri 14/11/14)
				}
//				break;  17/11/14 (mail roveri 14/11/14)
			}
		}
		++ch;
	} // End while

	if (tbReticoloTit)
		delete tbReticoloTit;

} // end tipo scarico opac



	//	marcRecord->addDataField(df);
	return df;
} // end creaTag463UnitaSingola


/*!
\brief <b>Tag 464 - Legame all'unita' singola analitica (spoglio)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>464</td></tr>
<tr><td valign=top>Descrizione</td><td>Legame all'unita' singola analitica (spoglio)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
        <li>1 - Tag 7xx embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag464_AnaliticaSpoglio(CString *bid, CString *sequenza)
{
	DataField *df;
	//	Subfield *sf;

	df = new DataField();
	df->setTag((char *)"464");
	if (notaAlLegame_311)
		df->setIndicator2('0');
	else
		df->setIndicator2('1');


	creaSubtag200(df);
	if (!sequenza->IsEmpty())	// 25/02/2016
	{
		Subfield *sf = new Subfield('v', sequenza);
		df->addSubfield(sf);
	}

	creatTag7xxNidificato(df, bid);

	return df;
} // end creaTag464_UnitaSingolaAnalitica


/*!
\brief <b>Tag 620 - Luogo di pubblicazione normalizzato (Libro Antico)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>620</td></tr>
<tr><td valign=top>Descrizione</td><td>Luogo di pubblicazione normalizzato (Libro Antico)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Codice paese
        <li>d - Descrizione luogo
        <li>3 - Identificativo luogo
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag620_LuogoDiPubblicazione() {
	DataField *df = 0;
	Subfield *sf;

//tbLuogo->dumpRecord();

	df = new DataField();
	df->setTag((char *)"620");
	if (!tbLuogo->getFieldString(tbLuogo->cd_paese)->IsEmpty()) // 03/08/2010
	{
		const char *cdPaese = tbLuogo->getField(tbLuogo->cd_paese);
	//	if (strcmp(cdPaese, "null"))
		if (*cdPaese)
			sf = new Subfield('a', tbLuogo->getFieldString(tbLuogo->cd_paese));
			//sf->setData();
		else
			sf = new Subfield('a',(char *)"UN", 2);
			//sf->setData();
		df->addSubfield(sf);
	}


	sf = new Subfield('d', tbLuogo->getFieldString(tbLuogo->ds_luogo));
	//sf->setData();
	df->addSubfield(sf);

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)// 28/04/10
	{
		sf = new Subfield('3', tbLuogo->getFieldString(tbLuogo->lid));
		//sf->setData();
		df->addSubfield(sf);
	}
	return df;
} // end creaTag620_LuogoDiPubblicazione


/*!
\brief <b>Tag 676 - Classificazione Decimale Dewey</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>676</td></tr>
<tr><td valign=top>Descrizione</td><td>Classificazione Decimale Dewey</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Codice classificazione
        <li>c - Descrizione classificazione (per INDICE)
        <li>9 - Descrizione classificazione (per POLI)
        <li>v - Codice edizione
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag676_ClassificazioneDecimaleDewey() {
	DataField *df;
	Subfield *sf;
//	CString s;
	CString *sPtr;

	df = new DataField((char *)"676", ' ', ' ');

	sPtr =tbClasse->getFieldString(tbClasse->classe);
	sPtr->Strip(sPtr->trailing, ' ');
	sf = new Subfield('a', sPtr);
	df->addSubfield(sf);


	sPtr = tbClasse->getFieldString(tbClasse->ds_classe);
	sPtr->Strip(sPtr->trailing, ' ');

	if (DATABASE_ID == DATABASE_INDICE)
		sf = new Subfield('c', sPtr); // 22/09/2010
	else
		sf = new Subfield('9', sPtr); // CFI 19/10/2009 12.25

	df->addSubfield(sf);

	sPtr = tbClasse->getFieldString(tbClasse->cd_edizione);
	sPtr->Strip(sPtr->trailing, ' ');

//	if (DATABASE_ID != DATABASE_INDICE)
	if (DATABASE_ID == DATABASE_SBNWEB)
	{ // POLO
//#ifdef EVOLUTIVA_POLO_2014
		// Remap codice eedizione in base alla tb_codici ECLA
		char *mappedCdEdizione = codiciEclaKV->GetValueFromKey(sPtr->data());
		if (!mappedCdEdizione)
			sf = new Subfield('v', sPtr); // Qui non dovrebbe mai arrivare
		else
			sf = new Subfield('v', mappedCdEdizione, strlen (mappedCdEdizione));

//#else
//		sf = new Subfield('v', sPtr);
//#endif
		}
	else
	{ // INDICE

//#ifdef EVOLUTIVA_2014
		sf = new Subfield('v', sPtr);
//#else
//		CString s;
//		char chr = sPtr->GetFirstChar();
//		if (chr >= '1' && chr <= '9')
//		{
//			s = '1';
//			s.AppendChar(chr);
//		}
//		else
//		{
//		switch (chr){
//			case 'X':
//				s.assign("10", 2); break;
//			case 'A':
//				s.assign("20", 2); break;
//			case 'B':
//				s.assign("21", 2); break;
//			case 'C':
//				s.assign("22", 2); break;
//			case 'D': // in previsione di ...
//				s.assign("23", 2); break;
//			case 'E':
//				s.assign("24", 2); break;
//			case 'F':
//				s.assign("25", 2); break;
//			case 'G':
//				s.assign("26", 2); break;
//			}
//		}
//		sf = new Subfield('v', &s);
//#endif
	} // end indice

	df->addSubfield(sf);

	return df;
} // End creaTag676ClassificazioneDecimaleDewey

/*!
\brief <b>Tag 686 - Altra classificazione</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>686</td></tr>
<tr><td valign=top>Descrizione</td><td>Altra classificazione</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Codice classificazione
        <li>c - Descrizione classificazione
        <li>v - Codice edizione
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag686_AltraClassificazione() {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setTag((char *)"686");

	CString s;
	s = tbClasse->getField(tbClasse->classe);
	s.Strip(s.trailing, ' ');
	sf = new Subfield('a', &s);
	//sf->setData();
	df->addSubfield(sf);

	s = tbClasse->getField(tbClasse->ds_classe);
	s.Strip(s.trailing, ' ');
	sf = new Subfield('c', &s);
	df->addSubfield(sf);

	s = tbClasse->getField(tbClasse->cd_sistema);
	s.Strip(s.trailing, ' ');
	if (!s.IsEmpty())
	{
		sf = new Subfield('2', &s);
		df->addSubfield(sf);
	}


	s = tbClasse->getField(tbClasse->cd_edizione);
	s.Strip(s.trailing, ' ');
	if (!s.IsEmpty())
	{
		sf = new Subfield('v', &s);
		df->addSubfield(sf);
	}

	return df;
} // End creaTag686_AltraClassificazione



/*!
\brief <b>Tag 856 - Localizzazione accesso risorsa elettronica</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>856</td></tr>
<tr><td valign=top>Descrizione</td><td>Localizzazione accesso risorsa elettronica</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>u - Unique Resource Identifier (URI)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppLegami::creaTag856_LinkMultimediale(const char *bid) {
	DataField *df = 0;
	Subfield *sf;

	while (tsLinkMultim->loadNextRecord(bid)) {
		if (tsLinkMultim->getFieldString(tsLinkMultim->uri_multim)->IsEmpty())
			continue;

		df = new DataField();
		df->setTag((char *)"856");
		sf = new Subfield('u', tsLinkMultim->getFieldString(tsLinkMultim->uri_multim)); // Uniform Resource Identifier
		//sf->setData();
		df->addSubfield(sf);
		marcRecord->addDataField(df);
		} // end while

	} // End creaTag856_LinkMultimediale


/*!
\brief <b>Tag 899 - Localizzazione</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>899</td></tr>
<tr><td valign=top>Descrizione</td><td>Localizzazione</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Livello gerarchico della notizia
    <UL>
        <li>0 - digitalizzazione parziale
        <li>1 - digitalizzazione totale
        <li>2 - copia di born digital
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
PER INDICE
    <UL>
        <li> a - Codice anagrafico della biblioteca se scarico OPAC oppure Descrizione biblioteca se scarico UNIMARC
        <li> 1 - Codice anagrafe biblioteche
        <li> 2 - Codice della biblioteca nel DB gestionale
        <li> 3 - Fondo
        <li> c - Segnatura (solo per scarico OPAC)
        <li> d - Citta'
        <li> e - Disponibilita' formato elettronico
        <li> f - Indicatore di tipo localizzazione
        <li> q - indicatore di esemplare mutilo
        <li> s - Segnatura antica (solo per scarico OPAC)
        <li> t - Tipo di digitalizzazione (solo per scarico OPAC)
    </UL>
PER POLO
    <UL>
        <li> a - Descrizione biblioteca
        <li> q - indicatore di esemplare mutilo (se valore diverso da 'N')
    </UL>
PER INDICE E POLO
    <UL>
        <li> 4 - Consistenza e stato di conservazione
        <li> b - Codice anagrafico della biblioteca (non per scarico OPAC)
        <li> n - Note
        <li> u - URI
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

//DataField * Marc4cppLegami::creaTag899_Localizzazione() {
DataField * Marc4cppLegami::creaTag899_Localizzazione(bool has430, bool has440) { // 16/03/2017 (Aste/Mataloni)


	DataField *df = 0;
	Subfield *sf;
	CString *sPtr, *cdAnaBib;

//trTitBib->dumpRecord();

	CString *cdPolo = trTitBib->getFieldString(trTitBib->cd_polo);
	CString kPoloBiblioteca(cdPolo->data(), cdPolo->Length()); // = (char *)trTitBib->getField(trTitBib->cd_polo); // 24/02/2010
    kPoloBiblioteca.AppendString(trTitBib->getFieldString(trTitBib->cd_biblioteca)); // prendi la biblioteca

    // 30/01/2018
	if (bibliotecheDaMostrareIn899KV)
		{ // Esporta solo le biblioteche richieste
			if (bibliotecheDaMostrareIn899KV->GetFirstIndexFromSortedKey(kPoloBiblioteca.data()) == -1)
					return df; // not found
		}


//printf ("\ncreaTag899_Localizzazione");
	char natura = *(tbTitolo->getField(tbTitolo->cd_natura));

//tbTitolo->dumpRecord();

//trTitBib->dumpRecord();

	if (natura != 'M' && natura != 'W' && natura != 'S' && natura != 'N') 	// Roveri mail 04/11/2010 13.01
		return 0;															// Non scaricare le localizzazioni per le collane
	sPtr = trTitBib->getFieldString(trTitBib->uri_copia);

//printf ("\nnatura ok");

	if (marc4cppDocumento->getPoloId() != POLO_RML // Fix per RML 28/01/2010 15.38
		&& marc4cppDocumento->getPoloId() != POLO_INDICE	) // Fix x Indice 26/04/10
	{
	if ( sPtr->IsEmpty()) // Obsolete Genera 856 solo se URI presente 11/12/2009 10.57 CFI
						  // Genera 899 solo se URI presente 18/10/2010 Contardi/Giangregorio
		return 0;
	}

	// Genera 856 per indice se presente la uri
	// Localizzazione accesso risorsa elettronica
//	if (sPtr->Length() && marc4cppDocumento->getPoloId() == POLO_INDICE)

//	if (sPtr->Length()) // 06/08/2010 10.42 richiesta CFI
//	{
//		//		printf ("Uri copia = '%s'", );
//		df = new DataField();
//		df->setTag("856");
//		sf = new Subfield('u'); // Uniform Resource Identifier
//		sf->setData(sPtr->data());
//		df->addSubfield(sf);
//		marcRecord->addDataField(df);
//
//	}

//	const char *bid = tbTitolo->getField(tbTitolo->bid);
//	if (tsLinkMultim->existsRecord(bid)) // 17/09/2010 Roveri
//		creaTag856_LinkMultimediale(bid);



	df = new DataField((char *)"899", 3);
	//df->setTag();
	char tipoDigitalizzazione = *(trTitBib->getField(trTitBib->tp_digitalizz));
	if (tipoDigitalizzazione) // Se non valorizzato .. in attesa di info
		df->setIndicator2(tipoDigitalizzazione);

    if (!tbfBiblioteca->loadRecord(kPoloBiblioteca.data()))
    {
//tbfBiblioteca->dumpRecord();
    	SignalAWarning(__FILE__, __LINE__, "Biblioteca non trovata per '%s'",	kPoloBiblioteca.data());
		return df;
    }
	cdAnaBib = tbfBiblioteca->getFieldString(tbfBiblioteca->cd_ana_biblioteca);

//printf ("\ncdAnaBib=%s", cdAnaBib->data());
	if (DATABASE_ID == DATABASE_INDICE)
	{

		sf = new Subfield('1', cdAnaBib);	// Anagrafica biblioteca
		df->addSubfield(sf);

//if (!strcmp(trTitBib->getField(trTitBib->bid), "MUS0232990"))
//	return df;

		sf = new Subfield('2', &kPoloBiblioteca);				// Codice polo + codice bib
		df->addSubfield(sf);

		if (!trTitBib->getFieldString(trTitBib->ds_fondo)->IsEmpty()) { // 22/09/2010
			sf = new Subfield('3', trTitBib->getFieldString(trTitBib->ds_fondo)); // Fondo
			df->addSubfield(sf);
		}

// Evolutiva 01/10/2013 - Info gia' presente in $1
//		if (TIPO_SCARICO == TIPO_SCARICO_OPAC)
//		{
//			sf = new Subfield('a', cdAnaBib);	// Anagrafica biblioteca
//			df->addSubfield(sf);
//		}

// Evolutiva 01/10/2013 - $d Non utilizzata per opac
	if (TIPO_SCARICO != TIPO_SCARICO_OPAC)
	{
		sPtr = tbfBiblioteca->getFieldString(tbfBiblioteca->localita);
		if (sPtr->Length())
		{
			sf = new Subfield('d', sPtr);
			df->addSubfield(sf);
		}
	}

		char flPossesso = *trTitBib->getField(trTitBib->fl_possesso);
		char flGestione = *trTitBib->getField(trTitBib->fl_gestione);
		if (flPossesso || flGestione)
		{

//			if (flPossesso != 'N' && flGestione != 'N')
//				sf = new Subfield('f', "P/G", 3);
//			else if (flPossesso != 'N' && flGestione == 'N')
//				sf = new Subfield('f', "P", 1);
//			else
//				sf = new Subfield('f', "G", 1);

// 27/01/14 consistenze eliminate e titoli cancellati ma localizzazioni ancora visibili in opac sbn MAIL FRANCO 22/01/2014
//			sf=0;
//			if (flPossesso != 'N' && flGestione != 'N')
//				sf = new Subfield('f', "P/G", 3);		// POSSESSO e GESTIONE
//
//			if (flPossesso != 'N' && flGestione == 'N')
//				sf = new Subfield('f', "P", 1);			// SOLO POSSESSO
//
//			if (DATABASE_ID != DATABASE_INDICE)
//				{ // Non tocchiamo lo scarico di polo
//				if (flPossesso == 'N' && flGestione != 'N')
//					sf = new Subfield('f', "G", 1);		// SOLO GESTIONE (solo per polo)
//				}

// Ripristinato il 03/02/2014 su richiesta di Paolucci/Simonelli
			if (flPossesso != 'N' && flGestione != 'N')
				sf = new Subfield('f', (char *)"P/G", 3);
			else if (flPossesso != 'N' && flGestione == 'N')
				sf = new Subfield('f', (char *)"P", 1);

//			else if (has430 || !has440) // 16/03/2017
//				sf = new Subfield('f', "G", 1); // se ha 430 "continuazione di" oppure manca la 440 "continua con" esportiamo localizzazione per sola gestione
//			else
//			{ // in presenza di una 440 "continua con" di notizia non posseduta non emettere localizzazione
//				delete df;
//				return 0;
//			}
			else // 22/05/2017 Barbieri/Mataloni/Roveri (riunione da Mataloni/Roveri)
				sf = new Subfield('f', (char *)"G", 1); // se ha 430 "continuazione di" oppure manca la 440 "continua con" esportiamo localizzazione per sola gestione


			if (sf)
				df->addSubfield(sf);
		}
	}




	if (TIPO_SCARICO != TIPO_SCARICO_OPAC)
	{

		sPtr = tbfBiblioteca->getFieldString(tbfBiblioteca->nom_biblioteca);
		sPtr->Strip(CString::trailing, ' ');
		sf = new Subfield('a', sPtr); // Descrizione della biblioteca come presente su sistema gestionale
		df->addSubfield(sf);
		if (!cdAnaBib->IsEmpty())
		{
			sf = new Subfield('b', cdAnaBib); // Codice anagrafe biblioteche
			df->addSubfield(sf);
		}
	}

	if (!trTitBib->getFieldString(trTitBib->ds_consistenza)->IsEmpty()) {
		sf = new Subfield('4', trTitBib->getFieldString(trTitBib->ds_consistenza)); // Consistenza e stato di conservazione
		df->addSubfield(sf);
	}

	if (TIPO_SCARICO == TIPO_SCARICO_OPAC ||
			DATABASE_ID == DATABASE_INDICE) // Mantis 5241 29/01/2013  Contardi
	{
	if (!trTitBib->getFieldString(trTitBib->ds_segn)->IsEmpty()) {
		sf = new Subfield('c', trTitBib->getFieldString(trTitBib->ds_segn)); // Segnatura
		df->addSubfield(sf);
	}
	}
//	if (DATABASE_ID != DATABASE_INDICE)	05/07/2011 Mantis SBN2 4550
//	{
		if (!trTitBib->getFieldString(trTitBib->fl_disp_elettr)->IsEmpty()){
		sf = new Subfield('e', trTitBib->getFieldString(trTitBib->fl_disp_elettr)); // Disponibilita' formato elettronico
		df->addSubfield(sf);
		}
//	}

	if (!trTitBib->getFieldString(trTitBib->nota_tit_bib)->IsEmpty()) {
		sf = new Subfield('n', trTitBib->getFieldString(trTitBib->nota_tit_bib)); // note
		df->addSubfield(sf);
	}

	sPtr = trTitBib->getFieldString(trTitBib->fl_mutilo);
	if (!sPtr->IsEmpty() && (sPtr->GetFirstChar() != 'N' || DATABASE_ID == DATABASE_INDICE)) // 23/09/2010
		{
		if(*trTitBib->getField(trTitBib->fl_mutilo) == 'X') // 04/10/2010
			sf = new Subfield('q', (char *)"S", 1); // indicatore di esemplare mutilo
		else
			sf = new Subfield('q', trTitBib->getFieldString(trTitBib->fl_mutilo)); // indicatore di esemplare mutilo
		df->addSubfield(sf);
	}

// 15/03/2010 14.13 Nuovo documento Rossana
	if (TIPO_SCARICO == TIPO_SCARICO_OPAC ||
			DATABASE_ID == DATABASE_INDICE) // Mantis 5241 29/01/2013 Contardi
	{
		if (!trTitBib->getFieldString(trTitBib->ds_antica_segn)->IsEmpty()) {
			sf = new Subfield('s', trTitBib->getFieldString(trTitBib->ds_antica_segn)); // Segnatura antica
			df->addSubfield(sf);
		}
		if (!trTitBib->getFieldString(trTitBib->tp_digitalizz)->IsEmpty()) {
			sf = new Subfield('t', trTitBib->getFieldString(trTitBib->tp_digitalizz)); // Tipo di digitalizzazione
			df->addSubfield(sf);
		}
	}
	if (!trTitBib->getFieldString(trTitBib->uri_copia)->IsEmpty()) {
		sf = new Subfield('u', trTitBib->getFieldString(trTitBib->uri_copia)); // URI
		df->addSubfield(sf);
	}

//trTitBib->dumpRecord();

// 29/01/2018
	if (z899)
	{
		sf = new Subfield('z', trTitBib->getFieldString(trTitBib->ts_ins));
		df->addSubfield(sf);
	}

// Gestione incrementale SOLO!!!!
// 14/02/2018
//char *incrementale_dal = "20180129";
//	if (xy899)
	if (incrementale_dal)
	{
//CString * tmpPtr= trTitBib->getFieldString(trTitBib->ts_var_indice);
//CString * tmpPtr= trTitBib->getFieldString(trTitBib->ts_var);

		//sf = new Subfield('y', trTitBib->getFieldString(trTitBib->ts_var_indice)); // solo per pbe (da db di indice modificato)
		sf = new Subfield('y', trTitBib->getFieldString(trTitBib->ts_var)); // 02/05/2018
		df->addSubfield(sf);

//char *field = trTitBib->getField(trTitBib->ts_ins);
//field = trTitBib->getField(trTitBib->ts_var);
//printf ("\nts_ins %d:%s, ", trTitBib->ts_ins, trTitBib->getField(trTitBib->ts_ins));
//printf ("\nts_var %d:%s, ", trTitBib->ts_var, trTitBib->getField(trTitBib->ts_var));

//trTitBib->getFieldString(trTitBib->ts_var)->assign("20180216"); // 20120419

//printf ("\nts_ins %d:%s, ", trTitBib->ts_ins, trTitBib->getField(trTitBib->ts_ins));
//printf ("\nts_var %d:%s, ", trTitBib->ts_var, trTitBib->getField(trTitBib->ts_var));


		// Localizzazione cancellata?
		// Stessa data?
		if (trTitBib->getFieldString(trTitBib->fl_canc)->isEqual("S"))
		{
			sf = new Subfield('x', (char *)"c");
			df->addSubfield(sf);
		}

		else if (!strcmp (trTitBib->getField(trTitBib->ts_ins), trTitBib->getField(trTitBib->ts_var)))
		{ // localizzazione nuova

			if (strcmp (trTitBib->getField(trTitBib->ts_ins), incrementale_dal->Data()) > 0)
			{
					sf = new Subfield('x', (char *)"n");
					df->addSubfield(sf);
			}
			else
			{
				sf = new Subfield('x', (char *)"i");
				df->addSubfield(sf);
			}
		}
		else
		{
			if (strcmp (trTitBib->getField(trTitBib->ts_var), incrementale_dal->Data()) > 0)
				{
					sf = new Subfield('x', (char *)"m");
					df->addSubfield(sf);
				}
			else
			{
				sf = new Subfield('x', (char *)"i");
				df->addSubfield(sf);
			}
		}

	}



	return df;

} // creaTag899_Localizzazione



/*!
\brief <b>Tag 922 - Rappresentazione (mat. musicale)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>922</td></tr>
<tr><td valign=top>Descrizione</td><td>Rappresentazione (mat. musicale)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - genere della rappresentazione. Non Ripetibile.
        <li> p - anno di rappresentazione. Non Ripetibile.
        <li> q - periodo di rappresentazione. Non Ripetibile.
        <li> r - teatro di rappresentazione. Non Ripetibile.
        <li> s - luogo di rappresentazione. Non Ripetibile.
        <li> t - nota alla rappresentazione. Non Ripetibile.
        <li> u - occasione della rappresentazione. Non Ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppLegami::creaTag922_Rappresentazione() {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setTag((char *)"922");

	if (!tbRappresent->getFieldString(tbRappresent->tp_genere)->IsEmpty())
	{
		tbRappresent->getFieldString(tbRappresent->tp_genere)->Strip(CString::trailing, ' ');
		sf = new Subfield('a', tbRappresent->getFieldString(tbRappresent->tp_genere)); // genere della rappresentazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbRappresent->getFieldString(tbRappresent->aa_rapp)->IsEmpty())
	{
		tbRappresent->getFieldString(tbRappresent->aa_rapp)->Strip(CString::trailing);
		sf = new Subfield('p', tbRappresent->getFieldString(tbRappresent->aa_rapp)); // anno di rappresentazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbRappresent->getFieldString(tbRappresent->ds_periodo)->IsEmpty())
	{
		tbRappresent->getFieldString(tbRappresent->ds_periodo)->Strip(CString::trailing);
		sf = new Subfield('q', tbRappresent->getFieldString(tbRappresent->ds_periodo)); // periodo di rappresentazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbRappresent->getFieldString(tbRappresent->ds_teatro)->IsEmpty())
	{
		tbRappresent->getFieldString(tbRappresent->ds_teatro)->Strip(CString::trailing);
		sf = new Subfield('r', tbRappresent->getFieldString(tbRappresent->ds_teatro)); // teatro di rappresentazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbRappresent->getFieldString(tbRappresent->ds_luogo_rapp)->IsEmpty())
	{
		tbRappresent->getFieldString(tbRappresent->ds_luogo_rapp)->Strip(CString::trailing);
		sf = new Subfield('s', tbRappresent->getFieldString(tbRappresent->ds_luogo_rapp)); // luogo di rappresentazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbRappresent->getFieldString(tbRappresent->nota_rapp)->IsEmpty())
	{
		tbRappresent->getFieldString(tbRappresent->nota_rapp)->Strip(CString::trailing);
		sf = new Subfield('t', tbRappresent->getFieldString(tbRappresent->nota_rapp)); // nota alla rappresentazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbRappresent->getFieldString(tbRappresent->ds_occasione)->IsEmpty())
	{
		tbRappresent->getFieldString(tbRappresent->ds_occasione)->Strip(CString::trailing);
		sf = new Subfield('u', tbRappresent->getFieldString(tbRappresent->ds_occasione)); // occasione della rappresentazione
		//sf->setData();
		df->addSubfield(sf);
	}

	marcRecord->addDataField(df);

} // end creaTag923_Rappresentazione


/*!
\brief <b>Tag 923 - Notazione (mat. musicale)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>923</td></tr>
<tr><td valign=top>Descrizione</td><td>Notazione (mat. musicale)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> b - codice stesura. Non ripetibile.
        <li> c - indicatore di composito. Non ripetibile.
        <li> d - indicatore di palinsesto. Non ripetibile.
        <li> e - datazione. Non ripetibile.
        <li> g - codice materia. Non ripetibile.
        <li> h - illustrazioni. Non ripetibile.
        <li> i - notazione musicale. Non ripetibile.
        <li> l - legatura (per manoscritti). Non ripetibile.
        <li> m - conservazione (per manoscritti). Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

void Marc4cppLegami::creaTag923_Notazione() {
	DataField *df;
	Subfield *sf;

//	if (!tbMusica->loadRecord(tbTitolo->getField(tbTitolo->bid)))
//		return;

	df = new DataField();
	df->setTag((char *)"923");

	if (!tbMusica->getFieldString(tbMusica->cd_stesura)->IsEmpty())
	{
		sf = new Subfield('b', tbMusica->getFieldString(tbMusica->cd_stesura)); // codice stesura
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->fl_composito)->IsEmpty())
	{
	sf = new Subfield('c', tbMusica->getFieldString(tbMusica->fl_composito)); // indicatore di composito
	//sf->setData();
	df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->fl_palinsesto)->IsEmpty())
	{
		sf = new Subfield('d', tbMusica->getFieldString(tbMusica->fl_palinsesto)); // indicatore di palinsesto
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->datazione)->IsEmpty())
	{
		sf = new Subfield('e', tbMusica->getFieldString(tbMusica->datazione)); // datazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->cd_materia)->IsEmpty())
	{
		sf = new Subfield('g', tbMusica->getFieldString(tbMusica->cd_materia)); // codice materia
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->ds_illustrazioni)->IsEmpty())
	{
		sf = new Subfield('h', tbMusica->getFieldString(tbMusica->ds_illustrazioni)); // illustrazioni
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->notazione_musicale)->IsEmpty())
	{
		sf = new Subfield('i', tbMusica->getFieldString(tbMusica->notazione_musicale)); // notazione musicale
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->ds_legatura)->IsEmpty())
	{
		sf = new Subfield('l', tbMusica->getFieldString(tbMusica->ds_legatura)); // legatura (per manoscritti)
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->ds_conservazione)->IsEmpty())
	{
		sf = new Subfield('m', tbMusica->getFieldString(tbMusica->ds_conservazione)); // conservazione (per manoscritti)
		//sf->setData();
		df->addSubfield(sf);
	}

	if (df->getSubfields()->Length())
		marcRecord->addDataField(df);
	else
		delete df;
} // End creaTag923_Notazione

/*!
\brief <b>Tag 926 - Incipit</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>926</td></tr>
<tr><td valign=top>Descrizione</td><td>Incipit</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - indicatore: P=precedente S=successivo. Non ripetibile.
        <li> b - numero composizioni. Non ripetibile.
        <li> c - contesto. Non ripetibile.
        <li> f - numero movimento. Non ripetibile.
        <li> g - numero progressivo movimenti. Non ripetibile.
        <li> h - registro musicale. Non ripetibile.
        <li> i - codice forma musicale. Non ripetibile.
        <li> l - codice tonalita'. Non ripetibile.
        <li> m - chiave musicale. Non ripetibile.
        <li> n - alterazione. Non ripetibile.
        <li> o - misura. Non ripetibile.
        <li> p - tempo musicale. Non ripetibile.
        <li> q - nome personaggio. Non ripetibile.
        <li> r - Identificativo del titolo di incipit letterario. Non ripetibile.
        <li> s - ISBD del titolo di incipit letterario. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

void Marc4cppLegami::creaTag926_Incipit() {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setTag((char *)"926");

//tbIncipit->dumpRecord();

	if (!tbIncipit->getFieldString(tbIncipit->tp_indicatore)->IsEmpty())
	{
		sf = new Subfield('a', tbIncipit->getFieldString(tbIncipit->tp_indicatore)); // indicatore: P=precedente S=successivo
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->numero_comp)->IsEmpty())
	{
		sf = new Subfield('b', tbIncipit->getFieldString(tbIncipit->numero_comp)); // numero composizioni
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->ds_contesto)->IsEmpty())
	{

//		if (DATABASE_ID == DATABASE_INDICE) Rimoss 16/06/15 a fronte di bonifica DB
//		{
//			CString * sPtr = tbIncipit->getFieldString(tbIncipit->ds_contesto);
//
//
//			char lastChar = ' ';
//			if (sPtr->EndsWith("~?"))
//					lastChar = sPtr->GetLastChar();
//
//			sPtr->ChangeTo('&', '{'); // 10/06/2015 Evolutiva richiesta dalla Barbieri
//			sPtr->ChangeTo('^', '}');
//			sPtr->ChangeTo('?', '}');
//			if (lastChar == '?')
//			{
//				// Rimettiamo in coda
//				sPtr->ExtractLastChar();
//				sPtr->AppendChar(lastChar);
//			}
//			sf = new Subfield('c', sPtr);
//		}
//		else
			sf = new Subfield('c', tbIncipit->getFieldString(tbIncipit->ds_contesto));


		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->numero_mov)->IsEmpty())
	{

		sf = new Subfield('f', tbIncipit->getFieldString(tbIncipit->numero_mov)); // numero movimento
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->numero_p_mov)->IsEmpty())
	{
		sf = new Subfield('g', tbIncipit->getFieldString(tbIncipit->numero_p_mov)); // numero progressivo movimenti
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->registro_mus)->IsEmpty())
	{
		tbIncipit->getFieldString(tbIncipit->registro_mus)->Strip(CString::trailing, ' ');
		sf = new Subfield('h', tbIncipit->getFieldString(tbIncipit->registro_mus)); // registro musicale
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->cd_forma)->IsEmpty())
	{
		sf = new Subfield('i', tbIncipit->getFieldString(tbIncipit->cd_forma)); // codice forma musicale
		///sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->cd_tonalita)->IsEmpty())
	{
		sf = new Subfield('l', tbIncipit->getFieldString(tbIncipit->cd_tonalita)); // codice tonalit�
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->chiave_mus)->IsEmpty())
	{
		sf = new Subfield('m', tbIncipit->getFieldString(tbIncipit->chiave_mus)); // chiave musicale
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->alterazione)->IsEmpty())
	{
		sf = new Subfield('n', tbIncipit->getFieldString(tbIncipit->alterazione)); // alterazione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->misura)->IsEmpty())
	{
		sf = new Subfield('o', tbIncipit->getFieldString(tbIncipit->misura)); // misura
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->tempo_mus)->IsEmpty())
	{
		sf = new Subfield('p', tbIncipit->getFieldString(tbIncipit->tempo_mus)); // tempo musicale
		//sf->setData();
		df->addSubfield(sf);
	}


	if (!tbIncipit->getFieldString(tbIncipit->nome_personaggio)->IsEmpty())
	{
		sf = new Subfield('q', tbIncipit->getFieldString(tbIncipit->nome_personaggio)); // nome personaggio
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbIncipit->getFieldString(tbIncipit->bid_letterario)->IsEmpty())
	{
		TbTitolo *tbTitoloIncipit = new TbTitolo(tbTitolo);

		sf = new Subfield('r', tbIncipit->getFieldString(tbIncipit->bid_letterario)); // Identificativo del titolo di incipit letterario
		//sf->setData();
		df->addSubfield(sf);

	//printf ("tbIncipit.bid=%s, tbTitolo.bid=%s", tbIncipit->getField(tbIncipit->bid), tbTitolo->getField(tbTitolo->bid));
		const char *bidLetterario = tbIncipit->getField(tbIncipit->bid_letterario);
		tbTitoloIncipit->loadRecord(bidLetterario);
//tbTitoloIncipit->dumpRecord();
//printf ("StringRecord=%s", tbTitoloIncipit->getStringRecord());


//		sf = new Subfield('s'); // ISBD del titolo di incipit letterario
//		sf->setData(tbTitoloIncipit->getField(tbTitolo->isbd));
//		df->addSubfield(sf);

		CString *sPtr = tbTitoloIncipit->getFieldString(tbTitolo->isbd);
		int pos;
		if ((pos = sPtr->First('*')) != -1) // abbiamo almeno un asterisco?
		{
			if (pos) // Se non primo carattere
			{
				sPtr->Replace(pos, 1, NSE.data());
				sPtr->PrependString(NSB.data());
			} else
				sPtr->ExtractFirstChar();
		}

		sf = new Subfield('s', sPtr);
		//sf->setData();
		df->addSubfield(sf);



		delete tbTitoloIncipit;

	}

	marcRecord->addDataField(df);
} // end creaTag926_Incipit




/*!
\brief <b>Tag 927 - Personaggi e interpreti (mat. musicale)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>927</td></tr>
<tr><td valign=top>Descrizione</td><td>Personaggi e interpreti (mat. musicale)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - personaggio. Non ripetibile.
        <li> b - timbro vocale. Non ripetibile.
        <li> 3 - identificativo dell'autore (interprete). Non ripetibile.
        <li> c - Nome dell'autore. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppLegami::creaTag927_PersonaggiEInterpreti() {
	DataField *df;
	Subfield *sf;
	CString *sPtr;
	CString *nomePersonaggioPtr, *timbroVocalePtr;
	CString entryFile;
	long position;
	//long offset;
	char *entryPtr;
	bool retb;
//tbPersonaggio->dumpRecord();

	nomePersonaggioPtr = tbPersonaggio->getFieldString(tbPersonaggio->nome_personaggio);
	timbroVocalePtr =  tbPersonaggio->getFieldString(tbPersonaggio->cd_timbro_vocale);
	if (nomePersonaggioPtr->IsEmpty() && timbroVocalePtr->IsEmpty())
		return;

	df = new DataField();
	df->setTag((char *)"927");

	if (!nomePersonaggioPtr->IsEmpty())
	{
		nomePersonaggioPtr->Strip(nomePersonaggioPtr->trailing, ' ');
		sf = new Subfield('a', nomePersonaggioPtr); // personaggio
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!timbroVocalePtr->IsEmpty())
	{
//		sPtr = tbPersonaggio->getFieldString(tbPersonaggio->cd_timbro_vocale);
		timbroVocalePtr->Strip(timbroVocalePtr->trailing);
		sf = new Subfield('b', timbroVocalePtr); // timbro vocale
		//sf->setData();
		df->addSubfield(sf);
	}


	CString *keyPtr = tbPersonaggio->getFieldString(tbPersonaggio->id_personaggio);
	CString keyTrTitAut;

	//	keyPtr->leftPadding('0', 10); // Make searcheable key
	//	keyPtr->rightPadding(' ', 10); // 06/12/2013 Make searcheable key per CFI


		keyPtr->leftPadding('0', 10); // 18/03/2014 Mantis 5530. Vale per indice e tutti i poli.
										// Il problema di CFI e' stato sistemato in fase di scarico della tabella tr_per_int che era toppato


	if (trPerInt->existsRecord(keyPtr->data()))
	{
//		CString * lineRead = new CString(2048);
		CString * lineRead = new CString(4096);
		//CString *lineRead = &FixedLengthLine;

		while (trPerInt->loadNextRecord(keyPtr->data())) {
//trPerInt->dumpRecord();
			// Dal vid vediamo se abbiamo un autore
			sPtr = trPerInt->getFieldString(trPerInt->vid);

			// L'autore ha una relazione 590 (dalla tb_codici LETA, interprete) nella tr_tit_aut?
		    // Troviamo le relazioni agli autori
		    // ---------------------------------
//BinarySearch *bs = new BinarySearch();
//bs->setDebug();

			if (trTitAutRel->getOffsetBufferTbPtr())
				retb = BinarySearch::search(
//				retb = bs->nonStatic_search(
						trTitAutRel->getOffsetBufferTbPtr(),
						trTitAutRel->getElementsTb(),
						trTitAutRel->getKeyPlusOffsetPlusLfLength(),
						rootBid,
						trTitAutRel->getKeyLength(), position, &entryPtr);
			else
			{
				retb = BinarySearch::search(
//				retb = bs->nonStatic_search(
						trTitAutRel->getTbOffsetIn(),
						trTitAutRel->getElementsTb(),
						trTitAutRel->getKeyPlusOffsetPlusLfLength(),
						rootBid,
						trTitAutRel->getKeyLength(), position, &entryFile);
				entryPtr = entryFile.data();
			}
//delete bs;
			if (retb)
			{ // Questo Bid ha legami ad autori

				// Dalla posizione prendiamo l'offset
//				long offset; // = atol (entryPtr+trTitAutRel->getKeyLength()); // offsetBuffertrTitAutRelPtr+position
				int offset; // 18/03/2014 32 bit

				if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//					memcpy (&offset, entryPtr+trTitAutRel->getKeyLength(), 4);	// OFFSET BINARI
					offset =  *((int*)(entryPtr+trTitAutRel->getKeyLength())); // 24/03/2015
				else
					offset = atoi (entryPtr+trTitAutRel->getKeyLength()); // OFFSET in ASCII

				// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
				trTitAutRel->getTbIn()->SeekTo(offset);
				if (!lineRead->ReadLineWithPrefixedMaxSize(trTitAutRel->getTbIn()))
			        SignalAnError(__FILE__, __LINE__, "read failed");

//printf ("\nsPtr(trPerInt->vid) = '%s'", sPtr->data());
//printf ("\nlineRead = '%s'", lineRead->data());

				// Splittiamo la riga negli n elementi che la compongono
				CTokenizer *Tokenizer = new CTokenizer("|");
				OrsChar *Token;

				Tokenizer->Assign(lineRead->data());

				Token = Tokenizer->GetToken(); // Remove root
				CString *cdRelazione;
				TbReticoloAut *tbReticoloAut = 0;
				while(*(Token = Tokenizer->GetToken()))
				{
					if (tbReticoloAut)
						tbReticoloAut->assign(Token);
					else
						tbReticoloAut = new TbReticoloAut( Token);
//tbReticoloAut->dumpRecord();
					// E' il vid di nostro interesse?
					if (sPtr->isEqual(tbReticoloAut->getField(tbReticoloAut->vid)))
					{ // stesso vid
						// Controlla codice di relazione
						cdRelazione = tbReticoloAut->getFieldString(tbReticoloAut->cd_relazione);
//						if (cdRelazione->isEqual("590")	|| cdRelazione->isEqual("721"))
						if (DATABASE_ID == DATABASE_INDICE || cdRelazione->isEqual("590"))
						{
							if (tbAutore->loadRecord(sPtr->data()))
							{
//tbAutore->dumpRecord();
								 sf = new Subfield('c', tbAutore->getFieldString(tbAutore->ds_nome_aut)); // Nome dell'autore
								 //sf->setData();
								 df->addSubfield(sf);

								 if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
									 sf = new Subfield('3', sPtr); // Vid dell'autore
									 //sf->setData();
								 else // TIPO_SCARICO_OPAC
								 {
//									CString id = "IT\\ICCU\\";
									CString id((char *)"IT\\ICCU\\", 8);
									id.AppendString(sPtr->data(), 4); // Prendi il Polo
									id.AppendChar('\\');
									id.AppendString(sPtr->data()+4, 10-4);	 // Prendi resto del bid
									 sf = new Subfield('3', &id); // Vid dell'autore
									//sf->setData();
								 }
								 df->addSubfield(sf);
							}
						}
					}
				}
				delete Tokenizer;
				delete tbReticoloAut;
			} // end if legami ad autori
		}
	delete lineRead;
	}



/*
	 sf = new Subfield('3'); // identificativo dell'autore (interprete)
	 sf->setData(tbPersonaggio->getField(tbPersonaggio->));
	 df->addSubfield(sf);

	 */

	marcRecord->addDataField(df);

} // end creaTag927_PersonaggiEInterpreti


/*!
\brief <b>Tag 928 - Dati codificati per titolo uniforme musicale</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>928</td></tr>
<tr><td valign=top>Descrizione</td><td>Dati codificati per titolo uniforme musicale</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - forma della composizione. Ripetibile.
        <li> b - organico sintetico della composizione. Non ripetibile.
        <li> c - organico analitico della composizione. Non ripetibile.
        <li> z - legame al titolo uniforme (tag 500)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

void Marc4cppLegami::creaTag928_TitoloUniformeMusicale() {
	DataField *df;
	Subfield *sf;

//	if (!tbComposizione->loadRecord(tbTitolo->getField(tbTitolo->bid)))
//		return;

	df = new DataField();
	df->setTag((char *)"928");

	if (!tbComposizione->getFieldString(tbComposizione->cd_forma_1)->IsEmpty()) {
		sf = new Subfield('a', tbComposizione->getFieldString(tbComposizione->cd_forma_1)); // forma della composizione
		//sf->setData();
		df->addSubfield(sf);
	}
	if (!tbComposizione->getFieldString(tbComposizione->cd_forma_2)->IsEmpty()) {
		sf = new Subfield('a', tbComposizione->getFieldString(tbComposizione->cd_forma_2)); // forma della composizione
		//sf->setData();
		df->addSubfield(sf);
	}
	if (!tbComposizione->getFieldString(tbComposizione->cd_forma_3)->IsEmpty()) {
		sf = new Subfield('a', tbComposizione->getFieldString(tbComposizione->cd_forma_3)); // forma della composizione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->ds_org_sint)->IsEmpty())
	{
		sf = new Subfield('b', tbMusica->getFieldString(tbMusica->ds_org_sint)); // organico sintetico della composizione
		//sf->setData();
		df->addSubfield(sf);
	}

	if (!tbMusica->getFieldString(tbMusica->ds_org_anal)->IsEmpty())
	{
		sf = new Subfield('c', tbMusica->getFieldString(tbMusica->ds_org_anal)); // organico analitico della composizione
		//sf->setData();
		df->addSubfield(sf);
	}

	// Mail Roveri 28/03/2011
	 if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		 sf = new Subfield('z', tbTitolo->getFieldString(tbTitolo->bid));
	 else // TIPO_SCARICO_OPAC
	 {
		char *bid = tbTitolo->getField(tbTitolo->bid);
		CString id((char *)"IT\\ICCU\\", 8);
		int pos =3;
		if (isAnticoE(bid))
			pos = 4;
		id.AppendString(bid, pos); // Prendi il Polo
		id.AppendChar('\\');
		id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid


		sf = new Subfield('z', &id);
		//sf->setData();
	 }
	df->addSubfield(sf);










	marcRecord->addDataField(df);

} // End creaTag928_TitoloUniformeMusicale

/*!
\brief <b>Tag 929 - Composizione (mat. musicale)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>929</td></tr>
<tr><td valign=top>Descrizione</td><td>Composizione (mat. musicale)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - Numero d'ordine. Non ripetibile.
        <li> b - numero d'opera . Non ripetibile.
        <li> c - numero di catalogo tematico. Non ripetibile.
        <li> d - datazione della composizione. Non ripetibile.
        <li> e - tonalita' della composizione. Non ripetibile.
        <li> f - sezioni. Non ripetibile.
        <li> g - titolo di ordinamento. Non ripetibile.
        <li> h - titolo dell'estratto. Non ripetibile.
        <li> i - appellativo. Non ripetibile.
        <li> z - legame al titolo uniforme (tag 500)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppLegami::creaTag929_Composizione() {
	DataField *df;
	Subfield *sf;
	//const char *ptr;
	CString *sPtr;
	int pos;

	df = new DataField();
	df->setTag((char *)"929");

	tbComposizione->getFieldString(tbComposizione->numero_ordine)->Strip(CString::trailing, ' ');
	sPtr = tbComposizione->getFieldString(tbComposizione->numero_ordine);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('a', sPtr); // Numero d'ordine
		//sf->setData();
		df->addSubfield(sf);
	}

	tbComposizione->getFieldString(tbComposizione->numero_opera)->Strip(CString::trailing, ' ');
	sPtr = tbComposizione->getFieldString(tbComposizione->numero_opera);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('b', sPtr); // numero d'opera
		//sf->setData();
		df->addSubfield(sf);
	}

	tbComposizione->getFieldString(tbComposizione->numero_cat_tem)->Strip(CString::trailing, ' ');
	sPtr = tbComposizione->getFieldString(tbComposizione->numero_cat_tem);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('c', sPtr); // numero di catalogo tematico
		//sf->setData();
		df->addSubfield(sf);
	}

	tbComposizione->getFieldString(tbComposizione->datazione)->Strip(CString::trailing, ' ');
	sPtr = tbComposizione->getFieldString(tbComposizione->datazione);
	if (!sPtr->IsEmpty())
	{
		sPtr->Strip(sPtr->trailing);
		sf = new Subfield('d', sPtr); // datazione della composizione
		//sf->setData();
		df->addSubfield(sf);
	}

//	tbComposizione->getFieldString(tbComposizione->cd_tonalita)->Strip(CString::trailing, ' ');
	sPtr = tbComposizione->getFieldString(tbComposizione->cd_tonalita);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('e', sPtr); // tonalit� della composizione
		//sf->setData();
		df->addSubfield(sf);
	}

	sPtr = tbComposizione->getFieldString(tbComposizione->ds_sezioni);
	if (!sPtr->IsEmpty())
	{
		sPtr->Strip(sPtr->trailing);
		sf = new Subfield('f', sPtr); // sezioni
		//sf->setData();
		df->addSubfield(sf);
	}

	sPtr = tbComposizione->getFieldString(tbComposizione->ky_ord_den);
	if (!sPtr->IsEmpty())
	{

//		char *ptr = sPtr->data();
//		if (*ptr == '*') // nosort
//			ptr++;
//
//		sf = new Subfield('g'); // titolo di ordinamento
//		sf->setData(ptr);
//		df->addSubfield(sf);

		if ((pos = sPtr->First('*')) != -1) // abbiamo almeno un asterisco?
		{
			if (pos) // Se non primo carattere
			{
				sPtr->Replace(pos, 1, NSE.data());
				sPtr->PrependString(NSB.data());
			} else
				sPtr->ExtractFirstChar();
		}

		sf = new Subfield('g', sPtr);
		//sf->setData();
		df->addSubfield(sf);

	}

	sPtr = tbComposizione->getFieldString(tbComposizione->ky_est_den);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('h', sPtr); // titolo dell'estratto
		//sf->setData();
		df->addSubfield(sf);
	}

	sPtr = tbComposizione->getFieldString(tbComposizione->ky_app_den);
	if (!sPtr->IsEmpty())
	{
//		char *ptr = sPtr->data();
//		if (*ptr == '*') // nosort
//			ptr++;

		if ((pos = sPtr->First('*')) != -1) // abbiamo almeno un asterisco?
		{
			if (pos) // Se non primo carattere
			{
				sPtr->Replace(pos, 1, NSE.data());
				sPtr->PrependString(NSB.data());
			} else
				sPtr->ExtractFirstChar();
		}

		sf = new Subfield('i', sPtr); // appellativo
		df->addSubfield(sf);
	} // End if

	// Mail Roveri 28/03/2011
//	sf = new Subfield('z', tbTitolo->getFieldString(tbTitolo->bid));
//	df->addSubfield(sf);

	// Mail Roveri 28/03/2011
	 if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		 sf = new Subfield('z', tbTitolo->getFieldString(tbTitolo->bid));
	 else // TIPO_SCARICO_OPAC
	 {
		char *bid = tbTitolo->getField(tbTitolo->bid);
		CString id((char *)"IT\\ICCU\\", 8);
		int pos =3;
		if (isAnticoE(bid))
			pos = 4;
		id.AppendString(bid, pos); // Prendi il Polo
		id.AppendChar('\\');
		id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid

		sf = new Subfield('z', &id); // Vid dell'autore
		//sf->setData();
	 }
	df->addSubfield(sf);


	marcRecord->addDataField(df);
} // end creaTag929_Composizione

/*!
\brief <b>Tag 951 - Acquisizioni (ordini)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>951</td></tr>
<tr><td valign=top>Descrizione</td><td>Acquisizioni (ordini)</td></tr>
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
        <li> b - Dati dell'ordine
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
bool Marc4cppLegami::creaTag951_Ordine() {
	// E' una biblioteca da esportare?
	char *cdBib = (char*) tbaOrdini->getField(tbaOrdini->cd_bib);
	if (bibliotecheDaNonMostrareIn950KV->existsKey(cdBib))
		return true; // Non creare la 951 per questa biblioteca

	DataField *df;
	Subfield *sf;
	df = new DataField();
	df->setTag((char *)"951");
	CString sTmp;


	CString *dsBiblioteca;
//	TbfBibliotecaInPolo *tbfBibliotecaInPolo =	(TbfBibliotecaInPolo *) tbfBibliotecaInPoloKV->GetValueFromKey(cdBib);
//	if (!tbfBibliotecaInPolo) {
//		SignalAWarning(__FILE__, __LINE__, "Biblioteca non trovata per '%s'",	tbaOrdini->getField(tbaOrdini->cd_bib));
//		dsBiblioteca = tbaOrdini->getField(tbaOrdini->cd_bib);
//	} else
//		dsBiblioteca = tbfBibliotecaInPolo->getField( tbfBibliotecaInPolo->ds_biblioteca);


    CString key = marc4cppDocumento->getPolo(); // prendi il polo
    key.AppendString(tbaOrdini->getFieldString(tbaOrdini->cd_bib)); // prendi la biblioteca

    if (tbfBiblioteca->loadRecord(key.data()))
        {
    	dsBiblioteca = tbfBiblioteca->getFieldString(tbfBiblioteca->nom_biblioteca);
        }
    else
    {
    	SignalAWarning(__FILE__, __LINE__, "Biblioteca non trovata per '%s'",	tbaOrdini->getField(tbaOrdini->cd_bib));
    	dsBiblioteca = tbaOrdini->getFieldString(tbaOrdini->cd_bib);
    }




	dsBiblioteca->Strip(dsBiblioteca->trailing, ' ');
	sf = new Subfield('a', dsBiblioteca); // Nome della biblioteca
	//sf->setData();
	df->addSubfield(sf);


	CString s;
	s = tbaOrdini->getField(tbaOrdini->cd_bib);
	s.AppendString(tbaOrdini->getFieldString(tbaOrdini->anno_ord));

	//sTmp = tbaOrdini->getField(tbaOrdini->id_ordine);
	sTmp = tbaOrdini->getField(tbaOrdini->cod_ord); // 17/09/2018 Mantis 6680


	sTmp.leftPadding('0', 9); // left pad con 0 fino a 9 caratteri
	s.AppendString(&sTmp);

	s.AppendString(tbaOrdini->getFieldString(tbaOrdini->cod_tip_ord));
	s.AppendString(tbaOrdini->getFieldString(tbaOrdini->stato_ordine));

	//s.AppendString(tbaOrdini->getField(tbaOrdini->data_ord));
//	CString *sPtr = tbaOrdini->getFieldString(tbaOrdini->data_ord);
//	char chr = sPtr->GetLastChar();
//	if ( chr == '\n')
//		sPtr->ExtractLastChar();
	s.AppendString(tbaOrdini->getFieldString(tbaOrdini->data_ord));

//	sPtr = tbaOrdini->getFieldString(tbaOrdini->note);
//	chr = sPtr->GetLastChar();
//	if ( chr == '\n')
//		sPtr->ExtractLastChar();
	s.AppendString(tbaOrdini->getFieldString(tbaOrdini->note));

	sf = new Subfield('b', &s); // Dati dell�ordine
	//sf->setData();
	df->addSubfield(sf);

	marcRecord->addDataField(df);
	return true;
} // End Marc4cppLegami::creaTag951_Ordine()



/*!
\brief <b>Tag 961 - Ordini</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>961</td></tr>
<tr><td valign=top>Descrizione</td><td>Ordini</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - Nome della biblioteca
        <li> b - Dati dell'ordine
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

/*
Aggiornamenti:
	 31/03/2010 10.48 Richieste CFI
	 - La 961 va prodotta solo per notizie di natura S in presenza di ordini continuativi;
		nello specifico va prodotta una 961 per ogni �primo ordine di abbonamento� sia esso
		aperto (stato=A) o chiuso (stato=C) mentre non danno seguito a produzione di 961 gli
		ordini di rinnovo (identificabili dal fatto che hanno il riferimento al primo ordine valorizzato).

		Vorrei precisare che in questa logica i dati riportati nei campi dell�etichetta 961
		sono tutti reperiti dal primo ordine,  e quindi anche Stato_ord e data_ord sono
		estratti dal primo ordine.


*/

bool Marc4cppLegami::creaTag961_Ordine() {
	DataField *df;
	Subfield *sf;

	// 25/01/2010 12.15
	char *cdBib = (char*) tbaOrdini->getField(tbaOrdini->cd_bib);
	if (bibliotecheDaNonMostrareIn950KV->existsKey(cdBib))
		return true; // Non creare la 961 per questa biblioteca

//tbaOrdini->dumpRecord();

//
//	char statoOrdine = *tbaOrdini->getField(tbaOrdini->stato_ordine);
//	if (statoOrdine == 'C' || statoOrdine == 'c')
//		return true; // Continua solo se ordine aperto

	df = new DataField();
	df->setTag((char *)"961");
	CString *sPtr, *sPtr1;
	CString str;
	char dateBuf[8 + 1];
	*(dateBuf + 8) = 0;

	char *ptr;
	CString dsBiblioteca;
    CString key = marc4cppDocumento->getPolo(); // prendi il polo
    key.AppendString(tbaOrdini->getFieldString(tbaOrdini->cd_bib)); // prendi la biblioteca

    if (tbfBiblioteca->loadRecord(key.data()))
        {
    	dsBiblioteca = tbfBiblioteca->getField(tbfBiblioteca->nom_biblioteca);
        }
    else
    {
    	SignalAWarning(__FILE__, __LINE__, "Biblioteca non trovata per '%s'",	tbaOrdini->getField(tbaOrdini->cd_bib));
    	dsBiblioteca = tbaOrdini->getField(tbaOrdini->cd_bib);
    }

    dsBiblioteca.Strip(dsBiblioteca.trailing, ' ');
	sf = new Subfield('a', &dsBiblioteca); // Nome della biblioteca
	//sf->setData();
	df->addSubfield(sf);

//	tbaOrdini->dumpRecord();

	sf = new Subfield('b'); // Dati dell'ordine
	CString s;
	s = tbaOrdini->getField(tbaOrdini->cd_bib) + 1; // salta il primo dei 3 caratteri	// Pos. 0-1

	s.AppendString(tbaOrdini->getFieldString(tbaOrdini->cod_tip_ord));						// Pos. 2

	sPtr1 = tbaOrdini->getFieldString(tbaOrdini->anno_ord);								// Pos. 3-5
	ptr = sPtr1->SubstringData(1);
	s.AppendString(ptr, sPtr1->getBufferSubStringLength());

	sPtr = tbaOrdini->getFieldString(tbaOrdini->cod_ord);								// Pos. 6-11
	sPtr->leftPadding('0', 6); // 08/03/2010
	s.AppendString(sPtr); // cod_1ord o cod_ord

	s.AppendString(tbaOrdini->getFieldString(tbaOrdini->stato_ordine));						// Pos. 12

	CMisc::formatDate1(tbaOrdini->getField(tbaOrdini->data_ord), dateBuf);				// Pos. 13-21
	s.AppendString(dateBuf, 8);

	sf->setData(&s);
	df->addSubfield(sf);


	sf = new Subfield('9', (char *)"???", 3); // Generare un nuovo sottocampo $9 con la collocazione collegata all�ordine presente nel $b.
							// Serve per associare abbonamento-collocazione nel caso di pi� abbonamenti per lo stesso BID (es. CFI0358199).

	//sf->setData();
	df->addSubfield(sf);



	marcRecord->addDataField(df);
	return true;
} // creaTag961_Ordine


void Marc4cppLegami::creaLegamiTitoloBiblioteca() {
	//char bid[10+1];	bid[10]=0;
	const char *bid = tbTitolo->getField(tbTitolo->bid);
	DataField *df;
	bool retb;

//printf ("\n\ncreaLegamiTitoloBiblioteca trTitBib->existsRecord(%s)", bid);

//	if (DATABASE_ID == DATABASE_INDICE)
//	{
//		retb= trTitBib->existsRecordNonUnique(bid);
//
//		trTitBib->loadNextRecord();
//		// Crea la 899
//		if (df = creaTag899_Localizzazione())
//			marcRecord->addDataField(df);
//
//		while (trTitBib->loadNextRecordDaIndice(bid)) {
//			// Crea la 899
//			if (df = creaTag899_Localizzazione())
//				marcRecord->addDataField(df);
//		}
//
//	}
//	else
//	{

	if (IS_TAG_TO_GENERATE(856))
		if (tsLinkMultim->existsRecord(bid)) // 18/10/2010 Contardi/Giangregorio. 856 va generata sempre se esiste il bid nella tsLinkMultim
			creaTag856_LinkMultimediale(bid);
//printf ("\ncreaTag856_LinkMultimediale(bid=%s)", bid);

	if (DATABASE_ID == DATABASE_SBNWEB && IS_TAG_TO_GENERATE(967)) // 15/01/2015 (mail La Porta del 09/12/2014)
		{
		int mCtr, nCtr;
		contaBidColl_MN((char *)bid, &mCtr, &nCtr);
		if (mCtr || nCtr)
			if (df = creaTag967_legamiNM(bid, mCtr, nCtr))
				marcRecord->addDataField(df);
		}


	if (DATABASE_ID == DATABASE_SBNWEB && IS_TAG_TO_GENERATE(977)) // 16/12/2014 (mail La Porta del 09/12/2014)
//		if (tb977->existsRecord(bid))
		if (tb977->existsRecordNonUnique(bid))
		{
			if (df = creaTag977_localizzazioni(bid))
				marcRecord->addDataField(df);
		}

	if (IS_TAG_TO_GENERATE(899))
	{
//printf ("\nGenerate 899");
		retb = trTitBib->existsRecord(bid);
		if (retb)
		{
			// Abbiamo un 430 ("continuazione di")?
			df = marcRecord->getDataField((char *)"430");
			bool has430 = df ? true : false;

			// Abbiamo un 440 ("continua con")?
			df = marcRecord->getDataField((char *)"440");
			bool has440 = df ? true : false;


//printf ("\nexists record");
			while (trTitBib->loadNextRecord(bid)) {
				// Crea la 899
				if (df = creaTag899_Localizzazione(has430, has440))
					marcRecord->addDataField(df);
			} // end while
		}

	}


} // End Marc4cppLegami::creaLegamiTitoloBiblioteca




/*
 Caso di notizia documento (M, S) SOLO ARRIVO di legami 01.
 Non ha relazioni 01 ad altri titoli
 Quindi puo essere arrivo per le 462, 463, 464. NON puo avere  461 perche' non e' partenza di nessun legame 01

 Controllo se qualcuno mi fa riferimento con legame 01 e natura base M e W o N
 Se SI per ogni riferimento cre una 46x. Con
	 x = 4 se il riferimento e di natura N
	 x = 3 se il riferimento NON e a sua volta riferimento di legame 01 con nature M o W
	 x = 2 se il riferimento e a sua volta riferimento di legame 01 con natura M o W
 */
void Marc4cppLegami::elabora46x(char *bid, bool bidHaPadre) // , char *sequenza
{
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit
	CString legame;

	//	char buf[80];
	char *entryPtr;
	char naturaBase, naturaColl, tmpChar;
	OrsChar *tokenPtr;
	DataField *df;

//tbTitolo->dumpRecord();


	char natura = *(tbTitolo->getField(tbTitolo->cd_natura));
//	if (natura != 'M' && natura != 'S') {
 	if (natura != NATURA_M_MONOGRAFIA
 		&& natura != NATURA_S_PERIODICO
 		&& natura != NATURA_N_TITOLO_ANALITICO

 		&& natura != NATURA_W_VOLUME_PRIVO_DI_TITOLO_SIGNIFICATIVO	// 27/09/2012 Segnalazione Renato per IEI bid RAV0107640

 	) {
		return;
	}

	// Controllo se qualcuno mi fa riferimento con legame 01 e natura M e W o N
	// Tramite la lista invertita

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
		CString entryFile;
 	if (natura == NATURA_N_TITOLO_ANALITICO)
 	{
 		if (offsetBufferTrTitTitRelPtr)
 	 	 	retb = BinarySearch::search(offsetBufferTrTitTitRelPtr, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length,position, &entryPtr);
 		else
 		{
 			retb = BinarySearch::search(trTitTitRelOffsetIn, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
 			entryPtr = entryFile.data();
 		}


 	 	if (!retb) {
 			return; // NON E' PARTENZA DI LEGAMI
 		}
 	}
 	else
 	{
 		if (offsetBufferTrTitTitInvRelPtr)
 	 		retb = BinarySearch::search(offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length,position, &entryPtr);
 		else
 		{
 			retb = BinarySearch::search(trTitTitInvRelOffsetIn, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
 			entryPtr = entryFile.data();
 		}

 		if (!retb) {
 			return; // NON E' ARRIVO DI LEGAMI
 		}
 	}

	// TITOLO ARRIVO DI LEGAMI.
	// Gestiamo solo quelli di tipo 01
//	CString *sPtr = new CString(4096);
	CString *sPtr = new CString(8192); // 30/07/2012

	// Dalla posizione prendiamo l'offset
//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atol (entryPtr+key_length); // OFFSET in ASCII


	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo

//printf ("\nOffset = %ld", offset);

 	if (natura == NATURA_N_TITOLO_ANALITICO) // Spoglio
 	{
 		trTitTitRelIn->SeekTo(offset);
 		retb = sPtr->ReadLineWithPrefixedMaxSize(trTitTitRelIn);
 	}
 	else
 	{
 		retb = trTitTitInvRelIn->SeekTo(offset);
 		retb = sPtr->ReadLineWithPrefixedMaxSize(trTitTitInvRelIn);
 	}
	if (!retb)
	    SignalAnError(__FILE__, __LINE__, "read failed");

//printf ("\n%s", sPtr->data());

	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	Tokenizer.GetToken(); // Skip bid riferito da

	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase,TokenCdNaturaColl, TokenSequenza;

	// Cicliamo sui legami di partenza
	const char *commaSep = ",";
	OrsChar *tokenLegamePtr;
	long tokenLegameLength;

	while (*(tokenPtr = Tokenizer.GetToken()))
	{
//printf ("\nDoing Token %s", tokenPtr);
		// Prendiamo il titolo legato
		CTokenizer tokenizerLegame(tokenPtr, commaSep);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(); //TokenTpLegameMusica




		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken(); // naturaColl = *




		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenSequenza.assign( tokenLegameLength, tokenLegamePtr);

		TokenSequenza.Strip(TokenSequenza.trailing, '\n');

		// E' un legame valido?
		if (!TokenTpLegame.isEqual(CD_LEGAME_01_FA_PARTE_DI)) // "01"
		{
			continue; // Non e' un legame di tipo valido
		}

//		if (natura != NATURA_N_TITOLO_ANALITICO) // Mantis 4279
//		{
//			// Scambio nature per uso lista invertita
//			tmpChar = naturaBase;
//			naturaBase = naturaColl;
//			naturaColl = tmpChar;
//			printf("\nScambio nature %s", tokenPtr);
//		}


		// E' un legame ad una natura valida?
		if (	naturaBase != NATURA_M_MONOGRAFIA &&
				naturaBase != NATURA_W_VOLUME_PRIVO_DI_TITOLO_SIGNIFICATIVO &&
				naturaBase != NATURA_N_TITOLO_ANALITICO
				)
			continue; // Non e' una natura valida
		// Abbiamo trovato un riferimento di partenza
		//	     crea una 46x. Con x = 	4 se il riferimento e' di natura N
		//								3 se il riferimento NON e a sua volta riferimento di legame 01 con nature M o W
		//								2 se il riferimento e a sua volta riferimento di legame 01 con natura M o W
		//								1 se il riferimento non e' partenza di legame 01




		// Carichiamo il titolo che fa riferimento a
		tbTitolo->loadRecord(TokenBid.data());

//tbTitolo->dumpRecord();


		df = 0;

	 	if (natura == NATURA_N_TITOLO_ANALITICO) // Natura del record che si sta scaricando
	 	{
	 		if (naturaColl == NATURA_S_PERIODICO ) // Bug mantis 4144
	 		{
	 			if (IS_TAG_TO_GENERATE(461))
	 				df = creaTag461_FaParteDi_NotiziaSuperiore(&TokenSequenza);
	 		}
	 		else
	 		{
	 			if (IS_TAG_TO_GENERATE(463))
	 				df = creaTag463_PezzoFisico(&TokenBid, &TokenSequenza); // 463 verso il pezzo fisico dalla SPOGLIO
	 		}
	 	}


//-- Mantis 4361 B)
	 	else if (natura == NATURA_S_PERIODICO && DATABASE_ID != DATABASE_INDICE) // Natura del record che si sta scaricando
	 	{
	 		if (isPartenzaLegame01MW(bid))
	 		{
//	 			if (IS_TAG_TO_GENERATE(461))
//	 				df = creaTag461_FaParteDi_NotiziaSuperiore(&TokenSequenza);
				if (IS_TAG_TO_GENERATE(463)) // 19/02/2013 mail Roveri del 18/02/2013 +  segnalazione Rossana
					df = creaTag463_PezzoFisico(&TokenBid, &TokenSequenza);

	 		}
	 		else
	 		{
//				if (naturaBase == NATURA_N_TITOLO_ANALITICO)	// 26/04/2011 Segnalazione Renato/Rossana TO00189128
				if (naturaBase == NATURA_N_TITOLO_ANALITICO && naturaColl == NATURA_S_PERIODICO)	// 03/05/2011 Segnalazione Renato/Rossana TO00189128
				{
					// 03/05/2011 Non generare legame da spogli a periodici per il polo. Problema MO CFI0214027
					//df = creaTag464_AnaliticaPezzoFisico(&TokenBid); // 464 verso lo spoglio dal pezzo fisico
				}
				else
				{
					if (IS_TAG_TO_GENERATE(463))
						df = creaTag463_PezzoFisico(&TokenBid, &TokenSequenza);
				}
	 		}
	 	}

//--

	 	else
	 	{
//			if (naturaBase == NATURA_N_TITOLO_ANALITICO)
			if (naturaBase == NATURA_N_TITOLO_ANALITICO)
			{
				if (naturaColl != NATURA_S_PERIODICO) // Mantis 4279
					if (IS_TAG_TO_GENERATE(464))
						//df = creaTag464_AnaliticaPezzoFisico(&TokenBid); // 464 verso lo spoglio dal pezzo fisico
						df = creaTag464_AnaliticaSpoglio(&TokenBid, &TokenSequenza); // 23/02/2016 gestione sequenza


			}
			else if (isPartenzaLegame01MW(bid)) // 29/09/2010 CFI0014434
//			else if (isPartenzaLegame01MW(TokenBid.data())) //
			{
				if (DATABASE_ID == DATABASE_INDICE && bidHaPadre) // Roveri 01/10/2010 Non si devono produrre questi legami discendenti alle monografie se esiste un legame ascendente. (I.E intermedia)
				{
					// Skip
				}
				else
				{
// Arge 27/01/2011 IEI0307468
					if (isRiferimentoLegame01MW(bid))
					{
						if (naturaColl != 'S') // Mantis 0004555 08/07/2011
						{
							if (IS_TAG_TO_GENERATE(463))
							{
								df = creaTag463_PezzoFisico(&TokenBid, &TokenSequenza); // Legame verso il pezzo fisico dal SET o SUBSET
							}
						}
// Contraddice mantis 4555 (scaricherebbe troppa roba per un record unimarc)
//						else if (naturaColl == 'S') // Mail Roveri lunedì 18 febbraio 2013 16.55
//						{
//						if (IS_TAG_TO_GENERATE(463))
//							df = creaTag463_PezzoFisico(&TokenBid, &TokenSequenza); // Legame verso il pezzo fisico dal SET o SUBSET
//						}

					}
					else
					{
						if (IS_TAG_TO_GENERATE(462))
						{
							df = creaTag462_LegameALivelloIntermedio(&TokenBid, &TokenSequenza); // Legame ad intermedia
						}
					}

				}
			}
			else
			{
				if (DATABASE_ID != DATABASE_INDICE) // Roveri 21/09/2010 Non si devono produrre questi legami discendenti alle monografie
				{
			 		if (naturaColl == NATURA_S_PERIODICO) // Bug mantis 4144
			 		{
			 			if (IS_TAG_TO_GENERATE(461))
			 				df = creaTag461_FaParteDi_NotiziaSuperiore(&TokenSequenza);
			 		}
			 		else
			 		{
			 			if (IS_TAG_TO_GENERATE(463))
			 				df = creaTag463_PezzoFisico(&TokenBid, &TokenSequenza); // Legame verso il pezzo fisico dal SET o SUBSET
			 		}
				}
			}
	 	}

	 		if (df)
	 			marcRecord->addDataField(df);

	} // End while

	delete sPtr;

} // end elabora46x




DataField *Marc4cppLegami::creaTag977_localizzazioni(const char *bid)
{
	DataField *df = 0;
	Subfield *sf;
	df = new DataField((char *)"977", 3);

	CString *bibStr;
	while (tb977->loadNextRecord(bid))
	{
		bibStr = tb977->getFieldString(tb977->cd_bib);
		sf = new Subfield('a', bibStr);
		df->addSubfield(sf);


	} // End while

	return df;
} // End creaTag977_localizzazioni()


DataField *Marc4cppLegami::creaTag967_legamiNM(const char *bid, int mCtr, int nCtr)
{
	DataField *df = 0;
	Subfield *sf;
	char buffer [15];

	df = new DataField((char *)"967", 3);
	if (mCtr)
	{
		//itoa (mCtr,buffer,10);
		sprintf(buffer, "%d", mCtr);
		sf = new Subfield('m');
		sf->appendData(buffer);
		df->addSubfield(sf);
	}
	if (nCtr)
	{
		//itoa (nCtr,buffer,10);
		sprintf(buffer, "%d", nCtr);
		sf = new Subfield('n');
		sf->appendData(buffer);
		df->addSubfield(sf);
	}

	return df;
} // End creaTag967_legamiNM()




// ritorna i contatori in mCtr e nCtr
void Marc4cppLegami::contaBidColl_MN(char *bid, int *mCtr, int *nCtr)
{
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit

//	CString legame;
	//	char buf[80];
	char *entryPtr;
	char naturaBase, naturaColl;
	OrsChar *Token;
//	int count = 0;
	*mCtr = *nCtr = 0; // inizializza

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
	if (offsetBufferTrTitTitInvRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitInvRelOffsetIn, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}

	if (!retb) {
		return ;
	}

	// Dalla posizione prendiamo l'offset
//	CString * sPtr = new CString(4096); //
	CString * sPtr = new CString(8192); // 30/07/2012
	sPtr->SetResizeValueBy(16384); // 8192

//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitInvRelIn->SeekTo(offset);
	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitInvRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");


	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase, TokenCdNaturaColl;

	Tokenizer.GetToken(); // Skip bid riferito da
	const char *commaSep = ",";
	OrsChar *tokenLegamePtr;
	long tokenLegameLength;
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);


		tokenizerLegame.GetToken(); //TokenBid.assign(tokenLength, tokenizerLegame.GetToken(IS_NOT_QUOTED_TOKEN, commaSep, &tokenLength));

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual("01"))
			continue; // Non e' un legame di tipo valido
		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken();

		if ((naturaColl == 'M' || naturaColl == 'S'))
		{
			if (naturaBase == 'M')
				(*mCtr)++;
			else if (naturaBase == 'N')
				(*nCtr)++;
		}

	} // End while

	delete sPtr;
} // End contaBidColl_MN



/*!
\brief <b>Tag 689 -Tesauro</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>689</td></tr>
<tr><td valign=top>Descrizione</td><td>Voce di tesauro legata al titolo</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Descrizione termine di tesauro
        <li>c - Ccodice tesauro di riferimento
        <li>3 - Identificativo tesauro
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag689_Tesauro() {
	DataField *df = 0;
	Subfield *sf;

//tbLuogo->dumpRecord();
	CString *cd_the = tbTermineTesauro->getFieldString(tbTermineTesauro->cd_the);

	if (cd_the->IsEmpty())
		return df;

	df = new DataField();
	df->setTag((char *)"689");

	if (!cd_the->IsEmpty())
	{

		if (cd_the->isEqual("TDR"))
		{
			CString *ds_the = tbTermineTesauro->getFieldString(tbTermineTesauro->ds_termine_thesauro);
			int pos = ds_the->First(' ');
			if (pos != -1)
				{
				CString s;
				s.Equals(ds_the->SubstringData(0,pos));
				sf = new Subfield('a', &s); // codice
				df->addSubfield(sf);

				s.Equals(ds_the->SubstringData(pos+1));
				sf = new Subfield('b', &s); // descrizione
				df->addSubfield(sf);

				}

			else
			{
			sf = new Subfield('b', tbTermineTesauro->getFieldString(tbTermineTesauro->ds_termine_thesauro));
			df->addSubfield(sf);
			}
		}
		else
		{
			sf = new Subfield('b', tbTermineTesauro->getFieldString(tbTermineTesauro->ds_termine_thesauro));
			df->addSubfield(sf);
		}
	}

	if (!cd_the->IsEmpty())
	{
		CString *s = tbTermineTesauro->getFieldString(tbTermineTesauro->cd_the);
		s->Strip(CString::trailing, ' ');
		sf = new Subfield('c', s);
		df->addSubfield(sf);
	}

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)// 28/04/10
	{
		sf = new Subfield('3', tbTermineTesauro->getFieldString(tbTermineTesauro->did));
		df->addSubfield(sf);
	}
	return df;
} // end creaTag689_Tesauro


/*
 * Trova se riferimento di legame 01 con natura M o W
 */
bool Marc4cppLegami::isRiferimentoLegame01MW(char *bid) {
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit
	CString legame;
	//	char buf[80];
	char *entryPtr;
	char naturaBase, naturaColl;
	OrsChar *Token;

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
	if (offsetBufferTrTitTitInvRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitInvRelOffsetIn, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb) {
		return false; // NON E' ARRIVO DI LEGAMI
	}
	// TITOLO ARRIVO DI LEGAMI.
	// Gestiamo solo quelli di tipo 01

	// Dalla posizione prendiamo l'offset
	CString * sPtr = new CString(4096);
//	CString *sPtr = &FixedLengthLine;

//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitInvRelIn->SeekTo(offset);
	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitInvRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");


	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase,
			TokenCdNaturaColl;
	Tokenizer.GetToken(); // Skip bid riferito da
	const char *commaSep = ",";
	long tokenLegameLength;
	OrsChar *tokenLegamePtr;
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual(CD_LEGAME_01_FA_PARTE_DI))
			continue; // Non e' un legame di tipo valido

		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken();


		if (DATABASE_ID == DATABASE_INDICE) // 09/08/2010   Gestione di tutte le nature C, M, N, S, W
		{
			delete sPtr;
			return true;
		}
		if ((naturaBase == 'M' || naturaBase == 'W')
			&& (naturaColl == 'M' || naturaColl == 'W')) {
			delete sPtr;
			return true;
		}

	} // End while

	// Nessun riferimento di tipo 01 M o W al bid in questione
	delete sPtr;
	return false;
} // End isRiferimentoLegame01MW



/*
 * Trova se riferimento di legame 01 con natura M o W per bid a bid_base
 * 10/03/2016
 */
bool Marc4cppLegami::isRiferimentoLegame01MW_2(char *bid, char *bid_base) {
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit
	CString legame;
	//	char buf[80];
	char *entryPtr;
	char naturaBase, naturaColl;
	OrsChar *Token;

	// Dal BID cerchiamo l'offset del file di relazione ai titoli
	CString entryFile;
	if (offsetBufferTrTitTitInvRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitInvRelOffsetIn, elementsTrTitTitInvRel, keyPlusOffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb) {
		return false; // NON E' ARRIVO DI LEGAMI
	}
	// TITOLO ARRIVO DI LEGAMI.
	// Gestiamo solo quelli di tipo 01

	// Dalla posizione prendiamo l'offset
	CString * sPtr = new CString(4096);
//	CString *sPtr = &FixedLengthLine;

//	offset = atol(entryPtr + key_length); // offsetBufferTrTitTitPtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
//		memcpy (&offset, entryPtr+key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
	trTitTitInvRelIn->SeekTo(offset);
	if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitInvRelIn))
        SignalAnError(__FILE__, __LINE__, "read failed");


	// Splittiamo la riga negli n elementi che la compongono
	CTokenizer Tokenizer(sPtr->data(), "|");
	CString TokenBid, TokenTpLegame, TokenTpLegameMusica, TokenCdNaturaBase,
			TokenCdNaturaColl;
	Tokenizer.GetToken(); // Skip bid riferito da
	const char *commaSep = ",";
	long tokenLegameLength;
	OrsChar *tokenLegamePtr;
	while (*(Token = Tokenizer.GetToken())) // Per ogni legame di partenza
	{
		CTokenizer tokenizerLegame(Token, commaSep);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenBid.assign(tokenLegameLength, tokenLegamePtr);

		tokenizerLegame.GetToken(&tokenLegamePtr, &tokenLegameLength);
		TokenTpLegame.assign(tokenLegameLength, tokenLegamePtr);

		if (!TokenTpLegame.isEqual(CD_LEGAME_01_FA_PARTE_DI))
			continue; // Non e' un legame di tipo valido

		tokenizerLegame.GetToken(); //TokenTpLegameMusica
		naturaBase = *tokenizerLegame.GetToken();
		naturaColl = *tokenizerLegame.GetToken();


		if (DATABASE_ID == DATABASE_INDICE) // 09/08/2010   Gestione di tutte le nature C, M, N, S, W
		{
			if (TokenBid.isEqual(bid_base)) //10/03/2016
			{
				delete sPtr;
				return true;
			}
		}
		if ((naturaBase == 'M' || naturaBase == 'W')
			&& (naturaColl == 'M' || naturaColl == 'W')
			&& TokenBid.isEqual(bid_base)) { // 10/03/2016
			delete sPtr;
			return true;
		}

	} // End while

	// Nessun riferimento di tipo 01 M o W al bid in questione
	delete sPtr;
	return false;
} // End isRiferimentoLegame01MW_2



