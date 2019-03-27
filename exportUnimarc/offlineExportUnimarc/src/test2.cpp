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
/media/export54/indice/dp/dev/offlineExportUnimarc.dev.cfg

/media/export/exportUnimarc/dp/dev/offlineExportUnimarc_dev.cfg

============================================================================
 Name        : test2.cpp
 Author      : Argentino
 Version     :
 Copyright   : Almaviva S.p.A.
 Description : offline export unimarc


Switch overrides
   '-b o --bidErrati'
   '-c o --incrementale_dal'
   '-e o --elaboraNRighe N'
   '-i o --iniziaElaborazioneDa N'
   '-j o --idXunimarc'
   '-l o --logOgniXRighe N'
   '-m o --markFileOut'
   '-p o --processLog'
   '-q o --esportaSoloInventariCollocati'
   '-s o --tipoScarico'
   '-t o --tags E:/SbnWeb/migrazione/ind2/tagsToExport.txt'
   '-u o --tipoUnimarc'
   '-x o --xmlMarkFileOut'
   '-z o --indiciBinari (ASCII/BINARY)' // 13/04/2015

# Defines a livello di progetto
	-DTRACK_MEMORY_LEAKS	// per intercettare i memory leakes
	-DLITTLE_ENDIAN			// For intel architectures
	-DMEMCPY_64BIT			// For 64 bit architectures


32 bit platform				64 bit platform
---------------	                        ---------------
sizeof char=1                           sizeof char=1
sizeof signed char=1                    sizeof signed char=1
sizeof unsigned char=1                  sizeof unsigned char=1

sizeof short=2                          sizeof short=2
sizeof signed short=2                   sizeof signed short=2
sizeof unsigned short=2                 sizeof unsigned short=2

sizeof short int=2                      sizeof short int=2
sizeof signed short int=2               sizeof signed short int=2
sizeof unsigned short int=2             sizeof unsigned short int=2

sizeof int=4                            sizeof int=4
sizeof signed int=4                     sizeof signed int=4
sizeof unsigned int=4                   sizeof unsigned int=4

sizeof long=4                           sizeof long=8
sizeof signed long=4                    sizeof signed long=8
sizeof unsigned long=4                  sizeof unsigned long=8

sizeof long long=8                      sizeof long long=8
sizeof signed long long=8               sizeof signed long long=8
sizeof unsigned long long=8             sizeof unsigned long long=8



============================================================================
*/

#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <time.h>
using namespace std;

#include "Marc4cpp.h"
#include "library/tree.hh"

#include "examples/ReadMarcExample.h"
//#include "examples/WriteMarcExample.h"
#include "library/Cini.h"
#include "library/GenericError.h"
#include "../include/library/CString.h"
#include "library/CFileInMemory.h"
#include "library/CFile.h"
#include "../include/library/CKeyValueVector.h"
#include "BinarySearch.h"
#include "../include/library/CFile.h"
#include "library/CMisc.h"

CKeyValueVector *codiciNotaKV = 0;

//#ifdef EVOLUTIVA_POLO_2014
CKeyValueVector *codiciEclaKV = 0;
//#endif
CKeyValueVector *codiciOrgaKV = 0; // 26/01/2015

// 2018/01/15 (veramente stiamo trattando dei dizionari)
//CKeyValueVector *varianteKsogVarKV = 0;
CKeyValueVector *variantiSinonimiaKsogVarKV = 0;
CKeyValueVector *variantiSinonimiaKvarSogKV = 0;

CKeyValueVector *variantiStoricheKsogVarKV = 0;
CKeyValueVector *variantiStoricheKvarSogKV = 0;


CKeyValueVector *scomposizioneSoggettoKV = 0;
CKeyValueVector *composto_non_preferito_ctr_KV = 0;
CKeyValueVector *compostoNonPreferitoKV = 0;

#ifdef TRACK_MEMORY_LEAKS

    #include "nvwa/debug_new.h"
#endif


char const * switchOverrides = "Switch overrides:\n\
   '-b o --bidErrati',\n\
   '-c o --incrementale_dal',\n\
   '-e o --elaboraNRighe N',\n\
   '-i o --iniziaElaborazioneDa N',\n\
   '-j o --idXunimarc',\n\
   '-l o --logOgniXRighe N',\n\
   '-m o --markFileOut',\n\
   '-p o --processLog',\n\
   '-q o --esportaSoloInventariCollocati',\n\
   '-s o --tipoScarico',\n\
   '-t o --tags nomeFileTagsToExport.txt',\n\
   '-u o --tipoUnimarc',\n\
   '-x o --xmlMarkFileOut',\n\
   '-y o --sistemaNumerico (decimale/esadecimale)',\n\
   '-z o --indiciBinari (ASCII/BINARY)'"; // 13/04/2015




//#include "MarcGlobals.h"

// Per PROFILING
//long tbAppendStringCtr;




// Global variables
//CString FixedLengthLine; usata in piu' parti mi ha generato corruzione di dati (464). 07/10/2010
CString NSB;
CString NSE;
CString
		AUTHORITY,
        IDXUNIMARC,
        RETICOLO_OUT,
        POLO,
        DESCPOLO,
        BIBLIOTECARICHIEDENTESCARICO,
        MARKFILEOUT,
        MARKFILEOUTTXT,
        MARKFILEOUTXML,
        PROCESSLOG,
        BIDERRATIOUT;
int		SISTEMA_NUMERICO_UNIMARC = SISTEMA_NUMERICO_DECIMALE; // default decimale
long	MAX_RECORD_SIZE = 99999; // defaul (sistema decimale)
long	MAX_FIELD_SIZE = 9999; // defaul (sistema decimale)
int		ISBN_CON_TRATTINI = 0;	// Mantis 4361 A)
int 	RECORDUNIMARCSUSINGOLARIGA = 0;
long 	INIZIAELABORAZIONEDARIGA = 0;
bool 	POSITIONBYOFFSET = false;
long 	ELABORANRIGHE=0;
long	MARKFILEOUT_BUFFER_SIZE=102400;
long	MARKFILEOUT_BUFFER_RESIZE_BY=4096; // 09/12/2010 Should never resize really, since we empty the buffer when fuul!!

int 	LOGOGNIXRIGHE=0;
int		DATABASE_ID = DATABASE_SBNWEB; // Default
int		TIPO_SCARICO = TIPO_SCARICO_UNIMARC;
int 	TIPO_UNIMARC = TIPO_UNIMARC_STANDARD;
bool	EXPORT_VIAF = false;
int		EXPORT_MAX_VIAFS;
int		OFFSET_TYPE = OFFSET_TYPE_ASCII;
bool 	logNaturaErrata = false;
bool	stampaRecordCancellato = false; // 02/02/2018

bool	esportaSoloInventariCollocati = false; // 31/05/2018


// 29/01/2018 Gestione parametrica delle nature da esportare
CString estrai_nature_indice = (char *)"mMwWsSnNcC";
CString estrai_nature_polo = (char *)"mMwWsSnNcCrR";
CString estrai_nature;
bool	z899 = false;
bool	check899 = false;
bool	_699_sintetica = false;
//bool	xy899 = false;
CString *incrementale_dal;

extern	long long binarySearchMemoryCtr;
extern	long long binarySearchFileCtr;
extern	long long binarySearchFileCtrSeekTo;

extern int memcpySizeCtr[];

#ifdef DEBUG_ARGE
	int	eccSoloTondaApertaCtr = 0;
	int	eccSoloTondaChiusaCtr = 0;
	int	eccDataNonPrecedutaDaVirgolaCtr = 0;
	int	eccTestoDopoVirgolaSpazioCtr = 0;
	int	eccParentesiTondeNonSottoarea = 0;
	int	dataPrecedutaDaVirgolaCtr = 0;
#endif


char const *paddingZeroes [] = {
""
"0",
"00",
"000",
"0000",
"00000",
"000000",
"0000000",
"00000000",
"000000000",
"0000000000",
"00000000000",
"000000000000",
"0000000000000",
"00000000000000",
"000000000000000",
};

char const *descTipoPeriodicita [] =    // Come da tb_codici.tp_tabella PPER
{
"Quotidiano",                 // A
"Bi o Trisettimanale",        // B
"Settimanale",                // C
"Quindicinale",               // D
"Bimensile",                  // E
"Mensile",                    // F
"Bimestrale",                 // G
"Trimestrale",                // H
"Quadrimestrale",             // I
"Semestrale",                 // J
"Annuale",                    // K
"Biennale",                   // L
"Triennale",                  // M
"Irregolare",                 // N
"Quinquennale",               // O
"Tre numeri al mese",         // P
"",                           // Q
"",                           // R
"",                           // S
"",                           // T
"Sconosciuto",                // U
"",                           // V
"",                           // W
"Complesso",                  // X
"",                           // Y
"altro"                       // Z
};






//CString titoliUnimarcRidotto, titoliVariati, titoliErrati; // titoliCancellati, titoliFusi, titoliLocDaCanc, titoliUnimarc
//CFile *titoliUnimarcRidottoOut, *titoliVariatiOut, *titoliErratiOut; // , *titoliUnimarcOut, *titoliCancellatiOut, *titoliFusiOut, *titoliLocDaCancOut,

// End global variables


extern void SignalAnError(	OrsChar *Module, OrsInt Line, OrsChar * MsgFmt, ...);
extern void SignalAWarning(	OrsChar *Module, OrsInt Line, OrsChar * MsgFmt, ...);

// Forward declaration
void testCFile();
void testMarcRead();
int offlineExport(int argc, const char* argv[]);
void printHeader();
void readConfig(CFile *iniFileIn, cIni *ini);
void addBibliotecheDaNonMostrareIn950(char *csvBibliotecheFilename);
bool addexportTagsfromFile(const char *tagsFilename, cIni *ini);
void printTagsGestiti();


char *tagsToGenerateBufferPtr;


ATTValVector <CString *> exportTags;
//ATTValVector <CString *> sezioniDiCollocazioneDaNonMostrareIn960Vect;

//CKeyValueVector *entitaKVvector; 		Problemi di corruzione di memoria ....non identificabili
//CKeyValueVector *relazioniKVvector;
//CKeyValueVector *offsetKVvector;

ATTValVector<CString *> *entitaVector = 0;
ATTValVector<CString *> *relazioniVector = 0;
ATTValVector<CString *> *offsetVector = 0;
ATTValVector<CString *> *dictionaryVector = 0;

CKeyValueVector *sezioniDiCollocazioneDaNonMostrareIn960KV = 0;
CKeyValueVector *bibliotecheDaNonMostrareIn950KV = 0;

CKeyValueVector *bibliotecheDaMostrareIn899KV = 0;




void addSezioniDiCollocazioneDaNonMostrareIn960(char *csvSez)
{
	ATTValVector <CString *> localVect;
	CString s = csvSez;
	s.Split(localVect, ',');

	for (int i=0; i < localVect.length(); i++)
		sezioniDiCollocazioneDaNonMostrareIn960KV->Add(localVect.Entry(i)->data(), "");

	localVect.DeleteAndClear();
}


/*
void testTokenizer()
{
	CTokenizer Tokenizer("B?&&$%MIGMUS000000&$%1", "&$%");
	OrsChar *tokenPtr;
	long tokenLength;

	int i=0;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength))
	{
		if(tokenLength)
		{
	printf ("\nToken=%s", tokenPtr);

		}
		i++;
	} // End while

}
*/


void logTabelleIni()
{
	for (int i=0; i < entitaVector->Length(); i++)
	{
		printf ("\nENTITA: %s",entitaVector->Entry(i)->data());
	}

	for (int i=0; i < relazioniVector->Length(); i++)
	{
		printf ("\nRELAZIONI: %s",relazioniVector->Entry(i)->data());
	}

	for (int i=0; i < offsetVector->Length(); i++)
	{
		printf ("\nOFFSET: %s",offsetVector->Entry(i)->data());
	}
}


void addExportTags(char *csvTags)
{
	ATTValVector <CString *> localExportTags;
	CString *sPtr;
	CString s = csvTags;
//	s.SkipWhitespace(true); //11/03/2016
	s.Split(localExportTags, ',');

	for (int i=0; i < localExportTags.length(); i++)
	{
		sPtr = localExportTags.Entry(i);
		sPtr->CropRightFrom(3);
		if (sPtr->Length())
		{
			exportTags.Add(sPtr);
		}
//printf ("\n Export Tag %s", localExportTags.Entry(i)->data());
	}

//printf ("\n --------------", exportTags.length());
//printf ("\n Export %d tags", exportTags.length());
}





bool truefunc(std::string& one, std::string& two)
   {
// std::cout << "comparing " << one << "==" << two << std::endl;
   return true;
   }

void print_tree(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end)
   {
   if(!tr.is_valid(it)) return;
   int rootdepth=tr.depth(it);
   std::cout << "-----" << std::endl;
   while(it!=end) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << "\n";//std::endl << std::flush;
      ++it;
      }
   std::cout << "-----" << std::endl;
   }

void print_tree_post(const tree<std::string>& tr, tree<std::string>::post_order_iterator it, tree<std::string>::post_order_iterator end)
   {
   int rootdepth=tr.depth(it);
   std::cout << "-----" << std::endl;
   while(it!=end) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << "\n";//std::endl << std::flush;
      ++it;
      }
   std::cout << "-----" << std::endl;
   }

void print_tree_rev(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end)
   {
   --it;
   int rootdepth=0;//tr.depth(it);
   std::cout << "-----" << std::endl;
   while(1==1) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << "\n";//std::endl;
      if(it==end) break;
      --it;
      }
   std::cout << "-----" << std::endl;
   }

void print_tree_rev_post(const tree<std::string>& tr, tree<std::string>::post_order_iterator it, tree<std::string>::post_order_iterator end)
   {
   --it;
   int rootdepth=0;//tr.depth(it);
   std::cout << "-----" << std::endl;
   while(1==1) {
      for(int i=0; i<tr.depth(it)-rootdepth; ++i)
         std::cout << "  ";
      std::cout << (*it) << "\n";//std::endl;
      if(it==end) break;
      --it;
      }
   std::cout << "-----" << std::endl;
   }




void testTreeHH()
   {
   unsigned int maxloop=1;

   for(unsigned int j=0; j<maxloop; ++j) {
      tree<std::string> tr9;
      tr9.set_head("hi");
      tr9.insert(tr9.begin().begin(), "0");
      tr9.insert(tr9.begin().begin(), "1");
      print_tree(tr9, tr9.begin(), tr9.end());


      tree<std::string> tr;
      tree<std::string>::pre_order_iterator html, body, h1, h3, bh1, mv1;

      std::cout << "empty tree to begin with:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());

      html=tr.insert(tr.begin(), "html");
      tr.insert(html,"extra");
//    tr.insert(html,"zextra2");
      body=tr.append_child(html, "body");
      h1  =tr.append_child(body, "h1");
      std::cout << tr.index(h1) << std::endl;
      bh1 =tr.insert(h1,"before h1");
      tr.append_child(h1, "some text");
      tree<std::string>::sibling_iterator more_text=tr.append_child(body, "more text");

      std::cout << " 'more text' is sibling " << tr.index(more_text) << " in its sibling range" << std::endl;

      std::cout << "filled tree:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());

      std::cout << "filled tree, post-order traversal:" << std::endl;
      print_tree_post(tr, tr.begin_post(), tr.end_post());

      tr.swap(bh1);
      std::cout << "swapped elements:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());
      tr.swap(h1);
      std::cout << "swapped back:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());

      tree<std::string> copytree(h1);
      std::cout << "copied tree:" << std::endl;
      print_tree(copytree, copytree.begin(), copytree.end());

      // Now test the STL algorithms
      std::cout << "result of search for h1 and kasper:" << std::endl;
      tree<std::string>::pre_order_iterator it;
      it=std::find(tr.begin(),tr.end(),std::string("h1"));
      if(it!=tr.end()) print_tree(tr, it, tr.next_sibling(it));
      else             std::cout << "h1 not found" << std::endl;
      it=std::find(tr.begin(),tr.end(), std::string("kasper"));
      if(it!=tr.end()) print_tree(tr, it, tr.next_sibling(it));
      else             std::cout << "kasper not found" << std::endl;
      std::cout << std::endl;

      // remove the h1, replace it with new subtree
      tree<std::string> replacement;
      h3  =replacement.insert(replacement.begin(), "h3");
      replacement.append_child(h3, "text in h3");
      std::cout << "replacement tree:" << std::endl;
      print_tree(replacement, replacement.begin(), replacement.end());
      print_tree(tr, tr.begin(), tr.end());
      h1=tr.replace(tree<std::string>::sibling_iterator(h1), tr.next_sibling(h1),
                    tree<std::string>::sibling_iterator(h3), tr.next_sibling(h3));
      std::cout << "filled tree with replacement done:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());

      // replace h3 node while keeping children
      h1=tr.replace(h1, "<foobar>");
      print_tree(tr, tr.begin(), tr.end());

      // add a sibling to the head
      tr.insert_after(h1, "more");

      // Copy object.
      tree<std::string> tr2=tr;
      print_tree(tr2, tr2.begin(), tr2.end());
      tree<std::string> tr3(tr);

      // reparent "before h1" to h3 node
      tr.reparent(h1, bh1, tr.next_sibling(bh1));
      std::cout << "moved content:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());

      // iterate over children only
      tree<std::string>::sibling_iterator ch=tr.begin(h1);
      std::cout << "children of h1:" << std::endl;
      while(ch!=tr.end(h1)) {
         std::cout << (*ch) << "\n";//std::endl;
         ++ch;
         }
      std::cout << std::endl;

      // flatten the h3 node
      tr.flatten(h1);
      std::cout << "flattened (at h3) tree:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());

      // Erase the subtree of tr below body.
      tr.erase_children(body);
      std::cout << "children of body erased:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());
      it=std::find(tr.begin(),tr.end(),"h1");
      if(it!=tr.end()) print_tree(tr, it, tr.next_sibling(it));
      else             std::cout << "h1 not found" << std::endl;

      // Erase everything
      tr.erase(tr.begin());
      std::cout << "erased tree:" << std::endl;
      print_tree(tr, tr.begin(), tr.end());

      // The copies are deep, ie. all nodes have been copied.
      std::cout << "copies still exist:" << std::endl;
      print_tree(tr2, tr2.begin(), tr2.end());
      print_tree(tr3, tr3.begin(), tr3.end());

      // Test comparison
      std::cout << "testing comparison functions:" << std::endl;
      std::cout << std::equal(tr2.begin(), tr2.end(), tr3.begin(), std::equal_to<std::string>())
                << " (should be 1)" << std::endl;
      // modify content but not structure
      tree<std::string>::pre_order_iterator fl3=tr3.begin();
      fl3+=4; // pointing to "<foobar>" node
      std::cout << (*fl3) << "\n";//std::endl;
      std::string tmpfl3=(*fl3);
      (*fl3)="modified";
      std::cout << std::equal(tr2.begin(), tr2.end(), tr3.begin(), std::equal_to<std::string>())
                << " (should be 0)" << std::endl;
      std::cout << tr2.equal(tr2.begin(), tr2.end(), tr3.begin(), std::equal_to<std::string>())
                << " (should be 0)" << std::endl;
      std::cout << tr2.equal(tr2.begin(), tr2.end(), tr3.begin(), truefunc)
                << " (should be 1)" << std::endl;
      // modify tr3 structure (but not content)
      (*fl3)=tmpfl3;
      tr3.flatten(fl3);
      std::cout << "tree flattened, test again" << std::endl;
      print_tree(tr3, tr3.begin(), tr3.end());

      // Test comparison again
      std::cout << tr2.equal(tr2.begin(), tr2.end(), tr3.begin(), std::equal_to<std::string>())
                << " (should be 0)" << std::endl;
      std::cout << std::equal(tr2.begin(), tr2.end(), tr3.begin(), std::equal_to<std::string>())
                << " (should be 1)" << std::endl;
      // Change content
      (*fl3)="modified";
      // Test comparison again
      std::cout << std::equal(tr2.begin(), tr2.end(), tr3.begin(), std::equal_to<std::string>())
                << " (should be 0)" << std::endl;
      std::cout << tr2.equal(tr2.begin(), tr2.end(), tr3.begin(), std::equal_to<std::string>())
                << " (should be 0)" << std::endl;

      // Testing sort. First add a subtree to one leaf
      tree<std::string>::pre_order_iterator txx3=tr3.begin();
      txx3+=5;
      tr3.append_child(txx3,"ccc");
      tr3.append_child(txx3,"bbb");
      tr3.append_child(txx3,"bbb");
      tr3.append_child(txx3,"aaa");
      std::less<std::string> comp;
      tree<std::string>::pre_order_iterator bdy=tr3.begin();
      bdy+=2;
      assert(tr.is_valid(bdy));
      std::cout << "unsorted subtree:" << std::endl;
      print_tree(tr3, tr3.begin(), tr3.end());
      tree<std::string>::sibling_iterator sortit1=tr3.begin(bdy), sortit2=tr3.begin(bdy);
      sortit1+=2;
      sortit2+=4;
      assert(tr.is_valid(sortit1));
      assert(tr.is_valid(sortit2));
      std::cout << "partially sorted subtree: ("
                << "sorted from " << (*sortit1) << " to "
                << (*sortit2) << ", excluding the last element)" << "\n";//std::endl;


      mv1=tr3.begin();
      ++mv1;
      tr3.sort(sortit1, sortit2);
      print_tree(tr3, tr3.begin(), tr3.end());
      tr3.sort(tr3.begin(bdy), tr3.end(bdy), comp, true); // false: no sorting of subtrees
//    Sorting the entire tree, level by level, is much simpler:
//    tr3.sort(tr3.begin(), tr3.end(), true);
      std::cout << "sorted subtree:" << std::endl;
      print_tree(tr3, tr3.begin(), tr3.end());

      // Michael's problem
//    std::cout << mv1.node << ", " << tr3.feet << ", " << tr3.feet->prev_sibling << std::endl;
//    std::cout << mv1.node->next_sibling << ", " << tr3.feet->prev_sibling << ", " << tr3.end().node << std::endl;
//    tr3.sort(tr3.begin(), tr3.end(), true);
//    std::cout << mv1.node << ", " << tr3.feet << ", " << tr3.feet->prev_sibling << std::endl;
//    std::cout << mv1.node->next_sibling << ", " << tr3.feet->prev_sibling << ", " << tr3.end().node << std::endl;
//    print_tree(tr3, tr3.begin(), tr3.end());
//    tr3.sort(tr3.begin(), tr3.end(), true);
//    std::cout << mv1.node << ", " << tr3.feet << ", " << tr3.feet->prev_sibling << std::endl;
//    std::cout << mv1.node->next_sibling << ", " << tr3.feet->prev_sibling << ", " << tr3.end().node << std::endl;
//    print_tree(tr3, tr3.begin(), tr3.end());
//    return 1;

      // Test merge algorithm.
      std::cout << "test merge" << std::endl;
      tree<std::string> mtree;
      tree<std::string>::pre_order_iterator mt1, mt2, mt3;
      mt1=mtree.insert(mtree.begin(),"html");
      mt2=mtree.append_child(mt1,"head");
      mt3=mtree.append_child(mt1,"body");
// Adding it without head having any children tests whether we can
// insert at the end of an empty list of children.
      mtree.append_child(mt2,"title");
      mtree.append_child(mt3,"h1");
      mtree.append_child(mt3,"h1");

      tree<std::string> mtBree;
      tree<std::string>::pre_order_iterator mtB1, mtB2;
      mtB1=mtBree.insert(mtBree.begin(),"head");
      mtB2=mtBree.append_child(mtB1,"another title");
      print_tree(mtree, mtree.begin(), mtree.end());
      print_tree(mtBree, mtBree.begin(), mtBree.end());

      mtree.merge(mtree.begin(), mtree.end(), mtBree.begin(), mtBree.end(), true);
      print_tree(mtree, mtree.begin(), mtree.end());
      mtree.merge(mtree.begin(mtree.begin()), mtree.end(mtree.begin()), mtBree.begin(), mtBree.end(), true);
      print_tree(mtree, mtree.begin(), mtree.end());

      // Print tree in reverse (test operator--)
      print_tree_rev(mtree, mtree.end(), mtree.begin());
      print_tree_rev_post(mtree, mtree.end_post(), mtree.begin_post());

      // Breadth-first
      tree<std::string> bft;
      tree<std::string>::iterator bfB,bfC,bfD;
      bft.set_head("A");
      bfB=bft.append_child(bft.begin(), "B");
      bfC=bft.append_child(bft.begin(), "C");
      bfD=bft.append_child(bft.begin(), "D");
      bft.append_child(bfB, "E");
      bft.append_child(bfB, "F");
      bft.append_child(bfC, "G");
      bft.append_child(bfC, "H");
      bft.append_child(bfD, "I");
      tree<std::string>::breadth_first_queued_iterator bfq=bft.begin_breadth_first();
      while(bfq!=bft.end_breadth_first()) {
         std::cout << *bfq << "\n";//std::endl;
         ++bfq;
         }

      print_tree(bft, bft.begin(), bft.end());
      bft.wrap(bfD, "wrap");
      print_tree(bft, bft.begin(), bft.end());

      tree<std::string>::leaf_iterator li=tr.begin_leaf(bfC);
      while(li!=tr.end_leaf(bfC)) {
          std::cout << *li << "\n";//std::endl;
          ++li;
          }

//      tree<std::string> testfixed;
//      testfixed.insert(testfixed.begin(), "one");
//      testfixed.insert(testfixed.begin(), "two");
//      testfixed.insert(testfixed.begin(), "three");
//      tree<std::string>::fixed_depth_iterator fit=testfixed.begin();
//      while(testfixed.is_valid(fit)) {
//         std::cout << *fit << std::endl;
//         ++fit;
//         }
      }
   }





// Print lines for CFile and DERIVED classes
void printLines(CFile* cFile)
{
	long ctr = 0;
	CString s;

	while(!cFile->Eof())
	{
		if (!s.ReadLine(cFile))
			break;
		ctr++;
		printf ("\nLette %ld righe: ultima %s", ctr, s.data());
		if (!(ctr & 0xf))
		{
			break;
		}
	}

}



void testCFileInMemory()
{

	printf ("\ntestCFileInMemory\n");

	CFileInMemory *cFileInMemory = new CFileInMemory("/media/hd3/SbnWeb/migrazione/bve/input/tr_tit_tit.out.srt.rel");

	printLines(cFileInMemory);

	delete cFileInMemory;
	printf ("\nFine testCFileInMemory");
}




void loadOffsetFiles2(CFile *fileOffsetIn, char *ptr)
{
	bool retb = true;
	int ctr = 0;
	fileOffsetIn->SeekToBegin();
	while (retb)
	{
		retb = fileOffsetIn->Read(ptr, READ_BLOCK_SIZE);
		ptr+=READ_BLOCK_SIZE;
		ctr++;
//		if (!(ctr & 0x3ff))
//			printf ("\nLetti %ld ca bytes", (long)ctr*READ_BLOCK_SIZE);
	}
	printf ("\nLetti %ld bytes ca", (long)ctr*READ_BLOCK_SIZE);
//	printf ("\nLetti %ld mega ca", ((long)ctr*READ_BLOCK_SIZE) >> 20);

} // End loadOffsetFiles2

void testOffsetFile()
{
	printf ("\ntestOffsetFile\n");
	char *entryPtr;

	// Apri il file
	CFile *tbClasseOffsetIn = new CFile("e:/SbnWeb/migrazione/bve/input/tb_classe.out.off.srt1");


	// Carica il file in memoria
	tbClasseOffsetIn->SeekToEnd();
	long fileSize = tbClasseOffsetIn->CurOffset();
	long elements = fileSize/(34+11+1);
	char *offsetBufferTbClassePtr = (char *)malloc(elements*(34+11+1)); // n righe di classe+Offset
	printf ("\n\nLeggendo offsets per %s", tbClasseOffsetIn->GetName());
	printf ("\nLeggendo %ld righe", elements);
	loadOffsetFiles2(tbClasseOffsetIn, offsetBufferTbClassePtr);

	// Esegui una ricerca
	char const *key = "D18937.                           ";
	long position;
	long offset;


	if (BinarySearch::search(offsetBufferTbClassePtr, elements, (34+11+1), key,  34, position, &entryPtr))
	{
		// Dalla posizione prendiamo l'offset
		offset = atol (entryPtr+34); // offsetBufferTrTitTitPtr+position
		printf("\nFOUND %s at position %ld, offset %ld", key, position, offset);
	}
	else
		printf("\nNOT FOUND '%s'", key);



	delete tbClasseOffsetIn;
	printf ("\nFine testOffsetFile");
}




void deleteVectors()
{
	if (entitaVector)
	{
		entitaVector->DeleteAndClear();
		delete entitaVector;
	}
	if (relazioniVector)
	{
		relazioniVector->DeleteAndClear();
		delete relazioniVector;
	}
	if (offsetVector)
	{
		offsetVector->DeleteAndClear();
		delete offsetVector;
	}
	if (dictionaryVector)
	{
		dictionaryVector->DeleteAndClear();
		delete dictionaryVector;
	}
	if (sezioniDiCollocazioneDaNonMostrareIn960KV)
		delete sezioniDiCollocazioneDaNonMostrareIn960KV;

	if (bibliotecheDaNonMostrareIn950KV)
		delete bibliotecheDaNonMostrareIn950KV;

	if (bibliotecheDaMostrareIn899KV)
		delete bibliotecheDaMostrareIn899KV;
}




void addBibliotecheInKVV(CFile *cFileIn, CKeyValueVector *kvv)
{
	CString s;// = csvBiblioteche;
	char buf[256];


//	while (s.ReadString(cFileIn))
	ATTValVector <CString *> localVect;

	while (fgets(buf, 256, cFileIn->GetFilePtr()))
	{
		if (buf[0] == '#') // 03/07/2018
			continue;
		int len = strlen(buf);
		if (*(buf+len-1) == '\n')
			*(buf+len-1) = 0;
		if (*(buf+len-2) == '\r')
			*(buf+len-2) = 0;

		s = buf;
		s.Split(localVect, ',');
		for (int i=0; i < localVect.length(); i++)
			kvv->Add(localVect.Entry(i)->data(), "");

	} // End while

	localVect.DeleteAndClear();
}


void addBibliotecheDaNonMostrareIn950(char *csvBibliotecheFilename)
{
	// Prendiamo il nome del file e controllimao se esiste
	if (!CFile::Exists(csvBibliotecheFilename))
		return;

	CFile *cFileIn = new CFile(csvBibliotecheFilename);

	addBibliotecheInKVV(cFileIn, bibliotecheDaNonMostrareIn950KV);

	//	printf ("\nDUMP bibliotecheDaNonMostrareIn950KV: ");
	//	for (int i=0; i < bibliotecheDaNonMostrareIn950KV->Length(); i++ )
	//		printf (", '%s'", bibliotecheDaNonMostrareIn950KV->Get(i)->Key);
	//	printf ("\n------------");
	delete (cFileIn);
}

void addBibliotecheDaMostrareIn899(char *csvBibliotecheFilename)
{
	// Prendiamo il nome del file e controllimao se esiste
	if (!CFile::Exists(csvBibliotecheFilename))
		return;

	CFile *cFileIn = new CFile(csvBibliotecheFilename);

	addBibliotecheInKVV(cFileIn, bibliotecheDaMostrareIn899KV);

	delete (cFileIn);
}





int offlineExport(int argc, const char* argv[])
{
	CFile *cFile;
	bool retb;
//	setvbuf(stdout, NULL, _IONBF, 0);

	time_t rawtimeStart, rawtimeEnd;
	cIni *ini;
	CFile *iniFileIn;
	char locBuf[160];



	printHeader();
	if (argc < 2)
	{
		printf ("\nUso: offlineExportUnimarc fileDiConfig.txt [switch overrides] \n"); // Fix vari per la 960 (Mail pasqualetti del 24/11/09)
		printTagsGestiti();
		return 1;
	}
	time ( &rawtimeStart );
	printf ( "\nInizio elaborazione: config: %s, %s", argv[1], ctime (&rawtimeStart));
	printf ("\n---------------------------------------------------------------");


	printf ( "\nSwitches overriden : ");
	if (argc < 3)
		printf (" NONE");
	else
	{
		for (int i = 2; i < argc; i++)
		{
			printf ("\n%s %s", argv[i], argv[i+1]);
			i++;
		}
	}
	printf ("\n----------------------------");




	try{
		iniFileIn = new CFile((char *)argv[1], AL_READ_FILE);
	} catch (GenericError e) {
		std::cout << e.errorMessage;
		return false;
	}

	ini =  new cIni();
	readConfig(iniFileIn, ini);
	delete iniFileIn;

	// Prendi i parametri esterni per andare in override di quelli del file di configurazione
	if (argc > 2)
	{ // Gestione switch
		const 	char 	*tagsFilename = 0;
		int 	elaboraNRigheSwitch = -1; 		//ELABORANRIGHE = atol(ini->fieldsVector->Entry(1)->data());
		long 	iniziaElaborazioneDaSwitch =-1; // INIZIAELABORAZIONEDARIGA = 0;
		int		logOgniXRigheSwitch=-1;			// LOGOGNIXRIGHE


		for (int i = 2; i < argc; i+=2) { /* We will iterate over argv[] to get the parameters stored inside.
                                          * Note that we're starting on 2 because we don't need to know the
                                          * path of the program and the config file, which is stored in argv[0] and argv[1] */
            if (i < argc) // Check that we haven't finished parsing already
                if (!strcmp(argv[i],"-t") || !strcmp(argv[i],"--tags")) {
                	tagsFilename = argv[i + 1]; // // We know the next argument *should* be the tags filename:
                } else if (!strcmp(argv[i],"-e") || !strcmp(argv[i],"--elaboraNRighe")) {
                	elaboraNRigheSwitch = atol(argv[i + 1]);
                } else if (!strcmp(argv[i],"-i") || !strcmp(argv[i],"--iniziaElaborazioneDa")) {
                	iniziaElaborazioneDaSwitch = atol(argv[i + 1]);
                } else if (!strcmp(argv[i],"-l") || !strcmp(argv[i],"--logOgniXRighe")) {
//                	logOgniXRigheSwitch = atol(argv[i + 1]);
            		sscanf(argv[i + 1], "%x", &logOgniXRigheSwitch);
                } else if (!strcmp(argv[i],"-u") || !strcmp(argv[i],"--tipoUnimarc")) {

                	if ( !strcmp(argv[i+1],"RIDOTTO"))
        				TIPO_UNIMARC = TIPO_UNIMARC_RIDOTTO;
        			else if ( !strcmp(argv[i+1],"STANDARD"))
        				TIPO_UNIMARC = TIPO_UNIMARC_STANDARD;
        			else
        			{
        				printf ("\nWARNING: tipoUnimarc '%s' sconosciuto, default to STANDARD", argv[i+1]);
        				TIPO_UNIMARC = TIPO_UNIMARC_STANDARD;

        			}
                } else if (!strcmp(argv[i],"-s") || !strcmp(argv[i],"--tipoScarico")) {
				{
                	if ( !strcmp(argv[i+1],"UNIMARC"))
                		TIPO_SCARICO = TIPO_SCARICO_UNIMARC;
        			else if ( !strcmp(argv[i+1],"OPAC"))
                		TIPO_SCARICO = TIPO_SCARICO_OPAC;
        			else
        			{
        				printf ("\nWARNING: tipoScarico '%s' sconosciuto, default to UNIMARC", argv[i+1]);
                		TIPO_SCARICO = TIPO_SCARICO_UNIMARC;
        			}
				}

                } else if (!strcmp(argv[i],"-b") || !strcmp(argv[i],"--bidErrati")) {
						BIDERRATIOUT.assign((char *)argv[i+1]);

                } else if (!strcmp(argv[i],"-c") || !strcmp(argv[i],"--incrementale_dal")) {
                	if (incrementale_dal)
                		incrementale_dal->Equals((char *)argv[i+1]);
                	else
                		incrementale_dal = new CString((char *)argv[i+1]);




                } else if (!strcmp(argv[i],"-m") || !strcmp(argv[i],"--markFileOut")) {
                	MARKFILEOUT.assign((char *)argv[i+1]);

                } else if (!strcmp(argv[i],"-x") || !strcmp(argv[i],"--xmlMarkFileOut")) {
                	MARKFILEOUTXML.assign((char *)argv[i+1]);

	} else if (!strcmp(argv[i],"-y") || !strcmp(argv[i],"--sistemaNumerico")) {
		if(!strcmp(argv[i+1],"decimale"))
		{
				SISTEMA_NUMERICO_UNIMARC = SISTEMA_NUMERICO_DECIMALE;
				MAX_RECORD_SIZE = 99999;
				MAX_FIELD_SIZE = 9999;
		}
		else if(!strcmp(argv[i+1],"esadecimale"))
		{
				SISTEMA_NUMERICO_UNIMARC = SISTEMA_NUMERICO_ESADECIMALE;
				MAX_RECORD_SIZE = 0xFFFFF;
				MAX_FIELD_SIZE = 0xFFFF;
		}
		else
			printf ("\nSistema numerico sconosciuto: %s. Lasciamo il sistema definito precedentemente dal file di configurazione o dal default", argv[i+1]);
		}
	else if (!strcmp(argv[i],"-j") || !strcmp(argv[i],"--idXunimarc")) { // bid list da trattare
		IDXUNIMARC.assign((char *)argv[i+1]);

	} else if (!strcmp(argv[i],"-p") || !strcmp(argv[i],"--processLog")) { // file per controllo processo
		PROCESSLOG.assign((char *)argv[i+1]);

	} else if (!strcmp(argv[i],"-q") || !strcmp(argv[i],"--esportaSoloInventariCollocati")) { // file per controllo processo
		esportaSoloInventariCollocati=true;

	} else if (!strcmp(argv[i],"-z") || !strcmp(argv[i],"--indiciBinari")) { // 13/04/2015

		if(!strcmp(argv[i+1],"ASCII"))
			OFFSET_TYPE = OFFSET_TYPE_ASCII;
		else if(!strcmp(argv[i+1],"BINARY"))
			OFFSET_TYPE = OFFSET_TYPE_BINARY;
		else
			printf ("\nWARNING: opzione -z/,--indiciBinari indici possono essere ASCII (default) o BINARY");

	} else {
		std::cout << "Not enough or invalid arguments, please try again.\n";
		exit(1);
	}
        }

		if (tagsFilename)
		{
			if (!addexportTagsfromFile(tagsFilename, ini))
			{
                printf ("\nNo export tags defined in %s, please try again.",tagsFilename);
                exit(1);
			}
		}

		if (elaboraNRigheSwitch != -1)
			ELABORANRIGHE = elaboraNRigheSwitch;
		if (iniziaElaborazioneDaSwitch != -1)
			INIZIAELABORAZIONEDARIGA = iniziaElaborazioneDaSwitch;
		if (logOgniXRigheSwitch != -1)
			LOGOGNIXRIGHE = logOgniXRigheSwitch;


	} // End // Gestione switch


	// 13/04/2015 Controlliamo se usiamo gli indici binari per cambiare il path agli indici
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY)
	{

		printf ("\nRimappiamo i percorsi per gli indici binari con '/boff'");

		CString * cStrPtr;
		for (int i=0; i < offsetVector->length(); i++)
		{
			cStrPtr =  offsetVector->Entry(i);

			long pos = cStrPtr->Last('/');
			if (pos)
				cStrPtr->InsertStringAt("/boff", pos);
		}
	}




	delete ini;


	// Segnala inizio del processo di creazione file unimarc
	if (!PROCESSLOG.IsEmpty())
	{
		cFile = new CFile(PROCESSLOG.data(), AL_WRITE_FILE);
		sprintf ( locBuf, "\nInizio processo di creazione file unimarc - %s, %s", ctime (&rawtimeStart), PROCESSLOG.data());
		cFile->Write(locBuf);
		delete cFile;
	}

	// For a quick access to the tags to export list...
	int tagsToGenerate = exportTags.length();
/***
	char *tagsToGenerateBufferPtr = (char *)malloc(tagsToGenerate*(3)); // 3 caratteri per tag
	char *ptr = tagsToGenerateBufferPtr;
	for (int i=0; i < tagsToGenerate; i++)
	{
		memcpy (ptr, exportTags.Entry(i)->data(), 3);
//printf("\ntagsToGenerate %d = %s",i, exportTags.Entry(i)->data());
		ptr+=3;
	}
***/
	// Nuova gestione per un accesso veloce senza ricerca dicotomica!!
	int maxTags=10*100+1;
	tagsToGenerateBufferPtr = (char *)calloc(maxTags, sizeof(char)); // 10 blocchi da 100 bytes settati a false +1 per start a 1 e non a 0

 	CString *tagPtr;
	int tag;
	for (int i=0; i < exportTags.length(); i++)
	{
		tagPtr = exportTags.Entry(i);
		tag = atoi (tagPtr->data());
		*(tagsToGenerateBufferPtr+tag) = 1;	// set to true
	}

	printf ("\nbid list: %s", IDXUNIMARC.data());
	printf ("\nExporting tags: ");
	for (int i=0; i < maxTags; i++)
	{
		if (*(tagsToGenerateBufferPtr+i) == 1)
			printf ("%03d, ", i);
	}




//	exportTags.DeleteAndClear();


	// Stiamo trattando i documenti?
	Marc4cpp *marc4cpp = new Marc4cpp();

	if (AUTHORITY.isEqual("documento"))
	{
		try {
		retb = marc4cpp->setupAuthDocument(
				tagsToGenerateBufferPtr,
				tagsToGenerate,
				MARKFILEOUT.data(),
				MARKFILEOUTTXT.data(),
				MARKFILEOUTXML.data(),
				POLO.data(),
				DESCPOLO.data(),
				BIBLIOTECARICHIEDENTESCARICO.data(),
				entitaVector, relazioniVector, offsetVector, dictionaryVector,
				IDXUNIMARC.data(),
				RETICOLO_OUT.data(),
				BIDERRATIOUT.data(),
				sezioniDiCollocazioneDaNonMostrareIn960KV,
				bibliotecheDaNonMostrareIn950KV
				);
		} catch (GenericError e) {
			std::cout << e.errorMessage;
		}
		if (!retb)
		{
			free (tagsToGenerateBufferPtr);
			delete marc4cpp;
			exportTags.DeleteAndClear();
			return 1;
		}

		// 23/03/2015
		time ( &rawtimeEnd );
		double diff = difftime(rawtimeEnd, rawtimeStart);
		int	ore, minuti, secondi;
		ore 	= diff/3600;
		minuti 	= ((int)diff/60)%60;
		secondi = (int)diff%60;
		printf ( "\nTempo di SETUP per elaborazione %.2d:%.2d:%.2d\n", ore, minuti, secondi);


		marc4cpp->CreaUnimarcBibliografico(RECORDUNIMARCSUSINGOLARIGA, INIZIAELABORAZIONEDARIGA, POSITIONBYOFFSET, ELABORANRIGHE, LOGOGNIXRIGHE);
	} // end if authotity.Equals("documento")
	else if (AUTHORITY.isEqual("autore"))
	{
		try {
		retb = marc4cpp->setupAuthAutori(
				tagsToGenerateBufferPtr,
				tagsToGenerate,
				MARKFILEOUT.data(),
				MARKFILEOUTTXT.data(),
				POLO.data(),
				entitaVector, relazioniVector, offsetVector,
				IDXUNIMARC.data(),
				RETICOLO_OUT.data(),
				EXPORT_VIAF,
				EXPORT_MAX_VIAFS
				);

		} catch (GenericError e) {
			std::cout << e.errorMessage;
		}
		if (!retb)
		{
			free (tagsToGenerateBufferPtr);
			delete marc4cpp;
			return 1;
		}
        marc4cpp->CreaUnimarcAuthority(INIZIAELABORAZIONEDARIGA, POSITIONBYOFFSET, ELABORANRIGHE, LOGOGNIXRIGHE);
	} // End if (AUTHORITY.isEqual("autore"))

	else if (AUTHORITY.isEqual("soggetto"))
	{

		try {
		retb = marc4cpp->setupAuthSoggetti(
				tagsToGenerateBufferPtr,
				tagsToGenerate,
				MARKFILEOUT.data(),
				MARKFILEOUTTXT.data(),
				POLO.data(),
				entitaVector, relazioniVector, offsetVector,
				IDXUNIMARC.data(),
				RETICOLO_OUT.data()
				);

		} catch (GenericError e) {
			std::cout << e.errorMessage;
		}
		if (!retb)
		{
			free (tagsToGenerateBufferPtr);
			delete marc4cpp;
			return 1;
		}

        marc4cpp->CreaUnimarcAuthority(INIZIAELABORAZIONEDARIGA, POSITIONBYOFFSET, ELABORANRIGHE, LOGOGNIXRIGHE);

	} // End if (AUTHORITY.isEqual("soggetto"))
	else if (AUTHORITY.isEqual("titolo_uniforme"))
	{

		try {
		retb = marc4cpp->setupAuthTitoliUniformi(
				tagsToGenerateBufferPtr,
				tagsToGenerate,
				MARKFILEOUT.data(),
				MARKFILEOUTTXT.data(),
				POLO.data(),
				entitaVector, relazioniVector, offsetVector,
				IDXUNIMARC.data(),
				RETICOLO_OUT.data()
				);

		} catch (GenericError e) {
			std::cout << e.errorMessage;
		}
		if (!retb)
		{
			free (tagsToGenerateBufferPtr);
			delete marc4cpp;
			return 1;
		}

        marc4cpp->CreaUnimarcAuthority(INIZIAELABORAZIONEDARIGA, POSITIONBYOFFSET, ELABORANRIGHE, LOGOGNIXRIGHE);

	} // End if (AUTHORITY.isEqual("titoli_uniformi"))


	else
	{
		printf("Authority %s not recognized", AUTHORITY.data());
		return false;
	}



	delete marc4cpp;
	free (tagsToGenerateBufferPtr);
	exportTags.DeleteAndClear();

//	sezioniDiCollocazioneDaNonMostrareIn960KV.DeleteAndClear();


	time ( &rawtimeEnd );
	printf ("\n---------------------------------------------------------------");

	printf ( "\n\nbinarySearchMemoryCtr : %lld", binarySearchMemoryCtr); // 04/02/2015
	printf ( "\nbinarySearchFileCtr : %lld", binarySearchFileCtr);
	printf ( "\nbinarySearchFileCtrSeekTo : %lld", binarySearchFileCtrSeekTo);



	int memcpyCalls = 0;
	for (int i=0; i < 33; i++)
		memcpyCalls += memcpySizeCtr[i];
	if (memcpyCalls)
	{
		printf ( "\n\nMemcpy trace for %d calls:", memcpyCalls);
		for (int i=0; i < 33; i++)
			printf ("\n\t%d=%d",i, memcpySizeCtr[i]);
		printf ("\n\tgt 32=%d", memcpySizeCtr[33]);
	}






	printf ( "\n\nFine elaborazione : %s", ctime (&rawtimeEnd));

	double diff = difftime(rawtimeEnd, rawtimeStart);
	int	ore, minuti, secondi;
	ore 	= diff/3600;
	minuti 	= ((int)diff/60)%60;
	secondi = (int)diff%60;

	//int minuti = (int)diff/60;
	//printf ( "\nTempo di elaborazione : %d minuti e %d secondi\n", minuti, (int)(diff-(minuti*60)));
	printf ( "\nTempo di elaborazione %.2d:%.2d:%.2d\n", ore, minuti, secondi);



	if (!PROCESSLOG.IsEmpty())
	{
		// Segnala fine del processo di creazione file unimarc
		cFile = new CFile(PROCESSLOG.data(), AL_APPEND_FILE_BEFORE_EOF);
		sprintf ( locBuf, "\nFine processo di creazione file unimarc - %s", ctime (&rawtimeEnd));
		cFile->Write(locBuf);
		delete cFile;
	}


	return 0;
} // fine offlineExport



bool addexportTagsfromFile(const char *tagsFilename, cIni *ini)
{
	CString tagsLine;

	// Reset eventuali export
	exportTags.DeleteAndClear();


	// Apri il file
	CFile *tagsFileIn=0; //= new CFile(tagsFilename);
	try{
		tagsFileIn = new CFile(tagsFilename, AL_READ_FILE);
	} catch (GenericError e) {
		if (tagsFileIn)
			delete tagsFileIn;
		std::cout << e.errorMessage;
		return false;
	}

	while(!tagsFileIn->Eof())
	{
		if (!tagsLine.ReadLine(tagsFileIn))
			break;
		if (tagsLine.GetFirstChar() == '#' || tagsLine.IsEmpty())
			continue;

		ini->SplitIniFields(tagsLine.data());
//		ini->SplitIniFields(tagsLine.data(), true); // 11/03/2016

		if (!ini->fieldsVector->Entry(0)->Compare("exportTags"))
			addExportTags(ini->fieldsVector->Entry(1)->data());

	}

	delete tagsFileIn;
	return true;
}

void readConfig(CFile *iniFileIn, cIni *ini)
{
	// Setup defaults
	NSB = "\033H"; // No Sort Begin
	NSE	= "\033I"; // No Sort End
	EXPORT_MAX_VIAFS = 5; // DEFAULT

CString configLine; // (256)
while (configLine.ReadLine(iniFileIn)) // ReadLineWithPrefixedMaxSize
{
	if (configLine.GetFirstChar() == '#' || configLine.IsEmpty())
		continue;

	ini->SplitIniFields(configLine.data());
//	ini->SplitIniFields(configLine.data(), true); // 11/03/2016

//printf ("\nargepf %s", ini->fieldsVector->Entry(0)->data());

	if (!ini->fieldsVector->Entry(0)->Compare("database"))
	{
		if(ini->fieldsVector->Entry(1)->isEqual("indice") || ini->fieldsVector->Entry(1)->isEqual("INDICE"))
		{
			DATABASE_ID = DATABASE_INDICE;
		}
		else if(ini->fieldsVector->Entry(1)->isEqual("sbnweb") || ini->fieldsVector->Entry(1)->isEqual("SBNWEB"))
		{
			DATABASE_ID = DATABASE_SBNWEB;
		}
		else
			printf ("\ndatabase sconosciuto: %s. Attivato di default il db di SBNWEB", ini->fieldsVector->Entry(1)->data());
	}

	 else if (!ini->fieldsVector->Entry(0)->Compare("incrementale_dal")) {
		if (incrementale_dal)
			incrementale_dal->Equals((char *)ini->fieldsVector->Entry(1));
		else
			incrementale_dal = new CString((char *)ini->fieldsVector->Entry(1));
	 }


	else if (!ini->fieldsVector->Entry(0)->Compare("estrai_nature"))
		estrai_nature.assign(ini->fieldsVector->Entry(1));


	else if (!ini->fieldsVector->Entry(0)->Compare("authority"))
		AUTHORITY.assign(ini->fieldsVector->Entry(1));

	else if (!ini->fieldsVector->Entry(0)->Compare("sistema_numerico_unimarc")) // 20/09/2017
	{
		if(ini->fieldsVector->Entry(1)->isEqual("decimale"))
		{
				SISTEMA_NUMERICO_UNIMARC = SISTEMA_NUMERICO_DECIMALE;
				MAX_RECORD_SIZE = 99999;
				MAX_FIELD_SIZE = 9999;
		}
		else if(ini->fieldsVector->Entry(1)->isEqual("esadecimale"))
		{
				SISTEMA_NUMERICO_UNIMARC = SISTEMA_NUMERICO_ESADECIMALE;
				MAX_RECORD_SIZE = 0xFFFFF;
				MAX_FIELD_SIZE = 0xFFFF;
		}
		else
			printf ("\nSistema numerico sconosciuto: %s. Lasciamo il sistema di default decimale", ini->fieldsVector->Entry(1)->data());
	}



	else if (!ini->fieldsVector->Entry(0)->Compare("exportViaf"))
	{
		if (ini->fieldsVector->Entry(1)->isEqual("true"))
			EXPORT_VIAF = true;
	}
	else if (!ini->fieldsVector->Entry(0)->Compare("exportMaxViafs"))
	{
		EXPORT_MAX_VIAFS = atoi(ini->fieldsVector->Entry(1)->data());
	}

	else if (!ini->fieldsVector->Entry(0)->Compare("recordUnimarcSuSingolaRiga"))
		RECORDUNIMARCSUSINGOLARIGA = atoi(ini->fieldsVector->Entry(1)->data());

	else if (!ini->fieldsVector->Entry(0)->Compare("isbnConTrattini"))
		ISBN_CON_TRATTINI = atoi(ini->fieldsVector->Entry(1)->data());


	else if (!ini->fieldsVector->Entry(0)->Compare("processLog"))
		PROCESSLOG.assign(ini->fieldsVector->Entry(1));

	else if (!ini->fieldsVector->Entry(0)->Compare("logNaturaErrata"))
		logNaturaErrata = true;

	else if (!ini->fieldsVector->Entry(0)->Compare("stampaRecordCancellato"))
		stampaRecordCancellato = true;

	else if (!ini->fieldsVector->Entry(0)->Compare("esportaSoloInventariCollocati"))
		esportaSoloInventariCollocati = true;


	else if (!ini->fieldsVector->Entry(0)->Compare("NSB"))
	{
		NSB.Clear();
		unsigned int c1, c2;
		sscanf(ini->fieldsVector->Entry(1)->data(), "%2x%2x", &c1, &c2);
		NSB.AppendChar((unsigned char)c1);
		NSB.AppendChar((unsigned char)c2);
//		NSB = ini->fieldsVector->Entry(1)->data();
	}
	else if (!ini->fieldsVector->Entry(0)->Compare("NSE"))
	{
		NSE.Clear();
		unsigned int c1, c2;
		sscanf(ini->fieldsVector->Entry(1)->data(), "%2x%2x", &c1, &c2);
		NSE.AppendChar((unsigned char)c1);
		NSE.AppendChar((unsigned char)c2);
		//NSE = ini->fieldsVector->Entry(1)->data();

	}

	else if (!ini->fieldsVector->Entry(0)->Compare("idXunimarc"))
		IDXUNIMARC.assign(ini->fieldsVector->Entry(1));
	else if (!ini->fieldsVector->Entry(0)->Compare("reticolo_out"))
		RETICOLO_OUT.assign(ini->fieldsVector->Entry(1));

	else if (!ini->fieldsVector->Entry(0)->Compare("markFileOutBufferSize"))
		MARKFILEOUT_BUFFER_SIZE = atol(ini->fieldsVector->Entry(1)->data());


	else if (!ini->fieldsVector->Entry(0)->Compare("markFileOut"))
		MARKFILEOUT.assign(ini->fieldsVector->Entry(1));
	else if (!ini->fieldsVector->Entry(0)->Compare("markFileOutTxt"))
		MARKFILEOUTTXT.assign(ini->fieldsVector->Entry(1));

	else if (!ini->fieldsVector->Entry(0)->Compare("markFileOutXml"))
		MARKFILEOUTXML.assign(ini->fieldsVector->Entry(1));


//	else if (!ini->fieldsVector->Entry(0)->Compare("titoliCancellati"))
//		titoliCancellati = ini->fieldsVector->Entry(1)->data();
//	else if (!ini->fieldsVector->Entry(0)->Compare("titoliFusi"))
//		titoliFusi = ini->fieldsVector->Entry(1)->data();
//	else if (!ini->fieldsVector->Entry(0)->Compare("titoliLocDaCanc"))
//		titoliLocDaCanc = ini->fieldsVector->Entry(1)->data();
//	else if (!ini->fieldsVector->Entry(0)->Compare("titoliUnimarcRidotto"))
//		titoliUnimarcRidotto = ini->fieldsVector->Entry(1)->data();
//	else if (!ini->fieldsVector->Entry(0)->Compare("titoliVariati"))
//		titoliVariati = ini->fieldsVector->Entry(1)->data();
	else if (!ini->fieldsVector->Entry(0)->Compare("bidErrati"))
			BIDERRATIOUT.assign(ini->fieldsVector->Entry(1));

	else if (!ini->fieldsVector->Entry(0)->Compare("tipoUnimarc"))
	{
			if ( ini->fieldsVector->Entry(1)->isEqual ("RIDOTTO"))
				TIPO_UNIMARC = TIPO_UNIMARC_RIDOTTO;
			else if ( ini->fieldsVector->Entry(1)->isEqual ("STANDARD"))
				TIPO_UNIMARC = TIPO_UNIMARC_STANDARD;
			else
			{
				printf ("\nWARNING: tipoUnimarc '%s' sconosciuto, default to STANDARD", ini->fieldsVector->Entry(1)->data());
				TIPO_UNIMARC = TIPO_UNIMARC_STANDARD;

			}
	}



	else if (!ini->fieldsVector->Entry(0)->Compare("polo"))
		POLO.assign(ini->fieldsVector->Entry(1));
	else if (!ini->fieldsVector->Entry(0)->Compare("descPolo"))
		DESCPOLO.assign(ini->fieldsVector->Entry(1));

	else if (!ini->fieldsVector->Entry(0)->Compare("bibliotecaRichiedenteScarico"))
		BIBLIOTECARICHIEDENTESCARICO.assign(ini->fieldsVector->Entry(1));

	else if (!ini->fieldsVector->Entry(0)->Compare("tipoScarico"))
	{
		if(ini->fieldsVector->Entry(1)->isEqual("OPAC") || ini->fieldsVector->Entry(1)->isEqual("opac"))
			TIPO_SCARICO = TIPO_SCARICO_OPAC;
		else if(ini->fieldsVector->Entry(1)->isEqual("UNIMARC") || ini->fieldsVector->Entry(1)->isEqual("unimarc"))
			TIPO_SCARICO = TIPO_SCARICO_UNIMARC;
		else
			printf ("\nWARNING: tipoScarico '%s' sconosciuto, default to UNIMARC", ini->fieldsVector->Entry(1)->data());
	}

	else if (!ini->fieldsVector->Entry(0)->Compare("iniziaElaborazioneDaRiga"))
		INIZIAELABORAZIONEDARIGA = atol(ini->fieldsVector->Entry(1)->data());
	else if (!ini->fieldsVector->Entry(0)->Compare("positionByOffset"))
	{
		if (ini->fieldsVector->Entry(1)->StartsWith("true"))
			POSITIONBYOFFSET = true;
		else
			POSITIONBYOFFSET = false;
	}
	else if (!ini->fieldsVector->Entry(0)->Compare("exportTags"))
		addExportTags(ini->fieldsVector->Entry(1)->data());

	else if (!ini->fieldsVector->Entry(0)->Compare("sezioniDiCollocazioneDaNonMostrareIn960"))
		addSezioniDiCollocazioneDaNonMostrareIn960(ini->fieldsVector->Entry(1)->data());

	else if (!ini->fieldsVector->Entry(0)->Compare("bibliotecheDaNonMostrareIn950"))
	{
		// Remove delimietrs
		if (ini->fieldsVector->Entry(1)->GetFirstChar() == '"')
		{
			ini->fieldsVector->Entry(1)->ExtractFirstChar(); // "
			ini->fieldsVector->Entry(1)->ExtractLastChar(); // "
		}

		addBibliotecheDaNonMostrareIn950(ini->fieldsVector->Entry(1)->data());
	}

	else if (!ini->fieldsVector->Entry(0)->Compare("bibliotecheDaMostrareIn899"))
	{
		// Remove delimietrs
		if (ini->fieldsVector->Entry(1)->GetFirstChar() == '"')
		{
			ini->fieldsVector->Entry(1)->ExtractFirstChar(); // "
			ini->fieldsVector->Entry(1)->ExtractLastChar(); // "
		}
		bibliotecheDaMostrareIn899KV = new CKeyValueVector(tKVSTRING, tKVSTRING);
		addBibliotecheDaMostrareIn899(ini->fieldsVector->Entry(1)->data());
	}


	else if (!ini->fieldsVector->Entry(0)->Compare("elaboraNRighe"))
		ELABORANRIGHE = atol(ini->fieldsVector->Entry(1)->data());

	else if (!ini->fieldsVector->Entry(0)->Compare("logOgniXRighe"))
		sscanf(ini->fieldsVector->Entry(1)->data(), "%x", &LOGOGNIXRIGHE);


	// TABELLE DI ENTITA'
	else if (!ini->fieldsVector->Entry(0)->Compare("entita"))
	{
		CString * cStrPtr = new CString(configLine.data());

		entitaVector->Add(cStrPtr);
	}
	else if (!ini->fieldsVector->Entry(0)->Compare("relazione"))
	{
		CString * cStrPtr = new CString(configLine.data());
		relazioniVector->Add(cStrPtr);
	}

	else if (!ini->fieldsVector->Entry(0)->Compare("offsetType"))
	{
		if(ini->fieldsVector->Entry(1)->isEqual("ASCII"))
			OFFSET_TYPE = OFFSET_TYPE_ASCII;
		else if(ini->fieldsVector->Entry(1)->isEqual("BINARY"))
			OFFSET_TYPE = OFFSET_TYPE_BINARY;
		else
			printf ("\nWARNING: offsetType '%s' sconosciuto, default to ASCII", ini->fieldsVector->Entry(1)->data());
	}

	else if (!ini->fieldsVector->Entry(0)->Compare("indice"))
	{
		CString * cStrPtr = new CString(configLine.data());

		offsetVector->Add(cStrPtr);
	}
	else if (!ini->fieldsVector->Entry(0)->Compare("codici") || !ini->fieldsVector->Entry(0)->Compare("dizionario"))
	{
		CString * cStrPtr = new CString(configLine.data());
		dictionaryVector->Add(cStrPtr);
	}

	else if (!ini->fieldsVector->Entry(0)->Compare("z899"))
		z899 = true;
//	else if (!ini->fieldsVector->Entry(0)->Compare("xy899"))
//		xy899 = true;
	else if (!ini->fieldsVector->Entry(0)->Compare("check899"))
		check899 = true;
	else if (!ini->fieldsVector->Entry(0)->Compare("incrementale_dal"))
		incrementale_dal = new CString(ini->fieldsVector->Entry(1)->data());
	else if (!ini->fieldsVector->Entry(0)->Compare("_699_sintetica"))
		_699_sintetica = true;


	else
		printf ("\nParametro sconosciuto: %s", configLine.data());
} // End while

if (estrai_nature.Length() == 0)
{ // Metti default
	if (DATABASE_ID == DATABASE_INDICE)
		estrai_nature = estrai_nature_indice.data();
	else
		estrai_nature = estrai_nature_polo.data();
}

} // End readConfig

void printTagsGestiti()
{
	printf ("\n\nTAGS gestiti");
	printf ("\n============");

	printf ("\n\nAuthority DOCUMENTO");
	printf ("\n-------------------");
	printf ("\n0-- Blocco di identificazione");
//			printf ("\n	001,005,010,011,012,013,015,016,017,020,022,071,072,073"); // 02/09/2014 + 016,072,073
//	printf ("\n	001,003,005,010,011,012,013,015,016,017,020,022,071,072,073"); // 21/01/2016 + 003
	printf ("\n	001,003,005,010,011,012,013,015,016,017,020,022,035,071,072,073"); // 17/10/2016 + 035 (solo per POLI)

	printf ("\n1-- Blocco delle informazioni codificate");
//			printf ("\n	100,101,102,105,110,115,116,120,121,123,124,125,126,128,140"); // 20/02/2015 115+126
//			printf ("\n	100,101,102,105,110,115,116,120,121,123,124,125,126,128,140,181,182"); // 00/03/2015 181,182
//			printf ("\n	100,101,102,105,110,115,116,120,121,123,124,125,126,128,140,181,182,183"); // 20/11/2015 183
	printf ("\n	100,101,102,105,110,115,116,120,121,123,124,125,126,127,128,140,181,182,183"); // 28/01/2016 127
	printf ("\n2-- Blocco delle informazioni descrittive");
	printf ("\n	200,205,206,207,208,210,215,225,230");
	printf ("\n3-- Blocco delle note");
//			printf ("\n	300,311,312,314,316,317,323,326,327,330,336,337");
	printf ("\n	300,311,312,314,316,317,321,323,326,327,330,336,337"); // 321 per indice 26/11/2015 (link a siti web)

	printf ("\n4-- Blocco dei titoli in relazione");
	// 27/10/2017 creaTag441_HaPerContinuazioneParziale()
	printf ("\n	410,421,422,423,430,431,434,440,441,447,451,452,454,461,462,463,464,488");
	printf ("\n5-- Blocco dei titoli di relazione");
//	printf ("\n	500,510,520,517,530,532");
	printf ("\n	500,506,510,520,517,530,532,576"); // 506 per titoli di acesso dell'opera, 576 per legami Titolo/Opera con autore

	printf ("\n6-- Blocco dell'analisi semantica");
//			printf ("\n	606,620,676,686");
//			printf ("\n	606,620,676,686, 696"); // 696 (tesauro) solo per Polo 11/12/2015
//	printf ("\n	606,620,676,686, 689"); // 696 diventa 689 (tesauro) solo per Polo 14/01/2016
	printf ("\n	606,620,676,686,689,696,699"); // 699 per le varianti storiche 16/01/2018
	printf ("\n7-- Blocco dei legami alle intestazioni");
	printf ("\n	700,701,702,710,711,712,790,791");
	printf ("\n8-- Blocco di informazioni sulla fonte");
	printf ("\n	801,850,856,899");
	printf ("\n9-- Blocco di uso locale (nazionale)");
	printf ("\n	921,922,923,926,927,928,929,950,951,956,960,961,967,977"); // 01/2015 967,977

	printf ("\n\nAuthority AUTORE");
	printf ("\n----------------");
	printf ("\n0-- Blocco di identificazione");
	printf ("\n	001,005,010,015");
	printf ("\n1-- Blocco delle informazioni codificate");
	printf ("\n	100,101,102,152");
	printf ("\n2-- Blocco delle informazioni descrittive");
	printf ("\n	200,210");// ,230,250,260
	printf ("\n3-- Blocco delle note");
	printf ("\n	300"); // ,305
	printf ("\n4-- Blocco delle relazioni (VEDI)");
	printf ("\n	400,410");// ,460
	printf ("\n5-- Blocco delle relazioni (VEDI ANCHE)");
	printf ("\n	500,510"); // ,560 NOPE
	printf ("\n6-- Blocco delle classificazioni");
	printf ("\n	676,686");
	printf ("\n8-- Blocco di informazioni sulla fonte");
	printf ("\n	801,810,815,830");  // 856 NOPE
	printf ("\n9-- Blocco di uso locale (nazionale)");
	printf ("\n	nessuna"); // 920,921,928,930,931,932 NOPE

	printf ("\n\nAuthority SOGGETTO");
	printf ("\n----------------");
	printf ("\n	001,005");
	printf ("\n	100");
	printf ("\n	250");
	printf ("\n	300");
	printf ("\n	801");
	printf ("\n	931,932");

	printf ("\n\nAuthority TITOLO_UNIFORME");
	printf ("\n----------------");
	printf ("\n001,005");
	printf ("\n100,101,102,152");
	printf ("\n230,231,240,241");
	printf ("\n300");
	printf ("\n431,441");
//	printf ("\n500,501,510,511");	// 19/05/2016
	printf ("\n500,501,510,511,530,531,541");	// 02/03/2017
	printf ("\n801,810,815,830");

}



void testMarcRead()
{
	ReadMarcExample readMarcExample;
	//readMarcExample.read("/home/export/offlineExport/bve/tmp/IE001_BVE_CR_00006446.mrc"); // OK da export offline
	readMarcExample.read((char *)"/home/export/offlineExport/bve/tmp/IEbase_BVE.mrc"); // Unimarc prodotto dal dataprep

}


void testCFile()
{
	CString s;
	CFile *cFile;
	long long ll;
	char const * filename;

	printf ("\ntestCFile\n");

//	filename = "F:/sbnweb/migrazione/ind/db_export/tr_tit_bib.out";
	filename = "tr_tit_bib.out.bytes.srt";
	printf ("\nTesting %s", filename);

	cFile = new CFile(filename);

//	ll = atoll("11889208257");
	ll = atoll("5255578839");

	cFile->SeekToLarge(ll);
	s.ReadLine(cFile);
	printf ("\nLetto %s:", s.data());

	ll = 1247;
	cFile->SeekToLarge(ll);
	s.ReadLine(cFile);
	printf ("\nLetto %s:", s.data());

	delete cFile;

/*
	filename = "E:/sbnweb/migrazione/ind/db_export/tb_autore.out";
	printf ("\nTesting %s", filename);

	cFile = new CFile(filename);


	ll = atoll("349");
	cFile->SeekToLarge(ll);
	s.ReadLine(cFile);
	printf ("\nLetto %s:", s.data());

//	ll = 200;
//	cFile->SeekToLarge(ll);
//	s.ReadLine(cFile);
//	printf ("\nLetto %s:", s.data());

	delete cFile;
*/
	printf ("\nFine testCFile");
}



void printHeader()
{
	printf ("\nOffline Export UNIMARC - (c)Copyright Iccu 2009-2018 (autore Argentino Trombin) - Open source");
//	printf ("\nVersione 1.0.0 del 17/11/2009");
//	printf ("\nVersione 1.0.1 del 19/11/2009"); 	// Rifatta la 200
//	printf ("\nVersione 1.0.2 del 23/11/2009"); 	// Rifatta la 960 in base a doc. di Rossana con aggiunta della $m
//	printf ("\nVersione 1.0.3 del 24/11/2009"); 	//Gestione +note all'inventario
													// Gestione sottoetichette 7xx per legame autore nella 410
													// Gestione 410 per monografie legate a collane tra di loro collegate e non (410 multiple)
//  printf ("\n\nVersione 1.0.4 del 26/11/2009"); 	// Fix vari per la 960 (Mail pasqualetti del 24/11/09)

//	printf ("\nVersione 1.0.5 03/12/2009"); // Fix gestione sezione ion $g della 960
											// Fix memory leak su file offset
											// Fix memory leak generato da stesso file dichiarato + volte
											// Chiusura file offset, entita e relazioni tramite vettore
//	printf ("\nVersione 1.0.6 03/12/2009"); // Chiusura tabelle da vettore
											// Sistemata la 410 BID della collana riportato nell'etichetta 001
//	printf ("\nVersione 1.0.7 04/12/2009"); // Aggiunto all 950 i sottocampi $f, $h, $i, $l e $m
											// Fix elementi in tbMarca e tbcNotaInv
//	printf ("\nVersione 1.0.8 11/12/2009"); // Aggiunto tag 090
											// Forzato $b in tag 020
											// Gestione null per 100,101,421
											// Gestione 440 opzionale
											// 899 generata solo se URI presente
											// 960 rimozione . superflui in $g
//	printf ("\nVersione 1.0.9 14/12/2009");		// Aggiunta gestione 463 per SPOGLI (Titoli analitici, caso d'uso RML RML0077001)

//	printf ("\nVersione 1.0.10 15/12/2009"); 	// Et. 311. Non generare sen non ci sono dati. 16/12/2009 15.27
												// Et. 210. Manca il $a con i relativi dati. E' un errore sulla base che non si vede? 16/12/2009 16.12


//	printf ("\nVersione 1.0.11 15/01/2010");	// Rimossi spazi nelle sottoetichette delle 410
//	printf ("\nVersione 1.0.12 16/01/2010");	// Fixed generazione a vuoto del tag 500
												// Risolto problema asterisco per la 200 rifatta
//	printf ("\nVersione 1.0.13 18/01/2010");	// Sostituito '$' con 0x1f nel file unimarc per i campi nidificati
//	printf ("\nVersione 1.0.14 19/01/2010");	// Spostata la gestione del ricalcolo aree isbd in TbTitolo (ricalcolaAreeIsbdUtf8)
												// Toppava con i tag embedded in quanto metodo venivca richiamato solo per i tag di primo livello
												// Export 950 se biblioteca non tra quelle da scartar
												//	 # Biblioteche che non devono aparire nella 950/951
												//	 bibliotecheDaNonMostrareIn950 " 01, 02"


	// printf ("\nVersione 1.0.15 19/01/2010");	// Gestione 951 come per 950 per quanto concerne le biblioteche che possone essere esportate o no

//	printf ("\nVersione 1.0.16 19/01/2010"); // problema parenti quadra chiusa mancante in 200in 200
											//  Bid UBO0082983 " [1!: *Storia dell'eta moderna : dal Cinquecento all'eta napoleonica / Massimo L. Salvadori."
//	printf ("\nVersione 1.0.17 20/01/2010"); // Risolto problemca cratteri accentati in 225 e 410 (letura record da stringa)
											// Rimozione errori per record non legati a composizioni
											// Risolto problema \r LO11259258  # problema \r

//	printf ("\nVersione 1.0.18 20/01/2010");	// Rimuovere i test sul "null" e testare per stringa vuota
												// Gestione del "null" per tag 100".
												//	Gestione "null"       ->IsEmpty()
												//	    Marc4cppCollocazione
												//	    Marc4cppDocumento
												//	    Marc4cppLegami
												//	    Marc4cppLegami2 controlla 961 No 1 ord




	//printf ("\nVersione 1.0.19 21/01/2010"); // Rivedere indice isbd per titoli uniformi musicali diverso da standard
											 // Sostituiamo alle varie A-0001,R-pppp,S-pppp,U-pppp (p stat per posizione)  la 200-0001
	// printf ("\nVersione 1.0.20 22/01/2010");	// Gestione parametrizzata dei tab NON SORTING (NSB e NSE). Unicode per CFI e esuenze di escape per gli altri

	// printf ("\nVersione 1.0.21 22/01/2010"); // Fix date vuote per tag 100
	//printf ("\nVersione 1.0.22 25/01/2010");	// # Biblioteche che non devono aparire nella 960 e 961 (come per la 950 e 951)
												// bibliotecheDaNonMostrareIn950 " 01, 02"
												// Aggiunte 2 tabelle tbc_possessore_provenienza e 	trc_poss_prov_inventari
												// Generare 500 solo se non ha un autore collegato valida solo per CFI.


	//printf ("\nVersione 1.0.23 26/01/2010");	// BVE, UBO3379970, 454 cercava autore principale invece del traduttore (altri autori). Marc4cppLegami::creaTag454TraduzioneDi
												// BVE, BVE0480371, Marc4cppLegami::creaLegameTitoloAutore gestione per TP_RESP_0_NOME_CITATO_NEL_DOCUMENTO
	// printf ("\nVersione 1.0.24 26/01/2010");	// Creazione record cancellati minimali
												// Gestione parametrizzata tramite file per le biblioteche da escludere dalla 950. Prendere elenco CSV da file. Se file non esiste falle tutte per default.
	// printf ("\nVersione 1.0.25 27/01/2010");	// Fix bug per SBW BNS0003292 # buffer sporco nella seconda $e. Non veniva ripulito il buffer dell-inventario
	// printf ("\nVersione 1.0.26 28/01/2010");	// Gestione parametrizzata della 317
												// Fix bug 105 e 140 per genere. CFI PUVE007724, RMG0036909
												// Fix bug 110 per CFI CFI0407712
												// Rimopzione test su copia_uri per generazione 899 per RML. Rimane problema dell-indicatore 2 quando campo usato per valorizzarlo e' null

	// printf ("\nVersione 1.0.27 28/01/2010");	// Gestione dei campi con $ come indicativo di campo vuoto
												//  tbc_sezione_collocazione
												//   note_sez                ok usato solo per la gestiione delle []
												//   descr                   NON GESTITA
												//   cd_cla                  NON GESTITA
												//  tbc_collocazione
												//   consis                  ok
												//  tbc_esemplare
												//   cons_doc                ok
												//  tbc_inventario
												//   precis_inv              ok
												//  tbc_serie_inventariale  NON GESTITA
												//   descr
												// Corretto bug per ISB della 926. Incipit
												// Aggiunta gestione export > 99.999 bytes. Record viene scritto ma troncato.
	// vesrione 1 del mese 2
	// printf ("\nVersione 1.2.1 01/02/2010");	// Fix lowercase per 102 $a
											// Attivata la gestione dei possesori tag 317, 702, 712
											// Aggiunta $x nella 606 (SBW CFI0197760)
											// Problema 700 ripetute dovute a record cancellati.
											//  Modificato scarico DB per tenere i record cancellati per i soli titoli.
	//printf ("\nVersione 1.2.2 02/02/2010"); // Fixed bug creazione record unimarc. MarcStreamWriter::write(MarcRecord* marcRecord)
											// Rimossi asterischi da 317 e 7x per possessori
											// Sistemati i null prodotto nei .rel (crea relationFile)
											// sistemati i null in tba_ordini durante scarico downloadDb
	//printf ("\nVersione 1.2.3 03/02/2010"); // Fix 317 e 7x possessori per ciclare sulla trcPossProvInventari
	//printf ("\nVersione 1.2.4 03/02/2010");		// Export unimarc parametrizzato in formato XML come per CFI

	// ------------------------------------
	// Release prima di passare all'Indice
	// printf ("\nVersione 1.2.5 04/02/2010");	// Rimessa virgola in $b della 70x. Richiesta Dina Pasqualetti, mail
											// Gestione soggettario CFI -> "nsogi" per 606,
	// ------------------------------------

	//printf ("\nVersione 1.2.6 05/02/2010");	// Export parametrico in formato XML secondo .XSD di CFI
											// Fix per export record troppo grandi
											// Fix $e seq_coll (padded) 950 per BVE
	//printf ("\nVersione 1.2.7 09/02/2010");	// Fix bug mantis 3540. Non veniva generata la 410
											// Fix risolvere il problema del null nel leadere con le collane
	//printf ("\nVersione 1.2.8 11/02/2010");	// Mantis 3550 problema in 440
										// Attivata la derefenziazione per tbf_biblioteca. Accesso multi DB
	//printf ("\nVersione 1.2.9 15/02/2010"); // Rimessa punteggiatura " : " nelle 71x
											// 500 rimosso test // Generare solo se non ha un autore collegato
											// 712 $5 prendi info da getBibliotecaRichiedenteScarico()

	// printf ("\nVersione 1.2.10 17/02/2010");	// Aggiunta Gestione file offset da disco
	//printf ("\nVersione 1.2.11 18/02/2010");	// Rimossa gestione della tbfBibliotecaInPolo per authority documento.
												//  Gestione fatta tramite tbf_biblioteca
												// Fix bug 210 $d (LO11213146 di indice) e LO10826791 BVE
												// Aggiunta $4 (codice di relazione) alla 700
												// Fixed bug in addCollocazione() per bidColl

	//printf ("\nVersione 1.2.12 25/02/2010");	// Gestionle 64 (toppa con file piccoli il long long) bit per accesso TrTitBib di indice
												// Fix bug $d 210 (BVE URB0441456 )
												// $5 della 317/7xx per possessori modificata

//	printf ("\nVersione 1.2.13 26/02/2010");	// Aggiustata la tbTitolo::loadRecord, tb::loadRecord ecc.
												// Aggiornate altre tabelle derivate da tb per il metodo loadRecord()

//	printf ("\nVersione 1.3.1 03/03/2010");		// Fix $b 961
												// Fix empty 950 $d in 316
//	printf ("\nVersione 1.3.1a 03/03/2010");	// Indice prodice (quasi) correttamente UFI0014737, UFI0018781 e SBL0360199
//	printf ("\nVersione 1.3.2 04/03/2010");		// Fix problema seekToLarge in memoria
												// Gestione $5 317 e 7xx pe rpossessori con codifica bilioteca SBN per RML
//	printf ("\nVersione 1.3.2 05/03/2010");		// Fix $b in 961. Tracciato era errato
//	printf ("\nVersione 1.3.2a 05/03/2010");	// Fix $b in 961. Left padding and not right paddine

//	printf ("\nVersione 1.3.3 09/03/2010");		// Fix 210 dentro le tonde '()'
												// Fix spazi per RML in 317/7xx possessori
//	printf ("\nVersione 1.3.4 15/03/2010");		// Rivisitazione della 899 secondo nuove specifiche

//	printf ("\nVersione 1.3.5 17/03/2010");		// Rimosso log per tb_impronta
												// Segmentation fault CFI SBL0488523
												// Fix 960 $g
//	printf ("\nVersione 1.3.6 25/03/2010");		// Fix livello bibliografico per spogli SBW0000222

//	printf ("\nVersione 1.3.6 29/03/2010");		// Fix $c della 921. Aggiunto tabella tb_repertorio e tr_rep_mar
//	printf ("\nVersione 1.3.7 30/03/2010");		// Gestione nuovo tag 956 per l'accesso al digitale

//	printf ("\nVersione 1.3.8 31/03/2010");		// Nuova gestione per la 961 su richiesta CFI

//	printf ("\nVersione 1.4.1 12/04/2010");		// Fix 961 - per specifiche errate su data ordine
												//         - per test su ordine continuativo

//	printf ("\nVersione 1.4.2 14/04/2010");		// Gestione exportTags commentati in file di configurazione
//	printf ("\nVersione 1.5.1 05/05/2010"); 	// Aggiunta tabella tb_nota e tags 323,327,330,336,337
												// Aggiunta la 856 per indice
												// Gestione id OPAC IT\ICCU\xxx\yyyyyy o IT\ICCU\xxxV\yyyyy
												// finita la 927
												// Aggiunta la tabella TrPerInt
												// Gestione singolo separatore (0xC0)per trTitBib e tbTitolo
//	printf ("\nVersione 1.5.2 12/05/2010");		// 311 e 312 search non unique
//	printf ("\nVersione 1.5.2a 12/05/2010");		// fixed bug for missing tbNota

//	printf ("\nVersione 1.5.b 13/05/2010"); // Fix ricerca per trtittit in 311 e 312

//	printf ("\nVersione 1.5.c 14/05/2010");	// fix 899 per RML

//	printf ("\nVersione 1.5.3 17/05/2010"); // Gestione tbc_sezione_collocazione

//	printf ("\n\nVersione 1.5.4 18/05/2010"); // Finita la 960 con gestione a formato/esplicita
//	printf ("\n\nVersione 1.5.5 18/05/2010"); // Aggiunta $9 a 961
//	printf ("\n\nVersione 1.5.6 31/05/2010"); // Giliberto (CFI) comunica nuiova regola per cdloc della $g nella 960
//	printf ("\n\nVersione 1.5.7 31/05/2010"); // Giliberto (CFI) comunica nuiova regola per cdloc della $g nella 960 per esplicita non strutturata
//	printf ("\n\nVersione 1.5.8 31/05/2010");		// Fix bug SignalAWarning buffer too small
//	printf ("\n\nVersione 1.6.1 01/06/2010");  // Rimosso messaggio di record non trovato per Impronta. Aggiunto ".   (("
//	printf ("\n\nVersione 1.6.2 03/06/2010");	// Fix per aree ISBD non congrue
//	printf ("\n\nVersione 1.6.3 04/06/2010");	// Fix 927 con nuove regole (Tersigni/Paolucci)
//	printf ("\n\nVersione 1.6.4 07/06/2010");	// Parametrizzazione da switch
												//# '-i o --iniziaElaborazioneDa N' da switch
												//# '-e o --elaboraNRighe N' da switch
												//# '-l o --logOgniXRighe N' da switch
												//# '-t o --tags E:/SbnWeb/migrazione/ind2/tagsToExport.txt' da switch

//	printf ("\n\nVersione 1.6.5 /06/2010");	 // fix 463 mancanti per RML. Mantis 0003794
//	printf ("\n\nVersione 1.6.6 21/06/2010");	// fix swicth in esadecimale per log counter
												// Aggiunto switch -b o --bidErrati
												// Fix $g 960 per buffer sporco
//		printf ("\n\nVersione 1.7. 05/07/2010"); // Aggiunto switch -m o --markFileOut
//		printf ("\n\nVersione 1.7. 13/07/2010"); // Aggiunto switch -j o --idXunimarc
//		printf ("\n\nVersione 1.8. 02-11/08/2010"); // Segnalazioni Roveri 02/08/2010
												// Fix 001 per antico
												// Fix 005 per indice
												// Fix 100 per indice
												// Fix 101 per indice
												// Fix 012 multiplo e aggiunta $9
												// Fix 102
												// Fix 200 problema escapes (problema di marc edit)
												// Fix 210
												// Fix 311/312
												// Fix 500 per id antico ed autori
												// Fix 801 per indice
												// Fix 620 per indice
												// Fix per 7xx responsabilita' inferiori per indice
												// Fix 899 per indice
												// Fix 116, 120,121,123,124  se vuoto
												// Fix 010 per tipo K
												// Fix 676 per indice
												// Fix 922, + $t e$u invertite
												// Fix 923
												// Fix
												// Fix 926

												// Genera 856 per tutti i poli (richiesta CFI)
												// 09/08/2010   Gestione livelli gerarchici per tutte le nature con legami 01 (C, M, N, S, W)
												// Fixed bug in atol per existsRecord in tb.cpp
//		printf ("\n\nVersione 1.8.1 25/08/2010"); // Fix authority autore per indice
//		printf ("\n\nVersione 1.9.1 06/09/2010"); // Aggiunta $2 in 017
//		printf ("\n\nVersione 1.9.2 06/09/2010"); // Fix bug gestione numero standard (void Marc4cppDocumento::elaboraDatiTipoNumeroStandard())
//		printf ("\n\nVersione 1.9.3 06/09/2010"); // Generazione tag 105 solo se esiste un genere valido
												 // Fix tag 100 "itac"


		//printf ("\n\nVersione 1.9.1 09/09/2010"); // 09/09/2010 Rifatto metoto elaboraLivelloGerarchicoNotizia in base a logica di Indice
		//printf ("\n\nVersione 1.9.2 20/09/2010"); 	// Fix pos 17 e 18 in leader
														// Rimozione spazio prima di $... nella 200, 410, 225,500
														// Fix $9 in 311, 314
														// Fix bid formato iccu in 410, 314
														// No sort in $9 della 500
														// $e solo se valorizzata in 517
														// Gestione campi vuoti in 92x
														// 801 indicatore 2 posto a '3'
														// 79x Gestione $y
														// 71x getione qualificazioni tipo R con aggiunta $e
														// 70x gestione qualificazioni con '-' trattino
														// 70x convewrsione '#' e '_' in ' '
														// 71x e 791: sottocampo non preceduto da spazio nelle qualificazioni
														// 7xx qualificazioni gestione spazio prina del < eg ' <'
														// 70x e 790, : i nomi personali in forma diretta che in SBN sono scanditi da �spazio due_punti spazio� (es.: �Dioscorides : Anazarbensis�)
														// 517 gestione campi vuoti
														// 500 $9:  se l�autore � un ente, non devono comparire gli asterischi
														// 210 comincia con un $d non giustificato; doveva essere $a
														// 520 e 532 opzionali

//		printf ("\n\nVersione 1.9.2 21/09/2010"); 	// Fix bug documento Roveri export_14_09_2010.doc
//		printf ("\n\nVersione 1.9.2 22/09/2010"); 	// Fix bug documento Roveri riunione 21/09/2010

//		printf ("\n\nVersione 1.9.3 27/09/2010");	// Fixis vari di Partizia
//		printf ("\n\nVersione 1.10.1 04/10/2010");	// Fixis vari di Roveri
//		printf ("\n\nVersione 1.10.2 05/10/2010");	// Gestione 410 per indice + fix riunione con Roveri 01/10/2010
//		printf ("\n\nVersione 1.10.3 06/10/2010");	// Osservazioni Roveri 30/09
//		printf ("\n\nVersione 1.10.4 12/10/2010"); 	// Fix per caricamento Inera 462
//		printf ("\n\nVersione 1.10.5 13/10/2010"); 	// Log allocated memory for files e gestione ordinamento 899
//		printf ("\n\nVersione 1.10.6 18/10/2010");	// Fix 856 per CFI
//		printf ("\n\nVersione 1.10.7 19/10/2010");	// ReFix 856 per CFI + altri fix Roveri 410, 210 ecc..
//		printf ("\n\nVersione 1.10.8 19/10/2010");	// Altri fix Roveri 410, 210 ecc..


		//printf ("\n\nVersione 1.10.9 22/10/2010");	// Fix vari + gestionedi lettura stringa a lunghezza fissa con file in memeoria
													// In creazione reticolo rimozione chiamata ricorsiva da SOGGETTO, CLASSE, MARCHE e LUOGO

		//printf ("\n\nVersione 1.11.1a 10/11/2010");	// Fix problema 215 CFI cper area note cje comincia con ' (('

		// printf ("\n\nVersione 1.11.2a 10/11/2010"); 	// Segnalazioni mail Roveri del 4/11
														// 1) 899 non scaricare le localizzazioni per le collane IT\ICCU\CFI\0010621
														// 2) una notevole percentuale di record di collezione che presentano, nelle posizioni 5-7 del leader, i caratteri 'n s'. IT\ICCU\CFI\0001538
														//		bool Marc4cpp::elaboraLeader() test if (chr && chr != ' ') // 05/11/2010 14.52
														// 3) Nei tag 70x i nomi di persona con qualificazioni (< >). IT\ICCU\RMLE\000449
//		printf ("\n\nVersione 1.11.2b 10/11/2010"); 	// Segnalazioni mail Roveri del 4/11
														// 4) Lo spacchettamento degli ISBN italiani IT\ICCU\UBO\1386502, IT\ICCU\AQ1\0001936
														//		CMisc::gestisciTrattiniNumStandard BASTARDISSIMO!!!!!

														// 5) Il numero di sequenza che si trova in un legame titolo-titolo viene collocato nel sottocampo annidato $17xx, anzich� in $1200
														//		IT\ICCU\BRI\0021861 462 e IT\ICCU\VEA\1022785 463
														// 6) Il tag 105 (raccomandato ma non obbligatorio) � inutile se non contiene altro che caratteri di riempimento:
														//		IT\ICCU\TO0\0952970
//		printf ("\n\nVersione 1.11.2c 10/11/2010"); 	// Segnalazioni mail Roveri del 4/11
														// 7) E' ancora molto frequente il mancato riconoscimento di 210 $d e 210 $h
														// IT\ICCU\UFE\0871970, CMisc::isDate
														// gestione flag "-p"
//		printf ("\n\nVersione 1.11.3 15/11/2010"); 		// Gestione tempi per log di lavorazione blocco
														// Rimosso memory leak Marc4cppLegami::creatTag7xxNidificato
														// Segnalazioni mail Roveri del 4/11
														//	# 620, recuperare il luogo controllato ;
														//	# 410, La perdita di articolo iniziale sembra verificarsi in presenza di una sottocollezione (tipo: "*La* collezione. Sottocollezione").
														//	# CMisc, ISBN a 13 cifre: non � opportuno separare col trattino il segmento iniziale (978, 979)
														//  # 010 Gestione Note isbn

		// printf ("\n\nVersione 1.11.4 16/11/2010"); 		// Rimossa OrsBool CString::AppendString(const OrsChar *aString, OrsLong Size)
														// Sostituendo le

														// Rimossa 		bool	CString::assign(const OrsChar * aString)
														//				OrsBool CString::AppendString(const OrsChar *aString)
														// Modificata	bool	CString::assign(OrsChar * aString) rimosssa clear
														// Aggiunta		OrsBool CString::assign(OrsChar *aString, OrsLong Size)
														// Aggiunta 	OrsChar	* CTokenizer::GetTokenLength()
														// Aggiunta		OrsChar	* CTokenizer::GetToken(	OrsBool CanBeQuoted, const OrsChar *aSeparatorSequence, long *tokenLength)

														// Modificato 	bool Tb per gestione getToken e assign nuove

														// Sostituire le assign("string") con assign("string", len) e getToken ...
														//		void TbReticolo::assign(char *record)
														//		void Tb950Coll::loadRecord(long offset)
														//		bool Tb950Coll::loadNextRecord(char *bid)
														//		bool Tb950Coll::loadNextRecord()
														//		bool TbcNotaInv::loadNextRecord(const char *key)
														//		bool TrAutAut::loadNextRecordDaIndice(const char *key, bool inverted)
														//		bool TrRepMar::loadNextRecord(const char *key)
														//		bool TsLinkMultim::loadNextRecord(const char *key)
														//		CMisc
														//		bool Biblio950::addInventario(Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, Tb950Ese *tb950Ese)
														//		Marc4cppLegami, Marc4cppLegami2, TrRel

//		printf ("\n\nVersione 1.11.5 17/11/2010");		// Sostituire chiamate da OrsBool AppendString(OrsChar *aString);
														//						a OrsBool AppendString(OrsChar *aString, OrsLong Size); dove possibile

//		printf ("\n\nVersione 1.11.6 17/11/2010");		// Riducendo chiamate senza lunghezza stringa
										//		11.78    3.83  776472/2286279     CString::operator=(char*) [13]
										//		17.02    5.53 1122215/2286279     CString::operator=(char const*) [9]
										// new Subfield instanziato e valorizzato in una botta
//		printf ("\n\nVersione 1.11.7 22/11/2010");
									//#ifdef TRACK_MEMORY_LEAKS
									//    #include "nvwa/debug_new.h"
									//#endif
									// Regression test su SBW OK

		//printf ("\n\nVersione 1.11.8 23/11/2010"); // Attivazione memcpy veloce !!!! NON RIESCO A COMPIALRE
							// Rimosso nvwa (contollo memory leaks) in locale e remoto

		//printf ("\n\nVersione 1.11.9 24/11/2010"); 	// Regression fix per 410 CFI creaTag410_InCascata_Polo
													// Regression fix vari per CFI

//		printf ("\n\nVersione 1.11.10 24/11/2010"); 	// Fix 454 mail roveri lun 22/11/2010 12.40
														// 	Rimosso test per autore legato
														// Rimosso 	void MarcStreamWriter::getDataElement
														//			void MarcStreamWriter::getEntry
													// Ottimizzato Marc4cppLegami::getBidColl
													// Subfield:: inizializza le stringed con un carattere invece che di una stringa con un carattere
													// DataField::  come sopra
													// CString:: Messe ResetNoResize al posto di Clear
													// --
													// Marc4cppDocumento::creaTag200_NonAnticoNew
													// DataField * Marc4cppDocumento::creaTag101_LinguaPubblicazione()
													// void Marc4cppLegami::elabora44x(char *bid)

		//printf ("\n\nVersione 1.11.11 25/11/2010");	//
		// [8]     46.9   80.58    2.55  227627         CString::operator=(char*) [8]
		// OK Marc4cppLegami::elabora46y(char*, TbReticoloTit*) [19]
		// OK Marc4cppLegami::elabora46x(char*, bool) [32]
		// OK Marc4cppLegami::creaTag899_Localizzazione() [17]
		// OK CMisc::isDate(char*) [44]
		// Rimesso l'autore nella 454
		// Divisione tramite shift in memcpy.c
		// OK Marc4cppLegami::contaBidColl(char*) [45]
		// OK Marc4cppLegami::creaLegamiTitoloTitolo() [9]

//		printf ("\n\nVersione 1.11.12 25/11/2010");	//
		// OK Marc4cppLegami::creaLegamiTitoloTitolo() [9]
		// OK MarcRecord::toString() [35]
		// OK Marc4cppDocumento::elaboraDatiDocumento() [15]
		// OK Marc4cppDocumento::creaTag102_PaeseDiPubblicazione() [53]
		// OK Marc4cppLegami::elabora42x(char*) [56]
		// OK Marc4cppDocumento::creaTag210_PubblicazioneProduzioneDistribuzione(char*, char*) [36]
		// OK Marc4cppLegami::isPartenzaLegame01MW(char*) [61]
		// bool Tb::loadNextRecordDaIndice(const char *key) // entryFile = entryPtrDaIndice; PORCA VACCA !!!! Si copiava tutto il file
		// bool TrAutAut::loadNextRecordDaIndice(const char *key, bool inverted); PORCA VACCA !!!! Si copiava tutto il file
		// OK Tb::loadNextRecordDaIndice(char const*) [58]
		// ok in parte Marc4cppLegami::creaTag410_InCascata_Indice(int, tree<std::string, std::allocator<tree_node_<std::string> > > const&, CKeyValueVector*, CKeyValueVector*) [42]
		// OK Marc4cppDocumento::creaTag010_Isbn(char) [92]
		// OK Marc4cppLegami::creaTag312_NotaLegameVariante(char*, char*, char, CString&, char) [84]
		// OK Marc4cppLegami::getVidFirstAutore(tree<std::string, std::allocator<tree_node_<std::string> > > const&, CString&) [127]
		// OK Marc4cppLegami::creaTag921_Marca() [89]
		// OK Marc4cppLegami::isRiferimentoLegame01MW(char*) [131]
		// OK Marc4cppLegami::creaTag676_ClassificazioneDecimaleDewey() [140]
		// OK Marc4cppLegami::creaTag71x(DataField*, CString*) [74]

		//printf ("\n\nVersione 1.11.13 29/11/2010");	//
			// Fix regression per $9 in 500 per CFI
		//printf ("\n\nVersione 1.11.13a 29/11/2010");	// Fix regression 461 mancanti
			// Fix 210 if (s.StartsWith("n. ") || s.EndsWith(" n.")) // nato
			// Fix int Marc4cppLegami::contaBidBase(char *bid)
			// Fix CMisc::isDate
			// OK Marc4cppDocumento::creaTag011_Issn() [231]
			// OK readConfig(CFile*, cIni*) [230]

		//printf ("\n\nVersione 1.11.14 29/11/2010");
		// Gestione indicatori 4xx
		//			if (notaAlLegame_311)
		//				df->setIndicator2('1');
		//			else
		//				df->setIndicator2('0');
		// Gestione 410 e 225 qundo legame != 01 TOPPA // CFI SBL0116422
		// 	if (!livelloValido)
		// 		return; // Legami non 01
		// 		CString id("IT\\ICCU\\", 8);
		// Fix bool MarcStreamWriter::prepareRecordTowrite(MarcRecord* marcRecord)

		//printf ("\n\nVersione 1.11.15 30/11/2010");	// Fix regression 461 mancanti

		//printf ("\n\nVersione 1.12.1 01/12/2010");
			// Gestione CTokenizer::GetToken(short, char const*, long*) [9]
			// OK Tb::loadNextRecord()
			// OK Tb::loadNextRecord(char const*) [18]
			// OK Tb::loadRecordFromString(char*) [43]
			// OK Marc4cppLegami::elabora44x(char*) [102]
			// OK Tb::loadNextRecordDaIndice(char const*) [170]
			// OK Marc4cppLegami::getBidColl(char*, CString*) [105]
			// TbReticolo::TbReticolo(char *record)
			// OK TbReticolo::assign(char*) [252]
			// OK Marc4cppLegami::isPartenzaLegame01MW(char*) [209]
			// OK Marc4cppLegami::contaBidColl(char*) [189]
			// OK Marc4cppLegami::contaBidBase(char*) [219]
			// OK Marc4cppLegami::elabora42x(char*) [208]
			// OK TrRepMar::loadNextRecord(char const*) [274]
			// OK Marc4cppLegami::isRiferimentoLegame01MW(char*) [271]
			// OK TrAutAut::loadNextRecordDaIndice(char const*, bool) [317]
			// bool TrRepMar::loadNextRecord(const char *key)
			// bool TbcNotaInv::loadNextRecord(const char *key)
			// void Tb950Coll::loadRecord(long offset)
			// bool TsLinkMultim::loadNextRecord(const char *key)
			// void Marc4cppLegami::elabora42x(char *bid) {
			// FIX BUG 960 Esemplari950 *Biblio950::creaEsemplare950(Tb950Ese *tb950Ese, const char *bidDoc, const char *cdDoc, const char *cdBib, const char *idEsemplare)
			//   fottutissimo puntatore a campo di record
			//		const char *cdDocEse = 0;
			//		CString  cdDocEse;

		//printf ("\n\nVersione 1.12.2 02/12/2010");
			// Fix Problema lunghezze aree  ANA0017070 # Crash
		//printf ("\n\nVersione 1.12.3 03/12/2010");
			// fixed test per frequenza a null, DataField * Marc4cppLegami::creaTag461_FaParteDi_NotiziaSuperiore(CString *sequenza)
			// Fix 5xx per CFI. 500, 510, 517, 520, 530, 532


//		printf ("\n\nVersione 1.12.4 05/12/2010");
			// Rinominando metodi e documentando
			// Aggiunto DataField::DataField(char* tag, int len)
			// DataField * Marc4cppDocumento::creaTag120_MaterialiCartografici(), aggiunto
			//	s.AppendString("        ");  // 1 - 8
			//	s.AppendString("  ");
			// DataField * Marc4cppDocumento::creaTag121_MaterialeCartografico_CaratteristicheFisiche(), aggiunti campi filler
			// Fix bool TbcNotaInv::loadNextRecord(const char *key)
			// Tb950Coll Gestione tbFields in loop
			// TrAutAut::loadNextRecordDaIndice Gestione tbFields in loop

		//printf ("\n\nVersione 1.12.5 09/12/2010");
			// Fixed problem ResizeBy = DEFAULT_RESIZE_BY; in CBufferedData
			// Fix 110 pos 1 e 2 (CFI)
//		printf ("\n\nVersione 1.12.6 10/12/2010");
			// In CFile.cpp per AIX #include <iostream>	// Looking for access() and unlink() in here.
			// trying stat()
			// CString OrsLong Size = BufferSize + GrowBy; // 12/12/2010
			// DEFAULT_RESIZE_BY diventa DEFAULT_BUFFERED_DATA_RESIZE_BY
			// Provato a compilare con opzione -D_LARGE_FILES ma fallisce su aix
			// Documentati tag area 3xx

			// Comaptibilit� gcc 4.4.4	DataField.cpp, Marc4CppLegami.cpp, MarcStreamReader.cpp, Tb.cpp, Tb950Coll.cpp, TbLuogo.cpp, CMisc.cpp
			// Fix 950 $d Mantis 4096

//	printf ("\n\nVersione 1.12.7 22/12/2010");
			// Fix 012 per materiale musicale Mantis Indice 4097

//	printf ("\n\nVersione 2.01.1 14/01/2011");
	// Tornati indietro sulla $d dell 950 (22/12/2010 Mantis 4096 (Polo NAP))
//	printf ("\n\nVersione 2.01.2 19/01/2011");
		// Fix bug mantis 4144 per gestione legami N -> S da 463 a 461
// printf ("\n\nVersione 2.01.3 24/01/2011");
		// Fix tag 326 per esportare descrizione e non codice (mail Rossana)

//	 printf ("\n\nVersione 2.01.4 26/01/2011");
		 // creaTag899_Localizzazione sottocampo 'a' codificato solo se scarico OPAC
//	 printf ("\n\nVersione 2.01.5 27/01/2011");
		// fix per gestione codice errato in 326
//	 printf ("\n\nVersione 2.01.6 27/01/2011");
		// fix per gestione 463 per IEI con  IEI0307468

//	 printf ("\n\nVersione 2.01.7 28/01/2011");
		 // 28/01/2011 Mantis 4175 200 $g!
//	 printf ("\n\nVersione 2.01.7a 28/01/2011"); // Passaggio ambiente di sviluppo da Argentosh a ArgeHp

//	 printf ("\n\nVersione 2.02.1 04/02/2011");
	 // OK Mantis 4189 200 $b. Assenza titolo tra parentesi quadre in record figlio. Veramente trattasi di mancanza del contenuto dell'area di designazione del materiale.
	 // OK 04/02/2011 Mail Rossana, 950 $c generata vuota. Ora generata solo se contiene dati.

//	 printf ("\n\nVersione 2.02.2 09/02/2011");
	 // siArg = 0; // 09/02/2011 Mancato reset causava CRASH casuali

//	 printf ("\n\nVersione 2.02.3 14/02/2011");
		 // Mantis 4224 - creaLegamiTitoloSoggetto() e creaLegamiTitoloClassificazione() erano rimasti disattivati dopo ricerca CRASH casuali
		 // Mantis 4219 - Fix per Tag 208 $d

//	 printf ("\n\nVersione 2.03.1 08/03/2011"); // Fix Mantis 4279 per generazione legami 461 e 42x N -> S
//	 printf ("\n\nVersione 2.03.2 09/03/2011"); // Definito nuovo polo. #define POLO_TO3        21 // 09/03/2011

//	 printf ("\n\nVersione 2.03.3 14/03/2011");  // FIX per bug BVE PAL0040043 14/03/2011
//	 printf ("\n\nVersione 2.03.3a 14/03/2011");  // FIX per bug BVE PAL0040043 14/03/2011 Aggiunto controllo in scrittura

//	printf ("\n\nVersione 2.03.4 16/03/2011");	// Fix "." per CFI CFI0617838 in assenza di codice sezione

//	printf ("\n\nVersione 2.03.5 29/03/2011");	// Aggiunta $z in 928 e 929. Segnalazione Roveri

//	printf ("\n\nVersione 2.04.1 01/04/2011");	// Aggiunto ts_ins_indice in tr_tit_bib,	// 01/04/2011 per gestione statistiche

//	printf ("\n\nVersione 2.04.2 06/04/2011");	// Creazione tag 311 per legami 01. tpLegame->isEqual("01") // 06/04/2011 Mantis 4343

//	printf ("\n\nVersione 2.04.3 12/04/2011");	// Mantis 4361 A) Gestione parametrizzata per la creazione del codice ISBN (010) con o senza trattini
												// Per i poli che vogliono la gestione dei trattini aggiungere queste due riche nel file di configurazione (.cfg)
												// Sicuramente il polo IND
												// # Gestione isbn (tag 010) con trattini (default 0) 1=true 0=false
												// isbnConTrattini 1

												// Mantis 4361 B) legami M01S invertiti

												// Mantis 4365 fix per 225 e 410 $i


	//printf ("\n\nVersione 2.04.4 26/04/2011");	 // Mantis 0004381. 26/04/2010.
												// Segnaliamo che abbiamo riscontrato nello scarico Unimarc la presenza di un eccessivo numero di spazi bianchi ogni volta che viene aggiunto il sottocampo che contiene il vid ($3 nei campi 7xx).
	//printf ("\n\nVersione 2.04.4a 26/04/2011"); // 26/04/2011 Segnalazione Renato/Rossana TO00189128

	//printf ("\n\nVersione 2.04.4b 26/04/2011");	// UNIMARC Relator code tipografi/editori Mail Contardi 26 Apr 2011.
												// Forzato relator code 650 per indice se tipo resp. e' a 4 per tag 702 e e 712
	// printf ("\n\nVersione 2.05.1 03/05/2011"); // 03/05/2011 Non generare legame da spogli a periodici per il polo. Problema MO! CFI0214027
//	printf ("\n\nVersione 2.05.2 24/05/2011");	// prepend 0 al cd_inventario nella 956 (Giangregorio)
//	printf ("\n\nVersione 2.05.2b 25/05/2011");	// prepend 0 al cd_inventario nella 956 (Giangregorio) 9 cifre totali non 10
//	printf ("\n\nVersione 2.07.1 05/07/2011");	// $e in 899 per indice 05/07/2011 Mantis SBN2 4550
												// 05/07/2011 Mail Franco Mon, 4 Jul 2011 11:06:21 +0200. Tesi di dottorato digitali in SBNWEB - proposta finale (o quasi)
	//	printf ("\n\nVersione 2.07.2 08/07/2011");	// Fix Mantis indice 0004555
	//	printf ("\n\nVersione 2.07.3 19/07/2011"); // Mantis 0004571 19/07/11
	//	printf ("\n\nVersione 2.08.1 08/08/2011");	//  Mail roveri per tag 105. Fix 08/08/2011 problema generi a null (0 binario)
	//	printf ("\n\nVersione 2.08.2 26/08/2011"); // 25/08/2011 712 $5, Problma segnalato da Rossana/Pavoletti. $5 errata e superflua
	//	printf ("\n\nVersione 2.08.3 31/08/2011"); // 31/08/2011 Mantis 4608 FIX valido solo per polo CFI
	//	printf ("\n\nVersione 2.08.3a 31/08/2011"); // 31/08/2011 Mantis 4608 FIX valido solo per polo CFI. Gestione $2 e fix $v
	//	printf ("\n\nVersione 2.09.1 16/09/2011"); // Mantis 0004627. 16/09/2011 Dopo aver parlato con Roveri e Paolucci si eccezione per la 123 e si tira fuori un record anche senza sottocampi!!!
//		printf ("\n\nVersione 2.10.1 07/10/2011"); // 07/10/2011 Mantis 0004657 Problema 464 assenti e 461 al posto
	//	printf ("\n\nVersione 2.10.2 10/10/2011"); // Rimosso DEBUG per la 461


		//	printf ("\n\nVersione 2.10.3 //2011"); // creaTag71xNew 24/10/11 Mantis 0004696 splittare vid
		//	printf ("\n\nVersione 2.12.1 13/12//2011"); // Mantis 4787 Rimozione $c nella 500
		//	printf ("\n\nVersione 2.12.2 23/12/2011"); // Gestione parentesi quadra ]. Mail Bergamin 18/12/2011
		//	printf ("\n\nVersione 3.01.1 20/01/2012"); // Mantis 4836 20/01/2012



		//	printf ("\n\nVersione 3.02.1 07/02/2012"); 	// 07/02/2012. Aggiungere la collocazione dell'inventario ($z) per gestione linik multimediale (accesso remoto) alla 956
														// Richiesta mail Calogiuri, Giangregorio del 03/02/2012


		//	printf ("\n\nVersione 3.02.2 07/02/2012"); // Mantis 4870 21/02/2010

			//printf ("\n\nVersione 3.5.1 03/05/2012");	// Tag005 gestione timestamp da sistema per authority

			//printf ("\n\nVersione 3.5.2 16/05/2012");	// Mantis 4949. Gestire $v singola in 410 per polo. 16/05/2012
														// Mantis 4996 17/05/2012

			//printf ("\n\nVersione 3.6.1 07/06/2012"); // Aggiunta $9 in tag 606 (Soggetti)
														  // TbDescrittore, aggiunto cd_edizione e cat_termine // 07/06/2012


			//printf ("\n\nVersione 3.7.1 16/07/2012"); // 16/07/2012 Fix per null e stato ordine in tag 961

			//printf ("\n\nVersione 3.7.2 30/07/2012");	// 30/07/2012 Fix per tr_tit_aut.out.srt di indice che sfora i 2.2 GB
														// Portato alcuni buffer da 4096 a 9192 per la ReadLineWithPrefixedMaxSize
			//printf ("\n\nVersione 3.8.1 22/08/2012"); // Fix mantis collaudo 5037 per campo cd_mat_inv
			//printf ("\n\nVersione 3.9.1 27/09/2012"); // elabora46x(),  27/09/2012 Segnalazione Renato per IEI bid RAV0107640

			//printf ("\n\nVersione 3.9.1.a 22/10/2012");	// #define _LARGE_FILES	// 22/10/2012. OK on AIX. Not required on linux. NB: Needs fseeko.
			//printf ("\n\nVersione 3.10.1. 24/10/2012");		// Gestione LITTLE_ENDIAN e MEMCPY_64BIT da define di compilatore (.mk)
			//printf ("\n\nVersione 3.10.1a. 25/10/2012");		// setRecordSeparator(0xC0); // 0xC0 25/10/2012 in tb_titolo e tr_tit_bib



			//	printf ("\n\nVersione 3.11.1 12/11/2012"); // Fixed CMisc::formatDate2 per Indice
			//	printf ("\n\nVersione 3.11.2 16/11/2012");	// Gestione parametrica di tutti i tag
			//	printf ("\n\nVersione 3.11.3 19/11/2012"); // CMisc::gestisciTrattiniNumStandard fix dei trattini. Mantis  4667
			//	printf ("\n\nVersione 3.11.4 21/11/2012"); // 21/11/2012 Fix memory leak, creaTag71xNew
			//	printf ("\n\nVersione 3.11.4a 21/11/2012"); // // 21/11/2012 Memory leak se df non aggiunto in record. elaboraDatiTipoNumeroStandard
			//	printf ("\n\nVersione 3.12.1 04/12/2012"); // 04/12/2012 225 NO per OPAC mail Roveri
			//	printf ("\n\nVersione 4.1.1 22/01/2013"); // Fix per tornare tag 001 in caso di record cancellato
			//	printf ("\n\nVersione 4.1.2 29/01/2013"); // Mantis 5241 29/01/2013 Contardi. 8999 $c,$s,$t
			//	printf ("\n\nVersione 4.1.3 30/01/2013"); // Fix Marc4cppDocumentoAuthority::creaTag210() per $c
			//	printf ("\n\nVersione 4.2.1 25/02/2013");	// Marc4cppLegami::elabora46x marcRecord->addDataField(df); // Mail Roveri  lunedì 18 febbraio 2013 16.55
															// Marc4cppLegami::elabora46x if (IS_TAG_TO_GENERATE(463)) // 19/02/2013 mail Roveri del 18/02/2013 +  segnalazione Rossana


			//	printf ("\n\nVersione 4.2.2 27/02/2013"); // printf("\nCodice unimarc non gestito per '%s'",	legameUnimarc.data()); 27/02/2013 Genera tropoo log!!! Rimosso
														// #define OrsMAX_EXPRESS_LEN		500 portato da 200 a 500 per gestione tagsToExport


			//	printf ("\n\nVersione 4.3.1 11/03/2013"); 	// Authority autore: sprintf (buf, "%s:AUT:%s", curLevel, Token);	// 11/03/2013 prendi il vid invertito
															// 11/03/2013 Rimosso controllo forma accettata
															// fixed test per tag to generate per authority
															// fixed 152 per authority autore
															// fixed 210 authority per $d,$f,$e

				// printf ("\n\nVersione 4.3.2 19/03/2013");	// Inizio gestione VIAF per authority autori
				//	printf ("\n\nVersione 4.3.3 25/03/2013");	// Fix per 421 MO10009897 (segnalazione Renato)
				//	printf ("\n\nVersione 4.4.1 16/04/2013");	// Scrittura file unimarc opzionale (se definito in file di configurazione)
																// Gestione export XML
																// Aggiunto switch -x --xmlMarkFileOut
				//	printf ("\n\nVersione 4.4.2 19/04/2013");	// Aggiunto OrsBool CString::Replace(const char *fromString, const char *toString, int occurance ) // 0 = all
																// Gestione entities per <, > e &
																// Cambiato percorso XSD <collection xmlns="http://www.bncf.firenze.sbn.it/unimarc/slim" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.bncf.firenze.sbn.it/unimarc/slim ./unimarcslim.xsd">
				//	printf ("\n\nVersione 4.4.3 22/04/2013");	// Implementato export soggetti e fix etichetta 931 e 932 con aggiunta di $3 e $4
				//	printf ("\n\nVersione 4.4.4 29/04/2013"); 	// Fix if (trDesDesInv) delete trDesDesInv;
																// Usato tabelleVector per autority soggetto. Per usufruire delle funzionalita' di stop
				//	printf ("\n\nVersione 4.5.1 06/05/2013");	// Fix per gestione $m dinamica in 950





// Aggiunto in  downloadSBW.txt
// # 03/05/2013 bug 5302
// tb_codici_cnot query="select cd_tabella, ds_tabella from tb_codici where tp_tabella = 'CNOT'"

// Aggiunto a makeIndici.sh
//# 22/04/2013
//java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_descrittore.out input/tb_descrittore.out.off
//echo sort input/tb_descrittore.off
//sort -T ./tmp -k 1.1,1.10 -o input/tb_descrittore.out.off.srt input/tb_descrittore.out.off
//
//
//# CID/BID invertiti rispetto a polo
//#echo sort tr_sog_des.out
//#sort -k 1.1,1.10 -o input/tr_sog_des.out.srt ../db_export/tr_sog_des.out
//sort -k 1.14,1.23 -k 1,10 -o input/tr_sog_des.out.srt ../db_export/tr_sog_des.out
//java it.finsiel.offlineExport.CreateOffsetFile input/tr_sog_des.out.srt input/tr_sog_des.out.srt.off db=polo
//
//
//echo sort tr_des_des.out
//sort -k 1.1,1.10 -o input/tr_des_des.out.srt ../db_export/tr_des_des.out
//java it.finsiel.offlineExport.CreateOffsetFile input/tr_des_des.out.srt input/tr_des_des.out.srt.off
//
//echo sort tr_des_des.out invertita
//sort -k 1.14,1.23 -o input/tr_des_des.out.inv.srt ../db_export/tr_des_des.out
//java it.finsiel.offlineExport.CreateOffsetFile input/tr_des_des.out.inv.srt input/tr_des_des.out.inv.srt.off listaInvertita
//
//# end 22/04/2013

// Aggiunto in coda a offlineExportUnimarc.linux.cfg
//#==================================
//# FILE DEI CODICI
//# File per gestione codici dinamici (bug mants polo 5302)
//# NB: Mai caricata in memoria
//#==================================
//#
//codici	tb_codici_cnot		/home/argentino/offlineExport/sbw/db_export/tb_codici_cnot.out	NO
//


				//	printf ("\n\nVersione 4.5.2 15/05/2013"); // df = creaTag011_Issn(); // Codice ISSN fix 15/03/2013
				//	printf ("\n\nVersione 4.5.3 21/05/2013"); // 21/05/2013 per poter generare le 956 indipendentemente dalle 95x		if (IS_TAG_TO_GENERATE(950))
				//	printf ("\n\nVersione 4.6.1 03/06/2013");	// Fixed Mantis/Polo 4979 per tag 312
//					printf ("\n\nVersione 4.6.2 20/06/2013"); // Fixed mantis 0005343 per 926 $c (problema del tokenizer)
//					printf ("\n\nVersione 4.8.1 08/08/2013"); // Fixed VIAF - elaboraDatiViaf
//					printf ("\n\nVersione 4.8.2 09/08/2013"); // Fixed VIAF - problema fine area e nature 'T'
//					printf ("\n\nVersione 4.8.3 23/08/2013"); // tbIncipit->dumpRecord();




					// printf ("\n\nVersione 4.9.1 06/09/2013"); // Printout of switch overrides
				//	printf ("\n\nVersione 4.10.1 01/10/2013"); 	// $899 Rimossa $a e $d per OPAC
																// Fix problema $9 per 606 e correzione puntamento a enum table
																// Fix Mantis Indice 5403
				//	printf ("\n\nVersione 4.10.2 09/10/2013"); // $2 tag 250 authority soggetto per polo
				//	printf ("\n\nVersione 4.12.1 06/12/2013"); // keyPtr->rightPadding(' ', 10); // 06/12/2013 Make searcheable key
																// Che ci sia nel file di configurazione: tr_per_int e tr_per_int_off
				//	printf ("\n\nVersione 5.1.1 20/01/2014");	// 20/01/2014 Fix export NON DEWEY if (*tbClasse->getField(tbClasse->cd_sistema) == 'D' && (secondchar >= '0' && secondchar <= '9') )
				//	printf ("\n\nVersione 5.1.2 27/01/2014"); // 27/01/14 consistenze eliminate e titoli cancellati ma localizzazioni ancora visibili in opac sbn MAIL FRANCO 22/01/2014
																// In 899 non scaricare se SOLO PER GESTIONE per l'Indice
				//	printf ("\n\nVersione 5.2.1 03/02/2014"); // Ripristinato il 03/02/2014 su indicazione di Paolucci/Simonelli (899 P, PG e G)



				//	printf ("\n\nVersione 5.2.2 24/02/2014"); // INDICE  24/02/14. Indice ha codice dewey di un singolo carattere. Mantis 5512
				//	printf ("\n\nVersione 5.2.3 26/02/2014"); // Gestione export viaf per autori che abbiano responsabilita' 1,2 o 3

				//	printf ("\n\nVersione 5.3.1 18/03/2014"); // Sistemato mantis 5530. E rimosso problema di scarico tr_per_int a Firenze
				//	printf ("\n\nVersione 5.3.2 28/03/2014"); // Per VIAF if (cdLivello->isEqual("97") || cdLivello->isEqual("95")) // 28/092014 Mail 27 Mar 2014 Di Franca Papi

				//	printf ("\n\nVersione 5.7.1 21/07/2014");	// Solo per POLO: estrazione record con natura R (Raccolta fattizia) 21/07/2014
																// Estrazione delle 560 per i record che hanno riferimenti a record con natura R (solo per POLO)
																// Ricordarsi di aggiungere la 560 tra i tag da eportare nel file di config (dei POLI)
				// #IFDEF EVOLUTIVA_2014
				//	printf ("\n\nVersione 5.9.1 02/09/2014");	// Nuova gestione numeri standard gestita tramite #IFDEF EVOLUTIVA_2014
																// Aggiunte etichette 016,072,073
				// UNDEF EVOLUTIVA_2014 (solo per polo)
				// printf ("\n\nVersione 5.9.2 12/09/2014");	// Sistemato la 560 per prendere lo localizzazioni

				// #IFDEF EVOLUTIVA_2014
				//	printf ("\n\nVersione 5.9.3 17/09/2014");	// Gestione numero standard tipo 'Z' in $f della 011

				// UNDEF EVOLUTIVA_2014
				//printf ("\n\nVersione 5.11.1 17/11/2014");  // SOLO INDICE: creaTag463_PezzoFisico. Fixed bug 463 (mail roveri 14/11/14)
				//printf ("\n\nVersione 5.11.2 26/11/2014");  // 26/11/14 Mantis 5668: tipoNumeroStd == TP_NUMERO_STANDARD_Z_ISSN_L



				//	printf ("\n\nVersione 5.12.1 15/12/2014");  // Vanno in linea le evolutive indice (#IFDEF EVOLUTIVA_2014)
																// e le evolutive per Polo (#IFDEF EVOLUTIVA_POLO_2014)
																// RIMOSSE le #IFDEF EVOLUTIVA_2014 e #IFDEF EVOLUTIVA_POLO_2014

//					printf ("\n\nVersione 6.1.1 08/01/2015"); 	// gestione 977 per Polo
//					printf ("\n\nVersione 6.1.2 14/01/2015");	// fixed bug introdotto in 010 ($a non contiene il numero standard)
																// Inizializzazione variabili in costruttore Marc4cpp per  Localizzazioni 977 (POLO) 14/01/2015
//					printf ("\n\nVersione 6.1.3 15/01/2015");	//  Tag 016, 071, 072, 073 (mail Roveri)
																// 	indicatore 2 messo a '1'. In quanto si scarica la nota
																// 	gestione $z in caso di "err"
//					printf ("\n\nVersione 6.1.4 16/01/2015");	// Gestione etichetta 967 per polo
//					printf ("\n\nVersione 6.1.5 20/01/2015");	// fix $n in 967
//					printf ("\n\nVersione 6.1.6 21/01/2015");	// rimossa gestione 979 in tag 013

//					printf ("\n\nVersione 6.1.7 29/01/2015");	// Gestione $4key=value in 7xx per strumento musicale tramite tb_codici ORGA
																// 700, 701, 702, 710, 711 e 712
																// Rimossa gestione trattini nella 072 $a
																// Export 7xx $4 (voci/strumenti) tramite #IFDEF VOCE_STRUMENTO (undefined per ora)
//					printf ("\n\nVersione 6.2.1 02/02/2015");	// Fixed bug per nuova gestione classi. Portato chiave a 36 caratteri valido sia per indice che per polo
																// Testato su INDICE
																// Testato su POLO
//					printf ("\n\nVersione 6.2.2 04/02/2015");	// Fix 977 (ricerca chiavi multiple)
																// Gestione offsetType	ASCII/BINARY in file di config (default ASCII if missing)
																// Log contatori per binarySearch (per vedere quante atol/atoll faccio sugli indici)
//					printf ("\n\nVersione 6.2.3 05/02/2015");	// Fix caricamento indici SI/NO (caricava sempre di default se non specificato).
																// Se non specificato indice viene caricato in memoria (pe retrocompatibilita')
																// Caricamento da file per coll_kloc_off950
																// Ristrutturato caricamento indici
																// Completato caricamento indici ASCII/BINARIO
//					printf ("\n\nVersione 6.2.4 16/02/2015");	// Attivato export 7xx $4 VOCE_STRUMENTO (solo per INDICE)
//					printf ("\n\nVersione 6.2.5 17/02/2015");	// Gestione export etichetta 115 (audio video)
//					printf ("\n\nVersione 6.2.6 19/02/2015");	// Gestione export etichetta 126 (disco sonoro)
//					printf ("\n\nVersione 6.2.7 20/02/2015");	// Gestion $z on 013 e rimosso trattini in numero standard 073
//					printf ("\n\nVersione 6.2.8 23/02/2015");	// Gestione caratteri no sort in tag 312 $a
//					printf ("\n\nVersione 6.3.1 06/03/2015");	// Fix 7xx $4 VOVE/INTERPRETE con ','
																// Nuova gestione 950 e 960 (segnalazione La Porta)
//					printf ("\n\nVersione 6.3.2 06/03/2015");	// Attivato export 7xx $4 VOCE_STRUMENTO anche per POLO

//					printf ("\n\nVersione 6.3.3 09/03/2015");	// Gestione tag 181 (area zero) non finita
//					printf ("\n\nVersione 6.3.4 10/03/2015");	// 10/03/2015 950 bid di collocazione diverso da bid di inventario e da bid di esemplare
//					printf ("\n\nVersione 6.3.5 11/03/2015");	// fix 950 (Mail Mery)
//					printf ("\n\nVersione 6.3.6 12/03/2015");	// Finito la 181 e fatto la 182

//					printf ("\n\nVersione 6.3.6b ");	// Fix 960 per CFI

//					printf ("\n\nVersione 6.3.7 23/03/2015");	// Gestione OFFSET_TYPE_BINARY con int al posto del long (a 32 bit (4 byte) su piattaforma Intel)
																// Indici binari ora girano sia su piattaforma a 32 bit che a 64 bit
																// Rivista gestione record/field/subfield troppo grandi e messaggistica
																// long	CFileInMemory::readFixedLenLine(char* string, long N)
																//		Rimosso 	*(string-1) = 0; // EOL 19/03/2015 (per poter concatenare resto di riga)
																//		Rimosso messaggio	SignalAnError(__FILE__, __LINE__, "%s: Buffer size: %d, End of line non raggiunta: %s", Filename, N, dest);
																// TbTitSet1 s125_indicatore_testo, letterario in pos 3 come per polo
//					printf ("\n\nVersione 6.3.8 24/03//2015");	// Rimosso chiamata a memcpy per assegnazione offset binario da indice. Assegnazione diretta
																// iPos =  *((int*)(entryPtr+ key_length)); // 24/03/2015
																// llPos =  *((long long*)(entryPtr+ key_length)); // 24/03/2015
																// appendString o costruttore di stringhe di un singolo carattere non usano + appendstring
																// Sostituito chiamate a memcpy con COPY_FAST( o assegnazione ottimizzata se singolo byte)
																//  per copie di 2,3,4,5,6,7,8,10,12 bytes
																// Per 10.000 record di prova (INDICE) passato da 14.805.538 chiamate a 4.525.702.
//					printf ("\n\nVersione 6.3.9 26/03/2015");	// Riscrittura 	bool Tb::loadNextRecord()
																// 				bool Tb::loadNextRecord(const char *key)
																// Aggiunto bool CString::Split_assign(ATTValVector <CString *> &OutStrings, const char *aStrSep)
																// Adeguamento numero campi in tb_classe (non corrispondevano a DB)
																// Adeguamneto init tbSoggetto per cd_edizione
//					printf ("\n\nVersione 6.3.10 31/03/2015");	// creaTag960_Gestionale aggiunto codice per
																// 	Gestione collocazioni senza inventario 27/03/2015
																//	Gestione collocazioni CON inventario 27/03/2015
																// Fixed CString = operator
																// Fix 181/182 multiple con $6
																// CString::Split_assign rimosso clear per campi non usati
																// Aggiunte MACRO_RESET_NO_RESIZE, MACRO_ASSIGN_CHAR

//					printf ("\n\nVersione 6.4.1 10/04/2015");	// Corretto $f creaTag200_AnticoNew
																// 423 gestito bidlink vuoto. DataField * Marc4cppDocumento::creaTag200_AreaTitoloEResponsabilita(char *areaStartPtr, char *areaEndPtr) {
																// (929 produce $g rispetto alla versione 6.3.6b)
																// 200 char *bid = tbTitolo->getField(tbTitolo->bid); // 10/04/2015
																//     Questo comporta parecchie differenze nella 200 e 423 causa moderno/antico
//					printf ("\n\nVersione 6.4.2 13/04/2015");	// Gestione dinamica path indici binary:
																//	printf ("\nRimappiamo i percorsi per gli indici binari con '/boff'");
																// Aggiunto switch "-z" / "--indiciBinari". Valori possibili ASCII (default) / BINARY
//					printf ("\n\nVersione 6.4.3 14/04/2015");	// Gestione valore di ritorno nullo in loadOffsetFile
//					printf ("\n\nVersione 6.4.4 16/05/2015");	// Fix errata generazione 182 in caso di collane
//					printf ("\n\nVersione 6.4.5 16/04/2015");	// Aggiunto polo PIS 25 in costanti
																// 16/04/2015 mantis 5851 polo PIS


//					printf ("\n\nVersione 6.4.6 17/04/2015");	// CString::CropRightBy if	(n > (UsedBytes-1)) // 17/04/2015
																// Aggiunto if (cdStrumentoMusicale.GetLastChar() == ',')

//					printf ("\n\nVersione 6.4.7 20/04/2015");	// 20/04/2015 mail Roveri problema virgola finale
//					printf ("\n\nVersione 6.4.8 28/04/2015");	// #define POLO_TO0        26 // 27/04/2015
																// long pos = s->First('['); // 28/04/2015
																// long pos = s->First('[');	// 28/04/2015

//					printf ("\n\nVersione 6.5.1 06/05/2015");	// 28/04/2015 Mail Barbieri : per INDICE unimarc 105 $a posizione 11 con valore "i" (moderno)
																// 28/04/2015 Mail Barbieri : unimarc 140 $a posizione 17 - 18 con valore "da" (antico)
																// 04/05/2015 mail MataloniRemove * fron $c $g in 210
																// OrsChar *CString::Strip(OrsChar c); // 04/05/2015
																// Rivisto 105 e 140 per uso tb_titset_1
																// Fix 314 (mail mataloni 05/05/2015)
//					printf ("\n\nVersione 6.5.2 11/05/2015");	// EVOLUTIVA (mail Roveri del 05/05/2015) per gestire errata punteggiatura in catalogazione della 210
					//printf ("\n\nVersione 6.5.3 13/05/2015");	// Gestione 105 per tipo materiale U (musica)
																// INDICE/POLO Nuova gestone 125$b (dati presi dalla tbtitset1)
																// Gestione tramite tbtitset1 per 105/140 pe POLO
																// Nuova gestione isAntico/isModerno (E in quarta posizione ignorata)
																// Rivisitazione criteri per scaricare 105/140
					//printf ("\n\nVersione 6.5.4 19/05/2015");	// 140 (tp_materiale == 'M' || tp_materiale == 'E' || tp_materiale == 'U') // 19/05/2015 si perdeva la E
																// genera 105 solo se genere o tipotesto letterario presente
																// Aggiunto isAnticoE e isModernoE per spacchettamento bid \IT\ICCU\XXX\1234567 \IT\ICCU\XXXE\123456
					//printf ("\n\nVersione 6.6.1 04/06/2015");	// Fix 200 $f authority autore
					//printf ("\n\nVersione 6.6.2 11/06/2015");	// Evolutiva del 27/05/2015 Monografie Livello inferiore (W) con 200 che prende dati dalle 461 e 462 solo INDICE
																// Evolutiva 926 (MGT) per punteggiatura (?,&,^ e "~?")
					//printf ("\n\nVersione 6.6.3 12/06/2015");	// Rimosso bool Marc4cppDocumento::isModerno() e bool Marc4cppDocumento::isAntico()
																// Aggiunto bool Marc4cppDocumento::isModerno105()
																//			int Marc4cppDocumento::isAnticoModernoUndefined()
					//printf ("\n\nVersione 6.6.4 16/06/2015");	// Rimesso a posto 200$a con nuova punteggiatura (mail Roveri/Barbieri/Mataloni 5-6/2015)
																// Rimosso sostituzione caratteri per incipit dopo bonifica base dati
					//printf ("\n\nVersione 6.6.5 16/06/2015");	// Fixed 200 per problemi dui mancata $a (errore di catalogazione)
					//printf ("\n\nVersione 6.6.6 17/06/2015");	// Rimosso doppio NSB e NSE in 200
																// 200 esporta solo per INDICE e se ind1 = 0
																// 200 aggiunte  [...]
					//printf ("\n\nVersione 6.6.7 18/06/2015");	// 18/06/15 Fix per 950 in MarcStreamWriter IEI ART0001916
					//printf ("\n\nVersione 6.6.8 19/06/2015");	// Evolutiva mail Barbieri 17/06/15 troncamento dop 150 caratteri in 200 antico (per le W). Solo INDICE

					//printf ("\n\nVersione 6.6.9 30/06/2015");	// 30/06/2015 ricostruisci200perM20 solo quando 200 presente (manca per scarico ridotto)
//					printf ("\n\nVersione 6.7.1 01/07/2015"); // Mail Mery 950, Segnalazione di MO1: il bid TSA0976170 ha due inventari ma le informazioni sono completamente duplicate nella 950.
//					printf ("\n\nVersione 6.7.2 07/07/2015"); // 07/07/2015 Richiesta verbale Roveri (non troncare sulla parola) nuovaA200.CropRightAfterChar(150, " ,.?!;:");
//					printf ("\n\nVersione 6.7.3 07/07/2015"); // 07/07/2015 Rossana propone problema di quando non esiste il tipo tetso letterario. Vedi NAP0183559
																// modificata la isModerno105() che in assenza di tipo testo letterario chiama la isAnticoModernoUndefined();
//					printf ("\n\nVersione 6.7.4 22/07/2015");	// Fix per data 210 (Mail Barbieri del 21/07/2015)
//					printf ("\n\nVersione 6.7.5 26/07/2015");	// Fix SIGSEV per data 210
//					printf ("\n\nVersione 6.8.1 13/08/2015");	// Fix OrsChar *CString::Strip(stripType st, OrsChar *s)
																// Fix long CString::IndexCharFrom(char aChar, int from, int direction)
																// Fix DataField * Marc4cppDocumento::creaTag210_AreaPubblicazioneProduzioneDistribuzione
//					printf ("\n\nVersione 6.8.2 31/08/2015");	// Mantis 5964. Problema lettura elenco biblioteche da escludere
//					printf ("\n\nVersione 6.9.1 31/08/2015");	// Fix 210 per areee di pubblicazione con sola parentesi ')'. Tag veniva omesso.
																// Ora lo produciamo anche se un po' sporco.
//					printf ("\n\nVersione 6.9.2 09/09/2015");	// Mantis 5975. Descrizione titolo uniforme in 500
//					printf ("\n\nVersione 6.9.3 21/09/2015");  // Fix 210 (mail Mataloni)

//					printf ("\n\nVersione 6.9.4 25/09/2015"); // Fix 210 ($d mail Carla/Roveri) CString::IndexLastSubString(const char *aSubString)
//					printf ("\n\nVersione 6.10.1 12/10/2015");  // 12/10e/2015 gestione 183 in tb_titset_1 (2 campi nuovi)
																// tb_luogo aggiunta nota_catalogatore
//					printf ("\n\nVersione 6.11.1 02/11/2015");  // Fix 210 $d
//					printf ("\n\nVersione 6.11.2 03/11/2015");  // Fix 210 $d BVE0476251 " ; Bozen : Folio, 2008"
//					printf ("\n\nVersione 6.11.3 20/11/2015");  // Authority autore. Aggiunto ISNI in tag 010 (VIAF)
																// RIFATTO la 210 once and for all
																// Implementato export 183
//					printf ("\n\nVersione 6.11.4 26/11/2015");	// Gestione tag 321 solo per INDICE. Riferimenti a siti web esterni all'Opac

//					printf ("\n\nVersione 6.11.5 30/11/2015");  // creaTag005 data presa da ts_var e non ts_ins
//					printf ("\n\nVersione 6.12.1 10/12/2015");	// NUOVA etichetta per polo 696 TESAURO
//					printf ("\n\nVersione 6.12.2 21/12/2015");	// fix 183 secondo supporto
//					printf ("\n\nVersione 6.12.3 22/12/2015");	// fix 18e3 $a -> $c + $2
//					printf ("\n\nVersione 6.12.4 22/12/2015");	// fix 183 $a -> $c + $2 +$6
//					printf ("\n\nVersione 6.12.4 23/12/2015");  // Fix 23/12/2015 Gestione .. -> ...


//					printf ("\n\nVersione 7.1.1 05/01/2016");	// Rivisitazione 181/182/183
//					printf ("\n\nVersione 7.1.2 12/01/2016");	// Aggiunto polo DDS
//					printf ("\n\nVersione 7.1.3 14/01/2016");	// Sostituita 696 CON 689 con gestione TDR
																// Fixed memory leak in biblio950.cpp
//					printf ("\n\nVersione 7.1.4 21/01/2016");	// Added tag 003 permanent link
//					printf ("\n\nVersione 7.1.5 26/01/2016");	// Permalink tramite COOL URI id.sbn.it/permalink=..........
//					printf ("\n\nVersione 7.1.6 27/01/2016");	// Rimossa ricostruisci200perM20 su richiesta di Maria Pia Barbieri il 27/01/2016 per segnalazioni interne!!!
																// Modifica cool URI permalink (003)
//					printf ("\n\nVersione 7.1.7 28/01/2016");	// Implementata etichetta 127 durata disco sonoro
//					printf ("\n\nVersione 7.1.8.29/01/2016");	// Gestione materiale H per tag 922 e 927
//					printf ("\n\nVersione 7.2.1 11/02/2016");	// 11/02/2016 Elena Ravelli RMLE030371 le parentesi che contengono il punto escalamativo dovrebbero essere quadre e non tonde.
//					printf ("\n\nVersione 7.2.2 22/02/2016");	// elaboraNote321 fixed memory leak
//					printf ("\n\nVersione 7.2.3 25/02/2016");	// Gestione $v sequenza in creaTag464_AnaliticaSpoglio per INDICE e POLO

//					printf ("\n\nVersione 7.3.1 02/03/2016");	// Modificato costruttore CSTRING SkipWhiteSpacesFlag = OrsFALSE;	// 01/03/2016 Bug IEI RT1V020699

//					printf ("\n\nVersione 7.3.2 10/03/2016"); 	// fix 462 che erano 461 (PARE021885) isRiferimentoLegame01MW_2(bidReticoloPtr->data(), bid)
																// Le 46x andrebbero rifatte!! sono un papocchio

//					printf ("\n\nVersione 7.3.3 30/03/2016");	// Gestione 922 e 927 anche per tipo materiale M(odenro) ed E(antico)
//					printf ("\n\nVersione 7.3.4 31/03/2016");	// Gestione TbTitset2.h e TbTitset2.cpp per la 231 (titolo dell'opera)
//					printf ("\n\nVersione 7.4.1.20/04/2016");	// Export 321 da tb_nota invece che da ts_link_web
																// Export Authority Titolo Uniforme (incompleto)
//					printf ("\n\nVersione 7.4.2 27/04/2016");	// Fix 321
//					printf ("\n\nVersione 7.5.1 03/05/2016");	// Fix 321 per gestione struttura errata (i/e errato buner odi campi)
//					printf ("\n\nVersione 7.5.2 05/05/2016");	// Pulizia della 300 "Riferimenti:" in base alle 321

//					printf ("\n\nVersione 7.5.3 30/05/2016");	// Rivisitazione creaLegameTitoloAutore
																// authority titolo uniforme per 500,501,510,511
																// $b e $c in 101 (da 454 e 500 $xyz (poi rimosse))
//					printf ("\n\nVersione 7.6.1 06/06/2016");	// 101 $b e $c minuscole
//					printf ("\n\nVersione 7.6.2 06/06/2016");	// Gestione 101 del polo uguale a quello di indice
//					printf ("\n\nVersione 7.6.3 07/06/2016");	// rimossa lingua da 231
//					printf ("\n\nVersione 7.6.4 13/06/2016");	// fix bug 321 $c
//					printf ("\n\nVersione 7.6.5 16/06/2016");	// Gestione tag 128 in audiovisivi, Esecuzioni Musicali E Partiture
//					printf ("\n\nVersione 7.6.6 30/06/2016");	// Gestione tag 696 (legami descrittore/descrittore) solo per POLO
//					printf ("\n\nVersione 7.7.1 01/07/2016");	// Tag 105 e 140 sempre quando info presente in tb_titset_1 (mail Mataloni 30/06/2016)
//					printf ("\n\nVersione 7.7.2 01/07/2016");	// 210 Gestione data nel mezzo del testo e non in coda (RMSE097965 (2 virgole e data dopo la prima))
//					printf ("\n\nVersione 7.7.3 06/07/2016");	// Genero 105/140 solo per nature MWSN
//					printf ("\n\nVersione 7.9.1 16/09/2016");	// Gestione link DB e REP offline. Solo per INDICE

						// - Creato due nuove classi: tb_nota300 e tb_nota321
						// - Procedure per la creazione di tb_nota_300.srt.out e tb_nota_321.srt.out (in linkDb321)
						// - Modificato makeIndici.restOfTables.sh
						//	java -classpath $BIN_DIR it.finsiel.offlineExport.CreateOffsetFile $INPUT_DIR/tb_nota_300.srt.out $INPUT_DIR/tb_nota_300.srt.out.off
						//	java -classpath $BIN_DIR it.finsiel.offlineExport.CreateOffsetFile $INPUT_DIR/tb_nota_321.srt.out $INPUT_DIR/tb_nota_321.srt.out.off
						// - offlineExportUnimarc64.linux.cfg						// -
						//		entita  tb_nota_300      /media/export54/indice/dp/input/tb_nota_300.srt.out             NO
						//		entita  tb_nota_321      /media/export54/indice/dp/input/tb_nota_321.srt.out             NO
						//		indice  tb_nota_300_off         /media/export54/indice/dp/input/tb_nota_300.srt.out.off          NO
						//		indice  tb_nota_321_off         /media/export54/indice/dp/input/tb_nota_321.srt.out.off          NO


//					printf ("\n\nVersione 7.10.1 17/10/2016");	// Export tag 035 (altri numeri, OCLC) solo per POLI
//					printf ("\n\nVersione 7.11.1 14/11/2016");	// Aggiunto polo UPG
//					printf ("\n\nVersione 7.12.1 05/12/2016");	// Barbieri mail 05/12/2016 - 500 $e va in 500 $a
//					printf ("\n\nVersione 7.12.2 15/12/2016");  // Gestione norme catalografiche REICAT in authority Documento, Autore



// Uscire con INTEGRALE del 06/06/2017!!!
// 					printf ("\n\nVersione 8.3.1 02/03/2017");	// Gestione authority Titoli Uniformi 231, 241, 431, 441, 530, 531, 541
																// Gestione documento 506 (NMB: da aggiungere in tagsToExport.txt)
																// 10/03/2017 - Export 321 anche se non presenti riferimenti in 300 (presi dalla tb_nota)
																// 16/03/2017 - Export 440 come riferimenti inversi per legami 04 (anche per polo)
																// 17/03/2017 - Export 899 senza localizzazioni per sola gestione in presenza di una 430 od assenza di una 440
																// 03/05/2017 - 200 authority autore $c e $f in specificazione (prima solo $f erroneamente)
																// 11/05/2017 - 015 authority dismessa

//					printf ("\n\nVersione 8.3.1a 02/03/2017");	// Gestione authority Titoli Uniformi 231, 241, 431, 441, 530, 531, 541
																// 22/05/2017 creaTag128_EsecuzioniMusicaliEPartiture delete df (memory leak)
//					printf ("\n\nVersione 8.5.1 24/05/2017");  // Ripristinato scarico 899 G senza condizione 430 e 440
																// 22/05/2017 #ifdef DEPLOY elaboraNote321(bid); Togliere #ifdef DEPLOY per deploy
																// 22/05/2017 #ifdef DEPLOY 506
																// il tag 152 $a (regole di catalogazione) dovrebbe mantenere il valore che si trova in Indice
																// 24/05/2017 fix memory leak. Delete cf
//					printf ("\n\nVersione 8.6.1 27/06/2017");	// Gestione 576 per legami Titolo/Opera
//					printf ("\n\nVersione 8.7.1 09/07/2017");	// Trim delle 950 per problema troncamento campi
																// Rivisitazione 231. Nuova tb_titset_2
//					printf ("\n\nVersione 8.8.1 07/08/2017");	// Gestione logNaturaErrata parametrizzata
																// Fix strip per precis_inv
//					printf ("\n\nVersione 8.9.1 08/09/2017");	// Fix 210: // 08/09/17 AGR0000744	prova con 210 "[S.l.] : Mariotti 1820, 2017"
//					printf ("\n\nVersione 8.9.2 12/09/2017");	// Fix 210: Rimasta stringa di debug in SW rilasciato  "[S.l.] : Mariotti 1820, 2017"
//					printf ("\n\nVersione 8.9.3 20/09/2017");	// Modifica tag 183 $a al posto di $c e $2
//					printf ("\n\nVersione 8.9.4 20/09/2017");	// Gestione sistema numerico decimale ed esadecimale. Aggiunto nel cfg 'sistema_numerico_unimarc esadecimale oppure decimale'
																// Se assente default e' decimale
//					printf ("\n\nVersione 8.10.1 02/10/2017");	// 183 spostamento $6 prima della $a
//					printf ("\n\nVersione 8.10.2 27/10/2017");	// Gestione nuovo tag 441 creaTag441_HaPerContinuazioneParziale()
//					printf ("\n\nVersione 9.1.1 23/01/2018");	// Gestione etichetta 699 (equvalente della 696) per le variante per varianti storiche e di sinonimia
																// Nuova keyword in ini "dizionario"
//					printf ("\n\nVersione 9.1.2 25/01/2018");	// Gestione composto non preferito (scomposizione) nella 699
//					printf ("\n\nVersione 9.1.3 30/01/2018");	// Gestione da ini file delle nature da esportare: estrai_nature. Se parametro non definito si mette il default per indice o polo
																// Gestione biblioteche da esportare in 899 tramite parametro 'bibliotecheDaMostrareIn899 filename' in .cfg
																// Gestione scarico z899 (nuova keyword in ini)
//					printf ("\n\nVersione 9.2.1 02/02/2018");	// Aggiunto Swicth 'stampaRecordCancellato'(nuova keyword in ini). Buono per incrementale

//					printf ("\n\nVersione 9.2.2 07/02/2018");	// Fix 464 $4590$4vl=violino
//					printf ("\n\nVersione 9.2.3 15/02/2018"); // Aggiunto controllo per export 899 obbligatoria per PBE (check899)
																// Aggiunta opzione
																//	- da .cfg di "incrementale_dal AAAAMMGG" per PBE
																//  - da riga comando: -c AAAAMMGG o --incrementale_dal AAAAMMGG
																// Rimosso da cfg xy899
//					printf ("\n\nVersione 9.2.4 20/02/2018");	// 899 $xm al posto di $xv (per localizzazione modificata)

//					printf ("\n\nVersione 9.2.5 21/02/2018");	// toggle _699_sintetica / estesa
//					printf ("\n\nVersione 9.2.6 28/02/2018");	// 699 gestione ricerca nella lista delle varianti/composti np. oltre che dei termini
//					printf ("\n\nVersione 9.4.1 11/04/2018");	// 699 gestione varianti di sinonimia, storiche e comproto non preferito distinte ($y,$z,$x+)
//					printf ("\n\nVersione 9.4.2 18/04/2018");	// 699 fix per composti non preferiti
//					printf ("\n\nVersione 9.4.3 19/04/2018");	// 699 fix termine preferito con uncinate (CFI0734055) $aPittura di paesaggio$yPaesaggio <Generi artistici>
//					printf ("\n\nVersione 9.5.1 02/05/2018");	// fix 899 $y
//					printf ("\n\nVersione 9.6.1 01/06/2018");	// Gestione 950 -q oppure --esportaSoloInventariCollocati
//					printf ("\n\nVersione 9.7.1 05/07/2018");	// fix void addBibliotecheInKVV(CFile *cFileIn, CKeyValueVector *kvv) per biblioteche separate da virgola
//					printf ("\n\nVersione 9.9.1 17/09/2018");	// 10/09/2018 JIRA 17/43 gestione tag 120 $a pos 7 e 8. Tp_proiezione
//																// 17/09/2018 Fix 951 cod_ord. Mantis 6680
//					printf ("\n\nVersione 9.9.2 25/09/2018");	// Fix pos 7-8 della 120
//					printf ("\n\nVersione 9.9.3 27/09/2018");	// Fix 225/410 per Polo ed Indice in base a mail di Giangregorio del 24/08/2018
//					printf ("\n\nVersione 9.11.1 05/11/2018");	// Fix check899 per pbe
//					printf ("\n\nVersione 9.11.2 22/11/2018");	// Fix null pointer 410 // 12/11/2018
//					printf ("\n\nVersione 10.3.1 12/03/2019");	// Bug mantis 0006904 problema 462. Fatta batteria di test completa!! Approvato da Rossana e Scognamilio
																// fix elabora46y // 12/03/2019 messa parentesi (bug insidioso individuato durante preparazione OS per git)
//					printf ("\n\nVersione 10.3.2 14/03/2019");	// Gestione 462 discendente (SET -> SUBSET) al posto della 463
					printf ("\n\nVersione 10.3.3 25/03/2019");	// BUG fix if (!entryReticolo || !livelloValido)	// 25/03/2019 BUG RAV0078503 (ambiene di new_sbw)	SEG FAULT
																// creaTag410_InCascata_Polo e creaTag225_AreaCollezione
//					printf ("\n\nVersione 10.. //2019");


//	mail Patrizia. Per quando aggiorniamo esercizio
//					DB
//					tb_cartografia
//					inserito campo TP_PROIEZIONE
//					--alter table tb_cartografia per aggiungere tp_proiezione
//					ALTER TABLE MULTIMATERIALE.TB_CARTOGRAFIA ADD TP_PROIEZIONE  varchar2(2);



			printf ("\n%s", switchOverrides);
			printf ("\n\nExport per viaf = %s", EXPORT_VIAF ? "true" : "false");


			printf ("\n");
} // End printHeader();



int main(int argc, const char* argv[]) {

	setvbuf(stdout, NULL, _IONBF, 0); // To flush output.

	//testTokenizer();
	//testCFile();
	//testMarcRead();

	entitaVector = new ATTValVector<CString *>();
	relazioniVector = new ATTValVector<CString *>();
	offsetVector = new ATTValVector<CString *>();
	dictionaryVector = new ATTValVector<CString *>();
	sezioniDiCollocazioneDaNonMostrareIn960KV = new CKeyValueVector(tKVSTRING, tKVSTRING);
	bibliotecheDaNonMostrareIn950KV = new CKeyValueVector(tKVSTRING, tKVSTRING);
	offlineExport(argc, argv);
	deleteVectors();
	if (incrementale_dal)
		delete incrementale_dal;

//#ifdef DEBUG_ARGE
//	printf ("\nLog statistiche delle eccezioni per tag 210");
//	printf ("\n-------------------------------------------");
//	printf ("\n210 Tonda aperta ma non chiusa = %d", eccSoloTondaApertaCtr);
//	printf ("\n210 Tonda chiusa ma non aperta = %d", eccSoloTondaChiusaCtr);
//
//	printf ("\n210 Date non precedute da virgola spazio = %d", eccDataNonPrecedutaDaVirgolaCtr);
//	printf ("\n210 Testo e non data dopo la virgola spazio=%d", eccTestoDopoVirgolaSpazioCtr);
//	printf ("\n210 Parentesi tonde NON sottoarea ..(..).. = %d", eccParentesiTondeNonSottoarea);
//#endif

}

