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
 * TbTitolo.cpp
 *
 *  Created on: 15-May-2009
 *      Author: argentino
 */


#include "TbTitolo.h"
#include "BinarySearch.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

TbTitolo::TbTitolo(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length)
: Tb(tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{


	if (DATABASE_ID == DATABASE_INDICE)
		//setRecordSeparator("�"); // 0xC0
		setRecordSeparator(0xC0); // 0xC0 25/10/2012

	tbFields = 46;
	init();

}

TbTitolo::TbTitolo()
: Tb()
{
	tbFields = 46;
	init();

}


TbTitolo::~TbTitolo() {

}


TbTitolo::TbTitolo(TbTitolo *tbTitolo) : Tb(tbTitolo)
{
}







bool TbTitolo::loadRecordFromString(char *aStringRecord, int length)
{

	if (!Tb::loadRecordFromString(aStringRecord, length))
		return false;

	return ricalcolaIndiceAree();
}







bool TbTitolo::loadRecord(const char *key)
{

//printf("\nTb::loadRecord(%s)", key);

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbTitolo::loadRecord %s", key);
		return false;
	}

//dumpRecord();
	return ricalcolaIndiceAree();
} // End loadRecord




bool TbTitolo::ricalcolaIndiceAree()
{
	CString * strIndiceAree;
	strIndiceAree = getFieldString(indice_isbd);

	// Se natura A e e Tipo materiale U gestiamo solo l-equivalente della 200
	if (*getField(tp_materiale) == TP_MATERIALE_MUSICA_U && *getField(cd_natura) == NATURA_A_TITOLO_DI_RAGGRUPPAMENTO_CONTROLLATO)
	{
		// Sostituiamo alle varie A-0001,R-pppp,S-pppp,U-pppp (p stat per posizione) con la 200-0001
		strIndiceAree->assign((char *)"200-0001", 8);
		return true;
	}


	ATTValVector<CString *> areeVect;

	if (strIndiceAree->GetLastChar() == ';')
		strIndiceAree->ExtractLastChar();

	strIndiceAree->Split(areeVect, ';');

	// Prendiamo l'Isbd
	CString *isbd = getFieldString(this->isbd);

	// Ricalcoliamo le aree
	ricalcolaAreeIsbdUtf8(isbd->data(), &areeVect);

	// Ricomponiamo l'indice ISBD
	strIndiceAree->Clear();
	for (int i = 0; i < areeVect.length(); i++) {
		if (i)
			strIndiceAree->AppendChar(';');
		strIndiceAree->AppendString(areeVect[i]);
	}
	areeVect.DeleteAndClear();
	return true;
}


void TbTitolo::ricalcolaAreeIsbdUtf8(const char *isbd, ATTValVector<CString *> *areeVect) {
	char *start = (char *)isbd;
	char *ptr;
	int pos;
	int sepLen = 4;

	if (areeVect->length() < 1)
		return;

	// La prima area comincia sempre con posizione 1

	for (int i = 0; i < (areeVect->length() - 1); i++) {
		if (areeVect->Entry(i)->IsEmpty())
			continue;
//printf("\n\nEntry(i+1)=%s, start=%s", areeVect->Entry(i+1)->data(), start);

		if (areeVect->Entry(i + 1)->GetFirstChar() == '3') {
			ptr = strstr(start, ". ((");
			if (!ptr) {
				if ((ptr = strstr(start, ".((")) || (ptr = strstr(start, " ((")))
					sepLen = 3;
				else if ( ptr = strstr(start, ".   ((") )
					sepLen = 6;

			} else
				sepLen = 4;
			if (!ptr) {
//				SignalAWarning(
//						__FILE__,
//						__LINE__,
//						"ricalcolaAreeIsbdUtf, separatore di area non trovato: BID=%s, ISBD=%s", getField(this->bid), start);
				// Area delle note non trovata (non obbligatoria)
				continue;
			}
		} else {
			ptr = strstr(start, ". - ");
			if (!ptr) {
				ptr = strstr(start, ".  - "); // Bug in ISBD per TO00365580
				if (ptr)
					sepLen = 5;
				else if (ptr = strstr(start, ". -"))
					sepLen = 3;
			} else
				sepLen = 4;

			if (!ptr) {
//				SignalAWarning(
//						__FILE__,
//						__LINE__,
//						"ricalcolaAreeIsbdUtf, separatore di area non trovato: BID=%s, ISBD=%s", getField(this->bid), start);
				continue;
			} // Controlliamo le note non in area 3XX

		}


		ptr += sepLen;
		pos = ptr - isbd;

		areeVect->Entry(i + 1)->CropRightFrom(4);

		char buffer[33];
//		snprintf(buffer, sizeof buffer, "%d", pos);
		snprintf(buffer, sizeof buffer, "%d", pos+1);

		areeVect->Entry(i + 1)->AppendString(buffer);

		start = ptr;

		//printf("\ndopo=%s", areeVect->Entry(i+1)->data());
	}
} // End ricalcolaAreeIsbdUtf8
