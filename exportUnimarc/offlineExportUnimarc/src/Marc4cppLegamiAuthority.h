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
 * Marc4cppLegamiAuthority.h
 *
 *  Created on: 20-jul-2009
 *      Author: Arge
 */


#ifndef MARC4CPPLEGAMIAUTHORITY_H_
#define MARC4CPPLEGAMIAUTHORITY_H_

#include "MarcRecord.h"
#include "TbAutore.h"
#include "TbCodici.h"
#include "library/tree.hh"
#include "library/CKeyValueVector.h"
//#include "Marc4cppDocumento.h"
#include "library/tvvector.h"
#include "library/CKeyValueVector.h"
#include "library/CFile.h"
#include "Marc4cppLegami.h"
#include "TrRepAut.h"
#include "TbRepertorio.h"
#include "TbSoggetto.h"
#include "TbDescrittore.h"
#include "TrSogDes.h"
#include "TrDesDes.h"

#include "library/CTokenizer.h"
#include "MarcGlobals.h"
#include "Marc4cppDocumentoAuthority.h"


class Marc4cppLegamiAuthority {
	Marc4cppDocumentoAuthority *marc4cppDocumentoAuthority;
	CKeyValueVector *legamiVectorKV;
	CKeyValueVector *tbCodiciKV;
	int authority;
	tree<std::string>::sibling_iterator siArg; // causa problemi di memoria provo a passare non tramite argument

	MarcRecord 		*marcRecord;
	//Marc4cppLegami *marc4cppLegami;

    char rootId[10+1];

    TbTitolo 		*tbTitolo;

	TbAutore		*tbAutore;
	TbSoggetto		*tbSoggetto;
	TbDescrittore	*tbDescrittore;
	CKeyValueVector *tbfBibliotecaKV; //*descBibKV, *anagrafeBibKV;

	TrRepAut *trRepAut;
	TbRepertorio *tbRepertorio;
	TrSogDes *trSogDes;
	TrDesDes *trDesDes;
	TrDesDes *trDesDesInv;

	CFile* trAutAutIn;
	CFile* trAutAutOffsetIn;
	char* offsetBufferTrAutAutPtr;
	long elementsTrAutAut;

	CFile* trTitAutRelInvIn;
	CFile* trTitAutRelInvOffsetIn;
	char* offsetBufferTrTitAutRelInvPtr;
	long elementsTrTitAutInvRel;

	// 12/04/2016
	CFile* trTitAutRelIn;
	CFile* trTitAutRelOffsetIn;
	char* offsetBufferTrTitAutRelPtr;
	long elementsTrTitAutRel;



	int keyPlusOffsetPlusLfLength;
	int classeKeyPlusOffsetPlusLfLength;
	int trKeyPlusOffsetPlusLfLength;
	int key_length;

	tree<std::string> reticolo;
	void getEntityId(char *entryReticolo, char *id);

	void creaLegamiTitoloTitolo();
	DataField * creaLegameTitoloTitolo(char *entryReticoloPtr, int pos);

	void creaLegamiTitoloAutore(bool isTitoloOpera);
	DataField * creaLegameTitoloAutore(char *entryReticoloPtr, int pos, bool isTitoloOpera);

	void creaLegamiAutoreAutore();
	void creaLegamiSoggettoDescrittori();
	void creaTag931_LegameSoggettoDescrittore();
	void creaLegamiDescrittoriDescrittori(char *didBase);
	void creaTag932_LegameDescrittoreDescrittore(CString *tpLegame, CString *didBase);


	DataField * creaLegameAutoreAutore(char *entryReticoloPtr, int pos);
	DataField * creaTag400_RinvioAutorePersonale(TbAutore * tbAutoreRinvio);
	DataField * creaTag410_RinvioAutoreCollettivo(TbAutore * tbAutoreRinvio);
	DataField * creaTag500_VediAncheAutorePersonale(TbAutore * tbAutoreRinvio);
	DataField * creaTag510_VediAncheAutoreCollettivo(TbAutore * tbAutoreRinvio);


	DataField * creaTag_81xRepertorio(char *tag);

	void creaLegamiAutoreRepertori();


	void creaTag_40x(DataField *df, TbAutore * tbAutoreRinvio);
	void creaTag_41x(DataField *df, TbAutore * tbAutoreRinvio);

	void init();
	void initLicr();
	void stop();
	void clearLegamiVector();
	void clearTbCodiciVEctor();
	bool haResponsabilitaPrincipale(const tree<std::string>& tr);


public:
	Marc4cppLegamiAuthority(Marc4cppDocumentoAuthority *marc4cppDocumentoAuthority,
			MarcRecord *marcRecord, // per autori
			TbAutore *tbAutore,
			TrRepAut *trRepAut,
			TbRepertorio *tbRepertorio,
			CFile* trAutAutIn, CFile* trAutAutOffsetIn,	char* offsetBufferTrAutAutPtr, long elementsTrAutAut,
			CFile* trTitAutRelInvIn, CFile* trTitAutRelInvOffsetIn, char* offsetBufferTrTitAutRelInvPtr, long elementsTrTitAutInvRel,


			CKeyValueVector *tbfBibliotecaKV,
			int keyPlusOffsetPlusLfLength,
			int trKeyPlusOffsetPlusLfLength,
			int key_length,
			int authority,
			TbTitolo *tbTitolo
			);
	Marc4cppLegamiAuthority(Marc4cppDocumentoAuthority *marc4cppDocumentoAuthority,
			MarcRecord *marcRecord, // per soggetti
			TbSoggetto *tbSoggetto,
			TbDescrittore *tbDescrittore,
			TrSogDes *trSogDes,
			TrDesDes *trDesDes,
			TrDesDes *trDesDesInv,
			CKeyValueVector *tbfBibliotecaKV,
			int keyPlusOffsetPlusLfLength,
			int trKeyPlusOffsetPlusLfLength,
			int key_length,
			int authority);


	Marc4cppLegamiAuthority(Marc4cppDocumentoAuthority *marc4cppDocumentoAuthority,
			MarcRecord *marcRecord,
			TbTitolo *tbTitolo,
			TbAutore *tbAutore,

			TrRepAut *trRepAut,
			TbRepertorio *tbRepertorio,

			CFile* trTitAutRelIn, CFile* trTitAutRelOffsetIn, char* offsetBufferTrTitAutRelPtr, long elementsTrTitAutRel,
			CKeyValueVector *tbfBibliotecaKV,
			int keyPlusOffsetPlusLfLength,
			int trKeyPlusOffsetPlusLfLength,
			int key_length,
			int authority
			);



	virtual ~Marc4cppLegamiAuthority();
	bool elaboraDatiLegamiAuthority(const tree<std::string>& reticolo, bool isTitoloOpera);
	bool elaboraDatiViaf(char *id, int exportMaxViafs);
	bool getTitoliViafConResponabilita(CString *sPtr, char responsabilita, CKeyValueVector *cdLivelloVidIsbdKV, int exportMaxViafs);
	bool getTitoliUniformiViaf(CString *sPtr, char responsabilita, CKeyValueVector *cdLivelloVidIsbdKV, int exportMaxViafs);

};

#endif /* MARC4CPPLEGAMIAUTHORITY_H_ */
