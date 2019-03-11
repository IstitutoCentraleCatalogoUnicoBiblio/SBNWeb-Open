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
/**
*  Author: Argentino Trombin.
*  Date:   Papozze 30/12/98
*  Module: CTokenizer.h
*  Desc:
*
**/

#ifndef __CTOKENIZER_H
#define __CTOKENIZER_H

#include "ors/types.h"
#include "library/CString.h"
#include "library/CBaseClass.h"

#define IS_NOT_QUOTED_TOKEN 0
#define CAN_BE_QUOTED_TOKEN 1 // May or May Not be quoted

//static char *DefSepSequence = " \t\r\n,.";

class CTokenizer : CBaseClass
	{
	OrsInt	LineFeeds;
	int sepSequenceLength;

	public:
	OrsChar *StringStartPtr, *Ptr1, *Ptr2, *SeparatorSequence;
	CString Token;
	bool QuotedToken;

	// Virtual function redefinition from CBaseClass
	//==============================================
	virtual OrsInt ClassId()		{return CTokenizerID;};
	virtual const OrsChar* ClassName()	{return "CTokenizerID";};

	CTokenizer();
	CTokenizer(const OrsChar *aSeparatorSequence);
	CTokenizer(const OrsChar *aString, const OrsChar *aSeparatorSequence);
	virtual ~CTokenizer();

	void Assign(const OrsChar *aString);
	void Assign(const OrsChar *aString, const OrsChar *aSeparatorSequence);

	OrsChar	* GetToken(); // Not Quoted
	OrsChar	* GetToken(OrsBool Quoted);
	OrsChar	* GetToken(const OrsChar *aSeparatorSequence);
	OrsChar	* GetToken(	OrsBool CanBeQuoted, // Default not quoted
			const OrsChar *aSeparatorSequence); // Default separator sequence

	//OrsChar	* GetToken(	OrsBool CanBeQuoted, const OrsChar *aSeparatorSequence, long *tokenLength);
	bool GetToken( OrsChar **foundToken, long *tokenLength);


	long 	GetTokenLength();
	void setQuotedToken(bool caBeQuoted);

	OrsInt GetLineFeeds();
	void Reset();

	OrsChar	*operator()(); // GetToken
	OrsChar	*operator()(const OrsChar *aSeparatorSequence); // GetToken
	void operator =	(const OrsChar *aString);

	bool hasToken();

	}; // End CTokenizer

#endif //__CTOKENIZER_H

