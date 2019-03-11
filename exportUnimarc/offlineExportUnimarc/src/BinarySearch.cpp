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
 * BinarySearch.cpp
 *
 *  Created on: 30-dic-2008
 *      Author: Arge
 */

#include "BinarySearch.h"

BinarySearch::BinarySearch() {
	debug = false;
}

BinarySearch::~BinarySearch() {
}

long long	binarySearchMemoryCtr = 0;
long long 	binarySearchFileCtr = 0;
long long	binarySearchFileCtrSeekTo = 0;

/*
 * Metodo che permette la ricerca in array di caratteri composti da chiavi o chiavi + offset a lunghezza fissa
 * Ritorna true tre trova chiave e posizione della chiave all'interno dell'array.
 */
bool BinarySearch::search(char* offsetMapPtr, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, char ** entryPtr)
{
	long low = 0, high = elements-1, midpoint = 0;
	char *midpointPtr;
	int ret;

binarySearchMemoryCtr++; // 04/02/2015

	while (low <= high)
	{
		midpoint = (low + high) >> 1; // bitwise divide by two
		midpointPtr = offsetMapPtr + (midpoint * rowLength);

//		printf ("\nhigh=%d, midpoint=%d, low=%d, key=%s, midpointPtr=%s", high, midpoint, low, keyToSearch, midpointPtr);

		ret = strncmp (keyToSearch, midpointPtr, keyLength);
		if (!ret)
			{
			position = midpoint;
			*entryPtr = midpointPtr;
			return true;
			}
		else if (ret < 0)
			high = midpoint - 1; // Cerca nella parte bassa
		else
			low = midpoint + 1; // Cerca nella parte alta
	}
	position = -1; // NOT FOUND
	return false;
} // End binarySearch


bool BinarySearch::search(CFile* offsetMapFile, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, CString* entryFile)
{
	long low = 0, high = elements-1, midpoint = 0;
	//char *midpointPtr;
	long offset;
	int ret;

	binarySearchFileCtr++; // 04/02/2015

//	CString s;
//printf ("\nBinarySearch::search offsetMapFile=%s elements=%ld", offsetMapFile->GetName(), elements);
//printf ("\nBinarySearch::search low=%ld high=%ld", low, high);
	while (low <= high)
	{
		midpoint = (low + high) >> 1; // bitwise divide by two

		// Get positioned in file
		offset=midpoint * rowLength;
		ret = offsetMapFile->SeekTo(offset);
binarySearchFileCtrSeekTo++;

//		midpointPtr = offsetMapPtr + (midpoint * rowLength);
		// read line
//		entryFile->ReadLine(offsetMapFile);
		entryFile->ReadBytes(offsetMapFile, rowLength); // 06/02/2015


//printf ("\nhigh=%d, midpoint=%d, low=%d, key='%s' midpointPtr='%s'", high, midpoint, low, keyToSearch, entryFile->data());

		ret = strncmp (keyToSearch, entryFile->data(), keyLength);
		if (!ret)
			{
			position = midpoint;
//			*entryPtr = midpointPtr;
			return true;
			}
		else if (ret < 0)
			high = midpoint - 1; // Cerca nella parte bassa
		else
			low = midpoint + 1; // Cerca nella parte alta
	}
	position = -1; // NOT FOUND
	return false;
} // End binarySearch

void BinarySearch::setDebug() {
 debug = true;
}

void BinarySearch::unsetDebug() {
	 debug = false;
}





bool BinarySearch::nonStatic_search(char* offsetMapPtr, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, char ** entryPtr)
{
	long low = 0, high = elements-1, midpoint = 0;
	char *midpointPtr;
	int ret;

	while (low <= high)
	{
		midpoint = (low + high) >> 1; // bitwise divide by two
		midpointPtr = offsetMapPtr + (midpoint * rowLength);

		if (debug)
			printf ("\nhigh=%d, midpoint=%d, low=%d, key=%s, midpointPtr=%.21s", high, midpoint, low, keyToSearch, midpointPtr);

		ret = strncmp (keyToSearch, midpointPtr, keyLength);
		if (!ret)
			{
			position = midpoint;
			*entryPtr = midpointPtr;
			if (debug)
				printf("\nTrovata");
			return true;
			}
		else if (ret < 0)
			high = midpoint - 1; // Cerca nella parte bassa
		else
			low = midpoint + 1; // Cerca nella parte alta
	}
	position = -1; // NOT FOUND
	if (debug)
		printf("\nNON Trovata");
	return false;
} // End binarySearch


bool BinarySearch::nonStatic_search(CFile* offsetMapFile, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, CString* entryFile)
{
	long low = 0, high = elements-1, midpoint = 0;
	//char *midpointPtr;
	int ret;
//	CString s;

	while (low <= high)
	{
		midpoint = (low + high) >> 1; // bitwise divide by two

		// Get positioned in file
		offsetMapFile->SeekTo(midpoint * rowLength);
//		midpointPtr = offsetMapPtr + (midpoint * rowLength);
		// read line
		entryFile->ReadLine(offsetMapFile);

		if (debug)
			printf ("\nhigh=%d, midpoint=%ld, low=%ld, key=%s, midpointPtr=%.21s", high, midpoint, low, keyToSearch, entryFile->data());

		ret = strncmp (keyToSearch, entryFile->data(), keyLength);
		if (!ret)
			{
			position = midpoint;
//			*entryPtr = midpointPtr;
			if (debug)
				printf("\nTrovata");
			return true;
			}
		else if (ret < 0)
			high = midpoint - 1; // Cerca nella parte bassa
		else
			low = midpoint + 1; // Cerca nella parte alta
	}
	position = -1; // NOT FOUND
	if (debug)
		printf("\nNON Trovata");
	return false;
} // End binarySearch






