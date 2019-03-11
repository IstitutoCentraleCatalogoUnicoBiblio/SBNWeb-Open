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
 * TsLinkMultim.cpp
 *
 *  Created on: 17-set-2010
 *      Author: Arge
 */


#include "TsLinkMultim.h"

#include "MarcConstants.h"
#include "MarcGlobals.h"
#include "BinarySearch.h"
#include "library/CTokenizer.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

TsLinkMultim::TsLinkMultim(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 8;

//	if (DATABASE_ID == DATABASE_INDICE)
//	{
//		initDereferencing();
//	}

	init();
}

//void TsLinkMultim::initDereferencing()
//{
//    if (DATABASE_ID == DATABASE_INDICE)
//    {
//        columnDereferenceAr[id_link_multim] = id_link_multim_indice;
//        columnDereferenceAr[ky_link_multim] = ky_link_multim_indice;
//        columnDereferenceAr[uri_multim] 	= uri_multim_indice;
//        columnDereferenceAr[ute_ins] 		= ute_ins_indice;
//        columnDereferenceAr[ts_ins] 		= ts_ins_indice;
//        columnDereferenceAr[ute_var] 		= ute_var_indice;
//        columnDereferenceAr[ts_var] 		= ts_var_indice;
//        columnDereferenceAr[fl_canc] 		= fl_canc_indice;
//        columnDereferenceAr[immagine] 		= immagine_indice;
//
//        this->tableIsDereferenced = true;
//    }
//}

TsLinkMultim::~TsLinkMultim() {

}

bool TsLinkMultim::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TsLinkMultim::loadRecord");
		return false;
	}
	return true;
} // End loadRecord




//void TsLinkMultim::getPrimaryKey(CString &pKey) {
//	char primaryKey [16+1];
//	primaryKey[16] = 0;
//	memcpy (primaryKey, stringRecord->Data()+6, 3);
//	memcpy (primaryKey+3, stringRecord->Data()+12, 3);
//	memcpy (primaryKey+6, stringRecord->Data()+18, 10);
//	pKey = primaryKey;
//}

bool TsLinkMultim::loadNextRecord(const char *key)
{
//	char * separator = "&$%"; // recordSeparator.data();
//	CTokenizer Tokenizer(separator);
//	long tokenLength;
//	OrsChar *tokenPtr;
	CString *s;

	//	CString record;
	CString pKey;


	clearRecord();
	if(!stringRecord->ReadLineWithPrefixedMaxSize(tbIn))
		return false;
	char primaryKey [10+1];
	primaryKey[10] = 0;
	memcpy (primaryKey, stringRecord->Data()+13, 10);

	if (strcmp (primaryKey, key))
		return false;

//	Tokenizer = stringRecord->Data();
//
//	int i=0;
//	while(i < tbFields && Tokenizer.hasToken())
//	{
//		s = (CString*)fieldsVector->Entry(i);
//		tokenPtr = Tokenizer.GetToken (IS_NOT_QUOTED_TOKEN, separator, &tokenLength);
//		s->assign(tokenPtr, tokenLength); // Mettendo le due chiamate come argomento mi esegue per prima la seconda quando la lunghezza non [ ancora valorizzata
//
//		i++;
//	}
//	s->Strip(s->trailing, '\n');


	CTokenizer Tokenizer(stringRecord->Data(), "&$%");
	long tokenLength;
	OrsChar *tokenPtr;
	int i=0;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength)  && i < tbFields)
	{
		if(tokenLength)
			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
		i++;
	} // End while

	if (i)
		i--;
	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');







	return true;

} // End loadNextRecord




