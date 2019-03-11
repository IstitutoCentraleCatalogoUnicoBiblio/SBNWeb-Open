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
/* Marc4cpp.cpp
 *
 *  Created on: 1-dic-2008
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
#include "library/macros.h"
#include <malloc.h>
#include "TbcSezioneCollocazione.h"
#include "MarcGlobals.h"

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

//#define LOG_SEEKS


#define TEST_EXPORT_DA_LISTA

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);


extern bool logNaturaErrata;

//#define DEBUG_LEGAMI


Marc4cpp::Marc4cpp() {

	zeroInit();

	if (OFFSET_TYPE == OFFSET_TYPE_ASCII)
	{
		keyPlusOffsetPlusLfLength = BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		titoloKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		titbibKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		classeKeyPlusOffsetPlusLfLength = CLASSE_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		trKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;

		trTitAutKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;

		sezioneDiColocazioneKeyPlusOffsetPlusLfLength = TBC_SEZIONE_COLLOCAZIONE_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		bibliotecaKeyPlusOffsetPlusLfLength = BIBLIOTECA_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		notaInvKeyPlusOffsetPlusLfLength = INVENTARIO_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		inventarioKeyPlusOffsetPlusLfLength = INVENTARIO_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		keyloctrKeyPlusOffsetPlusLfLength = KEYLOC_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		offsetPlusLfLength = OFFSET_LENGTH+LF_LENGTH;

		titset1KeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
		titset2KeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;

		trBidAltroidKeyPlusOffsetPlusLfLength = BID_ALTRO_ID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH;
	}
	else
	{ // OFFSET_TYPE_BINARY
		// NB: Gestione indici long long ad hoc
		keyPlusOffsetPlusLfLength = BID_KEY_LENGTH+LONG_SIZE;
		titoloKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+LONG_LONG_SIZE;
		titbibKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+LONG_LONG_SIZE;
		classeKeyPlusOffsetPlusLfLength = CLASSE_KEY_LENGTH+LONG_SIZE;

		trKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+BID_KEY_LENGTH+LONG_SIZE;
		trTitAutKeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+BID_KEY_LENGTH+LONG_LONG_SIZE;

		sezioneDiColocazioneKeyPlusOffsetPlusLfLength = TBC_SEZIONE_COLLOCAZIONE_KEY_LENGTH+LONG_SIZE;
		bibliotecaKeyPlusOffsetPlusLfLength = BIBLIOTECA_KEY_LENGTH+LONG_SIZE;
		notaInvKeyPlusOffsetPlusLfLength = INVENTARIO_KEY_LENGTH+LONG_SIZE;
		inventarioKeyPlusOffsetPlusLfLength = INVENTARIO_KEY_LENGTH+LONG_SIZE;
		keyloctrKeyPlusOffsetPlusLfLength = KEYLOC_KEY_LENGTH+LONG_SIZE;
		offsetPlusLfLength = 4; // long
		titset1KeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+LONG_LONG_SIZE;
		titset2KeyPlusOffsetPlusLfLength = BID_KEY_LENGTH+LONG_LONG_SIZE;

		trBidAltroidKeyPlusOffsetPlusLfLength = BID_ALTRO_ID_KEY_LENGTH+LONG_SIZE;;

	}

} // Marc4cpp

void Marc4cpp::zeroInit() {
	authority = 0;
	marcRecord = 0;
	tbTitolo = 0;
	tbfBiblioteca = 0;
	tbClasse = 0;
	tbNumeroStandard = 0;
	tbMarca = 0;
	tbcNotaInv = 0;
	trTitTit = 0;
	trTitMarRel = 0;
	trTitMarTb=0;
	tbImpronta = 0;
	tbLuogo = 0;
	tbGrafica = 0;
	tbCartografia = 0;
	tbMusica = 0;
	tbComposizione = 0;
	tbIncipit = 0;
	tbPersonaggio = 0;
	tbRappresent = 0;
	tbaOrdini = 0;
	tbfBibliotecaInPoloKV = 0;
	tbcSezioneCollocazioneQuadreKV = 0;

    marc4cppCollocazione = 0;
    marc4cppDocumento = 0;
    marc4cppLegami = 0;
    marc4cppLegamiAuthority = 0;
    marc4cppAltriDoc = 0;
	marcOut = 0;
	marcOutTxt = 0;
	marcOutXml = 0;
	bidErratiOut = 0;
//	marcStreamWriter = 0;
	bufferedMarcStreamWriter = 0;

	idXunimarcIn = 0;
	tbMarcaOffsetIn = 0;
	tbcNotaInvIn = 0;
	tbcNotaInvOffsetIn = 0;

	// Tabella dati comuni
	tbTitSet1In = 0;
	tbTitSet1OffsetIn = 0;
	elementsTbTitSet1 = 0;
	offsetBufferTbTitSet1Ptr = 0;

	// Tabella dati titolo dell'opera
	tbTitSet2In = 0;
	tbTitSet2OffsetIn = 0;
	elementsTbTitSet2 = 0;
	offsetBufferTbTitSet2Ptr = 0;


	// Tabella audiovideo
	tbAudiovideoIn = 0;
	tbAudiovideoOffsetIn = 0;
	elementsTbAudiovideo = 0;
	offsetBufferTbAudiovideoPtr = 0;

	tbDiscoSonoroIn = 0;
	tbDiscoSonoroOffsetIn = 0;
	elementsTbDiscoSonoro = 0;
	offsetBufferTbDiscoSonoroPtr = 0;


	// Tabella link multimediali
	tsLinkMultimIn = 0;
	tsLinkMultimOffsetIn = 0;
	elementsTsLinkMultim = 0;
	offsetBufferTsLinkMultimPtr = 0;


	tsLinkWebIn = 0;
	tsLinkWebOffsetIn = 0;
	elementsTsLinkWeb = 0;
	offsetBufferTsLinkWebPtr = 0;
	tsLinkWeb = 0;



	// Tabella tbcSezioneDiCollocazione
	tbcSezioneCollocazioneIn = 0;
	tbcSezioneCollocazioneOffsetIn = 0;
	elementsTbcSezioneCollocazione = 0;
	offsetBufferTbcSezioneCollocazionePtr = 0;


	// Tabella parola
	tbParolaIn = 0;
	tbParolaOffsetIn = 0;
	elementsTbParola = 0;
	offsetBufferTbParolaPtr = 0;


	// Tabella nota
	tbNotaIn = 0;
	tbNotaOffsetIn = 0;
	elementsTbNota = 0;
	offsetBufferTbNotaPtr = 0;

	// Tabella nota 300
	tbNota300In = 0;
	tbNota300OffsetIn = 0;
	elementsTbNota300 = 0;
	offsetBufferTbNota300Ptr = 0;

	// Tabella nota 321
	tbNota321In = 0;
	tbNota321OffsetIn = 0;
	elementsTbNota321 = 0;
	offsetBufferTbNota321Ptr = 0;


	// Authority titolo
	tbTitoloIn = 0;
	tbTitoloOffsetIn = 0;
	elementsTbTitolo = 0;
	offsetBufferTbTitoloPtr = 0;

	// Entity Biblioteca
	tbfBibliotecaIn = 0;
	tbfBibliotecaOffsetIn = 0;
	elementsTbfBiblioteca = 0;
	offsetBufferTbfBibliotecaPtr = 0;

	// Authority autore
	tbAutore = 0;
	tbAutoreIn = 0;
	tbAutoreOffsetIn = 0;
	elementsTbAutore = 0;
	offsetBufferTbAutorePtr = 0;


	// Authority numero standard
	tbNumeroStandardIn = 0;
	tbNumeroStandardOffsetIn = 0;
	elementsTbNumeroStandard = 0;
	offsetBufferTbNumeroStandardPtr = 0;

	// Authority soggetto
	tbSoggetto = 0;
	tbSoggettoIn = 0;
	tbSoggettoOffsetIn = 0;
	elementsTbSoggetto = 0;
	offsetBufferTbSoggettoPtr = 0;

	trTitAutRelInvIn=0; // 02/08/2016
	trTitAutRelInvOffsetIn = 0;
	offsetBufferTrTitAutRelInvPtr=0;
	elementsTrTitAutInvRel = 0;


	// Authority Descrittore
	tbDescrittore = 0;
	tbDescrittoreIn = 0;
	tbDescrittoreOffsetIn = 0;
	elementsTbDescrittore = 0;
	offsetBufferTbDescrittorePtr = 0;

	// Relazione soggetto descrittore
	trSogDes = 0;
	trSogDesIn = 0;
	trSogDesOffsetIn = 0;
	elementsTrSogDes = 0;
	offsetBufferTrSogDesPtr = 0;

	// Relazione descrittore/descrittore
	trDesDes = 0;
	trDesDesIn = 0;
	trDesDesOffsetIn = 0;
	elementsTrDesDes = 0;
	offsetBufferTrDesDesPtr = 0;


	// Relazione descrittore/descrittore inv
	trDesDesInv = 0;
	trDesDesInvIn = 0;
	trDesDesInvOffsetIn = 0;
	elementsTrDesDesInv = 0;
	offsetBufferTrDesDesInvPtr = 0;

	// Authority luogo
	tbLuogo = 0;
	tbLuogoIn = 0;
	tbLuogoOffsetIn = 0;
	elementsTbLuogo = 0;
	offsetBufferTbLuogoPtr = 0;

	// Relazioni titolo->titolo
	trTitTitRelIn = 0;
	trTitTitRelOffsetIn = 0;
	elementsTrTitTitRel = 0;
	offsetBufferTrTitTitRelPtr = 0;

	// Relazioni inverse titolo->titolo
	trTitTitInvRelIn = 0;
	trTitTitInvRelOffsetIn = 0;
	elementsTrTitTitInvRel = 0;
	offsetBufferTrTitTitInvRelPtr = 0;

	trTitTitIn = 0;
	trTitTitOffsetIn = 0;
	elementsTrTitTit = 0;
	offsetBufferTrTitTitPtr = 0;


	// Relazioni titolo->autore
	trTitAutRelIn = 0;
	trTitAutRelOffsetIn = 0;
	elementsTrTitAutRel = 0;
	offsetBufferTrTitAutRelPtr = 0;
	trTitAutRel = 0;

	trTitAutIn = 0;
	trTitAutOffsetIn = 0;
	elementsTrTitAut = 0;
	offsetBufferTrTitAutPtr = 0;
	trTitAut = 0;

	// Relazioni titolo->biblioteca
	trTitBibIn = 0;
	trTitBibOffsetIn = 0;
	elementsTrTitBib = 0;
	offsetBufferTrTitBibPtr = 0;
	trTitBib = 0;

	// Relazioni titolo->soggetto
	trTitSogBibIn = 0;
	trTitSogBibOffsetIn = 0;
	elementsTrTitSogBib = 0;
	offsetBufferTrTitSogBibPtr = 0;

	// Relazioni titolo->classe
	trTitClaIn = 0;
	trTitClaOffsetIn = 0;
	elementsTrTitCla = 0;
	offsetBufferTrTitClaPtr = 0;
	trTitCla = 0;

	// Relazioni titolo->marca
	trTitMarRelIn = 0;
	trTitMarRelOffsetIn = 0;
	elementsTrTitMarRel = 0;
	offsetBufferTrTitMarRelPtr = 0;

	trTitMarTbIn = 0;
	trTitMarTbOffsetIn = 0;
	elementsTrTitMarTb = 0;
	offsetBufferTrTitMarTbPtr = 0;

	// Relazioni titolo->collocazione (950)

	tb950InvOutFilenameIn = 0;
	tb950InvOutOffsetFilenameIn = 0;
	elementsTb950Inv = 0;
	offsetBufferTb950InvPtr = 0;
	tb950Inv = 0;

	tb950CollOutFilenameIn = 0;
	tb950CollOutOffsetBidFilenameIn = 0;
	tb950CollOutOffsetKLocFilenameIn = 0;
	elementsTb950BidColl = 0;	// Numero di elementi
	elementsTb950KeylocColl = 0;	// Numero di elementi
	offsetBufferTb950CollBidPtr = 0;
	offsetBufferTb950CollKLocPtr = 0;
	tb950Coll = 0;

	tb950EseOutFilenameIn = 0;
	tb950EseOutOffsetFilenameIn = 0;
	elementsTb950Ese = 0;	// Numero di elementi
	offsetBufferTb950EsePtr = 0;
	tb950Ese = 0;

	// Relazioni titolo->luogo
	trTitLuoIn = 0;
	trTitLuoOffsetIn = 0;
	elementsTrTitLuo = 0;
	offsetBufferTrTitLuoPtr = 0;
	trTitLuo = 0;


	// Relazioni titolo->altro id
	trBidAltroidIn = 0;
	trBidAltroidOffsetIn = 0;
	elementsTrBidAltroid = 0;
	offsetBufferTrBidAltroidPtr = 0;
	trBidAltroid = 0;


	// Entita' grafica
	tbGraficaIn = 0;
	tbGraficaOffsetIn = 0;
	elementsTbGrafica = 0;
	offsetBufferTbGraficaPtr = 0;

	// Entita' cartografia
	tbCartografiaIn = 0;
	tbCartografiaOffsetIn = 0;
	elementsTbCartografia = 0;
	offsetBufferTbCartografiaPtr = 0;

	// Entita' musica
	tbMusicaIn = 0;
	tbMusicaOffsetIn = 0;
	elementsTbMusica = 0;
	offsetBufferTbMusicaPtr = 0;

	// Entita' composizione
	tbComposizioneIn = 0;
	tbComposizioneOffsetIn = 0;
	elementsTbComposizione = 0;
	offsetBufferTbComposizionePtr = 0;

	// Entita' incipit
	tbIncipitIn = 0;
	tbIncipitOffsetIn = 0;
	elementsTbIncipit = 0;
	offsetBufferTbIncipitPtr = 0;

	// Entita' personaggio
	tbPersonaggioIn = 0;
	tbPersonaggioOffsetIn = 0;
	elementsTbPersonaggio = 0;
	offsetBufferTbPersonaggioPtr = 0;

	// Entita' rappresentazione
	tbRappresentIn = 0;
	tbRappresentOffsetIn = 0;
	elementsTbRappresent = 0;
	offsetBufferTbRappresentPtr = 0;

	// Entita' ordini
	tbaOrdiniIn = 0;
	tbaOrdiniOffsetIn = 0;
	elementsTbaOrdini = 0;
	offsetBufferTbaOrdiniPtr = 0;


	tbMarcaIn = 0;
	tbMarcaOffsetIn = 0;
	elementsTbMarca = 0;
	offsetBufferTbMarcaPtr = 0;


	tbcNotaInv = 0;
	tbcNotaInvOffsetIn = 0;
	elementsTbcNotaInv = 0;
	offsetBufferTbcNotaInvPtr = 0;



	tbImprontaIn = 0;
	tbImprontaOffsetIn = 0;
	elementsTbImpronta = 0;
	offsetBufferTbImprontaPtr = 0;

	trAutAutRelIn = 0;
	trAutAutRelOffsetIn = 0;
	elementsTrAutAutRel = 0;
	offsetBufferTrAutAutRelPtr = 0;

	tbSoggettoIn = 0;
	tbSoggettoOffsetIn = 0;
	elementsTbSoggetto = 0;
	offsetBufferTbSoggettoPtr = 0;

	tbDescrittoreIn = 0;
	tbDescrittoreOffsetIn = 0;
	elementsTbDescrittore = 0;
	offsetBufferTbDescrittorePtr = 0;


	//tbClasseOffsetIn = 0;
	tbClasseOffsetIn = 0;
	elementsTbClasse = 0;
	offsetBufferTbClassePtr = 0;

	tbRepertorio = 0;
	tbRepertorioIn = 0;
	tbRepertorioOffsetIn = 0;
	elementsTbRepertorio = 0;
	offsetBufferTbRepertorioPtr = 0;

	trRepAut = 0;
	trRepAutIn = 0;
	trRepAutOffsetIn = 0;
	elementsTrRepAut = 0;
	offsetBufferTrRepAutPtr = 0;

	// Authority classe
	tbClasseIn = 0;
	//tbClasseOffsetIn = 0;
	elementsTbClasse = 0;
	offsetBufferTbClassePtr = 0;


	trAutAutRelInvIn = 0;
	trAutAutRelInvOffsetIn = 0;
	elementsTrAutAutInvRel = 0;
	offsetBufferTrAutAutRelInvPtr = 0;


	trAutAutInvIn = 0;
	trAutAutInvOffsetIn = 0;
	elementsTrAutAutInv = 0;
	offsetBufferTrAutAutInvPtr = 0;
	trAutAutInv = 0;


	tbcPossessoreProvenienzaIn = 0;
	tbcPossessoreProvenienzaOffsetIn = 0;
	elementsTbcPossessoreProvenienza = 0;
	offsetBufferTbcPossessoreProvenienzaPtr = 0;
	tbcPossessoreProvenienza = 0;

	// Tabella di relazione tra possessori-provenienza ed inventari 26/01/2010 17.14
	trcPossProvInventariIn = 0;
	trcPossProvInventariOffsetIn = 0;
	elementsTrcPossProvInventari = 0;
	offsetBufferTrcPossProvInventariPtr = 0;
	trcPossProvInventari = 0;


	// Legami Marca/Repertorio
	// ------------------------
	trRepMarIn = 0;
	trRepMarOffsetIn = 0;
	elementsTrRepMar = 0;	// Numero di classi
	offsetBufferTrRepMarPtr = 0;
	trRepMar = 0;

	// Legami Personaggi/Interpreti
	// ----------------------------
	trPerIntIn = 0;
	trPerIntOffsetIn = 0;
	elementsTrPerInt = 0;
	offsetBufferTrPerIntPtr = 0;
	trPerInt = 0;


	// Localizzazioni 977 (POLO) 14/01/2015
	tb977In = 0;
	tb977OffsetIn = 0;
	elementsTb977 = 0;
	offsetBufferTb977Ptr = 0;
	tb977 = 0;


	// Per le 689
	tbTermineTesauroIn = 0;
	tbTermineTesauroOffsetIn = 0;
	elementsTbTermineTesauro = 0;
	offsetBufferTbTermineTesauroPtr = 0;
	tbTermineTesauro = 0;


	// Gestione tesauro relazioni 04/12/2015
	// --------------------------
	trsTerminiTitoliBibliotecheRelIn = 0;
	trsTerminiTitoliBibliotecheOffsetRelIn = 0;
	elementstsTrsTerminiTitoliBibliotecheRel = 0;
	offsetBuffertsTrsTerminiTitoliBibliotecheRelPtr = 0;
	trsTerminiTitoliBiblioteche = 0;



	marcOutTxt = 0;
	marcOutXml = 0;
	reticoliOut = 0;

	recordsNonDocumento = 0;
	marc4cppDocumentoAuthority = 0;
};


Marc4cpp::~Marc4cpp() {
	stop();
}// End ~Marc4cpp




///*
// * Metodo che permette la ricerca in array di caratteri composti da chiavi o chiavi + offset a lunghezza fissa
// * Ritorna true tre trova chiave e posizione della chiave all'interno dell'array.
// */
//bool Marc4cpp::binarySearch(char* offsetMapPtr, long elements, int rowLength, char *keyToSearch, int keyLength, long& position, char ** entryPtr)
//{
//	long low = 0, high = elements-1, midpoint = 0;
//	char *midpointPtr;
//	int ret;
//
//	while (low <= high)
//	{
//		midpoint = (low + high) >> 1; // bitwise divide by two
//		midpointPtr = offsetMapPtr + (midpoint * rowLength);
//		ret = strncmp (keyToSearch, midpointPtr, keyLength);
//		if (!ret)
//			{
//			position = midpoint;
//			*entryPtr = midpointPtr;
//			return true;
//			}
//		else if (ret < 0)
//			high = midpoint - 1; // Cerca nella parte bassa
//		else
//			low = midpoint + 1; // Cerca nella parte alta
//	}
//	position = -1; // NOT FOUND
//	return false;
//} // End binarySearch



void Marc4cpp::testBinarySearch()
{
	bool retb;
//	printf ("Marc4cpp");
//	long position;

	// Reading test
	long offset = 100;
	FILE* Filep = fopen("C:/SbnWeb/migrazione/CFI/BNCF.take2/db_out_postgres/tb_titolo.out", "rb");

	unsigned char buf [200];
	retb = fseek(Filep, offset, SEEK_SET) >= 0;

	fread(buf, sizeof(buf), 1, Filep);

//	printf("Buffer: %s", buf);

	if (Filep != NULL)
		fclose(Filep);

	// Writing test

	CFile *cFile;
	cFile = new CFile("c:/tmp/marc4cpp.txt", AL_WRITE_FILE);

	cFile->Write("Hello Marc4cpp");
	delete cFile;

/*
	// Try the binary search
	retb = binarySearch(offsetTrTitTit, elementsTrTitTit, keyPlusOffsetPlusLfLength, "key0000010", 10, position);
	retb = binarySearch(offsetTrTitTit, elementsTrTitTit, keyPlusOffsetPlusLfLength, "key0000001", 10, position);
	retb = binarySearch(offsetTrTitTit, elementsTrTitTit, keyPlusOffsetPlusLfLength, "key0000005", 10, position);
	retb = binarySearch(offsetTrTitTit, elementsTrTitTit, keyPlusOffsetPlusLfLength, "key0000011", 10, position);
*/
}

// iterate over children only
void Marc4cpp::print_children(const tree<std::string>& tr)
{
	tree<std::string>::pre_order_iterator it=tr.begin();

	std::cout << "children of :" << *it << std::endl;

	tree<std::string>::sibling_iterator ch=tr.begin().begin(); //h1
	while(ch!=tr.end().end()) { // h1
	   std::cout << (*ch) << std::endl;
	   ++ch;
	   }
	std::cout << std::endl;
}
//tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end


void Marc4cpp::print_tree(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end)
   {
   if(!tr.is_valid(it)) return;
   int rootdepth=tr.depth(it);
   std::cout << "-----" << std::endl;
   while(it!=end) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << std::endl << std::flush;
      ++it;
      }
   std::cout << "-----" << std::endl;
   }

void Marc4cpp::print_tree_post(const tree<std::string>& tr, tree<std::string>::post_order_iterator it, tree<std::string>::post_order_iterator end)
   {
   int rootdepth=tr.depth(it);
   std::cout << "-----" << std::endl;
   while(it!=end) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << std::endl << std::flush;
      ++it;
      }
   std::cout << "-----" << std::endl;
   }

void Marc4cpp::print_tree_rev(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end)
   {
   --it;
   int rootdepth=0;//tr.depth(it);
   std::cout << "-----" << std::endl;
   while(1==1) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << std::endl;
      if(it==end) break;
      --it;
      }
   std::cout << "-----" << std::endl;
   }

void Marc4cpp::print_tree_rev_post(const tree<std::string>& tr, tree<std::string>::post_order_iterator it, tree<std::string>::post_order_iterator end)
   {
   --it;
   int rootdepth=0;
   std::cout << "-----" << std::endl;
   while(1==1) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << std::endl;
      if(it==end) break;
      --it;
      }
   std::cout << "-----" << std::endl;
   }





void Marc4cpp::testReticolo()
{
    tree<std::string> reticolo;
    //tree<std::string>::pre_order_iterator nodeIter;

    reticolo.set_head("1:TIT:ANA0021073");

    tree<std::string> reticolo2;
  	tree<std::string>::pre_order_iterator node;

    reticolo2.set_head("1:TIT:1.");

    node = reticolo2.append_child(reticolo2.begin(), "1:TIT:1.1");
           reticolo2.append_child(node, "1:TIT:1.1.1");

    node = reticolo2.append_child(reticolo2.begin(), "1:TIT:1.2");
    reticolo2.append_child(node, "1:TIT:1.2.1");

    node = reticolo2.append_child(reticolo2.begin(), "1:AUT:1");
    reticolo2.append_child(node, "1:AUT:1.1");

    print_tree(reticolo2, reticolo2.begin(), reticolo2.end());

//      reticolo2.erase(reticolo2.begin());
      reticolo2.clear(); // Will erase all the nodes but not delete user allocated objects.
      					 // So if strings are allocated it is your responsibility to delete them!!
      std::cout << "erased tree:" << std::endl;
      print_tree(reticolo2, reticolo2.begin(), reticolo2.end());

//      print_children(reticolo2);

}











/*
 * Premessa:
 * 		1. Il file degli offset al file delle relazioni degli authority file sono stati caricati (eg. tr_tit_aut.out.rel.srt.off)
 * 		2. I file di relazione deli authority sono stati aperti (eg. tr_tit_aut.out.rel.srt)
 */
tree<std::string> *Marc4cpp::creaReticolo(char * bid)
{
	CString *sPtr = new CString (2048); // 01/06/2010
	char buf[80];


    tree<std::string> * reticolo = new tree<std::string>();
  	sprintf (buf, "1:TIT:%s", bid);
    reticolo->set_head(buf); // root strdup(buf)
    CreaReticoloAddNodeChildren(reticolo, reticolo->begin(), bid, "1", 1);
    if (reticoliOut)
    	writeReticolo(*reticolo, reticolo->begin(), reticolo->end());
    delete sPtr;
    return reticolo;
} // End Marc4cpp::testCreaReticolo

void Marc4cpp::writeReticolo(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end)
   {
   if(!tr.is_valid(it))
	   return;

   string str;

   int rootdepth=tr.depth(it);
//   std::cout << "-----" << std::endl;
   reticoliOut->Write("\n-----\n");

   while(it!=end) {
//	  reticoliOut->Write("\nNode");
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         //std::cout << "  ";
    	 reticoliOut->Write("  ");

      str = *it;
      char *d = (char*)str.data();
      reticoliOut->Write(d);
      reticoliOut->Write('\n');

      //std::cout << (*it) << std::endl << std::flush;
      ++it;
      }
   }




void Marc4cpp::getEntityId(char *entryReticolo, char *bid)
{
	char *BufTailPtr, *aString;
	char *ptr = strchr(entryReticolo, ':');
#ifdef DEBUG_LEGAMI
	printf("\ngetEntityId: ptr=%s, bid_address=%p", ptr, bid);
#endif
	ptr = strchr(ptr+1, ':');
#ifdef DEBUG_LEGAMI
	printf("\ngetEntityId: ptr=%s", ptr);
	printf("\ngetEntityId: ptr+1=%s", ptr+1);

#endif

	BufTailPtr = bid;
	aString = ptr+1;
	MACRO_COPY_FAST(10);
//	memcpy (bid, ptr+1, 10);

//	ptr++;
//	for (int i=0; i < 10; i++)
//		*bid++ = *ptr++;

}









bool Marc4cpp::writeRecordUnimarc()
{
//	return marcStreamWriter->write(marcRecord);
	return bufferedMarcStreamWriter->write(marcRecord);
}

#undef DEBUG_ARGE

bool Marc4cpp::writeRecordUnimarcTxt()
{
	CString * recordTxt = marcRecord->toCString();
	this->marcOutTxt->Write(recordTxt->data(), recordTxt->Length()); // recordTxt, marcRecord->toStringLength()
#ifndef DEBUG_ARGE
	this->marcOutTxt->Write("\n\n");
#endif
	return true;
}




void Marc4cpp::dumpRecordUnimarcTxt()
{
	CString * recordTxt = marcRecord->toCString();
	//this->marcOutTxt->Write(recordTxt, marcRecord->toStringLength());
	//this->marcOutTxt->Write("\n\n");
	printf ("\n%s", recordTxt->data());
}













void Marc4cpp::marc4CppMallinfo()
{
	struct mallinfo mallinfo( );

   /* what is the largest ECB heap buffer currently available? */



	printf("\nMALLINFO after Marc4cpp::setup");
	printf("\n-------------------------------");
	printf("\nto do");

//	printf("\nMALLINFO total size of memory arena %d", mallinfo.arena);
//	printf("\nMALLINFO number of ordinary memory blocks %d", info.ordblks);
//	printf("\nMALLINFO space used by ordinary memory blocks  %d", info.uordblks);
//	printf("\nMALLINFO space free for ordinary blocks %d", info.fordblks);
//	printf("\nMALLINFO size of largest free block %d", info.maxfree);

	printf("\n-------------------------------");






}

















void logTabelleIni(CKeyValueVector *entitaKVvector)
{
//	CKeyValue *kv;
//	printf ("\n-----------");
//	for (int i=0; i < entitaKVvector->Length(); i++)
//	{
//		kv = (CKeyValue *)entitaKVvector->GetValue(i);
//		printf ("\nENTITA: %s, InMemory=%s",kv->Key.p, kv->Val.p);
//	}

//	for (int i=0; i < relazioniKVvector->Length(); i++)
//	{
//		kv = (CKeyValue *)relazioniKVvector->GetValue(i);
//		printf ("\nRELAZIONE: %s, InMemory=%s",kv->Key.p, kv->Val.p);
//		delete kv;
//	}
//
//
//	for (int i=0; i < offsetKVvector->Length(); i++)
//	{
//		kv = (CKeyValue *)offsetKVvector->GetValue(i);
//		printf ("\nIndice: %s, InMemory=%s",kv->Key.p, kv->Val.p);
//		delete kv;
//	}
}














/*
* log seek di lettura per capire quali file hanno maggior accesso per poterli caricare in memoria
*/
void Marc4cpp::logSeek()
{

	printf ("\nLOG DELLE SEEK SU FILE ENTITA IN RAM");
	for (int i=0; i<fileEntitaInMemVector.length(); i++)
		printf ("\n%10ld accesso/i per %s ", fileEntitaInMemVector.Entry(i)->getSeekCtr(), fileEntitaInMemVector.Entry(i)->GetName());

	printf ("\nLOG DELLE SEEK SU FILE RELAZIONI IN RAM");
	for (int i=0; i<fileRelazioniInMemVector.length(); i++)
		printf ("\n%10ld accesso/i per %s ", fileRelazioniInMemVector.Entry(i)->getSeekCtr(), fileRelazioniInMemVector.Entry(i)->GetName());

	printf ("\nLOG DELLE SEEK SU FILE SU DISCO");
	for (int i=0; i<fileOnDiscVector.length(); i++)
		printf ("\n%10ld accesso/i per %s ", fileOnDiscVector.Entry(i)->getSeekCtr(), fileOnDiscVector.Entry(i)->GetName());

//	printf ("\nLOG DELLE SEEK SU FILE (OFFSET) IN RAM");
//	for (int i=0; i<offsetFileVectorInMem.length(); i++)
//		printf ("\n%10ld accesso/i per %s ", offsetFileVectorInMem.Entry(i)->getSeekCtr(), offsetFileVectorInMem.Entry(i)->GetName());


	printf ("\nLOG DELLE SEEK SU FILE (OFFSET) SU DISCO");
	for (int i=0; i<offsetFileVectorOnDisc.length(); i++)
		printf ("\n%10ld accesso/i per %s ", offsetFileVectorOnDisc.Entry(i)->getSeekCtr(), offsetFileVectorOnDisc.Entry(i)->GetName());


} // End logSeek


bool Marc4cpp::setupCommon(
		char *tagsToGenerateBufferPtr,
		int tagsToGenerate,
		char *marcFilename,
		char *marcTxtFilename,
		char *marcXmlFilename,
//		char *polo,
//		char *descPolo,
		ATTValVector<CString *> *entitaVector,
		ATTValVector<CString *> *relazioniVector,
		ATTValVector<CString *> *offsetVector,
		ATTValVector<CString *> *codiciVector, // Mantis 5302
		char *bidXunimarcFilename,
		char *reticoliFilename,
		char *bidErratiFilename)
	{
	bool retb;

	fileEntitaInMemSize = 0;
	fileRelazioniInMemSize = 0;
	fileOffsetInMemSize = 0;

// Prtato in ZeroInit 02/08/16
//trTitAutRelInvIn=0; // 06/05/2013 mantis 5302
//trTitAutRelInvOffsetIn = 0;


	this->dictionaryVector = codiciVector;


	marcRecord = new MarcRecord();
	this->tagsToGenerateBufferPtr = tagsToGenerateBufferPtr;
	this->tagsToGenerate = tagsToGenerate;

	if (!apriFileEntita(entitaVector))
		return false;
	if (!apriFileRelazioni(relazioniVector))
		return false;
	if (!apriFileOffset(offsetVector))
		return false;

	try {
		printf ("\nAPRI FILE bids': %s", bidXunimarcFilename);
		idXunimarcIn = new CFile(bidXunimarcFilename, AL_READ_FILE);

		if (*reticoliFilename)
		{
		printf ("\nAPRI reticoliOut: %s", reticoliFilename);
			reticoliOut = new CFile(reticoliFilename, AL_WRITE_FILE);
		}
		if (*marcFilename) // solo se dichiarato
		{
			printf ("\nAPRI marcOut: %s", marcFilename);
			marcOut = new CFile(marcFilename, AL_WRITE_FILE);
		}

		if (*marcTxtFilename) // solo se dichiarato
		{
		printf ("\nAPRI marcOutTxt: %s", marcTxtFilename);
			marcOutTxt = new CFile(marcTxtFilename, AL_WRITE_FILE);
		}

		if (marcXmlFilename && *marcXmlFilename) // solo se dichiarato
		{
		printf ("\nAPRI marcOutXml: %s", marcXmlFilename);
			marcOutXml = new CFile(marcXmlFilename, AL_WRITE_FILE);
		}

		if (bidErratiFilename && *bidErratiFilename)
		{
		printf ("\nAPRI bid errati: %s", bidErratiFilename);
		bidErratiOut = new CFile(bidErratiFilename, AL_WRITE_FILE);
		}

	} catch (GenericError e) {
		std::cout << e.errorMessage;
		stop();
		return false;
	}

//	marcStreamWriter = new MarcStreamWriter(marcOut);
	bufferedMarcStreamWriter = new BufferedMarcStreamWriter(marcOut, MARKFILEOUT_BUFFER_SIZE, MARKFILEOUT_BUFFER_RESIZE_BY);

	// Carichiamo gli indici (file degli offsets)
	retb = loadOffsetFiles();

 	closeOffsetFiles();


 	// Diciamo quanta memoria abbiamo allocato per i file in memoria
//	printf ("\nRAM allocata per i file ENTITA': %0.2f mb", (float)(fileEntitaInMemSize / 1024000));
//	printf ("\nRAM allocata per i file RELAZIONE: %0.2f mb", (float)(fileRelazioniInMemSize / 1024000));
//	printf ("\nRAM allocata per i file OFFSET: %0.2f mb", (float)(fileOffsetInMemSize / 1024000));

	printf ("\nRAM allocata per i file ENTITA': %ld mb", (fileEntitaInMemSize / 1024000));
	printf ("\nRAM allocata per i file RELAZIONE: %ld mb", (fileRelazioniInMemSize / 1024000));
	printf ("\nRAM allocata per i file OFFSET: %lld mb", fileOffsetInMemSize / 1024000);


	printf ("\n\nFine Marc4cpp::setupCommon\n");
	return retb;
} // End Marc4cpp::setupCommon















bool Marc4cpp::writeRecordUnimarcXml()
{
//	this->marcOutXml->Write("Record XML\n");

	char * recordXml = marcRecord->toXml();
	this->marcOutXml->Write(recordXml, marcRecord->toXmlLength());


	return true;
}








void Marc4cpp::stop()
{
	// used for profiling
#ifdef LOG_SEEKS
	logSeek();
#endif

//	closeOffsetFiles(); //11/05/10
	if (codiciNotaKV)
		delete codiciNotaKV; // 03/05/2013 Mantis polo 5302
	if (codiciEclaKV)
		delete codiciEclaKV;

	if (codiciOrgaKV)
		delete codiciOrgaKV;

	if (variantiSinonimiaKsogVarKV)	// 15/01/2018
		delete variantiSinonimiaKsogVarKV;

	if (variantiSinonimiaKvarSogKV)	// 15/01/2018
		delete variantiSinonimiaKvarSogKV;


	if (variantiStoricheKsogVarKV)	// 23/03/2018
		delete variantiStoricheKsogVarKV;

	if (variantiStoricheKvarSogKV)	// 23/03/2018
		delete variantiStoricheKvarSogKV;


	if (scomposizioneSoggettoKV)	// 25/01/2018
		delete scomposizioneSoggettoKV;

	if (composto_non_preferito_ctr_KV)	// 29/01/2018
		delete composto_non_preferito_ctr_KV;

	if (compostoNonPreferitoKV)	// 28/02/2018
		delete compostoNonPreferitoKV;



	// open file and load codes

	// Rilascio delle tabelle
    if (tb950Coll)      delete tb950Coll;
	for (int i=0; i <tabelleVector.length(); i++)
	{
		//printf ("\nCHIUDI TABELLA: %s ", "??");
		CFile * filePtr = (CFile *)tabelleVector.Entry(i);
		delete (filePtr);
			// offsetFileVector.SetEntry(i, 0);
	}
	tabelleVector.Clear();
	if (marcRecord)	{		delete marcRecord;		marcRecord = 0;	}
    if (marc4cppCollocazione)       delete marc4cppCollocazione;
    if (marc4cppDocumento)      delete marc4cppDocumento;
    if (marc4cppLegami)     delete marc4cppLegami;
    if (marc4cppLegamiAuthority)     delete marc4cppLegamiAuthority;
    if (marc4cppAltriDoc)	delete marc4cppAltriDoc;
    if (marc4cppDocumentoAuthority) delete marc4cppDocumentoAuthority;
//	if (marcStreamWriter)		delete marcStreamWriter;
	if (bufferedMarcStreamWriter)		delete bufferedMarcStreamWriter;
	if (marcOut)		delete marcOut;
	if (marcOutTxt)		delete marcOutTxt;
	if (marcOutXml)		delete marcOutXml;

    if (idXunimarcIn)      delete idXunimarcIn;
	if (bidErratiOut)		delete bidErratiOut;
    if (tbfBibliotecaInPoloKV)
    {
    	TbfBibliotecaInPolo *tbfBibliotecaInPolo;
    	for (int i=0; i < tbfBibliotecaInPoloKV->Length(); i++)
    	{
    		tbfBibliotecaInPolo = (TbfBibliotecaInPolo *)tbfBibliotecaInPoloKV->GetValue(i);
//    		printf("\nBiblioteca %s", tbfBiblioteca->getField(tbfBiblioteca->nom_biblioteca));
    		delete tbfBibliotecaInPolo;
    	}
    	delete tbfBibliotecaInPoloKV;
    }

    if (tbcSezioneCollocazioneQuadreKV)
    {
    	tbcSezioneCollocazioneQuadreKV->Clear();
    	delete tbcSezioneCollocazioneQuadreKV;
    }




	closeOffsetFiles();



    // rilascia la memoria
	if (offsetBufferTbAutorePtr)        	free (offsetBufferTbAutorePtr);
    if (offsetBufferTbNumeroStandardPtr)    free (offsetBufferTbNumeroStandardPtr);
    if (offsetBufferTbTitoloPtr)        	free (offsetBufferTbTitoloPtr);
    if (offsetBufferTbfBibliotecaPtr)       free (offsetBufferTbfBibliotecaPtr);
    if (offsetBufferTrTitAutRelPtr)        	free (offsetBufferTrTitAutRelPtr);
    if (offsetBufferTrTitTitRelPtr)        	free (offsetBufferTrTitTitRelPtr);
    if (offsetBufferTrTitTitInvRelPtr)      free (offsetBufferTrTitTitInvRelPtr);
    if (offsetBufferTrAutAutInvPtr) 		free (offsetBufferTrAutAutInvPtr);

    if (reticoliOut)        delete reticoliOut;


    // Chiudiamo tutti i file che erano stati caricati in memoria
	for (int i=0; i <fileEntitaInMemVector.length(); i++)
	{
//		printf ("\nCHIUDI FILES IN MEMORY FILE: %s ", fileInMemVector.Entry(i)->GetName());
		CFile * filePtr = (CFile *)fileEntitaInMemVector.Entry(i);
		delete (filePtr);
			// offsetFileVector.SetEntry(i, 0);
	}
	fileEntitaInMemVector.Clear();

	for (int i=0; i <fileRelazioniInMemVector.length(); i++)
	{
//		printf ("\nCHIUDI FILES IN MEMORY FILE: %s ", fileInMemVector.Entry(i)->GetName());
		CFile * filePtr = (CFile *)fileRelazioniInMemVector.Entry(i);
		delete (filePtr);
			// offsetFileVector.SetEntry(i, 0);
	}
	fileRelazioniInMemVector.Clear();



    // Chiudiamo tutti i file che erano stati letti da disco
	for (int i=0; i <fileOnDiscVector.length(); i++)
	{
//		printf ("\nCHIUDI FILES ON DISC: %s ", fileOnDiscVector.Entry(i)->GetName());
		CFile * filePtr = (CFile *)fileOnDiscVector.Entry(i);
		delete (filePtr);
			// offsetFileVector.SetEntry(i, 0);
	}
	fileOnDiscVector.Clear();

    // Chiudiamo tutti i file che erano stati letti da disco
	for (int i=0; i <offsetFileVectorOnDisc.length(); i++)
	{
//		printf ("\nCHIUDI FILES ON DISC: %s ", fileOnDiscVector.Entry(i)->GetName());
		CFile * filePtr = (CFile *)offsetFileVectorOnDisc.Entry(i);
		delete (filePtr);
			// offsetFileVector.SetEntry(i, 0);
	}
	offsetFileVectorOnDisc.Clear();


} // End stop







bool Marc4cpp::elaboraLeader()
{
	Leader *leader;
//	char implDefined[2+1];
//	implDefined[2]=0;
	// LDR
	char chr;
		leader = marcRecord->getLeader();

		if (authority == AUTHORITY_DOCUMENTO)
		{
			// leader->setRecordLength(); 				// 5, 1/5to carattere. Valorizzato alla fine
			if (*(tbTitolo->getField(tbTitolo->fl_canc)) == 'S' || *(tbTitolo->getField(tbTitolo->fl_canc)) == 's')
			{
				leader->setRecordStatus('d');	// cancellato
				return true;
			}
			else
				leader->setRecordStatus('n');	// nuovo




				chr = *(tbTitolo->getField(tbTitolo->tp_record_uni)); // 09/02/2010 16.12 per risolvere il problema del null con le collane
//			if (chr)
			if (chr && chr != ' ') // 05/11/2010 14.52
				leader->setTypeOfRecord(chr); 	// pos 6
			else
//				leader->setTypeOfRecord('a'); // Default testo a stampa
				leader->setTypeOfRecord('u'); // 30/09/2010 Default collane CFI0001538 indice

			char livBib = *(tbTitolo->getField(tbTitolo->cd_natura));

			if (livBib < 'a' )
				livBib = livBib+0x20; // Minuscolizzazione

			if (livBib == 'w') // la posizione 7 della  guida non pu� essere w, ma la naura w in sbn diventa m
				livBib = 'm';
			else if (livBib == 'n') // titolo analitico (spoglio)
				livBib = 'a';
			else if (livBib == 'c') // il 'c' in unimarc sta per collezione fattizia in unimarc
				livBib = 's';

			else if (livBib == 'r') // collezione fattizia 21/07/2014
				livBib = 'c';



			leader->setLivelloBibliografico(livBib);

			leader->setLivelloGerarchico(livelloGerarchico);
			//leader->setCharCodingScheme_typeOfEntity(ENCODING_UTF8);				// 1, decimo

			int livelloAuthority = atoi (tbTitolo->getField(tbTitolo->cd_livello)); // 10/09/10
			if (livelloAuthority == 5)
				leader->setLivelloDiCodifica('1');
			else if (livelloAuthority > 5 && livelloAuthority < 72)
				leader->setLivelloDiCodifica('3');
			// else 72-97 defaults to ' ';

		}
		if (authority == AUTHORITY_AUTORI)
		{

//tbAutore->dumpRecord();

			if (*(tbAutore->getField(tbAutore->fl_canc)) == 'S' || *(tbAutore->getField(tbAutore->fl_canc)) == 's')
				leader->setRecordStatus('d');	// cancellato
			else
				leader->setRecordStatus('n');	// nuovo

			leader->setTypeOfRecord('x'); 	// 1, 6to carattere x=authority record
			leader->setLivelloBibliografico(' '); // undefined
			leader->setLivelloGerarchico(' '); // undefined

//			char tpNomeAut = *(tbAutore->getField(tbAutore->tp_nome_aut));
//			if (tpNomeAut != 'A' && tpNomeAut != 'B' && tpNomeAut != 'C' && tpNomeAut != 'D')
//				leader->setCharCodingScheme_typeOfEntity('a');				// 1, decimo
//			else
//				leader->setCharCodingScheme_typeOfEntity('b');				// 1, decimo
			leader->setPos9Undefined(BLANK);

			leader->setLivelloDiCodifica(' ');
			leader->setTipoDiCatalogazioneDescrittiva(' ');
		}
		else if (authority == AUTHORITY_SOGGETTI)
		{
			if (*(tbSoggetto->getField(tbSoggetto->fl_canc)) == 'S' || *(tbSoggetto->getField(tbSoggetto->fl_canc)) == 's')
				leader->setRecordStatus('d');	// cancellato
			else
				leader->setRecordStatus('n');	// nuovo

			leader->setTypeOfRecord('x'); 	// 1, 7mo carattere x=authority record
			leader->setLivelloBibliografico(' '); // undefined
			leader->setLivelloGerarchico(' '); // undefined
//			leader->setCharCodingScheme_typeOfEntity('J');		// 1, decimo
			leader->setPos9Undefined(BLANK);		// 1, decimo

			leader->setLivelloDiCodifica(' ');
			leader->setTipoDiCatalogazioneDescrittiva(' ');
		}


		//	leader->setEntryMap();						// 5, 21/24mo Predi default costruttoire
	return true;
} // End Marc4cpp::elaboraLeader











/*
Caso di notizia documento per legami 01. // (M, W, S)

	ritorno;
		0 - monografie senza livelli e periodici
			se non ha relazioni 01 M/S ad altri titoli e
			se non e' arrivo di legami 01 M/S

		1 - monografia superiore
		 	se non ha relazioni 01 M/S ad altri titoli ed e
			se e' arrivo di legami 01 M/W

		2 - monografia inferiore o intermedia
		 	se ha relazioni 01 M/S ad altri titoli


Presupposto:
	si usa la tr_tit_tit e le colonne bid_base e bid_coll dove bid_coll puo stare anche in bid_base



*/
char  Marc4cpp::elaboraLivelloGerarchicoNotizia(char *bid)
{
	// ----------------------------------------
	// 09/09/2010 14.51 Logica presa da indice
	// ----------------------------------------
	// Contiamo quanti bid collegati ci sono al bid da analizzare (da bid_coll)
	char livelloGerarchico = '?';

	int n1 = marc4cppLegami->contaBidColl(bid);
//printf ("\nbid_coll per %s = %d", bid, n1);

	// Cerchiamo il bid tra i bid di partenza (bid_base)
	CString * bidColl = new CString();

	bool retb = marc4cppLegami->getBidColl(bid, bidColl);

	// Esite almeno un bid base?
	int n2 = 0;
	if (retb)
	{
		n2 = marc4cppLegami->contaBidBase(bidColl->data());

//printf ("\nbid_base per %s = %d", bidColl->data(), n2);

//	    if (n2 == 0)
//	    	livelloGerarchico =  '3';
//        else
        	livelloGerarchico = LIVELLO_GERARCHICO_2_INTERMEDIA_O_INFERIORE; // '2'

	    delete bidColl;
// printf ("\nlivello gerarchioco = %c", livelloGerarchico);
	    return livelloGerarchico;
	} // end abbiamoBidBase

    // senza livelli
    if (n1 == 0 && n2 == 0)
    	livelloGerarchico = LIVELLO_GERARCHICO_0_SENZA_LIVELLI_E_PERIODICI; // '0'
//    else if (n1 > 0 && n2 > 0)
//    	livelloGerarchico = '3';
    else if (n1 > 0 && n2 == 0)
    	livelloGerarchico = LIVELLO_GERARCHICO_1_SUPERIORE; // '1'
    else
    	livelloGerarchico = LIVELLO_GERARCHICO_2_INTERMEDIA_O_INFERIORE; // '2'

    delete bidColl;
//    printf ("\nLIVELLO gerarchioco = %c", livelloGerarchico);


    return livelloGerarchico;


} // End elaboraLivelloGerarchicoNotizia





void Marc4cpp::trovaAltraFormaAccettata(const char *vid, tree<std::string>* reticolo, tree<std::string>::pre_order_iterator startNodeIter, const char* level, int leafId) {

	if (trAutAutInv->existsRecordNonUniqueDaIndice(vid))
	{
		char curLevel[80];
		char buf[80];
	  	tree<std::string>::pre_order_iterator nodeIter;

	  	while (trAutAutInv->loadNextRecordDaIndice(vid, true)) // inverted key
		{
//			trAutAutInv->dumpRecord();
			// Costruiamo il .rel equivalente


			sprintf (curLevel, "%s.%d", level,++leafId);

			sprintf (buf, "%s:AUT:%s,%s", curLevel, trAutAutInv->getField(trAutAutInv->vid_base), trAutAutInv->getField(trAutAutInv->tp_legame)); // Token

//printf (buf, "arge %s:AUT:%s", curLevel, Token);
			nodeIter = reticolo->append_child(startNodeIter, buf);


		} // end while
	}

} // End addAutoreFormaAccettata









/*
*	Aggiungiamo i figli e progenie (legami) di un titolo
*   TITOLO -> TITOLO
*          -> AUTORE
*          -> SOGGETTO
*          -> CLASSE
*          -> MARCHE
*          -> LUOGO
*
*   AUTORE -> AUTORE
*   SOGGETTO -> --
*   CLASSE   -> --
*   MARCHE   -> --
*   LUOGO    -> --
*/
void Marc4cpp::CreaReticoloAddNodeChildren(tree<std::string>* reticolo, tree<std::string>::pre_order_iterator startNodeIter, const char* bid, const char* level, int levelPos)
{
	bool retb;
	long position;
//	long offset;
	int offset; // 18/03/2014 32 bit

	CString *sPtr = new CString (4096); // 2048. Mantis 4996 17/05/2012
//	CString * sPtr = &FixedLengthLine;
//	FixedLengthLine.ResetNoResize();

	char buf[80];
	int leafId = 0;
	CTokenizer *Tokenizer = new CTokenizer("|");

	OrsChar *Token;
	char *BufTailPtr, *aString;

  	tree<std::string>::pre_order_iterator nodeIter;
	char childBid[10+1];
	childBid[10]=0; // eos
	char *entryPtr;
	char curLevel[80];
	CString entryFile;

//printf ("\nCreaReticoloAddNodeChildren bid=%s, level=%s, levelPos=%d", bid, level, levelPos);


	// Se trattasi di VID cerchiamo i legami autore/autore
	if (*(bid+3) == 'V')
	{

		if (offsetBufferTrAutAutRelPtr)
			retb = BinarySearch::search(offsetBufferTrAutAutRelPtr, elementsTrAutAutRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
		else
		{
			retb = BinarySearch::search(trAutAutRelOffsetIn, elementsTrAutAutRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
			entryPtr = entryFile.data();
		}
		if (retb)
		{
			// Questo bid ha legami ad altri titoli
			// Dalla posizione prendiamo l'offset
//			offset = atol (entryPtr+BID_KEY_LENGTH); // offsetBufferTrAutAutPtr+position
			if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
				//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
				offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

			else
				//offset = atol (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII
				offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

			// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
			trAutAutRelIn->SeekTo(offset);
			if (!sPtr->ReadLineWithPrefixedMaxSize(trAutAutRelIn))
		        SignalAnError(__FILE__, __LINE__, "read failed");

#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami aut_aut=%s", sPtr->data());
#endif


			// Splittiamo la riga negli n elementi che la compongono
			Tokenizer->Assign(sPtr->data());

//printf("\ns.data() = %s", s.data());

			Token = Tokenizer->GetToken(); // Remove root
	//		CString level = "1.";

			while(*(Token = Tokenizer->GetToken()))
			{
				sprintf (curLevel, "%s.%d", level,++leafId);

				sprintf (buf, "%s:AUT:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
				nodeIter = reticolo->append_child(startNodeIter, buf);

				// Crea i figli
//				memcpy(childBid,Token, 10);
//				CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel);
			}
	//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
		}

	// 12/10/2010 Cerchiamo il legami 04 ad autori con forma accettata

	trovaAltraFormaAccettata(bid, reticolo, startNodeIter, level, leafId);

	delete Tokenizer;
	delete sPtr;
	return;
	} // End if VID




	// Dal BID cerchiamo l'offset del file di relazione ai titoli

	if (offsetBufferTrTitTitRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitTitRelPtr, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitTitRelOffsetIn, elementsTrTitTitRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
		entryPtr = entryFile.data();
	}

	if (retb)
	{
		// Questo bid ha legami ad altri titoli
		// Dalla posizione prendiamo l'offset
//		offset = atol (entryPtr+BID_KEY_LENGTH); // offsetBufferTrTitTitPtr+position
		if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
			//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
			offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

		else
			offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

		// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
		trTitTitRelIn->SeekTo(offset);
		if (!sPtr->ReadLineWithPrefixedMaxSize(trTitTitRelIn))
	        SignalAnError(__FILE__, __LINE__, "read failed");

#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami tit_tit=%s", sPtr->data());
#endif

		// Splittiamo la riga negli n elementi che la compongono
		Tokenizer->Assign(sPtr->data());
		Token = Tokenizer->GetToken(); // Remove root
//		CString level = "1.";
		char *BufTailPtr, *aString;

		while(*(Token = Tokenizer->GetToken()))
		{
			sprintf (curLevel, "%s.%d", level,++leafId);

			sprintf (buf, "%s:TIT:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
			nodeIter = reticolo->append_child(startNodeIter, buf);

//			levelPos++;

			//printf ("\nargepf levelPos=%d  buf=%s",levelPos, buf);
			if (levelPos > 3) // Stop al terso livello
			{
				delete Tokenizer;
				delete sPtr;
				return;
			}
			// Crea i figli
			BufTailPtr = childBid;
			aString = Token;
			MACRO_COPY_FAST(10);
//			memcpy(childBid,Token, 10);
			CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel, levelPos+1); // levelPos

		}

//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
	}


    // Troviamo le relazioni agli autori
    // ---------------------------------

	if (offsetBufferTrTitAutRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitAutRelPtr, elementsTrTitAutRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitAutRelOffsetIn, elementsTrTitAutRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (retb)
	{ // Questo Bid ha legami ad autori

		// Dalla posizione prendiamo l'offset
//		offset = atol (entryPtr+BID_KEY_LENGTH); // offsetBuffertrTitAutRelPtr+position
		if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
			//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
			offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

		else
			offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

		// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
		trTitAutRelIn->SeekTo(offset);
		if (!sPtr->ReadLineWithPrefixedMaxSize(trTitAutRelIn))
	        SignalAnError(__FILE__, __LINE__, "read failed");

#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami tit_aut=%s", sPtr->data());
#endif


		// Splittiamo la riga negli n elementi che la compongono
		Tokenizer->Assign(sPtr->data());

		Token = Tokenizer->GetToken(); // Remove root

		while(*(Token = Tokenizer->GetToken()))
		{
			sprintf (curLevel, "%s.%d", level,++leafId);

//			sprintf (buf, "%s.%d:AUT:%s", level,++leafId, Token);
			sprintf (buf, "%s:AUT:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
			nodeIter = reticolo->append_child(startNodeIter, buf);

//			levelPos++;
//printf ("\nargepf buf=%s",buf);
			// Crea i legami agli autori
			BufTailPtr = childBid;
			aString = Token;
			MACRO_COPY_FAST(10);
//			memcpy(childBid,Token, 10);

			CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel, levelPos+1);
		}

//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
	}


    // Troviamo le relazioni ai soggetti
    // ---------------------------------
	if (offsetBufferTrTitSogBibPtr)
		retb = BinarySearch::search(offsetBufferTrTitSogBibPtr, elementsTrTitSogBib, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitSogBibOffsetIn, elementsTrTitSogBib, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (retb)
	{
		// Questo bid ha legami a soggetti
//		offset = atol (entryPtr+BID_KEY_LENGTH);
		if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
			//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
			offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

		else
			offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

		// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/soggetto
		trTitSogBibIn->SeekTo(offset);
		if (!sPtr->ReadLineWithPrefixedMaxSize(trTitSogBibIn))
	        SignalAnError(__FILE__, __LINE__, "read failed");

#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami tit_sogBib=%s", sPtr->data());
#endif


		// Splittiamo la riga negli n elementi che la compongono
		Tokenizer->Assign(sPtr->data());
		Token = Tokenizer->GetToken(); // Remove root

		while(*(Token = Tokenizer->GetToken()))
		{
			sprintf (curLevel, "%s.%d", level,++leafId);

			sprintf (buf, "%s:SOG:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
			nodeIter = reticolo->append_child(startNodeIter, buf);

//			levelPos++;
//printf ("\nargepf buf=%s",buf);

// 22/10/2010
//			// Crea i figli
//			memcpy(childBid,Token, 10);
//			CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel, levelPos+1);

		}
//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
	}

    // Troviamo le relazioni alle classi
    // ---------------------------------
	if (offsetBufferTrTitClaPtr)
		retb = BinarySearch::search(offsetBufferTrTitClaPtr, elementsTrTitCla, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitClaOffsetIn, elementsTrTitCla, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
		entryPtr = entryFile.data();
	}
	if (retb)
	{
		// Questo bid ha legami ad altri titoli
		// Dalla posizione prendiamo l'offset
//		offset = atol (entryPtr+BID_KEY_LENGTH);
		if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
			//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
			offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

		else
			offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

		// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/classe
		trTitClaIn->SeekTo(offset);
		if (!sPtr->ReadLineWithPrefixedMaxSize(trTitClaIn))
	        SignalAnError(__FILE__, __LINE__, "read failed");

#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami tit_cla=%s", sPtr->data());
#endif

		// Splittiamo la riga negli n elementi che la compongono
		Tokenizer->Assign(sPtr->data());
		Token = Tokenizer->GetToken(); // Remove root

		while(*(Token = Tokenizer->GetToken()))
		{
			sprintf (curLevel, "%s.%d", level,++leafId);

			sprintf (buf, "%s:CLA:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
			nodeIter = reticolo->append_child(startNodeIter, buf);

//			levelPos++;
//printf ("\nargepf buf=%s",buf);

// 22/10/2010
//			// Crea i figli
//			memcpy(childBid,Token, 10);
//			CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel, levelPos+1);
		}
//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
	}

    // Troviamo le relazioni alle marche
    // ---------------------------------
	if (offsetBufferTrTitMarRelPtr)
		retb = BinarySearch::search(offsetBufferTrTitMarRelPtr, elementsTrTitMarRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitMarRelOffsetIn, elementsTrTitMarRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
		entryPtr = entryFile.data();
	}
	if (retb)
	{
		// Questo bid ha legami ad altri titoli
		// Dalla posizione prendiamo l'offset
//		offset = atol (entryPtr+BID_KEY_LENGTH);
		if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
			//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
			offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

		else
			offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

		// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
		trTitMarRelIn->SeekTo(offset);
		if (!sPtr->ReadLineWithPrefixedMaxSize(trTitMarRelIn))
	        SignalAnError(__FILE__, __LINE__, "read failed");

#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami tit_mar=%s", sPtr->data());
#endif

		// Splittiamo la riga negli n elementi che la compongono
		Tokenizer->Assign(sPtr->data());
		Token = Tokenizer->GetToken(); // Remove root

		while(*(Token = Tokenizer->GetToken()))
		{
			sprintf (curLevel, "%s.%d", level,++leafId);

			sprintf (buf, "%s:MAR:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
			nodeIter = reticolo->append_child(startNodeIter, buf);
//			levelPos++;
//printf ("\nargepf buf=%s",buf);

// 22/10/2010
//			// Crea i figli
//			memcpy(childBid,Token, 10);
//			CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel, levelPos+1);
		}
//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
	}

    // Troviamo le relazioni ai luoghi
    // -------------------------------
	if (offsetBufferTrTitLuoPtr)
		retb = BinarySearch::search(offsetBufferTrTitLuoPtr, elementsTrTitLuo, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trTitLuoOffsetIn, elementsTrTitLuo, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
		entryPtr = entryFile.data();
	}
	if (retb)
	{
		// Questo bid ha legami ad altri titoli
		// Dalla posizione prendiamo l'offset
//		offset = atol (entryPtr+BID_KEY_LENGTH);
		if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
			//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
			offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

		else
			offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

		// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/titolo
		trTitLuoIn->SeekTo(offset);
		if (!sPtr->ReadLineWithPrefixedMaxSize(trTitLuoIn))
	        SignalAnError(__FILE__, __LINE__, "read failed");


#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami tit_luo=%s", sPtr->data());
#endif

		// Splittiamo la riga negli n elementi che la compongono
		Tokenizer->Assign(sPtr->data());
		Token = Tokenizer->GetToken(); // Remove root

		while(*(Token = Tokenizer->GetToken()))
		{
			sprintf (curLevel, "%s.%d", level,++leafId);

			sprintf (buf, "%s:LUO:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
			nodeIter = reticolo->append_child(startNodeIter, buf);

//			levelPos++;
//printf ("\nargepf buf=%s",buf);

// 22/10/2010
//			// Crea i figli
//			memcpy(childBid,Token, 10);
//			CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel, levelPos+1);


		}
//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
	} // End relazione aui luoghi



    // Troviamo le relazioni al tesauro 10/12/2015
    // -------------------------------------------
	if (offsetBuffertsTrsTerminiTitoliBibliotecheRelPtr)
		retb = BinarySearch::search(offsetBuffertsTrsTerminiTitoliBibliotecheRelPtr, elementstsTrsTerminiTitoliBibliotecheRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(trsTerminiTitoliBibliotecheOffsetRelIn, elementstsTrsTerminiTitoliBibliotecheRel, keyPlusOffsetPlusLfLength, bid, BID_KEY_LENGTH, position, &entryFile);
		entryPtr = entryFile.data();
	}
	if (retb)
	{
		// Questo bid ha legami ad altri titoli
		// Dalla posizione prendiamo l'offset
//		offset = atol (entryPtr+BID_KEY_LENGTH);
		if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 09/02/2015
			//memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
			offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015

		else
			offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII

		// Dall'offset del file delle relazioni andiamo a prendere la relazione titolo/classe
		trsTerminiTitoliBibliotecheRelIn->SeekTo(offset);
		if (!sPtr->ReadLineWithPrefixedMaxSize(trsTerminiTitoliBibliotecheRelIn))
	        SignalAnError(__FILE__, __LINE__, "read failed");

#ifdef DEBUG_LEGAMI
printf ("\n\nBid+Offset=%s", entryPtr);
printf ("\nLegami tit_cla=%s", sPtr->data());
#endif

		// Splittiamo la riga negli n elementi che la compongono
		Tokenizer->Assign(sPtr->data());
		Token = Tokenizer->GetToken(); // Remove root

		while(*(Token = Tokenizer->GetToken()))
		{
			sprintf (curLevel, "%s.%d", level,++leafId);

			sprintf (buf, "%s:TES:%s", curLevel, Token);
//printf ("\nCreaReticoloAddNodeChildren %s", buf);
			nodeIter = reticolo->append_child(startNodeIter, buf);

//			levelPos++;
//printf ("\nargepf buf=%s",buf);

// 22/10/2010
//			// Crea i figli
//			memcpy(childBid,Token, 10);
//			CreaReticoloAddNodeChildren(reticolo, nodeIter, childBid, curLevel, levelPos+1);
		}
//		print_tree(*reticolo, reticolo->begin(), reticolo->end());
	}





delete sPtr;
delete Tokenizer;

} // End Marc4cpp::CreaReticoloAddNodeChildren




bool Marc4cpp::CreaUnimarcBibliografico(int recordUnimarcSuSingolaRiga, long iniziaElaborazioneDaRiga, bool positionByOffset, long elaboraNRighe, int logOgniXRighe)
{
	long ctr=0;
	char chr;
	long iniziaDaCtr=0;
	long recordScritti=0;
	bool retb;
	time_t timeStart, timeEnd;
	time ( &timeStart );
	double diff, tmpDouble;
	int	ore, minuti, secondi;




//    if (DATABASE_ID == DATABASE_INDICE && TIPO_SCARICO == TIPO_SCARICO_OPAC)
//    {
//    	// Apriamo i file accessori
//    	try {
//
//
//    		printf ("\nAPRI FILE %s", titoliCancellati.data());
//    		titoliCancellatiOut = new CFile(titoliCancellati.data(), AL_WRITE_FILE);
//
//    		printf ("\nAPRI FILE %s", titoliFusi.data());
//    		titoliFusiOut = new CFile(titoliFusi.data(), AL_WRITE_FILE);
//
//    		printf ("\nAPRI FILE %s", titoliLocDaCanc.data());
//    		titoliLocDaCancOut = new CFile(titoliLocDaCanc.data(), AL_WRITE_FILE);
//
////    		printf ("\nAPRI FILE %s", titoliUnimarc.data());
////    		titoliUnimarcOut = new CFile(titoliUnimarc.data(), AL_WRITE_FILE);
//
//    		printf ("\nAPRI FILE %s", titoliUnimarcRidotto.data());
//    		titoliUnimarcRidottoOut = new CFile(titoliUnimarcRidotto.data(), AL_WRITE_FILE);
//
//    		printf ("\nAPRI FILE %s", titoliVariati.data());
//    		titoliVariatiOut = new CFile(titoliVariati.data(), AL_WRITE_FILE);
//
//    	} catch (GenericError e) {
//    		std::cout << e.errorMessage;
//    		stop();
//    		return false;
//    	}
//
//    }




	CString *sPtr = new CString(2048);
	this->recordUnimarcSuSingolaRiga =  recordUnimarcSuSingolaRiga;

	if (iniziaElaborazioneDaRiga > 0)
		printf ("\nInizio elaborazione da riga %ld", iniziaElaborazioneDaRiga);


	if (iniziaElaborazioneDaRiga > 0 && positionByOffset)
	{
		long offset = (iniziaElaborazioneDaRiga-1)*(BID_LENGTH+1);
		idXunimarcIn->SeekTo(offset); // marc4cpp->
		iniziaDaCtr = iniziaElaborazioneDaRiga;
	}


	if (marcOutXml)
		marcOutXml->Write(
				//"<collection xsi:schemaLocation=\"http://www.bncf.firenze.sbn.it/unimarc/slim ./unimarcslim.xsd\">"
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<collection xmlns=\"http://www.bncf.firenze.sbn.it/unimarc/slim\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.bncf.firenze.sbn.it/unimarc/slim ./unimarcslim.xsd\">" // 19/04/2013
		);

	printf ("\nLog ogni %d record", logOgniXRighe+1);

	while(sPtr->ReadLine(idXunimarcIn)) // WithPrefixedMaxSize
	{

		chr = sPtr->GetFirstChar();
//		if (chr == '#' || chr == '\n' || chr == ' ')
		if (chr == '#' || sPtr->IsEmpty()) // 31/08/2015
			continue;

		iniziaDaCtr++;
		if (iniziaElaborazioneDaRiga && iniziaDaCtr < iniziaElaborazioneDaRiga)
			continue;

//printf ("\nargepf Elaborazione bid %s", sPtr->data());

		if (!logOgniXRighe || (!(ctr & logOgniXRighe)))
		{
			time ( &timeEnd );

			// Stampiamo il tempo richiesto per esportare il blocco di dati
			// Calcoliamo il tempo impegato per il trattamento di un blocco.
			// Serve a capire se l'elaborazione e' lineare o meno


			diff = difftime (timeEnd,timeStart); // %.2lf

			// Gestiamo solo fino a livello di secondi
			ore 	= diff/3600;
			minuti 	= ((int)diff/60)%60;
			secondi = (int)diff%60;

			printf ("\nElaborati %ld record (impiegato %0.2d:%0.2d:%0.2d ), elaborando bid: %s", ctr, ore, minuti, secondi, sPtr->data());

			timeStart = timeEnd;

//			if (!sPtr->Compare("RML0231063\n"))
//				printf ("break");

		}
		ctr++;

		if (sPtr->Length() > 10)
			retb = creaRecordUnimarcBibliografico(sPtr->SubstringData(0,10));
		else
			retb = creaRecordUnimarcBibliografico(sPtr->data());

		if (retb == true)
		{
			recordScritti++;
		}
		else
		{
//			printf ("\nRecord SCARTATO (non scritto) %ld, %s", ctr, sPtr->data());
			if (bidErratiOut)
			{
				bidErratiOut->Write(sPtr->SubstringData(0,10));
				bidErratiOut->Write('\n');
			}
		}

		if (elaboraNRighe && (ctr == elaboraNRighe))
			break;
	} // End while reading bids
	if (marcOutXml)
		marcOutXml->Write("\n</collection>");


	printf ("\n\nIniziata elaborazione da riga %ld", iniziaElaborazioneDaRiga);
	printf ("\nTotale records letti %ld", ctr);
	printf ("\nTotale records scritti %ld", recordScritti);
	printf ("\nTotale records non documento %ld", recordsNonDocumento);
	long errati =  ctr - (recordScritti + recordsNonDocumento);
	printf ("\nTotale records errati %ld", errati);

	printf ("\nNumero di scritture su markfileout = %ld ", bufferedMarcStreamWriter->getWritesCounter()+1);

	printf ("\nChecksum (errati + recordsNonDocumento + recordScritti)%ld = record letti %ld", errati + recordsNonDocumento + recordScritti, ctr);

	delete sPtr;
	return true;


//    if (DATABASE_ID == DATABASE_INDICE && TIPO_SCARICO == TIPO_SCARICO_OPAC)
//    {
//    	// Chiudiamo i file accessori i file accessori
//		delete titoliCancellatiOut;
//		delete titoliFusiOut;
//		delete titoliLocDaCancOut;
////		delete titoliUnimarcOut;
//		delete titoliUnimarcRidottoOut;
//		delete titoliVariatiOut;
//    }

} // End CreaUnimarcBibliografico



bool Marc4cpp::creaRecordBibliografico(MarcRecord *marcRecord, const tree<std::string>& reticolo, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end)
{
//	tree<std::string>::pre_order_iterator localIterator;


	this->marcRecord = marcRecord;
    char bid[10+1];
    bid[10]=0;
	string str;
	char natureValide[20];

	char* entryReticolo;
    str = *it;
    entryReticolo = (char*)str.data();
#ifdef DEBUG_LEGAMI
    printf("\ncreaRecordBibliografico: PRIMA entryReticolo=%s, bid=%sm bid_address=%p", entryReticolo, bid, bid);
#endif
    getEntityId(entryReticolo, bid);

#ifdef DEBUG_LEGAMI
    printf("\ncreaRecordBibliografico: DOPO entryReticolo=%s, bid=%s", entryReticolo, bid);
#endif

    if (!tbTitolo->loadRecord(bid))
    	return false;

//tbTitolo->dumpRecord();

    char natura = *tbTitolo->getField(tbTitolo->cd_natura);

//    if (DATABASE_ID == DATABASE_INDICE) //Roveri 20101008 e test su indice non scaricano le nature A
//    	strcpy (natureValide, "mMwWsSnNcCaA");
//    else
//    	strcpy (natureValide, "mMwWsSnNcC");


//    if (DATABASE_ID == DATABASE_INDICE)
//      	strcpy (natureValide, "mMwWsSnNcC");
//    else
//    	strcpy (natureValide, "mMwWsSnNcCrR"); // R=Raccolta fattizia 21/07/2014 (Calogiuri mail 08/07/14)
    strcpy (natureValide, estrai_nature.data());



//    char *natureValide = "mMwWsSnNcC";
//    char *natureValide = "mMwWsSnNcCaA"; // Per indice etichetta 928,929

    // SCARICARE SOLO TITOLI per record Documento (natura M, S, W, N, C)
//    if (natura != 'M' && natura != 'W' && natura != 'S' && natura != 'N' && natura != 'C')
    if (!strchr(natureValide, natura)) // 26/02/10
    	{
    	if (logNaturaErrata)
    		SignalAWarning(__FILE__, __LINE__, "Record non documento per bid %s, natura %c", bid, natura);
		recordsNonDocumento++;
		return false;
    	}

    livelloGerarchico = elaboraLivelloGerarchicoNotizia(bid);
//

//tbNumeroStandard->dumpRecord();

	if (!elaboraLeader())
		return false;



//	if (marcRecord->getLeader()->getRecordStatus() == 'd')
//		return true;

	marc4cppDocumento->elaboraDatiDocumento();

	// Mettiamo il test dopo per avere un minimo di informazioni sul record
	if (marcRecord->getLeader()->getRecordStatus() == 'd')
		if (!stampaRecordCancellato) // 02/02/2018
			return true; // Non stampare record cancellato


	if (TIPO_UNIMARC == TIPO_UNIMARC_RIDOTTO)
		marc4cppLegami->creaLegamiTitoloBiblioteca();
	else
	{
		marc4cppLegami->elaboraDatiLegamiAlDocumento(reticolo);
		marc4cppCollocazione->elaboraDatiCollocazione(bid, livelloGerarchico);
		marc4cppDocumento->elaboraAltriDatiDocumento();

#ifndef DEBUG_ARGE

		// Rimossa su richiesta di Maria Pia Barbieri il 27/01/2016 per segnalazioni interne!!!
		//ricostruisci200perM20(bid);	// 30/06/2015 Solo quando la 200 e' presente


		if (POLO.isEqual("INDICE") && TIPO_SCARICO == TIPO_SCARICO_OPAC)
			ricostruisci300_con_321();

	ricostruisci101();

#endif

	// 15/02/2018
	if (check899)
	{
	// Controlliamo che ci sia almeno una 899
		DataField *df_899 =  marcRecord->getDataField((char *)"899");
		if (!df_899)
		{
			printf ("Non esiste una localizzazione (899) per il bid %s", bid);
		}
	}

	}

	return true;
} // End Marc4cpp::creaRecordBibliografico



// 09/06/2015 Evolutiva 27/05/2015
#define TRUNCATE_FROM 150 // 150 default
void Marc4cpp::ricostruisci200perM20(char *bid)
{
	// 09/06/2015 Evolutiva 27/05/2015
	Leader *ldr = marcRecord->getLeader();
	//if (// DATABASE_ID == DATABASE_INDICE
	DataField *df200 = marcRecord->getDataField((char *)"200");

	if (POLO.isEqual("INDICE") // DATABASE_ID == DATABASE_INDICE // POLO == POLO_INDICE
		&&	ldr->getLivelloBibliografico() == 'm'
		&&	ldr->getLivelloGerarchico() == '2'
		&& df200->getIndicator1() == '0'	// 17/06/2015
	)
	{
		//		printf ("Ricostruisci la 200");
//		DataField *df461 = marcRecord->getDataField("461");
		DataField *df46x = marcRecord->getDataField((char *)"461");
		if (!df46x)
			df46x = marcRecord->getDataField((char *)"462");
		if(df46x)
		{
			DataField *df200 = marcRecord->getDataField((char *)"200");
			CString a200;
			if (df200->getSubfield('a')) // $a non sempre presente AQ10096838
			{
				a200.assign(df200->getSubfield('a')->getData());
				a200.ChangeTo(NSB.Data(),(char *)"");
				a200.ChangeTo(NSE.Data(),(char *)"");
			}
			CString nuovaA200(NSB.Data());
			nuovaA200.AppendChar('[');
//			nuovaA200.AppendChar('{'); // 02/10/15


			ATTValVector <Subfield *> *sfv;
			Subfield *sf;
		    // Copiamo i subfields
		    sfv = df46x->getSubfields();
		    bool in200 = false;
		    int aCtr=0;
		    bool ultimaA = false;
		    for (int i=0; i < sfv->length(); i++)
		    {
				// Controlliamo che sia una $1 200
		    	sf = sfv->Entry(i);
		    	if (sf->getCode() == '1' && sf->getDataString()->StartsWith("200"))
		    	{
		    		in200 = true;
		    		continue;
		    	}
		    	if (in200 )
		    	{
		    		if (sf->getCode() == 'a')
		    		{
		    		// E' l'ultima $a ? Vediamo il sottocampo successivo se esiste
		    		if ((i+1) < sfv->length())
		    		{
		    			if(sfv->Entry(i+1)->getCode() != 'a')
		    			{
			    			ultimaA = true;
		    			}
		    		}
		    		else
		    		{
		    			ultimaA = true;
		    		}

		    		// E' la prima $a?
	    			if(sfv->Entry(i-1)->getCode() != 'a')
	    			{
//		    			df200->getSubfield('a')->setData(sf->getDataString());	// Si
	    				CString s (sf->getData());
	    				s.ChangeTo(NSB.Data(), (char *)"");
	    				s.ChangeTo(NSE.Data(), (char *)"");
	    				nuovaA200.AppendString(&s);
	    			}
    			else
    			{
	    				//df200->insertSubfield(aCtr, new Subfield('a', sf->getDataString()));
	    				nuovaA200.AppendString((char *)" ; ");
	    				nuovaA200.AppendString(sf->getDataString());
    			}
		    		aCtr++;

		    		if(ultimaA)
		    		{


		    			// Abbiamo una $e nella 200?
		    		    for (int j=i+1; i < sfv->length(); i++)
		    		    {
		    		    	sf = sfv->Entry(i);
			    			if (sf->getCode() == '1') // fine 200
			    				break;
			    			if (sf->getCode() == 'e')
			    			{
				    			nuovaA200.AppendString((char *)" : ");
				    			nuovaA200.AppendString(sf->getData());
				    			break;
			    			}
		    		    }


						if (bid[3] == 'E' && (nuovaA200.Length() > TRUNCATE_FROM))	// Evolutiva mail Barbieri 17/06/15
						{
							//nuovaA200.CropRightFrom(150);
							// Troncare solo per antico a partire da TRUNCATE_FROM fino al primo separatore escluso
							nuovaA200.CropRightAfterChar(TRUNCATE_FROM, (char *)" ,.?!;:"); // 07/07/2015 Richiesta verbale Roveri


							nuovaA200.AppendString((char *)"... ");
						}

	    				nuovaA200.AppendString((char *)"] ");
//	    				nuovaA200.AppendString("} "); // 02/10/15
		    			nuovaA200.AppendString(NSE.Data());

		    			nuovaA200.AppendString(a200.Data());
	    				if (df200->getSubfield('a'))
	    					df200->getSubfield('a')->setData(&nuovaA200);
	    				else
	    					df200->insertSubfield(0, new Subfield('a', &nuovaA200));
//	    				// Gestiamo la $n e $p
//	    				long pos = a200.First(':');
//	    				if (pos > -1)
//	    				{
//
//	    					df200->addSubfield(new Subfield('h', a200.data(), pos));
//	    					df200->addSubfield(new Subfield('i', a200.data()+pos, a200.Length()-pos));
//	    				}
//	    				else
//	    				{
//	    					df200-> addSubfield(new Subfield('h', &nuovaA200));
//
//	    				}

	    				break;
		    		} // end if ultima A
		    		} // end if code = a
		    	} // end if in 200
		    } // end for sfv
		} // end if 461 o 461
	}

} // End Marc4cpp::ricostruisci200perM20





bool Marc4cpp::creaRecordUnimarcBibliografico(char *bid)
{
	tree<std::string>  *reticolo = 0;

	try {
		marcRecord->clear();
		try{
			reticolo = creaReticolo(bid); // ANA0021073 CFI0167118 , CRME000399, VEA0076138, CFI0028192, ANA0021073, "CFI0028192"
		} catch (GenericError e) {
			std::cout << e.errorMessage;
			return false;
		}

		if (creaRecordBibliografico(marcRecord, *reticolo, reticolo->begin(), reticolo->end()))
		{
			// Non scrivere record cancellato se scarico per OPAC indice
//			if (marcRecord->getLeader()->getRecordStatus() == 'd' && TIPO_SCARICO == TIPO_SCARICO_OPAC)
			if (marcRecord->getLeader()->getRecordStatus() == 'd' && (TIPO_SCARICO == TIPO_SCARICO_OPAC || !stampaRecordCancellato))
			{
				delete reticolo;
				return false; // Altrimenti me lo segnala come record scritto
			}
			else
			{

//				if (marcOut)
					marcRecord->sortDataFields();

				if (marcOutTxt)
					writeRecordUnimarcTxt();

				if (marcOutXml)
					writeRecordUnimarcXml();

//				if (!writeRecordUnimarc())
				if (marcOut && !writeRecordUnimarc()) // 13/11/15 non rendere obbligatoria la scrittuura del .mrc
				{
					printf ("\nERRORE writeRecordUnimarc() per bid %s", bid);
					delete reticolo;
					return false;
				}
//				if (recordUnimarcSuSingolaRiga)
//					this->marcOut->Write("\n");

			}

		}
		else
		{
			delete reticolo;
			return false;
		}

	} catch (GenericError e) {
		std::cout << e.errorMessage;
		if (reticolo)
			delete reticolo;
		return false;
	}


	delete reticolo;
	return true;

 } // End Marc4cpp::creaRecordUnimarcBibliografico


// 03/05/2013 Bug mabtis polo 5302
//bool Marc4cpp::loadCNOTcodes()
bool Marc4cpp::loadDictionary_KV(const char* filename, CKeyValueVector * kvVector)
{
	CFile* idXunimarcIn = 0; // File dei bid da trattare
	CString * sPtr;
	cIni ini;
	char * tabella;
	char * nomeFile;

	int i=0;
	for (; i < dictionaryVector->Length(); i++)
	{
		sPtr = dictionaryVector->Entry(i);
		ini.SplitIniFields(sPtr->data());
//		ini.SplitIniFields(sPtr->data(), true); // 11/03/2016
		tabella = ini.fieldsVector->Entry(1)->data();
		nomeFile = ini.fieldsVector->Entry(2)->data();
		if (!strcmp(tabella, filename )) // "tb_codici_cnot"
		{
//			printf ("\nCaricamento in memoria del FILE CODICI': %s = %s", tabella, nomeFile);
			printf ("\nCaricamento in memoria del DIZIONARIO': %s = %s", tabella, nomeFile);
			idXunimarcIn = new CFile(nomeFile, AL_READ_FILE);
			break;
		}
	}
	if (i == dictionaryVector->Length()) // Cercando di aprire un file non definito nel file di configurazione
		return false;

	if (!idXunimarcIn)
	{
//		printf ("\nNon posso aprire file codici per %s", filename);
		printf ("\nNon posso aprire file dizionairo per %s", filename);
		return false;
	}
	const char *recordSeparator = "&$%";
	ATTValVector<CString*> fieldsVector;
	int tbFields=2;
int rows_ctr=0;
	while(sPtr->ReadLine(idXunimarcIn)) // WithPrefixedMaxSize
	{
rows_ctr++;
		CTokenizer Tokenizer(sPtr->Data(), recordSeparator);
		OrsChar *tokenPtr;
		long tokenLength;

		int i=0;
		//fieldsVector.DeleteAndClear();
		while(Tokenizer.GetToken(&tokenPtr, &tokenLength) && i < tbFields)
		{
			if(tokenLength)
			{
				CString *sPtr = new CString (tokenPtr, tokenLength);
				sPtr->Strip(CString::trailing, ' ');
				fieldsVector.Add(sPtr); //, tokenLength
//				fieldsVector.Last()->Strip(CString::trailing, ' '); // remove trailing spaces
			}
		} // End while tokenizer

		fieldsVector.Last()->Strip(CString::trailing, '\n');
	} // end while readline

	// Ora che abbiamo i due campi (codice/descrizione)
	if (kvVector->ValType == tKVINT)
	{
		for (int i=0; i < fieldsVector.length(); i+=2)
			kvVector->Add(fieldsVector.Entry(i)->data(), atoi (fieldsVector.Entry(i+1)->data()));
	}
	else
	{
		for (int i=0; i < fieldsVector.length(); i+=2)
			kvVector->Add(fieldsVector.Entry(i)->data(), fieldsVector.Entry(i+1)->data());
	}
	fieldsVector.DeleteAndClear();
	delete idXunimarcIn;
	return true;

	//fieldsVector.DeleteAndClear();
} // End loadTbcodici_KV //loadCNOTcodes



bool Marc4cpp::setupAuthDocument(
		char *tagsToGenerateBufferPtr,
		int tagsToGenerate,
		char *marcFilename,
		char *marcTxtFilename,
		char *marcXmlFilename,
		char *polo,
		char *descPolo,
		char *bibliotecaRichiedenteScarico,
		ATTValVector<CString *> *entitaVector,
		ATTValVector<CString *> *relazioniVector,
		ATTValVector<CString *> *offsetVector,
		ATTValVector<CString *> *codiciVector,
		char *bidXunimarcFilename,
		char *reticoliFilename,
		char *bidErratiFilename,
		CKeyValueVector *sezioniDiCollocazioneDaNonMostrareIn960KV,
		CKeyValueVector *bibliotecheDaNonMostrareIn950KV)
	{
	bool retb;


	this->sezioniDiCollocazioneDaNonMostrareIn960KV = sezioniDiCollocazioneDaNonMostrareIn960KV;
	this->bibliotecheDaNonMostrareIn950KV = bibliotecheDaNonMostrareIn950KV;

	authority = AUTHORITY_DOCUMENTO;

	retb = setupCommon(tagsToGenerateBufferPtr,
			tagsToGenerate,
			marcFilename,
			marcTxtFilename,
			marcXmlFilename,
			entitaVector, relazioniVector, offsetVector, codiciVector,
			bidXunimarcFilename,
			reticoliFilename,
			bidErratiFilename);
	if (!retb)
		return retb;


	// Carichiamo i le tabelle dei codici
	composto_non_preferito_ctr_KV = new CKeyValueVector(tKVSTRING, tKVINT);	// 24/01/2018
	loadDictionary_KV("tb_composto_np_sog", composto_non_preferito_ctr_KV);

	variantiSinonimiaKsogVarKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 15/01/2018
	loadDictionary_KV("tb_variante_sinonimia_sog", variantiSinonimiaKsogVarKV);

	variantiSinonimiaKvarSogKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 22/02/2018
	loadDictionary_KV("tb_variante_sinonimia_var", variantiSinonimiaKvarSogKV);


	variantiStoricheKsogVarKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 15/01/2018
	loadDictionary_KV("tb_variante_storica_sog", variantiStoricheKsogVarKV);

	variantiStoricheKvarSogKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 22/02/2018
	loadDictionary_KV("tb_variante_storica_var", variantiStoricheKvarSogKV);



	scomposizioneSoggettoKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 24/01/2018
	loadDictionary_KV("tb_scomposizioni_sog", scomposizioneSoggettoKV);

	compostoNonPreferitoKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 24/01/2018
	loadDictionary_KV("tb_scomposizioni_cnp", compostoNonPreferitoKV);



	codiciNotaKV = new CKeyValueVector(tKVSTRING, tKVSTRING);
	loadDictionary_KV("tb_codici_cnot", codiciNotaKV);

	codiciEclaKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 12/2014
	loadDictionary_KV("tb_codici_ecla", codiciEclaKV);

	codiciOrgaKV = new CKeyValueVector(tKVSTRING, tKVSTRING);	// 26/01/2015
	loadDictionary_KV("tb_codici_orga", codiciOrgaKV);




	tb950Coll = new Tb950Coll (tb950CollOutFilenameIn, tb950CollOutOffsetBidFilenameIn, tb950CollOutOffsetKLocFilenameIn, offsetBufferTb950CollBidPtr, offsetBufferTb950CollKLocPtr, elementsTb950BidColl,elementsTb950KeylocColl, BID_KEY_LENGTH, KEYLOC_KEY_LENGTH, offsetPlusLfLength); //  OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(tb950Ese = new Tb950Ese (tb950EseOutFilenameIn, tb950EseOutOffsetFilenameIn, offsetBufferTb950EsePtr, elementsTb950Ese, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tb950Inv = new Tb950Inv (tb950InvOutFilenameIn, tb950InvOutOffsetFilenameIn, offsetBufferTb950InvPtr, elementsTb950Inv, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbaOrdini = new TbaOrdini(tbaOrdiniIn, tbaOrdiniOffsetIn, offsetBufferTbaOrdiniPtr, elementsTbaOrdini, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbAutore = new TbAutore(tbAutoreIn, tbAutoreOffsetIn, offsetBufferTbAutorePtr, elementsTbAutore, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbCartografia = new TbCartografia(tbCartografiaIn, tbCartografiaOffsetIn, offsetBufferTbCartografiaPtr, elementsTbCartografia, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbClasse = new TbClasse(tbClasseIn, tbClasseOffsetIn, offsetBufferTbClassePtr, elementsTbClasse, classeKeyPlusOffsetPlusLfLength, CLASSE_KEY_LENGTH));
    tabelleVector.Add(tbcNotaInv = new TbcNotaInv(tbcNotaInvIn, tbcNotaInvOffsetIn, offsetBufferTbcNotaInvPtr, elementsTbcNotaInv, notaInvKeyPlusOffsetPlusLfLength, INVENTARIO_KEY_LENGTH)); // INVENTARIO_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(tbComposizione = new TbComposizione(tbComposizioneIn, tbComposizioneOffsetIn, offsetBufferTbComposizionePtr, elementsTbComposizione, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbcPossessoreProvenienza = new TbcPossessoreProvenienza(tbcPossessoreProvenienzaIn, tbcPossessoreProvenienzaOffsetIn, offsetBufferTbcPossessoreProvenienzaPtr, elementsTbcPossessoreProvenienza, keyPlusOffsetPlusLfLength, PID_KEY_LENGTH));
    tabelleVector.Add(tbcSezioneCollocazione = new TbcSezioneCollocazione(tbcSezioneCollocazioneIn, tbcSezioneCollocazioneOffsetIn, offsetBufferTbcSezioneCollocazionePtr, elementsTbcSezioneCollocazione, TBC_SEZIONE_COLLOCAZIONE_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH, TBC_SEZIONE_COLLOCAZIONE_KEY_LENGTH));
    tabelleVector.Add(tbfBiblioteca = new TbfBiblioteca(tbfBibliotecaIn, tbfBibliotecaOffsetIn, offsetBufferTbfBibliotecaPtr, elementsTbfBiblioteca, bibliotecaKeyPlusOffsetPlusLfLength, BIBLIOTECA_KEY_LENGTH)); // BIBLIOTECA_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(tbGrafica = new TbGrafica(tbGraficaIn, tbGraficaOffsetIn, offsetBufferTbGraficaPtr, elementsTbGrafica, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbImpronta = new TbImpronta(tbImprontaIn, tbImprontaOffsetIn, offsetBufferTbImprontaPtr, elementsTbImpronta, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbIncipit = new TbIncipit(tbIncipitIn, tbIncipitOffsetIn, offsetBufferTbIncipitPtr, elementsTbIncipit, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbLuogo = new TbLuogo(tbLuogoIn, tbLuogoOffsetIn, offsetBufferTbLuogoPtr, elementsTbLuogo, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbMarca = new TbMarca(tbMarcaIn, tbMarcaOffsetIn, offsetBufferTbMarcaPtr, elementsTbMarca, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbMusica = new TbMusica(tbMusicaIn, tbMusicaOffsetIn, offsetBufferTbMusicaPtr, elementsTbMusica, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));


    tabelleVector.Add(tbAudiovideo = new TbAudiovideo(tbAudiovideoIn, tbAudiovideoOffsetIn, offsetBufferTbAudiovideoPtr, elementsTbAudiovideo, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbDiscoSonoro = new TbDiscoSonoro(tbDiscoSonoroIn, tbDiscoSonoroOffsetIn, offsetBufferTbDiscoSonoroPtr, elementsTbDiscoSonoro, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));

    tabelleVector.Add(tbNota = new TbNota(tbNotaIn, tbNotaOffsetIn, offsetBufferTbNotaPtr, elementsTbNota, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
if (tbNota300In)
	tabelleVector.Add(tbNota300 = new TbNota300(tbNota300In, tbNota300OffsetIn, offsetBufferTbNota300Ptr, elementsTbNota300, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
if (tbNota321In)
	tabelleVector.Add(tbNota321 = new TbNota321(tbNota321In, tbNota321OffsetIn, offsetBufferTbNota321Ptr, elementsTbNota321, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));


    tabelleVector.Add(tbNumeroStandard = new TbNumeroStd(tbNumeroStandardIn, tbNumeroStandardOffsetIn, offsetBufferTbNumeroStandardPtr, elementsTbNumeroStandard, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbPersonaggio = new TbPersonaggio(tbPersonaggioIn, tbPersonaggioOffsetIn, offsetBufferTbPersonaggioPtr, elementsTbPersonaggio, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbRappresent = new TbRappresent(tbRappresentIn, tbRappresentOffsetIn, offsetBufferTbRappresentPtr, elementsTbRappresent, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tbRepertorio = new TbRepertorio(tbRepertorioIn, tbRepertorioOffsetIn, offsetBufferTbRepertorioPtr, elementsTbRepertorio, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));

    tabelleVector.Add(tbTitolo = new TbTitolo(tbTitoloIn, tbTitoloOffsetIn, offsetBufferTbTitoloPtr, elementsTbTitolo, titoloKeyPlusOffsetPlusLfLength, BID_KEY_LENGTH)); // LONG LONG

    tabelleVector.Add(trAutAutInv = new TrAutAut(trAutAutInvIn, trAutAutInvOffsetIn, offsetBufferTrAutAutInvPtr, elementsTrAutAutInv, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(trcPossProvInventari = new TrcPossProvInventari(trcPossProvInventariIn, trcPossProvInventariOffsetIn, offsetBufferTrcPossProvInventariPtr, elementsTrcPossProvInventari, inventarioKeyPlusOffsetPlusLfLength, INVENTARIO_KEY_LENGTH)); // INVENTARIO_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(trPerInt = new TrPerInt(trPerIntIn, trPerIntOffsetIn, offsetBufferTrPerIntPtr, elementsTrPerInt, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(trRepMar = new TrRepMar(trRepMarIn, trRepMarOffsetIn, offsetBufferTrRepMarPtr, elementsTrRepMar, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));

    tabelleVector.Add(trTitAut = new TrTitAut(trTitAutIn, trTitAutOffsetIn, offsetBufferTrTitAutPtr, elementsTrTitAut, trTitAutKeyPlusOffsetPlusLfLength, BID_KEY_LENGTH+VID_KEY_LENGTH)); // BID_KEY_LENGTH+VID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH

if (trBidAltroidIn)
    tabelleVector.Add(trBidAltroid = new TrBidAltroid(trBidAltroidIn, trBidAltroidOffsetIn, offsetBufferTrBidAltroidPtr, elementsTrBidAltroid, trBidAltroidKeyPlusOffsetPlusLfLength, BID_ALTRO_ID_KEY_LENGTH));



    tabelleVector.Add(trTitAutRel = new TrTitAutRel(trTitAutRelIn, trTitAutRelOffsetIn, offsetBufferTrTitAutRelPtr, elementsTrTitAutRel, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH)); // BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(trTitBib = new TrTitBib(trTitBibIn, trTitBibOffsetIn, offsetBufferTrTitBibPtr, elementsTrTitBib, titbibKeyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(trTitCla = new TrTitCla(trTitClaIn, trTitClaOffsetIn, offsetBufferTrTitClaPtr, elementsTrTitCla, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(trTitLuo = new TrTitLuo(trTitLuoIn, trTitLuoOffsetIn, offsetBufferTrTitLuoPtr, elementsTrTitLuo, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(trTitMarRel = new TrTitMar(trTitMarRelIn, trTitMarRelOffsetIn, offsetBufferTrTitMarRelPtr, elementsTrTitMarRel, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(trTitMarTb = new TrTitMar(trTitMarTbIn, trTitMarTbOffsetIn, offsetBufferTrTitMarTbPtr, elementsTrTitMarTb, trKeyPlusOffsetPlusLfLength, BID_KEY_LENGTH+MID_KEY_LENGTH)); // BID_KEY_LENGTH+MID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(trTitTit = new TrTitTit(trTitTitIn, trTitTitOffsetIn, offsetBufferTrTitTitPtr, elementsTrTitTit, trKeyPlusOffsetPlusLfLength, TR_KEY_LENGTH)); // TR_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(tsLinkMultim = new TsLinkMultim(tsLinkMultimIn, tsLinkMultimOffsetIn, offsetBufferTsLinkMultimPtr, elementsTsLinkMultim, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH)); // BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(tbParola = new TbParola(tbParolaIn, tbParolaOffsetIn, offsetBufferTbParolaPtr, elementsTbParola, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH)); // BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(tbSoggetto = new TbSoggetto(tbSoggettoIn, tbSoggettoOffsetIn, offsetBufferTbSoggettoPtr, elementsTbSoggetto, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tb977 = new Tb977(tb977In, tb977OffsetIn, offsetBufferTb977Ptr, elementsTb977, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    tabelleVector.Add(tsLinkWeb = new TsLinkWeb(tsLinkWebIn, tsLinkWebOffsetIn, offsetBufferTsLinkWebPtr, elementsTsLinkWeb, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH)); // BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH

    tabelleVector.Add(tbTermineTesauro = new TbTermineTesauro(tbTermineTesauroIn, tbTermineTesauroOffsetIn, offsetBufferTbTermineTesauroPtr, elementsTbTermineTesauro, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH)); // BID_KEY_LENGTH+OFFSET_LENGTH+LF_LENGTH
    tabelleVector.Add(tbTitSet1 = new TbTitSet1(tbTitSet1In, tbTitSet1OffsetIn, offsetBufferTbTitSet1Ptr, elementsTbTitSet1, titset1KeyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
    // 31/03/2016
    tabelleVector.Add(tbTitSet2 = new TbTitSet2(tbTitSet2In, tbTitSet2OffsetIn, offsetBufferTbTitSet2Ptr, elementsTbTitSet2, titset2KeyPlusOffsetPlusLfLength, BID_KEY_LENGTH));

//	if (DATABASE_ID == DATABASE_SBNWEB && IS_TAG_TO_GENERATE(696))
//	{
//	    tabelleVector.Add(trSogDes = new TrSogDes(trSogDesIn, trSogDesOffsetIn, offsetBufferTrSogDesPtr, elementsTrSogDes, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
//	    tabelleVector.Add(trDesDes = new TrDesDes(trDesDesIn, trDesDesOffsetIn, offsetBufferTrDesDesPtr, elementsTrDesDes, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
//	    tabelleVector.Add(tbDescrittore = new TbDescrittore(tbDescrittoreIn, tbDescrittoreOffsetIn, offsetBufferTbDescrittorePtr, elementsTbDescrittore, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
//	}
    // 28/06/2016
		if (IS_TAG_TO_GENERATE(696) || IS_TAG_TO_GENERATE(699)) // DATABASE_ID == DATABASE_SBNWEB
	{
	    tabelleVector.Add(trSogDes = new TrSogDes(trSogDesIn, trSogDesOffsetIn, offsetBufferTrSogDesPtr, elementsTrSogDes, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
	    tabelleVector.Add(tbDescrittore = new TbDescrittore(tbDescrittoreIn, tbDescrittoreOffsetIn, offsetBufferTbDescrittorePtr, elementsTbDescrittore, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));

//	    if (IS_TAG_TO_GENERATE(696) && DATABASE_ID == DATABASE_SBNWEB)
	    	tabelleVector.Add(trDesDes = new TrDesDes(trDesDesIn, trDesDesOffsetIn, offsetBufferTrDesDesPtr, elementsTrDesDes, keyPlusOffsetPlusLfLength, BID_KEY_LENGTH));
	}


// 18/02/2010 12.15
//	// Creiamo l'elenco delle biblioteche codice/descrizione
//	tbfBibliotecaInPoloKV = new CKeyValueVector(tKVSTRING, tKVVOID);
//	if (!loadTbfBibliotecaInPoloRecords(tbfBibliotecaInPoloFilename.data(), polo))
//		return false;

	marc4cppDocumento = new Marc4cppDocumento(marcRecord,
			tbTitolo, tbImpronta, tbNumeroStandard, tbGrafica, tbCartografia,
			tbMusica,
			tbTitSet1,tbTitSet2,
			tbAudiovideo,
			tbDiscoSonoro,
			trTitMarRel, trTitBib, tbfBiblioteca, tbNota,
			tbNota300, tbNota321, trBidAltroid,
			tsLinkWeb,
			polo, descPolo, bibliotecaRichiedenteScarico, tagsToGenerateBufferPtr,
			tagsToGenerate);

if (DATABASE_ID == DATABASE_SBNWEB)
{
	tbcSezioneCollocazioneQuadreKV = new CKeyValueVector(tKVSTRING, tKVSTRING);
//	if (!loadTbcSezioneCollocazione(tbcSezioneCollocazioneFilename.data(), polo))
	if (!loadTbcSezioneCollocazione(polo))
		return false;
}

	marc4cppLegami = new Marc4cppLegami(
			marcRecord, marc4cppDocumento,
			tbTitolo, tbAutore, tbSoggetto, tbClasse, tbMarca, tbLuogo, tbMusica, tbComposizione, tbIncipit,
			tbPersonaggio, tbRappresent, tbaOrdini,trTitTit,
			trTitTitRelIn, trTitTitRelOffsetIn, offsetBufferTrTitTitRelPtr, elementsTrTitTitRel,
			trTitTitInvRelIn, trTitTitInvRelOffsetIn, offsetBufferTrTitTitInvRelPtr, elementsTrTitTitInvRel,
			trTitBib, trTitAut, trTitAutRel, trTitCla, trTitMarRel, trTitMarTb, trAutAutInv, trTitLuo, tbNumeroStandard,
			tbRepertorio, trRepMar, trPerInt, tsLinkMultim, tbParola,
			tbfBiblioteca,

			tb950Inv,	// 12/09/14
			tb950Coll,
			tb977,
			tbTermineTesauro,
			tbDescrittore, trSogDes, trDesDes, // 28/06/2016

			bibliotecheDaNonMostrareIn950KV,
			keyPlusOffsetPlusLfLength,
			trKeyPlusOffsetPlusLfLength,
			BID_KEY_LENGTH,
			polo
			);

	marc4cppCollocazione = new Marc4cppCollocazione(
			marcRecord, marc4cppDocumento, marc4cppLegami,
			tbcSezioneCollocazioneQuadreKV,
			tbTitolo,
			tb950Inv,
			tb950Coll,
			tb950Ese,
			tbfBiblioteca,
			tbcNotaInv,
			tbcPossessoreProvenienza,
			trcPossProvInventari,
			tbcSezioneCollocazione,
			sezioniDiCollocazioneDaNonMostrareIn960KV,
			bibliotecheDaNonMostrareIn950KV
	);
	marc4cppAltriDoc = new Marc4cppAltriDoc(marcRecord);

	printf ("\n\nFine Marc4cpp::setup\n");

//marc4CppMallinfo();
	return retb;
} // End Marc4cpp::setupAuthDocument



// 03/05/2016 Evolutiva
void Marc4cpp::ricostruisci300_con_321()
{
//return; // per vedere come era la vecchia 300

	DataField *df;
	DataField *df321 = marcRecord->getDataField((char *)"321");
	if (!df321)
		return; // Nessuna 321. Quindi niente da fare.

	// Prendiamo la prima nota (quella con Riferimenti: )
	DataField *df300 = marcRecord->getDataField((char *)"300");
	if (!df300)
		return; // Nessuna 300. Quindi nienteda fare

	// Troviamo tutti i riferimenti della nota
	Subfield *sf_a_300 = df300->getSubfield('a');
	Subfield *sf;
	ATTValVector <CString *> riferimenti;
	int riferimentiStartLen;

	int idx;

	CString *sPtr = sf_a_300->getDataString();
	char *idPtr;
	if (sPtr->StartsWith("Riferimenti"))
		{
		bool retb = sPtr->Split(riferimenti, ';');
		riferimentiStartLen = riferimenti.length();


		// Cicliaamo sulle 321 per trovare gli ID
		ATTValVector<DataField*> *dataFieldsVector = marcRecord->getDataFieldsVector();
		for (int i=0; i < dataFieldsVector->Length() ; i++)
			{
			df = dataFieldsVector->Entry(i);
			if (df)
				{
				CString * tag = df->getTagString();
				if (!tag->Compare("321")
						&& (sf = df->getSubfield('c'))	// 13/06/16
						)
					{
					// Troviamo l'id
					CString *id = sf->getDataString();


					if ((idx = id->IndexSubString("CNCE ")) > -1
						 || (idx = id->IndexSubString("ISTC ")) > -1
						 || (idx = id->IndexSubString("ESTC ")) > -1 // 13/06/16
						 || (idx = id->IndexSubString("VD16 ")) > -1
						 || (idx = id->IndexSubString("VD17 ")) > -1
						 ) // gia' rimosso in DB. attivo ora per demo
						idPtr = id->data()+idx+5;
					else
						idPtr = id->data();

//printf ("\nidPtr='%s'", idPtr);


					// cicliamo sui riferimenti
					for (int j=0; j < riferimenti.length(); j++)
						{
						if (riferimenti[j]->IndexSubString(idPtr) > -1) // Se troviamo id in uno dei riferimenti rimuoviamolo dalla lista
							{
//printf ("\nRemoving'%s'", riferimenti[j]->data());
							riferimenti.DeleteAndRemoveByEntry(j);
							break;
							}
						} // end for ciclo sui riferimenti
					}
				}
			} // End for 321

		// Se i riferimenti sono cambiati dobbiamo ricostruire la 300
		if (riferimentiStartLen != riferimenti.length())
			{ // Ricostruisci 300
			if (riferimenti.length())
				{
				CString dol_a;



				for (int i=0; i < riferimenti.length(); i++)
					{
					if (!i)
					{
						if (!riferimenti[i]->StartsWith("Riferimenti"))
							dol_a = "Riferimenti: ";
					}
					if (i)
						dol_a.AppendString((char *)" ; ");
					dol_a.AppendString(riferimenti[i]);
					} // end for ciclo sui riferimenti
				sf_a_300->setData(&dol_a);
				}
			else
				{
				// Rimossi titti i riferimenti - Rimuoviamo la 300
				int i = marcRecord->removeDataField(df300);
//if (i == -1)
//	printf ("\nNon posso rimuovere datafield 300");
//else
//	printf ("\nRimosso datafield 300 alla posizione %d", i);
			}

			}
		riferimenti.DeleteAndClear();
		}
//	else
//	{
//		printf ("\nPrima nota non inizia con 'Riferimenti' per bid %s", marcRecord->getControlField("001")->getData()->data());
//	}



	ATTValVector<DataField*> *dfs = marcRecord->getDataFieldsVector();








} // End Marc4cpp::ricostruisci300_con_321


void Marc4cpp::ricostruisci101()
{
//return; // per vedere come era la vecchia 300

	Subfield *sf;
	CString *linguaPtr;

	DataField *df_101 = marcRecord->getDataField((char *)"101");
	DataField *df_454 = marcRecord->getDataField((char *)"454");
	DataField *df_500 = marcRecord->getDataField((char *)"500");

	if (!df_101)
		return;

	if (df_454)
	{
		sf = df_454->getSubfield('x'); // il sottocampo di servizio per la lingua
		if (sf)
		{
			linguaPtr = sf->getDataString();
			linguaPtr->ToLower();
			sf = new Subfield('b', linguaPtr);
			if (sf)
			{
				df_101->addSubfield(sf);
				df_454->removeSubfield('x'); // Rimuoviamo il sottocampo di servizio nella 454
			}
		}
		sf = df_454->getSubfield('y'); // il sottocampo di servizio per la lingua
		if (sf)
		{
			linguaPtr = sf->getDataString();
			linguaPtr->ToLower();
			sf = new Subfield('b', linguaPtr);
			if (sf)
			{
				df_101->addSubfield(sf);
				df_454->removeSubfield('y'); // Rimuoviamo il sottocampo di servizio nella 454
			}

			sf = df_454->getSubfield('z'); // il sottocampo di servizio per la lingua
			if (sf)
			{
				linguaPtr = sf->getDataString();
				linguaPtr->ToLower();
				sf = new Subfield('b', linguaPtr);
				if (sf)
				{
					df_101->addSubfield(sf);
					df_454->removeSubfield('z'); // Rimuoviamo il sottocampo di servizio nella 454
				}
			}


		}
	}

	if (df_500)
	{
		sf = df_500->getSubfield('x'); // il sottocampo di servizio per la lingua
		if (sf)
		{
			linguaPtr = sf->getDataString();
			linguaPtr->ToLower();
			sf = new Subfield('c', linguaPtr);
			if (sf)
			{
				df_101->addSubfield(sf);
				df_500->removeSubfield('x'); // Rimuoviamo il sottocampo di servizio nella 500
			}
		}
		sf = df_500->getSubfield('y'); // il sottocampo di servizio per la lingua
		if (sf)
		{
			linguaPtr = sf->getDataString();
			linguaPtr->ToLower();
			sf = new Subfield('c', linguaPtr);
			if (sf)
			{
				df_101->addSubfield(sf);
				df_500->removeSubfield('y'); // Rimuoviamo il sottocampo di servizio nella 454
			}

			sf = df_500->getSubfield('z'); // il sottocampo di servizio per la lingua
			if (sf)
			{
				linguaPtr = sf->getDataString();
				linguaPtr->ToLower();
				sf = new Subfield('c', linguaPtr);
				if (sf)
				{
					df_101->addSubfield(sf);
					df_500->removeSubfield('z'); // Rimuoviamo il sottocampo di servizio nella 454
				}
			}
		}

	}



} // End Marc4cpp::ricostruisci101()

