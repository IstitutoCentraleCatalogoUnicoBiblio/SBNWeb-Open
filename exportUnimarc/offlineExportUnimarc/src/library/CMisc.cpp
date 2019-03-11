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
 * CMisc.cpp
 *
 *  Created on: 9-lug-2009
 *      Author: Arge
 */


#include "CMisc.h"
#include <stdio.h>
#include <stdlib.h>

static char *dateStartChar = "-[\\<?@&� pe";
static char *dateEndChar = ">]!?@&} ";

CMisc::CMisc() {

}

CMisc::~CMisc() {
}

bool CMisc::IsEmpty(char *ptr) {
	//	return (UsedBytes-1)? OrsFALSE : OrsTRUE;
	//	char *ptr = Buffer;

	while (*ptr) {
		if (*ptr != ' ' && *ptr != '\t' && *ptr != '\n' && *ptr != '\r' && *ptr
				!= '\v')
			return false;
		ptr++;
	}
	return true;
} // End CString::IsEmpty

/*
 *	Y = year
 *	M = month
 *	D = day
 *	h = ora
 *	m = minuti
 *	s = secondi
 *
 *	eg:	"YYYYMMDD", "YYYY-MM-DD hh:mm:ss", "2008-11-01 00:00:00", destBuf
 */
void CMisc::formatDate(char *inputFormat, char *outputFormat, char *sourceDate,
		char *destDate) {

	char *ptrOutputFormat, *ptrSourceDate;
	bool started;

	while (*inputFormat) {
		if (*inputFormat == 'Y') {
			// cerchiamo l'anno
			started = false;
			ptrOutputFormat = outputFormat;
			ptrSourceDate = sourceDate;
			while (*ptrOutputFormat) {
				if (*ptrOutputFormat != 'Y') {
					if (started)
						break;
					ptrOutputFormat++;
					ptrSourceDate++;
					continue;
				}

				*destDate++ = *ptrSourceDate++;
				started = true;
			}
		} else if (*inputFormat == 'M') {
			// cerchiamo il mese
			started = false;
			ptrOutputFormat = outputFormat;
			ptrSourceDate = sourceDate;
			while (*ptrOutputFormat) {
				if (*ptrOutputFormat != 'M') {
					if (started)
						break;
					ptrOutputFormat++;
					ptrSourceDate++;
					continue;
				}
				*destDate++ = *ptrSourceDate++;
				started = true;
			}
		} else if (*inputFormat == 'D') {
			// cerchiamo il giorno
			started = false;
			ptrOutputFormat = outputFormat;
			ptrSourceDate = sourceDate;
			while (*ptrOutputFormat) {
				if (*ptrOutputFormat != 'D') {
					if (started)
						break;
					ptrOutputFormat++;
					ptrSourceDate++;
					continue;
				}
				*destDate++ = *ptrSourceDate++;
				started = true;
			}
		} else {

		}

	}

} // end CMisc::formatDate


/*
 * Output: YYYYMMDDHHMMSS.T
 */
void CMisc::formatDate3(struct tm * timeinfo, char *destDate) {

	sprintf(destDate, "%d", timeinfo->tm_year + 1900);
	destDate += 4;

	char tmpBuf[10];
	sprintf(tmpBuf, "%02d", timeinfo->tm_mon + 1);
//	memcpy(destDate, tmpBuf, 2);
	*((short*)destDate) = *((short*)tmpBuf); // 24/03/2015
	destDate += 2;
	sprintf(tmpBuf, "%02d", timeinfo->tm_mday);
//	memcpy(destDate, tmpBuf, 2);
	*((short*)destDate) = *((short*)tmpBuf); // 24/03/2015
	destDate += 2;
	sprintf(tmpBuf, "%02d", timeinfo->tm_hour);
//	memcpy(destDate, tmpBuf, 2);
	*((short*)destDate) = *((short*)tmpBuf); // 24/03/2015
	destDate += 2;
	sprintf(tmpBuf, "%02d", timeinfo->tm_min);
//	memcpy(destDate, tmpBuf, 2);
	*((short*)destDate) = *((short*)tmpBuf); // 24/03/2015
	destDate += 2;
	sprintf(tmpBuf, "%02d", timeinfo->tm_sec);
//	memcpy(destDate, tmpBuf, 2);
	*((short*)destDate) = *((short*)tmpBuf); // 24/03/2015
	destDate += 2;
	strcpy(destDate, ".0");

} // End format date3

/*
 * Output: YYYYMMDD
 */
void CMisc::formatDate4(struct tm * timeinfo, char *destDate) {

	sprintf(destDate, "%d", timeinfo->tm_year + 1900);
	destDate += 4;

	char tmpBuf[10];
	sprintf(tmpBuf, "%02d", timeinfo->tm_mon + 1);
//	memcpy(destDate, tmpBuf, 2);
	*((short*)destDate) = *((short*)tmpBuf); // 24/03/2015
	destDate += 2;
	sprintf(tmpBuf, "%02d", timeinfo->tm_mday);
//	memcpy(destDate, tmpBuf, 2);
	*((short*)destDate) = *((short*)tmpBuf); // 24/03/2015


} // End format date3


/*
 * Output: YYYYMMDD
 */
void CMisc::formatDate1(const char *sourceDate, char *destDate) {
//	memcpy(destDate, sourceDate, 4);
	*((int*)destDate) = *((int*)sourceDate); // 25/03/2015

	destDate += 4;

	const char *ptr = sourceDate + 4 + 1; //
	if (*(ptr + 1) == '-') // Mese con carattere singolo
	{
		*destDate++ = '0';
		*destDate++ = *ptr++;
		ptr++; // move past '-'
	} else {
//		memcpy(destDate, ptr, 2);
		*((short*)destDate) = *((short*)ptr); // 24/03/2015
		destDate += 2;
		ptr += 3;// move past '-'
	}
	if (*(ptr + 1) == ' ' || *(ptr + 1) == '.') // Giorno con carattere singolo
	{
		*destDate++ = '0';
		*destDate = *ptr;
	} else {
//		memcpy(destDate, ptr, 2);
		*((short*)destDate) = *((short*)ptr); // 24/03/2015
	}

}

/*
 * Output: YYYYMMDDHHMMSS.T
 *         20090401114737.1
 input:  2009-4-1.11.47. 37. 100000000


 *
 */
void CMisc::formatDate2(const char *sourceDate, char *destDate) {
	char *dest = destDate;
	//	printf ("\nformatDate2: dateIn:=%s, dateOut=%s", sourceDate, destDate);

	// Anno
	//memcpy(destDate, sourceDate, 4);
	*((int*)destDate) = *((int*)sourceDate); // 25/03/2015


	destDate += 4;

	// Mese
	const char *ptr = sourceDate + 4 + 1; //
	if (*(ptr + 1) == '-') // Mese con carattere singolo
	{
		*destDate++ = '0';
		*destDate++ = *ptr++;
		ptr++; // move past '-'
	} else {
		// memcpy(destDate, ptr, 2); // Mese con due caratteri
		*((short*)destDate) = *((short*)ptr); // 24/03/2015
		destDate += 2;
		ptr += 3;// move past '-'
	}

	// Giorno
	if (*(ptr + 1) == '.') // Giorno con carattere singolo
	{
		*destDate++ = '0';
		*destDate++ = *ptr;
		ptr += 2;
	} else {
//		memcpy(destDate, ptr, 2); // // Giorno con due caratteri
		*((short*)destDate) = *((short*)ptr); // 24/03/2015
		destDate += 2;
		ptr += 3;// move past '.'
	}
	// Ora
	if (*(ptr + 1) == '.') // Ora con carattere singolo
	{
		*destDate++ = '0';
		*destDate++ = *ptr;
		ptr += 2;
	} else {
//		memcpy(destDate, ptr, 2); // // Ora con due caratteri
		*((short*)destDate) = *((short*)ptr); // 24/03/2015
		destDate += 2;
		ptr += 3;// move past '.'
	}
	// Minuti
	if (*(ptr + 1) == '.') // Minuti con carattere singolo
	{
		*destDate++ = '0';
		*destDate++ = *ptr;
		//		ptr+=3;
		ptr += 2;// 12/11/2012
	} else {
//		memcpy(destDate, ptr, 2); // Minuti con due caratteri
		*((short*)destDate) = *((short*)ptr); // 24/03/2015
		destDate += 2;
		//		ptr+=4;// move past '. '
		ptr += 3;// move past '.' 12/11/2012
	}
	// Secondi
	if (*(ptr + 1) == '.') // secondi con carattere singolo
	{
		*destDate++ = '0';
		*destDate++ = *ptr;
		//		ptr+=3;
		// ptr += 2;// 12/11/2012
		ptr+=3; // 05/12/2012

	} else {
//		memcpy(destDate, ptr, 2); // Secondi con due caratteri
		*((short*)destDate) = *((short*)ptr); // 24/03/2015
		destDate += 2;
		//		ptr+=4;// move past '. '
		//ptr += 3;// move past '.' 12/11/2012

		ptr += 2;// move past 'nn' // 05/12/2012
	}

	// Millesimi. NB alcuni record vengono scaricati senza i millesimi se a zero 12/11/2012
	*destDate++ = '.';
	if (*ptr) // 12/11/2012
	{
		if (*ptr == '.' && *(ptr+1)) // 05/12/2012
			*destDate++ = *(ptr+1);	// 05/12/2012
		else
			*destDate++ = *ptr;
	}
	else
		*destDate++ = '0'; // 12/11/2012
	*destDate = 0; // EOS



} // End formatDate2


/*
 *
 1. Prefisso EAN - sono le prime tre cifre del codice ISBN, introdotte a partire dal 2007; indicano che si � in presenza di un libro.
 2. Gruppo linguistico - � l'identificativo del paese o dell'area linguistica dell'editore; pu� utilizzare da 1 a 5 cifre.
 3. Editore - � l'identificativo della casa editrice o del marchio editoriale; pu� utilizzare da 2 a 7 cifre.
 4. Titolo - � l'identificativo del libro; pu� utilizzare da 1 a 6 cifre.
 5. Carattere di controllo - � l'ultima cifra del codice ISBN (nei "vecchi" codici ISBN-10, oltre ai numeri da 0 a 9, si utilizzava anche il 10 romano, cio� la "X") e serve a verificare che il codice non sia stato letto o trascritto erroneamente (cosa che pu� sempre accadere, specialmente quando si usano strumenti automatici come i lettori di codici a barre).
 *
 */

void CMisc::gestisciTrattiniNumStandardIsbn(CString *numeroStdIn,
		CString *numeroStdOut) {
	//char numero[10+3+4+1 + 10]; // max 5 aree con 5 separatori 10 per evirtare eventuali buffer overflow del piffero
	CString numero;

	//  char *numeroPtr = numero;
	//int numeroIdx=0;
	char *numeroStdInPtr = numeroStdIn->data();
	char pd[2 + 1];

	if (numeroStdIn->Length() == 13 && (numeroStdIn->StartsWith("978")
			|| numeroStdIn->StartsWith("979"))) {
		//        numero[numeroIdx++] = *numeroStdInPtr++; // Prendi l'EAN
		//        numero[numeroIdx++] = *numeroStdInPtr++;
		//        numero[numeroIdx++] = *numeroStdInPtr++;
		//        numero[numeroIdx++] = '-';

		numero.AppendChar(*numeroStdInPtr++);
		numero.AppendChar(*numeroStdInPtr++);
		numero.AppendChar(*numeroStdInPtr++);
		numero.AppendChar('-');
	}

	else if (numeroStdIn->Length() > 10) // numero_std del polo e' 13 mentre quello di indice e' 10
		if (numeroStdIn->GetLastChar() == ' ')
			numeroStdIn->CropRightFrom(10);

	if (*numeroStdInPtr != '8' || *(numeroStdInPtr + 1) != '8') {
		//        if (numero[numeroIdx] == '-')
		//            numeroIdx--; // rimuoviamolo
		//        strcpy(&numero[numeroIdx], numeroStdInPtr);
		//        numeroStdOut->assign(numero);

		if (numero.GetLastChar() == '-')
			numero.ExtractLastChar();
		numero.AppendString(numeroStdInPtr);

		numeroStdOut->assign(&numero); // .data(), numero.Length()
		return;
	}

	//    numero[numeroIdx++] = *numeroStdInPtr++; // Identificativo paese
	//    numero[numeroIdx++] = *numeroStdInPtr++;
	numero.AppendChar(*numeroStdInPtr++);
	numero.AppendChar(*numeroStdInPtr++);

	//  numero[numeroIdx++] = '-';

	pd[0] = *numeroStdInPtr; //
	pd[1] = *(numeroStdInPtr + 1);
	pd[2] = 0;

	int primiDue = atoi(pd);
	int len = strlen(numeroStdInPtr);
	if (len >= 7)
		len = 7;

	int diff = 0;
	//   if (!primiDue)	// Mantis 4870 21/02/2010
	//   {
	//       // dovremmo controllare il codi di errore
	//       numero.AppendChar('-');
	//       numero.AppendString(numeroStdInPtr);
	//       numeroStdOut->assign(&numero); // .data(), numero.Length()
	//       return;
	//   }

	if (primiDue < 20) // Gruppo A - Range 00/19
	{
		diff = 2;
	}
	//   else if (primiDue < 70) {
	else if (primiDue < 60) { // Gruppo B - Range 200/599.  19/11/2012 Mantis 4667
		diff = 3;
	} else if (primiDue < 85) { // Gruppo C - Range 6.000/8.499
		diff = 4;
	} else if (primiDue < 90) { // Gruppo D - Range 85.000/89.999
		diff = 5;
	} else if (primiDue == 90) { // Gruppo E - Range 900.000/909999
		diff = 6;
	} else if (primiDue < 93) { // Gruppo B - Range 910/929  19/11/2012
		diff = 3;
	} else { // Gruppo D - Range 95.000/99.999
		diff = 5;
	}

	//   numero[numeroIdx++] = '-';
	numero.AppendChar('-');
	for (int i = 0; i < diff; i++)
		//       numero[numeroIdx++] = *numeroStdInPtr++;
		//	   numero[numeroIdx++] = '-';
		numero.AppendChar(*numeroStdInPtr++);
	numero.AppendChar('-');

	for (int i = 0; i < (len - diff); i++) //
	//       numero[numeroIdx++] = *numeroStdInPtr++;
		numero.AppendChar(*numeroStdInPtr++);

	if (*numeroStdInPtr)
		//       numero[numeroIdx++] = '-';
		numero.AppendChar('-');
	while (*numeroStdInPtr)
		//       numero[numeroIdx++] = *numeroStdInPtr++;
		numero.AppendChar(*numeroStdInPtr++);
	//   numero[numeroIdx] = 0;
	//   numeroStdOut->assign(numero);
	numeroStdOut->assign(&numero);
} // End gestisciTrattiniNumStandard


bool CMisc::isDate(char *stringPtr) {
	return isDate(stringPtr, strlen(stringPtr));
}

bool CMisc::isDate(char *stringPtr, int len) {
	if (!len)
		return false;

	if (strchr(dateStartChar, *stringPtr)) {
		stringPtr++;
		len--;
	} else if ((unsigned char) *stringPtr == 0xC2
			&& (unsigned char) *(stringPtr + 1) == 0xA9) // '�'
	{
		stringPtr += 2;
		len -= 2;
	}
	CString s(stringPtr, len);
	//s = stringPtr;
	s.ToLower();

	if (strchr(dateEndChar, s.GetLastChar()))
		s.ExtractLastChar();

	if (s.StartsWith("sec.") || s.EndsWith("sec.")) // secolo
		return true;
	else if (s.StartsWith("ca.") || s.EndsWith(" ca.")) // circa
		return true;
//	else if (s.StartsWith("m.") || s.EndsWith(" m.")) // morto NO 17/11/15. Potrebbe essere una m puntata nel testo
//		return true;
//	else if (s.StartsWith("n. ") || s.EndsWith(" n.")) // nato NO 17/11/15. Potrebbe anche essere numero civico
//		return true;

	else if (s.StartsWith("fl.") || s.EndsWith(" fl."))// floruit (fiori')
		return true;
	else if (s.StartsWith("a.a.") || s.EndsWith(" a.a.")) // anno accademico
		return true;
	else if (s.StartsWith("nd-") || s.EndsWith(" nd-")) // non datati
		return true;
	else if (s.StartsWith("s.d.") || s.EndsWith(" s.d.")) // circa
		return true;
	else if (s.StartsWith("ann.") || s.EndsWith(" ann.")) //
		return true;
	else if (s.StartsWith("anno")) //
		return true;

	if (isdigit(*stringPtr) && isdigit(*(stringPtr + 1))

	&& (isdigit(*(stringPtr + 2)) || *(stringPtr + 2) == '.') && (isdigit(*(stringPtr + 3)) || *(stringPtr + 3) == '.'))
		return true;

	// eg. <nn/nn>
	if (isdigit(*stringPtr) && isdigit(*(stringPtr + 1)) && *(stringPtr + 2) == '/' && isdigit( *(stringPtr + 3)) && isdigit(*(stringPtr + 4)))
		return true;
	// eg. <18/t-1825p>
	if (isdigit(*stringPtr) && isdigit(*(stringPtr + 1)) && *(stringPtr + 2) == '/'
			&& (*(stringPtr + 3) == 't' || *(stringPtr + 3) == 'i' || *(stringPtr + 3)
			== 'f' || *(stringPtr + 3) == 'm' || *(stringPtr + 3) == 'p' || *(stringPtr + 3) == 's'	))
		return true;

	if (s.Length() >= 4) {
		// Finisce con un anno?
		stringPtr = s.data() + s.Length() - 1;
		if (isdigit(*stringPtr)
				&& isdigit(*(stringPtr - 1))
				&& (isdigit(*(stringPtr - 2)) || *(stringPtr - 2) == '.')
				&& (isdigit(*(stringPtr - 3)) || *(stringPtr - 3) == '.'))
			return true;
	}

	if (s.Length() >= 5) {
		// finisce con <nn/nn>
		stringPtr = s.data() + s.Length() - 1;
		if (isdigit(*stringPtr)
				&& isdigit(*(stringPtr - 1)) && *(stringPtr - 2) == '/'
				&& isdigit(*(stringPtr - 3)) && isdigit(*(stringPtr - 4)))
			return true;
	}


	// 16/11/2015 Troviamo una data di 4 cifre all'interno di una stringa
	char *ptr = s.data();
	while (*ptr)
	{
		if (!isdigit(*ptr))
		{
			ptr ++;
			continue;
		}
		// Trovato 1mo digit
		ptr++;
		if (!*ptr)
			return false;
		if ( !isdigit(*ptr))
			{ptr++; continue;}
		// Trovato 2do digit

		ptr++;
		if (!*ptr)
			return false;
		if ( !isdigit(*ptr) && *ptr != '.')
			{ptr++; continue;}
		// Trovato 3zo digit

		ptr++;
		if (!*ptr)
			return false;
		if ( !isdigit(*ptr) && *ptr != '.')
			{ptr++; continue;}
		// Trovato 4to digit

		return true;
	}

	return false;
} // End isDate
