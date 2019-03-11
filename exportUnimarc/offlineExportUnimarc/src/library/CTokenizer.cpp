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
*  Module: CTokenizer.cpp
*  Desc:
*
**/
#include "library/CTokenizer.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


CTokenizer::~CTokenizer()
	{
	} // End CTokenizer

void CTokenizer::Reset()
	{
	Ptr1=Ptr2=StringStartPtr;
	//LineFeeds;
	Token.Clear();
	} // End CTokenizer::Reset


OrsInt CTokenizer::GetLineFeeds()
	{
	return LineFeeds;
	} // End CTokenizer::GetLineFeeds


OrsChar *CTokenizer::operator () ()
	{
	return GetToken (IS_NOT_QUOTED_TOKEN, SeparatorSequence);
	} // End CTokenizer::operator ()


OrsChar	*CTokenizer::operator () (const OrsChar *aSeparatorSequence)
	{
	return GetToken(IS_NOT_QUOTED_TOKEN , aSeparatorSequence);
	} // End CTokenizer::operator ()


void CTokenizer::Assign(const OrsChar *aString)
	{
	StringStartPtr =Ptr1 =Ptr2 = (OrsChar *)aString;
	} // End CTokenizer::Assign

void CTokenizer::Assign(const OrsChar *aString, const OrsChar *aSeparatorSequence)
	{
	StringStartPtr =Ptr1 =Ptr2 = (OrsChar *)aString;
	SeparatorSequence = (OrsChar *)aSeparatorSequence;
	} // End CTokenizer::Assign


OrsChar	* CTokenizer::GetToken()
	{
	return GetToken (IS_NOT_QUOTED_TOKEN, SeparatorSequence);
	} // End CTokenizer::GetToken


OrsChar	* CTokenizer::GetToken(OrsBool Quoted)
	{
	return GetToken (Quoted, SeparatorSequence);
	} // End CTokenizer::GetToken


OrsChar	* CTokenizer::GetToken(const OrsChar *aSeparatorSequence)
	{
	return GetToken (IS_NOT_QUOTED_TOKEN, aSeparatorSequence);
	} // End CTokenizer::GetToken



void CTokenizer::operator = (const OrsChar *aString)
	{
	StringStartPtr =Ptr1 =Ptr2 = (OrsChar *)aString;
	QuotedToken = false;
	} // End CTokenizer::operator =


CTokenizer::CTokenizer()
	{
	LineFeeds=0;
	StringStartPtr=Ptr1=Ptr2=OrsNULL;
	SeparatorSequence = OrsNULL;
	QuotedToken = false;
	sepSequenceLength = 0;

	} // End CTokenizer::CTokenizer

CTokenizer::CTokenizer(const OrsChar *aSeparatorSequence)
	{
	LineFeeds=0;
	StringStartPtr=Ptr1=Ptr2=OrsNULL;
	SeparatorSequence = (OrsChar *)aSeparatorSequence;
	sepSequenceLength = strlen((OrsChar *)aSeparatorSequence);

	QuotedToken = false;
	} // End CTokenizer::CTokenizer

CTokenizer::CTokenizer(const OrsChar *aString, const OrsChar *aSeparatorSequence)
	{
	LineFeeds=0;
	StringStartPtr=Ptr1=Ptr2 = (OrsChar *)aString;
	SeparatorSequence = (OrsChar *)aSeparatorSequence;
	sepSequenceLength = strlen((OrsChar *)aSeparatorSequence);
	QuotedToken = false;
	} // End CTokenizer::CTokenizer


OrsChar	* CTokenizer::GetToken(	OrsBool CanBeQuoted, const OrsChar *aSeparatorSequence)
	{
	const OrsChar *SepPtr; // chr,
	int sepSequenceLength = strlen(aSeparatorSequence);

	OrsBool QuotedToken = OrsFALSE;
	SeparatorSequence = (OrsChar *)aSeparatorSequence;


	// End of tokenization?
	if	(!*Ptr2)
		return (OrsChar *) "";

	if	(CanBeQuoted && (*Ptr2=='"' || *Ptr2=='\''))
		{
		QuotedToken=OrsTRUE;
		Ptr1++;
		Ptr2++; // Skip Quotes
		}
	// Move past the token
	while (*Ptr2)
		{
		if	(QuotedToken)
			{
			if	( *Ptr2 == '"' || *Ptr2 == '\'')
				{
				// Store substring
				Token.Equals(Ptr1, Ptr2-Ptr1);
				Ptr1 = *Ptr2 ? ++Ptr2 : Ptr2;
				return (Token.Data());
				}
			}
		else
			{
			SepPtr = SeparatorSequence;
			while (true) // *SepPtr
				{
				if (!*SepPtr)
					{
						// Store substring
						Token.Equals(Ptr1, Ptr2-sepSequenceLength-Ptr1);
						Ptr1 = Ptr2; // *Ptr2 ? ++Ptr2 :
						return (Token.Data());
					}
				else if	((*Ptr2 != *SepPtr) || !*Ptr2)
					break;
				SepPtr++;
				Ptr2++;
				} // End while looking for a separator
			}
		// If last character is not a separator
		if	(!*Ptr2)
			break;
		Ptr2++;
		} // End while moving past token

	// Return the last substring
	Token = Ptr1;
	Ptr1 = Ptr2; // *Ptr2 ? ++Ptr2 :
	return (Token.Data());
	} // End CTokenizer::GetToken()


long CTokenizer::GetTokenLength()
{
	return Token.Length();
}

bool CTokenizer::hasToken()
{
	if	(!*Ptr2)
		return false;
	return true;
}



void CTokenizer::setQuotedToken(bool canBeQuoted)
{
	QuotedToken = canBeQuoted;
}


bool CTokenizer::GetToken( OrsChar **foundToken, long *tokenLength)
	{
	const OrsChar *SepPtr; // chr,
	//int sepSequenceLength = strlen(aSeparatorSequence);
	//OrsBool QuotedToken = OrsFALSE;
	//SeparatorSequence = aSeparatorSequence;
	// End of tokenization?
	if	(!*Ptr2)
	{
		*foundToken = "";
		*tokenLength = 0;
		return false;
	}
	if	(QuotedToken && (*Ptr2=='"' || *Ptr2=='\''))
		{
		//QuotedToken=OrsTRUE;
		Ptr1++;
		Ptr2++; // Skip Quotes
		}
	// Move past the token
	while (*Ptr2)
		{
		if	(QuotedToken)
			{
			if	( *Ptr2 == '"' || *Ptr2 == '\'')
				{
				// Store substring
//				Token.Equals(Ptr1, Ptr2-Ptr1);
//				Ptr1 = *Ptr2 ? ++Ptr2 : Ptr2;
//				*tokenLength = Token.Length();
//				return (Token.Data());
				*foundToken = Ptr1;
				*tokenLength = Ptr2-Ptr1;
				Ptr1 = *Ptr2 ? ++Ptr2 : Ptr2;
				return true;

				}
			}
		else
			{
			SepPtr = SeparatorSequence;
			while (true) // *SepPtr
				{
				if (!*SepPtr)
					{
						// Store substring
//						Token.Equals(Ptr1, Ptr2-sepSequenceLength-Ptr1);
//						Ptr1 = Ptr2; // *Ptr2 ? ++Ptr2 :
//						*tokenLength = Token.Length();
//						return (Token.Data());
						*foundToken = Ptr1;
						*tokenLength = Ptr2-sepSequenceLength-Ptr1;
						Ptr1 = Ptr2;
						return true;

					}
				else if	((*Ptr2 != *SepPtr) || !*Ptr2)
				{
					if (SepPtr != SeparatorSequence) // Mantis 0005343
					{
						SepPtr = SeparatorSequence;
						continue;
					}
					else
						break;
				}
				SepPtr++;
				Ptr2++;
				} // End while looking for a separator
			}
		// If last character is not a separator
		if	(!*Ptr2)
			break;
		Ptr2++;
		} // End while moving past token

	// Return the last substring
	//Token = Ptr1;

//	long len = Ptr2-Ptr1;
//	Token.Equals(Ptr1, len);
//	Ptr1 = Ptr2; // *Ptr2 ? ++Ptr2 :
//	*tokenLength = len; //Token.Length();
//	return (Token.Data());

	*foundToken = Ptr1;
	*tokenLength = Ptr2-Ptr1;

	Ptr1 = Ptr2;
	return true;

	} // End CTokenizer::GetToken(OrsChar **foundToken, long *tokenLength)







