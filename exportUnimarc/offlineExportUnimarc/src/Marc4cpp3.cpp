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
 * Marc4cpp2.cpp
 *
 *  Created on: 16-lug-2009
 *      Author: Arge
 */


#include <stdio.h>
#include <stdlib.h>
#include <iostream>

#include "Marc4cpp.h"
#include "library/CTokenizer.h"
#include "string"
#include "BinarySearch.h"
#include "library/CFile.h"
#include "library/GenericError.h"
#include "TbfBibliotecaInPolo.h"
#include "library/Cini.h"
#include <malloc.h>
#include "TbcSezioneCollocazione.h"
#include "MarcGlobals.h"

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif
extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



bool  Marc4cpp::loadTbfBibliotecaInPoloRecords(char *tbfBibliotecaInPoloFilename, char *polo)
{
	CFile* tbfBibliotecaInPoloIn;
	tbfBibliotecaInPoloIn = new CFile(tbfBibliotecaInPoloFilename, AL_READ_FILE);
	if (!tbfBibliotecaInPoloIn)
	{
	    SignalAnError(__FILE__, __LINE__, "Cannot open %s", tbfBibliotecaInPoloFilename);
		return false;
	}
	CString *recordPtr = new CString (2048);


	while (recordPtr->ReadLineWithPrefixedMaxSize(tbfBibliotecaInPoloIn))
	{
		TbfBibliotecaInPolo * tbfBibliotecaInPolo = new TbfBibliotecaInPolo();
		tbfBibliotecaInPolo->loadRecordFromString( recordPtr->data(), recordPtr->Length());
//printf ("\nBiblio key = '%s'", tbfBibliotecaInPolo->getField(tbfBibliotecaInPolo->cd_biblioteca));
		tbfBibliotecaInPoloKV->Add(tbfBibliotecaInPolo->getField(tbfBibliotecaInPolo->cd_biblioteca), tbfBibliotecaInPolo); // cdBib.data()
	} // end while
		// Gia' ordinato per chiave biblioteca
	delete tbfBibliotecaInPoloIn;
	delete recordPtr;
	return true;
} // End loadTbfBibliotecaRecords







void Marc4cpp::loadOffsetFiles2(CFile *fileOffsetIn, char *ptr)
{
	bool retb = true;
	int ctr = 0;

	// Per gestire info di sistema
	fileOffsetInMemSize += fileOffsetIn->CurOffset(); // Diamo per scontato di essere a fine file

	fileOffsetIn->SeekToBegin();
	while (retb)
	{
		retb = fileOffsetIn->Read(ptr, READ_BLOCK_SIZE);
		ptr+=READ_BLOCK_SIZE;
		ctr++;
//		if (!(ctr & 0x3ff))
//			printf ("\nLetti %ld ca bytes", (long)ctr*READ_BLOCK_SIZE);
	}

//	printf ("\nLetti %ld bytes ca", (long)ctr*READ_BLOCK_SIZE);

} // End loadOffsetFiles2





bool Marc4cpp::apriFileRelazioni(ATTValVector<CString *> *relazioniVector)
{
	char * tabella, *nomeFile, *loadInMemory;
	CFile* cFileIn;
	CString *sPtr;
	cIni ini;
//	CFileInMemory* cFileInMemory;


	for (int i=0; i < relazioniVector->Length(); i++)
	{
		sPtr = relazioniVector->Entry(i);
		ini.SplitIniFields(sPtr->data());
//		ini.SplitIniFields(sPtr->data(), true); // 11/03/2016

		tabella = ini.fieldsVector->Entry(1)->data();
		nomeFile = ini.fieldsVector->Entry(2)->data();
		loadInMemory = ini.fieldsVector->Entry(3)->data();

printf ("\nAPRI FILE RELAZIONI: %s", sPtr->data());

		if (*loadInMemory =='S' || *loadInMemory =='s')
		{
			cFileIn = new CFileInMemory(nomeFile, AL_READ_FILE);

			fileRelazioniInMemSize += ((CFileInMemory*)cFileIn)->getFileSize();

			fileRelazioniInMemVector.Add(cFileIn);
		}
		else
		{
			cFileIn = new CFile(nomeFile, AL_READ_FILE);
			fileOnDiscVector.Add(cFileIn);
		}


		if (!strcmp(tabella, "tr_tit_tit_rel"))
			trTitTitRelIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_tit_inv_rel"))
			trTitTitInvRelIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_aut_rel"))
			trTitAutRelIn = cFileIn;
		else if (!strcmp(tabella, "tr_aut_aut_rel"))
			trAutAutRelIn = cFileIn;
		else if (!strcmp(tabella, "tr_aut_aut_rel_inv"))
			trAutAutRelInvIn = cFileIn;

else if (!strcmp(tabella, "tr_tit_aut_rel_inv"))
	trTitAutRelInvIn = cFileIn;

		else if (!strcmp(tabella, "tr_tit_sog_bib"))
			trTitSogBibIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_cla"))
			trTitClaIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_mar"))
			trTitMarRelIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_luo"))
			trTitLuoIn = cFileIn;



		else if (!strcmp(tabella, "trs_termini_titoli_biblioteche_rel"))
			trsTerminiTitoliBibliotecheRelIn= cFileIn;

		else
		{
		    SignalAnError(__FILE__, __LINE__, "Relazione %s non identificata", tabella);
			return false;
		}
	}
	return true;
} // apriFileRelazioni



bool Marc4cpp::loadOffsetFiles()
{
	long fileSize, elements; // ,offset

	// Carichiamo gli offset dei titoli dell'opera 31/03/2016
	if (tbTitSet2OffsetIn)
	{
		tbTitSet2OffsetIn->SeekToEnd();
		fileSize = tbTitSet2OffsetIn->CurOffset();
		elements = elementsTbTitSet2 = fileSize/ titset1KeyPlusOffsetPlusLfLength;
		if (tbTitSet2OffsetIn && (offsetFileVectorInMem.FindByValue(tbTitSet2OffsetIn) != -1))
		{
			printf ("\n\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbTitSet2OffsetIn->GetName(), titset2KeyPlusOffsetPlusLfLength, elements);
			offsetBufferTbTitSet2Ptr = (char *)malloc(elements*(titset2KeyPlusOffsetPlusLfLength));
			if (!offsetBufferTbTitSet2Ptr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbTitSet2OffsetIn, offsetBufferTbTitSet2Ptr);
		}
	}



	// Carichiamo gli offset dei dati comuni (area 0) 09/03/2015
	if (tbTitSet1OffsetIn)
	{
		tbTitSet1OffsetIn->SeekToEnd();
		fileSize = tbTitSet1OffsetIn->CurOffset();
		elements = elementsTbTitSet1 = fileSize/ titset1KeyPlusOffsetPlusLfLength;
		if (tbTitSet1OffsetIn && (offsetFileVectorInMem.FindByValue(tbTitSet1OffsetIn) != -1))
		{
			printf ("\n\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbTitSet1OffsetIn->GetName(), titset1KeyPlusOffsetPlusLfLength, elements);
			offsetBufferTbTitSet1Ptr = (char *)malloc(elements*(titset1KeyPlusOffsetPlusLfLength));
			if (!offsetBufferTbTitSet1Ptr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbTitSet1OffsetIn, offsetBufferTbTitSet1Ptr);
		}
	}


	// Carichiamo gli offset del disco sonoro 19/02/2015
	if (tbDiscoSonoroOffsetIn)
	{
		tbDiscoSonoroOffsetIn->SeekToEnd();
		fileSize = tbDiscoSonoroOffsetIn->CurOffset();
		elements = elementsTbDiscoSonoro = fileSize/keyPlusOffsetPlusLfLength;
		if (tbDiscoSonoroOffsetIn && (offsetFileVectorInMem.FindByValue(tbDiscoSonoroOffsetIn) != -1))
		{
			printf ("\n\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbDiscoSonoroOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbDiscoSonoroPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbDiscoSonoroPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbDiscoSonoroOffsetIn, offsetBufferTbDiscoSonoroPtr);
		}
	}


	// Carichiamo gli offset dell'audiovideo 12/02/2015
	if (tbAudiovideoOffsetIn)
	{
		tbAudiovideoOffsetIn->SeekToEnd();
		fileSize = tbAudiovideoOffsetIn->CurOffset();
		elements = elementsTbAudiovideo = fileSize/keyPlusOffsetPlusLfLength;
		if (tbParolaOffsetIn && (offsetFileVectorInMem.FindByValue(tbAudiovideoOffsetIn) != -1))
		{
			printf ("\n\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbAudiovideoOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbAudiovideoPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbAudiovideoPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbAudiovideoOffsetIn, offsetBufferTbAudiovideoPtr);
		}
	}


	// Carichiamo gli offset delle parole
	if (tbParolaOffsetIn)
	{
		tbParolaOffsetIn->SeekToEnd();
		fileSize = tbParolaOffsetIn->CurOffset();
		elements = elementsTbParola = fileSize/keyPlusOffsetPlusLfLength;
		if (tbParolaOffsetIn && (offsetFileVectorInMem.FindByValue(tbParolaOffsetIn) != -1))
		{
			printf ("\n\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbParolaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbParolaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbParolaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbParolaOffsetIn, offsetBufferTbParolaPtr);
		}
	}

	// Carichiamo gli offset dei link al digitale
	if (tsLinkMultimOffsetIn)
	{
		tsLinkMultimOffsetIn->SeekToEnd();
		fileSize = tsLinkMultimOffsetIn->CurOffset();
		elements = elementsTsLinkMultim = fileSize/keyPlusOffsetPlusLfLength;
		if (tsLinkMultimOffsetIn && (offsetFileVectorInMem.FindByValue(tsLinkMultimOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tsLinkMultimOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTsLinkMultimPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTsLinkMultimPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tsLinkMultimOffsetIn, offsetBufferTsLinkMultimPtr);
		}
	}

	// Carichiamo gli offset dei link al web (tag 321 temporaneo)
	if (tsLinkWebOffsetIn)
	{
		tsLinkWebOffsetIn->SeekToEnd();
		fileSize = tsLinkWebOffsetIn->CurOffset();
		elements = elementsTsLinkWeb = fileSize/keyPlusOffsetPlusLfLength;
		if (tsLinkWebOffsetIn && (offsetFileVectorInMem.FindByValue(tsLinkWebOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tsLinkWebOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTsLinkWebPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTsLinkWebPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tsLinkWebOffsetIn, offsetBufferTsLinkWebPtr);
		}
	}

	// Carichiamo gli offset del tesauro
	if (tbTermineTesauroOffsetIn)
	{
		tbTermineTesauroOffsetIn->SeekToEnd();
		fileSize = tbTermineTesauroOffsetIn->CurOffset();
		elements = elementsTbTermineTesauro = fileSize/keyPlusOffsetPlusLfLength;
		if (tbTermineTesauroOffsetIn && (offsetFileVectorInMem.FindByValue(tbTermineTesauroOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbTermineTesauroOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbTermineTesauroPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbTermineTesauroPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbTermineTesauroOffsetIn, offsetBufferTbTermineTesauroPtr);
		}
	}



	// Carichiamo gli offset delle sezioni di collocazione
	if (tbcSezioneCollocazioneOffsetIn)
	{
		tbcSezioneCollocazioneOffsetIn->SeekToEnd();
		fileSize = tbcSezioneCollocazioneOffsetIn->CurOffset();
		//int saveKeyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
		//keyPlusOffsetPlusLfLength = TBC_SEZIONE_COLLOCAZIONE_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		elements = elementsTbcSezioneCollocazione = fileSize/sezioneDiColocazioneKeyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbcSezioneCollocazioneOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbcSezioneCollocazioneOffsetIn->GetName(), sezioneDiColocazioneKeyPlusOffsetPlusLfLength, elements);

			offsetBufferTbcSezioneCollocazionePtr = (char *)malloc(elements*(sezioneDiColocazioneKeyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbcSezioneCollocazionePtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbcSezioneCollocazioneOffsetIn, offsetBufferTbcSezioneCollocazionePtr);
		}
		//keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;
	}


	// Carichiamo gli offset delle note
	if (tbNotaOffsetIn)
	{
		tbNotaOffsetIn->SeekToEnd();
		fileSize = tbNotaOffsetIn->CurOffset();
		elements = elementsTbNota = fileSize/keyPlusOffsetPlusLfLength;
		if (tbNotaOffsetIn && (offsetFileVectorInMem.FindByValue(tbNotaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbNotaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbNotaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbNotaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbNotaOffsetIn, offsetBufferTbNotaPtr);
		}
	}

	// Carichiamo gli offset delle note 300
	if (tbNota300OffsetIn)
	{
		tbNota300OffsetIn->SeekToEnd();
		fileSize = tbNota300OffsetIn->CurOffset();
		elements = elementsTbNota300 = fileSize/keyPlusOffsetPlusLfLength;
		if (tbNota300OffsetIn && (offsetFileVectorInMem.FindByValue(tbNota300OffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbNota300OffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbNota300Ptr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbNota300Ptr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbNota300OffsetIn, offsetBufferTbNota300Ptr);
		}
	}

	// Carichiamo gli offset delle note 321
	if (tbNota321OffsetIn)
	{
		tbNota321OffsetIn->SeekToEnd();
		fileSize = tbNota321OffsetIn->CurOffset();
		elements = elementsTbNota321 = fileSize/keyPlusOffsetPlusLfLength;
		if (tbNota321OffsetIn && (offsetFileVectorInMem.FindByValue(tbNota321OffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbNota321OffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbNota321Ptr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbNota321Ptr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbNota321OffsetIn, offsetBufferTbNota321Ptr);
		}
	}



	// Carichiamo gli offset dei titoli
	if (tbTitoloOffsetIn)
	{
		tbTitoloOffsetIn->SeekToEnd();
		fileSize = tbTitoloOffsetIn->CurOffset();
		elements = elementsTbTitolo = fileSize/titoloKeyPlusOffsetPlusLfLength;
		if (tbTitoloOffsetIn && (offsetFileVectorInMem.FindByValue(tbTitoloOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbTitoloOffsetIn->GetName(), titoloKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTbTitoloPtr = (char *)malloc(elements*(titoloKeyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbTitoloPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbTitoloOffsetIn, offsetBufferTbTitoloPtr);
		}
	}


	// Carichiamo gli offset delle biblioteche
	if (tbfBibliotecaOffsetIn )
	{
		tbfBibliotecaOffsetIn->SeekToEnd();
		fileSize = tbfBibliotecaOffsetIn->CurOffset();

		//int saveKeyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
		//keyPlusOffsetPlusLfLength = BIBLIOTECA_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		elements = elementsTbfBiblioteca = fileSize/bibliotecaKeyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(tbfBibliotecaOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbfBibliotecaOffsetIn->GetName(), bibliotecaKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTbfBibliotecaPtr = (char *)malloc(elements*(bibliotecaKeyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbfBibliotecaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbfBibliotecaOffsetIn, offsetBufferTbfBibliotecaPtr);
		}
		//keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;
	}

	// Carichiamo gli offset degli autori
	if (tbAutoreOffsetIn)
	{
		tbAutoreOffsetIn->SeekToEnd();
		fileSize = tbAutoreOffsetIn->CurOffset();
		elements = elementsTbAutore = fileSize/keyPlusOffsetPlusLfLength;
//		elements = elementsTbAutore = fileSize/(10+4); // 04/02/2015

		if (offsetFileVectorInMem.FindByValue(tbAutoreOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbAutoreOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbAutorePtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbAutorePtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;}

			loadOffsetFiles2(tbAutoreOffsetIn, offsetBufferTbAutorePtr);
		}

	}

	// Carichiamo gli offset dei numeri standard
	if (tbNumeroStandardOffsetIn )
	{
		tbNumeroStandardOffsetIn->SeekToEnd();
		fileSize = tbNumeroStandardOffsetIn->CurOffset();
		elements = elementsTbNumeroStandard = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbNumeroStandardOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbNumeroStandardOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbNumeroStandardPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbNumeroStandardPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbNumeroStandardOffsetIn, offsetBufferTbNumeroStandardPtr);
		}
	}

	// Carichiamo gli offset dei soggetti
	if (tbSoggettoOffsetIn)
	{
		tbSoggettoOffsetIn->SeekToEnd();
		fileSize = tbSoggettoOffsetIn->CurOffset();
		elements = elementsTbSoggetto = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbSoggettoOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbSoggettoOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbSoggettoPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbSoggettoPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbSoggettoOffsetIn, offsetBufferTbSoggettoPtr);
		}
	}

	// Carichiamo gli offset dei soggetti
	if (tbDescrittoreOffsetIn ) // 09/02/2015 && (offsetFileVectorInMem.FindByValue(tbDescrittoreOffsetIn) != -1)
	{
		tbDescrittoreOffsetIn->SeekToEnd();
		fileSize = tbDescrittoreOffsetIn->CurOffset();
		elements = elementsTbDescrittore = fileSize/keyPlusOffsetPlusLfLength;

		if (offsetFileVectorInMem.FindByValue(tbDescrittoreOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbDescrittoreOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbDescrittorePtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbDescrittorePtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbDescrittoreOffsetIn, offsetBufferTbDescrittorePtr);
		}
	}

	// Carichiamo gli offset dei soggetti/descrittori
	if (trSogDesOffsetIn ) // && (offsetFileVectorInMem.FindByValue(trSogDesOffsetIn) != -1)
	{
		trSogDesOffsetIn->SeekToEnd();
		fileSize = trSogDesOffsetIn->CurOffset();
		elements = elementsTrSogDes = fileSize/keyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(trSogDesOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trSogDesOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrSogDesPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrSogDesPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trSogDesOffsetIn, offsetBufferTrSogDesPtr);
		}
	}

	// Carichiamo gli offset delle relazioni descrittore/descrittore
	if (trDesDesOffsetIn ) // && (offsetFileVectorInMem.FindByValue(trDesDesOffsetIn) != -1)
	{
		trDesDesOffsetIn->SeekToEnd();
		fileSize = trDesDesOffsetIn->CurOffset();
		elements = elementsTrDesDes = fileSize/keyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(trDesDesOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trDesDesOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrDesDesPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrDesDesPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trDesDesOffsetIn, offsetBufferTrDesDesPtr);
		}
	}

	// Carichiamo gli offset delle relazioni descrittore/descrittore invertiti
	if (trDesDesInvOffsetIn ) // && (offsetFileVectorInMem.FindByValue(trDesDesInvOffsetIn) != -1)
	{
		trDesDesInvOffsetIn->SeekToEnd();
		fileSize = trDesDesInvOffsetIn->CurOffset();
		elements = elementsTrDesDesInv = fileSize/keyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(trDesDesInvOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trDesDesInvOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrDesDesInvPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrDesDesInvPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trDesDesInvOffsetIn, offsetBufferTrDesDesInvPtr);
		}
	}

	// Carichiamo gli offset della 977
	if (tb977OffsetIn)
	{
		tb977OffsetIn->SeekToEnd();
		fileSize = tb977OffsetIn->CurOffset();
		elements = elementsTb977 = fileSize / keyPlusOffsetPlusLfLength;
		if (tb977OffsetIn && (offsetFileVectorInMem.FindByValue(tb977OffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tb977OffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTb977Ptr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength));
			if (!offsetBufferTb977Ptr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tb977OffsetIn, offsetBufferTb977Ptr);
		}
	}



	// Carichiamo gli offset delle classi
	if (tbClasseOffsetIn)
	{
		tbClasseOffsetIn->SeekToEnd();
		fileSize = tbClasseOffsetIn->CurOffset();
		elements = elementsTbClasse = fileSize/classeKeyPlusOffsetPlusLfLength;
		if (tbClasseOffsetIn && (offsetFileVectorInMem.FindByValue(tbClasseOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbClasseOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbClassePtr = (char *)malloc(elements*(classeKeyPlusOffsetPlusLfLength)); // n righe di classe+Offset
			if (!offsetBufferTbClassePtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbClasseOffsetIn, offsetBufferTbClassePtr);
		}
	}

	// Carichiamo gli offset delle marche
	if (tbMarcaOffsetIn)
	{
		tbMarcaOffsetIn->SeekToEnd();
		fileSize = tbMarcaOffsetIn->CurOffset();
		elements = elementsTbMarca = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbMarcaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbMarcaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbMarcaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di mid+Offset
			if (!offsetBufferTbMarcaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbMarcaOffsetIn, offsetBufferTbMarcaPtr);
		}
	}


	// Carichiamo gli offset delle note all'inventario
	//if (tbcNotaInvOffsetIn && (offsetFileVectorInMem.FindByValue(tbcNotaInvOffsetIn) != -1))
	if (tbcNotaInvOffsetIn ) // Mantis polo 5302
	{
	tbcNotaInvOffsetIn->SeekToEnd();
	fileSize = tbcNotaInvOffsetIn->CurOffset();

//	int saveKeyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
//	keyPlusOffsetPlusLfLength = INVENTARIO_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;

	elements = elementsTbcNotaInv = fileSize/notaInvKeyPlusOffsetPlusLfLength;

	if (offsetFileVectorInMem.FindByValue(tbcNotaInvOffsetIn) != -1)
	{
		printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbcNotaInvOffsetIn->GetName(), notaInvKeyPlusOffsetPlusLfLength, elements);
		offsetBufferTbcNotaInvPtr = (char *)malloc(elements*(notaInvKeyPlusOffsetPlusLfLength)); // n righe di mid+Offset
		if (!offsetBufferTbcNotaInvPtr)	{
			printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
			return false;	}
		loadOffsetFiles2(tbcNotaInvOffsetIn, offsetBufferTbcNotaInvPtr);
	}
	//keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;

	}


	// Carichiamo gli offset delle impronte
	if (tbImprontaOffsetIn )
	{
		tbImprontaOffsetIn->SeekToEnd();
		fileSize = tbImprontaOffsetIn->CurOffset();
		elements = elementsTbImpronta = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbImprontaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbImprontaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbImprontaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di mid+Offset
			if (!offsetBufferTbImprontaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbImprontaOffsetIn, offsetBufferTbImprontaPtr);

		}
	}

	// Carichiamo gli offset dei luoghi
	if (tbLuogoOffsetIn )
	{
	tbLuogoOffsetIn->SeekToEnd();
	fileSize = tbLuogoOffsetIn->CurOffset();
	elements = elementsTbLuogo = fileSize/keyPlusOffsetPlusLfLength;
	if (offsetFileVectorInMem.FindByValue(tbLuogoOffsetIn) != -1)
	{
		printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbLuogoOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
		offsetBufferTbLuogoPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
		if (!offsetBufferTbLuogoPtr)	{
			printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
			return false;	}
		loadOffsetFiles2(tbLuogoOffsetIn, offsetBufferTbLuogoPtr);
	}
	}

	// Carichiamo gli offset delle relazioni titolo/titolo (.rel)
	if (trTitTitRelOffsetIn)
	{
		trTitTitRelOffsetIn->SeekToEnd();
		fileSize = trTitTitRelOffsetIn->CurOffset();
		elements = elementsTrTitTitRel = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitTitRelOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitTitRelOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitTitRelPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitTitRelPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitTitRelOffsetIn, offsetBufferTrTitTitRelPtr);
		}
	}

	// Carichiamo gli offset delle relazioni inverse titolo/titolo (.rel)
	if (trTitTitInvRelOffsetIn )
	{
		trTitTitInvRelOffsetIn->SeekToEnd();
		fileSize = trTitTitInvRelOffsetIn->CurOffset();
		elements = elementsTrTitTitInvRel = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitTitInvRelOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitTitInvRelOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitTitInvRelPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitTitInvRelPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitTitInvRelOffsetIn, offsetBufferTrTitTitInvRelPtr);
		}
	}

	// Carichiamo gli offset delle relazioni titolo->titolo
	if (trTitTitOffsetIn)
	{
		trTitTitOffsetIn->SeekToEnd();
		fileSize = trTitTitOffsetIn->CurOffset();

		elements = elementsTrTitTit = fileSize/trKeyPlusOffsetPlusLfLength;

		if ((offsetFileVectorInMem.FindByValue(trTitTitOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitTitOffsetIn->GetName(), trKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitTitPtr = (char *)malloc(elements*(trKeyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitTitPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitTitOffsetIn, offsetBufferTrTitTitPtr);
		}
		//keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;
	}

	if (trTitAutRelOffsetIn)
	{
		trTitAutRelOffsetIn->SeekToEnd();
		fileSize = trTitAutRelOffsetIn->CurOffset();
//		int saveKeyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
//		keyPlusOffsetPlusLfLength = BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		elements = elementsTrTitAutRel = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitAutRelOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitAutRelOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitAutRelPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitAutRelPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitAutRelOffsetIn, offsetBufferTrTitAutRelPtr);
		}
//		keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;
	}


	// Carichiamo gli offset delle relazioni titolo/soggetti
	if (trTitSogBibOffsetIn )
	{
		trTitSogBibOffsetIn->SeekToEnd();
		fileSize = trTitSogBibOffsetIn->CurOffset();
		elements = elementsTrTitSogBib = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitSogBibOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitSogBibOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitSogBibPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitSogBibPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitSogBibOffsetIn, offsetBufferTrTitSogBibPtr);
		}
	}

	// Carichiamo gli offset delle relazioni titolo/classi
	if (trTitClaOffsetIn )
	{
		trTitClaOffsetIn->SeekToEnd();
		fileSize = trTitClaOffsetIn->CurOffset();
		elements = elementsTrTitCla = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitClaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitClaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitClaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitClaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitClaOffsetIn, offsetBufferTrTitClaPtr);
		}
	}

	// Carichiamo gli offset delle relazioni titolo/marche
	if (trTitMarRelOffsetIn )
	{
		trTitMarRelOffsetIn->SeekToEnd();
		fileSize = trTitMarRelOffsetIn->CurOffset();
		elements = elementsTrTitMarRel = fileSize/keyPlusOffsetPlusLfLength; // trKeyPlusOffsetPlusLfLength
		if ((offsetFileVectorInMem.FindByValue(trTitMarRelOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitMarRelOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitMarRelPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset trKeyPlusOffsetPlusLfLength
			if (!offsetBufferTrTitMarRelPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitMarRelOffsetIn, offsetBufferTrTitMarRelPtr);
		}
	}

	// Carichiamo gli offset delle relazioni titolo/marche
	if (trTitMarTbOffsetIn )
	{
		trTitMarTbOffsetIn->SeekToEnd();
		fileSize = trTitMarTbOffsetIn->CurOffset();

//		int saveKeyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
//		keyPlusOffsetPlusLfLength = BID_KEY_LENGTH+MID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		elements = elementsTrTitMarTb = fileSize/trKeyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitMarTbOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitMarTbOffsetIn->GetName(), trKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitMarTbPtr = (char *)malloc(elements*(trKeyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitMarTbPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitMarTbOffsetIn, offsetBufferTrTitMarTbPtr);
		}
//		keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;
	}


	// Carichiamo gli offset delle relazioni titolo/collocazioni (950)
	// Inventari
	if (tb950InvOutOffsetFilenameIn)
	{
		tb950InvOutOffsetFilenameIn->SeekToEnd();
		fileSize = tb950InvOutOffsetFilenameIn->CurOffset();
		elements = elementsTb950Inv = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tb950InvOutOffsetFilenameIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tb950InvOutOffsetFilenameIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTb950InvPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTb950InvPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tb950InvOutOffsetFilenameIn, offsetBufferTb950InvPtr);
		}
	}

	// Collocazioni/BID
	if (tb950CollOutOffsetBidFilenameIn)
	{
		tb950CollOutOffsetBidFilenameIn->SeekToEnd();
		fileSize = tb950CollOutOffsetBidFilenameIn->CurOffset();
		elements = elementsTb950BidColl = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tb950CollOutOffsetBidFilenameIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tb950CollOutOffsetBidFilenameIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTb950CollBidPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTb950CollBidPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tb950CollOutOffsetBidFilenameIn, offsetBufferTb950CollBidPtr);
		}
	}

	// Collocazioni/KLoc
	if (tb950CollOutOffsetKLocFilenameIn)
	{
		tb950CollOutOffsetKLocFilenameIn->SeekToEnd();
//		keyPlusOffsetPlusLfLength = KEYLOC_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		fileSize = tb950CollOutOffsetKLocFilenameIn->CurOffset();
		elements = elementsTb950KeylocColl = fileSize/keyloctrKeyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tb950CollOutOffsetKLocFilenameIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tb950CollOutOffsetKLocFilenameIn->GetName(), keyloctrKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTb950CollKLocPtr = (char *)malloc(elements*(keyloctrKeyPlusOffsetPlusLfLength)); // n righe di KLoc+Offset
			if (!offsetBufferTb950CollKLocPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tb950CollOutOffsetKLocFilenameIn, offsetBufferTb950CollKLocPtr);
		}
	}

	// Esemplari

	if (tb950EseOutOffsetFilenameIn )
	{
		tb950EseOutOffsetFilenameIn->SeekToEnd();
		//keyPlusOffsetPlusLfLength = BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		fileSize = tb950EseOutOffsetFilenameIn->CurOffset();
		elements = elementsTb950Ese = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tb950EseOutOffsetFilenameIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tb950EseOutOffsetFilenameIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTb950EsePtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTb950EsePtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tb950EseOutOffsetFilenameIn, offsetBufferTb950EsePtr);
		}
	}


	// Carichiamo gli offset della entita relazioni inverse autori/autori
	if (trAutAutInvOffsetIn)
	{
		trAutAutInvOffsetIn->SeekToEnd();
		fileSize = trAutAutInvOffsetIn->CurOffset();
		elements = elementsTrAutAutInv = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trAutAutInvOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trAutAutInvOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrAutAutInvPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrAutAutInvPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trAutAutInvOffsetIn, offsetBufferTrAutAutInvPtr);
		}
	}



	// Carichiamo gli offset delle relazioni autori/autori
	if (trAutAutRelOffsetIn )
	{
		trAutAutRelOffsetIn->SeekToEnd();
		fileSize = trAutAutRelOffsetIn->CurOffset();
		elements = elementsTrAutAutRel = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trAutAutRelOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trAutAutRelOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrAutAutRelPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrAutAutRelPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trAutAutRelOffsetIn, offsetBufferTrAutAutRelPtr);

		}
	}

	// Carichiamo gli offset delle relazioni inverse autori/autori (per tipo legame 4)
	if (trAutAutRelInvOffsetIn )
	{
		trAutAutRelInvOffsetIn->SeekToEnd();
		fileSize = trAutAutRelInvOffsetIn->CurOffset();
		elements = elementsTrAutAutInvRel = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trAutAutRelInvOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trAutAutRelInvOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrAutAutRelInvPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrAutAutRelInvPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trAutAutRelInvOffsetIn, offsetBufferTrAutAutRelInvPtr);
		}
	}


	// Carichiamo gli offset delle relazioni inverse titoli/autori 19/03/2013
	if (trTitAutRelInvOffsetIn )
	{
		trTitAutRelInvOffsetIn->SeekToEnd();
		fileSize = trTitAutRelInvOffsetIn->CurOffset();
		elements = elementsTrTitAutInvRel = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitAutRelInvOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitAutRelInvOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitAutRelInvPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitAutRelInvPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitAutRelInvOffsetIn, offsetBufferTrTitAutRelInvPtr);
		}
	}


	// Carichiamo gli offset delle relazioni titoli/tesauro 04/12/15
	if (trsTerminiTitoliBibliotecheOffsetRelIn )
	{
		trsTerminiTitoliBibliotecheOffsetRelIn->SeekToEnd();
		fileSize = trsTerminiTitoliBibliotecheOffsetRelIn->CurOffset();
		elements = elementstsTrsTerminiTitoliBibliotecheRel = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trsTerminiTitoliBibliotecheOffsetRelIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trsTerminiTitoliBibliotecheOffsetRelIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBuffertsTrsTerminiTitoliBibliotecheRelPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBuffertsTrsTerminiTitoliBibliotecheRelPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trsTerminiTitoliBibliotecheOffsetRelIn, offsetBuffertsTrsTerminiTitoliBibliotecheRelPtr);
		}
	}



	// Carichiamo gli offset delle relazioni titolo/luogo (.rel)
	if (trTitLuoOffsetIn )
	{
		trTitLuoOffsetIn->SeekToEnd();
		fileSize = trTitLuoOffsetIn->CurOffset();
		elements = elementsTrTitLuo = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitLuoOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitLuoOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitLuoPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitLuoPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitLuoOffsetIn, offsetBufferTrTitLuoPtr);
		}
	}


	// Carichiamo gli offset degli altri id
	if (trBidAltroidOffsetIn)
	{
		trBidAltroidOffsetIn->SeekToEnd();
		fileSize = trBidAltroidOffsetIn->CurOffset();
		elements = elementsTrBidAltroid = fileSize/trBidAltroidKeyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trBidAltroidOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trBidAltroidOffsetIn->GetName(), trBidAltroidKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTrBidAltroidPtr = (char *)malloc(elements*(trBidAltroidKeyPlusOffsetPlusLfLength));
			if (!offsetBufferTrBidAltroidPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trBidAltroidOffsetIn, offsetBufferTrBidAltroidPtr);
		}
	}


	// Carichiamo gli offset della grafica
	if (tbGraficaOffsetIn)
	{
		tbGraficaOffsetIn->SeekToEnd();
		fileSize = tbGraficaOffsetIn->CurOffset();
		elements = elementsTbGrafica = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbGraficaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbGraficaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbGraficaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbGraficaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbGraficaOffsetIn, offsetBufferTbGraficaPtr);
		}
	}

	// Carichiamo gli offset della cartografia
	if (tbCartografiaOffsetIn)
	{
		tbCartografiaOffsetIn->SeekToEnd();
		fileSize = tbCartografiaOffsetIn->CurOffset();
		elements = elementsTbCartografia = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbCartografiaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbCartografiaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbCartografiaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbCartografiaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbCartografiaOffsetIn, offsetBufferTbCartografiaPtr);
		}
	}

	// Carichiamo gli offset della musica
	if (tbMusicaOffsetIn )
	{
		tbMusicaOffsetIn->SeekToEnd();
		fileSize = tbMusicaOffsetIn->CurOffset();
		elements = elementsTbMusica = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbMusicaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbMusicaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbMusicaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbMusicaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbMusicaOffsetIn, offsetBufferTbMusicaPtr);
		}
	}


	// Carichiamo gli offset delle relazioni titolo/autori
	if (trTitAutOffsetIn)
	{
		trTitAutOffsetIn->SeekToEnd();
		fileSize = trTitAutOffsetIn->CurOffset();
		elements = elementsTrTitAut = fileSize/trTitAutKeyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitAutOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitAutOffsetIn->GetName(), trKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitAutPtr = (char *)malloc(elements*(trTitAutKeyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitAutPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitAutOffsetIn, offsetBufferTrTitAutPtr);
		}
	}


	// Carichiamo gli offset delle relazioni titolo->biblioteca
	if (trTitBibOffsetIn )
	{
		trTitBibOffsetIn->SeekToEnd();
		fileSize = trTitBibOffsetIn->CurOffset();
//		int saveKeyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;

//		if (DATABASE_ID == DATABASE_INDICE)
//			trTitBibKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+POLO_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
//		else
			//trTitBibKeyPlusOffsetPlusLfLength = titbibKeyPlusOffsetPlusLfLength; //keyPlusOffsetPlusLfLength;

		elements = elementsTrTitBib = fileSize/titbibKeyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trTitBibOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trTitBibOffsetIn->GetName(), titbibKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTrTitBibPtr = (char *)malloc(elements*(titbibKeyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTrTitBibPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trTitBibOffsetIn, offsetBufferTrTitBibPtr);
		}
//		keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;
	}



	// Carichiamo gli offset della composizione
	if (tbComposizioneOffsetIn)
	{
		tbComposizioneOffsetIn->SeekToEnd();
		fileSize = tbComposizioneOffsetIn->CurOffset();
		elements = elementsTbComposizione = fileSize/keyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(tbComposizioneOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbComposizioneOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbComposizionePtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbComposizionePtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbComposizioneOffsetIn, offsetBufferTbComposizionePtr);
		}
	}

	// Carichiamo gli offset dell'incipit
	if (tbIncipitOffsetIn)
	{
		tbIncipitOffsetIn->SeekToEnd();
		fileSize = tbIncipitOffsetIn->CurOffset();
		elements = elementsTbIncipit = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbIncipitOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbIncipitOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbIncipitPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbIncipitPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbIncipitOffsetIn, offsetBufferTbIncipitPtr);
		}
	}

	// Carichiamo gli offset dei personaggi
	if (tbPersonaggioOffsetIn)
	{
		tbPersonaggioOffsetIn->SeekToEnd();
		fileSize = tbPersonaggioOffsetIn->CurOffset();
		elements = elementsTbPersonaggio = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbPersonaggioOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbPersonaggioOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbPersonaggioPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbPersonaggioPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbPersonaggioOffsetIn, offsetBufferTbPersonaggioPtr);
		}
	}

	// Carichiamo gli offset della rappresentazione
	if (tbRappresentOffsetIn)
	{
		tbRappresentOffsetIn->SeekToEnd();
		fileSize = tbRappresentOffsetIn->CurOffset();
		elements = elementsTbRappresent = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbRappresentOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbRappresentOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbRappresentPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbRappresentPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbRappresentOffsetIn, offsetBufferTbRappresentPtr);
		}
	}

	// Carichiamo gli offset degli ordini
	if (tbaOrdiniOffsetIn )
	{
		tbaOrdiniOffsetIn->SeekToEnd();
		fileSize = tbaOrdiniOffsetIn->CurOffset();
		elements = elementsTbaOrdini = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbaOrdiniOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbaOrdiniOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbaOrdiniPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di BID+Offset
			if (!offsetBufferTbaOrdiniPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbaOrdiniOffsetIn, offsetBufferTbaOrdiniPtr);
		}
	}

	// Carichiamo gli offset dei repertori
	if (tbRepertorioOffsetIn)
	{
		tbRepertorioOffsetIn->SeekToEnd();
		fileSize = tbRepertorioOffsetIn->CurOffset();
		elements = elementsTbRepertorio = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbRepertorioOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbRepertorioOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbRepertorioPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di classe+Offset
			if (!offsetBufferTbRepertorioPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbRepertorioOffsetIn, offsetBufferTbRepertorioPtr);
		}
	}

	// Carichiamo gli offset delle relazioni autori/repertori
	if (trRepAutOffsetIn)
	{
		trRepAutOffsetIn->SeekToEnd();
		fileSize = trRepAutOffsetIn->CurOffset();
		elements = elementsTrRepAut = fileSize/keyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(trRepAutOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trRepAutOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrRepAutPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength)); // n righe di classe+Offset
			if (!offsetBufferTrRepAutPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trRepAutOffsetIn, offsetBufferTrRepAutPtr);
		}
	}

	// Carichiamo gli offset delle relazioni autori/repertori
	if (trRepMarOffsetIn)
	{
		trRepMarOffsetIn->SeekToEnd();
		fileSize = trRepMarOffsetIn->CurOffset();
		elements = elementsTrRepMar = fileSize/keyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(trRepMarOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trRepMarOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrRepMarPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength));
			if (!offsetBufferTrRepMarPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trRepMarOffsetIn, offsetBufferTrRepMarPtr);
		}
	}

	// Carichiamo gli offset delle relazioni Personaggi/Interpreti
	if (trPerIntOffsetIn)
	{
		trPerIntOffsetIn->SeekToEnd();
		fileSize = trPerIntOffsetIn->CurOffset();
		elements = elementsTrPerInt = fileSize/keyPlusOffsetPlusLfLength;
		if (offsetFileVectorInMem.FindByValue(trPerIntOffsetIn) != -1)
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trPerIntOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTrPerIntPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength));
			if (!offsetBufferTrPerIntPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trPerIntOffsetIn, offsetBufferTrPerIntPtr);
		}
	}




	if (tbcPossessoreProvenienzaOffsetIn)
	{
		tbcPossessoreProvenienzaOffsetIn->SeekToEnd();
		fileSize = tbcPossessoreProvenienzaOffsetIn->CurOffset();
		elements = elementsTbcPossessoreProvenienza = fileSize/keyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(tbcPossessoreProvenienzaOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", tbcPossessoreProvenienzaOffsetIn->GetName(), keyPlusOffsetPlusLfLength, elements);
			offsetBufferTbcPossessoreProvenienzaPtr = (char *)malloc(elements*(keyPlusOffsetPlusLfLength));
			if (!offsetBufferTbcPossessoreProvenienzaPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(tbcPossessoreProvenienzaOffsetIn, offsetBufferTbcPossessoreProvenienzaPtr);
		}
	}
	if (trcPossProvInventariOffsetIn )
	{
		trcPossProvInventariOffsetIn->SeekToEnd();
//		int saveKeyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
//		keyPlusOffsetPlusLfLength = INVENTARIO_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		fileSize = trcPossProvInventariOffsetIn->CurOffset();
		elements = elementsTrcPossProvInventari = fileSize/inventarioKeyPlusOffsetPlusLfLength;
		if ((offsetFileVectorInMem.FindByValue(trcPossProvInventariOffsetIn) != -1))
		{
			printf ("\nCarico in memoria indice %s, keyOffsetLen=%d, elements=%ld", trcPossProvInventariOffsetIn->GetName(), inventarioKeyPlusOffsetPlusLfLength, elements);
			offsetBufferTrcPossProvInventariPtr = (char *)malloc(elements*(inventarioKeyPlusOffsetPlusLfLength)); // n righe di KLoc+Offset
			if (!offsetBufferTrcPossProvInventariPtr)	{
				printf ("\n!!!! ALLOCAZIONE MEMORIA FALLITA");
				return false;	}
			loadOffsetFiles2(trcPossProvInventariOffsetIn, offsetBufferTrcPossProvInventariPtr);
		}
//		keyPlusOffsetPlusLfLength = saveKeyPlusOffsetPlusLfLength;
	}
	return true;
} // End loadOffsetFile


void Marc4cpp::closeOffsetFiles()
{
	for (int i=0; i < offsetFileVectorInMem.length(); i++)
	{
//		printf ("\nCHIUDI OFFSET FILE: %s ", offsetFileVector.Entry(i)->GetName());
		CFile * filePtr = (CFile *)offsetFileVectorInMem.Entry(i);
		delete (filePtr);
			// offsetFileVector.SetEntry(i, 0);
	}
	offsetFileVectorInMem.Clear();

} // End closeOffsetFiles



bool Marc4cpp::apriFileOffset(ATTValVector<CString *> *offsetVector)
{
	char * tabella, *nomeFile, *loadInMemory=0;
	CFile* cFileIn;
	CString *sPtr;
	cIni ini;


	for (int i=0; i < offsetVector->Length(); i++)
	{
		sPtr = offsetVector->Entry(i);
		ini.SplitIniFields(sPtr->data());
//		ini.SplitIniFields(sPtr->data(), true); // 11/03/2016
		tabella = ini.fieldsVector->Entry(1)->data();
		nomeFile = ini.fieldsVector->Entry(2)->data();

		if (ini.IniFields > 3)
//		if (ini.fieldsVector->length() > 3)
			loadInMemory = ini.fieldsVector->Entry(3)->data();

printf ("\nAPRI FILE OFFSET': %s", sPtr->data());
//printf ("\nAPRI FILE OFFSET tabella': %s", tabella);
//printf ("\nAPRI FILE OFFSET nomefile': %s", nomeFile);

try {
		// printf ("\nAPRI OFFSET FILE: %s | nomeFile: %s", sPtr->data(), nomeFile);
		cFileIn = new CFile(nomeFile, AL_READ_FILE);

//		if (loadInMemory && (*loadInMemory =='N' || *loadInMemory =='n'))
		if (!loadInMemory || (*loadInMemory =='N' || *loadInMemory =='n'))
			offsetFileVectorOnDisc.Add(cFileIn);
		else
			offsetFileVectorInMem.Add(cFileIn);

	} catch (GenericError e) {
		std::cout << e.errorMessage;
		stop();
		return false;
	}

		if (!strcmp(tabella, "tb_titolo_off"))
			tbTitoloOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tb_nota_off"))
			tbNotaOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tb_nota_300_off"))
			tbNota300OffsetIn = cFileIn;

		else if (!strcmp(tabella, "tb_nota_321_off"))
			tbNota321OffsetIn = cFileIn;

		else if (!strcmp(tabella, "tb_977_off"))
			tb977OffsetIn = cFileIn;


		else if (!strcmp(tabella, "tbf_biblioteca_off"))
			tbfBibliotecaOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tr_tit_tit_rel_off"))
			trTitTitRelOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_tit_off"))
			trTitTitOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_tit_inv_rel_off"))
			trTitTitInvRelOffsetIn = cFileIn;


		else if (!strcmp(tabella, "tb_autore_off"))
			tbAutoreOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_aut_rel_off"))
			trTitAutRelOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tr_tit_aut_rel_inv_off"))
			trTitAutRelInvOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tr_aut_aut_rel_off"))
			trAutAutRelOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_aut_aut_rel_inv_off"))
			trAutAutRelInvOffsetIn = cFileIn;


		else if (!strcmp(tabella, "tr_aut_aut_inv_off"))
			trAutAutInvOffsetIn = cFileIn;



		else if (!strcmp(tabella, "tb_numero_std_off"))
			tbNumeroStandardOffsetIn = cFileIn;
		else if (!strcmp(tabella, "coll_kloc_off950"))
			tb950CollOutOffsetKLocFilenameIn = cFileIn;
		else if (!strcmp(tabella, "inv_bid_off950"))
			tb950InvOutOffsetFilenameIn = cFileIn;
		else if (!strcmp(tabella, "coll_bid_off950"))
			tb950CollOutOffsetBidFilenameIn = cFileIn;
		else if (!strcmp(tabella, "ese_off950"))
			tb950EseOutOffsetFilenameIn = cFileIn;
		else if (!strcmp(tabella, "tb_soggetto_off"))
			tbSoggettoOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_descrittore_off"))
			tbDescrittoreOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_sog_des_off"))
			trSogDesOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_des_des_off"))
			trDesDesOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_des_des_inv_off"))
			trDesDesInvOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_sog_bib_off"))
			trTitSogBibOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_classe_off"))
			tbClasseOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_cla_off"))
			trTitClaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_marca_off"))
			tbMarcaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tbc_nota_inv_off"))
			tbcNotaInvOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tr_tit_mar_tb_off"))
			trTitMarTbOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tr_tit_mar_off"))
			trTitMarRelOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_impronta_off"))
			tbImprontaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_luogo_off"))
			tbLuogoOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_luo_off"))
			trTitLuoOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tr_bid_altroid_off"))
			trBidAltroidOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tb_grafica_off"))
			tbGraficaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_cartografia_off"))
			tbCartografiaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_musica_off"))
			tbMusicaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_aut_off"))
			trTitAutOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_bib_off"))
			trTitBibOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_composizione_off"))
			tbComposizioneOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_incipit_off"))
			tbIncipitOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_personaggio_off"))
			tbPersonaggioOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_rappresent_off"))
			tbRappresentOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tba_ordini_off"))
			tbaOrdiniOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_repertorio_off"))
			tbRepertorioOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_rep_aut_off"))
			trRepAutOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_rep_mar_off"))
			trRepMarOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tr_per_int_off"))
			trPerIntOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tbc_possessore_provenienza_off"))
			tbcPossessoreProvenienzaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tbc_sezione_collocazione_off"))
			tbcSezioneCollocazioneOffsetIn = cFileIn;
		else if (!strcmp(tabella, "trc_poss_prov_inventari_off"))
			trcPossProvInventariOffsetIn = cFileIn;
		else if (!strcmp(tabella, "ts_link_multim_off"))
			tsLinkMultimOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_parola_off"))
			tbParolaOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_audiovideo_off"))
			tbAudiovideoOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_disco_sonoro_off"))
			tbDiscoSonoroOffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_titset_1_off"))
			tbTitSet1OffsetIn = cFileIn;
		else if (!strcmp(tabella, "tb_titset_2_off"))
			tbTitSet2OffsetIn = cFileIn;
		else if (!strcmp(tabella, "ts_link_web_off"))
			tsLinkWebOffsetIn = cFileIn;

		else if (!strcmp(tabella, "tb_termine_thesauro_off"))
			tbTermineTesauroOffsetIn= cFileIn;
		else if (!strcmp(tabella, "trs_termini_titoli_biblioteche_rel_off"))
			trsTerminiTitoliBibliotecheOffsetRelIn= cFileIn;




		else
		{
		    SignalAnError(__FILE__, __LINE__, "File offset %s non identificato", tabella);
			return false;
		}
	}
	return true;
} // apriFileOffset


bool  Marc4cpp::loadTbcSezioneCollocazione(char *polo) // char *tbcSezioneCollocazioneFilename,
{
    CFile* tbcSezioneCollocazioneIn = 0;
//  tbcSezioneCollocazioneIn = new CFile(tbcSezioneCollocazioneFilename, AL_READ_FILE);
//  if (!tbcSezioneCollocazioneIn)
//  {
//      SignalAnError(__FILE__, __LINE__, "Cannot open %s", tbcSezioneCollocazioneFilename);
//      return false;
//  }

    char *ptr;
    int i;
    for (i=0; i<fileEntitaInMemVector.length(); i++) // Cerchiamo il file handle in memoria
    {
        {
        ptr = fileEntitaInMemVector.Entry(i)->GetName();
        if (strstr( ptr, "tbc_sezione_collocazione"))
        {
            tbcSezioneCollocazioneIn = fileEntitaInMemVector.Entry(i); // ->GetFilePtr()
            break;
        }
        }
    }
    if (i == fileEntitaInMemVector.length())
    {
        for (i=0; i<fileRelazioniInMemVector.length(); i++) // Cerchiamo il file handle in memoria
        {
            {
            ptr = fileRelazioniInMemVector.Entry(i)->GetName();
            if (strstr( ptr, "tbc_sezione_collocazione"))
            {
                tbcSezioneCollocazioneIn = fileRelazioniInMemVector.Entry(i); // ->GetFilePtr()
                break;
            }
            }
        }
    }

    if (i == fileRelazioniInMemVector.length())
    { // Cerchiamo il file handle su disco
        for (i=0; i<fileOnDiscVector.length(); i++) // Cerchiamo il file handle in memoria
        {
            {
            ptr = fileOnDiscVector.Entry(i)->GetName();
            if (strstr( ptr, "tbc_sezione_collocazione"))
            {
                tbcSezioneCollocazioneIn = fileOnDiscVector.Entry(i); // ->GetFilePtr()
                break;
            }
            }
        }

    }

    if (!tbcSezioneCollocazioneIn)
    {
        printf ("\nERRORE: non trovo handle per tbc_sezione_collocazione");
        return false;
    }

    CString key;
    TbcSezioneCollocazione *tbcSezioneCollocazione = new TbcSezioneCollocazione(tbcSezioneCollocazioneIn);

    const char *noteSez;
    while (tbcSezioneCollocazione->loadNextRecord())
    {
//tbcSezioneCollocazione->dumpRecord();
        noteSez = tbcSezioneCollocazione->getField(tbcSezioneCollocazione->note_sez);

        // Se il campo nota inizia per "[]" allora il codice sezione del campo $z cd_sez non deve essere visuualizzato
        if (*(noteSez) == '[' && *(noteSez+1) == ']')
        {
            key = tbcSezioneCollocazione->getField(tbcSezioneCollocazione->cd_biblioteca);
            key.AppendString(tbcSezioneCollocazione->getFieldString(tbcSezioneCollocazione->cd_sez));
            key.Strip(key.trailing, ' ');
            tbcSezioneCollocazioneQuadreKV->Add(key.data(), noteSez);
        }
    } // end while

    // Ordiniamo per una ricerca rapida
    tbcSezioneCollocazioneQuadreKV->SortAscendingByKey();

        // Gia' ordinato per chiave biblioteca
    delete tbcSezioneCollocazione;
//  delete tbcSezioneCollocazioneIn;
    return true;
} // End loadTbcSezioneCollocazione





// Apriamo i file delle entita
bool Marc4cpp::apriFileEntita(ATTValVector<CString *> *entitaVector)
{
	char * tabella, *nomeFile, *loadInMemory;
	CFile* cFileIn;
//	CFileInMemory* cFileInMemory;
	CString *sPtr;
	cIni ini;


	for (int i=0; i < entitaVector->Length(); i++)
	{
		sPtr = entitaVector->Entry(i);
		ini.SplitIniFields(sPtr->data());
//		ini.SplitIniFields(sPtr->data(), true); // 11/03/2016
		tabella = ini.fieldsVector->Entry(1)->data();
		nomeFile = ini.fieldsVector->Entry(2)->data();
		loadInMemory = ini.fieldsVector->Entry(3)->data();

printf ("\nAPRI FILE ENTITA': %s", sPtr->data());


		if (!strcmp(tabella, "tbf_biblioteca_in_polo"))
		{
			tbfBibliotecaInPoloFilename = nomeFile;
			continue; // gestita a parte per ora
		}


		if (*loadInMemory =='S' || *loadInMemory =='s')
		{
			cFileIn = new CFileInMemory(nomeFile, AL_READ_FILE);
			fileEntitaInMemSize += ((CFileInMemory*)cFileIn)->getFileSize();

			fileEntitaInMemVector.Add(cFileIn);
		}
		else
		{
			cFileIn = new CFile(nomeFile, AL_READ_FILE);
			fileOnDiscVector.Add(cFileIn);
		}


		if (!strcmp(tabella, "tb_titolo"))
			tbTitoloIn = cFileIn;
		else if (!strcmp(tabella, "tbf_biblioteca"))
			tbfBibliotecaIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_tit"))
			trTitTitIn = cFileIn;
		else if (!strcmp(tabella, "tb_autore"))
			tbAutoreIn = cFileIn;
		else if (!strcmp(tabella, "tb_numero_std"))
			tbNumeroStandardIn = cFileIn;
		else if (!strcmp(tabella, "inv950"))
			 tb950InvOutFilenameIn = cFileIn;
		else if (!strcmp(tabella, "coll950"))
			tb950CollOutFilenameIn = cFileIn;
		else if (!strcmp(tabella, "ese950"))
			tb950EseOutFilenameIn = cFileIn;

		else if (!strcmp(tabella, "tb_977")) // 16/12/14
			tb977In = cFileIn;

		else if (!strcmp(tabella, "tb_soggetto"))
			tbSoggettoIn = cFileIn;
		else if (!strcmp(tabella, "tb_descrittore"))
			tbDescrittoreIn = cFileIn;
		else if (!strcmp(tabella, "tr_sog_des"))
			trSogDesIn = cFileIn;
		else if (!strcmp(tabella, "tr_des_des"))
			trDesDesIn = cFileIn;
		else if (!strcmp(tabella, "tr_des_des_inv"))
			trDesDesInvIn = cFileIn;
		else if (!strcmp(tabella, "tb_classe"))
			tbClasseIn= cFileIn;
		else if (!strcmp(tabella, "tb_marca"))
			tbMarcaIn= cFileIn;
		else if (!strcmp(tabella, "tbc_nota_inv"))
			tbcNotaInvIn= cFileIn;
		else if (!strcmp(tabella, "tb_impronta"))
			tbImprontaIn= cFileIn;
		else if (!strcmp(tabella, "tb_luogo"))
			tbLuogoIn = cFileIn;
		else if (!strcmp(tabella, "tb_grafica"))
			tbGraficaIn= cFileIn;
		else if (!strcmp(tabella, "tb_cartografia"))
			tbCartografiaIn = cFileIn;
		else if (!strcmp(tabella, "tb_musica"))
			tbMusicaIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_aut"))
			trTitAutIn = cFileIn;
		else if (!strcmp(tabella, "tr_aut_aut_inv"))
			trAutAutInvIn = cFileIn;
		else if (!strcmp(tabella, "tr_tit_bib"))
			trTitBibIn = cFileIn;
		else if (!strcmp(tabella, "tb_composizione"))
			tbComposizioneIn = cFileIn;
		else if (!strcmp(tabella, "tb_incipit"))
			tbIncipitIn = cFileIn;
		else if (!strcmp(tabella, "tb_personaggio"))
			tbPersonaggioIn = cFileIn;
		else if (!strcmp(tabella, "tb_rappresent"))
			tbRappresentIn = cFileIn;
		else if (!strcmp(tabella, "tba_ordini"))
			tbaOrdiniIn = cFileIn;
		else if (!strcmp(tabella, "tb_repertorio"))
			tbRepertorioIn = cFileIn;
		else if (!strcmp(tabella, "tr_rep_aut"))
			trRepAutIn = cFileIn;

		else if (!strcmp(tabella, "tr_rep_mar"))
			trRepMarIn = cFileIn;

	else if (!strcmp(tabella, "tr_tit_mar_tb"))
		trTitMarTbIn = cFileIn;

		else if (!strcmp(tabella, "tr_per_int"))
			trPerIntIn = cFileIn;
		else if (!strcmp(tabella, "tbc_possessore_provenienza"))
			tbcPossessoreProvenienzaIn = cFileIn;

		else if (!strcmp(tabella, "tbc_sezione_collocazione"))
			tbcSezioneCollocazioneIn = cFileIn;

		else if (!strcmp(tabella, "trc_poss_prov_inventari"))
			trcPossProvInventariIn = cFileIn;
		else if (!strcmp(tabella, "tb_nota"))
			tbNotaIn = cFileIn;
		else if (!strcmp(tabella, "tb_nota_300"))
			tbNota300In = cFileIn;
		else if (!strcmp(tabella, "tb_nota_321"))
			tbNota321In = cFileIn;

		else if (!strcmp(tabella, "ts_link_multim"))
			tsLinkMultimIn= cFileIn;
		else if (!strcmp(tabella, "tb_parola"))
			tbParolaIn = cFileIn;
		else if (!strcmp(tabella, "tb_audiovideo"))
			tbAudiovideoIn = cFileIn;
		else if (!strcmp(tabella, "tb_disco_sonoro"))
			tbDiscoSonoroIn = cFileIn;
		else if (!strcmp(tabella, "tb_titset_1"))
			tbTitSet1In = cFileIn;
		else if (!strcmp(tabella, "tb_titset_2"))
			tbTitSet2In = cFileIn;
		else if (!strcmp(tabella, "ts_link_web"))
			tsLinkWebIn= cFileIn;
		else if (!strcmp(tabella, "tb_termine_thesauro"))
			tbTermineTesauroIn= cFileIn;
//		else if (!strcmp(tabella, "trs_termini_titoli_biblioteche"))
//			trsTerminiTitoliBibliotecheIn= cFileIn;

		else if (!strcmp(tabella, "tr_bid_altroid"))
			trBidAltroidIn = cFileIn;


		else
		{
		    SignalAnError(__FILE__, __LINE__, "Entita %s non identificata", tabella);
			return false;

		}
	}
	return true;
} // End apriFileEntita




