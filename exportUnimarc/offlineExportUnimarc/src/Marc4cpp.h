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
 * Marc4cpp.h
 *
 *  Created on: 1-dic-2008
 *      Author: Arge
 */


#ifndef MARC4CPP_H_
#define MARC4CPP_H_

#include "library/CString.h"
#include "library/CFile.h"
#include "library/tree.hh"
#include "TbAutore.h"
#include "TbTitolo.h"
#include "TbNumeroStd.h"
#include "MarcRecord.h"
#include "Leader.h"
//#include "MarcStreamWriter.h"
#include "BufferedMarcStreamWriter.h"

#include "Marc4cppDocumento.h"
#include "Marc4cppLegami.h"
#include "Marc4cppLegamiAuthority.h"
#include "Marc4cppCollocazione.h"
#include "TbSoggetto.h"
#include "TbClasse.h"
#include "TbMarca.h"
#include "TrTitTit.h"
#include "Marc4cppAltriDoc.h"
#include "TbImpronta.h"
#include "TbGrafica.h"
#include "TbCartografia.h"
#include "TbMusica.h"
#include "TbIncipit.h"
#include "TbPersonaggio.h"
#include "TbaOrdini.h"
#include "TbRepertorio.h"
#include "TrRepAut.h"
#include "TrRepMar.h"

#include "Marc4cppDocumentoAuthority.h"
#include "TbDescrittore.h"
#include "TrSogDes.h"
#include "TrTitMar.h"
#include "TrAutAut.h"
#include "TbfBiblioteca.h"
#include "TrTitBib.h"
#include "TrTitAut.h"
#include "TrTitCla.h"
#include "TrTitLuo.h"
#include "TrBidAltroid.h"
#include "TrTitAutRel.h"
#include "TbcNotaInv.h"
#include "TbcPossessoreProvenienza.h"
#include "TrcPossProvInventari.h"
#include "TbcSezioneCollocazione.h"
#include "TsLinkMultim.h"
#include "TbParola.h"
#include "TbAudiovideo.h"
#include "TbDiscoSonoro.h"

#include "TbTermineTesauro.h"
#include "TrsTerminiTitoliBiblioteche.h"


using namespace std;

//#define READ_BLOCK_SIZE	4096
//#define KEY_LENGTH	10
//#define BID_KEY_LENGTH	10
//#define VID_KEY_LENGTH	10
//#define PID_KEY_LENGTH	10
//#define MID_KEY_LENGTH	10
//#define CLASSE_KEY_LENGTH	34
#define CLASSE_KEY_LENGTH	36 // 02/02/2015
#define KEYLOC_KEY_LENGTH	9
#define TR_KEY_LENGTH	20
#define BIBLIOTECA_KEY_LENGTH 6
#define OFFSET_LENGTH 11
#define INVENTARIO_KEY_LENGTH	16
#define TBC_SEZIONE_COLLOCAZIONE_KEY_LENGTH 16
#define LF_LENGTH 1
#define POLO_KEY_LENGTH	3

#define BID_ALTRO_ID_KEY_LENGTH 10

class Marc4cpp {

private:
	//ATTValVector<CFile *> fileInMemVector;
	ATTValVector<CFile *> fileEntitaInMemVector;
	//long fileEntitaInMemSize;
	long long fileEntitaInMemSize; // 30/07/2012

	ATTValVector<CFile *> fileRelazioniInMemVector;
	long fileRelazioniInMemSize;

	ATTValVector<CFile *> fileOnDiscVector;

	ATTValVector<CFile *> offsetFileVectorInMem;
	long long fileOffsetInMemSize;

	ATTValVector<CFile *> offsetFileVectorOnDisc;

	ATTValVector<Tb *> tabelleVector;

	ATTValVector<CString *> *dictionaryVector;

	int authority;
	bool exportViaf;
	int exportMaxViafs;
	int recordUnimarcSuSingolaRiga;

	char *tagsToGenerateBufferPtr;
	int tagsToGenerate;

	bool apriFileEntita(ATTValVector<CString *> *entitaVector);
	bool apriFileRelazioni(ATTValVector<CString *> *relazioniVector);
	bool apriFileOffset(ATTValVector<CString *> *offsetVector);

	Marc4cppDocumentoAuthority *marc4cppDocumentoAuthority ;

	Marc4cppDocumento *marc4cppDocumento;
	Marc4cppLegami *marc4cppLegami;
	Marc4cppLegamiAuthority *marc4cppLegamiAuthority;

	Marc4cppCollocazione *marc4cppCollocazione;
	Marc4cppAltriDoc *marc4cppAltriDoc;




	MarcRecord *marcRecord;
	CFile* marcOut;
	CFile* marcOutTxt;
	CFile* marcOutXml;
	CFile* bidErratiOut;
//	MarcStreamWriter *marcStreamWriter;
	BufferedMarcStreamWriter *bufferedMarcStreamWriter;


	// Elenco delle biblioteche di polo cd_bib/record
//	CKeyValueVector *descBibKV;
//	CKeyValueVector *anagrafeBibKV;

	CKeyValueVector *tbfBibliotecaInPoloKV;


	CString tbfBibliotecaInPoloFilename;

	CKeyValueVector *tbcSezioneCollocazioneQuadreKV; // []
//	CString tbcSezioneCollocazioneFilename;


	// Gestione tesauro entita' 04/12/2015
	// ------------------------------------------
	CFile* tbTermineTesauroIn;
	CFile* tbTermineTesauroOffsetIn;
	long elementsTbTermineTesauro;
	char *offsetBufferTbTermineTesauroPtr;
	TbTermineTesauro *tbTermineTesauro;


	// Gestione tesauro relazioni 04/12/2015
	// --------------------------
	CFile* trsTerminiTitoliBibliotecheRelIn;
	CFile* trsTerminiTitoliBibliotecheOffsetRelIn;
	long elementstsTrsTerminiTitoliBibliotecheRel;
	char *offsetBuffertsTrsTerminiTitoliBibliotecheRelPtr;
	TrsTerminiTitoliBiblioteche *trsTerminiTitoliBiblioteche;




	// Tabella dei dati comuni (area 0)
	// ------------------------
	CFile* tbTitSet1In;
	CFile* tbTitSet1OffsetIn;
	long elementsTbTitSet1;
	char *offsetBufferTbTitSet1Ptr;
	TbTitSet1 *tbTitSet1;

	// Tabella dei dati del titolo dell'opera 31/03/2016
	// -------------------------------------------------
	CFile* tbTitSet2In;
	CFile* tbTitSet2OffsetIn;
	long elementsTbTitSet2;
	char *offsetBufferTbTitSet2Ptr;
	TbTitSet2 *tbTitSet2;



	// Tabella dell'audiovisivo
	// ------------------------
	CFile* tbAudiovideoIn;
	CFile* tbAudiovideoOffsetIn;
	long elementsTbAudiovideo;
	char *offsetBufferTbAudiovideoPtr;
	TbAudiovideo *tbAudiovideo;

	// Tabella del disco sonoro (registrazioni sonore)
	// ------------------------
	CFile* tbDiscoSonoroIn;
	CFile* tbDiscoSonoroOffsetIn;
	long elementsTbDiscoSonoro;
	char *offsetBufferTbDiscoSonoroPtr;
	TbDiscoSonoro *tbDiscoSonoro;


	// Tabella delle sezioni di collocazione
	// ------------------------
	CFile* tbcSezioneCollocazioneIn;
	CFile* tbcSezioneCollocazioneOffsetIn;	// File degli offse ordinato
	long elementsTbcSezioneCollocazione;	// Numero di autori
	char *offsetBufferTbcSezioneCollocazionePtr;
	TbcSezioneCollocazione *tbcSezioneCollocazione;


	// Tabella delle parole (per marche)
	// ------------------------
	CFile* tbParolaIn;
	CFile* tbParolaOffsetIn;	// File degli offse ordinato
	long elementsTbParola;
	char *offsetBufferTbParolaPtr;
	TbParola *tbParola;


	//	Tabella dei link multimediali al digitale
	// ------------------------------------------
	CFile* tsLinkMultimIn;
	CFile* tsLinkMultimOffsetIn;	// File degli offse ordinato
	long elementsTsLinkMultim;
	char *offsetBufferTsLinkMultimPtr;
	TsLinkMultim *tsLinkMultim;


	//	Tabella dei link verso altri siti Tag 321
	// Tabella temporanea. In futuro sara' gestito dalla tb_nota
	// ------------------------------------------
	CFile* tsLinkWebIn;
	CFile* tsLinkWebOffsetIn;
	long elementsTsLinkWeb;
	char *offsetBufferTsLinkWebPtr;
	TsLinkWeb *tsLinkWeb;


	// Tabella delle note
	// ------------------------
	CFile* tbNotaIn;
	CFile* tbNotaOffsetIn;	// File degli offse ordinato
	long elementsTbNota;	// Numero di note
	char *offsetBufferTbNotaPtr;
	TbNota *tbNota;

	// Tabella delle note 300 (prodotta e non scaricata)
	// ------------------------
	CFile* tbNota300In;
	CFile* tbNota300OffsetIn;
	long elementsTbNota300;
	char *offsetBufferTbNota300Ptr;
	TbNota300 *tbNota300;

	// Tabella delle note 321 (prodotta e non scaricata)
	// ------------------------
	CFile* tbNota321In;
	CFile* tbNota321OffsetIn;
	long elementsTbNota321;
	char *offsetBufferTbNota321Ptr;
	TbNota321 *tbNota321;



	// Authority Titolo
	// ------------------------
	CFile* tbTitoloIn;
	CString tbTitoloOffsetFilename;
	CFile* tbTitoloOffsetIn; // File degli offse ordinato
	long elementsTbTitolo;	// Numero di titoli
	char *offsetBufferTbTitoloPtr;
	TbTitolo *tbTitolo;


	// Authority Autore
	// ------------------------
	//CString tbAutoreFilename;
	CFile* tbAutoreIn;
	CFile* tbAutoreOffsetIn;	// File degli offse ordinato
	long elementsTbAutore;	// Numero di autori
	char *offsetBufferTbAutorePtr;
	TbAutore *tbAutore;



	// Entity Biblioteca
	// ------------------------
	CFile* tbfBibliotecaIn;
	CFile* tbfBibliotecaOffsetIn;
	long elementsTbfBiblioteca;	// Numero di soggetti
	char *offsetBufferTbfBibliotecaPtr;
	TbfBiblioteca *tbfBiblioteca;



	// Authority Soggetto
	// ------------------------
	CFile* tbSoggettoIn;
	CFile* tbSoggettoOffsetIn;
	long elementsTbSoggetto;	// Numero di soggetti
	char *offsetBufferTbSoggettoPtr;
	TbSoggetto *tbSoggetto;

	// Entity Descrittore
	// ------------------
	CFile* tbDescrittoreIn;
	CFile* tbDescrittoreOffsetIn;
	long elementsTbDescrittore;	// Numero di soggetti
	char *offsetBufferTbDescrittorePtr;
	TbDescrittore *tbDescrittore;

	// Entity Relazione soggetto descrittore
	// ------------------
	TrSogDes *trSogDes;
	CFile* trSogDesIn;
	CFile* trSogDesOffsetIn;
	long elementsTrSogDes;
	char *offsetBufferTrSogDesPtr;

	// Entity Relazione descrittore/descrittore
	// ------------------
	TrDesDes *trDesDes;
	CFile* trDesDesIn;
	CFile* trDesDesOffsetIn;
	long elementsTrDesDes;
	char *offsetBufferTrDesDesPtr;

	// Entity Relazione descrittore/descrittore invertita 03/04/2011
	// ------------------------
	TrDesDes *trDesDesInv;
	CFile* trDesDesInvIn;
	CFile* trDesDesInvOffsetIn;
	long elementsTrDesDesInv;	// Numero di relazioni
	char *offsetBufferTrDesDesInvPtr;






	// Authority Classe
	// ------------------------
	//CString tbClasseFilename;
	CFile* tbClasseIn;
	//CString tbClasseOffsetFilename;
	CFile* tbClasseOffsetIn;
	long elementsTbClasse;	// Numero di classi
	char *offsetBufferTbClassePtr;
	TbClasse *tbClasse;


	// Tabella dei numeri standard
	// ------------------------
	//CString tbNumeroStandardFilename;
	CFile* tbNumeroStandardIn;

	//CString tbNumeroStandardOffsetFilename;
	CFile* tbNumeroStandardOffsetIn;	// File degli offse ordinato

	long elementsTbNumeroStandard;	// Numero di voci
	char *offsetBufferTbNumeroStandardPtr;
	TbNumeroStd *tbNumeroStandard;


	// Authority Marca
	// ---------------a---------
	CFile* tbMarcaIn;
	CFile* tbMarcaOffsetIn;
	long elementsTbMarca;	// Numero di classi
	char *offsetBufferTbMarcaPtr;
	TbMarca *tbMarca;


	// Note all'inventario
	// ------------------------
	CFile* tbcNotaInvIn;
	CFile* tbcNotaInvOffsetIn;
	long elementsTbcNotaInv;
	char *offsetBufferTbcNotaInvPtr;
	TbcNotaInv *tbcNotaInv;

//#ifdef EVOLUTIVA_POLO_2014
	// 09/12/14 Mappatura codice edizione dewey (evolutiva POLO)
	CFile* tbcEclaIn;
	CFile* tbcEclaOffsetIn;
	long elementsEcla;
	char *offsetBufferEclaPtr;
	TbcNotaInv *EclaInv;

	// Localizzazioni 977 (POLO)
	// ------------------------
	CFile* tb977In;
	CFile* tb977OffsetIn;
	long elementsTb977;
	char *offsetBufferTb977Ptr;
	Tb977 *tb977;


//#endif

	// Authority Impronta
	// ------------------------
	CFile* tbImprontaIn;
	CFile* tbImprontaOffsetIn;
	long elementsTbImpronta;	// Numero di impronte
	char *offsetBufferTbImprontaPtr;
	TbImpronta *tbImpronta;


	// Authority Luogo
	// ------------------------
	//CString tbLuogoFilename;
	CFile* tbLuogoIn;
	//CString tbLuogoOffsetFilename;
	CFile* tbLuogoOffsetIn;
	long elementsTbLuogo;
	char *offsetBufferTbLuogoPtr;
	TbLuogo *tbLuogo;


	// Entita' Grafica
	// ------------------------
	//CString tbGraficaFilename;
	CFile* tbGraficaIn;
	//CString tbGraficaOffsetFilename;
	CFile* tbGraficaOffsetIn;
	long elementsTbGrafica;
	char *offsetBufferTbGraficaPtr;
	TbGrafica *tbGrafica;


	// Entita' cartografia
	// ------------------------
	//CString tbCartografiaFilename;
	CFile* tbCartografiaIn;
	//CString tbCartografiaOffsetFilename;
	CFile* tbCartografiaOffsetIn;
	long elementsTbCartografia;
	char *offsetBufferTbCartografiaPtr;
	TbCartografia *tbCartografia;


	// Entita' musica
	// ------------------------
	//CString tbMusicaFilename;
	CFile* tbMusicaIn;
	//CString tbMusicaOffsetFilename;
	CFile* tbMusicaOffsetIn;
	long elementsTbMusica;
	char *offsetBufferTbMusicaPtr;
	TbMusica *tbMusica;


	// Entita' composizione
	// ------------------------
	//CString tbComposizioneFilename;
	CFile* tbComposizioneIn;
	//CString tbComposizioneOffsetFilename;
	CFile* tbComposizioneOffsetIn;
	long elementsTbComposizione;
	char *offsetBufferTbComposizionePtr;
	TbComposizione *tbComposizione;

	// Entita' incipit
	// ------------------------
	//CString tbIncipitFilename;
	CFile* tbIncipitIn;
	//CString tbIncipitOffsetFilename;
	CFile* tbIncipitOffsetIn;
	long elementsTbIncipit;
	char *offsetBufferTbIncipitPtr;
	TbIncipit *tbIncipit;

	// Entita' Personaggi e interpreti (MAT. MUSICALE)
	// ------------------------
	//CString tbPersonaggioFilename;
	CFile* tbPersonaggioIn;
	//CString tbPersonaggioOffsetFilename;
	CFile* tbPersonaggioOffsetIn;
	long elementsTbPersonaggio;
	char *offsetBufferTbPersonaggioPtr;
	TbPersonaggio *tbPersonaggio;

	// Entita' rappresentazione
	// ------------------------
	//CString tbRappresentFilename;
	CFile* tbRappresentIn;
	//CString tbRappresentOffsetFilename;
	CFile* tbRappresentOffsetIn;
	long elementsTbRappresent;
	char *offsetBufferTbRappresentPtr;
	TbRappresent *tbRappresent;


	// Entita' ordini
	// ------------------------
	//CString tbaOrdiniFilename;
	CFile* tbaOrdiniIn;
	//CString tbaOrdiniOffsetFilename;
	CFile* tbaOrdiniOffsetIn;
	long elementsTbaOrdini;
	char *offsetBufferTbaOrdiniPtr;
	TbaOrdini *tbaOrdini;




	// Relazioni titolo->titolo (.rel)
	// ------------------------
	//CString trTitTitRelFilename;
	CFile* trTitTitRelIn;
	CFile* trTitTitRelOffsetIn;
	long elementsTrTitTitRel;	// Numero di relazioni
	char *offsetBufferTrTitTitRelPtr;

	TrTitTit *trTitTit;


	// Relazioni INVERSE titolo->titolo (.rel)
	// ------------------------
	//CString trTitTitInvRelFilename;
	CFile* trTitTitInvRelIn;


	//CString trTitTitInvRelOffsetFilename;
	CFile* trTitTitInvRelOffsetIn;

	long elementsTrTitTitInvRel;	// Numero di relazioni
	char *offsetBufferTrTitTitInvRelPtr;





	// Relazioni titolo->titolo
	// ------------------------
	//CString trTitTitFilename;
	CFile* trTitTitIn;
	//CString trTitTitOffsetFilename;
	CFile* trTitTitOffsetIn;
	long elementsTrTitTit;	// Numero di relazioni
	char *offsetBufferTrTitTitPtr;


	// Relazioni titolo->biblioteca
	// ------------------------
	//CString trTitBibFilename;
	CFile* trTitBibIn;
	//CString trTitBibOffsetFilename;
	CFile* trTitBibOffsetIn;
	long elementsTrTitBib;
	char *offsetBufferTrTitBibPtr;
	TrTitBib *trTitBib;
	int trTitBibKeyPlusOffsetPlusLfLength;


	// Relazioni titolo->autore
	// ------------------------
	CFile* trTitAutRelIn;
	CFile* trTitAutRelOffsetIn;
	long elementsTrTitAutRel;	// Numero di relazioni
	char *offsetBufferTrTitAutRelPtr;
	TrTitAutRel *trTitAutRel;

	CFile* trTitAutIn;
	CFile* trTitAutOffsetIn;
	long elementsTrTitAut;	// Numero di relazioni
	char *offsetBufferTrTitAutPtr;
	TrTitAut *trTitAut;


	// Entita autore->autore inverse 08/10/2009 13.58
	// ------------------------
	CFile* trAutAutInvIn;
	CFile* trAutAutInvOffsetIn;
	long elementsTrAutAutInv;	// Numero di relazioni
	char *offsetBufferTrAutAutInvPtr;
	TrAutAut *trAutAutInv;



	// Relazioni autore->autore
	// ------------------------
	CFile* trAutAutRelIn;
	CFile* trAutAutRelOffsetIn;
	long elementsTrAutAutRel;	// Numero di relazioni
	char *offsetBufferTrAutAutRelPtr;


	// Relazioni inveerse autore->autore (per tipo legame 4)
	// ------------------------
	CFile* trAutAutRelInvIn;
	CFile* trAutAutRelInvOffsetIn;
	long elementsTrAutAutInvRel;	// Numero di relazioni
	char *offsetBufferTrAutAutRelInvPtr;


	// Relazioni inveerse titolo->autore
	// ------------------------
	CFile* trTitAutRelInvIn;
	CFile* trTitAutRelInvOffsetIn;
	long elementsTrTitAutInvRel;	// Numero di relazioni
	char *offsetBufferTrTitAutRelInvPtr;
	TrTitAutRel *trTitAutRelInv;




	// Authority Repertorio
	// ------------------------
	CFile* tbRepertorioIn;
	CFile* tbRepertorioOffsetIn;
	long elementsTbRepertorio;	// Numero di classi
	char *offsetBufferTbRepertorioPtr;
	TbRepertorio *tbRepertorio;


	// Legami Autori/Repertorio
	// ------------------------
	CFile* trRepAutIn;
	CFile* trRepAutOffsetIn;
	long elementsTrRepAut;	// Numero di classi
	char *offsetBufferTrRepAutPtr;
	TrRepAut *trRepAut;

	// Legami Marca/Repertorio
	// ------------------------
	CFile* trRepMarIn;
	CFile* trRepMarOffsetIn;
	long elementsTrRepMar;	// Numero di classi
	char *offsetBufferTrRepMarPtr;
	TrRepMar *trRepMar;

	// Legami Personaggi/Interpreti
	// ----------------------------
	CFile* trPerIntIn;
	CFile* trPerIntOffsetIn;
	long elementsTrPerInt;
	char *offsetBufferTrPerIntPtr;
	TrPerInt *trPerInt;




	// Relazioni titolo->collocazione (950)
	// ------------------------------------

	//CString tb950InvOutFilename;
	CFile* tb950InvOutFilenameIn;
	//CString tb950InvOutOffsetFilename;
	CFile* tb950InvOutOffsetFilenameIn;
	long elementsTb950Inv;	// Numero di elementi
	char *offsetBufferTb950InvPtr;
	Tb950Inv *tb950Inv;

	//CString tb950CollOutFilename;
	CFile* tb950CollOutFilenameIn;
	//CString tb950CollOutOffsetBidFilename;
	CFile* tb950CollOutOffsetBidFilenameIn;
	//CString tb950CollOutOffsetKLocFilename;
	CFile* tb950CollOutOffsetKLocFilenameIn;
	long elementsTb950BidColl;	// Numero di elementi
	long elementsTb950KeylocColl;	// Numero di elementi
	char *offsetBufferTb950CollBidPtr;
	char *offsetBufferTb950CollKLocPtr;
	Tb950Coll *tb950Coll;

	//CString tb950EseOutFilename;
	CFile* tb950EseOutFilenameIn;
	//CString tb950EseOutOffsetFilename;
	CFile* tb950EseOutOffsetFilenameIn;
	long elementsTb950Ese;	// Numero di elementi
	char *offsetBufferTb950EsePtr;
	Tb950Ese *tb950Ese;

	// Relazioni titolo->soggetto
	// ------------------------
	//CString trTitSogBibFilename;
	CFile* trTitSogBibIn;
	CFile* trTitSogBibOffsetIn;
	long elementsTrTitSogBib;	// Numero di relazioni
	char *offsetBufferTrTitSogBibPtr;

	// Relazioni titolo->classe
	// ------------------------
	//CString trTitClaFilename;
	CFile* trTitClaIn;
	CFile* trTitClaOffsetIn;
	long elementsTrTitCla;	// Numero di relazioni
	char *offsetBufferTrTitClaPtr;
	TrTitCla *trTitCla;


	// Relazioni titolo->marca
	// ------------------------
	//CString trTitMarFilename;
	CFile* trTitMarRelIn;
	CFile* trTitMarRelOffsetIn;
	long elementsTrTitMarRel;
	char *offsetBufferTrTitMarRelPtr;
	TrTitMar *trTitMarRel; 	// TrTitMar e' impropriamente usata


	// Tabella titolo->marca
	// ------------------------
	//CString trTitMarFilename;
	CFile* trTitMarTbIn;
	CFile* trTitMarTbOffsetIn;
	long elementsTrTitMarTb;
	char *offsetBufferTrTitMarTbPtr;
	TrTitMar *trTitMarTb;




	// Relazioni titolo->luogo
	// ------------------------
	//CString trTitLuoFilename;
	CFile* trTitLuoIn;
	CFile* trTitLuoOffsetIn;
	long elementsTrTitLuo;	// Numero di relazioni
	char *offsetBufferTrTitLuoPtr;
	TrTitLuo *trTitLuo;

	// Relazioni titolo->altro numero (eg. OCLC Number)
	// ------------------------
	//CString trTitLuoFilename;
	CFile* trBidAltroidIn;
	CFile* trBidAltroidOffsetIn;
	long elementsTrBidAltroid;	// Numero di relazioni
	char *offsetBufferTrBidAltroidPtr;
	TrBidAltroid *trBidAltroid;



	// Tabella possessori e provenienza 26/01/2010 17.14
	// --------------------------------
	CFile* tbcPossessoreProvenienzaIn;
	CFile* tbcPossessoreProvenienzaOffsetIn;
	long elementsTbcPossessoreProvenienza;
	char *offsetBufferTbcPossessoreProvenienzaPtr;
	TbcPossessoreProvenienza *tbcPossessoreProvenienza;

	// Tabella di relazione tra possessori-provenienza ed inventari 26/01/2010 17.14
	CFile* trcPossProvInventariIn;
	CFile* trcPossProvInventariOffsetIn;
	long elementsTrcPossProvInventari;
	char *offsetBufferTrcPossProvInventariPtr;
	TrcPossProvInventari *trcPossProvInventari;




	// File dei reticoli
	CFile* reticoliOut;

	int keyPlusOffsetPlusLfLength;	// Lungezza del campo Chiave/Offset
	int titoloKeyPlusOffsetPlusLfLength;
	int titbibKeyPlusOffsetPlusLfLength;
	int classeKeyPlusOffsetPlusLfLength;	// Lungezza del campo Chiave/Offset
	int trKeyPlusOffsetPlusLfLength;
	int trTitAutKeyPlusOffsetPlusLfLength;
	int trBidAltroidKeyPlusOffsetPlusLfLength;
	int	sezioneDiColocazioneKeyPlusOffsetPlusLfLength; // 05/02/2015
	int	bibliotecaKeyPlusOffsetPlusLfLength;
	int notaInvKeyPlusOffsetPlusLfLength;
	int inventarioKeyPlusOffsetPlusLfLength;
	int keyloctrKeyPlusOffsetPlusLfLength;
	int offsetPlusLfLength;
	int titset1KeyPlusOffsetPlusLfLength;
	int titset2KeyPlusOffsetPlusLfLength;


	bool loadOffsetFiles();
	void loadOffsetFiles2(CFile *tbTitoloOffsetIn, char *ptr);

	void closeOffsetFiles();
//	bool binarySearch(char* offsetMapPtr, long elements, int rowLength, char *keyToSearch, int keyLength, long& position, char ** entryPtr);

	void print_tree_rev_post(const tree<std::string>& tr, tree<std::string>::post_order_iterator it, tree<std::string>::post_order_iterator end);
	void print_tree_rev(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end);
	void print_tree_post(const tree<std::string>& tr, tree<std::string>::post_order_iterator it, tree<std::string>::post_order_iterator end);
	void print_tree(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end);
	void print_children(const tree<std::string>& tr);

	void CreaReticoloAddNodeChildren(tree<std::string>* reticolo, tree<std::string>::pre_order_iterator startNodeIter, const char* bid, const char* level, int levelPos);
	void CreaReticoloAutoreAddNodeChildren(tree<std::string>* reticolo, tree<std::string>::pre_order_iterator startNodeIter, const char* bid, const char* level, int levelPos);
	void CreaReticoloSoggettoAddNodeChildren(tree<std::string>* reticolo, tree<std::string>::pre_order_iterator startNodeIter, const char* bid, const char* level, int levelPos);
	void CreaReticoloTitoloUniformeAddNodeChildren(tree<std::string>* reticolo, tree<std::string>::pre_order_iterator startNodeIter, const char* bid, const char* level, int levelPos); // 11/04/2016

	void writeReticolo(const tree<std::string>& tr, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end);

	bool creaRecordBibliografico(MarcRecord *marcRecord, const tree<std::string>& reticolo, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end);
	bool creaRecordAuthority(MarcRecord *marcRecord, const tree<std::string>& reticolo, tree<std::string>::pre_order_iterator it, tree<std::string>::pre_order_iterator end);

	void getEntityId(char *entryReticolo, char *bid);

	void marc4CppMallinfo();

	void zeroInit();
	void stop();
	bool elaboraLeader();
	bool  loadTbfBibliotecaInPoloRecords(char *tbfBibliotecaFilename, char *polo);
	bool  loadTbcSezioneCollocazione(char *polo); // char *tbcSezioneCollocazioneFilename,
	void dumpRecordUnimarcTxt();

	char  elaboraLivelloGerarchicoNotizia(char *bid);
	char livelloGerarchico;

	CKeyValueVector *sezioniDiCollocazioneDaNonMostrareIn960KV;
	CKeyValueVector *bibliotecheDaNonMostrareIn950KV;
	void ricostruisci200perM20(char *bid);
	void ricostruisci300_con_321();
	void ricostruisci101();

public:
	CFile* idXunimarcIn; // File dei bid da trattare
	long recordsNonDocumento;

	Marc4cpp();
	virtual ~Marc4cpp();
	bool setupAuthDocument(
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
			CKeyValueVector *bibliotecheDaNonMostrareIn950KV
	);

	bool setupAuthAutori(
			char *tagsToGenerateBufferPtr,
			int tagsToGenerate,
			char *marcFilename,
			char *marcTxtFilename,
			char *polo,
			ATTValVector<CString *> *entitaVector, ATTValVector<CString *> *relazioniVector, ATTValVector<CString *> *offsetVector,
			char *bidXunimarcFilename,
			char *reticoliFilename,
			bool exportViaf,
			int exportMaxViafs
	);
	bool setupAuthSoggetti(
			char *tagsToGenerateBufferPtr,
			int tagsToGenerate,
			char *marcFilename,
			char *marcTxtFilename,
			char *polo,
			ATTValVector<CString *> *entitaVector,
			ATTValVector<CString *> *relazioniVector,
			ATTValVector<CString *> *offsetVector,
			char *bidXunimarcFilename,
			char *reticoliFilename
	);
	bool setupAuthTitoliUniformi(
			char *tagsToGenerateBufferPtr,
			int tagsToGenerate,
			char *marcFilename,
			char *marcTxtFilename,
			char *polo,
			ATTValVector<CString *> *entitaVector,
			ATTValVector<CString *> *relazioniVector,
			ATTValVector<CString *> *offsetVector,
			char *bidXunimarcFilename,
			char *reticoliFilename
	);

	bool setupCommon(
			char *tagsToGenerateBufferPtr,
			int tagsToGenerate,
			char *marcFilename,
			char *marcTxtFilename,
			char *marcXmlFilename,
//			char *polo,
//			char *descPolo,
			ATTValVector<CString *> *entitaVector,
			ATTValVector<CString *> *relazioniVector,
			ATTValVector<CString *> *offsetVector,
			ATTValVector<CString *> *codiciVector,

			char *bidXunimarcFilename,
			char *reticoliFilename,
			char *bidErratiFilename);


	//bool loadCNOTcodes();
	bool loadDictionary_KV(const char* filename, CKeyValueVector * kvVector);

	void testReticolo();
	tree<std::string> *creaReticolo(char * bid);
//	tree<std::string> *creaReticoloAuthorityAutore(char * id);
	tree<std::string> *creaReticoloAuthority(char * id);

	//void creaReticoli();
	void testBinarySearch();

	bool creaRecordUnimarcBibliografico(char *bid);
	bool creaRecordUnimarcAuthority(char *id);

	bool writeRecordUnimarc();
	bool writeRecordUnimarcTxt();
	bool writeRecordUnimarcXml();


	bool CreaUnimarcBibliografico(int recordUnimarcSuSingolaRiga, long iniziaElaborazioneDaRiga, bool positionByOffset, long elaboraNRighe, int logOgniXRighe);
	bool CreaUnimarcAuthority(long iniziaElaborazioneDaRiga, bool positionByOffset, long elaboraNRighe, int logOgniXRighe);

	void logSeek();

	void trovaAltraFormaAccettata(const char *vid, tree<std::string>* reticolo, tree<std::string>::pre_order_iterator startNodeIter, const char* level, int leafId);

};






	// 250
	// 260
	// 400 - 500 Autore
	// 410 - 510 Ente
	// 460 - 560 Luogo

	/* 461 - 462 - 463
	 * si imposta 462 quando il titolo di arrivo del legame � un livello intermedio (ICCU 26.2.2004)
	 * si imposta 463 quando il legame il titolo base � uno spoglio (natura N)
	 */
	// 790 - 791
/*
	// 801 Fonte di provenienza del record
	Obbligatorio se proviene da catalogazione derivata
	Ripetibile
	Indicatore nella prima posizione: non definito
	Indicatore nella seconda posizione: Function indicator
	    0 Agenzia responsabile della creazione
	    1 Agenzia responsabile della conversione in formato macchina
	    2 Agenzia responsabile della modifica
	    3 Agenzia responsabile della distribuzione
	Sottocampi:
	    a - Paese dell'Agenzia
	    b - Nome dell'Agenzia
	    c - Data della transazione
	    g - Regole di catalogazione
*/
	// 810 - 815 Repertorio
/*
	// 856 Localizzazione risorsa elettronica e accesso
	Indicatore nella prima posizione: Metodo di accesso
	    # Nessuna informazione
	    0 e-mail
	    1 FTP
	    2 Remote login (Telnet)
	    3 Dial-up
	    4 HTTP
	    7 Metodo specificato nel sottocampo $2
	Indicatore nella seconda posizione: Relationship
	    # Nessuna informazione
	    0 Risorsa
	    1 Versione della risorsa
	    2 Relativo alla risorsa
	    8 Nessun metodo di generazione automatica

	Sottocampi:
	    $a � Nome dell�host
	    $b � Numero di accesso
	    $c - Informazioni sulla compressione
	    $d - Path
	    $f � Nome elettronico
	    $g - Nome uniforme della risorsa
	    $h - Processor of request
	    $i - Istruzioni
	    $j - Bits per secondo
	    $k - Password
	    $l - Logon/login
	    $m - Contatto per assistanza
	    $n - Nome dell'indirizzo dell'host del sottocampo $a
	    $o - Sistema operativo
	    $p - Porta
	    $q - Tipo del formato elettronico
	    $r - Configirazione (settaggio)
	    $s - Grandezza del file
	    $t - Emulatore di terminale
	    $u - Uniform Resource Locator (URL)
	    $v - Orario di accesso
	    $w - Numero del record di controllo
	    $x - Nota riservata
	    $z - Nota pubblica (OPAC)
	    $2 - Molalit� di accesso
	    $3 - Materiale specificato
	    $6 - Linkage
*/
	// 899

	// 921
	// 923
	// 926
	// 927
	// 928
	// 929
	// 930
	// 931
	// 932 Descrittore
	// 950





#endif /* MARC4CPP_H_ */
